package com.example.campus_management_system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus_management_system.pojo.Student;
import com.example.campus_management_system.service.StudentService;
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
@RequestMapping("/sms/studentController")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @ApiOperation("分页查询学生信息")
    @GetMapping("/getStudentByOpr/{pageNo}/{pageSize}")
    public Result getStudentByOpr(
            @ApiParam("页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("每页信息数量") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("查询条件") Student student
            ){
        Page<Student> pageParam = new Page(pageNo,pageSize);
        IPage<Student> studentIPage =  studentService.getStudentByOpr(pageParam,student);

        return Result.ok(studentIPage);
    }

    @ApiOperation("新建或修改学生信息")
    @PostMapping("/addOrUpdateStudent")
    public Result addOrUpdateStudent(
           @RequestBody Student student
    ){
       Integer id =  student.getId();

       if(null == id || 0 == id){
           student.setPassword(MD5.encrypt(student.getPassword()));
       }
       studentService.saveOrUpdate(student);
       return Result.ok();
    }


    @ApiOperation("删除学生信息")
    @DeleteMapping("/delStudentById")
    public Result delStudentById(
           @RequestBody List<Integer> ids
    ){
        studentService.removeByIds(ids);
        return Result.ok();
    }
}
