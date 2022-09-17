package com.prestrive.com.service.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
@EnableRedisHttpSession
public class HttpSessionConfig {

    //可选配置
    @Bean
    public CookieSerializer cookieSerializer(){
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
        //我们可以将Spring Session默认的Cookie Key从SESSION替换为原生的JSESSIONID
        cookieSerializer.setCookieName("JSESSIONID");
        // CookiePath设置为根路径 让cookie 能被其他 path 存取值
        cookieSerializer.setCookiePath("/");
        // 配置了相关的正则表达式，可以达到同父域下的单点登录的效果(去掉二级或三级域名，只保留顶级域名)
        cookieSerializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$");

        return cookieSerializer;
    }
}
