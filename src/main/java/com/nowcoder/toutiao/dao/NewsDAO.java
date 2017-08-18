package com.nowcoder.toutiao.dao;

import com.nowcoder.toutiao.model.News;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Sherl on 2017/7/16.
 */
@Mapper
public interface NewsDAO {
    String TABLE_NAME = "news";
    String INSERT_FIELDS = " title, link, image, likeCount, commentCount, createdDate, userId ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;
    List<News> findNewsList(@Param("userId") int userId, @Param("offset") int offset,
                            @Param("limit") int limit);
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{title},#{link},#{image},#{likeCount},#{commentCount},#{createdDate},#{userId})"})
    int addNews(News news);

    @Select({"select ", SELECT_FIELDS , " from ", TABLE_NAME, " where id=#{id}"})
    News getById(int id);

    @Update({"update ", TABLE_NAME, " set commentCount = #{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);

    @Update({"update ", TABLE_NAME, " set likeCount = #{likeCount} where id=#{id}"})
    int updatelikeCount(@Param("id") int id, @Param("likeCount") int likeCount);
}
