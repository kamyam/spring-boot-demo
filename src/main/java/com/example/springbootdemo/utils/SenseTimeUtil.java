package com.example.springbootdemo.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

/**
 * 接口文档: https://v2-devcenter.visioncloudapi.com/#!/home/doc/index
 * Created by wuganqin on 2018/11/8.
 */
public class SenseTimeUtil {
    private static final Logger logger = LoggerFactory.getLogger(SenseTimeUtil.class);

    private static final String HASH_ALGORITHM = "HmacSHA256";


    private static String genOriString(String apiKey, String timestamp, String nonce) {
        List<String> beforeSort = new ArrayList<>();
        beforeSort.add(apiKey);
        beforeSort.add(timestamp);
        beforeSort.add(nonce);
        beforeSort.sort((a, b) -> {
            try {
                String s1 = new String(a.getBytes("GB2312"), "ISO-8859-1");
                String s2 = new String(b.getBytes("GB2312"), "ISO-8859-1");
                return s1.compareTo(s2);
            } catch (Exception e) {
                return 0;
            }
        });
        return String.join("", beforeSort);
    }

    private static String genEncryptString(String genOriString, String apiSecret) {
        try {
            Key sk = new SecretKeySpec(apiSecret.getBytes(), HASH_ALGORITHM);
            Mac mac = Mac.getInstance(sk.getAlgorithm());
            mac.init(sk);
            final byte[] hmac = mac.doFinal(genOriString.getBytes());
            return Hex.encodeHexString(hmac);
        } catch (Exception e) {
            logger.error("HmacSHA256加密失败:{}", e);
        }
        return "";
    }

    /**
     * 根据API_KEY和API_SECRET生成的签名
     */
    public static String genHeaderParam(String apiKey, String apiSecret) {

        //用户自己生成 timestamp（Unix 时间戳）；
        String timestamp = Long.toString(System.currentTimeMillis());
        //生成随机数nonce(注：最好是32位的)
        String nonce = RandomStringUtils.randomAlphanumeric(16);
        //将timestamp、nonce、API_KEY 这三个字符串依据字符串首位字符的ASCII码进行升序排列，并join成一个字符串；
        String genOriString = genOriString(apiKey, timestamp, nonce);
        //然后用API_SECRET对这个字符串做hamc-sha256 签名，以16进制编码；
        String encryptedString = genEncryptString(genOriString, apiSecret);

        return "key=".concat(apiKey)
                .concat(",timestamp=").concat(timestamp)
                .concat(",nonce=").concat(nonce)
                .concat(",signature=").concat(encryptedString);
    }


}
