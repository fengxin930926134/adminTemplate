package com.fengx.template.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * token工具类
 */
public class TokenUtils {

    public static final String JWT_LOGIN_USERNAME = "jwt_login_username";
    /**
     * 过期时间1天 单位秒
     */
    public static final int JWT_EXPIRATION_TIME = 60 * 60 * 24;
    public static final String HEADER_STRING = "Authorization";
    /**
     * 密钥随便乱打一通我自己都不知道是啥就完事了
     */
    private static final String JWT_BASE64SECURITY = "a1dwd12da4grg1sdw12faw5da";
    private static final String TOKEN_PREFIX = "bearer ";

    public static String createToken(String userId) {
        return createJwt(
                userId,
                "user",
                "admin",
                JWT_EXPIRATION_TIME,
                JWT_BASE64SECURITY
        );
    }

    /**
     * 解析token
     * @param jwtToken jwtToken
     * @return Claims
     */
    public static Claims parseToken(String jwtToken) {
        try {
            return Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(JWT_BASE64SECURITY))
                    .parseClaimsJws(jwtToken.replace(TOKEN_PREFIX, "")).getBody();
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 创建JWT
     *
     * @param username       username
     * @param audience       jwtid
     * @param issuer         用于说明该JWT是由谁签发的
     * @param ttlMillis      过期时间
     * @param base64Security jwtbase64
     * @return jwt token
     */
    public static String createJwt(String username,
                                   String audience,
                                   String issuer,
                                   long ttlMillis,
                                   String base64Security) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        //生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64Security);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                .claim(JWT_LOGIN_USERNAME, username)
                .setIssuer(issuer)
                .setAudience(audience)
                .signWith(signatureAlgorithm, signingKey);
        //添加Token过期时间
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + (ttlMillis * 1000);
            Date exp = new Date(expMillis);
            builder.setExpiration(exp).setNotBefore(now);
        }
        //生成JWT
        return TOKEN_PREFIX.concat(builder.compact());
    }

    /**
     * 生成用户权限列表
     *
     * @return userRoles
     */
    public static Collection<GrantedAuthority> getAuthorities(List<String> roleIds) {
        if (roleIds == null) {
            roleIds = new ArrayList<>();
        }
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        // 用户角色 注意：必须"ROLE_"开头
        for(String roleId : roleIds) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + roleId));
        }
        return grantedAuthorities;
    }

}
