package com.prestrive.com.service.statistics.feign;

import com.prestrive.com.common.base.result.R;
import com.prestrive.com.service.statistics.feign.fallback.UcenterMemberServiceFallback;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@FeignClient(value = "service-ucenter",fallback = UcenterMemberServiceFallback.class)
public interface UcenterMemberService {

    @GetMapping(value = "/admin/ucenter/member/count-register-num/{day}")
    public R countRegisterNum(@PathVariable String day);

    @GetMapping("/admin/ucenter/login-log/inner/count-login-num/{day}")
    public R countLoginNum(@PathVariable String day);
}
