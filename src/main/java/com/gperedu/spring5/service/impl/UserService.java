package com.gperedu.spring5.service.impl;

import com.gperedu.spring5.annotation.GPService;
import com.gperedu.spring5.service.IUserService;

/**
 * @Description TODO
 * @Date 2019/3/27 19:31
 * @Created by rogan.luo
 */
@GPService
public class UserService implements IUserService {


    @Override
    public String get(String name) {
        return "My name is" + name;
    }
}
