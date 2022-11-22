package com.viettel.coms.webservice;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.viettel.coms.business.AssignHandoverBusinessImpl;
import com.viettel.coms.business.ConstructionBusinessImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.viettel.asset.dto.ResultInfo;
import com.viettel.coms.business.ObstructedBusinessImpl;
import com.viettel.coms.business.WorkItemBusinessImpl;
import com.viettel.coms.dto.AssignHandoverDTO;
import com.viettel.coms.dto.AssignHandoverRequest;
import com.viettel.coms.dto.AssignHandoverResponse;
import com.viettel.coms.dto.CatWorkItemTypeDTO;
import com.viettel.coms.dto.ObstructedDTO;
import com.viettel.coms.dto.WorkItemDTO;
import com.viettel.erp.dto.SysUserDTO;

@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON + ";charset=utf-8", MediaType.APPLICATION_XML})
@Path("/service")
public class AssignHandoverWsRsService {
	private Logger LOGGER = Logger.getLogger(AssignHandoverWsRsService.class);
	
	@Autowired
	private AssignHandoverBusinessImpl assignHandoverBusinessImpl;
	@Autowired
	ConstructionBusinessImpl constructionBusinessImpl;
	@Autowired
    ObstructedBusinessImpl obstructedBusinessImpl;
	@Autowired
    ConstructionBusinessImpl constructionBusiness;
	@Autowired
    WorkItemBusinessImpl workItemBusinessImpl;
	
	@POST
    @Path("/update/")
	@Transactional
    public AssignHandoverResponse update(AssignHandoverRequest req){
    	AssignHandoverResponse response = new AssignHandoverResponse();
    	int id = 0;
    	try{
    		if(req != null){
        		if(req.getAssignHandoverDTO() != null){
        			id  = assignHandoverBusinessImpl.updateAH(req.getAssignHandoverDTO());
        			LOGGER.warn("Đã update AssignHandover với id: " + id);
        			if(id != 0){
        				assignHandoverBusinessImpl.updateCons(req.getAssignHandoverDTO());
        				LOGGER.warn("Update HANDOVER_DATE_BUILD + STATUS BẢNG CONSTRUCTION với id: " + req.getAssignHandoverDTO().getConstructionId());
        				AssignHandoverDTO ass = assignHandoverBusinessImpl.getStation(req.getAssignHandoverDTO().getAssignHandoverId());
        				if(ass != null){
        					assignHandoverBusinessImpl.updateRP(ass.getCntContractId(),ass.getCatStationHouseId());
        					LOGGER.warn("Update BẢNG RP_STATION_COMPLETE với CntContractId: " + ass.getCntContractId() + "CatStationHouseId : " +ass.getCatStationHouseId());
        				}
        				/**Thêm mới hạng mục**/
        				if(req.getAssignHandoverDTO().getStationType() != null){
							String type = req.getAssignHandoverDTO().getStationType() == 3 ? "17" : "18";
	        				List<CatWorkItemTypeDTO> cw = assignHandoverBusinessImpl.getWIType(type);
	        				if(cw != null && cw.size() > 0){
	        					for (CatWorkItemTypeDTO di : cw){
	        						WorkItemDTO wi = new WorkItemDTO();
	        						wi.setCode(req.getAssignHandoverDTO().getConstructionCode() + "_" + di.getCode());
	        						List<WorkItemDTO> work = constructionBusiness.getWorkItemByCode(wi.getCode());
	        						if(work.size() == 0){
	        							wi.setName(di.getName());
	            						wi.setConstructionId(req.getAssignHandoverDTO().getConstructionId());
	            						wi.setIsInternal("1");
	            						if(StringUtils.isNotBlank(req.getAssignHandoverDTO().getReceivedObstructContent())){
	            							wi.setStatus("4");
	            						} else {
	            							wi.setStatus("1");
	            						}
	            						wi.setCreatedDate(new Date());
	            						wi.setConstructorId(req.getAssignHandoverDTO().getSysGroupId());
	            						wi.setSupervisorId(req.getAssignHandoverDTO().getSysGroupId());
	            						wi.setCatWorkItemGroupId(di.getCatWorkItemGroupId());
	            						wi.setCatWorkItemTypeId(di.getCatWorkItemTypeId());
	            						wi.setCreatedUserId(req.getSysUserId());
	            						wi.setCreatedGroupId(req.getAssignHandoverDTO().getSysGroupId());
	            			            Long idW = workItemBusinessImpl.save(wi);
	            			            LOGGER.warn("Đã thêm mới Hạng mục với id : " + idW + " cho công trình: " + req.getAssignHandoverDTO().getConstructionId());
	        						}
	        					}
	        				}
						}
        				if(StringUtils.isNotBlank(req.getAssignHandoverDTO().getReceivedObstructContent())){
        					ObstructedDTO obs = new ObstructedDTO();
            				obs.setObstructedState("1");
            				obs.setConstructionId(req.getAssignHandoverDTO().getConstructionId());
            				obs.setObstructedContent(req.getAssignHandoverDTO().getReceivedObstructContent());
            				obs.setCreatedDate(new Date());
            				obs.setCreatedUserId(req.getSysUserId());
            				SysUserDTO sysUserDTO = assignHandoverBusinessImpl.getSysUser(req.getSysUserId());
            				obs.setCreatedGroupId(sysUserDTO.getSysGroupId());
            				Long ids = obstructedBusinessImpl.save(obs);
            				LOGGER.warn("Có vướng > add Obstructed với id : " + ids);
            				
            				List<WorkItemDTO> lstW = assignHandoverBusinessImpl.getListWorkItem(req.getAssignHandoverDTO());
            				if(lstW != null && lstW.size() > 0){
            					for (WorkItemDTO dto : lstW){
            						assignHandoverBusinessImpl.updateWorkItem(dto.getWorkItemId());
            						LOGGER.warn("Có vướng > update workItem =4 với id : " + dto.getWorkItemId());
            					}
            				}
        				}
        			}
        			/**save list image moblie push**/
        			LOGGER.error("getConstructionImageInfo" + req.getAssignHandoverDTO().getConstructionImageInfo());
        			if(req.getAssignHandoverDTO().getConstructionImageInfo() != null && req.getAssignHandoverDTO().getConstructionImageInfo().size() > 0){
        				LOGGER.error("getConstructionImageInfo" + req.getAssignHandoverDTO().getConstructionImageInfo().size());
        				assignHandoverBusinessImpl.removeFile(req.getAssignHandoverDTO().getAssignHandoverId() , "56");
        				assignHandoverBusinessImpl.saveImage(req.getAssignHandoverDTO().getConstructionImageInfo(), req.getAssignHandoverDTO());
        			}
            	}
        	}
    		if (id != 0) {
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
	
	@POST
    @Path("/doSearchImage/")
    public AssignHandoverResponse doSearchImage(AssignHandoverRequest req){
    	AssignHandoverResponse response = new AssignHandoverResponse();
    	
    	try{
    		response.setConstructionImageInfo(constructionBusinessImpl.doSearchImage(req.getAssignHandoverId()));
    		
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

}
