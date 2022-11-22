package com.viettel.coms.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.xml.bind.annotation.XmlRootElement;

import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.viettel.cat.dto.ConstructionImageInfo;
import com.viettel.coms.bo.UserHolidayBO;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@XmlRootElement(name = "USER_HOLIDAYBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserHolidayDTO extends ComsBaseFWDTO<UserHolidayBO> {

	private Long userHolidayId;
	private Long sysUserId;
	private String staffCode;
	private String reason;
	private String address;
	private String startHour;
	private String endHour;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private java.util.Date startDate;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private java.util.Date endDate;
	private String numberHoliday;
	private String month;
	private String status;
	private String type;
	private String fullName;
	private String email;
	private String numberPhone;
	private String sysGroupName;
	private String reasonReject;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private java.util.Date createdDate;
	private String totalHolidayMonth;
	private String totalHolidayYear;
	private String version;
	private String link;
	private String typeRegister;
	private String typeHour;
	private String totalHolidayRegisterMonth;
	private String totalHolidayRegisterYear;
	
	public String getTotalHolidayRegisterMonth() {
		return totalHolidayRegisterMonth;
	}

	public void setTotalHolidayRegisterMonth(String totalHolidayRegisterMonth) {
		this.totalHolidayRegisterMonth = totalHolidayRegisterMonth;
	}

	public String getTotalHolidayRegisterYear() {
		return totalHolidayRegisterYear;
	}

	public void setTotalHolidayRegisterYear(String totalHolidayRegisterYear) {
		this.totalHolidayRegisterYear = totalHolidayRegisterYear;
	}

	public String getTypeRegister() {
		return typeRegister;
	}

	public void setTypeRegister(String typeRegister) {
		this.typeRegister = typeRegister;
	}

	public String getTypeHour() {
		return typeHour;
	}

	public void setTypeHour(String typeHour) {
		this.typeHour = typeHour;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getTotalHolidayMonth() {
		return totalHolidayMonth;
	}

	public void setTotalHolidayMonth(String totalHolidayMonth) {
		this.totalHolidayMonth = totalHolidayMonth;
	}

	public String getTotalHolidayYear() {
		return totalHolidayYear;
	}

	public void setTotalHolidayYear(String totalHolidayYear) {
		this.totalHolidayYear = totalHolidayYear;
	}

	public java.util.Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(java.util.Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getReasonReject() {
		return reasonReject;
	}

	public void setReasonReject(String reasonReject) {
		this.reasonReject = reasonReject;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNumberPhone() {
		return numberPhone;
	}

	public void setNumberPhone(String numberPhone) {
		this.numberPhone = numberPhone;
	}

	public String getSysGroupName() {
		return sysGroupName;
	}

	public void setSysGroupName(String sysGroupName) {
		this.sysGroupName = sysGroupName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getUserHolidayId() {
		return userHolidayId;
	}

	public void setUserHolidayId(Long userHolidayId) {
		this.userHolidayId = userHolidayId;
	}

	public Long getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(Long sysUserId) {
		this.sysUserId = sysUserId;
	}

	public String getStaffCode() {
		return staffCode;
	}

	public void setStaffCode(String staffCode) {
		this.staffCode = staffCode;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStartHour() {
		return startHour;
	}

	public void setStartHour(String startHour) {
		this.startHour = startHour;
	}

	public String getEndHour() {
		return endHour;
	}

	public void setEndHour(String endHour) {
		this.endHour = endHour;
	}

	public java.util.Date getStartDate() {
		return startDate;
	}

	public void setStartDate(java.util.Date startDate) {
		this.startDate = startDate;
	}

	public java.util.Date getEndDate() {
		return endDate;
	}

	public void setEndDate(java.util.Date endDate) {
		this.endDate = endDate;
	}

	public String getNumberHoliday() {
		return numberHoliday;
	}

	public void setNumberHoliday(String numberHoliday) {
		this.numberHoliday = numberHoliday;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public Long getFWModelId() {
		return this.getUserHolidayId();
	}

	@Override
	public String catchName() {
		return this.getUserHolidayId().toString();
	}

	@Override
	public UserHolidayBO toModel() {
		UserHolidayBO bo = new UserHolidayBO();
		bo.setUserHolidayId(this.getUserHolidayId());
		bo.setSysUserId(this.getSysUserId());
		bo.setStaffCode(this.getStaffCode());
		bo.setReason(this.getReason());
		bo.setAddress(this.getAddress());
		bo.setStartHour(this.getStartHour());
		bo.setEndHour(this.getEndHour());
		bo.setStartDate(this.getStartDate());
		bo.setEndDate(this.getEndDate());
		bo.setNumberHoliday(this.getNumberHoliday());
		bo.setMonth(this.getMonth());
		bo.setStatus(this.getStatus());
		bo.setReasonReject(this.getReasonReject());
		bo.setCreatedDate(this.getCreatedDate());
		bo.setTypeRegister(this.getTypeRegister());
		bo.setTypeHour(this.getTypeHour());
		return bo;
	}

}
