package org.github.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.client.utils.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.github.utils.MD5Utils;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @ClassName: AuthAndLogFilter
 * @Description:  授权日志过滤器
 * @author: <p> 雅诗兰黛  熬夜不怕黑眼圈</p>
 * @date 2020-09-08 22:52
 * @Copyright: 墨铭琦妙代码研究中心
 */

@Component
public class AuthAndLogFilter implements GlobalFilter, Ordered {

    static final Logger logger = LogManager.getLogger("request");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        ServerHttpResponse serverHttpResponse = exchange.getResponse();

        StringBuilder logBuilder = new StringBuilder();
        Map<String, String> params = parseRequest(exchange, logBuilder);
        boolean r = checkSignature(params, serverHttpRequest);
        if(!r) {
            Map map = new HashMap<>();
            map.put("code", 2);
            map.put("message", "签名验证失败");
            String resp = JSON.toJSONString(map);
            logBuilder.append(",resp=").append(resp);
            logger.info(logBuilder.toString());
            DataBuffer bodyDataBuffer = serverHttpResponse.bufferFactory().wrap(resp.getBytes());
            serverHttpResponse.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
            return serverHttpResponse.writeWith(Mono.just(bodyDataBuffer));
        }

        DataBufferFactory bufferFactory = serverHttpResponse.bufferFactory();
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(serverHttpResponse) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                    return super.writeWith(fluxBody.map(dataBuffer -> {
                        byte[] content = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(content);
                        DataBufferUtils.release(dataBuffer);
                        String resp = new String(content, Charset.forName("UTF-8"));
                        logBuilder.append(",resp=").append(resp);
                        logger.info(logBuilder.toString());
                        byte[] uppedContent = new String(content, Charset.forName("UTF-8")).getBytes();
                        return bufferFactory.wrap(uppedContent);
                    }));
                }
                return super.writeWith(body);
            }
        };
        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    private Map<String, String> parseRequest(ServerWebExchange exchange, StringBuilder logBuilder) {
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        String method = serverHttpRequest.getMethodValue().toUpperCase();
        logBuilder.append(method).append(",").append(serverHttpRequest.getURI());
        MultiValueMap<String, String> query = serverHttpRequest.getQueryParams();
        Map<String, String> params = new HashMap<>();
        query.forEach((k, v) -> {
            params.put(k, v.get(0));
        });
        if("POST".equals(method)) {
            String body = exchange.getAttributeOrDefault("cachedRequestBody", "");
            if(StringUtils.isNotBlank(body)) {
                logBuilder.append(",body=").append(body);
                String[] kvArray = body.split("&");
                for (String kv : kvArray) {
                    if (kv.indexOf("=") >= 0) {
                        String k = kv.split("=")[0];
                        String v = kv.split("=")[1];
                        if(!params.containsKey(k)) {
                            try {
                                params.put(k, URLDecoder.decode(v, "UTF-8"));
                            } catch (UnsupportedEncodingException e) {
                            }
                        }
                    }
                }
            }
        }
        return params;
    }

    private boolean checkSignature(Map<String, String> params, ServerHttpRequest serverHttpRequest) {
        String sign = params.get("sign");
        if(StringUtils.isBlank(sign)) {
            return false;
        }
        //检查签名
        Map<String, String> sorted = new TreeMap<>();
        params.forEach( (k, v) -> {
            if(!"sign".equals(k)) {
                sorted.put(k, v);
            }
        });
        StringBuilder builder = new StringBuilder();
        sorted.forEach((k, v) -> {
            builder.append(k).append("=").append(v).append("&");
        });
        String value = builder.toString();
        value = value.substring(0, value.length() - 1);
        if(!sign.equalsIgnoreCase(MD5Utils.MD5(value))) {
            return false;
        }

        return true;
    }

    @Override
    public int getOrder() {
        return -20;
    }

}

