package com.viettel.coms.dto;

import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

public class WoHcqtFtReportDTO {
    private Long ftId;
    private String ftName;
    private Long totalQuantityByPlan;
    private Double totalValueByPlan;
    private Long totalHasDnqtQuantity;
    private Double totalDnqtValue;
    private Long totalVtnetConfirmedQuantity;
    private Double totalVtnetConfirmedValue;
    private Long totalApprovedQuantity;
    private Double totalApprovedValue;
    private Long totalSendVtnetQuantity;
    private Double totalSendVtnetValue;

    private Long page;
    private Long pageSize;
    private Integer totalRecord;

    private String dateFrom;
    private String dateTo;
    private String dateFromDNQT;
    private String dateToDNQT;
    private String dateFromVTnet;
    private String dateToVTnet;
    private String dateFromInvestor;
    private String dateToInvestor;

    private Long quantityByPlanTotal;
    private Double valueByPlanTotal;
    private Long hasDnqtQuantityTotal;
    private Double dnqtValueTotal;
    private Long vtnetConfirmedQuantityTotal;
    private Double vtnetConfirmedValueTotal;
    private Long approvedQuantityTotal;
    private Double approvedValueTotal;
    private Long sendVtnetQuantityTotal;
    private Double sendVtnetValueTotal;
    private Double dnqtPercent;
    private Double vtnetPercent;
    private Double approvedPercent;

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

    public Long getTotalQuantityByPlan() {
        return totalQuantityByPlan;
    }

    public void setTotalQuantityByPlan(Long totalQuantityByPlan) {
        this.totalQuantityByPlan = totalQuantityByPlan;
    }

    public Double getTotalValueByPlan() {
        return totalValueByPlan;
    }

    public void setTotalValueByPlan(Double totalValueByPlan) {
        this.totalValueByPlan = totalValueByPlan;
    }

    public Long getTotalHasDnqtQuantity() {
        return totalHasDnqtQuantity;
    }

    public void setTotalHasDnqtQuantity(Long totalHasDnqtQuantity) {
        this.totalHasDnqtQuantity = totalHasDnqtQuantity;
    }

    public Double getTotalDnqtValue() {
        return totalDnqtValue;
    }

    public void setTotalDnqtValue(Double totalDnqtValue) {
        this.totalDnqtValue = totalDnqtValue;
    }

    public Long getTotalVtnetConfirmedQuantity() {
        return totalVtnetConfirmedQuantity;
    }

    public void setTotalVtnetConfirmedQuantity(Long totalVtnetConfirmedQuantity) {
        this.totalVtnetConfirmedQuantity = totalVtnetConfirmedQuantity;
    }

    public Double getTotalVtnetConfirmedValue() {
        return totalVtnetConfirmedValue;
    }

    public void setTotalVtnetConfirmedValue(Double totalVtnetConfirmedValue) {
        this.totalVtnetConfirmedValue = totalVtnetConfirmedValue;
    }

    public Long getTotalApprovedQuantity() {
        return totalApprovedQuantity;
    }

    public void setTotalApprovedQuantity(Long totalApprovedQuantity) {
        this.totalApprovedQuantity = totalApprovedQuantity;
    }

    public Double getTotalApprovedValue() {
        return totalApprovedValue;
    }

    public void setTotalApprovedValue(Double totalApprovedValue) {
        this.totalApprovedValue = totalApprovedValue;
    }

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }


    public Integer getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(Integer totalRecord) {
        this.totalRecord = totalRecord;
    }

    public Long getTotalSendVtnetQuantity() {
        return totalSendVtnetQuantity;
    }

    public void setTotalSendVtnetQuantity(Long totalSendVtnetQuantity) {
        this.totalSendVtnetQuantity = totalSendVtnetQuantity;
    }

    public Double getTotalSendVtnetValue() {
        return totalSendVtnetValue;
    }

    public void setTotalSendVtnetValue(Double totalSendVtnetValue) {
        this.totalSendVtnetValue = totalSendVtnetValue;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getDateFromDNQT() {
        return dateFromDNQT;
    }

    public void setDateFromDNQT(String dateFromDNQT) {
        this.dateFromDNQT = dateFromDNQT;
    }

    public String getDateToDNQT() {
        return dateToDNQT;
    }

    public void setDateToDNQT(String dateToDNQT) {
        this.dateToDNQT = dateToDNQT;
    }

    public String getDateFromVTnet() {
        return dateFromVTnet;
    }

    public void setDateFromVTnet(String dateFromVTnet) {
        this.dateFromVTnet = dateFromVTnet;
    }

    public String getDateToVTnet() {
        return dateToVTnet;
    }

    public void setDateToVTnet(String dateToVTnet) {
        this.dateToVTnet = dateToVTnet;
    }

    public String getDateFromInvestor() {
        return dateFromInvestor;
    }

    public void setDateFromInvestor(String dateFromInvestor) {
        this.dateFromInvestor = dateFromInvestor;
    }

    public String getDateToInvestor() {
        return dateToInvestor;
    }

    public void setDateToInvestor(String dateToInvestor) {
        this.dateToInvestor = dateToInvestor;
    }

    public Long getQuantityByPlanTotal() {
        return quantityByPlanTotal;
    }

    public void setQuantityByPlanTotal(Long quantityByPlanTotal) {
        this.quantityByPlanTotal = quantityByPlanTotal;
    }

    public Double getValueByPlanTotal() {
        return valueByPlanTotal;
    }

    public void setValueByPlanTotal(Double valueByPlanTotal) {
        this.valueByPlanTotal = valueByPlanTotal;
    }

    public Long getHasDnqtQuantityTotal() {
        return hasDnqtQuantityTotal;
    }

    public void setHasDnqtQuantityTotal(Long hasDnqtQuantityTotal) {
        this.hasDnqtQuantityTotal = hasDnqtQuantityTotal;
    }

    public Double getDnqtValueTotal() {
        return dnqtValueTotal;
    }

    public void setDnqtValueTotal(Double dnqtValueTotal) {
        this.dnqtValueTotal = dnqtValueTotal;
    }

    public Long getVtnetConfirmedQuantityTotal() {
        return vtnetConfirmedQuantityTotal;
    }

    public void setVtnetConfirmedQuantityTotal(Long vtnetConfirmedQuantityTotal) {
        this.vtnetConfirmedQuantityTotal = vtnetConfirmedQuantityTotal;
    }

    public Double getVtnetConfirmedValueTotal() {
        return vtnetConfirmedValueTotal;
    }

    public void setVtnetConfirmedValueTotal(Double vtnetConfirmedValueTotal) {
        this.vtnetConfirmedValueTotal = vtnetConfirmedValueTotal;
    }

    public Long getApprovedQuantityTotal() {
        return approvedQuantityTotal;
    }

    public void setApprovedQuantityTotal(Long approvedQuantityTotal) {
        this.approvedQuantityTotal = approvedQuantityTotal;
    }

    public Double getApprovedValueTotal() {
        return approvedValueTotal;
    }

    public void setApprovedValueTotal(Double approvedValueTotal) {
        this.approvedValueTotal = approvedValueTotal;
    }

    public Long getSendVtnetQuantityTotal() {
        return sendVtnetQuantityTotal;
    }

    public void setSendVtnetQuantityTotal(Long sendVtnetQuantityTotal) {
        this.sendVtnetQuantityTotal = sendVtnetQuantityTotal;
    }

    public Double getSendVtnetValueTotal() {
        return sendVtnetValueTotal;
    }

    public void setSendVtnetValueTotal(Double sendVtnetValueTotal) {
        this.sendVtnetValueTotal = sendVtnetValueTotal;
    }

    public Double getDnqtPercent() {
        return dnqtPercent;
    }

    public void setDnqtPercent(Double dnqtPercent) {
        this.dnqtPercent = dnqtPercent;
    }

    public Double getVtnetPercent() {
        return vtnetPercent;
    }

    public void setVtnetPercent(Double vtnetPercent) {
        this.vtnetPercent = vtnetPercent;
    }

    public Double getApprovedPercent() {
        return approvedPercent;
    }

    public void setApprovedPercent(Double approvedPercent) {
        this.approvedPercent = approvedPercent;
    }
}
