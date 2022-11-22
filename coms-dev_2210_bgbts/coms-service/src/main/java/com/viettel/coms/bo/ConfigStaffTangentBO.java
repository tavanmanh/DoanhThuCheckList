package com.viettel.coms.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.ConfigStaffTangentDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

@Entity
@Table(name = "CONFIG_STAFF_TANGENT")
public class ConfigStaffTangentBO extends BaseFWModelImpl {

	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "CONFIG_STAFF_TANGENT_SEQ") })
	@Column(name = "CONFIG_STAFF_TANGENT_ID", length = 10)
	private Long configStaffTangentId;
	@Column(name = "CAT_PROVINCE_ID", length = 10)
	private Long catProvinceId;
	@Column(name = "PROVINCE_CODE", length = 10)
	private String provinceCode;
	@Column(name = "TYPE", length = 2)
	private String type;
	@Column(name = "STAFF_ID", length = 10)
	private Long staffId;
	@Column(name = "STAFF_CODE", length = 100)
	private String staffCode;
	@Column(name = "STAFF_NAME", length = 100)
	private String staffName;
	@Column(name = "STAFF_PHONE", length = 20)
	private String staffPhone;
	@Column(name = "STATUS", length = 2)
	private String status;
	@Column(name = "CREATED_USER", length = 10)
	private Long createdUser;
	@Column(name = "CREATED_DATE", length = 22)
	private Date createdDate;
	@Column(name = "UPDATED_USER", length = 10)
	private Long updatedUser;
	@Column(name = "UPDATED_DATE", length = 22)
	private Date updatedDate;
	@Column(name = "EMAIL", length = 20)
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
	public BaseFWDTOImpl toDTO() {
		ConfigStaffTangentDTO dto = new ConfigStaffTangentDTO();
		dto.setConfigStaffTangentId(this.getConfigStaffTangentId());
		dto.setCatProvinceId(this.getCatProvinceId());
		dto.setProvinceCode(this.getProvinceCode());
		dto.setType(this.getType());
		dto.setStaffId(this.getStaffId());
		dto.setStaffCode(this.getStaffCode());
		dto.setStaffName(this.getStaffName());
		dto.setStaffPhone(this.getStaffPhone());
		dto.setStatus(this.getStatus());
		dto.setCreatedUser(this.getCreatedUser());
		dto.setCreatedDate(this.getCreatedDate());
		dto.setUpdatedUser(this.getUpdatedUser());
		dto.setUpdatedDate(this.getUpdatedDate());
		dto.setEmail(this.email);
		return dto;
	}

}
