package com.easyservice.test;

import com.easyservice.support.Role;

public class User implements Role{
	private int role;
	private String name;
	public User(){}
	public User(String name,int role)
	{
		this.name=name;
		this.role=role;
	}
	@Override
	public int getRole() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setRole(int role) {
		// TODO Auto-generated method stub
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
