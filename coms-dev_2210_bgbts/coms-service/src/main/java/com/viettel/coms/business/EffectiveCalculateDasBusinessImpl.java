package com.viettel.coms.business;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;
import com.viettel.cat.utils.ValidateUtils;
import com.viettel.coms.bo.EffectiveCalculateDasBO;
import com.viettel.coms.dao.EffectiveCalculateDasDAO;
import com.viettel.coms.dto.EffectiveCalculateDasDTO;
import com.viettel.coms.dto.ExcelErrorDTO;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.service.base.business.BaseFWBusinessImpl;

@Service("effectiveCalculateDasBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class EffectiveCalculateDasBusinessImpl extends BaseFWBusinessImpl<EffectiveCalculateDasDAO, EffectiveCalculateDasDTO, EffectiveCalculateDasBO> 
implements EffectiveCalculateDasBusiness {
	
	static Logger LOGGER = LoggerFactory.getLogger(EffectiveCalculateDasBusinessImpl.class);
	@Autowired
	EffectiveCalculateDasDAO effectiveCalculateDasDAO;
	@Override
	
	   public List<EffectiveCalculateDasDTO> getAssumptions(EffectiveCalculateDasDTO obj) {
			return effectiveCalculateDasDAO.getAssumptions(obj);
	   }

	   int [] validateCol = {1};
	    HashMap<Integer, String> colName = new HashMap<Integer, String>();
	    {
	        colName.put(1,"Nội dung");
	        colName.put(2,"Đơn vị tính");
	        colName.put(3,"Giá trị");
	        colName.put(4,"Ghi chú");	        
	    }
	    
		@Override
		public List<EffectiveCalculateDasDTO> importDas(String fileInput) throws Exception{
			EffectiveCalculateDasDTO dasFind = new EffectiveCalculateDasDTO();
			List<EffectiveCalculateDasDTO> dasList = effectiveCalculateDasDAO.getAssumptions(dasFind);
			List<EffectiveCalculateDasDTO> getContent = effectiveCalculateDasDAO.getContent(dasFind);
			HashMap<String,EffectiveCalculateDasDTO> dasSpace = new HashMap<String, EffectiveCalculateDasDTO>();
			HashMap<String, String> checkContent = new HashMap<String, String>();
			HashMap<String,String> checkKey = new HashMap<String, String>();
			
			for(EffectiveCalculateDasDTO das : dasList){
				dasSpace.put(das.getContentAssumptions().toUpperCase(), das);	
			}
			
			for (EffectiveCalculateDasDTO content : getContent) {
				checkContent.put(content.getName().toUpperCase(), content.getName());
			}

			List<EffectiveCalculateDasDTO> workLst = Lists.newArrayList();
			List<ExcelErrorDTO> errorList = new ArrayList<ExcelErrorDTO>();
			String error = "";
			
			try {
				File f = new File(fileInput);
				XSSFWorkbook workbook = new XSSFWorkbook(f);
				XSSFSheet sheet = workbook.getSheetAt(0);
				DataFormatter formatter = new DataFormatter();
				int count = 0;
				for (Row row : sheet) {
					EffectiveCalculateDasDTO obj = new EffectiveCalculateDasDTO();
					count++;
					if (count >= 3 && !ValidateUtils.checkIfRowIsEmpty(row)) {					
     					String contentAssumptions = formatter.formatCellValue(row.getCell(1));
						String unit = formatter.formatCellValue(row.getCell(2));
						String costAssumptions = formatter.formatCellValue(row.getCell(3));
						String noteAssumptions = formatter.formatCellValue(row.getCell(4));						
						validateRequiredCell(row, errorList); 	
						
						if(checkKey.get(contentAssumptions.toUpperCase()) == null) {
							checkKey.put(contentAssumptions.toUpperCase(), contentAssumptions);
							
						}else {
							ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, 2, colName.get(1)+ " đã bị trùng");
							errorList.add(errorDTO);
						}
						
						if(validateString(contentAssumptions)) {
							if(contentAssumptions.length() < 2000) {								
 								if(checkContent.get(contentAssumptions.toUpperCase()) != null) {
									obj.setContentAssumptions(contentAssumptions);
								}else {
									ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, 2, colName.get(1)+ " không hợp lệ");
									errorList.add(errorDTO);
								}
								EffectiveCalculateDasDTO dto = dasSpace.get(contentAssumptions.trim().toUpperCase());
								if(dto!=null) {
								obj.setAssumptionsId(dto.getAssumptionsId());
								}
							}else {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, 2, colName.get(1)+ " không hợp lệ");
								errorList.add(errorDTO);
							}							
						}
						
						if(validateString(unit))
							if(unit.length() < 100) {
								obj.setUnit(unit);
							}else {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, 3, colName.get(2)+ " không hợp lệ");
								errorList.add(errorDTO);
							}
												
						if(validateString(costAssumptions)) {
							if(costAssumptions.length() < 20) {
								try{
									obj.setCostAssumptions(Double.parseDouble(costAssumptions));
								} catch(Exception e){
									ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, 4, colName.get(3)+ " không hợp lệ");
									errorList.add(errorDTO);
								}
							}else {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, 4, colName.get(3)+ " không hợp lệ");
								errorList.add(errorDTO);
							}
						}
													
						if(validateString(noteAssumptions))
							if(noteAssumptions.length() < 2000) {
								obj.setNoteAssumptions(noteAssumptions);
							}else {
								ExcelErrorDTO errorDTO = createError(row.getRowNum() + 1, 4, colName.get(4)+ " không hợp lệ");
								errorList.add(errorDTO);
							}
							
																
						if(errorList.size() == 0){
							workLst.add(obj);
						}
					}				
				}

				if(errorList.size() > 0){
					String filePathError = UEncrypt.encryptFileUploadPath(fileInput);
					List<EffectiveCalculateDasDTO> emptyArray = Lists.newArrayList();
					workLst = emptyArray;
					EffectiveCalculateDasDTO errorContainer = new EffectiveCalculateDasDTO();
					errorContainer.setErrorList(errorList);
					errorContainer.setMessageColumn(5); 
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
						&& colIndex  != 5 && colIndex != 6 && colIndex != 7){
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
