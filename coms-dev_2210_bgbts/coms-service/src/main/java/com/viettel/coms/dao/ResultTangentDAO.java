package com.viettel.coms.dao;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.viettel.coms.bo.ResultTangentBO;
import com.viettel.coms.dto.ResultTangentDTO;
import com.viettel.coms.dto.UtilAttachDocumentDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@EnableTransactionManagement
@Transactional
@Repository("resultTangentDAO")
public class ResultTangentDAO extends BaseFWDAOImpl<ResultTangentBO, Long>{

	public ResultTangentDAO() {
		this.model = new ResultTangentBO();
	}

	public ResultTangentDAO(Session session) {
		this.session = session;
	}
	
	public List<ResultTangentBO> getResultTangentByTangentCustomerId(ResultTangentDTO obj){
		StringBuilder sql = new StringBuilder(" SELECT * from RESULT_TANGENT where TANGENT_CUSTOMER_ID=:tangentId ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addEntity(ResultTangentBO.class);
		query.setParameter("tangentId", obj.getTangentCustomerId());
		return query.list();
	}
	
	public ResultTangentDTO getMaxAppointmentDateByTangentCustomerId(ResultTangentDTO obj){
		StringBuilder sql = new StringBuilder(" SELECT T1.APPOINTMENT_DATE appointmentDate,T1.RESULT_TANGENT_ID resultTangentId,"
				+ " to_char(T1.CREATED_DATE,'dd/MM/yyyy HH24:Mi:SS') createdDateDb, "
				+ " to_char(T1.APPOINTMENT_DATE,'dd/MM/yyyy HH24:Mi:SS') appointmentDateDb, "
				+ " T1.RESULT_TANGENT_TYPE resultTangentType, "
				+ " to_char(T1.UPDATED_DATE,'dd/MM/yyyy HH24:Mi:SS') updatedDateDb, "
				+ " to_char(T2.CREATED_DATE,'dd/MM/yyyy HH24:Mi:SS') createdDateYes "
				+ " from RESULT_TANGENT T1 "
				+ " LEFT JOIN RESULT_TANGENT_DETAIL_YES T2 ON T2.RESULT_TANGENT_ID = T1.RESULT_TANGENT_ID ");
		sql.append(" where 1 = 1 and T1.TANGENT_CUSTOMER_ID =:tangentId ORDER BY T1.RESULT_TANGENT_ID DESC FETCH FIRST 1 ROW ONLY ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("appointmentDate", new DateType());
		query.addScalar("resultTangentId", new LongType());
		query.addScalar("createdDateDb", new StringType());
		query.addScalar("createdDateYes", new StringType());
		query.addScalar("appointmentDateDb", new StringType());
		query.addScalar("updatedDateDb", new StringType());
		query.addScalar("resultTangentType", new StringType());
		query.setParameter("tangentId", obj.getTangentCustomerId());
		query.setResultTransformer(Transformers.aliasToBean(ResultTangentDTO.class));
		List<ResultTangentDTO> ls = query.list();
		if(ls.size()>0) {
			return ls.get(0);
		}
		return null;
	}
	
	public List<ResultTangentDTO> getListResultTangentByTangentCustomerId(ResultTangentDTO obj){
		StringBuilder sql = new StringBuilder(" SELECT ");
		sql.append("RESULT_TANGENT_ID resultTangentId, ")
		.append("TANGENT_CUSTOMER_ID tangentCustomerId, ")
		.append("APPOINTMENT_DATE appointmentDate, ")
		.append("ORDER_RESULT_TANGENT orderResultTangent, ")
		.append("RESULT_TANGENT_TYPE resultTangentType, ")
		.append("REASON_REJECTION reasonRejection, ")
		.append("APPROVED_STATUS approvedStatus, ")
		.append("APPROVED_PERFORMER_ID approvedPerformerId, ")
		.append("APPROVED_DESCRIPTION approvedDescription, ")
		.append("APPROVED_USER_ID approvedUserId, ")
		.append("APPROVED_DATE approvedDate, ")
		.append("BUILDING_LOCATION buildingLocation, ")
		.append("PACKAGE_1 package1, ")
		.append("PACKAGE_2 package2, ")
		.append("PACKAGE_3 package3, ")
		.append("PACKAGE_4 package4, ")
		.append("PACKAGE_5 package5, ")
		.append("PACKAGE_6 package6, ")
		/*.append("PACKAGE_7 package7, ")
		.append("PACKAGE_8 package8, ")
		.append("PACKAGE_9 package9, ")*/
		.append("START_DATE startDate, ")
		.append("CONTRUCTION_RECORDS contructionRecords, ")
		.append("CONTRUCTION_DESIGN contructionDesign, ")
		.append("PERFORMER_ID performerId, ")
		.append("CREATED_USER createdUser, ")
		.append("CREATED_DATE createdDate, ")
		.append("to_char(CREATED_DATE,'dd/MM/yyyy HH24:Mi:SS') createdDateDb, ")
		.append("UPDATED_USER updatedUser, ")
		.append("UPDATED_DATE updatedDate, ")
		.append("REALITY_TANGENT_DATE realityTangentDate ")
		.append("FROM RESULT_TANGENT ")
		.append("WHERE 1=1 AND TANGENT_CUSTOMER_ID =:tangentId ");
		sql.append(" ORDER BY ORDER_RESULT_TANGENT DESC ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("resultTangentId", new LongType());
		query.addScalar("tangentCustomerId", new LongType());
		query.addScalar("appointmentDate", new DateType());
		query.addScalar("orderResultTangent", new StringType());
		query.addScalar("resultTangentType", new StringType());
		query.addScalar("reasonRejection", new StringType());
		query.addScalar("approvedStatus", new StringType());
		query.addScalar("approvedPerformerId", new LongType());
		query.addScalar("approvedDescription", new StringType());
		query.addScalar("approvedUserId", new LongType());
		query.addScalar("approvedDate", new DateType());
		query.addScalar("buildingLocation", new StringType());
		query.addScalar("package1", new StringType());
		query.addScalar("package2", new StringType());
		query.addScalar("package3", new StringType());
		query.addScalar("package4", new StringType());
		query.addScalar("package5", new StringType());
		query.addScalar("package6", new StringType());
		/*query.addScalar("package7", new StringType());
		query.addScalar("package8", new StringType());
		query.addScalar("package9", new StringType());*/
		query.addScalar("startDate", new DateType());
		query.addScalar("contructionRecords", new StringType());
		query.addScalar("contructionDesign", new StringType());
		query.addScalar("performerId", new LongType());
		query.addScalar("createdUser", new LongType());
		query.addScalar("createdDate", new DateType());
		query.addScalar("updatedUser", new LongType());
		query.addScalar("updatedDate", new DateType());
		query.addScalar("realityTangentDate", new DateType());
		query.addScalar("createdDateDb", new StringType());
		
		query.setResultTransformer(Transformers.aliasToBean(ResultTangentDTO.class));
		
		query.setParameter("tangentId", obj.getTangentCustomerId());
		
		return query.list();
	}
	
	public List<ResultTangentDTO> getListResultTangentJoinSysUserByTangentCustomerId(ResultTangentDTO obj){
		StringBuilder sql = new StringBuilder(" SELECT ");
		sql.append("rt.RESULT_TANGENT_ID resultTangentId, ")
		.append("rt.TANGENT_CUSTOMER_ID tangentCustomerId, ")
		.append("rt.APPOINTMENT_DATE appointmentDate, ")
		.append("rt.ORDER_RESULT_TANGENT orderResultTangent, ")
		.append("rt.RESULT_TANGENT_TYPE resultTangentType, ")
		.append("rt.REASON_REJECTION reasonRejection, ")
		.append("rt.APPROVED_STATUS approvedStatus, ")
		.append("rt.APPROVED_PERFORMER_ID approvedPerformerId, ")
		.append("rt.APPROVED_DESCRIPTION approvedDescription, ")
		.append("rt.APPROVED_USER_ID approvedUserId, ")
		.append("rt.APPROVED_DATE approvedDate, ")
		.append("rt.BUILDING_LOCATION buildingLocation, ")
		.append("rt.PACKAGE_1 package1, ")
		.append("rt.PACKAGE_2 package2, ")
		.append("rt.PACKAGE_3 package3, ")
		.append("rt.PACKAGE_4 package4, ")
		.append("rt.PACKAGE_5 package5, ")
		.append("rt.PACKAGE_6 package6, ")
		/*.append("rt.PACKAGE_7 package7, ")
		.append("rt.PACKAGE_8 package8, ")
		.append("rt.PACKAGE_9 package9, ")*/
		.append("rt.START_DATE startDate, ")
		.append("rt.CONTRUCTION_RECORDS contructionRecords, ")
		.append("rt.CONTRUCTION_DESIGN contructionDesign, ")
		.append("rt.PERFORMER_ID performerId, ")
		.append("rt.CREATED_USER createdUser, ")
		.append("rt.CREATED_DATE createdDate, ")
		.append("to_char(rt.CREATED_DATE,'dd/MM/yyyy HH24:Mi:SS') createdDateDb, ")
		.append("rt.UPDATED_USER updatedUser, ")
		.append("rt.UPDATED_DATE updatedDate, ")
		.append("su1.FULL_NAME approvedPerformerName, ")
		.append("su2.FULL_NAME approvedUserName, ")
		.append("su3.FULL_NAME performerName, ")
		.append("REALITY_TANGENT_DATE realityTangentDate ")
		.append("FROM RESULT_TANGENT rt ")
		.append("LEFT JOIN SYS_USER su1 on rt.APPROVED_PERFORMER_ID = su1.SYS_USER_ID ")
		.append("LEFT JOIN SYS_USER su2 on rt.APPROVED_USER_ID = su2.SYS_USER_ID ")
		.append("LEFT JOIN SYS_USER su3 on rt.PERFORMER_ID = su3.SYS_USER_ID ")
		.append("WHERE 1=1 AND rt.TANGENT_CUSTOMER_ID =:tangentId ");
		sql.append(" ORDER BY nvl(rt.ORDER_RESULT_TANGENT,0) DESC ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("resultTangentId", new LongType());
		query.addScalar("tangentCustomerId", new LongType());
		query.addScalar("appointmentDate", new DateType());
		query.addScalar("orderResultTangent", new StringType());
		query.addScalar("resultTangentType", new StringType());
		query.addScalar("reasonRejection", new StringType());
		query.addScalar("approvedStatus", new StringType());
		query.addScalar("approvedPerformerId", new LongType());
		query.addScalar("approvedDescription", new StringType());
		query.addScalar("approvedUserId", new LongType());
		query.addScalar("approvedDate", new DateType());
		query.addScalar("buildingLocation", new StringType());
		query.addScalar("package1", new StringType());
		query.addScalar("package2", new StringType());
		query.addScalar("package3", new StringType());
		query.addScalar("package4", new StringType());
		query.addScalar("package5", new StringType());
		query.addScalar("package6", new StringType());
		/*query.addScalar("package7", new StringType());
		query.addScalar("package8", new StringType());
		query.addScalar("package9", new StringType());*/
		query.addScalar("startDate", new DateType());
		query.addScalar("contructionRecords", new StringType());
		query.addScalar("contructionDesign", new StringType());
		query.addScalar("performerId", new LongType());
		query.addScalar("createdUser", new LongType());
		query.addScalar("createdDate", new DateType());
		query.addScalar("updatedUser", new LongType());
		query.addScalar("updatedDate", new DateType());
		query.addScalar("approvedPerformerName", new StringType());
		query.addScalar("approvedUserName", new StringType());
		query.addScalar("performerName", new StringType());
		query.addScalar("realityTangentDate", new DateType());
		query.addScalar("createdDateDb", new StringType());
		
		query.setResultTransformer(Transformers.aliasToBean(ResultTangentDTO.class));
		
		query.setParameter("tangentId", obj.getTangentCustomerId());
		
		return query.list();
	}
	
	public List<UtilAttachDocumentDTO> getFileAttachByResultTangentId(Long objectId, String type) {
		StringBuilder sql = new StringBuilder(
				"SELECT UTIL_ATTACH_DOCUMENT_ID utilAttachDocumentId, "
				+ "OBJECT_ID objectId, NAME name, "
				+ "FILE_PATH filePath "
				+ "from UTIL_ATTACH_DOCUMENT "
				+ "where STATUS!=0 "
				+ "AND OBJECT_ID = :objectId "
				+ "and TYPE = :type");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("utilAttachDocumentId", new LongType());
		query.addScalar("objectId", new LongType());
		query.addScalar("name", new StringType());
		query.addScalar("filePath", new StringType());
		
		query.setParameter("objectId", objectId);
		query.setParameter("type", type);
		
		query.setResultTransformer(Transformers.aliasToBean(UtilAttachDocumentDTO.class));
		
		List<UtilAttachDocumentDTO> ls = query.list();
		return ls;
	}
	
	public ResultTangentDTO getListResultTangentJoinSysUserByResultTangentId(ResultTangentDTO obj){
		StringBuilder sql = new StringBuilder(" SELECT ");
		sql.append("rt.RESULT_TANGENT_ID resultTangentId, ")
		.append("rt.TANGENT_CUSTOMER_ID tangentCustomerId, ")
		.append("rt.APPOINTMENT_DATE appointmentDate, ")
		.append("rt.ORDER_RESULT_TANGENT orderResultTangent, ")
		.append("rt.RESULT_TANGENT_TYPE resultTangentType, ")
		.append("rt.REASON_REJECTION reasonRejection, ")
		.append("rt.APPROVED_STATUS approvedStatus, ")
		.append("rt.APPROVED_PERFORMER_ID approvedPerformerId, ")
		.append("rt.APPROVED_DESCRIPTION approvedDescription, ")
		.append("rt.APPROVED_USER_ID approvedUserId, ")
		.append("rt.APPROVED_DATE approvedDate, ")
		.append("rt.BUILDING_LOCATION buildingLocation, ")
		.append("rt.PACKAGE_1 package1, ")
		.append("rt.PACKAGE_2 package2, ")
		.append("rt.PACKAGE_3 package3, ")
		.append("rt.PACKAGE_4 package4, ")
		.append("rt.PACKAGE_5 package5, ")
		.append("rt.PACKAGE_6 package6, ")
		/*.append("rt.PACKAGE_7 package7, ")
		.append("rt.PACKAGE_8 package8, ")
		.append("rt.PACKAGE_9 package9, ")*/
		.append("rt.START_DATE startDate, ")
		.append("rt.CONTRUCTION_RECORDS contructionRecords, ")
		.append("rt.CONTRUCTION_DESIGN contructionDesign, ")
		.append("rt.PERFORMER_ID performerId, ")
		.append("rt.CREATED_USER createdUser, ")
		.append("rt.CREATED_DATE createdDate, ")
		.append("to_char(rt.CREATED_DATE,'dd/MM/yyyy HH24:Mi:SS') createdDateDb, ")
		.append("rt.UPDATED_USER updatedUser, ")
		.append("rt.UPDATED_DATE updatedDate, ")
		.append("su1.FULL_NAME approvedPerformerName, ")
		.append("su2.FULL_NAME approvedUserName, ")
		.append("su3.FULL_NAME performerName, ")
		.append("su3.EMAIL performerEmail, ")
		.append("su3.PHONE_NUMBER performerPhone, ")
		.append("rt.REALITY_TANGENT_DATE realityTangentDate ")
		.append("FROM RESULT_TANGENT rt ")
		.append("LEFT JOIN SYS_USER su1 on rt.APPROVED_PERFORMER_ID = su1.SYS_USER_ID ")
		.append("LEFT JOIN SYS_USER su2 on rt.APPROVED_USER_ID = su2.SYS_USER_ID ")
		.append("LEFT JOIN SYS_USER su3 on rt.PERFORMER_ID = su3.SYS_USER_ID ")
		.append("WHERE 1=1 AND rt.RESULT_TANGENT_ID =:id ");
		sql.append(" ORDER BY rt.ORDER_RESULT_TANGENT DESC ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("resultTangentId", new LongType());
		query.addScalar("tangentCustomerId", new LongType());
		query.addScalar("appointmentDate", new DateType());
		query.addScalar("orderResultTangent", new StringType());
		query.addScalar("resultTangentType", new StringType());
		query.addScalar("reasonRejection", new StringType());
		query.addScalar("approvedStatus", new StringType());
		query.addScalar("approvedPerformerId", new LongType());
		query.addScalar("approvedDescription", new StringType());
		query.addScalar("approvedUserId", new LongType());
		query.addScalar("approvedDate", new DateType());
		query.addScalar("buildingLocation", new StringType());
		query.addScalar("package1", new StringType());
		query.addScalar("package2", new StringType());
		query.addScalar("package3", new StringType());
		query.addScalar("package4", new StringType());
		query.addScalar("package5", new StringType());
		query.addScalar("package6", new StringType());
		/*query.addScalar("package7", new StringType());
		query.addScalar("package8", new StringType());
		query.addScalar("package9", new StringType());*/
		query.addScalar("startDate", new DateType());
		query.addScalar("contructionRecords", new StringType());
		query.addScalar("contructionDesign", new StringType());
		query.addScalar("performerId", new LongType());
		query.addScalar("createdUser", new LongType());
		query.addScalar("createdDate", new DateType());
		query.addScalar("updatedUser", new LongType());
		query.addScalar("updatedDate", new DateType());
		query.addScalar("approvedPerformerName", new StringType());
		query.addScalar("approvedUserName", new StringType());
		query.addScalar("performerName", new StringType());
		query.addScalar("performerEmail", new StringType());
		query.addScalar("performerPhone", new StringType());
		query.addScalar("realityTangentDate", new DateType());
		query.addScalar("createdDateDb", new StringType());
		
		query.setResultTransformer(Transformers.aliasToBean(ResultTangentDTO.class));
		
		query.setParameter("id", obj.getResultTangentId());
		
		List<ResultTangentDTO> ls = query.list();
		if(ls.size()>0) {
			return ls.get(0);
		}
		return null;
	}
	
	public ResultTangentDTO getResultTangentTypeByResultTangentId(Long id){
		StringBuilder sql = new StringBuilder(" SELECT ");
		sql.append("rt.RESULT_TANGENT_TYPE resultTangentType ")
		.append("FROM RESULT_TANGENT rt ")
		
		.append("WHERE 1=1 AND rt.RESULT_TANGENT_ID =:id ");
		sql.append(" ORDER BY rt.ORDER_RESULT_TANGENT DESC ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("resultTangentType", new StringType());
		
		query.setResultTransformer(Transformers.aliasToBean(ResultTangentDTO.class));
		
		query.setParameter("id", id);
		
		List<ResultTangentDTO> ls = query.list();
		if(ls.size()>0) {
			return ls.get(0);
		}
		return null;
	}
	
}
