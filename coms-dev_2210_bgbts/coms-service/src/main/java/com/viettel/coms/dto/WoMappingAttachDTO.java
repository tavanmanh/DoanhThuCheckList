package com.viettel.coms.dto;

import com.viettel.coms.bo.WoMappingAttachBO;
import com.viettel.coms.bo.WoMappingChecklistBO;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WoMappingAttachDTO extends ComsBaseFWDTO<WoMappingAttachBO> {
    private Long id;
    private Long trId;
    private Long woId;
    private Long checklistId;
    private String fileName;
    private String filePath;
    private String userCreated;
    private Long status;
    private Date createdDate;
    private String type;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getTrId() {
        return trId;
    }

    public void setTrId(Long trId) {
        this.trId = trId;
    }

    public Long getWoId() {
        return woId;
    }

    public void setWoId(Long woId) {
        this.woId = woId;
    }

    public Long getChecklistId() {
        return checklistId;
    }

    public void setChecklistId(Long checklistId) {
        this.checklistId = checklistId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(String userCreated) {
        this.userCreated = userCreated;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
    public WoMappingAttachBO toModel() {
        WoMappingAttachBO woMappingAttachBO = new WoMappingAttachBO();
        woMappingAttachBO.setId(this.id);
        woMappingAttachBO.setTrId(this.trId);
        woMappingAttachBO.setWoId(this.woId);
        woMappingAttachBO.setChecklistId(this.checklistId);
        woMappingAttachBO.setFileName(this.fileName);
        woMappingAttachBO.setFilePath(this.filePath);
        woMappingAttachBO.setUserCreated(this.userCreated);
        woMappingAttachBO.setStatus(this.status);
        woMappingAttachBO.setCreatedDate(this.createdDate);
        woMappingAttachBO.setType(this.type);
        return woMappingAttachBO;
    }

    @Override
    public String catchName() {
        return getId().toString();
    }

    @Override
    public Long getFWModelId() {
        return id;
    }
}
