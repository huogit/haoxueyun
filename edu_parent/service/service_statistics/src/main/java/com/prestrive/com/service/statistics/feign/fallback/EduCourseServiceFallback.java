package com.prestrive.com.service.statistics.feign.fallback;

import com.prestrive.com.common.base.result.R;
import com.prestrive.com.service.statistics.feign.EduCourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EduCourseServiceFallback implements EduCourseService {


    @Override
    public R getCourseViewCount(String day) {
        log.error("熔断器被执行");
        return R.ok().data("registerNum", 0);
    }
}
