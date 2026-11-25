package com.cr.module5.service;

import com.cr.common.BizException;
import com.cr.module5.entity.Announcement;
import com.cr.module5.entity.Complaint;
import com.cr.module5.entity.ComplaintVO;
import com.cr.module5.entity.CustomerService;
import com.cr.module5.mapper.Module5Mapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 模块5：客服与公告 —— 投诉反馈、在线客服、通知公告
 */
@Service
public class Module5Service {

    private final Module5Mapper mapper;

    public Module5Service(Module5Mapper mapper) {
        this.mapper = mapper;
    }

    // ==================== 投诉反馈 ====================

    /**
     * 契约5：submitComplaint —— 提交投诉反馈
     */
    public void submitComplaint(Long userId, String title, String content, String picture) {
        if (!StringUtils.hasText(title) || !StringUtils.hasText(content)) {
            throw new BizException("请填写完整的投诉信息");
        }
        Complaint complaint = new Complaint();
        complaint.setUserId(userId);
        complaint.setTitle(title);
        complaint.setContent(content);
        complaint.setPicture(picture);
        mapper.insertComplaint(complaint);
    }

    public List<ComplaintVO> myComplaints(Long userId) {
        return mapper.listComplaintByUser(userId);
    }

    public List<ComplaintVO> allComplaints() {
        return mapper.listAllComplaints();
    }

    public void replyComplaint(Long id, String reply) {
        if (mapper.findComplaint(id) == null) {
            throw new BizException("投诉记录不存在");
        }
        if (!StringUtils.hasText(reply)) {
            throw new BizException("请填写回复内容");
        }
        mapper.replyComplaint(id, reply);
    }

    // ==================== 在线客服 ====================

    public void ask(Long userId, String ask) {
        if (!StringUtils.hasText(ask)) {
            throw new BizException("请输入咨询内容");
        }
        CustomerService service = new CustomerService();
        service.setUserId(userId);
        service.setAsk(ask);
        mapper.insertService(service);
    }

    public List<CustomerService> myService(Long userId) {
        return mapper.listServiceByUser(userId);
    }

    public List<CustomerService> allService() {
        return mapper.listAllService();
    }

    public void replyService(Long adminId, Long id, String reply) {
        CustomerService service = mapper.findService(id);
        if (service == null) {
            throw new BizException("咨询记录不存在");
        }
        if (service.getIsReply() != null && service.getIsReply() == 1) {
            throw new BizException("该咨询已回复");
        }
        if (!StringUtils.hasText(reply)) {
            throw new BizException("请填写回复内容");
        }
        mapper.replyService(id, adminId, reply);
    }

    // ==================== 通知公告 ====================

    public List<Announcement> listAnnouncements() {
        return mapper.listAnnouncements();
    }

    public void addAnnouncement(String title, String content) {
        if (!StringUtils.hasText(title)) {
            throw new BizException("公告标题不能为空");
        }
        Announcement announcement = new Announcement();
        announcement.setTitle(title);
        announcement.setContent(content);
        mapper.insertAnnouncement(announcement);
    }

    public void deleteAnnouncement(Long id) {
        mapper.deleteAnnouncement(id);
    }
}
