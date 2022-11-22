package com.viettel.coms.dto;

public class ApiReportPeriodicMaintanceDTO {

	//Khu vực
	private String areaCode;
	//Chi nhánh kỹ thuật
	private String cnkt;
	//Mã công việc định kỳ
	private String taskCode;
	//Công suất định danh tháng
	private String monthlyNominalCapacity;
	//Ngày báo cáo
	private String reportDate;
	//Ngày bảo dưỡng gần nhất
	private String nearestMaintainDate;
	//Mã WO
	private String woCode;
	//Tình trạng WO
	private String woState;
	//Kết quả wo
	private String woResult;
	//Số lần BD trong năm
	private Long numberMaintainTimePerYear;
	//Số lần bảo dưỡng trong 3 tháng tới
	private Long numberMaintainTimeNextThreeMonth;
	//Số tháng bảo dưỡng trong 3 tháng tới
	private String numberMonthMaintainNextThreeMonth;
	//Lượng chu kỳ
	private Long cycleLength;
	//Loại chu kỳ
	private String cycleType;
	private String cycleTypeName;
	
	public String getSystemCode() {
		return systemCode;
	}
	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}
	private String woName;
	private String systemCode;
	
	
	public String getWoName() {
		return woName;
	}
	public void setWoName(String woName) {
		this.woName = woName;
	}
	public String getCycleTypeName() {
		return cycleTypeName;
	}
	public void setCycleTypeName(String cycleTypeName) {
		this.cycleTypeName = cycleTypeName;
	}
	public Long getCycleLength() {
		return cycleLength;
	}
	public void setCycleLength(Long cycleLength) {
		this.cycleLength = cycleLength;
	}
	public String getCycleType() {
		return cycleType;
	}
	public void setCycleType(String cycleType) {
		this.cycleType = cycleType;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getCnkt() {
		return cnkt;
	}
	public void setCnkt(String cnkt) {
		this.cnkt = cnkt;
	}
	public String getTaskCode() {
		return taskCode;
	}
	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}
	public String getMonthlyNominalCapacity() {
		return monthlyNominalCapacity;
	}
	public void setMonthlyNominalCapacity(String monthlyNominalCapacity) {
		this.monthlyNominalCapacity = monthlyNominalCapacity;
	}
	public String getReportDate() {
		return reportDate;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	public String getWoCode() {
		return woCode;
	}
	public void setWoCode(String woCode) {
		this.woCode = woCode;
	}
	public String getWoState() {
		return woState;
	}
	public void setWoState(String woState) {
		this.woState = woState;
	}
	public String getWoResult() {
		return woResult;
	}
	public void setWoResult(String woResult) {
		this.woResult = woResult;
	}
	public String getNearestMaintainDate() {
		return nearestMaintainDate;
	}
	public void setNearestMaintainDate(String nearestMaintainDate) {
		this.nearestMaintainDate = nearestMaintainDate;
	}
	public Long getNumberMaintainTimePerYear() {
		return numberMaintainTimePerYear;
	}
	public void setNumberMaintainTimePerYear(Long numberMaintainTimePerYear) {
		this.numberMaintainTimePerYear = numberMaintainTimePerYear;
	}
	public Long getNumberMaintainTimeNextThreeMonth() {
		return numberMaintainTimeNextThreeMonth;
	}
	public void setNumberMaintainTimeNextThreeMonth(Long numberMaintainTimeNextThreeMonth) {
		this.numberMaintainTimeNextThreeMonth = numberMaintainTimeNextThreeMonth;
	}
	public String getNumberMonthMaintainNextThreeMonth() {
		return numberMonthMaintainNextThreeMonth;
	}
	public void setNumberMonthMaintainNextThreeMonth(String numberMonthMaintainNextThreeMonth) {
		this.numberMonthMaintainNextThreeMonth = numberMonthMaintainNextThreeMonth;
	}
	
	
}
