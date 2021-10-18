package com.xiaohe66.crud.register;


import com.xiaohe66.crud.register.scan.CrudEntityWrapper;
import com.xiaohe66.crud.server.ICrudService;

/**
 * @author xiaohe
 * @since 2021.09.28 15:52
 */
public interface ICrudServiceFactory {

    ICrudService<?> create(CrudEntityWrapper entityWrapper);

}
