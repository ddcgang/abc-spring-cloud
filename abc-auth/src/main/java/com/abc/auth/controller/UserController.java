package com.abc.auth.controller;

import com.abc.auth.feign.UserClient;
import com.abc.common.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
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
}
