package com.prestrive.com.service.edu.entity.vo;

import lombok.Data;
import sun.rmi.runtime.Log;

import java.math.BigDecimal;

@Data
public class WebCourseVo {

    private static final long serialVersionUID = 1L;

    private String id;
    private String title;
    private BigDecimal price;
    private Integer lessonNUm;
    private String cover;
    private Long buyCount;
    private Long viewCount;
    private String description;
    private String teacherId;
    private String teacherName;
    private String intro;
    private String avatar;
    private String subjectLevelOneId;
    private String subjectLevelOne;
    private String subjectLevelTwoId;
    private String subjectLevelTwo;
}
