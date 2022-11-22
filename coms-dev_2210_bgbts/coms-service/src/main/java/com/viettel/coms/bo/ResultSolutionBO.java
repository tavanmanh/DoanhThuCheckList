package com.viettel.coms.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.ResultSolutionDTO;
import com.viettel.service.base.model.BaseFWModelImpl;

@SuppressWarnings("serial")
@Entity
@Table(name = "RESULT_SOLUTION")
public class ResultSolutionBO extends BaseFWModelImpl {

	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "RESULT_SOLUTION_SEQ") })
	@Column(name = "RESULT_SOLUTION_ID", length = 10)
	private Long resultSolutionId;
	@Column(name = "TANGENT_CUSTOMER_ID", length = 10)
	private Long tangentCustomerId;
	@Column(name = "RESULT_SOLUTION_ORDER", length = 10)
	private Long resultSolutionOrder;
	@Column(name = "PRESENT_SOLUTION_DATE", length = 22)
	private Date presentSolutionDate;
	@Column(name = "RESULT_SOLUTION_TYPE", length = 5)
	private String resultSolutionType;
	@Column(name = "CONTRACT_ID", length = 10)
	private Long contractId;
	@Column(name = "CONTRACT_CODE", length = 100)
	private String contractCode;
	@Column(name = "SIGN_DATE", length = 22)
	private Date signDate;
	@Column(name = "GUARANTEE_TIME", length = 22)
	private String guaranteeTime;
	@Column(name = "CONSTRUCT_TIME", length = 22)
	private Date constructTime;
	@Column(name = "CONTRACT_ROSE", length = 10)
	private Double contractRose;
	@Column(name = "CONTENT_RESULT_SOLUTION", length = 1000)
	private String contentResultSolution;
	@Column(name = "APPROVED_PERFORMER_ID", length = 10)
	private Long approvedPerformerId;
	@Column(name = "APPROVED_DESCRIPTION", length = 1000)
	private String approvedDescription;
	@Column(name = "APPROVED_USER_ID", length = 10)
	private Long approvedUserId;
	@Column(name = "APPROVED_DATE", length = 22)
	private Date approvedDate;
	@Column(name = "PRESENT_SOLUTION_DATE_NEXT", length = 1000)
	private Date presentSolutionDateNext;
	@Column(name = "PERFORMER_ID", length = 10)
	private Long performerId;
	@Column(name = "CREATED_USER", length = 10)
	private Long createdUser;
	@Column(name = "CREATED_DATE", length = 22)
	private Date createdDate;
	@Column(name = "UPDATED_USER", length = 10)
	private Long updatedUser;
	@Column(name = "UPDATED_DATE", length = 22)
	private Date updatedDate;
	@Column(name = "APPROVED_STATUS", length = 22)
	private String approvedStatus;
	@Column(name = "CONTRACT_PRICE", length = 22)
	private Double contractPrice;
	@Column(name = "REALITY_SOLUTION_DATE", length = 22)
	private Date realitySolutionDate;
	@Column(name = "SIGN_STATUS", length = 1)
	private Long signStatus;
	@Column(name = "UNSECCESSFULL_REASON", length = 1000)
	private String unsuccessfullReason;

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

	public Long getResultSolutionId() {
		return resultSolutionId;
	}

	public void setResultSolutionId(Long resultSolutionId) {
		this.resultSolutionId = resultSolutionId;
	}

	public Long getTangentCustomerId() {
		return tangentCustomerId;
	}

	public void setTangentCustomerId(Long tangentCustomerId) {
		this.tangentCustomerId = tangentCustomerId;
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

	public String getApprovedStatus() {
		return approvedStatus;
	}

	public void setApprovedStatus(String approvedStatus) {
		this.approvedStatus = approvedStatus;
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

	@Override
	public ResultSolutionDTO toDTO() {
		ResultSolutionDTO dto = new ResultSolutionDTO();
		dto.setResultSolutionId(this.getResultSolutionId());
		dto.setTangentCustomerId(this.getTangentCustomerId());
		dto.setResultSolutionOrder(this.getResultSolutionOrder());
		dto.setPresentSolutionDate(this.getPresentSolutionDate());
		dto.setResultSolutionType(this.getResultSolutionType());
		dto.setContractId(this.getContractId());
		dto.setContractCode(this.getContractCode());
		dto.setSignDate(this.getSignDate());
		dto.setGuaranteeTime(this.getGuaranteeTime());
		dto.setConstructTime(this.getConstructTime());
		dto.setContractRose(this.getContractRose());
		dto.setContentResultSolution(this.getContentResultSolution());
		dto.setApprovedPerformerId(this.getApprovedPerformerId());
		dto.setApprovedDescription(this.getApprovedDescription());
		dto.setApprovedUserId(this.getApprovedUserId());
		dto.setApprovedDate(this.getApprovedDate());
		dto.setPresentSolutionDateNext(this.getPresentSolutionDateNext());
		dto.setPerformerId(this.getPerformerId());
		dto.setCreatedUser(this.getCreatedUser());
		dto.setCreatedDate(this.getCreatedDate());
		dto.setUpdatedUser(this.getUpdatedUser());
		dto.setUpdatedDate(this.updatedDate);
		dto.setApprovedStatus(this.approvedStatus);
		dto.setContractPrice(this.contractPrice);
		dto.setRealitySolutionDate(this.realitySolutionDate);
		dto.setSignStatus(this.getSignStatus());
		dto.setUnsuccessfullReason(this.getUnsuccessfullReason());
		return dto;
	}

}
