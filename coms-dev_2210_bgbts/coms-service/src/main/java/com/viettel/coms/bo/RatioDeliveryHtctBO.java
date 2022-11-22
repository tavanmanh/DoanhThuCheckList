package com.viettel.coms.bo;

import com.viettel.coms.dto.RatioDeliveryHtctDTO;
import com.viettel.service.base.model.BaseFWModelImpl;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@SuppressWarnings("serial")
@Entity(name = "com.viettel.coms.bo.RatioDeliveryHtctBO")
@Table(name = "RATIO_DELIVERY_HTCT")
public class RatioDeliveryHtctBO extends BaseFWModelImpl {
     
	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = {@Parameter(name = "sequence", value = "RATIO_DELIVERY_HTCT_SEQ")})
	@Column(name = "RATIO_DELIVERY_ID", length = 22)
	private java.lang.Long ratioDeliveryId;
	@Column(name = "CAT_PROVINCE_ID", length = 22)
	private java.lang.Long catProvinceId;
	@Column(name = "CAT_PROVINCE_CODE", length = 100)
	private java.lang.String catProvinceCode;
	@Column(name = "COST_DELIVERY_BTS", length = 22)
	private java.lang.Double costDeliveryBts;
	@Column(name = "COST_MOUNTAINS_BTS", length = 22)
	private java.lang.Double costMountainsBts;
	@Column(name = "COST_ROOF_BTS", length = 22)
	private java.lang.Double costRoofBts;
	@Column(name = "COST_DELIVERY_PRU", length = 22)
	private java.lang.Double costDeliveryPru;
	@Column(name = "COST_MOUNTAINS_PRU", length = 22)
	private java.lang.Double costMountainsPru;
	@Column(name = "COST_ROOF_PRU", length = 22)
	private java.lang.Double costRoofPru;
	@Column(name = "COST_DELIVERY_SMALLCELL", length = 22)
	private java.lang.Double costDeliverySmallcell;
	@Column(name = "COST_MOUNTAINS_SMALLCELL", length = 22)
	private java.lang.Double costMountainsSmallcell;
	@Column(name = "COST_ROOF_SMALLCELL", length = 22)
	private java.lang.Double costRoofSmallcell;
	@Column(name = "CREATED_DATE", length = 7)
	private java.util.Date createdDate;
	@Column(name = "CREATED_USER_ID", length = 22)
	private java.lang.Long createdUserId;
	@Column(name = "UPDATE_USER_ID", length = 22)
	private java.lang.Long updateUserId;
	@Column(name = "UPDATED_DATE", length = 7)
	private java.util.Date updatedDate;

	
	public java.lang.Long getRatioDeliveryId(){
		return ratioDeliveryId;
	}
	
	public void setRatioDeliveryId(java.lang.Long ratioDeliveryId)
	{
		this.ratioDeliveryId = ratioDeliveryId;
	}
	
	public java.lang.Long getCatProvinceId(){
		return catProvinceId;
	}
	
	public void setCatProvinceId(java.lang.Long catProvinceId)
	{
		this.catProvinceId = catProvinceId;
	}
	
	public java.lang.String getCatProvinceCode(){
		return catProvinceCode;
	}
	
	public void setCatProvinceCode(java.lang.String catProvinceCode)
	{
		this.catProvinceCode = catProvinceCode;
	}
	
	public java.lang.Double getCostDeliveryBts(){
		return costDeliveryBts;
	}
	
	public void setCostDeliveryBts(java.lang.Double costDeliveryBts)
	{
		this.costDeliveryBts = costDeliveryBts;
	}
	
	public java.lang.Double getCostMountainsBts(){
		return costMountainsBts;
	}
	
	public void setCostMountainsBts(java.lang.Double costMountainsBts)
	{
		this.costMountainsBts = costMountainsBts;
	}
	
	public java.lang.Double getCostRoofBts(){
		return costRoofBts;
	}
	
	public void setCostRoofBts(java.lang.Double costRoofBts)
	{
		this.costRoofBts = costRoofBts;
	}
	
	public java.lang.Double getCostDeliveryPru(){
		return costDeliveryPru;
	}
	
	public void setCostDeliveryPru(java.lang.Double costDeliveryPru)
	{
		this.costDeliveryPru = costDeliveryPru;
	}
	
	public java.lang.Double getCostMountainsPru(){
		return costMountainsPru;
	}
	
	public void setCostMountainsPru(java.lang.Double costMountainsPru)
	{
		this.costMountainsPru = costMountainsPru;
	}
	
	public java.lang.Double getCostRoofPru(){
		return costRoofPru;
	}
	
	public void setCostRoofPru(java.lang.Double costRoofPru)
	{
		this.costRoofPru = costRoofPru;
	}
	
	public java.lang.Double getCostDeliverySmallcell(){
		return costDeliverySmallcell;
	}
	
	public void setCostDeliverySmallcell(java.lang.Double costDeliverySmallcell)
	{
		this.costDeliverySmallcell = costDeliverySmallcell;
	}
	
	public java.lang.Double getCostMountainsSmallcell(){
		return costMountainsSmallcell;
	}
	
	public void setCostMountainsSmallcell(java.lang.Double costMountainsSmallcell)
	{
		this.costMountainsSmallcell = costMountainsSmallcell;
	}
	
	public java.lang.Double getCostRoofSmallcell(){
		return costRoofSmallcell;
	}
	
	public void setCostRoofSmallcell(java.lang.Double costRoofSmallcell)
	{
		this.costRoofSmallcell = costRoofSmallcell;
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
    public RatioDeliveryHtctDTO toDTO() {
        RatioDeliveryHtctDTO ratioDeliveryHtctDTO = new RatioDeliveryHtctDTO(); 
        ratioDeliveryHtctDTO.setRatioDeliveryId(this.ratioDeliveryId);		
        ratioDeliveryHtctDTO.setCatProvinceId(this.catProvinceId);		
        ratioDeliveryHtctDTO.setCatProvinceCode(this.catProvinceCode);		
        ratioDeliveryHtctDTO.setCostDeliveryBts(this.costDeliveryBts);		
        ratioDeliveryHtctDTO.setCostMountainsBts(this.costMountainsBts);		
        ratioDeliveryHtctDTO.setCostRoofBts(this.costRoofBts);		
        ratioDeliveryHtctDTO.setCostDeliveryPru(this.costDeliveryPru);		
        ratioDeliveryHtctDTO.setCostMountainsPru(this.costMountainsPru);		
        ratioDeliveryHtctDTO.setCostRoofPru(this.costRoofPru);		
        ratioDeliveryHtctDTO.setCostDeliverySmallcell(this.costDeliverySmallcell);		
        ratioDeliveryHtctDTO.setCostMountainsSmallcell(this.costMountainsSmallcell);		
        ratioDeliveryHtctDTO.setCostRoofSmallcell(this.costRoofSmallcell);		
        ratioDeliveryHtctDTO.setCreatedDate(this.createdDate);		
        ratioDeliveryHtctDTO.setCreatedUserId(this.createdUserId);		
        ratioDeliveryHtctDTO.setUpdateUserId(this.updateUserId);		
        ratioDeliveryHtctDTO.setUpdatedDate(this.updatedDate);		
        return ratioDeliveryHtctDTO;
    }
}
