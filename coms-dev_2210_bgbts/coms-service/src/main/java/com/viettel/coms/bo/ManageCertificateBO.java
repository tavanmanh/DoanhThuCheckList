package com.viettel.coms.bo;
import java.util.Date;

import javax.persistence.Column;
//duonghv13-start 21092021
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.ManageCertificateDTO;
import com.viettel.service.base.model.BaseFWModelImpl;

@Entity
@Table(name = "CERTIFICATE")
public class ManageCertificateBO extends BaseFWModelImpl{
	
	private Long certificateId;
    private String certificateCode;
    private String certificateName;
    private Long careerId;
    private Double practicePoint;
    private Double theoreticalPoint;
    
    private String unitCreated;

    private Long sysUserId;
    private String email;
    private String phoneNumber;
    private String positionName;

    private Date finishDate;
    private Date startDate;
    private Long status;
    private Long approveStatus;
    
	private Date createdDate;
	private Date updatedDate;
	private Long createdUserId;
	private Long updatedUserId;
	
	public ManageCertificateBO() {
		setColId("certificateId");
		setColName("certificateId");
		setUniqueColumn(new String[] { "certificateId" });

	}
	
	 @Id
	 @GeneratedValue(generator = "sequence")
	 @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {@Parameter(name = "sequence", value = "MANAGE_CERTIFICATE_SEQ")})
	
	@Column(name = "CERTIFICATE_ID", length = 10 )
	public Long getCertificateId() {
		return certificateId;
	}
	public void setCertificateId(Long certificateId) {
		this.certificateId = certificateId;
	}
	@Column(name = "CERTIFICATE_CODE", length = 100 )
	public String getCertificateCode() {
		return certificateCode;
	}
	public void setCertificateCode(String certificateCode) {
		this.certificateCode = certificateCode;
	}
	@Column(name = "CERTIFICATE_NAME", length = 1000 )
	public String getCertificateName() {
		return certificateName;
	}
	public void setCertificateName(String certificateName) {
		this.certificateName = certificateName;
	}
	@Column(name = "CAREER_ID", length = 10)
	public Long getCareerId() {
		return careerId;
	}
	public void setCareerId(Long careerId) {
		this.careerId = careerId;
	}
	
	@Column(name = "PRACTICE_POINT", length = 10)
	public Double getPracticePoint() {
		return practicePoint;
	}
	public void setPracticePoint(Double practicePoint) {
		this.practicePoint = practicePoint;
	}
	@Column(name = "THEORETICAL_POINT", length = 10)
	public Double getTheoreticalPoint() {
		return theoreticalPoint;
	}
	public void setTheoreticalPoint(Double theoreticalPoint) {
		this.theoreticalPoint = theoreticalPoint;
	}
	@Column(name = "UNIT_CREATED", length = 1000)
	public String getUnitCreated() {
		return unitCreated;
	}
	public void setUnitCreated(String unitCreated) {
		this.unitCreated = unitCreated;
	}
	
	@Column(name = "SYS_USER_ID", length = 10)
	public Long getSysUserId() {
		return sysUserId;
	}
	public void setSysUserId(Long sysUserId) {
		this.sysUserId = sysUserId;
	}
	
	@Column(name = "EMAIL", length = 50)
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Column(name = "PHONE_NUMBER", length = 20)
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	@Column(name = "POSITION_NAME", length = 100)
	public String getPositionName() {
		return positionName;
	}
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	@Column(name = "FINISH_DATE", length = 20)
	public Date getFinishDate() {
		return finishDate;
	}
	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}
	@Column(name = "START_DATE", length = 20)
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	@Column(name = "STATUS", length = 2)
	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
	}
	@Column(name = "APPROVE_STATUS", length = 2)
	public Long getApproveStatus() {
		return approveStatus;
	}
	public void setApproveStatus(Long approveStatus) {
		this.approveStatus = approveStatus;
	}
	
	@Column(name = "CREATED_DATE", length = 20)
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	@Column(name = "UPDATED_DATE", length = 20)
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	@Column(name = "CREATED_USER", length = 10)
	public Long getCreatedUserId() {
		return createdUserId;
	}
	public void setCreatedUserId(Long createdUserId) {
		this.createdUserId = createdUserId;
	}
	@Column(name = "UPDATED_USER", length = 10)
	public Long getUpdatedUserId() {
		return updatedUserId;
	}
	public void setUpdatedUserId(Long updatedUserId) {
		this.updatedUserId = updatedUserId;
	}
    
	public ManageCertificateDTO toDTO() {
		ManageCertificateDTO manageCertificateDTO = new ManageCertificateDTO(); 
		manageCertificateDTO.setCertificateId(this.certificateId);
		manageCertificateDTO.setCertificateCode(this.certificateCode);
		manageCertificateDTO.setCertificateName(this.certificateName);
		manageCertificateDTO.setCareerId(this.careerId);
		manageCertificateDTO.setPracticePoint(this.practicePoint);
		manageCertificateDTO.setTheoreticalPoint(this.theoreticalPoint);
		manageCertificateDTO.setUnitCreated(this.unitCreated);
		manageCertificateDTO.setSysUserId(this.sysUserId);;
		manageCertificateDTO.setEmail(this.email);
		manageCertificateDTO.setPhoneNumber(this.phoneNumber);
		manageCertificateDTO.setPositionName(this.positionName);
		manageCertificateDTO.setFinishDate(this.finishDate);
		manageCertificateDTO.setStartDate(this.startDate);
		manageCertificateDTO.setStatus(this.status);
		manageCertificateDTO.setApproveStatus(this.approveStatus);
		manageCertificateDTO.setCreatedDate(this.createdDate);
		manageCertificateDTO.setCreatedUserId(this.createdUserId);
		manageCertificateDTO.setUpdatedDate(this.updatedDate);
		manageCertificateDTO.setUpdatedUserId(this.updatedUserId);
        return manageCertificateDTO;
    }
}
	 
	
