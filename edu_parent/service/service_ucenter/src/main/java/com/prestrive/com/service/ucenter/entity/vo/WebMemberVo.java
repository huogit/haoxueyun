package com.prestrive.com.service.ucenter.entity.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class WebMemberVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String avatar;
    private String nickname;
    private Integer sex;
    private Integer age;
    private String sign;
}
