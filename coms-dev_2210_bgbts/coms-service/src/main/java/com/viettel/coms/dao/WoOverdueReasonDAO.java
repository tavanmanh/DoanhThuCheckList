package com.viettel.coms.dao;

import com.viettel.coms.bo.WoOverdueReasonBO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.type.LongType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class WoOverdueReasonDAO extends BaseFWDAOImpl<WoOverdueReasonBO, Long> {
    public WoOverdueReasonDAO(){this.model = new WoOverdueReasonBO();}
    public WoOverdueReasonDAO(Session session) {
        this.session = session;
    }

    public WoOverdueReasonBO getOneRaw(long id){
        return this.get(WoOverdueReasonBO.class, id);
    }

    public Long getIdByWoId(Long woId){
        String sql = "select id from wo_overdue_reason where wo_id = :woId";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("woId", woId);
        query.addScalar("id", new LongType());

        List<Long> result = query.list();
        if(result.size()==0) return null;
        return result.get(0);
    }
}
