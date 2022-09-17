package com.prestrive.com.service.statistics.feign.fallback;

import com.prestrive.com.common.base.result.R;
import com.prestrive.com.service.statistics.feign.UcenterMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UcenterMemberServiceFallback implements UcenterMemberService {

    @Override
    public R countRegisterNum(String day) {
        log.error("熔断器被执行");
        return R.ok().data("registerNum", 0);
    }

    @Override
    public R countLoginNum(String day) {
        log.error("熔断器被执行");
        return R.ok().data("registerNum", 0);
    }
}
