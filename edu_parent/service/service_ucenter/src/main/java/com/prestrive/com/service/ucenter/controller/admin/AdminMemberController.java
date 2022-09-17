package com.prestrive.com.service.ucenter.controller.admin;

import com.prestrive.com.common.base.result.R;
import com.prestrive.com.service.ucenter.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "后台会员管理")
@RestController
@RequestMapping("/admin/ucenter/member")
public class AdminMemberController {

    @Autowired
    MemberService memberService;

    @ApiOperation("跟据日期统计会员注册人数")
    @GetMapping("/count-register-num/{day}")
    public R countRegisterNum(
            @ApiParam(name = "day",value = "统计日期")
            @PathVariable String day){
        Integer count = memberService.findRegisterCountByDay(day);

        return R.ok().data("sum",count);
    }


}
