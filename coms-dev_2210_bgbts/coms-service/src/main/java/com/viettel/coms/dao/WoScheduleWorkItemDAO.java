package com.viettel.coms.dao;

import com.viettel.coms.bo.WoScheduleWorkItemBO;
import com.viettel.coms.bo.WoTrTypeBO;
import com.viettel.coms.dto.WoDTO;
import com.viettel.coms.dto.WoNameDTO;
import com.viettel.coms.dto.WoScheduleCheckListDTO;
import com.viettel.coms.dto.WoScheduleWorkItemDTO;
import com.viettel.coms.dto.WoTrTypeDTO;
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
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Repository
@Transactional
public class WoScheduleWorkItemDAO extends BaseFWDAOImpl<WoScheduleWorkItemBO, Long> {
    public WoScheduleWorkItemDAO(){this.model = new WoScheduleWorkItemBO();}
    public WoScheduleWorkItemDAO(Session session) {
        this.session = session;
    }

    public List<WoScheduleWorkItemDTO> doSearch(WoScheduleWorkItemDTO woScheduleWorkItemDTO) {
        StringBuilder sql = new StringBuilder("select "
                + "ID as woWorkItemId, CODE as workItemCode, NAME as workItemName, USER_CREATED as userCreated, CREATED_DATE as createdDate,  STATUS as status "
                + "from WO_SCHEDULE_WORK_ITEM WHERE STATUS>0 ");
        if (StringUtils.isNotEmpty(woScheduleWorkItemDTO.getWorkItemName()) ) {
            sql.append(" AND LOWER(NAME) LIKE :name ");
        }
        if (StringUtils.isNotEmpty(woScheduleWorkItemDTO.getWorkItemCode()) ) {
            sql.append(" AND LOWER(CODE) LIKE :code ");
        }

        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        sql.append("  ORDER BY CREATED_DATE DESC ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        if(StringUtils.isNotEmpty(woScheduleWorkItemDTO.getWorkItemName())){
            query.setParameter("name", "%"+woScheduleWorkItemDTO.getWorkItemName().toLowerCase()+"%");
            queryCount.setParameter("name", "%"+woScheduleWorkItemDTO.getWorkItemName().toLowerCase()+"%");
        }
        if(StringUtils.isNotEmpty(woScheduleWorkItemDTO.getWorkItemCode())){
            query.setParameter("code", "%"+woScheduleWorkItemDTO.getWorkItemCode().toLowerCase()+"%");
            queryCount.setParameter("code", "%"+woScheduleWorkItemDTO.getWorkItemCode().toLowerCase()+"%");
        }

        query = mapFields(query);

        if (woScheduleWorkItemDTO.getPage() != null && woScheduleWorkItemDTO.getPageSize() != null) {
            query.setFirstResult((woScheduleWorkItemDTO.getPage().intValue() - 1) * woScheduleWorkItemDTO.getPageSize().intValue());
            query.setMaxResults(woScheduleWorkItemDTO.getPageSize().intValue());
        }

        woScheduleWorkItemDTO.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

        return query.list();
    }

    private SQLQuery mapFields(SQLQuery query){
        query.addScalar("woWorkItemId", new LongType());
        query.addScalar("workItemCode", new StringType());
        query.addScalar("workItemName", new StringType());
        query.addScalar("userCreated", new StringType());
        query.addScalar("createdDate", new DateType());
        query.addScalar("status", new IntegerType());
        query.setResultTransformer(Transformers.aliasToBean(WoScheduleWorkItemDTO.class));
        return query;
    }

    public boolean checkExistWorkItemCode(String code){
        StringBuilder sql = new StringBuilder("select count(*) as total from WO_SCHEDULE_WORK_ITEM where STATUS>0 and LOWER(CODE) = :code ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("code", code.toLowerCase());
        query.addScalar("total", new LongType());
        long total = (Long) query.uniqueResult();

        if(total > 0) return false;
        else return true;
    }

    private SQLQuery mapFieldsDeatil(SQLQuery query){
        query.addScalar("woWorkItemId", new LongType());
        query.addScalar("workItemCode", new StringType());
        query.addScalar("workItemName", new StringType());
        query.addScalar("userCreated", new StringType());
        query.addScalar("createdDate", new DateType());
        query.addScalar("status", new IntegerType());
        query.addScalar("userCreatedFullName", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(WoScheduleWorkItemDTO.class));
        return query;
    }

    public WoScheduleWorkItemDTO getOneInfoWorkItem(Long woWorkItemId){
        StringBuilder sql = new StringBuilder("select "
                + "wi.ID as woWorkItemId, wi.CODE as workItemCode, wi.NAME as workItemName, wi.USER_CREATED as userCreated, wi.CREATED_DATE as createdDate,  wi.STATUS as status, "
                + " su.FULL_NAME as userCreatedFullName "
                + " from WO_SCHEDULE_WORK_ITEM wi "
                + " left join SYS_USER su on su.LOGIN_NAME = wi.USER_CREATED "
                + " WHERE wi.STATUS>0 AND wi.ID = :paramId ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("paramId", woWorkItemId);

        query = mapFieldsDeatil(query);

        return (WoScheduleWorkItemDTO) query.uniqueResult();
    }

    public WoScheduleWorkItemBO getOneRaw(Long id){
        return this.get(WoScheduleWorkItemBO.class, id);
    }

    public boolean checkDeletable(Long id){
        StringBuilder sql = new StringBuilder("select count(*) as total from WO_SCHEDULE_CONFIG where WO_SCHEDULE_WORK_ITEM_ID = :id ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("id", id);
        query.addScalar("total", new LongType());
        long total = (Long) query.uniqueResult();

        if(total > 0) return false;
        else return true;
    }

    public int deleteWorkItem(Long woWorkItemId) {
        StringBuilder sql = new StringBuilder("UPDATE WO_SCHEDULE_WORK_ITEM set status = 0  where ID = :woWorkItemId");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("woWorkItemId", woWorkItemId);
        return query.executeUpdate();
    }

    public List<WoScheduleCheckListDTO> doSearchWICheckList(WoScheduleWorkItemDTO dto) {
        StringBuilder sql = new StringBuilder( " select ID as scheduleCheckListId , CODE as scheduleCheckListCode, ");
        sql.append(" NAME as scheduleCheckListName, USER_CREATED as userCreated, CREATED_DATE as createdDate, STATUS as status, SCHEDULE_WORK_ITEM_ID AS scheduleWorkItemId");//huypq30_add_20201002

        sql.append(" from WO_SCHEDULE_CHECKLIST woSC ");

        sql.append(" WHERE woSC.STATUS>0 ");

//        if (StringUtils.isNotEmpty(dto.getKeySearch())) {
//            sql.append(" AND ( LOWER(woTbl.WO_NAME) LIKE :keySearch OR LOWER(woTbl.WO_CODE) LIKE :keySearch ) ");
//        }

        if (dto.getWoWorkItemId() != null) {
            sql.append(" AND woSC.SCHEDULE_WORK_ITEM_ID = :wiId ");
        }

        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        sql.append("  ORDER BY CREATED_DATE DESC ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        if (!Objects.isNull(dto.getWoWorkItemId())) {
            query.setParameter("wiId", dto.getWoWorkItemId());
            queryCount.setParameter("wiId", dto.getWoWorkItemId());
        }

        query.addScalar("scheduleCheckListId", new LongType());
        query.addScalar("scheduleCheckListCode", new StringType());
        query.addScalar("scheduleCheckListName", new StringType());
        query.addScalar("userCreated", new StringType());
        query.addScalar("createdDate", new DateType());
        query.addScalar("status", new IntegerType());
        query.addScalar("scheduleWorkItemId", new LongType());

        query.setResultTransformer(Transformers.aliasToBean(WoScheduleCheckListDTO.class));

        if (dto.getPage() != null && dto.getPageSize() != null) {
            query.setFirstResult((dto.getPage().intValue() - 1) * dto.getPageSize().intValue());
            query.setMaxResults(dto.getPageSize().intValue());
        }

        dto.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

        return query.list();
    }

    public boolean checkExistWICheckListCode(WoScheduleCheckListDTO dto){
        StringBuilder sql = new StringBuilder("select count(*) as total from WO_SCHEDULE_CHECKLIST where STATUS>0 and LOWER(CODE) = :code and SCHEDULE_WORK_ITEM_ID = :wiID");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("code", dto.getScheduleCheckListCode().toLowerCase());
        query.setParameter("wiID", dto.getScheduleWorkItemId());
        query.addScalar("total", new LongType());
        long total = (Long) query.uniqueResult();

        if(total > 0) return false;
        else return true;
    }

    public WoScheduleCheckListDTO getOneDetailsCheckList(Long scheduleCheckListId){
        StringBuilder sql = new StringBuilder("select "
                + " ID as scheduleCheckListId , CODE as scheduleCheckListCode, NAME as scheduleCheckListName, USER_CREATED as userCreated, CREATED_DATE as createdDate, STATUS as status, SCHEDULE_WORK_ITEM_ID AS scheduleWorkItemId "
                + "from WO_SCHEDULE_CHECKLIST WHERE STATUS>0 AND ID = :paramId");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("paramId", scheduleCheckListId);

        query.addScalar("scheduleCheckListId", new LongType());
        query.addScalar("scheduleCheckListCode", new StringType());
        query.addScalar("scheduleCheckListName", new StringType());
        query.addScalar("userCreated", new StringType());
        query.addScalar("createdDate", new DateType());
        query.addScalar("status", new IntegerType());
        query.addScalar("scheduleWorkItemId", new LongType());

        query.setResultTransformer(Transformers.aliasToBean(WoScheduleCheckListDTO.class));

        return (WoScheduleCheckListDTO) query.uniqueResult();
    }
    public WoScheduleWorkItemDTO getIdInfoWorkItem(String wiScheduleCode){
        StringBuilder sql = new StringBuilder("select "
                + "wi.ID as woWorkItemId, wi.CODE as workItemCode, wi.NAME as workItemName, wi.USER_CREATED as userCreated, wi.CREATED_DATE as createdDate,  wi.STATUS as status "
                + " from WO_SCHEDULE_WORK_ITEM wi "
                + " WHERE wi.STATUS>0 AND wi.CODE = :paramCode ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("paramCode", wiScheduleCode);

        query = mapFields(query);

        return (WoScheduleWorkItemDTO) query.uniqueResult();
    }
    
    //Huypq-23112021-start
    public List<WoNameDTO> getWoNameByWoTypeNLMT() {
        StringBuilder sql = new StringBuilder(" SELECT " + 
        		" 	 woType.ID woTypeId, " +
        		" 	 woName.ID id, " +
        		"    woType.WO_TYPE_NAME woTypeName, " + 
        		"    woName.NAME name " + 
        		"FROM " + 
        		"    CTCT_COMS_OWNER.WO_NAME woName " + 
        		"    INNER JOIN CTCT_COMS_OWNER.WO_TYPE woType ON woName.WO_TYPE_ID = woType.ID " + 
        		"WHERE " + 
        		"    woName.STATUS != 0 " + 
        		"    AND woType.STATUS != 0 " + 
        		"    AND ( woType.WO_TYPE_CODE LIKE '%NLMT%' OR woType.WO_TYPE_CODE LIKE '%BDMPD%' OR woType.WO_TYPE_CODE LIKE '%BDTHTCT%' ) " + //DUONGHV13 ADD 07122021
        		"    ORDER BY " + 
        		"    woType.WO_TYPE_NAME ");
        
        SQLQuery query = getSession().createSQLQuery(sql.toString());

        query.addScalar("woTypeId", new LongType());
        query.addScalar("id", new LongType());
        query.addScalar("woTypeName", new StringType());
        query.addScalar("name", new StringType());

        query.setResultTransformer(Transformers.aliasToBean(WoNameDTO.class));

        return query.list();
    }
    //Huy-end
}
