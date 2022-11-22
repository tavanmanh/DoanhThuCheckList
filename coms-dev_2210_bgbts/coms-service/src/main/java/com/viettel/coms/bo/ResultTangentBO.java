package com.viettel.coms.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.ResultTangentDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

@Entity
@Table(name = "RESULT_TANGENT")
public class ResultTangentBO extends BaseFWModelImpl {
	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "RESULT_TANGENT_SEQ") })
	@Column(name = "RESULT_TANGENT_ID", length = 10)
	private Long resultTangentId;
	@Column(name = "TANGENT_CUSTOMER_ID", length = 10)
	private Long tangentCustomerId;
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
	@Column(name = "PACKAGE_1", length = 5)
	private String package1;
	@Column(name = "PACKAGE_2", length = 5)
	private String package2;
	@Column(name = "PACKAGE_3", length = 5)
	private String package3;
	@Column(name = "PACKAGE_4", length = 5)
	private String package4;
	@Column(name = "PACKAGE_5", length = 5)
	private String package5;
	@Column(name = "PACKAGE_6", length = 5)
	private String package6;
	/*@Column(name = "PACKAGE_7", length = 5)
	private String package7;
	@Column(name = "PACKAGE_8", length = 5)
	private String package8;
	@Column(name = "PACKAGE_9", length = 5)
	private String package9;*/
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
	}
*/
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
		ResultTangentDTO dto = new ResultTangentDTO();
		dto.setResultTangentId(this.getResultTangentId());
		dto.setTangentCustomerId(this.getTangentCustomerId());
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
		dto.setPackage1(this.getPackage1());
		dto.setPackage2(this.getPackage2());
		dto.setPackage3(this.getPackage3());
		dto.setPackage4(this.getPackage4());
		dto.setPackage5(this.getPackage5());
		dto.setPackage6(this.getPackage6());
//		taotq start 07092021
		/*dto.setPackage7(this.getPackage7());
		dto.setPackage8(this.getPackage8());
		dto.setPackage9(this.getPackage9());*/
//		taotq end 07092021
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
