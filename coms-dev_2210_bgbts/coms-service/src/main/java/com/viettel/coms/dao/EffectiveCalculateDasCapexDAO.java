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
import com.viettel.coms.bo.EffectiveCalculateDasCapexBO;
import com.viettel.coms.dto.EffectiveCalculateDasCapexDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@EnableTransactionManagement
@Repository("effectiveCalculateDasCapexDAO")

public class EffectiveCalculateDasCapexDAO extends BaseFWDAOImpl<EffectiveCalculateDasCapexBO, Long> {
	
	public EffectiveCalculateDasCapexDAO() {
		this.model = new EffectiveCalculateDasCapexBO();
	}

	public EffectiveCalculateDasCapexDAO(Session session) {
		this.session = session;
	}
  
	public List<EffectiveCalculateDasCapexDTO> getAssumptionsCapex(EffectiveCalculateDasCapexDTO obj) {
		StringBuilder stringBuilder = new StringBuilder("select ");
		stringBuilder.append("A.ASSUMPTIONS_CAPEX_ID assumptionsCapexId ");
		stringBuilder.append(",A.ITEM_TYPE itemType ");
		stringBuilder.append(",A.ITEM item ");
		stringBuilder.append(",A.NOTE note ");
		stringBuilder.append(",A.COST cost ");
		stringBuilder.append(",A.UNIT unit ");
		stringBuilder.append(",A.MASS mass ");
		stringBuilder.append(",A.SSUMPTIONS_ID ssumptionsId ");
		stringBuilder.append(",A.CREATED_DATE createdDate ");
		stringBuilder.append(",A.CREATED_USER_ID createdUserId ");
		stringBuilder.append(",A.UPDATED_DATE updatedDate ");
		stringBuilder.append(",A.UPDATED_USER_ID updatedUserId ");	     
       	stringBuilder.append("FROM ASSUMPTIONS_CAPEX A where 1 = 1 ");    	
   		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
   		sqlCount.append(stringBuilder.toString());
   		sqlCount.append(")");
   		SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
   		SQLQuery queryCount=getSession().createSQLQuery(sqlCount.toString());
   		query.addScalar("assumptionsCapexId", new LongType());
		query.addScalar("itemType", new StringType());
		query.addScalar("note", new StringType());
		query.addScalar("item", new StringType());
		query.addScalar("cost", new DoubleType());
		query.addScalar("unit", new StringType());
		query.addScalar("mass", new DoubleType());
		query.addScalar("ssumptionsId", new LongType());	
		query.addScalar("createdDate", new DateType());
		query.addScalar("createdUserId", new LongType());
		query.addScalar("updatedDate", new DateType());
		query.addScalar("updatedUserId", new LongType());		
   	    query.setResultTransformer(Transformers.aliasToBean(EffectiveCalculateDasCapexDTO.class));   
   	    if(obj.getPage() != null && obj.getPageSize() != null) {
   	    	query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
            query.setMaxResults(obj.getPageSize().intValue());
   	    }
   	    obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
   		List ls = query.list();
   		return ls;
	}
	
	public List<EffectiveCalculateDasCapexDTO> getItem (EffectiveCalculateDasCapexDTO obj){
		StringBuilder stringBuilder = new StringBuilder("select ");
		stringBuilder.append("AP.NAME name ");
		stringBuilder.append("FROM APP_PARAM AP WHERE AP.PAR_TYPE = 'Gia_dinh_capex' AND AP.STATUS = 1 ");
		SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
		query.addScalar("name", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(EffectiveCalculateDasCapexDTO.class));
		List ls = query.list();
		return ls;
		
	}


}
