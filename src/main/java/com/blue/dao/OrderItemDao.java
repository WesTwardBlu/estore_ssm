package com.blue.dao;

import java.util.List;

import com.blue.bean.Order;
import com.blue.bean.OrderItem;

public interface OrderItemDao {
	// 添加订单项
    int addOrderItem(Order order);

    // 根据订单查询订单项
    List<OrderItem> findOrderItemByOrder(Order order);
    
    // 根据订单id查询订单项
    List<OrderItem> findOrderItemByOrderById(String orderId);

    //删除订单项
    int delOrderItem(String orderId);
}