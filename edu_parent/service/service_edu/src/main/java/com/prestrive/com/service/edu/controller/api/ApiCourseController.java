package com.prestrive.com.service.edu.controller.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.prestrive.com.common.base.result.R;
import com.prestrive.com.common.base.result.ResultCodeEnum;
import com.prestrive.com.service.base.dto.CourseDto;
import com.prestrive.com.service.base.exception.GlobalException;
import com.prestrive.com.service.edu.entity.Course;
import com.prestrive.com.service.edu.entity.vo.ChapterVo;
import com.prestrive.com.service.edu.entity.vo.WebCourseQueryVo;
import com.prestrive.com.service.edu.entity.vo.WebCourseVo;
import com.prestrive.com.service.edu.service.ChapterService;
import com.prestrive.com.service.edu.service.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(description = "课程")
//@CrossOrigin
@RequestMapping("api/edu/course")
public class ApiCourseController {

    @Autowired
    CourseService courseService;

    @Autowired
    ChapterService chapterService;


    @ApiOperation("课程列表")
    @GetMapping("/list")
    public R list(@ApiParam("查询条件") WebCourseQueryVo webCourseQueryVo){
        List<Course> courses = courseService.webSelectList(webCourseQueryVo);
        return R.ok().data("courseList",courses);
    }

    @ApiOperation("根据ID查询课程")
    @GetMapping("/get/{id}")
    public R getById(@ApiParam(value = "课程ID",required = true) @PathVariable String id){

        WebCourseVo webCourseVo = courseService.findWebCourseVoById(id);

        List<ChapterVo> chapterVos = chapterService.findChapterListByCourseId(id);

        return R.ok().data("course",webCourseVo).data("chapterList",chapterVos);
    }

    @ApiOperation("搜索课程")
    @GetMapping("/search/{word}")
    public R search(@ApiParam(value = "搜索关键字",required = true) @PathVariable String word){
        if(word == null){
           throw new GlobalException(ResultCodeEnum.PARAM_ERROR);
        }

        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("title",word);
        List<Course> courses = courseService.list(queryWrapper);

        return R.ok().data("items",courses);
    }

    @ApiOperation("根据课程id查询课程信息")
    @GetMapping("/inner/get-course-dto/{courseId}")
    public CourseDto getCourseDtoById(@ApiParam(value = "课程id",required = true)
                                      @PathVariable String courseId){
        CourseDto courseDto = courseService.getCourseDtoById(courseId);
        return courseDto;

    }



    //需要远程 数据共享
    @ApiOperation("根据课程id更改销售量")
    @PostMapping("/inner/update-buy-count/{id}")
    public R updateBuyCount(@ApiParam(value = "课程id",required = true) @PathVariable String id){
        courseService.updateBuyCountById(id);
        return R.ok();
    }

}
