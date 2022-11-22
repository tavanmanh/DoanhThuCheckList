package com.viettel.coms.dto;

import java.util.List;

import com.viettel.coms.bo.SignVofficeBO;
import com.viettel.wms.dto.SignVofficeDetailDTO;

public class SignVofficeRequestDTO {

	private java.lang.Long signVofficeId;
    private java.lang.Long sysUserId;
    private java.lang.Long stockId;
    private java.lang.String bussTypeId;
    private java.lang.Long oder;
    private java.lang.String oderName;
    private java.lang.String status;
    private java.lang.Long objectId;
    private java.lang.String objectCode;
    private java.lang.Long adOrgId;
    private java.lang.String errorCode;
    private java.util.Date createdDate;
    private java.lang.String transCode;
    private String vofficePass;
    private Long createdBy;
    private String email;
    private String signState;
    private Double totalPrice;
    private String signOrder;
    private String signLabelName;
    private List<SignVofficeDTO> listSignVoffice;
    private List<SignVofficeDetailDTO> listSignVofficeDetail;
    
	public java.lang.Long getSignVofficeId() {
		return signVofficeId;
	}
	public void setSignVofficeId(java.lang.Long signVofficeId) {
		this.signVofficeId = signVofficeId;
	}
	public java.lang.Long getSysUserId() {
		return sysUserId;
	}
	public void setSysUserId(java.lang.Long sysUserId) {
		this.sysUserId = sysUserId;
	}
	public java.lang.Long getStockId() {
		return stockId;
	}
	public void setStockId(java.lang.Long stockId) {
		this.stockId = stockId;
	}
	public java.lang.String getBussTypeId() {
		return bussTypeId;
	}
	public void setBussTypeId(java.lang.String bussTypeId) {
		this.bussTypeId = bussTypeId;
	}
	public java.lang.Long getOder() {
		return oder;
	}
	public void setOder(java.lang.Long oder) {
		this.oder = oder;
	}
	public java.lang.String getOderName() {
		return oderName;
	}
	public void setOderName(java.lang.String oderName) {
		this.oderName = oderName;
	}
	public java.lang.String getStatus() {
		return status;
	}
	public void setStatus(java.lang.String status) {
		this.status = status;
	}
	public java.lang.Long getObjectId() {
		return objectId;
	}
	public void setObjectId(java.lang.Long objectId) {
		this.objectId = objectId;
	}
	public java.lang.String getObjectCode() {
		return objectCode;
	}
	public void setObjectCode(java.lang.String objectCode) {
		this.objectCode = objectCode;
	}
	public java.lang.Long getAdOrgId() {
		return adOrgId;
	}
	public void setAdOrgId(java.lang.Long adOrgId) {
		this.adOrgId = adOrgId;
	}
	public java.lang.String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(java.lang.String errorCode) {
		this.errorCode = errorCode;
	}
	public java.util.Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(java.util.Date createdDate) {
		this.createdDate = createdDate;
	}
	public java.lang.String getTransCode() {
		return transCode;
	}
	public void setTransCode(java.lang.String transCode) {
		this.transCode = transCode;
	}
	public String getVofficePass() {
		return vofficePass;
	}
	public void setVofficePass(String vofficePass) {
		this.vofficePass = vofficePass;
	}
	public Long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSignState() {
		return signState;
	}
	public void setSignState(String signState) {
		this.signState = signState;
	}
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getSignOrder() {
		return signOrder;
	}
	public void setSignOrder(String signOrder) {
		this.signOrder = signOrder;
	}
	public String getSignLabelName() {
		return signLabelName;
	}
	public void setSignLabelName(String signLabelName) {
		this.signLabelName = signLabelName;
	}
	public List<SignVofficeDTO> getListSignVoffice() {
		return listSignVoffice;
	}
	public void setListSignVoffice(List<SignVofficeDTO> listSignVoffice) {
		this.listSignVoffice = listSignVoffice;
	}
	public List<SignVofficeDetailDTO> getListSignVofficeDetail() {
		return listSignVofficeDetail;
	}
	public void setListSignVofficeDetail(List<SignVofficeDetailDTO> listSignVofficeDetail) {
		this.listSignVofficeDetail = listSignVofficeDetail;
	}
    
	public SignVofficeBO toModel() {
        SignVofficeBO signVofficeBO = new SignVofficeBO();
        signVofficeBO.setSignVofficeId(this.signVofficeId);
        signVofficeBO.setSysUserId(this.sysUserId);
        signVofficeBO.setStockId(this.stockId);
        signVofficeBO.setBussTypeId(this.bussTypeId);
        signVofficeBO.setOder(this.oder);
        signVofficeBO.setOderName(this.oderName);
        signVofficeBO.setStatus(this.status);
        signVofficeBO.setObjectId(this.objectId);
        signVofficeBO.setErrorCode(this.errorCode);
        signVofficeBO.setCreatedDate(this.createdDate);
        signVofficeBO.setTransCode(this.transCode);
        return signVofficeBO;
    }
    
}
