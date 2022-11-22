/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.erp.dto;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.viettel.erp.bo.WorkItemsAcceptanceBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializer;
import com.viettel.erp.utils.JsonDateSerializerDate;
import com.viettel.ktts2.common.UDate;
import com.viettel.ktts2.common.UString;
import com.viettel.service.base.dto.BaseFWDTOImpl;


/**
 * @author hienlt56
 *
 */
@XmlRootElement(name = "WORK_ITEMS_ACCEPTANCEBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkItemsAcceptanceDTO extends BaseFWDTOImpl<WorkItemsAcceptanceBO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9194451638593899420L;
	private Long workItemsAcceptanceId;
	private String code;
	private Long monitorId;
	private Long inChargeConstructId;
	
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private java.util.Date acceptFromDate;
	
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private java.util.Date acceptToDate;
	
	private String acceptPlace;
	private String applyBenchmark;
	private String constructionQuality;
	private String otherDocuments;
	private String otherComments;
	private java.util.Date createdDate;
	private Long createdUserId;
	private java.util.Date approvalDate;
	private Long statusCa;
	private String documentCaId;
	
	//tatph - start 21112019
	private String workItemTypeName;
	private String itemsCode;
	private String workItemCode;
	
	public String getWorkItemCode() {
		return workItemCode;
	}

	public void setWorkItemCode(String workItemCode) {
		this.workItemCode = workItemCode;
	}

	public String getItemsCode() {
		return itemsCode;
	}

	public void setItemsCode(String itemsCode) {
		this.itemsCode = itemsCode;
	}

	public String getWorkItemTypeName() {
		return workItemTypeName;
	}

	public void setWorkItemTypeName(String workItemTypeName) {
		this.workItemTypeName = workItemTypeName;
	}

	//tatph - end 21112019
	public String getDocumentCaId() {
		return documentCaId;
	}

	public void setDocumentCaId(String documentCaId) {
		this.documentCaId = documentCaId;
	}
	private Long isActive;
	private Long conclusion;
	private String completeRequest;
	
	private String comments;
//	HieuNC 14/09/2017
//	B? sung tru?ng th�ng tin v?   don v? gi�m s�t (monitorGroupName),danh s�ch quy chu?n ti�u chu?n (applyStandardIds),
//	th?i gian ho�n th�nh (repairedFinishTime), c�ng vi?c d� d?m b?o (constructionQualityAssured), c�ng vi?c c?n kh?c ph?c(constructionQualityNeedFix)
//	nguy�n nh�n kh�ng nghi?m thu(notAcceptCause)
//	cho bi�n b?n nghi?m thu c�ng vi?c x�y d?ng
	private String constructionQualityNeedFix;
	public String getConstructionQualityNeedFix() {
		return constructionQualityNeedFix;
	}

	public void setConstructionQualityNeedFix(String constructionQualityNeedFix) {
		this.constructionQualityNeedFix = constructionQualityNeedFix;
	}

	public String getConstructionQualityAssured() {
		return constructionQualityAssured;
	}

	public void setConstructionQualityAssured(String constructionQualityAssured) {
		this.constructionQualityAssured = constructionQualityAssured;
	}

	public String getRepairedWorkItem() {
		return repairedWorkItem;
	}

	public void setRepairedWorkItem(String repairedWorkItem) {
		this.repairedWorkItem = repairedWorkItem;
	}

	public Date getRepairedFinishTime() {
		return repairedFinishTime;
	}

	public void setRepairedFinishTime(Date repairedFinishTime) {
		this.repairedFinishTime = repairedFinishTime;
	}

	public String getNotAcceptCause() {
		return notAcceptCause;
	}

	public void setNotAcceptCause(String notAcceptCause) {
		this.notAcceptCause = notAcceptCause;
	}

	public Long[] getApplyStandardIds() {
		return applyStandardIds;
	}

	public void setApplyStandardIds(Long[] applyStandardIds) {
		this.applyStandardIds = applyStandardIds;
	}

	public Long getWorkProgressStatus() {
		return workProgressStatus;
	}

	public void setWorkProgressStatus(Long workProgressStatus) {
		this.workProgressStatus = workProgressStatus;
	}

	public Long[] getWorkProgressFailReasonIds() {
		return workProgressFailReasonIds;
	}

	public void setWorkProgressFailReasonIds(Long[] workProgressFailReasonIds) {
		this.workProgressFailReasonIds = workProgressFailReasonIds;
	}

	public String getWpFailOtherReason() {
		return wpFailOtherReason;
	}

	public void setWpFailOtherReason(String wpFailOtherReason) {
		this.wpFailOtherReason = wpFailOtherReason;
	}
	private String monitorGroupName;
	private String constructionQualityAssured;
	private String repairedWorkItem;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date repairedFinishTime;
	private String notAcceptCause;
	private Long[] applyStandardIds;
	private Long workProgressStatus;
	private Long[] workProgressFailReasonIds;
	private String wpFailOtherReason;
	private String applyStandardIdsString;
	private String workProgressFailReasonIdstr;
//	support FreeMaker parse date to String in format "dd/Mm/yyyy"
	public String getRepairedFinishTimeString (){
		if (this.repairedFinishTime!=null){
			return UDate.toSimpleFormat(this.repairedFinishTime);
		}
		return "" ;
	}
	private List<String> applyStandardNames;
	private List<String>  workProgressFailReasonNames;
//	End
	
	
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private java.util.Date signDate;
	
	private String signDateDay = "";
	private String signDateMonth = "";
	private String signDateYear = "";
	
	private java.lang.String signPlace;
	// tungpv
	private String constrtCode;
	private String constrtName;
	private String constrtAddress;
	private String contractCode;
	private String contractName;
	private Long constructId;
	private String stationCode;
	private Long contractId;
	
	private List<EstimatesWorkItemsDTO> cvntList = Lists.newArrayList();
	
	private String acceptFromDateHour = "";
	private String acceptFromDateMinute = "";
	private String acceptFromDateDay = "";
	private String acceptFromDateMonth = "";
	private String acceptFromDateYear = "";
	
	private String acceptToDateHour = "";
	private String acceptToDateMinute = "";
	private String acceptToDateDay = "";
	private String acceptToDateMonth = "";
	private String acceptToDateYear = "";
	
	private String monitorIdName;
	private String inChargeConstructIdName;
	private String monitorIdNamePosition;
	private String inChargeConstructPosition;
	public String getInChargeConstructPosition() {
		return inChargeConstructPosition;
	}

	public void setInChargeConstructPosition(String inChargeConstructPosition) {
		this.inChargeConstructPosition = inChargeConstructPosition;
	}

	public String getMonitorIdNamePosition() {
		return monitorIdNamePosition;
	}

	public void setMonitorIdNamePosition(String monitorIdNamePosition) {
		this.monitorIdNamePosition = monitorIdNamePosition;
	}


	private java.lang.Long constrCompReMapId;
	private String monitorIdNamePath;
	private String inChargeConstructIdNamePath;
	
	private String monitorIdSign;
	private String inChargeConstructIdSign;
	
	private ConstrCompleteRecordsMapDTO constrCompleteRecordsMap;
	
	
	public String getMonitorIdSign() {
		return Strings.nullToEmpty(monitorIdSign);
	}

	public void setMonitorIdSign(String monitorIdSign) {
		this.monitorIdSign = monitorIdSign;
	}

	public String getInChargeConstructIdSign() {
		return Strings.nullToEmpty(inChargeConstructIdSign);
	}

	public void setInChargeConstructIdSign(String inChargeConstructIdSign) {
		this.inChargeConstructIdSign = inChargeConstructIdSign;
	}

	public Long getContractId() {
		return contractId;
	}

	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}

	public String getMonitorIdNamePath() {
		return monitorIdNamePath;
	}

	public void setMonitorIdNamePath(String monitorIdNamePath) {
		this.monitorIdNamePath = monitorIdNamePath;
	}

	public String getInChargeConstructIdNamePath() {
		return inChargeConstructIdNamePath;
	}

	public void setInChargeConstructIdNamePath(String inChargeConstructIdNamePath) {
		this.inChargeConstructIdNamePath = inChargeConstructIdNamePath;
	}

	public ConstrCompleteRecordsMapDTO getConstrCompleteRecordsMap() {
		return constrCompleteRecordsMap;
	}

	public void setConstrCompleteRecordsMap(ConstrCompleteRecordsMapDTO constrCompleteRecordsMap) {
		this.constrCompleteRecordsMap = constrCompleteRecordsMap;
	}

	public java.lang.Long getConstrCompReMapId() {
		return constrCompReMapId;
	}

	public void setConstrCompReMapId(java.lang.Long constrCompReMapId) {
		this.constrCompReMapId = constrCompReMapId;
	}

	public String getSignDateDay() {
		if (signDate != null) {
			signDateDay = DateFormatUtils.format(signDate, "dd");
		}
		return signDateDay;
	}

	public String getSignDateMonth() {
		if (signDate != null) {
			signDateMonth = DateFormatUtils.format(signDate, "MM");
		}
		return signDateMonth;
	}

	public String getSignDateYear() {
		if (signDate != null) {
			signDateYear = DateFormatUtils.format(signDate, "yyyy");
		}
		return signDateYear;
	}

	public String getConstrtAddress() {
		return Strings.nullToEmpty(constrtAddress);
	}

	public void setConstrtAddress(String constrtAddress) {
		this.constrtAddress = constrtAddress;
	}

	/**
	 * @return the monitorIdName
	 */
	public String getMonitorIdName() {
		return monitorIdName;
	}

	/**
	 * @param monitorIdName the monitorIdName to set
	 */
	public void setMonitorIdName(String monitorIdName) {
		this.monitorIdName = monitorIdName;
	}

	/**
	 * @return the inChargeConstructIdName
	 */
	public String getInChargeConstructIdName() {
		return inChargeConstructIdName;
	}

	/**
	 * @param inChargeConstructIdName the inChargeConstructIdName to set
	 */
	public void setInChargeConstructIdName(String inChargeConstructIdName) {
		this.inChargeConstructIdName = inChargeConstructIdName;
	}

	/**
	 * @return the acceptFromDateHour
	 */
	public String getAcceptFromDateHour() {
		if (acceptFromDate != null) {
			acceptFromDateHour = acceptFromDate.toString().substring(11, 13);
		}
		return acceptFromDateHour;
	}

	/**
	 * @return the acceptFromDateMinute
	 */
	public String getAcceptFromDateMinute() {
		if (acceptFromDate != null) {
			acceptFromDateMinute = DateFormatUtils.format(acceptFromDate, "mm");
		}
		return acceptFromDateMinute;
	}

	/**
	 * @return the acceptFromDateDay
	 */
	public String getAcceptFromDateDay() {
		if (acceptFromDate != null) {
			acceptFromDateDay = DateFormatUtils.format(acceptFromDate, "dd");
		}
		return acceptFromDateDay;
	}

	/**
	 * @return the acceptFromDateMonth
	 */
	public String getAcceptFromDateMonth() {
		if (acceptFromDate != null) {
			acceptFromDateMonth = DateFormatUtils.format(acceptFromDate, "MM");
		}
		return acceptFromDateMonth;
	}

	/**
	 * @return the acceptFromDateYear
	 */
	public String getAcceptFromDateYear() {
		if (acceptFromDate != null) {
			acceptFromDateYear = DateFormatUtils.format(acceptFromDate, "yyyy");
		}
		return acceptFromDateYear;
	}

	/**
	 * @return the acceptToDateHour
	 */
	public String getAcceptToDateHour() {
		if (acceptToDate != null) {
			acceptToDateHour = acceptToDate.toString().substring(11, 13);
		}
		return acceptToDateHour;
	}

	/**
	 * @return the acceptToDateMinute
	 */
	public String getAcceptToDateMinute() {
		if (acceptToDate != null) {
			acceptToDateMinute = DateFormatUtils.format(acceptToDate, "mm");
		}
		return acceptToDateMinute;
	}

	/**
	 * @return the acceptToDateDay
	 */
	public String getAcceptToDateDay() {
		if (acceptToDate != null) {
			acceptToDateDay = DateFormatUtils.format(acceptToDate, "dd");
		}
		return acceptToDateDay;
	}


	/**
	 * @return the acceptToDateMonth
	 */
	public String getAcceptToDateMonth() {
		if (acceptToDate != null) {
			acceptToDateMonth = DateFormatUtils.format(acceptToDate, "MM");
		}
		return acceptToDateMonth;
	}

	/**
	 * @return the acceptToDateYear
	 */
	public String getAcceptToDateYear() {
		if (acceptToDate != null) {
			acceptToDateYear = DateFormatUtils.format(acceptToDate, "yyyy");
		}
		return acceptToDateYear;
	}

	/**
	 * @return the cvntList
	 */
	public List<EstimatesWorkItemsDTO> getCvntList() {
		return cvntList;
	}

	/**
	 * @param cvntList the cvntList to set
	 */
	public void setCvntList(List<EstimatesWorkItemsDTO> cvntList) {
		this.cvntList = cvntList;
	}

	/**
	 * @return the stationCode
	 */
	public String getStationCode() {
		return Strings.nullToEmpty(stationCode);
	}

	/**
	 * @param stationCode the stationCode to set
	 */
	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	/**
	 * @return the constrtName
	 */
	public String getConstrtName() {
		return Strings.nullToEmpty(constrtName);
	}

	/**
	 * @param constrtName the constrtName to set
	 */
	public void setConstrtName(String constrtName) {
		this.constrtName = constrtName;
	}

	public Long getConstructId() {
		return constructId;
	}

	public void setConstructId(Long constructId) {
		this.constructId = constructId;
	}

	public String getConstrtCode() {
		return Strings.nullToEmpty(constrtCode);
	}

	public void setConstrtCode(String constrtCode) {
		this.constrtCode = constrtCode;
	}

	public String getContractCode() {
		return Strings.nullToEmpty(contractCode);
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getContractName() {
		return Strings.nullToEmpty(contractName);
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	@Override
	public WorkItemsAcceptanceBO toModel() {
		WorkItemsAcceptanceBO workItemsAcceptanceBO = new WorkItemsAcceptanceBO();
		
//		for (EstimatesWorkItemsDTO dto : cvntList) {
//			EstimatesWorkItemsBO bo = dto.toModel();
//			bo.getWorkItemsAcceptances().add(workItemsAcceptanceBO);
//			workItemsAcceptanceBO.getEstimatesWorkItems().add(bo);
//		}
		
		workItemsAcceptanceBO.setWorkItemsAcceptanceId(this.workItemsAcceptanceId);
		workItemsAcceptanceBO.setCode(this.code);
		workItemsAcceptanceBO.setMonitorId(this.monitorId);
		workItemsAcceptanceBO.setInChargeConstructId(this.inChargeConstructId);
		workItemsAcceptanceBO.setAcceptFromDate(this.acceptFromDate);
		workItemsAcceptanceBO.setAcceptToDate(this.acceptToDate);
		workItemsAcceptanceBO.setAcceptPlace(this.acceptPlace);
		workItemsAcceptanceBO.setApplyBenchmark(this.applyBenchmark);
		workItemsAcceptanceBO.setConstructionQuality(this.constructionQuality);
		workItemsAcceptanceBO.setOtherDocuments(this.otherDocuments);
		workItemsAcceptanceBO.setOtherComments(this.otherComments);
		workItemsAcceptanceBO.setCreatedDate(this.createdDate);
		workItemsAcceptanceBO.setCreatedUserId(this.createdUserId);
		workItemsAcceptanceBO.setApprovalDate(this.approvalDate);
		workItemsAcceptanceBO.setStatusCa(this.statusCa);
		workItemsAcceptanceBO.setIsActive(this.isActive);
		workItemsAcceptanceBO.setConclusion(this.conclusion);
		workItemsAcceptanceBO.setCompleteRequest(this.completeRequest);
		workItemsAcceptanceBO.setConstructId(this.constructId);
		workItemsAcceptanceBO.setSignDate(this.signDate);
		workItemsAcceptanceBO.setSignPlace(this.signPlace);
//	HieuNC 15/09/2017
//		additional new field
		workItemsAcceptanceBO.setApplyStandardIds(UString.parseLongArrayToString(this.applyStandardIds));
		workItemsAcceptanceBO.setConstructionQualityNeedFix(this.constructionQualityNeedFix);
		workItemsAcceptanceBO.setConstructionQualityAssured(this.constructionQualityAssured);
		workItemsAcceptanceBO.setRepairedWorkItem(this.repairedWorkItem);
		workItemsAcceptanceBO.setRepairedFinishTime(this.repairedFinishTime);
		workItemsAcceptanceBO.setNotAcceptCause(this.notAcceptCause);
		workItemsAcceptanceBO.setWorkProgressStatus(this.workProgressStatus);
		workItemsAcceptanceBO.setWorkProgressFailReasonIds(UString.parseLongArrayToString(this.workProgressFailReasonIds));
		workItemsAcceptanceBO.setWpFailOtherReason(this.wpFailOtherReason);
		
//	End
		workItemsAcceptanceBO.setAmonitorName(this.amonitorName);
		return workItemsAcceptanceBO;
	}

	@Override
	public Long getFWModelId() {
		return workItemsAcceptanceId;
	}

	@Override
	public String catchName() {
		return getWorkItemsAcceptanceId().toString();
	}

	public Long getWorkItemsAcceptanceId() {
		return workItemsAcceptanceId;
	}

	public void setWorkItemsAcceptanceId(Long workItemsAcceptanceId) {
		this.workItemsAcceptanceId = workItemsAcceptanceId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the monitorId
	 */
	public Long getMonitorId() {
		return monitorId;
	}

	/**
	 * @param monitorId the monitorId to set
	 */
	public void setMonitorId(Long monitorId) {
		this.monitorId = monitorId;
	}

	/**
	 * @return the inChargeConstructId
	 */
	public Long getInChargeConstructId() {
		return inChargeConstructId;
	}

	/**
	 * @param inChargeConstructId the inChargeConstructId to set
	 */
	public void setInChargeConstructId(Long inChargeConstructId) {
		this.inChargeConstructId = inChargeConstructId;
	}

	public java.util.Date getAcceptFromDate() {
		return acceptFromDate;
	}

	public void setAcceptFromDate(java.util.Date acceptFromDate) {
		this.acceptFromDate = acceptFromDate;
	}

	public java.util.Date getAcceptToDate() {
		return acceptToDate;
	}

	public void setAcceptToDate(java.util.Date acceptToDate) {
		this.acceptToDate = acceptToDate;
	}

	public String getAcceptPlace() {
		return Strings.nullToEmpty(acceptPlace);
	}

	public void setAcceptPlace(String acceptPlace) {
		this.acceptPlace = acceptPlace;
	}

	public String getApplyBenchmark() {
		return applyBenchmark;
	}

	public void setApplyBenchmark(String applyBenchmark) {
		this.applyBenchmark = applyBenchmark;
	}

	public String getConstructionQuality() {
		return constructionQuality;
	}

	public void setConstructionQuality(String constructionQuality) {
		this.constructionQuality = constructionQuality;
	}

	public String getOtherDocuments() {
		return Strings.nullToEmpty(otherDocuments);
	}

	public void setOtherDocuments(String otherDocuments) {
		this.otherDocuments = otherDocuments;
	}

	public String getOtherComments() {
		return Strings.nullToEmpty(otherComments);
	}

	public void setOtherComments(String otherComments) {
		this.otherComments = otherComments;
	}

	public java.util.Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(java.util.Date createdDate) {
		this.createdDate = createdDate;
	}

	public Long getCreatedUserId() {
		return createdUserId;
	}

	public void setCreatedUserId(Long createdUserId) {
		this.createdUserId = createdUserId;
	}

	public java.util.Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(java.util.Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public Long getStatusCa() {
		return statusCa;
	}

	public void setStatusCa(Long statusCa) {
		this.statusCa = statusCa;
	}

	public Long getIsActive() {
		return isActive;
	}

	public void setIsActive(Long isActive) {
		this.isActive = isActive;
	}

	public Long getConclusion() {
		return conclusion;
	}

	public void setConclusion(Long conclusion) {
		this.conclusion = conclusion;
	}

	public String getCompleteRequest() {
		return Strings.nullToEmpty(completeRequest);
	}

	public void setCompleteRequest(String completeRequest) {
		this.completeRequest = completeRequest;
	}

	public java.util.Date getSignDate(){
    return signDate;
    }
    public void setSignDate(java.util.Date signDate)
    {
    this.signDate = signDate;
    }
    
    public java.lang.String getSignPlace(){
    return signPlace;
    }
    
    public void setSignPlace(java.lang.String signPlace)
    {
    this.signPlace = signPlace;
    }

	public String getMonitorGroupName() {
		return monitorGroupName;
	}

	public void setMonitorGroupName(String monitorGroupName) {
		this.monitorGroupName = monitorGroupName;
	}

	public String getApplyStandardIdsString() {
		return applyStandardIdsString;
	}

	public void setApplyStandardIdsString(String applyStandardIdsString) {
		this.applyStandardIdsString = applyStandardIdsString;
	}

	public String getWorkProgressFailReasonIdstr() {
		return workProgressFailReasonIdstr;
	}

	public void setWorkProgressFailReasonIdstr(String workProgressFailReasonIdstr) {
		this.workProgressFailReasonIdstr = workProgressFailReasonIdstr;
	}

	public List<String> getApplyStandardNames() {
		return applyStandardNames;
	}

	public void setApplyStandardNames(List<String> applyStandardNames) {
		this.applyStandardNames = applyStandardNames;
	}

	public List<String> getWorkProgressFailReasonNames() {
		return workProgressFailReasonNames;
	}

	public void setWorkProgressFailReasonNames(List<String> workProgressFailReasonNames) {
		this.workProgressFailReasonNames = workProgressFailReasonNames;
	}
	
	//Huypq-20190710-start
	private Integer page;
	private Integer pageSize;
	private Integer totalRecord;
	private String sysGroupName;
	private String provinceCode;
	private String constructionCode;
	private String constructionTypeName;
	private String workItemName;
	private String ngay_thuchien_HSHC;
	private String ngay_hoanthanh_HSHC;
	private String nguoilap_HSHC;
	private String tinhtrang_HSHC;
	private String ngay_QT;
	private String giatri_QT;
	private String nguoilap_QT;
	private String ps_ngoaiHD;
	private Long sysGroupId;
	private Long catProvinceId;
	private String keySearch;

	public String getKeySearch() {
		return keySearch;
	}

	public void setKeySearch(String keySearch) {
		this.keySearch = keySearch;
	}

	public Long getSysGroupId() {
		return sysGroupId;
	}

	public void setSysGroupId(Long sysGroupId) {
		this.sysGroupId = sysGroupId;
	}

	public Long getCatProvinceId() {
		return catProvinceId;
	}

	public void setCatProvinceId(Long catProvinceId) {
		this.catProvinceId = catProvinceId;
	}

	public String getSysGroupName() {
		return sysGroupName;
	}

	public void setSysGroupName(String sysGroupName) {
		this.sysGroupName = sysGroupName;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getConstructionCode() {
		return constructionCode;
	}

	public void setConstructionCode(String constructionCode) {
		this.constructionCode = constructionCode;
	}

	public String getConstructionTypeName() {
		return constructionTypeName;
	}

	public void setConstructionTypeName(String constructionTypeName) {
		this.constructionTypeName = constructionTypeName;
	}

	public String getWorkItemName() {
		return workItemName;
	}

	public void setWorkItemName(String workItemName) {
		this.workItemName = workItemName;
	}

	public String getNgay_thuchien_HSHC() {
		return ngay_thuchien_HSHC;
	}

	public void setNgay_thuchien_HSHC(String ngay_thuchien_HSHC) {
		this.ngay_thuchien_HSHC = ngay_thuchien_HSHC;
	}

	public String getNgay_hoanthanh_HSHC() {
		return ngay_hoanthanh_HSHC;
	}

	public void setNgay_hoanthanh_HSHC(String ngay_hoanthanh_HSHC) {
		this.ngay_hoanthanh_HSHC = ngay_hoanthanh_HSHC;
	}

	public String getNguoilap_HSHC() {
		return nguoilap_HSHC;
	}

	public void setNguoilap_HSHC(String nguoilap_HSHC) {
		this.nguoilap_HSHC = nguoilap_HSHC;
	}

	public String getTinhtrang_HSHC() {
		return tinhtrang_HSHC;
	}

	public void setTinhtrang_HSHC(String tinhtrang_HSHC) {
		this.tinhtrang_HSHC = tinhtrang_HSHC;
	}

	public String getNgay_QT() {
		return ngay_QT;
	}

	public void setNgay_QT(String ngay_QT) {
		this.ngay_QT = ngay_QT;
	}

	public String getGiatri_QT() {
		return giatri_QT;
	}

	public void setGiatri_QT(String giatri_QT) {
		this.giatri_QT = giatri_QT;
	}

	public String getNguoilap_QT() {
		return nguoilap_QT;
	}

	public void setNguoilap_QT(String nguoilap_QT) {
		this.nguoilap_QT = nguoilap_QT;
	}

	public String getPs_ngoaiHD() {
		return ps_ngoaiHD;
	}

	public void setPs_ngoaiHD(String ps_ngoaiHD) {
		this.ps_ngoaiHD = ps_ngoaiHD;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(Integer totalRecord) {
		this.totalRecord = totalRecord;
	}
	
	//Huy-end
	
	//Huypq-20190801-start
	private String amonitorName;
	public String getAmonitorName() {
		return amonitorName;
	}

	public void setAmonitorName(String amonitorName) {
		this.amonitorName = amonitorName;
	}
	
	
	//Huy-end
	//HienLT56 start 20062020
	private String areaCode;
	private Long sumTypeConstruction;
	private Long btsTypeConstruction;
	private Long cbTypeConstruction;
	private Long hdkTypeConstruction;
	private Long sumProceedStatus;
	private Long ctcProceedStatus;
	private Long dtcProceedStatus;
	private Long htProceedStatus;
	private Long cancelProceedStatus;
	private Long sumCompleteStatus;
	private Long cthCompleteStatus;
	private Long dthCompleteStatus;
	private Long htCompleteStatus;
	private Long sumSettlementProposal;
	private Long cthSettlementProposal;
	private Long dthSettlementProposal;
	private Double gtSettlementProposal;
	private String note;

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public Long getSumTypeConstruction() {
		return sumTypeConstruction;
	}

	public void setSumTypeConstruction(Long sumTypeConstruction) {
		this.sumTypeConstruction = sumTypeConstruction;
	}

	public Long getBtsTypeConstruction() {
		return btsTypeConstruction;
	}

	public void setBtsTypeConstruction(Long btsTypeConstruction) {
		this.btsTypeConstruction = btsTypeConstruction;
	}

	public Long getCbTypeConstruction() {
		return cbTypeConstruction;
	}

	public void setCbTypeConstruction(Long cbTypeConstruction) {
		this.cbTypeConstruction = cbTypeConstruction;
	}

	public Long getHdkTypeConstruction() {
		return hdkTypeConstruction;
	}

	public void setHdkTypeConstruction(Long hdkTypeConstruction) {
		this.hdkTypeConstruction = hdkTypeConstruction;
	}

	public Long getSumProceedStatus() {
		return sumProceedStatus;
	}

	public void setSumProceedStatus(Long sumProceedStatus) {
		this.sumProceedStatus = sumProceedStatus;
	}

	public Long getCtcProceedStatus() {
		return ctcProceedStatus;
	}

	public void setCtcProceedStatus(Long ctcProceedStatus) {
		this.ctcProceedStatus = ctcProceedStatus;
	}

	public Long getDtcProceedStatus() {
		return dtcProceedStatus;
	}

	public void setDtcProceedStatus(Long dtcProceedStatus) {
		this.dtcProceedStatus = dtcProceedStatus;
	}

	public Long getHtProceedStatus() {
		return htProceedStatus;
	}

	public void setHtProceedStatus(Long htProceedStatus) {
		this.htProceedStatus = htProceedStatus;
	}

	public Long getCancelProceedStatus() {
		return cancelProceedStatus;
	}

	public void setCancelProceedStatus(Long cancelProceedStatus) {
		this.cancelProceedStatus = cancelProceedStatus;
	}

	public Long getSumCompleteStatus() {
		return sumCompleteStatus;
	}

	public void setSumCompleteStatus(Long sumCompleteStatus) {
		this.sumCompleteStatus = sumCompleteStatus;
	}

	public Long getCthCompleteStatus() {
		return cthCompleteStatus;
	}

	public void setCthCompleteStatus(Long cthCompleteStatus) {
		this.cthCompleteStatus = cthCompleteStatus;
	}

	public Long getDthCompleteStatus() {
		return dthCompleteStatus;
	}

	public void setDthCompleteStatus(Long dthCompleteStatus) {
		this.dthCompleteStatus = dthCompleteStatus;
	}

	public Long getHtCompleteStatus() {
		return htCompleteStatus;
	}

	public void setHtCompleteStatus(Long htCompleteStatus) {
		this.htCompleteStatus = htCompleteStatus;
	}

	public Long getSumSettlementProposal() {
		return sumSettlementProposal;
	}

	public void setSumSettlementProposal(Long sumSettlementProposal) {
		this.sumSettlementProposal = sumSettlementProposal;
	}

	public Long getCthSettlementProposal() {
		return cthSettlementProposal;
	}

	public void setCthSettlementProposal(Long cthSettlementProposal) {
		this.cthSettlementProposal = cthSettlementProposal;
	}

	public Long getDthSettlementProposal() {
		return dthSettlementProposal;
	}

	public void setDthSettlementProposal(Long dthSettlementProposal) {
		this.dthSettlementProposal = dthSettlementProposal;
	}

	public Double getGtSettlementProposal() {
		return gtSettlementProposal;
	}

	public void setGtSettlementProposal(Double gtSettlementProposal) {
		this.gtSettlementProposal = gtSettlementProposal;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	//HienLT56 end 20062020
	//HienLT56 start 23062020
	private String stationHouseCode;
	private String constrTypeName;
	private String haveNotExecuted;
	private String executing;
	private String completed;
	private String cancelExecute;
	private String assignSupervisionDutyCD;
	private String constrGroundHandoverCD;
	private String constrWorkLogsCD;
	private String constrWorkLogsLabelCD;
	private String completionDrawingCD;
	private String bMaterialAcceptanceCD;
	private String aMaterialHandoverCD;
	private String distanceUnloadConstrMinutesCD;
	private String workItemsAcceptanceCD;
	private String sceneGenerateWorkCDPS;
	private String sceneGenerateWorkCD;
	private String qualityCableMeaReportCD;
	private String aMaterialRecoveryMinutesCD;
	private String categoryAcceptanceCD;
	private String constrAcceptanceRequestCD;
	private String constructionAcceptanceCD;
	private String constructionADDcceptanceCD;
	private String constrWorkCompConfirmCD;
	private String haveNotExcutedSettlementProposal;
	private String excutedSettlementProposal;
	private Double valueSettlementProposal;
	private Long executedReport;
	private String statusHCQT;
	private Long statusExecute;
	
	
	public Long getStatusExecute() {
		return statusExecute;
	}
	public void setStatusExecute(Long statusExecute) {
		this.statusExecute = statusExecute;
	}
	public String getStationHouseCode() {
		return stationHouseCode;
	}
	public void setStationHouseCode(String stationHouseCode) {
		this.stationHouseCode = stationHouseCode;
	}
	public String getConstrTypeName() {
		return constrTypeName;
	}
	public void setConstrTypeName(String constrTypeName) {
		this.constrTypeName = constrTypeName;
	}
	public String getHaveNotExecuted() {
		return haveNotExecuted;
	}
	public void setHaveNotExecuted(String haveNotExecuted) {
		this.haveNotExecuted = haveNotExecuted;
	}
	public String getExecuting() {
		return executing;
	}
	public void setExecuting(String executing) {
		this.executing = executing;
	}
	public String getCompleted() {
		return completed;
	}
	public void setCompleted(String completed) {
		this.completed = completed;
	}
	public String getCancelExecute() {
		return cancelExecute;
	}
	public void setCancelExecute(String cancelExecute) {
		this.cancelExecute = cancelExecute;
	}
	public String getAssignSupervisionDutyCD() {
		return assignSupervisionDutyCD;
	}
	public void setAssignSupervisionDutyCD(String assignSupervisionDutyCD) {
		this.assignSupervisionDutyCD = assignSupervisionDutyCD;
	}
	public String getConstrGroundHandoverCD() {
		return constrGroundHandoverCD;
	}
	public void setConstrGroundHandoverCD(String constrGroundHandoverCD) {
		this.constrGroundHandoverCD = constrGroundHandoverCD;
	}
	public String getConstrWorkLogsCD() {
		return constrWorkLogsCD;
	}
	public void setConstrWorkLogsCD(String constrWorkLogsCD) {
		this.constrWorkLogsCD = constrWorkLogsCD;
	}
	public String getConstrWorkLogsLabelCD() {
		return constrWorkLogsLabelCD;
	}
	public void setConstrWorkLogsLabelCD(String constrWorkLogsLabelCD) {
		this.constrWorkLogsLabelCD = constrWorkLogsLabelCD;
	}
	public String getCompletionDrawingCD() {
		return completionDrawingCD;
	}
	public void setCompletionDrawingCD(String completionDrawingCD) {
		this.completionDrawingCD = completionDrawingCD;
	}
	public String getbMaterialAcceptanceCD() {
		return bMaterialAcceptanceCD;
	}
	public void setbMaterialAcceptanceCD(String bMaterialAcceptanceCD) {
		this.bMaterialAcceptanceCD = bMaterialAcceptanceCD;
	}
	public String getaMaterialHandoverCD() {
		return aMaterialHandoverCD;
	}
	public void setaMaterialHandoverCD(String aMaterialHandoverCD) {
		this.aMaterialHandoverCD = aMaterialHandoverCD;
	}
	public String getDistanceUnloadConstrMinutesCD() {
		return distanceUnloadConstrMinutesCD;
	}
	public void setDistanceUnloadConstrMinutesCD(String distanceUnloadConstrMinutesCD) {
		this.distanceUnloadConstrMinutesCD = distanceUnloadConstrMinutesCD;
	}
	public String getWorkItemsAcceptanceCD() {
		return workItemsAcceptanceCD;
	}
	public void setWorkItemsAcceptanceCD(String workItemsAcceptanceCD) {
		this.workItemsAcceptanceCD = workItemsAcceptanceCD;
	}
	public String getSceneGenerateWorkCDPS() {
		return sceneGenerateWorkCDPS;
	}
	public void setSceneGenerateWorkCDPS(String sceneGenerateWorkCDPS) {
		this.sceneGenerateWorkCDPS = sceneGenerateWorkCDPS;
	}
	public String getSceneGenerateWorkCD() {
		return sceneGenerateWorkCD;
	}
	public void setSceneGenerateWorkCD(String sceneGenerateWorkCD) {
		this.sceneGenerateWorkCD = sceneGenerateWorkCD;
	}
	public String getQualityCableMeaReportCD() {
		return qualityCableMeaReportCD;
	}
	public void setQualityCableMeaReportCD(String qualityCableMeaReportCD) {
		this.qualityCableMeaReportCD = qualityCableMeaReportCD;
	}
	public String getaMaterialRecoveryMinutesCD() {
		return aMaterialRecoveryMinutesCD;
	}
	public void setaMaterialRecoveryMinutesCD(String aMaterialRecoveryMinutesCD) {
		this.aMaterialRecoveryMinutesCD = aMaterialRecoveryMinutesCD;
	}
	public String getCategoryAcceptanceCD() {
		return categoryAcceptanceCD;
	}
	public void setCategoryAcceptanceCD(String categoryAcceptanceCD) {
		this.categoryAcceptanceCD = categoryAcceptanceCD;
	}
	public String getConstrAcceptanceRequestCD() {
		return constrAcceptanceRequestCD;
	}
	public void setConstrAcceptanceRequestCD(String constrAcceptanceRequestCD) {
		this.constrAcceptanceRequestCD = constrAcceptanceRequestCD;
	}
	public String getConstructionAcceptanceCD() {
		return constructionAcceptanceCD;
	}
	public void setConstructionAcceptanceCD(String constructionAcceptanceCD) {
		this.constructionAcceptanceCD = constructionAcceptanceCD;
	}
	public String getConstructionADDcceptanceCD() {
		return constructionADDcceptanceCD;
	}
	public void setConstructionADDcceptanceCD(String constructionADDcceptanceCD) {
		this.constructionADDcceptanceCD = constructionADDcceptanceCD;
	}
	public String getConstrWorkCompConfirmCD() {
		return constrWorkCompConfirmCD;
	}
	public void setConstrWorkCompConfirmCD(String constrWorkCompConfirmCD) {
		this.constrWorkCompConfirmCD = constrWorkCompConfirmCD;
	}
	public String getHaveNotExcutedSettlementProposal() {
		return haveNotExcutedSettlementProposal;
	}
	public void setHaveNotExcutedSettlementProposal(String haveNotExcutedSettlementProposal) {
		this.haveNotExcutedSettlementProposal = haveNotExcutedSettlementProposal;
	}
	public String getExcutedSettlementProposal() {
		return excutedSettlementProposal;
	}
	public void setExcutedSettlementProposal(String excutedSettlementProposal) {
		this.excutedSettlementProposal = excutedSettlementProposal;
	}
	public Double getValueSettlementProposal() {
		return valueSettlementProposal;
	}
	public void setValueSettlementProposal(Double valueSettlementProposal) {
		this.valueSettlementProposal = valueSettlementProposal;
	}
	public Long getExecutedReport() {
		return executedReport;
	}
	public void setExecutedReport(Long executedReport) {
		this.executedReport = executedReport;
	}
	public String getStatusHCQT() {
		return statusHCQT;
	}
	public void setStatusHCQT(String statusHCQT) {
		this.statusHCQT = statusHCQT;
	}
	
	//HienLT56 end 23062020
	
	//Huypq-16072020-start
	private List<WorkItemsAcceptanceDTO> lstWorkItemAcceptance;
	private List<Long> lstWorkItemAcceptanceId;

	public List<Long> getLstWorkItemAcceptanceId() {
		return lstWorkItemAcceptanceId;
	}

	public void setLstWorkItemAcceptanceId(List<Long> lstWorkItemAcceptanceId) {
		this.lstWorkItemAcceptanceId = lstWorkItemAcceptanceId;
	}

	public List<WorkItemsAcceptanceDTO> getLstWorkItemAcceptance() {
		return lstWorkItemAcceptance;
	}

	public void setLstWorkItemAcceptance(List<WorkItemsAcceptanceDTO> lstWorkItemAcceptance) {
		this.lstWorkItemAcceptance = lstWorkItemAcceptance;
	}
	
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date logDate;
	public Date getLogDate() {
		return logDate;
	}

	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}
	
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date acceptanceDate;

	public Date getAcceptanceDate() {
		return acceptanceDate;
	}

	public void setAcceptanceDate(Date acceptanceDate) {
		this.acceptanceDate = acceptanceDate;
	}

	
	
	//Huy-end
}
