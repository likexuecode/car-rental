package com.cr.module1.service;

import com.cr.common.BizException;
import com.cr.common.TokenStore;
import com.cr.module1.entity.User;
import com.cr.module1.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

/**
 * 模块1：用户与认证 —— 注册、登录、个人信息
 */
@Service
public class UserService {

    private static final Pattern PHONE = Pattern.compile("^1[3-9]\\d{9}$");

    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 契约1：register(account, password, confirmPassword, name, phone, idCard)
     */
    public void register(String account, String password, String confirmPassword,
                         String name, String phone, String idCard) {
        if (!StringUtils.hasText(account) || !StringUtils.hasText(password) || !StringUtils.hasText(name)) {
            throw new BizException("必填项不能为空");
        }
        if (userMapper.findByAccount(account) != null) {
            throw new BizException("该账号已被注册");
        }
        if (password.length() < 6) {
            throw new BizException("密码至少6位");
        }
        if (!password.equals(confirmPassword)) {
            throw new BizException("两次输入密码不一致");
        }
        if (!StringUtils.hasText(phone) || !PHONE.matcher(phone).matches()) {
            throw new BizException("请输入正确的手机号");
        }
        User user = new User();
        user.setAccount(account);
        user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)));
        user.setName(name);
        user.setPhone(phone);
        user.setIdCard(idCard);
        user.setRole("USER");
        userMapper.insert(user);
    }

    /**
     * 登录：校验密码生成 Token
     */
    public TokenStore.SessionUser login(String account, String password) {
        User user = userMapper.findByAccount(account);
        if (user == null) {
            throw new BizException("账号或密码错误");
        }
        String md5 = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        if (!md5.equals(user.getPassword())) {
            throw new BizException("账号或密码错误");
        }
        if (user.getState() != null && user.getState() == 0) {
            throw new BizException("该账号已被禁用");
        }
        return new TokenStore.SessionUser(user.getId(), user.getAccount(), user.getRole());
    }

    public User getById(Long id) {
        User user = userMapper.findById(id);
        if (user == null) {
            throw new BizException("用户不存在");
        }
        return user;
    }

    public void updateProfile(Long userId, String name, String phone, String idCard) {
        if (!StringUtils.hasText(name)) {
            throw new BizException("姓名不能为空");
        }
        if (StringUtils.hasText(phone) && !PHONE.matcher(phone).matches()) {
            throw new BizException("请输入正确的手机号");
        }
        User user = new User();
        user.setId(userId);
        user.setName(name);
        user.setPhone(phone);
        user.setIdCard(idCard);
        userMapper.updateProfile(user);
    }
}
