package com.viettel.coms.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.ElectricEarthingSystemDTO;
import com.viettel.coms.dto.ElectricExplosionFactoryDTO;
import com.viettel.coms.dto.ElectricLightningCutFilterDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

@SuppressWarnings("serial")
@Entity(name = "com.viettel.coms.bo.ElectricEarthingSytemBO")
@Table(name = "ELECTRIC_EARTHING_SYSTEM")
public class ElectricEarthingSytemBO extends BaseFWModelImpl{

	@Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "ELECTRIC_EARTHING_SYSTEM_SEQ")})
	
	@Column(name = "ID", length = 22)
	private Long id;
	@Column(name = "DEVICE_ID", length = 22)
	private Long deviceId;
	@Column(name = "ELECTRIC_EARTHING_SYSTEM_NAME", length = 200)
	private String electricEarthingSystemName;
	@Column(name = "GROUND_RESISTANCE", length = 10)
	private Long groundResistance;
	@Column(name = "GROUNDING_STATUS", length = 1)
	private Long groundStatus;
	@Column(name = "PRIMARY_CONDITION", length = 1)
	private Long primaryCondition;
	@Column(name = "LAST_MAINTENANCE_TIME")
	private Date lastMaintenanceTime;

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

	public String getElectricEarthingSystemName() {
		return electricEarthingSystemName;
	}

	public void setElectricEarthingSystemName(String electricEarthingSystemName) {
		this.electricEarthingSystemName = electricEarthingSystemName;
	}

	public Long getGroundResistance() {
		return groundResistance;
	}

	public void setGroundResistance(Long groundResistance) {
		this.groundResistance = groundResistance;
	}

	public Long getGroundStatus() {
		return groundStatus;
	}

	public void setGroundStatus(Long groundStatus) {
		this.groundStatus = groundStatus;
	}

	public Long getPrimaryCondition() {
		return primaryCondition;
	}

	public void setPrimaryCondition(Long primaryCondition) {
		this.primaryCondition = primaryCondition;
	}

	public Date getLastMaintenanceTime() {
		return lastMaintenanceTime;
	}

	public void setLastMaintenanceTime(Date lastMaintenanceTime) {
		this.lastMaintenanceTime = lastMaintenanceTime;
	}

	@Override
	public ElectricEarthingSystemDTO toDTO() {
		// TODO Auto-generated method stub
		ElectricEarthingSystemDTO obj = new ElectricEarthingSystemDTO();
		obj.setId(this.id);
		obj.setDeviceId(this.deviceId);
		obj.setElectricEarthingSystemName(this.electricEarthingSystemName);
		obj.setGroundResistance(this.groundResistance);
		obj.setGroundStatus(this.groundStatus);
		obj.setPrimaryCondition(this.primaryCondition);
		obj.setLastMaintenanceTime(this.lastMaintenanceTime);
		return obj;
	}



}
