package com.viettel.coms.rest;

import java.text.ParseException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.viettel.cat.dto.CatProvinceDTO;
import com.viettel.coms.dto.AppParamDTO;
import com.viettel.coms.dto.ConfigStaffTangentDTO;
import com.viettel.coms.dto.ResultSolutionGPTHDTO;
import com.viettel.coms.dto.ResultTangentGPTHDTO;
import com.viettel.coms.dto.ResultTangentGPTHDTO;
import com.viettel.coms.dto.TangentCustomerGPTHDTO;
import com.viettel.coms.dto.TangentCustomerGPTHDTO;
public interface TangentCustomerGPTHRsService {
	

	@POST
	@Path("/doSearch")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearch(TangentCustomerGPTHDTO obj) throws ParseException;

	@POST
	@Path("/save")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response save(TangentCustomerGPTHDTO obj) throws Exception;

	@POST
	@Path("/update")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response update(TangentCustomerGPTHDTO obj) throws Exception;
	
	@POST
	@Path("/getListResultTangentByTangentCustomerId")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getListResultTangentByTangentCustomerId(TangentCustomerGPTHDTO obj);

	@POST
	@Path("/doSearchDistrictByProvinceCode")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearchDistrictByProvinceCode(TangentCustomerGPTHDTO obj);

	@POST
	@Path("/doSearchCommunneByDistrict")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response doSearchCommunneByDistrict(TangentCustomerGPTHDTO obj);

	@POST
	@Path("/getUserConfigTagent")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getUserConfigTagent(ConfigStaffTangentDTO obj);

	@POST
	@Path("/deleteRecord")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response deleteRecord(TangentCustomerGPTHDTO obj);

	@POST
	@Path("/saveDetail")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response saveDetail(TangentCustomerGPTHDTO obj);

	@POST
	@Path("/saveApproveOrReject")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response saveApproveOrReject(ResultTangentGPTHDTO obj);

	@POST
	@Path("/saveNotDemain")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response saveNotDemain(ResultTangentGPTHDTO obj);

	@POST
	@Path("/checkRoleApproved")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response checkRoleApproved();

	@POST
	@Path("/getAllContractXdddSuccess")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getAllContractXdddSuccess(ResultSolutionGPTHDTO obj);

	@POST
	@Path("/getResultSolutionByContractId")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getResultSolutionByContractId(Long contractId);

	@POST
	@Path("/saveResultSolution")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response saveResultSolution(TangentCustomerGPTHDTO obj);

	@POST
	@Path("/saveApproveOrRejectGiaiPhap")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response saveApproveOrRejectGiaiPhap(ResultSolutionGPTHDTO obj);

	@POST
	@Path("/getListResultTangentJoinSysUserByTangentCustomerId")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getListResultTangentJoinSysUserByTangentCustomerId(ResultTangentGPTHDTO obj);

	@POST
	@Path("/getResultSolutionJoinSysUserByTangentCustomerId")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getResultSolutionJoinSysUserByTangentCustomerId(Long id);

	@POST
	@Path("/getListResultTangentByResultTangentId")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getListResultTangentByResultTangentId(ResultTangentGPTHDTO obj);

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
	public Response approveRose(ResultSolutionGPTHDTO obj) throws Exception;

	@POST
	@Path("/checkRoleSourceYCTX")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response checkRoleSourceYCTX(TangentCustomerGPTHDTO obj) throws Exception;

	@POST
	@Path("/getAllContractXdddSuccessInternal")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getAllContractXdddSuccessInternal(ResultSolutionGPTHDTO obj);

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
	@Path("/exportFile")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response exportFile(TangentCustomerGPTHDTO obj) throws Exception;
	
	@POST
    @Path("/exportExcelDetailData")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response exportExcelDetailData(TangentCustomerGPTHDTO obj) throws Exception ;
	
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
	public Response getCallbotConversation(TangentCustomerGPTHDTO obj);
}
