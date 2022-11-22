package com.viettel.coms.dto;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.WaccHtctBO;
import com.viettel.erp.utils.CustomJsonDateDeserializer;
import com.viettel.erp.utils.CustomJsonDateSerializer;

@SuppressWarnings("serial")
@XmlRootElement(name = "WACC_HTCTBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class WaccHtctDTO extends ComsBaseFWDTO<WaccHtctBO> {

	private java.lang.Long waccId;

	private java.lang.String waccName;

	private java.lang.Double waccRex;

	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date createdDate;

	private java.lang.Long createdUserId;

	private java.lang.Long updateUserId;

	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date updatedDate;


    @Override
    public WaccHtctBO toModel() {
        WaccHtctBO waccHtctBO = new WaccHtctBO();
        waccHtctBO.setWaccId(this.waccId);
        waccHtctBO.setWaccName(this.waccName);
        waccHtctBO.setWaccRex(this.waccRex);
        waccHtctBO.setCreatedDate(this.createdDate);
        waccHtctBO.setCreatedUserId(this.createdUserId);
        waccHtctBO.setUpdateUserId(this.updateUserId);
        waccHtctBO.setUpdatedDate(this.updatedDate);
        return waccHtctBO;
    }

    public java.lang.Long getWaccId(){
		return waccId;
    }

    public void setWaccId(java.lang.Long waccId){
		this.waccId = waccId;
    }

    public java.lang.String getWaccName(){
		return waccName;
    }

    public void setWaccName(java.lang.String waccName){
		this.waccName = waccName;
    }

	//
    public java.lang.Double getWaccRex(){
		return waccRex != null ? waccRex : 0;
    }

    public void setWaccRex(java.lang.Double waccRex){
		this.waccRex = waccRex;
    }

	//
    public java.util.Date getCreatedDate(){
		return createdDate;
    }

    public void setCreatedDate(java.util.Date createdDate){
		this.createdDate = createdDate;
    }

	//

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

	//

	@Override
	public String catchName() {
		return getWaccId().toString();
	}

	@Override
	public Long getFWModelId() {
		return waccId;
	}

}
