package com.viettel.coms.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.ElectricRectifierDTO;
import com.viettel.coms.dto.StationInformationDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

@SuppressWarnings("serial")
@Entity(name = "com.viettel.coms.bo.StationInformationBO")
@Table(name = "STATION_INFORMATION")
public class StationInformationBO extends BaseFWModelImpl{

	@Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "STATION_INFORMATION_SEQ")})
	
	@Column(name = "ID", length = 22)
	private Long id;
	@Column(name = "DEVICE_ID", length = 22)
	private Long deviceId;
	@Column(name = "MANAGER", length = 200)
	private String manager;
	@Column(name = "ADDRESS", length = 200)
	private String address;
	@Column(name = "STATION_TYPE", length = 200)
	private String stationType;
	@Column(name = "STATION_HOUSE_TYPE", length = 200)
	private String stationHouseType;
	@Column(name = "STATION_HOUSE_SIZE", length = 200)
	private String stationHouseSize;
	@Column(name = "WATTAGE_MAX", length = 10)
	private Long wattageMax;
	@Column(name = "WATTAGE_MAX_ACQUY", length = 10)
	private Long wattageMaxAcquy;
	@Column(name = "WATTAGE_ACQUY", length = 10)
	private Long wattageAcquy;
	@Column(name = "WATTAGE_AIR_CONDITIONING", length = 10)
	private Long wattageAirConditioning;
	@Column(name = "WATTAGE_DUST_FILTER", length = 10)
	private Long wattageDustFilter;
	@Column(name = "WATTAGE_HEAD_EXCHANGERS", length = 10)
	private Long wattageHeadExchangers;
	@Column(name = "VT_WATTAGE_TV", length = 10)
	private Long vtWattageTV;
	@Column(name = "VT_WATTAGE_TRANSMISSION", length = 10)
	private Long vtWattageTransmission;
	@Column(name = "VT_WATTAGE_IP", length = 10)
	private Long vtWattageIP;
	@Column(name = "VT_WATTAGE_CDBR", length = 10)
	private Long vtWattageCDBR;
	@Column(name = "VT_WATTAGE_PSTN", length = 10)
	private Long vtWattagePSTN;
	@Column(name = "VNP_WATTAGE_TV", length = 10)
	private Long vnpWattageTV;
	@Column(name = "VNP_WATTAGE_TRANSMISSION", length = 10)
	private Long vnpWattageTransmission;
	@Column(name = "VNP_WATTAGE_IP", length = 10)
	private Long vnpWattageIP;
	@Column(name = "VNP_WATTAGE_CDBR", length = 10)
	private Long vnpWattageCDBR;
	@Column(name = "VNP_WATTAGE_PSTN", length = 10)
	private Long vnpWattagePSTN;
	@Column(name = "VNM_WATTAGE_TV", length = 10)
	private Long vnmWattageTV;
	@Column(name = "VNM_WATTAGE_TRANSMISSION", length = 10)
	private Long vnmWattageTransmission;
	@Column(name = "VNM_WATTAGE_IP", length = 10)
	private Long vnmWattageIP;
	@Column(name = "VNM_WATTAGE_CDBR", length = 10)
	private Long vnmWattageCDBR;
	@Column(name = "VNM_WATTAGE_PSTN", length = 10)
	private Long vnmWattagePSTN;
	@Column(name = "MBP_WATTAGE_TV", length = 10)
	private Long mbpWattageTV;
	@Column(name = "MBP_WATTAGE_TRANSMISSION", length = 10)
	private Long mbpWattageTransmission;
	@Column(name = "MBP_WATTAGE_IP", length = 10)
	private Long mbpWattageIP;
	@Column(name = "MBP_WATTAGE_CDBR", length = 10)
	private Long mbpWattageCDBR;
	@Column(name = "MBP_WATTAGE_PSTN", length = 10)
	private Long mbpWattagePSTN;
	
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

	@Override
	public StationInformationDTO toDTO() {
		// TODO Auto-generated method stub
		StationInformationDTO obj = new StationInformationDTO();
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
