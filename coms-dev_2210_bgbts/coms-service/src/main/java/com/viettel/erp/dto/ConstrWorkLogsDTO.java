/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.erp.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.google.common.base.Strings;
import com.viettel.erp.bo.ConstrWorkLogsBO;
import com.viettel.erp.utils.CustomJsonDateDeserializer;
import com.viettel.erp.utils.CustomJsonDateSerializer;
import com.viettel.ktts2.common.UDate;
import com.viettel.service.base.dto.BaseFWDTOImpl;

/**
 *
 * @author thuannht
 */
@XmlRootElement(name = "CONSTR_WORK_LOGSBO")
public class ConstrWorkLogsDTO extends BaseFWDTOImpl<ConstrWorkLogsBO> {

	private java.lang.Long constructId;
	private java.lang.Long aMonitorId;
	private java.lang.Long bConstructId;
	private java.lang.String aMonitorPath;
	private java.lang.String bConstructPath;
	private java.lang.Long constrWorkLogsId;
	private java.lang.String code;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date logDate;
	private java.lang.String workContent;
	private java.lang.String additionChangeArise;
	private java.lang.String contractorComments;
	private java.lang.String monitorComments;
	private java.util.Date createdDate;
	private java.lang.Long createdUserId;
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	private java.util.Date approvalDate;
	private java.lang.Long statusCa;
	private java.lang.Long estimatesWorkItemId;
	private java.lang.Long isActive;

	private String aMonitorName;
	private String bConstructName;

	private String aMonitorNameSign;
	private String bConstructNameSign;

	private String workItemName;
	private String creatOrUpdate;

	private String contractCode;
	private String constrtCode;
	private String constrtName;
	private String constrtAddress;
	private Long constrCompReMapId;

	private String comments;

	// Hanhls1 -20170823 bo sung them tu dto cua bang
	private Long processId;
	private Long catWeatherId;

	private Long catEmployeeId;
	private Long isWork;
	private Long catCauseNotWorkId;
	private String catCauseNotWorkName;
	private String documentCaId;

	private List<Long> lstConstrWorkLogsId;

	public List<Long> getLstConstrWorkLogsId() {
		return lstConstrWorkLogsId;
	}

	public void setLstConstrWorkLogsId(List<Long> lstConstrWorkLogsId) {
		this.lstConstrWorkLogsId = lstConstrWorkLogsId;
	}

	public String getDocumentCaId() {
		return documentCaId;
	}

	public void setDocumentCaId(String documentCaId) {
		this.documentCaId = documentCaId;
	}

	// Th??ng tin c??c ?????i thi c??ng
	private String catConstrTeamName;
	private Long numOfTeam;

	public String toLogDateString() {
		if (logDate == null) {
			return "";
		}
		return UDate.toSimpleFormat(logDate);
	}

	public Long getCatWeatherId() {
		return catWeatherId;
	}

	public void setCatWeatherId(Long catWeatherId) {
		this.catWeatherId = catWeatherId;
	}

	public String getCatConstrTeamName() {
		return catConstrTeamName;
	}

	public void setCatConstrTeamName(String catConstrTeamName) {
		this.catConstrTeamName = catConstrTeamName;
	}

	public Long getNumOfTeam() {
		return numOfTeam;
	}

	public void setNumOfTeam(Long numOfTeam) {
		this.numOfTeam = numOfTeam;
	}

	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

	public Long getCatEmployeeId() {
		return catEmployeeId;
	}

	public void setCatEmployeeId(Long catEmployeeId) {
		this.catEmployeeId = catEmployeeId;
	}

	public Long getIsWork() {
		return isWork;
	}

	public void setIsWork(Long isWork) {
		this.isWork = isWork;
	}

	public Long getCatCauseNotWorkId() {
		return catCauseNotWorkId;
	}

	public void setCatCauseNotWorkId(Long catCauseNotWorkId) {
		this.catCauseNotWorkId = catCauseNotWorkId;
	}

	public String getCatCauseNotWorkName() {
		return catCauseNotWorkName;
	}

	public void setCatCauseNotWorkName(String catCauseNotWorkName) {
		this.catCauseNotWorkName = catCauseNotWorkName;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	// private int curentDate;
	// private int curentMonth;
	private String curentDate = "";
	private String curentMonth = "";
	private String curentYear = "";

	private String stationCode;

	@Override
	public ConstrWorkLogsBO toModel() {
		ConstrWorkLogsBO constrWorkLogsBO = new ConstrWorkLogsBO();
		constrWorkLogsBO.setConstructId(this.constructId);
		constrWorkLogsBO.setAMonitorId(this.aMonitorId);
		constrWorkLogsBO.setBConstructId(this.bConstructId);
		constrWorkLogsBO.setConstrWorkLogsId(this.constrWorkLogsId);
		constrWorkLogsBO.setCode(this.code);
		constrWorkLogsBO.setLogDate(this.logDate);
		constrWorkLogsBO.setWorkContent(this.workContent);
		constrWorkLogsBO.setAdditionChangeArise(this.additionChangeArise);
		constrWorkLogsBO.setContractorComments(this.contractorComments);
		constrWorkLogsBO.setMonitorComments(this.monitorComments);
		constrWorkLogsBO.setCreatedDate(this.createdDate);
		constrWorkLogsBO.setCreatedUserId(this.createdUserId);
		constrWorkLogsBO.setApprovalDate(this.approvalDate);
		constrWorkLogsBO.setStatusCa(this.statusCa);
		constrWorkLogsBO.setEstimatesWorkItemId(this.estimatesWorkItemId);
		constrWorkLogsBO.setIsActive(this.isActive);
		constrWorkLogsBO.setConstructionCondition(this.constructionCondition);
		constrWorkLogsBO.setWorkerCount(this.workerCount);
		return constrWorkLogsBO;
	}

	public java.lang.Long getConstructId() {
		return constructId;
	}

	public void setConstructId(java.lang.Long constructId) {
		this.constructId = constructId;
	}

	public java.lang.Long getAMonitorId() {
		return aMonitorId;
	}

	public void setAMonitorId(java.lang.Long aMonitorId) {
		this.aMonitorId = aMonitorId;
	}

	public java.lang.Long getBConstructId() {
		return bConstructId;
	}

	public void setBConstructId(java.lang.Long bConstructId) {
		this.bConstructId = bConstructId;
	}

	@Override
	public Long getFWModelId() {
		return constrWorkLogsId;
	}

	@Override
	public String catchName() {
		return getConstrWorkLogsId().toString();
	}

	public java.lang.Long getConstrWorkLogsId() {
		return constrWorkLogsId;
	}

	public void setConstrWorkLogsId(java.lang.Long constrWorkLogsId) {
		this.constrWorkLogsId = constrWorkLogsId;
	}

	public java.lang.String getCode() {
		return code;
	}

	public void setCode(java.lang.String code) {
		this.code = code;
	}

	public java.util.Date getLogDate() {
		return logDate;
	}

	public void setLogDate(java.util.Date logDate) {
		this.logDate = logDate;
	}

	public java.lang.String getWorkContent() {
		return Strings.nullToEmpty(workContent);
	}

	public void setWorkContent(java.lang.String workContent) {
		this.workContent = workContent;
	}

	public java.lang.String getAdditionChangeArise() {
		return Strings.nullToEmpty(additionChangeArise);
	}

	public void setAdditionChangeArise(java.lang.String additionChangeArise) {
		this.additionChangeArise = additionChangeArise;
	}

	public java.lang.String getContractorComments() {
		return Strings.nullToEmpty(contractorComments);
	}

	public void setContractorComments(java.lang.String contractorComments) {
		this.contractorComments = contractorComments;
	}

	public java.lang.String getMonitorComments() {
		return Strings.nullToEmpty(monitorComments);
	}

	public void setMonitorComments(java.lang.String monitorComments) {
		this.monitorComments = monitorComments;
	}

	public java.util.Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(java.util.Date createdDate) {
		this.createdDate = createdDate;
	}

	public java.lang.Long getCreatedUserId() {
		return createdUserId;
	}

	public void setCreatedUserId(java.lang.Long createdUserId) {
		this.createdUserId = createdUserId;
	}

	public java.util.Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(java.util.Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public java.lang.Long getStatusCa() {
		return statusCa;
	}

	public void setStatusCa(java.lang.Long statusCa) {
		this.statusCa = statusCa;
	}

	public java.lang.Long getEstimatesWorkItemId() {
		return estimatesWorkItemId;
	}

	public void setEstimatesWorkItemId(java.lang.Long estimatesWorkItemId) {
		this.estimatesWorkItemId = estimatesWorkItemId;
	}

	public java.lang.Long getIsActive() {
		return isActive;
	}

	public void setIsActive(java.lang.Long isActive) {
		this.isActive = isActive;
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

	public String getWorkItemName() {
		return Strings.nullToEmpty(workItemName);
	}

	public void setWorkItemName(String workItemName) {
		this.workItemName = workItemName;
	}

	public String getCreatOrUpdate() {
		return creatOrUpdate;
	}

	public void setCreatOrUpdate(String creatOrUpdate) {
		this.creatOrUpdate = creatOrUpdate;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getConstrtCode() {
		return constrtCode;
	}

	public void setConstrtCode(String constrtCode) {
		this.constrtCode = constrtCode;
	}

	public String getConstrtName() {
		return constrtName;
	}

	public void setConstrtName(String constrtName) {
		this.constrtName = constrtName;
	}

	public String getConstrtAddress() {
		return constrtAddress;
	}

	public void setConstrtAddress(String constrtAddress) {
		this.constrtAddress = constrtAddress;
	}

	public Long getConstrCompReMapId() {
		return constrCompReMapId;
	}

	public void setConstrCompReMapId(Long constrCompReMapId) {
		this.constrCompReMapId = constrCompReMapId;
	}

	// public int getCurentDate() {
	// return curentDate;
	// }
	//
	// public void setCurentDate(int curentDate) {
	// this.curentDate = curentDate;
	// }
	//
	// public int getCurentMonth() {
	// return curentMonth;
	// }
	//
	// public void setCurentMonth(int curentMonth) {
	// this.curentMonth = curentMonth;
	// }

	public String getCurentYear() {
		if (logDate != null) {
			curentYear = DateFormatUtils.format(logDate, "yyyy");
		}
		return curentYear;
	}

	public String getCurentDate() {
		if (logDate != null) {
			curentDate = DateFormatUtils.format(logDate, "dd");
		}
		return curentDate;
	}

	public void setCurentDate(String curentDate) {
		this.curentDate = curentDate;
	}

	public String getCurentMonth() {
		if (logDate != null) {
			curentMonth = DateFormatUtils.format(logDate, "MM");
		}
		return curentMonth;
	}

	public String getCatWeatherName() {
		if (catWeatherId == null) {
			return "N/A";
		}
		if (1 == catWeatherId) {
			return "M??a";
		} else if (2 == catWeatherId) {
			return "B??o";
		} else if (3 == catWeatherId) {
			return "L??";
		} else if (4 == catWeatherId) {
			return "N???ng";
		} else if (5 == catWeatherId) {
			return "B??nh th?????ng";
		}
		return "N/A";

	}

	public void setCurentMonth(String curentMonth) {
		this.curentMonth = curentMonth;
	}

	public void setCurentYear(String curentYear) {
		this.curentYear = curentYear;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public java.lang.String getaMonitorPath() {
		return aMonitorPath;
	}

	public void setaMonitorPath(java.lang.String aMonitorPath) {
		this.aMonitorPath = aMonitorPath;
	}

	public java.lang.String getbConstructPath() {
		return bConstructPath;
	}

	public void setbConstructPath(java.lang.String bConstructPath) {
		this.bConstructPath = bConstructPath;
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

	// Huypq-20190813-start
	private java.lang.String constructionCondition;
	private java.lang.String workerCount;

	public java.lang.String getConstructionCondition() {
		return constructionCondition;
	}

	public void setConstructionCondition(java.lang.String constructionCondition) {
		this.constructionCondition = constructionCondition;
	}

	public java.lang.String getWorkerCount() {
		return workerCount;
	}

	public void setWorkerCount(java.lang.String workerCount) {
		this.workerCount = workerCount;
	}

	// Huy-end

}
