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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.viettel.coms.bo.CapexSourceHTCTBO;
import com.viettel.coms.dao.CapexSourceHTCTDAO;
import com.viettel.coms.dto.CapexSourceHTCTDTO;
import com.viettel.coms.dto.ExcelErrorDTO;
import com.viettel.coms.utils.ValidateUtils;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.service.base.business.BaseFWBusinessImpl;

@Service("capexSourceHTCTBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CapexSourceHTCTBusinessImpl extends BaseFWBusinessImpl<CapexSourceHTCTDAO, CapexSourceHTCTDTO, CapexSourceHTCTBO>
			implements CapexSourceHTCTBusiness{
	
	@Autowired
	private CapexSourceHTCTDAO capexSourceHTCTDAO;
	
	public CapexSourceHTCTBusinessImpl() {
		tModel = new CapexSourceHTCTBO();
		tDAO = capexSourceHTCTDAO;
	}

	public List<CapexSourceHTCTDTO> getCapexSource(CapexSourceHTCTDTO obj) {
		return capexSourceHTCTDAO.getCapexSource(obj);
	}

	@Value("${folder_upload2}")
    private String folder2Upload;
    @Value("${default_sub_folder_upload}")
    private String defaultSubFolderUpload;
	public String downloadTemplateCapexNguon(CapexSourceHTCTDTO obj) throws Exception {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();
        InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Import_Capex_Nguon.xlsx"));
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
                udir.getAbsolutePath() + File.separator + "Import_Capex_Nguon.xlsx");
        workbook.write(outFile);
		workbook.close();
		outFile.close();
		String path = UEncrypt
				.encryptFileUploadPath(uploadPathReturn + File.separator + "Import_Capex_Nguon.xlsx");
		return path;	}

	public boolean validateString(String str) {
		return (str != null && str.trim().length() > 0);
	}
	int [] validateCol = {1,3};
	HashMap<Integer, String> colName = new HashMap<>();
	{
		colName.put(1, "Nội dung");
		colName.put(3, "Giá trị");
	}
	HashMap<Integer, String> colAlias = new HashMap<>();
	{
		colAlias.put(1, "B");
		colAlias.put(2, "C");
		colAlias.put(3, "D");
	}
	private ExcelErrorDTO createError(int row, String column, String detail) {
		ExcelErrorDTO err = new ExcelErrorDTO();
		err.setColumnError(column);
		err.setLineError(String.valueOf(row));
		err.setDetailError(detail);
		return err;
	}
	public List<CapexSourceHTCTDTO> importCapexNguon(String fileInput) {
		List<CapexSourceHTCTDTO> workLst = Lists.newArrayList();
		List<ExcelErrorDTO> errorLst = new ArrayList<>();
		List<CapexSourceHTCTDTO> lstCapexSource = capexSourceHTCTDAO.getCapexSource(new CapexSourceHTCTDTO());
		HashMap<String, CapexSourceHTCTDTO> mapDB = new HashMap<>();
		HashMap<String, String> mapFileExcel = new HashMap<>();
		List<String> lstCapexNName = capexSourceHTCTDAO.getCapexNName();
		
		for(CapexSourceHTCTDTO capexS : lstCapexSource) {
			mapDB.put(capexS.getContentCapex().trim().toUpperCase(), capexS);
		}
		try {
			File f = new File(fileInput);
			XSSFWorkbook workbook = new XSSFWorkbook(f);
			XSSFSheet sheet = workbook.getSheetAt(0);
			DataFormatter formatter = new DataFormatter();
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			int count = 0;
			for (Row row : sheet) {
				count++ ;
				if(count >=3 && !ValidateUtils.checkIfRowIsEmpty(row)) {
					String contentCapex = formatter.formatCellValue(row.getCell(1));
					String unit = formatter.formatCellValue(row.getCell(2));
					String costCapex = formatter.formatCellValue(row.getCell(3));
					CapexSourceHTCTDTO dto = new CapexSourceHTCTDTO();
					//Validate content. Content is key.
					if(validateString(contentCapex)) {
						if(lstCapexNName.contains(contentCapex)) {
							//Check duplicate in Excel
							if(mapFileExcel.size() == 0 ) {
								dto.setContentCapex(contentCapex);
								if(mapDB.get(dto.getContentCapex().trim().toUpperCase()) != null) {
									CapexSourceHTCTDTO obj = mapDB.get(dto.getContentCapex().trim().toUpperCase());
									dto.setCapexSourceId(obj.getCapexSourceId());
//									dto.setCreatedDate(obj.getCreatedDate());
//									dto.setCreatedUserId(obj.getCreatedUserId());
								}
								mapFileExcel.put(contentCapex.trim().toUpperCase(), contentCapex);
							} else {
								if(mapFileExcel.get(contentCapex.trim().toUpperCase())!=null) {
									ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "B", "Dữ liệu import có bản ghi với nội dung trùng nhau. Hãy xem lại!");
									errorLst.add(errorDTO);
								} else {
									dto.setContentCapex(contentCapex);
									if(mapDB.get(dto.getContentCapex().trim().toUpperCase()) != null) {
										CapexSourceHTCTDTO obj = mapDB.get(dto.getContentCapex().trim().toUpperCase());
										dto.setCapexSourceId(obj.getCapexSourceId());
//										dto.setCreatedDate(obj.getCreatedDate());
//										dto.setCreatedUserId(obj.getCreatedUserId());
									}
									mapFileExcel.put(contentCapex.trim().toUpperCase(), contentCapex);
								}
							}
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "B", "Nội dung không tồn tại");
							errorLst.add(errorDTO);
						}
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "B", " Nội dung không được để trống");
						errorLst.add(errorDTO);
					}
					if(validateString(unit)) {
						if(unit.equals("Đồng")) {
							dto.setUnit(unit);
						}else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "C", "Đơn vị tính không tồn tại");
							errorLst.add(errorDTO);
						}
					}
					//validate Gia tri
					if(validateString(costCapex)) {
						try {
							Long costC = Long.parseLong(costCapex);
							dto.setCostCapex(costC);
						} catch(Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "D", " Hãy nhập số vào ô giá trị");
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
				List<CapexSourceHTCTDTO> emptyArray = Lists.newArrayList();
				workLst = emptyArray;
				CapexSourceHTCTDTO errC = new CapexSourceHTCTDTO();
				errC.setErrorList(errorLst);
				errC.setMessageColumn(4); // Column to print error
				errC.setFilePathError(filePathError);
				workLst.add(errC);
			}
			workbook.close();
			return workLst;
		} catch(Exception e ) {
			ExcelErrorDTO errorDTO = createError(0, "", e.toString());
			errorLst.add(errorDTO);
			try {
				String filePathError = UEncrypt.encryptFileUploadPath(fileInput); 
			} catch(Exception ex) {
				errorDTO = createError(0, "", ex.toString());
				errorLst.add(errorDTO);
			}
			List<CapexSourceHTCTDTO> emptyArray = Lists.newArrayList();
			workLst = emptyArray;
			CapexSourceHTCTDTO errorContainer = new CapexSourceHTCTDTO();
			errorContainer.setErrorList(errorLst);
			errorContainer.setMessageColumn(4);//Column to print error
			workLst.add(errorContainer);
			return workLst;
		}
	}

	public void updateCapex(String unit,  Long costCapex, Long capexSourceId, String contentCapex, Date updatedDate, Long updateUserId) {
		capexSourceHTCTDAO.updateCapex(unit, costCapex, capexSourceId, contentCapex, updatedDate, updateUserId);
	}

	public Long saveCapex(CapexSourceHTCTDTO dto) {
		return capexSourceHTCTDAO.saveObject(dto.toModel());
		
	}

}
