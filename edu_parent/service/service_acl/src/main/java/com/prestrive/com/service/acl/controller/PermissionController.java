package com.prestrive.com.service.acl.controller;


import com.prestrive.com.common.base.result.R;
import com.prestrive.com.service.acl.entity.Permission;
import com.prestrive.com.service.acl.entity.RolePermission;
import com.prestrive.com.service.acl.service.PermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 权限 前端控制器
 * </p>
 *
 * @author strive
 * @since 2022-09-03
 */
@Api(description = "权限管理")
@RestController
@RequestMapping("/admin/acl/permission")
//@CrossOrigin
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @ApiOperation("获取全部菜单")
    @GetMapping("/list")
    public R indexAllPermission(){
        List<Permission> list = permissionService.queryAllMenu();

        return R.ok().data("items",list);
    }

    @ApiOperation("递归删除菜单")
    @DeleteMapping("/remove/{id}")
    public R remove(@ApiParam(value = "权限id",required = true) @PathVariable String id){
        permissionService.removeChildById(id);
        return R.ok();
    }

    @ApiOperation(value = "根据角色获取菜单")
    @GetMapping("/toAssign/{roleId}")
    public R toAssign(@PathVariable String roleId) {
        List<Permission> list = permissionService.selectAllMenu(roleId);
        return R.ok().data("children", list);
    }

    @ApiOperation(value = "新增菜单")
    @PostMapping("/save")
    public R save(@RequestBody Permission permission) {
        permissionService.save(permission);
        return R.ok();
    }

    @ApiOperation(value = "修改菜单")
    @PutMapping("update")
    public R updateById(@RequestBody Permission permission) {
        permissionService.updateById(permission);
        return R.ok();
    }

    @ApiOperation("给角色分配权限")
    @PostMapping("/do-assign")
    public R doAssign(String roleId, String[] permissionId){
        permissionService.saveRolePermissionRelation(roleId,permissionId);
        return R.ok();
    }
}

