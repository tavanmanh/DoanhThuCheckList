package com.viettel.coms.bo;

import com.viettel.coms.dto.CostVtnetHtctDTO;
import com.viettel.service.base.model.BaseFWModelImpl;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@SuppressWarnings("serial")
@Entity(name = "com.viettel.coms.bo.CostVtnetHtctBO")
@Table(name = "COST_VTNET_HTCT")
public class CostVtnetHtctBO extends BaseFWModelImpl {
     
	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = {@Parameter(name = "sequence", value = "COST_VTNET_HTCT_SEQ")})
	@Column(name = "COST_VTNET_ID", length = 22)
	private java.lang.Long costVtnetId;
	@Column(name = "STATION_TYPE", length = 100)
	private java.lang.String stationType;
	@Column(name = "NOT_SOURCE_HNI_HCM", length = 22)
	private java.lang.Double notSourceHniHcm;
	@Column(name = "NOT_SOURCE_61_PROVINCE", length = 22)
	private java.lang.Double notSource61Province;
	@Column(name = "SOURCE_HNI_HCM", length = 22)
	private java.lang.Double sourceHniHcm;
	@Column(name = "SOURCE_61_PROVINCE", length = 22)
	private java.lang.Double source61Province;
	@Column(name = "CREATED_DATE", length = 7)
	private java.util.Date createdDate;
	@Column(name = "CREATED_USER_ID", length = 22)
	private java.lang.Long createdUserId;
	@Column(name = "UPDATE_USER_ID", length = 22)
	private java.lang.Long updateUserId;
	@Column(name = "UPDATED_DATE", length = 7)
	private java.util.Date updatedDate;

	
	public java.lang.Long getCostVtnetId(){
		return costVtnetId;
	}
	
	public void setCostVtnetId(java.lang.Long costVtnetId)
	{
		this.costVtnetId = costVtnetId;
	}
	
	public java.lang.String getStationType(){
		return stationType;
	}
	
	public void setStationType(java.lang.String stationType)
	{
		this.stationType = stationType;
	}
	
	public java.lang.Double getNotSourceHniHcm(){
		return notSourceHniHcm;
	}
	
	public void setNotSourceHniHcm(java.lang.Double notSourceHniHcm)
	{
		this.notSourceHniHcm = notSourceHniHcm;
	}
	
	public java.lang.Double getNotSource61Province(){
		return notSource61Province;
	}
	
	public void setNotSource61Province(java.lang.Double notSource61Province)
	{
		this.notSource61Province = notSource61Province;
	}
	
	public java.lang.Double getSourceHniHcm(){
		return sourceHniHcm;
	}
	
	public void setSourceHniHcm(java.lang.Double sourceHniHcm)
	{
		this.sourceHniHcm = sourceHniHcm;
	}
	
	public java.lang.Double getSource61Province(){
		return source61Province;
	}
	
	public void setSource61Province(java.lang.Double source61Province)
	{
		this.source61Province = source61Province;
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
    public CostVtnetHtctDTO toDTO() {
        CostVtnetHtctDTO costVtnetHtctDTO = new CostVtnetHtctDTO(); 
        costVtnetHtctDTO.setCostVtnetId(this.costVtnetId);		
        costVtnetHtctDTO.setStationType(this.stationType);		
        costVtnetHtctDTO.setNotSourceHniHcm(this.notSourceHniHcm);		
        costVtnetHtctDTO.setNotSource61Province(this.notSource61Province);		
        costVtnetHtctDTO.setSourceHniHcm(this.sourceHniHcm);		
        costVtnetHtctDTO.setSource61Province(this.source61Province);		
        costVtnetHtctDTO.setCreatedDate(this.createdDate);		
        costVtnetHtctDTO.setCreatedUserId(this.createdUserId);		
        costVtnetHtctDTO.setUpdateUserId(this.updateUserId);		
        costVtnetHtctDTO.setUpdatedDate(this.updatedDate);		
        return costVtnetHtctDTO;
    }
}
