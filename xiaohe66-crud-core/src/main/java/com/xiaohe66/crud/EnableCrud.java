package com.xiaohe66.crud;

import com.xiaohe66.crud.register.EnableCrudRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xiaohe
 * @since 2021.09.26 10:51
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({EnableCrudRegister.class, CrudAutoConfiguration.class})
public @interface EnableCrud {

    String[] value() default "";
}
