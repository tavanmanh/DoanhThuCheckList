package com.viettel.coms.business;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.viettel.coms.dto.CatStationDTO;
import com.viettel.coms.dto.WoSimpleSysGroupDTO;
import com.viettel.coms.dto.WoTrDTO;
import com.viettel.coms.dto.WoTrTypeDTO;

public interface WoTrBusiness {

    boolean createTR(WoTrDTO dto, WoSimpleSysGroupDTO creatorDomainGroup, boolean isImporting) throws Exception;

    String updateTR(WoTrDTO dto) throws Exception;

    int deleteTR(WoTrDTO dto) throws Exception;

    boolean giveAssignmentToCD(WoTrDTO trDto);

    boolean cdReject(WoTrDTO trDto);

    boolean cdAccept(WoTrDTO trDto);

    String generateTRCode(WoTrDTO trDto, WoSimpleSysGroupDTO creatorDomainGroup);

    File createImportTrExcelTemplate(boolean isCNKT, String cnktGroupId) throws IOException;

    String changeStateTr(WoTrDTO trDto) throws Exception;

    void logTrWorkLogs(WoTrDTO dto, String logType, String content, String contentDetail, String userCreated, boolean isImporting) throws Exception;

    File importTrTypeExcelTemplate() throws IOException;

    File exportExcelTrList(List<WoTrDTO> trs) throws IOException;

    File exportExcelTrTypeList(List<WoTrTypeDTO> trs) throws IOException;

    boolean validateImportData(List<WoTrDTO> trs, boolean isCNKT) throws Exception;

    File genImportResultExcelFile(List<WoTrDTO> trs) throws Exception;

    void sendSmsToGroup(WoSimpleSysGroupDTO assignedGroup, String content);

    boolean createTRImportZip(WoTrDTO trDto, WoSimpleSysGroupDTO creatorDomainGroup, Map<String, String> mapFile, boolean isImporting,boolean xddtht) throws Exception;

    boolean validateFileName(List<WoTrDTO> trs, Map<String, String> mapFile);

    List<WoTrDTO> list(String employeeCode, List<String> lstGroupIds);

    //Huypq-09072021-start
    public String getExcelTemplate(CatStationDTO obj) throws Exception;
    
    public List<WoTrDTO> importFileTrTmbt(String fileInput, HttpServletRequest request) throws Exception;
    //Huy-end
}
