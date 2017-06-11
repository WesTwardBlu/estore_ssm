package com.blue.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class User implements Serializable{
   
	private static final long serialVersionUID = -6306584012372748328L;

	private Integer id;
	@NotEmpty(message="用户名不能为空")
    private String username;

	@NotEmpty(message="密码不能为空")
    private String password;

	@NotEmpty(message="昵称不能为空")
    private String nickname;

	@NotNull(message="邮箱不能为空")
	@Email(message="邮箱格式不正确")
    private String email;

    private String role;

    private Integer state;//是否激活

    private String activecode;//激活码

    private Timestamp updatetime;//更新时间

    public Map<String, String> validateRegist() {
    	Map<String, String> map = new HashMap<String, String>();
    	
    	if (username == null || username.trim().isEmpty()) {
    		map.put("username.message", "用户名不能为空");
    	}
    	
    	if (password == null || password.trim().isEmpty()) {
    		map.put("password.message", "密码不能为空");
    	}
    	
    	if (nickname == null || nickname.trim().isEmpty()) {
    		map.put("nickname.message", "昵称不能为空");
    	}
    	
    	if (email == null || email.trim().isEmpty()) {
    		map.put("email.message", "邮箱不能为空");
    	}
    	
    	return map;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role == null ? null : role.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getActivecode() {
        return activecode;
    }

    public void setActivecode(String activecode) {
        this.activecode = activecode == null ? null : activecode.trim();
    }

	public Timestamp getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}

	
    
}