package com.viettel.coms.webservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.viettel.asset.business.AuthenticateWsBusiness;
import com.viettel.asset.dto.ResultInfo;
import com.viettel.coms.business.GoodsPlanBusinessImpl;
import com.viettel.coms.dto.HcqtDTORequest;
import com.viettel.coms.dto.HcqtDTOResponse;
import com.viettel.erp.business.CatEmployeeBusinessImpl;
import com.viettel.erp.business.ConstrGroundHandoverBusinessImpl;
import com.viettel.erp.business.ConstrWorkLogsBusinessImpl;
import com.viettel.erp.business.ConstrWorkLogsLabelBusinessImpl;
import com.viettel.erp.business.EstimatesWorkItemsBusinessImpl;
import com.viettel.erp.business.MonitorMissionAssignBusinessImpl;
import com.viettel.erp.business.UtilAttachedDocumentsBusinessImpl;
import com.viettel.erp.dto.CatFileInvoiceDTO;
import com.viettel.erp.dto.ConstrCompleteRecordsMapDTO;
import com.viettel.erp.dto.ConstrGroundHandoverDTO;
import com.viettel.erp.dto.ConstrWorkLogsDTO;
import com.viettel.erp.dto.EstimatesWorkItemsDTO;
import com.viettel.erp.dto.MonitorMissionAssignDTO;
import com.viettel.erp.dto.RoleConfigProvinceUserCDTDTO;
import com.viettel.erp.dto.RoleConfigProvinceUserCTCTDTO;
import com.viettel.erp.dto.SettlementRightDTO;
import com.viettel.erp.dto.VConstructionHcqtDTO;
import com.viettel.ktts2.common.UEncrypt;

@Consumes({ MediaType.APPLICATION_JSON + ";charset=utf-8", MediaType.APPLICATION_XML })
@Produces({ MediaType.APPLICATION_JSON + ";charset=utf-8", MediaType.APPLICATION_XML })
@Path("/service")
public class HcqtWsRsService {

	private Logger LOGGER = Logger.getLogger(HcqtWsRsService.class);

	@Autowired
	private GoodsPlanBusinessImpl goodsPlanBusinessImpl;

	@Autowired
	MonitorMissionAssignBusinessImpl monitorMissionAssignBusinessImpl;

	@Autowired
	ConstrGroundHandoverBusinessImpl constrGroundHandoverBusinessImpl;

	@Autowired
	AuthenticateWsBusiness authenticateWsBusiness;

	@Autowired
	ConstrWorkLogsBusinessImpl constrWorkLogsBusinessImpl;

	@Autowired
	ConstrWorkLogsLabelBusinessImpl constrWorkLogsLabelBusinessImpl;
	
	@Autowired
	CatEmployeeBusinessImpl catEmployeeBusinessImpl;
	
	@Autowired
	UtilAttachedDocumentsBusinessImpl utilAttachedDocumentsBusinessImpl;
	
	@Autowired
	EstimatesWorkItemsBusinessImpl estimatesWorkItemsBusinessImpl;
	
	@Value("${monitorMissionAssign.attachType}")
	private Long attachTypeVal;

	// L???y danh s??ch c??ng tr??nh theo t???nh
	@POST
	@Path("/getAllConstructionHcqt")
	public HcqtDTOResponse getAllConstructionHcqt(HcqtDTORequest request) {
		HcqtDTOResponse response = new HcqtDTOResponse();
		try {
			authenticateWsBusiness.validateRequest(request.getSysUserRequest());
			List<VConstructionHcqtDTO> data = goodsPlanBusinessImpl
					.getAllConstructionHcqt(Long.parseLong(request.getSysUserRequest().getSysGroupId()));
			response.setLstConstructionDTO(data);
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

	// L???y chi ti???t theo id c??ng tr??nh
	@POST
	@Path("/getDetailConstrById")
	public HcqtDTOResponse getDetailConstrById(HcqtDTORequest request) {
		HcqtDTOResponse response = new HcqtDTOResponse();
		try {
			authenticateWsBusiness.validateRequest(request.getSysUserRequest());
			// L???y list data Giao nhi???m v??? gi??m s??t
			List<MonitorMissionAssignDTO> dataMonitor = goodsPlanBusinessImpl
					.getMonitorMissionAssignByConstrId(request.getvConstructionHcqtDTO());
			// L???y list data B??n giao m???t b???ng thi c??ng
			List<ConstrGroundHandoverDTO> dataBGMB = goodsPlanBusinessImpl
					.getAllConstrGroundHandover(request.getvConstructionHcqtDTO());
			// L???y list data Nh???t k?? c??ng tr??nh
			List<ConstrWorkLogsDTO> dataNK = goodsPlanBusinessImpl
					.getAllConstrWorkLogs(request.getvConstructionHcqtDTO());
			response.setLstMonitorMissionAssign(dataMonitor);
			response.setLstConstrGroundHandover(dataBGMB);
			response.setLstConstrWorkLogs(dataNK);
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

	// HienLT56 start 17082020
	// Th??m m???i c??ng tr??nh v???i danh s??ch c??ng tr??nh ch??a t???n t???i 1 trong 3 b???ng
	// @POST
	// @Path("/addNewConstruction")
	// public HcqtDTOResponse addNewConstruction(HcqtDTORequest request) {
	// HcqtDTOResponse response = new HcqtDTOResponse();
	// try {
	// authenticateWsBusiness.validateRequest(request.getSysUserRequest());
	// List<VConstructionHcqtDTO> data = goodsPlanBusinessImpl
	// .addNewConstruction(Long.parseLong(request.getSysUserRequest().getSysGroupId()));
	// response.setLstConstructionDTO(data);
	// ResultInfo resultInfo = new ResultInfo();
	// resultInfo.setStatus(ResultInfo.RESULT_OK);
	// response.setResultInfo(resultInfo);
	// } catch (Exception e) {
	// LOGGER.error(e.getMessage(), e);
	// ResultInfo resultInfo = new ResultInfo();
	// resultInfo.setStatus(ResultInfo.RESULT_NOK);
	// resultInfo.setMessage(e.getMessage());
	// response.setResultInfo(resultInfo);
	// }
	// return response;
	// }

	// Th??m m???i GNVGS
	@POST
	@Path("/addMonitorMissionAssign")
	public HcqtDTOResponse addMonitorMissionAssign(HcqtDTORequest request) {
		HcqtDTOResponse response = new HcqtDTOResponse();
		String documentPath = "";
		try {
			authenticateWsBusiness.validateRequest(request.getSysUserRequest());
			String code = goodsPlanBusinessImpl.autoGenCode();
			request.getMonitorMissionAssignDTO().setCreatedDate(new Date());
			request.getMonitorMissionAssignDTO().setCode(code);
			if(request.getMonitorMissionAssignDTO().getDocumentPath() != null) {
				documentPath = UEncrypt.decryptFileUploadPath(request.getMonitorMissionAssignDTO().getDocumentPath());		
			}
			if(request.getMonitorMissionAssignDTO().getDocumentName() != null) {
				request.getMonitorMissionAssignDTO().setDocumentName(request.getMonitorMissionAssignDTO().getDocumentName());
			} else {
				request.getMonitorMissionAssignDTO().setDocumentName("");
			}
			request.getMonitorMissionAssignDTO().setStatusCa(0L);
			request.getMonitorMissionAssignDTO().setIsActive(1l);
			request.getMonitorMissionAssignDTO().setCreatedUserId(request.getSysUserRequest().getSysUserId());
			CatFileInvoiceDTO catInvoice = goodsPlanBusinessImpl.onlyFindByTableName("MONITOR_MISSION_ASSIGN");
			Long catFileInvoiceId = catInvoice.getCatFileInvoiceId();
			ConstrCompleteRecordsMapDTO constrCompleteRecordMap = new ConstrCompleteRecordsMapDTO();
			constrCompleteRecordMap.setDataTableName("MONITOR_MISSION_ASSIGN");
			constrCompleteRecordMap.setDataTableId("MONITOR_MISSION_ASSIGN_ID");
			constrCompleteRecordMap.setCode(code);
			constrCompleteRecordMap.setCreatedDate(new Date());
			constrCompleteRecordMap.setStatus(0L);
			constrCompleteRecordMap.setLevelOrder(1l);
			constrCompleteRecordMap.setCreatedUserId(request.getMonitorMissionAssignDTO().getCreatedUserId());
			constrCompleteRecordMap.setConstructionId(request.getMonitorMissionAssignDTO().getConstructId());
			constrCompleteRecordMap.setCatFileInvoiceId(catFileInvoiceId);
			request.getMonitorMissionAssignDTO().setConstrCompleteRecordMap(constrCompleteRecordMap);
			Long id = goodsPlanBusinessImpl.saveTable(request.getMonitorMissionAssignDTO());
			utilAttachedDocumentsBusinessImpl.insert(request.getMonitorMissionAssignDTO().getDocumentName(), id, documentPath, attachTypeVal);
			ResultInfo resultInfo = new ResultInfo();
			if (id == 0l) {
				resultInfo.setStatus(ResultInfo.RESULT_NOK);
			} else {
				resultInfo.setStatus(ResultInfo.RESULT_OK);
			}
			// response.setLstConstructionDTO(data);
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

	// Update GNVGS
	@POST
	@Path("/updateMonitorMissionAssign")
	public HcqtDTOResponse updateMonitorMissionAssign(HcqtDTORequest request){
		HcqtDTOResponse response = new HcqtDTOResponse();
		String documentPath = "";
		try {
			authenticateWsBusiness.validateRequest(request.getSysUserRequest());
			request.getMonitorMissionAssignDTO().setIsActive(1L);
			if(request.getMonitorMissionAssignDTO().getDocumentPath() != null) {
				documentPath = UEncrypt.decryptFileUploadPath(request.getMonitorMissionAssignDTO().getDocumentPath());
			}
			if(request.getMonitorMissionAssignDTO().getDocumentName() != null) {
				request.getMonitorMissionAssignDTO().setDocumentName(request.getMonitorMissionAssignDTO().getDocumentName());
			} else {
				request.getMonitorMissionAssignDTO().setDocumentName("");
			}
			request.getMonitorMissionAssignDTO().setStatusCa(0L);
			Long id = monitorMissionAssignBusinessImpl.update(request.getMonitorMissionAssignDTO());
			utilAttachedDocumentsBusinessImpl.updateUtilByParentIdAndType(request.getMonitorMissionAssignDTO().getDocumentName(), request.getMonitorMissionAssignDTO().getMonitorMissionAssignId(), documentPath, attachTypeVal);
			if (request.getMonitorMissionAssignDTO().getStatusCa() == 0L) {
				goodsPlanBusinessImpl.getUpdateConstrCompleteRecod(
						request.getMonitorMissionAssignDTO().getMonitorMissionAssignId(), "MONITOR_MISSION_ASSIGN");
			}
			ResultInfo resultInfo = new ResultInfo();
			if (id == 0l) {
				resultInfo.setStatus(ResultInfo.RESULT_NOK);
				response.setResultInfo(resultInfo);
			} else {
				resultInfo.setStatus(ResultInfo.RESULT_OK);
				response.setResultInfo(resultInfo);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			ResultInfo resultInfo = new ResultInfo();
			resultInfo.setStatus(ResultInfo.RESULT_NOK);
			resultInfo.setMessage(e.getMessage());
			response.setResultInfo(resultInfo);
		}
		return response;
	}

	// Th??m m???i/Update B??n giao m???t b???ng gi??m s??t thi c??ng
	@POST
	@Path("/addConstrGroundHandover")
	public HcqtDTOResponse addConstrGroundHandover(HcqtDTORequest request) {
		HcqtDTOResponse response = new HcqtDTOResponse();
		ResultInfo resultInfo = new ResultInfo();
		try {
			if (request.getConstrGroundHandoverDTO().getConstrGroundHandoverId() != null
					&& request.getConstrGroundHandoverDTO().getIsActive() == 1) {

				if (request.getConstrGroundHandoverDTO().getStatusCa() == 3L) {
					request.getConstrGroundHandoverDTO().setStatusCa(0l);
					String nameTable = "CONSTR_GROUND_HANDOVER";
					goodsPlanBusinessImpl.updateAprroval(nameTable,
							request.getConstrGroundHandoverDTO().getConstrGroundHandoverId());
				}

				Long id = constrGroundHandoverBusinessImpl.update(request.getConstrGroundHandoverDTO());
				if (id == 0l) {
					resultInfo.setStatus(ResultInfo.RESULT_NOK);
					resultInfo.setMessage("C???p nh???t b???n ghi kh??ng th??nh c??ng!");
					response.setResultInfo(resultInfo);
				} else {
					resultInfo.setStatus(ResultInfo.RESULT_OK);
					resultInfo.setMessage("C???p nh???t b???n ghi th??nh c??ng!");
					response.setResultInfo(resultInfo);
				}
			} else {
				List<ConstrGroundHandoverDTO> list = goodsPlanBusinessImpl
						.getAllConstrGroundHandover(request.getConstrGroundHandoverDTO());
				if (list.size() == 0L) {
					String code = (String) goodsPlanBusinessImpl.getCode("CONSTR_GROUND_HANDOVER", "QLHC_BGMBTC");
					if (StringUtils.isNotEmpty(code)) {
						request.getConstrGroundHandoverDTO().setCode(code);
						request.getConstrGroundHandoverDTO().setCreatedDate(new Date());
						request.getConstrGroundHandoverDTO()
								.setCreatedUserId(request.getSysUserRequest().getSysUserId());
						request.getConstrGroundHandoverDTO().setIsActive(1l);
						request.getConstrGroundHandoverDTO().setStatusCa(0l);
						Long constructionId = request.getConstrGroundHandoverDTO().getConstructId();
						Long id = goodsPlanBusinessImpl.saveConstrGroundHand(request.getConstrGroundHandoverDTO());
						try {
							goodsPlanBusinessImpl.insert(constructionId, "CONSTR_GROUND_HANDOVER",
									"CONSTR_GROUND_HANDOVER_ID", id,
									request.getConstrGroundHandoverDTO().getCreatedUserId(), code);
						} catch (Exception ex) {
							LOGGER.error(ex.getMessage(), ex);
						}
						if (id == 0l) {
							resultInfo.setStatus(ResultInfo.RESULT_NOK);
							resultInfo.setMessage("Th??m m???i b???n ghi kh??ng th??nh c??ng!");
						} else {
							resultInfo.setStatus(ResultInfo.RESULT_OK);
							resultInfo.setMessage("Th??m m???i b???n ghi th??nh c??ng!");
						}
						response.setResultInfo(resultInfo);
					} else {
						resultInfo.setStatus(ResultInfo.RESULT_NOK);
						resultInfo.setMessage("T???o m?? Bi??n b???n b??n giao m???t b???ng thi c??ng kh??ng th??nh c??ng!");
						response.setResultInfo(resultInfo);
					}
				} else {
					resultInfo.setStatus(ResultInfo.RESULT_NOK);
					resultInfo.setMessage("???? t???n t???i 1 b???n ghi, b???n kh??ng th??? th???c hi???n ???????c thao t??c th??m m???i!");
					response.setResultInfo(resultInfo);
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			resultInfo.setStatus(ResultInfo.RESULT_NOK);
			resultInfo.setMessage(e.getMessage());
			response.setResultInfo(resultInfo);
		}
		return response;
	}

	// Th??m m???i/Update Nh???t k?? c??ng tr??nh
	@POST
	@Path("/addConstrWorkLogs")
	public HcqtDTOResponse addConstrWorkLogs(HcqtDTORequest request) {
		HcqtDTOResponse response = new HcqtDTOResponse();
		try {
			authenticateWsBusiness.validateRequest(request.getSysUserRequest());
			Long id = constrWorkLogsBusinessImpl.addConstrWorkLogs(request.getConstrWorkLogsDTO());
			ResultInfo resultInfo = new ResultInfo();
			if (id == 0l) {
				resultInfo.setStatus(ResultInfo.RESULT_NOK);
				response.setResultInfo(resultInfo);
			}
			 else if(id == -1l) {
			 resultInfo.setStatus(ResultInfo.RESULT_NOK);
			 resultInfo.setMessage("B??a nh???t k?? c??ng tr??nh ???? ???????c t???o, kh??ng th???c hi???n thao t??c t???o m???i nh???t k?? c??ng tr??nh!");
			 response.setResultInfo(resultInfo);
			 }
			else {
				resultInfo.setStatus(ResultInfo.RESULT_OK);
				response.setResultInfo(resultInfo);
			}
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
	@Path("/checkExistsConstrWorkLogsLabel")
	public HcqtDTOResponse checkExistsConstrWorkLogsLabel(HcqtDTORequest request) {
		HcqtDTOResponse response = new HcqtDTOResponse();
		try {
			authenticateWsBusiness.validateRequest(request.getSysUserRequest());
			Long id = 0l;
			if (goodsPlanBusinessImpl.checkExistsConstrWorkLogsLabel(request.getConstrWorkLogsDTO())) {
				id = -1l;
			}
			ResultInfo resultInfo = new ResultInfo();
			if (id == -1l) {
				resultInfo.setStatus(ResultInfo.RESULT_NOK);
				resultInfo.setMessage(
						"B??a nh???t k?? c??ng tr??nh ???? ???????c t???o, kh??ng th???c hi???n thao t??c t???o m???i nh???t k?? c??ng tr??nh!");
				response.setResultInfo(resultInfo);
			} else {
				resultInfo.setStatus(ResultInfo.RESULT_OK);
				response.setResultInfo(resultInfo);
			}
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
	// HienLT56 end 17082020
	
	// HienLT56 start 28082020
	@POST
	@Path("/getListEmployee")
	public HcqtDTOResponse getListEmployee(HcqtDTORequest request) {
		HcqtDTOResponse response = new HcqtDTOResponse();
		try {
			authenticateWsBusiness.validateRequest(request.getSysUserRequest());
			List<RoleConfigProvinceUserCDTDTO> lstEmployeeCDT = goodsPlanBusinessImpl.getListEmployeeCDT(request.getvConstructionHcqtDTO().getProvinceCode()); 
			List<RoleConfigProvinceUserCTCTDTO> lstEmployeeCTCT = goodsPlanBusinessImpl.getListEmployeeCTCT(request.getvConstructionHcqtDTO().getProvinceCode());
			response.setLstUserCDTDTO(lstEmployeeCDT);
			response.setLstUserCTCTDTO(lstEmployeeCTCT);
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
	//HienLT56 end 28082020
	
	//HienLT56 start 16092020
	@POST
	@Path("/getContentForLogs")
	public HcqtDTOResponse getContentForLogs(HcqtDTORequest request) {
		HcqtDTOResponse response = new HcqtDTOResponse();
		List<String> lstTemp = new ArrayList<String>();
		try {
			authenticateWsBusiness.validateRequest(request.getSysUserRequest());
			String contentForLogs = "";
			List<EstimatesWorkItemsDTO> ls = estimatesWorkItemsBusinessImpl.doSearchByWorkItems(request.getConstrWorkLogsDTO());
			if(ls.size() == 0) {
				contentForLogs = "";
			} else {
				for(int i = 0; i < ls.size(); i ++) {
					lstTemp.add(ls.get(i).getWorkItemName());
				}
				contentForLogs = StringUtils.join(lstTemp, "; ");
			}
			response.setContentForLogs(contentForLogs);
			ResultInfo resultInfo = new ResultInfo();
			resultInfo.setStatus(ResultInfo.RESULT_OK);
			response.setResultInfo(resultInfo);
		}catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			ResultInfo resultInfo = new ResultInfo();
			resultInfo.setStatus(ResultInfo.RESULT_NOK);
			resultInfo.setMessage(e.getMessage());
			response.setResultInfo(resultInfo);
		}
		return response;
	}
	//HienLT56 end 16092020
}
