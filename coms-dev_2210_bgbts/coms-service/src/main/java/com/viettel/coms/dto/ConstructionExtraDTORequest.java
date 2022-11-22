package com.viettel.coms.dto;

import java.util.List;

import com.viettel.cat.dto.ConstructionImageInfo;

public class ConstructionExtraDTORequest extends ConstructionExtraDTO {
	
	//
	private SysUserRequest userRequest;
	private List<ConstructionImageInfo> lstHandOverBuildingImages;
	private List<ConstructionImageInfo> lstHandOverElectricityImages;
	private List<ConstructionImageInfo> lstLicenseImages;
	private List<ConstructionImageInfo> lstDesignImages;
	

	
	public SysUserRequest getUserRequest() {
		return userRequest;
	}
	public void setUserRequest(SysUserRequest userRequest) {
		this.userRequest = userRequest;
	}
	public List<ConstructionImageInfo> getLstHandOverBuildingImages() {
		return lstHandOverBuildingImages;
	}
	public void setLstHandOverBuildingImages(List<ConstructionImageInfo> lstHandOverBuildingImages) {
		this.lstHandOverBuildingImages = lstHandOverBuildingImages;
	}
	public List<ConstructionImageInfo> getLstHandOverElectricityImages() {
		return lstHandOverElectricityImages;
	}
	public void setLstHandOverElectricityImages(List<ConstructionImageInfo> lstHandOverElectricityImages) {
		this.lstHandOverElectricityImages = lstHandOverElectricityImages;
	}
	public List<ConstructionImageInfo> getLstLicenseImages() {
		return lstLicenseImages;
	}
	public void setLstLicenseImages(List<ConstructionImageInfo> lstLicenseImages) {
		this.lstLicenseImages = lstLicenseImages;
	}
	public List<ConstructionImageInfo> getLstDesignImages() {
		return lstDesignImages;
	}
	public void setLstDesignImages(List<ConstructionImageInfo> lstDesignImages) {
		this.lstDesignImages = lstDesignImages;
	}
	

	
}
