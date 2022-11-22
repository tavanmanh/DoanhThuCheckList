package com.viettel.coms.dto;

import java.util.List;

import javax.persistence.Column;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.RevokeCashMonthPlanBO;
import com.viettel.erp.constant.ApplicationConstants;
import com.viettel.erp.utils.CustomJsonDateDeserializer;
import com.viettel.erp.utils.CustomJsonDateSerializer;
import com.viettel.service.base.dto.BaseFWDTOImpl;

/**
 *
 * @author hailh10
 */
@SuppressWarnings("serial")
@XmlRootElement(name = "REVOKE_CASH_MONTH_PLANBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RevokeCashMonthPlanDTO extends ComsBaseFWDTO<RevokeCashMonthPlanBO> {

	private java.lang.Long revokeCashMonthPlanId;
	private java.lang.String cntContractCode;
	private java.lang.String billCode;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date createdBillDate;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date createdBillDateFrom;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date createdBillDateTo;
	private java.lang.Double billValue;
	private java.lang.String areaCode;
	private java.lang.String provinceCode;
	private java.lang.Long performerId;
	private java.lang.String performerName;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date startDate;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date startDateFrom;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date startDateTo;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date endDate;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date endDateFrom;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date endDateTo;
	private java.lang.String description;
	private java.lang.Long createdUserId;
	private java.lang.String createdUserName;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date createdDate;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date createdDateFrom;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date createdDateTo;
	private java.lang.Long updatedUserId;
	private java.lang.String updatedUserName;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date updatedDate;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date updatedDateFrom;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date updatedDateTo;
	private java.lang.String signState;
	private java.lang.Long detailMonthPlanId;
	private java.lang.String detailMonthPlanName;
	private java.lang.Long sysUserId;
	private java.lang.String reasonReject;

    @Override
    public RevokeCashMonthPlanBO toModel() {
        RevokeCashMonthPlanBO revokeCashMonthPlanBO = new RevokeCashMonthPlanBO();
        revokeCashMonthPlanBO.setRevokeCashMonthPlanId(this.revokeCashMonthPlanId);
        revokeCashMonthPlanBO.setCntContractCode(this.cntContractCode);
        revokeCashMonthPlanBO.setBillCode(this.billCode);
        revokeCashMonthPlanBO.setCreatedBillDate(this.createdBillDate);
        revokeCashMonthPlanBO.setBillValue(this.billValue);
        revokeCashMonthPlanBO.setAreaCode(this.areaCode);
        revokeCashMonthPlanBO.setProvinceCode(this.provinceCode);
        revokeCashMonthPlanBO.setPerformerId(this.performerId);
        revokeCashMonthPlanBO.setStartDate(this.startDate);
        revokeCashMonthPlanBO.setEndDate(this.endDate);
        revokeCashMonthPlanBO.setDescription(this.description);
        revokeCashMonthPlanBO.setCreatedUserId(this.createdUserId);
        revokeCashMonthPlanBO.setCreatedDate(this.createdDate);
        revokeCashMonthPlanBO.setUpdatedUserId(this.updatedUserId);
        revokeCashMonthPlanBO.setUpdatedDate(this.updatedDate);
        revokeCashMonthPlanBO.setSignState(this.signState);
        revokeCashMonthPlanBO.setDetailMonthPlanId(this.detailMonthPlanId);
        revokeCashMonthPlanBO.setStatus(this.status);
        revokeCashMonthPlanBO.setConstructionId(this.constructionId);
        revokeCashMonthPlanBO.setConstructionCode(this.constructionCode);
        revokeCashMonthPlanBO.setCatStationId(this.catStationId);
        revokeCashMonthPlanBO.setCatStationCode(this.catStationCode);
        revokeCashMonthPlanBO.setReasonReject(this.reasonReject);
        revokeCashMonthPlanBO.setSysGroupId(this.sysGroupId);
        return revokeCashMonthPlanBO;
    }
    
    
    public java.lang.String getReasonReject() {
		return reasonReject;
	}


	public void setReasonReject(java.lang.String reasonReject) {
		this.reasonReject = reasonReject;
	}


	public java.lang.Long getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(java.lang.Long sysUserId) {
		this.sysUserId = sysUserId;
	}

	@Override
     public Long getFWModelId() {
        return revokeCashMonthPlanId;
    }
   
    @Override
    public String catchName() {
        return getRevokeCashMonthPlanId().toString();
    }
	
	@JsonProperty("revokeCashMonthPlanId")
    public java.lang.Long getRevokeCashMonthPlanId(){
		return revokeCashMonthPlanId;
    }
	
    public void setRevokeCashMonthPlanId(java.lang.Long revokeCashMonthPlanId){
		this.revokeCashMonthPlanId = revokeCashMonthPlanId;
    }	
	
	@JsonProperty("cntContractCode")
    public java.lang.String getCntContractCode(){
		return cntContractCode;
    }
	
    public void setCntContractCode(java.lang.String cntContractCode){
		this.cntContractCode = cntContractCode;
    }	
	
	@JsonProperty("billCode")
    public java.lang.String getBillCode(){
		return billCode;
    }
	
    public void setBillCode(java.lang.String billCode){
		this.billCode = billCode;
    }	
	
	@JsonProperty("createdBillDate")
    public java.util.Date getCreatedBillDate(){
		return createdBillDate;
    }
	
    public void setCreatedBillDate(java.util.Date createdBillDate){
		this.createdBillDate = createdBillDate;
    }	
	
	public java.util.Date getCreatedBillDateFrom() {
    	return createdBillDateFrom;
    }
	
    public void setCreatedBillDateFrom(java.util.Date createdBillDateFrom) {
    	this.createdBillDateFrom = createdBillDateFrom;
    }
	
	public java.util.Date getCreatedBillDateTo() {
    	return createdBillDateTo;
    }
	
    public void setCreatedBillDateTo(java.util.Date createdBillDateTo) {
    	this.createdBillDateTo = createdBillDateTo;
    }
	
	@JsonProperty("billValue")
    public java.lang.Double getBillValue(){
		return billValue;
    }
	
    public void setBillValue(java.lang.Double billValue){
		this.billValue = billValue;
    }	
	
	@JsonProperty("areaCode")
    public java.lang.String getAreaCode(){
		return areaCode;
    }
	
    public void setAreaCode(java.lang.String areaCode){
		this.areaCode = areaCode;
    }	
	
	@JsonProperty("provinceCode")
    public java.lang.String getProvinceCode(){
		return provinceCode;
    }
	
    public void setProvinceCode(java.lang.String provinceCode){
		this.provinceCode = provinceCode;
    }	
	
	@JsonProperty("performerId")
    public java.lang.Long getPerformerId(){
		return performerId;
    }
	
    public void setPerformerId(java.lang.Long performerId){
		this.performerId = performerId;
    }	
	
	@JsonProperty("performerName")
    public java.lang.String getPerformerName(){
		return performerName;
    }
	
    public void setPerformerName(java.lang.String performerName){
		this.performerName = performerName;
    }	
	
	@JsonProperty("startDate")
    public java.util.Date getStartDate(){
		return startDate;
    }
	
    public void setStartDate(java.util.Date startDate){
		this.startDate = startDate;
    }	
	
	public java.util.Date getStartDateFrom() {
    	return startDateFrom;
    }
	
    public void setStartDateFrom(java.util.Date startDateFrom) {
    	this.startDateFrom = startDateFrom;
    }
	
	public java.util.Date getStartDateTo() {
    	return startDateTo;
    }
	
    public void setStartDateTo(java.util.Date startDateTo) {
    	this.startDateTo = startDateTo;
    }
	
	@JsonProperty("endDate")
    public java.util.Date getEndDate(){
		return endDate;
    }
	
    public void setEndDate(java.util.Date endDate){
		this.endDate = endDate;
    }	
	
	public java.util.Date getEndDateFrom() {
    	return endDateFrom;
    }
	
    public void setEndDateFrom(java.util.Date endDateFrom) {
    	this.endDateFrom = endDateFrom;
    }
	
	public java.util.Date getEndDateTo() {
    	return endDateTo;
    }
	
    public void setEndDateTo(java.util.Date endDateTo) {
    	this.endDateTo = endDateTo;
    }
	
	@JsonProperty("description")
    public java.lang.String getDescription(){
		return description;
    }
	
    public void setDescription(java.lang.String description){
		this.description = description;
    }	
	
	@JsonProperty("createdUserId")
    public java.lang.Long getCreatedUserId(){
		return createdUserId;
    }
	
    public void setCreatedUserId(java.lang.Long createdUserId){
		this.createdUserId = createdUserId;
    }	
	
	@JsonProperty("createdUserName")
    public java.lang.String getCreatedUserName(){
		return createdUserName;
    }
	
    public void setCreatedUserName(java.lang.String createdUserName){
		this.createdUserName = createdUserName;
    }	
	
	@JsonProperty("createdDate")
    public java.util.Date getCreatedDate(){
		return createdDate;
    }
	
    public void setCreatedDate(java.util.Date createdDate){
		this.createdDate = createdDate;
    }	
	
	public java.util.Date getCreatedDateFrom() {
    	return createdDateFrom;
    }
	
    public void setCreatedDateFrom(java.util.Date createdDateFrom) {
    	this.createdDateFrom = createdDateFrom;
    }
	
	public java.util.Date getCreatedDateTo() {
    	return createdDateTo;
    }
	
    public void setCreatedDateTo(java.util.Date createdDateTo) {
    	this.createdDateTo = createdDateTo;
    }
	
	@JsonProperty("updatedUserId")
    public java.lang.Long getUpdatedUserId(){
		return updatedUserId;
    }
	
    public void setUpdatedUserId(java.lang.Long updatedUserId){
		this.updatedUserId = updatedUserId;
    }	
	
	@JsonProperty("updatedUserName")
    public java.lang.String getUpdatedUserName(){
		return updatedUserName;
    }
	
    public void setUpdatedUserName(java.lang.String updatedUserName){
		this.updatedUserName = updatedUserName;
    }	
	
	@JsonProperty("updatedDate")
    public java.util.Date getUpdatedDate(){
		return updatedDate;
    }
	
    public void setUpdatedDate(java.util.Date updatedDate){
		this.updatedDate = updatedDate;
    }	
	
	public java.util.Date getUpdatedDateFrom() {
    	return updatedDateFrom;
    }
	
    public void setUpdatedDateFrom(java.util.Date updatedDateFrom) {
    	this.updatedDateFrom = updatedDateFrom;
    }
	
	public java.util.Date getUpdatedDateTo() {
    	return updatedDateTo;
    }
	
    public void setUpdatedDateTo(java.util.Date updatedDateTo) {
    	this.updatedDateTo = updatedDateTo;
    }
	
	@JsonProperty("signState")
    public java.lang.String getSignState(){
		return signState;
    }
	
    public void setSignState(java.lang.String signState){
		this.signState = signState;
    }	
	
	@JsonProperty("detailMonthPlanId")
    public java.lang.Long getDetailMonthPlanId(){
		return detailMonthPlanId;
    }
	
    public void setDetailMonthPlanId(java.lang.Long detailMonthPlanId){
		this.detailMonthPlanId = detailMonthPlanId;
    }	
	
	@JsonProperty("detailMonthPlanName")
    public java.lang.String getDetailMonthPlanName(){
		return detailMonthPlanName;
    }
	
    public void setDetailMonthPlanName(java.lang.String detailMonthPlanName){
		this.detailMonthPlanName = detailMonthPlanName;
    }
	
    private String errorFilePath;

    public String getErrorFilePath() {
        return errorFilePath;
    }

    public void setErrorFilePath(String errorFilePath) {
        this.errorFilePath = errorFilePath;
    }
    
    private java.lang.Long status;
	private java.lang.Long constructionId;
	private java.lang.String constructionCode;
	private java.lang.Long catStationId;
	private java.lang.String catStationCode;

	public java.lang.Long getStatus() {
		return status;
	}

	public void setStatus(java.lang.Long status) {
		this.status = status;
	}

	public java.lang.Long getConstructionId() {
		return constructionId;
	}

	public void setConstructionId(java.lang.Long constructionId) {
		this.constructionId = constructionId;
	}

	public java.lang.String getConstructionCode() {
		return constructionCode;
	}

	public void setConstructionCode(java.lang.String constructionCode) {
		this.constructionCode = constructionCode;
	}

	public java.lang.Long getCatStationId() {
		return catStationId;
	}

	public void setCatStationId(java.lang.Long catStationId) {
		this.catStationId = catStationId;
	}

	public java.lang.String getCatStationCode() {
		return catStationCode;
	}

	public void setCatStationCode(java.lang.String catStationCode) {
		this.catStationCode = catStationCode;
	}
	
	private String month;
	private String year;
	private Long sysGroupId;
	private List<RevokeCashMonthPlanDTO> listDataImport;
	
	public List<RevokeCashMonthPlanDTO> getListDataImport() {
		return listDataImport;
	}


	public void setListDataImport(List<RevokeCashMonthPlanDTO> listDataImport) {
		this.listDataImport = listDataImport;
	}


	public String getMonth() {
		return month;
	}


	public void setMonth(String month) {
		this.month = month;
	}


	public String getYear() {
		return year;
	}


	public void setYear(String year) {
		this.year = year;
	}


	public Long getSysGroupId() {
		return sysGroupId;
	}


	public void setSysGroupId(Long sysGroupId) {
		this.sysGroupId = sysGroupId;
	}
	
	
}
