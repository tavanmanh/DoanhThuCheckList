package com.viettel.coms.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.AttachElectronicStationDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

@Entity
@Table(name = "ATTACH_ELECTRONIC_STATION")
public class AttachElectronicStationBO extends BaseFWModelImpl{
	
	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = {@Parameter(name = "sequence", value = "ATTACH_ELECTRONIC_STATION_SEQ")})
	@Column(name = "ATTACH_ELECTRONIC_STATION_ID", length = 10)
	private Long attachElctronicStationId;
	@Column(name = "OBJECT_ID", length = 10)
	private Long objectId;
	@Column(name = "TYPE", length = 50)
	private String type;
	@Column(name = "APP_PARAM_CODE", length = 200)
	private String appParamCode;
	@Column(name = "CODE", length = 100)
	private String code;
	@Column(name = "NAME", length = 1000)
	private String name;
	@Column(name = "ENCRYT_NAME", length = 200)
	private String encrytName;
	@Column(name = "DESCRIPTION", length = 1000)
	private String description;
	@Column(name = "STATUS", length = 1)
	private String status;
	@Column(name = "FILE_PATH", length = 1000)
	private String filePath;
	@Column(name = "CREATED_DATE", length = 22)
	private Date createdDate;
	@Column(name = "CREATED_USER_ID", length = 10)
	private Long createdUserId;
	@Column(name = "CREATED_USER_NAME", length = 1000)
	private String createdUserName;
	@Column(name = "LATITUDE", length = 38)
	private Double latitude;
	@Column(name = "LONGTITUDE", length = 38)
	private Double longtitude;
	@Column(name = "SWITCH_JOB", length = 2)
	private Long switchJob;
	
	public Long getAttachElctronicStationId() {
		return attachElctronicStationId;
	}

	public void setAttachElctronicStationId(Long attachElctronicStationId) {
		this.attachElctronicStationId = attachElctronicStationId;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAppParamCode() {
		return appParamCode;
	}

	public void setAppParamCode(String appParamCode) {
		this.appParamCode = appParamCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEncrytName() {
		return encrytName;
	}

	public void setEncrytName(String encrytName) {
		this.encrytName = encrytName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Long getCreatedUserId() {
		return createdUserId;
	}

	public void setCreatedUserId(Long createdUserId) {
		this.createdUserId = createdUserId;
	}

	public String getCreatedUserName() {
		return createdUserName;
	}

	public void setCreatedUserName(String createdUserName) {
		this.createdUserName = createdUserName;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(Double longtitude) {
		this.longtitude = longtitude;
	}

	public Long getSwitchJob() {
		return switchJob;
	}

	public void setSwitchJob(Long switchJob) {
		this.switchJob = switchJob;
	}

	@Override
	public BaseFWDTOImpl toDTO() {
		AttachElectronicStationDTO dto = new AttachElectronicStationDTO();
		dto.setAttachElctronicStationId(this.getAttachElctronicStationId());
		dto.setObjectId(this.getObjectId());
		dto.setType(this.getType());
		dto.setAppParamCode(this.getAppParamCode());
		dto.setCode(this.getCode());
		dto.setName(this.getName());
		dto.setEncrytName(this.getEncrytName());
		dto.setDescription(this.getDescription());
		dto.setStatus(this.getStatus());
		dto.setFilePath(this.getFilePath());
		dto.setCreatedDate(this.getCreatedDate());
		dto.setCreatedUserId(this.getCreatedUserId());
		dto.setCreatedUserName(this.getCreatedUserName());
		dto.setLatitude(this.getLatitude());
		dto.setLongtitude(this.getLongtitude());
		dto.setSwitchJob(this.getSwitchJob());
		return dto;
	}

}
