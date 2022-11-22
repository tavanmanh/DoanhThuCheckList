package com.viettel.coms.dto;

import java.util.Date;
import java.util.List;

import com.viettel.utils.CustomJsonDateDeserializer;
import com.viettel.utils.CustomJsonDateSerializer;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.WoBO;
import com.viettel.coms.bo.WoMappingChecklistBO;
import com.viettel.coms.bo.WoWorkLogsBO;
import com.viettel.coms.bo.WoXdddChecklistBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializer;
import com.viettel.erp.utils.JsonDateSerializerDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WoDTO extends ComsBaseFWDTO<WoBO> {
    private Long woId;
    private String woCode;
    private String woName;
    private Long woTypeId;
    private String woTypeName;
    private Long trId;
    private String trName;
    private String state;
    private Long constructionId;
    private String constructionName;
    private Long catWorkItemTypeId;
    private String catWorkItemTypeName;
    private String stationCode;
    private String userCreated;
    private String item;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date createdDate;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date finishDate;
    private Integer qoutaTime;
    private String executeMethod;
    private String quantityValue;
    private String cdLevel1;
    private String cdLevel1Name;
    private String cdLevel2;
    private String cdLevel2Name;
    private String cdLevel3;
    private String cdLevel3Name;
    private String cdLevel4;
    private String cdLevel4Name;
    private String cdLevel5;
    private String cdLevel5Name;
    private Long ftId;
    private String ftName;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date acceptTime;
    private String acceptTimeStr;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date endTime;
    private String endTimeStr;
    private String executeLat;
    private String executeLong;
    private Long status;
    private Long totalMonthPlanId;
    private String totalMonthPlanName;
    private Double moneyValue;
    private String moneyFlowBill;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date moneyFlowDate;
    private Long moneyFlowValue;
    private Long moneyFlowRequired;
    private String moneyFlowContent;
    private Long apConstructionType;
    private Long apWorkSrc;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date startTime;
    private String startTimeStr;

    public String loggedInUser;
    public String assignedCd;
    public String apWorkSrcName;
    public String apConstructionTypeName;
    public String trTypeName;
    public String trTypeCode;
    public String constructionType;
    public String constructionCode;
    public String contractCode;
    public String trExecuteLong;
    public String trExecuteLat;
    public String projectCode;

    private boolean inPlan;
    private boolean canFinish;
    private int roleForWo;
//    taotq start 23082021
    private String apWorkSource;
    private String apWorkSrcCode;
//  taotq start 23082021

    //ducpm
    private String manageUserName;
    private String descriptionFailure;
    private String describeAftemath;
    //ducpm

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date fromDate;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date toDate;


	private Long totalChecklistItem;
    private Long completedChecklistItem;
    private Long notCompletedChecklistItem;

    private String opinionResult;
    private String opinionComment;
    private Long contractId;
    private Long aioContractId;
    private Long quantityByDate;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date closedTime;
    private String aioContractCode;

    private Long checklistStep;
    private String catProvinceCode;

    private String userCdLevel2ReceiveWo;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date updateCdLevel2ReceiveWo;
    private String userCdLevel3ReceiveWo;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date updateCdLevel3ReceiveWo;
    private String userCdLevel4ReceiveWo;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date updateCdLevel4ReceiveWo;
    private String userCdLevel5ReceiveWo;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date updateCdLevel5ReceiveWo;
    private String userFtReceiveWo;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
	private Date updateFtReceiveWo;
    private String userCdApproveWo;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date updateCdApproveWo;
    private String userTthtApproveWo;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date updateTthtApproveWo;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date approveDateReportWo;
    private Long hcqtProjectId;

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date hshcReceiveDate;
    private String type;

    private String hcqtContractCode;
    private String cnkv;
    private String hcqtProjectName;
    private boolean filterWoTaskDaily;
    private String moneyValueString;
    private String cdDomainDataList;
    private String cdLevel5Email;

    private String userTcBranchApproveWo;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date updateTcBranchApproveWo;
    private String userTcTctApproveWo;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date updateTcTctApproveWo;

    private String updateCdLevel5ReceiveWoStr;

    private String overdueReason;
    private String overdueApproveState;
    private String overdueApprovePerson;
    private String overdueReasonText;

    private String description;
    private String vtnetWoCode;
    private String partner;

    private String woOrder;
    private Long voState;
    private String voRequestDept;
    private String voRequestRole;
    private String voApprovedDept;
    private String voApprovedRole;
    private String voMngtDept;
    private String voMngtRole;
    private Long woOrderId;
    private String businessType;
    private Long woOrderConfirm;
    private String emailTcTct;
    private Long catStationHouseId;
    private String catStationHouseTxt;
    private String checkImportWo;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date createdDate5s;
    private String jobUnfinished;
    private String stationVtnet;
    private String workItemBranch;

    //Huypq-22102021-start
    private Double moneyValueHtct;
    private String userDthtApprovedWo;
    private Date updateDthtApprovedWo;
    //Huy-end
    //Huyp-25102021-start
    private List<CatWorkItemTypeDTO> listWorkItem;
    //Huy-end
    //Huypq-02112021-start
    private String opinionType;
    private String placement;
    //Huy-end
    //Huypq-19032021-start
    private String tcBranchApproveWO;
    private String stateVuong;
    private Long woParentId;
    private String vtNetStationCode;
    //Huypq-14122021-start
    private String stateHtct;
    //Huy-end
    private EffectiveCalculationDetailsDTO effectiveDTO;
    //Duonghv13-11092021-start
    private String workContent;
    //Huypq-07022022-start
    private Boolean isCreateNew;
    private String ftPositionName;
    private String ftEmailSysUser;

    private Boolean checkUserKCQTD;
    private String manageUserCode;
    private String areaCode;
    private String serial;
    public String deviceName;

    private List<WoDTO> listWos;
    private List<WoWorkLogsBO> listWoWorkLogs;
    private List<WoXdddChecklistBO> listWoChecklists;
    private List<WoMappingChecklistBO> listWoMappingChecklists;
    private Long checklistId;
    private Boolean isIOC;
    private String invoicePeriod;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date stationRevenueDate;
    private String licenceName;//ducpm23 add 290722
    private UtilAttachDocumentDTO attachFile;//ducpm23 add 290722

    private SysUserRequest sysUserRequest;

    private List<Long> listChecklistId;

    public List<Long> getListChecklistId() {
        return listChecklistId;
    }

    public void setListChecklistId(List<Long> listChecklistId) {
        this.listChecklistId = listChecklistId;
    }

    public SysUserRequest getSysUserRequest() {
        return sysUserRequest;
    }

    public void setSysUserRequest(SysUserRequest sysUserRequest) {
        this.sysUserRequest = sysUserRequest;
    }

    public UtilAttachDocumentDTO getAttachFile() {
		return attachFile;
	}

	public void setAttachFile(UtilAttachDocumentDTO attachFile) {
		this.attachFile = attachFile;
	}

	public String getLicenceName() {
		return licenceName;
	}

	public void setLicenceName(String licenceName) {
		this.licenceName = licenceName;
	}

	public String getDeviceName() {
        return this.deviceName;
    }

    public boolean isAutoCreate() {
        return autoCreate;
    }

    boolean autoCreate;


    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    @JsonSerialize(using = CustomJsonDateSerializer.class)
    private java.util.Date deploymentDateReality;

    public Date getDeploymentDateReality() {
        return deploymentDateReality;
    }

    public void setDeploymentDateReality(Date deploymentDateReality) {
        this.deploymentDateReality = deploymentDateReality;
    }

    public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

    private String describeAfterMath;

    public String getTcBranchApproveWO() {
        return tcBranchApproveWO;
    }

    public void setTcBranchApproveWO(String tcBranchApproveWO) {
        this.tcBranchApproveWO = tcBranchApproveWO;
    }
    public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

    //Huy-end

    public String getWoOrder() {
        return woOrder;
    }

    public void setWoOrder(String woOrder) {
        this.woOrder = woOrder;
    }

    public Long getVoState() {
        return voState;
    }

    public void setVoState(Long voState) {
        this.voState = voState;
    }

    public String getVoRequestDept() {
        return voRequestDept;
    }

    public void setVoRequestDept(String voRequestDept) {
        this.voRequestDept = voRequestDept;
    }

    public String getVoRequestRole() {
        return voRequestRole;
    }

    public void setVoRequestRole(String voRequestRole) {
        this.voRequestRole = voRequestRole;
    }

    public String getVoApprovedDept() {
        return voApprovedDept;
    }

    public void setVoApprovedDept(String voApprovedDept) {
        this.voApprovedDept = voApprovedDept;
    }

    public String getVoApprovedRole() {
        return voApprovedRole;
    }

    public void setVoApprovedRole(String voApprovedRole) {
        this.voApprovedRole = voApprovedRole;
    }

    public String getVoMngtDept() {
        return voMngtDept;
    }

    public void setVoMngtDept(String voMngtDept) {
        this.voMngtDept = voMngtDept;
    }

    public String getVoMngtRole() {
        return voMngtRole;
    }

    public void setVoMngtRole(String voMngtRole) {
        this.voMngtRole = voMngtRole;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVtnetWoCode() {
        return vtnetWoCode;
    }

    public void setVtnetWoCode(String vtnetWoCode) {
        this.vtnetWoCode = vtnetWoCode;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getCdDomainDataList() {
        return cdDomainDataList;
    }

    public void setCdDomainDataList(String cdDomainDataList) {
        this.cdDomainDataList = cdDomainDataList;
    }

    public String getCnkv() {
        return cnkv;
    }

    public void setCnkv(String cnkv) {
        this.cnkv = cnkv;
    }

    public String getHcqtContractCode() {
        return hcqtContractCode;
    }

    public void setHcqtContractCode(String hcqtContractCode) {
        this.hcqtContractCode = hcqtContractCode;
    }

    public Long getHcqtProjectId() {
        return hcqtProjectId;
    }

    public void setHcqtProjectId(Long hcqtProjectId) {
        this.hcqtProjectId = hcqtProjectId;
    }

    public Date getHshcReceiveDate() {
        return hshcReceiveDate;
    }

    public void setHshcReceiveDate(Date hshcReceiveDate) {
        this.hshcReceiveDate = hshcReceiveDate;
    }

    public Long getChecklistStep() {
        return checklistStep;
    }

    public void setChecklistStep(Long checklistStep) {
        this.checklistStep = checklistStep;
    }

    public String getCatProvinceCode() {
        return catProvinceCode;
    }

    public void setCatProvinceCode(String catProvinceCode) {
        this.catProvinceCode = catProvinceCode;
    }

    public String getAioContractCode() {
        return aioContractCode;
    }

    public void setAioContractCode(String aioContractCode) {
        this.aioContractCode = aioContractCode;
    }

    public Date getClosedTime() {
        return closedTime;
    }

    public void setClosedTime(Date closedTime) {
        this.closedTime = closedTime;
    }

    public Long getQuantityByDate() {
        return quantityByDate;
    }

    public void setQuantityByDate(Long quantityByDate) {
        this.quantityByDate = quantityByDate;
    }

    public Long getAioContractId() {
        return aioContractId;
    }

    public void setAioContractId(Long aioContractId) {
        this.aioContractId = aioContractId;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    private Long catConstructionTypeId;

    private Double remainAmount;

    private String doneChecklistNumber;

    private String geoArea;
    private List<String> geoAreaList;

    private Long executeChecklist;

    private Integer parOrder;
    private String woTypeCode;
    private Long woNameId;
    private String woNameFromConfig;
    private String provinceCode;
    private String provinceName;
    private String provinceId;
    private Double acceptedMoneyValue;
    private Double acceptedMoneyValueDaily;
    private Double acceptedMoneyValueMonthly;

    private String trCode;
    private String catConstructionTypeName;
    private String catWorkItemTypeCode;
    private Long sysGroupId;
    private Long sysUserId;
    private Long catWorkItemTypeCatCon;
    private Long projectId;
    private String ftEmail;
    private String createdUserFullName;
    private String createdUserEmail;

    private Double dnqtValue;

    private List<Long> listWoTypeId;
    private String calculateMethod;
    private Long rejectedFtId;

    private Double approvedCollected;
    private Double unapprovedCollected;
    private boolean filterAutoExpire;
    private String autoExpire;

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date createdDateFrom;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date createdDateTo;
    private String createdDateStr;

    private List<WoXdddChecklistRequestDTO> xdddChecklist;
    private List<Long> xdddHshcChecklistItem;
    private Long createdUserId;

    private String bgbtsResult;

    public String getBgbtsResult() {
        return bgbtsResult;
    }

    public void setBgbtsResult(String bgbtsResult) {
        this.bgbtsResult = bgbtsResult;
    }

    public List<String> getGeoAreaList() {
        return geoAreaList;
    }

    public void setGeoAreaList(List<String> geoAreaList) {
        this.geoAreaList = geoAreaList;
    }

    public List<Long> getXdddHshcChecklistItem() {
        return xdddHshcChecklistItem;
    }

    public void setXdddHshcChecklistItem(List<Long> xdddHshcChecklistItem) {
        this.xdddHshcChecklistItem = xdddHshcChecklistItem;
    }

    public List<WoXdddChecklistRequestDTO> getXdddChecklist() {
        return xdddChecklist;
    }

    public void setXdddChecklist(List<WoXdddChecklistRequestDTO> xdddChecklist) {
        this.xdddChecklist = xdddChecklist;
    }

    public Date getCreatedDateFrom() {
        return createdDateFrom;
    }

    public void setCreatedDateFrom(Date createdDateFrom) {
        this.createdDateFrom = createdDateFrom;
    }

    public Date getCreatedDateTo() {
        return createdDateTo;
    }

    public void setCreatedDateTo(Date createdDateTo) {
        this.createdDateTo = createdDateTo;
    }

    public Double getApprovedCollected() {
        return approvedCollected;
    }

    public void setApprovedCollected(Double approvedCollected) {
        this.approvedCollected = approvedCollected;
    }

    public Double getUnapprovedCollected() {
        return unapprovedCollected;
    }

    public void setUnapprovedCollected(Double unapprovedCollected) {
        this.unapprovedCollected = unapprovedCollected;
    }

    public Long getRejectedFtId() {
        return rejectedFtId;
    }

    public void setRejectedFtId(Long rejectedFtId) {
        this.rejectedFtId = rejectedFtId;
    }

    public String getCalculateMethod() {
        return calculateMethod;
    }

    public void setCalculateMethod(String calculateMethod) {
        this.calculateMethod = calculateMethod;
    }

    public List<Long> getListWoTypeId() {
        return listWoTypeId;
    }

    public void setListWoTypeId(List<Long> listWoTypeId) {
        this.listWoTypeId = listWoTypeId;
    }

    public Double getDnqtValue() {
        return dnqtValue;
    }

    public void setDnqtValue(Double dnqtValue) {
        this.dnqtValue = dnqtValue;
    }

    public String getFtEmail() {
        return ftEmail;
    }

    public void setFtEmail(String ftEmail) {
        this.ftEmail = ftEmail;
    }

    public String getCreatedUserFullName() {
        return createdUserFullName;
    }

    public void setCreatedUserFullName(String createdUserFullName) {
        this.createdUserFullName = createdUserFullName;
    }

    public String getCreatedUserEmail() {
        return createdUserEmail;
    }

    public void setCreatedUserEmail(String createdUserEmail) {
        this.createdUserEmail = createdUserEmail;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getCatWorkItemTypeCatCon() {
        return catWorkItemTypeCatCon;
    }

    public void setCatWorkItemTypeCatCon(Long catWorkItemTypeCatCon) {
        this.catWorkItemTypeCatCon = catWorkItemTypeCatCon;
    }

    public String getCreatedDateStr() {
        return createdDateStr;
    }

    public void setCreatedDateStr(String createdDateStr) {
        this.createdDateStr = createdDateStr;
    }

    public String getCatWorkItemTypeCode() {
        return catWorkItemTypeCode;
    }

    public void setCatWorkItemTypeCode(String catWorkItemTypeCode) {
        this.catWorkItemTypeCode = catWorkItemTypeCode;
    }

    public Long getSysGroupId() {
        return sysGroupId;
    }

    public void setSysGroupId(Long sysGroupId) {
        this.sysGroupId = sysGroupId;
    }

    public Long getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Long sysUserId) {
        this.sysUserId = sysUserId;
    }

    public String getCatConstructionTypeName() {
        return catConstructionTypeName;
    }

    public void setCatConstructionTypeName(String catConstructionTypeName) {
        this.catConstructionTypeName = catConstructionTypeName;
    }

    public String getTrCode() {
        return trCode;
    }

    public void setTrCode(String trCode) {
        this.trCode = trCode;
    }

    public Double getAcceptedMoneyValue() {
        return acceptedMoneyValue;
    }

    public void setAcceptedMoneyValue(Double acceptedMoneyValue) {
        this.acceptedMoneyValue = acceptedMoneyValue;
    }

    public Double getAcceptedMoneyValueDaily() {
        return acceptedMoneyValueDaily;
    }

    public void setAcceptedMoneyValueDaily(Double acceptedMoneyValueDaily) {
        this.acceptedMoneyValueDaily = acceptedMoneyValueDaily;
    }

    public Double getAcceptedMoneyValueMonthly() {
        return acceptedMoneyValueMonthly;
    }

    public void setAcceptedMoneyValueMonthly(Double acceptedMoneyValueMonthly) {
        this.acceptedMoneyValueMonthly = acceptedMoneyValueMonthly;
    }

    public String getWoTypeCode() {
        return woTypeCode;
    }

    public void setWoTypeCode(String woTypeCode) {
        this.woTypeCode = woTypeCode;
    }

    public Integer getParOrder() {
        return parOrder;
    }

    public void setParOrder(Integer parOrder) {
        this.parOrder = parOrder;
    }

    public Long getExecuteChecklist() {
        return executeChecklist;
    }

    public void setExecuteChecklist(Long executeChecklist) {
        this.executeChecklist = executeChecklist;
    }

    public String getGeoArea() {
        return geoArea;
    }

    public void setGeoArea(String geoArea) {
        this.geoArea = geoArea;
    }

    public Double getRemainAmount() {
        return remainAmount;
    }

    public void setRemainAmount(Double remainAmount) {
        this.remainAmount = remainAmount;
    }

    public Long getWoId() {
        return woId;
    }

    public void setWoId(Long woId) {
        this.woId = woId;
    }

    public String getWoCode() {
        return woCode;
    }

    public void setWoCode(String woCode) {
        this.woCode = woCode;
    }

    public String getWoName() {
        return woName;
    }

    public void setWoName(String woName) {
        this.woName = woName;
    }

    public Long getWoTypeId() {
        return woTypeId;
    }

    public void setWoTypeId(Long woTypeId) {
        this.woTypeId = woTypeId;
    }

    public String getWoTypeName() {
        return woTypeName;
    }

    public void setWoTypeName(String woTypeName) {
        this.woTypeName = woTypeName;
    }

    public Long getTrId() {
        return trId;
    }

    public void setTrId(Long trId) {
        this.trId = trId;
    }

    public String getTrName() {
        return trName;
    }

    public void setTrName(String trName) {
        this.trName = trName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getConstructionId() {
        return constructionId;
    }

    public void setConstructionId(Long constructionId) {
        this.constructionId = constructionId;
    }

    public String getConstructionName() {
        return constructionName;
    }

    public void setConstructionName(String constructionName) {
        this.constructionName = constructionName;
    }

    public Long getCatWorkItemTypeId() {
        return catWorkItemTypeId;
    }

    public void setCatWorkItemTypeId(Long catWorkItemTypeId) {
        this.catWorkItemTypeId = catWorkItemTypeId;
    }

    public String getCatWorkItemTypeName() {
        return catWorkItemTypeName;
    }

    public void setCatWorkItemTypeName(String catWorkItemTypeName) {
        this.catWorkItemTypeName = catWorkItemTypeName;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public String getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(String userCreated) {
        this.userCreated = userCreated;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public Integer getQoutaTime() {
        return qoutaTime;
    }

    public void setQoutaTime(Integer qoutaTime) {
        this.qoutaTime = qoutaTime;
    }

    public String getExecuteMethod() {
        return executeMethod;
    }

    public void setExecuteMethod(String executeMethod) {
        this.executeMethod = executeMethod;
    }

    public String getQuantityValue() {
        return quantityValue;
    }

    public void setQuantityValue(String quantityValue) {
        this.quantityValue = quantityValue;
    }

    public String getCdLevel1() {
        return cdLevel1;
    }

    public void setCdLevel1(String cdLevel1) {
        this.cdLevel1 = cdLevel1;
    }

    public String getCdLevel1Name() {
        return cdLevel1Name;
    }

    public void setCdLevel1Name(String cdLevel1Name) {
        this.cdLevel1Name = cdLevel1Name;
    }

    public String getCdLevel2() {
        return cdLevel2;
    }

    public void setCdLevel2(String cdLevel2) {
        this.cdLevel2 = cdLevel2;
    }

    public String getCdLevel2Name() {
        return cdLevel2Name;
    }

    public void setCdLevel2Name(String cdLevel2Name) {
        this.cdLevel2Name = cdLevel2Name;
    }

    public String getCdLevel3() {
        return cdLevel3;
    }

    public void setCdLevel3(String cdLevel3) {
        this.cdLevel3 = cdLevel3;
    }

    public String getCdLevel3Name() {
        return cdLevel3Name;
    }

    public String getCdLevel4() {
        return cdLevel4;
    }

    public void setCdLevel4(String cdLevel4) {
        this.cdLevel4 = cdLevel4;
    }

    public String getCdLevel4Name() {
        return cdLevel4Name;
    }

    public void setCdLevel4Name(String cdLevel4Name) {
        this.cdLevel4Name = cdLevel4Name;
    }

    public void setCdLevel3Name(String cdLevel3Name) {
        this.cdLevel3Name = cdLevel3Name;
    }

    public Long getFtId() {
        return ftId;
    }

    public void setFtId(Long ftId) {
        this.ftId = ftId;
    }

    public String getFtName() {
        return ftName;
    }

    public void setFtName(String ftName) {
        this.ftName = ftName;
    }

    public Date getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(Date acceptTime) {
        this.acceptTime = acceptTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getExecuteLat() {
        return executeLat;
    }

    public void setExecuteLat(String executeLat) {
        this.executeLat = executeLat;
    }

    public String getExecuteLong() {
        return executeLong;
    }

    public void setExecuteLong(String executeLong) {
        this.executeLong = executeLong;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getTotalMonthPlanId() {
        return totalMonthPlanId;
    }

    public void setTotalMonthPlanId(Long totalMonthPlanId) {
        this.totalMonthPlanId = totalMonthPlanId;
    }

    public String getTotalMonthPlanName() {
        return totalMonthPlanName;
    }

    public void setTotalMonthPlanName(String totalMonthPlanName) {
        this.totalMonthPlanName = totalMonthPlanName;
    }

    public Double getMoneyValue() {
        return moneyValue;
    }

    public void setMoneyValue(Double moneyValue) {
        this.moneyValue = moneyValue;
    }

    public String getMoneyFlowBill() {
        return moneyFlowBill;
    }

    public void setMoneyFlowBill(String moneyFlowBill) {
        this.moneyFlowBill = moneyFlowBill;
    }

    public Date getMoneyFlowDate() {
        return moneyFlowDate;
    }

    public void setMoneyFlowDate(Date moneyFlowDate) {
        this.moneyFlowDate = moneyFlowDate;
    }

    public Long getMoneyFlowValue() {
        return moneyFlowValue;
    }

    public void setMoneyFlowValue(Long moneyFlowValue) {
        this.moneyFlowValue = moneyFlowValue;
    }

    public Long getMoneyFlowRequired() {
        return moneyFlowRequired;
    }

    public void setMoneyFlowRequired(Long moneyFlowRequired) {
        this.moneyFlowRequired = moneyFlowRequired;
    }

    public String getMoneyFlowContent() {
        return moneyFlowContent;
    }

    public void setMoneyFlowContent(String moneyFlowContent) {
        this.moneyFlowContent = moneyFlowContent;
    }

    public Long getApConstructionType() {
        return apConstructionType;
    }

    public void setApConstructionType(Long apConstructionType) {
        this.apConstructionType = apConstructionType;
    }

    public Long getApWorkSrc() {
        return apWorkSrc;
    }

    public void setApWorkSrc(Long apWorkSrc) {
        this.apWorkSrc = apWorkSrc;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(String loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public String getAssignedCd() {
        return assignedCd;
    }

    public void setAssignedCd(String assignedCd) {
        this.assignedCd = assignedCd;
    }

    public String getApWorkSrcName() {
        return apWorkSrcName;
    }

    public void setApWorkSrcName(String apWorkSrcName) {
        this.apWorkSrcName = apWorkSrcName;
    }

    public String getApConstructionTypeName() {
        return apConstructionTypeName;
    }

    public void setApConstructionTypeName(String apConstructionTypeName) {
        this.apConstructionTypeName = apConstructionTypeName;
    }

    public String getTrTypeName() {
        return trTypeName;
    }

    public void setTrTypeName(String trTypeName) {
        this.trTypeName = trTypeName;
    }

    public String getTrTypeCode() {
        return trTypeCode;
    }

    public void setTrTypeCode(String trTypeCode) {
        this.trTypeCode = trTypeCode;
    }

    public String getConstructionType() {
        return constructionType;
    }

    public void setConstructionType(String constructionType) {
        this.constructionType = constructionType;
    }

    public String getConstructionCode() {
        return constructionCode;
    }

    public void setConstructionCode(String constructionCode) {
        this.constructionCode = constructionCode;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getTrExecuteLong() {
        return trExecuteLong;
    }

    public void setTrExecuteLong(String trExecuteLong) {
        this.trExecuteLong = trExecuteLong;
    }

    public String getTrExecuteLat() {
        return trExecuteLat;
    }

    public void setTrExecuteLat(String trExecuteLat) {
        this.trExecuteLat = trExecuteLat;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public boolean isInPlan() {
        return inPlan;
    }

    public void setInPlan(boolean inPlan) {
        this.inPlan = inPlan;
    }

    public boolean isCanFinish() {
        return canFinish;
    }

    public void setCanFinish(boolean canFinish) {
        this.canFinish = canFinish;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Long getTotalChecklistItem() {
        return totalChecklistItem;
    }

    public void setTotalChecklistItem(Long totalChecklistItem) {
        this.totalChecklistItem = totalChecklistItem;
    }

    public Long getCompletedChecklistItem() {
        return completedChecklistItem;
    }

    public void setCompletedChecklistItem(Long completedChecklistItem) {
        this.completedChecklistItem = completedChecklistItem;
    }

    public Long getNotCompletedChecklistItem() {
        return notCompletedChecklistItem;
    }

    public void setNotCompletedChecklistItem(Long notCompletedChecklistItem) {
        this.notCompletedChecklistItem = notCompletedChecklistItem;
    }

    public String getOpinionResult() {
        return opinionResult;
    }

    public void setOpinionResult(String opinionResult) {
        this.opinionResult = opinionResult;
    }

    public String getOpinionComment() {
        return opinionComment;
    }

    public void setOpinionComment(String opinionComment) {
        this.opinionComment = opinionComment;
    }

    public int getRoleForWo() {
        return roleForWo;
    }

    public void setRoleForWo(int roleForWo) {
        this.roleForWo = roleForWo;
    }

    public Long getCatConstructionTypeId() {
        return catConstructionTypeId;
    }

    public void setCatConstructionTypeId(Long catConstructionTypeId) {
        this.catConstructionTypeId = catConstructionTypeId;
    }

    public String getDoneChecklistNumber() {
        return doneChecklistNumber;
    }

    public void setDoneChecklistNumber(String doneChecklistNumber) {
        this.doneChecklistNumber = doneChecklistNumber;
    }

    public Long getWoNameId() {
        return woNameId;
    }

    public void setWoNameId(Long woNameId) {
        this.woNameId = woNameId;
    }

    public String getWoNameFromConfig() {
        return woNameFromConfig;
    }

    public void setWoNameFromConfig(String woNameFromConfig) {
        this.woNameFromConfig = woNameFromConfig;
    }

    public String getAcceptTimeStr() {
        return acceptTimeStr;
    }

    public void setAcceptTimeStr(String acceptTimeStr) {
        this.acceptTimeStr = acceptTimeStr;
    }

    public String getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    public String getStartTimeStr() {
        return startTimeStr;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getUserCdLevel2ReceiveWo() {
        return userCdLevel2ReceiveWo;
    }

    public void setUserCdLevel2ReceiveWo(String userCdLevel2ReceiveWo) {
        this.userCdLevel2ReceiveWo = userCdLevel2ReceiveWo;
    }

    public Date getUpdateCdLevel2ReceiveWo() {
        return updateCdLevel2ReceiveWo;
    }

    public void setUpdateCdLevel2ReceiveWo(Date updateCdLevel2ReceiveWo) {
        this.updateCdLevel2ReceiveWo = updateCdLevel2ReceiveWo;
    }

    public String getUserCdLevel3ReceiveWo() {
        return userCdLevel3ReceiveWo;
    }

    public void setUserCdLevel3ReceiveWo(String userCdLevel3ReceiveWo) {
        this.userCdLevel3ReceiveWo = userCdLevel3ReceiveWo;
    }

    public Date getUpdateCdLevel3ReceiveWo() {
        return updateCdLevel3ReceiveWo;
    }

    public void setUpdateCdLevel3ReceiveWo(Date updateCdLevel3ReceiveWo) {
        this.updateCdLevel3ReceiveWo = updateCdLevel3ReceiveWo;
    }

    public String getUserCdLevel4ReceiveWo() {
        return userCdLevel4ReceiveWo;
    }

    public void setUserCdLevel4ReceiveWo(String userCdLevel4ReceiveWo) {
        this.userCdLevel4ReceiveWo = userCdLevel4ReceiveWo;
    }

    public Date getUpdateCdLevel4ReceiveWo() {
        return updateCdLevel4ReceiveWo;
    }

    public void setUpdateCdLevel4ReceiveWo(Date updateCdLevel4ReceiveWo) {
        this.updateCdLevel4ReceiveWo = updateCdLevel4ReceiveWo;
    }

    public String getUserFtReceiveWo() {
        return userFtReceiveWo;
    }

    public void setUserFtReceiveWo(String userFtReceiveWo) {
        this.userFtReceiveWo = userFtReceiveWo;
    }

    public Date getUpdateFtReceiveWo() {
        return updateFtReceiveWo;
    }

    public void setUpdateFtReceiveWo(Date updateFtReceiveWo) {
        this.updateFtReceiveWo = updateFtReceiveWo;
    }

    public String getUserCdApproveWo() {
        return userCdApproveWo;
    }

    public void setUserCdApproveWo(String userCdApproveWo) {
        this.userCdApproveWo = userCdApproveWo;
    }

    public Date getUpdateCdApproveWo() {
        return updateCdApproveWo;
    }

    public void setUpdateCdApproveWo(Date updateCdApproveWo) {
        this.updateCdApproveWo = updateCdApproveWo;
    }

    public String getUserTthtApproveWo() {
        return userTthtApproveWo;
    }

    public void setUserTthtApproveWo(String userTthtApproveWo) {
        this.userTthtApproveWo = userTthtApproveWo;
    }

    public Date getUpdateTthtApproveWo() {
        return updateTthtApproveWo;
    }

    public void setUpdateTthtApproveWo(Date updateTthtApproveWo) {
        this.updateTthtApproveWo = updateTthtApproveWo;
    }

    public Date getApproveDateReportWo() {
        return approveDateReportWo;
    }

    public void setApproveDateReportWo(Date approveDateReportWo) {
        this.approveDateReportWo = approveDateReportWo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHcqtProjectName() {
        return hcqtProjectName;
    }

    public void setHcqtProjectName(String hcqtProjectName) {
        this.hcqtProjectName = hcqtProjectName;
    }

    public boolean isFilterWoTaskDaily() {
        return filterWoTaskDaily;
    }

    public void setFilterWoTaskDaily(boolean filterWoTaskDaily) {
        this.filterWoTaskDaily = filterWoTaskDaily;
    }

    public String getMoneyValueString() {
        return moneyValueString;
    }

    public void setMoneyValueString(String moneyValueString) {
        this.moneyValueString = moneyValueString;
    }

    public String getCdLevel5() {
        return cdLevel5;
    }

    public void setCdLevel5(String cdLevel5) {
        this.cdLevel5 = cdLevel5;
    }

    public String getCdLevel5Name() {
        return cdLevel5Name;
    }

    public void setCdLevel5Name(String cdLevel5Name) {
        this.cdLevel5Name = cdLevel5Name;
    }

    public String getUserCdLevel5ReceiveWo() {
        return userCdLevel5ReceiveWo;
    }

    public void setUserCdLevel5ReceiveWo(String userCdLevel5ReceiveWo) {
        this.userCdLevel5ReceiveWo = userCdLevel5ReceiveWo;
    }

    public Date getUpdateCdLevel5ReceiveWo() {
        return updateCdLevel5ReceiveWo;
    }

    public void setUpdateCdLevel5ReceiveWo(Date updateCdLevel5ReceiveWo) {
        this.updateCdLevel5ReceiveWo = updateCdLevel5ReceiveWo;
    }

    public String getOverdueReason() {
		return overdueReason;
    }

    public void setOverdueReason(String overdueReason) {
        this.overdueReason = overdueReason;
    }

    public String getOverdueApproveState() {
        return overdueApproveState;
    }

    public void setOverdueApproveState(String overdueApproveState) {
        this.overdueApproveState = overdueApproveState;
    }

    public String getOverdueApprovePerson() {
        return overdueApprovePerson;
    }

    public void setOverdueApprovePerson(String overdueApprovePerson) {
        this.overdueApprovePerson = overdueApprovePerson;
    }

    public String getOverdueReasonText() {
        return overdueReasonText;
    }

    public void setOverdueReasonText(String overdueReasonText) {
        this.overdueReasonText = overdueReasonText;
    }

    @Override
    public WoBO toModel() {
        WoBO woBo = new WoBO();
        woBo.setWoId(this.woId);
        woBo.setWoCode(this.woCode);
        woBo.setWoName(this.woName);
        woBo.setWoTypeId(this.woTypeId);
        woBo.setTrId(this.trId);
        woBo.setState(this.state);
        woBo.setConstructionId(this.constructionId);
        woBo.setCatWorkItemTypeId(this.catWorkItemTypeId);
        woBo.setStationCode(this.stationCode);
        woBo.setUserCreated(this.userCreated);
        woBo.setCreatedDate(this.createdDate);
        woBo.setFinishDate(this.finishDate);
        woBo.setQoutaTime(this.qoutaTime);
        woBo.setExecuteMethod(this.executeMethod);
        woBo.setQuantityValue(this.quantityValue);
        woBo.setCdLevel1(this.cdLevel1);
        woBo.setCdLevel2(this.cdLevel2);
        woBo.setCdLevel3(this.cdLevel3);
        woBo.setCdLevel4(this.cdLevel4);
        woBo.setCdLevel5(this.cdLevel5);
        woBo.setFtId(this.ftId);
        woBo.setAcceptTime(this.acceptTime);
        woBo.setExecuteLat(this.executeLat);
        woBo.setExecuteLong(this.executeLong);
        woBo.setStatus(this.status);
        woBo.setTotalMonthPlanId(this.totalMonthPlanId);
        woBo.setMoneyValue(this.moneyValue);
        woBo.setMoneyFlowBill(this.moneyFlowBill);
        woBo.setMoneyFlowDate(this.moneyFlowDate);
        woBo.setMoneyFlowValue(this.moneyFlowValue);
        woBo.setMoneyFlowRequired(this.moneyFlowRequired);
        woBo.setMoneyFlowContent(this.moneyFlowContent);
        woBo.setApConstructionType(this.apConstructionType);
        woBo.setApWorkSrc(this.apWorkSrc);
        woBo.setEndTime(this.endTime);
        woBo.setStartTime(this.startTime);
        woBo.setOpinionResult(this.opinionResult);
        woBo.setExecuteChecklist(this.executeChecklist);
        woBo.setContractId(this.contractId);
        woBo.setWoNameId(this.woNameId);
        woBo.setQuantityByDate(this.quantityByDate);
        woBo.setClosedTime(this.closedTime);
        woBo.setConstructionCode(this.constructionCode);
        woBo.setContractCode(this.contractCode);
        woBo.setProjectId(this.projectId);
        woBo.setProjectCode(this.projectCode);
        woBo.setCdLevel1Name(this.cdLevel1Name);
        woBo.setCdLevel2Name(this.cdLevel2Name);
        woBo.setCdLevel3Name(this.cdLevel3Name);
        woBo.setCdLevel4Name(this.cdLevel4Name);
        woBo.setCdLevel5Name(this.cdLevel5Name);
        woBo.setFtName(this.ftName);
        woBo.setFtEmail(this.ftEmail);
        woBo.setCreatedUserFullName(this.createdUserFullName);
        woBo.setCreatedUserEmail(this.createdUserEmail);
        woBo.setTrCode(this.trCode);
        woBo.setCatConstructionTypeId(this.catConstructionTypeId);

        woBo.setUserCdLevel2ReceiveWo(this.userCdLevel2ReceiveWo);
        woBo.setUserCdLevel3ReceiveWo(this.userCdLevel3ReceiveWo);
        woBo.setUserCdLevel4ReceiveWo(this.userCdLevel4ReceiveWo);
        woBo.setUserCdLevel5ReceiveWo(this.userCdLevel5ReceiveWo);
        woBo.setUserFtReceiveWo(this.userFtReceiveWo);
        woBo.setUpdateCdLevel2ReceiveWo(this.updateCdLevel2ReceiveWo);
        woBo.setUpdateCdLevel3ReceiveWo(this.updateCdLevel3ReceiveWo);
        woBo.setUpdateCdLevel4ReceiveWo(this.updateCdLevel4ReceiveWo);
        woBo.setUpdateCdLevel5ReceiveWo(this.updateCdLevel5ReceiveWo);
        woBo.setUpdateFtReceiveWo(this.updateFtReceiveWo);

        woBo.setUserCdApproveWo(this.userCdApproveWo);
        woBo.setUpdateCdApproveWo(this.updateCdApproveWo);
        woBo.setUserTthtApproveWo(this.userTthtApproveWo);
        woBo.setUpdateTthtApproveWo(this.updateTthtApproveWo);
        woBo.setApproveDateReportWo(this.approveDateReportWo);
        woBo.setChecklistStep(this.checklistStep);
        woBo.setCatProvinceCode(this.catProvinceCode);
        woBo.setHcqtProjectId(this.hcqtProjectId);
        woBo.setHshcReceiveDate(this.hshcReceiveDate);
        woBo.setType(this.type);
        woBo.setHcqtContractCode(this.hcqtContractCode);
        woBo.setCnkv(this.cnkv);
        woBo.setAlarmId(this.alarmId);

        woBo.setUserTcBranchApproveWo(this.userTcBranchApproveWo);
        woBo.setUpdateTcBranchApproveWo(this.updateTcBranchApproveWo);
        woBo.setUserTcTctApproveWo(this.userTcTctApproveWo);
        woBo.setUpdateTcTctApproveWo(this.updateTcTctApproveWo);

        woBo.setOverdueReason(this.overdueReason);
        woBo.setOverdueApproveState(this.overdueApproveState);
        woBo.setOverdueApprovePerson(this.overdueApprovePerson);

        woBo.setDescription(this.description);
        woBo.setVtnetWoCode(this.vtnetWoCode);
        woBo.setPartner(this.partner);

        woBo.setWoOrder(this.woOrder);
        woBo.setVoState(this.voState);
        woBo.setVoRequestDept(this.voRequestDept);
        woBo.setVoRequestRole(this.voRequestRole);
        woBo.setVoApprovedDept(this.voApprovedDept);
        woBo.setVoApprovedRole(this.voApprovedRole);
        woBo.setVoMngtDept(this.voMngtDept);
        woBo.setVoMngtRole(this.voMngtRole);
        woBo.setWoOrderId(this.woOrderId);
        woBo.setBusinessType(this.businessType);
        woBo.setWoOrderConfirm(this.woOrderConfirm);
        woBo.setEmailTcTct(this.emailTcTct);
        woBo.setCatStationHouseId(this.catStationHouseId);
        woBo.setAutoExpire(this.autoExpire);
        woBo.setCreatedDate5s(this.createdDate5s);
        woBo.setMoneyValueHtct(this.moneyValueHtct);
        woBo.setUserDthtApprovedWo(this.userDthtApprovedWo);
        woBo.setUpdateDthtApprovedWo(this.updateDthtApprovedWo);
        woBo.setOpinionType(this.opinionType);
        woBo.setStateHtct(this.stateHtct);
        woBo.setDescribeAfterMath(this.describeAfterMath);
        woBo.setInvoicePeriod(this.invoicePeriod);
        woBo.setStationRevenueDate(this.stationRevenueDate);
        woBo.setLicenceName(this.licenceName);
        woBo.setBgbtsResult(this.bgbtsResult);
        return woBo;
    }

    @Override
    public Long getFWModelId() {
        return null;
    }

    @Override
    public String catchName() {
        return null;
    }

    //Huypq-07102020-start
    private String finishDateStr;
    private String approveDateReportWoStr;
    private String userTthtApproveWoStr;

    public String getFinishDateStr() {
        return finishDateStr;
    }

    public void setFinishDateStr(String finishDateStr) {
        this.finishDateStr = finishDateStr;
    }

    public String getApproveDateReportWoStr() {
        return approveDateReportWoStr;
    }

    public void setApproveDateReportWoStr(String approveDateReportWoStr) {
        this.approveDateReportWoStr = approveDateReportWoStr;
    }

    public String getUserTthtApproveWoStr() {
        return userTthtApproveWoStr;
    }

    public void setUserTthtApproveWoStr(String userTthtApproveWoStr) {
        this.userTthtApproveWoStr = userTthtApproveWoStr;
    }

    private String bill;

    public String getBill() {
        return bill;
    }

    public void setBill(String bill) {
        this.bill = bill;
    }

    private String doneStateWo;

    public String getDoneStateWo() {
        return doneStateWo;
    }

    public void setDoneStateWo(String doneStateWo) {
        this.doneStateWo = doneStateWo;
    }

    private String ftCode;

    public String getFtCode() {
        return ftCode;
    }

    public void setFtCode(String ftCode) {
        this.ftCode = ftCode;
    }
    //Huypq-end

    //HienLT56 start 01122020
    private String groupCreateWO;
    private String unitCreateOrBelongToWO;
    private String itemName;


    public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getUnitCreateOrBelongToWO() {
        return unitCreateOrBelongToWO;
    }

    public void setUnitCreateOrBelongToWO(String unitCreateOrBelongToWO) {
        this.unitCreateOrBelongToWO = unitCreateOrBelongToWO;
    }

    public String getGroupCreateWO() {
        return groupCreateWO;
    }

    public void setGroupCreateWO(String groupCreateWO) {
        this.groupCreateWO = groupCreateWO;
    }

    //HienLT56 end 01122020

    private boolean dontSendMocha;

    public boolean isDontSendMocha() {
        return dontSendMocha;
    }

    public void setDontSendMocha(boolean dontSendMocha) {
        this.dontSendMocha = dontSendMocha;
    }

    private String checklistItemNames;

    public String getChecklistItemNames() {
        return checklistItemNames;
    }

    public void setChecklistItemNames(String checklistItemNames) {
        this.checklistItemNames = checklistItemNames;
    }

    private boolean isMobile = false;

    public boolean isMobile() {
        return isMobile;
    }

    public void setMobile(boolean mobile) {
        isMobile = mobile;
    }

    private List<String> lstRolesOfWo;

    public List<String> getLstRolesOfWo() {
        return lstRolesOfWo;
    }

    public void setLstRolesOfWo(List<String> lstRolesOfWo) {
        this.lstRolesOfWo = lstRolesOfWo;
    }

    //Huypq-05012021-start
    private Long alarmId;

    public Long getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(Long alarmId) {
        this.alarmId = alarmId;
    }
    //Huy-end

    public String getCdLevel5Email() {
        return cdLevel5Email;
    }

    public void setCdLevel5Email(String cdLevel5Email) {
        this.cdLevel5Email = cdLevel5Email;
    }

    public String getUpdateCdLevel5ReceiveWoStr() {
        return updateCdLevel5ReceiveWoStr;
    }

    public void setUpdateCdLevel5ReceiveWoStr(String updateCdLevel5ReceiveWoStr) {
        this.updateCdLevel5ReceiveWoStr = updateCdLevel5ReceiveWoStr;
    }

    public String getUserTcBranchApproveWo() {
        return userTcBranchApproveWo;
    }

    public void setUserTcBranchApproveWo(String userTcBranchApproveWo) {
        this.userTcBranchApproveWo = userTcBranchApproveWo;
    }

    public Date getUpdateTcBranchApproveWo() {
        return updateTcBranchApproveWo;
    }

    public void setUpdateTcBranchApproveWo(Date updateTcBranchApproveWo) {
        this.updateTcBranchApproveWo = updateTcBranchApproveWo;
    }

    public String getUserTcTctApproveWo() {
        return userTcTctApproveWo;
    }

    public void setUserTcTctApproveWo(String userTcTctApproveWo) {
        this.userTcTctApproveWo = userTcTctApproveWo;
    }

    public Date getUpdateTcTctApproveWo() {
        return updateTcTctApproveWo;
    }

    public void setUpdateTcTctApproveWo(Date updateTcTctApproveWo) {
        this.updateTcTctApproveWo = updateTcTctApproveWo;
    }

    public Long getWoOrderId() {
        return woOrderId;
    }

    public void setWoOrderId(Long woOrderId) {
        this.woOrderId = woOrderId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public Long getWoOrderConfirm() {
        return woOrderConfirm;
    }

    public void setWoOrderConfirm(Long woOrderConfirm) {
        this.woOrderConfirm = woOrderConfirm;
    }

    public String getEmailTcTct() {
        return emailTcTct;
    }

    public void setEmailTcTct(String emailTcTct) {
        this.emailTcTct = emailTcTct;
    }

    // HienLT56 start 13012021
    private List<String> lstState;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date finishDateFrom;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date finishDateTo;

    public Date getFinishDateFrom() {
        return finishDateFrom;
    }

    public void setFinishDateFrom(Date finishDateFrom) {
        this.finishDateFrom = finishDateFrom;
    }

    public Date getFinishDateTo() {
        return finishDateTo;
    }

    public void setFinishDateTo(Date finishDateTo) {
        this.finishDateTo = finishDateTo;
    }

    public List<String> getLstState() {
        return lstState;
    }

    public void setLstState(List<String> lstState) {
        this.lstState = lstState;
    }

    // HienLT56 end 13012021

    public Long getCatStationHouseId() {
        return catStationHouseId;
    }

    public void setCatStationHouseId(Long catStationHouseId) {
        this.catStationHouseId = catStationHouseId;
    }

    public String getCatStationHouseTxt() {
        return catStationHouseTxt;
    }

    public void setCatStationHouseTxt(String catStationHouseTxt) {
        this.catStationHouseTxt = catStationHouseTxt;
    }

	// HienLT56 end 13012021
	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	//Huypq-30062021-start
	private String checkHTCT;

	public String getCheckHTCT() {
		return checkHTCT;
	}

	public void setCheckHTCT(String checkHTCT) {
		this.checkHTCT = checkHTCT;
	}
	//Huy-30062021-end
	public String getCheckImportWo() {
		return checkImportWo;
	}

	public void setCheckImportWo(String checkImportWo) {
		this.checkImportWo = checkImportWo;
	}
//Start add information for AVG
    private String orderCodeTgdd;
    private String orderCodeAvg;
    private String customerName;
    private String phoneNumber;
    private String personalId;
    private String address;
    private String productCode;
    private String paymentStatus;

    public String getOrderCodeTgdd() {
        return orderCodeTgdd;
    }

    public void setOrderCodeTgdd(String orderCodeTgdd) {
        this.orderCodeTgdd = orderCodeTgdd;
    }

    public String getOrderCodeAvg() {
        return orderCodeAvg;
    }

    public void setOrderCodeAvg(String orderCodeAvg) {
        this.orderCodeAvg = orderCodeAvg;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    //End add information for AVG

	public String getApWorkSource() {
		return apWorkSource;
	}

	public void setApWorkSource(String apWorkSource) {
		this.apWorkSource = apWorkSource;
	}

	public String getApWorkSrcCode() {
		return apWorkSrcCode;
	}

	public void setApWorkSrcCode(String apWorkSrcCode) {
		this.apWorkSrcCode = apWorkSrcCode;
	}

	public boolean isFilterAutoExpire() {
		return filterAutoExpire;
	}

	public void setFilterAutoExpire(boolean filterAutoExpire) {
		this.filterAutoExpire = filterAutoExpire;
	}

	public String getAutoExpire() {
		return autoExpire;
	}

	public void setAutoExpire(String autoExpire) {
		this.autoExpire = autoExpire;
	}

	public Date getCreatedDate5s() {
		return createdDate5s;
	}

	public void setCreatedDate5s(Date createdDate5s) {
		this.createdDate5s = createdDate5s;
	}

	public String getJobUnfinished() {
		return jobUnfinished;
	}

	public void setJobUnfinished(String jobUnfinished) {
		this.jobUnfinished = jobUnfinished;
	}

	public String getStationVtnet() {
		return stationVtnet;
	}

	public void setStationVtnet(String stationVtnet) {
		this.stationVtnet = stationVtnet;
	}

	public Double getMoneyValueHtct() {
		return moneyValueHtct;
	}

	public void setMoneyValueHtct(Double moneyValueHtct) {
		this.moneyValueHtct = moneyValueHtct;
	}

	public String getUserDthtApprovedWo() {
		return userDthtApprovedWo;
	}

	public void setUserDthtApprovedWo(String userDthtApprovedWo) {
		this.userDthtApprovedWo = userDthtApprovedWo;
	}

	public Date getUpdateDthtApprovedWo() {
		return updateDthtApprovedWo;
	}

	public void setUpdateDthtApprovedWo(Date updateDthtApprovedWo) {
		this.updateDthtApprovedWo = updateDthtApprovedWo;
	}

	public List<CatWorkItemTypeDTO> getListWorkItem() {
		return listWorkItem;
	}

	public void setListWorkItem(List<CatWorkItemTypeDTO> listWorkItem) {
		this.listWorkItem = listWorkItem;
	}

	public String getOpinionType() {
		return opinionType;
	}

	public void setOpinionType(String opinionType) {
		this.opinionType = opinionType;
	}

	public String getPlacement() {
		return placement;
	}

	public void setPlacement(String placement) {
		this.placement = placement;
	}

	public String getStateVuong() {
		return stateVuong;
	}

	public void setStateVuong(String stateVuong) {
		this.stateVuong = stateVuong;
	}

	public Long getWoParentId() {
		return woParentId;
	}

	public void setWoParentId(Long woParentId) {
		this.woParentId = woParentId;
	}

	public String getWorkContent() {
		return workContent;
	}

	public void setWorkContent(String workContent) {
		this.workContent = workContent;
	}

	public String getVtNetStationCode() {
		return vtNetStationCode;
	}

	public void setVtNetStationCode(String vtNetStationCode) {
		this.vtNetStationCode = vtNetStationCode;
	}

	public String getStateHtct() {
		return stateHtct;
	}

	public void setStateHtct(String stateHtct) {
		this.stateHtct = stateHtct;
	}

	public EffectiveCalculationDetailsDTO getEffectiveDTO() {
		return effectiveDTO;
	}

	public void setEffectiveDTO(EffectiveCalculationDetailsDTO effectiveDTO) {
		this.effectiveDTO = effectiveDTO;
	}

	public String getWorkItemBranch() {
		return workItemBranch;
	}

	public void setWorkItemBranch(String workItemBranch) {
		this.workItemBranch = workItemBranch;
	}

	public Boolean getIsCreateNew() {
		return isCreateNew;
	}

	public void setIsCreateNew(Boolean isCreateNew) {
		this.isCreateNew = isCreateNew;
	}

	public String getFtPositionName() {
		return ftPositionName;
	}

	public void setFtPositionName(String ftPositionName) {
		this.ftPositionName = ftPositionName;
	}

	public String getFtEmailSysUser() {
		return ftEmailSysUser;
	}

	public void setFtEmailSysUser(String ftEmailSysUser) {
		this.ftEmailSysUser = ftEmailSysUser;
	}

	public Boolean getCheckUserKCQTD() {
		return checkUserKCQTD;
	}

	public void setCheckUserKCQTD(Boolean checkUserKCQTD) {
		this.checkUserKCQTD = checkUserKCQTD;
	}

	public String getManageUserCode() {
		return manageUserCode;
	}

	public void setManageUserCode(String manageUserCode) {
		this.manageUserCode = manageUserCode;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
    public String getManageUserName() {
		return manageUserName;
	}

	public void setManageUserName(String manageUserName) {
		this.manageUserName = manageUserName;
	}

	public String getDescriptionFailure() {
		return descriptionFailure;
	}

	public void setDescriptionFailure(String descriptionFailure) {
		this.descriptionFailure = descriptionFailure;
	}

	public String getDescribeAftemath() {
		return describeAftemath;
	}

	public void setDescribeAftemath(String describeAftemath) {
		this.describeAftemath = describeAftemath;
	}


    public String getDescribeAfterMath() {

        return describeAfterMath;
    }

    public void setDescribeAfterMath(String describeAfterMath) {

        this.describeAfterMath = describeAfterMath;
    }
	public List<WoDTO> getListWos() {
		return listWos;
	}

	public void setListWos(List<WoDTO> listWos) {
		this.listWos = listWos;
	}

	public List<WoWorkLogsBO> getListWoWorkLogs() {
		return listWoWorkLogs;
	}

	public void setListWoWorkLogs(List<WoWorkLogsBO> listWoWorkLogs) {
		this.listWoWorkLogs = listWoWorkLogs;
	}

	public List<WoXdddChecklistBO> getListWoChecklists() {
		return listWoChecklists;
	}

	public void setListWoChecklists(List<WoXdddChecklistBO> listWoChecklists) {
		this.listWoChecklists = listWoChecklists;
	}

	public List<WoMappingChecklistBO> getListWoMappingChecklists() {
		return listWoMappingChecklists;
	}

	public void setListWoMappingChecklists(List<WoMappingChecklistBO> listWoMappingChecklists) {
		this.listWoMappingChecklists = listWoMappingChecklists;
	}

	public Long getChecklistId() {
		return checklistId;
	}

	public void setChecklistId(Long checklistId) {
		this.checklistId = checklistId;
	}

	public Boolean getIsIOC() {
		return isIOC;
	}

	public void setIsIOC(Boolean isIOC) {
		this.isIOC = isIOC;
	}

	public Long getCreatedUserId() {
		return createdUserId;
	}

	public void setCreatedUserId(Long createdUserId) {
		this.createdUserId = createdUserId;
	}

    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    @JsonSerialize(using = CustomJsonDateSerializer.class)
    private Date handoverUseDateReality;

    public Date getHandoverUseDateReality() {
        return handoverUseDateReality;
    }

    public void setHandoverUseDateReality(Date handoverUseDateReality) {
        this.handoverUseDateReality = handoverUseDateReality;
    }

    private Integer pmtStatus;

    public Integer getPmtStatus() {
        return pmtStatus;
    }

    public void setPmtStatus(Integer pmtStatus) {
        this.pmtStatus = pmtStatus;
    }
	public String getInvoicePeriod() {
		return invoicePeriod;
	}

	public void setInvoicePeriod(String invoicePeriod) {
		this.invoicePeriod = invoicePeriod;
	}

	public Date getStationRevenueDate() {
		return stationRevenueDate;
	}

	public void setStationRevenueDate(Date stationRevenueDate) {
		this.stationRevenueDate = stationRevenueDate;
	}

    // ThanhPT - WO BGBTS - Start
    List<com.viettel.wms.dto.AppParamDTO> lstAppParams;

    public List<com.viettel.wms.dto.AppParamDTO> getLstAppParams() {
        return lstAppParams;
    }

    public void setLstAppParams(List<com.viettel.wms.dto.AppParamDTO> lstAppParams) {
        this.lstAppParams = lstAppParams;
    }
    // ThanhPT - WO BGBTS - End
}
