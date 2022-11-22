package com.viettel.coms.dto;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.ProgressPlanProjectBO;
import com.viettel.erp.utils.CustomJsonDateDeserializer;
import com.viettel.erp.utils.CustomJsonDateSerializer;

@XmlRootElement(name = "PROGRESS_PLAN_PROJECTBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProgressPlanProjectDTO extends ComsBaseFWDTO<ProgressPlanProjectBO> {

	private Long progressPlanProjectId;
	private Long provinceId;
	private String provinceCode;
	private String areaCode;
	private String districtCode;
	private String communeCode;
	private String projectName;
	private String address;
	private String investorName;
	private String projectPerformance;
	private String projectType;
	private Long numberHouse;
	private Long numberBlock;
	private Double acreage;
	private String progressProject;
	private String deploymentMobile;
	private Long createUserId;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date createDate;
	private Long updateUserId;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date updateDate;
	private String status;
	private String contactCus;
	private String positionCus;
	private String phoneNumberCus;
	private String emailCus;
	private String contactEmploy;
	private String phoneNumberEmploy;
	private String emailEmploy;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date deadlineDateComplete;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date dateExposed;
	private String levelDeployment;
	private String contractingStatus;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date dateContract;
	private Long contractId;
	private String note;

	public Long getProgressPlanProjectId() {
		return progressPlanProjectId;
	}

	public void setProgressPlanProjectId(Long progressPlanProjectId) {
		this.progressPlanProjectId = progressPlanProjectId;
	}

	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getCommuneCode() {
		return communeCode;
	}

	public void setCommuneCode(String communeCode) {
		this.communeCode = communeCode;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getInvestorName() {
		return investorName;
	}

	public void setInvestorName(String investorName) {
		this.investorName = investorName;
	}

	public String getProjectPerformance() {
		return projectPerformance;
	}

	public void setProjectPerformance(String projectPerformance) {
		this.projectPerformance = projectPerformance;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public Long getNumberHouse() {
		return numberHouse;
	}

	public void setNumberHouse(Long numberHouse) {
		this.numberHouse = numberHouse;
	}

	public Long getNumberBlock() {
		return numberBlock;
	}

	public void setNumberBlock(Long numberBlock) {
		this.numberBlock = numberBlock;
	}

	public Double getAcreage() {
		return acreage;
	}

	public void setAcreage(Double acreage) {
		this.acreage = acreage;
	}

	public String getProgressProject() {
		return progressProject;
	}

	public void setProgressProject(String progressProject) {
		this.progressProject = progressProject;
	}

	public String getDeploymentMobile() {
		return deploymentMobile;
	}

	public void setDeploymentMobile(String deploymentMobile) {
		this.deploymentMobile = deploymentMobile;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getContactCus() {
		return contactCus;
	}

	public void setContactCus(String contactCus) {
		this.contactCus = contactCus;
	}

	public String getPositionCus() {
		return positionCus;
	}

	public void setPositionCus(String positionCus) {
		this.positionCus = positionCus;
	}

	public String getPhoneNumberCus() {
		return phoneNumberCus;
	}

	public void setPhoneNumberCus(String phoneNumberCus) {
		this.phoneNumberCus = phoneNumberCus;
	}

	public String getEmailCus() {
		return emailCus;
	}

	public void setEmailCus(String emailCus) {
		this.emailCus = emailCus;
	}

	public String getContactEmploy() {
		return contactEmploy;
	}

	public void setContactEmploy(String contactEmploy) {
		this.contactEmploy = contactEmploy;
	}

	public String getPhoneNumberEmploy() {
		return phoneNumberEmploy;
	}

	public void setPhoneNumberEmploy(String phoneNumberEmploy) {
		this.phoneNumberEmploy = phoneNumberEmploy;
	}

	public String getEmailEmploy() {
		return emailEmploy;
	}

	public void setEmailEmploy(String emailEmploy) {
		this.emailEmploy = emailEmploy;
	}

	public Date getDeadlineDateComplete() {
		return deadlineDateComplete;
	}

	public void setDeadlineDateComplete(Date deadlineDateComplete) {
		this.deadlineDateComplete = deadlineDateComplete;
	}

	public Date getDateExposed() {
		return dateExposed;
	}

	public void setDateExposed(Date dateExposed) {
		this.dateExposed = dateExposed;
	}

	public String getLevelDeployment() {
		return levelDeployment;
	}

	public void setLevelDeployment(String levelDeployment) {
		this.levelDeployment = levelDeployment;
	}

	public String getContractingStatus() {
		return contractingStatus;
	}

	public void setContractingStatus(String contractingStatus) {
		this.contractingStatus = contractingStatus;
	}

	public Date getDateContract() {
		return dateContract;
	}

	public void setDateContract(Date dateContract) {
		this.dateContract = dateContract;
	}

	public Long getContractId() {
		return contractId;
	}

	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public String catchName() {
		// TODO Auto-generated method stub
		return progressPlanProjectId.toString();
	}

	@Override
	public Long getFWModelId() {
		// TODO Auto-generated method stub
		return progressPlanProjectId;
	}

	@Override
	public ProgressPlanProjectBO toModel() {
		ProgressPlanProjectBO bo = new ProgressPlanProjectBO();
		bo.setProgressPlanProjectId(this.getProgressPlanProjectId());
		bo.setProvinceId(this.getProvinceId());
		bo.setProvinceCode(this.getProvinceCode());
		bo.setAreaCode(this.getAreaCode());
		bo.setDistrictCode(this.getDistrictCode());
		bo.setCommuneCode(this.getCommuneCode());
		bo.setProjectName(this.getProjectName());
		bo.setAddress(this.getAddress());
		bo.setInvestorName(this.getInvestorName());
		bo.setProjectPerformance(this.getProjectPerformance());
		bo.setProjectType(this.getProjectType());
		bo.setNumberHouse(this.getNumberHouse());
		bo.setNumberBlock(this.getNumberBlock());
		bo.setAcreage(this.getAcreage());
		bo.setProgressProject(this.getProgressProject());
		bo.setDeploymentMobile(this.getDeploymentMobile());
		bo.setCreateUserId(this.getCreateUserId());
		bo.setCreateDate(this.getCreateDate());
		bo.setUpdateUserId(this.getUpdateUserId());
		bo.setUpdateDate(this.getUpdateDate());
		bo.setStatus(this.getStatus());
		bo.setContactCus(this.getContactCus());
		bo.setPositionCus(this.getPositionCus());
		bo.setPhoneNumberCus(this.getPhoneNumberCus());
		bo.setEmailCus(this.getEmailCus());
		bo.setContactEmploy(this.getContactEmploy());
		bo.setPhoneNumberEmploy(this.getPhoneNumberEmploy());
		bo.setEmailEmploy(this.getEmailEmploy());
		bo.setDeadlineDateComplete(this.getDeadlineDateComplete());
		bo.setDateExposed(this.getDateExposed());
		bo.setLevelDeployment(this.getLevelDeployment());
		bo.setContractingStatus(this.getContractingStatus());
		bo.setDateContract(this.getDateContract());
		bo.setContractId(this.getContractId());
		bo.setNote(this.getNote());
		return bo;
	}
	
	private String contractCode;

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}
	
	private List<ProgressPlanProjectDTO> lstDataImport;

	public List<ProgressPlanProjectDTO> getLstDataImport() {
		return lstDataImport;
	}

	public void setLstDataImport(List<ProgressPlanProjectDTO> lstDataImport) {
		this.lstDataImport = lstDataImport;
	}

	private List<String> lstContract;

	public List<String> getLstContract() {
		return lstContract;
	}

	public void setLstContract(List<String> lstContract) {
		this.lstContract = lstContract;
	}
	
	private List<UtilAttachDocumentDTO> listFile;

	public List<UtilAttachDocumentDTO> getListFile() {
		return listFile;
	}

	public void setListFile(List<UtilAttachDocumentDTO> listFile) {
		this.listFile = listFile;
	}
	
}
