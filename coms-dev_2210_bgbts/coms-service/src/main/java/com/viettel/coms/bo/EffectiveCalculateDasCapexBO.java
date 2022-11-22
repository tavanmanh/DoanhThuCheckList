package com.viettel.coms.bo;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import com.viettel.coms.dto.EffectiveCalculateDasCapexDTO;
import com.viettel.service.base.model.BaseFWModelImpl;

@SuppressWarnings("serial")
@Entity(name = "com.viettel.coms.bo.EffectiveCalculateDasCapexBO")
@Table(name = "CTCT_COMS_OWNER.ASSUMPTIONS_CAPEX")

public class EffectiveCalculateDasCapexBO extends BaseFWModelImpl {
	
	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = { @Parameter(name = "sequence", value = "ASSUMPTIONS_CAPEX_SEQ") })
	@Column(name = "ASSUMPTIONS_CAPEX_ID", length = 22)
	private java.lang.Long assumptionsCapexId;
	@Column(name = "ITEM_TYPE", length = 2000)
	private java.lang.String itemType;
	@Column(name = "ITEM", length = 2000)
	private java.lang.String item;	
	@Column(name = "NOTE", length = 2000)
	private java.lang.String note;	
	@Column(name = "COST", length = 22)
	private java.lang.Double cost;
	@Column(name = "UNIT", length = 100)
	private java.lang.String unit;	
	@Column(name = "MASS", length = 22)
	private java.lang.Double mass;
	@Column(name = "CREATED_USER_ID", length = 22)
	private java.lang.Long createdUserId;	
	@Column(name = "UPDATED_USER_ID", length = 22)
	private java.lang.Long updatedUserId;
	@Column(name = "CREATED_DATE", length = 7)
	private java.util.Date createdDate;	
	@Column(name = "UPDATED_DATE", length = 7)
	private java.util.Date updatedDate;
	@Column(name = "SSUMPTIONS_ID", length = 22)
	private java.lang.Long ssumptionsId;
	
	public java.lang.Long getAssumptionsCapexId() {
		return assumptionsCapexId;
	}

	public void setAssumptionsCapexId(java.lang.Long assumptionsCapexId) {
		this.assumptionsCapexId = assumptionsCapexId;
	}

	public java.lang.String getItemType() {
		return itemType;
	}

	public void setItemType(java.lang.String itemType) {
		this.itemType = itemType;
	}
	
	public java.lang.String getItem() {
		return item;
	}

	public void setItem(java.lang.String item) {
		this.item = item;
	}
	
	public java.lang.String getNote() {
		return note;
	}

	public void setNote(java.lang.String note) {
		this.note = note;
	}

	public java.lang.Double getCost() {
		return cost;
	}

	public void setCost(java.lang.Double cost) {
		this.cost = cost;
	}

	public java.lang.String getUnit() {
		return unit;
	}

	public void setUnit(java.lang.String unit) {
		this.unit = unit;
	}

	public java.lang.Double getMass() {
		return mass;
	}

	public void setMass(java.lang.Double mass) {
		this.mass = mass;
	}

	public java.lang.Long getCreatedUserId() {
		return createdUserId;
	}

	public void setCreatedUserId(java.lang.Long createdUserId) {
		this.createdUserId = createdUserId;
	}

	public java.lang.Long getUpdatedUserId() {
		return updatedUserId;
	}

	public void setUpdatedUserId(java.lang.Long updatedUserId) {
		this.updatedUserId = updatedUserId;
	}

	public java.util.Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(java.util.Date createdDate) {
		this.createdDate = createdDate;
	}

	public java.util.Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(java.util.Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public java.lang.Long getSsumptionsId() {
		return ssumptionsId;
	}

	public void setSsumptionsId(java.lang.Long ssumptionsId) {
		this.ssumptionsId = ssumptionsId;
	}

	@Override
	public EffectiveCalculateDasCapexDTO toDTO() {
		EffectiveCalculateDasCapexDTO effectiveCalculateDasCapexDTO = new EffectiveCalculateDasCapexDTO();
		effectiveCalculateDasCapexDTO.setAssumptionsCapexId(this.assumptionsCapexId);
		effectiveCalculateDasCapexDTO.setItemType(this.itemType);
		effectiveCalculateDasCapexDTO.setItem(this.item);
		effectiveCalculateDasCapexDTO.setCost(this.cost);
		effectiveCalculateDasCapexDTO.setNote(this.note);
		effectiveCalculateDasCapexDTO.setUnit(this.unit);
		effectiveCalculateDasCapexDTO.setMass(this.mass);
		effectiveCalculateDasCapexDTO.setSsumptionsId(this.ssumptionsId);
		effectiveCalculateDasCapexDTO.setCreatedUserId(this.createdUserId);
		effectiveCalculateDasCapexDTO.setUpdatedUserId(this.updatedUserId);
		effectiveCalculateDasCapexDTO.setCreatedDate(this.createdDate);
		effectiveCalculateDasCapexDTO.setUpdatedDate(this.updatedDate);
		return effectiveCalculateDasCapexDTO;
	}

}
