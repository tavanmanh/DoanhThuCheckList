package com.viettel.coms.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.ProgressTaskOsDTO;
import com.viettel.service.base.model.BaseFWModelImpl;

@SuppressWarnings("serial")
@Entity(name = "com.viettel.erp.bo.ProgressTaskOsBO")
@Table(name = "PROGRESS_TASK_OS")
/**
 *
 * @author: hailh10
 */
public class ProgressTaskOsBO extends BaseFWModelImpl {

	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "PROGRESS_TASK_OS_SEQ") })
	@Column(name = "PROGRESS_TASK_OS_ID", length = 10)
	private Long progressTaskOsId;
	@Column(name = "CONSTRUCTION_CODE", length = 100)
	private String constructionCode;
	@Column(name = "CNT_CONTRACT_CODE", length = 100)
	private String cntContractCode;
	@Column(name = "TTKV", length = 50)
	private String ttkv;
	@Column(name = "TTKT", length = 50)
	private String ttkt;
	@Column(name = "DESCRIPTION", length = 1000)
	private String description;
	@Column(name = "SOURCE_TASK", length = 2)
	private String sourceTask;
	@Column(name = "CONSTRUCTION_TYPE", length = 2)
	private String constructionType;
	@Column(name = "QUANTITY_VALUE", length = 20)
	private Long quantityValue;
	@Column(name = "HSHC_VALUE", length = 20)
	private Long hshcValue;
	@Column(name = "SALARY_VALUE", length = 20)
	private Long salaryValue;
	@Column(name = "BILL_VALUE", length = 20)
	private Long billValue;
	@Column(name = "TDSL_ACCOMPLISHED_DATE", length = 22)
	private Date tdslAccomplishedDate;
	@Column(name = "TDSL_CONSTRUCTING", length = 22)
	private Date tdslConstructing;
	@Column(name = "TDSL_EXPECTED_COMPLETE_DATE", length = 22)
	private Date tdslExpectedCompleteDate;
	@Column(name = "TDHS_TCT_NOT_APPROVAL", length = 22)
	private Date tdhsTctNotApproval;
	@Column(name = "TDHS_SIGNING_GDCN", length = 22)
	private Date tdhsSigningGdcn;
	@Column(name = "TDHS_CONTROL_4A", length = 22)
	private Date tdhsControl4a;
	@Column(name = "TDHS_PHT_APPROVALING", length = 22)
	private Date tdhsPhtApprovaling;
	@Column(name = "TDHS_PHT_ACCEPTANCING", length = 22)
	private Date tdhsPhtAcceptancing;
	@Column(name = "TDHS_TTKT_PROFILE", length = 22)
	private Date tdhsTtktProfile;
	@Column(name = "TDHS_EXPECTED_COMPLETE_DATE", length = 22)
	private Date tdhsExpectedCompleteDate;
	@Column(name = "TDTT_COLLECT_MONEY_DATE", length = 22)
	private Date tdttCollectMoneyDate;
	@Column(name = "TDTT_PROFILE_PHT", length = 22)
	private Date tdttProfilePht;
	@Column(name = "TDTT_PROFILE_PTC", length = 22)
	private Date tdttProfilePtc;
	@Column(name = "TDTT_EXPECTED_COMPLETE_DATE", length = 22)
	private Date tdttExpectedCompleteDate;
	@Column(name = "CREATED_USER_ID", length = 10)
	private Long createdUserId;
	@Column(name = "CREATED_DATE", length = 22)
	private Date createdDate;
	@Column(name = "UPDATED_USER_ID", length = 10)
	private Long updatedUserId;
	@Column(name = "UPDATED_DATE", length = 22)
	private Date updatedDate;
	@Column(name = "STATUS", length = 2)
	private String status;
	@Column(name = "MONTH_YEAR", length = 20)
	private String monthYear;
	@Column(name = "CHECK_HTCT", length = 2)
	private String checkHtct;
	@Column(name = "APPROVE_REVENUE_DATE", length = 22)
	private Date approveRevenueDate;
	@Column(name = "APPROVE_COMPLETE_DATE", length = 22)
	private Date approveCompleteDate;
	@Column(name = "TTKT_ID", length = 22)
	private Long ttktId;
	@Column(name = "TYPE", length = 20)
	private String type;

	@Column(name = "WORK_ITEM_NAME", length = 22)
	private String workItemName;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getWorkItemName() {
		return workItemName;
	}

	public void setWorkItemName(String workItemName) {
		this.workItemName = workItemName;
	}

	public Long getTtktId() {
		return ttktId;
	}

	public void setTtktId(Long ttktId) {
		this.ttktId = ttktId;
	}

	public Date getApproveCompleteDate() {
		return approveCompleteDate;
	}

	public void setApproveCompleteDate(Date approveCompleteDate) {
		this.approveCompleteDate = approveCompleteDate;
	}
	public Date getApproveRevenueDate() {
		return approveRevenueDate;
	}

	public void setApproveRevenueDate(Date approveRevenueDate) {
		this.approveRevenueDate = approveRevenueDate;
	}

	public String getCheckHtct() {
		return checkHtct;
	}

	public void setCheckHtct(String checkHtct) {
		this.checkHtct = checkHtct;
	}
	public String getMonthYear() {
		return monthYear;
	}

	public void setMonthYear(String monthYear) {
		this.monthYear = monthYear;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getCreatedUserId() {
		return createdUserId;
	}

	public void setCreatedUserId(Long createdUserId) {
		this.createdUserId = createdUserId;
	}

	public java.util.Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(java.util.Date createdDate) {
		this.createdDate = createdDate;
	}

	public Long getUpdatedUserId() {
		return updatedUserId;
	}

	public void setUpdatedUserId(Long updatedUserId) {
		this.updatedUserId = updatedUserId;
	}

	public java.util.Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(java.util.Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public java.lang.Long getProgressTaskOsId() {
		return progressTaskOsId;
	}

	public void setProgressTaskOsId(java.lang.Long progressTaskOsId) {
		this.progressTaskOsId = progressTaskOsId;
	}

	public java.lang.String getConstructionCode() {
		return constructionCode;
	}

	public void setConstructionCode(java.lang.String constructionCode) {
		this.constructionCode = constructionCode;
	}

	public java.lang.String getCntContractCode() {
		return cntContractCode;
	}

	public void setCntContractCode(java.lang.String cntContractCode) {
		this.cntContractCode = cntContractCode;
	}

	public java.lang.String getTtkv() {
		return ttkv;
	}

	public void setTtkv(java.lang.String ttkv) {
		this.ttkv = ttkv;
	}

	public java.lang.String getTtkt() {
		return ttkt;
	}

	public void setTtkt(java.lang.String ttkt) {
		this.ttkt = ttkt;
	}

	public java.lang.String getDescription() {
		return description;
	}

	public void setDescription(java.lang.String description) {
		this.description = description;
	}

	public java.lang.String getSourceTask() {
		return sourceTask;
	}

	public void setSourceTask(java.lang.String sourceTask) {
		this.sourceTask = sourceTask;
	}

	public java.lang.String getConstructionType() {
		return constructionType;
	}

	public void setConstructionType(java.lang.String constructionType) {
		this.constructionType = constructionType;
	}

	public java.lang.Long getQuantityValue() {
		return quantityValue;
	}

	public void setQuantityValue(java.lang.Long quantityValue) {
		this.quantityValue = quantityValue;
	}

	public java.lang.Long getHshcValue() {
		return hshcValue;
	}

	public void setHshcValue(java.lang.Long hshcValue) {
		this.hshcValue = hshcValue;
	}

	public java.lang.Long getSalaryValue() {
		return salaryValue;
	}

	public void setSalaryValue(java.lang.Long salaryValue) {
		this.salaryValue = salaryValue;
	}

	public Long getBillValue() {
		return billValue;
	}

	public void setBillValue(Long billValue) {
		this.billValue = billValue;
	}

	public java.util.Date getTdslAccomplishedDate() {
		return tdslAccomplishedDate;
	}

	public void setTdslAccomplishedDate(java.util.Date tdslAccomplishedDate) {
		this.tdslAccomplishedDate = tdslAccomplishedDate;
	}

	public Date getTdslConstructing() {
		return tdslConstructing;
	}

	public void setTdslConstructing(Date tdslConstructing) {
		this.tdslConstructing = tdslConstructing;
	}

	public Date getTdslExpectedCompleteDate() {
		return tdslExpectedCompleteDate;
	}

	public void setTdslExpectedCompleteDate(Date tdslExpectedCompleteDate) {
		this.tdslExpectedCompleteDate = tdslExpectedCompleteDate;
	}

	public Date getTdhsTctNotApproval() {
		return tdhsTctNotApproval;
	}

	public void setTdhsTctNotApproval(Date tdhsTctNotApproval) {
		this.tdhsTctNotApproval = tdhsTctNotApproval;
	}

	public Date getTdhsSigningGdcn() {
		return tdhsSigningGdcn;
	}

	public void setTdhsSigningGdcn(Date tdhsSigningGdcn) {
		this.tdhsSigningGdcn = tdhsSigningGdcn;
	}

	public Date getTdhsControl4a() {
		return tdhsControl4a;
	}

	public void setTdhsControl4a(Date tdhsControl4a) {
		this.tdhsControl4a = tdhsControl4a;
	}

	public Date getTdhsPhtApprovaling() {
		return tdhsPhtApprovaling;
	}

	public void setTdhsPhtApprovaling(Date tdhsPhtApprovaling) {
		this.tdhsPhtApprovaling = tdhsPhtApprovaling;
	}

	public Date getTdhsPhtAcceptancing() {
		return tdhsPhtAcceptancing;
	}

	public void setTdhsPhtAcceptancing(Date tdhsPhtAcceptancing) {
		this.tdhsPhtAcceptancing = tdhsPhtAcceptancing;
	}

	public Date getTdhsTtktProfile() {
		return tdhsTtktProfile;
	}

	public void setTdhsTtktProfile(Date tdhsTtktProfile) {
		this.tdhsTtktProfile = tdhsTtktProfile;
	}

	public Date getTdhsExpectedCompleteDate() {
		return tdhsExpectedCompleteDate;
	}

	public void setTdhsExpectedCompleteDate(Date tdhsExpectedCompleteDate) {
		this.tdhsExpectedCompleteDate = tdhsExpectedCompleteDate;
	}

	public Date getTdttCollectMoneyDate() {
		return tdttCollectMoneyDate;
	}

	public void setTdttCollectMoneyDate(Date tdttCollectMoneyDate) {
		this.tdttCollectMoneyDate = tdttCollectMoneyDate;
	}

	public Date getTdttProfilePht() {
		return tdttProfilePht;
	}

	public void setTdttProfilePht(Date tdttProfilePht) {
		this.tdttProfilePht = tdttProfilePht;
	}

	public Date getTdttProfilePtc() {
		return tdttProfilePtc;
	}

	public void setTdttProfilePtc(Date tdttProfilePtc) {
		this.tdttProfilePtc = tdttProfilePtc;
	}

	public Date getTdttExpectedCompleteDate() {
		return tdttExpectedCompleteDate;
	}

	public void setTdttExpectedCompleteDate(Date tdttExpectedCompleteDate) {
		this.tdttExpectedCompleteDate = tdttExpectedCompleteDate;
	}

	@Override
	public ProgressTaskOsDTO toDTO() {
		ProgressTaskOsDTO progressTaskOsDTO = new ProgressTaskOsDTO();
		progressTaskOsDTO.setProgressTaskOsId(this.getProgressTaskOsId());
		progressTaskOsDTO.setConstructionCode(this.getConstructionCode());
		progressTaskOsDTO.setCntContractCode(this.getCntContractCode());
		progressTaskOsDTO.setTtkv(this.getTtkv());
		progressTaskOsDTO.setTtkt(this.getTtkt());
		progressTaskOsDTO.setDescription(this.getDescription());
		progressTaskOsDTO.setSourceTask(this.getSourceTask());
		progressTaskOsDTO.setConstructionType(this.getConstructionType());
		progressTaskOsDTO.setQuantityValue(this.getQuantityValue());
		progressTaskOsDTO.setHshcValue(this.getHshcValue());
		progressTaskOsDTO.setSalaryValue(this.getSalaryValue());
		progressTaskOsDTO.setBillValue(this.getBillValue());
		progressTaskOsDTO.setTdslAccomplishedDate(this.getTdslAccomplishedDate());
		progressTaskOsDTO.setTdslConstructing(this.getTdslConstructing());
		progressTaskOsDTO.setTdslExpectedCompleteDate(this.getTdslExpectedCompleteDate());
		progressTaskOsDTO.setTdhsTctNotApproval(this.getTdhsTctNotApproval());
		progressTaskOsDTO.setTdhsSigningGdcn(this.getTdhsSigningGdcn());
		progressTaskOsDTO.setTdhsControl4a(this.getTdhsControl4a());
		progressTaskOsDTO.setTdhsPhtApprovaling(this.getTdhsPhtApprovaling());
		progressTaskOsDTO.setTdhsPhtAcceptancing(this.getTdhsPhtAcceptancing());
		progressTaskOsDTO.setTdhsTtktProfile(this.getTdhsTtktProfile());
		progressTaskOsDTO.setTdhsExpectedCompleteDate(this.getTdhsExpectedCompleteDate());
		progressTaskOsDTO.setTdttCollectMoneyDate(this.getTdttCollectMoneyDate());
		progressTaskOsDTO.setTdttProfilePht(this.getTdttProfilePht());
		progressTaskOsDTO.setTdttProfilePtc(this.getTdttProfilePtc());
		progressTaskOsDTO.setTdttExpectedCompleteDate(this.getTdttExpectedCompleteDate());
		progressTaskOsDTO.setCreatedUserId(this.getCreatedUserId());
		progressTaskOsDTO.setCreatedDate(this.getCreatedDate());
		progressTaskOsDTO.setUpdatedUserId(this.getUpdatedUserId());
		progressTaskOsDTO.setUpdatedDate(this.getUpdatedDate());
		progressTaskOsDTO.setStatus(this.getStatus());
		progressTaskOsDTO.setMonthYear(this.getMonthYear());
		progressTaskOsDTO.setCheckHtct(this.getCheckHtct());
		progressTaskOsDTO.setApproveRevenueDate(this.approveRevenueDate);
		progressTaskOsDTO.setApproveCompleteDate(this.approveCompleteDate);
		progressTaskOsDTO.setTtktId(this.ttktId);
		progressTaskOsDTO.setWorkItemName(this.workItemName);
		progressTaskOsDTO.setType(this.type);
		return progressTaskOsDTO;
	}
}
