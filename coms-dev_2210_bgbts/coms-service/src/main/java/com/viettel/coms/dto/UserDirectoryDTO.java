package com.viettel.coms.dto;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.viettel.coms.bo.UserDirectoryBO;

@XmlRootElement(name = "USER_DIRECTORYBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDirectoryDTO extends ComsBaseFWDTO<UserDirectoryBO>{
	
	private Long idUserDirectory;
	private String loginName;
	private String fullName;
	private String provinceCode;
	private String unitName;
	private String position;
	private Long phone;
	private String email;
	
	public UserDirectoryDTO() {
		this.idUserDirectory = this.getId();
	}

	public UserDirectoryDTO(String loginName, String fullName, String proviceCode, String unitName, Long phone) {
		super();
		this.loginName = loginName;
		this.fullName = fullName;
		this.provinceCode = proviceCode;
		this.unitName = unitName;
		this.phone = phone;
	}
	
	public Long getIdUserDirectory() {
		return idUserDirectory;
	}

	public void setIdUserDirectory(Long idUserDirectory) {
		this.idUserDirectory = idUserDirectory;
	}

	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String proviceCode) {
		this.provinceCode = proviceCode;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public Long getPhone() {
		return phone;
	}
	public void setPhone(Long phone) {
		this.phone = phone;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	@Override
	public String catchName() {
		return this.getIdUserDirectory().toString();
	}

	@Override
	public Long getFWModelId() {
		return this.getId();
	}

	@Override
	public UserDirectoryBO toModel() {
		UserDirectoryBO bo = new UserDirectoryBO();
		
		bo.setIdUserDirectory(this.getId());
		bo.setLoginName(this.getLoginName());
		bo.setFullName(this.getFullName());
		bo.setPhone(this.getPhone());
		bo.setPosition(this.getPosition());
		bo.setProvinceCode(this.getProvinceCode());
		bo.setUnitName(this.getUnitName());
		
		return bo;
	}
	
	
}
