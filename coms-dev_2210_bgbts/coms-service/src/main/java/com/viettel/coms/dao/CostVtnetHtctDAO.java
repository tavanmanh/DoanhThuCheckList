package com.viettel.coms.dao;

import java.util.List;
import com.viettel.coms.bo.CostVtnetHtctBO;
import com.viettel.coms.dto.CostVtnetHtctDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.*;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;


@Repository("costVtnetHtctDAO")
public class CostVtnetHtctDAO extends BaseFWDAOImpl<CostVtnetHtctBO, Long> {

    public CostVtnetHtctDAO() {
        this.model = new CostVtnetHtctBO();
    }

    public CostVtnetHtctDAO(Session session) {
        this.session = session;
    }	
    
    @SuppressWarnings("unchecked")
	public List<CostVtnetHtctDTO> getData5(CostVtnetHtctDTO criteria) {
    	StringBuilder sql = new StringBuilder("select ");
		sql.append(" T1.COST_VTNET_ID costVtnetId ");
		sql.append(",T1.STATION_TYPE stationType ");
		sql.append(",T1.NOT_SOURCE_HNI_HCM notSourceHniHcm ");
		sql.append(",T1.NOT_SOURCE_61_PROVINCE notSource61Province ");
		sql.append(",T1.SOURCE_HNI_HCM sourceHniHcm ");
		sql.append(",T1.SOURCE_61_PROVINCE source61Province ");
		sql.append(",T1.CREATED_DATE createdDate ");
		sql.append(",T1.CREATED_USER_ID createdUserId ");
		sql.append(",T1.UPDATE_USER_ID updateUserId ");
		sql.append(",T1.UPDATED_DATE updatedDate ");
		sql.append(" FROM COST_VTNET_HTCT T1 ORDER BY T1.COST_VTNET_ID DESC");
    

		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
			query.addScalar("costVtnetId", new LongType());
		
			query.addScalar("stationType", new StringType());
		
			query.addScalar("notSourceHniHcm", new DoubleType());
		
			query.addScalar("notSource61Province", new DoubleType());
		
			query.addScalar("sourceHniHcm", new DoubleType());
		
			query.addScalar("source61Province", new DoubleType());
		
			query.addScalar("createdDate", new DateType());
		
			query.addScalar("createdUserId", new LongType());
		
			query.addScalar("updateUserId", new LongType());
		
			query.addScalar("updatedDate", new DateType());

		query.setResultTransformer(Transformers.aliasToBean(CostVtnetHtctDTO.class));    	
		if (criteria.getPage() != null && criteria.getPageSize() != null) {
			query.setFirstResult((criteria.getPage().intValue() - 1) * criteria.getPageSize().intValue());
			query.setMaxResults(criteria.getPageSize().intValue());
		}
		criteria.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
		return query.list();
	}

	public List<CostVtnetHtctDTO> getLstStationType() {
		StringBuilder sql = new StringBuilder("select NAME name  from APP_PARAM WHERE PAR_TYPE = 'VTNet' AND STATUS = 1");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("name", new StringType());
		return query.list();
	}  
}
