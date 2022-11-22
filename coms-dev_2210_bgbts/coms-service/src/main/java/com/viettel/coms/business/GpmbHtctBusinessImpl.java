package com.viettel.coms.business;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.viettel.coms.bo.GpmbHtctBO;
import com.viettel.coms.dao.GpmbHtctDAO;
import com.viettel.coms.dto.ExcelErrorDTO;
import com.viettel.coms.dto.GpmbHtctDTO;
import com.viettel.coms.dto.RatioDeliveryHtctDTO;
import com.viettel.coms.utils.ValidateUtils;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.service.base.business.BaseFWBusinessImpl;


@Service("gpmbHtctBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GpmbHtctBusinessImpl extends BaseFWBusinessImpl<GpmbHtctDAO,GpmbHtctDTO, GpmbHtctBO> implements GpmbHtctBusiness {

    @Autowired
    private GpmbHtctDAO gpmbHtctDAO;

    public GpmbHtctBusinessImpl() {
        tModel = new GpmbHtctBO();
        tDAO = gpmbHtctDAO;
    }

    public List<GpmbHtctDTO> getData4(GpmbHtctDTO obj){
    	return gpmbHtctDAO.getData4(obj);
    }


	public GpmbHtctDTO findByProvince(String codeProvince){
		return gpmbHtctDAO.findByProvince(codeProvince);
	}

    @Value("${folder_upload2}")
    private String folder2Upload;
    @Value("${default_sub_folder_upload}")
    private String defaultSubFolderUpload;
	public String downloadTemplateGPMB(GpmbHtctDTO obj) throws Exception {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();
        InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Import_ChiPhi_DauNoi_GPMB.xlsx"));
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
                udir.getAbsolutePath() + File.separator + "Import_ChiPhi_DauNoi_GPMB.xlsx");
        workbook.write(outFile);
		workbook.close();
		outFile.close();
		String path = UEncrypt
				.encryptFileUploadPath(uploadPathReturn + File.separator + "Import_ChiPhi_DauNoi_GPMB.xlsx");
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
	public List<GpmbHtctDTO> importGPMB(String fileInput) {
		List<GpmbHtctDTO> workLst = Lists.newArrayList();
		List<ExcelErrorDTO> errorLst = new ArrayList<>();
		List<GpmbHtctDTO> lstGpmb = gpmbHtctDAO.getData4(new GpmbHtctDTO());
		HashMap<String, GpmbHtctDTO> mapDB = new HashMap<>();
		HashMap<String, String> mapExcel = new HashMap<>();
		List<String> lstProvince = gpmbHtctDAO.getProvince();
		for(GpmbHtctDTO gp : lstGpmb) {
			mapDB.put(gp.getProvinceCode(), gp);
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
					String provinceCode = formatter.formatCellValue(row.getCell(1));
					String amountBTS = formatter.formatCellValue(row.getCell(2));
					String costGPMB = formatter.formatCellValue(row.getCell(3));
					String costNCDN = formatter.formatCellValue(row.getCell(4));
					GpmbHtctDTO dto = new GpmbHtctDTO();
					//Validate provinceCode
					if(validateString(provinceCode)) {
						if(!lstProvince.contains(provinceCode.trim().toUpperCase())) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "B", "Tỉnh không tồn tại");
							errorLst.add(errorDTO);
						} else {
							//Validate duplicate in file Import
							if(mapExcel.size() == 0) {
								dto.setProvinceCode(provinceCode.trim().toUpperCase());
								//Validate duplicate in DB
								if(mapDB.get(dto.getProvinceCode().trim().toUpperCase())!=null) {
									GpmbHtctDTO obj = mapDB.get(dto.getProvinceCode().trim().toUpperCase());
									dto.setGpmbId(obj.getGpmbId());
									dto.setCreatedDate(obj.getCreatedDate());
									dto.setCreatedUserId(obj.getCreatedUserId());
									dto.setProvinceId(obj.getProvinceId());
								}
								mapExcel.put(provinceCode.trim().toUpperCase(), provinceCode);
							} else {
								if(mapExcel.get(provinceCode.trim().toUpperCase()) != null) {
									ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "", "Dữ liệu import có bản ghi với Tỉnh trùng nhau. Hãy xem lại!");
									errorLst.add(errorDTO);
								} else {
									dto.setProvinceCode(provinceCode.trim().toUpperCase());
									if(mapDB.get(dto.getProvinceCode().trim().toUpperCase())!=null) {
										GpmbHtctDTO obj = mapDB.get(dto.getProvinceCode().trim().toUpperCase());
										dto.setGpmbId(obj.getGpmbId());
										dto.setCreatedDate(obj.getCreatedDate());
										dto.setCreatedUserId(obj.getCreatedUserId());
										dto.setProvinceId(obj.getProvinceId());
									}
									mapExcel.put(provinceCode.trim().toUpperCase(), provinceCode);
								}
							}
						}
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "B", " Tỉnh không được để trống");
						errorLst.add(errorDTO);
					}

					if(validateString(amountBTS)) {
						try {
							Long am = Long.parseLong(amountBTS);
							dto.setAmountBts(am);
						} catch(Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "C", " Giá trị Số lượng trạm BTS không hợp lệ. Hãy nhập số!");
							errorLst.add(errorDTO);
						}
					}

					if(validateString(costGPMB)) {
						try {
							Double am = Double.parseDouble(costGPMB);
							dto.setCostGpmb(am);
						} catch(Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "D", " Giá trị Chi phí nhân công giải phóng mặt bằng không hợp lệ. Hãy nhập số!");
							errorLst.add(errorDTO);
						}
					}

					if(validateString(costNCDN)) {
						try {
							Double am = Double.parseDouble(costNCDN);
							dto.setCostNcdn(am);
						} catch(Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() +1, "E", " Giá trị Chi phí nhân công đấu nối điện không hợp lệ. Hãy nhập số!");
							errorLst.add(errorDTO);
						}
					}
					if(errorLst.size() == 0) {
						workLst.add(dto);
					}
				}
			}
			if(errorLst.size() > 0) {
				String filePathError = UEncrypt.encryptFileUploadPath(fileInput);
				List<GpmbHtctDTO> emptyArray = Lists.newArrayList();
				workLst = emptyArray;
				GpmbHtctDTO errC = new GpmbHtctDTO();
				errC.setErrorList(errorLst);
				errC.setMessageColumn(5); // Column to print error
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
			List<GpmbHtctDTO> emptyArray = Lists.newArrayList();
			workLst = emptyArray;
			GpmbHtctDTO errorContainer = new GpmbHtctDTO();
			errorContainer.setErrorList(errorLst);
			errorContainer.setMessageColumn(5);//Column to print error
			workLst.add(errorContainer);
			return workLst;
		}
	}

	public void updateGpmb(GpmbHtctDTO dto) {
		gpmbHtctDAO.updateObject(dto.toModel());
	}

	public void saveGpmb(GpmbHtctDTO dto) {
		gpmbHtctDAO.saveObject(dto.toModel());
	}

	public Long getProvinceId(String provinceCode) {
		return gpmbHtctDAO.getProvinceIdForImport(provinceCode);
	}

}
