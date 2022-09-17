package com.prestrive.com.service.trade.controller.api;

import com.prestrive.com.common.base.result.R;
import com.prestrive.com.common.base.result.ResultCodeEnum;
import com.prestrive.com.common.base.util.JwtInfo;
import com.prestrive.com.common.base.util.JwtUtils;
import com.prestrive.com.service.base.exception.GlobalException;
import com.prestrive.com.service.trade.entity.Order;
import com.prestrive.com.service.trade.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api("网站订单管理")
@RestController
//@CrossOrigin
@RequestMapping("/api/trade/order")
@Slf4j
public class ApiOrderController {

    @Autowired
    OrderService orderService;

    @ApiOperation("新增订单")
    @PostMapping("/auth/save/{courseId}")
    public R save(@ApiParam(value = "课程id",required = true)
                  @PathVariable String courseId , HttpServletRequest request){

        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        if(jwtInfo == null){
            throw new GlobalException(ResultCodeEnum.PARAM_ERROR);
        }
        String memberId = jwtInfo.getId();
        String orderId = orderService.saveOrder(courseId,memberId);

        return R.ok().data("orderId",orderId);
    }

    @ApiOperation("获取订单")
    @GetMapping("auth/get/{orderId}")
    public R get(@PathVariable String orderId, HttpServletRequest request) {
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        if(jwtInfo == null){
            throw new GlobalException(ResultCodeEnum.PARAM_ERROR);
        }
        Order order = orderService.getByOrderId(orderId, jwtInfo.getId());

        return R.ok().data("item",order);
    }

    @ApiOperation("判断课程是否购买")
    @GetMapping("/auth/is-buy/{courseId}")
    public R isBuyByCourseId(@PathVariable String courseId, HttpServletRequest request){
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        if(jwtInfo == null){
            throw new GlobalException(ResultCodeEnum.PARAM_ERROR);
        }
        Boolean isBuy = orderService.isBuyByCourseId(courseId, jwtInfo.getId());
        return R.ok().data("isBuy", isBuy);
    }

    @ApiOperation(value = "获取当前用户订单列表")
    @GetMapping("/auth/list")
    public R list(HttpServletRequest request){
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        if(jwtInfo == null){
            throw new GlobalException(ResultCodeEnum.PARAM_ERROR);
        }
        List<Order> orderList = orderService.findListByMemberId(jwtInfo.getId());
        return R.ok().data("items", orderList);
    }

    @ApiOperation("删除订单")
    @DeleteMapping("/auth/remove/{orderId}")
    public R remove(@PathVariable String orderId, HttpServletRequest request) {
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        if(jwtInfo == null){
            throw new GlobalException(ResultCodeEnum.PARAM_ERROR);
        }
        boolean result = orderService.removeById(orderId, jwtInfo.getId());
        if (result) {
            return R.ok().message("删除成功");
        }else{
            return R.error().message("数据不存在");
        }
    }

    @GetMapping("/query-pay-status/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo){
        boolean result = orderService.queryPayStatus(orderNo);
        if (result) {
            return R.ok().message("支付成功");
        }else{
            return R.result(ResultCodeEnum.PAY_RUN);//支付中
        }
    }

    public static void main(String[] args) {
        int i = 2;


        if(i != 1){
            System.out.printf("1");
            return;
        }else if ( i == 2 ){
            System.out.printf("2");
            return;
        }else{
            System.out.println("3");
        }
    }
}
