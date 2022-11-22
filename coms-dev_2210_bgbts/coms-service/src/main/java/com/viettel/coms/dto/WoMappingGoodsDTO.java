package com.viettel.coms.dto;

import com.viettel.coms.bo.WoMappingGoodsBO;

public class WoMappingGoodsDTO extends ComsBaseFWDTO<WoMappingGoodsBO> {
    private Long woMappingGoodsId;
    private Long goodsId;
    private Long woId;
    private String name;
    private Double amount;
    private String goodsUnitName;
    private Double amountNeed;
    private Double amountReal;
    private Long isSerial;
    private Long isUsed;
    private String serial;

    public Long getWoMappingGoodsId() {
        return woMappingGoodsId;
    }

    public void setWoMappingGoodsId(Long woMappingGoodsId) {
        this.woMappingGoodsId = woMappingGoodsId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Long getWoId() {
        return woId;
    }

    public void setWoId(Long woId) {
        this.woId = woId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getGoodsUnitName() {
        return goodsUnitName;
    }

    public void setGoodsUnitName(String goodsUnitName) {
        this.goodsUnitName = goodsUnitName;
    }

    public Double getAmountNeed() {
        return amountNeed;
    }

    public void setAmountNeed(Double amountNeed) {
        this.amountNeed = amountNeed;
    }

    public Double getAmountReal() {
        return amountReal;
    }

    public void setAmountReal(Double amountReal) {
        this.amountReal = amountReal;
    }

    public Long getIsSerial() {
        return isSerial;
    }

    public void setIsSerial(Long isSerial) {
        this.isSerial = isSerial;
    }

    public Long getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Long isUsed) {
        this.isUsed = isUsed;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    @Override
    public WoMappingGoodsBO toModel() {
        WoMappingGoodsBO bo = new WoMappingGoodsBO();

        bo.setWoMappingGoodsId(this.woMappingGoodsId);
        bo.setGoodsId(this.goodsId);
        bo.setWoId(this.woId);
        bo.setName(this.name);
        bo.setAmount(this.amount);
        bo.setGoodsUnitName(this.goodsUnitName);
        bo.setAmountNeed(this.amountNeed);
        bo.setAmountReal(this.amountReal);
        bo.setIsSerial(this.isSerial);
        bo.setIsUsed(this.isUsed);
        bo.setSerial(this.serial);

        return bo;
    }

    @Override
    public Long getFWModelId() {
        return null;
    }

    @Override
    public String catchName() {
        return null;
    }
}
