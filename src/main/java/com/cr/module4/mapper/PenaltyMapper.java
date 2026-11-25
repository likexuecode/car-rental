package com.cr.module4.mapper;

import com.cr.module4.entity.Penalty;
import com.cr.module4.entity.PenaltyVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 模块4：补费数据访问
 */
@Mapper
public interface PenaltyMapper {

    String VO_COLUMNS = "p.id, p.return_id, p.title, p.type, p.fee, p.is_paid, p.create_time, "
            + "o.order_no, o.id as orderId, o.user_id, v.vehicle_name ";

    @Insert("insert into penalty(return_id, title, type, fee, is_paid, create_time) "
            + "values(#{returnId}, #{title}, #{type}, #{fee}, '未支付', now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Penalty penalty);

    @Select("select * from penalty where id = #{id}")
    Penalty findById(Long id);

    @Select("select " + VO_COLUMNS + "from penalty p "
            + "join return_record r on p.return_id = r.id "
            + "join orders o on r.order_id = o.id join vehicle v on o.vehicle_id = v.id "
            + "where o.user_id = #{userId} order by p.id desc")
    List<PenaltyVO> listByUser(Long userId);

    @Select("select " + VO_COLUMNS + "from penalty p "
            + "join return_record r on p.return_id = r.id "
            + "join orders o on r.order_id = o.id join vehicle v on o.vehicle_id = v.id "
            + "order by p.id desc")
    List<PenaltyVO> listAll();

    @Select("select " + VO_COLUMNS + "from penalty p "
            + "join return_record r on p.return_id = r.id "
            + "join orders o on r.order_id = o.id join vehicle v on o.vehicle_id = v.id "
            + "where p.return_id = #{returnId} order by p.id desc")
    List<PenaltyVO> listByReturn(Long returnId);

    @Update("update penalty set is_paid = '已支付' where id = #{id}")
    int markPaid(Long id);
}
