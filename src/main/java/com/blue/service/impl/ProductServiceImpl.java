package com.blue.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blue.bean.Product;
import com.blue.bean.User;
import com.blue.dao.ProductDao;
import com.blue.exception.PrivilegeException;
import com.blue.service.ProductService;

@Service("productService")
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductDao productDao;

	// 添加商品
	@Transactional
	@Override
	public void add(User user, Product product) throws PrivilegeException,
			Exception {
		productDao.addProduct(product);	
	}

	// 查找所有商品
	@Override
	public List<Product> findAll() throws Exception {
		return productDao.findAll();
	}

	// 根据id查找商品
	@Override
	public Product findById(String id) throws Exception {
		return productDao.findById(id);
	}

	// 得到销售榜单信息
	@Override
	public List<Product> findSell(User user) throws PrivilegeException,
			Exception {
		return productDao.findSell();
	}

}
