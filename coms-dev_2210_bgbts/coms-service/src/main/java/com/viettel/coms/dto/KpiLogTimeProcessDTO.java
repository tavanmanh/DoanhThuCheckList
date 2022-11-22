/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.coms.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.KpiLogBO;
import com.viettel.coms.bo.KpiLogTimeProcessBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;

/**
 * @author thuannht
 */
@XmlRootElement(name = "KPI_LOG_TIME_PROCESSBO")
public class KpiLogTimeProcessDTO extends ComsBaseFWDTO<KpiLogTimeProcessBO> {

	  private java.lang.Long id;
	    private java.lang.String appCode;
	    private java.lang.String funcCode;
	    private java.lang.String serviceCode;
	    @JsonSerialize(using = JsonDateSerializerDate.class)
		@JsonDeserialize(using = JsonDateDeserializer.class)
	    private Date startTime;
	    @JsonSerialize(using = JsonDateSerializerDate.class)
		@JsonDeserialize(using = JsonDateDeserializer.class)
	    private Date endTime;
	    private java.lang.Long startTimeProcess;
	    private java.lang.Long endTimeProcess;
	    private java.lang.Long processTime;
	    private String userName;

    @Override
    public KpiLogTimeProcessBO toModel() {
    	KpiLogTimeProcessBO bo = new KpiLogTimeProcessBO();
    	bo.setId(this.id);
    	bo.setAppCode(this.appCode);
    	bo.setFuncCode(this.funcCode);
    	bo.setStartTime(this.startTime);
    	bo.setEndTime(this.endTime);
    	bo.setStartTimeProcess(this.startTimeProcess);
    	bo.setEndTimeProcess(this.endTimeProcess);
    	bo.setProcessTime(this.processTime);
    	bo.setServiceCode(this.serviceCode);
    	bo.setUserName(this.userName);
        return bo;
    }
    
    
    
    public String getUserName() {
		return userName;
	}



	public void setUserName(String userName) {
		this.userName = userName;
	}



	public java.lang.String getServiceCode() {
		return serviceCode;
	}



	public void setServiceCode(java.lang.String serviceCode) {
		this.serviceCode = serviceCode;
	}



	@Override
    public String catchName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long getFWModelId() {
        // TODO Auto-generated method stub
        return null;
    }


	public java.lang.Long getId() {
		return id;
	}


	public void setId(java.lang.Long id) {
		this.id = id;
	}


	public java.lang.String getAppCode() {
		return appCode;
	}


	public void setAppCode(java.lang.String appCode) {
		this.appCode = appCode;
	}


	public java.lang.String getFuncCode() {
		return funcCode;
	}


	public void setFuncCode(java.lang.String funcCode) {
		this.funcCode = funcCode;
	}


	public Date getStartTime() {
		return startTime;
	}


	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}


	public Date getEndTime() {
		return endTime;
	}


	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}


	public java.lang.Long getStartTimeProcess() {
		return startTimeProcess;
	}


	public void setStartTimeProcess(java.lang.Long startTimeProcess) {
		this.startTimeProcess = startTimeProcess;
	}


	public java.lang.Long getEndTimeProcess() {
		return endTimeProcess;
	}


	public void setEndTimeProcess(java.lang.Long endTimeProcess) {
		this.endTimeProcess = endTimeProcess;
	}


	public java.lang.Long getProcessTime() {
		return processTime;
	}


	public void setProcessTime(java.lang.Long processTime) {
		this.processTime = processTime;
	}
    
}
