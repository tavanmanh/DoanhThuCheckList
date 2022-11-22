package com.viettel.coms.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;

import com.viettel.coms.dto.EffectiveCalculateBTSDTO;
import com.viettel.coms.dto.EffectiveCalculateDASCDBRDTO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;
import com.viettel.service.base.model.BaseFWModelImpl;

@Entity
@Table(name = "EFFECTIVE_CALCULATE_BTS")
public class EffectiveCalculateBTSBO extends BaseFWModelImpl {

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
	private Double price;
	private Double costMB;
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
	private Long createdUserId;
	private java.util.Date createdDate;
	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = { @org.hibernate.annotations.Parameter(name = "sequence", value = "Effective_Calculate_BTS_seq") })
	@Column(name = "EFFECTIVE_CALCULATE_BTS_ID", length = 11)	
	public Long getEffectiveCalculateBTSId() {
		return effectiveCalculateBTSId;
	}
	public void setEffectiveCalculateBTSId(Long effectiveCalculateBTSId) {
		this.effectiveCalculateBTSId = effectiveCalculateBTSId;
	}
	@Column(name = "CAT_PROVINCE_ID", length = 11)	
	public Long getCatProvinceId() {
		return catProvinceId;
	}
	public void setCatProvinceId(Long catProvinceId) {
		this.catProvinceId = catProvinceId;
	}
	@Column(name = "CAT_PROVINCE_CODE", length = 50)
	public String getCatProvinceCode() {
		return catProvinceCode;
	}
	public void setCatProvinceCode(String catProvinceCode) {
		this.catProvinceCode = catProvinceCode;
	}
	@Column(name = "CAT_PROVINCE_NAME", length = 50)
	public String getCatProvinceName() {
		return catProvinceName;
	}
	public void setCatProvinceName(String catProvinceName) {
		this.catProvinceName = catProvinceName;
	}
	@Column(name = "LATITUDE", length = 100)
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	@Column(name = "LONGITUDE", length = 100)
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	@Column(name = "HIGHT_BTS", length = 24)
	public Double getHightBts() {
		return hightBts;
	}
	public void setHightBts(Double hightBts) {
		this.hightBts = hightBts;
	}
	@Column(name = "COLUMN_TYPE", length = 500)
	public String getColumnType() {
		return columnType;
	}
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}
	@Column(name = "TOPOGRAPHIC", length = 500)
	public String getTopographic() {
		return topographic;
	}
	public void setTopographic(String topographic) {
		this.topographic = topographic;
	}
	@Column(name = "LOCATION", length = 500)
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	@Column(name = "ADDRESS", length = 500)
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Column(name = "TYPE_STATION", length = 500)
	public String getTypeStation() {
		return typeStation;
	}
	public void setTypeStation(String typeStation) {
		this.typeStation = typeStation;
	}
	@Column(name = "MNO_VIETTEL", length = 24)
	public String getMnoViettel() {
		return mnoViettel;
	}
	public void setMnoViettel(String mnoViettel) {
		this.mnoViettel = mnoViettel;
	}	
	@Column(name = "MNO_VINA", length = 24)	
	public String getMnoVina() {
		return mnoVina;
	}
	public void setMnoVina(String mnoVina) {
		this.mnoVina = mnoVina;
	}
	@Column(name = "MNO_MOBILE", length = 24)
	public String getMnoMobi() {
		return mnoMobi;
	}
	public void setMnoMobi(String mnoMobi) {
		this.mnoMobi = mnoMobi;
	}
	@Column(name = "OURCE_DEPLOYMENT", length = 11)
	public Long getOurceDeployment() {
		return ourceDeployment;
	}
	public void setOurceDeployment(Long ourceDeployment) {
		this.ourceDeployment = ourceDeployment;
	}
	@Column(name = "LECT_DEPRECIATION_PERIOD", length = 11)
	public String getLectDepreciationPeriod() {
		return lectDepreciationPeriod;
	}
	public void setLectDepreciationPeriod(String lectDepreciationPeriod) {
		this.lectDepreciationPeriod = lectDepreciationPeriod;
	}
	@Column(name = "SILK_ENTER_PRICE", length = 11)
	public String getSilkEnterPrice() {
		return silkEnterPrice;
	}
	public void setSilkEnterPrice(String silkEnterPrice) {
		this.silkEnterPrice = silkEnterPrice;
	}
	
	@Column(name = "PRICE", length = 24)
	public Double getPrice() {
		return price;
	}
	
	public void setPrice(Double price) {
		this.price = price;
	}
	@Column(name = "COST_MB", length = 24)
	public Double getCostMB() {
		return costMB;
	}
	public void setCostMB(Double costMB) {
		this.costMB = costMB;
	}
	@Column(name = "COLUMN_FOUNDATION_ITEMS", length = 500)
	public String getColumnFoundationItems() {
		return columnFoundationItems;
	}
	public void setColumnFoundationItems(String columnFoundationItems) {
		this.columnFoundationItems = columnFoundationItems;
	}
	@Column(name = "COST_COLUMN_FOUNDATION_ITEMS", length = 24)
	public Double getCostColumnFoundationItems() {
		return costColumnFoundationItems;
	}
	public void setCostColumnFoundationItems(Double costColumnFoundationItems) {
		this.costColumnFoundationItems = costColumnFoundationItems;
	}
	@Column(name = "HOUSE_FOUNDATION_ITEMS", length = 500)
	public String getHouseFoundationItems() {
		return houseFoundationItems;
	}
	public void setHouseFoundationItems(String houseFoundationItems) {
		this.houseFoundationItems = houseFoundationItems;
	}
	@Column(name = "COST_HOUSE_FOUNDATION_ITEMS", length = 24)
	public Double getCostHouseFoundationItems() {
		return costHouseFoundationItems;
	}
	public void setCostHouseFoundationItems(Double costHouseFoundationItems) {
		this.costHouseFoundationItems = costHouseFoundationItems;
	}
	@Column(name = "COLUMN_BODY_CATEGORY", length = 500)
	public String getColumnBodyCategory() {
		return columnBodyCategory;
	}
	public void setColumnBodyCategory(String columnBodyCategory) {
		this.columnBodyCategory = columnBodyCategory;
	}
	@Column(name = "COST_COLUMN_BODY_CATEGORY", length = 24)
	public Double getCostColumnBodyCategory() {
		return costColumnBodyCategory;
	}
	public void setCostColumnBodyCategory(Double costColumnBodyCategory) {
		this.costColumnBodyCategory = costColumnBodyCategory;
	}
	@Column(name = "MACHINE_ROOM_ITEMS", length = 500)
	public String getMachineRoomItems() {
		return machineRoomItems;
	}
	public void setMachineRoomItems(String machineRoomItems) {
		this.machineRoomItems = machineRoomItems;
	}
	@Column(name = "COST_MACHINE_ROOM_ITEMS", length = 24)
	public Double getCostMachineRoomItems() {
		return costMachineRoomItems;
	}
	public void setCostMachineRoomItems(Double costMachineRoomItems) {
		this.costMachineRoomItems = costMachineRoomItems;
	}
	@Column(name = "GROUNDING_ITEMS", length = 500)
	public String getGroundingItems() {
		return groundingItems;
	}
	public void setGroundingItems(String groundingItems) {
		this.groundingItems = groundingItems;
	}
	@Column(name = "COST_GROUNDING_ITEMS", length = 24)
	public Double getCostGroundingItems() {
		return costGroundingItems;
	}
	public void setCostGroundingItems(Double costGroundingItems) {
		this.costGroundingItems = costGroundingItems;
	}
	@Column(name = "ELECTRIC_TOWING_ITEMS", length = 500)
	public String getElectricTowingItems() {
		return electricTowingItems;
	}
	public void setElectricTowingItems(String electricTowingItems) {
		this.electricTowingItems = electricTowingItems;
	}
	@Column(name = "COST_ELECTRIC_TOWING_ITEMS", length = 24)
	public Double getCostElectricTowingItems() {
		return costElectricTowingItems;
	}
	public void setCostElectricTowingItems(Double costElectricTowingItems) {
		this.costElectricTowingItems = costElectricTowingItems;
	}
	@Column(name = "COLUMN_MOUNTING_ITEM", length = 500)
	public String getColumnMountingItem() {
		return columnMountingItem;
	}
	public void setColumnMountingItem(String columnMountingItem) {
		this.columnMountingItem = columnMountingItem;
	}
	@Column(name = "COST_COLUMN_MOUNTING_ITEM", length = 24)
	public Double getCostColumnMountingItem() {
		return costColumnMountingItem;
	}
	public void setCostColumnMountingItem(Double costColumnMountingItem) {
		this.costColumnMountingItem = costColumnMountingItem;
	}
	@Column(name = "INSTALLATION_HOUSES", length = 500)
	public String getInstallationHouses() {
		return installationHouses;
	}
	public void setInstallationHouses(String installationHouses) {
		this.installationHouses = installationHouses;
	}
	@Column(name = "COST_INSTALLATION_HOUSES", length = 24)
	public Double getCostInstallationHouses() {
		return costInstallationHouses;
	}
	public void setCostInstallationHouses(Double costInstallationHouses) {
		this.costInstallationHouses = costInstallationHouses;
	}
	@Column(name = "ELECTRICAL_ITEMS", length = 500)
	public String getElectricalItems() {
		return electricalItems;
	}
	public void setElectricalItems(String electricalItems) {
		this.electricalItems = electricalItems;
	}
	@Column(name = "COST_ELECTRICAL_ITEMS", length = 24)
	public Double getCostElectricalItems() {
		return costElectricalItems;
	}
	public void setCostElectricalItems(Double costElectricalItems) {
		this.costElectricalItems = costElectricalItems;
	}
	@Column(name = "MOTORIZED_TRANSPORT_ITEMS", length = 500)
	public String getMotorizedTransportItems() {
		return motorizedTransportItems;
	}
	public void setMotorizedTransportItems(String motorizedTransportItems) {
		this.motorizedTransportItems = motorizedTransportItems;
	}
	@Column(name = "COST_MOTORIZED_TRANSPORT_ITEMS", length = 24)
	public Double getCostMotorizedTransportItems() {
		return costMotorizedTransportItems;
	}
	public void setCostMotorizedTransportItems(Double costMotorizedTransportItems) {
		this.costMotorizedTransportItems = costMotorizedTransportItems;
	}
	@Column(name = "ITEM_MANUAL_SHIPPING", length = 500)
	public String getItemManualShipping() {
		return itemManualShipping;
	}
	public void setItemManualShipping(String itemManualShipping) {
		this.itemManualShipping = itemManualShipping;
	}
	@Column(name = "COST_ITEM_MANUAL_SHIPPING", length = 24)
	public Double getCostItemManualShipping() {
		return costItemManualShipping;
	}
	public void setCostItemManualShipping(Double costItemManualShipping) {
		this.costItemManualShipping = costItemManualShipping;
	}
	@Column(name = "ITEM_SHIPPING", length = 500)
	public String getItemShipping() {
		return itemShipping;
	}
	public void setItemShipping(String itemShipping) {
		this.itemShipping = itemShipping;
	}
	@Column(name = "COST_ITEM_SHIPPING", length = 24)
	public Double getCostItemShipping() {
		return costItemShipping;
	}
	public void setCostItemShipping(Double costItemShipping) {
		this.costItemShipping = costItemShipping;
	}
	@Column(name = "COST_ITEMS_OTHER_EXPENSES", length = 24)
	public Double getCostItemsOtherExpenses() {
		return costItemsOtherExpenses;
	}
	public void setCostItemsOtherExpenses(Double costItemsOtherExpenses) {
		this.costItemsOtherExpenses = costItemsOtherExpenses;
	}
	@Column(name = "OWER_CABINET_COOLING_SYSTEM", length = 24)
	public Double getOwerCabinetCoolingSystem() {
		return owerCabinetCoolingSystem;
	}
	public void setOwerCabinetCoolingSystem(Double owerCabinetCoolingSystem) {
		this.owerCabinetCoolingSystem = owerCabinetCoolingSystem;
	}
	@Column(name = "RECTIFIER_3000", length = 24)
	public Double getRectifier3000() {
		return rectifier3000;
	}
	public void setRectifier3000(Double rectifier3000) {
		this.rectifier3000 = rectifier3000;
	}
	@Column(name = "BATTERY_LITHIUM", length = 24)
	public Double getBatteryLithium() {
		return batteryLithium;
	}
	public void setBatteryLithium(Double batteryLithium) {
		this.batteryLithium = batteryLithium;
	}
	@Column(name = "OIL_GENERATOR", length = 24)
	public Double getOilGenerator() {
		return oilGenerator;
	}
	public void setOilGenerator(Double oilGenerator) {
		this.oilGenerator = oilGenerator;
	}
	@Column(name = "ATS", length = 24)
	public Double getAts() {
		return ats;
	}
	public void setAts(Double ats) {
		this.ats = ats;
	}
	@Column(name = "SUPERVISION_CONTROL", length = 24)
	public Double getSupervisionControl() {
		return supervisionControl;
	}
	public void setSupervisionControl(Double supervisionControl) {
		this.supervisionControl = supervisionControl;
	}
	@Column(name = "OTHER_AUXILIARY_SYSTEM", length = 24)
	public Double getOtherAuxiliarySystem() {
		return otherAuxiliarySystem;
	}
	public void setOtherAuxiliarySystem(Double otherAuxiliarySystem) {
		this.otherAuxiliarySystem = otherAuxiliarySystem;
	}
	@Column(name = "PUBLIC_INSTALLATION_POWER", length = 24)
	public Double getPublicInstallationPower() {
		return publicInstallationPower;
	}
	public void setPublicInstallationPower(Double publicInstallationPower) {
		this.publicInstallationPower = publicInstallationPower;
	}
	@Column(name = "EFFECTIVE", length = 11)
	public String getEffective() {
		return effective;
	}
	public void setEffective(String effective) {
		this.effective = effective;
	}
	@Column(name = "CREATED_USER_ID", length = 24)
	public Long getCreatedUserId() {
		return createdUserId;
	}
	public void setCreatedUserId(Long createdUserId) {
		this.createdUserId = createdUserId;
	}
	@Column(name = "CREATED_DATE", length = 24)
	public java.util.Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(java.util.Date createdDate) {
		this.createdDate = createdDate;
	}
	@Override
	public EffectiveCalculateBTSDTO toDTO() {
		EffectiveCalculateBTSDTO bo = new EffectiveCalculateBTSDTO();
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
		bo.setPrice(this.getPrice().toString());
		bo.setCostMB(this.getCostMB().toString());
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
