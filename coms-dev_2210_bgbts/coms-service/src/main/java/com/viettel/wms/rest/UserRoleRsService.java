/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.wms.rest;

import com.viettel.coms.dto.SysUserCOMSDTO;
import com.viettel.wms.dto.UserRoleDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author HungLQ9
 */
public interface UserRoleRsService {

    @GET
    @Path("/userRole")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getUserRole();

    @GET
    @Path("/userRole/{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getUserRoleById(@PathParam("id") Long id);

    @PUT
    @Path("/userRole/")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateUserRole(UserRoleDTO obj);

    @POST
    @Path("/userRole/")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response addUserRole(UserRoleDTO obj);

    @DELETE
    @Path("/userRole/{id}/")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteUserRole(@PathParam("id") Long id);

    @POST
    @Path("/userRole/doSearchUserRole")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchUserRole(UserRoleDTO obj);

    @POST
    @Path("/userRole/remove")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response remove(UserRoleDTO obj);

    @POST
    @Path("/userRole/doSearchRole")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchRole(UserRoleDTO obj);

    @POST
    @Path("/userRole/doSearchUserRoleData")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response doSearchUserRoleData(UserRoleDTO obj);

    @POST
    @Path("/userRole/deleteUserRoleData")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteUserRoleData(UserRoleDTO obj);

    @POST
    @Path("/userRole/insertUserRoleData")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response insertUserRoleData(List<UserRoleDTO> obj);

    @POST
    @Path("/userRole/getForAutoCompleteUserRoleData")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getForAutoCompleteUserRoleData(UserRoleDTO obj);

    @POST
    @Path("/userRole/addRole")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response addRole(List<UserRoleDTO> list);


    @POST
    @Path("/userRole/getForAutoComplete")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getForAutoComplete(SysUserCOMSDTO obj);

    @POST
    @Path("/userRole/getForAutoCompleteSysUser")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getForAutoCompleteSysUser(SysUserCOMSDTO obj);

    @POST
    @Path("/userRole/getAllUserRole")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAllUserRole(UserRoleDTO obj);

}
