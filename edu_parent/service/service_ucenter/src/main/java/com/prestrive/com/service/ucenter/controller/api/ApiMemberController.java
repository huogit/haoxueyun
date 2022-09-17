package com.prestrive.com.service.ucenter.controller.api;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.prestrive.com.common.base.result.R;
import com.prestrive.com.common.base.result.ResultCodeEnum;
import com.prestrive.com.common.base.util.JwtInfo;
import com.prestrive.com.common.base.util.JwtUtils;
import com.prestrive.com.service.base.dto.MemberDto;
import com.prestrive.com.service.base.exception.GlobalException;
import com.prestrive.com.service.ucenter.entity.Member;
import com.prestrive.com.service.ucenter.entity.vo.*;
import com.prestrive.com.service.ucenter.service.LoginLogService;
import com.prestrive.com.service.ucenter.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author strive
 * @since 2022-07-02
 */
@Api(description = "会员管理")
@RestController
@RequestMapping("/api/ucenter/member")
//@CrossOrigin
@Slf4j
public class ApiMemberController {

    @Autowired
    MemberService memberService;

    @ApiOperation("会员注册")
    @PostMapping("/register")
    public R register(@ApiParam(value = "注册数据表单",required = true)
                      @RequestBody RegisterVo registerVo){
        memberService.register(registerVo);
        return R.ok().message("注册成功");
    }

    @ApiOperation("会员登录")
    @PostMapping("/login")
    public R login(@ApiParam(value = "手机号和密码",required = true)
                       @RequestBody LoginVo loginVo , HttpServletRequest request){

        String remoteAddr = "";
        if (request.getHeader("x-forwarded-for") == null) {
            remoteAddr = request.getRemoteAddr();
        }else {
            remoteAddr = request.getHeader("x-forwarded-for");
        }

        remoteAddr = remoteAddr.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1":remoteAddr;
        String token = memberService.login(loginVo,remoteAddr);


      return R.ok().data("jwtToken",token);
    }

    @ApiOperation("根据token获取登录信息")
    @GetMapping("/auth/get-login-info")
    public R getLoginInfo(HttpServletRequest request ,@RequestHeader("token") String headToken){
        try {

            log.info("headToken: "+ headToken);
            JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
            //查询登录信息
            QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("id","nickname","avatar");
            queryWrapper.eq("id",jwtInfo.getId());
            Member member = memberService.getOne(queryWrapper);

            return R.ok().data("userInfo", member);
        }catch(Exception e){
            log.error("解析用户信息失败，" + e.getMessage());
            throw new GlobalException(ResultCodeEnum.FETCH_USERINFO_ERROR);
        }
    }

    @ApiOperation("根据会员id查询会员信息")
    @GetMapping("inner/get-member-dto/{memberId}")
    public MemberDto getMemberDtoByMemberId(
            @ApiParam(value = "会员ID",required = true)
            @PathVariable String memberId
    ){
        MemberDto memberDto = memberService.getMemberDtoByMemberId(memberId);
        return memberDto;
    }

    @ApiOperation("修改会员id修改会员信息")
    @PostMapping("/auth/update")
    public R update(
            @ApiParam(value = "会员数据",required = true)
            @RequestBody WebMemberVo webMemberVo,HttpServletRequest request){

        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        if(jwtInfo == null){
            throw new GlobalException(ResultCodeEnum.NO_LOGIN_ERROR);
        }
        boolean result = memberService.updateMemberById(jwtInfo.getId(), webMemberVo);
        if (result) {
            return R.ok().message("修改成功");
        }else {
            return R.error().message("修改失败");
        }

    }

    @ApiOperation("查询会员信息")
    @GetMapping("/auth/get")
    public R update(
            HttpServletRequest request){

        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        if(jwtInfo == null){
            throw new GlobalException(ResultCodeEnum.NO_LOGIN_ERROR);
        }
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","avatar","nickname","sex","age","sign");
        queryWrapper.eq("id",jwtInfo.getId());
        Member member = memberService.getOne(queryWrapper);
        return R.ok().data("member",member);
    }

    @ApiOperation("更改手机号")
    @PostMapping("/auth/update-mobile")
    public R updateMobile(@ApiParam(required = true,value = "手机号及验证码")
                          @RequestBody WebMobileVo webMobileVo, HttpServletRequest request){
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        if(jwtInfo == null){
            throw new GlobalException(ResultCodeEnum.NO_LOGIN_ERROR);
        }
        boolean result = memberService.updateMobile(webMobileVo, jwtInfo.getId());

        if (result) {
            return R.ok().message("更改成功");
        }else {
            return R.error().message("未知错误，更改失败");
        }
    }

    @ApiOperation("更改密码")
    @PostMapping("/auth/update-password")
    public R updatePassword(@ApiParam(required = true,value = "密码")
                          @RequestBody WebPasswordVo webPasswordVo, HttpServletRequest request){

        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        if(jwtInfo == null){
            throw new GlobalException(ResultCodeEnum.NO_LOGIN_ERROR);
        }
        boolean result = memberService.updatePassword(webPasswordVo, jwtInfo.getId());

        if (result) {
            return R.ok().message("更改成功");
        }else {
            return R.error().message("未知错误，更改失败");
        }
    }
}

