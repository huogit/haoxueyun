package com.prestrive.com.service.trade.service.impl;

import com.github.wxpay.sdk.WXPayUtil;
import com.prestrive.com.common.base.result.ResultCodeEnum;
import com.prestrive.com.common.base.util.HttpClientUtils;
import com.prestrive.com.service.base.exception.GlobalException;
import com.prestrive.com.service.trade.entity.Order;
import com.prestrive.com.service.trade.service.OrderService;
import com.prestrive.com.service.trade.service.WeixinPayService;
import com.prestrive.com.service.trade.util.WeixinPayProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class WeixinPayServiceImpl implements WeixinPayService {

    @Autowired
    OrderService orderService;
    @Autowired
    WeixinPayProperties weixinPayProperties;

    @Override
    public Map<String, Object> createNative(String orderNo, String remoteAddr) {
        //根据课程订单号获取订单信息
        Order order = orderService.getOrderByOrderNo(orderNo);

        //调用微信api接口：统一下单（支付订单）
        HttpClientUtils client = new HttpClientUtils("https://api.mch.weixin.qq.com/pay/unifiedorder");

        //组装接口参数
        Map<String, String> map = new HashMap<>();

        map.put("appid",weixinPayProperties.getAppId());//公众账号ID
        map.put("out_trade_no",orderNo);//商户订单号
        map.put("mch_id",weixinPayProperties.getPartner());//商户号
        map.put("body",order.getCourseTitle()); //商品描述
        map.put("nonce_str", WXPayUtil.generateNonceStr()); //随机字符串

        //注意，这里必须使用字符串类型的参数（总金额：分）
        String totalFee = order.getTotalFee().intValue() + "";
        map.put("total_fee",totalFee);//标价金额
        map.put("spbill_create_ip",remoteAddr);//客户端IP
        map.put("notify_url",weixinPayProperties.getNotifyUrl());//通知地址
        map.put("trade_type", "NATIVE");//交易类型


        String xmlParams = "";
        try {
            //将参数转换成xml字符串格式：生成带有签名的xml格式字符串
             xmlParams = WXPayUtil.generateSignedXml(map, weixinPayProperties.getPartnerKey());
            log.info("\n xmlParams：\n" + xmlParams);

            //组装请求并发起
            client.setXmlParam(xmlParams);//将参数放入请求对象的方法体
            client.setHttps(true);//使用https形式发送
            client.post();//发送请求
            String resultXml = client.getContent();//得到响应结果

            //解析响应
            Map<String, String> resultMap = WXPayUtil.xmlToMap(resultXml);

            //错误处理
            if("FAIL".equals(resultMap.get("return_code")) || "FAIL".equals(resultMap.get("result_code"))) {

                log.error("微信支付统一下单错误 - "+
                        "return_code:"
                                + resultMap.get("return_code")
                                + "return_msg:" + resultMap.get("return_msg")
                                + "result_code: " + resultMap.get("result_code")
                                + "err_code: " + resultMap.get("err_code")
                                + "err_code_des" + resultMap.get("err_code_des")
                        );
                throw new GlobalException(ResultCodeEnum.PAY_UNIFIEDORDER_ERROR);
            }

            //组装需要的内容
            Map<String, Object> data = new HashMap<>();
            data.put("result_code", resultMap.get("result_code"));//响应码
            data.put("code_url", resultMap.get("code_url"));//生成二维码的url
            data.put("course_id", order.getCourseId());//课程id
            data.put("total_fee", order.getTotalFee());//订单总金额
            data.put("out_trade_no", orderNo);//订单号

            return data;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResultCodeEnum.PAY_UNIFIEDORDER_ERROR);
        }

    }
}
