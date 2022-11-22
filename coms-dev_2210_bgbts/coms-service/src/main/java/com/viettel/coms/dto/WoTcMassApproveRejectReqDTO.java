package com.viettel.coms.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WoTcMassApproveRejectReqDTO {
    private String newState;
    private String loggedInUser;
    private List<WoDTO> listWo;

    public String getNewState() {
        return newState;
    }

    public void setNewState(String newState) {
        this.newState = newState;
    }

    public String getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(String loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public List<WoDTO> getListWo() {
        return listWo;
    }

    public void setListWo(List<WoDTO> listWo) {
        this.listWo = listWo;
    }
}
