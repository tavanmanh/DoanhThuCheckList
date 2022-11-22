package com.viettel.coms.business;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.viettel.cat.dto.ConstructionImageInfo;
import com.viettel.coms.ConstructionProgressDTO;
import com.viettel.coms.bo.ConstructionTaskBO;
import com.viettel.coms.bo.RevokeCashMonthPlanBO;
import com.viettel.coms.config.JasperReportConfig;
import com.viettel.coms.dao.ConstructionDAO;
import com.viettel.coms.dao.ConstructionTaskDAO;
import com.viettel.coms.dao.ConstructionTaskDailyDAO;
import com.viettel.coms.dao.DetailMonthPlanDAO;
import com.viettel.coms.dao.DetailMonthPlanOSDAO;
import com.viettel.coms.dao.RevokeCashMonthPlanDAO;
import com.viettel.coms.dao.RpConstructionHSHCDAO;
import com.viettel.coms.dao.UtilAttachDocumentDAO;
import com.viettel.coms.dao.WorkItemDAO;
import com.viettel.coms.dto.AppParamDTO;
import com.viettel.coms.dto.CNTContractDTO;
import com.viettel.coms.dto.CatStationDTO;
import com.viettel.coms.dto.CommonDTO;
import com.viettel.coms.dto.ConstructioIocDTO;
import com.viettel.coms.dto.ConstructionDTO;
import com.viettel.coms.dto.ConstructionDetailDTO;
import com.viettel.coms.dto.ConstructionHSHCDTOHolder;
import com.viettel.coms.dto.ConstructionInfoDTO;
import com.viettel.coms.dto.ConstructionStationWorkItemDTO;
import com.viettel.coms.dto.ConstructionTaskAssignmentsGranttDTO;
import com.viettel.coms.dto.ConstructionTaskDTO;
import com.viettel.coms.dto.ConstructionTaskDTOUpdateRequest;
import com.viettel.coms.dto.ConstructionTaskDailyDTO;
import com.viettel.coms.dto.ConstructionTaskDetailDTO;
import com.viettel.coms.dto.ConstructionTaskGranttDTO;
import com.viettel.coms.dto.ConstructionTaskResourcesGranttDTO;
import com.viettel.coms.dto.ConstructionTaskSlowDTO;
import com.viettel.coms.dto.ConstructionTotalValueDTO;
import com.viettel.coms.dto.CountConstructionTaskDTO;
import com.viettel.coms.dto.DepartmentDTO;
import com.viettel.coms.dto.DetailMonthPlanDTO;
import com.viettel.coms.dto.DomainDTO;
import com.viettel.coms.dto.ExcelErrorDTO;
import com.viettel.coms.dto.GranttDTO;
import com.viettel.coms.dto.RevokeCashMonthPlanDTO;
import com.viettel.coms.dto.RpHSHCDTO;
import com.viettel.coms.dto.SysUserCOMSDTO;
import com.viettel.coms.dto.SysUserDetailCOMSDTO;
import com.viettel.coms.dto.SysUserRequest;
import com.viettel.coms.dto.UtilAttachDocumentDTO;
import com.viettel.coms.dto.WorkItemDTO;
import com.viettel.coms.dto.WorkItemDetailDTO;
import com.viettel.coms.utils.ExcelUtils;
import com.viettel.coms.utils.PermissionUtils;
import com.viettel.erp.dao.SysUserDAO;
import com.viettel.erp.dto.CntContractDTO;
import com.viettel.erp.dto.SysUserDTO;
import com.viettel.ktts.vps.VpsPermissionChecker;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.service.base.utils.StringUtils;
import com.viettel.utils.Constant;
import com.viettel.utils.ConvertData;
import com.viettel.utils.DateTimeUtils;
import com.viettel.utils.ImageUtil;
import com.viettel.wms.utils.ValidateUtils;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;

@Service("constructionTaskBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ConstructionTaskBusinessImpl
		extends BaseFWBusinessImpl<ConstructionTaskDAO, ConstructionTaskDTO, ConstructionTaskBO>
		implements ConstructionTaskBusiness {

	private static final String CREATE_TASK = "CREATE_TASK";

	@Autowired
	private UtilAttachDocumentDAO utilAttrachDocumentDao;

	@Autowired
	private ConstructionTaskDAO constructionTaskDAO;
	@Autowired
	private ConstructionTaskDailyDAO constructionTaskDailyDAO;
	@Autowired
	private ConstructionDAO constructionDAO;
	@Autowired
	private SysUserDAO sysUserDAO;
	@Autowired
	private WorkItemDAO workItemDAO;
	
	@Autowired
	private RevokeCashMonthPlanDAO revokeCashMonthPlanDAO;

	@Autowired
    private DetailMonthPlanOSDAO detailMonthPlanOSDAO;
	
	@Autowired
	private RpConstructionHSHCDAO rpConstructionHSHCDAO;
	
	
	@Value("${folder_upload2}")
	private String folder2Upload;
	@Value("${default_sub_folder_upload}")
	private String defaultSubFolderUpload;

	@Context
	HttpServletResponse response;
	@Value("${folder_upload2}")
	private String folderUpload;

	@Value("${input_image_sub_folder_upload}")
	private String input_image_sub_folder_upload;

	@Autowired
	UtilAttachDocumentDAO utilAttachDocumentDAO;

	public ConstructionTaskBusinessImpl() {
		tModel = new ConstructionTaskBO();
		tDAO = constructionTaskDAO;
	}

	@Override
	public ConstructionTaskDAO gettDAO() {
		return constructionTaskDAO;
	}

	@Override
	public long count() {
		return constructionTaskDAO.count("ConstructionTaskBO", null);
	}

	// chinhpxn20180716_start
	@Context
	HttpServletRequest request;

	@Autowired
	private DetailMonthPlanDAO detailMonthPlanDAO;

	static Logger LOGGER = LoggerFactory.getLogger(ConstructionTaskBusinessImpl.class);

	int[] validateCol = { 1, 2, 3, 4, 5 };
	int[] validateColForAddTask = { 1, 2, 3, 4, 5, 6 };
	HashMap<Integer, String> colName = new HashMap();

	{
		colName.put(1, "Mã công trình");
		colName.put(2, "Tên hạng mục");
		colName.put(3, "Người thực hiện");
		colName.put(4, "Thời gian bắt đầu");
		colName.put(5, "Thời gian kết thúc");
		colName.put(6, "Giá trị");
	}

	HashMap<Integer, String> colAlias = new HashMap();

	{
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
		colAlias.put(13, "N");
		colAlias.put(14, "O");
		colAlias.put(15, "P");
		colAlias.put(16, "Q");
		colAlias.put(17, "R");
		colAlias.put(18, "S");
		colAlias.put(19, "T");
		colAlias.put(20, "U");
	}
//	hoanm1_20181219_start
	private final int[] requiredColHSHC = new int[] {1, 6, 11,13};
	private HashMap<Integer, String> colNameHSHC = new HashMap<>();
	{
		colNameHSHC.put(1,"Ngày hoàn thành");
		colNameHSHC.put(2,"Đơn vị thực hiện");
		colNameHSHC.put(3,"Mã tỉnh");
		colNameHSHC.put(4,"Mã nhà trạm");
		colNameHSHC.put(5,"Mã trạm");
		colNameHSHC.put(6,"Mã công trình");
		colNameHSHC.put(7,"Hợp đồng");
		colNameHSHC.put(8,"HSHC kế hoạch");
		colNameHSHC.put(9,"HSHC phê duyệt");
		colNameHSHC.put(10,"Hạng mục thực hiện");
		colNameHSHC.put(11,"Phê duyệt/Từ chối");
		colNameHSHC.put(12,"Lý do từ chối");
		colNameHSHC.put(13,"Ngày thi công xong");
	}
//	hoanm1_20181219_end
	public boolean validateString(String str) {
		return (str != null && str.length() > 0);
	}

	// chinhpxn20180716_end

	public DataListDTO doSearch(ConstructionTaskDetailDTO obj) {
		List<ConstructionTaskDetailDTO> ls;
		if ("5".equals(obj.getType())) {
			ls = constructionTaskDAO.doSearchForDT(obj);
		} else if("4".equals(obj.getType())) {
			ls = constructionTaskDAO.doSearchForRentGround(obj);
		} else if("8".equals(obj.getType())) {
			ls = constructionTaskDAO.doSearchTTXD(obj);
		} else {
			ls = constructionTaskDAO.doSearch(obj);
		}
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		// data.setTotal(obj.getTotalRecord());
		// chinhpxn20180705_start
		data.setTotal(obj.getTotalRecord());
		// chinhpxn20180705_end
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}

	// START SERVICE MOBILE
	public CountConstructionTaskDTO getCountRecordfromUserRequest(SysUserRequest request) {
		CountConstructionTaskDTO dto = constructionTaskDAO.getCount(request);
		return dto;
	}

	public List<ConstructionTaskDTO> getAllConstructionTask(SysUserRequest request) {
		List<ConstructionTaskDTO> ls = constructionTaskDAO.getAllConstrucionTaskDTO(request);

		return ls;
	}

	public int updateStopReasonConstructionTask(ConstructionTaskDTOUpdateRequest request) {

		return constructionTaskDAO.updateStopConstruction(request);

	}

	public int updatePercentConstructionTask(ConstructionTaskDTOUpdateRequest request) {
		// phucvx start
		if ("1".equals(request.getConstructionTaskDTO().getQuantityByDate())) {
			constructionTaskDailyDAO.deleteTaskDaily(request.getConstructionTaskDTO().getConstructionTaskId());
			constructionTaskDailyDAO.getSession().flush();
			if (request.getListParamDto().size() > 0) {
				for (AppParamDTO dto : request.getListParamDto()) {
					if (("0").equals(dto.getConfirm())) {
						ConstructionTaskDailyDTO daily = new ConstructionTaskDailyDTO();
						daily.setAmount(dto.getAmount());
						daily.setType(dto.getCode());
						daily.setConfirm("0");
						Double quantity = request.getConstructionTaskDTO().getPrice() * dto.getAmount();
						daily.setQuantity(quantity*1000000);
						daily.setSysGroupId(request.getConstructionTaskDTO().getSysGroupId());
						daily.setConstructionTaskId(request.getConstructionTaskDTO().getConstructionTaskId());
						daily.setWorkItemId(request.getConstructionTaskDTO().getWorkItemId());
						// hoanm1_20180710_start
						daily.setCatTaskId(request.getConstructionTaskDTO().getCatTaskId());
						// hoanm1_20180710_end
						daily.setCreatedUserId(request.getSysUserRequest().getSysUserId());
						daily.setCreatedGroupId(Long.parseLong(request.getSysUserRequest().getSysGroupId()));
						daily.setCreatedDate(new Date());
						constructionTaskDailyDAO.saveObject(daily.toModel());
						constructionTaskDailyDAO.getSession().flush();
					}
				}
			}

		}
		// end
		if (request.getListConstructionImageInfo() != null) {
			// luu anh va tra ve mot list path
			List<ConstructionImageInfo> lstConstructionImages = saveConstructionImages(
					request.getListConstructionImageInfo());

			constructionTaskDAO.saveImagePathsDao(lstConstructionImages,
					request.getConstructionTaskDTO().getConstructionTaskId(), request.getSysUserRequest());
		}
		request.getConstructionTaskDTO().setTaskOrder("2");
		return constructionTaskDAO.updatePercentConstruction(request);
	}

	public CountConstructionTaskDTO getCountPerformerSupvisor(SysUserRequest request) {
		return constructionTaskDAO.getCountPermissionSupervisior(request);
	}

	public Long insertCompleteRevenueTaskOther(ConstructionTaskDetailDTO dto, SysUserRequest sysRequest,
			Long sysGroupId) {
		// chinhpxn_20180711_start
		return constructionTaskDAO.insertCompleteRevenueTaskOther(dto, sysRequest, sysGroupId);
		// chinhpxn_20180711_end
	}

	public Long insertCompleteRevenueTaskOtherTC(ConstructionTaskDetailDTO dto, SysUserRequest request, Long sysGroupId,
			KttsUserSession objUser) {
		if (dto.getType().equals("1") && dto.getChildDTOList()!=null) {
			for (ConstructionTaskDetailDTO childDto : dto.getChildDTOList()) {
				//VietNT_20190117_start
				if(dto.getReceivedStatus() !=null){
					if (dto.getReceivedStatus() == 3) {
						childDto.setReceivedStatus(3L);
					}
				}
				//VietNT_end
				if(dto.getType().equals("1") || dto.getType().equals("3")) {
					childDto.setSourceWork(dto.getSourceWork());
					childDto.setConstructionTypeNew(dto.getConstructionTypeNew());
				}
				Long result = constructionTaskDAO.insertCompleteRevenueTaskOtherTC(childDto, request, sysGroupId,
						objUser);
				if (result < 0)
					return result;
			}
			return 1L;
		}
		return constructionTaskDAO.insertCompleteRevenueTaskOtherTC(dto, request, sysGroupId, objUser);
	}

	// nhantv 20102018 import danh sách công việc làm hshc/lên doanh thu
	public Long saveListTaskFromImport(ConstructionTaskDetailDTO dto, SysUserRequest request, Long sysGroupId,
			KttsUserSession objUser) {
		try {
			for (ConstructionTaskDetailDTO childDto : dto.getChildDTOList()) {
				//Huypq-20191001-start
//				Long month = getCurrentTimeStampMonth(childDto.getStartDate());
//				Long year = getCurrentTimeStampYear(childDto.getStartDate());
				Long month = childDto.getMonth();
				Long year = childDto.getYear();
				//Huypq-20191001-end
				Long detailMonthPlanId = constructionTaskDAO.getDetailMonthPlan(month, year, sysGroupId);
				constructionTaskDAO.getParentTask(childDto, childDto.getType(), detailMonthPlanId, request, sysGroupId);
				constructionTaskDAO.insertConstructionExcute(childDto, childDto.getType(), detailMonthPlanId, request,
						sysGroupId, objUser);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0L;
		}
		return 1L;
	}

	public List<ConstructionTaskDTO> getListConstructionTaskOnDay(SysUserRequest request) {
		return constructionTaskDAO.getListConstructionTaskOnDay(request);
	}

	// END SERVICE MOBILE

	public DataListDTO doSearchForReport(ConstructionTaskDetailDTO obj) {
		List<ConstructionTaskDetailDTO> ls = constructionTaskDAO.doSearchForReport(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}

	@Override
	public List<ConstructionProgressDTO> getConstructionTaskData() {
		List<ConstructionProgressDTO> result = constructionTaskDAO.getConstructionTaskData();
		populateDateToReturn(result);
		return result;
	}

	private void populateDateToReturn(List<ConstructionProgressDTO> result) {
		for (int i = 0; i < result.size(); i++) {
			result.get(i).setOrderId("" + i);
			result.get(i).setSummary("false");
			result.get(i).setExpanded("true");
			// float percent = (float)result.get(i).getPercentComplete()/100;
			result.get(i).setPercentCompleteStr("0.5");
		}
	}

	public String exportConstructionTask(ConstructionTaskDetailDTO obj, String userName) throws Exception {
		obj.setPage(null);
		obj.setPageSize(null);
		obj.setUserName(userName);
		String sysGroupName = constructionTaskDAO.getDataSysGroupNameByUserName(obj.getUserName());
		obj.setPage(null);
		obj.setPageSize(null);
		Date dateNow = new Date();
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Bao_cao_danh_sach_cong_viec.xlsx"));
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
				udir.getAbsolutePath() + File.separator + "Bao_cao_danh_sach_cong_viec.xlsx");
		List<ConstructionTaskDetailDTO> data = constructionTaskDAO.doSearchForReport(obj);
		// DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		XSSFSheet sheet = workbook.getSheetAt(0);
		XSSFCellStyle stt = ExcelUtils.styleText(sheet);
		stt.setAlignment(HorizontalAlignment.CENTER);
		XSSFCellStyle sttbold = ExcelUtils.styleBold(sheet);

		Row rowS2 = sheet.createRow(1);
		Cell cellS10 = rowS2.createCell(0, CellType.STRING);
		cellS10.setCellValue((sysGroupName != null) ? sysGroupName : "");
		cellS10.setCellStyle(stt);
		cellS10 = rowS2.createCell(7, CellType.STRING);
		cellS10.setCellValue("Độc lập - Tự do - Hạnh phúc");
		cellS10.setCellStyle(sttbold);

		Row rowS12 = sheet.createRow(4);
		Cell cellS12 = rowS12.createCell(0, CellType.STRING);
		cellS12.setCellValue("Ngày lập báo cáo:  " + (DateTimeUtils.convertDateToString(dateNow, "dd/MM/yyyy")));
		cellS12.setCellStyle(stt);

		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			// HuyPQ-22/08/2018-start
			XSSFCellStyle sttCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			sttCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			// HuyPQ-end
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			sttCenter.setAlignment(HorizontalAlignment.CENTER);
			int i = 7;
			for (ConstructionTaskDetailDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 7));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getYear() != null && dto.getMonth() != null)
						? "Tháng " + dto.getMonth() + "/" + dto.getYear()
						: "");
				cell.setCellStyle(sttCenter);
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getTaskName() != null) ? dto.getTaskName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getPerformerName() != null) ? dto.getPerformerName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getStartDate() != null) ? dto.getStartDate() : null);
				cell.setCellStyle(sttCenter);
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getEndDate() != null) ? dto.getEndDate() : null);
				cell.setCellStyle(sttCenter);
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getConstructionCode() != null) ? dto.getConstructionCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue((dto.getSysGroupName() != null) ? dto.getSysGroupName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue(getStringStatusConstructionTask(dto.getStatus()));
				cell.setCellStyle(style);
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue(
						(dto.getCompletePercent() != null) ? getStringForCompletePercent(dto.getCompletePercent())
								: "0%");
				cell.setCellStyle(styleNumber);
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue((dto.getCompleteState().equals("1")) ? "Đúng tiến độ" : "Chậm tiến độ");
				cell.setCellStyle(style);

				// thiếu quantity

			}
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt
				.encryptFileUploadPath(uploadPathReturn + File.separator + "Bao_cao_danh_sach_cong_viec.xlsx");
		return path;
	}

	private String getStringForCompletePercent(Double completePercent) {
		// TODO Auto-generated method stub

		return completePercent + "%";
	}

	private String getStringStatusConstructionTask(String status) {
		// TODO Auto-generated method stub
		if ("1".equals(status)) {
			return "Chưa thực hiện";
		} else if ("2".equals(status)) {
			return " Đang thực hiện";
		} else if ("4".equals(status)) {
			return " Đã hoàn thành";
		} else if ("3".equals(status)) {
			return " Tạm dừng";
		}
		return null;
	}

	// Huypq-20181017-start-exportFileSlow
	public String exportConstructionTaskSlow(GranttDTO granttSearch, Long sysGroupId, HttpServletRequest request)
			throws Exception {
		granttSearch.setPage(null);
		granttSearch.setPageSize(null);
		Date dateNow = new Date();
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Hang_muc_cham_tien_do.xlsx"));
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
				udir.getAbsolutePath() + File.separator + "Hang_muc_cham_tien_do.xlsx");
		List<ConstructionTaskSlowDTO> data = new ArrayList<ConstructionTaskSlowDTO>();
		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
				Constant.AdResourceKey.WORK_PROGRESS, request);
		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
		if (groupIdList != null && !groupIdList.isEmpty()) {
			data = constructionTaskDAO.getConstructionTaskSlow(granttSearch, groupIdList);
		}
		// List<ConstructionTaskSlowDTO> data =
		// constructionTaskDAO.getConstructionTaskSlow(obj);
		XSSFSheet sheet = workbook.getSheetAt(0);
		XSSFCellStyle stt = ExcelUtils.styleText(sheet);
		stt.setAlignment(HorizontalAlignment.CENTER);
		XSSFCellStyle sttbold = ExcelUtils.styleBold(sheet);
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle sttCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			sttCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			sttCenter.setAlignment(HorizontalAlignment.CENTER);
			int i = 1;
			for (ConstructionTaskSlowDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 1));
				cell.setCellStyle(stt);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getTimeReport() != null) ? dto.getTimeReport() : "");
				cell.setCellStyle(style);
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getSysGroupName() != null) ? dto.getSysGroupName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getProvinceCode() != null) ? dto.getProvinceCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getConstructionCode() != null) ? dto.getConstructionCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getWorkItemName() != null) ? dto.getWorkItemName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getTaskName() != null) ? dto.getTaskName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue((dto.getPartnerName() != null) ? dto.getPartnerName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue((dto.getFullName() != null) ? dto.getFullName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue((dto.getEmail() != null) ? dto.getEmail() : "");
				cell.setCellStyle(style);
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue((dto.getPhoneNumber() != null) ? dto.getPhoneNumber() : "");
				cell.setCellStyle(style);
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue((dto.getStartDate() != null) ? dto.getStartDate() : null);
				cell.setCellStyle(sttCenter);
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue((dto.getEndDate() != null) ? dto.getEndDate() : null);
				cell.setCellStyle(sttCenter);

				// thiếu quantity

			}
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "Hang_muc_cham_tien_do.xlsx");
		return path;
	}
	// Huypq-20181017-end

	public DataListDTO doSearchForConsManager(ConstructionTaskDetailDTO obj, HttpServletRequest request) {
		List<ConstructionTaskDetailDTO> ls = new ArrayList<ConstructionTaskDetailDTO>();
		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
				Constant.AdResourceKey.DATA, request);
		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
		if (groupIdList != null && !groupIdList.isEmpty())
			ls = constructionTaskDAO.doSearchForConsManager(obj, groupIdList);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}

	@Override
	public List<ConstructionTaskGranttDTO> getDataForGrant(GranttDTO granttSearch, Long sysGroupId,
			HttpServletRequest request) {
		List<ConstructionTaskGranttDTO> lstRs = new ArrayList<ConstructionTaskGranttDTO>();
		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
				Constant.AdResourceKey.WORK_PROGRESS, request);
		// hoanm1_20180905_start
		String groupIdTC = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
				Constant.AdResourceKey.WORK_PROGRESS_TC, request);
		String groupIdHSHC = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
				Constant.AdResourceKey.WORK_PROGRESS_HSHC, request);
		// hoanm1_20180905_end
		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
		if (groupIdList != null && !groupIdList.isEmpty()) {
			lstRs = constructionTaskDAO.getDataForGrant(granttSearch, groupIdList, groupIdTC, groupIdHSHC);
		}
		// Long constructionIndex = 0L;
		// Long workItemIndex = 0L;
		// Long taskIndex = 0L;
		// List<ConstructionTaskGranttDTO> sysGroupLst = new
		// ArrayList<ConstructionTaskGranttDTO>();
		// for (ConstructionTaskGranttDTO dto : lstRs) {
		// if (dto.getLevelId() == 1L) {
		// constructionIndex = 0L;
		// dto.setFullname("");
		// sysGroupLst.add(dto);
		// } else if (dto.getLevelId() == 2L) {
		// workItemIndex = 0L;
		// dto.setOrderID(constructionIndex);
		// dto.setFullname("");
		// constructionIndex++;
		// } else if (dto.getLevelId() == 3L) {
		// taskIndex = 0L;
		// dto.setOrderID(workItemIndex);
		// workItemIndex++;
		// } else {
		// dto.setOrderID(taskIndex);
		// dto.setSummary(false);
		// taskIndex++;
		// }
		// }
		return lstRs;
	}

	@Override
	public List<ConstructionTaskResourcesGranttDTO> getDataResources() {

		return constructionTaskDAO.getDataResources();
	}

	@Override
	public List<ConstructionTaskAssignmentsGranttDTO> getDataAssignments(GranttDTO granttSearch) {

		return constructionTaskDAO.getDataAssignments(granttSearch);
	}

	public int insertConstruction(ConstructionTaskDTOUpdateRequest request) {
		// return constructionTaskDAO.saveObject(dto.toModel()).intValue();
		return constructionTaskDAO.inserConstructionTask(request);
	}

	public DataListDTO doSearchForRevenue(ConstructionTaskDetailDTO obj, HttpServletRequest request) {
		List<ConstructionTaskDetailDTO> ls = new ArrayList<ConstructionTaskDetailDTO>();
		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
				Constant.AdResourceKey.DATA, request);
		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
		if (groupIdList != null && !groupIdList.isEmpty()) {
			ls = constructionTaskDAO.doSearchForRevenue(obj, groupIdList);
		}
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}

	public String exportContructionHSHC(ConstructionTaskDetailDTO obj, HttpServletRequest request) throws Exception {
		String fileName ="";
		if(obj.getType() != null){
			if("1".equals(obj.getType())){
				fileName = "Bao_cao_quan_li_QL_ngoaiOS.xlsx";
			}
		} else {
			fileName = "Bao_cao_quan_li_HSHC.xlsx";
		}
		obj.setPage(1L);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
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
		OutputStream outFile = new FileOutputStream(
				udir.getAbsolutePath() + File.separator + fileName);
		List provinceListId = PermissionUtils.getListIdInDomainData(Constant.OperationKey.VIEW,
				Constant.AdResourceKey.DATA, request);
		List<ConstructionTaskDetailDTO> data = new ArrayList<ConstructionTaskDetailDTO>();
		if (provinceListId != null && !provinceListId.isEmpty())
			data = constructionTaskDAO.doSearchForConsManager(obj, provinceListId);
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (data != null && !data.isEmpty()) {
			Date dateNow = new Date();
			XSSFCellStyle style = ExcelUtils.styleText(sheet);

			// HuyPQ-17/08/2018-start
			XSSFCellStyle styleNumber = ExcelUtils.styleNumber(sheet);
			styleNumber.setDataFormat(workbook.createDataFormat().getFormat("#,##0.000"));

			XSSFCellStyle styleDate = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleDate.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
			// HuyPQ-20/08/2018-end

			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			Row rowS12 = sheet.createRow(4);
			Cell cellS12 = rowS12.createCell(0, CellType.STRING);
			cellS12.setCellValue("Ngày lập báo cáo:  " + (DateTimeUtils.convertDateToString(dateNow, "dd/MM/yyyy")));
			cellS12.setCellStyle(styleDate);
			int i = 7;
			for (ConstructionTaskDetailDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 7));
				cell.setCellStyle(styleDate);
				// HuyPQ-21/08/2018-edit-start
				cell = row.createCell(1, CellType.STRING);
				Date dateComplete = dateFormat.parse(dto.getDateComplete());
				cell.setCellValue((dateComplete != null) ? dateComplete : null);
				cell.setCellStyle(styleDate);
				// HuyPQ-21/08/2018-edit-end
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getSysGroupName() != null) ? dto.getSysGroupName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(3, CellType.STRING);// tỉnh
				cell.setCellValue((dto.getCatProvinceCode() != null) ? dto.getCatProvinceCode() : "");
				cell.setCellStyle(style);
//				hoanm1_20181215_start
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getCatStationHouseCode() != null) ? dto.getCatStationHouseCode() : "");
				cell.setCellStyle(style);
//				hoanm1_20181215_end
				cell = row.createCell(5, CellType.STRING);// trạm
				cell.setCellValue((dto.getCatStationCode() != null) ? dto.getCatStationCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getConstructionCode() != null) ? dto.getConstructionCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue((dto.getCntContract() != null) ? dto.getCntContract() : "");
				cell.setCellStyle(style);
				// HuyPQ-17/08/2018-start
//				hoanm1_20181215_start
				cell = row.createCell(8, CellType.NUMERIC);// number
				Float completeValuePlan = dto.getCompleteValuePlan() != null ? Float.parseFloat(dto.getCompleteValuePlan()) : 0;
				cell.setCellValue((completeValuePlan != null) ? completeValuePlan : 0);
				cell.setCellStyle(styleNumber);
//				hoanm1_20181215_end
				cell = row.createCell(9, CellType.NUMERIC);// number
				Float completeValue = dto.getCompleteValue() != null ? Float.parseFloat(dto.getCompleteValue()) : 0;
				cell.setCellValue((completeValue != null) ? completeValue  : 0);
				cell.setCellStyle(styleNumber);

				// HuyPQ-17/08/2018-end
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue((dto.getStatus() != null) ? getStringForStatusConstr(dto.getStatus()) : "");
				cell.setCellStyle(style);
				cell = row.createCell(11, CellType.STRING);// workitem
				cell.setCellValue((dto.getWorkItemCode() != null) ? dto.getWorkItemCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(12, CellType.STRING);// ngày nhận
				String receiveDate = "";
				SimpleDateFormat fullDateFormat = new SimpleDateFormat("dd/MM/yyyy");
				if (dto.getReceiveRecordsDate() != null)
					receiveDate = fullDateFormat.format(dto.getReceiveRecordsDate());
				cell.setCellValue(dto.getReceiveRecordsDate() != null ? fullDateFormat.parse(receiveDate) : null);
				cell.setCellStyle(styleDate);
				// thiếu quantity
//				hoanm1_20181203_start
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue((dto.getApproceCompleteDescription() != null) ? dto.getApproceCompleteDescription() : "");
				cell.setCellStyle(style);
//				hoanm1_20181219_start
				cell = row.createCell(14, CellType.STRING);
				cell.setCellValue((dto.getDateCompleteTC() != null) ? dto.getDateCompleteTC() : "");
				cell.setCellStyle(style);
//				hoanm1_20181219_end
//				hoanm1_20191023_start
				if("1".equals(obj.getType())){
					cell = row.createCell(15, CellType.STRING);
					cell.setCellValue((dto.getImportCompleteHSHC() != null) ? dto.getImportCompleteHSHC() : "");
					cell.setCellStyle(style);
				}
//				hoanm1_20191023_end
			}
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + fileName);
		return path;
	}

	public String exportContructionDT(ConstructionTaskDetailDTO obj, HttpServletRequest request) throws Exception {
		/**Hoangnh start 20022019**/
		String fileName = "";
		if(obj.getType() != null){
			if("1".equals(obj.getType())){
				fileName = "Bao_cao_quan_ly_DT_ngoaiOS.xlsx";
			}
		} else {
			fileName = "Bao_cao_quan_ly_DT.xlsx";
		}
		/**Hoangnh start 20022019**/
		obj.setPage(1L);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
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
		OutputStream outFile = new FileOutputStream(
				udir.getAbsolutePath() + File.separator + fileName);
		List provinceListId = PermissionUtils.getListIdInDomainData(Constant.OperationKey.VIEW,
				Constant.AdResourceKey.DATA, request);
		List<ConstructionTaskDetailDTO> data = new ArrayList<ConstructionTaskDetailDTO>();
		List<AppParamDTO> lstSourceWork = constructionTaskDAO.getAppParamByParType("SOURCE_WORK");
		HashMap<String, String> mapSoure = new HashMap<>();
		for(AppParamDTO app : lstSourceWork) {
			mapSoure.put(app.getCode(), app.getName());
		}
		List<AppParamDTO> lstConstructionType = constructionTaskDAO.getAppParamByParType("CONSTRUCTION_TYPE");
		HashMap<String, String> mapConsType = new HashMap<>();
		for(AppParamDTO app : lstConstructionType) {
			mapConsType.put(app.getCode(), app.getName());
		}
		if (provinceListId != null && !provinceListId.isEmpty())
			data = constructionTaskDAO.doSearchForRevenue(obj, provinceListId);
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (data != null && !data.isEmpty()) {
			Date dateNow = new Date();
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			// HuyPQ-17/08/2018-start
			XSSFCellStyle styleNumber = ExcelUtils.styleNumber(sheet);
			styleNumber.setDataFormat(workbook.createDataFormat().getFormat("#,##0.000"));
			// HuyPQ-17/08/2018-end
			// HuyPQ-start
			XSSFCellStyle styleDate = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleDate.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
			// HuyPQ-end
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			Row rowS12 = sheet.createRow(4);
			Cell cellS12 = rowS12.createCell(0, CellType.STRING);
			cellS12.setCellValue("Ngày lập báo cáo:  " + (DateTimeUtils.convertDateToString(dateNow, "dd/MM/yyyy")));
			cellS12.setCellStyle(styleDate);
			int i = 7;
			for (ConstructionTaskDetailDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 7));
				cell.setCellStyle(styleNumber);
				// HuyPQ-22/08/2018-start
				cell = row.createCell(1, CellType.STRING);
				Date dateComplete = dateFormat.parse(dto.getDateComplete());
				cell.setCellValue((dateComplete != null) ? dateComplete : null);
				cell.setCellStyle(styleDate);
				// HuyPQ-end
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getSysGroupName() != null) ? dto.getSysGroupName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getPerformerName() != null) ? dto.getPerformerName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getCatStationCode() != null) ? dto.getCatStationCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getConstructionCode() != null) ? dto.getConstructionCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getCntContract() != null) ? dto.getCntContract() : "");
				cell.setCellStyle(style);
				// HuyPQ-17/08/2018-edit-start
				cell = row.createCell(7, CellType.STRING);
				if (dto.getCompleteValue() != null) {
					Float completeValue = Float.parseFloat(dto.getCompleteValue());
					cell.setCellValue(completeValue);
					cell.setCellStyle(styleNumber);
				} else {
					Float completeValue = (float) 0;
					cell.setCellValue(completeValue);
					cell.setCellStyle(styleNumber);
				}

				cell = row.createCell(8, CellType.NUMERIC);
//				Float consAppRevenueState = Float.parseFloat(dto.getConsAppRevenueState());
				if(dto.getConsAppRevenueValueDB()==null || dto.getConsAppRevenueValueDB()==0d) {
					cell.setCellValue(dto.getConsAppRevenueValue());
				} else {
					cell.setCellValue(dto.getConsAppRevenueValueDB());
				}
//				cell.setCellValue(
//						(consAppRevenueState == 1 || consAppRevenueState == null) ? dto.getConsAppRevenueValue()
//								: dto.getConsAppRevenueValueDB());
				cell.setCellStyle(styleNumber);
				// HuyPQ-17/08/2018-edit-end
				
				cell = row.createCell(9, CellType.STRING);
				if(dto.getSourceWork() != null) {
					cell.setCellValue(mapSoure.get(dto.getSourceWork()));
				} else {
					cell.setCellValue("");
				}
				cell.setCellStyle(style);
				cell = row.createCell(10, CellType.STRING);
				if(dto.getConstructionTypeNew() != null) {
					cell.setCellValue(mapConsType.get(dto.getConstructionTypeNew()));
				} else {
					cell.setCellValue("");
				}
				cell.setCellStyle(style);
				
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue((dto.getStatus() != null) ? getStringForStatusConstrV2(dto.getStatus()) : "");
				cell.setCellStyle(style);
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue(
						(dto.getConsAppRevenueState() != null) ? getStringForStateConstr(dto.getConsAppRevenueState())
								: "");
				cell.setCellStyle(style);
				// thiếu quantity
				if(obj.getType()!=null && "1".equals(obj.getType())){
					cell = row.createCell(13, CellType.STRING);
					cell.setCellValue((dto.getImportCompleteHSHC() != null) ? dto.getImportCompleteHSHC() : "");
					cell.setCellStyle(style);
					cell = row.createCell(14, CellType.STRING);
					cell.setCellValue((dto.getApproveRevenueDescription() != null) ? dto.getApproveRevenueDescription() : "");
					cell.setCellStyle(style);
				}
				

			}
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + fileName);
		return path;

	}

	@Override
	public int updateCompletePercent(ConstructionTaskGranttDTO dto, KttsUserSession objUser) {
		// if(inValidStartDate(dto))
		// return -1;
		// if(inValidEndDate(dto))
		// return -2;
//		if (dto.getType().equals("1")){
//			return -1;
//		}
		constructionTaskDAO.updateCompletePercent(dto, objUser);
		Long parentId = dto.getParentID();
		while (parentId != null) {
			constructionTaskDAO.updateTaskParent(parentId);
			parentId = constructionTaskDAO.getParentId(parentId);
		}
		return constructionTaskDAO.updateCompletePercent(dto, objUser);

	}

	private boolean inValidEndDate(ConstructionTaskGranttDTO dto) {
		// TODO Auto-generated method stub
		return constructionTaskDAO.inValidEndDate(dto);
	}

	private boolean inValidStartDate(ConstructionTaskGranttDTO dto) {
		// TODO Auto-generated method stub
		return constructionTaskDAO.inValidStartDate(dto);
	}

	@Override
	public int deleteGrantt(ConstructionTaskGranttDTO dto) {
		return constructionTaskDAO.deleteGrantt(dto);
	}

	@Override
	public int createTask(ConstructionTaskGranttDTO dto) {
		return constructionTaskDAO.createTask(dto);
	}

	public String exportDetailMonthPlanTab1(ConstructionTaskDetailDTO obj) throws Exception {

		obj.setPage(1L);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Export_thicong_thang_chitiet.xlsx"));
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
				udir.getAbsolutePath() + File.separator + "Export_thicong_thang_chitiet.xlsx");

		List<ConstructionTaskDetailDTO> data = constructionTaskDAO.doSearch(obj);
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			// HuyPQ-17/08/2018-edit-start
			XSSFCellStyle styleNumber = ExcelUtils.styleNumber(sheet);
			styleNumber.setDataFormat(workbook.createDataFormat().getFormat("#,##0.000"));
			// HuyPQ-17/08/2018-edit-end
			// HuyPQ-22/08/2018-edit-start
			XSSFCellStyle styleDate = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleDate.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			// HuyPQ-end
			XSSFCellStyle styleCurrency = ExcelUtils.styleCurrency(sheet);
			int i = 2;
			for (ConstructionTaskDetailDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 2));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getCatProvinceCode() != null) ? dto.getCatProvinceCode() : " ");
				cell.setCellStyle(style);
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getCatStationCode() != null) ? dto.getCatStationCode() : " ");
				cell.setCellStyle(style);
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getConstructionCode() != null) ? dto.getConstructionCode() : " ");
				cell.setCellStyle(style);
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getWorkItemName() != null) ? dto.getWorkItemName() : " ");
				cell.setCellStyle(style);
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getCntContract() != null) ? dto.getCntContract() : " ");
				cell.setCellStyle(style);
				cell = row.createCell(6, CellType.NUMERIC);
				cell.setCellValue((dto.getQuantity() != null) ? dto.getQuantity() : 0);
				cell.setCellStyle(styleNumber);
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue(("1".equals(dto.getSourceType())) ? "x" : " ");
				cell.setCellStyle(style);
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue(("2".equals(dto.getSourceType())) ? "x" : " ");
				cell.setCellStyle(style);
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue(("1".equals(dto.getDeployType())) ? "x" : " ");
				cell.setCellStyle(style);
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue(("2".equals(dto.getDeployType())) ? "x" : " ");
				cell.setCellStyle(style);
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue((dto.getSupervisorName() != null) ? dto.getSupervisorName() : " ");
				cell.setCellStyle(style);
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue((dto.getDirectorName() != null) ? dto.getDirectorName() : " ");
				cell.setCellStyle(style);
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue((dto.getPerformerName() != null) ? dto.getPerformerName() : " ");
				cell.setCellStyle(style);
				cell = row.createCell(14, CellType.STRING);
				cell.setCellValue((dto.getStartDate() != null) ? dto.getStartDate() : null);
				cell.setCellStyle(styleDate);
				cell = row.createCell(15, CellType.STRING);
				cell.setCellValue((dto.getEndDate() != null) ? dto.getEndDate() : null);
				cell.setCellStyle(styleDate);
				cell = row.createCell(16, CellType.STRING);
				cell.setCellValue((dto.getDescription() != null) ? dto.getDescription() : " ");
				cell.setCellStyle(style);

				// thiếu quantity

			}
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt
				.encryptFileUploadPath(uploadPathReturn + File.separator + "Export_thicong_thang_chitiet.xlsx");
		return path;
	}

	public String exportDetailMonthPlanTab2(ConstructionTaskDetailDTO obj) throws Exception {

		obj.setPage(1L);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Export_HSHC_thang_chitiet.xlsx"));
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
				udir.getAbsolutePath() + File.separator + "Export_HSHC_thang_chitiet.xlsx");

		List<ConstructionTaskDetailDTO> data = constructionTaskDAO.doSearch(obj);
		// DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			// HuyPQ-17/08/2018-edit-start
			XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
			styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0.000"));
			// HuyPQ-17/08/2018-edit-end
			// HuyPQ-22/08/2018-edit-start
			XSSFCellStyle styleDate = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleDate.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			// HuyPQ-end
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			int i = 2;
			for (ConstructionTaskDetailDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 2));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getCatStationCode() != null) ? dto.getCatStationCode() : " ");
				cell.setCellStyle(style);
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getConstructionCode() != null) ? dto.getConstructionCode() : " ");
				cell.setCellStyle(style);
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getConstructionType() != null) ? dto.getConstructionType() : " ");
				cell.setCellStyle(style);
				cell = row.createCell(4, CellType.STRING);
//				hoanm1_20181229_start
//				cell.setCellValue(("1".equals(dto.getIsObstructed())) ? "Có" : "Không");
				cell.setCellValue((dto.getWorkItemNameHSHC() != null) ? dto.getWorkItemNameHSHC() : " ");
//				hoanm1_20181229_end
				cell.setCellStyle(style);
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getCntContract() != null) ? dto.getCntContract() : " ");
				cell.setCellStyle(style);
				cell = row.createCell(6, CellType.NUMERIC);
				cell.setCellValue((dto.getQuantity() != null) ? dto.getQuantity() : 0);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue((dto.getCompleteDate() != null) ? dto.getCompleteDate() : null);
				cell.setCellStyle(styleDate);
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue((dto.getSupervisorName() != null) ? dto.getSupervisorName() : " ");
				cell.setCellStyle(style);
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue((dto.getDirectorName() != null) ? dto.getDirectorName() : " ");
				cell.setCellStyle(style);
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue((dto.getPerformerName() != null) ? dto.getPerformerName() : " ");
				cell.setCellStyle(style);
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue((dto.getStartDate() != null) ? dto.getStartDate() : null);
				cell.setCellStyle(styleDate);
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue((dto.getEndDate() != null) ? dto.getEndDate() : null);
				cell.setCellStyle(styleDate);
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue((dto.getDescription() != null) ? dto.getDescription() : " ");
				cell.setCellStyle(style);

				// thiếu quantity

			}
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt
				.encryptFileUploadPath(uploadPathReturn + File.separator + "Export_HSHC_thang_chitiet.xlsx");
		return path;
	}

	public String exportDetailMonthPlanTab3(ConstructionTaskDetailDTO obj) throws Exception {

		obj.setPage(1L);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(
				new FileInputStream(filePath + "Export_lendoanhthu_thang_chitiet.xlsx"));
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
				udir.getAbsolutePath() + File.separator + "Export_lendoanhthu_thang_chitiet.xlsx");

		List<ConstructionTaskDetailDTO> data = constructionTaskDAO.doSearch(obj);
		// DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			// HuyPQ-17/08/2018-edit-start
			XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
			styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0.000"));
			// HuyPQ-17/08/2018-edit-end
			// HuyPQ-22/08/2018-edit-start
			XSSFCellStyle styleDate = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleDate.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			// HuyPQ-end
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			int i = 2;
			for (ConstructionTaskDetailDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 2));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getCatProvinceCode() != null) ? dto.getCatProvinceCode() : " ");
				cell.setCellStyle(style);
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getCatStationCode() != null) ? dto.getCatStationCode() : " ");
				cell.setCellStyle(style);
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getConstructionCode() != null) ? dto.getConstructionCode() : " ");
				cell.setCellStyle(style);
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getConstructionType() != null) ? dto.getConstructionType() : " ");
				cell.setCellStyle(style);
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getCntContract() != null) ? dto.getCntContract() : " ");
				cell.setCellStyle(style);
				cell = row.createCell(6, CellType.NUMERIC);
				cell.setCellValue((dto.getQuantity() != null) ? dto.getQuantity() : 0);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue((dto.getApproveCompleteDate() != null) ? dto.getApproveCompleteDate() : null);
				cell.setCellStyle(styleDate);
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue((dto.getPerformerName() != null) ? dto.getPerformerName() : " ");
				cell.setCellStyle(style);
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue((dto.getStartDate() != null) ? dto.getStartDate() : null);
				cell.setCellStyle(styleDate);
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue((dto.getEndDate() != null) ? dto.getEndDate() : null);
				cell.setCellStyle(styleDate);
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue((dto.getDescription() != null) ? dto.getDescription() : " ");
				cell.setCellStyle(style);

				// thiếu quantity

			}
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt
				.encryptFileUploadPath(uploadPathReturn + File.separator + "Export_lendoanhthu_thang_chitiet.xlsx");
		return path;
	}

	public String exportDetailMonthPlanTab5(ConstructionTaskDetailDTO obj) throws Exception {

		obj.setPage(1L);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(
				new FileInputStream(filePath + "Export_dongtien_thang_chitiet.xlsx"));
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
				udir.getAbsolutePath() + File.separator + "Export_dongtien_thang_chitiet.xlsx");

		List<ConstructionTaskDetailDTO> data = constructionTaskDAO.doSearchForDT(obj);
		// DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			// HuyPQ-17/08/2018-edit-start
			XSSFCellStyle styleCurrency = ExcelUtils.styleNumber(sheet);
			styleCurrency.setDataFormat(workbook.createDataFormat().getFormat("#,##0.000"));
			// HuyPQ-17/08/2018-edit-end
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			int i = 1;
			for (ConstructionTaskDetailDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 1));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellStyle(style);
				cell.setCellValue((dto.getCatProvinceCode() != null) ? dto.getCatProvinceCode() : " ");
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getCatStationCode() != null) ? dto.getCatStationCode() : " ");
				cell.setCellStyle(style);
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getConstructionCode() != null) ? dto.getConstructionCode() : " ");
				cell.setCellStyle(style);
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getConstructionType() != null) ? dto.getConstructionType() : " ");
				cell.setCellStyle(style);
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getWorkItemName() != null) ? dto.getWorkItemName() : " ");
				cell.setCellStyle(style);
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getCntContract() != null) ? dto.getCntContract() : " ");
				cell.setCellStyle(style);
				cell = row.createCell(7, CellType.NUMERIC);
				cell.setCellValue((dto.getQuantity() != null) ? dto.getQuantity() : 0);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(8, CellType.NUMERIC);
				cell.setCellValue((dto.getVat() != null) ? dto.getVat() : 0);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(9, CellType.NUMERIC);
				cell.setCellValue(
						(dto.getVat() != null && dto.getQuantity() != null) ? getTotal(dto.getQuantity(), dto.getVat())
								: 0);
				cell.setCellStyle(styleCurrency);
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue((dto.getDescription() != null) ? dto.getDescription() : " ");
				cell.setCellStyle(style);

				// thiếu quantity

			}
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt
				.encryptFileUploadPath(uploadPathReturn + File.separator + "Export_dongtien_thang_chitiet.xlsx");
		return path;
	}

	private Double getTotal(Double quantity, Double vat) {
		// TODO Auto-generated method stub
		if (vat > 0) {
			return quantity + (quantity * (vat / 100));

		}
		return null;
	}

	public String exportDetailMonthPlanTab6(ConstructionTaskDetailDTO obj) throws Exception {

		obj.setPage(1L);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(
				new FileInputStream(filePath + "Export_congvieckhac_thang_chitiet.xlsx"));
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
				udir.getAbsolutePath() + File.separator + "Export_congvieckhac_thang_chitiet.xlsx");

		List<ConstructionTaskDetailDTO> data = constructionTaskDAO.doSearch(obj);
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			// DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			// HuyPQ-22/08/2018-edit-start
			XSSFCellStyle styleDate = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleDate.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			// HuyPQ-end
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			int i = 2;
			for (ConstructionTaskDetailDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 2));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getTaskName() != null) ? dto.getTaskName() : " ");
				cell.setCellStyle(style);
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getPerformerName() != null) ? dto.getPerformerName() : " ");
				cell.setCellStyle(style);
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getStartDate() != null) ? dto.getStartDate() : null);
				cell.setCellStyle(styleDate);
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getEndDate() != null) ? dto.getEndDate() : null);
				cell.setCellStyle(styleDate);
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getDescription() != null) ? dto.getDescription() : " ");
				cell.setCellStyle(style);

				// thiếu quantity

			}
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt
				.encryptFileUploadPath(uploadPathReturn + File.separator + "Export_congvieckhac_thang_chitiet.xlsx");
		return path;
	}

	private String numberFormat(double value) {
		DecimalFormat myFormatter = new DecimalFormat("###.###");
		String output = myFormatter.format(value);
		return output;
	}

	private String getStringForStatus(String status) {
		// TODO Auto-generated method stub
		if ("0".equals(status)) {
			return "Hết hiệu lực";
		} else if ("1".equals(status)) {
			return "Hiệu lực";
		}
		return null;
	}

	private String getStringForSignState(String signState) {
		// TODO Auto-generated method stub
		if ("1".equals(signState)) {
			return "Chưa trình ký";
		} else if ("2".equals(signState)) {
			return "Đã trình ký";
		} else if ("3".equals(signState)) {
			return "Đã ký duyệt";
		}
		return null;
	}
//hoanm1_20181215_start
	private String getStringForStatusConstr(String status) {
		// TODO Auto-generated method stub
		if ("0".equals(status)) {
			return "Đã hủy";
		} else if ("1".equals(status)) {
			return "Chờ phê duyệt";
		} else if ("2".equals(status)) {
			return "Đã phê duyệt";
		} else if ("3".equals(status)) {
			return "Từ chối";
		} 
		return null;
	}
//	hoanm1_20181215_end
	
	// hoanm1_20181215_start
	private String getStringForStatusConstrV2(String status) {
		// TODO Auto-generated method stub
		if ("4".equals(status)) {
			return "Đã tạm dừng";
		} else if ("5".equals(status)) {
			return "Đã hoàn thành";
		} else if ("6".equals(status)) {
			return "Đã nghiệm thu";
		} else if ("7".equals(status)) {
			return "Đã hoàn công";
		} else if ("8".equals(status)) {
			return "Đã quyết toán";
		}else {
			return "Đang thực hiện";
		}
	}

	// hoanm1_20181215_end
	private String getStringForStateConstr(String status) {
		// TODO Auto-generated method stub
		if ("1".equals(status)) {
			return "Chưa phê duyệt";
		} else if ("2".equals(status)) {
			return "Đã phê duyệt";
		} else if ("3".equals(status)) {
			return "Đã từ chối";
		}
		return null;
	}

	@Override
	public List<ConstructionTaskGranttDTO> getDataConstructionGrantt(GranttDTO granttSearch) {
		List<ConstructionTaskGranttDTO> lstRs = constructionTaskDAO.getDataConstructionGrantt(granttSearch);

		Long sysGroupIndex = 0L;
		Long constructionIndex = 0L;
		Long workItemIndex = 0L;
		Long taskIndex = 0L;
		for (ConstructionTaskGranttDTO dto : lstRs) {
			if (dto.getLevelId() == 1L) {
				constructionIndex = 0L;
				dto.setOrderID(sysGroupIndex);
				sysGroupIndex++;
			} else if (dto.getLevelId() == 2L) {
				workItemIndex = 0L;
				dto.setParentID(null);
				dto.setOrderID(constructionIndex);
				constructionIndex++;
			} else if (dto.getLevelId() == 3L) {
				taskIndex = 0L;
				dto.setOrderID(workItemIndex);
				workItemIndex++;
			} else {
				dto.setOrderID(taskIndex);
				dto.setSummary(false);
				taskIndex++;
			}
		}

		return lstRs;
	}

	// Luu danh sach anh gui ve va lay ra duong dan
	public List<ConstructionImageInfo> saveConstructionImages(List<ConstructionImageInfo> lstConstructionImages) {
		List<ConstructionImageInfo> result = new ArrayList<>();
		for (ConstructionImageInfo constructionImage : lstConstructionImages) {
			if (constructionImage.getStatus() == 0) {
				ConstructionImageInfo obj = new ConstructionImageInfo();
				obj.setImageName(constructionImage.getImageName());
				obj.setLatitude(constructionImage.getLatitude());
				obj.setLongtitude(constructionImage.getLongtitude());
				InputStream inputStream = ImageUtil.convertBase64ToInputStream(constructionImage.getBase64String());
				try {
					String imagePath = UFile.writeToFileServerATTT2(inputStream, constructionImage.getImageName(),
							input_image_sub_folder_upload, folder2Upload);

					obj.setImagePath(imagePath);
				} catch (Exception e) {
					// return result;
					// throw new BusinessException("Loi khi save file", e);
					continue;
				}
				result.add(obj);
			}

			if (constructionImage.getStatus() == -1 && constructionImage.getImagePath() != "") {
				// ImageUtil.deleteFile(constructionImage.getImagePath());
				utilAttrachDocumentDao.updateUtilAttachDocumentById(constructionImage.getUtilAttachDocumentId());
			}
		}

		return result;
	}

	// Service lay danh sach anh
	public List<ConstructionImageInfo> getImageByConstructionTaskId(Long constructionTaskId) {
		List<ConstructionImageInfo> listImageResponse = new ArrayList<>();
		// lay danh sach constructionImageInfo theo constructionTaskId truyen vao
		List<ConstructionImageInfo> listImage = utilAttrachDocumentDao.getListImageByConstructionId(constructionTaskId);
		// lay list anh dua vao listImage Lay duoc
		listImageResponse = getConstructionImages(listImage);

		return listImageResponse;
	}

	// kepv_20181010_start
	/**
	 * PKE 03-10-2018 Lấy danh sách ảnh theo thông tin công trình kèm theo
	 * 
	 * @param constructionId
	 * @param type
	 * @return
	 */
	public List<ConstructionImageInfo> getImageByConstructionId_Type(Long constructionId, String type) {
		List<ConstructionImageInfo> listImageResponse = new ArrayList<>();
		//
		List<ConstructionImageInfo> listImage = utilAttrachDocumentDao.getListImageByConstructionId_Type(constructionId,
				type);
		// lay list anh dua vao listImage Lay duoc
		listImageResponse = getConstructionImages(listImage);

		return listImageResponse;
	}
	// kepv_20181010_end

	// lay danh sach ten anh va anh dinh dang base64
	public List<ConstructionImageInfo> getConstructionImages(List<ConstructionImageInfo> lstConstructionImages) {
		List<ConstructionImageInfo> result = new ArrayList<>();

		for (ConstructionImageInfo constructionImage : lstConstructionImages) {
			try {
				String fullPath = folder2Upload + File.separator + constructionImage.getImagePath();
				String base64Image = ImageUtil.convertImageToBase64(fullPath);
				ConstructionImageInfo obj = new ConstructionImageInfo();
				obj.setImageName(constructionImage.getImageName());
				obj.setBase64String(base64Image);
				obj.setImagePath(fullPath);
				obj.setStatus('1');
				obj.setUtilAttachDocumentId(constructionImage.getUtilAttachDocumentId());
				result.add(obj);
			} catch (Exception e) {
				continue;
			}

		}

		return result;
	}

	public boolean saveConstructionInfo(List<ConstructionInfoDTO> lstConstructionInfo) {
		boolean result = true;

		for (ConstructionInfoDTO item : lstConstructionInfo) {
			InputStream inputStream = ImageUtil.convertBase64ToInputStream(item.getBase64Image());

			String filePath = null;

			try {
				System.out.println("defaultSubFolderUpload");
				System.out.println(defaultSubFolderUpload);
				System.out.println("folder2Upload");
				System.out.println(folder2Upload);
				filePath = UFile.writeToFileServerATTT2(inputStream, item.getFileName(), defaultSubFolderUpload,
						folder2Upload);
				System.out.println("filePath");
				System.out.println(filePath);
			} catch (Exception e) {
				result = false;
				// throw new BusinessException("Loi khi save file", e);
			}

			item.setFilePath(filePath);

			// TODO: lưu thông tin công trình vào DB
			// Các thông tin lấy trong item
		}

		return result;
	}

	public Long checkPermissions(ConstructionDetailDTO obj, Long sysGroupId, HttpServletRequest request) {

		if (!VpsPermissionChecker.hasPermission(Constant.OperationKey.UNDO, Constant.AdResourceKey.CONSTRUCTION_PRICE,
				request)) {
			throw new IllegalArgumentException("Bạn không có quyền hủy hoàn thành");
		}
		if (!VpsPermissionChecker.checkPermissionOnDomainData(Constant.OperationKey.UNDO,
				Constant.AdResourceKey.CONSTRUCTION_PRICE, obj.getCatProvinceId(), request)) {
			throw new IllegalArgumentException("Bạn không có quyền hủy hoàn thành cho đơn vị này");
		}
		try {
			return 0l;
		} catch (Exception e) {
			return 1l;
		}
	}
	//Tungtt_20181205_ start
	public Long checkPermissionsUndo(ConstructionDetailDTO obj, Long sysGroupId, HttpServletRequest request) {

		if (!VpsPermissionChecker.hasPermission(Constant.OperationKey.UNDO, Constant.AdResourceKey.CONSTRUCTION_PRICE,
				request)) {
			throw new IllegalArgumentException("Bạn không có quyền hủy hoàn thành");
		}
		if (!VpsPermissionChecker.checkPermissionOnDomainData(Constant.OperationKey.UNDO,
				Constant.AdResourceKey.CONSTRUCTION_PRICE, obj.getCatProvinceId(), request)) {
			throw new IllegalArgumentException("Bạn không có quyền hủy hoàn thành cho đơn vị này");
		}
		try {
			return 0l;
		} catch (Exception e) {
			return 1l;
		}
	}
	//Tungtt_20181205_ end
	
	public Long checkPermissionsApproved(ConstructionDetailDTO obj, Long sysGroupId, HttpServletRequest request) {

		if (!VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVED,
				Constant.AdResourceKey.CONSTRUCTION_PRICE, request)) {
			throw new IllegalArgumentException("Bạn không có quyền xác nhận");
		}
		if (!VpsPermissionChecker.checkPermissionOnDomainData(Constant.OperationKey.APPROVED,
				Constant.AdResourceKey.CONSTRUCTION_PRICE, obj.getCatProvinceId(), request)) {
			throw new IllegalArgumentException("Bạn không có quyền xác nhận cho cho đơn vị này");
		}
		try {
			return 0l;
		} catch (Exception e) {
			return 1l;
		}
	}
   //TungTT_2018_3011 start 
	public Long checkPermissionsApprovedHSHC(ConstructionDetailDTO obj, Long sysGroupId, HttpServletRequest request) {

		if (!VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVED,
				Constant.AdResourceKey.REVENUE_SALARY, request)) {
			throw new IllegalArgumentException("Bạn không có quyền xác nhận");
		}
		if (!VpsPermissionChecker.checkPermissionOnDomainData(Constant.OperationKey.APPROVED,
				Constant.AdResourceKey.REVENUE_SALARY, obj.getCatProvinceId(), request)) {
			throw new IllegalArgumentException("Bạn không có quyền xác nhận cho cho đơn vị này");
		}
		try {
			return 0l;
		} catch (Exception e) {
			return 1l;
		}
	}
	//TungTT_2018_3011 end
	
	public Long removeHSHC(ConstructionDetailDTO obj, HttpServletRequest request) {
		try {
			constructionTaskDAO.removeHSHC(obj);

			return 1l;
		} catch (Exception e) {
			return 0l;
		}
	}

	public Long removeComplete(List<String> listId, HttpServletRequest request,Long sysUserId) {
		if (!VpsPermissionChecker.hasPermission(Constant.OperationKey.UNDO, Constant.AdResourceKey.CONSTRUCTION_PRICE,
				request)) {
			throw new IllegalArgumentException("Bạn không có quyền hủy hoàn thành");
		}

		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.UNDO,
				Constant.AdResourceKey.CONSTRUCTION_PRICE, request);

		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
		Long groupIdList1 = Long.parseLong(groupIdList.get(0));

		if (!VpsPermissionChecker.checkPermissionOnDomainData(Constant.OperationKey.UNDO,
				Constant.AdResourceKey.CONSTRUCTION_PRICE, groupIdList1, request)) {
			throw new IllegalArgumentException("Bạn không có quyền hủy xác nhận cho đơn vị này");
		}

		try {
//			constructionTaskDAO.removeCompleteByListId(listId);

			return 1l;
		} catch (Exception e) {
			return 0l;
		}

	}

	public Long approveRevenue(List<ConstructionTaskDetailDTO> obj, Long sysUserId, HttpServletRequest request) {
		if (!VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVED,
				Constant.AdResourceKey.CONSTRUCTION_PRICE, request)) {
			throw new IllegalArgumentException("Bạn không có quyền phê duyệt");
		}

		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.APPROVED,
				Constant.AdResourceKey.CONSTRUCTION_PRICE, request);
		//
		// List<String> groupIdList = ConvertData
		// .convertStringToList(groupId, ",");
		// Long groupIdList1 = Long.parseLong(groupIdList.get(0));

		if (!VpsPermissionChecker.checkPermissionOnDomainData(Constant.OperationKey.APPROVED,
				Constant.AdResourceKey.CONSTRUCTION_PRICE, obj.get(0).getCatProvinceId(), request)) {
			throw new IllegalArgumentException("Bạn không có quyền phê duyệt cho đơn vị này");
		}
		try {
			for (ConstructionTaskDetailDTO dto : obj) {
				constructionTaskDAO.approveRevenue(dto, sysUserId);
//				hoanm1_20200707_start
				constructionTaskDAO.updateRpRevenue(dto);
//				hoanm1_20200707_end
			}

			return 1l;
		} catch (Exception e) {
			return 0l;
		}
	}
	


	//TungTT_20181129_Strat
	public Long approveHSHC(ConstructionDetailDTO obj, HttpServletRequest request) {
		
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
			 
			 constructionTaskDAO.approveRp_HSHC(obj);
			 return constructionTaskDAO.approveHSHCConstruction(obj);
			 
	}
	
	
    public Long UpdateUndoHshc(ConstructionDetailDTO obj, HttpServletRequest request) {
		
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
			 
			 constructionTaskDAO.updateUndoHSHC(obj);
			 constructionTaskDAO.updateUndoHSHCConstruction(obj);
			 return 1l;
			 
	}
	//TungTT_20181129_end 
	
	public Long callbackConstrRevenue(ConstructionDetailDTO obj, HttpServletRequest request) {
		// if (!VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVED,
		// Constant.AdResourceKey.CONSTRUCTION_PRICE, request)) {
		// throw new IllegalArgumentException(
		// "Bạn không có quyền undo");
		// }
		// if (!VpsPermissionChecker.checkPermissionOnDomainData(
		// Constant.OperationKey.APPROVED,
		// Constant.AdResourceKey.CONSTRUCTION_PRICE,
		// obj.getCatProvinceId(), request)) {
		// throw new IllegalArgumentException(
		// "Bạn không có quyền undo")
		// ;
		// }
		try {
			KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
			constructionTaskDAO.callbackConstrRevenue(obj, user.getVpsUserInfo().getSysUserId());
//			hoanm1_20200408_start
			constructionTaskDAO.callbackConstrRpRevenue(obj);
//			hoanm1_20200408_end
			return 1l;
		} catch (Exception e) {
			return 0l;
		}
	}

	public Long rejectionRevenue(List<ConstructionTaskDetailDTO> obj, Long deptId, HttpServletRequest request) {

		if (!VpsPermissionChecker.hasPermission(Constant.OperationKey.APPROVED,
				Constant.AdResourceKey.CONSTRUCTION_PRICE, request)) {
			throw new IllegalArgumentException("Bạn không có quyền từ chối");
		}
		try {
			for (ConstructionTaskDetailDTO dto : obj) {
				constructionTaskDAO.rejectionRevenue(dto, deptId);
//				hoanm1_20200707_start
				constructionTaskDAO.rejectRpRevenue(dto);
//				hoanm1_20200707_end
			}

			return 1l;
		} catch (Exception e) {
			return 0l;
		}
	}

	public List<ConstructionDetailDTO> searchConstruction(ConstructionDetailDTO obj, Long sysGroupId,
			HttpServletRequest request) {
		// List<ConstructionDetailDTO> ls = new ArrayList<ConstructionDetailDTO>();
		// String groupId = VpsPermissionChecker.getDomainDataItemIds(
		// Constant.OperationKey.VIEW, Constant.AdResourceKey.DATA,
		// request);
		// List<String> groupIdList = ConvertData
		// .convertStringToList(groupId, ",");
		// if (groupIdList != null && !groupIdList.isEmpty())
		// return constructionDAO.doSearch(obj, groupIdList);
		//
		// return ls;
		List<ConstructionDetailDTO> ls = new ArrayList<ConstructionDetailDTO>();
		// chinhpxn20180630_start
		String groupId;
		if (obj.getTaskType() == 3l) {
			groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.CREATE,
					Constant.AdResourceKey.TASK, request);
		} else {
			groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
					Constant.AdResourceKey.WORK_PROGRESS, request);
		}
		// chinhpxn20180630_end
		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
		//Huypq-20191202-start
		if (groupIdList != null && !groupIdList.isEmpty()) {
			ls = constructionDAO.doSearch(obj, groupIdList);
			for(ConstructionDetailDTO dto : ls) {
				List<WorkItemDetailDTO> lstWork= constructionDAO.getDataWorkItem(dto.getConstructionId());
				dto.setListWorkItemName(lstWork);
			}
		}
		//Huy-end
		return ls;
		// return constructionDAO.doSearch1(obj);
	}

	public DataListDTO rpDailyTaskConstruction(ConstructionDetailDTO obj) {
		List<ConstructionDetailDTO> ls = constructionDAO.rpDailyTaskConstruction(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}

	public List<ConstructionDetailDTO> searchConstructionDSTH(ConstructionDetailDTO obj, Long sysGroupId,
			HttpServletRequest request) {

		List<ConstructionDetailDTO> ls = new ArrayList<ConstructionDetailDTO>();
		String provinceId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.CREATE,
				Constant.AdResourceKey.EQUIPMENT_RETURN, request);
		List<String> provinceListId = ConvertData.convertStringToList(provinceId, ",");
		if (provinceListId != null && !provinceListId.isEmpty())
			ls = constructionDAO.doSearchDSTH(obj, provinceListId);

		return ls;
		// return constructionDAO.doSearch1(obj);
	}

	public List<WorkItemDetailDTO> searchWorkItems(WorkItemDetailDTO obj) {
		return workItemDAO.doSearch(obj);
	}

	public DataListDTO rpDailyTaskWorkItems(WorkItemDetailDTO obj) {
		List<WorkItemDetailDTO> ls = workItemDAO.rpDailyTaskWorkItems(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}

	public List<WorkItemDetailDTO> searchCatTask(WorkItemDetailDTO obj) {
		return constructionDAO.doSearchCatTask(obj);
	}

	public List<SysUserDetailCOMSDTO> searchPerformer(SysUserDetailCOMSDTO obj, Long sysGroupId,
			HttpServletRequest request) {
		List<SysUserDetailCOMSDTO> ls = new ArrayList<SysUserDetailCOMSDTO>();
		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
				Constant.AdResourceKey.WORK_PROGRESS, request);
		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
		if (groupIdList != null && !groupIdList.isEmpty())
			ls = constructionDAO.doSearchPerformer(obj, groupIdList);
		return ls;
	}

	public Long updatePerfomer(ConstructionTaskDTO obj) {
		constructionTaskDAO.updatePerfomer(obj);
		return obj.getConstructionTaskId();
	}

	public DataListDTO exportPdf(CommonDTO dto) {
		ConstructionTaskDetailDTO obj = new ConstructionTaskDetailDTO();
		List<ConstructionTaskDetailDTO> ls = constructionTaskDAO.doSearchForReport(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}

	public Response exportPdfService(String userName) throws Exception {
		if (userName != null) {
			ConstructionTaskDetailDTO obj = new ConstructionTaskDetailDTO();
			obj.setUserName(userName);
			obj.setPage(1L);
			obj.setPageSize(null);
			String sysGroupName = constructionTaskDAO.getDataSysGroupNameByUserName(obj.getUserName());
			List<ConstructionTaskDetailDTO> data = constructionTaskDAO.doSearchForReport(obj);
			// Date dateNow = new Date();
			// ExportPxkDTO data = constructionBusinessImpl.NoSynStockTransDetaill(dto);
			ConcurrentHashMap<String, Object> params = new ConcurrentHashMap<String, Object>();
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			String reportPath = classloader.getResource("../" + "doc-template").getPath();
			String filePath = reportPath + "/YeuCauCoSerial.jasper";
			JRBeanCollectionDataSource tbl2 = new JRBeanCollectionDataSource(data);
			params.put("tbl2", tbl2);
			Format formatter = new SimpleDateFormat("dd");
			String dd = formatter.format(new Date());
			formatter = new SimpleDateFormat("MM");
			String mm = formatter.format(new Date());
			formatter = new SimpleDateFormat("yyyy");
			String yyyy = formatter.format(new Date());
			params.put("mm", mm);
			params.put("dd", dd);
			params.put("year", yyyy);
			if (sysGroupName != null) {
				params.put("sysGroupName", sysGroupName);
			}

			JasperPrint jasperPrint = JasperFillManager.fillReport(filePath, params, new JREmptyDataSource());
			jasperPrint.addStyle(JasperReportConfig.getNormalStyle(reportPath));
			String pathReturn = generateLocationFile();
			File udir = new File(folderUpload + pathReturn);
			if (!udir.exists()) {
				udir.mkdirs();
			}
			File file = new File(folderUpload + pathReturn + File.separator + "YeuCauCoSerial.pdf");
			// JasperExportManager.exportReportToPdfFile(jasperPrint,folderUpload+pathReturn
			// +"KehoachThangChiTiet.pdf");
			JRDocxExporter exporter = new JRDocxExporter();
			exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE, file);
			exporter.exportReport();
			if (file.exists()) {
				ResponseBuilder response = Response.ok(file);
				response.header("Content-Disposition", "attachment; filename=\"" + "YeuCauKhongSerial.pdf" + "\"");
				return response.build();
			}
		}
		return null;
	}

	private String generateLocationFile() {
		Calendar cal = Calendar.getInstance();
		return File.separator + UFile.getSafeFileName(defaultSubFolderUpload) + File.separator + cal.get(Calendar.YEAR)
				+ File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator + cal.get(Calendar.DATE)
				+ File.separator + cal.get(Calendar.MILLISECOND);
	}

	public List<ConstructionTaskDetailDTO> getDataForExport(ConstructionTaskDetailDTO obj) {
		return constructionTaskDAO.doSearchForReport(obj);
	}

	public ConstructionTaskGranttDTO getCountConstruction(GranttDTO obj, Long sysGroupId, HttpServletRequest request) {
		// TODO Auto-generated method stub
		ConstructionTaskGranttDTO lstRs = new ConstructionTaskGranttDTO();
		lstRs.setTaskAll(0L);
		lstRs.setTaskPause(0L);
		lstRs.setTaskSlow(0L);
		lstRs.setTaskUnfulfilled(0L);
		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
				Constant.AdResourceKey.WORK_PROGRESS, request);
		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
		if (groupIdList != null && !groupIdList.isEmpty())
			lstRs = constructionTaskDAO.getCountConstruction(obj, groupIdList);
		return lstRs;
	}

	public ConstructionTaskGranttDTO getCountConstructionForTc(GranttDTO obj) {
		// TODO Auto-generated method stub
		return constructionTaskDAO.getCountConstructionForTc(obj);
	}

	public List<ConstructionStationWorkItemDTO> getNameConstruction(SysUserRequest request) {
		int isManage = constructionDAO.getByAdResourceCon(request, CREATE_TASK);
		if (isManage == 0) {
			List<ConstructionStationWorkItemDTO> lst = constructionDAO
					.getListConstructionByIdSysGroupIdCreate_task(request);
			return lst;
		} else {
			List<DomainDTO> isManage1 = null;
			List<ConstructionStationWorkItemDTO> lst = constructionDAO.getListConstructionByIdSysGroupId(request,
					isManage1);
			return lst;
		}
	}

	public List<AppParamDTO> getAmountSLTN(Long id) {
		List<AppParamDTO> listCode = constructionDAO.getAmountSLTN(id);
		List<AppParamDTO> listNew = new ArrayList<AppParamDTO>();
		if (listCode.size() > 0) {
			for (AppParamDTO dto : listCode) {
				AppParamDTO newObj = new AppParamDTO();
				String name = constructionDAO.getName(dto.getCode());
				newObj.setName(name);
				newObj.setAmount(dto.getAmount());
				newObj.setCode(dto.getCode());
				newObj.setConfirm(dto.getConfirm());
				// hoanm1_20180703_start
				newObj.setConstructionTaskDailyId(dto.getConstructionTaskDailyId());
				// hoanm1_20180703_end
				listNew.add(newObj);
			}
		}
		return listNew;
	}

	public Double getProcess(ConstructionTaskDTOUpdateRequest request) {
		Double total = constructionTaskDailyDAO.getTotal(request);
		if (request.getConstructionTaskDTO().getAmount() != null && request.getConstructionTaskDTO().getAmount() != 0) {
			Double result = (total / (request.getConstructionTaskDTO().getAmount())) * 100;
			Double a = (double) Math.round(result);
			return a;
		} else {
			return 0D;
		}
	}

	public Double getAmountPreview(ConstructionTaskDTOUpdateRequest request) {
		return constructionTaskDailyDAO.getAmountPreview(request.getConstructionTaskDTO().getWorkItemId(),
				request.getConstructionTaskDTO().getCatTaskId());
	}

	public int getListConfirmDaily(ConstructionTaskDTOUpdateRequest request) {
		return constructionTaskDailyDAO.getListConfirmDaily(request.getConstructionTaskDTO().getConstructionTaskId());
	}

	// chinhpxn20180716_start

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

	private ExcelErrorDTO createError(int row, String column, String detail) {
		ExcelErrorDTO err = new ExcelErrorDTO();
		err.setColumnError(column);
		err.setLineError(String.valueOf(row));
		err.setDetailError(detail);
		return err;
	}

	public boolean validateRequiredCell(Row row, List<ExcelErrorDTO> errorList, Long actionType) {
		DataFormatter formatter = new DataFormatter();
		boolean result = true;
		if (actionType == 1) {
			for (int colIndex : validateCol) {
				if (!validateString(formatter.formatCellValue(row.getCell(colIndex)))) {
					ExcelErrorDTO errorDTO = new ExcelErrorDTO();
					errorDTO.setColumnError(colAlias.get(colIndex));
					errorDTO.setLineError(String.valueOf(row.getRowNum() + 1));
					errorDTO.setDetailError(colName.get(colIndex) + " chưa nhập");
					errorList.add(errorDTO);
					result = false;
				}

			}
		} else {
			for (int colIndex : validateColForAddTask) {
				if (!validateString(formatter.formatCellValue(row.getCell(colIndex)))) {
					ExcelErrorDTO errorDTO = new ExcelErrorDTO();
					errorDTO.setColumnError(colAlias.get(colIndex));
					errorDTO.setLineError(String.valueOf(row.getRowNum() + 1));
					errorDTO.setDetailError(colName.get(colIndex) + " chưa nhập");
					errorList.add(errorDTO);
					result = false;
				}

			}
		}

		return result;
	}
 
	public List<ConstructionTaskDetailDTO> importConstructionTask(String fileInput, HttpServletRequest request,
			Long month, Long year, Long actionType, Long sysGroupId) throws Exception {
		List<ConstructionTaskDetailDTO> workLst = Lists.newArrayList();
		List<ExcelErrorDTO> errorList = new ArrayList<ExcelErrorDTO>();

		try {
			File f = new File(fileInput);
			ZipSecureFile.setMinInflateRatio(-1.0d);
			XSSFWorkbook workbook = new XSSFWorkbook(f);
			XSSFSheet sheet = workbook.getSheetAt(0);
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			DataFormatter formatter = new DataFormatter();
			int count = 0;

			//tatph - start - 25112019
			int counts = 0;
			List<String> listConstrExcel = new ArrayList<>();
			List<String> listWorkItemExcel = new ArrayList<>();
			 for (Row rows : sheet) {
				 counts++;
		            if (counts > 2 && !ValidateUtils.checkIfRowIsEmpty(rows)) {
		            	listConstrExcel.add(formatter.formatCellValue(rows.getCell(1)).trim());
		            	listWorkItemExcel.add(formatter.formatCellValue(rows.getCell(2)).trim());
		            }
		        }
			 
			//tatph - end - 25112019
			HashMap<String, String> constructionMap = new HashMap<String, String>();
//			List<ConstructionDetailDTO> constructionLst = constructionDAO.getConstructionForImportTask();
			//tatph - start 25112019
			List<ConstructionDetailDTO> constructionLst = constructionDAO.getConstructionForImportTaskExcel(listConstrExcel);
			for (ConstructionDetailDTO obj : constructionLst) {
				// if (obj.getCode() != null) {
				constructionMap.put(obj.getCode().toUpperCase(), obj.getConstructionId().toString());
				// }
			}
			HashMap<String, String> workItemMap = new HashMap<String, String>();
			HashMap<String, String> workItemMap2 = new HashMap<String, String>();
//			List<WorkItemDetailDTO> workItemLst = workItemDAO.getWorkItemByName(month, year);
			//tatph-code-25112019
			List<WorkItemDetailDTO> workItemLst = workItemDAO.getWorkItemByNameExcel(month, year,listWorkItemExcel);
			for (WorkItemDetailDTO obj : workItemLst) {
				if (obj.getConstructionCode() != null && obj.getName() != null) {
					workItemMap.put(
							obj.getConstructionCode().toUpperCase().trim() + "|" + obj.getName().toUpperCase().trim(),
							String.valueOf(obj.getWorkItemId()));
					workItemMap2.put(
							obj.getConstructionCode().toUpperCase().trim() + "|" + obj.getName().toUpperCase().trim(),
							obj.getStatus());
				}

			}

			HashMap<String, String> constructionTaskMap = new HashMap<String, String>();
			HashMap<String, String> constructionTaskMap2 = new HashMap<String, String>();
			HashMap<String, String> constructionTaskMap3 = new HashMap<String, String>();
			HashMap<String, Long> constructionTaskMap4 = new HashMap<String, Long>();
			HashMap<String, Long> constructionTaskMap5 = new HashMap<String, Long>();
			List<ConstructionTaskDetailDTO> constructionTaskLst = constructionTaskDAO.getConstructionTaskByDMP(month,
					year, 3l, actionType, sysGroupId);
			for (ConstructionTaskDetailDTO ct : constructionTaskLst) {
				constructionTaskMap.put(ct.getConstructionId() + "|" + ct.getWorkItemId(),
						String.valueOf(ct.getConstructionTaskId()));
				constructionTaskMap2.put(ct.getConstructionId() + "|" + ct.getWorkItemId(), ct.getTaskName());
				constructionTaskMap3.put(ct.getConstructionId() + "|" + ct.getWorkItemId(), ct.getType());
				constructionTaskMap4.put(ct.getConstructionId() + "|" + ct.getWorkItemId(), ct.getParentId());
				constructionTaskMap5.put(ct.getConstructionId() + "|" + ct.getWorkItemId(), ct.getPerformerId());
			}
			// hoanm1_20181023_start
			HashMap<String, String> userMapLogin = new HashMap<String, String>();
			HashMap<String, String> userMapEmail = new HashMap<String, String>();
//			List<SysUserDTO> lstUser = constructionTaskDAO.getListUser();
			//tatph-code-25112019
			List<SysUserDTO> lstUser = constructionTaskDAO.getListUser();
			for (SysUserDTO sys : lstUser) {
				userMapLogin.put(sys.getLoginName(), sys.getUserId().toString());
				userMapEmail.put(sys.getEmail(), sys.getUserId().toString());
			}
			// hoanm1_20181023_end
			for (Row row : sheet) {
				count++;
				if (count >= 3 && checkIfRowIsEmpty(row))
					continue;
				ConstructionTaskDetailDTO obj = new ConstructionTaskDetailDTO();
				if (count >= 3) {
					if (!validateRequiredCell(row, errorList, actionType))
						continue;
					// kiểm tra các ô bắt buộc nhập đã dc nhập chưa

					String constructionCode = formatter.formatCellValue(row.getCell(1)).trim();
					String workItemName = formatter.formatCellValue(row.getCell(2)).toString().trim();
					String performer = formatter.formatCellValue(row.getCell(3)).toString().trim();
					String startDate = formatter.formatCellValue(row.getCell(4)).toString().trim();
					String endDate = formatter.formatCellValue(row.getCell(5)).toString().trim();
					String quantity = "";
					if (actionType == 0) {
						quantity = formatter.formatCellValue(row.getCell(6)).toString().trim();
					}
					Long constructionId = 0l;
					Long workItemId = 0l;

					if (validateString(constructionCode)) {
						try {
							constructionId = Long.parseLong(constructionMap.get(constructionCode.toUpperCase()));
							obj.setConstructionId(constructionId);
							obj.setConstructionCode(constructionCode);
						} catch (Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(1),
									colName.get(1) + " không tồn tại!");
							errorList.add(errorDTO);
						}
					}
					if (actionType == 1) {
						if (validateString(workItemName)) {
							try {
								if (workItemMap2.get(
										constructionCode.toUpperCase().trim() + "|" + workItemName.toUpperCase().trim())
										.equals("3")) {
									ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(2),
											"Hạng mục đã hoàn thành không được phép chuyển người!");
									errorList.add(errorDTO);
								} else {
									workItemId = Long.parseLong(workItemMap.get(constructionCode.toUpperCase().trim()
											+ "|" + workItemName.toUpperCase().trim()));
									obj.setWorkItemId(workItemId);
									obj.setWorkItemName(workItemName);
								}
							} catch (Exception e) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(2),
										colName.get(2) + " không tồn tại trong kế hoạch tháng! ");
								errorList.add(errorDTO);
							}
						}
					} else {
						if (validateString(workItemName)) {
							try {
								// if (workItemDAO.getWorkItemByCodeNew(workItemName,
								// constructionCode).getStatus()
								// .equals("3")) {
								if (workItemMap2.get(
										constructionCode.toUpperCase().trim() + "|" + workItemName.toUpperCase().trim())
										.equals("3")) {
									ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(2),
											"Hạng mục đã hoàn thành không được phép import!");
									errorList.add(errorDTO);
								} else {
									// workItemId = workItemDAO.getWorkItemByCodeNew(workItemName, constructionCode)
									// .getWorkItemId();
									workItemId = Long.parseLong(workItemMap.get(constructionCode.toUpperCase().trim()
											+ "|" + workItemName.toUpperCase().trim()));
									obj.setWorkItemId(workItemId);
									obj.setWorkItemName(workItemName);
								}
							} catch (Exception e) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(2),
										colName.get(2) + " không tồn tại! ");
								errorList.add(errorDTO);
							}
						}
					}

					if (actionType == 0) {
						// check cong trinh+hang muc da ton tai trong ke hoach thang chua
						String key = obj.getConstructionId() + "|" + obj.getWorkItemId();
						if (constructionTaskMap.get(key) != null) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "B, C",
									"Hạng mục tên: [" + obj.getWorkItemName() + "] thuộc công trình có mã: ["
											+ obj.getConstructionCode() + "] đã tồn tại trong kế hoạch tháng " + month
											+ "/" + year + "!");
							errorList.add(errorDTO);
						}
					} else {
						// check cong trinh+hang muc da ton tai trong ke hoach thang chua
						String key = obj.getConstructionId() + "|" + obj.getWorkItemId();
						if (constructionTaskMap.get(key) == null) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "B, C",
									"Mã công trình: " + obj.getConstructionCode() + ", tên hạng mục: " + workItemName
											+ " không tồn tại trong kế hoạch tháng!");
							errorList.add(errorDTO);
						} else {
							obj.setConstructionTaskId(Long.parseLong(constructionTaskMap.get(key)));
							obj.setOldPerformerId(constructionTaskMap5.get(key));
							obj.setTaskName(constructionTaskMap2.get(key));
							obj.setType(constructionTaskMap3.get(key));
							obj.setParentId(constructionTaskMap4.get(key));
						}
					}

					if (validateString(performer)) {
						try {
							// hoanm1_20181023_start
							String userId = "";
							userId = userMapLogin.get(performer.toUpperCase().trim());
							if (userId == null) {
								userId = userMapEmail.get(performer.toUpperCase().trim());
							}
							obj.setPerformerId(Long.parseLong(userId));
							// hoanm1_20181023_end
						} catch (Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(3),
									colName.get(3) + " không hợp lệ!");
							errorList.add(errorDTO);
						}
					}

					if (validateString(startDate)) {
						try {
							Date startDateVal = dateFormat.parse(startDate);
							if (validateDate(startDate)) {
								obj.setStartDate(startDateVal);
							} else {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(4),
										colName.get(4) + " không hợp lệ!");
								errorList.add(errorDTO);
							}

							// obj.setStartDate(dateFormat.parse(startDate));
						} catch (Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(4),
									colName.get(4) + " không hợp lệ!");
							errorList.add(errorDTO);
						}
					}

					if (validateString(endDate)) {
						try {
							// obj.setEndDate(dateFormat.parse(endDate));
							Date endDateVal = dateFormat.parse(endDate);
							if (validateDate(endDate)) {
								obj.setEndDate(endDateVal);
							} else {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(5),
										colName.get(5) + " không hợp lệ!");
								errorList.add(errorDTO);
							}
						} catch (Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(5),
									colName.get(5) + " không hợp lệ!");
							errorList.add(errorDTO);
						}
					}
					// hoanm1_20181023_start_comment
					// if (obj.getEndDate() != null && obj.getStartDate() != null) {
					// if (obj.getEndDate().getTime() < obj.getStartDate().getTime()) {
					// ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "E, F",
					// "Thời gian kết thúc phải lớn hơn thời gian bắt đầu!");
					// errorList.add(errorDTO);
					// }
					// }
					// hoanm1_20181023_end_comment
					if (actionType == 0) {
						if (validateString(quantity)) {
							try {
								// if (quantity.length() > 14) {
								// ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(6),
								// colName.get(6) + " có độ dài vượt quá 14 số! ");
								// errorList.add(errorDTO);
								// }
								if (NumberUtils.isNumber(quantity)) {
									obj.setQuantity(Double.parseDouble(quantity));
								} else {
									ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(6),
											colName.get(6) + " không hợp lệ! ");
									errorList.add(errorDTO);
								}

							} catch (Exception e) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(6),
										colName.get(6) + " không hợp lệ! ");
								errorList.add(errorDTO);
							}
						}
					}

					if (errorList.size() == 0) {
						workLst.add(obj);
					}
				}
			}
			if (errorList.size() > 0) {
				String filePathError = UEncrypt.encryptFileUploadPath(fileInput);
				List<ConstructionTaskDetailDTO> emptyArray = Lists.newArrayList();
				workLst = emptyArray;
				ConstructionTaskDetailDTO errorContainer = new ConstructionTaskDetailDTO();
				errorContainer.setErrorList(errorList);
				if (actionType == 0) {
					errorContainer.setMessageColumn(7); // cột dùng để in ra lỗi
				} else {
					errorContainer.setMessageColumn(6); // cột dùng để in ra lỗi
				}

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
			List<ConstructionTaskDetailDTO> emptyArray = Lists.newArrayList();
			workLst = emptyArray;
			ConstructionTaskDetailDTO errorContainer = new ConstructionTaskDetailDTO();
			errorContainer.setErrorList(errorList);
			if (actionType == 0) {
				errorContainer.setMessageColumn(7); // cột dùng để in ra lỗi
			} else {
				errorContainer.setMessageColumn(6); // cột dùng để in ra lỗi
			}
			errorContainer.setFilePathError(filePathError);
			workLst.add(errorContainer);
			return workLst;
		}
	}

	public List<ConstructionTaskDetailDTO> updateListConstructionTaskPerformer(List<ConstructionTaskDetailDTO> result,
			Long month, Long year, String fileInput, Long actionType, Long sysGroupId, KttsUserSession user) {
		List<ExcelErrorDTO> errorList = new ArrayList<ExcelErrorDTO>();

		HashMap<String, Long> checkDupMap = new HashMap<String, Long>();
		Long i = 0L;
		try {
			for (ConstructionTaskDetailDTO obj : result) {
				String key = obj.getConstructionId() + "|" + obj.getWorkItemId();
				if (checkDupMap.get(key) == null) {
					checkDupMap.put(key, i++);
				} else {
					continue;
				}
				constructionTaskDAO.updateConstructionTaskPerformerId(obj);
				constructionTaskDAO.updateWorkItemPerformerId(obj);
				createSendSmsEmail(obj, user);
				createSendSmsEmailToConvert(obj, user);
				// hoanm1_20180723_start
				obj.setSysGroupId(sysGroupId);
				// hoanm1_20180723_end
				createSendSmsEmailToOperator(obj, user);
				LOGGER.info("Nhảy vào try đâyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
			}
		} catch (Exception e) {
			try {
				LOGGER.info("Nhảy vào catch đâyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
				result.clear();
				ExcelErrorDTO errorDTO = createError(0, "Lỗi phần mềm", e.toString());
				errorList.add(errorDTO);
				ConstructionTaskDetailDTO errorContainer = new ConstructionTaskDetailDTO();
				errorContainer.setErrorList(errorList);
				errorContainer.setMessageColumn(6); // cột dùng để in ra lỗi
				String filePathError = UEncrypt.encryptFileUploadPath(fileInput);
				errorContainer.setFilePathError(filePathError);
				result.add(errorContainer);
				LOGGER.info("Qua đây rồiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
			} catch (Exception ex) {
				LOGGER.error(ex.getMessage(), ex);
			}
		}

		return result;
	}

	public List<ConstructionTaskDetailDTO> addListConstructionTask(KttsUserSession user,
			List<ConstructionTaskDetailDTO> result, Long month, Long year, String fileInput, Long actionType,
			Long sysGroupId, Long detailMonthPlanId) {
		List<ExcelErrorDTO> errorList = new ArrayList<ExcelErrorDTO>();
		HashMap<String, Long> checkDupMap = new HashMap<String, Long>();
		// hoanm1_20181024_start
		HashMap<Long, String> sysGroupMap = new HashMap<Long, String>();
		List<DepartmentDTO> lstGroup = constructionTaskDAO.getListGroup();
		for (DepartmentDTO sys : lstGroup) {
			sysGroupMap.put(sys.getDepartmentId(), sys.getName());
		}
		Long checkGroupLevel1 = constructionTaskDAO.getLevel1SysGroupId(sysGroupId, "1", detailMonthPlanId);
		HashMap<String, Long> constructionCodeMap = new HashMap<String, Long>();
		List<ConstructionTaskDetailDTO> lstConstructionCode = constructionTaskDAO.getLevel2ConstructionCode("1", 2L,
				detailMonthPlanId, sysGroupId);
		for (ConstructionTaskDetailDTO code : lstConstructionCode) {
			constructionCodeMap.put(code.getConstructionCode(), code.getConstructionTaskId());
		}
		// hoanm1_20181024_end
		try {
			Long i = 0L;
			for (ConstructionTaskDetailDTO dto : result) {
				String key = dto.getConstructionId() + "|" + dto.getWorkItemId() + "|" + dto.getPerformerId();
				if (checkDupMap.get(key) == null) {
					checkDupMap.put(key, i++);
				} else {
					continue;
				}
				dto.setLevelId(3L);
				dto.setMonth(month);
				dto.setSysGroupId(sysGroupId);
				dto.setYear(year);
				dto.setType("1");
				dto.setDetailMonthPlanId(detailMonthPlanId);
				// hoanm1_20181024_start
				dto.setSysGroupName(sysGroupMap.get(sysGroupId));
				// hoanm1_20181024_end
				getParentTaskForThiCong(dto, "1", detailMonthPlanId, sysGroupId, checkGroupLevel1, constructionCodeMap);
				dto.setPerformerWorkItemId(dto.getPerformerId());
				// hoanm1_20181023_start
				// WorkItemDetailDTO work =
				// workItemDAO.getWorkItemByCodeNew(dto.getWorkItemName(),
				// dto.getConstructionCode());
				// dto.setTaskName(work.getName());
				// dto.setWorkItemId(work.getWorkItemId());
				dto.setTaskName(dto.getWorkItemName());
				dto.setWorkItemId(dto.getWorkItemId());
				// hoanm1_20181023_end
				dto.setBaselineStartDate(dto.getStartDate());
				dto.setBaselineEndDate(dto.getEndDate());
				dto.setStatus("1");
				dto.setCompleteState("1");
				dto.setPerformerWorkItemId(dto.getPerformerId());
				// hoanm1_20181023_start
				// if (dto.getSupervisorId() == null)
				// dto.setSupervisorId(
				// Double.parseDouble(constructionTaskDAO.getSuperVisorId(dto.getConstructionCode())));
				// if (dto.getDirectorId() == null)
				// dto.setDirectorId(Double.parseDouble(constructionTaskDAO.getDirectorId(dto.getConstructionCode())));
				// hoanm1_20181023_end
				dto.setCreatedDate(new Date());
				dto.setCreatedUserId(user.getVpsUserInfo().getSysUserId());
				dto.setCreatedGroupId(user.getVpsUserInfo().getSysGroupId());

				ConstructionTaskBO bo = dto.toModel();
				Long id = constructionTaskDAO.saveObject(bo);
				detailMonthPlanDAO.updatePerforment(dto.getPerformerId(), dto.getWorkItemId());
				bo.setPath(dto.getPath() + id + "/");
				dto.setPath(dto.getPath() + id + "/");
				dto.setConstructionTaskId(id);
				/**hoangnh start 13032019**/
				bo.setType("1");
				/**hoangnh start 13032019**/
				constructionTaskDAO.updateObject(bo);
				createSendSmsEmail(dto, user);
				if (dto.getWorkItemId() != null)
					insertTaskByWorkItem("1", dto.getWorkItemId(), dto);

			}
		} catch (Exception e) {
			try {
				ExcelErrorDTO errorDTO = createError(0, "Lỗi phần mềm", e.toString());
				errorList.add(errorDTO);
				result.clear();
				ConstructionTaskDetailDTO errorContainer = new ConstructionTaskDetailDTO();
				errorContainer.setErrorList(errorList);
				errorContainer.setMessageColumn(7); // cột dùng để in ra lỗi
				String filePathError = UEncrypt.encryptFileUploadPath(fileInput);
				errorContainer.setFilePathError(filePathError);
				result.add(errorContainer);
			} catch (Exception ex) {
				LOGGER.error(ex.getMessage(), ex);
			}
		}
		return result;
	}

	private void getParentTaskForThiCong(ConstructionTaskDetailDTO dto, String type, Long detailMonthPlanId,
			Long sysGroupId, Long provinceLevelId, HashMap<String, Long> constructionCodeMap) {
		// TODO Auto-generated method stub
		ConstructionTaskDetailDTO provinceLevel = new ConstructionTaskDetailDTO();
		ConstructionTaskDetailDTO constructionLevel = new ConstructionTaskDetailDTO();
		if (sysGroupId != null) {
			// hoanm1_20181024_start
			// provinceLevel = constructionTaskDAO.getLevel1BySysGroupId(sysGroupId, type,
			// detailMonthPlanId);
			// Long provinceLevelId = provinceLevel.getConstructionTaskId();
			// if (provinceLevel.getConstructionTaskId() == null) {//
			if (provinceLevelId == 0L || provinceLevelId == null) {
				// constructionTaskDAO.deleteConstructionTask(type, 1L, detailMonthPlanId);
				// hoanm1_20181023_end
				provinceLevel.setType(type);
				// hoanm1_20181024_start
				// provinceLevel.setTaskName(constructionTaskDAO.getDivisionCodeSysGroupId(sysGroupId));
				provinceLevel.setTaskName(dto.getSysGroupName());
				// hoanm1_20181024_end
				provinceLevel.setLevelId(1L);
				provinceLevel.setSysGroupId(sysGroupId);
				provinceLevel.setDetailMonthPlanId(detailMonthPlanId);
				provinceLevel.setMonth(dto.getMonth());
				provinceLevel.setYear(dto.getYear());
				provinceLevel.setStatus("1");
				provinceLevel.setCompleteState("1");
				ConstructionTaskBO bo = provinceLevel.toModel();
				provinceLevelId = constructionTaskDAO.saveObject(bo);
				bo.setPath("/" + provinceLevelId + "/");
				bo.setConstructionTaskId(provinceLevelId);
				/**Hoangnh start 13032019**/
				bo.setType(type);
				/**Hoangnh end 13032019**/
				constructionTaskDAO.update(bo);
				constructionTaskDAO.getSession().flush();
				// hoanm1_20181023_start
				// constructionTaskDAO.updateChildRecord(bo, detailMonthPlanId);
				// hoanm1_20181023_end

			}
			if (!StringUtils.isNullOrEmpty(dto.getConstructionCode())) {
				// constructionLevel =
				// constructionTaskDAO.getConstructionTaskByConstructionCode(dto.getConstructionCode(),
				// type, 2L, detailMonthPlanId);
				Long constructionLevelId = 0L;
				constructionLevelId = constructionCodeMap.get(dto.getConstructionCode());
				// Long constructionLevelId = constructionLevel.getConstructionTaskId();
				// if (constructionLevel.getConstructionTaskId() == null) {
				if (constructionLevelId == null) {
					constructionLevel.setType(type);
					constructionLevel.setTaskName(dto.getConstructionCode());
					constructionLevel.setLevelId(2L);
					constructionLevel.setDetailMonthPlanId(detailMonthPlanId);
					constructionLevel.setParentId(provinceLevelId);
					constructionLevel.setMonth(dto.getMonth());
					constructionLevel.setYear(dto.getYear());
					constructionLevel.setConstructionId(dto.getConstructionId());
					constructionLevel.setSysGroupId(sysGroupId);
					constructionLevel.setStatus("1");
					constructionLevel.setSupervisorId(dto.getSupervisorId());
					constructionLevel.setDirectorId(dto.getDirectorId());
					constructionLevel.setCompleteState("1");
					ConstructionTaskBO bo = constructionLevel.toModel();
					constructionLevelId = constructionTaskDAO.saveObject(bo);
					bo.setConstructionTaskId(constructionLevelId);
					bo.setPath("/" + provinceLevelId + "/" + constructionLevelId + "/");
					/**Hoangnh start 13032019**/
					bo.setType(type);
					/**Hoangnh end 13032019**/
					constructionTaskDAO.updateObject(bo);
					constructionTaskDAO.getSession().flush();
					constructionCodeMap.put(dto.getConstructionCode(), constructionLevelId);
				}
				dto.setParentId(constructionLevelId);
				dto.setPath("/" + provinceLevelId + "/" + constructionLevelId + "/");
			}
		}
	}

	private void insertTaskByWorkItem(String type, Long workItemId, ConstructionTaskDetailDTO parent) {
		// TODO Auto-generated method stub
		List<ConstructionTaskDTO> list = constructionTaskDAO.getTaskByWorkItem(workItemId);
		if (list != null && !list.isEmpty()) {
			for (ConstructionTaskDTO dto : list) {
				dto.setLevelId(4L);
				dto.setMonth(parent.getMonth());
				dto.setSysGroupId(parent.getSysGroupId());
				dto.setYear(parent.getYear());
				dto.setType(type);
				dto.setWorkItemId(workItemId);
				dto.setStartDate(parent.getStartDate());
				dto.setEndDate(parent.getEndDate());
				dto.setBaselineStartDate(parent.getStartDate());
				dto.setBaselineEndDate(parent.getEndDate());
				dto.setPerformerWorkItemId(parent.getPerformerId());
				dto.setPerformerId(parent.getPerformerId());
				dto.setDetailMonthPlanId(parent.getDetailMonthPlanId());
				dto.setParentId(parent.getConstructionTaskId());
				// hoanm1_20181023_start
				// dto.setSupervisorId(
				// Double.parseDouble(constructionTaskDAO.getSuperVisorId(parent.getConstructionCode())));
				// dto.setDirectorId(Double.parseDouble(constructionTaskDAO.getDirectorId(parent.getConstructionCode())));
				// hoanm1_20181023_end
				dto.setCompleteState("1");
				dto.setStatus("1");
				dto.setConstructionId(parent.getConstructionId());
				ConstructionTaskBO bo = dto.toModel();
				Long id = constructionTaskDAO.saveObject(bo);
				bo.setPath(parent.getPath() + id + "/");
				bo.setConstructionTaskId(id);
				/**Hoangnh start 13032019**/
				bo.setType(type);
				/**Hoangnh end 13032019**/
				constructionTaskDAO.updateObject(bo);
			}
		}

	}

	public DetailMonthPlanDTO getDetailMonthPlanId(Long month, Long year, Long sysGroupId) {
		return constructionTaskDAO.getDetailMonthPlanId(month, year, sysGroupId);
	}

	// chinhpxn20180716_end

	// chinhpxn20180718_start
	public int createSendSmsEmail(ConstructionTaskDTO request, KttsUserSession user) {
		return constructionTaskDAO.createSendSmsEmail(request, user);
	}

	public int createSendSmsEmailToConvert(ConstructionTaskDTO request, KttsUserSession user) {
		return constructionTaskDAO.createSendSmsEmailToConvert(request, user);
	}

	public int createSendSmsEmailToOperator(ConstructionTaskDTO request, KttsUserSession user) {
		return constructionTaskDAO.createSendSmsEmailToOperator(request, user);
	}
	// chinhpxn20180718_end

	@SuppressWarnings("unlikely-arg-type")
	public ConstructionTaskDTO getListImageById(ConstructionTaskDTO obj) throws Exception {
		ConstructionTaskDTO data = new ConstructionTaskDTO();
		List<Long> listConstructionTaskId = new ArrayList<>();
		if (obj.getLevelId() == 4l) {
			listConstructionTaskId.add(obj.getId());
		} else {
			listConstructionTaskId = constructionTaskDAO.getListConstructionTask(obj.getId());
		}
		if (listConstructionTaskId != null) {
			List<UtilAttachDocumentDTO> listImage = utilAttachDocumentDAO
					.getByTypeAndObjectListConstructionTask(listConstructionTaskId, 44L);
			if (listImage != null && !listImage.isEmpty()) {
				for (UtilAttachDocumentDTO dto : listImage) {
					dto.setBase64String(ImageUtil
							.convertImageToBase64(folder2Upload + UEncrypt.decryptFileUploadPath(dto.getFilePath())));
				}
			}
			data.setListImage(listImage);
		}
		return data;
	}

	// hoanm1_20180815_start
	// @Override
	public List<ConstructionTaskDTO> rpSumTask(ConstructionTaskDTO dto, List<String> groupIdList) {
		return constructionTaskDAO.rpSumTask(dto, groupIdList);
	}
	// hoanm1_20180815_end

	public String exportExcelRpSumTask(ConstructionTaskDTO obj, HttpServletRequest request) throws Exception {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Export_congviec_tonghop.xlsx"));
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
				udir.getAbsolutePath() + File.separator + "Export_congviec_tonghop.xlsx");
		List<ConstructionTaskDTO> data = new ArrayList<ConstructionTaskDTO>();
		// hoanm1_20180815_start
		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
				Constant.AdResourceKey.DATA, request);
		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
		data = constructionTaskDAO.rpSumTask(obj, groupIdList);
		// hoanm1_20180815_end
		List<ConstructionTaskDTO> dataSheet2 = constructionTaskDAO.getDataSheetTwoExcel(obj,groupIdList);// Huypq-20181105-edit
		XSSFSheet sheet = workbook.getSheetAt(0);
		if ((data != null && !data.isEmpty()) || (dataSheet2 != null && !dataSheet2.isEmpty())) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			// HuyPQ-20/8/2018-edit-start
			XSSFCellStyle styleNumber = ExcelUtils.styleNumber(sheet);
			styleNumber.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
			XSSFCellStyle styleNumber2 = ExcelUtils.styleNumber(sheet);
			styleNumber2.setDataFormat(workbook.createDataFormat().getFormat("0"));
			// HuyPQ-20/8/2018-edit-end
			// HuyPQ-22/08/2018-start
			XSSFCellStyle styleDate = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleDate.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			// HuyPQ-end
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			styleDate.setAlignment(HorizontalAlignment.CENTER);
			int i = 3;
			int j = 5; // Huypq-20181105-edit
			int m = 3; // Huypq-20181105-edit
			for (ConstructionTaskDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 3));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getSysGroupName() != null) ? dto.getSysGroupName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getFullName() != null) ? dto.getFullName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getContractCode() != null) ? dto.getContractCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getProvinceCode() != null) ? dto.getProvinceCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getStationCode() != null) ? dto.getStationCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getConstructionCode() != null) ? dto.getConstructionCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue((dto.getConstructionTypeName() != null) ? dto.getConstructionTypeName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue((dto.getWorkItemName() != null) ? dto.getWorkItemName() : "");
				cell.setCellStyle(style);
				// HuyPQ-20/8/2018-edit-start
				cell = row.createCell(9, CellType.NUMERIC);
				cell.setCellValue((dto.getQuantity() != null) ? dto.getQuantity() : 0);
				cell.setCellStyle(styleNumber);
				// HuyPQ-20/8/2018-edit-end
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue((dto.getTCTT() != null) ? dto.getTCTT() : "");
				cell.setCellStyle(style);
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue((dto.getGiam_sat() != null) ? dto.getGiam_sat() : "");
				cell.setCellStyle(style);
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue((dto.getKetthuc_trienkhai() != null) ? dto.getKetthuc_trienkhai() : null);
				cell.setCellStyle(styleDate);
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue((dto.getThicong_xong() != null) ? dto.getThicong_xong() : "");
				cell.setCellStyle(style);
				cell = row.createCell(14, CellType.STRING);
				cell.setCellValue((dto.getDang_thicong() != null) ? dto.getDang_thicong() : "");
				cell.setCellStyle(style);
				cell = row.createCell(15, CellType.STRING);
				cell.setCellValue((dto.getLuy_ke() != null) ? dto.getLuy_ke() : "");
				cell.setCellStyle(style);
				cell = row.createCell(16, CellType.STRING);
				cell.setCellValue((dto.getChua_thicong() != null) ? dto.getChua_thicong() : "");
				cell.setCellStyle(style);
				cell = row.createCell(17, CellType.STRING);
				cell.setCellValue((dto.getVuong() != null) ? dto.getVuong() : "");
				cell.setCellStyle(style);
				cell = row.createCell(18, CellType.STRING);
				cell.setCellValue((dto.getBatdau_thicong() != null) ? dto.getBatdau_thicong() : null);
				cell.setCellStyle(styleDate);
				cell = row.createCell(19, CellType.STRING);
				cell.setCellValue((dto.getKetthuc_thicong() != null) ? dto.getKetthuc_thicong() : null);
				cell.setCellStyle(styleDate);
			}
			// Huypq-20181105-start
			for (int k = 0; k < 1; k++) {

				XSSFSheet createSheet = workbook.getSheetAt(1);

				for (ConstructionTaskDTO dto : dataSheet2) {
					if (dto.getDeployType().equals("0")) {
						Row row2 = createSheet.createRow(m++);
						Cell cell2 = row2.createCell(0, CellType.STRING);
						cell2 = row2.createCell(1, CellType.STRING);
						cell2.setCellValue((dto.getSysGroupName() != null) ? dto.getSysGroupName() : "");
						cell2.setCellStyle(style);
						cell2 = row2.createCell(2, CellType.STRING);
						cell2.setCellValue((dto.getFillCatProvince() != null) ? dto.getFillCatProvince() : "");
						cell2.setCellStyle(style);
						cell2 = row2.createCell(3, CellType.STRING);
						cell2.setCellValue((dto.getCatProvinceCode() != null) ? dto.getCatProvinceCode() : "");
						cell2.setCellStyle(style);
						cell2 = row2.createCell(4, CellType.STRING);
						cell2.setCellValue((dto.getWorkItemName() != null) ? dto.getWorkItemName() : "");
						cell2.setCellStyle(style);
						cell2 = row2.createCell(5, CellType.STRING);
						cell2.setCellValue((dto.getWorkItemPartFinish() != null) ? dto.getWorkItemPartFinish() : 0);
						cell2.setCellStyle(styleNumber2);
						cell2 = row2.createCell(6, CellType.STRING);
						cell2.setCellValue((dto.getQuantityPartFinish() != null) ? dto.getQuantityPartFinish() : 0);
						cell2.setCellStyle(styleNumber);
						cell2 = row2.createCell(7, CellType.STRING);
						cell2.setCellValue((dto.getWorkItemConsFinish() != null) ? dto.getWorkItemConsFinish() : 0);
						cell2.setCellStyle(styleNumber2);
						cell2 = row2.createCell(8, CellType.STRING);
						cell2.setCellValue((dto.getQuantityConsFinish() != null) ? dto.getQuantityConsFinish() : 0);
						cell2.setCellStyle(styleNumber);
						cell2 = row2.createCell(9, CellType.STRING);
						cell2.setCellValue((dto.getWorkItemSumFinish() != null) ? dto.getWorkItemSumFinish() : 0);
						cell2.setCellStyle(styleNumber2);
						cell2 = row2.createCell(10, CellType.STRING);
						cell2.setCellValue((dto.getQuantitySumFinish() != null) ? dto.getQuantitySumFinish() : 0);
						cell2.setCellStyle(styleNumber);
						cell2 = row2.createCell(11, CellType.STRING);
						cell2.setCellValue(
								(dto.getWorkItemPartConstructing() != null) ? dto.getWorkItemPartConstructing() : 0);
						cell2.setCellStyle(styleNumber2);
						cell2 = row2.createCell(12, CellType.STRING);
						cell2.setCellValue(
								(dto.getQuantityPartConstructing() != null) ? dto.getQuantityPartConstructing() : 0);
						cell2.setCellStyle(styleNumber);
						cell2 = row2.createCell(13, CellType.STRING);
						cell2.setCellValue(
								(dto.getWorkItemConsConstructing() != null) ? dto.getWorkItemConsConstructing() : 0);
						cell2.setCellStyle(styleNumber2);
						cell2 = row2.createCell(14, CellType.STRING);
						cell2.setCellValue(
								(dto.getQuantityConsConstructing() != null) ? dto.getQuantityConsConstructing() : 0);
						cell2.setCellStyle(styleNumber);
						cell2 = row2.createCell(15, CellType.STRING);
						cell2.setCellValue(
								(dto.getWorkItemSumConstructing() != null) ? dto.getWorkItemSumConstructing() : 0);
						cell2.setCellStyle(styleNumber2);
						cell2 = row2.createCell(16, CellType.STRING);
						cell2.setCellValue(
								(dto.getQuantitySumConstructing() != null) ? dto.getQuantitySumConstructing() : 0);
						cell2.setCellStyle(styleNumber);
						cell2 = row2.createCell(17, CellType.STRING);
						cell2.setCellValue(
								(dto.getWorkItemPartNonConstruction() != null) ? dto.getWorkItemPartNonConstruction()
										: 0);
						cell2.setCellStyle(styleNumber2);
						cell2 = row2.createCell(18, CellType.STRING);
						cell2.setCellValue(
								(dto.getQuantityPartNonConstruction() != null) ? dto.getQuantityPartNonConstruction()
										: 0);
						cell2.setCellStyle(styleNumber);
						cell2 = row2.createCell(19, CellType.STRING);
						cell2.setCellValue(
								(dto.getWorkItemConsNonConstruction() != null) ? dto.getWorkItemConsNonConstruction()
										: 0);
						cell2.setCellStyle(styleNumber2);
						cell2 = row2.createCell(20, CellType.STRING);
						cell2.setCellValue(
								(dto.getQuantityConsNonConstruction() != null) ? dto.getQuantityConsNonConstruction()
										: 0);
						cell2.setCellStyle(styleNumber);
						cell2 = row2.createCell(21, CellType.STRING);
						cell2.setCellValue(
								(dto.getWorkItemSumNonConstruction() != null) ? dto.getWorkItemSumNonConstruction()
										: 0);
						cell2.setCellStyle(styleNumber2);
						cell2 = row2.createCell(22, CellType.STRING);
						cell2.setCellValue(
								(dto.getQuantitySumNonConstruction() != null) ? dto.getQuantitySumNonConstruction()
										: 0);
						cell2.setCellStyle(styleNumber);
						cell2 = row2.createCell(23, CellType.STRING);
						cell2.setCellValue((dto.getWorkItemPartStuck() != null) ? dto.getWorkItemPartStuck() : 0);
						cell2.setCellStyle(styleNumber2);
						cell2 = row2.createCell(24, CellType.STRING);
						cell2.setCellValue((dto.getQuantityPartStuck() != null) ? dto.getQuantityPartStuck() : 0);
						cell2.setCellStyle(styleNumber);
						cell2 = row2.createCell(25, CellType.STRING);
						cell2.setCellValue((dto.getWorkItemConsStuck() != null) ? dto.getWorkItemConsStuck() : 0);
						cell2.setCellStyle(styleNumber2);
						cell2 = row2.createCell(26, CellType.STRING);
						cell2.setCellValue((dto.getQuantityConsStuck() != null) ? dto.getQuantityConsStuck() : 0);
						cell2.setCellStyle(styleNumber);
						cell2 = row2.createCell(27, CellType.STRING);
						cell2.setCellValue((dto.getWorkItemSumStuck() != null) ? dto.getWorkItemSumStuck() : 0);
						cell2.setCellStyle(styleNumber2);
						cell2 = row2.createCell(28, CellType.STRING);
						cell2.setCellValue((dto.getQuantitySumStuck() != null) ? dto.getQuantitySumStuck() : 0);
						cell2.setCellStyle(styleNumber);
					} else {
						Row row2 = createSheet.createRow(j++);
						Cell cell2 = row2.createCell(0, CellType.STRING);
						cell2.setCellValue("" + (j - 5));
						cell2.setCellStyle(styleNumber);
						cell2 = row2.createCell(1, CellType.STRING);
						cell2.setCellValue((dto.getSysGroupName() != null) ? dto.getSysGroupName() : "");
						cell2.setCellStyle(style);
						cell2 = row2.createCell(2, CellType.STRING);
						cell2.setCellValue((dto.getFillCatProvince() != null) ? dto.getFillCatProvince() : "");
						cell2.setCellStyle(style);
						cell2 = row2.createCell(3, CellType.STRING);
						cell2.setCellValue((dto.getCatProvinceCode() != null) ? dto.getCatProvinceCode() : "");
						cell2.setCellStyle(style);
						cell2 = row2.createCell(4, CellType.STRING);
						cell2.setCellValue((dto.getWorkItemName() != null) ? dto.getWorkItemName() : "");
						cell2.setCellStyle(style);
						cell2 = row2.createCell(5, CellType.STRING);
						cell2.setCellValue((dto.getWorkItemPartFinish() != null) ? dto.getWorkItemPartFinish() : 0);
						cell2.setCellStyle(styleNumber2);
						cell2 = row2.createCell(6, CellType.STRING);
						cell2.setCellValue((dto.getQuantityPartFinish() != null) ? dto.getQuantityPartFinish() : 0);
						cell2.setCellStyle(styleNumber);
						cell2 = row2.createCell(7, CellType.STRING);
						cell2.setCellValue((dto.getWorkItemConsFinish() != null) ? dto.getWorkItemConsFinish() : 0);
						cell2.setCellStyle(styleNumber2);
						cell2 = row2.createCell(8, CellType.STRING);
						cell2.setCellValue((dto.getQuantityConsFinish() != null) ? dto.getQuantityConsFinish() : 0);
						cell2.setCellStyle(styleNumber);
						cell2 = row2.createCell(9, CellType.STRING);
						cell2.setCellValue((dto.getWorkItemSumFinish() != null) ? dto.getWorkItemSumFinish() : 0);
						cell2.setCellStyle(styleNumber2);
						cell2 = row2.createCell(10, CellType.STRING);
						cell2.setCellValue((dto.getQuantitySumFinish() != null) ? dto.getQuantitySumFinish() : 0);
						cell2.setCellStyle(styleNumber);
						cell2 = row2.createCell(11, CellType.STRING);
						cell2.setCellValue(
								(dto.getWorkItemPartConstructing() != null) ? dto.getWorkItemPartConstructing() : 0);
						cell2.setCellStyle(styleNumber2);
						cell2 = row2.createCell(12, CellType.STRING);
						cell2.setCellValue(
								(dto.getQuantityPartConstructing() != null) ? dto.getQuantityPartConstructing() : 0);
						cell2.setCellStyle(styleNumber);
						cell2 = row2.createCell(13, CellType.STRING);
						cell2.setCellValue(
								(dto.getWorkItemConsConstructing() != null) ? dto.getWorkItemConsConstructing() : 0);
						cell2.setCellStyle(styleNumber2);
						cell2 = row2.createCell(14, CellType.STRING);
						cell2.setCellValue(
								(dto.getQuantityConsConstructing() != null) ? dto.getQuantityConsConstructing() : 0);
						cell2.setCellStyle(styleNumber);
						cell2 = row2.createCell(15, CellType.STRING);
						cell2.setCellValue(
								(dto.getWorkItemSumConstructing() != null) ? dto.getWorkItemSumConstructing() : 0);
						cell2.setCellStyle(styleNumber2);
						cell2 = row2.createCell(16, CellType.STRING);
						cell2.setCellValue(
								(dto.getQuantitySumConstructing() != null) ? dto.getQuantitySumConstructing() : 0);
						cell2.setCellStyle(styleNumber);
						cell2 = row2.createCell(17, CellType.STRING);
						cell2.setCellValue(
								(dto.getWorkItemPartNonConstruction() != null) ? dto.getWorkItemPartNonConstruction()
										: 0);
						cell2.setCellStyle(styleNumber2);
						cell2 = row2.createCell(18, CellType.STRING);
						cell2.setCellValue(
								(dto.getQuantityPartNonConstruction() != null) ? dto.getQuantityPartNonConstruction()
										: 0);
						cell2.setCellStyle(styleNumber);
						cell2 = row2.createCell(19, CellType.STRING);
						cell2.setCellValue(
								(dto.getWorkItemConsNonConstruction() != null) ? dto.getWorkItemConsNonConstruction()
										: 0);
						cell2.setCellStyle(styleNumber2);
						cell2 = row2.createCell(20, CellType.STRING);
						cell2.setCellValue(
								(dto.getQuantityConsNonConstruction() != null) ? dto.getQuantityConsNonConstruction()
										: 0);
						cell2.setCellStyle(styleNumber);
						cell2 = row2.createCell(21, CellType.STRING);
						cell2.setCellValue(
								(dto.getWorkItemSumNonConstruction() != null) ? dto.getWorkItemSumNonConstruction()
										: 0);
						cell2.setCellStyle(styleNumber2);
						cell2 = row2.createCell(22, CellType.STRING);
						cell2.setCellValue(
								(dto.getQuantitySumNonConstruction() != null) ? dto.getQuantitySumNonConstruction()
										: 0);
						cell2.setCellStyle(styleNumber);
						cell2 = row2.createCell(23, CellType.STRING);
						cell2.setCellValue((dto.getWorkItemPartStuck() != null) ? dto.getWorkItemPartStuck() : 0);
						cell2.setCellStyle(styleNumber2);
						cell2 = row2.createCell(24, CellType.STRING);
						cell2.setCellValue((dto.getQuantityPartStuck() != null) ? dto.getQuantityPartStuck() : 0);
						cell2.setCellStyle(styleNumber);
						cell2 = row2.createCell(25, CellType.STRING);
						cell2.setCellValue((dto.getWorkItemConsStuck() != null) ? dto.getWorkItemConsStuck() : 0);
						cell2.setCellStyle(styleNumber2);
						cell2 = row2.createCell(26, CellType.STRING);
						cell2.setCellValue((dto.getQuantityConsStuck() != null) ? dto.getQuantityConsStuck() : 0);
						cell2.setCellStyle(styleNumber);
						cell2 = row2.createCell(27, CellType.STRING);
						cell2.setCellValue((dto.getWorkItemSumStuck() != null) ? dto.getWorkItemSumStuck() : 0);
						cell2.setCellStyle(styleNumber2);
						cell2 = row2.createCell(28, CellType.STRING);
						cell2.setCellValue((dto.getQuantitySumStuck() != null) ? dto.getQuantitySumStuck() : 0);
						cell2.setCellStyle(styleNumber);
					}
				}
			}
			// Huypq-20181105-end
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();
		String path = UEncrypt
				.encryptFileUploadPath(uploadPathReturn + File.separator + "Export_congviec_tonghop.xlsx");
		return path;
	}

	// nhantv 20180823 start
	public List<WorkItemDetailDTO> getWorkItemForAssign(WorkItemDetailDTO obj) {
		return constructionTaskDAO.getWorkItemForAssign(obj);
	}

	public List<WorkItemDTO> getWorkItemForAddingTask(WorkItemDetailDTO obj) {
		return workItemDAO.getWorkItemForAddingTask(obj);
	}

	public List<ConstructionDTO> getConstrForChangePerformer(ConstructionTaskDTO obj) {
		return constructionDAO.getForChangePerformer(obj);
	}

	// autocomplete công trình trong màn hình chuyển người
	public List<ConstructionDTO> getForChangePerformerAutocomplete(ConstructionTaskDTO obj) {
		return constructionDAO.getForChangePerformerAutocomplete(obj);
	}

	public List<SysUserDTO> getForChangePerformerAutocomplete(ConstructionTaskDTO obj, List<String> sysGroupId) {
		return sysUserDAO.getForChangePerformerAutocomplete(obj, sysGroupId);
	}

	public List<SysUserDTO> getPerformerForChanging(ConstructionTaskDTO obj, List<String> sysGroupId) {
		return sysUserDAO.getPerformerForChanging(obj, sysGroupId);
	}

	public DataListDTO findForChangePerformer(ConstructionTaskDTO obj) {
		List<ConstructionTaskDTO> ls = constructionTaskDAO.findForChangePerformer(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}

	public int updatePerformer(ConstructionTaskDetailDTO obj, KttsUserSession user) {
		for (ConstructionTaskDetailDTO cdt : obj.getChildDTOList()) {
			try {
				constructionTaskDAO.updateTransPerfomer(cdt);
				constructionTaskDAO.updateWorkItemPerformerId(cdt);

				constructionTaskDAO.createSendSmsEmail(cdt, user);
				constructionTaskDAO.createSendSmsEmailToConvert(cdt, user);
				constructionTaskDAO.createSendSmsEmailToOperator(cdt, user);
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
		}
		return 1;
	}
	// nhantv 20180823 stop

	private void checkValidateConstructionTypeLDT(ConstructionTaskDetailDTO newObj, Long loaict, List<ExcelErrorDTO> errorList, Boolean isExistError, HashMap<String, Long> mapLoaiCt, Row row) {
    	if(newObj.getSourceWork().equals("1")) {
			if(!String.valueOf(loaict).equals("1")) {
				isExistError = true;
            	ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "8",
						" Loại công trình không ứng với nguồn việc Xây lắp (Chỉ nhập trong các loại: 1) !");
            	errorList.add(errorDTO);
			} else {
				newObj.setConstructionTypeNew(String.valueOf(loaict));
        		mapLoaiCt.put(newObj.getConstructionCode(), loaict);
			}
		} else if(newObj.getSourceWork().equals("2")) {
			if(!String.valueOf(loaict).equals("2") && !String.valueOf(loaict).equals("3") && !String.valueOf(loaict).equals("4") 
					&& !String.valueOf(loaict).equals("5") && !String.valueOf(loaict).equals("9")) {
				isExistError = true;
            	ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "8",
						" Loại công trình không ứng với nguồn việc Chi phí (Chỉ nhập trong các loại: 2, 3, 4, 5, 9) !");
            	errorList.add(errorDTO);
			} else {
				newObj.setConstructionTypeNew(String.valueOf(loaict));
        		mapLoaiCt.put(newObj.getConstructionCode(), loaict);
			}
		} else if(newObj.getSourceWork().equals("3")) {
			if(!String.valueOf(loaict).equals("6")) {
				isExistError = true;
            	ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "8",
						" Loại công trình không ứng với nguồn việc Ngoài tập đoàn (Chỉ nhập trong các loại: 6) !");
            	errorList.add(errorDTO);
			} else {
				newObj.setConstructionTypeNew(String.valueOf(loaict));
        		mapLoaiCt.put(newObj.getConstructionCode(), loaict);
			}
		} else if(newObj.getSourceWork().equals("4")) {
			if(!String.valueOf(loaict).equals("7")) {
				isExistError = true;
            	ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "8",
						" Loại công trình không ứng với nguồn việc HTCT Xây dựng móng (Chỉ nhập trong các loại: 7) !");
            	errorList.add(errorDTO);
			} else {
				newObj.setConstructionTypeNew(String.valueOf(loaict));
        		mapLoaiCt.put(newObj.getConstructionCode(), loaict);
			}
		} else if(newObj.getSourceWork().equals("5")) {
			if(!String.valueOf(loaict).equals("7")) {
				isExistError = true;
            	ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "8",
						" Loại công trình không ứng với nguồn việc HTCT Hoàn thiện (Chỉ nhập trong các loại: 7) !");
            	errorList.add(errorDTO);
			} else {
				newObj.setConstructionTypeNew(String.valueOf(loaict));
        		mapLoaiCt.put(newObj.getConstructionCode(), loaict);
			}
		} else if(newObj.getSourceWork().equals("6")) {
			if(!String.valueOf(loaict).equals("8")) {
				isExistError = true;
            	ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "8",
						" Loại công trình không ứng với nguồn việc Công trình XDDD (Chỉ nhập trong các loại: 8) !");
				errorList.add(errorDTO);
			} else {
				newObj.setConstructionTypeNew(String.valueOf(loaict));
        		mapLoaiCt.put(newObj.getConstructionCode(), loaict);
			}
		} else if(newObj.getSourceWork().equals("7")) {
			if(!String.valueOf(loaict).equals("6")) {
				isExistError = true;
            	ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "8",
						"Loại công trình không ứng với nguồn việc Xây lắp - Trung tâm xây dựng (Chỉ nhập trong các loại: 6) !");
				errorList.add(errorDTO);
			} else {
				newObj.setConstructionTypeNew(String.valueOf(loaict));
        		mapLoaiCt.put(newObj.getConstructionCode(), loaict);
			}
		}
    }
	
	private void checkValidateConstructionTypeTC(ConstructionTaskDetailDTO newObj, Long loaict, List<ExcelErrorDTO> errorList, Boolean isExistError, HashMap<String, Long> mapLoaiCt, Row row) {
    	if(newObj.getSourceWork().equals("1")) {
			if(!String.valueOf(loaict).equals("1")) {
				isExistError = true;
            	ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(9),
						" Loại công trình không ứng với nguồn việc Xây lắp (Chỉ nhập trong các loại: 1) !");
            	errorList.add(errorDTO);
			} else {
				newObj.setConstructionTypeNew(String.valueOf(loaict));
        		mapLoaiCt.put(newObj.getConstructionCode(), loaict);
			}
		} else if(newObj.getSourceWork().equals("2")) {
			if(!String.valueOf(loaict).equals("2") && !String.valueOf(loaict).equals("3") && !String.valueOf(loaict).equals("4") 
					&& !String.valueOf(loaict).equals("5") && !String.valueOf(loaict).equals("9")) {
				isExistError = true;
            	ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(9),
						" Loại công trình không ứng với nguồn việc Chi phí (Chỉ nhập trong các loại: 2, 3, 4, 5, 9) !");
            	errorList.add(errorDTO);
			} else {
				newObj.setConstructionTypeNew(String.valueOf(loaict));
        		mapLoaiCt.put(newObj.getConstructionCode(), loaict);
			}
		} else if(newObj.getSourceWork().equals("3")) {
			if(!String.valueOf(loaict).equals("6")) {
				isExistError = true;
            	ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(9),
						" Loại công trình không ứng với nguồn việc Ngoài tập đoàn (Chỉ nhập trong các loại: 6) !");
            	errorList.add(errorDTO);
			} else {
				newObj.setConstructionTypeNew(String.valueOf(loaict));
        		mapLoaiCt.put(newObj.getConstructionCode(), loaict);
			}
		} else if(newObj.getSourceWork().equals("4")) {
			if(!String.valueOf(loaict).equals("7")) {
				isExistError = true;
            	ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(9),
						" Loại công trình không ứng với nguồn việc HTCT Xây dựng móng (Chỉ nhập trong các loại: 7) !");
            	errorList.add(errorDTO);
			} else {
				newObj.setConstructionTypeNew(String.valueOf(loaict));
        		mapLoaiCt.put(newObj.getConstructionCode(), loaict);
			}
		} else if(newObj.getSourceWork().equals("5")) {
			if(!String.valueOf(loaict).equals("7")) {
				isExistError = true;
            	ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(9),
						" Loại công trình không ứng với nguồn việc HTCT Hoàn thiện (Chỉ nhập trong các loại: 7) !");
            	errorList.add(errorDTO);
			} else {
				newObj.setConstructionTypeNew(String.valueOf(loaict));
        		mapLoaiCt.put(newObj.getConstructionCode(), loaict);
			}
		} else if(newObj.getSourceWork().equals("6")) {
			if(!String.valueOf(loaict).equals("8")) {
				isExistError = true;
            	ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(9),
						" Loại công trình không ứng với nguồn việc Công trình XDDD (Chỉ nhập trong các loại: 8) !");
				errorList.add(errorDTO);
			} else {
				newObj.setConstructionTypeNew(String.valueOf(loaict));
        		mapLoaiCt.put(newObj.getConstructionCode(), loaict);
			}
		}
    }
	
	public List<ConstructionTaskDetailDTO> importLDT(String fileInput, String filePath, long month, long year,
			long sysGroupId) throws Exception {
		Set<String> constructionSet = constructionTaskDAO.getConstructionTaskMap("3", month, year, sysGroupId);
		List<ConstructionTaskDetailDTO> workLst = new ArrayList<ConstructionTaskDetailDTO>();
//		Map<String, ConstructionDetailDTO> constructionMap = constructionDAO.getCodeAndIdForValidate(); tatph-code-25112019
		Map<String, SysUserDTO> userMapByLoginName = new HashMap<String, SysUserDTO>();
		Map<String, SysUserDTO> userMapByEmail = new HashMap<String, SysUserDTO>();
//		try {
//			detailMonthPlanDAO.getUserForMap(userMapByLoginName, userMapByEmail);
//
//		} catch (Exception e) {
//			return null;
//		} tatph-code-25112019
		File f = new File(fileInput);
		XSSFWorkbook workbook = new XSSFWorkbook(f);
		XSSFSheet sheet = workbook.getSheetAt(0);
		DataFormatter formatter = new DataFormatter();
		DecimalFormat df = new DecimalFormat("#.###");
		DecimalFormat longf = new DecimalFormat("#");
		List<ExcelErrorDTO> errorList = new ArrayList<ExcelErrorDTO>();
		boolean isExistError = false;
		//Huypq-20200201-start
        HashMap<String, Long> mapNguon = new HashMap<>();
        HashMap<String, Long> mapLoaiCt = new HashMap<>();
        HashMap<String, String> mapCheckDup = new HashMap<>();
        //Huy-end
		int count = 0;
		//tatph - start - 25112019
		int counts = 0;
		List<String> listConstr = new ArrayList<>();
		List<String> listConstrExcel = new ArrayList<>();
		List<String> listContractExcel = new ArrayList<>();
		for (Row rows : sheet) {
			counts++;
			if (counts > 2 && !ValidateUtils.checkIfRowIsEmpty(rows)) {
				listConstr.add(formatter.formatCellValue(rows.getCell(3)).trim());
				listConstrExcel.add(formatter.formatCellValue(rows.getCell(3)).trim().toUpperCase());
				listContractExcel.add(formatter.formatCellValue(rows.getCell(5)).trim().toUpperCase());
			}
		}
		Map<String, ConstructionDetailDTO> constructionMap = constructionDAO.getCodeAndIdForValidateExcel(listConstr);
		try {
			detailMonthPlanDAO.getUserForMap(userMapByLoginName, userMapByEmail);

		} catch (Exception e) {
			return null;
		} 
		//tatph - end - 25112019
		
		HashMap<String, ConstructionDetailDTO> mapContractCode = new HashMap<>();
        HashMap<String, ConstructionDetailDTO> mapContractCons = new HashMap<>();
        List<ConstructionDetailDTO> lstContract= detailMonthPlanOSDAO.getCntContractByLstContractCode(listContractExcel);
        for(ConstructionDetailDTO dto : lstContract) {
        	mapContractCode.put(dto.getCntContractCode().trim().toUpperCase(), dto);
        	mapContractCons.put(dto.getCntContractCode().trim().toUpperCase()+"|"+dto.getConstructionCode().trim().toUpperCase(), dto);
        }
		
		HashMap<Long, String> mapSourceWork = new HashMap<>();
        HashMap<Long, String> mapConsType = new HashMap<>();
        List<ConstructionTaskDetailDTO> lstConsType = constructionTaskDAO.checkSourceWorkByConsId(listConstrExcel);
        for(ConstructionTaskDetailDTO dto : lstConsType) {
        	mapSourceWork.put(dto.getConstructionId(), dto.getSourceWork());
        	mapConsType.put(dto.getConstructionId(), dto.getConstructionType());
        }
		
		for (Row row : sheet) {
			count++;
			if (count > 2 && !ValidateUtils.checkIfRowIsEmpty(row)) {
				boolean checkColumn3 = true;
				boolean checkColumn6 = true;
				boolean checkColumn8 = true;
				boolean checkColumn9 = true;
				boolean checkColumn10 = true;
				boolean checkColumn11 = true;
				boolean checkColumn12 = true;
				boolean checkColumn14 = true;
//				ConstructionTaskDetailDTO taskType = new ConstructionTaskDetailDTO();
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				ConstructionTaskDetailDTO newObj = new ConstructionTaskDetailDTO();
				for (int i = 0; i < 15; i++) {
					Cell cell = row.getCell(i);
					if (cell != null) {
						if (cell.getColumnIndex() == 3) {
							String code = formatter.formatCellValue(cell).trim();
							ConstructionDetailDTO obj = constructionMap.get(code);
							boolean isInPlan = constructionSet.contains(code);
							if(mapCheckDup.size()==0) {
								if (isInPlan) {
									isExistError = true;
									ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "4",
											" Công việc đã tồn tại trong kế hoạch tháng!");
									errorList.add(errorDTO);
								} else if (obj != null && !isInPlan) {
									if(obj.getApproveRevenueState().equals("2")) {
										isExistError = true;
										ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "4",
												" Mã công trình đã được phê duyệt doanh thu trước đó !");
										errorList.add(errorDTO);
									} else {
										newObj.setConstructionCode(obj.getCode());
										newObj.setConstructionName(obj.getName());
										newObj.setConstructionId(obj.getConstructionId());
										newObj.setCheckHTCT(obj.getCheckHTCT());
									}
								}
								mapCheckDup.put(code.toUpperCase().trim(), code);
							} else {
								if(mapCheckDup.get(code.toUpperCase().trim())!=null) {
									isExistError = true;
									ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "4",
											" Mã công trình trong cùng file import không được trùng nhau !");
									errorList.add(errorDTO);
								} else {
									if (isInPlan) {
										isExistError = true;
										ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "4",
												" Công việc đã tồn tại trong kế hoạch tháng!");
										errorList.add(errorDTO);
									} else if (obj != null && !isInPlan) {
										if(obj.getApproveRevenueState().equals("2")) {
											isExistError = true;
											ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "4",
													" Mã công trình đã được phê duyệt doanh thu trước đó !");
											errorList.add(errorDTO);
										} else {
											newObj.setConstructionCode(obj.getCode());
											newObj.setConstructionName(obj.getName());
											newObj.setConstructionId(obj.getConstructionId());
											newObj.setCheckHTCT(obj.getCheckHTCT());
										}
									}
									mapCheckDup.put(code.toUpperCase().trim(), code);
								}
							}
							if (obj == null) {
								isExistError = true;
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "4",
										" Mã công trình không tồn tại hoặc chưa được gán vào hợp đồng!");
								errorList.add(errorDTO);
							}
//							ConstructionDetailDTO obj = constructionMap.get(code);
//							boolean isInPlan = constructionSet.contains(code);
//							if (isInPlan) {
//								isExistError = true;
//								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "4",
//										" Công việc đã tồn tại trong kế hoạch tháng!");
//								errorList.add(errorDTO);
//							} else if (obj != null && !isInPlan) {
//								newObj.setConstructionCode(obj.getCode());
//								newObj.setConstructionName(obj.getName());
//								newObj.setConstructionId(obj.getConstructionId());
//							}
//							if (obj == null) {
//								isExistError = true;
//								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "4",
//										" Chưa nhập mã công trình hoặc mã công trình không tồn tại!");
//								errorList.add(errorDTO);
//							}

						} else if (cell.getColumnIndex() == 5) {
//							newObj.setCntContract(
//									formatter.formatCellValue(cell) != null ? formatter.formatCellValue(cell) : "");
							try {
                            	String code = formatter.formatCellValue(row.getCell(5));
                            	if(code!=null) {
                            		if(mapContractCode.get(code.trim().toUpperCase())!=null) {
                            			if(newObj.getConstructionCode()!=null && mapContractCons.get(code.trim().toUpperCase()+"|"+newObj.getConstructionCode().toUpperCase().trim())==null) {
                            				isExistError = true;
                                            ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "6",
            										" Mã công trình chưa được gán vào hợp đồng đầu ra !");
            								errorList.add(errorDTO);
                            			} else {
                            				newObj.setCntContract(code);
                            			}
                            		} else {
                            			isExistError = true;
                                        ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "6",
        										" Mã hợp đồng đầu ra không tồn tại");
        								errorList.add(errorDTO);
                            		}
                            	} else {
                            		isExistError = true;
                                    ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "6",
    										" Chưa nhập mã hợp đồng đầu ra!");
    								errorList.add(errorDTO);
                            	}
                            } catch (Exception e) {
                            	isExistError = true;
                                ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "6",
										" Mã hợp đồng đầu ra không hợp lệ !");
								errorList.add(errorDTO);
                            }
						} else if (cell.getColumnIndex() == 6) {
							try {
								//Huypq-20191003-start
								Double quantity = new Double(cell.getNumericCellValue());
//								Double quantity = new Double(cell.getNumericCellValue()*1000000);
								//Huypq-end
								newObj.setQuantity(quantity);
							} catch (Exception e) {
								checkColumn6 = false;
							}
							if (!checkColumn6) {
								isExistError = true;
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "7",
										" Giá trị không hợp lệ!");
								errorList.add(errorDTO);
							}
						} 
						
						else if (cell.getColumnIndex() == 7) {
                            if (row.getCell(7) != null) {
                            	try {
                            		long nguon = new Long(Long.parseLong(formatter.formatCellValue(row.getCell(7)).trim()));
                                    if(nguon>0 && nguon<8) {
                                    	//Huypq-20200201-start
                                    	if(newObj.getConstructionId()!=null) {
                                    		String sourceWorkDb = mapSourceWork.get(newObj.getConstructionId());
                                    		if(sourceWorkDb!=null) {
                                    			if(newObj.getCheckHTCT()!=null && newObj.getCheckHTCT()==1l) {
                                        			if(nguon != 4l && nguon != 5l) {
                                            			checkColumn8 = false;
                                                    	isExistError = true;
                        								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "8",
                        										" Nguồn việc của công trình HTCT chỉ được nhập Xây dựng móng và Hoàn thiện !");
                        								errorList.add(errorDTO);
                                        			} else {
                                        				newObj.setSourceWork(String.valueOf(nguon));
                                        			}
                                        		} else {
                                        			if(nguon == 4l || nguon == 5l) {
                                        				isExistError = true;
                        								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "8",
                        										" Nguồn việc Xây dựng móng và Hoàn thiện chỉ ứng với công trình HTCT !");
                        								errorList.add(errorDTO);
                                        			} else {
                                        				if(sourceWorkDb.equals(formatter.formatCellValue(row.getCell(7)).trim())) {
                                                			if(mapNguon.size()==0) {
                                                        		newObj.setSourceWork(String.valueOf(nguon));
                                                        		mapNguon.put(newObj.getConstructionCode(), nguon);
                                                        	} else {
                                                        		if(mapNguon.get(newObj.getConstructionCode())!=null && mapNguon.get(newObj.getConstructionCode()) != nguon) {
                                                                	isExistError = true;
                                    								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "8",
                                    										" Nguồn việc của những hạng mục thuộc cùng công trình phải giống nhau !");
                                    								errorList.add(errorDTO);
                                                        		} else {
                                                        			newObj.setSourceWork(String.valueOf(nguon));
                                                            		mapNguon.put(newObj.getConstructionCode(), nguon);
                                                        		}
                                                        	}
                                                		} else {
                                                			isExistError = true;
                            								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "8",
                            										" Nguồn việc của công trình: "+ newObj.getConstructionCode() + " đã tồn tại bằng: "+ sourceWorkDb);
                            								errorList.add(errorDTO);
                                                		}
                                        			}
                                        		}
                                        	} else {
                                        		if(newObj.getCheckHTCT()!=null && newObj.getCheckHTCT()==1l) {
                                        			if(nguon != 4l && nguon != 5l) {
                                            			checkColumn8 = false;
                                                    	isExistError = true;
                        								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "8",
                        										" Nguồn việc của công trình HTCT chỉ được nhập Xây dựng móng và Hoàn thiện !");
                        								errorList.add(errorDTO);
                                        			} else {
                                        				newObj.setSourceWork(String.valueOf(nguon));
                                        			}
                                        		} else {
                                        			if(nguon == 4l || nguon == 5l) {
                                        				isExistError = true;
                        								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "8",
                        										" Nguồn việc Xây dựng móng và Hoàn thiện chỉ ứng với công trình HTCT !");
                        								errorList.add(errorDTO);
                                        			} else {
                                        				if(mapNguon.size()==0) {
                                                    		newObj.setSourceWork(String.valueOf(nguon));
                                                    		mapNguon.put(newObj.getConstructionCode(), nguon);
                                                    	} else {
                                                    		if(mapNguon.get(newObj.getConstructionCode())!=null && mapNguon.get(newObj.getConstructionCode()) != nguon) {
                                                            	isExistError = true;
                                								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "8",
                                										" Nguồn việc của những hạng mục thuộc cùng công trình phải giống nhau !");
                                								errorList.add(errorDTO);
                                                    		} else {
                                                    			newObj.setSourceWork(String.valueOf(nguon));
                                                        		mapNguon.put(newObj.getConstructionCode(), nguon);
                                                    		}
                                                    	}
                                        			}
                                        		}
                                        	}
                                    	} else {
                                    		newObj.setSourceWork(String.valueOf(nguon));
                                    		mapNguon.put(newObj.getConstructionCode(), nguon);
                                    	}
                                    	//Huypq-end
                                    } else {
                                    	isExistError = true;
        								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "8",
        										" Nguồn việc không hợp lệ !");
        								errorList.add(errorDTO);
                                    }
                                } catch (Exception e) {
                                    isExistError = true;
    								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "8",
    										" Nguồn việc phải nhập dạng số!");
    								errorList.add(errorDTO);
                                }
                            } else {
                            	isExistError = true;
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "8",
										" Nguồn việc không được để trống !");
								errorList.add(errorDTO);
                            }
                        } else if (cell.getColumnIndex() == 8) {
                            if (row.getCell(8) != null) {
                            	try {
                            		long loaict = new Long(Long.parseLong(formatter.formatCellValue(row.getCell(8)).trim()));
                                    if(loaict>0 && loaict<10) {
                                    	//Huypq-20200201-start
                                    	if(newObj.getConstructionId()!=null) {
                                    		String consTypeDb = mapConsType.get(newObj.getConstructionId());
                                        	if(consTypeDb!=null) {
                                        		if(consTypeDb.equals(formatter.formatCellValue(row.getCell(8)).trim())) {
                                        			if(mapLoaiCt.size()==0) {
//                                                		newObj.setConstructionTypeNew(String.valueOf(loaict));
//                                                		mapLoaiCt.put(newObj.getConstructionCode(), loaict);
                                        				checkValidateConstructionTypeLDT(newObj, loaict, errorList, isExistError, mapLoaiCt, row);
                                                	} else {
                                                		if(mapLoaiCt.get(newObj.getConstructionCode())!=null && mapLoaiCt.get(newObj.getConstructionCode()) != loaict) {
                                                        	isExistError = true;
                            								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "9",
                            										" Loại công trình của những hạng mục thuộc cùng công trình phải giống nhau !");
                            								errorList.add(errorDTO);
                                                		} else {
                                                			checkValidateConstructionTypeLDT(newObj, loaict, errorList, isExistError, mapLoaiCt, row);
                                                		}
                                                	}
                                        		} else {
                                        			isExistError = true;
                    								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "9",
                    										" Loại công trình của công trình: "+ newObj.getConstructionCode() +" đã tồn tại bằng: "+ consTypeDb);
                    								errorList.add(errorDTO);
                                        		}
                                        	} else {
                                        		if(mapLoaiCt.size()==0) {
                                        			checkValidateConstructionTypeLDT(newObj, loaict, errorList, isExistError, mapLoaiCt, row);
                                            	} else {
                                            		if(mapLoaiCt.get(newObj.getConstructionCode())!=null && mapLoaiCt.get(newObj.getConstructionCode()) != loaict) {
                                                    	isExistError = true;
                        								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "9",
                        										" Loại công trình của những hạng mục thuộc cùng công trình phải giống nhau !");
                        								errorList.add(errorDTO);
                                            		} else {
                                            			checkValidateConstructionTypeLDT(newObj, loaict, errorList, isExistError, mapLoaiCt, row);
                                            		}
                                            	}
                                        	}
                                    	} else {
                                    		checkValidateConstructionTypeLDT(newObj, loaict, errorList, isExistError, mapLoaiCt, row);
                                    	}
                                    	
                                    	//Huypq-end
                                    } else {
                                    	isExistError = true;
        								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "9",
        										" Loại công trình không hợp lệ !");
        								errorList.add(errorDTO);
                                    }
                                } catch (Exception e) {
                                    isExistError = true;
    								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "9",
    										" Loại công trình phải nhập dạng số!");
    								errorList.add(errorDTO);
                                }
                            } else {
                            	isExistError = true;
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "9",
										" Loại công trình không được để trống !");
								errorList.add(errorDTO);
                            }
                        } 
						
						else if (cell.getColumnIndex() == 10) {
							String name = formatter.formatCellValue(cell).trim();
							SysUserDTO obj = userMapByLoginName.get(name.toUpperCase()) != null
									? userMapByLoginName.get(name.toUpperCase())
									: userMapByEmail.get(name.toUpperCase());
							if (obj != null) {
								newObj.setPerformerId(obj.getUserId());
								newObj.setPerformerName(obj.getFullName());
							} else {
								isExistError = true;
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "11",
										" Sai tên đăng nhập hoặc email!");
								errorList.add(errorDTO);
							}
						} else if (cell.getColumnIndex() == 11) {
							try {
								Date startDate = dateFormat.parse(formatter.formatCellValue(cell));
								if (validateDate(formatter.formatCellValue(cell)))
									newObj.setStartDate(startDate);
								else
									checkColumn11 = false;
							} catch (Exception e) {
								checkColumn11 = false;
							}
							if (!checkColumn11) {
								isExistError = true;
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "12",
										" Ngày bắt đầu không hợp lệ!");
								errorList.add(errorDTO);
							}
						} else if (cell.getColumnIndex() == 12) {
							try {
								Date endDate = dateFormat.parse(formatter.formatCellValue(cell));
								if (validateDate(formatter.formatCellValue(cell)))
									newObj.setEndDate(endDate);
								else
									checkColumn12 = false;
							} catch (Exception e) {
								checkColumn12 = false;
							}
							if (!checkColumn12) {
								isExistError = true;
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "13",
										" Ngày kết thúc không hợp lệ!");
								errorList.add(errorDTO);
							}
						} 
						//Huypq-20191001-start comment
//						else if (cell.getColumnIndex() == 11) {
//							try {
//								String taskOrder = formatter.formatCellValue(cell).trim();
//								if (taskOrder.equalsIgnoreCase("1")) {
//									newObj.setTaskOrder("1");
//									newObj.setTaskName(
//											"Tạo đề nghị quyết toán cho công trình " + newObj.getConstructionCode());
//								} else if (taskOrder.equalsIgnoreCase("2")) {
//									newObj.setTaskOrder("2");
//									newObj.setTaskName("Lên doanh thu cho công trình " + newObj.getConstructionCode());
//								} else {
//									checkColumn11 = false;
//								}
//							} catch (Exception e) {
//								checkColumn11 = false;
//							}
//							if (!checkColumn11) {
//								isExistError = true;
//								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "12",
//										" Quyết toán/ Doanh thu không hợp lệ!");
//								errorList.add(errorDTO);
//							}
//						} 
						//Huy-end
						else if (cell.getColumnIndex() == 14) {
							try {
								String descriptionHSHC = formatter.formatCellValue(cell).trim();
								if (descriptionHSHC.length() <= 1000) {
									newObj.setDescription(descriptionHSHC);
								} else {
									checkColumn14 = false;
									isExistError = true;
								}
							} catch (Exception e) {
								checkColumn14 = false;
							}
							if (!checkColumn14) {
								isExistError = true;
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "15",
										" Ghi chú vượt quá 1000 ký tự!");
								errorList.add(errorDTO);
							}
						}
						//Huypq-20191001-start
						newObj.setTaskOrder("2");
						newObj.setTaskName("Lên doanh thu cho công trình " + newObj.getConstructionCode());
						//Huy-end
					}
				}
				if (errorList.size()==0) {
					workLst.add(newObj);
				}
			}
		}
		if (errorList.size()>0) {
			workLst = new ArrayList<ConstructionTaskDetailDTO>();
			ConstructionTaskDetailDTO objErr = new ConstructionTaskDetailDTO();
			objErr.setErrorList(errorList);
			objErr.setMessageColumn(15);
			objErr.setErrorFilePath(UEncrypt.encryptFileUploadPath(filePath));
			objErr.setFilePathError(UEncrypt.encryptFileUploadPath(filePath));
			workLst.add(objErr);
		}
		workbook.close();
		return workLst;
	}

	public List<ConstructionTaskDetailDTO> importHSHC(String fileInput, String filePath, long month, long year,
			long sysGroupId) throws Exception {
		List<ConstructionTaskDetailDTO> workLst = new ArrayList<ConstructionTaskDetailDTO>();
		Set<String> constructionSet = constructionTaskDAO.getConstructionTaskMap("2", month, year, sysGroupId);
//		Map<String, ConstructionDetailDTO> constructionMap = constructionDAO.getCodeAndIdForValidate();  tatph-code-25112019
		Map<String, SysUserDTO> userMapByLoginName = new HashMap<String, SysUserDTO>();
		Map<String, SysUserDTO> userMapByEmail = new HashMap<String, SysUserDTO>();
		Map<String, ConstructionDetailDTO> constructionCntContractMap = constructionDAO.getConstructionCntContract();
//		try {
//			detailMonthPlanDAO.getUserForMap(userMapByLoginName, userMapByEmail);
//		} catch (Exception e) {
//			return null;
//		}   tatph - code - 25112019
		List<ExcelErrorDTO> errorList = new ArrayList<ExcelErrorDTO>();
		File f = new File(fileInput);
		XSSFWorkbook workbook = new XSSFWorkbook(f);
		XSSFSheet sheet = workbook.getSheetAt(0);
		DataFormatter formatter = new DataFormatter();
		boolean isExistError = false;
		int count = 0;
		//Huypq-20200201-start
        HashMap<String, Long> mapNguon = new HashMap<>();
        HashMap<String, Long> mapLoaiCt = new HashMap<>();
        //Huy-end
		//tatph-start-25112019
		int counts = 0;
		List<String> listConstrExcel = new ArrayList<>();
		List<String> listContractExcel  = new ArrayList<>();
		for (Row rows : sheet) {
			counts++;
			if (counts > 2 && !ValidateUtils.checkIfRowIsEmpty(rows)) {
				listConstrExcel.add(formatter.formatCellValue(rows.getCell(3)).trim());
				listContractExcel.add(formatter.formatCellValue(rows.getCell(6)).trim().toUpperCase());
			}
		}
		Map<String, ConstructionDetailDTO> constructionMap = constructionDAO.getCodeAndIdForValidateExcel(listConstrExcel);
		try {
			detailMonthPlanDAO.getUserForMap(userMapByLoginName, userMapByEmail);
		} catch (Exception e) {
			return null;
		}
		//tatph-end-25112019
		
		HashMap<String, ConstructionDetailDTO> mapContractCode = new HashMap<>();
        HashMap<String, ConstructionDetailDTO> mapContractCons = new HashMap<>();
        List<ConstructionDetailDTO> lstContract= detailMonthPlanOSDAO.getCntContractByLstContractCode(listContractExcel);
        for(ConstructionDetailDTO dto : lstContract) {
        	mapContractCode.put(dto.getCntContractCode().trim().toUpperCase(), dto);
        	mapContractCons.put(dto.getCntContractCode().trim().toUpperCase()+"|"+dto.getConstructionCode().trim().toUpperCase(), dto);
        }
        
		HashMap<String, String> mapCode = new HashMap<>();//HuyPQ-20190425-add
		for (Row row : sheet) {
			count++;
			if (count > 2 && !ValidateUtils.checkIfRowIsEmpty(row)) {
				boolean checkColumn2 = true;
				boolean checkColumn6 = true;
				boolean checkColumn8 = true;
				boolean checkColumn9 = true;
				boolean checkColumn10 = true;
				boolean checkColumn11 = true;
				boolean checkColumn12 = true;
				boolean checkColumn13 = true;
				boolean checkColumn14 = true;
//              hoanm1_20181229_start
				boolean checkColumn5 = true;
				boolean checkContract = true;
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				ConstructionTaskDetailDTO newObj = new ConstructionTaskDetailDTO();
				ConstructionTaskDetailDTO taskType = new ConstructionTaskDetailDTO();
				String code="";
//                hoanm1_20181229_end
				for (int i = 0; i < 15; i++) {
					Cell cell = row.getCell(i);
					if (cell != null) {
						if (cell.getColumnIndex() == 3) {
							code = formatter.formatCellValue(row.getCell(3)).trim();
							ConstructionDetailDTO obj = constructionMap.get(code);
							boolean isInPlan = constructionSet.contains(code);

							if (isInPlan) {
								isExistError = true;
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "4",
										"Công việc đã tồn tại trong kế hoạch tháng!");
								errorList.add(errorDTO);
							} else if (obj != null && !isInPlan) {
								newObj.setConstructionCode(obj.getCode());
								newObj.setConstructionName(obj.getName());
								newObj.setConstructionId(obj.getConstructionId());
								//HuyPQ-20190425-start
								if(mapCode.size()==0) {
									mapCode.put(code, code);
								} else {
									if(mapCode.get(code)!=null) {
										isExistError = true;
                            			ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "4",
        										" Mã công trình đã tồn tại trong file import!");
        								errorList.add(errorDTO);
									} else {
										mapCode.put(code, code);
									}
								}
								//HuyPQ-20190425-end
							}
							if (obj == null) {
								checkColumn2 = false;
								isExistError = true;
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "4",
										" Mã công trình không tồn tại hoặc chưa được gán vào hợp đồng!");
								errorList.add(errorDTO);
							}
							//Huypq-20190211-start
//                            if(constructionCodeMaps.size()>=2) {
//                            	for(int k=0;k<constructionCodeMaps.size();k++) {
//                                	for(int h=k+1;h<constructionCodeMaps.size();h++) {
//                                		if((constructionCodeMaps.get(k).getConstructionCode().trim()).equals(constructionCodeMaps.get(h).getConstructionCode().trim())) {
//                                			isExistError = true;
//                                			ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "4",
//            										" Mã công trình đã tồn tại trong file import!");
//            								errorList.add(errorDTO);
//                                		}
//                                	}
//                                }
//                            }
                            //Huypq-end
						} 
//                      hoanm1_20181229_start
                      else if (cell.getColumnIndex() == 5) {
                          try {
                          	if(!formatter.formatCellValue(cell).trim().isEmpty()){
                          		newObj.setWorkItemNameHSHC(formatter.formatCellValue(cell).trim());
                          	}else{
                          		checkColumn5 = false;
                          	}
                          } catch (Exception e) {
                        	  checkColumn5 = false;
                          }
//							if (!checkColumn5) {
//								isExistError = true;
//								errorList.add(createError(row.getRowNum() + 1, "5", "Hạng mục hoàn công không được để trống"));
//							}
                      } 
//						hoanm1_20190109_start
                      else if (cell.getColumnIndex() == 6) {
//                          try {
//                      			ConstructionDetailDTO obj = constructionCntContractMap.get(code);
//                            		if (obj == null) {
//                            			checkContract = false;	
//                            		}
//                            } catch (Exception e) {
//                            	checkContract = false;
//                            }
//  							if (!checkContract) {
//  								isExistError = true;
//  								errorList.add(createError(row.getRowNum() + 1, "6", "Công trình chưa được gán hợp đồng"));
//  							}
                    	  try {
                          	String contractCode = formatter.formatCellValue(row.getCell(6));
                          	if(contractCode!=null) {
                          		if(mapContractCode.get(contractCode.trim().toUpperCase())!=null) {
                          			if(mapContractCons.get(contractCode.trim().toUpperCase()+"|"+newObj.getConstructionCode().toUpperCase().trim())==null) {
                          				isExistError = true;
                                          errorList.add(createError(row.getRowNum() + 1, "6", "Mã công trình chưa được gán vào hợp đồng đầu ra !"));
                          			} else {
                          				newObj.setCntContract(contractCode);
                          			}
                          		} else {
                          			isExistError = true;
                                      errorList.add(createError(row.getRowNum() + 1, "6", "Mã hợp đồng đầu ra không tồn tại !"));
                          		}
                          	} else {
                          		isExistError = true;
                                  errorList.add(createError(row.getRowNum() + 1, "6", "Chưa nhập mã hợp đồng đầu ra !"));
                          	}
                          } catch (Exception e) {
                          	isExistError = true;
                              errorList.add(createError(row.getRowNum() + 1, "6", "Mã hợp đồng đầu ra không hợp lệ !"));
                          }
                        } 
//                        hoanm1_20190109_end
						else if (cell.getColumnIndex() == 7) {
							try {
								Double quantity = new Double(cell.getNumericCellValue() * 1000000);
								newObj.setQuantity(quantity);
							} catch (Exception e) {
								checkColumn6 = false;
							}
							if (!checkColumn6) {
								isExistError = true;
								errorList.add(createError(row.getRowNum() + 1, "7", " Giá trị không hợp lệ!"));
							}
						}
						
//						else if (cell.getColumnIndex() == 8) {
//                            if (row.getCell(8) != null) {
//                            	try {
//                                    Long nguon = new Long(Long.parseLong(formatter.formatCellValue(cell)));
//                                    if(nguon>0 && nguon<7) {
//                                    	//Huypq-20200201-start
//                                    	taskType = constructionTaskDAO.checkSourceWorkByConsId(newObj.getConstructionId());
//                                    	if(taskType!=null) {
//                                    		if(taskType.getSourceWork().equals(formatter.formatCellValue(cell))) {
//                                    			if(mapNguon.size()==0) {
//                                            		newObj.setSourceWork(String.valueOf(nguon));
//                                            		mapNguon.put(newObj.getConstructionCode(), nguon);
//                                            	} else {
//                                            		if(mapNguon.get(newObj.getConstructionCode()) != nguon) {
//                                                    	checkColumn8 = false;
//                                                    	errorList.add(createError(row.getRowNum() + 1, "8", "Nguồn việc của những hạng mục thuộc cùng công trình phải giống nhau!"));
//                                            		} else {
//                                            			newObj.setSourceWork(String.valueOf(nguon));
//                                                		mapNguon.put(newObj.getConstructionCode(), nguon);
//                                            		}
//                                            	}
//                                    		} else {
//                                    			checkColumn8 = false;
//                                            	errorList.add(createError(row.getRowNum() + 1, "8", " Nguồn việc của công trình "+ newObj.getConstructionCode() + " đã tồn tại bằng: "+ taskType.getSourceWork()));
//                                    		}
//                                    	} else {
//                                    		if(mapNguon.size()==0) {
//                                        		newObj.setSourceWork(String.valueOf(nguon));
//                                        		mapNguon.put(newObj.getConstructionCode(), nguon);
//                                        	} else {
//                                        		if(mapNguon.get(newObj.getConstructionCode()) != nguon) {
//                                                	checkColumn8 = false;
//                                                	errorList.add(createError(row.getRowNum() + 1, "8", "Nguồn việc của những hạng mục thuộc cùng công trình phải giống nhau!"));
//                                        		} else {
//                                        			newObj.setSourceWork(String.valueOf(nguon));
//                                            		mapNguon.put(newObj.getConstructionCode(), nguon);
//                                        		}
//                                        	}
//                                    	}
//                                    	//Huypq-end
//                                    } else {
//                                    	checkColumn8 = false;
//                                    	errorList.add(createError(row.getRowNum() + 1, "8", "Nguồn việc không hợp lệ !"));
//                                    }
//                                } catch (Exception e) {
//                                    checkColumn8 = false;
//                                    errorList.add(createError(row.getRowNum() + 1, "8", "Nguồn việc phải nhập dạng số !"));
//                                }
//                            } else {
//                            	checkColumn8 = false;
//                            	errorList.add(createError(row.getRowNum() + 1, "8", "Nguồn việc không được để trống !"));
//                            }
//                        } else if (cell.getColumnIndex() == 9) {
//                            if (row.getCell(9) != null) {
//                            	try {
//                                    Long loaict = new Long(Long.parseLong(formatter.formatCellValue(cell)));
//                                    if(loaict>0 && loaict<10) {
//                                    	//Huypq-20200201-start
//                                    	taskType = constructionTaskDAO.checkSourceWorkByConsId(newObj.getConstructionId());
//                                    	if(taskType!=null) {
//                                    		if(taskType.getConstructionType().equals(formatter.formatCellValue(cell))) {
//                                    			if(mapLoaiCt.size()==0) {
//                                            		newObj.setConstructionTypeNew(String.valueOf(loaict));
//                                            		mapLoaiCt.put(newObj.getConstructionCode(), loaict);
//                                            	} else {
//                                            		if(mapLoaiCt.get(newObj.getConstructionCode()) != loaict) {
//                                                    	checkColumn9 = false;
//                                                    	errorList.add(createError(row.getRowNum() + 1, "9", "Loại công trình của những hạng mục thuộc cùng công trình phải giống nhau !"));
//                                            		} else {
//                                            			newObj.setConstructionTypeNew(String.valueOf(loaict));
//                                                		mapLoaiCt.put(newObj.getConstructionCode(), loaict);
//                                            		}
//                                            	}
//                                    		} else {
//                                    			checkColumn9 = false;
//                                            	errorList.add(createError(row.getRowNum() + 1, "9", " Loại công trình của công trình "+ newObj.getConstructionCode() + " đã tồn tại bằng: "+ taskType.getConstructionType()));
//                                    		}
//                                    	} else {
//                                    		if(mapLoaiCt.size()==0) {
//                                        		newObj.setConstructionTypeNew(String.valueOf(loaict));
//                                        		mapLoaiCt.put(newObj.getConstructionCode(), loaict);
//                                        	} else {
//                                        		if(mapLoaiCt.get(newObj.getConstructionCode()) != loaict) {
//                                                	checkColumn9 = false;
//                                                	errorList.add(createError(row.getRowNum() + 1, "9", "Loại công trình của những hạng mục thuộc cùng công trình phải giống nhau !"));
//                                        		} else {
//                                        			newObj.setConstructionTypeNew(String.valueOf(loaict));
//                                            		mapLoaiCt.put(newObj.getConstructionCode(), loaict);
//                                        		}
//                                        	}
//                                    	}
//                                    	//Huypq-end
//                                    } else {
//                                    	checkColumn9 = false;
//                                    	errorList.add(createError(row.getRowNum() + 1, "9", "Loại công trình không hợp lệ !"));
//                                    }
//                                } catch (Exception e) {
//                                    checkColumn9 = false;
//                                    errorList.add(createError(row.getRowNum() + 1, "9", "Loại công trình phải nhập dạng số!"));
//                                }
//                            } else {
//                            	checkColumn9 = false;
//                            	errorList.add(createError(row.getRowNum() + 1, "9", "Loại công trình không được để trống !"));
//                            }
//                        } 
						
						else if (cell.getColumnIndex() == 10) {
							try {
								Date completeDate = dateFormat.parse(formatter.formatCellValue(cell));
								newObj.setCompleteDate(completeDate);
							} catch (Exception e) {
								checkColumn10 = false;
							}
						} else if (cell.getColumnIndex() == 11) {
							String name = formatter.formatCellValue(cell).trim();
							SysUserDTO obj = userMapByLoginName.get(name.toUpperCase()) != null
									? userMapByLoginName.get(name.toUpperCase())
									: userMapByEmail.get(name.toUpperCase());
							if (obj != null) {
								newObj.setPerformerId(obj.getUserId());
								newObj.setPerformerName(obj.getFullName());
							} else {
								isExistError = true;
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "11",
										" Sai tên đăng nhập hoặc email!");
								errorList.add(errorDTO);
							}
						} else if (cell.getColumnIndex() == 12) {
							try {
								Date startDate = dateFormat.parse(formatter.formatCellValue(cell));
								if (validateDate(formatter.formatCellValue(cell))) {
									newObj.setStartDate(startDate);
								} else {
									checkColumn12 = false;
								}
							} catch (Exception e) {
								checkColumn12 = false;
							}
							if (!checkColumn12) {
								isExistError = true;
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "12",
										"Ngày bắt đầu không hợp lệ!");
								errorList.add(errorDTO);
							}
						} else if (cell.getColumnIndex() == 13) {
							try {
								Date endDate = dateFormat.parse(formatter.formatCellValue(cell));
								if (validateDate(formatter.formatCellValue(cell))) {
									newObj.setEndDate(endDate);
								} else {
									checkColumn13 = false;
								}

							} catch (Exception e) {
								checkColumn13 = false;
							}
							if (!checkColumn13) {
								isExistError = true;
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "13",
										" Ngày kết thúc không hợp lệ!");
								errorList.add(errorDTO);
							}
						} else if (cell.getColumnIndex() == 14) {
							try {
								String descriptionHSHC = formatter.formatCellValue(cell).trim();
								if (descriptionHSHC.length() <= 1000) {
									newObj.setDescription(descriptionHSHC);
								} else {
									checkColumn14 = false;
									isExistError = true;
								}
							} catch (Exception e) {
								checkColumn14 = false;
							}
							if (!checkColumn14) {
								isExistError = true;
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "14",
										" Ghi chú vượt quá 1000 ký tự!");
								errorList.add(errorDTO);
							}
						}
					}
				}
				if (!isExistError) {
					newObj.setCntContract(formatter.formatCellValue(row.getCell(6)) != null
							? formatter.formatCellValue(row.getCell(6))
							: "");
					workLst.add(newObj);
				}
			}
		}
		if (isExistError) {
			workLst = new ArrayList<ConstructionTaskDetailDTO>();
			ConstructionTaskDetailDTO objErr = new ConstructionTaskDetailDTO();
			objErr.setErrorList(errorList);
			objErr.setMessageColumn(15);
			objErr.setErrorFilePath(UEncrypt.encryptFileUploadPath(filePath));
			objErr.setFilePathError(UEncrypt.encryptFileUploadPath(filePath));
			workLst.add(objErr);
		}
		workbook.close();
		return workLst;
	}

	public static Long getCurrentTimeStampMonth(Date date) {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");// dd/MM/yyyy
		String strDate = sdfDate.format(date);
		String res = strDate.substring(5, 7);
		return Long.parseLong(res);
	}

	public static Long getCurrentTimeStampYear(Date date) {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");// dd/MM/yyyy
		String strDate = sdfDate.format(date);
		String res = strDate.substring(0, 4);
		return Long.parseLong(res);
	}

	boolean validateDate(String date) {
		String dateBreaking[] = date.split("/");
		if (Integer.parseInt(dateBreaking[1]) > 12) {
			return false;
		}
		if (Integer.parseInt(dateBreaking[2]) < (new Date()).getYear() + 1900) {
			return false;
		}
		if (Integer.parseInt(dateBreaking[0]) > 31) {
			return false;
		}
		SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
		sdfrmt.setLenient(false);
		try {
			Date javaDate = sdfrmt.parse(date);
			// System.out.println(date+" is valid date format");
		} catch (Exception e) {
			// System.out.println(date+" is Invalid Date format");
			return false;
		}
		return true;
	}

	// Huypq_20181025-start-chart
	public List<ConstructionTaskDTO> getDataChart(ConstructionTaskDTO obj) {
		List<ConstructionTaskDTO> listChart = Lists.newArrayList();
		List<ConstructionTaskDTO> listChartHashSet = Lists.newArrayList();
		List<ConstructionTaskDTO> lstDay = constructionTaskDAO.listDay(obj);

		List<ConstructionTaskDTO> listData = constructionTaskDAO.getDataChart(obj);
		for (int i = 0; i < lstDay.size(); i++) {
			ConstructionTaskDTO newObj = new ConstructionTaskDTO();
			for (int j = 0; j < listData.size(); j++) {

				String dayData = listData.get(j).getStartDateChart();
				String day = lstDay.get(i).getStartDateChart();
				if (dayData.equals(day)) {

					newObj.setStartDateChart(listData.get(j).getStartDateChart());
					if (listData.get(j).getTypeName().equals("KH hoan cong")) {
						newObj.setQuantityKhHc(listData.get(j).getQuantityChart());
					}
					if (listData.get(j).getTypeName().equals("KH san luong")) {
						newObj.setQuantityKhSl(listData.get(j).getQuantityChart());
					}
					if (listData.get(j).getTypeName().equals("TH san luong")) {
						newObj.setQuantityThSl(listData.get(j).getQuantityChart());
					}
					if (listData.get(j).getTypeName().equals("TH hoan cong")) {
						newObj.setQuantityThHc(listData.get(j).getQuantityChart());

					}
				}
			}
			listChart.add(newObj);
		}

		return listChart;
	}

	public List<ConstructionTaskDTO> getDataChartAcc(ConstructionTaskDTO obj) {
		List<ConstructionTaskDTO> listChart = Lists.newArrayList();
		List<ConstructionTaskDTO> listChartHashSet = Lists.newArrayList();
		List<ConstructionTaskDTO> lstDay = constructionTaskDAO.listDayAcc(obj);

		List<ConstructionTaskDTO> listData = constructionTaskDAO.getDataChartAcc(obj);
		for (int i = 0; i < lstDay.size(); i++) {
			ConstructionTaskDTO newObj = new ConstructionTaskDTO();
			for (int j = 0; j < listData.size(); j++) {

				String dayData = listData.get(j).getStartDateChart();
				String day = lstDay.get(i).getStartDateChart();
				if (dayData.equals(day)) {

					newObj.setStartDateChart(listData.get(j).getStartDateChart());
					if (listData.get(j).getTypeName().equals("KH hoan cong")) {
						newObj.setQuantityKhHc(listData.get(j).getQuantityChart());
					}
					if (listData.get(j).getTypeName().equals("KH san luong")) {
						newObj.setQuantityKhSl(listData.get(j).getQuantityChart());
					}
					if (listData.get(j).getTypeName().equals("TH san luong")) {
						newObj.setQuantityThSl(listData.get(j).getQuantityChart());
					}
					if (listData.get(j).getTypeName().equals("TH hoan cong")) {
						newObj.setQuantityThHc(listData.get(j).getQuantityChart());

					}
				}
			}
			listChart.add(newObj);
		}

		return listChart;
	}

	// Huypq_20181025-end-chart

	//VietNT_20181128_start
	@SuppressWarnings("Duplicates")
	public ConstructionHSHCDTOHolder doImportConstructionHSHC(String filePath, Long sysUserId, String monthYear) throws Exception {
		List<RpHSHCDTO> dtoList = new ArrayList<>();
		List<ConstructionDTO> constructionDTOResults = new ArrayList<>();
		ConstructionHSHCDTOHolder res = new ConstructionHSHCDTOHolder();
		List<ExcelErrorDTO> errorList = new ArrayList<>();
		if (org.apache.commons.lang3.StringUtils.isEmpty(monthYear)) {
			throw new Exception("Chưa nhập tháng tìm kiếm!");
		}
		SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
		Date monthYearDate = sdf.parse(monthYear);


		try {
			File f = new File(filePath);
			XSSFWorkbook workbook = new XSSFWorkbook(f);
			XSSFSheet sheet = workbook.getSheetAt(0);

			DataFormatter formatter = new DataFormatter();
			int rowCount = 0;

			// prepare data for validate
			// get List rpHSHC
			ConstructionTaskDetailDTO criteria = new ConstructionTaskDetailDTO();
			criteria.setMonthYear(monthYearDate);
            List<RpHSHCDTO> rpHSHCs = rpConstructionHSHCDAO.doSearchHSHCForImport(criteria);

			for (Row row : sheet) {
				rowCount++;
				// data start from row 3
				if (rowCount < 3) {
					continue;
				}

				// check required field empty
				if (!this.isRequiredDataExist(row, errorList, formatter)) {
                    continue;
                }
				// ma cong trinh
				String constructionCode = formatter.formatCellValue(row.getCell(6)).trim();
				// gia tri phe duyet
				String completeValueStr = formatter.formatCellValue(row.getCell(9)).trim();
				// Phê duyệt/Từ chối: 1 = null, 2 = 1, 3 = 2
				String completeStateStr = formatter.formatCellValue(row.getCell(11)).trim();
				// Lý do từ chối
				String description = formatter.formatCellValue(row.getCell(12)).trim();
//				hoanm1_20181218_start
				String dateCompleteImport = formatter.formatCellValue(row.getCell(13)).trim();
//				hoanm1_20181218_end

				// validate constructionCode exist
				// find matching record in list get from db
				RpHSHCDTO dtoResult = rpHSHCs.stream()
						.filter(dto -> dto.getConstructionCode().toUpperCase().equals(constructionCode.toUpperCase()))
						.findFirst().orElse(null);
				ConstructionDTO constructionDTO = new ConstructionDTO();
//				ConstructionDTO constructionDTO = constructionDTOS.stream()
//						.filter(dto -> dto.getCode().toUpperCase().equals(constructionCode.toUpperCase()))
//						.findFirst().orElse(null);

				if (dtoResult != null) {
					// record exits, begin validate
					Date completedDate = new Date(dtoResult.getDateComplete().getTime());

					int errorCol = 8;
					long completeValue = this.getValidLongTypeData(rowCount, completeValueStr, errorList, errorCol);
					completeValue = completeValue == 0 ? dtoResult.getCompleteValuePlan() : completeValue;

					//long completeState = this.getValidLongTypeData(rowCount, completeStateStr, errorList, errorCol);
//					hoanm1_20181218_start
					this.validateCompleteState(rowCount, completeStateStr, completedDate, description, errorList,dateCompleteImport);
//					hoanm1_20181218_end
					long completeState = Long.parseLong(completeStateStr);

					// if no error, create DTO
					if (errorList.size() == 0) {
						// after validate, completeState can only be 1 or 2
						// update result dto
						if (completeState == 1) {
							long sumCompleteValue = rpConstructionHSHCDAO.sumCompleteValueByConsCode(dtoResult.getConstructionCode());
//							hoanm1_20181218_start
							this.updateDtoApprove(dtoResult, constructionDTO, completeValue, sumCompleteValue, sysUserId,dateCompleteImport);
						}
						else {
							this.updateDtoDenied(dtoResult, constructionDTO, description, sysUserId,dateCompleteImport);
						}
//						hoanm1_20181218_end
						dtoList.add(dtoResult);
						constructionDTOResults.add(constructionDTO);
					}

				} else {
					// record not found, add error
					ExcelErrorDTO errorDTO = this.createError(rowCount, colAlias.get(6), colNameHSHC.get(6) + " không ở trạng thái chờ phê duyệt");
					errorList.add(errorDTO);
				}
			}

			if (errorList.size() > 0) {
				String filePathError = UEncrypt.encryptFileUploadPath(filePath);
				this.doWriteError(errorList, dtoList, filePathError, 14);
			}
			workbook.close();

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			ExcelErrorDTO errorDTO = createError(0, "", e.toString());
			errorList.add(errorDTO);
			String filePathError = null;

			try {
				filePathError = UEncrypt.encryptFileUploadPath(filePath);
			} catch (Exception ex) {
				LOGGER.error(e.getMessage(), e);
				errorDTO = createError(0, "", ex.toString());
				errorList.add(errorDTO);
			}
			this.doWriteError(errorList, dtoList, filePathError, 14);
		}

		res.setRpHSHCDTOS(dtoList);
		res.setConstructionDTOS(constructionDTOResults);

		return res;
	}

	@SuppressWarnings("Duplicates")
	private void doWriteError(List<ExcelErrorDTO> errorList, List<RpHSHCDTO> dtoList, String filePathError, int errColumn) {
		dtoList.clear();

		RpHSHCDTO errorContainer = new RpHSHCDTO();
		errorContainer.setErrorList(errorList);
		errorContainer.setMessageColumn(errColumn); // cột dùng để in ra lỗi
		errorContainer.setFilePathError(filePathError);

		dtoList.add(errorContainer);
	}

	/**
	 * Update DTO data if approve
	 * @param dtoResult		record found
	 * @param completeValue	completeValue from Import Data
	 */
//	hoanm1_20181218_start
	private void updateDtoApprove(RpHSHCDTO dtoResult, ConstructionDTO consDto, long completeValue, long sumCompleteValue, Long sysUserId,String dateCompleteImport) {
		dtoResult.setCompleteValue(completeValue*1000000);
//		dtoResult.setCompleteValuePlan(completeValuePlan);
		dtoResult.setCompleteState(2L);
		Date today = new Date();
		dtoResult.setCompleteUpdateDate(today);
		dtoResult.setReceiveRecordsDate(today);
		dtoResult.setCompleteUserUpdate(sysUserId);
		dtoResult.setDateCompleteTC(dateCompleteImport);

		consDto.setCode(dtoResult.getConstructionCode());
		consDto.setReceiveRecordsDate(today);
		consDto.setCompleteApprovedUpdateDate(today);
		consDto.setCompleteApprovedValue(sumCompleteValue + completeValue*1000000);
		consDto.setCompleteApprovedState(2L);
		consDto.setCompleteApprovedUserId(sysUserId.toString());
		consDto.setDateCompleteTC(dateCompleteImport);
	}
	/**
	 * Update DTO data if denied
	 * @param dtoResult		record found
	 * @param description	description for denied
	 */
	private void updateDtoDenied(RpHSHCDTO dtoResult, ConstructionDTO consDto, String description, Long sysUserId,String dateCompleteImport) {
		dtoResult.setCompleteValue(0L);
		dtoResult.setCompleteState(3L);
		dtoResult.setDateCompleteTC(dateCompleteImport);

		Date today = new Date();
		dtoResult.setCompleteUpdateDate(today);
		dtoResult.setApproveCompleteDescription(description);
		dtoResult.setCompleteUserUpdate(sysUserId);

		consDto.setApproveCompleteDescription(description);
		consDto.setCode(dtoResult.getConstructionCode());
		consDto.setCompleteApprovedUpdateDate(today);
		consDto.setCompleteApprovedUserId(sysUserId.toString());
		consDto.setDateCompleteTC(dateCompleteImport);
		consDto.setCompleteApprovedState(3L);
	}
//	hoanm1_20181218_end
	/**
	 * Convert String to Date type, if string invalid add error, if string empty return null
	 * @param rowCount		current row
	 * @param dateStr		String contain date
	 * @param sdf			SimpleDateFormat obj
	 * @param errorList		list error
	 * @param errorCol		data's column
	 * @return Date type
	 */
    private Date getValidDateData(int rowCount, String dateStr, SimpleDateFormat sdf, List<ExcelErrorDTO> errorList, int errorCol) {
		Date date = null;
		if (this.validateString(dateStr)) {
			try {
				date = sdf.parse(dateStr);
			} catch (Exception e) {
				errorList.add(this.createError(rowCount, colAlias.get(errorCol), colNameHSHC.get(errorCol) + " sai kiểu dữ liệu"));
			}
		}
		return date;
	}

	/**
	 * Validate field completeDate is <= 5th of next month
	 * @param rowCount		current row
	 * @param completeDate	Complete Date
	 * @param errorList		list error
	 */
	private void validateCompleteDate(int rowCount, Date completeDate, List<ExcelErrorDTO> errorList) {
		LocalDateTime from = LocalDateTime.fromDateFields(completeDate);
		// extract day, month, year from completeDate
		int expiredDay = 5;
		int expiredMonth = from.getMonthOfYear() + 1;
		int expiredYear = from.getYear();

		if (from.getMonthOfYear() == 12) {
			expiredMonth = 1;
			expiredYear++;
		}

		// to create expiredDate(5th of next month)
		Date expiredDate = new LocalDateTime()
				.withDate(expiredYear, expiredMonth, expiredDay)
				.withHourOfDay(23)
				.withMinuteOfHour(59)
				.toDate();

		// compare now with expiredDate
		Date now = new Date();
		if (now.after(expiredDate)) {
			ExcelErrorDTO errorDTO = createError(rowCount, colAlias.get(1),
					colNameHSHC.get(1) + " đã quá thời gian phê duyệt");
			errorList.add(errorDTO);
		}
	}

	/**
	 * Convert Long type from String, if string invalid add error, if string empty return 0
	 * @param rowCount		current row
	 * @param strLong		String contain data
	 * @param errorList		list error
	 * @param errorCol		data's column
	 * @return Long type number
	 */
	private long getValidLongTypeData(int rowCount, String strLong, List<ExcelErrorDTO> errorList, int errorCol) {
		long numLong = 0;
		if (this.validateString(strLong)) {
			try {
				numLong = Long.parseLong(strLong);
			} catch (Exception e) {
				ExcelErrorDTO errorDTO = this.createError(rowCount, colAlias.get(errorCol), colNameHSHC.get(errorCol) + " sai kiểu dữ liệu");
				errorList.add(errorDTO);
			}
		}
		return numLong;
	}

	/**
	 * Validate required field base on completeState.
	 * 1 == approve: required receiveRecordDate
	 * 2 == denied: required description
	 * else reject input
	 *
	 * @param rowCount				current row
	 * @param completeStateStr		completeState, type Long, 1 = approve, 2 = denied
	 * @param completedDate			completeDate, for method validateCompleteState()
	 * @param description			description, required when 2
	 * @param errorList				list error
	 */
//	hoanm1_20181218_start
	private void validateCompleteState(int rowCount, String completeStateStr, Date completedDate, String description, List<ExcelErrorDTO> errorList,String dateCompleteImport) {
		if (!completeStateStr.equals("1") && !completeStateStr.equals("2")) {
			// completeState invalid(not equal to 1 or 2)
			errorList.add(this.createError(rowCount, colAlias.get(11), colNameHSHC.get(11) + " giá trị không hợp lệ(1: Chấp nhận, 2: Từ chối)"));
		} else {
//			hoanm1_20190610_start_comment
//			this.validateCompleteDate(rowCount, completedDate, errorList);
//			hoanm1_20190610_end_comment
			// if approve -> do nothing
			// if denied check description exist
			if (completeStateStr.equals("2")) {
				if (!this.validateString(description)) {
					errorList.add(this.createError(rowCount, colAlias.get(12), colNameHSHC.get(12) + " không được bỏ trống khi Từ chối" ));
				}
			}
		}
		if (!this.validateString(dateCompleteImport)) {
			errorList.add(this.createError(rowCount, colAlias.get(13), colNameHSHC.get(13) + " không được bỏ trống" ));
		}
	}
//	hoanm1_20181218_end
	/**
	 * Check if required data exist in cell
	 * @param row		row num
	 * @param errorList	list of errors
	 * @param formatter	POI formatter
	 * @return True if all required data exist
	 */
	private boolean isRequiredDataExist(Row row, List<ExcelErrorDTO> errorList, DataFormatter formatter) {
		int errCount = 0;
		for (int colIndex : requiredColHSHC) {
			if (!this.validateString(formatter.formatCellValue(row.getCell(colIndex)))) {
				ExcelErrorDTO errorDTO = this.createError(row.getRowNum() + 1, colAlias.get(colIndex), colNameHSHC.get(colIndex) + " chưa nhập");
                errorList.add(errorDTO);
				errCount++;
			}
		}
		return errCount == 0;
	}

    public void doUpdateHSHC(List<RpHSHCDTO> dtos, List<ConstructionDTO> consDto) {
		for (int i = 0; i < dtos.size(); i++) {
			try {
				constructionDAO.updateConstructionHSHC(consDto.get(i));
				rpConstructionHSHCDAO.update(dtos.get(i).toModel());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("Duplicates")
	public String makeTemplateHSHC(ConstructionTaskDetailDTO dtoSearch) throws Exception {
		String fileName = "";
		if(dtoSearch.getType() != null){
			if("1".equals(dtoSearch.getType())){
				fileName = "BieuMau_DoanhThu_NgoaiOS.xlsx";
			}
		} else {
			fileName = "BieuMau_HSHC.xlsx";
		}
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + fileName ));
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
				udir.getAbsolutePath() + File.separator + fileName );

		List<RpHSHCDTO> data = rpConstructionHSHCDAO.doSearchHSHCForImport(dtoSearch);
		List<DepartmentDTO> listGroup;
		HashMap<Long, String> mapIdNameGroup = new HashMap<>();
		if (data != null && !data.isEmpty()) {
			List<Long> ids = data.stream().map(RpHSHCDTO::getSysGroupId).collect(Collectors.toList());
			listGroup = constructionTaskDAO.getListGroupByIds(ids);
			listGroup.forEach(group -> mapIdNameGroup.put(group.getDepartmentId(), group.getName()));
		}

		XSSFSheet sheet = workbook.getSheetAt(0);
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);

			XSSFCellStyle styleNumber = ExcelUtils.styleNumber(sheet);
			styleNumber.setDataFormat(workbook.createDataFormat().getFormat("#,##0.000"));
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);

			XSSFCellStyle styleDate = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();

			styleDate.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));

			int i = 2;
			for (RpHSHCDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell;
				//stt
				cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 2));
				cell.setCellStyle(style);

				// ngay hoan thanh
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue(dto.getDateComplete());
				cell.setCellStyle(styleDate);

				// don vi thuc hien (groupId)
				String groupName = mapIdNameGroup.get(dto.getSysGroupId());
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue(null == groupName ? dto.getSysGroupId().toString() : groupName);
				cell.setCellStyle(style);

				// ma tinh
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue(dto.getCatProvinceCode());
				cell.setCellStyle(style);
				
				// hoanm1_20181219_start ma nha tram
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue(dto.getCatStationHouseCode());
				cell.setCellStyle(style);
//				hoanm1_20181219_end
				// ma tram
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue(dto.getCatStationCode());
				cell.setCellStyle(style);

				// ma cong trinh
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue(dto.getConstructionCode());
				cell.setCellStyle(style);

				// max hop dong
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue(dto.getCntContractCode());
				cell.setCellStyle(style);

				// completeValue plan
				cell = row.createCell(8, CellType.NUMERIC);
				cell.setCellValue(dto.getCompleteValuePlan() == null ? 0 : dto.getCompleteValuePlan()/1000000);
				cell.setCellStyle(style);

				// completeValue
				cell = row.createCell(9, CellType.NUMERIC);
				cell.setCellValue(dto.getCompleteValue() == null ? 0 : dto.getCompleteValue());
				cell.setCellStyle(style);

				// workitem code
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue(dto.getWorkItemCode());
				cell.setCellStyle(style);

				//complete state
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);

				// description
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				
//				hoanm1_20181218_start
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
//				hoanm1_20181218_end
			}
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + fileName);
		return path;
	}

	//VietNT_end
	// Huypq_20181025-end-chart
	public ConstructionDetailDTO getUserUpdate(Long rpHshcId) {
		return constructionTaskDAO.getUserUpdate(rpHshcId);
	}
	// Huypq_20181025-end-
	//VietNT_20181207_start
	public DataListDTO getConstructionByStationId(ConstructionDTO obj) {
		List<ConstructionDetailDTO> ls = constructionDAO.getConstructionByStationId(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
	//VietNT_end
	
	//tatph-start-20/12/2019
	public List<SysUserDetailCOMSDTO> searchPerformerV2(SysUserDetailCOMSDTO obj, Long sysGroupId,
			HttpServletRequest request) {
		List<SysUserDetailCOMSDTO> ls = new ArrayList<SysUserDetailCOMSDTO>();
//		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
//				Constant.AdResourceKey.WORK_PROGRESS, request);
//		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
//		if (groupIdList != null && !groupIdList.isEmpty())
			ls = constructionDAO.doSearchPerformerV2(obj);
		return ls;
	}
	//tatph-end-20/12/2019
	
	//Huypq-03042020-start
	public List<RevokeCashMonthPlanDTO> importThuHoiDT(String fileInput, String filePath, HttpServletRequest request, Long month, Long year, Long sysGroupId)
			throws Exception {
		RevokeCashMonthPlanDTO plan = constructionTaskDAO.getMonthPlanId(month.toString(), year.toString(), sysGroupId);
		if(plan==null) {
			throw new IllegalArgumentException("Kế hoạch tháng chưa trình ký hoặc không tồn tại !");
		}
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		List<RevokeCashMonthPlanDTO> workLst = Lists.newArrayList();
		List<ExcelErrorDTO> errorList = new ArrayList<ExcelErrorDTO>();

		File f = new File(fileInput);
		XSSFWorkbook workbook = new XSSFWorkbook(f);
		XSSFSheet sheet = workbook.getSheetAt(0);
		DataFormatter formatter = new DataFormatter();
		int count = 0;

		boolean isExistError = false;
		
		RevokeCashMonthPlanDTO consWi = new RevokeCashMonthPlanDTO();
		consWi.setPage(null);
		consWi.setPageSize(null);

		List<String> lstContractCode = new ArrayList<>();
		List<String> lstConstructionCode = new ArrayList<>();
		List<String> lstContractConstr = new ArrayList<>();
		int rowCount = 0;
		for (Row row : sheet) {
			rowCount++;
			if (rowCount >= 3 && !ValidateUtils.checkIfRowIsEmpty(row)) {
				String contract = formatter.formatCellValue(row.getCell(1)).trim();
				String constr = formatter.formatCellValue(row.getCell(2)).trim();
				lstContractCode.add(contract.toUpperCase());
				lstConstructionCode.add(constr.toUpperCase());
				lstContractConstr.add(contract.toUpperCase() + "+" + constr.toUpperCase());
			}
		}
		
		HashMap<String, CntContractDTO> mapContract = new HashMap<>();
		if(lstContractCode.size()>0) {
			List<CntContractDTO> lstContract = revokeCashMonthPlanDAO.getContractOutOS(lstContractCode);
			for(CntContractDTO contract : lstContract) {
				mapContract.put(contract.getCode().toUpperCase().trim(), contract);
			}
		}
		
		HashMap<String, ConstructionDTO> mapConstr = new HashMap<>();
		if(lstContractCode.size()>0) {
			List<ConstructionDTO> lstConstr = revokeCashMonthPlanDAO.getConstructionCodeByList(lstConstructionCode);
			for(ConstructionDTO constr : lstConstr) {
				mapConstr.put(constr.getCode().toUpperCase().trim(), constr);
			}
		}
		
		HashMap<String, RevokeCashMonthPlanDTO> mapContractConstr = new HashMap<>();
		if(lstContractConstr.size()>0) {
			List<RevokeCashMonthPlanDTO> lstConConstr = revokeCashMonthPlanDAO.getContractConstr(lstContractConstr);
			for(RevokeCashMonthPlanDTO constr : lstConConstr) {
				mapContractConstr.put(constr.getCntContractCode().toUpperCase().trim() + "+" + constr.getConstructionCode().toUpperCase().trim(), constr);
			}
		}
		
		HashMap<String, String> checkDupBillCode = new HashMap<>();
		HashMap<String, String> checkDupConsCode = new HashMap<>();
		
		RevokeCashMonthPlanDTO sysGroup = revokeCashMonthPlanDAO.getProvinceBySysGroup(user.getVpsUserInfo().getSysGroupId());

		HashMap<String, RevokeCashMonthPlanDTO> mapStatusRevoke = new HashMap<>();
		HashMap<String, RevokeCashMonthPlanDTO> mapConsBill = new HashMap<>();
		HashMap<String, RevokeCashMonthPlanDTO> mapContractBill = new HashMap<>();
		List<RevokeCashMonthPlanDTO> lstRevoke = constructionTaskDAO.getRevokeMonthPlanIdByListCons(month.toString(), year.toString(), sysGroupId, lstConstructionCode);
		for(RevokeCashMonthPlanDTO revo : lstRevoke) {
			mapStatusRevoke.put(revo.getConstructionCode().toUpperCase().trim(), revo);
			mapContractBill.put(revo.getBillCode(), revo);
		}
		
		for (Row row : sheet) {
			count++;
			if (count >= 3 && checkIfRowIsEmpty(row))
				continue;
			if (count >= 3) {
				String contractCode = formatter.formatCellValue(row.getCell(1)).trim();
				String constructionCode = formatter.formatCellValue(row.getCell(2)).trim();
				String billCode = formatter.formatCellValue(row.getCell(3)).trim();
				String createBillDate = formatter.formatCellValue(row.getCell(4)).trim();
				String billValue = formatter.formatCellValue(row.getCell(5)).trim();

				StringBuilder errorMesg = new StringBuilder();
				RevokeCashMonthPlanDTO obj = new RevokeCashMonthPlanDTO();

				// Hợp đồng
				if (validateString(contractCode)) {
					CntContractDTO dto = mapContract.get(contractCode.toUpperCase());
					if(dto==null) {
						isExistError = true;
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(1),
								"Mã hợp đồng không tồn tại hoặc không phải hợp đồng đầu ra");
						errorList.add(errorDTO);
					} else {
						obj.setCntContractCode(contractCode);
					}
				} else {
					isExistError = true;
					ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(1),
							"Mã hợp đồng không được bỏ trống");
					errorList.add(errorDTO);
				}
				
				// Công trình
				if (validateString(constructionCode)) {
					ConstructionDTO dto = mapConstr.get(constructionCode.toUpperCase());
					RevokeCashMonthPlanDTO contr = mapContractConstr.get(contractCode.toUpperCase() + "+" +constructionCode.toUpperCase());
					if(checkDupConsCode.size()==0) {
						if(dto==null) {
							isExistError = true;
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(2),
									"Mã công trình không tồn tại");
							errorList.add(errorDTO);
						} else if(contr==null) {
							isExistError = true;
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(2),
									"Mã công trình chưa gán với hợp đồng đầu ra");
							errorList.add(errorDTO);
						} else {
							obj.setConstructionId(dto.getConstructionId());
							obj.setConstructionCode(constructionCode);
							obj.setCatStationId(dto.getCatStationId());
							obj.setCatStationCode(dto.getCatStationCode());
						}
						checkDupConsCode.put(constructionCode, constructionCode);
					} else {
						if (checkDupConsCode.get(constructionCode) != null) {
							isExistError = true;
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(2),
									"Mã công trình đã tồn tại ở trên (Mã công trình là duy nhất)");
							errorList.add(errorDTO);
						} else {
							if(dto==null) {
								isExistError = true;
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(2),
										"Mã công trình không tồn tại");
								errorList.add(errorDTO);
							} else if(contr==null) {
								isExistError = true;
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(2),
										"Mã công trình chưa gán với hợp đồng đầu ra");
								errorList.add(errorDTO);
							} else {
								obj.setConstructionId(dto.getConstructionId());
								obj.setConstructionCode(constructionCode);
								obj.setCatStationId(dto.getCatStationId());
								obj.setCatStationCode(dto.getCatStationCode());
							}
							checkDupConsCode.put(constructionCode, constructionCode);
						}
						
					}
					
				} else {
					isExistError = true;
					ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(2),
							"Mã công trình không được bỏ trống");
					errorList.add(errorDTO);
				}

				// Số hoá đơn
				if (validateString(billCode)) {
					if(checkDupBillCode.size()==0) {
						if(obj.getConstructionCode()!= null && mapStatusRevoke.get(obj.getConstructionCode().toUpperCase().trim())!=null) {
							if(mapStatusRevoke.get(obj.getConstructionCode().toUpperCase().trim()).getStatus()!=1l) {
								isExistError = true;
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(3),
										"Số hoá đơn không đúng trạng thái để cập nhật ");
								errorList.add(errorDTO);
							} else if(obj.getCntContractCode()!=null 
									&& mapContractBill.get(obj.getBillCode())!=null 
									&& mapContractBill.get(obj.getBillCode()).getCntContractCode() != obj.getCntContractCode()) {
								isExistError = true;
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(3),
										"Số hoá đơn đã ứng với hợp đồng khác ");
								errorList.add(errorDTO);
							} else if(!mapStatusRevoke.get(obj.getConstructionCode().toUpperCase().trim()).getBillCode().equals(billCode)) {
								isExistError = true;
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(3),
										"Số hoá đơn của công trình " + obj.getConstructionCode() + " đã tồn tại với mã "+ mapStatusRevoke.get(obj.getConstructionCode().toUpperCase().trim()).getBillCode());
								errorList.add(errorDTO);
							}
							else {
								obj.setBillCode(billCode);
								if(obj.getCntContractCode()!=null) {
									checkDupBillCode.put(billCode, obj.getCntContractCode());
								}
							}
						} else {
							obj.setBillCode(billCode);
							if(obj.getCntContractCode()!=null) {
								checkDupBillCode.put(billCode, obj.getCntContractCode());
							}
						}
					} else {
						if(obj.getCntContractCode()!=null 
								&& checkDupBillCode.get(billCode)!=null 
								&& checkDupBillCode.get(billCode)!=obj.getCntContractCode()) {
							isExistError = true;
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(3),
									"Số hoá đơn đã được gán với hợp đồng " + checkDupBillCode.get(billCode));
							errorList.add(errorDTO);
						} else {
							if(obj.getConstructionCode()!= null && mapStatusRevoke.get(obj.getConstructionCode().toUpperCase().trim())!=null) {
								if(mapStatusRevoke.get(obj.getConstructionCode().toUpperCase().trim()).getStatus()!=1l) {
									isExistError = true;
									ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(3),
											"Số hoá đơn không đúng trạng thái để cập nhật ");
									errorList.add(errorDTO);
								} else if(obj.getCntContractCode()!=null 
										&& mapContractBill.get(obj.getBillCode())!=null 
										&& mapContractBill.get(obj.getBillCode()).getCntContractCode() != obj.getCntContractCode()) {
									isExistError = true;
									ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(3),
											"Số hoá đơn đã ứng với hợp đồng khác ");
									errorList.add(errorDTO);
								} else if(!mapStatusRevoke.get(obj.getConstructionCode().toUpperCase().trim()).getBillCode().equals(billCode)) {
									isExistError = true;
									ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(3),
											"Số hoá đơn của công trình " + obj.getConstructionCode() + " đã tồn tại với mã "+ mapStatusRevoke.get(obj.getConstructionCode().toUpperCase().trim()).getBillCode());
									errorList.add(errorDTO);
								}
								else {
									obj.setBillCode(billCode);
									if(obj.getCntContractCode()!=null) {
										checkDupBillCode.put(billCode, obj.getCntContractCode());
									}
								}
							} else {
								obj.setBillCode(billCode);
								if(obj.getCntContractCode()!=null) {
									checkDupBillCode.put(billCode, obj.getCntContractCode());
								}
							}
						}
					}
				} else {
					isExistError = true;
					ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(3),
							"Số hoá đơn không được bỏ trống");
					errorList.add(errorDTO);
				}

				// Ngày hoá đơn
				if (validateString(createBillDate)) {
					if (ValidateUtils.isValidFormat("dd/MM/yyyy", createBillDate) == null) {
						isExistError = true;
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(4),
								"Ngày lập hoá đơn sai định dạng 'dd/MM/yyyy'");
						errorList.add(errorDTO);
					} else {
						obj.setCreatedBillDate(ValidateUtils.isValidFormat("dd/MM/yyyy", createBillDate));
					}
				} else {
					isExistError = true;
					ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(4),
							"Ngày lập hoá đơn không được bỏ trống");
					errorList.add(errorDTO);
				}
				// Giá trị hoá đơn
				if (validateString(billValue)) {
					try {
						if (Double.compare(Double.parseDouble(billValue), 0D) < 0) {
							isExistError = true;
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(5),
									"Giá trị hoá đơn phải lớn hơn 0");
							errorList.add(errorDTO);
						} else {
							if(Double.compare(Double.parseDouble(billValue), 1000000d) >= 0) {
								isExistError = true;
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(5),
										"Giá trị hoá đơn không hợp lệ ");
								errorList.add(errorDTO);
							} else {
								obj.setBillValue(Double.parseDouble(billValue));
							}
						}
					} catch (Exception e) {
						isExistError = true;
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(5),
								"Giá trị hoá đơn chỉ được nhập số");
						errorList.add(errorDTO);
					}
				} else {
					isExistError = true;
					ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(5),
							"Giá trị hoá đơn không được bỏ trống");
					errorList.add(errorDTO);
				}

				if(sysGroup!=null) {
					obj.setAreaCode(sysGroup.getAreaCode());
					obj.setProvinceCode(sysGroup.getProvinceCode());
				}
				
//				Cell cell1 = row.createCell(6);
//                cell1.setCellValue(errorMesg.toString());
				
                obj.setDetailMonthPlanId(plan.getDetailMonthPlanId());
                obj.setSysGroupId(sysGroupId);
                
				if (!isExistError) {
					workLst.add(obj);
				}
			}
		}

		if (isExistError) {
			workLst = new ArrayList<RevokeCashMonthPlanDTO>();
			RevokeCashMonthPlanDTO objErr = new RevokeCashMonthPlanDTO();
			objErr.setErrorList(errorList);
			objErr.setMessageColumn(6);
			objErr.setErrorFilePath(UEncrypt.encryptFileUploadPath(filePath));
			objErr.setFilePathError(UEncrypt.encryptFileUploadPath(filePath));
			workLst.add(objErr);
		}
		workbook.close();
		return workLst;

	}
	
	public List<RevokeCashMonthPlanBO> getRevokeMonthPlanIdBySysGroupAndDate(String month, String year, Long sysGroupId){
		return constructionTaskDAO.getRevokeMonthPlanIdBySysGroupAndDate(month, year, sysGroupId);
	}
	
	public Long saveRevokeImport(HttpServletRequest request, RevokeCashMonthPlanDTO obj) {
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		obj.setCreatedUserId(user.getSysUserId());
		obj.setCreatedDate(new Date());
		obj.setSignState("3");
		obj.setStatus(1l);
		obj.setSysGroupId(obj.getSysGroupId());
		return revokeCashMonthPlanDAO.saveObject(obj.toModel());
	}
	
	public Long updateRevokeImport(HttpServletRequest request, RevokeCashMonthPlanDTO obj) {
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		obj.setUpdatedUserId(user.getSysUserId());
		obj.setUpdatedDate(new Date());
		return revokeCashMonthPlanDAO.updateObject(obj.toModel());
	}
	
	public List<ConstructionTaskDetailDTO> importSanLuong(String fileInput, String filePath, HttpServletRequest request, String month, String year, Long sysGroupId) throws Exception {
    	KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession"); 
        List<ConstructionTaskDetailDTO> workLst = new ArrayList<ConstructionTaskDetailDTO>();
        File f = new File(fileInput);
        ZipSecureFile.setMinInflateRatio(-1.0d);//hoangnh add
        XSSFWorkbook workbook = new XSSFWorkbook(f);
        XSSFSheet sheet = workbook.getSheetAt(0);
        DataFormatter formatter = new DataFormatter();
        DecimalFormat df = new DecimalFormat("#.###");
        DecimalFormat longf = new DecimalFormat("#");
        boolean isExistError = false;
        
        List<ExcelErrorDTO> errorList = new ArrayList<ExcelErrorDTO>();
        
        int counts = 0;
        List<String> listConstrExcel = new ArrayList<>();
        List<String> listWorkItemExcel = new ArrayList<>();
        List<String> listUserLoginExcel = new ArrayList<>();
        List<String> listProject  = new ArrayList<>();
        for (Row rows : sheet) {
        	counts++;
            if (counts > 3 && !ValidateUtils.checkIfRowIsEmpty(rows)) {
            	listConstrExcel.add(formatter.formatCellValue(rows.getCell(3)).trim().toUpperCase());
            	listWorkItemExcel.add(formatter.formatCellValue(rows.getCell(5)).trim().toUpperCase());
            	listUserLoginExcel.add(formatter.formatCellValue(rows.getCell(14)).trim().toUpperCase());
            	listProject.add(formatter.formatCellValue(rows.getCell(6)).trim().toUpperCase());
            }
        }
        
        List<CNTContractDTO> cntDto = detailMonthPlanOSDAO.getKeyValueTask(listConstrExcel);
        HashMap<String,Long> cntMap = new HashMap<String,Long>();
        for(CNTContractDTO dto: cntDto){
        	cntMap.put(dto.getCode().toUpperCase(),dto.getConstructionId());
        }
        
        Map<String, ConstructionDetailDTO> constructionMap = constructionDAO.getConstructionByCodeExcel(listConstrExcel);
        Map<String, WorkItemDetailDTO> workItemMap = workItemDAO.getWorkItemByCodeNewExcel(listWorkItemExcel);
        
        List<SysUserCOMSDTO> lstUser = detailMonthPlanOSDAO.getListSysUserEmailExcel(listUserLoginExcel);
        Map<String, SysUserCOMSDTO> userLoginMap = new HashMap<>();
        Map<String, SysUserCOMSDTO> userEmailMap = new HashMap<>();
        for(SysUserCOMSDTO userComs : lstUser) {
        	userLoginMap.put(userComs.getLoginName().toUpperCase().trim(), userComs);
        	userEmailMap.put(userComs.getEmail().toUpperCase().trim(), userComs);
        }
      
        XSSFCellStyle style = ExcelUtils.styleText(sheet);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        
        HashMap<String, Long> mapNguon = new HashMap<>();
        HashMap<String, Long> mapLoaiCt = new HashMap<>();
      //Huypq-10042020-start
        HashMap<String, String> mapCheckDup = new HashMap<>();
        HashMap<String, ConstructionTaskDTO> mapCheckDuplicateInDb = new HashMap<>();
        
        List<ConstructionTaskDTO> lstConsWi = constructionTaskDAO.getListConsWiCheckDuplicateDb(month, year, sysGroupId);
        for(ConstructionTaskDTO truc : lstConsWi) {
        	mapCheckDuplicateInDb.put(truc.getConstructionId() + "+" + truc.getWorkItemId(), truc);
        }
        //Huy-end
        
        HashMap<Long, String> mapSourceWork = new HashMap<>();
        HashMap<Long, String> mapConsType = new HashMap<>();
        List<ConstructionTaskDetailDTO> lstConsType = constructionTaskDAO.checkSourceWorkByConsId(listConstrExcel);
        for(ConstructionTaskDetailDTO dto : lstConsType) {
        	mapSourceWork.put(dto.getConstructionId(), dto.getSourceWork());
        	mapConsType.put(dto.getConstructionId(), dto.getConstructionType());
        }
        
        int count = 0;
        for (Row row : sheet) {
            count++;
            if (count > 3 && !ValidateUtils.checkIfRowIsEmpty(row)) {
                boolean checkColumn3 = true;
                boolean checkColumn4 = true;
                boolean checkColumn5 = true;
                boolean checkColumn6 = true;
                boolean checkColumn7 = true;
                boolean checkColumn8 = true;
                boolean checkColumn9 = true;
                boolean checkColumn14 = true;
                boolean checkColumn15 = true;
                boolean checkColumn16 = true;
                boolean checkColumn17 = true;
                boolean checkLength = true;
                StringBuilder errorMesg = new StringBuilder();
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String constructionCode = "";
                String wiName = "";
                String workItemType ="";
//                ConstructionTaskDetailDTO taskType = new ConstructionTaskDetailDTO();
                ConstructionTaskDetailDTO newObj = new ConstructionTaskDetailDTO();
                for (int i = 0; i < 18; i++) {
                    Cell cell = row.getCell(i);
                    if (cell != null) {
                        if (cell.getColumnIndex() == 3) {
                            try {
                                constructionCode = formatter.formatCellValue(row.getCell(3)).trim();

                                ConstructionDetailDTO obj = constructionMap.get(constructionCode.trim().toUpperCase());

                                if (obj.getConstructionId() == null) {
                                    checkColumn3 = false;
                                    isExistError = true;
                                    ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(3),
    										"Mã công trình không tồn tại !");
    								errorList.add(errorDTO);
                                }
                                newObj.setConstructionCode(obj.getCode());
                                newObj.setConstructionId(obj.getConstructionId());
                                newObj.setCatStationCode(obj.getCatStationCode());
                                newObj.setCatProvinceCode(obj.getCatProvince());
                                newObj.setConstructionName(obj.getName());
                                newObj.setProvinceName(obj.getProvinceName());
                                newObj.setCatConstructionTypeId(obj.getCatContructionTypeId());
                                newObj.setCheckHTCT(obj.getCheckHTCT() != null ?obj.getCheckHTCT() : 0L );
                            } catch (Exception e) {
                                checkColumn3 = false;
                                isExistError = true;
                                ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(3),
										"Mã công trình không hợp lệ!");
								errorList.add(errorDTO);
                            }
//                            if (!checkColumn3) {
//                                isExistError = true;
//                                ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(3),
//										"Mã công trình không hợp lệ!");
//								errorList.add(errorDTO);
//                            }

                        }
                        
                        else if (cell.getColumnIndex() == 5) {
                        
                        		  try {
                                      String name = formatter.formatCellValue(row.getCell(5)).trim();
                                      wiName = formatter.formatCellValue(row.getCell(5)).trim();
                                      	WorkItemDetailDTO obj =workItemMap.get(constructionCode.trim().toUpperCase()+'_'+name.trim().toUpperCase());
                                      	if(obj!=null) {
                                      	if (obj.getConstructionCode().trim().toUpperCase().equals(constructionCode.trim().toUpperCase())) {
                                          	if(obj.getStatus().equals("3")){
                                          		checkColumn5 = false;
                                                  isExistError = true;
                                                  ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(5),
                  										"Hạng mục đã được hoàn thành trong các tháng trước !");
                  								errorList.add(errorDTO);
                                          	}else{
                                          		if(newObj.getConstructionId()!=null) {
                                          			if(mapCheckDuplicateInDb.get(newObj.getConstructionId() + "+" + obj.getWorkItemId())!=null) {
                                          				checkColumn5 = false;
                                                        isExistError = true;
                                                        ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(5),
                        										"Hạng mục đã tồn tại trong kế hoạch tháng !");
                        								errorList.add(errorDTO);
                                          			} else {
                                          				newObj.setWorkItemId(obj.getWorkItemId());
                                                        newObj.setWorkItemName(obj.getName());
                                                        newObj.setWorkItemCode(obj.getCode());
                                          			}
                                          		} else {
                                          			newObj.setWorkItemId(obj.getWorkItemId());
                                                    newObj.setWorkItemName(obj.getName());
                                                    newObj.setWorkItemCode(obj.getCode());
                                          		}
                                          	}
                                          } else {
                                              checkColumn5 = false;
                                              isExistError = true;
                                              ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(5),
                										"Hạng mục không thuộc công trình !");
                								errorList.add(errorDTO);
                                          }
                                      	} else {
                                      		checkColumn5 = false;
                                            isExistError = true;
                                            ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(5),
            										"Hạng mục không thuộc công trình !");
            								errorList.add(errorDTO);
                                      	}
                                  } catch (Exception e) {
                                      checkColumn5 = false;
                                      isExistError = true;
                                      ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(5),
      										"Chưa nhập hạng mục hoặc tên hạng mục không tồn tại !");
      								errorList.add(errorDTO);
                                  }
//                                  if (!checkColumn5) {
//                                      isExistError = true;
//                                      ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(5),
//      										"Chưa nhập hạng mục hoặc tên hạng mục không tồn tại !");
//      								errorList.add(errorDTO);
//                                  }
                        	
                          

                        } else if(cell.getColumnIndex() == 6){
//                        	if(newObj.getCheckHTCT()==null) {
//                        		newObj.setCheckHTCT(0l);
//                        	}
                        		String name = formatter.formatCellValue(row.getCell(6)).trim();
                        		
                            	if(!StringUtils.isNullOrEmpty(name)){
                            		// Huypq-10042020-start check duplicate trong file
                            		if(!StringUtils.isNullOrEmpty(constructionCode) && !StringUtils.isNullOrEmpty(wiName)) {
                            			if (mapCheckDup.size() == 0) {
        									mapCheckDup.put(name + "+" + constructionCode + "+" + wiName, name);
        								} else {
        									String valueMap = mapCheckDup.get(name + "+" + constructionCode + "+" + wiName);
        									if (!StringUtils.isNullOrEmpty(valueMap)) {
        										checkColumn6 = false;
        										isExistError = true;
        										ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(6),
        	      										"Cặp Hợp đồng - Công trình - Hạng mục trong file import không được trùng nhau !");
        	      								errorList.add(errorDTO);
        									} else {
        										mapCheckDup.put(name + "+" + constructionCode + "+" + wiName, name);
        									}
        								}
                            		}
    								// Huy-end
                            		Long check  = null;
                            		String map = constructionCode.trim().toUpperCase() + "+" + name.trim().toUpperCase();
//                            		if(newObj.getCheckHTCT() != 1) {
//                            			check = cntMap.get(map.toUpperCase().trim());  
//                            		}else {
//                            			check = litMapPE.get(map.toUpperCase().trim()); 
//                            		}
                            			check = cntMap.get(map); 
                                		if(check != null){
                                			checkColumn6 = true;
                                			newObj.setCntContract(name);
                                		} else {
                                			isExistError = true;
                                			checkColumn6 = false;
                                			ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(6),
              										"Công trình chưa được gán vào hợp đồng đầu ra !");
              								errorList.add(errorDTO);
                                		}
                            		
                            		
                            	} else {
                            		isExistError = true;
                            		checkColumn6 = false;
                            		ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(6),
      										"Công trình không được để trống !");
      								errorList.add(errorDTO);
                            	}
//                            	if(!checkColumn6){
//                            		isExistError = true;
////                            		if(newObj.getCheckHTCT() != 1) {
////                            		 errorMesg.append("\nCông trình chưa được gán vào hợp đồng đầu ra!");
////                            		}else {
////                            			 errorMesg.append("\nCông trình và dự án không thuộc hạ tầng cho thuê!");
////                            		}
//                            		ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(6),
//      										"Công trình chưa được gán vào hợp đồng đầu ra !");
//      								errorList.add(errorDTO);
//                            	}
                        } else if (cell.getColumnIndex() == 7) {
                            try {
                                Double quantity = new Double(Double.parseDouble(formatter.formatCellValue(row.getCell(7)).trim()) * 1000000D);
                                if(quantity > 0){
                                	newObj.setQuantity(quantity);
                                } else {
                                	checkColumn7 = false;
                            		isExistError = true;
                                    ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(7),
      										"Giá trị không hợp lệ !");
      								errorList.add(errorDTO);
                                }
                                
                            } catch (Exception e) {
                                checkColumn7 = false;
                                isExistError = true;
                                ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(7),
  										"Giá trị không hợp lệ !");
  								errorList.add(errorDTO);
                            }
//                            if (!checkColumn7) {
//                                isExistError = true;
//                                ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(7),
//  										"Giá trị không hợp lệ !");
//  								errorList.add(errorDTO);
//                            }
                            
                        } 
                        
                        else if (cell.getColumnIndex() == 8) {
                            if (row.getCell(8) != null) {
                            	try {
                                    long nguon = new Long(Long.parseLong(formatter.formatCellValue(row.getCell(8)).trim()));
                                    if(nguon>0 && nguon<8) {
                                    	if(newObj.getConstructionId()!=null) {
                                    		String sourceWorkDb = mapSourceWork.get(newObj.getConstructionId());
                                        	if(sourceWorkDb!=null) {
                                        		if(newObj.getCheckHTCT()!=null && newObj.getCheckHTCT()==1l) {
                                        			if(nguon != 4l && nguon != 5l) {
                                                    	isExistError = true;
                                            			checkColumn8 = false;
                                                    	ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(8),
                                                    			"Nguồn việc của công trình HTCT chỉ được nhập Xây dựng móng và Hoàn thiện !");
                          								errorList.add(errorDTO);
                                        			} else {
                                        				newObj.setSourceWork(String.valueOf(nguon));
                                        			}
                                        		} else {
                                        			if(nguon == 4l || nguon == 5l) {
                                        				isExistError = true;
                                            			checkColumn8 = false;
                                                    	ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(8),
                          										"Nguồn việc Xây dựng móng và Hoàn thiện chỉ ứng với công trình HTCT !");
                          								errorList.add(errorDTO);
                                        			} else {
                                        				if(sourceWorkDb.equals(formatter.formatCellValue(row.getCell(8)).trim())) {
                                                			if(mapNguon.size()==0) {
                                                        		newObj.setSourceWork(String.valueOf(nguon));
                                                        		mapNguon.put(newObj.getConstructionCode(), nguon);
                                                        	} else {
                                                        		if(mapNguon.get(newObj.getConstructionCode())!=null && mapNguon.get(newObj.getConstructionCode()) != nguon) {
                                                        			 isExistError = true;
                                                        			checkColumn8 = false;
                                                                	ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(8),
                                      										"Nguồn việc của những hạng mục thuộc cùng công trình phải giống nhau !");
                                      								errorList.add(errorDTO);
                                                        		} else {
                                                        			newObj.setSourceWork(String.valueOf(nguon));
                                                            		mapNguon.put(newObj.getConstructionCode(), nguon);
                                                        		}
                                                        	}
                                                		} else {
                                                			 isExistError = true;
                                                			checkColumn8 = false;
                                                        	ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(8),
                                                        			" Nguồn việc của công trình: "+ newObj.getConstructionCode() + " đã tồn tại bằng: "+ sourceWorkDb);
                              								errorList.add(errorDTO);
                                                		}
                                        			}
                                        		}
                                        	} else {
                                        		if(newObj.getCheckHTCT()!=null && newObj.getCheckHTCT()==1l) {
                                        			if(nguon != 4l && nguon != 5l) {
                                        				isExistError = true;
                                            			checkColumn8 = false;
                                                    	ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(8),
                                                    			"Nguồn việc của công trình HTCT chỉ được nhập Xây dựng móng và Hoàn thiện !");
                          								errorList.add(errorDTO);
                                        			} else {
                                        				newObj.setSourceWork(String.valueOf(nguon));
                                        			}
                                        		} else {
                                        			if(nguon == 4l || nguon == 5l) {
                                        				isExistError = true;
                                            			checkColumn8 = false;
                                                    	ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(8),
                          										"Nguồn việc Xây dựng móng và Hoàn thiện chỉ ứng với công trình HTCT !");
                          								errorList.add(errorDTO);
                                        			} else {
                                        				if(mapNguon.size()==0) {
                                                    		newObj.setSourceWork(String.valueOf(nguon));
                                                    		mapNguon.put(newObj.getConstructionCode(), nguon);
                                                    	} else {
                                                    		LOGGER.info("Giá trị: " + (mapNguon.get(newObj.getConstructionCode())));
                                                    		LOGGER.info("Giá trị: " + (nguon));
                                                    		if(mapNguon.get(newObj.getConstructionCode())!=null && mapNguon.get(newObj.getConstructionCode()) != nguon) {
                                                    			isExistError = true;
                                                    			checkColumn8 = false;
                                                            	ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(8),
                                                            			"Nguồn việc của những hạng mục thuộc cùng công trình phải giống nhau !");
                                  								errorList.add(errorDTO);
                                                    		} else {
                                                    			newObj.setSourceWork(String.valueOf(nguon));
                                                        		mapNguon.put(newObj.getConstructionCode(), nguon);
                                                    		}
                                                    	}
                                        			}
                                        		}
                                        	}
                                    	} else {
                                    		newObj.setSourceWork(String.valueOf(nguon));
                                    		mapNguon.put(newObj.getConstructionCode(), nguon);
                                    	}
                                    	
                                    } else {
                                    	 isExistError = true;
                                    	checkColumn8 = false;
                                    	ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(8),
                                    			"Nguồn việc không hợp lệ !");
          								errorList.add(errorDTO);
                                    }
                                } catch (Exception e) {
                                	 isExistError = true;
                                    checkColumn8 = false;
                                    ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(8),
                                			"Nguồn việc phải nhập dạng số !");
      								errorList.add(errorDTO);
                                }
                            } else {
                            	 isExistError = true;
                            	checkColumn8 = false;
                            	ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(8),
                            			"Nguồn việc không được để trống !");
  								errorList.add(errorDTO);
                            }
                        } else if (cell.getColumnIndex() == 9) {
                            if (row.getCell(9) != null) {
                            	try {
                            		long loaict = new Long(Long.parseLong(formatter.formatCellValue(row.getCell(9)).trim()));
                                    if(loaict>0 && loaict<10) {
                                    	if(newObj.getConstructionId()!=null) {
                                    		String consTypeDb = mapConsType.get(newObj.getConstructionId());
                                    		if(consTypeDb!=null) {
                                        		if(consTypeDb.equals(formatter.formatCellValue(row.getCell(9)).trim())) {
                                        			if(mapLoaiCt.size()==0) {
//                                                		newObj.setConstructionTypeNew(String.valueOf(loaict));
//                                                		mapLoaiCt.put(newObj.getConstructionCode(), loaict);
                                        				checkValidateConstructionTypeTC(newObj, loaict, errorList, isExistError, mapLoaiCt, row);
                                                	} else {
                                                		if(mapLoaiCt.get(newObj.getConstructionCode())!=null && mapLoaiCt.get(newObj.getConstructionCode()) != loaict) {
                                                			isExistError = true;
                                                			checkColumn9 = false;
                                                        	ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(9),
                                                        			"Loại công trình của những hạng mục thuộc cùng công trình phải giống nhau !");
                              								errorList.add(errorDTO);
                                                		} else {
                                                			checkValidateConstructionTypeTC(newObj, loaict, errorList, isExistError, mapLoaiCt, row);
                                                		}
                                                	}
                                        		} else {
                                        			 isExistError = true;
                                        			checkColumn9 = false;
                                                	ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(9),
                                                			" Loại công trình của công trình: "+ newObj.getConstructionCode() +" đã tồn tại bằng: "+consTypeDb);
                      								errorList.add(errorDTO);
                                        		}
                                        	} else {
                                        		if(mapLoaiCt.size()==0) {
                                        			checkValidateConstructionTypeTC(newObj, loaict, errorList, isExistError, mapLoaiCt, row);
                                            	} else {
                                            		if(mapLoaiCt.get(newObj.getConstructionCode())!=null && mapLoaiCt.get(newObj.getConstructionCode()) != loaict) {
                                            			 isExistError = true;
                                            			checkColumn9 = false;
                                                    	ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(9),
                                                    			" Loại công trình của những hạng mục thuộc cùng công trình phải giống nhau!");
                          								errorList.add(errorDTO);
                                            		} else {
                                            			checkValidateConstructionTypeTC(newObj, loaict, errorList, isExistError, mapLoaiCt, row);
                                            		}
                                            	}
                                        	}
                                    	} else {
                                    		checkValidateConstructionTypeTC(newObj, loaict, errorList, isExistError, mapLoaiCt, row);
                                    	}
                                    	
                                    } else {
                                    	 isExistError = true;
                                    	checkColumn9 = false;
                                    	ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(9),
                                    			" Loại công trình không hợp lệ !");
          								errorList.add(errorDTO);
                                    }
                                } catch (Exception e) {
                                	 isExistError = true;
                                    checkColumn9 = false;
                                    ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(9),
                                			" Loại công trình phải nhập dạng số !");
      								errorList.add(errorDTO);
                                }
                            } else {
                            	 isExistError = true;
                            	checkColumn9 = false;
                            	ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(9),
                            			" Loại công trình không được để trống !");
  								errorList.add(errorDTO);
                            }
                        } 
                        else if (cell.getColumnIndex() == 10) {
                            if (row.getCell(10) != null) {
                                newObj.setSourceType("1");
                            } else {
                                newObj.setSourceType("2");
                            }

                        } else if (cell.getColumnIndex() == 12) {
                            if (row.getCell(12) != null) {
                                newObj.setDeployType("1");
                            } else {
                                newObj.setDeployType("2");
                            }

                        }
                        else if (cell.getColumnIndex() == 14) {
                            try {
                                String nameUser = formatter.formatCellValue(row.getCell(14)).trim();
                                SysUserCOMSDTO obj = new SysUserCOMSDTO();
                                obj=userLoginMap.get(nameUser.toUpperCase().trim());
                                if(obj == null){
                                	obj=userEmailMap.get(nameUser.toUpperCase().trim());	
                                }
                                newObj.setPerformerId(obj.getSysUserId());
                                newObj.setPerformerName(obj.getFullName());
                            } catch (Exception e) {
                                checkColumn14 = false;
                                isExistError = true;
                                ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(14),
                            			" Người thực hiện không tồn tại !");
  								errorList.add(errorDTO);
                            }
//                            if (!checkColumn14) {
//                                isExistError = true;
//                                ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(14),
//                            			" Người thực hiện không tồn tại !");
//  								errorList.add(errorDTO);
//                            }

                        } else if (cell.getColumnIndex() == 15) {
                            try {
                            	if(row.getCell(15)!=null) {
                            		Date startDate = dateFormat.parse(formatter.formatCellValue(row.getCell(15)));
                                    if(validateDate(formatter.formatCellValue(row.getCell(15)))){
                                    	newObj.setStartDate(startDate);
                                    }else{ 
                                    	isExistError = true;
                                    	checkColumn15 = false;
                                    	ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(15),
                                    			" Ngày bắt đầu không hợp lệ !");
          								errorList.add(errorDTO);
                                    }        
                            	} else {
                            		isExistError = true;
                            		checkColumn15 = false;
                            		ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(15),
                                			" Ngày bắt đầu không được để trống !");
      								errorList.add(errorDTO);
                            	}
                            } catch (Exception e) {
                            	isExistError = true;
                                checkColumn15 = false;
                                ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(15),
                            			" Ngày bắt đầu không hợp lệ !");
  								errorList.add(errorDTO);
                            }
                        } else if (cell.getColumnIndex() == 16) {
                            try {
                            	if(row.getCell(16)!=null) {
                            		Date endDate = dateFormat.parse(formatter.formatCellValue(row.getCell(16)));
                                    if(validateDate(formatter.formatCellValue(row.getCell(16)))){
                                    	newObj.setEndDate(endDate);
                                    }else{ 
                                    	isExistError = true;
                                    	checkColumn16 = false;
                                    	ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(16),
                                    			" Ngày kết thúc không hợp lệ !");
          								errorList.add(errorDTO);
                                    }  
                            	} else {
                            		isExistError = true;
                            		checkColumn16 = false;
                            		ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(16),
                                			" Ngày kết thúc không được để trống !");
      								errorList.add(errorDTO);
                            	}
                                
                            } catch (Exception e) {
                                checkColumn16 = false;
                                isExistError = true;
                                ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(16),
                            			" Ngày kết thúc không hợp lệ !");
  								errorList.add(errorDTO);
                            }
                        } else if (cell.getColumnIndex() == 17) {
                            try {
                                String descriptionTC = formatter.formatCellValue(cell).trim();
                                if (descriptionTC.length() <= 1000) {
                                    newObj.setDescription(descriptionTC);
                                } else {
                                    checkColumn17 = false;
                                    isExistError = true;
                                }

                            } catch (Exception e) {
                                checkColumn17 = false;
                                isExistError = true;
                                ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(17),
                            			" Ghi chú vượt quá 1000 ký tự !");
  								errorList.add(errorDTO);
                            }
//                            if (!checkColumn17) {
//                                isExistError = true;
//                                ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(17),
//                            			" Ghi chú vượt quá 1000 ký tự !");
//  								errorList.add(errorDTO);
//                            }
//                            Cell cell1 = row.createCell(18);
//                            cell1.setCellValue(errorMesg.toString());
                        }
                    }
                }
                if (errorList.size()==0) {
                    newObj.setSourceType(formatter.formatCellValue(row.getCell(10)).equals("1") ? "1" : "2");
                    newObj.setDeployType(formatter.formatCellValue(row.getCell(12)).equals("1") ? "1" : "2");
                    newObj.setWorkItemType("1");
                    workLst.add(newObj);
                }

            }
        }
        if (errorList.size()>0) {
        	workLst = new ArrayList<ConstructionTaskDetailDTO>();
			ConstructionTaskDetailDTO objErr = new ConstructionTaskDetailDTO();
			objErr.setErrorList(errorList);
			objErr.setMessageColumn(18);
			objErr.setErrorFilePath(UEncrypt.encryptFileUploadPath(filePath));
			objErr.setFilePathError(UEncrypt.encryptFileUploadPath(filePath));
			workLst.add(objErr);
        }
        workbook.close();
        return workLst;
    }
	//Huy-end
	
	//Huypq-20200430-start
	public ConstructionTaskDetailDTO checkSourceWorkByConstruction(ConstructionTaskDTO obj) {
		return constructionTaskDAO.checkSourceWorkByConstruction(obj);
	}
	
	public ConstructionTaskDetailDTO checkDupTaskByConstruction(ConstructionTaskDTO obj) {
		return constructionTaskDAO.checkDupTaskByConstruction(obj);
	}
	
	public ConstructionTaskDetailDTO checkMapConstructionContract(String consCode) {
		return constructionTaskDAO.checkMapConstructionContract(consCode);
	}
	
	
	public ConstructionTaskDetailDTO checkConstructionMapContract(ConstructionTaskDTO obj) {
		return constructionTaskDAO.checkConstructionMapContract(obj.getConstructionId());
	}
	//Huy-end
	
	//Huypq-20200514-start
	public List<ConstructionTaskDetailDTO> importRentGround(String fileInput, String filePath, HttpServletRequest request, Long month, Long year, Long sysGroupId)
			throws Exception {
		RevokeCashMonthPlanDTO plan = constructionTaskDAO.getMonthPlanId(month.toString(), year.toString(), sysGroupId);
		if(plan==null) {
			throw new IllegalArgumentException("Kế hoạch tháng chưa trình ký hoặc không tồn tại !");
		}
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		List<ConstructionTaskDetailDTO> workLst = Lists.newArrayList();
		List<ExcelErrorDTO> errorList = new ArrayList<ExcelErrorDTO>();

		File f = new File(fileInput);
		XSSFWorkbook workbook = new XSSFWorkbook(f);
		XSSFSheet sheet = workbook.getSheetAt(0);
		DataFormatter formatter = new DataFormatter();
		int count = 0;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		boolean isExistError = false;

		int counts = 0;
        List<String> lstStation = new ArrayList<>();
        List<String> lstUser = new ArrayList<>();
        for (Row row : sheet) {
        	counts++;
            if (counts >= 3 && !ValidateUtils.checkIfRowIsEmpty(row)) {
            	lstStation.add(formatter.formatCellValue(row.getCell(2)).trim().toUpperCase());
            	lstUser.add(formatter.formatCellValue(row.getCell(4)).trim().toUpperCase());
            }
        }
		
        HashMap<String, ConstructionTaskDetailDTO> mapStation = new HashMap<>();
        HashMap<String, String> mapCheckCompleteStatus = new HashMap<>();
        if(lstStation.size()>0) {
        	List<ConstructionTaskDetailDTO> listStation = detailMonthPlanOSDAO.getStationByLstCode(lstStation);
        	for(ConstructionTaskDetailDTO dto : listStation) {
        		mapStation.put(dto.getCatStationCode().toUpperCase(), dto);
        	}
        	
        	List<CatStationDTO> listCheckCompleteStatus = detailMonthPlanOSDAO.getCheckCompleteByListStationCode(lstStation);
        	for(CatStationDTO dto : listCheckCompleteStatus) {
        		mapCheckCompleteStatus.put(dto.getCode().toUpperCase(), dto.getCode());
        	}
        }
        
        HashMap<String, SysUserDTO> mapUserCode = new HashMap<>();
        HashMap<String, SysUserDTO> mapUserEmail = new HashMap<>();
        List<SysUserDTO> listUser = detailMonthPlanOSDAO.getUserByLstCode(lstUser);
        for(SysUserDTO dto : listUser) {
        	mapUserCode.put(dto.getEmployeeCode().toUpperCase(), dto);
        	mapUserEmail.put(dto.getEmail().toUpperCase(), dto);
        }
		
        HashMap<String, String> mapStationMonth = new HashMap<>();
        List<ConstructionTaskDTO> lstStationInMonthPlan = detailMonthPlanOSDAO.checkDupStationByMonthPlanId(plan.getDetailMonthPlanId(), sysGroupId, lstStation);
        for(ConstructionTaskDTO dto : lstStationInMonthPlan) {
        	mapStationMonth.put(dto.getStationCode().toUpperCase(), dto.getStationCode());
        }
        
        HashMap<String, String> mapCheckDupInFile = new HashMap<>();
        
		for (Row row : sheet) {
			count++;
			if (count >= 3 && !ValidateUtils.checkIfRowIsEmpty(row)) {
				String stationCode = formatter.formatCellValue(row.getCell(2)).trim();
				String completeDate = formatter.formatCellValue(row.getCell(3)).trim();
				String performerName = formatter.formatCellValue(row.getCell(4)).trim();
				String startDate = formatter.formatCellValue(row.getCell(5)).trim();
				String endDate = formatter.formatCellValue(row.getCell(6)).trim();
				String description = formatter.formatCellValue(row.getCell(7)).trim();

				StringBuilder errorMesg = new StringBuilder();
				ConstructionTaskDetailDTO newObj = new ConstructionTaskDetailDTO();

				// Mã trạm
				if (validateString(stationCode)) {
					if (mapCheckDupInFile.size() == 0) {
						ConstructionTaskDetailDTO stationDto = mapStation.get(stationCode.toUpperCase());
						if (stationDto != null) {
							if (mapStationMonth.get(stationCode.toUpperCase()) != null) {
								isExistError = true;
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(2),
										"Mã trạm đã được giao trong kế hoạch tháng");
								errorList.add(errorDTO);
							} else {
								if (mapCheckCompleteStatus.get(stationCode.toUpperCase().trim()) != null) {
									isExistError = true;
									ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(2),
											"Mã trạm đã hoàn thành không thể giao kế hoạch tháng !");
									errorList.add(errorDTO);
								} else {
									newObj.setCatProvinceCode(stationDto.getCatProvinceCode());
									newObj.setCatStationId(stationDto.getCatStationId());
									newObj.setStationCode(stationDto.getCatStationCode());
								}
							}
						} else {
							isExistError = true;
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(2),
									"Mã trạm không tồn tại");
							errorList.add(errorDTO);
						}
						mapCheckDupInFile.put(stationCode.toUpperCase(), stationCode);
					} else {
						if (mapCheckDupInFile.get(stationCode.toUpperCase()) != null) {
							isExistError = true;
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(2),
									"Mã trạm trong cùng file import không được trùng nhau");
							errorList.add(errorDTO);
						} else {
							ConstructionTaskDetailDTO stationDto = mapStation.get(stationCode.toUpperCase());
							if (stationDto != null) {
								if (mapStationMonth.get(stationCode.toUpperCase()) != null) {
									isExistError = true;
									ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(2),
											"Mã trạm đã được giao trong kế hoạch tháng");
									errorList.add(errorDTO);
								} else {
									if (mapCheckCompleteStatus.get(stationCode.toUpperCase().trim()) != null) {
										isExistError = true;
										ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(2),
												"Mã trạm đã hoàn thành không thể giao kế hoạch tháng !");
										errorList.add(errorDTO);
									} else {
										newObj.setCatProvinceCode(stationDto.getCatProvinceCode());
										newObj.setCatStationId(stationDto.getCatStationId());
										newObj.setStationCode(stationDto.getCatStationCode());
									}
								}
							} else {
								isExistError = true;
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(2),
										"Mã trạm không tồn tại");
								errorList.add(errorDTO);
							}
							mapCheckDupInFile.put(stationCode.toUpperCase(), stationCode);
						}
						
					}
				} else {
					isExistError = true;
					ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(2),
							"Mã trạm không được bỏ trống");
					errorList.add(errorDTO);
				}
				
				// Thời gian hoàn thành
				/*if (validateString(completeDate)) {
					if(validateDate(completeDate)){
                    	newObj.setCompleteDate(dateFormat.parse(completeDate));
                    }else{ 
                    	isExistError = true;
    					ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(3),
    							"Thời gian hoàn thành không hợp lệ");
    					errorList.add(errorDTO);
                    }      
				}*/

				// Người thực hiện
				if (validateString(performerName)) {
					SysUserDTO sysDto = new SysUserDTO();
                    if(mapUserCode.get(performerName.toUpperCase())!=null) {
                    	sysDto = mapUserCode.get(performerName.toUpperCase());
                    } else if(mapUserEmail.get(performerName.toUpperCase())!=null) {
                    	sysDto = mapUserEmail.get(performerName.toUpperCase());
                    } else {
                        isExistError = true;
                        ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(4),
    							"Người thực hiện không tồn tại");
    					errorList.add(errorDTO);
                    }
                    newObj.setPerformerId(sysDto.getSysUserId());
                    newObj.setPerformerName(sysDto.getFullName());
				} else {
					isExistError = true;
					ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(4),
							"Người thực hiện không được bỏ trống");
					errorList.add(errorDTO);
				}

				// Thời gian bắt đầu
				if (validateString(startDate)) {
					if(validateDate(startDate)){
                    	newObj.setStartDate(dateFormat.parse(startDate));
                    }else{ 
                    	isExistError = true;
    					ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(5),
    							"Thời gian bắt đầu không hợp lệ");
    					errorList.add(errorDTO);
                    }      
				} else {
					isExistError = true;
					ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(5),
							"Thời gian bắt đầu không được bỏ trống");
					errorList.add(errorDTO);
				}

				// Thời gian kết thúc
				if (validateString(endDate)) {
					if(validateDate(endDate)){
                    	newObj.setEndDate(dateFormat.parse(endDate));
                    }else{ 
                    	isExistError = true;
    					ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(6),
    							"Thời gian kết thúc không hợp lệ");
    					errorList.add(errorDTO);
                    }      
				} else {
					isExistError = true;
					ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(6),
							"Thời gian kết thúc không được bỏ trống");
					errorList.add(errorDTO);
				}
				
				if (validateString(description)) {
					if (description.length() <= 1000) {
                        newObj.setDescription(description);
                    } else {
                        isExistError = true;
                        ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(7),
    							"Ghi chú vượt quá 1000 ký tự");
    					errorList.add(errorDTO);
                    } 
				}
				
//				Cell cell1 = row.createCell(6);
//                cell1.setCellValue(errorMesg.toString());
				
                newObj.setDetailMonthPlanId(plan.getDetailMonthPlanId());
                
				if (!isExistError) {
					workLst.add(newObj);
				}
			}
		}

		if (isExistError) {
			workLst = new ArrayList<ConstructionTaskDetailDTO>();
			ConstructionTaskDetailDTO objErr = new ConstructionTaskDetailDTO();
			objErr.setErrorList(errorList);
			objErr.setMessageColumn(8);
			objErr.setErrorFilePath(UEncrypt.encryptFileUploadPath(filePath));
			objErr.setFilePathError(UEncrypt.encryptFileUploadPath(filePath));
			workLst.add(objErr);
		}
		workbook.close();
		return workLst;

	}
	
	private void getParentTask(ConstructionTaskDetailDTO dto, String type, Long detailMonthPlanId, Long sysGroupId,
			ConstructionTaskDetailDTO obj) {
		// TODO Auto-generated method stub
		ConstructionTaskDetailDTO provinceLevel = new ConstructionTaskDetailDTO();
		if (sysGroupId != null) {
			provinceLevel = constructionTaskDAO.getLevel1BySysGroupId(sysGroupId, type, detailMonthPlanId);
			Long provinceLevelId = provinceLevel.getConstructionTaskId();
			if (provinceLevelId == null) {
				constructionTaskDAO.deleteConstructionTask(type, 1L, detailMonthPlanId);
				provinceLevel.setType(type);
				provinceLevel.setTaskName(constructionTaskDAO.getDivisionCodeSysGroupId(sysGroupId));
				provinceLevel.setLevelId(1L);
				provinceLevel.setSysGroupId(sysGroupId);
				provinceLevel.setDetailMonthPlanId(detailMonthPlanId);
				provinceLevel.setMonth(obj.getMonth());
				provinceLevel.setYear(obj.getYear());
				ConstructionTaskBO bo = provinceLevel.toModel();
				provinceLevelId = constructionTaskDAO.saveObject(bo);
				bo.setPath("/" + provinceLevelId + "/");
				bo.setConstructionTaskId(provinceLevelId);
				bo.setType(type);
				constructionTaskDAO.update(bo);
				constructionTaskDAO.getSession().flush();
				constructionTaskDAO.updateChildRecord(bo, detailMonthPlanId);
			}
			dto.setPath("/" + provinceLevelId + "/");
			dto.setConstructionTaskId(provinceLevelId);
		}
	}
	
	public Long saveRentGround(ConstructionTaskDetailDTO obj, HttpServletRequest request) {
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
				Constant.AdResourceKey.WORK_PROGRESS, request);
		
		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
		Long sysGroupId = Long.parseLong(groupIdList.get(0));
		Long id = 0l;
		
		if(obj.getChildDTOList()!=null && obj.getChildDTOList().size()>0) {
			obj.setDetailMonthPlanId(obj.getChildDTOList().get(0).getDetailMonthPlanId());
			ConstructionTaskDetailDTO provinceLevel = new ConstructionTaskDetailDTO();
			provinceLevel = constructionTaskDAO.getLevel1ByConstructionCodeMobile(obj, "1", 1L, obj.getDetailMonthPlanId(), null, sysGroupId);
            Long provinceLevelId = provinceLevel.getConstructionTaskId();
            if(provinceLevelId==null) {
            	getParentTask(obj, "1", obj.getDetailMonthPlanId(), obj.getSysGroupId(), obj);
            	for(ConstructionTaskDetailDTO dto : obj.getChildDTOList()) {
    				dto.setMonth(obj.getMonth());
    	            dto.setSysGroupId(sysGroupId);
    	            dto.setYear(obj.getYear());
    	            dto.setType("4");
    	            dto.setDetailMonthPlanId(obj.getDetailMonthPlanId());
    	            dto.setLevelId(4L);
    	            dto.setTaskName("Thuê mặt bằng trạm hạ tầng cho thuê, mã trạm " + dto.getStationCode());
    	            dto.setParentId(obj.getConstructionTaskId());
    	            dto.setCreatedUserId(user.getVpsUserInfo().getSysUserId());
    	            dto.setCreatedDate(new Date());
    	            dto.setCreatedGroupId(user.getVpsUserInfo().getSysGroupId());
    	            dto.setBaselineStartDate(dto.getStartDate());
    	            dto.setBaselineEndDate(dto.getEndDate());
    	            dto.setStatus("1");
    	            dto.setCompleteState("1");
    	            dto.setCompletePercent(0d);
    	            dto.setPerformerWorkItemId(dto.getPerformerId());
    	            ConstructionTaskBO bo = dto.toModel();
//    	            bo.setCompleteDate(dto.getCompleteDate());
    	            id = constructionTaskDAO.saveObject(bo);
    	            bo.setPath(obj.getPath() + id + "/");
    	            bo.setType("4");
    	            constructionTaskDAO.updateObject(bo);
    			}
            } else {
            	for(ConstructionTaskDetailDTO dto : obj.getChildDTOList()) {
    				dto.setMonth(obj.getMonth());
    	            dto.setSysGroupId(sysGroupId);
    	            dto.setYear(obj.getYear());
    	            dto.setType("4");
    	            dto.setDetailMonthPlanId(obj.getDetailMonthPlanId());
    	            dto.setLevelId(4L);
    	            dto.setTaskName("Thuê mặt bằng trạm hạ tầng cho thuê, mã trạm " + dto.getStationCode());
    	            dto.setParentId(provinceLevelId);
    	            dto.setCreatedUserId(user.getVpsUserInfo().getSysUserId());
    	            dto.setCreatedDate(new Date());
    	            dto.setCreatedGroupId(user.getVpsUserInfo().getSysGroupId());
    	            dto.setBaselineStartDate(dto.getStartDate());
    	            dto.setBaselineEndDate(dto.getEndDate());
    	            dto.setStatus("1");
    	            dto.setCompleteState("1");
    	            dto.setCompletePercent(0d);
    	            dto.setPerformerWorkItemId(dto.getPerformerId());
    	            ConstructionTaskBO bo = dto.toModel();
//    	            bo.setCompleteDate(dto.getCompleteDate());
    	            id = constructionTaskDAO.saveObject(bo);
    	            bo.setPath(provinceLevel.getPath() + id + "/");
    	            bo.setType("4");
    	            constructionTaskDAO.updateObject(bo);
    			}
            	
            }
		}
		
		return id;
	}
	
	public DataListDTO doSearchManageRent(ConstructionTaskDetailDTO obj, HttpServletRequest request) {
		KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		String provinceId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
				Constant.AdResourceKey.DATA, request);
		List<String> provinceIdList = ConvertData.convertStringToList(provinceId, ",");
		List<ConstructionTaskDetailDTO> ls = new ArrayList<>();
		if (provinceIdList != null && !provinceIdList.isEmpty()) {
			ls = constructionTaskDAO.doSearchManageRent(obj, provinceIdList);
		}
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getPageSize());
		data.setStart(1);
		return data;
	}
	
	public List<UtilAttachDocumentDTO> getListImageRentHtct(Long id){
		try {
			List<UtilAttachDocumentDTO> ls = constructionTaskDAO.getListImageRentHtct(id);
			for (UtilAttachDocumentDTO dto : ls) {
				String fullPath = folderUpload + File.separator + dto.getFilePath();
				String base64Image = ImageUtil.convertImageToBase64(fullPath);
				dto.setBase64String(base64Image);
				dto.setFilePath(UEncrypt.encryptFileUploadPath(dto.getFilePath()));
			}
			return ls;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
	//huy-end
//	hoanm1_20200627_start
	public List<ConstructionTaskDTO> getListTaskXNXD(SysUserRequest request){
		 return constructionTaskDAO.getListTaskXNXD(request);
	}
	public int updateTaskXNXD(ConstructionTaskDTOUpdateRequest request) throws ParseException {
		try {
			constructionTaskDAO.updateTaskXNXD(request);
		} catch (Exception ex) {
			return 0;
		}
		return 1;
	}
	public List<ConstructionImageInfo> getImagesXNXD(ConstructionTaskDTOUpdateRequest request) {
        List<ConstructionImageInfo> listImageResponse = new ArrayList<>();
        List<ConstructionImageInfo> listImage = constructionTaskDAO.getImagesXNXD(request.getConstructionTaskDTO().getConstructionTaskId());
        listImageResponse = getResultSolutionImages(listImage);
        return listImageResponse;
	}
	public List<ConstructionImageInfo> getResultSolutionImages(List<ConstructionImageInfo> lstImages) {
        List<ConstructionImageInfo> result = new ArrayList<>();
        for (ConstructionImageInfo packageImage : lstImages) {
            try {
                String fullPath = folder2Upload + File.separator + packageImage.getImagePath();
                String base64Image = ImageUtil.convertImageToBase64(fullPath);
                ConstructionImageInfo obj = new ConstructionImageInfo();
                obj.setImageName(packageImage.getImageName());
                obj.setBase64String(base64Image);
                obj.setImagePath(fullPath);
                obj.setStatus(1L);
                obj.setUtilAttachDocumentId(packageImage.getUtilAttachDocumentId());
                result.add(obj);
            } catch (Exception e) {
                continue;
            }
        }

        return result;
    }
//	hoanm1_20200627_end
	
	//Huypq-13072020-start
	public ConstructionDetailDTO checkApproveRevenueStateOfConstruction(Long id) {
		return constructionTaskDAO.checkApproveRevenueStateOfConstruction(id);
	}
	//Huy-end
	//Huypq-28032022-start
	public List<ConstructioIocDTO> getDataConstructionForIOC(){
		 return constructionTaskDAO.getDataConstructionForIOC();
	}
	//Huy-end
	public List<ConstructionTotalValueDTO> getValueByConstructionFromPmxl(){
		 return constructionTaskDAO.getValueByConstructionFromPmxl();
	}
}
