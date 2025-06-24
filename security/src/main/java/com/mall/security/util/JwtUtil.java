package com.mall.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    private static final String USER_ID_KEY = "sub";
    private static final String CREATED_KEY = "created";
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;
    @Value("${jwt.tokenHead}")
    private String tokenHead;


    private String generateToken(Map<String,Object> keys){
        return Jwts.builder()
                .setClaims(keys)
                .setExpiration(new Date(System.currentTimeMillis()+expiration*1000L))
                .signWith(SignatureAlgorithm.ES256,secret)
                .compact();
    }

    /**
     * 获取负载体
     * @param token
     * @return
     */
    private Claims getClaimsFromToken(String token){
        try{
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            logger.info("jwt验证失败 : {}",e.getMessage());
            return null;
        }
    }

    /**
     * 检验token有效性并且获取userId
     * @param token
     * @return
     */
    public Object getUserIdFromToken(String token){
        Claims claims = getClaimsFromToken(token);
        if(claims!=null){
            try {
                Date date = claims.getExpiration();
                if (date.before(new Date())){
                    return null;
                }
                return claims.get(USER_ID_KEY);
            }catch (Exception e){
                logger.info("解析 userId失败 : {}",e.getMessage());
            }
        }
        return null;
    }

    /**
     * 检验token有效性
     * @param token
     * @param userDetails
     * @return
     */
    public boolean validateToken(String token, UserDetails userDetails){
        Object userId = getUserIdFromToken(token);
        return userId!=null && userId.toString().equals(userDetails.getUsername());
    }


    /**
     * 把userId传入userName
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails){
        Map<String,Object> map = new HashMap<>();
        map.put(USER_ID_KEY,userDetails.getUsername());
        map.put(CREATED_KEY,System.currentTimeMillis());
        return this.generateToken(map);
    }


    /**
     * 刷新token
     * @param oldToken
     * @return
     */
    public String refreshHeaderToken(String oldToken){
        if (oldToken==null || oldToken.isEmpty() || !oldToken.contains(tokenHead)){
            return null;
        }
        String token = oldToken.substring(tokenHead.length());
        if(token.isEmpty()){
            return null;
        }
        Claims claims = getClaimsFromToken(token);
        if(claims==null) {
            return null;
        }
        //token过期不支持刷新
        if (claims.getExpiration().before(new Date())){
            return null;
        }
        Long created = claims.get(CREATED_KEY,Long.class);
        if (created==null || System.currentTimeMillis() - created >= 30*60*1000){
            return token;
        }
        claims.put(CREATED_KEY,System.currentTimeMillis());
        return generateToken(claims);
    }
}
