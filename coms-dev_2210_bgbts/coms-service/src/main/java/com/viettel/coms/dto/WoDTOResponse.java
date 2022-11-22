package com.viettel.coms.dto;

import com.viettel.asset.dto.ResultInfo;
import com.viettel.coms.bo.WoTaskDailyBO;
import com.viettel.coms.bo.WoWorkLogsBO;
import com.viettel.coms.dto.vttb.VoGoodsDTO;
import com.viettel.wms.dto.AppParamDTO;
import org.json.JSONArray;

import java.util.List;
import java.util.Map;

public class WoDTOResponse {
    private ResultInfo resultInfo;
    private SysUserRequest sysUser;
    private List<WoDTO> lstWos;
    private List<WoMappingChecklistDTO> lstChecklistsOfWo;
    private List<AppParamDTO> lstDataForComboBox;
    private List<WoWorkLogsBO> logs;
    private WoDTO woDTO;
    List<WoSimpleFtDTO> listFtToAssign;
    Map<String, Integer> mapDataWoForChart;
    Map<String, Integer> mapDataWoPlanForChart;
    private WoMappingChecklistDTO woMappingChecklist;
    private List<WoChecklistDTO> lstChecklistsOfWoHcqt;
    private List<WoChecklistDTO> lstChecklistsOfAvg;
    private List<WoTaskDailyBO> lstTaskDaily;
    private List<Object> lstStocks;
    private List<Object> lstGoods;
    private List<Object> lstGoodsOffline;
    private List<Object> lstSignUsers;
    private List<Object> lstSignUserRoles;
    List<VoGoodsDTO> listMerEntity;
    private List<Object> lstData;
    private Integer totalRecord;
    private List<VoGoodsDTO> lstGoodsOfWo;

    // DBHT
    private List<WoAppParamDTO> lstClassDetails;
    private Long defaultClassValue;

    private List<CatStationDTO> lstCatStation;

    public List<WoChecklistDTO> getLstChecklistsOfAvg() {
        return lstChecklistsOfAvg;
    }

    public void setLstChecklistsOfAvg(List<WoChecklistDTO> lstChecklistsOfAvg) {
        this.lstChecklistsOfAvg = lstChecklistsOfAvg;
    }

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

    public List<WoDTO> getLstWos() {
        return lstWos;
    }

    public void setLstWos(List<WoDTO> lstWos) {
        this.lstWos = lstWos;
    }

    public List<WoMappingChecklistDTO> getLstChecklistsOfWo() {
        return lstChecklistsOfWo;
    }

    public void setLstChecklistsOfWo(List<WoMappingChecklistDTO> lstChecklistsOfWo) {
        this.lstChecklistsOfWo = lstChecklistsOfWo;
    }

    public List<AppParamDTO> getLstDataForComboBox() {
        return lstDataForComboBox;
    }

    public void setLstDataForComboBox(List<AppParamDTO> lstDataForComboBox) {
        this.lstDataForComboBox = lstDataForComboBox;
    }

    public List<WoWorkLogsBO> getLogs() {
        return logs;
    }

    public void setLogs(List<WoWorkLogsBO> logs) {
        this.logs = logs;
    }

    public WoDTO getWoDTO() {
        return woDTO;
    }

    public void setWoDTO(WoDTO woDTO) {
        this.woDTO = woDTO;
    }

    public List<WoSimpleFtDTO> getListFtToAssign() {
        return listFtToAssign;
    }

    public void setListFtToAssign(List<WoSimpleFtDTO> listFtToAssign) {
        this.listFtToAssign = listFtToAssign;
    }

    public Map<String, Integer> getMapDataWoForChart() {
        return mapDataWoForChart;
    }

    public void setMapDataWoForChart(Map<String, Integer> mapDataWoForChart) {
        this.mapDataWoForChart = mapDataWoForChart;
    }

    public Map<String, Integer> getMapDataWoPlanForChart() {
        return mapDataWoPlanForChart;
    }

    public void setMapDataWoPlanForChart(Map<String, Integer> mapDataWoPlanForChart) {
        this.mapDataWoPlanForChart = mapDataWoPlanForChart;
    }

    public List<WoChecklistDTO> getLstChecklistsOfWoHcqt() {
        return lstChecklistsOfWoHcqt;
    }

    public void setLstChecklistsOfWoHcqt(List<WoChecklistDTO> lstChecklistsOfWoHcqt) {
        this.lstChecklistsOfWoHcqt = lstChecklistsOfWoHcqt;
    }

    public WoMappingChecklistDTO getWoMappingChecklist() {
        return woMappingChecklist;
    }

    public void setWoMappingChecklist(WoMappingChecklistDTO woMappingChecklist) {
        this.woMappingChecklist = woMappingChecklist;
    }

    List<WoSimpleSysGroupDTO> cdsLv3;
    List<WoSimpleSysGroupDTO> cdsLv4;
    List<WoSimpleFtDTO> cdsLv5;
    List<WoSimpleFtDTO> ftList;

    public List<WoSimpleSysGroupDTO> getCdsLv3() {
        return cdsLv3;
    }

    public void setCdsLv3(List<WoSimpleSysGroupDTO> cdsLv3) {
        this.cdsLv3 = cdsLv3;
    }

    public List<WoSimpleSysGroupDTO> getCdsLv4() {
        return cdsLv4;
    }

    public void setCdsLv4(List<WoSimpleSysGroupDTO> cdsLv4) {
        this.cdsLv4 = cdsLv4;
    }

    public List<WoSimpleFtDTO> getFtList() {
        return ftList;
    }

    public void setFtList(List<WoSimpleFtDTO> ftList) {
        this.ftList = ftList;
    }

    private WoDashboardDTO dataDashboardWoMngt;
    private WoDashboardDTO dataDashboardWoExecute;

    public WoDashboardDTO getDataDashboardWoMngt() {
        return dataDashboardWoMngt;
    }

    public void setDataDashboardWoMngt(WoDashboardDTO dataDashboardWoMngt) {
        this.dataDashboardWoMngt = dataDashboardWoMngt;
    }

    public WoDashboardDTO getDataDashboardWoExecute() {
        return dataDashboardWoExecute;
    }

    public void setDataDashboardWoExecute(WoDashboardDTO dataDashboardWoExecute) {
        this.dataDashboardWoExecute = dataDashboardWoExecute;
    }

    public List<WoTaskDailyBO> getLstTaskDaily() {
        return lstTaskDaily;
    }

    public void setLstTaskDaily(List<WoTaskDailyBO> lstTaskDaily) {
        this.lstTaskDaily = lstTaskDaily;
    }

    private List<WoTrDTO> lstWoTrs;

    public List<WoTrDTO> getLstWoTrs() {
        return lstWoTrs;
    }

    public void setLstWoTrs(List<WoTrDTO> lstWoTrs) {
        this.lstWoTrs = lstWoTrs;
    }

    Map<String, Integer> mapDataWoTrForChart;

    public Map<String, Integer> getMapDataWoTrForChart() {
        return mapDataWoTrForChart;
    }

    public void setMapDataWoTrForChart(Map<String, Integer> mapDataWoTrForChart) {
        this.mapDataWoTrForChart = mapDataWoTrForChart;
    }

    public List<WoSimpleFtDTO> getCdsLv5() {
        return cdsLv5;
    }

    public void setCdsLv5(List<WoSimpleFtDTO> cdsLv5) {
        this.cdsLv5 = cdsLv5;
    }

    public List<Object> getLstData() {
        return lstData;
    }

    public void setLstData(List<Object> lstData) {
        this.lstData = lstData;
    }

    public List<Object> getLstStocks() {
        return lstStocks;
    }

    public void setLstStocks(List<Object> lstStocks) {
        this.lstStocks = lstStocks;
    }

    public List<Object> getLstGoods() {
        return lstGoods;
    }

    public void setLstGoods(List<Object> lstGoods) {
        this.lstGoods = lstGoods;
    }

    public List<Object> getLstSignUsers() {
        return lstSignUsers;
    }

    public void setLstSignUsers(List<Object> lstSignUsers) {
        this.lstSignUsers = lstSignUsers;
    }

    public List<Object> getLstSignUserRoles() {
        return lstSignUserRoles;
    }

    public void setLstSignUserRoles(List<Object> lstSignUserRoles) {
        this.lstSignUserRoles = lstSignUserRoles;
    }

    public List<Object> getLstGoodsOffline() {
        return lstGoodsOffline;
    }

    public void setLstGoodsOffline(List<Object> lstGoodsOffline) {
        this.lstGoodsOffline = lstGoodsOffline;
    }

    public List<VoGoodsDTO> getListMerEntity() {
        return listMerEntity;
    }

    public void setListMerEntity(List<VoGoodsDTO> listMerEntity) {
        this.listMerEntity = listMerEntity;
    }

    public Integer getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(Integer totalRecord) {
        this.totalRecord = totalRecord;
    }

    public List<CatStationDTO> getLstCatStation() {
        return lstCatStation;
    }

    public void setLstCatStation(List<CatStationDTO> lstCatStation) {
        this.lstCatStation = lstCatStation;
    }

    public List<WoAppParamDTO> getLstClassDetails() {
        return lstClassDetails;
    }

    public void setLstClassDetails(List<WoAppParamDTO> lstClassDetails) {
        this.lstClassDetails = lstClassDetails;
    }

    public Long getDefaultClassValue() {
        return defaultClassValue;
    }

    public void setDefaultClassValue(Long defaultClassValue) {
        this.defaultClassValue = defaultClassValue;
    }

    public List<VoGoodsDTO> getLstGoodsOfWo() {
        return lstGoodsOfWo;
    }

    public void setLstGoodsOfWo(List<VoGoodsDTO> lstGoodsOfWo) {
        this.lstGoodsOfWo = lstGoodsOfWo;
    }
}
