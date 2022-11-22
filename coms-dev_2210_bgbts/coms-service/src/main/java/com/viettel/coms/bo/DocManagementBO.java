package com.viettel.coms.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.viettel.coms.dto.DocManagementDTO;
import com.viettel.service.base.model.BaseFWModelImpl;

@Entity
@Table(name="DOCUMENT_MANAGEMENT")
public class DocManagementBO extends BaseFWModelImpl{

	private Long id;
	private String field;
	private String documentType;
	private String documentCode;
	private String issuingUnit;
	private String description;
	private Date dateIssued;
	
	@Id
	@GeneratedValue(generator="sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = { @org.hibernate.annotations.Parameter(name = "sequence", value = "DOCUMENT_MANAGEMENT_SEQ") })
	@Column(name="ID", length=11)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="FIELD", length=255)
	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	@Column(name="DOCUMENT_TYPE", length=255)
	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	@Column(name="DOCUMENT_CODE", length=255)
	public String getDocumentCode() {
		return documentCode;
	}

	public void setDocumentCode(String documentCode) {
		this.documentCode = documentCode;
	}

	@Column(name="ISSUING_UNIT", length=255)
	public String getIssuingUnit() {
		return issuingUnit;
	}

	public void setIssuingUnit(String issuingUnit) {
		this.issuingUnit = issuingUnit;
	}

	@Column(name="DESCRIPTION", length=255)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name="DATE_ISSUED", length=12)
	public Date getDateIssued() {
		return dateIssued;
	}

	public void setDateIssued(Date dateIssued) {
		this.dateIssued = dateIssued;
	}

	@Override
	public DocManagementDTO toDTO() {
		
		DocManagementDTO dto = new DocManagementDTO();
		dto.setId(this.getId());
		dto.setField(this.getField());
		dto.setDocumentType(this.getDocumentType());
		dto.setDocumentCode(this.getDocumentCode());
		dto.setIssuingUnit(this.getIssuingUnit());
		dto.setDateIssued(this.getDateIssued());
		dto.setDescription(this.getDescription());
		return dto;
	}

}
