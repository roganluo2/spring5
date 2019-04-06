package com.gperedu.spring5.annotation;

import java.lang.annotation.*;

/**
 * Created by 召君王 on 2019/3/29.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GPService {
    String value() default "";
}