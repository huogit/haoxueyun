package com.prestrive.com.service.ucenter.controller.api;

import com.google.gson.Gson;
import com.prestrive.com.common.base.result.ResultCodeEnum;
import com.prestrive.com.common.base.util.ExceptionUtils;
import com.prestrive.com.common.base.util.HttpClientUtils;
import com.prestrive.com.common.base.util.JwtInfo;
import com.prestrive.com.common.base.util.JwtUtils;
import com.prestrive.com.service.base.exception.GlobalException;
import com.prestrive.com.service.ucenter.entity.Member;
import com.prestrive.com.service.ucenter.service.LoginLogService;
import com.prestrive.com.service.ucenter.service.MemberService;
import com.prestrive.com.service.ucenter.util.UcenterProperties;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.UUID;

@Api("微信登录")
@Controller
@RequestMapping("/api/ucenter/wx")
@Slf4j
class ApiWxController {

    @Autowired
    UcenterProperties ucenterProperties;
    @Autowired
    MemberService memberService;
    @Autowired
    LoginLogService loginLogService;

    @GetMapping("/login")
    public String login(HttpSession session) {
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        //处理回调url
        String redirectUrl = "";
        try {
            redirectUrl = URLEncoder.encode(ucenterProperties.getRedirectUri(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GlobalException(ResultCodeEnum.URL_ENCODE_ERROR);
        }

        //处理state：生成随机数，存入session
        String state = UUID.randomUUID().toString();
        log.info("生成 state = " + state);
        session.setAttribute("wx_open_state", state);

        //拼接url
        String qrcodeUrl  = String.format(
                baseUrl,
                ucenterProperties.getAppId(),
                redirectUrl,
                state
        );

        return "redirect:" + qrcodeUrl;

    }

    @GetMapping("/callback")
    public String callback(String code , String state,HttpSession session){
        //回调被拉起，并获得code和state参数
        log.info("callback被调用");
        log.info("code:"+code);
        log.info("status:" + state);

        //校验参数
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(state)){
            log.info("非法回调请求");
            throw new GlobalException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }

        //校验state 防止crf攻击
        String wx_open_state = session.getAttribute("wx_open_state").toString();
        if(!code.equals(wx_open_state)){
            log.info("非法回调请求");
            throw new GlobalException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }

        //携带code和appid以及appsecret请求access_token
        String accessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";

        HashMap<String, String> params = new HashMap<>();
        params.put("appid",ucenterProperties.getAppId());
        params.put("secret",ucenterProperties.getAppSecret());
        params.put("code",code);
        params.put("grant_type","authorization_code");
        HttpClientUtils client  = new HttpClientUtils(accessTokenUrl, params);

        String result = "";
        try {
            client.get();
            //响应结果
            result = client.getContent();
        } catch (Exception e) {

            log.error("获取access_token失败: "+ ExceptionUtils.getMessage(e));
            throw new GlobalException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }

        Gson gson = new Gson();
        HashMap<String,Object> resultMap = gson.fromJson(result, HashMap.class);

        //判断微信获取access_token失败的响应
        Object  errcodeObj = resultMap.get("errcode");
        if(errcodeObj != null){
            String errmsg = (String)resultMap.get("errmsg");
            Double errcode = (Double)errcodeObj;

            log.error("获取access_token失败 - " + "message: " + errmsg + ", errcode: " + errcode);
            throw new GlobalException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }

        //微信获取access_token响应成功
        String access_token = resultMap.get("access_token").toString();
        String openid = resultMap.get("openid").toString();

        //查询是否存在该openid
        Member member =  memberService.selectByOpenId(openid);
        if (member == null){
            //根据access_token获取微信用户的基本信息
            //向微信的资源服务器发起请求，获取当前用户的用户信息
            String baseUserInfoUrl  = "https://api.weixin.qq.com/sns/userinfo";
            HashMap<String, String> map = new HashMap<>();
            map.put("access_token",access_token);
            map.put("openid",openid);

            //发起请求
             client = new HttpClientUtils(baseUserInfoUrl, map);
            String userInfoResult ="";
            try {
                client.get();
                userInfoResult = client.getContent();
            } catch (Exception e) {
               log.error("msg: 获取userInfoResult失败"+ExceptionUtils.getMessage(e));
               throw new GlobalException(ResultCodeEnum.FETCH_USERINFO_ERROR);
            }

            HashMap<String,Object> userInfo = gson.fromJson(userInfoResult, HashMap.class);
            Object userInfoErrCodeObj = userInfo.get("errcode");
            if(userInfoErrCodeObj != null){
                String userInfoErrMsg = userInfo.get("errmsg").toString();
                String userInfoErrCode = userInfoErrCodeObj.toString();
                log.error("获取用户信息失败,errCode：" + userInfoErrCode +"，message：" + userInfoErrMsg);
                throw new GlobalException(ResultCodeEnum.FETCH_USERINFO_ERROR);
            }

            //用户注册
             member = memberService.saveByUserInfo(userInfo);

        }
        JwtInfo jwtInfo = new JwtInfo();
        BeanUtils.copyProperties(member,jwtInfo);
        //记录登陆日志
        loginLogService.createOrUpdateByMemberId(member.getId(),"");
        String jwtToken = JwtUtils.getJwtToken(jwtInfo, 1800);
        //携带token跳转到首页
        return "redirect:http://localhost:3000?token=" + jwtToken;
    }

}
