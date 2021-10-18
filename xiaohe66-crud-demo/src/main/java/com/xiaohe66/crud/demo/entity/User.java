package com.xiaohe66.crud.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xiaohe66.crud.register.scan.Crud;
import lombok.Data;

/**
 * @author xiaohe
 * @since 2021.09.26 11:45
 */
@Crud
@Data
@TableName("user")
public class User {

    private Long id;

    private String name;

}
