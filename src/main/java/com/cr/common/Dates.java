package com.cr.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期解析工具：兼容 2026-11-25T10:00 / 2026-11-25 10:00 / 2026-11-25 10:00:00
 */
public class Dates {

    private static final String[] PATTERNS = {
            "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd'T'HH:mm",
            "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM-dd"
    };

    public static Date parse(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        String value = text.trim();
        for (String pattern : PATTERNS) {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            format.setLenient(false);
            try {
                return format.parse(value);
            } catch (ParseException ignored) {
                // 尝试下一种格式
            }
        }
        throw new BizException("时间格式不正确");
    }
}
