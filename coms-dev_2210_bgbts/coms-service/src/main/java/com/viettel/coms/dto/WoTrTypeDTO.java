package com.viettel.coms.dto;
import com.viettel.coms.bo.WoTrTypeBO;
import com.viettel.coms.dto.ComsBaseFWDTO;

public class WoTrTypeDTO extends ComsBaseFWDTO<WoTrTypeBO>{
    private Long woTrTypeId;
    private String trTypeCode;
    private String trTypeName;
    private int status;

    public Long getWoTrTypeId() {
        return woTrTypeId;
    }

    public void setWoTrTypeId(Long woTrTypeId) {
        this.woTrTypeId = woTrTypeId;
    }

    public String getTrTypeCode() {
        return trTypeCode;
    }

    public void setTrTypeCode(String trTypeCode) {
        this.trTypeCode = trTypeCode;
    }

    public String getTrTypeName() {
        return trTypeName;
    }

    public void setTrTypeName(String trTypeName) {
        this.trTypeName = trTypeName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    @Override
    public WoTrTypeBO toModel() {
        WoTrTypeBO woTrTypeBo = new WoTrTypeBO();
        woTrTypeBo.setWoTrTypeId(this.woTrTypeId);
        woTrTypeBo.setTrTypeCode(this.trTypeCode);
        woTrTypeBo.setTrTypeName(this.trTypeName);
        woTrTypeBo.setStatus(this.status);
        return woTrTypeBo;
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
