package com.viettel.coms.rest;

import com.viettel.utils.Constant;
import com.viettel.utils.ConvertData;
import com.viettel.utils.Constant.AdResourceKey;
import com.viettel.wms.business.UserRoleBusinessImpl;

import java.io.InputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.activation.DataHandler;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Null;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.common.collect.Lists;
import com.viettel.asset.dto.ResultInfo;
import com.viettel.coms.business.ComplainOrderRequestDetailLogHistoryBusinessImpl;
import com.viettel.coms.business.ComplainOrdersRequestBusinessImpl;
import com.viettel.coms.dao.ComplainOrderRequestDAO;
import com.viettel.coms.dto.AppParamDTO;
import com.viettel.coms.dto.ComplainOrderRequestDTO;
import com.viettel.coms.dto.ComplainOrderRequestDetailLogHistoryDTO;
import com.viettel.coms.dto.ComplainOrderRequestResponse;
import com.viettel.coms.dto.SysUserCOMSDTO;
import com.viettel.coms.utils.CallApiUtils;
import com.viettel.ktts.vps.VpsPermissionChecker;
import com.viettel.ktts2.common.BusinessException;
import com.viettel.ktts2.common.UFile;
import com.viettel.ktts2.common.UString;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.dto.DataListDTO;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class ComplainOrdersRequestRsServiceImpl implements ComplainOrdersRequestRsService {

	protected final Logger LOGGER = Logger.getLogger(ComplainOrdersRequestRsService.class);

	@Autowired
	private ComplainOrdersRequestBusinessImpl complainOrdersRequestBusinessImpl;

	@Autowired
	private ComplainOrderRequestDetailLogHistoryBusinessImpl complainOrderRequestDetailLogHistoryBusinessImpl;

	@Autowired
	private UserRoleBusinessImpl userRoleBusinessImpl;
	@Autowired
	private ComplainOrderRequestDAO complainOrderRequestDAO;

	@Context
	private HttpServletRequest request;

	@Value("${url_crm_service}")
	private String url_crm_service;
	@Value("${url_cim_service}")
	private String url_cim_service;
	

	private final String SUCCESS_MESSAGE = "COMS cập nhật dữ liệu thành công. Đang gửi/đồng bộ dữ liệu sang CRM";
	private final String BAD_MESSAGE = "COMS nhận/cập nhật dữ liệu thất bại. Đang gửi/đồng bộ dữ liệu sang CRM";
	private final String COMS_SUCCESS_MESSAGE = "COMS cập nhật dữ liệu thành công.";
	private final String STATUS_OK = "OK";
	private final String STATUS_NOK = "NOT_OK";

	@Override
	public Response doSearch(ComplainOrderRequestDTO obj,  HttpServletRequest request) throws Exception,ParseException {
		if(obj.getIsFirst()==0l) {
			Boolean checkRoleTTHTView = complainOrdersRequestBusinessImpl.checkRoleTTHTView(request);
			if(checkRoleTTHTView == true)	obj.setCheckRoleTTHTView(1l);
			else obj.setCheckRoleTTHTView(0l);
			
			Boolean checkRoleCSKH = complainOrdersRequestBusinessImpl.checkRoleCSKH(request);
			if (checkRoleCSKH==true) obj.setCheckRoleCSKH(1l);
			else obj.setCheckRoleCSKH(0l);
				
			List<String> groupIdList = new ArrayList<>();
	        Boolean isRoleViewData = VpsPermissionChecker.hasPermission(Constant.OperationKey.VIEW, Constant.AdResourceKey.DATA, request);
	        if (isRoleViewData==true) {
	        	obj.setCheckRoleViewData(1l);
	        	String provinceId = VpsPermissionChecker.getDomainDataItemIds(Constant.OperationKey.VIEW,
	    		Constant.AdResourceKey.DATA, request);
	        	groupIdList = ConvertData.convertStringToList(provinceId, ",");
	        }else obj.setCheckRoleViewData(0l);
	        obj.setProvinceViewData(groupIdList);
	        
//	        groupIdList = new ArrayList<>();
//	        Long checkRoleDeployTicket = complainOrdersRequestBusinessImpl.checkRoleDeployTicket(obj,request);
//	        if(checkRoleDeployTicket ==1l) {
//	        	obj.setCheckRoleDeployTicket(checkRoleDeployTicket);
//		        groupIdList = ConvertData.convertStringToList(obj.getSysGroupId().toString(), ",");
//	        }else obj.setCheckRoleDeployTicket(0l);
//	        obj.setProvinceViewDeploy(groupIdList);
		}
		List<ComplainOrderRequestDTO> ls = complainOrdersRequestBusinessImpl.doSearch(obj);
		if (ls == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		} else {
			DataListDTO data = new DataListDTO();
			if(ls.size() > 0)	{
				data.setData(ls);
				data.setTotal(obj.getTotalRecord());
			}
			else{
				data.setData(ls);
				data.setTotal(0);
			}
			data.setSize(obj.getPageSize());
			data.setStart(1);
			
			ComplainOrderRequestDTO res = new ComplainOrderRequestDTO();
			res.setDataListDTO(data);
			res.setIsFirst(obj.getIsFirst());
			res.setCheckRoleTTHTView(obj.getCheckRoleTTHTView());
			res.setCheckRoleCSKH(obj.getCheckRoleCSKH());
			res.setCheckRoleViewData(obj.getCheckRoleViewData());
			res.setProvinceViewData(obj.getProvinceViewData());
			res.setCheckRoleDeployTicket(obj.getCheckRoleDeployTicket());
			res.setProvinceViewDeploy(obj.getProvinceViewDeploy());
			return Response.ok(res).build();
		}

	}

//	@Override
//	public Response add(ComplainOrderRequestDetailLogHistoryDTO obj) throws Exception {
//		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//		ComplainOrderRequestDTO complainOrderRequestDTO = obj.getComplainOrderRequestDTO();
//		try {
//			complainOrderRequestDTO
//					.setCreateDateString(simpleDateFormat.format(complainOrderRequestDTO.getCreateDate()));
//			complainOrderRequestDTO.setCreateUser(objUser.getVpsUserInfo().getEmployeeCode());
//			complainOrderRequestDTO.setStatus(1l);
//			complainOrderRequestDTO.setPerformerName(objUser.getVpsUserInfo().getEmployeeCode());
//			Calendar calCreate = Calendar.getInstance();
//			calCreate.setTime(complainOrderRequestDTO.getCreateDate());
//			Calendar calGoal = Calendar.getInstance();
//			calGoal.setTime(new Date());
//			calGoal.set(Calendar.HOUR_OF_DAY, 17);
//			calGoal.set(Calendar.MINUTE, 0);
//			calGoal.set(Calendar.SECOND, 0);
//			calGoal.set(Calendar.MILLISECOND, 0);
//			if (calCreate.before(calGoal) == true) {
//				complainOrderRequestDTO.setIsNext(0l);
//				calCreate.add(Calendar.DAY_OF_MONTH, 1);
//				complainOrderRequestDTO.setCompletedTimeExpected(calCreate.getTime());
//			} else {
//				complainOrderRequestDTO.setIsNext(1l);
//				calGoal.add(Calendar.DAY_OF_MONTH, 2);
//				calGoal.set(Calendar.HOUR_OF_DAY, 8);
//				calGoal.set(Calendar.MINUTE, 0);
//				calGoal.set(Calendar.SECOND, 0);
//				calGoal.set(Calendar.MILLISECOND, 0);
//				complainOrderRequestDTO.setCompletedTimeExpected(calGoal.getTime());
//
//			}
//			Long complainId = complainOrdersRequestBusinessImpl.add(complainOrderRequestDTO, request);
//			if (complainId == 0l) {
//				return Response.status(Response.Status.BAD_REQUEST).build();
//			} else {
//				obj.setAction("CREATE: Tạo mới ticket");
//				obj.setStatus(1l);
//				obj.setCreateDate(new Timestamp(System.currentTimeMillis()));
//				obj.setCreateUser(objUser.getVpsUserInfo().getEmployeeCode().toString());
////				obj.setComplainOrderRequestId(complainId);
//				obj.setComplainOrderRequestId(complainOrderRequestDTO.getComplainOrderRequestId());
//				Long detaiId = complainOrderRequestDetailLogHistoryBusinessImpl.add(obj, request);
//				if (detaiId == 0l) {
//					return Response.status(Response.Status.BAD_REQUEST).build();
//				}
//				return Response.status(Response.Status.OK).build();
//			}
//		} catch (Exception e) {
//			return Response.ok().entity(Collections.singletonMap("error", e.getMessage())).build();
//		}
//	}

	@Override
	public Response update(ComplainOrderRequestDetailLogHistoryDTO obj) throws Exception {
		// TODO Auto-generated method stub
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		ObjectMapper mapper = new ObjectMapper();
		ComplainOrderRequestDTO complainOrderRequestDTO = obj.getComplainOrderRequestDTO();
//		String linkCall = url_crm_service + "/misv.php?com=api&elem=complain&func=UpdateTicketPMXL";
		String linkCall = url_cim_service + "/ticket/syncFromCOMS";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date completedTimeReal = new Timestamp(System.currentTimeMillis());
		//Bat validate 
		ComplainOrderRequestDetailLogHistoryDTO tmp = new ComplainOrderRequestDetailLogHistoryDTO();
		tmp.setComplainOrderRequestId(complainOrderRequestDTO.getComplainOrderRequestId());
		tmp.setPage(1l);
		tmp.setStatus(complainOrderRequestDTO.getStatus());
		try {
			
			List<ComplainOrderRequestDetailLogHistoryDTO> listLogFillterFollowStatus = complainOrderRequestDetailLogHistoryBusinessImpl.doSearch(tmp);
			List<ComplainOrderRequestDetailLogHistoryDTO> lstActionGiahan = new ArrayList<>();
			for(ComplainOrderRequestDetailLogHistoryDTO comp : listLogFillterFollowStatus) {
				if(StringUtils.isNotBlank(comp.getAction()) && comp.getAction().contains("Gia hạn ticket")) {
					lstActionGiahan.add(comp);
				}
			}
			obj.setStatus(complainOrderRequestDTO.getStatus());
			if (obj.getStatus() == 2l ){
				if(obj.getExtendDate()!=null) {
	//				Stream stream = listLogFillterFollowStatus.stream().filter(t -> t.getAction().contains("Gia hạn ticket") == true);
					if(lstActionGiahan.size() >= 2) {
						throw new BusinessException("Ticket được chọn đã được gia hạn 2 lần, không thể gia hạn thêm.");
					}else {
						obj.setTimes((long)lstActionGiahan.size() + 1);
						obj.setAction("Gia hạn ticket");
						obj.setExtendDate(simpleDateFormat.parse(complainOrderRequestDTO.getCompletedTimeExpectedString()));
						complainOrderRequestDTO.setActionLast("EXTEND");
					}
					
				}else if(obj.getExtendDate()==null){
					if(listLogFillterFollowStatus.size() == 0) {
						obj.setAction("UPDATE: Tiếp nhận ticket");
						complainOrderRequestDTO.setActionLast("RECEIVE");
					}
	//				else {
	//					Stream stream = listLogFillterFollowStatus.stream().filter(t -> t.getAction().contains("Trả lại ticket") == true);
	//					obj.setTimes(stream.count()+1);
	//					obj.setAction("Trả lại ticket lần " + obj.getTimes() + "");
	//					complainOrderRequestDTO.setActionLast("DONE_AGAIN");
	//				}
				}
			}else if (obj.getStatus() == 3l) {
				obj.setAction("UPDATE: Hoàn thành ticket");
				complainOrderRequestDTO.setIsTrace(1L);
				complainOrderRequestDTO.setActionLast("FINISH");
			}
		

			complainOrderRequestDTO.setUpdateDate(new Timestamp(System.currentTimeMillis()));
			complainOrderRequestDTO.setUpdateUser(objUser.getVpsUserInfo().getSysUserId().toString());
			JSONObject personJsonObject = new JSONObject();
			if(complainOrderRequestDTO.getReceivedDate() == null) {
				personJsonObject.put("status", complainOrderRequestDTO.getStatus());
				personJsonObject.put("ticketId", complainOrderRequestDTO.getComplainOrderRequestId());
				personJsonObject.put("updateUser", objUser.getVpsUserInfo().getFullName()+ " - "+ objUser.getVpsUserInfo().getEmployeeCode());
				personJsonObject.put("performerName",complainOrderRequestDTO.getPerformerName());
//				personJsonObject.put("performerName", complainOrderRequestDTO.getPerformerfullName()+ " - "+ complainOrderRequestDTO.getPerformerName());
			}else {
				if(complainOrderRequestDTO.getStatus() == 2l) {
					personJsonObject.put("ticketId", complainOrderRequestDTO.getComplainOrderRequestId());
//					personJsonObject.put("status", complainOrderRequestDTO.getStatus());
					personJsonObject.put("completedTimeExpected", complainOrderRequestDTO.getCompletedTimeExpectedString());
					personJsonObject.put("updateUser", objUser.getVpsUserInfo().getFullName()+ " - "+ objUser.getVpsUserInfo().getEmployeeCode());
					if(obj.getNote()==null) {
						personJsonObject.put("reason", obj.getReason());
						personJsonObject.put("reasonExtension", obj.getReason());
					}else {
						personJsonObject.put("reason", obj.getReason());
						personJsonObject.put("reasonExtension", obj.getReason());
						personJsonObject.put("reasonExtensionDetail", obj.getNote());
//						personJsonObject.put("reason", obj.getReason()+" - "+obj.getNote());
					}
				}else if(complainOrderRequestDTO.getStatus() == 3l){
					personJsonObject.put("status", complainOrderRequestDTO.getStatus());
					personJsonObject.put("ticketId", complainOrderRequestDTO.getComplainOrderRequestId());
					personJsonObject.put("updateUser", objUser.getVpsUserInfo().getFullName()+ " - "+ objUser.getVpsUserInfo().getEmployeeCode());
					personJsonObject.put("completedTimeReal", simpleDateFormat.format(completedTimeReal));
					if(obj.getNote()==null) {
						personJsonObject.put("overallReason", obj.getReason());
					}else {
						personJsonObject.put("overallReason", obj.getReason());
						personJsonObject.put("detailedReason", obj.getNote());
					}
				}
			}
			if (complainOrderRequestDTO.getStatus() == 2l) {
				if(complainOrderRequestDTO.getReceivedDate() == null)	complainOrderRequestDTO.setReceivedDate(new Timestamp(System.currentTimeMillis()));
				else 	complainOrderRequestDTO.setReceivedDate(simpleDateFormat.parse(complainOrderRequestDTO.getReceivedDateString()));
				complainOrderRequestDTO.setCompletedTimeExpected(simpleDateFormat.parse(complainOrderRequestDTO.getCompletedTimeExpectedString()));
				complainOrderRequestDTO.setCompletedTimeReal(null);
			} else if (complainOrderRequestDTO.getStatus() == 3l) {
				complainOrderRequestDTO.setCompletedTimeReal(completedTimeReal);
				complainOrderRequestDTO.setCompletedTimeRealString(simpleDateFormat.format(completedTimeReal));
			}
			
			// validate bat reponse tra ve
			String responseCallApiCrm = CallApiUtils.callApiResponseTicketString(linkCall, personJsonObject);
			
			 ResultInfo result = null;ComplainOrderRequestResponse response = new ComplainOrderRequestResponse();
		     result = mapper.readValue(responseCallApiCrm, ResultInfo.class);
			// validate bat reponse tra ve
			if (result.getStatus().equals("200") && result.getType().equals("success")) {
				
				Long id = complainOrderRequestDAO.updateComplainOrderRequest(complainOrderRequestDTO);
				if (id == 1l) {
					obj.setCreateDate(new Timestamp(System.currentTimeMillis()));
					obj.setCreateUser(objUser.getVpsUserInfo().getEmployeeCode().toString());
					obj.setComplainOrderRequestId(complainOrderRequestDTO.getComplainOrderRequestId());
					Long detaiId = complainOrderRequestDetailLogHistoryBusinessImpl.add(obj, request);
					if (detaiId != 0l) {
						ResultInfo resultInfo = new ResultInfo(); resultInfo.setType("success");
						resultInfo.setStatus(ResultInfo.RESULT_OK);
				        resultInfo.setMessage(COMS_SUCCESS_MESSAGE);
				        response.setResultInfo(resultInfo);	
					}	
				}
				return Response.ok(response).build();
			}else if (result.getStatus().equals("400")) {
				throw new BusinessException(result.getMessage());
				
			}
//			else if (result.getStatus().equals("400") && result.getType().equals("error")) {
//				
//				throw new BusinessException("Không tìm thấy server.");
//				
//			}
			else {
				throw new BusinessException("Đang nhận trạng thái phản hồi từ CIM trả về..... COMS nhận trạng thái thất bại.");
			}
		}catch (BusinessException e) {
			ComplainOrderRequestResponse response = new ComplainOrderRequestResponse();
			ResultInfo resultInfo = new ResultInfo(); resultInfo.setType("error");
            resultInfo.setStatus(ResultInfo.RESULT_NOK);resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
            return Response.ok(response).build();
        }catch (Exception e) {
        	ComplainOrderRequestResponse response = new ComplainOrderRequestResponse();
			ResultInfo resultInfo = new ResultInfo();resultInfo.setType("error");
            resultInfo.setStatus(ResultInfo.RESULT_NOK);resultInfo.setMessage("Có lỗi xảy ra khi lưu/cập nhật dữ liệu  bên phầm mềm COMS");
            response.setResultInfo(resultInfo);
            return Response.ok(response).build();
		}
		
	}

	

	@Override
	public Response getListPerformer(SysUserCOMSDTO sysUser) {
		// TODO Auto-generated method stub
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		List<SysUserCOMSDTO> lstUser = complainOrdersRequestBusinessImpl.getListPerformer(sysUser);
		if (lstUser == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		} else {
			DataListDTO data = new DataListDTO();
			data.setData(lstUser);
			data.setTotal(sysUser.getTotalRecord());
			data.setSize(sysUser.getPageSize());
			data.setStart(1);
			return Response.ok(data).build();
		}
	}

	@SuppressWarnings("static-access")
	@Override
	public Response choosePerformer(ComplainOrderRequestDetailLogHistoryDTO obj) {
		ObjectMapper mapper = new ObjectMapper();
		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
		ComplainOrderRequestDTO complainOrderRequestDTO = obj.getComplainOrderRequestDTO();
		complainOrderRequestDTO.setListId(Lists.newArrayList());
		for (AppParamDTO o : complainOrderRequestDTO.getListPerform()) {
			complainOrderRequestDTO.getListId().add(o.getAppParamId());
		}
//		String linkCall = url_crm_service + "/misv.php?com=api&elem=complain&func=UpdateTicketPMXL";
		String linkCall = url_cim_service + "/ticket/syncFromCOMS";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try {
			complainOrderRequestDTO.setUpdateDate(new Timestamp(System.currentTimeMillis()));
			complainOrderRequestDTO.setUpdateUser(objUser.getVpsUserInfo().getEmployeeCode().toString());
			complainOrderRequestDTO.setActionLast("DEPLOY");
			complainOrderRequestDTO.setIsTrace(1l);
			JSONObject personJsonObject = new JSONObject();
			personJsonObject.put("performerAll", "performerAll");
			personJsonObject.put("listId", complainOrderRequestDTO.getListId());
			personJsonObject.put("performerName", complainOrderRequestDTO.getPerformerId());
			personJsonObject.put("updateUser", objUser.getVpsUserInfo().getFullName()+ " - "+ objUser.getVpsUserInfo().getEmployeeCode());
			
			String responseCallApiCrm = CallApiUtils.callApiResponseTicketString(linkCall, personJsonObject);
			ResultInfo resultReponse = null;ComplainOrderRequestResponse response = new ComplainOrderRequestResponse();
			resultReponse = mapper.readValue(responseCallApiCrm, ResultInfo.class);
			
			if (resultReponse.getStatus().equals("200") && resultReponse.getType().equals("success")) {
				
				Long complainId = complainOrdersRequestBusinessImpl.choosePerformer(complainOrderRequestDTO);
				
				if (complainId == 1l) {
					for (AppParamDTO o : complainOrderRequestDTO.getListPerform()) {
						ComplainOrderRequestDetailLogHistoryDTO result = new ComplainOrderRequestDetailLogHistoryDTO();
						result.setComplainOrderRequestId(o.getAppParamId());
						result.setStatus(Long.parseLong(o.getStatus()));
						List<ComplainOrderRequestDetailLogHistoryDTO> listLogFillterFollowStatus = complainOrderRequestDetailLogHistoryBusinessImpl.doSearch(result);
//						Stream stream = listLogFillterFollowStatus.stream().filter(t -> t.getAction().contains("Điều phối nhân sự ticket") == true);
						List<ComplainOrderRequestDetailLogHistoryDTO> lstDieuphoi = new ArrayList<>();
						for(ComplainOrderRequestDetailLogHistoryDTO compl : listLogFillterFollowStatus) {
							if(StringUtils.isNotBlank(compl.getAction()) && compl.getAction().contains("Điều phối nhân sự ticket")) {
								lstDieuphoi.add(compl);
							}
						}
						result.setTimes((long)lstDieuphoi.size()+1);
						result.setAction("UPDATE: Điều phối nhân sự ticket lần " + result.getTimes() + "");
						result.setCreateDate(new Timestamp(System.currentTimeMillis()));
						result.setCreateUser(objUser.getVpsUserInfo().getEmployeeCode().toString());
						Long detaiId = complainOrderRequestDetailLogHistoryBusinessImpl.add(result, request);
						if (detaiId != 0l) {
							ResultInfo resultInfo = new ResultInfo(); resultInfo.setType("success");
							resultInfo.setStatus(ResultInfo.RESULT_OK);
					        resultInfo.setMessage(COMS_SUCCESS_MESSAGE);
					        response.setResultInfo(resultInfo);
							
						}
					}
					
				}
				return Response.ok(response).build();
			}else if (resultReponse.getStatus().equals("400")) {
				 throw new BusinessException(resultReponse.getMessage());
			}
//			else if (resultReponse.getStatus().equals("404") && resultReponse.getType().equals("error")) {
//				throw new BusinessException("Không tìm thấy server.");			
//			}
			else {
				throw new BusinessException("Đang nhận trạng thái phản hồi từ CIM trả về..... COMS nhận trạng thái thất bại.");
			}
			
			
		}catch (BusinessException e) {
			ComplainOrderRequestResponse response = new ComplainOrderRequestResponse();
			ResultInfo resultInfo = new ResultInfo();resultInfo.setType("error");
            resultInfo.setStatus(ResultInfo.RESULT_NOK);resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
            return Response.ok(response).build();
        }catch (Exception e) {
        	ComplainOrderRequestResponse response = new ComplainOrderRequestResponse();
			ResultInfo resultInfo = new ResultInfo();resultInfo.setType("error");
            resultInfo.setStatus(ResultInfo.RESULT_NOK);resultInfo.setMessage("Có lỗi xảy ra khi lưu/cập nhật dữ liệu bên phầm mềm COMS ");
            response.setResultInfo(resultInfo);
            return Response.ok(response).build();
		}
	}
	
	@Override
	public Response checkRoleTTHTView(ComplainOrderRequestDTO obj, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		Boolean checkRoleTTHTView = complainOrdersRequestBusinessImpl.checkRoleTTHTView(request);
		if (checkRoleTTHTView) {
			return Response.ok(Status.OK).build();
		}
		return Response.ok().build();
	}
	
	@Override
	public Response checkRoleDeployTicket(ComplainOrderRequestDTO obj, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		Long checkRoleDeployTicket = complainOrdersRequestBusinessImpl.checkRoleDeployTicket(obj,request);
		if (checkRoleDeployTicket == 1l) {
			return Response.ok(Status.OK).build();
		}
		return Response.ok().build();
	}
}
