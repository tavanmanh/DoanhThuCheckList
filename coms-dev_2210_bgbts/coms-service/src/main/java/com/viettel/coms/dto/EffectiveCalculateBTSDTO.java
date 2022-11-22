package com.viettel.coms.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.xml.bind.annotation.XmlRootElement;

import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.viettel.cat.dto.ConstructionImageInfo;
import com.viettel.coms.bo.EffectiveCalculateBTSBO;
import com.viettel.coms.bo.EffectiveCalculateDASCDBRBO;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@XmlRootElement(name = "EffectiveCalculateDASCDBRBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EffectiveCalculateBTSDTO extends
		ComsBaseFWDTO<EffectiveCalculateBTSBO> {

	private Long effectiveCalculateBTSId;
	private Long catProvinceId;
	private String catProvinceCode;
	private String catProvinceName;
	private String latitude;
	private String longitude;
	private Double hightBts;
	private String columnType;
	private String topographic;
	private String location;
	private String address;
	private String typeStation;
	private String mnoViettel;
	private String mnoVina;
	private String mnoMobi;
	private Long ourceDeployment;
	private String lectDepreciationPeriod;
	private String silkEnterPrice;
	private String price;
	private String costMB;
	private String columnFoundationItems;
	private Double costColumnFoundationItems;
	private String houseFoundationItems;
	private Double costHouseFoundationItems;
	private String columnBodyCategory;
	private Double costColumnBodyCategory;
	private String machineRoomItems;
	private Double costMachineRoomItems;
	private String groundingItems;
	private Double costGroundingItems;
	private String electricTowingItems;
	private Double costElectricTowingItems;
	private String columnMountingItem;
	private Double costColumnMountingItem;
	private String installationHouses;
	private Double costInstallationHouses;
	private String electricalItems;
	private Double costElectricalItems;
	private String motorizedTransportItems;
	private Double costMotorizedTransportItems;
	private String itemManualShipping;
	private Double costItemManualShipping;
	private String itemShipping;
	private Double costItemShipping;
	private Double costItemsOtherExpenses;
	private Double owerCabinetCoolingSystem;
	private Double rectifier3000;
	private Double batteryLithium;
	private Double oilGenerator;
	private Double ats;
	private Double supervisionControl;
	private Double otherAuxiliarySystem;
	private Double publicInstallationPower;
	private String effective;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private java.util.Date createdDate;
	private Long createdUserId;
	private String parType;
	private String nameParam;

	public String getNameParam() {
		return nameParam;
	}

	public void setNameParam(String nameParam) {
		this.nameParam = nameParam;
	}

	public String getParType() {
		return parType;
	}

	public void setParType(String parType) {
		this.parType = parType;
	}

	public String getCatProvinceName() {
		return catProvinceName == null ? "" : catProvinceName.trim();
	}

	public void setCatProvinceName(String catProvinceName) {
		this.catProvinceName = catProvinceName;
	}

	public Long getEffectiveCalculateBTSId() {
		return effectiveCalculateBTSId != null ? effectiveCalculateBTSId : 0;
	}

	public void setEffectiveCalculateBTSId(Long effectiveCalculateBTSId) {
		this.effectiveCalculateBTSId = effectiveCalculateBTSId;
	}

	public Long getCatProvinceId() {
		return catProvinceId != null ? catProvinceId : 0;
	}

	public void setCatProvinceId(Long catProvinceId) {
		this.catProvinceId = catProvinceId;
	}

	public String getCatProvinceCode() {
		return getStandardText(catProvinceCode);
	}

	public void setCatProvinceCode(String catProvinceCode) {
		this.catProvinceCode = catProvinceCode;
	}

	public String getLatitude() {
		return getStandardText(latitude);
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return getStandardText(longitude);
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public Double getHightBts() {
		return hightBts != null ? hightBts : 0;
	}

	public void setHightBts(Double hightBts) {
		this.hightBts = hightBts;
	}

	public String getColumnType() {
		return getStandardText(columnType);
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public String getTopographic() {
		return getStandardText(topographic);
	}

	public void setTopographic(String topographic) {
		this.topographic = topographic;
	}

	public String getLocation() {
		return getStandardText(location);
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getAddress() {
		return getStandardText(address);
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTypeStation() {
		return getStandardText(typeStation);
	}

	public void setTypeStation(String typeStation) {
		this.typeStation = typeStation;
	}

	public String getMnoViettel() {
		return getStandardText(mnoViettel);
	}

	public void setMnoViettel(String mnoViettel) {
		this.mnoViettel = mnoViettel;
	}

	public String getMnoVina() {
		return getStandardText(mnoVina);
	}

	public void setMnoVina(String mnoVina) {
		this.mnoVina = mnoVina;
	}

	public String getMnoMobi() {
		return getStandardText(mnoMobi);
	}

	public void setMnoMobi(String mnoMobi) {
		this.mnoMobi = mnoMobi;
	}

	public Long getOurceDeployment() {
		return ourceDeployment != null ? ourceDeployment : 0 ;
	}

	public void setOurceDeployment(Long ourceDeployment) {
		this.ourceDeployment = ourceDeployment;
	}

	public String getLectDepreciationPeriod() {
		return getStandardText(lectDepreciationPeriod);
	}

	public void setLectDepreciationPeriod(String lectDepreciationPeriod) {
		this.lectDepreciationPeriod = lectDepreciationPeriod;
	}

	public String getSilkEnterPrice() {
		return getStandardText(silkEnterPrice);
	}

	public void setSilkEnterPrice(String silkEnterPrice) {
		this.silkEnterPrice = silkEnterPrice;
	}

	public String getPrice() {
		return getStandardText(price);
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getCostMB() {
		return getStandardText(costMB);
	}

	public void setCostMB(String costMB) {
		this.costMB = costMB;
	}

	public String getColumnFoundationItems() {
		return getStandardText(columnFoundationItems);
	}

	public void setColumnFoundationItems(String columnFoundationItems) {
		this.columnFoundationItems = columnFoundationItems;
	}

	public Double getCostColumnFoundationItems() {
		return costColumnFoundationItems != null ? costColumnFoundationItems : 0;
	}

	public void setCostColumnFoundationItems(Double costColumnFoundationItems) {
		this.costColumnFoundationItems = costColumnFoundationItems;
	}

	public String getHouseFoundationItems() {
		return getStandardText(houseFoundationItems);
	}

	public void setHouseFoundationItems(String houseFoundationItems) {
		this.houseFoundationItems = houseFoundationItems;
	}

	public Double getCostHouseFoundationItems() {
		return costHouseFoundationItems != null  ? costHouseFoundationItems : 0;
	}

	public void setCostHouseFoundationItems(Double costHouseFoundationItems) {
		this.costHouseFoundationItems = costHouseFoundationItems;
	}

	public String getColumnBodyCategory() {
		return getStandardText(columnBodyCategory);
	}

	public void setColumnBodyCategory(String columnBodyCategory) {
		this.columnBodyCategory = columnBodyCategory;
	}

	public Double getCostColumnBodyCategory() {
		return costColumnBodyCategory != null ? costColumnBodyCategory : 0;
	}

	public void setCostColumnBodyCategory(Double costColumnBodyCategory) {
		this.costColumnBodyCategory = costColumnBodyCategory;
	}

	public String getMachineRoomItems() {
		return getStandardText(machineRoomItems);
	}

	public void setMachineRoomItems(String machineRoomItems) {
		this.machineRoomItems = machineRoomItems;
	}

	public Double getCostMachineRoomItems() {
		return costMachineRoomItems != null  ? costMachineRoomItems : 0;
	}

	public void setCostMachineRoomItems(Double costMachineRoomItems) {
		this.costMachineRoomItems = costMachineRoomItems;
	}

	public String getGroundingItems() {
		return getStandardText(groundingItems);
	}

	public void setGroundingItems(String groundingItems) {
		this.groundingItems = groundingItems;
	}

	public Double getCostGroundingItems() {
		return costGroundingItems != null  ? costGroundingItems : 0;
	}

	public void setCostGroundingItems(Double costGroundingItems) {
		this.costGroundingItems = costGroundingItems;
	}

	public String getElectricTowingItems() {
		return getStandardText(electricTowingItems);
	}

	public void setElectricTowingItems(String electricTowingItems) {
		this.electricTowingItems = electricTowingItems;
	}

	public Double getCostElectricTowingItems() {
		return costElectricTowingItems != null ? costElectricTowingItems : 0;
	}

	public void setCostElectricTowingItems(Double costElectricTowingItems) {
		this.costElectricTowingItems = costElectricTowingItems;
	}

	public String getColumnMountingItem() {
		return getStandardText(columnMountingItem);
	}

	public void setColumnMountingItem(String columnMountingItem) {
		this.columnMountingItem = columnMountingItem;
	}

	public Double getCostColumnMountingItem() {
		return costColumnMountingItem != null ? costColumnMountingItem : 0;
	}

	public void setCostColumnMountingItem(Double costColumnMountingItem) {
		this.costColumnMountingItem = costColumnMountingItem;
	}

	public String getInstallationHouses() {
		return getStandardText(installationHouses);
	}

	public void setInstallationHouses(String installationHouses) {
		this.installationHouses = installationHouses;
	}

	public Double getCostInstallationHouses() {
		return costInstallationHouses != null ? costInstallationHouses : 0;
	}

	public void setCostInstallationHouses(Double costInstallationHouses) {
		this.costInstallationHouses = costInstallationHouses;
	}

	public String getElectricalItems() {
		return getStandardText(electricalItems);
	}

	public void setElectricalItems(String electricalItems) {
		this.electricalItems = electricalItems;
	}

	public Double getCostElectricalItems() {
		return costElectricalItems != null  ? costElectricalItems : 0;
	}

	public void setCostElectricalItems(Double costElectricalItems) {
		this.costElectricalItems = costElectricalItems;
	}

	public String getMotorizedTransportItems() {
		return getStandardText(motorizedTransportItems);
	}

	public void setMotorizedTransportItems(String motorizedTransportItems) {
		this.motorizedTransportItems = motorizedTransportItems;
	}

	public Double getCostMotorizedTransportItems() {
		return costMotorizedTransportItems;
	}

	public void setCostMotorizedTransportItems(
			Double costMotorizedTransportItems) {
		this.costMotorizedTransportItems = costMotorizedTransportItems;
	}

	public String getItemManualShipping() {
		return getStandardText(itemManualShipping);
	}

	public void setItemManualShipping(String itemManualShipping) {
		this.itemManualShipping = itemManualShipping;
	}

	public Double getCostItemManualShipping() {
		return costItemManualShipping != null ? costItemManualShipping : 0;
	}

	public void setCostItemManualShipping(Double costItemManualShipping) {
		this.costItemManualShipping = costItemManualShipping;
	}

	public String getItemShipping() {
		return getStandardText(itemShipping);
	}

	public void setItemShipping(String itemShipping) {
		this.itemShipping = itemShipping;
	}

	public Double getCostItemShipping() {
		return costItemShipping != null ? costItemShipping : 0;
	}

	public void setCostItemShipping(Double costItemShipping) {
		this.costItemShipping = costItemShipping;
	}

	public Double getCostItemsOtherExpenses() {
		return costItemsOtherExpenses != null ? costItemsOtherExpenses : 0;
	}

	public void setCostItemsOtherExpenses(Double costItemsOtherExpenses) {
		this.costItemsOtherExpenses = costItemsOtherExpenses;
	}

	public Double getOwerCabinetCoolingSystem() {
		return owerCabinetCoolingSystem != null ? owerCabinetCoolingSystem : 0;
	}

	public void setOwerCabinetCoolingSystem(Double owerCabinetCoolingSystem) {
		this.owerCabinetCoolingSystem = owerCabinetCoolingSystem;
	}

	public Double getRectifier3000() {
		return rectifier3000 != null ? rectifier3000 : 0;
	}

	public void setRectifier3000(Double rectifier3000) {
		this.rectifier3000 = rectifier3000;
	}

	public Double getBatteryLithium() {
		return batteryLithium != null ? batteryLithium : 0;
	}

	public void setBatteryLithium(Double batteryLithium) {
		this.batteryLithium = batteryLithium;
	}

	public Double getOilGenerator() {
		return oilGenerator != null ? oilGenerator : 0;
	}

	public void setOilGenerator(Double oilGenerator) {
		this.oilGenerator = oilGenerator;
	}

	public Double getAts() {
		return ats != null ? ats : 0;
	}

	public void setAts(Double ats) {
		this.ats = ats;
	}

	public Double getSupervisionControl() {
		return supervisionControl != null ? supervisionControl : 0;
	}

	public void setSupervisionControl(Double supervisionControl) {
		this.supervisionControl = supervisionControl;
	}

	public Double getOtherAuxiliarySystem() {
		return otherAuxiliarySystem != null ? otherAuxiliarySystem : 0;
	}

	public void setOtherAuxiliarySystem(Double otherAuxiliarySystem) {
		this.otherAuxiliarySystem = otherAuxiliarySystem;
	}

	public Double getPublicInstallationPower() {
		return publicInstallationPower != null ? publicInstallationPower : 0;
	}

	public void setPublicInstallationPower(Double publicInstallationPower) {
		this.publicInstallationPower = publicInstallationPower;
	}

	public String getEffective() {
		return getStandardText(effective);
	}

	public void setEffective(String effective) {
		this.effective = effective;
	}

	public java.util.Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(java.util.Date createdDate) {
		this.createdDate = createdDate;
	}

	public Long getCreatedUserId() {
		return createdUserId;
	}

	public void setCreatedUserId(Long createdUserId) {
		this.createdUserId = createdUserId;
	}

	private String getStandardText(String text){
		return text == null ? "" : text.trim();
	}

	@Override
	public Long getFWModelId() {
		return this.getEffectiveCalculateBTSId();
	}

	@Override
	public String catchName() {
		return this.getEffectiveCalculateBTSId().toString();
	}

	@Override
	public EffectiveCalculateBTSBO toModel() {
		EffectiveCalculateBTSBO bo = new EffectiveCalculateBTSBO();
		bo.setEffectiveCalculateBTSId(this.getEffectiveCalculateBTSId());
		bo.setCatProvinceId(this.getCatProvinceId());
		bo.setCatProvinceCode(this.getCatProvinceCode());
		bo.setCatProvinceName(this.getCatProvinceName());
		bo.setLatitude(this.getLatitude());
		bo.setLongitude(this.getLongitude());
		bo.setHightBts(this.getHightBts());
		bo.setColumnType(this.getColumnType());
		bo.setTopographic(this.getTopographic());
		bo.setLocation(this.getLocation());
		bo.setAddress(this.getAddress());
		bo.setTypeStation(this.getTypeStation());
		bo.setMnoViettel(this.getMnoViettel());
		bo.setMnoVina(this.getMnoVina());
		bo.setMnoMobi(this.getMnoMobi());
		bo.setOurceDeployment(this.getOurceDeployment());
		bo.setLectDepreciationPeriod(this.getLectDepreciationPeriod());
		bo.setSilkEnterPrice(this.getSilkEnterPrice());
		bo.setPrice(Double.parseDouble(this.getPrice()));
		bo.setCostMB(Double.parseDouble(this.getCostMB()));
		bo.setColumnFoundationItems(this.getColumnFoundationItems());
		bo.setCostColumnFoundationItems(this.getCostColumnFoundationItems());
		bo.setHouseFoundationItems(this.getHouseFoundationItems());
		bo.setCostHouseFoundationItems(this.getCostHouseFoundationItems());
		bo.setColumnBodyCategory(this.getColumnBodyCategory());
		bo.setCostColumnBodyCategory(this.getCostColumnBodyCategory());
		bo.setMachineRoomItems(this.getMachineRoomItems());
		bo.setCostMachineRoomItems(this.getCostMachineRoomItems());
		bo.setGroundingItems(this.getGroundingItems());
		bo.setCostGroundingItems(this.getCostGroundingItems());
		bo.setElectricTowingItems(this.getElectricTowingItems());
		bo.setCostElectricTowingItems(this.getCostElectricTowingItems());
		bo.setColumnMountingItem(this.getColumnMountingItem());
		bo.setCostColumnFoundationItems(this.getCostColumnFoundationItems());
		bo.setInstallationHouses(this.getInstallationHouses());
		bo.setCostInstallationHouses(this.getCostInstallationHouses());
		bo.setElectricalItems(this.getElectricalItems());
		bo.setCostElectricalItems(this.getCostElectricalItems());
		bo.setMotorizedTransportItems(this.getMotorizedTransportItems());
		bo.setCostMotorizedTransportItems(this.getCostMotorizedTransportItems());
		bo.setItemManualShipping(this.getItemManualShipping());
		bo.setCostItemManualShipping(this.getCostItemManualShipping());
		bo.setItemShipping(this.getItemShipping());
		bo.setCostItemShipping(this.getCostItemShipping());
		bo.setCostItemsOtherExpenses(this.getCostItemsOtherExpenses());
		bo.setOwerCabinetCoolingSystem(this.getOwerCabinetCoolingSystem());
		bo.setRectifier3000(this.getRectifier3000());
		bo.setBatteryLithium(this.getBatteryLithium());
		bo.setOilGenerator(this.getOilGenerator());
		bo.setAts(this.getAts());
		bo.setSupervisionControl(this.getSupervisionControl());
		bo.setOtherAuxiliarySystem(this.getOtherAuxiliarySystem());
		bo.setPublicInstallationPower(this.getPublicInstallationPower());
		bo.setEffective(this.getEffective());
		bo.setCreatedDate(this.getCreatedDate());
		bo.setCreatedUserId(this.getCreatedUserId());
		return bo;
	}

}
