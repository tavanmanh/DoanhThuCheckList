package com.viettel.coms.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.xml.bind.annotation.XmlRootElement;

import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.viettel.cat.dto.ConstructionImageInfo;
import com.viettel.coms.bo.EffectiveCalculateDASCDBRBO;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@XmlRootElement(name = "EffectiveCalculateDASCDBRBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EffectiveCalculateDASCDBRDTO extends ComsBaseFWDTO<EffectiveCalculateDASCDBRBO> {

	private Long effectiveCalculateDASCDBRId;
	private Long dasType;
	private Long cdbrType;
	private String houseName;
	private Double totalArea;
	private Double totalApartment;
	private String costDas;
	private String costEngineRoomCDBR;
	private String costCDBR;
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
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private java.util.Date createdDate;
	private Long createdUserId;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private java.util.Date updatedDate;
	private Long updatedUserId;
	private String effective;

	public String getEffective() {
		return effective;
	}

	public void setEffective(String effective) {
		this.effective = effective;
	}

	public Long getUpdatedUserId() {
		return updatedUserId;
	}

	public void setUpdatedUserId(Long updatedUserId) {
		this.updatedUserId = updatedUserId;
	}

	public Long getEffectiveCalculateDASCDBRId() {
		return effectiveCalculateDASCDBRId;
	}

	public void setEffectiveCalculateDASCDBRId(Long effectiveCalculateDASCDBRId) {
		this.effectiveCalculateDASCDBRId = effectiveCalculateDASCDBRId;
	}

	public Long getDasType() {
		return dasType != null ? dasType : 0;
	}

	public void setDasType(Long dasType) {
		this.dasType = dasType;
	}

	public Long getCdbrType() {
		return cdbrType != null ? cdbrType : 0;
	}

	public void setCdbrType(Long cdbrType) {
		this.cdbrType = cdbrType;
	}

	public String getHouseName() {
		return houseName;
	}

	public void setHouseName(String houseName) {
		this.houseName = houseName;
	}

	public Double getTotalArea() {
		return totalArea != null ? totalArea : 0;
	}

	public void setTotalArea(Double totalArea) {
		this.totalArea = totalArea;
	}

	public Double getTotalApartment() {
		return totalApartment != null ? totalApartment :0;
	}

	public void setTotalApartment(Double totalApartment) {
		this.totalApartment = totalApartment;
	}

	public String getCostDas() {
		return costDas;
	}

	public void setCostDas(String costDas) {
		this.costDas = costDas;
	}

	public String getCostEngineRoomCDBR() {
		return costEngineRoomCDBR;
	}

	public void setCostEngineRoomCDBR(String costEngineRoomCDBR) {
		this.costEngineRoomCDBR = costEngineRoomCDBR;
	}

	public String getCostCDBR() {
		return costCDBR;
	}

	public void setCostCDBR(String costCDBR) {
		this.costCDBR = costCDBR;
	}

	public Double getRatioRate() {
		return ratioRate != null ? ratioRate : 0;
	}

	public void setRatioRate(Double ratioRate) {
		this.ratioRate = ratioRate;
	}

	public Long getEngineRoomDas() {
		return engineRoomDas != null ? engineRoomDas : 0;
	}

	public void setEngineRoomDas(Long engineRoomDas) {
		this.engineRoomDas = engineRoomDas;
	}

	public Long getFeederAntenDas() {
		return feederAntenDas != null ? feederAntenDas : 0;
	}

	public void setFeederAntenDas(Long feederAntenDas) {
		this.feederAntenDas = feederAntenDas;
	}

	public Long getCostOtherDas() {
		return costOtherDas != null ? costOtherDas : 0;
	}

	public void setCostOtherDas(Long costOtherDas) {
		this.costOtherDas = costOtherDas;
	}

	public Long getAxisCdbr() {
		return axisCdbr != null ? axisCdbr : 0;
	}

	public void setAxisCdbr(Long axisCdbr) {
		this.axisCdbr = axisCdbr;
	}

	public Long getApartmentsAllCdbr() {
		return apartmentsAllCdbr != null ? apartmentsAllCdbr : 0;
	}

	public void setApartmentsAllCdbr(Long apartmentsAllCdbr) {
		this.apartmentsAllCdbr = apartmentsAllCdbr;
	}

	public Long getApartmentsCdbr() {
		return apartmentsCdbr != null ? apartmentsCdbr : 0;
	}

	public void setApartmentsCdbr(Long apartmentsCdbr) {
		this.apartmentsCdbr = apartmentsCdbr;
	}

	public Long getCostOtherCdbr() {
		return costOtherCdbr != null ? costOtherCdbr : 0;
	}

	public void setCostOtherCdbr(Long costOtherCdbr) {
		this.costOtherCdbr = costOtherCdbr;
	}

	public Long getEngineRoomCdbr() {
		return engineRoomCdbr != null ? engineRoomCdbr : 0;
	}

	public void setEngineRoomCdbr(Long engineRoomCdbr) {
		this.engineRoomCdbr = engineRoomCdbr;
	}

	public Long getEngineRoomCableCdbr() {
		return engineRoomCableCdbr != null ? engineRoomCableCdbr : 0;
	}

	public void setEngineRoomCableCdbr(Long engineRoomCableCdbr) {
		this.engineRoomCableCdbr = engineRoomCableCdbr;
	}

	public java.util.Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(java.util.Date createdDate) {
		this.createdDate = createdDate;
	}

	public Long getCreatedUserId() {
		return createdUserId;
	}

	public void setCreatedUserId(Long createdUserId) {
		this.createdUserId = createdUserId;
	}

	public java.util.Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(java.util.Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	@Override
	public Long getFWModelId() {
		return this.getEffectiveCalculateDASCDBRId();
	}

	@Override
	public String catchName() {
		return this.getEffectiveCalculateDASCDBRId().toString();
	}
	@Override
	public EffectiveCalculateDASCDBRBO toModel() {
		EffectiveCalculateDASCDBRBO bo = new EffectiveCalculateDASCDBRBO();
		bo.setEffectiveCalculateDASCDBRId(this.getEffectiveCalculateDASCDBRId());
		bo.setDasType(this.getDasType());
		bo.setCdbrType(this.getCdbrType());
		bo.setHouseName(this.getHouseName());
		bo.setTotalArea(this.getTotalArea());
		bo.setTotalApartment(this.getTotalApartment());
		bo.setCostDas(Double.parseDouble(this.getCostDas()));
		bo.setCostEngineRoomCDBR(Double.parseDouble(this.getCostEngineRoomCDBR()));
		bo.setCostCDBR(Double.parseDouble(this.getCostCDBR()));
		bo.setRatioRate(this.getRatioRate());
		bo.setEngineRoomDas(this.getEngineRoomDas());
		bo.setFeederAntenDas(this.getFeederAntenDas());
		bo.setCostOtherDas(this.getCostOtherDas());
		bo.setAxisCdbr(this.getAxisCdbr());
		bo.setApartmentsAllCdbr(this.getApartmentsAllCdbr());
		bo.setApartmentsCdbr(this.getApartmentsCdbr());
		bo.setCostOtherCdbr(this.getCostOtherCdbr());
		bo.setEngineRoomCdbr(this.getEngineRoomCdbr());
		bo.setEngineRoomCableCdbr(this.getEngineRoomCableCdbr());
		bo.setCreatedDate(this.getCreatedDate());
		bo.setCreatedUserId(this.getCreatedUserId());
		bo.setUpdatedDate(this.getUpdatedDate());
		bo.setUpdatedUserId(this.getUpdatedUserId());
		bo.setEffective(this.getEffective());
		return bo;
	}

}
