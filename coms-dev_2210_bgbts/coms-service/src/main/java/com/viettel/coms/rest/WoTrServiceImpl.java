package com.viettel.coms.rest;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.activation.DataHandler;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.viettel.coms.bo.TrWorkLogsBO;
import com.viettel.coms.bo.WoTrBO;
import com.viettel.coms.bo.WoTrMappingStationBO;
import com.viettel.coms.bo.WoTrTypeBO;
import com.viettel.coms.business.WoTrBusiness;
import com.viettel.coms.business.WoTrBusinessImpl;
import com.viettel.coms.dao.CatStationDAO;
import com.viettel.coms.dao.TrWorkLogsDAO;
import com.viettel.coms.dao.WoConfigContractDAO;
import com.viettel.coms.dao.WoDAO;
import com.viettel.coms.dao.WoTrDAO;
import com.viettel.coms.dao.WoTrMappingStationDAO;
import com.viettel.coms.dao.WoTrTypeDAO;
import com.viettel.coms.dto.AIOTrContractDetailDTO;
import com.viettel.coms.dto.CatConstructionTypeDTO;
import com.viettel.coms.dto.CatStationDTO;
import com.viettel.coms.dto.CatStationRentDTO;
import com.viettel.coms.dto.CatWorkItemTypeDTO;
import com.viettel.coms.dto.ConstructionDTO;
import com.viettel.coms.dto.ExcelErrorDTO;
import com.viettel.coms.dto.WoAppParamDTO;
import com.viettel.coms.dto.WoCdDTO;
import com.viettel.coms.dto.WoConfigContractCommitteeDTO;
import com.viettel.coms.dto.WoDTO;
import com.viettel.coms.dto.WoSimpleConstructionDTO;
import com.viettel.coms.dto.WoSimpleContractDTO;
import com.viettel.coms.dto.WoSimpleProjectDTO;
import com.viettel.coms.dto.WoSimpleStationDTO;
import com.viettel.coms.dto.WoSimpleSysGroupDTO;
import com.viettel.coms.dto.WoTrDTO;
import com.viettel.coms.dto.WoTrMappingStationDTO;
import com.viettel.coms.dto.WoTrTypeDTO;
import com.viettel.coms.utils.BaseResponseOBJ;
import com.viettel.ktts.vps.VpsPermissionChecker;
import com.viettel.ktts2.common.BusinessException;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.ktts2.common.UString;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.utils.Constant;
import com.viettel.utils.ConvertData;
import com.viettel.wms.utils.ValidateUtils;

public class WoTrServiceImpl implements WoTrService {
    @Autowired
    WoDAO woDAO;

    @Autowired
    WoTrDAO trDAO;

    @Autowired
    WoTrTypeDAO trTypeDAO;

    @Autowired
    WoTrBusiness trBusiness;

    @Autowired
    TrWorkLogsDAO trWorkLogsDAO;

    @Autowired
    WoTrBusinessImpl woTrBusinessImpl;

    @Autowired
    WoConfigContractDAO woConfigContractDAO;

    @Autowired
    WoTrMappingStationDAO woTrMappingStationDAO;
    
    @Autowired
    CatStationDAO catStationDAO;

    @Value("${default_sub_folder_upload}")
    private String defaultSubFolderUpload;
    @Value("${allow.file.ext}")
    private String allowFileExt;
    @Value("${folder_upload2}")
    private String folderUpload;
    @Value("${allow.folder.dir}")
	private String allowFolderDir;

    private final int SUCCESS_CODE = 1;
    private final int ERROR_CODE = -1;
    private final String SUCCESS_MSG = "SUCCESS";
    private final String ERROR_MSG = "ERROR";
    private final String NOTFOUND_MSG = "NOT FOUND";
    private final String CANNOT_DELETE = "Có dữ liệu liên quan! Không được phép xóa.";
    private final String ALL_TYPE = "ALL TYPE";
    private final String AVAILABLE_TYPE = "AVAILABLE_TYPE";

    private final String UNASSIGN = "UNASSIGN";
    private final String ASSIGN_CD = "ASSIGN_CD";
    private final String REJECT_CD = "REJECT_CD";

    private final String CD_LEVEL_1 = "CD_LEVEL_1";
    private final String AIO = "AIO";
    private final String TTXDDTHT_GROUP_ID = "166677";

    @Override
    @Transactional
    public Response createTR(WoTrDTO trDto) throws Exception {
        BaseResponseOBJ resp;
        if (trDto.getCreateTrDomainGroupId() == null) {
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);
        }

        WoSimpleSysGroupDTO creatorDomainGroup = trDAO.getSysGroupById(trDto.getCreateTrDomainGroupId());

        if (trBusiness.createTR(trDto, creatorDomainGroup, false)) {
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, trDto.getTrId());
        } else {
            resp = new BaseResponseOBJ(ERROR_CODE, trDto.getCustomField(), null);
        }

        return Response.ok(resp).build();
    }

    @Override
    public Response createManyTR(List<WoTrDTO> trDtos, HttpServletRequest request) throws Exception {

        boolean isCNKT = VpsPermissionChecker.hasPermission(Constant.OperationKey.CRUD, Constant.AdResourceKey.CNKT_WOXL_TR, request);

        BaseResponseOBJ resp = null;
        try {
	        if (trBusiness.validateImportData(trDtos, isCNKT)) {
	            WoSimpleSysGroupDTO creatorDomainGroup = trDAO.getSysGroupById(trDtos.get(0).getCreateTrDomainGroupId());
	            long rowAffected = 0;
	            KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
	            if (!StringUtils.isNotEmpty(user.getVpsUserInfo().getEmployeeCode())) {
	                WoSimpleSysGroupDTO sysGroup = trDAO.getSysGroupCreateTR(user.getVpsUserInfo().getEmployeeCode());
	                for (WoTrDTO trDto : trDtos) {
	                    trDto.setGroupCreated(sysGroup.getSysGroupId().toString());
	                    trDto.setGroupCreatedName(sysGroup.getGroupName());
	                    if (trBusiness.createTR(trDto, creatorDomainGroup, true)) rowAffected++;
	                }
	            }
	            List<String> assignedGroupList = getAssignedGroupList(trDtos);
	            for (String groupId : assignedGroupList) {
	
	                int totalTr = countTrAssigned(groupId, trDtos);
	
	                String content = "Đơn vị vừa được giao " + totalTr + " Task Request. Đề nghị đồng chí vào kiểm tra!";
	                WoSimpleSysGroupDTO assignedGroup = woDAO.getSysGroup(groupId);
	                trBusiness.sendSmsToGroup(assignedGroup, content);
	            }
	
	            resp = new BaseResponseOBJ(SUCCESS_CODE, String.valueOf(rowAffected), trDtos);
	        } else {
	            resp = new BaseResponseOBJ(ERROR_CODE, null, trDtos);
	        }
        } catch (Exception e) {
			e.printStackTrace();
		}
        return Response.ok(resp).build();
    }

    private List<String> getAssignedGroupList(List<WoTrDTO> trDtos) {
        List<String> assignedGroupList = new ArrayList<>();
        for (WoTrDTO trDto : trDtos) {
            if (StringUtils.isNotEmpty(trDto.getCdLevel2())) {
                if (!assignedGroupList.contains(trDto.getCdLevel2())) assignedGroupList.add(trDto.getCdLevel2());
            } else {
                if (!assignedGroupList.contains(trDto.getCdLevel1())) assignedGroupList.add(trDto.getCdLevel1());
            }
        }

        return assignedGroupList;
    }

    private int countTrAssigned(String groupId, List<WoTrDTO> trDtos) {
        int total = 0;
        for (WoTrDTO trDto : trDtos) {
            if (StringUtils.isNotEmpty(trDto.getCdLevel2())) {
                if (groupId.equalsIgnoreCase(trDto.getCdLevel2())) total += 1;
            } else {
                if (groupId.equalsIgnoreCase(trDto.getCdLevel1())) total += 1;
            }
        }
        return total;
    }

    @Override
    public Response updateTR(WoTrDTO trDto) throws Exception {
        String returnStr = trBusiness.updateTR(trDto);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, returnStr, null);
        return Response.ok(resp).build();
    }

    @Override
    public Response changeStateTr(WoTrDTO trDto) throws Exception {
        String returnStr = trBusiness.changeStateTr(trDto);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, returnStr, null);
        return Response.ok(resp).build();
    }

    @Override
    public Response deleteTR(WoTrDTO trDto) throws Exception {
        Gson gson = new Gson();
        long trId = trDto.getTrId();
        BaseResponseOBJ resp;

        WoTrBO trBo = trDAO.getOneRaw(trId);
        if (trBo == null) {
            resp = new BaseResponseOBJ(ERROR_CODE, NOTFOUND_MSG, null);
            return Response.ok(resp).build();
        }
        trDto.setTrTypeId(trBo.getTrTypeId());
        trDto.setQoutaTime(trBo.getQoutaTime());
        int rowAffected = trBusiness.deleteTR(trDto);

        if (rowAffected == -1)
            resp = new BaseResponseOBJ(ERROR_CODE, CANNOT_DELETE, null);
        else
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, rowAffected);


        return Response.ok(resp).build();
    }

    @Override
    public Response getOneTRRaw(WoTrDTO trDto) throws Exception {
        long trId = trDto.getTrId();
        BaseResponseOBJ resp;

        WoTrBO trBo = trDAO.getOneRaw(trId);
        if (trBo == null) {
            resp = new BaseResponseOBJ(ERROR_CODE, NOTFOUND_MSG, null);
            return Response.ok(resp).build();
        }

        trDto = trBo.toDTO();

        resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, trDto);
        return Response.ok(resp).build();
    }

    @Override
    public Response getOneTRDetails(WoTrDTO trDto) throws Exception {
        Long trId = trDto.getTrId();
        BaseResponseOBJ resp;

        if (trId == null) {
            resp = new BaseResponseOBJ(ERROR_CODE, NOTFOUND_MSG, null);
            return Response.ok(resp).build();
        }

        trDto = trDAO.getOneDetails(trId);

        resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, trDto);
        return Response.ok(resp).build();
    }

    @Override
    public Response getListTRDetails(WoTrDTO trDto) throws Exception {

        if (trDto == null || trDto.getPage() <= 0 || trDto.getPageSize() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        List<WoTrDTO> listTrDto = trDAO.getByRange(trDto.getPage(), trDto.getPageSize());

        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, listTrDto);
        return Response.ok(resp).build();
    }

    @Override
    public Response doSearchTR(WoTrDTO trDto, HttpServletRequest request) throws Exception {
        String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
                Constant.AdResourceKey.WOXL_TR, request);

        boolean isGPTH = VpsPermissionChecker.hasPermission(Constant.OperationKey.VIEW, Constant.AdResourceKey.WOXL_TR_GPTH, request);
        boolean isDTHT = VpsPermissionChecker.hasPermission(Constant.OperationKey.VIEW, Constant.AdResourceKey.WOXL_TR_XDDTHT, request);
        
        List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");

        List<WoTrDTO> listTrDto = trDAO.doSearch(trDto, groupIdList, ALL_TYPE, isDTHT, isGPTH);
        List<WoTrDTO> listTr = new ArrayList<>();
        if(listTrDto.size() >0) {
        	for(WoTrDTO dto: listTrDto){
        		
        		List<Object[]> lst = trDAO.getListWo(dto.getTrId());
        		if(lst.size() > 0) {
        			String woCode = "";
        			String woTypeName = "";
        			for(Object[] obj: lst) {
        				woCode = woCode + obj[1].toString() + ", ";
        				woTypeName += (obj[2].toString() + ", ");
        			}
        			woCode = StringUtils.substring(woCode,0,-2);
        			woTypeName = StringUtils.substring(woTypeName,0,-2);
        			dto.setWoCode(woCode);
        			dto.setWoTypeName(woTypeName);
        		}
        		listTr.add(dto);
        	}
        }

        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, listTr);
        resp.setTotal(trDto.getTotalRecord());
        return Response.ok(resp).build();
    }

    @Override
    public Response doSearchAvailableTR(WoTrDTO trDto, HttpServletRequest request) throws Exception {
        if (StringUtils.isEmpty(trDto.getKeySearch())) {
            return Response.ok(new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, new ArrayList<>())).build();
        }

        List<String> groupIdList = new ArrayList<>();
        //lấy miền dữ liệu tạo wo của cnkt
        String groupIdDomainCnkt = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.CRUD,
                Constant.AdResourceKey.CNKT_WOXL, request);
        if (StringUtils.isNotEmpty(groupIdDomainCnkt)) {
            groupIdList = ConvertData.convertStringToList(groupIdDomainCnkt, ",");
        } else {
            //lấy miền dữ liệu tạo wo của cd lv 1
            String groupIdDomain = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.CREATE,
                    Constant.AdResourceKey.WOXL, request);
            groupIdList = ConvertData.convertStringToList(groupIdDomain, ",");
        }

        if (StringUtils.isNotEmpty(trDto.getLoggedInUser())) {
            WoSimpleSysGroupDTO userGroup = trDAO.getSysUserGroup(trDto.getLoggedInUser());
            String sysUserGroup = String.valueOf(userGroup.getSysGroupId());
            trDto.setCdLevel1(sysUserGroup);
        }

        List<WoTrDTO> listTrDto = trDAO.doSearch(trDto, groupIdList, AVAILABLE_TYPE, true, true);
        for (int i = 0; i < listTrDto.size(); i++) {
            String trTypeCode = listTrDto.get(i).getTrTypeCode();
            if (AIO.equalsIgnoreCase(trTypeCode)) {
                if (trDAO.checkAIOTrHasWO(listTrDto.get(i).getTrId())) {
                    listTrDto.remove(i);
                    i = i - 1;
                }
            }
        }

        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, listTrDto);
        return Response.ok(resp).build();
    }

    @Override
    public Response giveAssignmentToCD(WoTrDTO trDto) throws Exception {
        BaseResponseOBJ resp;

        if (trDto.getTrId() <= 0 || trDto.getAssignedCd() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        boolean cdAssignResult = trBusiness.giveAssignmentToCD(trDto);
        if (cdAssignResult)
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        else
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);

        return Response.ok(resp).build();
    }

    @Override
    public Response cdReject(WoTrDTO trDto) throws Exception {
        BaseResponseOBJ resp;

        if (trDto.getTrId() <= 0 || trDto.getLoggedInUser() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        boolean cdRejectResult = trBusiness.cdReject(trDto);
        if (cdRejectResult)
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        else
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);

        return Response.ok(resp).build();
    }

    @Override
    public Response cdAccept(WoTrDTO trDto) throws Exception {
        BaseResponseOBJ resp;

        if (trDto.getTrId() <= 0 || trDto.getLoggedInUser() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        boolean cdAcceptResult = trBusiness.cdAccept(trDto);
        if (cdAcceptResult)
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        else
            resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);

        return Response.ok(resp).build();
    }

    @Override
    public Response getImportExcelTemplate(WoTrDTO trDto, HttpServletRequest request) throws Exception {
        boolean isCNKT = VpsPermissionChecker.hasPermission(Constant.OperationKey.CRUD, Constant.AdResourceKey.CNKT_WOXL_TR, request);
        String cnktGroupId = "";

        if (isCNKT) {
            String groupIdDomain = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.CRUD,
                    Constant.AdResourceKey.CNKT_WOXL_TR, request);
            List<String> groupIdList = ConvertData.convertStringToList(groupIdDomain, ",");
            cnktGroupId = groupIdList.get(0); //lấy group đầu tiên trong miền dữ liệu tạo tr làm cdlv2
        }

        return Response.ok(trBusiness.createImportTrExcelTemplate(isCNKT, cnktGroupId)).build();
    }

    @Override
    public Response createTRType(WoTrTypeDTO dto) throws Exception {
        BaseResponseOBJ resp;

        boolean checkExistTrTypeCode = trTypeDAO.checkExistTrTypeCode(dto.getTrTypeCode());

        if (checkExistTrTypeCode) {
            String returnStr = trTypeDAO.save(dto.toModel());
            resp = new BaseResponseOBJ(SUCCESS_CODE, returnStr, null);
        } else {
            resp = new BaseResponseOBJ(ERROR_CODE, "Mã loại đã tồn tại.", null);
        }

        return Response.ok(resp).build();
    }

    @Override
    public Response createManyTRType(List<WoTrTypeDTO> dto) throws Exception {
        List<WoTrTypeBO> trTypeBos = new ArrayList<WoTrTypeBO>();
        for (WoTrTypeDTO item : dto) {
            trTypeBos.add(item.toModel());
        }
        String returnStr = trTypeDAO.saveListNoId(trTypeBos);

        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, returnStr, null);

        return Response.ok(resp).build();
    }

    @Override
    public Response getOneTRTypeDetails(WoTrTypeDTO dto) throws Exception {
        long trTypeId = dto.getWoTrTypeId();
        BaseResponseOBJ resp;

        if (trTypeId <= 0) {
            resp = new BaseResponseOBJ(ERROR_CODE, NOTFOUND_MSG, null);
            return Response.ok(resp).build();
        }

        dto = trTypeDAO.getOneDetails(trTypeId);

        resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, dto);
        return Response.ok(resp).build();
    }

    @Override
    public Response updateTRType(WoTrTypeDTO dto) throws Exception {
        String returnStr = trTypeDAO.update(dto.toModel());
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, returnStr, null);

        return Response.ok(resp).build();
    }

    @Override
    public Response deleteTRType(WoTrTypeDTO dto) throws Exception {
        long trTypeId = dto.getWoTrTypeId();
        BaseResponseOBJ resp;

        WoTrTypeBO trTypeBO = trTypeDAO.getOneRaw(trTypeId);
        if (trTypeBO == null) {
            resp = new BaseResponseOBJ(ERROR_CODE, NOTFOUND_MSG, null);
            return Response.ok(resp).build();
        }

        boolean isDeletable = trTypeDAO.checkDeletable(trTypeId);

        if (isDeletable) {
            trTypeDAO.delete(trTypeId);
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        } else {
            resp = new BaseResponseOBJ(ERROR_CODE, CANNOT_DELETE, null);
        }


        return Response.ok(resp).build();
    }

    @Override
    public Response getListTRType(WoTrTypeDTO dto) throws Exception {

        List<WoTrTypeDTO> listTrTypeDto = trTypeDAO.getListTRType();
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, listTrTypeDto);

        return Response.ok(resp).build();
    }


    @Override
    public Response doSearchTRType(WoTrTypeDTO dto) throws Exception {

        if (dto == null || dto.getPage() <= 0 || dto.getPageSize() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        List<WoTrTypeDTO> listTrDto = trTypeDAO.doSearch(dto);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, listTrDto);
        resp.setTotal(dto.getTotalRecord());
        return Response.ok(resp).build();
    }

    @Override
    public Response getCdLevel1(WoTrTypeDTO trTypeDto) throws Exception {
        List<WoAppParamDTO> cds = woDAO.getAppParam(CD_LEVEL_1);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, cds);
        return Response.ok(resp).build();
    }

    @Override
    public Response getAvailableProjects(WoTrDTO trDto) throws Exception {
        List<WoSimpleProjectDTO> projects = trDAO.getAvailableProjects();
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, projects);
        return Response.ok(resp).build();
    }

    @Override
    public Response doSearchProjects(WoTrDTO trDto) throws Exception {
        List<WoSimpleProjectDTO> projects = new ArrayList<>();

        if (StringUtils.isNotEmpty(trDto.getKeySearch())) projects = trDAO.doSearchProjects(trDto);

        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, projects);
        return Response.ok(resp).build();
    }

    @Override
    public Response getAvailableContracts(WoTrDTO trDto) throws Exception {
        List<WoSimpleContractDTO> contracts = trDAO.getAvailableContracts();
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, contracts);
        return Response.ok(resp).build();
    }

    @Override
    public Response doSearchContracts(WoTrDTO trDto, HttpServletRequest request) throws Exception {
        // Unikom_20210528_start
        String groupIdDomainCreateWoDoanhThu = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.CREATE, Constant.AdResourceKey.WOXL_DOANHTHU, request);
        if (StringUtils.isNotEmpty(groupIdDomainCreateWoDoanhThu)) {
            trDto.setCreateDoanhThuDomain(groupIdDomainCreateWoDoanhThu);
        }

        String groupIdDomainCreateWo = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.CREATE, Constant.AdResourceKey.WOXL, request);
        if (StringUtils.isNotEmpty(groupIdDomainCreateWo)) {
            trDto.setCreateWoDomain(groupIdDomainCreateWo);
        }

        String groupIdDomainCreateTr = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.CREATE, Constant.AdResourceKey.WOXL_TR, request);
        if (StringUtils.isNotEmpty(groupIdDomainCreateTr)) {
            trDto.setCreateTrDomain(groupIdDomainCreateTr);
        }
        // Unikom_20210528_end

        List<WoSimpleContractDTO> contracts = new ArrayList<>();

        if (StringUtils.isNotEmpty(trDto.getKeySearch())) contracts = trDAO.doSearchContracts(trDto);

        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, contracts);
        return Response.ok(resp).build();
    }

    @Override
    public Response doSearchAIOContracts(WoTrDTO trDto) throws Exception {

        List<WoSimpleContractDTO> contracts = new ArrayList<>();

        if (StringUtils.isNotEmpty(trDto.getKeySearch())) contracts = trDAO.doSearchAIOContracts(trDto);

        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, contracts);
        return Response.ok(resp).build();
    }

    @Override
    public Response doSearchConstruction(WoTrDTO trDto) throws Exception {
        String keySearch = trDto.getKeySearch();
        if (StringUtils.isEmpty(keySearch)) {
            return Response.ok(null).build();
        }

        List<WoSimpleConstructionDTO> constructions = trDAO.doSearchConstruction(trDto);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, constructions);
        return Response.ok(resp).build();
    }

    @Override
    public Response getConstructionByCode(WoTrDTO trDto) throws Exception {
        String constructionCode = trDto.getConstructionCode();
        if (StringUtils.isEmpty(constructionCode)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        WoSimpleConstructionDTO construction = trDAO.getConstructionByCode(constructionCode);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, construction);
        return Response.ok(resp).build();
    }

    @Override
    public Response getConstructionByContract(WoTrDTO trDto) throws Exception {
        List<WoSimpleConstructionDTO> cons = trDAO.getConstructionByContract(trDto);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, cons);
        return Response.ok(resp).build();
    }

    @Override
    public Response getConstructionByProject(WoTrDTO trDto) throws Exception {
        List<WoSimpleConstructionDTO> cons = trDAO.getConstructionByProject(trDto);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, cons);
        return Response.ok(resp).build();
    }

    @Override
    public Response getStationById(WoTrDTO trDto) throws Exception {
        WoSimpleStationDTO station = trDAO.getStationById(trDto);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, station);
        return Response.ok(resp).build();
    }

    @Override
    public Response getCdLevel2FromStation(WoTrDTO trDto) throws Exception {
        String stationCode = trDto.getStationCode();
        if (StringUtils.isEmpty(stationCode)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        WoSimpleSysGroupDTO group = trDAO.getCdLevel2FromStation(stationCode);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, group);
        return Response.ok(resp).build();
    }

    @Override
    public Response doSearchStation(WoTrDTO trDto) throws Exception {
        List<WoSimpleStationDTO> stations = new ArrayList<>();

        if (StringUtils.isNotEmpty(trDto.getKeySearch())) {
            stations = trDAO.doSearchStation(trDto);
        }

        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, stations);
        return Response.ok(resp).build();
    }

    @Override
    public Response getAIOPackagesByContract(WoTrDTO dto) throws Exception {
        Long contractId = dto.getContractId();
        if (contractId == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        List<AIOTrContractDetailDTO> packages = trDAO.getAIOPackagesByContract(dto);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, packages);
        resp.setTotal(dto.getTotalRecord());
        return Response.ok(resp).build();
    }

    @Override
    public Response getTrHistory(WoTrDTO dto) throws Exception {
        long trId = dto.getTrId();
        List<TrWorkLogsBO> logs = trWorkLogsDAO.doSearch(trId);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, logs);
        return Response.ok(resp).build();
    }

    @Override
    public Response getTrTypeImportTemplate() throws Exception {
        return Response.ok(trBusiness.importTrTypeExcelTemplate()).build();
    }

    @Override
    public Response getSysGroupById(WoSimpleSysGroupDTO dto) throws Exception {
        BaseResponseOBJ resp = new BaseResponseOBJ(ERROR_CODE, ERROR_MSG, null);
        if (dto.getSysGroupId() != null) {
            WoSimpleSysGroupDTO group = trDAO.getSysGroupById(dto.getSysGroupId());
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, group);
        }
        return Response.ok(resp).build();
    }

    @Override
    public Response exportTrExcel(WoTrDTO trDto, HttpServletRequest request) throws Exception {
        String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
                Constant.AdResourceKey.WOXL_TR, request);

        boolean isGPTH = VpsPermissionChecker.hasPermission(Constant.OperationKey.VIEW, Constant.AdResourceKey.WOXL_TR_GPTH, request);
        boolean isDTHT = VpsPermissionChecker.hasPermission(Constant.OperationKey.VIEW, Constant.AdResourceKey.WOXL_TR_XDDTHT, request);

        List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
        trDto.setPage(1l);
        trDto.setPageSize(9999);

        List<WoTrDTO> listTrDto = trDAO.doSearch(trDto, groupIdList, ALL_TYPE, isDTHT, isGPTH);
        List<WoTrDTO> listTr = new ArrayList<>();
        if(listTrDto.size() >0) {
        	for(WoTrDTO dto: listTrDto){
        		
        		List<Object[]> lst = trDAO.getListWo(dto.getTrId());
        		if(lst.size() > 0) {
        			String woCode = "";
        			String woTypeName = "";
        			for(Object[] obj: lst) {
        				woCode = woCode + obj[1].toString() + ", ";
        				woTypeName = woTypeName + obj[2].toString() + ", ";
        			}
        			woCode = StringUtils.substring(woCode,0,-2);
        			woTypeName = StringUtils.substring(woTypeName,0,-2);
        			dto.setWoCode(woCode);
        			dto.setWoTypeName(woTypeName);
        		}
        		listTr.add(dto);
        	}
        }

        return Response.ok(trBusiness.exportExcelTrList(listTr)).build();
    }

    @Override
    public Response exportTrTypeExcel(WoTrTypeDTO dto, HttpServletRequest request) throws Exception {
        dto.setPage(1l);
        dto.setPageSize(9999);

        List<WoTrTypeDTO> listTrTypeDto = trTypeDAO.doSearch(dto);

        return Response.ok(trBusiness.exportExcelTrTypeList(listTrTypeDto)).build();
    }

    @Override
    public Response doSearchStations(WoSimpleStationDTO dto) throws Exception {
        List<WoSimpleStationDTO> stations = trDAO.doSearchStations(dto);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, stations);
        return Response.ok(resp).build();
    }

    @Override
    public Response createManyTRReport(List<WoTrDTO> dtos) throws Exception {
        return Response.ok(trBusiness.genImportResultExcelFile(dtos)).build();
    }

    @Override
    public Response importFileZip(Attachment attachments, HttpServletRequest request) throws Exception {
        BaseResponseOBJ resp;
        String filePath;
        String folderParam = "upload";
        Calendar cal = Calendar.getInstance();
        DataHandler dataHandler = attachments.getDataHandler();
        String uploadPath = folderUpload + File.separator + UFile.getSafeFileName(folderParam) + File.separator + cal.get(Calendar.YEAR)
                + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator + cal.get(Calendar.DATE)
                + File.separator + cal.get(Calendar.MILLISECOND);
        String uploadLink = File.separator + UFile.getSafeFileName(folderParam) + File.separator + cal.get(Calendar.YEAR)
                + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator + cal.get(Calendar.DATE)
                + File.separator + cal.get(Calendar.MILLISECOND);

        // get filename to be uploaded
        MultivaluedMap<String, String> multivaluedMap = attachments.getHeaders();
        String fileName = UFile.getFileName(multivaluedMap);
        List<WoTrDTO> listWoTrDTO = Lists.newArrayList();
        Map<String, String> lstFileName = new HashMap<>();
        if (!isExtendAllowSave(fileName)) {
            throw new BusinessException("File extension khong nam trong list duoc up load, file_name:" + fileName);
        }
        byte[] BUFFER = new byte[1024];
        // write & upload file to server
        try (InputStream inputStream = dataHandler.getInputStream()) {
            String safeFileName = UString.getSafeFileName(fileName);
            File udir = new File(uploadPath);
            if (!udir.exists()) {
                udir.mkdirs();
            }
            try (OutputStream out = new FileOutputStream(udir.getAbsolutePath() + File.separator + safeFileName)) {
                int bytesRead = 0;
                byte[] buffer = new byte[1024 * 8];
                while ((bytesRead = inputStream.read(buffer, 0, 1024 * 8)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            filePath = uploadPath + File.separator + safeFileName;
        } catch (Exception ex) {
            throw new BusinessException("Loi khi save file", ex);
        }
        File folder = new File(uploadPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        ZipInputStream zis = null;
        try {
            zis = new ZipInputStream(new FileInputStream(filePath));
            ZipEntry entry;
            File file;
            OutputStream os;
            String entryName;
            String outFileName;
            System.out.println(filePath);
            while ((entry = zis.getNextEntry()) != null) {
                entryName = entry.getName();
                outFileName = uploadPath + File.separator + entryName;
                file = new File(outFileName);
                if (entry.isDirectory()) {
                    // Tạo các thư mục.
                    file.mkdirs();
                } else {
                    // Tạo các thư mục nếu không tồn tại
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    // Tạo một Stream để ghi dữ liệu vào file.
                    os = new FileOutputStream(outFileName);
                    int len;
                    while ((len = zis.read(BUFFER)) > 0) {
                        os.write(BUFFER, 0, len);
                    }
                    os.close();
                }
            }
            for (final File filenew : folder.listFiles()) {
                String fileEntryName = folder + File.separator + filenew.getName();
                if (filenew.isDirectory()) {
                    for (final File file2 : filenew.listFiles()) {
                        if (file2.isDirectory()) {
                        } else {
                            String fileEntryName2 = uploadLink + File.separator + filenew.getName() + File.separator + file2.getName();
                            lstFileName.put(file2.getName().trim().toUpperCase(), fileEntryName2);
                        }
                    }
                } else {
                    if (filenew.getName().substring(filenew.getName().lastIndexOf(".")).equals(".xlsx") || filenew.getName().substring(filenew.getName().lastIndexOf(".")).equals(".xls")) {
                        listWoTrDTO = importFileZipExcel(fileEntryName, request);
                    }
                }
            }
            if (listWoTrDTO.size() > 0) {
                String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.CREATE,
                        Constant.AdResourceKey.WOXL_TR, request);
                System.out.println("groupId CREATE WOXL_TR:.........................." + groupId);
                List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
                boolean isCNKT = VpsPermissionChecker.hasPermission(Constant.OperationKey.CRUD, Constant.AdResourceKey.CNKT_WOXL_TR, request);
                System.out.println("isCNKT CRUD CNKT_WOXL_TR:.........................." + isCNKT);
                boolean checkCDLevel5 = false;
                List<WoCdDTO> listWOcd = woDAO.getCdLevel1();
                for (String domainData : groupIdList) {
                    if (TTXDDTHT_GROUP_ID.equals(domainData)) {
                        checkCDLevel5 = true;
                    }
                }
                if (trBusiness.validateImportData(listWoTrDTO, isCNKT) && trBusiness.validateFileName(listWoTrDTO, lstFileName)) {
                    //WoSimpleSysGroupDTO creatorDomainGroup = trDAO.getSysGroupById(listWoTrDTO.get(0).getCreateTrDomainGroupId());
                    WoSimpleSysGroupDTO creatorDomainGroup = trDAO.getSysGroupById(Long.valueOf(groupIdList.get(0)));
                    long rowAffected = 0;
                    for (WoTrDTO trDto : listWoTrDTO) {
                        if (trBusiness.createTRImportZip(trDto, creatorDomainGroup, lstFileName, true, checkCDLevel5))
                            rowAffected++;
                    }
                    List<String> assignedGroupList = getAssignedGroupList(listWoTrDTO);

                    for (String groupIdX : assignedGroupList) {
                        int totalTr = countTrAssigned(groupIdX, listWoTrDTO);
                        String content = "Đơn vị vừa được giao " + totalTr + " Task Request. Đề nghị đồng chí vào kiểm tra!";
                        WoSimpleSysGroupDTO assignedGroup = woDAO.getSysGroup(groupIdX);
                        trBusiness.sendSmsToGroup(assignedGroup, content);
                    }

                    resp = new BaseResponseOBJ(SUCCESS_CODE, String.valueOf(rowAffected), listWoTrDTO);
                    return Response.ok(resp).build();
                } else {
                    resp = new BaseResponseOBJ(ERROR_CODE, "Import thất bại!", listWoTrDTO);
                    return Response.ok(resp).build();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp = new BaseResponseOBJ(ERROR_CODE, "Có lỗi trong quá trình import, liên hệ quản trị viên !", null);
            return Response.ok(resp).build();
        } finally {
            zis.close();
        }
        resp = new BaseResponseOBJ(SUCCESS_CODE, "Hoàn thành import !", null);
        return Response.ok(resp).build();
    }

    private boolean isExtendAllowSave(String fileName) {
        return UString.isExtendAllowSave(fileName, allowFileExt);
    }

    private ExcelErrorDTO createError(int row, int column, String detail) {
        ExcelErrorDTO err = new ExcelErrorDTO();
        err.setColumnError(String.valueOf(column));
        err.setLineError(String.valueOf(row));
        err.setDetailError(detail);
        return err;
    }

    public List<WoTrDTO> importFileZipExcel(String fileInput, HttpServletRequest request) {
        KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        List<WoTrDTO> workLst = Lists.newArrayList();
        WoSimpleSysGroupDTO woSimpleSysGroupDTO = trDAO.getSysGroupCreateTR(user.getVpsUserInfo().getEmployeeCode());
        try {
            File f = new File(fileInput);
            XSSFWorkbook workbook = new XSSFWorkbook(f);
            XSSFSheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();
            int count = 0;
            for (Row row : sheet) {
                WoTrDTO obj = new WoTrDTO();
                count++;
                if (count >= 3 && !ValidateUtils.checkIfRowIsEmpty(row)) {
                    SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
                    formatDate.setLenient(false);

                    String trName = formatter.formatCellValue(row.getCell(0));
                    String trType = formatter.formatCellValue(row.getCell(1));
                    String contractCode = formatter.formatCellValue(row.getCell(2));
                    String proCode = formatter.formatCellValue(row.getCell(3));
                    String constructionCode = formatter.formatCellValue(row.getCell(4));
                    String finishDate = formatter.formatCellValue(row.getCell(5));
                    String toalTime = formatter.formatCellValue(row.getCell(6));
                    String cdlevel1 = formatter.formatCellValue(row.getCell(7));
                    String cdlevel2 = formatter.formatCellValue(row.getCell(8));
                    String fileName = formatter.formatCellValue(row.getCell(9));
//                    String catWorkItemTypeListStr = formatter.formatCellValue(row.getCell(10));
                    String dbTkdaDate = formatter.formatCellValue(row.getCell(10));
                    String dbTtkdtDate = formatter.formatCellValue(row.getCell(11));
                    String dbVtDate = formatter.formatCellValue(row.getCell(12));

                    // Loai TR
                    try {
                        String[] listTrType = trType.split("-");
                        obj.setTrTypeId(Long.parseLong(listTrType[0]));
                        obj.setTrTypeName(listTrType[1]);
                        if (listTrType.length >= 2 && "TR_DONG_BO_HA_TANG".equalsIgnoreCase(listTrType[2])) {
                            obj.setTrTypeCode(listTrType.length >= 2 ? listTrType[2] : "");
                            // Bổ sung 3 ngày
                            if(!validateString(dbTkdaDate)) {
                            	obj.setCustomField(obj.getCustomField() + "\nNgày Đảm bảo thiết kế dự án không được để trống.");
                            } else {
                            	obj.setDbTkdaDate(formatDate.parse(dbTkdaDate));
                            }
                            
                            if(!validateString(dbTtkdtDate)) {
                            	obj.setCustomField(obj.getCustomField() + "\nNgày Đảm bảo thẩm thiết kế dự toán không được để trống.");
                            } else {
                            	obj.setDbTtkdtDate(formatDate.parse(dbTtkdtDate));
                            }
                            
                            if(!validateString(dbVtDate)) {
                            	obj.setCustomField(obj.getCustomField() + "\nNgày Đảm bảo vật tư không được để trống.");
                            } else {
                            	obj.setDbVtDate(formatDate.parse(dbVtDate));
                            }
                            // Set trạm và CD LV2
                            WoSimpleConstructionDTO constructionDTO = trDAO.getConstructionByCode(constructionCode);
                            if (constructionDTO != null && constructionDTO.getStationId() != null) {
                                WoTrDTO trDto = new WoTrDTO();
                                trDto.setStationId(constructionDTO.getStationId());
                                WoSimpleStationDTO station = trDAO.getStationById(trDto);
                                if (station != null) {
                                	if(station.getSysGroupId()==null) {
                                		obj.setCustomField(obj.getCustomField() + "\nTrạm chưa được cấu hình tỉnh.");
                                	} else {
                                		obj.setStationCode(station.getStationCode());
                                        obj.setExecuteLat(station.getLatitude());
                                        obj.setExecuteLong(station.getLongitude());
                                        obj.setCdLevel2Name(station.getSysGroupName());
                                        obj.setCdLevel2("" + station.getSysGroupId());
                                        obj.setCheckDBHT("1");
                                	}
                                } else {
                                    obj.setCustomField(obj.getCustomField() + "\nTrạm không tồn tại.");
                                }
                            } else {
                                obj.setCustomField(obj.getCustomField() + "\nCông trình không tồn tại hoặc không có trạm tương ứng.");
                            }
                        }
                    } catch (Exception ex) {
                    	ex.printStackTrace();
                        obj.setCustomField(obj.getCustomField() + "\nLoại TR không đúng định dạng.");
                    }
                    obj.setTrName(trName);
                    obj.setContractCode(contractCode.trim());
                    obj.setProjectCode(proCode.trim());
                    obj.setConstructionCode(constructionCode.trim());

                    // Dinh muc thoi gian hoan thanh
                    try {
                        obj.setQoutaTime(Integer.parseInt(toalTime));
                    } catch (Exception ex) {
                        obj.setCustomField(obj.getCustomField() + "\nĐịnh mức thời gian(Số giờ) không đúng.");
                    }

                    // Don vi dieu phoi cap 1
                    try {
                        String[] listCD1 = cdlevel1.split("-");
                        obj.setCdLevel1(listCD1[0]);
                        obj.setAssignedCd(listCD1[1] + "-" + listCD1[2]);
                        obj.setCdLevel1Name(listCD1[2]);
                    } catch (Exception ex) {
                        obj.setCustomField(obj.getCustomField() + "\nĐơn vị điều phối cấp 1 nhập không đúng.");
                    }

                    // Hạn hoàn thành
                    try {
                        Date date = formatDate.parse(finishDate);
                        obj.setFinishDate(date);
                    } catch (Exception ex) {
                        obj.setCustomField(obj.getCustomField() + "\n\"Hạn hoàn thành\" không đúng định dạng.");
                    }

                    // Don vi dieu phoi cap 2
//                    try {
//                        if (validateString(cdlevel2)) {
//                            String[] listCD2 = cdlevel2.split("-");
//                            obj.setCdLevel2(listCD2[0]);
//                            obj.setAssignedCd(listCD2[1] + "-" + listCD2[2]);
//                            obj.setCdLevel2Name(listCD2[2]);
//                        }
//                    } catch (Exception ex) {
//                        obj.setCustomField(obj.getCustomField() + "\nĐơn vị điều phối cấp 2 nhập không đúng.");
//                    }
                    obj.setFileName(fileName);
                    obj.setLoggedInUser(user.getVpsUserInfo().getEmployeeCode());
                    obj.setUserCreated(user.getVpsUserInfo().getEmployeeCode());
                    obj.setGroupCreated(woSimpleSysGroupDTO.getSysGroupId().toString());
                    obj.setGroupCreatedName(woSimpleSysGroupDTO.getGroupName());

                    // Hang muc
//                    if (StringUtils.isNotEmpty(catWorkItemTypeListStr)) {
//                        obj.setCatWorkItemTypeListString(catWorkItemTypeListStr);
//                    } else {
//                        obj.setCustomField(obj.getCustomField() + "\nHạng mục tạo WO tự động không được để trống.");
//                    }
                    obj.setStatus(1);
                    workLst.add(obj);
                }
            }
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return workLst;
    }

    @Override
    public Response exportFileZip(WoTrDTO obj, HttpServletRequest request) {
        String creatorDomain = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.CREATE,
                Constant.AdResourceKey.WOXL_TR, request);
        boolean isXddtht = false;
        if (creatorDomain.contains(TTXDDTHT_GROUP_ID)) isXddtht = true;

        String strReturn = "";
        boolean isCNKT = VpsPermissionChecker.hasPermission(Constant.OperationKey.CRUD, Constant.AdResourceKey.CNKT_WOXL_TR, request);
        String cnktGroupId = "";

        if (isCNKT) {
            String groupIdDomain = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.CRUD,
                    Constant.AdResourceKey.CNKT_WOXL_TR, request);
            List<String> groupIdList = ConvertData.convertStringToList(groupIdDomain, ",");
            cnktGroupId = groupIdList.get(0); //lấy group đầu tiên trong miền dữ liệu tạo tr làm cdlv2
        }
        try {
            strReturn = woTrBusinessImpl.exportFileZip(isCNKT, cnktGroupId, isXddtht);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Response doSearchStationByContract(WoSimpleStationDTO dto) throws Exception {
        if (dto.getContractId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        List<WoSimpleStationDTO> stations = trDAO.doSearchStationByContract(dto);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, stations);

        return Response.ok(resp).build();
    }

    public boolean validateString(String str) {
        return (str != null && str.length() > 0);
    }

    @Override
    public Response searchWoConfigContract(WoConfigContractCommitteeDTO trDto, HttpServletRequest request) throws Exception {
        if (StringUtils.isEmpty(trDto.getKeySearch())) {
            return Response.ok(new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, new ArrayList<>())).build();
        }
        List<WoConfigContractCommitteeDTO> listTrDto = woConfigContractDAO.doSearchWoConfigContract(trDto);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, listTrDto);
        return Response.ok(resp).build();
    }

    @Override
    public Response getFtList(WoConfigContractCommitteeDTO trDto, HttpServletRequest request) throws Exception {
        if (StringUtils.isEmpty(trDto.getKeySearch())) {
            return Response.ok(new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, new ArrayList<>())).build();
        }
        List<WoConfigContractCommitteeDTO> listTrDto = woConfigContractDAO.getFtList(trDto);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, listTrDto);
        return Response.ok(resp).build();
    }

    @Override
    public Response doSearchWoConfigContract(WoConfigContractCommitteeDTO dto) throws Exception {

        if (dto == null || dto.getPage() <= 0 || dto.getPageSize() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        List<WoConfigContractCommitteeDTO> listDto = woConfigContractDAO.doSearch(dto);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, listDto);
        resp.setTotal(dto.getTotalRecord());
        return Response.ok(resp).build();
    }

    @Override
    public Response createWoConfigContract(WoConfigContractCommitteeDTO dto, HttpServletRequest request) throws Exception {
        BaseResponseOBJ resp;
        String returnStr = "";
        KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        dto.setUserCreated(user.getVpsUserInfo().getSysUserId().toString());
        dto.setUserCreatedDate(new Date());
        boolean checkInser = woConfigContractDAO.checkWoConfigContractCommitte(dto);
        boolean checkType = woConfigContractDAO.checkBooleanContractType(dto);
        if (checkType == true) {
            returnStr = "Hợp đồng chọn không phải hợp đồng XDDD!";
            resp = new BaseResponseOBJ(ERROR_CODE, returnStr, null);
            return Response.ok(resp).build();
        }
        if (checkInser != true && checkType != true) {
            returnStr = woConfigContractDAO.save(dto.toModel());
        } else {
            returnStr = "Hợp đồng đã được giao cho nhân viên!";
            resp = new BaseResponseOBJ(ERROR_CODE, returnStr, null);
            return Response.ok(resp).build();
        }

        resp = new BaseResponseOBJ(SUCCESS_CODE, returnStr, null);
        return Response.ok(resp).build();
    }

    @Override
    public Response getOneWoConfigContract(WoConfigContractCommitteeDTO dto) throws Exception {
        Long id = dto.getId();
        BaseResponseOBJ resp;

        if (id <= 0) {
            resp = new BaseResponseOBJ(ERROR_CODE, NOTFOUND_MSG, null);
            return Response.ok(resp).build();
        }
        dto = woConfigContractDAO.getOneRaw(id).toDTO();
        resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, dto);
        return Response.ok(resp).build();
    }

    @Override
    public Response updateWoConfigContract(WoConfigContractCommitteeDTO dto, HttpServletRequest request) throws Exception {
        BaseResponseOBJ resp;
        String returnStr = "";
        KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        dto.setLastUpdateUser(user.getVpsUserInfo().getSysUserId().toString());
        dto.setLastUpdateDate(new Date());
//        boolean checkInser= woConfigContractDAO.checkWoConfigContractCommitte(dto);
//        if (checkInser == true ){
//            returnStr ="Hợp đồng đã được giao cho nhân viên!";
//            resp = new BaseResponseOBJ(ERROR_CODE, returnStr, null);
//            return Response.ok(resp).build();
//
//        }
        returnStr = woConfigContractDAO.update(dto.toModel());
        resp = new BaseResponseOBJ(SUCCESS_CODE, returnStr, null);
        return Response.ok(resp).build();
    }

    @Override
    public Response deleteWoConfigContract(WoConfigContractCommitteeDTO dto, HttpServletRequest request) throws Exception {
        Long id = dto.getId();
        BaseResponseOBJ resp;
        KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        WoConfigContractCommitteeDTO woScheduleConfigBO = woConfigContractDAO.getOneRaw(id).toDTO();
        if (woScheduleConfigBO == null) {
            resp = new BaseResponseOBJ(ERROR_CODE, NOTFOUND_MSG, null);
            return Response.ok(resp).build();
        } else {
            woConfigContractDAO.deleteWoConfigContract(id, user.getVpsUserInfo().getSysUserId().toString());
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        }
        return Response.ok(resp).build();
    }

    @Override
    public Response exportExcelWoConfigContract(HttpServletRequest request) throws Exception {
        String strReturn = " ";
        try {
            strReturn = woTrBusinessImpl.exportWoConfigContract();
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
        }
        return null;

    }

    @Override
    public Response getFtListByContract(WoConfigContractCommitteeDTO trDto, HttpServletRequest request) throws Exception {
        if (StringUtils.isEmpty(trDto.getKeySearch())) {
            return Response.ok(new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, new ArrayList<>())).build();
        }
        List<WoConfigContractCommitteeDTO> listTrDto = woConfigContractDAO.getFtListByContract(trDto);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, listTrDto);
        return Response.ok(resp).build();
    }

    @Override
    public Response getAutoWoWorkItems(CatWorkItemTypeDTO dto) throws Exception {
        BaseResponseOBJ resp;
        if (dto.getTrBranch() == null || dto.getCatConstructionTypeId() == null) {
            resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, new ArrayList<>());
            return Response.ok(resp).build();
        }

        List<CatWorkItemTypeDTO> data = woTrBusinessImpl.getAutoWoWorkItems(dto);
        resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, data);
        resp.setTotal(dto.getTotalRecord());
        return Response.ok(resp).build();
    }

    @Override
    public Response getCatConstructionTypes() throws Exception {
        BaseResponseOBJ resp;
        List<CatConstructionTypeDTO> data = trDAO.getCatConstructionTypes();
        resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, data);
        return Response.ok(resp).build();
    }

    @Override
    public Response getInactiveWoList(WoDTO dto) throws Exception {
        BaseResponseOBJ resp;
        List<WoDTO> data = woDAO.getInactiveWoList(dto);
        resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, data);
        return Response.ok(resp).build();
    }

    @Override
    public Response getListCD5ByContract(WoConfigContractCommitteeDTO trDto) throws Exception {
        List<WoConfigContractCommitteeDTO> listTrDto = woConfigContractDAO.getFtListByContract(trDto);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, listTrDto);
        return Response.ok(resp).build();
    }

	@Override
	public Response doSearchContruction(ConstructionDTO dto) throws Exception {
		 List<ConstructionDTO> lst = new ArrayList<>();
	        if (StringUtils.isNotEmpty(dto.getKeySearch())) {
	        	lst = trDAO.doSearchContruction(dto);
	        }

	        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, lst);
	        return Response.ok(resp).build();
	}

    @Override
    public Response getRentStation(WoTrDTO trDTO) throws Exception {
        List<CatStationRentDTO> lstRentStations = new ArrayList<>();
        if (trDTO.getTrId() == null) {
            lstRentStations = trDAO.getRentStation();
        } else {
            Long trId = trDTO.getTrId();
            List<WoTrMappingStationDTO> lstCatStationRents = woTrMappingStationDAO.getStationsOfTr(trId);
            for (WoTrMappingStationDTO iCsr : lstCatStationRents) {
                WoSimpleSysGroupDTO sysGroupDTO = woDAO.getSysGroup("" + iCsr.getSysGroupId());
                CatStationRentDTO catStationRentDTO = new CatStationRentDTO();
                catStationRentDTO.setBranch(sysGroupDTO.getCode() + " - " + sysGroupDTO.getGroupName());
                catStationRentDTO.setRentTarget((long) iCsr.getLstStations().split(",").length);
                // Get rent detail
                catStationRentDTO.setRentDetail(iCsr.getLstStationCodes());
                lstRentStations.add(catStationRentDTO);
            }
        }
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, lstRentStations);
        return Response.ok(resp).build();
    }

    @Override
    public Response getListStationByBranch(WoTrDTO trDTO) throws Exception {
        List<CatStationDTO> listStationByBranch = trDAO.getListStationByBranch(trDTO);
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, listStationByBranch);
        return Response.ok(resp).build();
    }

    @Override
    public Response changeTmbtStation(CatStationDTO dto) throws Exception {
        if (dto.getIsCheck() == 1) { // Insert bang WO_TR_MAPPING_STATION
//            CatStationBO catStationBO =
            WoTrMappingStationBO woTrMappingStationBO = new WoTrMappingStationBO();
            woTrMappingStationBO.setTrId(dto.getTmbtTrId());
            woTrMappingStationBO.setSysGroupId(dto.getTmbtSysGroupId());
            woTrMappingStationBO.setCatStationId(dto.getCatStationId());
            woTrMappingStationBO.setStatus(1l);
            woTrMappingStationDAO.save(woTrMappingStationBO);
        } else if (dto.getIsCheck() == 0) { // Delete bang WO_TR_MAPPING_STATION

        }
        BaseResponseOBJ resp = new BaseResponseOBJ(SUCCESS_CODE, SUCCESS_MSG, null);
        return Response.ok(resp).build();
    }
    
  //Huypq-09072021-start
    @Override
	public Response getExcelTemplate(CatStationDTO obj) throws Exception {
		try {
			String strReturn = trBusiness.getExcelTemplate(obj);
			return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    
    private boolean isFolderAllowFolderSave(String folderDir) {
		return UString.isFolderAllowFolderSave(folderDir, allowFolderDir);
	}
    
    @Override
	public Response importFileTrTmbt(Attachment attachments, HttpServletRequest request) throws Exception {
    	Gson gson = new Gson();
		String folderParam = UString.getSafeFileName(request.getParameter("folder"));
		Long createTRDomainDataList = Long.parseLong(UString.getSafeFileName(request.getParameter("createTRDomainDataList")));
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		String filePathReturn;
		String filePath;
		if (UString.isNullOrWhitespace(folderParam)) {
			folderParam = defaultSubFolderUpload;
		} else {
			if (!isFolderAllowFolderSave(folderParam)) {
				throw new BusinessException("folder khong nam trong white list: folderParam=" + folderParam);
			}
		}
		DataHandler dataHandler = attachments.getDataHandler();
		MultivaluedMap<String, String> multivaluedMap = attachments.getHeaders();
		String fileName = UFile.getFileName(multivaluedMap);
		if (!isExtendAllowSave(fileName)) {
			throw new BusinessException("File extension khong nam trong list duoc up load, file_name:" + fileName);
		}
		// write & upload file to server
		try (InputStream inputStream = dataHandler.getInputStream();) {
			filePath = UFile.writeToFileServerATTT2(inputStream, fileName, folderParam, folderUpload);
			filePathReturn = UEncrypt.encryptFileUploadPath(filePath);
		} catch (Exception ex) {
			throw new BusinessException("Loi khi save file", ex);
		}

		try {
			List<WoTrDTO> result = trBusiness.importFileTrTmbt(folderUpload + filePath, request);
			if (result != null && !result.isEmpty() && (result.get(0).getErrorList() == null || result.get(0).getErrorList().size() == 0)) {
				WoSimpleSysGroupDTO creatorDomainGroup = trDAO.getSysGroupById(createTRDomainDataList);
				WoSimpleSysGroupDTO woSimpleSysGroupDTO = trDAO.getSysGroupCreateTR(objUser.getVpsUserInfo().getEmployeeCode());
				for(WoTrDTO trDto: result) {
					trDto.setLoggedInUser(objUser.getVpsUserInfo().getEmployeeCode());
					trDto.setAssignedCd(trDto.getCdLevel1Name());
					
					String generatedTRCode = trBusiness.generateTRCode(trDto, creatorDomainGroup);
					
					trDto.setTrCode(generatedTRCode);
					trDto.setCustomField("Thành công. ");
					trDto.setGroupCreated(woSimpleSysGroupDTO.getSysGroupId().toString());
					trDto.setGroupCreatedName(woSimpleSysGroupDTO.getGroupName());
//					taotq add 22082022 start
					trDto.setCdLevel1("166677");
					trDto.setCdLevel1Name("TTDTHT - Trung tâm đầu tư hạ tầng");
					trDto.setTrTypeId(444522l);
					trDto.setUserCreated("-1");
					trDto.setCreatedDate(new Date());
//					taotq end
					Long trId = trDAO.saveObject(trDto.toModel());
					trDto.setTrId(trId);
					
					WoTrMappingStationDTO mapping = new WoTrMappingStationDTO();
					mapping.setTrId(trId);
					mapping.setSysGroupId(Long.valueOf(trDto.getStationSysGroup()));
					mapping.setCatStationId(trDto.getStationId());
					mapping.setStatus(1l);
					woTrMappingStationDAO.saveObject(mapping.toModel());
					
					catStationDAO.updateRentStatusStation(trDto.getStationId(), "3");
					
					trBusiness.logTrWorkLogs(trDto, "1", "Tạo mới TR. Trạng thái: Chờ CD tiếp nhận. ", gson.toJson(trDto.toModel()), trDto.getLoggedInUser(), false);
					
				}
				return Response.ok(result).build();
			} else if (result == null || result.isEmpty()) {
				return Response.ok().entity(Response.Status.NO_CONTENT).build();
			} else {
				return Response.ok(result).build();
			}

		} catch (IllegalArgumentException e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}
	}
    //Huy-end
}
