package com.prestrive.com.service.acl.service;

import com.prestrive.com.service.acl.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author strive
 * @since 2022-09-03
 */
public interface RoleService extends IService<Role> {

    /**
     * 根据用户id获取角色列表
     * @param userId
     * @return
     */
    HashMap<String, Object> findRoleByUserId(String userId);

    HashMap<String, Object> test();

    void saveUserRoleRelationShip(String userId, String[] roleIds);

    List<Role> selectRoleByUserId(String id);
}
