package com.xiaohe66.crud.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xiaohe66.common.dto.Page;
import com.xiaohe66.common.util.JsonUtils;
import com.xiaohe66.crud.ex.CrudException;
import com.xiaohe66.crud.server.ICrudDispatcher;
import com.xiaohe66.crud.server.ICrudService;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 请求分发器，当不存在处理器时，抛出异常
 *
 * @author xiaohe
 * @since 2021.09.26 10:40
 */
@RequiredArgsConstructor
public class DefaultCrudDispatcher implements ICrudDispatcher {

    private final Map<String, ICrudService<Object>> serviceImplMap = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public void registerService(String name, ICrudService<?> service) {

        serviceImplMap.put(name, (ICrudService<Object>) service);
    }

    @Override
    public void removeService(String name) {
        serviceImplMap.remove(name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> ICrudService<T> getService(String name) {
        return (ICrudService<T>) serviceImplMap.get(name);
    }

    @Override
    public boolean post(String name, String body) {

        checkHasService(name, body);

        ICrudService<Object> service = serviceImplMap.get(name);

        Object object = formatEntity(name, body, service.getEntityCls());

        return service.add(object);
    }

    @Override
    public boolean put(String name, String body) {

        checkHasService(name, body);

        ICrudService<Object> service = serviceImplMap.get(name);

        Object object = formatEntity(name, body, service.getEntityCls());

        return service.modifyById(object);
    }

    @Override
    public Page<Object> get(String name, String body, int pageNo, int pageSize) {

        checkHasService(name, body);

        ICrudService<Object> service = serviceImplMap.get(name);

        Object object = formatEntity(name, body, service.getEntityCls());

        return service.listByParam(object, pageNo, pageSize);
    }

    @Override
    public Object get(String name, Long id) {

        checkHasService(name, id);

        ICrudService<Object> service = serviceImplMap.get(name);

        return service.getById(id);
    }

    @Override
    public boolean delete(String name, Long id) {

        checkHasService(name, id);

        ICrudService<Object> service = serviceImplMap.get(name);

        return service.removeById(id);
    }

    protected Object formatEntity(String name, String body, Class<?> cls) {

        try {

            Class<?> clz = Objects.requireNonNullElse(cls, Map.class);

            return JsonUtils.formatObject(body, clz);

        } catch (JsonProcessingException e) {
            throw new CrudException(e, name, body);
        }
    }

    protected void checkHasService(String name, Object body) {
        if (!serviceImplMap.containsKey(name)) {
            throw new CrudException("not found service", name, String.valueOf(body));
        }
    }
}
