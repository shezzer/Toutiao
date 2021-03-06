package com.nowcoder.toutiao.dao;

import com.nowcoder.toutiao.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * Created by Sherl on 2017/7/15.
 */

@Mapper
@Repository
public interface UserDAO {
    String TABLE_NAME = "user";
    String INSERT_FIELDS = " name, password, salt, headUrl ";
    String SELECT_FIELDS = " id, name, password, salt, headUrl";
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{name},#{password},#{salt},#{headUrl})"})
    int addUser(User user);

    @Select({"select ", SELECT_FIELDS, " from", TABLE_NAME, " where id=#{id}"})
    User findById(int id);

    @Select({"select ", SELECT_FIELDS, " from", TABLE_NAME, " where name=#{name}"})
    User findByName(String name);

    @Update({"update ", TABLE_NAME, " set password=#{password} where id=#{id}"})
    void updatePassword(User user);

    @Delete({"delete from ", TABLE_NAME, " where id=#{id}"})
    void deleteById(int id);
}
