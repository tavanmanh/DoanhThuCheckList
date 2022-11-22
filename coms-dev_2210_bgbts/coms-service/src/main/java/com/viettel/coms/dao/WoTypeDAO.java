package com.viettel.coms.dao;

import com.viettel.coms.bo.WoTrBO;
import com.viettel.coms.bo.WoTypeBO;
import com.viettel.coms.dto.WoTrTypeDTO;
import com.viettel.coms.dto.WoTypeDTO;
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
import java.util.List;

@Repository
@Transactional
public class WoTypeDAO extends BaseFWDAOImpl<WoTypeBO, Long> {
    public WoTypeDAO(){this.model = new WoTypeBO();}
    public WoTypeDAO(Session session) {
        this.session = session;
    }

    public boolean checkExist(Long woTypeId){
        WoTypeBO bo = getOneRaw(woTypeId);
        if(bo == null) return false;
        if(bo.getStatus() == 0) return false;
        return true;
    }

    public boolean checkExistWoTypeCode(String code){
        StringBuilder sql = new StringBuilder("select count(*) as total from WO_TYPE where STATUS>0 and LOWER(WO_TYPE_CODE) = :code ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("code", code.toLowerCase());
        query.addScalar("total", new LongType());
        Long total = (long) query.uniqueResult();

        if(total > 0) return false;
        else return true;
    }

    public boolean checkDeleteEditable(Long woTypeId){
        WoTypeBO bo = getOneRaw(woTypeId);
        if(bo == null) return false;

        StringBuilder sql = new StringBuilder("select count(*) as total from WO where STATUS>0 and WO_TYPE_ID = :woTypeId ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("woTypeId", woTypeId);
        query.addScalar("total", new LongType());
        Long total = (long) query.uniqueResult();

        if(total > 0) return false;
        else return true;
    }

    public void delete(Long woTypeId) {
        StringBuilder sql = new StringBuilder("UPDATE WO_TYPE set status = 0  where ID = :woTypeId");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("woTypeId", woTypeId);
        query.executeUpdate();
    }

    public WoTypeBO getOneRaw(long woTypeId){
        return this.get(WoTypeBO.class, woTypeId);
    }

    public List<WoTypeDTO> getByRange(long pageNumber, long pageSize){
        StringBuilder sql = new StringBuilder("select "
                + "ID as woTypeId, WO_TYPE_CODE as woTypeCode, WO_TYPE_NAME as woTypeName, STATUS as status, "
                + " HAS_AP_WORK_SRC as hasApWorkSrc, HAS_CONSTRUCTION as hasConstruction, HAS_WORK_ITEM as hasWorkItem "
                + "from WO_TYPE WHERE STATUS>0 offset :offset rows fetch next :pageSize rows only");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("pageSize", pageSize);
        query.setParameter("offset", pageSize*(pageNumber -1));

        query = mapFields(query);
        query.setResultTransformer(Transformers.aliasToBean(WoTypeDTO.class));

        return query.list();
    }

    public List<WoTypeDTO> doSearch(WoTypeDTO dto){
        StringBuilder sql = new StringBuilder(" select "
                + " ID as woTypeId, WO_TYPE_CODE as woTypeCode, WO_TYPE_NAME as woTypeName, STATUS as status, "
                + " HAS_AP_WORK_SRC as hasApWorkSrc, HAS_CONSTRUCTION as hasConstruction, HAS_WORK_ITEM as hasWorkItem "
                + " from WO_TYPE WHERE STATUS>0 ");

        if (StringUtils.isNotEmpty(dto.getWoTypeCode())) {
            sql.append(" AND LOWER(WO_TYPE_CODE) LIKE :woTypeCode ");
        }

        if (StringUtils.isNotEmpty(dto.getWoTypeName())) {
            sql.append(" AND LOWER(WO_TYPE_NAME) LIKE :woTypename ");
        }

        if(dto.getIdRange() != null && dto.getIdRange().size()>0){
            sql.append(" and ID in (:idRange) ");
        }

        sql.append(" ORDER BY WO_TYPE_NAME ");

        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());


        if (StringUtils.isNotEmpty(dto.getWoTypeCode())) {
            query.setParameter("woTypeCode", toSearchStr(dto.getWoTypeCode()));
            queryCount.setParameter("woTypeCode", toSearchStr(dto.getWoTypeCode()));
        }

        if (StringUtils.isNotEmpty(dto.getWoTypeName())) {
            query.setParameter("woTypeName", toSearchStr(dto.getWoTypeName()));
            queryCount.setParameter("woTypeName", toSearchStr(dto.getWoTypeName()));
        }

        if(dto.getIdRange() != null && dto.getIdRange().size()>0){
            query.setParameterList("idRange", dto.getIdRange());
            queryCount.setParameterList("idRange", dto.getIdRange());
        }

        if (dto.getPage() != null && dto.getPageSize() != null) {
            query.setFirstResult((dto.getPage().intValue() - 1) * dto.getPageSize().intValue());
            query.setMaxResults(dto.getPageSize().intValue());
        }

        dto.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

        query = mapFields(query);
        query.setResultTransformer(Transformers.aliasToBean(WoTypeDTO.class));

        return query.list();
    }

    private String toSearchStr(String str){
        return "%"+str.toLowerCase()+"%";
    }

    private SQLQuery mapFields(SQLQuery query){
        query.addScalar("woTypeId", new LongType());
        query.addScalar("woTypeCode", new StringType());
        query.addScalar("woTypeName", new StringType());
        query.addScalar("status", new IntegerType());
        query.addScalar("hasApWorkSrc", new LongType());
        query.addScalar("hasConstruction", new LongType());
        query.addScalar("hasWorkItem", new LongType());

        return query;
    }

    public List<WoTypeDTO> getListWoType(){
        StringBuilder sql = new StringBuilder("select "
                + "ID as woTypeId, WO_TYPE_CODE as woTypeCode, WO_TYPE_NAME as woTypeName, STATUS as status, "
                + " HAS_AP_WORK_SRC as hasApWorkSrc, HAS_CONSTRUCTION as hasConstruction, HAS_WORK_ITEM as hasWorkItem "
                + "from WO_TYPE WHERE STATUS>0 ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());

        query = mapFields(query);
        query.setResultTransformer(Transformers.aliasToBean(WoTypeDTO.class));

        return query.list();
    }

    public Long getIdByCode(String code){
        String sql = "Select id from WO_TYPE where WO_TYPE_CODE = :code and status>0 fetch next 1 row only ";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("code", code);
        query.addScalar("id", new LongType());
        return (Long) query.uniqueResult();
    }

}
