package com.viettel.coms.dto;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.coms.bo.ManageDataOutsideOsBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;

@XmlRootElement(name = "MANAGE_DATA_OUTSIDE_OSBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ManageDataOutsideOsDTO extends ComsBaseFWDTO<ManageDataOutsideOsBO>{

	private Long manageDataOutsideOsId;
	private String constructionCode;
	private String stationCode;
	private String provinceCode;
	private String hdContractCode;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date hdSignDate;
	private Double hdContractValue;
	private Long hdPerformDay;
	private String constructionType;
	private String content;
	private String capitalNtd;
	private Double khtcSalary;
	private Double khtcLaborOutsource;
	private Double khtcCostMaterial;
	private Double khtcCostHshc;
	private Double khtcCostTransport;
	private Double khtcCostOrther;
	private String khtcDeploymentMonth;
	private Double khtcTotalMoney;
	private Double khtcEffective;
	private String khtcDescription;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date tuAdvanceDate;
	private Double tuLabor;
	private Double tuMaterial;
	private Double tuHshc;
	private Double tuCostTransport;
	private Double tuCostOrther;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date vtaSynchronizeDate;
	private Double vtaValue;
	private Double gtslQuantityValue;
	private String httcTdt;
	private String httcTctt;
	private String httcKn;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date tttcStartDate;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date tttcEndDate;
	private String tttcVuong;
	private String tttcClose;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date gtslCompleteExpectedDate;
	private String gtslDescription;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date tdntHshcStartDate;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date tdntAcceptanceStartDate;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date tdntKthtExpertiseDate;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date tdnt4AControlStartDate;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date tdntSignProvinceDate;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date tdntSendTctDate;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date tdntCompleteExpectedDate;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date tdntVuongDate;
	private String tdntVuongReason;
	private Double dnqtQtCdtVat;
	private Double dnqtQtCdtNotVat;
	private Double dnqtElectricalProcedures;
	private Double dnqtPullCableLabor;
	private Double dnqtCostMaterial;
	private Double dnqtCostHshc;
	private Double dnqtCostTransportWarehouse;
	private Double dnqtCostOrther;
	private Double dnqtSalaryCableOrther;
	private Double dnqtWeldingSalary;
	private Double dnqtVat;
	private Double dnqtTotalMoney;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date gttdHshcHardDate;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date gttdCompleteExpertiseDate;
	private Double gttdElectricalProcedures;
	private Double gttdPullCableLabor;
	private Double gttdCostMaterial;
	private Double gttdCostHshc;
	private Double gttdCostTransportWarehouse;
	private Double gttdCostOrther;
	private Double gttdSalaryCableOrther;
	private Double gttdWeldingSalary;
	private Double gttdVat;
	private Double gttdTotalMoney;
	private Double gttdGttdPtk;
	private String gttdHshcMonth;
	private String gttdSalaryMonth;
	private Double gttdSalaryReal;
	private String gttdHshcError;
	private String gttdErrorReason;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date qtdnSuggestionsDate;
	private Double qtdnValue;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date qtdnVtnetDate;
	private String qtdnDescription;
	private String qttdExpertiseEmployee;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date qttdExpertiseCompleteDate;
	private Double qttdValue;
	private String qttdDescription;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date xhdPtcDate;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date xhdXhdDate;
	private String xhdSoHd;
	private String xhdRevenueMonth;
	private String xhdDescription;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date tlSignDate;
	private Double tlValue;
	private String tlDescription;
	private Double tlDifferenceQuantity;
	private Double tlRate;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date qtncPhtDate;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date qtncPtcDate;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date qtncVtaAccountDate;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date qtncTakeMoneyDate;
	private String qtncVuong;
	private String description;
	private String status;
	private String constructionName;
	
	private String constructionTypeName;
	private String capitalNtdName;
	private String statusName;
	
	private List<String> listStatus;
	private List<String> listHttc;
	
	private Long createdUserId;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date createdDate;
	private Long scheduledUserId;
	private Long scheduledUpdatedId;
	private Long suggestedUserId;
	private Long suggestedUpdatedUserId;
	private Long expertisedUserId;
	private Long expertisedUpdatedUserId;
	private Long settlementedUserId;
	private Long settlementedUpdatedUserId;
	private Long invoiceUserId;
	private Long invoiceUpdatedUserId;
	private Long liquidatedUserId;
	private Long liquidatedUpdatedUserId;
	private Long laborUserId;
	private Long laborUpdatedUserId;
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

	public List<String> getListHttc() {
		return listHttc;
	}

	public void setListHttc(List<String> listHttc) {
		this.listHttc = listHttc;
	}

	public List<String> getListStatus() {
		return listStatus;
	}

	public void setListStatus(List<String> listStatus) {
		this.listStatus = listStatus;
	}

	public String getConstructionTypeName() {
		return constructionTypeName;
	}

	public void setConstructionTypeName(String constructionTypeName) {
		this.constructionTypeName = constructionTypeName;
	}

	public String getCapitalNtdName() {
		return capitalNtdName;
	}

	public void setCapitalNtdName(String capitalNtdName) {
		this.capitalNtdName = capitalNtdName;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getConstructionName() {
		return constructionName;
	}

	public void setConstructionName(String constructionName) {
		this.constructionName = constructionName;
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

	public void setContent(String content) {
		this.content = content;
	}


	public String getCapitalNtd() {
		return capitalNtd;
	}

	public void setCapitalNtd(String capitalNtd) {
		this.capitalNtd = capitalNtd;
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
	public String catchName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getFWModelId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ManageDataOutsideOsBO toModel() {
		// TODO Auto-generated method stub
		ManageDataOutsideOsBO bo = new ManageDataOutsideOsBO();
		bo.setManageDataOutsideOsId(this.getManageDataOutsideOsId());
		bo.setConstructionCode(this.getConstructionCode());
		bo.setStationCode(this.getStationCode());
		bo.setProvinceCode(this.getProvinceCode());
		bo.setHdContractCode(this.getHdContractCode());
		bo.setHdSignDate(this.getHdSignDate());
		bo.setHdContractValue(this.getHdContractValue());
		bo.setHdPerformDay(this.getHdPerformDay());
		bo.setConstructionType(this.getConstructionType());
		bo.setContent(this.getContent());
		bo.setCapitalNtd(this.getCapitalNtd());
		bo.setKhtcSalary(this.getKhtcSalary());
		bo.setKhtcLaborOutsource(this.getKhtcLaborOutsource());
		bo.setKhtcCostMaterial(this.getKhtcCostMaterial());
		bo.setKhtcCostHshc(this.getKhtcCostHshc());
		bo.setKhtcCostTransport(this.getKhtcCostTransport());
		bo.setKhtcCostOrther(this.getKhtcCostOrther());
		bo.setKhtcDeploymentMonth(this.getKhtcDeploymentMonth());
		bo.setKhtcTotalMoney(this.getKhtcTotalMoney());
		bo.setKhtcEffective(this.getKhtcEffective());
		bo.setKhtcDescription(this.getKhtcDescription());
		bo.setTuAdvanceDate(this.getTuAdvanceDate());
		bo.setTuLabor(this.getTuLabor());
		bo.setTuMaterial(this.getTuMaterial());
		bo.setTuHshc(this.getTuHshc());
		bo.setTuCostTransport(this.getTuCostTransport());
		bo.setTuCostOrther(this.getTuCostOrther());
		bo.setVtaSynchronizeDate(this.getVtaSynchronizeDate());
		bo.setVtaValue(this.getVtaValue());
		bo.setGtslQuantityValue(this.getGtslQuantityValue());
		bo.setHttcTdt(this.getHttcTdt());
		bo.setHttcTctt(this.getHttcTctt());
		bo.setHttcKn(this.getHttcKn());
		bo.setTttcStartDate(this.getTttcStartDate());
		bo.setTttcEndDate(this.getTttcEndDate());
		bo.setTttcVuong(this.getTttcVuong());
		bo.setTttcClose(this.getTttcClose());
		bo.setGtslCompleteExpectedDate(this.getGtslCompleteExpectedDate());
		bo.setGtslDescription(this.getGtslDescription());
		bo.setTdntHshcStartDate(this.getTdntHshcStartDate());
		bo.setTdntAcceptanceStartDate(this.getTdntAcceptanceStartDate());
		bo.setTdntKthtExpertiseDate(this.getTdntKthtExpertiseDate());
		bo.setTdnt4AControlStartDate(this.getTdnt4AControlStartDate());
		bo.setTdntSignProvinceDate(this.getTdntSignProvinceDate());
		bo.setTdntSendTctDate(this.getTdntSendTctDate());
		bo.setTdntCompleteExpectedDate(this.getTdntCompleteExpectedDate());
		bo.setTdntVuongDate(this.getTdntVuongDate());
		bo.setTdntVuongReason(this.getTdntVuongReason());
		bo.setDnqtQtCdtVat(this.getDnqtQtCdtVat());
		bo.setDnqtQtCdtNotVat(this.getDnqtQtCdtNotVat());
		bo.setDnqtElectricalProcedures(this.getDnqtElectricalProcedures());
		bo.setDnqtPullCableLabor(this.getDnqtPullCableLabor());
		bo.setDnqtCostMaterial(this.getDnqtCostMaterial());
		bo.setDnqtCostHshc(this.getDnqtCostHshc());
		bo.setDnqtCostTransportWarehouse(this.getDnqtCostTransportWarehouse());
		bo.setDnqtCostOrther(this.getDnqtCostOrther());
		bo.setDnqtSalaryCableOrther(this.getDnqtSalaryCableOrther());
		bo.setDnqtWeldingSalary(this.getDnqtWeldingSalary());
		bo.setDnqtVat(this.getDnqtVat());
		bo.setDnqtTotalMoney(this.getDnqtTotalMoney());
		bo.setGttdHshcHardDate(this.getGttdHshcHardDate());
		bo.setGttdCompleteExpertiseDate(this.getGttdCompleteExpertiseDate());
		bo.setGttdElectricalProcedures(this.getGttdElectricalProcedures());
		bo.setGttdPullCableLabor(this.getGttdPullCableLabor());
		bo.setGttdCostMaterial(this.getGttdCostMaterial());
		bo.setGttdCostHshc(this.getGttdCostHshc());
		bo.setGttdCostTransportWarehouse(this.getGttdCostTransportWarehouse());
		bo.setGttdCostOrther(this.getGttdCostOrther());
		bo.setGttdSalaryCableOrther(this.getGttdSalaryCableOrther());
		bo.setGttdWeldingSalary(this.getGttdWeldingSalary());
		bo.setGttdVat(this.getGttdVat());
		bo.setGttdTotalMoney(this.getGttdTotalMoney());
		bo.setGttdGttdPtk(this.getGttdGttdPtk());
		bo.setGttdHshcMonth(this.getGttdHshcMonth());
		bo.setGttdSalaryMonth(this.getGttdSalaryMonth());
		bo.setGttdSalaryReal(this.getGttdSalaryReal());
		bo.setGttdHshcError(this.getGttdHshcError());
		bo.setGttdErrorReason(this.getGttdErrorReason());
		bo.setQtdnSuggestionsDate(this.getQtdnSuggestionsDate());
		bo.setQtdnValue(this.getQtdnValue());
		bo.setQtdnVtnetDate(this.getQtdnVtnetDate());
		bo.setQtdnDescription(this.getQtdnDescription());
		bo.setQttdExpertiseEmployee(this.getQttdExpertiseEmployee());
		bo.setQttdExpertiseCompleteDate(this.getQttdExpertiseCompleteDate());
		bo.setQttdValue(this.getQttdValue());
		bo.setQttdDescription(this.getQttdDescription());
		bo.setXhdPtcDate(this.getXhdPtcDate());
		bo.setXhdXhdDate(this.getXhdXhdDate());
		bo.setXhdSoHd(this.getXhdSoHd());
		bo.setXhdRevenueMonth(this.getXhdRevenueMonth());
		bo.setXhdDescription(this.getXhdDescription());
		bo.setTlSignDate(this.getTlSignDate());
		bo.setTlValue(this.getTlValue());
		bo.setTlDescription(this.getTlDescription());
		bo.setTlDifferenceQuantity(this.getTlDifferenceQuantity());
		bo.setTlRate(this.getTlRate());
		bo.setQtncPhtDate(this.getQtncPhtDate());
		bo.setQtncPtcDate(this.getQtncPtcDate());
		bo.setQtncVtaAccountDate(this.getQtncVtaAccountDate());
		bo.setQtncTakeMoneyDate(this.getQtncTakeMoneyDate());
		bo.setQtncVuong(this.getQtncVuong());
		bo.setDescription(this.getDescription());
		bo.setStatus(this.getStatus());
		bo.setCreatedUserId(this.getCreatedUserId());
		bo.setCreatedDate(this.getCreatedDate());
		bo.setScheduledUserId(this.getScheduledUserId());
		bo.setScheduledUpdatedId(this.getScheduledUpdatedId());
		bo.setSuggestedUserId(this.getSuggestedUserId());
		bo.setSuggestedUpdatedUserId(this.getSuggestedUpdatedUserId());
		bo.setExpertisedUserId(this.getExpertisedUserId());
		bo.setExpertisedUpdatedUserId(this.getExpertisedUpdatedUserId());
		bo.setSettlementedUserId(this.getSettlementedUserId());
		bo.setSettlementedUpdatedUserId(this.getSettlementedUpdatedUserId());
		bo.setInvoiceUserId(this.getInvoiceUserId());
		bo.setInvoiceUpdatedUserId(this.getInvoiceUpdatedUserId());
		bo.setLiquidatedUserId(this.getLiquidatedUserId());
		bo.setLiquidatedUpdatedUserId(this.getLiquidatedUpdatedUserId());
		bo.setLaborUserId(this.getLaborUserId());
		bo.setLaborUpdatedUserId(this.getLaborUpdatedUserId());
		bo.setUpdatedUserId(this.updatedUserId);
		return bo;
	}

}
