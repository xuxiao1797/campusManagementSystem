package com.example.campus_management_system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.campus_management_system.pojo.LoginForm;
import com.example.campus_management_system.pojo.Student;

public interface StudentService extends IService<Student> {
    Student login(LoginForm loginForm);

    Student getStudentByID(Long userId);

    IPage<Student> getStudentByOpr(Page<Student> pageParam, Student student);
}
