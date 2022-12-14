package com.prestrive.com.service.edu.entity.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class ChapterVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String title;
    //ζεΊ
    private Integer sort;

    //θ
    private List<VideoVo> children = new ArrayList<>();
}
