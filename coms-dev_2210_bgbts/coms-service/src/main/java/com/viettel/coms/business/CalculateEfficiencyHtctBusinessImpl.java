package com.viettel.coms.business;
 
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;
import com.viettel.coms.bo.CalculateEfficiencyHtctBO;
import com.viettel.coms.dao.CalculateEfficiencyHtctDAO;
import com.viettel.coms.dto.CalculateEfficiencyHtctDTO;
import com.viettel.coms.dto.CapexBtsHtctDTO;
import com.viettel.coms.dto.ExcelErrorDTO;
import com.viettel.coms.dto.OfferHtctDTO;
import com.viettel.coms.utils.ValidateUtils;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.service.base.business.BaseFWBusinessImpl;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("calculateEfficiencyHtctBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CalculateEfficiencyHtctBusinessImpl extends BaseFWBusinessImpl<CalculateEfficiencyHtctDAO,CalculateEfficiencyHtctDTO, CalculateEfficiencyHtctBO> implements CalculateEfficiencyHtctBusiness {

    @Autowired
    private CalculateEfficiencyHtctDAO calculateEfficiencyHtctDAO;
     
    public CalculateEfficiencyHtctBusinessImpl() {
        tModel = new CalculateEfficiencyHtctBO();
        tDAO = calculateEfficiencyHtctDAO;
    }
    
    public List<CalculateEfficiencyHtctDTO> getData9(CalculateEfficiencyHtctDTO obj){
    	return calculateEfficiencyHtctDAO.getData9(obj);
    }

    @Value("${folder_upload2}")
    private String folder2Upload;
    @Value("${default_sub_folder_upload}")
    private String defaultSubFolderUpload;
	public String downloadTemplateEffectiveCalculate(CalculateEfficiencyHtctDTO obj) throws Exception {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();
        InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Import_Effective_Calculate.xlsx"));
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
                udir.getAbsolutePath() + File.separator + "Import_Effective_Calculate.xlsx");
        workbook.write(outFile);
		workbook.close();
		outFile.close();
		String path = UEncrypt
				.encryptFileUploadPath(uploadPathReturn + File.separator + "Import_Effective_Calculate.xlsx");
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
	public List<CalculateEfficiencyHtctDTO> importEffectiveCalculate(String fileInput) {
		List<CalculateEfficiencyHtctDTO> workLst = Lists.newArrayList();
		List<ExcelErrorDTO> errorLst = new ArrayList<>();
		List<CalculateEfficiencyHtctDTO> lstCal = calculateEfficiencyHtctDAO.getData9(new CalculateEfficiencyHtctDTO());
		HashMap<String, CalculateEfficiencyHtctDTO> mapDB = new HashMap<>();
		HashMap<String, String> mapExcel = new HashMap<>();
		List<String> lstContent = calculateEfficiencyHtctDAO.lstContent();
		
		for(CalculateEfficiencyHtctDTO cal : lstCal) {
			mapDB.put(cal.getContentCalEff(), cal);
		}
		try {
			File f = new File(fileInput);
			XSSFWorkbook workbook = new XSSFWorkbook(f);
			XSSFSheet sheet = workbook.getSheetAt(0);
			DataFormatter formatter = new DataFormatter();
			int count = 0;
			for (Row row : sheet) {
				count++;
				if(count>=3 && !ValidateUtils.checkIfRowIsEmpty(row)) {
					String contentCalEff = formatter.formatCellValue(row.getCell(1));
					String unit = formatter.formatCellValue(row.getCell(2));
					String costCalEff = formatter.formatCellValue(row.getCell(3));
					String costNotSource = formatter.formatCellValue(row.getCell(4));
					String costSource = formatter.formatCellValue(row.getCell(5));
					CalculateEfficiencyHtctDTO dto = new CalculateEfficiencyHtctDTO();
					List<String> lstUnit = new ArrayList<>(Arrays.asList("VNĐ","Đồng", "%", "Năm", "Đồng/Tháng"));
					//Validate content
					if(validateString(contentCalEff)) {
						//Check exist in App_param table
						if(lstContent.contains(contentCalEff)) {
							//Check duplicate in file import
							if(mapExcel.size() == 0) {
								dto.setContentCalEff(contentCalEff);
								if(mapDB.get(dto.getContentCalEff()) != null) {
									CalculateEfficiencyHtctDTO obj = mapDB.get(dto.getContentCalEff());
									dto.setCalculateEfficiencyId(obj.getCalculateEfficiencyId());
									dto.setCreatedDate(obj.getCreatedDate());
									dto.setCreatedUserId(obj.getCreatedUserId());
								}
								mapExcel.put(contentCalEff, contentCalEff);
							}else {
								if(mapExcel.get(contentCalEff) != null){
									ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "B", "Dữ liệu import có bản ghi với nội dung trùng nhau. Hãy xem lại!");
									errorLst.add(errorDTO);
								} else {
									dto.setContentCalEff(contentCalEff);
									if(mapDB.get(dto.getContentCalEff()) != null) {
										CalculateEfficiencyHtctDTO obj = mapDB.get(dto.getContentCalEff());
										dto.setCalculateEfficiencyId(obj.getCalculateEfficiencyId());
										dto.setCreatedDate(obj.getCreatedDate());
										dto.setCreatedUserId(obj.getCreatedUserId());
									}
									mapExcel.put(contentCalEff, contentCalEff);
								}
							}
						}else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "B", "Nội dung không tồn tại!");
							errorLst.add(errorDTO);
						}
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "B", " Nội dung không được để trống");
						errorLst.add(errorDTO);
					}
					if(validateString(unit)) {
						if(lstUnit.contains(unit.trim())) {
							dto.setUnit(unit.trim());
						}else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "C", " Đơn vị tính không tồn tại");
							errorLst.add(errorDTO);
						}
					}
					if(validateString(costCalEff)) {
						try {
							Double cost = Double.parseDouble(costCalEff);
							dto.setCostCalEff(cost);
						}catch(Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "D", " Hãy nhập số vào ô giá trị");
							errorLst.add(errorDTO);
						}
					}
					if(validateString(costNotSource)) {
						try {
							Double costN = Double.parseDouble(costNotSource);
							dto.setCostNotSource(costN);
						} catch(Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "E", " Hãy nhập số vào ô không nguồn");
							errorLst.add(errorDTO);
						}
					}
					if(validateString(costSource)) {
						try {
							Double costS = Double.parseDouble(costSource);
							dto.setCostSource(costS);
						} catch(Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "F", " Hãy nhập số vào ô có nguồn");
							errorLst.add(errorDTO);
						}
					}
					if(errorLst.size() == 0) {
						workLst.add(dto);
					}
				}
			}
			if(errorLst.size() >0) {
				String filePathError = UEncrypt.encryptFileUploadPath(fileInput);
				List<CalculateEfficiencyHtctDTO> emptyArray = Lists.newArrayList();
				workLst = emptyArray;
				CalculateEfficiencyHtctDTO errC = new CalculateEfficiencyHtctDTO();
				errC.setErrorList(errorLst);
				errC.setMessageColumn(6); // Column to print error
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
			} catch (Exception ex) {
				errorDTO = createError(0, "", ex.toString());
				errorLst.add(errorDTO);
			}
			List<CalculateEfficiencyHtctDTO> emptyArray = Lists.newArrayList();
			workLst = emptyArray;
			CalculateEfficiencyHtctDTO errorContainer = new CalculateEfficiencyHtctDTO();
			errorContainer.setErrorList(errorLst);
			errorContainer.setMessageColumn(6);// Column to print error
			workLst.add(errorContainer);
			return workLst;
		}
	}

	public void saveCalEff(CalculateEfficiencyHtctDTO dto) {
		calculateEfficiencyHtctDAO.saveObject(dto.toModel());
		
	}

	public void updateCalEff(CalculateEfficiencyHtctDTO dto) {
		calculateEfficiencyHtctDAO.updateObject(dto.toModel());
		
	}
   
}
