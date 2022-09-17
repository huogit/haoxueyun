package com.prestrive.com.service.trade.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "weixin.pay")
public class WeixinPayProperties {

    private String appId; //关联的公众号appid
    private String partner; //商户号

    private String partnerKey; //商户key
    private String notifyUrl;   //回调地址

}
