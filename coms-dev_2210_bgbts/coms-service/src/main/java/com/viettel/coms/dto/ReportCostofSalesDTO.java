package com.viettel.coms.dto;

import java.util.Date;

import com.viettel.service.base.model.BaseFWModelImpl;

public class ReportCostofSalesDTO extends ComsBaseFWDTO<BaseFWModelImpl>{
	private String area;
	private String provinceCode;
	private String contendContract;
	private String cDT;
	private String contractNumber;
	private String filter;
	private String recordedInMonth;
	private String employeeCode;
	private String employeeName;
	private String tilte;
	private Double prirceContract;
	private Double prirceContractCount;
	private Long dayNumber;
	private Long type;
	private Double costOfSales;
	private Double costOfSalesCount;
	private Date signDate;
	private Date startDate;
	private Date endDate;
	private String monthYear;
	private Double branchFund;
	private Double totalMoney;
	private Double giamdoc;
	private Double pgdkythuat;
	private Double pgdhatang;
	private Double pgdkinhdoanh;
	private Double phongkythuat;
	private Double phonghatang;
	private Double phongkinhdoanh;
	private Double khoihotro;
	private Double gdttqh;
	private Double employee;
	private String year;
	private Long userSearchId;
	
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public String getContendContract() {
		return contendContract;
	}
	public void setContendContract(String contendContract) {
		this.contendContract = contendContract;
	}
	public String getcDT() {
		return cDT;
	}
	public void setcDT(String cDT) {
		this.cDT = cDT;
	}
	public String getContractNumber() {
		return contractNumber;
	}
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	public String getRecordedInMonth() {
		return recordedInMonth;
	}
	public void setRecordedInMonth(String recordedInMonth) {
		this.recordedInMonth = recordedInMonth;
	}
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getTilte() {
		return tilte;
	}
	public void setTilte(String tilte) {
		this.tilte = tilte;
	}
	
	public Double getCostOfSales() {
		return costOfSales;
	}
	public void setCostOfSales(Double costOfSales) {
		this.costOfSales = costOfSales;
	}
	public Date getSignDate() {
		return signDate;
	}
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
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
	public Long getDayNumber() {
		return dayNumber;
	}
	public void setDayNumber(Long dayNumber) {
		this.dayNumber = dayNumber;
	}
	public Long getType() {
		return type;
	}
	public void setType(Long type) {
		this.type = type;
	}
	public String getMonthYear() {
		return monthYear;
	}
	public void setMonthYear(String monthYear) {
		this.monthYear = monthYear;
	}
	
	public Double getPrirceContract() {
		return prirceContract;
	}
	public void setPrirceContract(Double prirceContract) {
		this.prirceContract = prirceContract;
	}
	public Double getPrirceContractCount() {
		return prirceContractCount;
	}
	public void setPrirceContractCount(Double prirceContractCount) {
		this.prirceContractCount = prirceContractCount;
	}
	public Double getCostOfSalesCount() {
		return costOfSalesCount;
	}
	public void setCostOfSalesCount(Double costOfSalesCount) {
		this.costOfSalesCount = costOfSalesCount;
	}
	
	public Double getBranchFund() {
		return branchFund;
	}
	public void setBranchFund(Double branchFund) {
		this.branchFund = branchFund;
	}
	public Double getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
	}
	public Double getGiamdoc() {
		return giamdoc;
	}
	public void setGiamdoc(Double giamdoc) {
		this.giamdoc = giamdoc;
	}
	public Double getPgdkythuat() {
		return pgdkythuat;
	}
	public void setPgdkythuat(Double pgdkythuat) {
		this.pgdkythuat = pgdkythuat;
	}
	public Double getPgdhatang() {
		return pgdhatang;
	}
	public void setPgdhatang(Double pgdhatang) {
		this.pgdhatang = pgdhatang;
	}
	public Double getPgdkinhdoanh() {
		return pgdkinhdoanh;
	}
	public void setPgdkinhdoanh(Double pgdkinhdoanh) {
		this.pgdkinhdoanh = pgdkinhdoanh;
	}
	public Double getPhongkythuat() {
		return phongkythuat;
	}
	public void setPhongkythuat(Double phongkythuat) {
		this.phongkythuat = phongkythuat;
	}
	public Double getPhonghatang() {
		return phonghatang;
	}
	public void setPhonghatang(Double phonghatang) {
		this.phonghatang = phonghatang;
	}
	public Double getPhongkinhdoanh() {
		return phongkinhdoanh;
	}
	public void setPhongkinhdoanh(Double phongkinhdoanh) {
		this.phongkinhdoanh = phongkinhdoanh;
	}
	public Double getKhoihotro() {
		return khoihotro;
	}
	public void setKhoihotro(Double khoihotro) {
		this.khoihotro = khoihotro;
	}
	public Double getGdttqh() {
		return gdttqh;
	}
	public void setGdttqh(Double gdttqh) {
		this.gdttqh = gdttqh;
	}
	
	public Double getEmployee() {
		return employee;
	}
	public void setEmployee(Double employee) {
		this.employee = employee;
	}
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	
	public Long getUserSearchId() {
		return userSearchId;
	}
	public void setUserSearchId(Long userSearchId) {
		this.userSearchId = userSearchId;
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
	

}
