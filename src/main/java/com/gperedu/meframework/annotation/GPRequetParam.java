package com.gperedu.meframework.annotation;

import java.lang.annotation.*;

/**
 * Created by 召君王 on 2019/3/29.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GPRequetParam {

    String value() default "";
}


