package com.abc.auth.controller;

import com.abc.auth.feign.UserClient;
import com.abc.common.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController extends BaseController {

    @Autowired
    UserClient userClient;

    @GetMapping("/list")
    public ResultUtil name() {
        return new ResultUtil(userClient.findList());
    }

    @GetMapping("/find/{userName}")
    public String findByName(@PathVariable("userName") String userName) {
        System.out.println(userName);
        return "auth->" + userClient.findByUserName(userName);
    }

    @GetMapping("/admin/hello")
    public String admin() {
        return "hello admin";
    }

    @GetMapping("/user/hello")
    public String user() {
        return "hello user";
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}
