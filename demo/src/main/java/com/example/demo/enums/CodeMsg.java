package com.example.demo.enums;

/**
 * @author qzz
 * @date 2024/1/12
 */
public enum CodeMsg {

    SUCCESS(200,"成功"),
    ERROR(500,"服务器异常"),
    ACCESS_LIMIT_REACHED(403,"已达到访问限制");

    private int code;

    private String msg;

    CodeMsg(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
