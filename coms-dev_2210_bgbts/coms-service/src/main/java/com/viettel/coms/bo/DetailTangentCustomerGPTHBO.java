package com.viettel.coms.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import com.viettel.coms.dto.DetailTangentCustomerGPTHDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;
@Entity
@Table(name = "DETAIL_TANGENT_CUSTOMER_GPTH")
public class DetailTangentCustomerGPTHBO extends BaseFWModelImpl {
	
	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "DETAIL_TANGENT_CUSTOMER_GPTH_SEQ") })
	
	@Column(name = "DETAIL_TANGENT_CUSTOMER_GPTH_ID", length = 10)
	private Long detailTangentCustomerGPTHId;
	@Column(name = "TANGENT_CUSTOMER_GPTH_ID", length = 10)
	private Long tangentCustomerGPTHId;
	@Column(name = "PHONE", length = 20)
	private String phone;
	@Column(name = "CONTACT_DEPARTMENT", length = 200)
	private String contactDepartment;
	@Column(name = "POSITION", length = 200)
	private String position;
	@Column(name = "CONTACT_FORM", length = 200)
	private String contactForm;
	@Column(name = "CONTACT_PERSON", length = 200)
	private String contactPerson;
	
	@Override
	public BaseFWDTOImpl toDTO() {
		DetailTangentCustomerGPTHDTO dto = new DetailTangentCustomerGPTHDTO();
		dto.setDetailTangentCustomerGPTHId(this.getDetailTangentCustomerGPTHId());
		dto.setTangentCustomerGPTHId(this.getTangentCustomerGPTHId());
		dto.setPhone(this.getPhone());
		dto.setContactDepartment(this.getContactDepartment());
		dto.setPosition(this.getPosition());
		dto.setContactForm(this.getContactForm());
		dto.setContactPerson(this.getContactPerson());
		return dto;
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
	
}
