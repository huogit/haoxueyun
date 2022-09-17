package com.prestrive.com.service.edu.entity.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class SubjectVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String title;

    private String slot;
    //二级分类
    private List<SubjectVo>  children = new ArrayList<>();;

}
