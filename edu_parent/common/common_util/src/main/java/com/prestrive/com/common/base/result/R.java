package com.prestrive.com.common.base.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;

@Data
public class R {
    @ApiModelProperty(value = "是否成功")
    private Boolean success;
    private Integer code;
    private String message;
    private HashMap<String,Object> data = new HashMap<>();

    /**
     * 使用：R.ok().Data(Data):链式编程
     * @return
     */
    public static R ok(){
        R r = new R();
        r.setSuccess(ResultCodeEnum.SUCCESS.isSuccess());
        r.setCode(ResultCodeEnum.SUCCESS.getCode());
        r.setMessage(ResultCodeEnum.SUCCESS.getMsg());
        return r;
    }

    public static R error(){
        R r = new R();
        r.setSuccess(ResultCodeEnum.UNKNOWN_REASON.isSuccess());
        r.setCode(ResultCodeEnum.UNKNOWN_REASON.getCode());
        r.setMessage(ResultCodeEnum.UNKNOWN_REASON.getMsg());
        return r;
    }

    public static R result(ResultCodeEnum resultCodeEnum){
        R r = new R();
        r.setSuccess(resultCodeEnum.isSuccess());
        r.setCode(resultCodeEnum.getCode());
        r.setMessage(resultCodeEnum.getMsg());
        return r;
    }

    public R success(Boolean success){
       this.setSuccess(success);
        return this;
    }

    public R message(String msg){
        this.setMessage(msg);
        return this;
    }

    public R code(Integer code) {
        this.code = code;
        return this;
    }

    public R data(HashMap<String, Object> data) {
        this.data = data;
        return this;
    }

    public R data(String key,Object value) {
        this.data.put(key,value);
        return this;
    }
}
