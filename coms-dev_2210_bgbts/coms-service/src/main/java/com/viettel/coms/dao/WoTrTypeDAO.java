package com.viettel.coms.dao;

import com.viettel.coms.bo.WoTrTypeBO;
import com.viettel.coms.dto.WoTrDTO;
import com.viettel.coms.dto.WoTrTypeDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
@Transactional
public class WoTrTypeDAO extends BaseFWDAOImpl<WoTrTypeBO, Long> {
    public WoTrTypeDAO(){this.model = new WoTrTypeBO();}
    public WoTrTypeDAO(Session session) {
        this.session = session;
    }

    public boolean checkDeletable(long id){
        StringBuilder sql = new StringBuilder("select count(*) as total from WO_TR where STATUS>0 and TR_TYPE_ID = :id ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("id", id);
        query.addScalar("total", new LongType());
        long total = (Long) query.uniqueResult();

        if(total > 0) return false;
        else return true;
    }

    public boolean checkExistTrTypeCode(String code){
        StringBuilder sql = new StringBuilder("select count(*) as total from WO_TR_TYPE where STATUS>0 and LOWER(TR_TYPE_CODE) = :code ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("code", code.toLowerCase());
        query.addScalar("total", new LongType());
        long total = (Long) query.uniqueResult();

        if(total > 0) return false;
        else return true;
    }

    public int delete(Long woTrTypeId) {
        StringBuilder sql = new StringBuilder("UPDATE WO_TR_TYPE set status = 0  where ID = :woTrTypeId");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("woTrTypeId", woTrTypeId);
        return query.executeUpdate();
    }

    public WoTrTypeBO getOneRaw(Long id){
        return this.get(WoTrTypeBO.class, id);
    }

    public List<WoTrTypeDTO> getListTRType() {
        StringBuilder sql = new StringBuilder("select "
                + " ID as woTrTypeId, TR_TYPE_CODE as trTypeCode, TR_TYPE_NAME as trTypeName, STATUS as status "
                + " from WO_TR_TYPE WHERE STATUS>0 ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());

        query = mapFields(query);

        return query.list();
    }

    public List<WoTrTypeDTO> doSearch(WoTrTypeDTO trTypeDTO) {
        StringBuilder sql = new StringBuilder("select "
                + "ID as woTrTypeId, TR_TYPE_CODE as trTypeCode, TR_TYPE_NAME as trTypeName, STATUS as status "
                + "from WO_TR_TYPE WHERE STATUS>0 ");
        if (StringUtils.isNotEmpty(trTypeDTO.getTrTypeName()) ) {
            sql.append(" AND LOWER(TR_TYPE_NAME) LIKE :trTypeName ");
        }
        if (StringUtils.isNotEmpty(trTypeDTO.getTrTypeCode()) ) {
            sql.append(" AND LOWER(TR_TYPE_CODE) LIKE :trTypeCode ");
        }

        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());


        if(StringUtils.isNotEmpty(trTypeDTO.getTrTypeName())){
            query.setParameter("trTypeName", "%"+trTypeDTO.getTrTypeName().toLowerCase()+"%");
            queryCount.setParameter("trTypeName", "%"+trTypeDTO.getTrTypeName().toLowerCase()+"%");
        }
        if(StringUtils.isNotEmpty(trTypeDTO.getTrTypeCode())){
            query.setParameter("trTypeCode", "%"+trTypeDTO.getTrTypeCode().toLowerCase()+"%");
            queryCount.setParameter("trTypeCode", "%"+trTypeDTO.getTrTypeCode().toLowerCase()+"%");
        }

        query = mapFields(query);

        if (trTypeDTO.getPage() != null && trTypeDTO.getPageSize() != null) {
            query.setFirstResult((trTypeDTO.getPage().intValue() - 1) * trTypeDTO.getPageSize().intValue());
            query.setMaxResults(trTypeDTO.getPageSize().intValue());
        }

        trTypeDTO.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

        return query.list();
    }

    public WoTrTypeDTO getOneDetails(long woTrTypeId){
        ///todo
        StringBuilder sql = new StringBuilder("select "
                + "ID as woTrTypeId, TR_TYPE_CODE as trTypeCode, TR_TYPE_NAME as trTypeName, STATUS as status "
                + "from WO_TR_TYPE WHERE STATUS>0 AND ID = :paramId");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("paramId", woTrTypeId);

        query = mapFields(query);

        return (WoTrTypeDTO) query.uniqueResult();
    }

    private SQLQuery mapFields(SQLQuery query){
        query.addScalar("woTrTypeId", new LongType());
        query.addScalar("trTypeCode", new StringType());
        query.addScalar("trTypeName", new StringType());
        query.addScalar("status", new IntegerType());
        query.setResultTransformer(Transformers.aliasToBean(WoTrTypeDTO.class));
        return query;
    }



}
