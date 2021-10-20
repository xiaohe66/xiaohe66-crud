package com.xiaohe66.crud.server;

import java.util.Map;

/**
 * @author xiaohe
 * @since 2021.10.18 17:59
 */
public interface ICrudIdGenerator<T> {


    /**
     * 生成id
     *
     * @param params 参数
     * @return 生成的id
     */
    T generate(Map<String, Object> params);

}
