package com.viettel.coms.dao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.viettel.coms.dto.WoTaskDailyDTO;
import org.apache.commons.lang3.StringUtils;
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

import com.viettel.coms.bo.WoTaskDailyBO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@Repository
@Transactional
public class WoTaskDailyDAO extends BaseFWDAOImpl<WoTaskDailyBO, Long> {

    private String baseSelectStr = "select WO_TASK_DAILY_ID as id, " +
            " SYS_GROUP_ID as sysGroupId, AMOUNT as amount, TYPE as type, " +
            " CONFIRM as confirm, CREATED_DATE as createdDate, CREATED_USER_ID as createdUserId, " +
            " QUANTITY as quantity, WO_MAPPING_CHECKLIST_ID as woMappingChecklistId, " +
            " CONFIRM_USER_ID as confirmUserId, WO_ID as woId, STATUS as status, " +
            " APPROVE_DATE as approveDate, APPROVE_BY as approveBy ";

    private SQLQuery mapFields(SQLQuery query) {
        query.addScalar("id", new LongType());
        query.addScalar("sysGroupId", new LongType());
        query.addScalar("amount", new DoubleType());
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

    public WoTaskDailyDAO() {
        this.model = new WoTaskDailyBO();
    }

    public WoTaskDailyDAO(Session session) {
        this.session = session;
    }

    public WoTaskDailyBO getOneRaw(Long id){
        return this.get(WoTaskDailyBO.class,id);
    }

    public WoTaskDailyBO getOneRawByDate(Date date, Long checklistId){
        String sql = baseSelectStr + " from wo_task_daily where to_char(created_date,'mm-dd-yyyy') = :dateStr " +
                " and wo_mapping_checklist_id = :checklistId and status = 1 fetch next 1 row only";
        SQLQuery query = getSession().createSQLQuery(sql);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        String dateStr = dateFormat.format(date);
        query.setParameter("dateStr", dateStr);
        query.setParameter("checklistId", checklistId);
        query = mapFields(query);
        query.setResultTransformer(Transformers.aliasToBean(WoTaskDailyBO.class));
        return (WoTaskDailyBO) query.uniqueResult();
    }

    public List<WoTaskDailyBO> getUnapproved(Long checklistId){
        String sql = baseSelectStr + " from wo_task_daily where wo_mapping_checklist_id = :checklistId and confirm != '1' ";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("checklistId", checklistId);
        query = mapFields(query);
        query.setResultTransformer(Transformers.aliasToBean(WoTaskDailyBO.class));
        return query.list();
    }

    public Double getPriceOfChecklist(Long checkListId, Long constructionId, Long catConstructionId){
        String sql = "";

        if(catConstructionId == null) return 0d;
        if(catConstructionId != 2 && catConstructionId != 3) return 0d;

//        //công trình gpon
//        if(catConstructionId == 3){
//            sql = "select price from work_item_gpon where work_item_gpon_id = :checkListId fetch next 1 row only ";
//        }
//
//        //công trình tuyến
//        if(catConstructionId == 2){
//            sql = "select price from construction where construction_id = :constructionId fetch next 1 row only ";
//        }

        sql = "select nvl(price,0) as price from construction where construction_id = :constructionId fetch next 1 row only ";

        SQLQuery query = getSession().createSQLQuery(sql);

        //if(catConstructionId == 3)  query.setParameter("checkListId", checkListId);
        if(catConstructionId == 2 || catConstructionId == 3)  query.setParameter("constructionId", constructionId);
        query.addScalar("price", new DoubleType());

        List<Double> prices = query.list();
        if(prices.size()>0) return prices.get(0);
        else return 0d;
    }

    public Double sumUnapprovedQuantity(Long checklistId){
        String sql = "select nvl(sum(quantity),0) as total from wo_task_daily where status>0 and WO_MAPPING_CHECKLIST_ID = :checklistId and confirm != '1' ";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("checklistId", checklistId);
        query.addScalar("total", new DoubleType());
        return (Double) query.uniqueResult();
    }

    public Double sumWoUnapprovedQuantity(Long woId){
        String sql = "select nvl(sum(quantity),0) as total from wo_task_daily where status>0 and WO_ID = :woId and confirm != '1' ";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("woId", woId);
        query.addScalar("total", new DoubleType());
        return (Double) query.uniqueResult();
    }

    public Double sumAmountOfWo(Long woId){
        String sql = "select nvl(sum(amount),0) as total from wo_task_daily where status>0 and wo_id = :woId ";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("woId", woId);
        query.addScalar("total", new DoubleType());
        return (Double) query.uniqueResult();
    }

    public int updateQtyAndAmount(WoTaskDailyBO bo){
        StringBuilder sql = new StringBuilder("UPDATE WO_TASK_DAILY set amount = :amount, quantity = :quantity where WO_TASK_DAILY_ID = :id");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("amount", bo.getAmount());
        query.setParameter("quantity", bo.getQuantity());
        query.setParameter("id", bo.getId());

        return query.executeUpdate();
    }

    public List<WoTaskDailyBO> doSearch(WoTaskDailyDTO dto){
        String sql = baseSelectStr + "from wo_task_daily where status>0 ";

        if(dto.getWoMappingChecklistId()!=null) sql += " and WO_MAPPING_CHECKLIST_ID = :woMappingChecklist ";
        if(dto.getWoId()!=null) sql += " and wo_id = :woId";
        if(StringUtils.isNotEmpty(dto.getConfirm())) sql += " and confirm = :confirm ";

        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql);
        sqlCount.append(")");
        SQLQuery query = getSession().createSQLQuery(sql);
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        if(dto.getWoMappingChecklistId()!=null){
            query.setParameter("woMappingChecklist", dto.getWoMappingChecklistId());
            queryCount.setParameter("woMappingChecklist", dto.getWoMappingChecklistId());
        }

        if(dto.getWoId()!=null){
            query.setParameter("woId", dto.getWoId());
            queryCount.setParameter("woId", dto.getWoId());
        }

        if(StringUtils.isNotEmpty(dto.getConfirm())){
            query.setParameter("confirm", dto.getConfirm());
            queryCount.setParameter("confirm", dto.getConfirm());
        }

        if (dto.getPage() != null && dto.getPageSize() != null) {
            query.setFirstResult((dto.getPage().intValue() - 1) * dto.getPageSize().intValue());
            query.setMaxResults(dto.getPageSize().intValue());
        }

        dto.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

        query = mapFields(query);
        query.setResultTransformer(Transformers.aliasToBean(WoTaskDailyBO.class));

        return query.list();
    }

    public void tryUnconfirmAllQuantity(Long woId){
        String sql = " update wo_task_daily set confirm = '0', AMOUNT=0, QUANTITY=0, status=0 where wo_id =:woId";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("woId", woId);
        query.executeUpdate();
    }
    
    //Huypq-04082021-start
    public void updateWoMappingCheckList(Long woMappingId, String state){
        String sql = " update WO_MAPPING_CHECKLIST set STATE = :state where ID =:woMappingId";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("state", state);
        query.setParameter("woMappingId", woMappingId);
        query.executeUpdate();
    }
    //Huy-end
    

}
