package com.viettel.coms.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.viettel.coms.dto.ManageDataOutsideOsDTO;
import com.viettel.service.base.dto.BaseFWDTOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

@Entity
@Table(name = "MANAGE_DATA_OUTSIDE_OS")
public class ManageDataOutsideOsBO extends BaseFWModelImpl{

	@Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "MANAGE_DATA_OUTSIDE_OS_SEQ")})
	@Column(name = "MANAGE_DATA_OUTSIDE_OS_ID", length = 10)
	private Long manageDataOutsideOsId;
	@Column(name = "CONSTRUCTION_CODE")
	private String constructionCode;
	@Column(name = "STATION_CODE")
	private String stationCode;
	@Column(name = "PROVINCE_CODE")
	private String provinceCode;
	@Column(name = "HD_CONTRACT_CODE")
	private String hdContractCode;
	@Column(name = "HD_SIGN_DATE")
	private Date hdSignDate;
	@Column(name = "HD_CONTRACT_VALUE")
	private Double hdContractValue;
	@Column(name = "HD_PERFORM_DAY")
	private Long hdPerformDay;
	@Column(name = "CONSTRUCTION_TYPE")
	private String constructionType;
	@Column(name = "CONTENT")
	private String content;
	@Column(name = "CAPITAL_NTD")
	private String capitalNtd;
	@Column(name = "KHTC_SALARY")
	private Double khtcSalary;
	@Column(name = "KHTC_LABOR_OUTSOURCE")
	private Double khtcLaborOutsource;
	@Column(name = "KHTC_COST_MATERIAL")
	private Double khtcCostMaterial;
	@Column(name = "KHTC_COST_HSHC")
	private Double khtcCostHshc;
	@Column(name = "KHTC_COST_TRANSPORT")
	private Double khtcCostTransport;
	@Column(name = "KHTC_COST_ORTHER")
	private Double khtcCostOrther;
	@Column(name = "KHTC_DEPLOYMENT_MONTH")
	private String khtcDeploymentMonth;
	@Column(name = "KHTC_TOTAL_MONEY")
	private Double khtcTotalMoney;
	@Column(name = "KHTC_EFFECTIVE")
	private Double khtcEffective;
	@Column(name = "KHTC_DESCRIPTION")
	private String khtcDescription;
	@Column(name = "TU_ADVANCE_DATE")
	private Date tuAdvanceDate;
	@Column(name = "TU_LABOR")
	private Double tuLabor;
	@Column(name = "TU_MATERIAL")
	private Double tuMaterial;
	@Column(name = "TU_HSHC")
	private Double tuHshc;
	@Column(name = "TU_COST_TRANSPORT")
	private Double tuCostTransport;
	@Column(name = "TU_COST_ORTHER")
	private Double tuCostOrther;
	@Column(name = "VTA_SYNCHRONIZE_DATE")
	private Date vtaSynchronizeDate;
	@Column(name = "VTA_VALUE")
	private Double vtaValue;
	@Column(name = "GTSL_QUANTITY_VALUE")
	private Double gtslQuantityValue;
	@Column(name = "HTTC_TDT")
	private String httcTdt;
	@Column(name = "HTTC_TCTT")
	private String httcTctt;
	@Column(name = "HTTC_KN")
	private String httcKn;
	@Column(name = "TTTC_START_DATE")
	private Date tttcStartDate;
	@Column(name = "TTTC_END_DATE")
	private Date tttcEndDate;
	@Column(name = "TTTC_VUONG")
	private String tttcVuong;
	@Column(name = "TTTC_CLOSE")
	private String tttcClose;
	@Column(name = "GTSL_COMPLETE_EXPECTED_DATE")
	private Date gtslCompleteExpectedDate;
	@Column(name = "GTSL_DESCRIPTION")
	private String gtslDescription;
	@Column(name = "TDNT_HSHC_START_DATE")
	private Date tdntHshcStartDate;
	@Column(name = "TDNT_ACCEPTANCE_START_DATE")
	private Date tdntAcceptanceStartDate;
	@Column(name = "TDNT_KTHT_EXPERTISE_DATE")
	private Date tdntKthtExpertiseDate;
	@Column(name = "TDNT_4A_CONTROL_START_DATE")
	private Date tdnt4AControlStartDate;
	@Column(name = "TDNT_SIGN_PROVINCE_DATE")
	private Date tdntSignProvinceDate;
	@Column(name = "TDNT_SEND_TCT_DATE")
	private Date tdntSendTctDate;
	@Column(name = "TDNT_COMPLETE_EXPECTED_DATE")
	private Date tdntCompleteExpectedDate;
	@Column(name = "TDNT_VUONG_DATE")
	private Date tdntVuongDate;
	@Column(name = "TDNT_VUONG_REASON")
	private String tdntVuongReason;
	@Column(name = "DNQT_QT_CDT_VAT")
	private Double dnqtQtCdtVat;
	@Column(name = "DNQT_QT_CDT_NOT_VAT")
	private Double dnqtQtCdtNotVat;
	@Column(name = "DNQT_ELECTRICAL_PROCEDURES")
	private Double dnqtElectricalProcedures;
	@Column(name = "DNQT_PULL_CABLE_LABOR")
	private Double dnqtPullCableLabor;
	@Column(name = "DNQT_COST_MATERIAL")
	private Double dnqtCostMaterial;
	@Column(name = "DNQT_COST_HSHC")
	private Double dnqtCostHshc;
	@Column(name = "DNQT_COST_TRANSPORT_WAREHOUSE")
	private Double dnqtCostTransportWarehouse;
	@Column(name = "DNQT_COST_ORTHER")
	private Double dnqtCostOrther;
	@Column(name = "DNQT_SALARY_CABLE_ORTHER")
	private Double dnqtSalaryCableOrther;
	@Column(name = "DNQT_WELDING_SALARY")
	private Double dnqtWeldingSalary;
	@Column(name = "DNQT_VAT")
	private Double dnqtVat;
	@Column(name = "DNQT_TOTAL_MONEY")
	private Double dnqtTotalMoney;
	@Column(name = "GTTD_HSHC_HARD_DATE")
	private Date gttdHshcHardDate;
	@Column(name = "GTTD_COMPLETE_EXPERTISE_DATE")
	private Date gttdCompleteExpertiseDate;
	@Column(name = "GTTD_ELECTRICAL_PROCEDURES")
	private Double gttdElectricalProcedures;
	@Column(name = "GTTD_PULL_CABLE_LABOR")
	private Double gttdPullCableLabor;
	@Column(name = "GTTD_COST_MATERIAL")
	private Double gttdCostMaterial;
	@Column(name = "GTTD_COST_HSHC")
	private Double gttdCostHshc;
	@Column(name = "GTTD_COST_TRANSPORT_WAREHOUSE")
	private Double gttdCostTransportWarehouse;
	@Column(name = "GTTD_COST_ORTHER")
	private Double gttdCostOrther;
	@Column(name = "GTTD_SALARY_CABLE_ORTHER")
	private Double gttdSalaryCableOrther;
	@Column(name = "GTTD_WELDING_SALARY")
	private Double gttdWeldingSalary;
	@Column(name = "GTTD_VAT")
	private Double gttdVat;
	@Column(name = "GTTD_TOTAL_MONEY")
	private Double gttdTotalMoney;
	@Column(name = "GTTD_GTTD_PTK")
	private Double gttdGttdPtk;
	@Column(name = "GTTD_HSHC_MONTH")
	private String gttdHshcMonth;
	@Column(name = "GTTD_SALARY_MONTH")
	private String gttdSalaryMonth;
	@Column(name = "GTTD_SALARY_REAL")
	private Double gttdSalaryReal;
	@Column(name = "GTTD_HSHC_ERROR")
	private String gttdHshcError;
	@Column(name = "GTTD_ERROR_REASON")
	private String gttdErrorReason;
	@Column(name = "QTDN_SUGGESTIONS_DATE")
	private Date qtdnSuggestionsDate;
	@Column(name = "QTDN_VALUE")
	private Double qtdnValue;
	@Column(name = "QTDN_VTNET_DATE")
	private Date qtdnVtnetDate;
	@Column(name = "QTDN_DESCRIPTION")
	private String qtdnDescription;
	@Column(name = "QTTD_EXPERTISE_EMPLOYEE")
	private String qttdExpertiseEmployee;
	@Column(name = "QTTD_EXPERTISE_COMPLETE_DATE")
	private Date qttdExpertiseCompleteDate;
	@Column(name = "QTTD_VALUE")
	private Double qttdValue;
	@Column(name = "QTTD_DESCRIPTION")
	private String qttdDescription;
	@Column(name = "XHD_PTC_DATE")
	private Date xhdPtcDate;
	@Column(name = "XHD_XHD_DATE")
	private Date xhdXhdDate;
	@Column(name = "XHD_SO_HD")
	private String xhdSoHd;
	@Column(name = "XHD_REVENUE_MONTH")
	private String xhdRevenueMonth;
	@Column(name = "XHD_DESCRIPTION")
	private String xhdDescription;
	@Column(name = "TL_SIGN_DATE")
	private Date tlSignDate;
	@Column(name = "TL_VALUE")
	private Double tlValue;
	@Column(name = "TL_DESCRIPTION")
	private String tlDescription;
	@Column(name = "TL_DIFFERENCE_QUANTITY")
	private Double tlDifferenceQuantity;
	@Column(name = "TL_RATE")
	private Double tlRate;
	@Column(name = "QTNC_PHT_DATE")
	private Date qtncPhtDate;
	@Column(name = "QTNC_PTC_DATE")
	private Date qtncPtcDate;
	@Column(name = "QTNC_VTA_ACCOUNT_DATE")
	private Date qtncVtaAccountDate;
	@Column(name = "QTNC_TAKE_MONEY_DATE")
	private Date qtncTakeMoneyDate;
	@Column(name = "QTNC_VUONG")
	private String qtncVuong;
	@Column(name = "DESCRIPTION")
	private String description;
	@Column(name = "STATUS")
	private String status;
	@Column(name = "CREATED_USER_ID")
	private Long createdUserId;
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	@Column(name = "SCHEDULED_USER_ID")
	private Long scheduledUserId;
	@Column(name = "SCHEDULED_UPDATED_ID")
	private Long scheduledUpdatedId;
	@Column(name = "SUGGESTED_USER_ID")
	private Long suggestedUserId;
	@Column(name = "SUGGESTED_UPDATED_USER_ID")
	private Long suggestedUpdatedUserId;
	@Column(name = "EXPERTISED_USER_ID")
	private Long expertisedUserId;
	@Column(name = "EXPERTISED_UPDATED_USER_ID")
	private Long expertisedUpdatedUserId;
	@Column(name = "SETTLEMENTED_USER_ID")
	private Long settlementedUserId;
	@Column(name = "SETTLEMENTED_UPDATED_USER_ID")
	private Long settlementedUpdatedUserId;
	@Column(name = "INVOICE_USER_ID")
	private Long invoiceUserId;
	@Column(name = "INVOICE_UPDATED_USER_ID")
	private Long invoiceUpdatedUserId;
	@Column(name = "LIQUIDATED_USER_ID")
	private Long liquidatedUserId;
	@Column(name = "LIQUIDATED_UPDATED_USER_ID")
	private Long liquidatedUpdatedUserId;
	@Column(name = "LABOR_USER_ID")
	private Long laborUserId;
	@Column(name = "LABOR_UPDATED_USER_ID")
	private Long laborUpdatedUserId;
	@Column(name = "UPDATED_USER_ID")
	private Long updatedUserId;
	
	public Long getUpdatedUserId() {
		return updatedUserId;
	}



	public void setUpdatedUserId(Long updatedUserId) {
		this.updatedUserId = updatedUserId;
	}



	public Long getCreatedUserId() {
		return createdUserId;
	}



	public void setCreatedUserId(Long createdUserId) {
		this.createdUserId = createdUserId;
	}



	public Date getCreatedDate() {
		return createdDate;
	}



	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}



	public Long getScheduledUserId() {
		return scheduledUserId;
	}



	public void setScheduledUserId(Long scheduledUserId) {
		this.scheduledUserId = scheduledUserId;
	}



	public Long getScheduledUpdatedId() {
		return scheduledUpdatedId;
	}



	public void setScheduledUpdatedId(Long scheduledUpdatedId) {
		this.scheduledUpdatedId = scheduledUpdatedId;
	}



	public Long getSuggestedUserId() {
		return suggestedUserId;
	}



	public void setSuggestedUserId(Long suggestedUserId) {
		this.suggestedUserId = suggestedUserId;
	}



	public Long getSuggestedUpdatedUserId() {
		return suggestedUpdatedUserId;
	}



	public void setSuggestedUpdatedUserId(Long suggestedUpdatedUserId) {
		this.suggestedUpdatedUserId = suggestedUpdatedUserId;
	}



	public Long getExpertisedUserId() {
		return expertisedUserId;
	}



	public void setExpertisedUserId(Long expertisedUserId) {
		this.expertisedUserId = expertisedUserId;
	}



	public Long getExpertisedUpdatedUserId() {
		return expertisedUpdatedUserId;
	}



	public void setExpertisedUpdatedUserId(Long expertisedUpdatedUserId) {
		this.expertisedUpdatedUserId = expertisedUpdatedUserId;
	}



	public Long getSettlementedUserId() {
		return settlementedUserId;
	}



	public void setSettlementedUserId(Long settlementedUserId) {
		this.settlementedUserId = settlementedUserId;
	}



	public Long getSettlementedUpdatedUserId() {
		return settlementedUpdatedUserId;
	}



	public void setSettlementedUpdatedUserId(Long settlementedUpdatedUserId) {
		this.settlementedUpdatedUserId = settlementedUpdatedUserId;
	}



	public Long getInvoiceUserId() {
		return invoiceUserId;
	}



	public void setInvoiceUserId(Long invoiceUserId) {
		this.invoiceUserId = invoiceUserId;
	}



	public Long getInvoiceUpdatedUserId() {
		return invoiceUpdatedUserId;
	}



	public void setInvoiceUpdatedUserId(Long invoiceUpdatedUserId) {
		this.invoiceUpdatedUserId = invoiceUpdatedUserId;
	}



	public Long getLiquidatedUserId() {
		return liquidatedUserId;
	}



	public void setLiquidatedUserId(Long liquidatedUserId) {
		this.liquidatedUserId = liquidatedUserId;
	}



	public Long getLiquidatedUpdatedUserId() {
		return liquidatedUpdatedUserId;
	}



	public void setLiquidatedUpdatedUserId(Long liquidatedUpdatedUserId) {
		this.liquidatedUpdatedUserId = liquidatedUpdatedUserId;
	}



	public Long getLaborUserId() {
		return laborUserId;
	}



	public void setLaborUserId(Long laborUserId) {
		this.laborUserId = laborUserId;
	}



	public Long getLaborUpdatedUserId() {
		return laborUpdatedUserId;
	}



	public void setLaborUpdatedUserId(Long laborUpdatedUserId) {
		this.laborUpdatedUserId = laborUpdatedUserId;
	}



	public Long getManageDataOutsideOsId() {
		return manageDataOutsideOsId;
	}



	public void setManageDataOutsideOsId(Long manageDataOutsideOsId) {
		this.manageDataOutsideOsId = manageDataOutsideOsId;
	}



	public String getConstructionCode() {
		return constructionCode;
	}



	public void setConstructionCode(String constructionCode) {
		this.constructionCode = constructionCode;
	}



	public String getStationCode() {
		return stationCode;
	}



	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}



	public String getProvinceCode() {
		return provinceCode;
	}



	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}



	public String getHdContractCode() {
		return hdContractCode;
	}



	public void setHdContractCode(String hdContractCode) {
		this.hdContractCode = hdContractCode;
	}



	public Date getHdSignDate() {
		return hdSignDate;
	}



	public void setHdSignDate(Date hdSignDate) {
		this.hdSignDate = hdSignDate;
	}



	public Double getHdContractValue() {
		return hdContractValue;
	}



	public void setHdContractValue(Double hdContractValue) {
		this.hdContractValue = hdContractValue;
	}



	public Long getHdPerformDay() {
		return hdPerformDay;
	}



	public void setHdPerformDay(Long hdPerformDay) {
		this.hdPerformDay = hdPerformDay;
	}

	public String getContent() {
		return content;
	}


	public String getCapitalNtd() {
		return capitalNtd;
	}



	public void setCapitalNtd(String capitalNtd) {
		this.capitalNtd = capitalNtd;
	}



	public void setContent(String content) {
		this.content = content;
	}


	public Double getKhtcSalary() {
		return khtcSalary;
	}



	public void setKhtcSalary(Double khtcSalary) {
		this.khtcSalary = khtcSalary;
	}



	public Double getKhtcLaborOutsource() {
		return khtcLaborOutsource;
	}



	public void setKhtcLaborOutsource(Double khtcLaborOutsource) {
		this.khtcLaborOutsource = khtcLaborOutsource;
	}



	public Double getKhtcCostMaterial() {
		return khtcCostMaterial;
	}



	public void setKhtcCostMaterial(Double khtcCostMaterial) {
		this.khtcCostMaterial = khtcCostMaterial;
	}



	public Double getKhtcCostHshc() {
		return khtcCostHshc;
	}



	public void setKhtcCostHshc(Double khtcCostHshc) {
		this.khtcCostHshc = khtcCostHshc;
	}



	public Double getKhtcCostTransport() {
		return khtcCostTransport;
	}



	public void setKhtcCostTransport(Double khtcCostTransport) {
		this.khtcCostTransport = khtcCostTransport;
	}



	public Double getKhtcCostOrther() {
		return khtcCostOrther;
	}



	public void setKhtcCostOrther(Double khtcCostOrther) {
		this.khtcCostOrther = khtcCostOrther;
	}



	public String getKhtcDeploymentMonth() {
		return khtcDeploymentMonth;
	}



	public void setKhtcDeploymentMonth(String khtcDeploymentMonth) {
		this.khtcDeploymentMonth = khtcDeploymentMonth;
	}



	public Double getKhtcTotalMoney() {
		return khtcTotalMoney;
	}



	public void setKhtcTotalMoney(Double khtcTotalMoney) {
		this.khtcTotalMoney = khtcTotalMoney;
	}



	public Double getKhtcEffective() {
		return khtcEffective;
	}



	public void setKhtcEffective(Double khtcEffective) {
		this.khtcEffective = khtcEffective;
	}



	public String getKhtcDescription() {
		return khtcDescription;
	}



	public void setKhtcDescription(String khtcDescription) {
		this.khtcDescription = khtcDescription;
	}



	public Date getTuAdvanceDate() {
		return tuAdvanceDate;
	}



	public void setTuAdvanceDate(Date tuAdvanceDate) {
		this.tuAdvanceDate = tuAdvanceDate;
	}



	public Double getTuLabor() {
		return tuLabor;
	}



	public void setTuLabor(Double tuLabor) {
		this.tuLabor = tuLabor;
	}



	public Double getTuMaterial() {
		return tuMaterial;
	}



	public void setTuMaterial(Double tuMaterial) {
		this.tuMaterial = tuMaterial;
	}



	public Double getTuHshc() {
		return tuHshc;
	}



	public void setTuHshc(Double tuHshc) {
		this.tuHshc = tuHshc;
	}



	public Double getTuCostTransport() {
		return tuCostTransport;
	}



	public void setTuCostTransport(Double tuCostTransport) {
		this.tuCostTransport = tuCostTransport;
	}



	public Double getTuCostOrther() {
		return tuCostOrther;
	}



	public void setTuCostOrther(Double tuCostOrther) {
		this.tuCostOrther = tuCostOrther;
	}



	public Date getVtaSynchronizeDate() {
		return vtaSynchronizeDate;
	}



	public void setVtaSynchronizeDate(Date vtaSynchronizeDate) {
		this.vtaSynchronizeDate = vtaSynchronizeDate;
	}



	public Double getVtaValue() {
		return vtaValue;
	}



	public void setVtaValue(Double vtaValue) {
		this.vtaValue = vtaValue;
	}



	public Double getGtslQuantityValue() {
		return gtslQuantityValue;
	}



	public void setGtslQuantityValue(Double gtslQuantityValue) {
		this.gtslQuantityValue = gtslQuantityValue;
	}

	public String getHttcTdt() {
		return httcTdt;
	}



	public void setHttcTdt(String httcTdt) {
		this.httcTdt = httcTdt;
	}



	public String getHttcTctt() {
		return httcTctt;
	}



	public void setHttcTctt(String httcTctt) {
		this.httcTctt = httcTctt;
	}



	public String getHttcKn() {
		return httcKn;
	}



	public void setHttcKn(String httcKn) {
		this.httcKn = httcKn;
	}



	public Date getTttcStartDate() {
		return tttcStartDate;
	}



	public void setTttcStartDate(Date tttcStartDate) {
		this.tttcStartDate = tttcStartDate;
	}



	public Date getTttcEndDate() {
		return tttcEndDate;
	}



	public void setTttcEndDate(Date tttcEndDate) {
		this.tttcEndDate = tttcEndDate;
	}



	public String getTttcVuong() {
		return tttcVuong;
	}



	public void setTttcVuong(String tttcVuong) {
		this.tttcVuong = tttcVuong;
	}



	public String getTttcClose() {
		return tttcClose;
	}



	public void setTttcClose(String tttcClose) {
		this.tttcClose = tttcClose;
	}



	public Date getGtslCompleteExpectedDate() {
		return gtslCompleteExpectedDate;
	}



	public void setGtslCompleteExpectedDate(Date gtslCompleteExpectedDate) {
		this.gtslCompleteExpectedDate = gtslCompleteExpectedDate;
	}



	public String getGtslDescription() {
		return gtslDescription;
	}



	public void setGtslDescription(String gtslDescription) {
		this.gtslDescription = gtslDescription;
	}



	public Date getTdntHshcStartDate() {
		return tdntHshcStartDate;
	}



	public void setTdntHshcStartDate(Date tdntHshcStartDate) {
		this.tdntHshcStartDate = tdntHshcStartDate;
	}



	public Date getTdntAcceptanceStartDate() {
		return tdntAcceptanceStartDate;
	}



	public void setTdntAcceptanceStartDate(Date tdntAcceptanceStartDate) {
		this.tdntAcceptanceStartDate = tdntAcceptanceStartDate;
	}



	public Date getTdntKthtExpertiseDate() {
		return tdntKthtExpertiseDate;
	}



	public void setTdntKthtExpertiseDate(Date tdntKthtExpertiseDate) {
		this.tdntKthtExpertiseDate = tdntKthtExpertiseDate;
	}



	public Date getTdnt4AControlStartDate() {
		return tdnt4AControlStartDate;
	}



	public void setTdnt4AControlStartDate(Date tdnt4aControlStartDate) {
		tdnt4AControlStartDate = tdnt4aControlStartDate;
	}



	public Date getTdntSignProvinceDate() {
		return tdntSignProvinceDate;
	}



	public void setTdntSignProvinceDate(Date tdntSignProvinceDate) {
		this.tdntSignProvinceDate = tdntSignProvinceDate;
	}



	public Date getTdntSendTctDate() {
		return tdntSendTctDate;
	}



	public void setTdntSendTctDate(Date tdntSendTctDate) {
		this.tdntSendTctDate = tdntSendTctDate;
	}



	public Date getTdntCompleteExpectedDate() {
		return tdntCompleteExpectedDate;
	}



	public void setTdntCompleteExpectedDate(Date tdntCompleteExpectedDate) {
		this.tdntCompleteExpectedDate = tdntCompleteExpectedDate;
	}



	public Date getTdntVuongDate() {
		return tdntVuongDate;
	}



	public void setTdntVuongDate(Date tdntVuongDate) {
		this.tdntVuongDate = tdntVuongDate;
	}



	public String getTdntVuongReason() {
		return tdntVuongReason;
	}



	public void setTdntVuongReason(String tdntVuongReason) {
		this.tdntVuongReason = tdntVuongReason;
	}



	public Double getDnqtQtCdtVat() {
		return dnqtQtCdtVat;
	}



	public void setDnqtQtCdtVat(Double dnqtQtCdtVat) {
		this.dnqtQtCdtVat = dnqtQtCdtVat;
	}



	public Double getDnqtQtCdtNotVat() {
		return dnqtQtCdtNotVat;
	}



	public void setDnqtQtCdtNotVat(Double dnqtQtCdtNotVat) {
		this.dnqtQtCdtNotVat = dnqtQtCdtNotVat;
	}



	public Double getDnqtElectricalProcedures() {
		return dnqtElectricalProcedures;
	}



	public void setDnqtElectricalProcedures(Double dnqtElectricalProcedures) {
		this.dnqtElectricalProcedures = dnqtElectricalProcedures;
	}



	public Double getDnqtPullCableLabor() {
		return dnqtPullCableLabor;
	}



	public void setDnqtPullCableLabor(Double dnqtPullCableLabor) {
		this.dnqtPullCableLabor = dnqtPullCableLabor;
	}



	public Double getDnqtCostMaterial() {
		return dnqtCostMaterial;
	}



	public void setDnqtCostMaterial(Double dnqtCostMaterial) {
		this.dnqtCostMaterial = dnqtCostMaterial;
	}



	public Double getDnqtCostHshc() {
		return dnqtCostHshc;
	}



	public void setDnqtCostHshc(Double dnqtCostHshc) {
		this.dnqtCostHshc = dnqtCostHshc;
	}



	public Double getDnqtCostTransportWarehouse() {
		return dnqtCostTransportWarehouse;
	}



	public void setDnqtCostTransportWarehouse(Double dnqtCostTransportWarehouse) {
		this.dnqtCostTransportWarehouse = dnqtCostTransportWarehouse;
	}



	public Double getDnqtCostOrther() {
		return dnqtCostOrther;
	}



	public void setDnqtCostOrther(Double dnqtCostOrther) {
		this.dnqtCostOrther = dnqtCostOrther;
	}



	public Double getDnqtSalaryCableOrther() {
		return dnqtSalaryCableOrther;
	}



	public void setDnqtSalaryCableOrther(Double dnqtSalaryCableOrther) {
		this.dnqtSalaryCableOrther = dnqtSalaryCableOrther;
	}



	public Double getDnqtWeldingSalary() {
		return dnqtWeldingSalary;
	}



	public void setDnqtWeldingSalary(Double dnqtWeldingSalary) {
		this.dnqtWeldingSalary = dnqtWeldingSalary;
	}



	public Double getDnqtVat() {
		return dnqtVat;
	}



	public void setDnqtVat(Double dnqtVat) {
		this.dnqtVat = dnqtVat;
	}



	public Double getDnqtTotalMoney() {
		return dnqtTotalMoney;
	}



	public void setDnqtTotalMoney(Double dnqtTotalMoney) {
		this.dnqtTotalMoney = dnqtTotalMoney;
	}



	public Date getGttdHshcHardDate() {
		return gttdHshcHardDate;
	}



	public void setGttdHshcHardDate(Date gttdHshcHardDate) {
		this.gttdHshcHardDate = gttdHshcHardDate;
	}



	public Date getGttdCompleteExpertiseDate() {
		return gttdCompleteExpertiseDate;
	}



	public void setGttdCompleteExpertiseDate(Date gttdCompleteExpertiseDate) {
		this.gttdCompleteExpertiseDate = gttdCompleteExpertiseDate;
	}



	public Double getGttdElectricalProcedures() {
		return gttdElectricalProcedures;
	}



	public void setGttdElectricalProcedures(Double gttdElectricalProcedures) {
		this.gttdElectricalProcedures = gttdElectricalProcedures;
	}



	public Double getGttdPullCableLabor() {
		return gttdPullCableLabor;
	}



	public void setGttdPullCableLabor(Double gttdPullCableLabor) {
		this.gttdPullCableLabor = gttdPullCableLabor;
	}



	public Double getGttdCostMaterial() {
		return gttdCostMaterial;
	}



	public void setGttdCostMaterial(Double gttdCostMaterial) {
		this.gttdCostMaterial = gttdCostMaterial;
	}



	public Double getGttdCostHshc() {
		return gttdCostHshc;
	}



	public void setGttdCostHshc(Double gttdCostHshc) {
		this.gttdCostHshc = gttdCostHshc;
	}



	public Double getGttdCostTransportWarehouse() {
		return gttdCostTransportWarehouse;
	}



	public void setGttdCostTransportWarehouse(Double gttdCostTransportWarehouse) {
		this.gttdCostTransportWarehouse = gttdCostTransportWarehouse;
	}



	public Double getGttdCostOrther() {
		return gttdCostOrther;
	}



	public void setGttdCostOrther(Double gttdCostOrther) {
		this.gttdCostOrther = gttdCostOrther;
	}



	public Double getGttdSalaryCableOrther() {
		return gttdSalaryCableOrther;
	}



	public void setGttdSalaryCableOrther(Double gttdSalaryCableOrther) {
		this.gttdSalaryCableOrther = gttdSalaryCableOrther;
	}



	public Double getGttdWeldingSalary() {
		return gttdWeldingSalary;
	}



	public void setGttdWeldingSalary(Double gttdWeldingSalary) {
		this.gttdWeldingSalary = gttdWeldingSalary;
	}



	public Double getGttdVat() {
		return gttdVat;
	}



	public void setGttdVat(Double gttdVat) {
		this.gttdVat = gttdVat;
	}



	public Double getGttdTotalMoney() {
		return gttdTotalMoney;
	}



	public void setGttdTotalMoney(Double gttdTotalMoney) {
		this.gttdTotalMoney = gttdTotalMoney;
	}



	public Double getGttdGttdPtk() {
		return gttdGttdPtk;
	}



	public void setGttdGttdPtk(Double gttdGttdPtk) {
		this.gttdGttdPtk = gttdGttdPtk;
	}



	public String getGttdHshcMonth() {
		return gttdHshcMonth;
	}



	public void setGttdHshcMonth(String gttdHshcMonth) {
		this.gttdHshcMonth = gttdHshcMonth;
	}



	public String getGttdSalaryMonth() {
		return gttdSalaryMonth;
	}



	public void setGttdSalaryMonth(String gttdSalaryMonth) {
		this.gttdSalaryMonth = gttdSalaryMonth;
	}



	public Double getGttdSalaryReal() {
		return gttdSalaryReal;
	}



	public void setGttdSalaryReal(Double gttdSalaryReal) {
		this.gttdSalaryReal = gttdSalaryReal;
	}



	public String getGttdHshcError() {
		return gttdHshcError;
	}



	public void setGttdHshcError(String gttdHshcError) {
		this.gttdHshcError = gttdHshcError;
	}



	public String getGttdErrorReason() {
		return gttdErrorReason;
	}



	public void setGttdErrorReason(String gttdErrorReason) {
		this.gttdErrorReason = gttdErrorReason;
	}



	public Date getQtdnSuggestionsDate() {
		return qtdnSuggestionsDate;
	}



	public void setQtdnSuggestionsDate(Date qtdnSuggestionsDate) {
		this.qtdnSuggestionsDate = qtdnSuggestionsDate;
	}



	public Double getQtdnValue() {
		return qtdnValue;
	}



	public void setQtdnValue(Double qtdnValue) {
		this.qtdnValue = qtdnValue;
	}



	public Date getQtdnVtnetDate() {
		return qtdnVtnetDate;
	}



	public void setQtdnVtnetDate(Date qtdnVtnetDate) {
		this.qtdnVtnetDate = qtdnVtnetDate;
	}



	public String getQtdnDescription() {
		return qtdnDescription;
	}



	public void setQtdnDescription(String qtdnDescription) {
		this.qtdnDescription = qtdnDescription;
	}



	public String getQttdExpertiseEmployee() {
		return qttdExpertiseEmployee;
	}



	public void setQttdExpertiseEmployee(String qttdExpertiseEmployee) {
		this.qttdExpertiseEmployee = qttdExpertiseEmployee;
	}



	public Date getQttdExpertiseCompleteDate() {
		return qttdExpertiseCompleteDate;
	}



	public void setQttdExpertiseCompleteDate(Date qttdExpertiseCompleteDate) {
		this.qttdExpertiseCompleteDate = qttdExpertiseCompleteDate;
	}



	public Double getQttdValue() {
		return qttdValue;
	}



	public void setQttdValue(Double qttdValue) {
		this.qttdValue = qttdValue;
	}



	public String getQttdDescription() {
		return qttdDescription;
	}



	public void setQttdDescription(String qttdDescription) {
		this.qttdDescription = qttdDescription;
	}



	public Date getXhdPtcDate() {
		return xhdPtcDate;
	}



	public void setXhdPtcDate(Date xhdPtcDate) {
		this.xhdPtcDate = xhdPtcDate;
	}



	public Date getXhdXhdDate() {
		return xhdXhdDate;
	}



	public void setXhdXhdDate(Date xhdXhdDate) {
		this.xhdXhdDate = xhdXhdDate;
	}



	public String getXhdSoHd() {
		return xhdSoHd;
	}



	public void setXhdSoHd(String xhdSoHd) {
		this.xhdSoHd = xhdSoHd;
	}



	public String getXhdRevenueMonth() {
		return xhdRevenueMonth;
	}



	public void setXhdRevenueMonth(String xhdRevenueMonth) {
		this.xhdRevenueMonth = xhdRevenueMonth;
	}



	public String getXhdDescription() {
		return xhdDescription;
	}



	public void setXhdDescription(String xhdDescription) {
		this.xhdDescription = xhdDescription;
	}



	public Date getTlSignDate() {
		return tlSignDate;
	}



	public void setTlSignDate(Date tlSignDate) {
		this.tlSignDate = tlSignDate;
	}



	public Double getTlValue() {
		return tlValue;
	}



	public void setTlValue(Double tlValue) {
		this.tlValue = tlValue;
	}



	public String getTlDescription() {
		return tlDescription;
	}



	public void setTlDescription(String tlDescription) {
		this.tlDescription = tlDescription;
	}



	public Double getTlDifferenceQuantity() {
		return tlDifferenceQuantity;
	}



	public void setTlDifferenceQuantity(Double tlDifferenceQuantity) {
		this.tlDifferenceQuantity = tlDifferenceQuantity;
	}



	public Double getTlRate() {
		return tlRate;
	}



	public void setTlRate(Double tlRate) {
		this.tlRate = tlRate;
	}



	public Date getQtncPhtDate() {
		return qtncPhtDate;
	}



	public void setQtncPhtDate(Date qtncPhtDate) {
		this.qtncPhtDate = qtncPhtDate;
	}



	public Date getQtncPtcDate() {
		return qtncPtcDate;
	}



	public void setQtncPtcDate(Date qtncPtcDate) {
		this.qtncPtcDate = qtncPtcDate;
	}



	public Date getQtncVtaAccountDate() {
		return qtncVtaAccountDate;
	}



	public void setQtncVtaAccountDate(Date qtncVtaAccountDate) {
		this.qtncVtaAccountDate = qtncVtaAccountDate;
	}



	public Date getQtncTakeMoneyDate() {
		return qtncTakeMoneyDate;
	}



	public void setQtncTakeMoneyDate(Date qtncTakeMoneyDate) {
		this.qtncTakeMoneyDate = qtncTakeMoneyDate;
	}



	public String getQtncVuong() {
		return qtncVuong;
	}



	public void setQtncVuong(String qtncVuong) {
		this.qtncVuong = qtncVuong;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}


	public String getConstructionType() {
		return constructionType;
	}



	public void setConstructionType(String constructionType) {
		this.constructionType = constructionType;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	@Override
	public BaseFWDTOImpl toDTO() {
		// TODO Auto-generated method stub
		ManageDataOutsideOsDTO dto = new ManageDataOutsideOsDTO();
		dto.setManageDataOutsideOsId(this.getManageDataOutsideOsId());
		dto.setConstructionCode(this.getConstructionCode());
		dto.setStationCode(this.getStationCode());
		dto.setProvinceCode(this.getProvinceCode());
		dto.setHdContractCode(this.getHdContractCode());
		dto.setHdSignDate(this.getHdSignDate());
		dto.setHdContractValue(this.getHdContractValue());
		dto.setHdPerformDay(this.getHdPerformDay());
		dto.setConstructionType(this.getConstructionType());
		dto.setContent(this.getContent());
		dto.setCapitalNtd(this.getCapitalNtd());
		dto.setKhtcSalary(this.getKhtcSalary());
		dto.setKhtcLaborOutsource(this.getKhtcLaborOutsource());
		dto.setKhtcCostMaterial(this.getKhtcCostMaterial());
		dto.setKhtcCostHshc(this.getKhtcCostHshc());
		dto.setKhtcCostTransport(this.getKhtcCostTransport());
		dto.setKhtcCostOrther(this.getKhtcCostOrther());
		dto.setKhtcDeploymentMonth(this.getKhtcDeploymentMonth());
		dto.setKhtcTotalMoney(this.getKhtcTotalMoney());
		dto.setKhtcEffective(this.getKhtcEffective());
		dto.setKhtcDescription(this.getKhtcDescription());
		dto.setTuAdvanceDate(this.getTuAdvanceDate());
		dto.setTuLabor(this.getTuLabor());
		dto.setTuMaterial(this.getTuMaterial());
		dto.setTuHshc(this.getTuHshc());
		dto.setTuCostTransport(this.getTuCostTransport());
		dto.setTuCostOrther(this.getTuCostOrther());
		dto.setVtaSynchronizeDate(this.getVtaSynchronizeDate());
		dto.setVtaValue(this.getVtaValue());
		dto.setGtslQuantityValue(this.getGtslQuantityValue());
		dto.setHttcTdt(this.getHttcTdt());
		dto.setHttcTctt(this.getHttcTctt());
		dto.setHttcKn(this.getHttcKn());
		dto.setTttcStartDate(this.getTttcStartDate());
		dto.setTttcEndDate(this.getTttcEndDate());
		dto.setTttcVuong(this.getTttcVuong());
		dto.setTttcClose(this.getTttcClose());
		dto.setGtslCompleteExpectedDate(this.getGtslCompleteExpectedDate());
		dto.setGtslDescription(this.getGtslDescription());
		dto.setTdntHshcStartDate(this.getTdntHshcStartDate());
		dto.setTdntAcceptanceStartDate(this.getTdntAcceptanceStartDate());
		dto.setTdntKthtExpertiseDate(this.getTdntKthtExpertiseDate());
		dto.setTdnt4AControlStartDate(this.getTdnt4AControlStartDate());
		dto.setTdntSignProvinceDate(this.getTdntSignProvinceDate());
		dto.setTdntSendTctDate(this.getTdntSendTctDate());
		dto.setTdntCompleteExpectedDate(this.getTdntCompleteExpectedDate());
		dto.setTdntVuongDate(this.getTdntVuongDate());
		dto.setTdntVuongReason(this.getTdntVuongReason());
		dto.setDnqtQtCdtVat(this.getDnqtQtCdtVat());
		dto.setDnqtQtCdtNotVat(this.getDnqtQtCdtNotVat());
		dto.setDnqtElectricalProcedures(this.getDnqtElectricalProcedures());
		dto.setDnqtPullCableLabor(this.getDnqtPullCableLabor());
		dto.setDnqtCostMaterial(this.getDnqtCostMaterial());
		dto.setDnqtCostHshc(this.getDnqtCostHshc());
		dto.setDnqtCostTransportWarehouse(this.getDnqtCostTransportWarehouse());
		dto.setDnqtCostOrther(this.getDnqtCostOrther());
		dto.setDnqtSalaryCableOrther(this.getDnqtSalaryCableOrther());
		dto.setDnqtWeldingSalary(this.getDnqtWeldingSalary());
		dto.setDnqtVat(this.getDnqtVat());
		dto.setDnqtTotalMoney(this.getDnqtTotalMoney());
		dto.setGttdHshcHardDate(this.getGttdHshcHardDate());
		dto.setGttdCompleteExpertiseDate(this.getGttdCompleteExpertiseDate());
		dto.setGttdElectricalProcedures(this.getGttdElectricalProcedures());
		dto.setGttdPullCableLabor(this.getGttdPullCableLabor());
		dto.setGttdCostMaterial(this.getGttdCostMaterial());
		dto.setGttdCostHshc(this.getGttdCostHshc());
		dto.setGttdCostTransportWarehouse(this.getGttdCostTransportWarehouse());
		dto.setGttdCostOrther(this.getGttdCostOrther());
		dto.setGttdSalaryCableOrther(this.getGttdSalaryCableOrther());
		dto.setGttdWeldingSalary(this.getGttdWeldingSalary());
		dto.setGttdVat(this.getGttdVat());
		dto.setGttdTotalMoney(this.getGttdTotalMoney());
		dto.setGttdGttdPtk(this.getGttdGttdPtk());
		dto.setGttdHshcMonth(this.getGttdHshcMonth());
		dto.setGttdSalaryMonth(this.getGttdSalaryMonth());
		dto.setGttdSalaryReal(this.getGttdSalaryReal());
		dto.setGttdHshcError(this.getGttdHshcError());
		dto.setGttdErrorReason(this.getGttdErrorReason());
		dto.setQtdnSuggestionsDate(this.getQtdnSuggestionsDate());
		dto.setQtdnValue(this.getQtdnValue());
		dto.setQtdnVtnetDate(this.getQtdnVtnetDate());
		dto.setQtdnDescription(this.getQtdnDescription());
		dto.setQttdExpertiseEmployee(this.getQttdExpertiseEmployee());
		dto.setQttdExpertiseCompleteDate(this.getQttdExpertiseCompleteDate());
		dto.setQttdValue(this.getQttdValue());
		dto.setQttdDescription(this.getQttdDescription());
		dto.setXhdPtcDate(this.getXhdPtcDate());
		dto.setXhdXhdDate(this.getXhdXhdDate());
		dto.setXhdSoHd(this.getXhdSoHd());
		dto.setXhdRevenueMonth(this.getXhdRevenueMonth());
		dto.setXhdDescription(this.getXhdDescription());
		dto.setTlSignDate(this.getTlSignDate());
		dto.setTlValue(this.getTlValue());
		dto.setTlDescription(this.getTlDescription());
		dto.setTlDifferenceQuantity(this.getTlDifferenceQuantity());
		dto.setTlRate(this.getTlRate());
		dto.setQtncPhtDate(this.getQtncPhtDate());
		dto.setQtncPtcDate(this.getQtncPtcDate());
		dto.setQtncVtaAccountDate(this.getQtncVtaAccountDate());
		dto.setQtncTakeMoneyDate(this.getQtncTakeMoneyDate());
		dto.setQtncVuong(this.getQtncVuong());
		dto.setDescription(this.getDescription());
		dto.setStatus(this.getStatus());
		dto.setCreatedUserId(this.getCreatedUserId());
		dto.setCreatedDate(this.getCreatedDate());
		dto.setScheduledUserId(this.getScheduledUserId());
		dto.setScheduledUpdatedId(this.getScheduledUpdatedId());
		dto.setSuggestedUserId(this.getSuggestedUserId());
		dto.setSuggestedUpdatedUserId(this.getSuggestedUpdatedUserId());
		dto.setExpertisedUserId(this.getExpertisedUserId());
		dto.setExpertisedUpdatedUserId(this.getExpertisedUpdatedUserId());
		dto.setSettlementedUserId(this.getSettlementedUserId());
		dto.setSettlementedUpdatedUserId(this.getSettlementedUpdatedUserId());
		dto.setInvoiceUserId(this.getInvoiceUserId());
		dto.setInvoiceUpdatedUserId(this.getInvoiceUpdatedUserId());
		dto.setLiquidatedUserId(this.getLiquidatedUserId());
		dto.setLiquidatedUpdatedUserId(this.getLiquidatedUpdatedUserId());
		dto.setLaborUserId(this.getLaborUserId());
		dto.setLaborUpdatedUserId(this.getLaborUpdatedUserId());
		dto.setUpdatedUserId(this.updatedUserId);
		return dto;
	}

}
