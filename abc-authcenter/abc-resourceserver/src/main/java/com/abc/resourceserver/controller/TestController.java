package com.abc.resourceserver.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    //https://blog.csdn.net/csdnmuyi/article/details/107382461
    @GetMapping("/id/{id}")
    public String findById(@PathVariable("id") String id){
        return id;
    }
}
