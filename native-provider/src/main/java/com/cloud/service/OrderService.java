package com.cloud.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.entity.RabbitOrder;
import com.cloud.mapper.OrderMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-03-27
 */
@Service
public class OrderService extends ServiceImpl<OrderMapper, RabbitOrder> {

}
