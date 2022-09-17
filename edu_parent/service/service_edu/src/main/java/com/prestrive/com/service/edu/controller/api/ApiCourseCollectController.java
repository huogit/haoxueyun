package com.prestrive.com.service.edu.controller.api;


import com.prestrive.com.common.base.result.R;
import com.prestrive.com.common.base.result.ResultCodeEnum;
import com.prestrive.com.common.base.util.JwtInfo;
import com.prestrive.com.common.base.util.JwtUtils;
import com.prestrive.com.service.base.exception.GlobalException;
import com.prestrive.com.service.edu.entity.CourseCollect;
import com.prestrive.com.service.edu.entity.vo.CourseCollectVo;
import com.prestrive.com.service.edu.service.CourseCollectService;
import com.prestrive.com.service.edu.service.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 课程收藏 前端控制器
 * </p>
 *
 * @author strive
 * @since 2022-04-21
 */
@RestController
@RequestMapping("/api/edu/course-collect")
@Api(description = "课程收藏")
//@CrossOrigin
public class ApiCourseCollectController {

    @Autowired
    CourseCollectService courseCollectService;

    @ApiOperation("判断是否收藏")
    @GetMapping("/auth/is-collect/{courseId}")
    public R isCollect(
            @ApiParam(name = "courseId", value = "课程id", required = true)
            @PathVariable String courseId,
            HttpServletRequest request
    ){
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        if(jwtInfo == null){
            throw new GlobalException(ResultCodeEnum.LOGIN_AUTH);
        }
        boolean isCollect = courseCollectService.isCollect(courseId, jwtInfo.getId());
        return R.ok().data("isCollect", isCollect);
    }

    @ApiOperation(value = "收藏课程")
    @PostMapping("/auth/save/{courseId}")
    public R save(
            @ApiParam(name = "courseId", value = "课程id", required = true)
            @PathVariable String courseId,
            HttpServletRequest request) {
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        courseCollectService.saveCourseCollect(courseId, jwtInfo.getId());
        return R.ok();
    }

    @ApiOperation("获取课程列表")
    @GetMapping("/auth/list")
    public R list(HttpServletRequest request){
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        if(jwtInfo == null){
            throw new GlobalException(ResultCodeEnum.PARAM_ERROR);
        }

        List<CourseCollectVo> courseCollectVos = courseCollectService.findListByMemberId(jwtInfo.getId());

        return R.ok().data("items",courseCollectVos);
    }

    @ApiOperation(value = "取消收藏课程")
    @DeleteMapping("/auth/remove/{courseId}")
    public R remove(
            @ApiParam(name = "courseId", value = "课程id", required = true)
            @PathVariable String courseId,
            HttpServletRequest request) {
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        if(jwtInfo == null){
            throw new GlobalException(ResultCodeEnum.PARAM_ERROR);
        }
        boolean result = courseCollectService.removeCourseCollect(courseId, jwtInfo.getId());
        if (result) {
            return R.ok().message("已取消");
        } else {
            return R.error().message("取消失败");
        }
    }
}

