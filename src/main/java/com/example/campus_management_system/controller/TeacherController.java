package com.example.campus_management_system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus_management_system.pojo.Student;
import com.example.campus_management_system.pojo.Teacher;
import com.example.campus_management_system.service.TeacherService;
import com.example.campus_management_system.util.MD5;
import com.example.campus_management_system.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sms/teacherController")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @ApiOperation("获取教师信息")
    @GetMapping("/getTeachers/{pageNo}/{pageSize}")
    public Result getTeacher(
           @ApiParam("页码") @PathVariable Integer pageNo,
           @ApiParam("每页数量") @PathVariable Integer pageSize,
           @ApiParam("筛选") Teacher teacher
            ){
        Page<Teacher> pageParam = new Page(pageNo,pageSize);
        IPage<Teacher> iPage = teacherService.getTeacherByOpr(pageParam,teacher);

        return Result.ok(iPage);
    }

    @ApiOperation("新建或修改教师信息")
    @PostMapping("/saveOrUpdateTeacher")
    public Result saveOrUpdateTeacher(
           @ApiParam("JSON格式的教师信息") @RequestBody Teacher teacher
    ){

        Integer id =  teacher.getId();

        if(null == id || 0 == id){
            teacher.setPassword(MD5.encrypt(teacher.getPassword()));
        }
        teacherService.saveOrUpdate(teacher);
        return Result.ok();
    }

    @ApiOperation("删除教师信息")
    @DeleteMapping("/deleteTeacher")
    public Result deleteTeacher(
           @ApiParam("删除的id") @RequestBody List<Integer> ids
    ){
        teacherService.removeByIds(ids);
        return Result.ok();
    }
}
