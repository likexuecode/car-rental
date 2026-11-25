package com.cr.module5.mapper;

import com.cr.module5.entity.Complaint;
import com.cr.module5.entity.ComplaintVO;
import com.cr.module5.entity.CustomerService;
import com.cr.module5.entity.Announcement;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 模块5：投诉、在线客服、公告数据访问
 */
@Mapper
public interface Module5Mapper {

    // ==================== 投诉反馈 ====================

    @Insert("insert into complaint(user_id, title, content, picture, status, create_time) "
            + "values(#{userId}, #{title}, #{content}, #{picture}, '待处理', now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertComplaint(Complaint complaint);

    @Select("select * from complaint where id = #{id}")
    Complaint findComplaint(Long id);

    @Select("select c.id, c.user_id, u.name as userName, c.title, c.content, c.picture, c.reply, "
            + "c.status, c.create_time from complaint c join `user` u on c.user_id = u.id "
            + "where c.user_id = #{userId} order by c.id desc")
    List<ComplaintVO> listComplaintByUser(Long userId);

    @Select("select c.id, c.user_id, u.name as userName, c.title, c.content, c.picture, c.reply, "
            + "c.status, c.create_time from complaint c join `user` u on c.user_id = u.id "
            + "order by c.id desc")
    List<ComplaintVO> listAllComplaints();

    @Update("update complaint set reply = #{reply}, status = '已回复' where id = #{id}")
    int replyComplaint(@Param("id") Long id, @Param("reply") String reply);

    // ==================== 在线客服 ====================

    @Insert("insert into customer_service(user_id, ask, is_reply, create_time) "
            + "values(#{userId}, #{ask}, 0, now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertService(CustomerService service);

    @Select("select * from customer_service where id = #{id}")
    CustomerService findService(Long id);

    @Select("select * from customer_service where user_id = #{userId} order by id desc")
    List<CustomerService> listServiceByUser(Long userId);

    @Select("select s.*, u.name as userName from customer_service s "
            + "join `user` u on s.user_id = u.id order by s.id desc")
    List<CustomerService> listAllService();

    @Update("update customer_service set reply = #{reply}, admin_id = #{adminId}, is_reply = 1 where id = #{id}")
    int replyService(@Param("id") Long id, @Param("adminId") Long adminId, @Param("reply") String reply);

    // ==================== 通知公告 ====================

    @Insert("insert into announcement(title, content, create_time) values(#{title}, #{content}, now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertAnnouncement(Announcement announcement);

    @Select("select * from announcement order by id desc")
    List<Announcement> listAnnouncements();

    @Delete("delete from announcement where id = #{id}")
    int deleteAnnouncement(Long id);
}
