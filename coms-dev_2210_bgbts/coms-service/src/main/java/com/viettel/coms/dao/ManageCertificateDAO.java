package com.viettel.coms.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.viettel.coms.bo.ManageCareerBO;
import com.viettel.coms.bo.ManageCertificateBO;
import com.viettel.coms.bo.WoBO;
import com.viettel.coms.dto.ManageCareerDTO;
import com.viettel.coms.dto.ManageCertificateDTO;
import com.viettel.coms.dto.WoDTO;
import com.viettel.erp.utils.JsonDateDeserializer;
import com.viettel.erp.utils.JsonDateSerializerDate;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import com.viettel.wms.utils.ValidateUtils;
//Duonghv13-start 21092021
@Repository("manageCertificateDAO")
@Transactional
public class ManageCertificateDAO extends BaseFWDAOImpl<ManageCertificateBO,Long> {


    public ManageCertificateDAO() {
        this.model = new ManageCertificateBO();
    }

    public ManageCertificateDAO(Session session) {
        this.session = session;
    }
    
    @SuppressWarnings("unchecked")
    public List<ManageCertificateDTO> doSearch(ManageCertificateDTO criteria) {
        StringBuilder stringBuilder = new StringBuilder(
                "SELECT T3.LOGIN_NAME loginName," + "T3.FULL_NAME fullName,"
                        + " T5.NAME sysGroupName," + "T1.EMAIL email," + "T1.PHONE_NUMBER phoneNumber,"
                        + " T1.POSITION_NAME positionName," + "T1.PRACTICE_POINT practicePoint," + "T1.THEORETICAL_POINT theoreticalPoint,"
                        + " T1.CERTIFICATE_ID certificateId,"
                        + " T1.CERTIFICATE_CODE certificateCode," + "T1.CERTIFICATE_NAME certificateName," + "T2.NAME careerName," + "T2.CAREER_ID careerId,"
                        + " T1.UNIT_CREATED unitCreated," + "T1.START_DATE startDate," + "T1.FINISH_DATE finishDate,"
                        +"  CASE  WHEN (T1.finish_date  -  sysdate) >= 30 THEN 1  WHEN ((T1.finish_date  -  sysdate)< 30) THEN 2  ELSE 3 "
                        +" END as certificateStatus,"
                        + " T1.STATUS status," + "T6.STATUS approveStatus"
                        + " FROM  CERTIFICATE T1 "
                        + " LEFT JOIN CAREER T2 ON T1.CAREER_ID = T2.CAREER_ID"
                        + " LEFT JOIN SYS_USER T3 ON T1.SYS_USER_ID = T3.SYS_USER_ID"
                        + " LEFT JOIN SYS_GROUP T4 ON T3.SYS_GROUP_ID = T4.SYS_GROUP_ID" 
                        + " LEFT JOIN CAT_PROVINCE T5 ON T4.PROVINCE_ID = T5.CAT_PROVINCE_ID" 
                        + " LEFT JOIN CERTIFICATE_EXTEND T6  ON T1.CERTIFICATE_ID = T6.CERTIFICATE_ID"         
                        + " where 1=1 and T1.STATUS = 1 "
                        + " AND T6.CERTIFICATE_EXTEND_ID IN (SELECT MAX(CERTIFICATE_EXTEND_ID) FROM CERTIFICATE_EXTEND where CERTIFICATE_ID = T1.CERTIFICATE_ID and STATUS > 0 ) ");
        if (null != criteria.getCertificateCode()) {
            stringBuilder
                    .append("AND (upper(T1.CERTIFICATE_CODE) like upper(:certificateCode) or upper(T1.CERTIFICATE_NAME) like :certificateCode) ");
        }
        if (null != criteria.getStatusList() && criteria.getStatusList().size()>0) {
        	stringBuilder.append("AND (CASE " + 
        			"  WHEN (T1.finish_date  -  sysdate >= 30) THEN 1 " + 
        			"  WHEN ((T1.finish_date  -  sysdate)< 30) THEN 2 " + 
        			"  ELSE 3 "+ 
        			"  END) in (:statusList) ");
        }
        if(null != criteria.getApproveList() && criteria.getApproveList().size()>0) {
        	stringBuilder.append("AND T6.STATUS in (:approveList) ");
        }
        if (criteria.getFullName() != null){
        	stringBuilder.append(" AND ( upper(T3.LOGIN_NAME)  LIKE upper(:fullName) or upper(T3.FULL_NAME)  LIKE upper(:fullName) ) ");
		}
        if(criteria.getSysGroupId()!=null) {
        	stringBuilder.append("AND T4.SYS_GROUP_ID in (select SYS_GROUP_ID from SYS_GROUP where parent_id = ( select parent_id from SYS_GROUP where sys_group_id = :sysGroupId )) ");
        }
        
        if(criteria.getCareerId()!=null) {
        	stringBuilder.append("AND T2.CAREER_ID = :careerId");
        }
        
        stringBuilder.append(" ORDER BY T1.CERTIFICATE_CODE");
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(stringBuilder.toString());
        sqlCount.append(")");

        SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
        query.addScalar("loginName", new StringType());
        query.addScalar("fullName", new StringType());
        query.addScalar("sysGroupName", new StringType());
        query.addScalar("email", new StringType());
        query.addScalar("phoneNumber", new StringType());
        
        
        query.addScalar("positionName", new StringType());
        query.addScalar("practicePoint", new DoubleType());
        query.addScalar("theoreticalPoint", new DoubleType());
        query.addScalar("certificateId", new LongType());
        query.addScalar("certificateCode", new StringType());
        
        query.addScalar("certificateName", new StringType());
        query.addScalar("careerId", new LongType());
        query.addScalar("careerName", new StringType());
        query.addScalar("unitCreated", new StringType());
        
        query.addScalar("startDate", new DateType());
        query.addScalar("status", new LongType());
        query.addScalar("certificateStatus", new LongType());
        query.addScalar("approveStatus", new LongType());
        query.addScalar("finishDate", new DateType());

        if (null != criteria.getCertificateCode()) {
            query.setParameter("certificateCode", "%" + criteria.getCertificateCode() + "%");
            queryCount.setParameter("certificateCode", "%" + criteria.getCertificateCode() + "%");
        }
        
        if (null != criteria.getStatusList() && criteria.getStatusList().size()>0 ) {
			query.setParameterList("statusList",criteria.getStatusList());
			queryCount.setParameterList("statusList", criteria.getStatusList());
		}
        
        if(null != criteria.getApproveList() && criteria.getApproveList().size()>0) {
            query.setParameterList("approveList", criteria.getApproveList());
            queryCount.setParameterList("approveList",criteria.getApproveList());
        }
        if (criteria.getFullName() != null){
        	query.setParameter("fullName", "%" + criteria.getFullName() + "%");
            queryCount.setParameter("fullName", "%" + criteria.getFullName() + "%");
		}
        if(criteria.getSysGroupId()!=null) {
        	query.setParameter("sysGroupId", criteria.getSysGroupId());
            queryCount.setParameter("sysGroupId",criteria.getSysGroupId());
        }
        if(criteria.getCareerId()!=null) {
        	query.setParameter("careerId", criteria.getCareerId());
            queryCount.setParameter("careerId",criteria.getCareerId());
        }
        
        query.setResultTransformer(Transformers.aliasToBean(ManageCertificateDTO.class));
        if (criteria.getPage() != null && criteria.getPageSize() != null) {
            query.setFirstResult((criteria.getPage().intValue() - 1)
                    * criteria.getPageSize().intValue());
            query.setMaxResults(criteria.getPageSize().intValue());
        }

        List ls = query.list();
        criteria.setTotalRecord(((BigDecimal) queryCount.uniqueResult())
                .intValue());
        return ls;
    }

    public ManageCertificateDTO findByCertificateCodeAndUser(String code,Long careerId, String loginName) {
    	StringBuilder stringBuilder = new StringBuilder(
                "SELECT T3.LOGIN_NAME loginName," + "T3.FULL_NAME fullName,"
                        + " T4.NAME sysGroupName," + "T1.EMAIL email," + "T1.PHONE_NUMBER phoneNumber,"
                        + " T1.POSITION_NAME positionName," + "T1.PRACTICE_POINT practicePoint," + "T1.THEORETICAL_POINT theoreticalPoint,"
                        + " T1.CERTIFICATE_ID certificateId,"
                        + " T1.CERTIFICATE_CODE certificateCode," + "T1.CERTIFICATE_NAME certificateName," + "T2.NAME careerName,"
                        + " T1.UNIT_CREATED unitCreated," + "T1.START_DATE startDate," + "T1.FINISH_DATE finishDate,"
                        + " T1.STATUS status," + "T5.STATUS approveStatus"
                        + " FROM  CERTIFICATE T1 "
                        + " LEFT JOIN CAREER T2 ON T1.CAREER_ID = T2.CAREER_ID"
                        + " LEFT JOIN SYS_USER T3 ON T1.SYS_USER_ID = T3.SYS_USER_ID"
                        + " LEFT JOIN SYS_GROUP T4 ON T3.SYS_GROUP_ID = T4.SYS_GROUP_ID"
                        + " LEFT JOIN CERTIFICATE_EXTEND T5  ON T1.CERTIFICATE_ID = T5.CERTIFICATE_ID"  
                        + " where 1=1"
                        + " and T1.STATUS > 0 and T5.STATUS > 0 "
                        + " AND T5.CERTIFICATE_EXTEND_ID IN  (SELECT MAX(CERTIFICATE_EXTEND_ID) FROM CERTIFICATE_EXTEND where CERTIFICATE_ID = T1.CERTIFICATE_ID and STATUS > 0 ) "
                        + " AND upper(T1.CERTIFICATE_CODE) like upper(:code) "
                        + " AND T1.CAREER_ID = :careerId "
                        + " AND T3.LOGIN_NAME like (:loginName) ");

        SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
        query.addScalar("loginName", new StringType());
        query.addScalar("fullName", new StringType());
        query.addScalar("sysGroupName", new StringType());
        query.addScalar("email", new StringType());
        query.addScalar("phoneNumber", new StringType());
        
        
        query.addScalar("positionName", new StringType());
        query.addScalar("practicePoint", new DoubleType());
        query.addScalar("theoreticalPoint", new DoubleType());
        query.addScalar("certificateId", new LongType());
        query.addScalar("certificateCode", new StringType());
        
        query.addScalar("certificateName", new StringType());
        query.addScalar("careerName", new StringType());
        query.addScalar("unitCreated", new StringType());
        
        query.addScalar("startDate", new DateType());
        query.addScalar("status", new LongType());
        query.addScalar("approveStatus", new LongType());
        query.addScalar("finishDate", new DateType());

        query.setParameter("code", code);
        query.setParameter("careerId", careerId);
        query.setParameter("loginName", loginName);
        query.setResultTransformer(Transformers.aliasToBean(ManageCertificateDTO.class));
        return (ManageCertificateDTO) query.uniqueResult();
    }

    public ManageCertificateDTO getOneCertificateDetails(Long certificateId) {
    	StringBuilder stringBuilder = new StringBuilder(
               "SELECT  T3.LOGIN_NAME loginName, T3.FULL_NAME fullName, T4.SYS_GROUP_ID sysGroupId,"
            	+ " T4.CODE sysGroupCode,T4.NAME sysGroupName,T5.SYS_USER_ID sysUserId,"
            	+ " T5.EMAIL email,T5.PHONE_NUMBER phoneNumber, T5.POSITION_NAME positionName,"
            	+ " T5.PRACTICE_POINT practicePoint,T5.THEORETICAL_POINT theoreticalPoint,"
    		    + " T5.CERTIFICATE_ID certificateId,T5.CERTIFICATE_CODE certificateCode,T5.CERTIFICATE_NAME certificateName,"
    		    + " T2.CODE careerCode,"
    		    + " T5.CAREER_ID careerId,T2.NAME careerName,"
    		    + " T5.UNIT_CREATED unitCreated,T5.START_DATE startDate,T5.FINISH_DATE finishDate,T5.STATUS status,"
    		    + " T1.STATUS approveStatus,T1.CERTIFICATE_EXTEND_ID certificateExtendId,"
    		    + " T5.CREATED_DATE createdDate, T5.UPDATED_DATE updatedDate,T5.CREATED_USER createdUserId,T5.UPDATED_USER updatedUserId"
    		    + " FROM CERTIFICATE_EXTEND T1"
    		    + " LEFT JOIN CERTIFICATE T5 ON T1.CERTIFICATE_ID = T5.CERTIFICATE_ID"                           
    		    + " LEFT JOIN CAREER T2 ON T5.CAREER_ID = T2.CAREER_ID"   
    		    + " LEFT JOIN SYS_USER T3 ON T5.SYS_USER_ID = T3.SYS_USER_ID"   
    		    + " LEFT JOIN SYS_GROUP T4 ON T3.SYS_GROUP_ID = T4.SYS_GROUP_ID"     
    		    + "  WHERE 1 = 1 AND T5.STATUS > 0"
    		    + " AND T5.CERTIFICATE_ID = :certificateId"
    		    + " AND T1.CERTIFICATE_EXTEND_ID "
    		    + " IN ( SELECT MAX(CERTIFICATE_EXTEND_ID) FROM CERTIFICATE_EXTEND WHERE CERTIFICATE_ID = T5.CERTIFICATE_ID AND STATUS > 0 ) ");
        SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
        query.setParameter("certificateId",certificateId);
        query.addScalar("loginName", new StringType());
        query.addScalar("fullName", new StringType());
        query.addScalar("sysGroupId", new LongType());
        query.addScalar("sysGroupCode", new StringType());
        query.addScalar("sysGroupName", new StringType()); 
        query.addScalar("sysUserId", new LongType());     
        query.addScalar("email", new StringType());
        query.addScalar("phoneNumber", new StringType());
        query.addScalar("positionName", new StringType());
        query.addScalar("practicePoint", new DoubleType());
        query.addScalar("theoreticalPoint", new DoubleType());
        query.addScalar("certificateId", new LongType());
        query.addScalar("certificateCode", new StringType());
        query.addScalar("certificateName", new StringType());
        query.addScalar("careerId", new LongType());
        query.addScalar("careerCode", new StringType());
        query.addScalar("careerName", new StringType());   
        query.addScalar("unitCreated", new StringType());
        query.addScalar("startDate", new DateType());
        query.addScalar("status", new LongType());
        query.addScalar("approveStatus", new LongType());
        query.addScalar("certificateExtendId", new LongType());
        query.addScalar("finishDate", new DateType());
        query.addScalar("createdDate", new DateType());
        query.addScalar("createdUserId", new LongType());
        query.addScalar("updatedUserId", new LongType());
        query.addScalar("updatedDate", new DateType());
        query.setResultTransformer(Transformers.aliasToBean(ManageCertificateDTO.class));


        List<ManageCertificateDTO> ls = query.list();
        if(ls.size()>0) {
        	return ls.get(0);
        }
        return null;
    }

	public int delete(Long certificateId) {
		// TODO Auto-generated method stub
        StringBuilder sql = new StringBuilder("UPDATE CERTIFICATE set STATUS = 0 where CERTIFICATE_ID = :certificateId");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("certificateId", certificateId);
        return query.executeUpdate();  
    }
	
	public ManageCertificateBO getOneRaw(long Id) {
        return this.get(ManageCertificateBO.class, Id);
    }
	
	public ManageCertificateDTO getCertificateInfor(Long certificateId) {
    	StringBuilder stringBuilder = new StringBuilder(
                "SELECT  T1.CERTIFICATE_ID certificateId," 
                        + " T1.CERTIFICATE_CODE certificateCode," + "T1.CERTIFICATE_NAME certificateName " 
                        + " FROM  CERTIFICATE T1 "
                        + " where 1=1"
                        + " AND T1.STATUS > 0 "
                        + " AND T1.CERTIFICATE_ID = :certificateId ");
        SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
        query.setParameter("certificateId",certificateId);
        
        query.addScalar("certificateId", new LongType());
        query.addScalar("certificateCode", new StringType());
        query.addScalar("certificateName", new StringType());
        
        query.setResultTransformer(Transformers.aliasToBean(ManageCertificateDTO.class));
        return (ManageCertificateDTO) query.uniqueResult();
    }

	public Object getForAutoCompleteInSign(ManageCertificateDTO dto) {
		// TODO Auto-generated method stub
		String sql = "SELECT T1.CERTIFICATE_ID certificateId," 
                + " T1.CERTIFICATE_CODE certificateCode," + "T1.CERTIFICATE_NAME certificateName " 
                +" FROM  CERTIFICATE T1 "
                + " where 1=1"
                + " AND T1.STATUS > 0 ";

        StringBuilder stringBuilder = new StringBuilder(sql);

        stringBuilder.append(" AND ROWNUM <=10 ");
        if (StringUtils.isNotEmpty(dto.getCertificateName())) {
            stringBuilder.append(
                    " AND (upper(T1.CERTIFICATE_NAME) LIKE upper(:certificateName) escape '&' OR upper(T1.CERTIFICATE_CODE) LIKE upper(:certificateName) escape '&')");
        }

        stringBuilder.append(" ORDER BY T1.CERTIFICATE_ID");

        SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());

        query.addScalar("certificateId", new LongType());
        query.addScalar("certificateName", new StringType());
        query.addScalar("certificateCode", new StringType());

        query.setResultTransformer(Transformers.aliasToBean(ManageCertificateDTO.class));

        if (StringUtils.isNotEmpty(dto.getCertificateName())) {
            query.setParameter("certificateName", "%" + dto.getCertificateName() + "%");
        }

        return query.list();
	}

	public List<ManageCertificateDTO> getListCertificateEnableFT(ManageCertificateDTO certificateDTO) {
		// TODO Auto-generated method stub
		String sql = "SELECT T1.CERTIFICATE_ID certificateId," 
                + " T1.CERTIFICATE_CODE certificateCode," + "T1.CERTIFICATE_NAME certificateName, "  + "T1.FINISH_DATE finishDate, " 
                + " T2.CAREER_ID careerId," + "T2.WO_ID_LIST woIdList  " 
                + " FROM  CERTIFICATE T1 "
                + " LEFT JOIN CAREER T2 ON T1.CAREER_ID = T2.CAREER_ID AND T2.STATUS = '1' "
                + " where 1=1"
                + " AND T1.STATUS > 0 AND T1.FINISH_DATE > sysdate AND T1.APPROVE_STATUS = 2 ";

        StringBuilder stringBuilder = new StringBuilder(sql);

        if (certificateDTO.getSysUserId() != null) {
            stringBuilder.append(
                    " AND T1.SYS_USER_ID = :sysUserId ");
        }
        if (certificateDTO.getWoIdList() != null) {
            stringBuilder.append(" AND CONCAT(CONCAT(',',t2.wo_id_list ),',') LIKE :woIdList ");
        }

        stringBuilder.append(" ORDER BY T1.CERTIFICATE_ID");

        SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());

        query.addScalar("certificateId", new LongType());
        query.addScalar("certificateName", new StringType());
        query.addScalar("certificateCode", new StringType());
        query.addScalar("finishDate", new DateType());
        query.addScalar("careerId", new LongType());
        query.addScalar("woIdList", new StringType());

        query.setResultTransformer(Transformers.aliasToBean(ManageCertificateDTO.class));

        if (certificateDTO.getSysUserId() != null) {
        	query.setParameter("sysUserId", certificateDTO.getSysUserId());
        }
        if (certificateDTO.getWoIdList() != null) {
        	query.setParameter("woIdList", "%," + certificateDTO.getWoIdList() + ",%");
        }
        
        return query.list();
	}
	
	
    //DUONG-end
}
