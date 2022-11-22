package com.viettel.coms.dao;

import com.viettel.coms.bo.WoPlanBO;
import com.viettel.coms.dto.*;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("woPlanDAO")
public class WoPlanDAO extends BaseFWDAOImpl<WoPlanBO, Long> {
    public WoPlanDAO() {
        this.model = new WoPlanBO();
    }

    public WoPlanDAO(Session session) {
        this.session = session;
    }

    public WoPlanDTO findByName(String name, Long ftId) {
        StringBuilder sql = new StringBuilder("SELECT wp.NAME name FROM wo_plan wp WHERE wp.NAME = :name AND ft_id = :ftId AND ROWNUM < 2 ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.addScalar("name", new StringType());

        query.setResultTransformer(Transformers.aliasToBean(WoPlanDTO.class));
        query.setParameter("name", name);
        query.setParameter("ftId", ftId);
        return (WoPlanDTO) query.uniqueResult();
    }

    public List<WoPlanDTO> getListWOPlan(Long ftId) {
        StringBuilder sql = new StringBuilder("SELECT\n" +
                "    id,\n" +
                "    code,\n" +
                "    name,\n" +
                "    ft_id ftId,\n" +
                "    plan_type planType,\n" +
                "    created_date createdDate,\n" +
                "    from_date fromDate,\n" +
                "    TO_DATE toDate,\n" +
                "    status\n" +
                "FROM\n" +
                "    wo_plan\n" +
                "WHERE\n" +
                "    ft_id = :ftId\n" +
                "    and status = 1\n" +
                "    order by created_date desc \n");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("ftId", ftId);
        query.addScalar("id", new LongType());
        query.addScalar("code", new StringType());
        query.addScalar("name", new StringType());
        query.addScalar("ftId", new LongType());
        query.addScalar("planType", new StringType());
        query.addScalar("createdDate", new DateType());
        query.addScalar("fromDate", new DateType());
        query.addScalar("toDate", new DateType());
        query.addScalar("status", new LongType());
        query.setResultTransformer(Transformers.aliasToBean(WoPlanDTO.class));
        return query.list();
    }

    @Transactional
    public void delete(long woPlanId) {
        StringBuilder sql = new StringBuilder("UPDATE wo_plan set status = 0  where id = :id");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("id", woPlanId);
        query.executeUpdate();

        StringBuilder sqlDeleteMapping = new StringBuilder("UPDATE wo_mapping_plan set status = 0  where wo_plan_id = :woPlanId");
        SQLQuery queryDeleteMapping = getSession().createSQLQuery(sqlDeleteMapping.toString());
        queryDeleteMapping.setParameter("woPlanId", woPlanId);
        queryDeleteMapping.executeUpdate();
    }

    public List<WoPlanDTO> getDataForPlanChart(Long loginUserId) {
        String sql = "select\n" +
                "    wp.ID\n" +
                "    , wp.CODE\n" +
                "    , MAX((select count(*) from WO_MAPPING_PLAN where WO_PLAN_ID = wp.ID)) numWoOfPlan\n" +
                "    , SUM((select count(*) from WO where ID = w.ID AND STATE = 'OK')) woOk\n" +
                "from wo_plan wp\n" +
                "left join WO_MAPPING_PLAN wmp on wp.ID = wmp.WO_PLAN_ID\n" +
                "left join wo w on wmp.WO_ID = w.ID\n" +
                "where wp.ft_id = :ftId and wp.status = 1\n" +
                "group by wp.ID, wp.CODE";

        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("ftId", loginUserId);

        query.addScalar("id", new LongType());
        query.addScalar("code", new StringType());
        query.addScalar("numWoOfPlan", new LongType());
        query.addScalar("woOk", new LongType());
        query.setResultTransformer(Transformers.aliasToBean(WoPlanDTO.class));
        return query.list();
    }
}
