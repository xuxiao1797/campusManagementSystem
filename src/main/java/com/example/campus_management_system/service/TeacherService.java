package com.example.campus_management_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.campus_management_system.pojo.LoginForm;
import com.example.campus_management_system.pojo.Teacher;

public interface TeacherService extends IService<Teacher> {
    Teacher login(LoginForm loginForm);

    Teacher getTeacherByID(Long userId);
}
