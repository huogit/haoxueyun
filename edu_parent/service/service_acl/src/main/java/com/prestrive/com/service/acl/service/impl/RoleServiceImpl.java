package com.prestrive.com.service.acl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.prestrive.com.service.acl.entity.Role;
import com.prestrive.com.service.acl.entity.UserRole;
import com.prestrive.com.service.acl.mapper.RoleMapper;
import com.prestrive.com.service.acl.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.prestrive.com.service.acl.service.UserRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author strive
 * @since 2022-09-03
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleService userRoleService;

    @Override
    public HashMap<String, Object> findRoleByUserId(String userId) {
        //查询所有的角色
        List<Role> allRolesList = this.list();

        //根据用户id，查询用户拥有的角色id
        List<UserRole> existUserRoleList = userRoleService.list(new QueryWrapper<UserRole>().eq("user_id", userId).select("role_id"));

        //c -> c.getRoleId() 将roleId取出来   collect(Collectors.toList()) 将stream 转换为 List
        List<String> existRoleList = existUserRoleList.stream().map(c->c.getRoleId()).collect(Collectors.toList());

        //对角色进行分类
        List<Role> assignRoles = new ArrayList<Role>();
        for (Role role : allRolesList) {
            //已分配
            //如果role的id列表 包含在里面，则将权限取出来
            if(existRoleList.contains(role.getId())) {
                assignRoles.add(role);
            }
        }

        HashMap<String, Object> roleMap = new HashMap<>();
        roleMap.put("assignRoles", assignRoles); // 该用户的角色
        roleMap.put("allRolesList", allRolesList); //所有的 角色
        return roleMap;

    }

    @Override
    public HashMap<String, Object> test() {
        //查询所有的角色
        List<Role> allRolesList =baseMapper.selectList(null);

        //根据用户id，查询用户拥有的角色id
        List<UserRole> existUserRoleList = userRoleService.list(new QueryWrapper<UserRole>().eq("user_id", 2).select("role_id"));

        //c -> c.getRoleId() 将roleId取出来   collect(Collectors.toList()) 将stream 转换为 List
        List<String> stringStream = existUserRoleList.stream().map(c -> c.getRoleId()).collect(Collectors.toList());

        List<String> existRoleList = existUserRoleList.stream().map(c->c.getRoleId()).collect(Collectors.toList());
        HashMap<String, Object> roleMap = new HashMap<>();
        roleMap.put("existRoleList", existUserRoleList);
        return roleMap;
    }

    @Override
    public void saveUserRoleRelationShip(String userId, String[] roleIds) {

        //删除之前的该用户的 角色
        userRoleService.remove(new QueryWrapper<UserRole>().eq("user_id",userId));

        ArrayList<UserRole> userRoles = new ArrayList<>();

        // 如果是这样的话 里面存的其实是一个 userRole 对象
//        UserRole userRole = new UserRole();
//        userRole.setUserId(userId);
        for (String roleId  : roleIds) {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            if(StringUtils.isEmpty(roleId)) continue;
            userRole.setRoleId(roleId);
            userRoles.add(userRole);

        }

        userRoleService.saveBatch(userRoles);
    }

    //获取 用户角色
    @Override
    public List<Role> selectRoleByUserId(String id) {
        List<UserRole> userRoles = userRoleService.list(new QueryWrapper<UserRole>().eq("user_id", id));

        List<String> roleIds = userRoles.stream().map(item -> item.getRoleId()).collect(Collectors.toList());

        return this.listByIds(roleIds);
    }
}
