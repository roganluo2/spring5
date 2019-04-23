package com.gperedu.meframework.v3;

import com.gperedu.meframework.annotation.GPAutowired;
import com.gperedu.meframework.annotation.GPController;
import com.gperedu.meframework.annotation.GPRequestMapping;
import com.gperedu.meframework.annotation.GPService;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 召君王 on 2019/3/29.
 */
public class GPDispatcherServlet extends HttpServlet {

    private Properties contextConfig = new Properties();

    private List<String> classNames = new ArrayList<>();

    private Map<String,Object> ioc = new HashMap<>();

    private List<Handler> handlerMapping = new ArrayList<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
//        1.init config
        loadConfig(config.getInitParameter("contextConfigLocation"));
//        scan class
        doScanner(contextConfig.getProperty("basePackage"));

        doInstance();

        doAutowire();

        initHandleMapping();

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
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
        Handler handler = getHandler(req);
        if(null == handler)
        {
            resp.getWriter().write("404 Not Found!!!");
            return;
        }
        Class[] paramTypes = handler.getParamTypes();
        Object[] paramValues = new Object[paramTypes.length];
        Map<String, String[]> params = req.getParameterMap();
        for(Map.Entry<String,String[]> param : params.entrySet())
        {
            String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]", "").replace("\\s","");
            if(!handler.paramIndexMapping.containsKey(param.getKey())){return;}
            int index = handler.paramIndexMapping.get(param.getKey());
            paramValues[index] = convert(paramTypes[index], value);
        }

        if(handler.paramIndexMapping.containsKey(HttpServletRequest.class.getName()))
        {
            Integer reqIndex = handler.paramIndexMapping.get(HttpServletRequest.class.getName());
            paramValues[reqIndex] = req;
        }

        if(handler.paramIndexMapping.containsKey(HttpServletResponse.class.getName()))
        {
            int respIndex = handler.paramIndexMapping.get(HttpServletResponse.class.getName());
            paramValues[respIndex] = resp;
        }
        Object returnValue = handler.method.invoke(handler.controller, paramValues);
        if(returnValue == null || returnValue instanceof  Void) { return;}
        resp.getWriter().write(returnValue.toString());
    }

    private Handler getHandler(HttpServletRequest req) {
        if(handlerMapping.isEmpty())
        {
            return null;
        }
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replace(contextPath, "").replaceAll("/+", "/");
        for(Handler handler : handlerMapping)
        {
            try {
                Matcher matcher = handler.getPattern().matcher(url);
                if (!matcher.matches()) {
                    continue;
                }
                return handler;
            }
            catch (Exception e)
            {
                throw e;
            }
        }
        return null;
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
           Class<?> clazz = entry.getValue().getClass();
           if(!clazz.isAnnotationPresent(GPController.class))
           {
               continue;
           }
           String url = "";
           if(clazz.isAnnotationPresent(GPRequestMapping.class))
           {
               GPRequestMapping requestMapping = clazz.getAnnotation(GPRequestMapping.class);
               url = requestMapping.value();
           }
           Method[] methods = clazz.getMethods();
           for(Method method:methods)
           {
               if(!method.isAnnotationPresent(GPRequestMapping.class))
               {continue;}
               GPRequestMapping requestMapping = method.getAnnotation(GPRequestMapping.class);
               String regex = ("/" + url + requestMapping.value()).replaceAll("/+", "/");
               Pattern pattern = Pattern.compile(regex);
               handlerMapping.add(new Handler(entry.getValue(), method, pattern));
               System.out.println("mapping" + regex + "," + method);
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
//        InputStream is = this.getClass().getResourceAsStream(contextLocation);
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(contextLocation);
        try {
            contextConfig.load(is);
        } catch (IOException e) {
            e.printStackTrace();

        }
        finally {
            if(null != is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


}
