package com.viettel.coms.business;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


import org.apache.commons.collections.iterators.ArrayListIterator;
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
import com.viettel.coms.bo.Cost1477HtctBO;
import com.viettel.coms.dao.Cost1477HtctDAO;
import com.viettel.coms.dto.CalculateEfficiencyHtctDTO;
import com.viettel.coms.dto.CapexBtsHtctDTO;
import com.viettel.coms.dto.Cost1477HtctDTO;
import com.viettel.coms.dto.ExcelErrorDTO;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.wms.utils.ValidateUtils;

@Service("cost1477HtctBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Cost1477HtctBusinessImpl extends BaseFWBusinessImpl<Cost1477HtctDAO, Cost1477HtctDTO, Cost1477HtctBO>
		implements Cost1477HtctBusiness {

	@Autowired
	private Cost1477HtctDAO cost1477HtctDAO;

	public Cost1477HtctBusinessImpl() {
		tModel = new Cost1477HtctBO();
		tDAO = cost1477HtctDAO;
	}

	public List<Cost1477HtctDTO> getData6(Cost1477HtctDTO obj) {
		return cost1477HtctDAO.getData6(obj);
	}

	public Cost1477HtctDTO findByGroup(String typeGroup, String address, String topographic, String stationType) {
		return cost1477HtctDAO.findByGroup(typeGroup, address, topographic, stationType);
	}

	@Value("${folder_upload2}")
	private String folder2Upload;
	@Value("${default_sub_folder_upload}")
	private String defaultSubFolderUpload;

	public String downloadTemplateGiaThue1477(Cost1477HtctDTO obj) throws Exception {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("../" + "doc-template").getPath();
		InputStream file = new BufferedInputStream(
				new FileInputStream(filePath + "Import_Gia_Thue_TTR_Theo_1477.xlsx"));
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
				udir.getAbsolutePath() + File.separator + "Import_Gia_Thue_TTR_Theo_1477.xlsx");
		workbook.write(outFile);
		workbook.close();
		outFile.close();
		String path = UEncrypt
				.encryptFileUploadPath(uploadPathReturn + File.separator + "Import_Gia_Thue_TTR_Theo_1477.xlsx");
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

	public List<Cost1477HtctDTO> importGiaThue1477(String fileInput) {
		List<Cost1477HtctDTO> workLst = Lists.newArrayList();
		List<ExcelErrorDTO> errorLst = new ArrayList<>();
		List<Cost1477HtctDTO> lstCost1477 = cost1477HtctDAO.getData6(new Cost1477HtctDTO());
		HashMap<String, Cost1477HtctDTO> mapDB = new HashMap<>();
		HashMap<String, String> mapExcel = new HashMap<>();
		List<String> Gr = new ArrayList<>(Arrays.asList("Nhóm 1", "Nhóm 2", "Nhóm 3", "Nhóm 4", "Nhóm 5", "Nhóm 6",
				"Nhóm 7", "Nhóm 8", "Nhóm 9"));
		List<String> Add = new ArrayList<>(
				Arrays.asList("Thị xã", "Quận", "Thành phố", "Thị trấn - Huyện", "Xã - Huyện"));
		List<String> Topo = new ArrayList<>(Arrays.asList("Trên mái", "Dưới đất"));
		List<String> Type = new ArrayList<>(Arrays.asList("Trạm BTS", "Trạm RRU", "Trạm Small cell"));

		for (Cost1477HtctDTO cost : lstCost1477) {
			mapDB.put(cost.getTypeGroup() + cost.getAddress() + cost.getTopographic() + cost.getStationType(), cost);
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
					String typeGroup = formatter.formatCellValue(row.getCell(1));
					String address = formatter.formatCellValue(row.getCell(2));
					String topographic = formatter.formatCellValue(row.getCell(3));
					String stationType = formatter.formatCellValue(row.getCell(4));
					String cost1477 = formatter.formatCellValue(row.getCell(5));
					Cost1477HtctDTO dto = new Cost1477HtctDTO();
					if (validateString(typeGroup)) {
						if (Gr.contains(typeGroup)) {
							dto.setTypeGroup(typeGroup);
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "B", "Nhóm không tồn tại");
							errorLst.add(errorDTO);
						}
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "B", "Nhóm không được để trống");
						errorLst.add(errorDTO);
					}
					if (validateString(address)) {
						if (Add.contains(address)) {
							dto.setAddress(address);
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "C",
									"Địa điểm cụ thể không tồn tại");
							errorLst.add(errorDTO);
						}
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "C",
								"Địa điểm cụ thể không được để trống");
						errorLst.add(errorDTO);
					}
					if (validateString(topographic)) {
						if (Topo.contains(topographic)) {
							dto.setTopographic(topographic);
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "D", "Vị trí không tồn tại");
							errorLst.add(errorDTO);
						}
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "D", "Vị trí không được để trống");
						errorLst.add(errorDTO);
					}
					if (validateString(stationType)) {
						if (Type.contains(stationType)) {
							// Validate duplicate in file import
							if (mapExcel.size() == 0) {
								dto.setStationType(stationType);
								// Validate in DB
								if (validateString(typeGroup) && validateString(address)
										&& validateString(topographic)) {
									if (mapDB.get((dto.getTypeGroup() + dto.getAddress() + dto.getTopographic()
											+ dto.getStationType())) != null) {
										Cost1477HtctDTO obj = mapDB.get((dto.getTypeGroup() + dto.getAddress()
												+ dto.getTopographic() + dto.getStationType()));
										dto.setCost1477HtctId(obj.getCost1477HtctId());
										dto.setCreatedDate(obj.getCreatedDate());
										dto.setCreatedUserId(obj.getCreatedUserId());
									}
									mapExcel.put((typeGroup + address + topographic + stationType),
											(typeGroup + address + topographic + stationType));
								}
							} else {
								if (validateString(typeGroup) && validateString(address)
										&& validateString(topographic)) {
									if (mapExcel.get((typeGroup + address + topographic + stationType)) != null) {
										ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "",
												"Dữ liệu import có bản ghi với cùng Nhóm, Địa điểm cụ thể, Vị trí, Loại trạm trùng nhau. Hãy xem lại!");
										errorLst.add(errorDTO);
									} else {
										dto.setStationType(stationType);
										if (mapDB.get((dto.getTypeGroup() + dto.getAddress() + dto.getTopographic()
												+ dto.getStationType())) != null) {
											Cost1477HtctDTO obj = mapDB.get((dto.getTypeGroup() + dto.getAddress()
													+ dto.getTopographic() + dto.getStationType()));
											dto.setCost1477HtctId(obj.getCost1477HtctId());
											dto.setCreatedDate(obj.getCreatedDate());
											dto.setCreatedUserId(obj.getCreatedUserId());
										}
										mapExcel.put((typeGroup + address + topographic + stationType),
												(typeGroup + address + topographic + stationType));
									}
								}
							}
						} else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "E", "Loại trạm không tồn tại");
							errorLst.add(errorDTO);
						}
					} else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, "E", "Loại trạm không được để trống");
						errorLst.add(errorDTO);
					}

					if (validateString(cost1477)) {
						try {
							Double cs = Double.parseDouble(cost1477);
							dto.setCost1477(cs);
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
				List<Cost1477HtctDTO> emptyArray = Lists.newArrayList();
				workLst = emptyArray;
				Cost1477HtctDTO errC = new Cost1477HtctDTO();
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
			List<Cost1477HtctDTO> emptyArray = Lists.newArrayList();
			workLst = emptyArray;
			Cost1477HtctDTO errorContainer = new Cost1477HtctDTO();
			errorContainer.setErrorList(errorLst);
			errorContainer.setMessageColumn(6);// Column to print error
			workLst.add(errorContainer);
			return workLst;
		}
	}

	public void saveCost1477(Cost1477HtctDTO dto) {
		cost1477HtctDAO.saveObject(dto.toModel());

	}

	public void updateCost1477(Cost1477HtctDTO dto) {
		cost1477HtctDAO.updateObject(dto.toModel());

	}

}
