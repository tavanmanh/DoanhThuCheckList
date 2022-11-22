package com.viettel.coms.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.CabinetsSourceDCDTO;
import com.viettel.coms.dto.ElectricATSDTO;
import com.viettel.coms.dto.ElectricWarningSystemDTO;
import com.viettel.coms.dto.GeneratorDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

@SuppressWarnings("serial")
@Entity(name = "com.viettel.coms.bo.ElectricATSBO")
@Table(name = "ELECTRIC_ATS")
public class ElectricATSBO extends BaseFWModelImpl{

	@Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "ELECTRIC_ATS_SEQ")})
	
	@Column(name = "ID", length = 22)
	private Long id;
	@Column(name = "DEVICE_ID", length = 22)
	private Long deviceId;
	@Column(name = "GOOD_CODE_KTTS", length = 1)
	private String goodCodeKTTS;
	@Column(name = "PHASE_NUMBER", length = 10)
	private Long phaseNumber;
	@Column(name = "ELECTRIC_QUOTA", length = 10)
	private Long electricQuota;
	@Column(name = "STATE_EA", length = 1)
	private Long stateEA;
	@Column(name = "GOOD_NAME", length = 200)
	private String goodName;
	@Column(name = "GOOD_ELECTRIC_NAME", length = 200)
	private String goodElectricName;
	@Column(name = "SERIAL", length = 200)
	private String serial;
	@Column(name = "MODEL", length = 200)
	private String model;
	@Column(name = "MANUFACTURER", length = 200)
	private String manufacturer;
	@Column(name = "TIME_INTO_USE")
	private Date timeIntoUse;
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
	
	public String getGoodCodeKTTS() {
		return goodCodeKTTS;
	}


	public void setGoodCodeKTTS(String goodCodeKTTS) {
		this.goodCodeKTTS = goodCodeKTTS;
	}


	public Long getPhaseNumber() {
		return phaseNumber;
	}


	public void setPhaseNumber(Long phaseNumber) {
		this.phaseNumber = phaseNumber;
	}


	public Long getElectricQuota() {
		return electricQuota;
	}


	public void setElectricQuota(Long electricQuota) {
		this.electricQuota = electricQuota;
	}


	public Long getStateEA() {
		return stateEA;
	}


	public void setStateEA(Long stateEA) {
		this.stateEA = stateEA;
	}


	public String getGoodName() {
		return goodName;
	}


	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}


	public String getGoodElectricName() {
		return goodElectricName;
	}


	public void setGoodElectricName(String goodElectricName) {
		this.goodElectricName = goodElectricName;
	}


	public String getSerial() {
		return serial;
	}


	public void setSerial(String serial) {
		this.serial = serial;
	}


	public String getModel() {
		return model;
	}


	public void setModel(String model) {
		this.model = model;
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


	public String getManufacturer() {
		return manufacturer;
	}


	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}


	@Override
	public ElectricATSDTO toDTO() {
		// TODO Auto-generated method stub
		ElectricATSDTO obj = new ElectricATSDTO();
		obj.setId(this.id);
		obj.setDeviceId(this.deviceId);
		obj.setGoodName(this.goodName);
		obj.setGoodElectricName(this.goodElectricName);
		obj.setSerial(this.serial);
		obj.setModel(this.model);
		obj.setGoodCodeKTTS(this.goodCodeKTTS);
		obj.setManufacturer(this.manufacturer);
		obj.setPhaseNumber(this.phaseNumber);
		obj.setElectricQuota(this.electricQuota);
		obj.setStateEA(this.stateEA);
		obj.setTimeIntoUse(this.timeIntoUse);
		obj.setLastMaintenanceTime(this.lastMaintenanceTime);
		return obj;
	}



}
