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

    //Hàm hiển thị danh sách vai trò đã được gán cho người dùng
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
// Hàm xóa vai trò đang được gán cho người dùng(Update is_active =0 trong bảng UserRole)
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
    //Hàm hiển thị danh sách vai trò chưa được gán cho người dùng
    @Override
    public Response doSearchRole(UserRoleDTO obj) {
        DataListDTO data = userRoleBusinessImpl.doSearchRole(obj);
        if (data == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.ok(data).build();
        }
    }

    //Bổ sung vai trò của người dùng
    @Override
    public Response addRole(List<UserRoleDTO> list) {
        if (userRoleBusinessImpl.insertRole(list)) {
            return null;
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    //End
    //Hiển thị danh sách các kho vai trò
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

    //Xóa kho vai trò
    @Override
    public Response deleteUserRoleData(UserRoleDTO obj) {
        if (userRoleBusinessImpl.deleteUserRoleData(obj)) {
            return null;
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

    }

    //End
    //Hàm bổ sung kho vai trò
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
