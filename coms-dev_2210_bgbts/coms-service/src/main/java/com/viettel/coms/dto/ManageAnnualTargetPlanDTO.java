/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.coms.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
//import org.junit.Ignore;

import com.viettel.coms.bo.ManageAnnualTargetPlanBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;

/**
 * @author thuannht
 */
@XmlRootElement(name = "MANAGE_ANNUAL_TARGET_PLANBO")
public class ManageAnnualTargetPlanDTO extends ComsBaseFWDTO<ManageAnnualTargetPlanBO> {

	private java.lang.Long manageAnnualTargetPlanId;
	private java.lang.String year;
	private java.lang.String month;
	private java.lang.Long contractValue;
	private java.lang.Long tcValue;
	private java.lang.Long doanhThu;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date createDate;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date updateDate;
	private java.lang.Long createUserId;
	private java.lang.Long updateUserId;
	private Long status;

	@Override
	public ManageAnnualTargetPlanBO toModel() {
		ManageAnnualTargetPlanBO bo = new ManageAnnualTargetPlanBO();
		bo.setManageAnnualTargetPlanId(this.manageAnnualTargetPlanId);
		bo.setYear(this.year);
		bo.setMonth(this.month);
		bo.setContractValue(this.contractValue);
		bo.setTcValue(this.tcValue);
		bo.setDoanhThu(this.doanhThu);
		bo.setCreateDate(this.createDate);
		bo.setUpdateDate(this.updateDate);
		bo.setCreateUserId(this.createUserId);
		bo.setUpdateUserId(this.updateUserId);
		bo.setStatus(this.status);
		return bo;
	}

	@Override
	public String catchName() {
		// TODO Auto-generated method stub
		return null;
	}

	@JsonIgnore
	@Override
	public Long getFWModelId() {
		// TODO Auto-generated method stub
		return null;
	}
	

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public java.lang.Long getManageAnnualTargetPlanId() {
		return manageAnnualTargetPlanId;
	}

	public void setManageAnnualTargetPlanId(java.lang.Long manageAnnualTargetPlanId) {
		this.manageAnnualTargetPlanId = manageAnnualTargetPlanId;
	}

	public java.lang.String getYear() {
		return year;
	}

	public void setYear(java.lang.String year) {
		this.year = year;
	}

	public java.lang.String getMonth() {
		return month;
	}

	public void setMonth(java.lang.String month) {
		this.month = month;
	}

	public java.lang.Long getContractValue() {
		return contractValue;
	}

	public void setContractValue(java.lang.Long contractValue) {
		this.contractValue = contractValue;
	}

	public java.lang.Long getTcValue() {
		return tcValue;
	}

	public void setTcValue(java.lang.Long tcValue) {
		this.tcValue = tcValue;
	}

	public java.lang.Long getDoanhThu() {
		return doanhThu;
	}

	public void setDoanhThu(java.lang.Long doanhThu) {
		this.doanhThu = doanhThu;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public java.lang.Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(java.lang.Long createUserId) {
		this.createUserId = createUserId;
	}

	public java.lang.Long getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(java.lang.Long updateUserId) {
		this.updateUserId = updateUserId;
	}

}
