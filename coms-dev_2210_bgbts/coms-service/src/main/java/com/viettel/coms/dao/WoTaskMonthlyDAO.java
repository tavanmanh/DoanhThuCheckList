package com.viettel.coms.dao;

import java.util.Date;
import java.util.List;

import com.viettel.coms.bo.WorkItemBO;
import com.viettel.coms.dto.WoTaskMonthlyDTO;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.viettel.coms.bo.WoTaskMonthlyBO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@Repository
@Transactional
public class WoTaskMonthlyDAO extends BaseFWDAOImpl<WoTaskMonthlyBO, Long> {

    private String baseSelectStr = "select WO_TASK_MONTHLY_ID as woTaskMonthlyId, " +
            " SYS_GROUP_ID as sysGroupId, AMOUNT as amount, TYPE as type, " +
            " CONFIRM as confirm, CREATED_DATE as createdDate, CREATED_USER_ID as createdUserId, " +
            " QUANTITY as quantity, WO_MAPPING_CHECKLIST_ID as woMappingChecklistId, " +
            " CONFIRM_USER_ID as confirmUserId, WO_ID as woId, STATUS as status, " +
            " APPROVE_DATE as approveDate, APPROVE_BY as approveBy ";

    private SQLQuery mapFields(SQLQuery query) {
        query.addScalar("woTaskMonthlyId", new LongType());
        query.addScalar("sysGroupId", new LongType());
        query.addScalar("amount", new IntegerType());
        query.addScalar("type", new StringType());
        query.addScalar("confirm", new StringType());
        query.addScalar("createdDate", new DateType());
        query.addScalar("createdUserId", new LongType());
        query.addScalar("quantity", new DoubleType());
        query.addScalar("woMappingChecklistId", new LongType());
        query.addScalar("confirmUserId", new LongType());
        query.addScalar("woId", new LongType());
        query.addScalar("status", new LongType());
        query.addScalar("approveDate", new DateType());
        query.addScalar("approveBy", new StringType());

        return query;
    }

    public WoTaskMonthlyDAO() {
        this.model = new WoTaskMonthlyBO();
    }

    public WoTaskMonthlyDAO(Session session) {
        this.session = session;
    }

    public List<WoTaskMonthlyBO> getUnapproved(Long checklistId){
        String sql = baseSelectStr + " from wo_task_monthly where wo_mapping_checklist_id = :checklistId and confirm != '1' ";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("checklistId", checklistId);
        query = mapFields(query);
        query.setResultTransformer(Transformers.aliasToBean(WoTaskMonthlyBO.class));
        return query.list();
    }

    public Double getApprovedCollected(Long woId){
        String sql = "select nvl(sum(QUANTITY),0) as total from WO_TASK_MONTHLY where wo_id =:woId and confirm='1' and status>0 ";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("woId", woId);
        query.addScalar("total", new DoubleType());
        return (Double)  query.uniqueResult();
    }

    public Double getUnapprovedCollected(Long woId){
        String sql = "select nvl(sum(QUANTITY),0) as total from WO_TASK_MONTHLY where wo_id =:woId and confirm!='1' and status>0 ";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("woId", woId);
        query.addScalar("total", new DoubleType());
        return (Double)  query.uniqueResult();
    }

    public WoTaskMonthlyBO searchThisMonth(Long woId){
        Date now = new Date();
        int currentMonth = now.getMonth()+1;

        String sql = baseSelectStr + " from WO_TASK_MONTHLY where wo_id =:woId and EXTRACT(MONTH FROM CREATED_DATE) = :currentMonth and status>0 ";
        SQLQuery query = getSession().createSQLQuery(sql);

        query.setParameter("woId", woId);
        query.setParameter("currentMonth", currentMonth);
        query = mapFields(query);
        query.setResultTransformer(Transformers.aliasToBean(WoTaskMonthlyBO.class));

        if(query.list().size()>0) return (WoTaskMonthlyBO) query.uniqueResult();
        else return null;
    }

    public List<WoTaskMonthlyDTO> doSearch(WoTaskMonthlyDTO dto){
        String sql = baseSelectStr + " from WO_TASK_MONTHLY where status>0 ";

        if(dto.getWoId()!=null) sql+= " and wo_id = :woId ";

        SQLQuery query = getSession().createSQLQuery(sql);

        if(dto.getWoId()!=null) query.setParameter("woId", dto.getWoId());
        query.setResultTransformer(Transformers.aliasToBean(WoTaskMonthlyDTO.class));
        query = mapFields(query);

        return query.list();
    }

    public void tryUnconfirmAllQuantity(Long woId){
        String sql = " update wo_task_monthly set confirm = '0' where wo_id =:woId";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("woId", woId);
        query.executeUpdate();
    }

}
