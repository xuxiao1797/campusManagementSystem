package com.example.campus_management_system.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_class")
public class Class {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String name;
    private Integer number;
    private String introduction;
    private String headmaster;
    private String email;
    private String telephone;
    private String grade_name;
}
