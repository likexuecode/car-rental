package com.cr.module1.mapper;

import com.cr.module1.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 模块1：用户数据访问
 */
@Mapper
public interface UserMapper {

    @Select("select * from `user` where account = #{account}")
    User findByAccount(String account);

    @Select("select * from `user` where id = #{id}")
    User findById(Long id);

    @Insert("insert into `user`(account, password, name, phone, id_card, state, role, create_time) "
            + "values(#{account}, #{password}, #{name}, #{phone}, #{idCard}, 1, #{role}, now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Update("update `user` set name = #{name}, phone = #{phone}, id_card = #{idCard} where id = #{id}")
    int updateProfile(User user);

    @Update("update `user` set password = #{password} where id = #{id}")
    int updatePassword(@Param("id") Long id, @Param("password") String password);
}
