package com.blue.dao;

import java.util.List;

import com.blue.bean.Order;
import com.blue.bean.User;

public interface OrderDao {
	//删除订单
    int deleteOrderByPrimaryKey(String id);

    // 创建订单
    int createOrder(Order record);
    
    //根据用户查询订单
    List<Order> findOrderByUser(User user);

    //修改订单状态
    int updateStateByPrimaryKey(String id);
}