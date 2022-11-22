package com.viettel.coms.rest;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.viettel.asset.business.AssetReportBusiness;
import com.viettel.asset.dto.AssetReportS21SearchDto;
import com.viettel.cat.dto.CatProvinceDTO;
import com.viettel.coms.business.TangentCustomerBusiness;
import com.viettel.coms.dto.AppParamDTO;
import com.viettel.coms.dto.ConfigStaffTangentDTO;
import com.viettel.coms.dto.ManageCertificateDTO;
import com.viettel.coms.dto.ResultSolutionDTO;
import com.viettel.coms.dto.ResultTangentDTO;
import com.viettel.coms.dto.TangentCustomerDTO;
import com.viettel.ktts2.common.ResponseMessage;

public interface TangentCustomerRsService {
	

	@POST
	@Path("/doSearch")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearch(TangentCustomerDTO obj) throws ParseException;

	@POST
	@Path("/save")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response save(TangentCustomerDTO obj) throws Exception;

	@POST
	@Path("/update")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response update(TangentCustomerDTO obj) throws Exception;

	@POST
	@Path("/doSearchDistrictByProvinceCode")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearchDistrictByProvinceCode(TangentCustomerDTO obj);

	@POST
	@Path("/doSearchCommunneByDistrict")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearchCommunneByDistrict(TangentCustomerDTO obj);

	@POST
	@Path("/getUserConfigTagent")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getUserConfigTagent(ConfigStaffTangentDTO obj);

	@POST
	@Path("/deleteRecord")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response deleteRecord(TangentCustomerDTO obj);

	@POST
	@Path("/saveDetail")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response saveDetail(TangentCustomerDTO obj);

	@POST
	@Path("/saveApproveOrReject")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response saveApproveOrReject(ResultTangentDTO obj);

	@POST
	@Path("/saveNotDemain")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response saveNotDemain(ResultTangentDTO obj);

	@POST
	@Path("/checkRoleApproved")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response checkRoleApproved();

	@POST
	@Path("/getListResultTangentByTangentCustomerId")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getListResultTangentByTangentCustomerId(ResultTangentDTO obj);

	@POST
	@Path("/getAllContractXdddSuccess")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getAllContractXdddSuccess(ResultSolutionDTO obj);

	@POST
	@Path("/getResultSolutionByContractId")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getResultSolutionByContractId(Long contractId);

	@POST
	@Path("/saveResultSolution")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response saveResultSolution(TangentCustomerDTO obj);

	@POST
	@Path("/saveApproveOrRejectGiaiPhap")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response saveApproveOrRejectGiaiPhap(ResultSolutionDTO obj);

	@POST
	@Path("/getListResultTangentJoinSysUserByTangentCustomerId")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getListResultTangentJoinSysUserByTangentCustomerId(ResultTangentDTO obj);

	@POST
	@Path("/getResultSolutionJoinSysUserByTangentCustomerId")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getResultSolutionJoinSysUserByTangentCustomerId(Long id);

	@POST
	@Path("/getListResultTangentByResultTangentId")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getListResultTangentByResultTangentId(ResultTangentDTO obj);

	@POST
	@Path("/checkRoleUpdate")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response checkRoleUpdate();

	@POST
	@Path("/getContractRose")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getContractRose();

	@POST
	@Path("/doSearchProvince")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearchProvince(CatProvinceDTO obj);

	@POST
	@Path("/approveRose")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response approveRose(ResultSolutionDTO obj) throws Exception;

	@POST
	@Path("/checkRoleSourceYCTX")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response checkRoleSourceYCTX(TangentCustomerDTO obj) throws Exception;

	@POST
	@Path("/getAllContractXdddSuccessInternal")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getAllContractXdddSuccessInternal(ResultSolutionDTO obj);

	@POST
	@Path("/checkRoleCreated")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response checkRoleCreated();

	@POST
	@Path("/getChannel")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getChannel(AppParamDTO obj);

	@POST
	@Path("/exportExcelqlyctx")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response exportExcelqlyctx(TangentCustomerDTO obj) throws Exception;
	
	@POST
    @Path("/exportExcelDetailData")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response exportExcelDetailData(TangentCustomerDTO obj) throws Exception ;
	
	@POST
    @Path("/getUserConfigTagentByProvince")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getUserConfigTagentByProvince(ConfigStaffTangentDTO obj);
	
	@POST
	@Path("/checkRoleUserAssignYctx")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response checkRoleUserAssignYctx();

	@POST
	@Path("/getCallbotConversation")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getCallbotConversation(TangentCustomerDTO obj);
}
