package com.viettel.coms.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import com.viettel.coms.dto.ResultTangentGPTHDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

@Entity
@Table(name = "RESULT_TANGENT_GPTH")
public class ResultTangentGPTHBO extends BaseFWModelImpl {
	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "RESULT_TANGENT_GPTH_SEQ") })
	@Column(name = "RESULT_TANGENT_GPTH_ID", length = 10)
	private Long resultTangentGPTHId;
	@Column(name = "TANGENT_CUSTOMER_GPTH_ID", length = 10)
	private Long tangentCustomerGPTHId;
	@Column(name = "APPOINTMENT_DATE", length = 22)
	private Date appointmentDate;
	@Column(name = "ORDER_RESULT_TANGENT", length = 10)
	private String orderResultTangent;
	@Column(name = "RESULT_TANGENT_TYPE", length = 5)
	private String resultTangentType;
	@Column(name = "REASON_REJECTION", length = 1000)
	private String reasonRejection;
	@Column(name = "APPROVED_STATUS", length = 5)
	private String approvedStatus;
	@Column(name = "APPROVED_PERFORMER_ID", length = 10)
	private Long approvedPerformerId;
	@Column(name = "APPROVED_DESCRIPTION", length = 1000)
	private String approvedDescription;
	@Column(name = "APPROVED_USER_ID", length = 10)
	private Long approvedUserId;
	@Column(name = "APPROVED_DATE", length = 22)
	private Date approvedDate;
	@Column(name = "BUILDING_LOCATION", length = 500)
	private String buildingLocation;
	@Column(name = "START_DATE", length = 5)
	private Date startDate;
	@Column(name = "CONTRUCTION_RECORDS", length = 5)
	private String contructionRecords;
	@Column(name = "CONTRUCTION_DESIGN", length = 5)
	private String contructionDesign;
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
	@Column(name = "REALITY_TANGENT_DATE", length = 22)
	private Date realityTangentDate;

	public Date getRealityTangentDate() {
		return realityTangentDate;
	}

	public void setRealityTangentDate(Date realityTangentDate) {
		this.realityTangentDate = realityTangentDate;
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
	public BaseFWDTOImpl toDTO() {
		// TODO Auto-generated method stub
		ResultTangentGPTHDTO dto = new ResultTangentGPTHDTO();
		dto.setResultTangentGPTHId(this.getResultTangentGPTHId());
		dto.setTangentCustomerGPTHId(this.getTangentCustomerGPTHId());
		dto.setAppointmentDate(this.getAppointmentDate());
		dto.setOrderResultTangent(this.getOrderResultTangent());
		dto.setResultTangentType(this.getResultTangentType());
		dto.setReasonRejection(this.getReasonRejection());
		dto.setApprovedStatus(this.getApprovedStatus());
		dto.setApprovedPerformerId(this.getApprovedPerformerId());
		dto.setApprovedDescription(this.getApprovedDescription());
		dto.setApprovedUserId(this.getApprovedUserId());
		dto.setApprovedDate(this.getApprovedDate());
		dto.setBuildingLocation(this.getBuildingLocation());
		dto.setStartDate(this.startDate);
		dto.setContructionRecords(this.getContructionRecords());
		dto.setContructionDesign(this.getContructionDesign());
		dto.setPerformerId(this.getPerformerId());
		dto.setCreatedUser(this.getCreatedUser());
		dto.setCreatedDate(this.getCreatedDate());
		dto.setUpdatedUser(this.getUpdatedUser());
		dto.setUpdatedDate(this.getUpdatedDate());
		dto.setRealityTangentDate(this.realityTangentDate);
		return dto;
	}

}
