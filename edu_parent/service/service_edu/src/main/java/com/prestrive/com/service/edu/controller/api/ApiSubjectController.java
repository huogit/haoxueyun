package com.prestrive.com.service.edu.controller.api;

import com.prestrive.com.common.base.result.R;
import com.prestrive.com.service.edu.entity.vo.SubjectVo;
import com.prestrive.com.service.edu.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(description = "课程分类")
//@CrossOrigin
@RestController
@RequestMapping("api/edu/subject")
public class ApiSubjectController {

    @Autowired
    SubjectService subjectService;

    @ApiOperation("嵌套分类列表")
    @GetMapping("/nested-list")
    public R nestedList(){
        List<SubjectVo> subjectVos = subjectService.nestedList();
        return R.ok().data("items",subjectVos);
    }
}
