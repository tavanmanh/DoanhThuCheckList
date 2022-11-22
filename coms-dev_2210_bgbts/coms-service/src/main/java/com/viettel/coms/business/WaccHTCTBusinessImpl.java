package com.viettel.coms.business;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
import com.viettel.coms.bo.WaccHtctBO;
import com.viettel.coms.dao.WaccHtctDAO;
import com.viettel.coms.dto.CapexSourceHTCTDTO;
import com.viettel.coms.dto.ExcelErrorDTO;
import com.viettel.coms.dto.WaccHtctDTO;
import com.viettel.coms.utils.ValidateUtils;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.service.base.business.BaseFWBusinessImpl;

@Service("waccHTCTBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class WaccHTCTBusinessImpl extends BaseFWBusinessImpl<WaccHtctDAO, WaccHtctDTO, WaccHtctBO> implements WaccHTCTBusiness {

	@Autowired
	private WaccHtctDAO waccHtctDAO;
	
	public WaccHTCTBusinessImpl() {
		tModel = new WaccHtctBO();
		tDAO = waccHtctDAO;
	}
	public List<WaccHtctDTO> getData2(WaccHtctDTO obj) {
		return waccHtctDAO.getData2(obj);
	}
	@Value("${folder_upload2}")
    private String folder2Upload;
    @Value("${default_sub_folder_upload}")
    private String defaultSubFolderUpload;
	public String downloadTemplateWACC(WaccHtctDTO obj) throws Exception {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();
        InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Import_WACC.xlsx"));
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
                udir.getAbsolutePath() + File.separator + "Import_WACC.xlsx");
        workbook.write(outFile);
		workbook.close();
		outFile.close();
		String path = UEncrypt
				.encryptFileUploadPath(uploadPathReturn + File.separator + "Import_WACC.xlsx");
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
	public List<WaccHtctDTO> importWACC(String fileInput) {
		List<WaccHtctDTO> workLst = Lists.newArrayList();
		List<ExcelErrorDTO> errorLst = new ArrayList<>();
		List<WaccHtctDTO> lstWacc = waccHtctDAO.getData2(new WaccHtctDTO());
		HashMap<String, WaccHtctDTO> mapDB = new HashMap<>();
		HashMap<String, String> mapExcel = new HashMap<>();
		List<String> lstWaccName = waccHtctDAO.getWaccName();
		for(WaccHtctDTO wacc : lstWacc) {
			mapDB.put(wacc.getWaccName().trim().toUpperCase(), wacc);
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
					String waccName = formatter.formatCellValue(row.getCell(1));
					String waccRex = formatter.formatCellValue(row.getCell(2));
					WaccHtctDTO dto = new WaccHtctDTO();
					//Validate waccName
					if(validateString(waccName)) {
						if(lstWaccName.contains(waccName)) {
							//Check duplicate in file import
							if(mapExcel.size()== 0) {
								dto.setWaccName(waccName);
								if(mapDB.get(dto.getWaccName().trim().toUpperCase())!=null) {
									dto.setWaccId(mapDB.get(dto.getWaccName().trim().toUpperCase()).getWaccId());
								}
								mapExcel.put(waccName.trim().toUpperCase(), waccName);
							} else {
								if(mapExcel.get(waccName.trim().toUpperCase()) != null) {
									ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "B", "Dữ liệu import có bản ghi với danh mục trùng nhau. Hãy xem lại!");
									errorLst.add(errorDTO);
								} else {
									dto.setWaccName(waccName);
									if(mapDB.get(dto.getWaccName().trim().toUpperCase())!=null) {
										dto.setWaccId(mapDB.get(dto.getWaccName().trim().toUpperCase()).getWaccId());
									}
									mapExcel.put(waccName.trim().toUpperCase(), waccName);
								}
							}
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "B", " Danh mục không tồn tại");
							errorLst.add(errorDTO);
						}
						
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "B", " Danh mục không được để trống");
						errorLst.add(errorDTO);
					}
					
					//Validate tỉ lệ
					if(validateString(waccRex)) {
						try {
							Double waccR = Double.parseDouble(waccRex);
							dto.setWaccRex(waccR);
						} catch(Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "C", "Hãy nhập số vào ô tỉ lệ");
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
				List<WaccHtctDTO> emptyArray = Lists.newArrayList();
				workLst = emptyArray;
				WaccHtctDTO errC = new WaccHtctDTO();
				errC.setErrorList(errorLst);
				errC.setMessageColumn(3); // Column to print error
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
			} catch(Exception ex) {
				errorDTO = createError(0, "", ex.toString());
				errorLst.add(errorDTO);
			}
			List<WaccHtctDTO> emptyArray = Lists.newArrayList();
			workLst = emptyArray;
			WaccHtctDTO errorContainer = new WaccHtctDTO();
			errorContainer.setErrorList(errorLst);
			errorContainer.setMessageColumn(3);//Column to print error
			workLst.add(errorContainer);
			return workLst;
		}
	}
	public void saveWacc(WaccHtctDTO dto) {
		waccHtctDAO.saveObject(dto.toModel());
		
	}
	public void updateWacc(Double waccRex, String waccName, Long waccId, Date updatedDate, Long updateUserId) {
		waccHtctDAO.updateWacc(waccRex, waccName, waccId, updatedDate, updateUserId);
		
	}

}
