package com.viettel.coms.dto;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;
import com.viettel.service.base.model.BaseFWModelImpl;

public class SettlementDebtARpDTO extends ComsBaseFWDTO<BaseFWModelImpl>{

	private String sysGroupName;
	private String contractCode;
	private String constructionCode;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date realIeTransDate;
	private String goodsCode;
	private String goodsName;
	private String goodsUnitName;
	private String amountTotal;
	private String moneyTotal;
	private String overDay;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date dateFrom;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date dateTo;
	private String bpartnerDrValue;
	
	public String getBpartnerDrValue() {
		return bpartnerDrValue;
	}

	public void setBpartnerDrValue(String bpartnerDrValue) {
		this.bpartnerDrValue = bpartnerDrValue;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	public String getSysGroupName() {
		return sysGroupName;
	}

	public void setSysGroupName(String sysGroupName) {
		this.sysGroupName = sysGroupName;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getConstructionCode() {
		return constructionCode;
	}

	public void setConstructionCode(String constructionCode) {
		this.constructionCode = constructionCode;
	}

	public Date getRealIeTransDate() {
		return realIeTransDate;
	}

	public void setRealIeTransDate(Date realIeTransDate) {
		this.realIeTransDate = realIeTransDate;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsUnitName() {
		return goodsUnitName;
	}

	public void setGoodsUnitName(String goodsUnitName) {
		this.goodsUnitName = goodsUnitName;
	}

	public String getAmountTotal() {
		return amountTotal;
	}

	public void setAmountTotal(String amountTotal) {
		this.amountTotal = amountTotal;
	}

	public String getMoneyTotal() {
		return moneyTotal;
	}

	public void setMoneyTotal(String moneyTotal) {
		this.moneyTotal = moneyTotal;
	}

	public String getOverDay() {
		return overDay;
	}

	public void setOverDay(String overDay) {
		this.overDay = overDay;
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
