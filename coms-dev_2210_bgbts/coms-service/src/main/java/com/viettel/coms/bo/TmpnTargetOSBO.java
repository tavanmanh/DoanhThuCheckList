package com.viettel.coms.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.TmpnTargetDTO;
import com.viettel.coms.dto.TmpnTargetOSDTO;
import com.viettel.service.base.model.BaseFWModelImpl;

@Entity
@Table(name = "TMPN_TARGET_OS")
/**
 *
 * @author: HoangNH38
 * @version: 1.0
 * @since: 1.0
 */
public class TmpnTargetOSBO extends BaseFWModelImpl{


    private java.lang.Long tmpnTargetId;
    private java.lang.Long totalMonthPlanId;
    private java.lang.Long sysGroupId;
    private java.lang.Long month;
    private java.lang.Long year;
    private java.lang.Long quantity;
    private java.lang.Long complete;
    private java.lang.Long revenue;
    //Huypq-08042020-start
    private Long completeTarget;
    private Long hshcXlTarget;
    private Long revenueCpTarget;
    private Long revenueNtdGpdnTarget;
    private Long revenueNtdXdddTarget;
    private Long hshcHtctTarget;
    private Long quantityXlTarget;
    private Long quantityCpTarget;
    private Long quantityNtdGpdnTarget;
    private Long quantityNtdXdddTarget;
    private Long taskXdddTarget;
    private Long revokeCashTarget;
    private Long sumDeployHtct;
    private Long buildCompleteHtct;
    private Long completeDbHtct;
    private Long rentHtct;
    
	//Huy-end
    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "TMPN_TARGET_OS_SEQ")})
    @Column(name = "TMPN_TARGET_OS_ID", length = 22)
    public java.lang.Long getTmpnTargetId() {
        return tmpnTargetId;
    }

    public void setTmpnTargetId(java.lang.Long tmpnTargetId) {
        this.tmpnTargetId = tmpnTargetId;
    }

    @Column(name = "TOTAL_MONTH_PLAN_OS_ID", length = 22)
    public java.lang.Long getTotalMonthPlanId() {
        return totalMonthPlanId;
    }

    public void setTotalMonthPlanId(java.lang.Long totalMonthPlanId) {
        this.totalMonthPlanId = totalMonthPlanId;
    }

    @Column(name = "SYS_GROUP_ID", length = 22)
    public java.lang.Long getSysGroupId() {
        return sysGroupId;
    }

    public void setSysGroupId(java.lang.Long sysGroupId) {
        this.sysGroupId = sysGroupId;
    }

    @Column(name = "MONTH", length = 22)
    public java.lang.Long getMonth() {
        return month;
    }

    public void setMonth(java.lang.Long month) {
        this.month = month;
    }

    @Column(name = "YEAR", length = 22)
    public java.lang.Long getYear() {
        return year;
    }

    public void setYear(java.lang.Long year) {
        this.year = year;
    }

    @Column(name = "QUANTITY", length = 22)
    public java.lang.Long getQuantity() {
        return quantity;
    }

    public void setQuantity(java.lang.Long quantity) {
        this.quantity = quantity;
    }

    @Column(name = "COMPLETE", length = 22)
    public java.lang.Long getComplete() {
        return complete;
    }

    public void setComplete(java.lang.Long complete) {
        this.complete = complete;
    }

    @Column(name = "REVENUE", length = 22)
    public java.lang.Long getRevenue() {
        return revenue;
    }

    public void setRevenue(java.lang.Long revenue) {
        this.revenue = revenue;
    }

    private double mil = 1000000;

    @Column(name = "COMPLETE_TARGET", length = 22)
    public Long getCompleteTarget() {
		return completeTarget;
	}

	public void setCompleteTarget(Long completeTarget) {
		this.completeTarget = completeTarget;
	}

	@Column(name = "HSHC_XL_TARGET", length = 22)
	public Long getHshcXlTarget() {
		return hshcXlTarget;
	}

	public void setHshcXlTarget(Long hshcXlTarget) {
		this.hshcXlTarget = hshcXlTarget;
	}

	@Column(name = "REVENUE_CP_TARGET", length = 22)
	public Long getRevenueCpTarget() {
		return revenueCpTarget;
	}

	public void setRevenueCpTarget(Long revenueCpTarget) {
		this.revenueCpTarget = revenueCpTarget;
	}

	@Column(name = "REVENUE_NTD_GPDN_TARGET", length = 22)
	public Long getRevenueNtdGpdnTarget() {
		return revenueNtdGpdnTarget;
	}

	public void setRevenueNtdGpdnTarget(Long revenueNtdGpdnTarget) {
		this.revenueNtdGpdnTarget = revenueNtdGpdnTarget;
	}

	@Column(name = "REVENUE_NTD_XDDD_TARGET", length = 22)
	public Long getRevenueNtdXdddTarget() {
		return revenueNtdXdddTarget;
	}

	public void setRevenueNtdXdddTarget(Long revenueNtdXdddTarget) {
		this.revenueNtdXdddTarget = revenueNtdXdddTarget;
	}

	@Column(name = "HSHC_HTCT_TARGET", length = 22)
	public Long getHshcHtctTarget() {
		return hshcHtctTarget;
	}

	public void setHshcHtctTarget(Long hshcHtctTarget) {
		this.hshcHtctTarget = hshcHtctTarget;
	}

	@Column(name = "QUANTITY_XL_TARGET", length = 22)
	public Long getQuantityXlTarget() {
		return quantityXlTarget;
	}

	public void setQuantityXlTarget(Long quantityXlTarget) {
		this.quantityXlTarget = quantityXlTarget;
	}

	@Column(name = "QUANTITY_CP_TARGET", length = 22)
	public Long getQuantityCpTarget() {
		return quantityCpTarget;
	}

	public void setQuantityCpTarget(Long quantityCpTarget) {
		this.quantityCpTarget = quantityCpTarget;
	}

	@Column(name = "QUANTITY_NTD_GPDN_TARGET", length = 22)
	public Long getQuantityNtdGpdnTarget() {
		return quantityNtdGpdnTarget;
	}

	public void setQuantityNtdGpdnTarget(Long quantityNtdGpdnTarget) {
		this.quantityNtdGpdnTarget = quantityNtdGpdnTarget;
	}

	@Column(name = "QUANTITY_NTD_XDDD_TARGET", length = 22)
	public Long getQuantityNtdXdddTarget() {
		return quantityNtdXdddTarget;
	}

	public void setQuantityNtdXdddTarget(Long quantityNtdXdddTarget) {
		this.quantityNtdXdddTarget = quantityNtdXdddTarget;
	}

	@Column(name = "TASK_XDDD_TARGET", length = 22)
	public Long getTaskXdddTarget() {
		return taskXdddTarget;
	}

	public void setTaskXdddTarget(Long taskXdddTarget) {
		this.taskXdddTarget = taskXdddTarget;
	}

	@Column(name = "REVOKE_CASH_TARGET", length = 22)
	public Long getRevokeCashTarget() {
		return revokeCashTarget;
	}

	public void setRevokeCashTarget(Long revokeCashTarget) {
		this.revokeCashTarget = revokeCashTarget;
	}

	@Column(name = "SUM_DEPLOY_HTCT", length = 22)
	public Long getSumDeployHtct() {
		return sumDeployHtct;
	}

	public void setSumDeployHtct(Long sumDeployHtct) {
		this.sumDeployHtct = sumDeployHtct;
	}

	@Column(name = "BUILD_COMPLETE_HTCT", length = 22)
	public Long getBuildCompleteHtct() {
		return buildCompleteHtct;
	}

	public void setBuildCompleteHtct(Long buildCompleteHtct) {
		this.buildCompleteHtct = buildCompleteHtct;
	}

	@Column(name = "COMPLETE_DB_HTCT", length = 22)
	public Long getCompleteDbHtct() {
		return completeDbHtct;
	}

	public void setCompleteDbHtct(Long completeDbHtct) {
		this.completeDbHtct = completeDbHtct;
	}
	
	@Column(name = "RENT_HTCT", length = 22)
	public Long getRentHtct() {
		return rentHtct;
	}

	public void setRentHtct(Long rentHtct) {
		this.rentHtct = rentHtct;
	}

	@Override
    public TmpnTargetOSDTO toDTO() {
        TmpnTargetOSDTO tmpnTargetOSDTO = new TmpnTargetOSDTO();
        // set cac gia tri
        tmpnTargetOSDTO.setTmpnTargetId(this.tmpnTargetId);
        tmpnTargetOSDTO.setTotalMonthPlanId(this.totalMonthPlanId);
        tmpnTargetOSDTO.setSysGroupId(this.sysGroupId);
        tmpnTargetOSDTO.setMonth(this.month);
        tmpnTargetOSDTO.setYear(this.year);
        tmpnTargetOSDTO.setQuantity(this.quantity != null ? this.quantity / mil : this.quantity);
        tmpnTargetOSDTO.setComplete(this.complete != null ? this.complete / mil : this.complete);
        tmpnTargetOSDTO.setRevenue(this.revenue != null ? this.revenue / mil : this.revenue);
        tmpnTargetOSDTO.setCompleteTarget(this.getCompleteTarget()!=null ? this.getCompleteTarget()/mil : 0);
        tmpnTargetOSDTO.setHshcXlTarget(this.getHshcXlTarget()!=null ? this.getHshcXlTarget()/mil : 0);
        tmpnTargetOSDTO.setRevenueCpTarget(this.getRevenueCpTarget()!=null ? this.getRevenueCpTarget()/mil : 0);
        tmpnTargetOSDTO.setRevenueNtdGpdnTarget(this.getRevenueNtdGpdnTarget()!=null ? this.getRevenueNtdGpdnTarget()/mil : 0);
        tmpnTargetOSDTO.setRevenueNtdXdddTarget(this.getRevenueNtdXdddTarget()!=null ? this.getRevenueNtdXdddTarget()/mil : 0);
        tmpnTargetOSDTO.setHshcHtctTarget(this.getHshcHtctTarget()!=null ? this.getHshcHtctTarget()/mil : 0);
        tmpnTargetOSDTO.setQuantityXlTarget(this.getQuantityXlTarget()!=null ? this.getQuantityXlTarget()/mil : 0);
        tmpnTargetOSDTO.setQuantityCpTarget(this.getQuantityCpTarget()!=null ? this.getQuantityCpTarget()/mil : 0);
        tmpnTargetOSDTO.setQuantityNtdGpdnTarget(this.getQuantityNtdGpdnTarget()!=null ? this.getQuantityNtdGpdnTarget()/mil : 0);
        tmpnTargetOSDTO.setQuantityNtdXdddTarget(this.getQuantityNtdXdddTarget()!=null ? this.getQuantityNtdXdddTarget()/mil : 0);
        tmpnTargetOSDTO.setTaskXdddTarget(this.getTaskXdddTarget()!=null ? this.getTaskXdddTarget()/mil : 0);
        tmpnTargetOSDTO.setRevokeCashTarget(this.getRevokeCashTarget()!=null ? this.getRevokeCashTarget()/mil : 0);
        tmpnTargetOSDTO.setSumDeployHtct(this.getSumDeployHtct());
        tmpnTargetOSDTO.setBuildCompleteHtct(this.getBuildCompleteHtct());
        tmpnTargetOSDTO.setCompleteDbHtct(this.getCompleteDbHtct());
        tmpnTargetOSDTO.setRentHtct(this.rentHtct);
        return tmpnTargetOSDTO;
    }

}
