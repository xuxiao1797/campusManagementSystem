package com.example.campus_management_system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.campus_management_system.pojo.Admin;
import com.example.campus_management_system.pojo.LoginForm;
import com.example.campus_management_system.pojo.Student;
import com.example.campus_management_system.pojo.Teacher;
import com.example.campus_management_system.service.AdminService;
import com.example.campus_management_system.service.StudentService;
import com.example.campus_management_system.service.TeacherService;
import com.example.campus_management_system.util.*;
import com.sun.org.apache.xerces.internal.impl.xpath.XPath;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/sms/system")
public class SystemController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;

    @ApiOperation("文件上传统一入口")
    @PostMapping("/headerImgUpload")
    public Result headerImgUpload(
            @ApiParam("需要上传的文件") @RequestParam("multipartFile") MultipartFile multipartFile,
            HttpServletRequest request
    ){
        String uuid = UUID.randomUUID().toString().replace("-","").toLowerCase();
        //保存文件 响应图片的路径
        String originalFileName =  multipartFile.getOriginalFilename();
        int i = originalFileName.lastIndexOf(".");
        String newFileName =  uuid.concat(originalFileName.substring(i));

        request.getServletContext().getRealPath("public/upload/");

        String portraitPath = "D:/utilitySoftware/Project/campus_management_system/target/classes/public/upload/".concat(newFileName);

        try {
            multipartFile.transferTo(new File(portraitPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String path = "upload/".concat(newFileName);
        return Result.ok(path);
    }

    @GetMapping("/getInfo")
    public Result getInfoByToken(@RequestHeader("token") String token){
        boolean expiration =  JwtHelper.isExpiration(token);
        if(expiration){
            return Result.build(ResultCodeEnum.TOKEN_ERROR);
        }
        Long userId =  JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        Map<String,Object> map = new LinkedHashMap<>();
        switch (userType){
            case 1:
                Admin admin = adminService.getAdminById(userId);
                map.put("userType",1);
                map.put("user",admin);
                break;
            case 2:
                Student student = studentService.getStudentByID(userId);
                map.put("userType",2);
                map.put("user",student);
                break;
            case 3:
                Teacher teacher = teacherService.getTeacherByID(userId);
                map.put("userType",3);
                map.put("user",teacher);
                break;
        }


        return Result.ok(map);
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginForm loginForm,HttpServletRequest request){
        //校验验证码
        HttpSession session = request.getSession();
        String verifyCode = (String) session.getAttribute("verifyCode");
        String inputVerifyCode= loginForm.getVerifiCode();
        if("".equalsIgnoreCase(verifyCode) || verifyCode == null){
            return Result.fail().message("验证码失效，请刷新后重试");
        }
        if(!verifyCode.equalsIgnoreCase(inputVerifyCode)){
            return Result.fail().message("验证码不正确");
        }
        session.removeAttribute("verifyCode");

        //用户类型校验  1-管理员 2-学生 3-教师
        Map<String,Object> map = new LinkedHashMap<>();

        switch (loginForm.getUserType()){
            case 1:
                try {
                    Admin admin = adminService.login(loginForm);
                    if(admin != null){
                        String token = JwtHelper.createToken(admin.getId().longValue(),1);
                        map.put("token",token);
                    }else {
                        throw new RuntimeException("用户名或密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }

            case 2:
                try {
                    Student student = studentService.login(loginForm);
                    if(student != null){
                        String token = JwtHelper.createToken(student.getId().longValue(),2);
                        map.put("token",token);
                    }else {
                        throw new RuntimeException("用户名或密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 3:
                try {
                    Teacher teacher = teacherService.login(loginForm);
                    if(teacher != null){
                        String token = JwtHelper.createToken(teacher.getId().longValue(),3);
                        map.put("token",token);
                    }else {
                        throw new RuntimeException("用户名或密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
        }

        return Result.fail().message("未找到用户");

    }


    @GetMapping("/getVerifiCodeImage")
    public void getVerifyCodeImage(HttpServletRequest request, HttpServletResponse response){
        //获取验证码图片
        BufferedImage verifyCodeImage =  CreateVerifiCodeImage.getVerifiCodeImage();
        String verifyCode =  new String( CreateVerifiCodeImage.getVerifiCode());
        HttpSession session = request.getSession();
        session.setAttribute("verifyCode",verifyCode);

        try {
            ImageIO.write(verifyCodeImage,"JPEG",response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @ApiOperation("修改密码")
    @PostMapping("/updatePwd/{oldPwd}/{newPwd}")
    public Result updatePwd(
           @ApiParam("token") @RequestHeader("token") String token,
           @ApiParam("旧密码") @PathVariable("oldPwd") String oldPwd,
           @ApiParam("新密码") @PathVariable("newPwd") String newPwd
    ){
        boolean expiration =  JwtHelper.isExpiration(token);
        if(expiration){
            return Result.fail().message("登录状态失效，请重新登录");
        }
        Long userId = JwtHelper.getUserId(token);
        Integer userType =  JwtHelper.getUserType(token);
        oldPwd = MD5.encrypt(oldPwd);
        newPwd = MD5.encrypt(newPwd);

        switch (userType){
            case 1:
                QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("id",userId.intValue());
                queryWrapper.eq("password",oldPwd);
                Admin admin =  adminService.getOne(queryWrapper);
                if(admin != null){
                    admin.setPassword(newPwd);
                    adminService.saveOrUpdate(admin);
                }else {
                    return Result.fail().message("原密码有误");
                }
                break;
            case 2:
                QueryWrapper<Student> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("id",userId.intValue());
                queryWrapper2.eq("password",oldPwd);
                Student student =  studentService.getOne(queryWrapper2);
                if(student != null){
                    student.setPassword(newPwd);
                    studentService.saveOrUpdate(student);
                }else {
                    return Result.fail().message("原密码有误");
                }
                break;
            case 3:
                QueryWrapper<Teacher> queryWrapper3 = new QueryWrapper<>();
                queryWrapper3.eq("id",userId.intValue());
                queryWrapper3.eq("password",oldPwd);
                Teacher teacher =  teacherService.getOne(queryWrapper3);
                if(teacher != null){
                    teacher.setPassword(newPwd);
                    teacherService.saveOrUpdate(teacher);
                }else {
                    return Result.fail().message("原密码有误");
                }
                break;

        }

        return Result.ok();

    }


}
