package com.viettel.coms.rest;

import com.viettel.coms.dto.WO_DOANHTHU_GPTH_CHECKLIST_DTO;
import com.viettel.coms.dto.WoDTO;
import com.viettel.coms.dto.WoTypeDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public interface DoanhThuCheckListService {
    @POST
    @Path("/doanhThuCheckList/getNameProgress")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getNameProgress(WO_DOANHTHU_GPTH_CHECKLIST_DTO dto) throws Exception;

    @POST
    @Path("/updateNameProgress")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateNameProgress(WO_DOANHTHU_GPTH_CHECKLIST_DTO dto) throws Exception;

    @POST
    @Path("/deleteDoanhThuCheckList")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteDoanhThuCheckList(WO_DOANHTHU_GPTH_CHECKLIST_DTO dto) throws Exception;

    @POST
    @Path("/createDoanhThuCheckList")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createDoanhThuCheckList(WO_DOANHTHU_GPTH_CHECKLIST_DTO dto) throws Exception;

    @POST
    @Path("/doanhThuCheckList/getById")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getWOTypeById(WO_DOANHTHU_GPTH_CHECKLIST_DTO dto) throws Exception;

    @POST
    @Path("/createDTO")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createDTO(WO_DOANHTHU_GPTH_CHECKLIST_DTO dto) throws Exception;

}
