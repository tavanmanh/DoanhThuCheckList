package com.viettel.coms.dto;

import java.util.List;

public class WoGetListGoodsDTORequest {
    private String type;
    private List<Long> lstId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Long> getLstId() {
        return lstId;
    }

    public void setLstId(List<Long> lstId) {
        this.lstId = lstId;
    }
}
