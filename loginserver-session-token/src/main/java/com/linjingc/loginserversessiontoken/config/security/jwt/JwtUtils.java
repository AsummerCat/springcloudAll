package com.linjingc.loginserversessiontoken.config.security.jwt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.linjingc.loginserversessiontoken.entity.BasicUser;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Data
public class JwtUtils {
    private Algorithm algorithm;
    @Value("${basic.jwt.secret}")
    private String secret;
    @Value("${basic.jwt.tokenPrefix}")
    public String tokenPrefix;
    @Value("${basic.jwt.tokenHeader}")
    public String tokenHeader;
    @Value("${basic.jwt.issuer}")
    public String issuer;
    /**
     * 有效时间
     */
    @Value("${basic.jwt.expire}")
    private long expire;


    /**
     * 创建JWT签名
     *
     * @param id
     * @param subject  主题 可以是JSON数据 尽可能少
     * @param audience 签名接受者
     * @return
     */
    public String createJWT(String id, String subject, String audience) {
        algorithm = Algorithm.HMAC256(secret);
        Map<String, Object> map = new HashMap<String, Object>(4);
        map.put("alg", "HS256");
        map.put("typ", "JWT");

        Date nowDate = new Date();
        // 过期时间
        Date expireDate = new Date(nowDate.getTime() + expire * 1000);

        String token = JWT.create()
                //header
                .withHeader(map)
                /*设置 载荷 Payload*/
                .withClaim("org", "www.linjingc.top")
                //签名是有谁生成 例如 服务器
                .withIssuer(issuer)
                //签名的主题
                .withSubject(subject)
                //.withNotBefore(new Date())//该jwt都是不可用的时间
                //签名的观众 也可以理解谁接受签名的
                .withAudience(audience)
                //生成签名的时间
                .withIssuedAt(nowDate)
                //签名过期的时间
                .withExpiresAt(expireDate)
                /*签名 Signature */
                .sign(algorithm);
        return token;
    }


    /**
     * 检验token
     *
     * @param token
     * @return
     */
    public boolean verifyToken(String token, String issuer) {
        JWTVerifier verifier = JWT.require(algorithm).withIssuer(issuer).build();
        DecodedJWT jwt = verifier.verify(token);
        String subject = jwt.getSubject();
        List<String> audience = jwt.getAudience();
        String payload = jwt.getPayload();
        System.out.println("测试" + payload);

        Map<String, Claim> claims = jwt.getClaims();
        System.out.println(subject);
        System.out.println(audience.toString());
        System.out.println(claims.get("age").asString());
        System.out.println(claims.get("loginName").asString());
        System.out.println(claims.get("org").asString());

        return false;
    }


    /**
     * 测试方法
     *
     * @param arr
     */
    public static void main(String[] arr) {
        String jwt = new JwtUtils().createJWT("no.1", "这是个json消息", "夏天的狗");
    }

    /**
     * 从token中获取数据
     *
     * @param token
     * @return
     */
    public UserDetails getUser(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm).withIssuer(issuer).build();
        DecodedJWT jwt = verifier.verify(token);

        JSONObject userJson = JSONObject.parseObject(jwt.getSubject());
        BasicUser subject = JSON.toJavaObject(userJson, BasicUser.class);
        return subject;
    }

}
