package com.newcode.community.controller;

import com.newcode.community.entity.DiscussPost;
import com.newcode.community.entity.Page;
import com.newcode.community.entity.User;
import com.newcode.community.service.DiscussPostService;
import com.newcode.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zmm
 * @create 2023--04--18 10:47
 */
@Controller
public class HomeController {
    @Autowired
    private UserService userService;

    @Autowired
    private DiscussPostService discussPostService;

    /**
     *
     * @param model 通过Model携带数据给模板
     * @return 返回String类型即返回视图的路径（名称）
     */
    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page) {
        // 方法调用前，SpringMVC会自动实例化Model和Page，并将Page注入到Model
        // 所以，在thymeleaf中可以直接访问Page对象中的数据
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index"); //page的当前页码和每页显示的总条数由页面传过来，不是服务器端设置的

        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if(list != null) {
            for(DiscussPost post: list) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", post);
                User user = userService.findUserById(post.getUserId());
                map.put("user", user);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);
        return "/index";
    }

}
