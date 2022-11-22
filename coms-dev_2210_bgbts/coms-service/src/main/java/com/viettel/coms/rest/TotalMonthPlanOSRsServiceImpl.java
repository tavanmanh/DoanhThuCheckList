package com.viettel.coms.rest;

import java.io.InputStream;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.activation.DataHandler;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.viettel.coms.business.TotalMonthPlanOSBusinessImpl;
import com.viettel.coms.business.YearPlanOSBusinessImpl;
import com.viettel.coms.dto.KpiLogDTO;
import com.viettel.coms.dto.TmpnContractDTO;
import com.viettel.coms.dto.TmpnFinanceDTO;
import com.viettel.coms.dto.TmpnForceMaintainDTO;
import com.viettel.coms.dto.TmpnMaterialDTO;
import com.viettel.coms.dto.TmpnSourceDTO;
import com.viettel.coms.dto.TmpnTargetDTO;
import com.viettel.coms.dto.TmpnTargetOSDTO;
import com.viettel.coms.dto.TotalMonthPlanOSSimpleDTO;
import com.viettel.coms.dto.TotalMonthPlanSimpleDTO;
import com.viettel.coms.dto.YearPlanDetailOSDTO;
import com.viettel.ktts2.common.BusinessException;
import com.viettel.ktts2.common.UFile;
import com.viettel.ktts2.common.UString;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.dto.DataListDTO;

/**
 * @author HoangNH38
 */
public class TotalMonthPlanOSRsServiceImpl implements TotalMonthPlanOSRsService{


    protected final Logger log = Logger.getLogger(TotalMonthPlanOSRsServiceImpl.class);
    @Autowired
    TotalMonthPlanOSBusinessImpl totalMonthPlanOSBusinessImpl;
    @Autowired
    YearPlanOSBusinessImpl yearPlanOSBusinessImpl;
    @Context
    HttpServletRequest request;
    @Value("${folder_upload2}")
    private String folderUpload;

    @Value("${default_sub_folder_upload}")
    private String defaultSubFolderUpload;

    @Value("${allow.file.ext}")
    private String allowFileExt;
    @Value("${allow.folder.dir}")
    private String allowFolderDir;

    @Override
    public Response doSearch(TotalMonthPlanOSSimpleDTO obj) {
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setStartTime(dStart);
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("DOSEARCH_MONTH_TOTAL_PLAN_OS");
        objKpiLog.setDescription("Kế hoạch tháng tổng thể ngoài OS");
        objKpiLog.setTransactionCode(obj.getCode());
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
        DataListDTO data = totalMonthPlanOSBusinessImpl.doSearch(obj);
        Date dEnd = new Date();
        objKpiLog.setEndTime(dEnd);
        objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
        objKpiLog.setStatus("1");
        yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
        return Response.ok(data).build();
    }

    @Override
    public Response add(TotalMonthPlanOSSimpleDTO obj) throws Exception {
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setStartTime(dStart);
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("INSERT_MONTH_TOTAL_PLAN_OS");
        objKpiLog.setDescription("Kế hoạch tháng tổng thể ngoài OS");
        objKpiLog.setTransactionCode(obj.getCode());
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
        try {
            obj.setCreatedUserId(objUser.getVpsUserInfo().getSysUserId());
            obj.setStatus("1");
            obj.setCreatedDate(new Date());
            obj.setCreatedGroupId(objUser.getVpsUserInfo().getSysGroupId());
            obj.setCreatedUserId(objUser.getVpsUserInfo().getSysUserId());
            obj.setSignState("1");
            Long ids = totalMonthPlanOSBusinessImpl.add(obj, objUser.getVpsUserInfo().getFullName(),
                    objUser.getVpsUserInfo().getSysUserId(), request);
            if (ids == 0l) {
            	Date dEnd = new Date();
                objKpiLog.setEndTime(dEnd);
                objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
                objKpiLog.setStatus("0");
                yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
                return Response.status(Response.Status.BAD_REQUEST).build();
            } else {
            	Date dEnd = new Date();
                objKpiLog.setEndTime(dEnd);
                objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
                objKpiLog.setStatus("1");
                yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
                return Response.ok(Response.Status.CREATED).build();
            }
        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }
//end 20181112 by tanqn
    @Override
    public Response update(TotalMonthPlanOSSimpleDTO obj) throws Exception {
        //tanstart 20181112 start
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setStartTime(dStart);
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("UPDATE_MONTH_TOTAL_PLAN_OS");
        objKpiLog.setDescription("Kế hoạch tháng tổng thể ngoài OS");
        objKpiLog.setTransactionCode(obj.getCode());
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
        try {
            obj.setUpdatedUserId(objUser.getVpsUserInfo().getSysUserId());
            obj.setStatus("1");
            obj.setUpdatedDate(new Date());
            obj.setUpdatedGroupId(objUser.getVpsUserInfo().getSysGroupId());
            Long ids = totalMonthPlanOSBusinessImpl.updateMonth(obj, objUser.getVpsUserInfo().getFullName(), request);
            if (ids == 0l) {
            	 Date dEnd = new Date();
                 objKpiLog.setEndTime(dEnd);
                 objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
                 objKpiLog.setStatus("0");
                 yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
                return Response.status(Response.Status.BAD_REQUEST).build();
            } else {
            	Date dEnd = new Date();
                objKpiLog.setEndTime(dEnd);
                objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
                objKpiLog.setStatus("1");
                yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
                return Response.ok(Response.Status.CREATED).build();
            }
        } catch (IllegalArgumentException e) {
        	Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
            objKpiLog.setStatus("0");
            objKpiLog.setReason(e.toString());
            yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }
//tanqn 20181112 end
    @Override
    public Response getById(Long id) throws Exception {
        TotalMonthPlanOSSimpleDTO data = totalMonthPlanOSBusinessImpl.getById(id);
        return Response.ok(data).build();
    }
    
    @Override
    public Response getByIdV2(Long id) throws Exception {
        TotalMonthPlanOSSimpleDTO data = totalMonthPlanOSBusinessImpl.getByIdV2(id);
        return Response.ok(data).build();
    }
    
    @Override
    public Response getTmpnList(Long id) throws Exception {
    	TotalMonthPlanOSSimpleDTO data = totalMonthPlanOSBusinessImpl.getTmpnList(id);
        return Response.ok(data).build();
    }

    @Override
    public Response getByIdCopy(Long id) throws Exception {
        TotalMonthPlanOSSimpleDTO data = totalMonthPlanOSBusinessImpl.getByIdCopy(id);
        return Response.ok(data).build();
    }

    @Override
    public Response getSequence() throws Exception {
        Long seq = totalMonthPlanOSBusinessImpl.getSequence();
        return Response.ok(seq).build();
    }

    @Override
    public Response remove(TotalMonthPlanOSSimpleDTO obj) {
        try {
            totalMonthPlanOSBusinessImpl.remove(obj, request);
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }

    @Override
    public Response exportExcelTemplateTarget() throws Exception {
        try {
            String strReturn = totalMonthPlanOSBusinessImpl.exportExcelTemplate("Import_Chitiet_thang_tongthe_ngoaiOS.xlsx");
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

    @Override
    public Response exportExcelTemplateSource() throws Exception {
        try {
            String strReturn = totalMonthPlanOSBusinessImpl.exportExcelTemplate("Import_nguonviec_thang_tongthe.xlsx");
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

    @Override
    public Response exportExcelTemplateForceMaintain() throws Exception {
        try {
            String strReturn = totalMonthPlanOSBusinessImpl.exportExcelTemplate("Import_giaco_thang_tongthe.xlsx");
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

    @Override
    public Response exportExcelTemplateForceNewBts() throws Exception {
        try {
            String strReturn = totalMonthPlanOSBusinessImpl.exportExcelTemplate("Import_bts_thang_tongthe.xlsx");
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

    @Override
    public Response exportExcelTemplateForceNewLine() throws Exception {
        try {
            String strReturn = totalMonthPlanOSBusinessImpl.exportExcelTemplate("Import_ngamhoa_thang_tongthe.xlsx");
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

    @Override
    public Response exportExcelTemplateMaterial() throws Exception {
        try {
            String strReturn = totalMonthPlanOSBusinessImpl.exportExcelTemplate("Import_vattu_thang_tongthe.xlsx");
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

    @Override
    public Response exportExcelTemplateFinance() throws Exception {
        try {
            String strReturn = totalMonthPlanOSBusinessImpl.exportExcelTemplate("Import_taichinh_thang_tongthe.xlsx");
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

    @Override
    public Response exportExcelTemplateContract() throws Exception {
        try {
            String strReturn = totalMonthPlanOSBusinessImpl.exportExcelTemplate("Import_hopdong_thang_tongthe.xlsx");
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

    @Override
    public Response importTarget(Attachment attachments, HttpServletRequest request) throws Exception {
        String filePath = writeFileToServer(attachments, request);
        try {
            try {
                return Response.ok(totalMonthPlanOSBusinessImpl.importTarget(folderUpload + filePath, filePath)).build();
            } catch (Exception e) {
                return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
            }

        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }

    }

    private boolean isExtendAllowSave(String fileName) {
        return UString.isExtendAllowSave(fileName, allowFileExt);
    }

    private boolean isFolderAllowFolderSave(String folderDir) {
        return UString.isFolderAllowFolderSave(folderDir, allowFolderDir);
    }

    @Override
    public Response fillterAllActiveCatConstructionType(TotalMonthPlanOSSimpleDTO obj) {
        List<TotalMonthPlanOSSimpleDTO> data = totalMonthPlanOSBusinessImpl.fillterAllActiveCatConstructionType(obj);
        return Response.ok(data).build();
    }

    @Override
    public Response fillterAllActiveCatConstructionDeploy(TotalMonthPlanOSSimpleDTO obj) {
        List<TotalMonthPlanOSSimpleDTO> data = totalMonthPlanOSBusinessImpl.fillterAllActiveCatConstructionDeploy(obj);
        return Response.ok(data).build();
    }

    @Override
    public Response saveTarget(TotalMonthPlanOSSimpleDTO obj) throws Exception {
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setStartTime(dStart);
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("Target_INSERT_MONTH_PLAN");
        objKpiLog.setDescription("Kế hoạch tháng tổng thể");
        objKpiLog.setTransactionCode(obj.getCode());
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
        try {
        	totalMonthPlanOSBusinessImpl.saveTarget(obj);
        	Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
            objKpiLog.setStatus("1");
            yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
            return Response.ok(Response.Status.CREATED).build();
        } catch (IllegalArgumentException e) {
        	e.printStackTrace();
        	Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
            objKpiLog.setStatus("0");
            objKpiLog.setReason(e.toString());
            yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }

    @Override
    public Response saveSource(TotalMonthPlanOSSimpleDTO obj) throws Exception {
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setStartTime(dStart);
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("Source_INSERT_MONTH_TOTAL_PLAN");
        objKpiLog.setDescription("Kế hoạch tháng tổng thể");
        objKpiLog.setTransactionCode(obj.getCode());
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
        try {
        	Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
            objKpiLog.setStatus("1");
            yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
            totalMonthPlanOSBusinessImpl.saveSource(obj);
            return Response.ok(Response.Status.CREATED).build();
        } catch (IllegalArgumentException e) {
        	e.printStackTrace();
        	Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
            objKpiLog.setStatus("0");
            objKpiLog.setReason(e.toString());
            yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }

    @Override
    public Response saveContract(TotalMonthPlanOSSimpleDTO obj) throws Exception {
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setStartTime(dStart);
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("Contract_INSERT_MONTH_TOTAL_PLAN");
        objKpiLog.setDescription("Kế hoạch tháng tổng thể");
        objKpiLog.setTransactionCode(obj.getCode());
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
        try {
        	Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
            objKpiLog.setStatus("1");
            yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
            totalMonthPlanOSBusinessImpl.saveContract(obj);
            return Response.ok(Response.Status.CREATED).build();
        } catch (IllegalArgumentException e) {
        	
            	Date dEnd = new Date();
                objKpiLog.setEndTime(dEnd);
                objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
                objKpiLog.setStatus("0");
                objKpiLog.setReason(e.toString());
                yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }

    @Override
    public Response saveFinance(TotalMonthPlanOSSimpleDTO obj) throws Exception {
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setStartTime(dStart);
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("Finace_INSERT_MONTH_TOTAL_PLAN");
        objKpiLog.setDescription("Kế hoạch tháng tổng thể");
        objKpiLog.setTransactionCode(obj.getCode());
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
        try {
        	Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
            objKpiLog.setStatus("1");
            yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
            totalMonthPlanOSBusinessImpl.saveFinance(obj);
            return Response.ok(Response.Status.CREATED).build();
        } catch (IllegalArgumentException e) {
        	Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
            objKpiLog.setStatus("0");
            objKpiLog.setReason(e.toString());
            yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }

    @Override
    public Response saveMaterial(TotalMonthPlanOSSimpleDTO obj) throws Exception {
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setStartTime(dStart);
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("Material_INSERT_MONTH_TOTAL_PLAN");
        objKpiLog.setTransactionCode(obj.getCode());
        objKpiLog.setDescription("Kế hoạch tháng tổng thể");
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
        try {
        	Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
            objKpiLog.setStatus("1");
            yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
            totalMonthPlanOSBusinessImpl.saveMaterial(obj);
            return Response.ok(Response.Status.CREATED).build();
        } catch (IllegalArgumentException e) {
        	Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
            objKpiLog.setStatus("0");
            objKpiLog.setReason(e.toString());
            yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }

    @Override
    public Response saveForceNewBTS(TotalMonthPlanOSSimpleDTO obj) throws Exception {
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setStartTime(dStart);
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("ForceNewBTS_INSERT_MONTH_TOTAL_PLAN");
        objKpiLog.setDescription("Kế hoạch tháng tổng thể");
        objKpiLog.setTransactionCode(obj.getCode());
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
        try {
        	Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
            objKpiLog.setStatus("1");
            yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
            totalMonthPlanOSBusinessImpl.saveForceNewBTS(obj);
            return Response.ok(Response.Status.CREATED).build();
        } catch (IllegalArgumentException e) {
        	Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
            objKpiLog.setStatus("0");
            objKpiLog.setReason(e.toString());
            yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }

    @Override
    public Response saveForceNew(TotalMonthPlanOSSimpleDTO obj) throws Exception {
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setStartTime(dStart);
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("ForceNew_INSERT_MONTH_TOTAL_PLAN");
        obj.setDescription("Kế hoạch tháng tổng thể");
        objKpiLog.setTransactionCode(obj.getCode());
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
        try {
        	Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
            objKpiLog.setStatus("1");
            yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
            totalMonthPlanOSBusinessImpl.saveForceNew(obj);
            return Response.ok(Response.Status.CREATED).build();
        } catch (IllegalArgumentException e) {
        	Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
            objKpiLog.setStatus("0");
            objKpiLog.setReason(e.toString());
            yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }

    @Override
    public Response saveForceMaintain(TotalMonthPlanOSSimpleDTO obj) throws Exception {
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setStartTime(dStart);
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("ForceMaintain_INSERT_MONTH_PLAN");
        objKpiLog.setDescription("Kế hoạch tháng tổng thể");
        objKpiLog.setTransactionCode(obj.getCode());
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
        try {
            totalMonthPlanOSBusinessImpl.saveForceMaintain(obj);
            Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
            objKpiLog.setStatus("1");
            yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
            return Response.ok(Response.Status.CREATED).build();
        } catch (IllegalArgumentException e) {
        	Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
            objKpiLog.setStatus("0");
            objKpiLog.setReason(e.toString());
            yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }
    @Override
    public Response importSource(Attachment attachments, HttpServletRequest request) throws Exception {
        String filePath = writeFileToServer(attachments, request);
        try {
            try {

                return Response.ok(totalMonthPlanOSBusinessImpl.importSource(folderUpload + filePath, filePath)).build();
            } catch (Exception e) {
                return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
            }

        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }

    }

    private String writeFileToServer(Attachment attachments, HttpServletRequest request2) {
        String folderParam = UString.getSafeFileName(request.getParameter("folder"));
        if (UString.isNullOrWhitespace(folderParam)) {
            folderParam = defaultSubFolderUpload;
        } else {
            if (!isFolderAllowFolderSave(folderParam)) {
                throw new BusinessException("folder khong nam trong white list: folderParam=" + folderParam);
            }
        }

        DataHandler dataHandler = attachments.getDataHandler();

        // get filename to be uploaded
        MultivaluedMap<String, String> multivaluedMap = attachments.getHeaders();
        String fileName = UFile.getFileName(multivaluedMap);
        fileName = "file_loi_" + fileName;
        if (!isExtendAllowSave(fileName)) {
            throw new BusinessException("File extension khong nam trong list duoc up load, file_name:" + fileName);
        }
        // write & upload file to server
        try (InputStream inputStream = dataHandler.getInputStream();) {
            return UFile.writeToFileServerATTT2(inputStream, fileName, folderParam, folderUpload);
        } catch (Exception ex) {
            throw new BusinessException("Loi khi save file", ex);
        }

    }

    @Override
    public Response importForceMaintain(Attachment attachments, HttpServletRequest request) throws Exception {
        String filePath = writeFileToServer(attachments, request);
        try {
            try {

                return Response.ok(totalMonthPlanOSBusinessImpl.importForceMaintain(folderUpload + filePath, filePath))
                        .build();
            } catch (Exception e) {
                return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
            }

        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }

    }

    @Override
    public Response importForceNewBts(Attachment attachments, HttpServletRequest request) throws Exception {
        String filePath = writeFileToServer(attachments, request);
        try {
            try {

                return Response.ok(totalMonthPlanOSBusinessImpl.importForceNewBts(folderUpload + filePath, filePath))
                        .build();
            } catch (Exception e) {
                return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
            }

        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }

    }

    @Override
    public Response importForceNewLine(Attachment attachments, HttpServletRequest request) throws Exception {
        String filePath = writeFileToServer(attachments, request);
        try {
            try {
                return Response.ok(totalMonthPlanOSBusinessImpl.importForceNewLine(folderUpload + filePath, filePath))
                        .build();
            } catch (Exception e) {
                return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
            }
        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }

    }

    @Override
    public Response importMaterial(Attachment attachments, HttpServletRequest request) throws Exception {
        String filePath = writeFileToServer(attachments, request);
        try {
            try {

                return Response.ok(totalMonthPlanOSBusinessImpl.importMaterial(folderUpload + filePath, filePath))
                        .build();
            } catch (Exception e) {
                return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
            }

        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }

    }

    @Override
    public Response importFinance(Attachment attachments, HttpServletRequest request) throws Exception {
        String filePath = writeFileToServer(attachments, request);
        try {
            try {

                return Response.ok(totalMonthPlanOSBusinessImpl.importFinance(folderUpload + filePath, filePath)).build();
            } catch (Exception e) {
                return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
            }
        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }

    @Override
    public Response importContract(Attachment attachments, HttpServletRequest request) throws Exception {
        String filePath = writeFileToServer(attachments, request);
        try {
            try {

                return Response.ok(totalMonthPlanOSBusinessImpl.importContract(folderUpload + filePath, filePath))
                        .build();
            } catch (Exception e) {
                return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
            }

        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }

    }

    @Override
    public Response getYearPlanDetail(TotalMonthPlanOSSimpleDTO obj) throws Exception {
        YearPlanDetailOSDTO data = totalMonthPlanOSBusinessImpl.getYearPlanDetail(obj);
        return Response.ok(data).build();
    }

    @Override
    public Response exportTotalMonthPlan(TotalMonthPlanOSSimpleDTO obj) throws Exception {
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setStartTime(dStart);
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("EXPORT_TOTAL_MONTH_PLAN");
        objKpiLog.setDescription("Xuất excel tháng tổng thể");
        //objKpiLog.setTransactionCode(obj.getCode());
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
        try {
            String strReturn = totalMonthPlanOSBusinessImpl.exportTotalMonthPlan(obj);
            Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
            objKpiLog.setStatus("1");
            yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
            Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
            objKpiLog.setStatus("0");
            objKpiLog.setReason(e.toString());
            yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
        }
        return null;
    }

    @Override
    public Response getByIdTarget(TotalMonthPlanOSSimpleDTO obj) throws Exception {
        return Response.ok(totalMonthPlanOSBusinessImpl.getByIdTarget(obj.getMonth(), obj.getYear(), obj.getSysGroupId()))
                .build();
    }

    @Override
    public Response exportDetailTargetTotalMonthPlan(TmpnTargetDTO obj) throws Exception {
        try {
            String strReturn = totalMonthPlanOSBusinessImpl.exportDetailTargetTotalMonthPlan(obj);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

    @Override
    public Response exportDetailSourceTotalMonthPlan(TmpnSourceDTO obj) throws Exception {
        try {
            String strReturn = totalMonthPlanOSBusinessImpl.exportDetailSourceTotalMonthPlan(obj);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

    @Override
    public Response exportDetailForceMaintainTotalMonthPlan(TmpnForceMaintainDTO obj) throws Exception {
    	//tan start 20181112
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setStartTime(dStart);
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("EXPORT_DETAIL_FORCE_MAINTAIN_TOTAL_MONTH_PLAN");
        objKpiLog.setDescription("Xuất excel chi tiết lực lượng tổng kế hoạch tháng");
        //objKpiLog.setTransactionCode(obj.getCode());
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
        try {
            String strReturn = totalMonthPlanOSBusinessImpl.exportDetailForceMaintainTotalMonthPlan(obj);
            Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
            objKpiLog.setStatus("1");
            yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
            Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
            objKpiLog.setStatus("0");
            objKpiLog.setReason(e.toString());
            yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
        }
        return null;
    }

    @Override
    public Response exportDetailMaterialTotalMonthPlan(TmpnMaterialDTO obj) throws Exception {
        try {
            String strReturn = totalMonthPlanOSBusinessImpl.exportDetailMaterialTotalMonthPlan(obj);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

    @Override
    public Response exportDetailFinanceTotalMonthPlan(TmpnFinanceDTO obj) throws Exception {
        try {
            String strReturn = totalMonthPlanOSBusinessImpl.exportDetailFinanceTotalMonthPlan(obj);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

    @Override
    public Response exportDetailContractTotalMonthPlan(TmpnContractDTO obj) throws Exception {
        try {
            String strReturn = totalMonthPlanOSBusinessImpl.exportDetailContractTotalMonthPlan(obj);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

    @Override
    public Response getLKBySysList(TotalMonthPlanOSSimpleDTO obj) throws Exception {
        return Response
                .ok(totalMonthPlanOSBusinessImpl.getLKBySysList(obj.getMonth(), obj.getYear(), obj.getSysGroupIdList()))
                .build();
    }

    @Override
    public Response saveAppendixFile(TotalMonthPlanOSSimpleDTO obj) throws Exception {
    	//tanqn 20181112 start
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("INSERT_APPENDIXFILE");
        objKpiLog.setDescription("Thêm mới kế hoạch phụ lục tháng tổng thể");
        objKpiLog.setTransactionCode(obj.getCode());
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
        try {
        	Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
            objKpiLog.setStatus("1");
            yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
            totalMonthPlanOSBusinessImpl.saveAppendixFile(obj, objUser.getVpsUserInfo().getSysUserId());
            return Response.ok(Response.Status.CREATED).build();
        } catch (IllegalArgumentException e) {
        	
            	Date dEnd = new Date();
                objKpiLog.setEndTime(dEnd);
                objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
                objKpiLog.setStatus("0");
                objKpiLog.setReason(e.toString());
                yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }
//tanqn 20181112 end
    @Override
    public Response getFileAppendixParam() throws Exception {
        return Response.ok(totalMonthPlanOSBusinessImpl.getFileAppendixParam()).build();
    }

    @Override
    public Response exportDetailBTSGTotalMonthPlan(Long id) throws Exception {
    	//tanqn 20181112 start
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("EXPORT_DETAIL_BTSG_TOTAL_MONTH_PLAN");
        objKpiLog.setDescription("Xuất excel chi tiết BTSG tháng tổng thể");
        objKpiLog.setTransactionCode(id.toString());
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
        try {
            String strReturn = totalMonthPlanOSBusinessImpl.exportDetailBTSGTotalMonthPlan(id);
            Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
            objKpiLog.setStatus("1");
            yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
            Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
            objKpiLog.setStatus("0");
            objKpiLog.setReason(e.toString());
            yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
        }
        return null;
    }

    @Override
    public Response exportDetailForceNewTotalMonthPlan(Long id) throws Exception {
    	//tanqn 20181112 start
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("EXPORT_DETAIL_FORCE_NEW_TOTAL_MONTH_PLAN");
        objKpiLog.setDescription("Xuất excel chi tiết lực lượng mới của tháng tổng thể");
        objKpiLog.setTransactionCode(id.toString());
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
        try {
            String strReturn = totalMonthPlanOSBusinessImpl.exportDetailForceNewTotalMonthPlan(id);
            Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
            objKpiLog.setStatus("1");
            yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
            Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
            objKpiLog.setStatus("0");
            objKpiLog.setReason(e.toString());
            yearPlanOSBusinessImpl.addKpiLog(objKpiLog);
        }
        return null;
    }

    @Override
    public Response doSearchTarget(TotalMonthPlanOSSimpleDTO obj) throws Exception {
        DataListDTO data = totalMonthPlanOSBusinessImpl.doSearchTarget(obj);
        return Response.ok(data).build();
    }

    @Override
    public Response doSearchSource(TotalMonthPlanOSSimpleDTO obj) throws Exception {
        DataListDTO data = totalMonthPlanOSBusinessImpl.doSearchSource(obj);
        return Response.ok(data).build();
    }

    @Override
    public Response doSearchForcemaintain(TotalMonthPlanOSSimpleDTO obj) throws Exception {
        DataListDTO data = totalMonthPlanOSBusinessImpl.doSearchForcemaintain(obj);
        return Response.ok(data).build();
    }

    @Override
    public Response doSearchBTS(TotalMonthPlanOSSimpleDTO obj) throws Exception {
        DataListDTO data = totalMonthPlanOSBusinessImpl.doSearchBTS(obj);
        return Response.ok(data).build();
    }

    @Override
    public Response doSearchForceNew(TotalMonthPlanOSSimpleDTO obj) throws Exception {
        DataListDTO data = totalMonthPlanOSBusinessImpl.doSearchForceNew(obj);
        return Response.ok(data).build();
    }

    @Override
    public Response doSearchMaterial(TotalMonthPlanOSSimpleDTO obj) throws Exception {
        DataListDTO data = totalMonthPlanOSBusinessImpl.doSearchMaterial(obj);
        return Response.ok(data).build();
    }

    @Override
    public Response doSearchFinance(TotalMonthPlanOSSimpleDTO obj) throws Exception {
        DataListDTO data = totalMonthPlanOSBusinessImpl.doSearchFinance(obj);
        return Response.ok(data).build();
    }

    @Override
    public Response doSearchContract(TotalMonthPlanOSSimpleDTO obj) throws Exception {
        DataListDTO data = totalMonthPlanOSBusinessImpl.doSearchContract(obj);
        return Response.ok(data).build();
    }

    @Override
    public Response doSearchAppendix(TotalMonthPlanOSSimpleDTO obj) throws Exception {
        DataListDTO data = totalMonthPlanOSBusinessImpl.doSearchAppendix(obj);
        return Response.ok(data).build();
    }

    @Override
    public Response checkPermissionsAdd() {
        try {
            KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
            Long sysGroupId = objUser.getVpsUserInfo().getSysGroupId();
            return Response.ok(totalMonthPlanOSBusinessImpl.checkPermissionsAdd(sysGroupId, request)).build();
        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }

    @Override
    public Response checkPermissionsCopy() {
        try {
            KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
            Long sysGroupId = objUser.getVpsUserInfo().getSysGroupId();
            return Response.ok(totalMonthPlanOSBusinessImpl.checkPermissionsCopy(sysGroupId, request)).build();
        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }

    }

    @Override
    public Response checkPermissionsDelete() {
        try {
            KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
            Long sysGroupId = objUser.getVpsUserInfo().getSysGroupId();
            return Response.ok(totalMonthPlanOSBusinessImpl.checkPermissionsDelete(sysGroupId, request)).build();
        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }

    @Override
    public Response checkPermissionsRegistry() {
        try {
            KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
            Long sysGroupId = objUser.getVpsUserInfo().getSysGroupId();
            return Response.ok(totalMonthPlanOSBusinessImpl.checkPermissionsRegistry(sysGroupId, request)).build();
        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }

    @Override
    public Response checkPermissionsUpdate() {
        try {
            KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
            Long sysGroupId = objUser.getVpsUserInfo().getSysGroupId();
            Long id = totalMonthPlanOSBusinessImpl.checkPermissionsUpdate(sysGroupId, request);
            return Response.ok(id).build();
        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }

    @Override
    public Response updateRegistry(TotalMonthPlanOSSimpleDTO obj) {
        try {
        	totalMonthPlanOSBusinessImpl.updateRegistry(obj, request);
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }

}
