package com.nowcoder.toutiao.dao;

import com.nowcoder.toutiao.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by Sherl on 2017/7/23.
 */
@Mapper
public interface MessageDAO {
    String TABLE_NAME = "message";
    String INSERT_FIELDS = " fromId, toId, content, createdDate, hasRead, conversationId ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{fromId},#{toId},#{content},#{createdDate},#{hasRead},#{conversationId})"})
    int addMessage(Message msg);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where conversationId=#{conversationId} order by id desc limit #{offset},#{limit}"})
    List<Message> selectByConversationId(@Param("conversationId") String conversationId, @Param("offset") int offset, @Param("limit") int limit);

    @Select({"select ", INSERT_FIELDS, " ,count(id) as id from ( select * from", TABLE_NAME, " where fromId=#{userId} or toId=#{userId} order by id desc ) tt group by conversationId order by id desc limit #{offset},#{limit}"})
    List<Message> getConversationList(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit);

    @Select({"select count(id) from ", TABLE_NAME, "where toId=#{userId} and hasRead=0 and conversationId=#{conversationId}"})
    int getUnreadNum(@Param("userId") int userId,@Param("conversationId") String conversationId);
}
