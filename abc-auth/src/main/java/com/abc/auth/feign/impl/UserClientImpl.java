package com.abc.auth.feign.impl;

import com.abc.auth.feign.UserClient;
import com.abc.common.util.ResultUtil;
import org.springframework.stereotype.Component;

@Component
public class UserClientImpl implements UserClient {
    @Override
    public ResultUtil findList() {
        return new ResultUtil("微服务定制");
    }

    @Override
    public String findByUserName(String userName) {
        return "进入熔断器";
    }
}
