package com.gperedu.spring5.v3;

import com.gperedu.spring5.annotation.GPRequetParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by 召君王 on 2019/4/6.
 */
public class Handler {
    public Object controller;

    public Method method;

    public Pattern pattern;

    protected Map<String, Integer> paramIndexMapping;
    private Class[] paramTypes;

    public Handler(Object controller, Method method, Pattern pattern) {
        this.controller = controller;
        this.method = method;
        this.setPattern(pattern);
        paramIndexMapping = new HashMap<>();
        putParamIndexMapping(method);
    }

    private void putParamIndexMapping(Method method) {
        Annotation [][] pa = method.getParameterAnnotations();
        for(int i = 0; i < pa.length; i++)
        {
            for(Annotation a : pa[i])
            {
                if(a instanceof GPRequetParam)
                {
                    String paramName = ((GPRequetParam)a).value();
                    if(!"".equals(paramName.trim()))
                    {
                        paramIndexMapping.put(paramName, i);
                    }
                }
            }
        }

        Class<?>[] parameterTypes = method.getParameterTypes();
        for(int i = 0; i < parameterTypes.length; i++)
        {
            Class<?> type = parameterTypes[i];
            if(type == HttpServletRequest.class
                    || HttpServletResponse.class.isAssignableFrom(type))
            {
                paramIndexMapping.put(type.getName(), i);
            }

        }

    }
    public Pattern getPattern() {
        return pattern;
    }
    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }


    public Class[] getParamTypes() {
        return paramTypes;
    }
}
