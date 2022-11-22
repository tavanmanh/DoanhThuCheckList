package com.viettel.coms.business;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.collect.Lists;
import com.viettel.cat.utils.ValidateUtils;
import com.viettel.coms.bo.EffectiveCalculateDasCapexBO;
import com.viettel.coms.dao.EffectiveCalculateDasCapexDAO;
import com.viettel.coms.dto.EffectiveCalculateDasCapexDTO;
import com.viettel.coms.dto.ExcelErrorDTO;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.service.base.business.BaseFWBusinessImpl;

@Service("effectiveCalculateDasCapexBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class EffectiveCalculateDasCapexBusinessImpl extends BaseFWBusinessImpl<EffectiveCalculateDasCapexDAO, EffectiveCalculateDasCapexDTO, EffectiveCalculateDasCapexBO> 
implements EffectiveCalculateDasCapexBusiness {
	
	static Logger LOGGER = LoggerFactory.getLogger(EffectiveCalculateDasBusinessImpl.class);
	
	@Autowired
	EffectiveCalculateDasCapexDAO effectiveCalculateDasCapexDAO;

	@Override
	public List<EffectiveCalculateDasCapexDTO> getAssumptionsCapex(EffectiveCalculateDasCapexDTO obj) {
		return effectiveCalculateDasCapexDAO.getAssumptionsCapex(obj);
	}
	
	int [] validateCol = {2};
    HashMap<Integer, String> colName = new HashMap<Integer, String>();
    {
        colName.put(1,"Loại hạng mục");
        colName.put(2,"Hạng mục");
        colName.put(3,"ĐVT");
        colName.put(4,"Đơn giá");	
        colName.put(5,"Khối lượng");	
        colName.put(6,"Ghi chú");	
    }
	
	@Override
	public List<EffectiveCalculateDasCapexDTO> importDasCapex(String fileInput) throws Exception{
		
		EffectiveCalculateDasCapexDTO dasFind = new EffectiveCalculateDasCapexDTO();
		List<EffectiveCalculateDasCapexDTO> dasList = effectiveCalculateDasCapexDAO.getAssumptionsCapex(dasFind);	
		HashMap<String,EffectiveCalculateDasCapexDTO> dasSpace = new HashMap<String, EffectiveCalculateDasCapexDTO>();
		List<EffectiveCalculateDasCapexDTO> getItem = effectiveCalculateDasCapexDAO.getItem(dasFind);
		HashMap<String,String> checkKey = new HashMap<String, String>();
		HashMap<String, String> checkItem = new HashMap<String, String>();
		for(EffectiveCalculateDasCapexDTO das : dasList){		
			dasSpace.put(das.getItem().toUpperCase(), das);			
		}
		
		for (EffectiveCalculateDasCapexDTO item : getItem) {
			checkItem.put(item.getName().toUpperCase(), item.getName());
		}


		List<EffectiveCalculateDasCapexDTO> workLst = Lists.newArrayList();
		List<ExcelErrorDTO> errorList = new ArrayList<ExcelErrorDTO>();
		
		String error = "";
		try {
			File f = new File(fileInput);
			XSSFWorkbook workbook = new XSSFWorkbook(f);
			XSSFSheet sheet = workbook.getSheetAt(0);
			DataFormatter formatter = new DataFormatter();	
			int count = 0;
			for (Row row : sheet) {
				EffectiveCalculateDasCapexDTO obj = new EffectiveCalculateDasCapexDTO();
				count++;
				if (count >= 3 && !ValidateUtils.checkIfRowIsEmpty(row)) {					
 					String itemType = formatter.formatCellValue(row.getCell(1));
					String item = formatter.formatCellValue(row.getCell(2));
					String unit = formatter.formatCellValue(row.getCell(3));	
					String cost = formatter.formatCellValue(row.getCell(4));		
					String mass = formatter.formatCellValue(row.getCell(5));
					String note = formatter.formatCellValue(row.getCell(6));
					validateRequiredCell(row, errorList); 	
					if(checkKey.get(item.toUpperCase()) == null) {
						checkKey.put(item.toUpperCase(), item);
					}else {
						ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, 3, colName.get(2)+ " đã bị trùng");
						errorList.add(errorDTO);
					}
					
					if(validateString(itemType)) {
						if(itemType.length() < 2000) {
							obj.setItemType(itemType);
						}else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, 2, colName.get(1)+ " không hợp lệ");
							errorList.add(errorDTO);
						}		
					}
															
					if(validateString(item)) {
						if(item.length() < 2000) {
							if(checkItem.get(item.toUpperCase()) != null) {
								obj.setItem(item);
							}else {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, 3, colName.get(2)+ " không hợp lệ");
								errorList.add(errorDTO);
							}
							obj.setItem(item);
							EffectiveCalculateDasCapexDTO dto = dasSpace.get(item.trim().toUpperCase());
							if(dto!=null) {
								obj.setAssumptionsCapexId(dto.getAssumptionsCapexId());
							}
						}else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, 3, colName.get(2)+ " không hợp lệ");
							errorList.add(errorDTO);
						}						
					}
					
					if(validateString(unit)) {
						if( unit.length() < 100) {
							obj.setUnit(unit);
						}else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, 4, colName.get(3)+ " không hợp lệ");
							errorList.add(errorDTO);
						}		
					}
						
					if(validateString(cost)) {
						if(cost.length() < 20) {
							try{
								obj.setCost(Double.parseDouble(cost));
							} catch(Exception e){
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, 5, colName.get(4)+ " không hợp lệ");
								errorList.add(errorDTO);
							}
						}else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, 5, colName.get(4)+ " không hợp lệ");
							errorList.add(errorDTO);
						}						
					}
											
					if(validateString(mass) ) {
						if(mass.length() < 20) {
							try{
								obj.setMass(Double.parseDouble(mass));
							} catch(Exception e){
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, 6, colName.get(5)+ " không hợp lệ");
								errorList.add(errorDTO);
							}
						}else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, 6, colName.get(5)+ " không hợp lệ");
							errorList.add(errorDTO);	
						}						
					}
					
					if(validateString(note)) {
						if(note.length() < 2000) {
							obj.setNote(note);
						}else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, 7, colName.get(6)+ " không hợp lệ");
							errorList.add(errorDTO);
						}						
					}
																
					if(errorList.size() == 0){
						workLst.add(obj);
					}
				}		
			}

			if(errorList.size() > 0){
				String filePathError = UEncrypt.encryptFileUploadPath(fileInput);
				List<EffectiveCalculateDasCapexDTO> emptyArray = Lists.newArrayList();
				workLst = emptyArray;
				EffectiveCalculateDasCapexDTO errorContainer = new EffectiveCalculateDasCapexDTO();
				errorContainer.setErrorList(errorList);
				errorContainer.setMessageColumn(7); 
				errorContainer.setFilePathError(filePathError);
				workLst.add(errorContainer);
			}
			
			workbook.close();
			return workLst;
			
		} catch (NullPointerException pointerException) {
			LOGGER.error(pointerException.getMessage(), pointerException);
			throw new Exception(error);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new Exception(error);
		}		
	}
	
	public boolean validateString(String str){
		return (str != null && str.length()>0);
	}
		
	public Boolean isExist(String code, HashMap<String,String> dasSpace) {
		return dasSpace.get(code) != null;
	}
	
	private ExcelErrorDTO createError(int row, int column, String detail){
		ExcelErrorDTO err = new ExcelErrorDTO();
		err.setColumnError(String.valueOf(column));
		err.setLineError(String.valueOf(row));
		err.setDetailError(detail);
		return err;
	}
	
	public boolean validateRequiredCell(Row row, List<ExcelErrorDTO> errorList){
		DataFormatter formatter = new DataFormatter();
		boolean result = true;
		for(int colIndex : validateCol){
			if(!validateString(formatter.formatCellValue(row.getCell(colIndex)))
					&& colIndex  != 3 && colIndex  != 4 && colIndex  != 5 && colIndex != 6){
				ExcelErrorDTO errorDTO = new ExcelErrorDTO();
				errorDTO.setColumnError(String.valueOf(colIndex + 1));
				errorDTO.setLineError(String.valueOf(row.getRowNum() + 1));
				errorDTO.setDetailError(colName.get(colIndex)+" chưa nhập");
				errorList.add(errorDTO);
				result = false;
			}			
		}
		return result;
	}
}
