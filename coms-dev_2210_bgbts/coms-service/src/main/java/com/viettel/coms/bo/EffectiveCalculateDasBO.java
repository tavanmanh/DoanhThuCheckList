package com.viettel.coms.bo;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import com.viettel.coms.dto.EffectiveCalculateDasDTO;
import com.viettel.service.base.model.BaseFWModelImpl;

@SuppressWarnings("serial")
@Entity(name = "com.viettel.coms.bo.EffectiveCalculateDasBO")
@Table(name = "CTCT_COMS_OWNER.ASSUMPTIONS")

public class EffectiveCalculateDasBO extends BaseFWModelImpl {
	
	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = { @Parameter(name = "sequence", value = "ASSUMPTIONS_SEQ") })
	@Column(name = "ASSUMPTIONS_ID", length = 22)
	private java.lang.Long assumptionsId;
	@Column(name = "CONTENT_ASSUMPTIONS", length = 2000)
	private java.lang.String contentAssumptions;
	@Column(name = "UNIT", length = 100)
	private java.lang.String unit;	
	@Column(name = "COST_ASSUMPTIONS", length = 22)
	private java.lang.Double costAssumptions;
	@Column(name = "NOTE_ASSUMPTIONS", length = 2000)
	private java.lang.String noteAssumptions;	
	@Column(name = "CREATED_USER_ID", length = 22)
	private java.lang.Long createdUserId;	
	@Column(name = "UPDATED_USER_ID", length = 22)
	private java.lang.Long updatedUserId;
	@Column(name = "CREATED_DATE", length = 7)
	private java.util.Date createdDate;	
	@Column(name = "UPDATED_DATE", length = 7)
	private java.util.Date updatedDate;
	
	public java.lang.Long getAssumptionsId() {
		return assumptionsId;
	}

	public void setAssumptionsId(java.lang.Long assumptionsId) {
		this.assumptionsId = assumptionsId;
	}

	public java.lang.String getUnit() {
		return unit;
	}

	public void setUnit(java.lang.String unit) {
		this.unit = unit;
	}

	public java.lang.Long getCreatedUserId() {
		return createdUserId;
	}

	public java.lang.String getContentAssumptions() {
		return contentAssumptions;
	}

	public void setContentAssumptions(java.lang.String contentAssumptions) {
		this.contentAssumptions = contentAssumptions;
	}

	public java.lang.Double getCostAssumptions() {
		return costAssumptions;
	}

	public void setCostAssumptions(java.lang.Double costAssumptions) {
		this.costAssumptions = costAssumptions;
	}

	public java.lang.String getNoteAssumptions() {
		return noteAssumptions;
	}

	public void setNoteAssumptions(java.lang.String noteAssumptions) {
		this.noteAssumptions = noteAssumptions;
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

	@Override
	public EffectiveCalculateDasDTO toDTO() {
		EffectiveCalculateDasDTO effectiveCalculateDasDTO = new EffectiveCalculateDasDTO();
		effectiveCalculateDasDTO.setAssumptionsId(this.assumptionsId);
		effectiveCalculateDasDTO.setContentAssumptions(this.contentAssumptions);
		effectiveCalculateDasDTO.setUnit(this.unit);
		effectiveCalculateDasDTO.setCostAssumptions(this.costAssumptions);
		effectiveCalculateDasDTO.setNoteAssumptions(this.noteAssumptions);
		effectiveCalculateDasDTO.setCreatedUserId(this.createdUserId);
		effectiveCalculateDasDTO.setUpdatedUserId(this.updatedUserId);
		effectiveCalculateDasDTO.setCreatedDate(this.createdDate);
		effectiveCalculateDasDTO.setUpdatedDate(this.updatedDate);
		return effectiveCalculateDasDTO;
	}
	
	

}
