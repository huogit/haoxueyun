package com.prestrive.com.service.edu.controller.admin;


import com.prestrive.com.common.base.result.R;
import com.prestrive.com.common.base.result.ResultCodeEnum;
import com.prestrive.com.service.edu.entity.Teacher;
import com.prestrive.com.service.edu.entity.vo.TeacherQueryVo;
import com.prestrive.com.service.edu.feign.OssFileService;
import com.prestrive.com.service.edu.service.TeacherService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//@CrossOrigin //允许跨域
@Slf4j
@RestController
@RequestMapping("/admin/edu/teacher")
public class TeacherController {
    @Autowired
    TeacherService teacherService;
    @Autowired
    OssFileService ossFileService;

    @ApiOperation("所有讲师列表")
    @GetMapping("/list")
    public R listAll(){

        log.info("所有讲师列表....................");
        List<Teacher> list = teacherService.list();
        return R.ok().data("items",list);
    }

    @ApiOperation("删除讲师")
    @DeleteMapping("remove/{id}")
    public R removeTeacher(@ApiParam(value = "讲师id",required = true) @PathVariable("id") String id){

        //删除图片
        boolean b = teacherService.removeAvatarById(id);
        //删除讲师
        boolean result = teacherService.removeById(id);
        if (result) {
            return R.ok().message("删除成功");
        }else{
           return R.error().message("数据不存在");
        }
    }

    @ApiOperation("根据id列表删除讲师")
    @DeleteMapping("batch-remove")
    public R removeByIdList(@ApiParam(value = "id列表",required = true)
                                @RequestBody ArrayList<String> idList){
        boolean result = teacherService.removeByIds(idList);
        if(result){
            return R.ok().message("删除成功");
        }else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("讲师分页列表")
    @GetMapping("/listPage/{page}/{limit}")
    public R listPage(@ApiParam(value = "页码",required = true) @PathVariable("page") Integer page,
                      @ApiParam(value = "每页记录数",required = true) @PathVariable("limit") Integer limit,
                      @ApiParam("查询条件") TeacherQueryVo teacherQueryVo){

        R result = teacherService.findByPage(page,limit,teacherQueryVo);
        return result;
    }

    /**
     *  post：有请求体，get没有请求体，有路由参数
     * @param teacher
     * @return
     */
    @ApiOperation("新增教师")
    @PostMapping("/save")
    public R save(@ApiParam(value = "新增教师",required = true) @RequestBody Teacher teacher){
        //Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        if(teacher == null){
            return R.result(ResultCodeEnum.PARAM_ERROR);
        }

        boolean save = teacherService.save(teacher);
        if(save){
            return R.ok().message("保存成功");
        }else {
            return R.error().message("保存失败");
        }
    }


    @ApiOperation("更新教师信息")
    @PostMapping("/update")
    public R update(@ApiParam(value = "教师信息",required = true) @RequestBody Teacher teacher){
        if(teacher == null){
            return R.result(ResultCodeEnum.PARAM_ERROR);
        }

        boolean update = teacherService.updateById(teacher);

        if(update){
            return R.ok().message("更新成功");
        }else {
            return R.error().message("数据错误");
        }
    }

    @ApiOperation("根据id获取教师信息")
    @GetMapping("/get/{id}")
    public R getById(@ApiParam(value = "教师id",required = true) @PathVariable("id") String id){

        boolean b = StringUtils.hasLength(id);
        if(StringUtils.isEmpty(id)){
            return R.result(ResultCodeEnum.PARAM_ERROR);
        }

        Teacher teacher = teacherService.getById(id);

        if(teacher != null){
            return R.ok().data("item",teacher);
        }else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("根据左关键字查询讲师名称")
    @GetMapping("/list/name/{key}")
    public R selectNameListByKey(
            @ApiParam(value = "查询关键字",required = true)
            @PathVariable("key") String key
    ){
        List<Map<String, Object>> nameList = teacherService.selectNameListByKey(key);
        return R.ok().data("nameList",nameList);

    }


    @ApiOperation("测试服务调用")
    @GetMapping("test")
    public R test(){
        ossFileService.test();
        return R.ok();
    }
    @ApiOperation("测试高并发")
    @GetMapping("test_conCurrent")
    public R testConcurrent(){
        log.info("test_conCurrent");
        ossFileService.test();
        return R.ok();
    }

    @ApiOperation("sentinel测试1")
    @GetMapping("/message1")
    public R message1(){
        return R.ok().message("message1");
    }

    @ApiOperation("sentinel测试2")
    @GetMapping("/message2")
    public R message2(){
        return R.ok().message("message2");
    }

}
