/*
 * Copyright 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.coms.bo;

import com.viettel.coms.dto.KpiLogDTO;
import com.viettel.coms.dto.KpiLogTimeProcessDTO;
import com.viettel.coms.dto.ManageAnnualTargetPlanDTO;
import com.viettel.service.base.model.BaseFWModelImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "MANAGE_ANNUAL_TARGET_PLAN")
/**
 *
 * @author: ThuanNHT
 * @version: 1.0
 * @since: 1.0
 */
public class ManageAnnualTargetPlanBO extends BaseFWModelImpl {

	private java.lang.Long manageAnnualTargetPlanId;
	private java.lang.String year;
	private java.lang.String month;
	private java.lang.Long contractValue;
	private java.lang.Long tcValue;
	private java.lang.Long doanhThu;
	private Date createDate;
	private Date updateDate;
	private java.lang.Long createUserId;
	private java.lang.Long updateUserId;
	private Long status;

	@Override
	public ManageAnnualTargetPlanDTO toDTO() {
		ManageAnnualTargetPlanDTO bo = new ManageAnnualTargetPlanDTO();
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

	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "MANAGE_ANNUAL_TARGET_PLAN_SEQ") })
	@Column(name = "MANAGE_ANNUAL_TARGET_PLAN_ID", length = 22)
	public java.lang.Long getManageAnnualTargetPlanId() {
		return manageAnnualTargetPlanId;
	}

	public void setManageAnnualTargetPlanId(java.lang.Long manageAnnualTargetPlanId) {
		this.manageAnnualTargetPlanId = manageAnnualTargetPlanId;
	}

	
	@Column(name = "STATUS", length = 22)
	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	@Column(name = "YEAR", length = 22)
	public java.lang.String getYear() {
		return year;
	}

	public void setYear(java.lang.String year) {
		this.year = year;
	}

	@Column(name = "MONTH", length = 22)
	public java.lang.String getMonth() {
		return month;
	}

	public void setMonth(java.lang.String month) {
		this.month = month;
	}

	@Column(name = "CONTRACT_VALUE", length = 22)
	public java.lang.Long getContractValue() {
		return contractValue;
	}

	public void setContractValue(java.lang.Long contractValue) {
		this.contractValue = contractValue;
	}

	@Column(name = "TC_VALUE", length = 22)
	public java.lang.Long getTcValue() {
		return tcValue;
	}

	public void setTcValue(java.lang.Long tcValue) {
		this.tcValue = tcValue;
	}

	@Column(name = "DOANH_THU", length = 22)
	public java.lang.Long getDoanhThu() {
		return doanhThu;
	}

	public void setDoanhThu(java.lang.Long doanhThu) {
		this.doanhThu = doanhThu;
	}

	@Column(name = "CREATE_DATE", length = 22)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "UPDATE_DATE", length = 22)
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Column(name = "CREATE_USER_ID", length = 22)
	public java.lang.Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(java.lang.Long createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "UPDATE_USER_ID", length = 22)
	public java.lang.Long getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(java.lang.Long updateUserId) {
		this.updateUserId = updateUserId;
	}

}
