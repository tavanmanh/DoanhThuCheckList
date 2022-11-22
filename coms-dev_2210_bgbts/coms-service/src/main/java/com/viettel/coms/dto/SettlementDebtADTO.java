package com.viettel.coms.dto;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;
import com.viettel.service.base.model.BaseFWModelImpl;

public class SettlementDebtADTO extends ComsBaseFWDTO<BaseFWModelImpl> {

	private String bpartnerDrValue;
	private String bpartnerCrName;
	private String projectCrName;
	private String contractName;
	private String constructionValue;
	private String productCrValue;
	private String productCrName;
	private String unitTypeName;
	private Double amountErp;
	private Double moneyErp;
	private Double amountTH;
	private Double moneyTH;
	// @JsonSerialize(using = JsonDateSerializerDate.class)
	// @JsonDeserialize(using = JsonDateDeserializer.class)
	private String dateAcct;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date dateFrom;
	@JsonSerialize(using = JsonDateSerializerDate.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date dateTo;
	private String bpartnerDrName;
	private Double amountTHDu;
	private Double moneyTHDu;
	private Double amountTHNhap;
	private Double moneyTHNhap;

	public Double getAmountTHDu() {
		return amountTHDu;
	}

	public void setAmountTHDu(Double amountTHDu) {
		this.amountTHDu = amountTHDu;
	}

	public Double getMoneyTHDu() {
		return moneyTHDu;
	}

	public void setMoneyTHDu(Double moneyTHDu) {
		this.moneyTHDu = moneyTHDu;
	}

	public Double getAmountTHNhap() {
		return amountTHNhap;
	}

	public void setAmountTHNhap(Double amountTHNhap) {
		this.amountTHNhap = amountTHNhap;
	}

	public Double getMoneyTHNhap() {
		return moneyTHNhap;
	}

	public void setMoneyTHNhap(Double moneyTHNhap) {
		this.moneyTHNhap = moneyTHNhap;
	}

	public String getBpartnerDrName() {
		return bpartnerDrName;
	}

	public void setBpartnerDrName(String bpartnerDrName) {
		this.bpartnerDrName = bpartnerDrName;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getConstructionValue() {
		return constructionValue;
	}

	public void setConstructionValue(String constructionValue) {
		this.constructionValue = constructionValue;
	}

	public String getUnitTypeName() {
		return unitTypeName;
	}

	public void setUnitTypeName(String unitTypeName) {
		this.unitTypeName = unitTypeName;
	}

	public String getBpartnerDrValue() {
		return bpartnerDrValue;
	}

	public void setBpartnerDrValue(String bpartnerDrValue) {
		this.bpartnerDrValue = bpartnerDrValue;
	}

	public String getBpartnerCrName() {
		return bpartnerCrName;
	}

	public void setBpartnerCrName(String bpartnerCrName) {
		this.bpartnerCrName = bpartnerCrName;
	}

	public String getProjectCrName() {
		return projectCrName;
	}

	public void setProjectCrName(String projectCrName) {
		this.projectCrName = projectCrName;
	}

	public String getProductCrValue() {
		return productCrValue;
	}

	public void setProductCrValue(String productCrValue) {
		this.productCrValue = productCrValue;
	}

	public String getProductCrName() {
		return productCrName;
	}

	public void setProductCrName(String productCrName) {
		this.productCrName = productCrName;
	}

	public String getDateAcct() {
		return dateAcct;
	}

	public void setDateAcct(String dateAcct) {
		this.dateAcct = dateAcct;
	}

	public Double getAmountErp() {
		return amountErp;
	}

	public void setAmountErp(Double amountErp) {
		if (amountErp == null) {
			this.amountErp = 0D;
		} else {
			this.amountErp = amountErp;
		}
	}

	public Double getMoneyErp() {
		return moneyErp;
	}

	public void setMoneyErp(Double moneyErp) {
		if (moneyErp == null) {
			this.moneyErp = 0D;
		} else {
			this.moneyErp = moneyErp;
		}
	}

	public Double getAmountTH() {
		return amountTH;
	}

	public void setAmountTH(Double amountTH) {
		this.amountTH = amountTH;
	}

	public Double getMoneyTH() {
		return moneyTH;
	}

	public void setMoneyTH(Double moneyTH) {
		this.moneyTH = moneyTH;
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
	public BaseFWModelImpl toModel() {
		// TODO Auto-generated method stub
		return null;
	}

}
