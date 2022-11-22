package com.viettel.coms.bo;

import com.viettel.coms.dto.Cost1477HtctDTO;
import com.viettel.service.base.model.BaseFWModelImpl;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@SuppressWarnings("serial")
@Entity(name = "com.viettel.coms.bo.Cost1477HtctBO")
@Table(name = "COST_1477_HTCT")
public class Cost1477HtctBO extends BaseFWModelImpl {
     
	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = {@Parameter(name = "sequence", value = "COST_1477_HTCT_SEQ")})
	@Column(name = "COST_1477_HTCT_ID", length = 22)
	private java.lang.Long cost1477HtctId;
	@Column(name = "TYPE_GROUP", length = 2000)
	private java.lang.String typeGroup;
	@Column(name = "ADDRESS", length = 2000)
	private java.lang.String address;
	@Column(name = "TOPOGRAPHIC", length = 2000)
	private java.lang.String topographic;
	@Column(name = "STATION_TYPE", length = 2000)
	private java.lang.String stationType;
	@Column(name = "COST_1477", length = 22)
	private java.lang.Double cost1477;
	@Column(name = "CREATED_DATE", length = 7)
	private java.util.Date createdDate;
	@Column(name = "CREATED_USER_ID", length = 22)
	private java.lang.Long createdUserId;
	@Column(name = "UPDATE_USER_ID", length = 22)
	private java.lang.Long updateUserId;
	@Column(name = "UPDATED_DATE", length = 7)
	private java.util.Date updatedDate;

	
	public java.lang.Long getCost1477HtctId(){
		return cost1477HtctId;
	}
	
	public void setCost1477HtctId(java.lang.Long cost1477HtctId)
	{
		this.cost1477HtctId = cost1477HtctId;
	}
	
	public java.lang.String getTypeGroup(){
		return typeGroup;
	}
	
	public void setTypeGroup(java.lang.String typeGroup)
	{
		this.typeGroup = typeGroup;
	}
	
	public java.lang.String getAddress(){
		return address;
	}
	
	public void setAddress(java.lang.String address)
	{
		this.address = address;
	}
	
	public java.lang.String getTopographic(){
		return topographic;
	}
	
	public void setTopographic(java.lang.String topographic)
	{
		this.topographic = topographic;
	}
	
	public java.lang.String getStationType(){
		return stationType;
	}
	
	public void setStationType(java.lang.String stationType)
	{
		this.stationType = stationType;
	}
	
	public java.lang.Double getCost1477(){
		return cost1477;
	}
	
	public void setCost1477(java.lang.Double cost1477)
	{
		this.cost1477 = cost1477;
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
    public Cost1477HtctDTO toDTO() {
        Cost1477HtctDTO cost1477HtctDTO = new Cost1477HtctDTO(); 
        cost1477HtctDTO.setCost1477HtctId(this.cost1477HtctId);		
        cost1477HtctDTO.setTypeGroup(this.typeGroup);		
        cost1477HtctDTO.setAddress(this.address);		
        cost1477HtctDTO.setTopographic(this.topographic);		
        cost1477HtctDTO.setStationType(this.stationType);		
        cost1477HtctDTO.setCost1477(this.cost1477);		
        cost1477HtctDTO.setCreatedDate(this.createdDate);		
        cost1477HtctDTO.setCreatedUserId(this.createdUserId);		
        cost1477HtctDTO.setUpdateUserId(this.updateUserId);		
        cost1477HtctDTO.setUpdatedDate(this.updatedDate);		
        return cost1477HtctDTO;
    }
}
