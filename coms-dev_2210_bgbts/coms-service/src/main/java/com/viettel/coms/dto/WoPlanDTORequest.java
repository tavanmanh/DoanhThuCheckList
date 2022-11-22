package com.viettel.coms.dto;

import java.util.List;
import java.util.Map;

public class WoPlanDTORequest {
    private SysUserRequest sysUserRequest;
    private long woPlanId;

    private WoPlanDTO woPlanDTO;
    private List<WoDTO> lstWosOfPlan;

    private Map<String, Object> filter;

    private Long page;
    private Integer pageSize;

    public SysUserRequest getSysUserRequest() {
        return sysUserRequest;
    }

    public void setSysUserRequest(SysUserRequest sysUserRequest) {
        this.sysUserRequest = sysUserRequest;
    }

    public long getWoPlanId() {
        return woPlanId;
    }

    public void setWoPlanId(long woPlanId) {
        this.woPlanId = woPlanId;
    }

    public WoPlanDTO getWoPlanDTO() {
        return woPlanDTO;
    }

    public void setWoPlanDTO(WoPlanDTO woPlanDTO) {
        this.woPlanDTO = woPlanDTO;
    }

    public List<WoDTO> getLstWosOfPlan() {
        return lstWosOfPlan;
    }

    public void setLstWosOfPlan(List<WoDTO> lstWosOfPlan) {
        this.lstWosOfPlan = lstWosOfPlan;
    }

    public Map<String, Object> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, Object> filter) {
        this.filter = filter;
    }

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
