package com.viettel.coms.dto;

import com.viettel.coms.bo.OfferHtctBO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import com.viettel.erp.utils.CustomJsonDateDeserializer;
import com.viettel.erp.utils.CustomJsonDateSerializer;

@SuppressWarnings("serial")
@XmlRootElement(name = "OFFER_HTCTBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class OfferHtctDTO extends ComsBaseFWDTO<OfferHtctBO> {

	private java.lang.Long offerId;

	private java.lang.String categoryOffer;

	private java.lang.String symbol;

	private java.lang.String unit;

	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date createdDate;

	private java.lang.Long createdUserId;

	private java.lang.Long updateUserId;

	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date updatedDate;


    @Override
    public OfferHtctBO toModel() {
        OfferHtctBO offerHtctBO = new OfferHtctBO();
        offerHtctBO.setOfferId(this.offerId);
        offerHtctBO.setCategoryOffer(this.categoryOffer);
        offerHtctBO.setSymbol(this.symbol);
        offerHtctBO.setUnit(this.unit);
        offerHtctBO.setCreatedDate(this.createdDate);
        offerHtctBO.setCreatedUserId(this.createdUserId);
        offerHtctBO.setUpdateUserId(this.updateUserId);
        offerHtctBO.setUpdatedDate(this.updatedDate);
        return offerHtctBO;
    }

    public java.lang.Long getOfferId(){
		return offerId;
    }
	
    public void setOfferId(java.lang.Long offerId){
		this.offerId = offerId;
    }	
	
    public java.lang.String getCategoryOffer(){
		return categoryOffer;
    }
	
    public void setCategoryOffer(java.lang.String categoryOffer){
		this.categoryOffer = categoryOffer;
    }	
	
	// 	
    public java.lang.String getSymbol(){
		return symbol;
    }
	
    public void setSymbol(java.lang.String symbol){
		this.symbol = symbol;
    }	
	
	// 	
    public java.lang.String getUnit(){
		return unit;
    }
	
    public void setUnit(java.lang.String unit){
		this.unit = unit;
    }	
	
	// 	
    public java.util.Date getCreatedDate(){
		return createdDate;
    }
	
    public void setCreatedDate(java.util.Date createdDate){
		this.createdDate = createdDate;
    }	
	
	
    public java.lang.Long getCreatedUserId(){
		return createdUserId;
    }
	
    public void setCreatedUserId(java.lang.Long createdUserId){
		this.createdUserId = createdUserId;
    }	
	
    public java.lang.Long getUpdateUserId(){
		return updateUserId;
    }
	
    public void setUpdateUserId(java.lang.Long updateUserId){
		this.updateUserId = updateUserId;
    }	
	
    public java.util.Date getUpdatedDate(){
		return updatedDate;
    }
	
    public void setUpdatedDate(java.util.Date updatedDate){
		this.updatedDate = updatedDate;
    }	
	

	@Override
	public String catchName() {
		return getOfferId().toString();
	}

	@Override
	public Long getFWModelId() {
		return offerId;
	}
	
}
