package com.viettel.coms.dto;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.asset.dto.ResultInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
public class ApiConstructionResponse {

	private ResultInfo resultInfo;
	private String systemCode;
	private String address;
	private String longitude;
	private String latitude;
	private List<ApiListFileTrVhktDTO> lstFileTrVhkt;
	private List<ApiListWOInfoVhkt> lstWOInfoVhkt;
	private List<ApiReportPeriodicMaintanceDTO> lstDataReportMaintancePeriodic;
	private List<ApiConstructionResponse> listConstructionResponse;
	private String systemOriginalCode;
	private Long provinceId;
	private String provinceCode;
	
	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getSystemOriginalCode() {
		return systemOriginalCode;
	}

	public void setSystemOriginalCode(String systemOriginalCode) {
		this.systemOriginalCode = systemOriginalCode;
	}

	public List<ApiConstructionResponse> getListConstructionResponse() {
		return listConstructionResponse;
	}

	public void setListConstructionResponse(List<ApiConstructionResponse> listConstructionResponse) {
		this.listConstructionResponse = listConstructionResponse;
	}

	public List<ApiReportPeriodicMaintanceDTO> getLstDataReportMaintancePeriodic() {
		return lstDataReportMaintancePeriodic;
	}

	public void setLstDataReportMaintancePeriodic(List<ApiReportPeriodicMaintanceDTO> lstDataReportMaintancePeriodic) {
		this.lstDataReportMaintancePeriodic = lstDataReportMaintancePeriodic;
	}

	private Long numUnfinishWO;
	
	public Long getNumUnfinishWO() {
		return numUnfinishWO;
	}

	public void setNumUnfinishWO(Long numUnfinishWO) {
		this.numUnfinishWO = numUnfinishWO;
	}

	public List<ApiListWOInfoVhkt> getLstWOInfoVhkt() {
		return lstWOInfoVhkt;
	}

	public void setLstWOInfoVhkt(List<ApiListWOInfoVhkt> lstWOInfoVhkt) {
		this.lstWOInfoVhkt = lstWOInfoVhkt;
	}

	public List<ApiListFileTrVhktDTO> getLstFileTrVhkt() {
		return lstFileTrVhkt;
	}

	public void setLstFileTrVhkt(List<ApiListFileTrVhktDTO> lstFileTrVhkt) {
		this.lstFileTrVhkt = lstFileTrVhkt;
	}

	public ResultInfo getResultInfo() {
		return resultInfo;
	}

	public void setResultInfo(ResultInfo resultInfo) {
		this.resultInfo = resultInfo;
	}

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

}
