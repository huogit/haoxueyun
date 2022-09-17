package com.prestrive.com.service.edu.service;

import com.prestrive.com.common.base.result.R;
import com.prestrive.com.service.edu.entity.Teacher;
import com.baomidou.mybatisplus.extension.service.IService;
import com.prestrive.com.service.edu.entity.vo.TeacherQueryVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author strive
 * @since 2022-04-21
 */
public interface TeacherService extends IService<Teacher> {


    R findByPage(Integer page, Integer limit, TeacherQueryVo teacherQueryVo);

    /**
     * 根据key like查询 教师名字
     * @param name
     * @return
     */
    List<Map<String, Object>> selectNameListByKey(String name);

    /**
     * 删除教师头像
     * @param teacherId
     * @return
     */
    boolean removeAvatarById(String teacherId);


    /**
     * 根据讲师id获取讲师详情页数据
     * @param id
     * @return
     */
    HashMap<String,Object> getTeacherInfoById(String id);

    /**
     * 获取首页热门教师列表
     * @return
     */
    List<Teacher> selectHotTeacherList();
}
