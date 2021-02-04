package com.juzipi.test;

import com.juzipi.demo.pojo.UserInfo;
import com.juzipi.demo.utils.JwtUtils;
import com.juzipi.demo.utils.RsaUtils;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

public class JwtTest {

    private static final String pubKeyPath = "D:\\SpringBootLearning\\tmp\\rsa.pub";

    private static final String priKeyPath = "D:\\SpringBootLearning\\tmp\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "234");
    }

    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(20L, "jack"), privateKey, 5);
        System.out.println("token = " + token);
    }

    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiamFjayIsImV4cCI6MTYxMjQyOTg3M30.lhf6tuXYRczccdyAEl9J2gMZHlxEWdmvJ1pXFEOH_kk3msvOI6MX9u73JKW_cdRTqQp2CtIKGUIbQILA6HXUfJwjuxEb7pGMJNH8JmNHiSupLZDDO_lMlrdk6gG4Zbt3HMSA5VdJlI6s6WPrQJgRM6NuHunFF-aaX45XAuNBu8w";

        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
}