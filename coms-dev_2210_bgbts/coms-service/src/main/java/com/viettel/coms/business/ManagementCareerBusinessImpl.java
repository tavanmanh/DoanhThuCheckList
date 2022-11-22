package com.viettel.coms.business;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

//Duonghv13-start 16092021
import com.viettel.coms.dto.ManageCareerDTO;
import com.viettel.coms.dto.SysUserCOMSDTO;
import com.viettel.coms.utils.ExcelUtils;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.util.DateTimeUtils;
import com.viettel.coms.bo.ManageCareerBO;
import com.viettel.coms.dao.ManageCareerDAO;

@Service("managementCareerBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ManagementCareerBusinessImpl extends BaseFWBusinessImpl<ManageCareerDAO, ManageCareerDTO, ManageCareerBO>
	implements ManagementCareerBusiness{
	
	static Logger LOGGER = LoggerFactory.getLogger(ManagementCareerBusinessImpl.class);
	@Autowired
	private ManageCareerDAO manageCareerDAO;
	


	 @Value("${folder_upload2}")
	 private String folder2Upload;
	 @Value("${default_sub_folder_upload}")
	 private String defaultSubFolderUpload;

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ManageCareerDTO getById(Long id) {
		// TODO Auto-generated method stub
		return manageCareerDAO.getById(id);
	}

	@Override
	public List<ManageCareerDTO> doSearch(ManageCareerDTO obj) {
		// TODO Auto-generated method stub
		return manageCareerDAO.doSearch(obj);
	}

	@Override
	public Long createCareer(ManageCareerDTO obj) throws Exception {
		// TODO Auto-generated method stub
		boolean check = checkCode(obj.getCode(),obj.getCareerId());
		if (!check) {
			throw new IllegalArgumentException("Mã nghề đã tồn tại !");
		}
		return manageCareerDAO.saveObject(obj.toModel());
	}

	@Override
	public Long updateCareer(ManageCareerDTO obj) throws Exception {
		// TODO Auto-generated method stub
		boolean check = checkCode(obj.getCode(),obj.getCareerId());
		if (!check) {
			throw new IllegalArgumentException("Mã nghề đã tồn tại !");
		}
		return manageCareerDAO.updateObject(obj.toModel());
	}

	public ManagementCareerBusinessImpl() {
		tModel = new ManageCareerBO();
		tDAO = manageCareerDAO;
	}
	
	@Override
	public ManageCareerDAO gettDAO() {
		return manageCareerDAO;
	}
	
	

	
	
	@SuppressWarnings("unused")
	public Boolean checkCode(String code, Long careerId) {
		ManageCareerDTO obj = manageCareerDAO.findBycode(code);
		if (careerId == null) {
			if (obj == null) {
				return true;
			} else {
				return false;
			}
		} else {
			if (obj == null) {
				return true;
			} else if (obj != null && obj.getCareerId().longValue() == careerId) {
				return true;
			} else {
				return false;
			}
		}
		
	}

	public String exportCareer(ManageCareerDTO obj1) throws Exception {
		// TODO Auto-generated method stub
			obj1.setPage(1L);
			obj1.setPageSize(null);
			List<ManageCareerDTO> lstReport = manageCareerDAO.doSearch(obj1);
			String folderParam = defaultSubFolderUpload;
		    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		    String filePath = classloader.getResource("../" + "doc-template").getPath();
			System.out.println("OK");
			System.out.println(filePath);
					
			InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Danhsachnganhnghe.xlsx"));
			XSSFWorkbook workbook = new XSSFWorkbook(file);

	        file.close();
	        
	        String uploadPath = folder2Upload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload);

	        String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload);
	        File udir = new File(uploadPath);
	        if (!udir.exists()) {
	            udir.mkdirs();
	        }
	        OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "Danhsachnganhnghe.xlsx");
	        XSSFSheet sheet = workbook.getSheetAt(0);
			XSSFCellStyle stt = ExcelUtils.styleText(sheet);
			stt.setAlignment(HorizontalAlignment.CENTER);
			XSSFCellStyle sttbold = ExcelUtils.styleBold(sheet);
			
			Row rowS2 = sheet.createRow(2);
			
			Date dateNow = new Date();
			Cell cellS12 = rowS2.createCell(4, CellType.STRING);
			cellS12.setCellValue("Ngày tạo:  " + (DateTimeUtils.convertDateToString(dateNow, "dd/MM/yyyy")));
			cellS12.setCellStyle(sttbold);
			
			Row rowS12 = sheet.createRow(3);
			Cell cellS10 = rowS12.createCell(0, CellType.STRING);
			cellS10.setCellValue("Danh sách ngành nghề"); 
			cellS10.setCellStyle(sttbold);
			
	        
	        if (lstReport != null && !lstReport.isEmpty()) {
	            XSSFCellStyle style = ExcelUtils.styleText(sheet);
	            AtomicInteger ordinal = new AtomicInteger(6);
	            for (ManageCareerDTO obj : lstReport) {
	    			Row row = sheet.createRow(ordinal.getAndIncrement());
					Cell cell = row.createCell(0, CellType.STRING);
					cell.setCellValue(StringUtils.isNotEmpty(obj.getCode()) ? obj.getCode() : "");
					cell.setCellStyle(style);
					cell = row.createCell(1, CellType.STRING);
					cell.setCellValue(StringUtils.isNotEmpty(obj.getName()) ? obj.getName() : "");
					cell.setCellStyle(style);
	    			cell = row.createCell(2, CellType.STRING);
	    			cell.setCellValue(StringUtils.isNotEmpty(obj.getWoListName()) ? obj.getWoListName() : "");
	    			cell.setCellStyle(style);
	    		}
					
	        }
	        workbook.write(outFile);
	        workbook.close();
	        outFile.close();

	        String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "Danhsachnganhnghe.xlsx");
	        return path;
		}
	
		public Object getForAutoCompleteInSign(ManageCareerDTO obj) {
	        // TODO Auto-generated method stub
	        return manageCareerDAO.getForAutoCompleteInSign(obj);
	    }


		//Duonghv13 end-16/09/2021//
}
