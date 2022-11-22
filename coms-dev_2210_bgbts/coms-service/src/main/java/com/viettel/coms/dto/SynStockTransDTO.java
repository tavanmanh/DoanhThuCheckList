/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.coms.dto;

import com.viettel.coms.bo.SynStockTransBO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.persistence.Column;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;

/**
 * @author thuannht
 */
@XmlRootElement(name = "SYN_STOCK_TRANSBO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SynStockTransDTO extends ComsBaseFWDTO<SynStockTransBO> {

    private java.lang.Long synStockTransId;
    private java.lang.Long orderId;
    private java.lang.String orderCode;
    private java.lang.String code;
    private java.lang.String type;
    private java.lang.Long stockId;
    private java.lang.String status;
    private java.lang.String signState;
    private java.lang.Long fromStockTransId;
    private java.lang.String description;
    private java.lang.String createdByName;
    private java.lang.Long createdDeptId;
    private java.lang.String createdDeptName;
    private java.lang.Long updatedBy;
    private java.lang.Long stockTransId;
    private Long synType;
    private Long userLogin;
    private java.lang.String stockType;
    private java.lang.String confirmDescription;
    // CuongNV2 added start
// Tình trạng : stock_trans.state/syn_stock_trans.state
    private java.lang.String state;
    private java.lang.Long receiverId;
    private java.lang.String consCode;
    private java.lang.String synStockName;
    private java.lang.String synCreatedByName;
    private Date synCreatedDate;
    private java.lang.Long lastShipperId;
// CuongNV2 added end
//    hoanm1_20190306_start
    private java.lang.Long sysGroupIdConstruction;
    private java.lang.Double statusConstruction;
    
    //tatph-start-15/2/2020
    private List<UtilAttachDocumentDTO> filePaths;


	private java.lang.String realConfirmTransDate;
    private Long realRecieveAmount;
    private String realRecieveDate;
//    huypq30_20200526_start
//    List<SynStockTransDetailDTO> listSynStockTransDetailDto;
    List<AIOSynStockTransDetailDTO> listSynStockTransDetailDto;
    public List<AIOSynStockTransDetailDTO> getListSynStockTransDetailDto() {
		return listSynStockTransDetailDto;
	}

	public void setListSynStockTransDetailDto(List<AIOSynStockTransDetailDTO> listSynStockTransDetailDto) {
		this.listSynStockTransDetailDto = listSynStockTransDetailDto;
	}

//	public List<SynStockTransDetailDTO> getListSynStockTransDetailDto() {
//		return listSynStockTransDetailDto;
//	}
//
//	public void setListSynStockTransDetailDto(List<SynStockTransDetailDTO> listSynStockTransDetailDto) {
//		this.listSynStockTransDetailDto = listSynStockTransDetailDto;
//	}
//	huypq30_20200526_end
    

    public List<UtilAttachDocumentDTO> getFilePaths() {
		return filePaths;
	}

	public void setFilePaths(List<UtilAttachDocumentDTO> filePaths) {
		this.filePaths = filePaths;
	}

	public java.lang.String getRealConfirmTransDate() {
		return realConfirmTransDate;
	}

	public void setRealConfirmTransDate(java.lang.String realConfirmTransDate) {
		this.realConfirmTransDate = realConfirmTransDate;
	}
	
	public Long getRealRecieveAmount() {
		return realRecieveAmount;
	}

	public void setRealRecieveAmount(Long realRecieveAmount) {
		this.realRecieveAmount = realRecieveAmount;
	}

	public String getRealRecieveDate() {
		return realRecieveDate;
	}

	public void setRealRecieveDate(String realRecieveDate) {
		this.realRecieveDate = realRecieveDate;
	}

	public java.lang.Double getStatusConstruction() {
		return statusConstruction;
	}

	public void setStatusConstruction(java.lang.Double statusConstruction) {
		this.statusConstruction = statusConstruction;
	}

	public java.lang.Long getSysGroupIdConstruction() {
		return sysGroupIdConstruction;
	}

	public void setSysGroupIdConstruction(java.lang.Long sysGroupIdConstruction) {
		this.sysGroupIdConstruction = sysGroupIdConstruction;
	}
//	hoanm1_20190306_end
	public java.lang.Long getReceiverId() {
        return receiverId;
    }

    public java.lang.String getConsCode() {
        return consCode;
    }

    public void setConsCode(java.lang.String consCode) {
        this.consCode = consCode;
    }

    public java.lang.String getSynStockName() {
        return synStockName;
    }

    public void setSynStockName(java.lang.String synStockName) {
        this.synStockName = synStockName;
    }

    public java.lang.String getSynCreatedByName() {
        return synCreatedByName;
    }

    public void setSynCreatedByName(java.lang.String synCreatedByName) {
        this.synCreatedByName = synCreatedByName;
    }

    public void setReceiverId(java.lang.Long receiverId) {
        this.receiverId = receiverId;
    }

    public java.lang.String getConfirmDescription() {
        return confirmDescription;
    }

    public void setConfirmDescription(java.lang.String confirmDescription) {
        this.confirmDescription = confirmDescription;
    }

    public Long getSynType() {
        return synType;
    }

    public void setSynType(Long synType) {

        this.synType = synType;
    }

    public java.lang.Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(java.lang.Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    private java.util.Date updatedDate;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private java.util.Date realIeTransDate;
    private java.lang.String realIeUserId;
    private java.lang.String realIeUserName;
    private java.lang.Long shipperId;
    private java.lang.String shipperName;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private java.util.Date cancelDate;
    private java.lang.Long cancelBy;
    private java.lang.String cancelReasonName;
    private java.lang.String cancelDescription;
    private java.lang.String vofficeTransactionCode;
    private java.lang.String shipmentCode;
    private java.lang.String contractCode;
    private java.lang.String projectCode;
    private java.lang.Long custId;
    private java.lang.Long createdBy;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private java.util.Date createdDate;
    private java.lang.String cancelByName;
    private java.lang.String bussinessTypeName;
    private java.lang.String inRoal;
    private java.lang.String deptReceiveName;
    private java.lang.Long deptReceiveId;
    private java.lang.Long stockReceiveId;
    private java.lang.String stockReceiveCode;
    private java.lang.Long partnerId;
    private java.lang.Long typeExport;
    private java.lang.Long codeTinhTruong;

    public java.lang.Long getTypeExport() {
        return typeExport;
    }

    public void setTypeExport(java.lang.Long typeExport) {
        this.typeExport = typeExport;
    }

    private java.lang.String synTransType;
    private java.lang.String stockCode;
    private java.lang.String stockName;
    // Trạng thái: stock_trans.confirm/ syn_stock_trans.confirm
    private java.lang.String confirm;
    private String buss;

    //VietNT_20190116_start
    private String bussinessType;

    //dto only
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date dateFrom;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date dateTo;
    private String constructionCode;
    private Long sysGroupId;
    private String sysGroupName;
    private List<SynStockTransDTO> listToForward;
    // thong tin tinh truong
    private Long sysUserId;
    private String sysUserName;

    public String getBussinessType() {
        return bussinessType;
    }

    public void setBussinessType(String bussinessType) {
        this.bussinessType = bussinessType;
    }

    public List<SynStockTransDTO> getListToForward() {
        return listToForward;
    }

    public void setListToForward(List<SynStockTransDTO> listToForward) {
        this.listToForward = listToForward;
    }

    public Long getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Long sysUserId) {
        this.sysUserId = sysUserId;
    }

    public String getSysUserName() {
        return sysUserName;
    }

    public void setSysUserName(String sysUserName) {
        this.sysUserName = sysUserName;
    }

    public Long getSysGroupId() {
        return sysGroupId;
    }

    public void setSysGroupId(Long sysGroupId) {
        this.sysGroupId = sysGroupId;
    }

    public String getSysGroupName() {
        return sysGroupName;
    }

    public void setSysGroupName(String sysGroupName) {
        this.sysGroupName = sysGroupName;
    }

    public String getConstructionCode() {
        return constructionCode;
    }

    public void setConstructionCode(String constructionCode) {
        this.constructionCode = constructionCode;
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

    //VietNT_end

  //Huypq-08042019-start
    private Long destType;
    private Long reqType;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String catProvinceCode;
    private String catStationHouseCode;
    private String catStationCode;
    private String cntContractCode;
    private String goodsName;
    private String goodsCode;
    private String goodsUnitName;
    private String serial;
    private Double amount;
    private Double amountReal;
    private Double unitPrice;
    private Double thanhTien;
    private List<Integer> lstConfirm;
    private String realIeTransDateString;
    
    public String getRealIeTransDateString() {
		return realIeTransDateString;
	}

	public void setRealIeTransDateString(String realIeTransDateString) {
		this.realIeTransDateString = realIeTransDateString;
	}

	public List<Integer> getLstConfirm() {
		return lstConfirm;
	}

	public void setLstConfirm(List<Integer> lstConfirm) {
		this.lstConfirm = lstConfirm;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getCatProvinceCode() {
		return catProvinceCode;
	}

	public void setCatProvinceCode(String catProvinceCode) {
		this.catProvinceCode = catProvinceCode;
	}

	public String getCatStationHouseCode() {
		return catStationHouseCode;
	}

	public void setCatStationHouseCode(String catStationHouseCode) {
		this.catStationHouseCode = catStationHouseCode;
	}

	public String getCatStationCode() {
		return catStationCode;
	}

	public void setCatStationCode(String catStationCode) {
		this.catStationCode = catStationCode;
	}

	public String getCntContractCode() {
		return cntContractCode;
	}

	public void setCntContractCode(String cntContractCode) {
		this.cntContractCode = cntContractCode;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public String getGoodsUnitName() {
		return goodsUnitName;
	}

	public void setGoodsUnitName(String goodsUnitName) {
		this.goodsUnitName = goodsUnitName;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getAmountReal() {
		return amountReal;
	}

	public void setAmountReal(Double amountReal) {
		this.amountReal = amountReal;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getThanhTien() {
		return thanhTien;
	}

	public void setThanhTien(Double thanhTien) {
		this.thanhTien = thanhTien;
	}

	public Long getDestType() {
		return destType;
	}

	public void setDestType(Long destType) {
		this.destType = destType;
	}

	public Long getReqType() {
		return reqType;
	}

	public void setReqType(Long reqType) {
		this.reqType = reqType;
	}
	
    //Huy-end
    
	@Override
    public SynStockTransBO toModel() {
        SynStockTransBO synStockTransBO = new SynStockTransBO();
        synStockTransBO.setSynStockTransId(this.synStockTransId);
        synStockTransBO.setOrderId(this.orderId);
        synStockTransBO.setOrderCode(this.orderCode);
        synStockTransBO.setCode(this.code);
        synStockTransBO.setType(this.type);
        synStockTransBO.setStockId(this.stockId);
        synStockTransBO.setStatus(this.status);
        synStockTransBO.setSignState(this.signState);
        synStockTransBO.setFromStockTransId(this.fromStockTransId);
        synStockTransBO.setDescription(this.description);
        synStockTransBO.setCreatedByName(this.createdByName);
        synStockTransBO.setCreatedDeptId(this.createdDeptId);
        synStockTransBO.setCreatedDeptName(this.createdDeptName);
        synStockTransBO.setUpdatedBy(this.updatedBy);
        synStockTransBO.setUpdatedDate(this.updatedDate);
        synStockTransBO.setRealIeTransDate(this.realIeTransDate);
        synStockTransBO.setRealIeUserId(this.realIeUserId);
        synStockTransBO.setRealIeUserName(this.realIeUserName);
        synStockTransBO.setShipperId(this.shipperId);
        synStockTransBO.setShipperName(this.shipperName);
        synStockTransBO.setCancelDate(this.cancelDate);
        synStockTransBO.setCancelBy(this.cancelBy);
        synStockTransBO.setCancelReasonName(this.cancelReasonName);
        synStockTransBO.setCancelDescription(this.cancelDescription);
        synStockTransBO.setVofficeTransactionCode(this.vofficeTransactionCode);
        synStockTransBO.setShipmentCode(this.shipmentCode);
        synStockTransBO.setContractCode(this.contractCode);
        synStockTransBO.setProjectCode(this.projectCode);
        synStockTransBO.setCustId(this.custId);
        synStockTransBO.setCreatedBy(this.createdBy);
        synStockTransBO.setCreatedDate(this.createdDate);
        synStockTransBO.setCancelByName(this.cancelByName);
        synStockTransBO.setBussinessTypeName(this.bussinessTypeName);
        synStockTransBO.setInRoal(this.inRoal);
        synStockTransBO.setDeptReceiveName(this.deptReceiveName);
        synStockTransBO.setDeptReceiveId(this.deptReceiveId);
        synStockTransBO.setStockReceiveId(this.stockReceiveId);
        synStockTransBO.setStockReceiveCode(this.stockReceiveCode);
        synStockTransBO.setPartnerId(this.partnerId);
        synStockTransBO.setSynTransType(this.synTransType);
        synStockTransBO.setStockCode(this.stockCode);
        synStockTransBO.setStockName(this.stockName);
        synStockTransBO.setConfirm(this.confirm);
        synStockTransBO.setState(this.state);
        synStockTransBO.setDestType(this.destType);
        synStockTransBO.setReqType(reqType);
        synStockTransBO.setRealConfirmTransDate(this.realConfirmTransDate);
        return synStockTransBO;
    }

    @Override
    public Long getFWModelId() {
        return synStockTransId;
    }

    @Override
    public String catchName() {
        return getSynStockTransId().toString();
    }

    public java.lang.Long getSynStockTransId() {
        return synStockTransId;
    }

    public void setSynStockTransId(java.lang.Long synStockTransId) {
        this.synStockTransId = synStockTransId;
    }

    public java.lang.Long getOrderId() {
        return orderId;
    }

    public void setOrderId(java.lang.Long orderId) {
        this.orderId = orderId;
    }

    public java.lang.String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(java.lang.String orderCode) {
        this.orderCode = orderCode;
    }

    public java.lang.String getCode() {
        return code;
    }

    public void setCode(java.lang.String code) {
        this.code = code;
    }

    public java.lang.String getType() {
        return type;
    }

    public void setType(java.lang.String type) {
        this.type = type;
    }

    public java.lang.Long getStockId() {
        return stockId;
    }

    public void setStockId(java.lang.Long stockId) {
        this.stockId = stockId;
    }

    public java.lang.String getStatus() {
        return status;
    }

    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    public java.lang.String getSignState() {
        return signState;
    }

    public void setSignState(java.lang.String signState) {
        this.signState = signState;
    }

    public java.lang.Long getFromStockTransId() {
        return fromStockTransId;
    }

    public void setFromStockTransId(java.lang.Long fromStockTransId) {
        this.fromStockTransId = fromStockTransId;
    }

    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public java.lang.String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(java.lang.String createdByName) {
        this.createdByName = createdByName;
    }

    public java.lang.Long getCreatedDeptId() {
        return createdDeptId;
    }

    public void setCreatedDeptId(java.lang.Long createdDeptId) {
        this.createdDeptId = createdDeptId;
    }

    public java.lang.String getCreatedDeptName() {
        return createdDeptName;
    }

    public void setCreatedDeptName(java.lang.String createdDeptName) {
        this.createdDeptName = createdDeptName;
    }

    public java.lang.Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(java.lang.Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public java.util.Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(java.util.Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public java.util.Date getRealIeTransDate() {
        return realIeTransDate;
    }

    public void setRealIeTransDate(java.util.Date realIeTransDate) {
        this.realIeTransDate = realIeTransDate;
    }

    public java.lang.String getRealIeUserId() {
        return realIeUserId;
    }

    public void setRealIeUserId(java.lang.String realIeUserId) {
        this.realIeUserId = realIeUserId;
    }

    public java.lang.String getRealIeUserName() {
        return realIeUserName;
    }

    public void setRealIeUserName(java.lang.String realIeUserName) {
        this.realIeUserName = realIeUserName;
    }

    public java.lang.Long getShipperId() {
        return shipperId;
    }

    public void setShipperId(java.lang.Long shipperId) {
        this.shipperId = shipperId;
    }

    public java.lang.String getShipperName() {
        return shipperName;
    }

    public void setShipperName(java.lang.String shipperName) {
        this.shipperName = shipperName;
    }

    public java.util.Date getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(java.util.Date cancelDate) {
        this.cancelDate = cancelDate;
    }

    public java.lang.Long getCancelBy() {
        return cancelBy;
    }

    public void setCancelBy(java.lang.Long cancelBy) {
        this.cancelBy = cancelBy;
    }

    public java.lang.String getCancelReasonName() {
        return cancelReasonName;
    }

    public void setCancelReasonName(java.lang.String cancelReasonName) {
        this.cancelReasonName = cancelReasonName;
    }

    public java.lang.String getCancelDescription() {
        return cancelDescription;
    }

    public void setCancelDescription(java.lang.String cancelDescription) {
        this.cancelDescription = cancelDescription;
    }

    public java.lang.String getVofficeTransactionCode() {
        return vofficeTransactionCode;
    }

    public void setVofficeTransactionCode(java.lang.String vofficeTransactionCode) {
        this.vofficeTransactionCode = vofficeTransactionCode;
    }

    public java.lang.String getShipmentCode() {
        return shipmentCode;
    }

    public void setShipmentCode(java.lang.String shipmentCode) {
        this.shipmentCode = shipmentCode;
    }

    public java.lang.String getContractCode() {
        return contractCode;
    }

    public void setContractCode(java.lang.String contractCode) {
        this.contractCode = contractCode;
    }

    public java.lang.String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(java.lang.String projectCode) {
        this.projectCode = projectCode;
    }

    public java.lang.Long getCustId() {
        return custId;
    }

    public void setCustId(java.lang.Long custId) {
        this.custId = custId;
    }

    public java.lang.Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(java.lang.Long createdBy) {
        this.createdBy = createdBy;
    }

    public java.util.Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(java.util.Date createdDate) {
        this.createdDate = createdDate;
    }

    public java.lang.String getCancelByName() {
        return cancelByName;
    }

    public void setCancelByName(java.lang.String cancelByName) {
        this.cancelByName = cancelByName;
    }

    public java.lang.String getBussinessTypeName() {
        return bussinessTypeName;
    }

    public void setBussinessTypeName(java.lang.String bussinessTypeName) {
        this.bussinessTypeName = bussinessTypeName;
    }

    public java.lang.String getInRoal() {
        return inRoal;
    }

    public void setInRoal(java.lang.String inRoal) {
        this.inRoal = inRoal;
    }

    public java.lang.String getDeptReceiveName() {
        return deptReceiveName;
    }

    public void setDeptReceiveName(java.lang.String deptReceiveName) {
        this.deptReceiveName = deptReceiveName;
    }

    public java.lang.Long getDeptReceiveId() {
        return deptReceiveId;
    }

    public void setDeptReceiveId(java.lang.Long deptReceiveId) {
        this.deptReceiveId = deptReceiveId;
    }

    public java.lang.Long getStockReceiveId() {
        return stockReceiveId;
    }

    public void setStockReceiveId(java.lang.Long stockReceiveId) {
        this.stockReceiveId = stockReceiveId;
    }

    public java.lang.String getStockReceiveCode() {
        return stockReceiveCode;
    }

    public void setStockReceiveCode(java.lang.String stockReceiveCode) {
        this.stockReceiveCode = stockReceiveCode;
    }

    public java.lang.Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(java.lang.Long partnerId) {
        this.partnerId = partnerId;
    }

    public java.lang.String getSynTransType() {
        return synTransType;
    }

    public void setSynTransType(java.lang.String synTransType) {
        this.synTransType = synTransType;
    }

    public java.lang.String getStockCode() {
        return stockCode;
    }

    public void setStockCode(java.lang.String stockCode) {
        this.stockCode = stockCode;
    }

    public java.lang.String getStockName() {
        return stockName;
    }

    public void setStockName(java.lang.String stockName) {
        this.stockName = stockName;
    }

    public java.lang.String getConfirm() {
        return confirm;
    }

    public void setConfirm(java.lang.String confirm) {
        this.confirm = confirm;
    }

    private java.lang.Long constructionId;

    public java.lang.Long getConstructionId() {
        return constructionId;
    }

    public void setConstructionId(java.lang.Long constructionId) {
        this.constructionId = constructionId;
    }

    private java.lang.String isSyn;

    public java.lang.String getIsSyn() {
        return isSyn;
    }

    public void setIsSyn(java.lang.String isSyn) {
        this.isSyn = isSyn;
    }

    public String getBuss() {
        return buss;
    }

    public void setBuss(String buss) {
        this.buss = buss;
    }

    public java.lang.String getStockType() {
        return stockType;
    }

    public void setStockType(java.lang.String stockType) {
        this.stockType = stockType;
    }

    public Long getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(Long userLogin) {
        this.userLogin = userLogin;
    }

    public java.lang.Long getCodeTinhTruong() {
        return codeTinhTruong;
    }

    public void setCodeTinhTruong(java.lang.Long codeTinhTruong) {
        this.codeTinhTruong = codeTinhTruong;
    }

    public java.lang.String getState() {
        return state;
    }

    public void setState(java.lang.String state) {
        this.state = state;
    }

    public Date getSynCreatedDate() {
        return synCreatedDate;
    }

    public void setSynCreatedDate(Date synCreatedDate) {
        this.synCreatedDate = synCreatedDate;
    }

    public java.lang.Long getLastShipperId() {
        return lastShipperId;
    }

    public void setLastShipperId(java.lang.Long lastShipperId) {
        this.lastShipperId = lastShipperId;
    }
    
    //Huypq-20190905-start
    private String dayPXK;
    private String monthPXK;
    private String yearPXK;

	public String getDayPXK() {
		return dayPXK;
	}

	public void setDayPXK(String dayPXK) {
		this.dayPXK = dayPXK;
	}

	public String getMonthPXK() {
		return monthPXK;
	}

	public void setMonthPXK(String monthPXK) {
		this.monthPXK = monthPXK;
	}

	public String getYearPXK() {
		return yearPXK;
	}

	public void setYearPXK(String yearPXK) {
		this.yearPXK = yearPXK;
	}
    
    private Long synStockTransDetailId;

	public Long getSynStockTransDetailId() {
		return synStockTransDetailId;
	}

	public void setSynStockTransDetailId(Long synStockTransDetailId) {
		this.synStockTransDetailId = synStockTransDetailId;
	}
    
    private List<SynStockTransDTO> listSynStockTrans;

	public List<SynStockTransDTO> getListSynStockTrans() {
		return listSynStockTrans;
	}

	public void setListSynStockTrans(List<SynStockTransDTO> listSynStockTrans) {
		this.listSynStockTrans = listSynStockTrans;
	}

    private Long sysUserIdRecieve;

	public Long getSysUserIdRecieve() {
		return sysUserIdRecieve;
	}

	public void setSysUserIdRecieve(Long sysUserIdRecieve) {
		this.sysUserIdRecieve = sysUserIdRecieve;
	}
    
    private String tienBangChu;

	public String getTienBangChu() {
		return tienBangChu;
	}

	public void setTienBangChu(String tienBangChu) {
		this.tienBangChu = tienBangChu;
	}
    
	private Double sumTotalMoney;

	public Double getSumTotalMoney() {
		return sumTotalMoney;
	}

	public void setSumTotalMoney(Double sumTotalMoney) {
		this.sumTotalMoney = sumTotalMoney;
	}
	
	private String dayTX;
    private String monthTX;
    private String yearTX;

	public String getDayTX() {
		return dayTX;
	}

	public void setDayTX(String dayTX) {
		this.dayTX = dayTX;
	}

	public String getMonthTX() {
		return monthTX;
	}

	public void setMonthTX(String monthTX) {
		this.monthTX = monthTX;
	}

	public String getYearTX() {
		return yearTX;
	}

	public void setYearTX(String yearTX) {
		this.yearTX = yearTX;
	}

	private Date signDate;

	public Date getSignDate() {
		return signDate;
	}

	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	
	private List<String> provinceIdLst;

	public List<String> getProvinceIdLst() {
		return provinceIdLst;
	}

	public void setProvinceIdLst(List<String> provinceIdLst) {
		this.provinceIdLst = provinceIdLst;
	}
    //huy-end
//	hoanm1_20190919_start
	private String overDateKPI;

	public String getOverDateKPI() {
		return overDateKPI;
	}

	public void setOverDateKPI(String overDateKPI) {
		this.overDateKPI = overDateKPI;
	}
//	hoanm1_20190919_end
	
	//Huypq-20200207-start
	private List<UtilAttachDocumentDTO> listFileData;

	public List<UtilAttachDocumentDTO> getListFileData() {
		return listFileData;
	}

	public void setListFileData(List<UtilAttachDocumentDTO> listFileData) {
		this.listFileData = listFileData;
	}
	//Huy-end
	
	private String lastShipperName;

	public String getLastShipperName() {
		return lastShipperName;
	}

	public void setLastShipperName(String lastShipperName) {
		this.lastShipperName = lastShipperName;
	}
	//    20200523_hoanm1_vsmart_start
    private java.lang.String businessTypeName;
	private java.lang.String businessType;
	private java.lang.Long typeConfirm;
	
    public java.lang.Long getTypeConfirm() {
		return typeConfirm;
	}

	public void setTypeConfirm(java.lang.Long typeConfirm) {
		this.typeConfirm = typeConfirm;
	}

	public java.lang.String getBusinessTypeName() {
		return businessTypeName;
	}

	public void setBusinessTypeName(java.lang.String businessTypeName) {
		this.businessTypeName = businessTypeName;
	}

	public java.lang.String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(java.lang.String businessType) {
		this.businessType = businessType;
	}
	
//	20200523_hoanm1_vsmart_end
	
}
