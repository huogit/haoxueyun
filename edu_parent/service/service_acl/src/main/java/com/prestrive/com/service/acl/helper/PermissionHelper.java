package com.prestrive.com.service.acl.helper;

import com.prestrive.com.service.acl.entity.Permission;

import java.util.ArrayList;
import java.util.List;

/**
 * 更据权限数据构建菜单数据：从数据库取出来 构建
 */
public class PermissionHelper {

    /**
     * 使用递归方法建菜单
     * @param treeNodes 全部菜单（包括父子菜单）
     * @return
     */
    public static List<Permission> build(List<Permission> treeNodes){
        //构建一级菜单
        ArrayList<Permission> trees = new ArrayList<>();

        //虽然只有一个 但是不知道在哪里，所以循环找到 一级
        for (Permission treeNode : treeNodes) {
            if("0".equals(treeNode.getPid())){ //没有上级，即一级菜单
                treeNode.setLevel(1);
                trees.add(findChildren(treeNode,treeNodes)); //寻找子菜单，并存入list中
            }
        }
        return trees;
    }

    /**
     * 递归就是 把最后要存的或者要持续得到的东西 搞成方法递归循环利用 ，
     * 递归查找子节点，将子节点 Children 取出来设置层级并存到 ArrayList 列表里，直到 for循环 结束（即全部都查过）
     * @param treeNode
     * @param treeNodes
     * @return
     */
    public static Permission findChildren(Permission treeNode,List<Permission> treeNodes){
        //父节点，设置 子节点数组存放子节点
        treeNode.setChildren(new ArrayList<Permission>());

        //寻找子节点
        for (Permission it : treeNodes) {
            if(treeNode.getId().equals(it.getPid())){ //子节点的父节点 是 传过来的节点
              int level =  treeNode.getLevel() + 1; //是 则是子节点 层级 +1
              it.setLevel(level);
              if(treeNode.getChildren() == null){
                  treeNode.setChildren(new ArrayList<>()); //防止报错，下一次 if判断也过不了
              }
              treeNode.getChildren().add(findChildren(it,treeNodes));//继续 添加 n维 数据
            }
        }
        return treeNode; //if判断为否了 就结束递归
    }


}
