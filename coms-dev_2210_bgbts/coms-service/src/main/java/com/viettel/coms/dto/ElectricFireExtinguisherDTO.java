package com.viettel.coms.dto;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.ElectricFireExtinguisherBO;
import com.viettel.utils.CustomJsonDateDeserializer;
import com.viettel.utils.CustomJsonDateSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class ElectricFireExtinguisherDTO  extends ComsBaseFWDTO<ElectricFireExtinguisherBO>{

	private Long id;
	private Long deviceId;
	private String electricFireExtinguisherName;
	private String electricFireExtinguisherType;
	private Long weight;
	private Long electricFireExtinguisherState;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date timeIntoUse;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date lastMaintenanceTime;
	private Long state;
	private String electricFireExtinguisherLocation;
	private List<AttachElectronicStationDTO> listFileAttach;
	private List<AttachElectronicStationDTO> listImage1;
	private List<AttachElectronicStationDTO> listImage2;
	private List<AttachElectronicStationDTO> listImage3;
	private List<AttachElectronicStationDTO> listImage4;
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

	public String getElectricFireExtinguisherName() {
		return electricFireExtinguisherName;
	}

	public void setElectricFireExtinguisherName(String electricFireExtinguisherName) {
		this.electricFireExtinguisherName = electricFireExtinguisherName;
	}

	public String getElectricFireExtinguisherType() {
		return electricFireExtinguisherType;
	}

	public void setElectricFireExtinguisherType(String electricFireExtinguisherType) {
		this.electricFireExtinguisherType = electricFireExtinguisherType;
	}

	public Long getWeight() {
		return weight;
	}

	public void setWeight(Long weight) {
		this.weight = weight;
	}

	public Long getElectricFireExtinguisherState() {
		return electricFireExtinguisherState;
	}

	public void setElectricFireExtinguisherState(Long electricFireExtinguisherState) {
		this.electricFireExtinguisherState = electricFireExtinguisherState;
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

	public String getElectricFireExtinguisherLocation() {
		return electricFireExtinguisherLocation;
	}

	public void setElectricFireExtinguisherLocation(String electricFireExtinguisherLocation) {
		this.electricFireExtinguisherLocation = electricFireExtinguisherLocation;
	}

	public List<AttachElectronicStationDTO> getListFileAttach() {
		return listFileAttach;
	}

	public void setListFileAttach(List<AttachElectronicStationDTO> listFileAttach) {
		this.listFileAttach = listFileAttach;
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
	public ElectricFireExtinguisherBO toModel() {
		ElectricFireExtinguisherBO bo = new ElectricFireExtinguisherBO();
		bo.setId(this.getId());
		bo.setDeviceId(this.getDeviceId());
		bo.setElectricFireExtinguisherName(this.getElectricFireExtinguisherName());
		bo.setElectricFireExtinguisherType(this.getElectricFireExtinguisherType());
		bo.setWeight(this.getWeight());
		bo.setElectricFireExtinguisherState(this.getElectricFireExtinguisherState());
		bo.setTimeIntoUse(this.getTimeIntoUse());
		bo.setLastMaintenanceTime(this.getLastMaintenanceTime());
		bo.setElectricFireExtinguisherLocation(this.getElectricFireExtinguisherLocation());
		return bo;
	}

	
}
