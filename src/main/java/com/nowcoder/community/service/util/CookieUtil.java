package com.nowcoder.community.service.util;

import com.nowcoder.community.entity.LoginTicket;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author zmm
 * @create 2023--07--13 22:14
 */
@Component
public class CookieUtil {

    public static String getTicket(HttpServletRequest request, String name) {
        if(request == null || name == null) {
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            if(cookie.getName().equals(name)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
