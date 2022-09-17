package com.prestrive.com.service.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.prestrive.com.common.base.result.ResultCodeEnum;
import com.prestrive.com.common.base.util.FormUtils;
import com.prestrive.com.common.base.util.JwtInfo;
import com.prestrive.com.common.base.util.JwtUtils;
import com.prestrive.com.common.base.util.MD5;
import com.prestrive.com.service.base.dto.MemberDto;
import com.prestrive.com.service.base.exception.GlobalException;
import com.prestrive.com.service.ucenter.entity.Member;
import com.prestrive.com.service.ucenter.entity.vo.*;
import com.prestrive.com.service.ucenter.mapper.MemberMapper;
import com.prestrive.com.service.ucenter.service.LoginLogService;
import com.prestrive.com.service.ucenter.service.MemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author strive
 * @since 2022-07-02
 */
@Transactional(rollbackFor = Exception.class)
@Slf4j
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    MemberMapper memberMapper;
    @Autowired
    LoginLogService loginLogService;

    @Value("${JwtToken.expire}")
     Integer expire;

    @Override
    public void register(RegisterVo registerVo) {
        String code = registerVo.getCode();
        String mobile = registerVo.getMobile();
        String nickName = registerVo.getNickname();
        String password = registerVo.getPassword();

        //校验参数
        if(!StringUtils.hasLength(code) ||
                !StringUtils.hasLength(mobile) ||
                !StringUtils.hasLength(nickName) ||
                !StringUtils.hasLength(password)){
            throw new GlobalException(ResultCodeEnum.PARAM_ERROR);
        }

        //校验验证码
        //System.out.println(redisTemplate.opsForValue().get(mobile).toString());
        if(!redisTemplate.hasKey(mobile) || !code.equals(redisTemplate.opsForValue().get(mobile).toString())){
            throw new GlobalException(ResultCodeEnum.CODE_ERROR);
        }
        //System.out.println(redisTemplate.opsForValue().get(mobile).toString());
        //是否被注册
        QueryWrapper<Member> memberQueryWrapper = new QueryWrapper<>();
        memberQueryWrapper.eq("mobile",mobile);
        Integer count = memberMapper.selectCount(memberQueryWrapper);
        if(count > 0){
            throw new GlobalException(ResultCodeEnum.MOBILE_REGISTER_ERROR);
        }

        //注册
        Member member = new Member();
        member.setMobile(mobile);
        member.setNickname(nickName);
        member.setPassword(MD5.encrypt(password));
        member.setDisabled(false);
        //默认头像
        member.setAvatar("https://edu-file-2022-5-1.oss-cn-beijing.aliyuncs.com/avatar/default.png");
        memberMapper.insert(member);
    }

    @Override
    public String login(LoginVo loginVo, String remoteAddr) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        //校验参数
        if(StringUtils.isEmpty(mobile)
                || !FormUtils.isMobile(mobile)
                || StringUtils.isEmpty(password)){
            throw new GlobalException(ResultCodeEnum.PARAM_ERROR);
        }

        //校验手机号是否已注册
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",mobile);
        Member member = memberMapper.selectOne(queryWrapper);
        if(member == null){
            throw new GlobalException(ResultCodeEnum.LOGIN_MOBILE_ERROR);
        }

        //校验密码
        if(!member.getPassword().equals(MD5.encrypt(password))){
            throw new GlobalException(ResultCodeEnum.LOGIN_PASSWORD_ERROR);
        }

        JwtInfo jwtInfo = new JwtInfo();
        jwtInfo.setId(member.getId());
        jwtInfo.setAvatar(member.getAvatar());
        jwtInfo.setNickname(member.getNickname());

        //记录登录日志
        loginLogService.createOrUpdateByMemberId(member.getId(),remoteAddr);

        String jwtToken = JwtUtils.getJwtToken(jwtInfo, expire * 2 * 24 * 30);

        return jwtToken;
    }

    @Override
    public Member selectByOpenId(String openid) {

        QueryWrapper<Member> memberQueryWrapper = new QueryWrapper<>();
        memberQueryWrapper.eq("openid",openid);
        return memberMapper.selectOne(memberQueryWrapper);
    }

    @Override
    public  Member  saveByUserInfo(HashMap<String, Object> userInfo) {
        String openid = userInfo.get("openid").toString();
        String nickname = userInfo.get("nickname").toString();
        String headimgurl = userInfo.get("headimgurl").toString();
        Double sex = (Double)userInfo.get("sex");

        Member member = new Member();
        member.setOpenid(openid);
        member.setNickname(nickname);
        member.setAvatar(headimgurl);
        member.setSex(sex.intValue());
        memberMapper.insert(member);

        return member;
    }

    @Override
    public MemberDto getMemberDtoByMemberId(String memberId) {
        Member member = memberMapper.selectById(memberId);

        MemberDto memberDto = new MemberDto();
        BeanUtils.copyProperties(member,memberDto);

        return memberDto;
    }

    @Override
    public Integer findRegisterCountByDay(String day) {
        Integer count = memberMapper.selectRegisterNumByDay(day);

        return count;
    }

    @Override
    public boolean updateMemberById(String MemberId, WebMemberVo webMemberVo) {
        Member member = new Member();
        BeanUtils.copyProperties(webMemberVo,member);
        member.setId(MemberId);

        int result = memberMapper.updateById(member);

        return  result > 0;
    }

    @Override
    public boolean updateMobile(WebMobileVo webMobileVo, String memberId) {
        String mobile = webMobileVo.getMobile();
        String code = webMobileVo.getCode();

        //校验参数
        if(!StringUtils.hasLength(code) ||
                StringUtils.isEmpty(mobile)
                || !FormUtils.isMobile(mobile)
                ){
            throw new GlobalException(ResultCodeEnum.PARAM_ERROR);
        }
        //校验验证码
        //System.out.println(redisTemplate.opsForValue().get(mobile).toString());
        if(!redisTemplate.hasKey(mobile) || !code.equals(redisTemplate.opsForValue().get(mobile).toString())){
            throw new GlobalException(ResultCodeEnum.CODE_ERROR);
        }
        //校验手机号是否已被注册
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",mobile);
        Integer count = memberMapper.selectCount(queryWrapper);
        if(count > 0){
            throw new GlobalException(ResultCodeEnum.MOBILE_REGISTER_ERROR);
        }
        //获取原来手机号
        Member member = memberMapper.selectById(memberId);
        if(member == null){
            return false;
        }
        member.setMobile(mobile);

        int result = memberMapper.updateById(member);
        return result > 0;
    }

    @Override
    public boolean updatePassword(WebPasswordVo webPasswordVo, String id) {
        String oldPassword = webPasswordVo.getOldPassword();
        String newPassword = webPasswordVo.getNewPassword();

        //校验参数
        if (StringUtils.isEmpty(oldPassword)
                || StringUtils.isEmpty(newPassword)) {
            throw new GlobalException(ResultCodeEnum.PARAM_ERROR);
        }

        Member member = memberMapper.selectById(id);
        //校验原密码是否正确
        if (!member.getPassword().equals(MD5.encrypt(oldPassword))) {
            throw new GlobalException(ResultCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        member.setPassword(MD5.encrypt(newPassword));
        int result = memberMapper.updateById(member);

        return result > 0;
    }


    public static void main(String[] args) {

        Member member = new Member();
        member.setOpenid("openid");
        member.setNickname("nickname");
        member.setAvatar("headimgurl");


        JwtInfo jwtInfo = new JwtInfo();

        BeanUtils.copyProperties(member,jwtInfo);

        log.info(jwtInfo.toString());
    }




}
