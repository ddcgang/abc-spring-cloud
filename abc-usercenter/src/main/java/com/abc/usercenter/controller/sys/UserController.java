package com.abc.usercenter.controller.sys;

import com.abc.common.util.ResultUtil;
import com.abc.usercenter.controller.BaseController;
import com.abc.usercenter.model.sys.User;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RequestMapping("/user")
@RestController
public class UserController extends BaseController {

    @GetMapping("/find/{userName}")
    public String findByUserName(@PathVariable("userName") String userName) {
        System.out.println("-----------" + request.getHeader("Authorization"));
        return "usercenter->" + userName;
    }

    @GetMapping("/list")
    public ResultUtil findList() {
        User user = new User();
        user.setCreateTime(new Date());
        user.setId(1000);
        user.setUserName("admin");
        user.setPassWord("123456");
        return new ResultUtil(user);
    }

}
