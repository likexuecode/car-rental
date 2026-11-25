package com.cr.module3.mapper;

import com.cr.module3.entity.Order;
import com.cr.module3.entity.OrderVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 模块3：订单数据访问
 */
@Mapper
public interface OrderMapper {

    String VO_COLUMNS = "o.id, o.order_no, o.user_id, o.vehicle_id, o.rent_days, o.amount, o.start_time, "
            + "o.status, o.is_paid, o.create_time, v.vehicle_name, v.plate_no, "
            + "v.image as vehicleImage, v.daily_price, v.brand ";

    @Insert("insert into orders(order_no, user_id, vehicle_id, rent_days, amount, start_time, status, is_paid, create_time) "
            + "values(#{orderNo}, #{userId}, #{vehicleId}, #{rentDays}, #{amount}, #{startTime}, #{status}, #{isPaid}, now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Order order);

    @Select("select * from orders where id = #{id}")
    Order findById(Long id);

    @Select("select " + VO_COLUMNS + "from orders o join vehicle v on o.vehicle_id = v.id where o.id = #{id}")
    OrderVO findVOById(Long id);

    @Select("<script>"
            + "select " + VO_COLUMNS + "from orders o join vehicle v on o.vehicle_id = v.id "
            + "where o.user_id = #{userId} "
            + "<if test='status != null and status != \"\"'> and o.status = #{status} </if>"
            + "order by o.id desc"
            + "</script>")
    List<OrderVO> listByUser(@Param("userId") Long userId, @Param("status") String status);

    @Select("<script>"
            + "select " + VO_COLUMNS + "from orders o join vehicle v on o.vehicle_id = v.id "
            + "<where>"
            + "<if test='status != null and status != \"\"'> o.status = #{status} </if>"
            + "</where>"
            + "order by o.id desc"
            + "</script>")
    List<OrderVO> listAll(@Param("status") String status);

    @Select("select count(*) from orders where user_id = #{userId} and vehicle_id = #{vehicleId} "
            + "and status in ('待支付', '已取车', '归还审核中')")
    int countUnfinished(@Param("userId") Long userId, @Param("vehicleId") Long vehicleId);

    @Update("update orders set status = #{status} where id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    @Update("update orders set is_paid = '已支付' where id = #{id}")
    int markPaid(Long id);

    /** 续租审核通过后累加租期与金额 */
    @Update("update orders set rent_days = rent_days + #{days}, amount = amount + #{amount} where id = #{id}")
    int addRent(@Param("id") Long id, @Param("days") Integer days, @Param("amount") Float amount);

    @Select("select count(*) from orders where user_id = #{userId} and status in ('待支付', '已取车', '归还审核中')")
    int countUserUnfinished(Long userId);
}
