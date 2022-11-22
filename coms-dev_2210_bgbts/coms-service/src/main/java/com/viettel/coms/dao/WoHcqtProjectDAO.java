package com.viettel.coms.dao;

import com.viettel.coms.bo.WoHcqtProjectBO;
import com.viettel.coms.dto.WoHcqtProjectDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
@Transactional
public class WoHcqtProjectDAO  extends BaseFWDAOImpl<WoHcqtProjectBO, Long> {

    public WoHcqtProjectDAO() {
        this.model = new WoHcqtProjectBO();
    }

    public WoHcqtProjectDAO(Session session) {
        this.session = session;
    }

    public WoHcqtProjectBO getOneRaw(long trId) {
        return this.get(WoHcqtProjectBO.class, trId);
    }

    public int delete(Long id) {
        StringBuilder sql = new StringBuilder("UPDATE WO_HCQT_PROJECT set status = 0  where HCQT_PROJECT_ID = :id");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    public List<WoHcqtProjectDTO> doSearch(WoHcqtProjectDTO dto){
        StringBuilder sql = new StringBuilder("select HCQT_PROJECT_ID as hcqtProjectId, HCQT_PROJECT_CODE as code, HCQT_PROJECT_NAME as name, STATUS as status " +
                " from WO_HCQT_PROJECT where status>0 ");

        if(StringUtils.isNotBlank(dto.getKeySearch())) {
            sql.append(" AND (upper(HCQT_PROJECT_CODE) like upper(:keySearch) "
                    + " OR upper(HCQT_PROJECT_NAME) like upper(:keySearch) escape '&') ");
        }

        if(dto.getIdRange()!=null && dto.getIdRange().size()>0){
            sql.append(" AND HCQT_PROJECT_ID in (:idRange) ");
        }

        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        if(StringUtils.isNotBlank(dto.getKeySearch())) {
            query.setParameter("keySearch", "%" + dto.getKeySearch() + "%");
            queryCount.setParameter("keySearch", "%" + dto.getKeySearch()  + "%");
        }

        if(dto.getIdRange()!=null && dto.getIdRange().size()>0){
            query.setParameterList("idRange", dto.getIdRange());
            queryCount.setParameterList("idRange", dto.getIdRange());
        }

        query.addScalar("hcqtProjectId", new LongType());
        query.addScalar("code", new StringType());
        query.addScalar("name", new StringType());
        query.addScalar("status", new LongType());
        query.setResultTransformer(Transformers.aliasToBean(WoHcqtProjectDTO.class));

        if (dto.getPage() != null && dto.getPageSize() != null) {
            query.setFirstResult((dto.getPage().intValue() - 1) * dto.getPageSize().intValue());
            query.setMaxResults(dto.getPageSize().intValue());
        }

        dto.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

        return query.list();
    }

}
