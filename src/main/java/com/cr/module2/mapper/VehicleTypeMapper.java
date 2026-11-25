package com.cr.module2.mapper;

import com.cr.module2.entity.VehicleType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 模块2：汽车类型数据访问
 */
@Mapper
public interface VehicleTypeMapper {

    @Select("select * from vehicle_type order by id")
    List<VehicleType> findAll();

    @Select("select * from vehicle_type where type_name = #{typeName}")
    VehicleType findByName(String typeName);

    @Insert("insert into vehicle_type(type_name) values(#{typeName})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(VehicleType type);
}
