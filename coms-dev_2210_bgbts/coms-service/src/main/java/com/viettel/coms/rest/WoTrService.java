package com.viettel.coms.rest;

import com.viettel.coms.dto.*;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public interface WoTrService {
    @POST
    @Path("/tr/createTR")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createTR(WoTrDTO dto) throws Exception;

    @POST
    @Path("/tr/createManyTR")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createManyTR(List<WoTrDTO> trDto, @Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/tr/updateTR")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateTR(WoTrDTO trDto) throws Exception;

    @POST
    @Path("/tr/deleteTR")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteTR(WoTrDTO trDto) throws Exception;

    @POST
    @Path("/tr/changeStateTr")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response changeStateTr(WoTrDTO trDto) throws Exception;

    @POST
    @Path("/tr/getOneTRRaw")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getOneTRRaw(WoTrDTO trDto) throws Exception;

    @POST
    @Path("/tr/getOneTRDetails")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getOneTRDetails(WoTrDTO trDto) throws Exception;

    @POST
    @Path("/tr/getListTRDetails")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getListTRDetails(WoTrDTO trDto) throws Exception;

    @POST
    @Path("/tr/doSearch")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchTR(WoTrDTO trDto, @Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/tr/doSearchAvailable")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchAvailableTR(WoTrDTO trDto, @Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/tr/giveAssignmentToCD")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response giveAssignmentToCD(WoTrDTO trDto) throws Exception;

    @POST
    @Path("/tr/cdRejectAssignment")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response cdReject(WoTrDTO trDto) throws Exception;

    @POST
    @Path("/tr/cdAcceptAssignment")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response cdAccept(WoTrDTO trDto) throws Exception;

    @POST
    @Path("/tr/getImportExcelTemplate")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_OCTET_STREAM})
    public Response getImportExcelTemplate(WoTrDTO trDto, @Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/trType/createTRType")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createTRType(WoTrTypeDTO dto) throws Exception;

    @POST
    @Path("/trType/createManyTRType")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createManyTRType(List<WoTrTypeDTO> dto) throws Exception;

    @POST
    @Path("/trType/getOneTRTypeDetails")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getOneTRTypeDetails(WoTrTypeDTO dto) throws Exception;

    @POST
    @Path("/trType/updateTRType")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateTRType(WoTrTypeDTO dto) throws Exception;

    @POST
    @Path("/trType/deleteTRType")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteTRType(WoTrTypeDTO dto) throws Exception;

    @POST
    @Path("/trType/getListTRType")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getListTRType(WoTrTypeDTO dto) throws Exception;

    @POST
    @Path("/trType/doSearchTRType")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchTRType(WoTrTypeDTO trTypeDto) throws Exception;

    @POST
    @Path("/tr/getCdLevel1")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getCdLevel1(WoTrTypeDTO trTypeDto) throws Exception;

    @POST
    @Path("/tr/getAvailableProjects")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAvailableProjects(WoTrDTO trDto) throws Exception;

    @POST
    @Path("/tr/doSearchProjects")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchProjects(WoTrDTO trDto) throws Exception;

    @POST
    @Path("/tr/getAvailableContracts")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAvailableContracts(WoTrDTO trDto) throws Exception;

    @POST
    @Path("/tr/doSearchContracts")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchContracts(WoTrDTO trDto, @Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/tr/doSearchAIOContracts")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchAIOContracts(WoTrDTO trDto) throws Exception;

    @POST
    @Path("/tr/doSearchConstruction")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchConstruction(WoTrDTO trDto) throws Exception;

    @POST
    @Path("/tr/getConstructionByCode")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getConstructionByCode(WoTrDTO trDto) throws Exception;

    @POST
    @Path("/tr/getConstructionByContract")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getConstructionByContract(WoTrDTO trDto) throws Exception;

    @POST
    @Path("/tr/getConstructionByProject")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getConstructionByProject(WoTrDTO trDto) throws Exception;

    @POST
    @Path("/tr/getStationById")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getStationById(WoTrDTO trDto) throws Exception;

    @POST
    @Path("/tr/getCdLevel2FromStation")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getCdLevel2FromStation(WoTrDTO trDto) throws Exception;

    @POST
    @Path("/tr/doSearchStation")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchStation(WoTrDTO trDto) throws Exception;

    @POST
    @Path("/aioPackages/getByContract")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAIOPackagesByContract(WoTrDTO dto) throws Exception;

    @POST
    @Path("/tr/getHistory")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getTrHistory(WoTrDTO dto) throws Exception;

    @POST
    @Path("/trType/getTrTypeImportTemplate")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_OCTET_STREAM})
    public Response getTrTypeImportTemplate() throws Exception;

    @POST
    @Path("/group/getSysGroupById")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getSysGroupById(WoSimpleSysGroupDTO dto) throws Exception;

    @POST
    @Path("/tr/exportTrExcel")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_OCTET_STREAM})
    public Response exportTrExcel(WoTrDTO trDto, @Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/trType/exportTrTypeExcel")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_OCTET_STREAM})
    public Response exportTrTypeExcel(WoTrTypeDTO dto, @Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/tr/doSearchStations")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchStations(WoSimpleStationDTO dto) throws Exception;

    @POST
    @Path("/tr/createManyTRReport")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_OCTET_STREAM})
    public Response createManyTRReport(List<WoTrDTO> dtos) throws Exception;

    @POST
    @Path("/importFileZip")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response importFileZip(Attachment attachments, @Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/exportFileZip")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportFileZip (WoTrDTO obj, @Context HttpServletRequest request);

    @POST
    @Path("/tr/doSearchStationByContract")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchStationByContract(WoSimpleStationDTO dto) throws Exception;

    @POST
    @Path("/woConfigContract/searchWoConfigContract")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response searchWoConfigContract(WoConfigContractCommitteeDTO trDto, @Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/woConfigContract/getFtList")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getFtList(WoConfigContractCommitteeDTO trDto, @Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/woConfigContract/doSearchWoConfigContract")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchWoConfigContract(WoConfigContractCommitteeDTO dto) throws Exception;
    @POST
    @Path("/woConfigContract/createWoConfigContract")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createWoConfigContract(WoConfigContractCommitteeDTO dto,@Context HttpServletRequest request) throws Exception;
    @POST
    @Path("/woConfigContract/getOneWoConfigContract")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getOneWoConfigContract(WoConfigContractCommitteeDTO dto) throws Exception;

    @POST
    @Path("/woConfigContract/updateWoConfigContract")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateWoConfigContract(WoConfigContractCommitteeDTO dto,@Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/woConfigContract/deleteWoConfigContract")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteWoConfigContract(WoConfigContractCommitteeDTO dto,@Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/woConfigContract/exportExcelWoConfigContract")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response exportExcelWoConfigContract(@Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/woConfigContract/getFtListByContract")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getFtListByContract(WoConfigContractCommitteeDTO trDto, @Context HttpServletRequest request) throws Exception;

    @POST
    @Path("/workItem/getAutoWoWorkItems")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAutoWoWorkItems(CatWorkItemTypeDTO dto) throws Exception;

    @POST
    @Path("/workItem/getCatConstructionTypes")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getCatConstructionTypes() throws Exception;

    @POST
    @Path("/workItem/getInactiveWoList")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getInactiveWoList(WoDTO dto) throws Exception;

    @POST
    @Path("/woConfigContract/getListCD5ByContract")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getListCD5ByContract(WoConfigContractCommitteeDTO trDto) throws Exception;
    
    @POST
    @Path("/tr/doSearchContruction")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchContruction(ConstructionDTO dto) throws Exception;

    @POST
    @Path("/tmbt/getRentStation")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getRentStation(WoTrDTO trDTO) throws Exception;

    @POST
    @Path("/tmbt/getListStationByBranch")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getListStationByBranch(WoTrDTO trDTO) throws Exception;

    @POST
    @Path("/tmbt/changeTmbtStation")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response changeTmbtStation(CatStationDTO dto) throws Exception;
    
    //Huypq-09072021-start
    @POST
    @Path("/tmbt/getExcelTemplate")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getExcelTemplate(CatStationDTO obj) throws Exception;
    
    @POST
	@Path("/tmbt/importFileTrTmbt")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response importFileTrTmbt(Attachment attachments, @Context HttpServletRequest request) throws Exception;
    //Huy-end
}
