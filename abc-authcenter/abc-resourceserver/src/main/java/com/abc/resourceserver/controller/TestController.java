package com.abc.resourceserver.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/id/{id}")
    public String findById(@PathVariable("id") String id){
        return id;
    }
}
