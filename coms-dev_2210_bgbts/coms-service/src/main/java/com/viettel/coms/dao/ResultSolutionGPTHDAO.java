package com.viettel.coms.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.viettel.coms.bo.ResultSolutionBO;
import com.viettel.coms.bo.ResultSolutionGPTHBO;
import com.viettel.coms.dto.ResultSolutionGPTHDTO;
import com.viettel.coms.dto.ResultSolutionGPTHDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@EnableTransactionManagement
@Transactional
@Repository("resultSolutionGPTHDAO")
public class ResultSolutionGPTHDAO extends BaseFWDAOImpl<ResultSolutionGPTHBO, Long> {

	public ResultSolutionGPTHDAO() {
		this.model = new ResultSolutionGPTHBO();
	}

	public ResultSolutionGPTHDAO(Session session) {
		this.session = session;
	}

	public List<ResultSolutionGPTHDTO> getAllContractXdddSuccess(ResultSolutionGPTHDTO obj, Long sysGroupId) {
		StringBuilder sql = new StringBuilder(" SELECT CNT_CONTRACT_ID contractId,  C.code contractCode, C.SIGN_DATE signDate, C.PRICE contractPrice, C.WARRANTY_NUMBER_DAY guaranteeTime, C.DEPLOYMENT_DATE constructTime  ");
		sql.append(" FROM cnt_contract C  ");
		sql.append( " WHERE C.CONTRACT_TYPE =0 AND C.status! =0   AND C.CONTRACT_BRANCH = 1 AND (CNT_CONTRACT_APPROVE =1 OR CNT_CONTRACT_APPROVE IS NULL ) AND CNT_CONTRACT_ID NOT IN ( SELECT NVL(CONTRACT_ID,0)  FROM RESULT_SOLUTION ) " );
		
//		StringBuilder sql = new StringBuilder("SELECT CNT_CONTRACT_ID contractId, " + "  C.code contractCode, "
//				+ "  C.SIGN_DATE signDate, " + "  C.PRICE contractPrice, " + "  C.WARRANTY_NUMBER_DAY guaranteeTime, "
//				+ "  C.DEPLOYMENT_DATE constructTime " + "FROM cnt_contract C " + "WHERE C.CONTRACT_TYPE      =0 "
//				+ "  AND C.status!              =0 " + "  AND C.CONTRACT_TYPE_O      =4 " +
//				"  AND C.channel              =2 "
//				+ " AND C.CONTRACT_BRANCH = 1 " +
//				"  AND (CNT_CONTRACT_APPROVE =1 " + "  OR CNT_CONTRACT_APPROVE IS NULL)"
//				+ "  AND CNT_CONTRACT_ID NOT IN " + "  (SELECT NVL(CONTRACT_ID,0) FROM RESULT_SOLUTION " + "   "
//				+ "  AND SYS_GROUP_ID= (select case when sys.group_level=4 then  "
//				+ "           (select sys_group_id from sys_group a where a.sys_group_id=  "
//				+ "                 (select parent_id from sys_group a where a.sys_group_id=sys.parent_id))  "
//				+ "                 when sys.group_level=3 then  "
//				+ "                 (select sys_group_id from sys_group a where a.sys_group_id=sys.parent_id)   else sys_group_id   "
//				+ "                 end sys_group_id   from sys_group sys  "
//				+ "                  where sys_group_id = :sysGroupId)");
//				+ "                  where 1 = 1)");
		if (StringUtils.isNoneBlank(obj.getContractCode())) {
			sql.append(" AND upper(CODE) like upper(:code) escape '&' ");
		}
//		if (obj.getCreatedDate() != null) {
//			sql.append(" AND C.SIGN_DATE > :createdDate ");
//		}
		sql.append(" ORDER BY CODE ");
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		query.addScalar("contractId", new LongType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("signDate", new DateType());
		query.addScalar("contractPrice", new DoubleType());
		query.addScalar("guaranteeTime", new StringType());
		query.addScalar("constructTime", new DateType());

//		query.setParameter("sysGroupId", sysGroupId);
//		queryCount.setParameter("sysGroupId", sysGroupId);

		if (StringUtils.isNoneBlank(obj.getContractCode())) {
			query.setParameter("code", "%" + obj.getContractCode() + "%");
			queryCount.setParameter("code", "%" + obj.getContractCode() + "%");
		}
//		if (obj.getCreatedDate() != null) {
//			query.setParameter("createdDate", obj.getCreatedDate());
//			queryCount.setParameter("createdDate", obj.getCreatedDate());
//		}
		query.setResultTransformer(Transformers.aliasToBean(ResultSolutionGPTHDTO.class));

		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
		return query.list();
	}

	public ResultSolutionGPTHDTO getResultSolutionByContractId(Long contractId) {
		StringBuilder sql = new StringBuilder("select ");
		sql.append("RESULT_SOLUTION_ID resultSolutionId, ").append("TANGENT_CUSTOMER_ID tangentCustomerId, ")
				.append("RESULT_SOLUTION_ORDER resultSolutionOrder, ")
				.append("PRESENT_SOLUTION_DATE presentSolutionDate, ")
				.append("RESULT_SOLUTION_TYPE resultSolutionType, ").append("CONTRACT_ID contractId, ")
				.append("CONTRACT_CODE contractCode, ").append("SIGN_DATE signDate, ")
				.append("GUARANTEE_TIME guaranteeTime, ").append("CONSTRUCT_TIME constructTime, ")
				.append("CONTRACT_ROSE contractRose, ").append("CONTENT_RESULT_SOLUTION contentResultSolution, ")
				.append("APPROVED_PERFORMER_ID approvedPerformerId, ")
				.append("APPROVED_DESCRIPTION approvedDescription, ").append("APPROVED_USER_ID approvedUserId, ")
				.append("APPROVED_DATE approvedDate, ").append("PRESENT_SOLUTION_DATE_NEXT presentSolutionDateNext, ")
				.append("PERFORMER_ID performerId, ").append("CREATED_USER createdUser, ")
				.append("CREATED_DATE createdDate, ")
				.append("to_char(CREATED_DATE,'dd/MM/yyyy HH24:Mi:SS') createdDateDb, ")
				.append("UPDATED_USER updatedUser, ").append("UPDATED_DATE updatedDate, ")
				.append("APPROVED_STATUS approvedStatus, ").append("CONTRACT_PRICE contractPrice, ")
				.append("REALITY_SOLUTION_DATE realitySolutionDate, ")			
				.append("SIGN_STATUS signStatus, ")
				.append("UNSECCESSFULL_REASON unsuccessfullReason ")
				
				.append("FROM RESULT_SOLUTION where CONTRACT_ID=:contractId ");

		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("resultSolutionId", new LongType());
		query.addScalar("tangentCustomerId", new LongType());
		query.addScalar("resultSolutionOrder", new LongType());
		query.addScalar("presentSolutionDate", new DateType());
		query.addScalar("resultSolutionType", new StringType());
		query.addScalar("contractId", new LongType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("signDate", new DateType());
		query.addScalar("guaranteeTime", new StringType());
		query.addScalar("constructTime", new DateType());
		query.addScalar("contractRose", new DoubleType());
		query.addScalar("contentResultSolution", new StringType());
		query.addScalar("approvedPerformerId", new LongType());
		query.addScalar("approvedDescription", new StringType());
		query.addScalar("approvedUserId", new LongType());
		query.addScalar("approvedDate", new DateType());
		query.addScalar("presentSolutionDateNext", new DateType());
		query.addScalar("performerId", new LongType());
		query.addScalar("createdUser", new LongType());
		query.addScalar("createdDate", new DateType());
		query.addScalar("updatedUser", new LongType());
		query.addScalar("updatedDate", new DateType());
		query.addScalar("approvedStatus", new StringType());
		query.addScalar("contractPrice", new DoubleType());
		query.addScalar("realitySolutionDate", new DateType());
		query.addScalar("createdDateDb", new StringType());
		query.addScalar("signStatus", new LongType());
		List<ResultSolutionGPTHDTO> ls = query.list();
		if (ls.size() > 0) {
			return ls.get(0);
		}
		return null;
	}

	public List<ResultSolutionGPTHDTO> getResultSolutionByTangentCustomerId(Long customerId) {
		StringBuilder sql = new StringBuilder("select ");
		sql.append("RESULT_SOLUTION_ID resultSolutionId, ").append("TANGENT_CUSTOMER_ID tangentCustomerId, ")
				.append("RESULT_SOLUTION_ORDER resultSolutionOrder, ")
				.append("PRESENT_SOLUTION_DATE presentSolutionDate, ")
				.append("RESULT_SOLUTION_TYPE resultSolutionType, ").append("CONTRACT_ID contractId, ")
				.append("CONTRACT_CODE contractCode, ").append("SIGN_DATE signDate, ")
				.append("GUARANTEE_TIME guaranteeTime, ").append("CONSTRUCT_TIME constructTime, ")
				.append("CONTRACT_ROSE contractRose, ").append("CONTENT_RESULT_SOLUTION contentResultSolution, ")
				.append("APPROVED_PERFORMER_ID approvedPerformerId, ")
				.append("APPROVED_DESCRIPTION approvedDescription, ").append("APPROVED_USER_ID approvedUserId, ")
				.append("APPROVED_DATE approvedDate, ").append("PRESENT_SOLUTION_DATE_NEXT presentSolutionDateNext, ")
				.append("PERFORMER_ID performerId, ").append("CREATED_USER createdUser, ")
				.append("CREATED_DATE createdDate, ")
				.append("to_char(CREATED_DATE,'dd/MM/yyyy HH24:Mi:SS') createdDateDb, ")
				.append("UPDATED_USER updatedUser, ").append("UPDATED_DATE updatedDate, ")
				.append("APPROVED_STATUS approvedStatus, ").append("contract_Price contractPrice, ")
				.append("REALITY_SOLUTION_DATE realitySolutionDate, ")
				.append("SIGN_STATUS signStatus, ")
				.append("UNSECCESSFULL_REASON unsuccessfullReason ")
				
				.append("FROM RESULT_SOLUTION where TANGENT_CUSTOMER_ID=:customerId ");
		sql.append(" ORDER BY RESULT_SOLUTION_ID DESC ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("resultSolutionId", new LongType());
		query.addScalar("tangentCustomerId", new LongType());
		query.addScalar("resultSolutionOrder", new LongType());
		query.addScalar("presentSolutionDate", new DateType());
		query.addScalar("resultSolutionType", new StringType());
		query.addScalar("contractId", new LongType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("signDate", new DateType());
		query.addScalar("guaranteeTime", new StringType());
		query.addScalar("constructTime", new DateType());
		query.addScalar("contractRose", new DoubleType());
		query.addScalar("contentResultSolution", new StringType());
		query.addScalar("approvedPerformerId", new LongType());
		query.addScalar("approvedDescription", new StringType());
		query.addScalar("approvedUserId", new LongType());
		query.addScalar("approvedDate", new DateType());
		query.addScalar("presentSolutionDateNext", new DateType());
		query.addScalar("performerId", new LongType());
		query.addScalar("createdUser", new LongType());
		query.addScalar("createdDate", new DateType());
		query.addScalar("updatedUser", new LongType());
		query.addScalar("updatedDate", new DateType());
		query.addScalar("approvedStatus", new StringType());
		query.addScalar("contractPrice", new DoubleType());
		query.addScalar("realitySolutionDate", new DateType());
		query.addScalar("createdDateDb", new StringType());
		query.addScalar("signStatus", new LongType());
		query.addScalar("unsuccessfullReason", new StringType());
		query.setParameter("customerId", customerId);

		query.setResultTransformer(Transformers.aliasToBean(ResultSolutionGPTHDTO.class));

		List<ResultSolutionGPTHDTO> ls = query.list();

		return ls;
	}

	public List<ResultSolutionGPTHDTO> getResultSolutionJoinSysUserByTangentCustomerId(Long customerId) {
		StringBuilder sql = new StringBuilder("select ");
		sql.append("rs.RESULT_SOLUTION_ID resultSolutionId, ").append("rs.TANGENT_CUSTOMER_ID tangentCustomerId, ")
				.append("rs.RESULT_SOLUTION_ORDER resultSolutionOrder, ")
				.append("rs.PRESENT_SOLUTION_DATE presentSolutionDate, ")
				.append("rs.RESULT_SOLUTION_TYPE resultSolutionType, ").append("rs.CONTRACT_ID contractId, ")
				.append("rs.CONTRACT_CODE contractCode, ").append("rs.SIGN_DATE signDate, ")
				.append("rs.GUARANTEE_TIME guaranteeTime, ").append("rs.CONSTRUCT_TIME constructTime, ")
				.append("rs.CONTRACT_ROSE contractRose, ").append("rs.CONTENT_RESULT_SOLUTION contentResultSolution, ")
				.append("rs.APPROVED_PERFORMER_ID approvedPerformerId, ")
				.append("rs.APPROVED_DESCRIPTION approvedDescription, ").append("rs.APPROVED_USER_ID approvedUserId, ")
				.append("rs.APPROVED_DATE approvedDate, ")
				.append("rs.PRESENT_SOLUTION_DATE_NEXT presentSolutionDateNext, ")
				.append("rs.PERFORMER_ID performerId, ").append("rs.CREATED_USER createdUser, ")
				.append("rs.CREATED_DATE createdDate, ").append("rs.UPDATED_USER updatedUser, ")
				.append("rs.UPDATED_DATE updatedDate, ").append("rs.APPROVED_STATUS approvedStatus, ")
				.append("to_char(rs.CREATED_DATE,'dd/MM/yyyy HH24:Mi:SS') createdDateDb, ")
				.append("su1.FULL_NAME approvedPerformerName, ").append("su2.FULL_NAME approvedUserName, ")
				.append("su3.FULL_NAME performerName, ")

				.append("su3.EMAIL performerEmail, ").append("su3.PHONE_NUMBER performerPhone, ")
				.append("rs.CONTRACT_PRICE contractPrice, ").append("rs.REALITY_SOLUTION_DATE realitySolutionDate, ")
				.append("rs.SIGN_STATUS signStatus, ")
				.append("rs.UNSECCESSFULL_REASON unsuccessfullReason ")
				.append("FROM RESULT_SOLUTION rs ")
				.append("LEFT JOIN SYS_USER su1 on rs.APPROVED_PERFORMER_ID = su1.SYS_USER_ID ")
				.append("LEFT JOIN SYS_USER su2 on rs.APPROVED_USER_ID = su2.SYS_USER_ID ")
				.append("LEFT JOIN SYS_USER su3 on rs.PERFORMER_ID = su3.SYS_USER_ID ")
				.append("where rs.TANGENT_CUSTOMER_ID=:customerId ");
		sql.append(" ORDER BY rs.RESULT_SOLUTION_ID DESC ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("resultSolutionId", new LongType());
		query.addScalar("tangentCustomerId", new LongType());
		query.addScalar("resultSolutionOrder", new LongType());
		query.addScalar("presentSolutionDate", new DateType());
		query.addScalar("resultSolutionType", new StringType());
		query.addScalar("contractId", new LongType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("signDate", new DateType());
		query.addScalar("guaranteeTime", new StringType());
		query.addScalar("constructTime", new DateType());
		query.addScalar("contractRose", new DoubleType());
		query.addScalar("contentResultSolution", new StringType());
		query.addScalar("approvedPerformerId", new LongType());
		query.addScalar("approvedDescription", new StringType());
		query.addScalar("approvedUserId", new LongType());
		query.addScalar("approvedDate", new DateType());
		query.addScalar("presentSolutionDateNext", new DateType());
		query.addScalar("performerId", new LongType());
		query.addScalar("createdUser", new LongType());
		query.addScalar("createdDate", new DateType());
		query.addScalar("updatedUser", new LongType());
		query.addScalar("updatedDate", new DateType());
		query.addScalar("approvedStatus", new StringType());
		query.addScalar("approvedPerformerName", new StringType());
		query.addScalar("approvedUserName", new StringType());
		query.addScalar("performerName", new StringType());
		query.addScalar("performerEmail", new StringType());
		query.addScalar("performerPhone", new StringType());
		query.addScalar("contractPrice", new DoubleType());
		query.addScalar("realitySolutionDate", new DateType());
		query.addScalar("createdDateDb", new StringType());
		query.addScalar("signStatus", new LongType());
		query.addScalar("unsuccessfullReason", new StringType());
		query.setParameter("customerId", customerId);

		query.setResultTransformer(Transformers.aliasToBean(ResultSolutionGPTHDTO.class));

		List<ResultSolutionGPTHDTO> ls = query.list();

		return ls;
	}

	public ResultSolutionGPTHDTO getResultSolutionJoinSysUserByResultSolutionId(Long solutionId) {
		StringBuilder sql = new StringBuilder("select ");
		sql.append("rs.RESULT_SOLUTION_ID resultSolutionId, ").append("rs.TANGENT_CUSTOMER_ID tangentCustomerId, ")
				.append("rs.RESULT_SOLUTION_ORDER resultSolutionOrder, ")
				.append("rs.PRESENT_SOLUTION_DATE presentSolutionDate, ")
				.append("rs.RESULT_SOLUTION_TYPE resultSolutionType, ").append("rs.CONTRACT_ID contractId, ")
				.append("rs.CONTRACT_CODE contractCode, ").append("rs.SIGN_DATE signDate, ")
				.append("rs.GUARANTEE_TIME guaranteeTime, ").append("rs.CONSTRUCT_TIME constructTime, ")
				.append("rs.CONTRACT_ROSE contractRose, ").append("rs.CONTENT_RESULT_SOLUTION contentResultSolution, ")
				.append("rs.APPROVED_PERFORMER_ID approvedPerformerId, ")
				.append("rs.APPROVED_DESCRIPTION approvedDescription, ").append("rs.APPROVED_USER_ID approvedUserId, ")
				.append("rs.APPROVED_DATE approvedDate, ")
				.append("rs.PRESENT_SOLUTION_DATE_NEXT presentSolutionDateNext, ")
				.append("rs.PERFORMER_ID performerId, ").append("rs.CREATED_USER createdUser, ")
				.append("rs.CREATED_DATE createdDate, ").append("rs.UPDATED_USER updatedUser, ")
				.append("rs.UPDATED_DATE updatedDate, ").append("rs.APPROVED_STATUS approvedStatus, ")
				.append("to_char(rs.CREATED_DATE,'dd/MM/yyyy HH24:Mi:SS') createdDateDb, ")
				.append("su1.FULL_NAME approvedPerformerName, ").append("su2.FULL_NAME approvedUserName, ")
				.append("su3.FULL_NAME performerName, ")

				.append("su3.EMAIL performerEmail, ").append("su3.PHONE_NUMBER performerPhone, ")
				.append("rs.CONTRACT_PRICE contractPrice, ").append("rs.REALITY_SOLUTION_DATE realitySolutionDate, ")
				.append("rs.SIGN_STATUS signStatus, ")
				.append("rs.UNSECCESSFULL_REASON unsuccessfullReason ")
				.append("FROM RESULT_SOLUTION rs ")
				.append("LEFT JOIN SYS_USER su1 on rs.APPROVED_PERFORMER_ID = su1.SYS_USER_ID ")
				.append("LEFT JOIN SYS_USER su2 on rs.APPROVED_USER_ID = su2.SYS_USER_ID ")
				.append("LEFT JOIN SYS_USER su3 on rs.PERFORMER_ID = su3.SYS_USER_ID ")
				.append("where rs.RESULT_SOLUTION_ID=:solutionId ");
		sql.append(" ORDER BY rs.RESULT_SOLUTION_ID DESC ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("resultSolutionId", new LongType());
		query.addScalar("tangentCustomerId", new LongType());
		query.addScalar("resultSolutionOrder", new LongType());
		query.addScalar("presentSolutionDate", new DateType());
		query.addScalar("resultSolutionType", new StringType());
		query.addScalar("contractId", new LongType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("signDate", new DateType());
		query.addScalar("guaranteeTime", new StringType());
		query.addScalar("constructTime", new DateType());
		query.addScalar("contractRose", new DoubleType());
		query.addScalar("contentResultSolution", new StringType());
		query.addScalar("approvedPerformerId", new LongType());
		query.addScalar("approvedDescription", new StringType());
		query.addScalar("approvedUserId", new LongType());
		query.addScalar("approvedDate", new DateType());
		query.addScalar("presentSolutionDateNext", new DateType());
		query.addScalar("performerId", new LongType());
		query.addScalar("createdUser", new LongType());
		query.addScalar("createdDate", new DateType());
		query.addScalar("updatedUser", new LongType());
		query.addScalar("updatedDate", new DateType());
		query.addScalar("approvedStatus", new StringType());
		query.addScalar("approvedPerformerName", new StringType());
		query.addScalar("approvedUserName", new StringType());
		query.addScalar("performerName", new StringType());
		query.addScalar("performerEmail", new StringType());
		query.addScalar("performerPhone", new StringType());
		query.addScalar("contractPrice", new DoubleType());
		query.addScalar("realitySolutionDate", new DateType());
		query.addScalar("createdDateDb", new StringType());
		query.addScalar("signStatus", new LongType());
		query.addScalar("unsuccessfullReason", new StringType());
		query.setParameter("solutionId", solutionId);

		query.setResultTransformer(Transformers.aliasToBean(ResultSolutionGPTHDTO.class));

		List<ResultSolutionGPTHDTO> ls = query.list();
		if (ls.size() > 0) {
			return ls.get(0);
		}
		return null;
	}

	public List<ResultSolutionBO> getResultSolutionByTangentCustomerId(ResultSolutionGPTHDTO obj) {
		StringBuilder sql = new StringBuilder(" SELECT * from RESULT_SOLUTION where TANGENT_CUSTOMER_ID=:tangentId ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addEntity(ResultSolutionBO.class);
		query.setParameter("tangentId", obj.getTangentCustomerGPTHId());
		return query.list();
	}

	// duonghv13 add 29122021
	public ResultSolutionGPTHDTO getResultSolutionFieldByTargentCustomerId(Long tangentCustomerId) {
		StringBuilder sql = new StringBuilder("select ");
		sql.append("RESULT_SOLUTION_GPTH_ID resultSolutionGPTHId, ").append("TANGENT_CUSTOMER_GPTH_ID tangentCustomerGPTHId, ")
				.append("PRESENT_SOLUTION_DATE presentSolutionDate, ").append("SIGN_DATE signDate, ")
				.append("CREATED_DATE createdDate, ")
				.append("to_char(CREATED_DATE,'dd/MM/yyyy HH24:Mi:SS') createdDateDb, ")
				.append("to_char(UPDATED_DATE,'dd/MM/yyyy HH24:Mi:SS') updatedDateDb, ") 
				.append("PRESENT_SOLUTION_DATE_NEXT presentSolutionDateNext, ")

				.append("CONTRACT_ID contractId ")
				.append("FROM RESULT_SOLUTION_GPTH where TANGENT_CUSTOMER_GPTH_ID = :tangentCustomerId ");
		sql.append(" ORDER BY RESULT_SOLUTION_GPTH_ID DESC FETCH FIRST 1 ROW ONLY ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("resultSolutionGPTHId", new LongType());
		query.addScalar("tangentCustomerGPTHId", new LongType());
		query.addScalar("presentSolutionDate", new DateType());
		query.addScalar("signDate", new DateType());
		query.addScalar("createdDate", new DateType());
		query.addScalar("createdDateDb", new StringType());
		query.addScalar("updatedDateDb", new StringType());
		query.addScalar("presentSolutionDateNext", new DateType());
		query.addScalar("contractId", new LongType());

		query.setParameter("tangentCustomerId", tangentCustomerId);

		query.setResultTransformer(Transformers.aliasToBean(ResultSolutionGPTHDTO.class));

		List<ResultSolutionGPTHDTO> ls = query.list();
		if (ls.size() > 0) {
			return ls.get(0);
		}
		return null;
	}

	public List<ResultSolutionGPTHDTO> getAllContractXdddSuccessInternal(ResultSolutionGPTHDTO obj, Long sysGroupId){
		StringBuilder sql = new StringBuilder("SELECT C.CNT_CONTRACT_ID contractId, " + 
				"  C.CODE contractCode, " + 
				"  C.SIGN_DATE signDate, " + 
				"  C.PRICE contractPrice, " + 
				"  C.WARRANTY_NUMBER_DAY guaranteeTime, " + 
				"  C.DEPLOYMENT_DATE constructTime " + 
				" FROM cnt_contract C " +
				" WHERE C.CONTRACT_TYPE      = 0 " + 
				"  AND C.status!              = 0 " + 
				"  AND C.CONTRACT_TYPE_O      = 4 " + 
				"  AND C.channel              = 1 " +
				"  AND C.USER_SEARCH_ID IN ( SELECT SYS_USER_ID FROM SYS_USER WHERE TYPE_USER IS NULL ) " +
				"  AND C.USER_SEARCH_ID in (select Created_user from TANGENT_CUSTOMER) " +
				"  AND C.CNT_CONTRACT_APPROVE = 1 " +
				"  AND C.CNT_CONTRACT_ID NOT IN " + 
				"  (SELECT NVL(CONTRACT_ID,0) FROM RESULT_SOLUTION " + 
				"  ) " + 
				"  AND C.SYS_GROUP_ID = (select case when sys.group_level=4 then  " + 
				"           (select sys_group_id from sys_group a where a.sys_group_id=  " + 
				"                 (select parent_id from sys_group a where a.sys_group_id=sys.parent_id))  " + 
				"                 when sys.group_level=3 then  " + 
				"                 (select sys_group_id from sys_group a where a.sys_group_id=sys.parent_id)   else sys_group_id   " + 
				"                 end sys_group_id   from sys_group sys  " + 
				"                  where sys_group_id = :sysGroupId)");
		if(StringUtils.isNoneBlank(obj.getContractCode())) {
			sql.append(" AND upper(C.CODE) like upper(:code) escape '&' ");
		}

		if (obj.getCreatedDate() != null) {
			sql.append(" AND C.SIGN_DATE > :createdDate ");
		}
		sql.append(" ORDER BY C.CODE ");
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
		
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
		
		query.addScalar("contractId", new LongType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("signDate", new DateType());
		query.addScalar("contractPrice", new DoubleType());
		query.addScalar("guaranteeTime", new StringType());
		query.addScalar("constructTime", new DateType());
		
		query.setParameter("sysGroupId", sysGroupId);
		queryCount.setParameter("sysGroupId", sysGroupId);
		
		if(StringUtils.isNoneBlank(obj.getContractCode())) {
			query.setParameter("code", "%" + obj.getContractCode() + "%");
			queryCount.setParameter("code", "%" + obj.getContractCode() + "%");
		}
		if (obj.getCreatedDate() != null) {
	            query.setParameter("createdDate", obj.getCreatedDate());
	            queryCount.setParameter("createdDate", obj.getCreatedDate());
	    }
		query.setResultTransformer(Transformers.aliasToBean(ResultSolutionGPTHDTO.class));
		
		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
        return query.list();
	}
}
