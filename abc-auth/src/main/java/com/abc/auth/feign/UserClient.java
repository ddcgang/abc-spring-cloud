package com.abc.auth.feign;

import com.abc.auth.feign.impl.UserClientImpl;
import com.abc.common.util.ResultUtil;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Component
@FeignClient(value = "abc-usercenter", fallback = UserClientImpl.class)
public interface UserClient {

    @GetMapping("/user/find/{userName}")
    public String findByUserName(@PathVariable("userName") String userName);

    @GetMapping("/user/list")
    public ResultUtil findList();


}
