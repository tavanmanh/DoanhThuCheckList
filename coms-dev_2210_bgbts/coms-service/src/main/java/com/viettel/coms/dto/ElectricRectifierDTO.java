package com.viettel.coms.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.ElectricEarthingSytemBO;
import com.viettel.coms.bo.ElectricLightningCutFilterBO;
import com.viettel.coms.bo.ElectricRectifierBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;

public class ElectricRectifierDTO extends ComsBaseFWDTO<ElectricRectifierBO>{

	private Long id;
	private Long deviceId;
	private String serial;
	private Long kttsStatus;
	private String goodCode;
	private String goodName;
	private String model;
	private String manufacturer;
	private Long ratedPower;
	private Long quantityInUse;
	private Long quantitycanAdded;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date timeIntoUse;
	private Long state;
	private Long stateER;
	private List<AttachElectronicStationDTO> listImage1;
	private List<AttachElectronicStationDTO> listImage2;
	private List<AttachElectronicStationDTO> listImage3;
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

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public Long getKttsStatus() {
		return kttsStatus;
	}

	public void setKttsStatus(Long kttsStatus) {
		this.kttsStatus = kttsStatus;
	}

	public String getGoodCode() {
		return goodCode;
	}

	public void setGoodCode(String goodCode) {
		this.goodCode = goodCode;
	}

	public String getGoodName() {
		return goodName;
	}

	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public Long getRatedPower() {
		return ratedPower;
	}

	public void setRatedPower(Long ratedPower) {
		this.ratedPower = ratedPower;
	}

	public Long getQuantityInUse() {
		return quantityInUse;
	}

	public void setQuantityInUse(Long quantityInUse) {
		this.quantityInUse = quantityInUse;
	}

	public Long getQuantitycanAdded() {
		return quantitycanAdded;
	}

	public void setQuantitycanAdded(Long quantitycanAdded) {
		this.quantitycanAdded = quantitycanAdded;
	}

	public Date getTimeIntoUse() {
		return timeIntoUse;
	}

	public void setTimeIntoUse(Date timeIntoUse) {
		this.timeIntoUse = timeIntoUse;
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

	public Long getStateER() {
		return stateER;
	}

	public void setStateER(Long stateER) {
		this.stateER = stateER;
	}

	public List<AttachElectronicStationDTO> getListImage3() {
		return listImage3;
	}

	public void setListImage3(List<AttachElectronicStationDTO> listImage3) {
		this.listImage3 = listImage3;
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
	public ElectricRectifierBO toModel() {
		// TODO Auto-generated method stub
		ElectricRectifierBO obj = new ElectricRectifierBO();
		obj.setId(this.id);
		obj.setDeviceId(this.deviceId);
		obj.setSerial(this.serial);
		obj.setKttsStatus(this.kttsStatus);
		obj.setGoodCode(this.goodCode);
		obj.setGoodName(this.goodName);
		obj.setModel(this.model);
		obj.setManufacturer(this.manufacturer);
		obj.setRatedPower(this.ratedPower);
		obj.setQuantityInUse(this.quantityInUse);
		obj.setQuantitycanAdded(this.quantitycanAdded);
		obj.setTimeIntoUse(this.timeIntoUse);
		obj.setStateER(this.stateER);
		return obj;
	}

}
