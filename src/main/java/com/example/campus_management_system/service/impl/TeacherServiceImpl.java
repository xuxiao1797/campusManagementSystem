package com.example.campus_management_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.campus_management_system.mapper.TeacherMapper;
import com.example.campus_management_system.pojo.LoginForm;
import com.example.campus_management_system.pojo.Teacher;
import com.example.campus_management_system.service.TeacherService;
import com.example.campus_management_system.util.MD5;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service("teacherServiceImpl")
@Transactional
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {
    @Override
    public Teacher login(LoginForm loginForm) {
        QueryWrapper<Teacher> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("name",loginForm.getUsername());
        QueryWrapper.eq("password",MD5.encrypt(loginForm.getPassword()));
        Teacher teacher = baseMapper.selectOne(QueryWrapper);
        return teacher;
    }

    @Override
    public Teacher getTeacherByID(Long userId) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",userId);
        Teacher teacher =  baseMapper.selectOne(queryWrapper);
        return teacher;
    }

    @Override
    public IPage<Teacher> getTeacherByOpr(Page<Teacher> pageParam, Teacher teacher) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper();
        String className = teacher.getClazzName();
        if(!StringUtils.isEmpty(className)){
            queryWrapper.like("clazz_name",className);
        }
        String name = teacher.getName();
        if(!StringUtils.isEmpty(name)){
            queryWrapper.like("name",name);
        }
        queryWrapper.orderByDesc("id");
        Page<Teacher> teacherPage = baseMapper.selectPage(pageParam,queryWrapper);
        return teacherPage;
    }
}
