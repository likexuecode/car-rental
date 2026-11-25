package com.cr.module4.mapper;

import com.cr.module4.entity.ReturnRecord;
import com.cr.module4.entity.ReturnVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 模块4：归还记录数据访问
 */
@Mapper
public interface ReturnRecordMapper {

    String VO_COLUMNS = "r.id, r.order_id, r.return_time, r.is_damaged, r.is_fuel_exceed, r.is_violation, "
            + "r.audit_status, r.audit_reply, r.create_time, o.order_no, o.user_id, "
            + "u.name as userName, v.vehicle_name, v.plate_no ";

    @Insert("insert into return_record(order_id, return_time, is_damaged, is_fuel_exceed, is_violation, "
            + "audit_status, create_time) values(#{orderId}, #{returnTime}, #{isDamaged}, #{isFuelExceed}, "
            + "#{isViolation}, '待审核', now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ReturnRecord record);

    @Select("select * from return_record where id = #{id}")
    ReturnRecord findById(Long id);

    @Select("select * from return_record where order_id = #{orderId} limit 1")
    ReturnRecord findByOrderId(Long orderId);

    @Select("select " + VO_COLUMNS + "from return_record r "
            + "join orders o on r.order_id = o.id join vehicle v on o.vehicle_id = v.id "
            + "join `user` u on o.user_id = u.id where o.user_id = #{userId} order by r.id desc")
    List<ReturnVO> listByUser(Long userId);

    @Select("<script>"
            + "select " + VO_COLUMNS + "from return_record r "
            + "join orders o on r.order_id = o.id join vehicle v on o.vehicle_id = v.id "
            + "join `user` u on o.user_id = u.id "
            + "<if test='auditStatus != null and auditStatus != \"\"'> where r.audit_status = #{auditStatus} </if>"
            + "order by r.id desc"
            + "</script>")
    List<ReturnVO> listByStatus(@Param("auditStatus") String auditStatus);

    @Update("update return_record set audit_status = #{auditStatus}, audit_reply = #{reply} where id = #{id}")
    int audit(@Param("id") Long id, @Param("auditStatus") String auditStatus, @Param("reply") String reply);
}
