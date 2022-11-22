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
import com.viettel.coms.dto.ContactUnitDetailDTO;
import com.viettel.service.base.model.BaseFWModelImpl;
@SuppressWarnings("serial")
@Entity(name = "com.viettel.coms.ContactUnitDetailBO")
@Table(name = "CONTACT_UNIT_DETAIL")
public class ContactUnitDetailBO extends BaseFWModelImpl {

	@Column(name = "CONTACT_DATE", length = 22)
	private Date contactDate;
	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = { @Parameter(name = "sequence", value = "CONTACT_UNIT_DETAIL_SEQ") })
	@Column(name = "CONTACT_UNIT_DETAIL_ID")
	private Long contactUnitDetailId;
	@Column(name = "CONTACT_UNIT_ID")
	private Long contactUnitId;
	@Column(name = "CUSTOMER_ADDRESS", length = 1000 )
	private String customerAddress;
	@Column(name = "DESCRIPTION", length = 1000 )
	private String description;
	@Column(name = "DURING_DISSCUS", length = 1000 )
	private String duringDisscus;
	@Column(name = "FULL_NAME_CUS", length = 100 )
	private String fullNameCus;
	@Column(name = "FULL_NAME_EMPLOY", length = 100 )
	private String fullNameEmploy;
	@Column(name = "MAIL_CUS", length = 100 )
	private String mailCus;
	@Column(name = "MAIL_EMPLOY", length = 100 )
	private String mailEmploy;
	@Column(name = "NEED_DO_DAS_CDBR", length = 1000 )
	private String needDoDasCdbr;
	@Column(name = "NEED_DO_SUN_ENERGY", length = 1000 )
	private String needDoSunEnergy;
	@Column(name = "NEED_HIRE_STATION_BTS", length = 1000 )
	private String needHireStationBts;
	@Column(name = "NEED_HIRE_TRANSMISSION", length = 1000 )
	private String needHireTransmission;
	@Column(name = "NEED_OTHER", length = 1000 )
	private String needOther;
	@Column(name = "NO_NEED", length = 1000 )
	private String noNeed;
	@Column(name = "PHONE_NUMBER_CUS", length = 100 )
	private String phoneNumberCus;
	@Column(name = "PHONE_NUMBER_EMPLOY", length = 100 )
	private String phoneNumberEmploy;
	@Column(name = "POSITION_CUS", length = 1000 )
	private String positionCus;
	@Column(name = "SHORT_CONTENT", length = 1000 )
	private String shortContent;
	@Column(name = "SIGN_CONTRACT", length = 100 )
	private String signContract;
	@Column(name = "RESULT")
	private Long result;
	@Column(name = "TYPE")
	private Long type;
	
	
	
	public Long getType() {
		return type;
	}



	public void setType(Long type) {
		this.type = type;
	}



	public Long getResult() {
		return result;
	}



	public void setResult(Long result) {
		this.result = result;
	}



	public Date getContactDate() {
		return contactDate;
	}



	public void setContactDate(Date contactDate) {
		this.contactDate = contactDate;
	}



	public Long getContactUnitDetailId() {
		return contactUnitDetailId;
	}



	public void setContactUnitDetailId(Long contactUnitDetailId) {
		this.contactUnitDetailId = contactUnitDetailId;
	}



	public Long getContactUnitId() {
		return contactUnitId;
	}



	public void setContactUnitId(Long contactUnitId) {
		this.contactUnitId = contactUnitId;
	}



	public String getCustomerAddress() {
		return customerAddress;
	}



	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public String getDuringDisscus() {
		return duringDisscus;
	}



	public void setDuringDisscus(String duringDisscus) {
		this.duringDisscus = duringDisscus;
	}



	public String getFullNameCus() {
		return fullNameCus;
	}



	public void setFullNameCus(String fullNameCus) {
		this.fullNameCus = fullNameCus;
	}



	public String getFullNameEmploy() {
		return fullNameEmploy;
	}



	public void setFullNameEmploy(String fullNameEmploy) {
		this.fullNameEmploy = fullNameEmploy;
	}



	public String getMailCus() {
		return mailCus;
	}



	public void setMailCus(String mailCus) {
		this.mailCus = mailCus;
	}



	public String getMailEmploy() {
		return mailEmploy;
	}



	public void setMailEmploy(String mailEmploy) {
		this.mailEmploy = mailEmploy;
	}



	public String getNeedDoDasCdbr() {
		return needDoDasCdbr;
	}



	public void setNeedDoDasCdbr(String needDoDasCdbr) {
		this.needDoDasCdbr = needDoDasCdbr;
	}



	public String getNeedDoSunEnergy() {
		return needDoSunEnergy;
	}



	public void setNeedDoSunEnergy(String needDoSunEnergy) {
		this.needDoSunEnergy = needDoSunEnergy;
	}



	public String getNeedHireStationBts() {
		return needHireStationBts;
	}



	public void setNeedHireStationBts(String needHireStationBts) {
		this.needHireStationBts = needHireStationBts;
	}



	public String getNeedHireTransmission() {
		return needHireTransmission;
	}



	public void setNeedHireTransmission(String needHireTransmission) {
		this.needHireTransmission = needHireTransmission;
	}



	public String getNeedOther() {
		return needOther;
	}



	public void setNeedOther(String needOther) {
		this.needOther = needOther;
	}



	public String getNoNeed() {
		return noNeed;
	}



	public void setNoNeed(String noNeed) {
		this.noNeed = noNeed;
	}



	public String getPhoneNumberCus() {
		return phoneNumberCus;
	}



	public void setPhoneNumberCus(String phoneNumberCus) {
		this.phoneNumberCus = phoneNumberCus;
	}



	public String getPhoneNumberEmploy() {
		return phoneNumberEmploy;
	}



	public void setPhoneNumberEmploy(String phoneNumberEmploy) {
		this.phoneNumberEmploy = phoneNumberEmploy;
	}



	public String getPositionCus() {
		return positionCus;
	}



	public void setPositionCus(String positionCus) {
		this.positionCus = positionCus;
	}



	public String getShortContent() {
		return shortContent;
	}



	public void setShortContent(String shortContent) {
		this.shortContent = shortContent;
	}



	public String getSignContract() {
		return signContract;
	}



	public void setSignContract(String signContract) {
		this.signContract = signContract;
	}



	@Override
	public ContactUnitDetailDTO toDTO() {
		ContactUnitDetailDTO bo = new ContactUnitDetailDTO();
		bo.setContactDate(this.getContactDate());
		bo.setContactUnitDetailId(this.contactUnitDetailId);
		bo.setContactUnitId(this.contactUnitId);
		bo.setCustomerAddress(this.getCustomerAddress());
		bo.setDuringDisscus(this.getDuringDisscus());
		bo.setFullNameCus(this.getFullNameCus());
		bo.setFullNameEmploy(this.getFullNameEmploy());
		bo.setMailCus(this.getMailCus());
		bo.setMailEmploy(this.getMailEmploy());
		bo.setNeedDoDasCdbr(this.getNeedDoDasCdbr());
		bo.setNeedDoSunEnergy(this.getNeedDoSunEnergy());
		bo.setNeedHireStationBts(this.getNeedHireStationBts());
		bo.setNeedHireTransmission(this.getNeedHireTransmission());
		bo.setNeedOther(this.getNeedOther());
		bo.setNoNeed(this.getNoNeed());
		bo.setPhoneNumberCus(this.getPhoneNumberCus());
		bo.setPhoneNumberEmploy(this.getPhoneNumberEmploy());
		bo.setPositionCus(this.getPositionCus());
		bo.setShortContent(this.getShortContent());
		bo.setSignContract(this.getSignContract());
		bo.setResult(this.result);
		bo.setType(this.type);
		return bo;
	}
	
}
