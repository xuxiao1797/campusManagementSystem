package com.example.campus_management_system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.campus_management_system.pojo.Class;

import java.util.List;

public interface ClassService extends IService<Class> {
    IPage<Class> getClassByOpr(Page<Class> page,Class clazz);

    List<Class> getClazzs();
}
