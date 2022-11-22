package com.viettel.coms.dao;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.viettel.coms.bo.WoWorkLogsBO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@Repository
@Transactional
public class WoWorkLogsDAO extends BaseFWDAOImpl<WoWorkLogsBO, Long> {
    public WoWorkLogsDAO() {
        this.model = new WoWorkLogsBO();
    }

    public WoWorkLogsDAO(Session session) {
        this.session = session;
    }

    public void insert(WoWorkLogsBO woWorkLogsBO) {
        this.session.save(woWorkLogsBO);
    }

    public List<WoWorkLogsBO> doSearch(long woId) {
        StringBuilder sql = new StringBuilder(" select "
                + " ID as id, WO_ID as woId, log_time AS logtime, TO_CHAR(log_time,'dd/MM/yyyy hh24:mi:ss') AS logTimeStr, LOG_TYPE as logType, USER_CREATED as userCreated, "
                + " CONTENT as content, STATUS as status, CONTENT_DETAIL as contentDetail "
                + " from WO_WORKLOGS WHERE STATUS>0 and WO_ID = :woId order by LOG_TIME desc");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("woId", woId);

        query.addScalar("id", new LongType());
        query.addScalar("woId", new LongType());
        query.addScalar("logTime", new TimestampType());
        query.addScalar("logType", new StringType());
        query.addScalar("userCreated", new StringType());
        query.addScalar("content", new StringType());
        query.addScalar("status", new IntegerType());
        query.addScalar("logTimeStr", new StringType());
        query.addScalar("contentDetail", new StringType());

        query.setResultTransformer(Transformers.aliasToBean(WoWorkLogsBO.class));

        return query.list();
    }
}
