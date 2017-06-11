package com.blue.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blue.bean.Order;
import com.blue.bean.OrderItem;
import com.blue.bean.Product;
import com.blue.bean.User;
import com.blue.exception.PrivilegeException;
import com.blue.redis.RedisClintTemplate;
import com.blue.service.OrderService;

@Controller
@RequestMapping("/order")
public class OrderController {
	@Resource(name="orderService")
	private OrderService orderService;
	
	@Resource(name="redisClintTemplate")
	private RedisClintTemplate redisClintTemplate;
	
	@RequestMapping("/add")
	public String add(@ModelAttribute Order order,HttpSession session,HttpServletRequest request){
		order.setId(UUID.randomUUID().toString());
		User user= (User) session.getAttribute("user");
		if (null==user) {
			return "redirect:/error";
		}
		order.setUserId(user.getId());
		// 1.3 将订单项封装到订单中.
		/*Map<Product, Integer> cart= (Map<Product, Integer>) session.getAttribute("cart");
		List<OrderItem> orderItems= new ArrayList<>();
		for (Product product : cart.keySet()) {
			OrderItem orderItem= new OrderItem();
			orderItem.setOrderId(order.getId());
			orderItem.setProductId(product.getId());
			orderItem.setBuynum(cart.get(product));
			orderItems.add(orderItem);
		}*/
		
		//1.3 将订单项封装到订单中.
		Map<String, String> cart= redisClintTemplate.hgetAll(""+ user.getId());
		List<OrderItem> orderItems= new ArrayList<>();
		for (String productId : cart.keySet()) {
			OrderItem orderItem= new OrderItem();
			orderItem.setOrderId(order.getId());
			orderItem.setProductId(productId);
			orderItem.setBuynum(Integer.parseInt(cart.get(productId)));
			orderItems.add(orderItem);
		}
		
		
		
		
		
		order.setOrderItems(orderItems);
		
		// 2.调用OrderService中方法，创建订单
		try {
			orderService.add(user, order);
			
			//生成订单成功后，购物车的商品需要删除
			redisClintTemplate.del(""+ user.getId());
			
			return "redirect:/order/toadd";
		} catch (Exception e) {
			e.printStackTrace();
			
			return "redirect:/error";
		}
	}
	
	@RequestMapping("/toadd")
	public @ResponseBody String toAdd(HttpServletRequest request){
		return "下单成功,<a href='" + request.getContextPath()
							+ "/index'>继续购物</a>，<a href='"
							+ request.getContextPath()
							+ "/order/search'>查看订单</a>";
	}
	@RequestMapping("/orderdelerror")
	public @ResponseBody String orderdelerror(){
		return "删除订单失败";
	}
	@RequestMapping("/unloginerror")
	public @ResponseBody String unloginerror(HttpServletRequest request){
		return "请先<a href='" + request.getContextPath()
							+ "/index.jsp'>登录</a>";
	}
	@RequestMapping("/ordersearcherror")
	public @ResponseBody String ordersearcherror(){
		return "查询订单失败，可能是权限不足";
	}
	
	
	
	@RequestMapping("/del/{id}")
	public String del(@PathVariable("id") String id){
		try {
			orderService.delete(id);
			// 在次查询订单
			return "redirect:/order/search"; 
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/order/orderdelerror";
		}
	}
	
	@RequestMapping("/search")
	public String search(HttpSession session,Model model){
		User user= (User) session.getAttribute("user");
		if (null==user) {
			return "redirect:/order/unloginerror";
		}
		
		// 2.调用OrderService中查询订单操作
		try {
			List<Order> orders = orderService.find(user);
			model.addAttribute("orders", orders);
			return "showOrder";
		} catch (PrivilegeException e) {
			e.printStackTrace();
			return "redirect:/order/ordersearcherror";
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/order/ordersearcherror";
		}
	}
	
	@RequestMapping("/prepay")
	public String prePay(){
		return "pay";
	}
	
	
}
