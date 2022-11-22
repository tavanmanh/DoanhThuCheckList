package com.viettel.coms.business;
 
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;
import com.viettel.coms.bo.OfferHtctBO;
import com.viettel.coms.dao.OfferHtctDAO;
import com.viettel.coms.dto.ExcelErrorDTO;
import com.viettel.coms.dto.OfferHtctDTO;
import com.viettel.coms.dto.WaccHtctDTO;
import com.viettel.coms.utils.ValidateUtils;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.service.base.business.BaseFWBusinessImpl;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("offerHtctBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OfferHtctBusinessImpl extends BaseFWBusinessImpl<OfferHtctDAO,OfferHtctDTO, OfferHtctBO> implements OfferHtctBusiness {

    @Autowired
    private OfferHtctDAO offerHtctDAO;
     
    public OfferHtctBusinessImpl() {
        tModel = new OfferHtctBO();
        tDAO = offerHtctDAO;
    }
    public List<OfferHtctDTO> getData8(OfferHtctDTO obj){
    	return offerHtctDAO.getData8(obj);
    }
    @Value("${folder_upload2}")
    private String folder2Upload;
    @Value("${default_sub_folder_upload}")
    private String defaultSubFolderUpload;
	public String downloadTemplateChaoGia(OfferHtctDTO obj) throws Exception {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		String filePath = classLoader.getResource("../"+"doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Import_Chao_Gia.xlsx"));
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		file.close();
		Calendar cal = Calendar.getInstance();
		String uploadPath = folder2Upload + File.separator + UFile.getSafeFileName(defaultSubFolderUpload)
			+ File.separator + cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH) + 1) 
			+ File.separator + cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
		String uploadPathReturn = File.separator + UFile.getSafeFileName(defaultSubFolderUpload) + File.separator
				+ cal.get(Calendar.YEAR) + File.separator + (cal.get(Calendar.MONTH)+1) + File.separator 
				+ cal.get(Calendar.DATE) + File.separator + cal.get(Calendar.MILLISECOND);
		File udir = new File(uploadPath);
		if(!udir.exists()) {
			udir.mkdir();
		}
		OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "Import_Chao_Gia.xlsx");
		workbook.write(outFile);
		workbook.close();
		outFile.close();
		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "Import_Chao_Gia.xlsx");
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
	public List<OfferHtctDTO> importChaoGia(String fileInput) {
		List<OfferHtctDTO> workLst = Lists.newArrayList();
		List<ExcelErrorDTO> errorLst = new ArrayList<>();
		List<OfferHtctDTO> lstOffer = offerHtctDAO.getData8(new OfferHtctDTO());
		HashMap<String, OfferHtctDTO> mapDB = new HashMap<>();
		HashMap<String, String> mapExcel = new HashMap<>();
		List<String> lstCate = offerHtctDAO.getLstCate();
		List<String> lstUnit = new ArrayList<>(Arrays.asList("VNĐ","VNĐ/năm"));
		
		for(OfferHtctDTO offer : lstOffer) {
			mapDB.put(offer.getCategoryOffer().trim().toUpperCase(), offer);
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
					String categoryOffer = formatter.formatCellValue(row.getCell(1));
					String symbol = formatter.formatCellValue(row.getCell(2));
					String unit = formatter.formatCellValue(row.getCell(3));
					OfferHtctDTO dto = new OfferHtctDTO();
					//Validate categoryOffer
					if(validateString(categoryOffer)) {
						if(lstCate.contains(categoryOffer)) {
							//Check duplicate in file import
							if(mapExcel.size() == 0) {
								dto.setCategoryOffer(categoryOffer);
								if(mapDB.get(dto.getCategoryOffer().trim().toUpperCase())!= null) {
									dto.setOfferId(mapDB.get(dto.getCategoryOffer().trim().toUpperCase()).getOfferId());
								}
								mapExcel.put(categoryOffer.trim().toUpperCase(), categoryOffer);
							} else {
								if(mapExcel.get(categoryOffer.trim().toUpperCase()) != null) {
									ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "B", "Dữ liệu import có bản ghi với danh mục trùng nhau. Hãy xem lại!");
									errorLst.add(errorDTO);
								} else {
									dto.setCategoryOffer(categoryOffer);
									if(mapDB.get(dto.getCategoryOffer().trim().toUpperCase())!= null) {
										dto.setOfferId(mapDB.get(dto.getCategoryOffer().trim().toUpperCase()).getOfferId());
									}
									mapExcel.put(categoryOffer.trim().toUpperCase(), categoryOffer);
								}
							}
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "B", "Danh mục không tồn tại!");
							errorLst.add(errorDTO);
						}
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "B", " Danh mục không được để trống");
						errorLst.add(errorDTO);
					}
					
					if(validateString(symbol)) {
						try {
							Double symL = Double.parseDouble(symbol);
							dto.setSymbol(symL.toString());
						} catch(Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "C", "Hãy nhập số vào ô ký hiệu");
							errorLst.add(errorDTO);
						}
					}
					if(validateString(unit)) {
						if(lstUnit.contains(unit)) {
							dto.setUnit(unit);
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "D", "ĐVT không tồn tại");
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
				List<OfferHtctDTO> emptyArray = Lists.newArrayList();
				workLst = emptyArray;
				OfferHtctDTO errC = new OfferHtctDTO();
				errC.setErrorList(errorLst);
				errC.setMessageColumn(4); // Column to print error
				errC.setFilePathError(filePathError);
				workLst.add(errC);
			}
			workbook.close();
			return workLst;
		} catch(Exception e) {
			ExcelErrorDTO errorDTO = createError(0, "", e.toString());
			errorLst.add(errorDTO);
			try {
				String filePathError = UEncrypt.encryptFileUploadPath(fileInput); 
			} catch(Exception ex) {
				errorDTO = createError(0, "", ex.toString());
				errorLst.add(errorDTO);
			}
			List<OfferHtctDTO> emptyArray = Lists.newArrayList();
			workLst = emptyArray;
			OfferHtctDTO errorContainer = new OfferHtctDTO();
			errorContainer.setErrorList(errorLst);
			errorContainer.setMessageColumn(4);//Column to print error
			workLst.add(errorContainer);
			return workLst;
		}
	}
	public void saveOffer(OfferHtctDTO dto) {
		offerHtctDAO.saveObject(dto.toModel());
		
	}
	public void updateOffer(String categoryOffer, String symbol, String unit, Long offerId, Date updatedDate,
			Long updateUserId) {
		offerHtctDAO.updateOffer(categoryOffer, symbol, unit, offerId, updatedDate, updateUserId );
		
	}
}
