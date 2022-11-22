package com.viettel.coms.dto;

import java.util.List;
/**
 * @author HoangNH38
 */
public class ReportPlanOSDTO extends YearPlanDetailOSDTO{


    private java.lang.Double currentQuantity;
    private java.lang.Double progressQuantity;
    private java.lang.Double progressComplete;
    private java.lang.Double currentComplete;
    private java.lang.Double currentRevenue;
    private java.lang.Double progressRevenue;
    private java.lang.String sysGroupName;
    private List<String> listYear;
    private String userName;
    private String sysName;
    private String progress;
    private java.lang.Double tiendo;
    private List<ReportPlanOSDTO> reportPlanList;
    private List<Long> listTotalMonthPlanId;
    private java.lang.Double quantityTotal;
    private java.lang.Double currentQuantityTotal;
    private java.lang.Double completeTotal;
    private java.lang.Double currentCompleteTotal;
    private java.lang.Double revenueTotal;
    private java.lang.Double currentRevenueTotal;
    private java.lang.Double progressQuantityTotal;
    private java.lang.Double progressCompleteTotal;
    private java.lang.Double progressRevenueTotal;
    
    public java.lang.Double getProgressQuantityTotal() {
		return progressQuantityTotal;
	}

	public void setProgressQuantityTotal(java.lang.Double progressQuantityTotal) {
		this.progressQuantityTotal = progressQuantityTotal;
	}

	public java.lang.Double getProgressCompleteTotal() {
		return progressCompleteTotal;
	}

	public void setProgressCompleteTotal(java.lang.Double progressCompleteTotal) {
		this.progressCompleteTotal = progressCompleteTotal;
	}

	public java.lang.Double getProgressRevenueTotal() {
		return progressRevenueTotal;
	}

	public void setProgressRevenueTotal(java.lang.Double progressRevenueTotal) {
		this.progressRevenueTotal = progressRevenueTotal;
	}

	public java.lang.Double getQuantityTotal() {
		return quantityTotal;
	}

	public void setQuantityTotal(java.lang.Double quantityTotal) {
		this.quantityTotal = quantityTotal;
	}

	public java.lang.Double getCurrentQuantityTotal() {
		return currentQuantityTotal;
	}

	public void setCurrentQuantityTotal(java.lang.Double currentQuantityTotal) {
		this.currentQuantityTotal = currentQuantityTotal;
	}

	public java.lang.Double getCompleteTotal() {
		return completeTotal;
	}

	public void setCompleteTotal(java.lang.Double completeTotal) {
		this.completeTotal = completeTotal;
	}

	public java.lang.Double getCurrentCompleteTotal() {
		return currentCompleteTotal;
	}

	public void setCurrentCompleteTotal(java.lang.Double currentCompleteTotal) {
		this.currentCompleteTotal = currentCompleteTotal;
	}

	public java.lang.Double getRevenueTotal() {
		return revenueTotal;
	}

	public void setRevenueTotal(java.lang.Double revenueTotal) {
		this.revenueTotal = revenueTotal;
	}

	public java.lang.Double getCurrentRevenueTotal() {
		return currentRevenueTotal;
	}

	public void setCurrentRevenueTotal(java.lang.Double currentRevenueTotal) {
		this.currentRevenueTotal = currentRevenueTotal;
	}
//	hoanm1_20180922_end
	public List<Long> getListTotalMonthPlanId() {
        return listTotalMonthPlanId;
    }

    public void setListTotalMonthPlanId(List<Long> listTotalMonthPlanId) {
        this.listTotalMonthPlanId = listTotalMonthPlanId;
    }

    public List<ReportPlanOSDTO> getReportPlanList() {
        return reportPlanList;
    }

    public void setReportPlanList(List<ReportPlanOSDTO> reportPlanList) {
        this.reportPlanList = reportPlanList;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    private String tt;

    public String getTt() {
        return tt;
    }

    public void setTt(String tt) {
        this.tt = tt;

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public java.lang.Double getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(java.lang.Double currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    public void setProgressQuantity(java.lang.Double progressQuantity) {
        this.progressQuantity = progressQuantity;
    }

    public java.lang.Double getProgressQuantity() {
        return progressQuantity;
    }

    public java.lang.Double getProgressComplete() {
        return progressComplete;
    }

    public void setProgressComplete(java.lang.Double progressComplete) {
        this.progressComplete = progressComplete;
    }

    public java.lang.Double getCurrentComplete() {
        return currentComplete;
    }

    public void setCurrentComplete(java.lang.Double currentComplete) {
        this.currentComplete = currentComplete;
    }

    public java.lang.Double getCurrentRevenue() {
        return currentRevenue;
    }

    public void setCurrentRevenue(java.lang.Double currentRevenue) {
        this.currentRevenue = currentRevenue;
    }

    public java.lang.Double getProgressRevenue() {
        return progressRevenue;
    }

    public void setProgressRevenue(java.lang.Double progressRevenue) {
        this.progressRevenue = progressRevenue;
    }

    public java.lang.String getSysGroupName() {
        return sysGroupName;
    }

    public void setSysGroupName(java.lang.String sysGroupName) {
        this.sysGroupName = sysGroupName;
    }

    public List<String> getListYear() {
        return listYear;
    }

    public void setListYear(List<String> listYear) {
        this.listYear = listYear;
    }

    public java.lang.Double getTiendo() {
        return tiendo;
    }

    public void setTiendo(java.lang.Double tiendo) {
        this.tiendo = tiendo;
    }


}
