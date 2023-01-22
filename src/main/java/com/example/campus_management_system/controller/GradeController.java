package com.example.campus_management_system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus_management_system.pojo.Grade;
import com.example.campus_management_system.service.GradeService;
import com.example.campus_management_system.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Api(tags = "年级控制器")
@RestController
@RequestMapping("/sms/gradeController")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @ApiOperation("查询年级信息列表")
    @GetMapping("/getGrades/{pageNo}/{pageSize}")
    public Result getGrades(
            @ApiParam("分页查询页码") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("分页查询数量") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("分页查询模糊匹配名称") @RequestParam(value = "gradeName",required = false) String gradeName){

        //分页查询
        Page<Grade> page = new Page<>(pageNo,pageSize);

        IPage<Grade> pageRs =  gradeService.getGradeByOpr(page,gradeName);


        return Result.ok(pageRs);
    }

    @ApiOperation("新增或修改年级信息，有ID执行修改，没有执行增加")
    @PostMapping("/saveOrUpdateGrade")
    public Result saveOrUpdateGrade(
        @ApiParam("JSON格式的grade对象")    @RequestBody Grade grade){
        gradeService.saveOrUpdate(grade);
        return Result.ok();
    }

    @ApiOperation("删除grade信息")
    @DeleteMapping("deleteGrade")
    public Result deleteGrade(
        @ApiParam("删除的grade的id的JSON集合") @RequestBody List<Integer> ids){
        gradeService.removeByIds(ids);
        return Result.ok();
    }

    @ApiOperation("获取全部年级信息")
    @GetMapping("getGrades")
    public Result getGrades(){
        List<Grade> grades =  gradeService.getGrades();

        return Result.ok(grades);
    }
}
