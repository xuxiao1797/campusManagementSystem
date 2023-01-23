package com.example.campus_management_system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus_management_system.pojo.Admin;
import com.example.campus_management_system.service.AdminService;
import com.example.campus_management_system.util.MD5;
import com.example.campus_management_system.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController  //异步交互
@RequestMapping("/sms/adminController")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @ApiOperation("分页带条件查询管理员信息")
    @GetMapping("/getAllAdmin/{pageNo}/{pageSize}")
    public Result getAllAdmin(
            @ApiParam("页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("每页信息数量") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("管理员名称") String adminName){

        Page<Admin> pageParam = new Page<>(pageNo,pageSize);
        IPage<Admin> iPage =  adminService.getAdminByOpr(pageParam,adminName);

        return Result.ok(iPage);
    }

    @ApiOperation("修改或增加管理员信息")
    @PostMapping("/saveOrUpdateAdmin")
    public Result saveOrUpdateAdmin(
            @ApiParam("JSON格式的Admin对象")
            @RequestBody Admin admin
    ){
       if(null == admin.getId() || 0 == admin.getId()){
           admin.setPassword(MD5.encrypt(admin.getPassword()));
       }

       adminService.saveOrUpdate(admin);
       return Result.ok();
    }

    @ApiOperation("删除管理员信息")
    @DeleteMapping("/deleteAdmin")
    public Result deleteAdmin(
            @ApiParam("删除的管理员id")
           @RequestBody List<Integer> ids
    ){
        adminService.removeByIds(ids);
        return Result.ok();
    }
}
