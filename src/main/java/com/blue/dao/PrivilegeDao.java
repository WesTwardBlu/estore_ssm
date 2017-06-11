package com.blue.dao;

import java.util.List;


public interface PrivilegeDao {

	//查看某一角色role下的所有权限，并返回集合
    List<String> selectPrivilegesByRole(String role);

    
}