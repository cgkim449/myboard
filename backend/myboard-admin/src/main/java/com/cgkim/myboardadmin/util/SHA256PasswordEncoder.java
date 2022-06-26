package com.cgkim.myboardadmin.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 비밀번호를 SHA256으로 암호화하는 역할
 */
@Component
public class SHA256PasswordEncoder {

    private final String algorithm;

    /**
     * 암호화 알고리즘 설정 주입
     *
     * @param algorithm
     */
    public SHA256PasswordEncoder(@Value("${password.encoder-algorithm}") String algorithm) {

        this.algorithm = algorithm;
    }

    /**
     * 해싱
     *
     * @param message
     * @return String
     * @throws NoSuchAlgorithmException
     */
    public String getHash(String message) throws NoSuchAlgorithmException {

        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        messageDigest.update(message.getBytes());
        byte[] result = messageDigest.digest();

        return bytesToString(result);
    }

    /**
     * 바이트를 문자열로 변환
     *
     * @param result
     * @return String
     */
    private String bytesToString(byte[] result) {

        StringBuilder stringBuilder = new StringBuilder();

        for (byte b : result) {
            stringBuilder.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }

        return stringBuilder.toString();
    }
}
