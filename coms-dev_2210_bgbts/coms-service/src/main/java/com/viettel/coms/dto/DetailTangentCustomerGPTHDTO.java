package com.viettel.coms.dto;

import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import com.viettel.coms.bo.DetailTangentCustomerGPTHBO;

@XmlRootElement(name = "")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetailTangentCustomerGPTHDTO extends ComsBaseFWDTO<DetailTangentCustomerGPTHBO>{
	private Long detailTangentCustomerGPTHId;
	private Long tangentCustomerGPTHId;
	private String phone;
	private String contactDepartment;
	private String position;
	private String contactForm;
	private String contactPerson;
	
	@Override
	public DetailTangentCustomerGPTHBO toModel() {
		DetailTangentCustomerGPTHBO bo = new DetailTangentCustomerGPTHBO();
		bo.setDetailTangentCustomerGPTHId(this.getDetailTangentCustomerGPTHId());
		bo.setTangentCustomerGPTHId(this.getTangentCustomerGPTHId());
		bo.setPhone(this.getPhone());
		bo.setContactDepartment(this.getContactDepartment());
		bo.setPosition(this.getPosition());
		bo.setContactForm(this.getContactForm());
		bo.setContactPerson(this.getContactPerson());
		return bo;
	}
	
	public Long getDetailTangentCustomerGPTHId() {
		return detailTangentCustomerGPTHId;
	}

	public void setDetailTangentCustomerGPTHId(Long detailTangentCustomerGPTHId) {
		this.detailTangentCustomerGPTHId = detailTangentCustomerGPTHId;
	}

	public Long getTangentCustomerGPTHId() {
		return tangentCustomerGPTHId;
	}

	public void setTangentCustomerGPTHId(Long tangentCustomerGPTHId) {
		this.tangentCustomerGPTHId = tangentCustomerGPTHId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getContactDepartment() {
		return contactDepartment;
	}

	public void setContactDepartment(String contactDepartment) {
		this.contactDepartment = contactDepartment;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getContactForm() {
		return contactForm;
	}

	public void setContactForm(String contactForm) {
		this.contactForm = contactForm;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	@Override
	public String catchName() {
		// TODO Auto-generated method stub
		return detailTangentCustomerGPTHId.toString();
	}

	@Override
	public Long getFWModelId() {
		// TODO Auto-generated method stub
		return detailTangentCustomerGPTHId;
	}
	
}
