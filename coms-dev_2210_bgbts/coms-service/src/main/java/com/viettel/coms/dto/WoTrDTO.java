package com.viettel.coms.dto;

import com.viettel.coms.bo.WoTrBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializer;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WoTrDTO extends ComsBaseFWDTO<WoTrBO> {
    private Long trId;
    private String trCode;
    private String trName;
    private Long trTypeId;
    private String contractCode;
    private String projectCode;
    private String userCreated;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date createdDate;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date finishDate;
    private Date startDate;
    private String state;
    private Integer qoutaTime;
    private String executeLat;
    private String executeLong;
    private String cdLevel1;
    private String cdLevel1Name;
    private int status;

    private Double quantityValue;
    private Long customerType;
    private Long contractType;

    private String userApproveTr;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date updateApproveTr;
    private String cdLevel2;
    private String cdLevel2Name;
    private String fileName;
    private List<String> lstFileName;
    private String userReceiveTr;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date updateReceiveTr;

    private String groupCreated;
    private String groupCreatedName;
    private Long constructionId;
    private String createdDateStr;
    private String finishDateStr;
    //Huypq-20052021-start
    private String apWorkSrc;
    //Huy-end

    private Long tmbtTarget;
    private String tmbtTargetDetail;
    private String tmbtBranch;

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date dbTkdaDate;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date dbTtkdtDate;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date dbVtDate;
    private String createWoDomain;
    private String checkDBHT;
    private boolean filterAutoExpire;
    private String autoExpire;
    private String woCode;
    private String woTypeName;// ducpm23 add 080822
    
    public String getWoTypeName() {
		return woTypeName;
	}

	public void setWoTypeName(String woTypeName) {
		this.woTypeName = woTypeName;
	}

	public String getCreatedDateStr() {
        return createdDateStr;
    }

    public void setCreatedDateStr(String createdDateStr) {
        this.createdDateStr = createdDateStr;
    }

    public String getFinishDateStr() {
        return finishDateStr;
    }

    public void setFinishDateStr(String finishDateStr) {
        this.finishDateStr = finishDateStr;
    }

    public Double getQuantityValue() {
        return quantityValue;
    }

    public void setQuantityValue(Double quantityValue) {
        this.quantityValue = quantityValue;
    }

    public Long getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Long customerType) {
        this.customerType = customerType;
    }

    public Long getTrId() {
        return trId;
    }

    public void setTrId(Long trId) {
        this.trId = trId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getTrCode() {
        return trCode;
    }

    public void setTrCode(String trCode) {
        this.trCode = trCode;
    }

    public String getTrName() {
        return trName;
    }

    public void setTrName(String trName) {
        this.trName = trName;
    }

    public Long getTrTypeId() {
        return trTypeId;
    }

    public void setTrTypeId(Long trTypeId) {
        this.trTypeId = trTypeId;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getQoutaTime() {
        return qoutaTime;
    }

    public void setQoutaTime(Integer qoutaTime) {
        this.qoutaTime = qoutaTime;
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

    public String getCdLevel1() {
        return cdLevel1;
    }

    public void setCdLevel1(String cdLevel1) {
        this.cdLevel1 = cdLevel1;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserApproveTr() {
        return userApproveTr;
    }

    public void setUserApproveTr(String userApproveTr) {
        this.userApproveTr = userApproveTr;
    }

    public Date getUpdateApproveTr() {
        return updateApproveTr;
    }

    public void setUpdateApproveTr(Date updateApproveTr) {
        this.updateApproveTr = updateApproveTr;
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

    public String getUserReceiveTr() {
        return userReceiveTr;
    }

    public void setUserReceiveTr(String userReceiveTr) {
        this.userReceiveTr = userReceiveTr;
    }

    public Date getUpdateReceiveTr() {
        return updateReceiveTr;
    }

    public void setUpdateReceiveTr(Date updateReceiveTr) {
        this.updateReceiveTr = updateReceiveTr;
    }

    public List<String> getLstFileName() {
        return lstFileName;
    }

    public void setLstFileName(List<String> lstFileName) {
        this.lstFileName = lstFileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getGroupCreated() {
        return groupCreated;
    }

    public void setGroupCreated(String groupCreated) {
        this.groupCreated = groupCreated;
    }

    public String getGroupCreatedName() {
        return groupCreatedName;
    }

    public void setGroupCreatedName(String groupCreatedName) {
        this.groupCreatedName = groupCreatedName;
    }


    @Override
    public WoTrBO toModel() {
        WoTrBO woTrBo = new WoTrBO();
        woTrBo.setTrId(this.trId);
        woTrBo.setTrTypeId(this.trTypeId);
        woTrBo.setTrCode(this.trCode);
        woTrBo.setTrName(this.trName);
        woTrBo.setContractCode(this.contractCode);
        woTrBo.setProjectCode(this.projectCode);
        woTrBo.setUserCreated(this.userCreated);
        woTrBo.setCreatedDate(this.createdDate);
        woTrBo.setFinishDate(this.finishDate);
        woTrBo.setState(this.state);
        woTrBo.setQoutaTime(this.qoutaTime);
        woTrBo.setExecuteLat(this.executeLat);
        woTrBo.setExecuteLong(this.executeLong);
        woTrBo.setCdLevel1(this.getCdLevel1());
        woTrBo.setStatus(this.status);
        woTrBo.setConstructionCode(this.constructionCode);
        woTrBo.setStationCode(this.stationCode);
        woTrBo.setStartDate(this.startDate);
        woTrBo.setContractId(this.contractId);
        woTrBo.setQuantityValue(this.quantityValue);
        woTrBo.setCustomerType(this.customerType);
        woTrBo.setContractType(this.contractType);
        woTrBo.setProjectId(this.projectId);
        woTrBo.setCdLevel1Name(this.cdLevel1Name);
        woTrBo.setUserApproveTr(this.userApproveTr);
        woTrBo.setUpdateApproveTr(this.updateApproveTr);
        woTrBo.setCdLevel2(this.cdLevel2);
        woTrBo.setCdLevel2Name(this.cdLevel2Name);
        woTrBo.setUserReceiveTr(this.userReceiveTr);
        woTrBo.setUpdateReceiveTr(this.updateReceiveTr);
        woTrBo.setGroupCreated(this.groupCreated);
        woTrBo.setGroupCreatedName(this.groupCreatedName);
        woTrBo.setConstructionId(this.constructionId);
        woTrBo.setTmbtTarget(this.tmbtTarget);
        woTrBo.setTmbtTargetDetail(this.tmbtTargetDetail);
        woTrBo.setDbTkdaDate(this.dbTkdaDate);
        woTrBo.setDbTtkdtDate(this.dbTtkdtDate);
        woTrBo.setDbVtDate(this.dbVtDate);
        woTrBo.setAutoExpire(this.autoExpire);

        return woTrBo;
    }

    @Override
    public Long getFWModelId() {
        return null;
    }

    @Override
    public String catchName() {
        return null;
    }

    //additional info

    private String loggedInUser;
    private String assignedCd;
    private String trTypeName;
    private String constructionCode;
    private String constructionName;
    private Long catConstructionTypeId;

    private Long projectId;
    private String projectName;

    private Long contractId;
    private String contractName;

    private Long stationId;
    private String stationCode;
    private String stationName;
    private String stationSysGroup;
    private String userCreatedFullName;
    private String userCreatedEmail;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date createdDateFrom;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date createdDateTo;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date finishDateFrom;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date finishDateTo;

    private String trTypeCode;
    private Long aioContractId;
    private Long createTrDomainGroupId;
    private List<String> codeRange;
    private List<String> contractCodeRange;
    private List<String> projectCodeRange;
    private String createDoanhThuDomain;
    private String contractFilter;
    private List<CatWorkItemTypeDTO> catWorkItemTypeList;
    private String catWorkItemTypeListString;
    private List<WoDTO> inactiveWoList;

    public List<String> getProjectCodeRange() {
        return projectCodeRange;
    }

    public void setProjectCodeRange(List<String> projectCodeRange) {
        this.projectCodeRange = projectCodeRange;
    }

    public List<String> getContractCodeRange() {
        return contractCodeRange;
    }

    public void setContractCodeRange(List<String> contractCodeRange) {
        this.contractCodeRange = contractCodeRange;
    }

    public List<String> getCodeRange() {
        return codeRange;
    }

    public void setCodeRange(List<String> codeRange) {
        this.codeRange = codeRange;
    }

    public Long getCreateTrDomainGroupId() {
        return createTrDomainGroupId;
    }

    public void setCreateTrDomainGroupId(Long createTrDomainGroupId) {
        this.createTrDomainGroupId = createTrDomainGroupId;
    }

    public Long getAioContractId() {
        return aioContractId;
    }

    public void setAioContractId(Long aioContractId) {
        this.aioContractId = aioContractId;
    }

    public String getTrTypeCode() {
        return trTypeCode;
    }

    public void setTrTypeCode(String trTypeCode) {
        this.trTypeCode = trTypeCode;
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

    public String getUserCreatedFullName() {
        return userCreatedFullName;
    }

    public void setUserCreatedFullName(String userCreatedFullName) {
        this.userCreatedFullName = userCreatedFullName;
    }

    public String getUserCreatedEmail() {
        return userCreatedEmail;
    }

    public void setUserCreatedEmail(String userCreatedEmail) {
        this.userCreatedEmail = userCreatedEmail;
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

    public String getTrTypeName() {
        return trTypeName;
    }

    public void setTrTypeName(String trTypeName) {
        this.trTypeName = trTypeName;
    }

    public String getConstructionCode() {
        return constructionCode;
    }

    public void setConstructionCode(String constructionCode) {
        this.constructionCode = constructionCode;
    }

    public String getConstructionName() {
        return constructionName;
    }

    public void setConstructionName(String constructionName) {
        this.constructionName = constructionName;
    }

    public Long getConstructionId() {
        return constructionId;
    }

    public void setConstructionId(Long constructionId) {
        this.constructionId = constructionId;
    }

    public Long getCatConstructionTypeId() {
        return catConstructionTypeId;
    }

    public void setCatConstructionTypeId(Long catConstructionTypeId) {
        this.catConstructionTypeId = catConstructionTypeId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationSysGroup() {
        return stationSysGroup;
    }

    public void setStationSysGroup(String stationSysGroup) {
        this.stationSysGroup = stationSysGroup;
    }

    public String getCdLevel1Name() {
        return cdLevel1Name;
    }

    public void setCdLevel1Name(String cdLevel1Name) {
        this.cdLevel1Name = cdLevel1Name;
    }

    public Long getContractType() {
        return contractType;
    }

    public void setContractType(Long contractType) {
        this.contractType = contractType;
    }

    //HienLT56 start 16102020
    private List<Long> listTrTypeId;

    public List<Long> getListTrTypeId() {
        return listTrTypeId;
    }

    public void setListTrTypeId(List<Long> listTrTypeId) {
        this.listTrTypeId = listTrTypeId;
    }

    //HienLT56 end 16102020
    //HienLT56 start 01122020
    private List<String> listTrTypeCode;

    public List<String> getListTrTypeCode() {
        return listTrTypeCode;
    }

    public void setListTrTypeCode(List<String> listTrTypeCode) {
        this.listTrTypeCode = listTrTypeCode;
    }

    //HienLT56 end 01122020

    public String getCreateDoanhThuDomain() {
        return createDoanhThuDomain;
    }

    public void setCreateDoanhThuDomain(String createDoanhThuDomain) {
        this.createDoanhThuDomain = createDoanhThuDomain;
    }

    public String getContractFilter() {
        return contractFilter;
    }

    public void setContractFilter(String contractFilter) {
        this.contractFilter = contractFilter;
    }

    public List<CatWorkItemTypeDTO> getCatWorkItemTypeList() {
        return catWorkItemTypeList;
    }

    public void setCatWorkItemTypeList(List<CatWorkItemTypeDTO> catWorkItemTypeList) {
        this.catWorkItemTypeList = catWorkItemTypeList;
    }

    public String getCatWorkItemTypeListString() {
        return catWorkItemTypeListString;
    }

    public void setCatWorkItemTypeListString(String catWorkItemTypeListString) {
        this.catWorkItemTypeListString = catWorkItemTypeListString;
    }

    private boolean isCd;

    public boolean isCd() {
        return isCd;
    }

    public void setCd(boolean cd) {
        isCd = cd;
    }

    public List<WoDTO> getInactiveWoList() {
        return inactiveWoList;
    }

    public void setInactiveWoList(List<WoDTO> inactiveWoList) {
        this.inactiveWoList = inactiveWoList;
    }

    private Long constructionStatus;

    public Long getConstructionStatus() {
        return constructionStatus;
    }

    public void setConstructionStatus(Long constructionStatus) {
        this.constructionStatus = constructionStatus;
    }

    public Long getTmbtTarget() {
        return tmbtTarget;
    }

    public void setTmbtTarget(Long tmbtTarget) {
        this.tmbtTarget = tmbtTarget;
    }

	// Huypq-20052021-start
	public String getApWorkSrc() {
		return apWorkSrc;
	}

	public void setApWorkSrc(String apWorkSrc) {
		this.apWorkSrc = apWorkSrc;
	}
	// Huy-end

    private String createTrDomain;

    public String getCreateTrDomain() {
        return createTrDomain;
    }

    public void setCreateTrDomain(String createTrDomain) {
        this.createTrDomain = createTrDomain;
    }

    public String getTmbtBranch() {
        return tmbtBranch;
    }

    public void setTmbtBranch(String tmbtBranch) {
        this.tmbtBranch = tmbtBranch;
    }

    private List<CatStationDetailDTO> selectedStations;

    public List<CatStationDetailDTO> getSelectedStations() {
        return selectedStations;
    }

    public void setSelectedStations(List<CatStationDetailDTO> selectedStations) {
        this.selectedStations = selectedStations;
    }
    // Unikom_20210528_end

	public String getTmbtTargetDetail() {
		return tmbtTargetDetail;
	}

	public void setTmbtTargetDetail(String tmbtTargetDetail) {
		this.tmbtTargetDetail = tmbtTargetDetail;
	}

	public Date getDbTkdaDate() {
		return dbTkdaDate;
	}

	public void setDbTkdaDate(Date dbTkdaDate) {
		this.dbTkdaDate = dbTkdaDate;
	}

	public Date getDbTtkdtDate() {
		return dbTtkdtDate;
	}

	public void setDbTtkdtDate(Date dbTtkdtDate) {
		this.dbTtkdtDate = dbTtkdtDate;
	}

	public Date getDbVtDate() {
		return dbVtDate;
	}

	public void setDbVtDate(Date dbVtDate) {
		this.dbVtDate = dbVtDate;
	}

	public String getCreateWoDomain() {
		return createWoDomain;
	}

	public void setCreateWoDomain(String createWoDomain) {
		this.createWoDomain = createWoDomain;
	}

	public String getCheckDBHT() {
		return checkDBHT;
	}

	public void setCheckDBHT(String checkDBHT) {
		this.checkDBHT = checkDBHT;
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

	public String getWoCode() {
		return woCode;
	}

	public void setWoCode(String woCode) {
		this.woCode = woCode;
	}

}
