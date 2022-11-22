package com.viettel.coms.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import viettel.passport.client.UserToken;

import com.viettel.coms.business.ConstructionTaskBusinessImpl;
import com.viettel.coms.business.DmpnOrderBusinessImpl;
import com.viettel.coms.business.ManageVttbBusinessImpl;
import com.viettel.coms.business.YearPlanBusinessImpl;
import com.viettel.coms.dto.ConstructionTaskDTO;
import com.viettel.coms.dto.ConstructionTaskDetailDTO;
import com.viettel.coms.dto.DetailMonthPlanDTO;
import com.viettel.coms.dto.DetailMonthPlanSimpleDTO;
import com.viettel.coms.dto.KpiLogDTO;
import com.viettel.coms.dto.ManageDataOutsideOsDTO;
import com.viettel.coms.dto.ManageUsedMaterialDTO;
import com.viettel.coms.dto.ManageVttbDTO;
import com.viettel.coms.dto.RevokeCashMonthPlanDTO;
import com.viettel.coms.dto.SysUserDetailCOMSDTO;
import com.viettel.coms.dto.TmpnTargetDetailDTO;
import com.viettel.coms.dto.WorkItemDetailDTO;
import com.viettel.ktts.vps.VpsPermissionChecker;
import com.viettel.ktts2.common.BusinessException;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.ktts2.common.UString;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.utils.Constant;
import com.viettel.utils.ConvertData;


/**
 * @author HoangNH38
 */
public class ManageVttbRsServiceImpl implements ManageVttbRsService {

	protected final Logger log = Logger.getLogger(ManageVttbRsServiceImpl.class);
	@Autowired
	ManageVttbBusinessImpl manageVttbBusinessImpl;
	@Context
	HttpServletRequest request;
	@Value("${folder_upload2}")
	private String folderUpload;

	@Value("${default_sub_folder_upload}")
	private String defaultSubFolderUpload;

	@Value("${allow.file.ext}")
	private String allowFileExt;

	@Value("${allow.folder.dir}")
	private String allowFolderDir;

	private static RevokeCashMonthPlanDTO revokeCashMonthPlanDTO;

	// tatph-start 19/12/2019
	@Override
	public Response doSearch(ManageVttbDTO obj) {
		DataListDTO data = manageVttbBusinessImpl.doSearch(obj, request);
		return Response.ok(data).build();
	}
	
	@Override
	public Response doSearchUsedMaterial(ManageVttbDTO obj) {
		DataListDTO data = manageVttbBusinessImpl.doSearchUsedMaterial(obj, request);
		return Response.ok(data).build();
	}

	@Override
	public Response saveUsedMaterial(ManageUsedMaterialDTO obj) {
		if (obj == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		} else {
			manageVttbBusinessImpl.saveUsedMaterial(obj);
			return Response.ok(Response.Status.NO_CONTENT).build();
		}
	}



	@Override
    public Response getExcelTemplate(ManageVttbDTO obj) throws Exception {
        try {
            String strReturn = manageVttbBusinessImpl.getExcelTemplate(obj);
            return Response.ok(Collections.singletonMap("fileName", strReturn)).build();
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }


	// tatph-end 19/12/2019


}
