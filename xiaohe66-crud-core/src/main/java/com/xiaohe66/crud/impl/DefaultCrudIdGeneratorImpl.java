package com.xiaohe66.crud.impl;

import com.xiaohe66.common.util.IdWorker;
import com.xiaohe66.crud.server.ICrudIdGenerator;

import java.util.Map;

/**
 * 雪花 ID生成器
 *
 * @author xiaohe
 * @since 2021.10.18 18:03
 */
public class DefaultCrudIdGeneratorImpl implements ICrudIdGenerator<Long> {

    private IdWorker idWorker = new IdWorker(1);

    @Override
    public Long generate(Map<String, Object> params) {

        return idWorker.next();
    }
}
