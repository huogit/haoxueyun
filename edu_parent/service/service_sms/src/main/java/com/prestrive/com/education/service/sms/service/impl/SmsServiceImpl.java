package com.prestrive.com.education.service.sms.service.impl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import com.prestrive.com.common.base.result.ResultCodeEnum;
import com.prestrive.com.education.service.sms.service.SmsService;
import com.prestrive.com.education.service.sms.util.SmsProperties;
import com.prestrive.com.service.base.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

    @Autowired
    SmsProperties smsProperties;

    @Override
    public void send(String mobile, String checkCode) throws ClientException {
        DefaultProfile profile = DefaultProfile.getProfile(smsProperties.getRegionId(),
                smsProperties.getKeyId(),
                smsProperties.getKeySecret());

        IAcsClient client = new DefaultAcsClient(profile);
        //配置阿里云公共请求
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        //设置请求参数
        request.putQueryParameter("RegionId",smsProperties.getRegionId());
        request.putQueryParameter("PhoneNumbers",mobile);
        request.putQueryParameter("SignName",smsProperties.getSignName());
        request.putQueryParameter("TemplateCode",smsProperties.getTemplateCode());

        //设置TemplateParam 短信模板变量对应的实际值 {"code":"1234"}
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("code", checkCode);
        //将包含验证码的集合转换为json字符串
        Gson gson = new Gson();
        request.putQueryParameter("TemplateParam",gson.toJson(hashMap));
        //发送请求，得到响应
        CommonResponse commonResponse = client.getCommonResponse(request);

        //得到json字符串格式的响应结果
        String data = commonResponse.getData();

        //解析json字符串格式的响应结果：成功或失败
        HashMap<String,String> map  = gson.fromJson(data, HashMap.class);
        String code = map.get("Code");
        String message = map.get("Message");

        //配置参考：短信服务->系统设置->国内消息设置

        if ("isv.BUSINESS_LIMIT_CONTROL".equals(code)) {
            log.error("短信发送过于频繁 " + "【code】" + code + ", 【message】" + message);
            throw new GlobalException(ResultCodeEnum.SMS_SEND_ERROR_BUSINESS_LIMIT_CONTROL);
        }
        if (!"OK".equals(code)) {
            log.error("短信发送失败 " + " - code: " + code + ", message: " + message);
            throw new GlobalException(ResultCodeEnum.SMS_SEND_ERROR);
        }
    }

    public static void main(String[] args) {

        String checkCode = "1234";
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("code", checkCode);

        //String s = hashMap.toString(); //{code=1234}
        Gson gson = new Gson();
        String s = gson.toJson(hashMap); //{"code":"1234"}

        System.out.println(s);
    }
}
