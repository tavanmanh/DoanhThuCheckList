package com.viettel.coms.dto;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.WorkItemGponBO;
import com.viettel.erp.utils.CustomJsonDateDeserializer;
import com.viettel.erp.utils.CustomJsonDateSerializer;

/**
 *
 * @author hailh10
 */
@SuppressWarnings("serial")
@XmlRootElement(name = "WORK_ITEM_GPONBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkItemGponDTO extends ComsBaseFWDTO<WorkItemGponBO> {

	private java.lang.Long workItemGponId;
	private java.lang.Long constructionId;
	private java.lang.String constructionName;
	private java.lang.Long catWorkItemTypeId;
	private java.lang.String catWorkItemTypeName;
	private java.lang.Long workItemId;
	private java.lang.String workItemName;
	private java.lang.Long catTaskId;
	private java.lang.String catTaskName;
	private java.lang.String taskName;
	private java.lang.Double amount;
	private java.lang.Double price;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date createdDate;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date createdDateFrom;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date createdDateTo;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date updatedDate;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date updatedDateFrom;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date updatedDateTo;
	private java.lang.String constructionCode;
	private java.lang.Long createdUserId;
	private java.lang.String createdUserName;
	private java.lang.Long updateUserId;
	private java.lang.String updateUserName;
	private String text;
	private int start;
	private int maxResult;
	private String name ;
	private java.lang.String keySearch;
	

    public java.lang.String getKeySearch() {
		return keySearch;
	}

	public void setKeySearch(java.lang.String keySearch) {
		this.keySearch = keySearch;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
    public WorkItemGponBO toModel() {
        WorkItemGponBO workItemGponBO = new WorkItemGponBO();
        workItemGponBO.setWorkItemGponId(this.workItemGponId);
        workItemGponBO.setConstructionId(this.constructionId);
        workItemGponBO.setCatWorkItemTypeId(this.catWorkItemTypeId);
        workItemGponBO.setWorkItemId(this.workItemId);
        workItemGponBO.setCatTaskId(this.catTaskId);
        workItemGponBO.setTaskName(this.taskName);
        workItemGponBO.setAmount(this.amount);
        workItemGponBO.setPrice(this.price);
        workItemGponBO.setCreatedDate(this.createdDate);
        workItemGponBO.setUpdatedDate(this.updatedDate);
        workItemGponBO.setConstructionCode(this.constructionCode);
        workItemGponBO.setWorkItemName(this.workItemName);
        workItemGponBO.setCreatedUserId(this.createdUserId);
        workItemGponBO.setUpdateUserId(this.updateUserId);
        return workItemGponBO;
    }

    @Override
     public Long getFWModelId() {
        return workItemGponId;
    }
   
    @Override
    public String catchName() {
        return getWorkItemGponId().toString();
    }
	
	@JsonProperty("workItemGponId")
    public java.lang.Long getWorkItemGponId(){
		return workItemGponId;
    }
	
    public void setWorkItemGponId(java.lang.Long workItemGponId){
		this.workItemGponId = workItemGponId;
    }	
	
	@JsonProperty("constructionId")
    public java.lang.Long getConstructionId(){
		return constructionId;
    }
	
    public void setConstructionId(java.lang.Long constructionId){
		this.constructionId = constructionId;
    }	
	
	@JsonProperty("constructionName")
    public java.lang.String getConstructionName(){
		return constructionName;
    }
	
    public void setConstructionName(java.lang.String constructionName){
		this.constructionName = constructionName;
    }	
	
	@JsonProperty("catWorkItemTypeId")
    public java.lang.Long getCatWorkItemTypeId(){
		return catWorkItemTypeId;
    }
	
    public void setCatWorkItemTypeId(java.lang.Long catWorkItemTypeId){
		this.catWorkItemTypeId = catWorkItemTypeId;
    }	
	
	@JsonProperty("catWorkItemTypeName")
    public java.lang.String getCatWorkItemTypeName(){
		return catWorkItemTypeName;
    }
	
    public void setCatWorkItemTypeName(java.lang.String catWorkItemTypeName){
		this.catWorkItemTypeName = catWorkItemTypeName;
    }	
	
	@JsonProperty("workItemId")
    public java.lang.Long getWorkItemId(){
		return workItemId;
    }
	
    public void setWorkItemId(java.lang.Long workItemId){
		this.workItemId = workItemId;
    }	
	
	@JsonProperty("workItemName")
    public java.lang.String getWorkItemName(){
		return workItemName;
    }
	
    public void setWorkItemName(java.lang.String workItemName){
		this.workItemName = workItemName;
    }	
	
	@JsonProperty("catTaskId")
    public java.lang.Long getCatTaskId(){
		return catTaskId;
    }
	
    public void setCatTaskId(java.lang.Long catTaskId){
		this.catTaskId = catTaskId;
    }	
	
	@JsonProperty("catTaskName")
    public java.lang.String getCatTaskName(){
		return catTaskName;
    }
	
    public void setCatTaskName(java.lang.String catTaskName){
		this.catTaskName = catTaskName;
    }	
	
	@JsonProperty("taskName")
    public java.lang.String getTaskName(){
		return taskName;
    }
	
    public void setTaskName(java.lang.String taskName){
		this.taskName = taskName;
    }	
	
	@JsonProperty("amount")
    public java.lang.Double getAmount(){
		return amount;
    }
	
    public void setAmount(java.lang.Double amount){
		this.amount = amount;
    }	
	
	@JsonProperty("price")
    public java.lang.Double getPrice(){
		return price;
    }
	
    public void setPrice(java.lang.Double price){
		this.price = price;
    }	
	
	@JsonProperty("createdDate")
    public java.util.Date getCreatedDate(){
		return createdDate;
    }
	
    public void setCreatedDate(java.util.Date createdDate){
		this.createdDate = createdDate;
    }	
	
	public java.util.Date getCreatedDateFrom() {
    	return createdDateFrom;
    }
	
    public void setCreatedDateFrom(java.util.Date createdDateFrom) {
    	this.createdDateFrom = createdDateFrom;
    }
	
	public java.util.Date getCreatedDateTo() {
    	return createdDateTo;
    }
	
    public void setCreatedDateTo(java.util.Date createdDateTo) {
    	this.createdDateTo = createdDateTo;
    }
	
	@JsonProperty("updatedDate")
    public java.util.Date getUpdatedDate(){
		return updatedDate;
    }
	
    public void setUpdatedDate(java.util.Date updatedDate){
		this.updatedDate = updatedDate;
    }	
	
	public java.util.Date getUpdatedDateFrom() {
    	return updatedDateFrom;
    }
	
    public void setUpdatedDateFrom(java.util.Date updatedDateFrom) {
    	this.updatedDateFrom = updatedDateFrom;
    }
	
	public java.util.Date getUpdatedDateTo() {
    	return updatedDateTo;
    }
	
    public void setUpdatedDateTo(java.util.Date updatedDateTo) {
    	this.updatedDateTo = updatedDateTo;
    }
	
	@JsonProperty("constructionCode")
    public java.lang.String getConstructionCode(){
		return constructionCode;
    }
	
    public void setConstructionCode(java.lang.String constructionCode){
		this.constructionCode = constructionCode;
    }	
	
	
	@JsonProperty("createdUserId")
    public java.lang.Long getCreatedUserId(){
		return createdUserId;
    }
	
    public void setCreatedUserId(java.lang.Long createdUserId){
		this.createdUserId = createdUserId;
    }	
	
	@JsonProperty("createdUserName")
    public java.lang.String getCreatedUserName(){
		return createdUserName;
    }
	
    public void setCreatedUserName(java.lang.String createdUserName){
		this.createdUserName = createdUserName;
    }	
	
	@JsonProperty("updateUserId")
    public java.lang.Long getUpdateUserId(){
		return updateUserId;
    }
	
    public void setUpdateUserId(java.lang.Long updateUserId){
		this.updateUserId = updateUserId;
    }	
	
	@JsonProperty("updateUserName")
    public java.lang.String getUpdateUserName(){
		return updateUserName;
    }
	
    public void setUpdateUserName(java.lang.String updateUserName){
		this.updateUserName = updateUserName;
    }	
	
	@JsonProperty("start")
	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	@JsonProperty("maxResult")
	public int getMaxResult() {
		return maxResult;
	}

	public void setMaxResult(int maxResult) {
		this.maxResult = maxResult;
	}
	
	@JsonProperty("text")
	public String getText() {
		return "";
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	//Huypq-03082020-start
	private Long catConstructionTypeId;

	public Long getCatConstructionTypeId() {
		return catConstructionTypeId;
	}

	public void setCatConstructionTypeId(Long catConstructionTypeId) {
		this.catConstructionTypeId = catConstructionTypeId;
	}
	
	//Huy-end
}
