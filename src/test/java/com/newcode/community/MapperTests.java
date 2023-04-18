package com.newcode.community;

import com.newcode.community.dao.DiscussPostMapper;
import com.newcode.community.dao.UserMapper;
import com.newcode.community.entity.DiscussPost;
import com.newcode.community.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

/**
 * @author zmm
 * @create 2023--04--13 15:11
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Test
    public void testSelectUser() {
        User user = userMapper.selectById(101);
        System.out.println(user);

        User user1 = userMapper.selectByName("liubei");
        System.out.println(user1);

        User user2 = userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user2);
    }

    @Test
    public void testInsertUser() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("123456");
        user.setEmail("111@qq.com");

        int i = userMapper.insertUser(user);
        System.out.println(i);
    }

    @Test
    public void testUpdateUser() {
        int i = userMapper.updatePassword(150, "111111");
        System.out.println(i);

        int i1 = userMapper.updateHeader(150, "1111");
        System.out.println(i1);

        int i2 = userMapper.updateStatus(150, 1);
        System.out.println(i2);
    }

    @Test
    public void testSelectDiscussPost() {
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(149, 0, 10);
        for(DiscussPost post: discussPosts) {
            System.out.println(post);
        }

        int rows = discussPostMapper.selectDiscussPostRows(0);
        System.out.println(rows);
    }
}
