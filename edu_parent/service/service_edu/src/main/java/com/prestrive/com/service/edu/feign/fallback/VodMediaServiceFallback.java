package com.prestrive.com.service.edu.feign.fallback;

import com.prestrive.com.common.base.result.R;
import com.prestrive.com.service.edu.feign.VodMediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class VodMediaServiceFallback implements VodMediaService {

    @Override
    public R removeVideo(String vodId){
        log.info("熔断保护");
        return R.error();
    }

    @Override
    public R removeVideoByIdList(List<String> videoIdList) {
        log.info("熔断保护");
        return R.error().message("调用超时");
    }

}
