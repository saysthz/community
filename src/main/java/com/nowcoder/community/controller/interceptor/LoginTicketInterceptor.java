package com.nowcoder.community.controller.interceptor;

import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.service.util.CookieUtil;
import com.nowcoder.community.service.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author zmm
 * @create 2023--07--13 22:13
 */
@Component
public class LoginTicketInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    /**
     * 拦截除了静态资源之外的请求，如果已经有有效的登陆凭证，则将用户信息存入hostHolder
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = CookieUtil.getTicket(request, "ticket");
        if(ticket != null) {
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            if(loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())) {
                User user = userService.findUserById(loginTicket.getUserId());
                hostHolder.setUser(user);
            }
        }
        return true;
    }

    /**
     * 在渲染模板引擎之前将用户信息存入ModelAndView中，供模板使用
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if(user != null) {
            modelAndView.addObject("loginUser", user);
        }
    }

    /**
     * 在模板引擎渲染完毕后，将ThreadLocal中的数据清除，避免ThreadLocal的内存泄漏问题
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
