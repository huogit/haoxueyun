package com.prestrive.com.service.acl.entity;

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

import java.util.List;

/**
 * <p>
 * 权限
 * </p>
 *
 * @author strive
 * @since 2022-09-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("acl_permission")
@ApiModel(value="Permission对象", description="权限")
public class Permission extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "所属上级")
    private String pid;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "类型(1:菜单,2:按钮)")
    private Integer type;

    @ApiModelProperty(value = "权限值")
    private String permissionValue;

    @ApiModelProperty(value = "访问路径")
    private String path;

    @ApiModelProperty(value = "路由名字")
    private String routeName;

    @ApiModelProperty(value = "组件路径")
    private String component;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "状态(0:禁止,1:正常)")
    private Integer status;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableField("is_deleted")
    @TableLogic
    private Boolean deleted;

    @ApiModelProperty(value = "层级")
    @TableField(exist = false)
    private Integer level;

    @ApiModelProperty(value = "下级")
    @TableField(exist = false)
    private List<Permission> children;

    @ApiModelProperty(value = "是否被选中")
    @TableField(exist = false)
    private boolean isSelect;


}
