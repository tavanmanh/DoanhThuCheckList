/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.coms.webservice;

import com.viettel.coms.dto.ContructionLandHandoverPlanDTO;
import com.viettel.service.base.pojo.ConditionBean;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.Date;
import java.util.List;

/**
 * @author hunglq9
 */
@org.apache.cxf.feature.Features(features = "org.apache.cxf.feature.LoggingFeature")
@WebService(targetNamespace = "http://webservice.erp.viettel.com/")
public interface ContructionLandHandoverPlanWS {

    @WebMethod(operationName = "getTotal")
    long getTotal() throws Exception;


    @WebMethod(operationName = "getAll")
    public List<ContructionLandHandoverPlanDTO> getAll() throws java.lang.Exception;

    @WebMethod(operationName = "getOneById")
    ContructionLandHandoverPlanDTO getOneById(Long costCenterId) throws Exception;

    @WebMethod(operationName = "save")
    Long save(ContructionLandHandoverPlanDTO costCenterBO) throws Exception;

    @WebMethod(operationName = "delete")
    void delete(ContructionLandHandoverPlanDTO costCenterBO) throws Exception;

    @WebMethod(operationName = "searchByHql")
    List<ContructionLandHandoverPlanDTO> searchByHql(String hql, List<ConditionBean> conditionBeans) throws Exception;

    @WebMethod(operationName = "searchByHql2")
    List<ContructionLandHandoverPlanDTO> searchByHql(String hql, List<ConditionBean> conditionBeans, int startIdx, int endIdx)
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
