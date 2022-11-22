package com.viettel.coms.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.viettel.coms.bo.YearPlanOSBO;
import com.viettel.coms.dto.AppParamDTO;
import com.viettel.coms.dto.YearPlanDetailOSDTO;
import com.viettel.coms.dto.YearPlanOSDTO;
import com.viettel.coms.dto.YearPlanSimpleOSDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import com.viettel.wms.utils.ValidateUtils;

/**
 * @author HoangNH38
 */
@Repository("yearPlanOSDAO")
public class YearPlanOSDAO extends BaseFWDAOImpl<YearPlanOSBO, Long> {
	public YearPlanOSDAO() {
        this.model = new YearPlanOSBO();
    }

    public YearPlanOSDAO(Session session) {
        this.session = session;
    }
    
    public List<YearPlanOSDTO> doSearch(YearPlanSimpleOSDTO obj, Long sysGroupId) {
        StringBuilder sql = new StringBuilder("SELECT year_plan_os_id yearPlanId," + "year year,"
                + "CREATED_DATE createdDate," + "Created_user_id createdUserId," + "Created_group_id createdGroupId,"
                + "CODE code," + "NAME name," + "Sign_state signState," + "STATUS status, " + " DESCRIPTION description"
                + " FROM YEAR_PLAN_OS WHERE 1=1 ");
        if (StringUtils.isNotEmpty(obj.getKeySearch())) {
            sql.append(
            		" AND (upper(CODE) LIKE upper(:keySearch) OR  upper(YEAR) LIKE upper(:keySearch) OR upper(NAME) LIKE upper(:keySearch) escape '&')" );
        }

        if (StringUtils.isNotEmpty(obj.getStatus())) {
            sql.append(" AND STATUS = :status");
        }

        if (obj.getSignStateList() != null && !obj.getSignStateList().isEmpty()) {
            sql.append(" AND Sign_state  in :signStateList");
        }
        if (sysGroupId != null) {
            sql.append(" and CREATED_GROUP_ID = :sysGroupId");
        }
        sql.append(" ORDER BY year_plan_os_id DESC");

        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        query.addScalar("yearPlanId", new LongType());
        query.addScalar("year", new LongType());
        query.addScalar("createdDate", new DateType());
        query.addScalar("code", new StringType());
        query.addScalar("name", new StringType());
        query.addScalar("createdUserId", new LongType());
        query.addScalar("createdGroupId", new LongType());
        query.addScalar("status", new StringType());
        query.addScalar("signState", new StringType());
        query.addScalar("description", new StringType());

        query.setResultTransformer(Transformers.aliasToBean(YearPlanOSDTO.class));
        if (StringUtils.isNotEmpty(obj.getKeySearch())) {
            query.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch()) + "%");
            queryCount.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch()) + "%");
        }
        if (StringUtils.isNotEmpty(obj.getStatus()) && !"2".equals(obj.getStatus())) {
            query.setParameter("status", obj.getStatus());
            queryCount.setParameter("status", obj.getStatus());
        }
        if (sysGroupId != null) {
            query.setParameter("sysGroupId", sysGroupId);
            queryCount.setParameter("sysGroupId", sysGroupId);
        }
        if (obj.getSignStateList() != null && !obj.getSignStateList().isEmpty()) {
            query.setParameterList("signStateList", obj.getSignStateList());
            queryCount.setParameterList("signStateList", obj.getSignStateList());
        }
        query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
        query.setMaxResults(obj.getPageSize().intValue());
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

        return query.list();
    }

    public YearPlanSimpleOSDTO getYeaPlanById(Long id) {
        StringBuilder sql = new StringBuilder("SELECT year_plan_os_id yearPlanId," + "year year,"
                + "CREATED_DATE createdDate," + "Created_user_id createdUserId," + "Created_group_id createdGroupId,"
                + "CODE code," + "NAME name," + "Sign_state signState," + "STATUS status, " + " DESCRIPTION description"
                + " FROM YEAR_PLAN_OS WHERE YEAR_PLAN_OS_ID=:id");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.addScalar("yearPlanId", new LongType());
        query.addScalar("year", new LongType());
        query.addScalar("createdDate", new DateType());
        query.addScalar("code", new StringType());
        query.addScalar("name", new StringType());
        query.addScalar("createdUserId", new LongType());
        query.addScalar("createdGroupId", new LongType());
        query.addScalar("status", new StringType());
        query.addScalar("signState", new StringType());
        query.addScalar("description", new StringType());
        query.setParameter("id", id);
        query.setResultTransformer(Transformers.aliasToBean(YearPlanSimpleOSDTO.class));
        return (YearPlanSimpleOSDTO) query.uniqueResult();
    }

    public List<YearPlanDetailOSDTO> getYearPlanDetailByParentId(Long id) {
        StringBuilder sql = new StringBuilder("SELECT ypd.YEAR_PLAN_DETAIL_OS_ID yearPlanDetailId,"
                + "ypd.YEAR_PLAN_OS_ID yearPlanId," + "ypd.SYS_GROUP_ID sysGroupId," + "ypd.MONTH month,"
                + "ypd.YEAR year," + "ypd.SOURCE source," + "ypd.QUANTITY quantity," + "ypd.COMPLETE complete,"
                + "ypd.REVENUE revenue, " + "sg.NAME sysGroupName " + " FROM YEAR_PLAN_DETAIL_OS ypd");
        sql.append(" LEFT JOIN CTCT_CAT_OWNER.SYS_GROUP sg ON ypd.SYS_GROUP_ID =sg.SYS_GROUP_ID  ");
        sql.append("WHERE YEAR_PLAN_OS_ID=:id");
        sql.append(" ORDER BY sg.NAME ASC,ypd.month ASC ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("id", id);
        query.addScalar("yearPlanId", new LongType());
        query.addScalar("yearPlanDetailId", new LongType());
        query.addScalar("year", new LongType());
        query.addScalar("month", new LongType());
        query.addScalar("sysGroupId", new LongType());
        query.addScalar("source", new DoubleType());
        query.addScalar("quantity", new DoubleType());
        query.addScalar("complete", new DoubleType());
        query.addScalar("revenue", new DoubleType());
        query.addScalar("sysGroupName", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(YearPlanDetailOSDTO.class));
        return query.list();
    }

    public List<Long> getYearPlanDetailIdListInDB(Long yearPlanId) {
        StringBuilder sql = new StringBuilder(
                "SELECT YEAR_PLAN_DETAIL_OS_ID yearPlanDetailId from YEAR_PLAN_DETAIL_OS where YEAR_PLAN_OS_ID = :yearPlanId");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("yearPlanId", yearPlanId);
        query.addScalar("yearPlanDetailId", new LongType());
        List<Long> val = query.list();
        return val;
    }

    public void deleteYearPlanDetail(List<Long> deleteList) {
        StringBuilder sql = new StringBuilder("DELETE YEAR_PLAN_DETAIL_OS where YEAR_PLAN_DETAIL_OS_ID in :deleteList");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameterList("deleteList", deleteList);
        query.executeUpdate();
    }

    public Long getSequence() {
        StringBuilder sql = new StringBuilder("Select YEAR_PLAN_SEQ.NEXTVAL FROM DUAL");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        return ((BigDecimal) query.uniqueResult()).longValue();
    }

    public void remove(Long yearPlanId) {
        StringBuilder sql = new StringBuilder("UPDATE YEAR_PLAN_OS set status = 0  where YEAR_PLAN_OS_ID = :yearPlanId");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("yearPlanId", yearPlanId);
        query.executeUpdate();
    }

    public boolean checkYear(Long year, Long yearPlanId) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(YEAR_PLAN_OS_ID) FROM YEAR_PLAN_OS where 1=1 and status = 1  and year=:year");
        if (yearPlanId != null) {
            sql.append(" AND YEAR_PLAN_OS_ID != :yearPlanId ");
        }
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("year", year);
        if (yearPlanId != null) {
            query.setParameter("yearPlanId", yearPlanId);
        }
        return ((BigDecimal) query.uniqueResult()).intValue() > 0;
    }

    public List<YearPlanDetailOSDTO> getDataForSignVOffice(Long yearPlanId) {
        StringBuilder sql = new StringBuilder(
                " WITH parent AS   (SELECT SUM(ypd.source) source,     SUM(ypd.quantity) quantity,     SUM(ypd.COMPLETE) complete, ");
        sql.append(
                " SUM(ypd.REVENUE) revenue,     cast(sg.NAME as VARCHAR(200)) column1Name,     ypd.sys_group_id,     0 as orderSort  ");
        sql.append(
                " FROM year_plan_detail ypd  LEFT JOIN CTCT_CAT_OWNER.SYS_GROUP sg  ON sg.SYS_GROUP_ID = ypd.SYS_GROUP_ID ");
        sql.append(" WHERE 1            =1  and ypd.YEAR_PLAN_OS_ID = :yearPlanId group by ypd.SYS_GROUP_ID,sg.NAME  ), ");
        sql.append(
                " child AS  (SELECT ypd.source source,    ypd.quantity quantity,    ypd.COMPLETE complete,    ypd.REVENUE revenue, ");
        sql.append(
                " cast('Tháng '||ypd.month as VARCHAR(20)) column1Name,    ypd.sys_group_id ,     ypd.month as orderSort  FROM year_plan_detail ypd ");
        sql.append(
                " WHERE 1=1 and ypd.YEAR_PLAN_OS_ID = :yearPlanId   ), allRecord as( select * from  parent union all select * from child) ");
        sql.append(
                " select source source, quantity quantity,complete complete,revenue revenue,column1Name column1Name from allRecord  order by sys_group_id,orderSort  ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("yearPlanId", yearPlanId);
        query.addScalar("source", new DoubleType());
        query.addScalar("quantity", new DoubleType());
        query.addScalar("complete", new DoubleType());
        query.addScalar("revenue", new DoubleType());
        query.addScalar("column1Name", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(YearPlanDetailOSDTO.class));
        return query.list();

    }

    public List<YearPlanSimpleOSDTO> exportYearPlan(YearPlanSimpleOSDTO obj) {
        StringBuilder sql = new StringBuilder("SELECT YEAR_PLAN_OS_ID yearPlanId," + "year year,"
                + "CREATED_DATE createdDate," + "Created_user_id createdUserId," + "Created_group_id createdGroupId,"
                + "CODE code," + "NAME name," + "Sign_state signState," + "STATUS status, " + " DESCRIPTION description"
                + " FROM YEAR_PLAN_OS WHERE 1=1 ");
        if (StringUtils.isNotEmpty(obj.getKeySearch())) {
            sql.append(
                    " AND (upper(CODE) LIKE upper(:keySearch) OR  upper(YEAR) LIKE upper(:keySearch) OR upper(NAME) LIKE upper(:keySearch) escape '&')");
        }

        if (StringUtils.isNotEmpty(obj.getStatus())) {
            sql.append(" AND STATUS = :status");
        }

        if (obj.getSignStateList() != null && !obj.getSignStateList().isEmpty()) {
            sql.append(" AND Sign_state  in :signStateList");
        }

        sql.append(" ORDER BY YEAR_PLAN_OS_ID DESC");

        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        query.addScalar("yearPlanId", new LongType());
        query.addScalar("year", new LongType());
        query.addScalar("createdDate", new DateType());
        query.addScalar("code", new StringType());
        query.addScalar("name", new StringType());
        query.addScalar("createdUserId", new LongType());
        query.addScalar("createdGroupId", new LongType());
        query.addScalar("status", new StringType());
        query.addScalar("signState", new StringType());
        query.addScalar("description", new StringType());

        query.setResultTransformer(Transformers.aliasToBean(YearPlanSimpleOSDTO.class));
        if (StringUtils.isNotEmpty(obj.getKeySearch())) {
            query.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch()) + "%");
            queryCount.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch()) + "%");
        }
        if (StringUtils.isNotEmpty(obj.getStatus()) && !"2".equals(obj.getStatus())) {
            query.setParameter("status", obj.getStatus());
            queryCount.setParameter("status", obj.getStatus());
        }

        if (obj.getSignStateList() != null && !obj.getSignStateList().isEmpty()) {
            query.setParameterList("signStateList", obj.getSignStateList());
            queryCount.setParameterList("signStateList", obj.getSignStateList());
        }
        query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
        query.setMaxResults(obj.getPageSize().intValue());
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

        return query.list();
    }

    public List<AppParamDTO> getAppParamByType(String type) {
        // TODO Auto-generated method stub
        StringBuilder sql = new StringBuilder(
                "select CODE code, NAME name,PAR_ORDER parOrder,PAR_TYPE parType from APP_PARAM where 1=1");
        sql.append(" and  PAR_TYPE= :type and status = 1 order by PAR_ORDER");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("type", type);
        query.addScalar("name", new StringType());
        query.addScalar("code", new StringType());
        query.addScalar("parOrder", new StringType());
        query.addScalar("parType", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(AppParamDTO.class));
        return query.list();
    }
    
    public void updateRegistry(Long yearPlanId) {
        StringBuilder sql = new StringBuilder("UPDATE YEAR_PLAN_OS set SIGN_STATE = '3'  where YEAR_PLAN_OS_ID = :yearPlanId");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("yearPlanId", yearPlanId);
        query.executeUpdate();
    }

}
