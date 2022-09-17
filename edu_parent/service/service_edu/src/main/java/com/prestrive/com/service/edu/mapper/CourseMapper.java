package com.prestrive.com.service.edu.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants; //mybatis的包
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.prestrive.com.service.base.dto.CourseDto;
import com.prestrive.com.service.edu.entity.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.prestrive.com.service.edu.entity.vo.CoursePublishVo;
import com.prestrive.com.service.edu.entity.vo.CourseVo;

import com.prestrive.com.service.edu.entity.vo.WebCourseVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author strive
 * @since 2022-04-21
 */
@Repository
@Mapper
public interface CourseMapper extends BaseMapper<Course> {


    List<CourseVo> findCourseVosByPageAndCourseQueryVo(
            //mp会自动组装分页参数
            Page<CourseVo> courseVoPage,
            //mp会自动组装queryWrapper：
            //@Param(Constants.WRAPPER) 和 xml文件中的 ${ew.customSqlSegment} 对应
            @Param(Constants.WRAPPER) QueryWrapper<CourseVo> courseVoQueryWrapper);

    CoursePublishVo findCoursePublishVoById(String id);

    WebCourseVo selectWebCourseVoById(String id);

    CourseDto selectCourseDtoById(String courseId);



    Integer selectNewCourseNumByDay(String day);
}
