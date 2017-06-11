package com.blue.dao;

import java.util.List;

import com.blue.bean.Order;
import com.blue.bean.Product;

public interface ProductDao {

    //添加商品//
    int addProduct(Product record);

    //查找所有商品
    List<Product> findAll();
    
    //根据id查找商品
    Product findById(String id);

    //减商品数量
    int subProductCount(Order record);
    
    //增加商品数量
    int addProductCount(Order record);
    
    //查看所有商品的销售榜单
    List<Product> findSell();
}