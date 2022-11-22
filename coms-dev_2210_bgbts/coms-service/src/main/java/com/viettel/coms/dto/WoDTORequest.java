package com.viettel.coms.dto;

import com.viettel.coms.dto.vttb.VoGoodsDTO;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WoDTORequest {
    private SysUserRequest sysUserRequest;
    private WoDTO woDTO;
    private Map<String, Object> filter;
    private List<WoMappingChecklistDTO> lstChecklistsOfWo;
    private String opinionType;
    private String opinionContent;
    private String opinionObject;
    private Long groupId;
    private Long woId;
    private Long checkListId;
    private List<WoChecklistDTO> lstChecklistsOfWoHcqt;

    private List<WoChecklistDTO> lstChecklistsOfWoAvg;
    private WoTrDTO woTrDTO;
    private Object outsideApiRequest;
    // Nhap xuat VTTB
    private Long catStockId;
    private Map<String, List<String>> mapEmails;
    private List<VoGoodsDTO> listOrderGoodsDTO;
    private List<VofficeUserDTO> lstVofficeUsers;
    private String keysearch;
    private String vofficePass;
    private Long page;
    private Integer pageSize;
    // TMBT
    private List<CatStationDTO> lstCatStation;
    // DBHT
    private Long classId;
    private String checklistName;
    //avg
    private String productCodeId;
    private String catStockName;

    public String getProductCodeId() {
        return productCodeId;
    }

    public void setProductCodeId(String productCodeId) {
        this.productCodeId = productCodeId;
    }

    public String getCatStockName() {
        return catStockName;
    }

    public void setCatStockName(String catStockName) {
        this.catStockName = catStockName;
    }

    public List<WoChecklistDTO> getLstChecklistsOfWoAvg() {
        return lstChecklistsOfWoAvg;
    }

    public void setLstChecklistsOfWoAvg(List<WoChecklistDTO> lstChecklistsOfWoAvg) {
        this.lstChecklistsOfWoAvg = lstChecklistsOfWoAvg;
    }

    public SysUserRequest getSysUserRequest() {
        return sysUserRequest;
    }

    public void setSysUserRequest(SysUserRequest sysUserRequest) {
        this.sysUserRequest = sysUserRequest;
    }

    public WoDTO getWoDTO() {
        return woDTO;
    }

    public void setWoDTO(WoDTO woDTO) {
        this.woDTO = woDTO;
    }

    public Map<String, Object> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, Object> filter) {
        this.filter = filter;
    }

    public List<WoMappingChecklistDTO> getLstChecklistsOfWo() {
        return lstChecklistsOfWo;
    }

    public void setLstChecklistsOfWo(List<WoMappingChecklistDTO> lstChecklistsOfWo) {
        this.lstChecklistsOfWo = lstChecklistsOfWo;
    }

    public String getOpinionType() {
        return opinionType;
    }

    public void setOpinionType(String opinionType) {
        this.opinionType = opinionType;
    }

    public String getOpinionContent() {
        return opinionContent;
    }

    public void setOpinionContent(String opinionContent) {
        this.opinionContent = opinionContent;
    }

    public String getOpinionObject() {
        return opinionObject;
    }

    public void setOpinionObject(String opinionObject) {
        this.opinionObject = opinionObject;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public List<WoChecklistDTO> getLstChecklistsOfWoHcqt() {
        return lstChecklistsOfWoHcqt;
    }

    public void setLstChecklistsOfWoHcqt(List<WoChecklistDTO> lstChecklistsOfWoHcqt) {
        this.lstChecklistsOfWoHcqt = lstChecklistsOfWoHcqt;
    }

    public Long getWoId() {
        return woId;
    }

    public void setWoId(Long woId) {
        this.woId = woId;
    }

    public Long getCheckListId() {
        return checkListId;
    }

    public void setCheckListId(Long checkListId) {
        this.checkListId = checkListId;
    }

    private String approvedState;

    public String getApprovedState() {
        return approvedState;
    }

    public void setApprovedState(String approvedState) {
        this.approvedState = approvedState;
    }

    private WoMappingChecklistDTO acceptChecklistObj;

    public WoMappingChecklistDTO getAcceptChecklistObj() {
        return acceptChecklistObj;
    }

    public void setAcceptChecklistObj(WoMappingChecklistDTO acceptChecklistObj) {
        this.acceptChecklistObj = acceptChecklistObj;
    }

    public WoTrDTO getWoTrDTO() {
        return woTrDTO;
    }

    public void setWoTrDTO(WoTrDTO woTrDTO) {
        this.woTrDTO = woTrDTO;
    }

    public Object getOutsideApiRequest() {
        return outsideApiRequest;
    }

    public void setOutsideApiRequest(Object outsideApiRequest) {
        this.outsideApiRequest = outsideApiRequest;
    }

    public Long getCatStockId() {
        return catStockId;
    }

    public void setCatStockId(Long catStockId) {
        this.catStockId = catStockId;
    }

    public Map<String, List<String>> getMapEmails() {
        return mapEmails;
    }

    public void setMapEmails(Map<String, List<String>> mapEmails) {
        this.mapEmails = mapEmails;
    }

    public List<VoGoodsDTO> getListOrderGoodsDTO() {
        return listOrderGoodsDTO;
    }

    public void setListOrderGoodsDTO(List<VoGoodsDTO> listOrderGoodsDTO) {
        this.listOrderGoodsDTO = listOrderGoodsDTO;
    }

    public List<VofficeUserDTO> getLstVofficeUsers() {
        return lstVofficeUsers;
    }

    public void setLstVofficeUsers(List<VofficeUserDTO> lstVofficeUsers) {
        this.lstVofficeUsers = lstVofficeUsers;
    }

    public String getKeysearch() {
        return keysearch;
    }

    public void setKeysearch(String keysearch) {
        this.keysearch = keysearch;
    }

    public String getVofficePass() {
        return vofficePass;
    }

    public void setVofficePass(String vofficePass) {
        this.vofficePass = vofficePass;
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

    public List<CatStationDTO> getLstCatStation() {
        return lstCatStation;
    }

    public void setLstCatStation(List<CatStationDTO> lstCatStation) {
        this.lstCatStation = lstCatStation;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public String getChecklistName() {
        return checklistName;
    }

    public void setChecklistName(String checklistName) {
        this.checklistName = checklistName;
    }
}
