package com.newcode.community.service;

import com.newcode.community.dao.UserMapper;
import com.newcode.community.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zmm
 * @create 2023--04--18 10:36
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User findUserById(int id) {
        return userMapper.selectById(id);
    }

}
