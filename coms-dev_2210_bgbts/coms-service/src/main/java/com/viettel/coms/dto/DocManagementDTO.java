package com.viettel.coms.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.viettel.coms.bo.DocManagementBO;

@XmlRootElement(name="DOCUMENT_MANAGEMENTBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocManagementDTO extends ComsBaseFWDTO<DocManagementBO>{
	
	private Long id;
	private String field;
	private String documentType;
	private String documentCode;
	private String issuingUnit;
	private String description;
	private Date dateIssued;
	private String attachFileName;
	
	private UtilAttachDocumentDTO attachFile;
	
	public DocManagementDTO() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getField() {
		return field;
	}

	public String getAttachFileName() {
		return attachFileName;
	}

	public void setAttachFileName(String attachFileName) {
		this.attachFileName = attachFileName;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getDocumentType() {
		return documentType;
	}

	public UtilAttachDocumentDTO getAttachFile() {
		return attachFile;
	}

	public void setAttachFile(UtilAttachDocumentDTO attachFile) {
		this.attachFile = attachFile;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDocumentCode() {
		return documentCode;
	}

	public void setDocumentCode(String documentCode) {
		this.documentCode = documentCode;
	}

	public String getIssuingUnit() {
		return issuingUnit;
	}

	public void setIssuingUnit(String issuingUnit) {
		this.issuingUnit = issuingUnit;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDateIssued() {
		return dateIssued;
	}

	public void setDateIssued(Date dateIssued) {
		this.dateIssued = dateIssued;
	}

	@Override
	public String catchName() {
		return null;
	}

	@Override
	public Long getFWModelId() {
		return this.getId();
	}

	@Override
	public DocManagementBO toModel() {
		DocManagementBO bo = new DocManagementBO();
		bo.setId(this.getFWModelId());
		bo.setField(this.getField());
		bo.setDocumentType(this.getDocumentType());
		bo.setDocumentCode(this.getDocumentCode());
		bo.setIssuingUnit(this.getIssuingUnit());
		bo.setDateIssued(this.getDateIssued());
		bo.setDescription(this.getDescription());
		return bo;
	}
	
	
}
