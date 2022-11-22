package com.viettel.coms.webservice;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.viettel.asset.dto.ResultInfo;
import com.viettel.coms.business.ConstructionTaskBusinessImpl;
import com.viettel.coms.business.SignVofficeBusinessImpl;
import com.viettel.coms.business.WoBusinessImpl;
import com.viettel.coms.dao.WoDAO;
import com.viettel.coms.dto.ConstructioIocDTO;
import com.viettel.coms.dto.ConstructionTaskDTOResponse;
import com.viettel.coms.dto.ConstructionTotalValueDTO;
import com.viettel.coms.dto.SignVofficeDTO;
import com.viettel.coms.dto.SignVofficeRequestDTO;
import com.viettel.coms.dto.SysGroupDTO;
import com.viettel.coms.dto.WoDTO;
import com.viettel.coms.dto.WoXdddChecklistDTO;

@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Produces({ MediaType.APPLICATION_JSON + ";charset=utf-8", MediaType.APPLICATION_XML })
@Path("/")
public class IocRestApiWs {

	private Logger LOGGER = Logger.getLogger(IocRestApiWs.class);

	@Autowired
	ConstructionTaskBusinessImpl constructionTaskBusiness;

	@Autowired
	private WoBusinessImpl woBusinessImpl;

	@Autowired
	SignVofficeBusinessImpl signVofficeBusinessImpl;

	@Autowired
	WoDAO woDAO;

	private final String SUCCESS_MSG = "SUCCESS";

	// Huypq-28032022-start api IOC
	@POST
	@Path("/apiIoc/getDataConstructionForIOC/")
	public ConstructionTaskDTOResponse getDataConstructionForIOC() {
		ConstructionTaskDTOResponse response = new ConstructionTaskDTOResponse();
		try {
			List<ConstructioIocDTO> data = constructionTaskBusiness.getDataConstructionForIOC();
			response.setLstConstruction(data);
			ResultInfo resultInfo = new ResultInfo();
			resultInfo.setStatus(ResultInfo.RESULT_OK);
			response.setResultInfo(resultInfo);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			ResultInfo resultInfo = new ResultInfo();
			resultInfo.setStatus(ResultInfo.RESULT_NOK);
			resultInfo.setMessage(e.getMessage());
			response.setResultInfo(resultInfo);
		}
		return response;
	}

	@POST
	@Path("/apiIoc/createWoWhenCompleteWorkItemFromIOC/")
	public ConstructionTaskDTOResponse createWoWhenCompleteWorkItemFromIOC(WoDTO woDto) {
		System.out.println("Wo tạo từ IOC");
		ConstructionTaskDTOResponse response = new ConstructionTaskDTOResponse();
		WoDTO respWo = new WoDTO();
		try {
			if (woDto.getWoTypeId() != null) {
				if (woDto.getWoTypeId().compareTo(1l) == 0) {
					SysGroupDTO groupLv2 = woDAO.getCdLevel2BySysGroupIdAndCode(woDto.getSysGroupId(), "P.HT");
					woDto.setWoTypeId(1l);
					woDto.setWoName("Thi công công trình");
					woDto.setState("DONE");
					woDto.setCdLevel1("242656");
					woDto.setCdLevel1Name("Trung tâm hạ tầng");
					woDto.setCdLevel2(groupLv2.getSysGroupId().toString());
					woDto.setCdLevel2Name(groupLv2.getName());
					woDto.setCatConstructionTypeId(8l);
					woDto.setChecklistStep((long) woDto.getXdddChecklist().size());
					woDto.setApConstructionType(8l);
					woDto.setApWorkSrc(6l);
					woDto.setWoNameId(1l);
					woDto.setLoggedInUser(woDto.getUserCreated());
					woDto.setClosedTime(new Date());
					woDto.setEndTime(new Date());
					woDto.setSysUserId(woDto.getCreatedUserId());
				}
			}
			woDto.setIsIOC(true);
			woDto.setIsCreateNew(true);
			woBusinessImpl.createNewWOFromIOC(woDto);
			respWo.setWoId(woDto.getWoId());

			// response.setLstConstruction(data);
			ResultInfo resultInfo = new ResultInfo();
			resultInfo.setStatusReponse(200l);
			response.setResultInfo(resultInfo);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			ResultInfo resultInfo = new ResultInfo();
			resultInfo.setStatusReponse(400l);
			resultInfo.setMessage(e.getMessage());
			response.setResultInfo(resultInfo);
		}
		return response;
	}
	// Huy-end

	// Huypq-30052022-start
	@GET
	@Path("/apiIoc/getWoCheckListIsConfirm/")
	public ResultInfo getWoCheckListIsConfirm() {
		ResultInfo resultInfo = new ResultInfo();
		try {
			List<WoXdddChecklistDTO> data = woDAO.getCheckListIsConfirm();
			resultInfo.setResponse(data);
			resultInfo.setStatusReponse(ResultInfo.STATUS_OK);
			resultInfo.setMessage(SUCCESS_MSG);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			resultInfo.setStatusReponse(ResultInfo.STATUS_NOK);
			resultInfo.setMessage(e.getMessage());
		}
		return resultInfo;
	}

	@POST
	@Path("/apiIoc/sendApproveWoHcFromIoc")
	public ResultInfo sendApproveWoHcFromIoc(WoDTO request) {
		ResultInfo resultInfo = new ResultInfo();
		try {
			List<WoDTO> listWo = woBusinessImpl.sendApproveWoHcFromIoc(request);
			resultInfo.setResponse(listWo);
			resultInfo.setStatusReponse(ResultInfo.STATUS_OK);
			resultInfo.setMessage(SUCCESS_MSG);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			resultInfo.setStatusReponse(ResultInfo.STATUS_NOK);
			resultInfo.setMessage(e.getMessage());
		}
		return resultInfo;
	}

	@POST
	@Path("/apiIoc/approveExtentionWoHcFromIoc")
	public ResultInfo approveExtentionWoHcFromIoc(WoDTO request) {
		ResultInfo resultInfo = new ResultInfo();
		try {
			Long id = woBusinessImpl.approveExtentionWoHcFromIoc(request);
			if (id != 0) {
				resultInfo.setResponse(id);
				resultInfo.setStatusReponse(ResultInfo.STATUS_OK);
				resultInfo.setMessage(SUCCESS_MSG);
			} else {
				resultInfo.setStatusReponse(ResultInfo.STATUS_NOK);
				resultInfo.setMessage(SUCCESS_MSG);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			resultInfo.setStatusReponse(ResultInfo.STATUS_NOK);
			resultInfo.setMessage(e.getMessage());
		}
		return resultInfo;
	}
	// Huy-end

	// Huypq-13062022-start
	@POST
	@Path("/apiIoc/saveSignVoffice")
	public ResultInfo saveSignVoffice(SignVofficeRequestDTO dto) throws Exception {
		ResultInfo resultInfo = new ResultInfo();
		try {
			Long id = signVofficeBusinessImpl.saveSignVoffice(dto.getListSignVoffice());
			if (id != 0) {
				resultInfo.setResponse(id);
				resultInfo.setStatusReponse(ResultInfo.STATUS_OK);
				resultInfo.setMessage(SUCCESS_MSG);
			} else {
				resultInfo.setStatusReponse(ResultInfo.STATUS_NOK);
				resultInfo.setMessage(SUCCESS_MSG);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			resultInfo.setStatusReponse(ResultInfo.STATUS_NOK);
			resultInfo.setMessage(e.getMessage());
		}
		return resultInfo;
	}

	// Huy-end
	// Huypq-13062022-start
	@POST
	@Path("/apiIoc/getDataSignVoffice")
	public ResultInfo getDataSignVoffice(SignVofficeDTO dto) throws Exception {
		ResultInfo resultInfo = new ResultInfo();
		try {
			SignVofficeDTO docSign = signVofficeBusinessImpl.getByObjIdAndType(dto.getObjectId(), dto.getType());
			if (docSign != null) {
				resultInfo.setResponse(docSign);
				resultInfo.setStatusReponse(ResultInfo.STATUS_OK);
				resultInfo.setMessage(SUCCESS_MSG);
			} else {
				resultInfo.setStatusReponse(ResultInfo.STATUS_NOK);
				resultInfo.setMessage(SUCCESS_MSG);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			resultInfo.setStatusReponse(ResultInfo.STATUS_NOK);
			resultInfo.setMessage(e.getMessage());
		}
		return resultInfo;
	}
	// Huy-end

	// Huypq-13062022-start
	@POST
	@Path("/apiIoc/getUserSignContract")
	public ResultInfo getUserSignContract(SignVofficeDTO dto) throws Exception {
		ResultInfo resultInfo = new ResultInfo();
		try {
			SignVofficeDTO docSign = signVofficeBusinessImpl.getUserSignContract(dto.getObjectId(), dto.getType());
			if (docSign != null) {
				resultInfo.setResponse(docSign);
				resultInfo.setStatusReponse(ResultInfo.STATUS_OK);
				resultInfo.setMessage(SUCCESS_MSG);
			} else {
				resultInfo.setStatusReponse(ResultInfo.STATUS_NOK);
				resultInfo.setMessage(SUCCESS_MSG);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			resultInfo.setStatusReponse(ResultInfo.STATUS_NOK);
			resultInfo.setMessage(e.getMessage());
		}
		return resultInfo;
	}
	// Huy-end
	
	//Huypq-05102022-start
	@POST
	@Path("/apiIoc/getValueByConstructionFromPmxl/")
	public ConstructionTaskDTOResponse getValueByConstructionFromPmxl() {
		ConstructionTaskDTOResponse response = new ConstructionTaskDTOResponse();
		try {
			List<ConstructionTotalValueDTO> data = constructionTaskBusiness.getValueByConstructionFromPmxl();
			response.setLstConstructionTotalValue(data);
			ResultInfo resultInfo = new ResultInfo();
			resultInfo.setStatus(ResultInfo.RESULT_OK);
			response.setResultInfo(resultInfo);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			ResultInfo resultInfo = new ResultInfo();
			resultInfo.setStatus(ResultInfo.RESULT_NOK);
			resultInfo.setMessage(e.getMessage());
			response.setResultInfo(resultInfo);
		}
		return response;
	}
	//Huy-end
}
