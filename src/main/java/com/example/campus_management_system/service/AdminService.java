package com.example.campus_management_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.campus_management_system.pojo.Admin;
import com.example.campus_management_system.pojo.LoginForm;

public interface AdminService extends IService<Admin> {
    Admin login(LoginForm loginForm);

    Admin getAdminById(Long userId);
}
