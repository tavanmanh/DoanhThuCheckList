package com.viettel.coms.dto.avg;

import com.viettel.coms.dto.WoDTOResponse;

public class WoAvgRegisterResponseDTO extends WoDTOResponse {
    public Long getWoId() {
        return woId;
    }

    public void setWoId(Long woId) {
        this.woId = woId;
    }

    private Long woId;
}
