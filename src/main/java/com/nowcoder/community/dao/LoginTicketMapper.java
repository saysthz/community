package com.nowcoder.community.dao;

import com.nowcoder.community.entity.LoginTicket;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zmm
 * @create 2023--06--06 17:41
 */
@Mapper
public interface LoginTicketMapper {

    /**
     * 插入新的登陆凭证
     * @param loginTicket
     * @return
     */
    int insertLoginTicket(LoginTicket loginTicket);

    /**
     * 根据ticket查询登录凭证信息
     * @param ticket
     * @return
     */
    LoginTicket selectByTicket(String ticket);

    /**
     * 更新登陆凭证的状态
     * @param ticket
     * @param status
     * @return
     */
    int updateStatus(String ticket, int status);

}
