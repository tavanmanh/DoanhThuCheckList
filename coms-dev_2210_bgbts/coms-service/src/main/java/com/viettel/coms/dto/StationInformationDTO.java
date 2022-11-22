package com.viettel.coms.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import com.viettel.coms.bo.StationInformationBO;

public class StationInformationDTO extends ComsBaseFWDTO<StationInformationBO>{

	private Long id;
	private Long deviceId;
	private String manager;
	private String address;
	private String stationType;
	private String stationHouseType;
	private String stationHouseSize;
	private Long wattageMax;
	private Long wattageMaxAcquy;
	private Long wattageAcquy;
	private Long wattageAirConditioning;
	private Long wattageDustFilter;
	private Long wattageHeadExchangers;
	private Long vtWattageTV;
	private Long vtWattageTransmission;
	private Long vtWattageIP;
	private Long vtWattageCDBR;
	private Long vtWattagePSTN;
	private Long vnpWattageTV;
	private Long vnpWattageTransmission;
	private Long vnpWattageIP;
	private Long vnpWattageCDBR;
	private Long vnpWattagePSTN;
	private Long vnmWattageTV;
	private Long vnmWattageTransmission;
	private Long vnmWattageIP;
	private Long vnmWattageCDBR;
	private Long vnmWattagePSTN;
	private Long mbpWattageTV;
	private Long mbpWattageTransmission;
	private Long mbpWattageIP;
	private Long mbpWattageCDBR;
	private Long mbpWattagePSTN;
	private Long state;
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
	private List<AttachElectronicStationDTO> listImage17;
	private List<AttachElectronicStationDTO> listImage18;
	private List<AttachElectronicStationDTO> listImage19;
	private List<AttachElectronicStationDTO> listImage20;
	private List<AttachElectronicStationDTO> listFileAttach;
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

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStationType() {
		return stationType;
	}

	public void setStationType(String stationType) {
		this.stationType = stationType;
	}

	public String getStationHouseType() {
		return stationHouseType;
	}

	public void setStationHouseType(String stationHouseType) {
		this.stationHouseType = stationHouseType;
	}

	public String getStationHouseSize() {
		return stationHouseSize;
	}

	public void setStationHouseSize(String stationHouseSize) {
		this.stationHouseSize = stationHouseSize;
	}

	public Long getWattageMax() {
		return wattageMax;
	}

	public void setWattageMax(Long wattageMax) {
		this.wattageMax = wattageMax;
	}

	public Long getWattageMaxAcquy() {
		return wattageMaxAcquy;
	}

	public void setWattageMaxAcquy(Long wattageMaxAcquy) {
		this.wattageMaxAcquy = wattageMaxAcquy;
	}

	public Long getWattageAcquy() {
		return wattageAcquy;
	}

	public void setWattageAcquy(Long wattageAcquy) {
		this.wattageAcquy = wattageAcquy;
	}

	public Long getWattageAirConditioning() {
		return wattageAirConditioning;
	}

	public void setWattageAirConditioning(Long wattageAirConditioning) {
		this.wattageAirConditioning = wattageAirConditioning;
	}

	public Long getWattageDustFilter() {
		return wattageDustFilter;
	}

	public void setWattageDustFilter(Long wattageDustFilter) {
		this.wattageDustFilter = wattageDustFilter;
	}

	public Long getWattageHeadExchangers() {
		return wattageHeadExchangers;
	}

	public void setWattageHeadExchangers(Long wattageHeadExchangers) {
		this.wattageHeadExchangers = wattageHeadExchangers;
	}

	public Long getVtWattageTV() {
		return vtWattageTV;
	}

	public void setVtWattageTV(Long vtWattageTV) {
		this.vtWattageTV = vtWattageTV;
	}

	public Long getVtWattageTransmission() {
		return vtWattageTransmission;
	}

	public void setVtWattageTransmission(Long vtWattageTransmission) {
		this.vtWattageTransmission = vtWattageTransmission;
	}

	public Long getVtWattageIP() {
		return vtWattageIP;
	}

	public void setVtWattageIP(Long vtWattageIP) {
		this.vtWattageIP = vtWattageIP;
	}

	public Long getVtWattageCDBR() {
		return vtWattageCDBR;
	}

	public void setVtWattageCDBR(Long vtWattageCDBR) {
		this.vtWattageCDBR = vtWattageCDBR;
	}

	public Long getVtWattagePSTN() {
		return vtWattagePSTN;
	}

	public void setVtWattagePSTN(Long vtWattagePSTN) {
		this.vtWattagePSTN = vtWattagePSTN;
	}

	public Long getVnpWattageTV() {
		return vnpWattageTV;
	}

	public void setVnpWattageTV(Long vnpWattageTV) {
		this.vnpWattageTV = vnpWattageTV;
	}

	public Long getVnpWattageTransmission() {
		return vnpWattageTransmission;
	}

	public void setVnpWattageTransmission(Long vnpWattageTransmission) {
		this.vnpWattageTransmission = vnpWattageTransmission;
	}

	public Long getVnpWattageIP() {
		return vnpWattageIP;
	}

	public void setVnpWattageIP(Long vnpWattageIP) {
		this.vnpWattageIP = vnpWattageIP;
	}

	public Long getVnpWattageCDBR() {
		return vnpWattageCDBR;
	}

	public void setVnpWattageCDBR(Long vnpWattageCDBR) {
		this.vnpWattageCDBR = vnpWattageCDBR;
	}

	public Long getVnpWattagePSTN() {
		return vnpWattagePSTN;
	}

	public void setVnpWattagePSTN(Long vnpWattagePSTN) {
		this.vnpWattagePSTN = vnpWattagePSTN;
	}

	public Long getVnmWattageTV() {
		return vnmWattageTV;
	}

	public void setVnmWattageTV(Long vnmWattageTV) {
		this.vnmWattageTV = vnmWattageTV;
	}

	public Long getVnmWattageTransmission() {
		return vnmWattageTransmission;
	}

	public void setVnmWattageTransmission(Long vnmWattageTransmission) {
		this.vnmWattageTransmission = vnmWattageTransmission;
	}

	public Long getVnmWattageIP() {
		return vnmWattageIP;
	}

	public void setVnmWattageIP(Long vnmWattageIP) {
		this.vnmWattageIP = vnmWattageIP;
	}

	public Long getVnmWattageCDBR() {
		return vnmWattageCDBR;
	}

	public void setVnmWattageCDBR(Long vnmWattageCDBR) {
		this.vnmWattageCDBR = vnmWattageCDBR;
	}

	public Long getVnmWattagePSTN() {
		return vnmWattagePSTN;
	}

	public void setVnmWattagePSTN(Long vnmWattagePSTN) {
		this.vnmWattagePSTN = vnmWattagePSTN;
	}

	public Long getMbpWattageTV() {
		return mbpWattageTV;
	}

	public void setMbpWattageTV(Long mbpWattageTV) {
		this.mbpWattageTV = mbpWattageTV;
	}

	public Long getMbpWattageTransmission() {
		return mbpWattageTransmission;
	}

	public void setMbpWattageTransmission(Long mbpWattageTransmission) {
		this.mbpWattageTransmission = mbpWattageTransmission;
	}

	public Long getMbpWattageIP() {
		return mbpWattageIP;
	}

	public void setMbpWattageIP(Long mbpWattageIP) {
		this.mbpWattageIP = mbpWattageIP;
	}

	public Long getMbpWattageCDBR() {
		return mbpWattageCDBR;
	}

	public void setMbpWattageCDBR(Long mbpWattageCDBR) {
		this.mbpWattageCDBR = mbpWattageCDBR;
	}

	public Long getMbpWattagePSTN() {
		return mbpWattagePSTN;
	}

	public void setMbpWattagePSTN(Long mbpWattagePSTN) {
		this.mbpWattagePSTN = mbpWattagePSTN;
	}

	public Long getState() {
		return state;
	}

	public void setState(Long state) {
		this.state = state;
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

	public List<AttachElectronicStationDTO> getListImage17() {
		return listImage17;
	}

	public void setListImage17(List<AttachElectronicStationDTO> listImage17) {
		this.listImage17 = listImage17;
	}

	public List<AttachElectronicStationDTO> getListImage18() {
		return listImage18;
	}

	public void setListImage18(List<AttachElectronicStationDTO> listImage18) {
		this.listImage18 = listImage18;
	}

	public List<AttachElectronicStationDTO> getListImage19() {
		return listImage19;
	}

	public void setListImage19(List<AttachElectronicStationDTO> listImage19) {
		this.listImage19 = listImage19;
	}

	public List<AttachElectronicStationDTO> getListImage20() {
		return listImage20;
	}

	public void setListImage20(List<AttachElectronicStationDTO> listImage20) {
		this.listImage20 = listImage20;
	}

	public List<AttachElectronicStationDTO> getListFileAttach() {
		return listFileAttach;
	}

	public void setListFileAttach(List<AttachElectronicStationDTO> listFileAttach) {
		this.listFileAttach = listFileAttach;
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
	public StationInformationBO toModel() {
		// TODO Auto-generated method stub
		StationInformationBO obj = new StationInformationBO();
		obj.setId(this.id);
		obj.setDeviceId(this.deviceId);
		obj.setManager(this.manager);
		obj.setAddress(this.address);
		obj.setStationType(this.stationType);
		obj.setStationHouseType(this.stationHouseType);
		obj.setStationHouseSize(this.stationHouseSize);
		obj.setWattageMax(this.wattageMax);
		obj.setWattageMaxAcquy(this.wattageMaxAcquy);
		obj.setWattageAcquy(this.wattageAcquy);
		obj.setWattageAirConditioning(this.wattageAirConditioning);
		obj.setWattageDustFilter(this.wattageDustFilter);
		obj.setWattageHeadExchangers(this.wattageHeadExchangers);
		obj.setVtWattageTV(this.vtWattageTV);
		obj.setVtWattageTransmission(this.vtWattageTransmission);
		obj.setVtWattageIP(this.vtWattageIP);
		obj.setVtWattageCDBR(this.vtWattageCDBR);
		obj.setVtWattagePSTN(this.vtWattagePSTN);
		obj.setVnpWattageTV(this.vnpWattageTV);
		obj.setVnpWattageTransmission(this.vnpWattageTransmission);
		obj.setVnpWattageIP(this.vnpWattageIP);
		obj.setVnpWattageCDBR(this.vnpWattageCDBR);
		obj.setVnpWattagePSTN(this.vnpWattagePSTN);
		obj.setVnmWattageTV(this.vnmWattageTV);
		obj.setVnmWattageTransmission(this.vnmWattageTransmission);
		obj.setVnmWattageIP(this.vnmWattageIP);
		obj.setVnmWattageCDBR(this.vnmWattageCDBR);
		obj.setVnmWattagePSTN(this.vnmWattagePSTN);
		obj.setMbpWattageTV(this.mbpWattageTV);
		obj.setMbpWattageTransmission(this.mbpWattageTransmission);
		obj.setMbpWattageIP(this.mbpWattageIP);
		obj.setMbpWattageCDBR(this.mbpWattageCDBR);
		obj.setMbpWattagePSTN(this.mbpWattagePSTN);
		return obj;
	}

}
