package com.nowcoder.community.service.util;

import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * @author zmm
 * @create 2023--05--10 22:03
 */

public class CommunityUtil {

    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String md5(String key) {
        if(StringUtil.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }
}
