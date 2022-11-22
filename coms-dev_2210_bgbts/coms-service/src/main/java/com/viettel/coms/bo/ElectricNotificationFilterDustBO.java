package com.viettel.coms.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.ElectricNotificationFilterDustDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

@SuppressWarnings("serial")
@Entity(name = "com.viettel.coms.bo.ElectricNotificationFilterDustBO")
@Table(name = "ELECTRIC_NOTIFICATION_FILTER_DUST")
public class ElectricNotificationFilterDustBO extends BaseFWModelImpl {

	@Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "ELECTRIC_NOTIFICATION_FILTER_DUST_SEQ")})
	private Long id;
	@Column(name = "DEVICE_ID", length = 10)
	private Long deviceId;
	@Column(name = "ELECTRIC_NOTIFICATION_FILTER_DUST_NAME", length = 200)
	private String electricNotificationFilterDustName;
	@Column(name = "SERIAL", length = 22)
	private String serial;
	@Column(name = "STATE_SERIAL", length = 1)
	private String stateSerial;
	@Column(name = "MANUFACTURER", length = 200)
	private String manufacturer;
	@Column(name = "MODEL", length = 200)
	private String model;
	@Column(name = "GOOD_CODE", length = 10)
	private String goodCode;
	@Column(name = "MAXIMUM_CAPACITY", length = 10)
	private Long maximumCapacity;
	@Column(name = "STATE_KTTS", length = 200)
	private String stateKtts;
	@Column(name = "STATE_ENFD", length = 1)
	private Long stateENFD;
	@Column(name = "TIME_INTO_USE", length = 22)
	private Date timeIntoUse;
	@Column(name = "LAST_MAINTENANCE_TIME", length = 22)
	private Date lastMaintenanceTime;
	@Column(name = "NEAREST_REPAIR_TIME", length = 22)
	private Date nearestRepairTime;
	@Column(name = "TOTAL_REPAIR_COST", length = 10)
	private Long totalRepairCost;
	@Column(name = "TOTAL_NUMBER_FAILURES", length = 10)
	private Long totalNumberFailures;
	
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


	public String getElectricNotificationFilterDustName() {
		return electricNotificationFilterDustName;
	}


	public void setElectricNotificationFilterDustName(String electricNotificationFilterDustName) {
		this.electricNotificationFilterDustName = electricNotificationFilterDustName;
	}


	public String getSerial() {
		return serial;
	}


	public void setSerial(String serial) {
		this.serial = serial;
	}


	


	public String getStateSerial() {
		return stateSerial;
	}


	public void setStateSerial(String stateSerial) {
		this.stateSerial = stateSerial;
	}


	public String getManufacturer() {
		return manufacturer;
	}


	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}


	public String getModel() {
		return model;
	}


	public void setModel(String model) {
		this.model = model;
	}


	


	public String getGoodCode() {
		return goodCode;
	}


	public void setGoodCode(String goodCode) {
		this.goodCode = goodCode;
	}


	public Long getMaximumCapacity() {
		return maximumCapacity;
	}


	public void setMaximumCapacity(Long maximumCapacity) {
		this.maximumCapacity = maximumCapacity;
	}


	public String getStateKtts() {
		return stateKtts;
	}


	public void setStateKtts(String stateKtts) {
		this.stateKtts = stateKtts;
	}

	public Long getStateENFD() {
		return stateENFD;
	}


	public void setStateENFD(Long stateENFD) {
		this.stateENFD = stateENFD;
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


	public Date getNearestRepairTime() {
		return nearestRepairTime;
	}


	public void setNearestRepairTime(Date nearestRepairTime) {
		this.nearestRepairTime = nearestRepairTime;
	}


	public Long getTotalRepairCost() {
		return totalRepairCost;
	}


	public void setTotalRepairCost(Long totalRepairCost) {
		this.totalRepairCost = totalRepairCost;
	}


	public Long getTotalNumberFailures() {
		return totalNumberFailures;
	}


	public void setTotalNumberFailures(Long totalNumberFailures) {
		this.totalNumberFailures = totalNumberFailures;
	}


	@Override
	public BaseFWDTOImpl toDTO() {
		ElectricNotificationFilterDustDTO dto = new ElectricNotificationFilterDustDTO();
		dto.setId(this.getId());
		dto.setDeviceId(this.getDeviceId());
		dto.setElectricNotificationFilterDustName(this.getElectricNotificationFilterDustName());
		dto.setSerial(this.getSerial());
		dto.setStateSerial(this.getStateSerial());
		dto.setManufacturer(this.getManufacturer());
		dto.setModel(this.getModel());
		dto.setGoodCode(this.getGoodCode());
		dto.setMaximumCapacity(this.getMaximumCapacity());
		dto.setStateKtts(this.getStateKtts());
		dto.setStateENFD(this.getStateENFD());
		dto.setTimeIntoUse(this.getTimeIntoUse());
		dto.setLastMaintenanceTime(this.getLastMaintenanceTime());
		dto.setNearestRepairTime(this.getNearestRepairTime());
		dto.setTotalRepairCost(this.getTotalRepairCost());
		dto.setTotalNumberFailures(this.getTotalNumberFailures());
		return dto;
	}

}
