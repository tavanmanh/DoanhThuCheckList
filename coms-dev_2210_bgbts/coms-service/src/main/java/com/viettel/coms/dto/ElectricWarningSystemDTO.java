package com.viettel.coms.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import com.viettel.coms.bo.ElectricWarningSystemBO;
public class ElectricWarningSystemDTO extends ComsBaseFWDTO<ElectricWarningSystemBO>{

	@Column(name = "ID", length = 22)
	private Long id;
	private Long deviceId;
	private Long stateACBox;
	private Long statusACBox;
	private Long stateLowBattery;
	private Long statusLowBattery;
	private Long stateTemperatureWarning;
	private Long statusTemperatureWarning;
	private Long stateSmokeWarning;
	private Long statusSmokeWarning;
	private Long statePowerCabinetMalfuntionWarning;
	private Long statusPowerCabinetMalfuntionWarning;
	private Long stateExplosiveFactoryOpenWarning;
	private Long statusExplosiveFactoryOpenWarning;
	private Long stateStationOpenWarning;
	private Long statusStationOpenWarning;
	private Long stateLowAC;
	private Long statusLowAC;
	private Long state;
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
	private List<AttachElectronicStationDTO> listImage16;
	private String areaCode;
	private String provinceCode;
	private String districtCode;
	private String stationCode;

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

	public Long getStateACBox() {
		return stateACBox;
	}

	public void setStateACBox(Long stateACBox) {
		this.stateACBox = stateACBox;
	}

	public Long getStatusACBox() {
		return statusACBox;
	}

	public void setStatusACBox(Long statusACBox) {
		this.statusACBox = statusACBox;
	}

	public Long getStateLowBattery() {
		return stateLowBattery;
	}

	public void setStateLowBattery(Long stateLowBattery) {
		this.stateLowBattery = stateLowBattery;
	}

	public Long getStatusLowBattery() {
		return statusLowBattery;
	}

	public void setStatusLowBattery(Long statusLowBattery) {
		this.statusLowBattery = statusLowBattery;
	}

	public Long getStateTemperatureWarning() {
		return stateTemperatureWarning;
	}

	public void setStateTemperatureWarning(Long stateTemperatureWarning) {
		this.stateTemperatureWarning = stateTemperatureWarning;
	}

	public Long getStatusTemperatureWarning() {
		return statusTemperatureWarning;
	}

	public void setStatusTemperatureWarning(Long statusTemperatureWarning) {
		this.statusTemperatureWarning = statusTemperatureWarning;
	}

	public Long getStateSmokeWarning() {
		return stateSmokeWarning;
	}

	public void setStateSmokeWarning(Long stateSmokeWarning) {
		this.stateSmokeWarning = stateSmokeWarning;
	}

	public Long getStatusSmokeWarning() {
		return statusSmokeWarning;
	}

	public void setStatusSmokeWarning(Long statusSmokeWarning) {
		this.statusSmokeWarning = statusSmokeWarning;
	}

	public Long getStatePowerCabinetMalfuntionWarning() {
		return statePowerCabinetMalfuntionWarning;
	}

	public void setStatePowerCabinetMalfuntionWarning(Long statePowerCabinetMalfuntionWarning) {
		this.statePowerCabinetMalfuntionWarning = statePowerCabinetMalfuntionWarning;
	}

	public Long getStatusPowerCabinetMalfuntionWarning() {
		return statusPowerCabinetMalfuntionWarning;
	}

	public void setStatusPowerCabinetMalfuntionWarning(Long statusPowerCabinetMalfuntionWarning) {
		this.statusPowerCabinetMalfuntionWarning = statusPowerCabinetMalfuntionWarning;
	}

	public Long getStateExplosiveFactoryOpenWarning() {
		return stateExplosiveFactoryOpenWarning;
	}

	public void setStateExplosiveFactoryOpenWarning(Long stateExplosiveFactoryOpenWarning) {
		this.stateExplosiveFactoryOpenWarning = stateExplosiveFactoryOpenWarning;
	}

	public Long getStatusExplosiveFactoryOpenWarning() {
		return statusExplosiveFactoryOpenWarning;
	}

	public void setStatusExplosiveFactoryOpenWarning(Long statusExplosiveFactoryOpenWarning) {
		this.statusExplosiveFactoryOpenWarning = statusExplosiveFactoryOpenWarning;
	}

	public Long getStateStationOpenWarning() {
		return stateStationOpenWarning;
	}

	public void setStateStationOpenWarning(Long stateStationOpenWarning) {
		this.stateStationOpenWarning = stateStationOpenWarning;
	}

	public Long getStatusStationOpenWarning() {
		return statusStationOpenWarning;
	}

	public void setStatusStationOpenWarning(Long statusStationOpenWarning) {
		this.statusStationOpenWarning = statusStationOpenWarning;
	}

	public Long getStateLowAC() {
		return stateLowAC;
	}

	public void setStateLowAC(Long stateLowAC) {
		this.stateLowAC = stateLowAC;
	}

	public Long getStatusLowAC() {
		return statusLowAC;
	}

	public void setStatusLowAC(Long statusLowAC) {
		this.statusLowAC = statusLowAC;
	}

	public Long getState() {
		return state;
	}

	public void setState(Long state) {
		this.state = state;
	}

	public List<AttachElectronicStationDTO> getListFileAttach() {
		return listFileAttach;
	}

	public void setListFileAttach(List<AttachElectronicStationDTO> listFileAttach) {
		this.listFileAttach = listFileAttach;
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

	public List<AttachElectronicStationDTO> getListImage16() {
		return listImage16;
	}

	public void setListImage16(List<AttachElectronicStationDTO> listImage16) {
		this.listImage16 = listImage16;
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
	public ElectricWarningSystemBO toModel() {
		// TODO Auto-generated method stub
		ElectricWarningSystemBO obj = new ElectricWarningSystemBO();
		obj.setId(this.id);
		obj.setDeviceId(this.deviceId);
		obj.setStateACBox(this.stateACBox);
		obj.setStatusACBox(this.statusACBox);
		obj.setStateLowBattery(this.stateLowBattery);
		obj.setStatusLowBattery(this.statusLowBattery);
		obj.setStateTemperatureWarning(this.stateTemperatureWarning);
		obj.setStatusTemperatureWarning(this.statusTemperatureWarning);
		obj.setStateSmokeWarning(this.stateSmokeWarning);
		obj.setStatusSmokeWarning(this.statusSmokeWarning);
		obj.setStatePowerCabinetMalfuntionWarning(this.statePowerCabinetMalfuntionWarning);
		obj.setStatusPowerCabinetMalfuntionWarning(this.statusPowerCabinetMalfuntionWarning);
		obj.setStateExplosiveFactoryOpenWarning(this.stateExplosiveFactoryOpenWarning);
		obj.setStatusExplosiveFactoryOpenWarning(this.statusExplosiveFactoryOpenWarning);
		obj.setStateStationOpenWarning(this.stateStationOpenWarning);
		obj.setStatusStationOpenWarning(this.statusStationOpenWarning);
		obj.setStateLowAC(this.stateLowAC);
		obj.setStatusLowAC(this.statusLowAC);
		return obj;
	}

}
