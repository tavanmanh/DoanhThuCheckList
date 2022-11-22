package com.viettel.coms.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.viettel.coms.dto.UserDirectoryDTO;
import com.viettel.service.base.model.BaseFWModelImpl;

@Entity
@Table(name="USER_DIRECTORY")
public class UserDirectoryBO extends BaseFWModelImpl{
	
	private Long idUserDirectory;
	private String loginName;
	private String fullName;
	private String provinceCode;
	private String position;
	private String unitName;
	private Long phone;
	
	@Id
	@GeneratedValue(generator="sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = { @org.hibernate.annotations.Parameter(name = "sequence", value = "USER_DIRECTORY_SEQ") })
	@Column(name="ID", length=11)
	public Long getIdUserDirectory() {
		return idUserDirectory;
	}

	public void setIdUserDirectory(Long idUserDirectory) {
		this.idUserDirectory = idUserDirectory;
	}
	
	@Column(name="LOGIN_NAME", length=255)
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	@Column(name="FULL_NAME", length=255)
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	@Column(name="PROVINCE_CODE", length=12)
	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	@Column(name="POSITION", length=255)
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
	
	@Column(name="UNIT_NAME", length=255)
	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	@Column(name="PHONE", length=12)
	public Long getPhone() {
		return phone;
	}

	public void setPhone(Long phone) {
		this.phone = phone;
	}

	@Override
	public UserDirectoryDTO toDTO() {
		UserDirectoryDTO dto = new UserDirectoryDTO();
		dto.setIdUserDirectory(this.getIdUserDirectory());
		dto.setLoginName(this.getLoginName());
		dto.setFullName(this.getFullName());
		dto.setPhone(this.getPhone());
		dto.setPosition(this.getPosition());
		dto.setProvinceCode(this.getProvinceCode());
		dto.setUnitName(this.getUnitName());
		
		return dto;
	}
	
}
