package com.prestrive.com.service.cms.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.prestrive.com.service.cms.entity.Ad;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.prestrive.com.service.cms.entity.vo.AdVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 广告推荐 Mapper 接口
 * </p>
 *
 * @author strive
 * @since 2022-06-24
 */
@Repository
@Mapper
public interface AdMapper extends BaseMapper<Ad> {

    List<AdVo> findAdVoListByPage(
            Page<AdVo> adVoPage,
            //自动装配条件
            @Param(Constants.WRAPPER) QueryWrapper<AdVo> adVoQueryWrapper);
}
