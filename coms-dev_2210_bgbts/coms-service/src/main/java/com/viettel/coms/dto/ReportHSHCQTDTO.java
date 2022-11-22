package com.viettel.coms.dto;

import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializer;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
public class ReportHSHCQTDTO extends YearPlanDetailDTO {
    private Long type;
    private String problemName;
    private String provinceCode;
    private Long quantityMoney;
    private Long quantityDnqt;
    private Long quantityVtnet;
    private Long quantityAproved;
    private Double moneyValue;
    private Double dnqtValue;
    private Double vtnetValue;
    private Double aprovedValue;
    private Long quantityMoneyPoblem;
    private Long quantityDnqtPoblem;
    private Long quantityVtnetPoblem;
    private Long quantityAprovedPoblem;
    private Double moneyValuePoblem;
    private Double dnqtValuePoblem;
    private Double vtnetValuePoblem;
    private Double aprovedValuePoblem;
    private Long quantityMoneyTotal;
    private Long quantityDnqtTotal;
    private Long quantityVtnetTotal;
    private Long quantityAprovedTotal;
    private Double moneyValueTotal;
    private Double dnqtValueTotal;
    private Double vtnetValueTotal;
    private Double aprovedValueTotal;
    private Double valueTotal;
    private Long quantityTotal;
    private String dateFrom;
    private String dateTo;
    private String dateFromDNQT;
    private String dateToDNQT;
    private String dateFromVTnet;
    private String dateToVTnet;
    private String dateFromInvestor;
    private String dateToInvestor;


    public Double getValueTotal() {
        return valueTotal;
    }

    public void setValueTotal(Double valueTotal) {
        this.valueTotal = valueTotal;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public Long getQuantityMoneyPoblem() {
        return quantityMoneyPoblem;
    }

    public void setQuantityMoneyPoblem(Long quantityMoneyPoblem) {
        this.quantityMoneyPoblem = quantityMoneyPoblem;
    }

    public Long getQuantityDnqtPoblem() {
        return quantityDnqtPoblem;
    }

    public void setQuantityDnqtPoblem(Long quantityDnqtPoblem) {
        this.quantityDnqtPoblem = quantityDnqtPoblem;
    }

    public Long getQuantityVtnetPoblem() {
        return quantityVtnetPoblem;
    }

    public void setQuantityVtnetPoblem(Long quantityVtnetPoblem) {
        this.quantityVtnetPoblem = quantityVtnetPoblem;
    }

    public Long getQuantityAprovedPoblem() {
        return quantityAprovedPoblem;
    }

    public void setQuantityAprovedPoblem(Long quantityAprovedPoblem) {
        this.quantityAprovedPoblem = quantityAprovedPoblem;
    }

    public Double getMoneyValuePoblem() {
        return moneyValuePoblem;
    }

    public void setMoneyValuePoblem(Double moneyValuePoblem) {
        this.moneyValuePoblem = moneyValuePoblem;
    }

    public Double getDnqtValuePoblem() {
        return dnqtValuePoblem;
    }

    public void setDnqtValuePoblem(Double dnqtValuePoblem) {
        this.dnqtValuePoblem = dnqtValuePoblem;
    }

    public Double getVtnetValuePoblem() {
        return vtnetValuePoblem;
    }

    public void setVtnetValuePoblem(Double vtnetValuePoblem) {
        this.vtnetValuePoblem = vtnetValuePoblem;
    }

    public Double getAprovedValuePoblem() {
        return aprovedValuePoblem;
    }

    public void setAprovedValuePoblem(Double aprovedValuePoblem) {
        this.aprovedValuePoblem = aprovedValuePoblem;
    }

    public Long getQuantityTotal() {
        return quantityTotal;
    }

    public void setQuantityTotal(Long quantityTotal) {
        this.quantityTotal = quantityTotal;
    }

    public String getProblemName() {
        return problemName;
    }

    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }

    public Long getQuantityMoney() {
        return quantityMoney;
    }

    public void setQuantityMoney(Long quantityMoney) {
        this.quantityMoney = quantityMoney;
    }

    public Long getQuantityDnqt() {
        return quantityDnqt;
    }

    public void setQuantityDnqt(Long quantityDnqt) {
        this.quantityDnqt = quantityDnqt;
    }

    public Long getQuantityVtnet() {
        return quantityVtnet;
    }

    public void setQuantityVtnet(Long quantityVtnet) {
        this.quantityVtnet = quantityVtnet;
    }

    public Long getQuantityAproved() {
        return quantityAproved;
    }

    public void setQuantityAproved(Long quantityAproved) {
        this.quantityAproved = quantityAproved;
    }

    public Double getMoneyValue() {
        return moneyValue;
    }

    public void setMoneyValue(Double moneyValue) {
        this.moneyValue = moneyValue;
    }

    public Double getDnqtValue() {
        return dnqtValue;
    }

    public void setDnqtValue(Double dnqtValue) {
        this.dnqtValue = dnqtValue;
    }

    public Double getVtnetValue() {
        return vtnetValue;
    }

    public void setVtnetValue(Double vtnetValue) {
        this.vtnetValue = vtnetValue;
    }

    public Double getAprovedValue() {
        return aprovedValue;
    }

    public void setAprovedValue(Double aprovedValue) {
        this.aprovedValue = aprovedValue;
    }

    public Long getQuantityMoneyTotal() {
        return quantityMoneyTotal;
    }

    public void setQuantityMoneyTotal(Long quantityMoneyTotal) {
        this.quantityMoneyTotal = quantityMoneyTotal;
    }

    public Long getQuantityDnqtTotal() {
        return quantityDnqtTotal;
    }

    public void setQuantityDnqtTotal(Long quantityDnqtTotal) {
        this.quantityDnqtTotal = quantityDnqtTotal;
    }

    public Long getQuantityVtnetTotal() {
        return quantityVtnetTotal;
    }

    public void setQuantityVtnetTotal(Long quantityVtnetTotal) {
        this.quantityVtnetTotal = quantityVtnetTotal;
    }

    public Long getQuantityAprovedTotal() {
        return quantityAprovedTotal;
    }

    public void setQuantityAprovedTotal(Long quantityAprovedTotal) {
        this.quantityAprovedTotal = quantityAprovedTotal;
    }

    public Double getMoneyValueTotal() {
        return moneyValueTotal;
    }

    public void setMoneyValueTotal(Double moneyValueTotal) {
        this.moneyValueTotal = moneyValueTotal;
    }

    public Double getDnqtValueTotal() {
        return dnqtValueTotal;
    }

    public void setDnqtValueTotal(Double dnqtValueTotal) {
        this.dnqtValueTotal = dnqtValueTotal;
    }

    public Double getVtnetValueTotal() {
        return vtnetValueTotal;
    }

    public void setVtnetValueTotal(Double vtnetValueTotal) {
        this.vtnetValueTotal = vtnetValueTotal;
    }

    public Double getAprovedValueTotal() {
        return aprovedValueTotal;
    }

    public void setAprovedValueTotal(Double aprovedValueTotal) {
        this.aprovedValueTotal = aprovedValueTotal;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
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
}
