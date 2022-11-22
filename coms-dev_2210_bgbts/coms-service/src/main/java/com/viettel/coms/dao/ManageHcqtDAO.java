package com.viettel.coms.dao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.viettel.coms.bo.DetailMonthPlanBO;
import com.viettel.coms.bo.ManageHcqtBO;
import com.viettel.coms.bo.ManageVttbBO;
import com.viettel.coms.dto.CNTContractDTO;
import com.viettel.coms.dto.CatCommonDTO;
import com.viettel.coms.dto.ConstructionDTO;
import com.viettel.coms.dto.ConstructionTaskDetailDTO;
import com.viettel.coms.dto.DepartmentDTO;
import com.viettel.coms.dto.DetailMonthPlanDTO;
import com.viettel.coms.dto.DetailMonthPlanSimpleDTO;
import com.viettel.coms.dto.DetailMonthPlaningDTO;
import com.viettel.coms.dto.DmpnOrderDTO;
import com.viettel.coms.dto.ManageHcqtDTO;
import com.viettel.coms.dto.ManageVttbDTO;
import com.viettel.coms.dto.RevokeCashMonthPlanDTO;
import com.viettel.coms.dto.SysUserCOMSDTO;
import com.viettel.coms.dto.TmpnTargetDTO;
import com.viettel.coms.dto.TmpnTargetDetailDTO;
import com.viettel.coms.dto.WorkItemDTO;
import com.viettel.coms.dto.WorkItemDetailDTO;
import com.viettel.erp.dto.AMaterialRecoveryListDTO;
import com.viettel.erp.dto.AMaterialRecoveryListModelDTO;
import com.viettel.erp.dto.CntContractDTO;
import com.viettel.erp.dto.SysUserDTO;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import com.viettel.wms.utils.ValidateUtils;

/**
 * @author HoangNH38
 */
@Repository("manageHcqtDAO")
public class ManageHcqtDAO extends BaseFWDAOImpl<ManageHcqtBO, Long>{


    public ManageHcqtDAO() {
        this.model = new ManageHcqtBO();
    }

    public ManageHcqtDAO(Session session) {
        this.session = session;
    }

    
    //tatph -start - 20112019
    
    
    
    public List<ManageHcqtDTO> doSearch(ManageHcqtDTO obj , List<String> groupIdList) {
        StringBuilder sql = new StringBuilder()
        		.append(" SELECT ")
        		.append(" T1.MANAGE_HCQT_ID manageHcqtId, ")
        		.append(" T1.CONSTRUCTION_NAME constructionName, ")
        		.append(" T1.CONSTRUCTION_CODE constructionCode, ")
        		.append(" T1.CONSTRUCTION_ID constructionId, ")
        		.append(" T1.STATION_CODE stationCode, ")
        		.append(" T1.CONTRACT_CODE contractCode, ")
        		.append(" T1.CONTRACT_NAME contractName, ")
        		.append(" T1.STATION_NAME stationName, ")
        		.append(" T1.PROVINCE_CODE provinceCode, ")
        		.append(" T1.PROVINCE_NAME provinceName, ")
        		.append(" T1.VTTB_RECIVE_VALUE vttbReciveValue, ")
        		.append(" T1.VTTB_QT_VALUE vttbQtValue, ")
        		.append(" T1.VTTB_DT_VALUE vttbDtValue, ")
        		.append(" T2.STATUS constructionStatus, ")
        		.append(" T1.CREATE_DATE createDate, ")
        		.append(" T1.UPDATE_DATE updateDate, ")
        		.append(" T1.CREATE_USER_ID createUserId, ")
        		.append(" T1.UPDATE_USER_ID updateUserId ")
        		.append(" FROM MANAGE_HCQT T1")
        		.append(" left join V_CONSTRUCTION_HCQT T2 on T1.CONSTRUCTION_ID = T2.CONSTRUCT_ID ")
        		.append(" WHERE 1 = 1 ");
        		
//        if(groupIdList.size() > 0) {
//        	sql.append("  and T1.PROVINCE_CODE in (select code from CAT_PROVINCE where CAT_PROVINCE.CAT_PROVINCE_ID in (:groupIdList )) ");
//        }
        if (StringUtils.isNotEmpty(obj.getKeySearch())) {
            sql.append(
                    " AND (upper(T1.CONSTRUCTION_CODE) LIKE upper(:keySearch)  escape '&')");
        }
        if (StringUtils.isNotEmpty(obj.getStatus())) {
            sql.append(
                    " AND (upper(T2.STATUS) = :status)");
        }
//        }
        sql.append(" ORDER BY T1.MANAGE_HCQT_ID DESC");
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
        if (StringUtils.isNotEmpty(obj.getKeySearch())) {
            query.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch()) + "%");
            queryCount.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch()) + "%");
        }
        if (StringUtils.isNotEmpty(obj.getStatus())) {
            query.setParameter("status",  ValidateUtils.validateKeySearch(obj.getStatus()) );
            queryCount.setParameter("status", ValidateUtils.validateKeySearch(obj.getStatus()) );
        }
        
//        if (groupIdList.size() > 0) {
//            query.setParameterList("groupIdList", groupIdList);
//            queryCount.setParameterList("groupIdList", groupIdList);
//        }
        query.addScalar("manageHcqtId", new LongType());
        query.addScalar("constructionName", new StringType());
        query.addScalar("constructionCode", new StringType());
        query.addScalar("constructionId", new LongType());
        query.addScalar("stationCode", new StringType());
        query.addScalar("contractCode", new StringType());
        query.addScalar("contractName", new StringType());
        query.addScalar("stationName", new StringType());
        query.addScalar("provinceCode", new StringType());
        query.addScalar("provinceName", new StringType());
        
        query.addScalar("vttbReciveValue", new LongType());
        query.addScalar("vttbQtValue", new LongType());
        query.addScalar("vttbDtValue", new LongType());
        
        query.addScalar("constructionStatus", new StringType());
        query.addScalar("createDate", new DateType());
        query.addScalar("updateDate", new DateType());
        
        query.addScalar("createUserId", new LongType());
        query.addScalar("updateUserId", new LongType());

        query.setResultTransformer(Transformers.aliasToBean(ManageHcqtDTO.class));
        if(obj.getPage() != null && obj.getPageSize() != null) {
            query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
            query.setMaxResults(obj.getPageSize().intValue());
        }
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

        return query.list();
    }
    
    // AutocompleteDept
    @SuppressWarnings("unchecked")
    public List<ConstructionDTO> getForAutoCompleteConstruction(ConstructionDTO obj) {
        StringBuilder sql = new StringBuilder().append( " select construction_id constructionId , code code, name name from construction where status != 0 ");
        if(obj.getListCode() != null && obj.getListCode().size() > 0) {
        	sql.append(" AND  code in :listCode ");
        }
        sql.append(" ORDER BY construction_id");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.addScalar("constructionId", new LongType());
        query.addScalar("name", new StringType());
        query.addScalar("code", new StringType());
        if(obj.getListCode() != null && obj.getListCode().size() > 0) {
        	query.setParameterList("listCode", obj.getListCode());
        }
        query.setResultTransformer(Transformers.aliasToBean(ConstructionDTO.class));


        return query.list();
    }
    
    // AutocompleteDept
    @SuppressWarnings("unchecked")
    public List<CntContractDTO> getForAutoCompleteContract(CntContractDTO obj) {
        StringBuilder sql = new StringBuilder().append(" select cnt_contract_id cntContractId , code code, name name from cnt_contract where status != 0  ");
        if(obj.getListCode() != null && obj.getListCode().size() > 0) {
        	sql.append(" AND  code in :listCode ");
        }
        sql.append(" ORDER BY cnt_contract_id");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.addScalar("cntContractId", new LongType());
        query.addScalar("name", new StringType());
        query.addScalar("code", new StringType());
        if(obj.getListCode() != null && obj.getListCode().size() > 0) {
        	query.setParameterList("listCode", obj.getListCode());
        }
        query.setResultTransformer(Transformers.aliasToBean(CntContractDTO.class));


        return query.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<DepartmentDTO> getForAutoCompleteDept(DepartmentDTO obj) {
        StringBuilder sql = new StringBuilder().append("SELECT " + " ST.SYS_GROUP_ID id" + ",(ST.CODE ||'-' || ST.NAME) text" + " ,ST.NAME name"
                + " ,ST.CODE code" + " FROM CTCT_CAT_OWNER.SYS_GROUP ST" + " WHERE ST.STATUS = 1 AND ST.GROUP_LEVEL = 2 ");
        if(obj.getListCode() != null && obj.getListCode().size() > 0) {
        	sql.append(" AND  ST.CODE in :listCode ");
        }
        StringBuilder stringBuilder = new StringBuilder(sql);
        stringBuilder.append(" ORDER BY ST.CODE");

        SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
        query.addScalar("id", new LongType());
        query.addScalar("name", new StringType());
        query.addScalar("text", new StringType());
        query.addScalar("code", new StringType());
        if(obj.getListCode() != null && obj.getListCode().size() > 0) {
        	query.setParameterList("listCode", obj.getListCode());
        }
        query.setResultTransformer(Transformers.aliasToBean(DepartmentDTO.class));
        return query.list();
    }
    
    public void saveManageHcqt(ManageHcqtDTO manageHcqtDTO) {
		Session ss = getSession();
		ss.save(manageHcqtDTO.toModel());
	}
    
    public void updateManageVttbByCommand(ManageVttbDTO obj) {
		StringBuilder sql = new StringBuilder("update MANAGE_VTTB set "
				+ " contract_type=:contractType,"
				+ " update_date=:updateDate,"
				+ " update_user_id=:updateUserId,"
				+ " construction_status=:constructionStatus ") ;
				sql.append(" where MANAGE_VTTB_ID=:manageVttbId ");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("contractType", obj.getContractType());
		query.setParameter("constructionStatus", obj.getConstructionStatus());
		query.setParameter("manageVttbId", obj.getManageVttbId());
		query.setParameter("updateDate", obj.getUpdateDate());
		query.setParameter("updateUserId", obj.getUpdateUserId());
		
		query.executeUpdate();
	}
	@SuppressWarnings("unchecked")
	public List<AMaterialRecoveryListModelDTO> device(ManageHcqtDTO obj) {
		StringBuilder sql= new StringBuilder(" SELECT am.mer_entity_id merentityid,am.mer_name AS mername,am.serial_number serialnumber,"
				+ " case when am.unit_id=0 then am.unit_name else d.name end name,"
				+ " am.actual_receive_quantity AS handoverquantity," 
				+ " am.unit_id unitid,"
				+ " nvl(to_char(am.mer_entity_id),am.mer_name) merCompare,  "
				+ " t1.code constructionCode ,  "
				+ " t2.code stationCode ,  "
				+ " t3.code provinceCode,  "
				+ " t1.status constructionStatus  "
				+ " FROM a_material_handover_mer_list am "
				+ " JOIN a_material_handover b ON ( am.a_material_handover_id = b.a_material_handover_id and b.IS_ACTIVE=1 ) "
				+ " left JOIN cat_unit d ON am.unit_id = d.cat_unit_id  "
				+ " left join CONSTRUCTION t1 on b.construct_id = t1.construction_id  "
				+ " left join CAT_STATION t2 on t1.cat_station_id = t2.CAT_STATION_ID  "
				+ " left join CAT_PRovince t3 on t2.CAT_PROVINCE_ID = t3.CAT_PROVINCE_ID  "
				+ " WHERE 1 = 1 ")
//				+ " b.construct_id = :constructId ";
				;
		if(StringUtils.isNotEmpty(obj.getKeySearch())) {
			sql.append(" and  upper(t1.code) like upper(:keySearch) escape '&' ");
		}
		if(StringUtils.isNotEmpty(obj.getStatus())) {
			sql.append(" and  t1.status = :status ");
		}
		if(obj.getProvinceIds() != null && obj.getProvinceIds().size() > 0) {
			sql.append(" and t3.cat_province_id in (:provinceIds) ");
		}
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
	    sqlCount.append(sql.toString());
	    sqlCount.append(")");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
		query.addScalar("merEntityId", LongType.INSTANCE);
		query.addScalar("merName", StringType.INSTANCE);
		query.addScalar("serialNumber", StringType.INSTANCE);
		query.addScalar("name", StringType.INSTANCE);
		query.addScalar("handoverQuantity", DoubleType.INSTANCE);
		query.addScalar("unitId", LongType.INSTANCE);
		query.addScalar("merCompare", StringType.INSTANCE);
		query.addScalar("constructionCode", StringType.INSTANCE);
		query.addScalar("stationCode", StringType.INSTANCE);
		query.addScalar("provinceCode", StringType.INSTANCE);
		query.addScalar("constructionStatus", StringType.INSTANCE);
		query.setResultTransformer(Transformers.aliasToBean(AMaterialRecoveryListModelDTO.class));
		// query.setParameter("constructId", constructId);
		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		if(StringUtils.isNotEmpty(obj.getKeySearch())) {
			query.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch()) + "%");
			queryCount.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch()) + "%");
		}
		if(StringUtils.isNotEmpty(obj.getStatus())) {
			query.setParameter("status", obj.getStatus());
			queryCount.setParameter("status", obj.getStatus());
		}
		if(obj.getProvinceIds() != null && obj.getProvinceIds().size() > 0) {
			query.setParameterList("provinceIds", obj.getProvinceIds());
			queryCount.setParameterList("provinceIds", obj.getProvinceIds());
		}
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
		List<AMaterialRecoveryListModelDTO> list = query.list();
		return list;

	}
	@SuppressWarnings("unchecked")
	public List<AMaterialRecoveryListModelDTO> materials(Long constructId) {
		String sql=" SELECT am.mer_name AS mername,case when am.unit_id=0 then am.unit_name else d.name end name,am.mer_entity_id merid,am.unit_id unitid,"
				+ " am.actual_receive_quantity AS handoverquantity, am.actual_receive_quantity AS acceptquantity,nvl(to_char(am.mer_entity_id),am.mer_name) merCompare, "
				+ " t1.code constructionCode ,  "
				+ " t2.code stationCode ,  "
				+ " t3.code provinceCode,  "
				+ " t1.status constructionStatus  "
				+ " FROM a_material_handover_mer_list am "
				+ " JOIN a_material_handover b ON ( am.a_material_handover_id = b.a_material_handover_id and b.IS_ACTIVE=1 ) "
				+ " left JOIN cat_unit d ON am.unit_id = d.cat_unit_id  "
				+ " left join CONSTRUCTION t1 on b.construct_id = t1.construction_id  "
				+ " left join CAT_STATION t2 on t1.cat_station_id = t2.CAT_STATION_ID  "
				+ " left join CAT_PRovince t3 on t2.CAT_PROVINCE_ID = t3.CAT_PROVINCE_ID  "
				+ " WHERE "
				+ " am.MER_ENTITY_ID is null ";
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("merID", LongType.INSTANCE);
		query.addScalar("merName", StringType.INSTANCE);
		query.addScalar("name", StringType.INSTANCE);
		query.addScalar("handoverQuantity", DoubleType.INSTANCE);
		query.addScalar("acceptQuantity", DoubleType.INSTANCE);
		query.addScalar("unitId", LongType.INSTANCE);
		query.addScalar("merCompare", StringType.INSTANCE);
		query.addScalar("constructionCode", StringType.INSTANCE);
		query.addScalar("stationCode", StringType.INSTANCE);
		query.addScalar("provinceCode", StringType.INSTANCE);
		query.addScalar("constructionStatus", StringType.INSTANCE);
//		query.setParameter("constructId", constructId);
		query.setResultTransformer(Transformers.aliasToBean(AMaterialRecoveryListModelDTO.class));

		List<AMaterialRecoveryListModelDTO> list = query.list();
		return list;

	}
	@SuppressWarnings("unchecked")
	public List<AMaterialRecoveryListDTO> checkSum(Long constructId) {
		StringBuffer sql = new StringBuffer(
				"SELECT"
						+ " MER_ENTITY_ID merEntityId,"
						+ " SUM(RECOVERY_QUANTITY) sumRecoveryQuantity,"
						+ " SERIAL_NUMBER serialNumber,"
						+ " nvl(to_char(A_MATERIAL_RECOVERY_LIST.mer_entity_id),"
						+ " A_MATERIAL_RECOVERY_LIST.mer_name) merCompare  "
						+ " FROM A_MATERIAL_RECOVERY_LIST " + "INNER JOIN A_MATERIAL_RECOVERY_MINUTES AMRM "
						+ " ON AMRM.A_MATERIAL_RECOVERY_MINUTES_ID = A_MATERIAL_RECOVERY_LIST.A_MATERIAL_RECOVERY_MINUTES_ID "
						+ " JOIN construction VC ON VC.construction_id = AMRM.CONSTRUCT_ID "
						+ " WHERE AMRM.IS_ACTIVE = 1 "
//						+ " AND AMRM.CONSTRUCT_ID = :constructId  " 
						+ " GROUP BY MER_ENTITY_ID,SERIAL_NUMBER,"
						+ " nvl(TO_CHAR(a_material_recovery_list.mer_entity_id),a_material_recovery_list.mer_name) ");
		

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("merEntityId", LongType.INSTANCE);
		query.addScalar("serialNumber", StringType.INSTANCE);
		query.addScalar("sumRecoveryQuantity", DoubleType.INSTANCE);
		query.addScalar("merCompare", StringType.INSTANCE);
//		query.setParameter("constructId", constructId);

		query.setResultTransformer(Transformers.aliasToBean(AMaterialRecoveryListDTO.class));

		List<AMaterialRecoveryListDTO> list = query.list();
		return list;
	}
    //tatph -end - 20112019
    
}
