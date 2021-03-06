package com.jhy.app.shiro;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.jhy.app.common.properties.AppProperties;
import com.jhy.app.common.utils.AppUtil;
import com.jhy.app.common.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.util.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Stream;

@Slf4j
public class JWTFilter extends BasicHttpAuthenticationFilter {

    //认证头的名称
    private static final String TOKEN = "Authorization";

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws UnauthorizedException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        AppProperties appProperties = SpringContextUtil.getBean(AppProperties.class);
        String[] anonUrl = StringUtils.splitByWholeSeparatorPreserveAllTokens(appProperties.getShiro().getAnonUrl(), StringPool.COMMA);

        boolean match = false;
        for (String u : anonUrl) {
            if (pathMatcher.match(u, httpServletRequest.getRequestURI()))
                match = true;
        }
        if (match) return true;
        if (isLoginAttempt(request, response)) {
            return executeLogin(request, response);
        }
        return false;
    }

    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader(TOKEN);
        return token != null;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader(TOKEN);
        JWTToken jwtToken = new JWTToken(AppUtil.decryptToken(token));
        try {
            getSubject(request, response).login(jwtToken);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个 option请求，这里我们给 option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    public static void main(String[] args) {
        String[] tokens = StringUtils.splitByWholeSeparatorPreserveAllTokens("/auth/login,/auth/logout/**,/auth/regist,/user/check/**",StringPool.COMMA);
        //Stream.of(tokens).forEach(System.out::println);
        boolean b = new AntPathMatcher().match("/auth/*", "/auth/login");
        //return true
        System.out.println(b);
        SocketUtils.findAvailableTcpPort(8080);
    }
}
