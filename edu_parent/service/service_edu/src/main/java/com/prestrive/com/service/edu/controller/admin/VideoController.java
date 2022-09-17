package com.prestrive.com.service.edu.controller.admin;


import com.prestrive.com.common.base.result.R;
import com.prestrive.com.common.base.result.ResultCodeEnum;
import com.prestrive.com.service.edu.entity.Video;
import com.prestrive.com.service.edu.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author strive
 * @since 2022-04-21
 */
@Api(description = "课时管理")
//@CrossOrigin
@RestController
@RequestMapping("admin/edu/video")
@Slf4j
public class VideoController {

    @Autowired
    VideoService videoService;

    @ApiOperation("新增课时")
    @PostMapping("/save")
    public R save(@ApiParam(value = "课时对象",required = true) @RequestBody Video video){
        if(video == null){
            return R.result(ResultCodeEnum.PARAMETER_NULL_ERROR);
        }
        boolean save = videoService.save(video);
        if (save) {
            return R.ok().message("新增成功");
        }else{
            return R.error().message("新增失败");
        }
    }

    @ApiOperation("根据ID查询课时对象")
    @GetMapping("/get/{id}")
    public R getById(@ApiParam(value = "课时id",required = true) @PathVariable String id){
        Video video = videoService.getById(id);
        if(video != null){
            return R.ok().data("item",video);
        }else{
            return R.error().message("该数据不存在");
        }
    }

    @ApiOperation("更新课时")
    @PutMapping("/update")
    public R updateById(@ApiParam(value = "课时对象",required = true) @RequestBody Video video){
        if(video==null){
            return R.result(ResultCodeEnum.PARAMETER_NULL_ERROR);
        }

        boolean result = videoService.updateById(video);
        if (result) {
            return R.ok().message("更新成功");
        }else{
            return R.error().message("更新失败");
        }
    }

    @ApiOperation("根据ID删除课时")
    @DeleteMapping("/remove/{id}")
    public R removeById(@ApiParam(value = "课时ID",required = true) @PathVariable String id){
        boolean result = videoService.removeById(id);
        if (result) {

            //删除视频
            videoService.removeMediaVideoById(id);
            return R.ok().message("删除成功");
        }else {
            return R.error().message("数据不存在");
        }
    }


}

