package com.viettel.coms.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.ElectricLightningCutFilterBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;

public class ElectricLightningCutFilterDTO extends ComsBaseFWDTO<ElectricLightningCutFilterBO>{

	private Long id;
	private Long deviceId;
	private String electricLightningCutFilterName;
	private Long primaryStatus;
	private Long primaryQuantity;
	private Long primaryCondition;
	private String primarySpecies;
	private Long resistor;
	private Long secondaryStatus;
	private Long secondaryQuantity;
	private Long secondaryCodition;
	private String secondarySpecies;
	private String otherLightningCutFilterName;
	private Long otherLightningCutFilterStatus;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date timeIntoUse;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date lastMaintenanceTime;
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

	public String getElectricLightningCutFilterName() {
		return electricLightningCutFilterName;
	}

	public void setElectricLightningCutFilterName(String electricLightningCutFilterName) {
		this.electricLightningCutFilterName = electricLightningCutFilterName;
	}

	public Long getPrimaryStatus() {
		return primaryStatus;
	}

	public void setPrimaryStatus(Long primaryStatus) {
		this.primaryStatus = primaryStatus;
	}

	public Long getPrimaryQuantity() {
		return primaryQuantity;
	}

	public void setPrimaryQuantity(Long primaryQuantity) {
		this.primaryQuantity = primaryQuantity;
	}

	public Long getPrimaryCondition() {
		return primaryCondition;
	}

	public void setPrimaryCondition(Long primaryCondition) {
		this.primaryCondition = primaryCondition;
	}

	public String getPrimarySpecies() {
		return primarySpecies;
	}

	public void setPrimarySpecies(String primarySpecies) {
		this.primarySpecies = primarySpecies;
	}

	public Long getResistor() {
		return resistor;
	}

	public void setResistor(Long resistor) {
		this.resistor = resistor;
	}

	public Long getSecondaryStatus() {
		return secondaryStatus;
	}

	public void setSecondaryStatus(Long secondaryStatus) {
		this.secondaryStatus = secondaryStatus;
	}

	public Long getSecondaryQuantity() {
		return secondaryQuantity;
	}

	public void setSecondaryQuantity(Long secondaryQuantity) {
		this.secondaryQuantity = secondaryQuantity;
	}

	public Long getSecondaryCodition() {
		return secondaryCodition;
	}

	public void setSecondaryCodition(Long secondaryCodition) {
		this.secondaryCodition = secondaryCodition;
	}

	public String getSecondarySpecies() {
		return secondarySpecies;
	}

	public void setSecondarySpecies(String secondarySpecies) {
		this.secondarySpecies = secondarySpecies;
	}

	public String getOtherLightningCutFilterName() {
		return otherLightningCutFilterName;
	}

	public void setOtherLightningCutFilterName(String otherLightningCutFilterName) {
		this.otherLightningCutFilterName = otherLightningCutFilterName;
	}

	public Long getOtherLightningCutFilterStatus() {
		return otherLightningCutFilterStatus;
	}

	public void setOtherLightningCutFilterStatus(Long otherLightningCutFilterStatus) {
		this.otherLightningCutFilterStatus = otherLightningCutFilterStatus;
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
	public ElectricLightningCutFilterBO toModel() {
		// TODO Auto-generated method stub
		ElectricLightningCutFilterBO obj = new ElectricLightningCutFilterBO();
		obj.setId(this.id);
		obj.setDeviceId(this.deviceId);
		obj.setElectricLightningCutFilterName(this.electricLightningCutFilterName);
		obj.setPrimaryStatus(this.primaryStatus);
		obj.setPrimaryQuantity(this.primaryQuantity);
		obj.setPrimaryCondition(this.primaryCondition);
		obj.setPrimarySpecies(this.primarySpecies);
		obj.setResistor(this.resistor);
		obj.setSecondaryStatus(this.secondaryStatus);
		obj.setSecondaryQuantity(this.secondaryQuantity);
		obj.setSecondaryCodition(this.secondaryCodition);
		obj.setSecondarySpecies(this.secondarySpecies);
		obj.setOtherLightningCutFilterName(this.otherLightningCutFilterName);
		obj.setOtherLightningCutFilterStatus(this.otherLightningCutFilterStatus);
		obj.setTimeIntoUse(this.timeIntoUse);
		obj.setLastMaintenanceTime(this.lastMaintenanceTime);
		return obj;
	}

}
