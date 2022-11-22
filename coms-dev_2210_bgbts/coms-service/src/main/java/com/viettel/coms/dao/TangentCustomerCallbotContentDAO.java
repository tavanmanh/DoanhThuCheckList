package com.viettel.coms.dao;

import com.viettel.coms.bo.TangentCustomerCallbotContentBO;
import com.viettel.coms.bo.TangentCustomerNoticeBO;
import com.viettel.coms.dto.TangentCustomerCallbotContentDTO;
import com.viettel.coms.dto.TangentCustomerNoticeDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import java.util.List;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@EnableTransactionManagement
@Transactional
@Repository("tangentCustomerCallbotContentDAO")
public class TangentCustomerCallbotContentDAO extends BaseFWDAOImpl<TangentCustomerCallbotContentBO, Long> {

	public TangentCustomerCallbotContentDAO() {
		this.model = new TangentCustomerCallbotContentBO();
	}

	public TangentCustomerCallbotContentDAO(Session session) {
		this.session = session;
	}

	public List<TangentCustomerCallbotContentDTO> findByTangentCustomerNoticeId (Long id) {
		String sql = "SELECT SENDER sender, MESSAGE message, START_TIME startTime, END_TIME endTime FROM TANGENT_CUSTOMER_CALLBOT_CONTENT WHERE TANGENT_CUSTOMER_CALLBOT_ID = :id  ";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.addScalar("sender", new StringType());
		query.addScalar("message", new StringType());
		query.addScalar("startTime", new StringType());
		query.addScalar("endTime", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(TangentCustomerCallbotContentDTO.class));
		query.setParameter("id", id);
		return query.list();
	}

	
}
