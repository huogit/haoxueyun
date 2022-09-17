package com.prestrive.com.eduction.service.vod.controller.api;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(description = "阿里云视频点播")
//@CrossOrigin
@RequestMapping("/api/vod/media")
@Slf4j
public class ApiMediaController {

    @Autowired
    private VideoService videoService;

    @GetMapping("get-play-auth/{videoSourceId}")
    public R getPlayAuth(@ApiParam(value = "阿里云视频文件的id",required = true)
                             @PathVariable String videoSourceId){
        try {
            String playAuth = videoService.getPlayAuth(videoSourceId);
            return R.ok().data("playAuth",playAuth).message("获取播放凭证成功");
        } catch (ClientException e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GlobalException(ResultCodeEnum.FETCH_PLAYAUTH_ERROR);
        }
    }
}
