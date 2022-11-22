package com.viettel.coms.dto;

import com.viettel.service.base.model.BaseFWModelImpl;

public class ReportEffectiveDTO extends ComsBaseFWDTO<BaseFWModelImpl>{

	private String dasType;
	private String cdbrType;
	private Double costDas;
	private Double costEngineRoomCDBR;
	private Double costCDBR;
	private Double ratioRate;
	private String houseName;
	private Double totalArea;
	private Double totalApartment;
	private String engineRoomDas;
	private String feederAntenDas;
	private String costOtherDas;
	private String axisCdbr;
	private String apartmentsAllCdbr;
	private String apartmentsCdbr;
	private String costOtherCdbr;
	private String engineRoomCdbr;
	private String engineRoomCableCdbr;
	private String effective;
	private String type;
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDasType() {
		return dasType;
	}
	public void setDasType(String dasType) {
		this.dasType = dasType;
	}
	public String getCdbrType() {
		return cdbrType;
	}
	public void setCdbrType(String cdbrType) {
		this.cdbrType = cdbrType;
	}
	public Double getCostDas() {
		return costDas;
	}
	public void setCostDas(Double costDas) {
		this.costDas = costDas;
	}
	public Double getCostEngineRoomCDBR() {
		return costEngineRoomCDBR;
	}
	public void setCostEngineRoomCDBR(Double costEngineRoomCDBR) {
		this.costEngineRoomCDBR = costEngineRoomCDBR;
	}
	public Double getCostCDBR() {
		return costCDBR;
	}
	public void setCostCDBR(Double costCDBR) {
		this.costCDBR = costCDBR;
	}
	public Double getRatioRate() {
		return ratioRate;
	}
	public void setRatioRate(Double ratioRate) {
		this.ratioRate = ratioRate;
	}
	public String getHouseName() {
		return houseName;
	}
	public void setHouseName(String houseName) {
		this.houseName = houseName;
	}
	public Double getTotalArea() {
		return totalArea;
	}
	public void setTotalArea(Double totalArea) {
		this.totalArea = totalArea;
	}
	public Double getTotalApartment() {
		return totalApartment;
	}
	public void setTotalApartment(Double totalApartment) {
		this.totalApartment = totalApartment;
	}
	public String getEngineRoomDas() {
		return engineRoomDas;
	}
	public void setEngineRoomDas(String engineRoomDas) {
		this.engineRoomDas = engineRoomDas;
	}
	public String getFeederAntenDas() {
		return feederAntenDas;
	}
	public void setFeederAntenDas(String feederAntenDas) {
		this.feederAntenDas = feederAntenDas;
	}
	public String getCostOtherDas() {
		return costOtherDas;
	}
	public void setCostOtherDas(String costOtherDas) {
		this.costOtherDas = costOtherDas;
	}
	public String getAxisCdbr() {
		return axisCdbr;
	}
	public void setAxisCdbr(String axisCdbr) {
		this.axisCdbr = axisCdbr;
	}
	public String getApartmentsAllCdbr() {
		return apartmentsAllCdbr;
	}
	public void setApartmentsAllCdbr(String apartmentsAllCdbr) {
		this.apartmentsAllCdbr = apartmentsAllCdbr;
	}
	public String getApartmentsCdbr() {
		return apartmentsCdbr;
	}
	public void setApartmentsCdbr(String apartmentsCdbr) {
		this.apartmentsCdbr = apartmentsCdbr;
	}
	public String getCostOtherCdbr() {
		return costOtherCdbr;
	}
	public void setCostOtherCdbr(String costOtherCdbr) {
		this.costOtherCdbr = costOtherCdbr;
	}
	public String getEngineRoomCdbr() {
		return engineRoomCdbr;
	}
	public void setEngineRoomCdbr(String engineRoomCdbr) {
		this.engineRoomCdbr = engineRoomCdbr;
	}
	public String getEngineRoomCableCdbr() {
		return engineRoomCableCdbr;
	}
	public void setEngineRoomCableCdbr(String engineRoomCableCdbr) {
		this.engineRoomCableCdbr = engineRoomCableCdbr;
	}
	public String getEffective() {
		return effective;
	}
	public void setEffective(String effective) {
		this.effective = effective;
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
	public BaseFWModelImpl toModel() {
		// TODO Auto-generated method stub
		return null;
	}

	private String catProvinceCode;
	private String catProvinceName;
	private Double latitude;
	private Double longitude;
	private Double hightBts;
	private String columnType;
	private String topographic;
	private String location;
	private String address;
	private String typeStation;
	private String mNOViettel;
	private String mNOVina;
	private String mNOMobi;
	private String ourceDeployment;
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
	private String owerCabinetCoolingSystem;
	private String rectifier3000;
	private String batteryLithium;
	private String oilGenerator;
	private String ats;
	private String supervisionControl;
	private String otherAuxiliarySystem;
	private String publicInstallationPower;


	public String getCatProvinceCode() {
		return catProvinceCode;
	}
	public void setCatProvinceCode(String catProvinceCode) {
		this.catProvinceCode = catProvinceCode;
	}
	public String getCatProvinceName() {
		return catProvinceName;
	}
	public void setCatProvinceName(String catProvinceName) {
		this.catProvinceName = catProvinceName;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getHightBts() {
		return hightBts;
	}
	public void setHightBts(Double hightBts) {
		this.hightBts = hightBts;
	}
	public String getColumnType() {
		return columnType;
	}
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}
	public String getTopographic() {
		return topographic;
	}
	public void setTopographic(String topographic) {
		this.topographic = topographic;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTypeStation() {
		return typeStation;
	}
	public void setTypeStation(String typeStation) {
		this.typeStation = typeStation;
	}
	
	public String getmNOViettel() {
		return mNOViettel;
	}
	public void setmNOViettel(String mNOViettel) {
		this.mNOViettel = mNOViettel;
	}
	public String getmNOVina() {
		return mNOVina;
	}
	public void setmNOVina(String mNOVina) {
		this.mNOVina = mNOVina;
	}
	public String getmNOMobi() {
		return mNOMobi;
	}
	public void setmNOMobi(String mNOMobi) {
		this.mNOMobi = mNOMobi;
	}
	public String getOurceDeployment() {
		return ourceDeployment;
	}
	public void setOurceDeployment(String ourceDeployment) {
		this.ourceDeployment = ourceDeployment;
	}
	public String getLectDepreciationPeriod() {
		return lectDepreciationPeriod;
	}
	public void setLectDepreciationPeriod(String lectDepreciationPeriod) {
		this.lectDepreciationPeriod = lectDepreciationPeriod;
	}
	
	public String getSilkEnterPrice() {
		return silkEnterPrice;
	}
	public void setSilkEnterPrice(String silkEnterPrice) {
		this.silkEnterPrice = silkEnterPrice;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getCostMB() {
		return costMB;
	}
	public void setCostMB(Double costMB) {
		this.costMB = costMB;
	}
	public String getColumnFoundationItems() {
		return columnFoundationItems;
	}
	public void setColumnFoundationItems(String columnFoundationItems) {
		this.columnFoundationItems = columnFoundationItems;
	}
	public Double getCostColumnFoundationItems() {
		return costColumnFoundationItems;
	}
	public void setCostColumnFoundationItems(Double costColumnFoundationItems) {
		this.costColumnFoundationItems = costColumnFoundationItems;
	}
	public String getHouseFoundationItems() {
		return houseFoundationItems;
	}
	public void setHouseFoundationItems(String houseFoundationItems) {
		this.houseFoundationItems = houseFoundationItems;
	}
	public Double getCostHouseFoundationItems() {
		return costHouseFoundationItems;
	}
	public void setCostHouseFoundationItems(Double costHouseFoundationItems) {
		this.costHouseFoundationItems = costHouseFoundationItems;
	}
	public String getColumnBodyCategory() {
		return columnBodyCategory;
	}
	public void setColumnBodyCategory(String columnBodyCategory) {
		this.columnBodyCategory = columnBodyCategory;
	}
	public Double getCostColumnBodyCategory() {
		return costColumnBodyCategory;
	}
	public void setCostColumnBodyCategory(Double costColumnBodyCategory) {
		this.costColumnBodyCategory = costColumnBodyCategory;
	}
	public String getMachineRoomItems() {
		return machineRoomItems;
	}
	public void setMachineRoomItems(String machineRoomItems) {
		this.machineRoomItems = machineRoomItems;
	}
	public Double getCostMachineRoomItems() {
		return costMachineRoomItems;
	}
	public void setCostMachineRoomItems(Double costMachineRoomItems) {
		this.costMachineRoomItems = costMachineRoomItems;
	}
	public String getGroundingItems() {
		return groundingItems;
	}
	public void setGroundingItems(String groundingItems) {
		this.groundingItems = groundingItems;
	}
	public Double getCostGroundingItems() {
		return costGroundingItems;
	}
	public void setCostGroundingItems(Double costGroundingItems) {
		this.costGroundingItems = costGroundingItems;
	}
	public String getElectricTowingItems() {
		return electricTowingItems;
	}
	public void setElectricTowingItems(String electricTowingItems) {
		this.electricTowingItems = electricTowingItems;
	}
	public Double getCostElectricTowingItems() {
		return costElectricTowingItems;
	}
	public void setCostElectricTowingItems(Double costElectricTowingItems) {
		this.costElectricTowingItems = costElectricTowingItems;
	}
	public String getColumnMountingItem() {
		return columnMountingItem;
	}
	public void setColumnMountingItem(String columnMountingItem) {
		this.columnMountingItem = columnMountingItem;
	}
	public Double getCostColumnMountingItem() {
		return costColumnMountingItem;
	}
	public void setCostColumnMountingItem(Double costColumnMountingItem) {
		this.costColumnMountingItem = costColumnMountingItem;
	}
	public String getInstallationHouses() {
		return installationHouses;
	}
	public void setInstallationHouses(String installationHouses) {
		this.installationHouses = installationHouses;
	}
	public Double getCostInstallationHouses() {
		return costInstallationHouses;
	}
	public void setCostInstallationHouses(Double costInstallationHouses) {
		this.costInstallationHouses = costInstallationHouses;
	}
	public String getElectricalItems() {
		return electricalItems;
	}
	public void setElectricalItems(String electricalItems) {
		this.electricalItems = electricalItems;
	}
	public Double getCostElectricalItems() {
		return costElectricalItems;
	}
	public void setCostElectricalItems(Double costElectricalItems) {
		this.costElectricalItems = costElectricalItems;
	}
	public String getMotorizedTransportItems() {
		return motorizedTransportItems;
	}
	public void setMotorizedTransportItems(String motorizedTransportItems) {
		this.motorizedTransportItems = motorizedTransportItems;
	}
	public Double getCostMotorizedTransportItems() {
		return costMotorizedTransportItems;
	}
	public void setCostMotorizedTransportItems(Double costMotorizedTransportItems) {
		this.costMotorizedTransportItems = costMotorizedTransportItems;
	}
	public String getItemManualShipping() {
		return itemManualShipping;
	}
	public void setItemManualShipping(String itemManualShipping) {
		this.itemManualShipping = itemManualShipping;
	}
	public Double getCostItemManualShipping() {
		return costItemManualShipping;
	}
	public void setCostItemManualShipping(Double costItemManualShipping) {
		this.costItemManualShipping = costItemManualShipping;
	}
	public String getItemShipping() {
		return itemShipping;
	}
	public void setItemShipping(String itemShipping) {
		this.itemShipping = itemShipping;
	}
	public Double getCostItemShipping() {
		return costItemShipping;
	}
	public void setCostItemShipping(Double costItemShipping) {
		this.costItemShipping = costItemShipping;
	}
	public Double getCostItemsOtherExpenses() {
		return costItemsOtherExpenses;
	}
	public void setCostItemsOtherExpenses(Double costItemsOtherExpenses) {
		this.costItemsOtherExpenses = costItemsOtherExpenses;
	}
	public String getOwerCabinetCoolingSystem() {
		return owerCabinetCoolingSystem;
	}
	public void setOwerCabinetCoolingSystem(String owerCabinetCoolingSystem) {
		this.owerCabinetCoolingSystem = owerCabinetCoolingSystem;
	}
	public String getRectifier3000() {
		return rectifier3000;
	}
	public void setRectifier3000(String rectifier3000) {
		this.rectifier3000 = rectifier3000;
	}
	public String getBatteryLithium() {
		return batteryLithium;
	}
	public void setBatteryLithium(String batteryLithium) {
		this.batteryLithium = batteryLithium;
	}
	public String getOilGenerator() {
		return oilGenerator;
	}
	public void setOilGenerator(String oilGenerator) {
		this.oilGenerator = oilGenerator;
	}
	public String getAts() {
		return ats;
	}
	public void setAts(String ats) {
		this.ats = ats;
	}
	public String getSupervisionControl() {
		return supervisionControl;
	}
	public void setSupervisionControl(String supervisionControl) {
		this.supervisionControl = supervisionControl;
	}
	public String getOtherAuxiliarySystem() {
		return otherAuxiliarySystem;
	}
	public void setOtherAuxiliarySystem(String otherAuxiliarySystem) {
		this.otherAuxiliarySystem = otherAuxiliarySystem;
	}
	public String getPublicInstallationPower() {
		return publicInstallationPower;
	}
	public void setPublicInstallationPower(String publicInstallationPower) {
		this.publicInstallationPower = publicInstallationPower;
	}
	
	
}
