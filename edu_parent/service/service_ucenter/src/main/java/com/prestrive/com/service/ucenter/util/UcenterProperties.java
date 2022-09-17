package com.prestrive.com.service.ucenter.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
//注意prefix要写到最后一个 "." 符号之前
@ConfigurationProperties(prefix = "wx.open")
@Component
public class UcenterProperties {
    private String appId;
    private String appSecret;
    private String redirectUri;
}
