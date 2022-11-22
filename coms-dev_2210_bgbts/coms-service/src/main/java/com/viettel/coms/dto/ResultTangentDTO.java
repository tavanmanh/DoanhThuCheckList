package com.viettel.coms.dto;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.ResultTangentBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;

@XmlRootElement(name = "RESULT_TANGENTBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultTangentDTO extends ComsBaseFWDTO<ResultTangentBO>{

	private Long resultTangentId;
	private Long tangentCustomerId;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date appointmentDate;
	private String orderResultTangent;
	private String resultTangentType;
	private String reasonRejection;
	private String approvedStatus;
	private Long approvedPerformerId;
	private String approvedDescription;
	private Long approvedUserId;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date approvedDate;
	private String buildingLocation;
	private String package1;
	private String package2;
	private String package3;
	private String package4;
	private String package5;
	private String package6;
	/*private String package7;
	private String package8;
	private String package9;*/
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date startDate;
	private String contructionRecords;
	private String contructionDesign;
	private Long performerId;
	private Long createdUser;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date createdDate;
	private Long updatedUser;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date updatedDate;
	private String customerName;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date realityTangentDate;

	private Long partnerType;
	
	private String updatedDateDb;
	
	public Date getRealityTangentDate() {
		return realityTangentDate;
	}
	public void setRealityTangentDate(Date realityTangentDate) {
		this.realityTangentDate = realityTangentDate;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Long getResultTangentId() {
		return resultTangentId;
	}
	public void setResultTangentId(Long resultTangentId) {
		this.resultTangentId = resultTangentId;
	}
	public Long getTangentCustomerId() {
		return tangentCustomerId;
	}
	public void setTangentCustomerId(Long tangentCustomerId) {
		this.tangentCustomerId = tangentCustomerId;
	}
	public Date getAppointmentDate() {
		return appointmentDate;
	}
	public void setAppointmentDate(Date appointmentDate) {
		this.appointmentDate = appointmentDate;
	}
	public String getOrderResultTangent() {
		return orderResultTangent;
	}
	public void setOrderResultTangent(String orderResultTangent) {
		this.orderResultTangent = orderResultTangent;
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
	public String getApprovedStatus() {
		return approvedStatus;
	}
	public void setApprovedStatus(String approvedStatus) {
		this.approvedStatus = approvedStatus;
	}
	public Long getApprovedPerformerId() {
		return approvedPerformerId;
	}
	public void setApprovedPerformerId(Long approvedPerformerId) {
		this.approvedPerformerId = approvedPerformerId;
	}
	public String getApprovedDescription() {
		return approvedDescription;
	}
	public void setApprovedDescription(String approvedDescription) {
		this.approvedDescription = approvedDescription;
	}
	public Long getApprovedUserId() {
		return approvedUserId;
	}
	public void setApprovedUserId(Long approvedUserId) {
		this.approvedUserId = approvedUserId;
	}
	public Date getApprovedDate() {
		return approvedDate;
	}
	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}
	public String getBuildingLocation() {
		return buildingLocation;
	}
	public void setBuildingLocation(String buildingLocation) {
		this.buildingLocation = buildingLocation;
	}
	public String getPackage1() {
		return package1;
	}
	public void setPackage1(String package1) {
		this.package1 = package1;
	}
	public String getPackage2() {
		return package2;
	}
	public void setPackage2(String package2) {
		this.package2 = package2;
	}
	public String getPackage3() {
		return package3;
	}
	public void setPackage3(String package3) {
		this.package3 = package3;
	}
	public String getPackage4() {
		return package4;
	}
	public void setPackage4(String package4) {
		this.package4 = package4;
	}
	public String getPackage5() {
		return package5;
	}
	public void setPackage5(String package5) {
		this.package5 = package5;
	}
	public String getPackage6() {
		return package6;
	}
	public void setPackage6(String package6) {
		this.package6 = package6;
	}
	/*public String getPackage7() {
		return package7;
	}
	public void setPackage7(String package7) {
		this.package7 = package7;
	}
	public String getPackage8() {
		return package8;
	}
	public void setPackage8(String package8) {
		this.package8 = package8;
	}
	public String getPackage9() {
		return package9;
	}
	public void setPackage9(String package9) {
		this.package9 = package9;
	}*/
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getContructionRecords() {
		return contructionRecords;
	}
	public void setContructionRecords(String contructionRecords) {
		this.contructionRecords = contructionRecords;
	}
	public String getContructionDesign() {
		return contructionDesign;
	}
	public void setContructionDesign(String contructionDesign) {
		this.contructionDesign = contructionDesign;
	}
	public Long getPerformerId() {
		return performerId;
	}
	public void setPerformerId(Long performerId) {
		this.performerId = performerId;
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
		return resultTangentId.toString();
	}
	@Override
	public Long getFWModelId() {
		// TODO Auto-generated method stub
		return resultTangentId;
	}
	@Override
	public ResultTangentBO toModel() {
		// TODO Auto-generated method stub
		ResultTangentBO bo = new ResultTangentBO();
		bo.setResultTangentId(this.getResultTangentId());
		bo.setTangentCustomerId(this.getTangentCustomerId());
		bo.setAppointmentDate(this.getAppointmentDate());
		bo.setOrderResultTangent(this.getOrderResultTangent());
		bo.setResultTangentType(this.getResultTangentType());
		bo.setReasonRejection(this.getReasonRejection());
		bo.setApprovedStatus(this.getApprovedStatus());
		bo.setApprovedPerformerId(this.getApprovedPerformerId());
		bo.setApprovedDescription(this.getApprovedDescription());
		bo.setApprovedUserId(this.getApprovedUserId());
		bo.setApprovedDate(this.getApprovedDate());
		bo.setBuildingLocation(this.getBuildingLocation());
		bo.setPackage1(this.getPackage1());
		bo.setPackage2(this.getPackage2());
		bo.setPackage3(this.getPackage3());
		bo.setPackage4(this.getPackage4());
		bo.setPackage5(this.getPackage5());
		bo.setPackage6(this.getPackage6());
		/*bo.setPackage7(this.getPackage7());
		bo.setPackage8(this.getPackage8());
		bo.setPackage9(this.getPackage9());*/
		bo.setStartDate(this.startDate);
		bo.setContructionRecords(this.getContructionRecords());
		bo.setContructionDesign(this.getContructionDesign());
		bo.setPerformerId(this.getPerformerId());
		bo.setCreatedUser(this.getCreatedUser());
		bo.setCreatedDate(this.getCreatedDate());
		bo.setUpdatedUser(this.getUpdatedUser());
		bo.setUpdatedDate(this.getUpdatedDate());
		bo.setRealityTangentDate(this.realityTangentDate);
		return bo;
	}
	
	private List<UtilAttachDocumentDTO> fileReceipts;
	private List<UtilAttachDocumentDTO> fileRedBook;
	private List<UtilAttachDocumentDTO> fileTopographic;
	
	public List<UtilAttachDocumentDTO> getFileReceipts() {
		return fileReceipts;
	}
	public void setFileReceipts(List<UtilAttachDocumentDTO> fileReceipts) {
		this.fileReceipts = fileReceipts;
	}
	public List<UtilAttachDocumentDTO> getFileRedBook() {
		return fileRedBook;
	}
	public void setFileRedBook(List<UtilAttachDocumentDTO> fileRedBook) {
		this.fileRedBook = fileRedBook;
	}
	public List<UtilAttachDocumentDTO> getFileTopographic() {
		return fileTopographic;
	}
	public void setFileTopographic(List<UtilAttachDocumentDTO> fileTopographic) {
		this.fileTopographic = fileTopographic;
	}

	private ResultTangentDetailYesDTO resultTangentDetailYesDTO;
	private ResultTangentDetailNoDTO resultTangentDetailNoDTO;
	
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
	
	private ResultSolutionDTO resultSolutionDTO;
	public ResultSolutionDTO getResultSolutionDTO() {
		return resultSolutionDTO;
	}
	public void setResultSolutionDTO(ResultSolutionDTO resultSolutionDTO) {
		this.resultSolutionDTO = resultSolutionDTO;
	}
	
	private String approvedPerformerName;
	private String approvedUserName;
	private String performerName;
	private String performerEmail;
	private String performerPhone;
	private String approvedPerformerEmail;
	private String approvedPerformerPhone;
	
	public String getPerformerPhone() {
		return performerPhone;
	}
	public void setPerformerPhone(String performerPhone) {
		this.performerPhone = performerPhone;
	}
	public String getApprovedPerformerPhone() {
		return approvedPerformerPhone;
	}
	public void setApprovedPerformerPhone(String approvedPerformerPhone) {
		this.approvedPerformerPhone = approvedPerformerPhone;
	}
	public String getPerformerEmail() {
		return performerEmail;
	}
	public void setPerformerEmail(String performerEmail) {
		this.performerEmail = performerEmail;
	}
	public String getApprovedPerformerEmail() {
		return approvedPerformerEmail;
	}
	public void setApprovedPerformerEmail(String approvedPerformerEmail) {
		this.approvedPerformerEmail = approvedPerformerEmail;
	}
	public String getApprovedPerformerName() {
		return approvedPerformerName;
	}
	public void setApprovedPerformerName(String approvedPerformerName) {
		this.approvedPerformerName = approvedPerformerName;
	}
	public String getApprovedUserName() {
		return approvedUserName;
	}
	public void setApprovedUserName(String approvedUserName) {
		this.approvedUserName = approvedUserName;
	}
	public String getPerformerName() {
		return performerName;
	}
	public void setPerformerName(String performerName) {
		this.performerName = performerName;
	}
	
	private TangentCustomerDTO tangentCustomerDTO;

	public TangentCustomerDTO getTangentCustomerDTO() {
		return tangentCustomerDTO;
	}
	public void setTangentCustomerDTO(TangentCustomerDTO tangentCustomerDTO) {
		this.tangentCustomerDTO = tangentCustomerDTO;
	}
	
	private String createdDateDb;
	private String createdDateYes;
	
	private String appointmentDateDb;

	public String getCreatedDateDb() {
		return createdDateDb;
	}
	public void setCreatedDateDb(String createdDateDb) {
		this.createdDateDb = createdDateDb;
	}
	

	public String getCreatedDateYes() {
		return createdDateYes;
	}
	public void setCreatedDateYes(String createdDateYes) {
		this.createdDateYes = createdDateYes;
	}
	public String getAppointmentDateDb() {
		return appointmentDateDb;
	}
	public void setAppointmentDateDb(String appointmentDateDb) {
		this.appointmentDateDb = appointmentDateDb;
	}
	public Long getPartnerType() {
		return partnerType;
	}
	public void setPartnerType(Long partnerType) {
		this.partnerType = partnerType;
	}
	public String getUpdatedDateDb() {
		return updatedDateDb;
	}
	public void setUpdatedDateDb(String updatedDateDb) {
		this.updatedDateDb = updatedDateDb;
	}
	
}
