package com.prestrive.com.service.cms.service.impl;

 import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.prestrive.com.common.base.result.R;
import com.prestrive.com.service.cms.entity.Ad;
 import com.prestrive.com.service.cms.entity.AdType;
 import com.prestrive.com.service.cms.entity.vo.AdVo;
import com.prestrive.com.service.cms.feign.OssFileService;
import com.prestrive.com.service.cms.mapper.AdMapper;
import com.prestrive.com.service.cms.service.AdService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
 import com.prestrive.com.service.cms.service.AdTypeService;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.cache.annotation.Cacheable;
 import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

 import java.util.ArrayList;
 import java.util.List;

/**
 * <p>
 * 广告推荐 服务实现类
 * </p>
 *
 * @author strive
 * @since 2022-06-24
 */
@Service
public class AdServiceImpl extends ServiceImpl<AdMapper, Ad> implements AdService {

    @Autowired
    AdMapper adMapper;
    @Autowired
    OssFileService ossFileService;

    @Autowired
    AdTypeService adTypeService;

    @Override
    public boolean removeAdImageById(String id) {
        Ad ad = adMapper.selectById(id);
        String imageUrl = ad.getImageUrl();

        if(StringUtils.hasLength(imageUrl)){
            //删除图片
            R r = ossFileService.removeFile(imageUrl);
            return r.getSuccess();
        }
        return false;
    }

    @Override
    public IPage<AdVo> SelectPage(Long page, Long limit) {
        //分页条件
        Page<AdVo> adVoPage = new Page<>(page, limit);

        //排序条件
        QueryWrapper<AdVo> adVoQueryWrapper = new QueryWrapper<>();
        adVoQueryWrapper.orderByAsc("ad.type_id", "ad.sort");

        List<AdVo> adVoList =  adMapper.findAdVoListByPage(adVoPage,adVoQueryWrapper);

        adVoPage.setRecords(adVoList);

        return adVoPage;
    }


    @Override
    public List<Ad> findAdListByAdTypeId() {

        //查找可展示的推荐类别
        List<AdType> list = adTypeService.list(new QueryWrapper<AdType>().eq("is_show", 1).select("id"));

        ArrayList<String> ids = new ArrayList<>();
        for (AdType adType : list) {
            ids.add(adType.getId());
        }

        QueryWrapper<Ad> adQueryWrapper = new QueryWrapper<>();

        adQueryWrapper.in("type_id",ids);
        adQueryWrapper.orderByAsc("sort","id");

        return adMapper.selectList(adQueryWrapper);
    }
}
