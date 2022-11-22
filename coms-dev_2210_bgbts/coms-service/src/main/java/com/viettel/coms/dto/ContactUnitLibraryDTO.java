package com.viettel.coms.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.bo.ContactUnitBO;
import com.viettel.coms.bo.ContactUnitDetailBO;
import com.viettel.coms.bo.ContactUnitLibraryBO;
@SuppressWarnings("serial")
@XmlRootElement(name = "CONTACT_UNIT_LIBRARYBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactUnitLibraryDTO extends ComsBaseFWDTO<ContactUnitLibraryBO>{
	private Long contactUnitLibrarylId;
	
	private Date createDate;
	private Long createUserId;
	private Date updateDate;
	private Long updateUserId;
	
	private Date startDate;
	private Date endDate;
	private Integer countDetail;
	
	private Date contactDate;
	private String contactDateS;
	private Long contactUnitDetailId;
	private Long contactUnitDetailDescriptionId;
	private Long contactUnitId;
	private String customerAddress;
	private String description;
	private String duringDisscus;
	private String fullNameCus;
	private String fullNameEmploy;
	private String mailCus;
	private String mailEmploy;
	private String needDoDasCdbr;
	private String needDoSunEnergy;
	private String needHireTransmission;
	private String needHireStationBts;
	private String needOther;
	private String noNeed;
	private String phoneNumberCus;
	private String phoneNumberEmploy;
	private String positionCus;
	private String shortContent;
	private String signContract;
	private Long result;
	private String resultS;
	
	

	private Date deadlineDateComplete;
	private String deadlineDateCompleteS;
	private String provinceCode;
	private String provinceName;
	private String unitAddress;
	private String unitBoss;
	private String unitField;
	private Long unitId;
	private String unitName;
	private String createDateS;
	private Long type;
	private String typeS;
	
	private String userLogin;
	
	
	
	
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
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Integer getCountDetail() {
		return countDetail;
	}
	public void setCountDetail(Integer countDetail) {
		this.countDetail = countDetail;
	}
	public String getUserLogin() {
		return userLogin;
	}
	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}
	public String getTypeS() {
		return typeS;
	}
	public void setTypeS(String typeS) {
		this.typeS = typeS;
	}
	public Long getType() {
		return type;
	}
	public void setType(Long type) {
		this.type = type;
	}
	private List<ContactUnitDetailDescriptionDTO> contactUnitDetailDescriptionDTOs;
	
	public Long getContactUnitDetailDescriptionId() {
		return contactUnitDetailDescriptionId;
	}
	public void setContactUnitDetailDescriptionId(Long contactUnitDetailDescriptionId) {
		this.contactUnitDetailDescriptionId = contactUnitDetailDescriptionId;
	}
	public String getCreateDateS() {
		return createDateS;
	}
	public void setCreateDateS(String createDateS) {
		this.createDateS = createDateS;
	}
	public List<ContactUnitDetailDescriptionDTO> getContactUnitDetailDescriptionDTOs() {
		return contactUnitDetailDescriptionDTOs;
	}
	public void setContactUnitDetailDescriptionDTOs(
			List<ContactUnitDetailDescriptionDTO> contactUnitDetailDescriptionDTOs) {
		this.contactUnitDetailDescriptionDTOs = contactUnitDetailDescriptionDTOs;
	}
	public String getResultS() {
		return resultS;
	}
	public void setResultS(String resultS) {
		this.resultS = resultS;
	}
	public String getDeadlineDateCompleteS() {
		return deadlineDateCompleteS;
	}
	public void setDeadlineDateCompleteS(String deadlineDateCompleteS) {
		this.deadlineDateCompleteS = deadlineDateCompleteS;
	}
	public String getContactDateS() {
		return contactDateS;
	}
	public void setContactDateS(String contactDateS) {
		this.contactDateS = contactDateS;
	}
	private List<UtilAttachDocumentDTO> utilAttachDocumentDTOs;
	
	
	
	
	
	public Long getResult() {
		return result;
	}
	public void setResult(Long result) {
		this.result = result;
	}
	public List<UtilAttachDocumentDTO> getUtilAttachDocumentDTOs() {
		return utilAttachDocumentDTOs;
	}
	public void setUtilAttachDocumentDTOs(List<UtilAttachDocumentDTO> utilAttachDocumentDTOs) {
		this.utilAttachDocumentDTOs = utilAttachDocumentDTOs;
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
	public Long getContactUnitId() {
		return contactUnitId;
	}
	public void setContactUnitId(Long contactUnitId) {
		this.contactUnitId = contactUnitId;
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
	public ContactUnitLibraryBO toModel() {
		ContactUnitLibraryBO bo = new ContactUnitLibraryBO();
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
