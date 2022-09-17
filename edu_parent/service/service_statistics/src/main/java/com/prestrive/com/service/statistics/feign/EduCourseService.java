package com.prestrive.com.service.statistics.feign;

import com.prestrive.com.common.base.result.R;
import com.prestrive.com.service.statistics.feign.fallback.EduCourseServiceFallback;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@FeignClient(value = "service-edu",fallback = EduCourseServiceFallback.class)
public interface EduCourseService {

    @GetMapping(value = "/admin/edu/course/inner/get-course-num/{day}")
    R getCourseViewCount( @PathVariable String day);

}
