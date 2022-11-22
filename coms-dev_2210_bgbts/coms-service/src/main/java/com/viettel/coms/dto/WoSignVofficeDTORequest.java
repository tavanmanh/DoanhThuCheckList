package com.viettel.coms.dto;

import java.util.List;

public class WoSignVofficeDTORequest {
    private List<Long> listId;
    private List<String> listBussinessType;
    private List<String> listEmail;
    private Long sysUserId;
    private String sysGroupName;
    private List<VofficeUserDTO> listSignVoffice;

    public List<Long> getListId() {
        return listId;
    }

    public void setListId(List<Long> listId) {
        this.listId = listId;
    }

    public List<String> getListBussinessType() {
        return listBussinessType;
    }

    public void setListBussinessType(List<String> listBussinessType) {
        this.listBussinessType = listBussinessType;
    }

    public List<String> getListEmail() {
        return listEmail;
    }

    public void setListEmail(List<String> listEmail) {
        this.listEmail = listEmail;
    }

    public Long getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Long sysUserId) {
        this.sysUserId = sysUserId;
    }

    public String getSysGroupName() {
        return sysGroupName;
    }

    public void setSysGroupName(String sysGroupName) {
        this.sysGroupName = sysGroupName;
    }

    public List<VofficeUserDTO> getListSignVoffice() {
        return listSignVoffice;
    }

    public void setListSignVoffice(List<VofficeUserDTO> listSignVoffice) {
        this.listSignVoffice = listSignVoffice;
    }
}
