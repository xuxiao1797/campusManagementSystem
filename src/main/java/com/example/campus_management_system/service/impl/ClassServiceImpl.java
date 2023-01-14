package com.example.campus_management_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.campus_management_system.mapper.ClassMapper;
import com.example.campus_management_system.pojo.Class;
import com.example.campus_management_system.service.ClassService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("classServiceImpl")
@Transactional
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Class> implements ClassService {
}
