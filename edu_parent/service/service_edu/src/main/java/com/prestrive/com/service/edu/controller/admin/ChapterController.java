package com.prestrive.com.service.edu.controller.admin;


import com.prestrive.com.common.base.result.R;
import com.prestrive.com.service.edu.entity.Chapter;
import com.prestrive.com.service.edu.entity.vo.ChapterVo;
import com.prestrive.com.service.edu.service.ChapterService;
import com.prestrive.com.service.edu.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author strive
 * @since 2022-04-21
 */
//@CrossOrigin
@Api(description = "章节管理")
@RestController
@RequestMapping("admin/edu/chapter")
@Slf4j
public class ChapterController {
    @Autowired
    ChapterService chapterService;
    @Autowired
    VideoService videoService;

    @ApiOperation("新增章节")
    @PostMapping("save")
    public R save(@ApiParam(value = "章节对象",required = true) @RequestBody Chapter chapter){
        boolean save = chapterService.save(chapter);
        if(save){
            return R.ok().message("新增章节成功");
        }else{
            return R.error().message("新增章节失败");
        }
    }

    @ApiOperation("根据id查询章节")
    @GetMapping("/get/{id}")
    public  R getById(@ApiParam(value = "章节ID",required = true) @PathVariable String id){
        Chapter chapter = chapterService.getById(id);
        if(chapter != null){
            return R.ok().data("item",chapter);
        }else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("根据id更新章节")
    @PostMapping("/update")
    public R updateById(@ApiParam(value = "章节对象",required = true) @RequestBody Chapter chapter){
        boolean result = chapterService.updateById(chapter);
        if (result) {
            return R.ok().message("修改成功");
        }else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("根据ID删除章节")
    @DeleteMapping("/delete/{id}")
    public R deleteById(@ApiParam(value = "章节ID",required = true) @PathVariable String id) {

        //删除视频：VOD
        videoService.removeMediaVideoByChapterId(id);
        boolean result = chapterService.removeChapterById(id);
        if (result) {
            return R.ok().message("删除成功");
        } else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("嵌套章节数据列表")
    @GetMapping("nested-list/{courseId}")
    public R nestedListByCourseId(@ApiParam(value = "课程ID",required = true) @PathVariable String courseId) {
        List<ChapterVo> chapterVos = chapterService.findChapterListByCourseId(courseId);
        return R.ok().data("items", chapterVos);
    }
}

