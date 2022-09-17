package com.prestrive.com.service.edu.service;

import com.prestrive.com.service.edu.entity.CourseCollect;
import com.baomidou.mybatisplus.extension.service.IService;
import com.prestrive.com.service.edu.entity.vo.CourseCollectVo;

import java.util.List;

/**
 * <p>
 * 课程收藏 服务类
 * </p>
 *
 * @author strive
 * @since 2022-04-21
 */
public interface CourseCollectService extends IService<CourseCollect> {

    /**
     * 判断是否收藏
     * @param courseId
     * @param id
     * @return
     */
    boolean isCollect(String courseId, String memberId);

    /**
     * 添加收藏
     * @param courseId
     * @param memberId
     */
    void saveCourseCollect(String courseId, String memberId);

    /**
     * 获取收藏列表
     * @param memberId
     * @return
     */
    List<CourseCollectVo> findListByMemberId(String memberId);

    /**
     * 删除收藏
     * @param courseId
     * @param memberId
     * @return
     */
    boolean removeCourseCollect(String courseId, String memberId);
}
