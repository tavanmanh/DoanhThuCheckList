package com.viettel.coms.dao;

import java.math.BigDecimal;
import java.util.List;

import com.viettel.cat.dto.CatProvinceDTO;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.viettel.coms.bo.Cost1477HtctBO;
import com.viettel.coms.dto.Cost1477HtctDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;


@Repository("cost1477HtctDAO")
public class Cost1477HtctDAO extends BaseFWDAOImpl<Cost1477HtctBO, Long> {

    public Cost1477HtctDAO() {
        this.model = new Cost1477HtctBO();
    }

    public Cost1477HtctDAO(Session session) {
        this.session = session;
    }

    @SuppressWarnings("unchecked")
	public List<Cost1477HtctDTO> getData6(Cost1477HtctDTO criteria) {
    	StringBuilder sql = new StringBuilder("select  ");
		sql.append(" T1.COST_1477_HTCT_ID cost1477HtctId ");
		sql.append(",T1.TYPE_GROUP typeGroup ");
		sql.append(",T1.ADDRESS address ");
		sql.append(",T1.TOPOGRAPHIC topographic ");
		sql.append(",T1.STATION_TYPE stationType ");
		sql.append(",T1.COST_1477 cost1477 ");
		sql.append(",T1.CREATED_DATE createdDate ");
		sql.append(",T1.CREATED_USER_ID createdUserId ");
		sql.append(",T1.UPDATE_USER_ID updateUserId ");
		sql.append(",T1.UPDATED_DATE updatedDate ");
		sql.append(" FROM COST_1477_HTCT T1 ORDER BY T1.COST_1477_HTCT_ID DESC");

		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
			query.addScalar("cost1477HtctId", new LongType());

			query.addScalar("typeGroup", new StringType());

			query.addScalar("address", new StringType());

			query.addScalar("topographic", new StringType());

			query.addScalar("stationType", new StringType());

			query.addScalar("cost1477", new DoubleType());

			query.addScalar("createdDate", new DateType());

			query.addScalar("createdUserId", new LongType());

			query.addScalar("updateUserId", new LongType());

			query.addScalar("updatedDate", new DateType());


		query.setResultTransformer(Transformers.aliasToBean(Cost1477HtctDTO.class));
		if (criteria.getPage() != null && criteria.getPageSize() != null) {
			query.setFirstResult((criteria.getPage().intValue() - 1) * criteria.getPageSize().intValue());
			query.setMaxResults(criteria.getPageSize().intValue());
		}
		criteria.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
		return query.list();
	}

	public Cost1477HtctDTO findByGroup(String typeGroup, String address, String topographic, String stationType){
		StringBuilder sql = new StringBuilder("select  ");
		sql.append(" T1.COST_1477_HTCT_ID cost1477HtctId ");
		sql.append(",T1.TYPE_GROUP typeGroup ");
		sql.append(",T1.ADDRESS address ");
		sql.append(",T1.TOPOGRAPHIC topographic ");
		sql.append(",T1.STATION_TYPE stationType ");
		sql.append(",T1.COST_1477 cost1477 ");
		sql.append(",T1.CREATED_DATE createdDate ");
		sql.append(",T1.CREATED_USER_ID createdUserId ");
		sql.append(",T1.UPDATE_USER_ID updateUserId ");
		sql.append(",T1.UPDATED_DATE updatedDate ");
		sql.append(" FROM COST_1477_HTCT T1 Where 1 = 1 ");

		if(typeGroup != null){
			sql.append(" and UPPER(T1.TYPE_GROUP) = UPPER(:typeGroup)");
		}

		if(typeGroup != null){
			sql.append(" and UPPER(T1.ADDRESS) = UPPER(:address)");
		}

		if(typeGroup != null){
			sql.append(" and UPPER(T1.TOPOGRAPHIC) = UPPER(:topographic)");
		}

		if(typeGroup != null){
			sql.append(" and UPPER(T1.STATION_TYPE) = UPPER(:stationType)");
		}

		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("cost1477HtctId", new LongType());

		query.addScalar("typeGroup", new StringType());

		query.addScalar("address", new StringType());

		query.addScalar("topographic", new StringType());

		query.addScalar("stationType", new StringType());

		query.addScalar("cost1477", new DoubleType());

		query.addScalar("createdDate", new DateType());

		query.addScalar("createdUserId", new LongType());

		query.addScalar("updateUserId", new LongType());

		query.addScalar("updatedDate", new DateType());

		query.setParameter("typeGroup", typeGroup);

		query.setParameter("address", address);

		query.setParameter("topographic", topographic);

		query.setParameter("stationType", stationType);


		query.setResultTransformer(Transformers
				.aliasToBean(Cost1477HtctDTO.class));

		return (Cost1477HtctDTO) query.uniqueResult();
	}
}
