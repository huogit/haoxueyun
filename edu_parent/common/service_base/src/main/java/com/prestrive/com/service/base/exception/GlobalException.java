package com.prestrive.com.service.base.exception;

import com.prestrive.com.common.base.result.ResultCodeEnum;
import jdk.nashorn.internal.objects.Global;
import lombok.Data;

/**
 * 自定义异常类
 */
//RuntimeException 对代码没有侵入性
@Data
public class GlobalException extends RuntimeException {

    //状态码
    private Integer code;

    /**
     * 接收枚举类型
     * @param resultCodeEnum
     */
    public GlobalException(ResultCodeEnum resultCodeEnum){
        super(resultCodeEnum.getMsg());
        this.code = resultCodeEnum.getCode();
    }

    /**
     * 接受状态码和消息
     * @param message
     * @param code
     */
    public GlobalException(String message, Integer code){
        super(message);
        this.code = code;
    }

    /**
     * 打印完整的异常信息
     * @return
     */
    @Override
    public String toString() {
        return "GuliException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}
