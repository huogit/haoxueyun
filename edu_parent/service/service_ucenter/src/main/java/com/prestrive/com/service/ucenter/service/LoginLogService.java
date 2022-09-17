package com.prestrive.com.service.ucenter.service;

import com.prestrive.com.service.ucenter.entity.LoginLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 登录日志 服务类
 * </p>
 *
 * @author strive
 * @since 2022-07-23
 */
public interface LoginLogService extends IService<LoginLog> {

    /**
     * 查询登录数
     * @param day
     * @return
     */
    Integer findLoginNumByDay(String day);

    /**
     * 记录登录日志
     * @param id
     * @param remoteAddr
     */
    void createOrUpdateByMemberId(String id, String remoteAddr);
}
