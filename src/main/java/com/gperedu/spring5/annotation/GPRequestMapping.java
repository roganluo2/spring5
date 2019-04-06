package com.gperedu.spring5.annotation;

import java.lang.annotation.*;

/**
 * Created by 召君王 on 2019/3/30.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GPRequestMapping {

    String value() default "";

}