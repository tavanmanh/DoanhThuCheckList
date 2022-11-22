package com.viettel.coms.business;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.viettel.coms.bo.ConstructionTaskBO;
import com.viettel.coms.bo.DetailMonthPlanBO;
import com.viettel.coms.bo.RevokeCashMonthPlanBO;
import com.viettel.coms.dao.ConstructionDAO;
import com.viettel.coms.dao.ConstructionTaskDAO;
import com.viettel.coms.dao.DepartmentDAO;
import com.viettel.coms.dao.DetailMonthPlanOSDAO;
import com.viettel.coms.dao.DetailMonthQuantityDAO;
import com.viettel.coms.dao.DmpnOrderDAO;
import com.viettel.coms.dao.RevokeCashMonthPlanDAO;
import com.viettel.coms.dao.SysUserCOMSDAO;
import com.viettel.coms.dao.UtilAttachDocumentDAO;
import com.viettel.coms.dao.WorkItemDAO;
import com.viettel.coms.dto.CNTContractDTO;
import com.viettel.coms.dto.CatStationDTO;
import com.viettel.coms.dto.CommonDTO;
import com.viettel.coms.dto.ConstructionDTO;
import com.viettel.coms.dto.ConstructionDetailDTO;
import com.viettel.coms.dto.ConstructionTaskDTO;
import com.viettel.coms.dto.ConstructionTaskDetailDTO;
import com.viettel.coms.dto.DepartmentDTO;
import com.viettel.coms.dto.DetailMonthPlanDTO;
import com.viettel.coms.dto.DetailMonthPlanExportDTO;
import com.viettel.coms.dto.DetailMonthPlanSimpleDTO;
import com.viettel.coms.dto.DetailMonthPlaningDTO;
import com.viettel.coms.dto.DetailMonthQuantityDTO;
import com.viettel.coms.dto.DmpnOrderDTO;
import com.viettel.coms.dto.DmpnOrderDetailDTO;
import com.viettel.coms.dto.ExcelErrorDTO;
import com.viettel.coms.dto.RevokeCashMonthPlanDTO;
import com.viettel.coms.dto.SysUserCOMSDTO;
import com.viettel.coms.dto.TmpnTargetDetailDTO;
import com.viettel.coms.dto.UtilAttachDocumentDTO;
import com.viettel.coms.dto.WorkItemDTO;
import com.viettel.coms.dto.WorkItemDetailDTO;
import com.viettel.coms.utils.ExcelUtils;
import com.viettel.erp.dto.CntContractDTO;
import com.viettel.erp.dto.SysUserDTO;
import com.viettel.ktts.vps.VpsPermissionChecker;
import com.viettel.ktts2.common.BusinessException;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.utils.Constant;
import com.viettel.utils.ConvertData;
import com.viettel.utils.ImageUtil;
import com.viettel.wms.utils.ValidateUtils;

import viettel.passport.client.UserToken;

/**
 * @author HoangNH38
 */
@Service("detailMonthPlanOSBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DetailMonthPlanOSBusinessImpl extends BaseFWBusinessImpl<DetailMonthPlanOSDAO, DetailMonthPlanDTO, DetailMonthPlanBO>
	implements DetailMonthPlanOSBusiness{

	static Logger LOGGER = LoggerFactory.getLogger(DetailMonthPlanOSBusinessImpl.class);
	
    @Autowired
    private DetailMonthPlanOSDAO detailMonthPlanOSDAO;
    @Autowired
    private ConstructionTaskDAO constructionTaskDAO;
    @Autowired
    private ConstructionDAO constructionDAO;
    @Autowired
    private WorkItemDAO workItemDAO;
    @Autowired
    private DmpnOrderDAO dmpnOrderDAO;
    @Autowired
    private DepartmentDAO departmentDAO;
    @Autowired
    private SysUserCOMSDAO sysUserCOMSDAO;
    @Autowired
    private DetailMonthQuantityDAO detailMonthQuantityDAO;
    @Autowired
    private UtilAttachDocumentDAO utilAttachDocumentDAO;
    
   
    @Autowired
    private ConstructionTaskBusinessImpl constructionTaskBusinessImpl;
    
    @Autowired
    private RevokeCashMonthPlanDAO revokeCashMonthPlanDAO;

    private List<ConstructionDetailDTO> listConstruction = new ArrayList<ConstructionDetailDTO>();
    // List<CatStationDTO> listCatStation = constructionDAO.getCatStation(null);
    private List<DepartmentDTO> listSys = new ArrayList<DepartmentDTO>();
    private String DONG_TIEN = "Import_dongtien_thang_chitiet.xlsx";
    private String YEU_CAU_VAT_TU = "Import_vattu_thang_chitiet.xlsx";
    @Value("${folder_upload2}")
    private String folder2Upload;
    @Value("${default_sub_folder_upload}")
    private String defaultSubFolderUpload;

    // List<CatStationDTO> listCatStation = constructionDAO.getCatStation(null);
    // private List<DepartmentDTO> listSys = departmentDAO.getAll();

    // private List<ConstructionDetailDTO> listConstruction = new
    // ArrayList<ConstructionDetailDTO>();
    // List<CatStationDTO> listCatStation = constructionDAO.getCatStation(null);
    // private List<DepartmentDTO> listSys =new ArrayList<DepartmentDTO>();

    public DetailMonthPlanOSBusinessImpl() {
        tModel = new DetailMonthPlanBO();
        tDAO = detailMonthPlanOSDAO;
    }

    @Override
    public DetailMonthPlanOSDAO gettDAO() {
        return detailMonthPlanOSDAO;
    }

    @Override
    public long count() {
        return detailMonthPlanOSDAO.count("DetailMonthPlanBO", null);
    }

    public DataListDTO doSearch(DetailMonthPlanSimpleDTO obj, HttpServletRequest request) {
        List<DetailMonthPlaningDTO> ls = new ArrayList<DetailMonthPlaningDTO>();
        obj.setTotalRecord(0);
        String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
                Constant.AdResourceKey.MONTHLY_DETAIL_PLAN, request);
        List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
        if (groupIdList != null && !groupIdList.isEmpty())
            ls = detailMonthPlanOSDAO.doSearch(obj, groupIdList);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }

    public Long checkPermissionsAdd(Long sysGroupId, HttpServletRequest request) {

        if (!VpsPermissionChecker.hasPermission(Constant.OperationKey.CREATE,
                Constant.AdResourceKey.MONTHLY_DETAIL_PLAN, request)) {
            throw new IllegalArgumentException("Bạn không có quyền tạo mới kế hoạch tháng chi tiết");
        }
        try {
            return 0l;
        } catch (Exception e) {
            return 1l;
        }
    }

    public Long checkPermissionsCopy(Long sysGroupId, HttpServletRequest request) {

        if (!VpsPermissionChecker.hasPermission(Constant.OperationKey.CREATE,
                Constant.AdResourceKey.MONTHLY_DETAIL_PLAN, request)) {
            throw new IllegalArgumentException("Bạn không có quyền sao chép kế hoạch tháng chi tiết");
        }
        try {
            return 0l;
        } catch (Exception e) {
            return 1l;
        }
    }

    public Long checkPermissionsUpdate(Long sysGroupId, HttpServletRequest request, DetailMonthPlanSimpleDTO obj) {
        if (!VpsPermissionChecker.hasPermission(Constant.OperationKey.CREATE,
                Constant.AdResourceKey.MONTHLY_DETAIL_PLAN, request)) {
            return 2L;
        }
//		hoanm1_20180607_start
        String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.CREATE,
                Constant.AdResourceKey.MONTHLY_DETAIL_PLAN, request);
        List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
        if (!groupIdList.contains(obj.getSysGroupId().toString())) {
            return 2L;
        }
//		hoanm1_20180607_end
        try {
            return 0l;
        } catch (Exception e) {
            return 1l;
        }
    }

    public Long checkPermissionsDelete(Long sysGroupId, HttpServletRequest request, DetailMonthPlanSimpleDTO obj) {

        if (!VpsPermissionChecker.hasPermission(Constant.OperationKey.CREATE,
                Constant.AdResourceKey.MONTHLY_DETAIL_PLAN, request)) {
            throw new IllegalArgumentException("Bạn không có quyền xóa kế hoạch tháng chi tiết");
        }
//		hoanm1_20180607_start
        String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.CREATE,
                Constant.AdResourceKey.MONTHLY_DETAIL_PLAN, request);
        List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
        if (!groupIdList.contains(obj.getSysGroupId().toString())) {
            throw new IllegalArgumentException("Bạn không có quyền xóa kế hoạch tháng chi tiết cho đơn vị này");
        }
//		hoanm1_20180607_end
        try {
            return 0l;
        } catch (Exception e) {
            return 1l;
        }
    }

    public Long checkPermissionsRegistry(Long sysGroupId, HttpServletRequest request) {

        if (!VpsPermissionChecker.hasPermission(Constant.OperationKey.CREATE,
                Constant.AdResourceKey.MONTHLY_DETAIL_PLAN, request)) {
            throw new IllegalArgumentException("Bạn không có quyền trình kí kế hoạch tháng chi tiết");
        }
//		hoanm1_20180607_start
//		if (!VpsPermissionChecker.checkPermissionOnDomainData(
//				Constant.OperationKey.CREATE,
//				Constant.AdResourceKey.MONTHLY_DETAIL_PLAN,
//				sysGroupId, request)) {
//			throw new IllegalArgumentException(
//					"Bạn không có quyền trình kí kế hoạch tháng chi tiết cho đơn vị này");
//		}
//		hoanm1_20180607_end
        try {
            return 0l;
        } catch (Exception e) {
            return 1l;
        }
    }

    public Long addHTCT(DetailMonthPlanSimpleDTO obj, HttpServletRequest request) {
        KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        checkMonthYearSys(obj.getMonth(), obj.getYear(), obj.getSysGroupId(), null);//hoangnh cmt tạm
        obj.setType("1");//type ngoài OS
        Long detailMonthPlanId = detailMonthPlanOSDAO.saveObject(obj.toModel());
        HashMap<String, Long> checkDupMapWorkItem = new HashMap<String, Long>();

        Long i = 0L;
        if (obj.getListTC() != null && obj.getListTC().size() > 0) {
//          hoanm1_20181101_start
            HashMap<Long, String> sysGroupMap = new HashMap<Long, String>();
            List<DepartmentDTO> lstGroup = constructionTaskDAO.getListGroup();
            for(DepartmentDTO sys: lstGroup){
            	sysGroupMap.put(sys.getDepartmentId(),sys.getName());
            }
            Long checkGroupLevel1= constructionTaskDAO.getLevel1SysGroupId(obj.getSysGroupId(),"1",detailMonthPlanId);
            HashMap<Long,Long> constructionTaskSysGroupMap = new HashMap<Long,Long>();
            constructionTaskSysGroupMap.put(detailMonthPlanId, checkGroupLevel1);
            HashMap<String,Long> constructionCodeMap = new HashMap<String,Long>();
            List<ConstructionTaskDetailDTO> lstConstructionCode = constructionTaskDAO.getLevel2ConstructionCode("1",2L,detailMonthPlanId,obj.getSysGroupId());
            for(ConstructionTaskDetailDTO code: lstConstructionCode){
            	constructionCodeMap.put(code.getConstructionCode(),code.getConstructionTaskId());
            }
//            hoanm1_20181101_end
            for (ConstructionTaskDetailDTO dto : obj.getListTC()) {
            	String key ="";
            	Long workItemId = null;
            	String workItemName = null;
            	 if(dto.getWorkItemType().equals("2")){
            		List<WorkItemDTO> byId = detailMonthPlanOSDAO.getWiByIdNew(dto.getConstructionCode() + "_KHONGTHUONGXUYEN");
            		WorkItemDTO wDto = new WorkItemDTO();
            		wDto.setConstructionId(dto.getConstructionId());
            		wDto.setName(dto.getWorkItemName());
            		wDto.setIsInternal("1");
            		wDto.setConstructorId(obj.getSysGroupId());
            		wDto.setSupervisorId(obj.getSysGroupId());
            		wDto.setCreatedDate(new Date());
            		wDto.setStatus("1");
            		wDto.setCreatedUserId(obj.getCreatedUserId());
            		wDto.setCreatedGroupId(obj.getCreatedGroupId());
            		if(byId.size() == 0){
            			WorkItemDTO wType = detailMonthPlanOSDAO.getWiType();
                		wDto.setCatWorkItemTypeId(wType.getCatWorkItemTypeId());
                		wDto.setCode(dto.getConstructionCode() + "_KHONGTHUONGXUYEN");
                		workItemName = dto.getWorkItemName();
                		workItemId = workItemDAO.saveObject(wDto.toModel());
            		} else {
            			for(WorkItemDTO work : byId) {
            				if(!work.getName().equals(dto.getWorkItemName())) {
            					WorkItemDTO wType = detailMonthPlanOSDAO.getWiType();
                        		wDto.setCatWorkItemTypeId(wType.getCatWorkItemTypeId());
                        		wDto.setCode(dto.getConstructionCode() + "_KHONGTHUONGXUYEN");
                        		workItemName = dto.getWorkItemName();
                        		workItemId = workItemDAO.saveObject(wDto.toModel());
            				} else {
            					wDto.setCatWorkItemTypeId(work.getCatWorkItemTypeId());
                    			wDto.setCode(work.getCode());
                    			wDto.setWorkItemId(work.getWorkItemId());
                    			workItemName = work.getName();
                    			workItemId =work.getWorkItemId();
                    			workItemDAO.updateObject(wDto.toModel());
            				}
            			}
            		}
            		
            		key = dto.getConstructionId() + "|" + workItemId + "|" + dto.getPerformerId()+ "|" + workItemName;
            	} else if(dto.getWorkItemType().equals("1")){
            		key = dto.getConstructionId() + "|" + dto.getWorkItemId() + "|" + dto.getPerformerId();
            		workItemId = dto.getWorkItemId();
            	}
	                if (checkDupMapWorkItem.get(key) == null) {
	                	checkDupMapWorkItem.put(key, i++);
	                } else {
	                    continue;
	                }
	                dto.setLevelId(3L);
	                dto.setMonth(obj.getMonth());
	                dto.setSysGroupId(obj.getSysGroupId());
	                dto.setYear(obj.getYear());
	                dto.setType("1");
	                dto.setDetailMonthPlanId(detailMonthPlanId);
	                dto.setCreatedUserId(obj.getCreatedUserId());
	                dto.setCreatedDate(new Date());
	                dto.setCreatedGroupId(obj.getCreatedGroupId());
	                dto.setSysGroupName(sysGroupMap.get(obj.getSysGroupId()));
	//              hoanm1_20181101_end
	                dto.setPerformerWorkItemId(dto.getPerformerId());
	                dto.setTaskName(dto.getWorkItemName());
	                dto.setWorkItemId(workItemId);
	                dto.setBaselineStartDate(dto.getStartDate());
	                dto.setBaselineEndDate(dto.getEndDate());
	                dto.setStatus("1");
	                dto.setCompleteState("1");
	                dto.setPerformerWorkItemId(dto.getPerformerId());
	                getParentTaskForThiCong(dto, "1", detailMonthPlanId, obj.getSysGroupId(),constructionCodeMap,constructionTaskSysGroupMap);
	                ConstructionTaskBO bo = dto.toModel();
	                Long id = constructionTaskDAO.saveObject(bo);
	                
	                detailMonthPlanOSDAO.updatePerforment(dto.getPerformerId(), workItemId);
	                constructionTaskDAO.getSession().flush();
	                bo.setPath(dto.getPath() + id + "/");
	                bo.setWorkItemType(dto.getWorkItemType());
	                bo.setType("1");
	                dto.setPath(dto.getPath() + id + "/");
	                dto.setConstructionTaskId(id);
	                constructionTaskDAO.updateObject(bo);
              //hoanm1_20181001_start
//                constructionTaskBusinessImpl.createSendSmsEmail(dto, user);
              //hoanm1_20181001_end
//                if(workItemId != null && dto.getCheckHTCT() == 1) {
//                	insertTaskByWorkItemHTCT("1", workItemId, dto);
//                }
//                else if (workItemId != null && dto.getCatConstructionTypeId() !=3L &&  dto.getCheckHTCT() != 1){
//                	insertTaskByWorkItem("1", workItemId, dto);
//                }else if (workItemId != null && dto.getCatConstructionTypeId() ==3L &&  dto.getCheckHTCT() != 1){
//                	insertTaskByWorkItemGpon("1", workItemId, dto);
//                }
	              //tatph-start-21112019
	                 if (workItemId != null && dto.getCatConstructionTypeId() !=3L ){
	                	insertTaskByWorkItem("1", workItemId, dto);
	                }else if (workItemId != null && dto.getCatConstructionTypeId() ==3L ){
	                	insertTaskByWorkItemGpon("1", workItemId, dto);
	                }
	                 //tatph-start-21112019
            }
        }
        if (obj.getListHSHC() != null && obj.getListHSHC().size() > 0) {
            ConstructionTaskDetailDTO parent = new ConstructionTaskDetailDTO();
            getParentTask(parent, "1", detailMonthPlanId, obj.getSysGroupId(), obj);
            for (ConstructionTaskDetailDTO dto : obj.getListHSHC()) {
                dto.setMonth(obj.getMonth());
                dto.setSysGroupId(obj.getSysGroupId());
                dto.setYear(obj.getYear());
                dto.setType("2");
                dto.setDetailMonthPlanId(detailMonthPlanId);
                dto.setLevelId(4L);
                dto.setTaskName("Làm Quỹ lương cho công trình " + dto.getConstructionCode());
                dto.setParentId(parent.getConstructionTaskId());
                dto.setCreatedUserId(obj.getCreatedUserId());
                dto.setCreatedDate(new Date());
                dto.setCreatedGroupId(obj.getCreatedGroupId());
                dto.setBaselineStartDate(dto.getStartDate());
                dto.setBaselineEndDate(dto.getEndDate());
                dto.setStatus("1");
                dto.setCompleteState("1");
                if (dto.getSupervisorId() == null)
                    dto.setSupervisorId(
                            Double.parseDouble(constructionTaskDAO.getSuperVisorId(dto.getConstructionCode())));
                if (dto.getDirectorId() == null)
                    dto.setDirectorId(Double.parseDouble(constructionTaskDAO.getDirectorId(dto.getConstructionCode())));
                dto.setPerformerWorkItemId(dto.getPerformerId());
                ConstructionTaskBO bo = dto.toModel();
                Long id = constructionTaskDAO.saveObject(bo);
                bo.setPath(parent.getPath() + id + "/");
                bo.setType("2");
                constructionTaskDAO.updateObject(bo);
                // hoanm1_20181001_start
//                constructionTaskBusinessImpl.createSendSmsEmail(dto, user);
//                hoanm1_20181001_end
            }
        }
        
        if (obj.getListLDT() != null && obj.getListLDT().size() > 0) {
            ConstructionTaskDetailDTO parent = new ConstructionTaskDetailDTO();
            getParentTask(parent, "1", detailMonthPlanId, obj.getSysGroupId(), obj);
            for (ConstructionTaskDetailDTO dto : obj.getListLDT()) {
                dto.setMonth(obj.getMonth());
                dto.setSysGroupId(obj.getSysGroupId());
                dto.setYear(obj.getYear());
                dto.setType("3");
                dto.setDetailMonthPlanId(detailMonthPlanId);
                dto.setLevelId(4L);
                if (dto.getTaskOrder().equalsIgnoreCase("2")) {
                    dto.setTaskName("Lên doanh thu cho công trình " + dto.getConstructionCode());
                } else {
                    dto.setTaskName("Tạo đề nghị quyết toán cho công trình " + dto.getConstructionCode());
                }
                dto.setParentId(parent.getConstructionTaskId());
                dto.setCreatedUserId(obj.getCreatedUserId());
                dto.setCreatedDate(new Date());
                dto.setCreatedGroupId(obj.getCreatedGroupId());
                dto.setBaselineStartDate(dto.getStartDate());
                dto.setBaselineEndDate(dto.getEndDate());
                dto.setStatus("1");
                dto.setCompleteState("1");
                if (dto.getSupervisorId() == null)
                    dto.setSupervisorId(
                            Double.parseDouble(constructionTaskDAO.getSuperVisorId(dto.getConstructionCode())));
                if (dto.getDirectorId() == null)
                    dto.setDirectorId(Double.parseDouble(constructionTaskDAO.getDirectorId(dto.getConstructionCode())));
                dto.setPerformerWorkItemId(dto.getPerformerId());
                ConstructionTaskBO bo = dto.toModel();
                Long id = constructionTaskDAO.saveObject(bo);
                bo.setPath(parent.getPath() + id + "/");
                bo.setType("3");
                bo.setTaskOrder("2");
                constructionTaskDAO.updateObject(bo);
                // hoanm1_20181001_start
//                constructionTaskBusinessImpl.createSendSmsEmail(dto, user);
//                hoanm1_20181001_end
            }
        }
        
        //Huypq-20191218-start
        if(obj.getListRevokeDT()!=null && obj.getListRevokeDT().size()>0) {
        	for(RevokeCashMonthPlanDTO dto : obj.getListRevokeDT()) {
        		dto.setCreatedUserId(user.getSysUserId());
        		dto.setCreatedDate(new Date());
        		dto.setSignState("1");
        		dto.setStatus(1l);
        		dto.setDetailMonthPlanId(detailMonthPlanId);
        		dto.setSysGroupId(obj.getSysGroupId());
        		revokeCashMonthPlanDAO.saveObject(dto.toModel());
        	}
        }
        //Huy-end
        //Huypq-20200513-start
        if(obj.getListRentGround()!=null && obj.getListRentGround().size()>0) {
        	ConstructionTaskDetailDTO parent = new ConstructionTaskDetailDTO();
            getParentTask(parent, "1", detailMonthPlanId, obj.getSysGroupId(), obj);
             for(ConstructionTaskDetailDTO dto : obj.getListRentGround()) {
            	 dto.setMonth(obj.getMonth());
                 dto.setSysGroupId(obj.getSysGroupId());
                 dto.setYear(obj.getYear());
                 dto.setType("4");
                 dto.setDetailMonthPlanId(detailMonthPlanId);
                 dto.setLevelId(4L);
                 dto.setTaskName("Thuê mặt bằng trạm hạ tầng cho thuê, mã trạm " + dto.getStationCode());
                 dto.setParentId(parent.getConstructionTaskId());
                 dto.setCreatedUserId(obj.getCreatedUserId());
                 dto.setCreatedDate(new Date());
                 dto.setCreatedGroupId(obj.getCreatedGroupId());
                 dto.setBaselineStartDate(dto.getStartDate());
                 dto.setBaselineEndDate(dto.getEndDate());
                 dto.setStatus("1");
                 dto.setCompleteState("1");
                 dto.setCompletePercent(0d);
                 dto.setPerformerWorkItemId(dto.getPerformerId());
                 ConstructionTaskBO bo = dto.toModel();
                 Long id = constructionTaskDAO.saveObject(bo);
                 bo.setPath(parent.getPath() + id + "/");
                 bo.setType("4");
                 constructionTaskDAO.updateObject(bo);
             }
        }
        //huy-end
        return detailMonthPlanId;
    }
    
//    public Long add(DetailMonthPlanSimpleDTO obj, HttpServletRequest request) {
//        KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
//        checkMonthYearSys(obj.getMonth(), obj.getYear(), obj.getSysGroupId(), null);//hoangnh cmt tạm
//        obj.setType("1");//type ngoài OS
//        Long detailMonthPlanId = detailMonthPlanOSDAO.saveObject(obj.toModel());
//        HashMap<String, Long> checkDupMapWorkItem = new HashMap<String, Long>();
//
//        Long i = 0L;
//        if (obj.getListTC() != null && obj.getListTC().size() > 0) {
////          hoanm1_20181101_start
//            HashMap<Long, String> sysGroupMap = new HashMap<Long, String>();
//            List<DepartmentDTO> lstGroup = constructionTaskDAO.getListGroup();
//            for(DepartmentDTO sys: lstGroup){
//            	sysGroupMap.put(sys.getDepartmentId(),sys.getName());
//            }
//            Long checkGroupLevel1= constructionTaskDAO.getLevel1SysGroupId(obj.getSysGroupId(),"1",detailMonthPlanId);
//            HashMap<Long,Long> constructionTaskSysGroupMap = new HashMap<Long,Long>();
//            constructionTaskSysGroupMap.put(detailMonthPlanId, checkGroupLevel1);
//            HashMap<String,Long> constructionCodeMap = new HashMap<String,Long>();
//            List<ConstructionTaskDetailDTO> lstConstructionCode = constructionTaskDAO.getLevel2ConstructionCode("1",2L,detailMonthPlanId,obj.getSysGroupId());
//            for(ConstructionTaskDetailDTO code: lstConstructionCode){
//            	constructionCodeMap.put(code.getConstructionCode(),code.getConstructionTaskId());
//            }
////            hoanm1_20181101_end
//            for (ConstructionTaskDetailDTO dto : obj.getListTC()) {
//            	String key ="";
//            	Long workItemId = null;
//            	
//            	 if(dto.getWorkItemType().equals("2")){
//            		WorkItemDTO byId = detailMonthPlanOSDAO.getWiById(dto.getConstructionCode() + "_KHONGTHUONGXUYEN");
//            		WorkItemDTO wDto = new WorkItemDTO();
//            		wDto.setConstructionId(dto.getConstructionId());
//            		wDto.setName(dto.getWorkItemName());
//            		wDto.setIsInternal("1");
//            		wDto.setConstructorId(obj.getSysGroupId());
//            		wDto.setSupervisorId(obj.getSysGroupId());
//            		wDto.setCreatedDate(new Date());
//            		wDto.setStatus("1");
//            		wDto.setCreatedUserId(obj.getCreatedUserId());
//            		wDto.setCreatedGroupId(obj.getCreatedGroupId());
//            		if(byId == null){
//            			WorkItemDTO wType = detailMonthPlanOSDAO.getWiType();
//                		wDto.setCatWorkItemTypeId(wType.getCatWorkItemTypeId());
//                		wDto.setCode(dto.getConstructionCode() + "_KHONGTHUONGXUYEN");
//                		workItemId = workItemDAO.saveObject(wDto.toModel());
//            		} else {
//            			wDto.setCatWorkItemTypeId(byId.getCatWorkItemTypeId());
//            			wDto.setCode(byId.getCode());
//            			wDto.setWorkItemId(byId.getWorkItemId());
//            			workItemId =byId.getWorkItemId();
//            			workItemDAO.updateObject(wDto.toModel());
//            		}
//            		
//            		key = dto.getConstructionId() + "|" + workItemId + "|" + dto.getPerformerId();
//            	} else if(dto.getWorkItemType().equals("1")){
//            		key = dto.getConstructionId() + "|" + dto.getWorkItemId() + "|" + dto.getPerformerId();
//            		workItemId = dto.getWorkItemId();
//            	}
//	                if (checkDupMapWorkItem.get(key) == null) {
//	                	checkDupMapWorkItem.put(key, i++);
//	                } else {
//	                    continue;
//	                }
//	                dto.setLevelId(3L);
//	                dto.setMonth(obj.getMonth());
//	                dto.setSysGroupId(obj.getSysGroupId());
//	                dto.setYear(obj.getYear());
//	                dto.setType("1");
//	                dto.setDetailMonthPlanId(detailMonthPlanId);
//	                dto.setCreatedUserId(obj.getCreatedUserId());
//	                dto.setCreatedDate(new Date());
//	                dto.setCreatedGroupId(obj.getCreatedGroupId());
//	                dto.setSysGroupName(sysGroupMap.get(obj.getSysGroupId()));
//	//              hoanm1_20181101_end
//	                dto.setPerformerWorkItemId(dto.getPerformerId());
//	                dto.setTaskName(dto.getWorkItemName());
//	                dto.setWorkItemId(workItemId);
//	                dto.setBaselineStartDate(dto.getStartDate());
//	                dto.setBaselineEndDate(dto.getEndDate());
//	                dto.setStatus("1");
//	                dto.setCompleteState("1");
//	                dto.setPerformerWorkItemId(dto.getPerformerId());
//	                getParentTaskForThiCong(dto, "1", detailMonthPlanId, obj.getSysGroupId(),constructionCodeMap,constructionTaskSysGroupMap);
//	                ConstructionTaskBO bo = dto.toModel();
//	                Long id = constructionTaskDAO.saveObject(bo);
//	                detailMonthPlanOSDAO.updatePerforment(dto.getPerformerId(), workItemId);
//	                constructionTaskDAO.getSession().flush();
//	                bo.setPath(dto.getPath() + id + "/");
//	                bo.setWorkItemType(dto.getWorkItemType());
//	                bo.setType("1");
//	                dto.setPath(dto.getPath() + id + "/");
//	                dto.setConstructionTaskId(id);
//	                constructionTaskDAO.updateObject(bo);
//              //hoanm1_20181001_start
////                constructionTaskBusinessImpl.createSendSmsEmail(dto, user);
//              //hoanm1_20181001_end
//                if(workItemId != null && dto.getCheckHTCT() == 1) {
//                	insertTaskByWorkItemHTCT("1", workItemId, dto);
//                }
//                else if (workItemId != null && dto.getCatConstructionTypeId() !=3L &&  dto.getCheckHTCT() != 1){
//                	insertTaskByWorkItem("1", workItemId, dto);
//                }else {
//                	insertTaskByWorkItemGpon("1", workItemId, dto);
//                }
//            }
//        }
//        if (obj.getListHSHC() != null && obj.getListHSHC().size() > 0) {
//            ConstructionTaskDetailDTO parent = new ConstructionTaskDetailDTO();
//            getParentTask(parent, "1", detailMonthPlanId, obj.getSysGroupId(), obj);
//            for (ConstructionTaskDetailDTO dto : obj.getListHSHC()) {
//                dto.setMonth(obj.getMonth());
//                dto.setSysGroupId(obj.getSysGroupId());
//                dto.setYear(obj.getYear());
//                dto.setType("2");
//                dto.setDetailMonthPlanId(detailMonthPlanId);
//                dto.setLevelId(4L);
//                dto.setTaskName("Làm Quỹ lương cho công trình " + dto.getConstructionCode());
//                dto.setParentId(parent.getConstructionTaskId());
//                dto.setCreatedUserId(obj.getCreatedUserId());
//                dto.setCreatedDate(new Date());
//                dto.setCreatedGroupId(obj.getCreatedGroupId());
//                dto.setBaselineStartDate(dto.getStartDate());
//                dto.setBaselineEndDate(dto.getEndDate());
//                dto.setStatus("1");
//                dto.setCompleteState("1");
//                if (dto.getSupervisorId() == null)
//                    dto.setSupervisorId(
//                            Double.parseDouble(constructionTaskDAO.getSuperVisorId(dto.getConstructionCode())));
//                if (dto.getDirectorId() == null)
//                    dto.setDirectorId(Double.parseDouble(constructionTaskDAO.getDirectorId(dto.getConstructionCode())));
//                dto.setPerformerWorkItemId(dto.getPerformerId());
//                ConstructionTaskBO bo = dto.toModel();
//                Long id = constructionTaskDAO.saveObject(bo);
//                bo.setPath(parent.getPath() + id + "/");
//                bo.setType("2");
//                constructionTaskDAO.updateObject(bo);
//                // hoanm1_20181001_start
////                constructionTaskBusinessImpl.createSendSmsEmail(dto, user);
////                hoanm1_20181001_end
//            }
//        }
//        
//        if (obj.getListLDT() != null && obj.getListLDT().size() > 0) {
//            ConstructionTaskDetailDTO parent = new ConstructionTaskDetailDTO();
//            getParentTask(parent, "1", detailMonthPlanId, obj.getSysGroupId(), obj);
//            for (ConstructionTaskDetailDTO dto : obj.getListLDT()) {
//                dto.setMonth(obj.getMonth());
//                dto.setSysGroupId(obj.getSysGroupId());
//                dto.setYear(obj.getYear());
//                dto.setType("3");
//                dto.setDetailMonthPlanId(detailMonthPlanId);
//                dto.setLevelId(4L);
//                if (dto.getTaskOrder().equalsIgnoreCase("2")) {
//                    dto.setTaskName("Lên doanh thu cho công trình " + dto.getConstructionCode());
//                } else {
//                    dto.setTaskName("Tạo đề nghị quyết toán cho công trình " + dto.getConstructionCode());
//                }
//                dto.setParentId(parent.getConstructionTaskId());
//                dto.setCreatedUserId(obj.getCreatedUserId());
//                dto.setCreatedDate(new Date());
//                dto.setCreatedGroupId(obj.getCreatedGroupId());
//                dto.setBaselineStartDate(dto.getStartDate());
//                dto.setBaselineEndDate(dto.getEndDate());
//                dto.setStatus("1");
//                dto.setCompleteState("1");
//                if (dto.getSupervisorId() == null)
//                    dto.setSupervisorId(
//                            Double.parseDouble(constructionTaskDAO.getSuperVisorId(dto.getConstructionCode())));
//                if (dto.getDirectorId() == null)
//                    dto.setDirectorId(Double.parseDouble(constructionTaskDAO.getDirectorId(dto.getConstructionCode())));
//                dto.setPerformerWorkItemId(dto.getPerformerId());
//                ConstructionTaskBO bo = dto.toModel();
//                Long id = constructionTaskDAO.saveObject(bo);
//                bo.setPath(parent.getPath() + id + "/");
//                bo.setType("3");
//                constructionTaskDAO.updateObject(bo);
//                // hoanm1_20181001_start
////                constructionTaskBusinessImpl.createSendSmsEmail(dto, user);
////                hoanm1_20181001_end
//            }
//        }
//        
//       
//        return detailMonthPlanId;
//    }


    public void checkMonthYearSys(Long month, Long year, Long sysGroupId, Long detailMonthId) {
        boolean isExist = detailMonthPlanOSDAO.checkMonthYearSys(month, year, sysGroupId, detailMonthId);
        if (isExist)
            throw new IllegalArgumentException("Kế hoạch chi tiết của đơn vị đã tồn tại!");
    }

    public Long updateMonth(DetailMonthPlanSimpleDTO obj, HttpServletRequest request) {
    	KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        if (!VpsPermissionChecker.hasPermission(Constant.OperationKey.CREATE,
                Constant.AdResourceKey.MONTHLY_DETAIL_PLAN, request)) {
            throw new IllegalArgumentException("Bạn không có quyền chỉnh sửa kế hoạch tháng chi tiết");
        }
        if (!VpsPermissionChecker.checkPermissionOnDomainData(Constant.OperationKey.CREATE,
                Constant.AdResourceKey.MONTHLY_DETAIL_PLAN, obj.getSysGroupId(), request)) {
            throw new IllegalArgumentException("Bạn không có quyền chỉnh sửa kế hoạch tháng chi tiết cho đơn vị này");
        }
//        checkMonthYearSys(obj.getMonth(), obj.getYear(), obj.getSysGroupId(), obj.getDetailMonthPlanId());//hoangnh cmt tam
        obj.setType("1");
        detailMonthPlanOSDAO.updateObject(obj.toModel());
        
        if(obj.getListTC()!=null && obj.getListTC().size()>0) {
        	updateListTCHTCT(obj, user);
        }
        if(obj.getListHSHC()!=null && obj.getListHSHC().size()>0) {
        	updateListHSHC(obj, user);
        }
        if(obj.getListLDT()!=null && obj.getListLDT().size()>0) {
        	updateListLDT(obj, user);
        }
        if(obj.getListRevokeDT()!=null && obj.getListRevokeDT().size()>0) {
        	updateListRevokeDT(obj, user);
        }
        
        if(obj.getListRentGround()!=null && obj.getListRentGround().size()>0) {
        	updateListRentGround(obj, user);
        }
        return obj.getDetailMonthPlanId();
    }

    public DetailMonthPlanSimpleDTO getById(Long id) {
        DetailMonthPlanSimpleDTO dto = detailMonthPlanOSDAO.getById(id);
        return dto;
    }

    public void updateListTCHTCT(DetailMonthPlanSimpleDTO obj, KttsUserSession user) {
        Long detailMonthPlanId = obj.getDetailMonthPlanId();
        if ("1".equalsIgnoreCase(obj.getIsTCImport())) {
            // neu import tu file thi delete all trong db tru level = 1( dung
            // cho hshc,len doanh thu, cvkhac) va them moi all truyen
            // ve
            constructionTaskDAO.deleteConstructionTaskByType("1", detailMonthPlanId);
            constructionTaskDAO.getSession().flush();
            HashMap<String, Long> checkDupMapWorkItem = new HashMap<String, Long>();
            Long i = 0L;
            if (obj.getListTC() != null && !obj.getListTC().isEmpty()) {
//              hoanm1_20181101_start
                HashMap<Long, String> sysGroupMap = new HashMap<Long, String>();
                List<DepartmentDTO> lstGroup = constructionTaskDAO.getListGroup();
                for(DepartmentDTO sys: lstGroup){
                	sysGroupMap.put(sys.getDepartmentId(),sys.getName());
                }
                Long checkGroupLevel1= constructionTaskDAO.getLevel1SysGroupId(obj.getSysGroupId(),"1",detailMonthPlanId);
                HashMap<Long,Long> constructionTaskSysGroupMap = new HashMap<Long,Long>();
                constructionTaskSysGroupMap.put(detailMonthPlanId, checkGroupLevel1);
                HashMap<String,Long> constructionCodeMap = new HashMap<String,Long>();
                List<ConstructionTaskDetailDTO> lstConstructionCode = constructionTaskDAO.getLevel2ConstructionCode("1",2L,detailMonthPlanId,obj.getSysGroupId());
                for(ConstructionTaskDetailDTO code: lstConstructionCode){
                	constructionCodeMap.put(code.getConstructionCode(),code.getConstructionTaskId());
                }
//                hoanm1_20181101_end
                String key = "";
                for (ConstructionTaskDetailDTO dto : obj.getListTC()) {
                	Long workItemId = null;
                	String workItemName = null;
                	if(dto.getWorkItemType().equals("2")){
                		WorkItemDTO wDto = new WorkItemDTO();
                		List<WorkItemDTO> byId = detailMonthPlanOSDAO.getWiByIdNew(dto.getConstructionCode() + "_KHONGTHUONGXUYEN");
                    	wDto.setConstructionId(dto.getConstructionId());
                    	wDto.setName(dto.getWorkItemName());
                    	wDto.setIsInternal("1");
                    	wDto.setConstructorId(obj.getSysGroupId());
                    	wDto.setSupervisorId(obj.getSysGroupId());
                    	wDto.setCreatedDate(new Date());
                    	wDto.setStatus("1");
                    	wDto.setCreatedUserId(obj.getCreatedUserId());
                    	wDto.setCreatedGroupId(obj.getCreatedGroupId());
                    	//Huypq-20200114-start edit
                    	if(byId.size() == 0){
                			WorkItemDTO wType = detailMonthPlanOSDAO.getWiType();
                    		wDto.setCatWorkItemTypeId(wType.getCatWorkItemTypeId());
                    		wDto.setCode(dto.getConstructionCode() + "_KHONGTHUONGXUYEN");
                    		workItemName = dto.getWorkItemName();
                    		workItemId = workItemDAO.saveObject(wDto.toModel());
                		} else {
                			for(WorkItemDTO work : byId) {
                				if (!work.getName().equals(dto.getWorkItemName())) {
    								WorkItemDTO wType = detailMonthPlanOSDAO.getWiType();
    								wDto.setCatWorkItemTypeId(wType.getCatWorkItemTypeId());
    								wDto.setCode(dto.getConstructionCode() + "_KHONGTHUONGXUYEN");
    								workItemName = dto.getWorkItemName();
    								workItemId = workItemDAO.saveObject(wDto.toModel());
    							} else {
    								wDto.setCatWorkItemTypeId(work.getCatWorkItemTypeId());
    								wDto.setCode(work.getCode());
    								wDto.setWorkItemId(work.getWorkItemId());
    								workItemId = work.getWorkItemId();
    								workItemName = work.getName();
    								workItemDAO.updateObject(wDto.toModel());
    							}
                			}
                		}
                    	key = dto.getConstructionId() + "|" + workItemId + "|" + dto.getPerformerId()+ "|" + workItemName;
                    	//Huypq-20200114-end
                	} else {
                		key = dto.getConstructionId() + "|" + dto.getWorkItemId() + "|" + dto.getPerformerId();
                		workItemId = dto.getWorkItemId();
                	}
                    if (checkDupMapWorkItem.get(key) == null) {
                    	checkDupMapWorkItem.put(key, i++);
                    } else {
                        continue;
                    }
                    dto.setLevelId(3L);
                    dto.setMonth(obj.getMonth());
                    dto.setSysGroupId(obj.getSysGroupId());
                    dto.setYear(obj.getYear());
                    dto.setType("1");
                    dto.setDetailMonthPlanId(detailMonthPlanId);
                    dto.setSysGroupName(sysGroupMap.get(obj.getSysGroupId()));
                    dto.setPerformerWorkItemId(dto.getPerformerId());
                    dto.setTaskName(dto.getWorkItemName());
                    dto.setWorkItemId(workItemId);
                    dto.setBaselineStartDate(dto.getStartDate());
                    dto.setBaselineEndDate(dto.getEndDate());
                    dto.setStatus("1");
                    dto.setCompleteState("1");
                    dto.setPerformerWorkItemId(dto.getPerformerId());
                    dto.setCreatedDate(new Date());
                    dto.setCreatedUserId(user.getVpsUserInfo().getSysUserId());
                    dto.setCreatedGroupId(user.getVpsUserInfo().getSysGroupId());
                    getParentTaskForThiCong(dto, "1", detailMonthPlanId, obj.getSysGroupId(),constructionCodeMap,constructionTaskSysGroupMap);
                    ConstructionTaskBO bo = dto.toModel();
                    bo.setWorkItemType(dto.getWorkItemType());
                    bo.setType("1");
                    Long id = constructionTaskDAO.saveObject(bo);
                    detailMonthPlanOSDAO.updatePerforment(dto.getPerformerId(), workItemId);
                    bo.setPath(dto.getPath() + id + "/");
                    dto.setPath(dto.getPath() + id + "/");
                    dto.setConstructionTaskId(id);
                    constructionTaskDAO.updateObject(bo);

                    // chinhpxn20180718_start
//                    constructionTaskBusinessImpl.createSendSmsEmail(dto, user);
                    // chinhpxn20180718_end
//                    if(workItemId != null && dto.getCheckHTCT() == 1) {
//                    	insertTaskByWorkItemHTCT("1", workItemId, dto);
//                    }
//                    else if (workItemId != null && dto.getCatConstructionTypeId() !=3L && dto.getCheckHTCT() != 1){
//                    	insertTaskByWorkItem("1", workItemId, dto);
//                    }else if(workItemId != null && dto.getCatConstructionTypeId() ==3L && dto.getCheckHTCT() != 1) {
//                    	insertTaskByWorkItemGpon("1", workItemId, dto);
//                    }
                    
                    //tatph - start - 21112019
                     if (workItemId != null && dto.getCatConstructionTypeId() !=3L ){
                    	insertTaskByWorkItem("1", workItemId, dto);
                    }else if(workItemId != null && dto.getCatConstructionTypeId() ==3L ) {
                    	insertTaskByWorkItemGpon("1", workItemId, dto);
                    }
                     //tatph - end - 21112019
                }
            }

        } else {
            // neu khong thi xoa nhung ban ghi duoc chon va sua toi da 10 record
            if (obj.getListConstrTaskIdDelete() != null && obj.getListConstrTaskIdDelete().size() > 0) {
                // constructionTaskDAO.removeByListId(obj
                // .getListConstrTaskIdDelete());
                for (String id : obj.getListConstrTaskIdDelete()) {
                    constructionTaskDAO.deleteByParentId(id);
                    constructionTaskDAO.deleteParentAndChild(id);
                    constructionTaskDAO.getSession().flush();
                }
            }
            if (obj.getListTC() != null && !obj.getListTC().isEmpty()) {
                HashMap<String, Long> checkDupMapWorkItem = new HashMap<String, Long>();
                Long i = 0L;
//              hoanm1_20181101_start
                HashMap<Long, String> sysGroupMap = new HashMap<Long, String>();
                List<DepartmentDTO> lstGroup = constructionTaskDAO.getListGroup();
                for(DepartmentDTO sys: lstGroup){
                	sysGroupMap.put(sys.getDepartmentId(),sys.getName());
                }
                Long checkGroupLevel1= constructionTaskDAO.getLevel1SysGroupId(obj.getSysGroupId(),"1",detailMonthPlanId);
                HashMap<Long,Long> constructionTaskSysGroupMap = new HashMap<Long,Long>();
                constructionTaskSysGroupMap.put(detailMonthPlanId, checkGroupLevel1);
                HashMap<String,Long> constructionCodeMap = new HashMap<String,Long>();
                List<ConstructionTaskDetailDTO> lstConstructionCode = constructionTaskDAO.getLevel2ConstructionCode("1",2L,detailMonthPlanId,obj.getSysGroupId());
                for(ConstructionTaskDetailDTO code: lstConstructionCode){
                	constructionCodeMap.put(code.getConstructionCode(),code.getConstructionTaskId());
                }
//                hoanm1_20181101_end
                for (ConstructionTaskDetailDTO dto : obj.getListTC()) {
                    if (dto.getConstructionTaskId() == null) {
                        String key = "";
                        Long workItemType =null;
                        String workItemName = null;
                        if(dto.getWorkItemType().equals("2")){
                    		WorkItemDTO wDto = new WorkItemDTO();
                    		List<WorkItemDTO> byId = detailMonthPlanOSDAO.getWiByIdNew(dto.getConstructionCode() + "_KHONGTHUONGXUYEN");
                        	wDto.setConstructionId(dto.getConstructionId());
                        	wDto.setName(dto.getWorkItemName());
                        	wDto.setIsInternal("1");
                        	wDto.setConstructorId(obj.getSysGroupId());
                        	wDto.setSupervisorId(obj.getSysGroupId());
                        	wDto.setCreatedDate(new Date());
                        	wDto.setStatus("1");
                        	wDto.setCreatedUserId(obj.getCreatedUserId());
                        	wDto.setCreatedGroupId(obj.getCreatedGroupId());
                        	if(byId.size() == 0){
                    			WorkItemDTO wType = detailMonthPlanOSDAO.getWiType();
                        		wDto.setCatWorkItemTypeId(wType.getCatWorkItemTypeId());
                        		wDto.setCode(dto.getConstructionCode() + "_KHONGTHUONGXUYEN");
                        		workItemName = dto.getWorkItemName();
                        		workItemType = workItemDAO.saveObject(wDto.toModel());
                    		} else {
                    			for(WorkItemDTO work : byId) {
                    				if (!work.getName().equals(dto.getWorkItemName())) {
                    					WorkItemDTO wType = detailMonthPlanOSDAO.getWiType();
                                		wDto.setCatWorkItemTypeId(wType.getCatWorkItemTypeId());
                                		wDto.setCode(dto.getConstructionCode() + "_KHONGTHUONGXUYEN");
                                		workItemName = dto.getWorkItemName();
                                		workItemType = workItemDAO.saveObject(wDto.toModel());
        							} else {
        								wDto.setCatWorkItemTypeId(work.getCatWorkItemTypeId());
                            			wDto.setCode(work.getCode());
                            			wDto.setWorkItemId(work.getWorkItemId());
                            			workItemType =work.getWorkItemId();
                            			workItemName = work.getName();
                            			workItemDAO.updateObject(wDto.toModel());
        							}
                    			}
                    		}
                        	key = dto.getConstructionId() + "|" + workItemType + "|" + dto.getPerformerId() + "|" + workItemName;
                    		
                    	} else {
                    		key = dto.getConstructionId() + "|" + dto.getWorkItemId() + "|" + dto.getPerformerId();
                    		workItemType = dto.getWorkItemId();
                    	}
                        if (checkDupMapWorkItem.get(key) == null) {
                        	checkDupMapWorkItem.put(key, i++);
                        } else {
                            continue;
                        }
                        dto.setLevelId(3L);
                        dto.setMonth(obj.getMonth());
                        dto.setSysGroupId(obj.getSysGroupId());
                        dto.setYear(obj.getYear());
                        dto.setType("1");
                        dto.setDetailMonthPlanId(detailMonthPlanId);
                        dto.setSysGroupName(sysGroupMap.get(obj.getSysGroupId()));
                        getParentTaskForThiCong(dto, "1", detailMonthPlanId, obj.getSysGroupId(),constructionCodeMap,constructionTaskSysGroupMap);
                        dto.setCreatedDate(new Date());
                        dto.setCreatedUserId(user.getVpsUserInfo().getSysUserId());
                        dto.setCreatedGroupId(user.getVpsUserInfo().getSysGroupId());
                        dto.setPerformerWorkItemId(dto.getPerformerId());
                        /*
                         * WorkItemDetailDTO work = workItemDAO
                         * .getWorkItemByCodeNew(dto.getWorkItemName(),dto.getConstructionCode());
                         */
                        dto.setTaskName(dto.getWorkItemName());
                        dto.setWorkItemId(workItemType);
                        ConstructionTaskBO bo = dto.toModel();
                        ////////////
                        bo.setType(dto.getType());
                        bo.setWorkItemType(dto.getWorkItemType());
//                        bo.setCatTaskId(dto.getCatTaskId());
                        ////////////
                        Long id = constructionTaskDAO.saveObject(bo);
                        detailMonthPlanOSDAO.updatePerforment(dto.getPerformerId(), workItemType);
                        bo.setPath(dto.getPath() + id + "/");
                        dto.setPath(dto.getPath() + id + "/");
                        dto.setConstructionTaskId(id);
                        constructionTaskDAO.updateObject(bo);
                        if (workItemType != null){
                        	insertTaskByWorkItem("1", workItemType, dto);
                        }

                    } else {
                        dto.setLevelId(3L);
                        dto.setMonth(obj.getMonth());
                        dto.setSysGroupId(obj.getSysGroupId());
                        dto.setYear(obj.getYear());
                        dto.setType("1");
                        dto.setDetailMonthPlanId(detailMonthPlanId);
                        dto.setPerformerWorkItemId(dto.getPerformerId());
                        dto.setUpdatedDate(new Date());
                        dto.setUpdatedUserId(user.getVpsUserInfo().getSysUserId());
                        dto.setUpdatedGroupId(user.getVpsUserInfo().getSysGroupId());
                        ConstructionTaskBO bo = dto.toModel();
                        //////////////////
                        bo.setType("1");
                        bo.setWorkItemType(dto.getWorkItemType());
//                        bo.setCatTaskId(dto.getCatTaskId());
                        /////////////////
                        constructionTaskDAO.updateObject(bo);
                        detailMonthPlanOSDAO.updatePerforment(dto.getPerformerId(), dto.getWorkItemId());

                    }
                }
            }
        }
    }
    
//    public void updateListTC(DetailMonthPlanSimpleDTO obj, KttsUserSession user) {
//        Long detailMonthPlanId = obj.getDetailMonthPlanId();
//        if ("1".equalsIgnoreCase(obj.getIsTCImport())) {
//            // neu import tu file thi delete all trong db tru level = 1( dung
//            // cho hshc,len doanh thu, cvkhac) va them moi all truyen
//            // ve
//            constructionTaskDAO.deleteConstructionTaskByType("1", detailMonthPlanId);
//            constructionTaskDAO.getSession().flush();
//            HashMap<String, Long> checkDupMapWorkItem = new HashMap<String, Long>();
//            Long i = 0L;
//            if (obj.getListTC() != null && !obj.getListTC().isEmpty()) {
////              hoanm1_20181101_start
//                HashMap<Long, String> sysGroupMap = new HashMap<Long, String>();
//                List<DepartmentDTO> lstGroup = constructionTaskDAO.getListGroup();
//                for(DepartmentDTO sys: lstGroup){
//                	sysGroupMap.put(sys.getDepartmentId(),sys.getName());
//                }
//                Long checkGroupLevel1= constructionTaskDAO.getLevel1SysGroupId(obj.getSysGroupId(),"1",detailMonthPlanId);
//                HashMap<Long,Long> constructionTaskSysGroupMap = new HashMap<Long,Long>();
//                constructionTaskSysGroupMap.put(detailMonthPlanId, checkGroupLevel1);
//                HashMap<String,Long> constructionCodeMap = new HashMap<String,Long>();
//                List<ConstructionTaskDetailDTO> lstConstructionCode = constructionTaskDAO.getLevel2ConstructionCode("1",2L,detailMonthPlanId,obj.getSysGroupId());
//                for(ConstructionTaskDetailDTO code: lstConstructionCode){
//                	constructionCodeMap.put(code.getConstructionCode(),code.getConstructionTaskId());
//                }
////                hoanm1_20181101_end
//                String key = "";
//                for (ConstructionTaskDetailDTO dto : obj.getListTC()) {
//                	Long workItemId = null;
//                	if(dto.getWorkItemType().equals("2")){
//                		WorkItemDTO wDto = new WorkItemDTO();
//                		WorkItemDTO byId = detailMonthPlanOSDAO.getWiById(dto.getConstructionCode() + "_KHONGTHUONGXUYEN");
//                    	wDto.setConstructionId(dto.getConstructionId());
//                    	wDto.setName(dto.getWorkItemName());
//                    	wDto.setIsInternal("1");
//                    	wDto.setConstructorId(obj.getSysGroupId());
//                    	wDto.setSupervisorId(obj.getSysGroupId());
//                    	wDto.setCreatedDate(new Date());
//                    	wDto.setStatus("1");
//                    	wDto.setCreatedUserId(obj.getCreatedUserId());
//                    	wDto.setCreatedGroupId(obj.getCreatedGroupId());
//                    	if(byId == null){
//                			WorkItemDTO wType = detailMonthPlanOSDAO.getWiType();
//                    		wDto.setCatWorkItemTypeId(wType.getCatWorkItemTypeId());
//                    		wDto.setCode(dto.getConstructionCode() + "_KHONGTHUONGXUYEN");
//                    		workItemId = workItemDAO.saveObject(wDto.toModel());
//                		} else {
//                			wDto.setCatWorkItemTypeId(byId.getCatWorkItemTypeId());
//                			wDto.setCode(byId.getCode());
//                			wDto.setWorkItemId(byId.getWorkItemId());
//                			workItemId =byId.getWorkItemId();
//                			workItemDAO.updateObject(wDto.toModel());
//                		}
//                    	key = dto.getConstructionId() + "|" + workItemId + "|" + dto.getPerformerId();
//                	} else {
//                		key = dto.getConstructionId() + "|" + dto.getWorkItemId() + "|" + dto.getPerformerId();
//                		workItemId = dto.getWorkItemId();
//                	}
//                    if (checkDupMapWorkItem.get(key) == null) {
//                    	checkDupMapWorkItem.put(key, i++);
//                    } else {
//                        continue;
//                    }
//                    dto.setLevelId(3L);
//                    dto.setMonth(obj.getMonth());
//                    dto.setSysGroupId(obj.getSysGroupId());
//                    dto.setYear(obj.getYear());
//                    dto.setType("1");
//                    dto.setDetailMonthPlanId(detailMonthPlanId);
//                    dto.setSysGroupName(sysGroupMap.get(obj.getSysGroupId()));
//                    dto.setPerformerWorkItemId(dto.getPerformerId());
//                    dto.setTaskName(dto.getWorkItemName());
//                    dto.setWorkItemId(workItemId);
//                    dto.setBaselineStartDate(dto.getStartDate());
//                    dto.setBaselineEndDate(dto.getEndDate());
//                    dto.setStatus("1");
//                    dto.setCompleteState("1");
//                    dto.setPerformerWorkItemId(dto.getPerformerId());
//                    dto.setCreatedDate(new Date());
//                    dto.setCreatedUserId(user.getVpsUserInfo().getSysUserId());
//                    dto.setCreatedGroupId(user.getVpsUserInfo().getSysGroupId());
//                    getParentTaskForThiCong(dto, "1", detailMonthPlanId, obj.getSysGroupId(),constructionCodeMap,constructionTaskSysGroupMap);
//                    ConstructionTaskBO bo = dto.toModel();
//                    bo.setWorkItemType(dto.getWorkItemType());
//                    bo.setType("1");
//                    Long id = constructionTaskDAO.saveObject(bo);
//                    detailMonthPlanOSDAO.updatePerforment(dto.getPerformerId(), workItemId);
//                    bo.setPath(dto.getPath() + id + "/");
//                    dto.setPath(dto.getPath() + id + "/");
//                    dto.setConstructionTaskId(id);
//                    constructionTaskDAO.updateObject(bo);
//
//                    // chinhpxn20180718_start
////                    constructionTaskBusinessImpl.createSendSmsEmail(dto, user);
//                    // chinhpxn20180718_end
//
//                    if (workItemId != null && dto.getCatConstructionTypeId() !=3L){
//                    	insertTaskByWorkItem("1", workItemId, dto);
//                    }else {
//                    	insertTaskByWorkItemGpon("1", workItemId, dto);
//                    }
//                }
//            }
//
//        } else {
//            // neu khong thi xoa nhung ban ghi duoc chon va sua toi da 10 record
//            if (obj.getListConstrTaskIdDelete() != null && obj.getListConstrTaskIdDelete().size() > 0) {
//                // constructionTaskDAO.removeByListId(obj
//                // .getListConstrTaskIdDelete());
//                for (String id : obj.getListConstrTaskIdDelete()) {
//                    constructionTaskDAO.deleteByParentId(id);
//                    constructionTaskDAO.deleteParentAndChild(id);
//                    constructionTaskDAO.getSession().flush();
//                }
//            }
//            if (obj.getListTC() != null && !obj.getListTC().isEmpty()) {
//                HashMap<String, Long> checkDupMapWorkItem = new HashMap<String, Long>();
//                Long i = 0L;
////              hoanm1_20181101_start
//                HashMap<Long, String> sysGroupMap = new HashMap<Long, String>();
//                List<DepartmentDTO> lstGroup = constructionTaskDAO.getListGroup();
//                for(DepartmentDTO sys: lstGroup){
//                	sysGroupMap.put(sys.getDepartmentId(),sys.getName());
//                }
//                Long checkGroupLevel1= constructionTaskDAO.getLevel1SysGroupId(obj.getSysGroupId(),"1",detailMonthPlanId);
//                HashMap<Long,Long> constructionTaskSysGroupMap = new HashMap<Long,Long>();
//                constructionTaskSysGroupMap.put(detailMonthPlanId, checkGroupLevel1);
//                HashMap<String,Long> constructionCodeMap = new HashMap<String,Long>();
//                List<ConstructionTaskDetailDTO> lstConstructionCode = constructionTaskDAO.getLevel2ConstructionCode("1",2L,detailMonthPlanId,obj.getSysGroupId());
//                for(ConstructionTaskDetailDTO code: lstConstructionCode){
//                	constructionCodeMap.put(code.getConstructionCode(),code.getConstructionTaskId());
//                }
////                hoanm1_20181101_end
//                for (ConstructionTaskDetailDTO dto : obj.getListTC()) {
//                    if (dto.getConstructionTaskId() == null) {
//                        String key = "";
//                        Long workItemType =null;
//                        if(dto.getWorkItemType().equals("2")){
//                    		WorkItemDTO wDto = new WorkItemDTO();
//                    		WorkItemDTO byId = detailMonthPlanOSDAO.getWiById(dto.getConstructionCode() + "_KHONGTHUONGXUYEN");
//                        	wDto.setConstructionId(dto.getConstructionId());
//                        	wDto.setName(dto.getWorkItemName());
//                        	wDto.setIsInternal("1");
//                        	wDto.setConstructorId(obj.getSysGroupId());
//                        	wDto.setSupervisorId(obj.getSysGroupId());
//                        	wDto.setCreatedDate(new Date());
//                        	wDto.setStatus("1");
//                        	wDto.setCreatedUserId(obj.getCreatedUserId());
//                        	wDto.setCreatedGroupId(obj.getCreatedGroupId());
//                        	if(byId == null){
//                    			WorkItemDTO wType = detailMonthPlanOSDAO.getWiType();
//                        		wDto.setCatWorkItemTypeId(wType.getCatWorkItemTypeId());
//                        		wDto.setCode(dto.getConstructionCode() + "_KHONGTHUONGXUYEN");
//                        		workItemType = workItemDAO.saveObject(wDto.toModel());
//                    		} else {
//                    			wDto.setCatWorkItemTypeId(byId.getCatWorkItemTypeId());
//                    			wDto.setCode(byId.getCode());
//                    			wDto.setWorkItemId(byId.getWorkItemId());
//                    			workItemType =byId.getWorkItemId();
//                    			workItemDAO.updateObject(wDto.toModel());
//                    		}
//                        	key = dto.getConstructionId() + "|" + workItemType + "|" + dto.getPerformerId();
//                    		
//                    	} else {
//                    		key = dto.getConstructionId() + "|" + dto.getWorkItemId() + "|" + dto.getPerformerId();
//                    		workItemType = dto.getWorkItemId();
//                    	}
//                        if (checkDupMapWorkItem.get(key) == null) {
//                        	checkDupMapWorkItem.put(key, i++);
//                        } else {
//                            continue;
//                        }
//                        dto.setLevelId(3L);
//                        dto.setMonth(obj.getMonth());
//                        dto.setSysGroupId(obj.getSysGroupId());
//                        dto.setYear(obj.getYear());
//                        dto.setType("1");
//                        dto.setDetailMonthPlanId(detailMonthPlanId);
//                        dto.setSysGroupName(sysGroupMap.get(obj.getSysGroupId()));
//                        getParentTaskForThiCong(dto, "1", detailMonthPlanId, obj.getSysGroupId(),constructionCodeMap,constructionTaskSysGroupMap);
//                        dto.setCreatedDate(new Date());
//                        dto.setCreatedUserId(user.getVpsUserInfo().getSysUserId());
//                        dto.setCreatedGroupId(user.getVpsUserInfo().getSysGroupId());
//                        dto.setPerformerWorkItemId(dto.getPerformerId());
//                        /*
//                         * WorkItemDetailDTO work = workItemDAO
//                         * .getWorkItemByCodeNew(dto.getWorkItemName(),dto.getConstructionCode());
//                         */
//                        dto.setTaskName(dto.getWorkItemName());
//                        dto.setWorkItemId(workItemType);
//                        ConstructionTaskBO bo = dto.toModel();
//                        Long id = constructionTaskDAO.saveObject(bo);
//                        detailMonthPlanOSDAO.updatePerforment(dto.getPerformerId(), workItemType);
//                        bo.setPath(dto.getPath() + id + "/");
//                        dto.setPath(dto.getPath() + id + "/");
//                        dto.setConstructionTaskId(id);
//                        constructionTaskDAO.updateObject(bo);
//                        if (workItemType != null){
//                        	insertTaskByWorkItem("1", workItemType, dto);
//                        }
//
//                    } else {
//                        dto.setLevelId(3L);
//                        dto.setMonth(obj.getMonth());
//                        dto.setSysGroupId(obj.getSysGroupId());
//                        dto.setYear(obj.getYear());
//                        dto.setType("1");
//                        dto.setDetailMonthPlanId(detailMonthPlanId);
//                        dto.setPerformerWorkItemId(dto.getPerformerId());
//                        dto.setUpdatedDate(new Date());
//                        dto.setUpdatedUserId(user.getVpsUserInfo().getSysUserId());
//                        dto.setUpdatedGroupId(user.getVpsUserInfo().getSysGroupId());
//                        constructionTaskDAO.updateObject(dto.toModel());
//                        detailMonthPlanOSDAO.updatePerforment(dto.getPerformerId(), dto.getWorkItemId());
//
//                    }
//                }
//            }
//        }
//    }

    public void updateListHSHC(DetailMonthPlanSimpleDTO obj, KttsUserSession user) {
        Long detailMonthPlanId = obj.getDetailMonthPlanId();
        if ("1".equalsIgnoreCase(obj.getIsHSHCImport())) {
            // neu import thi xoa het ban ghi type 2 vao them moi
            constructionTaskDAO.deleteConstructionTaskByType("2", detailMonthPlanId);
            constructionTaskDAO.getSession().flush();
            if (obj.getListHSHC() != null && !obj.getListHSHC().isEmpty()) {
                ConstructionTaskDetailDTO parent = new ConstructionTaskDetailDTO();
                getParentTask(parent, "1", detailMonthPlanId, obj.getSysGroupId(), obj);
                for (ConstructionTaskDetailDTO dto : obj.getListHSHC()) {
                    dto.setMonth(obj.getMonth());
                    dto.setSysGroupId(obj.getSysGroupId());
                    dto.setYear(obj.getYear());
                    dto.setType("2");
                    dto.setDetailMonthPlanId(detailMonthPlanId);
                    dto.setLevelId(4L);
                    dto.setTaskName("Làm Quỹ lương cho công trình " + dto.getConstructionCode());
                    dto.setParentId(parent.getConstructionTaskId());
                    dto.setCreatedDate(new Date());
                    dto.setCreatedUserId(user.getVpsUserInfo().getSysUserId());
                    dto.setCreatedGroupId(user.getVpsUserInfo().getSysGroupId());
                    dto.setBaselineStartDate(dto.getStartDate());
                    dto.setBaselineEndDate(dto.getEndDate());
                    dto.setStatus("1");
                    dto.setCompleteState("1");
                    dto.setPerformerWorkItemId(dto.getPerformerId());
                    ConstructionTaskBO bo = dto.toModel();
                    bo.setType("2");
                    Long id = constructionTaskDAO.saveObject(bo);
                    bo.setPath(parent.getPath() + id + "/");
                    bo.setType("2");
                    constructionTaskDAO.updateObject(bo);
                    // chinhpxn20180718_start
//                    constructionTaskBusinessImpl.createSendSmsEmail(dto, user);
                    // chinhpxn20180718_end
                }
            }
        } else {
            if (obj.getListConstrTaskIdDelete() != null && obj.getListConstrTaskIdDelete().size() > 0) {
                for (String id : obj.getListConstrTaskIdDelete()) {
                    constructionTaskDAO.deleteParentAndChild(id);
                    constructionTaskDAO.getSession().flush();
                }

            }
            if (obj.getListHSHC() != null && !obj.getListHSHC().isEmpty()) {
                ConstructionTaskDetailDTO parent = new ConstructionTaskDetailDTO();
                getParentTask(parent, "1", detailMonthPlanId, obj.getSysGroupId(), obj);
                for (ConstructionTaskDetailDTO dto : obj.getListHSHC()) {
                    if (dto.getConstructionTaskId() == null) {
                        dto.setMonth(obj.getMonth());
                        dto.setSysGroupId(obj.getSysGroupId());
                        dto.setYear(obj.getYear());
                        dto.setDetailMonthPlanId(detailMonthPlanId);
                        dto.setLevelId(4L);
                        dto.setType("2");
                        dto.setTaskName("Làm Quỹ lương cho công trình " + dto.getConstructionCode());
                        dto.setParentId(parent.getConstructionTaskId());
                        dto.setCreatedDate(new Date());
                        dto.setCreatedUserId(user.getVpsUserInfo().getSysUserId());
                        dto.setCreatedGroupId(user.getVpsUserInfo().getSysGroupId());
                        dto.setBaselineStartDate(dto.getStartDate());
                        dto.setBaselineEndDate(dto.getEndDate());
                        dto.setStatus("1");
                        dto.setCompleteState("1");
                        dto.setPerformerWorkItemId(dto.getPerformerId());
                        ConstructionTaskBO bo = dto.toModel();
                        bo.setType("2");
                        Long id = constructionTaskDAO.saveObject(bo);
                        bo.setPath(parent.getPath() + id + "/");
                        bo.setType("2");
                        bo.setCatTaskId(dto.getCatTaskId());
                        
                        constructionTaskDAO.updateObject(bo);
                    } else {
                        dto.setMonth(obj.getMonth());
                        dto.setSysGroupId(obj.getSysGroupId());
                        dto.setYear(obj.getYear());
                        dto.setType("2");
                        dto.setDetailMonthPlanId(detailMonthPlanId);
                        dto.setLevelId(4L);
                        dto.setUpdatedDate(new Date());
                        dto.setUpdatedUserId(user.getVpsUserInfo().getSysUserId());
                        dto.setUpdatedGroupId(user.getVpsUserInfo().getSysGroupId());
                        ConstructionTaskBO bo = dto.toModel();
                        bo.setType("2");
                        constructionTaskDAO.updateObject(bo);
                    }
                }

            }
        }
    }

    public void updateListLDT(DetailMonthPlanSimpleDTO obj, KttsUserSession user) {
        Long detailMonthPlanId = obj.getDetailMonthPlanId();
        if ("1".equalsIgnoreCase(obj.getIsLDTImport())) {
            constructionTaskDAO.deleteConstructionTaskByType("3", detailMonthPlanId);
            constructionTaskDAO.getSession().flush();
            if (obj.getListLDT() != null && !obj.getListLDT().isEmpty()) {
                ConstructionTaskDetailDTO parent = new ConstructionTaskDetailDTO();
                getParentTask(parent, "1", detailMonthPlanId, obj.getSysGroupId(), obj);
                for (ConstructionTaskDetailDTO dto : obj.getListLDT()) {
                    dto.setMonth(obj.getMonth());
                    dto.setSysGroupId(obj.getSysGroupId());
                    dto.setYear(obj.getYear());
                    dto.setDetailMonthPlanId(detailMonthPlanId);
                    dto.setLevelId(4L);
                    dto.setType("3");
                    if (dto.getTaskOrder().equalsIgnoreCase("2")) {
                        dto.setTaskName("Lên doanh thu cho công trình " + dto.getConstructionCode());
                    } else {
                        dto.setTaskName("Tạo đề nghị quyết toán cho công trình " + dto.getConstructionCode());
                    }
                    dto.setParentId(parent.getConstructionTaskId());
                    dto.setCreatedDate(new Date());
                    dto.setCreatedUserId(user.getVpsUserInfo().getSysUserId());
                    dto.setCreatedGroupId(user.getVpsUserInfo().getSysGroupId());
                    dto.setBaselineStartDate(dto.getStartDate());
                    dto.setBaselineEndDate(dto.getEndDate());
                    dto.setStatus("1");
                    dto.setCompleteState("1");
                    dto.setPerformerWorkItemId(dto.getPerformerId());
                    ConstructionTaskBO bo = dto.toModel();
                    bo.setType("3");
                    Long id = constructionTaskDAO.saveObject(bo);
                    bo.setPath(parent.getPath() + id + "/");
                    bo.setType("3");
                    bo.setTaskOrder("2");
                    constructionTaskDAO.updateObject(bo);
                    // chinhpxn20180718_start
//                    constructionTaskBusinessImpl.createSendSmsEmail(dto, user);
                    // chinhpxn20180718_end
                }
            }
        } else {
            if (obj.getListConstrTaskIdDelete() != null && obj.getListConstrTaskIdDelete().size() > 0) {
                for (String id : obj.getListConstrTaskIdDelete()) {
                    constructionTaskDAO.deleteParentAndChild(id);
                    constructionTaskDAO.getSession().flush();
                }

            }
            if (obj.getListLDT() != null && !obj.getListLDT().isEmpty()) {
                ConstructionTaskDetailDTO parent = new ConstructionTaskDetailDTO();
                getParentTask(parent, "3", detailMonthPlanId, obj.getSysGroupId(), obj);
                for (ConstructionTaskDetailDTO dto : obj.getListLDT()) {
                    if (dto.getConstructionTaskId() == null) {
                        // dto.setPerformerId(getSysId(listSys,dto.getPerformerName()));
                        dto.setMonth(obj.getMonth());
                        dto.setSysGroupId(obj.getSysGroupId());
                        dto.setYear(obj.getYear());
                        dto.setDetailMonthPlanId(detailMonthPlanId);
                        dto.setLevelId(4L);
                        dto.setType("3");
                        dto.setTaskName("Lên doanh thu cho công trình " + dto.getConstructionCode());
                        dto.setParentId(parent.getConstructionTaskId());
                        dto.setCreatedDate(new Date());
                        dto.setCreatedUserId(user.getVpsUserInfo().getSysUserId());
                        dto.setCreatedGroupId(user.getVpsUserInfo().getSysGroupId());
                        ConstructionTaskBO bo = dto.toModel();
                        bo.setBaselineStartDate(dto.getStartDate());
                        bo.setBaselineEndDate(dto.getEndDate());
                        bo.setPerformerWorkItemId(dto.getPerformerId());
                        bo.setType("3");
                        Long id = constructionTaskDAO.saveObject(bo);
                        bo.setPath(parent.getPath() + id + "/");
                        bo.setType("3");
                        bo.setTaskOrder("2");
                        constructionTaskDAO.updateObject(bo);
                    } else {
                        dto.setMonth(obj.getMonth());
                        dto.setSysGroupId(obj.getSysGroupId());
                        dto.setYear(obj.getYear());
                        dto.setType("3");
                        dto.setUpdatedDate(new Date());
                        dto.setUpdatedUserId(user.getVpsUserInfo().getSysUserId());
                        dto.setUpdatedGroupId(user.getVpsUserInfo().getSysGroupId());
                        ConstructionTaskBO bo = dto.toModel();
                        bo.setType("3");
                        bo.setTaskOrder("2");
                        constructionTaskDAO.update(bo);
                    }
                }
            }
        }
    }

    public void updateListDT(DetailMonthPlanSimpleDTO obj, KttsUserSession user) {
        Long detailMonthPlanId = obj.getDetailMonthPlanId();
        if ("1".equalsIgnoreCase(obj.getIsDTImport())) {
            constructionTaskDAO.deleteConstructionTaskByType("5", detailMonthPlanId);
            for (ConstructionTaskDetailDTO dto : obj.getListDT()) {
                dto.setMonth(obj.getMonth());
                dto.setSysGroupId(obj.getSysGroupId());
                dto.setYear(obj.getYear());
                dto.setType("5");
                dto.setDetailMonthPlanId(detailMonthPlanId);
                dto.setCreatedDate(new Date());
                dto.setCreatedUserId(user.getVpsUserInfo().getSysUserId());
                dto.setCreatedGroupId(user.getVpsUserInfo().getSysGroupId());
                constructionTaskDAO.saveObject(dto.toModel());
            }
        } else {
//			chinhpxn_20180710_start
            if (obj.getConstructionIdLst() != null && obj.getConstructionIdLst().size() > 0) {
                constructionTaskDAO.removeConstrTaskDT(obj.getConstructionIdLst(), obj.getDetailMonthPlanId());
            }
        }
    }

    public void updateListCVK(DetailMonthPlanSimpleDTO obj, KttsUserSession user) {
        Long detailMonthPlanId = obj.getDetailMonthPlanId();
        if ("1".equalsIgnoreCase(obj.getIsCVKImport())) {
            constructionTaskDAO.deleteConstructionTaskByType("6", detailMonthPlanId);
            if (obj.getListCVK() != null && !obj.getListCVK().isEmpty()) {
                ConstructionTaskDetailDTO parent = new ConstructionTaskDetailDTO();
                getParentTaskForCVK(parent, "1", detailMonthPlanId, obj.getSysGroupId());
                for (ConstructionTaskDetailDTO dto : obj.getListCVK()) {
                    dto.setMonth(obj.getMonth());
                    dto.setSysGroupId(obj.getSysGroupId());
                    dto.setYear(obj.getYear());
                    dto.setType("6");
                    dto.setLevelId(4L);
                    dto.setDetailMonthPlanId(detailMonthPlanId);
                    dto.setParentId(parent.getConstructionTaskId());
                    dto.setCreatedDate(new Date());
                    dto.setCreatedUserId(user.getVpsUserInfo().getSysUserId());
                    dto.setCreatedGroupId(user.getVpsUserInfo().getSysGroupId());
                    dto.setBaselineStartDate(dto.getStartDate());
                    dto.setBaselineEndDate(dto.getEndDate());
                    dto.setStatus("1");
                    dto.setCompleteState("1");
                    ConstructionTaskBO bo = dto.toModel();
                    Long id = constructionTaskDAO.saveObject(bo);
                    bo.setPath(parent.getPath() + id + "/");
                    constructionTaskDAO.updateObject(bo);
                    // chinhpxn20180718_start
                    constructionTaskBusinessImpl.createSendSmsEmail(dto, user);
                    // chinhpxn20180718_end
                }
            }
        } else {
            if (obj.getListConstrTaskIdDelete() != null && obj.getListConstrTaskIdDelete().size() > 0) {
                for (String id : obj.getListConstrTaskIdDelete()) {
                    constructionTaskDAO.deleteParentAndChild(id);
                    constructionTaskDAO.getSession().flush();
                }
            }
        }
    }

    public void remove(DetailMonthPlanSimpleDTO obj) {
        detailMonthPlanOSDAO.remove(obj.getDetailMonthPlanId());
        detailMonthPlanOSDAO.removeConstructionTaskByDMPId(obj.getDetailMonthPlanId());
    }

    public Long getSequence() {
        // TODO Auto-generated method stub
        return detailMonthPlanOSDAO.getSequence();
    }

    public String exportTemplateListTC(Long sysGroupId, HttpServletRequest request) throws Exception {
        String folderParam = defaultSubFolderUpload;
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();
        InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Import_thicong_thang_chitiet_ngoaiOS.xlsx"));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.CREATE,
                Constant.AdResourceKey.MONTHLY_DETAIL_PLAN, request);
        List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
        file.close();
        Calendar cal = Calendar.getInstance();
        String uploadPath = folder2Upload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
                + File.separator + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1)
                + File.separator + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload) + File.separator
                + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator
                + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        File udir = new File(uploadPath);
        if (!udir.exists()) {
            udir.mkdirs();
        }
        OutputStream outFile = new FileOutputStream(
                udir.getAbsolutePath() + File.separator + "Import_thicong_thang_chitiet_ngoaiOS.xlsx");

//		chinhpxn20180705_start
//		List<WorkItemDetailDTO> listWorkItems = workItemDAO.getWorkForTC(sysGroupId);
//		chinhpxn20180705_end
        List<SysUserCOMSDTO> listUser = sysUserCOMSDAO.usersFillter(groupIdList);
        List<WorkItemDTO> listSheet3 = workItemDAO.GetListData();
        XSSFSheet sheet = workbook.getSheetAt(0);
//		chinhpxn20180705_start
//		fillSheet1(sheet, listWorkItems);
//		chinhpxn20180705_end
        sheet = workbook.getSheetAt(1);
        fillSheet2(sheet, listUser);
        sheet = workbook.getSheetAt(2);
        fillSheet3(sheet, listSheet3);
        workbook.write(outFile);
        workbook.close();
        outFile.close();

        String path = UEncrypt
                .encryptFileUploadPath(uploadPathReturn + File.separator + "Import_thicong_thang_chitiet_ngoaiOS.xlsx");
        return path;
    }

    private void fillSheet3(XSSFSheet sheet, List<WorkItemDTO> listSheet3) {
        if (listSheet3 != null && !listSheet3.isEmpty()) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet);
            int i = 1;
            for (WorkItemDTO dto : listSheet3) {
                Row row = sheet.createRow(i++);
                Cell cell = row.createCell(0, CellType.STRING);
                cell.setCellValue("" + (i - 1));
                cell.setCellStyle(style);
                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue(StringUtils.isNotEmpty(dto.getName()) ? dto.getName() : "");
                cell.setCellStyle(style);
                cell = row.createCell(2, CellType.STRING);
                cell.setCellValue(StringUtils.isNotEmpty(dto.getCode()) ? dto.getCode() : "");
                cell.setCellStyle(style);
            }
        }
    }

    private void fillSheet1(XSSFSheet sheet, List<WorkItemDetailDTO> listWorkItems) {
        if (listWorkItems != null && !listWorkItems.isEmpty()) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet);
            XSSFCellStyle styleCurrency = ExcelUtils.styleCurrency(sheet);
            int i = 3;
            for (WorkItemDetailDTO dto : listWorkItems) {
                Row row = sheet.createRow(i++);
                Cell cell = row.createCell(0, CellType.STRING);
                cell.setCellValue("" + (i - 3));
                cell.setCellStyle(style);
                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue(StringUtils.isNotEmpty(dto.getProvinceCode()) ? dto.getProvinceCode() : "");
                cell.setCellStyle(style);
                cell = row.createCell(2, CellType.STRING);
                cell.setCellValue(StringUtils.isNotEmpty(dto.getCatStationCode()) ? dto.getCatStationCode() : "");
                cell.setCellStyle(style);
                cell = row.createCell(3, CellType.STRING);
                cell.setCellValue(StringUtils.isNotEmpty(dto.getConstructionCode()) ? dto.getConstructionCode() : "");
                cell.setCellStyle(style);
                cell = row.createCell(4, CellType.STRING);
                cell.setCellValue(StringUtils.isNotEmpty(dto.getName()) ? dto.getName() : "");
                cell.setCellStyle(style);
                cell = row.createCell(5, CellType.STRING);
                cell.setCellValue(StringUtils.isNotEmpty(dto.getCntContract()) ? dto.getCntContract() : "");
                cell.setCellStyle(style);
                cell = row.createCell(6, CellType.NUMERIC);
                cell.setCellValue(dto.getQuantity() != null ? dto.getQuantity() : 0);
                cell.setCellStyle(styleCurrency);
                for (int j = 7; j < 15; j++) {
                    cell = row.createCell(j, CellType.STRING);
                    cell.setCellValue("");
                    cell.setCellStyle(style);
                }

            }
        }
    }

    private void fillSheet2(XSSFSheet sheet, List<SysUserCOMSDTO> listUser) {
        if (listUser != null && !listUser.isEmpty()) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet);
            int i = 1;
            for (SysUserCOMSDTO dto : listUser) {
                Row row = sheet.createRow(i++);
                Cell cell = row.createCell(0, CellType.STRING);
                cell.setCellValue("" + (i - 1));
                cell.setCellStyle(style);
                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue(StringUtils.isNotEmpty(dto.getEmployeeCode()) ? dto.getEmployeeCode() : "");
                cell.setCellStyle(style);
                cell = row.createCell(2, CellType.STRING);
                cell.setCellValue(StringUtils.isNotEmpty(dto.getLoginName()) ? dto.getLoginName() : "");
                cell.setCellStyle(style);
                cell = row.createCell(3, CellType.STRING);
                cell.setCellValue(StringUtils.isNotEmpty(dto.getFullName()) ? dto.getFullName() : "");
                cell.setCellStyle(style);
                cell = row.createCell(4, CellType.STRING);
                cell.setCellValue(StringUtils.isNotEmpty(dto.getEmail()) ? dto.getEmail() : "");
                cell.setCellStyle(style);
                cell = row.createCell(5, CellType.STRING);
                cell.setCellValue(StringUtils.isNotEmpty(dto.getSysGroupName()) ? dto.getSysGroupName() : "");
                cell.setCellStyle(style);

            }
        }
    }
    
    private void checkValidateConstructionType(ConstructionTaskDetailDTO newObj, Long loaict, StringBuilder errorMesg, Boolean isExistError, HashMap<String, Long> mapLoaiCt, List<String> listErorrString) {
    	if(newObj.getSourceWork().equals("1")) {
			if(!String.valueOf(loaict).equals("1")) {
				isExistError = true;
            	errorMesg.append("\nLoại công trình không ứng với nguồn việc Xây lắp (Chỉ nhập trong các loại: 1) !");
                listErorrString.add("Có lỗi validate!");
			} else {
				newObj.setConstructionTypeNew(String.valueOf(loaict));
        		mapLoaiCt.put(newObj.getConstructionCode(), loaict);
			}
		} else if(newObj.getSourceWork().equals("2")) {
			if(!String.valueOf(loaict).equals("2") && !String.valueOf(loaict).equals("3") && !String.valueOf(loaict).equals("4") 
					&& !String.valueOf(loaict).equals("5") && !String.valueOf(loaict).equals("9")) {
				isExistError = true;
            	errorMesg.append("\nLoại công trình không ứng với nguồn việc Chi phí (Chỉ nhập trong các loại: 2, 3, 4, 5, 9) !");
                listErorrString.add("Có lỗi validate!");
			} else {
				newObj.setConstructionTypeNew(String.valueOf(loaict));
        		mapLoaiCt.put(newObj.getConstructionCode(), loaict);
			}
		} else if(newObj.getSourceWork().equals("3")) {
			if(!String.valueOf(loaict).equals("6")) {
				isExistError = true;
            	errorMesg.append("\nLoại công trình không ứng với nguồn việc Ngoài tập đoàn (Chỉ nhập trong các loại: 6) !");
                listErorrString.add("Có lỗi validate!");
			} else {
				newObj.setConstructionTypeNew(String.valueOf(loaict));
        		mapLoaiCt.put(newObj.getConstructionCode(), loaict);
			}
		} else if(newObj.getSourceWork().equals("4")) {
			if(!String.valueOf(loaict).equals("7")) {
				isExistError = true;
            	errorMesg.append("\nLoại công trình không ứng với nguồn việc HTCT Xây dựng móng (Chỉ nhập trong các loại: 7) !");
                listErorrString.add("Có lỗi validate!");
			} else {
				newObj.setConstructionTypeNew(String.valueOf(loaict));
        		mapLoaiCt.put(newObj.getConstructionCode(), loaict);
			}
		} else if(newObj.getSourceWork().equals("5")) {
			if(!String.valueOf(loaict).equals("7")) {
				isExistError = true;
            	errorMesg.append("\nLoại công trình không ứng với nguồn việc HTCT Hoàn thiện (Chỉ nhập trong các loại: 7) !");
                listErorrString.add("Có lỗi validate!");
			} else {
				newObj.setConstructionTypeNew(String.valueOf(loaict));
        		mapLoaiCt.put(newObj.getConstructionCode(), loaict);
			}
		} else if(newObj.getSourceWork().equals("6")) {
			if(!String.valueOf(loaict).equals("8")) {
				isExistError = true;
            	errorMesg.append("\nLoại công trình không ứng với nguồn việc Công trình XDDD (Chỉ nhập trong các loại: 8) !");
                listErorrString.add("Có lỗi validate!");
			} else {
				newObj.setConstructionTypeNew(String.valueOf(loaict));
        		mapLoaiCt.put(newObj.getConstructionCode(), loaict);
			}
		} else if(newObj.getSourceWork().equals("7")) {
			if(!String.valueOf(loaict).equals("6")) {
				isExistError = true;
            	errorMesg.append("\nLoại công trình không ứng với nguồn việc Xây lắp - Trung tâm xây dựng (Chỉ nhập trong các loại: 6) !");
                listErorrString.add("Có lỗi validate!");
			} else {
				newObj.setConstructionTypeNew(String.valueOf(loaict));
        		mapLoaiCt.put(newObj.getConstructionCode(), loaict);
			}
		}
    }
    
    public List<ConstructionTaskDetailDTO> importTCHTCT(String fileInput, String filePath, HttpServletRequest request) throws Exception {
    	KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession"); 
        List<ConstructionTaskDetailDTO> workLst = new ArrayList<ConstructionTaskDetailDTO>();
        File f = new File(fileInput);
        ZipSecureFile.setMinInflateRatio(-1.0d);//hoangnh add
        XSSFWorkbook workbook = new XSSFWorkbook(f);
        XSSFSheet sheet = workbook.getSheetAt(0);
        DataFormatter formatter = new DataFormatter();
        DecimalFormat df = new DecimalFormat("#.###");
        DecimalFormat longf = new DecimalFormat("#");
        boolean isExistError = false;
        List<String> listErorrString = new ArrayList<>();
        
        //tatph - start - 15112019
        int counts = 0;
        List<String> listConstrExcel = new ArrayList<>();
        List<String> listWorkItemExcel = new ArrayList<>();
        List<String> listUserLoginExcel = new ArrayList<>();
        List<String> listProject  = new ArrayList<>();
        for (Row rows : sheet) {
        	counts++;
            if (counts > 3 && !ValidateUtils.checkIfRowIsEmpty(rows)) {
            	listConstrExcel.add(formatter.formatCellValue(rows.getCell(3)).trim().toUpperCase());
            	listWorkItemExcel.add(formatter.formatCellValue(rows.getCell(5)).trim().toUpperCase());
            	listUserLoginExcel.add(formatter.formatCellValue(rows.getCell(14)).trim().toUpperCase());
            	listProject.add(formatter.formatCellValue(rows.getCell(6)).trim().toUpperCase());
            }
        }
        
        List<CNTContractDTO> cntDto = detailMonthPlanOSDAO.getKeyValueTask(listConstrExcel);
        HashMap<String,Long> cntMap = new HashMap<String,Long>();
        for(CNTContractDTO dto: cntDto){
        	cntMap.put(dto.getCode().toUpperCase(),dto.getConstructionId());
        }
        
        Map<String, ConstructionDetailDTO> constructionMap = constructionDAO.getConstructionByCodeExcel(listConstrExcel);
        Map<String, WorkItemDetailDTO> workItemMap = workItemDAO.getWorkItemByCodeNewExcel(listWorkItemExcel);
        //Huypq-20200221-start
        List<SysUserCOMSDTO> lstUser = detailMonthPlanOSDAO.getListSysUserEmailExcel(listUserLoginExcel);
        Map<String, SysUserCOMSDTO> userLoginMap = new HashMap<>();
        Map<String, SysUserCOMSDTO> userEmailMap = new HashMap<>();
        for(SysUserCOMSDTO userComs : lstUser) {
        	userLoginMap.put(userComs.getLoginName().toUpperCase().trim(), userComs);
        	userEmailMap.put(userComs.getEmail().toUpperCase().trim(), userComs);
        }

      //Huy-end
        XSSFCellStyle style = ExcelUtils.styleText(sheet);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        //tatph - end - 15112019
        
        //Huypq-20200201-start
        HashMap<String, Long> mapNguon = new HashMap<>();
        HashMap<String, Long> mapLoaiCt = new HashMap<>();
        //Huy-end
        
        //Huypq-10042020-start
        HashMap<String, String> mapCheckDup = new HashMap<>();
        //Huy-end
        
        HashMap<Long, String> mapSourceWork = new HashMap<>();
        HashMap<Long, String> mapConsType = new HashMap<>();
        List<ConstructionTaskDetailDTO> lstConsType = constructionTaskDAO.checkSourceWorkByConsId(listConstrExcel);
        for(ConstructionTaskDetailDTO dto : lstConsType) {
        	mapSourceWork.put(dto.getConstructionId(), dto.getSourceWork());
        	mapConsType.put(dto.getConstructionId(), dto.getConstructionType());
        }
        
        int count = 0;
        for (Row row : sheet) {
            count++;
            if (count > 3 && !ValidateUtils.checkIfRowIsEmpty(row)) {
                boolean checkColumn3 = true;
                boolean checkColumn4 = true;
                boolean checkColumn5 = true;
                boolean checkColumn6 = true;
                boolean checkColumn7 = true;
                boolean checkColumn8 = true;
                boolean checkColumn9 = true;
                boolean checkColumn14 = true;
                boolean checkColumn15 = true;
                boolean checkColumn16 = true;
                boolean checkColumn17 = true;
                boolean checkLength = true;
                StringBuilder errorMesg = new StringBuilder();
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String constructionCode = "";
                String wiName = "";
                String workItemType ="";
//                ConstructionTaskDetailDTO taskType = new ConstructionTaskDetailDTO();
                ConstructionTaskDetailDTO newObj = new ConstructionTaskDetailDTO();
                for (int i = 0; i < 18; i++) {
                    Cell cell = row.getCell(i);
                    if (cell != null) {
                        if (cell.getColumnIndex() == 3) {
                            try {
                                constructionCode = formatter.formatCellValue(row.getCell(3)).trim();
//                                hoanm1_20191114_start
//                                ConstructionDetailDTO obj = constructionDAO.getConstructionByCode(constructionCode.trim());
                                ConstructionDetailDTO obj = constructionMap.get(constructionCode.trim().toUpperCase());
//                                hoanm1_20191114_start
                                if (obj.getConstructionId() == null) {
                                    checkColumn3 = false;
                                    isExistError = true;
                                    errorMesg.append("Mã công trình không tồn tại !");
                                    listErorrString.add("Có lỗi validate!");
                                }
                                newObj.setConstructionCode(obj.getCode());
                                newObj.setConstructionId(obj.getConstructionId());
                                newObj.setCatStationCode(obj.getCatStationCode());
                                newObj.setCatProvinceCode(obj.getCatProvince());
                                newObj.setConstructionName(obj.getName());
                                newObj.setProvinceName(obj.getProvinceName());
                                newObj.setCatConstructionTypeId(obj.getCatContructionTypeId());
                                newObj.setCheckHTCT(obj.getCheckHTCT() != null ?obj.getCheckHTCT() : 0L );
                            } catch (Exception e) {
                                checkColumn3 = false;
                                isExistError = true;
                                errorMesg.append("Mã công trình không hợp lệ!");
                                listErorrString.add("Có lỗi validate!");
                            }
//                            if (!checkColumn3) {
//                                isExistError = true;
//                                errorMesg.append("Mã công trình không hợp lệ!");
//                            }

                        }
                        
                        else if (cell.getColumnIndex() == 5) {
                        
                        		  try {
                                      String name = formatter.formatCellValue(row.getCell(5)).trim();
                                      wiName = formatter.formatCellValue(row.getCell(5)).trim();
//                                      hoanm1_20191114_start
                                      	WorkItemDetailDTO obj =workItemMap.get(constructionCode.trim().toUpperCase()+'_'+name.trim().toUpperCase());
                                      	if(obj!=null) {
//                                      	hoanm1_20191114_start
                                      	if (obj.getConstructionCode().trim().toUpperCase().equals(constructionCode.trim().toUpperCase())) {
//                                          	hoanm1_20190704_start
                                          	if(obj.getStatus().equals("3")){
                                          		checkColumn5 = false;
                                                  isExistError = true;
                                                  errorMesg.append("\nHạng mục đã được hoàn thành trong các tháng trước!");
                                                  listErorrString.add("Có lỗi validate!");
                                          	}else{
                                          		 newObj.setWorkItemId(obj.getWorkItemId());
                                                   newObj.setWorkItemName(obj.getName());
                                                   newObj.setWorkItemCode(obj.getCode());
                                          	}
//                                          	hoanm1_20190704_end
                                          } else {
                                              checkColumn5 = false;
                                              isExistError = true;
                                              errorMesg.append("\nHạng mục không thuộc công trình!");
                                              listErorrString.add("Có lỗi validate!");
                                          }
                                      	} else {
                                      		checkColumn5 = false;
                                            isExistError = true;
                                            errorMesg.append("\nHạng mục không thuộc công trình!");
                                            listErorrString.add("Có lỗi validate!");
                                      	}
//                                      }
//                                      }  
                                  } catch (Exception e) {
                                      checkColumn5 = false;
                                      isExistError = true;
                                      errorMesg.append("\nChưa nhập hạng mục hoặc tên hạng mục không tồn tại!");
                                      listErorrString.add("Có lỗi validate!");
                                  }
//                                  if (!checkColumn5) {
//                                      isExistError = true;
//                                      errorMesg.append("\nChưa nhập hạng mục hoặc tên hạng mục không tồn tại!");
//                                  }
                        	
                          

                        } else if(cell.getColumnIndex() == 6){
//                        	if(newObj.getCheckHTCT()==null) {
//                        		newObj.setCheckHTCT(0l);
//                        	}
                        		String name = formatter.formatCellValue(row.getCell(6)).trim();
                        		
                            	if(StringUtils.isNotBlank(name)){
                            		// Huypq-10042020-start check duplicate trong file
                            		if(StringUtils.isNoneBlank(constructionCode) && StringUtils.isNoneBlank(wiName)) {
                            			if (mapCheckDup.size() == 0) {
        									mapCheckDup.put(name + "+" + constructionCode + "+" + wiName, name);
        								} else {
        									String valueMap = mapCheckDup.get(name + "+" + constructionCode + "+" + wiName);
        									if (StringUtils.isNotBlank(valueMap)) {
        										checkColumn6 = false;
        										isExistError = true;
        										errorMesg.append("\nCặp Hợp đồng - Công trình - Hạng mục trong file import không được trùng nhau!");
        		                                listErorrString.add("Có lỗi validate!");
        									} else {
        										mapCheckDup.put(name + "+" + constructionCode + "+" + wiName, name);
        									}
        								}
                            		}
    								// Huy-end
                            		Long check  = null;
                            		String map = constructionCode.trim().toUpperCase() + "+" + name.trim().toUpperCase();
//                            		if(newObj.getCheckHTCT() != 1) {
//                            			check = cntMap.get(map.toUpperCase().trim());  
//                            		}else {
//                            			check = litMapPE.get(map.toUpperCase().trim()); 
//                            		}
                            			check = cntMap.get(map); 
                                		if(check != null){
                                			checkColumn6 = true;
                                			newObj.setCntContract(name);
                                		} else {
                                			checkColumn6 = false;
                                			checkColumn6 = true;
                                    		errorMesg.append("\nCông trình chưa được gán vào hợp đồng đầu ra!");
                                            listErorrString.add("Có lỗi validate!");
                                		}
                            		
                            	} else {
                            		isExistError = true;
                            		checkColumn6 = true;
                            		errorMesg.append("\nHợp đồng đầu ra không được để trống!");
                                    listErorrString.add("Có lỗi validate!");
                            	}
//                            	if(!checkColumn6){
//                            		isExistError = true;
//                            		if(newObj.getCheckHTCT() != 1) {
//                            		 errorMesg.append("\nCông trình chưa được gán vào hợp đồng đầu ra!");
//                            		}else {
//                            			 errorMesg.append("\nCông trình và dự án không thuộc hạ tầng cho thuê!");
//                            		}
//                            		errorMesg.append("\nCông trình chưa được gán vào hợp đồng đầu ra!");
//                            	}
                        } else if (cell.getColumnIndex() == 7) {
                            try {
                                Double quantity = new Double(Double.parseDouble(formatter.formatCellValue(row.getCell(7)).trim()) * 1000000D);
                                if(quantity > 0){
                                	newObj.setQuantity(quantity);
                                } else {
                                	checkColumn7 = false;
                            		isExistError = true;
                                    errorMesg.append("\nGiá trị không hợp lệ!");
                                    listErorrString.add("Có lỗi validate!");
                                }
                                
                            } catch (Exception e) {
                                checkColumn7 = false;
                                isExistError = true;
                                errorMesg.append("\nGiá trị không hợp lệ!");
                                listErorrString.add("Có lỗi validate!");
                            }
//                            if (!checkColumn7) {
//                                isExistError = true;
//                                errorMesg.append("\nGiá trị không hợp lệ!");
//                            }
                            
                        } 
                        
                        else if (cell.getColumnIndex() == 8) {
                            if (row.getCell(8) != null) {
                            	try {
                                    long nguon = new Long(Long.parseLong(formatter.formatCellValue(row.getCell(8)).trim()));
                                    if(nguon>0 && nguon<8) {
                                    	//Huypq-20200201-start
                                    	if(newObj.getConstructionId()!=null) {
                                    		String sourceWorkDb = mapSourceWork.get(newObj.getConstructionId());
                                        	if(sourceWorkDb!=null) {
                                        		if(newObj.getCheckHTCT()!=null && newObj.getCheckHTCT()==1l) {
                                        			if(nguon != 4l && nguon != 5l) {
                                        				isExistError = true;
                                            			checkColumn8 = false;
                                                    	errorMesg.append("\nNguồn việc của công trình HTCT chỉ được nhập Xây dựng móng và Hoàn thiện!");
                                                        listErorrString.add("Có lỗi validate!");
                                        			} else {
                                        				newObj.setSourceWork(String.valueOf(nguon));
                                        			}
                                        		} else {
                                        			if(nguon == 4l || nguon == 5l) {
                                        				isExistError = true;
                                            			checkColumn8 = false;
                                                    	errorMesg.append("\nNguồn việc Xây dựng móng và Hoàn thiện chỉ ứng với công trình HTCT!");
                                                        listErorrString.add("Có lỗi validate!");
                                        			} else {
                                        				if(sourceWorkDb.equals(formatter.formatCellValue(row.getCell(8)).trim())) {
                                                			if(mapNguon.size()==0) {
                                                        		newObj.setSourceWork(String.valueOf(nguon));
                                                        		mapNguon.put(newObj.getConstructionCode(), nguon);
                                                        	} else {
                                                        		if(mapNguon.get(newObj.getConstructionCode())!=null && mapNguon.get(newObj.getConstructionCode()) != nguon) {
                                                        			isExistError = true;
                                                        			checkColumn8 = false;
                                                                	errorMesg.append("\nNguồn việc của những hạng mục thuộc cùng công trình phải giống nhau!");
                                                                    listErorrString.add("Có lỗi validate!");
                                                        		} else {
                                                        			newObj.setSourceWork(String.valueOf(nguon));
                                                            		mapNguon.put(newObj.getConstructionCode(), nguon);
                                                        		}
                                                        	}
                                                		} else {
                                                			 isExistError = true;
                                                			checkColumn8 = false;
                                                        	errorMesg.append(" Nguồn việc của công trình: "+ newObj.getConstructionCode() + " đã tồn tại bằng: "+ sourceWorkDb);
                                                            listErorrString.add("Có lỗi validate!");
                                                		}
                                        			}
                                        		}
                                        	} else {
                                        		if(newObj.getCheckHTCT()!=null && newObj.getCheckHTCT()==1l) {
                                        			if(nguon != 4l && nguon != 5l) {
                                        				isExistError = true;
                                            			checkColumn8 = false;
                                                    	errorMesg.append("\nNguồn việc của công trình HTCT chỉ được nhập Xây dựng móng và Hoàn thiện!");
                                                        listErorrString.add("Có lỗi validate!");
                                        			} else {
                                        				newObj.setSourceWork(String.valueOf(nguon));
                                        			}
                                        		} else {
                                        			if(nguon == 4l || nguon == 5l) {
                                        				isExistError = true;
                                            			checkColumn8 = false;
                                                    	errorMesg.append("\nNguồn việc Xây dựng móng và Hoàn thiện chỉ ứng với công trình HTCT!");
                                                        listErorrString.add("Có lỗi validate!");
                                        			} else {
                                        				if(mapNguon.size()==0) {
                                                    		newObj.setSourceWork(String.valueOf(nguon));
                                                    		mapNguon.put(newObj.getConstructionCode(), nguon);
                                                    	} else {
                                                    		if(mapNguon.get(newObj.getConstructionCode())!=null && mapNguon.get(newObj.getConstructionCode()) != nguon) {
                                                    			 isExistError = true;
                                                    			checkColumn8 = false;
                                                            	errorMesg.append("\nNguồn việc của những hạng mục thuộc cùng công trình phải giống nhau!");
                                                                listErorrString.add("Có lỗi validate!");
                                                    		} else {
                                                    			newObj.setSourceWork(String.valueOf(nguon));
                                                        		mapNguon.put(newObj.getConstructionCode(), nguon);
                                                    		}
                                                    	}
                                        			}
                                        		}
                                        	}
                                    	} else {
                                    		newObj.setSourceWork(String.valueOf(nguon));
                                    		mapNguon.put(newObj.getConstructionCode(), nguon);
                                    	}
                                    	
                                    	//Huypq-end
                                    } else {
                                    	 isExistError = true;
                                    	checkColumn8 = false;
                                    	errorMesg.append("\nNguồn việc không hợp lệ !");
                                        listErorrString.add("Có lỗi validate!");
                                    }
                                } catch (Exception e) {
                                	 isExistError = true;
                                    checkColumn8 = false;
                                    errorMesg.append("\nNguồn việc phải nhập dạng số!");
                                    listErorrString.add("Có lỗi validate!");
                                }
                            } else {
                            	 isExistError = true;
                            	checkColumn8 = false;
                            	errorMesg.append("\nNguồn việc không được để trống !");
                                listErorrString.add("Có lỗi validate!");
                            }
                        } else if (cell.getColumnIndex() == 9) {
                            if (row.getCell(9) != null) {
                            	try {
                            		long loaict = new Long(Long.parseLong(formatter.formatCellValue(row.getCell(9)).trim()));
                                    if(loaict>0 && loaict<10) {
                                    	//Huypq-20200201-start
                                    	if(newObj.getConstructionId()!=null) {
                                        	String consTypeDb = mapConsType.get(newObj.getConstructionId());
                                    		if(consTypeDb!=null) {
                                        		if(consTypeDb.equals(formatter.formatCellValue(row.getCell(9)).trim())) {
                                        			if(mapLoaiCt.size()==0) {
                                        				checkValidateConstructionType(newObj, loaict, errorMesg, isExistError, mapLoaiCt, listErorrString);
                                                	} else {
                                                		if(mapLoaiCt.get(newObj.getConstructionCode())!=null && mapLoaiCt.get(newObj.getConstructionCode()) != loaict) {
                                                			isExistError = true;
                                                			checkColumn9 = false;
                                                        	errorMesg.append("\nLoại công trình của những hạng mục thuộc cùng công trình phải giống nhau!");
                                                            listErorrString.add("Có lỗi validate!");
                                                		} else {
                                                			checkValidateConstructionType(newObj, loaict, errorMesg, isExistError, mapLoaiCt, listErorrString);
                                                		}
                                                	}
                                        		} else {
                                        			 isExistError = true;
                                        			checkColumn9 = false;
                                                	errorMesg.append(" Loại công trình của công trình: "+ newObj.getConstructionCode() +" đã tồn tại bằng: "+ consTypeDb);
                                                    listErorrString.add("Có lỗi validate!");
                                        		}
                                        	} else {
                                        		if(mapLoaiCt.size()==0) {
                                        			checkValidateConstructionType(newObj, loaict, errorMesg, isExistError, mapLoaiCt, listErorrString);
                                            	} else {
                                            		if(mapLoaiCt.get(newObj.getConstructionCode())!=null && mapLoaiCt.get(newObj.getConstructionCode()) != loaict) {
                                            			 isExistError = true;
                                            			checkColumn9 = false;
                                                    	errorMesg.append("\nLoại công trình của những hạng mục thuộc cùng công trình phải giống nhau!");
                                                        listErorrString.add("Có lỗi validate!");
                                            		} else {
                                            			checkValidateConstructionType(newObj, loaict, errorMesg, isExistError, mapLoaiCt, listErorrString);
                                            		}
                                            	}
                                        	}
                                    	} else {
                                    		checkValidateConstructionType(newObj, loaict, errorMesg, isExistError, mapLoaiCt, listErorrString);
                                    	}
                                    	
                                    	//Huypq-end
                                    } else {
                                    	 isExistError = true;
                                    	checkColumn9 = false;
                                    	errorMesg.append("\nLoại công trình không hợp lệ !");
                                        listErorrString.add("Có lỗi validate!");
                                    }
                                } catch (Exception e) {
                                	 isExistError = true;
                                    checkColumn9 = false;
                                    errorMesg.append("\nLoại công trình phải nhập dạng số!");
                                    listErorrString.add("Có lỗi validate!");
                                }
                            } else {
                            	 isExistError = true;
                            	checkColumn9 = false;
                            	errorMesg.append("\nLoại công trình không được để trống !");
                                listErorrString.add("Có lỗi validate!");
                            }
                        } 
                        else if (cell.getColumnIndex() == 10) {
                            if (row.getCell(10) != null) {
                                newObj.setSourceType("1");
                            } else {
                                newObj.setSourceType("2");
                            }

                        } else if (cell.getColumnIndex() == 12) {
                            if (row.getCell(12) != null) {
                                newObj.setDeployType("1");
                            } else {
                                newObj.setDeployType("2");
                            }

                        }
                        else if (cell.getColumnIndex() == 14) {
                            try {
                                String nameUser = formatter.formatCellValue(row.getCell(14)).trim();
//                                hoanm1_20191114_start
                                SysUserCOMSDTO obj = new SysUserCOMSDTO();
//                                obj=detailMonthPlanOSDAO.getSysUser(nameUser);
                                obj=userLoginMap.get(nameUser.toUpperCase().trim());
                                if(obj == null){
                                	obj=userEmailMap.get(nameUser.toUpperCase().trim());	
                                }
                                newObj.setPerformerId(obj.getSysUserId());
                                newObj.setPerformerName(obj.getFullName());
//                                hoanm1_20191114_end
                            } catch (Exception e) {
                                checkColumn14 = false;
                                isExistError = true;
                                errorMesg.append("\nNgười thực hiện không tồn tại!");
                                listErorrString.add("Có lỗi validate!");
                            }
//                            if (!checkColumn14) {
//                                isExistError = true;
//                                errorMesg.append("\nNgười thực hiện không tồn tại!");
//                            }

                        } else if (cell.getColumnIndex() == 15) {
                            try {
                            	if(row.getCell(15)!=null) {
                            		Date startDate = dateFormat.parse(formatter.formatCellValue(row.getCell(15)));
                                    if(validateDate(formatter.formatCellValue(row.getCell(15)))){
                                    	newObj.setStartDate(startDate);
                                    }else{ 
                                    	isExistError = true;
                                    	checkColumn15 = false;
                                    	errorMesg.append("\nNgày bắt đầu không hợp lệ!");
                                        listErorrString.add("Có lỗi validate!");
                                    }        
                            	} else {
                            		isExistError = true;
                            		checkColumn15 = false;
                            		errorMesg.append("\nNgày bắt đầu không được để trống!");
                                    listErorrString.add("Có lỗi validate!");
                            	}
                            } catch (Exception e) {
                            	isExistError = true;
                                checkColumn15 = false;
                                errorMesg.append("\nNgày bắt đầu không hợp lệ!");
                                listErorrString.add("Có lỗi validate!");
                            }
//                            if (!checkColumn15) {
//                                isExistError = true;
//                                errorMesg.append("\nNgày bắt đầu không hợp lệ!");
//                            }
                        } else if (cell.getColumnIndex() == 16) {
                            try {
                            	if(row.getCell(16)!=null) {
                            		Date endDate = dateFormat.parse(formatter.formatCellValue(row.getCell(16)));
                                    if(validateDate(formatter.formatCellValue(row.getCell(16)))){
                                    	newObj.setEndDate(endDate);
                                    }else{ 
                                    	isExistError = true;
                                    	checkColumn16 = false;
                                    	errorMesg.append("\nNgày kết thúc không hợp lệ!");
                                        listErorrString.add("Có lỗi validate!");
                                    }  
                            	} else {
                            		isExistError = true;
                            		checkColumn16 = false;
                            		errorMesg.append("\nNgày kết thúc không được để trống!");
                                    listErorrString.add("Có lỗi validate!");
                            	}
                                
                            } catch (Exception e) {
                                checkColumn16 = false;
                                isExistError = true;
                                errorMesg.append("\nNgày kết thúc không hợp lệ!");
                                listErorrString.add("Có lỗi validate!");
                            }
//                            if (!checkColumn16) {
//                                isExistError = true;
//                                errorMesg.append("\nNgày kết thúc không hợp lệ!");
//                            }

                        } else if (cell.getColumnIndex() == 17) {
                            try {
                                String descriptionTC = formatter.formatCellValue(cell).trim();
                                if (descriptionTC.length() <= 1000) {
                                    newObj.setDescription(descriptionTC);
                                } else {
                                    checkColumn17 = false;
                                    isExistError = true;
                                }

                            } catch (Exception e) {
                                checkColumn17 = false;
                            }
                            if (!checkColumn17) {
                                isExistError = true;
                                errorMesg.append("\nGhi chú vượt quá 1000 ký tự!");
                                listErorrString.add("Có lỗi validate!");
                            }
                            Cell cell1 = row.createCell(18);
                            cell1.setCellValue(errorMesg.toString());
                        }
                        Cell cell1 = row.createCell(18);
                        cell1.setCellValue(errorMesg.toString());
                        cell1.setCellStyle(style);
                    }
                }
                if (listErorrString.size()==0) {
                    newObj.setSourceType(formatter.formatCellValue(row.getCell(10)).equals("1") ? "1" : "2");
                    newObj.setDeployType(formatter.formatCellValue(row.getCell(12)).equals("1") ? "1" : "2");
                    newObj.setWorkItemType("1");
//                    if(newObj.getWorkItemId() != null){
//                    	detailMonthPlanOSDAO.updatePerforment(newObj.getPerformerId(), newObj.getWorkItemId());
//                    }
                    workLst.add(newObj);
                }

            }
        }
        if (listErorrString.size()>0) {
//            XSSFCellStyle style = ExcelUtils.styleText(sheet);
//            style.setAlignment(HorizontalAlignment.CENTER);
//            style.setVerticalAlignment(VerticalAlignment.CENTER);
           
            Cell cell = sheet.getRow(1).createCell(18);
            cell.setCellValue("Cột lỗi");
            cell.setCellStyle(style);
            cell = sheet.getRow(2).createCell(18);
            cell.setCellStyle(style);
//          sheet.addMergedRegion(new CellRangeAddress(1, 2, 15, 15));
            ConstructionTaskDetailDTO objErr = new ConstructionTaskDetailDTO();
            OutputStream out = new FileOutputStream(f, true);
            workbook.write(out);
            out.close();
            objErr.setErrorFilePath(UEncrypt.encryptFileUploadPath(filePath));
            workLst.add(objErr);
        }
        workbook.close();
        return workLst;
    }
//    public List<ConstructionTaskDetailDTO> importTC(String fileInput, String filePath, HttpServletRequest request) throws Exception {
//    	KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession"); 
//        List<ConstructionTaskDetailDTO> workLst = new ArrayList<ConstructionTaskDetailDTO>();
//        File f = new File(fileInput);
//        ZipSecureFile.setMinInflateRatio(-1.0d);//hoangnh add
//        XSSFWorkbook workbook = new XSSFWorkbook(f);
//        XSSFSheet sheet = workbook.getSheetAt(0);
//        DataFormatter formatter = new DataFormatter();
//        DecimalFormat df = new DecimalFormat("#.###");
//        DecimalFormat longf = new DecimalFormat("#");
//        boolean isExistError = false;
//        List<CNTContractDTO> cntDto = detailMonthPlanOSDAO.getKeyValueTask();
//        HashMap<String,Long> cntMap = new HashMap<String,Long>();
//        for(CNTContractDTO dto: cntDto){
//        	cntMap.put(dto.getCode(),dto.getConstructionId());
//        }
//        
//        //tatph - start - 15112019
//        List<ConstructionDTO> listKeyProjectEstimates = detailMonthPlanOSDAO.getKeyProjectEstimates();
//        HashMap<String,Long> litMapPE = new HashMap<String,Long>();
//        for(ConstructionDTO dto: listKeyProjectEstimates){
//        	litMapPE.put(dto.getConstructionCode() +"+"+ dto.getProjectCode(),dto.getProjectEstimatesId());
//        }
//        //tatph - end - 15112019
//        
//        int count = 0;
////        hoanm1_20191114_start
//        Map<String, ConstructionDetailDTO> constructionMap = constructionDAO.getConstructionByCode();
//        Map<String, WorkItemDetailDTO> workItemMap = workItemDAO.getWorkItemByCodeNew();
//        Map<String, SysUserCOMSDTO> userLoginMap = detailMonthPlanOSDAO.getSysUserLoginName();
//        Map<String, SysUserCOMSDTO> userEmailMap = detailMonthPlanOSDAO.getSysUserEmail();
////        hoanm1_20191114_end
//        for (Row row : sheet) {
//            count++;
//            if (count > 3 && !ValidateUtils.checkIfRowIsEmpty(row)) {
//                boolean checkColumn3 = true;
//                boolean checkColumn4 = true;
//                boolean checkColumn5 = true;//checkColumn4
//                boolean checkColumn6 = true;
//                boolean checkColumn7 = true;//checkColumn6
//                boolean checkColumn12 = true;//checkColumn11
//                boolean checkColumn13 = true;//checkColumn12
//                boolean checkColumn14 = true;//checkColumn13
//                boolean checkColumn15 = true;//checkColumn14
//                boolean checkLength = true;
//                StringBuilder errorMesg = new StringBuilder();
//                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//                String constructionCode = "";
//                String workItemType ="";
//                ConstructionTaskDetailDTO newObj = new ConstructionTaskDetailDTO();
//                for (int i = 0; i < 16; i++) {
//                    Cell cell = row.getCell(i);
//                    if (cell != null) {
//                        if (cell.getColumnIndex() == 3) {
//                            try {
//                                constructionCode = formatter.formatCellValue(row.getCell(3)).trim();
////                                hoanm1_20191114_start
////                                ConstructionDetailDTO obj = constructionDAO.getConstructionByCode(constructionCode.trim());
//                                ConstructionDetailDTO obj = constructionMap.get(constructionCode.trim());
////                                hoanm1_20191114_start
//                                if (obj.getConstructionId() == null) {
//                                    checkColumn3 = false;
//                                }
//                                newObj.setConstructionCode(obj.getCode());
//                                newObj.setConstructionId(obj.getConstructionId());
//                                newObj.setCatStationCode(obj.getCatStationCode());
//                                newObj.setCatProvinceCode(obj.getCatProvince());
//                                newObj.setConstructionName(obj.getName());
//                                newObj.setProvinceName(obj.getProvinceName());
//                                newObj.setCatConstructionTypeId(obj.getCatContructionTypeId());
//                                newObj.setCheckHTCT(obj.getCheckHTCT() != null ?obj.getCheckHTCT() : 0L );
//                            } catch (Exception e) {
//                                checkColumn3 = false;
//                            }
//                            if (!checkColumn3) {
//                                isExistError = true;
//                                errorMesg.append("Mã công trình không hợp lệ!");
//                            }
//
//                        } if (cell.getColumnIndex() == 4) {
//                        	workItemType = formatter.formatCellValue(row.getCell(4)).trim();
//                        	if(workItemType != null || workItemType != ""){
//                        		if("1".equals(workItemType) || "2".equals(workItemType)){
//                        			newObj.setWorkItemType(workItemType);
//                        		}else {
//                        			checkColumn4 = false;
//                        		}
//                        	} else {
//                        		checkColumn4 = false;
//                        	}
//                        	if(!checkColumn4){
//                        		isExistError = true;
//                                errorMesg.append("Loại hạng mục không hợp lệ !");
//                        	}
//                        } else if (cell.getColumnIndex() == 5) {
//                            try {
//                            	Long workItemId;
//                                String name = formatter.formatCellValue(row.getCell(5));
//                                if(workItemType.equals("2")){
//                                	newObj.setWorkItemName(name);
//                                }  else 
//                                if(workItemType.equals("1")){
////                                	hoanm1_20191114_start
////                                	WorkItemDetailDTO obj = workItemDAO.getWorkItemByCodeNew(name.trim(), constructionCode.trim());
//                                	WorkItemDetailDTO obj =workItemMap.get(constructionCode.trim()+'_'+name.trim());
////                                	hoanm1_20191114_start
//                                	if (obj.getConstructionCode().trim().equals(constructionCode.trim())) {
////                                    	hoanm1_20190704_start
//                                    	if(obj.getStatus().equals("3")){
//                                    		checkColumn5 = false;
//                                            isExistError = true;
//                                            errorMesg.append("\nHạng mục đã được hoàn thành trong các tháng trước!");
//                                    	}else{
//                                    		 newObj.setWorkItemId(obj.getWorkItemId());
//                                             newObj.setWorkItemName(obj.getName());
//                                             newObj.setWorkItemCode(obj.getCode());
//                                    	}
////                                    	hoanm1_20190704_end
//                                    } else {
//                                        checkColumn5 = false;
//                                        isExistError = true;
//                                        errorMesg.append("\nHạng mục không thuộc công trình!");
//                                    }
//                                }
//                                
//                            } catch (Exception e) {
//                                checkColumn5 = false;
//                            }
//                            if (!checkColumn5) {
//                                isExistError = true;
//                                errorMesg.append("\nChưa nhập hạng mục hoặc tên hạng mục không tồn tại!");
//                            }
//
//                        } else if(cell.getColumnIndex() == 6){
//                        	String name = formatter.formatCellValue(row.getCell(6)).trim();
//                        	if(StringUtils.isNotBlank(name)){
//                        		Long check  = null;
//                        		String map = constructionCode + "+" + name;
//                        		if(newObj.getCheckHTCT() != 1) {
//                        			 check = cntMap.get(map);
//                        		}else {
//                        			check = litMapPE.get(map);
//                        		}
//                        	
//                        		if(check != null){
//                        			checkColumn6 = true;
//                        			newObj.setCntContract(name);
//                        		} else {
//                        			checkColumn6 = false;
//                        		}
//                        	} else {
//                        		checkColumn6 = true;
//                        	}
//                        	if(!checkColumn6){
//                        		isExistError = true;
//                        		if(newObj.getCheckHTCT() != 1) {
//                        			 errorMesg.append("\nCông trình chưa được gán vào hợp đồng đầu ra!");
//                        		}else {
//                        			 errorMesg.append("\nCông trình và dự án không thuộc hạ tầng cho thuê!");
//                        		}
//                               
//                        	}
//                        	
//                        } else if (cell.getColumnIndex() == 8) {
//                            if (row.getCell(8) != null) {
//                                newObj.setSourceType("1");
//                            } else {
//                                newObj.setSourceType("2");
//                            }
//
//                        } else if (cell.getColumnIndex() == 10) {
//                            if (row.getCell(10) != null) {
//                                newObj.setDeployType("1");
//                            } else {
//                                newObj.setDeployType("2");
//                            }
//
//                        } else if (cell.getColumnIndex() == 7) {
//                            try {
//                                Double quantity = new Double(cell.getNumericCellValue() * 1000000);
//                                if(quantity > 0){
//                                	newObj.setQuantity(quantity);
//                                } else {
//                                	checkColumn7 = false;
//                            		isExistError = true;
//                                    errorMesg.append("\nGiá trị không hợp lệ!");
//                                }
//                                
//                            } catch (Exception e) {
//                                checkColumn7 = false;
//                            }
//                            if (!checkColumn7) {
//                                isExistError = true;
//                                errorMesg.append("\nGiá trị không hợp lệ!");
//                            }
//                        } else if (cell.getColumnIndex() == 12) {
//                            try {
//                                String nameUser = formatter.formatCellValue(row.getCell(12)).trim();
////                                hoanm1_20191114_start
//                                SysUserCOMSDTO obj = new SysUserCOMSDTO();
////                                obj=detailMonthPlanOSDAO.getSysUser(nameUser);
//                                obj=userLoginMap.get(nameUser.toUpperCase());
//                                if(obj == null){
//                                	obj=userEmailMap.get(nameUser.toUpperCase());	
//                                }
//                                newObj.setPerformerId(obj.getSysUserId());
//                                newObj.setPerformerName(obj.getFullName());
////                                hoanm1_20191114_end
//                            } catch (Exception e) {
//                                checkColumn12 = false;
//                            }
//                            if (!checkColumn12) {
//                                isExistError = true;
//                                errorMesg.append("\nNgười thực hiện không tồn tại!");
//                            }
//
//                        } else if (cell.getColumnIndex() == 13) {
//                            try {
//
//                                Date startDate = dateFormat.parse(formatter.formatCellValue(row.getCell(13)));
//                                if(validateDate(formatter.formatCellValue(row.getCell(13)))){
//                                	newObj.setStartDate(startDate);
//                                }else{ 
//                                	checkColumn13 = false;
//                                }                                
//                            } catch (Exception e) {
//                                checkColumn13 = false;
//                            }
//                            if (!checkColumn13) {
//                                isExistError = true;
//                                errorMesg.append("\nNgày bắt đầu không hợp lệ!");
//                            }
//                        } else if (cell.getColumnIndex() == 14) {
//                            try {
//                                Date endDate = dateFormat.parse(formatter.formatCellValue(row.getCell(14)));
//                                if(validateDate(formatter.formatCellValue(row.getCell(14)))){
//                                	newObj.setEndDate(endDate);
//                                }else{ 
//                                	checkColumn13 = false;
//                                }  
//                            } catch (Exception e) {
//                                checkColumn14 = false;
//                            }
//                            if (!checkColumn14) {
//                                isExistError = true;
//                                errorMesg.append("\nNgày kết thúc không hợp lệ!");
//                            }
//
//                        } else if (cell.getColumnIndex() == 15) {
//                            try {
//                                String descriptionTC = formatter.formatCellValue(cell).trim();
//                                if (descriptionTC.length() <= 1000) {
//                                    newObj.setDescription(descriptionTC);
//                                } else {
//                                    checkColumn15 = false;
//                                    isExistError = true;
//                                }
//
//                            } catch (Exception e) {
//                                checkColumn15 = false;
//                            }
//                            if (!checkColumn15) {
//                                isExistError = true;
//                                errorMesg.append("\nGhi chú vượt quá 1000 ký tự!");
//                            }
//                            Cell cell1 = row.createCell(16);
//                            cell1.setCellValue(errorMesg.toString());
//                        }
//                        Cell cell1 = row.createCell(16);
//                        cell1.setCellValue(errorMesg.toString());
//                    }
//                }
//                if (!isExistError) {
//                    newObj.setSourceType(formatter.formatCellValue(row.getCell(8)).equals("1") ? "1" : "2");
//                    newObj.setDeployType(formatter.formatCellValue(row.getCell(10)).equals("1") ? "1" : "2");
////                    if(newObj.getWorkItemId() != null){
////                    	detailMonthPlanOSDAO.updatePerforment(newObj.getPerformerId(), newObj.getWorkItemId());
////                    }
//                    workLst.add(newObj);
//                }
//
//            }
//        }
//        if (isExistError) {
//            XSSFCellStyle style = ExcelUtils.styleText(sheet);
//            style.setAlignment(HorizontalAlignment.CENTER);
//            style.setVerticalAlignment(VerticalAlignment.CENTER);
//           
//            Cell cell = sheet.getRow(1).createCell(16);
//            cell.setCellValue("Cột lỗi");
//            cell.setCellStyle(style);
//            cell = sheet.getRow(2).createCell(16);
//            cell.setCellStyle(style);
////          sheet.addMergedRegion(new CellRangeAddress(1, 2, 15, 15));
//            ConstructionTaskDetailDTO objErr = new ConstructionTaskDetailDTO();
//            OutputStream out = new FileOutputStream(f, true);
//            workbook.write(out);
//            out.close();
//            objErr.setErrorFilePath(UEncrypt.encryptFileUploadPath(filePath));
//            workLst.add(objErr);
//        }
//        workbook.close();
//        return workLst;
//    }

    public List<TmpnTargetDetailDTO> getYearPlanDetailTarget(DetailMonthPlanSimpleDTO obj) {
        // TODO Auto-generated method stub
        return detailMonthPlanOSDAO.getYearPlanDetailTarget(obj);

    }

    public List<WorkItemDetailDTO> getWorkItemDetail(DetailMonthPlanSimpleDTO obj) {
        // TODO Auto-generated method stub
        return detailMonthPlanOSDAO.getWorkItemDetail(obj);

    }

    public String exportTemplateListHSHC(Long sysGroupId, HttpServletRequest request) throws Exception {
        String folderParam = defaultSubFolderUpload;
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();
        InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Import_QuyLuongOS_thang_chitiet.xlsx"));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        file.close();
        Calendar cal = Calendar.getInstance();
        String uploadPath = folder2Upload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
                + File.separator + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1)
                + File.separator + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload) + File.separator
                + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator
                + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        File udir = new File(uploadPath);
        if (!udir.exists()) {
            udir.mkdirs();
        }
        OutputStream outFile = new FileOutputStream(
                udir.getAbsolutePath() + File.separator + "Import_QuyLuongOS_thang_chitiet.xlsx");
//		chinhpxn20180705_start
//		List<ConstructionDetailDTO> listConstr = constructionDAO
//		chinhpxn20180705_end
//				.getConstructionForHSHC(sysGroupId);
        XSSFSheet sheet = workbook.getSheetAt(0);
//		chinhpxn20180705_start
//		fillSheetHSHC(sheet, listConstr);
//		chinhpxn20180705_end
        String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.CREATE,
                Constant.AdResourceKey.MONTHLY_DETAIL_PLAN, request);
        List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
        List<SysUserCOMSDTO> listUser = sysUserCOMSDAO.usersFillter(groupIdList);
        sheet = workbook.getSheetAt(1);
        fillSheet2(sheet, listUser);
        workbook.write(outFile);
        workbook.close();
        outFile.close();

        String path = UEncrypt
                .encryptFileUploadPath(uploadPathReturn + File.separator + "Import_QuyLuongOS_thang_chitiet.xlsx");
        return path;
    }

    private void fillSheetHSHC(XSSFSheet sheet, List<ConstructionDetailDTO> listConstr) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if (listConstr != null && !listConstr.isEmpty()) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet);
            XSSFCellStyle styleCurrency = ExcelUtils.styleCurrency(sheet);
            XSSFCellStyle styleDate = ExcelUtils.styleDate(sheet);
            int i = 2;
            for (ConstructionDetailDTO dto : listConstr) {
                Row row = sheet.createRow(i++);
                Cell cell = row.createCell(0, CellType.STRING);
                cell.setCellValue("" + (i - 2));
                cell.setCellStyle(style);
                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue(StringUtils.isNotEmpty(dto.getProvinceCode()) ? dto.getProvinceCode() : "");
                cell.setCellStyle(style);
                cell = row.createCell(2, CellType.STRING);
                cell.setCellValue(StringUtils.isNotEmpty(dto.getCatStationCode()) ? dto.getCatStationCode() : "");
                cell.setCellStyle(style);
                cell = row.createCell(3, CellType.STRING);
                cell.setCellValue(StringUtils.isNotEmpty(dto.getCode()) ? dto.getCode() : "");
                cell.setCellStyle(style);
                cell = row.createCell(4, CellType.STRING);
                cell.setCellValue(
                        StringUtils.isNotEmpty(dto.getCatContructionTypeName()) ? dto.getCatContructionTypeName() : "");
                cell.setCellStyle(style);
                cell = row.createCell(5, CellType.STRING);
                cell.setCellValue((dto.getIsObstructed() != null && dto.getIsObstructed().equals("1")) ? "Có" : "");
                cell.setCellStyle(style);
                cell = row.createCell(6, CellType.STRING);
                cell.setCellValue(StringUtils.isNotEmpty(dto.getCntContract()) ? dto.getCntContract() : "");
                cell.setCellStyle(style);
                cell = row.createCell(7, CellType.NUMERIC);
                cell.setCellValue((dto.getQuantity() != null) ? dto.getQuantity() : 0);
                cell.setCellStyle(styleCurrency);
                cell = row.createCell(8, CellType.STRING);
                cell.setCellValue((dto.getCompleteDate() != null) ? dateFormat.format(dto.getCompleteDate()) : "");
                cell.setCellStyle(styleDate);
                for (int j = 9; j < 13; j++) {
                    cell = row.createCell(j, CellType.STRING);
                    cell.setCellValue("");
                    cell.setCellStyle(style);
                }
            }
        }
    }

    public List<ConstructionTaskDetailDTO> importHSHC(String fileInput, String filePath) throws Exception {
        List<ConstructionTaskDetailDTO> workLst = new ArrayList<ConstructionTaskDetailDTO>();
        File f = new File(fileInput);
        ZipSecureFile.setMinInflateRatio(-1.0d);//hoangnh add
        XSSFWorkbook workbook = new XSSFWorkbook(f);
        XSSFSheet sheet = workbook.getSheetAt(0);
        DataFormatter formatter = new DataFormatter();
        DecimalFormat df = new DecimalFormat("#.###");
        DecimalFormat longf = new DecimalFormat("#");
        boolean isExistError = false;
        
        //Huypq-06052020-start
        int counts = 0;
        List<String> listConstrExcel = new ArrayList<>();
        List<String> listContractExcel  = new ArrayList<>();
        List<String> listSysUserExcel = new ArrayList<>();
        for (Row rows : sheet) {
        	counts++;
            if (counts > 2 && !ValidateUtils.checkIfRowIsEmpty(rows)) {
            	listConstrExcel.add(formatter.formatCellValue(rows.getCell(3)).trim().toUpperCase());
            	listContractExcel.add(formatter.formatCellValue(rows.getCell(6)).trim().toUpperCase());
            	listSysUserExcel.add(formatter.formatCellValue(rows.getCell(11)).trim().toUpperCase());
            }
        }
        
        HashMap<String, ConstructionDetailDTO> mapContractCode = new HashMap<>();
        HashMap<String, ConstructionDetailDTO> mapContractCons = new HashMap<>();
        List<ConstructionDetailDTO> lstContract= detailMonthPlanOSDAO.getCntContractByLstContractCode(listContractExcel);
        for(ConstructionDetailDTO dto : lstContract) {
        	mapContractCode.put(dto.getCntContractCode().trim().toUpperCase(), dto);
        	mapContractCons.put(dto.getCntContractCode().trim().toUpperCase()+"|"+dto.getConstructionCode().trim().toUpperCase(), dto);
        }
        
        HashMap<String, ConstructionDetailDTO> mapCons = new HashMap<>();
        List<ConstructionDetailDTO> lstCons = constructionDAO.getConstructionByCode(listConstrExcel);
        for(ConstructionDetailDTO dto : lstCons) {
        	mapCons.put(dto.getCode().toUpperCase(), dto);
        }
        
        HashMap<String, DepartmentDTO> mapEmployeeCode = new HashMap<>();
        HashMap<String, DepartmentDTO> mapEmail = new HashMap<>();
        List<DepartmentDTO> lstUser = detailMonthPlanOSDAO.getSysUserByLstUser(listSysUserExcel);
        for(DepartmentDTO dto : lstUser) {
        	mapEmployeeCode.put(dto.getEmployeeCode().toUpperCase().trim(), dto);
        	mapEmail.put(dto.getEmail().toUpperCase().trim(), dto);
        }
        //Huy-end
        
        //Huypq-20200201-start
        HashMap<String, Long> mapNguon = new HashMap<>();
        HashMap<String, Long> mapLoaiCt = new HashMap<>();
        HashMap<String, String> mapCheckDup = new HashMap<>();
        //Huy-end	
        int count = 0;
        for (Row row : sheet) {
            count++;
            if (count > 2 && !ValidateUtils.checkIfRowIsEmpty(row)) {
                boolean checkColumn2 = true;
                boolean checkColumn6 = true;
                boolean checkColumn8 = true;
                boolean checkColumn9 = true;
                boolean checkColumn10 = true;
                boolean checkColumn11 = true;
                boolean checkColumn12 = true;
                boolean checkColumn13 = true;
                boolean checkColumn14 = true;
                StringBuilder errorMesg = new StringBuilder();
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                ConstructionTaskDetailDTO newObj = new ConstructionTaskDetailDTO();
                for (int i = 0; i < 15; i++) {
                    Cell cell = row.getCell(i);
                    if (cell != null) {
                        if (cell.getColumnIndex() == 3) {
                            try {
                                String code = formatter.formatCellValue(row.getCell(3)).trim();
                                if(mapCheckDup.size()==0) {
//                                	ConstructionDetailDTO obj = constructionDAO.getConstructionByCode(code);
                                	ConstructionDetailDTO obj = mapCons.get(code.toUpperCase());
                                    newObj.setProvinceName(obj.getProvinceName());
                                    newObj.setConstructionCode(obj.getCode());
                                    newObj.setConstructionName(obj.getName());
                                    newObj.setCatStationCode(obj.getCatStationCode());
                                    newObj.setConstructionId(obj.getConstructionId());
                                    newObj.setConstructionType(obj.getCatContructionTypeName());
                                    newObj.setIsObstructedName("1".equals(obj.getIsObstructed()) ? "Có" : "");
                                    mapCheckDup.put(code.toUpperCase().trim(), code);
                                } else {
                                	if(mapCheckDup.get(code.toUpperCase().trim())!=null) {
                                		checkColumn2 = false;
                                		isExistError = true;
                                        errorMesg.append("\nMã công trình trong cùng file import không được trùng nhau !");
                                	} else {
                                		ConstructionDetailDTO obj = mapCons.get(code.toUpperCase());
                                        newObj.setProvinceName(obj.getProvinceName());
                                        newObj.setConstructionCode(obj.getCode());
                                        newObj.setConstructionName(obj.getName());
                                        newObj.setCatStationCode(obj.getCatStationCode());
                                        newObj.setConstructionId(obj.getConstructionId());
                                        newObj.setConstructionType(obj.getCatContructionTypeName());
                                        newObj.setIsObstructedName("1".equals(obj.getIsObstructed()) ? "Có" : "");
                                        mapCheckDup.put(code.toUpperCase().trim(), code);
                                	}
                                }
                            } catch (Exception e) {
                                checkColumn2 = false;
                                isExistError = true;
                                errorMesg.append("\nChưa nhập mã công trình hoặc bị sai");
                            }
//                            if (!checkColumn2) {
//                                isExistError = true;
//                                errorMesg.append("\nChưa nhập mã công trình hoặc bị sai");
//                            }

                        } else if (cell.getColumnIndex() == 6) {
                            try {
                            	String code = formatter.formatCellValue(row.getCell(6));
                            	if(StringUtils.isNotBlank(code)) {
                            		if(mapContractCode.get(code.trim().toUpperCase())!=null) {
                            			if(mapContractCons.get(code.trim().toUpperCase()+"|"+newObj.getConstructionCode().toUpperCase().trim())==null) {
                            				isExistError = true;
                                            errorMesg.append("\nMã công trình chưa được gán vào hợp đồng đầu ra !");
                            			} else {
                            				newObj.setCntContract(code);
                            			}
                            		} else {
                            			isExistError = true;
                                        errorMesg.append("\nMã hợp đồng đầu ra không tồn tại!");
                            		}
                            	} else {
                            		isExistError = true;
                                    errorMesg.append("\nChưa nhập mã hợp đồng đầu ra !");
                            	}
                            } catch (Exception e) {
                            	isExistError = true;
                                errorMesg.append("\nMã hợp đồng đầu ra không hợp lệ!");
                            }
                        } else if (cell.getColumnIndex() == 7) {
                            try {
                                Double quantity = new Double(cell.getNumericCellValue() * 1000000);
                                newObj.setQuantity(quantity);
                            } catch (Exception e) {
                                checkColumn6 = false;
                                isExistError = true;
                                errorMesg.append("\nGiá trị không hợp lệ!");
                            }
//                            if (!checkColumn6) {
//                                isExistError = true;
//                                errorMesg.append("\nGiá trị không hợp lệ!");
//                            }
                        } 
                        
//                        else if (cell.getColumnIndex() == 8) {
//                            if (row.getCell(8) != null) {
//                            	try {
//                                    Long nguon = new Long(Long.parseLong(formatter.formatCellValue(row.getCell(8)).trim()));
//                                    if(nguon>0 && nguon<7) {
//                                    	//Huypq-20200201-start
//                                    	if(mapNguon.size()==0) {
//                                    		newObj.setSourceWork(String.valueOf(nguon));
//                                    		mapNguon.put(newObj.getConstructionCode(), nguon);
//                                    	} else {
//                                    		if(mapNguon.get(newObj.getConstructionCode()) != nguon) {
//                                    			checkColumn8 = false;
//                                            	errorMesg.append("\nNguồn việc của những hạng mục thuộc cùng công trình phải giống nhau!");
//                                    		} else {
//                                    			newObj.setSourceWork(String.valueOf(nguon));
//                                        		mapNguon.put(newObj.getConstructionCode(), nguon);
//                                    		}
//                                    	}
//                                    	//Huypq-end
//                                    } else {
//                                    	checkColumn8 = false;
//                                    	errorMesg.append("\nNguồn việc không hợp lệ !");
//                                    }
//                                } catch (Exception e) {
//                                    checkColumn8 = false;
//                                    errorMesg.append("\nNguồn việc phải nhập dạng số!");
//                                }
//                            } else {
//                            	checkColumn8 = false;
//                            	errorMesg.append("\nNguồn việc không được để trống !");
//                            }
//                        } else if (cell.getColumnIndex() == 9) {
//                            if (row.getCell(9) != null) {
//                            	try {
//                                    Long loaict = new Long(Long.parseLong(formatter.formatCellValue(row.getCell(9)).trim()));
//                                    if(loaict>0 && loaict<10) {
//                                    	//Huypq-20200201-start
//                                    	if(mapLoaiCt.size()==0) {
//                                    		newObj.setConstructionTypeNew(String.valueOf(loaict));
//                                    		mapLoaiCt.put(newObj.getConstructionCode(), loaict);
//                                    	} else {
//                                    		if(mapLoaiCt.get(newObj.getConstructionCode()) != loaict) {
//                                    			checkColumn9 = false;
//                                            	errorMesg.append("\nLoại công trình của những hạng mục thuộc cùng công trình phải giống nhau!");
//                                    		} else {
//                                    			newObj.setConstructionTypeNew(String.valueOf(loaict));
//                                        		mapLoaiCt.put(newObj.getConstructionCode(), loaict);
//                                    		}
//                                    	}
//                                    	//Huypq-end
//                                    } else {
//                                    	checkColumn9 = false;
//                                    	errorMesg.append("\nLoại công trình không hợp lệ !");
//                                    }
//                                } catch (Exception e) {
//                                    checkColumn9 = false;
//                                    errorMesg.append("\nLoại công trình phải nhập dạng số!");
//                                }
//                            } else {
//                            	checkColumn9 = false;
//                            	errorMesg.append("\nLoại công trình không được để trống !");
//                            }
//                        } 
                        
                        else if (cell.getColumnIndex() == 10) {
                            try {
                                Date completeDate = dateFormat.parse(formatter.formatCellValue(cell));
                                newObj.setCompleteDate(completeDate);
                            } catch (Exception e) {
                                checkColumn10 = false;
                            }
                        } else if (cell.getColumnIndex() == 11) {
                            try {
                                String name = formatter.formatCellValue(cell);
                                if(StringUtils.isNotBlank(name)) {
                                	DepartmentDTO objCode = mapEmployeeCode.get(name.toUpperCase().trim());
                                	DepartmentDTO objEmail = mapEmail.get(name.toUpperCase().trim());
                                	if(objCode!=null) {
                                		newObj.setPerformerId(objCode.getDepartmentId());
                                        newObj.setPerformerName(objCode.getName());
                                	} else if(objEmail!=null) {
                                		newObj.setPerformerId(objEmail.getDepartmentId());
                                        newObj.setPerformerName(objEmail.getName());
                                	} else {
                                		checkColumn11 = false;
                                        isExistError = true;
                                        errorMesg.append("\nNgười thực hiện không tồn tại !");
                                	}
                                } else {
                                	checkColumn11 = false;
                                    isExistError = true;
                                    errorMesg.append("\nChưa nhập người thực hiện !");
                                }
//                                DepartmentDTO obj = detailMonthPlanOSDAO.getSysUser(name);
//                                newObj.setPerformerId(obj.getDepartmentId());
//                                newObj.setPerformerName(obj.getName());
                            } catch (Exception e) {
                                checkColumn11 = false;
                                isExistError = true;
                                errorMesg.append("\nNgười thực hiện không hợp lệ !");
                            }
//                            if (!checkColumn11) {
//                                isExistError = true;
//                                errorMesg.append("\nSai tên đăng nhập!");
//                            }

                        } else if (cell.getColumnIndex() == 12) {
                            try {
                             Date startDate = dateFormat.parse(formatter.formatCellValue(cell));
                             if(validateDate(formatter.formatCellValue(cell)))
                                	newObj.setStartDate(startDate);
                             else
                                	checkColumn12 = false;
                                
                            } catch (Exception e) {
                                checkColumn12 = false;
                            }
                            if (!checkColumn12) {
                                isExistError = true;
                                errorMesg.append("\nNgày bắt đầu không hợp lệ!");
                            }
                        } else if (cell.getColumnIndex() == 13) {
                            try {                                
                                Date endDate = dateFormat.parse(formatter.formatCellValue(cell));
                                if(validateDate(formatter.formatCellValue(cell)))
                                	newObj.setEndDate(endDate);
                                else
                                	checkColumn13 = false;
                            } catch (Exception e) {
                                checkColumn13 = false;
                            }
                            if (!checkColumn13) {
                                isExistError = true;
                                errorMesg.append("\nNgày kết thúc không hợp lệ!");
                            }
                        } else if (cell.getColumnIndex() == 14) {
                            try {
                                String descriptionHSHC = formatter.formatCellValue(cell).trim();
                                if (descriptionHSHC.length() <= 1000) {
                                    newObj.setDescription(descriptionHSHC);
                                } else {
                                    checkColumn14 = false;
                                    isExistError = true;
                                }

                            } catch (Exception e) {
                                checkColumn14 = false;
                            }
                            if (!checkColumn14) {
                                isExistError = true;
                                errorMesg.append("\nGhi chú vượt quá 1000 ký tự!");
                            }
                            Cell cell1 = row.createCell(15);
                            cell1.setCellValue(errorMesg.toString());

                        }
//                        hoanm1_20180912_start
                        Cell cell1 = row.createCell(15);
                        cell1.setCellValue(errorMesg.toString());
//                        hoanm1_20180912_end
                    }
                }
                if (!isExistError) {
                    newObj.setCntContract(formatter.formatCellValue(row.getCell(6)) != null
                            ? formatter.formatCellValue(row.getCell(6))
                            : "");
                    workLst.add(newObj);
                }

            }
        }
        if (isExistError) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            Cell cell = sheet.getRow(1).createCell(15);
            cell.setCellValue("Cột lỗi");
            cell.setCellStyle(style);
            ConstructionTaskDetailDTO objErr = new ConstructionTaskDetailDTO();
            OutputStream out = new FileOutputStream(f, true);
            workbook.write(out);
            out.close();
            objErr.setErrorFilePath(UEncrypt.encryptFileUploadPath(filePath));
            workLst.add(objErr);
        }
        workbook.close();
        return workLst;
    }

    public String exportTemplateListLDT(Long sysGroupId, HttpServletRequest request) throws Exception {
        String folderParam = defaultSubFolderUpload;
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();
        InputStream file = new BufferedInputStream(
                new FileInputStream(filePath + "Import_doanhthu_thang_chitiet_ngoaiOS.xlsx"));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        file.close();
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String uploadPath = folder2Upload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
                + File.separator + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1)
                + File.separator + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload) + File.separator
                + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator
                + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        File udir = new File(uploadPath);
        if (!udir.exists()) {
            udir.mkdirs();
        }

        OutputStream outFile = new FileOutputStream(
                udir.getAbsolutePath() + File.separator + "Import_doanhthu_thang_chitiet_ngoaiOS.xlsx");
//		chinhpxn20180705_start
//		List<ConstructionDetailDTO> listConstr = constructionDAO
//				.getConstructionForLDT(sysGroupId);
        XSSFSheet sheet = workbook.getSheetAt(0);
//		fillSheetLDT(sheet, listConstr);
//		chinhpxn20180705_end
        SysUserCOMSDTO obj = new SysUserCOMSDTO();
        String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
                Constant.AdResourceKey.MONTHLY_DETAIL_PLAN, request);
        List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
        List<SysUserCOMSDTO> listUser = sysUserCOMSDAO.usersFillter(groupIdList);
        sheet = workbook.getSheetAt(1);
        fillSheet2(sheet, listUser);
        workbook.write(outFile);
        workbook.close();
        outFile.close();

        String path = UEncrypt
                .encryptFileUploadPath(uploadPathReturn + File.separator + "Import_doanhthu_thang_chitiet_ngoaiOS.xlsx");
        return path;
    }

    private void fillSheetLDT(XSSFSheet sheet, List<ConstructionDetailDTO> listConstr) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if (listConstr != null && !listConstr.isEmpty()) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet);
            XSSFCellStyle styleCurrency = ExcelUtils.styleCurrency(sheet);
            XSSFCellStyle styleDate = ExcelUtils.styleDate(sheet);
            int i = 2;
            for (ConstructionDetailDTO dto : listConstr) {
                Row row = sheet.createRow(i++);
                Cell cell = row.createCell(0, CellType.STRING);
                cell.setCellValue("" + (i - 2));
                cell.setCellStyle(style);
                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue(StringUtils.isNotEmpty(dto.getCatProvince()) ? dto.getCatProvince() : "");
                cell.setCellStyle(style);
                cell = row.createCell(2, CellType.STRING);
                cell.setCellValue(StringUtils.isNotEmpty(dto.getCatStationCode()) ? dto.getCatStationCode() : "");
                cell.setCellStyle(style);
                cell = row.createCell(3, CellType.STRING);
                cell.setCellValue(StringUtils.isNotEmpty(dto.getCode()) ? dto.getCode() : "");
                cell.setCellStyle(style);
                cell = row.createCell(4, CellType.STRING);
                cell.setCellValue(
                        StringUtils.isNotEmpty(dto.getCatContructionTypeName()) ? dto.getCatContructionTypeName() : "");
                cell.setCellStyle(style);
                cell = row.createCell(5, CellType.STRING);
                cell.setCellValue(StringUtils.isNotEmpty(dto.getCntContract()) ? dto.getCntContract() : "");
                cell.setCellStyle(style);
                cell = row.createCell(6, CellType.NUMERIC);
                cell.setCellValue((dto.getQuantity() != null) ? dto.getQuantity() : 0);
                cell.setCellStyle(styleCurrency);
                cell = row.createCell(7, CellType.STRING);
                cell.setCellValue((dto.getCompleteDate() != null) ? dateFormat.format(dto.getCompleteDate()) : "");
                cell.setCellStyle(styleDate);
                for (int j = 8; j < 12; j++) {
                    cell = row.createCell(j, CellType.STRING);
                    cell.setCellValue("");
                    cell.setCellStyle(style);
                }
            }
        }
    }

    public List<ConstructionTaskDetailDTO> importLDT(String fileInput, String filePath) throws Exception {
        List<ConstructionTaskDetailDTO> workLst = new ArrayList<ConstructionTaskDetailDTO>();
        File f = new File(fileInput);
        ZipSecureFile.setMinInflateRatio(-1.0d);//hoangnh add
        XSSFWorkbook workbook = new XSSFWorkbook(f);
        XSSFSheet sheet = workbook.getSheetAt(0);
        DataFormatter formatter = new DataFormatter();
        DecimalFormat df = new DecimalFormat("#.###");
        DecimalFormat longf = new DecimalFormat("#");
        boolean isExistError = false;
        List<String> listErorrString = new ArrayList<>();
        //Huypq-20200201-start
        HashMap<String, Long> mapNguon = new HashMap<>();
        HashMap<String, Long> mapLoaiCt = new HashMap<>();
//        ConstructionTaskDetailDTO taskType = new ConstructionTaskDetailDTO();
        HashMap<String, String> mapCheckDup = new HashMap<>();
        //Huy-end
        
        //Huypq-14042020-start
        List<String> listConstrExcel = new ArrayList<>();
        List<String> listSysUserExcel = new ArrayList<>();
        List<String> listContractExcel = new ArrayList<>();
        int counts = 0;
        for (Row rows : sheet) {
        	counts++;
            if (counts > 2 && !ValidateUtils.checkIfRowIsEmpty(rows)) {
            	listConstrExcel.add(formatter.formatCellValue(rows.getCell(3)).trim().toUpperCase());
            	listContractExcel.add(formatter.formatCellValue(rows.getCell(5)).trim().toUpperCase());
            	listSysUserExcel.add(formatter.formatCellValue(rows.getCell(10)).trim().toUpperCase());
            }
        }
        
        HashMap<String, ConstructionDetailDTO> mapContractCode = new HashMap<>();
        HashMap<String, ConstructionDetailDTO> mapContractCons = new HashMap<>();
        List<ConstructionDetailDTO> lstContract= detailMonthPlanOSDAO.getCntContractByLstContractCode(listContractExcel);
        for(ConstructionDetailDTO dto : lstContract) {
        	mapContractCode.put(dto.getCntContractCode().trim().toUpperCase(), dto);
        	mapContractCons.put(dto.getCntContractCode().trim().toUpperCase()+"|"+dto.getConstructionCode().trim().toUpperCase(), dto);
        }
        
        HashMap<Long, String> mapSourceWork = new HashMap<>();
        HashMap<Long, String> mapConsType = new HashMap<>();
        List<ConstructionTaskDetailDTO> lstConsType = constructionTaskDAO.checkSourceWorkByConsId(listConstrExcel);
        for(ConstructionTaskDetailDTO dto : lstConsType) {
        	mapSourceWork.put(dto.getConstructionId(), dto.getSourceWork());
        	mapConsType.put(dto.getConstructionId(), dto.getConstructionType());
        }
        
        HashMap<String, ConstructionDetailDTO> mapCons = new HashMap<>();
        List<ConstructionDetailDTO> lstCons = constructionDAO.getConstructionByCode(listConstrExcel);
        for(ConstructionDetailDTO dto : lstCons) {
        	mapCons.put(dto.getCode().toUpperCase().trim(), dto);
        }
        
        HashMap<String, DepartmentDTO> mapEmployeeCode = new HashMap<>();
        HashMap<String, DepartmentDTO> mapEmail = new HashMap<>();
        List<DepartmentDTO> lstUser = detailMonthPlanOSDAO.getSysUserByLstUser(listSysUserExcel);
        for(DepartmentDTO dto : lstUser) {
        	mapEmployeeCode.put(dto.getEmployeeCode().toUpperCase().trim(), dto);
        	mapEmail.put(dto.getEmail().toUpperCase().trim(), dto);
        }
        //Huy-end
        
        int count = 0;
        for (Row row : sheet) {
            count++;
            if (count > 2 && !ValidateUtils.checkIfRowIsEmpty(row)) {
                boolean checkColumn3 = true;
                boolean checkColumn6 = true;
                boolean checkColumn7 = true;
                boolean checkColumn8 = true;
                boolean checkColumn9 = true;
                boolean checkColumn10 = true;
                boolean checkColumn11 = true;
                boolean checkColumn12 = true;
                boolean checkColumn14 = true;
                StringBuilder errorMesg = new StringBuilder();
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                ConstructionTaskDetailDTO newObj = new ConstructionTaskDetailDTO();
                for (int i = 0; i < 15; i++) {
                    Cell cell = row.getCell(i);
                    if (cell != null) {
                        if (cell.getColumnIndex() == 3) {
                            try {
                                String code = formatter.formatCellValue(cell).trim();
                                if(mapCheckDup.size()==0) {
                                	ConstructionDetailDTO obj = mapCons.get(code.toUpperCase());
                                	if(obj==null) {
                                		checkColumn3 = false;
                                        isExistError = true;
                                        errorMesg.append("\nMã công trình không tồn tại");
                                        listErorrString.add("Có lỗi validate");
                                	} else {
                                		if(obj.getApproveRevenueState().equals("2")) {
                                			checkColumn3 = false;
                                			isExistError = true;
                                            errorMesg.append("\nMã công trình đã được phê duyệt doanh thu trước đó !");
                                            listErorrString.add("Có lỗi validate");
                                		} else {
                                			newObj.setProvinceName(obj.getProvinceName());
                                            newObj.setConstructionCode(obj.getCode());
                                            newObj.setCatStationCode(obj.getCatStationCode());
                                            newObj.setConstructionName(obj.getName());
                                            newObj.setConstructionId(obj.getConstructionId());
                                            newObj.setConstructionType(obj.getCatContructionTypeName());
                                            newObj.setCheckHTCT(obj.getCheckHTCT());
                                		}
                                	}
                                	mapCheckDup.put(code.toUpperCase().trim(), code);
                                } else {
                                	if(mapCheckDup.get(code.toUpperCase().trim())!=null) {
                                		checkColumn3 = false;
                                        isExistError = true;
                                        errorMesg.append("\nMã công trình trong cùng file import không được trùng nhau !");
                                        listErorrString.add("Có lỗi validate");
                                	} else {
                                		ConstructionDetailDTO obj = mapCons.get(code.toUpperCase());
                                    	if(obj==null) {
                                    		checkColumn3 = false;
                                            isExistError = true;
                                            errorMesg.append("\nMã công trình không tồn tại");
                                            listErorrString.add("Có lỗi validate");
                                    	} else {
                                    		if(obj.getApproveRevenueState().equals("2")) {
                                    			checkColumn3 = false;
                                    			isExistError = true;
                                                errorMesg.append("\nMã công trình đã được phê duyệt doanh thu trước đó !");
                                                listErorrString.add("Có lỗi validate");
                                    		} else {
                                    			newObj.setProvinceName(obj.getProvinceName());
                                                newObj.setConstructionCode(obj.getCode());
                                                newObj.setCatStationCode(obj.getCatStationCode());
                                                newObj.setConstructionName(obj.getName());
                                                newObj.setConstructionId(obj.getConstructionId());
                                                newObj.setConstructionType(obj.getCatContructionTypeName());
                                                newObj.setCheckHTCT(obj.getCheckHTCT());
                                    		}
                                    	}
                                    	mapCheckDup.put(code.toUpperCase().trim(), code);
                                	}
                                }
                                
                            } catch (Exception e) {
                                checkColumn3 = false;
                                isExistError = true;
                                errorMesg.append("\nChưa nhập mã công trình hoặc mã công trình không tồn tại");
                                listErorrString.add("Có lỗi validate");
                            }
//                            if (!checkColumn3) {
//                                isExistError = true;
//                                errorMesg.append("\nChưa nhập mã công trình hoặc mã công trình không tồn tại");
//                            }

                        } else if (cell.getColumnIndex() == 5) {
//                            newObj.setCntContract(
//                                    formatter.formatCellValue(cell) != null ? formatter.formatCellValue(cell) : "");
                        	try {
                            	String code = formatter.formatCellValue(row.getCell(5));
                            	if(StringUtils.isNotBlank(code)) {
                            		if(mapContractCode.get(code.trim().toUpperCase())!=null) {
                            			if(newObj.getConstructionCode()!=null && mapContractCons.get(code.trim().toUpperCase()+"|"+newObj.getConstructionCode().toUpperCase().trim())==null) {
                            				isExistError = true;
                                            errorMesg.append("\nMã công trình chưa được gán vào hợp đồng đầu ra !");
                                            listErorrString.add("Có lỗi validate");
                            			} else {
                            				newObj.setCntContract(code);
                            			}
                            		} else {
                            			isExistError = true;
                                        errorMesg.append("\nMã hợp đồng đầu ra không tồn tại!");
                                        listErorrString.add("Có lỗi validate");
                            		}
                            	} else {
                            		isExistError = true;
                                    errorMesg.append("\nChưa nhập mã hợp đồng đầu ra !");
                                    listErorrString.add("Có lỗi validate");
                            	}
                            } catch (Exception e) {
                            	isExistError = true;
                                errorMesg.append("\nMã hợp đồng đầu ra không hợp lệ!");
                                listErorrString.add("Có lỗi validate");
                            }
                        } else if (cell.getColumnIndex() == 6) {
                            try {
                                // chinhpxn 20180606 start - delete * 1000000
//                                Double quantity = new Double(cell.getNumericCellValue()); ----Huypq-20191003-comment
                                // chinhpxn 20180606 end
                              //Huypq-20191003-start
                                Double quantity = new Double(cell.getNumericCellValue() * 1000000);
                                //Huy-end
                                newObj.setQuantity(quantity);
                            } catch (Exception e) {
                                checkColumn6 = false;
                                isExistError = true;
                                errorMesg.append("\nGiá trị không hợp lệ!");
                                listErorrString.add("Có lỗi validate");
                            }
//                            if (!checkColumn6) {
//                                isExistError = true;
//                                errorMesg.append("\nGiá trị không hợp lệ!");
//                            }
                        } 
                        
                        else if (cell.getColumnIndex() == 7) {
                            if (row.getCell(7) != null) {
                            	try {
                            		long nguon = new Long(Long.parseLong(formatter.formatCellValue(row.getCell(7)).trim()));
                                    if(nguon>0 && nguon<8) {
                                    	//Huypq-20200201-start
                                    	if(newObj.getConstructionId()!=null) {
                                    		String sourceWorkDb = mapSourceWork.get(newObj.getConstructionId());
                                    		if(sourceWorkDb!=null) {
                                    			if(newObj.getCheckHTCT()!=null && newObj.getCheckHTCT()==1l) {
                                        			if(nguon != 4l && nguon != 5l) {
                                        				isExistError = true;
                                        				checkColumn7 = false;
                                                    	errorMesg.append("\nNguồn việc của công trình HTCT chỉ được nhập Xây dựng móng và Hoàn thiện!");
                                                        listErorrString.add("Có lỗi validate");
                                        			} else {
                                        				newObj.setSourceWork(String.valueOf(nguon));
                                        			}
                                        		} else {
                                        			if(nguon == 4l || nguon == 5l) {
                                        				isExistError = true;
                                        				checkColumn7 = false;
                                                    	errorMesg.append("\nNguồn việc Xây dựng móng và Hoàn thiện chỉ ứng với công trình HTCT!");
                                                        listErorrString.add("Có lỗi validate");
                                        			} else {
                                        				if(sourceWorkDb.equals(formatter.formatCellValue(row.getCell(7)).trim())) {
                                                			if(mapNguon.size()==0) {
                                                        		newObj.setSourceWork(String.valueOf(nguon));
                                                        		mapNguon.put(newObj.getConstructionCode(), nguon);
                                                        	} else {
                                                        		if(mapNguon.get(newObj.getConstructionCode())!=null && mapNguon.get(newObj.getConstructionCode()) != nguon) {
                                                        			isExistError = true;
                                                        			checkColumn7 = false;
                                                                	errorMesg.append("\nNguồn việc của những hạng mục thuộc cùng công trình phải giống nhau!");
                                                                    listErorrString.add("Có lỗi validate");
                                                        		} else {
                                                        			newObj.setSourceWork(String.valueOf(nguon));
                                                            		mapNguon.put(newObj.getConstructionCode(), nguon);
                                                        		}
                                                        	}
                                                		} else {
                                                			isExistError = true;
                                                			checkColumn7 = false;
                                                        	errorMesg.append(" Nguồn việc của công trình: "+ newObj.getConstructionCode() + " đã tồn tại bằng: "+ sourceWorkDb);
                                                            listErorrString.add("Có lỗi validate");
                                                		}
                                        			}
                                        		}
                                        	} else {
                                        		if(newObj.getCheckHTCT()!=null && newObj.getCheckHTCT()==1l) {
                                        			if(nguon != 4l && nguon != 5l) {
                                        				isExistError = true;
                                        				checkColumn7 = false;
                                                    	errorMesg.append("\nNguồn việc của công trình HTCT chỉ được nhập Xây dựng móng và Hoàn thiện!");
                                                        listErorrString.add("Có lỗi validate");
                                        			} else {
                                        				newObj.setSourceWork(String.valueOf(nguon));
                                        			}
                                        		} else {
                                        			if(nguon == 4l || nguon == 5l) {
                                        				isExistError = true;
                                        				checkColumn7 = false;
                                                    	errorMesg.append("\nNguồn việc Xây dựng móng và Hoàn thiện chỉ ứng với công trình HTCT!");
                                                        listErorrString.add("Có lỗi validate");
                                        			} else {
                                        				if(mapNguon.size()==0) {
                                                    		newObj.setSourceWork(String.valueOf(nguon));
                                                    		mapNguon.put(newObj.getConstructionCode(), nguon);
                                                    	} else {
                                                    		if(mapNguon.get(newObj.getConstructionCode())!=null && mapNguon.get(newObj.getConstructionCode()) != nguon) {
                                                    			isExistError = true;
                                                    			checkColumn7 = false;
                                                            	errorMesg.append("\nNguồn việc của những hạng mục thuộc cùng công trình phải giống nhau!");
                                                                listErorrString.add("Có lỗi validate");
                                                    		} else {
                                                    			newObj.setSourceWork(String.valueOf(nguon));
                                                        		mapNguon.put(newObj.getConstructionCode(), nguon);
                                                    		}
                                                    	}
                                        			}
                                        		}
                                        	}
                                    	} else {
                                    		newObj.setSourceWork(String.valueOf(nguon));
                                    		mapNguon.put(newObj.getConstructionCode(), nguon);
                                    	}
                                    	//Huypq-end
                                    } else {
                                    	isExistError = true;
                                    	checkColumn7 = false;
                                    	errorMesg.append("\nNguồn việc không hợp lệ !");
                                        listErorrString.add("Có lỗi validate");
                                    }
                                } catch (Exception e) {
                                	isExistError = true;
                                    checkColumn7 = false;
                                    errorMesg.append("\nNguồn việc phải nhập dạng số!");
                                    listErorrString.add("Có lỗi validate");
                                }
                            } else {
                            	isExistError = true;
                            	checkColumn7 = false;
                            	errorMesg.append("\nNguồn việc không được để trống !");
                                listErorrString.add("Có lỗi validate");
                            }
                        } else if (cell.getColumnIndex() == 8) {
                            if (row.getCell(8) != null) {
                            	try {
                            		long loaict = new Long(Long.parseLong(formatter.formatCellValue(row.getCell(8)).trim()));
                                    if(loaict>0 && loaict<10) {
                                    	//Huypq-20200201-start
                                    	if(newObj.getConstructionId()!=null) {
                                        	String consTypeDb = mapConsType.get(newObj.getConstructionId());
                                    		if(consTypeDb!=null) {
                                        		if(consTypeDb.equals(formatter.formatCellValue(row.getCell(8)).trim())) {
                                        			if(mapLoaiCt.size()==0) {
                                        				checkValidateConstructionType(newObj, loaict, errorMesg, isExistError, mapLoaiCt, listErorrString);
                                                	} else {
                                                		if(mapLoaiCt.get(newObj.getConstructionCode())!=null && mapLoaiCt.get(newObj.getConstructionCode()) != loaict) {
                                                			 isExistError = true;
                                                 			checkColumn8 = false;
                                                         	errorMesg.append("\nLoại công trình của những hạng mục thuộc cùng công trình phải giống nhau!");
                                                            listErorrString.add("Có lỗi validate");
                                                		} else {
                                                			checkValidateConstructionType(newObj, loaict, errorMesg, isExistError, mapLoaiCt, listErorrString);
                                                		}
                                                	}
                                        		} else {
                                        			isExistError = true;
                                         			checkColumn8 = false;
                                                 	errorMesg.append(" Loại công trình của công trình: "+ newObj.getConstructionCode() +" đã tồn tại bằng: "+ consTypeDb);
                                                    listErorrString.add("Có lỗi validate");
                                        		}
                                        	} else {
                                        		if(mapLoaiCt.size()==0) {
                                        			checkValidateConstructionType(newObj, loaict, errorMesg, isExistError, mapLoaiCt, listErorrString);
                                            	} else {
                                            		if(mapLoaiCt.get(newObj.getConstructionCode())!=null && mapLoaiCt.get(newObj.getConstructionCode()) != loaict) {
                                            			isExistError = true;
                                             			checkColumn8 = false;
                                                     	errorMesg.append("\nLoại công trình của những hạng mục thuộc cùng công trình phải giống nhau!");
                                                        listErorrString.add("Có lỗi validate");
                                            		} else {
                                            			checkValidateConstructionType(newObj, loaict, errorMesg, isExistError, mapLoaiCt, listErorrString);
                                            		}
                                            	}
                                        	}
                                    	} else {
                                    		checkValidateConstructionType(newObj, loaict, errorMesg, isExistError, mapLoaiCt, listErorrString);
                                    	}
                                    	
                                    	//Huypq-end
                                    } else {
                                    	isExistError = true;
                                    	checkColumn8 = false;
                                    	errorMesg.append("\nLoại công trình không hợp lệ !");
                                        listErorrString.add("Có lỗi validate");
                                    }
                                } catch (Exception e) {
                                	isExistError = true;
                                    checkColumn8 = false;
                                    errorMesg.append("\nLoại công trình phải nhập dạng số!");
                                    listErorrString.add("Có lỗi validate");
                                }
                            } else {
                            	isExistError = true;
                            	checkColumn8 = false;
                            	errorMesg.append("\nLoại công trình không được để trống !");
                                listErorrString.add("Có lỗi validate");
                            }
                        } 
                        
                        else if (cell.getColumnIndex() == 10) {
//                            try {
//                                String name = formatter.formatCellValue(cell).trim();
//                                DepartmentDTO obj = detailMonthPlanOSDAO.getSysUser(name);
//                                newObj.setPerformerId(obj.getDepartmentId());
//                                newObj.setPerformerName(obj.getName());
//                            } catch (Exception e) {
//                                checkColumn10 = false;
//                                isExistError = true;
//                                errorMesg.append("\nSai tên đăng nhập!");
//                            }
                            try {
                                String name = formatter.formatCellValue(cell);
                                if(StringUtils.isNotBlank(name)) {
                                	DepartmentDTO objCode = mapEmployeeCode.get(name.toUpperCase().trim());
                                	DepartmentDTO objEmail = mapEmail.get(name.toUpperCase().trim());
                                	if(objCode!=null) {
                                		newObj.setPerformerId(objCode.getDepartmentId());
                                        newObj.setPerformerName(objCode.getName());
                                	} else if(objEmail!=null) {
                                		newObj.setPerformerId(objEmail.getDepartmentId());
                                        newObj.setPerformerName(objEmail.getName());
                                	} else {
                                		checkColumn10 = false;
                                		isExistError = true;
                                        errorMesg.append("\nNgười thực hiện không tồn tại !");
                                        listErorrString.add("Có lỗi validate");
                                	}
                                } else {
                                	checkColumn10 = false;
                                	isExistError = true;
                                    errorMesg.append("\nChưa nhập người thực hiện !");
                                    listErorrString.add("Có lỗi validate");
                                }
                            } catch (Exception e) {
                            	checkColumn10 = false;
                            	isExistError = true;
                                errorMesg.append("\nNgười thực hiện không hợp lệ !");
                                listErorrString.add("Có lỗi validate");
                            }
//                            if (!checkColumn10) {
//                                isExistError = true;
//                                errorMesg.append("\nSai tên đăng nhập!");
//                            }

                        } else if (cell.getColumnIndex() == 11) {
                            try {                               
                                Date startDate = dateFormat.parse(formatter.formatCellValue(cell));
                                if(validateDate(formatter.formatCellValue(cell))) {
                                	newObj.setStartDate(startDate);
                                } else {
                                	checkColumn11 = false;
                                	isExistError = true;
                                    errorMesg.append("\nNgày bắt đầu không hợp lệ!");
                                    listErorrString.add("Có lỗi validate");
                                }
                            } catch (Exception e) {
                                checkColumn11 = false;
                                isExistError = true;
                                errorMesg.append("\nNgày bắt đầu không hợp lệ!");
                                listErorrString.add("Có lỗi validate");
                            }
//                            if (!checkColumn11) {
//                                isExistError = true;
//                                errorMesg.append("\nNgày bắt đầu không hợp lệ!");
//                            }
                        } else if (cell.getColumnIndex() == 12) {
                            try {                                
                                Date endDate = dateFormat.parse(formatter.formatCellValue(cell));
                                if(validateDate(formatter.formatCellValue(cell))) {
                                	newObj.setEndDate(endDate);
                                } else {
                                	checkColumn12 = false;
                                	isExistError = true;
                                    errorMesg.append("\nNgày kết thúc không hợp lệ!");
                                    listErorrString.add("Có lỗi validate");
                                }
                            } catch (Exception e) {
                                checkColumn12 = false;
                                isExistError = true;
                                errorMesg.append("\nNgày kết thúc không hợp lệ!");
                                listErorrString.add("Có lỗi validate");
                            }
//                            if (!checkColumn12) {
//                                isExistError = true;
//                                errorMesg.append("\nNgày kết thúc không hợp lệ!");
//                            }
                            // chinhpxn20180621
                        } 
                        //Huypq-20191001-start
//                        else if (cell.getColumnIndex() == 11) {
//                            try {
//                                String taskOrder = formatter.formatCellValue(cell).trim();
//                                if (taskOrder.equalsIgnoreCase("1")) {
//                                    newObj.setTaskOrder("1");
//                                    newObj.setTaskName(
//                                            "Tạo đề nghị quyết toán cho công trình " + newObj.getConstructionCode());
//                                } else if (taskOrder.equalsIgnoreCase("2")) {
//                                    newObj.setTaskOrder("2");
//                                    newObj.setTaskName("Lên doanh thu cho công trình " + newObj.getConstructionCode());
//                                } else {
//                                    checkColumn11 = false;
//                                }
//                            } catch (Exception e) {
//                                checkColumn11 = false;
//                            }
//                            if (!checkColumn11) {
//                                isExistError = true;
//                                errorMesg.append("\nQuyết toán/ Doanh thu không hợp lệ");
//                            }
//                            Cell cell1 = row.createCell(13);
//                            cell1.setCellValue(errorMesg.toString());
//
//                        } 
                        //Huy-end
                        else if (cell.getColumnIndex() == 14) {
                            try {
                                String descriptionHSHC = formatter.formatCellValue(cell).trim();
                                if (descriptionHSHC.length() <= 1000) {
                                    newObj.setDescription(descriptionHSHC);
                                } else {
                                    checkColumn14 = false;
                                    isExistError = true;
                                }

                            } catch (Exception e) {
                                checkColumn14 = false;
                            }
                            if (!checkColumn14) {
                                isExistError = true;
                                errorMesg.append("\nGhi chú vượt quá 1000 ký tự!");
                                listErorrString.add("Có lỗi validate");
                            }

                        }
                        Cell cell1 = row.createCell(15);
                        cell1.setCellValue(errorMesg.toString());
                        // chinhpxn20180621
                        //Huypq-20191001-start
                        newObj.setTaskOrder("2");
                        newObj.setTaskName("Lên doanh thu cho công trình " + newObj.getConstructionCode());
                        //Huy-end
                    }
                }
                if (listErorrString.size()==0) {
                    workLst.add(newObj);
                }

            }
        }
        if (listErorrString.size()>0) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            // chinhpxn20180621
            Cell cell = sheet.getRow(1).createCell(15);
            // chinhpxn20180621
            cell.setCellValue("Cột lỗi");
            cell.setCellStyle(style);
            ConstructionTaskDetailDTO objErr = new ConstructionTaskDetailDTO();
            OutputStream out = new FileOutputStream(f, true);
            workbook.write(out);
            out.close();
            objErr.setErrorFilePath(UEncrypt.encryptFileUploadPath(filePath));
            workLst.add(objErr);
        }
        workbook.close();
        return workLst;
    }

    public String exportTemplateListCVK(Long sysGroupId, HttpServletRequest request) throws Exception {
        String folderParam = defaultSubFolderUpload;
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();
        InputStream file = new BufferedInputStream(
                new FileInputStream(filePath + "Import_congvieckhac_thang_chitiet.xlsx"));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        file.close();
        Calendar cal = Calendar.getInstance();
        String uploadPath = folder2Upload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
                + File.separator + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1)
                + File.separator + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload) + File.separator
                + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator
                + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        File udir = new File(uploadPath);
        if (!udir.exists()) {
            udir.mkdirs();
        }
        OutputStream outFile = new FileOutputStream(
                udir.getAbsolutePath() + File.separator + "Import_congvieckhac_thang_chitiet.xlsx");
        // List<WorkItemDetailDTO> listWorkItems = workItemDAO
        // .getWorkForTC(sysGroupId);
        // SysUserDetailCOMSDTO obj = new SysUserDetailCOMSDTO();
        String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
                Constant.AdResourceKey.MONTHLY_DETAIL_PLAN, request);
        List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
        List<SysUserCOMSDTO> listUser = sysUserCOMSDAO.usersFillter(groupIdList);
        XSSFSheet sheet = workbook.getSheetAt(0);
        // fillSheet1(sheet, listWorkItems);
        sheet = workbook.getSheetAt(1);
        fillSheet2(sheet, listUser);
        workbook.write(outFile);
        workbook.close();
        outFile.close();

        String path = UEncrypt
                .encryptFileUploadPath(uploadPathReturn + File.separator + "Import_congvieckhac_thang_chitiet.xlsx");
        return path;
    }

    public List<ConstructionTaskDetailDTO> importCVK(String fileInput, String filePath) throws Exception {
        List<ConstructionTaskDetailDTO> workLst = new ArrayList<ConstructionTaskDetailDTO>();
        File f = new File(fileInput);
        ZipSecureFile.setMinInflateRatio(-1.0d);//hoangnh add
        XSSFWorkbook workbook = new XSSFWorkbook(f);
        XSSFSheet sheet = workbook.getSheetAt(0);
        DataFormatter formatter = new DataFormatter();
        DecimalFormat df = new DecimalFormat("#.###");
        DecimalFormat longf = new DecimalFormat("#");
        boolean isExistError = false;
        int count = 0;
        for (Row row : sheet) {
            count++;
            if (count > 2 && !ValidateUtils.checkIfRowIsEmpty(row)) {
                boolean checkColumn2 = true;
                boolean checkColumn3 = true;
                boolean checkColumn4 = true;
                boolean checkColumn5 = true;
                StringBuilder errorMesg = new StringBuilder();
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                ConstructionTaskDetailDTO newObj = new ConstructionTaskDetailDTO();
                for (int i = 0; i < 6; i++) {
                    Cell cell = row.getCell(i);
                    if (cell != null) {
                        if (cell.getColumnIndex() == 1) {
                            // chinhpxn20180704_start
                            if (formatter.formatCellValue(cell).trim().length() > 2000) {
                                isExistError = true;
                                errorMesg.append("\nTên công việc vượt quá 2000 ký tự!");
                            } else {
                                // chinhpxn20180704_end
                                newObj.setTaskName(formatter.formatCellValue(cell).trim());
                            }

                        } else if (cell.getColumnIndex() == 2) {
                            try {
                                String name = formatter.formatCellValue(row.getCell(2)).trim();
                                DepartmentDTO obj = detailMonthPlanOSDAO.getSysUser(name);
                                newObj.setPerformerId(obj.getDepartmentId());
                                newObj.setPerformerName(obj.getName());
                            } catch (Exception e) {
                                checkColumn2 = false;
                            }
                            if (!checkColumn2) {
                                isExistError = true;
                                errorMesg.append("\nNgười thực hiện không tồn tại!");
                            }

                        } else if (cell.getColumnIndex() == 3) {
                            try {

                                Date startDate = dateFormat.parse(formatter.formatCellValue(row.getCell(3)));
                                newObj.setStartDate(startDate);
                            } catch (Exception e) {
                                checkColumn3 = false;
                            }
                            if (!checkColumn3) {
                                isExistError = true;
                                errorMesg.append("\nNgày bắt đầu không hợp lệ!");
                            }
                        } else if (cell.getColumnIndex() == 4) {
                            try {
                                Date endDate = dateFormat.parse(formatter.formatCellValue(row.getCell(4)));
                                newObj.setEndDate(endDate);
                            } catch (Exception e) {
                                checkColumn4 = false;
                            }
                            if (!checkColumn4) {
                                isExistError = true;
                                errorMesg.append("\nNgày kết thúc không hợp lệ!");
                            }
                        } else if (cell.getColumnIndex() == 5) {
                            try {
                                String descriptionHSHC = formatter.formatCellValue(cell).trim();
                                if (descriptionHSHC.length() <= 1000) {
                                    newObj.setDescription(descriptionHSHC);
                                } else {
                                    checkColumn5 = false;
                                    isExistError = true;
                                }

                            } catch (Exception e) {
                                checkColumn5 = false;
                            }
                            if (!checkColumn5) {
                                isExistError = true;
                                errorMesg.append("\nGhi chú vượt quá 100 ký tự!");
                            }
                            Cell cell1 = row.createCell(6);
                            cell1.setCellValue(errorMesg.toString());

                        }

                    }
                }
                if (checkColumn2 && checkColumn3 && checkColumn4) {
                    workLst.add(newObj);
                }

            }
        }
        if (isExistError) {
            ConstructionTaskDetailDTO objErr = new ConstructionTaskDetailDTO();
            OutputStream out = new FileOutputStream(f, true);
            workbook.write(out);
            out.close();
            objErr.setErrorFilePath(UEncrypt.encryptFileUploadPath(filePath));
            workLst.add(objErr);
        }
        workbook.close();
        return workLst;
    }

    public String exportExcelTemplate(String fileName, Long sysGroupId) throws Exception {
        // TODO Auto-generated method stub
        String folderParam = defaultSubFolderUpload;
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();
        InputStream file = new BufferedInputStream(new FileInputStream(filePath + fileName));
        // String filePath1 = UFile.writeToFileServerATTT2(file,
        // "Import_KeHoachNam.xlsx", folderParam, folder2Upload);
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        file.close();
        Calendar cal = Calendar.getInstance();
        String uploadPath = folder2Upload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
                + File.separator + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1)
                + File.separator + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload) + File.separator
                + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator
                + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        File udir = new File(uploadPath);
        if (!udir.exists()) {
            udir.mkdirs();
        }
        OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + fileName);
//		chinhpxn20180710_start
//		if (DONG_TIEN.equals(fileName)) {
//			List<ConstructionDetailDTO> listConstr = constructionDAO
//					.getConstructionForDT(sysGroupId);
//			XSSFSheet sheet = workbook.getSheetAt(0);
//			if (listConstr != null && !listConstr.isEmpty()) {
//				XSSFCellStyle style = ExcelUtils.styleText(sheet);
//				XSSFCellStyle styleNumber = ExcelUtils.styleNumber(sheet);
//				XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
//				int i = 2;
//				int j = 1;
//				for (ConstructionDetailDTO dto : listConstr) {
//					Row row = sheet.createRow(i++);
//					Cell cell = row.createCell(0, CellType.STRING);
//					cell.setCellValue(j++);
//					cell.setCellStyle(style);
//					cell = row.createCell(1, CellType.STRING);
//					cell.setCellValue(StringUtils.isNotEmpty(dto
//							.getProvinceCode()) ? dto.getProvinceCode() : "");
//					cell.setCellStyle(style);
//					cell = row.createCell(2, CellType.STRING);
//					cell.setCellValue(StringUtils.isNotEmpty(dto
//							.getCatStationCode()) ? dto.getCatStationCode()
//							: "");
//					cell.setCellStyle(style);
//					cell = row.createCell(3, CellType.STRING);
//					cell.setCellValue(StringUtils.isNotEmpty(dto.getCode()) ? dto
//							.getCode() : "");
//					cell.setCellStyle(style);
//					cell = row.createCell(4, CellType.STRING);
//					cell.setCellValue(StringUtils.isNotEmpty(dto
//							.getCatContructionTypeName()) ? dto
//							.getCatContructionTypeName() : "");
//					cell.setCellStyle(style);
//					cell = row.createCell(5, CellType.STRING);
//					cell.setCellValue(StringUtils.isNotEmpty(dto
//							.getWorkItemName()) ? dto.getWorkItemName() : "");
//					cell.setCellStyle(style);
//					cell = row.createCell(6, CellType.STRING);
//					cell.setCellValue(StringUtils.isNotEmpty(dto
//							.getCntContractCode()) ? dto.getCntContractCode()
//							: "");
//					cell.setCellStyle(style);
//					cell = row.createCell(7, CellType.NUMERIC);
//					cell.setCellValue((dto.getQuantity() != null ? dto
//							.getQuantity() : 0));
//					cell.setCellStyle(styleCurrency);
//					cell = row.createCell(8, CellType.NUMERIC);
//					cell.setCellStyle(styleCurrency);
//					cell = row.createCell(9, CellType.STRING);
//					cell.setCellStyle(style);
//				}
//			}
//		} else 
//		chinhpxn20180710_end
        if (YEU_CAU_VAT_TU.equals(fileName)) {
            List<DmpnOrderDTO> data = dmpnOrderDAO.getDataForYCVT();
            if (data != null && !data.isEmpty()) {
                XSSFSheet sheet = workbook.getSheetAt(1);
                XSSFCellStyle style = ExcelUtils.styleText(sheet);
                int i = 1;
                for (DmpnOrderDTO dto : data) {
                    Row row = sheet.createRow(i++);
                    Cell cell = row.createCell(0, CellType.STRING);
                    cell.setCellValue(StringUtils.isNotEmpty(dto.getGoodsCode()) ? dto.getGoodsCode() : "");
                    cell.setCellStyle(style);
                    cell = row.createCell(1, CellType.STRING);
                    cell.setCellValue(StringUtils.isNotEmpty(dto.getGoodsName()) ? dto.getGoodsName() : "");
                    cell.setCellStyle(style);
                }
            }
            List<DmpnOrderDTO> dvt = dmpnOrderDAO.getDVTForYCVT();
            if (dvt != null & !dvt.isEmpty()) {

                XSSFSheet sheet = workbook.getSheetAt(2);
                XSSFCellStyle style = ExcelUtils.styleText(sheet);
                int i = 1;
                for (DmpnOrderDTO dto : dvt) {
                    Row row = sheet.createRow(i++);
                    Cell cell = row.createCell(0, CellType.STRING);
                    cell.setCellValue(StringUtils.isNotEmpty(dto.getUnitCode()) ? dto.getUnitCode() : "");
                    cell.setCellStyle(style);
                    cell = row.createCell(1, CellType.STRING);
                    cell.setCellValue(StringUtils.isNotEmpty(dto.getUnitName()) ? dto.getUnitName() : "");
                    cell.setCellStyle(style);
                }

            }

        }
        /*
         * List<WorkItemDTO> listSheet3 = workItemDAO.GetListData(); XSSFSheet sheet1 =
         * workbook.getSheetAt(1); fillSheet3(sheet1, listSheet3);
         * workbook.write(outFile); workbook.close(); outFile.close();
         */
        workbook.write(outFile);
        workbook.close();
        outFile.close();

        String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + fileName);
        return path;
    }

    public List<ConstructionTaskDetailDTO> importDT(String fileInput, String filePath) throws Exception {
        List<ConstructionTaskDetailDTO> workLst = new ArrayList<ConstructionTaskDetailDTO>();
        File f = new File(fileInput);
        ZipSecureFile.setMinInflateRatio(-1.0d);//hoangnh add
        XSSFWorkbook workbook = new XSSFWorkbook(f);
        XSSFSheet sheet = workbook.getSheetAt(0);
        DataFormatter formatter = new DataFormatter();
        DecimalFormat df = new DecimalFormat("#.###");
        DecimalFormat longf = new DecimalFormat("#");
        boolean isExistError = false;
        int count = 0;
        for (Row row : sheet) {
            count++;
            if (count > 2 && !ValidateUtils.checkIfRowIsEmpty(row)) {
                boolean checkColumn3 = true;
                boolean checkColumn6 = true;
                boolean checkColumn7 = true;
                boolean checkColumn8 = true;
                boolean checkColumn5 = true;
                boolean checkColumn9 = true;
                StringBuilder errorMesg = new StringBuilder();
                String constructionCode = "";
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                ConstructionTaskDetailDTO newObj = new ConstructionTaskDetailDTO();
                // chinhpxn20180704_start
                for (int i = 0; i < 10; i++) {
                    // chinhpxn20180704_end
                    Cell cell = row.getCell(i);
                    if (cell != null) {
                        if (cell.getColumnIndex() == 2) {
                            newObj.setCatStationCode(formatter.formatCellValue(cell));
                        } else if (cell.getColumnIndex() == 3) {
                            try {
                                constructionCode = formatter.formatCellValue(row.getCell(3)).trim();
                                ConstructionDetailDTO obj = constructionDAO
                                        .getConstructionByCode(formatter.formatCellValue(cell));
                                newObj.setConstructionCode(obj.getCode());
                                newObj.setConstructionId(obj.getConstructionId());
                                if (obj.getConstructionId() == null)
                                    checkColumn3 = false;
                            } catch (Exception e) {
                                checkColumn3 = false;
                            }
                            if (!checkColumn3) {
                                isExistError = true;
                                errorMesg.append("Mã công trình không hợp lệ!");
                            }

                        } else if (cell.getColumnIndex() == 4) {

                            newObj.setConstructionType(formatter.formatCellValue(cell));
                        } else if (cell.getColumnIndex() == 5) {
                            try {
                                String code = formatter.formatCellValue(cell).trim();
                                WorkItemDetailDTO obj = workItemDAO.getWorkItemByCodeNew(code, constructionCode);
                                if (obj.getConstructionCode().equals(constructionCode)) {
                                    newObj.setWorkItemId(obj.getWorkItemId());
                                    newObj.setWorkItemName(obj.getName());
                                    newObj.setWorkItemCode(obj.getCode());
                                } else {
                                    checkColumn5 = false;
                                    isExistError = true;
                                    errorMesg.append("\nHạng mục không thuộc công trình!");
                                }
                            } catch (Exception e) {
                                checkColumn5 = false;
                            }
                            if (!checkColumn5) {
                                isExistError = true;
                                errorMesg.append("\nChưa nhập hạng hoặc tên hạng mục không tồn tại!");
                            }

                        } else if (cell.getColumnIndex() == 6) {
                            newObj.setCntContract(formatter.formatCellValue(cell));
                        } else if (cell.getColumnIndex() == 7) {
                            try {
                                // chinhpxn 20180606 start
                                newObj.setQuantity(new Double(cell.getNumericCellValue()));
                                // chinhpxn 20180606 end
                            } catch (Exception e) {
                                checkColumn7 = false;
                            }
                            if (!checkColumn7) {
                                isExistError = true;
                                errorMesg.append("Tổng cộng không hợp lệ!");
                            }
                        } else if (cell.getColumnIndex() == 8) {
                            try {
                                // chinhpxn 20180606 start
                                newObj.setVat(new Double(cell.getNumericCellValue()));
                                // chinhpxn 20180606 end
                            } catch (Exception e) {
                                checkColumn8 = false;
                            }
                            if (!checkColumn8) {
                                isExistError = true;
                                errorMesg.append("VAT không hợp lệ!");
                            }
                        } else if (cell.getColumnIndex() == 9) {
                            try {
                                String descriptionHSHC = formatter.formatCellValue(cell).trim();
                                if (descriptionHSHC.length() <= 1000) {
                                    newObj.setDescription(descriptionHSHC);
                                } else {
                                    checkColumn9 = false;
                                    isExistError = true;
                                }

                            } catch (Exception e) {
                                checkColumn9 = false;
                            }
                            if (!checkColumn9) {
                                isExistError = true;
                                errorMesg.append("\nGhi chú vượt quá 1000 ký tự!");
                            }
                            Cell cell1 = row.createCell(10);
                            cell1.setCellValue(errorMesg.toString());
                        } /*
                         * else if (cell.getColumnIndex() == 9) {
                         * newObj.setDescription(formatter.formatCellValue(row .getCell(9)).trim()); }
                         * Cell cell1 = row.createCell(10); cell1.setCellValue(errorMesg.toString());
                         */
                    }
                }
                if (checkColumn3 && checkColumn5 && checkColumn7 && checkColumn8 && checkColumn9) {

                    workLst.add(newObj);
                }

            }
        }
        if (isExistError) {
            ConstructionTaskDetailDTO objErr = new ConstructionTaskDetailDTO();
            OutputStream out = new FileOutputStream(f, true);
            workbook.write(out);
            out.close();
            objErr.setErrorFilePath(UEncrypt.encryptFileUploadPath(filePath));
            workLst.add(objErr);
        }
        workbook.close();
        return workLst;
    }

    public DetailMonthPlanExportDTO getDataSignForSignVOffice(CommonDTO dto) {
        // TODO Auto-generated method stub
        DetailMonthPlanExportDTO data = new DetailMonthPlanExportDTO();
        data.setSummaryList(detailMonthPlanOSDAO.getTmpnTargetForExport(dto.getObjectId(), dto.getMonth(), dto.getYear(),
                dto.getSysGroupId()));
        List<ConstructionTaskDetailDTO> pl1List = detailMonthPlanOSDAO.getPh12ForExportDoc(dto.getObjectId(), "1");
        if (pl1List != null && !pl1List.isEmpty()) {
            int i = 1;
            for (ConstructionTaskDetailDTO pl : pl1List) {
                pl.setTt((i++) + "");
            }
        }
        data.setPl1List(pl1List);
        List<ConstructionTaskDetailDTO> pl2List = detailMonthPlanOSDAO.getPh12ForExportDoc(dto.getObjectId(), "2");
        if (pl2List != null && !pl2List.isEmpty()) {
            int i = 1;
            for (ConstructionTaskDetailDTO pl : pl2List) {
                pl.setTt((i++) + "");
            }
        }
        data.setPl2List(pl2List);
        List<ConstructionTaskDetailDTO> pl3List = detailMonthPlanOSDAO.getPl3ForExportDoc(dto.getObjectId());
        if (pl3List != null && !pl3List.isEmpty()) {
            int i = 1;
            for (ConstructionTaskDetailDTO pl : pl3List) {
                pl.setTt((i++) + "");
            }
        }
        data.setPl3List(pl3List);
        return data;
    }

    public List<DmpnOrderDetailDTO> importYCVT(String fileInput, String filePath) throws Exception {
        List<DmpnOrderDetailDTO> workLst = new ArrayList<DmpnOrderDetailDTO>();
        File f = new File(fileInput);
        ZipSecureFile.setMinInflateRatio(-1.0d);//hoangnh add
        XSSFWorkbook workbook = new XSSFWorkbook(f);
        XSSFSheet sheet = workbook.getSheetAt(0);
        DataFormatter formatter = new DataFormatter();
        DecimalFormat df = new DecimalFormat("#.###");
        DecimalFormat longf = new DecimalFormat("#");
        boolean isExistError = false;
        int count = 0;
        for (Row row : sheet) {
            count++;
            if (count > 2 && !ValidateUtils.checkIfRowIsEmpty(row)) {
                boolean checkColumn1 = true;
                boolean checkColumn2 = true;
                boolean checkColumn3 = true;
                boolean checkColumn4 = true;
                StringBuilder errorMesg = new StringBuilder();
                DmpnOrderDetailDTO newObj = new DmpnOrderDetailDTO();
                for (int i = 0; i < 5; i++) {
                    Cell cell = row.getCell(i);
                    if (cell != null) {
                        if (cell.getColumnIndex() == 1) {
                            try {
                                String code = formatter.formatCellValue(row.getCell(1)).trim();
                                DmpnOrderDTO obj = dmpnOrderDAO
                                        .getOrderByCode(formatter.formatCellValue(row.getCell(1)).trim());
                                newObj.setGoodsId(obj.getGoodsId());
                                newObj.setGoodsCode(obj.getGoodsCode().trim());
                                newObj.setGoodsName(obj.getGoodsName().trim());

                                if (obj.getGoodsId() == null)
                                    checkColumn1 = false;
                            } catch (Exception e) {
                                checkColumn1 = false;
                            }
                            if (!checkColumn1) {
                                isExistError = true;
                                errorMesg.append("Mã hàng hóa không hợp lệ!");
                            }

                        } else if (cell.getColumnIndex() == 2) {
                            try {
                                DmpnOrderDTO obj = dmpnOrderDAO
                                        .getCatUnitByCode(formatter.formatCellValue(cell).trim());
                                newObj.setUnitName(obj.getUnitName());
                                newObj.setCatUnitId(obj.getCatUnitId());
                                if (obj.getCatUnitId() == null)
                                    checkColumn2 = false;
                            } catch (Exception e) {
                                checkColumn2 = false;
                            }
                            if (!checkColumn2) {
                                isExistError = true;
                                errorMesg.append("Đơn vị tính không hợp lệ!");
                            }
                        } else if (cell.getColumnIndex() == 3) {
                            try {
                                newObj.setQuantity(Double.parseDouble(formatter.formatCellValue(cell)));
                            } catch (Exception e) {
                                checkColumn3 = false;
                            }
                            if (!checkColumn3) {
                                isExistError = true;
                                errorMesg.append("Số lượng hợp lệ!");
                            }

                        } else if (cell.getColumnIndex() == 4) {
                            try {
                                String descriptionHSHC = formatter.formatCellValue(cell).trim();
                                if (descriptionHSHC.length() <= 1000) {
                                    newObj.setDescription(descriptionHSHC);
                                } else {
                                    checkColumn4 = false;
                                    isExistError = true;
                                }

                            } catch (Exception e) {
                                checkColumn4 = false;
                            }
                            if (!checkColumn4) {
                                isExistError = true;
                                errorMesg.append("\nGhi chú vượt quá 1000 ký tự!");
                            }
                            Cell cell1 = row.createCell(5);
                            cell1.setCellValue(errorMesg.toString());

                        }
                        Cell cell1 = row.createCell(5);
                        cell1.setCellValue(errorMesg.toString());

                    }
                }
                if (checkColumn1 && checkColumn2 && checkColumn3) {
                    workLst.add(newObj);
                }

            }
        }
        if (isExistError) {
            DmpnOrderDetailDTO objErr = new DmpnOrderDetailDTO();
            OutputStream out = new FileOutputStream(f, true);
            workbook.write(out);
            out.close();
            objErr.setErrorFilePath(UEncrypt.encryptFileUploadPath(filePath));
            workLst.add(objErr);
        }
        workbook.close();
        return workLst;
    }

    public void updateListDmpnOrder(DetailMonthPlanSimpleDTO obj, UserToken objUser) {
        Long detailMonthPlanId = obj.getDetailMonthPlanId();
        if ("1".equalsIgnoreCase(obj.getIsYCVTImport())) {
            dmpnOrderDAO.removeByDetailPlanId(detailMonthPlanId);
            for (DmpnOrderDetailDTO dto : obj.getListDmpnOrder()) {
                dto.setMonth(obj.getMonth());
                dto.setSysGroupId(obj.getSysGroupId());
                dto.setYear(obj.getYear());
                dto.setDetailMonthPlanId(detailMonthPlanId);
                dto.setCreatedDate(new Date());
                dto.setCreatedGroupId(objUser.getDeptId());
                dto.setCreatedUserId(objUser.getUserID());
                dmpnOrderDAO.saveObject(dto.toModel());

            }
        } else {
            if (obj.getListConstrTaskIdDelete() != null && obj.getListConstrTaskIdDelete().size() > 0) {
                dmpnOrderDAO.removeByListId(obj.getListConstrTaskIdDelete());
            }

            // for (DmpnOrderDetailDTO dto : obj.getListDmpnOrder()) {
            // if (dto.getDmpnOrderId() == null) {
            // dto.setMonth(obj.getMonth());
            // dto.setSysGroupId(obj.getSysGroupId());
            // dto.setYear(obj.getYear());
            // dto.setDetailMonthPlanId(detailMonthPlanId);
            // dto.setCreatedDate(new Date());
            // dto.setCreatedGroupId(objUser.getDeptId());
            // dto.setCreatedUserId(objUser.getUserID());
            // dmpnOrderDAO.saveObject(dto.toModel());
            // } else {
            // dto.setMonth(obj.getMonth());
            // dto.setSysGroupId(obj.getSysGroupId());
            // dto.setYear(obj.getYear());
            // dto.setUpdatedDate(new Date());
            // dto.setUpdatedGroupId(objUser.getDeptId());
            // dto.setUpdatedUserId(objUser.getUserID());
            // dmpnOrderDAO.update(dto.toModel());
            // }
            //
            // }
        }
    }

    private void getParentTaskForThiCong(ConstructionTaskDetailDTO dto, String type, Long detailMonthPlanId,
                                         Long sysGroupId,HashMap<String,Long> constructionCodeMap,HashMap<Long,Long> constructionTaskSysGroupMap) {
        // TODO Auto-generated method stub
        ConstructionTaskDetailDTO provinceLevel = new ConstructionTaskDetailDTO();
        ConstructionTaskDetailDTO constructionLevel = new ConstructionTaskDetailDTO();
        if (sysGroupId != null) {
        		Long provinceLevelId=constructionTaskSysGroupMap.get(detailMonthPlanId);
        		if (provinceLevelId == 0L || provinceLevelId == null) {
                provinceLevel.setType(type);
                provinceLevel.setTaskName(dto.getSysGroupName());
                provinceLevel.setLevelId(1L);
                provinceLevel.setSysGroupId(sysGroupId);
                provinceLevel.setDetailMonthPlanId(detailMonthPlanId);
                provinceLevel.setMonth(dto.getMonth());
                provinceLevel.setYear(dto.getYear());
                provinceLevel.setStatus("1");
                provinceLevel.setCompleteState("1");
                provinceLevel.setCreatedDate(new Date());
                provinceLevel.setCreatedGroupId(dto.getCreatedGroupId());
                provinceLevel.setCreatedUserId(dto.getCreatedUserId());
                ConstructionTaskBO bo = provinceLevel.toModel();
                provinceLevelId = constructionTaskDAO.saveObject(bo);
                bo.setPath("/" + provinceLevelId + "/");
                bo.setConstructionTaskId(provinceLevelId);
                bo.setWorkItemType(dto.getWorkItemType());
                bo.setType(type);
                constructionTaskDAO.update(bo);
                constructionTaskDAO.getSession().flush();
                dto.setConstructionTaskId(provinceLevelId);
                constructionTaskSysGroupMap.put(detailMonthPlanId,provinceLevelId);
//                constructionTaskDAO.updateChildRecord(bo, detailMonthPlanId);

            }
            if (StringUtils.isNotEmpty(dto.getConstructionCode())) {
//                constructionLevel = constructionTaskDAO.getConstructionTaskByConstructionCode(dto.getConstructionCode(),
//                        type, 2L, detailMonthPlanId);
//                Long constructionLevelId = constructionLevel.getConstructionTaskId();
            	 Long constructionLevelId=0L;
                 constructionLevelId=constructionCodeMap.get(dto.getConstructionCode());
//                if (constructionLevel.getConstructionTaskId() == null) {
                 if (constructionLevelId == null) {
                    constructionLevel.setType(type);
                    constructionLevel.setTaskName(dto.getConstructionCode());
                    constructionLevel.setLevelId(2L);
                    constructionLevel.setDetailMonthPlanId(detailMonthPlanId);
                    constructionLevel.setParentId(provinceLevelId);
                    constructionLevel.setMonth(dto.getMonth());
                    constructionLevel.setYear(dto.getYear());
                    constructionLevel.setConstructionId(dto.getConstructionId());
                    constructionLevel.setSysGroupId(sysGroupId);
                    constructionLevel.setStatus("1");
                    constructionLevel.setSupervisorId(dto.getSupervisorId());
                    constructionLevel.setDirectorId(dto.getDirectorId());
                    constructionLevel.setCompleteState("1");
                    constructionLevel.setCreatedDate(new Date());
                    constructionLevel.setCreatedGroupId(dto.getCreatedGroupId());
                    constructionLevel.setCreatedUserId(dto.getCreatedUserId());
                    constructionLevel.setWorkItemType(dto.getWorkItemType());
                    ConstructionTaskBO bo = constructionLevel.toModel();
                    constructionLevelId = constructionTaskDAO.saveObject(bo);
                    bo.setConstructionTaskId(constructionLevelId);
                    bo.setPath("/" + provinceLevelId + "/" + constructionLevelId + "/");
                    bo.setWorkItemType(dto.getWorkItemType());
                    bo.setType(type);
                    constructionTaskDAO.updateObject(bo);
                    constructionTaskDAO.getSession().flush();
                    constructionCodeMap.put(dto.getConstructionCode(),constructionLevelId);
                }
                dto.setParentId(constructionLevelId);
                dto.setPath("/" + provinceLevelId + "/" + constructionLevelId + "/");
            }
        }

    }

    private void insertTaskByWorkItem(String type, Long workItemId, ConstructionTaskDetailDTO parent) {
        // TODO Auto-generated method stub
    	if("1".equals(parent.getWorkItemType())){
    		List<ConstructionTaskDTO> list = constructionTaskDAO.getTaskByWorkItem(workItemId);
            if (list != null && !list.isEmpty()) {
                for (ConstructionTaskDTO dto : list) {
                    dto.setLevelId(4L);
                    dto.setMonth(parent.getMonth());
                    dto.setSysGroupId(parent.getSysGroupId());
                    dto.setYear(parent.getYear());
                    dto.setType(type);
                    dto.setWorkItemId(workItemId);
                    dto.setStartDate(parent.getStartDate());
                    dto.setEndDate(parent.getEndDate());
                    dto.setBaselineStartDate(parent.getStartDate());
                    dto.setBaselineEndDate(parent.getEndDate());
                    dto.setPerformerWorkItemId(parent.getPerformerId());
                    dto.setPerformerId(parent.getPerformerId());
                    dto.setDetailMonthPlanId(parent.getDetailMonthPlanId());
                    dto.setParentId(parent.getConstructionTaskId());
                    dto.setCompleteState("1");
                    dto.setStatus("1");
                    dto.setConstructionId(parent.getConstructionId());
                    dto.setTaskName(dto.getTaskName());
                    dto.setCatTaskId(dto.getCatTaskId());
                    dto.setCreatedDate(new Date());
                    dto.setCreatedUserId(parent.getCreatedUserId());
                    dto.setCreatedGroupId(parent.getCreatedGroupId());
                    ConstructionTaskBO bo = dto.toModel();
                    Long id = constructionTaskDAO.saveObject(bo);
                    bo.setPath(parent.getPath() + id + "/");
                    bo.setConstructionTaskId(id);
                    bo.setWorkItemType(parent.getWorkItemType());
                    constructionTaskDAO.updateObject(bo);
                }
            }
    	} else if("2".equals(parent.getWorkItemType())){
    		ConstructionTaskDTO dto = new ConstructionTaskDTO();
    		dto.setLevelId(4L);
            dto.setMonth(parent.getMonth());
            dto.setSysGroupId(parent.getSysGroupId());
            dto.setYear(parent.getYear());
            dto.setType(type);
            dto.setWorkItemId(workItemId);
            dto.setStartDate(parent.getStartDate());
            dto.setEndDate(parent.getEndDate());
            dto.setBaselineStartDate(parent.getStartDate());
            dto.setBaselineEndDate(parent.getEndDate());
            dto.setPerformerWorkItemId(parent.getPerformerId());
            dto.setPerformerId(parent.getPerformerId());
            dto.setDetailMonthPlanId(parent.getDetailMonthPlanId());
            dto.setParentId(parent.getConstructionTaskId());
            dto.setCompleteState("1");
            dto.setStatus("1");
            dto.setConstructionId(parent.getConstructionId());
            dto.setCreatedDate(new Date());
            dto.setCreatedUserId(parent.getCreatedUserId());
            dto.setCreatedGroupId(parent.getCreatedGroupId());
            dto.setTaskName(parent.getTaskName());
            ConstructionTaskBO bo = dto.toModel();
            Long id = constructionTaskDAO.saveObject(bo);
            bo.setPath(parent.getPath() + id + "/");
            bo.setConstructionTaskId(id);
            bo.setWorkItemType(parent.getWorkItemType());
            constructionTaskDAO.updateObject(bo);
    	}
    }
    //tatph - start - 15112019
    private void insertTaskByWorkItemGpon(String type, Long workItemId, ConstructionTaskDetailDTO parent) {
        // TODO Auto-generated method stub
    	if("1".equals(parent.getWorkItemType())){
    		List<ConstructionTaskDTO> list = constructionTaskDAO.getTaskByWorkItemGpon(workItemId);
            if (list != null && !list.isEmpty()) {
                for (ConstructionTaskDTO dto : list) {
                    dto.setLevelId(4L);
                    dto.setMonth(parent.getMonth());
                    dto.setSysGroupId(parent.getSysGroupId());
                    dto.setYear(parent.getYear());
                    dto.setType(type);
                    dto.setWorkItemId(workItemId);
                    dto.setStartDate(parent.getStartDate());
                    dto.setEndDate(parent.getEndDate());
                    dto.setBaselineStartDate(parent.getStartDate());
                    dto.setBaselineEndDate(parent.getEndDate());
                    dto.setPerformerWorkItemId(parent.getPerformerId());
                    dto.setPerformerId(parent.getPerformerId());
                    dto.setDetailMonthPlanId(parent.getDetailMonthPlanId());
                    dto.setParentId(parent.getConstructionTaskId());
                    dto.setCompleteState("1");
                    dto.setStatus("1");
                    dto.setConstructionId(parent.getConstructionId());
                    dto.setTaskName(dto.getTaskName());
                    dto.setCatTaskId(dto.getCatTaskId());
                    dto.setCreatedDate(new Date());
                    dto.setCreatedUserId(parent.getCreatedUserId());
                    dto.setCreatedGroupId(parent.getCreatedGroupId());
                    ConstructionTaskBO bo = dto.toModel();
                    Long id = constructionTaskDAO.saveObject(bo);
                    bo.setPath(parent.getPath() + id + "/");
                    bo.setConstructionTaskId(id);
                    bo.setWorkItemType(parent.getWorkItemType());
                    constructionTaskDAO.updateObject(bo);
                }
            }
    	} else if("2".equals(parent.getWorkItemType())){
    		ConstructionTaskDTO dto = new ConstructionTaskDTO();
    		dto.setLevelId(4L);
            dto.setMonth(parent.getMonth());
            dto.setSysGroupId(parent.getSysGroupId());
            dto.setYear(parent.getYear());
            dto.setType(type);
            dto.setWorkItemId(workItemId);
            dto.setStartDate(parent.getStartDate());
            dto.setEndDate(parent.getEndDate());
            dto.setBaselineStartDate(parent.getStartDate());
            dto.setBaselineEndDate(parent.getEndDate());
            dto.setPerformerWorkItemId(parent.getPerformerId());
            dto.setPerformerId(parent.getPerformerId());
            dto.setDetailMonthPlanId(parent.getDetailMonthPlanId());
            dto.setParentId(parent.getConstructionTaskId());
            dto.setCompleteState("1");
            dto.setStatus("1");
            dto.setConstructionId(parent.getConstructionId());
            dto.setCreatedDate(new Date());
            dto.setCreatedUserId(parent.getCreatedUserId());
            dto.setCreatedGroupId(parent.getCreatedGroupId());
            dto.setTaskName(parent.getTaskName());
            ConstructionTaskBO bo = dto.toModel();
            Long id = constructionTaskDAO.saveObject(bo);
            bo.setPath(parent.getPath() + id + "/");
            bo.setConstructionTaskId(id);
            bo.setWorkItemType(parent.getWorkItemType());
            constructionTaskDAO.updateObject(bo);
    	}
    }
    
    private void insertTaskByWorkItemHTCT(String type, Long workItemId, ConstructionTaskDetailDTO parent) {
        // TODO Auto-generated method stub
    	if("1".equals(parent.getWorkItemType())){
    		List<ConstructionTaskDTO> list = constructionTaskDAO.getTaskByWorkItemHTCT(workItemId);
            if (list != null && !list.isEmpty()) {
                for (ConstructionTaskDTO dto : list) {
                    dto.setLevelId(4L);
                    dto.setMonth(parent.getMonth());
                    dto.setSysGroupId(parent.getSysGroupId());
                    dto.setYear(parent.getYear());
                    dto.setType(type);
                    dto.setWorkItemId(workItemId);
                    dto.setStartDate(parent.getStartDate());
                    dto.setEndDate(parent.getEndDate());
                    dto.setBaselineStartDate(parent.getStartDate());
                    dto.setBaselineEndDate(parent.getEndDate());
                    dto.setPerformerWorkItemId(parent.getPerformerId());
                    dto.setPerformerId(parent.getPerformerId());
                    dto.setDetailMonthPlanId(parent.getDetailMonthPlanId());
                    dto.setParentId(parent.getConstructionTaskId());
                    dto.setCompleteState("1");
                    dto.setStatus("1");
                    dto.setConstructionId(parent.getConstructionId());
                    dto.setTaskName(dto.getTaskName());
                    dto.setCatTaskId(dto.getCatTaskId());
                    dto.setCreatedDate(new Date());
                    dto.setCreatedUserId(parent.getCreatedUserId());
                    dto.setCreatedGroupId(parent.getCreatedGroupId());
                    ConstructionTaskBO bo = dto.toModel();
                    Long id = constructionTaskDAO.saveObject(bo);
                    bo.setPath(parent.getPath() + id + "/");
                    bo.setConstructionTaskId(id);
                    bo.setWorkItemType(parent.getWorkItemType());
                    constructionTaskDAO.updateObject(bo);
                }
            }
    	} else if("2".equals(parent.getWorkItemType())){
    		ConstructionTaskDTO dto = new ConstructionTaskDTO();
    		dto.setLevelId(4L);
            dto.setMonth(parent.getMonth());
            dto.setSysGroupId(parent.getSysGroupId());
            dto.setYear(parent.getYear());
            dto.setType(type);
            dto.setWorkItemId(workItemId);
            dto.setStartDate(parent.getStartDate());
            dto.setEndDate(parent.getEndDate());
            dto.setBaselineStartDate(parent.getStartDate());
            dto.setBaselineEndDate(parent.getEndDate());
            dto.setPerformerWorkItemId(parent.getPerformerId());
            dto.setPerformerId(parent.getPerformerId());
            dto.setDetailMonthPlanId(parent.getDetailMonthPlanId());
            dto.setParentId(parent.getConstructionTaskId());
            dto.setCompleteState("1");
            dto.setStatus("1");
            dto.setConstructionId(parent.getConstructionId());
            dto.setCreatedDate(new Date());
            dto.setCreatedUserId(parent.getCreatedUserId());
            dto.setCreatedGroupId(parent.getCreatedGroupId());
            dto.setTaskName(parent.getTaskName());
            ConstructionTaskBO bo = dto.toModel();
            Long id = constructionTaskDAO.saveObject(bo);
            bo.setPath(parent.getPath() + id + "/");
            bo.setConstructionTaskId(id);
            bo.setWorkItemType(parent.getWorkItemType());
            constructionTaskDAO.updateObject(bo);
    	}
    }
    //tatph - end - 15112019
    private void getParentTask(ConstructionTaskDetailDTO dto, String type, Long detailMonthPlanId, Long sysGroupId,
                               DetailMonthPlanSimpleDTO obj) {
        // TODO Auto-generated method stub
        ConstructionTaskDetailDTO provinceLevel = new ConstructionTaskDetailDTO();
        if (sysGroupId != null) {
            provinceLevel = constructionTaskDAO.getLevel1BySysGroupId(sysGroupId, type, detailMonthPlanId);
            Long provinceLevelId = provinceLevel.getConstructionTaskId();
            if (provinceLevelId == null) {
                constructionTaskDAO.deleteConstructionTask(type, 1L, detailMonthPlanId);
                provinceLevel.setType(type);
                provinceLevel.setTaskName(constructionTaskDAO.getDivisionCodeSysGroupId(sysGroupId));
                provinceLevel.setLevelId(1L);
                provinceLevel.setSysGroupId(sysGroupId);
                provinceLevel.setDetailMonthPlanId(detailMonthPlanId);
                provinceLevel.setMonth(obj.getMonth());
                provinceLevel.setYear(obj.getYear());
                ConstructionTaskBO bo = provinceLevel.toModel();
                provinceLevelId = constructionTaskDAO.saveObject(bo);
                bo.setPath("/" + provinceLevelId + "/");
                bo.setConstructionTaskId(provinceLevelId);
                bo.setType(type);
                constructionTaskDAO.update(bo);
                constructionTaskDAO.getSession().flush();
                constructionTaskDAO.updateChildRecord(bo, detailMonthPlanId);
            }
            dto.setPath("/" + provinceLevelId + "/");
            dto.setConstructionTaskId(provinceLevelId);
        }
    }

    public String exportDetailMonthPlan(DetailMonthPlanSimpleDTO obj, HttpServletRequest request) throws Exception {
    	
        obj.setPage(1L);
        obj.setPageSize(100);
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();
        InputStream file = new BufferedInputStream(new FileInputStream(filePath + "KeHoachThangChiTiet.xlsx"));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        file.close();
        Calendar cal = Calendar.getInstance();
        String uploadPath = folder2Upload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
                + File.separator + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1)
                + File.separator + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload) + File.separator
                + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator
                + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        File udir = new File(uploadPath);
        if (!udir.exists()) {
            udir.mkdirs();
        }
        OutputStream outFile = new FileOutputStream(
                udir.getAbsolutePath() + File.separator + "KeHoachThangChiTiet.xlsx");
        /////////////
        List<DetailMonthPlaningDTO> data = new ArrayList<>();
        String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
                Constant.AdResourceKey.MONTHLY_DETAIL_PLAN, request);
        List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
        if (groupIdList != null && !groupIdList.isEmpty()) {
        	data = detailMonthPlanOSDAO.exportDetailMonthPlan(obj, groupIdList);
        }
//        List<DetailMonthPlaningDTO> data = detailMonthPlanOSDAO.exportDetailMonthPlan(obj);
        ////////////////
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        
        XSSFSheet sheet = workbook.getSheetAt(0);
        //Huypq-20191001-start
        XSSFCellStyle styleDate = ExcelUtils.styleDate(sheet);
		XSSFCreationHelper createHelper = workbook.getCreationHelper();
		styleDate.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
		//Huy-end
        if (data != null && !data.isEmpty()) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet);
            XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
            styleNumber.setAlignment(HorizontalAlignment.RIGHT);
            int i = 2;
            for (DetailMonthPlaningDTO dto : data) {
                Row row = sheet.createRow(i++);
                Cell cell = row.createCell(0, CellType.STRING);
                cell.setCellValue("" + (i - 2));
                cell.setCellStyle(styleNumber);
                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue((dto.getCode() != null) ? dto.getCode() : "");
                cell.setCellStyle(style);
                cell = row.createCell(2, CellType.STRING);
                cell.setCellValue((dto.getName() != null) ? dto.getName() : "");
                cell.setCellStyle(style);
                cell = row.createCell(3, CellType.STRING);
                cell.setCellValue((dto.getSysName() != null) ? dto.getSysName() : "");
                cell.setCellStyle(style);
                cell = row.createCell(4, CellType.STRING);
                cell.setCellValue((dto.getCreatedDate() != null) ? dto.getCreatedDate() : null);
                cell.setCellStyle(styleDate);
                cell = row.createCell(5, CellType.STRING);
                cell.setCellValue(
                        (dto.getYear() != null && dto.getMonth() != null) ? dto.getMonth() + "/" + dto.getYear() : "");
                cell.setCellStyle(styleNumber);
                cell = row.createCell(6, CellType.STRING);
                cell.setCellValue(getStringForSignState(dto.getSignState()));
                cell.setCellStyle(style);
                cell = row.createCell(7, CellType.STRING);
                cell.setCellValue(getStringForStatus(dto.getStatus()));
                cell.setCellStyle(style);

                // thiếu quantity

            }
        }
        ///////////////
//        XSSFSheet sheet1 = workbook.getSheetAt(1);
//        int k = 1;
//        int stt = 1;
//        int count = 1;
//        for (DetailMonthPlaningDTO dto : data) {
//        	ConstructionTaskDetailDTO detailDTO = new ConstructionTaskDetailDTO();
//            detailDTO.setType("1");
//            detailDTO.setDetailMonthPlanId(dto.getDetailMonthPlanId());
//            List<ConstructionTaskDetailDTO> lstTC = constructionTaskDAO.doSearch(detailDTO);
//            if(lstTC!=null && lstTC.size()>0) {
//            	XSSFCellStyle style = ExcelUtils.styleText(sheet);
//                XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
//                styleNumber.setAlignment(HorizontalAlignment.RIGHT);
//                if(k==1) {
//                	sheet1.addMergedRegion(new CellRangeAddress(k, k + lstTC.size() - 1, 0, 0));
//    				sheet1.addMergedRegion(new CellRangeAddress(k, k + lstTC.size() - 1, 1,1));
//                } else {
//                	sheet1.addMergedRegion(new CellRangeAddress(k, k + lstTC.size(), 0, 0));
//    				sheet1.addMergedRegion(new CellRangeAddress(k, k + lstTC.size(), 1,1));
//                }
//                Row row = sheet1.createRow(k);
//                stt++;
//                Cell cell = row.createCell(0, CellType.STRING);
//                cell.setCellValue("" + (stt-1));
//                cell.setCellStyle(styleNumber);
//                cell = row.createCell(1, CellType.STRING);
//                cell.setCellValue(dto.getName()!=null ? dto.getName() : "");
//                cell.setCellStyle(style);
//                if(k==1) {
//                	k=k++ +lstTC.size() - 1 ;
//                } else {
//                	k=k++ +lstTC.size();
//                }
//            	for(ConstructionTaskDetailDTO tc : lstTC) {
//            		Row rowTC = null;
////            		count++;
//            		if(sheet1.getRow(count)==null) {
//            			rowTC = sheet1.createRow(count++);
//            		} else {
//            			rowTC = sheet1.getRow(count++);
//            		}
//            		Cell cellTC = rowTC.createCell(2, CellType.STRING);
//            		cellTC.setCellValue(tc.getWorkItemName()!=null ? tc.getWorkItemName() : "");
//            		cellTC.setCellStyle(style);
//            	}
//            	count++;
//            }
//        }
        ///////////////
        workbook.write(outFile);
        workbook.close();
        outFile.close();

        String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "KeHoachThangChiTiet.xlsx");
        return path;
    }

    private String numberFormat(double value) {
        DecimalFormat myFormatter = new DecimalFormat("###.###");
        String output = myFormatter.format(value);
        return output;
    }

    private String getStringForStatus(String status) {
        // TODO Auto-generated method stub
        if ("0".equals(status)) {
            return "Hết hiệu lực";
        } else if ("1".equals(status)) {
            return "Hiệu lực";
        }
        return null;
    }

    private String getStringForSignState(String signState) {
        // TODO Auto-generated method stub
        if ("1".equals(signState)) {
            return "Chưa trình ký";
        } else if ("2".equals(signState)) {
            return "Đã trình ký";
        } else if ("3".equals(signState)) {
            return "Đã ký duyệt";
        } else if ("4".equals(signState)) {
            return "Từ chối ký duyệt";
        }
        return null;
    }

    private void getParentTaskForCVK(ConstructionTaskDetailDTO dto, String type, Long detailMonthPlanId,
                                     Long sysGroupId) {
        ConstructionTaskDetailDTO provinceLevel = new ConstructionTaskDetailDTO();
        if (sysGroupId != null) {
            provinceLevel = constructionTaskDAO.getLevel1BySysGroupId(sysGroupId, type, detailMonthPlanId);
            Long provinceLevelId = provinceLevel.getConstructionTaskId();
            if (provinceLevel.getConstructionTaskId() == null) {
                constructionTaskDAO.deleteConstructionTask(type, 1L, detailMonthPlanId);
                provinceLevel.setType(type);
                provinceLevel.setSysGroupId(sysGroupId);
                provinceLevel.setTaskName(constructionTaskDAO.getDivisionCodeSysGroupId(sysGroupId));
                provinceLevel.setLevelId(1L);
                provinceLevel.setDetailMonthPlanId(detailMonthPlanId);
                ConstructionTaskBO bo = provinceLevel.toModel();
                provinceLevelId = constructionTaskDAO.saveObject(bo);
                bo.setPath("/" + provinceLevelId + "/");
                bo.setConstructionTaskId(provinceLevelId);
                constructionTaskDAO.update(bo);
                constructionTaskDAO.getSession().flush();
                constructionTaskDAO.updateChildRecord(bo, detailMonthPlanId);

            }
            dto.setParentId(provinceLevelId);
            dto.setConstructionTaskId(provinceLevelId);
            dto.setPath("/" + provinceLevelId + "/");
        }
    }

    public DetailMonthPlanExportDTO getDataAttachForSignVOffice(CommonDTO dto) {
        // TODO Auto-generated method stub
        DetailMonthPlanExportDTO data = new DetailMonthPlanExportDTO();
        data.setPl1ExcelList(detailMonthPlanOSDAO.getPl1ForExportExcel(dto.getObjectId()));
        data.setPl4ExcelList(detailMonthPlanOSDAO.getPl4ExcelList(dto.getObjectId()));
        data.setPl6ExcelList(detailMonthPlanOSDAO.getPl6ForExportExcel(dto.getObjectId()));
        data.setListWorkItemTypeBTS(detailMonthPlanOSDAO.getListWorkItemTypeByName("Công trình BTS"));
        data.setListWorkItemTypeGPON(detailMonthPlanOSDAO.getListWorkItemTypeByName("Công trình GPON"));
        data.setListWorkItemTypeTuyen(detailMonthPlanOSDAO.getListWorkItemTypeByName("Công trình tuyến"));
        data.setListWorkItemTypeLe(detailMonthPlanOSDAO.getListWorkItemTypeByName("Công trình lẻ"));
        List<ConstructionTaskDetailDTO> pl2 = new ArrayList<ConstructionTaskDetailDTO>();
        pl2 = detailMonthPlanOSDAO.getPl235ForExportExcel(dto.getObjectId(), 2L);
        if (pl2 != null && !pl2.isEmpty()) {
            for (ConstructionTaskDetailDTO con : pl2) {
                if (con.getConstructionId() != null) {
                    con.setListWorkItemForExport(
                            detailMonthPlanOSDAO.getWorkItemDetailByConstructionId(con.getConstructionId()));
                }
            }
        }
        data.setPl2ExcelList(pl2);
        List<ConstructionTaskDetailDTO> pl3 = new ArrayList<ConstructionTaskDetailDTO>();
        pl3 = detailMonthPlanOSDAO.getPl235ForExportExcel(dto.getObjectId(), 3L);
        if (pl3 != null && !pl3.isEmpty()) {
            for (ConstructionTaskDetailDTO con : pl3) {
                if (con.getConstructionId() != null) {
                    con.setListWorkItemForExport(
                            detailMonthPlanOSDAO.getWorkItemDetailByConstructionId(con.getConstructionId()));
                }
            }
        }
        data.setPl3ExcelList(pl3);
        List<ConstructionTaskDetailDTO> pl5 = new ArrayList<ConstructionTaskDetailDTO>();
        pl5 = detailMonthPlanOSDAO.getPl235ForExportExcel(dto.getObjectId(), 5L);
        if (pl5 != null && !pl5.isEmpty()) {
            for (ConstructionTaskDetailDTO con : pl5) {
                if (con.getConstructionId() != null) {
                    con.setListWorkItemForExport(
                            detailMonthPlanOSDAO.getWorkItemDetailByConstructionId(con.getConstructionId()));
                }
            }
        }
        data.setPl5ExcelList(pl5);
        return data;
    }
    boolean validateDate(String date){
    	String dateBreaking[] = date.split("/");
    	if(Integer.parseInt(dateBreaking[1])>12){
    		return false;
    	}
    	if( Integer.parseInt(dateBreaking[2]) < (new Date()).getYear()+1900 ){
    		return false;
    	}
    	if(Integer.parseInt(dateBreaking[0])>31){
    		return false;
    	}
    	SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
	    sdfrmt.setLenient(false);
	    try
	    {
	        Date javaDate = sdfrmt.parse(date); 
	    }
	    catch (Exception e)
	    {
	        return false;
	    }
    	return true;
    }
	//tanqn start 20181108
    public void removeRow(ConstructionTaskDTO obj) {
        detailMonthPlanOSDAO.removeRow(obj.getConstructionTaskId());
    }
    //tanqn end 20181108
    public void updateRegistry(DetailMonthPlanSimpleDTO obj) {
    	detailMonthPlanOSDAO.updateRegistry(obj.getDetailMonthPlanId());

    }
    
    //tatph - start - 15112019
    
    //Huypq-20190527-start
    public Long addTaskHTCT(DetailMonthPlanSimpleDTO obj, Long ids, HttpServletRequest request,Long sysGroupIdDetail) {
    KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
    DetailMonthPlanSimpleDTO monthId = detailMonthPlanOSDAO.getDetailMonthPlanIdByTime(obj.getMonth(), obj.getYear(),sysGroupIdDetail);
//    hoanm1_20190926_start
//    obj.setSysGroupId(user.getVpsUserInfo().getSysGroupId());
    obj.setSysGroupId(sysGroupIdDetail);
//    hoanm1_20190926_end
    Long i = 0L;
    if (obj.getListTC() != null && obj.getListTC().size() > 0) {
        HashMap<Long, String> sysGroupMap = new HashMap<Long, String>();
        HashMap<String, Long> checkDupMapWorkItem = new HashMap<String, Long>();
        List<DepartmentDTO> lstGroup = constructionTaskDAO.getListGroup();
        for(DepartmentDTO sys: lstGroup){
        	sysGroupMap.put(sys.getDepartmentId(),sys.getName());
        }
        Long checkGroupLevel1= constructionTaskDAO.getLevel1SysGroupId(obj.getSysGroupId(),"1", monthId.getDetailMonthPlanId());
        HashMap<Long,Long> constructionTaskSysGroupMap = new HashMap<Long,Long>();
        constructionTaskSysGroupMap.put(monthId.getDetailMonthPlanId(), checkGroupLevel1);
        HashMap<String,Long> constructionCodeMap = new HashMap<String,Long>();
        List<ConstructionTaskDetailDTO> lstConstructionCode = constructionTaskDAO.getLevel2ConstructionCode("1",2L,monthId.getDetailMonthPlanId(),obj.getSysGroupId());
        for(ConstructionTaskDetailDTO code: lstConstructionCode){
        	constructionCodeMap.put(code.getConstructionCode(),code.getConstructionTaskId());
        }
        for (ConstructionTaskDetailDTO dto : obj.getListTC()) {
        	String key ="";
        	Long workItemId = null;
        	String workItemName = null;
        	
        	 if(dto.getWorkItemType().equals("2")){
        		List<WorkItemDTO> byId = detailMonthPlanOSDAO.getWiByIdNew(dto.getConstructionCode() + "_KHONGTHUONGXUYEN");
        		WorkItemDTO wDto = new WorkItemDTO();
        		wDto.setConstructionId(dto.getConstructionId());
        		wDto.setName(dto.getWorkItemName());
        		wDto.setIsInternal("1");
        		wDto.setConstructorId(obj.getSysGroupId());
        		wDto.setSupervisorId(obj.getSysGroupId());
        		wDto.setCreatedDate(new Date());
        		wDto.setStatus("1");
        		wDto.setCreatedUserId(obj.getCreatedUserId());
        		wDto.setCreatedGroupId(obj.getCreatedGroupId());
        		if(byId.size() == 0){
        			WorkItemDTO wType = detailMonthPlanOSDAO.getWiType();
            		wDto.setCatWorkItemTypeId(wType.getCatWorkItemTypeId());
            		wDto.setCode(dto.getConstructionCode() + "_KHONGTHUONGXUYEN");
            		workItemName = dto.getWorkItemName();
            		workItemId = workItemDAO.saveObject(wDto.toModel());
        		} else {
        			for(WorkItemDTO work : byId) {
        				if(!work.getName().equals(dto.getWorkItemName())) {
            				WorkItemDTO wType = detailMonthPlanOSDAO.getWiType();
                    		wDto.setCatWorkItemTypeId(wType.getCatWorkItemTypeId());
                    		wDto.setCode(dto.getConstructionCode() + "_KHONGTHUONGXUYEN");
                    		workItemName = dto.getWorkItemName();
                    		workItemId = workItemDAO.saveObject(wDto.toModel());
            			} else {
            				wDto.setCatWorkItemTypeId(work.getCatWorkItemTypeId());
                			wDto.setCode(work.getCode());
                			wDto.setWorkItemId(work.getWorkItemId());
                			workItemId =work.getWorkItemId();
                			workItemName = work.getName();
                			workItemDAO.updateObject(wDto.toModel());
            			}
        			}
        			
        		}
        		
        		key = dto.getConstructionId() + "|" + workItemId + "|" + dto.getPerformerId() + "|" + workItemName;
        	} else if(dto.getWorkItemType().equals("1")){
        		key = dto.getConstructionId() + "|" + dto.getWorkItemId() + "|" + dto.getPerformerId();
        		workItemId = dto.getWorkItemId();
        	}
                if (checkDupMapWorkItem.get(key) == null) {
                	checkDupMapWorkItem.put(key, i++);
                } else {
                    continue;
                }
                dto.setLevelId(3L);
                dto.setMonth(obj.getMonth());
                dto.setSysGroupId(obj.getSysGroupId());
                dto.setYear(obj.getYear());
                dto.setType("1");
                dto.setDetailMonthPlanId(monthId.getDetailMonthPlanId());
                dto.setCreatedUserId(obj.getCreatedUserId());
                dto.setCreatedDate(new Date());
                dto.setCreatedGroupId(obj.getCreatedGroupId());
                dto.setSysGroupName(sysGroupMap.get(obj.getSysGroupId()));
                dto.setPerformerWorkItemId(dto.getPerformerId());
                dto.setTaskName(dto.getWorkItemName());
                dto.setWorkItemId(workItemId);
                dto.setBaselineStartDate(dto.getStartDate());
                dto.setBaselineEndDate(dto.getEndDate());
                dto.setStatus("1");
                dto.setCompleteState("1");
                dto.setPerformerWorkItemId(dto.getPerformerId());
                getParentTaskForThiCong(dto, "1", monthId.getDetailMonthPlanId(), obj.getSysGroupId(),constructionCodeMap,constructionTaskSysGroupMap);
                ConstructionTaskBO bo = dto.toModel();
                Long id = constructionTaskDAO.saveObject(bo);
                detailMonthPlanOSDAO.updatePerforment(dto.getPerformerId(), workItemId);
                constructionTaskDAO.getSession().flush();
                bo.setPath(dto.getPath() + id + "/");
                bo.setWorkItemType(dto.getWorkItemType());
                bo.setType("1");
                dto.setPath(dto.getPath() + id + "/");
                dto.setConstructionTaskId(id);
                constructionTaskDAO.updateObject(bo);
//                hoanm1_20191018_comment
//                constructionTaskBusinessImpl.createSendSmsEmail(dto, user);
//                hoanm1_20191018_comment
                
                
//                if(workItemId != null && dto.getCheckHTCT() == 1 ) {
//                	insertTaskByWorkItemHTCT("1", workItemId, dto);
//                }
//                else if (workItemId != null && dto.getCatConstructionTypeId() !=3L &&  dto.getCheckHTCT() != 1){
//                	insertTaskByWorkItem("1", workItemId, dto);
//                }else if (workItemId != null && dto.getCatConstructionTypeId() == 3L && dto.getCheckHTCT() != 1){
//                	insertTaskByWorkItemGpon("1", workItemId, dto);
//                }
                //tatph-staRT-21112019
                
                 if (workItemId != null && dto.getCatConstructionTypeId() !=3L ){
                	insertTaskByWorkItem("1", workItemId, dto);
                }else if (workItemId != null && dto.getCatConstructionTypeId() == 3L ){
                	insertTaskByWorkItemGpon("1", workItemId, dto);
                }
                //tatph-end-21112019
        }
    }
    if(ids==0l) {
    	return i;
    } else {
    	return 0l;
    }
}
    
    //tatph - end - 15112019
    
    
    
//    //Huypq-20190527-start
//    public Long addTask(DetailMonthPlanSimpleDTO obj, Long ids, HttpServletRequest request,Long sysGroupIdDetail) {
//    KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
//    DetailMonthPlanSimpleDTO monthId = detailMonthPlanOSDAO.getDetailMonthPlanIdByTime(obj.getMonth(), obj.getYear(),sysGroupIdDetail);
////    hoanm1_20190926_start
////    obj.setSysGroupId(user.getVpsUserInfo().getSysGroupId());
//    obj.setSysGroupId(sysGroupIdDetail);
////    hoanm1_20190926_end
//    Long i = 0L;
//    if (obj.getListTC() != null && obj.getListTC().size() > 0) {
//        HashMap<Long, String> sysGroupMap = new HashMap<Long, String>();
//        HashMap<String, Long> checkDupMapWorkItem = new HashMap<String, Long>();
//        List<DepartmentDTO> lstGroup = constructionTaskDAO.getListGroup();
//        for(DepartmentDTO sys: lstGroup){
//        	sysGroupMap.put(sys.getDepartmentId(),sys.getName());
//        }
//        Long checkGroupLevel1= constructionTaskDAO.getLevel1SysGroupId(obj.getSysGroupId(),"1", monthId.getDetailMonthPlanId());
//        HashMap<Long,Long> constructionTaskSysGroupMap = new HashMap<Long,Long>();
//        constructionTaskSysGroupMap.put(monthId.getDetailMonthPlanId(), checkGroupLevel1);
//        HashMap<String,Long> constructionCodeMap = new HashMap<String,Long>();
//        List<ConstructionTaskDetailDTO> lstConstructionCode = constructionTaskDAO.getLevel2ConstructionCode("1",2L,monthId.getDetailMonthPlanId(),obj.getSysGroupId());
//        for(ConstructionTaskDetailDTO code: lstConstructionCode){
//        	constructionCodeMap.put(code.getConstructionCode(),code.getConstructionTaskId());
//        }
//        for (ConstructionTaskDetailDTO dto : obj.getListTC()) {
//        	String key ="";
//        	Long workItemId = null;
//        	
//        	 if(dto.getWorkItemType().equals("2")){
//        		WorkItemDTO byId = detailMonthPlanOSDAO.getWiById(dto.getConstructionCode() + "_KHONGTHUONGXUYEN");
//        		WorkItemDTO wDto = new WorkItemDTO();
//        		wDto.setConstructionId(dto.getConstructionId());
//        		wDto.setName(dto.getWorkItemName());
//        		wDto.setIsInternal("1");
//        		wDto.setConstructorId(obj.getSysGroupId());
//        		wDto.setSupervisorId(obj.getSysGroupId());
//        		wDto.setCreatedDate(new Date());
//        		wDto.setStatus("1");
//        		wDto.setCreatedUserId(obj.getCreatedUserId());
//        		wDto.setCreatedGroupId(obj.getCreatedGroupId());
//        		if(byId == null){
//        			WorkItemDTO wType = detailMonthPlanOSDAO.getWiType();
//            		wDto.setCatWorkItemTypeId(wType.getCatWorkItemTypeId());
//            		wDto.setCode(dto.getConstructionCode() + "_KHONGTHUONGXUYEN");
//            		workItemId = workItemDAO.saveObject(wDto.toModel());
//        		} else {
//        			wDto.setCatWorkItemTypeId(byId.getCatWorkItemTypeId());
//        			wDto.setCode(byId.getCode());
//        			wDto.setWorkItemId(byId.getWorkItemId());
//        			workItemId =byId.getWorkItemId();
//        			workItemDAO.updateObject(wDto.toModel());
//        		}
//        		
//        		key = dto.getConstructionId() + "|" + workItemId + "|" + dto.getPerformerId();
//        	} else if(dto.getWorkItemType().equals("1")){
//        		key = dto.getConstructionId() + "|" + dto.getWorkItemId() + "|" + dto.getPerformerId();
//        		workItemId = dto.getWorkItemId();
//        	}
//                if (checkDupMapWorkItem.get(key) == null) {
//                	checkDupMapWorkItem.put(key, i++);
//                } else {
//                    continue;
//                }
//                dto.setLevelId(3L);
//                dto.setMonth(obj.getMonth());
//                dto.setSysGroupId(obj.getSysGroupId());
//                dto.setYear(obj.getYear());
//                dto.setType("1");
//                dto.setDetailMonthPlanId(monthId.getDetailMonthPlanId());
//                dto.setCreatedUserId(obj.getCreatedUserId());
//                dto.setCreatedDate(new Date());
//                dto.setCreatedGroupId(obj.getCreatedGroupId());
//                dto.setSysGroupName(sysGroupMap.get(obj.getSysGroupId()));
//                dto.setPerformerWorkItemId(dto.getPerformerId());
//                dto.setTaskName(dto.getWorkItemName());
//                dto.setWorkItemId(workItemId);
//                dto.setBaselineStartDate(dto.getStartDate());
//                dto.setBaselineEndDate(dto.getEndDate());
//                dto.setStatus("1");
//                dto.setCompleteState("1");
//                dto.setPerformerWorkItemId(dto.getPerformerId());
//                getParentTaskForThiCong(dto, "1", monthId.getDetailMonthPlanId(), obj.getSysGroupId(),constructionCodeMap,constructionTaskSysGroupMap);
//                ConstructionTaskBO bo = dto.toModel();
//                Long id = constructionTaskDAO.saveObject(bo);
//                detailMonthPlanOSDAO.updatePerforment(dto.getPerformerId(), workItemId);
//                constructionTaskDAO.getSession().flush();
//                bo.setPath(dto.getPath() + id + "/");
//                bo.setWorkItemType(dto.getWorkItemType());
//                bo.setType("1");
//                dto.setPath(dto.getPath() + id + "/");
//                dto.setConstructionTaskId(id);
//                constructionTaskDAO.updateObject(bo);
////                hoanm1_20191018_comment
////                constructionTaskBusinessImpl.createSendSmsEmail(dto, user);
////                hoanm1_20191018_comment
//                
//                
//                if(workItemId != null && dto.getCheckHTCT() == 1 ) {
//                	insertTaskByWorkItemHTCT("1", workItemId, dto);
//                }
//                else if (workItemId != null && dto.getCatConstructionTypeId() !=3L &&  dto.getCheckHTCT() != 1){
//                	insertTaskByWorkItem("1", workItemId, dto);
//                }else if (workItemId != null && dto.getCatConstructionTypeId() == 3L && dto.getCheckHTCT() != 1){
//                	insertTaskByWorkItemGpon("1", workItemId, dto);
//                }
//                
//        }
//    }
//    if(ids==0l) {
//    	return i;
//    } else {
//    	return 0l;
//    }
//}
    //Huypq-end

  //Huypq-20190627-start
    public List<DetailMonthPlanDTO> checkTaskConstruction(Long id) {
    	List<DetailMonthPlanDTO> detail = detailMonthPlanOSDAO.checkTaskConstruction(id);
    	return detail;
    }
    //Huy-end
    
    public KttsUserSession getUser(HttpServletRequest request) {
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
    	return objUser;
    }
    
    //Huypq-20191218-start
    @SuppressWarnings("deprecation")
	private boolean checkIfRowIsEmpty(Row row) {
		if (row == null) {
			return true;
		}
		if (row.getLastCellNum() <= 0) {
			return true;
		}
		for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
			Cell cell = row.getCell(cellNum);
			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && StringUtils.isNotBlank(cell.toString())) {
				return false;
			}
		}
		return true;
	}

	public boolean validateString(String str) {
		return (str != null && str.length() > 0);
	}

	private ExcelErrorDTO createError(int row, String column, String detail) {
		ExcelErrorDTO err = new ExcelErrorDTO();
		err.setColumnError(column);
		err.setLineError(String.valueOf(row));
		err.setDetailError(detail);
		return err;
	}
    
	HashMap<Integer, String> colName = new HashMap();
	{
		colName.put(1, "Hợp đồng");
		colName.put(2, "Số hoá đơn");
		colName.put(3, "Ngày tạo hoá đơn");
		colName.put(4, "Giá trị hoá đơn");
	}

	HashMap<Integer, String> colAlias = new HashMap();
	{
		colAlias.put(1, "A");
		colAlias.put(2, "B");
		colAlias.put(3, "C");
		colAlias.put(4, "D");
		colAlias.put(5, "E");
		colAlias.put(6, "F");
		colAlias.put(7, "G");
	}
	
	HashMap<Integer, String> colNameMV = new HashMap();
	{
		colNameMV.put(1, "Hợp đồng");
		colNameMV.put(2, "Mã công trình");
		colNameMV.put(3, "Số hóa đơn");
		colNameMV.put(4, "Người thực hiện");
		colNameMV.put(5, "Thời gian bắt đầu");
		colNameMV.put(6, "Thời gian hoàn thành thu tiền");
		colNameMV.put(7, "Ghi chú");
	}

	HashMap<Integer, String> colAliasMV = new HashMap();
	{
		colAliasMV.put(1, "A");
		colAliasMV.put(2, "B");
		colAliasMV.put(3, "C");
		colAliasMV.put(4, "D");
		colAliasMV.put(5, "E");
		colAliasMV.put(6, "F");
		colAliasMV.put(7, "G");
	}
	
	public List<RevokeCashMonthPlanDTO> importThuHoiDT(String fileInput, String filePath, HttpServletRequest request)
			throws Exception {
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		List<RevokeCashMonthPlanDTO> workLst = Lists.newArrayList();
		List<ExcelErrorDTO> errorList = new ArrayList<ExcelErrorDTO>();

		File f = new File(fileInput);
		XSSFWorkbook workbook = new XSSFWorkbook(f);
		XSSFSheet sheet = workbook.getSheetAt(0);
		DataFormatter formatter = new DataFormatter();
		int count = 0;

		RevokeCashMonthPlanDTO consWi = new RevokeCashMonthPlanDTO();
		consWi.setPage(null);
		consWi.setPageSize(null);

		List<String> lstContractCode = new ArrayList<>();
		List<String> lstConstructionCode = new ArrayList<>();
		List<String> lstContractConstr = new ArrayList<>();
		int rowCount = 0;
		for (Row row : sheet) {
			rowCount++;
			if (rowCount >= 3 && !ValidateUtils.checkIfRowIsEmpty(row)) {
				String contract = formatter.formatCellValue(row.getCell(1)).trim();
				String constr = formatter.formatCellValue(row.getCell(2)).trim();
				lstContractCode.add(contract.toUpperCase());
				lstConstructionCode.add(constr.toUpperCase());
				lstContractConstr.add(contract.toUpperCase() + "+" + constr.toUpperCase());
			}
		}
		
		HashMap<String, CntContractDTO> mapContract = new HashMap<>();
		if(lstContractCode.size()>0) {
			List<CntContractDTO> lstContract = revokeCashMonthPlanDAO.getContractOutOS(lstContractCode);
			for(CntContractDTO contract : lstContract) {
				mapContract.put(contract.getCode().toUpperCase().trim(), contract);
			}
		}
		
		HashMap<String, ConstructionDTO> mapConstr = new HashMap<>();
		if(lstContractCode.size()>0) {
			List<ConstructionDTO> lstConstr = revokeCashMonthPlanDAO.getConstructionCodeByList(lstConstructionCode);
			for(ConstructionDTO constr : lstConstr) {
				mapConstr.put(constr.getCode().toUpperCase().trim(), constr);
			}
		}
		
		HashMap<String, RevokeCashMonthPlanDTO> mapContractConstr = new HashMap<>();
		if(lstContractConstr.size()>0) {
			List<RevokeCashMonthPlanDTO> lstConConstr = revokeCashMonthPlanDAO.getContractConstr(lstContractConstr);
			for(RevokeCashMonthPlanDTO constr : lstConConstr) {
				mapContractConstr.put(constr.getCntContractCode().toUpperCase().trim() + "+" + constr.getConstructionCode().toUpperCase().trim(), constr);
			}
		}
		
		HashMap<String, String> checkDupBillCode = new HashMap<>();
		HashMap<String, String> checkDupConsCode = new HashMap<>();
		
		RevokeCashMonthPlanDTO sysGroup = revokeCashMonthPlanDAO.getProvinceBySysGroup(user.getVpsUserInfo().getSysGroupId());
		
		for (Row row : sheet) {
			count++;
			if (count >= 3 && checkIfRowIsEmpty(row))
				continue;
			if (count >= 3) {
				String contractCode = formatter.formatCellValue(row.getCell(1)).trim();
				String constructionCode = formatter.formatCellValue(row.getCell(2)).trim();
				String billCode = formatter.formatCellValue(row.getCell(3)).trim();
				String createBillDate = formatter.formatCellValue(row.getCell(4)).trim();
				String billValue = formatter.formatCellValue(row.getCell(5)).trim();

				StringBuilder errorMesg = new StringBuilder();
				RevokeCashMonthPlanDTO obj = new RevokeCashMonthPlanDTO();

				// Hợp đồng
				if (validateString(contractCode)) {
					CntContractDTO dto = mapContract.get(contractCode.toUpperCase());
					if(dto==null) {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(1),
								colName.get(1) + " không tồn tại hoặc không phải hợp đồng đầu ra");
						errorList.add(errorDTO);
						errorMesg.append("Mã hợp đồng không tồn tại hoặc không phải hợp đồng đầu ra!");
					} else {
						obj.setCntContractCode(contractCode);
					}
				} else {
					ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(1),
							colName.get(1) + " không được bỏ trống");
					errorList.add(errorDTO);
					errorMesg.append("Mã hợp đồng không được bỏ trống!");
				}
				
				// Công trình
				if (validateString(constructionCode)) {
					ConstructionDTO dto = mapConstr.get(constructionCode.toUpperCase());
					RevokeCashMonthPlanDTO contr = mapContractConstr.get(contractCode.toUpperCase() + "+" +constructionCode.toUpperCase());
					if(checkDupConsCode.size()==0) {
						if(dto==null) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(2),
									colName.get(2) + " không tồn tại");
							errorList.add(errorDTO);
							errorMesg.append("Mã công trình không tồn tại!");
						} else if(contr==null) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(2),
									colName.get(2) + " chưa gán với hợp đồng đầu ra");
							errorList.add(errorDTO);
							errorMesg.append("Mã công trình chưa gán với hợp đồng đầu ra!");
						} else {
							obj.setConstructionId(dto.getConstructionId());
							obj.setConstructionCode(constructionCode);
							obj.setCatStationId(dto.getCatStationId());
							obj.setCatStationCode(dto.getCatStationCode());
						}
						checkDupConsCode.put(constructionCode, constructionCode);
					} else {
						if (checkDupConsCode.get(constructionCode) != null) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(2),
									colName.get(2) + "  đã tồn tại ở trên (Mã công trình là duy nhất)!");
							errorList.add(errorDTO);
							errorMesg.append("Mã công trình đã tồn tại ở trên (Mã công trình là duy nhất)!");
						} else {
							if (dto == null) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(2),
										colName.get(2) + " không tồn tại");
								errorList.add(errorDTO);
								errorMesg.append("Mã công trình không tồn tại!");
							} else if (contr == null) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(2),
										colName.get(2) + " chưa gán với hợp đồng đầu ra");
								errorList.add(errorDTO);
								errorMesg.append("Mã công trình chưa gán với hợp đồng đầu ra!");
							} else {
								obj.setConstructionId(dto.getConstructionId());
								obj.setConstructionCode(constructionCode);
								obj.setCatStationId(dto.getCatStationId());
								obj.setCatStationCode(dto.getCatStationCode());
							}
							checkDupConsCode.put(constructionCode, constructionCode);
						}
					}
					
				} else {
					ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(1),
							colName.get(1) + " không được bỏ trống");
					errorList.add(errorDTO);
					errorMesg.append("Mã công trình không được bỏ trống!");
				}

				// Số hoá đơn
				if (validateString(billCode)) {
					if(checkDupBillCode.size()==0) {
						obj.setBillCode(billCode);
						if(obj.getCntContractCode()!=null) {
							checkDupBillCode.put(billCode, obj.getCntContractCode());
						}
					} else {
						if(obj.getCntContractCode()!=null && checkDupBillCode.get(billCode)!=null && checkDupBillCode.get(billCode)!=obj.getCntContractCode()) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(3),
									colName.get(3) + " đã được gán với hợp đồng "+ checkDupBillCode.get(billCode));
							errorList.add(errorDTO);
							errorMesg.append("Số hoá đơn đã được gán với hợp đồng "+ checkDupBillCode.get(billCode));
						} else {
							obj.setBillCode(billCode);
							checkDupBillCode.put(billCode, obj.getCntContractCode());
						}
					}
				} else {
					ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(3),
							colName.get(3) + " không được bỏ trống");
					errorList.add(errorDTO);
					errorMesg.append("Số hoá đơn không được bỏ trống!");
				}

				// Ngày hoá đơn
				if (validateString(createBillDate)) {
					if (ValidateUtils.isValidFormat("dd/MM/yyyy", createBillDate) == null) {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(4),
								colAlias.get(4) + " sai định dạng 'dd/MM/yyyy'");
						errorList.add(errorDTO);
						errorMesg.append("Ngày lập hoá đơn sai định dạng 'dd/MM/yyyy'!");
					} else {
						obj.setCreatedBillDate(ValidateUtils.isValidFormat("dd/MM/yyyy", createBillDate));
					}
				} else {
					ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(4),
							colName.get(4) + " không được bỏ trống");
					errorList.add(errorDTO);
					errorMesg.append("Ngày lập hoá đơn không được bỏ trống!");
				}
				// Giá trị hoá đơn
				if (validateString(billValue)) {
					try {
						if (Double.compare(Double.parseDouble(billValue), 0D) < 0) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(5),
									colName.get(5) + " phải lớn hơn 0");
							errorList.add(errorDTO);
							errorMesg.append("Giá trị hoá đơn phải lớn hơn 0!");
						} else {
							if(Double.compare(Double.parseDouble(billValue), 1000000d) >= 0) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(5),
										colName.get(5) + " không hợp lệ ");
								errorList.add(errorDTO);
								errorMesg.append("Giá trị hoá đơn không hợp lệ!");
							} else {
								obj.setBillValue(Double.parseDouble(billValue));
							}
						}
					} catch (Exception e) {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(5),
								colName.get(5) + " chỉ được nhập số");
						errorList.add(errorDTO);
						errorMesg.append("Giá trị hoá đơn chỉ được nhập số!");
					}
				} else {
					ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(5),
							colName.get(5) + " không được bỏ trống");
					errorList.add(errorDTO);
					errorMesg.append("Giá trị hoá đơn không được bỏ trống!");
				}

				if(sysGroup!=null) {
					obj.setAreaCode(sysGroup.getAreaCode());
					obj.setProvinceCode(sysGroup.getProvinceCode());
				}
				
				obj.setStatus(1l);
				obj.setSignState("1");
				
				Cell cell1 = row.createCell(6);
                cell1.setCellValue(errorMesg.toString());
				
				if (errorList.size() == 0) {
					workLst.add(obj);
				}
			}
		}

		if (errorList.size() > 0) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			style.setAlignment(HorizontalAlignment.CENTER);
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			Cell cell = sheet.getRow(1).createCell(6);
			cell.setCellValue("Cột lỗi");
			cell.setCellStyle(style);
			RevokeCashMonthPlanDTO objErr = new RevokeCashMonthPlanDTO();
			OutputStream out = new FileOutputStream(f, true);
			workbook.write(out);
			out.close();
			objErr.setErrorFilePath(UEncrypt.encryptFileUploadPath(filePath));
			workLst.add(objErr);
		}
		workbook.close();
		return workLst;

	}
	
	public void updateListRevokeDT(DetailMonthPlanSimpleDTO obj, KttsUserSession user) {
		revokeCashMonthPlanDAO.deleteRevoke(obj.getDetailMonthPlanId());
		for (RevokeCashMonthPlanDTO dto : obj.getListRevokeDT()) {
			dto.setCreatedUserId(user.getSysUserId());
			dto.setCreatedDate(new Date());
			dto.setDetailMonthPlanId(obj.getDetailMonthPlanId());
			dto.setSysGroupId(obj.getSysGroupId());
			revokeCashMonthPlanDAO.saveObject(dto.toModel());
		}
	}
	
	public DataListDTO getRevokeCashMonthPlanByPlanId(RevokeCashMonthPlanDTO obj){
		DataListDTO data = new DataListDTO();
		if(obj.getDetailMonthPlanId()!=null) {
			List<RevokeCashMonthPlanBO> lst = revokeCashMonthPlanDAO.getRevokeCashMonthPlanByPlanId(obj);
			data.setData(lst);
		} else {
			data.setData(new ArrayList<RevokeCashMonthPlanBO>());
		}
		data.setSize(obj.getTotalRecord());
		data.setTotal(obj.getTotalRecord());
		data.setStart(1);
		return data;
	}
    //Huy-end
	
	//tatph-start-12/12/2019
	public DataListDTO doSearchManageValue(RevokeCashMonthPlanDTO obj , HttpServletRequest request) {
        List<RevokeCashMonthPlanDTO> ls = new ArrayList<RevokeCashMonthPlanDTO>();
        obj.setTotalRecord(0);
        String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
                Constant.AdResourceKey.DATA, request);
        List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
        if (groupIdList != null && !groupIdList.isEmpty())
            ls = detailMonthPlanOSDAO.doSearchManageValue(obj ,groupIdList);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }
	protected static final String USER_SESSION_KEY = "kttsUserSession";
	public KttsUserSession getUserSession(HttpServletRequest request) {
        KttsUserSession s = (KttsUserSession) request.getSession().getAttribute(USER_SESSION_KEY);
        if (s == null) {
            throw new BusinessException("user is not authen");
        }
        return s;

    }
	public List<RevokeCashMonthPlanDTO> getSysUserById(Long code) {
		return revokeCashMonthPlanDAO.getSysUserById(code);
	}
	
	public void updateRevokeCashMonthPlan(RevokeCashMonthPlanDTO obj, HttpServletRequest request) {
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		obj.setUpdatedDate(new Date());
		obj.setUpdatedUserId(user.getVpsUserInfo().getSysUserId());
		obj.setStatus(1l);
		revokeCashMonthPlanDAO.update(obj.toModel());
	}
	
	public void approveRevokeCashMonthPlan(RevokeCashMonthPlanDTO obj) {
		revokeCashMonthPlanDAO.updateManageValueByCommand(obj);
	}
	
	public List<RevokeCashMonthPlanDTO> importManageValue(String fileInput) {
		List<RevokeCashMonthPlanDTO> workLst = Lists.newArrayList();
		List<ExcelErrorDTO> errorList = new ArrayList<ExcelErrorDTO>();
		try {
			File f = new File(fileInput);
			XSSFWorkbook workbook = new XSSFWorkbook(f);
			XSSFSheet sheet = workbook.getSheetAt(0);
			DataFormatter formatter = new DataFormatter();
			int count = 0;
			// hienvd: check exit
			for (Row row : sheet) {
				count++;
				if (count >= 4 && checkIfRowIsEmpty(row))
					continue;
				if (count >= 4) {
					String contractCode = formatter.formatCellValue(row.getCell(0));
					contractCode = contractCode.trim();
					String constructionCode = formatter.formatCellValue(row.getCell(1));
					constructionCode = constructionCode.trim();
					String billCode = formatter.formatCellValue(row.getCell(2));
					billCode = billCode.trim();
					String performer = formatter.formatCellValue(row.getCell(3));
					performer = performer.trim();
					String startDate = formatter.formatCellValue(row.getCell(4));
					startDate = startDate.trim();
					String endDate = formatter.formatCellValue(row.getCell(5));
					endDate = endDate.trim();
					String description = formatter.formatCellValue(row.getCell(6));
					description = description.trim();
					

					RevokeCashMonthPlanDTO obj = new RevokeCashMonthPlanDTO();
					// validate cong trinh
					if (validateString(contractCode)) {
						obj.setCntContractCode(contractCode);
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasMV.get(1),
								colNameMV.get(1) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					//
					if (validateString(constructionCode)) {
						obj.setConstructionCode(constructionCode);
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasMV.get(2),
								colNameMV.get(2) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					//
					if (validateString(billCode)) {
						obj.setBillCode(billCode);
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasMV.get(3),
								colNameMV.get(3) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					//
					if (validateString(performer)) {
						obj.setPerformerId(Long.parseLong(performer));
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasMV.get(4),
								colNameMV.get(4) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					//
					obj.setDescription(!"".equals(description) ? description :"");
					if (validateString(startDate)) {
						Date date=new SimpleDateFormat("dd/MM/yyyy").parse(startDate);  
						obj.setStartDate(date);
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasMV.get(5),
								colNameMV.get(5) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					//
					if (validateString(endDate)) {
						Date date=new SimpleDateFormat("dd/MM/yyyy").parse(endDate);  
						obj.setEndDate(date);
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasMV.get(6),
								colNameMV.get(6) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					
					if (errorList.size() == 0) {
						workLst.add(obj);
					}
				}
			}

			if (errorList.size() > 0) {
				String filePathError = UEncrypt.encryptFileUploadPath(fileInput);
				List<RevokeCashMonthPlanDTO> emptyArray = Lists.newArrayList();
				workLst = emptyArray;
				RevokeCashMonthPlanDTO errorContainer = new RevokeCashMonthPlanDTO();
				errorContainer.setErrorList(errorList);
				errorContainer.setMessageColumn(7); // cột dùng để in ra lỗi
				errorContainer.setFilePathError(filePathError);
				workLst.add(errorContainer);
			}
			workbook.close();
			return workLst;

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			ExcelErrorDTO errorDTO = createError(0, "", e.toString());
			errorList.add(errorDTO);
			String filePathError = null;
			try {
				filePathError = UEncrypt.encryptFileUploadPath(fileInput);
			} catch (Exception ex) {
				LOGGER.error(e.getMessage(), e);
				errorDTO = createError(0, "", ex.toString());
				errorList.add(errorDTO);
			}
			List<RevokeCashMonthPlanDTO> emptyArray = Lists.newArrayList();
			workLst = emptyArray;
			RevokeCashMonthPlanDTO errorContainer = new RevokeCashMonthPlanDTO();
			errorContainer.setErrorList(errorList);
			errorContainer.setMessageColumn(8); // cột dùng để in ra lỗi
			errorContainer.setFilePathError(filePathError);
			workLst.add(errorContainer);
			return workLst;
		}
	}
	
	public String getExcelTemplate(RevokeCashMonthPlanDTO obj,HttpServletRequest request) throws Exception {
		obj.setPage(null);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Bieu_mau_quan_ly_gia_tri_thu_hoi_dong_tien.xlsx"));
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		file.close();
		Calendar cal = Calendar.getInstance();
		String uploadPath = folder2Upload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
				+ File.separator + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1)
				+ File.separator + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
		String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload) + File.separator
				+ cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator
				+ cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
		File udir = new File(uploadPath);
		if (!udir.exists()) {
			udir.mkdirs();
		}
		OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "Bieu_mau_quan_ly_gia_tri_thu_hoi_dong_tien.xlsx");
		
		
		List<RevokeCashMonthPlanDTO> constructionTypeLst = this.doSearchManageValue(obj,request ).getData();
		
		XSSFSheet sheet = workbook.getSheetAt(0);
		if ((constructionTypeLst != null && !constructionTypeLst.isEmpty())) { // huy-edit
  			CellStyle style = workbook.createCellStyle(); // Create new style
  			style.setWrapText(true); // Set wordwrap
  			int i = 4;
  			for (RevokeCashMonthPlanDTO dto : constructionTypeLst) {
  				Row row = sheet.createRow(i++);
  				Cell cell = row.createCell(0, CellType.STRING);
  				cell.setCellValue(dto.getCntContractCode() != null ? dto.getCntContractCode() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(1, CellType.STRING);
  				cell.setCellValue((dto.getConstructionCode() != null) ? dto.getConstructionCode() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(2, CellType.STRING);
  				cell.setCellValue((dto.getBillCode() != null) ? dto.getBillCode() : "");
  				cell.setCellStyle(style);
  			}
  		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "Bieu_mau_quan_ly_gia_tri_thu_hoi_dong_tien.xlsx");
		return path;
	}
	//tatph-end-12/12/2019
	
	//Huypq-20200113-start
	public boolean checkRoleTTHT(HttpServletRequest request) {
		boolean checkRoleTTHT = VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVED,
				Constant.AdResourceKey.COMPLETE_CONSTRUCTION, request);
		return checkRoleTTHT;
	}
	
	public void updateRejectRevokeCash(RevokeCashMonthPlanDTO obj, HttpServletRequest request) {
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		obj.setStatus(3l);
		obj.setUpdatedDate(new Date());
		obj.setUpdatedUserId(user.getVpsUserInfo().getSysUserId());
		revokeCashMonthPlanDAO.update(obj.toModel());
	}
	//Huy-end
	
	//Huypq-20200513-start
	public String exportTemplateRent(HttpServletRequest request) throws Exception {
        String folderParam = defaultSubFolderUpload;
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();
        InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Import_thue_tram_thang_chitiet_ngoaiOS.xlsx"));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        file.close();
        Calendar cal = Calendar.getInstance();
        String uploadPath = folder2Upload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
                + File.separator + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1)
                + File.separator + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload) + File.separator
                + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator
                + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        File udir = new File(uploadPath);
        if (!udir.exists()) {
            udir.mkdirs();
        }
        OutputStream outFile = new FileOutputStream(
                udir.getAbsolutePath() + File.separator + "Import_thue_tram_thang_chitiet_ngoaiOS.xlsx");

        XSSFSheet sheet = workbook.getSheetAt(0);
        String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.CREATE,
                Constant.AdResourceKey.MONTHLY_DETAIL_PLAN, request);
        List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
        List<SysUserCOMSDTO> listUser = sysUserCOMSDAO.usersFillter(groupIdList);
        sheet = workbook.getSheetAt(1);
        fillSheet2(sheet, listUser);
        
        workbook.write(outFile);
        workbook.close();
        outFile.close();

        String path = UEncrypt
                .encryptFileUploadPath(uploadPathReturn + File.separator + "Import_thue_tram_thang_chitiet_ngoaiOS.xlsx");
        return path;
    }
	
	public List<ConstructionTaskDetailDTO> importRentGround(String fileInput, String filePath) throws Exception {
        List<ConstructionTaskDetailDTO> workLst = new ArrayList<ConstructionTaskDetailDTO>();
        File f = new File(fileInput);
        ZipSecureFile.setMinInflateRatio(-1.0d);
        XSSFWorkbook workbook = new XSSFWorkbook(f);
        XSSFSheet sheet = workbook.getSheetAt(0);
        DataFormatter formatter = new DataFormatter();
        DecimalFormat df = new DecimalFormat("#.###");
        DecimalFormat longf = new DecimalFormat("#");
        boolean isExistError = false;
        int counts = 0;
        List<String> lstStation = new ArrayList<>();
        List<String> lstUser = new ArrayList<>();
        for (Row row : sheet) {
        	counts++;
            if (counts > 2 && !ValidateUtils.checkIfRowIsEmpty(row)) {
            	lstStation.add(formatter.formatCellValue(row.getCell(2)).trim().toUpperCase());
            	lstUser.add(formatter.formatCellValue(row.getCell(4)).trim().toUpperCase());
            }
        }
        
        HashMap<String, ConstructionTaskDetailDTO> mapStation = new HashMap<>();
        HashMap<String, String> mapCheckCompleteStatus = new HashMap<>();
        if(lstStation.size()>0) {
        	List<ConstructionTaskDetailDTO> listStation = detailMonthPlanOSDAO.getStationByLstCode(lstStation);
        	for(ConstructionTaskDetailDTO dto : listStation) {
        		mapStation.put(dto.getCatStationCode().toUpperCase(), dto);
        	}
        	
        	List<CatStationDTO> listCheckCompleteStatus = detailMonthPlanOSDAO.getCheckCompleteByListStationCode(lstStation);
        	for(CatStationDTO dto : listCheckCompleteStatus) {
        		mapCheckCompleteStatus.put(dto.getCode().toUpperCase(), dto.getCode());
        	}
        }
        
        HashMap<String, SysUserDTO> mapUserCode = new HashMap<>();
        HashMap<String, SysUserDTO> mapUserEmail = new HashMap<>();
        List<SysUserDTO> listUser = detailMonthPlanOSDAO.getUserByLstCode(lstUser);
        for(SysUserDTO dto : listUser) {
        	mapUserCode.put(dto.getEmployeeCode().toUpperCase(), dto);
        	mapUserEmail.put(dto.getEmail().toUpperCase(), dto);
        }
        
        HashMap<String, String> mapCheckDupInFile = new HashMap<>();
        
        int count = 0;
        for (Row row : sheet) {
            count++;
            if (count > 2 && !ValidateUtils.checkIfRowIsEmpty(row)) {
                StringBuilder errorMesg = new StringBuilder();
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                ConstructionTaskDetailDTO newObj = new ConstructionTaskDetailDTO();
                for (int i = 0; i < 9; i++) {
                    Cell cell = row.getCell(i);
                    if (cell != null) {
                        if (cell.getColumnIndex() == 2) {
                            try {
                               String code = formatter.formatCellValue(row.getCell(2)).trim();
                               if(mapCheckDupInFile.size()==0) {
                            	   ConstructionTaskDetailDTO stationDto = mapStation.get(code.toUpperCase().trim());
                                   if(stationDto!=null) {
                                	   if(mapCheckCompleteStatus.get(code.toUpperCase().trim())!=null) {
                                		   isExistError = true;
                                           errorMesg.append("\nMã trạm đã hoàn thành không thể giao kế hoạch tháng !");
                                	   } else {
                                		   newObj.setCatProvinceCode(stationDto.getCatProvinceCode());
                                    	   newObj.setCatStationId(stationDto.getCatStationId());
                                    	   newObj.setStationCode(stationDto.getCatStationCode());
                                	   }
                                   } else {
                                       isExistError = true;
                                       errorMesg.append("\nMã trạm không tồn tại !");
                                   }
                            	   mapCheckDupInFile.put(code.toUpperCase(), code);
                               } else {
                            	   if(mapCheckDupInFile.get(code.toUpperCase())!=null) {
                            		   isExistError = true;
                                       errorMesg.append("\nMã trạm trong cùng file import không được trùng nhau !");
                            	   } else {
                            		   ConstructionTaskDetailDTO stationDto = mapStation.get(code.toUpperCase().trim());
                                       if(stationDto!=null) {
                                    	   if(mapCheckCompleteStatus.get(code.toUpperCase().trim())!=null) {
                                    		   isExistError = true;
                                               errorMesg.append("\nMã trạm đã hoàn thành không thể giao kế hoạch tháng !");
                                    	   } else {
                                    		   newObj.setCatProvinceCode(stationDto.getCatProvinceCode());
                                        	   newObj.setCatStationId(stationDto.getCatStationId());
                                        	   newObj.setStationCode(stationDto.getCatStationCode());
                                    	   }
                                       } else {
                                           isExistError = true;
                                           errorMesg.append("\nMã trạm không tồn tại !");
                                       }
                                       mapCheckDupInFile.put(code.toUpperCase(), code);
                            	   }
                               }
                            } catch (Exception e) {
                                isExistError = true;
                                errorMesg.append("\nMã trạm không được để trống !");
                            }
                        } 
//                        else if (cell.getColumnIndex() == 3) {
////                            try {
//                            	if(row.getCell(3)!=null) {
////                            		Date startDate = dateFormat.parse(formatter.formatCellValue(row.getCell(3)));
//                                    if(validateDate(formatter.formatCellValue(row.getCell(3)))){
//                                    	newObj.setCompleteDate(dateFormat.parse(formatter.formatCellValue(row.getCell(3))));
//                                    }else{ 
//                                    	isExistError = true;
//                                    	errorMesg.append("\nThời gian hoàn thành không hợp lệ!");
//                                    }        
//                            	}
//                            } catch (Exception e) {
//                            	isExistError = true;
//                                errorMesg.append("\nNgày bắt đầu không hợp lệ!");
//                            }
//                        }
                        else if (cell.getColumnIndex() == 4) {
                            try {
                                String code = formatter.formatCellValue(row.getCell(4)).trim();
                                SysUserDTO sysDto = new SysUserDTO();
                                if(mapUserCode.get(code.toUpperCase())!=null) {
                                	sysDto = mapUserCode.get(code.toUpperCase());
                                } else if(mapUserEmail.get(code.toUpperCase())!=null) {
                                	sysDto = mapUserEmail.get(code.toUpperCase());
                                } else {
                                    isExistError = true;
                                    errorMesg.append("\nNgười thực hiện không tồn tại !");
                                }
                                newObj.setPerformerId(sysDto.getSysUserId());
                                newObj.setPerformerName(sysDto.getFullName());
                             } catch (Exception e) {
                                 isExistError = true;
                                 errorMesg.append("\nNgười thực hiện không được để trống !");
                             }
                         }
                        
                        else if (cell.getColumnIndex() == 5) {
                            try {
                            	if(row.getCell(5)!=null) {
                            		Date startDate = dateFormat.parse(formatter.formatCellValue(row.getCell(5)));
                                    if(validateDate(formatter.formatCellValue(row.getCell(5)))){
                                    	newObj.setStartDate(startDate);
                                    }else{ 
                                    	isExistError = true;
                                    	errorMesg.append("\nThời gian bắt đầu không hợp lệ!");
                                    }        
                            	} else {
                            		isExistError = true;
                            		errorMesg.append("\nThời gian bắt đầu không được để trống!");
                            	}
                            } catch (Exception e) {
                            	isExistError = true;
                                errorMesg.append("\nThời gian bắt đầu không hợp lệ!");
                            }
                        }
                        
                        else if (cell.getColumnIndex() == 6) {
                            try {
                            	if(row.getCell(6)!=null) {
                            		Date startDate = dateFormat.parse(formatter.formatCellValue(row.getCell(6)));
                                    if(validateDate(formatter.formatCellValue(row.getCell(6)))){
                                    	newObj.setEndDate(startDate);
                                    }else{ 
                                    	isExistError = true;
                                    	errorMesg.append("\nThời gian kết thúc không hợp lệ!");
                                    }        
                            	} else {
                            		isExistError = true;
                            		errorMesg.append("\nThời gian kết thúc không được để trống!");
                            	}
                            } catch (Exception e) {
                            	isExistError = true;
                                errorMesg.append("\nThời gian kết thúc không hợp lệ!");
                            }
                        }
                        
                        else if (cell.getColumnIndex() == 7) {
//                            try {
                            	if(formatter.formatCellValue(cell)!=null) {
                            		String descriptionTC = formatter.formatCellValue(cell).trim();
                                    if (descriptionTC.length() <= 1000) {
                                        newObj.setDescription(descriptionTC);
                                    } else {
                                    	errorMesg.append("\nGhi chú vượt quá 1000 ký tự!");
                                        isExistError = true;
                                    }
                            	}
//                            } catch (Exception e) {
//                                checkColumn17 = false;
//                            }
                        }
                        
                        Cell cell1 = row.createCell(8);
                        cell1.setCellValue(errorMesg.toString());
                    } else {
                    	if(i==2) {
                    		isExistError = true;
                            errorMesg.append("\nMã trạm không được để trống !");
                    	} else if(i==4) {
                    		isExistError = true;
                            errorMesg.append("\nNgười thực hiện không được để trống !");
                    	} else if(i==5) {
                    		isExistError = true;
                            errorMesg.append("\nThời gian bắt đầu không được để trống !");
                    	} else if(i==6) {
                    		isExistError = true;
                            errorMesg.append("\nThời gian kết thúc không được để trống !");
                    	}
                    }
                }
                if (!isExistError) {
                    workLst.add(newObj);
                }

            }
        }
        if (isExistError) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            Cell cell = sheet.getRow(1).createCell(8);
            cell.setCellValue("Cột lỗi");
            cell.setCellStyle(style);
            ConstructionTaskDetailDTO objErr = new ConstructionTaskDetailDTO();
            OutputStream out = new FileOutputStream(f, true);
            workbook.write(out);
            out.close();
            objErr.setErrorFilePath(UEncrypt.encryptFileUploadPath(filePath));
            workLst.add(objErr);
        }
        workbook.close();
        return workLst;
    }
	
	public void updateListRentGround(DetailMonthPlanSimpleDTO obj, KttsUserSession user) {
		detailMonthPlanOSDAO.deleteRentGround(obj.getDetailMonthPlanId());
		ConstructionTaskDetailDTO parent = new ConstructionTaskDetailDTO();
        getParentTask(parent, "4", obj.getDetailMonthPlanId(), obj.getSysGroupId(), obj);
		for (ConstructionTaskDetailDTO dto : obj.getListRentGround()) {
			dto.setMonth(obj.getMonth());
            dto.setSysGroupId(obj.getSysGroupId());
            dto.setYear(obj.getYear());
            dto.setType("4");
            dto.setDetailMonthPlanId(obj.getDetailMonthPlanId());
            dto.setLevelId(4L);
            dto.setTaskName("Thuê mặt bằng trạm hạ tầng cho thuê, mã trạm " + dto.getStationCode());
            dto.setParentId(parent.getConstructionTaskId());
            dto.setCreatedUserId(user.getVpsUserInfo().getSysUserId());
            dto.setCreatedDate(new Date());
            dto.setCreatedGroupId(user.getVpsUserInfo().getSysGroupId());
            dto.setBaselineStartDate(dto.getStartDate());
            dto.setBaselineEndDate(dto.getEndDate());
            dto.setStatus("1");
            dto.setCompleteState("1");
            dto.setPerformerWorkItemId(dto.getPerformerId());
            ConstructionTaskBO bo = dto.toModel();
//            bo.setCompleteDate(dto.getCompleteDate());
            dto.setCompletePercent(0d);
            Long id = constructionTaskDAO.saveObject(bo);
            bo.setPath(parent.getPath() + id + "/");
            bo.setType("4");
            constructionTaskDAO.updateObject(bo);
		}
	}
	//Huy-end
	
	//Huypq-29062020-start
	public String exportTemplateTargetTTXD(Long sysGroupId, HttpServletRequest request) throws Exception {
        String folderParam = defaultSubFolderUpload;
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();
        InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Import_chi_tieu_thang_chitietTTXD.xlsx"));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.CREATE,
                Constant.AdResourceKey.MONTHLY_DETAIL_PLAN, request);
        List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
        file.close();
        Calendar cal = Calendar.getInstance();
        String uploadPath = folder2Upload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
                + File.separator + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1)
                + File.separator + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload) + File.separator
                + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator
                + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
        File udir = new File(uploadPath);
        if (!udir.exists()) {
            udir.mkdirs();
        }
        OutputStream outFile = new FileOutputStream(
                udir.getAbsolutePath() + File.separator + "Import_chi_tieu_thang_chitietTTXD.xlsx");

        List<SysUserCOMSDTO> listUser = sysUserCOMSDAO.usersFillter(groupIdList);
        XSSFSheet sheet = workbook.getSheetAt(0);
        sheet = workbook.getSheetAt(1);
        fillSheet2(sheet, listUser);
        workbook.write(outFile);
        workbook.close();
        outFile.close();

        String path = UEncrypt
                .encryptFileUploadPath(uploadPathReturn + File.separator + "Import_chi_tieu_thang_chitietTTXD.xlsx");
        return path;
    }
	
	public List<DetailMonthQuantityDTO> importTargetTTXD(String fileInput, String filePath) throws Exception {
        List<DetailMonthQuantityDTO> workLst = new ArrayList<DetailMonthQuantityDTO>();
        File f = new File(fileInput);
        ZipSecureFile.setMinInflateRatio(-1.0d);
        XSSFWorkbook workbook = new XSSFWorkbook(f);
        XSSFSheet sheet = workbook.getSheetAt(0);
        DataFormatter formatter = new DataFormatter();
        DecimalFormat df = new DecimalFormat("#.###");
        DecimalFormat longf = new DecimalFormat("#");
        boolean isExistError = false;
        
        int counts = 0;
        List<String> listConstractExcel = new ArrayList<>();
        List<String> listSysUserExcel = new ArrayList<>();
        for (Row rows : sheet) {
        	counts++;
            if (counts > 3 && !ValidateUtils.checkIfRowIsEmpty(rows)) {
            	listConstractExcel.add(formatter.formatCellValue(rows.getCell(1)).trim().toUpperCase());
            	listSysUserExcel.add(formatter.formatCellValue(rows.getCell(5)).trim().toUpperCase());
            	listSysUserExcel.add(formatter.formatCellValue(rows.getCell(6)).trim().toUpperCase());
            }
        }
        
        HashMap<String, CntContractDTO> mapContractCode = new HashMap<>();
        if(listConstractExcel.size()>0) {
        	List<CntContractDTO> lstContract= detailMonthPlanOSDAO.getCntContractIdByLstContractCode(listConstractExcel);
            for(CntContractDTO dto : lstContract) {
            	mapContractCode.put(dto.getCode().trim().toUpperCase(), dto);
            }
        }
        
        HashMap<String, DepartmentDTO> mapEmployeeCode = new HashMap<>();
        HashMap<String, DepartmentDTO> mapEmail = new HashMap<>();
        if(listSysUserExcel.size()>0) {
        	List<DepartmentDTO> lstUser = detailMonthPlanOSDAO.getSysUserByLstUser(listSysUserExcel);
            for(DepartmentDTO dto : lstUser) {
            	mapEmployeeCode.put(dto.getEmployeeCode().toUpperCase().trim(), dto);
            	mapEmail.put(dto.getEmail().toUpperCase().trim(), dto);
            }
        }
	
        int count = 0;
        for (Row row : sheet) {
            count++;
            if (count > 3 && !ValidateUtils.checkIfRowIsEmpty(row)) {
                StringBuilder errorMesg = new StringBuilder();
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                DetailMonthQuantityDTO newObj = new DetailMonthQuantityDTO();
                for (int i = 0; i < 8; i++) {
                    Cell cell = row.getCell(i);
                    if (cell != null) {
                        if (cell.getColumnIndex() == 1) {
                            try {
                                String contractCode = formatter.formatCellValue(row.getCell(1)).trim();
                                if(mapContractCode.get(contractCode.toUpperCase())!=null) {
                                	newObj.setCntContractId(mapContractCode.get(contractCode.toUpperCase()).getCntContractId());
                                	newObj.setCntContractCode(contractCode);
                                } else {
                                	isExistError = true;
                                    errorMesg.append("\nHợp đồng không tồn tại hoặc không phải hợp đồng xí nghiệp xây dựng");
                                }
                            } catch (Exception e) {
                                isExistError = true;
                                errorMesg.append("\nChưa nhập hợp đồng");
                            }

                        } else if (cell.getColumnIndex() == 2) {
                            try {
                            	Double quantity = new Double(formatter.formatCellValue(row.getCell(2)).trim());
                            	newObj.setQuantity(quantity);
                            	newObj.setQuantityTarget(quantity);
                            } catch (Exception e) {
                            	isExistError = true;
                                errorMesg.append("\nGiá trị chỉ tiêu sản lượng không hợp lệ!");
                            }
                        } else if (cell.getColumnIndex() == 3) {
                            try {
                                Double revenue = new Double(formatter.formatCellValue(row.getCell(3)).trim());
                                newObj.setRevenue(revenue);
                                newObj.setRevenueTarget(revenue);
                            } catch (Exception e) {
                                isExistError = true;
                                errorMesg.append("\nGiá trị chỉ tiêu doanh thu không hợp lệ!");
                            }
                        } 
                        else if (cell.getColumnIndex() == 4) {
                            try {
                            	String otherTarget = formatter.formatCellValue(row.getCell(4));
                            	if(otherTarget!=null) {
                            		newObj.setOtherTarget(otherTarget.trim());
                            	} else {
                            		isExistError = true;
                                    errorMesg.append("\nYêu cầu khác không được để trống!");
                            	}
                            } catch (Exception e) {
                            	isExistError = true;
                                errorMesg.append("\nYêu cầu khác không được để trống!");
                            }
                        } else if (cell.getColumnIndex() == 5) {
                            try {
                                String name = formatter.formatCellValue(cell);
                                if(StringUtils.isNotBlank(name)) {
                                	DepartmentDTO objCode = mapEmployeeCode.get(name.toUpperCase().trim());
                                	DepartmentDTO objEmail = mapEmail.get(name.toUpperCase().trim());
                                	if(objCode!=null) {
                                		newObj.setPerformerId(objCode.getDepartmentId());
                                		newObj.setPerformerName(objCode.getName());
                                	} else if(objEmail!=null) {
                                		newObj.setPerformerId(objEmail.getDepartmentId());
                                		newObj.setPerformerName(objEmail.getName());
                                	} else {
                                        isExistError = true;
                                        errorMesg.append("\nNgười thực hiện không tồn tại !");
                                	}
                                } else {
                                    isExistError = true;
                                    errorMesg.append("\nChưa nhập người thực hiện !");
                                }
                            } catch (Exception e) {
                                isExistError = true;
                                errorMesg.append("\nNgười thực hiện không hợp lệ !");
                            }

                        } else if (cell.getColumnIndex() == 6) {
                        	try {
                                String name = formatter.formatCellValue(cell);
                                if(StringUtils.isNotBlank(name)) {
                                	DepartmentDTO objCode = mapEmployeeCode.get(name.toUpperCase().trim());
                                	DepartmentDTO objEmail = mapEmail.get(name.toUpperCase().trim());
                                	if(objCode!=null) {
                                		newObj.setSupervisorId(objCode.getDepartmentId().toString());
                                		newObj.setSupervisorName(objCode.getName());
                                	} else if(objEmail!=null) {
                                		newObj.setSupervisorId(objEmail.getDepartmentId().toString());
                                		newObj.setSupervisorName(objEmail.getName());
                                	} else {
                                        isExistError = true;
                                        errorMesg.append("\nNgười điều hành không tồn tại !");
                                	}
                                } else {
                                    isExistError = true;
                                    errorMesg.append("\nChưa nhập người điều hành !");
                                }
                            } catch (Exception e) {
                                isExistError = true;
                                errorMesg.append("\nNgười điều hành không hợp lệ !");
                            }
                        } else if (cell.getColumnIndex() == 7) {
                            try {                                
                                Date endDate = dateFormat.parse(formatter.formatCellValue(cell));
                                if(validateDate(formatter.formatCellValue(cell))) {
                                	newObj.setEndDate(endDate);
                                } else {
                                	isExistError = true;
                                    errorMesg.append("\nThời gian hoàn thành HSHC không hợp lệ!");
                                }
                            } catch (Exception e) {
                            	isExistError = true;
                                errorMesg.append("\nThời gian hoàn thành HSHC không hợp lệ!");
                            }
                        }
                        
                        Cell cell1 = row.createCell(8);
                        cell1.setCellValue(errorMesg.toString());
                    }
                }
                if (!isExistError) {
                    workLst.add(newObj);
                }

            }
        }
        if (isExistError) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            Cell cell = sheet.getRow(1).createCell(8);
            cell.setCellValue("Cột lỗi");
            cell.setCellStyle(style);
            DetailMonthQuantityDTO objErr = new DetailMonthQuantityDTO();
            OutputStream out = new FileOutputStream(f, true);
            workbook.write(out);
            out.close();
            objErr.setErrorFilePath(UEncrypt.encryptFileUploadPath(filePath));
            workLst.add(objErr);
        }
        workbook.close();
        return workLst;
    }
	
	public void checkMonthYearSysTTXD(Long month, Long year, Long sysGroupId, Long detailMonthId) {
        boolean isExist = detailMonthPlanOSDAO.checkMonthYearSysTTXD(month, year, sysGroupId, detailMonthId);
        if (isExist)
            throw new IllegalArgumentException("Kế hoạch chi tiết của đơn vị đã tồn tại!");
    }
	
	public static Date asDate(LocalDate localDate) {
	    return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	  }
	
	public Long addTTXD(DetailMonthPlanSimpleDTO obj, HttpServletRequest request) {
        KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        checkMonthYearSysTTXD(obj.getMonth(), obj.getYear(), obj.getSysGroupId(), null);
        obj.setType("2");
        Long detailMonthPlanId = detailMonthPlanOSDAO.saveObject(obj.toModel());

        Long id = 0L;
        
        ConstructionTaskDetailDTO parent = new ConstructionTaskDetailDTO();
        getParentTask(parent, "8", detailMonthPlanId, obj.getSysGroupId(), obj);
        
        for(DetailMonthQuantityDTO monthDto : obj.getLstMonthQuantity()) {
        	DetailMonthQuantityDTO monthQuantityDto = new DetailMonthQuantityDTO();
            monthQuantityDto.setDetailMonthPlanId(detailMonthPlanId);
            monthQuantityDto.setCntContractId(monthDto.getCntContractId());
            monthQuantityDto.setCntContractCode(monthDto.getCntContractCode());
            monthQuantityDto.setQuantity(monthDto.getQuantityTarget()*1000000);
            monthQuantityDto.setRevenue(monthDto.getRevenueTarget()*1000000);
            monthQuantityDto.setOtherTarget(monthDto.getOtherTarget());
            Long quantityId = detailMonthQuantityDAO.saveObject(monthQuantityDto.toModel());
            
            try {
            	String[] lstContent = monthDto.getOtherTarget().split(";");
            	for(String other : lstContent) {
            		ConstructionTaskDetailDTO dto = new ConstructionTaskDetailDTO();
                    dto.setMonth(obj.getMonth());
                    dto.setSysGroupId(obj.getSysGroupId());
                    dto.setYear(obj.getYear());
                    dto.setType("8");
                    dto.setDetailMonthPlanId(detailMonthPlanId);
                    dto.setLevelId(4L);
                    dto.setTaskName(other + " của mã hợp đồng " + monthDto.getCntContractCode());
                    dto.setParentId(parent.getConstructionTaskId());
                    dto.setCreatedUserId(user.getVpsUserInfo().getSysUserId());
                    dto.setCreatedGroupId(user.getVpsUserInfo().getDepartmentId());
                    dto.setCreatedDate(new Date());
                    dto.setCreatedGroupId(obj.getCreatedGroupId());
                    
                    YearMonth yearMonth = YearMonth.of(Integer.parseInt(obj.getYear().toString()), Integer.parseInt(obj.getMonth().toString()));
                    LocalDate firstOfMonth = yearMonth.atDay( 1 );
                    
                    dto.setStartDate(asDate(firstOfMonth));
                    dto.setEndDate(monthDto.getEndDate());
                    dto.setBaselineStartDate(dto.getStartDate());
                    dto.setBaselineEndDate(dto.getEndDate());
                    dto.setStatus("1");
                    dto.setCompleteState("1");
                    dto.setCompletePercent(0d);
                    dto.setPerformerId(monthDto.getPerformerId());
                    
                    dto.setSupervisorId(Double.parseDouble(monthDto.getSupervisorId()));
                    dto.setImportComplete(1l);
                    dto.setDetailMonthQuantityId(quantityId);
                    ConstructionTaskBO bo = dto.toModel();
                    Long consTaskId = constructionTaskDAO.saveObject(bo);
                    bo.setPath(parent.getPath() + consTaskId + "/");
                    bo.setType("8");
                    constructionTaskDAO.updateObject(bo);
            	}
			} catch (Exception e) {
				// TODO: handle exception
			}
        }

        return detailMonthPlanId;
    }
	
	public Long updateMonthTTXD(DetailMonthPlanSimpleDTO obj, HttpServletRequest request) {
    	KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        if (!VpsPermissionChecker.hasPermission(Constant.OperationKey.CREATE,
                Constant.AdResourceKey.MONTHLY_DETAIL_PLAN, request)) {
            throw new IllegalArgumentException("Bạn không có quyền chỉnh sửa kế hoạch tháng chi tiết");
        }
        if (!VpsPermissionChecker.checkPermissionOnDomainData(Constant.OperationKey.CREATE,
                Constant.AdResourceKey.MONTHLY_DETAIL_PLAN, obj.getSysGroupId(), request)) {
            throw new IllegalArgumentException("Bạn không có quyền chỉnh sửa kế hoạch tháng chi tiết cho đơn vị này");
        }
        obj.setType("2");
        detailMonthPlanOSDAO.updateObject(obj.toModel());
        
        if(obj.getLstMonthQuantity()!=null && obj.getLstMonthQuantity().size()>0) {
        	updateListQuantityTTXD(obj, user);
        }
        return obj.getDetailMonthPlanId();
    }
	
	public void updateListQuantityTTXD(DetailMonthPlanSimpleDTO obj, KttsUserSession user) {
		detailMonthPlanOSDAO.deleteConstructionTaskByType(obj.getDetailMonthPlanId(), "8");
		detailMonthPlanOSDAO.deleteDetailMonthQuantityByDetailMonthPlanId(obj.getDetailMonthPlanId());
		ConstructionTaskDetailDTO parent = new ConstructionTaskDetailDTO();
        getParentTask(parent, "8", obj.getDetailMonthPlanId(), obj.getSysGroupId(), obj);
		for (DetailMonthQuantityDTO monthDto : obj.getLstMonthQuantity()) {
			DetailMonthQuantityDTO monthQuantityDto = new DetailMonthQuantityDTO();
            monthQuantityDto.setDetailMonthPlanId(obj.getDetailMonthPlanId());
            monthQuantityDto.setCntContractId(monthDto.getCntContractId());
            monthQuantityDto.setCntContractCode(monthDto.getCntContractCode());
            monthQuantityDto.setQuantity(monthDto.getQuantityTarget()*1000000);
            monthQuantityDto.setRevenue(monthDto.getRevenueTarget()*1000000);
            monthQuantityDto.setOtherTarget(monthDto.getOtherTarget());
            Long quantityId = detailMonthQuantityDAO.saveObject(monthQuantityDto.toModel());
            
            try {
            	String[] lstContent = monthDto.getOtherTarget().split(";");
            	for(String other : lstContent) {
            		ConstructionTaskDetailDTO dto = new ConstructionTaskDetailDTO();
                    dto.setMonth(obj.getMonth());
                    dto.setSysGroupId(obj.getSysGroupId());
                    dto.setYear(obj.getYear());
                    dto.setType("8");
                    dto.setDetailMonthPlanId(obj.getDetailMonthPlanId());
                    dto.setLevelId(4L);
                    dto.setTaskName(other + " của mã hợp đồng " + monthDto.getCntContractCode());
                    dto.setParentId(parent.getConstructionTaskId());
                    dto.setCreatedUserId(user.getVpsUserInfo().getSysUserId());
                    dto.setCreatedGroupId(user.getVpsUserInfo().getDepartmentId());
                    dto.setCreatedDate(new Date());
                    dto.setCreatedGroupId(obj.getCreatedGroupId());
                    
                    YearMonth yearMonth = YearMonth.of(Integer.parseInt(obj.getYear().toString()), Integer.parseInt(obj.getMonth().toString()));
                    LocalDate firstOfMonth = yearMonth.atDay( 1 );
                    
                    dto.setStartDate(asDate(firstOfMonth));
                    dto.setEndDate(monthDto.getEndDate());
                    dto.setBaselineStartDate(dto.getStartDate());
                    dto.setBaselineEndDate(dto.getEndDate());
                    dto.setStatus("1");
                    dto.setCompleteState("1");
                    dto.setCompletePercent(0d);
                    dto.setPerformerId(monthDto.getPerformerId());
                    
                    dto.setSupervisorId(Double.parseDouble(monthDto.getSupervisorId()));
                    dto.setImportComplete(1l);
                    dto.setDetailMonthQuantityId(quantityId);
                    ConstructionTaskBO bo = dto.toModel();
                    Long consTaskId = constructionTaskDAO.saveObject(bo);
                    bo.setPath(parent.getPath() + consTaskId + "/");
                    bo.setType("8");
                    constructionTaskDAO.updateObject(bo);
            	}
            } catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	public DataListDTO doSearchResultMonthQuantityTTXD(ConstructionTaskDetailDTO obj) {
        List<ConstructionTaskDetailDTO> ls = new ArrayList<ConstructionTaskDetailDTO>();
        ls = constructionTaskDAO.doSearchResultMonthQuantityTTXD(obj);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }
	
	public List<UtilAttachDocumentDTO> getListAttachmentByIdAndType(Long objId) throws Exception{
		List<Long> idList = new ArrayList<>(); 
		idList.add(objId);
		List<String> types = new ArrayList<>(); 
		types.add("CVTTXD");
		List<UtilAttachDocumentDTO> listImage = utilAttachDocumentDAO.getListAttachmentByIdAndType(idList, types);
		
		for(UtilAttachDocumentDTO dto : listImage) {
			dto.setFilePath(UEncrypt.encryptFileUploadPath(dto.getFilePath()));
		}
		if (listImage != null && !listImage.isEmpty()) {
			for (UtilAttachDocumentDTO dto : listImage) {
				dto.setBase64String(ImageUtil
						.convertImageToBase64(folder2Upload + UEncrypt.decryptFileUploadPath(dto.getFilePath())));
			}
		}
		
		return listImage;
	}
	
	public String exportResultQuantityTTXD(ConstructionTaskDetailDTO obj) throws Exception {
		obj.setPage(null);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Export_KetQua_KHThang_TTXD.xlsx"));
		List<ConstructionTaskDetailDTO> data = null;
		data = constructionTaskDAO.doSearchResultMonthQuantityTTXD(obj);

		XSSFWorkbook workbook = new XSSFWorkbook(file);
		file.close();
		Calendar cal = Calendar.getInstance();
		String uploadPath = folder2Upload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
				+ File.separator + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1)
				+ File.separator + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
		String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload) + File.separator
				+ cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator
				+ cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
		File udir = new File(uploadPath);
		if (!udir.exists()) {
			udir.mkdirs();
		}
		OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "Export_KetQua_KHThang_TTXD.xlsx");
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
			styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0.000"));
			XSSFCellStyle styleDate = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleDate.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			int i = 2;
			for (ConstructionTaskDetailDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 2));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getCntContractCode() != null) ? dto.getCntContractCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getQuantityTarget() != null) ? dto.getQuantityTarget() : 0d);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getRevenueTarget() != null) ? dto.getRevenueTarget() : 0d);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getOtherTarget() != null) ? dto.getOtherTarget() : "");
				cell.setCellStyle(style);
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getPerformerName() != null) ? dto.getPerformerName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getSupervisorName() != null) ? dto.getSupervisorName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(7, CellType.STRING);
				if(dto.getDetailMonthQuantityType()!=null && dto.getDetailMonthQuantityType().equals("1")) {
					cell.setCellValue(dto.getQuantityXNXD()!=null ? dto.getQuantityXNXD() : "");
				} else {
					cell.setCellValue("");
				}
				cell.setCellStyle(style);
				cell = row.createCell(8, CellType.STRING);
				if(dto.getDetailMonthQuantityType()!=null && dto.getDetailMonthQuantityType().equals("2")) {
					cell.setCellValue(dto.getQuantityXNXD()!=null ? dto.getQuantityXNXD() : "");
				} else {
					cell.setCellValue("");
				}
				cell.setCellStyle(style);
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue((dto.getDescription() != null) ? dto.getDescription() : "");
				cell.setCellStyle(style);
			}
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "Export_KetQua_KHThang_TTXD.xlsx");
		return path;
	}
	
	public DataListDTO doSearchStaffByPopup(DetailMonthQuantityDTO obj) {
    	List<DetailMonthQuantityDTO> ls = new ArrayList<DetailMonthQuantityDTO>();
		ls = detailMonthQuantityDAO.doSearchStaffByPopup(obj);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }
	
	public DataListDTO doSearchContractByPopup(DetailMonthQuantityDTO obj) {
    	List<DetailMonthQuantityDTO> ls = new ArrayList<DetailMonthQuantityDTO>();
		ls = detailMonthQuantityDAO.doSearchContractByPopup(obj);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }
	
	public String exportGiaoChiTietTTXD(ConstructionTaskDetailDTO obj) throws Exception {
		obj.setPage(null);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Export_GiaoChiTieu_TTXD.xlsx"));
		List<ConstructionTaskDetailDTO> data = null;
		data = constructionTaskDAO.doSearchTTXD(obj);

		XSSFWorkbook workbook = new XSSFWorkbook(file);
		file.close();
		Calendar cal = Calendar.getInstance();
		String uploadPath = folder2Upload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
				+ File.separator + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1)
				+ File.separator + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
		String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload) + File.separator
				+ cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator
				+ cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
		File udir = new File(uploadPath);
		if (!udir.exists()) {
			udir.mkdirs();
		}
		OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "Export_GiaoChiTieu_TTXD.xlsx");
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
			styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0.000"));
			XSSFCellStyle styleDate = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleDate.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			int i = 2;
			for (ConstructionTaskDetailDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 2));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getCntContractCode() != null) ? dto.getCntContractCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getQuantityTarget() != null) ? dto.getQuantityTarget() : 0d);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getRevenueTarget() != null) ? dto.getRevenueTarget() : 0d);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getOtherTarget() != null) ? dto.getOtherTarget() : "");
				cell.setCellStyle(style);
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getPerformerName() != null) ? dto.getPerformerName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getSupervisorName() != null) ? dto.getSupervisorName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue((dto.getEndDate() != null) ? dto.getEndDate() : null);
				cell.setCellStyle(styleDate);
			}
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "Export_GiaoChiTieu_TTXD.xlsx");
		return path;
	}
	//Huy-end
}
