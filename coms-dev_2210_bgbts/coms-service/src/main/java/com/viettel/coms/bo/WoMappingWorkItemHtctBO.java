package com.viettel.coms.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.WoMappingWorkItemHtctDTO;
import com.viettel.service.base.model.BaseFWModelImpl;

@Entity
@Table(name = "WO_MAPPING_WORK_ITEM_HTCT")
public class WoMappingWorkItemHtctBO extends BaseFWModelImpl {

	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "WO_MAPPING_WORK_ITEM_HTCT_SEQ") })
	@Column(name = "WO_MAPPING_WORK_ITEM_HTCT_ID")
	private Long woMappingWorkItemHtctId;
	@Column(name = "WO_ID")
	private Long woId;
	@Column(name = "WO_CODE")
	private String woCode;
	@Column(name = "CONTRACT_ID")
	private Long contractId;
	@Column(name = "CONTRACT_CODE")
	private String contractCode;
	@Column(name = "CONSTRUCTION_ID")
	private Long constructionId;
	@Column(name = "CONSTRUCTION_CODE")
	private String constructionCode;
	@Column(name = "WORK_ITEM_ID")
	private Long workItemId;
	@Column(name = "WORK_ITEM_NAME")
	private String workItemName;
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	@Column(name = "CREATED_USER_ID")
	private Long createdUserId;
	@Column(name = "WORK_ITEM_VALUE")
	private Double workItemValue;
	@Column(name = "STATUS")
	private String status;
	@Column(name = "COMPLETED_DATE")
	private Date completedDate;
	@Column(name = "TR_ID")
	private Long trId;
	@Column(name = "TR_CODE")
	private String trCode;
	
	public Long getWoMappingWorkItemHtctId() {
		return woMappingWorkItemHtctId;
	}

	public void setWoMappingWorkItemHtctId(Long woMappingWorkItemHtctId) {
		this.woMappingWorkItemHtctId = woMappingWorkItemHtctId;
	}

	public Long getWoId() {
		return woId;
	}

	public void setWoId(Long woId) {
		this.woId = woId;
	}

	public String getWoCode() {
		return woCode;
	}

	public void setWoCode(String woCode) {
		this.woCode = woCode;
	}

	public Long getContractId() {
		return contractId;
	}

	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public Long getConstructionId() {
		return constructionId;
	}

	public void setConstructionId(Long constructionId) {
		this.constructionId = constructionId;
	}

	public String getConstructionCode() {
		return constructionCode;
	}

	public void setConstructionCode(String constructionCode) {
		this.constructionCode = constructionCode;
	}

	public Long getWorkItemId() {
		return workItemId;
	}

	public void setWorkItemId(Long workItemId) {
		this.workItemId = workItemId;
	}

	public String getWorkItemName() {
		return workItemName;
	}

	public void setWorkItemName(String workItemName) {
		this.workItemName = workItemName;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Long getCreatedUserId() {
		return createdUserId;
	}

	public void setCreatedUserId(Long createdUserId) {
		this.createdUserId = createdUserId;
	}

	public Double getWorkItemValue() {
		return workItemValue;
	}

	public void setWorkItemValue(Double workItemValue) {
		this.workItemValue = workItemValue;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}

	public Long getTrId() {
		return trId;
	}

	public void setTrId(Long trId) {
		this.trId = trId;
	}

	public String getTrCode() {
		return trCode;
	}

	public void setTrCode(String trCode) {
		this.trCode = trCode;
	}

	@Override
	public WoMappingWorkItemHtctDTO toDTO() {
		WoMappingWorkItemHtctDTO dto = new WoMappingWorkItemHtctDTO();
		dto.setWoMappingWorkItemHtctId(this.getWoMappingWorkItemHtctId());
		dto.setWoId(this.getWoId());
		dto.setWoCode(this.getWoCode());
		dto.setContractId(this.getContractId());
		dto.setContractCode(this.getContractCode());
		dto.setConstructionId(this.getConstructionId());
		dto.setConstructionCode(this.getConstructionCode());
		dto.setWorkItemId(this.getWorkItemId());
		dto.setWorkItemName(this.getWorkItemName());
		dto.setCreatedUserId(this.createdUserId);
		dto.setCreatedDate(this.createdDate);
		dto.setWorkItemValue(this.workItemValue);
		dto.setStatus(this.getStatus());
		dto.setCompletedDate(this.getCompletedDate());
		dto.setTrId(this.getTrId());
		dto.setTrCode(this.getTrCode());
		return dto;
	}

}
