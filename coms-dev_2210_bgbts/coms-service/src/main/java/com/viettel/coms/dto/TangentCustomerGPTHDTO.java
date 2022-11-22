package com.viettel.coms.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.viettel.coms.bo.TangentCustomerGPTHBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;

@XmlRootElement(name = "")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TangentCustomerGPTHDTO extends ComsBaseFWDTO<TangentCustomerGPTHBO>{

	private Long tangentCustomerGPTHId;
	private String customerName;
	private String bciCode;
	private String address;
	private Long areaId;
	private Long provinceId;
	private String provinceCode;
	private String provinceName;
	private String customerPhone;
	private String customerEmail;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date suggestTime;
	private Long performerId;
	private Long performerSolutionId;
	private String contentCustomer;
	private String status;
	private Long createdUser;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date createdDate;
	private Long updatedUser;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date updatedDate;
	private String updatedDateDb;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date createdDateFrom;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date createdDateTo;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date suggestTimeFrom;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date suggestTimeTo;
	private String performerName;
	private Long districtId;
	private String districtName;
	private Long communeId;
	private String communeName;
	private String performerSolutionName;
	private String createdDateDb;
	private Long crmId;
	private Date approvedDate;
	private Long userApprovedId;
	private String source;
	private Long overdueStatus;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date nextTangentTime;
	private List<String> lstOverdueStatus;
	@JsonProperty("isInternalSource")
	private Long isInternalSource;
	@JsonProperty("isSocialSource")
	private Long isSocialSource;
	@JsonProperty("isCustomerServiceSource")
	private Long isCustomerServiceSource;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date appointmentDate;
	private String receptionChannel;
	private String customerResources;
	private Long partnerType;
	private Long groupOrder;
	
	private String oldStatus;
	private String oldPhone;
	private String partnerCode;
	private String  userName;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date realityTangentDate;
	private Long sysUserId;
	public static final Map<String, String> listSources;
	static {
		listSources = new HashMap<String, String>();
		listSources.put("1", "Nội bộ");
		listSources.put("2", "Xã hội hóa");
		listSources.put("3", "CSKH");
	}
	private String provinceKeyCode;
//	@JsonSerialize(using = JsonDateSerializerDate.class)
//	@JsonDeserialize(using = JsonDateDeserializer.class)
//	private Date estimatedConstructionTime;
//	private String estimatedBudget;
//	private String registeredCustomerService;
//	private String modelOfTheBuilder;
	public String getCreatedDateDb() {
		return createdDateDb;
	}

	public void setCreatedDateDb(String createdDateDb) {
		this.createdDateDb = createdDateDb;
	}

	public Long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}

	public Long getCommuneId() {
		return communeId;
	}

	public void setCommuneId(Long communeId) {
		this.communeId = communeId;
	}

	public String getPerformerSolutionName() {
		return performerSolutionName;
	}

	public void setPerformerSolutionName(String performerSolutionName) {
		this.performerSolutionName = performerSolutionName;
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

	public String getPerformerName() {
		return performerName;
	}

	public void setPerformerName(String performerName) {
		this.performerName = performerName;
	}

	private List<String> lstStatus;
	
	public List<String> getLstStatus() {
		return lstStatus;
	}

	public void setLstStatus(List<String> lstStatus) {
		this.lstStatus = lstStatus;
	}

	public Date getCreatedDateFrom() {
		return createdDateFrom;
	}

	public void setCreatedDateFrom(Date createdDateFrom) {
		this.createdDateFrom = createdDateFrom;
	}

	public Date getCreatedDateTo() {
		return createdDateTo;
	}

	public void setCreatedDateTo(Date createdDateTo) {
		this.createdDateTo = createdDateTo;
	}

	public Date getSuggestTimeFrom() {
		return suggestTimeFrom;
	}

	public void setSuggestTimeFrom(Date suggestTimeFrom) {
		this.suggestTimeFrom = suggestTimeFrom;
	}

	public Date getSuggestTimeTo() {
		return suggestTimeTo;
	}

	public void setSuggestTimeTo(Date suggestTimeTo) {
		this.suggestTimeTo = suggestTimeTo;
	}

	public Long getTangentCustomerGPTHId() {
		return tangentCustomerGPTHId;
	}

	public void setTangentCustomerGPTHId(Long tangentCustomerGPTHId) {
		this.tangentCustomerGPTHId = tangentCustomerGPTHId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getBciCode() {
		return bciCode;
	}

	public void setBciCode(String bciCode) {
		this.bciCode = bciCode;
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

	@Override
	public String catchName() {
		// TODO Auto-generated method stub
		return tangentCustomerGPTHId.toString();
	}

	@Override
	public Long getFWModelId() {
		// TODO Auto-generated method stub
		return tangentCustomerGPTHId;
	}

	@Override
	public TangentCustomerGPTHBO toModel() {
		TangentCustomerGPTHBO bo = new TangentCustomerGPTHBO();
		bo.setTangentCustomerGPTHId(this.getTangentCustomerGPTHId());
		bo.setCustomerName(this.getCustomerName());
		bo.setBciCode(this.getBciCode());
		bo.setAddress(this.getAddress());
		bo.setAreaId(this.getAreaId());
		bo.setProvinceId(this.getProvinceId());
		bo.setProvinceCode(this.getProvinceCode());
		bo.setCustomerPhone(this.getCustomerPhone());
		bo.setCustomerEmail(this.getCustomerEmail());
		bo.setSuggestTime(this.getSuggestTime());
		bo.setPerformerId(this.getPerformerId());
		bo.setPerformerSolutionId(this.getPerformerSolutionId());
		bo.setContentCustomer(this.getContentCustomer());
		bo.setStatus(this.getStatus());
		bo.setCreatedUser(this.getCreatedUser());
		bo.setCreatedDate(this.getCreatedDate());
		bo.setUpdatedUser(this.getUpdatedUser());
		bo.setUpdatedDate(this.getUpdatedDate());
		bo.setDistrictName(this.getDistrictName());
		bo.setCommuneName(this.getCommuneName());
		bo.setApartmentNumber(this.getApartmentNumber());
		bo.setCrmId(this.getCrmId());
		bo.setApprovedDate(this.getApprovedDate());
		bo.setUserApprovedId(this.getUserApprovedId());
		//duonghv13 add 25122021
		bo.setSource(this.getSource());
		//duonghv13 add 11022022
		bo.setReceptionChannel(this.getReceptionChannel());
		bo.setCustomerResources(this.getCustomerResources());
		bo.setPartnerType(this.getPartnerType());
		bo.setPartnerCode(this.getPartnerCode());
		bo.setGroupOrder(this.getGroupOrder());
//		bo.setEstimatedConstructionTime(this.estimatedConstructionTime);
//		bo.setEstimatedBudget(this.estimatedBudget);
//		bo.setRegisteredCustomerService(this.registeredCustomerService);
//		bo.setModelOfTheBuilder(this.modelOfTheBuilder);
		return bo;
	}
	
	private String performerCode;
	private String performerSolutionCode;

	public String getPerformerCode() {
		return performerCode;
	}

	public void setPerformerCode(String performerCode) {
		this.performerCode = performerCode;
	}

	public String getPerformerSolutionCode() {
		return performerSolutionCode;
	}

	public void setPerformerSolutionCode(String performerSolutionCode) {
		this.performerSolutionCode = performerSolutionCode;
	}
	
	private ResultTangentGPTHDTO resultTangentGPTHDTO;
	private DetailTangentCustomerGPTHDTO detailTangentCustomerGPTH;
//	hoanm1_20200612_start
	private Long onTimeKPI;
	private Long overTimeKPI;
	private String createdUserName;
	private String suggestTimeCustomer;
	private String appointmentDateCustomer;
	private String resultTangentType;
	private String reasonRejection;
	private Long resultTangentId;
	private Long statusImage;	
    private Double longtitude;
    private Double latitude;
    private String imageName;
    private String base64String;
    private String imagePath;
    private long utilAttachDocumentId;
    private String presentSolutionDate;
    private String presentSolutionDateCheck;
    private Long resultSolutionId;
    private Long contractId;
    private String contractCode;
    private String signDate;
    private String contractPrice;
    private String resultSolutionType; 
    private String contentResultSolution;
    private String presentSolutionDateNext;    
	private String codeLocation;
	private String nameLocation;
	private String areaLevel;
	private Long parentId;
	private String email;
	private String phoneNumber;
	private String fullNameSolution;
	private String checkCreatedUser;
	private String checkPerformerUser;
	private String createdEmail;
	private String createdPhoneNumber;
	private String guaranteeTime;
	private String constructTime;

	public String getGuaranteeTime() {
		return guaranteeTime;
	}

	public void setGuaranteeTime(String guaranteeTime) {
		this.guaranteeTime = guaranteeTime;
	}

	public String getConstructTime() {
		return constructTime;
	}

	public void setConstructTime(String constructTime) {
		this.constructTime = constructTime;
	}

	public String getCreatedEmail() {
		return createdEmail;
	}

	public void setCreatedEmail(String createdEmail) {
		this.createdEmail = createdEmail;
	}

	public String getCreatedPhoneNumber() {
		return createdPhoneNumber;
	}

	public void setCreatedPhoneNumber(String createdPhoneNumber) {
		this.createdPhoneNumber = createdPhoneNumber;
	}

	public String getCheckCreatedUser() {
		return checkCreatedUser;
	}

	public void setCheckCreatedUser(String checkCreatedUser) {
		this.checkCreatedUser = checkCreatedUser;
	}

	public String getCheckPerformerUser() {
		return checkPerformerUser;
	}

	public void setCheckPerformerUser(String checkPerformerUser) {
		this.checkPerformerUser = checkPerformerUser;
	}

	public String getFullNameSolution() {
		return fullNameSolution;
	}

	public void setFullNameSolution(String fullNameSolution) {
		this.fullNameSolution = fullNameSolution;
	}

	public String getPresentSolutionDateCheck() {
		return presentSolutionDateCheck;
	}

	public void setPresentSolutionDateCheck(String presentSolutionDateCheck) {
		this.presentSolutionDateCheck = presentSolutionDateCheck;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getCodeLocation() {
		return codeLocation;
	}

	public void setCodeLocation(String codeLocation) {
		this.codeLocation = codeLocation;
	}

	public String getNameLocation() {
		return nameLocation;
	}

	public void setNameLocation(String nameLocation) {
		this.nameLocation = nameLocation;
	}

	public String getAreaLevel() {
		return areaLevel;
	}

	public void setAreaLevel(String areaLevel) {
		this.areaLevel = areaLevel;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getPresentSolutionDateNext() {
		return presentSolutionDateNext;
	}

	public void setPresentSolutionDateNext(String presentSolutionDateNext) {
		this.presentSolutionDateNext = presentSolutionDateNext;
	}

	public String getContentResultSolution() {
		return contentResultSolution;
	}

	public void setContentResultSolution(String contentResultSolution) {
		this.contentResultSolution = contentResultSolution;
	}

	public String getResultSolutionType() {
		return resultSolutionType;
	}

	public void setResultSolutionType(String resultSolutionType) {
		this.resultSolutionType = resultSolutionType;
	}

	public String getContractPrice() {
		return contractPrice;
	}

	public void setContractPrice(String contractPrice) {
		this.contractPrice = contractPrice;
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

	public String getSignDate() {
		return signDate;
	}

	public void setSignDate(String signDate) {
		this.signDate = signDate;
	}

	public Long getResultSolutionId() {
		return resultSolutionId;
	}

	public void setResultSolutionId(Long resultSolutionId) {
		this.resultSolutionId = resultSolutionId;
	}

	public String getPresentSolutionDate() {
		return presentSolutionDate;
	}

	public void setPresentSolutionDate(String presentSolutionDate) {
		this.presentSolutionDate = presentSolutionDate;
	}

	public long getUtilAttachDocumentId() {
		return utilAttachDocumentId;
	}

	public void setUtilAttachDocumentId(long utilAttachDocumentId) {
		this.utilAttachDocumentId = utilAttachDocumentId;
	}

	public String getBase64String() {
		return base64String;
	}

	public void setBase64String(String base64String) {
		this.base64String = base64String;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public Double getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(Double longtitude) {
		this.longtitude = longtitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public Long getStatusImage() {
		return statusImage;
	}

	public void setStatusImage(Long statusImage) {
		this.statusImage = statusImage;
	}

	public Long getResultTangentId() {
		return resultTangentId;
	}

	public void setResultTangentId(Long resultTangentId) {
		this.resultTangentId = resultTangentId;
	}

	public String getResultTangentType() {
		return resultTangentType;
	}

	public void setResultTangentType(String resultTangentType) {
		this.resultTangentType = resultTangentType;
	}

	public String getReasonRejection() {
		return reasonRejection;
	}

	public void setReasonRejection(String reasonRejection) {
		this.reasonRejection = reasonRejection;
	}

	public String getAppointmentDateCustomer() {
		return appointmentDateCustomer;
	}

	public void setAppointmentDateCustomer(String appointmentDateCustomer) {
		this.appointmentDateCustomer = appointmentDateCustomer;
	}

	public String getSuggestTimeCustomer() {
		return suggestTimeCustomer;
	}

	public void setSuggestTimeCustomer(String suggestTimeCustomer) {
		this.suggestTimeCustomer = suggestTimeCustomer;
	}

	public String getCreatedUserName() {
		return createdUserName;
	}

	public void setCreatedUserName(String createdUserName) {
		this.createdUserName = createdUserName;
	}

	public Long getOnTimeKPI() {
		return onTimeKPI;
	}

	public void setOnTimeKPI(Long onTimeKPI) {
		this.onTimeKPI = onTimeKPI;
	}

	public Long getOverTimeKPI() {
		return overTimeKPI;
	}

	public void setOverTimeKPI(Long overTimeKPI) {
		this.overTimeKPI = overTimeKPI;
	}
	
	public ResultTangentGPTHDTO getResultTangentGPTHDTO() {
		return resultTangentGPTHDTO;
	}

	public void setResultTangentGPTHDTO(ResultTangentGPTHDTO resultTangentGPTHDTO) {
		this.resultTangentGPTHDTO = resultTangentGPTHDTO;
	}

	private ResultTangentDetailYesDTO resultTangentDetailYesDTO;
	private ResultTangentDetailNoDTO resultTangentDetailNoDTO;
	private ResultSolutionGPTHDTO resultSolutionGPTHDTO;


	public ResultSolutionGPTHDTO getResultSolutionGPTHDTO() {
		return resultSolutionGPTHDTO;
	}

	public void setResultSolutionGPTHDTO(ResultSolutionGPTHDTO resultSolutionGPTHDTO) {
		this.resultSolutionGPTHDTO = resultSolutionGPTHDTO;
	}

	public ResultTangentDetailYesDTO getResultTangentDetailYesDTO() {
		return resultTangentDetailYesDTO;
	}

	public void setResultTangentDetailYesDTO(ResultTangentDetailYesDTO resultTangentDetailYesDTO) {
		this.resultTangentDetailYesDTO = resultTangentDetailYesDTO;
	}

	public ResultTangentDetailNoDTO getResultTangentDetailNoDTO() {
		return resultTangentDetailNoDTO;
	}

	public void setResultTangentDetailNoDTO(ResultTangentDetailNoDTO resultTangentDetailNoDTO) {
		this.resultTangentDetailNoDTO = resultTangentDetailNoDTO;
	}
	
	private List<ResultTangentGPTHDTO> listResultTangentGPTH;
	private List<ResultSolutionGPTHDTO>  listResultSolutionGPTH;

	public List<ResultTangentGPTHDTO> getListResultTangentGPTH() {
		return listResultTangentGPTH;
	}

	public void setListResultTangentGPTH(List<ResultTangentGPTHDTO> listResultTangentGPTH) {
		this.listResultTangentGPTH = listResultTangentGPTH;
	}
	
	public List<ResultSolutionGPTHDTO> getListResultSolutionGPTH() {
		return listResultSolutionGPTH;
	}

	public void setListResultSolutionGPTH(List<ResultSolutionGPTHDTO> listResultSolutionGPTH) {
		this.listResultSolutionGPTH = listResultSolutionGPTH;
	}

	private String apartmentNumber;

	public String getApartmentNumber() {
		return apartmentNumber;
	}

	public void setApartmentNumber(String apartmentNumber) {
		this.apartmentNumber = apartmentNumber;
	}
	
	private String performerEmail;
	private String performerPhoneNumber;
	private String performerSolutionEmail;
	private String performerSolutionNumber;

	public String getPerformerSolutionEmail() {
		return performerSolutionEmail;
	}

	public void setPerformerSolutionEmail(String performerSolutionEmail) {
		this.performerSolutionEmail = performerSolutionEmail;
	}

	public String getPerformerSolutionNumber() {
		return performerSolutionNumber;
	}

	public void setPerformerSolutionNumber(String performerSolutionNumber) {
		this.performerSolutionNumber = performerSolutionNumber;
	}

	public String getPerformerEmail() {
		return performerEmail;
	}

	public void setPerformerEmail(String performerEmail) {
		this.performerEmail = performerEmail;
	}

	public String getPerformerPhoneNumber() {
		return performerPhoneNumber;
	}

	public void setPerformerPhoneNumber(String performerPhoneNumber) {
		this.performerPhoneNumber = performerPhoneNumber;
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
	
	public Long getOverdueStatus() {
		return overdueStatus;
	}
	public void setOverdueStatus(Long overdueStatus) {
		this.overdueStatus = overdueStatus;
	}

	public Date getNextTangentTime() {
		return nextTangentTime;
	}

	public void setNextTangentTime(Date nextTangentTime) {
		this.nextTangentTime = nextTangentTime;
	}
	
	public List<String> getLstOverdueStatus() {
		return lstOverdueStatus;
	}

	public void setLstOverdueStatus(List<String> lstOverdueStatus) {
		this.lstOverdueStatus = lstOverdueStatus;
	}
	
	public Long getIsInternalSource() {
		return isInternalSource;
	}

	public void setIsInternalSource(Long isInternalSource) {
		this.isInternalSource = isInternalSource;
	}

	public Long getIsSocialSource() {
		return isSocialSource;
	}

	public void setIsSocialSource(Long isSocialSource) {
		this.isSocialSource = isSocialSource;
	}

	public Long getIsCustomerServiceSource() {
		return isCustomerServiceSource;
	}

	public void setIsCustomerServiceSource(Long isCustomerServiceSource) {
		this.isCustomerServiceSource = isCustomerServiceSource;
	}

	public Date getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(Date appointmentDate) {
		this.appointmentDate = appointmentDate;
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

	public String getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(String oldStatus) {
		this.oldStatus = oldStatus;
	}

	public Date getRealityTangentDate() {
		return realityTangentDate;
	}

	public void setRealityTangentDate(Date realityTangentDate) {
		this.realityTangentDate = realityTangentDate;
	}
	
	public Long getPartnerType() {
		return partnerType;
	}

	public void setPartnerType(Long partnerType) {
		this.partnerType = partnerType;
	}

	public Long getGroupOrder() {
		return groupOrder;
	}

	public void setGroupOrder(Long groupOrder) {
		this.groupOrder = groupOrder;
	}

	public String getPartnerCode() {
		return partnerCode;
	}

	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}

	public String getUpdatedDateDb() {
		return updatedDateDb;
	}

	public void setUpdatedDateDb(String updatedDateDb) {
		this.updatedDateDb = updatedDateDb;
	}

	public String getOldPhone() {
		return oldPhone;
	}

	public void setOldPhone(String oldPhone) {
		this.oldPhone = oldPhone;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(Long sysUserId) {
		this.sysUserId = sysUserId;
	}

	public static Map<String, String> getListsources() {
		return listSources;
	}

	public String getProvinceKeyCode() {
		return provinceKeyCode;
	}

	public void setProvinceKeyCode(String provinceKeyCode) {
		this.provinceKeyCode = provinceKeyCode;
	}

	public DetailTangentCustomerGPTHDTO getDetailTangentCustomerGPTH() {
		return detailTangentCustomerGPTH;
	}

	public void setDetailTangentCustomerGPTH(DetailTangentCustomerGPTHDTO detailTangentCustomerGPTH) {
		this.detailTangentCustomerGPTH = detailTangentCustomerGPTH;
	}
	
}
