package com.viettel.coms.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.viettel.coms.bo.DetailMonthQuantityBO;
import com.viettel.coms.dto.DetailMonthQuantityDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import com.viettel.utils.ValidateUtils;

@EnableTransactionManagement
@Transactional
@Repository("detailMonthQuantityDAO")
public class DetailMonthQuantityDAO extends BaseFWDAOImpl<DetailMonthQuantityBO, Long>{

	public DetailMonthQuantityDAO() {
        this.model = new DetailMonthQuantityBO();
    }

    public DetailMonthQuantityDAO(Session session) {
        this.session = session;
    }
    
    @SuppressWarnings("unchecked")
    public List<DetailMonthQuantityDTO> doSearchStaffByPopup(DetailMonthQuantityDTO obj) {
        StringBuilder sql = new StringBuilder(
                "SELECT SYS_USER_ID performerId, EMPLOYEE_CODE||'-'||FULL_NAME performerName FROM SYS_USER ");
        sql.append(" WHERE 1=1 and status!=0 "
        		);
        if (StringUtils.isNotEmpty(obj.getPerformerName())) {
            sql.append(" AND (upper(EMPLOYEE_CODE) LIKE upper(:name) escape '&' "
            		+ " OR upper(FULL_NAME) LIKE upper(:name) escape '&' "
            		+ " OR upper(EMAIL) LIKE upper(:name) escape '&') ");
        }
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        query.addScalar("performerId", new LongType());
        query.addScalar("performerName", new StringType());
        
        query.setResultTransformer(Transformers.aliasToBean(DetailMonthQuantityDTO.class));
        
        if (StringUtils.isNotEmpty(obj.getPerformerName())) {
            query.setParameter("name", "%" + ValidateUtils.validateKeySearch(obj.getPerformerName()) + "%");
            queryCount.setParameter("name", "%" + ValidateUtils.validateKeySearch(obj.getPerformerName()) + "%");
        }
        if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
        return query.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<DetailMonthQuantityDTO> doSearchContractByPopup(DetailMonthQuantityDTO obj) {
        StringBuilder sql = new StringBuilder(
                "SELECT CNT_CONTRACT_ID cntContractId, CODE cntContractCode FROM CNT_CONTRACT ");
        sql.append(" WHERE 1=1 and status!=0 and contract_type=9 "
        		);
        if (StringUtils.isNotBlank(obj.getCntContractCode())) {
            sql.append(" AND upper(CODE) LIKE upper(:name) escape '&' ");
        }
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        query.addScalar("cntContractId", new LongType());
        query.addScalar("cntContractCode", new StringType());
        
        query.setResultTransformer(Transformers.aliasToBean(DetailMonthQuantityDTO.class));
        
        if (StringUtils.isNotBlank(obj.getCntContractCode())) {
            query.setParameter("name", "%" + ValidateUtils.validateKeySearch(obj.getCntContractCode()) + "%");
            queryCount.setParameter("name", "%" + ValidateUtils.validateKeySearch(obj.getCntContractCode()) + "%");
        }
        
        if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
        return query.list();
    }
}
