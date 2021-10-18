package com.xiaohe66.crud.server;


import com.xiaohe66.common.dto.Page;

/**
 * @author xiaohe
 * @since 2021.09.26 11:57
 */
public interface ICrudService<T> {

    /**
     * 新增
     *
     * @param params 参数
     * @return 是否成功
     */
    boolean add(T params);

    /**
     * 修改
     *
     * @param params 参数
     * @return 是否成功
     */
    boolean modifyById(T params);

    /**
     * 根据参数查询
     *
     * @param params   参数
     * @param pageNo   页
     * @param pageSize 分页大小
     * @return 分页数据
     */
    Page<T> listByParam(T params, int pageNo, int pageSize);

    /**
     * 根据ID查询
     *
     * @param id id
     * @return 实体数据
     */
    T getById(Long id);

    /**
     * 根据ID删除
     *
     * @param id id
     * @return 是否成功
     */
    boolean removeById(Long id);

    /**
     * 当时servcie 表示的实体
     *
     * @return 实体的 Class 对象
     */
    Class<T> getEntityCls();

}