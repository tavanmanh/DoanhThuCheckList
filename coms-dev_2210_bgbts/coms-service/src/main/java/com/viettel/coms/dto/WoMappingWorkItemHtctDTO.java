package com.viettel.coms.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.viettel.coms.bo.WoMappingWorkItemHtctBO;

@XmlRootElement(name = "WO_MAPPING_WORK_ITEM_HTCTBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class WoMappingWorkItemHtctDTO extends ComsBaseFWDTO<WoMappingWorkItemHtctBO> {

	private Long woMappingWorkItemHtctId;
	private Long woId;
	private String woCode;
	private Long contractId;
	private String contractCode;
	private Long constructionId;
	private String constructionCode;
	private Long workItemId;
	private String workItemName;
	private Date createdDate;
	private Long createdUserId;
	private Double workItemValue;
	private String status;
	private Date completedDate;
	private Long trId;
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
	public String catchName() {
		return woMappingWorkItemHtctId.toString();
	}

	@Override
	public Long getFWModelId() {
		// TODO Auto-generated method stub
		return woMappingWorkItemHtctId;
	}

	@Override
	public WoMappingWorkItemHtctBO toModel() {
		WoMappingWorkItemHtctBO bo = new WoMappingWorkItemHtctBO();
		bo.setWoMappingWorkItemHtctId(this.getWoMappingWorkItemHtctId());
		bo.setWoId(this.getWoId());
		bo.setWoCode(this.getWoCode());
		bo.setContractId(this.getContractId());
		bo.setContractCode(this.getContractCode());
		bo.setConstructionId(this.getConstructionId());
		bo.setConstructionCode(this.getConstructionCode());
		bo.setWorkItemId(this.getWorkItemId());
		bo.setWorkItemName(this.getWorkItemName());
		bo.setCreatedUserId(this.createdUserId);
		bo.setCreatedDate(this.createdDate);
		bo.setWorkItemValue(this.workItemValue);
		bo.setStatus(this.getStatus());
		bo.setCompletedDate(this.getCompletedDate());
		bo.setTrId(this.getTrId());
		bo.setTrCode(this.getTrCode());
		return bo;
	}

}
