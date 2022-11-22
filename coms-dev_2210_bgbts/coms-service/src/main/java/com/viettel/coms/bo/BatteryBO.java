package com.viettel.coms.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.BatteryDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

@SuppressWarnings("serial")
@Entity(name = "com.viettel.coms.bo.BatteryBO")
@Table(name = "BATTERY")
public class BatteryBO extends BaseFWModelImpl{

	@Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "BATTERY_SEQ")})
	
	@Column(name = "ID", length = 22)
	private Long id;
	@Column(name = "DEVICE_ID", length = 22)
	private Long deviceId;
	@Column(name = "BATTERY_NAME", length = 22)
	private String batteryName;
	@Column(name = "MODEL", length = 10)
	private String model;
	@Column(name = "PRODUCTION_TECHNOLOGY", length = 200)
	private String productionTechnology;
	@Column(name = "GOOD_CODE", length = 200)
	private String goodCode;
	@Column(name = "MANUFACTURER", length = 200)
	private String manufactuber;
	@Column(name = "BATTERY_TYPE", length = 200)
	private String batteryType;
	@Column(name = "CAPACITY", length = 10)
	private Long capacity;
	@Column(name = "TIME_INTO_USE")
	private Date timeIntoUse;
	@Column(name = "STATION_OUTPUT_TIME_AFTER_RECOVERY")
	private Date stationOutputTimeAfterRecover;
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


	public String getBatteryName() {
		return batteryName;
	}


	public void setBatteryName(String batteryName) {
		this.batteryName = batteryName;
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


	public String getManufactuber() {
		return manufactuber;
	}


	public void setManufactuber(String manufactuber) {
		this.manufactuber = manufactuber;
	}


	public String getBatteryType() {
		return batteryType;
	}


	public void setBatteryType(String batteryType) {
		this.batteryType = batteryType;
	}


	public Long getCapacity() {
		return capacity;
	}


	public void setCapacity(Long capacity) {
		this.capacity = capacity;
	}


	public Date getTimeIntoUse() {
		return timeIntoUse;
	}


	public void setTimeIntoUse(Date timeIntoUse) {
		this.timeIntoUse = timeIntoUse;
	}


	public Date getStationOutputTimeAfterRecover() {
		return stationOutputTimeAfterRecover;
	}


	public void setStationOutputTimeAfterRecover(Date stationOutputTimeAfterRecover) {
		this.stationOutputTimeAfterRecover = stationOutputTimeAfterRecover;
	}


	public Date getLastMaintenanceTime() {
		return lastMaintenanceTime;
	}


	public void setLastMaintenanceTime(Date lastMaintenanceTime) {
		this.lastMaintenanceTime = lastMaintenanceTime;
	}


	public String getProductionTechnology() {
		return productionTechnology;
	}


	public void setProductionTechnology(String productionTechnology) {
		this.productionTechnology = productionTechnology;
	}


	@Override
	public BatteryDTO toDTO() {
		// TODO Auto-generated method stub
		BatteryDTO obj = new BatteryDTO();
		obj.setId(this.id);
		obj.setDeviceId(this.deviceId);
		obj.setBatteryName(this.batteryName);
		obj.setGoodCode(this.goodCode);
		obj.setModel(this.model);
		obj.setManufactuber(this.manufactuber);
		obj.setBatteryType(this.batteryType);
		obj.setCapacity(this.capacity);
		obj.setProductionTechnology(this.productionTechnology);
		obj.setTimeIntoUse(this.timeIntoUse);
		obj.setStationOutputTimeAfterRecover(this.stationOutputTimeAfterRecover);
		obj.setLastMaintenanceTime(this.lastMaintenanceTime);
		return obj;
	}



}
