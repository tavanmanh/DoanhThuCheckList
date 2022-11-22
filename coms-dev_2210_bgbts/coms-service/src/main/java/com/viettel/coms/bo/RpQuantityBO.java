package com.viettel.coms.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.viettel.coms.dto.RpQuantityDTO;
import com.viettel.service.base.model.BaseFWModelImpl;

@SuppressWarnings("serial")
@Entity(name = "com.viettel.erp.bo.RpQuantityBO")
@Table(name = "RP_QUANTITY")
/**
 *
 * @author: hailh10
 */
public class RpQuantityBO extends BaseFWModelImpl {
     
	@Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence", value = "RP_QUANTITY_SEQ")})
	@Column(name = "RP_QUANTITY_ID", length = 22)
	private java.lang.Long rpQuantityId;
	@Column(name = "STARTING_DATE", length = 7)
	private java.util.Date startingDate;
	@Column(name = "COMPLETE_DATE", length = 7)
	private java.util.Date completeDate;
	@Column(name = "PERFORMER_ID", length = 22)
	private java.lang.Long performerId;
	@Column(name = "PERFORMER_NAME", length = 100)
	private java.lang.String performerName;
	@Column(name = "SYS_GROUP_ID", length = 22)
	private java.lang.Long sysGroupId;
	@Column(name = "SYSGROUPNAME", length = 100)
	private java.lang.String sysgroupname;
	@Column(name = "CATSTATIONCODE", length = 100)
	private java.lang.String catstationcode;
	@Column(name = "CONSTRUCTIONID", length = 22)
	private java.lang.Long constructionid;
	@Column(name = "CONSTRUCTIONCODE", length = 100)
	private java.lang.String constructioncode;
	@Column(name = "WORKITEMID", length = 22)
	private java.lang.Long workitemid;
	@Column(name = "WORKITEMNAME", length = 500)
	private java.lang.String workitemname;
	@Column(name = "QUANTITY", length = 22)
	private java.lang.Double quantity;
	@Column(name = "QUANTITYBYDATE", length = 10)
	private java.lang.String quantitybydate;
	@Column(name = "STATUS", length = 10)
	private java.lang.String status;
	@Column(name = "CNTCONTRACTCODE", length = 100)
	private java.lang.String cntcontractcode;
	@Column(name = "CATPROVINCEID", length = 22)
	private java.lang.Long catprovinceid;
	@Column(name = "CATPROVINCECODE", length = 100)
	private java.lang.String catprovincecode;
	@Column(name = "STATUSCONSTRUCTION", length = 10)
	private java.lang.String statusconstruction;
	@Column(name = "APPROVECOMPLETEVALUE", length = 22)
	private java.lang.Double approvecompletevalue;
	@Column(name = "PRICE", length = 22)
	private java.lang.Double price;
	@Column(name = "OBSTRUCTEDSTATE", length = 10)
	private java.lang.String obstructedstate;
	@Column(name = "APPROVECOMPLETEDATE", length = 7)
	private java.util.Date approvecompletedate;
	@Column(name = "CAT_PARTNER_ID", length = 22)
	private java.lang.Long catPartnerId;
	@Column(name = "PARTNERNAME", length = 1000)
	private java.lang.String partnername;
	@Column(name = "PROCESS_DATE", length = 10)
	private java.lang.String processDate;
	@Column(name = "INSERT_TIME", length = 7)
	private java.util.Date insertTime;
	@Column(name = "SOURCEWORK", length = 7)
	private String sourceWork;
	@Column(name = "CONSTRUCTIONTYPE", length = 7)
	private String constructionType;
	
	public String getSourceWork() {
		return sourceWork;
	}

	public void setSourceWork(String sourceWork) {
		this.sourceWork = sourceWork;
	}

	public String getConstructionType() {
		return constructionType;
	}

	public void setConstructionType(String constructionType) {
		this.constructionType = constructionType;
	}

	public java.util.Date getInsertTime(){
		return insertTime;
	}
	
	public void setInsertTime(java.util.Date insertTime)
	{
		this.insertTime = insertTime;
	}
	
	public java.lang.Long getRpQuantityId(){
		return rpQuantityId;
	}
	
	public void setRpQuantityId(java.lang.Long rpQuantityId)
	{
		this.rpQuantityId = rpQuantityId;
	}
	
	public java.util.Date getStartingDate(){
		return startingDate;
	}
	
	public void setStartingDate(java.util.Date startingDate)
	{
		this.startingDate = startingDate;
	}
	
	public java.util.Date getCompleteDate(){
		return completeDate;
	}
	
	public void setCompleteDate(java.util.Date completeDate)
	{
		this.completeDate = completeDate;
	}
	
	public java.lang.Long getPerformerId(){
		return performerId;
	}
	
	public void setPerformerId(java.lang.Long performerId)
	{
		this.performerId = performerId;
	}
	
	public java.lang.String getPerformerName(){
		return performerName;
	}
	
	public void setPerformerName(java.lang.String performerName)
	{
		this.performerName = performerName;
	}
	
	public java.lang.Long getSysGroupId(){
		return sysGroupId;
	}
	
	public void setSysGroupId(java.lang.Long sysGroupId)
	{
		this.sysGroupId = sysGroupId;
	}
	
	public java.lang.String getSysgroupname(){
		return sysgroupname;
	}
	
	public void setSysgroupname(java.lang.String sysgroupname)
	{
		this.sysgroupname = sysgroupname;
	}
	
	public java.lang.String getCatstationcode(){
		return catstationcode;
	}
	
	public void setCatstationcode(java.lang.String catstationcode)
	{
		this.catstationcode = catstationcode;
	}
	
	public java.lang.Long getConstructionid(){
		return constructionid;
	}
	
	public void setConstructionid(java.lang.Long constructionid)
	{
		this.constructionid = constructionid;
	}
	
	public java.lang.String getConstructioncode(){
		return constructioncode;
	}
	
	public void setConstructioncode(java.lang.String constructioncode)
	{
		this.constructioncode = constructioncode;
	}
	
	public java.lang.Long getWorkitemid(){
		return workitemid;
	}
	
	public void setWorkitemid(java.lang.Long workitemid)
	{
		this.workitemid = workitemid;
	}
	
	public java.lang.String getWorkitemname(){
		return workitemname;
	}
	
	public void setWorkitemname(java.lang.String workitemname)
	{
		this.workitemname = workitemname;
	}
	
	public java.lang.Double getQuantity(){
		return quantity;
	}
	
	public void setQuantity(java.lang.Double quantity)
	{
		this.quantity = quantity;
	}
	
	public java.lang.String getQuantitybydate(){
		return quantitybydate;
	}
	
	public void setQuantitybydate(java.lang.String quantitybydate)
	{
		this.quantitybydate = quantitybydate;
	}
	
	public java.lang.String getStatus(){
		return status;
	}
	
	public void setStatus(java.lang.String status)
	{
		this.status = status;
	}
	
	public java.lang.String getCntcontractcode(){
		return cntcontractcode;
	}
	
	public void setCntcontractcode(java.lang.String cntcontractcode)
	{
		this.cntcontractcode = cntcontractcode;
	}
	
	public java.lang.Long getCatprovinceid(){
		return catprovinceid;
	}
	
	public void setCatprovinceid(java.lang.Long catprovinceid)
	{
		this.catprovinceid = catprovinceid;
	}
	
	public java.lang.String getCatprovincecode(){
		return catprovincecode;
	}
	
	public void setCatprovincecode(java.lang.String catprovincecode)
	{
		this.catprovincecode = catprovincecode;
	}
	
	public java.lang.String getStatusconstruction(){
		return statusconstruction;
	}
	
	public void setStatusconstruction(java.lang.String statusconstruction)
	{
		this.statusconstruction = statusconstruction;
	}
	
	public java.lang.Double getApprovecompletevalue(){
		return approvecompletevalue;
	}
	
	public void setApprovecompletevalue(java.lang.Double approvecompletevalue)
	{
		this.approvecompletevalue = approvecompletevalue;
	}
	
	public java.lang.Double getPrice(){
		return price;
	}
	
	public void setPrice(java.lang.Double price)
	{
		this.price = price;
	}
	
	public java.lang.String getObstructedstate(){
		return obstructedstate;
	}
	
	public void setObstructedstate(java.lang.String obstructedstate)
	{
		this.obstructedstate = obstructedstate;
	}
	
	public java.util.Date getApprovecompletedate(){
		return approvecompletedate;
	}
	
	public void setApprovecompletedate(java.util.Date approvecompletedate)
	{
		this.approvecompletedate = approvecompletedate;
	}
	
	public java.lang.Long getCatPartnerId(){
		return catPartnerId;
	}
	
	public void setCatPartnerId(java.lang.Long catPartnerId)
	{
		this.catPartnerId = catPartnerId;
	}
	
	public java.lang.String getPartnername(){
		return partnername;
	}
	
	public void setPartnername(java.lang.String partnername)
	{
		this.partnername = partnername;
	}
	
	public java.lang.String getProcessDate(){
		return processDate;
	}
	
	public void setProcessDate(java.lang.String processDate)
	{
		this.processDate = processDate;
	}
   
    @Override
    public RpQuantityDTO toDTO() {
        RpQuantityDTO rpQuantityDTO = new RpQuantityDTO(); 
        rpQuantityDTO.setInsertTime(this.insertTime);		
        rpQuantityDTO.setRpQuantityId(this.rpQuantityId);		
        rpQuantityDTO.setStartingDate(this.startingDate);		
        rpQuantityDTO.setCompleteDate(this.completeDate);		
        rpQuantityDTO.setPerformerId(this.performerId);		
        rpQuantityDTO.setPerformerName(this.performerName);		
        rpQuantityDTO.setSysGroupId(this.sysGroupId);		
        rpQuantityDTO.setSysgroupname(this.sysgroupname);		
        rpQuantityDTO.setCatstationcode(this.catstationcode);		
        rpQuantityDTO.setConstructionid(this.constructionid);		
        rpQuantityDTO.setConstructioncode(this.constructioncode);		
        rpQuantityDTO.setWorkitemid(this.workitemid);		
        rpQuantityDTO.setWorkitemname(this.workitemname);		
        rpQuantityDTO.setQuantity(this.quantity);		
        rpQuantityDTO.setQuantitybydate(this.quantitybydate);		
        rpQuantityDTO.setStatus(this.status);		
        rpQuantityDTO.setCntcontractcode(this.cntcontractcode);		
        rpQuantityDTO.setCatprovinceid(this.catprovinceid);		
        rpQuantityDTO.setCatprovincecode(this.catprovincecode);		
        rpQuantityDTO.setStatusconstruction(this.statusconstruction);		
        rpQuantityDTO.setApprovecompletevalue(this.approvecompletevalue);		
        rpQuantityDTO.setPrice(this.price);		
        rpQuantityDTO.setObstructedstate(this.obstructedstate);		
        rpQuantityDTO.setApprovecompletedate(this.approvecompletedate);		
        rpQuantityDTO.setCatPartnerId(this.catPartnerId);		
        rpQuantityDTO.setPartnername(this.partnername);		
        rpQuantityDTO.setProcessDate(this.processDate);	
        rpQuantityDTO.setSourceWork(this.sourceWork);
        rpQuantityDTO.setConstructionType(this.constructionType);
        return rpQuantityDTO;
    }
}
