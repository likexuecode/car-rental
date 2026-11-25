package com.cr.module2.mapper;

import com.cr.module2.entity.Vehicle;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 模块2：车辆数据访问
 */
@Mapper
public interface VehicleMapper {

    @Select("<script>"
            + "select v.*, t.type_name as typeName from vehicle v "
            + "left join vehicle_type t on v.type_id = t.id "
            + "<where>"
            + "  <if test='typeId != null'> and v.type_id = #{typeId} </if>"
            + "  <if test='keyword != null and keyword != \"\"'>"
            + "    and (v.vehicle_name like concat('%', #{keyword}, '%') "
            + "         or v.vehicle_no like concat('%', #{keyword}, '%') "
            + "         or v.brand like concat('%', #{keyword}, '%'))"
            + "  </if>"
            + "  <if test='onlyAvailable'> and v.stock &gt; 0 </if>"
            + "</where>"
            + "order by v.id desc"
            + "</script>")
    List<Vehicle> search(@Param("typeId") Long typeId,
                         @Param("keyword") String keyword,
                         @Param("onlyAvailable") boolean onlyAvailable);

    @Select("select v.*, t.type_name as typeName from vehicle v "
            + "left join vehicle_type t on v.type_id = t.id where v.id = #{id}")
    Vehicle findById(Long id);

    @Select("select * from vehicle where vehicle_no = #{vehicleNo}")
    Vehicle findByNo(String vehicleNo);

    @Insert("insert into vehicle(vehicle_no, vehicle_name, type_id, plate_no, seats, stock, daily_price, "
            + "status, image, brand, phone, address, description, create_time) values("
            + "#{vehicleNo}, #{vehicleName}, #{typeId}, #{plateNo}, #{seats}, #{stock}, #{dailyPrice}, "
            + "#{status}, #{image}, #{brand}, #{phone}, #{address}, #{description}, now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Vehicle vehicle);

    @Update("update vehicle set vehicle_name = #{vehicleName}, type_id = #{typeId}, plate_no = #{plateNo}, "
            + "seats = #{seats}, stock = #{stock}, daily_price = #{dailyPrice}, status = #{status}, "
            + "image = #{image}, brand = #{brand}, phone = #{phone}, address = #{address}, "
            + "description = #{description} where id = #{id}")
    int update(Vehicle vehicle);

    @Delete("delete from vehicle where id = #{id}")
    int delete(Long id);

    /** 扣减库存，库存不足时影响行数为 0（保证并发安全） */
    @Update("update vehicle set stock = stock - 1 where id = #{id} and stock > 0")
    int decreaseStock(Long id);

    @Update("update vehicle set stock = stock + 1 where id = #{id}")
    int increaseStock(Long id);

    @Select("select count(*) from orders where vehicle_id = #{vehicleId} "
            + "and status in ('待支付', '已取车', '归还审核中')")
    int countUnfinishedOrders(Long vehicleId);
}
