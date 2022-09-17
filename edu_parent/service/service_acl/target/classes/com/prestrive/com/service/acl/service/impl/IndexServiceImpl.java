package com.prestrive.com.service.acl.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.prestrive.com.common.base.result.ResultCodeEnum;
import com.prestrive.com.service.acl.entity.Role;
import com.prestrive.com.service.acl.entity.User;
import com.prestrive.com.service.acl.service.*;
import com.prestrive.com.service.base.exception.GlobalException;
import jdk.nashorn.internal.objects.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    UserService userService;

    @Autowired
    RolePermissionService rolePermissionService;

    @Autowired
    RoleService roleService;

    @Autowired
    PermissionService permissionService;

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 根据用户名获取用户登录信息
     *
     * @param username
     * @return
     */
    @Override
    public HashMap<String, Object> getUserInfo(String username) {
        HashMap<String, Object> result = new HashMap<>();

        //获取用户基本信息
        User user = userService.getOne(new QueryWrapper<User>().eq("username", username));
        if (null == user) {
            throw new GlobalException(ResultCodeEnum.FETCH_USERINFO_ERROR);
        }
        //获取改用户的菜单
        List<Role> roleList = roleService.selectRoleByUserId(user.getId());
        List<String> roleNames = roleList.stream().map(item -> item.getRoleName()).collect(Collectors.toList());
        if(roleNames.size() == 0) {
            //前端框架必须返回一个角色，否则报错，如果没有角色，返回一个空角色
            roleNames.add("");
        }

        //根据用户id获取操作权限值，存入redis ？
        List<String> permissionValueList = permissionService.selectPermissionValueByUserId(user.getId());
        redisTemplate.opsForValue().set(username, permissionValueList);

        result.put("name", user.getUsername());
        result.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        result.put("roles", roleNames);
        result.put("permissionValueList", permissionValueList);
        return result;

    }

    /**
     * 根据用户名获取动态菜单
     * @param username
     * @return
     */
    @Override
    public List<JSONObject> getMenu(String username) {
        User user = userService.selectByUsername(username);

        //根据用户id获取用户菜单权限
        List<JSONObject> permissionList = permissionService.selectPermissionByUserId(user.getId());
        return permissionList;
    }
}
