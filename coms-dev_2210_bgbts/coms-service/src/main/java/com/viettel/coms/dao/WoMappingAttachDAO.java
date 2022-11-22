package com.viettel.coms.dao;

import com.viettel.coms.bo.WoBO;
import com.viettel.coms.bo.WoMappingAttachBO;
import com.viettel.coms.dto.WoMappingAttachDTO;
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

import java.util.List;

@Repository
@Transactional
public class WoMappingAttachDAO extends BaseFWDAOImpl<WoMappingAttachBO, Long> {
    public WoMappingAttachDAO(){this.model = new WoMappingAttachBO();}
    public WoMappingAttachDAO(Session session) {
        this.session = session;
    }

    public void delete(Long woMappingAttachId) {
        StringBuilder sql = new StringBuilder("UPDATE WO_MAPPING_ATTACH set status = 0  where ID = :woMappingAttachId");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("woMappingAttachId", woMappingAttachId);
        query.executeUpdate();
    }

    public WoMappingAttachBO getOneRaw(long woMappingAttachId){
        return this.get(WoMappingAttachBO.class, woMappingAttachId);
    }

    public List<WoMappingAttachDTO> doSearch(WoMappingAttachDTO dto){
        StringBuilder sql = new StringBuilder("select "
                + "ID as id, WO_ID as woId, FILE_NAME as fileName, " +
                " FILE_PATH as filePath, USER_CREATED as userCreated, STATUS as status, " +
                " TR_ID as trId, CHECKLIST_ID as checklistId, CREATED_DATE as createdDate "
                + " from WO_MAPPING_ATTACH WHERE STATUS>0 AND TYPE is null ");

        if(dto.getId()!=null){
            sql.append(" AND ID = :id ");
        }

        if(dto.getWoId()!=null && dto.getTrId()==null){
            sql.append(" AND WO_ID = :woId ");
        }

        if(dto.getTrId()!=null && dto.getWoId()==null){
            sql.append(" AND TR_ID = :trId ");
        }

        if(dto.getWoId()!=null && dto.getTrId()!=null){
            sql.append(" AND (WO_ID = :woId or TR_ID = :trId) ");
        }

        if(dto.getChecklistId()!=null){
            sql.append(" AND CHECKLIST_ID = :checklistId ");
        }

        if (StringUtils.isNotEmpty(dto.getFileName())) {
            sql.append(" AND LOWER(FILE_NAME) LIKE :fileName ");
        }

        if (StringUtils.isNotEmpty(dto.getUserCreated()) ) {
            sql.append(" AND USER_CREATED = :userCreated ");
        }

        sql.append(" offset :offset rows fetch next :pageSize rows only ");

        int pageSize = dto.getPageSize();
        int offset = (dto.getPage().intValue()-1)*pageSize;

        SQLQuery query = getSession().createSQLQuery(sql.toString());

        query.setParameter("pageSize", pageSize);
        query.setParameter("offset", offset);

        if(dto.getId()!=null){
            query.setParameter("id", dto.getId());
        }

        if(dto.getWoId()!=null){
            query.setParameter("woId", dto.getWoId());
        }

        if(dto.getTrId()!=null){
            query.setParameter("trId", dto.getTrId());
        }

        if(dto.getChecklistId()!=null){
            query.setParameter("checklistId", dto.getChecklistId());
        }

        if(StringUtils.isNotEmpty(dto.getFileName())){
            query.setParameter("fileName", "%"+dto.getFileName().toLowerCase()+"%");
        }
        if(StringUtils.isNotEmpty(dto.getUserCreated())){
            query.setParameter("userCreated", dto.getUserCreated());
        }

        query = mapFields(query);

        return query.list();
    }

    private SQLQuery mapFields(SQLQuery query){
        query.addScalar("id", new LongType());
        query.addScalar("woId", new LongType());
        query.addScalar("trId", new LongType());
        query.addScalar("checklistId", new LongType());
        query.addScalar("fileName", new StringType());
        query.addScalar("filePath", new StringType());
        query.addScalar("userCreated", new StringType());
        query.addScalar("status", new LongType());
        query.addScalar("createdDate", new DateType());
        query.setResultTransformer(Transformers.aliasToBean(WoMappingAttachDTO.class));
        return query;
    }
    
    public List<WoMappingAttachDTO> searchFileCheckList(WoMappingAttachDTO dto){
        StringBuilder sql = new StringBuilder("select "
                + "ID as id, WO_ID as woId, FILE_NAME as fileName, " +
                " FILE_PATH as filePath, USER_CREATED as userCreated, STATUS as status, " +
                " TR_ID as trId, CHECKLIST_ID as checklistId, CREATED_DATE as createdDate "
                + " from WO_MAPPING_ATTACH WHERE STATUS>0 AND TYPE = '1' ");


        if(dto.getWoId()!=null && dto.getTrId()==null){
            sql.append(" AND WO_ID = :woId ");
        }

        sql.append(" offset :offset rows fetch next :pageSize rows only ");

        int pageSize = dto.getPageSize();
        int offset = (dto.getPage().intValue()-1)*pageSize;

        SQLQuery query = getSession().createSQLQuery(sql.toString());

        query.setParameter("pageSize", pageSize);
        query.setParameter("offset", offset);

        if(dto.getWoId()!=null){
            query.setParameter("woId", dto.getWoId());
        }

        query = mapFields(query);

        return query.list();
    }
	public void deleteFile(WoMappingAttachDTO dto) {
		String sql = "UPDATE WO_MAPPING_ATTACH SET STATUS = 0 where WO_ID = :woId AND TYPE = '1'";
		SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("woId", dto.getWoId());
        query.executeUpdate();
		// TODO Auto-generated method stub
		
	}
	public void updateWoMapingPlan(Long woId) {
		// TODO Auto-generated method stub
		String sql = "UPDATE WO_MAPPING_PLAN SET STATUS = 0 where WO_ID = :woId AND STATUS = 1";
		SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("woId", woId);
        query.executeUpdate();
	}


	
	public void updateWoPlan(Long woId) {
		// TODO Auto-generated method stub
        String sql = "UPDATE WO_PLAN SET STATUS = 0 where STATUS =1 AND ID in( SELECT WO_PLAN_ID from WO_MAPPING_PLAN WHERE WO_ID = :woId )";
		SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("woId", woId);
        query.executeUpdate();
	}

    public List<WoMappingAttachBO> findByWoIdAndStatus(WoBO bo) {
        String sql = "SELECT * FROM WO_MAPPING_ATTACH wma WHERE wma.WO_ID = :woId AND wma.STATUS = 1";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.addEntity(WoMappingAttachBO.class);
        query.setParameter("woId", bo.getWoId());
        return query.list();
    }


    public void deleteImg(Long woId) {
        String sql = "UPDATE WO_MAPPING_ATTACH SET STATUS = 0 where WO_ID = :woId AND STATUS = 1";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("woId", woId);
        query.executeUpdate();
        // TODO Auto-generated method stub

    }
}
