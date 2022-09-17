package com.prestrive.com.service.edu.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.prestrive.com.service.edu.entity.CourseCollect;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.prestrive.com.service.edu.entity.vo.CourseCollectVo;
import com.prestrive.com.service.edu.entity.vo.CourseVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 课程收藏 Mapper 接口
 * </p>
 *
 * @author strive
 * @since 2022-04-21
 */
@Repository
@Mapper
public interface CourseCollectMapper extends BaseMapper<CourseCollect> {

    List<CourseCollectVo> findListByMemberId(
            //@Param(Constants.WRAPPER) 和 xml文件中的 ${ew.customSqlSegment} 对应
            @Param(Constants.WRAPPER) QueryWrapper<CourseCollectVo> queryWrapper);
}
