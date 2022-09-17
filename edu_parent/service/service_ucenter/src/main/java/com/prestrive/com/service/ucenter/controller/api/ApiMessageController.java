package com.prestrive.com.service.ucenter.controller.api;


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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("api/ucenter/message")
public class ApiMessageController {

    @Autowired
    MessageService messageService;


    @ApiOperation("获取消息分页列表")
    @GetMapping("/list/{page}/{limit}")
    public R list(@ApiParam(value = "page",required = true) @PathVariable Long page,
                  @ApiParam(value = "每页记录数",required = true) @PathVariable Long limit)
    {
        Page<Message> param = new Page<>(page,limit);

        Page<Message> pageModel = messageService.page(param);
        List<Message> messageList = pageModel.getRecords();
        long total = pageModel.getTotal();

        return R.ok().data("items",messageList).data("total",total);
    }

    @ApiOperation("消息详情")
    @GetMapping("/get/{id}")
    public R list(@ApiParam(value = "消息id",required = true) @PathVariable String id)
    {

        MessageInfoVo messageInfoVo = messageService.selectMessageInfoById(id);

        return R.ok().data("item", messageInfoVo);
    }


}

