package com.viettel.coms.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.ElectricExplosionFactoryDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

@SuppressWarnings("serial")
@Entity(name = "com.viettel.coms.bo.ElectricExplosionFactoryBO")
@Table(name = "ELECTRIC_EXPLOSION_FACTORY")
public class ElectricExplosionFactoryBO extends BaseFWModelImpl{

	@Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "ELECTRIC_EXPLOSION_FACTORY_SEQ")})
	
	@Column(name = "ID", length = 22)
	private Long id;
	@Column(name = "DEVICE_ID", length = 22)
	private Long deviceId;
	@Column(name = "ELECTRIC_EXPLOSION_FACTORY_NAME", length = 200)
	private String electricExplosionName;
	@Column(name = "HOUSE_TYPE", length = 200)
	private String houseType;
	@Column(name = "IGNITER_SETTING_STATUS", length = 1)
	private Long igniterSettingStatus;
	@Column(name = "TIME_INTO_USE")
	private Date timeIntoUse;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public String getElectricExplosionName() {
		return electricExplosionName;
	}

	public void setElectricExplosionName(String electricExplosionName) {
		this.electricExplosionName = electricExplosionName;
	}

	public String getHouseType() {
		return houseType;
	}

	public void setHouseType(String houseType) {
		this.houseType = houseType;
	}

	public Long getIgniterSettingStatus() {
		return igniterSettingStatus;
	}

	public void setIgniterSettingStatus(Long igniterSettingStatus) {
		this.igniterSettingStatus = igniterSettingStatus;
	}

	public Date getTimeIntoUse() {
		return timeIntoUse;
	}

	public void setTimeIntoUse(Date timeIntoUse) {
		this.timeIntoUse = timeIntoUse;
	}

	@Override
	public ElectricExplosionFactoryDTO toDTO() {
		// TODO Auto-generated method stub
		ElectricExplosionFactoryDTO obj = new ElectricExplosionFactoryDTO();
		obj.setId(this.id);
		obj.setDeviceId(this.deviceId);
		obj.setHouseType(this.houseType);
		obj.setIgniterSettingStatus(this.igniterSettingStatus);
		obj.setElectricExplosionName(this.electricExplosionName);
		obj.setTimeIntoUse(this.timeIntoUse);
		return obj;
	}



}
