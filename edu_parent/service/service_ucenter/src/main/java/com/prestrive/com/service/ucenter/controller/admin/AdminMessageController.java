package com.prestrive.com.service.ucenter.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.prestrive.com.common.base.result.R;
import com.prestrive.com.service.ucenter.entity.Message;
import com.prestrive.com.service.ucenter.entity.MessageDescription;
import com.prestrive.com.service.ucenter.entity.vo.MessageInfoVo;
import com.prestrive.com.service.ucenter.service.MessageDescriptionService;
import com.prestrive.com.service.ucenter.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.MessageInfo;
import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author strive
 * @since 2022-09-14
 */
@Api(description = "消息")
@RestController
@RequestMapping("admin/ucenter/message")
public class AdminMessageController {

    @Autowired
    MessageService messageService;

    @ApiOperation("获取消息分页列表")
    @GetMapping("/list/{page}/{limit}")
    public R list(@ApiParam(value = "page",required = true) @PathVariable Long page,
                  @ApiParam(value = "每页记录数",required = true) @PathVariable Long limit,
                  @ApiParam(value = "查询条件",required = true) Message message)
    {
        Page<Message> param = new Page<>(page,limit);
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();

        if(StringUtils.hasLength(message.getTitle())) {
            queryWrapper.likeRight("title", message.getTitle());
        }

        Page<Message> pageModel = messageService.page(param,queryWrapper);
        List<Message> messageList = pageModel.getRecords();
        long total = pageModel.getTotal();

        return R.ok().data("items",messageList).data("total",total);
    }

    @ApiOperation("消息详情")
    @GetMapping("/get/{id}")
    public R list(@ApiParam(value = "消息id",required = true) @PathVariable Long id)
    {
        MessageInfoVo messageInfoVo = messageService.getMessageInfoById(id);

        if(messageInfoVo != null){
            return R.ok().data("item", messageInfoVo);
        }else{
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("新增消息")
    @PostMapping("/save")
    public R saveCourseInfo(
            @ApiParam(value = "消息表单",required = true)
            @RequestBody MessageInfoVo messageInfoVo) {

        String courseId = messageService.saveMessageInfo(messageInfoVo);
        return R.ok().data("messageId",courseId).message("保存成功");
    }

    @ApiOperation("更新消息数据")
    @PutMapping("/update")
    public R updateCourseInfoById(
            @ApiParam(value = "课程基本信息",required = true)
            @RequestBody MessageInfoVo messageInfoVo
    ) {
        boolean result = messageService.updateCourseInfoById(messageInfoVo);
        return R.ok().message("更新成功");

    }

    @ApiOperation("删除消息数据")
    @DeleteMapping("/remove/{id}")
    public R removeById(@ApiParam(value = "课程ID",required = true) @PathVariable String id) {

        //删除课程
        Boolean isDeleteCourse = messageService.deleteMessageById(id);
        if (isDeleteCourse) {
           return R.ok().message("删除成功");
        } else {
            return R.error().message("删除失败");
        }
    }

    @ApiOperation(value = "根据id列表删除消息")
    @DeleteMapping("batchRemove")
    public R batchRemove(@RequestBody List<String> idList) {
        messageService.removeMessageByIds(idList);
        return R.ok();
    }

}

