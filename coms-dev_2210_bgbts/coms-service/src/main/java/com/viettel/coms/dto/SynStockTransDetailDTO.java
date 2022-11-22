/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.coms.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.viettel.coms.bo.SynStockTransDetailBO;

/**
 * @author thuannht
 */
@XmlRootElement(name = "SYN_STOCK_TRANS_DETAILBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SynStockTransDetailDTO extends ComsBaseFWDTO<SynStockTransDetailBO> {

    private java.lang.Long synStockTransDetailId;
    private java.lang.Long orderId;
    private java.lang.String goodsType;
    private java.lang.String goodsTypeName;
    private java.lang.Long goodsId;
    private java.lang.String goodsCode;
    private java.lang.String goodsName;
    private java.lang.String goodsIsSerial;
    private java.lang.String goodsState;
    private java.lang.String goodsStateName;
    private java.lang.String goodsUnitName;
    private java.lang.Long goodsUnitId;
    private java.lang.Double amountOrder;
    private java.lang.Double amountReal;
    private java.lang.Double totalPrice;
    private java.lang.Double amount;
    private java.lang.Long stockTransId;
    private java.lang.Long synStockTransId;
    private java.lang.Long idTable;
    private java.lang.Long typeExport;
    private java.lang.Long maxTransactionId;
    private java.lang.String filePath;
    
    //Huypq-20200205-start
    private java.lang.Double realRecieveAmount;
    private java.lang.String realRecieveDate;
    
    public java.lang.String getFilePath() {
		return filePath;
	}

	public void setFilePath(java.lang.String filePath) {
		this.filePath = filePath;
	}

	public java.lang.Long getTypeExport() {
        return typeExport;
    }

    public void setTypeExport(java.lang.Long typeExport) {
        this.typeExport = typeExport;
    }

    public java.lang.Long getIdTable() {
        return idTable;
    }

    public void setIdTable(java.lang.Long idTable) {
        this.idTable = idTable;
    }

    public java.lang.Long getSynStockTransId() {
        return synStockTransId;
    }

    public void setSynStockTransId(java.lang.Long synStockTransId) {
        this.synStockTransId = synStockTransId;
    }

    //Huypq-20200205-start
    
    public java.lang.Double getRealRecieveAmount() {
		return realRecieveAmount;
	}

	public void setRealRecieveAmount(java.lang.Double realRecieveAmount) {
		this.realRecieveAmount = realRecieveAmount;
	}

	public java.lang.String getRealRecieveDate() {
		return realRecieveDate;
	}

	public void setRealRecieveDate(java.lang.String realRecieveDate) {
		this.realRecieveDate = realRecieveDate;
	}
	
	private List<SynStockTransDetailDTO> listDataMaterial;
	
	public List<SynStockTransDetailDTO> getListDataMaterial() {
		return listDataMaterial;
	}

	public void setListDataMaterial(List<SynStockTransDetailDTO> listDataMaterial) {
		this.listDataMaterial = listDataMaterial;
	}

	//Huy-end
	
	@Override
    public SynStockTransDetailBO toModel() {
        SynStockTransDetailBO synStockTransDetailBO = new SynStockTransDetailBO();
        synStockTransDetailBO.setSynStockTransDetailId(this.synStockTransDetailId);
        synStockTransDetailBO.setOrderId(this.orderId);
        synStockTransDetailBO.setGoodsType(this.goodsType);
        synStockTransDetailBO.setGoodsTypeName(this.goodsTypeName);
        synStockTransDetailBO.setGoodsId(this.goodsId);
        synStockTransDetailBO.setGoodsCode(this.goodsCode);
        synStockTransDetailBO.setGoodsName(this.goodsName);
        synStockTransDetailBO.setGoodsIsSerial(this.goodsIsSerial);
        synStockTransDetailBO.setGoodsState(this.goodsState);
        synStockTransDetailBO.setGoodsStateName(this.goodsStateName);
        synStockTransDetailBO.setGoodsUnitName(this.goodsUnitName);
        synStockTransDetailBO.setGoodsUnitId(this.goodsUnitId);
        synStockTransDetailBO.setAmountOrder(this.amountOrder);
        synStockTransDetailBO.setAmountReal(this.amountReal);
        synStockTransDetailBO.setTotalPrice(this.totalPrice);
        synStockTransDetailBO.setStockTransId(this.stockTransId);
        synStockTransDetailBO.setAmount(this.amount);
        synStockTransDetailBO.setRealRecieveAmount(this.realRecieveAmount);
        synStockTransDetailBO.setRealRecieveDate(this.realRecieveDate);
        synStockTransDetailBO.setFilePath(this.filePath);
        return synStockTransDetailBO;
    }

    @Override
    public Long getFWModelId() {
        return synStockTransDetailId;
    }

    @Override
    public String catchName() {
        return getSynStockTransDetailId().toString();
    }

    public java.lang.Long getSynStockTransDetailId() {
        return synStockTransDetailId;
    }

    public void setSynStockTransDetailId(java.lang.Long synStockTransDetailId) {
        this.synStockTransDetailId = synStockTransDetailId;
    }

    public java.lang.Long getOrderId() {
        return orderId;
    }

    public void setOrderId(java.lang.Long orderId) {
        this.orderId = orderId;
    }

    public java.lang.String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(java.lang.String goodsType) {
        this.goodsType = goodsType;
    }

    public java.lang.String getGoodsTypeName() {
        return goodsTypeName;
    }

    public void setGoodsTypeName(java.lang.String goodsTypeName) {
        this.goodsTypeName = goodsTypeName;
    }

    public java.lang.Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(java.lang.Long goodsId) {
        this.goodsId = goodsId;
    }

    public java.lang.String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(java.lang.String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public java.lang.String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(java.lang.String goodsName) {
        this.goodsName = goodsName;
    }

    public java.lang.String getGoodsIsSerial() {
        return goodsIsSerial;
    }

    public void setGoodsIsSerial(java.lang.String goodsIsSerial) {
        this.goodsIsSerial = goodsIsSerial;
    }

    public java.lang.String getGoodsState() {
        return goodsState;
    }

    public void setGoodsState(java.lang.String goodsState) {
        this.goodsState = goodsState;
    }

    public java.lang.String getGoodsStateName() {
        return goodsStateName;
    }

    public void setGoodsStateName(java.lang.String goodsStateName) {
        this.goodsStateName = goodsStateName;
    }

    public java.lang.String getGoodsUnitName() {
        return goodsUnitName;
    }

    public void setGoodsUnitName(java.lang.String goodsUnitName) {
        this.goodsUnitName = goodsUnitName;
    }

    public java.lang.Long getGoodsUnitId() {
        return goodsUnitId;
    }

    public void setGoodsUnitId(java.lang.Long goodsUnitId) {
        this.goodsUnitId = goodsUnitId;
    }

    public java.lang.Double getAmountOrder() {
        return amountOrder;
    }

    public void setAmountOrder(java.lang.Double amountOrder) {
        this.amountOrder = amountOrder;
    }

    public java.lang.Double getAmountReal() {
        return amountReal;
    }

    public void setAmountReal(java.lang.Double amountReal) {
        this.amountReal = amountReal;
    }

    public java.lang.Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(java.lang.Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public java.lang.Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(java.lang.Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public java.lang.Double getAmount() {
        return amount;
    }

    public void setAmount(java.lang.Double amount) {
        this.amount = amount;
    }

    public java.lang.Long getMaxTransactionId() {
        return maxTransactionId;
    }

    public void setMaxTransactionId(java.lang.Long maxTransactionId) {
        this.maxTransactionId = maxTransactionId;
    }

  //huypq-20190905-start
    private Integer stt = 0;

	public Integer getStt() {
		return stt;
	}

	public void setStt(Integer stt) {
		this.stt = stt;
	}
    
	private Double totalMoney;

	public Double getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
	}
	
	private List<SynStockTransDTO> listSynStockTrans;

	public List<SynStockTransDTO> getListSynStockTrans() {
		return listSynStockTrans;
	}

	public void setListSynStockTrans(List<SynStockTransDTO> listSynStockTrans) {
		this.listSynStockTrans = listSynStockTrans;
	}
	
	private List<UtilAttachDocumentDTO> listFileData;

	public List<UtilAttachDocumentDTO> getListFileData() {
		return listFileData;
	}

	public void setListFileData(List<UtilAttachDocumentDTO> listFileData) {
		this.listFileData = listFileData;
	}
	
    //huy-end
	//    hoanm1_20200329_vsmart_start
    private java.lang.String goodsNameImport;

    public java.lang.String getGoodsNameImport() {
		return goodsNameImport;
	}

	public void setGoodsNameImport(java.lang.String goodsNameImport) {
		this.goodsNameImport = goodsNameImport;
	}
//	hoanm1_20200329_vsmart_end
}
