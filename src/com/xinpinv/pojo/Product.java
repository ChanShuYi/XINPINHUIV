package com.xinpinv.pojo;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_yw_product")
public class Product {
	
	/** 商品编号 **/
	private int id;
	
	/** 商品实际ID **/
	private int trueId;
	
	/** 商品名称 **/
	private String productName;
	
	/** 最终价格 **/
	private float finalPrice;
	
	/** 商品价值 **/
	private float marketPrice;
	
	/** 竞购资格 **/
	private String bitCondition;
	
	/** 延时周期 **/
	private int downcount;
	
	/** 最终出价人 **/
	private String finalUser;
	
	/** 出价人数 **/
	private int bitCount;
	
	/** 出价规则 **/
	private String bitRule;
	
	/** 开始时间 **/
	private Date startTime;
	
	/** 结束时间 **/
	private Date endTime;

	/** 持续时间 **/
	private long duration;
	
	/** 竞拍信息 **/
	private List<BitInfo> bitInfos;
	
	@Id
	@SequenceGenerator(name = "prodG",sequenceName="PRODUCT_ID_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prodG")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Column(nullable = true)
	public int getTrueId() {
		return trueId;
	}

	public void setTrueId(int trueId) {
		this.trueId = trueId;
	}
	
	@Column(nullable = true)
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Column(nullable = true)
	public float getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(float finalPrice) {
		this.finalPrice = finalPrice;
	}

	@Column(nullable = true)
	public float getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(float marketPrice) {
		this.marketPrice = marketPrice;
	}

	@Column(nullable = true)
	public String getBitCondition() {
		return bitCondition;
	}

	public void setBitCondition(String bitCondition) {
		this.bitCondition = bitCondition;
	}

	@Column(nullable = true)
	public int getDowncount() {
		return downcount;
	}

	public void setDowncount(int downcount) {
		this.downcount = downcount;
	}

	@Column(nullable = true)
	public String getFinalUser() {
		return finalUser;
	}

	public void setFinalUser(String finalUser) {
		this.finalUser = finalUser;
	}

	@Column(nullable = true)
	public int getBitCount() {
		return bitCount;
	}

	public void setBitCount(int bitCount) {
		this.bitCount = bitCount;
	}

	@Column(nullable = true)
	public String getBitRule() {
		return bitRule;
	}

	public void setBitRule(String bitRule) {
		this.bitRule = bitRule;
	}

	@Column(nullable = true)
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Column(nullable = true)
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Column(nullable = true)
	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}
	
	@OneToMany(mappedBy = "product", cascade=CascadeType.ALL, targetEntity = BitInfo.class)
	public List<BitInfo> getBitInfos() {
		return bitInfos;
	}

	public void setBitInfos(List<BitInfo> bitInfos) {
		this.bitInfos = bitInfos;
	}
}
