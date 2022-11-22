package com.viettel.coms.dto;

import java.util.Map;

public class WoComfirmAmsAssetDTORequest {
    private Long stockTransId;
    private Long createdBy;
    private String createdByName;
    private Map<String, Object> mapReq;

    private Long orderId;
    private String orderCode;

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
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

    public Map<String, Object> getMapReq() {
        return mapReq;
    }

    public void setMapReq(Map<String, Object> mapReq) {
        this.mapReq = mapReq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }
}
