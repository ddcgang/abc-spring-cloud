package com.abc.usercenter.dao.sys;

import com.abc.usercenter.model.sys.User;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface UserMapper extends Mapper<User> {
}
