package com.prestrive.com.service.statistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.prestrive.com.common.base.result.R;
import com.prestrive.com.common.base.result.ResultCodeEnum;
import com.prestrive.com.common.base.util.RedisValueOperationsUtil;
import com.prestrive.com.service.base.exception.GlobalException;
import com.prestrive.com.service.statistics.entity.Daily;
import com.prestrive.com.service.statistics.feign.EduCourseService;
import com.prestrive.com.service.statistics.feign.UcenterMemberService;
import com.prestrive.com.service.statistics.mapper.DailyMapper;
import com.prestrive.com.service.statistics.service.DailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author strive
 * @since 2022-07-21
 */
@Service
public class DailyServiceImpl extends ServiceImpl<DailyMapper, Daily> implements DailyService {

    @Autowired
    DailyMapper dailyMapper;

    @Autowired
    UcenterMemberService ucenterMemberService;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RedisValueOperationsUtil redisValueOperationsUtil;

    @Autowired
    EduCourseService eduCourseService;


    @Override
    public void createByDay(String day) {

        //如果当日的统计记录已存在，则删除重新统计|或 提示用户当日记录已存在

        QueryWrapper<Daily> dailyQueryWrapper = new QueryWrapper<>();
        dailyQueryWrapper.eq("date_calculated", day);

        //获取昨天
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, -1);
        String yesterday = new SimpleDateFormat("yyyy-MM-dd").format(instance.getTime());
        dailyQueryWrapper.last("and date(\"" + yesterday + "\") > date(\"" + day + "\")");

        Integer count = dailyMapper.selectCount(dailyQueryWrapper);

        if (count > 0) {
            //昨天之前的并存在的 数据
            throw new GlobalException(ResultCodeEnum.RECORD_EXISTS_ERROR);
        } else {
            dailyQueryWrapper = new QueryWrapper<>();
            dailyQueryWrapper.eq("date_calculated", day);
            dailyMapper.delete(dailyQueryWrapper);
        }
        dailyMapper.delete(dailyQueryWrapper);
        //生成统计记录
        R result = ucenterMemberService.countRegisterNum(day);
        Integer registerNum = (Integer) result.getData().get("num");
        Integer loginNum = (Integer) ucenterMemberService.countLoginNum(day).getData().get("num");
        //从redis取出 videoViewNum
        int videoViewNum = 0;
        if (redisValueOperationsUtil.hasKey_video_view_count_day(day)) {
            videoViewNum = redisValueOperationsUtil.get_video_view_count_day(day);
        }
        Integer courseNum = (Integer) eduCourseService.getCourseViewCount(day).getData().get("num");

        //在本地数据库创建统计信息
        Daily daily = new Daily();
        daily.setCourseNum(courseNum);
        daily.setRegisterNum(registerNum);
        daily.setLoginNum(loginNum);
        daily.setVideoViewNum(videoViewNum);
        daily.setDateCalculated(day);

        dailyMapper.insert(daily);

    }

    @Override
    public Integer findVideoViewNumSumByDay(String day) {

       Integer sum = dailyMapper.selectCourseViewNumSumByDay(day);

        return sum;
    }

    @Override
    public HashMap<String, HashMap<String, Object>> getChartData(String begin, String end) {
        HashMap<String, HashMap<String, Object>> map = new HashMap<>();
        HashMap<String,Object> registerNum = getChartDataByType("register_num",begin,end);
        HashMap<String, Object> loginNum = getChartDataByType("login_num", begin, end);
        HashMap<String, Object> videoViewNum = getChartDataByType("video_view_num", begin, end);
        HashMap<String, Object> courseNum = getChartDataByType("course_num", begin, end);

        map.put("registerNum",registerNum);
        map.put("loginNum",loginNum);
        map.put("videoViewNum",videoViewNum);
        map.put("courseNum",courseNum);

        return map;
    }

    /**
     * 辅助方法：根据类别获取数据
     * @param type
     * @param begin
     * @param end
     * @return
     */
    private HashMap<String, Object> getChartDataByType(String type, String begin, String end) {
        QueryWrapper<Daily> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(type,"date_calculated");
        queryWrapper.between("date_calculated",begin,end );

        List<Map<String, Object>> mapsData  = dailyMapper.selectMaps(queryWrapper);

        ArrayList<String> xList = new ArrayList<>();//日期列表
        ArrayList<Integer> yList = new ArrayList<>();//数据列表

        HashMap<String, Object> map = new HashMap<>();
        for (Map<String, Object> data : mapsData ) {
            String dateCalculated = (String) data.get("date_calculated");
            xList.add(dateCalculated);

            Integer num = (Integer) data.get(type);
            yList.add(num);
        }

        map.put("xData",xList);
        map.put("yData",yList);

        return map;
    }
}
