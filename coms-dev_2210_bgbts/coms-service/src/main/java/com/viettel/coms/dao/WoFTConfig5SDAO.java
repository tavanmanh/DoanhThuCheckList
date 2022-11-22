package com.viettel.coms.dao;


import com.viettel.coms.bo.WoFTConfig5SBO;
import com.viettel.coms.bo.WoScheduleConfigBO;
import com.viettel.coms.bo.WoScheduleWorkItemBO;
import com.viettel.coms.dto.*;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
@Transactional
public class WoFTConfig5SDAO extends BaseFWDAOImpl<WoFTConfig5SBO, Long> {

    public WoFTConfig5SDAO() {
        this.model = new WoFTConfig5SBO();
    }

    public WoFTConfig5SDAO(Session session) {
        this.session = session;
    }

    public List<WoFTConfig5SDTO> doSearch(WoFTConfig5SDTO dto) {
        StringBuilder sql = new StringBuilder("select "
                + "ID as ftConfigId, CODE as ftConfigCode, NAME as ftConfigName, USER_CREATED as userCreated, USER_CREATED_NAME as userNameCreated, TO_CHAR(CREATED_DATE,'dd/MM/yyyy') as createdDateString,  STATUS as status, "
                + "CD_LEVEL_2 as cdLevel2, CD_LEVEL_2_NAME as cdLevel2Name, FT_ID AS ftId, FT_NAME AS ftName "
                + "from WO_FT_CONFIG_5S WHERE STATUS>0 ");
        if (StringUtils.isNotEmpty(dto.getFtConfigName())) {
            sql.append(" AND LOWER(NAME) LIKE :name ");
        }

        if (StringUtils.isNotEmpty(dto.getCdLevel2())) sql.append(" and CD_LEVEL_2 = :cdLevel2 ");
//        if (StringUtils.isNotEmpty(woFTConfig5SDTO.getFtConfigCode()) ) {
//            sql.append(" AND LOWER(CODE) LIKE :code ");
//        }

        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        sql.append("  ORDER BY ID DESC ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());


        if (StringUtils.isNotEmpty(dto.getFtConfigName())) {
            query.setParameter("name", "%" + dto.getFtConfigName().toLowerCase() + "%");
            queryCount.setParameter("name", "%" + dto.getFtConfigName().toLowerCase() + "%");
        }

        if (StringUtils.isNotEmpty(dto.getCdLevel2())) {
            query.setParameter("cdLevel2", dto.getCdLevel2());
            queryCount.setParameter("cdLevel2", dto.getCdLevel2());
        }

//        if(StringUtils.isNotEmpty(woFTConfig5SDTO.getFtConfigCode())){
//            query.setParameter("code", "%"+woFTConfig5SDTO.getFtConfigCode().toLowerCase()+"%");
//            queryCount.setParameter("code", "%"+woFTConfig5SDTO.getFtConfigCode().toLowerCase()+"%");
//        }

        query = mapFieldsSearch(query);

        if (dto.getPage() != null && dto.getPageSize() != null) {
            query.setFirstResult((dto.getPage().intValue() - 1) * dto.getPageSize().intValue());
            query.setMaxResults(dto.getPageSize().intValue());
        }

        dto.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

        return query.list();
    }

    private SQLQuery mapFieldsSearch(SQLQuery query) {
        query.addScalar("ftConfigId", new LongType());
        query.addScalar("ftConfigCode", new StringType());
        query.addScalar("ftConfigName", new StringType());
        query.addScalar("userCreated", new StringType());
        query.addScalar("userNameCreated", new StringType());
        query.addScalar("createdDateString", new StringType());
        query.addScalar("status", new IntegerType());
        query.addScalar("cdLevel2", new StringType());
        query.addScalar("cdLevel2Name", new StringType());
        query.addScalar("ftId", new LongType());
        query.addScalar("ftName", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(WoFTConfig5SDTO.class));
        return query;
    }

    private SQLQuery mapFields(SQLQuery query) {
        query.addScalar("ftConfigId", new LongType());
        query.addScalar("ftConfigCode", new StringType());
        query.addScalar("ftConfigName", new StringType());
        query.addScalar("userCreated", new StringType());
        query.addScalar("userNameCreated", new StringType());
        query.addScalar("createdDate", new DateType());
        query.addScalar("status", new IntegerType());
        query.addScalar("cdLevel2", new StringType());
        query.addScalar("cdLevel2Name", new StringType());
        query.addScalar("ftId", new LongType());
        query.addScalar("ftName", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(WoFTConfig5SDTO.class));
        return query;
    }


    public WoFTConfig5SDTO getOneWO5sConfig(Long ftConfigId) {
        StringBuilder sql = new StringBuilder("select "
                + "ID as ftConfigId, CODE as ftConfigCode, NAME as ftConfigName, USER_CREATED as userCreated, USER_CREATED_NAME as userNameCreated, CREATED_DATE as createdDate,  STATUS as status, "
                + "CD_LEVEL_2 as cdLevel2, CD_LEVEL_2_NAME as cdLevel2Name, FT_ID AS ftId, FT_NAME AS ftName "
                + "from WO_FT_CONFIG_5S WHERE STATUS>0 AND ID = :paramId");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("paramId", ftConfigId);

        query = mapFields(query);

        return (WoFTConfig5SDTO) query.uniqueResult();
    }

    public boolean checkExistConfigCode(String code) {
        StringBuilder sql = new StringBuilder("select count(*) as total from WO_FT_CONFIG_5S where STATUS>0 and LOWER(CODE) = :code ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("code", code.toLowerCase());
        query.addScalar("total", new LongType());
        long total = (Long) query.uniqueResult();

        if (total > 0) return false;
        else return true;
    }

    public boolean checkIsCNKTExisted(String cdLv2) {
        StringBuilder sql = new StringBuilder("select count(*) as total from WO_FT_CONFIG_5S where STATUS>0 and CD_LEVEL_2 = :cdLv2 ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("cdLv2", cdLv2);
        query.addScalar("total", new LongType());
        long total = (Long) query.uniqueResult();

        if (total > 0) return true;
        else return false;
    }

    public boolean checkExistConfigCodeId(String code, Long id) {
        StringBuilder sql = new StringBuilder("select count(*) as total from WO_FT_CONFIG_5S where STATUS>0 and LOWER(CODE) = :code and ID = :id ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("code", code.toLowerCase());
        query.setParameter("id", id);
        query.addScalar("total", new LongType());
        long total = (Long) query.uniqueResult();

        if (total > 0) return true;
        else return false;
    }

    public WoFTConfig5SBO getOneRaw(Long id) {
        return this.get(WoFTConfig5SBO.class, id);
    }

    public int deleteWO5sConfig(Long ftConfigId) {
        StringBuilder sql = new StringBuilder("UPDATE WO_FT_CONFIG_5S set status = 0  where ID = :ftConfigId");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("ftConfigId", ftConfigId);
        return query.executeUpdate();
    }

    public List<WoFTConfig5SDTO> doSearchReportWo5s(WoDTO woDTO) {
        StringBuilder sql = new StringBuilder("select "
                + "wo.CD_LEVEL_2_NAME as cdLevel2Name , wo.FT_NAME as ftName , "
                + "count(*) AS totalRecord5s, "
                + "COUNT(CASE WHEN wo.STATE = 'OK' AND trunc(wo.END_TIME) <= trunc(wo.FINISH_DATE) THEN 1 END) AS countDone, "
                + "COUNT(CASE WHEN wo.STATE = ('OK') AND trunc(wo.END_TIME) > trunc(wo.FINISH_DATE) THEN 1 END) AS countDoneOver, "
                + "COUNT(CASE WHEN wo.STATE NOT IN ('OK') AND trunc(sysdate) <= trunc(wo.FINISH_DATE) THEN 1 END) AS countNotDone, "
                + "COUNT(CASE WHEN wo.STATE NOT IN ('OK') AND trunc(sysdate) > trunc(wo.FINISH_DATE) THEN 1 END) AS countNotDoneOver "
                + "from WO wo inner join wo_type wt on wo.wo_type_id = wt.id and wt.wo_type_code = '5S' " +
                "  WHERE wo.status > 0 ");
        if (StringUtils.isNotEmpty(woDTO.getCdLevel2())) {
            sql.append(" AND wo.CD_LEVEL_2 = :cdLevel2Id ");
        }

        if (StringUtils.isNotEmpty(woDTO.getStartTimeStr())) {
            sql.append(" AND wo.CREATED_DATE >= TO_DATE(:startTime, 'dd/mm/yyyy')");
        }

        if (StringUtils.isNotEmpty(woDTO.getEndTimeStr())) {
            sql.append(" AND wo.CREATED_DATE <= TO_DATE(:endTime, 'dd/mm/yyyy')");
        }
        sql.append("  GROUP BY wo.CD_LEVEL_2_NAME, wo.FT_NAME ");

        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());


        if (StringUtils.isNotEmpty(woDTO.getCdLevel2())) {
            query.setParameter("cdLevel2Id", woDTO.getCdLevel2());
            queryCount.setParameter("cdLevel2Id", woDTO.getCdLevel2());
        }

        if (StringUtils.isNotEmpty(woDTO.getStartTimeStr())) {
            query.setParameter("startTime", woDTO.getStartTimeStr());
            queryCount.setParameter("startTime", woDTO.getStartTimeStr());
        }

        if (StringUtils.isNotEmpty(woDTO.getEndTimeStr())) {
            query.setParameter("endTime", woDTO.getEndTimeStr());
            queryCount.setParameter("endTime", woDTO.getEndTimeStr());
        }

        query = mapFieldsReport5s(query);

        if (woDTO.getPage() != null && woDTO.getPageSize() != null) {
            query.setFirstResult((woDTO.getPage().intValue() - 1) * woDTO.getPageSize().intValue());
            query.setMaxResults(woDTO.getPageSize().intValue());
        }

        woDTO.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

        return query.list();
    }

    public List<Report5sDTO> doSearchReportWo5sApproved(WoDTO woDTO) {
        String sql = "select\n" +
                "    CD_LEVEL_2 department\n" +
                "    , CD_LEVEL_2_NAME departmentName\n" +
                "    , NAME consTypeName\n" +
                "    , count(*) totalWo\n" +
                "    , sum(EXECUTE_IN_DEADLINE) executeInDeadline\n" +
                "    , sum(APPROVED_IN_DEADLINE) approvedInDeadline\n" +
                "    , ROUND(sum(EXECUTE_IN_DEADLINE) / count(*) * 100, 2) executeInDeadlineRatio\n" +
                "    , ROUND(sum(APPROVED_IN_DEADLINE) / count(*) * 100, 2) approvedInDeadlineRatio\n" +
                "from (\n" +
                "    select\n" +
                "        w.CD_LEVEL_2\n" +
                "        , w.CD_LEVEL_2_NAME\n" +
                "        , w.CONSTRUCTION_ID\n" +
                "        , const.NAME\n" +
                "        , w.FINISH_DATE\n" +
                "        , w.END_TIME\n" +
                "        , w.UPDATE_CD_APPROVE_WO\n" +
                "        , case when w.END_TIME <= w.FINISH_DATE then 1 else 0 end EXECUTE_IN_DEADLINE\n" +
                "        , case when w.UPDATE_CD_APPROVE_WO <= w.FINISH_DATE then 1 else 0 end APPROVED_IN_DEADLINE\n" +
                "    from wo w\n" +
                "    left join wo_type wt on w.wo_type_id = wt.id\n" +
                "    left join construction cons on w.CONSTRUCTION_ID = cons.CONSTRUCTION_ID\n" +
                "    left join CAT_CONSTRUCTION_TYPE const on cons.CAT_CONSTRUCTION_TYPE_ID = const.CAT_CONSTRUCTION_TYPE_ID\n" +
                "    where wt.WO_TYPE_CODE = '5S'\n";
        if (StringUtils.isNotEmpty(woDTO.getCdLevel2())) {
            sql += "    and w.cd_level_2 = :cdLevel2Id\n";
        }
        if (StringUtils.isNotEmpty(woDTO.getStartTimeStr())) {
            sql += "    and trunc(w.CREATED_DATE) >= trunc(TO_DATE(:startTime, 'dd/mm/yyyy'))\n";
        }
        if (StringUtils.isNotEmpty(woDTO.getEndTimeStr())) {
            sql += "    and trunc(w.CREATED_DATE) <= trunc(TO_DATE(:endTime, 'dd/mm/yyyy'))\n";
        }
        sql += "    and w.CD_LEVEL_2 is not null\n" +
                ")\n" +
                "group by CD_LEVEL_2, CD_LEVEL_2_NAME, NAME ";

        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql);
        sqlCount.append(")");
        SQLQuery query = getSession().createSQLQuery(sql);
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        if (StringUtils.isNotEmpty(woDTO.getCdLevel2())) {
            query.setParameter("cdLevel2Id", woDTO.getCdLevel2());
            queryCount.setParameter("cdLevel2Id", woDTO.getCdLevel2());
        }

        if (StringUtils.isNotEmpty(woDTO.getStartTimeStr())) {
            query.setParameter("startTime", woDTO.getStartTimeStr());
            queryCount.setParameter("startTime", woDTO.getStartTimeStr());
        }

        if (StringUtils.isNotEmpty(woDTO.getEndTimeStr())) {
            query.setParameter("endTime", woDTO.getEndTimeStr());
            queryCount.setParameter("endTime", woDTO.getEndTimeStr());
        }

        query.addScalar("department", new LongType());
        query.addScalar("departmentName", new StringType());
        query.addScalar("consTypeName", new StringType());
        query.addScalar("totalWo", new LongType());
        query.addScalar("executeInDeadline", new LongType());
        query.addScalar("approvedInDeadline", new LongType());
        query.addScalar("executeInDeadlineRatio", new DoubleType());
        query.addScalar("approvedInDeadlineRatio", new DoubleType());
        query.setResultTransformer(Transformers.aliasToBean(Report5sDTO.class));

        if (woDTO.getPage() != null && woDTO.getPageSize() != null) {
            query.setFirstResult((woDTO.getPage().intValue() - 1) * woDTO.getPageSize().intValue());
            query.setMaxResults(woDTO.getPageSize().intValue());
        }

        woDTO.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

        return query.list();
    }

    public List<Report5sDTO> doSearchReportWo5sDetail(WoDTO woDTO) {
        String sql = "select\n" +
                "    w.wo_code woCode\n" +
                "    , w.CD_LEVEL_2_NAME departmentName\n" +
                "    , const.name consTypeName\n" +
                "    , to_char(w.CREATED_DATE, 'dd/MM/yyyy') createdDateStr\n" +
                "    , to_char(w.UPDATE_CD_LEVEL2_RECEIVE_WO, 'dd/MM/yyyy') receiveDateStr\n" +
                "    , w.FT_NAME ftName\n" +
                "    , to_char(w.UPDATE_FT_RECEIVE_WO, 'dd/MM/yyyy') updateFtReceiveWoStr\n" +
                "    , w.USER_CD_APPROVE_WO userCdApproveWo\n" +
                "    , to_char(w.UPDATE_CD_APPROVE_WO, 'dd/MM/yyyy') updateCdApproveWoStr\n" +
                "    , to_char(w.END_TIME, 'dd/MM/yyyy') endTime\n" +
                "    , to_char(w.FINISH_DATE, 'dd/MM/yyyy') finishDate\n" +
                "    , w.state woState\n" +
                "from wo w\n" +
                "left join wo_type wt on w.wo_type_id = wt.id\n" +
                "left join construction cons on w.CONSTRUCTION_ID = cons.CONSTRUCTION_ID\n" +
                "left join CAT_CONSTRUCTION_TYPE const on cons.CAT_CONSTRUCTION_TYPE_ID = const.CAT_CONSTRUCTION_TYPE_ID\n" +
                "where wt.WO_TYPE_CODE = '5S'\n";
        if (StringUtils.isNotEmpty(woDTO.getCdLevel2())) {
            sql += "and w.cd_level_2 = :cdLevel2Id\n";
        }
        if (StringUtils.isNotEmpty(woDTO.getStartTimeStr())) {
            sql += "and trunc(w.CREATED_DATE) >= trunc(TO_DATE(:startTime, 'dd/mm/yyyy'))\n";
        }
        if (StringUtils.isNotEmpty(woDTO.getEndTimeStr())) {
            sql += "and trunc(w.CREATED_DATE) <= trunc(TO_DATE(:endTime, 'dd/mm/yyyy'))\n";
        }
        sql += "and w.CD_LEVEL_2 is not null";

        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql);
        sqlCount.append(")");
        SQLQuery query = getSession().createSQLQuery(sql);
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        if (StringUtils.isNotEmpty(woDTO.getCdLevel2())) {
            query.setParameter("cdLevel2Id", woDTO.getCdLevel2());
            queryCount.setParameter("cdLevel2Id", woDTO.getCdLevel2());
        }

        if (StringUtils.isNotEmpty(woDTO.getStartTimeStr())) {
            query.setParameter("startTime", woDTO.getStartTimeStr());
            queryCount.setParameter("startTime", woDTO.getStartTimeStr());
        }

        if (StringUtils.isNotEmpty(woDTO.getEndTimeStr())) {
            query.setParameter("endTime", woDTO.getEndTimeStr());
            queryCount.setParameter("endTime", woDTO.getEndTimeStr());
        }

        query.addScalar("woCode", new StringType());
        query.addScalar("departmentName", new StringType());
        query.addScalar("consTypeName", new StringType());
        query.addScalar("createdDateStr", new StringType());
        query.addScalar("receiveDateStr", new StringType());
        query.addScalar("ftName", new StringType());
        query.addScalar("updateFtReceiveWoStr", new StringType());
        query.addScalar("userCdApproveWo", new StringType());
        query.addScalar("updateCdApproveWoStr", new StringType());
        query.addScalar("woState", new StringType());
        query.addScalar("endTime", new StringType());
        query.addScalar("finishDate", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(Report5sDTO.class));

        if (woDTO.getPage() != null && woDTO.getPageSize() != null) {
            query.setFirstResult((woDTO.getPage().intValue() - 1) * woDTO.getPageSize().intValue());
            query.setMaxResults(woDTO.getPageSize().intValue());
        }

        woDTO.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

        return query.list();
    }

    private SQLQuery mapFieldsReport5s(SQLQuery query) {
        query.addScalar("cdLevel2Name", new StringType());
        query.addScalar("ftName", new StringType());
        query.addScalar("totalRecord5s", new LongType());
        query.addScalar("countDone", new LongType());
        query.addScalar("countDoneOver", new LongType());
        query.addScalar("countNotDone", new LongType());
        query.addScalar("countNotDoneOver", new LongType());
        query.setResultTransformer(Transformers.aliasToBean(Report5sDTO.class));
        return query;
    }

}
