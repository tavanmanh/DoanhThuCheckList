package com.viettel.coms.dto;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class ConstructioIocDTO {

	private Long constructionId;
    private String code;
    private String name;
    private Long stationId;
    private Long sysGroupId;
    private String b2BB2C;
    private String contructionType;
    private Date deploymentDate;
    private Date createDate;
    private Long createUser;
    private Long status;
    private Long performerId;
    private String stationCode;
    private Long stationHouseId;
    private String stationHouseCode;
    private String provinceCode;
    private String provinceName;
    private Date startDate;
    private Date endDate;

	public Long getConstructionId() {
		return constructionId;
	}
	public void setConstructionId(Long constructionId) {
		this.constructionId = constructionId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getStationId() {
		return stationId;
	}
	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}
	public Long getSysGroupId() {
		return sysGroupId;
	}
	public void setSysGroupId(Long sysGroupId) {
		this.sysGroupId = sysGroupId;
	}
	public String getB2BB2C() {
		return b2BB2C;
	}
	public void setB2BB2C(String b2bb2c) {
		b2BB2C = b2bb2c;
	}
	public String getContructionType() {
		return contructionType;
	}
	public void setContructionType(String contructionType) {
		this.contructionType = contructionType;
	}
	public Date getDeploymentDate() {
		return deploymentDate;
	}
	public void setDeploymentDate(Date deploymentDate) {
		this.deploymentDate = deploymentDate;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Long getCreateUser() {
		return createUser;
	}
	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}
	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
	}
	public Long getPerformerId() {
		return performerId;
	}
	public void setPerformerId(Long performerId) {
		this.performerId = performerId;
	}
	public String getStationCode() {
		return stationCode;
	}
	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}
	public Long getStationHouseId() {
		return stationHouseId;
	}
	public void setStationHouseId(Long stationHouseId) {
		this.stationHouseId = stationHouseId;
	}
	public String getStationHouseCode() {
		return stationHouseCode;
	}
	public void setStationHouseCode(String stationHouseCode) {
		this.stationHouseCode = stationHouseCode;
	}
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

    
}
