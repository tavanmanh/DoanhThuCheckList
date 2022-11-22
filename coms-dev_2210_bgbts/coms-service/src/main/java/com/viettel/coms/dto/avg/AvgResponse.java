package com.viettel.coms.dto.avg;

public class AvgResponse {
    public AvgResponseStatus getStatus() {
        return status;
    }

    public void setStatus(AvgResponseStatus status) {
        this.status = status;
    }

    public AvgResponseData getData() {
        return data;
    }

    public void setData(AvgResponseData data) {
        this.data = data;
    }

    private AvgResponseStatus status;
    private AvgResponseData data;
}
