package com.prestrive.com.service.ucenter.controller.admin;


import com.prestrive.com.common.base.result.R;
import com.prestrive.com.service.ucenter.service.LoginLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 登录日志 前端控制器
 * </p>
 *
 * @author strive
 * @since 2022-07-23
 */
@Api(description = "登录日志")
@RestController
@RequestMapping("/admin/ucenter/login-log")
public class AdminLoginLogController {

    @Autowired
    LoginLogService loginLogService;

    @ApiOperation("根据日期统计会员登录人数")
    @GetMapping("/inner/count-login-num/{day}")
    public R countLoginNum(
            @ApiParam(name = "day",value = "统计日期")
            @PathVariable String day){
        Integer count = loginLogService.findLoginNumByDay(day);

        return R.ok().data("num",count);
    }
}

