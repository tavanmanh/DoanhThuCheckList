package com.viettel.coms.bo;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.CapexSourceHTCTDTO;
import com.viettel.service.base.model.BaseFWModelImpl;

@Entity
@Table(name = "CAPEX_SOURCE_HTCT")
public class CapexSourceHTCTBO extends BaseFWModelImpl{

	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = {@Parameter(name = "sequence", value = "CAPEX_SOURCE_HTCT_SEQ")})
	@Column(name="CAPEX_SOURCE_ID", length = 22)
	private Long capexSourceId;
	@Column(name="CONTENT_CAPEX",length = 2000)
	private String contentCapex;
	@Column(name="UNIT",length = 100)
	private String unit;
	@Column(name="COST_CAPEX",length = 22)
	private Long costCapex;
	@Column(name="CREATED_DATE",length = 7)
	private Date createdDate;
	@Column(name="CREATED_USER_ID",length = 22)
	private Long createdUserId;
	@Column(name="UPDATE_USER_ID",length = 22)
	private Long updateUserId;
	@Column(name="UPDATED_DATE",length = 7)
	private Date updatedDate ;
	
	public Long getCapexSourceId() {
		return capexSourceId;
	}
	public void setCapexSourceId(Long capexSourceId) {
		this.capexSourceId = capexSourceId;
	}
	public String getContentCapex() {
		return contentCapex;
	}
	public void setContentCapex(String contentCapex) {
		this.contentCapex = contentCapex;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Long getCostCapex() {
		return costCapex;
	}
	public void setCostCapex(Long costCapex) {
		this.costCapex = costCapex;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Long getCreatedUserId() {
		return createdUserId;
	}
	public void setCreatedUserId(Long createdUserId) {
		this.createdUserId = createdUserId;
	}
	public Long getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	
	
	@Override
	public CapexSourceHTCTDTO toDTO() {
		CapexSourceHTCTDTO capexSourceHTCTDTO = new CapexSourceHTCTDTO();
		capexSourceHTCTDTO.setCapexSourceId(this.capexSourceId);
		capexSourceHTCTDTO.setContentCapex(this.contentCapex);
		capexSourceHTCTDTO.setUnit(this.unit);
		capexSourceHTCTDTO.setCostCapex(this.costCapex);
		capexSourceHTCTDTO.setCreatedDate(this.createdDate);
		capexSourceHTCTDTO.setCreatedUserId(this.createdUserId);
		capexSourceHTCTDTO.setUpdateUserId(this.updateUserId);
		capexSourceHTCTDTO.setUpdatedDate(this.updatedDate);
		return capexSourceHTCTDTO;
	}
	
}
