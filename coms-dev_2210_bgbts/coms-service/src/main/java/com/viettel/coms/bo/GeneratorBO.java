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
import com.viettel.coms.dto.GeneratorDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

@SuppressWarnings("serial")
@Entity(name = "com.viettel.coms.bo.GeneratorBO")
@Table(name = "GENERATOR")
public class GeneratorBO extends BaseFWModelImpl{

	@Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "GENERATOR_SEQ")})
	
	@Column(name = "ID", length = 22)
	private Long id;
	@Column(name = "DEVICE_ID", length = 22)
	private Long deviceId;
	@Column(name = "GENERATOR_NAME", length = 22)
	private String generatorName;
	@Column(name = "MODEL", length = 22)
	private String model;
	@Column(name = "MANUFACTURER", length = 200)
	private String manufactuber;
	@Column(name = "FUEL_TYPE", length = 200)
	private String fuelType;
	@Column(name = "SERIAL", length = 200)
	private String serial;
	@Column(name = "STATION_CODE_BEFORE_TRANSFER", length = 200)
	private String stationCodeBeforeTransfer;
	@Column(name = "RATED_POWER", length = 22)
	private Long ratedPower;
	@Column(name = "WATTAGE_MAX", length = 22)
	private Long wattageMax;
	@Column(name = "WORKSTATION_STATUS", length = 1)
	private Long workstationStatus;
	@Column(name = "DISTANCE_GENERATOR_STATION", length = 10)
	private Long distanceGeneratorStation;
	@Column(name = "STATUS", length = 22)
	private Long status;
	@Column(name = "TOTAL_RUNNING_TIME", length = 22)
	private Long totalRunningTime;
	@Column(name = "TOTAL_REPAIR_COST", length = 22)
	private Long totalRepairCost;
	@Column(name = "TOTAL_NUMBER_FAILURES", length = 22)
	private Long totalNumberFailures;
	@Column(name = "GENERATOR_OVER_CAPACITY", length = 22)
	private String generatorOverCapacity;
	@Column(name = "FT_CONFIRM_GENERATOR_OVER_CAPACITY", length = 22)
	private Long ftConfirmGeneratorOverCapacity;
	@Column(name = "TIME_INTO_USE")
	private Date timeIntoUse;
	@Column(name = "TIME_LAST_MAINTENANCE")
	private Date timeLastMaintenance;
	@Column(name = "LAST_REPAIR_TIME")
	private Date lastRepairTime;
	
	
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


	public String getGeneratorName() {
		return generatorName;
	}


	public void setGeneratorName(String generatorName) {
		this.generatorName = generatorName;
	}


	public String getModel() {
		return model;
	}


	public void setModel(String model) {
		this.model = model;
	}


	public String getManufactuber() {
		return manufactuber;
	}


	public void setManufactuber(String manufactuber) {
		this.manufactuber = manufactuber;
	}


	public String getFuelType() {
		return fuelType;
	}


	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}


	public String getSerial() {
		return serial;
	}


	public void setSerial(String serial) {
		this.serial = serial;
	}


	public String getStationCodeBeforeTransfer() {
		return stationCodeBeforeTransfer;
	}


	public void setStationCodeBeforeTransfer(String stationCodeBeforeTransfer) {
		this.stationCodeBeforeTransfer = stationCodeBeforeTransfer;
	}


	public Long getRatedPower() {
		return ratedPower;
	}


	public void setRatedPower(Long ratedPower) {
		this.ratedPower = ratedPower;
	}


	public Long getWattageMax() {
		return wattageMax;
	}


	public void setWattageMax(Long wattageMax) {
		this.wattageMax = wattageMax;
	}


	public Long getWorkstationStatus() {
		return workstationStatus;
	}


	public void setWorkstationStatus(Long workstationStatus) {
		this.workstationStatus = workstationStatus;
	}


	public Long getDistanceGeneratorStation() {
		return distanceGeneratorStation;
	}


	public void setDistanceGeneratorStation(Long distanceGeneratorStation) {
		this.distanceGeneratorStation = distanceGeneratorStation;
	}


	public Long getStatus() {
		return status;
	}


	public void setStatus(Long status) {
		this.status = status;
	}


	public Long getTotalRunningTime() {
		return totalRunningTime;
	}


	public void setTotalRunningTime(Long totalRunningTime) {
		this.totalRunningTime = totalRunningTime;
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


	public String getGeneratorOverCapacity() {
		return generatorOverCapacity;
	}


	public void setGeneratorOverCapacity(String generatorOverCapacity) {
		this.generatorOverCapacity = generatorOverCapacity;
	}


	public Long getFtConfirmGeneratorOverCapacity() {
		return ftConfirmGeneratorOverCapacity;
	}


	public void setFtConfirmGeneratorOverCapacity(Long ftConfirmGeneratorOverCapacity) {
		this.ftConfirmGeneratorOverCapacity = ftConfirmGeneratorOverCapacity;
	}


	public Date getTimeIntoUse() {
		return timeIntoUse;
	}


	public void setTimeIntoUse(Date timeIntoUse) {
		this.timeIntoUse = timeIntoUse;
	}


	public Date getTimeLastMaintenance() {
		return timeLastMaintenance;
	}


	public void setTimeLastMaintenance(Date timeLastMaintenance) {
		this.timeLastMaintenance = timeLastMaintenance;
	}


	public Date getLastRepairTime() {
		return lastRepairTime;
	}


	public void setLastRepairTime(Date lastRepairTime) {
		this.lastRepairTime = lastRepairTime;
	}


	@Override
	public GeneratorDTO toDTO() {
		// TODO Auto-generated method stub
		GeneratorDTO obj = new GeneratorDTO();
		obj.setId(this.id);
		obj.setDeviceId(this.deviceId);
		obj.setGeneratorName(this.generatorName);
		obj.setModel(this.model);
		obj.setRatedPower(this.ratedPower);
		obj.setManufactuber(this.manufactuber);
		obj.setFuelType(this.fuelType);
		obj.setWattageMax(this.wattageMax);
		obj.setSerial(this.serial);
		obj.setWorkstationStatus(this.workstationStatus);
		obj.setDistanceGeneratorStation(this.distanceGeneratorStation);
		obj.setStatus(this.status);
		obj.setStationCodeBeforeTransfer(this.stationCodeBeforeTransfer);
		obj.setTotalRunningTime(this.totalRunningTime);
		obj.setTimeIntoUse(this.timeIntoUse);
		obj.setTimeLastMaintenance(this.timeLastMaintenance);
		obj.setLastRepairTime(this.lastRepairTime);
		obj.setTotalRepairCost(this.totalRepairCost);
		obj.setTotalNumberFailures(this.totalNumberFailures);
		obj.setGeneratorOverCapacity(this.generatorOverCapacity);
		obj.setFtConfirmGeneratorOverCapacity(this.ftConfirmGeneratorOverCapacity);
		return obj;
	}



}
