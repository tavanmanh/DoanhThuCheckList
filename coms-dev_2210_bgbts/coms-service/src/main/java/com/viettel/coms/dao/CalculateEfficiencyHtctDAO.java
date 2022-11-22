package com.viettel.coms.dao;

import java.util.List;
import com.viettel.coms.bo.CalculateEfficiencyHtctBO;
import com.viettel.coms.dto.CalculateEfficiencyHtctDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.*;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;


@Repository("calculateEfficiencyHtctDAO")
public class CalculateEfficiencyHtctDAO extends BaseFWDAOImpl<CalculateEfficiencyHtctBO, Long> {

    public CalculateEfficiencyHtctDAO() {
        this.model = new CalculateEfficiencyHtctBO();
    }

    public CalculateEfficiencyHtctDAO(Session session) {
        this.session = session;
    }	
    
    @SuppressWarnings("unchecked")
	public List<CalculateEfficiencyHtctDTO> getData9(CalculateEfficiencyHtctDTO criteria) {
    	StringBuilder sql = new StringBuilder("select  ");
		sql.append(" T1.CALCULATE_EFFICIENCY_ID calculateEfficiencyId ");
		sql.append(",T1.CONTENT_CAL_EFF contentCalEff ");
		sql.append(",T1.UNIT unit ");
		sql.append(",T1.COST_CAL_EFF costCalEff ");
		sql.append(",T1.COST_NOT_SOURCE costNotSource ");
		sql.append(",T1.COST_SOURCE costSource ");
		sql.append(",T1.CREATED_DATE createdDate ");
		sql.append(",T1.CREATED_USER_ID createdUserId ");
		sql.append(",T1.UPDATE_USER_ID updateUserId ");
		sql.append(",T1.UPDATED_DATE updatedDate ");
		sql.append(" FROM CALCULATE_EFFICIENCY_HTCT T1 ORDER BY T1.UPDATED_DATE DESC ");

		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
			query.addScalar("calculateEfficiencyId", new LongType());
		
			query.addScalar("contentCalEff", new StringType());
		
			query.addScalar("unit", new StringType());
		
			query.addScalar("costCalEff", new DoubleType());
		
			query.addScalar("costNotSource", new DoubleType());
		
			query.addScalar("costSource", new DoubleType());
		
			query.addScalar("createdDate", new DateType());
		
			query.addScalar("createdUserId", new LongType());
		
			query.addScalar("updateUserId", new LongType());
		
			query.addScalar("updatedDate", new DateType());

		query.setResultTransformer(Transformers.aliasToBean(CalculateEfficiencyHtctDTO.class));    	
		if (criteria.getPage() != null && criteria.getPageSize() != null) {
			query.setFirstResult((criteria.getPage().intValue() - 1) * criteria.getPageSize().intValue());
			query.setMaxResults(criteria.getPageSize().intValue());
		}
		criteria.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
		return query.list();
	}

	public List<String> lstContent() {
		StringBuilder sql = new StringBuilder("select NAME name from APP_PARAM WHERE PAR_TYPE = 'TTHQ' AND STATUS = 1");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("name", new StringType());
		return query.list();
	}  
}
