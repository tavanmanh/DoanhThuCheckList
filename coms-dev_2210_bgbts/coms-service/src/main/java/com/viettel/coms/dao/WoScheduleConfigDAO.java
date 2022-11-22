package com.viettel.coms.dao;

import com.viettel.coms.bo.WoScheduleConfigBO;
import com.viettel.coms.bo.WoScheduleWorkItemBO;
import com.viettel.coms.dto.WoScheduleConfigDTO;
import com.viettel.coms.dto.WoScheduleWorkItemDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public class WoScheduleConfigDAO extends BaseFWDAOImpl<WoScheduleConfigBO, Long> {
    public WoScheduleConfigDAO() {
        this.model = new WoScheduleConfigBO();
    }

    public WoScheduleConfigDAO(Session session) {
        this.session = session;
    }

    public List<WoScheduleConfigDTO> doSearch(WoScheduleConfigDTO woScheduleConfigDTO) {
        StringBuilder sql = new StringBuilder("select "
                + "ID as scheduleConfigId, CODE as scheduleConfigCode, NAME as scheduleConfigName, USER_CREATED as userCreated, CREATED_DATE as createdDate,  STATUS as status, "
                + "TO_CHAR(START_TIME,'dd/MM/yyyy') as startTimeString,TO_CHAR(END_TIME,'dd/MM/yyyy')  as endTimeString, CYCLE_LENGTH as cycleLength, STATE as state, CYCLE_TYPE as cycleType, WO_SCHEDULE_WORK_ITEM_ID as scheduleWorkItemId, "
                + "QUOTA_TIME as quotaTime, CD_LEVEL_1 as cdLevel1, WO_TR_ID as trId, WO_TIME as woTime, CD_LEVEL_2 as cdLevel2, CD_LEVEL_1_NAME as cdLevel1Name, CD_LEVEL_2_NAME as cdLevel2Name" +
                ", WO_TR_CODE as woTRCode, WO_TYPE_ID woTypeId, WO_NAME_ID woNameId "
                + "from WO_SCHEDULE_CONFIG WHERE STATUS>0 ");
        if (StringUtils.isNotEmpty(woScheduleConfigDTO.getScheduleConfigName())) {
            sql.append(" AND LOWER(NAME) LIKE :name ");
        }
        if (StringUtils.isNotEmpty(woScheduleConfigDTO.getScheduleConfigCode())) {
            sql.append(" AND LOWER(CODE) LIKE :code ");
        }
        if(StringUtils.isNotEmpty(woScheduleConfigDTO.getStartDateFrom()) && !StringUtils.isNotEmpty(woScheduleConfigDTO.getStartDateTo())) {
				sql.append(" AND START_TIME >= TO_DATE(:startTimeFrom, 'dd/mm/yyyy')");
        }
        if(!StringUtils.isNotEmpty(woScheduleConfigDTO.getStartDateFrom()) && StringUtils.isNotEmpty(woScheduleConfigDTO.getStartDateTo())) {
				sql.append(" AND START_TIME <=  TO_DATE(:startTimeTo, 'dd/mm/yyyy')");
        }
        if(StringUtils.isNotEmpty(woScheduleConfigDTO.getStartDateFrom()) && StringUtils.isNotEmpty(woScheduleConfigDTO.getStartDateTo())) {
				sql.append(" AND  TO_DATE(:startTimeFrom, 'dd/mm/yyyy') <= START_TIME AND START_TIME <= TO_DATE(:startTimeTo, 'dd/mm/yyyy')");
        }
        if(StringUtils.isNotEmpty(woScheduleConfigDTO.getEndDateFrom()) && !StringUtils.isNotEmpty(woScheduleConfigDTO.getEndDateTo())) {
			sql.append(" AND END_TIME >= TO_DATE(:endTimeFrom, 'dd/mm/yyyy')");
	    }
	    if(!StringUtils.isNotEmpty(woScheduleConfigDTO.getEndDateFrom()) && StringUtils.isNotEmpty(woScheduleConfigDTO.getEndDateTo())) {
				sql.append(" AND END_TIME <=  TO_DATE(:endTimeTo, 'dd/mm/yyyy')");
	    }
	    if(StringUtils.isNotEmpty(woScheduleConfigDTO.getEndDateFrom()) && StringUtils.isNotEmpty(woScheduleConfigDTO.getEndDateTo())) {
				sql.append(" AND  TO_DATE(:endTimeFrom, 'dd/mm/yyyy') <= END_TIME AND END_TIME <= TO_DATE(:endTimeTo, 'dd/mm/yyyy')");
	    }
	    if(StringUtils.isNotEmpty(woScheduleConfigDTO.getCdLevel2Name())) {
	    	sql.append(" AND CD_LEVEL_2_NAME = :cdLevel2Name ");
	    }
	    if(StringUtils.isNotEmpty(woScheduleConfigDTO.getStatusS())) {
	    	sql.append(" AND STATUS = :statusS ");
	    }
	    if(woScheduleConfigDTO.getCycleType() != null) {
	    	sql.append(" AND CYCLE_TYPE = :cycleType ");
	    }

        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        sql.append("  ORDER BY ID DESC ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());


        if (StringUtils.isNotEmpty(woScheduleConfigDTO.getScheduleConfigName())) {
            query.setParameter("name", "%" + woScheduleConfigDTO.getScheduleConfigName().toLowerCase() + "%");
            queryCount.setParameter("name", "%" + woScheduleConfigDTO.getScheduleConfigName().toLowerCase() + "%");
        }
        if (StringUtils.isNotEmpty(woScheduleConfigDTO.getScheduleConfigCode())) {
            query.setParameter("code", "%" + woScheduleConfigDTO.getScheduleConfigCode().toLowerCase() + "%");
            queryCount.setParameter("code", "%" + woScheduleConfigDTO.getScheduleConfigCode().toLowerCase() + "%");
        }
        if(StringUtils.isNotEmpty(woScheduleConfigDTO.getStartDateFrom())) {
				query.setParameter("startTimeFrom", woScheduleConfigDTO.getStartDateFrom());
	            queryCount.setParameter("startTimeFrom", woScheduleConfigDTO.getStartDateFrom());
        }
        if(StringUtils.isNotEmpty(woScheduleConfigDTO.getStartDateTo())) {
				query.setParameter("startTimeTo", woScheduleConfigDTO.getStartDateTo());
	            queryCount.setParameter("startTimeTo", woScheduleConfigDTO.getStartDateTo());
        }
        if(StringUtils.isNotEmpty(woScheduleConfigDTO.getEndDateFrom())) {
			query.setParameter("endTimeFrom", woScheduleConfigDTO.getEndDateFrom());
            queryCount.setParameter("endTimeFrom", woScheduleConfigDTO.getEndDateFrom());
	    }
	    if(StringUtils.isNotEmpty(woScheduleConfigDTO.getEndDateTo())) {
				query.setParameter("endTimeTo", woScheduleConfigDTO.getEndDateTo());
	            queryCount.setParameter("endTimeTo", woScheduleConfigDTO.getEndDateTo());
	    }
	    if(StringUtils.isNotEmpty(woScheduleConfigDTO.getCdLevel2Name())) {
	    	
	    	query.setParameter("cdLevel2Name", woScheduleConfigDTO.getCdLevel2Name());
            queryCount.setParameter("cdLevel2Name", woScheduleConfigDTO.getCdLevel2Name());
	    }
	    if(StringUtils.isNotEmpty(woScheduleConfigDTO.getStatusS())) {
	    	
	    	query.setParameter("statusS", Integer.parseInt(woScheduleConfigDTO.getStatusS()));
            queryCount.setParameter("statusS", Integer.parseInt(woScheduleConfigDTO.getStatusS()));
	    }
	    
	    if(woScheduleConfigDTO.getCycleType() != null) {
	    	query.setParameter("cycleType", woScheduleConfigDTO.getCycleType());
            queryCount.setParameter("cycleType", woScheduleConfigDTO.getCycleType());
	    }
        if(StringUtils.isNotEmpty(woScheduleConfigDTO.getScheduleConfigCode())){
            query.setParameter("code", "%"+woScheduleConfigDTO.getScheduleConfigCode().toLowerCase()+"%");
            queryCount.setParameter("code", "%"+woScheduleConfigDTO.getScheduleConfigCode().toLowerCase()+"%");
        }

        query = mapFields(query);

        if (woScheduleConfigDTO.getPage() != null && woScheduleConfigDTO.getPageSize() != null) {
            query.setFirstResult((woScheduleConfigDTO.getPage().intValue() - 1) * woScheduleConfigDTO.getPageSize().intValue());
            query.setMaxResults(woScheduleConfigDTO.getPageSize().intValue());
        }

        woScheduleConfigDTO.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

        return query.list();
    }

    private SQLQuery mapFields(SQLQuery query) {
        query.addScalar("scheduleConfigId", new LongType());
        query.addScalar("scheduleConfigCode", new StringType());
        query.addScalar("scheduleConfigName", new StringType());
        query.addScalar("userCreated", new StringType());
        query.addScalar("createdDate", new DateType());
        query.addScalar("status", new IntegerType());
        query.addScalar("startTimeString", new StringType());
        query.addScalar("endTimeString", new StringType());
        query.addScalar("cycleLength", new LongType());
        query.addScalar("state", new LongType());
        query.addScalar("cycleType", new LongType());
        query.addScalar("scheduleWorkItemId", new LongType());
        query.addScalar("quotaTime", new LongType());
        query.addScalar("cdLevel1", new StringType());
        query.addScalar("trId", new LongType());
        query.addScalar("woTime", new DateType());
        query.addScalar("cdLevel2", new StringType());
        query.addScalar("cdLevel1Name", new StringType());
        query.addScalar("cdLevel2Name", new StringType());
        query.addScalar("woTRCode", new StringType());
        query.addScalar("woTypeId", new LongType());
        query.addScalar("woNameId", new LongType());
        query.setResultTransformer(Transformers.aliasToBean(WoScheduleConfigDTO.class));
        return query;
    }

    public boolean checkExistScheduleConfigCode(String code) {
        StringBuilder sql = new StringBuilder("select count(*) as total from WO_SCHEDULE_CONFIG where STATUS>0 and LOWER(CODE) = :code ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("code", code.toLowerCase());
        query.addScalar("total", new LongType());
        long total = (Long) query.uniqueResult();

        if (total > 0) return false;
        else return true;
    }

    private SQLQuery mapFieldsOneWI(SQLQuery query) {
        query.addScalar("scheduleConfigId", new LongType());
        query.addScalar("scheduleConfigCode", new StringType());
        query.addScalar("scheduleConfigName", new StringType());
        query.addScalar("userCreated", new StringType());
        query.addScalar("createdDate", new DateType());
        query.addScalar("status", new IntegerType());
        query.addScalar("startTime", new DateType());
        query.addScalar("endTime", new DateType());
        query.addScalar("cycleLength", new LongType());
        query.addScalar("state", new LongType());
        query.addScalar("cycleType", new LongType());
        query.addScalar("scheduleWorkItemId", new LongType());
        query.addScalar("quotaTime", new LongType());
        query.addScalar("cdLevel1", new StringType());
        query.addScalar("trId", new LongType());
        query.addScalar("woTime", new DateType());
        query.addScalar("cdLevel2", new StringType());
        query.addScalar("cdLevel1Name", new StringType());
        query.addScalar("cdLevel2Name", new StringType());
        query.addScalar("woTRCode", new StringType());
        query.addScalar("woTypeId", new LongType());
        query.addScalar("woNameId", new LongType());
        query.setResultTransformer(Transformers.aliasToBean(WoScheduleConfigDTO.class));
        return query;
    }

    public WoScheduleConfigDTO getOneWIConfig(Long scheduleConfigId) {
        StringBuilder sql = new StringBuilder("select "
                + "ID as scheduleConfigId, CODE as scheduleConfigCode, NAME as scheduleConfigName, USER_CREATED as userCreated, CREATED_DATE as createdDate,  STATUS as status, "
                + "START_TIME as startTime, END_TIME as endTime, CYCLE_LENGTH as cycleLength, STATE as state, CYCLE_TYPE as cycleType, WO_SCHEDULE_WORK_ITEM_ID as scheduleWorkItemId, "
                + "QUOTA_TIME as quotaTime, CD_LEVEL_1 as cdLevel1, WO_TR_ID as trId, WO_TIME as woTime, CD_LEVEL_2 as cdLevel2 , WO_TR_CODE as woTRCode, CD_LEVEL_1_NAME AS cdLevel1Name" +
                " , CD_LEVEL_2_NAME AS cdLevel2Name, WO_TYPE_ID woTypeId, WO_NAME_ID woNameId  "
                + "from WO_SCHEDULE_CONFIG WHERE STATUS>0 AND ID = :paramId");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("paramId", scheduleConfigId);

        query = mapFieldsOneWI(query);

        return (WoScheduleConfigDTO) query.uniqueResult();
    }

    public WoScheduleConfigBO getOneRaw(Long id) {
        return this.get(WoScheduleConfigBO.class, id);
    }

    public int deleteWIConfig(Long scheduleConfigId) {
        StringBuilder sql = new StringBuilder("UPDATE WO_SCHEDULE_CONFIG set status = 0  where ID = :scheduleWorkItemId");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("scheduleWorkItemId", scheduleConfigId);
        return query.executeUpdate();
    }

    public boolean checkDeletable(Long id) {
        StringBuilder sql = new StringBuilder("select count(*) as total from WO where CAT_WORK_ITEM_TYPE_ID = :id and WO_TYPE_ID= 202 ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("id", id);
        query.addScalar("total", new LongType());
        long total = (Long) query.uniqueResult();

        if (total > 0) return false;
        else return true;
    }
}
