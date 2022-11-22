package com.viettel.coms.dto.avg;

import com.viettel.asset.dto.ResultInfo;

public class WoAvgResponseDTO {
//    public int getError() {
//        return error;
//    }

//    public void setError(int error) {
//        this.error = error;
//    }

//    public String getPath() {
//        return path;
//    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
   // private int error;
   private String path;
    private String errorCode;
    private String errorDesc;
    private int status;
    private Object data;


}
