package com.prestrive.com.service.acl.mapper;

import com.prestrive.com.service.acl.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author strive
 * @since 2022-09-03
 */
@Repository
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
