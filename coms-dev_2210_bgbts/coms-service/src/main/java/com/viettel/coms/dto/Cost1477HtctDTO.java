package com.viettel.coms.dto;

import com.viettel.coms.bo.Cost1477HtctBO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import com.viettel.erp.utils.CustomJsonDateDeserializer;
import com.viettel.erp.utils.CustomJsonDateSerializer;

@SuppressWarnings("serial")
@XmlRootElement(name = "COST_1477_HTCTBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cost1477HtctDTO extends ComsBaseFWDTO<Cost1477HtctBO> {

	private java.lang.Long cost1477HtctId;

	private java.lang.String typeGroup;

	private java.lang.String address;

	private java.lang.String topographic;

	private java.lang.String stationType;

	private java.lang.Double cost1477;

	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date createdDate;

	private java.lang.Long createdUserId;

	private java.lang.Long updateUserId;

	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date updatedDate;


    @Override
    public Cost1477HtctBO toModel() {
        Cost1477HtctBO cost1477HtctBO = new Cost1477HtctBO();
        cost1477HtctBO.setCost1477HtctId(this.cost1477HtctId);
        cost1477HtctBO.setTypeGroup(this.typeGroup);
        cost1477HtctBO.setAddress(this.address);
        cost1477HtctBO.setTopographic(this.topographic);
        cost1477HtctBO.setStationType(this.stationType);
        cost1477HtctBO.setCost1477(this.cost1477);
        cost1477HtctBO.setCreatedDate(this.createdDate);
        cost1477HtctBO.setCreatedUserId(this.createdUserId);
        cost1477HtctBO.setUpdateUserId(this.updateUserId);
        cost1477HtctBO.setUpdatedDate(this.updatedDate);
        return cost1477HtctBO;
    }

    public java.lang.Long getCost1477HtctId(){
		return cost1477HtctId;
    }

    public void setCost1477HtctId(java.lang.Long cost1477HtctId){
		this.cost1477HtctId = cost1477HtctId;
    }

    public java.lang.String getTypeGroup(){
		return typeGroup;
    }

    public void setTypeGroup(java.lang.String typeGroup){
		this.typeGroup = typeGroup;
    }

	//
    public java.lang.String getAddress(){
		return address;
    }

    public void setAddress(java.lang.String address){
		this.address = address;
    }

	//
    public java.lang.String getTopographic(){
		return topographic;
    }

    public void setTopographic(java.lang.String topographic){
		this.topographic = topographic;
    }

	//
    public java.lang.String getStationType(){
		return stationType;
    }

    public void setStationType(java.lang.String stationType){
		this.stationType = stationType;
    }

	//
    public java.lang.Double getCost1477(){
		return cost1477 != null ? cost1477 : 0;
    }

    public void setCost1477(java.lang.Double cost1477){
		this.cost1477 = cost1477;
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
		return getCost1477HtctId().toString();
	}

	@Override
	public Long getFWModelId() {
		// TODO Auto-generated method stub
		return cost1477HtctId;
	}

}
