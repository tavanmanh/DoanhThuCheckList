package com.viettel.coms.dto;

import com.viettel.asset.dto.ResultInfo;
import com.viettel.cat.dto.ConstructionImageInfo;

import java.util.List;

public class TangentCustomerResponse {
	private ResultInfo resultInfo;
	private List<TangentCustomerDTO> lstTangentCustomerDTO;
	private TangentCustomerDTO tangentCustomerDTO;
	private List<ConstructionImageInfo> listImage;
	private List<TangentCustomerDTO> areaProvinceCity;
	private List<TangentCustomerDTO> areaDistrict;
	private List<TangentCustomerDTO> areaWard;

	public List<TangentCustomerDTO> getAreaProvinceCity() {
		return areaProvinceCity;
	}

	public void setAreaProvinceCity(List<TangentCustomerDTO> areaProvinceCity) {
		this.areaProvinceCity = areaProvinceCity;
	}

	public List<TangentCustomerDTO> getAreaDistrict() {
		return areaDistrict;
	}

	public void setAreaDistrict(List<TangentCustomerDTO> areaDistrict) {
		this.areaDistrict = areaDistrict;
	}

	public List<TangentCustomerDTO> getAreaWard() {
		return areaWard;
	}

	public void setAreaWard(List<TangentCustomerDTO> areaWard) {
		this.areaWard = areaWard;
	}

	public List<ConstructionImageInfo> getListImage() {
		return listImage;
	}

	public void setListImage(List<ConstructionImageInfo> listImage) {
		this.listImage = listImage;
	}

	public ResultInfo getResultInfo() {
		return resultInfo;
	}

	public void setResultInfo(ResultInfo resultInfo) {
		this.resultInfo = resultInfo;
	}

	public TangentCustomerDTO getTangentCustomerDTO() {
		return tangentCustomerDTO;
	}

	public void setTangentCustomerDTO(TangentCustomerDTO tangentCustomerDTO) {
		this.tangentCustomerDTO = tangentCustomerDTO;
	}

	public List<TangentCustomerDTO> getLstTangentCustomerDTO() {
		return lstTangentCustomerDTO;
	}

	public void setLstTangentCustomerDTO(
			List<TangentCustomerDTO> lstTangentCustomerDTO) {
		this.lstTangentCustomerDTO = lstTangentCustomerDTO;
	}

}
