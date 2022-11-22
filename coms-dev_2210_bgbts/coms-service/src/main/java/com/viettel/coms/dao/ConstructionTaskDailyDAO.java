/*
 * Copyright (C) 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.coms.dao;

import com.viettel.coms.bo.ConstructionTaskDailyBO;
import com.viettel.coms.dto.AppParamDTO;
import com.viettel.coms.dto.ConstructionTaskDTO;
import com.viettel.coms.dto.ConstructionTaskDTOUpdateRequest;
import com.viettel.coms.dto.ConstructionTaskDailyDTO;
import com.viettel.coms.dto.DetailMonthPlanDTO;
import com.viettel.coms.dto.RpQuantityDTO;
import com.viettel.coms.dto.SysUserCOMSDTO;
import com.viettel.coms.dto.WoDTO;
import com.viettel.coms.dto.WorkItemDTO;
import com.viettel.coms.dto.WorkItemDetailDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

/**
 * @author TruongBX3
 * @version 1.0
 * @since 08-May-15 4:07 PM
 */
@Repository("constructionTaskDailyDAO")
public class ConstructionTaskDailyDAO extends BaseFWDAOImpl<ConstructionTaskDailyBO, Long> {

    public ConstructionTaskDailyDAO() {
        this.model = new ConstructionTaskDailyBO();
    }

    public ConstructionTaskDailyDAO(Session session) {
        this.session = session;
    }
    private double completePercent;
    
    public double getCompletePercent() {
		return completePercent;
	}

	public void setCompletePercent(double completePercent) {
		this.completePercent = completePercent;
	}

	public void deleteTaskDaily(Long id) {
        String sql = new String(
                " delete from CONSTRUCTION_TASK_DAILY a where a.CONSTRUCTION_TASK_ID=:id and confirm =0 AND TO_CHAR(a.CREATED_DATE,'dd/mm/YYYY') = TO_CHAR(sysdate,'dd/mm/YYYY')");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    public Double getTotal(ConstructionTaskDTOUpdateRequest request) {
        /*
         * String sql = new String(
         * " SELECT nvl(SUM(a.AMOUNT),0) amount FROM CONSTRUCTION_TASK_DAILY a where a.confirm in (0,1) and a.CONSTRUCTION_TASK_ID ='"
         * +id+"' ");
         */

        // CuongNV2 mod start
        Long worItemId = request.getConstructionTaskDTO().getWorkItemId();
        Long catTaskId = request.getConstructionTaskDTO().getCatTaskId();
        String sql = new String(
                " SELECT nvl(SUM(a.AMOUNT),0) amount FROM CONSTRUCTION_TASK_DAILY a where a.confirm in (0,1) and a.WORK_ITEM_ID ='"
                        + worItemId + "' and a.cat_task_id ='" + catTaskId + "' ");
        // CuongNV2 mod end

        SQLQuery query = getSession().createSQLQuery(sql);
        BigDecimal result = (BigDecimal) query.uniqueResult();
        return result.doubleValue();
    }

    public Double getAmountPreview(Long worItemId, Long catTaskId) {
        // String sql = new
        // String(" SELECT nvl(SUM(a.AMOUNT),0) amount FROM CONSTRUCTION_TASK_DAILY a
        // where a.confirm in (0,1) and a.CONSTRUCTION_TASK_ID ='"+id+"' and
        // trunc(CREATED_DATE)< trunc(sysdate)");
        String sql = new String(
                " SELECT nvl(SUM(a.AMOUNT),0) amount FROM CONSTRUCTION_TASK_DAILY a where a.confirm in (0,1) and a.WORK_ITEM_ID ='"
                        + worItemId + "' and a.cat_task_id ='" + catTaskId
                        + "' and trunc(CREATED_DATE)< trunc(sysdate)");
        SQLQuery query = getSession().createSQLQuery(sql);
        BigDecimal result = (BigDecimal) query.uniqueResult();
        return result.doubleValue();
    }

    public int getListConfirmDaily(Long id) {
        String sql = new String(
                " SELECT CONSTRUCTION_TASK_DAILY_id FROM CONSTRUCTION_TASK_DAILY a where a.confirm in (1) and a.CONSTRUCTION_TASK_ID ='"
                        + id + "' and trunc(CREATED_DATE)= trunc(sysdate)");
        SQLQuery query = getSession().createSQLQuery(sql);
        List<Long> lt = query.list();
        if (lt.size() > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    public void updateQuantityWorkItem(Long id, Double quantity) {
        StringBuilder sql = new StringBuilder(" ");
        sql.append(" UPDATE WORK_ITEM a ");
        sql.append(" SET a.QUANTITY        =:quantity ");
        sql.append(" WHERE a.WORK_ITEM_ID IN ");
        sql.append(" (SELECT work_item_id FROM construction_task WHERE construction_task_id= :id) ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("id", id);
        query.setParameter("quantity", quantity);
        query.executeUpdate();
    }

    // hoanm1_20180703_start
    public void updateCompletePercentTask(Long id, Double completePercent) {
        // StringBuilder sql = new StringBuilder(" ");
        // sql.append(" UPDATE CONSTRUCTION_TASK a ");
        // sql.append(" SET a.COMPLETE_PERCENT =:completePercent ");
        // sql.append(" WHERE a.CONSTRUCTION_TASK_ID =:id");
        // SQLQuery query = getSession().createSQLQuery(sql.toString());
        // query.setParameter("id", id);
        // query.setParameter("completePercent", completePercent);
        // query.executeUpdate();
        String sql = new String("UPDATE CONSTRUCTION_TASK a " + " SET a.COMPLETE_PERCENT =:completePercent "
                + " WHERE a.CONSTRUCTION_TASK_ID =:id");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("id", id);
        query.setParameter("completePercent", Math.round(completePercent));
        query.executeUpdate();
    }

    // hoanm1_20180703_end

    public boolean checkInsert(String code, Long id) {
        StringBuilder sql = new StringBuilder(" ");
        sql.append(" SELECT COUNT(a.CONSTRUCTION_TASK_DAILY_ID ) ");
        sql.append(" FROM CONSTRUCTION_TASK_DAILY a ");
        sql.append(" WHERE a.CONSTRUCTION_TASK_ID       =:id ");
        sql.append(" AND a.TYPE    =:code ");
        sql.append(" AND TO_CHAR(a.CREATED_DATE,'dd/mm/YYYY') = TO_CHAR(sysdate,'dd/mm/YYYY') ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("id", id);
        query.setParameter("code", code);
        BigDecimal result = (BigDecimal) query.uniqueResult();
        if (result.intValue() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void updateNewConstructionTaskDaily(AppParamDTO dto, ConstructionTaskDTOUpdateRequest request) {
        Double quantity = request.getConstructionTaskDTO().getPrice() * dto.getAmount();
        StringBuilder sql = new StringBuilder(" ");
        sql.append(" UPDATE CONSTRUCTION_TASK_DAILY a ");
        sql.append(" SET a.AMOUNT                   = '" + dto.getAmount() + "', ");
        sql.append(" a.QUANTITY                     = '" + quantity + "', ");
        sql.append(" a.UPDATED_USER_ID              = '" + request.getSysUserRequest().getSysUserId() + "', ");
        sql.append(" a.UPDATED_DATE                 = sysdate, ");
        sql.append(" a.UPDATED_GROUP_ID             = '" + request.getSysUserRequest().getSysGroupId() + "' ");
        // sql.append(" WHERE a.CONSTRUCTION_TASK_ID = '" +
        // request.getConstructionTaskDTO().getConstructionTaskId() + "' ");
        // sql.append(" AND a.TYPE = '" + dto.getCode() +
        // "' ");
        // hoanm1_20180703_start
        sql.append(" where CONSTRUCTION_TASK_DAILY_ID = '" + dto.getConstructionTaskDailyId() + "' ");
        // hoanm1_20180703_end
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.executeUpdate();
    }

    // hungnx 20180710 start
    public List<ConstructionTaskDailyDTO> doSearchInPast(ConstructionTaskDailyDTO criteria) {
        StringBuilder stringBuilder = new StringBuilder(
                "SELECT CTD.CONSTRUCTION_TASK_DAILY_ID constructionTaskDailyId, CTD.AMOUNT amount, CTD.CAT_TASK_ID catTaskId"
                        + ", CTD.CONFIRM confirm, CTD.CONSTRUCTION_TASK_ID constructionTaskId, CTD.WORK_ITEM_ID workItemId "
                        + ", CT.STATUS statusConstructionTask, C.AMOUNT amountConstruction"
                        + " FROM CONSTRUCTION_TASK_DAILY ctd ,CONSTRUCTION_TASK ct, CONSTRUCTION c  "
                        + " where TO_CHAR(ctd.created_date, 'MM/yyyy') < to_char(sysdate, 'MM/yyyy')"
                        + " and CTD.CONSTRUCTION_TASK_ID = CT.CONSTRUCTION_TASK_ID and C.CONSTRUCTION_ID = CT.CONSTRUCTION_ID");
        if (StringUtils.isNotEmpty(criteria.getConfirm())) {
            stringBuilder.append(" and CTD.CONFIRM = :confirm");
        }
        SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
        if (StringUtils.isNotEmpty(criteria.getConfirm())) {
            query.setParameter("confirm", criteria.getConfirm());
        }
        query.addScalar("workItemId", new LongType());
        query.addScalar("constructionTaskDailyId", new LongType());
        query.addScalar("catTaskId", new LongType());
        query.addScalar("confirm", new StringType());
        query.addScalar("amount", new DoubleType());
        query.addScalar("constructionTaskId", new LongType());
        query.addScalar("amountConstruction", new DoubleType());
        query.addScalar("statusConstructionTask", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(ConstructionTaskDailyDTO.class));
        return query.list();
    }

    public int updateConfirm(ConstructionTaskDailyDTO criteria) {
        StringBuilder stringBuilder = new StringBuilder(
                "UPDATE CONSTRUCTION_TASK_DAILY ctd set CTD.CONFIRM = :confirm");
        stringBuilder.append(" where ctd.CONSTRUCTION_TASK_DAILY_ID = :dailyId");
        SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
        query.setParameter("confirm", criteria.getConfirm());
        query.setParameter("dailyId", criteria.getConstructionTaskDailyId());
        return query.executeUpdate();
    }
    // hungnx 20180710 end
    
    /**Hoangnh start 18022019**/
    @SuppressWarnings("unchecked")
	public List<ConstructionTaskDailyDTO> doSearch(ConstructionTaskDailyDTO obj,List<String> groupIdList){
    	StringBuilder sql = new StringBuilder("SELECT DISTINCT CD.CONSTRUCTION_TASK_DAILY_ID constructionTaskDailyId,"
    			+ "CD.SYS_GROUP_ID sysGroupId,"
    			+ "CD.AMOUNT amount,"
    			+ "CD.TYPE type,"
    			+ "CD.CONFIRM confirm,"
    			+ "CD.CREATED_DATE createdDate,"
    			+ "CD.CREATED_USER_ID createdUserId,"
    			+ "CD.CREATED_GROUP_ID createdGroupId,"
    			+ "CD.CONSTRUCTION_TASK_ID constructionTaskId,"
    			+ "CD.APPROVE_DATE approveDate,"
    			+ "CD.APPROVE_USER_ID approveUserId,"
    			+ "round(CD.QUANTITY/1000000,2) quantity,"
    			+ "CD.WORK_ITEM_ID workItemId,"
    			+ "WI.NAME workItemName,"
    			+ "WI.STATUS status,"
    			+ "CO.CODE constructionCode,"
    			+ "CO.CAT_STATION_ID catStationId,"
    			+ "CO.STATUS statusConstruction,"
    			+ "CS.CODE catStationCode,"
    			+ "CD.CAT_TASK_ID catTaskId,"
    			+ "CT.START_DATE startDate,"
    			+ "CT.END_DATE endDate,"
    			+ "SY.NAME sysGroupName,"
    			+ "SU.FULL_NAME fullName,"
    			+ "CNT.CODE cntContractCode,"
    			+ "CP.CODE catProvinceCode,"
    			+ "CO.CONSTRUCTION_ID constructionId,"
//    			hoanm1_20190527_start
    			+ "CD.AMOUNT amountConstruction,"
//    			+ "(case when CD.TYPE=10 then WI.AMOUNT when CD.TYPE=11 then WI.TOTAL_AMOUNT_GATE when CD.TYPE=12 then WI.TOTAL_AMOUNT_CHEST END) amountConstruction,"
    			+ "CT.STATUS statusConstructionTask,"
    			+ "CT.COMPLETE_PERCENT completePercent,"
    			+ "CT.PATH path,"
    			+ "CT.TASK_NAME taskName FROM CONSTRUCTION_TASK_DAILY CD "
    			+ "INNER JOIN WORK_ITEM WI ON WI.WORK_ITEM_ID = CD.WORK_ITEM_ID "
    			+ "INNER JOIN CONSTRUCTION_TASK CT ON CT.CONSTRUCTION_TASK_ID = CD.CONSTRUCTION_TASK_ID "
    			+ "INNER JOIN CONSTRUCTION CO ON CO.CONSTRUCTION_ID = CT.CONSTRUCTION_ID "
    			+ "LEFT JOIN CAT_STATION CS ON CO.CAT_STATION_ID = CS.CAT_STATION_ID "
//    			+ "LEFT JOIN CNT_CONSTR_WORK_ITEM_TASK STR ON STR.CONSTRUCTION_ID = CT.CONSTRUCTION_ID "
//    			+ "LEFT JOIN CNT_CONTRACT CNT ON CNT.CNT_CONTRACT_ID = STR.CNT_CONTRACT_ID "
				+ "LEFT JOIN (select max(b.code)code,a.construction_id from CNT_CONSTR_WORK_ITEM_TASK a,CNT_CONTRACT b where a.CNT_CONTRACT_id=b.CNT_CONTRACT_id "
				+ " and b.status !=0 and a.status!=0 and b.contract_type=0 group by a.CONSTRUCTION_ID ) CNT ON cnt.CONSTRUCTION_ID = CT.CONSTRUCTION_ID "
    			+ "LEFT JOIN CAT_PROVINCE CP ON CP.CAT_PROVINCE_ID = CS.CAT_PROVINCE_ID "
    			+ "LEFT JOIN SYS_GROUP SY ON SY.SYS_GROUP_ID = CD.SYS_GROUP_ID "
    			+ "LEFT JOIN SYS_USER SU ON SU.SYS_USER_ID = CD.CREATED_USER_ID "
//    			+ "WHERE 1=1 AND CD.TYPE IN(10,11,12) ");
    			+ "WHERE 1=1 AND ct.sys_group_id not in(166656,260629,260657,166617,166635) ");
//    	hoanm1_20190527_end
//    	hoanm1_20190528_start
    	if (groupIdList != null && !groupIdList.isEmpty()) {
    		sql.append(" and CT.SYS_GROUP_ID in :groupIdList ");
        }
//    	hoanm1_20190528_end
    	if (StringUtils.isNotEmpty(obj.getKeySearch())) {
    		sql.append(" AND (upper(CS.CODE) like upper(:keySearch) or upper(CO.CODE) like upper(:keySearch)) ");
        }
    	if(obj.getConfirmLst() != null){
        	sql.append(" AND CD.CONFIRM IN (:confirmLst) ");
    	}
    	if(StringUtils.isNotBlank(obj.getType())){
    		sql.append(" AND CD.TYPE =:type ");
    	}
    	if(obj.getStartDate() != null){
    		sql.append(" AND TRUNC(CD.CREATED_DATE) >= :startDate ");
    	}
    	if(obj.getEndDate() != null){
    		sql.append(" AND TRUNC(CD.CREATED_DATE) <= :endDate ");
    	}
    	if(obj.getSysUserId() != null){
    		sql.append(" AND CD.CREATED_USER_ID =:sysUserId ");
    	}
    	if(obj.getSysGroupId() != null){
    		sql.append(" AND CT.SYS_GROUP_ID=:sysGroupId ");
    	}
    	sql.append(" ORDER BY CD.CREATED_DATE DESC ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery("SELECT COUNT(*) FROM (" + sql.toString() + ")");
//        hoanm1_20190528_start
        if (groupIdList != null && !groupIdList.isEmpty()) {
            query.setParameterList("groupIdList", groupIdList);
            queryCount.setParameterList("groupIdList", groupIdList);
        }
//        hoanm1_20190528_end
        if (StringUtils.isNotEmpty(obj.getKeySearch())) {
        	query.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
        	queryCount.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
        }
        if(obj.getConfirmLst() != null){
    		query.setParameterList("confirmLst", obj.getConfirmLst());
        	queryCount.setParameter("confirmLst", obj.getConfirmLst());
    	}
    	if(StringUtils.isNotBlank(obj.getType())){
    		query.setParameter("type", obj.getType());
        	queryCount.setParameter("type", obj.getType());
    	}
    	if(obj.getStartDate() != null){
    		query.setParameter("startDate", obj.getStartDate());
        	queryCount.setParameter("startDate", obj.getStartDate());
    	}
    	if(obj.getEndDate() != null){
    		query.setParameter("endDate", obj.getEndDate());
        	queryCount.setParameter("endDate", obj.getEndDate());
    	}
    	if(obj.getSysUserId() != null){
    		query.setParameter("sysUserId", obj.getSysUserId());
        	queryCount.setParameter("sysUserId", obj.getSysUserId());
    	}
    	if(obj.getSysGroupId() != null){
    		query.setParameter("sysGroupId", obj.getSysGroupId());
        	queryCount.setParameter("sysGroupId", obj.getSysGroupId());
    	}
    	
    	query.addScalar("constructionTaskDailyId", new LongType());
    	query.addScalar("sysGroupId", new LongType());
    	query.addScalar("amount", new DoubleType());
    	query.addScalar("type", new StringType());
    	query.addScalar("confirm", new StringType());
    	query.addScalar("createdDate", new DateType());
    	query.addScalar("createdUserId", new LongType());
    	query.addScalar("createdGroupId", new LongType());
    	query.addScalar("constructionTaskId", new LongType());
    	query.addScalar("approveDate", new DateType());
    	query.addScalar("approveUserId", new LongType());
    	query.addScalar("quantity", new DoubleType());
    	query.addScalar("workItemId", new LongType());
    	query.addScalar("workItemName", new StringType());
    	query.addScalar("constructionCode", new StringType());
    	query.addScalar("catStationId", new LongType());
    	query.addScalar("catStationCode", new StringType());
    	query.addScalar("catTaskId", new LongType());
    	query.addScalar("taskName", new StringType());
    	query.addScalar("startDate", new DateType());
    	query.addScalar("endDate", new DateType());
    	query.addScalar("sysGroupName", new StringType());
    	query.addScalar("fullName", new StringType());
    	query.addScalar("status", new StringType());
    	query.addScalar("cntContractCode", new StringType());
    	query.addScalar("catProvinceCode", new StringType());
    	query.addScalar("statusConstruction", new StringType());
    	query.addScalar("constructionId", new LongType());
    	query.addScalar("amountConstruction", new DoubleType());
    	query.addScalar("statusConstructionTask", new StringType());
    	query.addScalar("completePercent", new StringType());
    	query.addScalar("path", new StringType());
        
        query.setResultTransformer(Transformers.aliasToBean(ConstructionTaskDailyDTO.class));
        if (obj.getPage() != null && obj.getPageSize() != null) {
            query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
            query.setMaxResults(obj.getPageSize().intValue());
        }
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

        return query.list();
    }
    
    public int updateConstructionTask(ConstructionTaskDTO criteria) {
        // complete percent cv
        StringBuilder stringBuilder = new StringBuilder("UPDATE CONSTRUCTION_TASK CT set ");
        double percent = 0;
        stringBuilder.append(" CT.STATUS = :status");
        if (criteria.getAmount() != null) {
            stringBuilder.append(", CT.COMPLETE_PERCENT = :completePercent");
            double amountDaily = getTotalAmountDaily(criteria);
            if (null != criteria.getAmount()) {
                percent = amountDaily / criteria.getAmount() * 100;
                setCompletePercent(percent);
            }
        }
        stringBuilder.append(" where CT.CONSTRUCTION_TASK_ID = :constructionTaskId");
        SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());

        if (criteria.getAmount() != null) {
            query.setParameter("completePercent", percent);
        }
        if (percent < 100) {
            query.setParameter("status", "2");
        } else {
            query.setParameter("status", "4");
        }
        query.setParameter("constructionTaskId", criteria.getConstructionTaskId());
        int rs = query.executeUpdate();
        if (criteria.getAmount() != null) {
            String[] lstPath = criteria.getPath().split("/");
            String sysGroupId = lstPath[1];
            String constructionId = lstPath[2];
            String workItemId = lstPath[3];
            updateWorkItemConstructionTask(workItemId, constructionId);
            updateSysGroupTask(sysGroupId);
        }
        // hoanm1_20180412_end
        return rs;
    }
    
    public Double getTotalAmountDaily(ConstructionTaskDTO criteria) {
        StringBuilder sql = new StringBuilder(
                " SELECT nvl(SUM(a.AMOUNT), 0) amount FROM CONSTRUCTION_TASK_DAILY a where a.confirm in(0,1)");
        if (null != criteria.getCatTaskId()) {
            sql.append(" and a.CAT_TASK_ID = :catTaskId");
        }
        if (null != criteria.getWorkItemId()) {
            sql.append(" and a.WORK_ITEM_ID = :workItemId");
        }
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        if (null != criteria.getCatTaskId()) {
            query.setParameter("catTaskId", criteria.getCatTaskId());
        }
        if (null != criteria.getWorkItemId()) {
            query.setParameter("workItemId", criteria.getWorkItemId());
        }
        BigDecimal result = (BigDecimal) query.uniqueResult();
        return result.doubleValue();
    }
    
    public void updateWorkItemConstructionTask(String workItemId, String constructionId) {
        // update lai hang muc trong bang construction_task
        StringBuilder sqlWorkItemTask = new StringBuilder(" UPDATE CONSTRUCTION_TASK c1 SET "
                + " c1.COMPLETE_PERCENT = ROUND((SELECT AVG(nvl(c2.COMPLETE_PERCENT,0)) FROM CONSTRUCTION_TASK c2 WHERE c2.PARENT_ID = :parentId),2),"
                + " c1.START_DATE =( SELECT min(c3.START_DATE) From CONSTRUCTION_TASK c3 WHERE c3.PARENT_ID = :parentId),"
                + " c1.END_DATE =( SELECT max(c4.END_DATE) From CONSTRUCTION_TASK c4 WHERE c4.PARENT_ID = :parentId)"
                + " Where c1.CONSTRUCTION_TASK_ID = :parentId ");
        SQLQuery queryWorkItemTask = getSession().createSQLQuery(sqlWorkItemTask.toString());
        queryWorkItemTask.setParameter("parentId", Long.parseLong(workItemId));
        queryWorkItemTask.executeUpdate();
        // update lai cong trinh trong bang construction_task
        StringBuilder sqlConstructionTask = new StringBuilder(" UPDATE CONSTRUCTION_TASK c1 SET "
                + " c1.COMPLETE_PERCENT = ROUND((SELECT AVG(nvl(c2.COMPLETE_PERCENT,0)) FROM CONSTRUCTION_TASK c2 WHERE c2.PARENT_ID = :parentId),2),"
                + " c1.START_DATE =( SELECT min(c3.START_DATE) From CONSTRUCTION_TASK c3 WHERE c3.PARENT_ID = :parentId),"
                + " c1.END_DATE =( SELECT max(c4.END_DATE) From CONSTRUCTION_TASK c4 WHERE c4.PARENT_ID = :parentId)"
                + " Where c1.CONSTRUCTION_TASK_ID = :parentId ");
        SQLQuery queryConstructionTask = getSession().createSQLQuery(sqlConstructionTask.toString());
        queryConstructionTask.setParameter("parentId", Long.parseLong(constructionId));
        queryConstructionTask.executeUpdate();

    }
    
    public void updateSysGroupTask(String sysGroupId) {
        // update lai chi nhanh trong bang construction_task
        StringBuilder sqlUnitTask = new StringBuilder(" UPDATE CONSTRUCTION_TASK c1 SET "
                + " c1.COMPLETE_PERCENT = ROUND((SELECT AVG(nvl(c2.COMPLETE_PERCENT,0)) FROM CONSTRUCTION_TASK c2 WHERE c2.PARENT_ID = :parentId),2),"
                + " c1.START_DATE =( SELECT min(c3.START_DATE) From CONSTRUCTION_TASK c3 WHERE c3.PARENT_ID = :parentId),"
                + " c1.END_DATE =( SELECT max(c4.END_DATE) From CONSTRUCTION_TASK c4 WHERE c4.PARENT_ID = :parentId)"
                + " Where c1.CONSTRUCTION_TASK_ID = :parentId ");
        SQLQuery queryUnitTask = getSession().createSQLQuery(sqlUnitTask.toString());
        queryUnitTask.setParameter("parentId", Long.parseLong(sysGroupId));
        queryUnitTask.executeUpdate();

    }
    
    public int updateStatusWorkItem(ConstructionTaskDailyDTO criteria) {
        StringBuilder stringBuilder = new StringBuilder("UPDATE WORK_ITEM w set W.STATUS = :status");
        stringBuilder.append(" where W.WORK_ITEM_ID = :workItemId");
        SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
        query.setParameter("status", criteria.getStatus());
        query.setParameter("workItemId", criteria.getWorkItemId());
        return query.executeUpdate();
    }
    
    public int approveQuantityWorkItem(Long workItemId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(
                "UPDATE WORK_ITEM wi set wi.QUANTITY = (select sum(ctd.quantity) from CONSTRUCTION_TASK_DAILY ctd"
                        + " WHERE ctd.CONSTRUCTION_TASK_ID in"
                        + " (SELECT ct.CONSTRUCTION_TASK_ID FROM CONSTRUCTION_TASK ct"
                        + " where ct.WORK_ITEM_ID = wi.WORK_ITEM_ID) and ctd.confirm=1)"
                        + " where wi.WORK_ITEM_ID= :workItemId");
        SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
        query.setParameter("workItemId", workItemId);
        return query.executeUpdate();
    }
    
    public void recalculateValueConstruction(Long constructionId) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE CONSTRUCTION con ");
        sql.append(" SET con.COMPLETE_VALUE= ");
        sql.append(" 	(SELECT SUM(quantity) ");
        sql.append(" 	 FROM work_item WORK ");
        sql.append(" 	 	INNER JOIN CONSTRUCTION con ");
        sql.append(" 	    ON WORK.CONSTRUCTION_ID=con.CONSTRUCTION_ID ");
        sql.append(" 	 WHERE con.CONSTRUCTION_ID= :constructionId ");
        sql.append(" 	)");
        sql.append(" 	WHERE con.CONSTRUCTION_ID=:constructionId ");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("constructionId", constructionId);
        query.executeUpdate();
    }
    
	public void updateConstructionTaskDaily(ConstructionTaskDailyDTO constructionTaskDailyDTO,boolean isUpdateQuantity) {
		StringBuilder stringBuilder = new StringBuilder("UPDATE CONSTRUCTION_TASK_DAILY CTD SET CTD.CONFIRM = :confirm");
		if (isUpdateQuantity) {
			stringBuilder.append(", CTD.AMOUNT = :amount"
							+ ", CTD.QUANTITY = :quantity"
							+ ", CTD.APPROVE_DATE = :approveDate, CTD.APPROVE_USER_ID = :userId");
		}
		stringBuilder.append(" WHERE CTD.CONSTRUCTION_TASK_DAILY_ID = :taskId");

		SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
		query.setParameter("confirm", constructionTaskDailyDTO.getConfirm());
		if (isUpdateQuantity) {
			query.setParameter("amount", constructionTaskDailyDTO.getAmount());
			query.setParameter("quantity", (constructionTaskDailyDTO.getPrice() != null) ? constructionTaskDailyDTO.getAmount() * constructionTaskDailyDTO.getPrice()
							: 0);
			query.setParameter("approveDate",constructionTaskDailyDTO.getApproveDate());
			query.setParameter("userId",constructionTaskDailyDTO.getApproveUserId());
		}
		query.setParameter("taskId", constructionTaskDailyDTO.getConstructionTaskDailyId());
		query.executeUpdate();
	}
	
	public void updateConstructionTask(ConstructionTaskDailyDTO obj) {
		StringBuilder stringBuilder = new StringBuilder("UPDATE CONSTRUCTION_TASK SET STATUS=:status"
				+ ",COMPLETE_PERCENT=:completePercent "
				+ "WHERE CONSTRUCTION_TASK_ID=:constructionTaskId ");

		SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
		query.setParameter("status", obj.getStatus());
		query.setParameter("completePercent", obj.getCompletePercent());
		query.setParameter("constructionTaskId", obj.getConstructionTaskId());
		query.executeUpdate();
	}
	
	public void updateInformation(ConstructionTaskDailyDTO obj) {
		String [] path = obj.getPath().split("/");
		String sysGroupId = path[1];
		String constructionId = path[2];
		String workItemId = path[3];
		updateWorkItemConstructionTask(workItemId, constructionId);
		WorkItemDTO getWById = getWbyId(obj.getWorkItemId());
		obj.setConstructionId(getWById.getConstructionId());
		updateSysGroupTask(sysGroupId);
//		SysUserCOMSDTO userDto = getSysUserBySysUserId(obj.getCreatedUserId());
//        ConstructionTaskDTOUpdateRequest stationProvinceDTO = getStationProvince(obj);
//        ConstructionTaskDTO  constructionTaskDTO = getConstructionTaskById(obj.getConstructionTaskId());
//        insertLogUpdate(userDto, obj, stationProvinceDTO,constructionTaskDTO);
       if(obj.getStartingDateTK() == null ){
	       StringBuilder sqlCst = new StringBuilder(" update construction set starting_date=sysdate,status=3 where construction_id = :constructionId ");
	       SQLQuery queryWorkItem = getSession().createSQLQuery(sqlCst.toString());
	       queryWorkItem.setParameter("constructionId", obj.getConstructionId());
	       queryWorkItem.executeUpdate();
       }
       // update status=2 vao work_item
       StringBuilder sqlWorkItem = new StringBuilder(" update work_item set status=2 where work_item_id in  ");
       sqlWorkItem.append("(select work_item_id from construction_task where construction_task_id= :constructionTaskId )");
       SQLQuery queryWorkItem = getSession().createSQLQuery(sqlWorkItem.toString());
       queryWorkItem.setParameter("constructionTaskId", obj.getConstructionTaskId());
       queryWorkItem.executeUpdate();
	}
	
	public SysUserCOMSDTO getSysUserBySysUserId(Long sysUserId) {
        StringBuilder sql = new StringBuilder("SELECT SU.SYS_USER_ID sysUserId "); 
		sql.append(",SU.LOGIN_NAME loginName ");
		sql.append(",SU.FULL_NAME fullName ");
		sql.append(",SU.PASSWORD password ");
		sql.append(",SU.EMPLOYEE_CODE employeeCode ");
		sql.append(",SU.EMAIL email ");
		sql.append(",SU.PHONE_NUMBER phoneNumber ");
		sql.append(",SU.STATUS status ");
		sql.append(",SU.SYS_GROUP_ID departmentId ");
		sql.append(",SY.NAME sysGroupName ");
		sql.append(" FROM SYS_USER SU ");
		sql.append("LEFT JOIN sys_group SY ");
		sql.append("ON SU.SYS_GROUP_ID = SY.SYS_GROUP_ID ");
		sql.append("WHERE SU.SYS_USER_ID = :sysUserId ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.addScalar("sysUserId", new LongType());
        query.addScalar("loginName", new StringType());
        query.addScalar("fullName", new StringType());
        query.addScalar("password", new StringType());
        query.addScalar("employeeCode", new StringType());
        query.addScalar("email", new StringType());
        query.addScalar("phoneNumber", new StringType());
        query.addScalar("status", new StringType());
        query.addScalar("departmentId", new LongType());
        query.addScalar("sysGroupName", new StringType());
        query.setParameter("sysUserId", sysUserId);
        query.setResultTransformer(Transformers.aliasToBean(SysUserCOMSDTO.class));
        return (SysUserCOMSDTO) query.list().get(0);
    }
	
	public ConstructionTaskDTOUpdateRequest getStationProvince(ConstructionTaskDailyDTO req) {

        StringBuilder sql = new StringBuilder("select b.code stationCode,");
		sql.append(" c.code provinceCode,");
		sql.append(" d.name constructionTypeName,");
		sql.append(" d.cat_construction_type_id constructionTypeId ");
		sql.append(" from construction a,cat_station b,cat_province c,cat_construction_type d ");
		sql.append(" where a.CAT_STATION_ID=b.CAT_STATION_ID ");
		sql.append(" and b.cat_province_id=c.cat_province_id and a.cat_construction_type_id=d.cat_construction_type_id ");
		sql.append(" and a.construction_id = :constructionId ");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.addScalar("stationCode", new StringType());
        query.addScalar("provinceCode", new StringType());
        query.addScalar("constructionTypeName", new StringType());
        query.addScalar("constructionTypeId", new LongType());

        query.setParameter("constructionId", req.getConstructionId());

        query.setResultTransformer(Transformers.aliasToBean(ConstructionTaskDTOUpdateRequest.class));
        if(query.list().size()>0){
        	return (ConstructionTaskDTOUpdateRequest) query.list().get(0);
        }else{
        	return null;
        }
    }
	
	
	public void insertLogUpdate(SysUserCOMSDTO userDto, ConstructionTaskDailyDTO obj,ConstructionTaskDTOUpdateRequest stationProvinceDTO,ConstructionTaskDTO dto) {
			StringBuilder sqlWorkItemTask = new StringBuilder(
			" INSERT INTO KPI_LOG_MOBILE (KPI_LOG_MOBILE_ID, SYSUSERID, LOGINNAME, PASSWORD,  EMAIL, FULLNAME, "
			    + " EMPLOYEECODE, PHONENUMBER, SYSGROUPNAME, SYSGROUPID, TIME_DATE,update_time,FUNCTION_CODE,DESCRIPTION, "
			    + " cat_task_id,cat_task_name,work_item_id,work_item_name,construction_id,construction_code,"
			    + " status,completePercent,start_Date,constructionTaskId,stationCode,provinceCode,constructionTypeName,CAT_CONSTRUCTION_TYPE_ID,type)"
			    + " VALUES (KPI_LOG_MOBILE_seq.nextval,:SYSUSERID,:LOGINNAME,:PASSWORD,:EMAIL,:FULLNAME,"
			    + " :EMPLOYEECODE,:PHONENUMBER,:SYSGROUPNAME,:SYSGROUPID,sysdate,trunc(sysdate),:functionCode,:description, "
			    + "  :catTaskId,:taskName,:workItemId,:workItemName,:constructionId,:constructionCode,"
			    + " :status,:completePercent,:startDate,:constructionTaskId,:stationCode,:provinceCode,:constructionTypeName,:catConstructionTypeId,:type) ");
			SQLQuery queryWorkItemTask = getSession().createSQLQuery(sqlWorkItemTask.toString());
			
			queryWorkItemTask.setParameter("SYSUSERID", userDto.getSysUserId());
			queryWorkItemTask.setParameter("LOGINNAME", userDto.getLoginName());
			queryWorkItemTask.setParameter("PASSWORD", userDto.getPassword());
			queryWorkItemTask.setParameter("EMAIL", userDto.getEmail());
			queryWorkItemTask.setParameter("FULLNAME", userDto.getFullName());
			queryWorkItemTask.setParameter("EMPLOYEECODE", userDto.getEmployeeCode());
			queryWorkItemTask.setParameter("PHONENUMBER", userDto.getPhoneNumber());
			queryWorkItemTask.setParameter("SYSGROUPNAME", userDto.getSysGroupName());
			queryWorkItemTask.setParameter("SYSGROUPID", userDto.getDepartmentId());
			queryWorkItemTask.setParameter("functionCode", "UPDATE");
			queryWorkItemTask.setParameter("description", "Cập nhật tiến độ công việc");
			queryWorkItemTask.setParameter("catTaskId", obj.getCatTaskId());
			queryWorkItemTask.setParameter("taskName", dto.getTaskName());
			queryWorkItemTask.setParameter("workItemId", obj.getWorkItemId());
			queryWorkItemTask.setParameter("workItemName", dto.getWorkItemName());
			queryWorkItemTask.setParameter("constructionId", obj.getConstructionId());
			queryWorkItemTask.setParameter("constructionCode", dto.getConstructionCode());
			queryWorkItemTask.setParameter("status", dto.getStatus());
			queryWorkItemTask.setParameter("completePercent", obj.getCompletePercent());
			queryWorkItemTask.setParameter("startDate", dto.getStartDate());
			queryWorkItemTask.setParameter("constructionTaskId", obj.getConstructionTaskId());
			if(stationProvinceDTO !=null){
				queryWorkItemTask.setParameter("stationCode", stationProvinceDTO.getStationCode());
				queryWorkItemTask.setParameter("provinceCode", stationProvinceDTO.getProvinceCode());
				queryWorkItemTask.setParameter("constructionTypeName", stationProvinceDTO.getConstructionTypeName());
				queryWorkItemTask.setParameter("catConstructionTypeId", stationProvinceDTO.getConstructionTypeId());
			}else{
				queryWorkItemTask.setParameter("stationCode", 1);
				queryWorkItemTask.setParameter("provinceCode", 1);
				queryWorkItemTask.setParameter("constructionTypeName", 1);
				queryWorkItemTask.setParameter("catConstructionTypeId", 1);
			}
			queryWorkItemTask.setParameter("type", dto.getType());
			
			queryWorkItemTask.executeUpdate();
			
			}
	
	public WorkItemDTO getWbyId(Long workItemId){
		StringBuilder sql = new StringBuilder("SELECT CONSTRUCTION_ID constructionId FROM WORK_ITEM WHERE WORK_ITEM_ID =:workItemId ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("constructionId", new LongType());
		query.setParameter("workItemId", workItemId);
		query.setResultTransformer(Transformers.aliasToBean(WorkItemDTO.class));
		return (WorkItemDTO) query.uniqueResult();
	}
	
	public ConstructionTaskDTO getConstructionTaskById(Long constructionTaskId){
		StringBuilder sql = new StringBuilder("SELECT CT.TASK_NAME taskName,");
		sql.append("CT.STATUS status,");
		sql.append("CT.TYPE type,");
		sql.append("W.NAME workItemName,");
		sql.append("CO.CODE constructionCode,");
		sql.append("CO.STARTING_DATE startDate ");
		sql.append("FROM CONSTRUCTION_TASK CT ");
		sql.append("LEFT JOIN WORK_ITEM W ON CT.WORK_ITEM_ID = W.WORK_ITEM_ID ");
		sql.append("LEFT JOIN CONSTRUCTION CO ON W.CONSTRUCTION_ID = CO.CONSTRUCTION_ID ");
		sql.append("WHERE CT.CONSTRUCTION_TASK_ID =:constructionTaskId ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("taskName", new StringType());
		query.addScalar("status", new StringType());
		query.addScalar("type", new StringType());
		query.addScalar("constructionCode", new StringType());
		query.addScalar("startDate", new DateType());
		query.setParameter("constructionTaskId", constructionTaskId);
		query.setResultTransformer(Transformers.aliasToBean(ConstructionTaskDTO.class));
		return (ConstructionTaskDTO) query.uniqueResult();
	}
    /**Hoangnh start 18022019**/
	
	//huypq-20191008-start
	public List<WorkItemDTO> doSearchCompleteManage(WorkItemDTO obj,List<String> groupIdList){
		Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
		StringBuilder sql = new StringBuilder(" SELECT task.updated_date updateDate, " + 
				"  sysg.name sysGroupName, " + 
				"  cst.code constructionCode, " + 
				"  task.task_name workItemName, " + 
				"  round(task.quantity/1000000,2) quantity, " + 
				"  sysu.email email, " + 
				"  cats.code catStationCode, " + 
				"  cc.CODE cntContractCode, " + 
				"  task.type typeImport " + 
				"FROM CONSTRUCTION_TASK task " + 
				"inner JOIN construction cst " + 
				"ON task.construction_id=cst.construction_id " + 
				"inner JOIN sys_group sysg " + 
				"ON task.sys_group_id =sysg.sys_group_id " + 
				"inner JOIN sys_user sysu " + 
				"ON task.PERFORMER_ID = sysu.sys_user_id " + 
				"left join CTCT_CAT_OWNER.CAT_STATION cats " + 
				"on cats.CAT_STATION_ID = cst.CAT_STATION_ID " + 
				
				" left join (select distinct max(b.code)code,a.CONSTRUCTION_ID from CNT_CONSTR_WORK_ITEM_TASK a inner join "
				+ " CNT_CONTRACT b on a.CNT_CONTRACT_id=b.CNT_CONTRACT_id and a.status !=0 and b.status!=0 and b.contract_type=0 "
				+ " group by a.CONSTRUCTION_ID ) cc on task.CONSTRUCTION_ID=cc.CONSTRUCTION_ID" 
				+ "  WHERE ((level_id =3 AND task.type =1) or(level_id =4 AND task.type in(2,3))) " + 
				"  AND import_complete     =1  ");
		if(StringUtils.isNotBlank(obj.getKeySearch())) {
			sql.append(" AND (UPPER(cst.code) LIKE UPPER(:keySearch) escape '&' "
					+ " OR UPPER(cats.code) LIKE UPPER(:keySearch) escape '&') ");
		}
		
		if(null!=obj.getSysGroupId()) {
			sql.append(" AND sysg.SYS_GROUP_ID=:sysGroupId ");
		}
		
		if(null!=obj.getTypeImport()) {
			sql.append(" AND task.type=:type ");
		}
		if(null!=obj.getCntContractCode()) {
			sql.append(" AND cc.code=:contractCode ");
		}
		if (groupIdList != null && !groupIdList.isEmpty()) {
	         sql.append(" and task.sys_group_id in (:groupIdList) ");
	    }
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery("SELECT COUNT(*) FROM ("+ sql.toString() +")");
		
		query.addScalar("updateDate", new DateType());
		query.addScalar("sysGroupName", new StringType());
		query.addScalar("constructionCode", new StringType());
		query.addScalar("workItemName", new StringType());
		query.addScalar("quantity", new DoubleType());
		query.addScalar("email", new StringType());
		query.addScalar("catStationCode", new StringType());
		query.addScalar("cntContractCode", new StringType());
		query.addScalar("typeImport", new LongType());
		
		query.setResultTransformer(Transformers.aliasToBean(WorkItemDTO.class));
		
//		query.setParameter("year", year);
//		queryCount.setParameter("year", year);
//		query.setParameter("month", month);
//		queryCount.setParameter("month", month);
		
		if(StringUtils.isNotBlank(obj.getKeySearch())) {
			query.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
			queryCount.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
		}
		
		if(null!=obj.getSysGroupId()) {
			query.setParameter("sysGroupId", obj.getSysGroupId());
			queryCount.setParameter("sysGroupId", obj.getSysGroupId());
		}
		
		if(null!=obj.getTypeImport()) {
			query.setParameter("type", obj.getTypeImport());
			queryCount.setParameter("type", obj.getTypeImport());
		}
		if(null!=obj.getCntContractCode()) {
			query.setParameter("contractCode", obj.getCntContractCode());
			queryCount.setParameter("contractCode", obj.getCntContractCode());
		}
		if (groupIdList != null && !groupIdList.isEmpty()) {
			query.setParameterList("groupIdList", groupIdList);
			queryCount.setParameterList("groupIdList", groupIdList);
		}
		
		
		if (obj.getPage() != null && obj.getPageSize() != null) {
            query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
            query.setMaxResults(obj.getPageSize().intValue());
        }
		
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
		
		return query.list();
		
	}
	
	//Get DetailMonthPlanId of now month
	public DetailMonthPlanDTO getDetailMonthPlanId(Long sysGroupId) {
		StringBuilder sql = new StringBuilder("select DETAIL_MONTH_PLAN_ID detailMonthPlanId from detail_month_plan" + 
				" where month=to_char(sysdate, 'mm')" + 
				" and year=to_char(sysdate, 'yyyy')" + 
				" and SIGN_STATE=3 and status=1 and sys_group_id=:sysGroupId");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("detailMonthPlanId", new LongType());
		query.setResultTransformer(Transformers.aliasToBean(DetailMonthPlanDTO.class));
		query.setParameter("sysGroupId", sysGroupId);
		@SuppressWarnings("unchecked")
        List<DetailMonthPlanDTO> lst = query.list();
        if (lst.size() > 0) {
            return lst.get(0);
        }
        return null;
	}
	
	//Update bảng Construction_Task
	
	public Long updateConstructionTaskWork(DetailMonthPlanDTO obj) {
		StringBuilder sql = new StringBuilder("UPDATE construction_task " + 
				"SET status              =4, " + 
				"complete_percent      =100, " + 
				"import_complete       =1, " + 
				"updated_date=sysdate, " + 
				"quantity       =:quantity " + 
				"WHERE sys_group_id      = :sysGroupId " + 
				"AND detail_month_plan_id= :detailMonthPlanId " + 
				"AND type                =1 " + 
				" AND construction_id IN (SELECT cons.construction_id  FROM construction cons   " + 
				" WHERE upper(cons.code)=upper(:constructionCode) and status !=0) " + 
				" and work_item_id in (SELECT wi.WORK_ITEM_id FROM WORK_ITEM wi WHERE upper(wi.name)= upper(:workItemName))");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("sysGroupId", obj.getSysGroupId());
		query.setParameter("detailMonthPlanId", obj.getDetailMonthPlanId());
		query.setParameter("constructionCode", obj.getConstructionCode());
		query.setParameter("workItemName", obj.getWorkItemName());
		query.setParameter("quantity", obj.getQuantity());
		
		return (long)query.executeUpdate();
	}
	
	public Long updateWorkItem(DetailMonthPlanDTO obj) {
//		StringBuilder sql = new StringBuilder("Update work_item set status=3, " + 
//				"complete_date=sysdate, " + 
//				"quantity=:quantity,import_complete =1 " + 
//				"where name=:workItemName and construction_id in(select construction_id from construction cons where upper(cons.code)=upper(:constructionCode) and status !=0)");
		StringBuilder sql = new StringBuilder(" Update work_item wi set wi.status=3,"  
				+ " wi.complete_date=sysdate,wi.quantity=:quantity,wi.import_complete =1 " 
				+ " where name=:workItemName and construction_id in(select cons.construction_id from construction cons,construction_task task where task.construction_id=cons.construction_id and "
 				+ " task.work_item_id=wi.work_item_id and task.detail_month_plan_id=:detailMonthPlanId and task.type=1 and task.level_id=3 and upper(cons.code)=upper(:constructionCode) and cons.status !=0)"); 
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("quantity", obj.getQuantity());
		query.setParameter("workItemName", obj.getWorkItemName());
		query.setParameter("detailMonthPlanId", obj.getDetailMonthPlanId());
		query.setParameter("constructionCode", obj.getConstructionCode());
		
		return (long)query.executeUpdate();
	}
	
	public List<RpQuantityDTO> getDataForInsertRpQuantity(){
		StringBuilder sql = new StringBuilder("SELECT to_date(dateComplete,'dd/MM/yyyy') startingDate, " + 
				"  completeDate , "  + 
				"  performerId , "  + 
				"  performerName , "  + 
				"  constructorName AS sysgroupname, "  + 
				"  sysGroupIdSMS   AS sysGroupId, "  + 
				"  catstationcode, "  + 
				"  constructioncode, "  + 
				"  quantity, "  + 
				"  quantitybydate, "  + 
				"  status, "  + 
				"  cntcontractcode, "  + 
				"  catprovincecode, "  + 
				"  workitemid, "  + 
				"  name workitemname, "  + 
				"  constructionid, "  + 
				"  statusconstruction, "  + 
				"  approvecompletevalue, "  + 
				"  catprovinceid, "  + 
				"  price, "  + 
				"  obstructedstate, "  + 
				"  approvecompletedate, "  + 
				"  TO_CHAR(TRUNC (sysdate, 'MONTH'),'yyyyMM') processDate, "  + 
				"  catPartnerId, "  + 
				"  partnername "  + 
				" FROM "  + 
				"  (WITH tblTaskDaily AS "  + 
				"  (SELECT TO_CHAR(ctd.CREATED_DATE, 'dd/MM/yyyy') CREATED_DATE, "  + 
				"  TO_CHAR(ctd.CREATED_DATE, 'MM/yyyy') CREATED_DATE_MONTH, "  + 
				"  ctd.CREATED_USER_ID, "  + 
				"  ctd.sys_group_id , "  + 
				"  SUM(ctd.amount) amount, "  + 
				"  SUM(ctd.QUANTITY) quantity , "  + 
				"  ctd.CONSTRUCTION_TASK_ID, "  + 
				"  ctd.work_item_id "  + 
				"  FROM CONSTRUCTION_TASK_DAILY ctd "  + 
				"  WHERE CTD.WORK_ITEM_ID      IS NOT NULL "  + 
				"  AND TRUNC(CTD.CREATED_DATE) >= TRUNC (sysdate, 'MONTH') "  + 
				"  AND TRUNC(CTD.CREATED_DATE) <= sysdate "  + 
				"  GROUP BY ctd.CONSTRUCTION_TASK_ID, "  + 
				"  TO_CHAR(ctd.CREATED_DATE, 'dd/MM/yyyy'), "  + 
				"  TO_CHAR(ctd.CREATED_DATE, 'MM/yyyy') , "  + 
				"  ctd.CREATED_USER_ID, "  + 
				"  ctd.sys_group_id, "  + 
				"  ctd.work_item_id "  + 
				"  ) "  + 
				" SELECT DISTINCT ctd.CREATED_DATE dateComplete, "  + 
				"  a.COMPLETE_DATE completeDate, "  + 
				"  a.PERFORMER_ID performerId, "  + 
				"  sysu.FULL_NAME performerName, "  + 
				"  (SELECT name FROM sys_group SYS WHERE sys.SYS_GROUP_ID=task.SYS_GROUP_ID "  + 
				"  ) constructorName, "  + 
				"  task.SYS_GROUP_ID sysGroupIdSMS, "  + 
				"  cat.CODE catstationcode, "  + 
				"  b.CODE constructioncode, "  + 
				"  a.NAME name, "  + 
				"  (SELECT SUM(cc.quantity) "  + 
				"  FROM CONSTRUCTION_TASK_DAILY cc "  + 
				"  WHERE TO_CHAR(cc.created_date, 'dd/MM/yyyy') = ctd.CREATED_DATE "  + 
				"  AND cc.work_item_id                          = ctd.work_item_id "  + 
				"  AND cc.confirm                               = 1 "  + 
				"  )/1000000 quantity, "  + 
				"  (SELECT 1 FROM dual "  + 
				"  ) quantitybydate, "  + 
				"  a.STATUS status, "  + 
				"  (SELECT MAX(b.code) "  + 
				"  FROM CNT_CONSTR_WORK_ITEM_TASK cnt_task, "  + 
				"  CNT_CONTRACT b "  + 
				"  WHERE cnt_task.CNT_CONTRACT_ID=b.CNT_CONTRACT_ID "  + 
				"  AND b.CONTRACT_TYPE           = 0 "  + 
				"  AND cnt_task.status           =1 "  + 
				"  AND b.status                 !=0 "  + 
				"  AND cnt_task.CONSTRUCTION_ID  =b.CONSTRUCTION_ID "  + 
				"  ) cntcontractcode, "  + 
				"  catPro.CODE catprovincecode, "  + 
				"  a.WORK_ITEM_ID workitemid, "  + 
				"  b.CONSTRUCTION_ID constructionid, "  + 
				"  b.status statusconstruction, "  + 
				"  b.approve_complete_value approvecompletevalue , "  + 
				"  catPro.CAT_PROVINCE_ID catprovinceid, "  + 
				"  b.price price, "  + 
				"  b.OBSTRUCTED_STATE obstructedstate , "  + 
				"  b.approve_complete_date approvecompletedate, "  + 
				"  par.cat_partner_id catPartnerId, "  + 
				"  par.name partnername "  + 
				" FROM WORK_ITEM a "  + 
				" LEFT JOIN SYS_USER sysu "  + 
				" ON sysu.SYS_USER_ID = a.PERFORMER_ID "  + 
				" JOIN CONSTRUCTION b "  + 
				" ON A.CONSTRUCTION_ID = B.CONSTRUCTION_ID "  + 
				" INNER JOIN "  + 
				"  (SELECT DISTINCT a.sys_group_id, "  + 
				"  a.CONSTRUCTION_ID "  + 
				"  FROM construction_task a, "  + 
				"  detail_month_plan b "  + 
				"  WHERE a.detail_month_plan_id=b.detail_month_plan_id "  + 
				"  AND b.sign_state            =3 "  + 
				"  AND b.status                =1 "  + 
				"  AND a.type                  =1 "  + 
				"  AND a.level_id              =2 "  + 
				"  ) task "  + 
				" ON b.CONSTRUCTION_ID=task.CONSTRUCTION_ID "  + 
				" JOIN tblTaskDaily ctd "  + 
				" ON CTD.WORK_ITEM_ID = A.WORK_ITEM_ID "  + 
				" LEFT JOIN CAT_STATION cat "  + 
				" ON b.CAT_STATION_ID =cat.CAT_STATION_ID "  + 
				" LEFT JOIN cat_province catPro "  + 
				" ON catPro.CAT_PROVINCE_ID = cat.CAT_PROVINCE_ID "  + 
				" LEFT JOIN cat_partner par "  + 
				" ON a.constructor_id=par.cat_partner_id "  + 
				" UNION "  + 
				" SELECT DISTINCT "  + 
				"  CASE "  + 
				"  WHEN a.STATUS= 3 "  + 
				"  THEN TO_CHAR(a.complete_date,'dd/MM/yyyy') "  + 
				"  WHEN a.STATUS=2 "  + 
				"  THEN TO_CHAR(b.complete_date,'dd/MM/yyyy') "  + 
				"  END dateComplete, "  + 
				"  a.COMPLETE_DATE completeDate, "  + 
				"  a.PERFORMER_ID performerId, "  + 
				"  sysu.FULL_NAME performerName, "  + 
				"  (SELECT name FROM sys_group SYS WHERE sys.SYS_GROUP_ID=task.SYS_GROUP_ID "  + 
				"  ) constructorName, "  + 
				"  task.SYS_GROUP_ID sysGroupIdSMS, "  + 
				"  cat.CODE catstationcode, "  + 
				"  b.CODE constructioncode, "  + 
				"  a.NAME name, "  + 
				"  a.QUANTITY/1000000 quantity, "  + 
				"  (SELECT 0 FROM dual "  + 
				"  ) quantitybydate, "  + 
				"  a.STATUS status, "  + 
				"  (SELECT MAX(b.code) "  + 
				"  FROM CNT_CONSTR_WORK_ITEM_TASK cnt_task, "  + 
				"  CNT_CONTRACT b "  + 
				"  WHERE cnt_task.CNT_CONTRACT_ID=b.CNT_CONTRACT_ID "  + 
				"  AND b.CONTRACT_TYPE           = 0 "  + 
				"  AND cnt_task.status           =1 "  + 
				"  AND b.status                 !=0 "  + 
				"  AND cnt_task.CONSTRUCTION_ID  =b.CONSTRUCTION_ID "  + 
				"  ) cntcontractcode, "  + 
				"  catPro.CODE catprovincecode, "  + 
				"  a.WORK_ITEM_ID workitemid, "  + 
				"  b.CONSTRUCTION_ID constructionid, "  + 
				"  b.status statusconstruction, "  + 
				"  b.approve_complete_value approvecompletevalue , "  + 
				"  catPro.CAT_PROVINCE_ID catprovinceid, "  + 
				"  b.price price, "  + 
				"  b.OBSTRUCTED_STATE obstructedstate , "  + 
				"  b.approve_complete_date approvecompletedate, "  + 
				"  par.cat_partner_id catPartnerId, "  + 
				"  par.name partnername "  + 
				" FROM WORK_ITEM a "  + 
				" LEFT JOIN SYS_USER sysu "  + 
				" ON sysu.SYS_USER_ID = a.PERFORMER_ID "  + 
				" JOIN CONSTRUCTION b "  + 
				" ON A.CONSTRUCTION_ID = B.CONSTRUCTION_ID "  + 
				" INNER JOIN "  + 
				"  (SELECT DISTINCT a.sys_group_id, "  + 
				"  a.CONSTRUCTION_ID "  + 
				"  FROM construction_task a, "  + 
				"  detail_month_plan b "  + 
				"  WHERE a.detail_month_plan_id=b.detail_month_plan_id "  + 
				"  AND b.sign_state            =3 "  + 
				"  AND b.status                =1 "  + 
				"  AND a.type                  =1 "  + 
				"  AND a.level_id              =2 "  + 
				"  ) task "  + 
				" ON b.CONSTRUCTION_ID=task.CONSTRUCTION_ID "  + 
				" LEFT JOIN CAT_STATION cat "  + 
				" ON b.CAT_STATION_ID =cat.CAT_STATION_ID "  + 
				" LEFT JOIN cat_province catPro "  + 
				" ON catPro.CAT_PROVINCE_ID = cat.CAT_PROVINCE_ID "  + 
				" LEFT JOIN cat_partner par "  + 
				" ON a.constructor_id    =par.cat_partner_id "  + 
				" WHERE 1                =1 "  + 
				" AND (a.STATUS          = 3 "  + 
				" OR (a.STATUS           =2 "  + 
				" AND b.STATUS           =4 "  + 
				" AND b.OBSTRUCTED_STATE =2) ) "  + 
				" AND NOT EXISTS "  + 
				"  (SELECT CTD.WORK_ITEM_ID "  + 
				"  FROM CONSTRUCTION_TASK_DAILY ctd "  + 
				"  WHERE CTD.WORK_ITEM_ID = A.WORK_ITEM_ID "  + 
				"  ) "  + 
				" AND TRUNC( "  + 
				"  CASE "  + 
				"  WHEN a.STATUS= 3 "  + 
				"  THEN a.complete_date "  + 
				"  WHEN a.STATUS=2 "  + 
				"  THEN b.complete_date "  + 
				"  END ) >= TRUNC (sysdate, 'MONTH') "  + 
				" AND TRUNC( "  + 
				"  CASE "  + 
				"  WHEN a.STATUS= 3 "  + 
				"  THEN a.complete_date "  + 
				"  WHEN a.STATUS=2 "  + 
				"  THEN b.complete_date "  + 
				"  END ) <= sysdate "  + 
				"  )" );
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("startingDate", new DateType());
		query.addScalar("completeDate", new DateType());
		query.addScalar("performerId", new LongType());
		query.addScalar("performerName", new StringType());
		
		query.addScalar("sysgroupname", new StringType());
		query.addScalar("sysGroupId", new LongType());
		query.addScalar("catstationcode", new StringType());
		
		query.addScalar("constructioncode", new StringType());
		query.addScalar("quantity", new DoubleType());
		query.addScalar("quantitybydate", new StringType());
		query.addScalar("status", new StringType());
		query.addScalar("cntcontractcode", new StringType());
		query.addScalar("catprovincecode", new StringType());
		query.addScalar("workitemid", new LongType());
		
		query.addScalar("workitemname", new StringType());
		query.addScalar("constructionid", new LongType());
		query.addScalar("statusconstruction", new StringType());
		query.addScalar("approvecompletevalue", new DoubleType());
		query.addScalar("catprovinceid", new LongType());
		query.addScalar("price", new DoubleType());
		query.addScalar("obstructedstate", new StringType());
		query.addScalar("approvecompletedate", new DateType());
		query.addScalar("processDate", new StringType());
		query.addScalar("catPartnerId", new LongType());
		query.addScalar("partnername", new StringType());
		
		query.setResultTransformer(Transformers.aliasToBean(RpQuantityDTO.class));

		return query.list();
	}
	
	public Long deleteRpQuantity(Long sysGroupId) {
		StringBuilder sql = new StringBuilder("delete from rp_quantity "
				+ "where process_date=to_char(TRUNC (sysdate, 'MONTH'),'yyyyMM') "
//				+ " and SYS_GROUP_ID=:sysGroupId "
				+ "");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
//		query.setParameter("sysGroupId", sysGroupId);
		return (long)query.executeUpdate();
	}
	
	//Huy-end
	
	//Huypq-20191021-start
	public void updateConstructionTaskQuyLuong(DetailMonthPlanDTO obj) {
		StringBuilder sql = new StringBuilder(" UPDATE construction_task " + 
				"SET status              =4, " + 
				" complete_percent      =100, " + 
				" import_complete       =1, " + 
				" quantity              =:quantity, " + 
				" updated_date           =sysdate " + 
				" WHERE sys_group_id      =:sysGroupId " + 
				" AND detail_month_plan_id=:detailMonthPlanId " + 
				" AND type                =2 " + 
				" AND construction_id    IN " + 
				" (SELECT construction_id " + 
				"  FROM construction " + 
				"  WHERE upper(code)=upper(:constructionCode) " + 
				"  ) ");
		
		StringBuilder sqlCons = new StringBuilder(" UPDATE construction " + 
				"SET COMPLETE_APPROVED_VALUE_PLAN =:quantity, " + 
				"  APPROVE_COMPLETE_STATE = 1, " + 
				"  APPROVE_COMPLETE_DATE = sysdate " + 
				"WHERE upper(code) = upper(:constructionCode) and construction_id in(select construction_id from construction_task where detail_month_plan_id= :detailMonthPlanId and type=2) ");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCons = getSession().createSQLQuery(sqlCons.toString());
		
		query.setParameter("quantity", obj.getQuantity());
		query.setParameter("sysGroupId", obj.getSysGroupId());
		query.setParameter("detailMonthPlanId", obj.getDetailMonthPlanId());
		query.setParameter("constructionCode", obj.getConstructionCode());
		
		queryCons.setParameter("quantity", obj.getQuantity());
		queryCons.setParameter("constructionCode", obj.getConstructionCode());
		queryCons.setParameter("detailMonthPlanId", obj.getDetailMonthPlanId());
		
		query.executeUpdate();
		queryCons.executeUpdate();
	}
	
	public void updateConstructionTaskDoanhThu(DetailMonthPlanDTO obj) {
		StringBuilder sql = new StringBuilder(" UPDATE construction_task " + 
				"SET status              = 4, " + 
				" complete_percent      = 100, " + 
				" import_complete       = 1, " + 
				" quantity              = :quantity, " + 
				" updated_date           = sysdate " + 
				" WHERE sys_group_id      = :sysGroupId " + 
				" AND detail_month_plan_id= :detailMonthPlanId " + 
				" AND type                = 3 " + 
				" AND construction_id    IN " + 
				" (SELECT construction_id " + 
				" 	FROM construction " + 
				"  	WHERE upper(code)=upper(:constructionCode) " + 
				"  ) ");
		
		StringBuilder sqlCons = new StringBuilder(" UPDATE construction " + 
				"SET APPROVE_REVENUE_VALUE_PLAN = :quantity, " + 
				"  APPROVE_REVENUE_STATE        =1, " + 
				"  APPROVE_REVENUE_DATE         = sysdate " + 
				"WHERE upper(code)              =upper(:constructionCode) and construction_id in(select construction_id from construction_task where detail_month_plan_id= :detailMonthPlanId and type=3)");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCons = getSession().createSQLQuery(sqlCons.toString());
		
		query.setParameter("quantity", obj.getQuantity());
		query.setParameter("sysGroupId", obj.getSysGroupId());
		query.setParameter("detailMonthPlanId", obj.getDetailMonthPlanId());
		query.setParameter("constructionCode", obj.getConstructionCode());
		
		queryCons.setParameter("quantity", obj.getQuantity());
		queryCons.setParameter("constructionCode", obj.getConstructionCode());
		queryCons.setParameter("detailMonthPlanId", obj.getDetailMonthPlanId());
		
		query.executeUpdate();
		queryCons.executeUpdate();
	}
	//Huy-end
	
	//Huypq-20200227-start
	public List<ConstructionTaskDTO> getConsTaskByDetailMonthPlan(Long monthPlanId){
//		StringBuilder sql = new StringBuilder(" select TASK_NAME taskName, "
//				+ " CONSTRUCTION_ID constructionId "
//				+ " from construction_task "
//				+ " where status!=0 "
//				+ " and type=1 "
//				+ " and LEVEL_ID=3 "
//				+ " and detail_month_plan_id = :monthPlanId ");
		StringBuilder sql = new StringBuilder(" select DISTINCT wi.WORK_ITEM_ID workItemId, "
				+ " wi.CONSTRUCTION_ID constructionId "
				+ " from WORK_ITEM wi"
				+ " left join CONSTRUCTION_TASK ct on wi.WORK_ITEM_ID = ct.WORK_ITEM_ID "
				+ " where ct.status!=0 "
				+ " and wi.status!=0 "
				+ " and ct.type=1 "
				+ " and ct.LEVEL_ID=3 "
				+ " and ct.detail_month_plan_id = :monthPlanId ");
				
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("workItemId", new LongType());
		query.addScalar("constructionId", new LongType());
		query.setParameter("monthPlanId", monthPlanId);
		
		query.setResultTransformer(Transformers.aliasToBean(ConstructionTaskDTO.class));
		
		return query.list();
	}
	
	public void updateStatusCons(Long consId) {
//		taotq start 01072022
		Boolean check = false;
		String sql1 = "select distinct w.WO_TYPE_ID woTypeId, w.CD_LEVEL_1 cdLevel1 from CONSTRUCTION c LEFT JOIN WO w ON c.CONSTRUCTION_ID = w.CONSTRUCTION_ID where c.CAT_CONSTRUCTION_TYPE_ID =8 AND c.CONSTRUCTION_ID = :constructionId ";
		SQLQuery query1 = getSession().createSQLQuery(sql1);
		query1.setParameter("constructionId", consId);
		query1.setResultTransformer(Transformers.aliasToBean(WoDTO.class));
		query1.addScalar("woTypeId", new LongType());
		query1.addScalar("cdLevel1", new StringType());
		List<WoDTO> lst = query1.list();
		if(lst.size()>0) {
			if(lst.get(0).getWoTypeId() == 1 && lst.get(0).getCdLevel1().equals("242656")) {
				check = true;
			}
		}
		
		String sql = "";
		if(check) {
			sql = "update construction set status= -5, COMPLETE_DATE=sysdate where construction_id=:consId";
		}else {
			sql = "update construction set status= 5, COMPLETE_DATE=sysdate where construction_id=:consId";
		}
//		taotq end 01072022
//		StringBuilder sql = new StringBuilder("update construction set status=5, COMPLETE_DATE=sysdate where construction_id=:consId");
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("consId", consId);
		query.executeUpdate();
	}
	//huy-end
	public void updateStatusConsProcess(Long consId) {
		StringBuilder sql = new StringBuilder("update construction set status=3 where construction_id=:consId");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("consId", consId);
		query.executeUpdate();
	}
//	hoanm1_20200514_start
	 public Double avgStatus(Long constructionId) {
	        String sql = new String("select nvl(round(AVG(b.status),2),0) status "
	                + " from construction a, work_item b where a.CONSTRUCTION_ID=b.CONSTRUCTION_ID and a.status !=0  "
	                + " and a.construction_id = :constructionId ");
	        SQLQuery query = getSession().createSQLQuery(sql);
	        query.addScalar("status", new DoubleType());
	        query.setParameter("constructionId", constructionId);
	        List<Double> lstDoub = query.list();
	        if (lstDoub != null && lstDoub.size() > 0) {
	            return lstDoub.get(0);
	        }
	        return 0D;
	    }
//	 hoanm1_20200514_end
	}