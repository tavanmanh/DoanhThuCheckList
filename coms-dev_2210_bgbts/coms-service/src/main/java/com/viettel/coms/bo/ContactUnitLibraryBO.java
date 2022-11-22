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
import com.viettel.coms.dto.ContactUnitLibraryDTO;
import com.viettel.service.base.model.BaseFWModelImpl;
@SuppressWarnings("serial")
@Entity(name = "com.viettel.coms.ContactUnitLibraryBO")
@Table(name = "CONTACT_UNIT_LIBRARY")
public class ContactUnitLibraryBO extends BaseFWModelImpl {

	@Column(name = "CONTACT_DATE", length = 22)
	private Date contactDate;
	
	@Column(name = "CONTACT_UNIT_LIBRARY_ID")
	private Long contactUnitLibrarylId;
	
	
	
	@Id
	@Column(name = "CONTACT_UNIT_ID", length = 22)
	private Long contactUnitId;
	@Column(name = "CREATE_DATE", length = 22)
	private Date createDate;
	@Column(name = "CREATE_USER_ID", length = 22)
	private Long createUserId;
	@Column(name = "DEADLINE_DATE_COMPLETE", length = 22)
	private Date deadlineDateComplete;
	@Column(name = "PROVINCE_CODE", length = 200 )
	private String provinceCode;
	@Column(name = "PROVINCE_NAME", length = 200 )
	private String provinceName;
	@Column(name = "UNIT_ADDRESS", length = 1000 )
	private String unitAddress;
	@Column(name = "UNIT_BOSS", length = 200 )
	private String unitBoss;
	@Column(name = "UNIT_FIELD", length = 1000 )
	private String unitField;
	@Column(name = "UNIT_ID", length = 1000 )
	private Long unitId;
	@Column(name = "UNIT_NAME", length = 200 )
	private String unitName;
	@Column(name = "UPDATE_DATE", length = 22)
	private Date updateDate;
	@Column(name = "UPDATE_USER_ID", length = 22)
	private Long updateUserId;
	
	
	
	
	
	
	
	@Column(name = "CONTACT_UNIT_DETAIL_ID")
	private Long contactUnitDetailId;
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
	
	
	
	



	public Long getContactUnitLibrarylId() {
		return contactUnitLibrarylId;
	}



	public void setContactUnitLibrarylId(Long contactUnitLibrarylId) {
		this.contactUnitLibrarylId = contactUnitLibrarylId;
	}



	public Date getCreateDate() {
		return createDate;
	}



	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}



	public Long getCreateUserId() {
		return createUserId;
	}



	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}



	public Date getDeadlineDateComplete() {
		return deadlineDateComplete;
	}



	public void setDeadlineDateComplete(Date deadlineDateComplete) {
		this.deadlineDateComplete = deadlineDateComplete;
	}



	public String getProvinceCode() {
		return provinceCode;
	}



	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}



	public String getProvinceName() {
		return provinceName;
	}



	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}



	public String getUnitAddress() {
		return unitAddress;
	}



	public void setUnitAddress(String unitAddress) {
		this.unitAddress = unitAddress;
	}



	public String getUnitBoss() {
		return unitBoss;
	}



	public void setUnitBoss(String unitBoss) {
		this.unitBoss = unitBoss;
	}



	public String getUnitField() {
		return unitField;
	}



	public void setUnitField(String unitField) {
		this.unitField = unitField;
	}



	public Long getUnitId() {
		return unitId;
	}



	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}



	public String getUnitName() {
		return unitName;
	}



	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}



	public Date getUpdateDate() {
		return updateDate;
	}



	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}



	public Long getUpdateUserId() {
		return updateUserId;
	}



	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}



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
	public ContactUnitLibraryDTO toDTO() {
		ContactUnitLibraryDTO bo = new ContactUnitLibraryDTO();
		bo.setContactUnitLibrarylId(this.contactUnitLibrarylId);
		bo.setCreateDate(this.getCreateDate());
		bo.setCreateUserId(this.createUserId);
		bo.setDeadlineDateComplete(this.getDeadlineDateComplete());
		bo.setProvinceCode(this.getProvinceCode());
		bo.setProvinceName(this.getProvinceName());
		bo.setUnitAddress(this.getUnitAddress());
		bo.setUnitBoss(this.getUnitBoss());
		bo.setUnitField(this.getUnitField());
		bo.setUnitId(this.unitId);
		bo.setUnitName(this.getUnitName());
		bo.setUpdateDate(this.getUpdateDate());
		bo.setUpdateUserId(this.updateUserId);
		
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
		bo.setDescription(this.description);
		return bo;
	}
	
}
