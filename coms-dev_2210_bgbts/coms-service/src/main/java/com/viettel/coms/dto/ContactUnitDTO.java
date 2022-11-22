package com.viettel.coms.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.ContactUnitBO;
import com.viettel.utils.CustomJsonDateDeserializer;
import com.viettel.utils.CustomJsonDateSerializer;
@SuppressWarnings("serial")
@XmlRootElement(name = "CONTACT_UNITBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactUnitDTO extends ComsBaseFWDTO<ContactUnitBO>{
	private Long contactUnitId;
	private Long contactUnitDetailId;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date createDate;
	private Long createUserId;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date deadlineDateComplete;
	private String deadlineDateCompleteS;
	private String provinceCode;
	private String provinceName;
	private String unitAddress;
	private String unitBoss;
	private String unitField;
	private Long unitId;
	private String unitName;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date updateDate;
	private Long updateUserId;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date startDate;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date endDate;
	private Integer countDetail;
	private Long type;
	private String typeS;
	private Long countMess;
	private Long userLoginId;
	private Long isMess;
	private ContactUnitLibraryDTO contactUnitLibraryDTO;
	
	
	
	public ContactUnitLibraryDTO getContactUnitLibraryDTO() {
		return contactUnitLibraryDTO;
	}
	public void setContactUnitLibraryDTO(ContactUnitLibraryDTO contactUnitLibraryDTO) {
		this.contactUnitLibraryDTO = contactUnitLibraryDTO;
	}
	
	
	public Long getIsMess() {
		return isMess;
	}
	public void setIsMess(Long isMess) {
		this.isMess = isMess;
	}
	public Long getUserLoginId() {
		return userLoginId;
	}
	public void setUserLoginId(Long userLoginId) {
		this.userLoginId = userLoginId;
	}
	public String getTypeS() {
		return typeS;
	}
	public Long getCountMess() {
		return countMess;
	}
	public void setCountMess(Long countMess) {
		this.countMess = countMess;
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
	public Long getContactUnitDetailId() {
		return contactUnitDetailId;
	}
	public void setContactUnitDetailId(Long contactUnitDetailId) {
		this.contactUnitDetailId = contactUnitDetailId;
	}
	public Integer getCountDetail() {
		return countDetail;
	}
	public void setCountDetail(Integer countDetail) {
		this.countDetail = countDetail;
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
	public String getDeadlineDateCompleteS() {
		return deadlineDateCompleteS;
	}
	public void setDeadlineDateCompleteS(String deadlineDateCompleteS) {
		this.deadlineDateCompleteS = deadlineDateCompleteS;
	}
	public Long getContactUnitId() {
		return contactUnitId;
	}
	public void setContactUnitId(Long contactUnitId) {
		this.contactUnitId = contactUnitId;
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
	public ContactUnitBO toModel() {
		ContactUnitBO bo = new ContactUnitBO();
		bo.setContactUnitId(this.contactUnitId);
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
		bo.setType(this.type);
		bo.setAreaCode(this.areaCode); //HienLT56 add 01072020
		bo.setUnitCode(this.unitCode); //HienLT56 add 01072020
		return bo;
	}
	
	//HienLT56 start 01072020
	private String areaCode;
	private String unitCode;
	private String name;
	private String code;
	private Long catProvinceId;
	private String contactDateS;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private Date contactDate;
	private String fullNameCus;
	private String fullNameEmploy;
	private String mailCus;
	private String mailEmploy;
	private String positionCus;
	private String shortContent;
	private String phoneNumberCus;
	private String resultS;
	private String phoneNumberEmploy;
	private String description;
	private ContactUnitDetailDTO contactUnitDetailDTO;
	
	
	
	public ContactUnitDetailDTO getContactUnitDetailDTO() {
		return contactUnitDetailDTO;
	}
	public void setContactUnitDetailDTO(ContactUnitDetailDTO contactUnitDetailDTO) {
		this.contactUnitDetailDTO = contactUnitDetailDTO;
	}
	public Date getContactDate() {
		return contactDate;
	}
	public void setContactDate(Date contactDate) {
		this.contactDate = contactDate;
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
	public String getPhoneNumberCus() {
		return phoneNumberCus;
	}
	public void setPhoneNumberCus(String phoneNumberCus) {
		this.phoneNumberCus = phoneNumberCus;
	}
	public String getResultS() {
		return resultS;
	}
	public void setResultS(String resultS) {
		this.resultS = resultS;
	}
	public String getPhoneNumberEmploy() {
		return phoneNumberEmploy;
	}
	public void setPhoneNumberEmploy(String phoneNumberEmploy) {
		this.phoneNumberEmploy = phoneNumberEmploy;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getContactDateS() {
		return contactDateS;
	}
	public void setContactDateS(String contactDateS) {
		this.contactDateS = contactDateS;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Long getCatProvinceId() {
		return catProvinceId;
	}
	public void setCatProvinceId(Long catProvinceId) {
		this.catProvinceId = catProvinceId;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getUnitCode() {
		return unitCode;
	}
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	
	//HienLT56 end 01072020
}
