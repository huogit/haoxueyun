package com.prestrive.com.service.ucenter.service;

import com.prestrive.com.service.base.dto.MemberDto;
import com.prestrive.com.service.ucenter.entity.Member;
import com.baomidou.mybatisplus.extension.service.IService;
import com.prestrive.com.service.ucenter.entity.vo.*;

import java.util.HashMap;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author strive
 * @since 2022-07-02
 */
public interface MemberService extends IService<Member> {

    /**
     * 会员注册
     * @param registerVo
     */
    void register(RegisterVo registerVo);

    /**
     * 会员登录
     * @param loginVo
     * @param remoteAddr
     * @return
     */
    String login(LoginVo loginVo, String remoteAddr);

    Member selectByOpenId(String openid);

    /**
     * 保存 微信用户
     * @param userInfo
     * @return
     */
    Member saveByUserInfo(HashMap<String, Object> userInfo);

    /**
     * 获取 MemberDto
     * @param memberId
     * @return
     */
    MemberDto getMemberDtoByMemberId(String memberId);

    /**
     * 根据日期统计注册人数
     * @param day
     */
    Integer findRegisterCountByDay(String day);

    /**
     * 更新会员基本信息
     * @param MemberId
     * @return
     */
    boolean updateMemberById(String MemberId, WebMemberVo webMemberVo);

    /**
     * 更改手机号
     * @param webMobileVo
     * @param memberId
     * @return
     */
    boolean updateMobile(WebMobileVo webMobileVo, String memberId);

    /**
     * 更改密码
     * @param webPasswordVo
     * @param id
     * @return
     */
    boolean updatePassword(WebPasswordVo webPasswordVo, String id);
}
