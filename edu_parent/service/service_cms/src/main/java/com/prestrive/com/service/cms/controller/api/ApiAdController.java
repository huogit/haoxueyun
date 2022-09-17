package com.prestrive.com.service.cms.controller.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.prestrive.com.common.base.result.R;
import com.prestrive.com.service.cms.entity.Ad;
import com.prestrive.com.service.cms.service.AdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @Cacheable(value = "xxx", key = "'xxx'")：标注在方法上，对方法返回结果进行缓存。
 * 下次请求时，如果缓存存在，则直接读取缓存数据返回；如果缓存不存在，则执行方法，
 * 并把返回的结果存入缓存中。一般用在查询方法上。
 */

@RestController
@Api(description = "广告推荐")
@RequestMapping("/api/cms/ad")
//@CrossOrigin
public class ApiAdController {

    @Autowired
    AdService adService;


    @ApiOperation("根据推荐位id显示广告推荐")
    @GetMapping("/list")
    public R listByAdTypeId(){
        List<Ad> adList = adService.findAdListByAdTypeId();

        return R.ok().data("items",adList);
    }



}
