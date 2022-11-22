package com.viettel.coms.webservice;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.viettel.asset.business.AuthenticateWsBusiness;
import com.viettel.asset.dto.ResultInfo;
import com.viettel.coms.business.ComplainOrderRequestDetailLogHistoryBusinessImpl;
import com.viettel.coms.business.ComplainOrdersRequestBusinessImpl;
import com.viettel.coms.dao.ComplainOrderRequestDAO;
import com.viettel.coms.dto.ComplainOrderRequestDTO;
import com.viettel.coms.dto.ComplainOrderRequestDetailLogHistoryDTO;
import com.viettel.coms.dto.ComplainOrderRequestResponse;
import com.viettel.coms.utils.CallApiUtils;
import com.viettel.ktts2.common.BusinessException;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.wms.business.UserRoleBusinessImpl;

@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Produces({ MediaType.APPLICATION_JSON + ";charset=utf-8", MediaType.APPLICATION_XML })
@Path("/service")
public class ComplainOrderRequestWsRsService {
	private Logger LOGGER = Logger.getLogger(ComplainOrderRequestWsRsService.class);

	@Autowired
	AuthenticateWsBusiness authenticateWsBusiness;

	@Autowired
	private ComplainOrdersRequestBusinessImpl complainOrdersRequestBusinessImpl;

	@Autowired
	private ComplainOrderRequestDetailLogHistoryBusinessImpl complainOrderRequestDetailLogHistoryBusinessImpl;

	@Autowired
	private ComplainOrderRequestDAO complainOrderRequestDAO;
	

	@Context
	private HttpServletRequest request;

	@Value("${url_crm_service}")
	private String url_crm_service;

	private final String SUCCESS_MESSAGE = "COMS cập nhật dữ liệu thành công.";
	private final String BAD_MESSAGE = "COMS nhận/cập nhật dữ liệu thất bại.";

	private final String STATUS_OK = "OK";
	private final String STATUS_NOK = "NOK";

	@POST
	@Path("/synchronizedTicketCRM")
	public Response synchronizedTicket(ComplainOrderRequestDTO obj) throws Exception {
		ComplainOrderRequestResponse response = new ComplainOrderRequestResponse();
//		ResultInfo resultInfo = new ResultInfo();
//		resultInfo.setStatus(ResultInfo.RESULT_OK);
//        resultInfo.setMessage(SUCCESS_MESSAGE);
//        response.setResultInfo(resultInfo);
//        return Response.ok(response).build();
		// TODO Auto-generated method stub
//		KttsUserSession objUser = (KttsUserSession) request.getSession().getAttribute("kttsUserSession");
//		String linkCall = url_cim_service + "/ticket/syncFromCOMS";
//		String linkCall = url_crm_service + "/misv.php?com=api&elem=complain&func=UpdateTicketPMXL";
		Transaction tx = null;
		Session session = null;
		
		try {
			session = complainOrderRequestDAO.getSessionFactory().openSession();
			ComplainOrderRequestDetailLogHistoryDTO detailLog = new ComplainOrderRequestDetailLogHistoryDTO();
			ComplainOrderRequestDTO find = complainOrderRequestDAO.getByTicketCode(obj);
			if(null != find) {
				 if (obj.getType() != null && obj.getType().equals("Edit") == true) {
					tx = session.beginTransaction();
					ResultInfo resultInfo = new ResultInfo();
//					ComplainOrderRequestDTO compare = complainOrderRequestDAO.getById(obj.getTicketId());
					if(find.getStatus() != obj.getStatus()) {
						detailLog.setComplainOrderRequestId(obj.getComplainOrderRequestId());
						detailLog.setStatus(obj.getStatus());
						List<ComplainOrderRequestDetailLogHistoryDTO> listLogFillterFollowStatus = complainOrderRequestDetailLogHistoryBusinessImpl
								.doSearch(detailLog);
						
						if (detailLog.getStatus() == 1l) {
								detailLog.setAction("UPDATE: Cập nhật ticket");
								obj.setIsTrace(1l);
							
						}
						else if (detailLog.getStatus() == 2l) {
							List<ComplainOrderRequestDetailLogHistoryDTO> lstReturn = new ArrayList<>();
							for(ComplainOrderRequestDetailLogHistoryDTO compl : listLogFillterFollowStatus) {
								if(StringUtils.isNotBlank(compl.getAction()) && compl.getAction().contains("Trả lại ticket")) {
									lstReturn.add(compl);
								}
							}
//							Stream stream = listLogFillterFollowStatus.stream().filter(t -> t.getAction().contains("Trả lại ticket") == true);
							detailLog.setTimes((long)lstReturn.size()+1);
							detailLog.setAction("UPDATE: Trả lại ticket lần " + detailLog.getTimes() + "");
							obj.setActionLast("DO_AGAIN");
						}else if (detailLog.getStatus() == 4l) {
							if(obj.getReason().equals("ATTITUDEPROGRESS")==true) {
								detailLog.setAction("UPDATE: Đóng ticket");
								detailLog.setReason("Không hài lòng về thái độ nghiệp vụ hoặc tiến độ");
								detailLog.setNote(obj.getNote());
								obj.setIsTrace(1l);
								obj.setActionLast("DO_FAIL");
							}else if (obj.getReason().equals("NEWCOMPLAIN")==true) {
								detailLog.setAction("UPDATE: Đóng ticket");
								detailLog.setReason("Phát sinh yêu cầu/sự cố/khiếu nại mới");
								detailLog.setNote("Đóng ticket và thực hiện tạo ticket mới");
								obj.setActionLast("DO_NEW");
								obj.setIsTrace(1l);
							}else {
								detailLog.setAction("UPDATE: Đóng ticket");
								obj.setActionLast("CLOSE");
								obj.setIsTrace(1l);
							}
						}
					}
					
					Long complainId = complainOrdersRequestBusinessImpl.updateCRM(obj, request);
					if (complainId == 0l) {
						resultInfo.setStatus(ResultInfo.RESULT_NOK);resultInfo.setType("error");
			            resultInfo.setMessage(BAD_MESSAGE);
			            response.setResultInfo(resultInfo);
					} else {
						if(find.getStatus() != obj.getStatus()) {
							detailLog.setCreateDate(new Timestamp(System.currentTimeMillis()));
							detailLog.setCreateUser(obj.getCreateUser());
							detailLog.setComplainOrderRequestId(obj.getComplainOrderRequestId());
							Long detaiId = complainOrderRequestDetailLogHistoryBusinessImpl.add(detailLog, request);
							if (detaiId == 0l) {
								resultInfo.setStatus(ResultInfo.RESULT_NOK);
					            resultInfo.setMessage(BAD_MESSAGE);resultInfo.setType("error");
					            response.setResultInfo(resultInfo);
							}else {
								resultInfo.setStatus(ResultInfo.RESULT_OK);
						        resultInfo.setMessage(SUCCESS_MESSAGE);resultInfo.setType("success");
						        response.setResultInfo(resultInfo);
								tx.commit();
							}
						}else {
							resultInfo.setStatus(ResultInfo.RESULT_OK);
					        resultInfo.setMessage(SUCCESS_MESSAGE);resultInfo.setType("success");
					        response.setResultInfo(resultInfo);
							tx.commit();
						}
						
					}
				}
	
			}else {
				obj.setActionLast("CREATE");
				obj.setIsTrace(1l);
				Long complainId = complainOrdersRequestBusinessImpl.add(obj, request);
				tx = session.beginTransaction();
				if (complainId == 0l) {
					ResultInfo resultInfo = new ResultInfo();
		            resultInfo.setStatus(ResultInfo.RESULT_NOK);
		            resultInfo.setMessage(BAD_MESSAGE);resultInfo.setType("error");
		            response.setResultInfo(resultInfo);
				}
				else {		
					ResultInfo resultInfo = new ResultInfo();
					// validate bat reponse tra ve
					detailLog.setAction("CREATE: Tạo mới ticket");
					detailLog.setStatus(1l);
					detailLog.setCreateDate(new Timestamp(System.currentTimeMillis()));
					detailLog.setCreateUser(obj.getCreateUser());
					detailLog.setComplainOrderRequestId(obj.getComplainOrderRequestId());
					
					Long detaiId = complainOrderRequestDetailLogHistoryBusinessImpl.add(detailLog, request);
					if (detaiId == 0l) {
			            resultInfo.setStatus(ResultInfo.RESULT_NOK);
			            resultInfo.setMessage(BAD_MESSAGE);resultInfo.setType("error");
			            response.setResultInfo(resultInfo);
						if (tx != null)
							tx.rollback();
					} else {
						resultInfo.setStatus(ResultInfo.RESULT_OK);
				        resultInfo.setMessage(SUCCESS_MESSAGE);
				        response.setResultInfo(resultInfo);resultInfo.setType("success");
				        response.setPerformerId(obj.getPerformerId());
				        response.setPerformerName(obj.getPerformerfullName()+ " - "+obj.getPerformerName());
						tx.commit();
					}
				}
			}
			return Response.ok(response).build();

		} catch (BusinessException e) {
			ResultInfo resultInfo = new ResultInfo();resultInfo.setType("error");
            resultInfo.setStatus(ResultInfo.RESULT_NOK);resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
            return Response.ok(response).build();
       }catch (Exception e) {
    	   ResultInfo resultInfo = new ResultInfo();
           resultInfo.setStatus(ResultInfo.RESULT_NOK);resultInfo.setMessage("Có lỗi xảy ra khi lưu/cập nhật dữ liệu bên phầm mềm COMS");resultInfo.setType("error");
           response.setResultInfo(resultInfo);
			return Response.ok(response).build();
		}finally {
			if (session != null) {
				if (session.isOpen())
					session.close();
			}
		}
	}

}
