package com.xiaohe66.crud.impl;

import com.xiaohe66.crud.register.ICrudServiceFactory;
import com.xiaohe66.crud.register.scan.CrudEntityWrapper;
import com.xiaohe66.crud.server.ICrudIdGenerator;
import com.xiaohe66.crud.server.ICrudService;
import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;

/**
 * @author xiaohe
 * @since 2021.09.28 15:56
 */
@RequiredArgsConstructor
public class DefaultCrudServiceFactoryImpl implements ICrudServiceFactory {

    private final DataSource dataSource;
    private final ICrudIdGenerator<?> crudIdGenerator;

    @Override
    public ICrudService<?> create(CrudEntityWrapper entityWrapper) {

        return new DefaultCrudServiceImpl(entityWrapper, dataSource, crudIdGenerator);
    }
}
