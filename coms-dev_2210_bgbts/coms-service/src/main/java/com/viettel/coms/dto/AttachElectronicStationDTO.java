package com.viettel.coms.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.AttachElectronicStationBO;
import com.viettel.utils.CustomJsonDateDeserializer;
import com.viettel.utils.CustomJsonDateSerializer;

@XmlRootElement(name = "ATTACH_ELECTRONIC_STATIONBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AttachElectronicStationDTO extends ComsBaseFWDTO<AttachElectronicStationBO>{

	private Long attachElctronicStationId;
	private Long objectId;
	private String type;
	private String appParamCode;
	private String code;
	private String name;
	private String encrytName;
	private String description;
	private String status;
	private String filePath;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
    @JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date createdDate;
	private Long createdUserId;
	private String createdUserName;
	private Double latitude;
	private Double longtitude;
	private Long switchJob;
	private String imageName;
	private String base64String;
	
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

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getBase64String() {
		return base64String;
	}

	public void setBase64String(String base64String) {
		this.base64String = base64String;
	}

	@Override
	public String catchName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getFWModelId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AttachElectronicStationBO toModel() {
		AttachElectronicStationBO bo = new AttachElectronicStationBO();
		bo.setAttachElctronicStationId(this.getAttachElctronicStationId());
		bo.setObjectId(this.getObjectId());
		bo.setType(this.getType());
		bo.setAppParamCode(this.getAppParamCode());
		bo.setCode(this.getCode());
		bo.setName(this.getName());
		bo.setEncrytName(this.getEncrytName());
		bo.setDescription(this.getDescription());
		bo.setStatus(this.getStatus());
		bo.setFilePath(this.getFilePath());
		bo.setCreatedDate(this.getCreatedDate());
		bo.setCreatedUserId(this.getCreatedUserId());
		bo.setCreatedUserName(this.getCreatedUserName());
		bo.setLatitude(this.getLatitude());
		bo.setLongtitude(this.getLongtitude());
		bo.setSwitchJob(this.getSwitchJob());
		return bo;
	}

}
