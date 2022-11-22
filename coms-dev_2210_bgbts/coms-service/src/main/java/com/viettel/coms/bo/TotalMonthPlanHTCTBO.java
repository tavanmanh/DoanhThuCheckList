package com.viettel.coms.bo;

//Duonghv13 start-16/08/2021//
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.TotalMonthPlanHTCTDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

@Entity(name="com.viettel.coms.bo.TotalMonthPlanHTCTBO")
@Table(name = "TOTAL_MONTH_PLAN_DTHT")
public class TotalMonthPlanHTCTBO extends BaseFWModelImpl{
	
	private java.lang.Long totalMonthPlanDTHTId;
	private java.lang.String month;
    private java.lang.Long year;
    
	private java.lang.String provinceCode;
	
	private java.lang.String stationCodeVCC;
	private java.lang.String stationCodeVTNET;
	
	private java.lang.Long soluong_KC;
	private java.lang.Long soluong_DB;
	private java.lang.Long soluong_PS;
	private java.lang.Long soluong_TMB;
	private java.lang.Long soluong_HSHC;
	private java.lang.Long tram_toDoanhThu;
	private java.util.Date insertTime;
	private java.lang.Long createdBy;
	
	private java.util.Date updatedTime;
	private java.lang.Long updatedBy;
	
	private java.lang.String status;
	private java.lang.Long soluong_HSHC_DTHT;
	private java.lang.Long soluong_TKDT;
	
	public TotalMonthPlanHTCTBO() {
		setColId("totalMonthPlanDTHTId");
		setColName("totalMonthPlanDTHTId");
		setUniqueColumn(new String[] { "totalMonthPlanDTHTId" });

	}
	
	@Id
    @GeneratedValue(generator = "sequence",strategy = GenerationType.AUTO)
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "TOTAL_MONTH_PLAN_DTHT_SEQ")})
    @Column(name = "TOTAL_MONTH_PLAN_DTHT_ID", length = 22)
	public java.lang.Long getTotalMonthPlanDTHTId() {
		return totalMonthPlanDTHTId;
	}
	public void setTotalMonthPlanDTHTId(java.lang.Long totalMonthPlanDTHTId) {
		this.totalMonthPlanDTHTId = totalMonthPlanDTHTId;
	}
	@Column(name = "MONTH", length = 2)
	public java.lang.String getMonth() {
		return month;
	}
	public void setMonth(java.lang.String month) {
		this.month = month;
	}
	@Column(name = "YEAR", length = 4)
	public java.lang.Long getYear() {
		return year;
	}
	public void setYear(java.lang.Long year) {
		this.year = year;
	}
	
	@Column(name = "PROVINCE_CODE", length = 4)
	public java.lang.String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(java.lang.String provinceCode) {
		this.provinceCode = provinceCode;
	}
	
	@Column(name = "STATION_CODE_VCC", length = 50)
	public java.lang.String getStationCodeVCC() {
		return stationCodeVCC;
	}

	public void setStationCodeVCC(java.lang.String stationCodeVCC) {
		this.stationCodeVCC = stationCodeVCC;
	}

	@Column(name = "STATION_CODE_VTNET", length = 50)
	public java.lang.String getStationCodeVTNET() {
		return stationCodeVTNET;
	}

	public void setStationCodeVTNET(java.lang.String stationCodeVTNET) {
		this.stationCodeVTNET = stationCodeVTNET;
	}

	@Column(name = "KHOI_CONG", length = 20)
	public java.lang.Long getSoluong_KC() {
		return soluong_KC;
	}
	public void setSoluong_KC(java.lang.Long soluong_KC) {
		this.soluong_KC = soluong_KC;
	}
	@Column(name = "DONG_BO", length = 20)
	public java.lang.Long getSoluong_DB() {
		return soluong_DB;
	}
	public void setSoluong_DB(java.lang.Long soluong_DB) {
		this.soluong_DB = soluong_DB;
	}
	@Column(name = "PHAT_SONG", length = 20)
	public java.lang.Long getSoluong_PS() {
		return soluong_PS;
	}
	public void setSoluong_PS(java.lang.Long soluong_PS) {
		this.soluong_PS = soluong_PS;
	}
	@Column(name = "THUE_MAT_BANG", length = 20)
	public java.lang.Long getSoluong_TMB() {
		return soluong_TMB;
	}
	public void setSoluong_TMB(java.lang.Long soluong_TMB) {
		this.soluong_TMB = soluong_TMB;
	}
	@Column(name = "HSHC", length = 20)
	public java.lang.Long getSoluong_HSHC() {
		return soluong_HSHC;
	}
	public void setSoluong_HSHC(java.lang.Long soluong_HSHC) {
		this.soluong_HSHC = soluong_HSHC;
	}
	@Column(name = "DOANH_THU", length = 20)
	public java.lang.Long getTram_toDoanhThu() {
		return tram_toDoanhThu;
	}
	public void setTram_toDoanhThu(java.lang.Long tram_toDoanhThu) {
		this.tram_toDoanhThu = tram_toDoanhThu;
	}
	
	@Column(name = "INSERT_TIME", length = 7)
	public java.util.Date getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(java.util.Date insertTime) {
		this.insertTime = insertTime;
	}
	
	@Column(name = "CREATED_BY")
	public java.lang.Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(java.lang.Long createdBy) {
		this.createdBy = createdBy;
	}
	@Column(name = "UPDATED_TIME", length = 7)
	public java.util.Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(java.util.Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	@Column(name = "UPDATED_BY")
	public java.lang.Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(java.lang.Long updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	@Column(name = "STATUS", length = 1)
	public java.lang.String getStatus() {
		return status;
	}

	public void setStatus(java.lang.String status) {
		this.status = status;
	}

	@Column(name = "HSHC_DTHT", length = 20)
	public java.lang.Long getSoluong_HSHC_DTHT() {
		return soluong_HSHC_DTHT;
	}

	public void setSoluong_HSHC_DTHT(java.lang.Long soluong_HSHC_DTHT) {
		this.soluong_HSHC_DTHT = soluong_HSHC_DTHT;
	}
	@Column(name = "TKDT", length = 20)
	public java.lang.Long getSoluong_TKDT() {
		return soluong_TKDT;
	}

	public void setSoluong_TKDT(java.lang.Long soluong_TKDT) {
		this.soluong_TKDT = soluong_TKDT;
	}

	@Override
	public TotalMonthPlanHTCTDTO  toDTO() {
		// TODO Auto-generated method stub
		TotalMonthPlanHTCTDTO totalMonthPlanHTCTDTO = new TotalMonthPlanHTCTDTO();
		totalMonthPlanHTCTDTO.setTotalMonthPlanDTHTId(this.totalMonthPlanDTHTId);
		totalMonthPlanHTCTDTO.setMonth(this.month);
		totalMonthPlanHTCTDTO.setYear(this.year);
//		totalMonthPlanHTCTDTO.setMonthYear(this.monthYear);
		totalMonthPlanHTCTDTO.setProvinceCode(this.provinceCode);
		
		totalMonthPlanHTCTDTO.setStationCodeVCC(this.stationCodeVCC);
		totalMonthPlanHTCTDTO.setStationCodeVTNET(this.stationCodeVTNET);
		
		totalMonthPlanHTCTDTO.setSoluong_TMB(this.soluong_TMB);
		totalMonthPlanHTCTDTO.setSoluong_KC(this.soluong_KC);
		totalMonthPlanHTCTDTO.setSoluong_DB(this.soluong_DB);
		totalMonthPlanHTCTDTO.setSoluong_PS(this.soluong_PS);
		totalMonthPlanHTCTDTO.setSoluong_HSHC(this.soluong_HSHC);
		totalMonthPlanHTCTDTO.setTram_toDoanhThu(this.tram_toDoanhThu);
		totalMonthPlanHTCTDTO.setInsertTime(this.insertTime);
		totalMonthPlanHTCTDTO.setCreatedBy(this.createdBy);
		totalMonthPlanHTCTDTO.setUpdatedTime(this.updatedTime);
		totalMonthPlanHTCTDTO.setUpdatedBy(this.updatedBy);
		totalMonthPlanHTCTDTO.setStatus(this.status);
		totalMonthPlanHTCTDTO.setSoluong_HSHC_DTHT(this.soluong_HSHC_DTHT);
		totalMonthPlanHTCTDTO.setSoluong_TKDT(this.soluong_TKDT);
		return totalMonthPlanHTCTDTO;
	}
	
	
	//Duonghv13 end-16/08/2021//
}
