package com.gxdemo.dto;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
//@TableName("")   //置顶具体表名
public class ProductCategory {

    //@TableId //主键
    @TableId(type = IdType.AUTO)
    private  Integer categoryId;

    // @TableField("name")   //指定具体表面
    private  String categoryName;

    private  Integer categoryType;

    @TableField( fill = FieldFill.INSERT)// 新增执行
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE) // 新增和更新执行
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private  LocalDateTime updateTime;

    //方式1   关键字transient 不参与序列序列化 private transient String remark;
    //方式2   关键字static 忽略 参与序列化  private static String remark;
    //方式3 @TableField(exist = false)
    //private transient String remark;//不对应具体字段（不存在d字段）

}
