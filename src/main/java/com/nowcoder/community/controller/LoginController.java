package com.nowcoder.community.controller;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @author zmm
 * @create 2023--05--10 21:24
 */

@Controller
public class LoginController implements CommunityConstUtil {
    @Autowired
    private UserService userService;

    /**
     * 访问注册页面
     */
    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String getRegisterPage() {
        return "/site/register";
    }

    /**
     * 处理提交的注册表单
     */
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(Model model, User user) {
        Map<String, Object> map = userService.register(user);
        if(map == null || map.isEmpty()) {
            model.addAttribute("msg", "注册成功，我们已向您的邮箱发送了一封激活邮件，请尽快激活。");
            model.addAttribute("target", "/index");
            return "/site/operate-result";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            return "/site/register";
        }
    }

    @RequestMapping(path = "/activation/{userId}/{activationCode}")
    public String activate(Model model,@PathVariable("userId") int userId, @PathVariable("activationCode")String activationCode) {
        int code = userService.activation(userId, activationCode);

        if(code == ACTIVATION_SUCCESS) {
            model.addAttribute("msg", "您的账号已经激活成功,可以正常使用了!");
            model.addAttribute("target", "/login");
        } else if(code == ACTIVATION_REPEAT) {
            model.addAttribute("msg", "重复激活");
            model.addAttribute("target", "/login");
        } else if(code == ACTIVATION_FAIL) {
            model.addAttribute("msg", "激活码无效，激活失败！");
            model.addAttribute("target", "/index");
        }
        return "/site/operate-result";
    }

    @RequestMapping(path = "login", method = RequestMethod.GET)
    public String login() {
        return "/site/login";
    }
}