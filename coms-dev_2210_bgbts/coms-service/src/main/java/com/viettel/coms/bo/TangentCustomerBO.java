package com.viettel.coms.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.TangentCustomerDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

@Entity
@Table(name = "TANGENT_CUSTOMER")
public class TangentCustomerBO extends BaseFWModelImpl {

	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "TANGENT_CUSTOMER_SEQ") })
	@Column(name = "TANGENT_CUSTOMER_ID", length = 10)
	private Long tangentCustomerId;
	@Column(name = "CUSTOMER_NAME", length = 500)
	private String customerName;
	@Column(name = "BIRTH_YEAR", length = 5)
	private String birthYear;
	@Column(name = "ADDRESS", length = 1000)
	private String address;
	@Column(name = "AREA_ID", length = 10)
	private Long areaId;
	@Column(name = "PROVINCE_ID", length = 10)
	private Long provinceId;
	@Column(name = "PROVINCE_CODE", length = 50)
	private String provinceCode;
	@Column(name = "CUSTOMER_PHONE", length = 50)
	private String customerPhone;
	@Column(name = "CUSTOMER_EMAIL", length = 200)
	private String customerEmail;
	@Column(name = "SUGGEST_TIME", length = 22)
	private Date suggestTime;
	@Column(name = "PERFORMER_ID", length = 10)
	private Long performerId;
	@Column(name = "PERFORMER_SOLUTION_ID", length = 10)
	private Long performerSolutionId;
	@Column(name = "CONTENT_CUSTOMER", length = 2000)
	private String contentCustomer;
	@Column(name = "STATUS", length = 5)
	private String status;
	@Column(name = "CREATED_USER", length = 10)
	private Long createdUser;
	@Column(name = "CREATED_DATE", length = 22)
	private Date createdDate;
	@Column(name = "UPDATED_USER", length = 10)
	private Long updatedUser;
	@Column(name = "UPDATED_DATE", length = 22)
	private Date updatedDate;
	@Column(name = "DISTRICT_NAME", length = 200)
	private String districtName;
	@Column(name = "COMMUNE_NAME", length = 200)
	private String communeName;
	@Column(name = "APARTMENT_NUMBER", length = 200)
	private String apartmentNumber;
	@Column(name = "APPOVED_DATE", length = 200)
	private Date approvedDate;
	@Column(name = "USER_APPROVED_ID", length = 200)
	private Long userApprovedId;
	@Column(name = "CRM_ID", length = 200)
	private Long crmId;
	//duonghv13 add 25122021
	@Column(name = "SOURCE", length = 1)
	private String source;
	
	//duonghv13 add 11022022
	@Column(name = "RECEPTION_CHANNEL", length = 200)
	private String receptionChannel;
	@Column(name = "CUSTOMER_RESOURCES", length = 200)
	private String customerResources;
	@Column(name = "PARTNER_TYPE", length = 1)
	private Long partnerType;
	@Column(name = "PARTNER_CODE", length = 50)
	private String partnerCode;
	@Column(name = "GROUP_ORDER", length = 1)
	private Long groupOrder;
	@Column(name = "ESTIMATED_CONSTRUCTION_TIME", length = 200)
	private Date estimatedConstructionTime;
	@Column(name = "ESTIMATED_BUDGET", length = 1)
	private String estimatedBudget;
	@Column(name = "REGISTERED_CUSTOMER_SERVICE", length = 50)
	private String registeredCustomerService;
	@Column(name = "MODEL_OF_THE_BUILDER", length = 1)
	private String modelOfTheBuilder;
	
	
	public String getApartmentNumber() {
		return apartmentNumber;
	}

	public void setApartmentNumber(String apartmentNumber) {
		this.apartmentNumber = apartmentNumber;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getCommuneName() {
		return communeName;
	}

	public void setCommuneName(String communeName) {
		this.communeName = communeName;
	}

	public Long getTangentCustomerId() {
		return tangentCustomerId;
	}

	public void setTangentCustomerId(Long tangentCustomerId) {
		this.tangentCustomerId = tangentCustomerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getBirthYear() {
		return birthYear;
	}

	public void setBirthYear(String birthYear) {
		this.birthYear = birthYear;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
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

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public Date getSuggestTime() {
		return suggestTime;
	}

	public void setSuggestTime(Date suggestTime) {
		this.suggestTime = suggestTime;
	}

	public Long getPerformerId() {
		return performerId;
	}

	public void setPerformerId(Long performerId) {
		this.performerId = performerId;
	}

	public Long getPerformerSolutionId() {
		return performerSolutionId;
	}

	public void setPerformerSolutionId(Long performerSolutionId) {
		this.performerSolutionId = performerSolutionId;
	}

	public String getContentCustomer() {
		return contentCustomer;
	}

	public void setContentCustomer(String contentCustomer) {
		this.contentCustomer = contentCustomer;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(Long createdUser) {
		this.createdUser = createdUser;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Long getUpdatedUser() {
		return updatedUser;
	}

	public void setUpdatedUser(Long updatedUser) {
		this.updatedUser = updatedUser;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Long getCrmId() {
		return crmId;
	}

	public void setCrmId(Long crmId) {
		this.crmId = crmId;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public Long getUserApprovedId() {
		return userApprovedId;
	}

	public void setUserApprovedId(Long userApprovedId) {
		this.userApprovedId = userApprovedId;
	}
	
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	public String getReceptionChannel() {
		return receptionChannel;
	}

	public void setReceptionChannel(String receptionChannel) {
		this.receptionChannel = receptionChannel;
	}

	public String getCustomerResources() {
		return customerResources;
	}

	public void setCustomerResources(String customerResources) {
		this.customerResources = customerResources;
	}
	
	public Long getPartnerType() {
		return partnerType;
	}

	public void setPartnerType(Long partnerType) {
		this.partnerType = partnerType;
	}
	
	public String getPartnerCode() {
		return partnerCode;
	}

	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}

	public Long getGroupOrder() {
		return groupOrder;
	}

	public void setGroupOrder(Long groupOrder) {
		this.groupOrder = groupOrder;
	}
	
	public Date getEstimatedConstructionTime() {
		return estimatedConstructionTime;
	}

	public void setEstimatedConstructionTime(Date estimatedConstructionTime) {
		this.estimatedConstructionTime = estimatedConstructionTime;
	}

	public String getEstimatedBudget() {
		return estimatedBudget;
	}

	public void setEstimatedBudget(String estimatedBudget) {
		this.estimatedBudget = estimatedBudget;
	}

	public String getRegisteredCustomerService() {
		return registeredCustomerService;
	}

	public void setRegisteredCustomerService(String registeredCustomerService) {
		this.registeredCustomerService = registeredCustomerService;
	}

	public String getModelOfTheBuilder() {
		return modelOfTheBuilder;
	}

	public void setModelOfTheBuilder(String modelOfTheBuilder) {
		this.modelOfTheBuilder = modelOfTheBuilder;
	}

	@Override
	public BaseFWDTOImpl toDTO() {
		TangentCustomerDTO dto = new TangentCustomerDTO();
		dto.setTangentCustomerId(this.getTangentCustomerId());
		dto.setCustomerName(this.getCustomerName());
		dto.setBirthYear(this.getBirthYear());
		dto.setAddress(this.getAddress());
		dto.setAreaId(this.getAreaId());
		dto.setProvinceId(this.getProvinceId());
		dto.setProvinceCode(this.getProvinceCode());
		dto.setCustomerPhone(this.getCustomerPhone());
		dto.setCustomerEmail(this.getCustomerEmail());
		dto.setSuggestTime(this.getSuggestTime());
		dto.setPerformerId(this.getPerformerId());
		dto.setPerformerSolutionId(this.getPerformerSolutionId());
		dto.setContentCustomer(this.getContentCustomer());
		dto.setStatus(this.getStatus());
		dto.setCreatedUser(this.getCreatedUser());
		dto.setCreatedDate(this.getCreatedDate());
		dto.setUpdatedUser(this.getUpdatedUser());
		dto.setUpdatedDate(this.getUpdatedDate());
		dto.setDistrictName(this.getDistrictName());
		dto.setCommuneName(this.getCommuneName());
		dto.setApartmentNumber(this.getApartmentNumber());
		dto.setCrmId(this.getCrmId());
		dto.setApprovedDate(this.getApprovedDate());
		dto.setUserApprovedId(this.getUserApprovedId());
		//duonghv13 add 25122021
		dto.setSource(this.getSource());
		//duonghv13 add 11022022
		dto.setReceptionChannel(this.getReceptionChannel());
		dto.setCustomerResources(this.getCustomerResources());
		dto.setPartnerType(this.getPartnerType());
		dto.setPartnerCode(this.getPartnerCode());
		dto.setGroupOrder(this.getGroupOrder());
		dto.setEstimatedConstructionTime(this.estimatedConstructionTime);
		dto.setEstimatedBudget(this.estimatedBudget);
		dto.setRegisteredCustomerService(this.registeredCustomerService);
		dto.setModelOfTheBuilder(this.modelOfTheBuilder);
		return dto;
	}

	
}
