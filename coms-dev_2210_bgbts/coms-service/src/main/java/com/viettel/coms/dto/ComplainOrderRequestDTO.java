package com.viettel.coms.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.spi.DirStateFactory.Result;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.asset.dto.ResultInfo;
import com.viettel.coms.bo.ComplainOrderRequestBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;
import com.viettel.service.base.dto.DataListDTO;

@XmlRootElement(name = "COMPLAIN_ORDER_REQUEST_BO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ComplainOrderRequestDTO extends ComsBaseFWDTO<ComplainOrderRequestBO> implements Serializable{
	

	private Long complainOrderRequestId;
	private String customerName;
	private String customerPhone;
	private String customerAddress;
	
	private Long provinceId;
	private String provinceCode;
	private String provinceName;
	
	private String districtName;
	private String wardsName;
	
	private List<String> provinceViewData;
	private List<String> provinceViewDeploy;
	
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date createDate;
	private String createUser;
	
	private String createUserName;
	
	private Long isNext;
	
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date receivedDate;
	
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date completedTimeExpected;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date completedTimeReal;

	private Long status;
	
	private String updateUser;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date updateDate;
	
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date createDateFrom;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date createDateTo;
	
	private String performerName;
	private String performerfullName;
	
	private String performerUser;
	
	private Long performerId;
	
	private String service;
	private String title;
	private String complainGroup;
	private String keySearch;
	private String process;
	
	private String createDateString;
	private String receivedDateString;
	private String completedTimeExpectedString;
	private String completedTimeRealString;
	
	private Double realTimeDate;
	
	private Double workTimeDate;
	
	private Double realCompletedTimeDate;
	
	private List<ComplainOrderRequestDetailLogHistoryDTO> listLogForComplainOder;
	
	private String address; private String phone ; private String content ;
	
	private String employeeCode; private Long ticketId; 
	
	private List<Long> listId;
	
	private String reason;
	private String note;
	
	private Long isTrace;
	
	
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date createTime;
	
	private String type;
	private String ticketCode;
	private String business;
	
	private ResultInfo resultInfo;
	
	private Long checkRoleTTHTView;
	private Long checkRoleViewData;
	private Long checkRoleCSKH;
	private Long checkRoleDeployTicket;
	
	private String actionLast;
	
	private Long isFirst;
	private DataListDTO dataListDTO;
	
	public List<AppParamDTO> listPerform;
	
	public Long sysUserId;public Long sysGroupId;
	
	private String performerShow;
	
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
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
	public Long getIsNext() {
		return isNext;
	}
	public void setIsNext(Long isNext) {
		this.isNext = isNext;
	}
	public Date getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}
	@JsonProperty("completedTimeExpected")
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
	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
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
	
	public Date getCreateDateFrom() {
		return createDateFrom;
	}
	public void setCreateDateFrom(Date createDateFrom) {
		this.createDateFrom = createDateFrom;
	}
	public Date getCreateDateTo() {
		return createDateTo;
	}
	public void setCreateDateTo(Date createDateTo) {
		this.createDateTo = createDateTo;
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
	
	public String getKeySearch() {
		return keySearch;
	}
	public void setKeySearch(String keySearch) {
		this.keySearch = keySearch;
	}
	
	public String getProcess() {
		return process;
	}
	public void setProcess(String process) {
		this.process = process;
	}
	
	public String getCreateDateString() {
		return createDateString;
	}
	public void setCreateDateString(String createDateString) {
		this.createDateString = createDateString;
	}
	public String getReceivedDateString() {
		return receivedDateString;
	}
	public void setReceivedDateString(String receivedDateString) {
		this.receivedDateString = receivedDateString;
	}
	public String getCompletedTimeExpectedString() {
		return completedTimeExpectedString;
	}
	public void setCompletedTimeExpectedString(String completedTimeExpectedString) {
		this.completedTimeExpectedString = completedTimeExpectedString;
	}
	public String getCompletedTimeRealString() {
		return completedTimeRealString;
	}
	public void setCompletedTimeRealString(String completedTimeRealString) {
		this.completedTimeRealString = completedTimeRealString;
	}
	
	public Double getRealTimeDate() {
		return realTimeDate;
	}
	public void setRealTimeDate(Double realTimeDate) {
		this.realTimeDate = realTimeDate;
	}
	public Double getWorkTimeDate() {
		return workTimeDate;
	}
	public void setWorkTimeDate(Double workTimeDate) {
		this.workTimeDate = workTimeDate;
	}
	
	public Double getRealCompletedTimeDate() {
		return realCompletedTimeDate;
	}
	public void setRealCompletedTimeDate(Double realCompletedTimeDate) {
		this.realCompletedTimeDate = realCompletedTimeDate;
	}
	
	public List<ComplainOrderRequestDetailLogHistoryDTO> getListLogForComplainOder() {
		return listLogForComplainOder;
	}
	public void setListLogForComplainOder(List<ComplainOrderRequestDetailLogHistoryDTO> listLogForComplainOder) {
		this.listLogForComplainOder = listLogForComplainOder;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public Long getTicketId() {
		return ticketId;
	}
	public void setTicketId(Long ticketId) {
		this.ticketId = ticketId;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	
	public List<Long> getListId() {
		return listId;
	}
	public void setListId(List<Long> listId) {
		this.listId = listId;
	}
	
	public String getTicketCode() {
		return ticketCode;
	}
	public void setTicketCode(String ticketCode) {
		this.ticketCode = ticketCode;
	}

	public String getBusiness() {
		return business;
	}
	public void setBusiness(String business) {
		this.business = business;
	}
	
	public String getPerformerfullName() {
		return performerfullName;
	}
	public void setPerformerfullName(String performerfullName) {
		this.performerfullName = performerfullName;
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
	public ResultInfo getResultInfo() {
		return resultInfo;
	}
	public void setResultInfo(ResultInfo resultInfo) {
		this.resultInfo = resultInfo;
	}
	
	public Long getCheckRoleTTHTView() {
		return checkRoleTTHTView;
	}
	public void setCheckRoleTTHTView(Long checkRoleTTHTView) {
		this.checkRoleTTHTView = checkRoleTTHTView;
	}
	public Long getCheckRoleViewData() {
		return checkRoleViewData;
	}
	public void setCheckRoleViewData(Long checkRoleViewData) {
		this.checkRoleViewData = checkRoleViewData;
	}
	public Long getCheckRoleCSKH() {
		return checkRoleCSKH;
	}
	public void setCheckRoleCSKH(Long checkRoleCSKH) {
		this.checkRoleCSKH = checkRoleCSKH;
	}
	public Long getCheckRoleDeployTicket() {
		return checkRoleDeployTicket;
	}
	public void setCheckRoleDeployTicket(Long checkRoleDeployTicket) {
		this.checkRoleDeployTicket = checkRoleDeployTicket;
	}
	
	public List<String> getProvinceViewData() {
		return provinceViewData;
	}
	public void setProvinceViewData(List<String> provinceViewData) {
		this.provinceViewData = provinceViewData;
	}
	public List<String> getProvinceViewDeploy() {
		return provinceViewDeploy;
	}
	public void setProvinceViewDeploy(List<String> provinceViewDeploy) {
		this.provinceViewDeploy = provinceViewDeploy;
	}
	public Long getIsFirst() {
		return isFirst;
	}
	public void setIsFirst(Long isFirst) {
		this.isFirst = isFirst;
	}
	
	public DataListDTO getDataListDTO() {
		return dataListDTO;
	}

	public void setDataListDTO(DataListDTO dataListDTO) {
		this.dataListDTO = dataListDTO;
	}
	
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	public String getPerformerUser() {
		return performerUser;
	}
	public void setPerformerUser(String performerUser) {
		this.performerUser = performerUser;
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
	
	public List<AppParamDTO> getListPerform() {
		return listPerform;
	}
	public void setListPerform(List<AppParamDTO> listPerform) {
		this.listPerform = listPerform;
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
	
	public Long getSysUserId() {
		return sysUserId;
	}
	public void setSysUserId(Long sysUserId) {
		this.sysUserId = sysUserId;
	}
	public Long getSysGroupId() {
		return sysGroupId;
	}
	public void setSysGroupId(Long sysGroupId) {
		this.sysGroupId = sysGroupId;
	}
	
	public String getPerformerShow() {
		return performerShow;
	}
	public void setPerformerShow(String performerShow) {
		this.performerShow = performerShow;
	}
	
	public Long getPerformerId() {
		return performerId;
	}
	public void setPerformerId(Long performerId) {
		this.performerId = performerId;
	}
	@Override
	public String catchName() {
		// TODO Auto-generated method stub
		return complainOrderRequestId.toString();
	}

	@Override
	public Long getFWModelId() {
		// TODO Auto-generated method stub
		return complainOrderRequestId;
	}

	@Override
	public ComplainOrderRequestBO toModel() {
		ComplainOrderRequestBO bo = new ComplainOrderRequestBO();
		bo.setComplainOrderRequestId(this.complainOrderRequestId);
		bo.setTicketCode(this.ticketCode);
		bo.setProvinceId(this.provinceId);
		bo.setProvinceCode(this.provinceCode);
		bo.setDistrictName(this.districtName);
		bo.setWardsName(this.wardsName);
		bo.setProvinceName(this.provinceName);
		bo.setCustomerName(this.customerName);
		bo.setCustomerPhone(this.customerPhone);
		bo.setCustomerAddress(this.customerAddress);
		bo.setCreateDate(this.createDate);
		bo.setCreateUser(this.createUser);
		bo.setIsNext(this.isNext);
		bo.setReceivedDate(this.receivedDate);
		bo.setUpdateUser(this.updateUser);
		bo.setUpdateDate(this.updateDate);
		bo.setCompletedTimeExpected(this.completedTimeExpected);
		bo.setCompletedTimeReal(this.completedTimeReal);
		bo.setService(this.service);
		bo.setTitle(this.title);
		bo.setPerformerName(this.performerName);
		bo.setStatus(this.status);
		bo.setComplainGroup(this.complainGroup);
		bo.setActionLast(this.actionLast);
		bo.setIsTrace(this.isTrace);
		return bo;
	}
}