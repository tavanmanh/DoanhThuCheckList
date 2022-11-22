package com.viettel.coms.bo;

import com.viettel.coms.dto.CapexBtsHtctDTO;
import com.viettel.service.base.model.BaseFWModelImpl;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@SuppressWarnings("serial")
@Entity(name = "com.viettel.coms.bo.CapexBtsHtctBO")
@Table(name = "CAPEX_BTS_HTCT")
public class CapexBtsHtctBO extends BaseFWModelImpl {
     
	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = {@Parameter(name = "sequence", value = "CAPEX_BTS_HTCT_SEQ")})
	@Column(name = "CAPEX_BTS_ID", length = 22)
	private java.lang.Long capexBtsId;
	@Column(name = "ITEM_TYPE", length = 2000)
	private java.lang.String itemType;
	@Column(name = "ITEM", length = 2000)
	private java.lang.String item;
	@Column(name = "WORK_CAPEX", length = 2000)
	private java.lang.String workCapex;
	@Column(name = "PROVINCE_ID", length = 22)
	private java.lang.Long provinceId;
	@Column(name = "PROVINCE_CODE", length = 100)
	private java.lang.String provinceCode;
	@Column(name = "COST_CAPEX_BTS", length = 22)
	private java.lang.Double costCapexBts;
	@Column(name = "CREATED_DATE", length = 7)
	private java.util.Date createdDate;
	@Column(name = "CREATED_USER_ID", length = 22)
	private java.lang.Long createdUserId;
	@Column(name = "UPDATE_USER_ID", length = 22)
	private java.lang.Long updateUserId;
	@Column(name = "UPDATED_DATE", length = 7)
	private java.util.Date updatedDate;

	
	public java.lang.Long getCapexBtsId(){
		return capexBtsId;
	}
	
	public void setCapexBtsId(java.lang.Long capexBtsId)
	{
		this.capexBtsId = capexBtsId;
	}
	
	public java.lang.String getItemType(){
		return itemType;
	}
	
	public void setItemType(java.lang.String itemType)
	{
		this.itemType = itemType;
	}
	
	public java.lang.String getItem(){
		return item;
	}
	
	public void setItem(java.lang.String item)
	{
		this.item = item;
	}
	
	public java.lang.String getWorkCapex(){
		return workCapex;
	}
	
	public void setWorkCapex(java.lang.String workCapex)
	{
		this.workCapex = workCapex;
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
	
	public java.lang.Double getCostCapexBts(){
		return costCapexBts;
	}
	
	public void setCostCapexBts(java.lang.Double costCapexBts)
	{
		this.costCapexBts = costCapexBts;
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
    public CapexBtsHtctDTO toDTO() {
        CapexBtsHtctDTO capexBtsHtctDTO = new CapexBtsHtctDTO(); 
        capexBtsHtctDTO.setCapexBtsId(this.capexBtsId);		
        capexBtsHtctDTO.setItemType(this.itemType);		
        capexBtsHtctDTO.setItem(this.item);		
        capexBtsHtctDTO.setWorkCapex(this.workCapex);		
        capexBtsHtctDTO.setProvinceId(this.provinceId);		
        capexBtsHtctDTO.setProvinceCode(this.provinceCode);		
        capexBtsHtctDTO.setCostCapexBts(this.costCapexBts);		
        capexBtsHtctDTO.setCreatedDate(this.createdDate);		
        capexBtsHtctDTO.setCreatedUserId(this.createdUserId);		
        capexBtsHtctDTO.setUpdateUserId(this.updateUserId);		
        capexBtsHtctDTO.setUpdatedDate(this.updatedDate);		
        return capexBtsHtctDTO;
    }
}
