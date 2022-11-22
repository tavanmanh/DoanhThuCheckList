package com.viettel.coms.business;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

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

import com.google.common.collect.Lists;
import com.viettel.coms.bo.RatioDeliveryHtctBO;
import com.viettel.coms.dao.RatioDeliveryHtctDAO;
import com.viettel.coms.dto.CapexBtsHtctDTO;
import com.viettel.coms.dto.ExcelErrorDTO;
import com.viettel.coms.dto.OfferHtctDTO;
import com.viettel.coms.dto.RatioDeliveryHtctDTO;
import com.viettel.coms.utils.ExcelUtils;
import com.viettel.coms.utils.ValidateUtils;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.service.base.business.BaseFWBusinessImpl;


@Service("ratioDeliveryHtctBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RatioDeliveryHtctBusinessImpl extends BaseFWBusinessImpl<RatioDeliveryHtctDAO,RatioDeliveryHtctDTO, RatioDeliveryHtctBO> implements RatioDeliveryHtctBusiness {

    @Autowired
    private RatioDeliveryHtctDAO ratioDeliveryHtctDAO;

    public RatioDeliveryHtctBusinessImpl() {
        tModel = new RatioDeliveryHtctBO();
        tDAO = ratioDeliveryHtctDAO;
    }

    public List<RatioDeliveryHtctDTO> getData3(RatioDeliveryHtctDTO obj){
    	return ratioDeliveryHtctDAO.getData3(obj);
    }

	public RatioDeliveryHtctDTO findByProvince(String codeProvince){
		return ratioDeliveryHtctDAO.findByProvince(codeProvince);
	}

    @Value("${folder_upload2}")
	private String folder2Upload;
	@Value("${default_sub_folder_upload}")
	private String defaultSubFolderUpload;
	public String exportData3(RatioDeliveryHtctDTO obj) throws Exception {
		obj.setPage(null);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Bao_cao_ty_le_giao_khoan.xlsx"));
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
				udir.getAbsolutePath() + File.separator + "Bao_cao_ty_le_giao_khoan.xlsx");

		//Sheet 1
		List<RatioDeliveryHtctDTO> data3 = ratioDeliveryHtctDAO.getData3(obj);
		XSSFSheet sheet = workbook.getSheetAt(0);
		if(data3 != null && !data3.isEmpty()) {
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
			int i = 3;
			for (RatioDeliveryHtctDTO dto : data3) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0,CellType.STRING);
				cell.setCellValue(""+(i-3));
				cell.setCellStyle(styleNumber);

				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getCatProvinceCode() != null) ? dto.getCatProvinceCode() : "");
				cell.setCellStyle(style);

				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getCostDeliveryBts() != null) ? dto.getCostDeliveryBts() : 0d);
				cell.setCellStyle(stylePercent);

				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getCostMountainsBts() != null) ? dto.getCostMountainsBts() : 0d);
				cell.setCellStyle(stylePercent);

				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getCostRoofBts() != null) ? dto.getCostRoofBts() : 0d);
				cell.setCellStyle(stylePercent);

				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getCostDeliveryPru() != null ) ? dto.getCostDeliveryPru() : 0d);
				cell.setCellStyle(stylePercent);

				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getCostMountainsPru() != null) ? dto.getCostMountainsPru() : 0d);
				cell.setCellStyle(stylePercent);

				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue((dto.getCostRoofPru() != null) ? dto.getCostRoofPru() : 0d);
				cell.setCellStyle(stylePercent);

				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue((dto.getCostDeliverySmallcell() != null) ? dto.getCostDeliverySmallcell() : 0d);
				cell.setCellStyle(stylePercent);

				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue((dto.getCostMountainsSmallcell() != null) ? dto.getCostMountainsSmallcell() : 0d);
				cell.setCellStyle(stylePercent);

				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue((dto.getCostRoofSmallcell() != null) ? dto.getCostRoofSmallcell() : 0d);
				cell.setCellStyle(stylePercent);

			}
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();
		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "Bao_cao_ty_le_giao_khoan.xlsx");
		return path;
	}

	public String downloadTemplateTLGK(RatioDeliveryHtctDTO obj) throws Exception {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		String filePath = classLoader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Import_Ti_le_giao_khoan.xlsx"));
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
                udir.getAbsolutePath() + File.separator + "Import_Ti_le_giao_khoan.xlsx");
        workbook.write(outFile);
		workbook.close();
		outFile.close();
		String path = UEncrypt
				.encryptFileUploadPath(uploadPathReturn + File.separator + "Import_Ti_le_giao_khoan.xlsx");
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
	public List<RatioDeliveryHtctDTO> importTLGK(String fileInput) {
		List<RatioDeliveryHtctDTO> workLst = Lists.newArrayList();
		List<ExcelErrorDTO> errorLst = new ArrayList<>();
		List<RatioDeliveryHtctDTO> lstRatio = ratioDeliveryHtctDAO.getData3(new RatioDeliveryHtctDTO());
		HashMap<String, RatioDeliveryHtctDTO> mapDB = new HashMap<>();
		HashMap<String, String> mapExcel = new HashMap<>();
		List<String> lstProvince = ratioDeliveryHtctDAO.getProvince();
		for(RatioDeliveryHtctDTO ratio : lstRatio) {
			mapDB.put(ratio.getCatProvinceCode(), ratio);
		}
		try {
			File f = new File(fileInput);
			XSSFWorkbook workbook = new XSSFWorkbook(f);
			XSSFSheet sheet = workbook.getSheetAt(0);
			DataFormatter formatter = new DataFormatter();
			int count = 0;
			for (Row row : sheet) {
				count++;
				if(count>=4 && !ValidateUtils.checkIfRowIsEmpty(row)) {
					String catProvinceCode = formatter.formatCellValue(row.getCell(1));
					String costDeliveryBTS = formatter.formatCellValue(row.getCell(2));
					String costMountainsBTS = formatter.formatCellValue(row.getCell(3));
					String costRoofBTS = formatter.formatCellValue(row.getCell(4));
					String costDeliveryPru = formatter.formatCellValue(row.getCell(5));
					String costMountainsPru = formatter.formatCellValue(row.getCell(6));
					String costRoofPru = formatter.formatCellValue(row.getCell(7));
					String costDeliverySmallCell = formatter.formatCellValue(row.getCell(8));
					String costMountainsSmallCell = formatter.formatCellValue(row.getCell(9));
					String costRoofSmallCell = formatter.formatCellValue(row.getCell(10));
					RatioDeliveryHtctDTO dto = new RatioDeliveryHtctDTO();
					//Validate catProvinceCode
					if(validateString(catProvinceCode)) {
						if(!lstProvince.contains(catProvinceCode.trim().toUpperCase())) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "B", "Tỉnh không tồn tại");
							errorLst.add(errorDTO);
						}else {
							//Validate duplicate in file Import
							if(mapExcel.size() == 0) {
								dto.setCatProvinceCode(catProvinceCode.trim().toUpperCase());
								//Validate duplicate in DB
								if(mapDB.get(dto.getCatProvinceCode().trim().toUpperCase())!= null) {
									RatioDeliveryHtctDTO obj = mapDB.get(dto.getCatProvinceCode().trim().toUpperCase());
									dto.setRatioDeliveryId(obj.getRatioDeliveryId());
									dto.setCreatedDate(obj.getCreatedDate());
									dto.setCreatedUserId(obj.getCreatedUserId());
									dto.setCatProvinceId(obj.getCatProvinceId());
								}
								mapExcel.put(catProvinceCode.trim().toUpperCase(), catProvinceCode);
							} else {
								if(mapExcel.get(catProvinceCode.trim().toUpperCase())!= null) {
									ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "", "Dữ liệu import có bản ghi với cùng Tỉnh trùng nhau. Hãy xem lại!");
									errorLst.add(errorDTO);
								} else {
									dto.setCatProvinceCode(catProvinceCode.trim().toUpperCase());
									if(mapDB.get(dto.getCatProvinceCode().trim().toUpperCase())!= null) {
										RatioDeliveryHtctDTO obj = mapDB.get(dto.getCatProvinceCode().trim().toUpperCase());
										dto.setRatioDeliveryId(obj.getRatioDeliveryId());
										dto.setCreatedDate(obj.getCreatedDate());
										dto.setCreatedUserId(obj.getCreatedUserId());
										dto.setCatProvinceId(obj.getCatProvinceId());
									}
									mapExcel.put(catProvinceCode.trim().toUpperCase(), catProvinceCode);
								}
							}
						}
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "B", " Tỉnh không được để trống");
						errorLst.add(errorDTO);
					}
					//Validate other double values
					if(validateString(costDeliveryBTS)) {
						try {
							Double costDBTS = Double.parseDouble(costDeliveryBTS);
							dto.setCostDeliveryBts(costDBTS);
						}catch(Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "C", " Giá trị giao khoán TCTT trạm Macro_Đồng bằng không hợp lệ. Hãy nhập số!");
							errorLst.add(errorDTO);
						}
					}
					if(validateString(costMountainsBTS)) {
						try {
							Double costMBTS = Double.parseDouble(costMountainsBTS);
							dto.setCostMountainsBts(costMBTS);
						}catch(Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "D", " Giá trị giao khoán TCTT trạm Macro_Miền núi không hợp lệ. Hãy nhập số!");
							errorLst.add(errorDTO);
						}
					}
					if(validateString(costRoofBTS)) {
						try {
							Double costRBTS = Double.parseDouble(costRoofBTS);
							dto.setCostRoofBts(costRBTS);
						}catch(Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "E", " Giá trị giao khoán TCTT trạm Macro_Trên mái không hợp lệ. Hãy nhập số!");
							errorLst.add(errorDTO);
						}
					}
					if(validateString(costDeliveryPru)) {
						try {
							Double costDRRU = Double.parseDouble(costDeliveryPru);
							dto.setCostDeliveryPru(costDRRU);
						}catch(Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "F", " Giá trị giao khoán TCTT trạm RRU_Đồng bằng không hợp lệ. Hãy nhập số!");
							errorLst.add(errorDTO);
						}
					}
					if(validateString(costMountainsPru)) {
						try {
							Double costMRRU = Double.parseDouble(costMountainsPru);
							dto.setCostMountainsPru(costMRRU);
						}catch(Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "G", " Giá trị giao khoán TCTT trạm RRU_Miền núi không hợp lệ. Hãy nhập số!");
							errorLst.add(errorDTO);
						}
					}
					if(validateString(costRoofPru)) {
						try {
							Double costRRRU = Double.parseDouble(costRoofPru);
							dto.setCostRoofPru(costRRRU);
						}catch(Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "H", " Giá trị giao khoán TCTT trạm RRU_Trên mái không hợp lệ. Hãy nhập số!");
							errorLst.add(errorDTO);
						}
					}
					if(validateString(costDeliverySmallCell)) {
						try {
							Double costDSmallCell = Double.parseDouble(costDeliverySmallCell);
							dto.setCostDeliverySmallcell(costDSmallCell);
						}catch(Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "I", " Giá trị giao khoán TCTT trạm Smallcell_Đồng bằng không hợp lệ. Hãy nhập số!");
							errorLst.add(errorDTO);
						}
					}
					if(validateString(costMountainsSmallCell)) {
						try {
							Double costMSmallCell = Double.parseDouble(costMountainsSmallCell);
							dto.setCostMountainsSmallcell(costMSmallCell);
						}catch(Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "J", " Giá trị giao khoán TCTT trạm Smallcell_Miền núi không hợp lệ. Hãy nhập số!");
							errorLst.add(errorDTO);
						}
					}
					if(validateString(costRoofSmallCell)) {
						try {
							Double costRSmallCell = Double.parseDouble(costRoofSmallCell);
							dto.setCostRoofSmallcell(costRSmallCell);
						}catch(Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "K", " Giá trị giao khoán TCTT trạm Smallcell_Trên mái không hợp lệ. Hãy nhập số!");
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
				List<RatioDeliveryHtctDTO> emptyArray = Lists.newArrayList();
				workLst = emptyArray;
				RatioDeliveryHtctDTO errC = new RatioDeliveryHtctDTO();
				errC.setErrorList(errorLst);
				errC.setMessageColumn(11); // Column to print error
				errC.setFilePathError(filePathError);
				workLst.add(errC);
			}
			workbook.close();
			return workLst;

		} catch(Exception e) {
			ExcelErrorDTO errorDTO = createError(0, "", e.toString());
			errorLst.add(errorDTO);
			try {
				String filePathError = UEncrypt.encryptFileUploadPath(fileInput);
			} catch(Exception ex) {
				errorDTO = createError(0, "", ex.toString());
				errorLst.add(errorDTO);
			}
			List<RatioDeliveryHtctDTO> emptyArray = Lists.newArrayList();
			workLst = emptyArray;
			RatioDeliveryHtctDTO errorContainer = new RatioDeliveryHtctDTO();
			errorContainer.setErrorList(errorLst);
			errorContainer.setMessageColumn(11);//Column to print error
			workLst.add(errorContainer);
			return workLst;
		}
	}

	public void updateTLGK(RatioDeliveryHtctDTO dto) {
		ratioDeliveryHtctDAO.updateObject(dto.toModel());
	}

	public void saveTLGK(RatioDeliveryHtctDTO dto) {
		ratioDeliveryHtctDAO.saveObject(dto.toModel());

	}

	public Long getProvinceId(String catProvinceCode) {
		return ratioDeliveryHtctDAO.getProvinceIdForImport(catProvinceCode);
	}

}
