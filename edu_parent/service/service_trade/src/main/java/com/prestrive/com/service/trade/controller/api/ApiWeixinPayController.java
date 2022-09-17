package com.prestrive.com.service.trade.controller.api;

import com.github.wxpay.sdk.WXPayUtil;
import com.prestrive.com.common.base.result.R;
import com.prestrive.com.common.base.util.StreamUtils;
import com.prestrive.com.service.trade.entity.Order;
import com.prestrive.com.service.trade.service.OrderService;
import com.prestrive.com.service.trade.service.WeixinPayService;
import com.prestrive.com.service.trade.util.WeixinPayProperties;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Api(description = "网站微信支付")
@RestController
@RequestMapping("/api/trade/weixin-pay")
//@CrossOrigin
@Slf4j
public class ApiWeixinPayController {

    @Autowired
    WeixinPayService weixinPayService;
    @Autowired
    OrderService orderService;

    @Autowired
    private WeixinPayProperties weixinPayProperties;

    @GetMapping("/create-native/{orderNo}")
    public R createNative(@PathVariable String orderNo, HttpServletRequest request){

        String remoteAddr = request.getRemoteAddr(); //获取客户端ip地址

        Map<String, Object> data = weixinPayService.createNative(orderNo, remoteAddr);

        return R.ok().data("native",data);
    }

    /**
     * 支付回调：注意这里是【post】方式
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/callback/notify")
    public String wxNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("callback/notify被调用");

        //获取通知结果
        //只有POST请求方式，才有请求体，在请求体中封装了POST请求的请求参数
        ServletInputStream inputStream = request.getInputStream();//获取字节输入流，可以操作所有类型数据
        //将字节输入流（body的数据）转换成String字符串(xml字符串)
        String notifyXml = StreamUtils.inputStream2String(inputStream, "utf-8");
        System.out.println(notifyXml);

        //定义响应对象
        HashMap<String, String> result = new HashMap<>();

        //签名验证：防止伪造回调
        if(WXPayUtil.isSignatureValid(notifyXml,weixinPayProperties.getPartnerKey())){
           //解析返回结果
            Map<String, String> notifyMap = WXPayUtil.xmlToMap(notifyXml);

            //判断是否支付成功
            if("SUCCESS".equals(notifyMap.get("result_code"))){

                // 校验订单金额是否一致
                String total_fee = notifyMap.get("total_fee");
                String out_trade_no = notifyMap.get("out_trade_no");

                Order order = orderService.getOrderByOrderNo(out_trade_no);

                if(order != null && order.getTotalFee().intValue() == Integer.parseInt(total_fee)){
                    // 判断订单状态：保证接口调用的幂等性，如果订单状态已更新直接返回成功响应
                    // 幂等性：无论调用多少次结果都是一样的
                    if(order.getStatus() == 1){
                        result.put("return_code","SUCCESS");
                        result.put("return_msg","OK");
                        String resultXml = WXPayUtil.mapToXml(result);
                        response.setContentType("text/xml");
                        log.warn("通知已处理");
                        return resultXml;
                    }else{
                        // 更新订单支付状态，并返回成功响应
                        orderService.updateOrderStatusByNotifyMap(notifyMap);
                        result.put("return_code","SUCCESS");
                        result.put("return_msg","OK");
                        String resultXml = WXPayUtil.mapToXml(result);
                        response.setContentType("text/xml");
                        log.warn("支付成功，通知已处理");
                        return resultXml;
                    }
                }
            }

        }
        // 校验失败，返回失败应答
        result.put("return_code","FAIL");
        result.put("return_msg","");
        String resultXml = WXPayUtil.mapToXml(result);
        response.setContentType("text/xml");
        log.warn("校验失败");
        return resultXml;
    }

}
