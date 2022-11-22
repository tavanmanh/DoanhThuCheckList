package com.viettel.coms.dto;

import java.util.List;

import com.viettel.coms.bo.TmpnTargetOSBO;

public class TmpnTargetOSDTO extends ComsBaseFWDTO<TmpnTargetOSBO>{

    private java.lang.Long tmpnTargetId;
    private java.lang.Long totalMonthPlanId;
    private java.lang.Long sysGroupId;
    private java.lang.Long month;
    private java.lang.Long year;
    private java.lang.Double quantity;
    private java.lang.Double complete;
    private java.lang.Double revenue;
    private Double quantityInYear;
    private Double completeInYear;
    private Double revenueInYear;
    private Double quantityLk;
    private Double completeLk;
    private Double revenueLk;
    private double mil = 1000000;
    private String sysGroupName;
    private String errorFilePath;
    private String quantityPercen;
    private String completePercen;
    private List<Long> sysGroupIdList;
    private Long rentHtct;

	public Long getRentHtct() {
		return rentHtct;
	}

	public void setRentHtct(Long rentHtct) {
		this.rentHtct = rentHtct;
	}

	@Override
    public TmpnTargetOSBO toModel() {
    	TmpnTargetOSBO tmpnTargetOSBO = new TmpnTargetOSBO();
        tmpnTargetOSBO.setTmpnTargetId(this.tmpnTargetId);
        tmpnTargetOSBO.setTotalMonthPlanId(this.totalMonthPlanId);
        tmpnTargetOSBO.setSysGroupId(this.sysGroupId);
        tmpnTargetOSBO.setMonth(this.month);
        tmpnTargetOSBO.setYear(this.year);
        tmpnTargetOSBO.setQuantity((long) (this.quantity != null ? this.quantity * mil : 0));
        tmpnTargetOSBO.setComplete((long) (this.complete != null ? this.complete * mil : 0));
        tmpnTargetOSBO.setRevenue((long) (this.revenue != null ? this.revenue * mil : 0));
        tmpnTargetOSBO.setCompleteTarget((long) (this.getCompleteTarget()!=null ? this.getCompleteTarget() * mil : 0));
        tmpnTargetOSBO.setHshcXlTarget((long) (this.getHshcXlTarget()!=null ? this.getHshcXlTarget() * mil : 0));
        tmpnTargetOSBO.setRevenueCpTarget((long) (this.getRevenueCpTarget()!=null ? this.getRevenueCpTarget() * mil : 0));
        tmpnTargetOSBO.setRevenueNtdGpdnTarget((long) (this.getRevenueNtdGpdnTarget()!=null ? this.getRevenueNtdGpdnTarget() * mil : 0));
        tmpnTargetOSBO.setRevenueNtdXdddTarget((long) (this.getRevenueNtdXdddTarget()!=null ? this.getRevenueNtdXdddTarget() * mil : 0));
        tmpnTargetOSBO.setHshcHtctTarget((long) (this.getHshcHtctTarget()!=null ? this.getHshcHtctTarget() * mil : 0));
        tmpnTargetOSBO.setQuantityXlTarget((long) (this.getQuantityXlTarget()!=null ? this.getQuantityXlTarget() * mil : 0));
        tmpnTargetOSBO.setQuantityCpTarget((long) (this.getQuantityCpTarget()!=null ? this.getQuantityCpTarget() * mil : 0));
        tmpnTargetOSBO.setQuantityNtdGpdnTarget((long) (this.getQuantityNtdGpdnTarget()!=null ? this.getQuantityNtdGpdnTarget() * mil : 0));
        tmpnTargetOSBO.setQuantityNtdXdddTarget((long) (this.getQuantityNtdXdddTarget()!=null ? this.getQuantityNtdXdddTarget() * mil : 0));
        tmpnTargetOSBO.setTaskXdddTarget((long) (this.getTaskXdddTarget()!=null ? this.getTaskXdddTarget() * mil : 0));
        tmpnTargetOSBO.setRevokeCashTarget((long) (this.getRevokeCashTarget()!=null ? this.getRevokeCashTarget() * mil : 0));
        tmpnTargetOSBO.setSumDeployHtct(this.getSumDeployHtct());
        tmpnTargetOSBO.setBuildCompleteHtct(this.getBuildCompleteHtct());
        tmpnTargetOSBO.setCompleteDbHtct(this.getCompleteDbHtct());
        tmpnTargetOSBO.setRentHtct(this.rentHtct);
        return tmpnTargetOSBO;
    }

    public List<Long> getSysGroupIdList() {
        return sysGroupIdList;
    }

    public void setSysGroupIdList(List<Long> sysGroupIdList) {
        this.sysGroupIdList = sysGroupIdList;
    }

    public String getQuantityPercen() {
        return quantityPercen;
    }

    public void setQuantityPercen(String quantityPercen) {
        this.quantityPercen = quantityPercen;
    }

    public String getCompletePercen() {
        return completePercen;
    }

    public void setCompletePercen(String completePercen) {
        this.completePercen = completePercen;
    }

    public String getErrorFilePath() {
        return errorFilePath;
    }

    public void setErrorFilePath(String errorFilePath) {
        this.errorFilePath = errorFilePath;
    }

    public String getSysGroupName() {
        return sysGroupName;
    }

    public void setSysGroupName(String sysGroupName) {
        this.sysGroupName = sysGroupName;
    }

    public Double getQuantityInYear() {
        return quantityInYear != null ? quantityInYear / mil : 0;
    }

    public void setQuantityInYear(Double quantityInYear) {
        this.quantityInYear = quantityInYear;
    }

    public Double getCompleteInYear() {
        return completeInYear != null ? completeInYear / mil : 0;
    }

    public void setCompleteInYear(Double completeInYear) {
        this.completeInYear = completeInYear;
    }

    public Double getRevenueInYear() {
        return revenueInYear != null ? revenueInYear / mil : 0;
    }

    public void setRevenueInYear(Double revenueInYear) {
        this.revenueInYear = revenueInYear;
    }

    public Double getQuantityLk() {
        return quantityLk != null ? quantityLk / mil : 0;
    }

    public void setQuantityLk(Double quantityLk) {
        this.quantityLk = quantityLk;
    }

    public Double getCompleteLk() {
        return completeLk != null ? completeLk / mil : 0;
    }

    public void setCompleteLk(Double completeLk) {
        this.completeLk = completeLk;
    }

    public Double getRevenueLk() {
        return revenueLk != null ? revenueLk / mil : 0;
    }

    public void setRevenueLk(Double revenueLk) {
        this.revenueLk = revenueLk;
    }

    @Override
    public Long getFWModelId() {
        return tmpnTargetId;
    }

    @Override
    public String catchName() {
        return getTmpnTargetId().toString();
    }

    public java.lang.Long getTmpnTargetId() {
        return tmpnTargetId;
    }

    public void setTmpnTargetId(java.lang.Long tmpnTargetId) {
        this.tmpnTargetId = tmpnTargetId;
    }

    public java.lang.Long getTotalMonthPlanId() {
        return totalMonthPlanId;
    }

    public void setTotalMonthPlanId(java.lang.Long totalMonthPlanId) {
        this.totalMonthPlanId = totalMonthPlanId;
    }

    public java.lang.Long getSysGroupId() {
        return sysGroupId;
    }

    public void setSysGroupId(java.lang.Long sysGroupId) {
        this.sysGroupId = sysGroupId;
    }

    public java.lang.Long getMonth() {
        return month;
    }

    public void setMonth(java.lang.Long month) {
        this.month = month;
    }

    public java.lang.Long getYear() {
        return year;
    }

    public void setYear(java.lang.Long year) {
        this.year = year;
    }

    public java.lang.Double getQuantity() {
        return quantity != null ? quantity / mil : 0;
    }

    public void setQuantity(java.lang.Double quantity) {
        this.quantity = quantity;
    }

    public java.lang.Double getComplete() {
        return complete != null ? complete / mil : 0;
    }

    public void setComplete(java.lang.Double complete) {
        this.complete = complete;
    }

    public java.lang.Double getRevenue() {
        return revenue != null ? revenue / mil : 0;
    }

    public void setRevenue(java.lang.Double revenue) {
        this.revenue = revenue;
    }
    
	//Huypq-08042020-start
    private Double completeTarget;
    private Double hshcXlTarget;
    private Double revenueCpTarget;
    private Double revenueNtdGpdnTarget;
    private Double revenueNtdXdddTarget;
    private Double hshcHtctTarget;
    private Double quantityXlTarget;
    private Double quantityCpTarget;
    private Double quantityNtdGpdnTarget;
    private Double quantityNtdXdddTarget;
    private Double taskXdddTarget;
    private Double revokeCashTarget;
    private Long sumDeployHtct;
    private Long buildCompleteHtct;
    private Long completeDbHtct;

	public Double getCompleteTarget() {
		return completeTarget;
	}

	public void setCompleteTarget(Double completeTarget) {
		this.completeTarget = completeTarget;
	}

	public Double getHshcXlTarget() {
		return hshcXlTarget;
	}

	public void setHshcXlTarget(Double hshcXlTarget) {
		this.hshcXlTarget = hshcXlTarget;
	}

	public Double getRevenueCpTarget() {
		return revenueCpTarget;
	}

	public void setRevenueCpTarget(Double revenueCpTarget) {
		this.revenueCpTarget = revenueCpTarget;
	}

	public Double getRevenueNtdGpdnTarget() {
		return revenueNtdGpdnTarget;
	}

	public void setRevenueNtdGpdnTarget(Double revenueNtdGpdnTarget) {
		this.revenueNtdGpdnTarget = revenueNtdGpdnTarget;
	}

	public Double getRevenueNtdXdddTarget() {
		return revenueNtdXdddTarget;
	}

	public void setRevenueNtdXdddTarget(Double revenueNtdXdddTarget) {
		this.revenueNtdXdddTarget = revenueNtdXdddTarget;
	}

	public Double getQuantityXlTarget() {
		return quantityXlTarget;
	}

	public void setQuantityXlTarget(Double quantityXlTarget) {
		this.quantityXlTarget = quantityXlTarget;
	}

	public Double getQuantityCpTarget() {
		return quantityCpTarget;
	}

	public void setQuantityCpTarget(Double quantityCpTarget) {
		this.quantityCpTarget = quantityCpTarget;
	}

	public Double getQuantityNtdGpdnTarget() {
		return quantityNtdGpdnTarget;
	}

	public void setQuantityNtdGpdnTarget(Double quantityNtdGpdnTarget) {
		this.quantityNtdGpdnTarget = quantityNtdGpdnTarget;
	}

	public Double getQuantityNtdXdddTarget() {
		return quantityNtdXdddTarget;
	}

	public void setQuantityNtdXdddTarget(Double quantityNtdXdddTarget) {
		this.quantityNtdXdddTarget = quantityNtdXdddTarget;
	}

	public Double getTaskXdddTarget() {
		return taskXdddTarget;
	}

	public void setTaskXdddTarget(Double taskXdddTarget) {
		this.taskXdddTarget = taskXdddTarget;
	}

	public Double getRevokeCashTarget() {
		return revokeCashTarget;
	}

	public void setRevokeCashTarget(Double revokeCashTarget) {
		this.revokeCashTarget = revokeCashTarget;
	}

	public Long getSumDeployHtct() {
		return sumDeployHtct;
	}

	public void setSumDeployHtct(Long sumDeployHtct) {
		this.sumDeployHtct = sumDeployHtct;
	}

	public Long getBuildCompleteHtct() {
		return buildCompleteHtct;
	}

	public void setBuildCompleteHtct(Long buildCompleteHtct) {
		this.buildCompleteHtct = buildCompleteHtct;
	}

	public Long getCompleteDbHtct() {
		return completeDbHtct;
	}

	public void setCompleteDbHtct(Long completeDbHtct) {
		this.completeDbHtct = completeDbHtct;
	}

	public void setHshcHtctTarget(Double hshcHtctTarget) {
		this.hshcHtctTarget = hshcHtctTarget;
	}

	public Double getHshcHtctTarget() {
		return hshcHtctTarget;
	}
    
    
    //Huy-end
}
