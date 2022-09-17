package com.prestrive.com.service.acl.service;

import com.alibaba.fastjson.JSONObject;
import com.prestrive.com.service.acl.entity.Permission;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 权限 服务类
 * </p>
 *
 * @author strive
 * @since 2022-09-03
 */
public interface PermissionService extends IService<Permission> {

    /**
     * 获取全部菜单
     * @return
     */
    List<Permission> queryAllMenu();

    /**
     * 递归删除菜单
     * @param id
     */
    void removeChildById(String id);


    /**
     * 根据用户id 获取 该用户的权限的值
     * @param userId
     * @return
     */
    List<String> selectPermissionValueByUserId(String userId);


    List<JSONObject> selectPermissionByUserId(String userId);

    /**
     * 根据角色id获取权限
     * @param roleId
     * @return
     */
    List<Permission> selectAllMenu(String roleId);

    /**
     * 获取全部菜单
     * @return
     */
     List<Permission> queryAllMenuGuli();

    /**
     * 递归删除菜单
     * @param id
     */
     void removeChildByIdGuli(String id);

    /**
     *给角色分配权限
     * @param roleId
     * @param permissionIds
     */
     void saveRolePermissionRealtionShipGuli(String roleId, String[] permissionIds);

    /**
     * 保存角色权限关系
     * @param roleId
     * @param permissionId
     */
    void saveRolePermissionRelation(String roleId, String[] permissionId);
}
