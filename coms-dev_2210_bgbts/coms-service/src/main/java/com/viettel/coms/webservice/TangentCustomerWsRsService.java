package com.viettel.coms.webservice;


import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.viettel.asset.business.AuthenticateWsBusiness;
import com.viettel.asset.dto.ResultInfo;
import com.viettel.coms.business.HolidayBusinessImpl;
import com.viettel.coms.business.TangentCustomerBusinessImpl;
import com.viettel.coms.dto.*;
import com.viettel.erp.dto.SysUserDTO;
import com.viettel.service.base.dto.DataListDTO;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import java.util.List;

import com.viettel.cat.dto.ConstructionImageInfo;

@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON + ";charset=utf-8", MediaType.APPLICATION_XML})
@Path("/service")
public class TangentCustomerWsRsService {
    private Logger LOGGER = Logger.getLogger(TangentCustomerWsRsService.class);

    @Context
	HttpServletRequest req;
    
    @Autowired
    TangentCustomerBusinessImpl tangentCustomerBusinessImpl;
    @Autowired
    AuthenticateWsBusiness authenticateWsBusiness;
    @POST
    @Path("/getCountStateTangentCustomer/")
    public TangentCustomerResponse getCountStateTangentCustomer(SysUserRequest request) {
    	TangentCustomerResponse response = new TangentCustomerResponse();
        try {
        	TangentCustomerDTO data = tangentCustomerBusinessImpl.getCountStateTangentCustomer(request);
            response.setTangentCustomerDTO(data);
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
    @Path("/getListTangentCustomer/")
    public TangentCustomerResponse getListTangentCustomer(SysUserRequest request) {
    	TangentCustomerResponse response = new TangentCustomerResponse();
        try {
            List<TangentCustomerDTO> data = tangentCustomerBusinessImpl.getListTangentCustomer(request);
            response.setLstTangentCustomerDTO(data);
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
    @Path("/updateTangentCustomer/")
    public TangentCustomerResponse updateTangentCustomer(TangentCustomerRequest request) {
    	TangentCustomerResponse response = new TangentCustomerResponse();
        try {
            int result = tangentCustomerBusinessImpl.updateTangentCustomer(request);
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
    @Path("/getImagesResultSolution/")
    public TangentCustomerResponse getImagesResultSolution(TangentCustomerRequest request) {
    	TangentCustomerResponse response = new TangentCustomerResponse();

        List<ConstructionImageInfo> listImage = tangentCustomerBusinessImpl.getImagesResultSolution(request);
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
    @POST
    @Path("/getListContract/")
    public TangentCustomerResponse getListContract(SysUserRequest request) {
    	TangentCustomerResponse response = new TangentCustomerResponse();
        try {
            List<TangentCustomerDTO> data = tangentCustomerBusinessImpl.getListContract(request);
            response.setLstTangentCustomerDTO(data);
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
    @Path("/addTangentCustomer/")
    public TangentCustomerResponse addTangentCustomer(TangentCustomerRequest request) throws Exception {
    	TangentCustomerResponse response = new TangentCustomerResponse();
    	AppParamDTO obj = new AppParamDTO(); 
    	List<String> lstParType = Lists.newArrayList();
		lstParType.add("RECEPTION_CHANNEL");
		obj.setLstParType(lstParType);
		DataListDTO data = tangentCustomerBusinessImpl.getChannel(obj);
		List<AppParamDTO> listValidate = data.getData();
		TangentCustomerDTO draft = new TangentCustomerDTO();
		System.out.println("getTangentCustomerDTO: " + new Gson().toJson(request.getTangentCustomerDTO()));
		System.out.println("getLstTangentCustomer: " + new Gson().toJson(request.getLstTangentCustomer()));
		if(request.getTangentCustomerDTO()!=null && StringUtils.isNotBlank(request.getTangentCustomerDTO().getUserName())) {
			draft.setUserName(request.getTangentCustomerDTO().getUserName());
		} else {
			draft.setUserName(request.getLstTangentCustomer()!=null ? request.getLstTangentCustomer().get(0).getUserName() : null);
		}
        try {
        	draft.setCreatedUser(request.getSysUserRequest().getSysUserId());
        	Long IDRole = tangentCustomerBusinessImpl.checkRoleSourceYCTX(draft, req);
        	if(request.getLstTangentCustomer()!=null && request.getLstTangentCustomer().size() > 0) {
        		List<TangentCustomerDTO> result = request.getLstTangentCustomer();
        		for(TangentCustomerDTO tangent :result) {
        			if (IDRole == 3l) {
        				tangent.setGroupOrder(1l);
            			if(listValidate.stream()
                				.filter(t -> t.getName().equals(request.getTangentCustomerDTO().getReceptionChannel())).findAny()
                				.isPresent() == false) {
                    		
            				tangent.setReceptionChannel("Khác");
                    	}
            		}else {
            			tangent.setGroupOrder(2l);
            		}
        			request.setSource(IDRole.toString());
        		}
        	}else {
        		request.getTangentCustomerDTO().setSource(IDRole.toString());
        		request.getTangentCustomerDTO().setProvinceCode(request.getTangentCustomerDTO().getProvinceName());
        		request.getTangentCustomerDTO().setCreatedUser(draft.getSysUserId());
        	}

            Long result = tangentCustomerBusinessImpl.addTangentCustomer(request);     
            if (result == 1L) {
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setStatus(ResultInfo.RESULT_OK);
                resultInfo.setMessage("Thêm mới thành công");
                response.setResultInfo(resultInfo);
            } else {
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setStatus(ResultInfo.RESULT_NOK);
                resultInfo.setMessage("Có lỗi xảy ra");
                response.setResultInfo(resultInfo);
            }
            
        
        }catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setStatus(ResultInfo.RESULT_NOK);
            resultInfo.setMessage("Có lỗi xảy ra");
            response.setResultInfo(resultInfo);
        }
        return response;
    }
    @POST
    @Path("/updateTangentCustomerCreate/")
    public TangentCustomerResponse updateTangentCustomerCreate(TangentCustomerRequest request) {
    	TangentCustomerResponse response = new TangentCustomerResponse();
        try {
            Long result = tangentCustomerBusinessImpl.updateTangentCustomerCreate(request);
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
    @Path("/getDataProvinceCity/")
    public TangentCustomerResponse getDataProvinceCity() {
    	TangentCustomerResponse response = new TangentCustomerResponse();
        try {
            List<TangentCustomerDTO> data = tangentCustomerBusinessImpl.getDataProvinceCity();
            response.setAreaProvinceCity(data);
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
    
    //Lấy quận/huyện
    @POST
    @Path("/getDataDistrict/")
    public TangentCustomerResponse getDataDistrict(TangentCustomerRequest request) {
    	TangentCustomerResponse response = new TangentCustomerResponse();
        try {
            List<TangentCustomerDTO> data = tangentCustomerBusinessImpl.getDataDistrict(request.getTangentCustomerDTO());
            response.setAreaDistrict(data);
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
    
  //Lấy xã/phường
    @POST
    @Path("/getDataWard/")
    public TangentCustomerResponse getDataWard(TangentCustomerRequest request) {
    	TangentCustomerResponse response = new TangentCustomerResponse();
        try {
            List<TangentCustomerDTO> data = tangentCustomerBusinessImpl.getDataWard(request.getTangentCustomerDTO());
            response.setAreaWard(data);
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
}
