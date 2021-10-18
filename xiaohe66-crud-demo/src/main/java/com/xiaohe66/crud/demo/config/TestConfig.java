package com.xiaohe66.crud.demo.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @author xiaohe
 * @since 2021.10.18 14:48
 */
@Component
public class TestConfig implements InitializingBean {


    @Override
    public void afterPropertiesSet() throws Exception {

        System.out.println("----------------------------------");
    }
}
