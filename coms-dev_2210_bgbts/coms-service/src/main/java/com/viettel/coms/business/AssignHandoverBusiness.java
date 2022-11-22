package com.viettel.coms.business;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.viettel.coms.dto.AssignHandoverDTO;
import com.viettel.service.base.dto.DataListDTO;

//VietNT_20181210_created
public interface AssignHandoverBusiness {

    Long addNewAssignHandover(AssignHandoverDTO dto, HttpServletRequest request) throws Exception;

    List<AssignHandoverDTO> doImportExcel(String filePath, Long sysUserId);

    String downloadTemplate() throws Exception;

    DataListDTO doSearch(AssignHandoverDTO criteria);

    String removeAssignHandover(Long assignHandoverId, Long sysUserId);

    Long updateAttachFileDesign(AssignHandoverDTO dto) throws Exception;

    DataListDTO getAttachFile(Long id) throws Exception;
}
