/*
 * Copyright (C) 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.coms.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.viettel.coms.bo.KpiLogBO;
import com.viettel.coms.dto.KpiLogDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

/**
 * @author TruongBX3
 * @version 1.0
 * @since 08-May-15 4:07 PM
 */
@Repository("kpiLogDAO")
public class KpiLogDAO extends BaseFWDAOImpl<KpiLogBO, Long> {

    public KpiLogDAO() {
        this.model = new KpiLogBO();
    }

    public KpiLogDAO(Session session) {
        this.session = session;
    }
    
    @SuppressWarnings("unchecked")
	public List<KpiLogDTO> doSearch(KpiLogDTO obj){
    	StringBuilder sql = new StringBuilder(" SELECT sys.full_name sysUserName, " + 
    			"  	sys.email email, " + 
    			"  	g.group_name_level2 sysGroupName, " + 
    			"  	a.DESCRIPTION description, " + 
    			"  	TO_CHAR(a.CREATE_DATETIME,'dd/MM/yyyy') submitDay, " + 
    			"  	TO_CHAR(a.start_time,'dd/MM/yyyy hh24:mi:ss') submitStartTime, " + 
    			"  	TO_CHAR(a.end_time,'dd/MM/yyyy hh24:mi:ss') submitEndTime " + 
    			"	FROM kpi_log a " + 
    			"	INNER JOIN SYS_USER sys " + 
    			"	ON a.CREATE_USER_ID=sys.SYS_USER_id " + 
    			"	INNER JOIN sys_group g " + 
    			"	ON sys.sys_group_id=g.sys_group_id " + 
    			"	WHERE 1=1 and a.FUNCTION_CODE in('DOSERACH_DISTRIBUTE_A_MANAGE','DOSERACH_DISTRIBUTE_PXK_A','DOSERACH_DISTRIBUTE_PXK_A_LOG') ");
    	if(StringUtils.isNotBlank(obj.getKeySearch())) {
    		sql.append(" AND (upper(a.FUNCTION_CODE) like upper(:keySearch) escape '&' "
    				+ " OR upper(a.DESCRIPTION) like upper(:keySearch) escape '&' "
    				+ ") ");
    	}
    	
    	if(StringUtils.isNotBlank(obj.getSysGroupName())) {
    		sql.append(" and g.group_name_level2=:sysGroupName ");
    	}
    	
    	if(obj.getDateFrom()!=null) {
    		sql.append(" and a.CREATE_DATETIME >= :dateFrom ");
    	}
    	
    	if(obj.getDateTo()!=null) {
    		sql.append(" and a.CREATE_DATETIME <= :dateTo ");
    	}
    	
    	sql.append(" ORDER BY to_date(a.CREATE_DATETIME,'dd/MM/yyyy') DESC ");
    	
    	StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
    	
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
    	
    	query.addScalar("sysUserName", new StringType());
    	query.addScalar("email", new StringType());
    	query.addScalar("sysGroupName", new StringType());
    	query.addScalar("description", new StringType());
    	query.addScalar("submitDay", new StringType());
    	query.addScalar("submitStartTime", new StringType());
    	query.addScalar("submitEndTime", new StringType());
    	
    	query.setResultTransformer(Transformers.aliasToBean(KpiLogDTO.class));
    	
    	if(StringUtils.isNotBlank(obj.getKeySearch())) {
    		query.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
    		queryCount.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
    	}
    	
    	if(StringUtils.isNotBlank(obj.getSysGroupName())) {
    		query.setParameter("sysGroupName", obj.getSysGroupName());
    		queryCount.setParameter("sysGroupName", obj.getSysGroupName());
    	}
    	
    	if(obj.getDateFrom()!=null) {
    		query.setParameter("dateFrom", obj.getDateFrom());
    		queryCount.setParameter("dateFrom", obj.getDateFrom());
    	}
    	
    	if(obj.getDateTo()!=null) {
    		query.setParameter("dateTo", obj.getDateTo());
    		queryCount.setParameter("dateTo", obj.getDateTo());
    	}
    	
    	if (obj.getPage() != null && obj.getPageSize() != null) {
            query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
            query.setMaxResults(obj.getPageSize().intValue());
        }
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

        return query.list();
    }
}
