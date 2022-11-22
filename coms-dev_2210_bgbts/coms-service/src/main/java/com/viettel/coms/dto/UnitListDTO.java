package com.viettel.coms.dto;

import javax.xml.bind.annotation.XmlRootElement;

import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.viettel.coms.bo.UnitListBO;
import com.viettel.coms.bo.UserHolidayBO;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@XmlRootElement(name = "UNIT_LISTBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UnitListDTO extends ComsBaseFWDTO<UnitListBO> {

	private Long idUnit;
	private String unitCode;
	private String unitName;
	private String areaCode;
	
	public Long getIdUnit() {
		return idUnit;
	}

	public void setIdUnit(Long idUnit) {
		this.idUnit = idUnit;
	}

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	@Override
	public String catchName() {
		return this.getIdUnit().toString();
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	@Override
	public Long getFWModelId() {
		return this.getId();
	}
	
	@Override
	public UnitListBO toModel() {
		UnitListBO bo = new UnitListBO();
		bo.setIdUnit(this.getId());
		bo.setUnitCode(this.getUnitCode());
		bo.setUnitName(this.getUnitName());
		return bo;
	}

		

}
