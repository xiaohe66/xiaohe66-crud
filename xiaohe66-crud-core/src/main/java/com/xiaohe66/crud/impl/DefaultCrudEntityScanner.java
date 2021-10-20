package com.xiaohe66.crud.impl;

import com.xiaohe66.crud.register.scan.Crud;
import com.xiaohe66.crud.register.scan.CrudEntityWrapper;
import com.xiaohe66.crud.register.scan.ICrudEntityScanner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author xiaohe
 * @since 2021.09.26 10:49
 */
@Slf4j
public class DefaultCrudEntityScanner implements ICrudEntityScanner {

    /*
        TODO : 这两个类考虑使用 spring 中的，但目前不知获取方法
     */
    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    private CachingMetadataReaderFactory readerFactory = new CachingMetadataReaderFactory();

    @Override
    public List<CrudEntityWrapper> scan(String basePackage) {

        String replace = basePackage.replace(".", "/");

        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + replace + "/**/*.class";

        Resource[] resources;
        try {
            resources = resourcePatternResolver.getResources(packageSearchPath);

        } catch (IOException e) {
            throw new IllegalStateException("cannot get crud class", e);
        }

        ArrayList<CrudEntityWrapper> entityWrapperList = new ArrayList<>();

        for (Resource resource : resources) {

            Class<?> entityCls;
            try {
                MetadataReader metadataReader = readerFactory.getMetadataReader(resource);
                ClassMetadata classMetadata = metadataReader.getClassMetadata();
                entityCls = Class.forName(classMetadata.getClassName());

            } catch (IOException e) {
                log.error("cannot getMetadataReader : {}", resource.getFilename(), e);
                continue;

            } catch (ClassNotFoundException e) {
                log.error("cannot forClass : {}", resource.getFilename(), e);
                continue;
            }

            Crud crud = entityCls.getAnnotation(Crud.class);
            if (crud != null) {

                Set<String> fieldNames = new HashSet<>();

                Field[] fields = entityCls.getDeclaredFields();
                for (Field field : fields) {
                    fieldNames.add(field.getName());
                }


                CrudEntityWrapper entityWrapper = new CrudEntityWrapper();
                entityWrapper.setCls(entityCls);

                String tableName = crud.tableName();

                if (StringUtils.isNotEmpty(crud.value())) {

                    entityWrapper.setName(crud.value());
                    if (StringUtils.isEmpty(tableName)) {

                        tableName = crud.value();
                    }

                } else {

                    // 首字母小写
                    char[] chars = entityCls.getSimpleName().toCharArray();
                    if (chars[0] >= 'A' && chars[0] <= 'Z') {
                        chars[0] += 32;
                    }
                    String name = new String(chars);

                    entityWrapper.setName(name);

                    if (StringUtils.isEmpty(tableName)) {
                        tableName = name;
                    }
                }
                entityWrapper.setTableName(tableName);
                entityWrapper.setFieldNames(fieldNames);

                entityWrapperList.add(entityWrapper);
            }
        }

        return entityWrapperList;
    }
}
