/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.wms.webservice;

import com.viettel.service.base.pojo.ConditionBean;
import com.viettel.wms.dto.UserRoleDTO;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.Date;
import java.util.List;

/**
 * @author hunglq9
 */
@org.apache.cxf.feature.Features(features = "org.apache.cxf.feature.LoggingFeature")
@WebService(targetNamespace = "http://webservice.wms.viettel.com/")
public interface UserRoleWS {

    @WebMethod(operationName = "getTotal")
    long getTotal() throws Exception;


    @WebMethod(operationName = "getAll")
    public List<UserRoleDTO> getAll() throws java.lang.Exception;

    @WebMethod(operationName = "getOneById")
    UserRoleDTO getOneById(Long costCenterId) throws Exception;

    @WebMethod(operationName = "save")
    Long save(UserRoleDTO costCenterBO) throws Exception;

    @WebMethod(operationName = "delete")
    void delete(UserRoleDTO costCenterBO) throws Exception;

    @WebMethod(operationName = "searchByHql")
    List<UserRoleDTO> searchByHql(String hql, List<ConditionBean> conditionBeans) throws Exception;

    @WebMethod(operationName = "searchByHql2")
    List<UserRoleDTO> searchByHql(String hql, List<ConditionBean> conditionBeans, int startIdx, int endIdx)
            throws Exception;

    @WebMethod(operationName = "countByHql")
    Long countByHql(String hql, List<ConditionBean> conditionBeans) throws Exception;

    @WebMethod(operationName = "executeByHql")
    int executeByHql(String hql, List<ConditionBean> conditionBeans) throws Exception;

    @WebMethod(operationName = "getSysDate")
    Date getSysDate() throws Exception;

    @WebMethod(operationName = "getNextValSequence")
    Long getNextValSequence(String sequense) throws Exception;
}
