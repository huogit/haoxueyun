package com.prestrive.com.service.acl.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.prestrive.com.service.acl.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author strive
 * @since 2022-09-03
 */
public interface UserService extends IService<User> {

    /**
     * 更据page查询用户列表
     * @param page
     * @param limit
     * @param userQueryVo
     * @return
     */
    Page<User> selectUserListByPage(Long page, Long limit, User userQueryVo);

    /**
     * 获取user
     * @param username
     * @return
     */
    User selectByUsername(String username);
}
