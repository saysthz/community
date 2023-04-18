package com.newcode.community.dao.impl;

import com.newcode.community.dao.AlphaDao;
import org.springframework.stereotype.Repository;

/**
 * @author zmm
 * @create 2023--04--04 15:42
 */

@Repository
public class AlphaDaoHibernateImpl implements AlphaDao {
    @Override
    public String select() {
        return "Hibernate";
    }
}
