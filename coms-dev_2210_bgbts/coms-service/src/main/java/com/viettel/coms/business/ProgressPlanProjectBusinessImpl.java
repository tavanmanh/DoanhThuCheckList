package com.viettel.coms.business;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.viettel.cat.dto.CatProvinceDTO;
import com.viettel.coms.bo.ProgressPlanProjectBO;
import com.viettel.coms.bo.SendEmailBO;
import com.viettel.coms.bo.SendSmsEmailBO;
import com.viettel.coms.bo.UtilAttachDocumentBO;
import com.viettel.coms.dao.ProgressPlanProjectDAO;
import com.viettel.coms.dao.SendEmailDAO;
import com.viettel.coms.dao.SendSmsEmailDAO;
import com.viettel.coms.dao.UtilAttachDocumentDAO;
import com.viettel.coms.dto.ExcelErrorDTO;
import com.viettel.coms.dto.ProgressPlanProjectDTO;
import com.viettel.coms.dto.UtilAttachDocumentDTO;
import com.viettel.coms.utils.ExcelUtils;
import com.viettel.ktts.vps.VpsPermissionChecker;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.service.base.utils.StringUtils;
import com.viettel.utils.Constant;
import com.viettel.utils.ConvertData;
import com.viettel.wms.utils.ValidateUtils;

@Service("progressPlanProjectBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ProgressPlanProjectBusinessImpl
		extends BaseFWBusinessImpl<ProgressPlanProjectDAO, ProgressPlanProjectDTO, ProgressPlanProjectBO>
		implements ProgressPlanProjectBusiness {

	@Autowired
	ProgressPlanProjectDAO progressPlanProjectDAO;
	
	@Autowired
    private SendEmailDAO sendEmailDAO;
	
	@Autowired
    private SendSmsEmailDAO sendSmsEmailDAO;
	
	@Autowired
    private UtilAttachDocumentDAO utilAttachDocumentDAO;
	
	public ProgressPlanProjectBusinessImpl() {
        tModel = new ProgressPlanProjectBO();
        tDAO = progressPlanProjectDAO;
    }
	
    @Value("${folder_upload2}")
    private String folder2Upload;
    @Value("${default_sub_folder_upload}")
    private String defaultSubFolderUpload;
    
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
		colAlias.put(21, "V");
		colAlias.put(22, "W");
		colAlias.put(23, "X");
		colAlias.put(24, "Y");
		colAlias.put(25, "Z");
		colAlias.put(26, "AA");
		colAlias.put(27, "AB");
		colAlias.put(28, "AC");
		colAlias.put(29, "AD");
		colAlias.put(30, "AE");
	}
    
    public DataListDTO doSearch(ProgressPlanProjectDTO obj, HttpServletRequest request) {
//    	List<ProgressPlanProjectDTO> ls = new ArrayList<ProgressPlanProjectDTO>();
//    	String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
//				Constant.AdResourceKey.DATA, request);
//		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
//		if (groupIdList != null && !groupIdList.isEmpty()) {
//			ls = progressPlanProjectDAO.doSearch(obj, groupIdList);
//		}
    	
    	List<ProgressPlanProjectDTO> ls = progressPlanProjectDAO.doSearch(obj);
    	DataListDTO data = new DataListDTO();
    	data.setData(ls);
    	data.setStart(1);
    	data.setSize(obj.getTotalRecord());
    	data.setTotal(obj.getTotalRecord());
    	return data;
    }
    
    public DataListDTO doSearchProvinceInPopup(CatProvinceDTO obj, HttpServletRequest request) {
    	List<CatProvinceDTO> ls = new ArrayList<CatProvinceDTO>();
//    	String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
//				Constant.AdResourceKey.DATA, request);
//		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
//		if (groupIdList != null && !groupIdList.isEmpty()) {
			ls = progressPlanProjectDAO.doSearchProvinceByRolePopup(obj);
//		}
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }
    
    public DataListDTO getCntContractInHtct(ProgressPlanProjectDTO obj) {
    	List<ProgressPlanProjectDTO> ls = progressPlanProjectDAO.getCntContractInHtct(obj);
        DataListDTO data = new DataListDTO();
        data.setData(ls);
        data.setTotal(obj.getTotalRecord());
        data.setSize(obj.getPageSize());
        data.setStart(1);
        return data;
    }
    
    public Long saveProject(ProgressPlanProjectDTO obj, HttpServletRequest request) throws Exception{
    	KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
    	SimpleDateFormat dateFor = new SimpleDateFormat("dd/MM/yyyy");
    	String dateDeadline = dateFor.format(obj.getDeadlineDateComplete());
    	String address = "";
    	if(obj.getAddress()!=null) {
    		address = obj.getAddress();
    	}
    	List<ProgressPlanProjectDTO> ls = progressPlanProjectDAO.checkValidateProjectCdt(obj.getProjectName() +"+"+ address +"+"+ obj.getInvestorName(), null);
    	if(ls.size()>0) {
    		throw new IllegalArgumentException("Cặp dự án, địa chỉ, tên chủ đầu tư đã tồn tại trong cơ sở dữ liệu !");
    	}
    	obj.setCreateDate(new Date());
    	obj.setCreateUserId(user.getVpsUserInfo().getSysUserId());
    	if(obj.getLevelDeployment()!=null) {
    		if(obj.getLevelDeployment().equals("2")) {
        		if((obj.getDateContract()==null && obj.getContractCode()==null)) {
        			obj.setStatus("2");
        		} else if(obj.getDateContract()!=null && obj.getContractCode()!=null) {
        			obj.setStatus("3");
        		} else {
        			obj.setStatus("2");
        		}
        	} else {
        		obj.setStatus("3");
        	}
    	} else {
    		obj.setStatus("1");
    	}
    	
    	if(obj.getStatus()!=null && !obj.getStatus().equals("3")) {
    		SendEmailBO email = new SendEmailBO();
    		email.setSubject("Quản lý dự án tiếp xúc IBS");
    		email.setStatus(0l);
    		email.setContent("Sắp đến ngày hạn hoàn thành tiếp xúc "+ dateDeadline + " của dự án: "+ obj.getProjectName());
    		email.setReceiveEmail(obj.getEmailEmploy());
    		email.setCreatedDate(new Date());
    		email.setCreatedUserId(user.getVpsUserInfo().getSysUserId());
    		sendEmailDAO.saveObject(email);
    		
    		SendSmsEmailBO sms = new SendSmsEmailBO();
    		sms.setSubject("Quản lý dự án tiếp xúc IBS");
    		sms.setStatus("0");
    		sms.setContent("Sắp đến ngày hạn hoàn thành tiếp xúc "+ dateDeadline + " của dự án: "+ obj.getProjectName());
    		sms.setReceiveEmail(obj.getEmailEmploy());
    		sms.setReceivePhoneNumber(obj.getPhoneNumberEmploy());
    		sms.setCreatedDate(new Date());
    		sms.setCreatedUserId(user.getVpsUserInfo().getSysUserId());
    		sendSmsEmailDAO.saveObject(sms);
    		
    		ProgressPlanProjectDTO gdt = progressPlanProjectDAO.getInfoUserBySysUser(user.getVpsUserInfo().getSysGroupId());
    		
    		if(gdt==null) {
    			throw new IllegalArgumentException("Không tìm thấy tỉnh trưởng từ người dùng đăng nhập !");
    		}
    		
    		SendEmailBO emailGdt = new SendEmailBO();
    		emailGdt.setSubject("Quản lý dự án tiếp xúc IBS");
    		emailGdt.setStatus(0l);
    		emailGdt.setContent("Sắp đến ngày hạn hoàn thành tiếp xúc "+ dateDeadline + " của dự án: "+ obj.getProjectName());
    		emailGdt.setReceiveEmail(gdt.getEmailEmploy());
    		emailGdt.setCreatedDate(new Date());
    		emailGdt.setCreatedUserId(user.getVpsUserInfo().getSysUserId());
    		sendEmailDAO.saveObject(emailGdt);
    		
    		SendSmsEmailBO smsGdt = new SendSmsEmailBO();
    		smsGdt.setSubject("Quản lý dự án tiếp xúc IBS");
    		smsGdt.setStatus("0");
    		smsGdt.setContent("Sắp đến ngày hạn hoàn thành tiếp xúc "+ dateDeadline + " của dự án: "+ obj.getProjectName());
    		smsGdt.setReceiveEmail(gdt.getEmailEmploy());
    		smsGdt.setReceivePhoneNumber(gdt.getPhoneNumberEmploy());
    		smsGdt.setCreatedDate(new Date());
    		smsGdt.setCreatedUserId(user.getVpsUserInfo().getSysUserId());
    		sendSmsEmailDAO.saveObject(smsGdt);
    	}
    	
    	Long id = progressPlanProjectDAO.saveObject(obj.toModel());
    	
    	if(obj.getListFile()!=null && obj.getListFile().size()>0) {
    		for(UtilAttachDocumentDTO dto : obj.getListFile()) {
    			dto.setObjectId(id);
    			dto.setType("IBS");
    			dto.setDescription("File đính kèm tiếp xúc IBS");
    			dto.setStatus("1");
    			utilAttachDocumentDAO.saveObject(dto.toModel());
    		}
    	}
    	
    	return id;
    }
    
    public Long updateProject(ProgressPlanProjectDTO obj, HttpServletRequest request) throws Exception{
    	KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
    	SimpleDateFormat dateFor = new SimpleDateFormat("dd/MM/yyyy");
    	String dateDeadline = dateFor.format(obj.getDeadlineDateComplete());
    	
    	ProgressPlanProjectDTO project = progressPlanProjectDAO.getCodeById(obj.getProgressPlanProjectId());
    	if(!project.getProjectName().equals((obj.getProjectName() +"+"+ (obj.getAddress()!=null ? obj.getAddress() : "") +"+"+ obj.getInvestorName()).toUpperCase())) {
    		String address = "";
    		if(obj.getAddress()!=null) {
        		address = obj.getAddress();
        	}
        	List<ProgressPlanProjectDTO> ls = progressPlanProjectDAO.checkValidateProjectCdt(obj.getProjectName() +"+"+ address +"+"+ obj.getInvestorName(), null);
        	if(ls.size()>0) {
        		throw new IllegalArgumentException("Cặp dự án, địa chỉ, tên chủ đầu tư đã tồn tại trong cơ sở dữ liệu !");
        	}
    	}
    	
    	obj.setUpdateDate(new Date());
    	obj.setUpdateUserId(user.getVpsUserInfo().getSysUserId());
    	if(obj.getLevelDeployment()!=null) {
    		if(obj.getLevelDeployment().equals("2")) {
        		if((obj.getDateContract()==null && obj.getContractCode()==null)) {
        			obj.setStatus("2");
        		} else if(obj.getDateContract()!=null && obj.getContractCode()!=null) {
        			obj.setStatus("3");
        		} else {
        			obj.setStatus("2");
        		}
        	} else {
        		obj.setStatus("3");
        	}
    	} else {
    		obj.setStatus("1");
    	}
    	
    	if(obj.getStatus()!=null && !obj.getStatus().equals("3") && !project.getEmailEmploy().equals(obj.getEmailEmploy())) {
    		SendEmailBO email = new SendEmailBO();
    		email.setSubject("Quản lý dự án tiếp xúc IBS");
    		email.setStatus(0l);
    		email.setContent("Sắp đến ngày hạn hoàn thành tiếp xúc "+ dateDeadline + " của dự án: "+ obj.getProjectName());
    		email.setReceiveEmail(obj.getEmailEmploy());
    		email.setCreatedDate(new Date());
    		email.setCreatedUserId(user.getVpsUserInfo().getSysUserId());
    		sendEmailDAO.saveObject(email);
    		
    		SendSmsEmailBO sms = new SendSmsEmailBO();
    		sms.setSubject("Quản lý dự án tiếp xúc IBS");
    		sms.setStatus("0");
    		sms.setContent("Sắp đến ngày hạn hoàn thành tiếp xúc "+ dateDeadline + " của dự án: "+ obj.getProjectName());
    		sms.setReceiveEmail(obj.getEmailEmploy());
    		sms.setReceivePhoneNumber(obj.getPhoneNumberEmploy());
    		sms.setCreatedDate(new Date());
    		sms.setCreatedUserId(user.getVpsUserInfo().getSysUserId());
    		sendSmsEmailDAO.saveObject(sms);
    		
    		ProgressPlanProjectDTO gdt = progressPlanProjectDAO.getInfoUserBySysUser(user.getVpsUserInfo().getSysGroupId());
    		
    		if(gdt==null) {
    			throw new IllegalArgumentException("Không tìm thấy tỉnh trưởng từ người dùng đăng nhập !");
    		}
    		
    		SendEmailBO emailGdt = new SendEmailBO();
    		emailGdt.setSubject("Quản lý dự án tiếp xúc IBS");
    		emailGdt.setStatus(0l);
    		emailGdt.setContent("Sắp đến ngày hạn hoàn thành tiếp xúc "+ dateDeadline + " của dự án: "+ obj.getProjectName());
    		emailGdt.setReceiveEmail(gdt.getEmailEmploy());
    		emailGdt.setCreatedDate(new Date());
    		emailGdt.setCreatedUserId(user.getVpsUserInfo().getSysUserId());
    		sendEmailDAO.saveObject(emailGdt);
    		
    		SendSmsEmailBO smsGdt = new SendSmsEmailBO();
    		smsGdt.setSubject("Quản lý dự án tiếp xúc IBS");
    		smsGdt.setStatus("0");
    		smsGdt.setContent("Sắp đến ngày hạn hoàn thành tiếp xúc "+ dateDeadline + " của dự án: "+ obj.getProjectName());
    		smsGdt.setReceiveEmail(gdt.getEmailEmploy());
    		smsGdt.setReceivePhoneNumber(gdt.getPhoneNumberEmploy());
    		smsGdt.setCreatedDate(new Date());
    		smsGdt.setCreatedUserId(user.getVpsUserInfo().getSysUserId());
    		sendSmsEmailDAO.saveObject(smsGdt);
    	}
    	
    	progressPlanProjectDAO.deleteFile(obj.getProgressPlanProjectId(), "IBS");
    	
    	if(obj.getListFile()!=null && obj.getListFile().size()>0) {
    		for(UtilAttachDocumentDTO dto : obj.getListFile()) {
    			dto.setObjectId(obj.getProgressPlanProjectId());
    			dto.setType("IBS");
    			dto.setDescription("File đính kèm tiếp xúc IBS");
    			dto.setStatus("1");
    			utilAttachDocumentDAO.saveObject(dto.toModel());
    		}
    	}
    	
    	return progressPlanProjectDAO.updateObject(obj.toModel());
    }
    
    private ExcelErrorDTO createError(int row, String column, String detail) {
		ExcelErrorDTO err = new ExcelErrorDTO();
		err.setColumnError(column);
		err.setLineError(String.valueOf(row));
		err.setDetailError(detail);
		return err;
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
    
    private boolean validateEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
     }
    
    public List<ProgressPlanProjectDTO> importProject(String fileInput, String filePath, HttpServletRequest request) throws Exception {
    	KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession"); 
        List<ProgressPlanProjectDTO> workLst = new ArrayList<ProgressPlanProjectDTO>();
        File f = new File(fileInput);
        ZipSecureFile.setMinInflateRatio(-1.0d);//hoangnh add
        XSSFWorkbook workbook = new XSSFWorkbook(f);
        XSSFSheet sheet = workbook.getSheetAt(0);
        DataFormatter formatter = new DataFormatter();
        DecimalFormat df = new DecimalFormat("#.###");
        DecimalFormat longf = new DecimalFormat("#");
        boolean isExistError = false;
        
        String regex = "(0/91)?[7-9][0-9]{9}";
        
        List<ExcelErrorDTO> errorList = new ArrayList<ExcelErrorDTO>();
        
        int counts = 0;
        List<String> listProvince  = new ArrayList<>();
        List<String> lstProjectCdt = new ArrayList<>();
        List<String> lstContractInHtct = new ArrayList<>();
        for (Row rows : sheet) {
        	counts++;
            if (counts > 7 && !ValidateUtils.checkIfRowIsEmpty(rows)) {
            	listProvince.add(formatter.formatCellValue(rows.getCell(2)).trim().toUpperCase());
				lstProjectCdt.add(formatter.formatCellValue(rows.getCell(5)).trim().toUpperCase() + "+"
						+ formatter.formatCellValue(rows.getCell(6)).trim().toUpperCase() + "+"
						+ formatter.formatCellValue(rows.getCell(7)).trim().toUpperCase());
				lstContractInHtct.add(formatter.formatCellValue(rows.getCell(25)).trim().toUpperCase());
            }
        }
        
        if(listProvince.size()==0) {
        	throw new IllegalArgumentException("File import không có nội dung");
        }
        
        HashMap<String, ProgressPlanProjectDTO> mapContract = new HashMap<>();
        ProgressPlanProjectDTO planPro = new ProgressPlanProjectDTO();
        planPro.setLstContract(lstContractInHtct);
        planPro.setPage(null);
        planPro.setPageSize(null);
        List<ProgressPlanProjectDTO> lstContract = progressPlanProjectDAO.getCntContractInHtct(planPro);
        for(ProgressPlanProjectDTO dto : lstContract) {
        	mapContract.put(dto.getContractCode().toUpperCase(), dto);
        }
        
        HashMap<String, String> mapProjectCdt = new HashMap<>();
        List<ProgressPlanProjectDTO> ls = progressPlanProjectDAO.checkValidateProjectCdt(null, lstProjectCdt);
        for(ProgressPlanProjectDTO dto : ls) {
        	String keyMap = dto.getProjectName() +"+"+ (dto.getAddress()!=null ? dto.getAddress() : "") +"+"+ dto.getInvestorName();
        	mapProjectCdt.put(keyMap.toUpperCase(), dto.getProjectName());
        }
        
        List<ProgressPlanProjectDTO> lstProvince = progressPlanProjectDAO.getProvinceByListId(listProvince);
        HashMap<String, ProgressPlanProjectDTO> mapProvince = new HashMap<>();
        HashMap<String, ProgressPlanProjectDTO> mapArea = new HashMap<>();
        for(ProgressPlanProjectDTO dto : lstProvince) {
        	mapProvince.put(dto.getProvinceCode().toUpperCase(), dto);
        	mapArea.put(dto.getAreaCode().toUpperCase(), dto);
        }
        
        HashMap<String, String> checkDuplicateInFile = new HashMap<>();
        
        XSSFCellStyle style = ExcelUtils.styleText(sheet);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        
        int count = 0;
        for (Row row : sheet) {
            count++;
            if (count > 7 && !ValidateUtils.checkIfRowIsEmpty(row)) {
                StringBuilder errorMesg = new StringBuilder();
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String areaCode = "";
                String provinceCode = "";
                ProgressPlanProjectDTO newObj = new ProgressPlanProjectDTO();
                List<ExcelErrorDTO> listError = new ArrayList<ExcelErrorDTO>();
                for (int i = 0; i < 27; i++) {
                    Cell cell = row.getCell(i);
                    if (cell != null) {
                        if (cell.getColumnIndex() == 1) {
                            try {
                            	areaCode = formatter.formatCellValue(row.getCell(1)).trim();

                                ProgressPlanProjectDTO obj = mapArea.get(areaCode.trim().toUpperCase());

                                if (obj == null) {
                                    isExistError = true;
                                    ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(1),
    										"Mã khu vực không tồn tại !");
    								errorList.add(errorDTO);
    								listError.add(errorDTO);
                                } else {
                                	newObj.setAreaCode(obj.getAreaCode());
                                }
                                
                            } catch (Exception e) {
                                isExistError = true;
                                ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(1),
										"Mã khu vực không hợp lệ!");
								errorList.add(errorDTO);
								listError.add(errorDTO);
                            }
                        }
                        
                        else if (cell.getColumnIndex() == 2) {
                            try {
                            	provinceCode = formatter.formatCellValue(row.getCell(2)).trim();
                                ProgressPlanProjectDTO obj = mapProvince.get(provinceCode.trim().toUpperCase());
                                if (obj == null) {
                                    isExistError = true;
                                    ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(2),
    										"Tỉnh không tồn tại !");
    								errorList.add(errorDTO);
    								listError.add(errorDTO);
                                } else {
                                	if(!obj.getAreaCode().equals(newObj.getAreaCode())) {
                                		isExistError = true;
                                        ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(2),
        										"Tỉnh không thuộc khu vực "+ newObj.getAreaCode() +" !");
        								errorList.add(errorDTO);
        								listError.add(errorDTO);
                                	} else {
                                		newObj.setProvinceId(obj.getProvinceId());
                                		newObj.setProvinceCode(obj.getProvinceCode());
                                	}
                                }
                            } catch (Exception e) {
                                isExistError = true;
                                ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(2),
										"Mã tỉnh không hợp lệ!");
								errorList.add(errorDTO);
								listError.add(errorDTO);
                            }
                        } 
                        
                        else if (cell.getColumnIndex() == 7) {
                        	String keyImport = formatter.formatCellValue(row.getCell(5)).trim() + "+"
    								+ (formatter.formatCellValue(row.getCell(6))!=null?formatter.formatCellValue(row.getCell(6)).trim() : "") + "+"
    								+ formatter.formatCellValue(row.getCell(7)).trim();
                        	String checkString = mapProjectCdt.get(keyImport.toUpperCase());
    						if(!StringUtils.isNullOrEmpty(checkString)) {
    							isExistError = true;
                                ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(7),
                            			" Cặp dự án, địa chỉ, tên chủ đầu tư đã tồn tại trong cơ sở dữ liệu !");
    							errorList.add(errorDTO);
								listError.add(errorDTO);
    						}
    						if(checkDuplicateInFile.size()==0) {
    							checkDuplicateInFile.put(keyImport.toUpperCase(), keyImport);
    						} else {
    							if(checkDuplicateInFile.get(keyImport.toUpperCase())!=null) {
    								isExistError = true;
                                    ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(7),
                                			" Cặp dự án, địa chỉ, tên chủ đầu tư không được trùng trong cùng file import !");
        							errorList.add(errorDTO);
    								listError.add(errorDTO);
    							} else {
    								checkDuplicateInFile.put(keyImport.toUpperCase(), keyImport);
    							}
    						}
                        }
                        
                        else if (cell.getColumnIndex() == 9) {
                            try {
                            	Long numberHouse = Long.parseLong(formatter.formatCellValue(row.getCell(9)).trim());
                            	if(numberHouse<0) {
                            		isExistError = true;
                                    ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(9),
    										"Số căn hộ phải nhập số lớn hơn 0!");
    								errorList.add(errorDTO);
    								listError.add(errorDTO);
                            	} else {
                            		newObj.setNumberHouse(numberHouse);
                            	}
                            } catch (Exception e) {
                                isExistError = true;
                                ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(9),
										"Số căn hộ phải nhập dạng số!");
								errorList.add(errorDTO);
								listError.add(errorDTO);
                            }
                        } 
                        
                        else if (cell.getColumnIndex() == 10) {
                            try {
                            	Long numberBlock = Long.parseLong(formatter.formatCellValue(row.getCell(10)).trim());
                            	if(numberBlock<0) {
                            		isExistError = true;
                                    ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(9),
    										"Số Block phải nhập số lớn hơn 0!");
    								errorList.add(errorDTO);
    								listError.add(errorDTO);
                            	} else {
                            		newObj.setNumberBlock(numberBlock);
                            	}
                            } catch (Exception e) {
                                isExistError = true;
                                ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(10),
										"Số Block phải nhập dạng số!");
								errorList.add(errorDTO);
								listError.add(errorDTO);
                            }
                        } 
                        
                        else if (cell.getColumnIndex() == 11) {
                            try {
                            	Double acreage = Double.parseDouble(formatter.formatCellValue(row.getCell(11)).trim());
                            	if(acreage<0) {
                            		isExistError = true;
                                    ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(9),
    										"Diện tích phải nhập số lớn hơn 0!");
    								errorList.add(errorDTO);
    								listError.add(errorDTO);
                            	} else {
                            		newObj.setAcreage(acreage);
                            	}
                            } catch (Exception e) {
                                isExistError = true;
                                ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(11),
										"Diện tích phải nhập dạng số!");
								errorList.add(errorDTO);
								listError.add(errorDTO);
                            }
                        } 
                        
                        else if (cell.getColumnIndex() == 15) {
                            try {
                            	Long phoneNumberCus = Long.parseLong(formatter.formatCellValue(row.getCell(15)).trim());
                            	newObj.setPhoneNumberCus(formatter.formatCellValue(row.getCell(15)).trim());
                            } catch (Exception e) {
                                isExistError = true;
                                ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(15),
										"Số điện thoại CĐT phải nhập dạng số!");
								errorList.add(errorDTO);
								listError.add(errorDTO);
                            }
                        }
                        
                        else if (cell.getColumnIndex() == 16) {
                            try {
                            	String email = formatter.formatCellValue(row.getCell(16)).trim();
                            	if(!validateEmail(email)) {
                            		isExistError = true;
                                    ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(16),
    										"Email CĐT không đúng định dạng !");
    								errorList.add(errorDTO);
    								listError.add(errorDTO);
                            	}
                            	newObj.setEmailCus(email);
                            } catch (Exception e) {
                                isExistError = true;
                                ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(16),
										"Email CĐT không hợp lệ!");
								errorList.add(errorDTO);
								listError.add(errorDTO);
                            }
                        }
                        
                        else if (cell.getColumnIndex() == 18) {
                            try {
                            	Long phoneNumberEmploy = Long.parseLong(formatter.formatCellValue(row.getCell(18)).trim());
                            	newObj.setPhoneNumberEmploy(formatter.formatCellValue(row.getCell(18)).trim());
                            } catch (Exception e) {
                                isExistError = true;
                                ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(18),
										"Số điện thoại người tiếp xúc phải nhập dạng số!");
								errorList.add(errorDTO);
								listError.add(errorDTO);
                            }
                        }
                        
                        else if (cell.getColumnIndex() == 19) {
                            try {
                            	String email = formatter.formatCellValue(row.getCell(19)).trim();
                            	if(!validateEmail(email)) {
                            		isExistError = true;
                                    ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(19),
    										"Email người tiếp xúc không đúng định dạng !");
    								errorList.add(errorDTO);
    								listError.add(errorDTO);
                            	}
                            	newObj.setEmailEmploy(email);
                            } catch (Exception e) {
                                isExistError = true;
                                ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(19),
										"Email người tiếp xúc không hợp lệ!");
								errorList.add(errorDTO);
								listError.add(errorDTO);
                            }
                        }
                        
                        else if (cell.getColumnIndex() == 20) {
                        	try {
								Date deadlineDateComplete = dateFormat.parse(formatter.formatCellValue(cell));
								if (validateDateFormat(formatter.formatCellValue(cell))) {
									newObj.setDeadlineDateComplete(deadlineDateComplete);
								} else {
									isExistError = true;
	                                ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(20),
											"Hạn hoàn thành tiếp xúc không đúng định dạng !");
									errorList.add(errorDTO);
									listError.add(errorDTO);
								}
							} catch (Exception e) {
								isExistError = true;
                                ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(20),
										"Hạn hoàn thành tiếp xúc không hợp lệ !");
								errorList.add(errorDTO);
								listError.add(errorDTO);
							}
                        } 
                        
                        else if (cell.getColumnIndex() == 21) {
                        	try {
								Date dateExposed = dateFormat.parse(formatter.formatCellValue(cell));
								if (validateDateFormat(formatter.formatCellValue(cell))) {
									newObj.setDateExposed(dateExposed);
								} else {
									isExistError = true;
	                                ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(21),
											"Ngày đã tiếp xúc không đúng định dạng !");
									errorList.add(errorDTO);
									listError.add(errorDTO);
								}
							} catch (Exception e) {
								isExistError = true;
                                ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(21),
										"Ngày đã tiếp xúc không hợp lệ !");
								errorList.add(errorDTO);
								listError.add(errorDTO);
							}
                        } 
                        
                        else if (cell.getColumnIndex() == 22) {
                            try {
                            	Long levelDeployment = Long.parseLong(formatter.formatCellValue(row.getCell(22)).trim());
                            	if(levelDeployment < 1l || levelDeployment > 2l) {
                            		isExistError = true;
                                    ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(22),
    										"Mức độ triển khai chỉ nhập 1 hoặc 2 !");
    								errorList.add(errorDTO);
    								listError.add(errorDTO);
                            	} else {
                            		newObj.setLevelDeployment(formatter.formatCellValue(row.getCell(22)).trim());
                            	}
                            } catch (Exception e) {
                                isExistError = true;
                                ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(22),
										"Mức độ triển khai phải nhập dạng số !");
								errorList.add(errorDTO);
								listError.add(errorDTO);
                            }
                        }
                        
                        else if (cell.getColumnIndex() == 23) {
                            try {
                            	Long contractingStatus = Long.parseLong(formatter.formatCellValue(row.getCell(23)).trim());
                            	if(contractingStatus < 1l || contractingStatus > 2l) {
                            		isExistError = true;
                                    ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(23),
    										"Kết quả thương thảo, ký hợp đồng chỉ nhập 1 hoặc 2 !");
    								errorList.add(errorDTO);
    								listError.add(errorDTO);
                            	} else {
                            		if(newObj.getLevelDeployment()!=null & !newObj.getLevelDeployment().equals("1")) {
                            			newObj.setContractingStatus(formatter.formatCellValue(row.getCell(23)).trim());
                            		}
                            	}
                            } catch (Exception e) {
                                isExistError = true;
                                ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(23),
										"Kết quả thương thảo, ký hợp đồng phải nhập dạng số !");
								errorList.add(errorDTO);
								listError.add(errorDTO);
                            }
                        }
                        
						else if (cell.getColumnIndex() == 24) {
							try {
								Date dateContract = dateFormat.parse(formatter.formatCellValue(cell));
								if (validateDateFormat(formatter.formatCellValue(cell))) {
									newObj.setDateContract(dateContract);
								} else {
									isExistError = true;
	                                ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(24),
											"Ngày ký hợp đồng không đúng định dạng !");
									errorList.add(errorDTO);
								}
							} catch (Exception e) {
								isExistError = true;
                                ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(24),
										"Ngày ký hợp đồng không hợp lệ !");
								errorList.add(errorDTO);
							}
						}

						else if (cell.getColumnIndex() == 25) {
							String contractCode = formatter.formatCellValue(cell).trim();
							if(newObj.getContractingStatus()!=null && newObj.getContractingStatus().equals("2")) {
								ProgressPlanProjectDTO dto = mapContract.get(contractCode.toUpperCase());
								if(dto==null) {
									isExistError = true;
                                    ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(25),
                                			" Số hợp đồng không tồn tại !");
      								errorList.add(errorDTO);
    								listError.add(errorDTO);
								} else {
									if(newObj.getContractingStatus()!=null && newObj.getContractingStatus().equals("2")) {
										newObj.setContractId(dto.getContractId());
//										newObj.setDateContract(dto.getDateContract());
									}
								}
							}
						}
                        
                        else if (cell.getColumnIndex() == 26) {
                                String description = formatter.formatCellValue(cell).trim();
                                if (description.length() <= 1000) {
                                    newObj.setNote(description);
                                } else {
                                	isExistError = true;
                                    ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(26),
                                			" Ghi chú vượt quá 1000 ký tự !");
      								errorList.add(errorDTO);
    								listError.add(errorDTO);
                                }
                        }
                        
                      //
                    } else {
                    	if(i==1) {
							checkNotNullFileImport(i,isExistError, errorList, row, "Mã khu vực không được để trống !", listError);
                    	} else if(i==2) {
							checkNotNullFileImport(i,isExistError, errorList, row, "Tỉnh không được để trống !", listError);
                    	} else if(i==5) {
							checkNotNullFileImport(i,isExistError, errorList, row, "Tên dự án không được để trống !", listError);
                    	} else if(i==7) {
							checkNotNullFileImport(i,isExistError, errorList, row, "Tên chủ đầu tư không được để trống !", listError);
                    	} else if(i==9) {
							checkNotNullFileImport(i,isExistError, errorList, row, "Số căn hộ không được để trống !", listError);
                    	} else if(i==11) {
							checkNotNullFileImport(i,isExistError, errorList, row, "Diện tích không được để trống !", listError);
                    	} else if(i==13) {
							checkNotNullFileImport(i,isExistError, errorList, row, "Đầu mối liên hệ CĐT không được để trống !", listError);
                    	} else if(i==15) {
							checkNotNullFileImport(i,isExistError, errorList, row, "Số điện thoại CĐT không được để trống !", listError);
                    	} 
//                    	else if(i==16) {
//							checkNotNullFileImport(i,isExistError, errorList, row, "Email CĐT không được để trống !");
//                    	} 
                    	else if(i==17) {
							checkNotNullFileImport(i,isExistError, errorList, row, "Họ tên người tiếp xúc không được để trống !", listError);
                    	} else if(i==18) {
							checkNotNullFileImport(i,isExistError, errorList, row, "Số điện thoại người tiếp xúc không được để trống !", listError);
                    	} else if(i==19) {
							checkNotNullFileImport(i,isExistError, errorList, row, "Email người tiếp xúc không được để trống !", listError);
                    	} else if(i==20) {
							checkNotNullFileImport(i,isExistError, errorList, row, "Hạn hoàn thành tiếp xúc không được để trống !", listError);
                    	} else if(i==25){
                    		if(newObj.getContractingStatus()!=null && newObj.getContractingStatus().equals("2")) {
                    			checkNotNullFileImport(i,isExistError, errorList, row, "Số hợp đồng không được để trống !", listError);
                    		}
                    	}
                    }
                }
                if (listError.size()==0) {
                    newObj.setDistrictCode(formatter.formatCellValue(row.getCell(3)).trim());
                    newObj.setCommuneCode(formatter.formatCellValue(row.getCell(4)).trim());
                    newObj.setProjectName(formatter.formatCellValue(row.getCell(5)).trim());
                    newObj.setAddress(formatter.formatCellValue(row.getCell(6)).trim());
                    newObj.setInvestorName(formatter.formatCellValue(row.getCell(7)).trim());
                    newObj.setProjectPerformance(formatter.formatCellValue(row.getCell(8)).trim());
                    newObj.setProgressProject(formatter.formatCellValue(row.getCell(12)).trim());
                    newObj.setContactCus(formatter.formatCellValue(row.getCell(13)).trim());
                    newObj.setPositionCus(formatter.formatCellValue(row.getCell(14)).trim());
                    newObj.setEmailCus(formatter.formatCellValue(row.getCell(16)).trim());
                    newObj.setContactEmploy(formatter.formatCellValue(row.getCell(17)).trim());
                    newObj.setEmailEmploy(formatter.formatCellValue(row.getCell(19)).trim());
                    
                    workLst.add(newObj);
                }

            }
        }
        if (errorList.size()>0) {
        	workLst = new ArrayList<ProgressPlanProjectDTO>();
        	ProgressPlanProjectDTO objErr = new ProgressPlanProjectDTO();
			objErr.setErrorList(errorList);
			objErr.setMessageColumn(27);
//			objErr.setErrorFilePath(UEncrypt.encryptFileUploadPath(filePath));
			objErr.setFilePathError(UEncrypt.encryptFileUploadPath(filePath));
			workLst.add(objErr);
        }
        workbook.close();
        return workLst;
    }
    
    private void checkNotNullFileImport(int i, Boolean isExistError, List<ExcelErrorDTO> errorList, Row row, String detailErr, List<ExcelErrorDTO> listError) {
    	isExistError = true;
        ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, colAlias.get(i),
        		detailErr);
		errorList.add(errorDTO);
		listError.add(errorDTO);
    }
    
    public Long saveImportProject(ProgressPlanProjectDTO obj, HttpServletRequest request) throws Exception{
    	KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
    	Long ids = 0l;
    	for(ProgressPlanProjectDTO dto : obj.getLstDataImport()) {
    		dto.setCreateDate(new Date());
    		dto.setCreateUserId(user.getVpsUserInfo().getSysUserId());
    		if(dto.getLevelDeployment()!=null) {
        		if(dto.getLevelDeployment().equals("2")) {
            		if((dto.getDateContract()==null && dto.getContractCode()==null)) {
            			dto.setStatus("2");
            		} else if(dto.getDateContract()!=null && dto.getContractCode()!=null) {
            			dto.setStatus("3");
            		} else {
            			dto.setStatus("2");
            		}
            	} else {
            		dto.setStatus("3");
            	}
        	} else {
        		dto.setStatus("1");
        	}
    		
    		ids = progressPlanProjectDAO.saveObject(dto.toModel());
    		
    		if(dto.getStatus()!=null && !dto.getStatus().equals("3")) {
    			SimpleDateFormat dateFor = new SimpleDateFormat("dd/MM/yyyy");
    	    	String dateDeadline = dateFor.format(dto.getDeadlineDateComplete());
    			
    	    	SendEmailBO email = new SendEmailBO();
        		email.setSubject("Quản lý dự án tiếp xúc IBS");
        		email.setStatus(0l);
        		email.setContent("Sắp đến ngày hạn hoàn thành tiếp xúc "+ dateDeadline + " của dự án: "+ dto.getProjectName());
        		email.setReceiveEmail(dto.getEmailEmploy());
        		email.setCreatedDate(new Date());
        		email.setCreatedUserId(user.getVpsUserInfo().getSysUserId());
        		sendEmailDAO.saveObject(email);
        		
        		SendSmsEmailBO sms = new SendSmsEmailBO();
        		sms.setSubject("Quản lý dự án tiếp xúc IBS");
        		sms.setStatus("0");
        		sms.setContent("Sắp đến ngày hạn hoàn thành tiếp xúc "+ dateDeadline + " của dự án: "+ dto.getProjectName());
        		sms.setReceiveEmail(dto.getEmailEmploy());
        		sms.setReceivePhoneNumber(dto.getPhoneNumberEmploy());
        		sms.setCreatedDate(new Date());
        		sms.setCreatedUserId(user.getVpsUserInfo().getSysUserId());
        		sendSmsEmailDAO.saveObject(sms);
        		
        		ProgressPlanProjectDTO gdt = progressPlanProjectDAO.getInfoUserBySysUser(user.getVpsUserInfo().getSysGroupId());
        		
        		if(gdt==null) {
        			throw new IllegalArgumentException("Không tìm thấy tỉnh trưởng từ người dùng đăng nhập !");
        		}
        		
        		SendEmailBO emailGdt = new SendEmailBO();
        		emailGdt.setSubject("Quản lý dự án tiếp xúc IBS");
        		emailGdt.setStatus(0l);
        		emailGdt.setContent("Sắp đến ngày hạn hoàn thành tiếp xúc "+ dateDeadline + " của dự án: "+ dto.getProjectName());
        		emailGdt.setReceiveEmail(gdt.getEmailEmploy());
        		emailGdt.setCreatedDate(new Date());
        		emailGdt.setCreatedUserId(user.getVpsUserInfo().getSysUserId());
        		sendEmailDAO.saveObject(emailGdt);
        		
        		SendSmsEmailBO smsGdt = new SendSmsEmailBO();
        		smsGdt.setSubject("Quản lý dự án tiếp xúc IBS");
        		smsGdt.setStatus("0");
        		smsGdt.setContent("Sắp đến ngày hạn hoàn thành tiếp xúc "+ dateDeadline + " của dự án: "+ dto.getProjectName());
        		smsGdt.setReceiveEmail(gdt.getEmailEmploy());
        		smsGdt.setReceivePhoneNumber(gdt.getPhoneNumberEmploy());
        		smsGdt.setCreatedDate(new Date());
        		smsGdt.setCreatedUserId(user.getVpsUserInfo().getSysUserId());
        		sendSmsEmailDAO.saveObject(smsGdt);
        	}
    	}
    	
    	return ids;
    }
    
    public List<String> checkDomainUser(HttpServletRequest request){
    	String groupId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
				Constant.AdResourceKey.DATA, request);
		List<String> groupIdList = ConvertData.convertStringToList(groupId, ",");
		return groupIdList;
    }
    
    public String exportExcelProject(ProgressPlanProjectDTO obj) throws Exception {
		obj.setPage(null);
		obj.setPageSize(null);
		InputStream file = null;
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		file = new BufferedInputStream(new FileInputStream(filePath + "Export_danhsach_tiepxuc_IBS.xlsx"));
		List<ProgressPlanProjectDTO> data = progressPlanProjectDAO.doSearch(obj);

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
				udir.getAbsolutePath() + File.separator + "Export_danhsach_tiepxuc_IBS.xlsx");
		// DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		XSSFSheet sheet = workbook.getSheetAt(0);
		if (data != null && !data.isEmpty()) {
			XSSFCellStyle style = ExcelUtils.styleText(sheet);
			XSSFCellStyle styleNumber = ExcelUtils.styleText(sheet);
			// HuyPQ-22/08/2018-start
			XSSFCellStyle styleCenter = ExcelUtils.styleDate(sheet);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			styleCenter.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			// HuyPQ-end
			XSSFCellStyle styleCurrency = ExcelUtils.styleCurrency(sheet);
			styleNumber.setAlignment(HorizontalAlignment.RIGHT);
			int i = 3;
			for (ProgressPlanProjectDTO dto : data) {
				Row row = sheet.createRow(i++);
				Cell cell = row.createCell(0, CellType.STRING);
				cell.setCellValue("" + (i - 3));
				cell.setCellStyle(styleNumber);
				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue((dto.getAreaCode() != null) ? dto.getAreaCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue((dto.getProvinceCode() != null) ? dto.getProvinceCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(3, CellType.STRING);
				cell.setCellValue((dto.getDistrictCode() != null) ? dto.getDistrictCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue((dto.getCommuneCode() != null) ? dto.getCommuneCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue((dto.getProjectName() != null) ? dto.getProjectName() : "");
				cell.setCellStyle(styleCenter);
				cell = row.createCell(6, CellType.STRING);
				cell.setCellValue((dto.getAddress() != null) ? dto.getAddress() : "");
				cell.setCellStyle(style);
				cell = row.createCell(7, CellType.STRING);
				cell.setCellValue((dto.getInvestorName() != null) ? dto.getInvestorName() : "");
				cell.setCellStyle(style);
				
				cell = row.createCell(8, CellType.STRING);
				cell.setCellValue((dto.getProjectPerformance() != null) ? dto.getProjectPerformance() : "");
				cell.setCellStyle(style);
				cell = row.createCell(9, CellType.STRING);
				cell.setCellValue((dto.getNumberHouse() != null) ? dto.getNumberHouse() : 0l);
				cell.setCellStyle(style);
				cell = row.createCell(10, CellType.STRING);
				cell.setCellValue((dto.getNumberBlock() != null) ? dto.getNumberBlock() : 0l);
				cell.setCellStyle(style);
				cell = row.createCell(11, CellType.STRING);
				cell.setCellValue((dto.getAcreage() != null) ? dto.getAcreage() : 0D);
				cell.setCellStyle(style);
				cell = row.createCell(12, CellType.STRING);
				cell.setCellValue((dto.getProgressProject() != null) ? dto.getProgressProject() : "");
				cell.setCellStyle(style);
				cell = row.createCell(13, CellType.STRING);
				cell.setCellValue((dto.getContactCus() != null) ? dto.getContactCus() : "");
				cell.setCellStyle(style);
				cell = row.createCell(14, CellType.STRING);
				cell.setCellValue((dto.getPositionCus() != null) ? dto.getPositionCus() : "");
				cell.setCellStyle(style);
				cell = row.createCell(15, CellType.STRING);
				cell.setCellValue((dto.getPhoneNumberCus() != null) ? dto.getPhoneNumberCus() : "");
				cell.setCellStyle(style);
				cell = row.createCell(16, CellType.STRING);
				cell.setCellValue((dto.getEmailCus() != null) ? dto.getEmailCus() : "");
				cell.setCellStyle(style);
				cell = row.createCell(17, CellType.STRING);
				cell.setCellValue((dto.getContactEmploy() != null) ? dto.getContactEmploy() : "");
				cell.setCellStyle(style);
				cell = row.createCell(18, CellType.STRING);
				cell.setCellValue((dto.getPhoneNumberEmploy() != null) ? dto.getPhoneNumberEmploy() : "");
				cell.setCellStyle(style);
				cell = row.createCell(19, CellType.STRING);
				cell.setCellValue((dto.getEmailEmploy() != null) ? dto.getEmailEmploy() : "");
				cell.setCellStyle(style);
				cell = row.createCell(20, CellType.STRING);
				cell.setCellValue((dto.getDeadlineDateComplete() != null) ? dto.getDeadlineDateComplete() : null);
				cell.setCellStyle(styleCenter);
				cell = row.createCell(21, CellType.STRING);
				cell.setCellValue((dto.getDateExposed() != null) ? dto.getDateExposed() : null);
				cell.setCellStyle(styleCenter);
				cell = row.createCell(22, CellType.STRING);
				if(dto.getLevelDeployment()!=null) {
					if(dto.getLevelDeployment().equals("2")) {
						cell.setCellValue("Có thể triển khai");
					} else {
						cell.setCellValue("Không thể triển khai");
					}
				} else {
					cell.setCellValue("");
				}
				cell.setCellStyle(style);
				cell = row.createCell(23, CellType.STRING);
				if(dto.getContractingStatus()!=null) {
					if(dto.getContractingStatus().equals("1")) {
						cell.setCellValue("Tiếp xúc thành công , đang thương thảo ký hợp đồng");
					} else {
						cell.setCellValue("Đã ký hợp đồng");
					}
				} else {
					cell.setCellValue("");
				}
				cell.setCellStyle(style);
				cell = row.createCell(24, CellType.STRING);
				cell.setCellValue((dto.getDateContract() != null) ? dto.getDateContract() : null);
				cell.setCellStyle(styleCenter);
				cell = row.createCell(25, CellType.STRING);
				cell.setCellValue((dto.getContractCode() != null) ? dto.getContractCode() : "");
				cell.setCellStyle(style);
				cell = row.createCell(26, CellType.STRING);
				if(dto.getStatus()!=null) {
					if(dto.getStatus().equals("1")) {
						cell.setCellValue("Chưa thực hiện");
					} else {
						cell.setCellValue("Đang thực hiện");
					}
				} else {
					cell.setCellValue("");
				}
				cell.setCellStyle(style);
				cell = row.createCell(27, CellType.STRING);
				cell.setCellValue((dto.getNote() != null) ? dto.getNote() : "");
				cell.setCellStyle(style);
			}
		}
		workbook.write(outFile);
		workbook.close();
		outFile.close();

		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "Export_danhsach_tiepxuc_IBS.xlsx");
		return path;
	}
    
    public List<UtilAttachDocumentDTO> getListFile(Long objId){
    	return progressPlanProjectDAO.getListFile(objId);
    }
    
    boolean validateDateFormat(String date) {
		String dateBreaking[] = date.split("/");
		if (Integer.parseInt(dateBreaking[1]) > 12) {
			return false;
		}
		if (Integer.parseInt(dateBreaking[2]) < 1900) {
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
}
