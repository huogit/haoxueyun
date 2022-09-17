package com.prestrive.com.eduction.service.vod.controller.admin;

import com.aliyuncs.exceptions.ClientException;
import com.prestrive.com.common.base.result.R;
import com.prestrive.com.common.base.result.ResultCodeEnum;
import com.prestrive.com.common.base.util.ExceptionUtils;
import com.prestrive.com.eduction.service.vod.service.VideoService;
import com.prestrive.com.service.base.exception.GlobalException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
//@CrossOrigin
@Api(description = "阿里云视频点播")
@Slf4j
@RequestMapping("/admin/vod/media")
public class MediaController {

    @Autowired
    private VideoService videoService;

    @ApiOperation("上传视频")
    @PostMapping("/upload")
    public R uploadVideo(
            @ApiParam(name = "file", value = "文件", required = true)
            @RequestParam("file") MultipartFile file
            ){
        try {
            String originalFilename = file.getOriginalFilename();
            InputStream inputStream = file.getInputStream();
            String videoId = videoService.uploadVideo(inputStream, originalFilename);
            return R.ok().data("videoId",videoId).message("视频上传成功");
        } catch (IOException e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GlobalException(ResultCodeEnum.VIDEO_UPLOAD_TOMCAT_ERROR);
        }
    }

    @ApiOperation("删除视频")
    @DeleteMapping("/remove/{videoId}")
    public R removeVideo(@ApiParam(value = "阿里云视频id",required = true) @PathVariable String videoId){
        try {
            videoService.removeVideoById(videoId);
            return R.ok().message("删除视频成功");
        } catch (ClientException e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GlobalException(ResultCodeEnum.VIDEO_DELETE_ALIYUN_ERROR);
        }
    }

    @ApiOperation("批量删除视频")
    @DeleteMapping("/remove")
    public R remove(@ApiParam(value = "阿里云视频id列表",required = true)
                        @RequestBody List<String> videoIdList){
        try {
            videoService.removeVideoByVideoIdList(videoIdList);
            return R.ok().message("批量删除成功");
        } catch (ClientException e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GlobalException(ResultCodeEnum.VIDEO_DELETE_ALIYUN_ERROR);
        }
    }
}