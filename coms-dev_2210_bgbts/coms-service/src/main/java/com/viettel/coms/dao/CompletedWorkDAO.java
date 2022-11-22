package com.viettel.coms.dao;

import com.viettel.coms.bo.WorkItemBO;
import com.viettel.coms.dto.ConstructionTaskDailyDTO;
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
import java.util.ArrayList;
import java.util.List;

@Repository("completedWorkDAO")
public class CompletedWorkDAO extends BaseFWDAOImpl<WorkItemBO, Long> {

    public CompletedWorkDAO() {
        this.model = new WorkItemBO();
    }

    public CompletedWorkDAO(Session session) {
        this.session = session;
    }

    public List<WorkItemDetailDTO> doSearchWork(WorkItemDetailDTO obj) {
        StringBuilder stringBuilder = buildQueryWorkCompleted();

        if (StringUtils.isNotEmpty(obj.getKeySearch())) {
            stringBuilder
                    .append(" and (upper(cs.CODE) like upper(:keySearch) or upper(cst.CODE) like upper(:keySearch))");
        }
        if (obj.getSysGroupId() != null) {
            stringBuilder.append(" AND sg.SYS_GROUP_ID = :sysGroupId");
        }

        if (obj.getSysUserId() != null) {
            stringBuilder.append(" AND su.SYS_USER_ID = :sysUserId");
        }

        if (obj.getDateFrom() != null) {
            stringBuilder.append(" and trunc(wi.END_DATE_KTDB) >= :startDateFrom");
        }
        if (obj.getDateTo() != null) {
            stringBuilder.append(" and trunc(wi.END_DATE_KTDB) <= :startDateTo");
        }

        if (obj.getWorkItemId() != null) {
            stringBuilder.append(" and wi.WORK_ITEM_ID = :workItemId");
        }

        if (obj.getConfirmLst() != null && obj.getConfirmLst().size() > 0) {
            stringBuilder.append(" and wi.STATE_KTDB in :confirmLst");
        }

        if (obj.getCatProvinceId() != null) {
            stringBuilder.append(" AND cp.CAT_PROVINCE_ID = :catProvinceId ");
        }

        stringBuilder.append(" ORDER BY cst.construction_id desc");
        SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
        SQLQuery queryCount = getSession().createSQLQuery("SELECT COUNT(*) FROM (" + stringBuilder.toString() + ")");

        if (obj.getWorkItemId() != null) {
            query.setParameter("workItemId", obj.getWorkItemId());
            queryCount.setParameter("workItemId", obj.getWorkItemId());
        }

        if (StringUtils.isNotEmpty(obj.getKeySearch())) {
            query.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
            queryCount.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
        }
        if (obj.getSysGroupId() != null) {
            query.setParameter("sysGroupId", obj.getSysGroupId());
            queryCount.setParameter("sysGroupId", obj.getSysGroupId());
        }

        if (obj.getSysUserId() != null) {
            query.setParameter("sysUserId", obj.getSysUserId());
            queryCount.setParameter("sysUserId", obj.getSysUserId());
        }
        if (obj.getDateFrom() != null) {
            query.setParameter("startDateFrom", obj.getDateFrom());
            queryCount.setParameter("startDateFrom", obj.getDateFrom());
        }
        if (obj.getDateTo() != null) {
            query.setParameter("startDateTo", obj.getDateTo());
            queryCount.setParameter("startDateTo", obj.getDateTo());
        }
        if (obj.getConfirmLst() != null && obj.getConfirmLst().size() > 0) {
            query.setParameterList("confirmLst", obj.getConfirmLst());
            queryCount.setParameterList("confirmLst", obj.getConfirmLst());
        }

        if (obj.getCatProvinceId() != null) {
            query.setParameter("catProvinceId", obj.getCatProvinceId());
            queryCount.setParameter("catProvinceId", obj.getCatProvinceId());
        }

        query.addScalar("endDateKTDB", new DateType());
        query.addScalar("sysGroupName", new StringType());
        query.addScalar("catStationCode", new StringType());
        query.addScalar("constructionCode", new StringType());
        query.addScalar("userName", new StringType());
        query.addScalar("constructionId", new LongType());
        query.addScalar("stateKTDB", new LongType());

        query.setResultTransformer(Transformers.aliasToBean(WorkItemDetailDTO.class));
        if (obj.getPage() != null && obj.getPageSize() != null) {
            query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
            query.setMaxResults(obj.getPageSize().intValue());
        }
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
        return query.list();
    }

    private StringBuilder buildQueryWorkCompleted() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Select distinct wi.END_DATE_KTDB endDateKTDB, sg.name sysGroupName , cs.code catStationCode, cst.code constructionCode"
                + " ,su.full_name userName, cs.CAT_PROVINCE_ID catProvinceId"
                + " ,cst.construction_id constructionId, wi.STATE_KTDB stateKTDB"
                + " FROM construction cst"
                + " inner join work_item wi on cst.construction_id = wi.construction_id"
                + " left join CAT_STATION cs on cst.CAT_STATION_ID = cs.CAT_STATION_ID"
                + " left join SYS_USER su on wi.USER_UPDATE_KTDB = su.SYS_USER_ID"
                + " left join SYS_GROUP sg on cst.SYS_GROUP_ID = sg.SYS_GROUP_ID"
                + " left JOIN CAT_PROVINCE cp on cp.CAT_PROVINCE_ID = cs.CAT_PROVINCE_ID"
                + " where wi.END_DATE_KTDB is not null ");
        return stringBuilder;
    }

    public void updateCompletedWork(WorkItemDetailDTO obj) {
        Long constructionId = Long.parseLong(obj.getConstructionId().toString().trim());
        StringBuilder sql = new StringBuilder(
                "UPDATE WORK_ITEM SET STATE_KTDB = 1 WHERE CONSTRUCTION_ID = :constructionId");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("constructionId", constructionId);
        query.executeUpdate();
    }

    public void rejectCompletedWork(WorkItemDetailDTO obj) {
        Long constructionId = Long.parseLong(obj.getConstructionId().toString().trim());
        StringBuilder sql = new StringBuilder(
                "UPDATE WORK_ITEM SET STATE_KTDB = 2 WHERE CONSTRUCTION_ID = :constructionId");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("constructionId", constructionId);
        query.executeUpdate();
    }

}
