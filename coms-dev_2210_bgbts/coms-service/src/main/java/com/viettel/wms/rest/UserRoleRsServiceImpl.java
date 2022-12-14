/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.wms.rest;

import com.viettel.coms.dto.SysUserCOMSDTO;
import com.viettel.service.base.dto.DataListDTO;
import com.viettel.wms.business.UserRoleBusinessImpl;
import com.viettel.wms.dto.UserRoleDTO;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author HungLQ9
 */
public class UserRoleRsServiceImpl implements UserRoleRsService {

    //    protected final Logger log = Logger.getLogger(UserRsService.class);
    @Autowired
    UserRoleBusinessImpl userRoleBusinessImpl;

    @Override
    public Response getUserRole() {
        List<UserRoleDTO> ls = userRoleBusinessImpl.getAll();
        if (ls == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            DataListDTO data = new DataListDTO();
            data.setData(ls);
            data.setTotal(ls.size());
            data.setSize(ls.size());
            data.setStart(1);
            return Response.ok(data).build();
        }
    }

    @Override
    public Response getUserRoleById(Long id) {
        UserRoleDTO obj = (UserRoleDTO) userRoleBusinessImpl.getOneById(id);
        if (obj == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.ok(obj).build();
        }
    }

    @Override
    public Response updateUserRole(UserRoleDTO obj) {
        Long id = userRoleBusinessImpl.update(obj);
        if (id == 0l) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.ok().build();
        }

    }

    @Override
    public Response addUserRole(UserRoleDTO obj) {
        Long id = userRoleBusinessImpl.save(obj);
        if (id == 0l) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.ok(Response.Status.CREATED).build();
        }
    }

    @Override
    public Response deleteUserRole(Long id) {
        UserRoleDTO obj = (UserRoleDTO) userRoleBusinessImpl.getOneById(id);
        if (obj == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            userRoleBusinessImpl.delete(obj);
            return Response.ok(Response.Status.NO_CONTENT).build();
        }
    }

    //H??m hi???n th??? danh s??ch vai tr?? ???? ???????c g??n cho ng?????i d??ng
    @Override
    public Response doSearchUserRole(UserRoleDTO obj) {
        // TODO Auto-generated method stub
        DataListDTO data = userRoleBusinessImpl.doSearchUserRole(obj);
        if (data == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.ok(data).build();
        }
    }

    //End
// H??m x??a vai tr?? ??ang ???????c g??n cho ng?????i d??ng(Update is_active =0 trong b???ng UserRole)
    @Override
    public Response remove(UserRoleDTO obj) {
        obj.setIsActive((long) 0);
        Long id = userRoleBusinessImpl.update(obj);
        if (id == 0l) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.ok().build();
        }
    }

    //End
    //H??m hi???n th??? danh s??ch vai tr?? ch??a ???????c g??n cho ng?????i d??ng
    @Override
    public Response doSearchRole(UserRoleDTO obj) {
        DataListDTO data = userRoleBusinessImpl.doSearchRole(obj);
        if (data == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.ok(data).build();
        }
    }

    //B??? sung vai tr?? c???a ng?????i d??ng
    @Override
    public Response addRole(List<UserRoleDTO> list) {
        if (userRoleBusinessImpl.insertRole(list)) {
            return null;
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    //End
    //Hi???n th??? danh s??ch c??c kho vai tr??
    @Override
    public Response doSearchUserRoleData(UserRoleDTO obj) {
        List<UserRoleDTO> data = userRoleBusinessImpl.doSearchUserRoleData(obj);
        if (data == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.ok(data).build();
        }
    }

    //End
    //AutocompleteStock
    @Override
    public Response getForAutoCompleteUserRoleData(UserRoleDTO obj) {
        // TODO Auto-generated method stub
        return Response.ok(userRoleBusinessImpl.getForAutoCompleteUserRoleData(obj)).build();
    }
    //End

    //X??a kho vai tr??
    @Override
    public Response deleteUserRoleData(UserRoleDTO obj) {
        if (userRoleBusinessImpl.deleteUserRoleData(obj)) {
            return null;
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

    }

    //End
    //H??m b??? sung kho vai tr??
    @Override
    public Response insertUserRoleData(List<UserRoleDTO> obj) {
        if (userRoleBusinessImpl.insertUserRoleData(obj)) {
            return null;
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    //End
    //AutocompleteUser
    @Override
    public Response getForAutoComplete(SysUserCOMSDTO obj) {
        return Response.ok(userRoleBusinessImpl.getForAutoComplete(obj)).build();
    }

    //End
    @Override
    public Response getForAutoCompleteSysUser(SysUserCOMSDTO obj) {
        return Response.ok(userRoleBusinessImpl.getForAutoCompleteSysUser(obj)).build();
    }

    @Override
    public Response getAllUserRole(UserRoleDTO obj) {
        return Response.ok(userRoleBusinessImpl.getAllUserRole(obj)).build();
    }


}
