package com.xiaohe66.crud.register.scan;

import lombok.Data;

import java.util.Set;

/**
 * @author xiaohe
 * @since 2021.09.26 10:36
 */
@Data
public class CrudEntityWrapper {

    /**
     * 实体名, 默认为？？
     */
    private String name;

    /**
     * 数据库的表名
     */
    private String tableName;

    /**
     * 实体包含的字段
     */
    private Set<String> fieldNames;

    /**
     * 实体的 Class 对象
     */
    private Class<?> cls;

}
