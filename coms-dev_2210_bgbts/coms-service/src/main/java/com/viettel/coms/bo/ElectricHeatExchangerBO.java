package com.viettel.coms.bo;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.ElectricHeatExchangerDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

@SuppressWarnings("serial")
@Entity(name = "com.viettel.coms.bo.ElectricHeatExchangerBO")
@Table(name = "ELECTRIC_HEAT_EXCHANGER")
public class ElectricHeatExchangerBO extends BaseFWModelImpl{

	@Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "ELECTRIC_HEAT_EXCHANGER_SEQ")})
	private Long id;
	@Column(name = "DEVICE_ID", length = 10)
	private Long deviceId;
	@Column(name = "DIVICE_NAME", length = 22)
	private String deviceName;
	@Column(name = "STATE_EHE", length = 22)
	private String stateEHE ;
	@Column(name = "SERIAL", length = 10)
	private String serial;
	@Column(name = "WATTAGE", length = 10)
	private Long wattage;
	@Column(name = "MANUFACTURER", length = 200)
	private String manufacturer;
	@Column(name = "MODEL", length = 200)
	private String model;
	@Column(name = "GOOD_NAME", length = 200)
	private String goodName;
	@Column(name = "GOOD_CODE", length = 10)
	private String goodCode;
	@Column(name = "GOOD_CODE_KTTS", length = 10)
	private String goodCodeKtts;
	@Column(name = "TIME_INTO_USE", length = 22)
	private Date timeIntoUse;
	@Column(name = "LAST_MAJOR_MAINTENANCE_TIME", length = 22)
	private Date lastMajorMaintenanceTime;
	
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
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getStateEHE() {
		return stateEHE;
	}

	public void setStateEHE(String stateEHE) {
		this.stateEHE = stateEHE;
	}


	public String getSerial() {
		return serial;
	}


	public void setSerial(String serial) {
		this.serial = serial;
	}


	public Long getWattage() {
		return wattage;
	}


	public void setWattage(Long wattage) {
		this.wattage = wattage;
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


	public String getGoodName() {
		return goodName;
	}


	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}


	public String getGoodCode() {
		return goodCode;
	}


	public void setGoodCode(String goodCode) {
		this.goodCode = goodCode;
	}


	public String getGoodCodeKtts() {
		return goodCodeKtts;
	}


	public void setGoodCodeKtts(String goodCodeKtts) {
		this.goodCodeKtts = goodCodeKtts;
	}


	public Date getTimeIntoUse() {
		return timeIntoUse;
	}


	public void setTimeIntoUse(Date timeIntoUse) {
		this.timeIntoUse = timeIntoUse;
	}


	public Date getLastMajorMaintenanceTime() {
		return lastMajorMaintenanceTime;
	}


	public void setLastMajorMaintenanceTime(Date lastMajorMaintenanceTime) {
		this.lastMajorMaintenanceTime = lastMajorMaintenanceTime;
	}


	@Override
	public BaseFWDTOImpl toDTO() {
		ElectricHeatExchangerDTO dto = new ElectricHeatExchangerDTO();
		dto.setId(this.getId());
		dto.setDeviceId(this.deviceId);
		dto.setDeviceName(this.deviceName);
		dto.setStateEHE(this.getStateEHE());
		dto.setSerial(this.getSerial());
		dto.setWattage(this.getWattage());
		dto.setManufacturer(this.getManufacturer());
		dto.setModel(this.getModel());
		dto.setGoodName(this.getGoodName());
		dto.setGoodCode(this.getGoodCode());
		dto.setGoodCodeKtts(this.getGoodCodeKtts());
		dto.setTimeIntoUse(this.getTimeIntoUse());
		dto.setLastMajorMaintenanceTime(this.getLastMajorMaintenanceTime());
		return dto;
	}

	
}
