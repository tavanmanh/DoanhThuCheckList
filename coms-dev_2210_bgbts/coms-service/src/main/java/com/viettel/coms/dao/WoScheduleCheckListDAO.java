package com.viettel.coms.dao;

import com.viettel.coms.bo.WoScheduleCheckListBO;
import com.viettel.coms.bo.WoScheduleWorkItemBO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public class WoScheduleCheckListDAO extends BaseFWDAOImpl<WoScheduleCheckListBO, Long> {
    public WoScheduleCheckListDAO(){this.model = new WoScheduleCheckListBO();}
    public WoScheduleCheckListDAO(Session session) {
        this.session = session;
    }

    public WoScheduleCheckListBO getOneRaw(Long id){
        return this.get(WoScheduleCheckListBO.class, id);
    }

    public int deleteWorkItemCheckList(Long scheduleCheckListId) {
        StringBuilder sql = new StringBuilder("UPDATE WO_SCHEDULE_CHECKLIST set status = 0  where ID = :scheduleCheckListId");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("scheduleCheckListId", scheduleCheckListId);
        return query.executeUpdate();
    }
}
