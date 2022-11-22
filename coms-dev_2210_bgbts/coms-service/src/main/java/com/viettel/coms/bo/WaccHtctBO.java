package com.viettel.coms.bo;

import com.viettel.coms.dto.WaccHtctDTO;
import com.viettel.service.base.model.BaseFWModelImpl;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@SuppressWarnings("serial")
@Entity(name = "com.viettel.coms.bo.WaccHtctBO")
@Table(name = "WACC_HTCT")
public class WaccHtctBO extends BaseFWModelImpl {
	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = {@Parameter(name = "sequence", value = "WACC_HTCT_SEQ")})
	@Column(name = "WACC_ID", length = 22)
	private java.lang.Long waccId;
	@Column(name = "WACC_NAME", length = 2000)
	private java.lang.String waccName;
	@Column(name = "WACC_REX", length = 22)
	private java.lang.Double waccRex;
	@Column(name = "CREATED_DATE", length = 7)
	private java.util.Date createdDate;
	@Column(name = "CREATED_USER_ID", length = 22)
	private java.lang.Long createdUserId;
	@Column(name = "UPDATE_USER_ID", length = 22)
	private java.lang.Long updateUserId;
	@Column(name = "UPDATED_DATE", length = 7)
	private java.util.Date updatedDate;

	
	public java.lang.Long getWaccId(){
		return waccId;
	}
	
	public void setWaccId(java.lang.Long waccId)
	{
		this.waccId = waccId;
	}
	
	public java.lang.String getWaccName(){
		return waccName;
	}
	
	public void setWaccName(java.lang.String waccName)
	{
		this.waccName = waccName;
	}
	
	public java.lang.Double getWaccRex(){
		return waccRex;
	}
	
	public void setWaccRex(java.lang.Double waccRex)
	{
		this.waccRex = waccRex;
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
    public WaccHtctDTO toDTO() {
        WaccHtctDTO waccHtctDTO = new WaccHtctDTO(); 
        waccHtctDTO.setWaccId(this.waccId);		
        waccHtctDTO.setWaccName(this.waccName);		
        waccHtctDTO.setWaccRex(this.waccRex);		
        waccHtctDTO.setCreatedDate(this.createdDate);		
        waccHtctDTO.setCreatedUserId(this.createdUserId);		
        waccHtctDTO.setUpdateUserId(this.updateUserId);		
        waccHtctDTO.setUpdatedDate(this.updatedDate);		
        return waccHtctDTO;
    }
}
