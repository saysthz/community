package com.nowcoder.community.service;

import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityConstUtil;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.MailClient;
import com.nowcoder.community.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author zmm
 * @create 2023--04--18 10:36
 */
@Service
public class UserService implements CommunityConstUtil {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private MailClient mailClient;

    @Resource
    private TemplateEngine templateEngine;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;


    public User findUserById(int id) {
        return userMapper.selectById(id);
    }

    /**
     * 注册用户，并发送激活邮件
     * @param user
     * @return
     */
    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if(user == null) {
            throw new IllegalArgumentException("参数不能为空！");
        }
        if(StringUtil.isBlank(user.getUsername())) {
            map.put("usernameMsg", "用户名不能为空！");
            return map;
        }
        if(StringUtil.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空！");
            return map;
        }
        if(StringUtil.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空！");
            return map;
        }

        // 验证账号
        if(userMapper.selectByName(user.getUsername()) != null) {
            map.put("usernameMsg", "用户已存在！");
            return map;
        }

        // 验证邮箱
        if(userMapper.selectByEmail(user.getEmail()) != null) {
            map.put("emailMsg", "该邮箱已被占用！");
            return map;
        }

        // 可以注册，将用户信息保存在数据库中
        user.setSalt(CommunityUtil.generateUUID().substring(0,4));
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setStatus(0); //未激活
        user.setType(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setCreateTime(new Date());
        user.setHeaderUrl("http://images.nowcoder.com/head/" + new Random().nextInt(1000) + "t.png");
        userMapper.insertUser(user);

        // 发送激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        String url = domain + contextPath + "/activation" + "/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String process = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(), "激活账号", process);
        return map;
    }

    public int activation(int userId, String activationCode) {
        User user = userMapper.selectById(userId);
        if(user == null) {
            return ACTIVATION_FAIL;
        }
        if(user.getStatus() == 1) {
            return ACTIVATION_REPEAT;
        }else {
            if(user.getActivationCode().equals(activationCode)) {
                userMapper.updateStatus(userId, 1);
                return ACTIVATION_SUCCESS;
            }else {
                return ACTIVATION_FAIL;
            }
        }
    }

    public Map<String, Object> login(String username, String password, int expiredSeconds) {
        Map<String, Object> map = new HashMap<>();
        if(StringUtil.isBlank(username)) {
            map.put("usernameMsg", "用户名不能为空");
            return map;
        }
        if(StringUtil.isBlank(password)) {
            map.put("passwordMag", "密码不能为空");
            return map;
        }
        User user = userMapper.selectByName(username);
        if(user == null) {
            map.put("usernameMsg", "用户不存在");
            return map;
        }
        if(!CommunityUtil.md5(password + user.getSalt()).equals(user.getPassword())) {
            map.put("passwordMsg", "密码不正确");
            return map;
        }

        // 用户信息验证正确，生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));
        loginTicketMapper.insertLoginTicket(loginTicket);

        map.put("ticket", loginTicket.getTicket());

        return map;
    }

    public void logout(String ticket) {
        loginTicketMapper.updateStatus(ticket, 1);
    }

    public LoginTicket findLoginTicket(String ticket) {
        return loginTicketMapper.selectByTicket(ticket);
    }


}
