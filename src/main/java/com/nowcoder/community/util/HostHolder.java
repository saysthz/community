package com.nowcoder.community.util;

import com.nowcoder.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * @author zmm
 * @create 2023--07--13 22:24
 */

/**
 * 持有用户信息，用于代替session对象
 */
@Component
public class HostHolder {
    private  ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }

}
