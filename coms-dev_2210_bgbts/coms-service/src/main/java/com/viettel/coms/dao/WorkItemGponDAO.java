/*
 * Copyright (C) 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.coms.dao;
import java.util.Date;
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

import com.viettel.cat.dto.CatTaskDTO;
import com.viettel.coms.bo.WorkItemGponBO;
import com.viettel.coms.dto.ConstructionDTO;
import com.viettel.coms.dto.WoDTO;
import com.viettel.coms.dto.WorkItemDTO;
import com.viettel.coms.dto.WorkItemGponDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

/**
 * @author TruongBX3
 * @version 1.0
 * @since 08-May-15 4:07 PM
 */
@Repository("workItemGponDAO")
public class WorkItemGponDAO extends BaseFWDAOImpl<WorkItemGponBO, Long> {


    public WorkItemGponDAO() {
        this.model = new WorkItemGponBO();
    }

    public WorkItemGponDAO(Session session) {
        this.session = session;
    }

   public Long  saveGP(WorkItemGponDTO wo) {
	   Session ss = getSession();
		return (Long) ss.save(wo.toModel());
   }
   
   public void updateGP(WorkItemGponDTO  wo) {
		Session ss = getSession();
		ss.update( wo.toModel());
	}
   
   public void removeGpon(WorkItemGponDTO constructionProjectDTO) {
		StringBuilder sql = new StringBuilder("delete from    "
				+ "  WORK_ITEM T1 ");
		sql.append(" WHERE UPPER(T1.WORK_ITEM_ID) = UPPER(:id) " );
		SQLQuery query= getSession().createSQLQuery(sql.toString());
		if (null != constructionProjectDTO.getWorkItemId()) { //id cong viec
			query.setParameter("id", constructionProjectDTO.getWorkItemId());
		}
		 query.executeUpdate();
	}
   public void removeDetailitemGpon(WorkItemGponDTO constructionProjectDTO) {
		StringBuilder sql = new StringBuilder("delete from    "
				+ "  WORK_ITEM_GPON T1 ");
		sql.append(" WHERE UPPER(T1.WORK_ITEM_GPON_ID) = UPPER(:id) " );
		SQLQuery query= getSession().createSQLQuery(sql.toString());
		if (null != constructionProjectDTO.getWorkItemGponId()) { //id cong viec
			query.setParameter("id", constructionProjectDTO.getWorkItemGponId());
		}
		 query.executeUpdate();
	}
   
   public void editGpon(WorkItemGponDTO constructionProjectDTO) {
		StringBuilder sql = new StringBuilder("update     "
				+ "  WORK_ITEM_GPON T1 set T1.AMOUNT = :amount , T1.PRICE = :price ");
		sql.append(" WHERE UPPER(T1.WORK_ITEM_GPON_ID) = UPPER(:id) " );
		SQLQuery query= getSession().createSQLQuery(sql.toString());
		if (null != constructionProjectDTO.getWorkItemGponId()) { //id cong viec
			query.setParameter("id", constructionProjectDTO.getWorkItemGponId());
			query.setParameter("amount", constructionProjectDTO.getAmount());
			query.setParameter("price", constructionProjectDTO.getPrice());
		}
		 query.executeUpdate();
	}
   
   public List<ConstructionDTO> getConstructionById(Long id) {
		StringBuilder stringBuilder = new StringBuilder("SELECT ");
		stringBuilder.append("T1.name name ");
		stringBuilder.append(",T1.code code ");
		stringBuilder.append("FROM construction T1 ");
		stringBuilder.append("WHERE 1=1 AND T1.STATUS != 0 ");
		stringBuilder.append("and T1.CONSTRUCTION_ID = :constructionId");
		SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
		query.addScalar("name", new StringType());
		query.addScalar("code", new StringType());
		query.setResultTransformer(Transformers
				.aliasToBean(ConstructionDTO.class));
		query.setParameter("constructionId", id);
		return query.list();
	}
   
   public List<WorkItemDTO> getWorkItemById(Long id) {
		StringBuilder stringBuilder = new StringBuilder("SELECT ");
		stringBuilder.append("T1.name name ");
		stringBuilder.append(",T1.code code ");
		stringBuilder.append(",T1.WORK_ITEM_ID workItemId ");
		stringBuilder.append("FROM work_item T1 ");
		stringBuilder.append("WHERE 1=1 AND T1.STATUS != 0 ");
		stringBuilder.append("and T1.WORK_ITEM_ID = :id");
		SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
		query.addScalar("name", new StringType());
		query.addScalar("code", new StringType());
		query.addScalar("workItemId", new LongType());
		query.setResultTransformer(Transformers
				.aliasToBean(WorkItemDTO.class));
		query.setParameter("id", id);
		return query.list();
	}
   public List<CatTaskDTO> getCatTaskByName(String name) {
		StringBuilder stringBuilder = new StringBuilder("SELECT ");
		stringBuilder.append("T1.name name ");
		stringBuilder.append(",T1.code code ");
		stringBuilder.append(",T1.CAT_TASK_ID catTaskId ");
		stringBuilder.append("FROM cat_task T1 ");
		stringBuilder.append("WHERE 1=1 AND T1.STATUS != 0 ");
		stringBuilder.append("and T1.name = :name");
		SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
		query.addScalar("name", new StringType());
		query.addScalar("catTaskId", new LongType());
		query.addScalar("code", new StringType());
		query.setResultTransformer(Transformers
				.aliasToBean(CatTaskDTO.class));
		query.setParameter("name", name);
		return query.list();
	}
   
   //Huypq-03082020-start
   public void removeWorkItemGpon(WorkItemGponDTO constructionProjectDTO) {
		StringBuilder sql = new StringBuilder("delete from    "
				+ "  WORK_ITEM_GPON T1 ");
		sql.append(" WHERE T1.WORK_ITEM_ID = :id " );
		SQLQuery query= getSession().createSQLQuery(sql.toString());
		if (null != constructionProjectDTO.getWorkItemId()) { //id cong viec
			query.setParameter("id", constructionProjectDTO.getWorkItemId());
		}
		 query.executeUpdate();
	}
   //Huyy-end
   
   //huypq-29082020-start
   public Boolean checkWorkItemInWoForDelete(Long consId, Long catWiTypeId) {
	   StringBuilder sql = new StringBuilder(" select WO_CODE woCode from wo where STATUS!=0 and CONSTRUCTION_ID=:consId and CAT_WORK_ITEM_TYPE_ID=:catWiTypeId ");
	   SQLQuery query = getSession().createSQLQuery(sql.toString());
	   query.addScalar("woCode", new StringType());
	   query.setParameter("consId", consId);
	   query.setParameter("catWiTypeId", catWiTypeId);
	   String code = (String)query.uniqueResult();
	   if(StringUtils.isNoneBlank(code)) {
		   return true;
	   }
	   return false;
   }
   //Huy-end
   
   //Huypq-28102020-start
   public Boolean getAvgStatusWorkItemById(Long workId, Long consId) {
		StringBuilder stringBuilder = new StringBuilder("SELECT ");
		stringBuilder.append("AVG(T1.status) status ");
		stringBuilder.append("FROM work_item T1 ");
		stringBuilder.append("WHERE 1=1 AND T1.STATUS != 0 ");
		stringBuilder.append("and T1.WORK_ITEM_ID != :workId ");
		stringBuilder.append("and T1.CONSTRUCTION_ID = :consId ");
		SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
		query.addScalar("status", new DoubleType());
		
		query.setParameter("workId", workId);
		query.setParameter("consId", consId);
		
		if(query.uniqueResult()!=null) {
			Double status = (Double)query.uniqueResult();
			if(status!=null && status==3) {
				return true;
			}
		}
		return false;
	}
   
//   public void updateStatusConstruction(Long consId) {
//		StringBuilder sql = new StringBuilder("update construction set status=5 where construction_id=:consId");
//		SQLQuery query= getSession().createSQLQuery(sql.toString());
//		query.setParameter("consId", consId);
//		query.executeUpdate();
//	}
   
   //Huy-end
   
	// Huypq-02012021-start
	public void updateStatusConstruction(Long consId, Date completeDate, Double quantity) {
//		taotq start 01072022
		Boolean check = checkWo(consId);
		StringBuilder sql = new StringBuilder(" update construction set ");
		if(check) {
			sql.append(" status= -5 ");
		}else {
			sql.append(" status=5 ");
		}
		sql.append(" , COMPLETE_DATE=:completeDate, COMPLETE_VALUE=:quantity where construction_id=:consId ");
//		StringBuilder sql = new StringBuilder("update construction set status=5, COMPLETE_DATE=:completeDate, COMPLETE_VALUE=:quantity where construction_id=:consId");
//		taotq end 01072022
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("consId", consId);
		query.setParameter("completeDate", completeDate);
		query.setParameter("quantity", quantity);
		query.executeUpdate();
	}

	public WorkItemDTO getMaxCompleteDateByConsId(Long consId) {
		StringBuilder sql = new StringBuilder(
				"select MAX(COMPLETE_DATE) completeDate, SUM(QUANTITY) quantity from WORK_ITEM where status=3 and CONSTRUCTION_ID=:consId");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("completeDate", new DateType());
		query.addScalar("quantity", new DoubleType());

		query.setParameter("consId", consId);

		query.setResultTransformer(Transformers.aliasToBean(WorkItemDTO.class));

		List<WorkItemDTO> ls = query.list();
		if (ls.size() > 0) {
			return ls.get(0);
		}
		return null;
	}
	// Huy-end
	
//  taotq start 01072022
  public Boolean checkWo(Long consId) {
		Boolean check = false;
		String sql1 = "select distinct w.WO_TYPE_ID woTypeId, w.CD_LEVEL_1 cdLevel1 from CONSTRUCTION c LEFT JOIN WO w ON c.CONSTRUCTION_ID = w.CONSTRUCTION_ID where c.CAT_CONSTRUCTION_TYPE_ID =8 AND c.CONSTRUCTION_ID = :constructionId ";
		SQLQuery query1 = getSession().createSQLQuery(sql1);
		query1.setParameter("constructionId", consId);
		query1.setResultTransformer(Transformers.aliasToBean(WoDTO.class));
		query1.addScalar("woTypeId", new LongType());
		query1.addScalar("cdLevel1", new StringType());
		List<WoDTO> lst = query1.list();
		if(lst.size()>0) {
			if(lst.get(0).getWoTypeId() == 1 && lst.get(0).getCdLevel1().equals("242656")) {
				check = true;
			}
		}
		return check;
	}
//  taotq end 01072022
}
