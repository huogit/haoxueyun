package com.prestrive.com.service.acl.mapper;

import com.prestrive.com.service.acl.entity.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 权限 Mapper 接口
 * </p>
 *
 * @author strive
 * @since 2022-09-03
 */
@Repository
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {


    List<String> selectPermissionValueByUserId(String userId);

    List<String> selectAllPermissionValue();

    List<Permission> selectPermissionByUserId(String userId);
}
