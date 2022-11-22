package com.viettel.coms.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.DetailTangentCustomerBO;
import com.viettel.coms.bo.TangentCustomerBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;

@XmlRootElement(name = "DETAIL_TANGENT_CUSTOMERBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetailTangentCustomerDTO extends ComsBaseFWDTO<DetailTangentCustomerBO>{
	private Long detailTangentCustomerId;
	private Long tangentCustomerId;
	private Long sexB2C;
	private String phoneB2C;
	private String emailB2C;
	private String geographicalArea;

	private String fieldWork;

	private String position;

	private String customers;

	private String unitName;
	private String taxCode;
	private String business;
	private String detailBusinessOther;
	
	private String employee;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date founding;
	private String emailB2B;
	private String representative;
	
	private String directContact;
	
	private String positionRepresentative;

	private String positionDirectContact;

	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date birthYearB2C;
	private String emailRepresentative;
	private String emailDirectContact;

	private String phoneRepresentative;
	private String phoneDirectContact;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date birthYearRepresentative;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date birthYearDirectContact;

	private String addressRepresentative;
	private String addressDirectContact;
	private Long sexRepresentative;
	private Long sexDirectContact;
	
	private Long partnerType;
	
	@Override
	public DetailTangentCustomerBO toModel() {
		DetailTangentCustomerBO bo = new DetailTangentCustomerBO();
		bo.setDetailTangentCustomerId(this.getDetailTangentCustomerId());
		bo.setTangentCustomerId(this.getTangentCustomerId());
		bo.setPartnerType(this.getPartnerType());
		bo.setSexB2C(this.getSexB2C());
		bo.setPhoneB2C(this.getPhoneB2C());
		bo.setEmailB2C(this.getEmailB2C());
		bo.setBirthYearB2C(this.getBirthYearB2C());
		bo.setUnitName(this.getUnitName());
		bo.setTaxCode(this.getTaxCode());
		bo.setBusiness(this.getBusiness());
		bo.setDetailBusinessOther(this.getDetailBusinessOther());
		bo.setEmployee(this.getEmployee());
		bo.setFounding(this.getFounding());
		bo.setEmailB2B(this.getEmailB2B());
		bo.setGeographicalArea(this.getGeographicalArea());
		bo.setFieldWork(this.getFieldWork());
		bo.setPosition(this.getPosition());
		bo.setCustomers(this.getCustomers());
		bo.setRepresentative(this.getRepresentative());
		bo.setDirectContact(this.getDirectContact());
		bo.setPositionRepresentative(this.getPositionRepresentative());
		bo.setPositionDirectContact(this.getPositionDirectContact());
		bo.setEmailRepresentative(this.getEmailRepresentative());
		bo.setEmailDirectContact(this.getEmailDirectContact());
		bo.setPhoneRepresentative(this.getPhoneRepresentative());
		bo.setPhoneDirectContact(this.getPhoneDirectContact());
		bo.setBirthYearRepresentative(this.getBirthYearRepresentative());
		bo.setBirthYearDirectContact(this.getBirthYearDirectContact());
		bo.setAddressRepresentative(this.getAddressRepresentative());
		bo.setAddressDirectContact(this.getAddressDirectContact());
		bo.setSexRepresentative(this.getSexRepresentative());
		bo.setSexDirectContact(this.getSexDirectContact());
		return bo;
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

	public Date getBirthYearB2C() {
		return birthYearB2C;
	}

	public void setBirthYearB2C(Date birthYearB2C) {
		this.birthYearB2C = birthYearB2C;
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

	@Override
	public String catchName() {
		// TODO Auto-generated method stub
		return detailTangentCustomerId.toString();
	}

	@Override
	public Long getFWModelId() {
		// TODO Auto-generated method stub
		return detailTangentCustomerId;
	}
	
}
