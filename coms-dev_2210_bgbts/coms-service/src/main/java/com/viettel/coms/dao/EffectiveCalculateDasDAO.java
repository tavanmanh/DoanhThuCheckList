package com.viettel.coms.dao;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import com.viettel.coms.bo.EffectiveCalculateDasBO;
import com.viettel.coms.dto.EffectiveCalculateDasDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@EnableTransactionManagement
@Repository("effectiveCalculateDasDAO")
public class EffectiveCalculateDasDAO extends BaseFWDAOImpl<EffectiveCalculateDasBO, Long> {

  public EffectiveCalculateDasDAO() {
    this.model = new EffectiveCalculateDasBO();
  }

  public EffectiveCalculateDasDAO(Session session) {
    this.session = session;
  }

  public List<EffectiveCalculateDasDTO> getAssumptions(EffectiveCalculateDasDTO obj) {
    StringBuilder stringBuilder = new StringBuilder("select ");
    stringBuilder.append("A.ASSUMPTIONS_ID assumptionsId ");
    stringBuilder.append(",A.CONTENT_ASSUMPTIONS contentAssumptions ");
    stringBuilder.append(",A.UNIT unit ");
    stringBuilder.append(",A.COST_ASSUMPTIONS costAssumptions ");
    stringBuilder.append(",A.NOTE_ASSUMPTIONS noteAssumptions ");
    stringBuilder.append(",A.CREATED_DATE createdDate ");
    stringBuilder.append(",A.CREATED_USER_ID createdUserId ");
    stringBuilder.append(",A.UPDATED_DATE updatedDate ");
    stringBuilder.append(",A.UPDATED_USER_ID updatedUserId ");
    stringBuilder.append("FROM ASSUMPTIONS A where 1 = 1 ");
    StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
    sqlCount.append(stringBuilder.toString());
    sqlCount.append(")");
    SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
    SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
    query.addScalar("assumptionsId", new LongType());
    query.addScalar("contentAssumptions", new StringType());
    query.addScalar("unit", new StringType());
    query.addScalar("costAssumptions", new DoubleType());
    query.addScalar("noteAssumptions", new StringType());
    query.addScalar("createdDate", new DateType());
    query.addScalar("createdUserId", new LongType());
    query.addScalar("updatedDate", new DateType());
    query.addScalar("updatedUserId", new LongType());
    query.setResultTransformer(Transformers.aliasToBean(EffectiveCalculateDasDTO.class));
    if (obj.getPage() != null && obj.getPageSize() != null) {
      query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
      query.setMaxResults(obj.getPageSize().intValue());
    }
    obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
    List ls = query.list();
    return ls;
  }

  public List<EffectiveCalculateDasDTO> getContent(EffectiveCalculateDasDTO obj) {
    StringBuilder stringBuilder = new StringBuilder("select ");
    stringBuilder.append("AP.NAME name ");
    stringBuilder.append("FROM APP_PARAM AP WHERE AP.PAR_TYPE = 'Gia_dinh' AND AP.STATUS = 1 ");
    SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
    query.addScalar("name", new StringType());
    query.setResultTransformer(Transformers.aliasToBean(EffectiveCalculateDasDTO.class));
    List ls = query.list();
    return ls;

  }

}
