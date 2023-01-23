package com.example.campus_management_system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus_management_system.pojo.Class;
import com.example.campus_management_system.service.ClassService;
import com.example.campus_management_system.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sms/clazzController")
public class ClassController {
    @Autowired
    private ClassService classService;


    @ApiOperation("查询所有班级信息")
    @GetMapping("/getClazzs")
    public Result getClazzs(){
        List<Class> classes =  classService.getClazzs();
        return Result.ok(classes);
    }

    @GetMapping("/getClazzsByOpr/{pageNo}/{pageSize}")
    public Result getClazzByOpr(
          @ApiParam("分页查询页码数") @PathVariable("pageNo") Integer pageNo,
          @ApiParam("分页查询每页数量") @PathVariable("pageSize")  Integer pageSize,
          @ApiParam("分页查询条件") Class clazz
    ){
        Page<Class> page = new Page<>(pageNo,pageSize);

        IPage<Class> Ipage =  classService.getClassByOpr(page,clazz);

        return Result.ok(Ipage);
    }

    @ApiOperation("增加或修改班级信息")
    @PostMapping("/saveOrUpdateClazz")
    public Result saveOrUpdateClazz(
            @ApiParam("JSON格式的班级信息")
            @RequestBody Class clazz
    ){
        classService.saveOrUpdate(clazz);
        return Result.ok();
    }

    @ApiOperation("删除班级信息")
    @DeleteMapping("/deleteClazz")
    public Result deleteClazz(
            @RequestBody List<Integer> ids){
        classService.removeByIds(ids);
        return Result.ok();
    }
}
