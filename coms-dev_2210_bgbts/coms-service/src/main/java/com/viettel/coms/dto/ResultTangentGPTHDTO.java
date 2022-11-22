package com.viettel.coms.dto;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.ResultTangentBO;
import com.viettel.coms.bo.ResultTangentGPTHBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;

@XmlRootElement(name = "")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultTangentGPTHDTO extends ComsBaseFWDTO<ResultTangentGPTHBO>{

	private Long resultTangentGPTHId;
	private Long tangentCustomerGPTHId;
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
	
	public Long getResultTangentGPTHId() {
		return resultTangentGPTHId;
	}
	public void setResultTangentGPTHId(Long resultTangentGPTHId) {
		this.resultTangentGPTHId = resultTangentGPTHId;
	}
	public Long getTangentCustomerGPTHId() {
		return tangentCustomerGPTHId;
	}
	public void setTangentCustomerGPTHId(Long tangentCustomerGPTHId) {
		this.tangentCustomerGPTHId = tangentCustomerGPTHId;
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
		return resultTangentGPTHId.toString();
	}
	@Override
	public Long getFWModelId() {
		// TODO Auto-generated method stub
		return resultTangentGPTHId;
	}
	@Override
	public ResultTangentGPTHBO toModel() {
		// TODO Auto-generated method stub
		ResultTangentGPTHBO bo = new ResultTangentGPTHBO();
		bo.setResultTangentGPTHId(this.getResultTangentGPTHId());
		bo.setTangentCustomerGPTHId(this.getTangentCustomerGPTHId());
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
	
	private ResultSolutionGPTHDTO resultSolutionGPTHDTO;
	
	public ResultSolutionGPTHDTO getResultSolutionGPTHDTO() {
		return resultSolutionGPTHDTO;
	}
	public void setResultSolutionGPTHDTO(ResultSolutionGPTHDTO resultSolutionGPTHDTO) {
		this.resultSolutionGPTHDTO = resultSolutionGPTHDTO;
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
	
	private TangentCustomerGPTHDTO tangentCustomerGPTHDTO;

	public TangentCustomerGPTHDTO getTangentCustomerGPTHDTO() {
		return tangentCustomerGPTHDTO;
	}
	public void setTangentCustomerGPTHDTO(TangentCustomerGPTHDTO tangentCustomerGPTHDTO) {
		this.tangentCustomerGPTHDTO = tangentCustomerGPTHDTO;
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
