package com.cr.module3.mapper;

import com.cr.module3.entity.Renewal;
import com.cr.module3.entity.RenewalVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 模块3：续租数据访问
 */
@Mapper
public interface RenewalMapper {

    String VO_COLUMNS = "r.id, r.order_id, r.start_time, r.renewal_days, r.amount, r.audit_status, "
            + "r.audit_reply, r.apply_time, o.order_no, o.user_id, v.vehicle_name, v.plate_no ";

    @Insert("insert into renewal(order_id, start_time, renewal_days, amount, audit_status, apply_time) "
            + "values(#{orderId}, #{startTime}, #{renewalDays}, #{amount}, '待审核', now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Renewal renewal);

    @Select("select * from renewal where id = #{id}")
    Renewal findById(Long id);

    @Select("select " + VO_COLUMNS + "from renewal r "
            + "join orders o on r.order_id = o.id join vehicle v on o.vehicle_id = v.id "
            + "where o.user_id = #{userId} order by r.id desc")
    List<RenewalVO> listByUser(Long userId);

    @Select("<script>"
            + "select " + VO_COLUMNS + "from renewal r "
            + "join orders o on r.order_id = o.id join vehicle v on o.vehicle_id = v.id "
            + "<if test='auditStatus != null and auditStatus != \"\"'> where r.audit_status = #{auditStatus} </if>"
            + "order by r.id desc"
            + "</script>")
    List<RenewalVO> listByStatus(@Param("auditStatus") String auditStatus);

    @Select("select count(*) from renewal where order_id = #{orderId} and audit_status = '待审核'")
    int countPending(Long orderId);

    @Update("update renewal set audit_status = #{auditStatus}, audit_reply = #{reply} where id = #{id}")
    int audit(@Param("id") Long id, @Param("auditStatus") String auditStatus, @Param("reply") String reply);
}
