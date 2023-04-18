package com.newcode.community.dao.impl;

import com.newcode.community.dao.AlphaDao;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

/**
 * @author zmm
 * @create 2023--04--04 15:50
 */

@Repository
@Primary // 设置优先级，当AlphaDao接口的不同实现类注入时，优先该实现类
public class AlphaDaoMybatisImpl implements AlphaDao {
    @Override
    public String select() {
        return "Mybatis";
    }
}
