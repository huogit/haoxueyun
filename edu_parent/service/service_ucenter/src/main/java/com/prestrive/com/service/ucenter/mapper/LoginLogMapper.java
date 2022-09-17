package com.prestrive.com.service.ucenter.mapper;

import com.prestrive.com.service.ucenter.entity.LoginLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 登录日志 Mapper 接口
 * </p>
 *
 * @author strive
 * @since 2022-07-23
 */
@Repository
@Mapper
public interface LoginLogMapper extends BaseMapper<LoginLog> {

    Integer selectCourseNumByDay(String day);
}
