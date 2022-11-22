package com.viettel.coms.bo;

import com.viettel.coms.dto.WoMappingAttachDTO;
import com.viettel.service.base.model.BaseFWModelImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "WO_MAPPING_ATTACH")
public class WoMappingAttachBO extends BaseFWModelImpl {
    private Long id;
    private Long trId;
    private Long woId;
    private Long checklistId;
    private String fileName;
    private String filePath;
    private String userCreated;
    private Long status;
    private Double latitude;
    private Double longtitude;
    private Date createdDate;
    private String type;

    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(
            name = "sequence"
            , strategy = "sequence"
            , parameters = {@Parameter(name = "sequence", value = "WO_MAPPING_ATTACH_SEQ")}
    )
    @Column(name = "ID", length = 11)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "TR_ID")
    public Long getTrId() {
        return trId;
    }

    public void setTrId(Long trId) {
        this.trId = trId;
    }

    @Column(name = "WO_ID")
    public Long getWoId() {
        return woId;
    }

    public void setWoId(Long woId) {
        this.woId = woId;
    }

    @Column(name = "CHECKLIST_ID")
    public Long getChecklistId() {
        return checklistId;
    }

    public void setChecklistId(Long checklistId) {
        this.checklistId = checklistId;
    }

    @Column(name = "FILE_NAME")
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Column(name = "FILE_PATH")
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Column(name = "USER_CREATED")
    public String getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(String userCreated) {
        this.userCreated = userCreated;
    }

    @Column(name = "STATUS")
    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Column(name = "LATITUDE")
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Column(name = "LONGTITUDE")
    public Double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Double longtitude) {
        this.longtitude = longtitude;
    }

    @Column(name = "CREATED_DATE")
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
    public WoMappingAttachDTO toDTO() {
        WoMappingAttachDTO woMappingAttachDTO = new WoMappingAttachDTO();
        woMappingAttachDTO.setId(this.id);
        woMappingAttachDTO.setTrId(this.trId);
        woMappingAttachDTO.setWoId(this.woId);
        woMappingAttachDTO.setChecklistId(this.checklistId);
        woMappingAttachDTO.setFileName(this.fileName);
        woMappingAttachDTO.setFilePath(this.filePath);
        woMappingAttachDTO.setUserCreated(this.userCreated);
        woMappingAttachDTO.setStatus(this.status);
        woMappingAttachDTO.setCreatedDate(this.createdDate);
        woMappingAttachDTO.setType(this.type);
        return woMappingAttachDTO;
    }
}
