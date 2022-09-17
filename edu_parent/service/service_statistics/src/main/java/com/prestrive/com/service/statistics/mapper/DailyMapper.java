package com.prestrive.com.service.statistics.mapper;

import com.prestrive.com.service.statistics.entity.Daily;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 网站统计日数据 Mapper 接口
 * </p>
 *
 * @author strive
 * @since 2022-07-21
 */
@Mapper
@Repository
public interface DailyMapper extends BaseMapper<Daily> {

    Integer selectCourseViewNumSumByDay(String day);
}
