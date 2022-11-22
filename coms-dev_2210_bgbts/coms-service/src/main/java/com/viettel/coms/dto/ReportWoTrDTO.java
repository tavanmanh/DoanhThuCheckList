package com.viettel.coms.dto;

import java.util.List;

public class ReportWoTrDTO extends YearPlanDetailDTO {
    private String type;
    private String name;
    private String fromDate;
    private String toDate;
    private List<Long> listWoTypeId;
    private List<Long> listGroupId;
    private Long woTypeID;
    private Long  tthtApprove;
    private Long  tthtSystem;
    private Long  tthtAssignCd;
    private Long  tthtRejectCd;
    private Long  tthtTrWo;
    private Long  tthtNotTrWo;
    private Long  cnktApprove;
    private Long  cnktSystemWo;
    private Long  woAssignCd;
    private Long  woAcceptCd;
    private Long  woAcceptFt;
    private Long  woAcceptFtSystem;
    private Long  woAcceptFtWo;
    private Long  woAssignFt;
    private Long  woFinish;
    private Long  woCompleted;
    private Long  woNotCompleted;
    private Long  woNotFinish ;
    private Long  woNotFinishDate ;
    private Long  woFinishDayEx ;
    private Long groupCreated;
    private String status;
    private Double  tthtApprovePrecent;
    private Double  tthtSystemPrecent;
    private Double  tthtAssignCdPrecent;
    private Double  tthtRejectCdPrecent;
    private Double  tthtTrWoPrecent;
    private Double  tthtNotTrWoPrecent;
    private Double  cnktApprovePrecent;
    private Double  cnktSystemWoPrecent;
    private Double  woAssignCdPrecent;
    private Double  woAcceptCdPrecent;
    private Double  woAcceptFtPrecent;
    private Double  woAcceptFtSystemPrecent;
    private Double  woAcceptFtWoPrecent;
    private Double  woAssignFtPrecent;
    private Double  woFinishPrecent;
    private Double  woCompletedPrecent;
    private Double  woNotCompletedPrecent;
    private Double  woNotFinishPrecent ;
    private Double  woNotFinishDatePrecent ;
    private Double  woFinishDayExPrecent ;
    //Huypq-02122020-start
    private String sysGroupAssign;
    
    public String getSysGroupAssign() {
		return sysGroupAssign;
	}

	public void setSysGroupAssign(String sysGroupAssign) {
		this.sysGroupAssign = sysGroupAssign;
	}
    //Huy-end

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getGroupCreated() {
        return groupCreated;
    }

    public void setGroupCreated(Long groupCreated) {
        this.groupCreated = groupCreated;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public Long getWoTypeID() {
        return woTypeID;
    }

    public void setWoTypeID(Long woTypeID) {
        this.woTypeID = woTypeID;
    }

    public Long getTthtApprove() {
        return tthtApprove;
    }

    public void setTthtApprove(Long tthtApprove) {
        this.tthtApprove = tthtApprove;
    }

    public Long getTthtSystem() {
        return tthtSystem;
    }

    public void setTthtSystem(Long tthtSystem) {
        this.tthtSystem = tthtSystem;
    }

    public Long getTthtAssignCd() {
        return tthtAssignCd;
    }

    public void setTthtAssignCd(Long tthtAssignCd) {
        this.tthtAssignCd = tthtAssignCd;
    }

    public Long getTthtRejectCd() {
        return tthtRejectCd;
    }

    public void setTthtRejectCd(Long tthtRejectCd) {
        this.tthtRejectCd = tthtRejectCd;
    }

    public Long getTthtTrWo() {
        return tthtTrWo;
    }

    public void setTthtTrWo(Long tthtTrWo) {
        this.tthtTrWo = tthtTrWo;
    }

    public Long getTthtNotTrWo() {
        return tthtNotTrWo;
    }

    public void setTthtNotTrWo(Long tthtNotTrWo) {
        this.tthtNotTrWo = tthtNotTrWo;
    }

    public Long getCnktApprove() {
        return cnktApprove;
    }

    public void setCnktApprove(Long cnktApprove) {
        this.cnktApprove = cnktApprove;
    }

    public Long getCnktSystemWo() {
        return cnktSystemWo;
    }

    public void setCnktSystemWo(Long cnktSystemWo) {
        this.cnktSystemWo = cnktSystemWo;
    }

    public Long getWoAssignCd() {
        return woAssignCd;
    }

    public void setWoAssignCd(Long woAssignCd) {
        this.woAssignCd = woAssignCd;
    }

    public Long getWoAcceptCd() {
        return woAcceptCd;
    }

    public void setWoAcceptCd(Long woAcceptCd) {
        this.woAcceptCd = woAcceptCd;
    }

    public Long getWoAcceptFt() {
        return woAcceptFt;
    }

    public void setWoAcceptFt(Long woAcceptFt) {
        this.woAcceptFt = woAcceptFt;
    }

    public Long getWoAcceptFtSystem() {
        return woAcceptFtSystem;
    }

    public void setWoAcceptFtSystem(Long woAcceptFtSystem) {
        this.woAcceptFtSystem = woAcceptFtSystem;
    }

    public Long getWoAcceptFtWo() {
        return woAcceptFtWo;
    }

    public void setWoAcceptFtWo(Long woAcceptFtWo) {
        this.woAcceptFtWo = woAcceptFtWo;
    }

    public Long getWoAssignFt() {
        return woAssignFt;
    }

    public void setWoAssignFt(Long woAssignFt) {
        this.woAssignFt = woAssignFt;
    }

    public Long getWoFinish() {
        return woFinish;
    }

    public void setWoFinish(Long woFinish) {
        this.woFinish = woFinish;
    }

    public Long getWoCompleted() {
        return woCompleted;
    }

    public void setWoCompleted(Long woCompleted) {
        this.woCompleted = woCompleted;
    }

    public Long getWoNotCompleted() {
        return woNotCompleted;
    }

    public void setWoNotCompleted(Long woNotCompleted) {
        this.woNotCompleted = woNotCompleted;
    }

    public Long getWoNotFinish() {
        return woNotFinish;
    }

    public void setWoNotFinish(Long woNotFinish) {
        this.woNotFinish = woNotFinish;
    }

    public Long getWoNotFinishDate() {
        return woNotFinishDate;
    }

    public void setWoNotFinishDate(Long woNotFinishDate) {
        this.woNotFinishDate = woNotFinishDate;
    }

    public Long getWoFinishDayEx() {
        return woFinishDayEx;
    }

    public void setWoFinishDayEx(Long woFinishDayEx) {
        this.woFinishDayEx = woFinishDayEx;
    }

    public List<Long> getListWoTypeId() {
        return listWoTypeId;
    }

    public void setListWoTypeId(List<Long> listWoTypeId) {
        this.listWoTypeId = listWoTypeId;
    }

    public List<Long> getListGroupId() {
        return listGroupId;
    }

    public void setListGroupId(List<Long> listGroupId) {
        this.listGroupId = listGroupId;
    }

    public Double getCnktApprovePrecent() {
        return cnktApprovePrecent;
    }

    public void setCnktApprovePrecent(Double cnktApprovePrecent) {
        this.cnktApprovePrecent = cnktApprovePrecent;
    }

    public Double getCnktSystemWoPrecent() {
        return cnktSystemWoPrecent;
    }

    public void setCnktSystemWoPrecent(Double cnktSystemWoPrecent) {
        this.cnktSystemWoPrecent = cnktSystemWoPrecent;
    }

    public Double getWoAssignCdPrecent() {
        return woAssignCdPrecent;
    }

    public void setWoAssignCdPrecent(Double woAssignCdPrecent) {
        this.woAssignCdPrecent = woAssignCdPrecent;
    }

    public Double getWoAcceptCdPrecent() {
        return woAcceptCdPrecent;
    }

    public void setWoAcceptCdPrecent(Double woAcceptCdPrecent) {
        this.woAcceptCdPrecent = woAcceptCdPrecent;
    }

    public Double getWoAcceptFtPrecent() {
        return woAcceptFtPrecent;
    }

    public void setWoAcceptFtPrecent(Double woAcceptFtPrecent) {
        this.woAcceptFtPrecent = woAcceptFtPrecent;
    }

    public Double getWoAcceptFtSystemPrecent() {
        return woAcceptFtSystemPrecent;
    }

    public void setWoAcceptFtSystemPrecent(Double woAcceptFtSystemPrecent) {
        this.woAcceptFtSystemPrecent = woAcceptFtSystemPrecent;
    }

    public Double getWoAcceptFtWoPrecent() {
        return woAcceptFtWoPrecent;
    }

    public void setWoAcceptFtWoPrecent(Double woAcceptFtWoPrecent) {
        this.woAcceptFtWoPrecent = woAcceptFtWoPrecent;
    }

    public Double getWoAssignFtPrecent() {
        return woAssignFtPrecent;
    }

    public void setWoAssignFtPrecent(Double woAssignFtPrecent) {
        this.woAssignFtPrecent = woAssignFtPrecent;
    }

    public Double getWoFinishPrecent() {
        return woFinishPrecent;
    }

    public void setWoFinishPrecent(Double woFinishPrecent) {
        this.woFinishPrecent = woFinishPrecent;
    }

    public Double getWoCompletedPrecent() {
        return woCompletedPrecent;
    }

    public void setWoCompletedPrecent(Double woCompletedPrecent) {
        this.woCompletedPrecent = woCompletedPrecent;
    }

    public Double getWoNotCompletedPrecent() {
        return woNotCompletedPrecent;
    }

    public void setWoNotCompletedPrecent(Double woNotCompletedPrecent) {
        this.woNotCompletedPrecent = woNotCompletedPrecent;
    }

    public Double getWoNotFinishPrecent() {
        return woNotFinishPrecent;
    }

    public void setWoNotFinishPrecent(Double woNotFinishPrecent) {
        this.woNotFinishPrecent = woNotFinishPrecent;
    }

    public Double getWoNotFinishDatePrecent() {
        return woNotFinishDatePrecent;
    }

    public void setWoNotFinishDatePrecent(Double woNotFinishDatePrecent) {
        this.woNotFinishDatePrecent = woNotFinishDatePrecent;
    }

    public Double getWoFinishDayExPrecent() {
        return woFinishDayExPrecent;
    }

    public void setWoFinishDayExPrecent(Double woFinishDayExPrecent) {
        this.woFinishDayExPrecent = woFinishDayExPrecent;
    }

    public Double getTthtApprovePrecent() {
        return tthtApprovePrecent;
    }

    public void setTthtApprovePrecent(Double tthtApprovePrecent) {
        this.tthtApprovePrecent = tthtApprovePrecent;
    }

    public Double getTthtSystemPrecent() {
        return tthtSystemPrecent;
    }

    public void setTthtSystemPrecent(Double tthtSystemPrecent) {
        this.tthtSystemPrecent = tthtSystemPrecent;
    }

    public Double getTthtAssignCdPrecent() {
        return tthtAssignCdPrecent;
    }

    public void setTthtAssignCdPrecent(Double tthtAssignCdPrecent) {
        this.tthtAssignCdPrecent = tthtAssignCdPrecent;
    }

    public Double getTthtRejectCdPrecent() {
        return tthtRejectCdPrecent;
    }

    public void setTthtRejectCdPrecent(Double tthtRejectCdPrecent) {
        this.tthtRejectCdPrecent = tthtRejectCdPrecent;
    }

    public Double getTthtTrWoPrecent() {
        return tthtTrWoPrecent;
    }

    public void setTthtTrWoPrecent(Double tthtTrWoPrecent) {
        this.tthtTrWoPrecent = tthtTrWoPrecent;
    }

    public Double getTthtNotTrWoPrecent() {
        return tthtNotTrWoPrecent;
    }

    public void setTthtNotTrWoPrecent(Double tthtNotTrWoPrecent) {
        this.tthtNotTrWoPrecent = tthtNotTrWoPrecent;
    }
}
