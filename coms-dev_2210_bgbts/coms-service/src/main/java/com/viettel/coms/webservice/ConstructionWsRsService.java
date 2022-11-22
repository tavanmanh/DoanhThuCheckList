package com.viettel.coms.webservice;

import com.viettel.asset.business.AuthenticateWsBusiness;
import com.viettel.asset.dto.ResultInfo;
import com.viettel.cat.dto.ConstructionImageInfo;
import com.viettel.coms.business.*;
import com.viettel.coms.dao.ConstructionScheduleDAO;
import com.viettel.coms.dao.ConstructionTaskDAO;
import com.viettel.coms.dto.*;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON + ";charset=utf-8", MediaType.APPLICATION_XML})
@Path("/service")
public class ConstructionWsRsService {

    private static final String MONTHLY_PLAN = "2";
    private static final String MONITORING = "1";
    private static final String CONSTRUCTION = "0";
    private static final String STOP_STATE = "4";
    public static final String MANAGE_PLAN = "MANAGE PLAN";
    private Logger LOGGER = Logger.getLogger(ConstructionTaskWsRsService.class);

    @Autowired
    ConstructionBusinessImpl constructionBusiness;
    @Autowired
    ObstructedBusinessImpl obstructedBusinessImpl;
    @Autowired
    WorkItemBusinessImpl workItemBusinessImpl;
    @Context
    HttpServletRequest request1;
    @Autowired
    ConstructionTaskBusinessImpl constructionTaskBusiness;
    @Autowired
    ConstructionScheduleBusinessImpl constructionScheduleBusiness;
    @Autowired
    AuthenticateWsBusiness authenticateWsBusiness;
    @Autowired
    ConstructionTaskDAO construcitonTaskDao;
    @Autowired
    private ConstructionScheduleDAO constructionScheduleDAO;
    @Autowired
    private ConstructionTaskDailyBusinessImpl constructionTaskDailyBusinessImpl;
    
    @Autowired
    private WoBusinessImpl woBusinessImpl;

    /**
     * convert DTO from ConstructionScheduleDTO To ConstructionTaskDTO
     *
     * @param request
     * @param convertForm
     */
    private void GetValueFromConstructionScheduleDTORequestToConstructionTaskDTOUpdateRequest(ConstructionScheduleDTORequest request, ConstructionTaskDTOUpdateRequest convertForm) {

        convertForm.getSysUserRequest().setSysGroupId(request.getSysUserRequest().getSysGroupId());
        convertForm.getSysUserRequest().setSysUserId(request.getSysUserRequest().getSysUserId());

        convertForm.getConstructionTaskDTO().setCompletePercent(Double.valueOf(request.getConstructionScheduleWorkItemDTO().getCompletePercent()));
        convertForm.getConstructionTaskDTO().setDescription(request.getConstructionScheduleWorkItemDTO().getDescription());
        convertForm.getConstructionTaskDTO().setPerformerId(request.getConstructionScheduleWorkItemDTO().getPerformerId());
        convertForm.getConstructionTaskDTO().setConstructionTaskId(request.getConstructionScheduleWorkItemDTO().getConstructionTaskId());
        convertForm.getConstructionTaskDTO().setPath(request.getConstructionScheduleWorkItemDTO().getPath());
        convertForm.getConstructionTaskDTO().setType(request.getConstructionScheduleWorkItemDTO().getType());
        convertForm.getConstructionTaskDTO().setQuantity(request.getConstructionScheduleWorkItemDTO().getQuantity());
        convertForm.getConstructionTaskDTO().setTaskOrder(request.getConstructionScheduleWorkItemDTO().getTaskOrder());
        convertForm.getConstructionTaskDTO().setAmount(request.getConstructionScheduleWorkItemDTO().getAmount());
        convertForm.getConstructionTaskDTO().setPrice(request.getConstructionScheduleWorkItemDTO().getPrice());
        convertForm.getConstructionTaskDTO().setQuantityByDate(request.getConstructionScheduleWorkItemDTO().getQuantityByDate());
//		hoanm1_20180710_start
        convertForm.getConstructionTaskDTO().setWorkItemId(request.getConstructionScheduleWorkItemDTO().getWorkItemId());
        convertForm.getConstructionTaskDTO().setCatTaskId(request.getConstructionScheduleWorkItemDTO().getCatTaskId());
        convertForm.getConstructionTaskDTO().setSysGroupId(request.getConstructionScheduleWorkItemDTO().getSysGroupId());
//		hoanm1_20180809_start
        convertForm.getConstructionTaskDTO().setTaskName(request.getConstructionScheduleWorkItemDTO().getTaskName());
        convertForm.getConstructionTaskDTO().setWorkItemName(request.getConstructionScheduleWorkItemDTO().getWorkName());
        convertForm.getConstructionTaskDTO().setConstructionId(request.getConstructionScheduleWorkItemDTO().getConstructionId());
        convertForm.getConstructionTaskDTO().setConstructionCode(request.getConstructionScheduleWorkItemDTO().getConstructionCode());
        convertForm.getConstructionTaskDTO().setStatus(request.getConstructionScheduleWorkItemDTO().getStatus());
        convertForm.getConstructionTaskDTO().setStartDate(request.getConstructionScheduleWorkItemDTO().getStartDate());
        convertForm.getConstructionTaskDTO().setEndDate(request.getConstructionScheduleWorkItemDTO().getEndDate());
        convertForm.getConstructionTaskDTO().setObstructedState(request.getConstructionScheduleWorkItemDTO().getObstructedState());
//        hoanm1_20190108_start
        convertForm.getConstructionTaskDTO().setStartingDateTK(request.getConstructionScheduleWorkItemDTO().getStartingDateTK());
        convertForm.getConstructionTaskDTO().setHandoverDateBuildBGMB(request.getConstructionScheduleWorkItemDTO().getHandoverDateBuildBGMB());
        convertForm.getConstructionTaskDTO().setCheckBGMB(request.getConstructionScheduleWorkItemDTO().getCheckBGMB());
        convertForm.getConstructionTaskDTO().setCatConstructionTypeId(request.getConstructionScheduleWorkItemDTO().getCatConstructionTypeId());
//        hoanm1_20190108_end
//        hoanm1_20190830_start
        convertForm.getConstructionTaskDTO().setCheckEntangle(request.getConstructionScheduleWorkItemDTO().getCheckEntangle());
//        hoanm1_20190830_end
        convertForm.setFlag(0l);
    }

    /**
     * convert List
     *
     * @param listConstructionScheduleRealization
     * @return List<ConstructionScheduleDTO>
     * @throws ParseException
     */
    private List<ConstructionScheduleDTO> convertTextStatus(ConstructionScheduleDTORequest request, List<ConstructionScheduleDTO> listConstructionScheduleRealization, String typeNumb) throws ParseException {
        for (int i = 0; i < listConstructionScheduleRealization.size(); i++) {
            ConstructionScheduleDTO realization = listConstructionScheduleRealization.get(i);
            String status = realization.getStatus();

            // init progress
            if (realization.getProgress() == null) {
                realization.setProgress((double) 0);
            }

            // set tab number
            realization.setScheduleType(typeNumb);

            //sort by month
            if (realization.getStartingDate() != null) {
                Date dateNow = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateNow);
                int thisMonth = cal.get(Calendar.MONTH);
                int thisYear = cal.get(Calendar.YEAR);

                DateFormat df = new SimpleDateFormat("dd-MM-yy");
                Date startMonth = df.parse(realization.getStartingDate());
                Calendar calStart = Calendar.getInstance();
                calStart.setTime(startMonth);
                int intStartMonth = calStart.get(Calendar.MONTH);
                int intStartYear = calStart.get(Calendar.YEAR);

                // sort to filter
                if (thisMonth == intStartMonth && thisYear == intStartYear) {
                    realization.setSortThisMonth(CONSTRUCTION);
                }
            }

            // get process construction
            Long Uncompleted = constructionScheduleBusiness.getUnCompletedTask(request, realization, typeNumb);
            realization.setUnCompletedTask(Uncompleted.toString());
            Long totalCompleted = constructionScheduleBusiness.getTotalTask(request, realization, typeNumb);
            realization.setTotalTask(totalCompleted.toString());

            // set progress view
            realization.setUncomTotalTask(Uncompleted + "/" + totalCompleted);
        }
        return listConstructionScheduleRealization;
    }

    /**
     * get Construction Task
     *
     * @param request
     * @return
     */
    @POST
//	@Path("/getListConstructionStationWorkItem/")
    @Path("/getNameConstructionIDbySysUserId/")
    public ConstructionStationWorkItemDTOResponse getConstructionTask(SysUserRequest request) {
        ConstructionStationWorkItemDTOResponse response = new ConstructionStationWorkItemDTOResponse();
        try {
//			hoanm1_20180602_start
//			request.setSysGroupId(construcitonTaskDao.getSysGroupId(request.getAuthenticationInfo().getUsername()));
//			request.setDepartmentId(Long.parseLong(construcitonTaskDao.getSysGroupId(request.getAuthenticationInfo().getUsername())));
            request.setSysGroupId(construcitonTaskDao.getSysGroupIdUserId(request.getSysUserId()));
            request.setDepartmentId(Long.parseLong(construcitonTaskDao.getSysGroupIdUserId(request.getSysUserId())));
//			hoanm1_20180602_end
            authenticateWsBusiness.validateRequest(request);
            List<ConstructionStationWorkItemDTO> data = constructionBusiness.getNameAndAddressContruction(request, response);
            response.setListConstructionStationWorkItem(data);
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

    /**
     * Màn hình công trình
     *
     * @param request
     * @return response ConstructionScheduleDTOResponse
     * @author CuongNV2 AntSoft
     */
    @POST
    @Path("/getValueToInitContructionManagement/")
    public ConstructionScheduleDTOResponse getValueToInitContructionManagement(ConstructionScheduleDTORequest request) {
        ConstructionScheduleDTOResponse response = new ConstructionScheduleDTOResponse();
        try {
//			hoanm1_20180822_start
//			List<DomainDTO> isManage = constructionScheduleDAO.getByAdResource(request.getSysUserRequest().getSysUserId(),
//					MANAGE_PLAN);
//			hoanm1_20180822_end
            // thi công
            if (request.getSysUserRequest().getFlag() == 0) {
//			response.setListConstructionScheduleRealizationDTO(convertTextStatus(request, constructionScheduleBusiness.getValueToInitContruction(request, CONSTRUCTION, response),CONSTRUCTION));
                response.setListConstructionScheduleRealizationDTO(constructionScheduleBusiness.getValueToInitContructionTurning(request, CONSTRUCTION, response));
            } else if (request.getSysUserRequest().getFlag() == 1) {
                // giám sát
//			response.setListConstructionSchedulePartnerDTO(convertTextStatus(request, constructionScheduleBusiness.getValueToInitContruction(request, MONITORING, response),MONITORING));
                response.setListConstructionSchedulePartnerDTO(constructionScheduleBusiness.getValueToInitContructionTurning(request, MONITORING, response));
            } else {
                // kế hoạch tháng
//			response.setListConstructionScheduleDirectorByDTO(convertTextStatus(request, constructionScheduleBusiness.getValueToInitContruction(request, MONTHLY_PLAN, response),MONTHLY_PLAN));
                response.setListConstructionScheduleDirectorByDTO(constructionScheduleBusiness.getValueToInitContructionTurning(request, MONTHLY_PLAN, response));
            }
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

    /**
     * Màn hình hạng mục
     *
     * @param request
     * @return response
     * @author CuongNV2 AntSoft
     */
    @POST
    @Path("/getValueToInitContructionManagementItem/")
    public ConstructionScheduleDTOResponse getValueToInitContructionManagementItem(ConstructionScheduleDTORequest request) {
        ConstructionScheduleDTOResponse response = new ConstructionScheduleDTOResponse();
        try {
            response.setListConstructionScheduleItemDTO(constructionScheduleBusiness.getValueToInitContructionManagementItem(request));
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

    /**
     * Màn hình chi tiết hạng mục
     *
     * @param request
     * @return response
     * @author CuongNV2 AntSoft
     */
    @POST
    @Path("/getValueToInitConstructionScheduleWorkItem/")
    public ConstructionScheduleDTOResponse getValueToInitConstructionScheduleWorkItemDTO(ConstructionScheduleDTORequest request) {
        ConstructionScheduleDTOResponse response = new ConstructionScheduleDTOResponse();
        try {
            List<ConstructionScheduleWorkItemDTO> data = constructionScheduleBusiness.getValueToInitConstructionScheduleWorkItemDTO(request);
            response.setListConstructionScheduleWorkItemDTO(data);
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

    // Reference
    // get image
    // public static final String END_URL_GET_LIST_IMAGE = "ConstructionTaskRestService/service/getImagesByConstructionTaskId";
    // push image
    // public static final String END_URL_UPDATE_WORK = "ConstructionTaskRestService/service/updatePercentConstructionTask";

    /**
     * updatePercentConstructionTask
     *
     * @param request
     * @return
     */
    @POST
    @Path("/updatePercentConstructionTask/")
    public ConstructionTaskDTOResponse updatePercentConstructionTask(ConstructionScheduleDTORequest request) {
        ConstructionTaskDTOResponse response = new ConstructionTaskDTOResponse();
		/*request.getSysUserRequest().setSysGroupId(construcitonTaskDao.getSysGroupId(request.getSysUserRequest().getAuthenticationInfo().getUsername()));
		request.getSysUserRequest().setDepartmentId(Long.parseLong(construcitonTaskDao.getSysGroupId(request.getSysUserRequest().getAuthenticationInfo().getUsername())));*/
        ConstructionTaskDTOUpdateRequest convertForm = new ConstructionTaskDTOUpdateRequest();
        convertForm.setListConstructionImageInfo(request.getListConstructionImageInfo());
        GetValueFromConstructionScheduleDTORequestToConstructionTaskDTOUpdateRequest(request, convertForm);
        try {
            /*authenticateWsBusiness.validateRequest(request.getSysUserRequest());*/
            int result = constructionTaskBusiness.updatePercentConstructionTask(convertForm);
            if (result != 0) {
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setStatus(ResultInfo.RESULT_OK);
                response.setResultInfo(resultInfo);
            } else {
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setStatus(ResultInfo.RESULT_NOK);
                response.setResultInfo(resultInfo);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            response.setResultInfo(resultInfo);
        }

        return response;
    }

    /**
     * Handling By Other Person
     *
     * @param request
     * @return
     */
    @POST
    @Path("/handlingByOtherPerson/")
    public ConstructionScheduleDTOResponse handlingByOtherPerson(ConstructionScheduleDTORequest request) {
        ConstructionScheduleDTOResponse response = new ConstructionScheduleDTOResponse();
        try {
            int result = 0;
            String authorities = request.getSysUserRequest().getAuthorities();
            if (ConstructionScheduleBusinessImpl.MANAGE_PLAN.equals(authorities)) {
                result = constructionScheduleBusiness.handlingByOtherPerson(request);
            }
            if (result == 2) {
//				chinhpxn20180720_start
                //insert sms_email
                Long newPerformerId = request.getSysUserReceiver().getSysUserId();
                Long oldPerformerId = request.getConstructionScheduleItemDTO().getPerformerId();
                Long sysUserId = request.getSysUserRequest().getSysUserId();
                String sysGroupId = request.getSysUserRequest().getSysGroupId();
                ConstructionScheduleItemDTO obj = request.getConstructionScheduleItemDTO();
                constructionScheduleBusiness.createSendSmsEmail(obj, sysGroupId, sysUserId, newPerformerId);
                constructionScheduleBusiness.createSendSmsEmailToConvert(obj, sysGroupId, sysUserId, oldPerformerId);
                constructionScheduleBusiness.createSendSmsEmailToOperator(obj, sysGroupId, sysUserId, newPerformerId, oldPerformerId);
//				chinhpxn20180720_end
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setStatus(ResultInfo.RESULT_OK);
                resultInfo.setMessage("Chuyển người thành công");
                response.setResultInfo(resultInfo);

            } else {
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setStatus(ResultInfo.RESULT_NOK);
                resultInfo.setMessage("Chuyển người thất bại");
                response.setResultInfo(resultInfo);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage("Chuyển người thất bại");
            response.setResultInfo(resultInfo);
        }

        return response;
    }
//    hoanm1_20180829_start    
    @POST
    @Path("/getChartWorkItem/")
    public ConstructionScheduleDTOResponse getChartWorkItem(ConstructionScheduleDTORequest request) {
        ConstructionScheduleDTOResponse response = new ConstructionScheduleDTOResponse();
        try {
            response.setListConstructionScheduleItemDTO(constructionScheduleBusiness.getChartWorkItem(request));
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
//    hoanm1_20180829_end
    @POST
	@Path("/updateConstructionExtraInfo/")
	public ResultInfo updateConstructionExtraInfo(ConstructionExtraDTORequest request) {
		ResultInfo resultInfo = new ResultInfo();
		resultInfo.setStatus(ResultInfo.RESULT_NOK);
		try {
			boolean result = constructionBusiness.gettDAO().updateConstructionExtra(request);
			if (result) {
				resultInfo.setStatus(ResultInfo.RESULT_OK);
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			resultInfo.setMessage(e.getMessage());
		}
		return resultInfo;
	}

	@POST
	@Path("/getConstructionExtraInfoByID/")
	public ConstructionExtraDTOResponse getConstructionExtraInfoByID(ConstructionIDExtraDTORequest request) {
		ConstructionExtraDTOResponse response = new ConstructionExtraDTOResponse();
		ResultInfo resultInfo = new ResultInfo();
		resultInfo.setStatus(ResultInfo.RESULT_NOK);
		try {
			ConstructionExtraDTO dto = constructionBusiness.gettDAO()
					.getConstructionExtraDTOByID(request.getConstructionID());

			resultInfo.setStatus(ResultInfo.RESULT_OK);
			response.setData(dto);

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			resultInfo.setMessage(e.getMessage());
		}
		response.setResultInfo(resultInfo);
		return response;
	}

	@POST
	@Path("/getImagesByConstructionExtraIDType/")
	public ConstructionExtraDTOImageResponse getListImages(ConstructionExtraDTOImageRequest request) {
		ConstructionExtraDTOImageResponse response = new ConstructionExtraDTOImageResponse();

		List<ConstructionImageInfo> listImage = constructionTaskBusiness
				.getImageByConstructionId_Type(request.getConstructionID(), request.getImageType());
		if (listImage != null) {
			response.setListImage(listImage);
			ResultInfo resultInfo = new ResultInfo();
			resultInfo.setStatus(ResultInfo.RESULT_OK);
			response.setResultInfo(resultInfo);

		} else {
			ResultInfo resultInfo = new ResultInfo();
			resultInfo.setStatus(ResultInfo.RESULT_NOK);
			response.setResultInfo(resultInfo);
		}

		return response;
	}
	
	
	/**hoangnh 251218 start**/
    @POST
    @Path("/updateHandoverGround/")
    @Transactional
    public AssignHandoverResponse updateStopConstructionTask(AssignHandoverRequest req) {
    	AssignHandoverResponse response = new AssignHandoverResponse();
    	String check = null;
    	String checkRP = null;
    	String checkUpdateRP = null;
    	Long idW = null;
    	String type = null;
    	WorkItemDTO wIDto = new WorkItemDTO();
    	wIDto.setStatus("1");
        try {
        	if(req != null && req.getAssignHandoverDTO() != null){
        		AssignHandoverDTO request = req.getAssignHandoverDTO();
        		if(request.getConstructionImageInfo() != null){
        			constructionBusiness.saveImageDB(req);
        		}
        		if(request.getAssignHandoverId() != null){
            		SysUserCOMSDTO sysDto = constructionBusiness.getListUser(req.getSysUserId());
            		constructionBusiness.updateHandoverDate(request.getConstructionId());
            			/**Chỉ chọn vướng**/
                	if("1".equals(request.getIsReceivedObstructStr()) && "0".equals(request.getIsReceivedGoodsStr())){
                		wIDto.setStatus("4");
                		check = constructionBusiness.updateHandoverFull(request);
                		if("Succes".equals(check)){
                			checkUpdateRP = constructionBusiness.updateRP(request,checkRP);
                			constructionBusiness.updateCons(request);
                		}
                	}
                	if("1".equals(request.getIsReceivedObstructStr())){
                		wIDto.setStatus("4");
                		ObstructedDTO dto = new ObstructedDTO();
            			dto.setObstructedState("1");
            			dto.setConstructionId(request.getConstructionId());
            			dto.setCreatedDate(new Date());
            			dto.setCreatedUserId(req.getSysUserId());
            			dto.setCreatedGroupId(sysDto.getSysGroupId());
            			dto.setObstructedContent(request.getReceivedObstructContent());
            			Long ids = obstructedBusinessImpl.save(dto);
            			System.out.printf("obstructedId" + ":" + ids);
            			/**Nếu chọn vướng--update construction.status = 4**/
            			constructionBusiness.updateCons(request);
                	}
                	/**Chỉ chọn có vật tư may đo**/
                	if("0".equals(request.getIsReceivedObstructStr()) && "1".equals(request.getIsReceivedGoodsStr())){
                		check = constructionBusiness.updateHandoverFull(request);
                		if("Succes".equals(check)){
                			checkUpdateRP = constructionBusiness.updateRP(request,checkRP);
                		}
                	}
                	/**Chọn cả vướng + vật tư may đo**/
                	if("1".equals(request.getIsReceivedObstructStr()) && "1".equals(request.getIsReceivedGoodsStr())){
                		check = constructionBusiness.updateHandoverFull(request);
                		if("Succes".equals(check)){
                			checkUpdateRP = constructionBusiness.updateRP(request,checkRP);
                		}
                	}
                	/**Không chọn vướng + vật tư may đo**/
                	if("0".equals(request.getIsReceivedObstructStr()) && "0".equals(request.getIsReceivedGoodsStr())){
                		check = constructionBusiness.updateHandoverFull(request);
//                		String checkU = constructionBusiness.updateBuild(request);
                		constructionBusiness.updateStatusConsDDK(request.getConstructionId()); //HuyPQ-20190423-add
                		if("Succes".equals(check)){
                			checkRP = "1";
                			checkUpdateRP = constructionBusiness.updateRP(request,checkRP);
                		}
                	}
                	constructionBusiness.updateBuild(request);
                	if("Succes".equals(check) && "Succes".equals(checkUpdateRP)){
                		List<CatWorkItemTypeDTO> listWIT = null;
                		wIDto.setCreatedUserId(req.getSysUserId());
        	    		wIDto.setCreatedGroupId(sysDto.getSysGroupId());
//        	    		hoanm1_20190905_start
        	    		HashMap<String, String> workItemNameMap = new HashMap<String, String>();
        	    		List<WorkItemDetailDTO> lstName = constructionBusiness.getListWorkItemName(request.getCatStationHouseId(),request.getCntContractId());
        	    		for(WorkItemDetailDTO lst: lstName){
        		            workItemNameMap.put(lst.getName(),lst.getName());
        		         }
//        	    		hoanm1_20190905_end
        	    		/**chọn độ cao cột + dưới đất**/
                		if(request.getColumnHeight() > 0 && request.getStationType() == 1){
                			type = "1";
                			List<CatWorkItemTypeDTO> lstW = constructionBusiness.getWorkItemByType(type);
                			if(lstW != null && lstW.size() > 0){
//                				saveCatWorkItemType(lstW,wIDto,request);
                				saveCatWorkItemType(lstW,wIDto,request,workItemNameMap);
                			}
                		}
                	
//                		hoanm1_20190906_start
                		
                		/**chọn độ cao cột + trên mái**/
                		if(request.getColumnHeight() > 0 && request.getStationType() == 2){
                			type = "2";
                			List<CatWorkItemTypeDTO> lstW = constructionBusiness.getWorkItemByType(type);
                			if(lstW != null && lstW.size() > 0){
                				saveCatWorkItemType(lstW,wIDto,request,workItemNameMap);
                			}
                		}
                		
                		/**Chọn dưới đất**/
                		if(request.getStationType() == 1){
                			type = "15";
                			List<CatWorkItemTypeDTO> lstW = constructionBusiness.getWorkItemByType(type);
                			if(lstW != null && lstW.size() > 0){
                				saveCatWorkItemType(lstW,wIDto,request,workItemNameMap);
                			}
                		}
                		/**Chọn trên mái**/
                		if(request.getStationType() == 2){
                			type = "16";
                			List<CatWorkItemTypeDTO> lstW = constructionBusiness.getWorkItemByType(type);
                			if(lstW != null && lstW.size() > 0){
                				saveCatWorkItemType(lstW,wIDto,request,workItemNameMap);
                			}
                		}
                		
                		if(request.getHouseTypeId() != null){
                			/**chọn loại nhà LG**/
                    		if(request.getHouseTypeId() == 1){
                    			type = "3";
                    			List<CatWorkItemTypeDTO> lstW = constructionBusiness.getWorkItemByType(type);
                    			if(lstW != null && lstW.size() > 0){
                    				saveCatWorkItemType(lstW,wIDto,request,workItemNameMap);
                    			}
                    		}
                    		
                    		/**chọn loại nhà Cabin**/
                    		if(request.getHouseTypeId() == 2){
                    			type = "4";
                    			List<CatWorkItemTypeDTO> lstW = constructionBusiness.getWorkItemByType(type);
                    			if(lstW != null && lstW.size() > 0){
                    				saveCatWorkItemType(lstW,wIDto,request,workItemNameMap);
                    			}
                    		}
                    		
                    		/**chọn loại nhà Minishelter**/
//                    		hoanm1_20190805 start comment
//                    		if(request.getHouseTypeId() == 3){
//                    			type = "5";
//                    			List<CatWorkItemTypeDTO> lstW = constructionBusiness.getWorkItemByType(type);
//                    			if(lstW != null && lstW.size() > 0){
//                    				saveCatWorkItemType(lstW,wIDto,request);
//                    			}
//                    		}
//                    		hoanm1_20190805 end comment
                    		/**chọn nhà cải tạo**/
                    		if(request.getHouseTypeId() == 5){
                    			type = "8";
                    			List<CatWorkItemTypeDTO> lstW = constructionBusiness.getWorkItemByType(type);
                    			if(lstW != null && lstW.size() > 0){
                    				saveCatWorkItemType(lstW,wIDto,request,workItemNameMap);
                    			}
                    		}
                    		
                    		if(request.getStationType() != null){
                    			/**chọn nhà xây + dưới đất**/
                        		if(request.getHouseTypeId() == 4 && request.getStationType() == 1){
                        			type = "6";
                        			List<CatWorkItemTypeDTO> lstW = constructionBusiness.getWorkItemByType(type);
                        			if(lstW != null && lstW.size() > 0){
                        				saveCatWorkItemType(lstW,wIDto,request,workItemNameMap);
                        			}
                        		}
                        		
                        		/**chọn nhà xây + trên mái**/
                        		if(request.getHouseTypeId() == 4 && request.getStationType() == 2){
                        			type = "7";
                        			List<CatWorkItemTypeDTO> lstW = constructionBusiness.getWorkItemByType(type);
                        			if(lstW != null && lstW.size() > 0){
                        				saveCatWorkItemType(lstW,wIDto,request,workItemNameMap);
                        			}
                        		}
                    		}
                    		
                		}
                		
                		if(request.getGroundingTypeId() != null){
                			/**chọn loại tiếp địa Gem**/
                    		if(request.getGroundingTypeId() == 1){
                    			type= "9";
                    			List<CatWorkItemTypeDTO> lstW = constructionBusiness.getWorkItemByType(type);
                    			if(lstW != null && lstW.size() > 0){
                    				saveCatWorkItemType(lstW,wIDto,request,workItemNameMap);
                    			}
                    		}
                    		
                    		/**chọn loại tiếp địa lập là**/
                    		if(request.getGroundingTypeId() == 2){
                    			type = "10";
                    			List<CatWorkItemTypeDTO> lstW = constructionBusiness.getWorkItemByType(type);
                    			if(lstW != null && lstW.size() > 0){
                    				saveCatWorkItemType(lstW,wIDto,request,workItemNameMap);
                    			}
                    		}
                    		
                    		/**chọn loại tiếp địa khoan cọc + dưới đất**/
                    		if(request.getGroundingTypeId() == 3 && request.getStationType() == 1){
                    			type = "11";
                    			List<CatWorkItemTypeDTO> lstW = constructionBusiness.getWorkItemByType(type);
                    			if(lstW != null && lstW.size() > 0){
                    				saveCatWorkItemType(lstW,wIDto,request,workItemNameMap);
                    			}
                    		}
                    		
                    		/**chọn loại tiếp địa khoan cọc + trên mái**/
                    		if(request.getGroundingTypeId() == 3 && request.getStationType() == 2){
                    			type = "12";
                    			List<CatWorkItemTypeDTO> lstW = constructionBusiness.getWorkItemByType(type);
                    			if(lstW != null && lstW.size() > 0){
                    				saveCatWorkItemType(lstW,wIDto,request,workItemNameMap);
                    			}
                    		}
                		}
                		
                		/**chọn tường rào**/
                		if("1".equals(request.getIsFenceStr())){
                			type = "13";
                			List<CatWorkItemTypeDTO> lstW = constructionBusiness.getWorkItemByType(type);
                			if(lstW != null && lstW.size() > 0){
                				saveCatWorkItemType(lstW,wIDto,request,workItemNameMap);
                			}
                		}
                		
                		/**Không chọn AC có sẵn**/
//                		if(request.getHaveWorkItemName() != null){
//                			if(("AC").contains(request.getHaveWorkItemName())){
//	                			type = "14";
//	                        	List<CatWorkItemTypeDTO> lstW = constructionBusiness.getWorkItemByType(type);
//	                        	if(lstW != null && lstW.size() > 0){
//	                        		saveCatWorkItemType(lstW,wIDto,request);
//	                        	}
//                			}
//                		}
                		
                		if("0".equals(request.getIsACStr())){
                			type = "14";
                				List<CatWorkItemTypeDTO> lstW = constructionBusiness.getWorkItemByType(type);
                				if(lstW != null && lstW.size() > 0){
                					saveCatWorkItemType(lstW,wIDto,request,workItemNameMap);
                				}
                			}
//                		hoanm1_20190503_start
                		if(request.getCheckXPXD()==1){
                			type = "18";
                			List<CatWorkItemTypeDTO> lstW = constructionBusiness.getWorkItemByType(type);
                			if(lstW != null && lstW.size() > 0){
                				saveCatWorkItemType(lstW,wIDto,request,workItemNameMap);
                			}
                		}
                		if(request.getCheckXPAC()==1){
                			type = "19";
                			List<CatWorkItemTypeDTO> lstW = constructionBusiness.getWorkItemByType(type);
                			if(lstW != null && lstW.size() > 0){
                				saveCatWorkItemType(lstW,wIDto,request,workItemNameMap);
                			}
                		}
//                		hoanm1_20190503_end
                		
//                		hoanm1_20190906_end
                	}
                }
        	}
            if ("Succes".equals(check) && "Succes".equals(checkUpdateRP)) {
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setStatus(ResultInfo.RESULT_OK);
                response.setResultInfo(resultInfo);
            } else {
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setStatus(ResultInfo.RESULT_NOK);
                response.setResultInfo(resultInfo);
            }
        } catch (Exception e) {
        	LOGGER.error(e.getMessage(), e);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            response.setResultInfo(resultInfo);
        }
        return response;
    }
//    hoanm1_20190906_start
    /**created new work item**/
//    public void saveCatWorkItemType(List<CatWorkItemTypeDTO> lstW, WorkItemDTO wIDto, AssignHandoverDTO request){
//    	for(CatWorkItemTypeDTO dto : lstW){
//    		/**Công trình bắt đầu bằng CB || TC thì k insert vào workItem**/
//    		String name = (String.valueOf(request.getConstructionCode().charAt(0)) + String.valueOf(request.getConstructionCode().charAt(1)));
//    		if(!(name.toUpperCase().equals("CB") || name.toUpperCase().equals("TC"))){
//    			wIDto.setCode(request.getConstructionCode() + "_" + dto.getCode());
//        		List<WorkItemDTO> work = constructionBusiness.getWorkItemByCode(wIDto.getCode());
//        		if(work.size() == 0){
//        			wIDto.setName(dto.getName());
//            		wIDto.setConstructionId(request.getConstructionId());
//            		wIDto.setIsInternal("1");
//            		wIDto.setCreatedDate(new Date());
//            		wIDto.setConstructorId(request.getSysGroupId());
//            		wIDto.setSupervisorId(request.getSysGroupId());
//            		wIDto.setCatWorkItemGroupId(dto.getCatWorkItemGroupId());
//            		wIDto.setCatWorkItemTypeId(dto.getCatWorkItemTypeId());
//            		Long idW = workItemBusinessImpl.save(wIDto);
//        		}
//    		}
//    	}
//    }
    
    public void saveCatWorkItemType(List<CatWorkItemTypeDTO> lstW, WorkItemDTO wIDto, AssignHandoverDTO request,HashMap<String, String> workItemNameMap){
    	for(CatWorkItemTypeDTO dto : lstW){
    		/**Công trình bắt đầu bằng CB || TC thì k insert vào workItem**/
    		String name = (String.valueOf(request.getConstructionCode().charAt(0)) + String.valueOf(request.getConstructionCode().charAt(1)));
    		if(!(name.toUpperCase().equals("CB") || name.toUpperCase().equals("TC"))){
    			wIDto.setCode(request.getConstructionCode() + "_" + dto.getCode());
        		List<WorkItemDTO> work = constructionBusiness.getWorkItemByCode(wIDto.getCode());
        		if(work.size() == 0){
        			if("".equals(workItemNameMap.get(dto.getName())) || workItemNameMap.get(dto.getName())==null ){
        				wIDto.setName(dto.getName());
        				wIDto.setConstructionId(request.getConstructionId());
        				wIDto.setIsInternal("1");
        				wIDto.setCreatedDate(new Date());
        				wIDto.setConstructorId(request.getSysGroupId());
        				wIDto.setSupervisorId(request.getSysGroupId());
        				wIDto.setCatWorkItemGroupId(dto.getCatWorkItemGroupId());
        				wIDto.setCatWorkItemTypeId(dto.getCatWorkItemTypeId());
        				Long idW = workItemBusinessImpl.save(wIDto);
        			}
        		}
    		}
    	}
    }
//    hoanm1_20190906_end
    /**Khởi tạo màn hình dashboard**/
    @POST
    @Path("/doSearchDashBoard/")
    public AssignHandoverResponse doSearchDashBoard (AssignHandoverRequest req){
    	AssignHandoverResponse response = new AssignHandoverResponse();
    	try{
    		AssignHandoverResponse resReceived = constructionBusiness.doSearchReceived(req);
    		AssignHandoverResponse resNotReceived = constructionBusiness.doSearchNotReceived(req);
    		response.setTotalRecordReceived(resReceived.getTotalRecordReceived());
    		response.setTotalRecordNotReceived(resNotReceived.getTotalRecordNotReceived());
    		if (response.getTotalRecordNotReceived() != null && response.getTotalRecordReceived() != null) {
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setStatus(ResultInfo.RESULT_OK);
                response.setResultInfo(resultInfo);
            } else {
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setStatus(ResultInfo.RESULT_NOK);
                response.setResultInfo(resultInfo);
            }
    	} catch (Exception e){
    		LOGGER.error(e.getMessage(), e);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            response.setResultInfo(resultInfo);
    	}
    	return response;
    }
    
    /**Màn hình chi tiết dashboard**/
    @POST
    @Path("/doSearchAssign/")
    public AssignHandoverResponse doSearchAssign(AssignHandoverRequest req){
    	AssignHandoverResponse response = new AssignHandoverResponse();
    	
    	try{
    		response.setAssignHandoverDTO(constructionBusiness.doSearchAssign(req));
    		AssignHandoverResponse resReceived = constructionBusiness.doSearchReceived(req);
    		AssignHandoverResponse resNotReceived = constructionBusiness.doSearchNotReceived(req);
    		response.setTotalRecordReceived(resReceived.getTotalRecordReceived());
    		response.setTotalRecordNotReceived(resNotReceived.getTotalRecordNotReceived());
    		response.setHouseType(constructionBusiness.getHouseType());
    		response.setGroundingType(constructionBusiness.getGroundingType());
    		
    		if (response.getAssignHandoverDTO() != null) {
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setStatus(ResultInfo.RESULT_OK);
                response.setResultInfo(resultInfo);
            } else {
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setStatus(ResultInfo.RESULT_NOK);
                response.setResultInfo(resultInfo);
            }
    	} catch (Exception e){
    		LOGGER.error(e.getMessage(), e);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            response.setResultInfo(resultInfo);
    	}
    	return response;
    }
    
    /**Màn hình tìm kiếm ảnh**/
    @POST
    @Path("/doSearchImage/")
    public AssignHandoverResponse doSearchImage(AssignHandoverRequest req){
    	AssignHandoverResponse response = new AssignHandoverResponse();
    	
    	try{
    		response.setConstructionImageInfo(constructionBusiness.doSearchImage(req.getAssignHandoverId()));
    		
    		if (response.getConstructionImageInfo() != null) {
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setStatus(ResultInfo.RESULT_OK);
                response.setResultInfo(resultInfo);
            } else {
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setStatus(ResultInfo.RESULT_NOK);
                response.setResultInfo(resultInfo);
            }
    	} catch (Exception e){
    		LOGGER.error(e.getMessage(), e);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            response.setResultInfo(resultInfo);
    	}
    	return response;
    }
    
    /**hoangnh 251218 end**/
    
    /**hoangh start 14022019 -- Màn hình thêm mới**/
    @POST
    @Path("/insertConstructionTaskDaily/")
    public ConstructionTaskDailyResponse insertConstructionTaskDaily(ConstructionTaskDTOUpdateRequest req){
    	LOGGER.warn("Service Mobile: Đã vào Service insertConstructionTaskDaily");
    	ConstructionTaskDailyResponse response = new ConstructionTaskDailyResponse();
    	try{
    		Long id =0L;
    		DecimalFormat dcf = new DecimalFormat("#.##");
        	if(req != null){
        		/**Check với constructionTask request đã có bản ghi nào đc tạo chưa, nếu có thì update >< tạo mới**/
        		ConstructionTaskDailyDTO checkExits = constructionBusiness.checkExits(req.getConstructionTaskDailyDTO());
        		if(req.getConstructionTaskDailyDTO().getCurrentAmount().contains(",")){
        			req.getConstructionTaskDailyDTO().setCurrentAmount(req.getConstructionTaskDailyDTO().getCurrentAmount().replace(",", "."));
        		}
        		if(req.getConstructionTaskDailyDTO().getTotalAmount().contains(",")){
        			req.getConstructionTaskDailyDTO().setTotalAmount(req.getConstructionTaskDailyDTO().getTotalAmount().replace(",", "."));
        		}
        		Double currentAmount = Double.valueOf(req.getConstructionTaskDailyDTO().getCurrentAmount());
        		Double totalAmount = Double.valueOf(req.getConstructionTaskDailyDTO().getTotalAmount());
        		int retval = Double.compare(currentAmount, totalAmount);
        		if( retval == 0){
        			req.getConstructionTaskDailyDTO().setStatus("4");
        			req.getConstructionTaskDailyDTO().setCompletePercent("100");
        		} else {
        			req.getConstructionTaskDailyDTO().setStatus("2");
        			req.getConstructionTaskDailyDTO().setCompletePercent(String.valueOf(dcf.format((currentAmount/totalAmount)*100)));
        		}
        		req.getConstructionTaskDailyDTO().setConfirm("0");
    			req.getConstructionTaskDailyDTO().setCreatedDate(new Date());
    			req.getConstructionTaskDailyDTO().setCreatedGroupId(req.getConstructionTaskDailyDTO().getSysGroupId());
//    			hoanm1_20191112_start
    			req.getConstructionTaskDailyDTO().setQuantity(1000000*req.getConstructionTaskDailyDTO().getQuantity());
//    			hoanm1_20191112_end
        		if((checkExits != null) ){
        			LOGGER.warn("Service Mobile: Request != null > cập nhật >");
        			req.getConstructionTaskDailyDTO().setConstructionTaskDailyId(checkExits.getConstructionTaskDailyId());
        			id = constructionTaskDailyBusinessImpl.updateConstructionTaskDaily(req.getConstructionTaskDailyDTO());
        			constructionTaskDailyBusinessImpl.updateConstructionTask(req.getConstructionTaskDailyDTO());
        			if(req.getListConstructionImageInfo() != null){
        				if(req.getListConstructionImageInfo().size() > 0){
        					LOGGER.warn("Size ảnh:" + req.getListConstructionImageInfo().size());
            				LOGGER.warn("Service Mobile: Request != null > cập nhật > cập nhật ảnh");
            				constructionBusiness.checkDocs(req.getConstructionTaskDailyDTO().getConstructionTaskDailyId(), "44");
            				constructionBusiness.saveImageConstructionTaskDaily(req.getListConstructionImageInfo(),req.getConstructionTaskDailyDTO() , checkExits.getConstructionTaskDailyId());
            			}
        			}
        		} else {
        			LOGGER.warn("Service Mobile: Request != null > thêm mới >");
            		id = constructionTaskDailyBusinessImpl.saveConstructionTaskDaily(req.getConstructionTaskDailyDTO());
            		constructionTaskDailyBusinessImpl.updateConstructionTask(req.getConstructionTaskDailyDTO());
            		if(req.getListConstructionImageInfo() != null){
            			if(req.getListConstructionImageInfo().size() > 0){
            				LOGGER.warn("Size ảnh:" + req.getListConstructionImageInfo().size());
                    		LOGGER.warn("Service Mobile: Request != null > thêm mới > thêm mới ảnh");
                    		constructionBusiness.saveImageConstructionTaskDaily(req.getListConstructionImageInfo(),req.getConstructionTaskDailyDTO(), id);
                    	}
            		}
        		}
        	}
        	constructionTaskDailyBusinessImpl.updateInformation(req.getConstructionTaskDailyDTO());
        	LOGGER.warn("Đã update workItem + construction + ghi log");
    		if (id != 0L) {
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setStatus(ResultInfo.RESULT_OK);
                response.setResultInfo(resultInfo);
            } else {
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setStatus(ResultInfo.RESULT_NOK);
                response.setResultInfo(resultInfo);
            }
    	} catch (Exception e){
    		LOGGER.error(e.getMessage(), e);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            response.setResultInfo(resultInfo);
    	}
    	return response;
    }
    
    /**Lấy ảnh từ sever**/
    @POST
    @Path("/imageConstructionTaskDaily/")
    public ConstructionTaskDailyResponse imageConstructionTaskDaily(ConstructionTaskDTOUpdateRequest req){
    	ConstructionTaskDailyResponse response = new ConstructionTaskDailyResponse();
    	
    	try{
    		ConstructionTaskDailyDTO checkExits = constructionBusiness.checkExits(req.getConstructionTaskDailyDTO());
    		if(checkExits != null){
    			response.setListImage(constructionBusiness.imageConstructionTaskDaily(checkExits.getConstructionTaskDailyId()));
    			LOGGER.warn("Đã có ảnh :" + response.getListImage());
    		}
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_OK);
            response.setResultInfo(resultInfo);
    	} catch (Exception e){
    		LOGGER.error(e.getMessage(), e);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            response.setResultInfo(resultInfo);
    	}
    	return response;
    }
    
    /**hoangh start 14022019**/
    
    //Huypq-20190826-start
    @POST
    @Path("/updateHandoverMachine/")
    @Transactional
    public AssignHandoverResponse updateHandoverMachine(AssignHandoverRequest req) {
    	AssignHandoverResponse response = new AssignHandoverResponse();
    	String check = null;
    	String checkRP = null;
    	String checkUpdateRP = null;
    	String type = null;
    	WorkItemDTO wIDto = new WorkItemDTO();
    	wIDto.setStatus("1");
        try {
        	if(req != null && req.getAssignHandoverDTO() != null){
        		AssignHandoverDTO request = req.getAssignHandoverDTO();
        		if(request.getConstructionImageInfo() != null){
        			constructionBusiness.saveImageDB(req);
        		}
        		if(request.getAssignHandoverId() != null){
            		SysUserCOMSDTO sysDto = constructionBusiness.getListUser(req.getSysUserId());
            		constructionBusiness.updateHandoverDate(request.getConstructionId());
            			/**Chỉ chọn vướng**/
                	if("1".equals(request.getIsReceivedObstructStr()) && "0".equals(request.getIsReceivedGoodsStr())){
                		wIDto.setStatus("4");
                		check = constructionBusiness.updateHandoverMachine(request);
                		if("Succes".equals(check)){
                			checkUpdateRP = constructionBusiness.updateRP(request,checkRP);
                			constructionBusiness.updateCons(request);
                		}
                	}
                	if("1".equals(request.getIsReceivedObstructStr())){
                		wIDto.setStatus("4");
                		ObstructedDTO dto = new ObstructedDTO();
            			dto.setObstructedState("1");
            			dto.setConstructionId(request.getConstructionId());
            			dto.setCreatedDate(new Date());
            			dto.setCreatedUserId(req.getSysUserId());
            			dto.setCreatedGroupId(sysDto.getSysGroupId());
            			dto.setObstructedContent(request.getReceivedObstructContent());
            			Long ids = obstructedBusinessImpl.save(dto);
            			System.out.printf("obstructedId" + ":" + ids);
            			/**Nếu chọn vướng--update construction.status = 4**/
            			constructionBusiness.updateCons(request);
                	}
                	/**Chỉ chọn có vật tư may đo**/
                	if("0".equals(request.getIsReceivedObstructStr()) && "1".equals(request.getIsReceivedGoodsStr())){
                		check = constructionBusiness.updateHandoverMachine(request);
                		if("Succes".equals(check)){
                			checkUpdateRP = constructionBusiness.updateRP(request,checkRP);
                		}
                	}
                	/**Chọn cả vướng + vật tư may đo**/
                	if("1".equals(request.getIsReceivedObstructStr()) && "1".equals(request.getIsReceivedGoodsStr())){
                		check = constructionBusiness.updateHandoverMachine(request);
                		if("Succes".equals(check)){
                			checkUpdateRP = constructionBusiness.updateRP(request,checkRP);
                		}
                	}
                	/**Không chọn vướng + vật tư may đo**/
                	if("0".equals(request.getIsReceivedObstructStr()) && "0".equals(request.getIsReceivedGoodsStr())){
                		check = constructionBusiness.updateHandoverMachine(request);
//                		String checkU = constructionBusiness.updateBuild(request);
                		constructionBusiness.updateStatusConsDDK(request.getConstructionId()); //HuyPQ-20190423-add
                		if("Succes".equals(check)){
                			checkRP = "1";
                			checkUpdateRP = constructionBusiness.updateRP(request,checkRP);
                		}
                	}
                	constructionBusiness.updateBuild(request);
                	if("Succes".equals(check) && "Succes".equals(checkUpdateRP)){
                		wIDto.setCreatedUserId(req.getSysUserId());
        	    		wIDto.setCreatedGroupId(sysDto.getSysGroupId());
//        	    		hoanm1_20190905_start
        	    		HashMap<String, String> workItemNameMap = new HashMap<String, String>();
        	    		List<WorkItemDetailDTO> lstName = constructionBusiness.getListWorkItemName(request.getCatStationHouseId(),request.getCntContractId());
        	    		for(WorkItemDetailDTO lst: lstName){
        		            workItemNameMap.put(lst.getName(),lst.getName());
        		         }
//        	    		hoanm1_20190905_end
                		if(request.getTypeConstructionBgmb().equals("1")) {
                			type = "20";
                			List<CatWorkItemTypeDTO> lstW = constructionBusiness.getWorkItemByType(type);
                			if(lstW != null && lstW.size() > 0){
                				saveCatWorkItemType(lstW,wIDto,request,workItemNameMap);
                			}
                		}
                		
                		if(request.getTypeConstructionBgmb().equals("2")) {
                			type = "21";
                			List<CatWorkItemTypeDTO> lstW = constructionBusiness.getWorkItemByType(type);
                			if(lstW != null && lstW.size() > 0){
                				saveCatWorkItemType(lstW,wIDto,request,workItemNameMap);
                			}
                		}
                		
                		if("1".equals(request.getIsFenceStr())){
                			type = "13";
                			List<CatWorkItemTypeDTO> lstW = constructionBusiness.getWorkItemByType(type);
                			if(lstW != null && lstW.size() > 0){
                				saveCatWorkItemType(lstW,wIDto,request,workItemNameMap);
                			}
                		}
                	}
                }
        	}
            if ("Succes".equals(check) && "Succes".equals(checkUpdateRP)) {
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setStatus(ResultInfo.RESULT_OK);
                response.setResultInfo(resultInfo);
            } else {
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setStatus(ResultInfo.RESULT_NOK);
                response.setResultInfo(resultInfo);
            }
        } catch (Exception e) {
        	LOGGER.error(e.getMessage(), e);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            response.setResultInfo(resultInfo);
        }
        return response;
    }
    //Huy-end
//    hoanm1_20200627_start
    @POST
    @Path("/getListTaskXNXD/")
    public ConstructionTaskDTOResponse getListTaskXNXD(SysUserRequest request) {
    	ConstructionTaskDTOResponse response = new ConstructionTaskDTOResponse();
        try {
            List<ConstructionTaskDTO> data = constructionTaskBusiness.getListTaskXNXD(request);
            response.setLstConstrucitonTask(data);
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
    @Path("/updateTaskXNXD/")
    public ConstructionTaskDTOResponse updateTaskXNXD(ConstructionTaskDTOUpdateRequest request) {
    	ConstructionTaskDTOResponse response = new ConstructionTaskDTOResponse();
        try {
            int result = constructionTaskBusiness.updateTaskXNXD(request);
            ResultInfo resultInfo = new ResultInfo();
            if (result > 0) {
                resultInfo.setStatus(ResultInfo.RESULT_OK);
                resultInfo.setMessage("Cập nhật thành công");
                response.setResultInfo(resultInfo);
            } else {
                resultInfo.setStatus(ResultInfo.RESULT_NOK);
                resultInfo.setMessage("Có lỗi xảy ra");
                response.setResultInfo(resultInfo);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage("Có lỗi xảy ra");
            response.setResultInfo(resultInfo);
        }
        return response;
    }
    @POST
    @Path("/getImagesXNXD/")
    public ConstructionTaskDTOResponse getImagesXNXD(ConstructionTaskDTOUpdateRequest request) {
    	ConstructionTaskDTOResponse response = new ConstructionTaskDTOResponse();

        List<ConstructionImageInfo> listImage = constructionTaskBusiness.getImagesXNXD(request);
        if (listImage != null) {
            response.setListImage(listImage);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_OK);
            response.setResultInfo(resultInfo);

        } else {
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            response.setResultInfo(resultInfo);
        }

        return response;
    }
//    hoanm1_20200627_end
}