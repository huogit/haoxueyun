package com.prestrive.com.service.edu.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.prestrive.com.common.base.result.R;
import com.prestrive.com.service.edu.entity.form.CourseInfoForm;
import com.prestrive.com.service.edu.entity.vo.CoursePublishVo;
import com.prestrive.com.service.edu.entity.vo.CourseQueryVo;
import com.prestrive.com.service.edu.entity.vo.CourseVo;
import com.prestrive.com.service.edu.feign.OssFileService;
import com.prestrive.com.service.edu.service.CourseService;
import com.prestrive.com.service.edu.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@Api(description = "课程管理")
@RestController
//@CrossOrigin
@RequestMapping("/admin/edu/course")
@Slf4j
public class CourseController {

    @Autowired
    CourseService courseService;
    @Autowired
    VideoService videoService;


    @ApiOperation("新增课程")
    @PostMapping("/save-course-info")
    public R saveCourseInfo(
            @ApiParam(value = "课程信息表单",required = true)
            @RequestBody CourseInfoForm courseInfoForm) {

      String courseId = courseService.saveCourseInfo(courseInfoForm);
      return R.ok().data("courseId",courseId).message("保存成功");
    }

    @ApiOperation("根据id查询课程")
    @GetMapping("/course-info/{id}")
    public R getById(
            @ApiParam(name = "课程ID",required = true)
            @PathVariable String id
    ){
       CourseInfoForm courseInfoForm =  courseService.getCourseInfoById(id);
       if(courseInfoForm != null){
           return R.ok().data("item",courseInfoForm);
       }else{
           return R.error().message("数据不存在");
       }
    }

    @ApiOperation("更新课程信息")
    @PutMapping("/update-course-info")
    public R updateCourseInfoById(
            @ApiParam(value = "课程基本信息",required = true)
            @RequestBody CourseInfoForm courseInfoForm
    ) {
        boolean result = courseService.updateCourseInfoById(courseInfoForm);
        return R.ok().message("更新成功");

    }

    //难点：使用MyBatis Plus的分页插件和QueryWrapper结合自定义mapper xml实现多表关联查询
    @ApiOperation("分页课程列表")
    @GetMapping("/list/{page}/{limit}")
    public  R PageList(
            @ApiParam(value = "当前页码",required = true) @PathVariable("page") Long  page,
            @ApiParam(value = "每页显示条数",required = true) @PathVariable("limit") Long  limit,
            @ApiParam(value="查询条件")   CourseQueryVo courseQueryVo

            ){
        IPage<CourseVo> courseVoPage = courseService.findCourseVosByPage(page,limit,courseQueryVo);
        List<CourseVo> records = courseVoPage.getRecords();
        long total = courseVoPage.getTotal();

        return R.ok().data("rows",records).data("total",total);
    }

    @ApiOperation("删除课程")
    @DeleteMapping("/remove/{id}")
    public R removeById(@ApiParam(value = "课程ID",required = true) @PathVariable String id) {
        //删除视频
        videoService.removeMediaVideoByCourseId(id);
        //删除封面:oss
        String result = courseService.deleteCoverById(id) ? "1" : "2";
        //删除课程
        Boolean isDeleteCourse = courseService.deleteCourseById(id);
        if (isDeleteCourse) {
            return R.ok().message("删除成功，图片删除：" + result);
        } else {
            return R.error().message("删除失败，无该数据");
        }
    }

    @ApiOperation("根据ID获取课程发布信息")
    @GetMapping("/course-publish-info/{id}")
    public R coursePublishById(
            @ApiParam(value = "课程id",required = true) @PathVariable String id){
        CoursePublishVo coursePublishVo = courseService.findCoursePublishVoById(id);
        if (coursePublishVo != null) {
            return R.ok().data("item", coursePublishVo);
        }else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("根据ID发布课程")
    @PostMapping("/publish-course/{id}")
    public R publishCourseById(@ApiParam(value = "课程id",required = true)@PathVariable String id) {
        Boolean result = courseService.updateCourseStatusById(id);
        if (result) {
            return R.ok().message("发布成功");
        } else {
            return R.error().message("发布失败，无该数据");
        }
    }

    @ApiOperation("根据日期查询课程新增课程数量")
    @GetMapping("/inner/get-course-num/{day}")
    public R getCourseViewCount(@ApiParam(value = "日期",required = true) @PathVariable String day){
        Integer num = courseService.getNewCourseNumByDay(day);

        return R.ok().data("num",num);

    }

}
