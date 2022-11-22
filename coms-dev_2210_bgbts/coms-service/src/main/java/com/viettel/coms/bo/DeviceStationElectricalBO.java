package com.viettel.coms.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.DeviceStationElectricalDTO;
import com.viettel.coms.dto.TmpnTargetOSDTO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;
import com.viettel.service.base.model.BaseFWModelImpl;

@SuppressWarnings("serial")
@Entity(name = "com.viettel.coms.bo.DeviceStationElectricalBO")
@Table(name = "DEVICE_STATION_ELECTRICAL")
public class DeviceStationElectricalBO extends BaseFWModelImpl{
	
	private java.lang.Long deviceId;
	private java.lang.String type;
	private java.lang.String deviceCode;
	private java.lang.String deviceName;
	
	private java.lang.String serial;
	
	private java.lang.String status;
	private java.lang.String state;
	
	private java.lang.Long stationId;
	
	private Date createDate;
	private java.lang.Long createUser;
	private String reason;

	private Long failureStatus;

	private Long createdUser;

	private Date createdDate;

	private Long approvedUser;

	private Date approvedDate;

	private String descriptionFailure;

	private String failure;
	
	@Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "DEVICE_STATION_ELECTRICAL_SEQ")})
    @Column(name = "DEVICE_ID", length = 22)
	public java.lang.Long getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(java.lang.Long deviceId) {
		this.deviceId = deviceId;
	}
	@Column(name = "TYPE", length = 20)
	public java.lang.String getType() {
		return type;
	}
	public void setType(java.lang.String type) {
		this.type = type;
	}
	@Column(name = "DEVICE_CODE", length = 100)
	public java.lang.String getDeviceCode() {
		return deviceCode;
	}
	public void setDeviceCode(java.lang.String deviceCode) {
		this.deviceCode = deviceCode;
	}
	@Column(name = "DEVICE_NAME", length = 100)
	public java.lang.String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(java.lang.String deviceName) {
		this.deviceName = deviceName;
	}
	@Column(name = "SERIAL", length = 100)
	public java.lang.String getSerial() {
		return serial;
	}
	public void setSerial(java.lang.String serial) {
		this.serial = serial;
	}
	@Column(name = "STATUS", length = 1)
	public java.lang.String getStatus() {
		return status;
	}
	public void setStatus(java.lang.String status) {
		this.status = status;
	}
	@Column(name = "STATE", length = 1)
	public java.lang.String getState() {
		return state;
	}
	public void setState(java.lang.String state) {
		this.state = state;
	}
	@Column(name = "STATION_ID", length = 22)
	public java.lang.Long getStationId() {
		return stationId;
	}
	public void setStationId(java.lang.Long stationId) {
		this.stationId = stationId;
	}
	@Column(name = "CREATE_DATE", length = 7)
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Column(name = "CREATE_USER", length = 22)
	public java.lang.Long getCreateUser() {
		return createUser;
	}
	public void setCreateUser(java.lang.Long createUser) {
		this.createUser = createUser;
	}
	@Column(name = "REASON", length = 500)
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	@Override
    public DeviceStationElectricalDTO toDTO() {
		DeviceStationElectricalDTO device = new DeviceStationElectricalDTO();
        // set cac gia tri
		device.setDeviceId(this.deviceId);
		device.setType(this.type);
		device.setDeviceCode(this.deviceCode);
		device.setDeviceName(this.deviceName);
		device.setSerial(this.serial);
		device.setStatus(this.status);
		device.setState(this.state);
		device.setStationId(this.stationId);
		device.setReason(this.reason);
		device.setFailureStatus(failureStatus);
		device.setCreateDate(createDate);
		device.setCreateUser(createUser);
		device.setApprovedUser(approvedUser);
		device.setApprovedDate(approvedDate);
		device.setDescriptionFailure(descriptionFailure);
        return device;
    }

	@Column(name = "FAILURE_STATUS")
	public Long getFailureStatus() {

		return failureStatus;
	}

	public void setFailureStatus(Long failureStatus) {

		this.failureStatus = failureStatus;
	}

	@Column(name = "CREATED_USER", length = 500)
	public Long getCreatedUser() {

		return createdUser;
	}

	public void setCreatedUser(Long createdUser) {

		this.createdUser = createdUser;
	}

	@Column(name = "CREATED_DATE", length = 500)
	public Date getCreatedDate() {

		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {

		this.createdDate = createdDate;
	}

	@Column(name = "APPROVED_USER", length = 500)
	public Long getApprovedUser() {

		return approvedUser;
	}

	public void setApprovedUser(Long approvedUser) {

		this.approvedUser = approvedUser;
	}

	@Column(name = "APPROVED_DATE", length = 500)
	public Date getApprovedDate() {

		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {

		this.approvedDate = approvedDate;
	}

	@Column(name = "DESCRIPTION_FAILURE", length = 500)
	public String getDescriptionFailure() {

		return descriptionFailure;
	}

	public void setDescriptionFailure(String descriptionFailure) {

		this.descriptionFailure = descriptionFailure;
	}

	@Column(name = "FAILURE", length = 500)
	public String getFailure() {

		return failure;
	}

	public void setFailure(String failure) {

		this.failure = failure;
	}
}
