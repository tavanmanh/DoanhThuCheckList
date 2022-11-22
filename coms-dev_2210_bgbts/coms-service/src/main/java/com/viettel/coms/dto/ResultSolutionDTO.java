package com.viettel.coms.dto;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.ResultSolutionBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;

@XmlRootElement(name = "RESULT_SOLUTIONBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultSolutionDTO extends ComsBaseFWDTO<ResultSolutionBO> {

	private Long resultSolutionId;
	private Long tangentCustomerId;
	private Long resultSolutionOrder;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date presentSolutionDate;
	private String resultSolutionType;
	private Long contractId;
	private String contractCode;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date signDate;
//	@JsonSerialize(using = JsonDateSerializerDate.class)
//	@JsonDeserialize(using = JsonDateDeserializer.class)
	private String guaranteeTime;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date constructTime;
	private Double contractRose;
	private String contentResultSolution;
	private Long approvedPerformerId;
	private String approvedDescription;
	private Long approvedUserId;
	
	private Long signStatus;
	private String unsuccessfullReason;

	public Long getUserApprovedId() {
		return userApprovedId;
	}

	public void setUserApprovedId(Long userApprovedId) {
		this.userApprovedId = userApprovedId;
	}

	private Long userApprovedId;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date approvedDate;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date presentSolutionDateNext;
	private Long performerId;
	
	private Long createdUser;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date createdDate;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date appovedDate;
	private Long updatedUser;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date updatedDate;
	
	private String updatedDateDb;
	private String customerName;
	private Double contractPrice;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date realitySolutionDate;
	private String createdDateDb;
	private String source;

	public ResultSolutionDTO() {
	}

	public String getCreatedDateDb() {
		return createdDateDb;
	}

	public void setCreatedDateDb(String createdDateDb) {
		this.createdDateDb = createdDateDb;
	}

	public Date getRealitySolutionDate() {
		return realitySolutionDate;
	}

	public void setRealitySolutionDate(Date realitySolutionDate) {
		this.realitySolutionDate = realitySolutionDate;
	}

	public Double getContractPrice() {
		return contractPrice;
	}

	public void setContractPrice(Double contractPrice) {
		this.contractPrice = contractPrice;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Long getResultSolutionId() {
		return resultSolutionId;
	}

	public void setResultSolutionId(Long resultSolutionId) {
		this.resultSolutionId = resultSolutionId;
	}

	public Long getResultSolutionOrder() {
		return resultSolutionOrder;
	}

	public void setResultSolutionOrder(Long resultSolutionOrder) {
		this.resultSolutionOrder = resultSolutionOrder;
	}

	public Date getPresentSolutionDate() {
		return presentSolutionDate;
	}

	public void setPresentSolutionDate(Date presentSolutionDate) {
		this.presentSolutionDate = presentSolutionDate;
	}

	public String getResultSolutionType() {
		return resultSolutionType;
	}

	public void setResultSolutionType(String resultSolutionType) {
		this.resultSolutionType = resultSolutionType;
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

	public Date getSignDate() {
		return signDate;
	}

	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}

	public String getGuaranteeTime() {
		return guaranteeTime;
	}

	public void setGuaranteeTime(String guaranteeTime) {
		this.guaranteeTime = guaranteeTime;
	}

	public Date getConstructTime() {
		return constructTime;
	}

	public void setConstructTime(Date constructTime) {
		this.constructTime = constructTime;
	}

	public Double getContractRose() {
		return contractRose;
	}

	public void setContractRose(Double contractRose) {
		this.contractRose = contractRose;
	}

	public String getContentResultSolution() {
		return contentResultSolution;
	}

	public void setContentResultSolution(String contentResultSolution) {
		this.contentResultSolution = contentResultSolution;
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

	public Date getPresentSolutionDateNext() {
		return presentSolutionDateNext;
	}

	public void setPresentSolutionDateNext(Date presentSolutionDateNext) {
		this.presentSolutionDateNext = presentSolutionDateNext;
	}

	public Long getPerformerId() {
		return performerId;
	}

	public void setPerformerId(Long performerId) {
		this.performerId = performerId;
	}

	public Long getTangentCustomerId() {
		return tangentCustomerId;
	}

	public void setTangentCustomerId(Long tangentCustomerId) {
		this.tangentCustomerId = tangentCustomerId;
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

	public Date getAppovedDate() {
		return appovedDate;
	}

	public void setAppovedDate(Date appovedDate) {
		this.appovedDate = appovedDate;
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
		return resultSolutionId.toString();
	}

	@Override
	public Long getFWModelId() {
		// TODO Auto-generated method stub
		return resultSolutionId;
	}

	@Override
	public ResultSolutionBO toModel() {
		// TODO Auto-generated method stub
		ResultSolutionBO bo = new ResultSolutionBO();
		bo.setResultSolutionId(this.getResultSolutionId());
		bo.setTangentCustomerId(this.getTangentCustomerId());
		bo.setResultSolutionOrder(this.getResultSolutionOrder());
		bo.setPresentSolutionDate(this.getPresentSolutionDate());
		bo.setResultSolutionType(this.getResultSolutionType());
		bo.setContractId(this.getContractId());
		bo.setContractCode(this.getContractCode());
		bo.setSignDate(this.getSignDate());
		bo.setGuaranteeTime(this.getGuaranteeTime());
		bo.setConstructTime(this.getConstructTime());
		bo.setContractRose(this.getContractRose());
		bo.setContentResultSolution(this.getContentResultSolution());
		bo.setApprovedPerformerId(this.getApprovedPerformerId());
		bo.setApprovedDescription(this.getApprovedDescription());
		bo.setApprovedUserId(this.getApprovedUserId());
		bo.setApprovedDate(this.getApprovedDate());
		bo.setPresentSolutionDateNext(this.getPresentSolutionDateNext());
		bo.setPerformerId(this.getPerformerId());
		bo.setCreatedUser(this.getCreatedUser());
		bo.setCreatedDate(this.getCreatedDate());
		bo.setUpdatedUser(this.getUpdatedUser());
		bo.setUpdatedDate(this.updatedDate);
		bo.setApprovedStatus(this.approvedStatus);
		bo.setContractPrice(this.contractPrice);
		bo.setRealitySolutionDate(this.realitySolutionDate);
		bo.setSignStatus(this.getSignStatus());
		bo.setUnsuccessfullReason(this.getUnsuccessfullReason());
		return bo;
	}

	private List<UtilAttachDocumentDTO> fileResultSolution;
	
	public List<UtilAttachDocumentDTO> getFileResultSolution() {
		return fileResultSolution;
	}

	public void setFileResultSolution(List<UtilAttachDocumentDTO> fileResultSolution) {
		this.fileResultSolution = fileResultSolution;
	}

	private String approvedStatus;

	public String getApprovedStatus() {
		return approvedStatus;
	}

	public void setApprovedStatus(String approvedStatus) {
		this.approvedStatus = approvedStatus;
	}
	
	private String approvedPerformerName;
	private String approvedUserName;
	private String performerName;
	private String performerEmail;
	private String performerPhone;
	private String approvedPerformerEmail;
	private String approvedPerformerPhone;

	public String getPerformerEmail() {
		return performerEmail;
	}

	public void setPerformerEmail(String performerEmail) {
		this.performerEmail = performerEmail;
	}

	public String getPerformerPhone() {
		return performerPhone;
	}

	public void setPerformerPhone(String performerPhone) {
		this.performerPhone = performerPhone;
	}

	public String getApprovedPerformerEmail() {
		return approvedPerformerEmail;
	}

	public void setApprovedPerformerEmail(String approvedPerformerEmail) {
		this.approvedPerformerEmail = approvedPerformerEmail;
	}

	public String getApprovedPerformerPhone() {
		return approvedPerformerPhone;
	}

	public void setApprovedPerformerPhone(String approvedPerformerPhone) {
		this.approvedPerformerPhone = approvedPerformerPhone;
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
	
	private Long provinceId;

	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public Long getSignStatus() {
		return signStatus;
	}

	public void setSignStatus(Long signStatus) {
		this.signStatus = signStatus;
	}

	public String getUnsuccessfullReason() {
		return unsuccessfullReason;
	}

	public void setUnsuccessfullReason(String unsuccessfullReason) {
		this.unsuccessfullReason = unsuccessfullReason;
	}

	public String getUpdatedDateDb() {
		return updatedDateDb;
	}

	public void setUpdatedDateDb(String updatedDateDb) {
		this.updatedDateDb = updatedDateDb;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	
}
