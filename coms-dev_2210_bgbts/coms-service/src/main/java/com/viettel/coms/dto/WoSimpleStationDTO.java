package com.viettel.coms.dto;

import java.util.List;

public class WoSimpleStationDTO {
    private Long stationId;
    private String stationName;
    private String stationCode;
    private String stationAddress;
    private String latitude;
    private String longitude;
    private String keySearch;
    private String completeStatus;
    private List<String> codeRange;
    private String catProvinceCode;
    private Long constructionId;
    private Long contractId;
    private String areaCode;
    private Long catStationHouseId;
    private String catStationHouseCode;

    // Dong bo ha tang
    private Long sysGroupId;
    private String sysGroupCode;
    private String sysGroupName;
    private String checkDBHT;

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public String getStationAddress() {
        return stationAddress;
    }

    public void setStationAddress(String stationAddress) {
        this.stationAddress = stationAddress;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getKeySearch() {
        return keySearch;
    }

    public void setKeySearch(String keySearch) {
        this.keySearch = keySearch;
    }

    public List<String> getCodeRange() {
        return codeRange;
    }

    public void setCodeRange(List<String> codeRange) {
        this.codeRange = codeRange;
    }

    public String getCompleteStatus() {
        return completeStatus;
    }

    public void setCompleteStatus(String completeStatus) {
        this.completeStatus = completeStatus;
    }

    public String getCatProvinceCode() {
        return catProvinceCode;
    }

    public void setCatProvinceCode(String catProvinceCode) {
        this.catProvinceCode = catProvinceCode;
    }

    public Long getConstructionId() {
        return constructionId;
    }

    public void setConstructionId(Long constructionId) {
        this.constructionId = constructionId;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public Long getCatStationHouseId() {
        return catStationHouseId;
    }

    public void setCatStationHouseId(Long catStationHouseId) {
        this.catStationHouseId = catStationHouseId;
    }

    public String getCatStationHouseCode() {
        return catStationHouseCode;
    }

    public void setCatStationHouseCode(String catStationHouseCode) {
        this.catStationHouseCode = catStationHouseCode;
    }

    public Long getSysGroupId() {
        return sysGroupId;
    }

    public void setSysGroupId(Long sysGroupId) {
        this.sysGroupId = sysGroupId;
    }

    public String getSysGroupCode() {
        return sysGroupCode;
    }

    public void setSysGroupCode(String sysGroupCode) {
        this.sysGroupCode = sysGroupCode;
    }

    public String getSysGroupName() {
        return sysGroupName;
    }

    public void setSysGroupName(String sysGroupName) {
        this.sysGroupName = sysGroupName;
    }

	public String getCheckDBHT() {
		return checkDBHT;
	}

	public void setCheckDBHT(String checkDBHT) {
		this.checkDBHT = checkDBHT;
	}

}
