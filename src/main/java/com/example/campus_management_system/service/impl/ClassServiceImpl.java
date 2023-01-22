package com.example.campus_management_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.campus_management_system.mapper.ClassMapper;
import com.example.campus_management_system.pojo.Class;
import com.example.campus_management_system.service.ClassService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service("classServiceImpl")
@Transactional
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Class> implements ClassService {
    @Override
    public IPage<Class> getClassByOpr(Page<Class> pageParam, Class clazz) {
        QueryWrapper<Class> queryWrapper = new QueryWrapper<>();
        String gradeName = clazz.getGradeName();
        if(!StringUtils.isEmpty(gradeName)){
            queryWrapper.like("grade_name",gradeName);
        }
        String name = clazz.getName();
        if(!StringUtils.isEmpty(name)){
            queryWrapper.like("name",name);
        }

        queryWrapper.orderByDesc("id");

        return baseMapper.selectPage(pageParam,queryWrapper);
    }
}
