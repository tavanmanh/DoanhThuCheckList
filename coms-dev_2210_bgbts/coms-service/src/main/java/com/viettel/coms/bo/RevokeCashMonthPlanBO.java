package com.viettel.coms.bo;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.RevokeCashMonthPlanDTO;
import com.viettel.service.base.model.BaseFWModelImpl;

@SuppressWarnings("serial")
@Entity(name = "com.viettel.erp.bo.RevokeCashMonthPlanBO")
@Table(name = "REVOKE_CASH_MONTH_PLAN")
/**
 *
 * @author: hailh10
 */
public class RevokeCashMonthPlanBO extends BaseFWModelImpl {
     
	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = { @Parameter(name = "sequence", value = "REVOKE_CASH_MONTH_PLAN_SEQ") })
	@Column(name = "REVOKE_CASH_MONTH_PLAN_ID", length = 22)
	private java.lang.Long revokeCashMonthPlanId;
	@Column(name = "CNT_CONTRACT_CODE", length = 1000)
	private java.lang.String cntContractCode;
	@Column(name = "BILL_CODE", length = 200)
	private java.lang.String billCode;
	@Column(name = "CREATED_BILL_DATE", length = 7)
	private java.util.Date createdBillDate;
	@Column(name = "BILL_VALUE", length = 22)
	private java.lang.Double billValue;
	@Column(name = "AREA_CODE", length = 200)
	private java.lang.String areaCode;
	@Column(name = "PROVINCE_CODE", length = 200)
	private java.lang.String provinceCode;
	@Column(name = "PERFORMER_ID", length = 22)
	private java.lang.Long performerId;
	@Column(name = "START_DATE", length = 7)
	private java.util.Date startDate;
	@Column(name = "END_DATE", length = 7)
	private java.util.Date endDate;
	@Column(name = "DESCRIPTION", length = 1000)
	private java.lang.String description;
	@Column(name = "CREATED_USER_ID", length = 22)
	private java.lang.Long createdUserId;
	@Column(name = "CREATED_DATE", length = 7)
	private java.util.Date createdDate;
	@Column(name = "UPDATED_USER_ID", length = 22)
	private java.lang.Long updatedUserId;
	@Column(name = "UPDATED_DATE", length = 7)
	private java.util.Date updatedDate;
	@Column(name = "SIGN_STATE", length = 4)
	private java.lang.String signState;
	@Column(name = "DETAIL_MONTH_PLAN_ID", length = 22)
	private java.lang.Long detailMonthPlanId;
	
	@Column(name = "STATUS", length = 22)
	private java.lang.Long status;
	@Column(name = "CONSTRUCTION_ID", length = 22)
	private java.lang.Long constructionId;
	@Column(name = "CONSTRUCTION_CODE", length = 22)
	private java.lang.String constructionCode;
	@Column(name = "CAT_STATION_ID", length = 22)
	private java.lang.Long catStationId;
	@Column(name = "CAT_STATION_CODE", length = 100)
	private java.lang.String catStationCode;
	
	@Column(name = "REASON_REJECT", length = 500)
	private java.lang.String reasonReject;
	
	@Column(name = "SYS_GROUP_ID", length = 500)
	private Long sysGroupId;
	
	
	public Long getSysGroupId() {
		return sysGroupId;
	}

	public void setSysGroupId(Long sysGroupId) {
		this.sysGroupId = sysGroupId;
	}

	public java.lang.String getReasonReject() {
		return reasonReject;
	}

	public void setReasonReject(java.lang.String reasonReject) {
		this.reasonReject = reasonReject;
	}

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

	public java.lang.Long getRevokeCashMonthPlanId(){
		return revokeCashMonthPlanId;
	}
	
	public void setRevokeCashMonthPlanId(java.lang.Long revokeCashMonthPlanId)
	{
		this.revokeCashMonthPlanId = revokeCashMonthPlanId;
	}
	
	public java.lang.String getCntContractCode(){
		return cntContractCode;
	}
	
	public void setCntContractCode(java.lang.String cntContractCode)
	{
		this.cntContractCode = cntContractCode;
	}
	
	public java.lang.String getBillCode(){
		return billCode;
	}
	
	public void setBillCode(java.lang.String billCode)
	{
		this.billCode = billCode;
	}
	
	public java.util.Date getCreatedBillDate(){
		return createdBillDate;
	}
	
	public void setCreatedBillDate(java.util.Date createdBillDate)
	{
		this.createdBillDate = createdBillDate;
	}
	
	public java.lang.Double getBillValue(){
		return billValue;
	}
	
	public void setBillValue(java.lang.Double billValue)
	{
		this.billValue = billValue;
	}
	
	public java.lang.String getAreaCode(){
		return areaCode;
	}
	
	public void setAreaCode(java.lang.String areaCode)
	{
		this.areaCode = areaCode;
	}
	
	public java.lang.String getProvinceCode(){
		return provinceCode;
	}
	
	public void setProvinceCode(java.lang.String provinceCode)
	{
		this.provinceCode = provinceCode;
	}
	
	public java.lang.Long getPerformerId(){
		return performerId;
	}
	
	public void setPerformerId(java.lang.Long performerId)
	{
		this.performerId = performerId;
	}
	
	public java.util.Date getStartDate(){
		return startDate;
	}
	
	public void setStartDate(java.util.Date startDate)
	{
		this.startDate = startDate;
	}
	
	public java.util.Date getEndDate(){
		return endDate;
	}
	
	public void setEndDate(java.util.Date endDate)
	{
		this.endDate = endDate;
	}
	
	public java.lang.String getDescription(){
		return description;
	}
	
	public void setDescription(java.lang.String description)
	{
		this.description = description;
	}
	
	public java.lang.Long getCreatedUserId(){
		return createdUserId;
	}
	
	public void setCreatedUserId(java.lang.Long createdUserId)
	{
		this.createdUserId = createdUserId;
	}
	
	public java.util.Date getCreatedDate(){
		return createdDate;
	}
	
	public void setCreatedDate(java.util.Date createdDate)
	{
		this.createdDate = createdDate;
	}
	
	public java.lang.Long getUpdatedUserId(){
		return updatedUserId;
	}
	
	public void setUpdatedUserId(java.lang.Long updatedUserId)
	{
		this.updatedUserId = updatedUserId;
	}
	
	public java.util.Date getUpdatedDate(){
		return updatedDate;
	}
	
	public void setUpdatedDate(java.util.Date updatedDate)
	{
		this.updatedDate = updatedDate;
	}
	
	public java.lang.String getSignState(){
		return signState;
	}
	
	public void setSignState(java.lang.String signState)
	{
		this.signState = signState;
	}
	
	public java.lang.Long getDetailMonthPlanId(){
		return detailMonthPlanId;
	}
	
	public void setDetailMonthPlanId(java.lang.Long detailMonthPlanId)
	{
		this.detailMonthPlanId = detailMonthPlanId;
	}
   
    @Override
    public RevokeCashMonthPlanDTO toDTO() {
        RevokeCashMonthPlanDTO revokeCashMonthPlanDTO = new RevokeCashMonthPlanDTO(); 
        revokeCashMonthPlanDTO.setRevokeCashMonthPlanId(this.revokeCashMonthPlanId);		
        revokeCashMonthPlanDTO.setCntContractCode(this.cntContractCode);		
        revokeCashMonthPlanDTO.setBillCode(this.billCode);		
        revokeCashMonthPlanDTO.setCreatedBillDate(this.createdBillDate);		
        revokeCashMonthPlanDTO.setBillValue(this.billValue);		
        revokeCashMonthPlanDTO.setAreaCode(this.areaCode);		
        revokeCashMonthPlanDTO.setProvinceCode(this.provinceCode);		
        revokeCashMonthPlanDTO.setPerformerId(this.performerId);		
        revokeCashMonthPlanDTO.setStartDate(this.startDate);		
        revokeCashMonthPlanDTO.setEndDate(this.endDate);		
        revokeCashMonthPlanDTO.setDescription(this.description);		
        revokeCashMonthPlanDTO.setCreatedUserId(this.createdUserId);		
        revokeCashMonthPlanDTO.setCreatedDate(this.createdDate);		
        revokeCashMonthPlanDTO.setUpdatedUserId(this.updatedUserId);		
        revokeCashMonthPlanDTO.setUpdatedDate(this.updatedDate);		
        revokeCashMonthPlanDTO.setSignState(this.signState);		
        revokeCashMonthPlanDTO.setDetailMonthPlanId(this.detailMonthPlanId);
        
        revokeCashMonthPlanDTO.setStatus(this.status);
        revokeCashMonthPlanDTO.setConstructionId(this.constructionId);
        revokeCashMonthPlanDTO.setConstructionCode(this.constructionCode);
        revokeCashMonthPlanDTO.setCatStationId(this.catStationId);
        revokeCashMonthPlanDTO.setCatStationCode(this.catStationCode);
        revokeCashMonthPlanDTO.setReasonReject(this.reasonReject);
        revokeCashMonthPlanDTO.setSysGroupId(this.sysGroupId);
        return revokeCashMonthPlanDTO;
    }
}
