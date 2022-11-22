package com.viettel.coms.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.ProgressPlanProjectDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

@Entity
@Table(name = "PROGRESS_PLAN_PROJECT")
public class ProgressPlanProjectBO extends BaseFWModelImpl {

	@Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "PROGRESS_PLAN_PROJECT_SEQ")})
	@Column(name = "PROGRESS_PLAN_PROJECT_ID", length = 10)
	private Long progressPlanProjectId;
	@Column(name = "PROVINCE_ID", length = 10)
	private Long provinceId;
	@Column(name = "PROVINCE_CODE", length = 50)
	private String provinceCode;
	@Column(name = "AREA_CODE", length = 50)
	private String areaCode;
	@Column(name = "DISTRICT_CODE", length = 500)
	private String districtCode;
	@Column(name = "COMMUNE_CODE", length = 500)
	private String communeCode;
	@Column(name = "PROJECT_NAME", length = 500)
	private String projectName;
	@Column(name = "ADDRESS", length = 500)
	private String address;
	@Column(name = "INVESTOR_NAME", length = 500)
	private String investorName;
	@Column(name = "PROJECT_PERFORMANCE", length = 500)
	private String projectPerformance;
	@Column(name = "PROJECT_TYPE", length = 500)
	private String projectType;
	@Column(name = "NUMBER_HOUSE", length = 10)
	private Long numberHouse;
	@Column(name = "NUMBER_BLOCK", length = 10)
	private Long numberBlock;
	@Column(name = "ACREAGE", length = 20)
	private Double acreage;
	@Column(name = "PROGRESS_PROJECT", length = 500)
	private String progressProject;
	@Column(name = "DEPLOYMENT_MOBILE", length = 50)
	private String deploymentMobile;
	@Column(name = "CREATE_USER_ID", length = 10)
	private Long createUserId;
	@Column(name = "CREATE_DATE", length = 22)
	private Date createDate;
	@Column(name = "UPDATE_USER_ID", length = 10)
	private Long updateUserId;
	@Column(name = "UPDATE_DATE", length = 22)
	private Date updateDate;
	@Column(name = "STATUS", length = 2)
	private String status;
	@Column(name = "CONTACT_CUS", length = 1000)
	private String contactCus;
	@Column(name = "POSITION_CUS", length = 1000)
	private String positionCus;
	@Column(name = "PHONE_NUMBER_CUS", length = 100)
	private String phoneNumberCus;
	@Column(name = "EMAIL_CUS", length = 100)
	private String emailCus;
	@Column(name = "CONTACT_EMPLOY", length = 1000)
	private String contactEmploy;
	@Column(name = "PHONE_NUMBER_EMPLOY", length = 100)
	private String phoneNumberEmploy;
	@Column(name = "EMAIL_EMPLOY", length = 100)
	private String emailEmploy;
	@Column(name = "DEADLINE_DATE_COMPLETE", length = 22)
	private Date deadlineDateComplete;
	@Column(name = "DATE_EXPOSED", length = 22)
	private Date dateExposed;
	@Column(name = "LEVEL_DEPLOYMENT", length = 2)
	private String levelDeployment;
	@Column(name = "CONTRACTING_STATUS", length = 2)
	private String contractingStatus;
	@Column(name = "DATE_CONTRACT", length = 22)
	private Date dateContract;
	@Column(name = "CONTRACT_ID", length = 10)
	private Long contractId;
	@Column(name = "NOTE", length = 1000)
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
	public BaseFWDTOImpl toDTO() {
		ProgressPlanProjectDTO dto = new ProgressPlanProjectDTO();
		dto.setProgressPlanProjectId(this.getProgressPlanProjectId());
		dto.setProvinceId(this.getProvinceId());
		dto.setProvinceCode(this.getProvinceCode());
		dto.setAreaCode(this.getAreaCode());
		dto.setDistrictCode(this.getDistrictCode());
		dto.setCommuneCode(this.getCommuneCode());
		dto.setProjectName(this.getProjectName());
		dto.setAddress(this.getAddress());
		dto.setInvestorName(this.getInvestorName());
		dto.setProjectPerformance(this.getProjectPerformance());
		dto.setProjectType(this.getProjectType());
		dto.setNumberHouse(this.getNumberHouse());
		dto.setNumberBlock(this.getNumberBlock());
		dto.setAcreage(this.getAcreage());
		dto.setProgressProject(this.getProgressProject());
		dto.setDeploymentMobile(this.getDeploymentMobile());
		dto.setCreateUserId(this.getCreateUserId());
		dto.setCreateDate(this.getCreateDate());
		dto.setUpdateUserId(this.getUpdateUserId());
		dto.setUpdateDate(this.getUpdateDate());
		dto.setStatus(this.getStatus());
		dto.setContactCus(this.getContactCus());
		dto.setPositionCus(this.getPositionCus());
		dto.setPhoneNumberCus(this.getPhoneNumberCus());
		dto.setEmailCus(this.getEmailCus());
		dto.setContactEmploy(this.getContactEmploy());
		dto.setPhoneNumberEmploy(this.getPhoneNumberEmploy());
		dto.setEmailEmploy(this.getEmailEmploy());
		dto.setDeadlineDateComplete(this.getDeadlineDateComplete());
		dto.setDateExposed(this.getDateExposed());
		dto.setLevelDeployment(this.getLevelDeployment());
		dto.setContractingStatus(this.getContractingStatus());
		dto.setDateContract(this.getDateContract());
		dto.setContractId(this.getContractId());
		dto.setNote(this.getNote());
		return dto;
	}

}
