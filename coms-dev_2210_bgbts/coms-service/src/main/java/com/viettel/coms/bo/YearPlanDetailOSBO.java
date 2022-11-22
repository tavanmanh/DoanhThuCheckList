package com.viettel.coms.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.YearPlanDetailOSDTO;
import com.viettel.service.base.model.BaseFWModelImpl;

/**
 * @author HoangNH38
 */
@Entity
@Table(name = "YEAR_PLAN_DETAIL_OS")
public class YearPlanDetailOSBO extends BaseFWModelImpl{

    private java.lang.Long sysGroupId;
    private java.lang.Double source;
    private java.lang.Double quantity;
    private java.lang.Double complete;
    private java.lang.Double revenue;
    private java.lang.Long yearPlanId;
    private java.lang.Long yearPlanDetailId;
    private java.lang.Long month;
    private java.lang.Long year;

    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "YEAR_PLAN_DETAIL_OS_SEQ")})
    @Column(name = "YEAR_PLAN_DETAIL_OS_ID", length = 10)
    public java.lang.Long getYearPlanDetailId() {
        return yearPlanDetailId;
    }

    public void setYearPlanDetailId(java.lang.Long yearPlanDetailId) {
        this.yearPlanDetailId = yearPlanDetailId;
    }

    @Column(name = "SYS_GROUP_ID", length = 22)
    public java.lang.Long getSysGroupId() {
        return sysGroupId;
    }

    public void setSysGroupId(java.lang.Long sysGroupId) {
        this.sysGroupId = sysGroupId;
    }

    @Column(name = "SOURCE", length = 20)
    public java.lang.Double getSource() {
        return source;
    }

    public void setSource(java.lang.Double source) {
        this.source = source;
    }

    @Column(name = "QUANTITY", length = 20)
    public java.lang.Double getQuantity() {
        return quantity;
    }

    public void setQuantity(java.lang.Double quantity) {
        this.quantity = quantity;
    }

    @Column(name = "COMPLETE", length = 20)
    public java.lang.Double getComplete() {
        return complete;
    }

    public void setComplete(java.lang.Double complete) {
        this.complete = complete;
    }

    @Column(name = "REVENUE", length = 20)
    public java.lang.Double getRevenue() {
        return revenue;
    }

    public void setRevenue(java.lang.Double revenue) {
        this.revenue = revenue;
    }

    @Column(name = "YEAR_PLAN_OS_ID", length = 22)
    public java.lang.Long getYearPlanId() {
        return yearPlanId;
    }

    public void setYearPlanId(java.lang.Long yearPlanId) {
        this.yearPlanId = yearPlanId;
    }

    @Column(name = "MONTH", length = 2)
    public java.lang.Long getMonth() {
        return month;
    }

    public void setMonth(java.lang.Long month) {
        this.month = month;
    }

    @Column(name = "YEAR", length = 10)
    public java.lang.Long getYear() {
        return year;
    }

    public void setYear(java.lang.Long year) {
        this.year = year;
    }

    @Override
    public YearPlanDetailOSDTO toDTO() {
    	YearPlanDetailOSDTO yearPlanDetailOSDTO = new YearPlanDetailOSDTO();
        // set cac gia tri
        yearPlanDetailOSDTO.setSysGroupId(this.sysGroupId);
        yearPlanDetailOSDTO.setSource((double) this.source);
        yearPlanDetailOSDTO.setQuantity((double) this.quantity);
        yearPlanDetailOSDTO.setComplete((double) this.complete);
        yearPlanDetailOSDTO.setRevenue((double) this.revenue);
        yearPlanDetailOSDTO.setYearPlanId(this.yearPlanId);
        yearPlanDetailOSDTO.setMonth(month);
        yearPlanDetailOSDTO.setYear(year);
        return yearPlanDetailOSDTO;
    }
}
