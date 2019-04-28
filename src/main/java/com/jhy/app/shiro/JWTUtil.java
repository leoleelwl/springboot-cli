package com.jhy.app.shiro;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jhy.app.common.properties.AppProperties;
import com.jhy.app.common.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

@Slf4j
public class JWTUtil {

    private static final long EXPIRE_TIME = SpringContextUtil.getBean(AppProperties.class).getShiro().getJwtTimeOut() * 1000;

    /**
     * 校验 token是否正确
     *
     * @param token  密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, String username, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", username)
                    .build();
            verifier.verify(token);
            log.info("token is valid");
            return true;
        }catch (TokenExpiredException e) {
            log.info("token is invalid{}", e.getMessage());
            return false;
        }catch (InvalidClaimException e){
            log.info("token has expired{}", e.getMessage());
            return false;
        }catch (Exception e){
            log.info("token is invalid{}", e.getMessage());
            return false;
        }
    }

    /**
     * 从 token中获取用户名
     *
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            log.error("error：{}", e.getMessage());
            return null;
        }
    }

    /**
     * 生成 token
     *
     * @param username 用户名
     * @param secret   用户的密码
     * @return token
     */
    public static String sign(String username, String secret) {
        try {
            username = StringUtils.lowerCase(username);
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withClaim("username", username)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception e) {
            log.error("error：{}", e);
            return null;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Algorithm algorithm = Algorithm.HMAC256("sss");
        String sign = JWT.create()
                .withClaim("username", "jhy")
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000L))
                .sign(algorithm);
        System.out.println(sign);
        Thread.sleep(2000);
        String decoded = JWT.decode(sign).getClaim("username").asString();
        System.out.println(decoded);
        JWT.require(algorithm).withClaim("username", "jhy").build().verify(sign);
    }
}
