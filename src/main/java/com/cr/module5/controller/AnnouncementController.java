package com.cr.module5.controller;

import com.cr.common.Result;
import com.cr.module5.entity.Announcement;
import com.cr.module5.service.Module5Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 模块5：通知公告接口
 */
@RestController
public class AnnouncementController {

    private final Module5Service service;

    public AnnouncementController(Module5Service service) {
        this.service = service;
    }

    @GetMapping("/api/announcements")
    public Result<List<Announcement>> list() {
        return Result.ok(service.listAnnouncements());
    }

    @PostMapping("/api/admin/announcements")
    public Result<Void> add(@RequestBody Announcement announcement) {
        service.addAnnouncement(announcement.getTitle(), announcement.getContent());
        return Result.ok();
    }

    @DeleteMapping("/api/admin/announcements/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        service.deleteAnnouncement(id);
        return Result.ok();
    }
}
