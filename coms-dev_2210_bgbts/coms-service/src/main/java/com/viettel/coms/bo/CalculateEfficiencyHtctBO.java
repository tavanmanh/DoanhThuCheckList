package com.viettel.coms.bo;

import com.viettel.coms.dto.CalculateEfficiencyHtctDTO;
import com.viettel.service.base.model.BaseFWModelImpl;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@SuppressWarnings("serial")
@Entity(name = "com.viettel.coms.bo.CalculateEfficiencyHtctBO")
@Table(name = "CALCULATE_EFFICIENCY_HTCT")
public class CalculateEfficiencyHtctBO extends BaseFWModelImpl {
     
	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = {@Parameter(name = "sequence", value = "CALCULATE_EFFICIENCY_HTCT_SEQ")})
	@Column(name = "CALCULATE_EFFICIENCY_ID", length = 22)
	private java.lang.Long calculateEfficiencyId;
	@Column(name = "CONTENT_CAL_EFF", length = 2000)
	private java.lang.String contentCalEff;
	@Column(name = "UNIT", length = 200)
	private java.lang.String unit;
	@Column(name = "COST_CAL_EFF", length = 22)
	private java.lang.Double costCalEff;
	@Column(name = "COST_NOT_SOURCE", length = 22)
	private java.lang.Double costNotSource;
	@Column(name = "COST_SOURCE", length = 22)
	private java.lang.Double costSource;
	@Column(name = "CREATED_DATE", length = 7)
	private java.util.Date createdDate;
	@Column(name = "CREATED_USER_ID", length = 22)
	private java.lang.Long createdUserId;
	@Column(name = "UPDATE_USER_ID", length = 22)
	private java.lang.Long updateUserId;
	@Column(name = "UPDATED_DATE", length = 7)
	private java.util.Date updatedDate;

	
	public java.lang.Long getCalculateEfficiencyId(){
		return calculateEfficiencyId;
	}
	
	public void setCalculateEfficiencyId(java.lang.Long calculateEfficiencyId)
	{
		this.calculateEfficiencyId = calculateEfficiencyId;
	}
	
	public java.lang.String getContentCalEff(){
		return contentCalEff;
	}
	
	public void setContentCalEff(java.lang.String contentCalEff)
	{
		this.contentCalEff = contentCalEff;
	}
	
	public java.lang.String getUnit(){
		return unit;
	}
	
	public void setUnit(java.lang.String unit)
	{
		this.unit = unit;
	}
	
	public java.lang.Double getCostCalEff(){
		return costCalEff;
	}
	
	public void setCostCalEff(java.lang.Double costCalEff)
	{
		this.costCalEff = costCalEff;
	}
	
	public java.lang.Double getCostNotSource(){
		return costNotSource;
	}
	
	public void setCostNotSource(java.lang.Double costNotSource)
	{
		this.costNotSource = costNotSource;
	}
	
	public java.lang.Double getCostSource(){
		return costSource;
	}
	
	public void setCostSource(java.lang.Double costSource)
	{
		this.costSource = costSource;
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
    public CalculateEfficiencyHtctDTO toDTO() {
        CalculateEfficiencyHtctDTO calculateEfficiencyHtctDTO = new CalculateEfficiencyHtctDTO(); 
        calculateEfficiencyHtctDTO.setCalculateEfficiencyId(this.calculateEfficiencyId);		
        calculateEfficiencyHtctDTO.setContentCalEff(this.contentCalEff);		
        calculateEfficiencyHtctDTO.setUnit(this.unit);		
        calculateEfficiencyHtctDTO.setCostCalEff(this.costCalEff);		
        calculateEfficiencyHtctDTO.setCostNotSource(this.costNotSource);		
        calculateEfficiencyHtctDTO.setCostSource(this.costSource);		
        calculateEfficiencyHtctDTO.setCreatedDate(this.createdDate);		
        calculateEfficiencyHtctDTO.setCreatedUserId(this.createdUserId);		
        calculateEfficiencyHtctDTO.setUpdateUserId(this.updateUserId);		
        calculateEfficiencyHtctDTO.setUpdatedDate(this.updatedDate);		
        return calculateEfficiencyHtctDTO;
    }
}
