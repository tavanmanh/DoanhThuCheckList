package com.viettel.coms.dao;

import java.util.Date;
import java.util.List;
import com.viettel.coms.bo.OfferHtctBO;
import com.viettel.coms.dto.OfferHtctDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.*;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;


@Repository("offerHtctDAO")
public class OfferHtctDAO extends BaseFWDAOImpl<OfferHtctBO, Long> {

    public OfferHtctDAO() {
        this.model = new OfferHtctBO();
    }

    public OfferHtctDAO(Session session) {
        this.session = session;
    }	
    
    @SuppressWarnings("unchecked")
	public List<OfferHtctDTO> getData8(OfferHtctDTO criteria) {
    	StringBuilder sql = new StringBuilder("select ");
		sql.append(" T1.OFFER_ID offerId ");
		sql.append(",T1.CATEGORY_OFFER categoryOffer ");
		sql.append(",T1.SYMBOL symbol ");
		sql.append(",T1.UNIT unit ");
		sql.append(",T1.CREATED_DATE createdDate ");
		sql.append(",T1.CREATED_USER_ID createdUserId ");
		sql.append(",T1.UPDATE_USER_ID updateUserId ");
		sql.append(",T1.UPDATED_DATE updatedDate ");
		sql.append(" FROM OFFER_HTCT T1 ORDER BY T1.OFFER_ID DESC");

		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
			query.addScalar("offerId", new LongType());
		
			query.addScalar("categoryOffer", new StringType());
		
			query.addScalar("symbol", new StringType());
		
			query.addScalar("unit", new StringType());
		
			query.addScalar("createdDate", new DateType());
		
			query.addScalar("createdUserId", new LongType());
		
			query.addScalar("updateUserId", new LongType());
		
			query.addScalar("updatedDate", new DateType());
		
		query.setResultTransformer(Transformers.aliasToBean(OfferHtctDTO.class));    	
		if (criteria.getPage() != null && criteria.getPageSize() != null) {
			query.setFirstResult((criteria.getPage().intValue() - 1) * criteria.getPageSize().intValue());
			query.setMaxResults(criteria.getPageSize().intValue());
		}
		criteria.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
		return query.list();
	}

	public void updateOffer(String categoryOffer, String symbol, String unit, Long offerId, Date updatedDate,
			Long updateUserId) {
		StringBuilder sql = new StringBuilder("UPDATE OFFER_HTCT SET " + 
				"SYMBOL = :symbol, " + 
				"UNIT = :unit, " + 
				"UPDATED_DATE = :updatedDate, " + 
				"UPDATE_USER_ID = :updateUserId " + 
				"WHERE CATEGORY_OFFER = :categoryOffer " + 
				"AND OFFER_ID = :offerId ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("symbol", symbol);
		query.setParameter("unit", unit);
		query.setParameter("updatedDate", updatedDate);
		query.setParameter("updateUserId", updateUserId);
		query.setParameter("categoryOffer", categoryOffer);
		query.setParameter("offerId", offerId);
		query.executeUpdate();
	}

	public List<String> getLstCate() {
		StringBuilder sql = new StringBuilder("select NAME name from APP_PARAM WHERE PAR_TYPE = 'Don_Gia' AND STATUS = 1");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("name", new StringType());
		return query.list();
	}  
}
