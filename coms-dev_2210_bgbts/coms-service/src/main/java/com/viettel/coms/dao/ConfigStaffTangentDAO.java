package com.viettel.coms.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.viettel.cat.dto.CatProvinceDTO;
import com.viettel.coms.bo.ConfigStaffTangentBO;
import com.viettel.coms.dto.ConfigStaffTangentDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import com.viettel.wms.utils.ValidateUtils;

@EnableTransactionManagement
@Repository("configStaffTangentDAO")
public class ConfigStaffTangentDAO extends BaseFWDAOImpl<ConfigStaffTangentBO, Long> {

	public ConfigStaffTangentDAO() {
		this.model = new ConfigStaffTangentBO();
	}

	public ConfigStaffTangentDAO(Session session) {
		this.session = session;
	}
	
	public List<ConfigStaffTangentDTO> doSearch(ConfigStaffTangentDTO obj) {
		StringBuilder sql = new StringBuilder(" SELECT ");
		sql.append("CONFIG_STAFF_TANGENT_ID configStaffTangentId, ")
		.append("CAT_PROVINCE_ID catProvinceId, ")
		.append("PROVINCE_CODE provinceCode, ")
		.append("TYPE type, ")
		.append("STAFF_ID staffId, ")
		.append("STAFF_CODE staffCode, ")
		.append("STAFF_NAME staffName, ")
		.append("STAFF_PHONE staffPhone, ")
		.append("STATUS status, ")
		.append("CREATED_USER createdUser, ")
		.append("CREATED_DATE createdDate, ")
		.append("UPDATED_USER updatedUser, ")
		.append("UPDATED_DATE updatedDate, ")
		.append("EMAIL email ")
		.append(" FROM CONFIG_STAFF_TANGENT ")
		.append(" WHERE 1=1 and type in (1,2) ");
		
		if(obj.getCatProvinceId()!=null) {
			sql.append(" AND CAT_PROVINCE_ID=:provinceId ");
		}
		
		if(StringUtils.isNotBlank(obj.getType())) {
			sql.append(" AND TYPE=:type ");
		}
		
		if(obj.getStaffId()!=null) {
			sql.append(" AND STAFF_ID=:staffId ");
		}
		
		sql.append(" ORDER BY CONFIG_STAFF_TANGENT_ID DESC ");
		
		StringBuilder sqlCount = new StringBuilder("select count(*) from (" + sql.toString() + ")");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
		
		query.addScalar("configStaffTangentId", new LongType());
		query.addScalar("catProvinceId", new LongType());
		query.addScalar("provinceCode", new StringType());
		query.addScalar("type", new StringType());
		query.addScalar("staffId", new LongType());
		query.addScalar("staffCode", new StringType());
		query.addScalar("staffName", new StringType());
		query.addScalar("staffPhone", new StringType());
		query.addScalar("status", new StringType());
		query.addScalar("createdUser", new LongType());
		query.addScalar("createdDate", new DateType());
		query.addScalar("updatedUser", new LongType());
		query.addScalar("updatedDate", new DateType());
		query.addScalar("email", new StringType());
		
		if(obj.getCatProvinceId()!=null) {
			query.setParameter("provinceId", obj.getCatProvinceId());
			queryCount.setParameter("provinceId", obj.getCatProvinceId());
		}
		
		if(StringUtils.isNotBlank(obj.getType())) {
			query.setParameter("type", obj.getType());
			queryCount.setParameter("type", obj.getType());
		}
		
		if(obj.getStaffId()!=null) {
			query.setParameter("staffId", obj.getStaffId());
			queryCount.setParameter("staffId", obj.getStaffId());
		}
		
		query.setResultTransformer(Transformers.aliasToBean(ConfigStaffTangentDTO.class));
		
		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
    public List<CatProvinceDTO> doSearchProvinceByRolePopup(CatProvinceDTO obj) {
        StringBuilder sql = new StringBuilder(
                "SELECT CAT_PROVINCE_ID catProvinceId, NAME name, STATUS status, CODE code, AREA_ID areaId, AREA_CODE areaCode FROM CAT_PROVINCE cpro ");
        sql.append(" WHERE 1=1 AND STATUS!=0 "
        		);
        if (StringUtils.isNotEmpty(obj.getName())) {
            sql.append(" AND upper(cpro.NAME) LIKE upper(:name) escape '&' OR upper(cpro.CODE) LIKE upper(:name) escape '&' ");
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
        query.addScalar("areaId", new LongType());
        
        query.setResultTransformer(Transformers.aliasToBean(CatProvinceDTO.class));
        if (StringUtils.isNotEmpty(obj.getName())) {
            query.setParameter("name", "%" + ValidateUtils.validateKeySearch(obj.getName()) + "%");
            queryCount.setParameter("name", "%" + ValidateUtils.validateKeySearch(obj.getName()) + "%");
        }
        if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
        return query.list();
    }
	
	@SuppressWarnings("unchecked")
    public List<ConfigStaffTangentDTO> doSearchStaffByPopup(ConfigStaffTangentDTO obj) {
        StringBuilder sql = new StringBuilder(
                "SELECT SYS_USER_ID staffId, EMPLOYEE_CODE staffCode, FULL_NAME staffName, PHONE_NUMBER staffPhone, EMAIL email FROM SYS_USER ");
        sql.append(" WHERE 1=1 and status!=0 and type_user is null "
        		);
        if (StringUtils.isNotEmpty(obj.getStaffName())) {
            sql.append(" AND upper(EMPLOYEE_CODE) LIKE upper(:name) escape '&' "
            		+ " OR upper(FULL_NAME) LIKE upper(:name) escape '&' "
            		+ " OR upper(EMAIL) LIKE upper(:name) escape '&'");
        }
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        query.addScalar("staffId", new LongType());
        query.addScalar("staffCode", new StringType());
        query.addScalar("staffName", new StringType());
        query.addScalar("staffPhone", new StringType());
        query.addScalar("email", new StringType());
        
        query.setResultTransformer(Transformers.aliasToBean(ConfigStaffTangentDTO.class));
        if (StringUtils.isNotEmpty(obj.getStaffName())) {
            query.setParameter("name", "%" + ValidateUtils.validateKeySearch(obj.getStaffName()) + "%");
            queryCount.setParameter("name", "%" + ValidateUtils.validateKeySearch(obj.getStaffName()) + "%");
        }
        if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
        return query.list();
    }
	
	@SuppressWarnings("unchecked")
    public List<CatProvinceDTO> doSearchProvinceByRolePopupByRole(CatProvinceDTO obj, List<String> provinceIdLst) {
        StringBuilder sql = new StringBuilder(
                "SELECT AREA_ID areaId, " + 
                "PROVINCE_ID catProvinceId, " + 
                "  code code, " + 
                "  name name " + 
//                "FROM AIO_AREA " +
                "FROM CTCT_IMS_OWNER.AREA " +
                "WHERE AREA_LEVEL=2 "
        		);
        if(provinceIdLst!=null && provinceIdLst.size()>0) {
        	sql.append(" AND PROVINCE_ID in (:provinceIdLst) ");
        }
        
        if (StringUtils.isNotEmpty(obj.getName())) {
            sql.append(" AND (upper(code) LIKE upper(:name) escape '&' OR upper(name) LIKE upper(:name) escape '&') ");
        }
        
        sql.append(" ORDER BY CODE ASC ");
        
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        query.addScalar("areaId", new LongType());
        query.addScalar("catProvinceId", new LongType());
        query.addScalar("name", new StringType());
        query.addScalar("code", new StringType());
        
        query.setResultTransformer(Transformers.aliasToBean(CatProvinceDTO.class));
        
        if(provinceIdLst!=null && provinceIdLst.size()>0) {
        	query.setParameterList("provinceIdLst", provinceIdLst);
            queryCount.setParameterList("provinceIdLst", provinceIdLst);
        }
        
        if (StringUtils.isNotEmpty(obj.getName())) {
            query.setParameter("name", "%" + ValidateUtils.validateKeySearch(obj.getName()) + "%");
            queryCount.setParameter("name", "%" + ValidateUtils.validateKeySearch(obj.getName()) + "%");
        }
        if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
        return query.list();
    }
	
	public ConfigStaffTangentDTO checkDuplicateConfig(Long catProvinceId, String type) {
		StringBuilder sql = new StringBuilder("select STAFF_ID staffId from CONFIG_STAFF_TANGENT where STATUS!=0 AND CAT_PROVINCE_ID=:catProvinceId and type=:type ");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("staffId", new LongType());
		
		query.setParameter("catProvinceId", catProvinceId);
		query.setParameter("type", type);
		
		query.setResultTransformer(Transformers.aliasToBean(ConfigStaffTangentDTO.class));
		
		List<ConfigStaffTangentDTO> ls = query.list();
		if(ls.size()>0) {
			return ls.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
    public List<ConfigStaffTangentDTO> doSearchStaffByConfigProvinceId(ConfigStaffTangentDTO obj) {
        StringBuilder sql = new StringBuilder("SELECT DISTINCT cst.STAFF_ID staffId, " + 
        		"cst.STAFF_CODE staffCode, " + 
        		"cst.STAFF_NAME staffName, " + 
        		"su.EMAIL email, " + 
        		"cst.STAFF_PHONE staffPhone " +
        		"FROM CONFIG_STAFF_TANGENT cst " + 
        		"left join sys_user su on su.SYS_USER_ID = cst.STAFF_ID " +
        		"WHERE cst.STATUS!=0 AND cst.CAT_PROVINCE_ID=:catProvinceId " + 
        		"AND cst.type             =:type");
        if (StringUtils.isNotEmpty(obj.getStaffName())) {
            sql.append(" AND (upper(STAFF_CODE) LIKE upper(:name) escape '&' "
            		+ " OR upper(STAFF_NAME) LIKE upper(:name) escape '&') ");
        }
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        query.addScalar("staffId", new LongType());
        query.addScalar("staffCode", new StringType());
        query.addScalar("staffName", new StringType());
        query.addScalar("email", new StringType());
        query.addScalar("staffPhone", new StringType());
        
        query.setResultTransformer(Transformers.aliasToBean(ConfigStaffTangentDTO.class));
        
        query.setParameter("catProvinceId", obj.getCatProvinceId());
        queryCount.setParameter("catProvinceId", obj.getCatProvinceId());
        
        query.setParameter("type", obj.getType());
        queryCount.setParameter("type", obj.getType());
        
        if (StringUtils.isNotEmpty(obj.getStaffName())) {
            query.setParameter("name", "%" + ValidateUtils.validateKeySearch(obj.getStaffName()) + "%");
            queryCount.setParameter("name", "%" + ValidateUtils.validateKeySearch(obj.getStaffName()) + "%");
        }
        if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
        return query.list();
    }
	
	public ConfigStaffTangentDTO checkUserBeforeDelete(Long performerId){
		StringBuilder sql = new StringBuilder("SELECT PERFORMER_ID staffId, PERFORMER_SOLUTION_ID FROM TANGENT_CUSTOMER WHERE (PERFORMER_ID=:performerId OR PERFORMER_SOLUTION_ID=:performerId) AND STATUS IN (1,2,3,4,5,6,7) ");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("staffId", new LongType());
		
		query.setParameter("performerId", performerId);
		
		query.setResultTransformer(Transformers.aliasToBean(ConfigStaffTangentDTO.class));
		
		List<ConfigStaffTangentDTO> ls = query.list();
		if(ls.size()>0) {
			return ls.get(0);
		}
		return null;
	}
}
