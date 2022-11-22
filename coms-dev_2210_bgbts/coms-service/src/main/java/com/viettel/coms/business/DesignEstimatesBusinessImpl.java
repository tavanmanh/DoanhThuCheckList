package com.viettel.coms.business;

import com.viettel.cat.dto.CatProvinceDTO;
import com.viettel.coms.bo.DesignEstimatesBO;
import com.viettel.coms.dao.*;
import com.viettel.coms.dto.*;
import com.viettel.coms.utils.ExcelUtils;
import com.viettel.erp.dto.CatProvincesDTO;
import com.viettel.erp.dto.SysUserDTO;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.utils.Constant;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service("designEstimatesBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DesignEstimatesBusinessImpl
        extends BaseFWBusinessImpl<DesignEstimatesDAO, DesignEstimatesDTO, DesignEstimatesBO>
        implements DetailMonthPlanBusiness {

    @Autowired
    private DesignEstimatesDAO designEstimatesDAO;

    @Autowired
    private UtilAttachDocumentDAO utilAttachDocumentDAO;
    
    @Value("${folder_upload2}")
    private String folder2Upload;
    @Value("${default_sub_folder_upload}")
    private String defaultSubFolderUpload;

    public DesignEstimatesBusinessImpl() {
        tModel = new DesignEstimatesBO();
        tDAO = designEstimatesDAO;
    }

    @Override
    public DesignEstimatesDAO gettDAO() {
        return designEstimatesDAO;
    }
    
    private static String DEESIGN_ESTIMATES = "DEESIGN_ESTIMATES";


    public DataListDTO doSearch(DesignEstimatesDTO obj, HttpServletRequest request) throws Exception {
        List<DesignEstimatesDTO> ls = new ArrayList<DesignEstimatesDTO>();
        List<DesignEstimatesDTO> lst = new ArrayList<DesignEstimatesDTO>();
        obj.setTotalRecord(0);
            ls = designEstimatesDAO.doSearch(obj);
            if(ls.size() > 0) {
            	for(DesignEstimatesDTO dto: ls) {
            		List<UtilAttachDocumentDTO> lsFile = utilAttachDocumentDAO.getByTypeAndObjectTC(dto.getDesignEstimatesId(), DEESIGN_ESTIMATES);
            		dto.setFileLst(lsFile);
            		lst.add(dto);
            	}
            }
            
        DataListDTO data = new DataListDTO();
        data.setData(lst);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Long save(DesignEstimatesDTO obj, HttpServletRequest request) {
		// TODO Auto-generated method stub
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		obj.setCreatedDate(new Date());
		obj.setStatus(1l);
		obj.setCreatedUser(user.getVpsUserInfo().getSysUserId());
		Long id = designEstimatesDAO.saveObject(obj.toModel());
		if(obj.getFileLst() != null && obj.getFileLst().size() > 0) {
			UtilAttachDocumentDTO fileDto = obj.getFileLst().get(0);
			fileDto.setObjectId(id);
			fileDto.setType(DEESIGN_ESTIMATES);
			fileDto.setCreatedDate(new Date());
			fileDto.setCreatedUserId(user.getVpsUserInfo().getSysUserId());
			utilAttachDocumentDAO.saveObject(fileDto.toModel());
		}
		return id;
	}

	public Boolean checkRoleUserAssignYctx(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	public Long update(DesignEstimatesDTO obj, HttpServletRequest request) {
		// TODO Auto-generated method stub
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		obj.setUpdateDate(new Date());
		obj.setUpdateUser(user.getVpsUserInfo().getSysUserId());
		Long id = designEstimatesDAO.updateObject(obj.toModel());
		if(obj.getFileLst().size() > 0) {
			for(UtilAttachDocumentDTO fileDto: obj.getFileLst()) {
				if(fileDto.getUtilAttachDocumentId() ==  null) {
					fileDto.setObjectId(obj.getDesignEstimatesId());
					fileDto.setType(DEESIGN_ESTIMATES);
					fileDto.setCreatedDate(new Date());
					fileDto.setCreatedUserId(user.getVpsUserInfo().getSysUserId());
					utilAttachDocumentDAO.saveObject(fileDto.toModel());
				}
			}
		}
		
		return id;
	}

	public Long delete(DesignEstimatesDTO obj, HttpServletRequest request) {
		// TODO Auto-generated method stub
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		obj.setStatus(0l);
		Long id = designEstimatesDAO.updateObject(obj.toModel());
		return id;
	}

	public DataListDTO doSearchArea(CatProvincesDTO obj) {
        List<CatProvincesDTO> ls = designEstimatesDAO.doSearchArea(obj);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        return data;
    }

	public DataListDTO doSearchUser(SysUserDTO obj) {
        List<SysUserDTO> ls = designEstimatesDAO.doSearchUser(obj);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        return data;
    }
	
	public DataListDTO doSearchStationVCC(CatStationDTO obj) {
        List<CatStationDTO> ls = designEstimatesDAO.doSearchStationVCC(obj);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        return data;
    }
	
	public DataListDTO doSearchStationVTNET(CatStationDTO obj) {
        List<CatStationDTO> ls = designEstimatesDAO.doSearchStationVTNET(obj);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        return data;
    }

	public String exportFile(DesignEstimatesDTO dto, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		dto.setPage(null);
		dto.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Tham_Thiet_Ke_Du_Toan.xlsx"));
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
				udir.getAbsolutePath() + File.separator + "Tham_Thiet_Ke_Du_Toan.xlsx");
		List<DesignEstimatesDTO> data = designEstimatesDAO.doSearch(dto);
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (data != null && !data.isEmpty()) {
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
			stylePercent.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

			XSSFFont font = workbook.createFont();
			font.setBold(true);
			font.setItalic(false);
			font.setFontName("Times New Roman");
			font.setFontHeightInPoints((short) 12);

			XSSFCellStyle styleYear = ExcelUtils.styleText(sheet);
			styleYear.setAlignment(HorizontalAlignment.CENTER);
			styleYear.setFont(font);
			styleYear.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
			styleYear.setFillPattern(FillPatternType.NO_FILL);

			Date currentDateEx = new Date();
			int i = 6;
			for (DesignEstimatesDTO obj : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 6));
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getArea()) ? obj.getArea() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getProvinceName()) ? obj.getProvinceName() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getStationVTNET()) ? obj.getStationVTNET() : "");
				cell.setCellStyle(style);

				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getStationVCC()) ? obj.getStationVCC() : "");
				cell.setCellStyle(style);

				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getStationAddress()) ? obj.getStationAddress() : "");
				cell.setCellStyle(style);

				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getDistrictName()) ? obj.getDistrictName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getStationType()) ? obj.getStationType() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getTerrain()) ? obj.getTerrain() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getStationLong()) ? obj.getStationLong() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getStationLat()) ? obj.getStationLat() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getPillarType()) ? obj.getPillarType() : "");
				cell.setCellStyle(style);

				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getLocation()) ? obj.getLocation() : "");
				cell.setCellStyle(style);

				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getPillarHight()) ? obj.getPillarHight() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(14, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getTube()) ? obj.getTube() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(15, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getFundamental()) ? obj.getFundamental() : "");
				cell.setCellStyle(style);

				cell = row.createCell(16, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getLevelRockEarth()) ? obj.getLevelRockEarth() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(17, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getEngineRoom()) ? obj.getEngineRoom() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(18, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getFlootPass()) ? obj.getFlootPass() : "");
				cell.setCellStyle(style);

				cell = row.createCell(19, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getExplosionFactory()) ? obj.getExplosionFactory() : "");
				cell.setCellStyle(style);

				cell = row.createCell(20, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getSourceMacro()) ? obj.getSourceMacro() : "");
				cell.setCellStyle(style);

				cell = row.createCell(21, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getSourceRRU()) ? obj.getSourceRRU() : "");
				cell.setCellStyle(style);

				cell = row.createCell(22, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getGroundingPile()) ? obj.getGroundingPile() : "");
				cell.setCellStyle(style);

				cell = row.createCell(23, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getGroundingGem()) ? obj.getGroundingGem() : "");
				cell.setCellStyle(style);

				cell = row.createCell(24, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getGroundingDrill()) ? obj.getGroundingDrill() : "");
				cell.setCellStyle(style);

				cell = row.createCell(25, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getWire2x25()) ? obj.getWire2x25() : "");
				cell.setCellStyle(style);

				cell = row.createCell(26, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getWire2x35()) ? obj.getWire2x35() : "");
				cell.setCellStyle(style);

				cell = row.createCell(27, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getWire2x50()) ? obj.getWire2x50() : "");
				cell.setCellStyle(style);

				cell = row.createCell(28, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getWire2x70()) ? obj.getWire2x70() : "");
				cell.setCellStyle(style);

				cell = row.createCell(29, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getWire4x25()) ? obj.getWire4x25() : "");
				cell.setCellStyle(style);

				cell = row.createCell(30, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getWire4x35()) ? obj.getWire4x35() : "");
				cell.setCellStyle(style);

				cell = row.createCell(31, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getPillarAvailable()) ? obj.getPillarAvailable() : "");
				cell.setCellStyle(style);

				cell = row.createCell(32, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getTmPillar60()) ? obj.getTmPillar60() : "");
				cell.setCellStyle(style);

				cell = row.createCell(33, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getTmPillar70()) ? obj.getTmPillar60() : "");
				cell.setCellStyle(style);

				cell = row.createCell(34, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getTmPillar75()) ? obj.getTmPillar75() : "");
				cell.setCellStyle(style);

				cell = row.createCell(35, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getTmPillar80()) ? obj.getTmPillar80() : "");
				cell.setCellStyle(style);

				cell = row.createCell(36, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getTmPillar85()) ? obj.getTmPillar85() : "");
				cell.setCellStyle(style);

				cell = row.createCell(37, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getTmPillar100()) ? obj.getTmPillar100() : "");
				cell.setCellStyle(style);

				cell = row.createCell(38, CellType.STRING);
				cell.setCellValue((obj.getDesignDate() != null) ? obj.getDesignDate() : null);
				cell.setCellStyle(styleCenter);

				cell = row.createCell(39, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getDesignUserName()) ? obj.getDesignUserName() : "");
				cell.setCellStyle(style);

				cell = row.createCell(40, CellType.STRING);
				cell.setCellValue((obj.getDesignUpdateDate() != null) ? obj.getDesignUpdateDate() : null);
				cell.setCellStyle(styleCenter);

				cell = row.createCell(41, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getDesignUpdateUserName()) ? obj.getDesignUpdateUserName() : "");
				cell.setCellStyle(style);

				cell = row.createCell(42, CellType.STRING);
				cell.setCellValue((obj.getEstimatingDate() != null) ? obj.getEstimatingDate() : null);
				cell.setCellStyle(styleCenter);
				
				cell = row.createCell(43, CellType.STRING);
				cell.setCellValue((obj.getCreatDesignEstimatesUserName() != null) ? obj.getCreatDesignEstimatesUserName() : null);
				cell.setCellStyle(styleCenter);

				cell = row.createCell(44, CellType.STRING);
				cell.setCellValue(StringUtils.isNotEmpty(obj.getNode()) ? obj.getNode() : "");
				cell.setCellStyle(style);

			}
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();
		String path = UEncrypt
				.encryptFileUploadPath(uploadPathReturn + File.separator + "Tham_Thiet_Ke_Du_Toan.xlsx");
		return path;
		
	}

	public DataListDTO doSearchFile(UtilAttachDocumentDTO obj) throws Exception {
		// TODO Auto-generated method stub
		obj.setType(DEESIGN_ESTIMATES);
		  List<UtilAttachDocumentDTO> ls = utilAttachDocumentDAO.getByTypeAndObjectTC(obj.getObjectId(), obj.getType());
	        DataListDTO data = new DataListDTO();
	        data.setData(ls);
	        return data;
	}

	public void deleteFile(UtilAttachDocumentDTO obj) {
		// TODO Auto-generated method stub
		utilAttachDocumentDAO.updateUtilAttachDocumentById(obj.getUtilAttachDocumentId());
		
	}

	public Object getFile(String code) {
		// TODO Auto-generated method stub
		return designEstimatesDAO.getFile(code);
	}
}
