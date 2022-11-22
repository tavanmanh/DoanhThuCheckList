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
import java.util.HashMap;
import java.util.List;

import javax.activation.DataHandler;
import javax.servlet.http.HttpServletRequest;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
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

import com.viettel.coms.bo.WorkItemBO;
import com.viettel.coms.dao.RpBTSDAO;
import com.viettel.coms.dao.UtilAttachDocumentDAO;
import com.viettel.coms.dto.ManageVttbDTO;
import com.viettel.coms.dto.ReportBTSByDADTO;
import com.viettel.coms.dto.ReportDTO;
import com.viettel.coms.dto.ReportEffectiveDTO;
import com.viettel.coms.dto.RpBTSDTO;
import com.viettel.coms.dto.RpKHBTSDTO;
import com.viettel.coms.utils.ExcelUtils;
import com.viettel.ktts.vps.VpsPermissionChecker;
import com.viettel.ktts2.common.ChuyenTienRaChu;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.service.base.utils.StringUtils;
import com.viettel.utils.Constant;
import com.viettel.utils.ConvertData;
import com.viettel.utils.DateTimeUtils;

@Service("rpBTSBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RpBTSBusinessImpl extends BaseFWBusinessImpl<RpBTSDAO, RpBTSDTO, WorkItemBO> implements RpBTSBusiness {

	@Autowired
    private RpBTSDAO rpBTSDAO;
	@Value("${folder_upload2}")
	private String folder2Upload;
	@Value("${default_sub_folder_upload}")
	private String defaultSubFolderUpload;
	@Autowired
	UtilAttachDocumentDAO utilAttachDocumentDAO;
	
	public RpBTSBusinessImpl() {
        tModel = new WorkItemBO();
        tDAO = rpBTSDAO;
    }

    @Override
    public RpBTSDAO gettDAO() {
        return rpBTSDAO;
    }

    

    public DataListDTO doSearchBTS(RpBTSDTO obj) {
        List<RpBTSDTO> ls = rpBTSDAO.doSearchBTS(obj);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }
    
    //tatph-start-6/2/2020
    public DataListDTO doSearchCongNoTonVTAC(ManageVttbDTO obj) {
        List<ManageVttbDTO> ls = rpBTSDAO.doSearchCongNoTonVTAC_TH(obj);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }
    
    public DataListDTO doSearchTongHopVTTB(ManageVttbDTO obj) {
        List<ManageVttbDTO> ls = rpBTSDAO.doSearchTongHopVTTB_TH(obj);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }
    
    public DataListDTO doSearchTongHopPXK(ManageVttbDTO obj) {
        List<ManageVttbDTO> ls = rpBTSDAO.doSearchTongHopPXK(obj);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }
    //tatph-end-6/2/2020
    

	@Override
	public long count() {
		return rpBTSDAO.count("RpBTSBO", null);
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
					&& !StringUtils.isStringNullOrEmpty(cell.toString())) {
				return false;
			}
		}
		return true;
	}
	
	private List<String> getStationCodeLst(InputStream inputStream) throws Exception {
		List<String> stationLst = new ArrayList<String>();
		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
		XSSFSheet sheet = workbook.getSheetAt(0);
		int count = 0;
		DataFormatter formatter = new DataFormatter();
		int countRowBlank = 0;
		for (Row row : sheet) {
			count++;
			if (count < 4)
				continue;
			if (checkIfRowIsEmpty(row)) {
				countRowBlank++;
				if (countRowBlank >= 3)
					break;
				else
					continue;
			}
			if (countRowBlank >= 3)
				break;
			countRowBlank = 0;
			String code = formatter.formatCellValue(row.getCell(0));
			stationLst.add(code.trim());
		}
		workbook.close();
		return stationLst;
	}
	
	private List<String> getContractCodeLst(InputStream inputStream) throws Exception {
		List<String> contractCodeLst = new ArrayList<String>();
		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
		XSSFSheet sheet = workbook.getSheetAt(0);
		int count = 0;
		DataFormatter formatter = new DataFormatter();
		int countRowBlank = 0;
		for (Row row : sheet) {
			count++;
			if (count < 4)
				continue;
			if (checkIfRowIsEmpty(row)) {
				countRowBlank++;
				if (countRowBlank >= 3)
					break;
				else
					continue;
			}
			if (countRowBlank >= 3)
				break;
			countRowBlank = 0;
			String code = formatter.formatCellValue(row.getCell(0));
			contractCodeLst.add(code.trim());
		}
		workbook.close();
		return contractCodeLst;
	}
	
	public List<String> readFileStation(Attachment attachments) throws Exception {
		DataHandler dataHandler = attachments.getDataHandler();
		InputStream inputStream = dataHandler.getInputStream();
		return getStationCodeLst(inputStream);
	}
	
	public List<String> readFileContract(Attachment attachments) throws Exception {
		DataHandler dataHandler = attachments.getDataHandler();
		InputStream inputStream = dataHandler.getInputStream();
		return getContractCodeLst(inputStream);
	}
	
	public String exportCompleteProgressBTS(RpBTSDTO obj) throws Exception {
		obj.setPage(null);
		obj.setPageSize(null);
		Date dateNow = new Date();
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "RpBTS_excel.xlsx"));
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
		OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "RpBTS_excel.xlsx");
		List<RpBTSDTO> data = rpBTSDAO.doSearchBTS(obj);
		XSSFSheet sheet = workbook.getSheetAt(0);
		XSSFCellStyle stt = ExcelUtils.styleText(sheet);
		stt.setAlignment(HorizontalAlignment.CENTER);
		Row rowS12 = sheet.createRow(2);
		Cell cellS12 = rowS12.createCell(12, CellType.STRING);
		cellS12.setCellValue("Ngày lập báo cáo:  " + (DateTimeUtils.convertDateToString(dateNow, "dd/MM/yyyy")));
		cellS12.setCellStyle(stt);

		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleNumber(sheet);
			styleNumber.setDataFormat(workbook.createDataFormat().getFormat("0"));
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			int i = 5;
			for (RpBTSDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue(0 + (i - 5));
				cell.setCellStyle(stt);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getChiNhanh() != null) ? dto.getChiNhanh() : "");
				cell.setCellStyle(style);
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getProvinceCode() != null) ? dto.getProvinceCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getCat_station_house_code() != null) ? dto.getCat_station_house_code() : "");
				cell.setCellStyle(style);
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getCnt_contract_code() != null) ? dto.getCnt_contract_code() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getXDDaCoMb() != null) ? dto.getXDDaCoMb() : 0);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getXDCanGPXD() != null) ? dto.getXDCanGPXD() : 0);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue((dto.getXDDaCoGPXD() != null) ? dto.getXDDaCoGPXD() : 0);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue((dto.getXDChuaCo() != null) ? dto.getXDChuaCo() : 0);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue((dto.getXDDuDKNhanBGMB() != null) ? dto.getXDDuDKNhanBGMB() : 0);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue((dto.getXDDaNhanBGMB() != null) ? dto.getXDDaNhanBGMB() : 0);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue((dto.getXDDuDKChuaDiNhan() != null) ? dto.getXDDuDKChuaDiNhan() : 0);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue((dto.getQuahan_khoicong_XD() != null) ? dto.getQuahan_khoicong_XD() : 0);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue((dto.getXDDaVaoTK() != null) ? dto.getXDDaVaoTK() : 0);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(14, CellType.STRING);
				cell.setCellValue((dto.getXDNhanChuaTK() != null) ? dto.getXDNhanChuaTK() : 0);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(15, CellType.STRING);
				cell.setCellValue((dto.getXDDangTKXDDoDang() != null) ? dto.getXDDangTKXDDoDang() : 0);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(16, CellType.STRING);
				cell.setCellValue((dto.getDangtrienkhai_XD() != null) ? dto.getDangtrienkhai_XD() : 0);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(17, CellType.STRING);
				cell.setCellValue((dto.getCDNhanBGDiemDauNoi() != null) ? dto.getCDNhanBGDiemDauNoi() : 0);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(18, CellType.STRING);
				cell.setCellValue((dto.getCDVuong() != null) ? dto.getCDVuong() : 0);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(19, CellType.STRING);
				cell.setCellValue((dto.getCDDangTK() != null) ? dto.getCDDangTK() : 0);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(20, CellType.STRING);
				cell.setCellValue((dto.getCDChuaTK() != null) ? dto.getCDChuaTK() : 0);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(21, CellType.STRING);
				cell.setCellValue((dto.getCDTCXongDien() != null) ? dto.getCDTCXongDien() : 0);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(22, CellType.STRING);
				cell.setCellValue((dto.getLDDuDKChuaCap() != null) ? dto.getLDDuDKChuaCap() : 0);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(23, CellType.STRING);
				cell.setCellValue((dto.getLDCapChuaLap() != null) ? dto.getLDCapChuaLap() : 0);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(24, CellType.STRING);
				cell.setCellValue((dto.getLDVuongLD() != null) ? dto.getLDVuongLD() : 0);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(25, CellType.STRING);
				cell.setCellValue((dto.getLDDangLap() != null) ? dto.getLDDangLap() : 0);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(26, CellType.STRING);
				cell.setCellValue((dto.getLDTCXongLapDung() != null) ? dto.getLDTCXongLapDung() : 0);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(27, CellType.STRING);
				cell.setCellValue((dto.getBTSDuDKChuaCapBTS() != null) ? dto.getBTSDuDKChuaCapBTS() : 0);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(28, CellType.STRING);
				cell.setCellValue((dto.getBTSCapChuaLap() != null) ? dto.getBTSCapChuaLap() : 0);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(29, CellType.STRING);
				cell.setCellValue((dto.getBTSDangLap() != null) ? dto.getBTSDangLap() : 0);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(30, CellType.STRING);
				cell.setCellValue((dto.getBTSTCXongBTS() != null) ? dto.getBTSTCXongBTS() : 0);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(31, CellType.STRING);
				cell.setCellValue((dto.getTramXongDB() != null) ? dto.getTramXongDB() : 0);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(31, CellType.STRING);
				cell.setCellValue((dto.getTCQuahan() != null) ? dto.getTCQuahan() : 0);
				cell.setCellStyle(styleNumber);
			}
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "RpBTS_excel.xlsx");
		return path;
	}
	
	//Huypq-20191126-start
	public DataListDTO doSearchChart(RpBTSDTO obj){
		List<RpBTSDTO> ls = rpBTSDAO.doSearchKpi(obj);
		
		List<RpBTSDTO> lstSys = rpBTSDAO.getListTtkt();
		HashMap<String, String> mapCons = new HashMap<>();
		for(RpBTSDTO dto : lstSys) {
			mapCons.put(dto.getSysGroupCode(), dto.getSysGroupName());
		}
		
		for(RpBTSDTO sys : ls) {
			if(mapCons.get(sys.getSysGroupCode())!=null) {
				sys.setSysGroupName(mapCons.get(sys.getSysGroupCode()));
			}
		}
		
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setSize(obj.getTotalRecord());
		data.setStart(obj.getPageSize());
		data.setTotal(obj.getTotalRecord());
		return data;
	}
	
	public List<RpBTSDTO> getDataChart(RpBTSDTO obj){
		obj.setPage(null);
		obj.setPageSize(null);
		List<RpBTSDTO> ls = rpBTSDAO.getDataChart(obj); //list data có số ct
		List<RpBTSDTO> lstSys = rpBTSDAO.getListTtkt(); //list sysGroup
		HashMap<String, Long> mapCons = new HashMap<>();
		for(RpBTSDTO dto : ls) {
			mapCons.put(dto.getSysGroupCode(), dto.getCountConstruction());
		}
		for(RpBTSDTO sys : lstSys) {
			if(mapCons.get(sys.getSysGroupCode())!=null) {
				sys.setCountConstruction(mapCons.get(sys.getSysGroupCode()));
			} else {
				sys.setCountConstruction(0l);
			}
		}
		return lstSys;
	}
	
	public String exportFileDetailKpi45Days(RpBTSDTO obj) throws Exception{
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "BM_BC_QuaHan_45_Ngay.xlsx"));
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
		OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "BM_BC_QuaHan_45_Ngay.xlsx");
		
		List<RpBTSDTO> data = rpBTSDAO.doSearchKpi(obj);
		List<RpBTSDTO> lstSys = rpBTSDAO.getListTtkt();
		HashMap<String, String> mapCons = new HashMap<>();
		for(RpBTSDTO dto : lstSys) {
			mapCons.put(dto.getSysGroupCode(), dto.getSysGroupName());
		}
		
		for(RpBTSDTO sys : data) {
			if(mapCons.get(sys.getSysGroupCode())!=null) {
				sys.setSysGroupName(mapCons.get(sys.getSysGroupCode()));
			}
		}
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			
			XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
			styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			int i = 2;
			Long sum = 0l;
			for (RpBTSDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 2));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getConstructionCode() != null) ? dto.getConstructionCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getValueConstruction() != null) ? dto.getValueConstruction() : 0l);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getSysGroupName() != null) ? (dto.getSysGroupCode()+ "-" + dto.getSysGroupName()) : "");
				cell.setCellStyle(style);
				
				sum += dto.getValueConstruction();
			}
			
			Row rowSum = sheet.createRow(i++);
			Cell cellSum = rowSum.createCell(0, CellType.STRING);
			cellSum.setCellValue("TỔNG");
			cellSum.setCellStyle(style);
			cellSum = rowSum.createCell(1, CellType.STRING);
			cellSum.setCellValue(data.size());
			cellSum.setCellStyle(styleNumber);
			cellSum = rowSum.createCell(2, CellType.STRING);
			cellSum.setCellValue(sum);
			cellSum.setCellStyle(styleCurrency);
			cellSum = rowSum.createCell(3, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(style);
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "BM_BC_QuaHan_45_Ngay.xlsx");
		return path;
	}
	//Huy-end
	
	//tatph-start-6/2/2019
	public DataListDTO doSearchChart60days(RpBTSDTO obj){
		List<RpBTSDTO> ls = rpBTSDAO.doSearchKpi60days(obj);
		
		List<RpBTSDTO> lstSys = rpBTSDAO.getListTtkt();
		HashMap<String, String> mapCons = new HashMap<>();
		for(RpBTSDTO dto : lstSys) {
			mapCons.put(dto.getSysGroupCode(), dto.getSysGroupName());
		}
		
		for(RpBTSDTO sys : ls) {
			if(mapCons.get(sys.getSysGroupCode())!=null) {
				sys.setSysGroupName(mapCons.get(sys.getSysGroupCode()));
			}
		}
		
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setSize(obj.getTotalRecord());
		data.setStart(obj.getPageSize());
		data.setTotal(obj.getTotalRecord());
		return data;
	}
	
	public List<RpBTSDTO> getDataChart60days(RpBTSDTO obj){
		obj.setPage(null);
		obj.setPageSize(null);
		List<RpBTSDTO> ls = rpBTSDAO.getDataChart60days(obj); //list data có số ct
		List<RpBTSDTO> lstSys = rpBTSDAO.getListTtkt(); //list sysGroup
		HashMap<String, Long> mapCons = new HashMap<>();
		for(RpBTSDTO dto : ls) {
			mapCons.put(dto.getSysGroupCode(), dto.getCountConstruction());
		}
		for(RpBTSDTO sys : lstSys) {
			if(mapCons.get(sys.getSysGroupCode())!=null) {
				sys.setCountConstruction(mapCons.get(sys.getSysGroupCode()));
			} else {
				sys.setCountConstruction(0l);
			}
		}
		return lstSys;
	}
	
	public String exportFileDetailKpi60Days(RpBTSDTO obj) throws Exception{
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "BM_BC_QuaHan_60_Ngay.xlsx"));
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
		OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "BM_BC_QuaHan_60_Ngay.xlsx");
		
		List<RpBTSDTO> data = rpBTSDAO.doSearchKpi60days(obj);
		List<RpBTSDTO> lstSys = rpBTSDAO.getListTtkt();
		HashMap<String, String> mapCons = new HashMap<>();
		for(RpBTSDTO dto : lstSys) {
			mapCons.put(dto.getSysGroupCode(), dto.getSysGroupName());
		}
		
		for(RpBTSDTO sys : data) {
			if(mapCons.get(sys.getSysGroupCode())!=null) {
				sys.setSysGroupName(mapCons.get(sys.getSysGroupCode()));
			}
		}
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			
			XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
			styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			int i = 2;
			Long sum = 0l;
			for (RpBTSDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 2));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getConstructionCode() != null) ? dto.getConstructionCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getValueConstruction() != null) ? dto.getValueConstruction() : 0l);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getSysGroupName() != null) ? (dto.getSysGroupCode()+ "-" + dto.getSysGroupName()) : "");
				cell.setCellStyle(style);
				
				sum += dto.getValueConstruction();
			}
			
			Row rowSum = sheet.createRow(i++);
			Cell cellSum = rowSum.createCell(0, CellType.STRING);
			cellSum.setCellValue("TỔNG");
			cellSum.setCellStyle(style);
			cellSum = rowSum.createCell(1, CellType.STRING);
			cellSum.setCellValue(data.size());
			cellSum.setCellStyle(styleNumber);
			cellSum = rowSum.createCell(2, CellType.STRING);
			cellSum.setCellValue(sum);
			cellSum.setCellStyle(styleCurrency);
			cellSum = rowSum.createCell(3, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(style);
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "BM_BC_QuaHan_60_Ngay.xlsx");
		return path;
	}
	
	// <------------------------------------------------------------------------------------------------------------------------>
	
	public DataListDTO doSearchChart135days(RpBTSDTO obj){
		List<RpBTSDTO> ls = rpBTSDAO.doSearchKpi135days(obj);
		
		List<RpBTSDTO> lstSys = rpBTSDAO.getListTtkt();
		HashMap<String, String> mapCons = new HashMap<>();
		for(RpBTSDTO dto : lstSys) {
			mapCons.put(dto.getSysGroupCode(), dto.getSysGroupName());
		}
		
		for(RpBTSDTO sys : ls) {
			if(mapCons.get(sys.getSysGroupCode())!=null) {
				sys.setSysGroupName(mapCons.get(sys.getSysGroupCode()));
			}
		}
		
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setSize(obj.getTotalRecord());
		data.setStart(obj.getPageSize());
		data.setTotal(obj.getTotalRecord());
		return data;
	}
	
	public List<RpBTSDTO> getDataChart135days(RpBTSDTO obj){
		obj.setPage(null);
		obj.setPageSize(null);
		List<RpBTSDTO> ls = rpBTSDAO.getDataChart135days(obj); //list data có số ct
		List<RpBTSDTO> lstSys = rpBTSDAO.getListTtkt(); //list sysGroup
		HashMap<String, Long> mapCons = new HashMap<>();
		for(RpBTSDTO dto : ls) {
			mapCons.put(dto.getSysGroupCode(), dto.getCountConstruction());
		}
		for(RpBTSDTO sys : lstSys) {
			if(mapCons.get(sys.getSysGroupCode())!=null) {
				sys.setCountConstruction(mapCons.get(sys.getSysGroupCode()));
			} else {
				sys.setCountConstruction(0l);
			}
		}
		return lstSys;
	}
	
	public String exportFileDetailKpi135Days(RpBTSDTO obj) throws Exception{
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "BM_BC_QuaHan_135_Ngay.xlsx"));
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
		OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "BM_BC_QuaHan_135_Ngay.xlsx");
		
		List<RpBTSDTO> data = rpBTSDAO.doSearchKpi135days(obj);
		List<RpBTSDTO> lstSys = rpBTSDAO.getListTtkt();
		HashMap<String, String> mapCons = new HashMap<>();
		for(RpBTSDTO dto : lstSys) {
			mapCons.put(dto.getSysGroupCode(), dto.getSysGroupName());
		}
		
		for(RpBTSDTO sys : data) {
			if(mapCons.get(sys.getSysGroupCode())!=null) {
				sys.setSysGroupName(mapCons.get(sys.getSysGroupCode()));
			}
		}
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			
			XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
			styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			int i = 2;
			Long sum = 0l;
			for (RpBTSDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 2));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getConstructionCode() != null) ? dto.getConstructionCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getValueConstruction() != null) ? dto.getValueConstruction() : 0l);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getSysGroupName() != null) ? (dto.getSysGroupCode()+ "-" + dto.getSysGroupName()) : "");
				cell.setCellStyle(style);
				
				sum += dto.getValueConstruction();
			}
			
			Row rowSum = sheet.createRow(i++);
			Cell cellSum = rowSum.createCell(0, CellType.STRING);
			cellSum.setCellValue("TỔNG");
			cellSum.setCellStyle(style);
			cellSum = rowSum.createCell(1, CellType.STRING);
			cellSum.setCellValue(data.size());
			cellSum.setCellStyle(styleNumber);
			cellSum = rowSum.createCell(2, CellType.STRING);
			cellSum.setCellValue(sum);
			cellSum.setCellStyle(styleCurrency);
			cellSum = rowSum.createCell(3, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(style);
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "BM_BC_QuaHan_135_Ngay.xlsx");
		return path;
	}
	
	public String convertContractType(Long type , Long typeO , String typeOsName) {
		if(type == 0 ) {
			return "Hợp đồng xây lắp đầu ra";
		}else if (type == 1){
			return "Hợp đồng xây lắp đầu vào";
		}else if (type == 2){
			return "Thương mại đầu ra";
		}else if (type == 3){
			return "Thương mại đầu vào";
		}else if (type == 4){
			return "Hợp đồng vật tư";
		}else if (type == 5){
			return "Hợp đồng vật tư";
		}else if (type == 6){
			return "Hợp đồng vật tư";
		}else if (type == 7){
			return "Hạ tâng cho thuê đầu ra";
		}else if (type == 8){
			return "Hạ tầng cho thuê đầu vào";
		}else if (type == 9){
			return "Xí nghiệp xây dựng";
		}else {
			return "";
		}
	}
	
	public String convertConstructionStatus(String status ) {
		if("0".equals(status)) {
			return "Đã hủy";
		}else if("1".equals(status)) {
			return "Chờ bàn giao mặt bằng";
		}else if("2".equals(status)) {
			return "Chờ khởi công";
		}else if("3".equals(status)) {
			return "Đang thực hiện";
		}else if("4".equals(status)) {
			return "Đã tạm dừng";
		}else if("5".equals(status)) {
			return "Đã hoàn thành";
		}else if("6".equals(status)) {
			return "Đã nghiệm thu";
		}else if("7".equals(status)) {
			return "Đã hoàn công";
		}else if("8".equals(status)) {
			return "Đã quyết toán";
		}else {
			return "";
		}
	}
	
	public String exportFileCongNoTonVTAC(ManageVttbDTO obj) throws Exception{
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "BM_BC_Du_No_VTAC.xlsx"));
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
		OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "BM_BC_Du_No_VTAC.xlsx");
		
		List<ManageVttbDTO> data = rpBTSDAO.doSearchCongNoTonVTAC_CT(obj);
		List<ManageVttbDTO> data_TH = rpBTSDAO.doSearchCongNoTonVTAC_TH(obj);
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		//sheet 1 - Th
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (data_TH != null && !data_TH.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleBold = ExcelUtils.styleBold(sheet);
			XSSFCellStyle styleBoldCurrency = ExcelUtils.styleBoldCurrency(sheet);
			styleBoldCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##"));
			XSSFCellStyle styleCurrency = ExcelUtils.styleCurrencyV2(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			int i = 6;
			Long soLuongDDK = 0l;
			Long ttDDK = 0l;
			Long soLuongNTK = 0l;
			Long ttNTK = 0l;
			Long soLuongQT = 0l;
			Long ttQT = 0l;
			Long soLuongTCK = 0l;
			Long ttTCK = 0l;
			for (ManageVttbDTO dto : data_TH) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 6));
				cell.setCellStyle(styleNumber);
				//
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue(dto.getSysGroupName() != null ? dto.getSysGroupName() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue(dto.getContractCode() != null ? dto.getContractCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue(dto.getContractType() != null ? this.convertContractType(dto.getContractType(),dto.getContractTypeO(),dto.getContractTypeOsName()) : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue(dto.getConstructionCode() != null ? dto.getConstructionCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue(dto.getConstructionStatus() != null ? this.convertConstructionStatus(dto.getConstructionStatus()) : "");
				cell.setCellStyle(style);
			
				//
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue(dto.getSoLuongDDK() != null ? dto.getSoLuongDDK() : 0L);
				cell.setCellStyle(styleCurrency);
				//
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue(dto.getTtDDK() != null ? dto.getTtDDK() : 0L);
				cell.setCellStyle(styleCurrency);
				//
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue(dto.getSoLuongNTK() != null ? dto.getSoLuongNTK() : 0L);
				cell.setCellStyle(styleCurrency);
				//
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue(dto.getTtNTK() != null ? dto.getTtNTK() : 0L);
				cell.setCellStyle(styleCurrency);
				//
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue(dto.getSoLuongQT() != null ? dto.getSoLuongQT() : 0L);
				cell.setCellStyle(styleCurrency);
				//
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue(dto.getTtQT() != null ? dto.getTtQT() : 0L);
				cell.setCellStyle(styleCurrency);
				//
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue(dto.getSoLuongTCK() != null ? dto.getSoLuongTCK() : 0L);
				cell.setCellStyle(styleCurrency);
				//
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue(dto.getTtTCK() != null ? dto.getTtTCK() : 0L);
				cell.setCellStyle(styleCurrency);
				
				 soLuongDDK += dto.getSoLuongDDK();
				 ttDDK += dto.getTtDDK();
				 soLuongNTK += dto.getSoLuongNTK();
				 ttNTK += dto.getTtNTK();
				 soLuongQT += dto.getSoLuongQT();
				 ttQT += dto.getTtQT();
				 soLuongTCK += dto.getSoLuongTCK();
				 ttTCK += dto.getTtTCK();
				
				
				
			}
			
			Row rowSum = sheet.createRow(i++);
			Cell cellSum = rowSum.createCell(0, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(1, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(2, CellType.STRING);
			cellSum.setCellValue("Tổng cộng");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(3, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(4, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(5, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			
			
			//
			cellSum = rowSum.createCell(6, CellType.STRING);
			cellSum.setCellValue(soLuongDDK);
			cellSum.setCellStyle(styleBoldCurrency);
			//
			cellSum = rowSum.createCell(7, CellType.STRING);
			cellSum.setCellValue(ttDDK);
			cellSum.setCellStyle(styleBoldCurrency);
			//
			cellSum = rowSum.createCell(8, CellType.STRING);
			cellSum.setCellValue(soLuongNTK);
			cellSum.setCellStyle(styleBoldCurrency);
			//
			cellSum = rowSum.createCell(9, CellType.STRING);
			cellSum.setCellValue(ttNTK);
			cellSum.setCellStyle(styleBoldCurrency);
			//
			cellSum = rowSum.createCell(10, CellType.STRING);
			cellSum.setCellValue(soLuongQT);
			cellSum.setCellStyle(styleBoldCurrency);
			//
			cellSum = rowSum.createCell(11, CellType.STRING);
			cellSum.setCellValue(ttQT);
			cellSum.setCellStyle(styleBoldCurrency);
			//
			cellSum = rowSum.createCell(12, CellType.STRING);
			cellSum.setCellValue(soLuongTCK);
			cellSum.setCellStyle(styleBoldCurrency);
			//
			cellSum = rowSum.createCell(13, CellType.STRING);
			cellSum.setCellValue(ttTCK);
			cellSum.setCellStyle(styleBoldCurrency);
			
			
		}
		//sheet 2 - CT
		XSSFSheet sheet2 = workbook.getSheetAt(1);
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet2);
			XSSFCellStyle styleBold = ExcelUtils.styleBold(sheet2);
			XSSFCellStyle styleBoldCurrency = ExcelUtils.styleBoldCurrency(sheet2);
			styleBoldCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##"));
			XSSFCellStyle styleCurrency = ExcelUtils.styleCurrencyV2(sheet2);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet2);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			int i = 6;
			Long soLuongDDK = 0l;
			Long ttDDK = 0l;
			Long soLuongNTK = 0l;
			Long ttNTK = 0l;
			Long soLuongQT = 0l;
			Long ttQT = 0l;
			Long soLuongTCK = 0l;
			Long ttTCK = 0l;
			for (ManageVttbDTO dto : data) {
				Row row = sheet2.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 6));
				cell.setCellStyle(styleNumber);
				//
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue(dto.getSysGroupName() != null ? dto.getSysGroupName() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue(dto.getProjectCode() != null ? dto.getProjectCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue(dto.getContractCode() != null ? dto.getContractCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue(dto.getConstructionCode() != null ? dto.getConstructionCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue(dto.getVttbCode() != null ? dto.getVttbCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue(dto.getVttbName() != null ? dto.getVttbName() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue(dto.getDvt() != null ? dto.getDvt() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue(dto.getSoLuongDDK() != null ? dto.getSoLuongDDK() : 0L);
				cell.setCellStyle(styleCurrency);
				//
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue(dto.getTtDDK() != null ? dto.getTtDDK() : 0L);
				cell.setCellStyle(styleCurrency);
				//
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue(dto.getSoLuongNTK() != null ? dto.getSoLuongNTK() : 0L);
				cell.setCellStyle(styleCurrency);
				//
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue(dto.getTtNTK() != null ? dto.getTtNTK() : 0L);
				cell.setCellStyle(styleCurrency);
				//
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue(dto.getSoLuongQT() != null ? dto.getSoLuongQT() : 0L);
				cell.setCellStyle(styleCurrency);
				//
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue(dto.getTtQT() != null ? dto.getTtQT() : 0L);
				cell.setCellStyle(styleCurrency);
				//
				cell = row.createCell(14, CellType.STRING);
				cell.setCellValue(dto.getSoLuongTCK() != null ? dto.getSoLuongTCK() : 0L);
				cell.setCellStyle(styleCurrency);
				//
				cell = row.createCell(15, CellType.STRING);
				cell.setCellValue(dto.getTtTCK() != null ? dto.getTtTCK() : 0L);
				cell.setCellStyle(styleCurrency);
				
				 soLuongDDK += dto.getSoLuongDDK();
				 ttDDK += dto.getTtDDK();
				 soLuongNTK += dto.getSoLuongNTK();
				 ttNTK += dto.getTtNTK();
				 soLuongQT += dto.getSoLuongQT();
				 ttQT += dto.getTtQT();
				 soLuongTCK += dto.getSoLuongTCK();
				 ttTCK += dto.getTtTCK();
				
				
				
			}
			
			Row rowSum = sheet2.createRow(i++);
			Cell cellSum = rowSum.createCell(0, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(1, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(2, CellType.STRING);
			cellSum.setCellValue("Tổng cộng");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(3, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(4, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(5, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(6, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(7, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			
			//
			cellSum = rowSum.createCell(8, CellType.STRING);
			cellSum.setCellValue(soLuongDDK);
			cellSum.setCellStyle(styleBoldCurrency);
			//
			cellSum = rowSum.createCell(9, CellType.STRING);
			cellSum.setCellValue(ttDDK);
			cellSum.setCellStyle(styleBoldCurrency);
			//
			cellSum = rowSum.createCell(10, CellType.STRING);
			cellSum.setCellValue(soLuongNTK);
			cellSum.setCellStyle(styleBoldCurrency);
			//
			cellSum = rowSum.createCell(11, CellType.STRING);
			cellSum.setCellValue(ttNTK);
			cellSum.setCellStyle(styleBoldCurrency);
			//
			cellSum = rowSum.createCell(12, CellType.STRING);
			cellSum.setCellValue(soLuongQT);
			cellSum.setCellStyle(styleBoldCurrency);
			//
			cellSum = rowSum.createCell(13, CellType.STRING);
			cellSum.setCellValue(ttQT);
			cellSum.setCellStyle(styleBoldCurrency);
			//
			cellSum = rowSum.createCell(14, CellType.STRING);
			cellSum.setCellValue(soLuongTCK);
			cellSum.setCellStyle(styleBoldCurrency);
			//
			cellSum = rowSum.createCell(15, CellType.STRING);
			cellSum.setCellValue(ttTCK);
			cellSum.setCellStyle(styleBoldCurrency);
			
			
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "BM_BC_Du_No_VTAC.xlsx");
		return path;
	}
	
	public String exportFileTongHopVTTB(ManageVttbDTO obj) throws Exception{
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "BM_BC_Tong_hop_VTTB.xlsx"));
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
		OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "BM_BC_Tong_hop_VTTB.xlsx");
		
		List<ManageVttbDTO> data_TH = rpBTSDAO.doSearchTongHopVTTB_TH(obj);
		List<ManageVttbDTO> data_CT = rpBTSDAO.doSearchTongHopVTTB_CT(obj);
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		//sheet 1 - Th
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (data_TH != null && !data_TH.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleBold = ExcelUtils.styleBold(sheet);
			XSSFCellStyle styleBoldCurrency = ExcelUtils.styleBoldCurrency(sheet);
			styleBoldCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##"));
			XSSFCellStyle styleCurrency = ExcelUtils.styleCurrencyV2(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			int i = 5;
			Long soLuongPxk = 0l;
			Long soLuongXacNhan = 0l;
			for (ManageVttbDTO dto : data_TH) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 5));
				cell.setCellStyle(styleNumber);
				//
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue(dto.getConstructionCode() != null ? dto.getConstructionCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue(dto.getContractCode() != null ? dto.getContractCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue(dto.getContractType() != null ? this.convertContractType(dto.getContractType(),dto.getContractTypeO(),dto.getContractTypeOsName()) : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue(dto.getStationCode() != null ? dto.getStationCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue(dto.getGiaTriPxk() != null ? dto.getGiaTriPxk() : 0L);
				cell.setCellStyle(styleCurrency);
				//
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue(dto.getGiaTriXacNhan() != null ? dto.getGiaTriXacNhan() : 0L);
				cell.setCellStyle(styleCurrency);
				//
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue(dto.getSysGroupName() != null ? dto.getSysGroupName() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue(dto.getConstructionStatus() != null ? this.convertConstructionStatus(dto.getConstructionStatus()) : "");
				cell.setCellStyle(style);
				
				soLuongPxk += dto.getGiaTriPxk();
				soLuongXacNhan += dto.getGiaTriXacNhan();
				
				
				
			}
			
			Row rowSum = sheet.createRow(i++);
			Cell cellSum = rowSum.createCell(0, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(1, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(2, CellType.STRING);
			cellSum.setCellValue("Tổng cộng");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(3, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(4, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(5, CellType.STRING);
			cellSum.setCellValue(soLuongPxk);
			cellSum.setCellStyle(styleBoldCurrency);
			//
			cellSum = rowSum.createCell(6, CellType.STRING);
			cellSum.setCellValue(soLuongXacNhan);
			cellSum.setCellStyle(styleBoldCurrency);
			//
			cellSum = rowSum.createCell(7, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(8, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			
			
			
		}
		//sheet 2 - CT
		XSSFSheet sheet2 = workbook.getSheetAt(1);
		if (data_CT != null && !data_CT.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet2);
			XSSFCellStyle styleBold = ExcelUtils.styleBold(sheet2);
			XSSFCellStyle styleBoldCurrency = ExcelUtils.styleBoldCurrency(sheet2);
			styleBoldCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##"));
			XSSFCellStyle styleCurrency = ExcelUtils.styleCurrencyV2(sheet2);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet2);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			int i = 5;
			Long soLuongPxk = 0l;
			Long soLuongXacNhan = 0l;
			for (ManageVttbDTO dto : data_CT) {
				Row row = sheet2.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 5));
				cell.setCellStyle(styleNumber);
				//
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue(dto.getPxkCode() != null ? dto.getPxkCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue(dto.getPxkDateS() != null ? dto.getPxkDateS() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue(dto.getStationCode() != null ? dto.getStationCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue(dto.getContractCode() != null ? dto.getContractCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue(dto.getContractType() != null ? convertContractType(dto.getContractType(), dto.getContractTypeO(), dto.getContractTypeOsName()) : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue(dto.getConstructionCode() != null ? dto.getConstructionCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue(dto.getVttbCode() != null ? dto.getVttbCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue(dto.getVttbName() != null ? dto.getVttbName() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue(dto.getGiaTriPxk() != null ? dto.getGiaTriPxk() : 0L);
				cell.setCellStyle(styleCurrency);
				//
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue(dto.getGiaTriXacNhan() != null ? dto.getGiaTriXacNhan() : 0L);
				cell.setCellStyle(styleCurrency);
				//
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue(dto.getSysGroupName() != null ? dto.getSysGroupName() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue(dto.getNguoiXN() != null ? dto.getNguoiXN() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue(dto.getNgayXN() != null ? dto.getNgayXN().toString() : "");
				cell.setCellStyle(style);
				//
				if(dto.getGiaTriChenhLech() != null) {
					if(dto.getGiaTriChenhLech() > 0 ) {
						cell = row.createCell(14, CellType.STRING);
						cell.setCellValue(dto.getGiaTriChenhLech());
						cell.setCellStyle(styleCurrency);
						//
						cell = row.createCell(15, CellType.STRING);
						cell.setCellValue(0L);
						cell.setCellStyle(styleCurrency);
					}else {
						cell = row.createCell(14, CellType.STRING);
						cell.setCellValue(0L);
						cell.setCellStyle(styleCurrency);
						//
						cell = row.createCell(15, CellType.STRING);
						cell.setCellValue(dto.getGiaTriChenhLech().toString().replace("-", ""));
						cell.setCellStyle(styleCurrency);
					}
				}else {
					cell = row.createCell(14, CellType.STRING);
					cell.setCellValue(0L);
					cell.setCellStyle(styleCurrency);
					//
					cell = row.createCell(15, CellType.STRING);
					cell.setCellValue(0L);
					cell.setCellStyle(styleCurrency);
				}
				
				
				 soLuongPxk += dto.getGiaTriPxk();
				 soLuongXacNhan += dto.getGiaTriXacNhan();
				
				
				
			}
			
			Row rowSum = sheet2.createRow(i++);
			Cell cellSum = rowSum.createCell(0, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(1, CellType.STRING);
			cellSum.setCellValue("Tổng cộng");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(2, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(3, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(4, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(5, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(6, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(7, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			
			//
			cellSum = rowSum.createCell(8, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(9, CellType.STRING);
			cellSum.setCellValue(soLuongPxk);
			cellSum.setCellStyle(styleBoldCurrency);
			//
			cellSum = rowSum.createCell(10, CellType.STRING);
			cellSum.setCellValue(soLuongXacNhan);
			cellSum.setCellStyle(styleBoldCurrency);
			//
			cellSum = rowSum.createCell(11, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(12, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(13, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(14, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(15, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			
			
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "BM_BC_Tong_hop_VTTB.xlsx");
		return path;
	}
	
	public String exportFileTongHopPXK(ManageVttbDTO obj) throws Exception{
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "BM_BC_Tong_hop_PXK.xlsx"));
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
		OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "BM_BC_Tong_hop_PXK.xlsx");
		
		List<ManageVttbDTO> data = rpBTSDAO.doSearchTongHopPXK(obj);
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		//sheet 1 - Th
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleBold = ExcelUtils.styleBold(sheet);
			XSSFCellStyle styleBoldCurrency = ExcelUtils.styleBoldCurrency(sheet);
			styleBoldCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##"));
			XSSFCellStyle styleCurrency = ExcelUtils.styleCurrencyV2(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			int i = 5;
			Long soLuongPxk = 0l;
			Long soLuongSuDung = 0l;
			Long donGia = 0l;
			Long giaTriSuDung = 0l;
			for (ManageVttbDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 5));
				cell.setCellStyle(styleNumber);
				//
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue(dto.getPxkCode() != null ? dto.getPxkCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue(dto.getPxkDateS() != null ? dto.getPxkDateS() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue(dto.getStockCode() != null ? dto.getStockCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue(dto.getConstructionCode() != null ? dto.getConstructionCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue(dto.getSysGroupName() != null ? dto.getSysGroupName() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue(dto.getProvinceCode() != null ? dto.getProvinceCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue(dto.getCatStationHouseCode() != null ? dto.getCatStationHouseCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue(dto.getStationCode() != null ? dto.getStationCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue(dto.getContractCode() != null ? dto.getContractCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue(dto.getVttbName() != null ? dto.getVttbName() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue(dto.getVttbCode() != null ? dto.getVttbCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue(dto.getDvt() != null ? dto.getDvt() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue(dto.getSerial() != null ? dto.getSerial() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(14, CellType.STRING);
				cell.setCellValue(dto.getSoLuongPxk() != null ? dto.getSoLuongPxk() : 0L);
				cell.setCellStyle(styleCurrency);
				//
				cell = row.createCell(15, CellType.STRING);
				cell.setCellValue(dto.getSoLuongSuDung() != null ? dto.getSoLuongSuDung() : 0L);
				cell.setCellStyle(styleCurrency);
				//
				cell = row.createCell(16, CellType.STRING);
				cell.setCellValue(dto.getDonGia() != null ? dto.getDonGia() : 0L);
				cell.setCellStyle(styleCurrency);
				//
				cell = row.createCell(17, CellType.STRING);
				cell.setCellValue(dto.getGiaTriSuDung() != null ? dto.getGiaTriSuDung() : 0L);
				cell.setCellStyle(styleCurrency);
				//
				cell = row.createCell(18, CellType.STRING);
				cell.setCellValue(dto.getStatus() != null ? dto.getStatus() : "");
				cell.setCellStyle(style);
				
				soLuongPxk += dto.getSoLuongPxk();
				soLuongSuDung += dto.getSoLuongSuDung();
				donGia += dto.getDonGia();
				giaTriSuDung += dto.getGiaTriSuDung();
				
				
				
			}
			
			Row rowSum = sheet.createRow(i++);
			Cell cellSum = rowSum.createCell(0, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(1, CellType.STRING);
			cellSum.setCellValue("Tổng cộng");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(2, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(3, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(4, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(5, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(6, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(7, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(8, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(9, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(10, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(11, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(12, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(13, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			//
			cellSum = rowSum.createCell(14, CellType.STRING);
			cellSum.setCellValue(soLuongPxk);
			cellSum.setCellStyle(styleBoldCurrency);
			//
			cellSum = rowSum.createCell(15, CellType.STRING);
			cellSum.setCellValue(soLuongSuDung);
			cellSum.setCellStyle(styleBoldCurrency);
			//
			cellSum = rowSum.createCell(16, CellType.STRING);
			cellSum.setCellValue(donGia);
			cellSum.setCellStyle(styleBoldCurrency);
			//
			cellSum = rowSum.createCell(17, CellType.STRING);
			cellSum.setCellValue(giaTriSuDung);
			cellSum.setCellStyle(styleBoldCurrency);
			//
			cellSum = rowSum.createCell(18, CellType.STRING);
			cellSum.setCellValue("");
			cellSum.setCellStyle(styleBold);
			
			
			
		}
		
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "BM_BC_Tong_hop_PXK.xlsx");
		return path;
	}
	
	
	public List<RpBTSDTO> getDataChartPxk7days(RpBTSDTO obj){
		obj.setPage(null);
		obj.setPageSize(null);
		List<RpBTSDTO> ls = rpBTSDAO.getDataChartPxk7days(obj); //list data có số ct
		List<RpBTSDTO> lstSys = rpBTSDAO.getListTtkt(); //list sysGroup
		HashMap<String, Long> mapCons = new HashMap<>();
		for(RpBTSDTO dto : ls) {
			mapCons.put(dto.getSysGroupCode(), dto.getCountConstruction());
		}
		for(RpBTSDTO sys : lstSys) {
			if(mapCons.get(sys.getSysGroupCode())!=null) {
				sys.setCountConstruction(mapCons.get(sys.getSysGroupCode()));
			} else {
				sys.setCountConstruction(0l);
			}
		}
		return lstSys;
	}
	
	public String exportChartPxk7Days(RpBTSDTO obj) throws Exception{
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "BM_BC_PXK_Qua_Han_7_Ngay.xlsx"));
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
		OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "BM_BC_PXK_Qua_Han_7_Ngay.xlsx");
		
		List<ManageVttbDTO> data = rpBTSDAO.doSearchPxk7days(obj);
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			
			XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
			styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			int i = 5;
			for (ManageVttbDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 5));
				cell.setCellStyle(styleNumber);
				//
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getStockCode() != null) ? dto.getStockCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getPxkCode() != null) ? dto.getPxkCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getTransDate() != null) ? dto.getTransDate() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getContractCode() != null) ? dto.getContractCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getConstructionCode() != null) ? dto.getConstructionCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getSysGroupName() != null) ? dto.getSysGroupName() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue((dto.getNgayQuaHan() != null) ? dto.getNgayQuaHan() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue((dto.getTransMan() != null) ? dto.getTransMan() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue((dto.getEmail() != null) ? dto.getEmail() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue((dto.getPhoneNumber() != null) ? dto.getPhoneNumber() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue((dto.getStatus() != null) ? dto.getStatus() : "");
				cell.setCellStyle(style);
				
				
			}
			
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "BM_BC_PXK_Qua_Han_7_Ngay.xlsx");
		return path;
	}
	
	public List<RpBTSDTO> getDataChartVttb45days(RpBTSDTO obj){
		obj.setPage(null);
		obj.setPageSize(null);
		List<RpBTSDTO> ls = rpBTSDAO.getDataChartVttb45days(obj); //list data có số ct
		List<RpBTSDTO> lstSys = rpBTSDAO.getListTtkt(); //list sysGroup
		HashMap<String, Long> mapCons = new HashMap<>();
		for(RpBTSDTO dto : ls) {
			mapCons.put(dto.getSysGroupCode(), dto.getCountConstruction());
		}
		for(RpBTSDTO sys : lstSys) {
			if(mapCons.get(sys.getSysGroupCode())!=null) {
				sys.setCountConstruction(mapCons.get(sys.getSysGroupCode()));
			} else {
				sys.setCountConstruction(0l);
			}
		}
		return lstSys;
	}
	
	public String exportChartVttb45Days(RpBTSDTO obj) throws Exception{
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "BM_BC_VTTB_Qua_Han_45_Ngay.xlsx"));
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
		OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "BM_BC_VTTB_Qua_Han_45_Ngay.xlsx");
		
		List<ManageVttbDTO> data = rpBTSDAO.doSearchVttb45days(obj);
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			
			XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
			styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			int i = 5;
			for (ManageVttbDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 5));
				cell.setCellStyle(styleNumber);
				//
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getStockCode() != null) ? dto.getStockCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getPxkCode() != null) ? dto.getPxkCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getTransDate() != null) ? dto.getTransDate() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getContractCode() != null) ? dto.getContractCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getConstructionCode() != null) ? dto.getConstructionCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getVttbName() != null) ? dto.getVttbName() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue((dto.getVttbCode() != null) ? dto.getVttbCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue((dto.getSysGroupName() != null) ? dto.getSysGroupName() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue((dto.getNgayQuaHan() != null) ? dto.getNgayQuaHan() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue((dto.getTransMan() != null) ? dto.getTransMan() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue((dto.getEmail() != null) ? dto.getEmail() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue((dto.getPhoneNumber() != null) ? dto.getPhoneNumber() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue((dto.getStatus() != null) ? dto.getStatus() : "");
				cell.setCellStyle(style);
				
				
			}
			
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "BM_BC_VTTB_Qua_Han_45_Ngay.xlsx");
		return path;
	}
	
	//tatph-end-6/2/2019
	
	//Huypq-07072020-start
	public String exportRpGeneralPaymentCtv(ReportDTO obj, HttpServletRequest request) throws Exception{
		obj.setPage(null);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Export_TongHop_ThanhToan_CTV.xlsx"));
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
		OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "Export_TongHop_ThanhToan_CTV.xlsx");
		
		List<ReportDTO> data = new ArrayList<>();
		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
                Constant.AdResourceKey.DATA, request);
        List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
        if(groupIdList!=null && groupIdList.size()>0) {
        	data = rpBTSDAO.doSearchRpGeneralPaymentCtv(obj, groupIdList);
        }
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		XSSFSheet sheet = workbook.getSheetAt(0);
		
		XSSFCellStyle style = ExcelUtils.styleText(sheet);
		XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
		styleNumber.setAlignment(HorizontalAlignment.RIGHT);
		
		XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
		XSSFCreationHelper createHelper = workbook.getCreationHelper();
		styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
		
		XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
		styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
		
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 12);
		font.setFontName("Times New Roman");
		font.setColor(IndexedColors.BLACK.getIndex());
		font.setBold(true);
		font.setItalic(false);
		
		XSSFCellStyle styleTitle = ExcelUtils.styleText(sheet);
		styleTitle.setAlignment(HorizontalAlignment.CENTER);
		styleTitle.setFont(font);
		styleTitle.setWrapText(true);
		styleTitle.setBorderBottom(BorderStyle.NONE);
		styleTitle.setBorderTop(BorderStyle.NONE);
		styleTitle.setBorderLeft(BorderStyle.NONE);
		styleTitle.setBorderRight(BorderStyle.NONE);
		
		Row rowTitle = sheet.createRow(4);
		Cell cellTitle = rowTitle.createCell(0, CellType.STRING);
		if(!StringUtils.isNullOrEmpty(obj.getTypeTime()) && obj.getTypeTime().equals("1")) {
			cellTitle.setCellValue("TỔNG HỢP THANH TOÁN HOA HỒNG CTV XDDD THÁNG " + obj.getThang());
		} else {
			cellTitle.setCellValue("TỔNG HỢP THANH TOÁN HOA HỒNG CTV XDDD TỪ " + dateFormat.format(obj.getDateFrom()) + " ĐẾN " + dateFormat.format(obj.getDateTo()));
		}
		cellTitle.setCellStyle(styleTitle);
		
		if (data != null && !data.isEmpty()) {
			int i = 8;
			for (ReportDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 8));
				cell.setCellStyle(styleNumber);
				//
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getProvinceCode() != null) ? dto.getProvinceCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getEmployeeCode() != null) ? dto.getEmployeeCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getFullName() != null) ? dto.getFullName() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getPhoneNumber() != null) ? dto.getPhoneNumber() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getEmployeeCodeCTV() != null) ? dto.getEmployeeCodeCTV() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getFullNameCTV() != null) ? dto.getFullNameCTV() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue((dto.getCmtnd() != null) ? dto.getCmtnd() : "");
				cell.setCellStyle(style);
				//
//				cell = row.createCell(8, CellType.STRING);
//				cell.setCellValue("");
//				cell.setCellStyle(style);
				//
//				cell = row.createCell(9, CellType.STRING);
//				cell.setCellValue("");
//				cell.setCellStyle(style);
				//
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue((dto.getVtpay() != null) ? dto.getVtpay() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue((dto.getGiaTruocThue() != null) ? dto.getGiaTruocThue() : 0d);
				cell.setCellStyle(styleCurrency);
				//
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue((dto.getHoaHong() != null) ? dto.getHoaHong() : 0d);
				cell.setCellStyle(styleCurrency);
				
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue((dto.getThueTNCN() != null) ? dto.getThueTNCN() : 0d);
				cell.setCellStyle(styleCurrency);
				
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue((dto.getThucNhan() != null) ? dto.getThucNhan() : 0d);
				cell.setCellStyle(styleCurrency);
				
				cell = row.createCell(13, CellType.STRING);
				if(org.apache.commons.lang3.StringUtils.isNotBlank(dto.getStatus())) {
					cell.setCellValue(Constant.TANGENT_STATUS.get(dto.getStatus()));
				} else {
					cell.setCellValue("");
				}
				cell.setCellStyle(style);
				
				cell = row.createCell(14, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				
			}
			
			Row row = sheet.createRow(i);
			sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 8));
			Cell cell2 = row.createCell(0, CellType.STRING);
			cell2.setCellValue("Tổng cộng");
			cell2.setCellStyle(style);
			
			cell2 = row.createCell(1, CellType.STRING);
			cell2.setCellValue("");
			cell2.setCellStyle(style);
			
			cell2 = row.createCell(2, CellType.STRING);
			cell2.setCellValue("");
			cell2.setCellStyle(style);
			
			cell2 = row.createCell(3, CellType.STRING);
			cell2.setCellValue("");
			cell2.setCellStyle(style);
			
			cell2 = row.createCell(4, CellType.STRING);
			cell2.setCellValue("");
			cell2.setCellStyle(style);
			
			cell2 = row.createCell(5, CellType.STRING);
			cell2.setCellValue("");
			cell2.setCellStyle(style);
			
			cell2 = row.createCell(6, CellType.STRING);
			cell2.setCellValue("");
			cell2.setCellStyle(style);
			
			cell2 = row.createCell(7, CellType.STRING);
			cell2.setCellValue("");
			cell2.setCellStyle(style);
			
//			cell2 = row.createCell(8, CellType.STRING);
//			cell2.setCellValue("");
//			cell2.setCellStyle(style);
//			
//			cell2 = row.createCell(9, CellType.STRING);
//			cell2.setCellValue("");
//			cell2.setCellStyle(style);
			
			cell2 = row.createCell(8, CellType.STRING);
			cell2.setCellValue("");
			cell2.setCellStyle(style);
	
			cell2 = row.createCell(9, CellType.STRING);
			cell2.setCellValue(data.get(0).getSumGiaTruocThue()!=null ? data.get(0).getSumGiaTruocThue() : 0d);
			cell2.setCellStyle(styleCurrency);
			
			cell2 = row.createCell(10, CellType.STRING);
			cell2.setCellValue(data.get(0).getSumHoaHong()!=null ? data.get(0).getSumHoaHong() : 0d);
			cell2.setCellStyle(styleCurrency);
			
			cell2 = row.createCell(11, CellType.STRING);
			cell2.setCellValue(data.get(0).getSumThueTNCN()!=null ? data.get(0).getSumThueTNCN() : 0d);
			cell2.setCellStyle(styleCurrency);
			
			cell2 = row.createCell(12, CellType.STRING);
			cell2.setCellValue(data.get(0).getSumThucNhan()!=null ? data.get(0).getSumThucNhan() : 0d);
			cell2.setCellStyle(styleCurrency);
			
			cell2 = row.createCell(13, CellType.STRING);
			cell2.setCellValue("");
			cell2.setCellStyle(style);
			
			cell2 = row.createCell(14, CellType.STRING);
			cell2.setCellValue("");
			cell2.setCellStyle(style);
			
			i++;
			
			Row row2 = sheet.createRow(i);
			sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 14));
			Cell cell3 = row2.createCell(0, CellType.STRING);
			cell3.setCellValue("Bằng chữ: " + ChuyenTienRaChu.chuyenThanhChu2(data.get(0).getSoTienChuyenChu()));
			cell3.setCellStyle(style);
			
			cell3 = row2.createCell(1, CellType.STRING);
			cell3.setCellValue("");
			cell3.setCellStyle(style);
			
			cell3 = row2.createCell(2, CellType.STRING);
			cell3.setCellValue("");
			cell3.setCellStyle(style);
			
			cell3 = row2.createCell(3, CellType.STRING);
			cell3.setCellValue("");
			cell3.setCellStyle(style);
			
			cell3 = row2.createCell(4, CellType.STRING);
			cell3.setCellValue("");
			cell3.setCellStyle(style);
			
			cell3 = row2.createCell(5, CellType.STRING);
			cell3.setCellValue("");
			cell3.setCellStyle(style);
			
			cell3 = row2.createCell(6, CellType.STRING);
			cell3.setCellValue("");
			cell3.setCellStyle(style);
			
			cell3 = row2.createCell(7, CellType.STRING);
			cell3.setCellValue("");
			cell3.setCellStyle(style);
			
			cell3 = row2.createCell(8, CellType.STRING);
			cell3.setCellValue("");
			cell3.setCellStyle(style);
	
			cell3 = row2.createCell(9, CellType.STRING);
			cell3.setCellValue(data.get(0).getSumGiaTruocThue()!=null ? data.get(0).getSumGiaTruocThue() : 0d);
			cell3.setCellStyle(styleCurrency);
			
			cell3 = row2.createCell(10, CellType.STRING);
			cell3.setCellValue(data.get(0).getSumHoaHong()!=null ? data.get(0).getSumHoaHong() : 0d);
			cell3.setCellStyle(styleCurrency);
			
			cell3 = row2.createCell(11, CellType.STRING);
			cell3.setCellValue(data.get(0).getSumThueTNCN()!=null ? data.get(0).getSumThueTNCN() : 0d);
			cell3.setCellStyle(styleCurrency);
			
			cell3 = row2.createCell(12, CellType.STRING);
			cell3.setCellValue(data.get(0).getSumThucNhan()!=null ? data.get(0).getSumThucNhan() : 0d);
			cell3.setCellStyle(styleCurrency);
			
			cell3 = row2.createCell(13, CellType.STRING);
			cell3.setCellValue("");
			cell3.setCellStyle(style);
			
			cell3 = row2.createCell(14, CellType.STRING);
			cell3.setCellValue("");
			cell3.setCellStyle(style);
			
			i++;
			i++;
			
			Row row3 = sheet.createRow(i);
			sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 3));
			Cell cell4 = row3.createCell(0, CellType.STRING);
			cell4.setCellValue("TRUNG TÂM HẠ TẦNG");
			cell4.setCellStyle(styleTitle);	
			
			sheet.addMergedRegion(new CellRangeAddress(i, i, 4, 6));
			cell4 = row3.createCell(4, CellType.STRING);
			cell4.setCellValue("P. TỔ CHỨC LAO ĐỘNG");
			cell4.setCellStyle(styleTitle);
			
			sheet.addMergedRegion(new CellRangeAddress(i, i, 7, 9));
			cell4 = row3.createCell(7, CellType.STRING);
			cell4.setCellValue("P. TÀI CHÍNH KẾ TOÁN");
			cell4.setCellStyle(styleTitle);
			
			sheet.addMergedRegion(new CellRangeAddress(i, i, 10, 13));
			cell4 = row3.createCell(10, CellType.STRING);
			cell4.setCellValue("PHÓ TỔNG GIẢM ĐỐC");
			cell4.setCellStyle(styleTitle);
			
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "Export_TongHop_ThanhToan_CTV.xlsx");
		return path;
	}
	
	public String exportRpDetailContractCtv(ReportDTO obj, HttpServletRequest request) throws Exception{
		obj.setPage(null);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = null;
		if(!StringUtils.isNullOrEmpty(obj.getTypeTime()) && obj.getTypeTime().equals("1")) {
			file = new BufferedInputStream(new FileInputStream(filePath + "Export_ChiTiet_HopDong_CTV_Thang.xlsx"));
		} else {
			file = new BufferedInputStream(new FileInputStream(filePath + "Export_ChiTiet_HopDong_CTV.xlsx"));
		}
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
		OutputStream outFile = null;
		if(!StringUtils.isNullOrEmpty(obj.getTypeTime()) && obj.getTypeTime().equals("1")) {
			outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "Export_ChiTiet_HopDong_CTV_Thang.xlsx");
		} else {
			outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "Export_ChiTiet_HopDong_CTV.xlsx");
		}
		
		List<ReportDTO> data = new ArrayList<>();
		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
                Constant.AdResourceKey.DATA, request);
        List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
        if(groupIdList!=null && groupIdList.size()>0) {
        	data = rpBTSDAO.doSearchRpDetailContractCtv(obj, groupIdList);
        }
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		XSSFSheet sheet = workbook.getSheetAt(0);
		
		XSSFCellStyle style = ExcelUtils.styleText(sheet);
		XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
		styleNumber.setAlignment(HorizontalAlignment.RIGHT);
		
		XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
		XSSFCreationHelper createHelper = workbook.getCreationHelper();
		styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
		
		XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
		styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
		
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 12);
		font.setFontName("Times New Roman");
		font.setColor(IndexedColors.BLACK.getIndex());
		font.setBold(true);
		font.setItalic(false);
		
		XSSFCellStyle styleTitle = ExcelUtils.styleText(sheet);
		styleTitle.setAlignment(HorizontalAlignment.CENTER);
		styleTitle.setFont(font);
		styleTitle.setWrapText(true);
		styleTitle.setBorderBottom(BorderStyle.NONE);
		styleTitle.setBorderTop(BorderStyle.NONE);
		styleTitle.setBorderLeft(BorderStyle.NONE);
		styleTitle.setBorderRight(BorderStyle.NONE);
		
		Row rowTitle = sheet.createRow(0);
		Cell cellTitle = rowTitle.createCell(0, CellType.STRING);
		if(!StringUtils.isNullOrEmpty(obj.getTypeTime()) && obj.getTypeTime().equals("1")) {
			cellTitle.setCellValue("DANH SÁCH HỢP ĐỒNG THANH TOÁN HOA HỒNG CTV THÁNG " + obj.getThang());
		} else {
			cellTitle.setCellValue("DANH SÁCH HỢP ĐỒNG THANH TOÁN HOA HỒNG CTV TỪ " + dateFormat.format(obj.getDateFrom()) + " ĐẾN " + dateFormat.format(obj.getDateTo()));
		}
		cellTitle.setCellStyle(styleTitle);
		
		if (data != null && !data.isEmpty()) {
			int i = 4;
			for (ReportDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 4));
				cell.setCellStyle(styleNumber);
				//
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getProvinceCode() != null) ? dto.getProvinceCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getDistrictName() != null) ? dto.getDistrictName() : "");
				cell.setCellStyle(style);
				
				
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getEmployeeCode() != null) ? dto.getEmployeeCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getFullName() != null) ? dto.getFullName() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getPhoneNumber() != null) ? dto.getPhoneNumber() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getEmployeeCodeCTV() != null) ? dto.getEmployeeCodeCTV() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue((dto.getFullNameCTV() != null) ? dto.getFullNameCTV() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue((dto.getVtpay() != null) ? dto.getVtpay() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue((dto.getContractCode() != null) ? dto.getContractCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue((dto.getGiaTruocThue() != null) ? dto.getGiaTruocThue() : 0d);
				cell.setCellStyle(styleCurrency);
				//
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue((dto.getHoaHong() != null) ? dto.getHoaHong() : 0d);
				cell.setCellStyle(styleCurrency);
				
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue((dto.getThueTNCN() != null) ? dto.getThueTNCN() : 0d);
				cell.setCellStyle(styleCurrency);
				
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue((dto.getThucNhan() != null) ? dto.getThucNhan() : 0d);
				cell.setCellStyle(styleCurrency);
				
				if(!StringUtils.isNullOrEmpty(obj.getTypeTime()) && obj.getTypeTime().equals("1")) {
					cell = row.createCell(14, CellType.STRING);
					if(org.apache.commons.lang3.StringUtils.isNotBlank(dto.getStatus())) {
						cell.setCellValue(Constant.TANGENT_STATUS.get(dto.getStatus()));
					} else {
						cell.setCellValue("");
					}
					cell.setCellStyle(style);
				}
			}
			
			Row row = sheet.createRow(i);
			sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 6));
			Cell cell2 = row.createCell(0, CellType.STRING);
			cell2.setCellValue("Tổng cộng");
			cell2.setCellStyle(style);
			
			cell2 = row.createCell(1, CellType.STRING);
			cell2.setCellValue("");
			cell2.setCellStyle(style);
			
			cell2 = row.createCell(2, CellType.STRING);
			cell2.setCellValue("");
			cell2.setCellStyle(style);
			
			cell2 = row.createCell(3, CellType.STRING);
			cell2.setCellValue("");
			cell2.setCellStyle(style);
			
			cell2 = row.createCell(4, CellType.STRING);
			cell2.setCellValue("");
			cell2.setCellStyle(style);
			
			cell2 = row.createCell(5, CellType.STRING);
			cell2.setCellValue("");
			cell2.setCellStyle(style);
			
			cell2 = row.createCell(6, CellType.STRING);
			cell2.setCellValue("");
			cell2.setCellStyle(style);
			
			cell2 = row.createCell(7, CellType.STRING);
			cell2.setCellValue("");
			cell2.setCellStyle(style);
			
			cell2 = row.createCell(8, CellType.STRING);
			cell2.setCellValue("");
			cell2.setCellStyle(style);
			
			cell2 = row.createCell(9, CellType.STRING);
			cell2.setCellValue(data.get(0).getSumGiaTruocThue()!=null ? data.get(0).getSumGiaTruocThue() : 0d);
			cell2.setCellStyle(styleCurrency);
			
			cell2 = row.createCell(10, CellType.STRING);
			cell2.setCellValue(data.get(0).getSumHoaHong()!=null ? data.get(0).getSumHoaHong() : 0d);
			cell2.setCellStyle(styleCurrency);
			
			cell2 = row.createCell(11, CellType.STRING);
			cell2.setCellValue(data.get(0).getSumThueTNCN()!=null ? data.get(0).getSumThueTNCN() : 0d);
			cell2.setCellStyle(styleCurrency);
			
			cell2 = row.createCell(12, CellType.STRING);
			cell2.setCellValue(data.get(0).getSumThucNhan()!=null ? data.get(0).getSumThucNhan() : 0d);
			cell2.setCellStyle(styleCurrency);
			
			/*cell2 = row.createCell(13, CellType.STRING);
			cell2.setCellValue("");
			cell2.setCellStyle(style);*/
			
			i++;
			
			Row row2 = sheet.createRow(i);
			sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 13));
			Cell cell3 = row2.createCell(0, CellType.STRING);
			cell3.setCellValue("Bằng chữ: " + ChuyenTienRaChu.chuyenThanhChu2(data.get(0).getSoTienChuyenChu()));
			cell3.setCellStyle(style);
			
			cell3 = row2.createCell(1, CellType.STRING);
			cell3.setCellValue("");
			cell3.setCellStyle(style);
			
			cell3 = row2.createCell(2, CellType.STRING);
			cell3.setCellValue("");
			cell3.setCellStyle(style);
			
			cell3 = row2.createCell(3, CellType.STRING);
			cell3.setCellValue("");
			cell3.setCellStyle(style);
			
			cell3 = row2.createCell(4, CellType.STRING);
			cell3.setCellValue("");
			cell3.setCellStyle(style);
			
			cell3 = row2.createCell(5, CellType.STRING);
			cell3.setCellValue("");
			cell3.setCellStyle(style);
			
			cell3 = row2.createCell(6, CellType.STRING);
			cell3.setCellValue("");
			cell3.setCellStyle(style);
			
			cell3 = row2.createCell(7, CellType.STRING);
			cell3.setCellValue("");
			cell3.setCellStyle(style);
			
			cell3 = row2.createCell(8, CellType.STRING);
			cell3.setCellValue("");
			cell3.setCellStyle(style);
			
			cell3 = row2.createCell(9, CellType.STRING);
			cell3.setCellValue(data.get(0).getSumGiaTruocThue()!=null ? data.get(0).getSumGiaTruocThue() : 0d);
			cell3.setCellStyle(styleCurrency);
			
			cell3 = row2.createCell(10, CellType.STRING);
			cell3.setCellValue(data.get(0).getSumHoaHong()!=null ? data.get(0).getSumHoaHong() : 0d);
			cell3.setCellStyle(styleCurrency);
			
			cell3 = row2.createCell(11, CellType.STRING);
			cell3.setCellValue(data.get(0).getSumThueTNCN()!=null ? data.get(0).getSumThueTNCN() : 0d);
			cell3.setCellStyle(styleCurrency);
			
			cell3 = row2.createCell(12, CellType.STRING);
			cell3.setCellValue(data.get(0).getSumThucNhan()!=null ? data.get(0).getSumThucNhan() : 0d);
			cell3.setCellStyle(styleCurrency);
			
			cell3 = row2.createCell(13, CellType.STRING);
			cell3.setCellValue("");
			cell3.setCellStyle(style);
			
			i++;
			i++;
			
			Row row3 = sheet.createRow(i);
			sheet.addMergedRegion(new CellRangeAddress(i, i, 8, 12));
			Cell cell4 = row3.createCell(8, CellType.STRING);
			cell4.setCellValue("TRUNG TÂM HẠ TẦNG");
			cell4.setCellStyle(styleTitle);	
			
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();
		String path = null;
		if(!StringUtils.isNullOrEmpty(obj.getTypeTime()) && obj.getTypeTime().equals("1")) {
			path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "Export_ChiTiet_HopDong_CTV_Thang.xlsx");
		} else {
			path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "Export_ChiTiet_HopDong_CTV.xlsx");
		}
		return path;
	}
	
	public String exportRpTranfersCtv(ReportDTO obj, HttpServletRequest request) throws Exception{
		obj.setPage(null);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Export_ChuyenTien_CTV.xlsx"));
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
		OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "Export_ChuyenTien_CTV.xlsx");
		
		List<ReportDTO> data = new ArrayList<>();
		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
                Constant.AdResourceKey.DATA, request);
        List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
        if(groupIdList!=null && groupIdList.size()>0) {
        	data = rpBTSDAO.doSearchRpTranfersCtv(obj, groupIdList);
        }
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		XSSFSheet sheet = workbook.getSheetAt(0);
		
		XSSFCellStyle style = ExcelUtils.styleText(sheet);
		XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
		styleNumber.setAlignment(HorizontalAlignment.RIGHT);
		
		XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
		XSSFCreationHelper createHelper = workbook.getCreationHelper();
		styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
		
		XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
		styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
		
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 12);
		font.setFontName("Times New Roman");
		font.setColor(IndexedColors.BLACK.getIndex());
		font.setBold(true);
		font.setItalic(false);
		
		XSSFCellStyle styleTitle = ExcelUtils.styleText(sheet);
		styleTitle.setAlignment(HorizontalAlignment.CENTER);
		styleTitle.setFont(font);
		styleTitle.setWrapText(true);
		styleTitle.setBorderBottom(BorderStyle.NONE);
		styleTitle.setBorderTop(BorderStyle.NONE);
		styleTitle.setBorderLeft(BorderStyle.NONE);
		styleTitle.setBorderRight(BorderStyle.NONE);
		
		Row rowTitle = sheet.createRow(0);
		Cell cellTitle = rowTitle.createCell(0, CellType.STRING);
		if(!StringUtils.isNullOrEmpty(obj.getTypeTime()) && obj.getTypeTime().equals("1")) {
			cellTitle.setCellValue("ĐỀ NGHỊ THANH TOÁN HOA HỒNG CTV XDDD THÁNG " + obj.getThang());
		} else {
			cellTitle.setCellValue("ĐỀ NGHỊ THANH TOÁN HOA HỒNG CTV XDDD TỪ " + dateFormat.format(obj.getDateFrom()) + " ĐẾN " + dateFormat.format(obj.getDateTo()));
		}
		cellTitle.setCellStyle(styleTitle);
		
		if (data != null && !data.isEmpty()) {
			int i = 3;
			for (ReportDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 3));
				cell.setCellStyle(styleNumber);
				//
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getProvinceCode() != null) ? dto.getProvinceCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getEmployeeCodeCTV() != null) ? dto.getEmployeeCodeCTV() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getFullNameCTV() != null) ? dto.getFullNameCTV() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getVtpay() != null) ? dto.getVtpay() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getSoTien() != null) ? dto.getSoTien() : 0d);
				cell.setCellStyle(styleCurrency);
				
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getTinNhan() != null) ? dto.getTinNhan() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				
			}
			
			Row row = sheet.createRow(i);
			sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 4));
			Cell cell2 = row.createCell(0, CellType.STRING);
			cell2.setCellValue("Tổng cộng");
			cell2.setCellStyle(style);
			
			cell2 = row.createCell(1, CellType.STRING);
			cell2.setCellValue("");
			cell2.setCellStyle(style);
			
			cell2 = row.createCell(2, CellType.STRING);
			cell2.setCellValue("");
			cell2.setCellStyle(style);
			
			cell2 = row.createCell(3, CellType.STRING);
			cell2.setCellValue("");
			cell2.setCellStyle(style);
			
			cell2 = row.createCell(4, CellType.STRING);
			cell2.setCellValue("");
			cell2.setCellStyle(style);
			
			cell2 = row.createCell(5, CellType.STRING);
			cell2.setCellValue(data.get(0).getSumGiaTruocThue()!=null ? data.get(0).getSumGiaTruocThue() : 0d);
			cell2.setCellStyle(styleCurrency);
			
			cell2 = row.createCell(6, CellType.STRING);
			cell2.setCellValue("");
			cell2.setCellStyle(style);
			
			cell2 = row.createCell(7, CellType.STRING);
			cell2.setCellValue("");
			cell2.setCellStyle(style);
			
			i++;
			
			Row row2 = sheet.createRow(i);
			sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 7));
			Cell cell3 = row2.createCell(0, CellType.STRING);
			cell3.setCellValue("Bằng chữ: " + ChuyenTienRaChu.chuyenThanhChu2(data.get(0).getSoTienChuyenChu()));
			cell3.setCellStyle(style);
			
			cell3 = row2.createCell(1, CellType.STRING);
			cell3.setCellValue("");
			cell3.setCellStyle(style);
			
			cell3 = row2.createCell(2, CellType.STRING);
			cell3.setCellValue("");
			cell3.setCellStyle(style);
			
			cell3 = row2.createCell(3, CellType.STRING);
			cell3.setCellValue("");
			cell3.setCellStyle(style);
			
			cell3 = row2.createCell(4, CellType.STRING);
			cell3.setCellValue("");
			cell3.setCellStyle(style);
			
			cell3 = row2.createCell(5, CellType.STRING);
			cell3.setCellValue("");
			cell3.setCellStyle(style);
			
			cell3 = row2.createCell(6, CellType.STRING);
			cell3.setCellValue("");
			cell3.setCellStyle(style);
			
			cell3 = row2.createCell(7, CellType.STRING);
			cell3.setCellValue("");
			cell3.setCellStyle(style);
			
			i++;
			i++;
			
			Row row3 = sheet.createRow(i);
			sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 3));
			Cell cell4 = row3.createCell(0, CellType.STRING);
			cell4.setCellValue("TRUNG TÂM HẠ TẦNG");
			cell4.setCellStyle(styleTitle);
			
			Row row4 = sheet.getRow(i);
			sheet.addMergedRegion(new CellRangeAddress(i, i, 4, 8));
			Cell cell5 = row4.createCell(4, CellType.STRING);
			cell5.setCellValue("PHÒNG TÀI CHÍNH KẾ TOÁN");
			cell5.setCellStyle(styleTitle);
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "Export_ChuyenTien_CTV.xlsx");
		return path;
	}
	
	public DataListDTO doSearchRpGeneralPaymentCtv(ReportDTO obj, HttpServletRequest request) {
		List<ReportDTO> ls = new ArrayList<>();
		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
                Constant.AdResourceKey.DATA, request);
        List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
        if(groupIdList!=null && groupIdList.size()>0) {
        	ls = rpBTSDAO.doSearchRpGeneralPaymentCtv(obj, groupIdList);
        }
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }
	
	public DataListDTO doSearchRpDetailContractCtv(ReportDTO obj, HttpServletRequest request) {
		List<ReportDTO> ls = new ArrayList<>();
		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
                Constant.AdResourceKey.DATA, request);
        List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
        if(groupIdList!=null && groupIdList.size()>0) {
        	ls = rpBTSDAO.doSearchRpDetailContractCtv(obj, groupIdList);
        }
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }
	
	public DataListDTO doSearchRpTranfersCtv(ReportDTO obj, HttpServletRequest request) {
		List<ReportDTO> ls = new ArrayList<>();
		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
                Constant.AdResourceKey.DATA, request);
        List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
        if(groupIdList!=null && groupIdList.size()>0) {
        	ls = rpBTSDAO.doSearchRpTranfersCtv(obj, groupIdList);
        }
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }
	
	//Huy-end
	
	//Huypq-20072020-start
	public DataListDTO doSearchEffective(ReportEffectiveDTO obj) {
        List<ReportEffectiveDTO> ls = rpBTSDAO.doSearchEffective(obj);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }
	//huy-end

	//huypq-21062021-start
	@Override
	public DataListDTO doSearchReportMassSearchConstr(ReportDTO obj) {
		List<ReportDTO> ls = rpBTSDAO.doSearchReportMassSearchConstr(obj);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
	}
	
	@Override
	public DataListDTO doSearchReportResultDeployBts(ReportDTO obj) {
		List<ReportDTO> ls = rpBTSDAO.doSearchReportResultDeployBts(obj);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
	}
	
	@Override
	public String exportRpMassSearchConstr(ReportDTO obj, HttpServletRequest request) throws Exception{
		obj.setPage(null);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "BaoCao_KhoiLuong_ThiCong.xlsx"));
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
		OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "BaoCao_KhoiLuong_ThiCong.xlsx");
		
		List<ReportDTO> data = rpBTSDAO.doSearchReportMassSearchConstr(obj);
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		XSSFSheet sheet = workbook.getSheetAt(0);
		
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 12);
		font.setFontName("Times New Roman");
		font.setColor(IndexedColors.BLACK.getIndex());
		font.setBold(true);
		font.setItalic(false);
		
		XSSFCellStyle styleTitle = ExcelUtils.styleText(sheet);
		styleTitle.setAlignment(HorizontalAlignment.CENTER);
		styleTitle.setFont(font);
		styleTitle.setWrapText(true);
		
		XSSFCellStyle style = ExcelUtils.styleText(sheet);
		XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
		styleNumber.setAlignment(HorizontalAlignment.RIGHT);
		styleNumber.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
		
		XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
		XSSFCreationHelper createHelper = workbook.getCreationHelper();
		styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
		
		XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
//		styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0%"));

		if (data != null && !data.isEmpty()) {
			int i = 2;
			for (ReportDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 2));
				cell.setCellStyle(styleNumber);
				//
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getMonth() != null) ? dto.getMonth() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getYear() != null) ? dto.getYear() : "");
				cell.setCellStyle(style);
				//
//				cell = row.createCell(3, CellType.STRING);
//				cell.setCellValue((dto.getSysGroupCode() != null) ? dto.getSysGroupCode() : "");
//				cell.setCellStyle(style);
				//
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getProvinceCode() != null) ? dto.getProvinceCode() : "");
				cell.setCellStyle(style);
				//
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getPlanTMB() != null) ? dto.getPlanTMB() : 0d);
				cell.setCellStyle(styleNumber);
				//
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getPerformTMB() != null) ? dto.getPerformTMB() : 0d);
				cell.setCellStyle(styleNumber);
				//
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getRatioTMB() != null) ? dto.getRatioTMB() + "%" : "0%");
				cell.setCellStyle(styleCurrency);
				
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue((dto.getPlanKC() != null) ? dto.getPlanKC() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue((dto.getPerformKC() != null) ? dto.getPerformKC() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue((dto.getRatioKC() != null) ? dto.getRatioKC() + "%" : "0%");
				cell.setCellStyle(styleCurrency);
				//
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue((dto.getPlanDBHT() != null) ? dto.getPlanDBHT() : 0d);
				cell.setCellStyle(styleNumber);
				//
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue((dto.getPerformDBHT() != null) ? dto.getPerformDBHT() : 0d);
				cell.setCellStyle(styleNumber);
				//
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue((dto.getRatioDBHT() != null) ? dto.getRatioDBHT() + "%" : "0%");
				cell.setCellStyle(styleCurrency);
				//
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue((dto.getPlanPS() != null) ? dto.getPlanPS() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(14, CellType.STRING);
				cell.setCellValue((dto.getPerformPS() != null) ? dto.getPerformPS() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(15, CellType.STRING);
				cell.setCellValue((dto.getRatioPS() != null) ? dto.getRatioPS() + "%" : "0%");
				cell.setCellStyle(styleCurrency);
				
				cell = row.createCell(16, CellType.STRING);
				cell.setCellValue((dto.getPlanHSHC() != null) ? dto.getPlanHSHC() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(17, CellType.STRING);
				cell.setCellValue((dto.getPerformHSHC() != null) ? dto.getPerformHSHC() : 0d);
				cell.setCellStyle(styleNumber);
				
				cell = row.createCell(18, CellType.STRING);
				cell.setCellValue((dto.getRatioHSHC() != null) ? dto.getRatioHSHC() + "%" : "0%");
				cell.setCellStyle(styleCurrency);
			}
			
			Row row = sheet.createRow(i);
			sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 3));
			Cell cell2 = row.createCell(0, CellType.STRING);
			cell2.setCellValue("Tổng cộng");
			cell2.setCellStyle(styleTitle);
			
			cell2 = row.createCell(1, CellType.STRING);
			cell2.setCellValue("");
			cell2.setCellStyle(style);
			
			cell2 = row.createCell(2, CellType.STRING);
			cell2.setCellValue("");
			cell2.setCellStyle(style);
			
			cell2 = row.createCell(3, CellType.STRING);
			cell2.setCellValue("");
			cell2.setCellStyle(style);
			
			cell2 = row.createCell(4, CellType.STRING);
			cell2.setCellValue(data.get(0).getSumPlanTMB()!=null ? data.get(0).getSumPlanTMB() : 0d);
			cell2.setCellStyle(styleTitle);
			
			cell2 = row.createCell(5, CellType.STRING);
			cell2.setCellValue(data.get(0).getSumPerformTMB()!=null ? data.get(0).getSumPerformTMB() : 0d);
			cell2.setCellStyle(styleTitle);
			
			cell2 = row.createCell(6, CellType.STRING);
			cell2.setCellValue(data.get(0).getSumRatioTMB()!=null ? data.get(0).getSumRatioTMB() : 0d);
			cell2.setCellStyle(styleTitle);
			
			cell2 = row.createCell(7, CellType.STRING);
			cell2.setCellValue(data.get(0).getSumPlanKC()!=null ? data.get(0).getSumPlanKC() : 0d);
			cell2.setCellStyle(styleTitle);
			
			cell2 = row.createCell(8, CellType.STRING);
			cell2.setCellValue(data.get(0).getSumPerformKC()!=null ? data.get(0).getSumPerformKC() : 0d);
			cell2.setCellStyle(styleTitle);
			
			cell2 = row.createCell(9, CellType.STRING);
			cell2.setCellValue(data.get(0).getSumRatioKC()!=null ? data.get(0).getSumRatioKC() : 0d);
			cell2.setCellStyle(styleTitle);
			
			cell2 = row.createCell(10, CellType.STRING);
			cell2.setCellValue(data.get(0).getSumPlanDBHT()!=null ? data.get(0).getSumPlanDBHT() : 0d);
			cell2.setCellStyle(styleTitle);
			
			cell2 = row.createCell(11, CellType.STRING);
			cell2.setCellValue(data.get(0).getSumPerformDBHT()!=null ? data.get(0).getSumPerformDBHT() : 0d);
			cell2.setCellStyle(styleTitle);
			
			cell2 = row.createCell(12, CellType.STRING);
			cell2.setCellValue(data.get(0).getSumRatioDBHT()!=null ? data.get(0).getSumRatioDBHT() : 0d);
			cell2.setCellStyle(styleTitle);
			
			cell2 = row.createCell(13, CellType.STRING);
			cell2.setCellValue(data.get(0).getSumPlanPS()!=null ? data.get(0).getSumPlanPS() : 0d);
			cell2.setCellStyle(styleTitle);
			
			cell2 = row.createCell(14, CellType.STRING);
			cell2.setCellValue(data.get(0).getSumPerformPS()!=null ? data.get(0).getSumPerformPS() : 0d);
			cell2.setCellStyle(styleTitle);
			
			cell2 = row.createCell(15, CellType.STRING);
			cell2.setCellValue(data.get(0).getSumRatioPS()!=null ? data.get(0).getSumRatioPS() : 0d);
			cell2.setCellStyle(styleTitle);
			
			cell2 = row.createCell(16, CellType.STRING);
			cell2.setCellValue(data.get(0).getSumPlanHSHC()!=null ? data.get(0).getSumPlanHSHC() : 0d);
			cell2.setCellStyle(styleTitle);
			
			cell2 = row.createCell(17, CellType.STRING);
			cell2.setCellValue(data.get(0).getSumPerformHSHC()!=null ? data.get(0).getSumPerformHSHC() : 0d);
			cell2.setCellStyle(styleTitle);
			
			cell2 = row.createCell(18, CellType.STRING);
			cell2.setCellValue(data.get(0).getSumRatioHSHC()!=null ? data.get(0).getSumRatioHSHC() : 0d);
			cell2.setCellStyle(styleTitle);
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "BaoCao_KhoiLuong_ThiCong.xlsx");
		return path;
	}
	//Huy-end
	
	// Huypq-08072021-start
	@Override
	public DataListDTO doSearchReportAcceptHSHC(ReportDTO obj) {
		List<ReportDTO> ls = rpBTSDAO.doSearchReportAcceptHSHC(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
	//Huy-end
//	taotq start 27092021
	 public DataListDTO doSearchReportKHBTS(RpKHBTSDTO obj) {
	        List<RpKHBTSDTO> ls = rpBTSDAO.doSearchReportKHBTS(obj);
	        DataListDTO data = new DataListDTO();
	        data.setData(ls);
	        data.setTotal(obj.getTotalRecord());
	        data.setSize(obj.getPageSize());
	        data.setStart(1);
	        return data;
	    }
	 public DataListDTO doSearchStation(RpKHBTSDTO obj) {
	        List<RpKHBTSDTO> ls = rpBTSDAO.doSearchStation(obj);
	        DataListDTO data = new DataListDTO();
	        data.setData(ls);
	        data.setTotal(obj.getTotalRecord());
	        data.setSize(obj.getPageSize());
	        data.setStart(1);
	        return data;
	    }
	 
	 public DataListDTO doSearchWO(RpKHBTSDTO obj) {
	        List<RpKHBTSDTO> ls = rpBTSDAO.doSearchWO(obj);
	        DataListDTO data = new DataListDTO();
	        data.setData(ls);
	        data.setTotal(obj.getTotalRecord());
	        data.setSize(obj.getPageSize());
	        data.setStart(1);
	        return data;
	    }
	 
	 public String exportStation(RpKHBTSDTO obj, HttpServletRequest request) throws Exception {
			obj.setPage(null);
			obj.setPageSize(null);
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			String filePath = classloader.getResource("../" + "doc-template").getPath();
			
			if(obj.getTypes() == 1) {
				InputStream file = new BufferedInputStream(new FileInputStream(filePath + "EXPORT_STATION_TMB.xlsx"));
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
				OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "EXPORT_STATION_TMB.xlsx");
				List<RpKHBTSDTO> data = rpBTSDAO.doSearchStation(obj);

				XSSFSheet sheet = workbook.getSheetAt(0);
				if (data != null && !data.isEmpty()) {
					XSSFCellStyle style = ExcelUtils.styleText(sheet);
					XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
					XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
					XSSFCreationHelper createHelper = workbook.getCreationHelper();
					styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
					styleNumber.setAlignment(HorizontalAlignment.RIGHT);
					styleNumber.setDataFormat(workbook.createDataFormat().getFormat("##0.00"));

					XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
					styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
					styleCurrency.setAlignment(HorizontalAlignment.RIGHT);

					XSSFFont font = workbook.createFont();
					font.setBold(false);
					font.setItalic(false);
					font.setFontName("Times New Roman");
					font.setFontHeightInPoints((short) 12);

					XSSFCellStyle styleYear = ExcelUtils.styleText(sheet);
					styleYear.setAlignment(HorizontalAlignment.CENTER);
					styleYear.setFont(font);

					Row rowYear = sheet.getRow(1);
					Cell cellYear = rowYear.createCell(0, CellType.STRING);
					cellYear.setCellValue("Thời gian: " + obj.getMonthYear());
					cellYear.setCellStyle(styleYear);
					
					int i = 5;
					for (RpKHBTSDTO dto : data) {
						Row row = sheet.createRow(i++);
						
						Cell cell = row.createCell(0, CellType.STRING);
						cell.setCellValue("" + (i - 5));
						cell.setCellStyle(styleNumber);
						
						cell = row.createCell(1, CellType.STRING);
						cell.setCellValue((dto.getProvinceCode() != null) ? dto.getProvinceCode() : "");
						cell.setCellStyle(style);
						
						cell = row.createCell(2, CellType.STRING);
						cell.setCellValue((dto.getVtnetCode() != null) ? dto.getVtnetCode() : "");
						cell.setCellStyle(style);
						
						cell = row.createCell(3, CellType.STRING);
						cell.setCellValue((dto.getVccCode() != null) ? dto.getVccCode() : "");
						cell.setCellStyle(style);
						
						cell = row.createCell(4, CellType.STRING);
						cell.setCellValue((dto.getPlanTMB() != null) ? Double.valueOf(dto.getPlanTMB()) : 0d);
						cell.setCellStyle(styleNumber);
						
						cell = row.createCell(5, CellType.STRING);
						cell.setCellValue((dto.getPerformTMB() != null) ? Double.valueOf(dto.getPerformTMB()) : 0d);
						cell.setCellStyle(styleNumber);

						cell = row.createCell(6, CellType.STRING);
						cell.setCellValue((dto.getNumberDateLimitKPI() != 0) ? Integer.valueOf(dto.getNumberDateLimitKPI()) : 0d);
						cell.setCellStyle(styleNumber);

						cell = row.createCell(7, CellType.STRING);
						cell.setCellValue((dto.getNumberFines() != null) ? Double.valueOf(dto.getNumberFines()) : 0d);
						cell.setCellStyle(styleNumber);
					}
				}

				workbook.write(outFile);
				workbook.close();
				outFile.close();

				String path = UEncrypt
						.encryptFileUploadPath(uploadPathReturn + File.separator + "EXPORT_STATION_TMB.xlsx");
				return path;
			}if(obj.getTypes() == 2) {
				InputStream file = new BufferedInputStream(new FileInputStream(filePath + "EXPORT_STATION_TKDT.xlsx"));
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
				OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "EXPORT_STATION_TKDT.xlsx");
				List<RpKHBTSDTO> data = rpBTSDAO.doSearchStation(obj);

				XSSFSheet sheet = workbook.getSheetAt(0);
				if (data != null && !data.isEmpty()) {
					XSSFCellStyle style = ExcelUtils.styleText(sheet);
					XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
					XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
					XSSFCreationHelper createHelper = workbook.getCreationHelper();
					styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
					styleNumber.setAlignment(HorizontalAlignment.RIGHT);
					styleNumber.setDataFormat(workbook.createDataFormat().getFormat("##0.00"));

					XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
					styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
					styleCurrency.setAlignment(HorizontalAlignment.RIGHT);

					XSSFFont font = workbook.createFont();
					font.setBold(false);
					font.setItalic(false);
					font.setFontName("Times New Roman");
					font.setFontHeightInPoints((short) 12);

					XSSFCellStyle styleYear = ExcelUtils.styleText(sheet);
					styleYear.setAlignment(HorizontalAlignment.CENTER);
					styleYear.setFont(font);

					Row rowYear = sheet.getRow(1);
					Cell cellYear = rowYear.createCell(0, CellType.STRING);
					cellYear.setCellValue("Thời gian: " + obj.getMonthYear());
					cellYear.setCellStyle(styleYear);
					
					int i = 5;
					for (RpKHBTSDTO dto : data) {
						Row row = sheet.createRow(i++);
						
						Cell cell = row.createCell(0, CellType.STRING);
						cell.setCellValue("" + (i - 5));
						cell.setCellStyle(styleNumber);
						
						cell = row.createCell(1, CellType.STRING);
						cell.setCellValue((dto.getProvinceCode() != null) ? dto.getProvinceCode() : "");
						cell.setCellStyle(style);
						
						cell = row.createCell(2, CellType.STRING);
						cell.setCellValue((dto.getVtnetCode() != null) ? dto.getVtnetCode() : "");
						cell.setCellStyle(style);
						
						cell = row.createCell(3, CellType.STRING);
						cell.setCellValue((dto.getVccCode() != null) ? dto.getVccCode() : "");
						cell.setCellStyle(style);
						
						cell = row.createCell(4, CellType.STRING);
						cell.setCellValue((dto.getPlanTKDT() != null) ? Double.valueOf(dto.getPlanTKDT()) : 0d);
						cell.setCellStyle(styleNumber);
						
						cell = row.createCell(5, CellType.STRING);
						cell.setCellValue((dto.getPerformTKDT() != null) ? Double.valueOf(dto.getPerformTKDT()) : 0d);
						cell.setCellStyle(styleNumber);

						cell = row.createCell(6, CellType.STRING);
						cell.setCellValue((dto.getNumberDateLimitKPI() != 0) ? Integer.valueOf(dto.getNumberDateLimitKPI()) : 0d);
						cell.setCellStyle(styleNumber);
					}
				}

				workbook.write(outFile);
				workbook.close();
				outFile.close();

				String path = UEncrypt
						.encryptFileUploadPath(uploadPathReturn + File.separator + "EXPORT_STATION_TKDT.xlsx");
				return path;
			}if(obj.getTypes() == 3) {
				InputStream file = new BufferedInputStream(new FileInputStream(filePath + "EXPORT_STATION_TTKDT.xlsx"));
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
				OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "EXPORT_STATION_TTKDT.xlsx");
				List<RpKHBTSDTO> data = rpBTSDAO.doSearchStation(obj);

				XSSFSheet sheet = workbook.getSheetAt(0);
				if (data != null && !data.isEmpty()) {
					XSSFCellStyle style = ExcelUtils.styleText(sheet);
					XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
					XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
					XSSFCreationHelper createHelper = workbook.getCreationHelper();
					styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
					styleNumber.setAlignment(HorizontalAlignment.RIGHT);
					styleNumber.setDataFormat(workbook.createDataFormat().getFormat("##0.00"));

					XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
					styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
					styleCurrency.setAlignment(HorizontalAlignment.RIGHT);

					XSSFFont font = workbook.createFont();
					font.setBold(false);
					font.setItalic(false);
					font.setFontName("Times New Roman");
					font.setFontHeightInPoints((short) 12);

					XSSFCellStyle styleYear = ExcelUtils.styleText(sheet);
					styleYear.setAlignment(HorizontalAlignment.CENTER);
					styleYear.setFont(font);

					Row rowYear = sheet.getRow(1);
					Cell cellYear = rowYear.createCell(0, CellType.STRING);
					cellYear.setCellValue("Thời gian: " + obj.getMonthYear());
					cellYear.setCellStyle(styleYear);
					
					int i = 5;
					for (RpKHBTSDTO dto : data) {
						Row row = sheet.createRow(i++);
						
						Cell cell = row.createCell(0, CellType.STRING);
						cell.setCellValue("" + (i - 5));
						cell.setCellStyle(styleNumber);
						
						cell = row.createCell(1, CellType.STRING);
						cell.setCellValue((dto.getProvinceCode() != null) ? dto.getProvinceCode() : "");
						cell.setCellStyle(style);
						
						cell = row.createCell(2, CellType.STRING);
						cell.setCellValue((dto.getVtnetCode() != null) ? dto.getVtnetCode() : "");
						cell.setCellStyle(style);
						
						cell = row.createCell(3, CellType.STRING);
						cell.setCellValue((dto.getVccCode() != null) ? dto.getVccCode() : "");
						cell.setCellStyle(style);
						
						cell = row.createCell(4, CellType.STRING);
						cell.setCellValue((dto.getPlanTTKDT() != null) ? Double.valueOf(dto.getPlanTTKDT()) : 0d);
						cell.setCellStyle(styleNumber);
						
						cell = row.createCell(5, CellType.STRING);
						cell.setCellValue((dto.getPerformTTKDT() != null) ? Double.valueOf(dto.getPerformTTKDT()) : 0d);
						cell.setCellStyle(styleNumber);
					}
				}

				workbook.write(outFile);
				workbook.close();
				outFile.close();

				String path = UEncrypt
						.encryptFileUploadPath(uploadPathReturn + File.separator + "EXPORT_STATION_TTKDT.xlsx");
				return path;
			}
			if(obj.getTypes() == 4) {
				InputStream file = new BufferedInputStream(new FileInputStream(filePath + "EXPORT_STATION_KC.xlsx"));
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
				OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "EXPORT_STATION_KC.xlsx");
				List<RpKHBTSDTO> data = rpBTSDAO.doSearchStation(obj);

				XSSFSheet sheet = workbook.getSheetAt(0);
				if (data != null && !data.isEmpty()) {
					XSSFCellStyle style = ExcelUtils.styleText(sheet);
					XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
					XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
					XSSFCreationHelper createHelper = workbook.getCreationHelper();
					styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
					styleNumber.setAlignment(HorizontalAlignment.RIGHT);
					styleNumber.setDataFormat(workbook.createDataFormat().getFormat("##0.00"));

					XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
					styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
					styleCurrency.setAlignment(HorizontalAlignment.RIGHT);

					XSSFFont font = workbook.createFont();
					font.setBold(false);
					font.setItalic(false);
					font.setFontName("Times New Roman");
					font.setFontHeightInPoints((short) 12);

					XSSFCellStyle styleYear = ExcelUtils.styleText(sheet);
					styleYear.setAlignment(HorizontalAlignment.CENTER);
					styleYear.setFont(font);

					Row rowYear = sheet.getRow(1);
					Cell cellYear = rowYear.createCell(0, CellType.STRING);
					cellYear.setCellValue("Thời gian: " + obj.getMonthYear());
					cellYear.setCellStyle(styleYear);
					
					int i = 5;
					for (RpKHBTSDTO dto : data) {
						Row row = sheet.createRow(i++);
						
						Cell cell = row.createCell(0, CellType.STRING);
						cell.setCellValue("" + (i - 5));
						cell.setCellStyle(styleNumber);
						
						cell = row.createCell(1, CellType.STRING);
						cell.setCellValue((dto.getProvinceCode() != null) ? dto.getProvinceCode() : "");
						cell.setCellStyle(style);
						
						cell = row.createCell(2, CellType.STRING);
						cell.setCellValue((dto.getVtnetCode() != null) ? dto.getVtnetCode() : "");
						cell.setCellStyle(style);
						
						cell = row.createCell(3, CellType.STRING);
						cell.setCellValue((dto.getVccCode() != null) ? dto.getVccCode() : "");
						cell.setCellStyle(style);
						
						cell = row.createCell(4, CellType.STRING);
						cell.setCellValue((dto.getPlanKC() != null) ? Double.valueOf(dto.getPlanKC()) : 0d);
						cell.setCellStyle(styleNumber);
						
						cell = row.createCell(5, CellType.STRING);
						cell.setCellValue((dto.getPerformKC() != null) ? Double.valueOf(dto.getPerformKC()) : 0d);
						cell.setCellStyle(styleNumber);

						cell = row.createCell(6, CellType.STRING);
						cell.setCellValue((dto.getNumberDateLimitKPI() != 0) ? Integer.valueOf(dto.getNumberDateLimitKPI()) : 0d);
						cell.setCellStyle(styleNumber);

						cell = row.createCell(7, CellType.STRING);
						cell.setCellValue((dto.getNumberFines() != null) ? Double.valueOf(dto.getNumberFines()) : 0d);
						cell.setCellStyle(styleNumber);
					}
				}

				workbook.write(outFile);
				workbook.close();
				outFile.close();

				String path = UEncrypt
						.encryptFileUploadPath(uploadPathReturn + File.separator + "EXPORT_STATION_KC.xlsx");
				return path;
			}
			if(obj.getTypes() == 5) {
				InputStream file = new BufferedInputStream(new FileInputStream(filePath + "EXPORT_STATION_DBHT.xlsx"));
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
				OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "EXPORT_STATION_DBHT.xlsx");
				List<RpKHBTSDTO> data = rpBTSDAO.doSearchStation(obj);

				XSSFSheet sheet = workbook.getSheetAt(0);
				if (data != null && !data.isEmpty()) {
					XSSFCellStyle style = ExcelUtils.styleText(sheet);
					XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
					XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
					XSSFCreationHelper createHelper = workbook.getCreationHelper();
					styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
					styleNumber.setAlignment(HorizontalAlignment.RIGHT);
					styleNumber.setDataFormat(workbook.createDataFormat().getFormat("##0.00"));

					XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
					styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
					styleCurrency.setAlignment(HorizontalAlignment.RIGHT);

					XSSFFont font = workbook.createFont();
					font.setBold(false);
					font.setItalic(false);
					font.setFontName("Times New Roman");
					font.setFontHeightInPoints((short) 12);

					XSSFCellStyle styleYear = ExcelUtils.styleText(sheet);
					styleYear.setAlignment(HorizontalAlignment.CENTER);
					styleYear.setFont(font);

					Row rowYear = sheet.getRow(1);
					Cell cellYear = rowYear.createCell(0, CellType.STRING);
					cellYear.setCellValue("Thời gian: " + obj.getMonthYear());
					cellYear.setCellStyle(styleYear);
					
					int i = 5;
					for (RpKHBTSDTO dto : data) {
						Row row = sheet.createRow(i++);
						
						Cell cell = row.createCell(0, CellType.STRING);
						cell.setCellValue("" + (i - 5));
						cell.setCellStyle(styleNumber);
						
						cell = row.createCell(1, CellType.STRING);
						cell.setCellValue((dto.getProvinceCode() != null) ? dto.getProvinceCode() : "");
						cell.setCellStyle(style);
						
						cell = row.createCell(2, CellType.STRING);
						cell.setCellValue((dto.getVtnetCode() != null) ? dto.getVtnetCode() : "");
						cell.setCellStyle(style);
						
						cell = row.createCell(3, CellType.STRING);
						cell.setCellValue((dto.getVccCode() != null) ? dto.getVccCode() : "");
						cell.setCellStyle(style);
						
						cell = row.createCell(4, CellType.STRING);
						cell.setCellValue((dto.getPlanDBHT() != null) ? Double.valueOf(dto.getPlanDBHT()) : 0d);
						cell.setCellStyle(styleNumber);
						
						cell = row.createCell(5, CellType.STRING);
						cell.setCellValue((dto.getPerformDBHT() != null) ? Double.valueOf(dto.getPerformDBHT()) : 0d);
						cell.setCellStyle(styleNumber);
						
						cell = row.createCell(6, CellType.STRING);
						cell.setCellValue((dto.getNumberDateLimitKPI() != 0) ? Integer.valueOf(dto.getNumberDateLimitKPI()) : 0d);
						cell.setCellStyle(styleNumber);

						cell = row.createCell(7, CellType.STRING);
						cell.setCellValue((dto.getNumberFines() != null) ? Double.valueOf(dto.getNumberFines()) : 0d);
						cell.setCellStyle(styleNumber);
					}
				}

				workbook.write(outFile);
				workbook.close();
				outFile.close();

				String path = UEncrypt
						.encryptFileUploadPath(uploadPathReturn + File.separator + "EXPORT_STATION_DBHT.xlsx");
				return path;
			}
			if(obj.getTypes() == 6) {
				InputStream file = new BufferedInputStream(new FileInputStream(filePath + "EXPORT_STATION_PS.xlsx"));
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
				OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "EXPORT_STATION_PS.xlsx");
				List<RpKHBTSDTO> data = rpBTSDAO.doSearchStation(obj);

				XSSFSheet sheet = workbook.getSheetAt(0);
				if (data != null && !data.isEmpty()) {
					XSSFCellStyle style = ExcelUtils.styleText(sheet);
					XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
					XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
					XSSFCreationHelper createHelper = workbook.getCreationHelper();
					styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
					styleNumber.setAlignment(HorizontalAlignment.RIGHT);
					styleNumber.setDataFormat(workbook.createDataFormat().getFormat("##0.00"));

					XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
					styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
					styleCurrency.setAlignment(HorizontalAlignment.RIGHT);

					XSSFFont font = workbook.createFont();
					font.setBold(false);
					font.setItalic(false);
					font.setFontName("Times New Roman");
					font.setFontHeightInPoints((short) 12);

					XSSFCellStyle styleYear = ExcelUtils.styleText(sheet);
					styleYear.setAlignment(HorizontalAlignment.CENTER);
					styleYear.setFont(font);

					Row rowYear = sheet.getRow(1);
					Cell cellYear = rowYear.createCell(0, CellType.STRING);
					cellYear.setCellValue("Thời gian: " + obj.getMonthYear());
					cellYear.setCellStyle(styleYear);
					
					int i = 5;
					for (RpKHBTSDTO dto : data) {
						Row row = sheet.createRow(i++);
						
						Cell cell = row.createCell(0, CellType.STRING);
						cell.setCellValue("" + (i - 5));
						cell.setCellStyle(styleNumber);
						
						cell = row.createCell(1, CellType.STRING);
						cell.setCellValue((dto.getProvinceCode() != null) ? dto.getProvinceCode() : "");
						cell.setCellStyle(style);
						
						cell = row.createCell(2, CellType.STRING);
						cell.setCellValue((dto.getVtnetCode() != null) ? dto.getVtnetCode() : "");
						cell.setCellStyle(style);
						
						cell = row.createCell(3, CellType.STRING);
						cell.setCellValue((dto.getVccCode() != null) ? dto.getVccCode() : "");
						cell.setCellStyle(style);
						
						cell = row.createCell(4, CellType.STRING);
						cell.setCellValue((dto.getPlanPS() != null) ? Double.valueOf(dto.getPlanPS()) : 0d);
						cell.setCellStyle(styleNumber);
						
						cell = row.createCell(5, CellType.STRING);
						cell.setCellValue((dto.getPerformPS() != null) ? Double.valueOf(dto.getPerformPS()) : 0d);
						cell.setCellStyle(styleNumber);
					}
				}

				workbook.write(outFile);
				workbook.close();
				outFile.close();

				String path = UEncrypt
						.encryptFileUploadPath(uploadPathReturn + File.separator + "EXPORT_STATION_PS.xlsx");
				return path;
			}
			if(obj.getTypes() == 7) {
				InputStream file = new BufferedInputStream(new FileInputStream(filePath + "EXPORT_STATION_DT.xlsx"));
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
				OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "EXPORT_STATION_DT.xlsx");
				List<RpKHBTSDTO> data = rpBTSDAO.doSearchStation(obj);

				XSSFSheet sheet = workbook.getSheetAt(0);
				if (data != null && !data.isEmpty()) {
					XSSFCellStyle style = ExcelUtils.styleText(sheet);
					XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
					XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
					XSSFCreationHelper createHelper = workbook.getCreationHelper();
					styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
					styleNumber.setAlignment(HorizontalAlignment.RIGHT);
					styleNumber.setDataFormat(workbook.createDataFormat().getFormat("##0.00"));

					XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
					styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
					styleCurrency.setAlignment(HorizontalAlignment.RIGHT);

					XSSFFont font = workbook.createFont();
					font.setBold(false);
					font.setItalic(false);
					font.setFontName("Times New Roman");
					font.setFontHeightInPoints((short) 12);

					XSSFCellStyle styleYear = ExcelUtils.styleText(sheet);
					styleYear.setAlignment(HorizontalAlignment.CENTER);
					styleYear.setFont(font);

					Row rowYear = sheet.getRow(1);
					Cell cellYear = rowYear.createCell(0, CellType.STRING);
					cellYear.setCellValue("Thời gian: " + obj.getMonthYear());
					cellYear.setCellStyle(styleYear);
					
					int i = 5;
					for (RpKHBTSDTO dto : data) {
						Row row = sheet.createRow(i++);
						
						Cell cell = row.createCell(0, CellType.STRING);
						cell.setCellValue("" + (i - 5));
						cell.setCellStyle(styleNumber);
						
						cell = row.createCell(1, CellType.STRING);
						cell.setCellValue((dto.getProvinceCode() != null) ? dto.getProvinceCode() : "");
						cell.setCellStyle(style);
						
						cell = row.createCell(2, CellType.STRING);
						cell.setCellValue((dto.getVtnetCode() != null) ? dto.getVtnetCode() : "");
						cell.setCellStyle(style);
						
						cell = row.createCell(3, CellType.STRING);
						cell.setCellValue((dto.getVccCode() != null) ? dto.getVccCode() : "");
						cell.setCellStyle(style);
						
						cell = row.createCell(4, CellType.STRING);
						cell.setCellValue((dto.getPlanDT() != null) ? Double.valueOf(dto.getPlanDT()) : 0d);
						cell.setCellStyle(styleNumber);
						
						cell = row.createCell(5, CellType.STRING);
						cell.setCellValue((dto.getPerformDT() != null) ? Double.valueOf(dto.getPerformDT()) : 0d);
						cell.setCellStyle(styleNumber);

						cell = row.createCell(6, CellType.STRING);
						cell.setCellValue((dto.getNumberDateLimitKPI() != 0) ? Integer.valueOf(dto.getNumberDateLimitKPI()) : 0d);
						cell.setCellStyle(styleNumber);

						cell = row.createCell(7, CellType.STRING);
						cell.setCellValue((dto.getNumberFines() != null) ? Double.valueOf(dto.getNumberFines()) : 0d);
						cell.setCellStyle(styleNumber);
					}
				}

				workbook.write(outFile);
				workbook.close();
				outFile.close();

				String path = UEncrypt
						.encryptFileUploadPath(uploadPathReturn + File.separator + "EXPORT_STATION_DT.xlsx");
				return path;
			}
			if(obj.getTypes() == 8) {
				InputStream file = new BufferedInputStream(new FileInputStream(filePath + "EXPORT_STATION_HSHC_TTHT.xlsx"));
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
				OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "EXPORT_STATION_HSHC_TTHT.xlsx");
				List<RpKHBTSDTO> data = rpBTSDAO.doSearchStation(obj);

				XSSFSheet sheet = workbook.getSheetAt(0);
				if (data != null && !data.isEmpty()) {
					XSSFCellStyle style = ExcelUtils.styleText(sheet);
					XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
					XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
					XSSFCreationHelper createHelper = workbook.getCreationHelper();
					styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
					styleNumber.setAlignment(HorizontalAlignment.RIGHT);
					styleNumber.setDataFormat(workbook.createDataFormat().getFormat("##0.00"));

					XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
					styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
					styleCurrency.setAlignment(HorizontalAlignment.RIGHT);

					XSSFFont font = workbook.createFont();
					font.setBold(false);
					font.setItalic(false);
					font.setFontName("Times New Roman");
					font.setFontHeightInPoints((short) 12);

					XSSFCellStyle styleYear = ExcelUtils.styleText(sheet);
					styleYear.setAlignment(HorizontalAlignment.CENTER);
					styleYear.setFont(font);

					Row rowYear = sheet.getRow(1);
					Cell cellYear = rowYear.createCell(0, CellType.STRING);
					cellYear.setCellValue("Thời gian: " + obj.getMonthYear());
					cellYear.setCellStyle(styleYear);
					
					int i = 5;
					for (RpKHBTSDTO dto : data) {
						Row row = sheet.createRow(i++);
						
						Cell cell = row.createCell(0, CellType.STRING);
						cell.setCellValue("" + (i - 5));
						cell.setCellStyle(styleNumber);
						
						cell = row.createCell(1, CellType.STRING);
						cell.setCellValue((dto.getProvinceCode() != null) ? dto.getProvinceCode() : "");
						cell.setCellStyle(style);
						
						cell = row.createCell(2, CellType.STRING);
						cell.setCellValue((dto.getVtnetCode() != null) ? dto.getVtnetCode() : "");
						cell.setCellStyle(style);
						
						cell = row.createCell(3, CellType.STRING);
						cell.setCellValue((dto.getVccCode() != null) ? dto.getVccCode() : "");
						cell.setCellStyle(style);
						
						cell = row.createCell(4, CellType.STRING);
						cell.setCellValue((dto.getPlanHSHCTTHT() != null) ? Double.valueOf(dto.getPlanHSHCTTHT()) : 0d);
						cell.setCellStyle(styleNumber);
						
						cell = row.createCell(5, CellType.STRING);
						cell.setCellValue((dto.getPerformHSHCTTHT() != null) ? Double.valueOf(dto.getPerformHSHCTTHT()) : 0d);
						cell.setCellStyle(styleNumber);

						cell = row.createCell(6, CellType.STRING);
						cell.setCellValue((dto.getNumberDateLimitKPI() != 0) ? Integer.valueOf(dto.getNumberDateLimitKPI()) : 0d);
						cell.setCellStyle(styleNumber);

						cell = row.createCell(7, CellType.STRING);
						cell.setCellValue((dto.getNumberFines() != null) ? Double.valueOf(dto.getNumberFines()) : 0d);
						cell.setCellStyle(styleNumber);
					}
				}

				workbook.write(outFile);
				workbook.close();
				outFile.close();

				String path = UEncrypt
						.encryptFileUploadPath(uploadPathReturn + File.separator + "EXPORT_STATION_HSHC_TTHT.xlsx");
				return path;
			}
			else {
				return null;
			}
			
		}
	 
	private void setValueStringCell(Row row, Cell cell, int stt, String value, XSSFCellStyle style) {
		cell = row.createCell(stt, CellType.STRING);
		cell.setCellValue((value != null) ? value : "");
		cell.setCellStyle(style);
	}
	
	private void setValueDoubleCell(Row row, Cell cell, int stt, Double value, XSSFCellStyle style) {
		cell = row.createCell(stt, CellType.STRING);
		cell.setCellValue((value != null) ? value : 0d);
		cell.setCellStyle(style);
	}
	
	private void setValueLongCell(Row row, Cell cell, int stt, Long value, XSSFCellStyle style) {
		cell = row.createCell(stt, CellType.STRING);
		cell.setCellValue((value != null) ? value : 0l);
		cell.setCellStyle(style);
	}
	
	public String exportexcelKHBTS(RpKHBTSDTO obj, HttpServletRequest request) throws Exception {
		obj.setPage(null);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();

		String fileName = "EXPORT_KH_BTS.xlsx";
		if(obj.getTypeBc().equals("2")) {
			fileName = "EXPORT_KH_BTS_DETAIL.xlsx";
		}
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + fileName));
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
		OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + fileName);
		List<RpKHBTSDTO> data = rpBTSDAO.doSearchReportKHBTS(obj);

		XSSFSheet sheet = workbook.getSheetAt(0);
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			styleNumber.setDataFormat(workbook.createDataFormat().getFormat("##0.00"));

			XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
			styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			styleCurrency.setAlignment(HorizontalAlignment.RIGHT);

			XSSFFont font = workbook.createFont();
			font.setBold(false);
			font.setItalic(false);
			font.setFontName("Times New Roman");
			font.setFontHeightInPoints((short) 12);

			XSSFCellStyle styleYear = ExcelUtils.styleText(sheet);
			styleYear.setAlignment(HorizontalAlignment.CENTER);
			styleYear.setFont(font);

			Row rowYear = sheet.getRow(1);
			Cell cellYear = rowYear.createCell(0, CellType.STRING);
			cellYear.setCellValue("Thời gian: " + obj.getMonthYearD());
			cellYear.setCellStyle(styleYear);

			int i = 5;
			for (RpKHBTSDTO dto : data) {
				Row row = sheet.createRow(i++);

				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 5));
				cell.setCellStyle(styleNumber);

				if(obj.getTypeBc().equals("2")) {
					setValueStringCell(row, cell, 1, dto.getStationVccCode(), style);
					setValueStringCell(row, cell, 2, dto.getStationVtNetCode(), style);
					setValueStringCell(row, cell, 3, dto.getProvinceCode(), style);
					setValueDoubleCell(row, cell, 4, dto.getPlanTMB(), styleNumber);
					setValueDoubleCell(row, cell, 5, dto.getPerformTMB(), styleNumber);
					setValueDoubleCell(row, cell, 6, dto.getRatioTMB(), styleNumber);
					setValueDoubleCell(row, cell, 7, dto.getPlanTKDT(), styleNumber);
					setValueDoubleCell(row, cell, 8, dto.getPerformTTKDT(), styleNumber);
					setValueDoubleCell(row, cell, 9, dto.getRatioTKDT(), styleNumber);
					setValueDoubleCell(row, cell, 10, dto.getPlanTTKDT(), styleNumber);
					setValueDoubleCell(row, cell, 11, dto.getPerformTTKDT(), styleNumber);
					setValueDoubleCell(row, cell, 12, dto.getRatioTTKDT(), styleNumber);
					setValueDoubleCell(row, cell, 13, dto.getPlanKC(), styleNumber);
					setValueDoubleCell(row, cell, 14, dto.getPerformKC(), styleNumber);
					setValueDoubleCell(row, cell, 15, dto.getRatioKC(), styleNumber);
					setValueDoubleCell(row, cell, 16, dto.getPlanDBHT(), styleNumber);
					setValueDoubleCell(row, cell, 17, dto.getPerformDBHT(), styleNumber);
					setValueDoubleCell(row, cell, 18, dto.getRatioDBHT(), styleNumber);
					setValueDoubleCell(row, cell, 19, dto.getPlanPS(), styleNumber);
					setValueDoubleCell(row, cell, 20, dto.getPerformPS(), styleNumber);
					setValueDoubleCell(row, cell, 21, dto.getRatioPS(), styleNumber);
					setValueDoubleCell(row, cell, 22, dto.getPlanDT(), styleNumber);
					setValueDoubleCell(row, cell, 23, dto.getPerformDT(), styleNumber);
					setValueDoubleCell(row, cell, 24, dto.getRatioDT(), styleNumber);
					setValueDoubleCell(row, cell, 25, dto.getPlanHSHCTTHT(), styleNumber);
					setValueDoubleCell(row, cell, 26, dto.getPerformHSHCTTHT(), styleNumber);
					setValueDoubleCell(row, cell, 27, dto.getRatioHSHCTTHT(), styleNumber);
					setValueDoubleCell(row, cell, 28, dto.getPlanHSHCDTHT(), styleNumber);
					setValueDoubleCell(row, cell, 29, dto.getPerformHSHCDTHT(), styleNumber);
					setValueDoubleCell(row, cell, 30, dto.getRatioHSHCDTHT(), styleNumber);
					setValueDoubleCell(row, cell, 31, dto.getOverdueKPIRentMB(), styleNumber);
					setValueDoubleCell(row, cell, 32, dto.getOverdueKPIKC(), styleNumber);
					setValueDoubleCell(row, cell, 33, dto.getOverdueKPIDBHT(), styleNumber);
					setValueDoubleCell(row, cell, 34, dto.getOverdueKPILDT(), styleNumber);
					setValueDoubleCell(row, cell, 35, dto.getOverdueKPIHSHCTTHT(), styleNumber);
					setValueDoubleCell(row, cell, 36, dto.getOverdueKPIHSHCDTHT(), styleNumber);
					setValueDoubleCell(row, cell, 37, dto.getOverdueTKDT(), styleNumber);
					setValueDoubleCell(row, cell, 38, dto.getRentMB(), styleNumber);
					setValueDoubleCell(row, cell, 39, dto.getStartUp(), styleNumber);
					setValueDoubleCell(row, cell, 40, dto.getRentDBHT(), styleNumber);
					setValueDoubleCell(row, cell, 41, dto.getDraftingRevenue(), styleNumber);
					setValueDoubleCell(row, cell, 42, dto.getHshcTTHT(), styleNumber);	
				} else {
					setValueStringCell(row, cell, 1, dto.getProvinceCode(), style);
					setValueDoubleCell(row, cell, 2, dto.getPlanTMB(), styleNumber);
					setValueDoubleCell(row, cell, 3, dto.getPerformTMB(), styleNumber);
					setValueDoubleCell(row, cell, 4, dto.getRatioTMB(), styleNumber);
					setValueDoubleCell(row, cell, 5, dto.getPlanTKDT(), styleNumber);
					setValueDoubleCell(row, cell, 6, dto.getPerformTTKDT(), styleNumber);
					setValueDoubleCell(row, cell, 7, dto.getRatioTKDT(), styleNumber);
					setValueDoubleCell(row, cell, 8, dto.getPlanTTKDT(), styleNumber);
					setValueDoubleCell(row, cell, 9, dto.getPerformTTKDT(), styleNumber);
					setValueDoubleCell(row, cell, 10, dto.getRatioTTKDT(), styleNumber);
					setValueDoubleCell(row, cell, 11, dto.getPlanKC(), styleNumber);
					setValueDoubleCell(row, cell, 12, dto.getPerformKC(), styleNumber);
					setValueDoubleCell(row, cell, 13, dto.getRatioKC(), styleNumber);
					setValueDoubleCell(row, cell, 14, dto.getPlanDBHT(), styleNumber);
					setValueDoubleCell(row, cell, 15, dto.getPerformDBHT(), styleNumber);
					setValueDoubleCell(row, cell, 16, dto.getRatioDBHT(), styleNumber);
					setValueDoubleCell(row, cell, 17, dto.getPlanPS(), styleNumber);
					setValueDoubleCell(row, cell, 18, dto.getPerformPS(), styleNumber);
					setValueDoubleCell(row, cell, 19, dto.getRatioPS(), styleNumber);
					setValueDoubleCell(row, cell, 20, dto.getPlanDT(), styleNumber);
					setValueDoubleCell(row, cell, 21, dto.getPerformDT(), styleNumber);
					setValueDoubleCell(row, cell, 22, dto.getRatioDT(), styleNumber);
					setValueDoubleCell(row, cell, 23, dto.getPlanHSHCTTHT(), styleNumber);
					setValueDoubleCell(row, cell, 24, dto.getPerformHSHCTTHT(), styleNumber);
					setValueDoubleCell(row, cell, 25, dto.getRatioHSHCTTHT(), styleNumber);
					setValueDoubleCell(row, cell, 26, dto.getPlanHSHCDTHT(), styleNumber);
					setValueDoubleCell(row, cell, 27, dto.getPerformHSHCDTHT(), styleNumber);
					setValueDoubleCell(row, cell, 28, dto.getRatioHSHCDTHT(), styleNumber);
					setValueDoubleCell(row, cell, 29, dto.getOverdueKPIRentMB(), styleNumber);
					setValueDoubleCell(row, cell, 30, dto.getOverdueKPIKC(), styleNumber);
					setValueDoubleCell(row, cell, 31, dto.getOverdueKPIDBHT(), styleNumber);
					setValueDoubleCell(row, cell, 32, dto.getOverdueKPILDT(), styleNumber);
					setValueDoubleCell(row, cell, 33, dto.getOverdueKPIHSHCTTHT(), styleNumber);
					setValueDoubleCell(row, cell, 34, dto.getOverdueKPIHSHCDTHT(), styleNumber);
					setValueDoubleCell(row, cell, 35, dto.getOverdueTKDT(), styleNumber);
					setValueDoubleCell(row, cell, 36, dto.getRentMB(), styleNumber);
					setValueDoubleCell(row, cell, 37, dto.getStartUp(), styleNumber);
					setValueDoubleCell(row, cell, 38, dto.getRentDBHT(), styleNumber);
					setValueDoubleCell(row, cell, 39, dto.getDraftingRevenue(), styleNumber);
					setValueDoubleCell(row, cell, 40, dto.getHshcTTHT(), styleNumber);
				}
			}
			if (data.size() > 0) {
				RpKHBTSDTO dto = data.get(0);
				Row row = sheet.createRow(i);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				if (obj.getTypeBc().equals("2")) {
					setValueStringCell(row, cell, 1, "", style);
					setValueStringCell(row, cell, 2, "", style);
					setValueStringCell(row, cell, 3, "Tổng", style);
					setValueDoubleCell(row, cell, 4, dto.getSumPlanTMB(), styleNumber);
					setValueDoubleCell(row, cell, 5, dto.getSumPerformTMB(), styleNumber);
					setValueDoubleCell(row, cell, 6, dto.getSumRatioTMB(), styleNumber);
					setValueDoubleCell(row, cell, 7, dto.getSumPlanTKDT(), styleNumber);
					setValueDoubleCell(row, cell, 8, dto.getSumPerformTTKDT(), styleNumber);
					setValueDoubleCell(row, cell, 9, dto.getSumRatioTKDT(), styleNumber);
					setValueDoubleCell(row, cell, 10, dto.getSumPlanTTKDT(), styleNumber);
					setValueDoubleCell(row, cell, 11, dto.getSumPerformTTKDT(), styleNumber);
					setValueDoubleCell(row, cell, 12, dto.getSumRatioTTKDT(), styleNumber);
					setValueDoubleCell(row, cell, 13, dto.getSumPlanKC(), styleNumber);
					setValueDoubleCell(row, cell, 14, dto.getSumPerformKC(), styleNumber);
					setValueDoubleCell(row, cell, 15, dto.getSumRatioKC(), styleNumber);
					setValueDoubleCell(row, cell, 16, dto.getSumPlanDBHT(), styleNumber);
					setValueDoubleCell(row, cell, 17, dto.getSumPerformDBHT(), styleNumber);
					setValueDoubleCell(row, cell, 18, dto.getSumRatioDBHT(), styleNumber);
					setValueDoubleCell(row, cell, 19, dto.getSumPlanPS(), styleNumber);
					setValueDoubleCell(row, cell, 20, dto.getSumPerformPS(), styleNumber);
					setValueDoubleCell(row, cell, 21, dto.getSumRatioPS(), styleNumber);
					setValueDoubleCell(row, cell, 22, dto.getSumPlanDT(), styleNumber);
					setValueDoubleCell(row, cell, 23, dto.getSumPerformDT(), styleNumber);
					setValueDoubleCell(row, cell, 24, dto.getSumRatioDT(), styleNumber);
					setValueDoubleCell(row, cell, 25, dto.getSumPlanHSHCTTHT(), styleNumber);
					setValueDoubleCell(row, cell, 26, dto.getSumPerformHSHCTTHT(), styleNumber);
					setValueDoubleCell(row, cell, 27, dto.getSumRatioHSHCTTHT(), styleNumber);
					setValueDoubleCell(row, cell, 28, dto.getSumPlanHSHCDTHT(), styleNumber);
					setValueDoubleCell(row, cell, 29, dto.getSumPerformHSHCDTHT(), styleNumber);
					setValueDoubleCell(row, cell, 30, dto.getSumRatioHSHCDTHT(), styleNumber);
					setValueDoubleCell(row, cell, 31, dto.getSumOverdueKPIRentMB(), styleNumber);
					setValueDoubleCell(row, cell, 32, dto.getSumOverdueKPIKC(), styleNumber);
					setValueDoubleCell(row, cell, 33, dto.getSumOverdueKPIDBHT(), styleNumber);
					setValueDoubleCell(row, cell, 34, dto.getSumOverdueKPILDT(), styleNumber);
					setValueDoubleCell(row, cell, 35, dto.getSumOverdueKPIHSHCTTHT(), styleNumber);
					setValueDoubleCell(row, cell, 36, dto.getSumOverdueKPIHSHCDTHT(), styleNumber);
					setValueDoubleCell(row, cell, 37, dto.getSumOverdueTKDT(), styleNumber);
					setValueDoubleCell(row, cell, 38, dto.getSumRentMB(), styleNumber);
					setValueDoubleCell(row, cell, 39, dto.getSumStartUp(), styleNumber);
					setValueDoubleCell(row, cell, 40, dto.getSumRentDBHT(), styleNumber);
					setValueDoubleCell(row, cell, 41, dto.getSumDraftingRevenue(), styleNumber);
					setValueDoubleCell(row, cell, 42, dto.getSumHshcTTHT(), styleNumber);
				} else {
					setValueStringCell(row, cell, 1, "Tổng", style);
					setValueDoubleCell(row, cell, 2, dto.getSumPlanTMB(), styleNumber);
					setValueDoubleCell(row, cell, 3, dto.getSumPerformTMB(), styleNumber);
					setValueDoubleCell(row, cell, 4, dto.getSumRatioTMB(), styleNumber);
					setValueDoubleCell(row, cell, 5, dto.getSumPlanTKDT(), styleNumber);
					setValueDoubleCell(row, cell, 6, dto.getSumPerformTTKDT(), styleNumber);
					setValueDoubleCell(row, cell, 7, dto.getSumRatioTKDT(), styleNumber);
					setValueDoubleCell(row, cell, 8, dto.getSumPlanTTKDT(), styleNumber);
					setValueDoubleCell(row, cell, 9, dto.getSumPerformTTKDT(), styleNumber);
					setValueDoubleCell(row, cell, 10, dto.getSumRatioTTKDT(), styleNumber);
					setValueDoubleCell(row, cell, 11, dto.getSumPlanKC(), styleNumber);
					setValueDoubleCell(row, cell, 12, dto.getSumPerformKC(), styleNumber);
					setValueDoubleCell(row, cell, 13, dto.getSumRatioKC(), styleNumber);
					setValueDoubleCell(row, cell, 14, dto.getSumPlanDBHT(), styleNumber);
					setValueDoubleCell(row, cell, 15, dto.getSumPerformDBHT(), styleNumber);
					setValueDoubleCell(row, cell, 16, dto.getSumRatioDBHT(), styleNumber);
					setValueDoubleCell(row, cell, 17, dto.getSumPlanPS(), styleNumber);
					setValueDoubleCell(row, cell, 18, dto.getSumPerformPS(), styleNumber);
					setValueDoubleCell(row, cell, 19, dto.getSumRatioPS(), styleNumber);
					setValueDoubleCell(row, cell, 20, dto.getSumPlanDT(), styleNumber);
					setValueDoubleCell(row, cell, 21, dto.getSumPerformDT(), styleNumber);
					setValueDoubleCell(row, cell, 22, dto.getSumRatioDT(), styleNumber);
					setValueDoubleCell(row, cell, 23, dto.getSumPlanHSHCTTHT(), styleNumber);
					setValueDoubleCell(row, cell, 24, dto.getSumPerformHSHCTTHT(), styleNumber);
					setValueDoubleCell(row, cell, 25, dto.getSumRatioHSHCTTHT(), styleNumber);
					setValueDoubleCell(row, cell, 26, dto.getSumPlanHSHCDTHT(), styleNumber);
					setValueDoubleCell(row, cell, 27, dto.getSumPerformHSHCDTHT(), styleNumber);
					setValueDoubleCell(row, cell, 28, dto.getSumRatioHSHCDTHT(), styleNumber);
					setValueDoubleCell(row, cell, 29, dto.getSumOverdueKPIRentMB(), styleNumber);
					setValueDoubleCell(row, cell, 30, dto.getSumOverdueKPIKC(), styleNumber);
					setValueDoubleCell(row, cell, 31, dto.getSumOverdueKPIDBHT(), styleNumber);
					setValueDoubleCell(row, cell, 32, dto.getSumOverdueKPILDT(), styleNumber);
					setValueDoubleCell(row, cell, 33, dto.getSumOverdueKPIHSHCTTHT(), styleNumber);
					setValueDoubleCell(row, cell, 34, dto.getSumOverdueKPIHSHCDTHT(), styleNumber);
					setValueDoubleCell(row, cell, 35, dto.getSumOverdueTKDT(), styleNumber);
					setValueDoubleCell(row, cell, 36, dto.getSumRentMB(), styleNumber);
					setValueDoubleCell(row, cell, 37, dto.getSumStartUp(), styleNumber);
					setValueDoubleCell(row, cell, 38, dto.getSumRentDBHT(), styleNumber);
					setValueDoubleCell(row, cell, 39, dto.getSumDraftingRevenue(), styleNumber);
					setValueDoubleCell(row, cell, 40, dto.getSumHshcTTHT(), styleNumber);
				}
			}
		}

		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + fileName);
		return path;
	}
	
	
	 public DataListDTO doSearchReportBTSByDA(ReportBTSByDADTO obj) {
	        List<ReportBTSByDADTO> ls = rpBTSDAO.doSearchReportBTSByDA(obj);
	        DataListDTO data = new DataListDTO();
	        data.setData(ls);
	        data.setTotal(obj.getTotalRecord());
	        data.setSize(obj.getPageSize());
	        data.setStart(1);
	        return data;
	    }
	 
	 public String exportexcelRPBTSByDA(ReportBTSByDADTO obj, HttpServletRequest request) throws Exception {
			obj.setPage(null);
			obj.setPageSize(null);
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			String filePath = classloader.getResource("../" + "doc-template").getPath();

			String fileName = "BAO_CAO_BTS_THEO_DA.xlsx";
			if(obj.getTypeBc().equals("2")) {
				fileName = "BAO_CAO_BTS_THEO_DA_DETAIL.xlsx";
			}
			InputStream file = new BufferedInputStream(new FileInputStream(filePath + fileName));
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
			OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + fileName);
			List<ReportBTSByDADTO> data = rpBTSDAO.doSearchReportBTSByDA(obj);

			XSSFSheet sheet = workbook.getSheetAt(0);
			if (data != null && !data.isEmpty()) {
				XSSFCellStyle style = ExcelUtils.styleText(sheet);
				XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
				XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
				XSSFCreationHelper createHelper = workbook.getCreationHelper();
				styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
				styleNumber.setAlignment(HorizontalAlignment.RIGHT);
				styleNumber.setDataFormat(workbook.createDataFormat().getFormat("##0.00"));

				XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
				styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
				styleCurrency.setAlignment(HorizontalAlignment.RIGHT);

				XSSFFont font = workbook.createFont();
				font.setBold(false);
				font.setItalic(false);
				font.setFontName("Times New Roman");
				font.setFontHeightInPoints((short) 12);

				XSSFCellStyle styleYear = ExcelUtils.styleText(sheet);
				styleYear.setAlignment(HorizontalAlignment.CENTER);
				styleYear.setFont(font);

				Row rowYear = sheet.getRow(1);
				Cell cellYear = rowYear.createCell(0, CellType.STRING);
				cellYear.setCellValue("Thời gian: " + obj.getMonthYearD());
				cellYear.setCellStyle(styleYear);

				int i = 6;
				for (ReportBTSByDADTO dto : data) {
					Row row = sheet.createRow(i++);

					Cell cell = row.createCell(0, CellType.STRING);
					cell.setCellValue("" + (i - 6));
					cell.setCellStyle(styleNumber);

					if(obj.getTypeBc().equals("2")) {
						setValueStringCell(row, cell, 1, dto.getProvinceCode(), style);
						setValueStringCell(row, cell, 2, dto.getStationVccCode(), style);
						setValueStringCell(row, cell, 3, dto.getStationVtNetCode(), style);
						setValueDoubleCell(row, cell, 4, dto.getCount(), styleNumber);
						setValueDoubleCell(row, cell, 5, dto.gettMB(), styleNumber);
						setValueDoubleCell(row, cell, 6, dto.gettKDT(), styleNumber);
						setValueDoubleCell(row, cell, 7, dto.getThamTKDT(), styleNumber);
						setValueDoubleCell(row, cell, 8, dto.getTonChuaCoTKDTTham(), styleNumber);
						setValueDoubleCell(row, cell, 9, dto.getVuong(), styleNumber);
						setValueDoubleCell(row, cell, 10, dto.getDaKC(), styleNumber);
						setValueDoubleCell(row, cell, 11, dto.getdBHT(), styleNumber);
						setValueDoubleCell(row, cell, 12, dto.getDaPS(), styleNumber);
						setValueDoubleCell(row, cell, 13, dto.gethSHCVeDTHT(), styleNumber);
						setValueDoubleCell(row, cell, 14, dto.gethSHCVeDTHT(), styleNumber);
						setValueDoubleCell(row, cell, 15, dto.getCountTonHSHCChuaVeTTHT(), styleNumber);
						setValueDoubleCell(row, cell, 16, dto.getN1TonHSHCChuaVeTTHT(), styleNumber);
						setValueDoubleCell(row, cell, 17, dto.getN2TonHSHCChuaVeTTHT(), styleNumber);
						setValueDoubleCell(row, cell, 18, dto.getN3TonHSHCChuaVeTTHT(), styleNumber);
						setValueDoubleCell(row, cell, 19, dto.getN4TonHSHCChuaVeTTHT(), styleNumber);
						setValueDoubleCell(row, cell, 20, dto.getCountTonHSHCChuaVeDHTH(), styleNumber);
						setValueDoubleCell(row, cell, 21, dto.getN1TonHSHCChuaVeDHTH(), styleNumber);
						setValueDoubleCell(row, cell, 22, dto.getN2TonHSHCChuaVeDHTH(), styleNumber);
						setValueDoubleCell(row, cell, 23, dto.getN3TonHSHCChuaVeDHTH(), styleNumber);
						setValueDoubleCell(row, cell, 24, dto.getN4TonHSHCChuaVeDHTH(), styleNumber);
						setValueStringCell(row, cell, 25, dto.getDescription(), style);
					} else {
						setValueStringCell(row, cell, 1, dto.getProvinceCode(), style);
						setValueDoubleCell(row, cell, 2, dto.getCount(), styleNumber);
						setValueDoubleCell(row, cell, 3, dto.gettMB(), styleNumber);
						setValueDoubleCell(row, cell, 4, dto.gettKDT(), styleNumber);
						setValueDoubleCell(row, cell, 5, dto.getThamTKDT(), styleNumber);
						setValueDoubleCell(row, cell, 6, dto.getTonChuaCoTKDTTham(), styleNumber);
						setValueDoubleCell(row, cell, 7, dto.getVuong(), styleNumber);
						setValueDoubleCell(row, cell, 8, dto.getDaKC(), styleNumber);
						setValueDoubleCell(row, cell, 9, dto.getdBHT(), styleNumber);
						setValueDoubleCell(row, cell, 10, dto.getDaPS(), styleNumber);
						setValueDoubleCell(row, cell, 11, dto.gethSHCVeDTHT(), styleNumber);
						setValueDoubleCell(row, cell, 12, dto.gethSHCVeDTHT(), styleNumber);
						setValueDoubleCell(row, cell, 13, dto.getCountTonHSHCChuaVeTTHT(), styleNumber);
						setValueDoubleCell(row, cell, 14, dto.getN1TonHSHCChuaVeTTHT(), styleNumber);
						setValueDoubleCell(row, cell, 15, dto.getN2TonHSHCChuaVeTTHT(), styleNumber);
						setValueDoubleCell(row, cell, 16, dto.getN3TonHSHCChuaVeTTHT(), styleNumber);
						setValueDoubleCell(row, cell, 17, dto.getN4TonHSHCChuaVeTTHT(), styleNumber);
						setValueDoubleCell(row, cell, 18, dto.getCountTonHSHCChuaVeDHTH(), styleNumber);
						setValueDoubleCell(row, cell, 19, dto.getN1TonHSHCChuaVeDHTH(), styleNumber);
						setValueDoubleCell(row, cell, 20, dto.getN2TonHSHCChuaVeDHTH(), styleNumber);
						setValueDoubleCell(row, cell, 21, dto.getN3TonHSHCChuaVeDHTH(), styleNumber);
						setValueDoubleCell(row, cell, 22, dto.getN4TonHSHCChuaVeDHTH(), styleNumber);
						setValueStringCell(row, cell, 23, dto.getDescription(), style);
					}
				}
				if (data.size() > 0) {
					ReportBTSByDADTO dto = data.get(0);
					Row row = sheet.createRow(i);
					Cell cell = row.createCell(0, CellType.STRING);
					cell.setCellValue("");
					cell.setCellStyle(style);
					if (obj.getTypeBc().equals("2")) {
						setValueStringCell(row, cell, 1, "Tổng", style);
						setValueStringCell(row, cell, 2, "", style);
						setValueStringCell(row, cell, 3, "", style);
						setValueDoubleCell(row, cell, 4, dto.getSumCount(), styleNumber);
						setValueDoubleCell(row, cell, 5, dto.getSumTMB(), styleNumber);
						setValueDoubleCell(row, cell, 6, dto.getSumTKDT(), styleNumber);
						setValueDoubleCell(row, cell, 7, dto.getSumThamTKDT(), styleNumber);
						setValueDoubleCell(row, cell, 8, dto.getSumTonChuaCoTKDTTham(), styleNumber);
						setValueDoubleCell(row, cell, 9, dto.getSumVuong(), styleNumber);
						setValueDoubleCell(row, cell, 10, dto.getSumDaKC(), styleNumber);
						setValueDoubleCell(row, cell, 11, dto.getSumDBHT(), styleNumber);
						setValueDoubleCell(row, cell, 12, dto.getSumDaPS(), styleNumber);
						setValueDoubleCell(row, cell, 13, dto.getSumHSHCVeDTHT(), styleNumber);
						setValueDoubleCell(row, cell, 14, dto.getSumHSHCVeDTHT(), styleNumber);
						setValueDoubleCell(row, cell, 15, dto.getSumCountTonHSHCChuaVeTTHT(), styleNumber);
						setValueDoubleCell(row, cell, 16, dto.getSumN1TonHSHCChuaVeTTHT(), styleNumber);
						setValueDoubleCell(row, cell, 17, dto.getSumN2TonHSHCChuaVeTTHT(), styleNumber);
						setValueDoubleCell(row, cell, 18, dto.getSumN3TonHSHCChuaVeTTHT(), styleNumber);
						setValueDoubleCell(row, cell, 19, dto.getSumN4TonHSHCChuaVeTTHT(), styleNumber);
						setValueDoubleCell(row, cell, 20, dto.getSumCountTonHSHCChuaVeDHTH(), styleNumber);
						setValueDoubleCell(row, cell, 21, dto.getSumN1TonHSHCChuaVeDHTH(), styleNumber);
						setValueDoubleCell(row, cell, 22, dto.getSumN2TonHSHCChuaVeDHTH(), styleNumber);
						setValueDoubleCell(row, cell, 23, dto.getSumN3TonHSHCChuaVeDHTH(), styleNumber);
						setValueDoubleCell(row, cell, 24, dto.getSumN4TonHSHCChuaVeDHTH(), styleNumber);
						setValueStringCell(row, cell, 25, dto.getDescription(), style);
					} else {
						setValueStringCell(row, cell, 1, "Tổng", style);
						setValueDoubleCell(row, cell, 2, dto.getSumCount(), styleNumber);
						setValueDoubleCell(row, cell, 3, dto.getSumTMB(), styleNumber);
						setValueDoubleCell(row, cell, 4, dto.getSumTKDT(), styleNumber);
						setValueDoubleCell(row, cell, 5, dto.getSumThamTKDT(), styleNumber);
						setValueDoubleCell(row, cell, 6, dto.getSumTonChuaCoTKDTTham(), styleNumber);
						setValueDoubleCell(row, cell, 7, dto.getSumVuong(), styleNumber);
						setValueDoubleCell(row, cell, 8, dto.getSumDaKC(), styleNumber);
						setValueDoubleCell(row, cell, 9, dto.getSumDBHT(), styleNumber);
						setValueDoubleCell(row, cell, 10, dto.getSumDaPS(), styleNumber);
						setValueDoubleCell(row, cell, 11, dto.getSumHSHCVeDTHT(), styleNumber);
						setValueDoubleCell(row, cell, 12, dto.getSumHSHCVeDTHT(), styleNumber);
						setValueDoubleCell(row, cell, 13, dto.getSumCountTonHSHCChuaVeTTHT(), styleNumber);
						setValueDoubleCell(row, cell, 14, dto.getSumN1TonHSHCChuaVeTTHT(), styleNumber);
						setValueDoubleCell(row, cell, 15, dto.getSumN2TonHSHCChuaVeTTHT(), styleNumber);
						setValueDoubleCell(row, cell, 16, dto.getSumN3TonHSHCChuaVeTTHT(), styleNumber);
						setValueDoubleCell(row, cell, 17, dto.getSumN4TonHSHCChuaVeTTHT(), styleNumber);
						setValueDoubleCell(row, cell, 18, dto.getSumCountTonHSHCChuaVeDHTH(), styleNumber);
						setValueDoubleCell(row, cell, 19, dto.getSumN1TonHSHCChuaVeDHTH(), styleNumber);
						setValueDoubleCell(row, cell, 20, dto.getSumN2TonHSHCChuaVeDHTH(), styleNumber);
						setValueDoubleCell(row, cell, 21, dto.getSumN3TonHSHCChuaVeDHTH(), styleNumber);
						setValueDoubleCell(row, cell, 22, dto.getSumN4TonHSHCChuaVeDHTH(), styleNumber);
						setValueStringCell(row, cell, 23, dto.getDescription(), style);
					}
				}
			}

			workbook.write(outFile);
			workbook.close();
			outFile.close();

			String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + fileName);
			return path;
		}
//	taotq end 27092021
	 
	// Huypq-12012022-start
	public DataListDTO doSearchReportResultPerform(ReportBTSByDADTO obj) {
		List<ReportBTSByDADTO> ls = rpBTSDAO.doSearchReportResultPerform(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
	
	public String exportExcelReportResultPerform(ReportBTSByDADTO obj, HttpServletRequest request) throws Exception {
		obj.setPage(null);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();

		String fileName = "BaoCao_TH_KetQua_ThucHien_BTS.xlsx";
		if(obj.getTypeBc().equals("2")) {
			fileName = "BaoCao_CT_KetQua_ThucHien_BTS.xlsx";
		}
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + fileName));
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
		OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + fileName);
		List<ReportBTSByDADTO> data = rpBTSDAO.doSearchReportResultPerform(obj);

		XSSFSheet sheet = workbook.getSheetAt(0);
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			styleNumber.setDataFormat(workbook.createDataFormat().getFormat("##0"));

			XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
			styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			styleCurrency.setAlignment(HorizontalAlignment.RIGHT);

			XSSFFont font = workbook.createFont();
			font.setBold(false);
			font.setItalic(false);
			font.setFontName("Times New Roman");
			font.setFontHeightInPoints((short) 12);

			XSSFCellStyle styleYear = ExcelUtils.styleText(sheet);
			styleYear.setAlignment(HorizontalAlignment.CENTER);
			styleYear.setFont(font);

			Row rowYear = sheet.getRow(1);
			Cell cellYear = rowYear.createCell(0, CellType.STRING);
			cellYear.setCellValue("Thời gian: " + obj.getMonthYearD());
			cellYear.setCellStyle(styleYear);

			int i = 3;
			for (ReportBTSByDADTO dto : data) {
				Row row = sheet.createRow(i++);

				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 3));
				cell.setCellStyle(styleNumber);

				if(obj.getTypeBc().equals("2")) {
					setValueStringCell(row, cell, 1, dto.getProvinceCode(), style);
					setValueStringCell(row, cell, 2, dto.getStationVtNetCode(), style);
					setValueStringCell(row, cell, 3, dto.getStationVccCode(), style);
					setValueStringCell(row, cell, 4, dto.getYearPlan(), style);
					setValueLongCell(row, cell, 5, dto.getMacroNumber(), styleNumber);
					setValueLongCell(row, cell, 6, dto.getRruNumber(), styleNumber);
					setValueLongCell(row, cell, 7, dto.getSmcNumber(), styleNumber);
					setValueLongCell(row, cell, 8, dto.getThueMb(), styleNumber);
					setValueLongCell(row, cell, 9, dto.getTyLeHt(), styleNumber);
					setValueLongCell(row, cell, 10, dto.getDaChotGia(), styleNumber);
					setValueDoubleCell(row, cell, 11, dto.gettKDT(), styleNumber);
					setValueDoubleCell(row, cell, 12, dto.getThamTKDT(), styleNumber);
					setValueLongCell(row, cell, 13, dto.getDuyetTKDT(), styleNumber);
					setValueDoubleCell(row, cell, 14, dto.getTonChuaCoTKDTTham(), styleNumber);
					setValueLongCell(row, cell, 15, dto.getDuDieuKienTk(), styleNumber);
					setValueDoubleCell(row, cell, 16, dto.getVuong(), styleNumber);
					setValueDoubleCell(row, cell, 17, dto.getDaKC(), styleNumber);
					setValueLongCell(row, cell, 18, dto.getXongDBHT(), styleNumber);
					setValueDoubleCell(row, cell, 19, dto.getdBHT(), styleNumber);
					setValueDoubleCell(row, cell, 20, dto.getDaPS(), styleNumber);
					setValueLongCell(row, cell, 21, dto.getDangTc(), styleNumber);
					setValueLongCell(row, cell, 22, dto.getChuaTk(), styleNumber);
					setValueDoubleCell(row, cell, 23, dto.gethSHCVeTTHT(), styleNumber);
					setValueDoubleCell(row, cell, 24, dto.gethSHCVeDTHT(), styleNumber);
					setValueLongCell(row, cell, 25, dto.gethSHCVeTc(), style);
					setValueDoubleCell(row, cell, 26, dto.getCountTonHSHCChuaVeTTHT(), styleNumber);
					setValueDoubleCell(row, cell, 27, dto.getN1TonHSHCChuaVeTTHT(), styleNumber);
					setValueDoubleCell(row, cell, 28, dto.getN2TonHSHCChuaVeTTHT(), styleNumber);
					setValueDoubleCell(row, cell, 29, dto.getN3TonHSHCChuaVeTTHT(), styleNumber);
					setValueDoubleCell(row, cell, 30, dto.getN4TonHSHCChuaVeTTHT(), styleNumber);
					setValueDoubleCell(row, cell, 31, dto.getCountTonHSHCChuaVeDHTH(), styleNumber);
					setValueDoubleCell(row, cell, 32, dto.getN1TonHSHCChuaVeDHTH(), styleNumber);
					setValueDoubleCell(row, cell, 33, dto.getN2TonHSHCChuaVeDHTH(), styleNumber);
					setValueDoubleCell(row, cell, 34, dto.getN3TonHSHCChuaVeDHTH(), styleNumber);
					setValueDoubleCell(row, cell, 35, dto.getN4TonHSHCChuaVeDHTH(), styleNumber);
					setValueLongCell(row, cell, 36, dto.getCountTonHSHCChuaVeTC(), styleNumber);
					setValueLongCell(row, cell, 37, dto.getN1TonHSHCChuaVeTC(), styleNumber);
					setValueLongCell(row, cell, 38, dto.getN2TonHSHCChuaVeTC(), styleNumber);
					setValueLongCell(row, cell, 39, dto.getN3TonHSHCChuaVeTC(), styleNumber);
					setValueLongCell(row, cell, 40, dto.getN4TonHSHCChuaVeTC(), styleNumber);
				} else {
					setValueStringCell(row, cell, 1, dto.getProvinceCode(), style);
					setValueStringCell(row, cell, 2, dto.getYearPlan(), style);
					setValueLongCell(row, cell, 3, dto.getMacroNumber(), styleNumber);
					setValueLongCell(row, cell, 4, dto.getRruNumber(), styleNumber);
					setValueLongCell(row, cell, 5, dto.getSmcNumber(), styleNumber);
					setValueLongCell(row, cell, 6, dto.getThueMb(), styleNumber);
					setValueLongCell(row, cell, 7, dto.getTyLeHt(), styleNumber);
					setValueLongCell(row, cell, 8, dto.getDaChotGia(), styleNumber);
					setValueDoubleCell(row, cell, 9, dto.gettKDT(), styleNumber);
					setValueDoubleCell(row, cell, 10, dto.getThamTKDT(), styleNumber);
					setValueLongCell(row, cell, 11, dto.getDuyetTKDT(), styleNumber);
					setValueDoubleCell(row, cell, 12, dto.getTonChuaCoTKDTTham(), styleNumber);
					setValueLongCell(row, cell, 13, dto.getDuDieuKienTk(), styleNumber);
					setValueDoubleCell(row, cell, 14, dto.getVuong(), styleNumber);
					setValueDoubleCell(row, cell, 15, dto.getDaKC(), styleNumber);
					setValueLongCell(row, cell, 16, dto.getXongDBHT(), styleNumber);
					setValueDoubleCell(row, cell, 17, dto.getdBHT(), styleNumber);
					setValueDoubleCell(row, cell, 18, dto.getDaPS(), styleNumber);
					setValueLongCell(row, cell, 19, dto.getDangTc(), styleNumber);
					setValueLongCell(row, cell, 20, dto.getChuaTk(), styleNumber);
					setValueDoubleCell(row, cell, 21, dto.gethSHCVeTTHT(), styleNumber);
					setValueDoubleCell(row, cell, 22, dto.gethSHCVeDTHT(), styleNumber);
					setValueLongCell(row, cell, 23, dto.gethSHCVeTc(), style);
					setValueDoubleCell(row, cell, 24, dto.getCountTonHSHCChuaVeTTHT(), styleNumber);
					setValueDoubleCell(row, cell, 25, dto.getN1TonHSHCChuaVeTTHT(), styleNumber);
					setValueDoubleCell(row, cell, 26, dto.getN2TonHSHCChuaVeTTHT(), styleNumber);
					setValueDoubleCell(row, cell, 27, dto.getN3TonHSHCChuaVeTTHT(), styleNumber);
					setValueDoubleCell(row, cell, 28, dto.getN4TonHSHCChuaVeTTHT(), styleNumber);
					setValueDoubleCell(row, cell, 29, dto.getCountTonHSHCChuaVeDHTH(), styleNumber);
					setValueDoubleCell(row, cell, 30, dto.getN1TonHSHCChuaVeDHTH(), styleNumber);
					setValueDoubleCell(row, cell, 31, dto.getN2TonHSHCChuaVeDHTH(), styleNumber);
					setValueDoubleCell(row, cell, 32, dto.getN3TonHSHCChuaVeDHTH(), styleNumber);
					setValueDoubleCell(row, cell, 33, dto.getN4TonHSHCChuaVeDHTH(), styleNumber);
					setValueLongCell(row, cell, 34, dto.getCountTonHSHCChuaVeTC(), styleNumber);
					setValueLongCell(row, cell, 35, dto.getN1TonHSHCChuaVeTC(), styleNumber);
					setValueLongCell(row, cell, 36, dto.getN2TonHSHCChuaVeTC(), styleNumber);
					setValueLongCell(row, cell, 37, dto.getN3TonHSHCChuaVeTC(), styleNumber);
					setValueLongCell(row, cell, 38, dto.getN4TonHSHCChuaVeTC(), styleNumber);
				}
			}
			if (data.size() > 0) {
				ReportBTSByDADTO dto = data.get(0);
				Row row = sheet.createRow(i);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				if (obj.getTypeBc().equals("2")) {
					setValueStringCell(row, cell, 1, "Tổng", style);
					setValueStringCell(row, cell, 2, "", style);
					setValueStringCell(row, cell, 3, "", style);
					setValueStringCell(row, cell, 4, "", style);
					setValueLongCell(row, cell, 5, dto.getSumMacroNumber(), styleNumber);
					setValueLongCell(row, cell, 6, dto.getSumRruNumber(), styleNumber);
					setValueLongCell(row, cell, 7, dto.getSumSmcNumber(), styleNumber);
					setValueStringCell(row, cell, 8, "", style);
					setValueLongCell(row, cell, 9, dto.getSumDaChotGia(), styleNumber);
					setValueDoubleCell(row, cell, 10, dto.getSumTKDT(), styleNumber);
					setValueDoubleCell(row, cell, 11, dto.getSumThamTKDT(), styleNumber);
					setValueLongCell(row, cell, 12, dto.getSumDuyetTKDT(), styleNumber);
					setValueDoubleCell(row, cell, 13, dto.getSumTonChuaCoTKDTTham(), styleNumber);
					setValueLongCell(row, cell, 14, dto.getSumDuDieuKienTk(), styleNumber);
					setValueDoubleCell(row, cell, 15, dto.getSumVuong(), styleNumber);
					setValueDoubleCell(row, cell, 16, dto.getSumDaKC(), styleNumber);
					setValueLongCell(row, cell, 17, dto.getSumXongDBHT(), styleNumber);
					setValueDoubleCell(row, cell, 18, dto.getSumDBHT(), styleNumber);
					setValueDoubleCell(row, cell, 19, dto.getSumDaPS(), styleNumber);
					setValueLongCell(row, cell, 20, dto.getSumDangTc(), styleNumber);
					setValueLongCell(row, cell, 21, dto.getSumChuaTk(), styleNumber);
					setValueDoubleCell(row, cell, 22, dto.getSumHSHCVeDTHT(), styleNumber);
					setValueDoubleCell(row, cell, 23, dto.getSumHSHCVeDTHT(), styleNumber);
					setValueLongCell(row, cell, 24, dto.getSumHSHCVeTc(), styleNumber);
					setValueDoubleCell(row, cell, 25, dto.getSumCountTonHSHCChuaVeTTHT(), styleNumber);
					setValueDoubleCell(row, cell, 26, dto.getSumN1TonHSHCChuaVeTTHT(), styleNumber);
					setValueDoubleCell(row, cell, 27, dto.getSumN2TonHSHCChuaVeTTHT(), styleNumber);
					setValueDoubleCell(row, cell, 28, dto.getSumN3TonHSHCChuaVeTTHT(), styleNumber);
					setValueDoubleCell(row, cell, 29, dto.getSumN4TonHSHCChuaVeTTHT(), styleNumber);
					setValueDoubleCell(row, cell, 30, dto.getSumCountTonHSHCChuaVeDHTH(), styleNumber);
					setValueDoubleCell(row, cell, 31, dto.getSumN1TonHSHCChuaVeDHTH(), styleNumber);
					setValueDoubleCell(row, cell, 32, dto.getSumN2TonHSHCChuaVeDHTH(), styleNumber);
					setValueDoubleCell(row, cell, 33, dto.getSumN3TonHSHCChuaVeDHTH(), styleNumber);
					setValueDoubleCell(row, cell, 34, dto.getSumN4TonHSHCChuaVeDHTH(), styleNumber);
					setValueLongCell(row, cell, 35, dto.getSumCountTonHSHCChuaVeTC(), styleNumber);
					setValueLongCell(row, cell, 36, dto.getSumN1TonHSHCChuaVeTC(), styleNumber);
					setValueLongCell(row, cell, 37, dto.getSumN2TonHSHCChuaVeTC(), styleNumber);
					setValueLongCell(row, cell, 38, dto.getSumN3TonHSHCChuaVeTC(), styleNumber);
					setValueLongCell(row, cell, 39, dto.getSumN4TonHSHCChuaVeTC(), styleNumber);
					setValueStringCell(row, cell, 40, dto.getDescription(), style);
				} else {
					setValueStringCell(row, cell, 1, "Tổng", style);
					setValueStringCell(row, cell, 2, "", style);
					setValueLongCell(row, cell, 3, dto.getSumMacroNumber(), styleNumber);
					setValueLongCell(row, cell, 4, dto.getSumRruNumber(), styleNumber);
					setValueLongCell(row, cell, 5, dto.getSumSmcNumber(), styleNumber);
					setValueStringCell(row, cell, 6, "", style);
					setValueLongCell(row, cell, 7, dto.getSumDaChotGia(), styleNumber);
					setValueDoubleCell(row, cell, 8, dto.getSumTKDT(), styleNumber);
					setValueDoubleCell(row, cell, 9, dto.getSumThamTKDT(), styleNumber);
					setValueLongCell(row, cell, 10, dto.getSumDuyetTKDT(), styleNumber);
					setValueDoubleCell(row, cell, 11, dto.getSumTonChuaCoTKDTTham(), styleNumber);
					setValueLongCell(row, cell, 12, dto.getSumDuDieuKienTk(), styleNumber);
					setValueDoubleCell(row, cell, 13, dto.getSumVuong(), styleNumber);
					setValueDoubleCell(row, cell, 14, dto.getSumDaKC(), styleNumber);
					setValueLongCell(row, cell, 15, dto.getSumXongDBHT(), styleNumber);
					setValueDoubleCell(row, cell, 16, dto.getSumDBHT(), styleNumber);
					setValueDoubleCell(row, cell, 17, dto.getSumDaPS(), styleNumber);
					setValueLongCell(row, cell, 18, dto.getSumDangTc(), styleNumber);
					setValueLongCell(row, cell, 19, dto.getSumChuaTk(), styleNumber);
					setValueDoubleCell(row, cell, 20, dto.getSumHSHCVeDTHT(), styleNumber);
					setValueDoubleCell(row, cell, 21, dto.getSumHSHCVeDTHT(), styleNumber);
					setValueLongCell(row, cell, 22, dto.getSumHSHCVeTc(), styleNumber);
					setValueDoubleCell(row, cell, 23, dto.getSumCountTonHSHCChuaVeTTHT(), styleNumber);
					setValueDoubleCell(row, cell, 24, dto.getSumN1TonHSHCChuaVeTTHT(), styleNumber);
					setValueDoubleCell(row, cell, 25, dto.getSumN2TonHSHCChuaVeTTHT(), styleNumber);
					setValueDoubleCell(row, cell, 26, dto.getSumN3TonHSHCChuaVeTTHT(), styleNumber);
					setValueDoubleCell(row, cell, 27, dto.getSumN4TonHSHCChuaVeTTHT(), styleNumber);
					setValueDoubleCell(row, cell, 28, dto.getSumCountTonHSHCChuaVeDHTH(), styleNumber);
					setValueDoubleCell(row, cell, 29, dto.getSumN1TonHSHCChuaVeDHTH(), styleNumber);
					setValueDoubleCell(row, cell, 30, dto.getSumN2TonHSHCChuaVeDHTH(), styleNumber);
					setValueDoubleCell(row, cell, 31, dto.getSumN3TonHSHCChuaVeDHTH(), styleNumber);
					setValueDoubleCell(row, cell, 32, dto.getSumN4TonHSHCChuaVeDHTH(), styleNumber);
					setValueLongCell(row, cell, 33, dto.getSumCountTonHSHCChuaVeTC(), styleNumber);
					setValueLongCell(row, cell, 34, dto.getSumN1TonHSHCChuaVeTC(), styleNumber);
					setValueLongCell(row, cell, 35, dto.getSumN2TonHSHCChuaVeTC(), styleNumber);
					setValueLongCell(row, cell, 36, dto.getSumN3TonHSHCChuaVeTC(), styleNumber);
					setValueLongCell(row, cell, 37, dto.getSumN4TonHSHCChuaVeTC(), styleNumber);
					setValueStringCell(row, cell, 38, dto.getDescription(), style);
				}
			}
		}

		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + fileName);
		return path;
	}
	// Huy-end
	
	public DataListDTO getProjectForAutocomplete(ReportBTSByDADTO obj) {
        List<ReportBTSByDADTO> ls = rpBTSDAO.getProjectForAutocomplete(obj);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }
	
}
