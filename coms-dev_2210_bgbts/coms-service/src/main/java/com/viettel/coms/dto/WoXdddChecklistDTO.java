package com.viettel.coms.dto;

import com.viettel.coms.bo.WoXdddChecklistBO;

import java.util.Date;

public class WoXdddChecklistDTO extends ComsBaseFWDTO<WoXdddChecklistBO> {

    public Long id;
    public Long woId;
    public String state;
    public Long status;
    public Double value;
    public String name;
    public Long confirm;
    public Date confirmDate;
    public String confirmBy;
    public Long hshc;
    public String loggedInUser;
    public Date createDate;
    public String userCreated;
    public Long constructionId;
    public String workItemName;
    public String constructionCode;
    public Long woPmxlId;
    public String keyMap;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getWoId() {
        return woId;
    }

    public void setWoId(Long woId) {
        this.woId = woId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getConfirm() {
        return confirm;
    }

    public void setConfirm(Long confirm) {
        this.confirm = confirm;
    }

    public Date getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(Date confirmDate) {
        this.confirmDate = confirmDate;
    }

    public String getConfirmBy() {
        return confirmBy;
    }

    public void setConfirmBy(String confirmBy) {
        this.confirmBy = confirmBy;
    }

    public Long getHshc() {
        return hshc;
    }

    public void setHshc(Long hshc) {
        this.hshc = hshc;
    }

    public String getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(String loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(String userCreated) {
        this.userCreated = userCreated;
    }

    public Long getConstructionId() {
		return constructionId;
	}

	public void setConstructionId(Long constructionId) {
		this.constructionId = constructionId;
	}

	public String getWorkItemName() {
		return workItemName;
	}

	public void setWorkItemName(String workItemName) {
		this.workItemName = workItemName;
	}

	public String getConstructionCode() {
		return constructionCode;
	}

	public void setConstructionCode(String constructionCode) {
		this.constructionCode = constructionCode;
	}

	public Long getWoPmxlId() {
		return woPmxlId;
	}

	public void setWoPmxlId(Long woPmxlId) {
		this.woPmxlId = woPmxlId;
	}

	public String getKeyMap() {
		return keyMap;
	}

	public void setKeyMap(String keyMap) {
		this.keyMap = keyMap;
	}

	@Override
    public WoXdddChecklistBO toModel() {
        WoXdddChecklistBO bo = new WoXdddChecklistBO();

        bo.setId(this.id);
        bo.setWoId(this.woId);
        bo.setState(this.state);
        bo.setStatus(this.status);
        bo.setValue(this.value);
        bo.setName(this.name);
        bo.setConfirm(this.confirm);
        bo.setConfirmDate(this.confirmDate);
        bo.setConfirmBy(this.confirmBy);
        bo.setHshc(this.hshc);
        bo.setCreateDate(this.createDate);
        bo.setUserCreated(this.userCreated);

        return bo;
    }

    @Override
    public Long getFWModelId() {
        return null;
    }

    @Override
    public String catchName() {
        return null;
    }

    public WoMappingChecklistDTO toMappingChecklist(){
        WoMappingChecklistDTO checklist = new WoMappingChecklistDTO();

        checklist.setId(this.id);
        checklist.setWoId(this.woId);
        checklist.setState(this.state);
        checklist.setStatus(this.status);
        checklist.setValue(this.value);
        checklist.setName(this.name);
        checklist.setConfirm(this.confirm);
        checklist.setConfirmDate(this.confirmDate);
        checklist.setConfirmBy(this.confirmBy);
        checklist.setHshc(this.hshc);

        return checklist;
    }
}
