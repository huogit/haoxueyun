package com.prestrive.com.service.edu.entity.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CourseQueryVo implements Serializable {

    private String title;

    private String teacherId;

    private String subjectParentId;

    private String subjectId;
}
