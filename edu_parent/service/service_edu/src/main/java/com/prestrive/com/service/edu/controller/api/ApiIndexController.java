package com.prestrive.com.service.edu.controller.api;

import com.prestrive.com.common.base.result.R;
import com.prestrive.com.service.edu.entity.Course;
import com.prestrive.com.service.edu.entity.Teacher;
import com.prestrive.com.service.edu.service.CourseService;
import com.prestrive.com.service.edu.service.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(description = "广告推荐")
@RequestMapping("/api/edu/index")
public class ApiIndexController {

    @Autowired
    CourseService courseService;

    @Autowired
    TeacherService teacherService;

    @ApiOperation("课程列表")
    @GetMapping
    public R index(){
        List<Course> courseList = courseService.selectHotCourseList();

        List<Teacher> teacherList = teacherService.selectHotTeacherList();

        return R.ok().data("courseList",courseList).data("teacherList",teacherList);
    }
}
