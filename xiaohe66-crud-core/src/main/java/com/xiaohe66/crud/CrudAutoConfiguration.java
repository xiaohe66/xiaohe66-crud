package com.xiaohe66.crud;

import com.xiaohe66.crud.impl.DefaultCrudDispatcher;
import com.xiaohe66.crud.impl.DefaultCrudEntityScanner;
import com.xiaohe66.crud.impl.DefaultCrudIdGeneratorImpl;
import com.xiaohe66.crud.impl.DefaultCrudServiceFactoryImpl;
import com.xiaohe66.crud.register.ICrudServiceFactory;
import com.xiaohe66.crud.register.scan.ICrudEntityScanner;
import com.xiaohe66.crud.server.CrudController;
import com.xiaohe66.crud.server.ICrudDispatcher;
import com.xiaohe66.crud.server.ICrudIdGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author xiaohe
 * @since 2021.09.24 11:43
 */
@Configuration
public class CrudAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ICrudDispatcher crudDispatcher() {

        return new DefaultCrudDispatcher();
    }

    @Bean
    @ConditionalOnMissingBean
    public ICrudEntityScanner crudEntityScanner() {
        return new DefaultCrudEntityScanner();
    }

    @Bean
    @ConditionalOnMissingBean
    public ICrudIdGenerator<Long> crudIdGenerator() {
        return new DefaultCrudIdGeneratorImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public ICrudServiceFactory crudServiceFactory(DataSource dataSource, ICrudIdGenerator crudIdGenerator) {
        return new DefaultCrudServiceFactoryImpl(dataSource, crudIdGenerator);
    }

    @Bean
    public CrudController crudController(ICrudDispatcher crudDispatcher) {
        return new CrudController(crudDispatcher);
    }
}
