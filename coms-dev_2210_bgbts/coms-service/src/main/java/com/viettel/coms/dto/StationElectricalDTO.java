package com.viettel.coms.dto;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.TangentCustomerBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;
import com.viettel.service.base.model.BaseFWModelImpl;
@XmlRootElement(name = "STATION_ELECTRICALBO")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class StationElectricalDTO extends ComsBaseFWDTO<BaseFWModelImpl>{
	private java.lang.Long stationElectricalId;
	private java.lang.Long stationId;
	private java.lang.String stationCode;
	private java.lang.String stationAddress;
	private java.lang.String areaCode;
	private java.lang.Long manageUserId;
	private java.lang.String manageUserCode;
	private java.lang.String manageUserName;
	private java.lang.String manageUserEmail;
	private java.lang.String stationType;
	private Boolean checkRoleCD;
	private Long sysGroupId;
	private Long manageUserPhone;
	private Date formedAssetDate;
	
//	@JsonSerialize(using = JsonDateSerializerDate.class)
//	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date broadcastingDate;
	
//	@JsonSerialize(using = JsonDateSerializerDate.class)
//	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date lastMaintenance;
	
	private java.lang.Long status;
	
	private java.lang.Long provinceId;
	private java.lang.String catProvince;
	private java.lang.String deviceType;
	private String stationCodeVtnet;
	private Long tramTrenMai;
	private Long tramDuoiDat;
	private Long khongDien;
	private Long motPha;
	private Long baPha;
	private Long muaEVN;
	private Long muaNgoaiEVN;
	private Long tram1Rectifiter;
	private Long tramcoATS;
	private Long mayphatCoDinh;
	private Long mayPhatHong;
	private Long woQuaHan;
	private Long woDangThucHien;
	private String location;
	private String address;
	private String provinceCode;
	private Long type;
	private Boolean checkUserKCQTD;
	private List<Long> listType;
	private Long pheDuyet;
	
	public java.lang.Long getStationId() {
		return stationId;
	}
	public void setStationId(java.lang.Long stationId) {
		this.stationId = stationId;
	}
	public java.lang.String getStationCode() {
		return stationCode;
	}
	public void setStationCode(java.lang.String stationCode) {
		this.stationCode = stationCode;
	}
	public java.lang.String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(java.lang.String areaCode) {
		this.areaCode = areaCode;
	}
	
	public java.lang.Long getManageUserId() {
		return manageUserId;
	}
	public void setManageUserId(java.lang.Long manageUserId) {
		this.manageUserId = manageUserId;
	}
	public java.lang.String getManageUserCode() {
		return manageUserCode;
	}
	public void setManageUserCode(java.lang.String manageUserCode) {
		this.manageUserCode = manageUserCode;
	}
	public java.lang.String getManageUserName() {
		return manageUserName;
	}
	public void setManageUserName(java.lang.String manageUserName) {
		this.manageUserName = manageUserName;
	}
	public java.lang.String getStationType() {
		return stationType;
	}
	public void setStationType(java.lang.String stationType) {
		this.stationType = stationType;
	}
	public java.util.Date getBroadcastingDate() {
		return broadcastingDate;
	}
	public void setBroadcastingDate(java.util.Date broadcastingDate) {
		this.broadcastingDate = broadcastingDate;
	}
	public java.util.Date getLastMaintenance() {
		return lastMaintenance;
	}
	public void setLastMaintenance(java.util.Date lastMaintenance) {
		this.lastMaintenance = lastMaintenance;
	}
	public java.lang.Long getStatus() {
		return status;
	}
	public void setStatus(java.lang.Long status) {
		this.status = status;
	}
	
	public java.lang.Long getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(java.lang.Long provinceId) {
		this.provinceId = provinceId;
	}
	public java.lang.String getCatProvince() {
		return catProvince;
	}
	public void setCatProvince(java.lang.String catProvince) {
		this.catProvince = catProvince;
	}
	
	public java.lang.String getManageUserEmail() {
		return manageUserEmail;
	}
	public void setManageUserEmail(java.lang.String manageUserEmail) {
		this.manageUserEmail = manageUserEmail;
	}

	public java.lang.String getStationAddress() {
		return stationAddress;
	}
	public void setStationAddress(java.lang.String stationAddress) {
		this.stationAddress = stationAddress;
	}
	
	public java.lang.String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(java.lang.String deviceType) {
		this.deviceType = deviceType;
	}
	
	public String getStationCodeVtnet() {
		return stationCodeVtnet;
	}
	public void setStationCodeVtnet(String stationCodeVtnet) {
		this.stationCodeVtnet = stationCodeVtnet;
	}
	
	public java.lang.Long getStationElectricalId() {
		return stationElectricalId;
	}
	public void setStationElectricalId(java.lang.Long stationElectricalId) {
		this.stationElectricalId = stationElectricalId;
	}
	
	public Boolean getCheckRoleCD() {
		return checkRoleCD;
	}
	public void setCheckRoleCD(Boolean checkRoleCD) {
		this.checkRoleCD = checkRoleCD;
	}
	
	public Long getSysGroupId() {
		return sysGroupId;
	}
	public void setSysGroupId(Long sysGroupId) {
		this.sysGroupId = sysGroupId;
	}
	
	
	public Long getTramTrenMai() {
		return tramTrenMai;
	}
	public void setTramTrenMai(Long tramTrenMai) {
		this.tramTrenMai = tramTrenMai;
	}
	public Long getTramDuoiDat() {
		return tramDuoiDat;
	}
	public void setTramDuoiDat(Long tramDuoiDat) {
		this.tramDuoiDat = tramDuoiDat;
	}
	public Long getKhongDien() {
		return khongDien;
	}
	public void setKhongDien(Long khongDien) {
		this.khongDien = khongDien;
	}
	public Long getMotPha() {
		return motPha;
	}
	public void setMotPha(Long motPha) {
		this.motPha = motPha;
	}
	public Long getBaPha() {
		return baPha;
	}
	public void setBaPha(Long baPha) {
		this.baPha = baPha;
	}
	public Long getMuaEVN() {
		return muaEVN;
	}
	public void setMuaEVN(Long muaEVN) {
		this.muaEVN = muaEVN;
	}
	public Long getMuaNgoaiEVN() {
		return muaNgoaiEVN;
	}
	public void setMuaNgoaiEVN(Long muaNgoaiEVN) {
		this.muaNgoaiEVN = muaNgoaiEVN;
	}
	public Long getTram1Rectifiter() {
		return tram1Rectifiter;
	}
	public void setTram1Rectifiter(Long tram1Rectifiter) {
		this.tram1Rectifiter = tram1Rectifiter;
	}
	public Long getTramcoATS() {
		return tramcoATS;
	}
	public void setTramcoATS(Long tramcoATS) {
		this.tramcoATS = tramcoATS;
	}
	public Long getMayphatCoDinh() {
		return mayphatCoDinh;
	}
	public void setMayphatCoDinh(Long mayphatCoDinh) {
		this.mayphatCoDinh = mayphatCoDinh;
	}
	public Long getMayPhatHong() {
		return mayPhatHong;
	}
	public void setMayPhatHong(Long mayPhatHong) {
		this.mayPhatHong = mayPhatHong;
	}
	
	public Long getWoQuaHan() {
		return woQuaHan;
	}
	public void setWoQuaHan(Long woQuaHan) {
		this.woQuaHan = woQuaHan;
	}
	public Long getWoDangThucHien() {
		return woDangThucHien;
	}
	public void setWoDangThucHien(Long woDangThucHien) {
		this.woDangThucHien = woDangThucHien;
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
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public Long getType() {
		return type;
	}
	public void setType(Long type) {
		this.type = type;
	}
	
	public Boolean getCheckUserKCQTD() {
		return checkUserKCQTD;
	}
	public void setCheckUserKCQTD(Boolean checkUserKCQTD) {
		this.checkUserKCQTD = checkUserKCQTD;
	}
	
	public List<Long> getListType() {
		return listType;
	}
	public void setListType(List<Long> listType) {
		this.listType = listType;
	}
	
	public Long getManageUserPhone() {
		return manageUserPhone;
	}
	public void setManageUserPhone(Long manageUserPhone) {
		this.manageUserPhone = manageUserPhone;
	}
	@Override
	public String catchName() {
		// TODO Auto-generated method stub
		return stationId.toString();
	}
	
	public Long getPheDuyet() {
		return pheDuyet;
	}
	public void setPheDuyet(Long pheDuyet) {
		this.pheDuyet = pheDuyet;
	}
	
	public Date getFormedAssetDate() {
		return formedAssetDate;
	}
	public void setFormedAssetDate(Date formedAssetDate) {
		this.formedAssetDate = formedAssetDate;
	}
	@Override
	public Long getFWModelId() {
		// TODO Auto-generated method stub
		return stationId;
	}
	@Override
	public BaseFWModelImpl toModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
