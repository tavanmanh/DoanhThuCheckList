package com.viettel.coms.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.viettel.coms.bo.WaccHtctBO;
import com.viettel.coms.dto.WaccHtctDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@Repository("waccHtctDAO")
public class WaccHtctDAO extends BaseFWDAOImpl<WaccHtctBO, Long>{

	public WaccHtctDAO() {
		this.model = new WaccHtctBO();
	}
	public WaccHtctDAO(Session session) {
		this.session = session;
	}
	public List<WaccHtctDTO> getData2(WaccHtctDTO obj) {
		StringBuilder sql = new StringBuilder("SELECT "
				+ " WACC_ID waccId, WACC_NAME waccName, WACC_REX waccRex, CREATED_DATE createdDate,"
				+ " CREATED_USER_ID createdUserId,  UPDATE_USER_ID updateUserId, UPDATED_DATE updatedDate "
				+ " FROM WACC_HTCT ORDER BY WACC_ID DESC ");
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(" )");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
		query.addScalar("waccId", new LongType());
		query.addScalar("waccName", new StringType());
		query.addScalar("waccRex", new DoubleType());
		query.addScalar("createdDate", new DateType());
		query.addScalar("createdUserId", new LongType());
		query.addScalar("updateUserId", new LongType());
		query.addScalar("updatedDate", new DateType());
		
		query.setResultTransformer(Transformers.aliasToBean(WaccHtctDTO.class));
		if(obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
		return query.list();
	}
	public void updateWacc(Double waccRex, String waccName, Long waccId, Date updatedDate, Long updateUserId) {
		StringBuilder sql = new StringBuilder("UPDATE WACC_HTCT SET " ); 
		if(waccRex != null) {
			sql.append(" WACC_REX = :waccRex, ");
		} else {
			sql.append(" WACC_REX = 0, ");
		}
		sql.append(" UPDATED_DATE = :updatedDate, " + 
				" UPDATE_USER_ID = :updateUserId " + 
				" WHERE UPPER(WACC_NAME) = UPPER(:waccName) " + 
				" AND WACC_ID = :waccId ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		if(waccRex != null) {
			query.setParameter("waccRex", waccRex);
		}
		query.setParameter("waccName", waccName);
		query.setParameter("waccId", waccId);
		query.setParameter("updatedDate", updatedDate);
		query.setParameter("updateUserId", updateUserId);
		query.executeUpdate();
		
	}
	public List<String> getWaccName() {
		StringBuilder sql = new StringBuilder("select NAME name from APP_PARAM WHERE PAR_TYPE = 'WACC' AND STATUS = 1");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("name", new StringType());
		return query.list();
	}

}
