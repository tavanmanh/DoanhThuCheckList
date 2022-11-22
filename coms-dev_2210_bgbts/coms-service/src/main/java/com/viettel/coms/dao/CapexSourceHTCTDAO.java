package com.viettel.coms.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.viettel.coms.bo.CapexSourceHTCTBO;
import com.viettel.coms.dto.CapexSourceHTCTDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;


@Repository("capexSourceHTCTDAO")
public class CapexSourceHTCTDAO extends BaseFWDAOImpl<CapexSourceHTCTBO, Long> {
	public CapexSourceHTCTDAO() {
		this.model = new CapexSourceHTCTBO();
	}

	public CapexSourceHTCTDAO(Session session) {
		this.session = session;
	}

	public List<CapexSourceHTCTDTO> getCapexSource(CapexSourceHTCTDTO obj) {
		StringBuilder sql = new StringBuilder("SELECT CAPEX_SOURCE_ID capexSourceId, " + "CONTENT_CAPEX contentCapex, "
				+ "UNIT unit, " + "COST_CAPEX costCapex, " + "CREATED_DATE createdDate, "
				+ "CREATED_USER_ID createdUserId, " + "UPDATE_USER_ID updateUserId, " + "UPDATED_DATE updatedDate "
				+ "FROM CAPEX_SOURCE_HTCT " + "ORDER BY CAPEX_SOURCE_ID DESC ");
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM ( ");
		sqlCount.append(sql.toString());
		sqlCount.append(" )");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
		query.addScalar("capexSourceId", new LongType());
		query.addScalar("contentCapex", new StringType());
		query.addScalar("unit", new StringType());
		query.addScalar("costCapex", new LongType());
		query.addScalar("createdDate", new DateType());
		query.addScalar("createdUserId", new LongType());
		query.addScalar("updateUserId", new LongType());
		query.addScalar("updatedDate", new DateType());
		query.setResultTransformer(Transformers.aliasToBean(CapexSourceHTCTDTO.class));
		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
		return query.list();
	}

	public void updateCapex(String unit,  Long costCapex, Long capexSourceId, String contentCapex, Date updatedDate, Long updateUserId) {
		StringBuilder sql = new StringBuilder("UPDATE CAPEX_SOURCE_HTCT SET UNIT = :unit, UPDATED_DATE = :updatedDate, UPDATE_USER_ID = :updateUserId, ");
		if(costCapex != null) {
			sql.append(" COST_CAPEX = :costCapex ");
		}  else {
			sql.append(" COST_CAPEX = 0 ");
		}
		sql.append(" WHERE CAPEX_SOURCE_ID = :capexSourceId AND UPPER(CONTENT_CAPEX) = UPPER(:contentCapex) ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("unit", unit);
		query.setParameter("updatedDate", updatedDate);
		query.setParameter("updateUserId", updateUserId);
		if(costCapex != null) {
			query.setParameter("costCapex", costCapex);
		}
	    query.setParameter("capexSourceId", capexSourceId);
	    query.setParameter("contentCapex", contentCapex);
	    query.executeUpdate();
	}

	public List<String> getCapexNName() {
		StringBuilder sql = new StringBuilder("select NAME name from APP_PARAM WHERE PAR_TYPE = 'Capex_Nguon' AND STATUS = 1");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("name", new StringType());
		
		return query.list();
	}
}
