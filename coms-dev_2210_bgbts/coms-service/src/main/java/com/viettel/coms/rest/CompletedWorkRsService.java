package com.viettel.coms.rest;

import com.viettel.coms.dto.ConstructionTaskDailyDTO;
import com.viettel.coms.dto.WorkItemDetailDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public interface CompletedWorkRsService {
    @POST
    @Path("/doSearch")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearch(WorkItemDetailDTO obj);

    @POST
    @Path("/approveCompletedWork")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response approveCompletedWork(WorkItemDetailDTO obj);

    @POST
    @Path("/rejectCompletedWork")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response rejectCompletedWork(WorkItemDetailDTO obj);

    @POST
    @Path("/approveCompletedWorkChecked")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response approveCompletedWorkChecked(List<WorkItemDetailDTO> lstObj);
}
