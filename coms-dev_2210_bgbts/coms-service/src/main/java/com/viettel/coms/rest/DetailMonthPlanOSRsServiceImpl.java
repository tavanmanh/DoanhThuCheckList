package com.viettel.coms.rest;

import java.io.InputStream;
import java.util.ArrayList;
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

import com.viettel.coms.business.ConstructionTaskBusinessImpl;
import com.viettel.coms.business.DetailMonthPlanOSBusinessImpl;
import com.viettel.coms.business.DmpnOrderBusinessImpl;
import com.viettel.coms.business.YearPlanBusinessImpl;
import com.viettel.coms.dto.ConstructionTaskDTO;
import com.viettel.coms.dto.ConstructionTaskDetailDTO;
import com.viettel.coms.dto.DetailMonthPlanDTO;
import com.viettel.coms.dto.DetailMonthPlanSimpleDTO;
import com.viettel.coms.dto.DetailMonthQuantityDTO;
import com.viettel.coms.dto.KpiLogDTO;
import com.viettel.coms.dto.RevokeCashMonthPlanDTO;
import com.viettel.coms.dto.SysUserDetailCOMSDTO;
import com.viettel.coms.dto.TmpnTargetDetailDTO;
import com.viettel.coms.dto.WorkItemDetailDTO;
import com.viettel.ktts.vps.VpsPermissionChecker;
import com.viettel.ktts2.common.BusinessException;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.ktts2.common.UString;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.utils.Constant;
import com.viettel.utils.ConvertData;

import viettel.passport.client.UserToken;

/**
 * @author HoangNH38
 */
public class DetailMonthPlanOSRsServiceImpl implements DetailMonthPlanOSRsService{


    protected final Logger log = Logger.getLogger(DetailMonthPlanOSRsServiceImpl.class);
    @Autowired
    DetailMonthPlanOSBusinessImpl detailMonthPlanOSBusinessImpl;
    @Autowired
    ConstructionTaskBusinessImpl constructionTaskBusinessImpl;
    @Autowired
    DmpnOrderBusinessImpl dmpnOrderBusinessImpl;
    @Autowired
    YearPlanBusinessImpl yearPlanBusinessImpl;
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
    public Response doSearch(DetailMonthPlanSimpleDTO obj) {
        // TODO Auto-generated method stub
    	//tanqn start 20181113
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setStartTime(dStart);
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("DOSEARCH_DETAIL_MONTH_PLAN_OS");
        objKpiLog.setTransactionCode(obj.getConstructionCode());
        objKpiLog.setDescription("Kế hoạch tháng chi tiết ngoài OS");
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
        DataListDTO data = detailMonthPlanOSBusinessImpl.doSearch(obj, request);
        Date dEnd = new Date();
        objKpiLog.setEndTime(dEnd);
        objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
        objKpiLog.setStatus("1");
        yearPlanBusinessImpl.addKpiLog(objKpiLog);
        return Response.ok(data).build();
    }

    @Override
    public Response add(DetailMonthPlanSimpleDTO obj) throws Exception {
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setStartTime(dStart);
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("INSERT_DETAIL_MONTH_PLAN_OS");
        objKpiLog.setDescription("Kế hoạch tháng chi tiết ngoài OS");
        objKpiLog.setTransactionCode(obj.getCode());
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
        try {
//			hoanm1_20180607_start
            String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.CREATE,
                    Constant.AdResourceKey.MONTHLY_DETAIL_PLAN, request);
            List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
            if (!groupIdList.contains(obj.getSysGroupId().toString())) {
                throw new IllegalArgumentException("Bạn không có quyền tạo kế hoạch tháng chi tiết cho đơn vị này");
            }
//			hoanm1_20180607_end
            obj.setCreatedUserId(objUser.getVpsUserInfo().getSysUserId());
            obj.setStatus("1");
            obj.setCreatedDate(new Date());
            obj.setCreatedGroupId(objUser.getVpsUserInfo().getSysGroupId());
            obj.setSignState("1");
            Long ids = 0l;
            if(obj.getType()!=null && obj.getType().equals("2")) {
            	ids = detailMonthPlanOSBusinessImpl.addTTXD(obj, request);
            } else {
            	ids = detailMonthPlanOSBusinessImpl.addHTCT(obj, request);
            }
            if (ids == 0l) {
            	Date dEnd = new Date();
                objKpiLog.setEndTime(dEnd);
                objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
                objKpiLog.setStatus("0");
                yearPlanBusinessImpl.addKpiLog(objKpiLog);
                return Response.status(Response.Status.BAD_REQUEST).build();
            } else {
            	Date dEnd = new Date();
                objKpiLog.setEndTime(dEnd);
                objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
                objKpiLog.setStatus("1");
                yearPlanBusinessImpl.addKpiLog(objKpiLog);
                return Response.ok(Response.Status.CREATED).build();
            }
        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }

    @Override
    public Response update(DetailMonthPlanSimpleDTO obj) throws Exception {
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setStartTime(dStart);
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("UPDATE_MONTH_PLAN_OS");
        objKpiLog.setDescription("Kế hoạch tháng chi tiết ngoài OS");
        objKpiLog.setTransactionCode(obj.getCode());
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
    	try {
//			hoanm1_20180607_start
            String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.CREATE,
                    Constant.AdResourceKey.MONTHLY_DETAIL_PLAN, request);
            List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
            if (!groupIdList.contains(obj.getSysGroupId().toString())) {
//				return Response.ok(2L).build();
                throw new IllegalArgumentException("Bạn không có quyền sửa kế hoạch tháng chi tiết cho đơn vị này");
            }
//			hoanm1_20180607_end
            obj.setUpdatedUserId(objUser.getVpsUserInfo().getSysUserId());
            obj.setStatus("1");
            obj.setUpdatedDate(new Date());
            obj.setUpdatedGroupId(objUser.getVpsUserInfo().getSysGroupId());
            Long ids = 0l;
            if(obj.getType()!=null && obj.getType().equals("2")) {
            	ids = detailMonthPlanOSBusinessImpl.updateMonthTTXD(obj, request);
            } else {
            	ids = detailMonthPlanOSBusinessImpl.updateMonth(obj, request);
            }
            if (ids == 0l) {
            	Date dEnd = new Date();
                objKpiLog.setEndTime(dEnd);
                objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
                objKpiLog.setStatus("0");
                yearPlanBusinessImpl.addKpiLog(objKpiLog);
                return Response.status(Response.Status.BAD_REQUEST).build();
            } else {
            	Date dEnd = new Date();
                objKpiLog.setEndTime(dEnd);
                objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
                objKpiLog.setStatus("1");
                yearPlanBusinessImpl.addKpiLog(objKpiLog);
                return Response.ok(Response.Status.CREATED).build();
            }
        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }

    @Override
    public Response getById(Long id) throws Exception {
        // TODO Auto-generated method stub
        DetailMonthPlanSimpleDTO data = detailMonthPlanOSBusinessImpl.getById(id);
        return Response.ok(data).build();
    }

    @Override
    public Response getSequence() throws Exception {
        // TODO Auto-generated method stub
        Long seq = detailMonthPlanOSBusinessImpl.getSequence();
        return Response.ok(seq).build();
    }

    @Override
    public Response remove(DetailMonthPlanSimpleDTO obj) {
        // TODO Auto-generated method stub
    	//HuyPQ-20190627-start log
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setStartTime(dStart);
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("DELETE_MONTH_PLAN");
        objKpiLog.setDescription("Kế hoạch tháng chi tiết ngoài OS");
        objKpiLog.setTransactionCode(obj.getCode());
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
    	//Huy-end
//		hoanm1_20180607_start
        try {
            String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.CREATE,
                    Constant.AdResourceKey.MONTHLY_DETAIL_PLAN, request);
            List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
            if (!groupIdList.contains(obj.getSysGroupId().toString())) {
                throw new IllegalArgumentException("Bạn không có quyền xóa kế hoạch tháng chi tiết cho đơn vị này");
            }
//		hoanm1_20180607_end
          //HuyPQ-20190627-start
            List<DetailMonthPlanDTO> detail = detailMonthPlanOSBusinessImpl.checkTaskConstruction(obj.getDetailMonthPlanId());
            if(detail.size()>0) {
            	throw new IllegalArgumentException("Kế hoạch tháng đã phát sinh tiến độ, không được xoá");
            }
            //Huy-end
            detailMonthPlanOSBusinessImpl.remove(obj);
          //HuyPQ-20190627-start
            Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
            objKpiLog.setStatus("1");
            yearPlanBusinessImpl.addKpiLog(objKpiLog);
            //Huy-end
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
        	//HuyPQ-20190627-start
            Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
            objKpiLog.setStatus("0");
            yearPlanBusinessImpl.addKpiLog(objKpiLog);
            //Huy-end
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }

    @Override
    public Response updateListTC(DetailMonthPlanSimpleDTO obj) throws Exception {
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        //tanqn 20181109 start
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("updateListTC_MONTH_PLAN_OS");
        objKpiLog.setDescription("Kế hoạch tháng chi tiết ngoài OS");
        objKpiLog.setTransactionCode(obj.getCode());
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
        objKpiLog.setStartTime(dStart);
        detailMonthPlanOSBusinessImpl.updateListTCHTCT(obj, objUser);
        Date dEnd = new Date();
        objKpiLog.setEndTime(dEnd);
        objKpiLog.setDuration((long)dEnd.getSeconds() - (long)dStart.getSeconds());
        objKpiLog.setStatus("1");
        yearPlanBusinessImpl.addKpiLog(objKpiLog);
      //tanqn 20181109 end
        return Response.ok().build();
    }

    @Override
    public Response updateListHSHC(DetailMonthPlanSimpleDTO obj) throws Exception {
        KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        detailMonthPlanOSBusinessImpl.updateListHSHC(obj, objUser);
        return Response.ok().build();
    }

    @Override
    public Response updateListLDT(DetailMonthPlanSimpleDTO obj) throws Exception {
        KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        detailMonthPlanOSBusinessImpl.updateListLDT(obj, objUser);
        return Response.ok().build();
    }

    @Override
    public Response updateListDT(DetailMonthPlanSimpleDTO obj) throws Exception {
        KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        detailMonthPlanOSBusinessImpl.updateListDT(obj, objUser);
        return Response.ok().build();
    }

    @Override
    public Response updateListCVK(DetailMonthPlanSimpleDTO obj) throws Exception {
        KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        detailMonthPlanOSBusinessImpl.updateListCVK(obj, objUser);
        return Response.ok().build();
    }

    @Override
    public Response exportTemplateListTC(SysUserDetailCOMSDTO obj) throws Exception {
        try {
            String strReturn = detailMonthPlanOSBusinessImpl.exportTemplateListTC(obj.getSysGroupId(), request);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

    @Override

    public Response exportTemplateListHSHC(SysUserDetailCOMSDTO obj) throws Exception {
        try {
            String strReturn = detailMonthPlanOSBusinessImpl.exportTemplateListHSHC(obj.getSysGroupId(), request);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

    @Override

    public Response exportTemplateListLDT(SysUserDetailCOMSDTO obj) throws Exception {
        try {
            String strReturn = detailMonthPlanOSBusinessImpl.exportTemplateListLDT(obj.getSysGroupId(), request);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

    @Override

    public Response exportTemplateListCVK(SysUserDetailCOMSDTO obj) throws Exception {
        try {
            String strReturn = detailMonthPlanOSBusinessImpl.exportTemplateListCVK(obj.getSysGroupId(), request);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

    @Override
    public Response importTC(Attachment attachments, HttpServletRequest request) throws Exception {
        String filePath = writeFileToServer(attachments, request);
        try {
            try {
                return Response.ok(detailMonthPlanOSBusinessImpl.importTCHTCT(folderUpload + filePath, filePath, request)).build();
            } catch (Exception e) {
                // TODO Auto-generated catch block
            	e.printStackTrace();
                return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
            }
        } catch (IllegalArgumentException e) {
        	e.printStackTrace();
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }

    @Override
    public Response importHSHC(Attachment attachments, HttpServletRequest request) throws Exception {
        String filePath = writeFileToServer(attachments, request);
        try {
            try {
                return Response.ok(detailMonthPlanOSBusinessImpl.importHSHC(folderUpload + filePath, filePath)).build();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
            }
        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }

    @Override
    public Response importLDT(Attachment attachments, HttpServletRequest request) throws Exception {
        String filePath = writeFileToServer(attachments, request);
        try {
            try {
            	 long startTime = System.nanoTime();
            	 List<ConstructionTaskDetailDTO> result = detailMonthPlanOSBusinessImpl.importLDT(folderUpload + filePath, filePath);
            	 long endTime = System.nanoTime();
            	 System.out.println("total execution time:"+ (endTime - startTime));
                return Response.ok(result).build();
               
            } catch (Exception e) {
                // TODO Auto-generated catch block
                return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
            }
        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }

    @Override
    public Response importCVK(Attachment attachments, HttpServletRequest request) throws Exception {
        String filePath = writeFileToServer(attachments, request);
        try {
            try {
                return Response.ok(detailMonthPlanOSBusinessImpl.importCVK(folderUpload + filePath, filePath)).build();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
            }
        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }

    private String writeFileToServer(Attachment attachments, HttpServletRequest request2) {
        // TODO Auto-generated method stub
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

    private boolean isFolderAllowFolderSave(String folderDir) {
        return UString.isFolderAllowFolderSave(folderDir, allowFolderDir);
    }

    private boolean isExtendAllowSave(String fileName) {
        return UString.isExtendAllowSave(fileName, allowFileExt);
    }

    public Response getYearPlanDetailTarget(DetailMonthPlanSimpleDTO obj) throws Exception {
        // TODO Auto-generated method stub
        List<TmpnTargetDetailDTO> data = new ArrayList<TmpnTargetDetailDTO>();
        if (obj.getMonth() != null && obj.getYear() != null && obj.getSysGroupId() != null)
            data = detailMonthPlanOSBusinessImpl.getYearPlanDetailTarget(obj);
        return Response.ok(data).build();

    }

    public Response getWorkItemDetail(DetailMonthPlanSimpleDTO obj) throws Exception {
        // TODO Auto-generated method stub
        List<WorkItemDetailDTO> data = new ArrayList<WorkItemDetailDTO>();
        if (obj.getConstructionCode() != null)
            data = detailMonthPlanOSBusinessImpl.getWorkItemDetail(obj);
        return Response.ok(data).build();

    }

    @Override
    public Response doSearchConsTask(ConstructionTaskDetailDTO obj) throws Exception {
        DataListDTO data = constructionTaskBusinessImpl.doSearch(obj);
        return Response.ok(data).build();
    }

    @Override
    public Response exportTemplateListDT(SysUserDetailCOMSDTO obj) throws Exception {
        try {
            String strReturn = detailMonthPlanOSBusinessImpl.exportExcelTemplate("Import_dongtien_thang_chitiet.xlsx",
                    obj.getSysGroupId());
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

    @Override
    public Response exportTemplateListYCVT(SysUserDetailCOMSDTO obj) throws Exception {
        try {
            UserToken objUser = (UserToken) request.getSession().getAttribute("vsaUserToken");
            String strReturn = detailMonthPlanOSBusinessImpl.exportExcelTemplate("Import_vattu_thang_chitiet.xlsx",
                    obj.getSysGroupId());
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

    @Override
    public Response importDT(Attachment attachments, HttpServletRequest request) throws Exception {
        String filePath = writeFileToServer(attachments, request);
        try {
            try {
                return Response.ok(detailMonthPlanOSBusinessImpl.importDT(folderUpload + filePath, filePath)).build();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
            }
        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }

    @Override
    public Response doSearchMaterial(ConstructionTaskDetailDTO obj) throws Exception {
        // TODO Auto-generated method stub
        DataListDTO data = dmpnOrderBusinessImpl.doSearchForDetailMonth(obj);
        return Response.ok(data).build();

    }

    @Override
    public Response importYCVT(Attachment attachments, HttpServletRequest request) throws Exception {
        String filePath = writeFileToServer(attachments, request);
        try {
            try {
                return Response.ok(detailMonthPlanOSBusinessImpl.importYCVT(folderUpload + filePath, filePath)).build();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
            }
        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }

    @Override
    public Response updateListDmpnOrder(DetailMonthPlanSimpleDTO obj) throws Exception {
        UserToken objUser = (UserToken) request.getSession().getAttribute("vsaUserToken");
        detailMonthPlanOSBusinessImpl.updateListDmpnOrder(obj, objUser);
        return Response.ok().build();
    }

    @Override
    public Response exportDetailMonthPlan(DetailMonthPlanSimpleDTO obj) throws Exception {
        try {
            String strReturn = detailMonthPlanOSBusinessImpl.exportDetailMonthPlan(obj, request);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

    @Override
    public Response exportDetailMonthPlanTab1(ConstructionTaskDetailDTO obj) throws Exception {
    	//tanqn 20181109 start
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("EXPORT_DETAIL_MONTH_PLAN_TAB1");
        objKpiLog.setDescription("Kế hoạch tháng chi tiết");
        objKpiLog.setTransactionCode(obj.getConstructionCode());
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
        objKpiLog.setStartTime(dStart);
        try {
            String strReturn = constructionTaskBusinessImpl.exportDetailMonthPlanTab1(obj);
            Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)dEnd.getSeconds() - (long)dStart.getSeconds());
            objKpiLog.setStatus("1");
            yearPlanBusinessImpl.addKpiLog(objKpiLog);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
            Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)dEnd.getSeconds() - (long)dStart.getSeconds());
            objKpiLog.setStatus("0");
            objKpiLog.setReason(e.toString());
            yearPlanBusinessImpl.addKpiLog(objKpiLog);
        }//end
        return null;
    }

    @Override
    public Response exportDetailMonthPlanTab2(ConstructionTaskDetailDTO obj) throws Exception {
    	//tanqn 20181109 start
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("EXPORT_DETAIL_MONTH_PLAN_TAB2_OS");
        objKpiLog.setDescription("Kế hoạch tháng chi tiết ngoài OS");
        objKpiLog.setTransactionCode(obj.getConstructionCode());
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
        objKpiLog.setStartTime(dStart);
        try {
            String strReturn = constructionTaskBusinessImpl.exportDetailMonthPlanTab2(obj);
            Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)dEnd.getSeconds() - (long)dStart.getSeconds());
            objKpiLog.setStatus("1");
            yearPlanBusinessImpl.addKpiLog(objKpiLog);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
            Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)dEnd.getSeconds() - (long)dStart.getSeconds());
            objKpiLog.setStatus("0");
            objKpiLog.setReason(e.toString());
            yearPlanBusinessImpl.addKpiLog(objKpiLog);
        }
        return null;
    }

    @Override
    public Response exportDetailMonthPlanTab3(ConstructionTaskDetailDTO obj) throws Exception {
    	//tanqn 20181109 start
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("EXPORT_DETAIL_MONTH_PLAN_TAB3_OS");
        objKpiLog.setDescription("Kế hoạch tháng chi tiết ngoài OS");
        objKpiLog.setTransactionCode(obj.getConstructionCode());
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
        objKpiLog.setStartTime(dStart);
        try {
            String strReturn = constructionTaskBusinessImpl.exportDetailMonthPlanTab3(obj);
            Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)dEnd.getSeconds() - (long)dStart.getSeconds());
            objKpiLog.setStatus("1");
            yearPlanBusinessImpl.addKpiLog(objKpiLog);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
            Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)dEnd.getSeconds() - (long)dStart.getSeconds());
            objKpiLog.setStatus("0");
            objKpiLog.setReason(e.toString());
            yearPlanBusinessImpl.addKpiLog(objKpiLog);
        }
        return null;
    }

    @Override
    public Response exportDetailMonthPlanTab5(ConstructionTaskDetailDTO obj) throws Exception {
    	//tanqn 20181109 start
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("EXPORT_DETAIL_MONTH_PLAN_TAB5");
        objKpiLog.setDescription("Kế hoạch tháng chi tiết");
        objKpiLog.setTransactionCode(obj.getConstructionCode());
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
        objKpiLog.setStartTime(dStart);
        try {
            String strReturn = constructionTaskBusinessImpl.exportDetailMonthPlanTab5(obj);
            Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)dEnd.getSeconds() - (long)dStart.getSeconds());
            objKpiLog.setStatus("1");
            yearPlanBusinessImpl.addKpiLog(objKpiLog);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
            Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)dEnd.getSeconds() - (long)dStart.getSeconds());
            objKpiLog.setStatus("0");
            objKpiLog.setReason(e.toString());
            yearPlanBusinessImpl.addKpiLog(objKpiLog);
        }
        return null;
    }

    @Override
    public Response exportDetailMonthPlanTab6(ConstructionTaskDetailDTO obj) throws Exception {
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("EXPORT_DETAIL_MONTH_PLAN_TAB6");
        objKpiLog.setDescription("Kế hoạch tháng chi tiết");
        objKpiLog.setTransactionCode(obj.getConstructionCode());
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
        objKpiLog.setStartTime(dStart);
        try {
            String strReturn = constructionTaskBusinessImpl.exportDetailMonthPlanTab6(obj);
            Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)dEnd.getSeconds() - (long)dStart.getSeconds());
            objKpiLog.setStatus("1");
            yearPlanBusinessImpl.addKpiLog(objKpiLog);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
            Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)dEnd.getSeconds() - (long)dStart.getSeconds());
            objKpiLog.setStatus("0");
            objKpiLog.setReason(e.toString());
            yearPlanBusinessImpl.addKpiLog(objKpiLog);
        }
        return null;
    }

    @Override
    public Response exportDetailMonthPlanBTS(ConstructionTaskDetailDTO obj) throws Exception {
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("EXPORT_DETAIL_MONTH_PLAN_BTS");
        objKpiLog.setDescription("Kế hoạch tháng chi tiết");
        objKpiLog.setTransactionCode(obj.getConstructionCode());
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
        objKpiLog.setStartTime(dStart);
        try {
            String strReturn = dmpnOrderBusinessImpl.exportDetailMonthPlanBTS(obj);
            Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)dEnd.getSeconds() - (long)dStart.getSeconds());
            objKpiLog.setStatus("1");
            yearPlanBusinessImpl.addKpiLog(objKpiLog);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
            Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)dEnd.getSeconds() - (long)dStart.getSeconds());
            objKpiLog.setStatus("0");
            objKpiLog.setReason(e.toString());
            yearPlanBusinessImpl.addKpiLog(objKpiLog);
        }
        return null;
    }

    @Override
    public Response checkPermissionsAdd() {
        try {
            // TODO Auto-generated method stub
            KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
            Long sysGroupId = objUser.getVpsUserInfo().getSysGroupId();
            return Response.ok(detailMonthPlanOSBusinessImpl.checkPermissionsAdd(sysGroupId, request)).build();
        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }

    @Override
    public Response checkPermissionsCopy() {
        try {
            // TODO Auto-generated method stub
            KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
            Long sysGroupId = objUser.getVpsUserInfo().getSysGroupId();
            return Response.ok(detailMonthPlanOSBusinessImpl.checkPermissionsCopy(sysGroupId, request)).build();
        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }

    @Override
    public Response checkPermissionsDelete(DetailMonthPlanSimpleDTO obj) {
        try {
            // TODO Auto-generated method stub
            KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
            Long sysGroupId = objUser.getVpsUserInfo().getSysGroupId();
            return Response.ok(detailMonthPlanOSBusinessImpl.checkPermissionsDelete(sysGroupId, request, obj)).build();
        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }

    @Override
    public Response checkPermissionsRegistry() {
        try {
            // TODO Auto-generated method stub
            KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
            Long sysGroupId = objUser.getVpsUserInfo().getSysGroupId();
            return Response.ok(detailMonthPlanOSBusinessImpl.checkPermissionsRegistry(sysGroupId, request)).build();
        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }

    @Override
    public Response checkPermissionsUpdate(DetailMonthPlanSimpleDTO obj) {
        try {
            // TODO Auto-generated method stub
            KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
            Long sysGroupId = objUser.getVpsUserInfo().getSysGroupId();
            Long id = detailMonthPlanOSBusinessImpl.checkPermissionsUpdate(sysGroupId, request, obj);
            return Response.ok(id).build();
        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }
    //tanqn start 20181108
	@Override
	public Response removeRow(ConstructionTaskDTO obj) {
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        //tanqn 20181109 start
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("DELETE_DETAIL_YEAR_PLAN");
        objKpiLog.setDescription("Kế hoạch tháng chi tiết");
        objKpiLog.setTransactionCode(obj.getConstructionCode());
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
        objKpiLog.setStartTime(new Date());
		try {
//            String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.CREATE,
//                    Constant.AdResourceKey.MONTHLY_DETAIL_PLAN, request);
//            List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
//            if (!groupIdList.contains(obj.getSysGroupId().toString())) {
//                throw new IllegalArgumentException("Bạn không có quyền xóa kế hoạch tháng chi tiết cho đơn vị này");
//            }

            detailMonthPlanOSBusinessImpl.removeRow(obj);
            Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)dEnd.getSeconds() - (long)dStart.getSeconds());
            objKpiLog.setStatus("1");
            yearPlanBusinessImpl.addKpiLog(objKpiLog);
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
        	Date dEnd = new Date();
            objKpiLog.setEndTime(dEnd);
            objKpiLog.setDuration((long)dEnd.getSeconds() - (long)dStart.getSeconds());
            objKpiLog.setStatus("0");
            objKpiLog.setReason(e.toString());
            yearPlanBusinessImpl.addKpiLog(objKpiLog);
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
	}
	//tanqn end 20181108
	
	@Override
    public Response updateRegistry(DetailMonthPlanSimpleDTO obj) {
        // TODO Auto-generated method stub
        try {
            String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.CREATE,
                    Constant.AdResourceKey.MONTHLY_DETAIL_PLAN, request);
            List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
            if (!groupIdList.contains(obj.getSysGroupId().toString())) {
                throw new IllegalArgumentException("Bạn không có quyền trình ký kế hoạch tháng chi tiết cho đơn vị này");
            }
            detailMonthPlanOSBusinessImpl.updateRegistry(obj);
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }
	
	@Override
    public Response addTask(DetailMonthPlanSimpleDTO obj) throws Exception {
		KttsUserSession objUser = detailMonthPlanOSBusinessImpl.getUser(request);
    	String path = objUser.getVpsUserInfo().getPath();
    	String[] listGroup = path.split("\\/");
    	Long id = 0l;
        KpiLogDTO objKpiLog = new KpiLogDTO();
        Date dStart = new Date();
        objKpiLog.setStartTime(dStart);
        objKpiLog.setCreateDatetime(dStart);
        objKpiLog.setFunctionCode("INSERT_DETAIL_MONTH_PLAN_OS");
        objKpiLog.setDescription("Kế hoạch tháng chi tiết ngoài OS");
        objKpiLog.setTransactionCode(obj.getCode());
        objKpiLog.setCreateUserId(objUser.getVpsUserInfo().getSysUserId());
        try {
//			hoanm1_20180607_start
            String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.CREATE,
                    Constant.AdResourceKey.MONTHLY_DETAIL_PLAN, request);
            Long sysGroupIdDetail;
            List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
            if (!groupIdList.contains(listGroup[2])) {
            	id = 1l;
                throw new IllegalArgumentException("Bạn không có quyền tạo kế hoạch tháng chi tiết cho đơn vị này");
            }
            if (groupIdList.isEmpty()) {
            	id = 1l;
                throw new IllegalArgumentException(
                        "Không thể tạo công việc do người dùng chưa được phân quyền miền dữ liệu!");
            } else if (groupIdList.size() > 1) {
            	id = 1l;
                throw new IllegalArgumentException(
                        "Không thể tạo công việc do người dùng được phân quyền nhiều miền dữ liệu!");
            } else {
            	sysGroupIdDetail = Long.parseLong(groupIdList.get(0));
            }
//			hoanm1_20180607_end
            obj.setCreatedUserId(objUser.getVpsUserInfo().getSysUserId());
            obj.setStatus("1");
            obj.setCreatedDate(new Date());
            obj.setCreatedGroupId(objUser.getVpsUserInfo().getSysGroupId());
            obj.setSignState("1");
            Long ids = detailMonthPlanOSBusinessImpl.addTaskHTCT(obj, id, request,sysGroupIdDetail);
            if (ids == 0l) {
            	Date dEnd = new Date();
                objKpiLog.setEndTime(dEnd);
                objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
                objKpiLog.setStatus("0");
                yearPlanBusinessImpl.addKpiLog(objKpiLog);
                return Response.status(Response.Status.BAD_REQUEST).build();
            } else {
            	Date dEnd = new Date();
                objKpiLog.setEndTime(dEnd);
                objKpiLog.setDuration((long)(dEnd.getSeconds() - dStart.getSeconds()));
                objKpiLog.setStatus("1");
                yearPlanBusinessImpl.addKpiLog(objKpiLog);
                return Response.ok(Response.Status.CREATED).build();
            }
        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }

	@Override
    public Response importThuHoiDT(Attachment attachments, HttpServletRequest request) throws Exception {
        String filePath = writeFileToServer(attachments, request);
        try {
            try {
                return Response.ok(detailMonthPlanOSBusinessImpl.importThuHoiDT(folderUpload + filePath, filePath, request)).build();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
            }
        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }

	
	//tatph-start 19/12/2019
	   @Override
	    public Response doSearchManageValue(RevokeCashMonthPlanDTO obj) {
	        DataListDTO data = detailMonthPlanOSBusinessImpl.doSearchManageValue(obj ,request);
	        return Response.ok(data).build();
	    }
	   
	   @Override
		public Response updateRevokeCashMonthPlan(RevokeCashMonthPlanDTO obj) {
		   if (obj == null) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			} else {
				detailMonthPlanOSBusinessImpl.updateRevokeCashMonthPlan(obj, request);
				return Response.ok(Response.Status.NO_CONTENT).build();
			}
		}
	   
	   @Override
		public Response approveRevokeCashMonthPlan(RevokeCashMonthPlanDTO obj) {
		   if (obj == null) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			} else {
				detailMonthPlanOSBusinessImpl.approveRevokeCashMonthPlan(obj);
				return Response.ok(Response.Status.NO_CONTENT).build();
			}
		}
	   
	   
	   @Override
	    public Response getExcelTemplate(RevokeCashMonthPlanDTO obj) throws Exception {
	        try {
	            String strReturn = detailMonthPlanOSBusinessImpl.getExcelTemplate(obj,request);
	            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
	        } catch (Exception e) {
	            log.error(e);
	        }
	        return null;
	    }
	   
	   @Override
		public Response importManageValue(Attachment attachments, HttpServletRequest request) throws Exception {
			KttsUserSession objUser = detailMonthPlanOSBusinessImpl.getUserSession(request);
			String folderParam = UString.getSafeFileName(request.getParameter("folder"));
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

			// get filename to be uploaded
			MultivaluedMap<String, String> multivaluedMap = attachments.getHeaders();
			String fileName = UFile.getFileName(multivaluedMap);

			if (!isExtendAllowSave(fileName)) {
				throw new BusinessException("File extension khong nam trong list duoc up load, file_name:" + fileName);
			}

			// write & upload file to server
			try (InputStream inputStream = dataHandler.getInputStream();) {
				filePath = UFile.writeToFileServerATTT(inputStream, fileName, folderParam, folderUpload);
				filePathReturn = UEncrypt.encryptFileUploadPath(filePath);
			} catch (Exception ex) {
				throw new BusinessException("Loi khi save file", ex);
			}

			try {
				List<RevokeCashMonthPlanDTO> result = detailMonthPlanOSBusinessImpl
						.importManageValue( filePath);
				if (result != null && !result.isEmpty()
						&& (result.get(0).getErrorList() == null || result.get(0).getErrorList().size() == 0)) {
					for (RevokeCashMonthPlanDTO obj : result) {
						RevokeCashMonthPlanDTO dto = new RevokeCashMonthPlanDTO();
						dto.setKeySearch(obj.getBillCode());
						List<RevokeCashMonthPlanDTO> lst = detailMonthPlanOSBusinessImpl.doSearchManageValue(dto , request).getData();
						if(lst != null && lst.size() > 0) {
							lst.forEach(dtos -> {
								dtos.setPerformerId(this.getSysUserById(obj.getPerformerId()));
								dtos.setStartDate(obj.getStartDate());
								dtos.setEndDate(obj.getEndDate());
								dtos.setDescription(obj.getDescription());
								detailMonthPlanOSBusinessImpl.updateRevokeCashMonthPlan(dtos, request);
							});
							
						}
						
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
		
	   public Long getSysUserById(Long code) {
		   List<RevokeCashMonthPlanDTO> data = detailMonthPlanOSBusinessImpl.getSysUserById(code);
		   if(data.size() > 0) {
			   return data.get(0).getSysUserId();
		   }
		   return 0L;
	   }
	//tatph-end 19/12/2019
	@Override
	public Response getRevokeCashMonthPlanByPlanId(RevokeCashMonthPlanDTO obj) {
		DataListDTO data = detailMonthPlanOSBusinessImpl.getRevokeCashMonthPlanByPlanId(obj);
		return Response.ok(data).build();
	}

	//Huypq-20200113-start
	@Override
	public Response checkRoleTTHT() {
		// TODO Auto-generated method stub
		if (!detailMonthPlanOSBusinessImpl.checkRoleTTHT(request)) {
			return Response.ok().entity(Collections.singletonMap("error", "error")).build();
		} else {
			return Response.ok(Response.Status.OK).build();
		}
	}
	
	@Override
	public Response updateRejectRevokeCash(RevokeCashMonthPlanDTO obj) {
	   if (obj == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		} else {
			detailMonthPlanOSBusinessImpl.updateRejectRevokeCash(obj, request);
			return Response.ok(Response.Status.NO_CONTENT).build();
		}
	}
	//Huy-end
	
	//Huypq-20200513-start
	@Override
    public Response exportTemplateRent(SysUserDetailCOMSDTO obj) throws Exception {
        try {
            String strReturn = detailMonthPlanOSBusinessImpl.exportTemplateRent(request);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }
	
	//Import thuê mặt bằng trạm
	@Override
    public Response importRentGround(Attachment attachments, HttpServletRequest request) throws Exception {
        String filePath = writeFileToServer(attachments, request);
        try {
            try {
                return Response.ok(detailMonthPlanOSBusinessImpl.importRentGround(folderUpload + filePath, filePath)).build();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
            }
        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }
	//huy-end
	
	//Huypq-29062020-start
	@Override
    public Response exportTemplateTargetTTXD(SysUserDetailCOMSDTO obj) throws Exception {
        try {
            String strReturn = detailMonthPlanOSBusinessImpl.exportTemplateTargetTTXD(obj.getSysGroupId(), request);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }
	
	@Override
    public Response importTargetTTXD(Attachment attachments, HttpServletRequest request) throws Exception {
        String filePath = writeFileToServer(attachments, request);
        try {
            try {
                return Response.ok(detailMonthPlanOSBusinessImpl.importTargetTTXD(folderUpload + filePath, filePath)).build();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
            }
        } catch (IllegalArgumentException e) {
            return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
        }
    }
	//huy-end

	@Override
	public Response doSearchResultMonthQuantityTTXD(ConstructionTaskDetailDTO obj) {
		// TODO Auto-generated method stub
		return Response.ok(detailMonthPlanOSBusinessImpl.doSearchResultMonthQuantityTTXD(obj)).build();
	}

	@Override
	public Response getListAttachmentByIdAndType(Long id) throws Exception{
		// TODO Auto-generated method stub
		return Response.ok(detailMonthPlanOSBusinessImpl.getListAttachmentByIdAndType(id)).build();
	}
	
	public Response exportResultQuantityTTXD(ConstructionTaskDetailDTO obj) throws Exception {
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		try {
			String strReturn = detailMonthPlanOSBusinessImpl.exportResultQuantityTTXD(obj);
			return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
		} catch (Exception e) {
			log.error(e);
		}
		return null;
	}

	@Override
	public Response doSearchStaffByPopup(DetailMonthQuantityDTO obj) {
		// TODO Auto-generated method stub
		return Response.ok(detailMonthPlanOSBusinessImpl.doSearchStaffByPopup(obj)).build();
	}
	
	@Override
	public Response doSearchContractByPopup(DetailMonthQuantityDTO obj) {
		// TODO Auto-generated method stub
		return Response.ok(detailMonthPlanOSBusinessImpl.doSearchContractByPopup(obj)).build();
	}
	
	public Response exportGiaoChiTietTTXD(ConstructionTaskDetailDTO obj) throws Exception {
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		try {
			String strReturn = detailMonthPlanOSBusinessImpl.exportGiaoChiTietTTXD(obj);
			return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
		} catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	//Huy-end
}
