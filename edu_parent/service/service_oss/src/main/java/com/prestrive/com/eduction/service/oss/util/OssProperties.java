package com.prestrive.com.eduction.service.oss.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component //标注成组件，让springboot扫描到
//注意prefix要写到最后一个 "." 符号之前 如 aliyun:
//                                          oss:
@ConfigurationProperties(prefix = "aliyun.oss")
public class OssProperties {
    private String endpoint;
    private String keyid;
    private String keysecret;
    private String bucketname;
}
