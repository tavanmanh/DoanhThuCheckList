package com.viettel.coms.dao;

import com.viettel.coms.bo.WoOpinionBO;
import com.viettel.coms.dto.WoOpinionDTO;
import com.viettel.coms.dto.WoTrTypeDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class WoOpinionDAO extends BaseFWDAOImpl<WoOpinionBO, Long> {
    public WoOpinionDAO(){this.model = new WoOpinionBO();}
    public WoOpinionDAO(Session session) {
        this.session = session;
    }

    public void delete(Long opinionId) {
        StringBuilder sql = new StringBuilder("UPDATE WO_OPINION set status = 0  where ID = :opinionId");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("opinionId", opinionId);
        query.executeUpdate();
    }

    public List<WoOpinionDTO> doSearch(WoOpinionDTO woOpinionDTO) {
        StringBuilder sql = new StringBuilder(" select "
                + " op.ID as opinionId, op.WO_ID as opinionWoId, op.OPINION_TYPE_ID as opinionTypeId, op.CONTENT as opinionContent, op.STATE as opinionState, "
                + " opt.OPINION_NAME as opinionTypeName "
                + " from WO_OPINION op "
                + " join WO_OPINION_TYPE opt on op.OPINION_TYPE_ID = opt.ID " +
                " WHERE op.STATUS>0 ");
        sql.append(" AND op.WO_ID LIKE :opinionWoId ");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("opinionWoId", woOpinionDTO.getOpinionWoId());
        query.addScalar("opinionTypeName", new LongType());
        query = mapFields(query);

        return query.list();
    }

    private SQLQuery mapFields(SQLQuery query){
        query.addScalar("opinionId", new LongType());
        query.addScalar("opinionWoId", new LongType());
        query.addScalar("opinionTypeId", new LongType());
        query.addScalar("opinionContent", new StringType());
        query.addScalar("opinionState", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(WoOpinionDTO.class));
        return query;
    }
}
