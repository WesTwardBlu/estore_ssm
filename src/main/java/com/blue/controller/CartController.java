package com.blue.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.blue.bean.Product;
import com.blue.bean.User;
import com.blue.redis.RedisClintTemplate;
import com.blue.service.ProductService;

@Controller
@RequestMapping("/cart")
public class CartController {
	
	@Resource(name="productService")
	private ProductService productService;
	
	@Resource(name="redisClintTemplate")
	private RedisClintTemplate redisClintTemplate;
	// 添加商品到购物车
	@RequestMapping("/add")
	public String add(@RequestParam String id,HttpSession session) {
		try {
//			Product product= productService.findById(id);
			// 3.得到购物车
//			Map<Product, Integer> cart= (Map<Product, Integer>) session.getAttribute("cart");
			
//			Map<String, String> cartNew= new HashMap<>(10);
			User user= (User) session.getAttribute("user");
			if (user!=null) {
				redisClintTemplate.hincrBy(""+ user.getId(), id, 1);//将商品放入redis购物车
			}
			
			
			/*if (cart==null) {
				cart= new HashMap<Product, Integer>();
			}
			Integer count= cart.put(product, 1);
			if (null!=count) {//说明购物车里本身已经有此商品了，所以需要加上本来就有的count
				cart.put(product, count+1);
			}
			session.setAttribute("cart", cart);//因为前面有newhashmap
*/		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:/index";
	}
	
	// 从购物车中删除商品
	@RequestMapping("/remove")
	public String remove(@RequestParam String id,HttpSession session){
		/*Product product= new Product();
		product.setId(id);
		Map<Product, Integer> cart= (Map<Product, Integer>) session.getAttribute("cart");
		cart.remove(product);
		if (cart.size()==0) {
			session.removeAttribute("cart");
		}*/
		
		
		User user= (User) session.getAttribute("user");
		if (user!=null) {
			
			redisClintTemplate.hdel(""+ user.getId(), id);
			if (redisClintTemplate.hlen(""+ user.getId())==0) {
				redisClintTemplate.del(""+ user.getId());
			}
		}
		
		
		
//		return "redirect:"+ request.getContextPath()+ "/WEB-INF/jsp/showCart.jsp";
		return "redirect:/cart/showcart";
	}
	
	// 修改购物车中商品数量
	// 得到要修改的商品的数量与商品id
	@RequestMapping("/update")
	public String update(@RequestParam String id,@RequestParam int count,HttpSession session) {
		/*Product product= new Product();	
		product.setId(id);
		Map<Product, Integer> cart= (Map<Product, Integer>) session.getAttribute("cart");
		if (count==0) {
			cart.remove(product);
		} else {
			cart.put(product, count);
		}*/
		
		User user= (User) session.getAttribute("user");
		if (user!=null) {
			
			if (count==0) {
				redisClintTemplate.hdel(""+ user.getId(), id);
			} else {
				redisClintTemplate.hset(""+ user.getId(), id, ""+count);
			}
		}
		
		
//		return "redirect:"+ request.getContextPath()+ "/WEB-INF/jsp/showCart.jsp";
		return "redirect:/cart/showcart";
		
	}
	
	@RequestMapping("preorder")
	public  String preOrder(){
		return "order";
	}
	
	@RequestMapping("showcart")
	public  String preShowCart(HttpSession session){
		User user= (User) session.getAttribute("user");
		if (user!=null) {
			try {
				Map<Product, String> cartTemp= new HashMap<>(10);
				Map<String, String> cart= redisClintTemplate.hgetAll(""+ user.getId());
				for (String productId : cart.keySet()) {
					
					Product product= productService.findById(productId);
					cartTemp.put(product, cart.get(productId));
					
				}
				session.setAttribute("cart", cartTemp);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		return "showCart";
	}
	
	
}
