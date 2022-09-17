package com.prestrive.com.service.edu.controller.admin;

import com.prestrive.com.common.base.result.R;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

@Api(description = "测试")
//@CrossOrigin
@RequestMapping("/user")
@RestController
public class LoginController {
    @PostMapping("/login")
    public R login(){
        return  R.ok().data("token","admin");
    }

    @GetMapping("/info")
    public R info(){
        return R.ok().data("roles","[admin]")
                .data("name","admin")
                .data("avatar","https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
    }

    @PostMapping("/logout")
    public R logout(){
        return R.ok();
    }
}
