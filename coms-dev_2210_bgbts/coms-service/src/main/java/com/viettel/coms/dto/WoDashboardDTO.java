package com.viettel.coms.dto;

public class WoDashboardDTO {

    // Thong tin dieu hanh
    private Long totalWo;
    private Long numWOReceiveOver;
    private Double percentReceiveOver;
    private Long numWoNotAssignFt;
    private Double percentNotAssignFt;
    private Long numWoNotVerifyComplete;
    private Double percentNotVerifyComplete;
    private Long numWoFtReceiveOver;
    private Double percentFtReceiveOver;
    private Long otherDh;

    private Long numWoOngoingInTerm;
    private Long numWoOngoingOverTerm;
    private Long numWoCompletedApproved;
    private Long numWoCompletedUnapproved;
    private Long otherCb;


    // Thong tin thuc hien
    private Long numAssignCd;
    private Long numAcceptCd;
    private Long numAssignFt;
    private Long numRejectFt;
    private Long countFtDone;

    public Long getTotalWo() {
        return totalWo;
    }

    public void setTotalWo(Long totalWo) {
        this.totalWo = totalWo;
    }

    public Long getNumWOReceiveOver() {
        return numWOReceiveOver;
    }

    public void setNumWOReceiveOver(Long numWOReceiveOver) {
        this.numWOReceiveOver = numWOReceiveOver;
    }

    public Long getNumWoNotAssignFt() {
        return numWoNotAssignFt;
    }

    public void setNumWoNotAssignFt(Long numWoNotAssignFt) {
        this.numWoNotAssignFt = numWoNotAssignFt;
    }

    public Long getNumWoNotVerifyComplete() {
        return numWoNotVerifyComplete;
    }

    public void setNumWoNotVerifyComplete(Long numWoNotVerifyComplete) {
        this.numWoNotVerifyComplete = numWoNotVerifyComplete;
    }

    public Long getNumWoFtReceiveOver() {
        return numWoFtReceiveOver;
    }

    public void setNumWoFtReceiveOver(Long numWoFtReceiveOver) {
        this.numWoFtReceiveOver = numWoFtReceiveOver;
    }

    public Long getNumWoOngoingInTerm() {
        return numWoOngoingInTerm;
    }

    public void setNumWoOngoingInTerm(Long numWoOngoingInTerm) {
        this.numWoOngoingInTerm = numWoOngoingInTerm;
    }

    public Long getNumWoOngoingOverTerm() {
        return numWoOngoingOverTerm;
    }

    public void setNumWoOngoingOverTerm(Long numWoOngoingOverTerm) {
        this.numWoOngoingOverTerm = numWoOngoingOverTerm;
    }

    public Long getNumWoCompletedApproved() {
        return numWoCompletedApproved;
    }

    public void setNumWoCompletedApproved(Long numWoCompletedApproved) {
        this.numWoCompletedApproved = numWoCompletedApproved;
    }

    public Long getNumWoCompletedUnapproved() {
        return numWoCompletedUnapproved;
    }

    public void setNumWoCompletedUnapproved(Long numWoCompletedUnapproved) {
        this.numWoCompletedUnapproved = numWoCompletedUnapproved;
    }

    public Double getPercentReceiveOver() {
        return percentReceiveOver;
    }

    public void setPercentReceiveOver(Double percentReceiveOver) {
        this.percentReceiveOver = percentReceiveOver;
    }

    public Double getPercentNotAssignFt() {
        return percentNotAssignFt;
    }

    public void setPercentNotAssignFt(Double percentNotAssignFt) {
        this.percentNotAssignFt = percentNotAssignFt;
    }

    public Double getPercentNotVerifyComplete() {
        return percentNotVerifyComplete;
    }

    public void setPercentNotVerifyComplete(Double percentNotVerifyComplete) {
        this.percentNotVerifyComplete = percentNotVerifyComplete;
    }

    public Double getPercentFtReceiveOver() {
        return percentFtReceiveOver;
    }

    public void setPercentFtReceiveOver(Double percentFtReceiveOver) {
        this.percentFtReceiveOver = percentFtReceiveOver;
    }

    public Long getNumAssignCd() {
        return numAssignCd;
    }

    public void setNumAssignCd(Long numAssignCd) {
        this.numAssignCd = numAssignCd;
    }

    public Long getNumAcceptCd() {
        return numAcceptCd;
    }

    public void setNumAcceptCd(Long numAcceptCd) {
        this.numAcceptCd = numAcceptCd;
    }

    public Long getNumAssignFt() {
        return numAssignFt;
    }

    public void setNumAssignFt(Long numAssignFt) {
        this.numAssignFt = numAssignFt;
    }

    public Long getNumRejectFt() {
        return numRejectFt;
    }

    public void setNumRejectFt(Long numRejectFt) {
        this.numRejectFt = numRejectFt;
    }

    public Long getCountFtDone() {
        return countFtDone;
    }

    public void setCountFtDone(Long countFtDone) {
        this.countFtDone = countFtDone;
    }

    public Long getOtherDh() {
        return otherDh;
    }

    public void setOtherDh(Long otherDh) {
        this.otherDh = otherDh;
    }

    public Long getOtherCb() {
        return otherCb;
    }

    public void setOtherCb(Long otherCb) {
        this.otherCb = otherCb;
    }
}
