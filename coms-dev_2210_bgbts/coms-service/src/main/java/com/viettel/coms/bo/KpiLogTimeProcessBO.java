/*
 * Copyright 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.coms.bo;

import com.viettel.coms.dto.KpiLogDTO;
import com.viettel.coms.dto.KpiLogTimeProcessDTO;
import com.viettel.service.base.model.BaseFWModelImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "KPI_LOG_TIME_PROCESS")
/**
 *
 * @author: ThuanNHT
 * @version: 1.0
 * @since: 1.0
 */
public class KpiLogTimeProcessBO extends BaseFWModelImpl {

    private java.lang.Long id;
    private java.lang.String appCode;
    private java.lang.String funcCode;
    private java.lang.String serviceCode;
    private Date startTime;
    private Date endTime;
    private java.lang.Long startTimeProcess;
    private java.lang.Long endTimeProcess;
    private java.lang.Long processTime;
    private String userName;

   
  

    @Override
    public KpiLogTimeProcessDTO toDTO() {
    	KpiLogTimeProcessDTO bo = new KpiLogTimeProcessDTO();
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

    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "KPI_LOG_TIME_PROCESS_SEQ")})
    @Column(name = "ID", length = 22)
	public java.lang.Long getId() {
		return id;
	}


	public void setId(java.lang.Long id) {
		this.id = id;
	}
	
	

	 @Column(name = "USER_NAME", length = 22)
	  public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "SERVICE_CODE", length = 22)
	public java.lang.String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(java.lang.String serviceCode) {
		this.serviceCode = serviceCode;
	}

	@Column(name = "APP_CODE", length = 22)
	public java.lang.String getAppCode() {
		return appCode;
	}


	public void setAppCode(java.lang.String appCode) {
		this.appCode = appCode;
	}

	
	


	

	@Column(name = "FUNC_CODE", length = 50)
	public java.lang.String getFuncCode() {
		return funcCode;
	}

	public void setFuncCode(java.lang.String funcCode) {
		this.funcCode = funcCode;
	}
	@Column(name = "START_TIME", length = 22)
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Column(name = "END_TIME", length = 22)
	public Date getEndTime() {
		return endTime;
	}


	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Column(name = "START_TIME_PROCESS", length = 22)
	public java.lang.Long getStartTimeProcess() {
		return startTimeProcess;
	}


	public void setStartTimeProcess(java.lang.Long startTimeProcess) {
		this.startTimeProcess = startTimeProcess;
	}

	@Column(name = "END_TIME_PROCESS", length = 22)
	public java.lang.Long getEndTimeProcess() {
		return endTimeProcess;
	}


	public void setEndTimeProcess(java.lang.Long endTimeProcess) {
		this.endTimeProcess = endTimeProcess;
	}

	@Column(name = "PROCESS_TIME", length = 22)
	public java.lang.Long getProcessTime() {
		return processTime;
	}


	public void setProcessTime(java.lang.Long processTime) {
		this.processTime = processTime;
	}
}
