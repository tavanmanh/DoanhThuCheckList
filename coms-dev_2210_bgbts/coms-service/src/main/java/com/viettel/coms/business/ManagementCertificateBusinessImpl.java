package com.viettel.coms.business;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.schema.PathResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.viettel.coms.bo.ManageCareerBO;
import com.viettel.coms.bo.ManageCertificateBO;
import com.viettel.coms.bo.WoBO;
import com.viettel.coms.bo.WoXdddChecklistBO;
import com.viettel.coms.dao.ManageCareerDAO;
import com.viettel.coms.dao.ManageCertificateDAO;
import com.viettel.coms.dto.ManageCareerDTO;
import com.viettel.coms.dto.ManageCertificateDTO;
import com.viettel.coms.dto.WoDTO;
import com.viettel.coms.dto.WoMappingChecklistDTO;
import com.viettel.coms.dto.WoSimpleSysUserDTO;
import com.viettel.coms.dto.WoTaskDailyDTO;
import com.viettel.coms.dto.WoXdddChecklistDTO;
import com.viettel.coms.utils.ExcelUtils;
import com.viettel.erp.dto.CntContractDTO;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.util.DateTimeUtils;
//Duonghv13-start 21092021
@Service("managementCertificateBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ManagementCertificateBusinessImpl
		extends BaseFWBusinessImpl<ManageCertificateDAO, ManageCertificateDTO, ManageCertificateBO>
		implements ManagementCertificateBusiness {

	static Logger LOGGER = LoggerFactory.getLogger(ManagementCertificateBusinessImpl.class);
	@Autowired
	private ManageCertificateDAO manageCertificateDAO;

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
	public ManageCertificateDTO getById(Long id) {
		// TODO Auto-generated method stub
		return manageCertificateDAO.getOneCertificateDetails(id);
	}

	@Override
	public List<ManageCertificateDTO> doSearch(ManageCertificateDTO obj) {
		// TODO Auto-generated method stub
		return manageCertificateDAO.doSearch(obj);
	}

	@Override
	public Long createCertificate(ManageCertificateDTO obj) throws Exception {
		// TODO Auto-generated method stub
		boolean check = checkCodeAndUser(obj.getCertificateCode(),obj.getCareerId(), obj.getLoginName(),obj.getCertificateId());
		if (!check) {
			throw new IllegalArgumentException("Chứng chỉ đã tồn tại hoặc được gán!");
		}
		return manageCertificateDAO.saveObject(obj.toModel());
	}
	
	@SuppressWarnings("unused")
	public Boolean checkCodeAndUser(String code,Long careerId,String loginName, Long certificateId) {
		ManageCertificateDTO obj = manageCertificateDAO.findByCertificateCodeAndUser(code,careerId,loginName);
		if (certificateId == null) {
			if (obj == null) {
				return true;
			} else {
				return false;
			}
		} else {
			if (obj == null) {
				return true;
			} else if (obj != null && obj.getCertificateId().longValue() == certificateId) {
				return true;
			} else {
				return false;
			}
		}
		
	}
	@Override
	public Long updateCertificate(ManageCertificateDTO obj) throws Exception {
		// TODO Auto-generated method stub
		boolean check = checkCodeAndUser(obj.getCertificateCode(),obj.getCareerId(), obj.getLoginName(),obj.getCertificateId());
		if (!check) {
			throw new IllegalArgumentException("Chứng chỉ đã tồn tại hoặc được gán!");
		}
		return manageCertificateDAO.updateObject(obj.toModel());
	}
	
	@Override
	public String exportCertificate(ManageCertificateDTO obj1) throws Exception {
		// TODO Auto-generated method stub
		obj1.setPage(1L);
		List<ManageCertificateDTO> lstReport = manageCertificateDAO.doSearch(obj1);
		String folderParam = defaultSubFolderUpload;
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		System.out.println("OK");
		System.out.println(filePath);

		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Danhsachchungchi.xlsx"));
		XSSFWorkbook workbook = new XSSFWorkbook(file);

		file.close();

		String uploadPath = folder2Upload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload);

		String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload);
		File udir = new File(uploadPath);
		if (!udir.exists()) {
			udir.mkdirs();
		}
		OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "Danhsachchungchi.xlsx");
		XSSFSheet sheet = workbook.getSheetAt(0);
		XSSFCellStyle stt = ExcelUtils.styleText(sheet);
		stt.setAlignment(HorizontalAlignment.CENTER);
		XSSFCellStyle styleTitle = ExcelUtils.styleBold(sheet);
		
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
		cellS10.setCellValue("Danh sách chứng chỉ");
		cellS10.setCellStyle(styleTitle);

		if (lstReport != null && !lstReport.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			style.setAlignment(HorizontalAlignment.CENTER);
			AtomicInteger ordinal = new AtomicInteger(3);
			AtomicInteger no = new AtomicInteger(1);
			for (ManageCertificateDTO obj : lstReport) {
				 Row row = sheet.createRow(ordinal.getAndIncrement());
				 Cell cell = row.createCell(0, CellType.NUMERIC);
				 cell.setCellValue(no.getAndIncrement());
				 cell.setCellStyle(style);
				 cell = row.createCell(1, CellType.STRING);
				 cell.setCellValue(StringUtils.isNotEmpty(obj.getCertificateCode()) ? obj.getCertificateCode() : "");
				 cell.setCellStyle(style);
				 cell = row.createCell(2, CellType.STRING);
				 cell.setCellValue(StringUtils.isNotEmpty(obj.getCertificateName()) ? obj.getCertificateName() : "");
				 cell.setCellStyle(style);
				 cell = row.createCell(3, CellType.STRING);
				 cell.setCellValue(StringUtils.isNotEmpty(obj.getCareerName()) ? obj.getCareerName() : "");
				 cell.setCellStyle(style);
				 
				 cell = row.createCell(4, CellType.STRING);
				 cell.setCellValue(StringUtils.isNotEmpty(obj.getFullName()) ? obj.getFullName() : "");
				 cell.setCellStyle(style);
				 cell = row.createCell(5, CellType.STRING);
				 cell.setCellValue(StringUtils.isNotEmpty(obj.getLoginName()) ? obj.getLoginName() : "");
				 cell.setCellStyle(style);
				 cell = row.createCell(6, CellType.STRING);
				 cell.setCellValue(StringUtils.isNotEmpty(obj.getSysGroupName()) ? obj.getSysGroupName() : "");
				 cell.setCellStyle(style);
				 
				 cell = row.createCell(7, CellType.STRING);
				 cell.setCellValue(StringUtils.isNotEmpty(obj.getEmail()) ? obj.getEmail() : "");
				 cell.setCellStyle(style);
				 cell = row.createCell(8, CellType.STRING);
				 cell.setCellValue(StringUtils.isNotEmpty(obj.getPhoneNumber()) ? obj.getPhoneNumber() : "");
				 cell.setCellStyle(style);
				 cell = row.createCell(9, CellType.STRING);
				 cell.setCellValue(StringUtils.isNotEmpty(obj.getPositionName()) ? obj.getPositionName() : "");
				 cell.setCellStyle(style);
				 
				 cell = row.createCell(10, CellType.NUMERIC);
				 cell.setCellValue(obj.getPracticePoint() != null ? obj.getPracticePoint() : 0);
				 cell.setCellStyle(style);
				 cell = row.createCell(11, CellType.NUMERIC);
				 cell.setCellValue(obj.getTheoreticalPoint() != null ? obj.getTheoreticalPoint() : 0);
				 cell.setCellStyle(style);
				 cell = row.createCell(12, CellType.STRING);
				 cell.setCellValue(StringUtils.isNotEmpty(obj.getUnitCreated()) ? obj.getUnitCreated() : "");
				 cell.setCellStyle(style);
				 DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");  
				 cell = row.createCell(13, CellType.STRING);
				 cell.setCellValue(dateFormat.format(obj.getStartDate()));
				 cell.setCellStyle(style);
				 cell = row.createCell(14, CellType.STRING);
				 cell.setCellValue(dateFormat.format(obj.getFinishDate()));
				 cell.setCellStyle(style);
				 cell = row.createCell(15, CellType.STRING);
				 if(obj.getCertificateStatus()==1l) {
					 cell.setCellValue("Còn hạn");
				 }else if(obj.getCertificateStatus()==2l) {
					 cell.setCellValue("Sắp hết hạn");
				 }else {
					 cell.setCellValue("Hết hạn");
				 }
				 cell.setCellStyle(style);
				 cell = row.createCell(16, CellType.STRING);
				 if(obj.getApproveStatus()==1l) {
					 cell.setCellValue("Chờ phê duyệt");
				 }else if(obj.getApproveStatus()==2l) {
					 cell.setCellValue("Đã phê duyệt");
				 }else {
					 cell.setCellValue("Từ chối phê duyệt");
				 }
				 cell.setCellStyle(style);
			}

		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "Danhsachchungchi.xlsx");
		return path;
	}

	@Override
    public int deleteCertificate(Long id) {
		return manageCertificateDAO.delete(id);
    }


	public ManagementCertificateBusinessImpl() {
		tModel = new ManageCertificateBO();
		tDAO = manageCertificateDAO;
	}

	@Override
	public ManageCertificateDAO gettDAO() {
		return manageCertificateDAO;
	}
	@Override
	public ManageCertificateDTO getOneCertificateDetails(Long id) {
		// TODO Auto-generated method stub
		return manageCertificateDAO.getOneCertificateDetails(id);
	}

	public Object getForAutoCompleteInSign(ManageCertificateDTO dto) {
		// TODO Auto-generated method stub
	       return manageCertificateDAO.getForAutoCompleteInSign(dto);
	}

}
//Duong-end
