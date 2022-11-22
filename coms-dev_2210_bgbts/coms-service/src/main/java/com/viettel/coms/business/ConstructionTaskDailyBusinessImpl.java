package com.viettel.coms.business;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
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
import com.viettel.coms.bo.ConstructionTaskDailyBO;
import com.viettel.coms.dao.ConstructionDAO;
import com.viettel.coms.dao.ConstructionTaskDAO;
import com.viettel.coms.dao.ConstructionTaskDailyDAO;
import com.viettel.coms.dao.QuantityConstructionDAO;
import com.viettel.coms.dao.RpQuantityDAO;
import com.viettel.coms.dao.UtilAttachDocumentDAO;
import com.viettel.coms.dao.WorkItemDAO;
import com.viettel.coms.dto.ConstructionDetailDTO;
import com.viettel.coms.dto.ConstructionTaskDTO;
import com.viettel.coms.dto.ConstructionTaskDailyDTO;
import com.viettel.coms.dto.DetailMonthPlanDTO;
import com.viettel.coms.dto.ExcelErrorDTO;
import com.viettel.coms.dto.RpQuantityDTO;
import com.viettel.coms.dto.UtilAttachDocumentDTO;
import com.viettel.coms.dto.WorkItemDTO;
import com.viettel.coms.dto.WorkItemDetailDTO;
import com.viettel.coms.utils.ExcelUtils;
import com.viettel.coms.utils.PermissionUtils;
import com.viettel.erp.dto.SysUserDTO;
import com.viettel.ktts.vps.VpsPermissionChecker;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.utils.Constant;
import com.viettel.utils.ImageUtil;
import com.viettel.wms.utils.ValidateUtils;
import com.viettel.utils.ConvertData;

@Service("constructionTaskDailyBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ConstructionTaskDailyBusinessImpl
        extends BaseFWBusinessImpl<ConstructionTaskDailyDAO, ConstructionTaskDailyDTO, ConstructionTaskDailyBO>
        implements ConstructionTaskDailyBusiness {

    @Autowired
    private ConstructionTaskDailyDAO constructionTaskDailyDAO;
    @Autowired
    UtilAttachDocumentDAO utilAttachDocumentDAO;
    @Autowired
    WorkItemDAO workItemDAO;
    @Autowired
    QuantityConstructionDAO quantityConstructionDAO;
    
    @Autowired
	private ConstructionDAO constructionDAO;
    
    @Autowired
	private ConstructionTaskDAO constructionTaskDAO;
    
    @Autowired
	private RpQuantityDAO rpQuantityDAO;
    
    static Logger LOGGER = LoggerFactory.getLogger(ConstructionTaskDailyBusinessImpl.class);
    
    @Value("${folder_upload2}")
    private String folder2Upload;
    @Value("${default_sub_folder_upload}")
    private String defaultSubFolderUpload;

    public ConstructionTaskDailyBusinessImpl() {
        tModel = new ConstructionTaskDailyBO();
        tDAO = constructionTaskDailyDAO;
    }

    @Override
    public ConstructionTaskDailyDAO gettDAO() {
        return constructionTaskDailyDAO;
    }

    @Override
    public long count() {
        return constructionTaskDailyDAO.count("ConstructionTaskDailyBO", null);
    }
    /**Hoangnh start 15022019**/
    public Long updateConstructionTaskDaily(ConstructionTaskDailyDTO req){
    	Long id = 0L;
    	id = constructionTaskDailyDAO.updateObject(req.toModel());
    	return id;
    }
    
    public Long saveConstructionTaskDaily(ConstructionTaskDailyDTO req){
    	Long id = 0L;
    	id = constructionTaskDailyDAO.saveObject(req.toModel());
    	return id;
    }
    
    public DataListDTO doSearch(ConstructionTaskDailyDTO obj,HttpServletRequest request) {
//    	hoanm1_20190528_start
    	List<ConstructionTaskDailyDTO> ls= new ArrayList<ConstructionTaskDailyDTO>();
    	List<String> groupIdList = PermissionUtils.getListIdInDomainData(Constant.OperationKey.VIEW,
                Constant.AdResourceKey.VIEW_QUANTITY_DAILY, request);
        if (groupIdList != null && !groupIdList.isEmpty()) {
        	ls = constructionTaskDailyDAO.doSearch(obj,groupIdList);
        }
//        hoanm1_20190528_end
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }
    
    public Object getListImage(ConstructionTaskDailyDTO obj) throws Exception {
        if (obj.getConstructionTaskId() != null) {
            UtilAttachDocumentDTO file = new UtilAttachDocumentDTO();
            file.setType("44");
            file.setObjectId(obj.getConstructionTaskDailyId());
            List<UtilAttachDocumentDTO> listImage = utilAttachDocumentDAO.doSearch(file);
            if (listImage != null && !listImage.isEmpty()) {
                for (UtilAttachDocumentDTO dto : listImage) {
                    dto.setBase64String(ImageUtil.convertImageToBase64(folder2Upload + (dto.getFilePath())));
                    dto.setFilePath(UEncrypt.encryptFileUploadPath(dto.getFilePath()));
                }
            }
            obj.setListImage(listImage);
        }
        return obj;
    }
    
    public void rejectQuantityByDay(ConstructionTaskDailyDTO obj, HttpServletRequest request) {
    	obj.setConfirm("2");
    	quantityConstructionDAO.updateConstructionTaskDaily(obj, false);
        updateCompletePercentTask(obj);
    }
    
    private void updateCompletePercentTask(ConstructionTaskDailyDTO obj) {
        ConstructionTaskDTO criteria = new ConstructionTaskDTO();
        criteria.setStatus(obj.getStatusConstructionTask());
        criteria.setConstructionTaskId(obj.getConstructionTaskId());
        criteria.setAmount(obj.getAmountConstruction());
        criteria.setCatTaskId(obj.getCatTaskId());
        criteria.setWorkItemId(obj.getWorkItemId());
        criteria.setPath(obj.getPath());
        int sttUpdate = constructionTaskDailyDAO.updateConstructionTask(criteria);
        if (constructionTaskDailyDAO.getCompletePercent() < 100 && sttUpdate != 0) {
//			case wi hoan thanh
            if (StringUtils.isNotEmpty(obj.getStatus()) && "3".equals(obj.getStatus())) {
                obj.setStatus("2");
                sttUpdate = constructionTaskDailyDAO.updateStatusWorkItem(obj);
            }
            if ("5".equals(obj.getStatusConstruction())) {
                obj.setStatusConstruction("3");
                workItemDAO.updateStatusConstruction(obj.getConstructionId(), obj.getStatusConstruction());
            }
        }
    }
    
    public void cancelApproveQuantityByDay(ConstructionTaskDailyDTO obj, HttpServletRequest request) {
        obj.setConfirm("2");
          quantityConstructionDAO.updateConstructionTaskDaily(obj, false);
        if ("3".equalsIgnoreCase(obj.getStatus())) {
            reCalculateValueWorkItemAndConstruction(obj);
        }
        updateCompletePercentTask(obj);
    }
    
    private void reCalculateValueWorkItemAndConstruction(ConstructionTaskDailyDTO obj) {
    	constructionTaskDailyDAO.approveQuantityWorkItem(obj.getWorkItemId());
    	constructionTaskDailyDAO.recalculateValueConstruction(obj.getConstructionId());
    }
    
    public void approveQuantityByDay(ConstructionTaskDailyDTO obj, HttpServletRequest request) {
        KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        Long userId = objUser.getVpsUserInfo().getSysUserId();
        obj.setUpdatedUserId(userId);
        obj.setApproveUserId(userId);
        obj.setConfirm("1");
        obj.setQuantity(obj.getQuantity()*1000000);
        obj.setApproveDate(new Date());
        constructionTaskDailyDAO.update(obj.toModel());
        if ("3".equalsIgnoreCase(obj.getStatus())) {
            reCalculateValueWorkItemAndConstruction(obj);
        }
        updateCompletePercentTask(obj);
    }
    
    public String exportConstructionTaskDaily(ConstructionTaskDailyDTO obj, HttpServletRequest request) throws Exception {
        obj.setPage(1L);
        obj.setPageSize(null);
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();
        InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Export_sanluong_ngay.xlsx"));
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
                udir.getAbsolutePath() + File.separator + "Export_sanluong_ngay.xlsx");
        List<String> provinceListId = PermissionUtils.getListIdInDomainData(Constant.OperationKey.VIEW,
                Constant.AdResourceKey.VIEW_QUANTITY_DAILY, request);
        List<ConstructionTaskDailyDTO> data = new ArrayList<ConstructionTaskDailyDTO>();
        if (provinceListId != null && !provinceListId.isEmpty())
            data = constructionTaskDailyDAO.doSearch(obj,provinceListId);
        XSSFSheet sheet = workbook.getSheetAt(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd");
        if (data != null && !data.isEmpty()) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet);
            // HuyPQ-17/08/2018-edit-start
            XSSFCellStyle styleNumber = ExcelUtils.styleNumber(sheet);
            styleNumber.setDataFormat(workbook.createDataFormat().getFormat("#,##0.000"));

            XSSFCellStyle styleDate = ExcelUtils.styleDate(sheet);
            XSSFCreationHelper createHelper = workbook.getCreationHelper();
            styleDate.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
            // HuyPQ-17/08/2018-edit-end

            styleNumber.setAlignment(HorizontalAlignment.RIGHT);
            styleDate.setAlignment(HorizontalAlignment.CENTER);
            int i = 2;
            for (ConstructionTaskDailyDTO dto : data) {
                Row row = sheet.createRow(i++);
                Cell cell = row.createCell(0, CellType.STRING);
                cell.setCellValue("" + (i - 2));
                cell.setCellStyle(styleNumber);
                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue((dto.getCreatedDate() != null) ? dto.getCreatedDate() : null);
                cell.setCellStyle(styleDate);
                
                cell = row.createCell(2, CellType.STRING);
                cell.setCellValue((dto.getSysGroupName() != null) ? dto.getSysGroupName() : "");
                cell.setCellStyle(style);
                
                cell = row.createCell(3, CellType.STRING);
                cell.setCellValue((dto.getCatStationCode() != null) ? dto.getCatStationCode() : "");
                cell.setCellStyle(style);

                cell = row.createCell(4, CellType.STRING);
                cell.setCellValue((dto.getConstructionCode() != null) ? dto.getConstructionCode() : "");
                cell.setCellStyle(style);

                cell = row.createCell(5, CellType.STRING);
                cell.setCellValue((dto.getWorkItemName() != null) ? dto.getWorkItemName() : "");
                cell.setCellStyle(style);

                cell = row.createCell(6, CellType.STRING);
                cell.setCellValue((dto.getTaskName() != null) ? dto.getTaskName() : "");
                cell.setCellStyle(style);
                
                cell = row.createCell(7, CellType.STRING);
                cell.setCellValue((dto.getFullName() != null) ? dto.getFullName() : "");
                cell.setCellStyle(style);
                
                cell = row.createCell(8, CellType.NUMERIC);
                cell.setCellValue((dto.getAmount() != null) ? dto.getAmount() : 0);
                cell.setCellStyle(styleNumber);

                cell = row.createCell(9, CellType.NUMERIC);
                cell.setCellValue((dto.getQuantity() != null) ? dto.getQuantity() : 0);
                cell.setCellStyle(styleNumber);
                
                cell = row.createCell(10, CellType.STRING);
                cell.setCellValue(getStringForStatus(dto.getConfirm()));
                cell.setCellStyle(style);
            }
        }
        workbook.write(outFile);
        workbook.close();
        outFile.close();

        String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "Export_sanluong_ngay.xlsx");
        return path;
    }
    private String getStringForStatus(String status) {
        if ("1".equals(status)) {
            return "Đã xác nhận";
        } else if ("2".equals(status)) {
            return "Đã từ chối";
        } else if ("0".equals(status)) {
            return "Chưa xác nhận";
        }
        return null;
    }
    
    public void updateConstructionTask(ConstructionTaskDailyDTO req){
    	 constructionTaskDailyDAO.updateConstructionTask(req);
    }
    
    public void updateInformation(ConstructionTaskDailyDTO req){
    	constructionTaskDailyDAO.updateInformation(req);
    }
    /**Hoangnh end 15022019**/
    
    //HuyPq-20191009-start
    public DataListDTO doSearchCompleteManage(WorkItemDTO obj,HttpServletRequest request) {
    	 String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
                 Constant.AdResourceKey.MONTHLY_DETAIL_PLAN, request);
         List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
         
    	List<WorkItemDTO> ls = constructionTaskDailyDAO.doSearchCompleteManage(obj,groupIdList);
    	DataListDTO data = new DataListDTO();
    	data.setData(ls);
    	data.setSize(obj.getPageSize());
    	data.setStart(1);
    	data.setTotal(obj.getTotalRecord());
    	return data;
    }
    
    int[] validateCol = { 1, 2, 3, 4};
	int[] constructionValidateCol = { 1, 2, 3, 4};
	
	HashMap<Integer, String> colName = new HashMap();
	{
		colName.put(1, "Mã công trình");
		colName.put(2, "Tên hạng mục");
		colName.put(3, "Người thực hiện");
		colName.put(4, "Giá trị");
	}
	
	HashMap<Integer, String> colAlias = new HashMap();
	{
		colAlias.put(1, "B");
		colAlias.put(2, "C");
		colAlias.put(3, "D");
		colAlias.put(4, "E");
		
	}
    
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
			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK
					&& !StringUtils.isBlank(cell.toString())) {
				return false;
			}
		}
		return true;
	}
    
    public boolean validateString(String str) {
		if(StringUtils.isNotBlank(str)) {
			return true;
		} else {
			return false;
		}
	}
    
    public boolean validateRequiredCell(Row row, List<ExcelErrorDTO> errorList) {
		DataFormatter formatter = new DataFormatter();
		boolean result = true;
		for (int colIndex : validateCol) {
			if (!validateString(formatter.formatCellValue(row.getCell(colIndex)))) {
				ExcelErrorDTO errorDTO = new ExcelErrorDTO();
				errorDTO.setColumnError(colAlias.get(colIndex));
				errorDTO.setLineError(String.valueOf(row.getRowNum() + 1));
				errorDTO.setDetailError(colName.get(colIndex) + " chưa nhập");
				errorList.add(errorDTO);
				result = false;
			}

		}
		return result;
	}

	private ExcelErrorDTO createError(int row, String column, String detail) {
		ExcelErrorDTO err = new ExcelErrorDTO();
		err.setColumnError(column);
		err.setLineError(String.valueOf(row));
		err.setDetailError(detail);
		return err;
	}
    
    public List<WorkItemDTO> importCompleteWorkItem(String fileInput, String typeImport) throws Exception {
		List<WorkItemDTO> workLst = Lists.newArrayList();
		List<ExcelErrorDTO> errorList = new ArrayList<ExcelErrorDTO>();
		String error = "";

		try {
			File f = new File(fileInput);
			ZipSecureFile.setMinInflateRatio(-1.0d);
			
			XSSFWorkbook workbook = new XSSFWorkbook(f);
			XSSFSheet sheet = workbook.getSheetAt(0);

			DataFormatter formatter = new DataFormatter();
			int count = 0;

			int countData = 0;
			List<String> lstSysUser = new ArrayList<>();
			List<String> lstConsCode = new ArrayList<>();
			List<String> lstWorkItem = new ArrayList<>();
			for (Row row : sheet) {
				countData++;
				if (countData >= 3 && !ValidateUtils.checkIfRowIsEmpty(row)) {
//					continue;
//				if (countData >= 3) {
					if(typeImport.equals("1")) {
						lstWorkItem.add(formatter.formatCellValue(row.getCell(2)).toUpperCase().trim());
						lstSysUser.add(formatter.formatCellValue(row.getCell(3)).toString().toUpperCase().trim());
					} else {
						lstSysUser.add(formatter.formatCellValue(row.getCell(2)).toString().toUpperCase().trim());
					}
					lstConsCode.add(formatter.formatCellValue(row.getCell(1)).toUpperCase().trim());
//				}
				}
			}
			
			HashMap<String, String> userMapLogin = new HashMap<String, String>();
			HashMap<String, String> userMapEmail = new HashMap<String, String>();
			List<SysUserDTO> lstUser = constructionTaskDAO.getListUser(lstSysUser);
			for (SysUserDTO sys : lstUser) {
				userMapLogin.put(sys.getLoginName().toUpperCase().trim(), sys.getUserId().toString());
				userMapEmail.put(sys.getEmail().toUpperCase().trim(), sys.getUserId().toString());
			}
			
			HashMap<String, String> constructionMap = new HashMap<String, String>();
			List<ConstructionDetailDTO> constructionLst = constructionDAO.getConstructionForImportTask(lstConsCode);
			for (ConstructionDetailDTO obj : constructionLst) {
				constructionMap.put(obj.getCode().toUpperCase(), obj.getConstructionId().toString());
			}
			
			HashMap<String, String> workItemMap = new HashMap<String, String>();
			HashMap<String, String> workItemMap2 = new HashMap<String, String>();
			if(typeImport.equals("1")) {
				List<WorkItemDetailDTO> workItemLst = workItemDAO.getWorkItemByName(1l, 1l, lstWorkItem);
				for (WorkItemDetailDTO obj : workItemLst) {
					if (obj.getConstructionCode() != null && obj.getName() != null) {
						workItemMap2.put(obj.getName().toUpperCase().trim(), obj.getName());
						workItemMap.put(
								obj.getConstructionCode().toUpperCase().trim() + "|" + obj.getName().toUpperCase().trim(),
								String.valueOf(obj.getWorkItemId()));
					}
	
				}
				}
			for (Row row : sheet) {
				count++;
				if (count >= 3 && checkIfRowIsEmpty(row))
					continue;
				WorkItemDTO obj = new WorkItemDTO();
				if (count >= 3) {
//					if (!validateRequiredCell(row, errorList))
//						continue;
					// kiểm tra các ô bắt buộc nhập đã dc nhập chưa

					String constructionCode = formatter.formatCellValue(row.getCell(1)).trim();
					String workItemName = null;
					String performer = null;
					String quantity = null;
					if(typeImport.equals("1")) {
						workItemName = formatter.formatCellValue(row.getCell(2)).toString().trim();
						performer = formatter.formatCellValue(row.getCell(3)).toString().toUpperCase().trim();
						quantity = formatter.formatCellValue(row.getCell(4)).toString().toUpperCase().trim();
					} else {
						performer = formatter.formatCellValue(row.getCell(2)).toString().toUpperCase().trim();
						quantity = formatter.formatCellValue(row.getCell(3)).toString().toUpperCase().trim();
					}
					

					if (validateString(constructionCode)) {
						String consCode = null;
						consCode = constructionMap.get(constructionCode.toUpperCase());
						if(consCode==null) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(1),
									colName.get(1) + " không tồn tại");
							errorList.add(errorDTO);
						} else {
							obj.setConstructionCode(constructionCode);
							obj.setConstructionId(Long.parseLong(consCode));
						}
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(1),
								colName.get(1) + " không được để trống");
						errorList.add(errorDTO);
					}

					if(typeImport.equals("1")) {
						if (validateString(workItemName)) {
							String workItem = null;
							String workItemCons = null;
							workItem = workItemMap2.get(workItemName.toUpperCase().trim());
							workItemCons = workItemMap.get(constructionCode.toUpperCase().trim() 
									+ "|" + workItemName.toUpperCase().trim());
							if(workItem==null) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(2),
										colName.get(2) + " không tồn tại");
								errorList.add(errorDTO);
							} else {
								if(workItemCons==null) {
									ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(2),
											colName.get(2) + " không thuộc công trình " + constructionCode);
									errorList.add(errorDTO);
								} else {
									obj.setWorkItemName(workItemName);
									obj.setWorkItemId(Long.parseLong(workItemCons));
								}
							}
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(2),
									colName.get(2) + " không được để trống");
							errorList.add(errorDTO);
						}
						
						if (validateString(performer)) {
							String userId = null;
							userId = userMapLogin.get(performer.toUpperCase().trim());
							if (userId == null) {
								userId = userMapEmail.get(performer.toUpperCase().trim());
								if(userId==null) {
									ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(3),
											colName.get(3) + " không tồn tại");
									errorList.add(errorDTO);
								} else {
									obj.setPerformerId(Long.parseLong(userId));
								}
							} else {
								obj.setPerformerId(Long.parseLong(userId));
							}
							
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(3),
									colName.get(3) + " không được để trống");
							errorList.add(errorDTO);
						}
						
						if (validateString(quantity)) {
							obj.setQuantity(Double.valueOf(quantity));
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(4),
									colName.get(4) + " không được để trống");
							errorList.add(errorDTO);
						}
					} else {
						if (validateString(performer)) {
							String userId = null;
							userId = userMapLogin.get(performer.toUpperCase().trim());
							if (userId == null) {
								userId = userMapEmail.get(performer.toUpperCase().trim());
								if(userId==null) {
									ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(2),
											colName.get(3) + " không tồn tại");
									errorList.add(errorDTO);
								} else {
									obj.setPerformerId(Long.parseLong(userId));
								}
							} else {
								obj.setPerformerId(Long.parseLong(userId));
							}
							
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(2),
									colName.get(3) + " không được để trống");
							errorList.add(errorDTO);
						}
						
						if (validateString(quantity)) {
							obj.setQuantity(Double.valueOf(quantity));
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(3),
									colName.get(4) + " không được để trống");
							errorList.add(errorDTO);
						}
					}
					
					if (errorList.size() == 0) {
						workLst.add(obj);
					}
				}
			}

			if (errorList.size() > 0) {
				String filePathError = UEncrypt.encryptFileUploadPath(fileInput);
				List<WorkItemDTO> emptyArray = Lists.newArrayList();
				workLst = emptyArray;
				WorkItemDTO errorContainer = new WorkItemDTO();
				errorContainer.setErrorList(errorList);
				if(typeImport.equals("1")) {
					errorContainer.setMessageColumn(5); // cột dùng để in ra lỗi
				} else {
					errorContainer.setMessageColumn(4); // cột dùng để in ra lỗi
				}
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
			List<WorkItemDTO> emptyArray = Lists.newArrayList();
			workLst = emptyArray;
			WorkItemDTO errorContainer = new WorkItemDTO();
			errorContainer.setErrorList(errorList);
			if(typeImport.equals("1")) {
				errorContainer.setMessageColumn(5); // cột dùng để in ra lỗi
			} else {
				errorContainer.setMessageColumn(4); // cột dùng để in ra lỗi
			}
			errorContainer.setFilePathError(filePathError);
			workLst.add(errorContainer);
			return workLst;
		}
	}
    
    public DetailMonthPlanDTO getDetailMonthPlanId(Long sysGroupId) {
    	return constructionTaskDailyDAO.getDetailMonthPlanId(sysGroupId);
    }
    
    public Long updateConstructionTaskWork(DetailMonthPlanDTO obj) {
    	return constructionTaskDailyDAO.updateConstructionTaskWork(obj);
    }
    
    public Long updateWorkItem(DetailMonthPlanDTO obj) {
    	return constructionTaskDailyDAO.updateWorkItem(obj);
    }
    
    public List<RpQuantityDTO> getDataForInsertRpQuantity(){
    	return constructionTaskDailyDAO.getDataForInsertRpQuantity();
    }
    
    public Long deleteRpQuantity(Long id) {
    	return constructionTaskDailyDAO.deleteRpQuantity(id);
    }
    
    public Long insertDataInRpQuantity(RpQuantityDTO obj) {
    	obj.setInsertTime(new Date());
		return rpQuantityDAO.saveObject(obj.toModel());
	}
    
    public void updateConstructionTaskQuyLuong(DetailMonthPlanDTO obj) {
    	constructionTaskDailyDAO.updateConstructionTaskQuyLuong(obj);
    }
    
    public void updateConstructionTaskDoanhThu(DetailMonthPlanDTO obj) {
    	constructionTaskDailyDAO.updateConstructionTaskDoanhThu(obj);
    }
    //Huy-end
    
    //Huypq-20200227-start
    public List<ConstructionTaskDTO> getConsTaskByDetailMonthPlan(Long monthPlanId){
    	return constructionTaskDailyDAO.getConsTaskByDetailMonthPlan(monthPlanId);
    }
    
    public void updateStatusCons(Long consId) {
    	constructionTaskDailyDAO.updateStatusCons(consId);
    }
    
    //Huy-end
//    hoanm1_20200514_start
    public Double avgStatus(Long constructionId) {
    	return constructionTaskDailyDAO.avgStatus(constructionId);
    }
    public void updateStatusConsProcess(Long consId) {
    	constructionTaskDailyDAO.updateStatusConsProcess(consId);
    }
//    hoanm1_20200514_end
}
