package com.viettel.coms.rest;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.activation.DataHandler;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.viettel.asset.dto.SysGroupDto;
import com.viettel.coms.bo.ManageDataOutsideOsBO;
import com.viettel.coms.business.ManageDataOutsideOsBusinessImpl;
import com.viettel.coms.dto.AppParamDTO;
import com.viettel.coms.dto.CatConstructionTypeDTO;
import com.viettel.coms.dto.CatStationDTO;
import com.viettel.coms.dto.ConstructionDetailDTO;
import com.viettel.coms.dto.ExcelErrorDTO;
import com.viettel.coms.dto.ManageDataOutsideOsDTO;
import com.viettel.coms.utils.ExcelUtils;
import com.viettel.ktts2.common.BusinessException;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.ktts2.common.UString;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.service.base.utils.StringUtils;

public class ManageDataOutsideOsRsServiceImpl implements ManageDataOutsideOsRsService{
	
	protected final Logger log = Logger.getLogger(ManageDataOutsideOsRsServiceImpl.class);
	@Value("${folder_upload2}")
	private String folderUpload;
	@Value("${default_sub_folder_upload}")
	private String defaultSubFolderUpload;
	@Value("${allow.file.ext}")
	private String allowFileExt;
	@Value("${allow.folder.dir}")
	private String allowFolderDir;
	@Value("${folder_upload}")
	private String folderTemp;
	
	@Value("${folder_upload}")
	private String folder2Upload;
	private static ManageDataOutsideOsDTO manaOs;
	@Autowired
	ManageDataOutsideOsBusinessImpl manageDataOutsideOsBusinessImpl;
	
	@Context
	HttpServletRequest request;
	 
	 public Response doSearchOS(ManageDataOutsideOsDTO obj) {
		 DataListDTO ls = manageDataOutsideOsBusinessImpl.doSearchOS(obj);
		 return Response.ok(ls).build();
	 }

	@Override
	public Response checkRoleCNKT() {
		Boolean check = manageDataOutsideOsBusinessImpl.checkRoleCNKT(request);
		if(check) {
			return Response.ok(1l).build();
		} else {
			return Response.ok(0l).build();
		}
		
	}
	
	public Response saveAddNew(ManageDataOutsideOsDTO obj){
		try {
			Long id = manageDataOutsideOsBusinessImpl.saveAddNew(obj,request);
			if(id==0l) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			} else {
				return Response.ok(id).build();
			}
		} catch (BusinessException e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		} catch (Exception e) {
            e.printStackTrace();
            return Response.ok().entity(Collections.singletonMap("error", "Có lỗi xảy ra")).build();
        }
	}
	
	public Response saveUpdateNew(ManageDataOutsideOsDTO obj){
		try {
			Long id = manageDataOutsideOsBusinessImpl.saveUpdateNew(obj,request);
			if(id==0l) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			} else {
				return Response.ok(id).build();
			}
		} catch (BusinessException e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		} catch (Exception e) {
            e.printStackTrace();
            return Response.ok().entity(Collections.singletonMap("error", "Có lỗi xảy ra")).build();
        }
	}
	
	@Override
	public Response getAutoCompleteConstruction(ManageDataOutsideOsDTO obj) {
		DataListDTO ls = manageDataOutsideOsBusinessImpl.getAutoCompleteConstruction(obj);
		return Response.ok(ls).build();
	}
	
	@Override
	public Response setStatus(ManageDataOutsideOsDTO obj) {
		manaOs = obj;
		return Response.ok(manaOs).build();
	}
	
	//tatph - start 13/11/2019
	@Override
	public Response downloadFile(HttpServletRequest request) throws Exception {
		String fileName = UEncrypt.decryptFileUploadPath(request.getQueryString());
		File file = new File(folderUpload + File.separatorChar + fileName);
		InputStream ExcelFileToRead = new FileInputStream(folderUpload + File.separatorChar + fileName);
		if (!file.exists()) {
			file = new File(folderUpload + File.separatorChar + fileName);
			if (!file.exists()) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}

		}
		int lastIndex = fileName.lastIndexOf(File.separatorChar);
		String fileNameReturn = fileName.substring(lastIndex + 1);
		return Response.ok((Object) file)
				.header("Content-Disposition", "attachment; filename=\"" + fileNameReturn + "\"").build();
	}



	private boolean isFolderAllowFolderSave(String folderDir) {
		return UString.isFolderAllowFolderSave(folderDir, allowFolderDir);
	}

	private boolean isExtendAllowSave(String fileName) {
		return UString.isExtendAllowSave(fileName, allowFileExt);
	}


	@Override
	public Response importExpertiseProposal(Attachment attachments, HttpServletRequest request) throws Exception {
		KttsUserSession objUser = manageDataOutsideOsBusinessImpl.getUserSession(request);
		String folderParam = UString.getSafeFileName(request.getParameter("folder"));
		String filePathReturn;
		String filePath;

		if (UString.isNullOrWhitespace(folderParam)) {
			folderParam = defaultSubFolderUpload;
		} else {
			if (!isFolderAllowFolderSave(folderParam)) {
				throw new BusinessException("folder khong nam trong white list: folderParam=" + folderParam);
			}
		}

		DataHandler dataHandler = attachments.getDataHandler();

		// get filename to be uploaded
		MultivaluedMap<String, String> multivaluedMap = attachments.getHeaders();
		String fileName = UFile.getFileName(multivaluedMap);

		if (!isExtendAllowSave(fileName)) {
			throw new BusinessException("File extension khong nam trong list duoc up load, file_name:" + fileName);
		}

		// write & upload file to server
		try (InputStream inputStream = dataHandler.getInputStream();) {
			filePath = UFile.writeToFileServerATTT(inputStream, fileName, folderParam, folderUpload);
			filePathReturn = UEncrypt.encryptFileUploadPath(filePath);
		} catch (Exception ex) {
			throw new BusinessException("Loi khi save file", ex);
		}

		try {
			List<ManageDataOutsideOsDTO> result = manageDataOutsideOsBusinessImpl
					.importExpertiseProposal( filePath);
			if (result != null && !result.isEmpty()
					&& (result.get(0).getErrorList() == null || result.get(0).getErrorList().size() == 0)) {
				for (ManageDataOutsideOsDTO obj : result) {
					ManageDataOutsideOsDTO dto = new ManageDataOutsideOsDTO();
					dto.setPage(null);
					dto.setPageSize(null);
					dto.setStatus(null);
					dto.setConstructionCode(obj.getConstructionCode());
					List<ManageDataOutsideOsDTO> lst = manageDataOutsideOsBusinessImpl.doSearchOS(dto).getData();
					if(lst != null && lst.size() > 0) {
						ManageDataOutsideOsDTO saveObj = lst.get(0);
						saveObj.setStatus("4");
						saveObj.setConstructionCode(obj.getConstructionCode());
						saveObj.setStationCode(obj.getStationCode());
						saveObj.setHdContractCode(obj.getHdContractCode());
						
						saveObj.setGttdHshcHardDate(obj.getGttdHshcHardDate());
						saveObj.setGttdCompleteExpertiseDate(obj.getGttdCompleteExpertiseDate());
						saveObj.setGttdElectricalProcedures(obj.getGttdElectricalProcedures());
						saveObj.setGttdPullCableLabor(obj.getGttdPullCableLabor());
						saveObj.setGttdCostMaterial(obj.getGttdCostMaterial());
						saveObj.setGttdCostHshc(obj.getGttdCostHshc());
						saveObj.setGttdCostTransportWarehouse(obj.getGttdCostTransportWarehouse());
						saveObj.setGttdCostOrther(obj.getGttdCostOrther());
						saveObj.setGttdSalaryCableOrther(obj.getGttdSalaryCableOrther());
						saveObj.setGttdWeldingSalary(obj.getGttdWeldingSalary());
						saveObj.setGttdVat(obj.getGttdVat());
						saveObj.setGttdTotalMoney(obj.getGttdTotalMoney());
						saveObj.setGttdGttdPtk(obj.getGttdGttdPtk());
						saveObj.setGttdHshcMonth(obj.getGttdHshcMonth());
						saveObj.setGttdSalaryMonth(obj.getGttdSalaryMonth());
						saveObj.setGttdSalaryReal(obj.getGttdSalaryReal());
						saveObj.setGttdHshcError(obj.getGttdHshcError());
						saveObj.setGttdErrorReason(obj.getGttdErrorReason());
						
						saveObj.setExpertisedUserId(objUser.getSysUserId());
						
						manageDataOutsideOsBusinessImpl.updateManaOs(saveObj);
					}
					
				}
				return Response.ok(result).build();
			} else if (result == null || result.isEmpty()) {
				return Response.ok().entity(Response.Status.NO_CONTENT).build();
			} else {
				return Response.ok(result).build();
			}

		} catch (IllegalArgumentException e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}

	}
	
	@Override
	public Response importExpertiseProposalCDT(Attachment attachments, HttpServletRequest request) throws Exception {
		KttsUserSession objUser = manageDataOutsideOsBusinessImpl.getUserSession(request);
		String folderParam = UString.getSafeFileName(request.getParameter("folder"));
		String filePathReturn;
		String filePath;

		if (UString.isNullOrWhitespace(folderParam)) {
			folderParam = defaultSubFolderUpload;
		} else {
			if (!isFolderAllowFolderSave(folderParam)) {
				throw new BusinessException("folder khong nam trong white list: folderParam=" + folderParam);
			}
		}

		DataHandler dataHandler = attachments.getDataHandler();

		// get filename to be uploaded
		MultivaluedMap<String, String> multivaluedMap = attachments.getHeaders();
		String fileName = UFile.getFileName(multivaluedMap);

		if (!isExtendAllowSave(fileName)) {
			throw new BusinessException("File extension khong nam trong list duoc up load, file_name:" + fileName);
		}

		// write & upload file to server
		try (InputStream inputStream = dataHandler.getInputStream();) {
			filePath = UFile.writeToFileServerATTT(inputStream, fileName, folderParam, folderUpload);
			filePathReturn = UEncrypt.encryptFileUploadPath(filePath);
		} catch (Exception ex) {
			throw new BusinessException("Loi khi save file", ex);
		}

		try {
			List<ManageDataOutsideOsDTO> result = manageDataOutsideOsBusinessImpl
					.importExpertiseProposalCDT( filePath);
			if (result != null && !result.isEmpty()
					&& (result.get(0).getErrorList() == null || result.get(0).getErrorList().size() == 0)) {
				for (ManageDataOutsideOsDTO obj : result) {
					ManageDataOutsideOsDTO dto = new ManageDataOutsideOsDTO();
					dto.setPage(null);
					dto.setPageSize(null);
					dto.setStatus(null);
					dto.setConstructionCode(obj.getConstructionCode());
					List<ManageDataOutsideOsDTO> lst = manageDataOutsideOsBusinessImpl.doSearchOS(dto).getData();
					if(lst != null && lst.size() > 0) {
						ManageDataOutsideOsDTO saveObj = lst.get(0);
						saveObj.setConstructionCode(obj.getConstructionCode());
						saveObj.setStationCode(obj.getStationCode());
						saveObj.setHdContractCode(obj.getHdContractCode());
						saveObj.setStatus("5");
						
						saveObj.setQtdnSuggestionsDate(obj.getQtdnSuggestionsDate());
						saveObj.setQtdnValue(obj.getQtdnValue());
						saveObj.setQtdnVtnetDate(obj.getQtdnVtnetDate());
						saveObj.setQtdnDescription(obj.getQtdnDescription());
						saveObj.setQttdExpertiseEmployee(obj.getQttdExpertiseEmployee());
						saveObj.setQttdExpertiseCompleteDate(obj.getQttdExpertiseCompleteDate());
						saveObj.setQttdValue(obj.getQttdValue());
						saveObj.setQttdDescription(obj.getQttdDescription());
						
						saveObj.setSettlementedUserId(objUser.getSysUserId());
						
						manageDataOutsideOsBusinessImpl.updateManaOs(saveObj);
					}
					
				}
				return Response.ok(result).build();
			} else if (result == null || result.isEmpty()) {
				return Response.ok().entity(Response.Status.NO_CONTENT).build();
			} else {
				return Response.ok(result).build();
			}

		} catch (IllegalArgumentException e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}

	}
	
	@Override
	public Response importInvoice(Attachment attachments, HttpServletRequest request) throws Exception {
		KttsUserSession objUser = manageDataOutsideOsBusinessImpl.getUserSession(request);
		String folderParam = UString.getSafeFileName(request.getParameter("folder"));
		String filePathReturn;
		String filePath;

		if (UString.isNullOrWhitespace(folderParam)) {
			folderParam = defaultSubFolderUpload;
		} else {
			if (!isFolderAllowFolderSave(folderParam)) {
				throw new BusinessException("folder khong nam trong white list: folderParam=" + folderParam);
			}
		}

		DataHandler dataHandler = attachments.getDataHandler();

		// get filename to be uploaded
		MultivaluedMap<String, String> multivaluedMap = attachments.getHeaders();
		String fileName = UFile.getFileName(multivaluedMap);

		if (!isExtendAllowSave(fileName)) {
			throw new BusinessException("File extension khong nam trong list duoc up load, file_name:" + fileName);
		}

		// write & upload file to server
		try (InputStream inputStream = dataHandler.getInputStream();) {
			filePath = UFile.writeToFileServerATTT(inputStream, fileName, folderParam, folderUpload);
			filePathReturn = UEncrypt.encryptFileUploadPath(filePath);
		} catch (Exception ex) {
			throw new BusinessException("Loi khi save file", ex);
		}

		try {
			List<ManageDataOutsideOsDTO> result = manageDataOutsideOsBusinessImpl
					.importInvoice( filePath);
			if (result != null && !result.isEmpty()
					&& (result.get(0).getErrorList() == null || result.get(0).getErrorList().size() == 0)) {
				for (ManageDataOutsideOsDTO obj : result) {
					ManageDataOutsideOsDTO dto = new ManageDataOutsideOsDTO();
					dto.setPage(null);
					dto.setPageSize(null);
					dto.setStatus(null);
					dto.setConstructionCode(obj.getConstructionCode());
					List<ManageDataOutsideOsDTO> lst = manageDataOutsideOsBusinessImpl.doSearchOS(dto).getData();
					if(lst != null && lst.size() > 0) {
						ManageDataOutsideOsDTO saveObj = lst.get(0);
						saveObj.setConstructionCode(obj.getConstructionCode());
						saveObj.setStationCode(obj.getStationCode());
						saveObj.setHdContractCode(obj.getHdContractCode());
						saveObj.setStatus("6");
						
						saveObj.setXhdPtcDate(obj.getXhdPtcDate());
						saveObj.setXhdXhdDate(obj.getXhdXhdDate());
						saveObj.setXhdSoHd(obj.getXhdSoHd());
						saveObj.setXhdRevenueMonth(obj.getXhdRevenueMonth());
						saveObj.setXhdDescription(obj.getXhdDescription());
						
						saveObj.setInvoiceUserId(objUser.getSysUserId());
						
						manageDataOutsideOsBusinessImpl.updateManaOs(saveObj);
					}
					
				}
				return Response.ok(result).build();
			} else if (result == null || result.isEmpty()) {
				return Response.ok().entity(Response.Status.NO_CONTENT).build();
			} else {
				return Response.ok(result).build();
			}

		} catch (IllegalArgumentException e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}

	}
	
	@Override
	public Response importLiquidation(Attachment attachments, HttpServletRequest request) throws Exception {
		KttsUserSession objUser = manageDataOutsideOsBusinessImpl.getUserSession(request);
		String folderParam = UString.getSafeFileName(request.getParameter("folder"));
		String filePathReturn;
		String filePath;

		if (UString.isNullOrWhitespace(folderParam)) {
			folderParam = defaultSubFolderUpload;
		} else {
			if (!isFolderAllowFolderSave(folderParam)) {
				throw new BusinessException("folder khong nam trong white list: folderParam=" + folderParam);
			}
		}

		DataHandler dataHandler = attachments.getDataHandler();

		// get filename to be uploaded
		MultivaluedMap<String, String> multivaluedMap = attachments.getHeaders();
		String fileName = UFile.getFileName(multivaluedMap);

		if (!isExtendAllowSave(fileName)) {
			throw new BusinessException("File extension khong nam trong list duoc up load, file_name:" + fileName);
		}

		// write & upload file to server
		try (InputStream inputStream = dataHandler.getInputStream();) {
			filePath = UFile.writeToFileServerATTT(inputStream, fileName, folderParam, folderUpload);
			filePathReturn = UEncrypt.encryptFileUploadPath(filePath);
		} catch (Exception ex) {
			throw new BusinessException("Loi khi save file", ex);
		}

		try {
			List<ManageDataOutsideOsDTO> result = manageDataOutsideOsBusinessImpl
					.importLiquidation( filePath);
			if (result != null && !result.isEmpty()
					&& (result.get(0).getErrorList() == null || result.get(0).getErrorList().size() == 0)) {
				for (ManageDataOutsideOsDTO obj : result) {
					ManageDataOutsideOsDTO dto = new ManageDataOutsideOsDTO();
					dto.setPage(null);
					dto.setPageSize(null);
					dto.setStatus(null);
					dto.setConstructionCode(obj.getConstructionCode());
					List<ManageDataOutsideOsDTO> lst = manageDataOutsideOsBusinessImpl.doSearchOS(dto).getData();
					if(lst != null && lst.size() > 0) {
						ManageDataOutsideOsDTO saveObj = lst.get(0);
						saveObj.setConstructionCode(obj.getConstructionCode());
						saveObj.setStationCode(obj.getStationCode());
						saveObj.setHdContractCode(obj.getHdContractCode());
						saveObj.setStatus("7");
						
						saveObj.setTlSignDate(obj.getTlSignDate());
						saveObj.setTlValue(obj.getTlValue());
						saveObj.setTlDescription(obj.getTlDescription());
						saveObj.setTlDifferenceQuantity(saveObj.getGtslQuantityValue() - saveObj.getHdContractValue());
						saveObj.setTlRate((double)Math.round((saveObj.getGtslQuantityValue() - saveObj.getHdContractValue())/saveObj.getHdContractValue() * 100000d) / 1000d);
						
						saveObj.setLiquidatedUserId(objUser.getSysUserId());
						
						manageDataOutsideOsBusinessImpl.updateManaOs(saveObj);
					}
					
				}
				return Response.ok(result).build();
			} else if (result == null || result.isEmpty()) {
				return Response.ok().entity(Response.Status.NO_CONTENT).build();
			} else {
				return Response.ok(result).build();
			}

		} catch (IllegalArgumentException e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}

	}
	
	@Override
	public Response getExcelTemplateExpertiseProposal(HttpServletRequest request) throws Exception {
		String filePath = UEncrypt.decryptFileUploadPath(request.getQueryString());
		File file = new File(filePath);
		InputStream ExcelFileToRead = new FileInputStream(filePath);

		if (!file.exists()) {
			file = new File(filePath);
			if (!file.exists()) {
				// logger.warn("File {} is not found", fileName);
				return Response.status(Response.Status.BAD_REQUEST).build();
			}

		}

		

		// get data for sheet 1
//		manaOs.setStatus("3");
		List<ManageDataOutsideOsDTO> constructionTypeLst = manageDataOutsideOsBusinessImpl.doSearchOS(manaOs).getData();
		XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
		XSSFSheet sheet = wb.getSheetAt(0);
		// write susGroup data to excel file
//		XSSFSheet sheet = workbook.getSheetAt(0);
  		if ((constructionTypeLst != null && !constructionTypeLst.isEmpty())) { // huy-edit
  			

  			CellStyle style = wb.createCellStyle(); // Create new style
  			style.setWrapText(true); // Set wordwrap
  			int i = 4;
  			for (ManageDataOutsideOsDTO dto : constructionTypeLst) {
  				Row row = sheet.createRow(i++);
  				Cell cell = row.createCell(0, CellType.STRING);
  				cell.setCellValue(dto.getConstructionCode() != null ? dto.getConstructionCode() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(1, CellType.STRING);
  				cell.setCellValue((dto.getStationCode() != null) ? dto.getStationCode() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(2, CellType.STRING);
  				cell.setCellValue((dto.getHdContractCode() != null) ? dto.getHdContractCode() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(3, CellType.STRING);
  				cell.setCellValue((dto.getConstructionTypeName() != null) ? dto.getConstructionTypeName() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(6, CellType.STRING);
  				cell.setCellValue((dto.getDnqtElectricalProcedures() != null) ? dto.getDnqtElectricalProcedures() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(7, CellType.STRING);
  				cell.setCellValue((dto.getDnqtPullCableLabor() != null) ? dto.getDnqtPullCableLabor() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(8, CellType.STRING);
  				cell.setCellValue((dto.getDnqtCostMaterial() != null) ? dto.getDnqtCostMaterial() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(9, CellType.STRING);
  				cell.setCellValue((dto.getDnqtCostHshc() != null) ? dto.getDnqtCostHshc() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(10, CellType.STRING);
  				cell.setCellValue((dto.getDnqtCostTransportWarehouse() != null) ? dto.getDnqtCostTransportWarehouse() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(11, CellType.STRING);
  				cell.setCellValue((dto.getDnqtCostOrther() != null) ? dto.getDnqtCostOrther() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(12, CellType.STRING);
  				cell.setCellValue((dto.getDnqtSalaryCableOrther() != null) ? dto.getDnqtSalaryCableOrther() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(13, CellType.STRING);
  				cell.setCellValue((dto.getDnqtWeldingSalary() != null) ? dto.getDnqtWeldingSalary() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(14, CellType.STRING);
  				cell.setCellValue((dto.getDnqtVat() != null) ? dto.getDnqtVat() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(15, CellType.STRING);
  				cell.setCellValue((dto.getDnqtTotalMoney() != null) ? dto.getDnqtTotalMoney() : 0D);
  				cell.setCellStyle(style);
  				
//  				cell = row.createCell(15, CellType.STRING);
//  				cell.setCellValue((dto.getDnqtTotalMoney() != null) ? dto.getDnqtTotalMoney() : 0D);
//  				cell.setCellStyle(style);
  				
  			}
  		}
		FileOutputStream fileOut = new FileOutputStream(file);
		// write this workbook to an Outputstream.
		wb.write(fileOut);
		wb.close();
		fileOut.flush();
		fileOut.close();
		String fileName = file.getName();
		int lastIndex = fileName.lastIndexOf(File.separatorChar);
		String fileNameReturn = fileName.substring(lastIndex + 1);

		return Response.ok((Object) file)
				.header("Content-Disposition", "attachment; filename=\"" + fileNameReturn + "\"").build();
	}
	
	public String convertDate(Date date) {
		Format formatter = new SimpleDateFormat("dd/MM/yyyy");
		String s = formatter.format(date);
		return s;
	}
	@Override
	public Response exportFile(HttpServletRequest request) throws Exception {
		String filePath = UEncrypt.decryptFileUploadPath(request.getQueryString());
		File file = new File(filePath);
		InputStream ExcelFileToRead = new FileInputStream(filePath);

		if (!file.exists()) {
			file = new File(filePath);
			if (!file.exists()) {
				// logger.warn("File {} is not found", fileName);
				return Response.status(Response.Status.BAD_REQUEST).build();
			}

		}

		

		// get data for sheet 1
		List<ManageDataOutsideOsDTO> constructionTypeLst = manageDataOutsideOsBusinessImpl.doSearchOS(manaOs).getData();
		XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
		XSSFSheet sheet = wb.getSheetAt(0);
		// write susGroup data to excel file
//		XSSFSheet sheet = workbook.getSheetAt(0);
  		if ((constructionTypeLst != null && !constructionTypeLst.isEmpty())) { // huy-edit
  			

  			CellStyle style = wb.createCellStyle(); // Create new style
  			style.setWrapText(true); // Set wordwrap
  			int i = 4;
  			for (ManageDataOutsideOsDTO dto : constructionTypeLst) {
  				
  				
  				Row row = sheet.createRow(i++);
  				Cell cell = row.createCell(0, CellType.STRING);
  				cell.setCellValue(i - 4);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(1, CellType.STRING);
  				cell.setCellValue((dto.getHdSignDate() != null) ? convertDate(dto.getHdSignDate()) : null);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(2, CellType.STRING);
  				cell.setCellValue((dto.getHdContractCode() != null) ? dto.getHdContractCode() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(3, CellType.STRING);
  				cell.setCellValue((dto.getHdContractValue() != null) ? dto.getHdContractValue() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(4, CellType.STRING);
  				cell.setCellValue((dto.getHdPerformDay() != null) ? dto.getHdPerformDay() : 0L);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(5, CellType.STRING);
  				cell.setCellValue((dto.getProvinceCode() != null) ? dto.getProvinceCode() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(6, CellType.STRING);
  				cell.setCellValue((dto.getConstructionCode() != null) ? dto.getConstructionCode() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(7, CellType.STRING);
  				cell.setCellValue((dto.getStationCode() != null) ? dto.getStationCode() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(8, CellType.STRING);
  				cell.setCellValue((dto.getConstructionTypeName() != null) ? dto.getConstructionTypeName() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(9, CellType.STRING);
  				cell.setCellValue((dto.getContent() != null) ? dto.getContent() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(10, CellType.STRING);
  				cell.setCellValue((dto.getCapitalNtdName() != null) ? dto.getCapitalNtdName() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(11, CellType.STRING);
  				cell.setCellValue((dto.getKhtcSalary() != null) ? dto.getKhtcSalary() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(12, CellType.STRING);
  				cell.setCellValue((dto.getKhtcLaborOutsource() != null) ? dto.getKhtcLaborOutsource() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(13, CellType.STRING);
  				cell.setCellValue((dto.getKhtcCostMaterial() != null) ? dto.getKhtcCostMaterial() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(14, CellType.STRING);
  				cell.setCellValue((dto.getKhtcCostHshc() != null) ? dto.getKhtcCostHshc() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(15, CellType.STRING);
  				cell.setCellValue((dto.getKhtcCostTransport() != null) ? dto.getKhtcCostTransport() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(16, CellType.STRING);
  				cell.setCellValue((dto.getKhtcCostOrther() != null) ? dto.getKhtcCostOrther() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(17, CellType.STRING);
  				cell.setCellValue((dto.getKhtcDeploymentMonth() != null) ? dto.getKhtcDeploymentMonth() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(18, CellType.STRING);
  				cell.setCellValue((dto.getKhtcTotalMoney() != null) ? dto.getKhtcTotalMoney() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(19, CellType.STRING);
  				cell.setCellValue((dto.getKhtcEffective() != null) ? dto.getKhtcEffective() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(20, CellType.STRING);
  				cell.setCellValue((dto.getKhtcDescription() != null) ? dto.getKhtcDescription() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(21, CellType.STRING);
  				cell.setCellValue((dto.getTuAdvanceDate() != null) ? convertDate(dto.getTuAdvanceDate()) : null);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(22, CellType.STRING);
  				cell.setCellValue((dto.getTuLabor() != null) ? dto.getTuLabor() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(23, CellType.STRING);
  				cell.setCellValue((dto.getTuMaterial() != null) ? dto.getTuMaterial() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(24, CellType.STRING);
  				cell.setCellValue((dto.getTuHshc() != null) ? dto.getTuHshc() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(25, CellType.STRING);
  				cell.setCellValue((dto.getTuCostTransport() != null) ? dto.getTuCostTransport() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(26, CellType.STRING);
  				cell.setCellValue((dto.getTuCostOrther() != null) ? dto.getTuCostOrther() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(27, CellType.STRING);
  				cell.setCellValue((dto.getVtaSynchronizeDate() != null) ? convertDate(dto.getVtaSynchronizeDate()) : null);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(28, CellType.STRING);
  				cell.setCellValue((dto.getVtaValue() != null) ? dto.getVtaValue() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(29, CellType.STRING);
  				cell.setCellValue((dto.getGtslQuantityValue() != null) ? dto.getGtslQuantityValue() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(30, CellType.STRING);
  				cell.setCellValue((dto.getHttcTdt() != null) ? "x" : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(31, CellType.STRING);
  				cell.setCellValue((dto.getHttcTctt() != null) ? "x" : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(32, CellType.STRING);
  				cell.setCellValue((dto.getHttcKn() != null) ? "x" : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(33, CellType.STRING);
  				cell.setCellValue((dto.getTttcStartDate() != null) ? convertDate(dto.getTttcStartDate()) : null);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(34, CellType.STRING);
  				cell.setCellValue((dto.getTttcEndDate() != null) ? convertDate(dto.getTttcEndDate()) : null);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(35, CellType.STRING);
  				cell.setCellValue((dto.getTttcVuong() != null) ? dto.getTttcVuong() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(36, CellType.STRING);
  				cell.setCellValue((dto.getTttcClose() != null) ? dto.getTttcClose() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(37, CellType.STRING);
  				cell.setCellValue((dto.getGtslCompleteExpectedDate() != null) ? convertDate(dto.getGtslCompleteExpectedDate()) : null);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(38, CellType.STRING);
  				cell.setCellValue((dto.getGtslDescription() != null) ? dto.getGtslDescription() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(39, CellType.STRING);
  				cell.setCellValue((dto.getTdntHshcStartDate() != null) ? convertDate(dto.getTdntHshcStartDate()) : null);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(40, CellType.STRING);
  				cell.setCellValue((dto.getTdntAcceptanceStartDate() != null) ? convertDate(dto.getTdntAcceptanceStartDate()) : null);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(41, CellType.STRING);
  				cell.setCellValue((dto.getTdntKthtExpertiseDate() != null) ? convertDate(dto.getTdntKthtExpertiseDate()) : null);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(42, CellType.STRING);
  				cell.setCellValue((dto.getTdnt4AControlStartDate() != null) ? convertDate(dto.getTdnt4AControlStartDate()) : null);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(43, CellType.STRING);
  				cell.setCellValue((dto.getTdntSignProvinceDate() != null) ?convertDate( dto.getTdntSignProvinceDate()) : null);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(44, CellType.STRING);
  				cell.setCellValue((dto.getTdntSendTctDate() != null) ? convertDate(dto.getTdntSendTctDate()) : null);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(45, CellType.STRING);
  				cell.setCellValue((dto.getTdntCompleteExpectedDate() != null) ? convertDate(dto.getTdntCompleteExpectedDate()) : null);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(46, CellType.STRING);
  				cell.setCellValue((dto.getTdntVuongDate() != null) ? convertDate(dto.getTdntVuongDate()) : null);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(47, CellType.STRING);
  				cell.setCellValue((dto.getTdntVuongReason() != null) ? dto.getTdntVuongReason() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(48, CellType.STRING);
  				cell.setCellValue((dto.getDnqtQtCdtNotVat() != null) ? dto.getDnqtQtCdtNotVat() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(49, CellType.STRING);
  				cell.setCellValue((dto.getDnqtQtCdtVat() != null) ? dto.getDnqtQtCdtVat() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(50, CellType.STRING);
  				cell.setCellValue((dto.getDnqtElectricalProcedures() != null) ? dto.getDnqtElectricalProcedures() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(51, CellType.STRING);
  				cell.setCellValue((dto.getDnqtPullCableLabor() != null) ? dto.getDnqtPullCableLabor() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(52, CellType.STRING);
  				cell.setCellValue((dto.getDnqtCostMaterial() != null) ? dto.getDnqtCostMaterial() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(53, CellType.STRING);
  				cell.setCellValue((dto.getDnqtCostHshc() != null) ? dto.getDnqtCostHshc() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(54, CellType.STRING);
  				cell.setCellValue((dto.getDnqtCostTransportWarehouse() != null) ? dto.getDnqtCostTransportWarehouse() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(55, CellType.STRING);
  				cell.setCellValue((dto.getDnqtCostOrther() != null) ? dto.getDnqtCostOrther() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(56, CellType.STRING);
  				cell.setCellValue((dto.getDnqtSalaryCableOrther() != null) ? dto.getDnqtSalaryCableOrther() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(57, CellType.STRING);
  				cell.setCellValue((dto.getDnqtWeldingSalary() != null) ? dto.getDnqtWeldingSalary() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(58, CellType.STRING);
  				cell.setCellValue((dto.getDnqtVat() != null) ? dto.getDnqtVat() : 0D);
  				cell.setCellStyle(style);

  				cell = row.createCell(59, CellType.STRING);
  				cell.setCellValue((dto.getDnqtTotalMoney() != null) ? dto.getDnqtTotalMoney() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(60, CellType.STRING);
  				cell.setCellValue((dto.getGttdHshcHardDate() != null) ? convertDate(dto.getGttdHshcHardDate()) : null);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(61, CellType.STRING);
  				cell.setCellValue((dto.getGttdCompleteExpertiseDate() != null) ? convertDate(dto.getGttdCompleteExpertiseDate()) : null);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(62, CellType.STRING);
  				cell.setCellValue((dto.getGttdElectricalProcedures() != null) ? dto.getGttdElectricalProcedures() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(63, CellType.STRING);
  				cell.setCellValue((dto.getGttdPullCableLabor() != null) ? dto.getGttdPullCableLabor() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(64, CellType.STRING);
  				cell.setCellValue((dto.getGttdCostMaterial() != null) ? dto.getGttdCostMaterial() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(65, CellType.STRING);
  				cell.setCellValue((dto.getGttdCostHshc() != null) ? dto.getGttdCostHshc() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(66, CellType.STRING);
  				cell.setCellValue((dto.getGttdCostTransportWarehouse() != null) ? dto.getGttdCostTransportWarehouse() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(67, CellType.STRING);
  				cell.setCellValue((dto.getGttdCostOrther() != null) ? dto.getGttdCostOrther() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(68, CellType.STRING);
  				cell.setCellValue((dto.getGttdSalaryCableOrther() != null) ? dto.getGttdSalaryCableOrther() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(69, CellType.STRING);
  				cell.setCellValue((dto.getGttdWeldingSalary() != null) ? dto.getGttdWeldingSalary() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(70, CellType.STRING);
  				cell.setCellValue((dto.getGttdVat() != null) ? dto.getGttdVat() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(71, CellType.STRING);
  				cell.setCellValue((dto.getGttdTotalMoney() != null) ? dto.getGttdTotalMoney() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(72, CellType.STRING);
  				cell.setCellValue((dto.getGttdGttdPtk() != null) ? dto.getGttdGttdPtk() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(73, CellType.STRING);
  				cell.setCellValue((dto.getGttdHshcMonth() != null) ? dto.getGttdHshcMonth() : null);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(74, CellType.STRING);
  				cell.setCellValue((dto.getGttdSalaryMonth() != null) ? dto.getGttdSalaryMonth() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(75, CellType.STRING);
  				cell.setCellValue((dto.getGttdSalaryReal() != null) ? dto.getGttdSalaryReal() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(76, CellType.STRING);
  				cell.setCellValue((dto.getGttdHshcError() != null) ? dto.getGttdHshcError() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(77, CellType.STRING);
  				cell.setCellValue((dto.getGttdErrorReason() != null) ? dto.getGttdErrorReason() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(78, CellType.STRING);
  				cell.setCellValue((dto.getQtdnSuggestionsDate() != null) ? convertDate(dto.getQtdnSuggestionsDate()) : null);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(79, CellType.STRING);
  				cell.setCellValue((dto.getQtdnValue() != null) ? dto.getQtdnValue() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(80, CellType.STRING);
  				cell.setCellValue((dto.getQtdnVtnetDate() != null) ?convertDate( dto.getQtdnVtnetDate()) : null);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(81, CellType.STRING);
  				cell.setCellValue((dto.getQtdnDescription() != null) ? dto.getQtdnDescription() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(82, CellType.STRING);
  				cell.setCellValue((dto.getQttdExpertiseEmployee() != null) ? dto.getQttdExpertiseEmployee() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(83, CellType.STRING);
  				cell.setCellValue((dto.getQttdExpertiseCompleteDate() != null) ? convertDate(dto.getQttdExpertiseCompleteDate()) : null);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(84, CellType.STRING);
  				cell.setCellValue((dto.getQttdValue() != null) ? dto.getQttdValue() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(85, CellType.STRING);
  				cell.setCellValue((dto.getQttdDescription() != null) ? dto.getQttdDescription() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(86, CellType.STRING);
  				cell.setCellValue((dto.getXhdPtcDate() != null) ? convertDate(dto.getXhdPtcDate()) : null);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(87, CellType.STRING);
  				cell.setCellValue((dto.getXhdXhdDate() != null) ? convertDate(dto.getXhdXhdDate()) : null);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(88, CellType.STRING);
  				cell.setCellValue((dto.getXhdSoHd() != null) ? dto.getXhdSoHd() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(89, CellType.STRING);
  				cell.setCellValue((dto.getXhdRevenueMonth() != null) ? dto.getXhdRevenueMonth() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(90, CellType.STRING);
  				cell.setCellValue((dto.getXhdDescription() != null) ? dto.getXhdDescription() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(91, CellType.STRING);
  				cell.setCellValue((dto.getTlSignDate() != null) ? convertDate(dto.getTlSignDate()) : null);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(92, CellType.STRING);
  				cell.setCellValue((dto.getTlValue() != null) ? dto.getTlValue() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(93, CellType.STRING);
  				cell.setCellValue((dto.getTlDescription() != null) ? dto.getTlDescription() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(94, CellType.STRING);
  				cell.setCellValue((dto.getTlDifferenceQuantity() != null) ? dto.getTlDifferenceQuantity() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(95, CellType.STRING);
  				cell.setCellValue((dto.getTlRate() != null) ? dto.getTlRate() : 0D);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(96, CellType.STRING);
  				cell.setCellValue((dto.getQtncPhtDate() != null) ? convertDate(dto.getQtncPhtDate()) : null);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(97, CellType.STRING);
  				cell.setCellValue((dto.getQtncPtcDate() != null) ? convertDate(dto.getQtncPtcDate()) : null);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(98, CellType.STRING);
  				cell.setCellValue((dto.getQtncVtaAccountDate() != null) ? convertDate(dto.getQtncVtaAccountDate()) : null);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(99, CellType.STRING);
  				cell.setCellValue((dto.getQtncTakeMoneyDate() != null) ? convertDate(dto.getQtncTakeMoneyDate()) : null);
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(100, CellType.STRING);
  				cell.setCellValue((dto.getQtncVuong() != null) ? dto.getQtncVuong() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(101, CellType.STRING);
  				cell.setCellValue((dto.getDescription() != null) ? dto.getDescription() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(102, CellType.STRING);
  				cell.setCellValue((dto.getStatusName() != null) ? dto.getStatusName() : "");
  				cell.setCellStyle(style);
  				
  			}
  		}
		FileOutputStream fileOut = new FileOutputStream(file);
		// write this workbook to an Outputstream.
		wb.write(fileOut);
		wb.close();
		fileOut.flush();
		fileOut.close();
		String fileName = file.getName();
		int lastIndex = fileName.lastIndexOf(File.separatorChar);
		String fileNameReturn = fileName.substring(lastIndex + 1);

		return Response.ok((Object) file)
				.header("Content-Disposition", "attachment; filename=\"" + fileNameReturn + "\"").build();
	}
	
	
	@Override
	public Response getExcelTemplateExpertiseProposalCDT(HttpServletRequest request) throws Exception {
		String filePath = UEncrypt.decryptFileUploadPath(request.getQueryString());
		File file = new File(filePath);
		InputStream ExcelFileToRead = new FileInputStream(filePath);

		if (!file.exists()) {
			file = new File(filePath);
			if (!file.exists()) {
				// logger.warn("File {} is not found", fileName);
				return Response.status(Response.Status.BAD_REQUEST).build();
			}

		}

		

		// get data for sheet 1
//		manaOs.setStatus("4");
		List<ManageDataOutsideOsDTO> constructionTypeLst = manageDataOutsideOsBusinessImpl.doSearchOS(manaOs).getData();
		XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
		XSSFSheet sheet = wb.getSheetAt(0);
		// write susGroup data to excel file
//		XSSFSheet sheet = workbook.getSheetAt(0);
  		if ((constructionTypeLst != null && !constructionTypeLst.isEmpty())) { // huy-edit
  			

  			CellStyle style = wb.createCellStyle(); // Create new style
  			style.setWrapText(true); // Set wordwrap
  			int i = 4;
  			for (ManageDataOutsideOsDTO dto : constructionTypeLst) {
  				Row row = sheet.createRow(i++);
  				Cell cell = row.createCell(0, CellType.STRING);
  				cell.setCellValue(dto.getConstructionCode() != null ? dto.getConstructionCode() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(1, CellType.STRING);
  				cell.setCellValue((dto.getStationCode() != null) ? dto.getStationCode() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(2, CellType.STRING);
  				cell.setCellValue((dto.getHdContractCode() != null) ? dto.getHdContractCode() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(3, CellType.STRING);
  				cell.setCellValue((dto.getConstructionTypeName() != null) ? dto.getConstructionTypeName() : "");
  				cell.setCellStyle(style);
  			}
  		}
		FileOutputStream fileOut = new FileOutputStream(file);
		// write this workbook to an Outputstream.
		wb.write(fileOut);
		wb.close();
		fileOut.flush();
		fileOut.close();
		String fileName = file.getName();
		int lastIndex = fileName.lastIndexOf(File.separatorChar);
		String fileNameReturn = fileName.substring(lastIndex + 1);

		return Response.ok((Object) file)
				.header("Content-Disposition", "attachment; filename=\"" + fileNameReturn + "\"").build();
	}
	@Override
	public Response getExcelTemplateInvoice(HttpServletRequest request) throws Exception {
		String filePath = UEncrypt.decryptFileUploadPath(request.getQueryString());
		File file = new File(filePath);
		InputStream ExcelFileToRead = new FileInputStream(filePath);

		if (!file.exists()) {
			file = new File(filePath);
			if (!file.exists()) {
				// logger.warn("File {} is not found", fileName);
				return Response.status(Response.Status.BAD_REQUEST).build();
			}

		}
		// get data for sheet 1
//		manaOs.setStatus("5");
		List<ManageDataOutsideOsDTO> constructionTypeLst = manageDataOutsideOsBusinessImpl.doSearchOS(manaOs).getData();
		XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
		XSSFSheet sheet = wb.getSheetAt(0);
		// write susGroup data to excel file
//		XSSFSheet sheet = workbook.getSheetAt(0);
  		if ((constructionTypeLst != null && !constructionTypeLst.isEmpty())) { // huy-edit
  			CellStyle style = wb.createCellStyle(); // Create new style
  			style.setWrapText(true); // Set wordwrap
  			int i = 4;
  			for (ManageDataOutsideOsDTO dto : constructionTypeLst) {
  				Row row = sheet.createRow(i++);
  				Cell cell = row.createCell(0, CellType.STRING);
  				cell.setCellValue(dto.getConstructionCode() != null ? dto.getConstructionCode() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(1, CellType.STRING);
  				cell.setCellValue((dto.getStationCode() != null) ? dto.getStationCode() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(2, CellType.STRING);
  				cell.setCellValue((dto.getHdContractCode() != null) ? dto.getHdContractCode() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(3, CellType.STRING);
  				cell.setCellValue((dto.getConstructionTypeName() != null) ? dto.getConstructionTypeName() : "");
  				cell.setCellStyle(style);
  			}
  		}
		FileOutputStream fileOut = new FileOutputStream(file);
		// write this workbook to an Outputstream.
		wb.write(fileOut);
		wb.close();
		fileOut.flush();
		fileOut.close();
		String fileName = file.getName();
		int lastIndex = fileName.lastIndexOf(File.separatorChar);
		String fileNameReturn = fileName.substring(lastIndex + 1);

		return Response.ok((Object) file)
				.header("Content-Disposition", "attachment; filename=\"" + fileNameReturn + "\"").build();
	}
	
	@Override
	public Response getExcelTemplateLiquidation(HttpServletRequest request) throws Exception {
		String filePath = UEncrypt.decryptFileUploadPath(request.getQueryString());
		File file = new File(filePath);
		InputStream ExcelFileToRead = new FileInputStream(filePath);

		if (!file.exists()) {
			file = new File(filePath);
			if (!file.exists()) {
				// logger.warn("File {} is not found", fileName);
				return Response.status(Response.Status.BAD_REQUEST).build();
			}

		}
		// get data for sheet 1
//		manaOs.setStatus("6");
		List<ManageDataOutsideOsDTO> constructionTypeLst = manageDataOutsideOsBusinessImpl.doSearchOS(manaOs).getData();
		XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
		XSSFSheet sheet = wb.getSheetAt(0);
		// write susGroup data to excel file
//		XSSFSheet sheet = workbook.getSheetAt(0);
  		if ((constructionTypeLst != null && !constructionTypeLst.isEmpty())) { // huy-edit
  			CellStyle style = wb.createCellStyle(); // Create new style
  			style.setWrapText(true); // Set wordwrap
  			int i = 4;
  			for (ManageDataOutsideOsDTO dto : constructionTypeLst) {
  				Row row = sheet.createRow(i++);
  				Cell cell = row.createCell(0, CellType.STRING);
  				cell.setCellValue(dto.getConstructionCode() != null ? dto.getConstructionCode() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(1, CellType.STRING);
  				cell.setCellValue((dto.getStationCode() != null) ? dto.getStationCode() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(2, CellType.STRING);
  				cell.setCellValue((dto.getHdContractCode() != null) ? dto.getHdContractCode() : "");
  				cell.setCellStyle(style);
  				
  				cell = row.createCell(3, CellType.STRING);
  				cell.setCellValue((dto.getConstructionTypeName() != null) ? dto.getConstructionTypeName() : "");
  				cell.setCellStyle(style);
  			}
  		}
		FileOutputStream fileOut = new FileOutputStream(file);
		// write this workbook to an Outputstream.
		wb.write(fileOut);
		wb.close();
		fileOut.flush();
		fileOut.close();
		String fileName = file.getName();
		int lastIndex = fileName.lastIndexOf(File.separatorChar);
		String fileNameReturn = fileName.substring(lastIndex + 1);

		return Response.ok((Object) file)
				.header("Content-Disposition", "attachment; filename=\"" + fileNameReturn + "\"").build();
	}
	@Override
	public Response downloadFileTempATTT(HttpServletRequest request)
			throws Exception {
		String fileName = UEncrypt.decryptFileUploadPath(request
				.getQueryString());
		File file = new File(folderTemp + File.separatorChar + fileName);
		if (!file.exists()) {
			file = new File(folderUpload + File.separatorChar + fileName);
			if (!file.exists()) {
//				log.warn("File {} is not found", fileName);
				return Response.status(Response.Status.BAD_REQUEST).build();
			}

		}
		int lastIndex = fileName.lastIndexOf(File.separatorChar);
		String fileNameReturn = fileName.substring(lastIndex + 1);

		return Response
				.ok((Object) file)
				.header("Content-Disposition",
						"attachment; filename=\"" + fileNameReturn + "\"")
				.build();
	}
	//tatph - end 13/11/2019
	
	//Huypq-20191114
	@Override
    public Response exportTemplateLapTienDo(ManageDataOutsideOsDTO obj) throws Exception {
        try {
            String strReturn = manageDataOutsideOsBusinessImpl.exportTemplateLapTienDo(obj);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }
	
	@Override
    public Response exportTemplateDNQT(ManageDataOutsideOsDTO obj) throws Exception {
        try {
            String strReturn = manageDataOutsideOsBusinessImpl.exportTemplateDNQT(obj);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }
	
	@Override
    public Response exportTemplateQuyetToanNC(ManageDataOutsideOsDTO obj) throws Exception {
        try {
            String strReturn = manageDataOutsideOsBusinessImpl.exportTemplateQuyetToanNC(obj);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }
	
	@Override
    public Response exportTemplateConsContract(ManageDataOutsideOsDTO obj) throws Exception {
        try {
            String strReturn = manageDataOutsideOsBusinessImpl.exportTemplateConsContract(obj);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }
	
	@Override
	public Response importConsContract(Attachment attachments, HttpServletRequest request) throws Exception {
		String folderParam = UString.getSafeFileName(request.getParameter("folder"));
		
		String filePathReturn;
		String filePath;
		if (UString.isNullOrWhitespace(folderParam)) {
			folderParam = defaultSubFolderUpload;
		} else {
			if (!isFolderAllowFolderSave(folderParam)) {
				throw new BusinessException("folder khong nam trong white list: folderParam=" + folderParam);
			}
		}
		DataHandler dataHandler = attachments.getDataHandler();
		MultivaluedMap<String, String> multivaluedMap = attachments.getHeaders();
		String fileName = UFile.getFileName(multivaluedMap);
		if (!isExtendAllowSave(fileName)) {
			throw new BusinessException("File extension khong nam trong list duoc up load, file_name:" + fileName);
		}
//		 write & upload file to server
		try (InputStream inputStream = dataHandler.getInputStream();) {
			filePath = UFile.writeToFileTempServerATTT2(inputStream, fileName, folderParam, folder2Upload);
			filePathReturn = UEncrypt.encryptFileUploadPath(filePath);
		} catch (Exception ex) {
			throw new BusinessException("Loi khi save file", ex);
		}

		try {
			List<ManageDataOutsideOsDTO> result = manageDataOutsideOsBusinessImpl.importConsContract(folder2Upload + filePath);
			if(result != null && !result.isEmpty() && (result.get(0).getErrorList()==null || result.get(0).getErrorList().size() == 0)){
				manageDataOutsideOsBusinessImpl.saveListNew(result, request);
				return Response.ok(result).build();
			}else if (result == null || result.isEmpty()) {
				return Response.ok().entity(Response.Status.NO_CONTENT).build(); }
			else{
				return Response.ok(result).build();
			}

		} catch (IllegalArgumentException e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}

	}
	
	@Override
	public Response importSchedule(Attachment attachments, HttpServletRequest request) throws Exception {
		String folderParam = UString.getSafeFileName(request.getParameter("folder"));
		
		String filePathReturn;
		String filePath;
		if (UString.isNullOrWhitespace(folderParam)) {
			folderParam = defaultSubFolderUpload;
		} else {
			if (!isFolderAllowFolderSave(folderParam)) {
				throw new BusinessException("folder khong nam trong white list: folderParam=" + folderParam);
			}
		}
		DataHandler dataHandler = attachments.getDataHandler();
		MultivaluedMap<String, String> multivaluedMap = attachments.getHeaders();
		String fileName = UFile.getFileName(multivaluedMap);
		if (!isExtendAllowSave(fileName)) {
			throw new BusinessException("File extension khong nam trong list duoc up load, file_name:" + fileName);
		}
//		 write & upload file to server
		try (InputStream inputStream = dataHandler.getInputStream();) {
			filePath = UFile.writeToFileTempServerATTT2(inputStream, fileName, folderParam, folder2Upload);
			filePathReturn = UEncrypt.encryptFileUploadPath(filePath);
		} catch (Exception ex) {
			throw new BusinessException("Loi khi save file", ex);
		}

		try {
			List<ManageDataOutsideOsDTO> result = manageDataOutsideOsBusinessImpl.importSchedule(folder2Upload + filePath);
			if(result != null && !result.isEmpty() && (result.get(0).getErrorList()==null || result.get(0).getErrorList().size() == 0)){
				manageDataOutsideOsBusinessImpl.updateListNew(result, request);
				return Response.ok(result).build();
			}else if (result == null || result.isEmpty()) {
				return Response.ok().entity(Response.Status.NO_CONTENT).build(); }
			else{
				return Response.ok(result).build();
			}

		} catch (IllegalArgumentException e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}

	}
	
	@Override
	public Response importSettlementProposal(Attachment attachments, HttpServletRequest request) throws Exception {
		String folderParam = UString.getSafeFileName(request.getParameter("folder"));
		
		String filePathReturn;
		String filePath;
		if (UString.isNullOrWhitespace(folderParam)) {
			folderParam = defaultSubFolderUpload;
		} else {
			if (!isFolderAllowFolderSave(folderParam)) {
				throw new BusinessException("folder khong nam trong white list: folderParam=" + folderParam);
			}
		}
		DataHandler dataHandler = attachments.getDataHandler();
		MultivaluedMap<String, String> multivaluedMap = attachments.getHeaders();
		String fileName = UFile.getFileName(multivaluedMap);
		if (!isExtendAllowSave(fileName)) {
			throw new BusinessException("File extension khong nam trong list duoc up load, file_name:" + fileName);
		}
//		 write & upload file to server
		try (InputStream inputStream = dataHandler.getInputStream();) {
			filePath = UFile.writeToFileTempServerATTT2(inputStream, fileName, folderParam, folder2Upload);
			filePathReturn = UEncrypt.encryptFileUploadPath(filePath);
		} catch (Exception ex) {
			throw new BusinessException("Loi khi save file", ex);
		}

		try {
			List<ManageDataOutsideOsDTO> result = manageDataOutsideOsBusinessImpl.importSettlementProposal(folder2Upload + filePath);
			if(result != null && !result.isEmpty() && (result.get(0).getErrorList()==null || result.get(0).getErrorList().size() == 0)){
				manageDataOutsideOsBusinessImpl.updateListSettle(result, request);
				return Response.ok(result).build();
			}else if (result == null || result.isEmpty()) {
				return Response.ok().entity(Response.Status.NO_CONTENT).build(); }
			else{
				return Response.ok(result).build();
			}

		} catch (IllegalArgumentException e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}

	}
	
	@Override
	public Response importProposalLabor(Attachment attachments, HttpServletRequest request) throws Exception {
		String folderParam = UString.getSafeFileName(request.getParameter("folder"));
		
		String filePathReturn;
		String filePath;
		if (UString.isNullOrWhitespace(folderParam)) {
			folderParam = defaultSubFolderUpload;
		} else {
			if (!isFolderAllowFolderSave(folderParam)) {
				throw new BusinessException("folder khong nam trong white list: folderParam=" + folderParam);
			}
		}
		DataHandler dataHandler = attachments.getDataHandler();
		MultivaluedMap<String, String> multivaluedMap = attachments.getHeaders();
		String fileName = UFile.getFileName(multivaluedMap);
		if (!isExtendAllowSave(fileName)) {
			throw new BusinessException("File extension khong nam trong list duoc up load, file_name:" + fileName);
		}
//		 write & upload file to server
		try (InputStream inputStream = dataHandler.getInputStream();) {
			filePath = UFile.writeToFileTempServerATTT2(inputStream, fileName, folderParam, folder2Upload);
			filePathReturn = UEncrypt.encryptFileUploadPath(filePath);
		} catch (Exception ex) {
			throw new BusinessException("Loi khi save file", ex);
		}

		try {
			List<ManageDataOutsideOsDTO> result = manageDataOutsideOsBusinessImpl.importProposalLabor(folder2Upload + filePath);
			if(result != null && !result.isEmpty() && (result.get(0).getErrorList()==null || result.get(0).getErrorList().size() == 0)){
				manageDataOutsideOsBusinessImpl.updateListLabor(result, request);
				return Response.ok(result).build();
			}else if (result == null || result.isEmpty()) {
				return Response.ok().entity(Response.Status.NO_CONTENT).build(); }
			else{
				return Response.ok(result).build();
			}

		} catch (IllegalArgumentException e) {
			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
		}

	}
	//Huy-end
}
