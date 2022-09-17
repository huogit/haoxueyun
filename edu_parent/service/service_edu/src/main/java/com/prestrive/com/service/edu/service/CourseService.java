package com.prestrive.com.service.edu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.prestrive.com.service.base.dto.CourseDto;
import com.prestrive.com.service.edu.entity.Course;
import com.baomidou.mybatisplus.extension.service.IService;
import com.prestrive.com.service.edu.entity.form.CourseInfoForm;
import com.prestrive.com.service.edu.entity.vo.*;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author strive
 * @since 2022-04-21
 */
public interface CourseService extends IService<Course> {

    String saveCourseInfo(CourseInfoForm courseInfoForm);

    /**
     * 根据id查询课程
     * @param id
     * @return
     */
    CourseInfoForm getCourseInfoById(String id);

    boolean updateCourseInfoById(CourseInfoForm courseInfoForm);

    IPage<CourseVo> findCourseVosByPage(Long  page, Long  limit, CourseQueryVo courseQueryVo);

    Boolean deleteCoverById(String id);

    Boolean deleteCourseById(String id);

    CoursePublishVo findCoursePublishVoById(String id);

    Boolean updateCourseStatusById(String id);

    List<Course> webSelectList(WebCourseQueryVo webCourseQueryVo);

    /**
     * 获取课程信息并更新浏览量
     * @param id
     * @return
     */
    WebCourseVo findWebCourseVoById(String id);

    /**
     * 获取首页热门课程
     * @return
     */
    List<Course> selectHotCourseList();
    /**
     * 获取courseDto
     * @return
     */
    CourseDto getCourseDtoById(String courseId);

    /**
     * 根据课程id修改课程销量
     * @param id
     */
    void updateBuyCountById(String id);


    /**
     * 查询某日新增课程数
     * @param day
     * @return
     */
    Integer getNewCourseNumByDay(String day);
}
