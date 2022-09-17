package com.prestrive.com.service.ucenter.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.prestrive.com.service.base.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 课程简介
 * </p>
 *
 * @author strive
 * @since 2022-09-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ucenter_message_description")
@ApiModel(value="MessageDescription对象", description="课程简介")
public class MessageDescription extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "课程简介")
    private String description;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableField("is_deleted")
    @TableLogic
    private Boolean deleted;


}
