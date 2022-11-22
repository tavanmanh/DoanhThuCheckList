package com.viettel.coms.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.ElectricFireExtinguisherDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

@SuppressWarnings("serial")
@Entity(name = "com.viettel.coms.bo.ElectricFireExtinguisherBO")
@Table(name = "ELECTRIC_FIRE_EXTINGUISHER")
public class ElectricFireExtinguisherBO extends BaseFWModelImpl {

	@Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "ELECTRIC_FIRE_EXTINGUISHER_SEQ")})
	private Long id;
	@Column(name = "DEVICE_ID", length = 22)
	private Long deviceId;
	@Column(name = "ELECTRIC_FIRE_EXTINGUISHER_NAME", length = 200)
	private String electricFireExtinguisherName;
	@Column(name = "ELECTRIC_FIRE_EXTINGUISHER_TYPE", length = 200)
	private String electricFireExtinguisherType;
	@Column(name = "WEIGHT", length = 10)
	private Long weight;
	@Column(name = "ELECTRIC_FIRE_EXTINGUISHER_STATE", length = 1)
	private Long electricFireExtinguisherState;
	@Column(name = "TIME_INTO_USE", length = 22)
	private Date timeIntoUse;
	@Column(name = "LAST_MAINTENANCE_TIME", length = 22)
	private Date lastMaintenanceTime;
	@Column(name = "ELECTRIC_FIRE_EXTINGUISHER_LOCATION", length = 200)
	private String electricFireExtinguisherLocation;

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


	public String getElectricFireExtinguisherName() {
		return electricFireExtinguisherName;
	}


	public void setElectricFireExtinguisherName(String electricFireExtinguisherName) {
		this.electricFireExtinguisherName = electricFireExtinguisherName;
	}


	public String getElectricFireExtinguisherType() {
		return electricFireExtinguisherType;
	}


	public void setElectricFireExtinguisherType(String electricFireExtinguisherType) {
		this.electricFireExtinguisherType = electricFireExtinguisherType;
	}


	public Long getWeight() {
		return weight;
	}


	public void setWeight(Long weight) {
		this.weight = weight;
	}


	public Long getElectricFireExtinguisherState() {
		return electricFireExtinguisherState;
	}


	public void setElectricFireExtinguisherState(Long electricFireExtinguisherState) {
		this.electricFireExtinguisherState = electricFireExtinguisherState;
	}


	public Date getTimeIntoUse() {
		return timeIntoUse;
	}


	public void setTimeIntoUse(Date timeIntoUse) {
		this.timeIntoUse = timeIntoUse;
	}


	public Date getLastMaintenanceTime() {
		return lastMaintenanceTime;
	}


	public void setLastMaintenanceTime(Date lastMaintenanceTime) {
		this.lastMaintenanceTime = lastMaintenanceTime;
	}


	public String getElectricFireExtinguisherLocation() {
		return electricFireExtinguisherLocation;
	}


	public void setElectricFireExtinguisherLocation(String electricFireExtinguisherLocation) {
		this.electricFireExtinguisherLocation = electricFireExtinguisherLocation;
	}


	@Override
	public BaseFWDTOImpl toDTO() {
		ElectricFireExtinguisherDTO bo = new ElectricFireExtinguisherDTO();
		bo.setId(this.getId());
		bo.setDeviceId(this.getDeviceId());
		bo.setElectricFireExtinguisherName(this.getElectricFireExtinguisherName());
		bo.setElectricFireExtinguisherType(this.getElectricFireExtinguisherType());
		bo.setWeight(this.getWeight());
		bo.setElectricFireExtinguisherState(this.getElectricFireExtinguisherState());
		bo.setTimeIntoUse(this.getTimeIntoUse());
		bo.setLastMaintenanceTime(this.getLastMaintenanceTime());
		bo.setElectricFireExtinguisherLocation(this.getElectricFireExtinguisherLocation());
		return bo;
	}

}
