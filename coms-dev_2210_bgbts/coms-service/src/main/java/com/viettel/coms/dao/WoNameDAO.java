package com.viettel.coms.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.viettel.coms.bo.WoNameBO;
import com.viettel.coms.dto.WoNameDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@Repository
@Transactional
public class WoNameDAO extends BaseFWDAOImpl<WoNameBO, Long> {

    public WoNameDAO(){this.model = new WoNameBO();}
    public WoNameDAO(Session session) {
        this.session = session;
    }

    public int delete(Long id) {
        StringBuilder sql = new StringBuilder("UPDATE WO_NAME set status = 0  where ID = :id");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    public List<WoNameDTO> doSearch(WoNameDTO dto) {
        StringBuilder sql = new StringBuilder(" select "
                + " wn.ID as id, wn.NAME as name, wn.WO_TYPE_ID as woTypeId, wn.STATUS as status, wt.WO_TYPE_NAME as woTypeName, wt.WO_TYPE_CODE as woTypeCode "
                + " from WO_NAME wn "
                + " join WO_TYPE wt on wn.WO_TYPE_ID = wt.ID and wt.STATUS>0" +
                " WHERE wn.STATUS>0 ");

        if(dto.getWoTypeId() != null ){
            sql.append(" AND wn.WO_TYPE_ID = :woTypeId ");
        }

        if(dto.getIdRange() != null && dto.getIdRange().size()>0){
            sql.append(" AND wn.ID in (:idRange) ");
        }

        //Huypq-02082020-start
        if(StringUtils.isNotBlank(dto.getKeySearch())) {
        	sql.append(" AND (upper(wn.NAME) like upper(:keySearch) "
        			+ "OR upper(wt.WO_TYPE_NAME) like upper(:keySearch) "
        			+ "OR upper(wt.WO_TYPE_CODE) like upper(:keySearch) escape '&') ");
        }
        //Huy-end
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        if(dto.getWoTypeId() != null ){
            query.setParameter("woTypeId", dto.getWoTypeId());
            queryCount.setParameter("woTypeId", dto.getWoTypeId());
        }

        if(dto.getIdRange() != null && dto.getIdRange().size()>0){
            query.setParameterList("idRange", dto.getIdRange());
            queryCount.setParameterList("idRange", dto.getIdRange());
        }

        //Huypq-02082020-start
        if(StringUtils.isNotBlank(dto.getKeySearch())) {
        	query.setParameter("keySearch", "%" + dto.getKeySearch() + "%");
            queryCount.setParameter("keySearch", "%" + dto.getKeySearch()  + "%");
        }
        //Huy-end
        
        query.addScalar("woTypeName", new StringType());
        query.addScalar("woTypeCode", new StringType());
        query = mapFields(query);

        if (dto.getPage() != null && dto.getPageSize() != null) {
            query.setFirstResult((dto.getPage().intValue() - 1) * dto.getPageSize().intValue());
            query.setMaxResults(dto.getPageSize().intValue());
        }

        dto.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

        return query.list();
    }

    public WoNameBO getOneRaw(long id){
        return this.get(WoNameBO.class, id);
    }

    private SQLQuery mapFields(SQLQuery query){
        query.addScalar("id", new LongType());
        query.addScalar("name", new StringType());
        query.addScalar("woTypeId", new LongType());
        query.addScalar("status", new LongType());

        query.setResultTransformer(Transformers.aliasToBean(WoNameDTO.class));
        return query;
    }

}
