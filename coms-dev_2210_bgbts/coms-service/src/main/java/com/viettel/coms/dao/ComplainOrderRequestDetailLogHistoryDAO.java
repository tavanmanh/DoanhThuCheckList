package com.viettel.coms.dao;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.viettel.coms.bo.ComplainOrderRequestBO;
import com.viettel.coms.bo.ComplainOrderRequestDetailLogHistoryBO;
import com.viettel.coms.dto.ComplainOrderRequestDTO;
import com.viettel.coms.dto.ComplainOrderRequestDetailLogHistoryDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@Repository("complainOrderRequestDetailLogHistoryDAO")
@Transactional
public class ComplainOrderRequestDetailLogHistoryDAO extends BaseFWDAOImpl<ComplainOrderRequestDetailLogHistoryBO, Long> {


    public ComplainOrderRequestDetailLogHistoryDAO() {
        this.model = new ComplainOrderRequestDetailLogHistoryBO();
    }

    public ComplainOrderRequestDetailLogHistoryDAO(Session session) {
        this.session = session;
    }
    
    @SuppressWarnings("unchecked")
    public List<ComplainOrderRequestDetailLogHistoryDTO> doSearch(ComplainOrderRequestDetailLogHistoryDTO criteria) {
        StringBuilder stringBuilder = new StringBuilder(
                "SELECT T1.COMPLAIN_ORDER_REQUEST_DETAIL_ID complainOrderRequestDetailId,T1.STATUS status,"
                        + " T1.ACTION action,T1.REASON reason,T1.TIMES times,T1.NOTE note,"
                        + " T1.CREATE_DATE createDate," 
                        + " to_char(T1.CREATE_DATE,'dd/MM/yyyy HH24:Mi:SS') createDateString, "
                        + " T1.CREATE_USER createUser," + " T3.FULL_NAME createUserName," 
                        + " T1.EXTEND_DATE extendDate," 
                        +"  T1.COMPLAIN_ORDER_REQUEST_ID complainOrderRequestId "
                        + " FROM CTCT_COMS_OWNER.COMPLAIN_ORDER_REQUEST_DETAIL_LOG_HISTORY T1 "
                        + " LEFT JOIN SYS_USER T3 ON T1.CREATE_USER = T3.EMPLOYEE_CODE "
                        + " where 1=1  ");
        if (null != criteria.getComplainOrderRequestId()) {
            stringBuilder
                    .append("AND T1.COMPLAIN_ORDER_REQUEST_ID = :complainOrderRequestId ");
        }
        
        if (criteria.getCreateUser() != null) {
        	stringBuilder.append("And T1.CREATE_USER like :createUser ");
        }
        
      
        if (criteria.getStatus() != null) {
        	stringBuilder.append("And T1.STATUS =:status ");
        }

        stringBuilder.append("ORDER BY T1.CREATE_DATE DESC ");
       
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(stringBuilder.toString());
        sqlCount.append(")");

        SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
        query.addScalar("complainOrderRequestDetailId", new LongType());
        
        query.addScalar("action", new StringType());
		query.addScalar("reason", new StringType());
		query.addScalar("times", new LongType());
        query.addScalar("note", new StringType());
        query.addScalar("status", new LongType());
        
        query.addScalar("extendDate", new DateType());
        query.addScalar("createDate", new DateType());
        query.addScalar("createUser", new StringType());
        query.addScalar("createUserName", new StringType());
        query.addScalar("createDateString", new StringType());
        query.addScalar("complainOrderRequestId", new LongType());
        
     
        if (null != criteria.getComplainOrderRequestId()) {
            query.setParameter("complainOrderRequestId", criteria.getComplainOrderRequestId());
            queryCount.setParameter("complainOrderRequestId",criteria.getComplainOrderRequestId());
        }
        
        if (criteria.getCreateUser() != null) {
            query.setParameter("createUser", "%" + criteria.getCreateUser() + "%");
            queryCount.setParameter("createUser", "%" + criteria.getCreateUser() + "%");
        }
        
        if (criteria.getStatus() != null) {
			query.setParameter("status", criteria.getStatus());
			queryCount.setParameter("status", criteria.getStatus());
		}
        query.setResultTransformer(Transformers
                .aliasToBean(ComplainOrderRequestDetailLogHistoryDTO.class));
        if (criteria.getPage() != null && criteria.getPageSize() != null) {
            query.setFirstResult((criteria.getPage().intValue() - 1)
                    * criteria.getPageSize().intValue());
            query.setMaxResults(criteria.getPageSize().intValue());
        }

        List ls = query.list();
        criteria.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
        return ls;
    }


}
