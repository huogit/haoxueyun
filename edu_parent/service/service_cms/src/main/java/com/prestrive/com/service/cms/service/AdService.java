package com.prestrive.com.service.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.prestrive.com.service.cms.entity.Ad;
import com.baomidou.mybatisplus.extension.service.IService;
import com.prestrive.com.service.cms.entity.vo.AdVo;

import java.util.List;

/**
 * <p>
 * 广告推荐 服务类
 * </p>
 *
 * @author strive
 * @since 2022-06-24
 */
public interface AdService extends IService<Ad> {

    boolean  removeAdImageById(String id);

    IPage<AdVo> SelectPage(Long page, Long limit);

    List<Ad> findAdListByAdTypeId();
}
