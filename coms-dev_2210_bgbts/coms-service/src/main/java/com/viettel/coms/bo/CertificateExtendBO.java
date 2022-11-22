package com.viettel.coms.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.CertificateExtendDTO;
import com.viettel.service.base.model.BaseFWModelImpl;

@Entity
@Table(name = "CERTIFICATE_EXTEND")
public class CertificateExtendBO extends BaseFWModelImpl {

	private Long certificateExtendId;
	private Date finishDate;
	private String description;
	private Long status;
	private Long certificateId;

	private Date createdDate;
	private Date updatedDate;
	private Long createdUserId;
	private Long updatedUserId;
	
	private String reason;

	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "CERTIFICATE_EXTEND_SEQ") })
	
	@Column(name = "CERTIFICATE_EXTEND_ID", length = 10 )
	public Long getCertificateExtendId() {
		return certificateExtendId;
	}

	public void setCertificateExtendId(Long certificateExtendId) {
		this.certificateExtendId = certificateExtendId;
	}
	@Column(name = "FINISH_DATE", length = 20 )
	public Date getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}
	@Column(name = "DESCRIPTION", length = 1000 )
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	@Column(name = "STATUS", length = 2)
	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}
	@Column(name = "CERTIFICATE_ID", length = 10)
	public Long getCertificateId() {
		return certificateId;
	}

	public void setCertificateId(Long certificateId) {
		this.certificateId = certificateId;
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
	
	
	@Column(name = "REASON", length = 1000 )
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	public CertificateExtendDTO toDTO() {
		// TODO Auto-generated method stub
		CertificateExtendDTO certificateExtendDTO = new CertificateExtendDTO(); 
		certificateExtendDTO.setCertificateExtendId(this.certificateExtendId);
		certificateExtendDTO.setCertificateId(this.certificateId);
		certificateExtendDTO.setFinishDate(this.finishDate);
		certificateExtendDTO.setStatus(this.status);
		certificateExtendDTO.setDescription(this.description);
		certificateExtendDTO.setCreatedDate(this.createdDate);
		certificateExtendDTO.setCreatedUserId(this.createdUserId);
		certificateExtendDTO.setUpdatedDate(this.updatedDate);
		certificateExtendDTO.setUpdatedUserId(this.updatedUserId);
		certificateExtendDTO.setReason(this.reason);
		return certificateExtendDTO;
	}

}
