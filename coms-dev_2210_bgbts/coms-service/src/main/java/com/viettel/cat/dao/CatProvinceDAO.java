package com.viettel.cat.dao;

import com.viettel.cat.bo.CatProvinceBO;
import com.viettel.cat.dto.CatProvinceDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import com.viettel.wms.utils.ValidateUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author hailh10
 */
@Repository("catProvinceDAO")
public class CatProvinceDAO extends BaseFWDAOImpl<CatProvinceBO, Long> {

    public CatProvinceDAO() {
        this.model = new CatProvinceBO();
    }

    public CatProvinceDAO(Session session) {
        this.session = session;
    }

    @SuppressWarnings("unchecked")
    public List<CatProvinceDTO> doSearch(CatProvinceDTO criteria) {
        // StringBuilder stringBuilder = new
        // StringBuilder("select totalRecord ");
        // stringBuilder.append(",T1.CAT_PROVINCE_ID catProvinceId ");
        // stringBuilder.append(",T1.CODE code ");
        // stringBuilder.append(",T1.NAME name ");
        // stringBuilder.append(",T1.STATUS status ");
        // stringBuilder.append("WHERE 1=1 ");
        StringBuilder stringBuilder = new StringBuilder(
                "SELECT T1.CAT_PROVINCE_ID catProvinceId," + "T1.CODE code,"
                        + "T1.NAME name," + "T1.STATUS status "
                        + " FROM CAT_PROVINCE T1 where 1=1 ");
        if (null != criteria.getKeySearch()) {
            stringBuilder
                    .append("AND (upper(T1.CODE) like upper(:key) or upper(T1.NAME) like :key)");
        }

        if (StringUtils.isNotEmpty(criteria.getStatus())) {
            stringBuilder
                    .append("AND UPPER(T1.STATUS) LIKE UPPER(:status) ESCAPE '\\' ");
        }

        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(stringBuilder.toString());
        sqlCount.append(")");

        SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        query.addScalar("catProvinceId", new LongType());
        query.addScalar("code", new StringType());
        query.addScalar("name", new StringType());
        query.addScalar("status", new StringType());

        if (null != criteria.getKeySearch()) {
            query.setParameter("key", "%" + criteria.getKeySearch() + "%");
            queryCount.setParameter("key", "%" + criteria.getKeySearch() + "%");
        }

        if (null != criteria.getStatus()) {
            query.setParameter("status", criteria.getStatus());
            queryCount.setParameter("status", criteria.getStatus());
        }

        query.setResultTransformer(Transformers
                .aliasToBean(CatProvinceDTO.class));
        if (criteria.getPage() != null && criteria.getPageSize() != null) {
            query.setFirstResult((criteria.getPage().intValue() - 1)
                    * criteria.getPageSize().intValue());
            query.setMaxResults(criteria.getPageSize().intValue());
        }

        List ls = query.list();
        criteria.setTotalRecord(((BigDecimal) queryCount.uniqueResult())
                .intValue());
        return ls;
    }

    public CatProvinceDTO findByCode(String code) {
        StringBuilder stringBuilder = new StringBuilder(
                "Select T1.CAT_PROVINCE_ID catProvinceId," + "T1.CODE code,"
                        + "T1.NAME name," + "T1.STATUS status, " + "T1.GROUP_NAME groupName"
                        + " FROM CAT_PROVINCE T1 "
                        + " WHERE 1=1 AND upper(T1.CODE) = upper(:code)");

        SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());

        query.addScalar("catProvinceId", new LongType());
        query.addScalar("code", new StringType());
        query.addScalar("name", new StringType());
        query.addScalar("status", new StringType());
        query.addScalar("groupName", new StringType());

        query.setParameter("code", code);
        query.setResultTransformer(Transformers
                .aliasToBean(CatProvinceDTO.class));

        return (CatProvinceDTO) query.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public List<CatProvinceDTO> getForComboBox(CatProvinceDTO obj) {
        String sqlStr = "SELECT CAT_PROVINCE_ID catProvinceId" + " ,NAME name"
                + " ,CODE code" + " FROM CAT_PROVINCE" + " WHERE 1=1";

        StringBuilder sql = new StringBuilder(sqlStr);
        if (StringUtils.isNotEmpty(obj.getStatus())) {
            sql.append(" AND STATUS = :status ");
        }

        if (StringUtils.isNotEmpty(obj.getCode())) {
            sql.append(" AND upper(CODE)=upper(:code) ");
        }

        if(obj.getLstProvince()!=null && !obj.getLstProvince().isEmpty()) {
        	sql.append(" AND CODE in (:lstProvince) ");
        }
        
        sql.append(" ORDER BY CODE ");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.addScalar("catProvinceId", new LongType());
        query.addScalar("code", new StringType());
        query.addScalar("name", new StringType());

        query.setResultTransformer(Transformers
                .aliasToBean(CatProvinceDTO.class));

        if (StringUtils.isNotEmpty(obj.getStatus())) {
            query.setParameter("status", obj.getStatus());
        }

        if (StringUtils.isNotEmpty(obj.getCode())) {
            query.setParameter("code", obj.getCode());
        }
        
        if(obj.getLstProvince()!=null && !obj.getLstProvince().isEmpty()) {
        	query.setParameterList("lstProvince", obj.getLstProvince());
        }

        return query.list();
    }

    @SuppressWarnings("unchecked")
    public CatProvinceDTO getById(Long id) {
        StringBuilder stringBuilder = new StringBuilder("SELECT ");
        stringBuilder.append("T1.CAT_PROVINCE_ID catProvinceId ");
        stringBuilder.append(",T1.CODE code ");
        stringBuilder.append(",T1.NAME name ");
        stringBuilder.append(",T1.STATUS status ");

        stringBuilder.append("FROM CAT_PROVINCE T1 ");
        stringBuilder
                .append("WHERE T1.IS_DELETED = 'N' AND T1.CAT_PROVINCE_ID = :catProvinceId ");

        SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());

        query.addScalar("catProvinceId", new LongType());
        query.addScalar("code", new StringType());
        query.addScalar("name", new StringType());
        query.addScalar("status", new StringType());

        query.setParameter("catProvinceId", id);
        query.setResultTransformer(Transformers
                .aliasToBean(CatProvinceDTO.class));

        return (CatProvinceDTO) query.uniqueResult();
    }

    // Tìm kiếm tỉnh trong popup
    @SuppressWarnings("unchecked")
    public List<CatProvinceDTO> doSearchProvinceInPopup(CatProvinceDTO obj) {
        StringBuilder sql = new StringBuilder(
                "SELECT CAT_PROVINCE_ID catProvinceId, NAME name, STATUS status, CODE code, AREA_CODE areaCode FROM CAT_PROVINCE cpro ");
        if (StringUtils.isNotEmpty(obj.getName())) {
            sql.append(" WHERE upper(cpro.NAME) LIKE upper(:name) escape '&' OR upper(cpro.CODE) LIKE upper(:name) escape '&' ");
        }
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        query.addScalar("catProvinceId", new LongType());
        query.addScalar("name", new StringType());
        query.addScalar("status", new StringType());
        query.addScalar("code", new StringType());
        query.addScalar("areaCode", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(CatProvinceDTO.class));
        if (StringUtils.isNotEmpty(obj.getName())) {
            query.setParameter("name", "%" + ValidateUtils.validateKeySearch(obj.getName()) + "%");
            queryCount.setParameter("name", "%" + ValidateUtils.validateKeySearch(obj.getName()) + "%");
        }
        
        if (obj.getPage() != null && obj.getPageSize() != null) {
            query.setFirstResult((obj.getPage().intValue() - 1)
                    * obj.getPageSize().intValue());
            query.setMaxResults(obj.getPageSize().intValue());
        }

        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
        return query.list();
    }

    //Huypq-28052020-start
    @SuppressWarnings("unchecked")
    public List<CatProvinceDTO> getProvinceByDomainInPopup(CatProvinceDTO obj, List<String> lstProvince) {
        StringBuilder sql = new StringBuilder(
                "SELECT CAT_PROVINCE_ID catProvinceId, "
                + "NAME name, "
                + "STATUS status, "
                + "CODE code, "
                + "AREA_CODE areaCode "
                + "FROM CAT_PROVINCE cpro "
                + "WHERE STATUS!=0");
        if (StringUtils.isNotEmpty(obj.getName())) {
            sql.append(" AND upper(cpro.NAME) LIKE upper(:name) escape '&' OR upper(cpro.CODE) LIKE upper(:name) escape '&' ");
        }
        
        sql.append(" AND CAT_PROVINCE_ID in (:lstProvince) ");
        sql.append(" ORDER BY CODE ASC ");
        
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        query.addScalar("catProvinceId", new LongType());
        query.addScalar("name", new StringType());
        query.addScalar("status", new StringType());
        query.addScalar("code", new StringType());
        query.addScalar("areaCode", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(CatProvinceDTO.class));
        if (StringUtils.isNotEmpty(obj.getName())) {
            query.setParameter("name", "%" + ValidateUtils.validateKeySearch(obj.getName()) + "%");
            queryCount.setParameter("name", "%" + ValidateUtils.validateKeySearch(obj.getName()) + "%");
        }
        
        query.setParameterList("lstProvince", lstProvince);
        queryCount.setParameterList("lstProvince", lstProvince);
        
        if (obj.getPage() != null && obj.getPageSize() != null) {
            query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
            query.setMaxResults(obj.getPageSize().intValue());
        }
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
        return query.list();
    }
    //Huy-end

    
    //Duonghv13-start-23/08/2021//
  	  @SuppressWarnings("unchecked")
      public List<CatProvinceDTO> getAllProvince() {
          StringBuilder stringBuilder = new StringBuilder("SELECT ");
          stringBuilder.append("T1.CAT_PROVINCE_ID catProvinceId ");
          stringBuilder.append(",T1.CODE code ");
          stringBuilder.append(",T1.NAME name ");
          stringBuilder.append(",T1.STATUS status ");

          stringBuilder.append("FROM CAT_PROVINCE T1 ");
          stringBuilder.append(" order by T1.NAME ASC");
         

          SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());

          query.addScalar("catProvinceId", new LongType());
          query.addScalar("code", new StringType());
          query.addScalar("name", new StringType());
          query.addScalar("status", new StringType());
          
          query.setResultTransformer(Transformers
                  .aliasToBean(CatProvinceDTO.class));

          return query.list();
      }
  	
  	public List<CatProvinceDTO> autoCompleteSearch(CatProvinceDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder stringBuilder = new StringBuilder("SELECT CP.CAT_PROVINCE_ID catProvinceId, "
                + "CP.NAME name, "
                + "CP.STATUS status, "
                + "CP.CODE code, "
                + "CP.AREA_CODE areaCode "
                + "FROM CAT_PROVINCE CP "
                + "where 1=1 ");

        if (null != obj.getName()) {
            stringBuilder.append("AND (upper(CP.NAME) like upper(:name) or upper(CP.CODE) like upper(:name) escape '&')");
        }

        if (null != obj.getStatus()) {
            stringBuilder.append(" AND CP.STATUS = :status ");
        }

        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM ( ");
        sqlCount.append(stringBuilder.toString());
        sqlCount.append(")");

        SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());

        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        query.addScalar("catProvinceId", new LongType());
        query.addScalar("name", new StringType());
        query.addScalar("status", new StringType());
        query.addScalar("code", new StringType());
        query.addScalar("areaCode", new StringType());

        if (null != obj.getName()) {
            query.setParameter("name", "%" + obj.getName() + "%");
            queryCount.setParameter("name", "%" + obj.getName() + "%");
        }

        
        if (null != obj.getStatus()) {
            query.setParameter("status", obj.getStatus());
            queryCount.setParameter("status", obj.getStatus());
        }

        query.setResultTransformer(Transformers.aliasToBean(CatProvinceDTO.class));

        if (obj.getPage() != null && obj.getPageSize() != null) {
            query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
            query.setMaxResults(obj.getPageSize().intValue());
        }

        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
        return query.list();
	}
    //Duonghv13-end-23/08/2021//

}
