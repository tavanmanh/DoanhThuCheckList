package com.viettel.coms.dao;

import com.viettel.coms.bo.WoMappingHshcTcBO;
import com.viettel.coms.dto.WoDTO;
import com.viettel.coms.dto.WoMappingHshcTcDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository("woMappingHshcTcDAO")
@Transactional
public class WoMappingHshcTcDAO extends BaseFWDAOImpl<WoMappingHshcTcBO, Long> {
    public WoMappingHshcTcDAO() {
        this.model = new WoMappingHshcTcBO();
    }

    public WoMappingHshcTcDAO(Session session) {
        this.session = session;
    }

    public List<WoDTO> doSearch(WoMappingHshcTcDTO dto) {
        StringBuilder sql = new StringBuilder("SELECT\n" +
                "    wo.id AS woId,\n" +
                "    wo.wo_name AS woName,\n" +
                "    wo.wo_code AS woCode,\n" +
                "    wo.state,\n" +
                "    wo.end_time AS endTime,\n" +
                "    wo.money_value AS moneyValue,\n" +
                "    wo.construction_code constructionCode\n" +
                "FROM\n" +
                "    wo_mapping_hshc_tc wm\n" +
                "    JOIN wo ON wm.wo_tc_id = wo.id\n" +
                "WHERE\n" +
                "    wm.status > 0");

        if (dto.getWoHshcId() != null) {
            sql.append(" AND wm.WO_HSHC_ID = :woHshcId ");
            sql.append(" AND wm.CONTRACT_CODE = wo.CONTRACT_CODE ");
        }

        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        if (dto.getWoHshcId() != null) {
            query.setParameter("woHshcId", dto.getWoHshcId());
            queryCount.setParameter("woHshcId", dto.getWoHshcId());
        }

        query.addScalar("woId", new LongType());
        query.addScalar("woName", new StringType());
        query.addScalar("woCode", new StringType());
        query.addScalar("state", new StringType());
        query.addScalar("constructionCode", new StringType());
        query.addScalar("endTime", new DateType());
        query.addScalar("moneyValue", new DoubleType());

        query.setResultTransformer(Transformers.aliasToBean(WoDTO.class));

        if (dto.getPage() != null && dto.getPageSize() != null) {
            query.setFirstResult((dto.getPage().intValue() - 1) * dto.getPageSize().intValue());
            query.setMaxResults(dto.getPageSize().intValue());
        }

        dto.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

        return query.list();
    }
}
