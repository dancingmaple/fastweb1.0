package com.maple.fastweb.util.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 自定义校验注解方式（字符串中不能包含空格）
 * CannotContainSpacesValidator.class 是具体的校验实现类
 */

@Constraint(validatedBy = CannotContainSpacesValidator.class) //具体的实现
@Target( { java.lang.annotation.ElementType.METHOD,
        java.lang.annotation.ElementType.FIELD })
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
public @interface CannotContainSpaces {
    String message() default "{Cannot.contain.Spaces}"; //提示信息,可以写死,可以填写国际化的key

    int length() default 5;

    //下面这两个属性必须添加
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}