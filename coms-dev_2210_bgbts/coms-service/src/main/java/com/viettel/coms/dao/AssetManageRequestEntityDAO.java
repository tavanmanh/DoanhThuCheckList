/*
 * Copyright (C) 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.coms.dao;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.springframework.stereotype.Repository;

import com.viettel.coms.bo.AssetManageRequestEntityBO;
import com.viettel.coms.dto.AssetManageRequestEntityDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

/**
 * @author TruongBX3
 * @version 1.0
 * @since 08-May-15 4:07 PM
 */
@Repository("assetManageRequestEntityDAO")
public class AssetManageRequestEntityDAO extends BaseFWDAOImpl<AssetManageRequestEntityBO, Long> {

	public AssetManageRequestEntityDAO() {
		this.model = new AssetManageRequestEntityBO();
	}

	public AssetManageRequestEntityDAO(Session session) {
		this.session = session;
	}

	public void updateMerentityById(Long merentityId, Long amreId) {
		StringBuilder sql = new StringBuilder("");
		sql.append(" UPDATE ASSET_MANAGE_REQUEST_ENTITY amre ");
		sql.append(" SET ");
		sql.append(" amre.MER_ENTITY_ID =:merentityId ");
		sql.append(" WHERE amre.ASSET_MANAGE_REQUEST_ENTITY_ID =:amreId ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("merentityId", merentityId);
		query.setParameter("amreId", amreId);
		query.executeUpdate();
	}
	
    //nhantv get list ASSET_MANAGE_REQUEST_ENTITY: mer_entity_id, parent_mer_entity_id, amount 
    public List<AssetManageRequestEntityDTO> getListByMotherfuckerId(Long id){
    	 StringBuilder sql = new StringBuilder("SELECT AE.MER_ENTITY_ID merEntityId,"
                 + "AE.PARENT_MER_ENTITY_ID parentMerEntityId, quantity quantity FROM ASSET_MANAGE_REQUEST_ENTITY AE WHERE AE.ASSET_MANAGEMENT_REQUEST_ID = "+id);
      
         SQLQuery query = getSession().createSQLQuery(sql.toString());

         
         query.addScalar("merEntityId", new LongType());
         query.addScalar("quantity", new DoubleType());
         query.addScalar("parentMerEntityId", new DoubleType());
         query.setResultTransformer(Transformers.aliasToBean(AssetManageRequestEntityDTO.class));
         return query.list();
    }

    //nhantv get list ASSET_MANAGE_REQUEST_ENTITY: mer_entity_id, parent_mer_entity_id, amount 
    @SuppressWarnings("unchecked")
	public AssetManageRequestEntityDTO getByShit(Long merEntityId){
    	 StringBuilder sql = new StringBuilder("SELECT AE.MER_ENTITY_ID merEntityId,"
                 + "AE.PARENT_MER_ENTITY_ID parentMerEntityId, quantity quantity FROM ASSET_MANAGE_REQUEST_ENTITY AE where ae.");
      
         SQLQuery query = getSession().createSQLQuery(sql.toString());
//         query.setParameter("merEntityId", merEntityId);
         
         query.addScalar("merEntityId", new DoubleType());
//         query.addScalar("quantity", new DoubleType());
//         query.addScalar("parentMerEntityId", new DoubleType());
         System.out.println("id: "+ merEntityId);
         query.setResultTransformer(Transformers.aliasToBean(AssetManageRequestEntityDTO.class));
         List <AssetManageRequestEntityDTO> list =  query.list();
         for(AssetManageRequestEntityDTO obj : list) {
        	 System.out.println(obj.getMerEntityId().longValue());
         }
         return list.get(0);
    }
}
