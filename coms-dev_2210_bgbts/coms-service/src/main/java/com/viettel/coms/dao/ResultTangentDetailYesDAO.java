package com.viettel.coms.dao;

import java.util.List;

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

import com.viettel.coms.bo.ResultTangentDetailYesBO;
import com.viettel.coms.dto.ResultTangentDetailYesDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@EnableTransactionManagement
@Transactional
@Repository("resultTangentDetailYesDAO")
public class ResultTangentDetailYesDAO extends BaseFWDAOImpl<ResultTangentDetailYesBO, Long>{

	public ResultTangentDetailYesDAO() {
		this.model = new ResultTangentDetailYesBO();
	}

	public ResultTangentDetailYesDAO(Session session) {
		this.session = session;
	}
	
	public ResultTangentDetailYesDTO getListResultTangentYesByTangentCustomerId(Long resultTangentId){
		StringBuilder sql = new StringBuilder(" select RESULT_TANGENT_DETAIL_YES_ID resultTangentDetailYesId,CREATED_DATE createdDate,to_char(CREATED_DATE,'dd/MM/yyyy HH24:Mi:SS') createdDateDb from RESULT_TANGENT_DETAIL_YES where RESULT_TANGENT_ID=:id ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("resultTangentDetailYesId", new LongType());
		query.addScalar("createdDate", new DateType());
		query.addScalar("createdDateDb", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(ResultTangentDetailYesDTO.class));
		
		query.setParameter("id", resultTangentId);
		
		List<ResultTangentDetailYesDTO> ls = query.list();
		if(ls.size()>0) {
			return ls.get(0);
		}
		return null;
	}
	
	public ResultTangentDetailYesDTO getResultTangentYesByResultTangentId(Long resultTangentId){
		StringBuilder sql = new StringBuilder(" select ");
		sql.append("RESULT_TANGENT_DETAIL_YES_ID resultTangentDetailYesId, ")
		.append("RESULT_TANGENT_ID resultTangentId, ")
		.append("INFORMATION_1 information1, ")
		.append("INFORMATION_2 information2, ")
		.append("INFORMATION_3 information3, ")
		.append("INFORMATION_41 information41, ")
		.append("INFORMATION_42 information42, ")
		.append("INFORMATION_43 information43, ")
		.append("INFORMATION_51 information51, ")
		.append("INFORMATION_52 information52, ")
		.append("INFORMATION_53 information53, ")
		.append("INFORMATION_54 information54, ")
		.append("INFORMATION_55 information55, ")
		.append("INFORMATION_6 information6, ")
		.append("INFORMATION_7 information7, ")
		.append("INFORMATION_81 information81, ")
		.append("INFORMATION_82 information82, ")
		.append("CREATED_USER createdUser, ")
		.append("CREATED_DATE createdDate, ")
		.append("to_char(CREATED_DATE,'dd/MM/yyyy HH24:Mi:SS') createdDateDb, ")
		.append("UPDATED_USER updatedUser, ")
		.append("UPDATED_DATE updatedDate, ")
		
		.append("DATE_OF_BIRTH dateOfBirth, ")
		.append("JOB_CUSTOMER jobCustomer, ")
		.append("INFORMATION_1_1 information1_1, ")
		.append("INFORMATION_1_21 information1_21, ")
		.append("INFORMATION_1_22 information1_22, ")
		.append("INFORMATION_1_23 information1_23, ")
		.append("INFORMATION_1_3 information1_3, ")
		.append("INFORMATION_3_1 information3_1, ")
		.append("INFORMATION_3_2 information3_2, ")
		.append("INFORMATION_3_3 information3_3, ")
		.append("INFORMATION_3_4 information3_4, ")
		.append("INFORMATION_3_5 information3_5, ")
		.append("INFORMATION_3_6 information3_6, ")
		.append("INFORMATION_3_7 information3_7, ")
		.append("INFORMATION_3_8 information3_8, ")
		.append("INFORMATION_3_9 information3_9, ")
		.append("CONTENT_BONUS contentBonus, ")
		.append("SCHEDULE_BUILD scheduleBuild, ")
		.append("PROVINCE_CONSTRUCTION provinceConstruction, ")
		.append("DISTRICT_CONSTRUCTION districtConstruction, ")
		.append("COMMUNE_CONSTRUCTION communeConstruction, ")
		.append("DETAIL_ADDRESS_CONSTRUCTION detailAddressConstruction ");
		sql.append("from RESULT_TANGENT_DETAIL_YES where RESULT_TANGENT_ID=:id ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("resultTangentDetailYesId", new LongType());
		query.addScalar("resultTangentId", new LongType());
		query.addScalar("information1", new StringType());
		query.addScalar("information2", new StringType());
		query.addScalar("information3", new StringType());
		query.addScalar("information41", new DoubleType());
		query.addScalar("information42", new StringType());
		query.addScalar("information43", new StringType());
		query.addScalar("information51", new DoubleType());
		query.addScalar("information52", new StringType());
		query.addScalar("information53", new StringType());
		query.addScalar("information54", new StringType());
		query.addScalar("information55", new StringType());
		query.addScalar("information6", new StringType());
		query.addScalar("information7", new StringType());
		query.addScalar("information81", new DoubleType());
		query.addScalar("information82", new DoubleType());
		query.addScalar("createdUser", new LongType());
		query.addScalar("createdDate", new DateType());
		query.addScalar("updatedUser", new LongType());
		query.addScalar("updatedDate", new DateType());
		query.addScalar("createdDateDb", new StringType());
		
		query.addScalar("dateOfBirth", new DateType());
		query.addScalar("jobCustomer", new StringType());
		query.addScalar("information1_1", new StringType());
		query.addScalar("information1_21", new StringType());
		query.addScalar("information1_22", new StringType());
		query.addScalar("information1_23", new StringType());
		query.addScalar("information1_3", new StringType());
		query.addScalar("information3_1", new LongType());
		query.addScalar("information3_2", new LongType());
		query.addScalar("information3_3", new LongType());
		query.addScalar("information3_4", new LongType());
		query.addScalar("information3_5", new LongType());
		query.addScalar("information3_6", new LongType());
		query.addScalar("information3_7", new LongType());
		query.addScalar("information3_8", new LongType());
		query.addScalar("information3_9", new LongType());
		
		query.addScalar("contentBonus", new StringType());
		query.addScalar("scheduleBuild", new DateType());
		query.addScalar("provinceConstruction", new StringType());
		query.addScalar("districtConstruction", new StringType());
		query.addScalar("communeConstruction", new StringType());
		query.addScalar("detailAddressConstruction", new StringType());
		
		query.setParameter("id", resultTangentId);
		
		query.setResultTransformer(Transformers.aliasToBean(ResultTangentDetailYesDTO.class));
		
		List<ResultTangentDetailYesDTO> ls = query.list();
		if(ls.size()>0) {
			return ls.get(0);
		}
		return null;
	}
}
