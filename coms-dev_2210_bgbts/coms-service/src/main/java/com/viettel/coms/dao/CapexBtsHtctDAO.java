package com.viettel.coms.dao;

import java.util.Date;
import java.util.List;
import com.viettel.coms.bo.CapexBtsHtctBO;
import com.viettel.coms.dto.CapexBtsHtctDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.*;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;

@Repository("capexBtsHtctDAO")
public class CapexBtsHtctDAO extends BaseFWDAOImpl<CapexBtsHtctBO, Long> {

	public CapexBtsHtctDAO() {
		this.model = new CapexBtsHtctBO();
	}

	public CapexBtsHtctDAO(Session session) {
		this.session = session;
	}

	@SuppressWarnings("unchecked")
	public List<CapexBtsHtctDTO> getData7(CapexBtsHtctDTO criteria) {
		StringBuilder sql = new StringBuilder("select  ");
		sql.append(" T1.CAPEX_BTS_ID capexBtsId ");
		sql.append(",T1.ITEM_TYPE itemType ");
		sql.append(",T1.ITEM item ");
		sql.append(",T1.WORK_CAPEX workCapex ");
		sql.append(",T1.PROVINCE_ID provinceId ");
		sql.append(",T1.PROVINCE_CODE provinceCode ");
		sql.append(",T1.COST_CAPEX_BTS costCapexBts ");
		sql.append(",T1.CREATED_DATE createdDate ");
		sql.append(",T1.CREATED_USER_ID createdUserId ");
		sql.append(",T1.UPDATE_USER_ID updateUserId ");
		sql.append(",T1.UPDATED_DATE updatedDate ");
		sql.append(" FROM CAPEX_BTS_HTCT T1 ORDER BY T1.PROVINCE_CODE ASC ");

		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
		query.addScalar("capexBtsId", new LongType());

		query.addScalar("itemType", new StringType());

		query.addScalar("item", new StringType());

		query.addScalar("workCapex", new StringType());

		query.addScalar("provinceId", new LongType());

		query.addScalar("provinceCode", new StringType());

		query.addScalar("costCapexBts", new DoubleType());

		query.addScalar("createdDate", new DateType());

		query.addScalar("createdUserId", new LongType());

		query.addScalar("updateUserId", new LongType());

		query.addScalar("updatedDate", new DateType());

		query.setResultTransformer(Transformers.aliasToBean(CapexBtsHtctDTO.class));
		if (criteria.getPage() != null && criteria.getPageSize() != null) {
			query.setFirstResult((criteria.getPage().intValue() - 1) * criteria.getPageSize().intValue());
			query.setMaxResults(criteria.getPageSize().intValue());
		}
		criteria.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
		return query.list();
	}

	public List<String> getProvince() {
		StringBuilder sql = new StringBuilder("SELECT DISTINCT CODE code " + "FROM CAT_PROVINCE");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("code", new StringType());
		return query.list();
	}

	public void updateCapex(String itemType, String item, String workCapex, String provinceCode, Double costCapexBts,
			Long capexBtsId, Date updatedDate, Long updateUserId) {
		StringBuilder sql = new StringBuilder("UPDATE CAPEX_BTS_HTCT SET ");
		if (costCapexBts != null) {
			sql.append(" COST_CAPEX_BTS = :costCapexBts, ");
		} else {
			sql.append(" COST_CAPEX_BTS = 0, ");
		}
		sql.append(" UPDATED_DATE = :updatedDate, UPDATE_USER_ID = :updateUserId "
				+ "WHERE CAPEX_BTS_ID = :capexBtsId " + "AND UPPER(ITEM_TYPE) = UPPER(:itemType) " + "AND UPPER(ITEM) = UPPER(:item) "
				+ "AND UPPER(WORK_CAPEX) = UPPER(:workCapex) " + "AND UPPER(PROVINCE_CODE) =  UPPER(:provinceCode) ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("itemType", itemType);
		query.setParameter("item", item);
		query.setParameter("workCapex", workCapex);
		query.setParameter("provinceCode", provinceCode);
		if (costCapexBts != null) {
			query.setParameter("costCapexBts", costCapexBts);
		}
		query.setParameter("capexBtsId", capexBtsId);
		query.setParameter("updatedDate", updatedDate);
		query.setParameter("updateUserId", updateUserId);
		query.executeUpdate();
	}

	public Long getProvinceIdForImport(String provinceCode) {
		StringBuilder sql = new StringBuilder(
				"SELECT CAT_PROVINCE_ID provinceId FROM CAT_PROVINCE WHERE CODE = :provinceCode");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("provinceId", new LongType());
		query.setParameter("provinceCode", provinceCode);
		return (Long) query.uniqueResult();
	}

	public List<CapexBtsHtctDTO> lstItemCapexTem() {
		StringBuilder sql = new StringBuilder("Select ITEM_TYPE itemType, ITEM item, WORK workCapex from ITEM_CAPEX WHERE STATUS = 1");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("itemType", new StringType());
		query.addScalar("item", new StringType());
		query.addScalar("workCapex", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(CapexBtsHtctDTO.class));
		return query.list();
	}

	public List<CapexBtsHtctDTO> findByProvince(String codeProvince) {
		StringBuilder sql = new StringBuilder("select  ");
		sql.append(" T1.CAPEX_BTS_ID capexBtsId ");
		sql.append(",T1.ITEM_TYPE itemType ");
		sql.append(",T1.ITEM item ");
		sql.append(",T1.WORK_CAPEX workCapex ");
		sql.append(",T1.PROVINCE_ID provinceId ");
		sql.append(",T1.PROVINCE_CODE provinceCode ");
		sql.append(",T1.COST_CAPEX_BTS costCapexBts ");
		sql.append(",T1.CREATED_DATE createdDate ");
		sql.append(",T1.CREATED_USER_ID createdUserId ");
		sql.append(",T1.UPDATE_USER_ID updateUserId ");
		sql.append(",T1.UPDATED_DATE updatedDate ");
		sql.append(" FROM CAPEX_BTS_HTCT T1 ");

		if(codeProvince != null){
			sql.append(" WHERE UPPER(T1.PROVINCE_CODE) = UPPER(:codeProvince)");
		}

		sql.append(" ORDER BY T1.PROVINCE_CODE ASC");

		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("capexBtsId", new LongType());

		query.addScalar("itemType", new StringType());

		query.addScalar("item", new StringType());

		query.addScalar("workCapex", new StringType());

		query.addScalar("provinceId", new LongType());

		query.addScalar("provinceCode", new StringType());

		query.addScalar("costCapexBts", new DoubleType());

		query.addScalar("createdDate", new DateType());

		query.addScalar("createdUserId", new LongType());

		query.addScalar("updateUserId", new LongType());

		query.addScalar("updatedDate", new DateType());

		if(codeProvince != null){
			query.setParameter("codeProvince", codeProvince);
		}

		query.setResultTransformer(Transformers.aliasToBean(CapexBtsHtctDTO.class));

		return query.list();
	}

}
