package com.prestrive.com.service.trade.mapper;

import com.prestrive.com.service.trade.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 订单 Mapper 接口
 * </p>
 *
 * @author strive
 * @since 2022-07-09
 */
@Mapper
@Repository
public interface OrderMapper extends BaseMapper<Order> {

}
