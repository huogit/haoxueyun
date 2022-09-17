package com.prestrive.com.service.edu.controller.api;

import com.prestrive.com.common.base.result.R;
import com.prestrive.com.common.base.util.RedisValueOperationsUtil;
import com.prestrive.com.service.edu.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@Api(description = "前台视频")
@RequestMapping("/api/edu/video")
@RestController
public class ApiVideoController {

    @Autowired
    VideoService videoService;

    @Autowired
    RedisValueOperationsUtil redisValueOperationsUtil;

    @ApiOperation("添加每日播放视频数")
    @GetMapping("/create/play_count")
    public R getVideoViewCount(){

        String day = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        //将当天的浏览量记录到redis
        if(redisValueOperationsUtil.hasKey_video_view_count_day(day)){
            redisValueOperationsUtil.increment_video_view_count_day(day);
        }else {
            redisValueOperationsUtil.set_video_view_count_day(day,1);
        }

        System.out.println(redisValueOperationsUtil.get_video_view_count_day(day));
        return R.ok().message("成功");
    }

}
