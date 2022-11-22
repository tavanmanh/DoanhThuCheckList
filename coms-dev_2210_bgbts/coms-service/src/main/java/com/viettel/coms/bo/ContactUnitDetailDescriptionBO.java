package com.viettel.coms.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.ContactUnitDTO;
import com.viettel.coms.dto.ContactUnitDetailDescriptionDTO;
import com.viettel.service.base.model.BaseFWModelImpl;
@SuppressWarnings("serial")
@Entity(name = "com.viettel.coms.ContactUnitDetailDescriptionBO")
@Table(name = "CONTACT_UNIT_DETAIL_DESCRIPTION")
public class ContactUnitDetailDescriptionBO extends BaseFWModelImpl {
	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = { @Parameter(name = "sequence", value = "CONTACT_UNIT_DETAIL_DESCRIPTION_SEQ") })
	@Column(name = "CONTACT_UNIT_DETAIL_DESCRIPTION_ID")
	private Long contactUnitDetailDescriptionId;
	@Column(name = "CONTACT_UNIT_DETAIL_ID")
	private Long contactUnitDetailId;
	@Column(name = "CREATE_DATE")
	private Date createDate;
	@Column(name = "DESCRIPTION", length = 1000)
	private String description;
	
	@Column(name = "CREATE_USER_ID", length = 1000)
	private String createUserId;
	@Column(name = "IS_VIEW", length = 1000)
	private Long isView;
	
	
	
	public String getCreateUserId() {
		return createUserId;
	}



	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}



	public Long getIsView() {
		return isView;
	}



	public void setIsView(Long isView) {
		this.isView = isView;
	}



	public Long getContactUnitDetailDescriptionId() {
		return contactUnitDetailDescriptionId;
	}



	public void setContactUnitDetailDescriptionId(Long contactUnitDetailDescriptionId) {
		this.contactUnitDetailDescriptionId = contactUnitDetailDescriptionId;
	}



	public Long getContactUnitDetailId() {
		return contactUnitDetailId;
	}



	public void setContactUnitDetailId(Long contactUnitDetailId) {
		this.contactUnitDetailId = contactUnitDetailId;
	}



	public Date getCreateDate() {
		return createDate;
	}



	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	@Override
	public ContactUnitDetailDescriptionDTO toDTO() {
		ContactUnitDetailDescriptionDTO bo = new ContactUnitDetailDescriptionDTO();
		bo.setContactUnitDetailDescriptionId(this.contactUnitDetailDescriptionId);
		bo.setContactUnitDetailId(this.contactUnitDetailId);
		bo.setCreateDate(this.createDate);
		bo.setDescription(description);
		bo.setCreateUserId(this.createUserId);
		bo.setIsView(this.isView);
		return bo;
	}
	
}
