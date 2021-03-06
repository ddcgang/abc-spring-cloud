package com.abc.resourceserver.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    //https://blog.csdn.net/csdnmuyi/article/details/107382461
    @GetMapping("/admin/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String findById(@PathVariable("id") String id, Authentication authentication){

        return id;
    }

    @GetMapping("/user/{name}")
    @PreAuthorize("hasPermission('/product/list','product:list')")
    public String findByName(@PathVariable("name") String name, Authentication authentication){
        System.out.println(authentication.getAuthorities().toString());
        return name;
    }
    @GetMapping("/list")
    //@PreAuthorize("hasAuthority('abc-usercenter:test:list')")
    public String list(Authentication authentication){
        System.out.println(authentication.getAuthorities().toString());
        return "list";
    }
}
