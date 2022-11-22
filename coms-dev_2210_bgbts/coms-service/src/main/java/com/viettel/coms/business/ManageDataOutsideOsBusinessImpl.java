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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.viettel.coms.bo.ManageDataOutsideOsBO;
import com.viettel.coms.dao.ManageDataOutsideOsDAO;
import com.viettel.coms.dto.ExcelErrorDTO;
import com.viettel.coms.dto.ManageDataOutsideOsDTO;
import com.viettel.coms.utils.ExcelUtils;
import com.viettel.coms.utils.ValidateUtils;
import com.viettel.ktts.vps.VpsPermissionChecker;
import com.viettel.ktts2.common.BusinessException;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.utils.Constant;
import com.viettel.wms.business.UserRoleBusinessImpl;

@Service("manageDataOutsideOsBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ManageDataOutsideOsBusinessImpl
		extends BaseFWBusinessImpl<ManageDataOutsideOsDAO, ManageDataOutsideOsDTO, ManageDataOutsideOsBO>
		implements ManageDataOutsideOsBusiness {
	static Logger LOGGER = LoggerFactory.getLogger(ManageDataOutsideOsBusinessImpl.class);

	@Value("${folder_upload}")
	private String folder2Upload;

	@Value("${folder_upload2}")
	private String folderUpload;

	@Value("${folder_upload}")
	private String folderTemp;

	@Value("${default_sub_folder_upload}")
	private String defaultSubFolderUpload;
	
	@Autowired
    private UserRoleBusinessImpl userRoleBusinessImpl;

	@Autowired
	private ManageDataOutsideOsDAO manageDataOutsideOsDAO;

	public ManageDataOutsideOsBusinessImpl() {
		tModel = new ManageDataOutsideOsBO();
		tDAO = manageDataOutsideOsDAO;
	}

	  HashMap<Integer, String> colName = new HashMap();
	    {
	        colName.put(1,"Mã công trình");
	        colName.put(2,"Mã trạm");
	        colName.put(3,"Mã hợp đồng");
	        colName.put(4,"Loại công trình");
	        colName.put(5,"Ngày nhận HSHC bản cứng");
	        colName.put(6,"Ngày thẩm xong");
	        colName.put(7,"Thủ tục điện lực\r\n" + 
	        		"(Đối với các công trình GPON)");
	        colName.put(8,"Nhân công kéo cáp/Nhân công\r\n" + 
	        		"(Kéo cáp áp dụng đối với CT GPON)");
	        colName.put(9,"CP Vật liệu ");
	        colName.put(10,"CP HSHC");
	        colName.put(11,"CP Vận chuyển kho bãi");
	        colName.put(12,"CP khác");
	        colName.put(13,"Lương kéo cáp/lương khác\r\n" + 
	        		"(Kéo cáp áp dụng đối với CT GPON)");
	        colName.put(14,"Lương hàn nối");
	        colName.put(15,"VAT");
	        colName.put(16,"Tổng ");
	        colName.put(17,"Giá trị Thẩm định của PTK (chưa VAT)");
	        colName.put(18,"Tháng ghi nhận HSHC");
	        colName.put(19,"Tháng ghi nhận quỹ lương");
	        colName.put(20,"Lương Thực Nhận");
	        colName.put(21,"Lỗi HSHC");
	        colName.put(22,"Nguyên nhân lỗi");
	    }
	    
	    HashMap<Integer, String> colNameCDT = new HashMap();
	    {
	    	colNameCDT.put(1,"Mã công trình");
	    	colNameCDT.put(2,"Mã trạm");
	    	colNameCDT.put(3,"Mã hợp đồng");
	    	colNameCDT.put(4,"Loại công trình");
	    	colNameCDT.put(5,"Ngày lập đề nghị");
	    	colNameCDT.put(6,"Giá trị ");
	    	colNameCDT.put(7,"Ngày nộp VTNet");
	        colNameCDT.put(8,"Ghi chú");
	        colNameCDT.put(9,"Nhân viên thẩm định");
	        colNameCDT.put(10,"Ngày Thẩm định xong");
	        colNameCDT.put(11,"Giá trị ");
	        colNameCDT.put(12,"Ghi chú ");
	    }
	    
	    HashMap<Integer, String> colNameInvoice = new HashMap();
	    {
	    	colNameInvoice.put(1,"Mã công trình");
	    	colNameInvoice.put(2,"Mã trạm");
	    	colNameInvoice.put(3,"Mã hợp đồng");
	    	colNameInvoice.put(4,"Loại công trình");
	    	colNameInvoice.put(5,"Ngày chuyển Phòng tài chính");
	    	colNameInvoice.put(6,"Ngày xuất hóa đơn");
	    	colNameInvoice.put(7,"Số Hóa đơn");
	    	colNameInvoice.put(8,"Tháng chốt Doanh thu ");
	    	colNameInvoice.put(9,"Ghi chú");
	    }
	    
	    HashMap<Integer, String> colNameLiquidation = new HashMap();
	    {
	    	colNameLiquidation.put(1,"Mã công trình");
	    	colNameLiquidation.put(2,"Mã trạm");
	    	colNameLiquidation.put(3,"Mã hợp đồng");
	    	colNameLiquidation.put(4,"Loại công trình");
	    	colNameLiquidation.put(5,"Ngày ký thanh lý");
	    	colNameLiquidation.put(6,"Giá trị ");
	    	colNameLiquidation.put(7,"Ghi chú ");
	    	colNameLiquidation.put(8,"Chênh lệch sản lượng, thanh lý");
	    	colNameLiquidation.put(9,"Tỷ lệ ");
	    }

	    HashMap<Integer, String>colAlias  = new HashMap();
	    {
	        colAlias.put(1,"A");
	        colAlias.put(2,"B");
	        colAlias.put(3,"C");
	        colAlias.put(4,"D");
	        colAlias.put(5,"E");
	        colAlias.put(6,"F");
	        colAlias.put(7,"G");
	        colAlias.put(8,"H");
	        colAlias.put(9,"I");
	        colAlias.put(10,"J");
	        colAlias.put(11,"K");
	        colAlias.put(12,"L");
	        colAlias.put(13,"M");
	        colAlias.put(14,"N");
	        colAlias.put(15,"O");
	        colAlias.put(16,"P");
	        colAlias.put(17,"Q");
	        colAlias.put(18,"R");
	        colAlias.put(19,"S");
	        colAlias.put(20,"T");
	        colAlias.put(21,"U");
	    }
	
	//Huypq-20191114
	HashMap<Integer, String> colNameNew = new HashMap();
    {
    	colNameNew.put(1,"Ngày ký");
    	colNameNew.put(2,"Số hợp đồng");
    	colNameNew.put(3,"Giá trị hợp đồng");
    	colNameNew.put(4,"Thời gian thực hiện hợp đồng");
    	colNameNew.put(5,"Tỉnh");
    	colNameNew.put(6,"Mã công trình");
    	colNameNew.put(7,"Mã trạm/tuyến");
    	colNameNew.put(8,"Loại công trình");
    	colNameNew.put(9,"Nội dung");
    	colNameNew.put(10,"Nguồn vốn");
    	colNameNew.put(11,"Lương");
    	colNameNew.put(12,"Nhân công thuê ngoài");
    	colNameNew.put(13,"Chi phí vật liệu");
    	colNameNew.put(14,"Chi phí HSHC");
    	colNameNew.put(15,"Chi phí vận chuyển");
    	colNameNew.put(16,"Chi phí khác");
    	colNameNew.put(17,"Tháng triển khai");
    	colNameNew.put(18,"Tổng cộng");
    	colNameNew.put(19,"Hiệu quả");
    	colNameNew.put(20,"Ghi chú");
    	colNameNew.put(21,"Ngày tạm ứng");
    	colNameNew.put(22,"Nhân công");
    	colNameNew.put(23,"Vật liệu");
    	colNameNew.put(24,"Hồ sơ hoàn công");
    	colNameNew.put(25,"Chi phí vận chuyển");
    	colNameNew.put(26,"Chi phí khác");
    	colNameNew.put(27,"Ngày nhận đồng bộ vật tư");
    	colNameNew.put(28,"Giá trị");
    	colNameNew.put(29,"Giá trị sản lượng");
    	colNameNew.put(30,"Thuê đối tác");
    	colNameNew.put(31,"Thi công trực tiếp");
    	colNameNew.put(32,"Kẹp nách");
    	colNameNew.put(33,"Ngày bắt đầu");
    	colNameNew.put(34,"Ngày kết thúc");
    	colNameNew.put(35,"Vướng");
    	colNameNew.put(36,"Hủy");
    	colNameNew.put(37,"Ngày dự kiến hoàn thành");
    	colNameNew.put(38,"Ghi chú");
    	colNameNew.put(66,"Hình thức thi công");
    }
    
    HashMap<Integer, String>colAliasNew  = new HashMap();
    {
    	colAliasNew.put(1,"B");
    	colAliasNew.put(2,"C");
    	colAliasNew.put(3,"D");
    	colAliasNew.put(4,"E");
    	colAliasNew.put(5,"F");
    	colAliasNew.put(6,"G");
    	colAliasNew.put(7,"H");
    	colAliasNew.put(8,"I");
    	colAliasNew.put(9,"J");
    	colAliasNew.put(10,"K");
    	colAliasNew.put(11,"L");
    	colAliasNew.put(12,"M");
    	colAliasNew.put(13,"N");
    	colAliasNew.put(14,"O");
    	colAliasNew.put(15,"P");
    	colAliasNew.put(16,"Q");
    	colAliasNew.put(17,"R");
    	colAliasNew.put(18,"S");
    	colAliasNew.put(19,"T");
    	colAliasNew.put(20,"U");
    	colAliasNew.put(21,"V");
    	colAliasNew.put(22,"W");
    	colAliasNew.put(23,"X");
    	colAliasNew.put(24,"Y");
    	colAliasNew.put(25,"Z");
    	colAliasNew.put(26,"AA");
    	colAliasNew.put(27,"AB");
    	colAliasNew.put(28,"AC");
    	colAliasNew.put(29,"AD");
    	colAliasNew.put(30,"AE");
    	colAliasNew.put(31,"AF");
    	colAliasNew.put(32,"AG");
    	colAliasNew.put(33,"AH");
    	colAliasNew.put(34,"AI");
    	colAliasNew.put(35,"AJ");
    	colAliasNew.put(36,"AK");
    	colAliasNew.put(37,"AL");
    	colAliasNew.put(38,"AM");
    	colAliasNew.put(39,"AN");
    	colAliasNew.put(40,"AO");
    }
	//Huy-end
	public DataListDTO doSearchOS(ManageDataOutsideOsDTO obj) {
		List<ManageDataOutsideOsDTO> ls = manageDataOutsideOsDAO.doSearchOS(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setSize(obj.getTotalRecord());
		data.setStart(1);
		data.setTotal(obj.getTotalRecord());
		return data;
	}

	public Boolean checkRoleCNKT(HttpServletRequest request) {
		Boolean check = VpsPermissionChecker.hasPermission(Constant.OperationKey.RULE, Constant.AdResourceKey.TTHT,
				request);
		return check;
	}

	public Long saveAddNew(ManageDataOutsideOsDTO obj, HttpServletRequest request) throws Exception{
		KttsUserSession objUser = userRoleBusinessImpl.getUserSession(request);
		ManageDataOutsideOsDTO dto = manageDataOutsideOsDAO.checkDuplicateInDb(obj.getConstructionCode(),null);
		if(dto!=null) {
			throw new BusinessException("Công trình đã tồn tại trong dữ liệu quản lý số liệu ngoài OS");
		}
		obj.setStatus("1");
		if(obj.getListHttc()!=null && obj.getListHttc().size()>0) {
			for(String httc : obj.getListHttc()) {
				if(httc.equals("1")) {
					obj.setHttcTdt("1");
				}
				if(httc.equals("2")) {
					obj.setHttcTctt("2");
				}
				if(httc.equals("3")) {
					obj.setHttcKn("3");
				}
			}
		}
		obj.setCreatedDate(new Date());
		obj.setCreatedUserId(objUser.getSysUserId());
		return manageDataOutsideOsDAO.saveObject(obj.toModel());
	}

	public Long saveUpdateNew(ManageDataOutsideOsDTO obj, HttpServletRequest request) {
		KttsUserSession objUser = userRoleBusinessImpl.getUserSession(request);
		ManageDataOutsideOsDTO dto = manageDataOutsideOsDAO.checkDuplicateInDb(obj.getConstructionCode(),obj.getManageDataOutsideOsId());
		if(dto==null) {
			throw new BusinessException("Công trình không tương ứng với dữ liệu bản ghi cập nhật");
		}
		for(String httc : obj.getListHttc()) {
			if(httc.equals("1")) {
				obj.setHttcTdt("1");
			}
			if(httc.equals("2")) {
				obj.setHttcTctt("2");
			}
			if(httc.equals("3")) {
				obj.setHttcKn("3");
			}
		}
		if(obj.getStatus().equals("1")) {
			obj.setUpdatedUserId(objUser.getSysUserId());
		} else if(obj.getStatus().equals("2")) {
			obj.setScheduledUpdatedId(objUser.getSysUserId());
		} else if(obj.getStatus().equals("3")) {
			obj.setSuggestedUpdatedUserId(objUser.getSysUserId());
		} else if(obj.getStatus().equals("4")) {
			obj.setExpertisedUpdatedUserId(objUser.getSysUserId());
		} else if(obj.getStatus().equals("5")) {
			obj.setSettlementedUpdatedUserId(objUser.getSysUserId());
		} else if(obj.getStatus().equals("6")) {
			obj.setInvoiceUpdatedUserId(objUser.getSysUserId());
		} else if(obj.getStatus().equals("7")) {
			obj.setLiquidatedUpdatedUserId(objUser.getSysUserId());
		} else if(obj.getStatus().equals("8")) {
			obj.setLaborUpdatedUserId(objUser.getSysUserId());
		}
		return manageDataOutsideOsDAO.updateObject(obj.toModel());
	}

	public DataListDTO getAutoCompleteConstruction(ManageDataOutsideOsDTO obj) {
		List<ManageDataOutsideOsDTO> ls = manageDataOutsideOsDAO.getAutoCompleteConstruction(obj);
		DataListDTO data = new DataListDTO();
		data.setData(ls);
		data.setStart(1);
		data.setTotal(obj.getTotalRecord());
		data.setSize(obj.getTotalRecord());
		return data;
	}

	// tatph - start 13/11/2019
	protected static final String USER_SESSION_KEY = "kttsUserSession";

	public KttsUserSession getUserSession(HttpServletRequest request) {
		KttsUserSession s = (KttsUserSession) request.getSession().getAttribute(USER_SESSION_KEY);
		if (s == null) {
			throw new BusinessException("user is not authen");
		}
		return s;

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
			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && StringUtils.isNotBlank(cell.toString())) {
				return false;
			}
		}
		return true;
	}

	public List<ManageDataOutsideOsDTO> importExpertiseProposal(String fileInput) {
		List<ManageDataOutsideOsDTO> workLst = Lists.newArrayList();
		List<ExcelErrorDTO> errorList = new ArrayList<ExcelErrorDTO>();
		
		List<ManageDataOutsideOsDTO> listStatus = manageDataOutsideOsDAO.getDataManageOutsideOs();
        HashMap<String, ManageDataOutsideOsDTO> mapStatus = new HashMap<>();
        for(ManageDataOutsideOsDTO dto : listStatus) {
        	mapStatus.put(dto.getConstructionCode().toUpperCase().trim(), dto);
        }
		
		try {
			File f = new File(fileInput);
			XSSFWorkbook workbook = new XSSFWorkbook(f);
			XSSFSheet sheet = workbook.getSheetAt(0);
			DataFormatter formatter = new DataFormatter();
			int count = 0;
			// hienvd: check exit
			for (Row row : sheet) {
				count++;
				if (count >= 4 && checkIfRowIsEmpty(row))
					continue;
				if (count >= 4) {
					String constructionCode = formatter.formatCellValue(row.getCell(0));
					constructionCode = constructionCode.trim();
					String stationCode = formatter.formatCellValue(row.getCell(1));
					stationCode = stationCode.trim();
					String contractCode = formatter.formatCellValue(row.getCell(2));
					contractCode = contractCode.trim();
					String constructionType = formatter.formatCellValue(row.getCell(3));
					constructionType = constructionType.trim();
					String gttdHshcHardDate = formatter.formatCellValue(row.getCell(4));
					gttdHshcHardDate = gttdHshcHardDate.trim();
					String gttdCompleteExpertiseDate = formatter.formatCellValue(row.getCell(5));
					gttdCompleteExpertiseDate = gttdCompleteExpertiseDate.trim();
					String dnqtElectricalProcedures = formatter.formatCellValue(row.getCell(6));
					dnqtElectricalProcedures = dnqtElectricalProcedures.trim();
					String dnqtPullCableLabor = formatter.formatCellValue(row.getCell(7));
					dnqtPullCableLabor = dnqtPullCableLabor.trim();
					String dnqtCostMaterial = formatter.formatCellValue(row.getCell(8));
					dnqtCostMaterial = dnqtCostMaterial.trim();
					String dnqtCostHshc = formatter.formatCellValue(row.getCell(9));
					dnqtCostHshc = dnqtCostHshc.trim();
					String dnqtCostTransportWarehouse = formatter.formatCellValue(row.getCell(10));
					dnqtCostTransportWarehouse = dnqtCostTransportWarehouse.trim();
					String dnqtCostOrther = formatter.formatCellValue(row.getCell(11));
					dnqtCostOrther = dnqtCostOrther.trim();
					String dnqtSalaryCableOrther = formatter.formatCellValue(row.getCell(12));
					dnqtSalaryCableOrther = dnqtSalaryCableOrther.trim();
					String dnqtWeldingSalary = formatter.formatCellValue(row.getCell(13));
					dnqtWeldingSalary = dnqtWeldingSalary.trim();
					String dnqtVat = formatter.formatCellValue(row.getCell(14));
					dnqtVat = dnqtVat.trim();
					String dnqtTotalMoney = formatter.formatCellValue(row.getCell(15));
					dnqtTotalMoney = dnqtTotalMoney.trim();
					String gttdGttdPtk = formatter.formatCellValue(row.getCell(16));
					gttdGttdPtk = gttdGttdPtk.trim();
					String gttdHshcMonth = formatter.formatCellValue(row.getCell(17));
					gttdHshcMonth = gttdHshcMonth.trim();
					String gttdSalaryMonth = formatter.formatCellValue(row.getCell(18));
					gttdSalaryMonth = gttdSalaryMonth.trim();
					String gttdSalaryReal = formatter.formatCellValue(row.getCell(19));
					gttdSalaryReal = gttdSalaryReal.trim();
					String gttdHshcError = formatter.formatCellValue(row.getCell(20)); 
					gttdHshcError = gttdHshcError.trim();
					String gttdErrorReason = formatter.formatCellValue(row.getCell(21));
					gttdErrorReason = gttdErrorReason.trim();

					ManageDataOutsideOsDTO obj = new ManageDataOutsideOsDTO();
					// validate cong trinh
					obj.setConstructionCode(constructionCode);
					obj.setStationCode(stationCode);
					obj.setHdContractCode(contractCode);
					obj.setGttdCostMaterial(!"".equals(dnqtCostMaterial) ? Double.parseDouble(dnqtCostMaterial) :0D);
					obj.setGttdCostHshc(!"".equals(dnqtCostHshc) ? Double.parseDouble(dnqtCostHshc) : 0D);
					obj.setGttdCostTransportWarehouse(!"".equals(dnqtCostHshc) ? Double.parseDouble(dnqtCostTransportWarehouse) : 0D);
					obj.setGttdCostOrther(!"".equals(dnqtCostHshc) ? Double.parseDouble(dnqtCostOrther) : 0D );
					obj.setGttdVat(!"".equals(dnqtCostHshc) ? Double.parseDouble(dnqtVat) : 0D);
					obj.setGttdTotalMoney(!"".equals(dnqtCostHshc) ? Double.parseDouble(dnqtTotalMoney) : 0D);
					obj.setGttdHshcError(!"".equals(dnqtCostHshc) ? gttdHshcError : "");
					obj.setGttdErrorReason(!"".equals(dnqtCostHshc) ?gttdErrorReason : "");
					if (validateString(gttdHshcHardDate)) {
						Date date=new SimpleDateFormat("dd/MM/yyyy").parse(gttdHshcHardDate);  
						obj.setGttdHshcHardDate(date);
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(5),
								colName.get(5) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					//
//					if (validateString(gttdCompleteExpertiseDate)) {
//						Date date=new SimpleDateFormat("dd/MM/yyyy").parse(gttdCompleteExpertiseDate);  
						obj.setGttdCompleteExpertiseDate(new Date());
//					} else {
//						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(6),
//								colName.get(6) + " bị bỏ trống");
//						errorList.add(errorDTO);
//					}
					//
					  if(validateString(dnqtElectricalProcedures)){
	                    	if(dnqtElectricalProcedures.length() <= 20) {
	                            try{
	                                if(Double.parseDouble(dnqtElectricalProcedures)<0){
	                                    ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(7), colName.get(7)+ " nhỏ hơn 0");
	                                    errorList.add(errorDTO);
	                                } else
	                                	 obj.setGttdElectricalProcedures(Double.parseDouble(dnqtElectricalProcedures));
	                            } catch(Exception e){
	                                ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(7), colName.get(7)+ " chỉ được nhập số");
	                                errorList.add(errorDTO);
	                            }
	                        } else {
	                            ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(7), colName.get(7)+ " có độ dài quá giới hạn");
	                            errorList.add(errorDTO);
	                        }
	                    }else {
	                    	ManageDataOutsideOsDTO dtoMap = mapStatus.get(constructionCode.toUpperCase().trim());
							if(dtoMap.getConstructionType().equals("4")) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(7), colName.get(7)+ " bị bỏ trống");
		                        errorList.add(errorDTO);
							}
	                    }
					  //
					  if(validateString(dnqtPullCableLabor)){
	                    	if(dnqtPullCableLabor.length() <= 20) {
	                            try{
	                                if(Double.parseDouble(dnqtPullCableLabor)<0){
	                                    ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(8), colName.get(8)+ " nhỏ hơn 0");
	                                    errorList.add(errorDTO);
	                                } else
	                                	 obj.setGttdPullCableLabor(Double.parseDouble(dnqtPullCableLabor));
	                            } catch(Exception e){
	                                ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(8), colName.get(8)+ " chỉ được nhập số");
	                                errorList.add(errorDTO);
	                            }
	                        } else {
	                            ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(8), colName.get(8)+ " có độ dài quá giới hạn");
	                            errorList.add(errorDTO);
	                        }
	                    }else {
	                        ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(8), colName.get(8)+ " bị bỏ trống");
	                        errorList.add(errorDTO);
	                    }
					  
					  if(validateString(dnqtSalaryCableOrther)){
	                    	if(dnqtSalaryCableOrther.length() <= 20) {
	                            try{
	                                if(Double.parseDouble(dnqtSalaryCableOrther)<0){
	                                    ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(13), colName.get(13)+ " nhỏ hơn 0");
	                                    errorList.add(errorDTO);
	                                } else
	                                	 obj.setGttdSalaryCableOrther(Double.parseDouble(dnqtSalaryCableOrther));
	                            } catch(Exception e){
	                                ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(13), colName.get(13)+ " chỉ được nhập số");
	                                errorList.add(errorDTO);
	                            }
	                        } else {
	                            ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(13), colName.get(13)+ " có độ dài quá giới hạn");
	                            errorList.add(errorDTO);
	                        }
	                    }else {
	                        ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(13), colName.get(13)+ " bị bỏ trống");
	                        errorList.add(errorDTO);
	                    }
					  //
					  if(validateString(dnqtWeldingSalary)){
	                    	if(dnqtWeldingSalary.length() <= 20) {
	                            try{
	                                if(Double.parseDouble(dnqtWeldingSalary)<0){
	                                    ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(14), colName.get(14)+ " nhỏ hơn 0");
	                                    errorList.add(errorDTO);
	                                } else
	                                	 obj.setGttdWeldingSalary(Double.parseDouble(dnqtWeldingSalary));
	                            } catch(Exception e){
	                                ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(14), colName.get(14)+ " chỉ được nhập số");
	                                errorList.add(errorDTO);
	                            }
	                        } else {
	                            ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(14), colName.get(14)+ " có độ dài quá giới hạn");
	                            errorList.add(errorDTO);
	                        }
	                    }else {
	                    	ManageDataOutsideOsDTO dtoMap = mapStatus.get(constructionCode.toUpperCase().trim());
							if(dtoMap.getConstructionType().equals("4")) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(14), colName.get(14)+ " bị bỏ trống");
		                        errorList.add(errorDTO);
							}
	                    }
					  //
					  if(validateString(gttdGttdPtk)){
	                    	if(gttdGttdPtk.length() <= 20) {
	                            try{
	                                if(Double.parseDouble(gttdGttdPtk)<0){
	                                    ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(17), colName.get(17)+ " nhỏ hơn 0");
	                                    errorList.add(errorDTO);
	                                } else
	                                	 obj.setGttdGttdPtk(Double.parseDouble(gttdGttdPtk));
	                            } catch(Exception e){
	                                ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(17), colName.get(17)+ " chỉ được nhập số");
	                                errorList.add(errorDTO);
	                            }
	                        } else {
	                            ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(17), colName.get(17)+ " có độ dài quá giới hạn");
	                            errorList.add(errorDTO);
	                        }
	                    }else {
	                        ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(17), colName.get(17)+ " bị bỏ trống");
	                        errorList.add(errorDTO);
	                    }
					  //
						if (validateString(gttdHshcMonth)) {
							if(ValidateUtils.isValidFormat("MM/yyyy", gttdHshcMonth)==null) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(18),
										colName.get(18) + " sai định dạng 'MM/yyyy'");
								errorList.add(errorDTO);
							} else {
								obj.setGttdHshcMonth(gttdHshcMonth);
							}
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(18),
									colName.get(18) + " bị bỏ trống");
							errorList.add(errorDTO);
						}
						
						  //
						if (validateString(gttdSalaryMonth)) {
							if(ValidateUtils.isValidFormat("MM/yyyy", gttdSalaryMonth)==null) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(19),
										colName.get(19) + " sai định dạng 'MM/yyyy'");
								errorList.add(errorDTO);
							} else {
								obj.setGttdSalaryMonth(gttdSalaryMonth);
							}
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(19),
									colName.get(19) + " bị bỏ trống");
							errorList.add(errorDTO);
						}
						//
						 if(validateString(gttdSalaryReal)){
		                    	if(gttdSalaryReal.length() <= 20) {
		                            try{
		                                if(Double.parseDouble(gttdSalaryReal)<0){
		                                    ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(20), colName.get(20)+ " nhỏ hơn 0");
		                                    errorList.add(errorDTO);
		                                } else
		                                	 obj.setGttdSalaryReal(Double.parseDouble(gttdSalaryReal));
		                            } catch(Exception e){
		                                ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(20), colName.get(20)+ " chỉ được nhập số");
		                                errorList.add(errorDTO);
		                            }
		                        } else {
		                            ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(20), colName.get(20)+ " có độ dài quá giới hạn");
		                            errorList.add(errorDTO);
		                        }
		                    }else {
		                        ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(20), colName.get(20)+ " bị bỏ trống");
		                        errorList.add(errorDTO);
		                    }
					  

					  
					  
					if (errorList.size() == 0) {
						workLst.add(obj);
					}
				}
			}

			if (errorList.size() > 0) {
				String filePathError = UEncrypt.encryptFileUploadPath(fileInput);
				List<ManageDataOutsideOsDTO> emptyArray = Lists.newArrayList();
				workLst = emptyArray;
				ManageDataOutsideOsDTO errorContainer = new ManageDataOutsideOsDTO();
				errorContainer.setErrorList(errorList);
				errorContainer.setMessageColumn(21); // cột dùng để in ra lỗi
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
			List<ManageDataOutsideOsDTO> emptyArray = Lists.newArrayList();
			workLst = emptyArray;
			ManageDataOutsideOsDTO errorContainer = new ManageDataOutsideOsDTO();
			errorContainer.setErrorList(errorList);
			errorContainer.setMessageColumn(21); // cột dùng để in ra lỗi
			errorContainer.setFilePathError(filePathError);
			workLst.add(errorContainer);
			return workLst;
		}
	}
	
	public List<ManageDataOutsideOsDTO> importExpertiseProposalCDT(String fileInput) {
		List<ManageDataOutsideOsDTO> workLst = Lists.newArrayList();
		List<ExcelErrorDTO> errorList = new ArrayList<ExcelErrorDTO>();
		try {
			File f = new File(fileInput);
			XSSFWorkbook workbook = new XSSFWorkbook(f);
			XSSFSheet sheet = workbook.getSheetAt(0);
			DataFormatter formatter = new DataFormatter();
			int count = 0;
			// hienvd: check exit
			for (Row row : sheet) {
				count++;
				if (count >= 5 && checkIfRowIsEmpty(row))
					continue;
				if (count >= 5) {
					String constructionCode = formatter.formatCellValue(row.getCell(0));
					constructionCode = constructionCode.trim();
					String stationCode = formatter.formatCellValue(row.getCell(1));
					stationCode = stationCode.trim();
					String contractCode = formatter.formatCellValue(row.getCell(2));
					contractCode = contractCode.trim();
					String constructionType = formatter.formatCellValue(row.getCell(3));
					constructionType = constructionType.trim();
					String qtdnSuggestionsDate = formatter.formatCellValue(row.getCell(4));
					qtdnSuggestionsDate = qtdnSuggestionsDate.trim();
					String qtdnValue = formatter.formatCellValue(row.getCell(5));
					qtdnValue = qtdnValue.trim();
					String qtdnVtnetDate = formatter.formatCellValue(row.getCell(6));
					qtdnVtnetDate = qtdnVtnetDate.trim();
					String qtdnDescription = formatter.formatCellValue(row.getCell(7));
					qtdnDescription = qtdnDescription.trim();
					String qttdExpertiseEmployee = formatter.formatCellValue(row.getCell(8));
					qttdExpertiseEmployee = qttdExpertiseEmployee.trim();
					String qttdExpertiseCompleteDate = formatter.formatCellValue(row.getCell(9));
					qttdExpertiseCompleteDate = qttdExpertiseCompleteDate.trim();
					String qttdValue = formatter.formatCellValue(row.getCell(10));
					qttdValue = qttdValue.trim();
					String qttdDescription = formatter.formatCellValue(row.getCell(11));
					qttdDescription = qttdDescription.trim();

					ManageDataOutsideOsDTO obj = new ManageDataOutsideOsDTO();
					// validate cong trinh
					obj.setConstructionCode(constructionCode);
					obj.setStationCode(stationCode);
					obj.setHdContractCode(contractCode);
					
					obj.setQtdnDescription(!"".equals(qtdnDescription) ? qtdnDescription :"");
					obj.setQttdExpertiseEmployee(!"".equals(qttdExpertiseEmployee) ? qttdExpertiseEmployee : "");
					obj.setQttdDescription(!"".equals(qttdDescription) ? qttdDescription : "");
					if (validateString(qtdnSuggestionsDate)) {
						Date date=new SimpleDateFormat("dd/MM/yyyy").parse(qtdnSuggestionsDate);  
						obj.setQtdnSuggestionsDate(date);
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(5),
								colNameCDT.get(5) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					//
					if (validateString(qtdnVtnetDate)) {
						Date date=new SimpleDateFormat("dd/MM/yyyy").parse(qtdnVtnetDate);  
						obj.setQtdnVtnetDate(date);
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(7),
								colNameCDT.get(7) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					//
					if (validateString(qttdExpertiseCompleteDate)) {
						Date date=new SimpleDateFormat("dd/MM/yyyy").parse(qttdExpertiseCompleteDate);  
						obj.setQttdExpertiseCompleteDate(date);
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(10),
								colNameCDT.get(10) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					//
					  if(validateString(qtdnValue)){
	                    	if(qtdnValue.length() <= 20) {
	                            try{
	                                if(Double.parseDouble(qtdnValue)<0){
	                                    ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(6), colNameCDT.get(6)+ " nhỏ hơn 0");
	                                    errorList.add(errorDTO);
	                                } else
	                                	 obj.setQtdnValue(Double.parseDouble(qtdnValue));
	                            } catch(Exception e){
	                                ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(6), colNameCDT.get(6)+ " chỉ được nhập số");
	                                errorList.add(errorDTO);
	                            }
	                        } else {
	                            ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(6), colNameCDT.get(6)+ " có độ dài quá giới hạn");
	                            errorList.add(errorDTO);
	                        }
	                    }else {
	                        ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(6), colNameCDT.get(6)+ " bị bỏ trống");
	                        errorList.add(errorDTO);
	                    }
					  //
					  if(validateString(qttdValue)){
	                    	if(qttdValue.length() <= 20) {
	                            try{
	                                if(Double.parseDouble(qttdValue)<0){
	                                    ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(11), colNameCDT.get(11)+ " nhỏ hơn 0");
	                                    errorList.add(errorDTO);
	                                } else
	                                	 obj.setQttdValue(Double.parseDouble(qttdValue));
	                            } catch(Exception e){
	                                ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(11), colNameCDT.get(11)+ " chỉ được nhập số");
	                                errorList.add(errorDTO);
	                            }
	                        } else {
	                            ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(11), colNameCDT.get(11)+ " có độ dài quá giới hạn");
	                            errorList.add(errorDTO);
	                        }
	                    }else {
	                        ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(11), colNameCDT.get(11)+ " bị bỏ trống");
	                        errorList.add(errorDTO);
	                    }
					  
					  

					  
					  
					if (errorList.size() == 0) {
						workLst.add(obj);
					}
				}
			}

			if (errorList.size() > 0) {
				String filePathError = UEncrypt.encryptFileUploadPath(fileInput);
				List<ManageDataOutsideOsDTO> emptyArray = Lists.newArrayList();
				workLst = emptyArray;
				ManageDataOutsideOsDTO errorContainer = new ManageDataOutsideOsDTO();
				errorContainer.setErrorList(errorList);
				errorContainer.setMessageColumn(11); // cột dùng để in ra lỗi
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
			List<ManageDataOutsideOsDTO> emptyArray = Lists.newArrayList();
			workLst = emptyArray;
			ManageDataOutsideOsDTO errorContainer = new ManageDataOutsideOsDTO();
			errorContainer.setErrorList(errorList);
			errorContainer.setMessageColumn(11); // cột dùng để in ra lỗi
			errorContainer.setFilePathError(filePathError);
			workLst.add(errorContainer);
			return workLst;
		}
	}
	
	public List<ManageDataOutsideOsDTO> importInvoice(String fileInput) {
		List<ManageDataOutsideOsDTO> workLst = Lists.newArrayList();
		List<ExcelErrorDTO> errorList = new ArrayList<ExcelErrorDTO>();
		try {
			File f = new File(fileInput);
			XSSFWorkbook workbook = new XSSFWorkbook(f);
			XSSFSheet sheet = workbook.getSheetAt(0);
			DataFormatter formatter = new DataFormatter();
			int count = 0;
			// hienvd: check exit
			for (Row row : sheet) {
				count++;
				if (count >= 4 && checkIfRowIsEmpty(row))
					continue;
				if (count >= 4) {
					String constructionCode = formatter.formatCellValue(row.getCell(0));
					constructionCode = constructionCode.trim();
					String stationCode = formatter.formatCellValue(row.getCell(1));
					stationCode = stationCode.trim();
					String contractCode = formatter.formatCellValue(row.getCell(2));
					contractCode = contractCode.trim();
					String constructionType = formatter.formatCellValue(row.getCell(3));
					constructionType = constructionType.trim();
					String xhdPtcDate = formatter.formatCellValue(row.getCell(4));
					xhdPtcDate = xhdPtcDate.trim();
					String xhdXhdDate = formatter.formatCellValue(row.getCell(5));
					xhdXhdDate = xhdXhdDate.trim();
					String xhdSoHd = formatter.formatCellValue(row.getCell(6));
					xhdSoHd = xhdSoHd.trim();
					String xhdRevenueMonth = formatter.formatCellValue(row.getCell(7));
					xhdRevenueMonth = xhdRevenueMonth.trim();
					String xhdDescription = formatter.formatCellValue(row.getCell(8));
					xhdDescription = xhdDescription.trim();
					

					ManageDataOutsideOsDTO obj = new ManageDataOutsideOsDTO();
					// validate cong trinh
					obj.setConstructionCode(constructionCode);
					obj.setStationCode(stationCode);
					obj.setHdContractCode(contractCode);
					
					obj.setXhdDescription(!"".equals(xhdDescription) ? xhdDescription :"");
					if (validateString(xhdPtcDate)) {
						Date date=new SimpleDateFormat("dd/MM/yyyy").parse(xhdPtcDate);  
						obj.setXhdPtcDate(date);
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(5),
								colNameInvoice.get(5) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					//
					if (validateString(xhdXhdDate)) {
						Date date=new SimpleDateFormat("dd/MM/yyyy").parse(xhdXhdDate);  
						obj.setXhdXhdDate(date);
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(6),
								colNameInvoice.get(6) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					//
					if (validateString(xhdSoHd)) {
						obj.setXhdSoHd(xhdSoHd);
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(7),
								colNameInvoice.get(7) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					//
					if (validateString(xhdRevenueMonth)) {
						if(ValidateUtils.isValidFormat("MM/yyyy", xhdRevenueMonth)==null) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(8),
									colNameInvoice.get(8) + " sai định dạng 'MM/yyyy'");
							errorList.add(errorDTO);
						} else {
							obj.setXhdRevenueMonth(xhdRevenueMonth);
						}
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(8),
								colNameInvoice.get(8) + " bị bỏ trống");
						errorList.add(errorDTO);
					} 
					  

					  
					  
					if (errorList.size() == 0) {
						workLst.add(obj);
					}
				}
			}

			if (errorList.size() > 0) {
				String filePathError = UEncrypt.encryptFileUploadPath(fileInput);
				List<ManageDataOutsideOsDTO> emptyArray = Lists.newArrayList();
				workLst = emptyArray;
				ManageDataOutsideOsDTO errorContainer = new ManageDataOutsideOsDTO();
				errorContainer.setErrorList(errorList);
				errorContainer.setMessageColumn(8); // cột dùng để in ra lỗi
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
			List<ManageDataOutsideOsDTO> emptyArray = Lists.newArrayList();
			workLst = emptyArray;
			ManageDataOutsideOsDTO errorContainer = new ManageDataOutsideOsDTO();
			errorContainer.setErrorList(errorList);
			errorContainer.setMessageColumn(8); // cột dùng để in ra lỗi
			errorContainer.setFilePathError(filePathError);
			workLst.add(errorContainer);
			return workLst;
		}
	}
	
	public List<ManageDataOutsideOsDTO> importLiquidation(String fileInput) {
		List<ManageDataOutsideOsDTO> workLst = Lists.newArrayList();
		List<ExcelErrorDTO> errorList = new ArrayList<ExcelErrorDTO>();
		try {
			File f = new File(fileInput);
			XSSFWorkbook workbook = new XSSFWorkbook(f);
			XSSFSheet sheet = workbook.getSheetAt(0);
			DataFormatter formatter = new DataFormatter();
			int count = 0;
			// hienvd: check exit
			for (Row row : sheet) {
				count++;
				if (count >= 4 && checkIfRowIsEmpty(row))
					continue;
				if (count >= 4) {
					String constructionCode = formatter.formatCellValue(row.getCell(0));
					constructionCode = constructionCode.trim();
					String stationCode = formatter.formatCellValue(row.getCell(1));
					stationCode = stationCode.trim();
					String contractCode = formatter.formatCellValue(row.getCell(2));
					contractCode = contractCode.trim();
					String constructionType = formatter.formatCellValue(row.getCell(3));
					constructionType = constructionType.trim();
					String tlSignDate = formatter.formatCellValue(row.getCell(4));
					tlSignDate = tlSignDate.trim();
					String tlValue = formatter.formatCellValue(row.getCell(5));
					tlValue = tlValue.trim();
					String tlDescription = formatter.formatCellValue(row.getCell(6));
					tlDescription = tlDescription.trim();
					String tlDifferenceQuantity = formatter.formatCellValue(row.getCell(7));
					tlDifferenceQuantity = tlDifferenceQuantity.trim();
					String tlRate = formatter.formatCellValue(row.getCell(8));
					tlRate = tlRate.trim();
					

					ManageDataOutsideOsDTO obj = new ManageDataOutsideOsDTO();
					// validate cong trinh
					obj.setConstructionCode(constructionCode);
					obj.setStationCode(stationCode);
					obj.setHdContractCode(contractCode);
					
					obj.setTlDescription(!"".equals(tlDescription) ? tlDescription :"");
					obj.setTlDifferenceQuantity(tlDifferenceQuantity != null && !"".equals(tlDifferenceQuantity) ?Double.parseDouble(tlDifferenceQuantity)  :0D);
					obj.setTlRate(tlRate != null && !"".equals(tlRate) ?Double.parseDouble(tlRate)  :0D);
					if (validateString(tlSignDate)) {
						Date date=new SimpleDateFormat("dd/MM/yyyy").parse(tlSignDate);  
						obj.setTlSignDate(date);
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(5),
								colNameLiquidation.get(5) + " bị bỏ trống");
						errorList.add(errorDTO);
					}
					//
					 if(validateString(tlValue)){
	                    	if(tlValue.length() <= 20) {
	                            try{
	                                if(Double.parseDouble(tlValue)<0){
	                                    ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(6), colNameCDT.get(6)+ " nhỏ hơn 0");
	                                    errorList.add(errorDTO);
	                                } else
	                                	 obj.setTlValue(Double.parseDouble(tlValue));
	                            } catch(Exception e){
	                                ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(6), colNameCDT.get(6)+ " chỉ được nhập số");
	                                errorList.add(errorDTO);
	                            }
	                        } else {
	                            ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(6), colNameCDT.get(6)+ " có độ dài quá giới hạn");
	                            errorList.add(errorDTO);
	                        }
	                    }else {
	                        ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(6), colNameCDT.get(6)+ " bị bỏ trống");
	                        errorList.add(errorDTO);
	                    }
					  

					  
					  
					if (errorList.size() == 0) {
						workLst.add(obj);
					}
				}
			}

			if (errorList.size() > 0) {
				String filePathError = UEncrypt.encryptFileUploadPath(fileInput);
				List<ManageDataOutsideOsDTO> emptyArray = Lists.newArrayList();
				workLst = emptyArray;
				ManageDataOutsideOsDTO errorContainer = new ManageDataOutsideOsDTO();
				errorContainer.setErrorList(errorList);
				errorContainer.setMessageColumn(8); // cột dùng để in ra lỗi
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
			List<ManageDataOutsideOsDTO> emptyArray = Lists.newArrayList();
			workLst = emptyArray;
			ManageDataOutsideOsDTO errorContainer = new ManageDataOutsideOsDTO();
			errorContainer.setErrorList(errorList);
			errorContainer.setMessageColumn(8); // cột dùng để in ra lỗi
			errorContainer.setFilePathError(filePathError);
			workLst.add(errorContainer);
			return workLst;
		}
	}

	
	public void updateManaOs(ManageDataOutsideOsDTO manageDataOutsideOsDTO) {
		 manageDataOutsideOsDAO.updateManaOs(manageDataOutsideOsDTO);
	}
	
	public void addManaOs(ManageDataOutsideOsDTO manageDataOutsideOsDTO) {
		 manageDataOutsideOsDAO.addManaOs(manageDataOutsideOsDTO);
	}
	// tatph - end 13/11/2019
	
	//HuyPQ-20191114
	
	//Export file biểu mẫu công trình hợp đồng
	public String exportTemplateConsContract(ManageDataOutsideOsDTO obj) throws Exception {
		obj.setPage(null);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "BM_CongTrinh_HopDong.xlsx"));
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
		OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "BM_CongTrinh_HopDong.xlsx");
		
		List<ManageDataOutsideOsDTO> data = manageDataOutsideOsDAO.getAutoCompleteConstruction(new ManageDataOutsideOsDTO());
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		XSSFSheet sheet = workbook.getSheetAt(1);
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			int i = 1;
			for (ManageDataOutsideOsDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 1));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getConstructionCode() != null) ? dto.getConstructionCode() : "");
				cell.setCellStyle(style);
			}
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "BM_CongTrinh_HopDong.xlsx");
		return path;
	}
	
	// Export file biểu mẫu lập tiến độ
	public String exportTemplateLapTienDo(ManageDataOutsideOsDTO obj) throws Exception {
		obj.setPage(null);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "BM_Lap_TienDo.xlsx"));
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
		OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "BM_Lap_TienDo.xlsx");
		obj.setStatus("1");
		List<ManageDataOutsideOsDTO> data = manageDataOutsideOsDAO.doSearchOS(obj);
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			// HuyPQ-22/08/2018-start
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			// HuyPQ-end
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			int i = 5;
			for (ManageDataOutsideOsDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 5));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getConstructionCode() != null) ? dto.getConstructionCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getHdContractCode() != null) ? dto.getHdContractCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getStationCode() != null) ? dto.getStationCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getConstructionTypeName() != null) ? dto.getConstructionTypeName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
			}
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "BM_Lap_TienDo.xlsx");
		return path;
	}

	// Export file biểu mẫu Đề nghị quyết toán
	public String exportTemplateDNQT(ManageDataOutsideOsDTO obj) throws Exception {
		obj.setPage(null);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "BM_DeNghi_QuyetToan.xlsx"));
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
				udir.getAbsolutePath() + File.separator + "BM_DeNghi_QuyetToan.xlsx");
		obj.setStatus("2");
		List<ManageDataOutsideOsDTO> data = manageDataOutsideOsDAO.doSearchOS(obj);
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			// HuyPQ-22/08/2018-start
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			// HuyPQ-end
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			int i = 4;
			for (ManageDataOutsideOsDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 4));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getConstructionCode() != null) ? dto.getConstructionCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getHdContractCode() != null) ? dto.getHdContractCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getStationCode() != null) ? dto.getStationCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getConstructionTypeName() != null) ? dto.getConstructionTypeName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				cell = row.createCell(14, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				cell = row.createCell(15, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				cell = row.createCell(16, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
			}
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "BM_DeNghi_QuyetToan.xlsx");
		return path;
	}

	// Export file biểu mẫu Quyết toán nhân công
	public String exportTemplateQuyetToanNC(ManageDataOutsideOsDTO obj) throws Exception {
		obj.setPage(null);
		obj.setPageSize(null);
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "BM_QuyetToan_NhanCong.xlsx"));
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
				udir.getAbsolutePath() + File.separator + "BM_QuyetToan_NhanCong.xlsx");
		List<String> listStatus = new ArrayList<>();
		listStatus.add("6");
		listStatus.add("7");
		obj.setStatus(null);
		obj.setListStatus(listStatus);
		List<ManageDataOutsideOsDTO> data = manageDataOutsideOsDAO.doSearchOS(obj);
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			// HuyPQ-22/08/2018-start
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			// HuyPQ-end
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			int i = 4;
			for (ManageDataOutsideOsDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 4));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getConstructionCode() != null) ? dto.getConstructionCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getHdContractCode() != null) ? dto.getHdContractCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getStationCode() != null) ? dto.getStationCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getConstructionTypeName() != null) ? dto.getConstructionTypeName() : "");
				cell.setCellStyle(style);
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue("");
				cell.setCellStyle(style);
			}
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "BM_QuyetToan_NhanCong.xlsx");
		return path;
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
	
	//---------------Import khai báo công trình hợp đồng---------------//
	public List<ManageDataOutsideOsDTO> importConsContract(String fileInput) {
        List<ManageDataOutsideOsDTO> workLst = Lists.newArrayList();
        List<ExcelErrorDTO> errorList = new ArrayList<ExcelErrorDTO>();
        try {
            File f = new File(fileInput);
            XSSFWorkbook workbook = new XSSFWorkbook(f);
            XSSFSheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();
            int count = 0;

            ManageDataOutsideOsDTO consWi = new ManageDataOutsideOsDTO();
            consWi.setPage(null);
            consWi.setPageSize(null);
            
            List<ManageDataOutsideOsDTO> listDataCons = manageDataOutsideOsDAO.getAutoCompleteConstruction(consWi);
            HashMap<String, ManageDataOutsideOsDTO> mapCons = new HashMap<>();
            for(ManageDataOutsideOsDTO dto : listDataCons) {
            	mapCons.put(dto.getConstructionCode().toUpperCase().trim(), dto);
            }
            
            List<ManageDataOutsideOsDTO> listConsManage = manageDataOutsideOsDAO.getDataManageOutsideOs();
            HashMap<String, ManageDataOutsideOsDTO> mapConsManage = new HashMap<>();
            for(ManageDataOutsideOsDTO dto : listConsManage) {
            	mapConsManage.put(dto.getConstructionCode().toUpperCase().trim(), dto);
            }
            
            for (Row row : sheet) {
                count++;
                if(count >= 6 && checkIfRowIsEmpty(row)) continue;
                if (count >= 6) {
                	String hdSignDate = formatter.formatCellValue(row.getCell(1)).trim();
                    String hdContractCode = formatter.formatCellValue(row.getCell(2)).trim();
                    String hdContractValue = formatter.formatCellValue(row.getCell(3)).trim();
                    String hdPerformDay = formatter.formatCellValue(row.getCell(4)).trim();
                    String provinceCode = formatter.formatCellValue(row.getCell(5)).trim();
                    String constructionCode = formatter.formatCellValue(row.getCell(6)).trim();
                    String stationCode = formatter.formatCellValue(row.getCell(7)).trim();
                    String constructionType = formatter.formatCellValue(row.getCell(8)).trim();
                    String content = formatter.formatCellValue(row.getCell(9)).trim();
                    String capitalNtd = formatter.formatCellValue(row.getCell(10)).trim();
                    String khtcSalary = formatter.formatCellValue(row.getCell(11)).trim();
                    String khtcLaborOutsource = formatter.formatCellValue(row.getCell(12)).trim();
                    String khtcCostMaterial = formatter.formatCellValue(row.getCell(13)).trim();
                    String khtcCostHshc = formatter.formatCellValue(row.getCell(14)).trim();
                    String khtcCostTransport = formatter.formatCellValue(row.getCell(15)).trim();
                    String khtcCostOrther = formatter.formatCellValue(row.getCell(16)).trim();
                    String khtcDeploymentMonth = formatter.formatCellValue(row.getCell(17)).trim();
                    String khtcTotalMoney = formatter.formatCellValue(row.getCell(18)).trim();
                    String khtcEffective = formatter.formatCellValue(row.getCell(19)).trim();
                    String khtcDescription = formatter.formatCellValue(row.getCell(20)).trim();
                    String tuAdvanceDate = formatter.formatCellValue(row.getCell(21)).trim();
                    String tuLabor = formatter.formatCellValue(row.getCell(22)).trim();
                    String tuMaterial = formatter.formatCellValue(row.getCell(23)).trim();
                    String tuHshc = formatter.formatCellValue(row.getCell(24)).trim();
                    String tuCostTransport = formatter.formatCellValue(row.getCell(25)).trim();
                    String tuCostOrther = formatter.formatCellValue(row.getCell(26)).trim();
                    String vtaSynchronizeDate = formatter.formatCellValue(row.getCell(27)).trim();
                    String vtaValue = formatter.formatCellValue(row.getCell(28)).trim();
                    String gtslQuantityValue = formatter.formatCellValue(row.getCell(29)).trim();
                    String httcTdt = formatter.formatCellValue(row.getCell(30)).trim();
                    String httcTctt = formatter.formatCellValue(row.getCell(31)).trim();
                    String httcKn = formatter.formatCellValue(row.getCell(32)).trim();
                    String tttcStartDate = formatter.formatCellValue(row.getCell(33)).trim();
                    String tttcEndDate = formatter.formatCellValue(row.getCell(34)).trim();
                    String tttcVuong = formatter.formatCellValue(row.getCell(35)).trim();
                    String tttcClose = formatter.formatCellValue(row.getCell(36)).trim();
                    String gtslCompleteExpectedDate = formatter.formatCellValue(row.getCell(37)).trim();
                    String gtslDescription = formatter.formatCellValue(row.getCell(38)).trim();
                   
                    ManageDataOutsideOsDTO obj = new ManageDataOutsideOsDTO();
                    
                    if (validateString(constructionCode)) {
                    	ManageDataOutsideOsDTO dto = mapCons.get(constructionCode.toUpperCase().trim());
						if(dto==null) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(6),
									colNameNew.get(6) + " không tồn tại hoặc chưa được gán với hợp đồng nào");
							errorList.add(errorDTO);
						} else {
							ManageDataOutsideOsDTO check = mapConsManage.get(constructionCode.toUpperCase().trim());
							if(check!=null) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(6),
										colNameNew.get(6) + " đã tồn tại trong quản lý dữ liệu ngoài OS");
								errorList.add(errorDTO);
							} else {
								SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
								String dateString = df.format(dto.getHdSignDate());
								Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
								obj.setConstructionCode(constructionCode);
								obj.setHdSignDate(date);
								obj.setHdContractCode(dto.getHdContractCode());
								obj.setHdContractValue(dto.getHdContractValue());
								obj.setHdPerformDay(dto.getHdPerformDay());
								obj.setProvinceCode(dto.getProvinceCode());
								obj.setStationCode(dto.getStationCode());
							}
						}
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(6),
								colNameNew.get(6) + " không được bỏ trống");
						errorList.add(errorDTO);
					}
                    
                    if (validateString(constructionType)) {
                    	try {
							if (Long.parseLong(constructionType) < 0) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(8),
										colNameNew.get(8) + " không hợp lệ");
								errorList.add(errorDTO);
							} else
								obj.setConstructionType(constructionType);
						} catch (Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(8),
									colNameNew.get(8) + " sai định dạng số");
							errorList.add(errorDTO);
						}
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(8),
								colNameNew.get(8) + " không được bỏ trống");
						errorList.add(errorDTO);
					}
                    
					if (validateString(content)) {
						obj.setContent(content);
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(9),
								colNameNew.get(9) + " không được bỏ trống");
						errorList.add(errorDTO);
					}
					
					if (validateString(capitalNtd)) {
						Long capital = Long.parseLong(capitalNtd);
						try {
							if (Long.parseLong(capitalNtd) < 0) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(10),
										colNameNew.get(10) + " không hợp lệ");
								errorList.add(errorDTO);
							} else
								obj.setCapitalNtd(capitalNtd);
						} catch (Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(10),
									colNameNew.get(10) + " sai định dạng số");
							errorList.add(errorDTO);
						}
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(10),
								colNameNew.get(10) + " không được bỏ trống");
						errorList.add(errorDTO);
					}

					if (validateString(khtcSalary)) {
						try {
							if (Double.parseDouble(khtcSalary) < 0) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(11),
										colNameNew.get(11) + " không được nhỏ hơn 0");
								errorList.add(errorDTO);
							} else
								obj.setKhtcSalary(Double.parseDouble(khtcSalary));
						} catch (Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(11),
									colNameNew.get(11) + " sai định dạng số");
							errorList.add(errorDTO);
						}
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(11),
								colNameNew.get(11) + " không được bỏ trống");
						errorList.add(errorDTO);
					}

					if (validateString(khtcLaborOutsource)) {
						try {
							if (Double.parseDouble(khtcLaborOutsource) < 0) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(12),
										colNameNew.get(12) + " không được nhỏ hơn 0");
								errorList.add(errorDTO);
							} else
								obj.setKhtcLaborOutsource(Double.parseDouble(khtcLaborOutsource));
						} catch (Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(12),
									colNameNew.get(12) + " sai định dạng số");
							errorList.add(errorDTO);
						}
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(12),
								colNameNew.get(12) + " không được bỏ trống");
						errorList.add(errorDTO);
					}
					
					if (validateString(khtcCostMaterial)) {
						try {
							if (Double.parseDouble(khtcCostMaterial) < 0) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(13),
										colNameNew.get(13) + " không được nhỏ hơn 0");
								errorList.add(errorDTO);
							} else
								obj.setKhtcCostMaterial(Double.parseDouble(khtcCostMaterial));
						} catch (Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(13),
									colNameNew.get(13) + " sai định dạng số");
							errorList.add(errorDTO);
						}
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(13),
								colNameNew.get(13) + " không được bỏ trống");
						errorList.add(errorDTO);
					}
					
					if (validateString(khtcCostHshc)) {
						try {
							if (Double.parseDouble(khtcCostHshc) < 0) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(14),
										colNameNew.get(14) + " không được nhỏ hơn 0");
								errorList.add(errorDTO);
							} else
								obj.setKhtcCostHshc(Double.parseDouble(khtcCostHshc));
						} catch (Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(14),
									colNameNew.get(14) + " sai định dạng số");
							errorList.add(errorDTO);
						}
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(14),
								colNameNew.get(14) + " không được bỏ trống");
						errorList.add(errorDTO);
					}
					
					if (validateString(khtcCostTransport)) {
						try {
							if (Double.parseDouble(khtcCostTransport) < 0) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(15),
										colNameNew.get(15) + " không được nhỏ hơn 0");
								errorList.add(errorDTO);
							} else
								obj.setKhtcCostTransport(Double.parseDouble(khtcCostTransport));
						} catch (Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(15),
									colNameNew.get(15) + " sai định dạng số");
							errorList.add(errorDTO);
						}
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(15),
								colNameNew.get(15) + " không được bỏ trống");
						errorList.add(errorDTO);
					}
					
					if (validateString(khtcCostOrther)) {
						try {
							if (Double.parseDouble(khtcCostOrther) < 0) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(16),
										colNameNew.get(16) + " không được nhỏ hơn 0");
								errorList.add(errorDTO);
							} else
								obj.setKhtcCostOrther(Double.parseDouble(khtcCostOrther));
						} catch (Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(16),
									colNameNew.get(16) + " sai định dạng số");
							errorList.add(errorDTO);
						}
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(16),
								colNameNew.get(16) + " không được bỏ trống");
						errorList.add(errorDTO);
					}
					
					if (validateString(khtcDeploymentMonth)) {
						if (ValidateUtils.isValidFormat("MM/yyyy",khtcDeploymentMonth)==null) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(17),
									colNameNew.get(17) + " sai định dạng 'MM/yyyy'");
							errorList.add(errorDTO);
						} else
							obj.setKhtcDeploymentMonth(khtcDeploymentMonth);
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(17),
								colNameNew.get(17) + " không được bỏ trống");
						errorList.add(errorDTO);
					}
					
					if (validateString(khtcTotalMoney)) {
						try {
							if (Double.parseDouble(khtcTotalMoney) < 0) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(18),
										colNameNew.get(18) + " không được nhỏ hơn 0");
								errorList.add(errorDTO);
							} else
								obj.setKhtcTotalMoney(Double.parseDouble(khtcTotalMoney));
						} catch (Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(18),
									colNameNew.get(18) + " sai định dạng số");
							errorList.add(errorDTO);
						}
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(18),
								colNameNew.get(18) + " không được bỏ trống");
						errorList.add(errorDTO);
					}
					
//					if (validateString(khtcEffective)) {
//						try {
//							if (Double.parseDouble(khtcEffective) < 0) {
//								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(19),
//										colNameNew.get(19) + " không được nhỏ hơn 0");
//								errorList.add(errorDTO);
//							} else
//								obj.setKhtcEffective(Double.parseDouble(khtcEffective));
//						} catch (Exception e) {
//							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(19),
//									colNameNew.get(19) + " sai định dạng số");
//							errorList.add(errorDTO);
//						}
//					} else {
//						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(19),
//								colNameNew.get(19) + " không được bỏ trống");
//						errorList.add(errorDTO);
//					}
					
					obj.setKhtcDescription(khtcDescription);
					
					if (validateString(tuAdvanceDate)) {
						if (ValidateUtils.isValidFormat("dd/MM/yyyy",tuAdvanceDate) == null) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(21),
									colNameNew.get(21) + " sai định dạng 'dd/MM/yyyy'");
							errorList.add(errorDTO);
						} else {
							obj.setTuAdvanceDate(ValidateUtils.isValidFormat("dd/MM/yyyy",tuAdvanceDate));
						}
					}
					
					if (validateString(tuLabor)) {
						if (Double.parseDouble(tuLabor) < 0) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(22),
									colNameNew.get(22) + " không được nhỏ hơn 0");
							errorList.add(errorDTO);
						} else {
							obj.setTuLabor(Double.parseDouble(tuLabor));
						}
					}
					
					if (validateString(tuMaterial)) {
						if (Double.parseDouble(tuMaterial) < 0) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(23),
									colNameNew.get(23) + " không được nhỏ hơn 0");
							errorList.add(errorDTO);
						} else {
							obj.setTuMaterial(Double.parseDouble(tuMaterial));
						}
					}
					
					if (validateString(tuHshc)) {
						if (Double.parseDouble(tuHshc) < 0) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(24),
									colNameNew.get(24) + " không được nhỏ hơn 0");
							errorList.add(errorDTO);
						} else {
							obj.setTuHshc(Double.parseDouble(tuHshc));
						}
					}
					
					if (validateString(tuCostTransport)) {
						if (Double.parseDouble(tuCostTransport) < 0) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(25),
									colNameNew.get(25) + " không được nhỏ hơn 0");
							errorList.add(errorDTO);
						} else {
							obj.setTuCostTransport(Double.parseDouble(tuCostTransport));
						}
					}
					
					if (validateString(tuCostOrther)) {
						if (Double.parseDouble(tuCostOrther) < 0) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(26),
									colNameNew.get(26) + " không được nhỏ hơn 0");
							errorList.add(errorDTO);
						} else {
							obj.setTuCostOrther(Double.parseDouble(tuCostOrther));
						}
					}
					
					if (validateString(vtaSynchronizeDate)) {
						if (ValidateUtils.isValidFormat("dd/MM/yyyy",vtaSynchronizeDate) == null) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(27),
									colNameNew.get(27) + " sai định dạng 'dd/MM/yyyy'");
							errorList.add(errorDTO);
						} else {
							obj.setVtaSynchronizeDate(ValidateUtils.isValidFormat("dd/MM/yyyy",vtaSynchronizeDate));
						}
					}
					
					if (validateString(vtaValue)) {
						if (Double.parseDouble(vtaValue) < 0) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(28),
									colNameNew.get(28) + " không được nhỏ hơn 0");
							errorList.add(errorDTO);
						} else {
							obj.setVtaValue(Double.parseDouble(vtaValue));
						}
					}
					
					if (validateString(gtslQuantityValue)) {
						try {
							if (Double.parseDouble(gtslQuantityValue) < 0) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(29),
										colNameNew.get(29) + " không được nhỏ hơn 0");
								errorList.add(errorDTO);
							} else
								obj.setGtslQuantityValue(Double.parseDouble(gtslQuantityValue));
						} catch (Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(29),
									colNameNew.get(29) + " sai định dạng số");
							errorList.add(errorDTO);
						}
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(29),
								colNameNew.get(29) + " không được bỏ trống");
						errorList.add(errorDTO);
					}
					
					if(!validateString(httcTdt) && !validateString(httcTctt) && !validateString(httcKn)) {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(30),
								colNameNew.get(66) + " không được để trống");
						errorList.add(errorDTO);
					} else {
						if (validateString(httcTdt)) {
							obj.setHttcTdt("1");
						}
						
						if (validateString(httcTctt)) {
							obj.setHttcTctt("2");
						}

						if (validateString(httcKn)) {
							obj.setHttcKn("3");
						}
					}
					
					if (validateString(tttcStartDate)) {
						if (ValidateUtils.isValidFormat("dd/MM/yyyy",tttcStartDate) == null) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(33),
									colNameNew.get(33) + " sai định dạng 'dd/MM/yyyy'");
							errorList.add(errorDTO);
						} else
							obj.setTttcStartDate(ValidateUtils.isValidFormat("dd/MM/yyyy",tttcStartDate));
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(33),
								colNameNew.get(33) + " không được bỏ trống");
						errorList.add(errorDTO);
					}
					
					if (validateString(tttcEndDate)) {
						if (ValidateUtils.isValidFormat("dd/MM/yyyy",tttcEndDate) == null) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(34),
									colNameNew.get(34) + " sai định dạng 'dd/MM/yyyy'");
							errorList.add(errorDTO);
						} else {
							obj.setTttcEndDate(ValidateUtils.isValidFormat("dd/MM/yyyy",tttcEndDate));
						}
					}
					
					obj.setTttcVuong(tttcVuong);
					obj.setTttcClose(tttcClose);
					
					if (validateString(gtslCompleteExpectedDate)) {
						if (ValidateUtils.isValidFormat("dd/MM/yyyy",gtslCompleteExpectedDate) == null) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(37),
									colNameNew.get(37) + " sai định dạng 'dd/MM/yyyy'");
							errorList.add(errorDTO);
						} else
							obj.setGtslCompleteExpectedDate(ValidateUtils.isValidFormat("dd/MM/yyyy",gtslCompleteExpectedDate));
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(37),
								colNameNew.get(37) + " không được bỏ trống");
						errorList.add(errorDTO);
					}
					
					obj.setGtslDescription(gtslDescription);
					
					/////
                    if(errorList.size() == 0){
                        workLst.add(obj);
                    }
                }
            }

            if(errorList.size() > 0){
                String filePathError = UEncrypt.encryptFileUploadPath(fileInput);
                List<ManageDataOutsideOsDTO> emptyArray = Lists.newArrayList();
                workLst = emptyArray;
                ManageDataOutsideOsDTO errorContainer = new ManageDataOutsideOsDTO();
                errorContainer.setErrorList(errorList);
                errorContainer.setMessageColumn(39);
                errorContainer.setFilePathError(filePathError);
                workLst.add(errorContainer);
            }
            workbook.close();
            return workLst;

        }  catch (Exception e) {
            ExcelErrorDTO errorDTO = createError(0, "", e.toString());
            errorList.add(errorDTO);
            String filePathError = null;
            try {
                filePathError = UEncrypt.encryptFileUploadPath(fileInput);
            } catch(Exception ex) {
                errorDTO = createError(0, "", ex.toString());
                errorList.add(errorDTO);
            }
            List<ManageDataOutsideOsDTO> emptyArray = Lists.newArrayList();
            workLst = emptyArray;
            ManageDataOutsideOsDTO errorContainer = new ManageDataOutsideOsDTO();
            errorContainer.setErrorList(errorList);
            errorContainer.setMessageColumn(39);
            errorContainer.setFilePathError(filePathError);
            workLst.add(errorContainer);
            return workLst;
        }
    }
	
	 public void saveListNew(List<ManageDataOutsideOsDTO> list, HttpServletRequest request) {
		 KttsUserSession objUser = userRoleBusinessImpl.getUserSession(request);
		 for(ManageDataOutsideOsDTO dto : list) {
			 dto.setStatus("1");
			 dto.setCreatedDate(new Date());
			 dto.setCreatedUserId(objUser.getSysUserId());
			 dto.setKhtcEffective((double)(Math.round(dto.getKhtcTotalMoney()/dto.getHdContractValue()*100000d)/1000d));
			 manageDataOutsideOsDAO.saveObject(dto.toModel());
		 }
     }
	 
	 //--------------Import Lập tiên độ----------------//
	 HashMap<Integer, String> colNameSchedule = new HashMap();
	    {
	    	colNameSchedule.put(1,"Mã công trình");
	    	colNameSchedule.put(2,"Mã hợp đồng");
	    	colNameSchedule.put(3,"Mã trạm");
	    	colNameSchedule.put(4,"Loại công trình");
	    	colNameSchedule.put(5,"Ngày bắt đầu dựng HSHC");
	    	colNameSchedule.put(6,"Ngày bắt đầu nghiệm thu");
	    	colNameSchedule.put(7,"Ngày gửi P.KTHT thẩm duyệt");
	    	colNameSchedule.put(8,"Ngày bắt đầu làm đối soát 4A");
	    	colNameSchedule.put(9,"Ngày trình ký GĐ tỉnh");
	    	colNameSchedule.put(10,"Ngày gửi Hồ sơ lên TCT");
	    	colNameSchedule.put(11,"Ngày dự kiến hoàn thành");
	    	colNameSchedule.put(12,"Ngày vướng");
	    	colNameSchedule.put(13,"Nguyên nhân vướng");
	    }
	 
	    public List<ManageDataOutsideOsDTO> importSchedule(String fileInput) {
	        List<ManageDataOutsideOsDTO> workLst = Lists.newArrayList();
	        List<ExcelErrorDTO> errorList = new ArrayList<ExcelErrorDTO>();
	        try {
	            File f = new File(fileInput);
	            XSSFWorkbook workbook = new XSSFWorkbook(f);
	            XSSFSheet sheet = workbook.getSheetAt(0);
	            DataFormatter formatter = new DataFormatter();
	            int count = 0;

	            ManageDataOutsideOsDTO consWi = new ManageDataOutsideOsDTO();
	            consWi.setPage(null);
	            consWi.setPageSize(null);
	            
	            List<ManageDataOutsideOsDTO> listDataCons = manageDataOutsideOsDAO.getAutoCompleteConstruction(consWi);
	            HashMap<String, ManageDataOutsideOsDTO> mapCons = new HashMap<>();
	            for(ManageDataOutsideOsDTO dto : listDataCons) {
	            	mapCons.put(dto.getConstructionCode().toUpperCase().trim(), dto);
	            }
	            
	            List<ManageDataOutsideOsDTO> listStatus = manageDataOutsideOsDAO.getDataManageOutsideOs();
	            HashMap<String, String> mapStatus = new HashMap<>();
	            for(ManageDataOutsideOsDTO dto : listStatus) {
	            	mapStatus.put(dto.getConstructionCode().toUpperCase().trim(), dto.getStatus());
	            }
	            
	            for (Row row : sheet) {
	                count++;
	                if(count >= 6 && checkIfRowIsEmpty(row)) continue;
	                if (count >= 6) {
	                	String constructionCode = formatter.formatCellValue(row.getCell(1));
	                	String hdContractCode = formatter.formatCellValue(row.getCell(2));
	                	String stationCode = formatter.formatCellValue(row.getCell(3));
	                	String constructionType = formatter.formatCellValue(row.getCell(4));
	                	String tdntHshcStartDate = formatter.formatCellValue(row.getCell(5));
	                	String tdntAcceptanceStartDate = formatter.formatCellValue(row.getCell(6));
	                	String tdntKthtExpertiseDate = formatter.formatCellValue(row.getCell(7));
	                	String tdnt4AControlStartDate = formatter.formatCellValue(row.getCell(8));
	                	String tdntSignProvinceDate = formatter.formatCellValue(row.getCell(9));
	                	String tdntSendTctDate = formatter.formatCellValue(row.getCell(10));
	                	String tdntCompleteExpectedDate = formatter.formatCellValue(row.getCell(11));
	                	String tdntVuongDate = formatter.formatCellValue(row.getCell(12));
	                	String tdntVuongReason = formatter.formatCellValue(row.getCell(13));
	                   
	                    ManageDataOutsideOsDTO obj = new ManageDataOutsideOsDTO();
	                    
	                    if (validateString(constructionCode)) {
	                    	ManageDataOutsideOsDTO dto = mapCons.get(constructionCode.toUpperCase().trim());
							if(dto==null) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(1),
										colNameSchedule.get(1) + " không tồn tại hoặc chưa được gán với hợp đồng nào");
								errorList.add(errorDTO);
							} else {
								String checkStatus = mapStatus.get(constructionCode.toUpperCase().trim());
								if(!checkStatus.equals("1")) {
									ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(1),
											colNameSchedule.get(1) + " không đúng trạng thái lập tiến độ");
									errorList.add(errorDTO);
								} else {
									obj.setConstructionCode(constructionCode);
								}
							}
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(1),
									colNameSchedule.get(1) + " không được bỏ trống");
							errorList.add(errorDTO);
						}
	                    
	                    if (validateString(tdntHshcStartDate)) {
							if (ValidateUtils.isValidFormat("dd/MM/yyyy",tdntHshcStartDate) == null) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(5),
										colNameSchedule.get(5) + " sai định dạng 'dd/MM/yyyy'");
								errorList.add(errorDTO);
							} else
								obj.setTdntHshcStartDate(ValidateUtils.isValidFormat("dd/MM/yyyy",tdntHshcStartDate));
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(5),
									colNameSchedule.get(5) + " không được bỏ trống");
							errorList.add(errorDTO);
						}
	                    
	                    if (validateString(tdntAcceptanceStartDate)) {
							if (ValidateUtils.isValidFormat("dd/MM/yyyy",tdntAcceptanceStartDate) == null) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(6),
										colNameSchedule.get(6) + " sai định dạng 'dd/MM/yyyy'");
								errorList.add(errorDTO);
							} else
								obj.setTdntAcceptanceStartDate(ValidateUtils.isValidFormat("dd/MM/yyyy",tdntAcceptanceStartDate));
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(6),
									colNameSchedule.get(6) + " không được bỏ trống");
							errorList.add(errorDTO);
						}
	                    
	                    if (validateString(tdntKthtExpertiseDate)) {
							if (ValidateUtils.isValidFormat("dd/MM/yyyy",tdntKthtExpertiseDate) == null) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(7),
										colNameSchedule.get(7) + " sai định dạng 'dd/MM/yyyy'");
								errorList.add(errorDTO);
							} else
								obj.setTdntKthtExpertiseDate(ValidateUtils.isValidFormat("dd/MM/yyyy",tdntKthtExpertiseDate));
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(7),
									colNameSchedule.get(7) + " không được bỏ trống");
							errorList.add(errorDTO);
						}
	                    
	                    if (validateString(tdnt4AControlStartDate)) {
							if (ValidateUtils.isValidFormat("dd/MM/yyyy",tdnt4AControlStartDate) == null) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(8),
										colNameSchedule.get(8) + " sai định dạng 'dd/MM/yyyy'");
								errorList.add(errorDTO);
							} else
								obj.setTdnt4AControlStartDate(ValidateUtils.isValidFormat("dd/MM/yyyy",tdnt4AControlStartDate));
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(8),
									colNameSchedule.get(8) + " không được bỏ trống");
							errorList.add(errorDTO);
						}
	                    
	                    if (validateString(tdntSignProvinceDate)) {
							if (ValidateUtils.isValidFormat("dd/MM/yyyy",tdntSignProvinceDate) == null) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(9),
										colNameSchedule.get(9) + " sai định dạng 'dd/MM/yyyy'");
								errorList.add(errorDTO);
							} else
								obj.setTdntSignProvinceDate(ValidateUtils.isValidFormat("dd/MM/yyyy",tdntSignProvinceDate));
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(9),
									colNameSchedule.get(9) + " không được bỏ trống");
							errorList.add(errorDTO);
						}
	                    
	                    if (validateString(tdntSendTctDate)) {
							if (ValidateUtils.isValidFormat("dd/MM/yyyy",tdntSendTctDate) == null) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(10),
										colNameSchedule.get(10) + " sai định dạng 'dd/MM/yyyy'");
								errorList.add(errorDTO);
							} else
								obj.setTdntSendTctDate(ValidateUtils.isValidFormat("dd/MM/yyyy",tdntSendTctDate));
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(10),
									colNameSchedule.get(10) + " không được bỏ trống");
							errorList.add(errorDTO);
						}
	                    
	                    if (validateString(tdntCompleteExpectedDate)) {
							if (ValidateUtils.isValidFormat("dd/MM/yyyy",tdntCompleteExpectedDate) == null) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(11),
										colNameSchedule.get(11) + " sai định dạng 'dd/MM/yyyy'");
								errorList.add(errorDTO);
							} else
								obj.setTdntCompleteExpectedDate(ValidateUtils.isValidFormat("dd/MM/yyyy",tdntCompleteExpectedDate));
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(11),
									colNameSchedule.get(11) + " không được bỏ trống");
							errorList.add(errorDTO);
						}
	                    
	                    if (validateString(tdntVuongDate)) {
							if (ValidateUtils.isValidFormat("dd/MM/yyyy",tdntCompleteExpectedDate) == null) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(12),
										colNameSchedule.get(12) + " sai định dạng 'dd/MM/yyyy'");
								errorList.add(errorDTO);
							} else {
								if (validateString(tdntVuongReason)) {
									obj.setTdntVuongDate(ValidateUtils.isValidFormat("dd/MM/yyyy",tdntCompleteExpectedDate));
									obj.setTdntVuongReason(tdntVuongReason);
								} else {
									ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(12),
											colNameSchedule.get(12) + " chưa nhập nguyên nhân vướng");
									errorList.add(errorDTO);
								}
							}
						} else {
							if (validateString(tdntVuongReason)) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(12),
										colNameSchedule.get(12) + " chưa nhập ngày vướng");
								errorList.add(errorDTO);
							}
						}
	                    
							/////
		                    if(errorList.size() == 0){
		                        workLst.add(obj);
		                    }
		                }
		            }

		            if(errorList.size() > 0){
		                String filePathError = UEncrypt.encryptFileUploadPath(fileInput);
		                List<ManageDataOutsideOsDTO> emptyArray = Lists.newArrayList();
		                workLst = emptyArray;
		                ManageDataOutsideOsDTO errorContainer = new ManageDataOutsideOsDTO();
		                errorContainer.setErrorList(errorList);
		                errorContainer.setMessageColumn(13);
		                errorContainer.setFilePathError(filePathError);
		                workLst.add(errorContainer);
		            }
		            workbook.close();
		            return workLst;

		        }  catch (Exception e) {
		            ExcelErrorDTO errorDTO = createError(0, "", e.toString());
		            errorList.add(errorDTO);
		            String filePathError = null;
		            try {
		                filePathError = UEncrypt.encryptFileUploadPath(fileInput);
		            } catch(Exception ex) {
		                errorDTO = createError(0, "", ex.toString());
		                errorList.add(errorDTO);
		            }
		            List<ManageDataOutsideOsDTO> emptyArray = Lists.newArrayList();
		            workLst = emptyArray;
		            ManageDataOutsideOsDTO errorContainer = new ManageDataOutsideOsDTO();
		            errorContainer.setErrorList(errorList);
		            errorContainer.setMessageColumn(13);
		            errorContainer.setFilePathError(filePathError);
		            workLst.add(errorContainer);
		            return workLst;
		        }
		    }
	 
	    public void updateListNew(List<ManageDataOutsideOsDTO> list, HttpServletRequest request) {
	    	KttsUserSession objUser = userRoleBusinessImpl.getUserSession(request);
	    	for(ManageDataOutsideOsDTO dto : list) {
	    		dto.setScheduledUserId(objUser.getSysUserId());
	    		manageDataOutsideOsDAO.updateSchedule(dto);
	    	}
	    }
	    
	    //--------------Import Đề nghị quyết toán-------------//
	    HashMap<Integer, String> colNameSettle = new HashMap();
	    {
	    	colNameSettle.put(1,"Mã công trình");
	    	colNameSettle.put(2,"Mã hợp đồng");
	    	colNameSettle.put(3,"Mã trạm");
	    	colNameSettle.put(4,"Loại công trình");
	    	colNameSettle.put(5,"Giá trị QT CĐT(chưa VAT)");
	    	colNameSettle.put(6,"Giá trị QT CĐT (có VAT)");
	    	colNameSettle.put(7,"Thủ tục điện lực");
	    	colNameSettle.put(8,"Nhân công kéo cáp/Nhân công");
	    	colNameSettle.put(9,"CP Vật liệu");
	    	colNameSettle.put(10,"CP HSHC");
	    	colNameSettle.put(11,"CP Vận chuyển kho bãi");
	    	colNameSettle.put(12,"CP khác");
	    	colNameSettle.put(13,"Lương kéo cáp/lương khác");
	    	colNameSettle.put(14,"Lương hàn nối GPON");
	    	colNameSettle.put(15,"VAT");
	    	colNameSettle.put(16,"Tổng");
	    }
	    
	    public List<ManageDataOutsideOsDTO> importSettlementProposal(String fileInput) {
	        List<ManageDataOutsideOsDTO> workLst = Lists.newArrayList();
	        List<ExcelErrorDTO> errorList = new ArrayList<ExcelErrorDTO>();
	        try {
	            File f = new File(fileInput);
	            XSSFWorkbook workbook = new XSSFWorkbook(f);
	            XSSFSheet sheet = workbook.getSheetAt(0);
	            DataFormatter formatter = new DataFormatter();
	            int count = 0;

	            ManageDataOutsideOsDTO consWi = new ManageDataOutsideOsDTO();
	            consWi.setPage(null);
	            consWi.setPageSize(null);
	            
	            List<ManageDataOutsideOsDTO> listDataCons = manageDataOutsideOsDAO.getAutoCompleteConstruction(consWi);
	            HashMap<String, ManageDataOutsideOsDTO> mapCons = new HashMap<>();
	            for(ManageDataOutsideOsDTO dto : listDataCons) {
	            	mapCons.put(dto.getConstructionCode().toUpperCase().trim(), dto);
	            }
	            
	            List<ManageDataOutsideOsDTO> listStatus = manageDataOutsideOsDAO.getDataManageOutsideOs();
	            HashMap<String, ManageDataOutsideOsDTO> mapStatus = new HashMap<>();
	            for(ManageDataOutsideOsDTO dto : listStatus) {
	            	mapStatus.put(dto.getConstructionCode().toUpperCase().trim(), dto);
	            }
	            
	            for (Row row : sheet) {
	                count++;
	                if(count >= 5 && checkIfRowIsEmpty(row)) continue;
	                if (count >= 5) {
	                	String constructionCode = formatter.formatCellValue(row.getCell(1));
	                	String hdContractCode = formatter.formatCellValue(row.getCell(2));
	                	String stationCode = formatter.formatCellValue(row.getCell(3));
	                	String constructionType = formatter.formatCellValue(row.getCell(4));
	                	String dnqtQtCdtNotVat = formatter.formatCellValue(row.getCell(5));
	                	String dnqtQtCdtVat = formatter.formatCellValue(row.getCell(6));
	                	String dnqtElectricalProcedures = formatter.formatCellValue(row.getCell(7));
	                	String dnqtPullCableLabor = formatter.formatCellValue(row.getCell(8));
	                	String dnqtCostMaterial = formatter.formatCellValue(row.getCell(9));
	                	String dnqtCostHshc = formatter.formatCellValue(row.getCell(10));
	                	String dnqtCostTransportWarehouse = formatter.formatCellValue(row.getCell(11));
	                	String dnqtCostOrther = formatter.formatCellValue(row.getCell(12));
	                	String dnqtSalaryCableOrther = formatter.formatCellValue(row.getCell(13));
	                	String dnqtWeldingSalary = formatter.formatCellValue(row.getCell(14));
	                	String dnqtVat = formatter.formatCellValue(row.getCell(15));
	                	String dnqtTotalMoney = formatter.formatCellValue(row.getCell(16));
	                   
	                    ManageDataOutsideOsDTO obj = new ManageDataOutsideOsDTO();
	                    
	                    if (validateString(constructionCode)) {
	                    	ManageDataOutsideOsDTO dto = mapCons.get(constructionCode.toUpperCase().trim());
							if(dto==null) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(1),
										colNameSettle.get(1) + " không tồn tại hoặc chưa được gán với hợp đồng nào");
								errorList.add(errorDTO);
							} else {
								ManageDataOutsideOsDTO dtoMap = mapStatus.get(constructionCode.toUpperCase().trim());
								if(!dtoMap.getStatus().equals("2")) {
									ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(1),
											colNameSettle.get(1) + " không đúng trạng thái lập tiến độ");
									errorList.add(errorDTO);
								} else {
									obj.setConstructionCode(constructionCode);
								}
							}
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(1),
									colNameSettle.get(1) + " không được bỏ trống");
							errorList.add(errorDTO);
						}
	                    
	                    if (validateString(dnqtQtCdtNotVat)) {
							try {
								if (Double.parseDouble(dnqtQtCdtNotVat) < 0) {
									ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(5),
											colNameSettle.get(5) + " không được nhỏ hơn 0");
									errorList.add(errorDTO);
								} else {
									obj.setDnqtQtCdtNotVat(Double.parseDouble(dnqtQtCdtNotVat));
								}
							} catch (Exception e) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(5),
										colNameSettle.get(5) + " sai định dạng số");
								errorList.add(errorDTO);
							}
						} else {
							ManageDataOutsideOsDTO dtoMap = mapStatus.get(constructionCode.toUpperCase().trim());
							if(dtoMap.getConstructionType().equals("2") || dtoMap.getConstructionType().equals("4") || dtoMap.getConstructionType().equals("6")) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(5),
										colNameSettle.get(5) + " không được để trống");
								errorList.add(errorDTO);
							}
						}
	                    
	                    if (validateString(dnqtQtCdtVat)) {
							try {
								if (Double.parseDouble(dnqtQtCdtVat) < 0) {
									ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(6),
											colNameSettle.get(6) + " không được nhỏ hơn 0");
									errorList.add(errorDTO);
								} else
									obj.setDnqtQtCdtVat(Double.parseDouble(dnqtQtCdtVat));
							} catch (Exception e) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(6),
										colNameSettle.get(6) + " sai định dạng số");
								errorList.add(errorDTO);
							}
						} else {
							ManageDataOutsideOsDTO dtoMap = mapStatus.get(constructionCode.toUpperCase().trim());
							if(dtoMap.getConstructionType().equals("2") || dtoMap.getConstructionType().equals("4") || dtoMap.getConstructionType().equals("6")) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(6),
										colNameSettle.get(6) + " không được để trống");
								errorList.add(errorDTO);
							}
						}
	                    
	                    if (validateString(dnqtElectricalProcedures)) {
							try {
								if (Double.parseDouble(dnqtElectricalProcedures) < 0) {
									ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(7),
											colNameSettle.get(7) + " không được nhỏ hơn 0");
									errorList.add(errorDTO);
								} else
									obj.setDnqtElectricalProcedures(Double.parseDouble(dnqtElectricalProcedures));
							} catch (Exception e) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(7),
										colNameSettle.get(7) + " sai định dạng số");
								errorList.add(errorDTO);
							}
						} else {
							ManageDataOutsideOsDTO dtoMap = mapStatus.get(constructionCode.toUpperCase().trim());
							if(dtoMap.getConstructionType().equals("4")) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(7),
										colNameSettle.get(7) + " không được để trống");
								errorList.add(errorDTO);
							}
						}
	                    
	                    if (validateString(dnqtPullCableLabor)) {
							try {
								if (Double.parseDouble(dnqtPullCableLabor) < 0) {
									ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(8),
											colNameSettle.get(8) + " không được nhỏ hơn 0");
									errorList.add(errorDTO);
								} else
									obj.setDnqtPullCableLabor(Double.parseDouble(dnqtPullCableLabor));
							} catch (Exception e) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(8),
										colNameSettle.get(8) + " sai định dạng số");
								errorList.add(errorDTO);
							}
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(8),
									colNameSettle.get(8) + " không được để trống");
							errorList.add(errorDTO);
						}
	                    
	                    if (validateString(dnqtCostMaterial)) {
							try {
								if (Double.parseDouble(dnqtCostMaterial) < 0) {
									ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(9),
											colNameSettle.get(9) + " không được nhỏ hơn 0");
									errorList.add(errorDTO);
								} else
									obj.setDnqtCostMaterial(Double.parseDouble(dnqtCostMaterial));
							} catch (Exception e) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(9),
										colNameSettle.get(9) + " sai định dạng số");
								errorList.add(errorDTO);
							}
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(9),
									colNameSettle.get(9) + " không được để trống");
							errorList.add(errorDTO);
						}
	                    
	                    if (validateString(dnqtCostHshc)) {
							try {
								if (Double.parseDouble(dnqtCostHshc) < 0) {
									ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(10),
											colNameSettle.get(10) + " không được nhỏ hơn 0");
									errorList.add(errorDTO);
								} else
									obj.setDnqtCostHshc(Double.parseDouble(dnqtCostHshc));
							} catch (Exception e) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(10),
										colNameSettle.get(10) + " sai định dạng số");
								errorList.add(errorDTO);
							}
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(10),
									colNameSettle.get(10) + " không được để trống");
							errorList.add(errorDTO);
						}
	                    
	                    if (validateString(dnqtCostTransportWarehouse)) {
							try {
								if (Double.parseDouble(dnqtCostTransportWarehouse) < 0) {
									ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(11),
											colNameSettle.get(11) + " không được nhỏ hơn 0");
									errorList.add(errorDTO);
								} else
									obj.setDnqtCostTransportWarehouse(Double.parseDouble(dnqtCostTransportWarehouse));
							} catch (Exception e) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(11),
										colNameSettle.get(11) + " sai định dạng số");
								errorList.add(errorDTO);
							}
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(11),
									colNameSettle.get(11) + " không được để trống");
							errorList.add(errorDTO);
						}
	                    
	                    if (validateString(dnqtCostOrther)) {
							try {
								if (Double.parseDouble(dnqtCostOrther) < 0) {
									ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(12),
											colNameSettle.get(12) + " không được nhỏ hơn 0");
									errorList.add(errorDTO);
								} else
									obj.setDnqtCostOrther(Double.parseDouble(dnqtCostOrther));
							} catch (Exception e) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(12),
										colNameSettle.get(12) + " sai định dạng số");
								errorList.add(errorDTO);
							}
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(12),
									colNameSettle.get(12) + " không được để trống");
							errorList.add(errorDTO);
						}
	                    
	                    if (validateString(dnqtSalaryCableOrther)) {
							try {
								if (Double.parseDouble(dnqtSalaryCableOrther) < 0) {
									ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(13),
											colNameSettle.get(13) + " không được nhỏ hơn 0");
									errorList.add(errorDTO);
								} else
									obj.setDnqtSalaryCableOrther(Double.parseDouble(dnqtSalaryCableOrther));
							} catch (Exception e) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(13),
										colNameSettle.get(13) + " sai định dạng số");
								errorList.add(errorDTO);
							}
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(13),
									colNameSettle.get(13) + " không được để trống");
							errorList.add(errorDTO);
						}
	                    
	                    if (validateString(dnqtWeldingSalary)) {
							try {
								if (Double.parseDouble(dnqtWeldingSalary) < 0) {
									ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(14),
											colNameSettle.get(14) + " không được nhỏ hơn 0");
									errorList.add(errorDTO);
								} else
									obj.setDnqtWeldingSalary(Double.parseDouble(dnqtWeldingSalary));
							} catch (Exception e) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(14),
										colNameSettle.get(14) + " sai định dạng số");
								errorList.add(errorDTO);
							}
						} else {
							ManageDataOutsideOsDTO dtoMap = mapStatus.get(constructionCode.toUpperCase().trim());
							if(dtoMap.getConstructionType().equals("4")) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(14),
										colNameSettle.get(14) + " không được để trống");
								errorList.add(errorDTO);
							}
						}
	                    
	                    if (validateString(dnqtVat)) {
							try {
								if (Double.parseDouble(dnqtVat) < 0) {
									ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(15),
											colNameSettle.get(15) + " không được nhỏ hơn 0");
									errorList.add(errorDTO);
								} else
									obj.setDnqtVat(Double.parseDouble(dnqtVat));
							} catch (Exception e) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(15),
										colNameSettle.get(15) + " sai định dạng số");
								errorList.add(errorDTO);
							}
						}
	                    
	                    if (validateString(dnqtTotalMoney)) {
							try {
								if (Double.parseDouble(dnqtTotalMoney) < 0) {
									ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(16),
											colNameSettle.get(16) + " không được nhỏ hơn 0");
									errorList.add(errorDTO);
								} else
									obj.setDnqtTotalMoney(Double.parseDouble(dnqtTotalMoney));
							} catch (Exception e) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(16),
										colNameSettle.get(16) + " sai định dạng số");
								errorList.add(errorDTO);
							}
						}
	                    
	                    
							/////
		                    if(errorList.size() == 0){
		                        workLst.add(obj);
		                    }
		                }
		            }

		            if(errorList.size() > 0){
		                String filePathError = UEncrypt.encryptFileUploadPath(fileInput);
		                List<ManageDataOutsideOsDTO> emptyArray = Lists.newArrayList();
		                workLst = emptyArray;
		                ManageDataOutsideOsDTO errorContainer = new ManageDataOutsideOsDTO();
		                errorContainer.setErrorList(errorList);
		                errorContainer.setMessageColumn(39);
		                errorContainer.setFilePathError(filePathError);
		                workLst.add(errorContainer);
		            }
		            workbook.close();
		            return workLst;

		        }  catch (Exception e) {
		            ExcelErrorDTO errorDTO = createError(0, "", e.toString());
		            errorList.add(errorDTO);
		            String filePathError = null;
		            try {
		                filePathError = UEncrypt.encryptFileUploadPath(fileInput);
		            } catch(Exception ex) {
		                errorDTO = createError(0, "", ex.toString());
		                errorList.add(errorDTO);
		            }
		            List<ManageDataOutsideOsDTO> emptyArray = Lists.newArrayList();
		            workLst = emptyArray;
		            ManageDataOutsideOsDTO errorContainer = new ManageDataOutsideOsDTO();
		            errorContainer.setErrorList(errorList);
		            errorContainer.setMessageColumn(39);
		            errorContainer.setFilePathError(filePathError);
		            workLst.add(errorContainer);
		            return workLst;
		        }
		    }
	    
	    public void updateListSettle(List<ManageDataOutsideOsDTO> list, HttpServletRequest request) {
	    	KttsUserSession objUser = userRoleBusinessImpl.getUserSession(request);
	    	for(ManageDataOutsideOsDTO obj : list) {
	    		obj.setSuggestedUserId(objUser.getSysUserId());
	    		manageDataOutsideOsDAO.updateListSettle(obj);
	    	}
	    }
	    
	    //---------------Import Quyết toán nhân công, vật tư A cấp-----------//
	    HashMap<Integer, String> colNameLabor = new HashMap();
	    {
	    	colNameLabor.put(1,"Mã công trình");
	    	colNameLabor.put(2,"Mã hợp đồng");
	    	colNameLabor.put(3,"Mã trạm");
	    	colNameLabor.put(4,"Loại công trình");
	    	colNameLabor.put(5,"Ngày chuyển hồ sơ lên phòng hạ tầng");
	    	colNameLabor.put(6,"Ngày chuyển hồ sơ lên phòng tài chính");
	    	colNameLabor.put(7,"Ngày hạch toán nhân công và vật tư A cấp");
	    	colNameLabor.put(8,"Ngày nhận tiền");
	    	colNameLabor.put(9,"Vướng mắc");
	    }
	    
	    public List<ManageDataOutsideOsDTO> importProposalLabor(String fileInput) {
	        List<ManageDataOutsideOsDTO> workLst = Lists.newArrayList();
	        List<ExcelErrorDTO> errorList = new ArrayList<ExcelErrorDTO>();
	        try {
	            File f = new File(fileInput);
	            XSSFWorkbook workbook = new XSSFWorkbook(f);
	            XSSFSheet sheet = workbook.getSheetAt(0);
	            DataFormatter formatter = new DataFormatter();
	            int count = 0;

	            ManageDataOutsideOsDTO consWi = new ManageDataOutsideOsDTO();
	            consWi.setPage(null);
	            consWi.setPageSize(null);
	            
	            List<ManageDataOutsideOsDTO> listDataCons = manageDataOutsideOsDAO.getAutoCompleteConstruction(consWi);
	            HashMap<String, ManageDataOutsideOsDTO> mapCons = new HashMap<>();
	            for(ManageDataOutsideOsDTO dto : listDataCons) {
	            	mapCons.put(dto.getConstructionCode().toUpperCase().trim(), dto);
	            }
	            
	            List<ManageDataOutsideOsDTO> listStatus = manageDataOutsideOsDAO.getDataManageOutsideOs();
	            HashMap<String, String> mapStatus = new HashMap<>();
	            for(ManageDataOutsideOsDTO dto : listStatus) {
	            	mapStatus.put(dto.getConstructionCode().toUpperCase().trim(), dto.getStatus());
	            }
	            
	            for (Row row : sheet) {
	                count++;
	                if(count >= 5 && checkIfRowIsEmpty(row)) continue;
	                if (count >= 5) {
	                	String constructionCode = formatter.formatCellValue(row.getCell(1));
	                	String hdContractCode = formatter.formatCellValue(row.getCell(2));
	                	String stationCode = formatter.formatCellValue(row.getCell(3));
	                	String constructionType = formatter.formatCellValue(row.getCell(4));
	                	String qtncPhtDate = formatter.formatCellValue(row.getCell(5));
	                	String qtncPtcDate = formatter.formatCellValue(row.getCell(6));
	                	String qtncVtaAccountDate = formatter.formatCellValue(row.getCell(7));
	                	String qtncTakeMoneyDate = formatter.formatCellValue(row.getCell(8));
	                	String qtncVuong = formatter.formatCellValue(row.getCell(9));
	                	
	                    ManageDataOutsideOsDTO obj = new ManageDataOutsideOsDTO();
	                    
	                    if (validateString(constructionCode)) {
	                    	ManageDataOutsideOsDTO dto = mapCons.get(constructionCode.toUpperCase().trim());
							if(dto==null) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(1),
										colNameLabor.get(1) + " không tồn tại hoặc chưa được gán với hợp đồng nào");
								errorList.add(errorDTO);
							} else {
								String checkStatus = mapStatus.get(constructionCode.toUpperCase().trim());
								if(!checkStatus.equals("6") && !checkStatus.equals("7")) {
									ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(1),
											colNameLabor.get(1) + " không đúng trạng thái lập tiến độ");
									errorList.add(errorDTO);
								} else {
									obj.setConstructionCode(constructionCode);
								}
							}
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(1),
									colNameLabor.get(1) + " không được bỏ trống");
							errorList.add(errorDTO);
						}
	                    
	                    if (validateString(qtncPhtDate)) {
							if (ValidateUtils.isValidFormat("dd/MM/yyyy",qtncPhtDate) == null) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(5),
										colNameLabor.get(5) + " sai định dạng 'dd/MM/yyyy'");
								errorList.add(errorDTO);
							} else
								obj.setQtncPhtDate(ValidateUtils.isValidFormat("dd/MM/yyyy",qtncPhtDate));
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(5),
									colNameLabor.get(5) + " không được bỏ trống");
							errorList.add(errorDTO);
						}
	                    
	                    if (validateString(qtncPtcDate)) {
							if (ValidateUtils.isValidFormat("dd/MM/yyyy",qtncPtcDate) == null) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(6),
										colNameLabor.get(6) + " sai định dạng 'dd/MM/yyyy'");
								errorList.add(errorDTO);
							} else
								obj.setQtncPtcDate(ValidateUtils.isValidFormat("dd/MM/yyyy",qtncPtcDate));
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(6),
									colNameLabor.get(6) + " không được bỏ trống");
							errorList.add(errorDTO);
						}
	                    
	                    if (validateString(qtncVtaAccountDate)) {
							if (ValidateUtils.isValidFormat("dd/MM/yyyy",qtncVtaAccountDate) == null) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(7),
										colNameLabor.get(7) + " sai định dạng 'dd/MM/yyyy'");
								errorList.add(errorDTO);
							} else
								obj.setQtncVtaAccountDate(ValidateUtils.isValidFormat("dd/MM/yyyy",qtncVtaAccountDate));
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(7),
									colNameLabor.get(7) + " không được bỏ trống");
							errorList.add(errorDTO);
						}
	                    
	                    if (validateString(qtncTakeMoneyDate)) {
							if (ValidateUtils.isValidFormat("dd/MM/yyyy",qtncTakeMoneyDate) == null) {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(8),
										colNameLabor.get(8) + " sai định dạng 'dd/MM/yyyy'");
								errorList.add(errorDTO);
							} else
								obj.setQtncTakeMoneyDate(ValidateUtils.isValidFormat("dd/MM/yyyy",qtncTakeMoneyDate));
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAliasNew.get(8),
									colNameLabor.get(8) + " không được bỏ trống");
							errorList.add(errorDTO);
						}
	                    
	                    obj.setQtncVuong(qtncVuong);
	                    
							/////
		                    if(errorList.size() == 0){
		                        workLst.add(obj);
		                    }
		                }
		            }

		            if(errorList.size() > 0){
		                String filePathError = UEncrypt.encryptFileUploadPath(fileInput);
		                List<ManageDataOutsideOsDTO> emptyArray = Lists.newArrayList();
		                workLst = emptyArray;
		                ManageDataOutsideOsDTO errorContainer = new ManageDataOutsideOsDTO();
		                errorContainer.setErrorList(errorList);
		                errorContainer.setMessageColumn(39);
		                errorContainer.setFilePathError(filePathError);
		                workLst.add(errorContainer);
		            }
		            workbook.close();
		            return workLst;

		        }  catch (Exception e) {
		            ExcelErrorDTO errorDTO = createError(0, "", e.toString());
		            errorList.add(errorDTO);
		            String filePathError = null;
		            try {
		                filePathError = UEncrypt.encryptFileUploadPath(fileInput);
		            } catch(Exception ex) {
		                errorDTO = createError(0, "", ex.toString());
		                errorList.add(errorDTO);
		            }
		            List<ManageDataOutsideOsDTO> emptyArray = Lists.newArrayList();
		            workLst = emptyArray;
		            ManageDataOutsideOsDTO errorContainer = new ManageDataOutsideOsDTO();
		            errorContainer.setErrorList(errorList);
		            errorContainer.setMessageColumn(39);
		            errorContainer.setFilePathError(filePathError);
		            workLst.add(errorContainer);
		            return workLst;
		        }
		    }
	    
	    public void updateListLabor(List<ManageDataOutsideOsDTO> list, HttpServletRequest request) {
	    	KttsUserSession objUser = userRoleBusinessImpl.getUserSession(request);
	    	for(ManageDataOutsideOsDTO obj : list) {
	    		obj.setLaborUserId(objUser.getSysUserId());
	    		manageDataOutsideOsDAO.updateListLabor(obj);
	    	}
	    }
	//Huy-end
}
