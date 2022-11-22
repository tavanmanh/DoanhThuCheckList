package com.viettel.coms.dao;

import java.util.List;
import com.viettel.coms.bo.GpmbHtctBO;
import com.viettel.coms.dto.Cost1477HtctDTO;
import com.viettel.coms.dto.GpmbHtctDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.*;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;


@Repository("gpmbHtctDAO")
public class GpmbHtctDAO extends BaseFWDAOImpl<GpmbHtctBO, Long> {

    public GpmbHtctDAO() {
        this.model = new GpmbHtctBO();
    }

    public GpmbHtctDAO(Session session) {
        this.session = session;
    }

    @SuppressWarnings("unchecked")
	public List<GpmbHtctDTO> getData4(GpmbHtctDTO criteria) {
    	StringBuilder sql = new StringBuilder("select ");
		sql.append(" T1.GPMB_ID gpmbId ");
		sql.append(",T1.PROVINCE_ID provinceId ");
		sql.append(",T1.PROVINCE_CODE provinceCode ");
		sql.append(",T1.AMOUNT_BTS amountBts ");
		sql.append(",T1.COST_GPMB costGpmb ");
		sql.append(",T1.COST_NCDN costNcdn ");
		sql.append(",T1.CREATED_DATE createdDate ");
		sql.append(",T1.CREATED_USER_ID createdUserId ");
		sql.append(",T1.UPDATE_USER_ID updateUserId ");
		sql.append(",T1.UPDATED_DATE updatedDate ");
		sql.append(" FROM GPMB_HTCT T1 ORDER BY T1.PROVINCE_CODE ASC ");

		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
			query.addScalar("gpmbId", new LongType());
			query.addScalar("provinceId", new LongType());
			query.addScalar("provinceCode", new StringType());
			query.addScalar("amountBts", new LongType());
			query.addScalar("costGpmb", new DoubleType());
			query.addScalar("costNcdn", new DoubleType());
			query.addScalar("createdDate", new DateType());
			query.addScalar("createdUserId", new LongType());
			query.addScalar("updateUserId", new LongType());
			query.addScalar("updatedDate", new DateType());
		query.setResultTransformer(Transformers.aliasToBean(GpmbHtctDTO.class));
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

	public Long getProvinceIdForImport(String provinceCode) {
		StringBuilder sql = new StringBuilder(
				"SELECT CAT_PROVINCE_ID provinceId FROM CAT_PROVINCE WHERE CODE = :provinceCode");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("provinceId", new LongType());
		query.setParameter("provinceCode", provinceCode);
		return (Long) query.uniqueResult();
	}


	public GpmbHtctDTO findByProvince(String codeProvince) {
		StringBuilder sql = new StringBuilder("select ");
		sql.append(" T1.GPMB_ID gpmbId ");
		sql.append(",T1.PROVINCE_ID provinceId ");
		sql.append(",T1.PROVINCE_CODE provinceCode ");
		sql.append(",T1.AMOUNT_BTS amountBts ");
		sql.append(",T1.COST_GPMB costGpmb ");
		sql.append(",T1.COST_NCDN costNcdn ");
		sql.append(",T1.CREATED_DATE createdDate ");
		sql.append(",T1.CREATED_USER_ID createdUserId ");
		sql.append(",T1.UPDATE_USER_ID updateUserId ");
		sql.append(",T1.UPDATED_DATE updatedDate ");
		sql.append(" FROM GPMB_HTCT T1 WHERE 1 = 1");

		if(codeProvince != null){
			sql.append(" AND UPPER(T1.PROVINCE_CODE) = UPPER(:codeProvince)");
		}

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("gpmbId", new LongType());
		query.addScalar("provinceId", new LongType());
		query.addScalar("provinceCode", new StringType());
		query.addScalar("amountBts", new LongType());
		query.addScalar("costGpmb", new DoubleType());
		query.addScalar("costNcdn", new DoubleType());
		query.addScalar("createdDate", new DateType());
		query.addScalar("createdUserId", new LongType());
		query.addScalar("updateUserId", new LongType());
		query.addScalar("updatedDate", new DateType());

		if(codeProvince != null){
			query.setParameter("codeProvince", codeProvince);
		}
		query.setResultTransformer(Transformers.aliasToBean(GpmbHtctDTO.class));
		return (GpmbHtctDTO) query.uniqueResult();
	}
}
