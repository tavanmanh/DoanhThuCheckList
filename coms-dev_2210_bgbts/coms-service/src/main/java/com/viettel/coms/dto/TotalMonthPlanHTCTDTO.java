package com.viettel.coms.dto;
import java.util.List;

//Duonghv13 start-16/08/2021//
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.type.LongType;

import com.viettel.coms.bo.TotalMonthPlanHTCTBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@XmlRootElement(name = "TOTAL_MONTH_PLAN_DTHTBO")
public class TotalMonthPlanHTCTDTO extends ComsBaseFWDTO<TotalMonthPlanHTCTBO>{
	private java.lang.Long totalMonthPlanDTHTId;
	private java.lang.String month;
    private java.lang.Long year;
	private java.lang.String monthYear;
	private java.lang.String provinceCode;
	private java.lang.String stationCodeVCC;
	private java.lang.String stationCodeVTNET;
	private java.lang.Long soluong_KC;
	private java.lang.Long soluong_DB;
	private java.lang.Long soluong_PS;
	private java.lang.Long soluong_TMB;
	private java.lang.Long soluong_HSHC;
	private java.lang.Long tram_toDoanhThu;
	private java.lang.Long soluong_HSHC_DTHT;
	private java.lang.Long soluong_TKDT;
	
	private java.lang.Long sumsoluong_KC;
	private java.lang.Long sumsoluong_DB;
	private java.lang.Long sumsoluong_PS;
	private java.lang.Long sumsoluong_TMB;
	private java.lang.Long sumsoluong_HSHC;
	private java.lang.Long sumsoluong_HSHC_DTHT;
	private java.lang.Long sumtram_toDoanhThu;
	private java.lang.Long sumsoluong_TKDT;
	

	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private java.util.Date insertTime;
	private java.lang.Long createdBy;
	
	private java.lang.String createdByName;
	
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private java.util.Date updatedTime;
	private java.lang.Long updatedBy;
	
	private java.lang.String updatedByName;
	
	private java.lang.String status;
	
	private java.lang.String sysUserName;
	
	private List<String> listmonthYear;
	private List<String> listprovinceCode;
	
	@Override
	public TotalMonthPlanHTCTBO toModel() {
		// TODO Auto-generated method stub
		TotalMonthPlanHTCTBO totalMonthPlanHTCTBO = new TotalMonthPlanHTCTBO();
		totalMonthPlanHTCTBO.setTotalMonthPlanDTHTId(this.totalMonthPlanDTHTId);
		totalMonthPlanHTCTBO.setMonth(this.month);
		totalMonthPlanHTCTBO.setYear(this.year);

		totalMonthPlanHTCTBO.setProvinceCode(this.provinceCode);
		
		totalMonthPlanHTCTBO.setStationCodeVCC(this.stationCodeVCC);
		totalMonthPlanHTCTBO.setStationCodeVTNET(this.stationCodeVTNET);
		
		totalMonthPlanHTCTBO.setSoluong_TMB(this.soluong_TMB);
		totalMonthPlanHTCTBO.setSoluong_KC(this.soluong_KC);
		totalMonthPlanHTCTBO.setSoluong_DB(this.soluong_DB);
		totalMonthPlanHTCTBO.setSoluong_PS(this.soluong_PS);
		totalMonthPlanHTCTBO.setSoluong_HSHC(this.soluong_HSHC);
		totalMonthPlanHTCTBO.setTram_toDoanhThu(this.tram_toDoanhThu);
		totalMonthPlanHTCTBO.setInsertTime(this.insertTime);
		totalMonthPlanHTCTBO.setCreatedBy(this.createdBy);
		totalMonthPlanHTCTBO.setUpdatedTime(this.updatedTime);
		totalMonthPlanHTCTBO.setUpdatedBy(this.updatedBy);
		totalMonthPlanHTCTBO.setStatus(this.status);
		totalMonthPlanHTCTBO.setSoluong_HSHC_DTHT(this.soluong_HSHC_DTHT);
		totalMonthPlanHTCTBO.setSoluong_TKDT(this.soluong_TKDT);
		return totalMonthPlanHTCTBO;
	}
	
	@Override
	public Long getFWModelId() {
	    return totalMonthPlanDTHTId;
	}

	@Override
	public String catchName() {
	     return getTotalMonthPlanDTHTId().toString();
	}
	@JsonProperty("totalMonthPlanDTHTId")
	public java.lang.Long getTotalMonthPlanDTHTId() {
		return totalMonthPlanDTHTId;
	}
	public void setTotalMonthPlanDTHTId(java.lang.Long totalMonthPlanDTHTId) {
		this.totalMonthPlanDTHTId = totalMonthPlanDTHTId;
	}
	@JsonProperty("month")
	public java.lang.String getMonth() {
		return month;
	}
	public void setMonth(java.lang.String month) {
		this.month = month;
	}
	@JsonProperty("year")
	public java.lang.Long getYear() {
		return year;
	}
	public void setYear(java.lang.Long year) {
		this.year = year;
	}
	@JsonProperty("monthYear")
	public java.lang.String getMonthYear() {
		return monthYear;
	}
	public void setMonthYear(java.lang.String monthYear) {
		this.monthYear = monthYear;
	}
	@JsonProperty("provinceCode")
	public java.lang.String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(java.lang.String provinceCode) {
		this.provinceCode = provinceCode;
	}
	
	@JsonProperty("stationCodeVCC")
	public java.lang.String getStationCodeVCC() {
		return stationCodeVCC;
	}

	public void setStationCodeVCC(java.lang.String stationCodeVCC) {
		this.stationCodeVCC = stationCodeVCC;
	}

	@JsonProperty("stationCodeVTNET")
	public java.lang.String getStationCodeVTNET() {
		return stationCodeVTNET;
	}

	public void setStationCodeVTNET(java.lang.String stationCodeVTNET) {
		this.stationCodeVTNET = stationCodeVTNET;
	}

	@JsonProperty("soluong_KC")
	public java.lang.Long getSoluong_KC() {
		return soluong_KC;
	}
	public void setSoluong_KC(java.lang.Long soluong_KC) {
		this.soluong_KC = soluong_KC;
	}
	@JsonProperty("soluong_DB")
	public java.lang.Long getSoluong_DB() {
		return soluong_DB;
	}
	public void setSoluong_DB(java.lang.Long soluong_DB) {
		this.soluong_DB = soluong_DB;
	}
	@JsonProperty("soluong_PS")
	public java.lang.Long getSoluong_PS() {
		return soluong_PS;
	}
	public void setSoluong_PS(java.lang.Long soluong_PS) {
		this.soluong_PS = soluong_PS;
	}
	@JsonProperty("soluong_TMB")
	public java.lang.Long getSoluong_TMB() {
		return soluong_TMB;
	}
	public void setSoluong_TMB(java.lang.Long soluong_TMB) {
		this.soluong_TMB = soluong_TMB;
	}
	@JsonProperty("soluong_HSHC")
	public java.lang.Long getSoluong_HSHC() {
		return soluong_HSHC;
	}
	public void setSoluong_HSHC(java.lang.Long soluong_HSHC) {
		this.soluong_HSHC = soluong_HSHC;
	}
	@JsonProperty("tram_toDoanhThu")
	public java.lang.Long getTram_toDoanhThu() {
		return tram_toDoanhThu;
	}
	public void setTram_toDoanhThu(java.lang.Long tram_toDoanhThu) {
		this.tram_toDoanhThu = tram_toDoanhThu;
	}
	@JsonProperty("insertTime")
	public java.util.Date getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(java.util.Date insertTime) {
		this.insertTime = insertTime;
	}
	@JsonProperty("createdBy")
	public java.lang.Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(java.lang.Long createdBy) {
		this.createdBy = createdBy;
	}
	
	@JsonProperty("createdByName")
	public java.lang.String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(java.lang.String createdByName) {
		this.createdByName = createdByName;
	}

	@JsonProperty("updatedTime")
	public java.util.Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(java.util.Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	@JsonProperty("updatedBy")
	public java.lang.Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(java.lang.Long updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	@JsonProperty("updatedByName")
	public java.lang.String getUpdatedByName() {
		return updatedByName;
	}

	public void setUpdatedByName(java.lang.String updatedByName) {
		this.updatedByName = updatedByName;
	}

	@JsonProperty("status")
	public java.lang.String getStatus() {
		return status;
	}

	public void setStatus(java.lang.String status) {
		this.status = status;
	}
	@JsonProperty("sysUserName")
	public java.lang.String getSysUserName() {
		return sysUserName;
	}

	public void setSysUserName(java.lang.String sysUserName) {
		this.sysUserName = sysUserName;
	}
	@JsonProperty("soluong_HSHC_DTHT")
	public java.lang.Long getSoluong_HSHC_DTHT() {
		return soluong_HSHC_DTHT;
	}

	public void setSoluong_HSHC_DTHT(java.lang.Long soluong_HSHC_DTHT) {
		this.soluong_HSHC_DTHT = soluong_HSHC_DTHT;
	}
	@JsonProperty("soluong_TKDT")
	public java.lang.Long getSoluong_TKDT() {
		return soluong_TKDT;
	}

	public void setSoluong_TKDT(java.lang.Long soluong_TKDT) {
		this.soluong_TKDT = soluong_TKDT;
	}
	@JsonProperty("sumsoluong_KC")
	public java.lang.Long getSumsoluong_KC() {
		return sumsoluong_KC;
	}

	public void setSumsoluong_KC(java.lang.Long sumsoluong_KC) {
		this.sumsoluong_KC = sumsoluong_KC;
	}
	@JsonProperty("sumsoluong_DB")
	public java.lang.Long getSumsoluong_DB() {
		return sumsoluong_DB;
	}

	public void setSumsoluong_DB(java.lang.Long sumsoluong_DB) {
		this.sumsoluong_DB = sumsoluong_DB;
	}
	@JsonProperty("sumsoluong_PS")
	public java.lang.Long getSumsoluong_PS() {
		return sumsoluong_PS;
	}

	public void setSumsoluong_PS(java.lang.Long sumsoluong_PS) {
		this.sumsoluong_PS = sumsoluong_PS;
	}
	@JsonProperty("sumsoluong_TMB")
	public java.lang.Long getSumsoluong_TMB() {
		return sumsoluong_TMB;
	}

	public void setSumsoluong_TMB(java.lang.Long sumsoluong_TMB) {
		this.sumsoluong_TMB = sumsoluong_TMB;
	}
	@JsonProperty("sumsoluong_HSHC")
	public java.lang.Long getSumsoluong_HSHC() {
		return sumsoluong_HSHC;
	}

	public void setSumsoluong_HSHC(java.lang.Long sumsoluong_HSHC) {
		this.sumsoluong_HSHC = sumsoluong_HSHC;
	}
	@JsonProperty("sumsoluong_HSHC_DTHT")
	public java.lang.Long getSumsoluong_HSHC_DTHT() {
		return sumsoluong_HSHC_DTHT;
	}

	public void setSumsoluong_HSHC_DTHT(java.lang.Long sumsoluong_HSHC_DTHT) {
		this.sumsoluong_HSHC_DTHT = sumsoluong_HSHC_DTHT;
	}
	@JsonProperty("sumtram_toDoanhThu")
	public java.lang.Long getSumtram_toDoanhThu() {
		return sumtram_toDoanhThu;
	}

	public void setSumtram_toDoanhThu(java.lang.Long sumtram_toDoanhThu) {
		this.sumtram_toDoanhThu = sumtram_toDoanhThu;
	}
	@JsonProperty("sumsoluong_TKDT")
	public java.lang.Long getSumsoluong_TKDT() {
		return sumsoluong_TKDT;
	}

	public void setSumsoluong_TKDT(java.lang.Long sumsoluong_TKDT) {
		this.sumsoluong_TKDT = sumsoluong_TKDT;
	}

	public List<String> getListmonthYear() {
		return listmonthYear;
	}

	public void setListmonthYear(List<String> listmonthYear) {
		this.listmonthYear = listmonthYear;
	}

	public List<String> getListprovinceCode() {
		return listprovinceCode;
	}

	public void setListprovinceCode(List<String> listprovinceCode) {
		this.listprovinceCode = listprovinceCode;
	}
	
	//Duonghv13 end-16/08/2021//
}
