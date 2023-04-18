package com.newcode.community.dao;

import com.newcode.community.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zmm
 * @create 2023--04--13 12:05
 */
@Mapper
public interface UserMapper {
    User selectById(int userId);

    User selectByName(String username);

    User selectByEmail(String email);

    int insertUser(User user);

    int updateStatus(int id, int status);

    int updateHeader(int id, String headerUrl);

    int updatePassword(int id, String password);
}
