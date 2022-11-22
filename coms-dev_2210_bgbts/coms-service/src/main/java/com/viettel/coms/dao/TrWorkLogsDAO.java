package com.viettel.coms.dao;

import com.viettel.coms.bo.TrWorkLogsBO;
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

@Repository
@Transactional
public class TrWorkLogsDAO extends BaseFWDAOImpl<TrWorkLogsBO, Long> {
    public TrWorkLogsDAO() {
        this.model = new TrWorkLogsBO();
    }

    public TrWorkLogsDAO(Session session) {
        this.session = session;
    }

    public void insert(TrWorkLogsBO trWorkLogsBO) {
        this.session.save(trWorkLogsBO);
    }

    public List<TrWorkLogsBO> doSearch(long trId) {
        StringBuilder sql = new StringBuilder(" select "
                + " ID as id, TR_ID as trId, log_time AS logtime, TO_CHAR(log_time,'dd/MM/yyyy HH:mm') AS logTimeStr, LOG_TYPE as logType, USER_CREATED as userCreated, "
                + " CONTENT as content, STATUS as status, CONTENT_DETAIL as contentDetail "
                + " from TR_WORKLOGS WHERE STATUS > 0 and TR_ID = :trId order by LOG_TIME desc");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("trId", trId);

        query.addScalar("id", new LongType());
        query.addScalar("trId", new LongType());
        query.addScalar("logTime", new DateType());
        query.addScalar("logType", new StringType());
        query.addScalar("userCreated", new StringType());
        query.addScalar("content", new StringType());
        query.addScalar("status", new IntegerType());
        query.addScalar("logTimeStr", new StringType());
        query.addScalar("contentDetail", new StringType());

        query.setResultTransformer(Transformers.aliasToBean(TrWorkLogsBO.class));

        return query.list();
    }
}
