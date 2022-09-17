package com.prestrive.com.service.statistics.task;

import com.prestrive.com.service.statistics.service.DailyService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ScheduledTask {

    @Autowired
    private DailyService dailyService;

    /**
     * 测试
     */
    @Scheduled(cron = "0 0 1 * * ?") //注意只支持6位表达式
    public void task1(){
        //获取上一天的日期
        String day = new DateTime().minusDays(1).toString("yyyy-MM-dd");
        dailyService.createByDay(day);
        log.info("createByDay 统计完毕");
    }
}
