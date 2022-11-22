package com.viettel.coms.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.viettel.coms.dto.UnitListDTO;
import com.viettel.service.base.model.BaseFWModelImpl;

@Entity
@Table(name = "UNIT_LIST")
public class UnitListBO extends BaseFWModelImpl {
	
	private Long idUnit;
	private String unitCode;
	private String unitName;
	
	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = { @org.hibernate.annotations.Parameter(name = "sequence", value = "UNIT_LIST_SEQ") })
	@Column(name = "ID", length = 11)
	public Long getIdUnit() {
		return idUnit;
	}

	public void setIdUnit(Long idUnit) {
		this.idUnit = idUnit;
	}
	
	@Column(name = "UNIT_CODE", length = 255)
	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	@Column(name = "UNIT_NAME", length = 255)
	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	@Override
	public UnitListDTO toDTO() {
		UnitListDTO dto = new UnitListDTO();
		dto.setIdUnit(this.getIdUnit());
		dto.setUnitCode(this.getUnitCode());
		dto.setUnitName(this.getUnitName());
		return dto;
	}
}
