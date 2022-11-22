package com.viettel.coms.dao;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.viettel.coms.bo.TmpnTargetOSBO;
import com.viettel.coms.dto.ReportPlanOSDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@Repository("tmpnTargetOSDAO")
public class TmpnTargetOSDAO extends BaseFWDAOImpl<TmpnTargetOSBO, Long>{


    public TmpnTargetOSDAO() {
        this.model = new TmpnTargetOSBO();
    }

    public TmpnTargetOSDAO(Session session) {
        this.session = session;
    }

    public void deleteTarget(List<Long> deleteList) {
        // TODO Auto-generated method stub
        if (deleteList != null) {
            StringBuilder sql = new StringBuilder("Delete TMPN_TARGET_OS where TMPN_TARGET_OS_ID in :deleteList");
            SQLQuery query = getSession().createSQLQuery(sql.toString());
            query.setParameterList("deleteList", deleteList);
            query.executeUpdate();
        }

    }

    public List<Long> getTargetInDB(Long totalMonthPlanId) {
        StringBuilder sql = new StringBuilder(
                "SELECT TMPN_TARGET_OS_ID tmpnTargetId from TMPN_TARGET_OS where TOTAL_MONTH_PLAN_OS_ID = :totalMonthPlanId");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("totalMonthPlanId", totalMonthPlanId);
        query.addScalar("tmpnTargetId", new LongType());
        List<Long> val = query.list();
        return val;
    }

    public List<ReportPlanOSDTO> reportProgress(ReportPlanOSDTO obj) {
    	StringBuilder sql = new StringBuilder("SELECT B.YEAR ,B.sysGroupName ,B.quantity ,NVL(B.currentQuantity,0) currentQuantity ,"
    			  + " ROUND(DECODE(B.quantity,0,0,100* B.currentQuantity/(B.quantity/1000000)),2)progressQuantity,"
    			  + " B.complete ,NVL(B.currentComplete,0) currentComplete,"
    			  + " ROUND(DECODE(B.COMPLETE,0,0,100* B.CURRENTCOMPLETE/(B.COMPLETE/1000000)),2)progressComplete,"
    			  + " B.revenue , b.month, NVL(B.currentRevenue,0) currentRevenue,"
    			  + " ROUND(DECODE(B.revenue,0,0,100* B.currentRevenue/(B.revenue/1000000)),2)progressRevenue "
    			  + " FROM "
    			  + " (SELECT a.MONTH MONTH, a.YEAR YEAR,(SELECT name FROM sys_group SYS WHERE sys.sys_group_id=a.sys_group_id)sysGroupName,"
    			  + " SUM(a.QUANTITY)quantity,"
    			  + " (select sum(quantity) from rp_quantity rp where rp.sys_group_id =a.sys_group_id and to_char(starting_date,'MM/yyyy')= (CASE WHEN a.MONTH <10 THEN 0 ||a.Month ELSE TO_CHAR(a.Month) "
    			  + " END ) ||'/'||a.YEAR )currentQuantity,"
    			  + " SUM(a.COMPLETE)complete,"
    			  + " (select sum(completeValue/1000000) from rp_hshc rp where rp.sysGroupId =a.sys_group_id and to_char(dateComplete,'MM/yyyy')= (CASE WHEN a.MONTH <10 THEN 0 ||a.Month ELSE TO_CHAR(a.Month) "
    			  + " END ) ||'/'||a.YEAR )currentComplete, SUM(a.REVENUE)revenue,"
    			  + " (select sum(decode(consAppRevenueValueDB,0,completeValue,consAppRevenueValueDB)/1000000) from rp_revenue rp where rp.sysGroupId =a.sys_group_id and to_char(dateComplete,'MM/yyyy')= (CASE WHEN a.MONTH <10 THEN 0 ||a.Month ELSE TO_CHAR(a.Month) "
    			  + " END ) ||'/'||a.YEAR )currentRevenue FROM tmpn_target_os a ,TOTAL_MONTH_PLAN_OS b WHERE a.TOTAL_MONTH_PLAN_OS_ID=b.TOTAL_MONTH_PLAN_OS_ID  AND b.STATUS =1");
        if (obj.getYear() != null && obj.getMonth() != null) {
            sql.append(" and a.month = :month and a.year = :year");
        }
        if (obj.getSysGroupId() != null) {
            sql.append(" AND a.SYS_GROUP_ID =:sysGroupId ");
        }
        if (obj.getListYear() != null && obj.getListYear().size() > 0) {
            sql.append(" AND a.YEAR in :listYear ");
        }
        sql.append(" group by a.MONTH,a.YEAR,a.SYS_GROUP_ID ) b");
        sql.append("  ");

        if (obj.getProgress() != null && !obj.getProgress().isEmpty()) {
            sql.append(
                    "  where (case when (progr0essQuantity > 100 and progressComplete >100 and progressRevenue >100 ) then 1 else 0 end)=:progress ");
        }
        sql.append("  order by month,sysGroupName ");
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        StringBuilder sqlQuerySum = new StringBuilder("SELECT sum(quantity)/1000000 quantityTotal, sum(currentQuantity)currentQuantityTotal,"
        		+ " round(decode(sum(quantity),0,0,100* sum(currentQuantity)/sum(quantity/1000000)),2)progressQuantityTotal,"
        		+ " sum(complete)/1000000 completeTotal,sum(currentComplete)currentCompleteTotal,"
        		+ " round(decode(sum(COMPLETE),0,0,100* sum(CURRENTCOMPLETE)/sum(COMPLETE/1000000)),2)progressCompleteTotal,"
        		+ " sum(revenue)/1000000 revenueTotal,sum(currentRevenue)currentRevenueTotal,"
        		+ " round(decode(sum(revenue),0,0,100* sum(currentRevenue)/sum(revenue/1000000)),2)progressRevenueTotal "
        		+ "  FROM (");
        sqlQuerySum.append(sql);
        sqlQuerySum.append(")");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
        SQLQuery querySum = getSession().createSQLQuery(sqlQuerySum.toString());

        if (obj.getSysGroupId() != null) {
            query.setParameter("sysGroupId", obj.getSysGroupId());
            queryCount.setParameter("sysGroupId", obj.getSysGroupId());
            querySum.setParameter("sysGroupId", obj.getSysGroupId());
        }
        if (obj.getListYear() != null && obj.getListYear().size() > 0) {
            query.setParameterList("listYear", obj.getListYear());
            queryCount.setParameterList("listYear", obj.getListYear());
            querySum.setParameterList("listYear", obj.getListYear());
        }
        if (obj.getMonth() != null) {
            query.setParameter("month", obj.getMonth());
            queryCount.setParameter("month", obj.getMonth());
            querySum.setParameter("month", obj.getMonth());
        }
        if (obj.getYear() != null) {
            query.setParameter("year", obj.getYear());
            queryCount.setParameter("year", obj.getYear());
            querySum.setParameter("year", obj.getYear());
        }

        if (obj.getListTotalMonthPlanId() != null && obj.getListTotalMonthPlanId().size() > 0) {
            query.setParameterList("listTotalMonthPlanId", obj.getListTotalMonthPlanId());
            queryCount.setParameterList("listTotalMonthPlanId", obj.getListTotalMonthPlanId());
            querySum.setParameterList("listTotalMonthPlanId", obj.getListTotalMonthPlanId());
        }

        if (obj.getProgress() != null) {
            query.setParameter("progress", obj.getProgress());
            queryCount.setParameter("progress", obj.getProgress());
            querySum.setParameter("progress", obj.getProgress());
        }

        query.addScalar("revenue", new DoubleType());
        query.addScalar("quantity", new DoubleType());
        query.addScalar("complete", new DoubleType());
        query.addScalar("currentComplete", new DoubleType());
        query.addScalar("progressComplete", new DoubleType());
        query.addScalar("currentRevenue", new DoubleType());
        query.addScalar("progressRevenue", new DoubleType());
        query.addScalar("progressQuantity", new DoubleType());
        query.addScalar("currentQuantity", new DoubleType());
        query.addScalar("sysGroupName", new StringType());
        query.addScalar("year", new LongType());
        query.addScalar("month", new LongType());
        
        querySum.addScalar("quantityTotal", new DoubleType());
        querySum.addScalar("currentQuantityTotal", new DoubleType());
        querySum.addScalar("progressQuantityTotal", new DoubleType());
        querySum.addScalar("completeTotal", new DoubleType());
        querySum.addScalar("currentCompleteTotal", new DoubleType());
        querySum.addScalar("progressCompleteTotal", new DoubleType());
        querySum.addScalar("revenueTotal", new DoubleType());
        querySum.addScalar("currentRevenueTotal", new DoubleType());
        querySum.addScalar("progressRevenueTotal", new DoubleType());
        query.setResultTransformer(Transformers.aliasToBean(ReportPlanOSDTO.class));
        if (obj.getPage() != null && obj.getPageSize() != null) {
            query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
            query.setMaxResults(obj.getPageSize().intValue());
        }

        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
        List<ReportPlanOSDTO> lst = query.list();

        if (lst.size() > 0) {
        	List<Object[]> rs = querySum.list();
        	for (Object[] objects : rs) {
        		lst.get(0).setQuantityTotal((Double) objects[0]);
        		lst.get(0).setCurrentQuantityTotal((Double) objects[1]);
            	lst.get(0).setProgressQuantityTotal((Double) objects[2]);
            	lst.get(0).setCompleteTotal((Double) objects[3]);
            	lst.get(0).setCurrentCompleteTotal((Double) objects[4]);
            	lst.get(0).setProgressCompleteTotal((Double) objects[5]);
            	lst.get(0).setRevenueTotal((Double) objects[6]);
            	lst.get(0).setCurrentRevenueTotal((Double) objects[7]);
            	lst.get(0).setProgressRevenueTotal((Double) objects[8]);
        	}
        }
        return lst;
    }

    public String getSysGroupNameByUserName(String userName) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT sys.NAME sysName");
        sql.append(" FROM SYS_USER u ");
        sql.append(" LEFT JOIN SYS_GROUP sys ");
        sql.append(" ON u.SYS_GROUP_ID  = sys.SYS_GROUP_ID ");
        sql.append(" WHERE u.LOGIN_NAME =:userName ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.addScalar("sysName", new StringType());
        query.setParameter("userName", userName);
        return (String) query.uniqueResult();
    }

}
