package com.xiaohe66.crud.register;

import com.xiaohe66.crud.CrudScannerConfigurer;
import com.xiaohe66.crud.EnableCrud;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;

import java.util.Map;

/**
 * @author xiaohe
 * @since 2021.10.12 10:23
 */
public class EnableCrudRegister implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {

        Map<String, Object> enableCrudAttrs = annotationMetadata.getAnnotationAttributes(EnableCrud.class.getName());

        if (enableCrudAttrs == null) {
            return;
        }

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(CrudScannerConfigurer.class);

        String[] values = (String[]) enableCrudAttrs.get("value");

        if (ArrayUtils.isNotEmpty(values) && StringUtils.isNotEmpty(values[0])) {

            // TODO : 支持多个包
            builder.addPropertyValue("basePackage", values[0]);
        } else {

            Class<?> cls = ((StandardAnnotationMetadata) annotationMetadata).getIntrospectedClass();
            builder.addPropertyValue("basePackage", cls.getPackage().getName());

        }

        String beanName = generateBaseBeanName(annotationMetadata, 0);

        beanDefinitionRegistry.registerBeanDefinition(beanName, builder.getBeanDefinition());
    }


    private static String generateBaseBeanName(AnnotationMetadata importingClassMetadata, int index) {

        // note : 抄 mybatis-MapperScannerRegistrar
        return importingClassMetadata.getClassName() + "#" + EnableCrud.class.getSimpleName() + "#" + index;
    }
}
