package com.prestrive.com.service.acl.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public interface IndexService  {
    /**
     *根据用户名获取用户登录信息
     * @param username
     * @return
     */
    HashMap<String,Object> getUserInfo(String username);

    /**
     * 更加用户名获取菜单
     * @param username
     * @return
     */
    List<JSONObject> getMenu(String username);
}
