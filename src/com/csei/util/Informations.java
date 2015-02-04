package com.csei.util;

import java.io.Serializable;

public class Informations implements Serializable{
	public int number;
	public String role;
	public int roleNum;
	public String name;
	public String userName;
	public int id;
	public String image;
	public String sex;
	public String userRole;
	
	public Informations(String number,String role,String roleNum,String name,String userName,String id,String image,String sex,String userRole) {
		// TODO Auto-generated constructor stub
		this.number=Integer.parseInt(number);
		this.role=role;
		this.roleNum=Integer.parseInt(roleNum);
		this.name=name;
		this.userName=userName;
		this.id=Integer.parseInt(id);
		this.image=image;
		this.sex=sex;
		this.userRole=userRole;
	}
}
