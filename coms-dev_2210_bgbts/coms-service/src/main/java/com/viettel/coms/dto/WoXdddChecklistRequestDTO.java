package com.viettel.coms.dto;

import java.util.Date;
import java.util.List;

import com.viettel.coms.bo.WoXdddChecklistBO;

public class WoXdddChecklistRequestDTO {

	private Long id;
    private Long woId;
    private String state;
    private Long status;
    private Double value;
    private String name;
    private Long confirm;
    private Date confirmDate;
    private String confirmBy;
    private Long hshc;
    private String loggedInUser;
    private Date createDate;
    private String userCreated;
    private Long constructionId;
    private String workItemName;
    private String constructionCode;
    private List<ImgChecklistDTO> lstAttach;
    
	public Long getId() {
		return id;
	}
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
	public List<ImgChecklistDTO> getLstAttach() {
		return lstAttach;
	}
	public void setLstAttach(List<ImgChecklistDTO> lstAttach) {
		this.lstAttach = lstAttach;
	}
    
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
}
