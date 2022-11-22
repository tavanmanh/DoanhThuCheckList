package com.viettel.coms.dto;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.erp.utils.CustomJsonDateDeserializer;
import com.viettel.erp.utils.CustomJsonDateSerializer;
import com.viettel.service.base.model.BaseFWModelImpl;

public class ReportErpAmsWoDTO extends ComsBaseFWDTO<BaseFWModelImpl>{

	private String stationCode;
	private String constructionCode;
	private String provinceCode;
	private String provinceName;
	private String contractCode;
	private String ftName;
	private String state;
	private String endTime;
	private String datePayment;
	private String billCode;
	private String goodsName;
	private Double totalPrice;
	private Double amountReal;
	private Double amount;
	
	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public String getConstructionCode() {
		return constructionCode;
	}

	public void setConstructionCode(String constructionCode) {
		this.constructionCode = constructionCode;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getFtName() {
		return ftName;
	}

	public void setFtName(String ftName) {
		this.ftName = ftName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getDatePayment() {
		return datePayment;
	}

	public void setDatePayment(String datePayment) {
		this.datePayment = datePayment;
	}

	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Double getAmountReal() {
		return amountReal;
	}

	public void setAmountReal(Double amountReal) {
		this.amountReal = amountReal;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@Override
	public String catchName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getFWModelId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseFWModelImpl toModel() {
		// TODO Auto-generated method stub
		return null;
	}

}
