package com.prestrive.com.service.ucenter.entity.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class WebPasswordVo implements Serializable {

    private String oldPassword;
    private String newPassword;
}
