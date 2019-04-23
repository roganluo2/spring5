package com.gperedu.meframework.controller;

import com.gperedu.meframework.annotation.GPAutowired;
import com.gperedu.meframework.annotation.GPController;
import com.gperedu.meframework.annotation.GPRequestMapping;
import com.gperedu.meframework.annotation.GPRequetParam;
import com.gperedu.meframework.service.IUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description TODO
 * @Date 2019/3/27 19:31
 * @Created by rogan.luo
 */
@GPController
@GPRequestMapping("/user")
public class UserController {

    @GPAutowired
    private IUserService userService;

    @GPRequestMapping("/query")
    public void query(HttpServletRequest request, HttpServletResponse response, @GPRequetParam("name") String name)
    {
        String result = userService.get(name);

        try {
            response.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void add(HttpServletRequest req, HttpServletResponse res, @GPRequetParam("a") Integer a, @GPRequetParam("b") Integer b)
    {
        try {
            res.getWriter().write( a+"+"+"b" +"=" + (a + b));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
