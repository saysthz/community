package com.nowcoder.community.controller;

import com.google.code.kaptcha.Producer;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstUtil;
import com.nowcoder.community.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

/**
 * @author zmm
 * @create 2023--05--10 21:24
 */

@Controller
public class LoginController implements CommunityConstUtil {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private Producer kaptchaProducer;

    @Value("server.servlet.context-path")
    private String contextPath;

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

    @RequestMapping(path = "/kaptcha", method = RequestMethod.GET)
    public void kaptcha(HttpServletResponse response, HttpSession session) {
        // 生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        // 将验证码存入session
        session.setAttribute("kaptcha", text);

        response.setContentType("image/png");
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            ImageIO.write(image, "png", outputStream);
        } catch (IOException e) {
            logger.error("响应验证码失败" + e.getMessage());
        }
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(Model model, String username, String password, boolean rememberme, String code, HttpSession session, HttpServletResponse response) {
        String kapthca = (String) session.getAttribute("kaptcha");
        if(StringUtil.isBlank(code) || StringUtil.isBlank(kapthca) || !kapthca.equalsIgnoreCase(code)) {
            model.addAttribute("codeMsg", "验证码不正确");
            return "/site/login";
        }
        int expired = rememberme ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(username, password, expired);
        if(map.containsKey("ticket")) {  // 说明已经生成了登陆凭证，生成Cookie，保存登录的ticket
            String ticket = (String) map.get("ticket");
            Cookie cookie = new Cookie("ticket", ticket);
            cookie.setPath(contextPath);
            cookie.setMaxAge(expired);
            response.addCookie(cookie);
            return "redirect:/index";
        }else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "/site/login";
        }
    }



}
