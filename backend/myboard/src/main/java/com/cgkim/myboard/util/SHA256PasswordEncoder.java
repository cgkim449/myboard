package com.cgkim.myboard.util;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class SHA256PasswordEncoder {
    /**
     * 해싱
     */
    public String getHash(String message) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(message.getBytes());
        byte[] result = messageDigest.digest();
        return bytesToString(result);
    }

    /**
     * 바이트를 문자열로 변환
     */
    private String bytesToString(byte[] result) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : result) {
            stringBuilder.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return stringBuilder.toString();
    }
}
