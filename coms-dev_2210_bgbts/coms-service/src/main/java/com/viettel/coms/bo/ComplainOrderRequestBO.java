package com.viettel.coms.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.ComplainOrderRequestDTO;
import com.viettel.coms.dto.TangentCustomerDTO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

@Entity
@Table(name = "COMPLAIN_ORDER_REQUEST")
public class ComplainOrderRequestBO extends BaseFWModelImpl implements Serializable{
	@Id
//	@GeneratedValue(generator = "sequence")
//	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
//			@Parameter(name = "sequence", value = "COMPLAIN_ORDER_REQUEST_SEQ") })
	@Column(name = "COMPLAIN_ORDER_REQUEST_ID", length = 10)
	private Long complainOrderRequestId;
	@Column(name = "TICKET_CODE", length = 100)
	private String ticketCode;
	@Column(name = "PROVINCE_ID", length = 10)
	private Long provinceId;
	@Column(name = "PROVINCE_CODE", length = 100)
	private String provinceCode;
	@Column(name = "PROVINCE_NAME", length = 100)
	private String provinceName;
	@Column(name = "DISTRICT_NAME", length = 100)
	private String districtName;
	@Column(name = "WARDS_NAME", length = 100)
	private String wardsName;
	@Column(name = "CUSTOMER_NAME", length = 500)
	private String customerName;
	@Column(name = "CUSTOMER_ADDRESS", length = 1000)
	private String customerAddress;
	
	@Column(name = "CUSTOMER_PHONE", length = 50)
	private String customerPhone;
	
	@Column(name = "STATUS", length = 1)
	private Long status;
	
	@Column(name = "IS_NEXT", length = 1)
	private Long isNext;
	
	@Column(name = "CREATE_USER", length = 255)
	private String createUser;
	@Column(name = "CREATE_DATE", length = 22)
	private Date createDate;
	@Column(name = "UPDATE_USER", length = 10)
	private String updateUser;
	@Column(name = "UPDATE_DATE", length = 22)
	private Date updateDate;
	
	@Column(name = "RECEIVED_DATE", length = 22)
	private Date receivedDate;
	@Column(name = "COMPLETE_TIME_EXPECTED", length = 22)
	private Date completedTimeExpected;
	@Column(name = "COMPLETE_TIME_REAL", length = 22)
	private Date completedTimeReal;
	
	@Column(name = "PERFORM_NAME", length = 255)
	private String performerName;
	@Column(name = "SERVICE", length = 255)
	private String service;
	@Column(name = "TITLE", length = 1000)
	private String title;
	@Column(name = "COMPLAIN_GROUP", length = 100)
	private String complainGroup;
	@Column(name = "ACTION_LAST", length = 50)
	private String actionLast;
	@Column(name = "IS_TRACE", length = 1)
	private Long isTrace;
	
	public Long getComplainOrderRequestId() {
		return complainOrderRequestId;
	}
	public void setComplainOrderRequestId(Long complainOrderRequestId) {
		this.complainOrderRequestId = complainOrderRequestId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerAddress() {
		return customerAddress;
	}
	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}
	public String getCustomerPhone() {
		return customerPhone;
	}
	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}
	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Date getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}
	public Date getCompletedTimeExpected() {
		return completedTimeExpected;
	}
	public void setCompletedTimeExpected(Date completedTimeExpected) {
		this.completedTimeExpected = completedTimeExpected;
	}
	public Date getCompletedTimeReal() {
		return completedTimeReal;
	}
	public void setCompletedTimeReal(Date completedTimeReal) {
		this.completedTimeReal = completedTimeReal;
	}
	public String getPerformerName() {
		return performerName;
	}
	public void setPerformerName(String performerName) {
		this.performerName = performerName;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getComplainGroup() {
		return complainGroup;
	}
	public void setComplainGroup(String complainGroup) {
		this.complainGroup = complainGroup;
	}
	public Long getIsNext() {
		return isNext;
	}
	public void setIsNext(Long isNext) {
		this.isNext = isNext;
	}
	
	public String getTicketCode() {
		return ticketCode;
	}
	public void setTicketCode(String ticketCode) {
		this.ticketCode = ticketCode;
	}
	
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
	
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public String getWardsName() {
		return wardsName;
	}
	public void setWardsName(String wardsName) {
		this.wardsName = wardsName;
	}
	
	public String getActionLast() {
		return actionLast;
	}
	public void setActionLast(String actionLast) {
		this.actionLast = actionLast;
	}
	
	public Long getIsTrace() {
		return isTrace;
	}
	public void setIsTrace(Long isTrace) {
		this.isTrace = isTrace;
	}
	@Override
	public BaseFWDTOImpl toDTO() {
		ComplainOrderRequestDTO dto = new ComplainOrderRequestDTO();
		dto.setComplainOrderRequestId(this.complainOrderRequestId);
		dto.setTicketCode(this.ticketCode);
		dto.setProvinceId(this.provinceId);
		dto.setProvinceCode(this.provinceCode);
		dto.setProvinceName(this.provinceName);
		dto.setDistrictName(this.districtName);
		dto.setWardsName(this.wardsName);
		dto.setCustomerName(this.customerName);
		dto.setCustomerPhone(this.customerPhone);
		dto.setCustomerAddress(this.customerAddress);
		dto.setCreateDate(this.createDate);
		dto.setCreateUser(this.createUser);
		dto.setReceivedDate(this.receivedDate);
		dto.setUpdateUser(this.updateUser);
		dto.setUpdateDate(this.updateDate);
		dto.setCompletedTimeExpected(this.completedTimeExpected);
		dto.setCompletedTimeReal(this.completedTimeReal);
		dto.setService(this.service);
		dto.setTitle(this.title);
		dto.setPerformerName(this.performerName);
		dto.setStatus(this.status);
		dto.setComplainGroup(this.complainGroup);
		dto.setActionLast(this.actionLast);
		dto.setIsTrace(this.isTrace);
		return dto;
	}
}
