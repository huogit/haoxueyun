package com.prestrive.com.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.prestrive.com.service.edu.entity.CourseCollect;
import com.prestrive.com.service.edu.entity.vo.CourseCollectVo;
import com.prestrive.com.service.edu.mapper.CourseCollectMapper;
import com.prestrive.com.service.edu.service.CourseCollectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 课程收藏 服务实现类
 * </p>
 *
 * @author strive
 * @since 2022-04-21
 */
@Service
public class CourseCollectServiceImpl extends ServiceImpl<CourseCollectMapper, CourseCollect> implements CourseCollectService {

    @Autowired
    CourseCollectMapper courseCollectMapper;

    @Override
    public boolean isCollect(String courseId, String memberId) {
        QueryWrapper<CourseCollect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId).eq("member_id", memberId);

        Integer count = courseCollectMapper.selectCount(queryWrapper);

        return count > 0;
    }

    @Override
    public void saveCourseCollect(String courseId, String memberId) {
        //未收藏则收藏
        if(!this.isCollect(courseId,memberId)){
            CourseCollect courseCollect = new CourseCollect();
            courseCollect.setCourseId(courseId);
            courseCollect.setMemberId(memberId);
            courseCollectMapper.insert(courseCollect);
        }
    }

    @Override
    public List<CourseCollectVo> findListByMemberId(String memberId) {
        QueryWrapper<CourseCollectVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cl.member_id",memberId);
        queryWrapper.orderByDesc("cl.gmt_create");

       List<CourseCollectVo> courseCollectVos = courseCollectMapper.findListByMemberId(queryWrapper);

       return courseCollectVos;
    }

    @Override
    public boolean removeCourseCollect(String courseId, String memberId) {
        if(this.isCollect(courseId,memberId)){
            QueryWrapper<CourseCollect> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("course_id", courseId).eq("member_id", memberId);
            int delete = courseCollectMapper.delete(queryWrapper);
            return delete > 0;
        }
        return false;
    }
}
