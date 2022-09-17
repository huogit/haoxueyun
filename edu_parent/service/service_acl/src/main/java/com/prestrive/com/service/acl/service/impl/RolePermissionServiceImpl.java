package com.prestrive.com.service.acl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.prestrive.com.service.acl.entity.RolePermission;
import com.prestrive.com.service.acl.mapper.RolePermissionMapper;
import com.prestrive.com.service.acl.service.RolePermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

/**
 * <p>
 * 角色权限 服务实现类
 * </p>
 *
 * @author strive
 * @since 2022-09-03
 */
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {

    @Autowired
    RolePermissionMapper rolePermissionMapper;

    @Override
    public void saveRolePermissionRelation(String roleId, String[] permissionIds) {
        //删除该角色之前的权限
        rolePermissionMapper.delete(new QueryWrapper<RolePermission>().eq("role_id",roleId));

        //将角色 与 权限 一个一个的关联起来到 实体类
        ArrayList<RolePermission> rolePermissions = new ArrayList<>();
        for (String permissionId : permissionIds) {
            if(StringUtils.isEmpty(permissionId)){
                continue;//中止当前迭代的循环，进入下一次的迭代
            }
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            rolePermissions.add(rolePermission);
        }
        // 批量保存
        this.saveBatch(rolePermissions);

    }
}
