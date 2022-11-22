/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.erp.dto;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

/**
 *
 * @author thinhnd
 */
@XmlRootElement(name = "RoleConfigProvinceUserCDTBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleConfigProvinceUserCDTDTO extends BaseFWDTOImpl<BaseFWModelImpl> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1153401569491550624L;
	private java.lang.Long roleConfigProvinceUserCDTId;
	private java.lang.Long roleConfigProvinceId;
	private java.lang.Long sysUserId;
	private java.lang.String fullName;
	private java.lang.String email;
	private java.lang.String phoneNumber;
	private java.lang.Long catProvinceId;
	private java.lang.String catProvinceCode;
	private java.lang.String catProvinceName;
	
	public java.lang.String getCatProvinceName() {
		return catProvinceName;
	}

	public void setCatProvinceName(java.lang.String catProvinceName) {
		this.catProvinceName = catProvinceName;
	}

	public java.lang.Long getRoleConfigProvinceId() {
		return roleConfigProvinceId;
	}

	public void setRoleConfigProvinceId(java.lang.Long roleConfigProvinceId) {
		this.roleConfigProvinceId = roleConfigProvinceId;
	}

	public java.lang.Long getCatProvinceId() {
		return catProvinceId;
	}

	public void setCatProvinceId(java.lang.Long catProvinceId) {
		this.catProvinceId = catProvinceId;
	}

	public java.lang.String getCatProvinceCode() {
		return catProvinceCode;
	}

	public void setCatProvinceCode(java.lang.String catProvinceCode) {
		this.catProvinceCode = catProvinceCode;
	}

	public java.lang.Long getRoleConfigProvinceUserCDTId() {
		return roleConfigProvinceUserCDTId;
	}

	public void setRoleConfigProvinceUserCDTId(java.lang.Long roleConfigProvinceUserCDTId) {
		this.roleConfigProvinceUserCDTId = roleConfigProvinceUserCDTId;
	}

	public java.lang.Long getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(java.lang.Long sysUserId) {
		this.sysUserId = sysUserId;
	}

	public java.lang.String getFullName() {
		return fullName;
	}

	public void setFullName(java.lang.String fullName) {
		this.fullName = fullName;
	}

	public java.lang.String getEmail() {
		return email;
	}

	public void setEmail(java.lang.String email) {
		this.email = email;
	}

	public java.lang.String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(java.lang.String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	@Override
	public Long getFWModelId() {
		return roleConfigProvinceId;
	}

	@Override
	public String catchName() {
		return getRoleConfigProvinceId().toString();
	}

	@Override
	public BaseFWModelImpl toModel() {
		return null;
	}

	

}
