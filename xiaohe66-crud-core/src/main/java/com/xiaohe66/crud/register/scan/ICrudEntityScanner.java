package com.xiaohe66.crud.register.scan;

import java.util.List;

/**
 * @author xiaohe
 * @since 2021.09.24 14:02
 */
public interface ICrudEntityScanner {

    /**
     * 扫描
     * @param basePackage 被扫描的饭
     *
     * @return 被扫描到的实体
     */
    List<CrudEntityWrapper> scan(String basePackage);

}
