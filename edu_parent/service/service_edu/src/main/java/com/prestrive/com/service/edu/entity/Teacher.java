package com.prestrive.com.service.edu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.prestrive.com.service.base.model.BaseEntity;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 讲师
 * </p>
 *
 * @author strive
 * @since 2022-04-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("edu_teacher")
@ApiModel(value="Teacher对象", description="讲师")
public class Teacher extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "讲师姓名",example = "雪之下雪乃")
    private String name;

    @ApiModelProperty(value = "讲师简介",example = "日本大学法学院副教授")
    private String intro;

    @ApiModelProperty(value = "讲师资历,一句话说明讲师",example = "讲师资历,一句话说明讲师")
    private String career;

    @ApiModelProperty(value = "头衔 1高级讲师 2首席讲师",example = "1")
    private Integer level;

    @ApiModelProperty(value = "讲师头像",example = "beijing.aliyuncs.com/avatar/2019/11/25/03.jpg")
    private String avatar;

    @ApiModelProperty(value = "排序",example = "10")
    private Integer sort;

    @ApiModelProperty(value = "入驻时间",example = "2019-10-29")
    //局部自定义时间格式,返回时可以转换成 yyyy-MM-dd
    // swagger传过来的 yyyy-MM-dd 也可以转换
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date joinDate;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableField("is_deleted")
    @TableLogic
    private Boolean deleted;


}
