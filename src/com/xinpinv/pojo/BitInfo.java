package com.xinpinv.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_yw_bitinfo")
public class BitInfo {
	
	/** ��� **/
	private int id;
	
	/** �����û� **/
	private String username;
	
	/** �����۸� **/
	private float price;
	
	/** ����ʱ�� **/
	private Date bitTime;
	
	/** �û�IP **/
	private String ip;
	
	/** �û���ַ **/
	private String address;
	
	/** ������Ϣ��������Ʒ **/
	private Product product;

	@Id
	@SequenceGenerator(name = "bitInfoG",sequenceName="BITINFO_ID_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bitInfoG")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Column(nullable = true)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(nullable = true)
	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	@Column(nullable = true)
	public Date getBitTime() {
		return bitTime;
	}

	public void setBitTime(Date bitTime) {
		this.bitTime = bitTime;
	}

	@Column(nullable = true)
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(nullable = true)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@ManyToOne()
	@JoinTable(
				name = "t_yw_productmapbitInfo",
				joinColumns = @JoinColumn(name = "bitinfoid"),
				inverseJoinColumns = @JoinColumn(name = "productid")
			  )
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	
}
