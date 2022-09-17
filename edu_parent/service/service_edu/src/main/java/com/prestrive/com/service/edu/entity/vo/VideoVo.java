package com.prestrive.com.service.edu.entity.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class VideoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String title;
    private Boolean free;
    //排序字段
    private Integer sort;

    private String videoSourceId;
}
