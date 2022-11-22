package com.viettel.coms.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.ContactUnitDetailDescriptionBO;
import com.viettel.utils.CustomJsonDateDeserializer;
import com.viettel.utils.CustomJsonDateSerializer;
@SuppressWarnings("serial")
@XmlRootElement(name = "CONTACT_UNIT_DETAIL_DESCRIPTIONBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactUnitDetailDescriptionDTO extends ComsBaseFWDTO<ContactUnitDetailDescriptionBO>{
	private Long contactUnitDetailDescriptionId;
	private Long contactUnitDetailId;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date createDate;
	private String description;
	private String createDateS;
	
	private String createUserId;
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
	public String getCreateDateS() {
		return createDateS;
	}
	public void setCreateDateS(String createDateS) {
		this.createDateS = createDateS;
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
	public String catchName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Long getFWModelId() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ContactUnitDetailDescriptionBO toModel() {
		ContactUnitDetailDescriptionBO bo = new ContactUnitDetailDescriptionBO();
		bo.setContactUnitDetailDescriptionId(this.contactUnitDetailDescriptionId);
		bo.setContactUnitDetailId(this.contactUnitDetailId);
		bo.setCreateDate(this.createDate);
		bo.setDescription(description);
		bo.setCreateUserId(this.createUserId);
		bo.setIsView(this.isView);
		return bo;
	}
	
}
