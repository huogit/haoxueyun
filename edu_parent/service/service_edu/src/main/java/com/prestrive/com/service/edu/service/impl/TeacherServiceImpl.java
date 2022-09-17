package com.prestrive.com.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.prestrive.com.common.base.result.R;
import com.prestrive.com.service.edu.entity.Course;
import com.prestrive.com.service.edu.entity.Teacher;
import com.prestrive.com.service.edu.entity.vo.TeacherQueryVo;
import com.prestrive.com.service.edu.feign.OssFileService;
import com.prestrive.com.service.edu.mapper.TeacherMapper;
import com.prestrive.com.service.edu.service.CourseService;
import com.prestrive.com.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author strive
 * @since 2022-04-21
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @Autowired
    TeacherMapper teacherMapper;

    @Autowired
    OssFileService ossFileService;

    @Autowired
    CourseService courseService;

    @Override
    public R findByPage(Integer page, Integer limit, TeacherQueryVo teacherQueryVo) {
        Page<Teacher> teacherPage = new Page<>(page, limit);
        //构建查询条件
        QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();
        if(teacherQueryVo != null){

            String name = teacherQueryVo.getName();
            String joinDateBegin = teacherQueryVo.getJoinDateBegin();
            String joinDateEnd = teacherQueryVo.getJoinDateEnd();
            Integer level = teacherQueryVo.getLevel();

            if(StringUtils.hasLength(name)){
                //左%会使索引失效
                teacherQueryWrapper.likeRight("name",name);
            }

            if(StringUtils.hasLength(joinDateBegin)){
                teacherQueryWrapper.gt("join_date",joinDateBegin);
            }

            if(StringUtils.hasLength(joinDateEnd)){
                teacherQueryWrapper.le("join_date",joinDateEnd);
            }
            if(level != null){
                teacherQueryWrapper.eq("level",level);
            }
        }

        IPage<Teacher> pageModel = teacherMapper.selectPage(teacherPage, teacherQueryWrapper);

        List<Teacher> records = pageModel.getRecords();
        long total = pageModel.getTotal();
        long pages = pageModel.getPages();

        return R.ok().data("records",records).data("total",total).data("pages",pages);
    }

    @Override
    public List<Map<String, Object>> selectNameListByKey(String name) {
        QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();
        teacherQueryWrapper.select("name");
        teacherQueryWrapper.likeRight("name",name);

        List<Map<String, Object>> mapList = teacherMapper.selectMaps(teacherQueryWrapper);
        return mapList;
    }

    @Override
    public boolean removeAvatarById(String teacherId) {
        Teacher teacher = teacherMapper.selectById(teacherId);
        if(teacher != null){
            String avatar = teacher.getAvatar();
            if(StringUtils.hasLength(avatar)){
                //删除图片
                R r = ossFileService.removeFile(avatar);
                return r.getSuccess();
            }
        }
        return false;
    }

    @Override
    public HashMap<String, Object> getTeacherInfoById(String id) {
        Teacher teacher = teacherMapper.selectById(id);

        List<Course> courseList = courseService.list(
                new QueryWrapper<Course>()
                        .select("id","cover", "title")
                        .eq("teacher_id", id));

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("teacher",teacher);
        hashMap.put("courseList",courseList);

        return hashMap;
    }

    //@Cacheable(value = "Index",key = "'selectHotTeacherList'")
    @Override
    public List<Teacher> selectHotTeacherList() {

        QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();

        teacherQueryWrapper.orderByAsc("sort");

        teacherQueryWrapper.last("limit 4");

        return teacherMapper.selectList(teacherQueryWrapper);
    }
}
