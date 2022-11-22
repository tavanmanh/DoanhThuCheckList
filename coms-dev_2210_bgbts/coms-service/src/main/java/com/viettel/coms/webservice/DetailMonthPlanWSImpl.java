/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.coms.webservice;

/**
 * @author hunglq9
 */

import com.viettel.coms.business.DetailMonthPlanBusinessImpl;
import com.viettel.coms.dto.DetailMonthPlanDTO;
import com.viettel.service.base.pojo.ConditionBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jws.WebService;
import java.util.Date;
import java.util.List;


@WebService(endpointInterface = "com.viettel.coms.webservice.DetailMonthPlanWS")

public class DetailMonthPlanWSImpl implements DetailMonthPlanWS {

    @Autowired
    DetailMonthPlanBusinessImpl detailMonthPlanImpl;
    Logger logger = Logger.getLogger(DetailMonthPlanWSImpl.class);

    @Override
    public long getTotal() throws Exception {
        try {
            return detailMonthPlanImpl.count();
        } catch (Exception ex) {
            logger.error(ex);
            throw ex;
        }
    }

    @Override
    public List<DetailMonthPlanDTO> getAll() throws Exception {
        try {
            return detailMonthPlanImpl.getAll(); //To change body of generated methods, choose Tools | Templates.
        } catch (Exception ex) {
            logger.error(ex);
            throw ex;
        }
    }

    public DetailMonthPlanBusinessImpl getDetailMonthPlanBusinessImpl() {
        return detailMonthPlanImpl;

    }

    public void setDetailMonthPlanBusinessImpl(DetailMonthPlanBusinessImpl detailMonthPlanImpl) {
        this.detailMonthPlanImpl = detailMonthPlanImpl;
    }

    @Override
    public DetailMonthPlanDTO getOneById(Long costCenterId) throws Exception {
        try {
            return (DetailMonthPlanDTO) this.detailMonthPlanImpl.getOneById(costCenterId);

        } catch (Exception ex) {
            logger.error(ex);
            throw ex;
        }
    }

    @Override
    public Long save(DetailMonthPlanDTO costCenterBO) throws Exception {
        try {
            return this.detailMonthPlanImpl.save(costCenterBO);
        } catch (Exception ex) {
            logger.error(ex);
            throw ex;
        }
    }

    @Override
    public void delete(DetailMonthPlanDTO costCenterBO) throws Exception {
        try {
            this.detailMonthPlanImpl.delete(costCenterBO);
        } catch (Exception ex) {
            logger.error(ex);
            throw ex;
        }
    }

    @Override
    public List<DetailMonthPlanDTO> searchByHql(String hql, List<ConditionBean> conditionBeans)
            throws Exception {
        try {
            return this.detailMonthPlanImpl.searchByHql(hql, conditionBeans);
        } catch (Exception ex) {
            logger.error(ex);
            throw ex;
        }
    }

    @Override
    public List<DetailMonthPlanDTO> searchByHql(String hql, List<ConditionBean> conditionBeans, int startIdx, int endIdx)
            throws Exception {
        try {
            return this.detailMonthPlanImpl.searchByHql(hql, conditionBeans);
        } catch (Exception ex) {
            logger.error(ex);
            throw ex;
        }
    }

    @Override
    public Long countByHql(String hql, List<ConditionBean> conditionBeans) throws Exception {
        try {
            return this.detailMonthPlanImpl.countByHql(hql, conditionBeans);
        } catch (Exception ex) {
            logger.error(ex);
            throw ex;
        }
    }

    @Override
    public int executeByHql(String hql, List<ConditionBean> conditionBeans) throws Exception {
        try {
            return this.detailMonthPlanImpl.executeByHql(hql, conditionBeans);
        } catch (Exception ex) {
            logger.error(ex);
            throw ex;
        }
    }

    @Override
    public Date getSysDate() throws Exception {
        try {
            return this.detailMonthPlanImpl.getSysDate();
        } catch (Exception ex) {
            logger.error(ex);
            throw ex;
        }
    }

    @Override
    public Long getNextValSequence(String sequense) throws Exception {
        try {
            return this.detailMonthPlanImpl.getNextValSequence(sequense);
        } catch (Exception ex) {
            logger.error(ex);
            throw ex;
        }
    }

}
