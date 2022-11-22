package com.viettel.coms.webservice;

import com.viettel.asset.business.AuthenticateWsBusiness;
import com.viettel.asset.dto.ResultInfo;
import com.viettel.coms.business.SynStockTransBusinessImpl;
import com.viettel.coms.dto.*;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.wms.dto.AppParamDTO;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CuongNV2
 * @version 1.0
 * @since 2018-06-15
 */
@Consumes({MediaType.APPLICATION_JSON + ";charset=utf-8", MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON + ";charset=utf-8", MediaType.APPLICATION_XML})
@Path("/service")
public class SynStockTransWsRsService {

    private Logger LOGGER = Logger.getLogger(SynStockTransWsRsService.class);
    @Autowired
    SynStockTransBusinessImpl synStockTransBusiness;

    @Autowired
    AuthenticateWsBusiness authenticateWsBusiness;

    /**
     * getConstructionTask
     *
     * @param request
     * @return StockTransResponse
     */
    @POST
    @Path("/getCountConstructionTask/")
    public StockTransResponse getConstructionTask(SysUserRequest request) {
        StockTransResponse response = new StockTransResponse();
        try {

            authenticateWsBusiness.validateRequest(request);
            CountConstructionTaskDTO data = synStockTransBusiness.getCountContructionTask(request);
            response.setCountStockTrans(data);
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
     * getListSysStockTransDTO
     *
     * @param request
     * @return StockTransResponse
     */
    @POST
    @Path("/getListSynStockTransDTO/")
    public StockTransResponse getListSysStockTransDTO(StockTransRequest request) {
        StockTransResponse response = new StockTransResponse();
        try {

            authenticateWsBusiness.validateRequest(request.getSysUserRequest());
            //huypq-20190907-start
//            List<SynStockTransDTO> data = synStockTransBusiness.getListSysStockTransDTO(request);
            DataListDTO data = synStockTransBusiness.getListSysStockTransDTO(request);
//            response.setLstSynStockTransDto(data);
//            response.setDataList(data);
            response.setLstSynStockTransDto(data.getData());
            response.setTotal((long)data.getTotal());
            //huy-end
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
     * getListSynStockTransDetail
     *
     * @param request
     * @return StockTransResponse
     */
    @POST
    @Path("/getListSynStockTransDetailDTO/")
    public StockTransResponse getListSynStockTransDetail(StockTransRequest request) {
        StockTransResponse response = new StockTransResponse();
        try {

            authenticateWsBusiness.validateRequest(request.getSysUserRequest());
            List<SynStockTransDetailDTO> data = synStockTransBusiness.getListSynStockTransDetail(request.getSynStockTransDto());
            response.setLstSynStockTransDetail(data);
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
     * getListMerEntityDto
     *
     * @param request
     * @return StockTransResponse
     */
    @POST
    @Path("/getListMerEntityDTO/")
    public StockTransResponse getListMerEntityDto(StockTransRequest request) {
        StockTransResponse response = new StockTransResponse();
        try {

            authenticateWsBusiness.validateRequest(request.getSysUserRequest());
            List<MerEntityDTO> data = synStockTransBusiness.getListMerEntityDto(request);
            response.setLstMerEntity(data);
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
     * updateStockTrans
     *
     * @param request
     * @return StockTransResponse
     */
    @POST
    @Path("/updateStockTrans/")
    public StockTransResponse updateStockTrans(StockTransRequest request) {
        StockTransResponse response = new StockTransResponse();
        try {

            authenticateWsBusiness.validateRequest(request.getSysUserRequest());
            int result = synStockTransBusiness.updateStockTrans(request);
            if (result > 0) {
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
            resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
        }
        return response;


    }

    /**
     * updateSynStockTrans
     *
     * @param request
     * @return StockTransResponse
     */
    @POST
    @Path("/updateSynStockTrans/")
    public StockTransResponse updateSynStockTrans(StockTransRequest request) {
        StockTransResponse response = new StockTransResponse();
        try {

            authenticateWsBusiness.validateRequest(request.getSysUserRequest());
            int result = synStockTransBusiness.updateSynStockTrans(request);
            if (result > 0) {
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
            resultInfo.setMessage(e.getMessage());
            response.setResultInfo(resultInfo);
        }
        return response;

    }

    /**
     * getCongNo
     *
     * @param request
     * @return StockTransResponse
     */
    @POST
    @Path("/getCongNo/")
    public StockTransResponse getCongNo(SysUserRequest request) {
        StockTransResponse response = new StockTransResponse();
        try {

            authenticateWsBusiness.validateRequest(request);
            List<MerEntityDTO> result = synStockTransBusiness.getCongNo(request);
            response.setLstMerEntity(result);
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
     * DeliveryMaterials
     *
     * @param request
     * @return StockTransResponse
     */
    @POST
    @Path("/DeliveryMaterials/")
    public StockTransResponse DeliveryMaterials(StockTransRequest request) {
        StockTransResponse response = new StockTransResponse();
        try {
            ResultInfo resultInfo = new ResultInfo();
            int result = synStockTransBusiness.DeliveryMaterials(request, resultInfo);
            if (result > 0) {
                resultInfo.setStatus(ResultInfo.RESULT_OK);
                response.setResultInfo(resultInfo);
            } else {
                //ResultInfo resultInfo = new ResultInfo();
                resultInfo.setStatus(ResultInfo.RESULT_NOK);
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

    @POST
    @Path("/totalDelivery/")
    public StockTransResponse totalDelivery(SysUserRequest request) {
        StockTransResponse response = new StockTransResponse();
        try {
            ResultInfo resultInfo = new ResultInfo();
            CountConstructionTaskDTO result = synStockTransBusiness.countMaterials(request);
            response.setCountStockTrans(result);
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
    
    //Huypq-20191002-start
    @POST
    @Path("/DeliveryMaterialsNew/")
    public StockTransResponse DeliveryMaterialsNew(StockTransRequest request) {
        StockTransResponse response = new StockTransResponse();
        try {
            ResultInfo resultInfo = new ResultInfo();
//            hoanm1_20191012_start
//            int result = synStockTransBusiness.DeliveryMaterialsNew(request, resultInfo);
            int result = synStockTransBusiness.DeliveryMaterialsUpdate(request, resultInfo);
//            hoanm1_20191012_end
            if (result > 0) {
                resultInfo.setStatus(ResultInfo.RESULT_OK);
                response.setResultInfo(resultInfo);
            } else {
                //ResultInfo resultInfo = new ResultInfo();
                resultInfo.setStatus(ResultInfo.RESULT_NOK);
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
    //Huy-end//
    
    //tatph-start-11/2/2020
    @POST
    @Path("/DeliveryMaterialsNewV2/")
    public StockTransResponse DeliveryMaterialsNewV2(StockTransRequest request) {
        StockTransResponse response = new StockTransResponse();
        try {
            ResultInfo resultInfo = new ResultInfo();
            List<Integer> result = synStockTransBusiness.DeliveryMaterialsUpdateV2(request, resultInfo);
            List<Integer> zeroLst = new ArrayList<>();
            if(result.size() > 0) {
            	result.forEach(dto ->{
            		if(dto == 0) {
            			zeroLst.add(dto);
            		}
            	});
            }
            if (zeroLst.size() > 0) {
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
    
    @POST
    @Path("/getAppParam/")
    public StockTransResponse getAppParam(StockTransRequest request) {
        StockTransResponse response = new StockTransResponse();
        try {
            ResultInfo resultInfo = new ResultInfo();
            List<AppParamDTO> result = synStockTransBusiness.getAppParam(request);
            if (result.size() > 0) {
                resultInfo.setStatus(ResultInfo.RESULT_OK);
                response.setAppParamDTO(result.get(0));
                response.setResultInfo(resultInfo);
            } else {
            	  resultInfo.setStatus(ResultInfo.RESULT_NOK);
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
    
    @POST
    @Path("/getAttachment/")
    public StockTransResponse getAttachment(StockTransRequest request) {
        StockTransResponse response = new StockTransResponse();
        try {
            ResultInfo resultInfo = new ResultInfo();
            List<UtilAttachDocumentDTO> result = synStockTransBusiness.getListAttachmentByIdAndType(request);
            if (result.size() > 0) {
                resultInfo.setStatus(ResultInfo.RESULT_OK);
                response.setUtilAttachDocumentDTOs(result);
                response.setResultInfo(resultInfo);
            } else {
            	  resultInfo.setStatus(ResultInfo.RESULT_NOK);
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
    
    @POST
	@Path("/saveKpiLogTimeProcess/")
    @CrossOrigin
	public StockTransResponse saveKpiLogTimeProcess(StockTransRequest request) {
    	StockTransResponse response = new StockTransResponse();
		Long id = 0L;
		try {
			id = synStockTransBusiness.saveKpiLogTimeProcess(request);
			if (id < 0 || id == -1) {
				ResultInfo resultInfo = new ResultInfo();
				resultInfo.setStatus(ResultInfo.RESULT_NOK);
				response.setResultInfo(resultInfo);
			} else {
				KpiLogTimeProcessDTO dto = new KpiLogTimeProcessDTO();
				dto.setId(id);
				ResultInfo resultInfo = new ResultInfo();
				resultInfo.setStatus(ResultInfo.RESULT_OK);
				response.setResultInfo(resultInfo);
				response.setKpiLogTimeProcessDTO(dto);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			ResultInfo resultInfo = new ResultInfo();
			resultInfo.setStatus(ResultInfo.RESULT_NOK);
			response.setResultInfo(resultInfo);
		}
		return response;
	}

	@POST
	@Path("/updateKpiLogTimeProcess/")
	@CrossOrigin
	public StockTransResponse updateKpiLogTimeProcess(StockTransRequest request ) {
		StockTransResponse response = new StockTransResponse();
		try {
			synStockTransBusiness.updateKpiLogTimeProcess(request);
			ResultInfo resultInfo = new ResultInfo();
			resultInfo.setStatus(ResultInfo.RESULT_OK);
			response.setResultInfo(resultInfo);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			ResultInfo resultInfo = new ResultInfo();
			resultInfo.setStatus(ResultInfo.RESULT_NOK);
			response.setResultInfo(resultInfo);
		}
		return response;
	}
    //tatph-end-11/2/2020
}
