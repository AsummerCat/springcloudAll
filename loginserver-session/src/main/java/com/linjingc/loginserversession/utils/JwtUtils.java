package com.linjingc.loginserver.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Data
public class JwtUtils {
    private Algorithm algorithm;
    @Value("${basic.JWT.secret}")
    private String secret;
    @Value("${basic.JWT.tokenPrefix}")
    public String TOKEN_PREFIX;
    @Value("${basic.JWT.tokenHeader}")
    public String TOKEN_HEADER;
    @Value("${basic.JWT.issuer}")
    public String ISSUER;


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
        Date expireDate = getAfterDate(nowDate, 0, 0, 0, 2, 0, 0);

        String token = JWT.create()
                //header
                .withHeader(map)
                /*设置 载荷 Payload*/
                .withClaim("loginName", "cat")
                .withClaim("age", "18")
                .withClaim("org", "www.linjingc.top")
                //签名是有谁生成 例如 服务器
                .withIssuer(ISSUER)
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
     * 返回一定时间后的日期
     *
     * @param date   开始计时的时间
     * @param year   增加的年
     * @param month  增加的月
     * @param day    增加的日
     * @param hour   增加的小时
     * @param minute 增加的分钟
     * @param second 增加的秒
     * @return         
     */
    private static Date getAfterDate(Date date, int year, int month, int day, int hour, int minute, int second) {
        if (date == null) {
            date = new Date();
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        if (year != 0) {
            cal.add(Calendar.YEAR, year);
        }
        if (month != 0) {
            cal.add(Calendar.MONTH, month);
        }
        if (day != 0) {
            cal.add(Calendar.DATE, day);
        }
        if (hour != 0) {
            cal.add(Calendar.HOUR_OF_DAY, hour);
        }
        if (minute != 0) {
            cal.add(Calendar.MINUTE, minute);
        }
        if (second != 0) {
            cal.add(Calendar.SECOND, second);
        }
        return cal.getTime();
    }


    /**
     * 测试方法
     *
     * @param arr
     */
    public static void main(String[] arr) {
        String jwt = new JwtUtils().createJWT("no.1", "这是个json消息", "夏天的狗");
        new JwtUtils().verifyToken(jwt, "夏天的猫");
    }

    /**
     * 从token中获取数据
     *
     * @param token
     * @return
     */
    public String getUsername(String token) {
        algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
        DecodedJWT jwt = verifier.verify(token);
        String subject = jwt.getSubject();
        List<String> audience = jwt.getAudience();
        String payload = jwt.getPayload();
        return subject;
    }

}
