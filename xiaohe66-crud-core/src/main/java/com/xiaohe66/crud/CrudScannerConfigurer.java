package com.xiaohe66.crud;

import com.xiaohe66.crud.register.ICrudServiceFactory;
import com.xiaohe66.crud.register.scan.CrudEntityWrapper;
import com.xiaohe66.crud.register.scan.ICrudEntityScanner;
import com.xiaohe66.crud.server.ICrudDispatcher;
import com.xiaohe66.crud.server.ICrudService;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaohe
 * @since 2021.10.12 10:28
 */
public class CrudScannerConfigurer implements ApplicationContextAware, InitializingBean {

    /**
     * 用于保存所有自定义的 CrudService的 Map集合, key为实体的Class对象, value为实现类
     */
    private Map<Class<?>, ICrudService<?>> crudServiceMap = new ConcurrentHashMap<>();

    private ApplicationContext applicationContext;

    @Setter
    private String basePackage;

    @Override
    public void afterPropertiesSet() throws Exception {

        ICrudEntityScanner entityScanner = applicationContext.getBean(ICrudEntityScanner.class);
        ICrudServiceFactory crudServiceFactory = applicationContext.getBean(ICrudServiceFactory.class);
        ICrudDispatcher crudDispatcher = applicationContext.getBean(ICrudDispatcher.class);

        // 1. 获取所有的 CrudService
        Map<String, ICrudService> map = applicationContext.getBeansOfType(ICrudService.class);

        for (Map.Entry<String, ICrudService> entry : map.entrySet()) {

            ICrudService value = entry.getValue();

            crudServiceMap.put(value.getEntityCls(), value);
        }

        // 2. 扫描所有 Crud 实体
        List<CrudEntityWrapper> classWrapperList = entityScanner.scan(basePackage);

        // 3. 注册 CrudService , 若无实现，则使用默认实现
        for (CrudEntityWrapper entityWrapper : classWrapperList) {

            ICrudService<?> crudService = crudServiceMap.get(entityWrapper.getCls());

            if (crudService == null) {

                crudService = crudServiceFactory.create(entityWrapper);
            }

            crudDispatcher.registerService(entityWrapper.getName(), crudService);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
