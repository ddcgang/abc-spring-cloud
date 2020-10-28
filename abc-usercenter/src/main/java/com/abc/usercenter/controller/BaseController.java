package com.abc.usercenter.controller;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseController {
    @Autowired
    public HttpServletResponse response;
    @Autowired
    public HttpServletRequest request;
}
