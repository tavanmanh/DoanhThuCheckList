package com.viettel.coms.bo;

import com.viettel.coms.dto.GpmbHtctDTO;
import com.viettel.service.base.model.BaseFWModelImpl;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@SuppressWarnings("serial")
@Entity(name = "com.viettel.coms.bo.GpmbHtctBO")
@Table(name = "GPMB_HTCT")
public class GpmbHtctBO extends BaseFWModelImpl {
     
	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = {@Parameter(name = "sequence", value = "GPMB_HTCT_SEQ")})
	@Column(name = "GPMB_ID", length = 22)
	private java.lang.Long gpmbId;
	@Column(name = "PROVINCE_ID", length = 22)
	private java.lang.Long provinceId;
	@Column(name = "PROVINCE_CODE", length = 100)
	private java.lang.String provinceCode;
	@Column(name = "AMOUNT_BTS", length = 22)
	private java.lang.Long amountBts;
	@Column(name = "COST_GPMB", length = 22)
	private java.lang.Double costGpmb;
	@Column(name = "COST_NCDN", length = 22)
	private java.lang.Double costNcdn;
	@Column(name = "CREATED_DATE", length = 7)
	private java.util.Date createdDate;
	@Column(name = "CREATED_USER_ID", length = 22)
	private java.lang.Long createdUserId;
	@Column(name = "UPDATE_USER_ID", length = 22)
	private java.lang.Long updateUserId;
	@Column(name = "UPDATED_DATE", length = 7)
	private java.util.Date updatedDate;

	
	public java.lang.Long getGpmbId(){
		return gpmbId;
	}
	
	public void setGpmbId(java.lang.Long gpmbId)
	{
		this.gpmbId = gpmbId;
	}
	
	public java.lang.Long getProvinceId(){
		return provinceId;
	}
	
	public void setProvinceId(java.lang.Long provinceId)
	{
		this.provinceId = provinceId;
	}
	
	public java.lang.String getProvinceCode(){
		return provinceCode;
	}
	
	public void setProvinceCode(java.lang.String provinceCode)
	{
		this.provinceCode = provinceCode;
	}
	
	public java.lang.Long getAmountBts(){
		return amountBts;
	}
	
	public void setAmountBts(java.lang.Long amountBts)
	{
		this.amountBts = amountBts;
	}
	
	public java.lang.Double getCostGpmb(){
		return costGpmb;
	}
	
	public void setCostGpmb(java.lang.Double costGpmb)
	{
		this.costGpmb = costGpmb;
	}
	
	public java.lang.Double getCostNcdn(){
		return costNcdn;
	}
	
	public void setCostNcdn(java.lang.Double costNcdn)
	{
		this.costNcdn = costNcdn;
	}
	
	public java.util.Date getCreatedDate(){
		return createdDate;
	}
	
	public void setCreatedDate(java.util.Date createdDate)
	{
		this.createdDate = createdDate;
	}
	
	public java.lang.Long getCreatedUserId(){
		return createdUserId;
	}
	
	public void setCreatedUserId(java.lang.Long createdUserId)
	{
		this.createdUserId = createdUserId;
	}
	
	public java.lang.Long getUpdateUserId(){
		return updateUserId;
	}
	
	public void setUpdateUserId(java.lang.Long updateUserId)
	{
		this.updateUserId = updateUserId;
	}
	
	public java.util.Date getUpdatedDate(){
		return updatedDate;
	}
	
	public void setUpdatedDate(java.util.Date updatedDate)
	{
		this.updatedDate = updatedDate;
	}
   
    @Override
    public GpmbHtctDTO toDTO() {
        GpmbHtctDTO gpmbHtctDTO = new GpmbHtctDTO(); 
        gpmbHtctDTO.setGpmbId(this.gpmbId);		
        gpmbHtctDTO.setProvinceId(this.provinceId);		
        gpmbHtctDTO.setProvinceCode(this.provinceCode);		
        gpmbHtctDTO.setAmountBts(this.amountBts);		
        gpmbHtctDTO.setCostGpmb(this.costGpmb);		
        gpmbHtctDTO.setCostNcdn(this.costNcdn);		
        gpmbHtctDTO.setCreatedDate(this.createdDate);		
        gpmbHtctDTO.setCreatedUserId(this.createdUserId);		
        gpmbHtctDTO.setUpdateUserId(this.updateUserId);		
        gpmbHtctDTO.setUpdatedDate(this.updatedDate);		
        return gpmbHtctDTO;
    }
}
