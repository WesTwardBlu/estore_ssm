package com.blue.service.impl;

import java.sql.SQLException;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blue.bean.Order;
import com.blue.bean.OrderItem;
import com.blue.bean.User;
import com.blue.dao.OrderDao;
import com.blue.dao.OrderItemDao;
import com.blue.dao.ProductDao;
import com.blue.exception.PrivilegeException;
import com.blue.service.OrderService;

@Service("orderService")//在spring容器中，注册此servi
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private OrderItemDao orderItemDao;
	@Autowired
	private ProductDao productDao;
	
	// 添加订单
	@Transactional
	@Override
	public void add(User user, Order order) throws PrivilegeException,
			Exception {
		orderDao.createOrder(order);
		orderItemDao.addOrderItem(order);
		productDao.subProductCount(order);
	}

	// 根据用户查找订单
	@Override
	public List<Order> find(User user) throws PrivilegeException, Exception {
		List<Order> orders= orderDao.findOrderByUser(user);
		for (Order order : orders) {
			List<OrderItem> orderItems= orderItemDao.findOrderItemByOrder(order);
			order.setOrderItems(orderItems);
		}
		
		return orders;
	}

	// 根据id删除订单
	@Transactional
	@Override
	public void delete(String id) throws Exception {
		Order order= new Order();
		List<OrderItem> orderItems = orderItemDao.findOrderItemByOrderById(id);
		order.setOrderItems(orderItems);
		productDao.addProductCount(order);
		orderItemDao.delOrderItem(id);
		orderDao.deleteOrderByPrimaryKey(id);
	}

	//根据订单id修改订单状态
	@Transactional
	@Override
	public void updateState(String id) throws SQLException {
		orderDao.updateStateByPrimaryKey(id);
	}

}
