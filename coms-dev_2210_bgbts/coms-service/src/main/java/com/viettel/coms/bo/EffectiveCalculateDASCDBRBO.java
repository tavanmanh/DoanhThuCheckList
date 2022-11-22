package com.viettel.coms.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;

import com.viettel.coms.dto.EffectiveCalculateDASCDBRDTO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;
import com.viettel.service.base.model.BaseFWModelImpl;

@Entity
@Table(name = "EFFECTIVE_CALCULATE_DAS_CDBR")
public class EffectiveCalculateDASCDBRBO extends BaseFWModelImpl {

	private Long effectiveCalculateDASCDBRId;
	private Long dasType;
	private Long cdbrType;
	private String houseName;
	private Double totalArea;
	private Double totalApartment;
	private Double costDas;
	private Double costEngineRoomCDBR;
	private Double costCDBR;
	private Double ratioRate;
	private Long engineRoomDas;
	private Long feederAntenDas;
	private Long costOtherDas;
	private Long axisCdbr;
	private Long apartmentsAllCdbr;
	private Long apartmentsCdbr;
	private Long costOtherCdbr;
	private Long engineRoomCdbr;
	private Long engineRoomCableCdbr;
	private Long createdUserId;
	private Long updatedUserId;
	private java.util.Date createdDate;
	private java.util.Date updatedDate;
	private String effective;

	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = { @org.hibernate.annotations.Parameter(name = "sequence", value = "Effective_Calculate_DAS_CDBR_seq") })
	@Column(name = "EFFECTIVE_CALCULATE_DAS_CDBR_ID", length = 11)
	public Long getEffectiveCalculateDASCDBRId() {
		return effectiveCalculateDASCDBRId;
	}

	public void setEffectiveCalculateDASCDBRId(Long effectiveCalculateDASCDBRId) {
		this.effectiveCalculateDASCDBRId = effectiveCalculateDASCDBRId;
	}
	@Column(name = "DAS_TYPE", length = 11)
	public Long getDasType() {
		return dasType;
	}

	public void setDasType(Long dasType) {
		this.dasType = dasType;
	}
	@Column(name = "CDBR_TYPE", length = 11)
	public Long getCdbrType() {
		return cdbrType;
	}

	public void setCdbrType(Long cdbrType) {
		this.cdbrType = cdbrType;
	}
	@Column(name = "HOUSE_NAME", length = 2000)
	public String getHouseName() {
		return houseName;
	}

	public void setHouseName(String houseName) {
		this.houseName = houseName;
	}
	@Column(name = "TOTAL_AREA", length = 24)
	public Double getTotalArea() {
		return totalArea;
	}

	public void setTotalArea(Double totalArea) {
		this.totalArea = totalArea;
	}
	@Column(name = "TOTAL_APARTMENTS", length = 24)
	public Double getTotalApartment() {
		return totalApartment;
	}

	public void setTotalApartment(Double totalApartment) {
		this.totalApartment = totalApartment;
	}
	@Column(name = "COST_DAS", length = 24)
	public Double getCostDas() {
		return costDas;
	}

	public void setCostDas(Double costDas) {
		this.costDas = costDas;
	}
	@Column(name = "COST_ENGINE_ROOM_CDBR", length = 24)
	public Double getCostEngineRoomCDBR() {
		return costEngineRoomCDBR;
	}

	public void setCostEngineRoomCDBR(Double costEngineRoomCDBR) {
		this.costEngineRoomCDBR = costEngineRoomCDBR;
	}
	@Column(name = "COST_CDBR", length = 24)
	public Double getCostCDBR() {
		return costCDBR;
	}

	public void setCostCDBR(Double costCDBR) {
		this.costCDBR = costCDBR;
	}
	@Column(name = "RATIO_RATE", length = 24)
	public Double getRatioRate() {
		return ratioRate;
	}

	public void setRatioRate(Double ratioRate) {
		this.ratioRate = ratioRate;
	}
	@Column(name = "ENGINE_ROOM_DAS", length = 24)
	public Long getEngineRoomDas() {
		return engineRoomDas;
	}

	public void setEngineRoomDas(Long engineRoomDas) {
		this.engineRoomDas = engineRoomDas;
	}
	@Column(name = "FEEDER_ANTEN_DAS", length = 24)
	public Long getFeederAntenDas() {
		return feederAntenDas;
	}

	public void setFeederAntenDas(Long feederAntenDas) {
		this.feederAntenDas = feederAntenDas;
	}
	@Column(name = "COST_OTHER_DAS", length = 24)
	public Long getCostOtherDas() {
		return costOtherDas;
	}

	public void setCostOtherDas(Long costOtherDas) {
		this.costOtherDas = costOtherDas;
	}
	@Column(name = "AXIS_CDBR", length = 24)
	public Long getAxisCdbr() {
		return axisCdbr;
	}

	public void setAxisCdbr(Long axisCdbr) {
		this.axisCdbr = axisCdbr;
	}
	@Column(name = "APARTMENTS_ALL_CDBR", length = 24)
	public Long getApartmentsAllCdbr() {
		return apartmentsAllCdbr;
	}

	public void setApartmentsAllCdbr(Long apartmentsAllCdbr) {
		this.apartmentsAllCdbr = apartmentsAllCdbr;
	}
	@Column(name = "APARTMENTS_CDBR", length = 24)
	public Long getApartmentsCdbr() {
		return apartmentsCdbr;
	}

	public void setApartmentsCdbr(Long apartmentsCdbr) {
		this.apartmentsCdbr = apartmentsCdbr;
	}
	@Column(name = "COST_OTHER_CDBR", length = 24)
	public Long getCostOtherCdbr() {
		return costOtherCdbr;
	}

	public void setCostOtherCdbr(Long costOtherCdbr) {
		this.costOtherCdbr = costOtherCdbr;
	}
	@Column(name = "ENGINE_ROOM_CDBR", length = 24)
	public Long getEngineRoomCdbr() {
		return engineRoomCdbr;
	}

	public void setEngineRoomCdbr(Long engineRoomCdbr) {
		this.engineRoomCdbr = engineRoomCdbr;
	}
	@Column(name = "ENGINE_ROOM_CABLE_CDBR", length = 24)
	public Long getEngineRoomCableCdbr() {
		return engineRoomCableCdbr;
	}

	public void setEngineRoomCableCdbr(Long engineRoomCableCdbr) {
		this.engineRoomCableCdbr = engineRoomCableCdbr;
	}
	@Column(name = "CREATED_USER_ID", length = 24)
	public Long getCreatedUserId() {
		return createdUserId;
	}

	public void setCreatedUserId(Long createdUserId) {
		this.createdUserId = createdUserId;
	}
	@Column(name = "UPDATE_USER_ID", length = 24)
	public Long getUpdatedUserId() {
		return updatedUserId;
	}

	public void setUpdatedUserId(Long updatedUserId) {
		this.updatedUserId = updatedUserId;
	}
	@Column(name = "CREATED_DATE", length = 24)
	public java.util.Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(java.util.Date createdDate) {
		this.createdDate = createdDate;
	}
	@Column(name = "UPDATED_DATE", length = 24)
	public java.util.Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(java.util.Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	@Column(name = "EFFECTIVE", length = 24)
	public String getEffective() {
		return effective;
	}

	public void setEffective(String effective) {
		this.effective = effective;
	}

	@Override
	public EffectiveCalculateDASCDBRDTO toDTO() {
		EffectiveCalculateDASCDBRDTO dto = new EffectiveCalculateDASCDBRDTO();
		dto.setEffectiveCalculateDASCDBRId(this.getEffectiveCalculateDASCDBRId());
		dto.setDasType(this.getDasType());
		dto.setCdbrType(this.getCdbrType());
		dto.setHouseName(this.getHouseName());
		dto.setTotalArea(this.getTotalArea());
		dto.setTotalApartment(this.getTotalApartment());
		dto.setCostDas(this.getCostDas().toString());
		dto.setCostEngineRoomCDBR(this.getCostEngineRoomCDBR().toString());
		dto.setCostCDBR(this.getCostCDBR().toString());
		dto.setRatioRate(this.getRatioRate());
		dto.setEngineRoomDas(this.getEngineRoomDas());
		dto.setFeederAntenDas(this.getFeederAntenDas());
		dto.setCostOtherDas(this.getCostOtherDas());
		dto.setAxisCdbr(this.getAxisCdbr());
		dto.setApartmentsAllCdbr(this.getApartmentsAllCdbr());
		dto.setApartmentsCdbr(this.getApartmentsCdbr());
		dto.setCostOtherCdbr(this.getCostOtherCdbr());
		dto.setEngineRoomCdbr(this.getEngineRoomCdbr());
		dto.setEngineRoomCableCdbr(this.getEngineRoomCableCdbr());
		dto.setCreatedDate(this.getCreatedDate());
		dto.setCreatedUserId(this.getCreatedUserId());
		dto.setUpdatedDate(this.getUpdatedDate());
		dto.setUpdatedUserId(this.getUpdatedUserId());
		dto.setEffective(this.getEffective());
		return dto;
	}
}
