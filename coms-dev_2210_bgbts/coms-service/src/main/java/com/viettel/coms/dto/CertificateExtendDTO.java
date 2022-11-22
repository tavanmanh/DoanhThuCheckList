package com.viettel.coms.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.CertificateExtendBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;

@XmlRootElement(name = "CERTIFICATE_EXTENDBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CertificateExtendDTO extends ComsBaseFWDTO<CertificateExtendBO> {

	private Long certificateExtendId;
	
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date finishDate;
	
	private String description;
	private Long status;
	private String reason;
	
	private Long certificateId;
	private UtilAttachDocumentDTO utilAttachDocumentDTO;
	private String attachFileName;
	private String attachFileExtendPath;
	private Long attachOldFileId;
    @JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date createdDate;
    @JsonSerialize(using = JsonDateSerializerDate.class)
   	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date updatedDate;
	private Long createdUserId;
	private Long updatedUserId;
	
	private String updatedUserName;
    
    public CertificateExtendDTO() {
    	
	}

    @Override
    public CertificateExtendBO toModel() {
    	CertificateExtendBO certificateExtendBO = new CertificateExtendBO();
    	certificateExtendBO.setCertificateExtendId(this.certificateExtendId);
    	certificateExtendBO.setCertificateId(this.certificateId);
    	certificateExtendBO.setFinishDate(this.finishDate);
    	certificateExtendBO.setStatus(this.status);
    	certificateExtendBO.setDescription(this.description);
    	certificateExtendBO.setCreatedDate(this.createdDate);
    	certificateExtendBO.setCreatedUserId(this.createdUserId);
    	certificateExtendBO.setUpdatedDate(this.updatedDate);
    	certificateExtendBO.setUpdatedUserId(this.updatedUserId);
    	certificateExtendBO.setReason(this.reason);
        return certificateExtendBO;
    }
 
    @Override
    public String catchName() {
        return getCertificateExtendId().toString();
    }

	@Override
	public Long getFWModelId() {
		// TODO Auto-generated method stub
		return certificateExtendId;
	}

	public Long getCertificateExtendId() {
		return certificateExtendId;
	}

	public void setCertificateExtendId(Long certificateExtendId) {
		this.certificateExtendId = certificateExtendId;
	}

	public Date getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Long getCertificateId() {
		return certificateId;
	}

	public void setCertificateId(Long certificateId) {
		this.certificateId = certificateId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Long getCreatedUserId() {
		return createdUserId;
	}

	public void setCreatedUserId(Long createdUserId) {
		this.createdUserId = createdUserId;
	}

	public Long getUpdatedUserId() {
		return updatedUserId;
	}

	public void setUpdatedUserId(Long updatedUserId) {
		this.updatedUserId = updatedUserId;
	}

	public UtilAttachDocumentDTO getUtilAttachDocumentDTO() {
		return utilAttachDocumentDTO;
	}

	public void setUtilAttachDocumentDTO(UtilAttachDocumentDTO utilAttachDocumentDTO) {
		this.utilAttachDocumentDTO = utilAttachDocumentDTO;
	}

	public String getAttachFileName() {
		return attachFileName;
	}

	public void setAttachFileName(String attachFileName) {
		this.attachFileName = attachFileName;
	}

	public Long getAttachOldFileId() {
		return attachOldFileId;
	}

	public void setAttachOldFileId(Long attachOldFileId) {
		this.attachOldFileId = attachOldFileId;
	}

	public String getAttachFileExtendPath() {
		return attachFileExtendPath;
	}

	public void setAttachFileExtendPath(String attachFileExtendPath) {
		this.attachFileExtendPath = attachFileExtendPath;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getUpdatedUserName() {
		return updatedUserName;
	}

	public void setUpdatedUserName(String updatedUserName) {
		this.updatedUserName = updatedUserName;
	}
	
	
//Duong -end
}

