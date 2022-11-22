package com.viettel.coms.dto;

import java.util.List;

import javax.persistence.Column;

import com.viettel.coms.bo.CabinetsSourceDCBO;

public class CabinetsSourceDCDTO extends ComsBaseFWDTO<CabinetsSourceDCBO>{

	private Long cabinetsDCId;
	private Long id;
	private Long deviceId;
	private Long preventtive;
	private String cabinetsSourceDCName;
	private Long stateCabinetsSourceDC;
	private Long powerCabinetMonitoring;
	private String notChargeTheBattery;
	private String chargeTheBattery;
	private Long cbNumberLessThan30AUnused;
	private Long cbNumberGreaterThan30AUnused;
	private Long cbNymberAddition;
	private Long stateRectifer;
	private Long quantityUse;
	private Long quantityAddition;
	private String serial;
	private String numberDeviceModel;
	private Long stateModule;
	private Long state;
	private Long recfiterNumber;
	private String sireal;
	private List<AttachElectronicStationDTO> listFileAttach;
	private List<AttachElectronicStationDTO> listImage1;
	private List<AttachElectronicStationDTO> listImage2;
	private List<AttachElectronicStationDTO> listImage3;
	private List<AttachElectronicStationDTO> listImage4;
	private List<AttachElectronicStationDTO> listImage5;
	private List<AttachElectronicStationDTO> listImage6;
	private List<AttachElectronicStationDTO> listImage7;
	private List<AttachElectronicStationDTO> listImage8;
	private List<AttachElectronicStationDTO> listImage9;
	private List<AttachElectronicStationDTO> listImage10;
	private List<AttachElectronicStationDTO> listImage11;
	private List<AttachElectronicStationDTO> listImage12;
	private List<AttachElectronicStationDTO> listImage13;
	private List<AttachElectronicStationDTO> listImage14;
	private List<AttachElectronicStationDTO> listImage15;
	private String areaCode;
	private String provinceCode;
	private String districtCode;
	private String stationCode;

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

	public Long getStateModule() {
		return stateModule;
	}

	public void setStateModule(Long stateModule) {
		this.stateModule = stateModule;
	}

	public Long getState() {
		return state;
	}

	public void setState(Long state) {
		this.state = state;
	}

	public Long getRecfiterNumber() {
		return recfiterNumber;
	}

	public void setRecfiterNumber(Long recfiterNumber) {
		this.recfiterNumber = recfiterNumber;
	}

	public String getNumberDeviceModel() {
		return numberDeviceModel;
	}

	public void setNumberDeviceModel(String numberDeviceModel) {
		this.numberDeviceModel = numberDeviceModel;
	}

	public List<AttachElectronicStationDTO> getListFileAttach() {
		return listFileAttach;
	}

	public void setListFileAttach(List<AttachElectronicStationDTO> listFileAttach) {
		this.listFileAttach = listFileAttach;
	}

	public String getSireal() {
		return sireal;
	}

	public void setSireal(String sireal) {
		this.sireal = sireal;
	}

	public List<AttachElectronicStationDTO> getListImage1() {
		return listImage1;
	}

	public void setListImage1(List<AttachElectronicStationDTO> listImage1) {
		this.listImage1 = listImage1;
	}

	public List<AttachElectronicStationDTO> getListImage2() {
		return listImage2;
	}

	public void setListImage2(List<AttachElectronicStationDTO> listImage2) {
		this.listImage2 = listImage2;
	}

	public List<AttachElectronicStationDTO> getListImage3() {
		return listImage3;
	}

	public void setListImage3(List<AttachElectronicStationDTO> listImage3) {
		this.listImage3 = listImage3;
	}

	public List<AttachElectronicStationDTO> getListImage4() {
		return listImage4;
	}

	public void setListImage4(List<AttachElectronicStationDTO> listImage4) {
		this.listImage4 = listImage4;
	}

	public List<AttachElectronicStationDTO> getListImage5() {
		return listImage5;
	}

	public void setListImage5(List<AttachElectronicStationDTO> listImage5) {
		this.listImage5 = listImage5;
	}

	public List<AttachElectronicStationDTO> getListImage6() {
		return listImage6;
	}

	public void setListImage6(List<AttachElectronicStationDTO> listImage6) {
		this.listImage6 = listImage6;
	}

	public List<AttachElectronicStationDTO> getListImage7() {
		return listImage7;
	}

	public void setListImage7(List<AttachElectronicStationDTO> listImage7) {
		this.listImage7 = listImage7;
	}

	public List<AttachElectronicStationDTO> getListImage8() {
		return listImage8;
	}

	public void setListImage8(List<AttachElectronicStationDTO> listImage8) {
		this.listImage8 = listImage8;
	}

	public List<AttachElectronicStationDTO> getListImage9() {
		return listImage9;
	}

	public void setListImage9(List<AttachElectronicStationDTO> listImage9) {
		this.listImage9 = listImage9;
	}

	public List<AttachElectronicStationDTO> getListImage10() {
		return listImage10;
	}

	public void setListImage10(List<AttachElectronicStationDTO> listImage10) {
		this.listImage10 = listImage10;
	}

	public List<AttachElectronicStationDTO> getListImage11() {
		return listImage11;
	}

	public void setListImage11(List<AttachElectronicStationDTO> listImage11) {
		this.listImage11 = listImage11;
	}

	public List<AttachElectronicStationDTO> getListImage12() {
		return listImage12;
	}

	public void setListImage12(List<AttachElectronicStationDTO> listImage12) {
		this.listImage12 = listImage12;
	}

	public List<AttachElectronicStationDTO> getListImage13() {
		return listImage13;
	}

	public void setListImage13(List<AttachElectronicStationDTO> listImage13) {
		this.listImage13 = listImage13;
	}

	public List<AttachElectronicStationDTO> getListImage14() {
		return listImage14;
	}

	public void setListImage14(List<AttachElectronicStationDTO> listImage14) {
		this.listImage14 = listImage14;
	}

	public List<AttachElectronicStationDTO> getListImage15() {
		return listImage15;
	}

	public void setListImage15(List<AttachElectronicStationDTO> listImage15) {
		this.listImage15 = listImage15;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String catchName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getFWModelId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CabinetsSourceDCBO toModel() {
		// TODO Auto-generated method stub
		CabinetsSourceDCBO obj = new CabinetsSourceDCBO();
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
		obj.setDeviceModel(this.numberDeviceModel);
		obj.setStateModule(this.stateModule);
		obj.setRecfiterNumber(this.recfiterNumber);
		return obj;
	}

}
