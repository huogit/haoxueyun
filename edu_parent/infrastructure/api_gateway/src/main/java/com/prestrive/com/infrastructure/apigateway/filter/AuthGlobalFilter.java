package com.prestrive.com.infrastructure.apigateway.filter;

import com.google.gson.JsonObject;
import com.prestrive.com.common.base.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Component //注解为spring组件，不然springboot无法识别，则不执行
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        //得到真实路由
        String path = request.getURI().getPath();

        //校验用户必须登录
        //路由匹配器
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        if(antPathMatcher.match("/api/**/auth/**",path)){
            //将 带有/api/**/auth/** 这些关键字的路由 进行鉴权
            List<String> tokenList = request.getHeaders().get("token");

            //没有token
            if(tokenList == null){
                //获取response
                ServerHttpResponse response = exchange.getResponse();
                log.info("filter:没有token");
                return out(response);
            }

            //token校验失败
            log.info(tokenList.get(0));
            boolean isCheck = JwtUtils.checkJwtTToken(tokenList.get(0));
            if(!isCheck){
                log.info("filter:token校验失败");
                ServerHttpResponse response = exchange.getResponse();
                return out(response);
            }
        }
            log.info("filter:token校验成功，放行");
            //放行
            return chain.filter(exchange);
    }

    //定义当前过滤器的优先级，值越小，优先级越高，越先通过
    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * 配置鉴权失败响应
     * @param response
     * @return
     */
    private Mono<Void> out(ServerHttpResponse response) {
        JsonObject message  = new JsonObject();
        message.addProperty("success",false);
        message.addProperty("code",28004);
        message.addProperty("data","");
        message.addProperty("message","鉴权失败");
        //转成字节数组
        byte[] bytes = message.toString().getBytes(StandardCharsets.UTF_8);

        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type","application/json;charset=UTF-8");

        return response.writeWith(Mono.just(buffer));
    }

}
