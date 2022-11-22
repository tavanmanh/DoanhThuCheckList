package com.viettel.coms.business;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
//Duonghv13 start-16/08/2021//
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.Rows;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.viettel.cat.dao.CatProvinceDAO;
import com.viettel.cat.dto.CatProvinceDTO;
import com.viettel.coms.bo.TotalMonthPlanHTCTBO;
import com.viettel.coms.dao.CatStationDAO;
import com.viettel.coms.dao.TotalMonthPlanHTCTDAO;
import com.viettel.coms.dto.CatStationDTO;
import com.viettel.coms.dto.ConstructionDetailDTO;
import com.viettel.coms.dto.ConstructionTaskDetailDTO;
import com.viettel.coms.dto.ContactUnitDTO;
import com.viettel.coms.dto.DepartmentDTO;
import com.viettel.coms.dto.ExcelErrorDTO;
import com.viettel.coms.dto.SynStockTransDetailDTO;
import com.viettel.coms.dto.SynStockTransDetailSerialDTO;
import com.viettel.coms.dto.SysUserCOMSDTO;
import com.viettel.coms.dto.TotalMonthPlanHTCTDTO;
import com.viettel.coms.utils.ExcelUtils;
import com.viettel.ktts.vps.VpsPermissionChecker;
import com.viettel.ktts2.common.ChuyenTienRaChu;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.utils.Constant;
import com.viettel.utils.ConvertData;
import com.viettel.utils.DateTimeUtils;
import com.viettel.wms.utils.ValidateUtils;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import javassist.compiler.ast.Pair;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service("totalMonthPlanHTCTBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TotalMonthPlanHTCTBusinessImpl extends BaseFWBusinessImpl<TotalMonthPlanHTCTDAO, TotalMonthPlanHTCTDTO, TotalMonthPlanHTCTBO>
	implements TotalMonthPlanHTCTBusiness{
	
	static Logger LOGGER = LoggerFactory.getLogger(TotalMonthPlanHTCTBusinessImpl.class);


	@Autowired
	private TotalMonthPlanHTCTDAO totalMonthPlanHTCTDAO;
	@Autowired
	private CatProvinceDAO catProvinceDAO;
	

	 @Value("${folder_upload2}")
	 private String folder2Upload;
	 @Value("${default_sub_folder_upload}")
	 private String defaultSubFolderUpload;

	public TotalMonthPlanHTCTBusinessImpl() {
		tModel = new TotalMonthPlanHTCTBO();
		tDAO = totalMonthPlanHTCTDAO;
	}
	
	@Override
	public TotalMonthPlanHTCTDAO gettDAO() {
		return totalMonthPlanHTCTDAO;
	}
	@Override
	public long count() {
		return 0;
		// TODO Auto-generated method stub
		
	}

	@Override
	public TotalMonthPlanHTCTDTO findbyProvinceCodeandMonth(String code,String month,String year,String stationCodeVCC) {
		return totalMonthPlanHTCTDAO.findbyProvinceCodeAndMonth(code,month,year,stationCodeVCC);
	}
	@Override
	public TotalMonthPlanHTCTDTO getById(Long id) {
		return totalMonthPlanHTCTDAO.getById(id);
	}

	@Override
	public List<TotalMonthPlanHTCTDTO> doSearch(TotalMonthPlanHTCTDTO obj) {
		// TODO Auto-generated method stub
		return totalMonthPlanHTCTDAO.doSearch(obj);
	}
	
	@SuppressWarnings("unused")
	public Boolean checkCodeandMonth(String code,String month,String year,String stationCodeVCC, Long totalMonthPlanDTHTId) {
		TotalMonthPlanHTCTDTO obj = totalMonthPlanHTCTDAO.findbyProvinceCodeAndMonth(code,month,year,stationCodeVCC);
		if (totalMonthPlanDTHTId == null) {
			if (obj == null) {
				return true;
			} else {
				return false;
			}
		} else {
			if (obj == null) {
				return true;
			} else if (obj != null && obj.getTotalMonthPlanDTHTId().longValue() == totalMonthPlanDTHTId) {
				return true;
			} else {
				return false;
			}
		}
		
	}

	@Override
	public Long createTotalMonthPlanHTCT(TotalMonthPlanHTCTDTO obj) throws Exception {
		// TODO Auto-generated method stub
		boolean check = checkCodeandMonth(obj.getProvinceCode(),obj.getMonth(),obj.getYear().toString(),obj.getStationCodeVCC(), obj.getTotalMonthPlanDTHTId());
		if (!check) {
			throw new IllegalArgumentException("Chỉ tiêu gắn cho tỉnh và mã trạm trên đã tồn tại trong danh sách kế hoạch tháng.Vui lòng xem lại !");
		}
		return totalMonthPlanHTCTDAO.saveObject(obj.toModel());
	}

	@Override
	public Long updateTotalMonthPlanHTCT(TotalMonthPlanHTCTDTO obj) throws Exception {
		// TODO Auto-generated method stub
		return totalMonthPlanHTCTDAO.updateObject(obj.toModel());
	}

	@Override
	public Long deleteTotalMonthPlanHTCT(TotalMonthPlanHTCTDTO obj) {
		// TODO Auto-generated method stub
		return totalMonthPlanHTCTDAO.updateObject(obj.toModel());
	}

	@Override
	public List<TotalMonthPlanHTCTDTO> importTotalMonthPlanHTCT(String fileInput) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String exportExcelTemplate() throws Exception{
		TotalMonthPlanHTCTDTO temp = new TotalMonthPlanHTCTDTO();
		List<CatStationDTO> lstStation = totalMonthPlanHTCTDAO.getAllStationVCCHTCT(temp);
		String folderParam = defaultSubFolderUpload;
	    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
	    String filePath = classloader.getResource("../" + "doc-template").getPath();
		System.out.println("OK");
		System.out.println(filePath);
				
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "BieuMauKeHoachThangTongTheHTCT.xlsx"));

		XSSFWorkbook workbook = new XSSFWorkbook(file);

        file.close();
        
        String uploadPath = folder2Upload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload);

        String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload);
        File udir = new File(uploadPath);
        if (!udir.exists()) {
            udir.mkdirs();
        }
        OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "BieuMauKeHoachThangTongTheHTCT.xlsx");

        XSSFSheet sheet = workbook.getSheetAt(1);
        if (lstStation != null && !lstStation.isEmpty()) {
            XSSFCellStyle style = ExcelUtils.styleText(sheet);
            AtomicInteger ordinal = new AtomicInteger(1);
            for (CatStationDTO entry : lstStation) {
    			Row row = sheet.createRow(ordinal.getAndIncrement());
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(entry.getCode()) ? entry.getCode().toString() : "");
				cell.setCellStyle(style);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(entry.getCatProvinceName().toString()) ? entry.getCatProvinceName().toString() : "");
				cell.setCellStyle(style);
			
    		}	
        }
        workbook.setActiveSheet(0);
        workbook.write(outFile);
        workbook.close();
        outFile.close();

        String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "BieuMauKeHoachThangTongTheHTCT.xlsx");

        return path;
	}

	
	
	int [] validateCol = {0,1,2,3,4};
    HashMap<Integer, String> colName = new HashMap();
    {
        colName.put(0,"Tháng");
        colName.put(1,"Năm");
        colName.put(2,"Mã tỉnh");
        colName.put(3,"Mã trạm VCC");
        colName.put(4,"Mã trạm VTNET");
        colName.put(5,"Tổng thuê mặt bằng");
        colName.put(6,"Tổng thiết kế dự toán");
        colName.put(7,"Tổng khởi công");
        colName.put(8,"Tổng đồng bộ hạ tầng");
        colName.put(9,"Tổng phát sóng");
        colName.put(10,"Tổng hồ sơ hoàn công về TTHT");
        colName.put(11,"Tổng hồ sơ hoàn công về DTHT");
        colName.put(12,"Tổng trạm lên doanh thu");
        

    }
    
    HashMap<Integer, String> colAlias = new HashMap();
	{
		colAlias.put(0, "A");
		colAlias.put(1, "B");
		colAlias.put(2, "C");
		colAlias.put(3, "D");
		colAlias.put(4, "E");
		colAlias.put(5, "F");
		colAlias.put(6, "G");
		colAlias.put(7, "H");
		colAlias.put(8, "I");
		colAlias.put(9, "J");
		colAlias.put(10, "K");
		colAlias.put(11, "L");
		colAlias.put(12, "M");
		
	}
    
	@Override
	public List<TotalMonthPlanHTCTDTO> importReport(String fileInput) throws Exception{
		File f = new File(fileInput);
		DataFormatter formatter = new DataFormatter();
		XSSFWorkbook workbook = new XSSFWorkbook(f);
		XSSFSheet sheet = workbook.getSheetAt(0);
		Row valuecheck = sheet.getRow(3);
		List<CatProvinceDTO> listProvince = Lists.newArrayList();
		List<CatStationDTO> listStation = Lists.newArrayList();
		List<TotalMonthPlanHTCTDTO> reportList = Lists.newArrayList();
		TotalMonthPlanHTCTDTO temp = new TotalMonthPlanHTCTDTO();
		Multimap<String, String> mapReport = ArrayListMultimap.create();
		int counts = 0;
		List<String> monthYearMapExcel = new ArrayList<>();
		List<String> provinceCodeMapExcel = new ArrayList<>();
		for (Row rows : sheet) {
			counts++;
			if (counts > 3 && !ValidateUtils.checkIfRowIsEmpty(rows)) {
				String month = formatter.formatCellValue(rows.getCell(0));
				String year = formatter.formatCellValue(rows.getCell(1));
				String provinceCode = formatter.formatCellValue(rows.getCell(2));
				StringBuilder result =new StringBuilder("");
				String regex1 = "^(1[012]|0?[1-9])$";
				if(validateString(month)) {
					if (Pattern.matches(regex1,month.trim())) {
						if(month.length()==1 && month.equalsIgnoreCase("0")==false) {
							month ="0" +month;			
						}
						result.append(month);
					} 
			    }
			    String regex2 = "^\\d{4}";
			    if(validateString(year)) {
					if (Pattern.matches(regex2,year.trim())) {	
						result.append("/"+year);

					} 
				}
			    if( validateString(month) && validateString(year))
			    	monthYearMapExcel.add(result.toString());
			    if(validateString(provinceCode)) {
			    	provinceCodeMapExcel.add(provinceCode);
				}
			}
		}
		TotalMonthPlanHTCTDTO temporary = new TotalMonthPlanHTCTDTO();
		temporary.setListmonthYear(monthYearMapExcel);
		temporary.setListprovinceCode(provinceCodeMapExcel);
		temp.setListprovinceCode(provinceCodeMapExcel);
		if(formatter.formatCellValue(valuecheck.getCell(4)) !="" ){
			reportList = totalMonthPlanHTCTDAO.getAllTotalMonthPlan(temporary);
			for (TotalMonthPlanHTCTDTO dto : reportList) {
				mapReport.put( dto.getMonthYear(),dto.getProvinceCode()+"_"+dto.getStationCodeVCC());
			}
			listProvince = catProvinceDAO.getAllProvince();
			listStation = totalMonthPlanHTCTDAO.getAllStationVCCHTCT(temp);
		}
		List<TotalMonthPlanHTCTDTO> workLst = Lists.newArrayList();
		List<ExcelErrorDTO> errorList = new ArrayList<ExcelErrorDTO>();
		String error = "";
		try {
			int count = 0;
			for (Row row : sheet) {			
				TotalMonthPlanHTCTDTO obj = new TotalMonthPlanHTCTDTO();
				count++;
				if (count > 3 && checkIfRowIsEmpty(row)) 
					continue;
				if (count > 3) {
					if (!validateRequiredCell(row, errorList))
						continue;
					String month = formatter.formatCellValue(row.getCell(0));
					String year = formatter.formatCellValue(row.getCell(1));
					String provinceCode = formatter.formatCellValue(row.getCell(2));
					String stationCodeVCC = formatter.formatCellValue(row.getCell(3));
					String stationCodeVTNET = formatter.formatCellValue(row.getCell(4));
					String soluong_TMB = formatter.formatCellValue(row.getCell(5));
					String soluong_TKDT = formatter.formatCellValue(row.getCell(6));
					String soluong_KC = formatter.formatCellValue(row.getCell(7));
					String soluong_DB = formatter.formatCellValue(row.getCell(8));
					String soluong_PS = formatter.formatCellValue(row.getCell(9));
					String soluong_HSHC = formatter.formatCellValue(row.getCell(10));
					String soluong_HSHC_DTHT = formatter.formatCellValue(row.getCell(11));
					String tram_toDoanhThu = formatter.formatCellValue(row.getCell(12));
					

					String regex1 = "^(1[012]|0?[1-9])$";
					
					if(validateString(month)) {
						if (Pattern.matches(regex1,month.trim())) {
							if(month.length()==1 && month.equalsIgnoreCase("0")==false) {
								month ="0" +month;			
							}
							obj.setMonth(month);
							
							
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(0), colName.get(0)+ " không hợp lệ hoặc sai định dạng.");
							errorList.add(errorDTO);
						}
					}
					String regex2 = "^\\d{4}";
					if(validateString(year)) {
						if (Pattern.matches(regex2,year.trim())) {
							
							obj.setYear(Long.parseLong(year));
							
							
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(1), colName.get(1)+ " không hợp lệ hoặc sai định dạng.");
							errorList.add(errorDTO);
						}
					}
					obj.setMonthYear(obj.getMonth()+"/"+obj.getYear().toString());
					if(validateString(provinceCode)) {
						if(listProvince.stream().anyMatch(o -> o.getCode().equals(provinceCode)) ==false) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(2), "Mã tỉnh không hợp lệ hoặc không tồn tại");
							errorList.add(errorDTO);
						}else {
							boolean value = mapReport.containsKey(obj.getMonthYear());
							if (value == true ){
								Collection<String> all = mapReport.get(obj.getMonthYear());
								boolean result = all.contains(provinceCode+"_"+stationCodeVCC.toUpperCase());
								if(result == false) {
									obj.setProvinceCode(provinceCode);
								}else {
									ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(2), "Chỉ tiêu gắn cho trạm của "+ colName.get(2) +" trên đã tồn tại trong file hoặc cơ sở dữ liệu");
									errorList.add(errorDTO);
								}
							}else {
								obj.setProvinceCode(provinceCode);
							}
						}
					 }
					if(validateString(stationCodeVCC)) {
						if(listStation ==null || listStation.stream().anyMatch(o -> o.getCode().equals(stationCodeVCC)) ==false) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(3), "Mã trạm VCC không hợp lệ,không tồn tại hoặc không khớp với tỉnh trên .");
							errorList.add(errorDTO);
						}else {
							obj.setStationCodeVCC(stationCodeVCC);
						}
					 }
					if(validateString(stationCodeVTNET)) {
					   obj.setStationCodeVTNET(stationCodeVTNET);
					}
					
					//validate giá trị số
					String regex3 = "^[01]$";
					if(validateString(soluong_TMB)) {
						if (Pattern.matches(regex3,soluong_TMB.trim())) {
							obj.setSoluong_TMB(Long.parseLong(soluong_TMB.toString()));	
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(5), colName.get(5)+ " sai định dạng.");
							errorList.add(errorDTO);
						}
					}else obj.setSoluong_TMB(0l);	
					
					if(validateString(soluong_TKDT)) {
						if (Pattern.matches(regex3,soluong_TKDT.trim())) {
							obj.setSoluong_TKDT(Long.parseLong(soluong_TKDT.toString()));	
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(6), colName.get(6)+ " sai định dạng.");
							errorList.add(errorDTO);
						}
					}else obj.setSoluong_TKDT(0l);
					
					if(validateString(soluong_KC)) {
						if (Pattern.matches(regex3,soluong_KC.trim())) {
							obj.setSoluong_KC(Long.parseLong(soluong_KC.toString()));	
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(7), colName.get(7)+ " sai định dạng.");
							errorList.add(errorDTO);
						}
					}else obj.setSoluong_KC(0l);
					if(validateString(soluong_DB)) {
						if (Pattern.matches(regex3,soluong_DB.trim())) {
							obj.setSoluong_DB(Long.parseLong(soluong_DB.toString()));	
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(8), colName.get(8)+ " sai định dạng.");
							errorList.add(errorDTO);
						}
					}else obj.setSoluong_DB(0l);	
					if(validateString(soluong_PS)) {
						if (Pattern.matches(regex3,soluong_PS.trim())) {
							obj.setSoluong_PS(Long.parseLong(soluong_PS.toString()));	
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(9), colName.get(9)+ " sai định dạng.");
							errorList.add(errorDTO);
						}
					}else obj.setSoluong_PS(0l);
					
					if(validateString(soluong_HSHC)) {
						if (Pattern.matches(regex3,soluong_HSHC.trim())) {
							obj.setSoluong_HSHC(Long.parseLong(soluong_HSHC.toString()));	
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(10), colName.get(10)+ " sai định dạng.");
							errorList.add(errorDTO);
						}
					}else obj.setSoluong_HSHC(0l);
					if(validateString(soluong_HSHC_DTHT)) {
						if (Pattern.matches(regex3,soluong_HSHC_DTHT.trim())) {
							obj.setSoluong_HSHC_DTHT(Long.parseLong(soluong_HSHC_DTHT.toString()));	
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(11), colName.get(11)+ " sai định dạng.");
							errorList.add(errorDTO);
						}
					}else obj.setSoluong_HSHC_DTHT(0l);
					
					
					if(validateString(tram_toDoanhThu)) {
						if (Pattern.matches(regex3,tram_toDoanhThu.trim())) {
							obj.setTram_toDoanhThu(Long.parseLong(tram_toDoanhThu.toString()));	
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(12), colName.get(12)+ " sai định dạng.");
							errorList.add(errorDTO);
						}
					}else obj.setTram_toDoanhThu(0l);	
					
					
					if (errorList.size() == 0) {
						mapReport.put(obj.getMonthYear(),obj.getProvinceCode()+"_"+obj.getStationCodeVCC().toUpperCase());
						workLst.add(obj);
					}

				}
					
			}

			if(errorList.size() > 0){
				String filePathError = UEncrypt.encryptFileUploadPath(fileInput);
				List<TotalMonthPlanHTCTDTO> emptyArray = Lists.newArrayList();
				workLst = emptyArray;
				TotalMonthPlanHTCTDTO errorContainer = new TotalMonthPlanHTCTDTO();
				errorContainer.setErrorList(errorList);
				errorContainer.setMessageColumn(13); // cột dùng để in ra lỗi
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
			List<TotalMonthPlanHTCTDTO> emptyArray = Lists.newArrayList();
			workLst = emptyArray;
			TotalMonthPlanHTCTDTO errorContainer = new TotalMonthPlanHTCTDTO();
			errorContainer.setErrorList(errorList);
			errorContainer.setMessageColumn(13); // cột dùng để in ra lỗi
			errorContainer.setFilePathError(filePathError);
			workLst.add(errorContainer);
			return workLst;
		}	
	}
	
	//HienLT56 start 16032021
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
	
	public boolean validateString(String str){
		return (str != null && str.length()>0);
	}
	
	
	
	private ExcelErrorDTO createError(int row, String column, String detail){
		ExcelErrorDTO err = new ExcelErrorDTO();
		err.setColumnError(column);
		err.setLineError(String.valueOf(row));
		err.setDetailError(detail);
		return err;
	}
	
	public boolean validateRequiredCell(Row row, List<ExcelErrorDTO> errorList){
		DataFormatter formatter = new DataFormatter();
		boolean result = true;
		for(int colIndex : validateCol){
			if(!StringUtils.isNotEmpty(formatter.formatCellValue(row.getCell(colIndex)))){
				ExcelErrorDTO errorDTO = new ExcelErrorDTO();
				errorDTO.setColumnError(colAlias.get(colIndex));
				errorDTO.setLineError(String.valueOf(row.getRowNum() + 1));
				errorDTO.setDetailError(colName.get(colIndex)+" chưa nhập");
				errorList.add(errorDTO);
				result = false;
			}
			
		}
		return result;
	}
	
	public Boolean checkUsedProvince(String provinceCode) {
		CatProvinceDTO result = catProvinceDAO.findByCode(provinceCode);
		if(result == null) {
			return false;
		}
		return true;
	}

	public String exportMonthPlan(TotalMonthPlanHTCTDTO obj1) throws Exception {
		// TODO Auto-generated method stub
		
		obj1.setPage(1L);
		obj1.setPageSize(null);
		List<TotalMonthPlanHTCTDTO> lstReport = totalMonthPlanHTCTDAO.doSearch(obj1);
		String folderParam = defaultSubFolderUpload;
	    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
	    String filePath = classloader.getResource("../" + "doc-template").getPath();
		System.out.println("OK");
		System.out.println(filePath);
				
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "KeHoachThangTongTheHTCT.xlsx"));

		XSSFWorkbook workbook = new XSSFWorkbook(file);

        file.close();
        
        String uploadPath = folder2Upload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload);

        String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload);
        File udir = new File(uploadPath);
        if (!udir.exists()) {
            udir.mkdirs();
        }

        OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "KeHoachThangTongTheHTCT.xlsx");

        XSSFSheet sheet = workbook.getSheetAt(0);
		XSSFCellStyle stt = ExcelUtils.styleText(sheet);
		stt.setAlignment(HorizontalAlignment.CENTER);
		XSSFCellStyle styleTitle = ExcelUtils.styleBold(sheet);
		
//		Row rowS2 = sheet.createRow(0);
//		
//		Date dateNow = new Date();
//		Cell cellS12 = rowS2.createCell(8, CellType.STRING);
//		cellS12.setCellValue("Ngày lập kế hoạch:  " + (DateTimeUtils.convertDateToString(dateNow, "dd/MM/yyyy")));
//		cellS12.setCellStyle(sttbold);
		
		XSSFFont fontStyle = workbook.createFont();
		fontStyle.setFontHeightInPoints((short) 20);
		fontStyle.setFontName("Times New Roman");
		fontStyle.setBold(true);
		fontStyle.setItalic(false);
		styleTitle.setAlignment(HorizontalAlignment.CENTER);
		styleTitle.setFont(fontStyle);
		styleTitle.setWrapText(false);
		
		Row rowS12 = sheet.createRow(0);
		Cell cellS10 = rowS12.createCell(0, CellType.STRING);
		cellS10.setCellValue("Kế hoạch tháng tổng thể hạ tầng cho thuê"); 
		cellS10.setCellStyle(styleTitle);
		
        if (lstReport != null && !lstReport.isEmpty()) {
            AtomicInteger ordinal = new AtomicInteger(4);
            AtomicInteger no = new AtomicInteger(1);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			styleNumber.setAlignment(HorizontalAlignment.CENTER);
            for (TotalMonthPlanHTCTDTO obj : lstReport) {
            	Row row = sheet.createRow(ordinal.getAndIncrement());
				Cell cell = row.createCell(0, CellType.NUMERIC);
				cell.setCellValue(no.getAndIncrement());
				cell.setCellStyle(styleNumber);
    			cell = row.createCell(1, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getMonth().toString()) ? obj.getMonth().toString() : "");
				cell.setCellStyle(styleNumber);
				cell = row.createCell(2, CellType.NUMERIC);
				cell.setCellValue( obj.getYear());
				cell.setCellStyle(styleNumber);
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getProvinceCode().toString()) ? obj.getProvinceCode().toString() : "");
				cell.setCellStyle(styleNumber);
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getStationCodeVCC()) ? obj.getStationCodeVCC() : "");
				cell.setCellStyle(styleNumber);
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getStationCodeVTNET()) ? obj.getStationCodeVTNET() : "");
				cell.setCellStyle(styleNumber);
				cell = row.createCell(6, CellType.NUMERIC);
				cell.setCellValue(obj.getSoluong_TMB() != null ? obj.getSoluong_TMB() : 0);
				cell.setCellStyle(styleNumber);
				
				
				cell = row.createCell(7, CellType.NUMERIC);
				cell.setCellValue(obj.getSoluong_TKDT() != null ? obj.getSoluong_TKDT() : 0);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(8, CellType.NUMERIC);
				cell.setCellValue(obj.getSoluong_KC() != null ? obj.getSoluong_KC() : 0);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(9, CellType.NUMERIC);
				cell.setCellValue(obj.getSoluong_DB() != null ? obj.getSoluong_DB() : 0);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(10, CellType.NUMERIC);
				cell.setCellValue(obj.getSoluong_PS() != null ? obj.getSoluong_PS() : 0);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(11, CellType.NUMERIC);
				cell.setCellValue(obj.getSoluong_HSHC() != null ? obj.getSoluong_HSHC() : 0);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(12, CellType.NUMERIC);
				cell.setCellValue(obj.getSoluong_HSHC_DTHT() != null ? obj.getSoluong_HSHC_DTHT() : 0);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(13, CellType.NUMERIC);
				cell.setCellValue(obj.getTram_toDoanhThu() != null ? obj.getTram_toDoanhThu() : 0);
				cell.setCellStyle(styleNumber);

			
    		}
            
            Row row = sheet.createRow(ordinal.get());
            Cell cell = row.createCell(5, CellType.STRING);
    		cell.setCellValue("Tổng chỉ tiêu"); 
    		cell.setCellStyle(styleTitle);
            cell = row.createCell(6, CellType.NUMERIC);
			cell.setCellValue(lstReport.get(0).getSumsoluong_TMB());
			cell.setCellStyle(styleTitle);
			
			cell = row.createCell(7, CellType.NUMERIC);
			cell.setCellValue(lstReport.get(0).getSumsoluong_TKDT());
			cell.setCellStyle(styleTitle);
			
			cell = row.createCell(8, CellType.NUMERIC);
			cell.setCellValue(lstReport.get(0).getSumsoluong_KC());
			cell.setCellStyle(styleTitle);
			
			cell = row.createCell(9, CellType.NUMERIC);
			cell.setCellValue(lstReport.get(0).getSumsoluong_DB());
			cell.setCellStyle(styleTitle);
			
			cell = row.createCell(10, CellType.NUMERIC);
			cell.setCellValue(lstReport.get(0).getSumsoluong_PS());
			cell.setCellStyle(styleTitle);
			
			cell = row.createCell(11, CellType.NUMERIC);
			cell.setCellValue(lstReport.get(0).getSumsoluong_HSHC());
			cell.setCellStyle(styleTitle);
			
			cell = row.createCell(12, CellType.NUMERIC);
			cell.setCellValue(lstReport.get(0).getSumsoluong_HSHC_DTHT());
			cell.setCellStyle(styleTitle);
			
			cell = row.createCell(13, CellType.NUMERIC);
			cell.setCellValue(lstReport.get(0).getSumtram_toDoanhThu());
			cell.setCellStyle(styleTitle);
				
        }
        workbook.write(outFile);
        workbook.close();
        outFile.close();

        String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "KeHoachThangTongTheHTCT.xlsx");

        return path;
	}
	
	public String exportPDFReport(TotalMonthPlanHTCTDTO criteria) throws Exception {
		criteria.setPage(null);
		criteria.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		String sourceFileName = null;
		List<TotalMonthPlanHTCTDTO> ls = totalMonthPlanHTCTDAO.doSearch(criteria);
		sourceFileName = filePath + "/KeHoachThangTongTheHTCT.jrxml";
		if (!new File(sourceFileName).exists()) {
			return null;
		}
		String fileName = "KeHoachThangTongTheHTCT";

		InputStream in = new FileInputStream(new File(sourceFileName));
		JasperReport report = JasperCompileManager.compileReport(in);
		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(ls);
		final Map<String, Object> parameters = new HashMap<>();
        parameters.put("sysUserName", criteria.getSysUserName());
        parameters.put("provinceCode", criteria.getProvinceCode());
        parameters.put("status", criteria.getStatus());
        parameters.put("monthYear", criteria.getMonthYear());
        parameters.put("createdByName", criteria.getCreatedByName());
        parameters.put("total", ls.size());
		String fileNameEncrypt = null;
		try {
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, beanColDataSource);
			if (jasperPrint != null) {
				String exportPath = UFile.getFilePath(folder2Upload, defaultSubFolderUpload);

				String destFileName = exportPath + File.separatorChar + fileName + ".pdf";
				JasperExportManager.exportReportToPdfFile(jasperPrint, destFileName);

				fileNameEncrypt = UEncrypt.encryptFileUploadPath(
						exportPath.replace(folder2Upload, "") + File.separatorChar + fileName + ".pdf");
				return fileNameEncrypt;
			}
		} catch (JRException e) {
			e.printStackTrace();
		}
		return null;
//		InputStream in = new FileInputStream(new File(sourceFileName));
//		IXDocReport report = XDReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Freemarker);
//		FieldsMetadata metadata = report.createFieldsMetadata();
//		// 3) Create context Java model
//		IContext context = report.createContext();
//		context.put("HTCTReport", criteria);
//		metadata.load("HTCTReport", TotalMonthPlanHTCTDTO.class, true);
//		Date now = new Date();
//		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");  
//		String strDate = dateFormat.format(now);
//		String exportType = "jasper";
//		File fout = new File(folder2Upload + "/" + strDate + "_"+ fileName+ "." +exportType.trim().toLowerCase());
//		OutputStream out = new FileOutputStream(fout);
//		if("pdf".equals(exportType)){
//			Options options = Options.getTo(ConverterTypeTo.PDF).via(ConverterTypeVia.XWPF);
//			report.convert(context, options, out);
//		}else{
//			report.process(context, out);
//		}
//		out.flush();
//		out.close();
//		return strDate + "_" + fileName+ "." +exportType;
	}

	//Duonghv13 end-16/08/2021//
	
	//Duonghv13 START-17/09/2021//
	public Boolean checkRoleCreateMonthPlan(HttpServletRequest request) {
		// TODO Auto-generated method stub
		if (!VpsPermissionChecker.hasPermission(Constant.OperationKey.CREATED, Constant.AdResourceKey.MONTH_PLAN_HTCT,
				request)) {
			return false;
		}
		return true;
	}
	//Duonghv13 END-17/09/2021//

	public DataListDTO getAllStationVCCHTCT(TotalMonthPlanHTCTDTO obj,HttpServletRequest request) {
		// TODO Auto-generated method stub
		List<CatStationDTO> ls  = totalMonthPlanHTCTDAO.getAllStationVCCHTCT(obj);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
	}

}
