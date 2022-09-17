package com.prestrive.com.service.trade.service;

import com.prestrive.com.service.trade.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author strive
 * @since 2022-07-09
 */
public interface OrderService extends IService<Order> {

    /**
     * 保存订单并返回订单id
     * @param courseId
     * @param memberId
     * @return
     */
    String saveOrder(String courseId, String memberId);

    /**
     * 获取订单
     * @param orderId
     * @param memberId
     * @return
     */
    Order getByOrderId(String orderId, String memberId);

    /**
     * 根据课程id查询是否已购买
     * @param courseId
     * @param id
     * @return
     */
    Boolean isBuyByCourseId(String courseId, String id);

    /**
     * 根据会员id 查询 订单列表
     * @param id
     * @return
     */
    List<Order> findListByMemberId(String id);

    /**
     * 删除订单
     * @param orderId
     * @param memberId
     * @return
     */
    boolean removeById(String orderId, String memberId);

    /**
     * 根据订单号查询订单
     * @param orderNo
     * @return
     */
    Order getOrderByOrderNo(String orderNo);

    /**
     * 更新订单状态
     * @param notifyMap
     */
    void updateOrderStatusByNotifyMap(Map<String, String> notifyMap);

    /**
     * 查询订单状态
     * @param orderNo
     * @return
     */
    boolean queryPayStatus(String orderNo);
}
