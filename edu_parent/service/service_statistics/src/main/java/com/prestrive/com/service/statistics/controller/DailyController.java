package com.prestrive.com.service.statistics.controller;


import com.prestrive.com.common.base.result.R;
import com.prestrive.com.service.statistics.service.DailyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author strive
 * @since 2022-07-21
 */
@Api(description = "统计分析管理")
@RestController
@RequestMapping("/admin/statistics/daily")
public class DailyController {

    @Autowired
    DailyService dailyService;

    @ApiOperation("生成统计记录")
    @PostMapping("/create/{day}")
    public R create(@ApiParam(value = "统计日期",required = true) @PathVariable String day){

        dailyService.createByDay(day);
        return R.ok().message("数据统计生成成功");
    }

    @ApiOperation("查询某天及以前的课程累计观看总数")
    @GetMapping("/inner/get-video-view-num-sum/{day}")
    public R getCourseViewCountSum(@ApiParam(value = "日期",required = true) @PathVariable String day){

       Integer viewCount = dailyService.findVideoViewNumSumByDay(day);

       return R.ok().data("num",viewCount);
    }

    @ApiOperation("显示统计数据")
    @GetMapping("/show-chart/{begin}/{end}")
    public R showChart(
            @ApiParam("开始时间") @PathVariable String begin,
            @ApiParam("截止时间") @PathVariable String end
    ){
       HashMap<String,HashMap<String,Object>> map = dailyService.getChartData(begin,end);

       return R.ok().data("chartData",map);
    }
}

