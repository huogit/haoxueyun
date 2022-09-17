package com.prestrive.com.service.statistics.service;

import com.prestrive.com.service.statistics.entity.Daily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.HashMap;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author strive
 * @since 2022-07-21
 */
public interface DailyService extends IService<Daily> {

    void createByDay(String day);

    /**
     * 查询某天之前的课程累计总观看数
     * @param day
     * @return
     */
    Integer findVideoViewNumSumByDay(String day);

    /**
     * 根据开始时间和结束时间 获取图表
     * @param begin
     * @param end
     * @return
     */
    HashMap<String, HashMap<String, Object>> getChartData(String begin, String end);
}
