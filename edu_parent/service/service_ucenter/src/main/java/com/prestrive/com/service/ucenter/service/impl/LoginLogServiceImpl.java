package com.prestrive.com.service.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.prestrive.com.service.ucenter.entity.LoginLog;
import com.prestrive.com.service.ucenter.mapper.LoginLogMapper;
import com.prestrive.com.service.ucenter.service.LoginLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * 登录日志 服务实现类
 * </p>
 *
 * @author strive
 * @since 2022-07-23
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

    @Autowired
    LoginLogMapper loginLogMapper;

    @Override
    public Integer findLoginNumByDay(String day) {

        QueryWrapper<LoginLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("date(gmt_create)",day);
        return loginLogMapper.selectCount(queryWrapper);
    }

    @Override
    public void createOrUpdateByMemberId(String id, String remoteAddr) {
        //查询是否存在该日志
        QueryWrapper<LoginLog> loginLogQueryWrapper = new QueryWrapper<>();
        String day = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        //date(gmt_create) :sql的函数，将时间转换为 yyyy-MM-dd
        loginLogQueryWrapper.eq("date(gmt_create)",day);
        loginLogQueryWrapper.eq("member_id",id);
        LoginLog loginLog = loginLogMapper.selectOne(loginLogQueryWrapper);

        if(loginLog == null){
            //新增
            loginLog  = new LoginLog();
            loginLog.setIp(remoteAddr);
            loginLog.setLoginNum(1);
            loginLog.setMemberId(id);

            loginLogMapper.insert(loginLog);
        }else {
            //更新
            loginLog.setLoginNum(loginLog.getLoginNum() + 1);
            loginLog.setIp(remoteAddr);
            loginLogMapper.updateById(loginLog);
        }
    }
}
