package com.example.campus_management_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.campus_management_system.mapper.StudentMapper;
import com.example.campus_management_system.pojo.LoginForm;
import com.example.campus_management_system.pojo.Student;
import com.example.campus_management_system.service.StudentService;
import com.example.campus_management_system.util.MD5;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service("studentServiceImpl")
@Transactional
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    @Override
    public Student login(LoginForm loginForm) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",loginForm.getUsername());
        queryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));

        Student student = baseMapper.selectOne(queryWrapper);

        return student;
    }

    @Override
    public Student getStudentByID(Long userId) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",userId);
        Student student = baseMapper.selectOne(queryWrapper);
        return student;
    }

    @Override
    public IPage<Student> getStudentByOpr(Page<Student> pageParam, Student student) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        String studentName = student.getName();
        if(!StringUtils.isEmpty(studentName)){
            queryWrapper.like("name",studentName);
        }
        String className = student.getClazzName();
        if(!StringUtils.isEmpty(className)){
            queryWrapper.like("clazz_name",className);
        }
        queryWrapper.orderByDesc("id");

        Page<Student> studentPage = baseMapper.selectPage(pageParam,queryWrapper);
        return studentPage;
    }
}
