package com.viettel.coms.dao;

import com.viettel.coms.bo.WoMappingPlanBO;
import com.viettel.coms.dto.WoPlanDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository("woMappingPlanDAO")
public class WoMappingPlanDAO extends BaseFWDAOImpl<WoMappingPlanBO, Long> {
    public WoMappingPlanDAO() {
        this.model = new WoMappingPlanBO();
    }

    public WoMappingPlanDAO(Session session) {
        this.session = session;
    }

    public void removeWoMappingPlan(WoPlanDTO obj) {
        StringBuilder sql = new StringBuilder("DELETE WO_MAPPING_PLAN where WO_PLAN_ID =:woPlanId ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("woPlanId", obj.getId());
        query.executeUpdate();
    }

    public Long countPlanOfWo(Long woId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT count(*) from  WO_MAPPING_PLAN a where a.WO_ID = :woId and status != 0 ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("woId", woId);
        return ((BigDecimal) query.uniqueResult()).longValue();
    }
}
