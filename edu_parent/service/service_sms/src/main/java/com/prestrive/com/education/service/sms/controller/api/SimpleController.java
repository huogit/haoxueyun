package com.prestrive.com.education.service.sms.controller.api;

import com.prestrive.com.common.base.result.R;
import com.prestrive.com.education.service.sms.util.SmsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/sms/simple")
public class SimpleController {

    @Value("${aliyun.sms.signName}")
    private String signName;

    @Autowired
    SmsProperties smsProperties;

    @GetMapping("/test1")
    public R test1(){
        return R.ok().data("signName",signName);
    }

    @GetMapping("/test2")
    public R test2(){
        return R.ok().data("signName",smsProperties.getSignName());
    }

}
