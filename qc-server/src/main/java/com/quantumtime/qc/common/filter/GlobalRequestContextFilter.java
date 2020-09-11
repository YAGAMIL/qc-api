package com.quantumtime.qc.common.filter;

import com.alibaba.fastjson.JSON;
import com.quantumtime.qc.wrap.BaseInfo;
import io.micrometer.core.instrument.MeterRegistry;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Objects;
import javax.annotation.Nullable;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import static org.apache.commons.codec.CharEncoding.UTF_8;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;
/**
 * .Description:token解析过滤 & Created on 2019/10/28 11:20
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Slf4j
public class GlobalRequestContextFilter extends OncePerRequestFilter implements Ordered {

    public static final ThreadLocal<BaseInfo> REQUEST_CONTEXT = new ThreadLocal<>();

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final MeterRegistry registry;

    public GlobalRequestContextFilter(MeterRegistry registry) {
        this.registry = registry;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 9;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable FilterChain filterChain)
            throws ServletException, IOException {
        REQUEST_CONTEXT.remove();
        String header = request.getHeader("BaseInfo");
        if (StringUtils.isNotEmpty(header)) {
            BaseInfo baseInfo = JSON.parseObject(URLDecoder.decode(header, UTF_8), BaseInfo.class);
            log.debug("api获取用户信息--------" + baseInfo.getUser());
            REQUEST_CONTEXT.set(baseInfo);
        }
        Objects.requireNonNull(filterChain).doFilter(request, response);
    }
}
