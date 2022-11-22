/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.coms.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.ManageCareerBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;
//Duonghv13-start 16092021
@XmlRootElement(name = "MANAGE_CAREERBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ManageCareerDTO extends ComsBaseFWDTO<ManageCareerBO> {

    private java.lang.Long careerId;
    private java.lang.String code;
    private java.lang.String name;
    
    private java.lang.String status;
    
    @JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
    private java.util.Date createdDate;
    private java.lang.Long createdUserId;
    @JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
    private java.util.Date updatedDate;
    private java.lang.Long updatedUserId;
   
    private java.lang.String woIdList;
    
    private java.lang.String woListName;
    
    
    public ManageCareerDTO() {
    	
	}

    
	
    @Override
    public ManageCareerBO toModel() {
        ManageCareerBO manageCareerBO = new ManageCareerBO();
        manageCareerBO.setCareerId(this.careerId);
        manageCareerBO.setCode(this.code);
        manageCareerBO.setName(this.name);
        manageCareerBO.setWoIdList(this.woIdList); 
        manageCareerBO.setStatus(this.status);
        manageCareerBO.setCreatedDate(this.createdDate);
        manageCareerBO.setCreatedUserId(this.createdUserId);
        manageCareerBO.setUpdatedDate(this.updatedDate);
        manageCareerBO.setUpdatedUserId(this.updatedUserId);
        return manageCareerBO;
    }

   

    public java.lang.Long getCareerId() {
		return careerId;
	}



	public void setCareerId(java.lang.Long careerId) {
		this.careerId = careerId;
	}


	public java.lang.String getCode() {
        return code;
    }

    public void setCode(java.lang.String code) {
        this.code = code;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    
    public java.lang.String getStatus() {
        return status;
    }

    public void setStatus(java.lang.String status) {
        this.status = status;
    }


	public java.lang.String getWoIdList() {
		return woIdList;
	}



	public void setWoIdList(java.lang.String woIdList) {
		this.woIdList = woIdList;
	}


    
    public java.util.Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(java.util.Date createdDate) {
        this.createdDate = createdDate;
    }

    public java.lang.Long getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(java.lang.Long createdUserId) {
        this.createdUserId = createdUserId;
    }

   

    public java.util.Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(java.util.Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public java.lang.Long getUpdatedUserId() {
        return updatedUserId;
    }

    public void setUpdatedUserId(java.lang.Long updatedUserId) {
        this.updatedUserId = updatedUserId;
    }

    
    
    @Override
    public String catchName() {
        return getCareerId().toString();
    }



	@Override
	public Long getFWModelId() {
		// TODO Auto-generated method stub
		return careerId;
	}



	public java.lang.String getWoListName() {
		return woListName;
	}



	public void setWoListName(java.lang.String woListName) {
		this.woListName = woListName;
	}
	
//Duong -end
}
