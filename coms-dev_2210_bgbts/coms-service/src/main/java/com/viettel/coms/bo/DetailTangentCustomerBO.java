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
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.DetailTangentCustomerDTO;
import com.viettel.coms.dto.TangentCustomerDTO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;
@Entity
@Table(name = "DETAIL_TANGENT_CUSTOMER")
public class DetailTangentCustomerBO extends BaseFWModelImpl {
	
	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
			@Parameter(name = "sequence", value = "DETAIL_TANGENT_CUSTOMER_SEQ") })
	
	@Column(name = "DETAIL_TANGENT_CUSTOMER_ID", length = 10)
	private Long detailTangentCustomerId;
	@Column(name = "TANGENT_CUSTOMER_ID", length = 10)
	private Long tangentCustomerId;
	@Column(name = "PARTNER_TYPE", length = 1)
	private Long partnerType;
	@Column(name = "SEX_B2C", length = 10)
	private Long sexB2C; 
	@Column(name = "PHONE_B2C", length = 20)
	private String phoneB2C;
	@Column(name = "EMAIL_B2C", length = 200)
	private String emailB2C;
	@Column(name = "BIRTH_YEAR_B2C", length = 50)
	private Date birthYearB2C;
	@Column(name = "UNIT_NAME", length = 200)
	private String unitName;
	@Column(name = "TAX_CODE", length = 20)
	private String taxCode;
	@Column(name = "BUSINESS", length = 200)
	private String business;
	
	@Column(name = "DETAIL_BUSINESS_OTHER", length = 200)
	private String detailBusinessOther;
	
	@Column(name = "EMPLOYEE", length = 200)
	private String employee;
	@Column(name = "FOUNDING", length = 50)
	private Date founding;
	@Column(name = "EMAIL_B2B", length = 200)
	private String emailB2B;
	
	@Column(name = "GEOGRAPHICAL_AREA", length = 20)
	private String geographicalArea;
	@Column(name = "FIELD_WORK", length = 20)
	private String fieldWork;
	
	@Column(name = "POSITION", length = 100)
	private String position;
	@Column(name = "CUSTOMERS", length = 200)
	private String customers;
	@Column(name = "REPRESENTATIVE", length = 200)
	private String representative;
	@Column(name = "DIRECT_CONTACT", length = 200)
	private String directContact; 
	@Column(name = "POSITION_REPRESENTATIVE", length = 200)
	private String positionRepresentative;
	@Column(name = "POSITION_DIRECT_CONTACT", length = 200)
	private String positionDirectContact;

	@Column(name = "EMAIL_REPRESENTATIVE", length = 200)
	private String emailRepresentative;
	@Column(name = "EMAIL_DIRECT_CONTACT", length = 200)
	private String emailDirectContact;
	@Column(name = "PHONE_REPRESENTATIVE", length = 200)
	private String phoneRepresentative;
	@Column(name = "PHONE_DIRECT_CONTACT", length = 200)
	private String phoneDirectContact;
	@Column(name = "BIRTH_YEAR_REPRESENTATIVE", length = 50)
	private Date birthYearRepresentative;
	@Column(name = "BIRTH_YEAR_DIRECT_CONTACT", length = 50)
	private Date birthYearDirectContact;
	@Column(name = "ADDRESS_REPRESENTATIVE", length = 1000)
	private String addressRepresentative;
	@Column(name = "ADDRESS_DIRECT_CONTACT", length = 1000)
	private String addressDirectContact;
	@Column(name = "SEX_REPRESENTATIVE", length = 1)
	private Long sexRepresentative;
	@Column(name = "SEX_DIRECT_CONTACT", length = 1)
	private Long sexDirectContact;
	 
	@Override
	public BaseFWDTOImpl toDTO() {
		DetailTangentCustomerDTO dto = new DetailTangentCustomerDTO();
		dto.setDetailTangentCustomerId(this.getDetailTangentCustomerId());
		dto.setTangentCustomerId(this.getTangentCustomerId());
		dto.setPartnerType(this.getPartnerType());
		dto.setSexB2C(this.getSexB2C());
		dto.setPhoneB2C(this.getPhoneB2C());
		dto.setEmailB2C(this.getEmailB2C());
		dto.setBirthYearB2C(this.getBirthYearB2C());
		dto.setUnitName(this.getUnitName());
		dto.setTaxCode(this.getTaxCode());
		dto.setBusiness(this.getBusiness());
	    dto.setDetailBusinessOther(this.getDetailBusinessOther());
		dto.setEmployee(this.getEmployee());
		dto.setFounding(this.getFounding());
		dto.setEmailB2B(this.getEmailB2B());
		dto.setGeographicalArea(this.getGeographicalArea());
		dto.setFieldWork(this.getFieldWork());
		dto.setPosition(this.getPosition());
		dto.setCustomers(this.getCustomers());
		dto.setRepresentative(this.getRepresentative());
		dto.setDirectContact(this.getDirectContact());
		dto.setPositionRepresentative(this.getPositionRepresentative());
		dto.setPositionDirectContact(this.getPositionDirectContact());
		dto.setEmailRepresentative(this.getEmailRepresentative());
		dto.setEmailDirectContact(this.getEmailDirectContact());
		dto.setPhoneRepresentative(this.getPhoneRepresentative());
		dto.setPhoneDirectContact(this.getPhoneDirectContact());
		dto.setBirthYearRepresentative(this.getBirthYearRepresentative());
		dto.setBirthYearDirectContact(this.getBirthYearDirectContact());
		dto.setAddressRepresentative(this.getAddressRepresentative());
		dto.setAddressDirectContact(this.getAddressDirectContact());
		dto.setSexRepresentative(this.getSexRepresentative());
		dto.setSexDirectContact(this.getSexDirectContact());
		return dto;
	}

	public Long getDetailTangentCustomerId() {
		return detailTangentCustomerId;
	}

	public void setDetailTangentCustomerId(Long detailTangentCustomerId) {
		this.detailTangentCustomerId = detailTangentCustomerId;
	}

	public Long getTangentCustomerId() {
		return tangentCustomerId;
	}

	public void setTangentCustomerId(Long tangentCustomerId) {
		this.tangentCustomerId = tangentCustomerId;
	}

	public Long getSexB2C() {
		return sexB2C;
	}

	public void setSexB2C(Long sexB2C) {
		this.sexB2C = sexB2C;
	}

	public String getPhoneB2C() {
		return phoneB2C;
	}

	public void setPhoneB2C(String phoneB2C) {
		this.phoneB2C = phoneB2C;
	}

	public Date getBirthYearB2C() {
		return birthYearB2C;
	}

	public void setBirthYearB2C(Date birthYearB2C) {
		this.birthYearB2C = birthYearB2C;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public String getEmployee() {
		return employee;
	}

	public void setEmployee(String employee) {
		this.employee = employee;
	}

	public Date getFounding() {
		return founding;
	}

	public void setFounding(Date founding) {
		this.founding = founding;
	}

	public String getGeographicalArea() {
		return geographicalArea;
	}

	public void setGeographicalArea(String geographicalArea) {
		this.geographicalArea = geographicalArea;
	}

	public String getFieldWork() {
		return fieldWork;
	}

	public void setFieldWork(String fieldWork) {
		this.fieldWork = fieldWork;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getCustomers() {
		return customers;
	}

	public void setCustomers(String customers) {
		this.customers = customers;
	}

	public String getRepresentative() {
		return representative;
	}

	public void setRepresentative(String representative) {
		this.representative = representative;
	}

	public String getDirectContact() {
		return directContact;
	}

	public void setDirectContact(String directContact) {
		this.directContact = directContact;
	}

	public String getPositionRepresentative() {
		return positionRepresentative;
	}

	public void setPositionRepresentative(String positionRepresentative) {
		this.positionRepresentative = positionRepresentative;
	}

	public String getPositionDirectContact() {
		return positionDirectContact;
	}

	public void setPositionDirectContact(String positionDirectContact) {
		this.positionDirectContact = positionDirectContact;
	}

	public String getEmailRepresentative() {
		return emailRepresentative;
	}

	public void setEmailRepresentative(String emailRepresentative) {
		this.emailRepresentative = emailRepresentative;
	}

	public String getEmailDirectContact() {
		return emailDirectContact;
	}

	public void setEmailDirectContact(String emailDirectContact) {
		this.emailDirectContact = emailDirectContact;
	}

	public String getPhoneRepresentative() {
		return phoneRepresentative;
	}

	public void setPhoneRepresentative(String phoneRepresentative) {
		this.phoneRepresentative = phoneRepresentative;
	}

	public String getPhoneDirectContact() {
		return phoneDirectContact;
	}

	public void setPhoneDirectContact(String phoneDirectContact) {
		this.phoneDirectContact = phoneDirectContact;
	}

	public Date getBirthYearRepresentative() {
		return birthYearRepresentative;
	}

	public void setBirthYearRepresentative(Date birthYearRepresentative) {
		this.birthYearRepresentative = birthYearRepresentative;
	}

	public Date getBirthYearDirectContact() {
		return birthYearDirectContact;
	}

	public void setBirthYearDirectContact(Date birthYearDirectContact) {
		this.birthYearDirectContact = birthYearDirectContact;
	}

	public String getAddressRepresentative() {
		return addressRepresentative;
	}

	public void setAddressRepresentative(String addressRepresentative) {
		this.addressRepresentative = addressRepresentative;
	}

	public String getAddressDirectContact() {
		return addressDirectContact;
	}

	public void setAddressDirectContact(String addressDirectContact) {
		this.addressDirectContact = addressDirectContact;
	}

	public Long getSexRepresentative() {
		return sexRepresentative;
	}

	public void setSexRepresentative(Long sexRepresentative) {
		this.sexRepresentative = sexRepresentative;
	}

	public Long getSexDirectContact() {
		return sexDirectContact;
	}

	public void setSexDirectContact(Long sexDirectContact) {
		this.sexDirectContact = sexDirectContact;
	}

	public Long getPartnerType() {
		return partnerType;
	}

	public void setPartnerType(Long partnerType) {
		this.partnerType = partnerType;
	}

	public String getEmailB2C() {
		return emailB2C;
	}

	public void setEmailB2C(String emailB2C) {
		this.emailB2C = emailB2C;
	}
	
	public String getDetailBusinessOther() {
		return detailBusinessOther;
	}

	public void setDetailBusinessOther(String detailBusinessOther) {
		this.detailBusinessOther = detailBusinessOther;
	}
	
	public String getEmailB2B() {
		return emailB2B;
	}

	public void setEmailB2B(String emailB2B) {
		this.emailB2B = emailB2B;
	}
	
}
