package com.maple.fastweb.util.validator;

import com.maple.fastweb.util.SpringContextUtil;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by zsj on 2017/9/27.
 * 校验帮助类，把校验信息拼接返回
 */
public class ValidateUtil {

    public static String validate(Object obj){
        StringBuffer buffer = new StringBuffer(64);//用于存储验证后的错误信息

        Validator validator = (Validator) SpringContextUtil.getBeanById("validator");

        Set<ConstraintViolation<Object>> constraintViolations = validator
                .validate(obj);//验证某个对象,，其实也可以只验证其中的某一个属性的
        Iterator<ConstraintViolation<Object>> iter = constraintViolations
                .iterator();
        while (iter.hasNext()) {
            ConstraintViolation<Object> con = iter.next();
            String message = "--"+con.getPropertyPath() +" : "+con.getMessage();
            buffer.append(message);
        }
        return buffer.toString();
    }
}
