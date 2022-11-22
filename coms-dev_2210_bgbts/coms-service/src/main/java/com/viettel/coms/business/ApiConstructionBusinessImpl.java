package com.viettel.coms.business;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.viettel.coms.bo.WoBO;
import com.viettel.coms.bo.WoMappingChecklistBO;
import com.viettel.coms.bo.WoWorkLogsBO;
import com.viettel.coms.dao.ApiConstructionDAO;
import com.viettel.coms.dao.ConstructionDAO;
import com.viettel.coms.dao.WoDAO;
import com.viettel.coms.dao.WoMappingChecklistDAO;
import com.viettel.coms.dao.WoTrDAO;
import com.viettel.coms.dao.WoWorkLogsDAO;
import com.viettel.coms.dto.ApiConstructionRequest;
import com.viettel.coms.dto.ApiConstructionResponse;
import com.viettel.coms.dto.ConstructionDTO;
import com.viettel.coms.dto.WoAppParamDTO;
import com.viettel.coms.dto.WoDTO;
import com.viettel.coms.dto.WoSimpleSysGroupDTO;
import com.viettel.coms.dto.WoSimpleSysUserDTO;
import com.viettel.service.base.business.BaseFWBusinessImpl;
import com.viettel.service.base.model.BaseFWModelImpl;
import com.viettel.service.base.utils.StringUtils;

@Service("apiConstructionBusiness")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ApiConstructionBusinessImpl extends BaseFWBusinessImpl<ApiConstructionDAO, ConstructionDTO, BaseFWModelImpl>
implements ApiConstructionBusiness{

	@Autowired
	private ApiConstructionDAO apiConstructionDAO;
	
	@Autowired
	private WoDAO woDAO;
	
	@Autowired
	private ConstructionDAO constructionDAO;
	
	@Autowired
	private WoTrDAO trDAO;
	
	@Autowired
	private WoMappingChecklistDAO woMappingChecklistDAO;
	
	@Autowired
	private WoWorkLogsDAO woWorkLogsDAO;
	
	@Override
	public List<ApiConstructionResponse> getSolarSystemBussinessInfo(ApiConstructionRequest request) {
		// TODO Auto-generated method stub
		return apiConstructionDAO.getSolarSystemBussinessInfo(request);
	}

	@Override
	public ApiConstructionResponse getConstructionAttachFile(ApiConstructionRequest request) {
		// TODO Auto-generated method stub
		ApiConstructionResponse response = new ApiConstructionResponse();
//		response.setSystemCode(request.getSystemCode());
		response.setLstFileTrVhkt(apiConstructionDAO.getConstructionAttachFile(request));
		return response;
	}

	@Override
	public ApiConstructionResponse getWOInfoByAlert(ApiConstructionRequest request) {
		// TODO Auto-generated method stub
		ApiConstructionResponse response = new ApiConstructionResponse();
//		response.setSystemCode(request.getSystemCode());
		response.setLstWOInfoVhkt(apiConstructionDAO.getWOInfoByAlert(request));
		return response;
	}

	@Override
	public ApiConstructionResponse getTRWOBySystem(ApiConstructionRequest request) {
		// TODO Auto-generated method stub
		ApiConstructionResponse response = new ApiConstructionResponse();
//		response.setSystemCode(request.getSystemCode());
		response.setLstWOInfoVhkt(apiConstructionDAO.getTRWOBySystem(request));
		return response;
	}

	@Override
	public ApiConstructionResponse getQuickGISBySystem(ApiConstructionRequest request) {
		// TODO Auto-generated method stub
		ApiConstructionResponse response = new ApiConstructionResponse();
//		response.setSystemCode(request.getSystemCode());
		response.setLstWOInfoVhkt(apiConstructionDAO.getQuickGISBySystem(request));
		return response;
	}
	
	@Override
	public ApiConstructionResponse getDataReportMaintancePeriodic(ApiConstructionRequest request) {
		// TODO Auto-generated method stub
		ApiConstructionResponse response = new ApiConstructionResponse();
		response.setLstDataReportMaintancePeriodic(apiConstructionDAO.getDataReportMaintancePeriodic(request));
		return response;
	}
	
	@Override
	public String createWO(WoDTO request) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		String mess = "";
		Calendar current = Calendar.getInstance();
//		AppParamDTO appDto = constructionDAO.getCodeWOInAppParam("1", "WNM_VHKT_WO");
		String codeWO = woDAO.getCodeAIOWO("1");
//		String cdLevel1 = woDAO.getCodeAppParam("CD_LEVEL_1", "1").getCode();
		String cdLevel1 = "270120";
//		if(cdLevel1!=null) {
			WoSimpleSysGroupDTO cd1 = trDAO.getSysGroupById(Long.parseLong(cdLevel1));
			if (cd1 != null) {
				request.setCdLevel1Name(cd1.getGroupName());
	        }
//		}
		ConstructionDTO consDto = constructionDAO.getConsIdByCode(request.getConstructionCode());
		if(consDto!=null) {
			request.setConstructionId(consDto.getConstructionId());
			request.setContractId(consDto.getContractId());
			request.setContractCode(consDto.getContractCode());
		}
		
		WoDTO cdLv = constructionDAO.getCdLv2ByProvinceCode(request.getCatProvinceCode());
//		WoDTO cdLv = constructionDAO.getCdLv2ByProvinceCode("BGG");
		if(cdLv!=null) {
			request.setCdLevel2(cdLv.getCdLevel2());
			request.setCdLevel2Name(cdLv.getCdLevel2Name());
			request.setCdLevel3(cdLv.getCdLevel2());
			request.setCdLevel3Name(cdLv.getCdLevel2Name());
		}
		request.setCdLevel1(cdLevel1);
		request.setStatus(1l);
		request.setWoTypeId(121l);
		request.setState("ASSIGN_CD");
		request.setWoName("Xử lý sự cố");
		request.setCreatedDate(new Date());
		request.setMoneyValue(request.getMoneyValue()!=null ? request.getMoneyValue() : 0d);
		current.add(Calendar.DATE, 7);
		request.setFinishDate(current.getTime());
		WoBO bo = request.toModel();
		Long id = woDAO.saveObject(bo);
		
		bo.setWoCode("VNM_VHKT_WO" + "_" + id);
		woDAO.update(bo);
		request.setWoId(id);
		//Lưu bảng work log
		String content = "Tạo mới; ";
		content += "Giao cho CD: " + request.getCdLevel2Name() + ";";
        content += "Trạng thái: " + woDAO.getNameAppParam(request.getState(), "WO_XL_STATE");
        logWoWorkLogs(request, "1", content, gson.toJson(request), null);
        
		//Lưu mapping check list
		List<WoMappingChecklistBO> xlscChecklist = createXlscChecklistSet(id);
        woMappingChecklistDAO.saveListNoId(xlscChecklist);
		
		if(id!=0l) {
			mess = bo.getWoCode();
		}
		return mess;
	}
	
	private List<WoMappingChecklistBO> createXlscChecklistSet(Long woId) {
        List<WoMappingChecklistBO> newSet = new ArrayList<>();
        List<WoAppParamDTO> hcqtChecklistName = woDAO.getAppParam("XLSC_CHECKLIST");
        for (WoAppParamDTO param : hcqtChecklistName) {
            WoMappingChecklistBO item = new WoMappingChecklistBO();
            item.setStatus(1l);
            item.setWoId(woId);
            item.setName(param.getName());
            item.setState("NEW");
            item.setCheckListId(param.getParOrder() - 3);
            newSet.add(item);
        }

        return newSet;
    }
	
	@Transactional
    public void logWoWorkLogs(WoDTO dto, String logType, String content, String contentDetail, String loggedInUser){
        WoWorkLogsBO workLogs = new WoWorkLogsBO();
        workLogs.setWoId(dto.getWoId());
        workLogs.setContent(content);
        workLogs.setContentDetail(contentDetail);
        workLogs.setLogTime(new Date());
        workLogs.setLogType(logType);
        workLogs.setStatus(1);

        String loggedInUserStr = "Hệ thống NOCPRO";
        workLogs.setUserCreated(loggedInUserStr);

        // Write worklogs
        woWorkLogsDAO.saveObject(workLogs);
    }
	
}
