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
import com.viettel.service.base.model.BaseFWModelImpl;
@SuppressWarnings("serial")
@Entity(name = "com.viettel.coms.ContactUnitBO")
@Table(name = "CONTACT_UNIT")
public class ContactUnitBO extends BaseFWModelImpl {
	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = { @Parameter(name = "sequence", value = "CONTACT_UNIT_SEQ") })
	@Column(name = "CONTACT_UNIT_ID", length = 22)
	private Long contactUnitId;
	@Column(name = "CREATE_DATE", length = 22)
	private Date createDate;
	@Column(name = "CREATE_USER_ID", length = 22)
	private Long createUserId;
	@Column(name = "DEADLINE_DATE_COMPLETE", length = 22)
	private java.util.Date deadlineDateComplete;
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
	@Column(name = "TYPE", length = 22)
	private Long type;
	
	
	public Long getType() {
		return type;
	}
	public void setType(Long type) {
		this.type = type;
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
	public ContactUnitDTO toDTO() {
		ContactUnitDTO bo = new ContactUnitDTO();
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
	@Column(name = "AREA_CODE", length = 200 )
	private String areaCode;
	@Column(name = "UNIT_CODE", length = 200 )
	private String unitCode;


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
