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
import org.springframework.transaction.annotation.Transactional;

import com.viettel.coms.bo.AIOSysUserBO;
import com.viettel.coms.dto.AIOSysGroupDTO;
import com.viettel.coms.dto.AIOSysUserDTO;
import com.viettel.coms.dto.ComsBaseFWDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

//VietNT_20190506_create
@EnableTransactionManagement
@Transactional
@Repository("aioSysUserDAO")
public class AIOSysUserDAO extends BaseFWDAOImpl<AIOSysUserBO, Long> {

	private AIOSysUserDTO aioSysUserDTO;

	public AIOSysUserDAO() {
		this.model = new AIOSysUserBO();
	}

	public AIOSysUserDAO(Session session) {
		this.session = session;
	}

	private static final String HIBERNATE_ESCAPE_CHAR = "\\";

	public <T extends ComsBaseFWDTO> void setPageSize(T obj, SQLQuery query, SQLQuery queryCount) {
		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize());
			query.setMaxResults(obj.getPageSize());
		}

		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
	}

	// Huypq-19082020-start
	public List<AIOSysUserDTO> doSearch(AIOSysUserDTO criteria, List<String> groupIdList) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(" select ");
		sqlBuilder.append(" u.SYS_USER_ID sysUserId, ");
		sqlBuilder.append(" u.LOGIN_NAME loginName, ");
		sqlBuilder.append(" u.FULL_NAME fullName, ");
		sqlBuilder.append(" u.PASSWORD password, ");
		sqlBuilder.append(" u.EMPLOYEE_CODE employeeCode, ");
		sqlBuilder.append(" u.EMAIL email, ");
		sqlBuilder.append(" u.PHONE_NUMBER phoneNumber, ");
		sqlBuilder.append(" u.STATUS status, ");
		sqlBuilder.append(" u.NEW_ID newId, ");
		sqlBuilder.append(" u.CHANGE_PASSWORD_DATE changePasswordDate, ");
		sqlBuilder.append(" u.NEED_CHANGE_PASSWORD needChangePassword, ");
		sqlBuilder.append(" u.SYS_GROUP_ID sysGroupId, ");
		sqlBuilder.append(" u.CREATED_DATE createdDate, ");
		sqlBuilder.append(" u.IS_CONFIRM isConfirm, ");
		sqlBuilder.append(" sg.NAME sysGroupName, ");
		sqlBuilder.append(" sglv2.SYS_GROUP_ID provinceIdXddd, ");
		sqlBuilder.append(" sglv2.CODE sysGroupLv2Code, ");
		sqlBuilder.append(" sglv2.NAME provinceNameXddd, ");
		sqlBuilder.append(" u.SALE_CHANNEL saleChannel, ");
		sqlBuilder.append(" u.PARENT_USER_ID parentUserId, ");
		sqlBuilder.append(" u.type_user typeUser, ");
		sqlBuilder.append(" u.ADDRESS address, ");
		sqlBuilder.append(" u.USER_BIRTHDAY userBirthday, ");
		sqlBuilder.append(" u.TAX_CODE taxCode,"
				+ " u.TAX_CODE_USER taxCodeUser,"
				+ " u.ACCOUNT_NUMBER accountNumber,"
				+ " u.CONTRACT_CODE contractCode,");
//				+ " u.PROVINCE_ID_XDDD provinceIdXddd,"
//				+ " u.PROVINCE_NAME_XDDD provinceNameXddd, ");
		sqlBuilder.append(
				" (select EMPLOYEE_CODE || '-' || FULL_NAME from sys_user where sys_user_id = u.PARENT_USER_ID) parentName, ");
		sqlBuilder.append(
				" (select PHONE_NUMBER from sys_user where sys_user_id = u.PARENT_USER_ID) parentPhone ");
//		sqlBuilder.append(
//				" (select EMPLOYEE_CODE || '-' || FULL_NAME from sys_user where sys_user_id = u.PARENT_USER_ID) sysUserName ");
		sqlBuilder.append(",u.FIELD_TYPE fieldType ");
		sqlBuilder.append(",u.PROVINCE_ID_CTV_XDDD provinceIdCtvXddd, u.PROVINCE_NAME_CTV_XDDD provinceNameCtvXddd, u.COMMUNE_NAME communeName, u.DISTRICT_NAME districtName ");
		sqlBuilder.append(" from SYS_USER u, ");
		sqlBuilder.append(
				"      SYS_GROUP sg left outer join (select SYS_GROUP_ID, CODE,NAME,PROVINCE_ID from sys_group where group_level = 2) sglv2");
		sqlBuilder.append("          on instr(sg.PATH, sglv2.SYS_GROUP_ID) > 0");
		sqlBuilder.append(" where 1=1 ");
		sqlBuilder.append("     and u.SYS_GROUP_ID = sg.SYS_GROUP_ID ");
		sqlBuilder.append("     and instr(sg.PATH, sglv2.SYS_GROUP_ID) > 0 ");
		if (StringUtils.isNotEmpty(criteria.getKeySearch())) {
			sqlBuilder.append(" and (upper(u.FULL_NAME) LIKE upper(:keySearch) escape  :escapeChar ");
			sqlBuilder.append(" or upper(u.EMAIL) LIKE upper(:keySearch) escape :escapeChar ");
			sqlBuilder.append(" or upper(u.EMPLOYEE_CODE) LIKE upper(:keySearch) escape  :escapeChar ");
			sqlBuilder.append(" or upper(u.LOGIN_NAME) LIKE upper(:keySearch) escape  :escapeChar ");
			sqlBuilder.append(" or upper(u.PHONE_NUMBER) LIKE upper(:keySearch) escape  :escapeChar ) ");
		}
		if (criteria.getSysGroupId() != null) {
			sqlBuilder.append(" and sglv2.SYS_GROUP_ID = :sysGroupId ");
		}
		if (criteria.getStatus() != null) {
			sqlBuilder.append(" and u.STATUS = :status ");
		}

		if(criteria.getStartDate()!=null) {
			sqlBuilder.append(" and u.CREATED_DATE >= :startDate ");
		}
		
		if(criteria.getEndDate()!=null) {
			sqlBuilder.append(" and u.CREATED_DATE <= :endDate ");
		}
		
		sqlBuilder.append(" and sglv2.PROVINCE_ID in (:groupIdList) ");
		
		sqlBuilder.append(" and u.type_user IN (1) and u.OCCUPATION is null ");

		sqlBuilder.append("order by u.CREATED_DATE desc ");

		SQLQuery query = this.getSession().createSQLQuery(sqlBuilder.toString());
		SQLQuery queryCount = this.getSession().createSQLQuery("SELECT COUNT(*) FROM (" + sqlBuilder.toString() + ") ");
		if (StringUtils.isNotEmpty(criteria.getKeySearch())) {
			String keySearch = criteria.getKeySearch().replace("\\", HIBERNATE_ESCAPE_CHAR + "\\")
					.replace("_", HIBERNATE_ESCAPE_CHAR + "_").replace("%", HIBERNATE_ESCAPE_CHAR + "%");
			query.setParameter("keySearch", "%" + keySearch + "%");
			queryCount.setParameter("keySearch", "%" + keySearch + "%");
			query.setParameter("escapeChar", HIBERNATE_ESCAPE_CHAR);
			queryCount.setParameter("escapeChar", HIBERNATE_ESCAPE_CHAR);

		}
		if (criteria.getSysGroupId() != null) {
			query.setParameter("sysGroupId", criteria.getSysGroupId());
			queryCount.setParameter("sysGroupId", criteria.getSysGroupId());
		}
		if (criteria.getStatus() != null) {
			query.setParameter("status", criteria.getStatus());
			queryCount.setParameter("status", criteria.getStatus());
		}

		if(criteria.getStartDate()!=null) {
			query.setParameter("startDate", criteria.getStartDate());
			queryCount.setParameter("startDate", criteria.getStartDate());
		}
		
		if(criteria.getEndDate()!=null) {
			query.setParameter("endDate", criteria.getEndDate());
			queryCount.setParameter("endDate", criteria.getEndDate());
		}
		
		query.setParameterList("groupIdList", groupIdList);
		queryCount.setParameterList("groupIdList", groupIdList);
		
		query.addScalar("sysUserId", new LongType());
		query.addScalar("loginName", new StringType());
		query.addScalar("fullName", new StringType());
		query.addScalar("password", new StringType());
		query.addScalar("employeeCode", new StringType());
		query.addScalar("email", new StringType());
		query.addScalar("phoneNumber", new StringType());
		query.addScalar("status", new LongType());
		query.addScalar("newId", new LongType());
		query.addScalar("changePasswordDate", new DateType());
		query.addScalar("needChangePassword", new LongType());
		query.addScalar("sysGroupId", new LongType());
		query.addScalar("sysGroupName", new StringType());
		query.addScalar("sysGroupLv2Code", new StringType());
		query.addScalar("saleChannel", new StringType());
		query.addScalar("parentUserId", new LongType());
		query.addScalar("typeUser", new LongType());
		query.addScalar("address", new StringType());
//		query.addScalar("sysUserName", new StringType());
		query.addScalar("createdDate", new DateType());
		query.addScalar("userBirthday", new DateType());
		query.addScalar("taxCode", new StringType());
		query.addScalar("accountNumber", new StringType());
		query.addScalar("taxCodeUser", new StringType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("parentName", new StringType());
		query.addScalar("parentPhone", new StringType());
		query.addScalar("provinceIdXddd", new LongType());
		query.addScalar("provinceNameXddd", new StringType());
		query.addScalar("fieldType", new StringType());
		query.addScalar("provinceIdCtvXddd", new LongType());
		query.addScalar("provinceNameCtvXddd", new StringType());
		query.addScalar("districtName", new StringType());
		query.addScalar("communeName", new StringType());
		
		query.setResultTransformer(Transformers.aliasToBean(AIOSysUserDTO.class));
		this.setPageSize(criteria, query, queryCount);
		List<AIOSysUserDTO> ls = query.list();
		return ls;
	}

	public AIOSysUserDTO getSysUserByTaxCodeAndPhoneNum(String taxCode) {
		String sql = "select " + "su.SYS_USER_ID sysUserId, " + "su.EMPLOYEE_CODE employeeCode, "
				+ "su.PHONE_NUMBER phoneNumber, " + "su.tax_code taxCode " +

				"from sys_user su " + "where tax_code = :taxCode " +
				// "or PHONE_NUMBER = :phoneNumber) " +
				"and type_user is not null " + "and type_user in (1,2) " + "and status!=0 ";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setResultTransformer(Transformers.aliasToBean(AIOSysUserDTO.class));

		query.addScalar("sysUserId", new LongType());
		query.addScalar("employeeCode", new StringType());
		query.addScalar("phoneNumber", new StringType());
		query.addScalar("taxCode", new StringType());
		query.setParameter("taxCode", taxCode);
		// query.setParameter("phoneNumber", phoneNumber, new StringType());

		List list = query.list();
		if (list != null && !list.isEmpty()) {
			return (AIOSysUserDTO) (list.get(0));
		}
		return null;
	}

	public AIOSysUserDTO getUserIdByPhone(String tel) {
		String sql = "SELECT SYS_USER_ID sysUserId, SYS_GROUP_ID sysGroupId from SYS_USER " + "where " + "status = 1 "
				+ "and (type_user is null or type_user = 0) " + "and (case when PHONE_NUMBER like '84%' then '0'||ltrim(PHONE_NUMBER, '84') " + 
						"                when PHONE_NUMBER not like '0%' then '0'||PHONE_NUMBER " + 
						"                else PHONE_NUMBER end) = :tel ";

		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.setParameter("tel", tel);
		query.addScalar("sysUserId", new LongType());
		query.addScalar("sysGroupId", new LongType());
		query.setResultTransformer(Transformers.aliasToBean(AIOSysUserDTO.class));

		List<AIOSysUserDTO> list = query.list();
		if(list.size()>0) {
			return list.get(0);
		}
		return null;
	}
	
	public List<AIOSysGroupDTO> getGroupTree(AIOSysGroupDTO dto, String path, List<Long> level) {
		StringBuilder sql = new StringBuilder("select " + "SYS_GROUP_ID sysGroupId, " + "CODE code, " + "NAME name, "
				+ "PARENT_ID parentId, " + "GROUP_LEVEL groupLevel " + "from sys_group where path like :path "
				+ "and GROUP_LEVEL in (:level) " + "and status = 1 " + "and upper(code) like '%CNCT%' ");
		if (StringUtils.isNotBlank(dto.getName())) {
			sql.append(" AND (upper(CODE) like upper(:name) OR upper(NAME) like upper(:name) escape '&' ) ");
		}
		
		if (StringUtils.isNotBlank(dto.getCode())) {
			sql.append(" AND (upper(CODE) like upper(:code) OR upper(NAME) like upper(:code) escape '&' ) ");
		}

		sql.append(" order by SYS_GROUP_ID ");

		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = this.getSession().createSQLQuery("SELECT COUNT(*) FROM (" + sql.toString() + ") ");
		
		query.setResultTransformer(Transformers.aliasToBean(AIOSysGroupDTO.class));
		
		query.addScalar("sysGroupId", new LongType());
		query.addScalar("code", new StringType());
		query.addScalar("name", new StringType());
		query.addScalar("parentId", new LongType());
		query.addScalar("groupLevel", new StringType());
		
		query.setParameterList("level", level);
		queryCount.setParameterList("level", level);
		
		query.setParameter("path", path);
		queryCount.setParameter("path", path);
		
		if(StringUtils.isNotBlank(dto.getName())) {
	        query.setParameter("name", "%" + dto.getName() + "%");
	        queryCount.setParameter("name", "%" + dto.getName() + "%");
	    }
		
		if (StringUtils.isNotBlank(dto.getCode())) {
			query.setParameter("code", "%" + dto.getCode() + "%");
	        queryCount.setParameter("code", "%" + dto.getCode() + "%");
		}
		
		this.setPageSize(dto, query, queryCount);
		return query.list();
	}
	
	public AIOSysUserDTO getDataById(Long id) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(" select ");
		sqlBuilder.append(" u.SYS_USER_ID sysUserId, ");
		sqlBuilder.append(" u.LOGIN_NAME loginName, ");
		sqlBuilder.append(" u.FULL_NAME fullName, ");
		sqlBuilder.append(" u.PASSWORD password, ");
		sqlBuilder.append(" u.EMPLOYEE_CODE employeeCode, ");
		sqlBuilder.append(" u.EMAIL email, ");
		sqlBuilder.append(" u.PHONE_NUMBER phoneNumber, ");
		sqlBuilder.append(" u.STATUS status, ");
		sqlBuilder.append(" u.NEW_ID newId, ");
		sqlBuilder.append(" u.CHANGE_PASSWORD_DATE changePasswordDate, ");
		sqlBuilder.append(" u.NEED_CHANGE_PASSWORD needChangePassword, ");
		sqlBuilder.append(" u.SYS_GROUP_ID sysGroupId, ");
		sqlBuilder.append(" u.SALE_CHANNEL saleChannel, ");
		sqlBuilder.append(" u.PARENT_USER_ID parentUserId, ");
		sqlBuilder.append(" u.type_user typeUser, ");
		sqlBuilder.append(" u.ADDRESS address, ");
		sqlBuilder.append(" u.CREATED_DATE createdDate, ");
		sqlBuilder.append(" to_char(u.CREATED_DATE,'dd/MM/yyyy HH24:Mi:SS') createdDateDb,");
		sqlBuilder.append(" u.USER_BIRTHDAY userBirthday, ");
		sqlBuilder.append(" u.TAX_CODE taxCode,"
				+ " u.TAX_CODE_USER taxCodeUser,"
				+ " u.ACCOUNT_NUMBER accountNumber,"
				+ " u.CONTRACT_CODE contractCode, "
				+ " u.PROVINCE_ID_XDDD provinceIdXddd,"
				+ " u.PROVINCE_NAME_XDDD provinceNameXddd, u.FIELD_TYPE fieldType ");
		sqlBuilder.append(" from SYS_USER u where u.SYS_USER_ID=:id ");
		
		SQLQuery query = getSession().createSQLQuery(sqlBuilder.toString());
		
		query.addScalar("sysUserId", new LongType());
		query.addScalar("loginName", new StringType());
		query.addScalar("fullName", new StringType());
		query.addScalar("password", new StringType());
		query.addScalar("employeeCode", new StringType());
		query.addScalar("email", new StringType());
		query.addScalar("phoneNumber", new StringType());
		query.addScalar("status", new LongType());
		query.addScalar("newId", new LongType());
		query.addScalar("changePasswordDate", new DateType());
		query.addScalar("needChangePassword", new LongType());
		query.addScalar("sysGroupId", new LongType());
		query.addScalar("saleChannel", new StringType());
		query.addScalar("parentUserId", new LongType());
		query.addScalar("typeUser", new LongType());
		query.addScalar("address", new StringType());
		query.addScalar("createdDate", new DateType());
		query.addScalar("userBirthday", new DateType());
		query.addScalar("taxCode", new StringType());
		query.addScalar("taxCodeUser", new StringType());
		query.addScalar("accountNumber", new StringType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("provinceIdXddd", new LongType());
		query.addScalar("provinceNameXddd", new StringType());
		query.addScalar("createdDateDb", new StringType());
		query.addScalar("fieldType", new StringType());
		
		query.setResultTransformer(Transformers.aliasToBean(AIOSysUserDTO.class));
		
		query.setParameter("id", id);
		
		List<AIOSysUserDTO> ls = query.list();
		if(ls.size()>0) {
			return ls.get(0);
		}
		return null;
	}
	
	public Long removeRecord(Long id) {
		StringBuilder sql = new StringBuilder("UPDATE SYS_USER set STATUS=0 where SYS_USER_ID=:id");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("id", id);
		return (long) query.executeUpdate();
	}
	// Huy-end
}
