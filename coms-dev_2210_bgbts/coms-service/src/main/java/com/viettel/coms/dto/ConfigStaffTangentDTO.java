package com.viettel.coms.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.ConfigStaffTangentBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;

@XmlRootElement(name = "CONFIG_STAFF_TANGENTBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfigStaffTangentDTO extends ComsBaseFWDTO<ConfigStaffTangentBO>{

	private Long configStaffTangentId;
	private Long catProvinceId;
	private String provinceCode;
	private String type;
	private Long staffId;
	private String staffCode;
	private String staffName;
	private String staffPhone;
	private String status;
	private Long createdUser;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date createdDate;
	private Long updatedUser;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date updatedDate;
	private String email;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getConfigStaffTangentId() {
		return configStaffTangentId;
	}

	public void setConfigStaffTangentId(Long configStaffTangentId) {
		this.configStaffTangentId = configStaffTangentId;
	}

	public Long getCatProvinceId() {
		return catProvinceId;
	}

	public void setCatProvinceId(Long catProvinceId) {
		this.catProvinceId = catProvinceId;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}

	public String getStaffCode() {
		return staffCode;
	}

	public void setStaffCode(String staffCode) {
		this.staffCode = staffCode;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getStaffPhone() {
		return staffPhone;
	}

	public void setStaffPhone(String staffPhone) {
		this.staffPhone = staffPhone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(Long createdUser) {
		this.createdUser = createdUser;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Long getUpdatedUser() {
		return updatedUser;
	}

	public void setUpdatedUser(Long updatedUser) {
		this.updatedUser = updatedUser;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Override
	public String catchName() {
		// TODO Auto-generated method stub
		return configStaffTangentId.toString();
	}

	@Override
	public Long getFWModelId() {
		// TODO Auto-generated method stub
		return configStaffTangentId;
	}

	@Override
	public ConfigStaffTangentBO toModel() {
		ConfigStaffTangentBO bo = new ConfigStaffTangentBO();
		bo.setConfigStaffTangentId(this.getConfigStaffTangentId());
		bo.setCatProvinceId(this.getCatProvinceId());
		bo.setProvinceCode(this.getProvinceCode());
		bo.setType(this.getType());
		bo.setStaffId(this.getStaffId());
		bo.setStaffCode(this.getStaffCode());
		bo.setStaffName(this.getStaffName());
		bo.setStaffPhone(this.getStaffPhone());
		bo.setStatus(this.getStatus());
		bo.setCreatedUser(this.getCreatedUser());
		bo.setCreatedDate(this.getCreatedDate());
		bo.setUpdatedUser(this.getUpdatedUser());
		bo.setUpdatedDate(this.getUpdatedDate());
		bo.setEmail(this.email);
		return bo;
	}

}
