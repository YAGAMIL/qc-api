//package com.quantumtime.qc.common.jwt;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import lombok.Data;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import java.io.Serializable;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//
//@Data
//@ConfigurationProperties(prefix = "jwt")
//@Component
//public class JwtTokenUtil implements Serializable {
//
//    private static final String CLAIM_KEY_USER_ACCOUNT = "sub";
//
//    private static final String CLAIM_KEY_CREATED = "created";
//
//    private String secret; //秘钥
//
//    private Long expiration; //过期时间
//
//    private String tokenHeader;
//
//    private String wechatTokenHead;
//
//    private String tokenHead;
//
//    /**
//     * 从数据声明生成令牌
//     *
//     * @param claims 数据声明
//     * @return 令牌
//     */
//    private String generateToken(Map<String, Object> claims) {
////        Date expirationDate = new Date(System.currentTimeMillis() + expiration);
//        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
//    }
//
//    /**
//     * 从令牌中获取数据声明
//     *
//     * @param token 令牌
//     * @return 数据声明
//     */
//    public Claims getClaimsFromToken(String token) {
//        Claims claims;
//        try {
//            claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
//        } catch (Exception e) {
//            claims = null;
//        }
//        return claims;
//    }
//
//    /**
//     * 生成令牌
//     *
//     * @param userDetails 用户
//     * @return 令牌
//     */
//    public String generateToken(JwtUser userDetails) {
//        Map<String, Object> claims = new HashMap<>(2);
//        claims.put("sub", userDetails.getPhone());
//        claims.put("created", new Date());
//        return generateToken(claims);
//    }
//
//    /**
//     * 从令牌中获取用户名
//     *
//     * @param token 令牌
//     * @return 用户名
//     */
//    public String getUsernameFromToken(String token) {
//        String username;
//        try {
//            Claims claims = getClaimsFromToken(token);
//            username = claims.getSubject();
//        } catch (Exception e) {
//            username = null;
//        }
//        return username;
//    }
//
//    /**
//     * 判断令牌是否过期
//     *
//     * @param token 令牌
//     * @return 是否过期
//     */
//    public Boolean isTokenExpired(String token) {
//        try {
//            Claims claims = getClaimsFromToken(token);
//            Date expiration = claims.getExpiration();
//            return expiration.before(new Date());
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    /**
//     * 刷新令牌
//     *
//     * @param token 原令牌
//     * @return 新令牌
//     */
//    public String refreshToken(String token) {
//        String refreshedToken;
//        try {
//            Claims claims = getClaimsFromToken(token);
//            claims.put("created", new Date());
//            refreshedToken = generateToken(claims);
//        } catch (Exception e) {
//            refreshedToken = null;
//        }
//        return refreshedToken;
//    }
//
//    /**
//     * 验证令牌
//     *
//     * @param token       令牌
//     * @param userDetails 用户
//     * @return 是否有效
//     */
//    public Boolean validateToken(String token, UserDetails userDetails) {
//        JwtUser user = (JwtUser) userDetails;
//        String username = getUsernameFromToken(token);
//        return (username.equals(user.getPhone()));
//    }
//
//}