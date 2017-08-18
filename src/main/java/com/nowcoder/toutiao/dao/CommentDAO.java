package com.nowcoder.toutiao.dao;

import com.nowcoder.toutiao.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Sherl on 2017/7/21.
 */
@Mapper
public interface CommentDAO {
    String TABLE_NAME = "comment";
    String INSERT_FIELDS = " userId, entityId, entityType, status, content, createdDate ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{userId},#{entityId},#{entityType},#{status},#{content},#{createdDate})"})
    int addComment(Comment comment);

    @Select({"select count(id) from ", TABLE_NAME, " where entityId=#{entityId} and entityType=#{entityType}"})
    int getCommentCount(@Param("entityId") int entityId,@Param("entityType") int entityType);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where entityType=#{entityType} and entityId=#{entityId} order by id desc "})
    List<Comment> selectByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Update({"update ", TABLE_NAME, " set status = #{status} where id=#{id}"})
    int updateCommentCount(@Param("id") int id, @Param("status") int status);
}
