package com.prestrive.com.common.base.result;


import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public enum ResultCodeEnum {

    //get处理器，用于取值
    SUCCESS(true,20000,"成功"), //他规定是20000，不然其他的都认为是失败
    UNKNOWN_REASON(false, 20001, "未知错误"),
    PARAM_ERROR(false, 21003, "参数不正确"),
    BAD_SQL_GRAMMAR(false, 21001, "sql语法错误"),
    JSON_PARSE_ERROR(false, 21002, "json解析异常"),

    FILE_UPLOAD_ERROR(false, 21004, "文件上传错误"),
//    FILE_DELETE_ERROR(false, 21005, "文件刪除错误"),
    EXCEL_DATA_IMPORT_ERROR(false, 21006, "Excel数据导入错误"),
    PARAMETER_NULL_ERROR(false,21007,"参数不能为空"),

//
    VIDEO_UPLOAD_ALIYUN_ERROR(false, 22001, "视频上传至阿里云失败"),
    VIDEO_UPLOAD_TOMCAT_ERROR(false, 22002, "视频上传至业务服务器失败"),
    VIDEO_DELETE_ALIYUN_ERROR(false, 22003, "阿里云视频文件删除失败"),
//    FETCH_VIDEO_UPLOADAUTH_ERROR(false, 22004, "获取上传地址和凭证失败"),
//    REFRESH_VIDEO_UPLOADAUTH_ERROR(false, 22005, "刷新上传地址和凭证失败"),
    FETCH_PLAYAUTH_ERROR(false, 22006, "获取播放凭证失败"),
//
    URL_ENCODE_ERROR(false, 23001, "URL编码失败"),
    ILLEGAL_CALLBACK_REQUEST_ERROR(false, 23002, "非法回调请求"),
    FETCH_ACCESSTOKEN_FAILD(false, 23003, "获取accessToken失败"),
    FETCH_USERINFO_ERROR(false, 23004, "获取用户信息失败"),
    LOGIN_ERROR(false, 23005, "登录失败"),
    NO_LOGIN_ERROR(false,23006,"未登录"),
//
//    COMMENT_EMPTY(false, 24006, "评论内容必须填写"),
//
    PAY_RUN(false, 25000, "支付中"),
    PAY_UNIFIEDORDER_ERROR(false, 25001, "统一下单错误"),
//    PAY_ORDERQUERY_ERROR(false, 25002, "查询支付结果错误"),
//
//    ORDER_EXIST_ERROR(false, 25003, "课程已购买"),
//
//    GATEWAY_ERROR(false, 26000, "服务不能访问"),
//
    CODE_ERROR(false, 28000, "验证码错误"),
//
    LOGIN_PHONE_ERROR(false, 28009, "手机号码不正确"),
    LOGIN_MOBILE_ERROR(false, 28001, "账号不正确"),
    LOGIN_PASSWORD_ERROR(false, 28008, "密码不正确"),
//    LOGIN_DISABLED_ERROR(false, 28002, "该用户已被禁用"),
    MOBILE_REGISTER_ERROR(false, 28003, "手机号已被注册"),
    LOGIN_AUTH(false, 28004, "需要登录"),
//    LOGIN_ACL(false, 28005, "没有权限"),
    SMS_SEND_ERROR(false, 28006, "短信发送失败"),
    SMS_SEND_ERROR_BUSINESS_LIMIT_CONTROL(false, 28007, "短信发送过于频繁"),
//    MY_VALUE(false, 30000, "我的错误");

    RECORD_EXISTS_ERROR(false,28008,"该记录已存在")

    ;

    private boolean success;
    private Integer code;
    private String msg;

    //构造函数
    ResultCodeEnum(boolean success,Integer code,String msg){
        this.code = code;
        this.success = success;
        this.msg  =msg;
    }
}
