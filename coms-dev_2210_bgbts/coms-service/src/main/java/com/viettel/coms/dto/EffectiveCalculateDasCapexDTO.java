package com.viettel.coms.dto;


import com.viettel.coms.bo.EffectiveCalculateDasCapexBO;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import com.viettel.utils.CustomJsonDateDeserializer;
import com.viettel.utils.CustomJsonDateSerializer;

@SuppressWarnings("serial")
@XmlRootElement(name = "EffectiveCalculateDasCapexBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EffectiveCalculateDasCapexDTO extends ComsBaseFWDTO<EffectiveCalculateDasCapexBO> {

	private java.lang.Long assumptionsCapexId;
	private java.lang.String itemType;
	private java.lang.String item;
	private java.lang.String note;
	private java.lang.Double cost;
	private java.lang.String unit;
	private java.lang.Double mass;
	private java.lang.Long ssumptionsId;
	private java.lang.Long createdUserId;
	private java.lang.Long updatedUserId;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date createdDate;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date updatedDate;
	private java.lang.String name;
	@Override
	public String catchName() {
		// TODO Auto-generated method stub
		return assumptionsCapexId.toString();
	}

	@Override
	public Long getFWModelId() {
		// TODO Auto-generated method stub
		return assumptionsCapexId;
	}
	@JsonProperty("assumptionsCapexId")
	public java.lang.Long getAssumptionsCapexId() {
		return assumptionsCapexId;
	}

	public void setAssumptionsCapexId(java.lang.Long assumptionsCapexId) {
		this.assumptionsCapexId = assumptionsCapexId;
	}

	@JsonProperty("itemType")
	public java.lang.String getItemType() {
		return itemType;
	}
	public void setItemType(java.lang.String itemType) {
		this.itemType = itemType;
	}
	@JsonProperty("item")
	public java.lang.String getItem() {
		return item;
	}

	public void setItem(java.lang.String item) {
		this.item = item;
	}
	@JsonProperty("note")
	public java.lang.String getNote() {
		return note;
	}

	public void setNote(java.lang.String note) {
		this.note = note;
	}
	@JsonProperty("cost")
	public java.lang.Double getCost() {
		return cost != null ? cost : 0D;
	}

	public void setCost(java.lang.Double cost) {
		this.cost = cost;
	}
	@JsonProperty("unit")
	public java.lang.String getUnit() {
		return unit;
	}

	public void setUnit(java.lang.String unit) {
		this.unit = unit;
	}
	@JsonProperty("mass")
	public java.lang.Double getMass() {
		return mass;
	}

	public void setMass(java.lang.Double mass) {
		this.mass = mass;
	}
	@JsonProperty("ssumptionsId")
	public java.lang.Long getSsumptionsId() {
		return ssumptionsId;
	}

	public void setSsumptionsId(java.lang.Long ssumptionsId) {
		this.ssumptionsId = ssumptionsId;
	}
	@JsonProperty("createdUserId")
	public java.lang.Long getCreatedUserId() {
		return createdUserId;
	}

	public void setCreatedUserId(java.lang.Long createdUserId) {
		this.createdUserId = createdUserId;
	}
	@JsonProperty("updatedUserId")
	public java.lang.Long getUpdatedUserId() {
		return updatedUserId;
	}

	public void setUpdatedUserId(java.lang.Long updatedUserId) {
		this.updatedUserId = updatedUserId;
	}
	@JsonProperty("createdDate")
	public java.util.Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(java.util.Date createdDate) {
		this.createdDate = createdDate;
	}
	@JsonProperty("updatedDate")
	public java.util.Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(java.util.Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	@JsonProperty("name")
	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	@Override
	public EffectiveCalculateDasCapexBO toModel() {
		EffectiveCalculateDasCapexBO effectiveCalculateDasCapexBO = new EffectiveCalculateDasCapexBO();
		effectiveCalculateDasCapexBO.setAssumptionsCapexId(this.assumptionsCapexId);
		effectiveCalculateDasCapexBO.setItemType(this.itemType);
		effectiveCalculateDasCapexBO.setItem(this.item);
		effectiveCalculateDasCapexBO.setCost(this.cost);
		effectiveCalculateDasCapexBO.setNote(this.note);
		effectiveCalculateDasCapexBO.setUnit(this.unit);
		effectiveCalculateDasCapexBO.setMass(this.mass);
		effectiveCalculateDasCapexBO.setSsumptionsId(this.ssumptionsId);
		effectiveCalculateDasCapexBO.setCreatedUserId(this.createdUserId);
		effectiveCalculateDasCapexBO.setUpdatedUserId(this.updatedUserId);
		effectiveCalculateDasCapexBO.setCreatedDate(this.createdDate);
		effectiveCalculateDasCapexBO.setUpdatedDate(this.updatedDate);
		return effectiveCalculateDasCapexBO;
	}



}
