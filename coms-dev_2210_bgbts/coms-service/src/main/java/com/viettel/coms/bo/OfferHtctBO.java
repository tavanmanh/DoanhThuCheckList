package com.viettel.coms.bo;

import com.viettel.coms.dto.OfferHtctDTO;
import com.viettel.service.base.model.BaseFWModelImpl;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@SuppressWarnings("serial")
@Entity(name = "com.viettel.coms.bo.OfferHtctBO")
@Table(name = "OFFER_HTCT")
public class OfferHtctBO extends BaseFWModelImpl {
     
	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = {@Parameter(name = "sequence", value = "OFFER_HTCT_SEQ")})
	@Column(name = "OFFER_ID", length = 22)
	private java.lang.Long offerId;
	@Column(name = "CATEGORY_OFFER", length = 2000)
	private java.lang.String categoryOffer;
	@Column(name = "SYMBOL", length = 2000)
	private java.lang.String symbol;
	@Column(name = "UNIT", length = 200)
	private java.lang.String unit;
	@Column(name = "CREATED_DATE", length = 7)
	private java.util.Date createdDate;
	@Column(name = "CREATED_USER_ID", length = 22)
	private java.lang.Long createdUserId;
	@Column(name = "UPDATE_USER_ID", length = 22)
	private java.lang.Long updateUserId;
	@Column(name = "UPDATED_DATE", length = 7)
	private java.util.Date updatedDate;

	
	public java.lang.Long getOfferId(){
		return offerId;
	}
	
	public void setOfferId(java.lang.Long offerId)
	{
		this.offerId = offerId;
	}
	
	public java.lang.String getCategoryOffer(){
		return categoryOffer;
	}
	
	public void setCategoryOffer(java.lang.String categoryOffer)
	{
		this.categoryOffer = categoryOffer;
	}
	
	public java.lang.String getSymbol(){
		return symbol;
	}
	
	public void setSymbol(java.lang.String symbol)
	{
		this.symbol = symbol;
	}
	
	public java.lang.String getUnit(){
		return unit;
	}
	
	public void setUnit(java.lang.String unit)
	{
		this.unit = unit;
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
    public OfferHtctDTO toDTO() {
        OfferHtctDTO offerHtctDTO = new OfferHtctDTO(); 
        offerHtctDTO.setOfferId(this.offerId);		
        offerHtctDTO.setCategoryOffer(this.categoryOffer);		
        offerHtctDTO.setSymbol(this.symbol);		
        offerHtctDTO.setUnit(this.unit);		
        offerHtctDTO.setCreatedDate(this.createdDate);		
        offerHtctDTO.setCreatedUserId(this.createdUserId);		
        offerHtctDTO.setUpdateUserId(this.updateUserId);		
        offerHtctDTO.setUpdatedDate(this.updatedDate);		
        return offerHtctDTO;
    }
}
