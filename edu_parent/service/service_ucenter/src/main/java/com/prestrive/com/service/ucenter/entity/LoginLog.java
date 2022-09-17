package com.prestrive.com.service.ucenter.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.prestrive.com.service.base.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 登录日志
 * </p>
 *
 * @author strive
 * @since 2022-07-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ucenter_login_log")
@ApiModel(value="LoginLog对象", description="登录日志")
public class LoginLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "登录的ip地址")
    private String ip;

    @ApiModelProperty(value = "登录次数")
    private Integer loginNum;

    @ApiModelProperty(value = "会员id")
    private String memberId;


}
