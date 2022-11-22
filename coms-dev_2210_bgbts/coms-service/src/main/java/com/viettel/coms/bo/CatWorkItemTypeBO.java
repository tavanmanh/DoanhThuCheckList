package com.viettel.coms.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

@SuppressWarnings("serial")
@Entity(name = "com.viettel.coms.bo.CatWorkItemTypeBO")
@Table(name = "CAT_WORK_ITEM_TYPE")
public class CatWorkItemTypeBO extends BaseFWModelImpl {

	@Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "CAT_WORK_ITEM_TYPE_SEQ")})
	@Column(name = "CAT_WORK_ITEM_TYPE_ID", length = 10)
	private Long catWorkItemTypeId;
	@Column(name = "NAME", length = 200)
	private String name;
	@Column(name = "CODE", length = 50)
	private String code;
	@Column(name = "STATUS", length = 20)
	private String status;
	@Column(name = "DESCRIPTION", length = 1000)
	private String description;
	@Column(name = "CAT_CONSTRUCTION_TYPE_ID", length = 10)
	private Long catConstructionTypeId;
	@Column(name = "CREATED_DATE", length = 22)
	private Date createdDate;
	@Column(name = "UPDATED_DATE", length = 22)
	private Date updatedDate;
	@Column(name = "CREATED_USER", length = 10)
	private Long createdUser;
	@Column(name = "UPDATED_USER", length = 10)
	private Long updatedUser;
	@Column(name = "ITEM_ORDER", length = 10)
	private Long itemOrder;
	@Column(name = "TAB", length = 20)
	private String tab;
	@Column(name = "QUANTITY_BY_DATE", length = 1)
	private String quantityByDate;
	@Column(name = "CAT_WORK_ITEM_GROUP_ID", length = 11)
	private Long catWorkItemGroupId;
	@Column(name = "TYPE", length = 2)
	private Long type;
	@Column(name = "TR_BRANCH", length = 2)
	private Long trBranch;
	@Column(name = "HM_TYPE_VALUE", length = 2)
	private Long hmTypeValue;
	@Column(name = "HM_VALUE", length = 32)
	private Long hmValue;
	@Column(name = "HM_QUOTA_TIME", length = 6)
	private Long hmQuotaTime;

	public Long getCatWorkItemTypeId() {
		return catWorkItemTypeId;
	}

	public void setCatWorkItemTypeId(Long catWorkItemTypeId) {
		this.catWorkItemTypeId = catWorkItemTypeId;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getCatConstructionTypeId() {
		return catConstructionTypeId;
	}

	public void setCatConstructionTypeId(Long catConstructionTypeId) {
		this.catConstructionTypeId = catConstructionTypeId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Long getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(Long createdUser) {
		this.createdUser = createdUser;
	}

	public Long getUpdatedUser() {
		return updatedUser;
	}

	public void setUpdatedUser(Long updatedUser) {
		this.updatedUser = updatedUser;
	}

	public Long getItemOrder() {
		return itemOrder;
	}

	public void setItemOrder(Long itemOrder) {
		this.itemOrder = itemOrder;
	}

	public String getTab() {
		return tab;
	}

	public void setTab(String tab) {
		this.tab = tab;
	}

	public String getQuantityByDate() {
		return quantityByDate;
	}

	public void setQuantityByDate(String quantityByDate) {
		this.quantityByDate = quantityByDate;
	}

	public Long getCatWorkItemGroupId() {
		return catWorkItemGroupId;
	}

	public void setCatWorkItemGroupId(Long catWorkItemGroupId) {
		this.catWorkItemGroupId = catWorkItemGroupId;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public Long getTrBranch() {
		return trBranch;
	}

	public void setTrBranch(Long trBranch) {
		this.trBranch = trBranch;
	}

	public Long getHmTypeValue() {
		return hmTypeValue;
	}

	public void setHmTypeValue(Long hmTypeValue) {
		this.hmTypeValue = hmTypeValue;
	}

	public Long getHmValue() {
		return hmValue;
	}

	public void setHmValue(Long hmValue) {
		this.hmValue = hmValue;
	}

	public Long getHmQuotaTime() {
		return hmQuotaTime;
	}

	public void setHmQuotaTime(Long hmQuotaTime) {
		this.hmQuotaTime = hmQuotaTime;
	}

	@Override
	public BaseFWDTOImpl toDTO() {
		// TODO Auto-generated method stub
		return null;
	}

}
