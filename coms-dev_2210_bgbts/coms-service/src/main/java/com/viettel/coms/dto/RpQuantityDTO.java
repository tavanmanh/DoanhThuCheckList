package com.viettel.coms.dto;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.RpQuantityBO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.utils.CustomJsonDateDeserializer;
import com.viettel.utils.CustomJsonDateSerializer;

@SuppressWarnings("serial")
@XmlRootElement(name = "RP_QUANTITYBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RpQuantityDTO extends BaseFWDTOImpl<RpQuantityBO> {

	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date insertTime;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date insertTimeFrom;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date insertTimeTo;
	private java.lang.Long rpQuantityId;
	private java.lang.String rpQuantityName;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date startingDate;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date startingDateFrom;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date startingDateTo;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date completeDate;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date completeDateFrom;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date completeDateTo;
	private java.lang.Long performerId;
	private java.lang.String performerName;
	private java.lang.Long sysGroupId;
	private java.lang.String sysGroupName;
	private java.lang.String sysgroupname;
	private java.lang.String catstationcode;
	private java.lang.Long constructionid;
	private java.lang.String constructioncode;
	private java.lang.Long workitemid;
	private java.lang.String workitemname;
	private java.lang.Double quantity;
	private java.lang.String quantitybydate;
	private java.lang.String status;
	private java.lang.String cntcontractcode;
	private java.lang.Long catprovinceid;
	private java.lang.String catprovincecode;
	private java.lang.String statusconstruction;
	private java.lang.Double approvecompletevalue;
	private java.lang.Double price;
	private java.lang.String obstructedstate;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date approvecompletedate;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date approvecompletedateFrom;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date approvecompletedateTo;
	private java.lang.Long catPartnerId;
	private java.lang.String catPartnerName;
	private java.lang.String partnername;
	private java.lang.String processDate;
	private String text;
	private int start;
	private int maxResult;
	private String sourceWork;
	private String constructionType;

	@JsonProperty("sourceWork")
    public String getSourceWork() {
		return sourceWork;
	}

	public void setSourceWork(String sourceWork) {
		this.sourceWork = sourceWork;
	}

	@JsonProperty("constructionType")
	public String getConstructionType() {
		return constructionType;
	}

	public void setConstructionType(String constructionType) {
		this.constructionType = constructionType;
	}

	@Override
    public RpQuantityBO toModel() {
        RpQuantityBO rpQuantityBO = new RpQuantityBO();
        rpQuantityBO.setInsertTime(this.insertTime);
        rpQuantityBO.setRpQuantityId(this.rpQuantityId);
        rpQuantityBO.setStartingDate(this.startingDate);
        rpQuantityBO.setCompleteDate(this.completeDate);
        rpQuantityBO.setPerformerId(this.performerId);
        rpQuantityBO.setPerformerName(this.performerName);
        rpQuantityBO.setSysGroupId(this.sysGroupId);
        rpQuantityBO.setSysgroupname(this.sysgroupname);
        rpQuantityBO.setCatstationcode(this.catstationcode);
        rpQuantityBO.setConstructionid(this.constructionid);
        rpQuantityBO.setConstructioncode(this.constructioncode);
        rpQuantityBO.setWorkitemid(this.workitemid);
        rpQuantityBO.setWorkitemname(this.workitemname);
        rpQuantityBO.setQuantity(this.quantity);
        rpQuantityBO.setQuantitybydate(this.quantitybydate);
        rpQuantityBO.setStatus(this.status);
        rpQuantityBO.setCntcontractcode(this.cntcontractcode);
        rpQuantityBO.setCatprovinceid(this.catprovinceid);
        rpQuantityBO.setCatprovincecode(this.catprovincecode);
        rpQuantityBO.setStatusconstruction(this.statusconstruction);
        rpQuantityBO.setApprovecompletevalue(this.approvecompletevalue);
        rpQuantityBO.setPrice(this.price);
        rpQuantityBO.setObstructedstate(this.obstructedstate);
        rpQuantityBO.setApprovecompletedate(this.approvecompletedate);
        rpQuantityBO.setCatPartnerId(this.catPartnerId);
        rpQuantityBO.setPartnername(this.partnername);
        rpQuantityBO.setProcessDate(this.processDate);
        rpQuantityBO.setSourceWork(this.sourceWork);
        rpQuantityBO.setConstructionType(this.constructionType);
        return rpQuantityBO;
    }

	@JsonProperty("insertTime")
    public java.util.Date getInsertTime(){
		return insertTime;
    }
	
    public void setInsertTime(java.util.Date insertTime){
		this.insertTime = insertTime;
    }	
	
	public java.util.Date getInsertTimeFrom() {
    	return insertTimeFrom;
    }
	
    public void setInsertTimeFrom(java.util.Date insertTimeFrom) {
    	this.insertTimeFrom = insertTimeFrom;
    }
	
	public java.util.Date getInsertTimeTo() {
    	return insertTimeTo;
    }
	
    public void setInsertTimeTo(java.util.Date insertTimeTo) {
    	this.insertTimeTo = insertTimeTo;
    }
	
	@JsonProperty("rpQuantityId")
    public java.lang.Long getRpQuantityId(){
		return rpQuantityId;
    }
	
    public void setRpQuantityId(java.lang.Long rpQuantityId){
		this.rpQuantityId = rpQuantityId;
    }	
	
	@JsonProperty("rpQuantityName")
    public java.lang.String getRpQuantityName(){
		return rpQuantityName;
    }
	
    public void setRpQuantityName(java.lang.String rpQuantityName){
		this.rpQuantityName = rpQuantityName;
    }	
	
	@JsonProperty("startingDate")
    public java.util.Date getStartingDate(){
		return startingDate;
    }
	
    public void setStartingDate(java.util.Date startingDate){
		this.startingDate = startingDate;
    }	
	
	public java.util.Date getStartingDateFrom() {
    	return startingDateFrom;
    }
	
    public void setStartingDateFrom(java.util.Date startingDateFrom) {
    	this.startingDateFrom = startingDateFrom;
    }
	
	public java.util.Date getStartingDateTo() {
    	return startingDateTo;
    }
	
    public void setStartingDateTo(java.util.Date startingDateTo) {
    	this.startingDateTo = startingDateTo;
    }
	
	@JsonProperty("completeDate")
    public java.util.Date getCompleteDate(){
		return completeDate;
    }
	
    public void setCompleteDate(java.util.Date completeDate){
		this.completeDate = completeDate;
    }	
	
	public java.util.Date getCompleteDateFrom() {
    	return completeDateFrom;
    }
	
    public void setCompleteDateFrom(java.util.Date completeDateFrom) {
    	this.completeDateFrom = completeDateFrom;
    }
	
	public java.util.Date getCompleteDateTo() {
    	return completeDateTo;
    }
	
    public void setCompleteDateTo(java.util.Date completeDateTo) {
    	this.completeDateTo = completeDateTo;
    }
	
	@JsonProperty("performerId")
    public java.lang.Long getPerformerId(){
		return performerId;
    }
	
    public void setPerformerId(java.lang.Long performerId){
		this.performerId = performerId;
    }	
	
	@JsonProperty("performerName")
    public java.lang.String getPerformerName(){
		return performerName;
    }
	
    public void setPerformerName(java.lang.String performerName){
		this.performerName = performerName;
    }	
	
	@JsonProperty("sysGroupId")
    public java.lang.Long getSysGroupId(){
		return sysGroupId;
    }
	
    public void setSysGroupId(java.lang.Long sysGroupId){
		this.sysGroupId = sysGroupId;
    }	
	
	@JsonProperty("sysGroupName")
    public java.lang.String getSysGroupName(){
		return sysGroupName;
    }
	
    public void setSysGroupName(java.lang.String sysGroupName){
		this.sysGroupName = sysGroupName;
    }	
	
	@JsonProperty("sysgroupname")
    public java.lang.String getSysgroupname(){
		return sysgroupname;
    }
	
    public void setSysgroupname(java.lang.String sysgroupname){
		this.sysgroupname = sysgroupname;
    }	
	
	@JsonProperty("catstationcode")
    public java.lang.String getCatstationcode(){
		return catstationcode;
    }
	
    public void setCatstationcode(java.lang.String catstationcode){
		this.catstationcode = catstationcode;
    }	
	
	@JsonProperty("constructionid")
    public java.lang.Long getConstructionid(){
		return constructionid;
    }
	
    public void setConstructionid(java.lang.Long constructionid){
		this.constructionid = constructionid;
    }	
	
	@JsonProperty("constructioncode")
    public java.lang.String getConstructioncode(){
		return constructioncode;
    }
	
    public void setConstructioncode(java.lang.String constructioncode){
		this.constructioncode = constructioncode;
    }	
	
	@JsonProperty("workitemid")
    public java.lang.Long getWorkitemid(){
		return workitemid;
    }
	
    public void setWorkitemid(java.lang.Long workitemid){
		this.workitemid = workitemid;
    }	
	
	@JsonProperty("workitemname")
    public java.lang.String getWorkitemname(){
		return workitemname;
    }
	
    public void setWorkitemname(java.lang.String workitemname){
		this.workitemname = workitemname;
    }	
	
	@JsonProperty("quantity")
    public java.lang.Double getQuantity(){
		return quantity;
    }
	
    public void setQuantity(java.lang.Double quantity){
		this.quantity = quantity;
    }	
	
	@JsonProperty("quantitybydate")
    public java.lang.String getQuantitybydate(){
		return quantitybydate;
    }
	
    public void setQuantitybydate(java.lang.String quantitybydate){
		this.quantitybydate = quantitybydate;
    }	
	
	@JsonProperty("status")
    public java.lang.String getStatus(){
		return status;
    }
	
    public void setStatus(java.lang.String status){
		this.status = status;
    }	
	
	@JsonProperty("cntcontractcode")
    public java.lang.String getCntcontractcode(){
		return cntcontractcode;
    }
	
    public void setCntcontractcode(java.lang.String cntcontractcode){
		this.cntcontractcode = cntcontractcode;
    }	
	
	@JsonProperty("catprovinceid")
    public java.lang.Long getCatprovinceid(){
		return catprovinceid;
    }
	
    public void setCatprovinceid(java.lang.Long catprovinceid){
		this.catprovinceid = catprovinceid;
    }	
	
	@JsonProperty("catprovincecode")
    public java.lang.String getCatprovincecode(){
		return catprovincecode;
    }
	
    public void setCatprovincecode(java.lang.String catprovincecode){
		this.catprovincecode = catprovincecode;
    }	
	
	@JsonProperty("statusconstruction")
    public java.lang.String getStatusconstruction(){
		return statusconstruction;
    }
	
    public void setStatusconstruction(java.lang.String statusconstruction){
		this.statusconstruction = statusconstruction;
    }	
	
	@JsonProperty("approvecompletevalue")
    public java.lang.Double getApprovecompletevalue(){
		return approvecompletevalue;
    }
	
    public void setApprovecompletevalue(java.lang.Double approvecompletevalue){
		this.approvecompletevalue = approvecompletevalue;
    }	
	
	@JsonProperty("price")
    public java.lang.Double getPrice(){
		return price;
    }
	
    public void setPrice(java.lang.Double price){
		this.price = price;
    }	
	
	@JsonProperty("obstructedstate")
    public java.lang.String getObstructedstate(){
		return obstructedstate;
    }
	
    public void setObstructedstate(java.lang.String obstructedstate){
		this.obstructedstate = obstructedstate;
    }	
	
	@JsonProperty("approvecompletedate")
    public java.util.Date getApprovecompletedate(){
		return approvecompletedate;
    }
	
    public void setApprovecompletedate(java.util.Date approvecompletedate){
		this.approvecompletedate = approvecompletedate;
    }	
	
	public java.util.Date getApprovecompletedateFrom() {
    	return approvecompletedateFrom;
    }
	
    public void setApprovecompletedateFrom(java.util.Date approvecompletedateFrom) {
    	this.approvecompletedateFrom = approvecompletedateFrom;
    }
	
	public java.util.Date getApprovecompletedateTo() {
    	return approvecompletedateTo;
    }
	
    public void setApprovecompletedateTo(java.util.Date approvecompletedateTo) {
    	this.approvecompletedateTo = approvecompletedateTo;
    }
	
	@JsonProperty("catPartnerId")
    public java.lang.Long getCatPartnerId(){
		return catPartnerId;
    }
	
    public void setCatPartnerId(java.lang.Long catPartnerId){
		this.catPartnerId = catPartnerId;
    }	
	
	@JsonProperty("catPartnerName")
    public java.lang.String getCatPartnerName(){
		return catPartnerName;
    }
	
    public void setCatPartnerName(java.lang.String catPartnerName){
		this.catPartnerName = catPartnerName;
    }	
	
	@JsonProperty("partnername")
    public java.lang.String getPartnername(){
		return partnername;
    }
	
    public void setPartnername(java.lang.String partnername){
		this.partnername = partnername;
    }	
	
	@JsonProperty("processDate")
    public java.lang.String getProcessDate(){
		return processDate;
    }
	
    public void setProcessDate(java.lang.String processDate){
		this.processDate = processDate;
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
}