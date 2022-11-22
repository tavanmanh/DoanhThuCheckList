package com.viettel.coms.business;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.SQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import com.viettel.cat.dto.ConstructionImageInfo;
import com.fasterxml.jackson.databind.ObjectWriter.GeneratorSettings;
import com.viettel.coms.bo.AssignHandoverBO;
import com.viettel.coms.bo.SendSmsEmailBO;
import com.viettel.coms.dao.AssignHandoverDAO;
import com.viettel.coms.dao.ConstructionDAO;
import com.viettel.coms.dao.ConstructionTaskDAO;
import com.viettel.coms.dao.ObstructedDAO;
import com.viettel.coms.dao.SendSmsEmailDAO;
import com.viettel.coms.dao.UtilAttachDocumentDAO;
import com.viettel.coms.dao.WorkItemDAO;
import com.viettel.coms.dto.AssignHandoverDTO;
import com.viettel.coms.dto.CatWorkItemTypeDTO;
import com.viettel.coms.dto.ConstructionDTO;
import com.viettel.coms.dto.ConstructionDetailDTO;
import com.viettel.coms.dto.ConstructionTaskDTO;
import com.viettel.coms.dto.ConstructionTaskDetailDTO;
import com.viettel.coms.dto.ExcelErrorDTO;
import com.viettel.coms.dto.GoodsPlanDTO;
import com.viettel.coms.dto.ObstructedDTO;
import com.viettel.coms.dto.RequestGoodsDetailDTO;
import com.viettel.coms.dto.SysUserCOMSDTO;
import com.viettel.coms.dto.UtilAttachDocumentDTO;
import com.viettel.coms.dto.WorkItemDTO;
import com.viettel.coms.dto.WorkItemDetailDTO;
import com.viettel.coms.utils.ExcelUtils;
import com.viettel.erp.dto.CntContractDTO;
import com.viettel.erp.dto.SysUserDTO;
import com.viettel.ktts.vps.VpsPermissionChecker;
import com.viettel.ktts.vps.VpsUserToken;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.utils.Constant;
import com.viettel.utils.ConvertData;
import com.viettel.utils.ImageUtil;
//VietNT_20181210_created
@Service("assignHandoverBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AssignHandoverBusinessImpl extends BaseFWBusinessImpl<AssignHandoverDAO, AssignHandoverDTO, AssignHandoverBO> implements AssignHandoverBusiness {

    static Logger LOGGER = LoggerFactory.getLogger(AssignHandoverBusinessImpl.class);

    @Autowired
    private AssignHandoverDAO assignHandoverDAO;

    @Autowired
    private UtilAttachDocumentBusinessImpl utilAttachDocumentBusinessImpl;

    @Autowired
    private ConstructionDAO constructionDAO;
    
    @Autowired
    private SendSmsEmailDAO sendSmsEmailDAO; //Huypq-add
    
    @Autowired
    private ConstructionTaskDAO constructionTaskDAO;  //Huypq-add
    
    @Autowired
    private ObstructedDAO obstructedDAO;  //Huypq-add
    
    @Autowired
    private WorkItemDAO workItemDAO; 
    
    @Autowired
    private UtilAttachDocumentDAO utilAttachDocumentDAO;

    @Autowired
    private UtilAttachDocumentBusinessImpl utilBusiness;

    @Value("${folder_upload2}")
    private String folderUpload;

    @Value("${allow.file.ext}")
    private String allowFileExt;

    @Value("${allow.folder.dir}")
    private String allowFolderDir;

    @Value("${default_sub_folder_upload}")
    private String defaultSubFolderUpload;
    @Value("${folder_upload2}")
	private String folder2Upload;
	@Value("${input_image_sub_folder_upload}")
	private String input_image_sub_folder_upload;
    @Context
    HttpServletRequest request;

    private final String MAIL_SUBJECT = "Thông báo nhận BGMB";
    private final String MAIL_CONTENT_CN = "TTHT giao cho CN/TTKV nhận BGMB công trình ";
    private final String MAIL_CONTENT_NV = "Bạn được giao nhận BGMB công trình ";
    private final String ATTACH_DOCUMENT_TYPE_HANDOVER = "57";

    private HashMap<Integer, String> colAlias = new HashMap<>();

    {
        colAlias.put(0, "");
        colAlias.put(1, "B");
        colAlias.put(2, "C");
        colAlias.put(3, "D");
    }

    private HashMap<Integer, String> colName = new HashMap<>();

    {
        colName.put(0, "");
//        colName.put(1, "STT");
        colName.put(1, "ĐƠN VỊ");
        colName.put(2, "MÃ CÔNG TRÌNH");
        colName.put(3, "THIẾT KẾ");
        colName.put(4, "Nhân viên");
        
    }
    
    private HashMap<Integer, String> columnName = new HashMap<>();

    {
    	columnName.put(0, "");
    	columnName.put(1, "MÃ CÔNG TRÌNH");
    	columnName.put(2, "Nhân viên");
        
    }

    private final int[] requiredCol = new int[]{1, 2, 3};


    //VietNT_20190109_start
    @Override
    public DataListDTO doSearch(AssignHandoverDTO criteria) {
    	UtilAttachDocumentDTO objFile = null;
        List<AssignHandoverDTO> dtos = assignHandoverDAO.doSearch(criteria);
        //Huypq-20190330-start
        for(AssignHandoverDTO obj : dtos) {
        	objFile = assignHandoverDAO.getFileByAssignHandoverId(obj.getAssignHandoverId());
        	if(null!=objFile) {
        		try {
    				objFile.setFilePath(UEncrypt.encryptFileUploadPath(objFile.getFilePath()));
    			} catch (Exception e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
            	obj.setFileDesign(objFile);
            	if(StringUtils.isNotEmpty(objFile.getName())) {
            		obj.setFileName(objFile.getName());
            	} else {
            		obj.setFileName("");
            	}
        	}
        }
        //Huy-end
        DataListDTO dataListDTO = new DataListDTO();
        dataListDTO.setData(dtos);
        dataListDTO.setTotal(criteria.getTotalRecord());
        dataListDTO.setSize(criteria.getPageSize());
        dataListDTO.setStart(1);
        return dataListDTO;
    }

    @Override
    public Long addNewAssignHandover(AssignHandoverDTO dto, HttpServletRequest request) throws Exception {
        // validate construction_id exist in table cnt_constr_work_item_task
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        List<AssignHandoverDTO> results = assignHandoverDAO.findConstructionContractRef(dto.getConstructionId());
        AssignHandoverDTO ref = null;
        if (results != null && !results.isEmpty()) {
            ref = results.get(0);
        }
        if (null == ref || null == ref.getCntContractId()) {
            throw new Exception("Công trình chưa thuộc hợp đồng nào. " +
                    "Thực hiện gán công trình vào hợp đồng trước khi giao việc cho CN/ TTKT");
        }
        
        CntContractDTO contractDto = assignHandoverDAO.getContractByConsId(dto.getConstructionId());
    	AssignHandoverDTO objNew = new AssignHandoverDTO();
    	objNew.setCatStationHouseCode(dto.getCatStationHouseCode());
    	objNew.setCntContractCode(contractDto.getCode());
    	List<AssignHandoverDTO> assign = assignHandoverDAO.checkStationContract(objNew);
    	if(assign.size()>0) {
    		throw new Exception("Mã nhà trạm và mã hợp đồng của công trình "+ dto.getConstructionCode() +" đã tồn tại !");
    	}

        // do insert assign handover to table
        // ma nha tram, ma tram, ma cong trinh. don vi, design
        //VietNT_20190122_start
        /*
        if (null == dto.getCatStationHouseId() || null == dto.getCatStationId()) {
            dto.setCatStationHouseId(ref.getCatStationHouseId());
            dto.setCatStationHouseCode(ref.getCatStationHouseCode());
            dto.setCatStationId(ref.getCatStationId());
            dto.setCatStationCode(ref.getCatStationCode());
        }
        dto.setCatProvinceId(ref.getCatProvinceId());
        dto.setCatProvinceCode(ref.getCatProvinceCode());
        */
        //VietNT_end

        dto.setCntContractId(ref.getCntContractId());
        dto.setCntContractCode(ref.getCntContractCode());
        dto.setAreaId(dto.getSysGroupId());
        dto.setAreaCode(dto.getSysGroupCode());
        dto.setIsDelivered("0");
        dto.setCreatedGroupId(objUser.getVpsUserInfo().getSysGroupId());
        Long resultId = assignHandoverDAO.saveObject(dto.toModel());
        try {
            if (resultId != 0L) {
                // Import file đính kèm thực hiện lưu dữ liệu vào bảng UTIL_ATTACH_DOCUMENT với object_id = Assign_Handover_id và type = 57
                this.importAttachDocument(dto.getFileDesign(), resultId);

                // insert dữ liệu vào bảng RP_STATION_COMPLETE nếu cặp khóa mã hợp đồng và mã nhà trạm chưa tồn tại trong bảng:
                this.insertIntoRpStationComplete(dto);

                // insert dữ liệu vào bảng SEND_SMS_EMAIL với các điều kiện xác định RECEIVE_PHONE_NUMBER, RECEIVE_EMAIL
                this.sendSms(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultId;
    }

    private void importAttachDocument(UtilAttachDocumentDTO attachDocument, Long assignHandoverId) throws Exception {
        if (attachDocument == null) {
            return;
        }

        attachDocument.setObjectId(assignHandoverId);
        attachDocument.setType(ATTACH_DOCUMENT_TYPE_HANDOVER);
        attachDocument.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        attachDocument.setFilePath(UEncrypt.decryptFileUploadPath(attachDocument.getFilePath()));
        utilAttachDocumentBusinessImpl.save(attachDocument);
    }

    private void insertIntoRpStationComplete(AssignHandoverDTO dto) throws Exception {
        if (!assignHandoverDAO.checkContractCatStationHouseExist(dto.getCatStationHouseId(), dto.getCntContractId())) {
            assignHandoverDAO.insertIntoRpStationComplete(dto);
        }
    }

    /*
    private void sendSms(Long sysGroupId, String constructionCode, Long createdUserId) throws Exception {
        List<SysUserDTO> users = assignHandoverDAO.findUsersReceiveMail(sysGroupId);
    	
//        List<SysUserDTO> users = new ArrayList<>();
        if (null == users || users.isEmpty()) {
            return;
        }

        String subject = "Thông báo nhận BGMB";
        String content = "TTHT giao cho chi nhánh nhận BGMB công trình " + constructionCode;
        Date createdDate = new Date();

        for (SysUserDTO user : users) {
            assignHandoverDAO.insertIntoSendSmsEmailTable(user, subject, content, createdUserId, createdDate);
        }
    }
    */

    private void sendSms(AssignHandoverDTO dto) throws Exception {
        List<SysUserDTO> users = assignHandoverDAO.findUsersReceiveMail(dto.getSysGroupId());

//        List<SysUserDTO> users = new ArrayList<>();
        if (null == users || users.isEmpty()) {
            return;
        }

        for (SysUserDTO user : users) {
            assignHandoverDAO.insertIntoSendSmsEmailTable(
                    user,
                    MAIL_SUBJECT,
                    MAIL_CONTENT_CN + dto.getConstructionCode(),
                    dto.getCreateUserId(),
                    dto.getCreateDate(),
                    dto.getCreatedGroupId());
        }
    }

    @Override
    public List<AssignHandoverDTO> doImportExcel(String filePath, Long sysUserId) {
        List<AssignHandoverDTO> dtoList = new ArrayList<>();
        List<ExcelErrorDTO> errorList = new ArrayList<>();

        try {
            File f = new File(filePath);
            ZipSecureFile.setMinInflateRatio(-1.0d);
            XSSFWorkbook workbook = new XSSFWorkbook(f);
            XSSFSheet sheet = workbook.getSheetAt(0);

            DataFormatter formatter = new DataFormatter();
            int rowCount = 0;

            // prepare data for validate
            //VietNT_20190122_start
            // construction info: province, station, stationHouse
            List<ConstructionDetailDTO> consInfos = constructionDAO.getConstructionByStationId(new ConstructionDTO());
            HashMap<String, ConstructionDetailDTO> consInfoRef = new HashMap<>();
            if (consInfos != null) {
                consInfos.forEach(info -> consInfoRef.put(info.getCode().toUpperCase(), info));
            }
            //VietNT_end

            //HuyPq-11022020-start
            List<String> lstCons = new ArrayList<>();
            int rowCountFile = 0;
            for (Row row1 : sheet) {
            	rowCountFile++;
                // data start from row 3
                if (rowCountFile < 6) {
                    continue;
                } else {
                	if(!isRowEmpty(row1)) {
                		String constructionCode = formatter.formatCellValue(row1.getCell(2)).trim().toUpperCase();
                		lstCons.add(constructionCode);
                	}
                }
                
                
            }
            //Huy-end
            
            // cntContract
            List<AssignHandoverDTO> ref = assignHandoverDAO.findConstructionContractRef(lstCons);
            HashMap<String, AssignHandoverDTO> mapConstructionRef = new HashMap<>();
            if (ref != null) {
                ref.forEach(r -> mapConstructionRef.put(r.getConstructionCode().toUpperCase(), r));
            }

            List<AssignHandoverDTO> sysGroupInfos = assignHandoverDAO.getListSysGroupCode();
            HashMap<String, AssignHandoverDTO> mapSysGroupRef = new HashMap<>();
            if (sysGroupInfos != null) {
                sysGroupInfos.forEach(r -> mapSysGroupRef.put(r.getSysGroupCode().toUpperCase(), r));
            }

            AssignHandoverDTO activeConstructionCriteria = new AssignHandoverDTO();
            activeConstructionCriteria.setStatus(1L);
            List<AssignHandoverDTO> exist = assignHandoverDAO.doSearch(activeConstructionCriteria);
            HashMap<String, String> consCodeSysGroupMapRef = new HashMap<>();
            if (exist != null && !exist.isEmpty()) {
                exist.forEach(dto -> consCodeSysGroupMapRef.put(dto.getConstructionCode().toUpperCase(), dto.getSysGroupName()));
            }

            for (Row row : sheet) {
                rowCount++;
                // data start from row 3
                if (rowCount < 6) {
                    continue;
                }

                // check required field empty
                //if (!this.isRequiredDataExist(row, errorList, formatter)) {
                //    continue;
                //}
                this.isRequiredDataExist(row, errorList, formatter);

                AssignHandoverDTO dtoValidated = new AssignHandoverDTO();
                String sysGroupCode = formatter.formatCellValue(row.getCell(1)).trim();
                String constructionCode = formatter.formatCellValue(row.getCell(2)).trim();
                String isDesignStr = formatter.formatCellValue(row.getCell(3)).trim();

                int errorCol = 1;
                if (!StringUtils.isEmpty(sysGroupCode)) {
                    AssignHandoverDTO sysGroupInfo = mapSysGroupRef.get(sysGroupCode.toUpperCase());
                    if (sysGroupInfo != null) {
                        dtoValidated.setSysGroupCode(sysGroupInfo.getSysGroupCode());
                        dtoValidated.setSysGroupId(sysGroupInfo.getSysGroupId());
                        dtoValidated.setSysGroupName(sysGroupInfo.getSysGroupName());
                    } else {
                        errorList.add(this.createError(rowCount, errorCol, "không tồn tại"));
                    }
                } else {
                    errorList.add(this.createError(rowCount, errorCol, "không được bỏ trống"));
                }

                errorCol = 2;
                int errorColNull = 0;
                if (!StringUtils.isEmpty(constructionCode)) {
                    // check exist
                    String assignedGroupName = consCodeSysGroupMapRef.get(constructionCode.toUpperCase());
                    //Huypq-20190320-start
                    AssignHandoverDTO assignData = assignHandoverDAO.getAssignByConsCode(constructionCode);
                    //Huy-end
                    //HuyPQ-20190524-start
                    ConstructionDTO constr = assignHandoverDAO.getHouseCodeByConsId(constructionCode);
//                    hoanm1_20190624_start
                    if (constr !=null) {
//                    	hoanm1_20190624_end
                    CntContractDTO contractDto = assignHandoverDAO.getContractByConsId(constr.getConstructionId());
                	AssignHandoverDTO objNew = new AssignHandoverDTO();
                	objNew.setCatStationHouseCode(constr.getCatStationHouseCode());
                	objNew.setCntContractCode(contractDto.getCode());
                	List<AssignHandoverDTO> assign = assignHandoverDAO.checkStationContract(objNew);
                	//Huy-end
                    if (StringUtils.isEmpty(assignedGroupName)) {
                        // check info catStation, catStationHouse, catProvince
                        ConstructionDetailDTO consInfo = consInfoRef.get(constructionCode.toUpperCase());
                        if (consInfo != null) {
                            // check info contract
                            AssignHandoverDTO consContractInfo = mapConstructionRef.get(constructionCode.toUpperCase());
                            if (consContractInfo != null) {
                                dtoValidated.setConstructionCode(consContractInfo.getConstructionCode());
                                dtoValidated.setConstructionId(consContractInfo.getConstructionId());
                                dtoValidated.setCntContractId(consContractInfo.getCntContractId());
                                dtoValidated.setCntContractCode(consContractInfo.getCntContractCode());

                                dtoValidated.setCatProvinceId(consInfo.getCatProvinceId());
                                dtoValidated.setCatProvinceCode(consInfo.getCatProvinceCode());
                                dtoValidated.setCatStationHouseId(consInfo.getCatStationHouseId());
                                dtoValidated.setCatStationHouseCode(consInfo.getCatStationHouseCode());
                                dtoValidated.setCatStationId(consInfo.getCatStationId());
                                dtoValidated.setCatStationCode(consInfo.getCatStationCode());

                                if (StringUtils.isNotEmpty(dtoValidated.getSysGroupName())) {
                                    consCodeSysGroupMapRef.put(constructionCode.toUpperCase(), dtoValidated.getSysGroupName());
                                }
                            } else {
                                errorList.add(this.createError(rowCount, errorCol, "chưa thuộc hợp đồng nào. Thực hiện gán công trình vào hợp đồng trước khi giao việc cho CN/ TTKT"));
                            }
                        } else {
                            errorList.add(this.createError(rowCount, errorCol, "không thuộc tỉnh, nhà trạm hoặc mã nhà trạm"));
                        }
                        //HuyPq-start
                    } else if(assign.size() > 0){
                        errorList.add(this.createError(rowCount, errorColNull, "Mã nhà trạm và hợp đồng của công trình " +constructionCode.toUpperCase() + " đã tồn tại"));
                    } else if(!StringUtils.isEmpty(assignedGroupName)){
                        errorList.add(this.createError(rowCount, errorCol, constructionCode.toUpperCase() + " đã được giao cho chi nhánh: " + assignedGroupName));
                    }
                    //Huy-end
//                 hoanm1_20190624_start   
                } else {
                    errorList.add(this.createError(rowCount, errorCol, "không tồn tại"));
                }
//                    hoanm1_20190624_end
                } else {
                    errorList.add(this.createError(rowCount, errorCol, "không được bỏ trống"));
                }

                errorCol = 3;
                if (StringUtils.isEmpty(isDesignStr)) {
                    errorList.add(this.createError(rowCount, errorCol, "không được bỏ trống"));
                } else if (!isDesignStr.equals("0") && !isDesignStr.equals("1")) {
                    errorList.add(this.createError(rowCount, errorCol, "Sai kiểu dữ liệu"));
                } else {
                    dtoValidated.setIsDesign(Long.parseLong(isDesignStr));
                    if (isDesignStr.equals("0")) {
                        dtoValidated.setCompanyAssignDate(new Date());
                    }
                }

                if (errorList.size() == 0) {
                    dtoValidated.setStatus(1L);
                    dtoValidated.setReceivedStatus(1L);
                    dtoValidated.setCreateDate(new Date());
                    dtoValidated.setCreateUserId(sysUserId);
                    dtoList.add(dtoValidated);
                }
            }

            if (errorList.size() > 0) {
                String filePathError = UEncrypt.encryptFileUploadPath(filePath);
                this.doWriteError(errorList, dtoList, filePathError, 5);
            }
            workbook.close();

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ExcelErrorDTO errorDTO = createError(0, 0, e.toString());
            errorList.add(errorDTO);
            String filePathError = null;

            try {
                filePathError = UEncrypt.encryptFileUploadPath(filePath);
            } catch (Exception ex) {
                LOGGER.error(e.getMessage(), e);
                errorDTO = createError(0, 0, ex.toString());
                errorList.add(errorDTO);
            }
            this.doWriteError(errorList, dtoList, filePathError, 5);
        }

        return dtoList;
    }

    public void addNewAssignHandoverImport(List<AssignHandoverDTO> dtos) throws Exception {
        List<String> exist = assignHandoverDAO.findUniqueRpStationComplete();
        String search = "";
        for (AssignHandoverDTO dto : dtos) {
        	dto.setAreaId(dto.getSysGroupId());
        	dto.setAreaCode(dto.getSysGroupCode());
        	dto.setIsDelivered("0");
        	dto.setCompanyAssignDate(new Date());
            assignHandoverDAO.saveObject(dto.toModel());
            if (dto.getCatStationHouseId() != null && dto.getCntContractId() != null) {
                search = dto.getCatStationHouseId().toString().toUpperCase() + "_" + dto.getCntContractId().toString().toUpperCase();
            }
            // not found insert
            if (exist.indexOf(search) < 0) {
                assignHandoverDAO.insertIntoRpStationComplete(dto);
            }
            this.sendSms(dto);
        }
    }

    private void doWriteError(List<ExcelErrorDTO> errorList, List<AssignHandoverDTO> dtoList, String filePathError, int errColumn) {
        dtoList.clear();

        AssignHandoverDTO errorContainer = new AssignHandoverDTO();
        errorContainer.setErrorList(errorList);
        errorContainer.setMessageColumn(errColumn); // cột dùng để in ra lỗi
        errorContainer.setFilePathError(filePathError);

        dtoList.add(errorContainer);
    }

    private boolean isRequiredDataExist(Row row, List<ExcelErrorDTO> errorList, DataFormatter formatter) {
        int errCount = 0;
        for (int colIndex : requiredCol) {
            if (StringUtils.isEmpty(formatter.formatCellValue(row.getCell(colIndex)))) {
                ExcelErrorDTO errorDTO = this.createError(row.getRowNum() + 1, colIndex, "chưa nhập");
                errorList.add(errorDTO);
                errCount++;
            }
        }
        return errCount == 0;
    }

    private ExcelErrorDTO createError(int row, int columnIndex, String detail) {
        ExcelErrorDTO err = new ExcelErrorDTO();
        err.setColumnError(colAlias.get(columnIndex));
        err.setLineError(String.valueOf(row));
        err.setDetailError(colName.get(columnIndex) + " " + detail);
        return err;
    }

    private ExcelErrorDTO createErrorBGMB(int row, int columnIndex, String detail) {
        ExcelErrorDTO err = new ExcelErrorDTO();
        err.setColumnError(colAlias.get(columnIndex));
        err.setLineError(String.valueOf(row));
        err.setDetailError(columnName.get(columnIndex) + " " + detail);
        return err;
    }
    
    @Override
    public String downloadTemplate() throws Exception {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String fileName = "GiaoViecChoCN.xlsx";
        String filePath = classloader.getResource("../" + "doc-template").getPath() + fileName;
        InputStream file = new BufferedInputStream(new FileInputStream(filePath));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        file.close();
        Calendar cal = Calendar.getInstance();
        String uploadPath = folderUpload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
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
        
        workbook.write(outFile);
        workbook.close();
        outFile.close();
        String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + fileName);
        return path;
    }

    public String downloadTemplateTTKT() throws Exception {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String fileName = "GiaoViecChoTTKT.xlsx";
        String filePath = classloader.getResource("../" + "doc-template").getPath() + fileName;
        InputStream file = new BufferedInputStream(new FileInputStream(filePath));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        file.close();
        Calendar cal = Calendar.getInstance();
        String uploadPath = folderUpload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
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

        workbook.write(outFile);
        workbook.close();
        outFile.close();
        String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + fileName);
        return path;
    }

    @Override
    public DataListDTO getAttachFile(Long id) throws Exception {
        List<UtilAttachDocumentDTO> attachFileList = utilAttachDocumentDAO.getByTypeAndObjectTC(id, ATTACH_DOCUMENT_TYPE_HANDOVER);
        DataListDTO dataListDTO = new DataListDTO();
        dataListDTO.setData(attachFileList);
        dataListDTO.setTotal(1);
        dataListDTO.setSize(1);
        dataListDTO.setStart(1);
        return dataListDTO;
    }

    @Override
    public Long updateAttachFileDesign(AssignHandoverDTO dto) throws Exception {
        if (dto.getFileDesign() == null) {
            return 0L;
        } else {
            AssignHandoverDTO assignHandoverDTO = assignHandoverDAO.findById(dto.getAssignHandoverId());
            if (assignHandoverDTO == null) {
                return 0L;
            }

            Date today = new Date();
            Long id;
            List<UtilAttachDocumentDTO> attachFileList = utilAttachDocumentDAO.getByTypeAndObjectTC(dto.getAssignHandoverId(), ATTACH_DOCUMENT_TYPE_HANDOVER);
            UtilAttachDocumentDTO file = dto.getFileDesign();
            file.setObjectId(dto.getAssignHandoverId());
            file.setType(ATTACH_DOCUMENT_TYPE_HANDOVER);
            file.setFilePath(UEncrypt.decryptFileUploadPath(file.getFilePath()));
            file.setCreatedDate(today);
            if (attachFileList != null && !attachFileList.isEmpty()) {
                //found, update current file (same id)
                file.setUtilAttachDocumentId(attachFileList.get(0).getUtilAttachDocumentId());
                id = utilAttachDocumentBusinessImpl.update(file);
            } else {
                id = utilAttachDocumentBusinessImpl.save(file);
            }

            // setCompanyAssignDate = sysDate when update design
            assignHandoverDTO.setCompanyAssignDate(today);
            assignHandoverDAO.updateObject(assignHandoverDTO.toModel());
            return id;
        }
    }

//    @Override
//    public Long removeAssignHandover(Long assignHandoverId, Long sysUserId) {
//        try {
//            AssignHandoverDTO match = assignHandoverDAO.findById(assignHandoverId);
////            if (match == null || match.getPerformentId() != null) {
////                return 0L;
////            }
//
//            match.setStatus(0L);
//            match.setUpdateDate(new Date());
//            match.setUpdateUserId(sysUserId);
//            return assignHandoverDAO.updateObject(match.toModel());
//        } catch (Exception e) {
//            e.printStackTrace();
//            return 0L;
//        }
//    }
    //Huypq-start
    @Override
    public String removeAssignHandover(Long assignHandoverId, Long sysUserId) {
        try {
            AssignHandoverDTO match = assignHandoverDAO.findById(assignHandoverId);
//            if (match == null || match.getPerformentId() != null) {
//                return 0L;
//            }

//            match.setStatus(0L);
//            match.setUpdateDate(new Date());
//            match.setUpdateUserId(sysUserId);
            String dao = assignHandoverDAO.delete(match.toModel());
            return dao;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    //Huy-end

    //VietNT_20181218_start
    public DataListDTO doSearchNV(AssignHandoverDTO criteria, List<String> sysGroupId) {
        List<AssignHandoverDTO> dtos = assignHandoverDAO.doSearchNV(criteria, sysGroupId);
        for(AssignHandoverDTO dto : dtos) {
        	AssignHandoverDTO assign = assignHandoverDAO.getCatConstructionTypeId(dto.getConstructionId());
        	dto.setCatConstructionTypeId(assign.getCatConstructionTypeId());
        }
        DataListDTO dataListDTO = new DataListDTO();
        dataListDTO.setData(dtos);
        dataListDTO.setTotal(criteria.getTotalRecord());
        dataListDTO.setSize(criteria.getPageSize());
        dataListDTO.setStart(1);
        return dataListDTO;
    }

    public Long doAssignHandover(AssignHandoverDTO updateInfo, Long sysUserId, Long createdGroupId) {
        List<AssignHandoverDTO> handoverDtos = assignHandoverDAO.findByIdList(updateInfo.getAssignHandoverIdList());
        if (handoverDtos == null || handoverDtos.isEmpty()) {
            return 0L;
        }
        Date today = new Date();
        handoverDtos.forEach(dto -> {
            dto.setUpdateUserId(sysUserId);
            dto.setUpdateDate(today);
            dto.setDepartmentAssignDate(today);
            dto.setPerformentId(updateInfo.getPerformentId());

            assignHandoverDAO.updateObject(dto.toModel());
            this.sendSmsPerformer(
                    dto.getConstructionCode(),
                    updateInfo.getEmail(),
                    updateInfo.getPhoneNumber(),
                    sysUserId,
                    today,
                    createdGroupId
                    );
        });

        return 1L;
    }

    private void sendSmsPerformer(String constructionCode, String email, String phoneNum, Long createdUserId, Date createdDate, Long createdGroupId) {
        assignHandoverDAO.insertIntoSendSmsEmailTable(
                MAIL_SUBJECT,
                MAIL_CONTENT_NV + constructionCode,
                email,
                phoneNum,
                createdUserId,
                createdDate,
                createdGroupId);
    }

    public List<UtilAttachDocumentDTO> getListImageHandover(Long handoverId) throws Exception {
        List<UtilAttachDocumentDTO> imageList = utilAttachDocumentDAO.getByTypeAndObjectTC(handoverId, "56");
        this.convertImageToBase64(imageList);

        return imageList;
    }

    private void convertImageToBase64(List<UtilAttachDocumentDTO> imageList) {
        imageList.forEach(img -> {
            try {
                String fullPath = folderUpload + File.separator + UEncrypt.decryptFileUploadPath(img.getFilePath());
                String base64Image = ImageUtil.convertImageToBase64(fullPath);
                img.setBase64String(base64Image);
//                img.setFilePath(fullPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ConstructionDetailDTO getConstructionProvinceByCode(String constructionCode) {
        return constructionDAO.getConstructionByCode(constructionCode);
    }

    public void updateDeliveryConstructionDate(Long assignHandoverId, Long updateUserId) {
        try {
            AssignHandoverDTO dto = assignHandoverDAO.findById(assignHandoverId);
            if (dto != null && dto.getDeliveryConstructionDate() == null) {
                Date today = new Date();
                dto.setDeliveryConstructionDate(today);
                dto.setUpdateDate(today);
                dto.setUpdateUserId(updateUserId);
                assignHandoverDAO.updateObject(dto.toModel());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DataListDTO getForSysUserAutoComplete(SysUserCOMSDTO obj) {
        List<SysUserCOMSDTO> dtos = assignHandoverDAO.getForSysUserAutoComplete(obj);
        DataListDTO dataListDTO = new DataListDTO();
        dataListDTO.setData(dtos);
        dataListDTO.setTotal(obj.getTotalRecord());
        dataListDTO.setSize(obj.getPageSize());
        dataListDTO.setStart(1);
        return dataListDTO;
    }

    public Long updateWorkItemConstructor(ConstructionTaskDetailDTO dto) {
        try {
            Long result = 1L;
            if (null == dto || null == dto.getWorkItemId() || null == dto.getConstructorId()) {
                result = -1L;
            } else {
                assignHandoverDAO.updateWorkItemConstructor(dto.getWorkItemId(), dto.getConstructorId());
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return -1L;
        }
    }
    //VietNT_end
    //VietNT_20190225_start
    private final String HANDOVER_NV_LIST = "Danh_sach_giao_viec_cho_NV.xlsx";

    public String exportHandoverNV(AssignHandoverDTO criteria, List<String> sysGroupIdStr) throws Exception {
    	List<AssignHandoverDTO> dtos = assignHandoverDAO.doSearchNV(criteria, sysGroupIdStr);
        XSSFWorkbook workbook = utilBusiness.createWorkbook(HANDOVER_NV_LIST);
        XSSFSheet sheet = workbook.getSheetAt(0);

        // prepare cell style
        List<CellStyle> styles = this.prepareCellStyles(workbook, sheet);

        // start from
        int rowNo = 1;
        int[] sumValue = new int[] {0, 0, 0};
        XSSFRow row;
        for (AssignHandoverDTO dto : dtos) {
            row = sheet.createRow(rowNo);
            this.createRowHandoverNV(dto, row, styles);
            if (dto.getReceivedObstructDate() != null) {
                sumValue[0]++;
            }
            if (dto.getReceivedGoodsDate() != null) {
                sumValue[1]++;
            }
            if (dto.getReceivedDate() != null) {
                sumValue[2]++;
            }
            rowNo++;
        }

        // create row sum
        int[] colIndex = new int[] {7, 8, 9};
        row = sheet.createRow(rowNo);
        this.createRowSum(row, styles, colIndex, sumValue);

        String path = utilBusiness.writeToFileOnServer(workbook, HANDOVER_NV_LIST);
        return path;
    }

    private void createRowSum(XSSFRow row, List<CellStyle> styles, int[] colIndex, int[] sumValue) {
        for (int i = 0; i < 19; i++) {
            utilBusiness.createExcelCell(row, i, styles.get(0));
        }
        utilBusiness.createExcelCell(row, colIndex[0] - 1, styles.get(0)).setCellValue("Tổng: ");
        for (int i = 0; i < colIndex.length; i++) {
            utilBusiness.createExcelCell(row, colIndex[i], styles.get(0)).setCellValue(sumValue[i]);
        }
    }
    
    private final String[] CONS_STATUS_CONVERT = new String[] {StringUtils.EMPTY, "Chưa thi công", "Đang thi công", "Thi công xong"}; 

    private void createRowHandoverNV(AssignHandoverDTO dto, XSSFRow row, List<CellStyle> styles) {
    	utilBusiness.createExcelCell(row, 0, styles.get(0)).setCellValue(row.getRowNum());
    	utilBusiness.createExcelCell(row, 1, styles.get(1)).setCellValue(dto.getCompanyAssignDate());
        utilBusiness.createExcelCell(row, 2, styles.get(0)).setCellValue(dto.getConstructionCode() != null ? dto.getConstructionCode() : "");
        String design = dto.getIsDesign() == 1L ? "Có thiết kế" : "Không có thiết kế";
        utilBusiness.createExcelCell(row, 3, styles.get(0)).setCellValue(design);
        utilBusiness.createExcelCell(row, 4, styles.get(0)).setCellValue(dto.getFullName() != null ? dto.getFullName() : "");
        utilBusiness.createExcelCell(row, 5, styles.get(1)).setCellValue(dto.getDepartmentAssignDate());
        utilBusiness.createExcelCell(row, 6, styles.get(0)).setCellValue(dto.getOutOfDateReceived() != null ? dto.getOutOfDateReceived() : 0);
        utilBusiness.createExcelCell(row, 7, styles.get(1)).setCellValue(dto.getReceivedObstructDate());
        utilBusiness.createExcelCell(row, 8, styles.get(1)).setCellValue(dto.getReceivedGoodsDate());
        utilBusiness.createExcelCell(row, 9, styles.get(1)).setCellValue(dto.getReceivedDate());
        utilBusiness.createExcelCell(row, 10, styles.get(1)).setCellValue(dto.getDeliveryConstructionDate());
        utilBusiness.createExcelCell(row, 11, styles.get(0)).setCellValue(dto.getPerformentConstructionName() != null ? dto.getPerformentConstructionName() : "");
        utilBusiness.createExcelCell(row, 12, styles.get(0)).setCellValue(dto.getSupervisorConstructionName() != null ? dto.getSupervisorConstructionName() : "");
        utilBusiness.createExcelCell(row, 13, styles.get(1)).setCellValue(dto.getStartingDate());
        utilBusiness.createExcelCell(row, 14, styles.get(0)).setCellValue(dto.getOutOfDateStartDate() != null ? dto.getOutOfDateStartDate() : 0);
        String consStatus = dto.getConstructionStatus() != null ? CONS_STATUS_CONVERT[dto.getConstructionStatus().intValue()] : ""; 
        utilBusiness.createExcelCell(row, 15, styles.get(0)).setCellValue(consStatus);
        utilBusiness.createExcelCell(row, 16, styles.get(0)).setCellValue(dto.getCatStationHouseCode() != null ? dto.getCatStationHouseCode() : "");
        utilBusiness.createExcelCell(row, 17, styles.get(0)).setCellValue(dto.getCntContractCode() != null ? dto.getCntContractCode() : "");
        utilBusiness.createExcelCell(row, 18, styles.get(0)).setCellValue(dto.getPartnerName() != null ? dto.getPartnerName() : "");
    }
   
    
    private List<CellStyle> prepareCellStyles(XSSFWorkbook workbook, XSSFSheet sheet) {
    	CellStyle styleText = ExcelUtils.styleText(sheet);
		CellStyle styleDate = ExcelUtils.styleDate(sheet);

		XSSFCreationHelper createHelper = workbook.getCreationHelper();
		styleDate.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
		styleDate.setAlignment(HorizontalAlignment.CENTER);
		
		return Arrays.asList(styleText, styleDate);
    }
    //VietNT_end
    
	/**Hoangnh start 04022019 -- BGMB Tuyến**/
 	public void saveImage(List<ConstructionImageInfo> req, AssignHandoverDTO dto){
		List<ConstructionImageInfo> lstConstructionImages = saveImages(req);
		
		assignHandoverDAO.saveImagePath(lstConstructionImages, dto);
	}
 	
 	// Luu danh sach anh gui ve va lay ra duong dan
   	public List<ConstructionImageInfo> saveImages(List<ConstructionImageInfo> lstConstructionImages) {
   		List<ConstructionImageInfo> result = new ArrayList<>();
   		LOGGER.warn("log 1:" );
   		for (ConstructionImageInfo constructionImage : lstConstructionImages) {
   			if (constructionImage.getStatus() == 0) {
   				LOGGER.warn("log 2:" + constructionImage.getStatus());
   				ConstructionImageInfo obj = new ConstructionImageInfo();
   				LOGGER.warn("log 3:" + constructionImage.getImageName());
   				obj.setImageName(constructionImage.getImageName());
   				obj.setLatitude(constructionImage.getLatitude());
   				obj.setLongtitude(constructionImage.getLongtitude());
   				LOGGER.warn("log 5:" + constructionImage.getBase64String());
   				InputStream inputStream = ImageUtil.convertBase64ToInputStream(constructionImage.getBase64String());
   				LOGGER.warn("log 4:" + inputStream);
   				try {
   					String imagePath = UFile.writeToFileServer(inputStream, constructionImage.getImageName(),input_image_sub_folder_upload, folder2Upload);
   					obj.setImagePath(imagePath);
   					LOGGER.warn("imagePath :" + imagePath);
   				} catch (Exception e) {
   					continue;
   				}
   				result.add(obj);
   			}
   		}

   		return result;
   	}
    
   	public int updateAH(AssignHandoverDTO obj){
   		return assignHandoverDAO.updateAH(obj);
   	}
   	public int updateCons(AssignHandoverDTO obj){
   		return assignHandoverDAO.updateCons(obj);
   	}
   	
   	public void updateRP(Long cntContractId,Long catStationHouseId){
   		assignHandoverDAO.updateRP(cntContractId, catStationHouseId);
   	}
   	
   	public void removeFile(Long objectId , String type){
   		assignHandoverDAO.removeFile(objectId, type);
   	}
   	
   	public List<WorkItemDTO> getListWorkItem(AssignHandoverDTO obj){
   		return assignHandoverDAO.getListWorkItem(obj);
   	}
   	
   	public void updateWorkItem(Long workItemId){
   		assignHandoverDAO.updateWorkItem(workItemId);
   	}
   	
   	public AssignHandoverDTO getStation(Long assignHandoverId){
   		return assignHandoverDAO.getStation(assignHandoverId);
   	}
   	
   	public SysUserDTO getSysUser(Long sysUserId){
   		return assignHandoverDAO.getSysUser(sysUserId);
   	}
   	
   	public List<CatWorkItemTypeDTO> getWIType(String type){
   		return assignHandoverDAO.getWIType(type);
   	}
    /**Hoangnh end 04022019 -- BGMB Tuyến**/
	
    //Huypq-20190315-start
    public static boolean isRowEmpty(Row row) {
		for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
			Cell cell = row.getCell(c);
			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
				return false;
			}
		}
		return true;
	}
    
    public List<AssignHandoverDTO> checkStationContractBGMB(AssignHandoverDTO obj){
    	return assignHandoverDAO.checkStationContractBGMB(obj);
    }
    
    public DataListDTO doSearchTTKT(AssignHandoverDTO criteria, HttpServletRequest request) {
    	UtilAttachDocumentDTO objFile = null;
    	String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.ASSIGN,
				Constant.AdResourceKey.TTKT, request);
		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
        List<AssignHandoverDTO> dtos = assignHandoverDAO.doSearchTTKT(criteria,groupIdList);
        for(AssignHandoverDTO obj : dtos) {
        	objFile = assignHandoverDAO.getFileByAssignHandoverId(obj.getAssignHandoverId());
        	if(null!=objFile) {
        		try {
    				objFile.setFilePath(UEncrypt.encryptFileUploadPath(objFile.getFilePath()));
    			} catch (Exception e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
            	obj.setFileDesign(objFile);
            	if(StringUtils.isNotEmpty(objFile.getName())) {
            		obj.setFileName(objFile.getName());
            	} else {
            		obj.setFileName("");
            	}
        	}
        }
        DataListDTO dataListDTO = new DataListDTO();
        dataListDTO.setData(dtos);
        dataListDTO.setTotal(criteria.getTotalRecord());
        dataListDTO.setSize(criteria.getPageSize());
        dataListDTO.setStart(1);
        return dataListDTO;
    }
    
    public int updateSysGroupInAssignHandover(AssignHandoverDTO obj) {
    	int i = 0;
    	for(AssignHandoverDTO dto :obj.getLstAssignId()) {
    		assignHandoverDAO.updateRpStationCompleteTTKT(obj,dto.getCatStationHouseCode(),dto.getCntContractCode());
    	}
    	for(AssignHandoverDTO id : obj.getLstAssignId()) {
    		id.setSysGroupId(obj.getSysGroupId());
    		id.setSysGroupCode(obj.getSysGroupCode());
    		id.setSysGroupName(obj.getSysGroupName());
    		i = assignHandoverDAO.updateSysGroupInAssignHandover(id);
    	}
    	return i;
    }
    
    public int removeAssignById(Long id) {
        return assignHandoverDAO.removeAssignById(id);
    }
    
    public List<AssignHandoverDTO> getCheckDataWorkItem(AssignHandoverDTO obj){
    	return assignHandoverDAO.getCheckDataWorkItem(obj);
    }
    
    public void deleteDataRpStation(AssignHandoverDTO obj) {
    	assignHandoverDAO.deleteDataRpStation(obj);
//    	List<AssignHandoverDTO> dto = assignHandoverDAO.getCheckDataWorkItem(obj);
//    	if(dto.size()==0) {
    		List<WorkItemDTO> wiDto =  assignHandoverDAO.getWorkItemByStationHouseCode(obj);
        	for(WorkItemDTO wi : wiDto) {
        		workItemDAO.delete(wi.toModel());
        	}
//    	}
    	List<ConstructionTaskDTO> consTask = assignHandoverDAO.getConsTaskByConsId(obj);
    	for(ConstructionTaskDTO cons : consTask) {
    		constructionTaskDAO.delete(cons.toModel());
    	}
    	assignHandoverDAO.updateHandoverDateBuild(obj.getConstructionId());
    }
    
    public boolean checkRolePermissionSMS(HttpServletRequest request) {
		if (!VpsPermissionChecker.hasPermission(Constant.OperationKey.RECEIVE,
				Constant.AdResourceKey.SMS_HANDOVER, request)) {
			return false;
		}
		return true;
	}
    
    public List<AssignHandoverDTO> checkGroupList(AssignHandoverDTO obj, HttpServletRequest request){
    	String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.RECEIVE,
				Constant.AdResourceKey.SMS_HANDOVER, request);
		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
		List<AssignHandoverDTO> dto = new ArrayList<AssignHandoverDTO>();
		if(obj.getLstAssignId()!=null) {
			if(obj.getLstAssignId().size()>0) {
				for(AssignHandoverDTO id : obj.getLstAssignId()) {
					dto = assignHandoverDAO.checkGroupList(groupIdList, id.getAssignHandoverId());
				}
			}
		} else {
			dto = assignHandoverDAO.checkGroupList(groupIdList, obj.getAssignHandoverId());
		}
		return dto;
    }
    
    public String insertSmsemail(AssignHandoverDTO obj) {
//    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
//    	VpsUserToken token = (VpsUserToken) request.getSession().getAttribute("vpsUserToken");
    	SendSmsEmailBO bo = new SendSmsEmailBO();
    	bo.setSubject("Thông báo  hủy bỏ việc nhận BGMB");
    	bo.setContent("TTHT hủy việc nhận BGMB công trình " + obj.getConstructionCode() + ". Tất cả các công việc liên quan đến công trình đã bị huỷ.");
    	bo.setReceivePhoneNumber(obj.getUserNumberPhone());
    	bo.setReceiveEmail(obj.getUserEmail());
//    	bo.setStatus("0");
//    	bo.setCreatedDate(new Date());
//    	bo.setCreatedUserId(token.getSysUserId());
//    	bo.setCreatedGroupId(objUser.getVpsUserInfo().getSysGroupId());
    	bo.setStatus("0");
    	bo.setCreatedDate(new Date());
    	bo.setCreatedGroupId(obj.getCreatedGroupId());
    	bo.setCreatedUserId(obj.getCreateUserId());
    	return sendSmsEmailDAO.save(bo);
    }
    
    public String insertSmsemailTTKT(AssignHandoverDTO obj, HttpServletRequest request) {
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
    	VpsUserToken token = (VpsUserToken) request.getSession().getAttribute("vpsUserToken");
    	String ids = null;
    	for(AssignHandoverDTO id : obj.getLstAssignId()) {
    		SendSmsEmailBO bo = new SendSmsEmailBO();
    		bo.setSubject("Thông báo nhận BGMB");
        	bo.setContent("TTKV giao cho TTKT nhận BGMB công trình " + id.getConstructionCode());
        	bo.setReceivePhoneNumber(obj.getUserNumberPhone());
        	bo.setReceiveEmail(obj.getUserEmail());
        	bo.setStatus("0");
        	bo.setCreatedDate(new Date());
        	bo.setCreatedUserId(token.getSysUserId());
        	bo.setCreatedGroupId(objUser.getVpsUserInfo().getSysGroupId());
        	ids = sendSmsEmailDAO.save(bo);
    	}
    	return ids;
    }
    
    public List<AssignHandoverDTO> checkStationContract(AssignHandoverDTO obj){
    	return assignHandoverDAO.checkStationContract(obj);
    }
    
    public DataListDTO getListHouseType(AssignHandoverDTO obj){
    	List<AssignHandoverDTO> dtos = assignHandoverDAO.getHouseType(obj);
    	DataListDTO dataListDTO = new DataListDTO();
        dataListDTO.setData(dtos);
        dataListDTO.setTotal(obj.getTotalRecord());
        dataListDTO.setSize(obj.getPageSize());
        dataListDTO.setStart(1);
        return dataListDTO;
    }
    
    public DataListDTO getListGroundingType(AssignHandoverDTO obj){
    	List<AssignHandoverDTO> dtos = assignHandoverDAO.getGroundingType(obj);
    	DataListDTO dataListDTO = new DataListDTO();
        dataListDTO.setData(dtos);
        dataListDTO.setTotal(obj.getTotalRecord());
        dataListDTO.setSize(obj.getPageSize());
        dataListDTO.setStart(1);
        return dataListDTO;
    }
    
    public long removeAssignHandoverById(AssignHandoverDTO obj) {
    	obj.setReceivedStatus(null);
    	obj.setReceivedObstructDate(null);
    	obj.setReceivedObstructContent(null);
    	obj.setReceivedGoodsDate(null);
    	obj.setReceivedGoodsContent(null);
    	obj.setReceivedDate(null);
    	obj.setColumnHeight(null);
    	obj.setStationType(null);
    	obj.setNumberCo(null);
    	obj.setHouseTypeId(null);
    	obj.setHouseTypeName(null);
    	obj.setHaveWorkItemName(null);
    	obj.setIsFence(null);
    	return assignHandoverDAO.updateObject(obj.toModel());
    }
    
    public long updateEditDataAssignHandoverNv(AssignHandoverDTO obj) {
    	AssignHandoverDTO dto = assignHandoverDAO.findById(obj.getAssignHandoverId());
    	dto.setColumnHeight(obj.getColumnHeight());
    	dto.setStationType(obj.getStationType());
    	dto.setNumberCo(obj.getNumberCo());
    	dto.setHouseTypeId(obj.getHouseTypeId());
    	dto.setHouseTypeName(obj.getHouseTypeName());
    	dto.setIsFence(obj.getIsFence());
    	dto.setHaveWorkItemName(obj.getHaveWorkItemName());
    	dto.setCatStationHouseCode(obj.getCatStationHouseCode());
    	dto.setCntContractCode(obj.getCntContractCode());
    	assignHandoverDAO.updateRpStationCompleteByHouseId(dto);
    	return assignHandoverDAO.updateObject(dto.toModel());
    }
    
    public long updateAssignHandoverVuong(AssignHandoverDTO obj, HttpServletRequest request) throws Exception{
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
    	VpsUserToken token = (VpsUserToken) request.getSession().getAttribute("vpsUserToken");
//    	List<ConstructionTaskDTO> consTask = assignHandoverDAO.findConstructionTaskByConsId(obj);
    	List<GoodsPlanDTO> goods = assignHandoverDAO.findGoodsPlanByConsId(obj);
    	List<RequestGoodsDetailDTO> requestGoods = assignHandoverDAO.findRequestGoods(obj);
    	ConstructionDTO dto = assignHandoverDAO.getStatusByConsId(obj.getConstructionId());
    	Long id = null;
    	if(goods.size()>0) {
    		for(GoodsPlanDTO good : goods) {
    			if(!good.getSignState().equals("1")) {
    				throw new Exception("Công trình đã được đặt vật tư sang phòng đầu tư.");
    			} else {
    				for(RequestGoodsDetailDTO requestGood : requestGoods) {
    					assignHandoverDAO.deleteRequestGoodsByConsId(requestGood.getRequestGoodsId());
        				assignHandoverDAO.deleteRequestGoodsDetailByConsId(requestGood.getRequestGoodsDetailId());
    				}
    				assignHandoverDAO.deleteGoodsPlanDetailByConsId(good.getGoodsPlanDetailId());
    			}
    		}
    	}
    	if(StringUtils.isNotEmpty(dto.getStatus())) {
    		if(!dto.getStatus().equals("4")) {
    			assignHandoverDAO.updateVuongTaskWorkCons(obj.getConstructionId());
        	}
    	}
    	if(StringUtils.isNotEmpty(obj.getTotalLength())) {
    		assignHandoverDAO.updateVuongTaskWorkCons(obj.getConstructionId());
    	}
    	ObstructedDTO dtoObstruct = new ObstructedDTO();
    	dtoObstruct.setObstructedState("1");
    	dtoObstruct.setConstructionId(obj.getConstructionId());
    	dtoObstruct.setCreatedDate(new Date());
    	dtoObstruct.setCreatedUserId(token.getSysUserId());
    	dtoObstruct.setCreatedGroupId(objUser.getVpsUserInfo().getSysGroupId());
    	dtoObstruct.setObstructedContent(obj.getReceivedObstructContent());
		obstructedDAO.saveObject(dtoObstruct.toModel());
		
		assignHandoverDAO.updateHandoverDateBuildNow(obj.getConstructionId());
    	id = assignHandoverDAO.updateAssignHandoverVuong(obj);
    	
    	return id;
    }
    
    
    
    public long updateAssignHandoverVtmd(AssignHandoverDTO obj, HttpServletRequest request) throws Exception{
//    	List<ConstructionTaskDTO> consTask = assignHandoverDAO.findConstructionTaskByConsIdVtmd(obj);
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
    	VpsUserToken token = (VpsUserToken) request.getSession().getAttribute("vpsUserToken");
    	Long id = null;
    	List<ObstructedDTO> obstruct = assignHandoverDAO.findObstructedByConsId(obj);
    	ConstructionDTO dto = assignHandoverDAO.getStatusByConsId(obj.getConstructionId());
    	List<GoodsPlanDTO> goods = assignHandoverDAO.findGoodsPlanByConsId(obj);
		if(goods.size()>0) {
    		for(GoodsPlanDTO good : goods) {
    			if(!good.getSignState().equals("1")) {
    				throw new Exception("Công trình đã được đặt vật tư sang phòng đầu tư.");
    			}
    		}
    	}
    	if(StringUtils.isNotEmpty(dto.getStatus())) {
    		if(dto.getStatus().equals("4")) {
//		    	if(consTask.size()>0) {
//		    		for(ConstructionTaskDTO cons : consTask) {
//		    			cons.setStatus("1");
//		    			constructionTaskDAO.updateObject(cons.toModel());
//		    		}
//		    	}
    			assignHandoverDAO.updateVTMDTaskWorkCons(obj.getConstructionId());
    	}
    	}
    	if(obstruct.size()>0) {
    		for(ObstructedDTO obs : obstruct) {
    			obs.setClosedDate(new Date());
    			obs.setObstructedState("2");
    			obs.setUpdatedDate(new Date());
    			obs.setUpdatedUserId(token.getSysUserId());
    			obs.setUpdatedGroupId(objUser.getVpsUserInfo().getSysGroupId());
    			obstructedDAO.updateObject(obs.toModel());
    		}
    	}
    	id = assignHandoverDAO.updateAssignHandoverVtmd(obj);
    	return id;
    }
    
    public long updateAssignHandoverVuongVtmd(AssignHandoverDTO obj, HttpServletRequest request) throws Exception{
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
//    	List<ConstructionTaskDTO> consTask = assignHandoverDAO.findConstructionTaskByConsIdVuongVtmd(obj);
    	Long id=null;
    	VpsUserToken token = (VpsUserToken) request.getSession().getAttribute("vpsUserToken");
    	ConstructionDTO dto = assignHandoverDAO.getStatusByConsId(obj.getConstructionId());
    	List<GoodsPlanDTO> goods = assignHandoverDAO.findGoodsPlanByConsId(obj);
		if(goods.size()>0) {
    		for(GoodsPlanDTO good : goods) {
    			if(!good.getSignState().equals("1")) {
    				throw new Exception("Công trình đã được đặt vật tư sang phòng đầu tư.");
    			}
    		}
    	}
    	if(StringUtils.isNotEmpty(dto.getStatus())) {
    		if(!dto.getStatus().equals("4")) {
    			assignHandoverDAO.updateVuongTaskWorkCons(obj.getConstructionId());
        	}
    	}		
		
		ObstructedDTO dtoObstruct = new ObstructedDTO();
    	dtoObstruct.setObstructedState("1");
    	dtoObstruct.setConstructionId(obj.getConstructionId());
    	dtoObstruct.setCreatedDate(new Date());
    	dtoObstruct.setCreatedUserId(token.getSysUserId());
    	dtoObstruct.setCreatedGroupId(objUser.getVpsUserInfo().getSysGroupId());
    	dtoObstruct.setObstructedContent(obj.getReceivedObstructContent());
		obstructedDAO.saveObject(dtoObstruct.toModel());
		
    	id = assignHandoverDAO.updateAssignHandoverVuongVtmd(obj);
    	return id;
    }
    
    public long updateAssignHandoverNotVuongVtmd(AssignHandoverDTO obj, HttpServletRequest request) throws Exception{
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
    	VpsUserToken token = (VpsUserToken) request.getSession().getAttribute("vpsUserToken");
    	Long id=null;
//    	List<ConstructionTaskDTO> consTask = assignHandoverDAO.findConstructionTaskByConsIdNotVuongVtmd(obj);
		List<ObstructedDTO> obstruct = assignHandoverDAO.findObstructedByConsId(obj);
		List<GoodsPlanDTO> goods = assignHandoverDAO.findGoodsPlanByConsId(obj);
		List<RequestGoodsDetailDTO> requestGoods = assignHandoverDAO.findRequestGoods(obj);
		ConstructionDTO dto = assignHandoverDAO.getStatusByConsId(obj.getConstructionId());
		if(goods.size()>0) {
    		for(GoodsPlanDTO good : goods) {
    			if(!good.getSignState().equals("1")) {
    				throw new Exception("Công trình đã được đặt vật tư sang phòng đầu tư.");
    			} else {
    				for(RequestGoodsDetailDTO requestGood : requestGoods) {
    					assignHandoverDAO.deleteRequestGoodsByConsId(requestGood.getRequestGoodsId());
        				assignHandoverDAO.deleteRequestGoodsDetailByConsId(requestGood.getRequestGoodsDetailId());
    				}
    				assignHandoverDAO.deleteGoodsPlanDetailByConsId(good.getGoodsPlanDetailId());
    			}
    		}
    	}
    	if(StringUtils.isNotEmpty(dto.getStatus())) {
    		if(dto.getStatus().equals("4")) {
    			assignHandoverDAO.updateHetVuongTaskWorkCons(obj.getConstructionId());
	    		if(obstruct.size()>0) {
	        		for(ObstructedDTO obs : obstruct) {
	        			obs.setClosedDate(new Date());
	        			obs.setObstructedState("2");
	        			obs.setUpdatedDate(new Date());
	        			obs.setUpdatedUserId(token.getSysUserId());
	        			obs.setUpdatedGroupId(objUser.getVpsUserInfo().getSysGroupId());
	        			obstructedDAO.updateObject(obs.toModel());
	        		}
	        	}
	    	}
		}
    	assignHandoverDAO.updateStatusHetVuong(obj.getConstructionId());
    	assignHandoverDAO.updateHandoverDateBuildNowStatus(obj.getConstructionId());
    	assignHandoverDAO.updateRpStationCompleteHandoverDateBuild(obj);
    	id = assignHandoverDAO.updateAssignHandoverNotVuongVtmd(obj);
    	return id;
    }
    
    public List<AssignHandoverDTO> doImportExcelTTKT(String filePath, Long sysUserId, HttpServletRequest request) {
    	String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.ASSIGN,
				Constant.AdResourceKey.TTKT_DV, request);
		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
		VpsUserToken token = (VpsUserToken) request.getSession().getAttribute("vpsUserToken");
        List<AssignHandoverDTO> dtoList = new ArrayList<>();
        List<ExcelErrorDTO> errorList = new ArrayList<>();

        try {
            File f = new File(filePath);
            ZipSecureFile.setMinInflateRatio(-1.0d);
            XSSFWorkbook workbook = new XSSFWorkbook(f);
            XSSFSheet sheet = workbook.getSheetAt(0);

            DataFormatter formatter = new DataFormatter();
            int rowCount = 0;
            
            List<ConstructionDetailDTO> consInfos = constructionDAO.getConstructionByStationId(new ConstructionDTO());
            HashMap<String, ConstructionDetailDTO> consInfoRef = new HashMap<>();
            if (consInfos != null) {
                consInfos.forEach(info -> consInfoRef.put(info.getCode().toUpperCase(), info));
            }
            List<AssignHandoverDTO> ref = assignHandoverDAO.findConstructionByAssignHandoverTable();
            HashMap<String, AssignHandoverDTO> mapConstructionRef = new HashMap<>();
            if (ref != null) {
                ref.forEach(r -> mapConstructionRef.put(r.getConstructionCode().toUpperCase(), r));
            }

            List<AssignHandoverDTO> sysGroupInfos = assignHandoverDAO.getListSysGroupCode();
            HashMap<String, AssignHandoverDTO> mapSysGroupRef = new HashMap<>();
            if (sysGroupInfos != null) {
                sysGroupInfos.forEach(r -> mapSysGroupRef.put(r.getSysGroupCode().toUpperCase(), r));
            }

            HashMap<String, String> mapSysGroupLst = new HashMap<>();
            if (groupIdList != null) {
            	groupIdList.forEach(r -> mapSysGroupLst.put(r,r));
            }
            
            AssignHandoverDTO activeConstructionCriteria = new AssignHandoverDTO();
            activeConstructionCriteria.setStatus(1L);
            List<AssignHandoverDTO> exist = assignHandoverDAO.doSearch(activeConstructionCriteria);
            HashMap<String, String> consCodeSysGroupMapRef = new HashMap<>();
            if (exist != null && !exist.isEmpty()) {
                exist.forEach(dto -> consCodeSysGroupMapRef.put(dto.getConstructionCode().toUpperCase(), dto.getSysGroupName()));
            }
            String sysGroupCodeImport = null;
            String sysGroupName = null;
            Long sysGroupId = null;
            for (Row row : sheet) {
                rowCount++;
                
                if (rowCount >=6 && !isRowEmpty(row)) {
                AssignHandoverDTO dtoValidated = new AssignHandoverDTO();
                String sysGroupCode = formatter.formatCellValue(row.getCell(1)).trim();
                String constructionCode = formatter.formatCellValue(row.getCell(2)).trim();

                int errorCol = 1;
                if (!StringUtils.isEmpty(sysGroupCode)) {
                	List<String> listRoleGroupCode = assignHandoverDAO.getCodeByGroupIdLst(groupIdList,sysGroupCode);
                	AssignHandoverDTO sysGroupInfo = mapSysGroupRef.get(sysGroupCode.toUpperCase());
                    if(listRoleGroupCode.size()<1) {
                    	errorList.add(this.createError(rowCount, errorCol, sysGroupCode + " chưa được phân quyền nhận BGMB "));
                    }else {
                    	if (sysGroupInfo != null) {
                    		String groupLst = mapSysGroupLst.get(sysGroupInfo.getSysGroupId().toString());
                    		if(StringUtils.isEmpty(groupLst)) {
                    			errorList.add(this.createError(rowCount, errorCol, sysGroupCode + " chưa được phân miền nhận BGMB "));
                    		} else {
                    			dtoValidated.setSysGroupCode(sysGroupInfo.getSysGroupCode());
                                dtoValidated.setSysGroupId(sysGroupInfo.getSysGroupId());
                                dtoValidated.setSysGroupName(sysGroupInfo.getSysGroupName());
                                sysGroupCodeImport=sysGroupInfo.getSysGroupCode();
                                sysGroupName=sysGroupInfo.getSysGroupName();
                                sysGroupId=sysGroupInfo.getSysGroupId();
                    		}
                        } else {
                            errorList.add(this.createError(rowCount, errorCol, "không tồn tại"));
                        }
                    }
                } else {
                    errorList.add(this.createError(rowCount, errorCol, "không được bỏ trống"));
                }

                errorCol = 2;
                if (!StringUtils.isEmpty(constructionCode)) {
                	AssignHandoverDTO constructionInfo = mapConstructionRef.get(constructionCode.toUpperCase());
                    if(constructionInfo!=null) {
//                    	 dtoValidated.setConstructionCode(constructionInfo.getConstructionCode());
//                         dtoValidated.setConstructionId(constructionInfo.getConstructionId());
//                         dtoValidated.setCntContractId(constructionInfo.getCntContractId());
//                         dtoValidated.setCntContractCode(constructionInfo.getCntContractCode());
//                         dtoValidated.setCatProvinceId(constructionInfo.getCatProvinceId());
//                         dtoValidated.setCatProvinceCode(constructionInfo.getCatProvinceCode());
//                         dtoValidated.setCatStationHouseId(constructionInfo.getCatStationHouseId());
//                         dtoValidated.setCatStationHouseCode(constructionInfo.getCatStationHouseCode());
//                         dtoValidated.setCatStationId(constructionInfo.getCatStationId());
//                         dtoValidated.setCatStationCode(constructionInfo.getCatStationCode());
//                         dtoValidated.setAssignHandoverId(constructionInfo.getAssignHandoverId());
//                         dtoValidated.setAreaId(constructionInfo.getAreaId());
//                         dtoValidated.setAreaCode(constructionInfo.getAreaCode());
                    	dtoValidated = constructionInfo;
                         dtoValidated.setIsDelivered("1");
                         dtoValidated.setSysGroupId(sysGroupId);
                         dtoValidated.setSysGroupCode(sysGroupCodeImport);
                         dtoValidated.setSysGroupName(sysGroupName);
//                         dtoValidated.setIsDesign(constructionInfo.getIsDesign());
//                         dtoValidated.setStatus(constructionInfo.getStatus());
//                         dtoValidated.setReceivedStatus(constructionInfo.getReceivedStatus());
//                         dtoValidated.setCreateDate(constructionInfo.getCreateDate());
//                         dtoValidated.setCreateUserId(constructionInfo.getCreateUserId());
//                         dtoValidated.setCompanyAssignDate(constructionInfo.getCompanyAssignDate());
                    } else {
                    	errorList.add(this.createError(rowCount, errorCol, " đã được giao hoặc không tồn tại"));
                    }

                } else {
                    errorList.add(this.createError(rowCount, errorCol, "không được bỏ trống"));
                }

                if (errorList.size() == 0) {
                	dtoValidated.setUpdateDate(new Date());
                	dtoValidated.setUpdateUserId(token.getSysUserId());
                    dtoList.add(dtoValidated);
                }
            }
        }
            if (errorList.size() > 0) {
                String filePathError = UEncrypt.encryptFileUploadPath(filePath);
                this.doWriteError(errorList, dtoList, filePathError, 3);
            }
            workbook.close();

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ExcelErrorDTO errorDTO = createError(0, 0, e.toString());
            errorList.add(errorDTO);
            String filePathError = null;

            try {
                filePathError = UEncrypt.encryptFileUploadPath(filePath);
            } catch (Exception ex) {
                LOGGER.error(e.getMessage(), e);
                errorDTO = createError(0, 0, ex.toString());
                errorList.add(errorDTO);
            }
            this.doWriteError(errorList, dtoList, filePathError, 3);
        }

        return dtoList;
    }
    
    public void addNewAssignHandoverImportTTKT(List<AssignHandoverDTO> dtos) throws Exception {
        for (AssignHandoverDTO dto : dtos) {
        	AssignHandoverDTO obj = assignHandoverDAO.getContractHouseByConsId(dto.getAssignHandoverId());
        	assignHandoverDAO.updateRpStationComplete(dto,obj.getCatStationHouseCode(),obj.getCntContractCode());
        	dto.setIsDelivered("1");
        	dto.setTtkvAssignDate(new Date());
            assignHandoverDAO.updateObject(dto.toModel());
        }
    }
    
    public String insertSmsemailImportTTKT(AssignHandoverDTO obj, HttpServletRequest request) {
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
    	VpsUserToken token = (VpsUserToken) request.getSession().getAttribute("vpsUserToken");
    	SendSmsEmailBO bo = new SendSmsEmailBO();
    	bo.setSubject("Thông báo nhận BGMB");
    	bo.setContent("TTKV giao cho TTKT nhận BGMB công trình " + obj.getConstructionCode());
    	bo.setReceivePhoneNumber(obj.getUserNumberPhone());
    	bo.setReceiveEmail(obj.getUserEmail());
    	bo.setStatus("0");
    	bo.setCreatedDate(new Date());
    	bo.setCreatedUserId(token.getSysUserId());
    	bo.setCreatedGroupId(objUser.getVpsUserInfo().getSysGroupId());
    	return sendSmsEmailDAO.save(bo);
    }
    
//    public List<WorkItemDTO> checkType(AssignHandoverDTO obj) {
//    	return assignHandoverDAO.checkByType(obj);
//    }
    
    public void checkWorkItemConsTask(AssignHandoverDTO obj, HttpServletRequest request) {
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
    	VpsUserToken token = (VpsUserToken) request.getSession().getAttribute("vpsUserToken");
    	List<WorkItemDTO> checkType = assignHandoverDAO.checkByType(obj);
    	WorkItemDTO internal = assignHandoverDAO.getIsInternal(obj);
    	String inter =null;
    	Long sysId =null;
    	Long constorId = null;
    	if(internal!=null) {
    		if(internal.getIsInternal()!=null) {
        		inter = internal.getIsInternal();
        	}
        	if(internal.getSupervisorId()!=null) {
        		sysId = internal.getSupervisorId();
        	}
        	if(internal.getConstructorId()!=null) {
        		constorId = internal.getConstructorId();
        	}
    	}
    	if(obj.getReceivedStatus()!=null) {
    		if(obj.getReceivedStatus()==4l || obj.getReceivedStatus()==5l) {
        		assignHandoverDAO.updateReceiveDate(obj.getAssignHandoverId());
        	}
    	}
    	
    	if(checkType.size()>0) {
    		for(WorkItemDTO id : checkType) {
    			if(obj.getLstType().size()!=1 && obj.getTypeSave().size()!=1) {
    				assignHandoverDAO.deleteWorkItem(id.getWorkItemId());
    				assignHandoverDAO.deleteConstructionTask(id.getWorkItemId());
    			}
//            	assignHandoverDAO.deleteConsTask(id.getWorkItemId(),id.getConstructionId());
    		}
    	}
    	
    	if(obj.getLstType().size()>0 && obj.getTypeSave().size()==0) {
    		List<WorkItemDTO> itemDTO = assignHandoverDAO.getWorkItemByCatTypeDelete(obj);
    		for(WorkItemDTO item : itemDTO) {
    			assignHandoverDAO.deleteWorkItemByConsId(obj.getConstructionId(), item.getCatWorkItemTypeId());
    			assignHandoverDAO.deleteConstructionTask(item.getWorkItemId());
    		}
    		assignHandoverDAO.updateRpStationCompleteNV(obj);
    		if(obj.getStationType()!=null) {
    			assignHandoverDAO.updateRpStationCompleteWhenAssign(obj);
    		}
    	} else {
    		if(obj.getLstType().size()==1) {
        		if(obj.getLstType().get(0).equals("13") || obj.getLstType().get(0).equals("14") || obj.getLstType().get(0).equals("15") 
        				|| obj.getLstType().get(0).equals("16")) {
            		List<WorkItemDTO> item = assignHandoverDAO.checkHaveCatWorkById(obj.getLstType().get(0),obj.getConstructionId());
            		if(item.size()==0) {
            			List<CatWorkItemTypeDTO> lstCatWork = assignHandoverDAO.getCatWorkItemTypeByType(obj.getTypeSave());
            	    	for(CatWorkItemTypeDTO dto : lstCatWork) {
            	    		WorkItemDTO work = new WorkItemDTO();
            	    		work.setConstructionId(obj.getConstructionId());
            	    		work.setCatWorkItemTypeId(dto.getCatWorkItemTypeId());
            	    		work.setCode(obj.getConstructionCode()+"_"+dto.getCode());
            	    		work.setName(dto.getName());
            	    		work.setIsInternal(inter);
            	    		work.setConstructorId(constorId);
            	    		work.setSupervisorId(sysId);
            	    		work.setStatus("1");
            	    		work.setCatWorkItemGroupId(dto.getCatWorkItemGroupId());
            	    		work.setCreatedDate(new Date());
            	    		work.setCreatedUserId(token.getSysUserId());
            	    		work.setCreatedGroupId(objUser.getVpsUserInfo().getSysGroupId());
            	    		workItemDAO.saveObject(work.toModel());
            	    	}
            		}
            	} else if(obj.getLstType().get(0).equals("17") || obj.getLstType().get(0).equals("18")) {
            		List<WorkItemDTO> item = assignHandoverDAO.checkHaveCatWorkById(obj.getLstType().get(0),obj.getConstructionId());
            		if(item.size()>0) {
            			List<WorkItemDTO> itemDTO = assignHandoverDAO.getWorkItemByCatTypeDelete(obj);
                		for(WorkItemDTO a : itemDTO) {
                			assignHandoverDAO.deleteWorkItemByConsId(obj.getConstructionId(), a.getCatWorkItemTypeId());
                			assignHandoverDAO.deleteConstructionTask(a.getWorkItemId());
                		}
            		}
//            			List<CatWorkItemTypeDTO> lstCatWork = assignHandoverDAO.getCatWorkItemTypeByType(obj.getTypeSave());
//            	    	for(CatWorkItemTypeDTO dto : lstCatWork) {
//            	    		WorkItemDTO work = new WorkItemDTO();
//            	    		work.setConstructionId(obj.getConstructionId());
//            	    		work.setCatWorkItemTypeId(dto.getCatWorkItemTypeId());
//            	    		work.setCode(obj.getConstructionCode()+"_"+dto.getCode());
//            	    		work.setName(dto.getName());
//            	    		work.setIsInternal(inter);
//            	    		work.setConstructorId(constorId);
//            	    		work.setSupervisorId(sysId);
//            	    		work.setStatus("1");
//            	    		work.setCatWorkItemGroupId(dto.getCatWorkItemGroupId());
//            	    		work.setCreatedDate(new Date());
//            	    		work.setCreatedUserId(token.getSysUserId());
//            	    		work.setCreatedGroupId(objUser.getVpsUserInfo().getSysGroupId());
//            	    		workItemDAO.saveObject(work.toModel());
//            	    	}
            	    	for(String a : obj.getTypeSave()) {
                			List<WorkItemDTO> itemId = assignHandoverDAO.checkHaveCatWorkById(a,obj.getConstructionId());
                			if(itemId.size()==0) {
                				List<String> array = new ArrayList<String>();
                				array.add(a);
                				List<CatWorkItemTypeDTO> lstCatWork = assignHandoverDAO.getCatWorkItemTypeByType(array);
                            	for(CatWorkItemTypeDTO dto : lstCatWork) {
                            		WorkItemDTO work = new WorkItemDTO();
                            		work.setConstructionId(obj.getConstructionId());
                            		work.setCatWorkItemTypeId(dto.getCatWorkItemTypeId());
                            		work.setCode(obj.getConstructionCode()+"_"+dto.getCode());
                            		work.setName(dto.getName());
                            		work.setIsInternal(inter);
                            		work.setConstructorId(constorId);
                            		work.setSupervisorId(sysId);
                            		work.setStatus("1");
                            		work.setCatWorkItemGroupId(dto.getCatWorkItemGroupId());
                            		work.setCreatedDate(new Date());
                            		work.setCreatedUserId(token.getSysUserId());
                            		work.setCreatedGroupId(objUser.getVpsUserInfo().getSysGroupId());
                            		workItemDAO.saveObject(work.toModel());
                            	}
                			}
                		}
            	}
        	} else {
        		List<WorkItemDTO> itemDTO = assignHandoverDAO.getWorkItemByCatTypeDelete(obj);
        		for(WorkItemDTO item : itemDTO) {
        			assignHandoverDAO.deleteWorkItemByConsId(obj.getConstructionId(), item.getCatWorkItemTypeId());
        			assignHandoverDAO.deleteConstructionTask(item.getWorkItemId());
        		}
        		for(String a : obj.getTypeSave()) {
        			List<WorkItemDTO> item = assignHandoverDAO.checkHaveCatWorkById(a,obj.getConstructionId());
        			if(item.size()==0) {
        				List<String> array = new ArrayList<String>();
        				array.add(a);
        				List<CatWorkItemTypeDTO> lstCatWork = assignHandoverDAO.getCatWorkItemTypeByType(array);
                    	for(CatWorkItemTypeDTO dto : lstCatWork) {
                    		WorkItemDTO work = new WorkItemDTO();
                    		work.setConstructionId(obj.getConstructionId());
                    		work.setCatWorkItemTypeId(dto.getCatWorkItemTypeId());
                    		work.setCode(obj.getConstructionCode()+"_"+dto.getCode());
                    		work.setName(dto.getName());
                    		work.setIsInternal(inter);
                    		work.setConstructorId(constorId);
                    		work.setSupervisorId(sysId);
                    		work.setStatus("1");
                    		work.setCatWorkItemGroupId(dto.getCatWorkItemGroupId());
                    		work.setCreatedDate(new Date());
                    		work.setCreatedUserId(token.getSysUserId());
                    		work.setCreatedGroupId(objUser.getVpsUserInfo().getSysGroupId());
                    		workItemDAO.saveObject(work.toModel());
                    	}
        			}
        		}
        		assignHandoverDAO.updateRpStationCompleteWhenAssign(obj);
        	}
    	}
    }
    
    public void updateWorkItem(AssignHandoverDTO obj, HttpServletRequest request) {
    	VpsUserToken token = (VpsUserToken) request.getSession().getAttribute("vpsUserToken");
//    	AssignHandoverDTO dto = assignHandoverDAO.findById(obj.getAssignHandoverId());
    	obj.setUpdateDate(new Date());
    	obj.setUpdateUserId(token.getSysUserId());
//    	assignHandoverDAO.updateUserId(token.getSysUserId());
    	assignHandoverDAO.updateObject(obj.toModel());
    }
    
    public List<AssignHandoverDTO> checkStationBGMB(AssignHandoverDTO list){
    	List<AssignHandoverDTO> lst = new ArrayList<AssignHandoverDTO>();
    	List<String> lstHouseCode = new ArrayList<String>();
    	List<String> lstContractCode = new ArrayList<String>();
    	for(AssignHandoverDTO obj : list.getLstAssignId()) {
    		lstHouseCode.add(obj.getCatStationHouseCode());
    		lstContractCode.add(obj.getCntContractCode());
    	}
    	lst = assignHandoverDAO.checkStationBGMB(lstHouseCode,lstContractCode);
    	return lst;
    }
    
    public String downloadTemplateBGMB() throws Exception {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String fileName = "Import_giao_viec_cho_NV.xlsx";
        String filePath = classloader.getResource("../" + "doc-template").getPath() + fileName;
        InputStream file = new BufferedInputStream(new FileInputStream(filePath));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        file.close();
        Calendar cal = Calendar.getInstance();
        String uploadPath = folderUpload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
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

        workbook.write(outFile);
        workbook.close();
        outFile.close();
        String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + fileName);
        return path;
    }
    
    public List<AssignHandoverDTO> doImportExcelBGMB(String filePath, Long sysUserId, HttpServletRequest request) {
    	String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.ASSIGN,
				Constant.AdResourceKey.TASK, request);
		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
		VpsUserToken token = (VpsUserToken) request.getSession().getAttribute("vpsUserToken");
        List<AssignHandoverDTO> dtoList = new ArrayList<>();
        List<ExcelErrorDTO> errorList = new ArrayList<>();
        try {
            File f = new File(filePath);
            ZipSecureFile.setMinInflateRatio(-1.0d);
            XSSFWorkbook workbook = new XSSFWorkbook(f);
            XSSFSheet sheet = workbook.getSheetAt(0);

            HashMap<String, String> mapSysGroupLst = new HashMap<>();
            if (groupIdList != null) {
            	groupIdList.forEach(r -> mapSysGroupLst.put(r,r));
            }
            
            DataFormatter formatter = new DataFormatter();
            int rowCount = 0;
            for (Row row : sheet) {
                rowCount++;
                if (rowCount >=2 && !isRowEmpty(row)) {
                AssignHandoverDTO dtoValidated = new AssignHandoverDTO();
                String constructionCode = formatter.formatCellValue(row.getCell(1)).trim();
                String email = formatter.formatCellValue(row.getCell(2)).trim();
                int errorCol = 1;
                if (!StringUtils.isEmpty(constructionCode)) {
                	AssignHandoverDTO station = assignHandoverDAO.findStationContractByConstructionCode(constructionCode);
                	if(station!=null) {
                		String groupLst = mapSysGroupLst.get(station.getSysGroupId().toString());
                		if(StringUtils.isEmpty(groupLst)) {
                        	errorList.add(this.createErrorBGMB(rowCount, errorCol, "không được giao nhận BGMB (Đơn vị không có quyền giao nhận)"));
                        } else if(station.getStatus()==1l) {
                        	dtoValidated = station;
                    	} else {
                    		errorList.add(this.createErrorBGMB(rowCount, errorCol, "đã hết hiệu lực"));
                    	}
                	} else {
                		errorList.add(this.createErrorBGMB(rowCount, errorCol, "không tồn tại"));
                	}
                } else {
                    errorList.add(this.createErrorBGMB(rowCount, errorCol, "không được bỏ trống"));
                }

                errorCol = 2;
                if (!StringUtils.isEmpty(email)) {
                	String[] arr = new String[] {};
                	arr = email.split("@");
                	if(arr.length==2) {
                		List<AssignHandoverDTO> checkRoleUser = assignHandoverDAO.checkGroupByRole(email, groupIdList);
                    	if(checkRoleUser.size()>0) {
                    		for(AssignHandoverDTO dtoRole : checkRoleUser) {
                    			dtoValidated.setPerformentId(dtoRole.getSysUserId());
                    			dtoValidated.setPhoneNumber(dtoRole.getPhoneNumber());
                    			dtoValidated.setEmail(dtoRole.getEmail());
                    		}
                    	} else {
                    		errorList.add(this.createErrorBGMB(rowCount, errorCol, "không có quyền nhận BGMB"));
                    	}
                	} else {
                		List<SysUserDTO> ref = assignHandoverDAO.getAllSysUser(groupIdList);
                        for(SysUserDTO user : ref) {
                        	user.setEmailName(user.getEmail().split("@")[0]);
                        }
                        
                        HashMap<String, SysUserDTO> mapEmployeeCode = new HashMap<>();
                        if (ref != null) {
                            ref.forEach(r -> mapEmployeeCode.put(r.getEmployeeCode().toUpperCase(), r));
                        }
                        
                        HashMap<String, SysUserDTO> mapEmailName = new HashMap<>();
                        if (ref != null) {
                        	ref.forEach(r -> mapEmailName.put(r.getEmailName().toUpperCase(), r));
                        }
                		SysUserDTO employeeCode = mapEmployeeCode.get(email.toUpperCase());
                		SysUserDTO employeeEmail = mapEmailName.get(email.toUpperCase());
                		if(employeeCode==null && employeeEmail==null) {
                			errorList.add(this.createErrorBGMB(rowCount, errorCol, "không có quyền nhận BGMB"));
                		} else {
                			SysUserDTO userDetail = new SysUserDTO();
                			if(employeeCode!=null && employeeEmail==null) {
                				userDetail = assignHandoverDAO.getSysUserById(employeeCode.getEmployeeCode(), null);
                			} else if (employeeEmail!=null && employeeCode==null) {
                				String name = employeeEmail.getEmailName() + "@viettel.com.vn";
                				userDetail = assignHandoverDAO.getSysUserById(null, name);
                			} else {
                				String name = employeeEmail.getEmailName() + "@viettel.com.vn";
                				userDetail = assignHandoverDAO.getSysUserById(employeeCode.getEmployeeCode(), name);
                			}
                			List<AssignHandoverDTO> checkRoleUserCode = assignHandoverDAO.checkGroupByRole(userDetail.getEmail(), groupIdList);
                			if(checkRoleUserCode.size()>0) {
                        		for(AssignHandoverDTO dtoRole : checkRoleUserCode) {
                        			dtoValidated.setPerformentId(dtoRole.getSysUserId());
                        			dtoValidated.setPhoneNumber(dtoRole.getPhoneNumber());
                        			dtoValidated.setEmail(dtoRole.getEmail());
                        		}
                        	} else {
                        		errorList.add(this.createErrorBGMB(rowCount, errorCol, "không có quyền nhận BGMB"));
                        	}
                		}
                	}
                } else {
                    errorList.add(this.createErrorBGMB(rowCount, errorCol, "không được bỏ trống"));
                }

                if (errorList.size() == 0) {
                	dtoValidated.setDepartmentAssignDate(new Date());
                	dtoValidated.setReceivedStatus(1l);
                    dtoList.add(dtoValidated);
                }
            }
        }
            if (errorList.size() > 0) {
                String filePathError = UEncrypt.encryptFileUploadPath(filePath);
                this.doWriteError(errorList, dtoList, filePathError, 3);
            }
            workbook.close();

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ExcelErrorDTO errorDTO = createErrorBGMB(0, 0, e.toString());
            errorList.add(errorDTO);
            String filePathError = null;

            try {
                filePathError = UEncrypt.encryptFileUploadPath(filePath);
            } catch (Exception ex) {
                LOGGER.error(e.getMessage(), e);
                errorDTO = createErrorBGMB(0, 0, ex.toString());
                errorList.add(errorDTO);
            }
            this.doWriteError(errorList, dtoList, filePathError, 3);
        }

        return dtoList;
    }
    
    public void updateAssignHandoverImportBGMB(List<AssignHandoverDTO> dtos) throws Exception {
        for (AssignHandoverDTO dto : dtos) {
//        	dto.setIsDelivered("1");
//        	dto.setTtkvAssignDate(new Date());
            assignHandoverDAO.updateObject(dto.toModel());
        }
    }
    
    public String insertSmsemailImportBGMB(AssignHandoverDTO obj, HttpServletRequest request) {
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
    	VpsUserToken token = (VpsUserToken) request.getSession().getAttribute("vpsUserToken");
    	SendSmsEmailBO bo = new SendSmsEmailBO();
    	bo.setSubject("Thông báo nhận BGMB");
    	bo.setContent("Bạn được giao nhận BGMB công trình " + obj.getConstructionCode());
    	bo.setReceivePhoneNumber(obj.getPhoneNumber());
    	bo.setReceiveEmail(obj.getEmail());
    	bo.setStatus("1");
    	bo.setCreatedDate(new Date());
    	bo.setCreatedUserId(token.getSysUserId());
    	bo.setCreatedGroupId(objUser.getVpsUserInfo().getSysGroupId());
    	return sendSmsEmailDAO.save(bo);
    }
    
    public String exportAssignHandoverCN(AssignHandoverDTO obj) throws Exception {
		obj.setPage(null);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "GiaoViecChoCN.xlsx"));
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		file.close();
		Calendar cal = Calendar.getInstance();
		String uploadPath = folderUpload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
				+ File.separator + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1)
				+ File.separator + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
		String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload) + File.separator
				+ cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator
				+ cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
		File udir = new File(uploadPath);
		if (!udir.exists()) {
			udir.mkdirs();
		}
		OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "GiaoViecChoCN.xlsx");
		List<AssignHandoverDTO> data = assignHandoverDAO.getAllSysGroup();
		XSSFSheet sheet0 = workbook.getSheetAt(0);
		
		XSSFSheet sheet = workbook.getSheetAt(1);
		if (data != null && !data.isEmpty()) {
			int i = 1;
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			for (AssignHandoverDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 1));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getSysGroupCode() != null) ? dto.getSysGroupCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getSysGroupName() != null) ? dto.getSysGroupName() : "");
				cell.setCellStyle(style);
			}
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "GiaoViecChoCN.xlsx");
		return path;
	}
    
    public String exportAssignHandoverTTKT(AssignHandoverDTO obj) throws Exception {
		obj.setPage(null);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "GiaoViecChoTTKT.xlsx"));
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		file.close();
		Calendar cal = Calendar.getInstance();
		String uploadPath = folderUpload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
				+ File.separator + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1)
				+ File.separator + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
		String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload) + File.separator
				+ cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator
				+ cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
		File udir = new File(uploadPath);
		if (!udir.exists()) {
			udir.mkdirs();
		}
		OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "GiaoViecChoTTKT.xlsx");
		List<AssignHandoverDTO> data = assignHandoverDAO.getAllSysGroup();
		XSSFSheet sheet0 = workbook.getSheetAt(0);
		XSSFSheet sheet = workbook.getSheetAt(1);
		if (data != null && !data.isEmpty()) {
			int i = 1;
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			for (AssignHandoverDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 1));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getSysGroupCode() != null) ? dto.getSysGroupCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getSysGroupName() != null) ? dto.getSysGroupName() : "");
				cell.setCellStyle(style);
			}
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "GiaoViecChoTTKT.xlsx");
		return path;
	}
    
    public List<GoodsPlanDTO> findSignStateGoodsPlanByConsId(Long id){
    	return assignHandoverDAO.findSignStateGoodsPlanByConsId(id);
    }
    
    public void updateConstructionTuyen(AssignHandoverDTO obj, HttpServletRequest request) {
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
    	assignHandoverDAO.updateConstructionTuyen(obj,objUser.getVpsUserInfo().getSysUserId());
    }

    //hienvd: created 3/7/2019
    public DataListDTO doSearchKPI(AssignHandoverDTO criteria) {
        UtilAttachDocumentDTO objFile = null;
        List<AssignHandoverDTO> dtos = assignHandoverDAO.doSearchKPI(criteria);
        DataListDTO dataListDTO = new DataListDTO();
        dataListDTO.setData(dtos);
        dataListDTO.setTotal(criteria.getTotalRecord());
        dataListDTO.setSize(criteria.getPageSize());
        dataListDTO.setStart(1);
        return dataListDTO;
    }

    //hienvd: end
    //Huypq-20190828-start
    public DataListDTO getDataReportStartInMonth(AssignHandoverDTO criteria) {
        List<AssignHandoverDTO> dtos = assignHandoverDAO.getDataReportStartInMonth(criteria);
        DataListDTO dataListDTO = new DataListDTO();
        dataListDTO.setData(dtos);
        dataListDTO.setTotal(criteria.getTotalRecord());
        dataListDTO.setSize(criteria.getPageSize());
        dataListDTO.setStart(1);
        return dataListDTO;
    }
    //Huy-end
}
