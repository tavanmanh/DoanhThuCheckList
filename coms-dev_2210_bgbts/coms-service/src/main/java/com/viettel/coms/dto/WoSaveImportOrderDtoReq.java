package com.viettel.coms.dto;

import com.viettel.coms.dto.vttb.VoGoodsDTO;

import java.util.List;

public class WoSaveImportOrderDtoReq {

    private Long stockId;

    private String catStationCode;
    private Long catStationId;

    private Long cntContractId;
    private String contractCode;

    private Long deptReceiveId;
    private String deptReceiveName;

    private Long shipperId;
    private String shipperName;

    private Long createdBy;
    private String createdByName;

    private Long woOrderId;
    private List<VoGoodsDTO> listOrderGoodsDTO;

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public String getCatStationCode() {
        return catStationCode;
    }

    public void setCatStationCode(String catStationCode) {
        this.catStationCode = catStationCode;
    }

    public Long getCatStationId() {
        return catStationId;
    }

    public void setCatStationId(Long catStationId) {
        this.catStationId = catStationId;
    }

    public Long getCntContractId() {
        return cntContractId;
    }

    public void setCntContractId(Long cntContractId) {
        this.cntContractId = cntContractId;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Long getDeptReceiveId() {
        return deptReceiveId;
    }

    public void setDeptReceiveId(Long deptReceiveId) {
        this.deptReceiveId = deptReceiveId;
    }

    public String getDeptReceiveName() {
        return deptReceiveName;
    }

    public void setDeptReceiveName(String deptReceiveName) {
        this.deptReceiveName = deptReceiveName;
    }

    public Long getShipperId() {
        return shipperId;
    }

    public void setShipperId(Long shipperId) {
        this.shipperId = shipperId;
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public Long getWoOrderId() {
        return woOrderId;
    }

    public void setWoOrderId(Long woOrderId) {
        this.woOrderId = woOrderId;
    }

    public List<VoGoodsDTO> getListOrderGoodsDTO() {
        return listOrderGoodsDTO;
    }

    public void setListOrderGoodsDTO(List<VoGoodsDTO> listOrderGoodsDTO) {
        this.listOrderGoodsDTO = listOrderGoodsDTO;
    }
}
