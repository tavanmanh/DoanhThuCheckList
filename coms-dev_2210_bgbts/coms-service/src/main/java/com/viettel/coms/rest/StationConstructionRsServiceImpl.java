package com.viettel.coms.rest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;



import com.viettel.asset.dto.ReportIncreaseDecreaseDto;
import com.viettel.coms.business.RpBTSBusinessImpl;
import com.viettel.coms.business.StationConstructionBusinessImpl;
import com.viettel.coms.business.YearPlanBusinessImpl;
import com.viettel.coms.dto.KpiLogDTO;
import com.viettel.coms.dto.RpBTSDTO;
import com.viettel.coms.dto.StationConstructionDTO;
import com.viettel.coms.dto.StationConstructionOverviewDTO;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.dto.DataListDTO;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

public class StationConstructionRsServiceImpl implements StationConstructionRsService{

	
    @Autowired
    StationConstructionBusinessImpl stationConstructionBusinessImpl;
    @Context
    HttpServletRequest request;
    
    @Value("${folder_upload2}")
	private String folderUpload;

    
    static Logger LOGGER = LoggerFactory.getLogger(StationConstructionRsServiceImpl.class);
    
	@Override
	public Response doSearch(StationConstructionDTO obj) {
		//tanqn start 20181113
    	KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
       
        DataListDTO data = stationConstructionBusinessImpl.doSearch(obj);
        return Response.ok(data).build();
	}
	
	@Override
	public Response exportExcel(StationConstructionDTO obj) throws Exception {
		// TODO Auto-generated method stub
		//Get Data
        
        KttsUserSession user = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        String filePath = classloader.getResource("../" + "doc-template").getPath();

        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss aa");
        //Desired format: 24 hour format: Change the pattern as per the need
        String exportTime = sdf.format(new Timestamp(System.currentTimeMillis()));
        DataListDTO data = stationConstructionBusinessImpl.doSearch(obj);
        @SuppressWarnings("unchecked")
		List<StationConstructionOverviewDTO> lstStationReport = data.getData();
        InputStream is;
        String fileName = "";
        try {

            Map beans = new HashMap();
            beans.put("exportTime", exportTime);
            beans.put("exportUser", user.getVpsUserInfo().getFullName());

            if(obj.getTypeBc().equals("1")==true){
            	if(lstStationReport != null && lstStationReport.size() > 0) {
            		List<StationConstructionDTO> lstDetail = lstStationReport.get(0).getListStation();
            		beans.put("lstStationReport", lstDetail);
            		beans.put("allStationVTNet", lstDetail.get(0).getSumStationVtNet());
            		beans.put("allStationVcc", lstDetail.get(0).getSumStationVcc());
            		beans.put("allProject", lstDetail.get(0).getSumProject());
            		
            	}
            	fileName = "BAO_CAO_CHI_TIET_DANH_MUC_NHA_TRAM_CONG_TRINH.xlsx";
            }else {
            	if(lstStationReport != null && lstStationReport.size() > 0)	beans.put("lstStationReport", lstStationReport);
            	fileName = "BAO_CAO_TONG_QUAN_DANH_MUC_NHA_TRAM_CONG_TRINH.xlsx";
            }
            	
            
            
            
            is = new BufferedInputStream(new FileInputStream(filePath + fileName));
            XLSTransformer transformer = new XLSTransformer();
            Workbook resultWorkbook = transformer.transformXLS(is, beans);
            is.close();
            saveWorkbook(resultWorkbook, folderUpload + "/" + fileName);
        } catch (FileNotFoundException fe) {
            LOGGER.error(fe.getMessage(), fe);
        } catch (ParsePropertyException pe) {
            LOGGER.error(pe.getMessage(), pe);
        } catch (InvalidFormatException formate) {
            LOGGER.error(formate.getMessage(), formate);
        } catch (IOException ioe) {
            LOGGER.error(ioe.getMessage(), ioe);
        }
        
        
        String path = UEncrypt.encryptFileUploadPath(fileName);
        return Response.ok(Collections.singletonMap("fileName", path)).build();
	}
	
	private void saveWorkbook(Workbook resultWorkbook, String fileName) throws IOException {
        OutputStream os = new BufferedOutputStream(new FileOutputStream(fileName));
        resultWorkbook.write(os);
        os.flush();
        os.close();
    }


}
