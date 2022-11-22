package com.viettel.coms.dao;

import java.math.BigDecimal;
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
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.viettel.cat.dto.CatProvinceDTO;
import com.viettel.coms.bo.ProgressPlanProjectBO;
import com.viettel.coms.bo.UtilAttachDocumentBO;
import com.viettel.coms.dto.ProgressPlanProjectDTO;
import com.viettel.coms.dto.UtilAttachDocumentDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import com.viettel.wms.utils.ValidateUtils;

@EnableTransactionManagement
@Repository("progressPlanProjectDAO")
public class ProgressPlanProjectDAO extends BaseFWDAOImpl<ProgressPlanProjectBO, Long> {

	public ProgressPlanProjectDAO() {
		this.model = new ProgressPlanProjectBO();
	}

	public ProgressPlanProjectDAO(Session session) {
		this.session = session;
	}
	
	public List<ProgressPlanProjectDTO> doSearch(ProgressPlanProjectDTO obj){
		StringBuilder sql = new StringBuilder("SELECT ");
		sql.append("pp.PROGRESS_PLAN_PROJECT_ID progressPlanProjectId, ")
		.append("pp.PROVINCE_ID provinceId, ")
		.append("pp.PROVINCE_CODE provinceCode, ")
		.append("pp.AREA_CODE areaCode, ")
		.append("pp.DISTRICT_CODE districtCode, ")
		.append("pp.COMMUNE_CODE communeCode, ")
		.append("pp.PROJECT_NAME projectName, ")
		.append("pp.ADDRESS address, ")
		.append("pp.INVESTOR_NAME investorName, ")
		.append("pp.PROJECT_PERFORMANCE projectPerformance, ")
		.append("pp.PROJECT_TYPE projectType, ")
		.append("pp.NUMBER_HOUSE numberHouse, ")
		.append("pp.NUMBER_BLOCK numberBlock, ")
		.append("pp.ACREAGE acreage, ")
		.append("pp.PROGRESS_PROJECT progressProject, ")
		.append("pp.DEPLOYMENT_MOBILE deploymentMobile, ")
		.append("pp.CREATE_USER_ID createUserId, ")
		.append("pp.CREATE_DATE createDate, ")
		.append("pp.UPDATE_USER_ID updateUserId, ")
		.append("pp.UPDATE_DATE updateDate, ")
		.append("pp.STATUS status, ")
		.append("pp.CONTACT_CUS contactCus, ")
		.append("pp.POSITION_CUS positionCus, ")
		.append("pp.PHONE_NUMBER_CUS phoneNumberCus, ")
		.append("pp.EMAIL_CUS emailCus, ")
		.append("pp.CONTACT_EMPLOY contactEmploy, ")
		.append("pp.PHONE_NUMBER_EMPLOY phoneNumberEmploy, ")
		.append("pp.EMAIL_EMPLOY emailEmploy, ")
		.append("pp.DEADLINE_DATE_COMPLETE deadlineDateComplete, ")
		.append("pp.DATE_EXPOSED dateExposed, ")
		.append("pp.LEVEL_DEPLOYMENT levelDeployment, ")
		.append("pp.CONTRACTING_STATUS contractingStatus, ")
		.append("pp.DATE_CONTRACT dateContract, ")
		.append("pp.CONTRACT_ID contractId, ")
		.append("pp.NOTE note, ")
		.append("cc.CODE contractCode ");
		sql.append(" FROM PROGRESS_PLAN_PROJECT pp ");
		sql.append(" LEFT JOIN CNT_CONTRACT cc on cc.CNT_CONTRACT_ID = pp.CONTRACT_ID ");
		sql.append(" WHERE 1=1 ");
//		sql.append(" AND PROVINCE_ID in (:groupIdList)");
		
		if(StringUtils.isNotBlank(obj.getProvinceCode())) {
			sql.append(" and pp.PROVINCE_CODE = :provinceCode ");
		}
		
		if(StringUtils.isNotBlank(obj.getProjectName())) {
			sql.append(" and upper(pp.PROJECT_NAME) like upper(:projectName) escape '&' ");
		}
		
		sql.append(" ORDER BY pp.PROGRESS_PLAN_PROJECT_ID DESC ");
		
		StringBuilder sqlCount = new StringBuilder("select count(*) from (" + sql.toString() + ")");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
		
		query.addScalar("progressPlanProjectId", new LongType());
		query.addScalar("provinceId", new LongType());
		query.addScalar("provinceCode", new StringType());
		query.addScalar("areaCode", new StringType());
		query.addScalar("districtCode", new StringType());
		query.addScalar("communeCode", new StringType());
		query.addScalar("projectName", new StringType());
		query.addScalar("address", new StringType());
		query.addScalar("investorName", new StringType());
		query.addScalar("projectPerformance", new StringType());
		query.addScalar("projectType", new StringType());
		query.addScalar("numberHouse", new LongType());
		query.addScalar("numberBlock", new LongType());
		query.addScalar("acreage", new DoubleType());
		query.addScalar("progressProject", new StringType());
		query.addScalar("deploymentMobile", new StringType());
		query.addScalar("createUserId", new LongType());
		query.addScalar("createDate", new DateType());
		query.addScalar("updateUserId", new LongType());
		query.addScalar("updateDate", new DateType());
		query.addScalar("status", new StringType());
		query.addScalar("contactCus", new StringType());
		query.addScalar("positionCus", new StringType());
		query.addScalar("phoneNumberCus", new StringType());
		query.addScalar("emailCus", new StringType());
		query.addScalar("contactEmploy", new StringType());
		query.addScalar("phoneNumberEmploy", new StringType());
		query.addScalar("emailEmploy", new StringType());
		query.addScalar("deadlineDateComplete", new DateType());
		query.addScalar("dateExposed", new DateType());
		query.addScalar("levelDeployment", new StringType());
		query.addScalar("contractingStatus", new StringType());
		query.addScalar("dateContract", new DateType());
		query.addScalar("contractId", new LongType());
		query.addScalar("note", new StringType());
		query.addScalar("contractCode", new StringType());
		
//		query.setParameterList("groupIdList", groupIdList);
//		queryCount.setParameterList("groupIdList", groupIdList);
		
		if(StringUtils.isNotBlank(obj.getProvinceCode())) {
			query.setParameter("provinceCode", obj.getProvinceCode());
			queryCount.setParameter("provinceCode", obj.getProvinceCode());
		}
		
		if(StringUtils.isNotBlank(obj.getProjectName())) {
			query.setParameter("projectName", "%" + obj.getProjectName() + "%");
			queryCount.setParameter("projectName", "%" + obj.getProjectName() + "%");
		}
		
		query.setResultTransformer(Transformers.aliasToBean(ProgressPlanProjectDTO.class));
		
		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
    public List<CatProvinceDTO> doSearchProvinceByRolePopup(CatProvinceDTO obj) {
        StringBuilder sql = new StringBuilder(
                "SELECT CAT_PROVINCE_ID catProvinceId, NAME name, STATUS status, CODE code, AREA_CODE areaCode FROM CAT_PROVINCE cpro ");
        sql.append(" WHERE 1=1 "
//        		+ "AND CAT_PROVINCE_ID in (:groupIdList) "
        		);
        if (StringUtils.isNotEmpty(obj.getName())) {
            sql.append(" AND upper(cpro.NAME) LIKE upper(:name) escape '&' OR upper(cpro.CODE) LIKE upper(:name) escape '&' ");
        }
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        query.addScalar("catProvinceId", new LongType());
        query.addScalar("name", new StringType());
        query.addScalar("status", new StringType());
        query.addScalar("code", new StringType());
        query.addScalar("areaCode", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(CatProvinceDTO.class));
//        query.setParameterList("groupIdList", groupIdList);
//        queryCount.setParameterList("groupIdList", groupIdList);
        if (StringUtils.isNotEmpty(obj.getName())) {
            query.setParameter("name", "%" + ValidateUtils.validateKeySearch(obj.getName()) + "%");
            queryCount.setParameter("name", "%" + ValidateUtils.validateKeySearch(obj.getName()) + "%");
        }
        if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
        return query.list();
    }
	
	public List<ProgressPlanProjectDTO> getCntContractInHtct(ProgressPlanProjectDTO obj) {
		StringBuilder sql = new StringBuilder(" select CNT_CONTRACT_ID contractId, " + 
				"code contractCode, " + 
				"SIGN_DATE dateContract " +
				"from CNT_CONTRACT  " + 
				"where CONTRACT_TYPE=8  " + 
				"and status!=0 ");
		
		if(StringUtils.isNotBlank(obj.getKeySearch())) {
			sql.append(" AND (upper(code) like upper(:keySearch) escape '&') ");
		}
		
		if(obj.getLstContract()!=null && obj.getLstContract().size()>0) {
			sql.append(" AND (upper(code) in (:lstContract)) ");
		}
		
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
		
		query.addScalar("contractId", new LongType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("dateContract", new DateType());
		
		if (StringUtils.isNotEmpty(obj.getKeySearch())) {
            query.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch()) + "%");
            queryCount.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch()) + "%");
        }
		
		if(obj.getLstContract()!=null && obj.getLstContract().size()>0) {
			query.setParameterList("lstContract", obj.getLstContract());
            queryCount.setParameterList("lstContract", obj.getLstContract());
		}
		
		query.setResultTransformer(Transformers.aliasToBean(ProgressPlanProjectDTO.class));
		
		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
		
		return query.list();
	}
	
	public List<ProgressPlanProjectDTO> getProvinceByListId(List<String> lstProvince) {
		StringBuilder sql = new StringBuilder(" SELECT CAT_PROVINCE_ID provinceId, "
				+ " CODE provinceCode, "
				+ " AREA_CODE areaCode "
				+ " FROM CAT_PROVINCE WHERE STATUS!=0 AND UPPER(CODE) IN (:lstProvince) ");
		
        SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("provinceId", new LongType());
		query.addScalar("provinceCode", new StringType());
		query.addScalar("areaCode", new StringType());
		
        query.setParameterList("lstProvince", lstProvince);
		
		query.setResultTransformer(Transformers.aliasToBean(ProgressPlanProjectDTO.class));
		return query.list();
	}
	
	public List<ProgressPlanProjectDTO> checkValidateProjectCdt(String keyProjectCdt, List<String> lstProjectCdt){
		StringBuilder sql = new StringBuilder(" SELECT PROGRESS_PLAN_PROJECT_ID progressPlanProjectId,"
				+ "project_Name projectName,"
				+ "address address,"
				+ "investor_Name investorName "
				+ " FROM PROGRESS_PLAN_PROJECT WHERE 1=1 ");
		
		if(keyProjectCdt!=null) {
			sql.append(" AND upper(project_Name || '+' || address || '+' || investor_Name)  = upper(:keyProjectCdt) ");
		}
		
		if(lstProjectCdt!=null && lstProjectCdt.size()>0) {
			sql.append(" AND upper(project_Name || '+' || address || '+' || investor_Name) in (:lstProjectCdt) ");
		}
		
        SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("progressPlanProjectId", new LongType());
		query.addScalar("projectName", new StringType());
		query.addScalar("address", new StringType());
		query.addScalar("investorName", new StringType());
		
		if(keyProjectCdt!=null) {
			query.setParameter("keyProjectCdt", keyProjectCdt);
		}
		
		if(lstProjectCdt!=null && lstProjectCdt.size()>0) {
			query.setParameterList("lstProjectCdt", lstProjectCdt);
		}
		
		query.setResultTransformer(Transformers.aliasToBean(ProgressPlanProjectDTO.class));
		return query.list();
	}
	
	public ProgressPlanProjectDTO getCodeById(Long projectId){
		StringBuilder sql = new StringBuilder(" SELECT UPPER(project_Name || '+' || address || '+' || investor_Name) projectName, "
				+ " EMAIL_EMPLOY emailEmploy "
				+ " FROM PROGRESS_PLAN_PROJECT WHERE 1=1 ");
		sql.append(" AND PROGRESS_PLAN_PROJECT_ID = :projectId ");
		
        SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("projectName", new StringType());
		query.addScalar("emailEmploy", new StringType());
		
		query.setParameter("projectId", projectId);
		
		query.setResultTransformer(Transformers.aliasToBean(ProgressPlanProjectDTO.class));
		
		List<ProgressPlanProjectDTO> ls = query.list();
		if(ls.size()>0) {
			return ls.get(0);
		}
		return null;
	}
	
	public ProgressPlanProjectDTO getInfoUserBySysUser(Long sysGroupId){
		StringBuilder sql = new StringBuilder(" select PHONE_NUMBER phoneNumberEmploy, " + 
				" EMAIL emailEmploy " + 
				" from CONFIG_USER_SMS_HTCT " + 
				" where PROVINCE_CODE = ( " + 
				"  select sg.PROVINCE_CODE " + 
				"  from SYS_GROUP sg  " + 
				"  where sg.SYS_GROUP_ID = :sysGroupId " + 
				"  ) ");
		
        SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("phoneNumberEmploy", new StringType());
		query.addScalar("emailEmploy", new StringType());
		
		query.setParameter("sysGroupId", sysGroupId);
		
		query.setResultTransformer(Transformers.aliasToBean(ProgressPlanProjectDTO.class));
		
		List<ProgressPlanProjectDTO> ls = query.list();
		if(ls.size()>0) {
			return ls.get(0);
		}
		return null;
	}
	
	public void deleteFile(Long objId, String type) {
		StringBuilder sql = new StringBuilder("DELETE FROM UTIL_ATTACH_DOCUMENT WHERE OBJECT_ID=:objId AND TYPE=:type");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("objId", objId);
		query.setParameter("type", type);
		query.executeUpdate();
	}
	
	public List<UtilAttachDocumentDTO> getListFile(Long objId){
		StringBuilder sql = new StringBuilder("select UTIL_ATTACH_DOCUMENT_ID utilAttachDocumentId,");
        sql.append(" NAME name, CREATED_DATE createdDate, CREATED_USER_NAME createdUserName,FILE_PATH filePath "
				+ " FROM UTIL_ATTACH_DOCUMENT WHERE TYPE='IBS' AND OBJECT_ID=:objId");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.addScalar("utilAttachDocumentId", new LongType());
        query.addScalar("name", new StringType());
        query.addScalar("createdDate", new DateType());
        query.addScalar("createdUserName", new StringType());
        query.addScalar("filePath", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(UtilAttachDocumentDTO.class));
		query.setParameter("objId", objId);
		return query.list();
	}
}
