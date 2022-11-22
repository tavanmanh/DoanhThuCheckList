package com.viettel.coms.business;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.viettel.coms.bo.WoBO;
import com.viettel.coms.bo.WoOverdueReasonBO;
import com.viettel.coms.bo.WoTaskDailyBO;
import com.viettel.coms.bo.WoWorkLogsBO;
import com.viettel.coms.dto.AIOWoTrDTO;
import com.viettel.coms.dto.ManageCertificateDTO;
import com.viettel.coms.dto.Report5sDTO;
import com.viettel.coms.dto.SysUserRequest;
import com.viettel.coms.dto.WoChartDataDto;
import com.viettel.coms.dto.WoChecklistDTO;
import com.viettel.coms.dto.WoDTO;
import com.viettel.coms.dto.WoDTORequest;
import com.viettel.coms.dto.WoDTOResponse;
import com.viettel.coms.dto.WoHcqtDTO;
import com.viettel.coms.dto.WoMappingChecklistDTO;
import com.viettel.coms.dto.WoMappingHshcTcDTO;
import com.viettel.coms.dto.WoNameDTO;
import com.viettel.coms.dto.WoOverdueReasonDTO;
import com.viettel.coms.dto.WoScheduleConfigDTO;
import com.viettel.coms.dto.WoSimpleFtDTO;
import com.viettel.coms.dto.WoSimpleSysGroupDTO;
import com.viettel.coms.dto.WoTaskDailyDTO;
import com.viettel.coms.dto.WoTcMassApproveRejectReqDTO;
import com.viettel.coms.dto.WoTypeDTO;
import com.viettel.coms.dto.avg.AvgResponse;
import com.viettel.coms.dto.avg.GetWoAvgOutputDto;
import com.viettel.coms.dto.avg.WoAvgInputDTO;
import com.viettel.coms.dto.avg.WoAvgResponseDTO;
import com.viettel.coms.dto.avg.WoAvgVhktInputDTO;
import com.viettel.coms.utils.BaseResponseOBJ;

public interface WoBusiness {
    List<WoDTO> list(WoDTORequest request, List<Long> lstWoId, Map<String, Object> filter);

    List<WoDTO> doSearch(WoDTO woDto, List<String> groupIdList) throws Exception;

    WoDTO getById(Long loginUserId, Long woId);

    void update(WoDTO woDTO, String content, SysUserRequest sysU) throws Exception;

    List<WoMappingChecklistDTO> getListChecklistsOfWo(Long woId, String woTypeCode);

    List<WoMappingChecklistDTO> getListChecklistsOfWoForWeb(Long woId, String woTypeCode);

    List<WoChecklistDTO> getHcqtChecklist(WoChecklistDTO checklistDTO);

    List<WoChecklistDTO> getAvgChecklist(WoChecklistDTO checklistDTO);

    List<WoChecklistDTO> getListChecklistsOfWoHcqt(Long woId);

    List<WoChecklistDTO> getListChecklistsOfWoAvg(Long woId);

    void updateChecklist(WoDTO woDTO, WoMappingChecklistDTO woMappingChecklistDTO);

    void updateChecklistHcqt(WoDTO woDTO, WoChecklistDTO woChecklistDTO);

    void updateChecklistXddd(WoDTO woDTO, WoMappingChecklistDTO checklistDto);

    boolean giveAssignment(WoDTO woDto);

    boolean cdAcceptAssignment(WoDTO woDto);

    boolean cdRejectAssignment(WoDTO woDto);

    boolean giveAssignmentToFT(WoDTO woDto);

    boolean changeWoState(WoDTO woDto, String state);

    BaseResponseOBJ changeWoStateWrapper(WoDTO woDto, String state);

    void mappingChecklistToWO(long catWorkItemTypeId, long woId, Long catConsTypeId, Long constructionId);

    void mappingChecklistAIOToWO(long contractId, long woId);

    String generateWoCode(WoDTO woDto);

    void logWoWorkLogs(WoDTO dto, String logType, String content, String contentDetail, String userCreated) throws Exception;

    List<WoWorkLogsBO> getListWorklogsOfWo(Long woId);

    String createTRContractAIO(AIOWoTrDTO aioWoTrDTO);

    String updateTRContractAIO(AIOWoTrDTO aioWoTrDTO);

    File createImportWoExcelTemplate(String loggedInUser, List<String> groupIdList, boolean isCnkt, boolean isRevenueRole, boolean isCreateHcqt) throws IOException;

    @Transactional
    boolean createNewWO(WoDTO woDto, boolean isImporting) throws Exception;

    void tryCreateChecklistForNewWo(WoDTO woDto);

    List<WoSimpleFtDTO> getListFtToAssign(Long parentGroupId);

    @Transactional
    void processOpinion(WoDTO woDTO) throws Exception;

    List<WoMappingChecklistDTO> getCheckListOfWo(WoDTO dto);

    boolean woCheckGpon(WoDTO dto);

    List<WoMappingChecklistDTO> getCheckListNeedAdd(WoDTO dto);

    List<WoMappingChecklistDTO> getAIOCheckListOfWo(WoDTO dto);

    WoDTO getOneWoDetails(long woId);

    File importWoTypeExcelTemplate() throws IOException;

    File exportExcelWoNameList(List<WoNameDTO> names) throws IOException;

    File exportExcelWoTypeList(List<WoTypeDTO> types) throws IOException;

    File exportExcelWoList(List<WoDTO> wos) throws IOException;

    boolean insertUpdateDailyChecklist(WoMappingChecklistDTO checklistDto);

    boolean addImageToChecklist(WoChecklistDTO dto) throws Exception;

    void tryUpdateWoProcessing(Long woId, String loggedInUser) throws Exception;

    WoMappingChecklistDTO listChecklistsDetail(Long woId, Long checkListID);

    WoChecklistDTO getChecklistsDetailAvg(Long woId, Long checkListID);

    List<WoMappingChecklistDTO> getListChecklistsOfWoTest(Long woId);

    boolean checkDoneCTTuyen(Long woId);

    public File createImportHCQTWoExcelTemplate() throws IOException;

    public int insertUpdateManyHcqtWo(List<WoHcqtDTO> dtos) throws Exception;

    void declareHcqtProblem(WoChecklistDTO obj) throws Exception;

    void resolveHcqtProblem(WoChecklistDTO obj) throws Exception;

    void completeHcqtChecklist(WoChecklistDTO obj);

    public String exportExcelHcqtWo(List<WoHcqtDTO> data) throws Exception;

    public boolean declareThdtAddedValue(WoMappingChecklistDTO obj);

    boolean createScheduleWoConfig(WoScheduleConfigDTO woDto);

    public File getImportScheduleConfigResult(List<WoScheduleConfigDTO> dtos) throws IOException;

    public String getImportDoneResult(List<WoDTO> lstData) throws Exception;

    boolean acceptXdddChecklistItemValue(Long id, String loggedInUser);

    public File exportFileReport5s(List<Report5sDTO> listObj) throws IOException;

    public boolean createWo5s(WoDTO triggerWo, Date finsishDate);

    public void tryCreate5SForXDDD(WoBO bo, Date startDate);

    public void tryUpdateWorkItem(List<Long> ids, Long woId);

    public boolean acceptWo(WoDTO woDto);

    public List<WoSimpleSysGroupDTO> getCdLv3List(WoDTO woDto);

    public List<WoSimpleSysGroupDTO> getCdLv4List(WoDTO woDto);

    public List<WoSimpleFtDTO> getFtList(WoDTO woDto);

    public BaseResponseOBJ approvedWo(WoDTO woDto, boolean checkRoleApproveHshc, String state);

    public void acceptChecklistQuantity(WoMappingChecklistDTO dto) throws Exception;

    public void rejectChecklistQuantity(WoMappingChecklistDTO dto) throws Exception;

    List<WoTaskDailyBO> doSearchWoTaskDaily(WoTaskDailyDTO searchObj);

    public List<WoSimpleFtDTO> getFtListCdLevel5(WoDTO woDto);

    public void tryResetXdddHshc(Long woId);

    public WoOverdueReasonBO getWoOverdueReason(WoOverdueReasonDTO dto);

    public void insertOverdueReason(WoDTO dto);

    public String callOutsideApi(Object req, String url);

    public WoDTOResponse callOutsideApiWraper(WoDTORequest request, String url);

    public WoDTOResponse woSignVoffice(WoDTORequest request, String url);

    public WoDTOResponse woSaveImportOrder(WoDTORequest request, String url);

    public WoDTOResponse getListGoodsByWoId(WoDTORequest request);

    public WoDTOResponse woUpdateAmountReal(WoDTORequest request);

    public WoDTOResponse checkVofficeInfo(WoDTORequest request, String url);

    public WoDTOResponse getOrderGoodsDetail(WoDTORequest request, String url);

    public WoDTOResponse saveVofficepassMB(WoDTORequest request, String url);

    public void tcAcceptAllSelected(WoTcMassApproveRejectReqDTO req) throws Exception;

    public void tcRejectAllSelected(WoTcMassApproveRejectReqDTO req) throws Exception;

    public List<WoDTO> getListWoByWoHshcId(WoMappingHshcTcDTO dto);

    public void updateChecklistTmbt(WoDTORequest request) throws Exception;

    public void updateChecklistTthq(WoDTORequest request) throws Exception;
    
    public void logWoWorkLogEditTTHT(WoDTO dto, String logType, String content, String contentDetail, String userCreated); //HienLT56 add 03062021

    public void updateChecklistDbht(WoDTORequest request) throws Exception;

    public Map<String,List<WoChartDataDto>>  getWoDataForChart(WoChartDataDto obj) throws Exception;
	
	public WoAvgResponseDTO createWoAvg(WoAvgVhktInputDTO woAvgInputDTO);

    public List<GetWoAvgOutputDto> getWoAvg(WoAvgVhktInputDTO request);

    AvgResponse getAvgToken();

    AvgResponse activeAvgDevice(WoChecklistDTO checklistDTO);

    boolean updateChecklistAvg(WoDTORequest woDTO, WoChecklistDTO woChecklistDTO);

    public List<ManageCertificateDTO> getListCertificateEnableFT(ManageCertificateDTO certificateDTO);
}
