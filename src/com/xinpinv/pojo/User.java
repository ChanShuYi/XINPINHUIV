package com.xinpinv.pojo;

public class User {

	private String ip;
	
	private String username;
	
	private String address;
	
	private int count;
	
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(String ip, String username, String address, int count) {
		super();
		this.ip = ip;
		this.username = username;
		this.address = address;
		this.count = count;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	
	
}
