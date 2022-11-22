package com.viettel.coms.dto;

import com.viettel.service.base.model.BaseFWModelImpl;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.viettel.service.base.dto.BaseFWDTOImpl;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RpKHBTSDTO extends ComsBaseFWDTO<BaseFWModelImpl>{

	private String provinceName;
	private String provinceCode;
	private Double planTMB;
	private Double performTMB;
	private Double ratioTMB;
	private Double planTKDT;
	private Double performTKDT;
	private Double ratioTKDT;
	private Double planTTKDT;
	private Double performTTKDT;
	private Double ratioTTKDT;
	private Double planKC;
	private Double performKC;
	private Double ratioKC;
	private Double planDBHT;
	private Double performDBHT;
	private Double ratioDBHT;
	private Double planPS;
	private Double performPS;
	private Double ratioPS;
	private Double planDT;
	private Double performDT;
	private Double ratioDT;
	private Double planHSHCTTHT;
	private Double performHSHCTTHT;
	private Double ratioHSHCTTHT;
	private Double planHSHCDTHT;
	private Double performHSHCDTHT;
	private Double ratioHSHCDTHT;
	private Double overdueKPIRentMB;
	private Double overdueKPIKC;
	private Double overdueKPILDT;
	private Double overdueKPIHSHCTTHT;
	private Double overdueKPIHSHCDTHT;
	private Double overdueKPIDBHT;
	private Double overdueTKDT;
	private Double rentMB;
	private Double startUp;
	private Double rentDBHT;
	private Double draftingRevenue;
	private Double hshcTTHT;
	private Double sumPlanTMB;
	private Double sumPerformTMB;
	private Double sumRatioTMB;
	private Double sumPlanTKDT;
	private Double sumPerformTKDT;
	private Double sumRatioTKDT;
	private Double sumPlanTTKDT;
	private Double sumPerformTTKDT;
	private Double sumRatioTTKDT;
	private Double sumPlanKC;
	private Double sumPerformKC;
	private Double sumRatioKC;
	private Double sumPlanDBHT;
	private Double sumPerformDBHT;
	private Double sumRatioDBHT;
	private Double sumPlanPS;
	private Double sumPerformPS;
	private Double sumRatioPS;
	private Double sumPlanDT;
	private Double sumPerformDT;
	private Double sumRatioDT;
	private Double sumPlanHSHCTTHT;
	private Double sumPerformHSHCTTHT;
	private Double sumRatioHSHCTTHT;
	private Double sumPlanHSHCDTHT;
	private Double sumPerformHSHCDTHT;
	private Double sumRatioHSHCDTHT;
	private Double sumOverdueKPIRentMB;
	private Double sumOverdueKPIKC;
	private Double sumOverdueKPILDT;
	private Double sumOverdueKPIHSHCTTHT;
	private Double sumOverdueKPIHSHCDTHT;
	private Double sumOverdueTKDT;
	private Double sumRentMB;
	private Double sumStartUp;
	private Double sumDraftingRevenue;
	private Double sumHshcTTHT;
	private Double sumOverdueKPIDBHT;
	private Double sumRentDBHT;
	
	private Integer totalRecord;
	private Integer pageSize;
	private String monthYear;
	private String provinceId;
	private int types;
	private Long woId;
	private String vtnetCode;
	private String vccCode;
	private int numberDateLimitKPI;
	private Double numberFines;
	private String provinceCodeD;
	private String monthYearD;
	private Long woTypeId;
	private Long trTypeId;
	private Long catWorkItemTypeId;
	private String woCode;
	private String woName;
	private String status;
	private String constructionCode;
	
	private String itemName;
	private String contractCode;
	private Date finishDate;
	private String ftName;
	private Double cost;
	
	private String trCode;
	private String stationCode;
	private String cd2Name;
	private String type;
	private Date createDate;
	private Date approveDate;
	private String typeBc;
	private String stationVccCode;
	private String stationVtNetCode;
	
	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public Double getPlanTMB() {
		return planTMB;
	}

	public void setPlanTMB(Double planTMB) {
		this.planTMB = planTMB;
	}

	public Double getPerformTMB() {
		return performTMB;
	}

	public void setPerformTMB(Double performTMB) {
		this.performTMB = performTMB;
	}

	public Double getRatioTMB() {
		return ratioTMB;
	}

	public void setRatioTMB(Double ratioTMB) {
		this.ratioTMB = ratioTMB;
	}

	public Double getPlanTKDT() {
		return planTKDT;
	}

	public void setPlanTKDT(Double planTKDT) {
		this.planTKDT = planTKDT;
	}

	public Double getPerformTKDT() {
		return performTKDT;
	}

	public void setPerformTKDT(Double performTKDT) {
		this.performTKDT = performTKDT;
	}

	public Double getRatioTKDT() {
		return ratioTKDT;
	}

	public void setRatioTKDT(Double ratioTKDT) {
		this.ratioTKDT = ratioTKDT;
	}

	public Double getPlanTTKDT() {
		return planTTKDT;
	}

	public void setPlanTTKDT(Double planTTKDT) {
		this.planTTKDT = planTTKDT;
	}

	public Double getPerformTTKDT() {
		return performTTKDT;
	}

	public void setPerformTTKDT(Double performTTKDT) {
		this.performTTKDT = performTTKDT;
	}

	public Double getRatioTTKDT() {
		return ratioTTKDT;
	}

	public void setRatioTTKDT(Double ratioTTKDT) {
		this.ratioTTKDT = ratioTTKDT;
	}

	public Double getPlanKC() {
		return planKC;
	}

	public void setPlanKC(Double planKC) {
		this.planKC = planKC;
	}

	public Double getPerformKC() {
		return performKC;
	}

	public void setPerformKC(Double performKC) {
		this.performKC = performKC;
	}

	public Double getRatioKC() {
		return ratioKC;
	}

	public void setRatioKC(Double ratioKC) {
		this.ratioKC = ratioKC;
	}

	public Double getPlanDBHT() {
		return planDBHT;
	}

	public void setPlanDBHT(Double planDBHT) {
		this.planDBHT = planDBHT;
	}

	public Double getPerformDBHT() {
		return performDBHT;
	}

	public void setPerformDBHT(Double performDBHT) {
		this.performDBHT = performDBHT;
	}

	public Double getRatioDBHT() {
		return ratioDBHT;
	}

	public void setRatioDBHT(Double ratioDBHT) {
		this.ratioDBHT = ratioDBHT;
	}

	public Double getPlanPS() {
		return planPS;
	}

	public void setPlanPS(Double planPS) {
		this.planPS = planPS;
	}

	public Double getPerformPS() {
		return performPS;
	}

	public void setPerformPS(Double performPS) {
		this.performPS = performPS;
	}

	public Double getRatioPS() {
		return ratioPS;
	}

	public void setRatioPS(Double ratioPS) {
		this.ratioPS = ratioPS;
	}

	public Double getPlanDT() {
		return planDT;
	}

	public void setPlanDT(Double planDT) {
		this.planDT = planDT;
	}

	public Double getPerformDT() {
		return performDT;
	}

	public void setPerformDT(Double performDT) {
		this.performDT = performDT;
	}

	public Double getRatioDT() {
		return ratioDT;
	}

	public void setRatioDT(Double ratioDT) {
		this.ratioDT = ratioDT;
	}

	public Double getPlanHSHCTTHT() {
		return planHSHCTTHT;
	}

	public void setPlanHSHCTTHT(Double planHSHCTTHT) {
		this.planHSHCTTHT = planHSHCTTHT;
	}

	public Double getPerformHSHCTTHT() {
		return performHSHCTTHT;
	}

	public void setPerformHSHCTTHT(Double performHSHCTTHT) {
		this.performHSHCTTHT = performHSHCTTHT;
	}

	public Double getRatioHSHCTTHT() {
		return ratioHSHCTTHT;
	}

	public void setRatioHSHCTTHT(Double ratioHSHCTTHT) {
		this.ratioHSHCTTHT = ratioHSHCTTHT;
	}

	public Double getPlanHSHCDTHT() {
		return planHSHCDTHT;
	}

	public void setPlanHSHCDTHT(Double planHSHCDTHT) {
		this.planHSHCDTHT = planHSHCDTHT;
	}

	public Double getPerformHSHCDTHT() {
		return performHSHCDTHT;
	}

	public void setPerformHSHCDTHT(Double performHSHCDTHT) {
		this.performHSHCDTHT = performHSHCDTHT;
	}

	public Double getRatioHSHCDTHT() {
		return ratioHSHCDTHT;
	}

	public void setRatioHSHCDTHT(Double ratioHSHCDTHT) {
		this.ratioHSHCDTHT = ratioHSHCDTHT;
	}

	public Double getOverdueKPIRentMB() {
		return overdueKPIRentMB;
	}

	public void setOverdueKPIRentMB(Double overdueKPIRentMB) {
		this.overdueKPIRentMB = overdueKPIRentMB;
	}

	public Double getOverdueKPIKC() {
		return overdueKPIKC;
	}

	public void setOverdueKPIKC(Double overdueKPIKC) {
		this.overdueKPIKC = overdueKPIKC;
	}

	public Double getOverdueKPILDT() {
		return overdueKPILDT;
	}

	public void setOverdueKPILDT(Double overdueKPILDT) {
		this.overdueKPILDT = overdueKPILDT;
	}

	public Double getOverdueKPIHSHCTTHT() {
		return overdueKPIHSHCTTHT;
	}

	public void setOverdueKPIHSHCTTHT(Double overdueKPIHSHCTTHT) {
		this.overdueKPIHSHCTTHT = overdueKPIHSHCTTHT;
	}

	public Double getOverdueKPIHSHCDTHT() {
		return overdueKPIHSHCDTHT;
	}

	public void setOverdueKPIHSHCDTHT(Double overdueKPIHSHCDTHT) {
		this.overdueKPIHSHCDTHT = overdueKPIHSHCDTHT;
	}

	public Double getOverdueTKDT() {
		return overdueTKDT;
	}

	public void setOverdueTKDT(Double overdueTKDT) {
		this.overdueTKDT = overdueTKDT;
	}

	public Double getRentMB() {
		return rentMB;
	}

	public void setRentMB(Double rentMB) {
		this.rentMB = rentMB;
	}

	public Double getStartUp() {
		return startUp;
	}

	public void setStartUp(Double startUp) {
		this.startUp = startUp;
	}

	public Double getDraftingRevenue() {
		return draftingRevenue;
	}

	public void setDraftingRevenue(Double draftingRevenue) {
		this.draftingRevenue = draftingRevenue;
	}

	public Double getHshcTTHT() {
		return hshcTTHT;
	}

	public void setHshcTTHT(Double hshcTTHT) {
		this.hshcTTHT = hshcTTHT;
	}

	public Integer getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(Integer totalRecord) {
		this.totalRecord = totalRecord;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getMonthYear() {
		return monthYear;
	}

	public void setMonthYear(String monthYear) {
		this.monthYear = monthYear;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	

	public int getTypes() {
		return types;
	}

	public void setTypes(int types) {
		this.types = types;
	}

	public Long getWoId() {
		return woId;
	}

	public void setWoId(Long woId) {
		this.woId = woId;
	}

	public String getVtnetCode() {
		return vtnetCode;
	}

	public void setVtnetCode(String vtnetCode) {
		this.vtnetCode = vtnetCode;
	}

	public String getVccCode() {
		return vccCode;
	}

	public void setVccCode(String vccCode) {
		this.vccCode = vccCode;
	}

	public int getNumberDateLimitKPI() {
		return numberDateLimitKPI;
	}

	public void setNumberDateLimitKPI(int numberDateLimitKPI) {
		this.numberDateLimitKPI = numberDateLimitKPI;
	}

	public Double getNumberFines() {
		return numberFines;
	}

	public void setNumberFines(Double numberFines) {
		this.numberFines = numberFines;
	}

	/*@Override
	public BaseFWDTOImpl toDTO() {
		// TODO Auto-generated method stub
		return null;
	}*/

	@Override
	public String catchName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getProvinceCodeD() {
		return provinceCodeD;
	}

	public void setProvinceCodeD(String provinceCodeD) {
		this.provinceCodeD = provinceCodeD;
	}

	public String getMonthYearD() {
		return monthYearD;
	}

	public void setMonthYearD(String monthYearD) {
		this.monthYearD = monthYearD;
	}

	public Long getWoTypeId() {
		return woTypeId;
	}

	public void setWoTypeId(Long woTypeId) {
		this.woTypeId = woTypeId;
	}

	public Long getTrTypeId() {
		return trTypeId;
	}

	public void setTrTypeId(Long trTypeId) {
		this.trTypeId = trTypeId;
	}

	public Long getCatWorkItemTypeId() {
		return catWorkItemTypeId;
	}

	public void setCatWorkItemTypeId(Long catWorkItemTypeId) {
		this.catWorkItemTypeId = catWorkItemTypeId;
	}

	public String getWoCode() {
		return woCode;
	}

	public void setWoCode(String woCode) {
		this.woCode = woCode;
	}

	public String getWoName() {
		return woName;
	}

	public void setWoName(String woName) {
		this.woName = woName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getConstructionCode() {
		return constructionCode;
	}

	public void setConstructionCode(String constructionCode) {
		this.constructionCode = constructionCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public Date getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}

	public String getFtName() {
		return ftName;
	}

	public void setFtName(String ftName) {
		this.ftName = ftName;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public String getTrCode() {
		return trCode;
	}

	public void setTrCode(String trCode) {
		this.trCode = trCode;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public String getCd2Name() {
		return cd2Name;
	}

	public void setCd2Name(String cd2Name) {
		this.cd2Name = cd2Name;
	}

	public Double getSumPlanTMB() {
		return sumPlanTMB;
	}

	public void setSumPlanTMB(Double sumPlanTMB) {
		this.sumPlanTMB = sumPlanTMB;
	}

	public Double getSumPerformTMB() {
		return sumPerformTMB;
	}

	public void setSumPerformTMB(Double sumPerformTMB) {
		this.sumPerformTMB = sumPerformTMB;
	}

	public Double getSumRatioTMB() {
		return sumRatioTMB;
	}

	public void setSumRatioTMB(Double sumRatioTMB) {
		this.sumRatioTMB = sumRatioTMB;
	}

	public Double getSumPlanTKDT() {
		return sumPlanTKDT;
	}

	public void setSumPlanTKDT(Double sumPlanTKDT) {
		this.sumPlanTKDT = sumPlanTKDT;
	}

	public Double getSumPerformTKDT() {
		return sumPerformTKDT;
	}

	public void setSumPerformTKDT(Double sumPerformTKDT) {
		this.sumPerformTKDT = sumPerformTKDT;
	}

	public Double getSumRatioTKDT() {
		return sumRatioTKDT;
	}

	public void setSumRatioTKDT(Double sumRatioTKDT) {
		this.sumRatioTKDT = sumRatioTKDT;
	}

	public Double getSumPlanTTKDT() {
		return sumPlanTTKDT;
	}

	public void setSumPlanTTKDT(Double sumPlanTTKDT) {
		this.sumPlanTTKDT = sumPlanTTKDT;
	}

	public Double getSumPerformTTKDT() {
		return sumPerformTTKDT;
	}

	public void setSumPerformTTKDT(Double sumPerformTTKDT) {
		this.sumPerformTTKDT = sumPerformTTKDT;
	}

	public Double getSumRatioTTKDT() {
		return sumRatioTTKDT;
	}

	public void setSumRatioTTKDT(Double sumRatioTTKDT) {
		this.sumRatioTTKDT = sumRatioTTKDT;
	}

	public Double getSumPlanKC() {
		return sumPlanKC;
	}

	public void setSumPlanKC(Double sumPlanKC) {
		this.sumPlanKC = sumPlanKC;
	}

	public Double getSumPerformKC() {
		return sumPerformKC;
	}

	public void setSumPerformKC(Double sumPerformKC) {
		this.sumPerformKC = sumPerformKC;
	}

	public Double getSumRatioKC() {
		return sumRatioKC;
	}

	public void setSumRatioKC(Double sumRatioKC) {
		this.sumRatioKC = sumRatioKC;
	}

	public Double getSumPlanDBHT() {
		return sumPlanDBHT;
	}

	public void setSumPlanDBHT(Double sumPlanDBHT) {
		this.sumPlanDBHT = sumPlanDBHT;
	}

	public Double getSumPerformDBHT() {
		return sumPerformDBHT;
	}

	public void setSumPerformDBHT(Double sumPerformDBHT) {
		this.sumPerformDBHT = sumPerformDBHT;
	}

	public Double getSumRatioDBHT() {
		return sumRatioDBHT;
	}

	public void setSumRatioDBHT(Double sumRatioDBHT) {
		this.sumRatioDBHT = sumRatioDBHT;
	}

	public Double getSumPlanPS() {
		return sumPlanPS;
	}

	public void setSumPlanPS(Double sumPlanPS) {
		this.sumPlanPS = sumPlanPS;
	}

	public Double getSumPerformPS() {
		return sumPerformPS;
	}

	public void setSumPerformPS(Double sumPerformPS) {
		this.sumPerformPS = sumPerformPS;
	}

	public Double getSumRatioPS() {
		return sumRatioPS;
	}

	public void setSumRatioPS(Double sumRatioPS) {
		this.sumRatioPS = sumRatioPS;
	}

	public Double getSumPlanDT() {
		return sumPlanDT;
	}

	public void setSumPlanDT(Double sumPlanDT) {
		this.sumPlanDT = sumPlanDT;
	}

	public Double getSumPerformDT() {
		return sumPerformDT;
	}

	public void setSumPerformDT(Double sumPerformDT) {
		this.sumPerformDT = sumPerformDT;
	}

	public Double getSumRatioDT() {
		return sumRatioDT;
	}

	public void setSumRatioDT(Double sumRatioDT) {
		this.sumRatioDT = sumRatioDT;
	}

	public Double getSumPlanHSHCTTHT() {
		return sumPlanHSHCTTHT;
	}

	public void setSumPlanHSHCTTHT(Double sumPlanHSHCTTHT) {
		this.sumPlanHSHCTTHT = sumPlanHSHCTTHT;
	}

	public Double getSumPerformHSHCTTHT() {
		return sumPerformHSHCTTHT;
	}

	public void setSumPerformHSHCTTHT(Double sumPerformHSHCTTHT) {
		this.sumPerformHSHCTTHT = sumPerformHSHCTTHT;
	}

	public Double getSumRatioHSHCTTHT() {
		return sumRatioHSHCTTHT;
	}

	public void setSumRatioHSHCTTHT(Double sumRatioHSHCTTHT) {
		this.sumRatioHSHCTTHT = sumRatioHSHCTTHT;
	}

	public Double getSumPlanHSHCDTHT() {
		return sumPlanHSHCDTHT;
	}

	public void setSumPlanHSHCDTHT(Double sumPlanHSHCDTHT) {
		this.sumPlanHSHCDTHT = sumPlanHSHCDTHT;
	}

	public Double getSumPerformHSHCDTHT() {
		return sumPerformHSHCDTHT;
	}

	public void setSumPerformHSHCDTHT(Double sumPerformHSHCDTHT) {
		this.sumPerformHSHCDTHT = sumPerformHSHCDTHT;
	}

	public Double getSumRatioHSHCDTHT() {
		return sumRatioHSHCDTHT;
	}

	public void setSumRatioHSHCDTHT(Double sumRatioHSHCDTHT) {
		this.sumRatioHSHCDTHT = sumRatioHSHCDTHT;
	}

	public Double getSumOverdueKPIRentMB() {
		return sumOverdueKPIRentMB;
	}

	public void setSumOverdueKPIRentMB(Double sumOverdueKPIRentMB) {
		this.sumOverdueKPIRentMB = sumOverdueKPIRentMB;
	}

	public Double getSumOverdueKPIKC() {
		return sumOverdueKPIKC;
	}

	public void setSumOverdueKPIKC(Double sumOverdueKPIKC) {
		this.sumOverdueKPIKC = sumOverdueKPIKC;
	}

	public Double getSumOverdueKPILDT() {
		return sumOverdueKPILDT;
	}

	public void setSumOverdueKPILDT(Double sumOverdueKPILDT) {
		this.sumOverdueKPILDT = sumOverdueKPILDT;
	}

	public Double getSumOverdueKPIHSHCTTHT() {
		return sumOverdueKPIHSHCTTHT;
	}

	public void setSumOverdueKPIHSHCTTHT(Double sumOverdueKPIHSHCTTHT) {
		this.sumOverdueKPIHSHCTTHT = sumOverdueKPIHSHCTTHT;
	}

	public Double getSumOverdueKPIHSHCDTHT() {
		return sumOverdueKPIHSHCDTHT;
	}

	public void setSumOverdueKPIHSHCDTHT(Double sumOverdueKPIHSHCDTHT) {
		this.sumOverdueKPIHSHCDTHT = sumOverdueKPIHSHCDTHT;
	}

	public Double getSumOverdueTKDT() {
		return sumOverdueTKDT;
	}

	public void setSumOverdueTKDT(Double sumOverdueTKDT) {
		this.sumOverdueTKDT = sumOverdueTKDT;
	}

	public Double getSumRentMB() {
		return sumRentMB;
	}

	public void setSumRentMB(Double sumRentMB) {
		this.sumRentMB = sumRentMB;
	}

	public Double getSumStartUp() {
		return sumStartUp;
	}

	public void setSumStartUp(Double sumStartUp) {
		this.sumStartUp = sumStartUp;
	}

	public Double getSumDraftingRevenue() {
		return sumDraftingRevenue;
	}

	public void setSumDraftingRevenue(Double sumDraftingRevenue) {
		this.sumDraftingRevenue = sumDraftingRevenue;
	}

	public Double getSumHshcTTHT() {
		return sumHshcTTHT;
	}

	public void setSumHshcTTHT(Double sumHshcTTHT) {
		this.sumHshcTTHT = sumHshcTTHT;
	}

	public Double getOverdueKPIDBHT() {
		return overdueKPIDBHT;
	}

	public void setOverdueKPIDBHT(Double overdueKPIDBHT) {
		this.overdueKPIDBHT = overdueKPIDBHT;
	}

	public Double getRentDBHT() {
		return rentDBHT;
	}

	public void setRentDBHT(Double rentDBHT) {
		this.rentDBHT = rentDBHT;
	}

	public Double getSumOverdueKPIDBHT() {
		return sumOverdueKPIDBHT;
	}

	public void setSumOverdueKPIDBHT(Double sumOverdueKPIDBHT) {
		this.sumOverdueKPIDBHT = sumOverdueKPIDBHT;
	}

	public Double getSumRentDBHT() {
		return sumRentDBHT;
	}

	public void setSumRentDBHT(Double sumRentDBHT) {
		this.sumRentDBHT = sumRentDBHT;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getApproveDate() {
		return approveDate;
	}

	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}

	
	public String getTypeBc() {
		return typeBc;
	}

	public void setTypeBc(String typeBc) {
		this.typeBc = typeBc;
	}

	
	public String getStationVccCode() {
		return stationVccCode;
	}

	public void setStationVccCode(String stationVccCode) {
		this.stationVccCode = stationVccCode;
	}

	public String getStationVtNetCode() {
		return stationVtNetCode;
	}

	public void setStationVtNetCode(String stationVtNetCode) {
		this.stationVtNetCode = stationVtNetCode;
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

}
