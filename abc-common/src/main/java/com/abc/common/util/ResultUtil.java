package com.abc.common.util;

import lombok.Data;



@Data
public class ResultUtil {
    private Integer code;
    private String message;
    private Object result;

    public ResultUtil() {
        this.setCode(1);
        this.setMessage("success");
    }

    public ResultUtil(Integer code, String message) {
        this.setCode(code);
        this.setMessage(message);
    }

    public ResultUtil(Object result) {
        this.setCode(1);
        this.setMessage("success");
        this.setResult(result);
    }

    public ResultUtil(Integer code, String message, Object result) {
        this.setCode(code);
        this.setMessage(message);
        this.setResult(result);
    }


}
