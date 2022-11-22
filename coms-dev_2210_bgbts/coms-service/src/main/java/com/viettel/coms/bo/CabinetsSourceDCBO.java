package com.viettel.coms.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.CabinetsSourceDCDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

@SuppressWarnings("serial")
@Entity(name = "com.viettel.coms.bo.CabinetsSourceDCBO")
@Table(name = "CABINETS_SOURCE_DC")
public class CabinetsSourceDCBO extends BaseFWModelImpl{

	@Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "CABINETS_SOURCE_DC_SEQ")})
	
	@Column(name = "ID", length = 22)
	private Long cabinetsDCId;
	@Column(name = "DEVICE_ID", length = 22)
	private Long deviceId;
	@Column(name = "PREVENTIVE", length = 22)
	private Long preventtive;
	@Column(name = "CABINETS_SOURCE_DC_NAME", length = 22)
	private String cabinetsSourceDCName;
	@Column(name = "STATE_CABINETS_SOURCE_DC", length = 22)
	private Long stateCabinetsSourceDC;
	@Column(name = "POWER_CABINET_MONITORING", length = 22)
	private Long powerCabinetMonitoring;
	@Column(name = "NOT_CHARGE_THE_BATTERY", length = 22)
	private String notChargeTheBattery;
	@Column(name = "CHARGE_THE_BATTERY", length = 22)
	private String chargeTheBattery;
	@Column(name = "CB_NUMBER_LESS_THAN_30A_UNUSED", length = 22)
	private Long cbNumberLessThan30AUnused;
	@Column(name = "CB_NUMBER_GREATER_THAN_30A_UNUSED", length = 22)
	private Long cbNumberGreaterThan30AUnused;
	@Column(name = "CB_NYMBER_ADDITION", length = 22)
	private Long cbNymberAddition;
	@Column(name = "STATE_RECTIFER", length = 22)
	private Long stateRectifer;
	@Column(name = "QUANTITY_USE", length = 22)
	private Long quantityUse;
	@Column(name = "QUANTITY_ADDITION", length = 22)
	private Long quantityAddition;
	@Column(name = "SERIAL", length = 22)
	private String serial;
	@Column(name = "DEVICE_MODEL", length = 22)
	private String deviceModel;
	@Column(name = "STATE_MODULE", length = 22)
	private Long stateModule;
	@Column(name = "RECFITER_NUMBER", length = 22)
	private Long recfiterNumber;
	
	
	@Override
	public CabinetsSourceDCDTO toDTO() {
		// TODO Auto-generated method stub
		CabinetsSourceDCDTO obj = new CabinetsSourceDCDTO();
		obj.setCabinetsDCId(this.cabinetsDCId);
		obj.setDeviceId(this.deviceId);
		obj.setPreventtive(this.preventtive);
		obj.setCabinetsSourceDCName(this.cabinetsSourceDCName);
		obj.setStateCabinetsSourceDC(this.stateCabinetsSourceDC);
		obj.setPowerCabinetMonitoring(this.powerCabinetMonitoring);
		obj.setNotChargeTheBattery(this.notChargeTheBattery);
		obj.setChargeTheBattery(this.chargeTheBattery);
		obj.setCbNumberLessThan30AUnused(this.cbNumberLessThan30AUnused);
		obj.setCbNumberGreaterThan30AUnused(this.cbNumberGreaterThan30AUnused);
		obj.setCbNymberAddition(this.cbNymberAddition);
		obj.setStateRectifer(this.stateRectifer);
		obj.setQuantityUse(this.quantityUse);
		obj.setQuantityAddition(this.quantityAddition);
		obj.setSerial(this.serial);
		obj.setNumberDeviceModel(this.deviceModel);
		obj.setStateModule(this.stateModule);
		obj.setRecfiterNumber(this.recfiterNumber);
		return obj;
	}


	public Long getCabinetsDCId() {
		return cabinetsDCId;
	}


	public void setCabinetsDCId(Long cabinetsDCId) {
		this.cabinetsDCId = cabinetsDCId;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public Long getPreventtive() {
		return preventtive;
	}

	public void setPreventtive(Long preventtive) {
		this.preventtive = preventtive;
	}

	public String getCabinetsSourceDCName() {
		return cabinetsSourceDCName;
	}

	public void setCabinetsSourceDCName(String cabinetsSourceDCName) {
		this.cabinetsSourceDCName = cabinetsSourceDCName;
	}

	public Long getStateCabinetsSourceDC() {
		return stateCabinetsSourceDC;
	}

	public void setStateCabinetsSourceDC(Long stateCabinetsSourceDC) {
		this.stateCabinetsSourceDC = stateCabinetsSourceDC;
	}

	public Long getPowerCabinetMonitoring() {
		return powerCabinetMonitoring;
	}

	public void setPowerCabinetMonitoring(Long powerCabinetMonitoring) {
		this.powerCabinetMonitoring = powerCabinetMonitoring;
	}

	public String getNotChargeTheBattery() {
		return notChargeTheBattery;
	}

	public void setNotChargeTheBattery(String notChargeTheBattery) {
		this.notChargeTheBattery = notChargeTheBattery;
	}

	public String getChargeTheBattery() {
		return chargeTheBattery;
	}

	public void setChargeTheBattery(String chargeTheBattery) {
		this.chargeTheBattery = chargeTheBattery;
	}

	public Long getCbNumberLessThan30AUnused() {
		return cbNumberLessThan30AUnused;
	}

	public void setCbNumberLessThan30AUnused(Long cbNumberLessThan30AUnused) {
		this.cbNumberLessThan30AUnused = cbNumberLessThan30AUnused;
	}

	public Long getCbNumberGreaterThan30AUnused() {
		return cbNumberGreaterThan30AUnused;
	}

	public void setCbNumberGreaterThan30AUnused(Long cbNumberGreaterThan30AUnused) {
		this.cbNumberGreaterThan30AUnused = cbNumberGreaterThan30AUnused;
	}

	public Long getCbNymberAddition() {
		return cbNymberAddition;
	}

	public void setCbNymberAddition(Long cbNymberAddition) {
		this.cbNymberAddition = cbNymberAddition;
	}

	public Long getStateRectifer() {
		return stateRectifer;
	}

	public void setStateRectifer(Long stateRectifer) {
		this.stateRectifer = stateRectifer;
	}

	public Long getQuantityUse() {
		return quantityUse;
	}

	public void setQuantityUse(Long quantityUse) {
		this.quantityUse = quantityUse;
	}

	public Long getQuantityAddition() {
		return quantityAddition;
	}

	public void setQuantityAddition(Long quantityAddition) {
		this.quantityAddition = quantityAddition;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}


	public Long getStateModule() {
		return stateModule;
	}

	public void setStateModule(Long stateModule) {
		this.stateModule = stateModule;
	}

	public Long getRecfiterNumber() {
		return recfiterNumber;
	}

	public void setRecfiterNumber(Long recfiterNumber) {
		this.recfiterNumber = recfiterNumber;
	}

}
