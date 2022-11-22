package com.viettel.coms.dto;

import java.util.List;

import javax.persistence.Column;

import com.viettel.coms.bo.CabinetsSourceACBO;

public class CabinetsSourceACDTO extends ComsBaseFWDTO<CabinetsSourceACBO>{

	private Long id;
	private Long deviceId;
	private String cabinetsSourceName;
	private Long phaseNumber;
	private Long status;
	private Long state;
	private List<AttachElectronicStationDTO> listFileAttach;
	private List<AttachElectronicStationDTO> listImage1;
	private List<AttachElectronicStationDTO> listImage2;
	private List<AttachElectronicStationDTO> listImage3;
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

	public String getCabinetsSourceName() {
		return cabinetsSourceName;
	}

	public void setCabinetsSourceName(String cabinetsSourceName) {
		this.cabinetsSourceName = cabinetsSourceName;
	}

	public Long getPhaseNumber() {
		return phaseNumber;
	}

	public void setPhaseNumber(Long phaseNumber) {
		this.phaseNumber = phaseNumber;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
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
	public CabinetsSourceACBO toModel() {
		// TODO Auto-generated method stub
		CabinetsSourceACBO bo = new CabinetsSourceACBO();
		bo.setId(this.id);
		bo.setDeviceId(this.deviceId);
		bo.setCabinetsSourceName(this.cabinetsSourceName);
		bo.setPhaseNumber(this.phaseNumber);
		bo.setStatus(this.status);
		return bo;
	}

}
