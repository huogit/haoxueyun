package com.prestrive.com.service.acl.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.prestrive.com.common.base.result.R;
import com.prestrive.com.common.base.util.MD5;
import com.prestrive.com.service.acl.entity.Role;
import com.prestrive.com.service.acl.entity.User;
import com.prestrive.com.service.acl.entity.UserRole;
import com.prestrive.com.service.acl.service.RoleService;
import com.prestrive.com.service.acl.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.Result;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author strive
 * @since 2022-09-03
 */
@Api(description = "用户")
@RestController
@RequestMapping("/admin/acl/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @ApiOperation("获取管理用户分页列表")
    @GetMapping("/list/{page}/{limit}")
    public R ListPage(
            @ApiParam(value = "页码",required = true) @PathVariable("page") Long page,
            @ApiParam(value = "每页记录数",required = true) @PathVariable("limit") Long limit,
            @ApiParam(value = "查询对象", required = false) User userQueryVo
            ){

        Page<User> pageModel = userService.selectUserListByPage(page, limit, userQueryVo);
        return R.ok().data("items",pageModel.getRecords())
                .data("total",pageModel.getTotal());
    }

    @ApiOperation("新增管理用户")
    @PostMapping("/save")
    public R save(@ApiParam(value = "用户表单对象",required = true) @RequestBody User user){
        user.setPassword(MD5.encrypt(user.getPassword()));
        userService.save(user);
        return R.ok();
    }

    @ApiOperation(value = "修改管理用户")
    @PutMapping("/update")
    public R updateById(@RequestBody User user) {
        userService.updateById(user);
        return R.ok();
    }

    @ApiOperation(value = "删除管理用户")
    @DeleteMapping("remove/{id}")
    public R remove(@PathVariable String id) {
        userService.removeById(id);
        return R.ok();
    }

    @ApiOperation(value = "根据id列表删除管理用户")
    @DeleteMapping("batchRemove")
    public R batchRemove(@RequestBody List<String> idList) {
        userService.removeByIds(idList);
        return R.ok();
    }

    @ApiOperation(value = "根据用户id获取角色数据")
    @GetMapping("/toAssign/{userId}")
    public R toAssign(@PathVariable String userId) {
        HashMap<String, Object> roleMap = roleService.findRoleByUserId(userId);
        return R.ok().data(roleMap);
    }

    @ApiOperation(value = "根据用户分配角色")
    @PostMapping("/doAssign")
    public R doAssign(@RequestParam String userId,@RequestParam String[] roleId) {
        roleService.saveUserRoleRelationShip(userId,roleId);
        return R.ok();
    }

    @ApiOperation(value= "根据id获取用户信息")
    @GetMapping("/get/{id}")
    public R getById(@ApiParam(required = true,value = "管理员id") @PathVariable String id){
        User user = userService.getById(id);

        return R.ok().data("item",user);
    }


    @GetMapping("/test")
    public R test(){

        //return R.ok();
        HashMap<String, Object> map = roleService.test();

        return R.ok().data(map);
    }
}

