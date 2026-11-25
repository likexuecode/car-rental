package com.cr.common;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 图片上传（车辆图片、投诉图片共用）
 */
@RestController
public class UploadController {

    private static final long MAX_SIZE = 2 * 1024 * 1024L;

    @PostMapping("/api/upload")
    public Result<Map<String, String>> upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new BizException("请选择要上传的图片");
        }
        String name = file.getOriginalFilename() == null ? "" : file.getOriginalFilename().toLowerCase();
        if (!name.endsWith(".jpg") && !name.endsWith(".jpeg") && !name.endsWith(".png")) {
            throw new BizException("请上传jpg或png格式的图片");
        }
        if (file.getSize() > MAX_SIZE) {
            throw new BizException("图片大小不能超过2MB");
        }
        File dir = new File("upload");
        if (!dir.exists() && !dir.mkdirs()) {
            throw new BizException("上传目录创建失败");
        }
        String ext = name.substring(name.lastIndexOf('.'));
        String fileName = UUID.randomUUID().toString().replace("-", "") + ext;
        file.transferTo(new File(dir, fileName));
        Map<String, String> data = new HashMap<>();
        data.put("url", "/upload/" + fileName);
        return Result.ok(data);
    }
}
