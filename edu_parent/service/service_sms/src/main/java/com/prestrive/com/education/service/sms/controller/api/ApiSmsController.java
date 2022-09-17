package com.prestrive.com.education.service.sms.controller.api;

import com.aliyuncs.exceptions.ClientException;
import com.prestrive.com.common.base.result.R;
import com.prestrive.com.common.base.result.ResultCodeEnum;
import com.prestrive.com.common.base.util.FormUtils;
import com.prestrive.com.common.base.util.RandomUtils;
import com.prestrive.com.education.service.sms.service.SmsService;
import com.prestrive.com.service.base.exception.GlobalException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@Api(description = "短信管理")
@RestController
@RequestMapping("/api/sms")
//@CrossOrigin
@Slf4j
public class ApiSmsController {
    @Autowired
    SmsService smsService;
    @Autowired
    RedisTemplate redisTemplate;

    @ApiOperation("获取验证码")
    @GetMapping("send/{mobile}")
    public R getCode(@ApiParam(value = "手机号",required = true)
                     @PathVariable String mobile){
        if(!StringUtils.hasLength(mobile) || !FormUtils.isMobile(mobile)){
            log.error("请输入正确的手机号码 ");
            throw new GlobalException(ResultCodeEnum.LOGIN_PHONE_ERROR);
        }

        //生成验证码
        String code = RandomUtils.getFourBitRandom();

        //发送验证码
        try {
            smsService.send(mobile,code);
        } catch (ClientException e) {
            log.error("短信发送失败");
            throw new GlobalException(ResultCodeEnum.SMS_SEND_ERROR);
        }

        //存入redis 设置 5分钟过期
        redisTemplate.opsForValue().set(mobile,code,5, TimeUnit.MINUTES );
        return R.ok().message("短信发送成功，五分钟内有效");
    }
}
