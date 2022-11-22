package com.viettel.coms.dto;

import java.util.Date;

//@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
public class ApiListWOInfoVhkt {
	private String alarmID;
	private String ftCode;
	private String ftName;
	private String woCode;
	private String woName;
	private String state;
	private String stateName;
	private String trCode;
	private String trName;
	private String trStart;
	private String trEnd;
	private String woStart;
	private String woEnd;
	private String trState;
	private String trStateName;
	private String systemCode;
	private Long numUnfinishWO;
	private String woStartStr;
	private String woEndStr;
	private String woCreateStr;
	private String woDeadlineStr;
	private String systemOriginalCode;

	public String getSystemOriginalCode() {
		return systemOriginalCode;
	}

	public void setSystemOriginalCode(String systemOriginalCode) {
		this.systemOriginalCode = systemOriginalCode;
	}

	public String getWoCreateStr() {
		return woCreateStr;
	}

	public void setWoCreateStr(String woCreateStr) {
		this.woCreateStr = woCreateStr;
	}

	public String getWoDeadlineStr() {
		return woDeadlineStr;
	}

	public void setWoDeadlineStr(String woDeadlineStr) {
		this.woDeadlineStr = woDeadlineStr;
	}

	public String getWoStartStr() {
		return woStartStr;
	}

	public void setWoStartStr(String woStartStr) {
		this.woStartStr = woStartStr;
	}

	public String getWoEndStr() {
		return woEndStr;
	}

	public void setWoEndStr(String woEndStr) {
		this.woEndStr = woEndStr;
	}

	public Long getNumUnfinishWO() {
		return numUnfinishWO;
	}

	public void setNumUnfinishWO(Long numUnfinishWO) {
		this.numUnfinishWO = numUnfinishWO;
	}

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public String getTrStateName() {
		return trStateName;
	}

	public void setTrStateName(String trStateName) {
		this.trStateName = trStateName;
	}

	public String getTrCode() {
		return trCode;
	}

	public void setTrCode(String trCode) {
		this.trCode = trCode;
	}

	public String getTrName() {
		return trName;
	}

	public void setTrName(String trName) {
		this.trName = trName;
	}

	public String getTrStart() {
		return trStart;
	}

	public void setTrStart(String trStart) {
		this.trStart = trStart;
	}

	public String getTrEnd() {
		return trEnd;
	}

	public void setTrEnd(String trEnd) {
		this.trEnd = trEnd;
	}

	public String getWoStart() {
		return woStart;
	}

	public void setWoStart(String woStart) {
		this.woStart = woStart;
	}

	public String getWoEnd() {
		return woEnd;
	}

	public void setWoEnd(String woEnd) {
		this.woEnd = woEnd;
	}

	public String getTrState() {
		return trState;
	}

	public void setTrState(String trState) {
		this.trState = trState;
	}

	public String getAlarmID() {
		return alarmID;
	}

	public void setAlarmID(String alarmID) {
		this.alarmID = alarmID;
	}

	public String getFtCode() {
		return ftCode;
	}

	public void setFtCode(String ftCode) {
		this.ftCode = ftCode;
	}

	public String getFtName() {
		return ftName;
	}

	public void setFtName(String ftName) {
		this.ftName = ftName;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
}
