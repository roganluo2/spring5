package com.gperedu.spring5.v2.servlet;

import com.gperedu.spring5.annotation.*;
import com.sun.deploy.net.HttpResponse;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * Created by 召君王 on 2019/3/29.
 */
public class GPDispatcherServlet extends HttpServlet {

    private Properties contextConfig = new Properties();

    private List<String> classNames = new ArrayList<>();

    private Map<String,Object> ioc = new HashMap<>();

    private Map<String,Object> handlerMapping = new HashMap<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
//        1.init config
        loadConfig(config.getInitParameter("contextLocation"));
//        scan class
        doScanner(contextConfig.getProperty("basePackagePath"));

        doInstance();

        doAutowire();

        initHandleMapping();


    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doDispatcher(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doDispatcher(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replace(contextPath, "").replace("/+", "/");
        if(!handlerMapping.containsKey(url))
        {
            resp.getWriter().write("404 not found");
            return;
        }
        Method method = (Method) handlerMapping.get(url);
        Map<String, String[]> parameterMap = req.getParameterMap();

        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] paramValues = new Object[parameterTypes.length];

        for(int i = 0; i< parameterTypes.length ; i ++)
        {
            Class<?> parameterType = parameterTypes[i];
            if(parameterType.isAssignableFrom(HttpServletRequest.class))
            {
                paramValues[i] = req;
                continue;
            }
            if(HttpServletResponse.class.isAssignableFrom(parameterType))
            {
                paramValues[i] = resp;
                continue;
            }
            if(String.class.isAssignableFrom(parameterType))
            {
                GPRequetParam annotation = parameterType.getAnnotation(GPRequetParam.class);
                if(parameterMap.containsKey(annotation.value()))
                {
                    for(Map.Entry<String,String[]> param: parameterMap.entrySet()  )
                    {
                        String vlaue = Arrays.toString(param.getValue()).replaceAll("\\[|\\]","").replaceAll("\\s","");
                        paramValues[i] = vlaue;
                    }
                }
            }

        }
        String beanName = toLowerFirstCase(method.getDeclaringClass().getSimpleName());
        method.invoke(ioc.get(beanName), paramValues);

    }

    private Object convert(Class<?> type, String value)
    {
        if(Integer.class == type)
        {
            return Integer.valueOf(value);
        }
        return value;
    }

    //    url --> method
    private void initHandleMapping() {
        for(Map.Entry<String,Object> entry : ioc.entrySet())
        {
            Class<?> aClass = entry.getValue().getClass();
            if(!aClass.isAnnotationPresent(GPController.class))
            {
                continue;
            }
            String baseUrl = "";
            if(aClass.isAnnotationPresent(GPRequestMapping.class))
            {
                GPRequestMapping annotation = aClass.getAnnotation(GPRequestMapping.class);
                baseUrl = annotation.value();
            }
            for(Method method : aClass.getMethods())
            {
                if(!method.isAnnotationPresent(GPRequestMapping.class)){continue;}
                GPRequestMapping methodMapping = method.getAnnotation(GPRequestMapping.class);
                String url = "/" + baseUrl + "/" + methodMapping.value().replaceAll("/+", "/");
                handlerMapping.put(url,method);
            }
        }

    }



    private void doAutowire() {
        Set<Map.Entry<String, Object>> entries = ioc.entrySet();
        for(Map.Entry<String,Object> entry : entries)
        {
            Field[] declaredFields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : declaredFields)
            {
                if(!field.isAnnotationPresent(GPAutowired.class))
                {
                    continue;
                }
                GPAutowired autowired = field.getAnnotation(GPAutowired.class);
                String beanName = autowired.value().trim();
                if("".endsWith(beanName))
                {
                    beanName = field.getType().getName();
                }
                field.setAccessible(true);
                try {
                    field.set(entry.getValue(), ioc.get(beanName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    private void doInstance() {

        try {

            for(String className : classNames)
            {
                Class<?> aClass = Class.forName(className);
                if(aClass.isAnnotationPresent(GPController.class))
                {
                    Object o = aClass.newInstance();
                    String beanName = toLowerFirstCase(aClass.getSimpleName());
                    ioc.put(beanName, o);
                    continue;
                }
                if(aClass.isAnnotationPresent(GPService.class))
                {
                    GPService annotation = aClass.getAnnotation(GPService.class);
                    String beanName = annotation.value();
                    if("".equals(beanName.trim()))
                    {
                        beanName = toLowerFirstCase(aClass.getSimpleName());
                    }
                    Object o = aClass.newInstance();
                    ioc.put(beanName,o);
                    for(Class i : aClass.getInterfaces())
                    {
                        if(ioc.containsKey(i.getName()))
                        {
                            throw new RuntimeException("beanName" + i.getName() +" already exists");
                        }
                        ioc.put(i.getName(),o);
                    }
                }

            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private String toLowerFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] = (char) (chars[0] + 32);
        return String.valueOf(chars);
    }

    private void doScanner(String basePackagePath) {
        URL url = this.getClass().getClassLoader().getResource("/" + basePackagePath.replaceAll("\\.", "/"));
        File classPath = new File(url.getFile());
        for(File file : classPath.listFiles())
        {
            if(file.isDirectory())
            {
                doScanner(basePackagePath + "." + file.getName());
                continue;
            }
            if(!file.getName().endsWith(".class"))
            {
                continue;
            }
            String className = basePackagePath + "." + file.getName().replace(".class","");
            classNames.add(className);

        }

    }

    private void loadConfig(String contextLocation) {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(contextLocation);

        try {
            contextConfig.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
