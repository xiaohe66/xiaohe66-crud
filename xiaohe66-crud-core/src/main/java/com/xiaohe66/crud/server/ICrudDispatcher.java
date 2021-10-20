package com.xiaohe66.crud.server;


import com.xiaohe66.common.dto.Page;

/**
 * @author xiaohe
 * @since 2021.09.24 17:53
 */
public interface ICrudDispatcher {

    void registerService(String name, ICrudService<?> service);

    void removeService(String name);

    <T> ICrudService<T> getService(String name);

    boolean post(String table, String body);

    boolean put(String table, String body);

    Page<Object> get(String table, String body, int pageNo, int pageSize);

    Object get(String table, Long id);

    boolean delete(String table, Long id);
}
