package com.nowcoder.community.service.util;


/**
 * @author zmm
 * @create 2023--05--10 21:52
 */
public class StringUtil {
    public static boolean isBlank(String s) {
        if(s == null || s.length() == 0 || s.trim().length() == 0) {
            return true;
        }
        return false;
    }
}
