package com.prestrive.com.service.base.handler;

import com.prestrive.com.common.base.result.R;
import com.prestrive.com.common.base.result.ResultCodeEnum;
import com.prestrive.com.common.base.util.ExceptionUtils;
import com.prestrive.com.service.base.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 通用异常：未知异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public R exception(Exception e){
        //e.printStackTrace(); 打印出来太难看
        log.error(ExceptionUtils.getMessage(e));
        return R.error().message(e.getMessage());
    }

    /**
     * 特定异常：sql语法异常
     * @return
     */
    @ExceptionHandler(BadSqlGrammarException.class)
    public R exception(BadSqlGrammarException e){
        log.error(ExceptionUtils.getMessage(e));
        return R.result(ResultCodeEnum.BAD_SQL_GRAMMAR);
    }

    /**
     * 特定异常：json解析异常
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public R exception(HttpMessageNotReadableException e){
        log.error(ExceptionUtils.getMessage(e));
        return R.result(ResultCodeEnum.JSON_PARSE_ERROR);
    }

    /**
     * 自定义异常：比如 oss配置文件错误
     * 业务中需要的位置用 try catch 抛出 GlobalException
     * @return
     */
    @ExceptionHandler(GlobalException.class)
    public R exception(GlobalException e){
        log.error(ExceptionUtils.getMessage(e));
        return R.error().message(e.getMessage()).code(e.getCode());
    }
}
