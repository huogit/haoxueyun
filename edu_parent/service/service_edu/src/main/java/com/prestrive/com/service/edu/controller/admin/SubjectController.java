package com.prestrive.com.service.edu.controller.admin;


import com.prestrive.com.common.base.result.R;
import com.prestrive.com.common.base.result.ResultCodeEnum;
import com.prestrive.com.service.base.exception.GlobalException;
import com.prestrive.com.service.edu.entity.vo.SubjectVo;
import com.prestrive.com.service.edu.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author strive
 * @since 2022-04-21
 */
@Api(description = "课程分类管理")
@RestController
@RequestMapping("/admin/edu/subject")
//@CrossOrigin //允许跨域
@Slf4j
public class SubjectController {

    @Autowired
    SubjectService subjectService;

    @ApiOperation("Excel批量导入课程类别数据")
    @PostMapping("/import")
    public R batchImport(
            @ApiParam(value = "Excel文件",required = true)
            @RequestParam("file")MultipartFile file
            ){
//        //return R.ok().message("批量导入成功");
//        return R.error().message("批量导入失败");
        try {
            InputStream inputStream = file.getInputStream();
            subjectService.batchImport(inputStream);
            return R.ok().message("批量导入成功");
        } catch (IOException e) {
            e.printStackTrace();
            throw new GlobalException(ResultCodeEnum.FILE_UPLOAD_ERROR);
        }
    }

    @ApiOperation("嵌套数据列表")
    @GetMapping("/nested-list")
    public R nestedList(){
        List<SubjectVo> subjectVos = subjectService.nestedList();
        return R.ok().data("items",subjectVos);
    }

}

