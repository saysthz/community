package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zmm
 * @create 2023--04--13 11:15
 */
@Mapper
public interface DiscussPostMapper {

    /**
     * 用于查询所有的帖子，当首页查询时，不带userId的条件，当在个人主页查询我的所有帖子时，会带上个人的userId
     * 在sql语句中采用<if>动态拼接是否需要userId
     * @param userId
     * @param offset 用于分页查询，每一页起始行的行号
     * @param limit 用于分页查询，每页显示的记录数
     * @return
     */
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    /**
     * 查询一共有多少条帖子
     * @param userId 如果sql中需要动态拼接一个条件，并且这个方法有且只有这一个参数，则需要加上@Param注解给参数取别名
     * @return
     */
    int selectDiscussPostRows(@Param("userId")int userId);
}
