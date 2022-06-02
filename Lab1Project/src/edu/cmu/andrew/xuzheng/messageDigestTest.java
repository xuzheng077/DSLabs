package edu.cmu.andrew.xuzheng;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Xu Zheng
 * @description
 */
public class messageDigestTest {
    public String messageDigestTest(String message) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(message.getBytes(StandardCharsets.UTF_8));
            byte[] result = messageDigest.digest();
            return byteToHex(result);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Cited from https://juejin.cn/post/6844903924105756686
     *
     * @param bytes
     * @return
     */
    private static String byteToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        String temp;
        for (byte b : bytes) {
            temp = Integer.toHexString(b & 0xFF);
            if (temp.length() == 1)
                builder.append(0);
            builder.append(temp);
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        messageDigestTest obj = new messageDigestTest();
        String result = obj.messageDigestTest("Hello World");
        System.out.println(result);
    }

}
