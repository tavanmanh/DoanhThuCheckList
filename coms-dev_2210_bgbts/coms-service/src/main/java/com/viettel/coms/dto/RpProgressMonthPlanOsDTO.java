package com.viettel.coms.dto;

public class RpProgressMonthPlanOsDTO {

	//Huypq-29052020-start
		private Long sysGroupId;
		private String month;
		private String year;
		private String sysGroupName;
		private Double quantity;
		private Double currentQuantity;
		private Double progressQuantity;
		private Double complete;
		private Double currentComplete;
		private Double progressComplete;
		private Double revenue;
		private Double currentRevenueMonth;
		private Double progressRevenueMonth;
		private Double revokeCashTarget;
		private Double revokeCashComplete;
		private Double processRevokeCash;
		
		public Long getSysGroupId() {
			return sysGroupId;
		}

		public void setSysGroupId(Long sysGroupId) {
			this.sysGroupId = sysGroupId;
		}

		public String getMonth() {
			return month;
		}

		public void setMonth(String month) {
			this.month = month;
		}

		public String getYear() {
			return year;
		}

		public void setYear(String year) {
			this.year = year;
		}

		public String getSysGroupName() {
			return sysGroupName;
		}

		public void setSysGroupName(String sysGroupName) {
			this.sysGroupName = sysGroupName;
		}

		public Double getProgressQuantity() {
			return progressQuantity;
		}

		public void setProgressQuantity(Double progressQuantity) {
			this.progressQuantity = progressQuantity;
		}

		public Double getProgressComplete() {
			return progressComplete;
		}

		public void setProgressComplete(Double progressComplete) {
			this.progressComplete = progressComplete;
		}

		public Double getProgressRevenueMonth() {
			return progressRevenueMonth;
		}

		public void setProgressRevenueMonth(Double progressRevenueMonth) {
			this.progressRevenueMonth = progressRevenueMonth;
		}

		public Double getRevokeCashTarget() {
			return revokeCashTarget;
		}

		public void setRevokeCashTarget(Double revokeCashTarget) {
			this.revokeCashTarget = revokeCashTarget;
		}

		public Double getRevokeCashComplete() {
			return revokeCashComplete;
		}

		public void setRevokeCashComplete(Double revokeCashComplete) {
			this.revokeCashComplete = revokeCashComplete;
		}

		public Double getProcessRevokeCash() {
			return processRevokeCash;
		}

		public void setProcessRevokeCash(Double processRevokeCash) {
			this.processRevokeCash = processRevokeCash;
		}
		
		private Double quyLuongTarget;
		private Double quyLuongComplete;
		private Double processQuyLuong;
		private Double hshcXlTarget;
		private Double hshcXlComplete;
		private Double processHshcXl;
		private Double revenueCpTarget;
		private Double revenueCpComplete;
		private Double processRevenueCp;
		private Double revenueNtdgpdnTarget;
		private Double revenueNtdgpdnComplete;
		private Double processRevenueNtdgpdn;
		private Double revenueNtdxdddTarget;
		private Double revenueNtdxdddComplete;
		private Double processRevenueNtdxddd;
		private Double hshcHtctTarget;
		private Double hshcHtctComplete;
		private Double processHshcHtct;
		private Double quantityXlTarget;
		private Double quantityXlComplete;
		private Double processQuantityXl;
		private Double quantityCpTarget;
		private Double quantityCpComplete;
		private Double processQuantityCp;
		private Double quantityNtdGpdnTarget;
		private Double quantityNtdGpdnComplete;
		private Double processQuantityNtdGpdn;
		private Double quantityNtdXdddTarget;
		private Double quantityNtdXdddComplete;
		private Double processQuantityNtdXddd;
		private Double taskXdddTarget;
		private Double taskXdddComplete;
		private Double processTaskXddd;
		private Double sumDeployHtctTarget;
		private Double sumDeployHtctComplete;
		private Double processSumDeployHtct;
		private Double mongHtctTarget;
		private Double mongHtctComplete;
		private Double processMongHtct;
		private Double dbHtctTarget;
		private Double dbHtctComplete;
		private Double processDbHtct;
		private Double rentHtctTarget;
		private Double rentHtctComplete;
		private Double processRentHtct;
		private String monthYear;

		public String getMonthYear() {
			return monthYear;
		}

		public void setMonthYear(String monthYear) {
			this.monthYear = monthYear;
		}


		public Double getProcessQuyLuong() {
			return processQuyLuong;
		}

		public void setProcessQuyLuong(Double processQuyLuong) {
			this.processQuyLuong = processQuyLuong;
		}


		public Double getProcessHshcXl() {
			return processHshcXl;
		}

		public void setProcessHshcXl(Double processHshcXl) {
			this.processHshcXl = processHshcXl;
		}


		public Double getProcessRevenueCp() {
			return processRevenueCp;
		}

		public void setProcessRevenueCp(Double processRevenueCp) {
			this.processRevenueCp = processRevenueCp;
		}

		public Double getProcessRevenueNtdgpdn() {
			return processRevenueNtdgpdn;
		}

		public void setProcessRevenueNtdgpdn(Double processRevenueNtdgpdn) {
			this.processRevenueNtdgpdn = processRevenueNtdgpdn;
		}


		public Double getProcessRevenueNtdxddd() {
			return processRevenueNtdxddd;
		}

		public void setProcessRevenueNtdxddd(Double processRevenueNtdxddd) {
			this.processRevenueNtdxddd = processRevenueNtdxddd;
		}


		public Double getProcessHshcHtct() {
			return processHshcHtct;
		}

		public void setProcessHshcHtct(Double processHshcHtct) {
			this.processHshcHtct = processHshcHtct;
		}

		public Double getProcessQuantityXl() {
			return processQuantityXl;
		}

		public void setProcessQuantityXl(Double processQuantityXl) {
			this.processQuantityXl = processQuantityXl;
		}


		public Double getProcessQuantityCp() {
			return processQuantityCp;
		}

		public void setProcessQuantityCp(Double processQuantityCp) {
			this.processQuantityCp = processQuantityCp;
		}


		public Double getProcessQuantityNtdGpdn() {
			return processQuantityNtdGpdn;
		}

		public void setProcessQuantityNtdGpdn(Double processQuantityNtdGpdn) {
			this.processQuantityNtdGpdn = processQuantityNtdGpdn;
		}


		public Double getProcessQuantityNtdXddd() {
			return processQuantityNtdXddd;
		}

		public void setProcessQuantityNtdXddd(Double processQuantityNtdXddd) {
			this.processQuantityNtdXddd = processQuantityNtdXddd;
		}

		public Double getTaskXdddComplete() {
			return taskXdddComplete;
		}

		public void setTaskXdddComplete(Double taskXdddComplete) {
			this.taskXdddComplete = taskXdddComplete;
		}

		public Double getProcessTaskXddd() {
			return processTaskXddd;
		}

		public void setProcessTaskXddd(Double processTaskXddd) {
			this.processTaskXddd = processTaskXddd;
		}

		public Double getProcessSumDeployHtct() {
			return processSumDeployHtct;
		}

		public void setProcessSumDeployHtct(Double processSumDeployHtct) {
			this.processSumDeployHtct = processSumDeployHtct;
		}


		public Double getProcessMongHtct() {
			return processMongHtct;
		}

		public void setProcessMongHtct(Double processMongHtct) {
			this.processMongHtct = processMongHtct;
		}


		public Double getProcessDbHtct() {
			return processDbHtct;
		}

		public void setProcessDbHtct(Double processDbHtct) {
			this.processDbHtct = processDbHtct;
		}

		public Double getProcessRentHtct() {
			return processRentHtct;
		}

		public void setProcessRentHtct(Double processRentHtct) {
			this.processRentHtct = processRentHtct;
		}

		//Báo cáo chấm điểm
//		private String	year;
//		private String	month;
//		private String	sysGroupName;
//		private Long	sysGroupId;
		private String	areaCode;
		private String	provinceCode;
		private Double	diemDatTong;
		private Double	diemThuongTong;
		private Double	tongDiem;
		private Double	quyDoiDiem;
//		private Double	quyLuongTarget;
//		private Double	quyLuongComplete;
//		private Double	processQuyLuong;
		private Double	diemDatQuyLuong;
		private Double	diemThuongQuyLuong;
//		private Double	hshcXlTarget;
//		private Double	hshcXlComplete;
//		private Double	processHshcXl;
		private Double	diemDatHschXl;
		private Double	diemThuongHshcXl;
//		private Double	revenueCpTarget;
//		private Double	revenueCpComplete;
//		private Double	processRevenueCp;
		private Double	diemDatRevenueCp;
		private Double	diemThuongRevenueCp;
		private Double	revenueNtdXdddTarget;
		private Double	revenueNtdXdddComplete;
		private Double	processRevenueNtdXddd;
		private Double	diemDatRevenueNtdXddd;
		private Double	diemThuongRevenueNtdXddd;
//		private Double	quantityXlTarget;
//		private Double	quantityXlComplete;
//		private Double	processQuantityXl;
		private Double	diemDatQuantityXl;
		private Double	diemThuongQuantityXl;
//		private Double	quantityCpTarget;
//		private Double	quantityCpComplete;
//		private Double	processQuantityCp;
		private Double	diemDatQuantityCp;
		private Double	diemThuongQuantityCp;
//		private Double	quantityNtdXdddTarget;
//		private Double	quantityNtdXdddComplete;
//		private Double	processQuantityNtdXddd;
		private Double	diemDatQuantityNtdXddd;
		private Double	diemThuongQuantityNtdXddd;
//		private Double	taskXdddTarget;
//		private Double	taskXdddComplete;
//		private Double	processTaskXddd;
		private Double	diemDatTaskXddd;
		private Double	diemThuongTaskXddd;
//		private Double	revokeCashTarget;
//		private Double	revokeCashComplete;
//		private Double	processRevokeCash;
		private Double	diemDatRevokeCash;
		private Double	diemThuongRevokeCash;
//		private Double	rentHtctTarget;
//		private Double	rentHtctComplete;
//		private Double	processRentHtct;
		private Double	diemdatRentHtct;
		private Double	diemThuongRentHtct;
//		private Double	mongHtctTarget;
//		private Double	mongHtctComplete;
//		private Double	processMongHtct;
		private Double	diemDatMongHtct;
		private Double	diemThuongMongHtct;
//		private Double	dbHtctTarget;
//		private Double	dbHtctComplete;
//		private Double	processDbHtct;
		private Double	diemDatDbHtct;
		private Double	diemThuongDbHtct;
		
		private Double tkHtctTarget;
		private Double tkHtctComplete;
		private Double processTkHtct;
		private Double diemDatTkHtct;
		private Double diemThuongTkHtct;
		private Double revenueNotApprove;
		private Double processRevenueNotApprove;

		public Double getRevenueNotApprove() {
			return revenueNotApprove;
		}

		public void setRevenueNotApprove(Double revenueNotApprove) {
			this.revenueNotApprove = revenueNotApprove;
		}

		public Double getProcessRevenueNotApprove() {
			return processRevenueNotApprove;
		}

		public void setProcessRevenueNotApprove(Double processRevenueNotApprove) {
			this.processRevenueNotApprove = processRevenueNotApprove;
		}

		public Double getTkHtctTarget() {
			return tkHtctTarget;
		}

		public void setTkHtctTarget(Double tkHtctTarget) {
			this.tkHtctTarget = tkHtctTarget;
		}

		public Double getTkHtctComplete() {
			return tkHtctComplete;
		}

		public void setTkHtctComplete(Double tkHtctComplete) {
			this.tkHtctComplete = tkHtctComplete;
		}

		public Double getProcessTkHtct() {
			return processTkHtct;
		}

		public void setProcessTkHtct(Double processTkHtct) {
			this.processTkHtct = processTkHtct;
		}

		public Double getDiemDatTkHtct() {
			return diemDatTkHtct;
		}

		public void setDiemDatTkHtct(Double diemDatTkHtct) {
			this.diemDatTkHtct = diemDatTkHtct;
		}

		public Double getDiemThuongTkHtct() {
			return diemThuongTkHtct;
		}

		public void setDiemThuongTkHtct(Double diemThuongTkHtct) {
			this.diemThuongTkHtct = diemThuongTkHtct;
		}

		public String getAreaCode() {
			return areaCode;
		}

		public void setAreaCode(String areaCode) {
			this.areaCode = areaCode;
		}

		public String getProvinceCode() {
			return provinceCode;
		}

		public void setProvinceCode(String provinceCode) {
			this.provinceCode = provinceCode;
		}

		public Double getDiemDatTong() {
			return diemDatTong;
		}

		public void setDiemDatTong(Double diemDatTong) {
			this.diemDatTong = diemDatTong;
		}

		public Double getDiemThuongTong() {
			return diemThuongTong;
		}

		public void setDiemThuongTong(Double diemThuongTong) {
			this.diemThuongTong = diemThuongTong;
		}

		public Double getTongDiem() {
			return tongDiem;
		}

		public void setTongDiem(Double tongDiem) {
			this.tongDiem = tongDiem;
		}

		public Double getQuyDoiDiem() {
			return quyDoiDiem;
		}

		public void setQuyDoiDiem(Double quyDoiDiem) {
			this.quyDoiDiem = quyDoiDiem;
		}

		public Double getDiemDatQuyLuong() {
			return diemDatQuyLuong;
		}

		public void setDiemDatQuyLuong(Double diemDatQuyLuong) {
			this.diemDatQuyLuong = diemDatQuyLuong;
		}

		public Double getDiemThuongQuyLuong() {
			return diemThuongQuyLuong;
		}

		public void setDiemThuongQuyLuong(Double diemThuongQuyLuong) {
			this.diemThuongQuyLuong = diemThuongQuyLuong;
		}

		public Double getDiemDatHschXl() {
			return diemDatHschXl;
		}

		public void setDiemDatHschXl(Double diemDatHschXl) {
			this.diemDatHschXl = diemDatHschXl;
		}

		public Double getDiemThuongHshcXl() {
			return diemThuongHshcXl;
		}

		public void setDiemThuongHshcXl(Double diemThuongHshcXl) {
			this.diemThuongHshcXl = diemThuongHshcXl;
		}

		public Double getDiemDatRevenueCp() {
			return diemDatRevenueCp;
		}

		public void setDiemDatRevenueCp(Double diemDatRevenueCp) {
			this.diemDatRevenueCp = diemDatRevenueCp;
		}

		public Double getDiemThuongRevenueCp() {
			return diemThuongRevenueCp;
		}

		public void setDiemThuongRevenueCp(Double diemThuongRevenueCp) {
			this.diemThuongRevenueCp = diemThuongRevenueCp;
		}

		public Double getRevenueNtdXdddTarget() {
			return revenueNtdXdddTarget;
		}

		public void setRevenueNtdXdddTarget(Double revenueNtdXdddTarget) {
			this.revenueNtdXdddTarget = revenueNtdXdddTarget;
		}

		public Double getRevenueNtdXdddComplete() {
			return revenueNtdXdddComplete;
		}

		public void setRevenueNtdXdddComplete(Double revenueNtdXdddComplete) {
			this.revenueNtdXdddComplete = revenueNtdXdddComplete;
		}

		public Double getProcessRevenueNtdXddd() {
			return processRevenueNtdXddd;
		}

		public void setProcessRevenueNtdXddd(Double processRevenueNtdXddd) {
			this.processRevenueNtdXddd = processRevenueNtdXddd;
		}

		public Double getDiemDatRevenueNtdXddd() {
			return diemDatRevenueNtdXddd;
		}

		public void setDiemDatRevenueNtdXddd(Double diemDatRevenueNtdXddd) {
			this.diemDatRevenueNtdXddd = diemDatRevenueNtdXddd;
		}

		public Double getDiemThuongRevenueNtdXddd() {
			return diemThuongRevenueNtdXddd;
		}

		public void setDiemThuongRevenueNtdXddd(Double diemThuongRevenueNtdXddd) {
			this.diemThuongRevenueNtdXddd = diemThuongRevenueNtdXddd;
		}

		public Double getDiemDatQuantityXl() {
			return diemDatQuantityXl;
		}

		public void setDiemDatQuantityXl(Double diemDatQuantityXl) {
			this.diemDatQuantityXl = diemDatQuantityXl;
		}

		public Double getDiemThuongQuantityXl() {
			return diemThuongQuantityXl;
		}

		public void setDiemThuongQuantityXl(Double diemThuongQuantityXl) {
			this.diemThuongQuantityXl = diemThuongQuantityXl;
		}

		public Double getDiemDatQuantityCp() {
			return diemDatQuantityCp;
		}

		public void setDiemDatQuantityCp(Double diemDatQuantityCp) {
			this.diemDatQuantityCp = diemDatQuantityCp;
		}

		public Double getDiemThuongQuantityCp() {
			return diemThuongQuantityCp;
		}

		public void setDiemThuongQuantityCp(Double diemThuongQuantityCp) {
			this.diemThuongQuantityCp = diemThuongQuantityCp;
		}

		public Double getDiemDatQuantityNtdXddd() {
			return diemDatQuantityNtdXddd;
		}

		public void setDiemDatQuantityNtdXddd(Double diemDatQuantityNtdXddd) {
			this.diemDatQuantityNtdXddd = diemDatQuantityNtdXddd;
		}

		public Double getDiemThuongQuantityNtdXddd() {
			return diemThuongQuantityNtdXddd;
		}

		public void setDiemThuongQuantityNtdXddd(Double diemThuongQuantityNtdXddd) {
			this.diemThuongQuantityNtdXddd = diemThuongQuantityNtdXddd;
		}

		public Double getDiemDatTaskXddd() {
			return diemDatTaskXddd;
		}

		public void setDiemDatTaskXddd(Double diemDatTaskXddd) {
			this.diemDatTaskXddd = diemDatTaskXddd;
		}

		public Double getDiemThuongTaskXddd() {
			return diemThuongTaskXddd;
		}

		public void setDiemThuongTaskXddd(Double diemThuongTaskXddd) {
			this.diemThuongTaskXddd = diemThuongTaskXddd;
		}

		public Double getDiemDatRevokeCash() {
			return diemDatRevokeCash;
		}

		public void setDiemDatRevokeCash(Double diemDatRevokeCash) {
			this.diemDatRevokeCash = diemDatRevokeCash;
		}

		public Double getDiemThuongRevokeCash() {
			return diemThuongRevokeCash;
		}

		public void setDiemThuongRevokeCash(Double diemThuongRevokeCash) {
			this.diemThuongRevokeCash = diemThuongRevokeCash;
		}

		public Double getDiemdatRentHtct() {
			return diemdatRentHtct;
		}

		public void setDiemdatRentHtct(Double diemdatRentHtct) {
			this.diemdatRentHtct = diemdatRentHtct;
		}

		public Double getDiemThuongRentHtct() {
			return diemThuongRentHtct;
		}

		public void setDiemThuongRentHtct(Double diemThuongRentHtct) {
			this.diemThuongRentHtct = diemThuongRentHtct;
		}

		public Double getDiemDatMongHtct() {
			return diemDatMongHtct;
		}

		public void setDiemDatMongHtct(Double diemDatMongHtct) {
			this.diemDatMongHtct = diemDatMongHtct;
		}

		public Double getDiemThuongMongHtct() {
			return diemThuongMongHtct;
		}

		public void setDiemThuongMongHtct(Double diemThuongMongHtct) {
			this.diemThuongMongHtct = diemThuongMongHtct;
		}

		public Double getDiemDatDbHtct() {
			return diemDatDbHtct;
		}

		public void setDiemDatDbHtct(Double diemDatDbHtct) {
			this.diemDatDbHtct = diemDatDbHtct;
		}

		public Double getDiemThuongDbHtct() {
			return diemThuongDbHtct;
		}

		public void setDiemThuongDbHtct(Double diemThuongDbHtct) {
			this.diemThuongDbHtct = diemThuongDbHtct;
		}

		public Double getQuantity() {
			return quantity;
		}

		public void setQuantity(Double quantity) {
			this.quantity = quantity;
		}

		public Double getCurrentQuantity() {
			return currentQuantity;
		}

		public void setCurrentQuantity(Double currentQuantity) {
			this.currentQuantity = currentQuantity;
		}

		public Double getComplete() {
			return complete;
		}

		public void setComplete(Double complete) {
			this.complete = complete;
		}

		public Double getCurrentComplete() {
			return currentComplete;
		}

		public void setCurrentComplete(Double currentComplete) {
			this.currentComplete = currentComplete;
		}

		public Double getRevenue() {
			return revenue;
		}

		public void setRevenue(Double revenue) {
			this.revenue = revenue;
		}

		public Double getCurrentRevenueMonth() {
			return currentRevenueMonth;
		}

		public void setCurrentRevenueMonth(Double currentRevenueMonth) {
			this.currentRevenueMonth = currentRevenueMonth;
		}

		public Double getQuyLuongTarget() {
			return quyLuongTarget;
		}

		public void setQuyLuongTarget(Double quyLuongTarget) {
			this.quyLuongTarget = quyLuongTarget;
		}

		public Double getQuyLuongComplete() {
			return quyLuongComplete;
		}

		public void setQuyLuongComplete(Double quyLuongComplete) {
			this.quyLuongComplete = quyLuongComplete;
		}

		public Double getHshcXlTarget() {
			return hshcXlTarget;
		}

		public void setHshcXlTarget(Double hshcXlTarget) {
			this.hshcXlTarget = hshcXlTarget;
		}

		public Double getHshcXlComplete() {
			return hshcXlComplete;
		}

		public void setHshcXlComplete(Double hshcXlComplete) {
			this.hshcXlComplete = hshcXlComplete;
		}

		public Double getRevenueCpTarget() {
			return revenueCpTarget;
		}

		public void setRevenueCpTarget(Double revenueCpTarget) {
			this.revenueCpTarget = revenueCpTarget;
		}

		public Double getRevenueCpComplete() {
			return revenueCpComplete;
		}

		public void setRevenueCpComplete(Double revenueCpComplete) {
			this.revenueCpComplete = revenueCpComplete;
		}

		public Double getRevenueNtdgpdnTarget() {
			return revenueNtdgpdnTarget;
		}

		public void setRevenueNtdgpdnTarget(Double revenueNtdgpdnTarget) {
			this.revenueNtdgpdnTarget = revenueNtdgpdnTarget;
		}

		public Double getRevenueNtdgpdnComplete() {
			return revenueNtdgpdnComplete;
		}

		public void setRevenueNtdgpdnComplete(Double revenueNtdgpdnComplete) {
			this.revenueNtdgpdnComplete = revenueNtdgpdnComplete;
		}

		public Double getRevenueNtdxdddTarget() {
			return revenueNtdxdddTarget;
		}

		public void setRevenueNtdxdddTarget(Double revenueNtdxdddTarget) {
			this.revenueNtdxdddTarget = revenueNtdxdddTarget;
		}

		public Double getRevenueNtdxdddComplete() {
			return revenueNtdxdddComplete;
		}

		public void setRevenueNtdxdddComplete(Double revenueNtdxdddComplete) {
			this.revenueNtdxdddComplete = revenueNtdxdddComplete;
		}

		public Double getHshcHtctTarget() {
			return hshcHtctTarget;
		}

		public void setHshcHtctTarget(Double hshcHtctTarget) {
			this.hshcHtctTarget = hshcHtctTarget;
		}

		public Double getHshcHtctComplete() {
			return hshcHtctComplete;
		}

		public void setHshcHtctComplete(Double hshcHtctComplete) {
			this.hshcHtctComplete = hshcHtctComplete;
		}

		public Double getQuantityXlTarget() {
			return quantityXlTarget;
		}

		public void setQuantityXlTarget(Double quantityXlTarget) {
			this.quantityXlTarget = quantityXlTarget;
		}

		public Double getQuantityXlComplete() {
			return quantityXlComplete;
		}

		public void setQuantityXlComplete(Double quantityXlComplete) {
			this.quantityXlComplete = quantityXlComplete;
		}

		public Double getQuantityCpTarget() {
			return quantityCpTarget;
		}

		public void setQuantityCpTarget(Double quantityCpTarget) {
			this.quantityCpTarget = quantityCpTarget;
		}

		public Double getQuantityCpComplete() {
			return quantityCpComplete;
		}

		public void setQuantityCpComplete(Double quantityCpComplete) {
			this.quantityCpComplete = quantityCpComplete;
		}

		public Double getQuantityNtdGpdnTarget() {
			return quantityNtdGpdnTarget;
		}

		public void setQuantityNtdGpdnTarget(Double quantityNtdGpdnTarget) {
			this.quantityNtdGpdnTarget = quantityNtdGpdnTarget;
		}

		public Double getQuantityNtdGpdnComplete() {
			return quantityNtdGpdnComplete;
		}

		public void setQuantityNtdGpdnComplete(Double quantityNtdGpdnComplete) {
			this.quantityNtdGpdnComplete = quantityNtdGpdnComplete;
		}

		public Double getQuantityNtdXdddTarget() {
			return quantityNtdXdddTarget;
		}

		public void setQuantityNtdXdddTarget(Double quantityNtdXdddTarget) {
			this.quantityNtdXdddTarget = quantityNtdXdddTarget;
		}

		public Double getQuantityNtdXdddComplete() {
			return quantityNtdXdddComplete;
		}

		public void setQuantityNtdXdddComplete(Double quantityNtdXdddComplete) {
			this.quantityNtdXdddComplete = quantityNtdXdddComplete;
		}

		public Double getTaskXdddTarget() {
			return taskXdddTarget;
		}

		public void setTaskXdddTarget(Double taskXdddTarget) {
			this.taskXdddTarget = taskXdddTarget;
		}

		public Double getSumDeployHtctTarget() {
			return sumDeployHtctTarget;
		}

		public void setSumDeployHtctTarget(Double sumDeployHtctTarget) {
			this.sumDeployHtctTarget = sumDeployHtctTarget;
		}

		public Double getSumDeployHtctComplete() {
			return sumDeployHtctComplete;
		}

		public void setSumDeployHtctComplete(Double sumDeployHtctComplete) {
			this.sumDeployHtctComplete = sumDeployHtctComplete;
		}

		public Double getMongHtctTarget() {
			return mongHtctTarget;
		}

		public void setMongHtctTarget(Double mongHtctTarget) {
			this.mongHtctTarget = mongHtctTarget;
		}

		public Double getMongHtctComplete() {
			return mongHtctComplete;
		}

		public void setMongHtctComplete(Double mongHtctComplete) {
			this.mongHtctComplete = mongHtctComplete;
		}

		public Double getDbHtctTarget() {
			return dbHtctTarget;
		}

		public void setDbHtctTarget(Double dbHtctTarget) {
			this.dbHtctTarget = dbHtctTarget;
		}

		public Double getDbHtctComplete() {
			return dbHtctComplete;
		}

		public void setDbHtctComplete(Double dbHtctComplete) {
			this.dbHtctComplete = dbHtctComplete;
		}

		public Double getRentHtctTarget() {
			return rentHtctTarget;
		}

		public void setRentHtctTarget(Double rentHtctTarget) {
			this.rentHtctTarget = rentHtctTarget;
		}

		public Double getRentHtctComplete() {
			return rentHtctComplete;
		}

		public void setRentHtctComplete(Double rentHtctComplete) {
			this.rentHtctComplete = rentHtctComplete;
		}

		//
		private Double quantityTotal;
        private Double currentQuantityTotal;
        private Double progressQuantityTotal;
        private Double completeTotal;
        private Double currentCompleteTotal;
        private Double progressCompleteTotal;
        private Double revenueTotal;
        private Double currentRevenueTotal;
        private Double progressRevenueTotal;
        private Double revokeCashTargetTotal;
        private Double revokeCashCompleteTotal;
        private Double processRevokeCashTotal;
        private Double revenueNotApproveTotal;
        private Double processRevenueNotApproveTotal;

		public Double getQuantityTotal() {
			return quantityTotal;
		}

		public void setQuantityTotal(Double quantityTotal) {
			this.quantityTotal = quantityTotal;
		}

		public Double getCurrentQuantityTotal() {
			return currentQuantityTotal;
		}

		public void setCurrentQuantityTotal(Double currentQuantityTotal) {
			this.currentQuantityTotal = currentQuantityTotal;
		}

		public Double getProgressQuantityTotal() {
			return progressQuantityTotal;
		}

		public void setProgressQuantityTotal(Double progressQuantityTotal) {
			this.progressQuantityTotal = progressQuantityTotal;
		}

		public Double getCompleteTotal() {
			return completeTotal;
		}

		public void setCompleteTotal(Double completeTotal) {
			this.completeTotal = completeTotal;
		}

		public Double getCurrentCompleteTotal() {
			return currentCompleteTotal;
		}

		public void setCurrentCompleteTotal(Double currentCompleteTotal) {
			this.currentCompleteTotal = currentCompleteTotal;
		}

		public Double getProgressCompleteTotal() {
			return progressCompleteTotal;
		}

		public void setProgressCompleteTotal(Double progressCompleteTotal) {
			this.progressCompleteTotal = progressCompleteTotal;
		}

		public Double getRevenueTotal() {
			return revenueTotal;
		}

		public void setRevenueTotal(Double revenueTotal) {
			this.revenueTotal = revenueTotal;
		}

		public Double getCurrentRevenueTotal() {
			return currentRevenueTotal;
		}

		public void setCurrentRevenueTotal(Double currentRevenueTotal) {
			this.currentRevenueTotal = currentRevenueTotal;
		}

		public Double getProgressRevenueTotal() {
			return progressRevenueTotal;
		}

		public void setProgressRevenueTotal(Double progressRevenueTotal) {
			this.progressRevenueTotal = progressRevenueTotal;
		}

		public Double getRevokeCashTargetTotal() {
			return revokeCashTargetTotal;
		}

		public void setRevokeCashTargetTotal(Double revokeCashTargetTotal) {
			this.revokeCashTargetTotal = revokeCashTargetTotal;
		}

		public Double getRevokeCashCompleteTotal() {
			return revokeCashCompleteTotal;
		}

		public void setRevokeCashCompleteTotal(Double revokeCashCompleteTotal) {
			this.revokeCashCompleteTotal = revokeCashCompleteTotal;
		}

		public Double getProcessRevokeCashTotal() {
			return processRevokeCashTotal;
		}

		public void setProcessRevokeCashTotal(Double processRevokeCashTotal) {
			this.processRevokeCashTotal = processRevokeCashTotal;
		}

		public Double getRevenueNotApproveTotal() {
			return revenueNotApproveTotal;
		}

		public void setRevenueNotApproveTotal(Double revenueNotApproveTotal) {
			this.revenueNotApproveTotal = revenueNotApproveTotal;
		}

		public Double getProcessRevenueNotApproveTotal() {
			return processRevenueNotApproveTotal;
		}

		public void setProcessRevenueNotApproveTotal(Double processRevenueNotApproveTotal) {
			this.processRevenueNotApproveTotal = processRevenueNotApproveTotal;
		}
		
		//Huy-end
}
