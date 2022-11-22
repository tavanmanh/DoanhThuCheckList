package com.viettel.coms.dto;

import com.viettel.asset.dto.ResultInfo;

import java.util.List;

public class WoPlanDTOResponse {
    private ResultInfo resultInfo;
    private SysUserRequest sysUser;

    private List<WoPlanDTO> listWoPlans;
    private List<WoDTO> lstWosOfPlan;

    public ResultInfo getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(ResultInfo resultInfo) {
        this.resultInfo = resultInfo;
    }

    public SysUserRequest getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUserRequest sysUser) {
        this.sysUser = sysUser;
    }

    public List<WoPlanDTO> getListWoPlans() {
        return listWoPlans;
    }

    public void setListWoPlans(List<WoPlanDTO> listWoPlans) {
        this.listWoPlans = listWoPlans;
    }

    public List<WoDTO> getLstWosOfPlan() {
        return lstWosOfPlan;
    }

    public void setLstWosOfPlan(List<WoDTO> lstWosOfPlan) {
        this.lstWosOfPlan = lstWosOfPlan;
    }
}
