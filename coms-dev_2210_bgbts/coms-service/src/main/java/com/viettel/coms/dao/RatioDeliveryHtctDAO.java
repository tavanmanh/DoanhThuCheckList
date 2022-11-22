package com.viettel.coms.dao;

import java.math.BigDecimal;
import java.util.List;

import com.viettel.coms.dto.Cost1477HtctDTO;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.viettel.coms.bo.RatioDeliveryHtctBO;
import com.viettel.coms.dto.RatioDeliveryHtctDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;


@Repository("ratioDeliveryHtctDAO")
public class RatioDeliveryHtctDAO extends BaseFWDAOImpl<RatioDeliveryHtctBO, Long> {

    public RatioDeliveryHtctDAO() {
        this.model = new RatioDeliveryHtctBO();
    }

    public RatioDeliveryHtctDAO(Session session) {
        this.session = session;
    }

    @SuppressWarnings("unchecked")
	public List<RatioDeliveryHtctDTO> getData3(RatioDeliveryHtctDTO criteria) {
    	StringBuilder sql = new StringBuilder("select ");
		sql.append("T1.RATIO_DELIVERY_ID ratioDeliveryId ");
		sql.append(",T1.CAT_PROVINCE_ID catProvinceId ");
		sql.append(",T1.CAT_PROVINCE_CODE catProvinceCode ");
		sql.append(",T1.COST_DELIVERY_BTS costDeliveryBts ");
		sql.append(",T1.COST_MOUNTAINS_BTS costMountainsBts ");
		sql.append(",T1.COST_ROOF_BTS costRoofBts ");
		sql.append(",T1.COST_DELIVERY_PRU costDeliveryPru ");
		sql.append(",T1.COST_MOUNTAINS_PRU costMountainsPru ");
		sql.append(",T1.COST_ROOF_PRU costRoofPru ");
		sql.append(",T1.COST_DELIVERY_SMALLCELL costDeliverySmallcell ");
		sql.append(",T1.COST_MOUNTAINS_SMALLCELL costMountainsSmallcell ");
		sql.append(",T1.COST_ROOF_SMALLCELL costRoofSmallcell ");
		sql.append(",T1.CREATED_DATE createdDate ");
		sql.append(",T1.CREATED_USER_ID createdUserId ");
		sql.append(",T1.UPDATE_USER_ID updateUserId ");
		sql.append(",T1.UPDATED_DATE updatedDate ");
		sql.append(" FROM RATIO_DELIVERY_HTCT T1 ORDER BY T1.CAT_PROVINCE_CODE ASC ");
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
			query.addScalar("ratioDeliveryId", new LongType());
			query.addScalar("catProvinceId", new LongType());
			query.addScalar("catProvinceCode", new StringType());
			query.addScalar("costDeliveryBts", new DoubleType());
			query.addScalar("costMountainsBts", new DoubleType());
			query.addScalar("costRoofBts", new DoubleType());
			query.addScalar("costDeliveryPru", new DoubleType());
			query.addScalar("costMountainsPru", new DoubleType());
			query.addScalar("costRoofPru", new DoubleType());
			query.addScalar("costDeliverySmallcell", new DoubleType());
			query.addScalar("costMountainsSmallcell", new DoubleType());
			query.addScalar("costRoofSmallcell", new DoubleType());
			query.addScalar("createdDate", new DateType());
			query.addScalar("createdUserId", new LongType());
			query.addScalar("updateUserId", new LongType());
			query.addScalar("updatedDate", new DateType());

		query.setResultTransformer(Transformers.aliasToBean(RatioDeliveryHtctDTO.class));
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

	public RatioDeliveryHtctDTO findByProvince(String codeProvince) {
		StringBuilder sql = new StringBuilder("select ");
		sql.append("T1.RATIO_DELIVERY_ID ratioDeliveryId ");
		sql.append(",T1.CAT_PROVINCE_ID catProvinceId ");
		sql.append(",T1.CAT_PROVINCE_CODE catProvinceCode ");
		sql.append(",T1.COST_DELIVERY_BTS costDeliveryBts ");
		sql.append(",T1.COST_MOUNTAINS_BTS costMountainsBts ");
		sql.append(",T1.COST_ROOF_BTS costRoofBts ");
		sql.append(",T1.COST_DELIVERY_PRU costDeliveryPru ");
		sql.append(",T1.COST_MOUNTAINS_PRU costMountainsPru ");
		sql.append(",T1.COST_ROOF_PRU costRoofPru ");
		sql.append(",T1.COST_DELIVERY_SMALLCELL costDeliverySmallcell ");
		sql.append(",T1.COST_MOUNTAINS_SMALLCELL costMountainsSmallcell ");
		sql.append(",T1.COST_ROOF_SMALLCELL costRoofSmallcell ");
		sql.append(",T1.CREATED_DATE createdDate ");
		sql.append(",T1.CREATED_USER_ID createdUserId ");
		sql.append(",T1.UPDATE_USER_ID updateUserId ");
		sql.append(",T1.UPDATED_DATE updatedDate ");
		sql.append(" FROM RATIO_DELIVERY_HTCT T1 WHERE 1=1");

		if(codeProvince != null){
			sql.append(" AND UPPER(T1.CAT_PROVINCE_CODE) = UPPER(:codeProvince)");
		}

		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("ratioDeliveryId", new LongType());
		query.addScalar("catProvinceId", new LongType());
		query.addScalar("catProvinceCode", new StringType());
		query.addScalar("costDeliveryBts", new DoubleType());
		query.addScalar("costMountainsBts", new DoubleType());
		query.addScalar("costRoofBts", new DoubleType());
		query.addScalar("costDeliveryPru", new DoubleType());
		query.addScalar("costMountainsPru", new DoubleType());
		query.addScalar("costRoofPru", new DoubleType());
		query.addScalar("costDeliverySmallcell", new DoubleType());
		query.addScalar("costMountainsSmallcell", new DoubleType());
		query.addScalar("costRoofSmallcell", new DoubleType());
		query.addScalar("createdDate", new DateType());
		query.addScalar("createdUserId", new LongType());
		query.addScalar("updateUserId", new LongType());
		query.addScalar("updatedDate", new DateType());

		query.setParameter("codeProvince", codeProvince);

		query.setResultTransformer(Transformers.aliasToBean(RatioDeliveryHtctDTO.class));

		return (RatioDeliveryHtctDTO) query.uniqueResult();
	}

}
