package com.viettel.coms.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.ElectricAirConditioningDCDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

@SuppressWarnings("serial")
@Entity(name = "com.viettel.coms.bo.ElectricAirConditioningDCBO")
@Table(name = "ELECTRIC_AIR_CONDITIONING_DC")
public class ElectricAirConditioningDCBO extends BaseFWModelImpl {

	@Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "ELECTRIC_AIR_CONDITIONING_DC_SEQ")})
	private Long id;
	@Column(name = "DEVICE_ID", length = 10)
	private Long deviceId;
	@Column(name = "ELECTRIC_AIR_CONDITIONING_DC_NAME", length = 200)
	private String electricAirConditioningDcName;
	@Column(name = "SERIAL", length = 22)
	private String serial;
	@Column(name = "STATE_EACD", length = 1)
	private Long stateEACD;
	@Column(name = "MANUFACTURER", length = 200)
	private String manufacturer;
	@Column(name = "MODEL", length = 200)
	private String model;
	@Column(name = "REFRIGERATION_CAPACITY", length = 10)
	private Long refrigerationCapacity;
	@Column(name = "POWER_CAPACITY", length = 10)
	private Long powerCapacity;
	@Column(name = "GOOD_NAME", length = 200)
	private String goodName;
	@Column(name = "GOOD_CODE", length = 200)
	private String goodCode;
	@Column(name = "GOOD_CODE_KTTS", length = 200)
	private String goodCodeKtts;
	@Column(name = "LAST_MAINTENANCE_TIME", length = 22)
	private Date lastMaintenanceTime;
	@Column(name = "NEAREST_REPAIR_TIME", length = 22)
	private Date nearestRepairTime;
	@Column(name = "TOTAL_REPAIR_COST", length = 10)
	private Long totalRepairCost;
	@Column(name = "TOTAL_NUMBER_FAILURES", length = 10)
	private Long totalNumberFailures;
	@Column(name = "TYPE_GAS", length = 200)
	private String typeGas;
	@Column(name = "TIME_INTO_USE", length = 22)
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
	public String getElectricAirConditioningDcName() {
		return electricAirConditioningDcName;
	}
	public void setElectricAirConditioningDcName(String electricAirConditioningDcName) {
		this.electricAirConditioningDcName = electricAirConditioningDcName;
	}
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}
	
	public Long getStateEACD() {
		return stateEACD;
	}
	public void setStateEACD(Long stateEACD) {
		this.stateEACD = stateEACD;
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
	public Long getRefrigerationCapacity() {
		return refrigerationCapacity;
	}
	public void setRefrigerationCapacity(Long refrigerationCapacity) {
		this.refrigerationCapacity = refrigerationCapacity;
	}
	public Long getPowerCapacity() {
		return powerCapacity;
	}
	public void setPowerCapacity(Long powerCapacity) {
		this.powerCapacity = powerCapacity;
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
	public String getTypeGas() {
		return typeGas;
	}
	public void setTypeGas(String typeGas) {
		this.typeGas = typeGas;
	}
	
	public Date getTimeIntoUse() {
		return timeIntoUse;
	}
	public void setTimeIntoUse(Date timeIntoUse) {
		this.timeIntoUse = timeIntoUse;
	}
	@Override
	public BaseFWDTOImpl toDTO() {
		ElectricAirConditioningDCDTO bo = new ElectricAirConditioningDCDTO();
		bo.setId(this.getId());
		bo.setDeviceId(this.getDeviceId());
		bo.setElectricAirConditioningDcName(this.getElectricAirConditioningDcName());
		bo.setSerial(this.getSerial());
		bo.setStateEACD(this.getStateEACD());
		bo.setManufacturer(this.getManufacturer());
		bo.setModel(this.getModel());
		bo.setRefrigerationCapacity(this.getRefrigerationCapacity());
		bo.setPowerCapacity(this.getPowerCapacity());
		bo.setGoodName(this.getGoodName());
		bo.setGoodCode(this.getGoodCode());
		bo.setGoodCodeKtts(this.getGoodCodeKtts());
		bo.setLastMaintenanceTime(this.getLastMaintenanceTime());
		bo.setNearestRepairTime(this.getNearestRepairTime());
		bo.setTotalRepairCost(this.getTotalRepairCost());
		bo.setTotalNumberFailures(this.getTotalNumberFailures());
		bo.setTypeGas(this.getTypeGas());
		bo.setTimeIntoUse(this.getTimeIntoUse());
		return bo;
	}
	
	
}
