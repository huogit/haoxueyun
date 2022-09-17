package com.prestrive.com.service.trade.feign;

import com.prestrive.com.common.base.result.R;
import com.prestrive.com.service.base.dto.CourseDto;
import com.prestrive.com.service.trade.feign.fallback.EduCourseServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Service
@FeignClient(value = "service-edu",fallback = EduCourseServiceFallback.class)
public interface EduCourseService {

    @GetMapping(value = "/api/edu/course/inner/get-course-dto/{courseId}")
    CourseDto getCourseDtoById(@PathVariable(value = "courseId") String courseId);

    @PostMapping(value = "/api/edu/course/inner/update-buy-count/{id}")
    R updateBuyCountById(@PathVariable String id);


}
