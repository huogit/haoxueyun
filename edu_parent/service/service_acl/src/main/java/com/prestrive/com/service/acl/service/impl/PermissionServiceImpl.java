package com.prestrive.com.service.acl.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.prestrive.com.service.acl.entity.Permission;
import com.prestrive.com.service.acl.entity.RolePermission;
import com.prestrive.com.service.acl.entity.User;

import com.prestrive.com.service.acl.helper.MemuHelper;
import com.prestrive.com.service.acl.helper.PermissionHelper;
import com.prestrive.com.service.acl.mapper.PermissionMapper;
import com.prestrive.com.service.acl.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.prestrive.com.service.acl.service.RolePermissionService;
import com.prestrive.com.service.acl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 权限 服务实现类
 * </p>
 *
 * @author strive
 * @since 2022-09-03
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    PermissionMapper permissionMapper;

    @Autowired
    UserService userService;
    
    @Autowired
    RolePermissionService rolePermissionService;

    @Override
    public List<Permission> queryAllMenu() {

        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();

        queryWrapper.orderByDesc("id");

        List<Permission> permissionList = permissionMapper.selectList(queryWrapper);

        return build(permissionList);
    }

    /**
     * 递归删除菜单
     * @param id
     */
    @Override
    public void removeChildById(String id) {
        List<String> idList = new ArrayList<>();
        this.selectChildListById(id,idList);

        //把根据节点id放到list中
        idList.add(id);
        //批量删除
        permissionMapper.deleteBatchIds(idList);
    }

    @Override
    public List<String> selectPermissionValueByUserId(String userId) {
        List<String> selectPermissionValueList = null;
        if(this.isSysAdmin(userId)) {
            //如果是系统管理员，获取所有权限
            selectPermissionValueList = permissionMapper.selectAllPermissionValue();
        } else {
            selectPermissionValueList = permissionMapper.selectPermissionValueByUserId(userId);
        }
        return selectPermissionValueList;

    }

    @Override
    public List<JSONObject> selectPermissionByUserId(String userId) {
        List<Permission> selectPermissionList = null;
        if(this.isSysAdmin(userId)) {
            //如果是超级管理员，获取所有菜单
            selectPermissionList = permissionMapper.selectList(null);
        } else {
            selectPermissionList = permissionMapper.selectPermissionByUserId(userId);
        }
        //把 菜单和子菜单排列号 ，返回
        List<Permission> permissionList = PermissionHelper.build(selectPermissionList);
        List<JSONObject> result = MemuHelper.build(permissionList);
        return result;
    }

    @Override
    public List<Permission> selectAllMenu(String roleId) {

        //查出所有权限 cast(id as signed)  把id 的数据类型 转换 整数
        List<Permission> allPermissionList = permissionMapper.selectList(new QueryWrapper<Permission>().orderByDesc("CAST(id AS SIGNED)"));

        //查出该角色的 角色与权限关系表 的数据
        List<RolePermission> rolePermissionList = rolePermissionService.list(new QueryWrapper<RolePermission>().eq("role_id", roleId));
        
        //循环 比对 选中 权限表的数据
        for (Permission permission : allPermissionList) {
            for (RolePermission rolePermission : rolePermissionList) {
                if(rolePermission.getPermissionId().equals(permission.getId())){
                    permission.setSelect(true);
                }
            }
        }
        //并将菜单进行分级
        List<Permission> permissionList = build(allPermissionList);

        return permissionList;
    }

    /**
     * 判断用户是否系统管理员
     * @param userId
     * @return
     */
    private boolean isSysAdmin(String userId) {
        User user = userService.getById(userId);

        if(null != user && "admin".equals(user.getUsername())) {
            return true;
        }
        return false;
    }


    /**
     * 根据父节点 查询子节点，并存入idList
     * @param id 父节点
     * @param idList 存储列表
     */
    private void selectChildListById(String id, List<String> idList){
        List<Permission> childList = permissionMapper.selectList(new QueryWrapper<Permission>().select("id").eq("pid", id));

        childList.stream().forEach(item -> {
            if(item == null){
                return;
            }
            idList.add(item.getId());
            //继续递归 寻找 子节点的子节点
            this.selectChildListById(item.getId(),idList);
        });
    }

    /**
     * 使用递归方法建菜单
     * @param treeNodes
     * @return
     */
    private static List<Permission> build(List<Permission> treeNodes) {
        List<Permission> trees = new ArrayList<>();
        for (Permission treeNode : treeNodes) {
            if ("0".equals(treeNode.getPid())) {
                treeNode.setLevel(1);
                trees.add(findChildren(treeNode,treeNodes));
            }
        }
        return trees;
    }

    /**
     * 递归查找子节点
     * @param treeNodes
     * @return
     */
    private static Permission findChildren(Permission treeNode,List<Permission> treeNodes) {
        treeNode.setChildren(new ArrayList<Permission>());

        for (Permission it : treeNodes) {
            if(treeNode.getId().equals(it.getPid())) {
                int level = treeNode.getLevel() + 1;
                it.setLevel(level);
                if (treeNode.getChildren() == null) {
                    treeNode.setChildren(new ArrayList<>());
                }
                treeNode.getChildren().add(findChildren(it,treeNodes));
            }
        }
        return treeNode;
    }


    //========================递归查询所有菜单================================================
    //获取全部菜单
    @Override
    public List<Permission> queryAllMenuGuli() {
        //1 查询菜单表所有数据
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        List<Permission> permissionList = baseMapper.selectList(wrapper);
        //2 把查询所有菜单list集合按照要求进行封装
        List<Permission> resultList = bulidPermission(permissionList);
        return resultList;
    }

    //把返回所有菜单list集合进行封装的方法
    public static List<Permission> bulidPermission(List<Permission> permissionList) {

        //创建list集合，用于数据最终封装
        List<Permission> finalNode = new ArrayList<>();
        //把所有菜单list集合遍历，得到顶层菜单 pid=0菜单，设置level是1
        for(Permission permissionNode : permissionList) {
            //得到顶层菜单 pid=0菜单
            if("0".equals(permissionNode.getPid())) {
                //设置顶层菜单的level是1
                permissionNode.setLevel(1);
                //根据顶层菜单，向里面进行查询子菜单，封装到finalNode里面
                finalNode.add(selectChildren(permissionNode,permissionList));
            }
        }
        return finalNode;
    }

    private static Permission selectChildren(Permission permissionNode, List<Permission> permissionList) {
        //1 因为向一层菜单里面放二层菜单，二层里面还要放三层，把对象初始化
        permissionNode.setChildren(new ArrayList<Permission>());

        //2 遍历所有菜单list集合，进行判断比较，比较id和pid值是否相同
        for(Permission it : permissionList) {
            //判断 id和pid值是否相同
            if(permissionNode.getId().equals(it.getPid())) {
                //把父菜单的level值+1
                int level = permissionNode.getLevel()+1;
                it.setLevel(level);
                //如果children为空，进行初始化操作
                if(permissionNode.getChildren() == null) {
                    permissionNode.setChildren(new ArrayList<Permission>());
                }
                //把查询出来的子菜单放到父菜单里面
                permissionNode.getChildren().add(selectChildren(it,permissionList));
            }
        }
        return permissionNode;
    }

    //============递归删除菜单==================================
    @Override
    public void removeChildByIdGuli(String id) {
        //1 创建list集合，用于封装所有删除菜单id值
        List<String> idList = new ArrayList<>();
        //2 向idList集合设置删除菜单id
        this.selectPermissionChildById(id,idList);
        //把当前id封装到list里面
        idList.add(id);
        baseMapper.deleteBatchIds(idList);
    }

    //2 根据当前菜单id，查询菜单里面子菜单id，封装到list集合
    private void selectPermissionChildById(String id, List<String> idList) {
        //查询菜单里面子菜单id
        QueryWrapper<Permission>  wrapper = new QueryWrapper<>();
        wrapper.eq("pid",id);
        wrapper.select("id");
        List<Permission> childIdList = baseMapper.selectList(wrapper); // 这个为空时结束
        //把childIdList里面菜单id值获取出来，封装idList里面，做递归查询
        childIdList.stream().forEach(item -> {
            //封装idList里面
            idList.add(item.getId()); //放在循环体前面或后面都差不多，只是顺序不一样罢了
            //递归查询
            this.selectPermissionChildById(item.getId(),idList);
        });
    }

    //=========================给角色分配菜单=======================
    @Override
    public void saveRolePermissionRealtionShipGuli(String roleId, String[] permissionIds) {
        //roleId角色id
        //permissionId菜单id 数组形式
        //1 创建list集合，用于封装添加数据
        List<RolePermission> rolePermissionList = new ArrayList<>();
        //遍历所有菜单数组
        for(String perId : permissionIds) {
            //RolePermission对象
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(perId);
            //封装到list集合
            rolePermissionList.add(rolePermission);
        }
        //添加到角色菜单关系表
        rolePermissionService.saveBatch(rolePermissionList);
    }

    @Override
    public void saveRolePermissionRelation(String roleId, String[] permissionId) {
        rolePermissionService.saveRolePermissionRelation(roleId,permissionId);
    }
}
