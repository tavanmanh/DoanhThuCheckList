package com.viettel.coms.dto.avg;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.utils.JsonDateDeserializer;
import com.viettel.utils.JsonDateSerializerDate;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
public class WoAvgVhktInputDTO {
	private String checkSum;
    private String orderCodeTgdd;
    private String orderCodeAvg;
    private String customerName;
    private String phoneNumber;
    private String personalId;
    private String address;
    private String productCode;
    private String paymentStatus;
    private String servicePackage;
    private String completeDateFrom;
    private String completeDateTo;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date finishDate;
    private String userCreated;
    private Long woId;
    private Double moneyValue;
    
	public String getCheckSum() {
		return checkSum;
	}
	public void setCheckSum(String checkSum) {
		this.checkSum = checkSum;
	}
	public String getOrderCodeTgdd() {
		return orderCodeTgdd;
	}
	public void setOrderCodeTgdd(String orderCodeTgdd) {
		this.orderCodeTgdd = orderCodeTgdd;
	}
	public String getOrderCodeAvg() {
		return orderCodeAvg;
	}
	public void setOrderCodeAvg(String orderCodeAvg) {
		this.orderCodeAvg = orderCodeAvg;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getPersonalId() {
		return personalId;
	}
	public void setPersonalId(String personalId) {
		this.personalId = personalId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getServicePackage() {
		return servicePackage;
	}
	public void setServicePackage(String servicePackage) {
		this.servicePackage = servicePackage;
	}
	public String getCompleteDateFrom() {
		return completeDateFrom;
	}
	public void setCompleteDateFrom(String completeDateFrom) {
		this.completeDateFrom = completeDateFrom;
	}
	public String getCompleteDateTo() {
		return completeDateTo;
	}
	public void setCompleteDateTo(String completeDateTo) {
		this.completeDateTo = completeDateTo;
	}
	public Date getFinishDate() {
		return finishDate;
	}
	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}
	public String getUserCreated() {
		return userCreated;
	}
	public void setUserCreated(String userCreated) {
		this.userCreated = userCreated;
	}
	public Long getWoId() {
		return woId;
	}
	public void setWoId(Long woId) {
		this.woId = woId;
	}
	public Double getMoneyValue() {
		return moneyValue;
	}
	public void setMoneyValue(Double moneyValue) {
		this.moneyValue = moneyValue;
	}
	
}
