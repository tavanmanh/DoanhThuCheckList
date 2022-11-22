package com.viettel.coms.bo;

import com.viettel.coms.dto.WoMappingGoodsDTO;
import com.viettel.service.base.model.BaseFWModelImpl;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@Entity
@Table(name = "WO_MAPPING_GOODS")
public class WoMappingGoodsBO extends BaseFWModelImpl {
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

    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence", value = "WO_MAPPING_GOODS_SEQ")})
    @Column(name = "ID")
    public Long getWoMappingGoodsId() {
        return woMappingGoodsId;
    }

    public void setWoMappingGoodsId(Long woMappingGoodsId) {
        this.woMappingGoodsId = woMappingGoodsId;
    }

    @Column(name = "GOODS_ID")
    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    @Column(name = "WO_ID")
    public Long getWoId() {
        return woId;
    }

    public void setWoId(Long woId) {
        this.woId = woId;
    }

    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "AMOUNT")
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Column(name = "GOODS_UNIT_NAME")
    public String getGoodsUnitName() {
        return goodsUnitName;
    }

    public void setGoodsUnitName(String goodsUnitName) {
        this.goodsUnitName = goodsUnitName;
    }

    @Column(name = "AMOUNT_NEED")
    public Double getAmountNeed() {
        return amountNeed;
    }

    public void setAmountNeed(Double amountNeed) {
        this.amountNeed = amountNeed;
    }

    @Column(name = "AMOUNT_REAL")
    public Double getAmountReal() {
        return amountReal;
    }

    public void setAmountReal(Double amountReal) {
        this.amountReal = amountReal;
    }

    @Column(name = "HAS_SERIAL")
    public Long getIsSerial() {
        return isSerial;
    }

    public void setIsSerial(Long isSerial) {
        this.isSerial = isSerial;
    }

    @Column(name = "IS_USED")
    public Long getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Long isUsed) {
        this.isUsed = isUsed;
    }

    @Column(name = "SERIAL")
    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    @Override
    public WoMappingGoodsDTO toDTO() {
        WoMappingGoodsDTO dto = new WoMappingGoodsDTO();

        dto.setWoMappingGoodsId(this.woMappingGoodsId);
        dto.setGoodsId(this.goodsId);
        dto.setWoId(this.woId);
        dto.setName(this.name);
        dto.setAmount(this.amount);
        dto.setGoodsUnitName(this.goodsUnitName);
        dto.setAmountNeed(this.amountNeed);
        dto.setAmountReal(this.amountReal);
        dto.setIsSerial(this.isSerial);
        dto.setIsUsed(this.isUsed);
        dto.setSerial(this.serial);

        return dto;
    }
}
