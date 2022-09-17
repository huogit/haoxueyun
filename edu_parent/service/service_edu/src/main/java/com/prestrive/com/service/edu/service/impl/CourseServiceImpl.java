package com.prestrive.com.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.prestrive.com.common.base.result.R;
import com.prestrive.com.common.base.util.RedisValueOperationsUtil;
import com.prestrive.com.service.base.dto.CourseDto;
import com.prestrive.com.service.edu.entity.*;
import com.prestrive.com.service.edu.entity.form.CourseInfoForm;
import com.prestrive.com.service.edu.entity.vo.*;
import com.prestrive.com.service.edu.feign.OssFileService;
import com.prestrive.com.service.edu.mapper.CourseMapper;
import com.prestrive.com.service.edu.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author strive
 * @since 2022-04-21
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Autowired
    CourseMapper courseMapper;
    @Autowired
    CourseDescriptionService courseDescriptionService;
    @Autowired
    OssFileService ossFileService;
    @Autowired
    CourseCollectService courseCollectService;
    @Autowired
    CommentService commentService;
    @Autowired
    VideoService videoService;
    @Autowired
    ChapterService chapterService;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RedisValueOperationsUtil redisValueOperationsUtil;

    @Override
    public String saveCourseInfo(CourseInfoForm courseInfoForm) {
        //保存课程基本信息
        Course course = new Course();
        course.setStatus(Course.COURSE_DRAFT);//状态设置为 课程草稿
        BeanUtils.copyProperties(courseInfoForm,course);
        courseMapper.insert(course);

        //保存课程详情
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setId(course.getId());
        courseDescription.setDescription(courseInfoForm.getDescription());
        courseDescriptionService.save(courseDescription);

        return course.getId();
    }

    @Override
    public CourseInfoForm getCourseInfoById(String id) {
        CourseInfoForm courseInfoForm = new CourseInfoForm();

        //从课程表获取数据
        Course course = courseMapper.selectById(id);
        //健壮性判断
        if(course == null){
            return null;
        }
        BeanUtils.copyProperties(course,courseInfoForm);

        //获取课程详情
        CourseDescription description = courseDescriptionService.getById(id);
        courseInfoForm.setDescription(description.getDescription());

        return courseInfoForm;

    }

    @Override
    public boolean updateCourseInfoById(CourseInfoForm courseInfoForm) {
        //更新课程基本信息
        Course course = new Course();
        BeanUtils.copyProperties(courseInfoForm,course);
        int i = courseMapper.updateById(course);
        //更新课程详情
        CourseDescription description = new CourseDescription();
        description.setId(courseInfoForm.getId());
        description.setDescription(courseInfoForm.getDescription());
        boolean b = courseDescriptionService.updateById(description);

        return b || i > 0;
    }

    @Override
    public IPage<CourseVo> findCourseVosByPage(Long page, Long limit, CourseQueryVo courseQueryVo) {
        QueryWrapper<CourseVo> courseVoQueryWrapper = new QueryWrapper<>();
        courseVoQueryWrapper.orderByDesc("c.gmt_create");

        String subjectId = courseQueryVo.getSubjectId();
        String subjectParentId = courseQueryVo.getSubjectParentId();
        String teacherId = courseQueryVo.getTeacherId();
        String title = courseQueryVo.getTitle();

        //查询条件，不是用于关联查询
        if(StringUtils.hasLength(subjectId)){
            courseVoQueryWrapper.eq("c.subject_id",subjectId);
        }
        if(StringUtils.hasLength(subjectParentId)){
            courseVoQueryWrapper.eq("c.subject_parent_id",subjectParentId);
        }
        if(StringUtils.hasLength(teacherId)){
            courseVoQueryWrapper.eq("c.teacher",teacherId);
        }
        if(StringUtils.hasLength(title)){
            courseVoQueryWrapper.like("c.title",title);
        }
        // public Page(long current, long size)
        Page<CourseVo> courseVoPage = new Page<>(page,limit);
        //放入分页参数和查询条件参数，mp会自动组装
        List<CourseVo> courseVoList = courseMapper.findCourseVosByPageAndCourseQueryVo(courseVoPage,courseVoQueryWrapper);
        courseVoPage.setRecords(courseVoList);

        return courseVoPage;
    }

    @Override
    public Boolean deleteCoverById(String id) {
        Course course = courseMapper.selectById(id);
        //判断是否为空
        if(course !=null){
            String cover = course.getCover();
            if(StringUtils.hasLength(cover)){
                R r = ossFileService.removeFile(cover);
                return r.getSuccess();
            }
        }
        return false;
    }

    @Override
    public Boolean deleteCourseById(String id) {

        //收藏信息：course_collect
        //QueryWrapper注入的是什么，查询结果就会封装到该类，如果调用mapper的原始方法，就必须是mapper对应的实体类
        QueryWrapper<CourseCollect> courseCollectQueryWrapper = new QueryWrapper<>();
        courseCollectQueryWrapper.eq("course_id",id);
        courseCollectService.remove(courseCollectQueryWrapper);
        //评论信息：comment
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.eq("course_id",id);
        commentService.remove(commentQueryWrapper);
        //课时信息：video
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id",id);
        videoService.remove(videoQueryWrapper);
        //章节信息：chapter
        QueryWrapper<Chapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id",id);
        chapterService.remove(chapterQueryWrapper);
        //课程详情：course_description
        courseDescriptionService.removeById(id);
        //课程信息：course
        return this.removeById(id);
    }

    @Override
    public CoursePublishVo findCoursePublishVoById(String id) {
       return courseMapper.findCoursePublishVoById(id);
    }

    @Override
    public Boolean updateCourseStatusById(String id) {
        Course course = courseMapper.selectById(id);
        if(course == null){
            return false;
        }
        course.setStatus(Course.COURSE_NORMAL);
        return this.updateById(course);
    }

    @Override
    public List<Course> webSelectList(WebCourseQueryVo webCourseQueryVo) {

        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();

        //查询已发布的课程
        queryWrapper.eq("status", Course.COURSE_NORMAL);

        if(StringUtils.hasLength(webCourseQueryVo.getSubjectParentId())){
            queryWrapper.eq("subject_parent_id",webCourseQueryVo.getSubjectParentId());
        }

        if(StringUtils.hasLength(webCourseQueryVo.getSubjectId())){
            queryWrapper.eq("subject_id",webCourseQueryVo.getSubjectId());
        }

        if(StringUtils.hasLength(webCourseQueryVo.getBuyCountSort())){
            queryWrapper.orderByDesc("buy_count");
        }

        if(StringUtils.hasLength(webCourseQueryVo.getPriceSort())){
            if(webCourseQueryVo.getType() != null && webCourseQueryVo.getType() == 1){
                queryWrapper.orderByAsc("price"); //正序
            }else {
                queryWrapper.orderByDesc("price");//倒序
            }

        }

        if(StringUtils.hasLength(webCourseQueryVo.getGmtCreateSort())){
            queryWrapper.orderByDesc("gmt_create");
        }

        return courseMapper.selectList(queryWrapper);
    }

    @Override
    public WebCourseVo findWebCourseVoById(String id) {

        ValueOperations valueOperations = redisTemplate.opsForValue();


        Course course = courseMapper.selectById(id);
        Long viewCount = course.getViewCount();
        course.setViewCount(viewCount + 1);
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        //更新课程
        courseMapper.updateById(course);

        return courseMapper.selectWebCourseVoById(id);
    }

    @Cacheable(value = "Index",key = "'selectHotCourseList'")
    @Override
    public List<Course> selectHotCourseList() {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("view_count");
        queryWrapper.last("limit 8");

        return courseMapper.selectList(queryWrapper);
    }

    @Override
    public CourseDto getCourseDtoById(String courseId) {

        return baseMapper.selectCourseDtoById(courseId);
    }

    @Override
    public void updateBuyCountById(String id) {
        Course course = courseMapper.selectById(id);
        course.setBuyCount(course.getBuyCount() + 1);

        courseMapper.updateById(course);
    }

    @Override
    public Integer getNewCourseNumByDay(String day) {
       return courseMapper.selectNewCourseNumByDay(day);
    }


    public static void main(String[] args) {
        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }
}
