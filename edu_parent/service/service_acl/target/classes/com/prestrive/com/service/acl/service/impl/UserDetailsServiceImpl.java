package com.prestrive.com.service.acl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.prestrive.com.security.entity.SecurityUser;
import com.prestrive.com.service.acl.entity.Permission;
import com.prestrive.com.service.acl.entity.User;
import com.prestrive.com.service.acl.service.PermissionService;
import com.prestrive.com.service.acl.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 自定义userDetailsService - 认证用户详情
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserService userService;

    @Autowired
    PermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getOne(new QueryWrapper<User>().eq("username", username));
        if(user == null){
            throw new UsernameNotFoundException("用户名不存在！");
        }

        com.prestrive.com.security.entity.User curUser = new com.prestrive.com.security.entity.User();
        BeanUtils.copyProperties(user,curUser);

        //把 用户信息 设置到 安全认证用户详情信息securityUser
        SecurityUser securityUser = new SecurityUser(curUser);

        //获取该用户的权限,并存入
        List<String> authorities = permissionService.selectPermissionValueByUserId(user.getId());
        securityUser.setPermissionValueList(authorities);
        return securityUser;
    }
}
