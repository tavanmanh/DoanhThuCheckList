package com.viettel.coms.business;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;
import com.viettel.coms.bo.CapexBtsHtctBO;
import com.viettel.coms.dao.CapexBtsHtctDAO;
import com.viettel.coms.dto.CapexBtsHtctDTO;
import com.viettel.coms.dto.CapexSourceHTCTDTO;
import com.viettel.coms.dto.ExcelErrorDTO;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.wms.utils.ValidateUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
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

@Service("capexBtsHtctBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CapexBtsHtctBusinessImpl extends BaseFWBusinessImpl<CapexBtsHtctDAO, CapexBtsHtctDTO, CapexBtsHtctBO>
		implements CapexBtsHtctBusiness {

	@Autowired
	private CapexBtsHtctDAO capexBtsHtctDAO;

	public CapexBtsHtctBusinessImpl() {
		tModel = new CapexBtsHtctBO();
		tDAO = capexBtsHtctDAO;
	}

	public List<CapexBtsHtctDTO> getData7(CapexBtsHtctDTO obj) {
		return capexBtsHtctDAO.getData7(obj);
	}

	public List<CapexBtsHtctDTO> findByProvince(String codeProvince) {
		return capexBtsHtctDAO.findByProvince(codeProvince);
	}

	@Value("${folder_upload2}")
	private String folder2Upload;
	@Value("${default_sub_folder_upload}")
	private String defaultSubFolderUpload;

	public String downloadTemplateCapex(CapexBtsHtctDTO obj) throws Exception {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		String filePath = classLoader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(new FileInputStream(filePath + "Import_Capex.xlsx"));
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
			udir.mkdir();
		}
		OutputStream outFile = new FileOutputStream(udir.getAbsolutePath() + File.separator + "Import_Capex.xlsx");
		workbook.write(outFile);
		workbook.close();
		outFile.close();
		String path = UEncrypt.encryptFileUploadPath(uploadPathReturn + File.separator + "Import_Capex.xlsx");
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

	public List<CapexBtsHtctDTO> importCapex(String fileInput) {
		List<CapexBtsHtctDTO> workLst = Lists.newArrayList();
		List<ExcelErrorDTO> errorLst = new ArrayList<>();
		List<CapexBtsHtctDTO> lstCapex = capexBtsHtctDAO.getData7(new CapexBtsHtctDTO());
		HashMap<String, CapexBtsHtctDTO> mapDB = new HashMap<>();
		HashMap<String, String> mapDuplicate = new HashMap<>();
		List<String> lstProvince = capexBtsHtctDAO.getProvince();
		List<CapexBtsHtctDTO> lstItemCapexTem = capexBtsHtctDAO.lstItemCapexTem();
		HashMap<String, CapexBtsHtctDTO> mapTem = new HashMap<>();

		for (CapexBtsHtctDTO capex : lstCapex) {
			mapDB.put(capex.getItemType() + capex.getItem() + capex.getWorkCapex() + capex.getProvinceCode(), capex);
		}
		for (CapexBtsHtctDTO itemC : lstItemCapexTem) {
			mapTem.put(itemC.getItemType() + itemC.getItem() + itemC.getWorkCapex(), itemC);
		}

		try {
			File f = new File(fileInput);
			XSSFWorkbook workbook = new XSSFWorkbook(f);
			XSSFSheet sheet = workbook.getSheetAt(0);
			DataFormatter formatter = new DataFormatter();
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			int count = 0;
			for (Row row : sheet) {
				count++;
				if (count >= 3 && !ValidateUtils.checkIfRowIsEmpty(row)) {
					String itemType = formatter.formatCellValue(row.getCell(1));
					String item = formatter.formatCellValue(row.getCell(2));
					String workCapex = formatter.formatCellValue(row.getCell(3));
					String provinceCode = formatter.formatCellValue(row.getCell(4));
					String costCapexBTS = formatter.formatCellValue(row.getCell(5));
					CapexBtsHtctDTO dto = new CapexBtsHtctDTO();
					if (validateString(itemType)) {
						dto.setItemType(itemType);
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "B",
								"Loại hạng mục không được để trống");
						errorLst.add(errorDTO);
					}
					if (validateString(item)) {
						dto.setItem(item);
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "C", "Hạng mục không được để trống");
						errorLst.add(errorDTO);
					}
					if (validateString(workCapex)) {
						// Validate LHM, HM, CV exist in Item_capex table
						if (validateString(itemType) && validateString(item)
								&& (mapTem.get(itemType + item + workCapex) != null)) {
							dto.setWorkCapex(workCapex);
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "D",
									"Loại hạng mục, Hạng mục, Công việc không tồn tại");
							errorLst.add(errorDTO);
						}

					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "D", "Công việc không được để trống");
						errorLst.add(errorDTO);
					}
					// validate provinceCode
					if (validateString(provinceCode)) {
						if (!lstProvince.contains(provinceCode.trim().toUpperCase())) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "E", "Tỉnh không tồn tại");
							errorLst.add(errorDTO);
						} else {
							// Validate duplicate in file import
							if (mapDuplicate.size() == 0) {
								dto.setProvinceCode(provinceCode.trim().toUpperCase());
								// Validate duplicate in DB
								if (validateString(itemType) && validateString(item) && validateString(workCapex)) {
									if (mapDB.get(dto.getItemType() + dto.getItem() + dto.getWorkCapex()
											+ dto.getProvinceCode().trim().toUpperCase()) != null) {
										CapexBtsHtctDTO obj = mapDB.get(dto.getItemType() + dto.getItem()
												+ dto.getWorkCapex() + dto.getProvinceCode().trim().toUpperCase());
										dto.setCapexBtsId(obj.getCapexBtsId());
									}
									mapDuplicate.put((itemType + item + workCapex + provinceCode.trim().toUpperCase()),
											itemType + item + workCapex + provinceCode.trim().toUpperCase());
								}

							} else {
								if (validateString(itemType) && validateString(item) && validateString(workCapex)) {
									if (mapDuplicate.get((itemType + item + workCapex
											+ provinceCode.trim().toUpperCase())) != null) {
										ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "",
												"Dữ liệu import có bản ghi với cùng Loại hạng mục, Hạng mục, Công việc, Tỉnh trùng nhau. Hãy xem lại!");
										errorLst.add(errorDTO);
									} else {
										dto.setProvinceCode(provinceCode.trim().toUpperCase());
										if (mapDB.get(dto.getItemType() + dto.getItem() + dto.getWorkCapex()
												+ dto.getProvinceCode().trim().toUpperCase()) != null) {
											CapexBtsHtctDTO obj = mapDB.get(dto.getItemType() + dto.getItem()
													+ dto.getWorkCapex() + dto.getProvinceCode().trim().toUpperCase());
											dto.setCapexBtsId(obj.getCapexBtsId());
										}
										mapDuplicate.put((itemType + item + workCapex + provinceCode.trim().toUpperCase()),
												itemType + item + workCapex + provinceCode.trim().toUpperCase());
									}
								}
							}
						}
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "E", "Tỉnh không được để trống");
						errorLst.add(errorDTO);
					}
					// validate Gia tri
					if (validateString(costCapexBTS)) {
						try {
							Double costC = Double.parseDouble(costCapexBTS);
							dto.setCostCapexBts(costC);
						} catch (Exception e) {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "F",
									" Hãy nhập số vào ô giá trị");
							errorLst.add(errorDTO);
						}
					}
					if (errorLst.size() == 0) {
						workLst.add(dto);
					}
				}

			}
			if (errorLst.size() > 0) {
				String filePathError = UEncrypt.encryptFileUploadPath(fileInput);
				List<CapexBtsHtctDTO> emptyArray = Lists.newArrayList();
				workLst = emptyArray;
				CapexBtsHtctDTO errC = new CapexBtsHtctDTO();
				errC.setErrorList(errorLst);
				errC.setMessageColumn(6); // Column to print error
				errC.setFilePathError(filePathError);
				workLst.add(errC);
			}
			workbook.close();
			return workLst;
		} catch (Exception e) {
			ExcelErrorDTO errorDTO = createError(0, "", e.toString());
			errorLst.add(errorDTO);
			try {
				String filePathError = UEncrypt.encryptFileUploadPath(fileInput);
			} catch (Exception ex) {
				errorDTO = createError(0, "", ex.toString());
				errorLst.add(errorDTO);
			}
			List<CapexBtsHtctDTO> emptyArray = Lists.newArrayList();
			workLst = emptyArray;
			CapexBtsHtctDTO errorContainer = new CapexBtsHtctDTO();
			errorContainer.setErrorList(errorLst);
			errorContainer.setMessageColumn(6);// Column to print error
			workLst.add(errorContainer);
			return workLst;
		}
	}

	public void updateCapex(String itemType, String item, String workCapex, String provinceCode, Double costCapexBts,
			Long capexBtsId, Date updatedDate, Long updateUserId) {
		capexBtsHtctDAO.updateCapex(itemType, item, workCapex, provinceCode, costCapexBts, capexBtsId, updatedDate,
				updateUserId);

	}

	public void saveCapex(CapexBtsHtctDTO dto) {
		capexBtsHtctDAO.saveObject(dto.toModel());

	}

	public Long getProvinceIdForImport(String provinceCode) {
		return capexBtsHtctDAO.getProvinceIdForImport(provinceCode);
	}

}
