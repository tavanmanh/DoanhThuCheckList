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

import com.viettel.coms.bo.ResultTangentDetailNoBO;
import com.viettel.coms.dto.ResultTangentDetailNoDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@EnableTransactionManagement
@Transactional
@Repository("resultTangentDetailNoDAO")
public class ResultTangentDetailNoDAO extends BaseFWDAOImpl<ResultTangentDetailNoBO, Long>{

	public ResultTangentDetailNoDAO() {
		this.model = new ResultTangentDetailNoBO();
	}

	public ResultTangentDetailNoDAO(Session session) {
		this.session = session;
	}
	
	public ResultTangentDetailNoDTO getListResultTangentNoByTangentCustomerId(Long resultTangentId){
		StringBuilder sql = new StringBuilder(" select RESULT_TANGENT_DETAIL_NO_ID resultTangentDetailNoId,CREATED_DATE createdDate,to_char(CREATED_DATE,'dd/MM/yyyy HH24:Mi:SS') createdDateDb from RESULT_TANGENT_DETAIL_NO where RESULT_TANGENT_ID=:id ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("resultTangentDetailNoId", new LongType());
		query.addScalar("createdDate", new DateType());
		query.addScalar("createdDateDb", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(ResultTangentDetailNoDTO.class));
		
		query.setParameter("id", resultTangentId);
		
		List<ResultTangentDetailNoDTO> ls = query.list();
		if(ls.size()>0) {
			return ls.get(0);
		}
		return null;
	}
	
	public void deleteFileAttachByObjectId(Long objId, List<String> lsType) {
		StringBuilder sql = new StringBuilder("DELETE FROM UTIL_ATTACH_DOCUMENT WHERE OBJECT_ID=:objId AND TYPE in (:lsType) ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("objId", objId);
		query.setParameterList("lsType", lsType);
		query.executeUpdate();
	}
	
	public ResultTangentDetailNoDTO getResultTangentNoByResultTangentId(Long resultTangentId){
		StringBuilder sql = new StringBuilder(" select "); 
		sql.append("RESULT_TANGENT_DETAIL_NO_ID resultTangentDetailNoId, ")
		.append("RESULT_TANGENT_ID resultTangentId, ")
		.append("INFORMATION_1 information1, ")
		.append("INFORMATION_21 information21, ")
		.append("INFORMATION_22 information22, ")
		.append("INFORMATION_23 information23, ")
		.append("INFORMATION_3 information3, ")
		.append("INFORMATION_4 information4, ")
		.append("INFORMATION_51 information51, ")
		.append("INFORMATION_52 information52, ")
		.append("INFORMATION_53 information53, ")
		.append("INFORMATION_61 information61, ")
		.append("INFORMATION_62 information62, ")
		.append("INFORMATION_63 information63, ")
		.append("INFORMATION_64 information64, ")
		.append("INFORMATION_65 information65, ")
		.append("INFORMATION_71 information71, ")
		.append("INFORMATION_72 information72, ")
		.append("INFORMATION_81 information81, ")
		.append("INFORMATION_82 information82, ")
		.append("INFORMATION_83 information83, ")
		.append("INFORMATION_84 information84, ")
		.append("INFORMATION_85 information85, ")
		.append("INFORMATION_91 information91, ")
		.append("INFORMATION_92 information92, ")
		.append("INFORMATION_10 information10, ")
		.append("INFORMATION_11 information11, ")
		.append("INFORMATION_121 information121, ")
		.append("INFORMATION_122 information122, ")
		.append("INFORMATION_123 information123, ")
		.append("INFORMATION_131 information131, ")
		.append("INFORMATION_132 information132, ")
		.append("INFORMATION_133 information133, ")
		.append("INFORMATION_134 information134, ")
		.append("INFORMATION_135 information135, ")
		.append("INFORMATION_14 information14, ")
		.append("INFORMATION_151 information151, ")
		.append("INFORMATION_152 information152, ")
		.append("INFORMATION_153 information153, ")
		.append("INFORMATION_154 information154, ")
		.append("INFORMATION_161 information161, ")
		.append("INFORMATION_162 information162, ")
		.append("CREATED_USER createdUser, ")
		.append("CREATED_DATE createdDate, ")
		.append("to_char(CREATED_DATE,'dd/MM/yyyy HH24:Mi:SS') createdDateDb, ")
		.append("UPDATED_USER updatedUser, ")
		.append("UPDATED_DATE updatedDate ");
		sql.append(" from RESULT_TANGENT_DETAIL_NO where RESULT_TANGENT_ID=:id ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("resultTangentDetailNoId", new LongType());
		query.addScalar("resultTangentId", new LongType());
		query.addScalar("information1", new StringType());
		query.addScalar("information21", new StringType());
		query.addScalar("information22", new StringType());
		query.addScalar("information23", new StringType());
		query.addScalar("information3", new StringType());
		query.addScalar("information4", new StringType());
		query.addScalar("information51", new DoubleType());
		query.addScalar("information52", new StringType());
		query.addScalar("information53", new StringType());
		query.addScalar("information61", new LongType());
		query.addScalar("information62", new StringType());
		query.addScalar("information63", new StringType());
		query.addScalar("information64", new StringType());
		query.addScalar("information65", new LongType());
		query.addScalar("information71", new LongType());
		query.addScalar("information72", new StringType());
		query.addScalar("information81", new StringType());
		query.addScalar("information82", new StringType());
		query.addScalar("information83", new StringType());
		query.addScalar("information84", new StringType());
		query.addScalar("information85", new StringType());
		query.addScalar("information91", new StringType());
		query.addScalar("information92", new StringType());
		query.addScalar("information10", new StringType());
		query.addScalar("information11", new StringType());
		query.addScalar("information121", new DoubleType());
		query.addScalar("information122", new DoubleType());
		query.addScalar("information123", new DoubleType());
		query.addScalar("information131", new DoubleType());
		query.addScalar("information132", new DoubleType());
		query.addScalar("information133", new StringType());
		query.addScalar("information134", new StringType());
		query.addScalar("information135", new StringType());
		query.addScalar("information14", new StringType());
		query.addScalar("information151", new StringType());
		query.addScalar("information152", new StringType());
		query.addScalar("information153", new StringType());
		query.addScalar("information154", new StringType());
		query.addScalar("information161", new DoubleType());
		query.addScalar("information162", new DoubleType());
		query.addScalar("createdUser", new LongType());
		query.addScalar("createdDate", new DateType());
		query.addScalar("createdDateDb", new StringType());
		query.addScalar("updatedUser", new LongType());
		query.addScalar("updatedDate", new DateType());
		
		query.setParameter("id", resultTangentId);
		
		query.setResultTransformer(Transformers.aliasToBean(ResultTangentDetailNoDTO.class));
		
		List<ResultTangentDetailNoDTO> ls = query.list();
		if(ls.size()>0) {
			return ls.get(0);
		}
		return null;
	}
}
