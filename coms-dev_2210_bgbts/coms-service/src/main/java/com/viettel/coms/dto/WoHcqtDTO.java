package com.viettel.coms.dto;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;

public class WoHcqtDTO extends WoDTO {

    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date dnqtDate;

    private String dnqtPerson;
    private Long dnqtPersonId;

    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date vtnetSendDate;

    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date vtnetConfirmDate;

    private String vtnetConfirmPerson;
    private Long vtnetConfirmPersonId;

    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date aprovedDocDate;
    private Double vtnetConfirmValue;
    private String aprovedPerson;

//    private String finishDateStr;
    private String hshcReceiveDateStr;
    private String dnqtDateStr;
    private String vtnetSendDateStr;
    private String vtnetConfirmDateStr;
    private String aprovedDocDateStr;

    private Long hasProblem;
    private String problemName;
    private String problemContent;

    public Date getDnqtDate() {
        return dnqtDate;
    }

    public void setDnqtDate(Date dnqtDate) {
        this.dnqtDate = dnqtDate;
    }

    public String getDnqtPerson() {
        return dnqtPerson;
    }

    public void setDnqtPerson(String dnqtPerson) {
        this.dnqtPerson = dnqtPerson;
    }

    public Long getDnqtPersonId() {
        return dnqtPersonId;
    }

    public void setDnqtPersonId(Long dnqtPersonId) {
        this.dnqtPersonId = dnqtPersonId;
    }

    public Date getVtnetSendDate() {
        return vtnetSendDate;
    }

    public void setVtnetSendDate(Date vtnetSendDate) {
        this.vtnetSendDate = vtnetSendDate;
    }

    public Date getVtnetConfirmDate() {
        return vtnetConfirmDate;
    }

    public void setVtnetConfirmDate(Date vtnetConfirmDate) {
        this.vtnetConfirmDate = vtnetConfirmDate;
    }

    public String getVtnetConfirmPerson() {
        return vtnetConfirmPerson;
    }

    public void setVtnetConfirmPerson(String vtnetConfirmPerson) {
        this.vtnetConfirmPerson = vtnetConfirmPerson;
    }

    public Long getVtnetConfirmPersonId() {
        return vtnetConfirmPersonId;
    }

    public void setVtnetConfirmPersonId(Long vtnetConfirmPersonId) {
        this.vtnetConfirmPersonId = vtnetConfirmPersonId;
    }

    public Date getAprovedDocDate() {
        return aprovedDocDate;
    }

    public void setAprovedDocDate(Date aprovedDocDate) {
        this.aprovedDocDate = aprovedDocDate;
    }

    public Double getVtnetConfirmValue() {
        return vtnetConfirmValue;
    }

    public void setVtnetConfirmValue(Double vtnetConfirmValue) {
        this.vtnetConfirmValue = vtnetConfirmValue;
    }

    public String getAprovedPerson() {
        return aprovedPerson;
    }

    public void setAprovedPerson(String aprovedPerson) {
        this.aprovedPerson = aprovedPerson;
    }

//    public String getFinishDateStr() {
//        return finishDateStr;
//    }
//
//    public void setFinishDateStr(String finishDateStr) {
//        this.finishDateStr = finishDateStr;
//    }

    public String getHshcReceiveDateStr() {
        return hshcReceiveDateStr;
    }

    public void setHshcReceiveDateStr(String hshcReceiveDateStr) {
        this.hshcReceiveDateStr = hshcReceiveDateStr;
    }

    public String getDnqtDateStr() {
        return dnqtDateStr;
    }

    public void setDnqtDateStr(String dnqtDateStr) {
        this.dnqtDateStr = dnqtDateStr;
    }

    public String getVtnetSendDateStr() {
        return vtnetSendDateStr;
    }

    public void setVtnetSendDateStr(String vtnetSendDateStr) {
        this.vtnetSendDateStr = vtnetSendDateStr;
    }

    public String getVtnetConfirmDateStr() {
        return vtnetConfirmDateStr;
    }

    public void setVtnetConfirmDateStr(String vtnetConfirmDateStr) {
        this.vtnetConfirmDateStr = vtnetConfirmDateStr;
    }

    public String getAprovedDocDateStr() {
        return aprovedDocDateStr;
    }

    public void setAprovedDocDateStr(String aprovedDocDateStr) {
        this.aprovedDocDateStr = aprovedDocDateStr;
    }

    public Long getHasProblem() {
        return hasProblem;
    }

    public void setHasProblem(Long hasProblem) {
        this.hasProblem = hasProblem;
    }

    public String getProblemName() {
        return problemName;
    }

    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }

    public String getProblemContent() {
        return problemContent;
    }

    public void setProblemContent(String problemContent) {
        this.problemContent = problemContent;
    }
}
