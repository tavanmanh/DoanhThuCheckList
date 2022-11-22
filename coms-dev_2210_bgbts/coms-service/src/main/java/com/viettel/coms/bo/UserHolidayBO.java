package com.viettel.coms.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;

import com.viettel.coms.dto.UserHolidayDTO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;
import com.viettel.service.base.model.BaseFWModelImpl;

@Entity
@Table(name = "USER_HOLIDAY")
public class UserHolidayBO extends BaseFWModelImpl {

	private Long userHolidayId;
	private Long sysUserId;
	private String staffCode;
	private String reason;
	private String address;
	private String startHour;
	private String endHour;
	private java.util.Date startDate;
	private java.util.Date endDate;
	private String numberHoliday;
	private String month;
	private String status;
	private String reasonReject;
	private java.util.Date createdDate;
	private String typeRegister;
	private String typeHour;

	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = { @org.hibernate.annotations.Parameter(name = "sequence", value = "USER_HOLIDAY_SEQ") })
	@Column(name = "USER_HOLIDAY_ID", length = 11)
	public Long getUserHolidayId() {
		return userHolidayId;
	}

	public void setUserHolidayId(Long userHolidayId) {
		this.userHolidayId = userHolidayId;
	}

	@Column(name = "SYS_USER_ID", length = 12)
	public Long getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(Long sysUserId) {
		this.sysUserId = sysUserId;
	}

	@Column(name = "STAFFCODE", length = 12)
	public String getStaffCode() {
		return staffCode;
	}

	public void setStaffCode(String staffCode) {
		this.staffCode = staffCode;
	}

	@Column(name = "REASON", length = 3000)
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Column(name = "ADDRESS", length = 3000)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "STARTHOUR", length = 12)
	public String getStartHour() {
		return startHour;
	}

	public void setStartHour(String startHour) {
		this.startHour = startHour;
	}

	@Column(name = "ENDHOUR", length = 12)
	public String getEndHour() {
		return endHour;
	}

	public void setEndHour(String endHour) {
		this.endHour = endHour;
	}

	@Column(name = "STARTDATE", length = 20)
	public java.util.Date getStartDate() {
		return startDate;
	}

	public void setStartDate(java.util.Date startDate) {
		this.startDate = startDate;
	}

	@Column(name = "ENDDATE", length = 20)
	public java.util.Date getEndDate() {
		return endDate;
	}

	public void setEndDate(java.util.Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "NUMBERHOLIDAY", length = 10)
	public String getNumberHoliday() {
		return numberHoliday;
	}

	public void setNumberHoliday(String numberHoliday) {
		this.numberHoliday = numberHoliday;
	}

	@Column(name = "MONTH", length = 10)
	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	@Column(name = "STATUS", length = 10)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "REASONREJECT", length = 3000)
	public String getReasonReject() {
		return reasonReject;
	}

	public void setReasonReject(String reasonReject) {
		this.reasonReject = reasonReject;
	}

	@Column(name = "CREATED_DATE", length = 11)
	public java.util.Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(java.util.Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "TYPE_REGISTER", length = 12)
	public String getTypeRegister() {
		return typeRegister;
	}

	public void setTypeRegister(String typeRegister) {
		this.typeRegister = typeRegister;
	}

	@Column(name = "TYPE_HOUR", length = 12)
	public String getTypeHour() {
		return typeHour;
	}

	public void setTypeHour(String typeHour) {
		this.typeHour = typeHour;
	}

	@Override
	public UserHolidayDTO toDTO() {
		UserHolidayDTO dto = new UserHolidayDTO();
		dto.setUserHolidayId(this.getUserHolidayId());
		dto.setSysUserId(this.getSysUserId());
		dto.setStaffCode(this.getStaffCode());
		dto.setReason(this.getReason());
		dto.setAddress(this.getAddress());
		dto.setStartHour(this.getStartHour());
		dto.setEndHour(this.getEndHour());
		dto.setStartDate(this.getStartDate());
		dto.setEndDate(this.getEndDate());
		dto.setNumberHoliday(this.getNumberHoliday());
		dto.setMonth(this.getMonth());
		dto.setStatus(this.getStatus());
		dto.setReasonReject(this.getReasonReject());
		dto.setCreatedDate(this.getCreatedDate());
		dto.setTypeRegister(this.getTypeRegister());
		dto.setTypeHour(this.getTypeHour());
		return dto;
	}
}
