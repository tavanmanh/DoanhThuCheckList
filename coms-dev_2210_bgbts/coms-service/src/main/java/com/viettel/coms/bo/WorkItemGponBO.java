package com.viettel.coms.bo;

import com.viettel.coms.dto.WorkItemGponDTO;
import com.viettel.service.base.model.BaseFWModelImpl;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@SuppressWarnings("serial")
@Entity(name = "com.viettel.erp.bo.WorkItemGponBO")
@Table(name = "WORK_ITEM_GPON")
/**
 *
 * @author: hailh10
 */
public class WorkItemGponBO extends BaseFWModelImpl {
     
	@Id
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = { @Parameter(name = "sequence", value = "WORK_ITEM_GPON_SEQ") })
	@Column(name = "WORK_ITEM_GPON_ID", length = 22)
	private java.lang.Long workItemGponId;
	@Column(name = "CONSTRUCTION_ID", length = 22)
	private java.lang.Long constructionId;
	@Column(name = "CAT_WORK_ITEM_TYPE_ID", length = 22)
	private java.lang.Long catWorkItemTypeId;
	@Column(name = "WORK_ITEM_ID", length = 22)
	private java.lang.Long workItemId;
	@Column(name = "CAT_TASK_ID", length = 22)
	private java.lang.Long catTaskId;
	@Column(name = "TASK_NAME", length = 200)
	private java.lang.String taskName;
	@Column(name = "AMOUNT", length = 22)
	private java.lang.Double amount;
	@Column(name = "PRICE", length = 22)
	private java.lang.Double price;
	@Column(name = "CREATED_DATE", length = 7)
	private java.util.Date createdDate;
	@Column(name = "UPDATED_DATE", length = 7)
	private java.util.Date updatedDate;
	@Column(name = "CONSTRUCTION_CODE", length = 20)
	private java.lang.String constructionCode;
	@Column(name = "WORK_ITEM_NAME", length = 200)
	private java.lang.String workItemName;
	@Column(name = "CREATED_USER_ID", length = 22)
	private java.lang.Long createdUserId;
	@Column(name = "UPDATE_USER_ID", length = 22)
	private java.lang.Long updateUserId;

	
	public java.lang.Long getWorkItemGponId(){
		return workItemGponId;
	}
	
	public void setWorkItemGponId(java.lang.Long workItemGponId)
	{
		this.workItemGponId = workItemGponId;
	}
	
	public java.lang.Long getConstructionId(){
		return constructionId;
	}
	
	public void setConstructionId(java.lang.Long constructionId)
	{
		this.constructionId = constructionId;
	}
	
	public java.lang.Long getCatWorkItemTypeId(){
		return catWorkItemTypeId;
	}
	
	public void setCatWorkItemTypeId(java.lang.Long catWorkItemTypeId)
	{
		this.catWorkItemTypeId = catWorkItemTypeId;
	}
	
	public java.lang.Long getWorkItemId(){
		return workItemId;
	}
	
	public void setWorkItemId(java.lang.Long workItemId)
	{
		this.workItemId = workItemId;
	}
	
	public java.lang.Long getCatTaskId(){
		return catTaskId;
	}
	
	public void setCatTaskId(java.lang.Long catTaskId)
	{
		this.catTaskId = catTaskId;
	}
	
	public java.lang.String getTaskName(){
		return taskName;
	}
	
	public void setTaskName(java.lang.String taskName)
	{
		this.taskName = taskName;
	}
	
	public java.lang.Double getAmount(){
		return amount;
	}
	
	public void setAmount(java.lang.Double amount)
	{
		this.amount = amount;
	}
	
	public java.lang.Double getPrice(){
		return price;
	}
	
	public void setPrice(java.lang.Double price)
	{
		this.price = price;
	}
	
	public java.util.Date getCreatedDate(){
		return createdDate;
	}
	
	public void setCreatedDate(java.util.Date createdDate)
	{
		this.createdDate = createdDate;
	}
	
	public java.util.Date getUpdatedDate(){
		return updatedDate;
	}
	
	public void setUpdatedDate(java.util.Date updatedDate)
	{
		this.updatedDate = updatedDate;
	}
	
	public java.lang.String getConstructionCode(){
		return constructionCode;
	}
	
	public void setConstructionCode(java.lang.String constructionCode)
	{
		this.constructionCode = constructionCode;
	}
	
	public java.lang.String getWorkItemName(){
		return workItemName;
	}
	
	public void setWorkItemName(java.lang.String workItemName)
	{
		this.workItemName = workItemName;
	}
	
	public java.lang.Long getCreatedUserId(){
		return createdUserId;
	}
	
	public void setCreatedUserId(java.lang.Long createdUserId)
	{
		this.createdUserId = createdUserId;
	}
	
	public java.lang.Long getUpdateUserId(){
		return updateUserId;
	}
	
	public void setUpdateUserId(java.lang.Long updateUserId)
	{
		this.updateUserId = updateUserId;
	}
   
    @Override
    public WorkItemGponDTO toDTO() {
        WorkItemGponDTO workItemGponDTO = new WorkItemGponDTO(); 
        workItemGponDTO.setWorkItemGponId(this.workItemGponId);		
        workItemGponDTO.setConstructionId(this.constructionId);		
        workItemGponDTO.setCatWorkItemTypeId(this.catWorkItemTypeId);		
        workItemGponDTO.setWorkItemId(this.workItemId);		
        workItemGponDTO.setCatTaskId(this.catTaskId);		
        workItemGponDTO.setTaskName(this.taskName);		
        workItemGponDTO.setAmount(this.amount);		
        workItemGponDTO.setPrice(this.price);		
        workItemGponDTO.setCreatedDate(this.createdDate);		
        workItemGponDTO.setUpdatedDate(this.updatedDate);		
        workItemGponDTO.setConstructionCode(this.constructionCode);		
        workItemGponDTO.setWorkItemName(this.workItemName);		
        workItemGponDTO.setCreatedUserId(this.createdUserId);		
        workItemGponDTO.setUpdateUserId(this.updateUserId);		
        return workItemGponDTO;
    }
}
