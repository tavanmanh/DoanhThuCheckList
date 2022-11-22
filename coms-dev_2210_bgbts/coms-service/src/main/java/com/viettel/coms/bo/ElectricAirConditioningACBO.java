package com.viettel.coms.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.ElectricAirConditioningACDTO;
import com.viettel.service.base.model.BaseFWModelImpl;

@SuppressWarnings("serial")
@Entity(name = "com.viettel.coms.bo.ElectricAirConditioningACBO")
@Table(name = "ELECTRIC_AIR_CONDITIONING_AC")
public class ElectricAirConditioningACBO extends BaseFWModelImpl{
	@Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {@Parameter(name = "sequence", value = "ELECTRIC_AIR_CONDITIONING_AC_SEQ")})
	
	@Column(name = "ID", length = 11)
	private Long id;
	@Column(name = "DEVICE_ID", length = 22)
	private Long deviceId;
	@Column(name = "ELECTRIC_AIR_CONDITIONING_AC_NAME", length = 22)
	private String electricAirConditioningACName;
	@Column(name = "STATE_AIR_CONDITIONING", length = 1)
	private Long stateAirConditioning;
	@Column(name = "SERI_N_L", length = 22)
	private String seriNL;
	@Column(name = "MAX_FIX_TME")
	private Date maxFixTme ;
	@Column(name = "MANUFACTURER", length = 200)
	private String manufacturer ;
	@Column(name = "MODEL", length = 200)
	private String model;
	@Column(name = "GOOD_NAME", length = 200)
	private String goodName ;
	@Column(name = "GOOD_CODE", length = 22)
	private String goodCode;
	@Column(name = "GOOD_CODE_KTTS", length = 22)
	private String goodCodeKTTS;
	@Column(name = "TIME_INTO_USE")
	private Date timeIntoUse;
	@Column(name = "LAST_MAJOR_MAINTENANCE_TIME")
	private Date lastMajorMaintenanceTime;
	@Column(name = "MODEL_COLD_UNIT", length = 200)
	private String modelColdUnit ;
	@Column(name = "MANUFACTURER_COLD_UNIT", length = 200)
	private String manufacturerColdUnit;
	@Column(name = "GOOD_CODE_COLD_UNIT", length = 200)
	private String goodCodeColdUnit;
	@Column(name = "TYPE_COLD_UNIT", length = 1)
	private Long typeCodeUnit;
	@Column(name = "WATTAGE_DH_BTU", length = 22)
	private Long wattageDHBTU;
	@Column(name = "WATTAGE_ELICTRONIC", length = 22)
	private Long wattageElictronic;
	@Column(name = "MODEL_HOT_UNIT", length = 200)
	private String modelHotUnit;
	@Column(name = "MANUFACTURER_HOT_UNIT", length = 200)
	private String manufacturerHotUnit;
	@Column(name = "TOTAL_REPAIR_COST", length = 22)
	private Long totalRepairCost;
	@Column(name = "TOTAL_NUMBER_FAILURES", length = 22)
	private Long totalNumberFailures;
	@Column(name = "TYPE_OF_GAS", length = 200)
	private String typeOfGas;
	
	@Override
	public ElectricAirConditioningACDTO toDTO() {
		// TODO Auto-generated method stub
		ElectricAirConditioningACDTO obj = new ElectricAirConditioningACDTO();
		obj.setId(this.id);
		obj.setDeviceId(this.deviceId);
		obj.setElectricAirConditioningACName(this.electricAirConditioningACName);
		obj.setSeriNL(this.seriNL);
		obj.setMaxFixTme(this.maxFixTme);
		obj.setManufacturer(this.manufacturer);
		obj.setModel(this.model);
		obj.setGoodName(this.goodName);
		obj.setGoodCode(this.goodCode);
		obj.setGoodCodeKTTS(this.goodCodeKTTS);
		obj.setTimeIntoUse(this.timeIntoUse);
		obj.setLastMajorMaintenanceTime(this.lastMajorMaintenanceTime);
		obj.setModelColdUnit(this.modelColdUnit);
		obj.setManufacturerColdUnit(this.manufacturerColdUnit);
		obj.setGoodCodeColdUnit(this.goodCodeColdUnit);
		obj.setTypeCodeUnit(this.typeCodeUnit);
		obj.setWattageDHBTU(this.wattageDHBTU);
		obj.setWattageElictronic(this.wattageElictronic);
		obj.setModelHotUnit(this.modelHotUnit);
		obj.setManufacturerHotUnit(this.manufacturerHotUnit);
		obj.setTotalRepairCost(this.totalRepairCost);
		obj.setTotalNumberFailures(this.totalNumberFailures);
		obj.setTypeOfGas(this.typeOfGas);
		obj.setStateAirConditioning(this.stateAirConditioning);
		return obj;
	}


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

	public String getElectricAirConditioningACName() {
		return electricAirConditioningACName;
	}

	public void setElectricAirConditioningACName(String electricAirConditioningACName) {
		this.electricAirConditioningACName = electricAirConditioningACName;
	}

	public Long getStateAirConditioning() {
		return stateAirConditioning;
	}

	public void setStateAirConditioning(Long stateAirConditioning) {
		this.stateAirConditioning = stateAirConditioning;
	}

	public String getSeriNL() {
		return seriNL;
	}

	public void setSeriNL(String seriNL) {
		this.seriNL = seriNL;
	}

	public Date getMaxFixTme() {
		return maxFixTme;
	}

	public void setMaxFixTme(Date maxFixTme) {
		this.maxFixTme = maxFixTme;
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


	public String getGoodCodeKTTS() {
		return goodCodeKTTS;
	}


	public void setGoodCodeKTTS(String goodCodeKTTS) {
		this.goodCodeKTTS = goodCodeKTTS;
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

	public String getModelColdUnit() {
		return modelColdUnit;
	}

	public void setModelColdUnit(String modelColdUnit) {
		this.modelColdUnit = modelColdUnit;
	}

	public String getManufacturerColdUnit() {
		return manufacturerColdUnit;
	}

	public void setManufacturerColdUnit(String manufacturerColdUnit) {
		this.manufacturerColdUnit = manufacturerColdUnit;
	}

	public String getGoodCodeColdUnit() {
		return goodCodeColdUnit;
	}

	public void setGoodCodeColdUnit(String goodCodeColdUnit) {
		this.goodCodeColdUnit = goodCodeColdUnit;
	}

	public Long getTypeCodeUnit() {
		return typeCodeUnit;
	}

	public void setTypeCodeUnit(Long typeCodeUnit) {
		this.typeCodeUnit = typeCodeUnit;
	}

	public Long getWattageDHBTU() {
		return wattageDHBTU;
	}

	public void setWattageDHBTU(Long wattageDHBTU) {
		this.wattageDHBTU = wattageDHBTU;
	}

	public Long getWattageElictronic() {
		return wattageElictronic;
	}

	public void setWattageElictronic(Long wattageElictronic) {
		this.wattageElictronic = wattageElictronic;
	}

	public String getModelHotUnit() {
		return modelHotUnit;
	}

	public void setModelHotUnit(String modelHotUnit) {
		this.modelHotUnit = modelHotUnit;
	}

	public String getManufacturerHotUnit() {
		return manufacturerHotUnit;
	}

	public void setManufacturerHotUnit(String manufacturerHotUnit) {
		this.manufacturerHotUnit = manufacturerHotUnit;
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

	public String getTypeOfGas() {
		return typeOfGas;
	}

	public void setTypeOfGas(String typeOfGas) {
		this.typeOfGas = typeOfGas;
	}

}
