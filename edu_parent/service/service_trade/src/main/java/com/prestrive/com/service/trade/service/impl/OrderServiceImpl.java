package com.prestrive.com.service.trade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.prestrive.com.common.base.result.ResultCodeEnum;
import com.prestrive.com.service.base.dto.CourseDto;
import com.prestrive.com.service.base.dto.MemberDto;
import com.prestrive.com.service.base.exception.GlobalException;
import com.prestrive.com.service.trade.entity.Order;

import com.prestrive.com.service.trade.entity.PayLog;
import com.prestrive.com.service.trade.feign.EduCourseService;
import com.prestrive.com.service.trade.feign.UcenterMemberService;
import com.prestrive.com.service.trade.mapper.OrderMapper;
import com.prestrive.com.service.trade.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.prestrive.com.service.trade.service.PayLogService;
import com.prestrive.com.service.trade.util.OrderNoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author strive
 * @since 2022-07-09
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    UcenterMemberService ucenterMemberService;

    @Autowired
    EduCourseService eduCourseService;

    @Autowired
    PayLogService payLogService;

    @Override
    public String saveOrder(String courseId, String memberId) {

        //查询当前用户是否已有当前课程的订单
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("member_id", memberId);
        queryWrapper.eq("course_id",courseId);
        Order orderExist = orderMapper.selectOne(queryWrapper);
        if(orderExist != null){
           return orderExist.getId();//如果订单已存在，则直接返回订单id
        }

        //查询课程信息
        CourseDto courseDto = eduCourseService.getCourseDtoById(courseId);
        if(courseDto == null){
            throw new GlobalException(ResultCodeEnum.PARAM_ERROR);
        }

        //查询用户信息
        MemberDto memberDto = ucenterMemberService.getMemberDtoByMemberId(memberId);
        if(memberDto == null){
            throw new GlobalException(ResultCodeEnum.PARAM_ERROR);
        }

        //创建订单
        Order order = new Order();
        order.setOrderNo(OrderNoUtils.getOrderNo());
        order.setCourseId(courseId);
        order.setCourseTitle(courseDto.getTitle());
        order.setCourseCover(courseDto.getCover());
        order.setTeacherName(courseDto.getTeacherName());
        order.setTotalFee(courseDto.getPrice().multiply(new BigDecimal(100)));
        order.setMemberId(memberId);
        order.setMobile(memberDto.getMobile());
        order.setNickname(memberDto.getNickname());
        order.setStatus(0);//未支付
        order.setPayType(1);//微信支付
        orderMapper.insert(order);
        return order.getId();
    }

    @Override
    public Order getByOrderId(String orderId, String memberId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id",orderId).eq("member_id", memberId);
        return orderMapper.selectOne(queryWrapper);
    }

    @Override
    public Boolean isBuyByCourseId(String courseId, String id) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        queryWrapper.eq("member_id",id).eq("status", 1);;

        Integer count = orderMapper.selectCount(queryWrapper);

        return count > 0;
    }

    @Override
    public List<Order> findListByMemberId(String id) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_create");
        queryWrapper.eq("member_id",id);

        return orderMapper.selectList(queryWrapper);
    }

    @Override
    public boolean removeById(String orderId, String memberId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",orderId).eq("member_id",memberId);

        int delete = orderMapper.delete(queryWrapper);
        return delete > 0;
    }

    @Override
    public Order getOrderByOrderNo(String orderNo) {

        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        return orderMapper.selectOne(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateOrderStatusByNotifyMap(Map<String, String> notifyMap) {
        //更新订单状态
        String out_trade_no = notifyMap.get("out_trade_no");
        Order order = this.getOrderByOrderNo(out_trade_no);
        order.setStatus(1);
        orderMapper.updateById(order);

       //记录支付日志
        PayLog payLog = new PayLog();
        payLog.setOrderNo(out_trade_no);
        payLog.setPayTime(new Date());
        payLog.setPayType(1);//支付类型
        payLog.setTotalFee(Long.parseLong(notifyMap.get("total_fee")));//总金额(分)
        payLog.setTradeState(notifyMap.get("result_code"));//支付状态
        payLog.setTradeState(notifyMap.get("transaction_id"));
        payLog.setAttr(new Gson().toJson(notifyMap));

        //更新课程销量：有问题直接熔断
        eduCourseService.updateBuyCountById(order.getCourseId());
        payLogService.save(payLog);
    }

    @Override
    public boolean queryPayStatus(String orderNo) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no",orderNo);

        Order order = orderMapper.selectOne(queryWrapper);
        Integer status = order.getStatus();

        return status == 1;
    }

    public static void main(String[] args) {
        BigDecimal bigDecimal = new BigDecimal(12);
        BigDecimal abs = bigDecimal.abs();
        System.out.println(abs);
        BigDecimal multiply = bigDecimal.multiply(new BigDecimal(100));
        System.out.println(multiply);
    }
}
