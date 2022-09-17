package com.prestrive.com.service.acl.service;

import com.prestrive.com.service.acl.entity.RolePermission;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 角色权限 服务类
 * </p>
 *
 * @author strive
 * @since 2022-09-03
 */
public interface RolePermissionService extends IService<RolePermission> {

    /**
     * 保存 角色与权限的关系
     * @param roleId
     * @param permissionIds
     */
    void saveRolePermissionRelation(String roleId, String[] permissionIds);
}
