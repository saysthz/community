package com.nowcoder.community.util;

/**
 * @author zmm
 * @create 2023--05--10 22:08
 */

public interface CommunityConstUtil {
    // 激活成功
    int ACTIVATION_SUCCESS = 0;

    // 重复激活
    int ACTIVATION_REPEAT = 1;

    //激活失败
    int ACTIVATION_FAIL = 2;

    // 默认过期时长，12小时
    int DEFAULT_EXPIRED_SECONDS = 60 * 60 * 12;

    // 记住密码的过期时长，100天
    int REMEMBER_EXPIRED_SECONDS = 60 * 60 * 24 * 3;




}
