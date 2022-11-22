package com.viettel.coms.business;
 
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;
import com.viettel.coms.bo.CostVtnetHtctBO;
import com.viettel.coms.dao.CostVtnetHtctDAO;
import com.viettel.coms.dto.CapexBtsHtctDTO;
import com.viettel.coms.dto.CostVtnetHtctDTO;
import com.viettel.coms.dto.ExcelErrorDTO;
import com.viettel.coms.utils.ExcelUtils;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.wms.utils.ValidateUtils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("costVtnetHtctBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CostVtnetHtctBusinessImpl extends BaseFWBusinessImpl<CostVtnetHtctDAO,CostVtnetHtctDTO, CostVtnetHtctBO> implements CostVtnetHtctBusiness {

    @Autowired
    private CostVtnetHtctDAO costVtnetHtctDAO;
     
    public CostVtnetHtctBusinessImpl() {
        tModel = new CostVtnetHtctBO();
        tDAO = costVtnetHtctDAO;
    }
    
    public List<CostVtnetHtctDTO> getData5(CostVtnetHtctDTO obj){
    	return costVtnetHtctDAO.getData5(obj);
    }
    
    @Value("${folder_upload2}")
   	private String folder2Upload;
   	@Value("${default_sub_folder_upload}")
   	private String defaultSubFolderUpload;
   	
	public String exportData5(CostVtnetHtctDTO obj) throws Exception {
		obj.setPage(null);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Bao_cao_dau_gia_thue_VTNet.xlsx"));
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
				udir.getAbsolutePath() + File.separator + "Bao_cao_dau_gia_thue_VTNet.xlsx");
		List<CostVtnetHtctDTO> data5 = costVtnetHtctDAO.getData5(obj);
		XSSFSheet sheet = workbook.getSheetAt(0);
		if(data5 != null && !data5.isEmpty()) {		
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			
			XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
			styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			styleCurrency.setAlignment(HorizontalAlignment.RIGHT);

			XSSFCellStyle stylePercent = ExcelUtils.styleNumber(sheet);
			styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
			int i = 2;
			for(CostVtnetHtctDTO dto : data5) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue(""+ (i-2));
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getStationType() != null) ? dto.getStationType() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getNotSourceHniHcm() != null) ? dto.getNotSourceHniHcm() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getNotSource61Province() != null) ? dto.getNotSource61Province() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getSourceHniHcm() != null ) ? dto.getSourceHniHcm() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getSource61Province() != null) ? dto.getSource61Province() : 0d);
				cell.setCellStyle(styleNumber);
				
			}
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();
		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "Bao_cao_dau_gia_thue_VTNet.xlsx");
		return path;
	}

	public String downloadTemplateDgiaThueVTNet(CostVtnetHtctDTO obj) throws Exception {
		 ClassLoader classloader = Thread.currentThread().getContextClassLoader();
	        String filePath = classloader.getResource("../" + "doc-template").getPath();
	        InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Import_Dgia_thue_VTNet.xlsx"));
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
	                udir.getAbsolutePath() + File.separator + "Import_Dgia_thue_VTNet.xlsx");
	        workbook.write(outFile);
			workbook.close();
			outFile.close();
			String path = UEncrypt
					.encryptFileUploadPath(uploadPathReturn + File.separator + "Import_Dgia_thue_VTNet.xlsx");
			return path;
	}

	public boolean validateString(String str) {
		return (str != null && str.trim().length() > 0);
	}

	private ExcelErrorDTO createError(int row, String column, String detail) {
		ExcelErrorDTO err = new ExcelErrorDTO();
		err.setColumnError(column);
		err.setLineError(String.valueOf(row));
		err.setDetailError(detail);
		return err;
	}
	public List<CostVtnetHtctDTO> importDgiaThueVTNet(String fileInput) {
		List<CostVtnetHtctDTO> workLst = Lists.newArrayList();
		List<ExcelErrorDTO> errorLst = new ArrayList<>();
		List<CostVtnetHtctDTO> lstCostVTNet = costVtnetHtctDAO.getData5(new CostVtnetHtctDTO());
		HashMap<String , CostVtnetHtctDTO> mapDB = new HashMap<>();
		HashMap<String , String> mapExcel = new HashMap<>();
		List<CostVtnetHtctDTO> lstStationType = costVtnetHtctDAO.getLstStationType();
		
		for(CostVtnetHtctDTO cost : lstCostVTNet) {
			mapDB.put(cost.getStationType().trim().toUpperCase(), cost);
		}
		try {
			File f = new File(fileInput);
			XSSFWorkbook workbook = new XSSFWorkbook(f);
			XSSFSheet sheet = workbook.getSheetAt(0);
			DataFormatter formatter = new DataFormatter();
			int count = 0;
			for (Row row : sheet) {
				count++;
				if (count >= 4 && !ValidateUtils.checkIfRowIsEmpty(row)) {
					String stationType = formatter.formatCellValue(row.getCell(1));
					String notSourceHNIHCM = formatter.formatCellValue(row.getCell(2));
					String notSource61Province = formatter.formatCellValue(row.getCell(3));
					String sourceHNIHCM = formatter.formatCellValue(row.getCell(4));
					String source61Province = formatter.formatCellValue(row.getCell(5));
					CostVtnetHtctDTO dto = new CostVtnetHtctDTO();
					//Validate stationType
					if(validateString(stationType)) {
						if(lstStationType.contains(stationType)) {
							//Check duplicate in file Import
							if(mapExcel.size() == 0) {
								dto.setStationType(stationType);
								//Check duplicate in DB
								if(mapDB.get(dto.getStationType().trim().toUpperCase())!=null) {
									CostVtnetHtctDTO obj = mapDB.get(dto.getStationType().trim().toUpperCase());
									dto.setCostVtnetId(obj.getCostVtnetId());
									dto.setCreatedDate(obj.getCreatedDate());
									dto.setCreatedUserId(obj.getCreatedUserId());
								}
								mapExcel.put(stationType.trim().toUpperCase(), stationType);
							} else {
								if(mapExcel.get(stationType.trim().toUpperCase()) !=null) {
									ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "B", "Dữ liệu import có bản ghi với loại trạm trùng nhau. Hãy xem lại!");
									errorLst.add(errorDTO);
								} else {
									dto.setStationType(stationType);
									if(mapDB.get(dto.getStationType().trim().toUpperCase())!=null) {
										CostVtnetHtctDTO obj = mapDB.get(dto.getStationType().trim().toUpperCase());
										dto.setCostVtnetId(obj.getCostVtnetId());
										dto.setCreatedDate(obj.getCreatedDate());
										dto.setCreatedUserId(obj.getCreatedUserId());
									}
									mapExcel.put(stationType.trim().toUpperCase(), stationType);
								}
							}
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "B", "Loại trạm không tồn tại");
							errorLst.add(errorDTO);
						}
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "B", " Loại trạm không được để trống");
						errorLst.add(errorDTO);
					}
					if(validateString(notSourceHNIHCM)) {
						try {
							Double notSH = Double.parseDouble(notSourceHNIHCM);
							dto.setNotSourceHniHcm(notSH);
						} catch(Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "C", " Giá trị Chỉ có cột - Chưa gồm nguồn_HNI,HCM không hợp lệ. Hãy nhập số!");
							errorLst.add(errorDTO);
						}
						
					}
					if(validateString(notSource61Province)) {
						try {
							Double notSH = Double.parseDouble(notSource61Province);
							dto.setNotSource61Province(notSH);
						} catch(Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "D", " Giá trị Chỉ có cột - Chưa gồm nguồn_61 tỉnh/TP khác không hợp lệ. Hãy nhập số!");
							errorLst.add(errorDTO);
						}
						
					}
					if(validateString(sourceHNIHCM)) {
						try {
							Double notSH = Double.parseDouble(sourceHNIHCM);
							dto.setSourceHniHcm(notSH);
						} catch(Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "E", " Giá trị Cột + hệ thống nguồn - chưa nhiên liệu/điện_HNI,HCM không hợp lệ. Hãy nhập số!");
							errorLst.add(errorDTO);
						}
						
					}
					if(validateString(source61Province)) {
						try {
							Double notSH = Double.parseDouble(source61Province);
							dto.setSource61Province(notSH);
						} catch(Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "F", " Giá trị Giá trị Cột + hệ thống nguồn - chưa nhiên liệu/điện_61 tỉnh/TP khác không hợp lệ. Hãy nhập số!");
							errorLst.add(errorDTO);
						}
						
					}
					if(errorLst.size() == 0) {
						workLst.add(dto);
					}
				}
			}
			if(errorLst.size() > 0) {
				String filePathError = UEncrypt.encryptFileUploadPath(fileInput);
				List<CostVtnetHtctDTO> emptyArray = Lists.newArrayList();
				workLst = emptyArray;
				CostVtnetHtctDTO errC = new CostVtnetHtctDTO();
				errC.setErrorList(errorLst);
				errC.setMessageColumn(6); // Column to print error
				errC.setFilePathError(filePathError);
				workLst.add(errC);
			}
			workbook.close();
			return workLst;
		}catch(Exception e) {
			ExcelErrorDTO errorDTO = createError(0, "", e.toString());
			errorLst.add(errorDTO);
			try {
				String filePathError = UEncrypt.encryptFileUploadPath(fileInput);
			} catch (Exception ex) {
				errorDTO = createError(0, "", ex.toString());
				errorLst.add(errorDTO);
			}
			List<CostVtnetHtctDTO> emptyArray = Lists.newArrayList();
			workLst = emptyArray;
			CostVtnetHtctDTO errorContainer = new CostVtnetHtctDTO();
			errorContainer.setErrorList(errorLst);
			errorContainer.setMessageColumn(6);// Column to print error
			workLst.add(errorContainer);
			return workLst;
		}
	}

	public void updateCostVTNet(CostVtnetHtctDTO dto) {
		costVtnetHtctDAO.updateObject(dto.toModel());
		
	}

	public void saveCostVTNet(CostVtnetHtctDTO dto) {
		costVtnetHtctDAO.saveObject(dto.toModel());
		
	}
   
}
