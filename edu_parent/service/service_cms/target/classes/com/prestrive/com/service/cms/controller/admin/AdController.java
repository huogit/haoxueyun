package com.prestrive.com.service.cms.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.prestrive.com.common.base.result.R;
import com.prestrive.com.service.cms.entity.Ad;
import com.prestrive.com.service.cms.entity.AdType;
import com.prestrive.com.service.cms.entity.vo.AdVo;
import com.prestrive.com.service.cms.service.AdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 广告推荐 前端控制器
 * </p>
 *
 * @author strive
 * @since 2022-06-24
 */
@RestController
@RequestMapping("/admin/cms/ad")
//@CrossOrigin
@Api(description = "广告推荐管理")
public class AdController {

    @Autowired
    AdService adService;

    @Autowired
    RedisTemplate redisTemplate;

    @ApiOperation("redis保存")
    @GetMapping("save-test")
    public R saveTest(){
        return R.ok();
    }


    @ApiOperation("广告推荐列表")
    @GetMapping("/list")
    public R list(){
        List<Ad> list = adService.list();

        return R.ok().data("items",list);
    }

    @ApiOperation("根据id获取广告推荐信息")
    @GetMapping("/get/{id}")
    public R get(@ApiParam(value = "广告id",required = true) @PathVariable String id){

        Ad ad = adService.getById(id);

        return R.ok().data("item",ad);
    }

    @ApiOperation("根据id删除广告")
    @DeleteMapping("/remove/{id}")
    public R removeById(@ApiParam(value = "广告id",required = true) @PathVariable String id){

        //删除图片
        adService.removeAdImageById(id);

        boolean b = adService.removeById(id);

        if(b){
            return R.ok().message("删除成功");
        }else{
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("新增广告")
    @PostMapping("/save")
    public R save(@ApiParam(value = "广告推荐对象表单",required = true) @RequestBody Ad ad){

        boolean save = adService.save(ad);

        if (save) {
            return R.ok().message("新增成功");
        }else{
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("更新广告")
    @PutMapping("/update")
    public R update(@ApiParam(value = "广告推荐对象表单",required = true) @RequestBody Ad ad){

        boolean save = adService.updateById(ad);

        if (save) {
            return R.ok().message("更新成功");
        }else{
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("广告分页列表")
    @GetMapping("/list/{page}/{limit}")
    public R pageList(@ApiParam(value = "分页页码",required = true) @PathVariable Long page,
                      @ApiParam(value = "每页显示个数",required = true) @PathVariable Long limit){


        IPage<AdVo> pageModel = adService.SelectPage(page,limit);

        List<AdVo> adTypeList = pageModel.getRecords();
        long total = pageModel.getTotal();

        return R.ok().data("items",adTypeList).data("total",total);
    }
}

