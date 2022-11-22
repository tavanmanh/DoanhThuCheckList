package com.viettel.coms.dto;

import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializer;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.List;

public class WoGeneralReportDTO {
    private Long marketId;
    private String marketName;
    private Long areaId;
    private String areaName;
    private Long sectionId;
    private String sectionName;
    private Long ftId;
    private String ftName;

    private Long totalWO;
    private Long totalUnassign;
    private Long totalAssignCd;
    private Long totalAcceptCd;
    private Long totalRejectCd;
    private Long totalAssignFt;
    private Long totalAcceptFt;
    private Long totalRejectFt;
    private Long totalProcessing;
    private Long totalDone;
    private Long totalCdOk;
    private Long totalCdNotGood;
    private Long totalOk;
    private Long totalNotGood;
    private Long totalOpinionRequest;
    private Long totalOverDue;
    private Long totalFinishOverDue;

    private Long totalAllWO;
    private Long totalAllUnassign;
    private Long totalAllAssignCd;
    private Long totalAllAcceptCd;
    private Long totalAllRejectCd;
    private Long totalAllAssignFt;
    private Long totalAllAcceptFt;
    private Long totalAllRejectFt;
    private Long totalAllProcessing;
    private Long totalAllDone;
    private Long totalAllCdOk;
    private Long totalAllCdNotGood;
    private Long totalAllOk;
    private Long totalAllNotGood;
    private Long totalAllOpinionRequest;
    private Long totalAllOverDue;
    private Long totalAllTotalFinishOverDue;

    private Long totalTR;
    private Long totalTrOk;
    private Long totalTrOpinionRq;
    private Long totalTrProcessing;
    private Long totalTrAssignCd;
    private Long totalTrAssignFt;
    private Long totalTrNotOk;
    private Long totalTrDone;
    private Long totalTrRejectCd;
    private Long totalTrAcceptCd;

    private Integer totalRecord;
    private int byLowerCd = 0;
    private int byFt = 0;

    private List<String> geoAreaList;

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date fromDate;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date toDate;

    private Long page;
    private Integer pageSize;

    private String geoArea;
    private String type;

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
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

    public Long getTotalWO() {
        return totalWO;
    }

    public void setTotalWO(Long totalWO) {
        this.totalWO = totalWO;
    }

    public Long getTotalUnassign() {
        return totalUnassign;
    }

    public void setTotalUnassign(Long totalUnassign) {
        this.totalUnassign = totalUnassign;
    }

    public Long getTotalAssignCd() {
        return totalAssignCd;
    }

    public void setTotalAssignCd(Long totalAssignCd) {
        this.totalAssignCd = totalAssignCd;
    }

    public Long getTotalAcceptCd() {
        return totalAcceptCd;
    }

    public void setTotalAcceptCd(Long totalAcceptCd) {
        this.totalAcceptCd = totalAcceptCd;
    }

    public Long getTotalRejectCd() {
        return totalRejectCd;
    }

    public void setTotalRejectCd(Long totalRejectCd) {
        this.totalRejectCd = totalRejectCd;
    }

    public Long getTotalAssignFt() {
        return totalAssignFt;
    }

    public void setTotalAssignFt(Long totalAssignFt) {
        this.totalAssignFt = totalAssignFt;
    }

    public Long getTotalAcceptFt() {
        return totalAcceptFt;
    }

    public void setTotalAcceptFt(Long totalAcceptFt) {
        this.totalAcceptFt = totalAcceptFt;
    }

    public Long getTotalRejectFt() {
        return totalRejectFt;
    }

    public void setTotalRejectFt(Long totalRejectFt) {
        this.totalRejectFt = totalRejectFt;
    }

    public Long getTotalProcessing() {
        return totalProcessing;
    }

    public void setTotalProcessing(Long totalProcessing) {
        this.totalProcessing = totalProcessing;
    }

    public Long getTotalDone() {
        return totalDone;
    }

    public void setTotalDone(Long totalDone) {
        this.totalDone = totalDone;
    }

    public Long getTotalOk() {
        return totalOk;
    }

    public void setTotalOk(Long totalOk) {
        this.totalOk = totalOk;
    }

    public Long getTotalNotGood() {
        return totalNotGood;
    }

    public void setTotalNotGood(Long totalNotGood) {
        this.totalNotGood = totalNotGood;
    }

    public Long getTotalOpinionRequest() {
        return totalOpinionRequest;
    }

    public void setTotalOpinionRequest(Long totalOpinionRequest) {
        this.totalOpinionRequest = totalOpinionRequest;
    }

    public Long getTotalOverDue() {
        return totalOverDue;
    }

    public void setTotalOverDue(Long totalOverDue) {
        this.totalOverDue = totalOverDue;
    }

    public Long getTotalAllWO() {
        return totalAllWO;
    }

    public void setTotalAllWO(Long totalAllWO) {
        this.totalAllWO = totalAllWO;
    }

    public Long getTotalAllUnassign() {
        return totalAllUnassign;
    }

    public void setTotalAllUnassign(Long totalAllUnassign) {
        this.totalAllUnassign = totalAllUnassign;
    }

    public Long getTotalAllAssignCd() {
        return totalAllAssignCd;
    }

    public void setTotalAllAssignCd(Long totalAllAssignCd) {
        this.totalAllAssignCd = totalAllAssignCd;
    }

    public Long getTotalAllAcceptCd() {
        return totalAllAcceptCd;
    }

    public void setTotalAllAcceptCd(Long totalAllAcceptCd) {
        this.totalAllAcceptCd = totalAllAcceptCd;
    }

    public Long getTotalAllRejectCd() {
        return totalAllRejectCd;
    }

    public void setTotalAllRejectCd(Long totalAllRejectCd) {
        this.totalAllRejectCd = totalAllRejectCd;
    }

    public Long getTotalAllAssignFt() {
        return totalAllAssignFt;
    }

    public void setTotalAllAssignFt(Long totalAllAssignFt) {
        this.totalAllAssignFt = totalAllAssignFt;
    }

    public Long getTotalAllAcceptFt() {
        return totalAllAcceptFt;
    }

    public void setTotalAllAcceptFt(Long totalAllAcceptFt) {
        this.totalAllAcceptFt = totalAllAcceptFt;
    }

    public Long getTotalAllRejectFt() {
        return totalAllRejectFt;
    }

    public void setTotalAllRejectFt(Long totalAllRejectFt) {
        this.totalAllRejectFt = totalAllRejectFt;
    }

    public Long getTotalAllProcessing() {
        return totalAllProcessing;
    }

    public void setTotalAllProcessing(Long totalAllProcessing) {
        this.totalAllProcessing = totalAllProcessing;
    }

    public Long getTotalAllDone() {
        return totalAllDone;
    }

    public void setTotalAllDone(Long totalAllDone) {
        this.totalAllDone = totalAllDone;
    }

    public Long getTotalAllOk() {
        return totalAllOk;
    }

    public void setTotalAllOk(Long totalAllOk) {
        this.totalAllOk = totalAllOk;
    }

    public Long getTotalAllNotGood() {
        return totalAllNotGood;
    }

    public void setTotalAllNotGood(Long totalAllNotGood) {
        this.totalAllNotGood = totalAllNotGood;
    }

    public Long getTotalAllOpinionRequest() {
        return totalAllOpinionRequest;
    }

    public void setTotalAllOpinionRequest(Long totalAllOpinionRequest) {
        this.totalAllOpinionRequest = totalAllOpinionRequest;
    }

    public Long getTotalAllOverDue() {
        return totalAllOverDue;
    }

    public void setTotalAllOverDue(Long totalAllOverDue) {
        this.totalAllOverDue = totalAllOverDue;
    }

    public Integer getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(Integer totalRecord) {
        this.totalRecord = totalRecord;
    }

    public int getByLowerCd() {
        return byLowerCd;
    }

    public void setByLowerCd(int byLowerCd) {
        this.byLowerCd = byLowerCd;
    }

    public int getByFt() {
        return byFt;
    }

    public void setByFt(int byFt) {
        this.byFt = byFt;
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

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getGeoArea() {
        return geoArea;
    }

    public void setGeoArea(String geoArea) {
        this.geoArea = geoArea;
    }

    public Long getTotalCdOk() {
        return totalCdOk;
    }

    public void setTotalCdOk(Long totalCdOk) {
        this.totalCdOk = totalCdOk;
    }

    public Long getTotalCdNotGood() {
        return totalCdNotGood;
    }

    public void setTotalCdNotGood(Long totalCdNotGood) {
        this.totalCdNotGood = totalCdNotGood;
    }

    public Long getTotalAllCdOk() {
        return totalAllCdOk;
    }

    public void setTotalAllCdOk(Long totalAllCdOk) {
        this.totalAllCdOk = totalAllCdOk;
    }

    public Long getTotalAllCdNotGood() {
        return totalAllCdNotGood;
    }

    public void setTotalAllCdNotGood(Long totalAllCdNotGood) {
        this.totalAllCdNotGood = totalAllCdNotGood;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTotalFinishOverDue() {
        return totalFinishOverDue;
    }

    public void setTotalFinishOverDue(Long totalFinishOverDue) {
        this.totalFinishOverDue = totalFinishOverDue;
    }

    public Long getTotalAllTotalFinishOverDue() {
        return totalAllTotalFinishOverDue;
    }

    public void setTotalAllTotalFinishOverDue(Long totalAllTotalFinishOverDue) {
        this.totalAllTotalFinishOverDue = totalAllTotalFinishOverDue;
    }

    public List<String> getGeoAreaList() {
        return geoAreaList;
    }

    public void setGeoAreaList(List<String> geoAreaList) {
        this.geoAreaList = geoAreaList;
    }

    public Long getTotalTR() {
        return totalTR;
    }

    public void setTotalTR(Long totalTR) {
        this.totalTR = totalTR;
    }

    public Long getTotalTrOk() {
        return totalTrOk;
    }

    public void setTotalTrOk(Long totalTrOk) {
        this.totalTrOk = totalTrOk;
    }

    public Long getTotalTrOpinionRq() {
        return totalTrOpinionRq;
    }

    public void setTotalTrOpinionRq(Long totalTrOpinionRq) {
        this.totalTrOpinionRq = totalTrOpinionRq;
    }

    public Long getTotalTrProcessing() {
        return totalTrProcessing;
    }

    public void setTotalTrProcessing(Long totalTrProcessing) {
        this.totalTrProcessing = totalTrProcessing;
    }

    public Long getTotalTrAssignCd() {
        return totalTrAssignCd;
    }

    public void setTotalTrAssignCd(Long totalTrAssignCd) {
        this.totalTrAssignCd = totalTrAssignCd;
    }

    public Long getTotalTrAssignFt() {
        return totalTrAssignFt;
    }

    public void setTotalTrAssignFt(Long totalTrAssignFt) {
        this.totalTrAssignFt = totalTrAssignFt;
    }

    public Long getTotalTrNotOk() {
        return totalTrNotOk;
    }

    public void setTotalTrNotOk(Long totalTrNotOk) {
        this.totalTrNotOk = totalTrNotOk;
    }

    public Long getTotalTrDone() {
        return totalTrDone;
    }

    public void setTotalTrDone(Long totalTrDone) {
        this.totalTrDone = totalTrDone;
    }

    public Long getTotalTrRejectCd() {
        return totalTrRejectCd;
    }

    public void setTotalTrRejectCd(Long totalTrRejectCd) {
        this.totalTrRejectCd = totalTrRejectCd;
    }

    public Long getTotalTrAcceptCd() {
        return totalTrAcceptCd;
    }

    public void setTotalTrAcceptCd(Long totalTrAcceptCd) {
        this.totalTrAcceptCd = totalTrAcceptCd;
    }
}
