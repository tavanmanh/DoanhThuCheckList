/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.erp.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.google.common.base.Strings;
import com.viettel.erp.bo.VConstructionHcqtBO;
import com.viettel.erp.utils.CustomJsonDateDeserializer;
import com.viettel.erp.utils.CustomJsonDateSerializer;
import com.viettel.service.base.dto.BaseFWDTOImpl;

/**
 *
 * @author thuannht
 */
@XmlRootElement(name = "V_CONSTRUCTION_HCQTBO")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_EMPTY)
public class VConstructionHcqtDTO extends BaseFWDTOImpl<VConstructionHcqtBO> {

	private java.lang.Double catProjectId;
	private java.lang.String catApProjectName;
	private java.lang.Long constructId;
	private java.lang.String constrtCode;
	private java.lang.Double constrType;
	private java.lang.String constrTypeName;
	private java.lang.String constrTypeCode;
	private java.lang.String constrtName;
	private java.lang.String constrtAddress;
	private java.lang.Double constructorId;
	private java.lang.Double supervisorId;
	private java.util.Date startingDate;
	private java.util.Date completeDate;
	private java.lang.Double sysUserId;
	private java.lang.Double catApProjectId;
	private java.lang.String sysUserName;
	private java.lang.Double sysGroupId;
	private java.lang.String sysGroupName;
	private java.util.Date sysCreatedDate;
	private java.lang.Double status;
	private java.util.Date expectedCompleteDate;
	private java.lang.String startComment;
	private java.lang.Double isActive;
	private java.lang.Double provinceId;
	private java.lang.Double districtId;
	private java.lang.Double investProjectId;
	private java.lang.String investProjectName;
	private java.lang.String investProjectCode;
	private java.lang.Double stationId;
	private java.util.Date serviceProvideDate;
	private java.lang.String districtCode;
	private java.lang.String districtName;
	private java.util.Date handoverDate;
	private java.lang.String stationCode;
	private java.lang.String stationAddress;
	private java.lang.String provinceCode;
	private java.lang.String constructorName;
	private java.lang.String supervisorName;
	private java.lang.String provinceName;
	private java.lang.Double superForm;
	private java.lang.Double constrtForm;
	private java.lang.Double isObstructed;
	private java.lang.Double statusId;
	private java.lang.String statusName;
	private java.lang.String statusCode;
	private java.lang.Double isAccepted;
	private java.util.Date preStartingDate;
	private java.util.Date acceptStartDate;
	private java.util.Date acceptFinishDate;
	private java.lang.Double lineMode;
	private java.lang.Double projectType;
	private java.lang.String constructorPerson;
	private java.lang.String constructorPhone;
	private java.lang.String supervisorPerson;
	private java.lang.String supervisorPhone;
	private java.lang.Double cableTypeId;
	private java.lang.Double lineLength;
	private java.lang.Double lineType;
	private java.lang.String executer;
	private java.lang.Double executeGroup;
	private java.lang.String executePhone;
	private java.lang.Double finishTest;
	private java.lang.Double percentFinish;
	private java.lang.Double preparedPremises;
	private java.lang.Double handoverMaterials;
	private java.lang.Double progressCommitment;
	private java.lang.String contractCode;
	private java.lang.Double contractId;
	private java.lang.String contractName;

	private java.lang.String aMonitorPath;
	private java.lang.String bConstructPath;
	private java.lang.String aDirectorPath;
	private java.lang.String bDirectorPath;

	private java.lang.Long aMonitorId;
	private java.lang.Long bConstructId;
	private java.lang.Long aDirectorId;
	private java.lang.Long bDirectorId;
	private java.lang.Long statusCa;
	private java.lang.Long documentCaId;

	private java.lang.Long isEstimated;

	private Long detailSettlementProposalId;
	private String proposalCode;
	private Long proposalStatusCa;
	private Long detailSettlementEvaluateId;
	private String evaluateCode;
	private Long evaluateStatusCa;
	private Long evaluateStatus;
	private Long checkHtct;

	/*-----------------ITSOL start-----------------*/

	private String code;

	public Long getCheckHtct() {
		return checkHtct;
	}

	public void setCheckHtct(Long checkHtct) {
		this.checkHtct = checkHtct;
	}

	private String stationName;
	private String statusNameContract;
	private Long totalConstr;
	private String totalConstrDone;
	private String totalConstrNonStart;
	private String totalConstrWorking;
	private String totalConstrBreak;
	private Long countConstr;
	private Long countConstrDone;
	private Long countConstrNonStart;
	private Long countConstrWorking;
	private Long countConstrBreak;
	private String totalValue;
	private String startingDate1;
	private String completeDate1;
	private String acceptFinishDate1;
	private Long duration;
	private Long thoigianconlai;

	// Huynv start 2019-08-05
	private Long statusApprovedLabor;
	private Long isUpdatedLabor;
	// Huynv end

	public String getStartingDate1() {
		return startingDate1;
	}

	public void setStartingDate1(String startingDate1) {
		this.startingDate1 = startingDate1;
	}

	public String getCompleteDate1() {
		return completeDate1;
	}

	public void setCompleteDate1(String completeDate1) {
		this.completeDate1 = completeDate1;
	}

	public String getAcceptFinishDate1() {
		return acceptFinishDate1;
	}

	public void setAcceptFinishDate1(String acceptFinishDate1) {
		this.acceptFinishDate1 = acceptFinishDate1;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public Long getThoigianconlai() {
		return thoigianconlai;
	}

	public void setThoigianconlai(Long thoigianconlai) {
		this.thoigianconlai = thoigianconlai;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	private String areaName;
	private String settlementStatus;
	private Long sttProvinceGroupId;
	private String romanCount;
	/*-----------------ITSOL end-----------------*/

	public Date getAcceptanceDate() {
		return acceptanceDate;
	}

	public void setAcceptanceDate(Date acceptanceDate) {
		this.acceptanceDate = acceptanceDate;
	}

	public Double getTotalACap() {
		return totalACap;
	}

	public void setTotalACap(Double totalACap) {
		this.totalACap = totalACap;
	}

	public Long getDetailSettlementProposalId() {
		return detailSettlementProposalId;
	}

	public void setDetailSettlementProposalId(Long detailSettlementProposalId) {
		this.detailSettlementProposalId = detailSettlementProposalId;
	}

	public String getProposalCode() {
		return proposalCode;
	}

	public void setProposalCode(String proposalCode) {
		this.proposalCode = proposalCode;
	}

	public Long getProposalStatusCa() {
		return proposalStatusCa;
	}

	public void setProposalStatusCa(Long proposalStatusCa) {
		this.proposalStatusCa = proposalStatusCa;
	}

	public Long getDetailSettlementEvaluateId() {
		return detailSettlementEvaluateId;
	}

	public void setDetailSettlementEvaluateId(Long detailSettlementEvaluateId) {
		this.detailSettlementEvaluateId = detailSettlementEvaluateId;
	}

	public String getEvaluateCode() {
		return evaluateCode;
	}

	public void setEvaluateCode(String evaluateCode) {
		this.evaluateCode = evaluateCode;
	}

	public Long getEvaluateStatusCa() {
		return evaluateStatusCa;
	}

	public void setEvaluateStatusCa(Long evaluateStatusCa) {
		this.evaluateStatusCa = evaluateStatusCa;
	}

	public Long getEvaluateStatus() {
		return evaluateStatus;
	}

	public void setEvaluateStatus(Long evaluateStatus) {
		this.evaluateStatus = evaluateStatus;
	}

	public java.lang.Long getIsEstimated() {
		return isEstimated;
	}

	public void setIsEstimated(java.lang.Long isEstimated) {
		this.isEstimated = isEstimated;
	}

	public java.lang.Long getaMonitorId() {
		return aMonitorId;
	}

	public void setaMonitorId(java.lang.Long aMonitorId) {
		this.aMonitorId = aMonitorId;
	}

	public java.lang.Long getbConstructId() {
		return bConstructId;
	}

	public void setbConstructId(java.lang.Long bConstructId) {
		this.bConstructId = bConstructId;
	}

	public java.lang.Long getaDirectorId() {
		return aDirectorId;
	}

	public void setaDirectorId(java.lang.Long aDirectorId) {
		this.aDirectorId = aDirectorId;
	}

	public java.lang.Long getbDirectorId() {
		return bDirectorId;
	}

	public void setbDirectorId(java.lang.Long bDirectorId) {
		this.bDirectorId = bDirectorId;
	}

	public java.lang.Long getStatusCa() {
		return statusCa;
	}

	public void setStatusCa(java.lang.Long statusCa) {
		this.statusCa = statusCa;
	}

	public java.lang.Long getDocumentCaId() {
		return documentCaId;
	}

	public void setDocumentCaId(java.lang.Long documentCaId) {
		this.documentCaId = documentCaId;
	}

	private Long CaStatus;

	private String aDirectorName;
	private String aMonitorName;
	private String bConstructName;
	private String bDirectorName;

	private String aDirectorNameSign;
	private String aMonitorNameSign;
	private String bConstructNameSign;
	private String bDirectorNameSign;

	// HieuNC 09/09/2017
	// Expand 4 field completeRecordNum (s??? bi??n b???n ???? l???p) , signedRecordNum
	// (s??? bi??n b???n ???? k??/duy???t)
	// khu v???c (provinceGroupId) v?? t??nh tr???ng quy???t to??n
	// cho ch???c n??ng Qu???n l?? b??? HSHC
	private Long provinceGroupId;
	private Long settlementStatusId;
	private Long completeRecordNum;
	private Long signedRecordNum;
	private Integer totalNum;
	private String description;
	// End

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	// HieuNC 04/10/2017
	private String investGroupName;
	private String monitorGroupName;
	private String consultGroupName;

	// End
	public String getaDirectorNameSign() {
		return aDirectorNameSign;
	}

	public void setaDirectorNameSign(String aDirectorNameSign) {
		this.aDirectorNameSign = aDirectorNameSign;
	}

	public String getaMonitorNameSign() {
		return aMonitorNameSign;
	}

	public void setaMonitorNameSign(String aMonitorNameSign) {
		this.aMonitorNameSign = aMonitorNameSign;
	}

	public String getbConstructNameSign() {
		return bConstructNameSign;
	}

	public void setbConstructNameSign(String bConstructNameSign) {
		this.bConstructNameSign = bConstructNameSign;
	}

	public String getbDirectorNameSign() {
		return bDirectorNameSign;
	}

	public void setbDirectorNameSign(String bDirectorNameSign) {
		this.bDirectorNameSign = bDirectorNameSign;
	}

	public String getaDirectorName() {
		return aDirectorName;
	}

	public void setaDirectorName(String aDirectorName) {
		this.aDirectorName = aDirectorName;
	}

	public String getaMonitorName() {
		return aMonitorName;
	}

	public void setaMonitorName(String aMonitorName) {
		this.aMonitorName = aMonitorName;
	}

	public String getbConstructName() {
		return bConstructName;
	}

	public void setbConstructName(String bConstructName) {
		this.bConstructName = bConstructName;
	}

	public String getbDirectorName() {
		return bDirectorName;
	}

	public void setbDirectorName(String bDirectorName) {
		this.bDirectorName = bDirectorName;
	}

	@JsonSerialize(using = CustomJsonDateSerializer.class)
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	private java.util.Date signed_date;

	private java.lang.Double contract_total_value;
	private java.lang.Double isAcceptedContract;
	private java.lang.Double isOff;

	private String signDateString;
	private String currentYear;

	private java.lang.Double hiring_monitor_consult;
	private java.lang.Integer total = 10000;

	@Override
	public VConstructionHcqtBO toModel() {
		VConstructionHcqtBO vConstructionHcqtBO = new VConstructionHcqtBO();
		vConstructionHcqtBO.setCatProjectId(this.catProjectId);
		vConstructionHcqtBO.setCatApProjectName(this.catApProjectName);
		vConstructionHcqtBO.setConstructId(this.constructId);
		vConstructionHcqtBO.setConstrtCode(this.constrtCode);
		vConstructionHcqtBO.setConstrType(this.constrType);
		vConstructionHcqtBO.setConstrTypeName(this.constrTypeName);
		vConstructionHcqtBO.setConstrTypeCode(this.constrTypeCode);
		vConstructionHcqtBO.setConstrtName(this.constrtName);
		vConstructionHcqtBO.setConstrtAddress(this.constrtAddress);
		vConstructionHcqtBO.setConstructorId(this.constructorId);
		vConstructionHcqtBO.setSupervisorId(this.supervisorId);
		vConstructionHcqtBO.setStartingDate(this.startingDate);
		vConstructionHcqtBO.setCompleteDate(this.completeDate);
		vConstructionHcqtBO.setSysUserId(this.sysUserId);
		vConstructionHcqtBO.setCatApProjectId(this.catApProjectId);
		vConstructionHcqtBO.setSysUserName(this.sysUserName);
		vConstructionHcqtBO.setSysGroupId(this.sysGroupId);
		vConstructionHcqtBO.setSysGroupName(this.sysGroupName);
		vConstructionHcqtBO.setSysCreatedDate(this.sysCreatedDate);
		vConstructionHcqtBO.setStatus(this.status);
		vConstructionHcqtBO.setExpectedCompleteDate(this.expectedCompleteDate);
		vConstructionHcqtBO.setStartComment(this.startComment);
		vConstructionHcqtBO.setIsActive(this.isActive);
		vConstructionHcqtBO.setProvinceId(this.provinceId);
		vConstructionHcqtBO.setDistrictId(this.districtId);
		vConstructionHcqtBO.setInvestProjectId(this.investProjectId);
		vConstructionHcqtBO.setInvestProjectName(this.investProjectName);
		vConstructionHcqtBO.setInvestProjectCode(this.investProjectCode);
		vConstructionHcqtBO.setStationId(this.stationId);
		vConstructionHcqtBO.setServiceProvideDate(this.serviceProvideDate);
		vConstructionHcqtBO.setDistrictCode(this.districtCode);
		vConstructionHcqtBO.setDistrictName(this.districtName);
		vConstructionHcqtBO.setHandoverDate(this.handoverDate);
		vConstructionHcqtBO.setStationCode(this.stationCode);
		vConstructionHcqtBO.setStationAddress(this.stationAddress);
		vConstructionHcqtBO.setProvinceCode(this.provinceCode);
		vConstructionHcqtBO.setConstructorName(this.constructorName);
		vConstructionHcqtBO.setSupervisorName(this.supervisorName);
		vConstructionHcqtBO.setHiring_monitor_consult(hiring_monitor_consult);
		// HieuNC 04/10/2017
		vConstructionHcqtBO.setInvestGroupName(investGroupName);
		vConstructionHcqtBO.setMonitorGroupName(monitorGroupName);
		vConstructionHcqtBO.setConsultGroupName(consultGroupName);
		vConstructionHcqtBO.setProvinceGroupId(provinceGroupId);
		vConstructionHcqtBO.setSettlementStatusId(settlementStatusId);
		// End
		return vConstructionHcqtBO;
	}

	public Long getCaStatus() {
		return CaStatus;
	}

	public void setCaStatus(Long caStatus) {
		CaStatus = caStatus;
	}

	public java.lang.Double getCatProjectId() {
		return catProjectId;
	}

	public void setCatProjectId(java.lang.Double catProjectId) {
		this.catProjectId = catProjectId;
	}

	public java.lang.String getCatApProjectName() {
		return catApProjectName;
	}

	public void setCatApProjectName(java.lang.String catApProjectName) {
		this.catApProjectName = catApProjectName;
	}

	public java.lang.Long getConstructId() {
		return constructId;
	}

	public void setConstructId(java.lang.Long constructId) {
		this.constructId = constructId;
	}

	public java.lang.String getConstrtCode() {
		return constrtCode;
	}

	public void setConstrtCode(java.lang.String constrtCode) {
		this.constrtCode = constrtCode;
	}

	public java.lang.Double getConstrType() {
		return constrType;
	}

	public void setConstrType(java.lang.Double constrType) {
		this.constrType = constrType;
	}

	public java.lang.String getConstrTypeName() {
		return constrTypeName;
	}

	public void setConstrTypeName(java.lang.String constrTypeName) {
		this.constrTypeName = constrTypeName;
	}

	public java.lang.String getConstrTypeCode() {
		return constrTypeCode;
	}

	public void setConstrTypeCode(java.lang.String constrTypeCode) {
		this.constrTypeCode = constrTypeCode;
	}

	public java.lang.String getConstrtName() {
		return Strings.nullToEmpty(constrtName);
	}

	public void setConstrtName(java.lang.String constrtName) {
		this.constrtName = constrtName;
	}

	public java.lang.String getConstrtAddress() {
		return Strings.nullToEmpty(constrtAddress);

	}

	public void setConstrtAddress(java.lang.String constrtAddress) {
		this.constrtAddress = constrtAddress;
	}

	public java.lang.Double getConstructorId() {
		return constructorId;
	}

	public void setConstructorId(java.lang.Double constructorId) {
		this.constructorId = constructorId;
	}

	public java.lang.Double getSupervisorId() {
		return supervisorId;
	}

	public void setSupervisorId(java.lang.Double supervisorId) {
		this.supervisorId = supervisorId;
	}

	public java.util.Date getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(java.util.Date startingDate) {
		this.startingDate = startingDate;
	}

	public java.util.Date getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(java.util.Date completeDate) {
		this.completeDate = completeDate;
	}

	public java.lang.Double getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(java.lang.Double sysUserId) {
		this.sysUserId = sysUserId;
	}

	public java.lang.Double getCatApProjectId() {
		return catApProjectId;
	}

	public void setCatApProjectId(java.lang.Double catApProjectId) {
		this.catApProjectId = catApProjectId;
	}

	public java.lang.String getSysUserName() {
		return sysUserName;
	}

	public void setSysUserName(java.lang.String sysUserName) {
		this.sysUserName = sysUserName;
	}

	public java.lang.Double getSysGroupId() {
		return sysGroupId;
	}

	public void setSysGroupId(java.lang.Double sysGroupId) {
		this.sysGroupId = sysGroupId;
	}

	public java.lang.String getSysGroupName() {
		return sysGroupName;
	}

	public void setSysGroupName(java.lang.String sysGroupName) {
		this.sysGroupName = sysGroupName;
	}

	public java.util.Date getSysCreatedDate() {
		return sysCreatedDate;
	}

	public void setSysCreatedDate(java.util.Date sysCreatedDate) {
		this.sysCreatedDate = sysCreatedDate;
	}

	public java.lang.Double getStatus() {
		return status;
	}

	public void setStatus(java.lang.Double status) {
		this.status = status;
	}

	public java.util.Date getExpectedCompleteDate() {
		return expectedCompleteDate;
	}

	public void setExpectedCompleteDate(java.util.Date expectedCompleteDate) {
		this.expectedCompleteDate = expectedCompleteDate;
	}

	public java.lang.String getStartComment() {
		return startComment;
	}

	public void setStartComment(java.lang.String startComment) {
		this.startComment = startComment;
	}

	public java.lang.Double getIsActive() {
		return isActive;
	}

	public void setIsActive(java.lang.Double isActive) {
		this.isActive = isActive;
	}

	public java.lang.Double getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(java.lang.Double provinceId) {
		this.provinceId = provinceId;
	}

	public java.lang.Double getDistrictId() {
		return districtId;
	}

	public void setDistrictId(java.lang.Double districtId) {
		this.districtId = districtId;
	}

	public java.lang.Double getInvestProjectId() {
		return investProjectId;
	}

	public void setInvestProjectId(java.lang.Double investProjectId) {
		this.investProjectId = investProjectId;
	}

	public java.lang.String getInvestProjectName() {
		return investProjectName;
	}

	public void setInvestProjectName(java.lang.String investProjectName) {
		this.investProjectName = investProjectName;
	}

	public java.lang.String getInvestProjectCode() {
		return investProjectCode;
	}

	public void setInvestProjectCode(java.lang.String investProjectCode) {
		this.investProjectCode = investProjectCode;
	}

	public java.lang.Double getStationId() {
		return stationId;
	}

	public void setStationId(java.lang.Double stationId) {
		this.stationId = stationId;
	}

	public java.util.Date getServiceProvideDate() {
		return serviceProvideDate;
	}

	public void setServiceProvideDate(java.util.Date serviceProvideDate) {
		this.serviceProvideDate = serviceProvideDate;
	}

	public java.lang.String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(java.lang.String districtCode) {
		this.districtCode = districtCode;
	}

	public java.lang.String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(java.lang.String districtName) {
		this.districtName = districtName;
	}

	public java.util.Date getHandoverDate() {
		return handoverDate;
	}

	public void setHandoverDate(java.util.Date handoverDate) {
		this.handoverDate = handoverDate;
	}

	public java.lang.String getStationCode() {
		return stationCode;
	}

	public void setStationCode(java.lang.String stationCode) {
		this.stationCode = stationCode;
	}

	public java.lang.String getStationAddress() {
		return stationAddress;
	}

	public void setStationAddress(java.lang.String stationAddress) {
		this.stationAddress = stationAddress;
	}

	public java.lang.String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(java.lang.String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public java.lang.String getConstructorName() {
		return Strings.nullToEmpty(constructorName);
	}

	public void setConstructorName(java.lang.String constructorName) {
		this.constructorName = constructorName;
	}

	public java.lang.String getSupervisorName() {
		return Strings.nullToEmpty(supervisorName);
	}

	public void setSupervisorName(java.lang.String supervisorName) {
		this.supervisorName = supervisorName;
	}

	public java.lang.String getProvinceName() {
		return Strings.nullToEmpty(provinceName);
	}

	public void setProvinceName(java.lang.String provinceName) {
		this.provinceName = provinceName;
	}

	public java.lang.Double getSuperForm() {
		return superForm;
	}

	public void setSuperForm(java.lang.Double superForm) {
		this.superForm = superForm;
	}

	public java.lang.Double getConstrtForm() {
		return constrtForm;
	}

	public void setConstrtForm(java.lang.Double constrtForm) {
		this.constrtForm = constrtForm;
	}

	public java.lang.Double getIsObstructed() {
		return isObstructed;
	}

	public void setIsObstructed(java.lang.Double isObstructed) {
		this.isObstructed = isObstructed;
	}

	public java.lang.Double getStatusId() {
		return statusId;
	}

	public void setStatusId(java.lang.Double statusId) {
		this.statusId = statusId;
	}

	public java.lang.String getStatusName() {
		return statusName;
	}

	public void setStatusName(java.lang.String statusName) {
		this.statusName = statusName;
	}

	public java.lang.String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(java.lang.String statusCode) {
		this.statusCode = statusCode;
	}

	public java.lang.Double getIsAccepted() {
		return isAccepted;
	}

	public void setIsAccepted(java.lang.Double isAccepted) {
		this.isAccepted = isAccepted;
	}

	public java.util.Date getPreStartingDate() {
		return preStartingDate;
	}

	public void setPreStartingDate(java.util.Date preStartingDate) {
		this.preStartingDate = preStartingDate;
	}

	public java.util.Date getAcceptStartDate() {
		return acceptStartDate;
	}

	public void setAcceptStartDate(java.util.Date acceptStartDate) {
		this.acceptStartDate = acceptStartDate;
	}

	public java.util.Date getAcceptFinishDate() {
		return acceptFinishDate;
	}

	public void setAcceptFinishDate(java.util.Date acceptFinishDate) {
		this.acceptFinishDate = acceptFinishDate;
	}

	public java.lang.Double getLineMode() {
		return lineMode;
	}

	public void setLineMode(java.lang.Double lineMode) {
		this.lineMode = lineMode;
	}

	public java.lang.Double getProjectType() {
		return projectType;
	}

	public void setProjectType(java.lang.Double projectType) {
		this.projectType = projectType;
	}

	public java.lang.String getConstructorPerson() {
		return constructorPerson;
	}

	public void setConstructorPerson(java.lang.String constructorPerson) {
		this.constructorPerson = constructorPerson;
	}

	public java.lang.String getConstructorPhone() {
		return constructorPhone;
	}

	public void setConstructorPhone(java.lang.String constructorPhone) {
		this.constructorPhone = constructorPhone;
	}

	public java.lang.String getSupervisorPerson() {
		return supervisorPerson;
	}

	public void setSupervisorPerson(java.lang.String supervisorPerson) {
		this.supervisorPerson = supervisorPerson;
	}

	public java.lang.String getSupervisorPhone() {
		return supervisorPhone;
	}

	public void setSupervisorPhone(java.lang.String supervisorPhone) {
		this.supervisorPhone = supervisorPhone;
	}

	public java.lang.Double getCableTypeId() {
		return cableTypeId;
	}

	public void setCableTypeId(java.lang.Double cableTypeId) {
		this.cableTypeId = cableTypeId;
	}

	public java.lang.Double getLineLength() {
		return lineLength;
	}

	public void setLineLength(java.lang.Double lineLength) {
		this.lineLength = lineLength;
	}

	public java.lang.Double getLineType() {
		return lineType;
	}

	public void setLineType(java.lang.Double lineType) {
		this.lineType = lineType;
	}

	public java.lang.String getExecuter() {
		return executer;
	}

	public void setExecuter(java.lang.String executer) {
		this.executer = executer;
	}

	public java.lang.Double getExecuteGroup() {
		return executeGroup;
	}

	public void setExecuteGroup(java.lang.Double executeGroup) {
		this.executeGroup = executeGroup;
	}

	public java.lang.String getExecutePhone() {
		return executePhone;
	}

	public void setExecutePhone(java.lang.String executePhone) {
		this.executePhone = executePhone;
	}

	public java.lang.Double getFinishTest() {
		return finishTest;
	}

	public void setFinishTest(java.lang.Double finishTest) {
		this.finishTest = finishTest;
	}

	public java.lang.Double getPercentFinish() {
		return percentFinish;
	}

	public void setPercentFinish(java.lang.Double percentFinish) {
		this.percentFinish = percentFinish;
	}

	public java.lang.Double getPreparedPremises() {
		return preparedPremises;
	}

	public void setPreparedPremises(java.lang.Double preparedPremises) {
		this.preparedPremises = preparedPremises;
	}

	public java.lang.Double getHandoverMaterials() {
		return handoverMaterials;
	}

	public void setHandoverMaterials(java.lang.Double handoverMaterials) {
		this.handoverMaterials = handoverMaterials;
	}

	public java.lang.Double getProgressCommitment() {
		return progressCommitment;
	}

	public void setProgressCommitment(java.lang.Double progressCommitment) {
		this.progressCommitment = progressCommitment;
	}

	public java.lang.String getContractCode() {
		return Strings.nullToEmpty(contractCode);
	}

	public void setContractCode(java.lang.String contractCode) {
		this.contractCode = contractCode;
	}

	public java.lang.Double getContractId() {
		return contractId;
	}

	public void setContractId(java.lang.Double contractId) {
		this.contractId = contractId;
	}

	public java.lang.Double getIsAcceptedContract() {
		return isAcceptedContract;
	}

	public void setIsAcceptedContract(java.lang.Double isAcceptedContract) {
		this.isAcceptedContract = isAcceptedContract;
	}

	public java.lang.Double getIsOff() {
		return isOff;
	}

	public void setIsOff(java.lang.Double isOff) {
		this.isOff = isOff;
	}

	@Override
	public Long getFWModelId() {
		// TODO Auto-generated method stub
		return constructId;
	}

	@Override
	public String catchName() {
		// TODO Auto-generated method stub
		return getConstructId().toString();
	}

	public java.lang.String getaMonitorPath() {
		return Strings.nullToEmpty(aMonitorPath);
	}

	public void setaMonitorPath(java.lang.String aMonitorPath) {
		this.aMonitorPath = aMonitorPath;
	}

	public java.lang.String getbConstructPath() {
		return Strings.nullToEmpty(bConstructPath);
	}

	public void setbConstructPath(java.lang.String bConstructPath) {
		this.bConstructPath = bConstructPath;
	}

	public java.lang.String getaDirectorPath() {
		return Strings.nullToEmpty(aDirectorPath);
	}

	public void setaDirectorPath(java.lang.String aDirectorPath) {
		this.aDirectorPath = aDirectorPath;
	}

	public java.lang.String getbDirectorPath() {
		return Strings.nullToEmpty(bDirectorPath);
	}

	public void setbDirectorPath(java.lang.String bDirectorPath) {
		this.bDirectorPath = bDirectorPath;
	}

	public java.lang.String getContractName() {
		return contractName;
	}

	public void setContractName(java.lang.String contractName) {
		this.contractName = contractName;
	}

	public java.util.Date getSigned_date() {
		return signed_date;
	}

	public void setSigned_date(java.util.Date signed_date) {
		this.signed_date = signed_date;
	}

	/**
	 * @return the total
	 */
	public java.lang.Integer getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(java.lang.Integer total) {
		this.total = total;
	}

	public java.lang.Double getContract_total_value() {
		return contract_total_value;
	}

	public void setContract_total_value(java.lang.Double contract_total_value) {
		this.contract_total_value = contract_total_value;
	}

	public java.lang.Double getHiring_monitor_consult() {
		return hiring_monitor_consult;
	}

	public void setHiring_monitor_consult(java.lang.Double hiring_monitor_consult) {
		this.hiring_monitor_consult = hiring_monitor_consult;
	}

	public String getSignDateString() {
		return Strings.nullToEmpty(signDateString);
	}

	public void setSignDateString(String signDateString) {
		this.signDateString = signDateString;
	}

	public String getCurrentYear() {
		return currentYear;
	}

	public void setCurrentYear(String currentYear) {
		this.currentYear = currentYear;
	}

	public String getInvestGroupName() {
		return investGroupName;
	}

	public void setInvestGroupName(String investGroupName) {
		this.investGroupName = investGroupName;
	}

	public String getMonitorGroupName() {
		return monitorGroupName;
	}

	public void setMonitorGroupName(String monitorGroupName) {
		this.monitorGroupName = monitorGroupName;
	}

	public String getConsultGroupName() {
		return consultGroupName;
	}

	public void setConsultGroupName(String consultGroupName) {
		this.consultGroupName = consultGroupName;
	}

	public Long getProvinceGroupId() {
		return provinceGroupId;
	}

	public void setProvinceGroupId(Long provinceGroupId) {
		this.provinceGroupId = provinceGroupId;
	}

	public Long getSettlementStatusId() {
		return settlementStatusId;
	}

	public void setSettlementStatusId(Long settlementStatusId) {
		this.settlementStatusId = settlementStatusId;
	}

	public Long getCompleteRecordNum() {
		return completeRecordNum;
	}

	public void setCompleteRecordNum(Long completeRecordNum) {
		this.completeRecordNum = completeRecordNum;
	}

	public Long getSignedRecordNum() {
		return signedRecordNum;
	}

	public void setSignedRecordNum(Long signedRecordNum) {
		this.signedRecordNum = signedRecordNum;
	}

	public Integer getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}

	private Double sumTotalMoney;
	private Double sumTotalMoneySettlement;

	public Double getSumTotalMoneySettlement() {
		return sumTotalMoneySettlement;
	}

	public void setSumTotalMoneySettlement(Double sumTotalMoneySettlement) {
		this.sumTotalMoneySettlement = sumTotalMoneySettlement;
	}

	private Double sumChenhLech;
	private Double sumTotalMoneyEvaluate;
	private Double totalACap;
	private Date acceptanceDate;

	public Double getSumTotalMoneyEvaluate() {
		return sumTotalMoneyEvaluate;
	}

	public void setSumTotalMoneyEvaluate(Double sumTotalMoneyEvaluate) {
		this.sumTotalMoneyEvaluate = sumTotalMoneyEvaluate;
	}

	public Double getSumChenhLech() {
		return sumChenhLech;
	}

	public void setSumChenhLech(Double sumChenhLech) {
		this.sumChenhLech = sumChenhLech;
	}

	public Double getSumTotalMoney() {
		return sumTotalMoney;
	}

	public void setSumTotalMoney(Double sumTotalMoney) {
		this.sumTotalMoney = sumTotalMoney;
	}

	/*---------------------ITSOL start--------------------------*/
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getStatusNameContract() {
		return statusNameContract;
	}

	public void setStatusNameContract(String statusNameContract) {
		this.statusNameContract = statusNameContract;
	}

	public Long getTotalConstr() {
		return totalConstr;
	}

	public void setTotalConstr(Long totalConstr) {
		this.totalConstr = totalConstr;
	}

	public String getTotalConstrDone() {
		return totalConstrDone;
	}

	public void setTotalConstrDone(String totalConstrDone) {
		this.totalConstrDone = totalConstrDone;
	}

	public String getTotalConstrWorking() {
		return totalConstrWorking;
	}

	public void setTotalConstrWorking(String totalConstrWorking) {
		this.totalConstrWorking = totalConstrWorking;
	}

	public String getTotalConstrBreak() {
		return totalConstrBreak;
	}

	public void setTotalConstrBreak(String totalConstrBreak) {
		this.totalConstrBreak = totalConstrBreak;
	}

	public Long getCountConstr() {
		return countConstr;
	}

	public void setCountConstr(Long countConstr) {
		this.countConstr = countConstr;
	}

	public String getTotalConstrNonStart() {
		return totalConstrNonStart;
	}

	public void setTotalConstrNonStart(String totalConstrNonStart) {
		this.totalConstrNonStart = totalConstrNonStart;
	}

	public Long getCountConstrDone() {
		return countConstrDone;
	}

	public void setCountConstrDone(Long countConstrDone) {
		this.countConstrDone = countConstrDone;
	}

	public Long getCountConstrNonStart() {
		return countConstrNonStart;
	}

	public void setCountConstrNonStart(Long countConstrNonStart) {
		this.countConstrNonStart = countConstrNonStart;
	}

	public Long getCountConstrWorking() {
		return countConstrWorking;
	}

	public void setCountConstrWorking(Long countConstrWorking) {
		this.countConstrWorking = countConstrWorking;
	}

	public Long getCountConstrBreak() {
		return countConstrBreak;
	}

	public void setCountConstrBreak(Long countConstrBreak) {
		this.countConstrBreak = countConstrBreak;
	}

	public String getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(String totalValue) {
		this.totalValue = totalValue;
	}

	public String getSettlementStatus() {
		return settlementStatus;
	}

	public void setSettlementStatus(String settlementStatus) {
		this.settlementStatus = settlementStatus;
	}

	public Long getSttProvinceGroupId() {
		return sttProvinceGroupId;
	}

	public void setSttProvinceGroupId(Long sttProvinceGroupId) {
		this.sttProvinceGroupId = sttProvinceGroupId;
	}

	public String getRomanCount() {
		return romanCount;
	}

	public void setRomanCount(String romanCount) {
		this.romanCount = romanCount;
	}

	/*---------------------ITSOL end--------------------------*/

	// Huynv start

	public Long getStatusApprovedLabor() {
		return statusApprovedLabor;
	}

	public void setStatusApprovedLabor(Long statusApprovedLabor) {
		this.statusApprovedLabor = statusApprovedLabor;
	}

	public Long getIsUpdatedLabor() {
		return isUpdatedLabor;
	}

	public void setIsUpdatedLabor(Long isUpdatedLabor) {
		this.isUpdatedLabor = isUpdatedLabor;
	}

	// Huynv end
	// hoanm1_20190705_start
	private java.lang.String areaCode;
	private java.lang.String stationHouseCode;

	public java.lang.String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(java.lang.String areaCode) {
		this.areaCode = areaCode;
	}

	public java.lang.String getStationHouseCode() {
		return stationHouseCode;
	}

	public void setStationHouseCode(java.lang.String stationHouseCode) {
		this.stationHouseCode = stationHouseCode;
	}
	// hoanm1_20190705_end

	// HuyPQ-20190726-start
	private String workItemName;

	public String getWorkItemName() {
		return workItemName;
	}

	public void setWorkItemName(String workItemName) {
		this.workItemName = workItemName;
	}

	// huy-end
	//HienLT56 start 19082020
	public Long checkEx;

	public Long getCheckEx() {
		return checkEx;
	}

	public void setCheckEx(Long checkEx) {
		this.checkEx = checkEx;
	}
	
	//HienLT56 end 19082020
}
