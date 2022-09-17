package com.prestrive.com.service.acl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.prestrive.com.service.acl.entity.User;
import com.prestrive.com.service.acl.entity.UserRole;
import com.prestrive.com.service.acl.mapper.UserMapper;
import com.prestrive.com.service.acl.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author strive
 * @since 2022-09-03
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public Page<User> selectUserListByPage(Long page, Long limit, User userQueryVo) {
        Page<User> userPage = new Page<>(page,limit);
        

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();



        String nickName = userQueryVo.getNickName();
        if(!StringUtils.isEmpty(nickName)){
            queryWrapper.likeRight("nick_name",nickName);
        }
        String username = userQueryVo.getUsername();
        if(!StringUtils.isEmpty(username)){
            queryWrapper.likeRight("username",username);
        }

        Page<User> pageModel = userMapper.selectPage(userPage, queryWrapper);

        return pageModel;
    }

    @Override
    public User selectByUsername(String username) {

        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));

        return user;
    }
}
