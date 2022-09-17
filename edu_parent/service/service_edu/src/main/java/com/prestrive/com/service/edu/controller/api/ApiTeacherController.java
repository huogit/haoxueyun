package com.prestrive.com.service.edu.controller.api;


import com.prestrive.com.common.base.result.R;
import com.prestrive.com.service.edu.entity.Teacher;
import com.prestrive.com.service.edu.service.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author strive
 * @since 2022-04-21
 */
@RestController
@RequestMapping("api/edu/teacher")
//@CrossOrigin
@Api(description = "讲师")
public class ApiTeacherController {

    @Autowired
    private TeacherService teacherService;

    @ApiOperation("讲师列表")
    @GetMapping("/list")
    public R listAll(){
        List<Teacher> list = teacherService.list();

        return R.ok().data("items",list).message("获取讲师列表成功");
    }

    @ApiOperation("讲师详情")
    @GetMapping("/get/{id}")
    public R getById(@ApiParam(value = "讲师id",required = true) @PathVariable String id){
        HashMap<String, Object> teacherInfo = teacherService.getTeacherInfoById(id);

        return R.ok().data(teacherInfo);
    }


}

