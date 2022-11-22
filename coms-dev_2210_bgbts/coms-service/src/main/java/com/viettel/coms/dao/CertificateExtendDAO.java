package com.viettel.coms.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.viettel.asset.bo.MerHandOverInfo;
import com.viettel.coms.bo.CertificateExtendBO;
import com.viettel.coms.bo.ManageCareerBO;
import com.viettel.coms.dto.CertificateExtendDTO;
import com.viettel.coms.dto.ManageCareerDTO;
import com.viettel.coms.dto.SysUserCOMSDTO;
import com.viettel.coms.dto.UtilAttachDocumentDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import com.viettel.wms.utils.ValidateUtils;

@Repository("certificateExtendDAO")
public class CertificateExtendDAO extends BaseFWDAOImpl<CertificateExtendBO, Long> {


    public CertificateExtendDAO() {
        this.model = new CertificateExtendBO();
    }

    public CertificateExtendDAO(Session session) {
        this.session = session;
    }

    @SuppressWarnings("unchecked")
    public List<CertificateExtendDTO> doSearch(CertificateExtendDTO criteria) {
        StringBuilder stringBuilder = new StringBuilder(
                "SELECT T1.CERTIFICATE_EXTEND_ID certificateExtendId," + "T1.FINISH_DATE finishDate,"
                        + " T1.DESCRIPTION description," + "T1.CERTIFICATE_ID certificateId," + "T1.STATUS status,"
                        + " T1.REASON reason,"
                        + " T1.CREATED_DATE createdDate," + "T1.UPDATED_DATE updatedDate," 
                        + " T1.CREATED_USER createdUserId," + "T1.UPDATED_USER updatedUserId," 
                        + " T3.FULL_NAME updatedUserName," 
                        + " T2.NAME attachFileName," 
                        + " T2.FILE_PATH attachFileExtendPath" 
                        + " FROM CERTIFICATE_EXTEND T1 "
                        + " LEFT JOIN CTCT_CAT_OWNER.UTIL_ATTACH_DOCUMENT T2 ON  T2.OBJECT_ID = T1.CERTIFICATE_EXTEND_ID AND T2.TYPE ='QLCC' "
                        + " LEFT JOIN SYS_USER T3 ON T1.UPDATED_USER = T3.SYS_USER_ID "
                        + " where 1=1 AND T1.STATUS > 0 "
        		);
        if (null != criteria.getCertificateId()) {
            stringBuilder
                    .append("AND T1.CERTIFICATE_ID = :certificateId ");
        }
        
        stringBuilder.append("ORDER BY T1.CERTIFICATE_EXTEND_ID DESC ");
        
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(stringBuilder.toString());
        sqlCount.append(")");

        SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        query.addScalar("certificateExtendId", new LongType());
        query.addScalar("finishDate", new DateType());
        query.addScalar("description", new StringType());
        query.addScalar("certificateId", new LongType());
        query.addScalar("status", new LongType());
        query.addScalar("reason", new StringType());
        query.addScalar("attachFileName", new StringType());
        query.addScalar("attachFileExtendPath", new StringType());
        query.addScalar("createdDate", new DateType());
        query.addScalar("updatedDate", new DateType());
        query.addScalar("createdUserId", new LongType());
        query.addScalar("updatedUserId", new LongType());
        query.addScalar("updatedUserName", new StringType());

        if (null != criteria.getCertificateId()) {
            query.setParameter("certificateId", criteria.getCertificateId());
            queryCount.setParameter("certificateId",criteria.getCertificateId());
        }

        query.setResultTransformer(Transformers
                .aliasToBean(CertificateExtendDTO.class));
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

//    public ManageCareerDTO findBycode(String code) {
//        StringBuilder stringBuilder = new StringBuilder(
//                "select T1.CAREER_ID careerId," + "T1.CODE code,"
//                        + "T1.NAME name," + "T1.WO_ID_LIST woIdList," + "T1.STATUS status "
//                        + " FROM CAREER T1 "
//                        + " WHERE 1=1 AND upper(T1.CODE) like upper(:code)");
//
//        SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
//
//        query.addScalar("careerId", new LongType());
//        query.addScalar("code", new StringType());
//        query.addScalar("name", new StringType());
//        query.addScalar("woIdList", new StringType());
//        query.addScalar("status", new StringType());
//        query.setParameter("code", code);
//        query.setResultTransformer(Transformers
//                .aliasToBean(ManageCareerDTO.class));
//
//        return (ManageCareerDTO) query.uniqueResult();
//    }

    

    @SuppressWarnings("unchecked")
    public CertificateExtendDTO getById(Long certificateExtendId) {
        StringBuilder stringBuilder = new StringBuilder("SELECT ");
        stringBuilder.append("T1.CERTIFICATE_EXTEND_ID certificateExtendId ");
        stringBuilder.append(",T1.FINISH_DATE finishDate ");
        stringBuilder.append(",T1.DESCRIPTION description ");
        stringBuilder.append(",T1.CERTIFICATE_ID certificateId ");
        stringBuilder.append(",T1.STATUS status ");
        stringBuilder.append(",T1.CREATED_DATE createdDate ");
        stringBuilder.append(",T1.CREATED_USER createdUserId ");
        stringBuilder.append(", T2.NAME attachFileName "); 
        stringBuilder.append(", T2.FILE_PATH attachFileExtendPath ");
        
        stringBuilder.append(" FROM CERTIFICATE_EXTEND T1 ");
        stringBuilder.append(" LEFT JOIN CTCT_CAT_OWNER.UTIL_ATTACH_DOCUMENT T2 ON  T2.OBJECT_ID = T1.CERTIFICATE_EXTEND_ID AND T2.TYPE ='QLCC' ");
        stringBuilder
                .append("WHERE T1.CERTIFICATE_EXTEND_ID = :certificateExtendId ORDER BY T1.CREATED_DATE,T1.UPDATED_DATE DESC");

        SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());

        query.addScalar("certificateExtendId", new LongType());
        query.addScalar("finishDate", new DateType());
        query.addScalar("description", new StringType());
        query.addScalar("attachFileName", new StringType());
        query.addScalar("attachFileExtendPath", new StringType());
        query.addScalar("certificateId", new LongType());
        query.addScalar("status", new LongType());
        query.addScalar("createdDate", new DateType());
        query.addScalar("createdUserId", new LongType());
        query.setParameter("certificateExtendId", certificateExtendId);
        query.setResultTransformer(Transformers
                .aliasToBean(CertificateExtendDTO.class));
        return (CertificateExtendDTO) query.uniqueResult();
    }
    //DUONG-end

	public UtilAttachDocumentDTO getFileAttachByResultId(Long objectId, String type) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder(
				"SELECT UTIL_ATTACH_DOCUMENT_ID utilAttachDocumentId, "
				+ "OBJECT_ID objectId, NAME name, "
				+ "FILE_PATH filePath "
				+ "from UTIL_ATTACH_DOCUMENT "
				+ "where STATUS!=0 "
				+ "AND OBJECT_ID = :objectId "
				+ "and TYPE = :type");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("utilAttachDocumentId", new LongType());
		query.addScalar("objectId", new LongType());
		query.addScalar("name", new StringType());
		query.addScalar("filePath", new StringType());
		
		query.setParameter("objectId", objectId);
		query.setParameter("type", type);
		
		query.setResultTransformer(Transformers.aliasToBean(UtilAttachDocumentDTO.class));
		return (UtilAttachDocumentDTO) query.uniqueResult();
	}

	public List<SysUserCOMSDTO> getLstUsertoSend() {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder("SELECT SU.SYS_USER_ID sysUserId, SU.PHONE_NUMBER phoneNumber, SU.EMAIL email, dd.DATA_ID sysGroupId " + 
				"FROM CTCT_VPS_OWNER.SYS_USER SU " + 
				"INNER JOIN CTCT_VPS_OWNER.USER_ROLE UR ON SU.SYS_USER_ID = UR.SYS_USER_ID " + 
				"INNER JOIN CTCT_VPS_OWNER.SYS_ROLE SR ON UR.SYS_ROLE_ID = SR.SYS_ROLE_ID " + 
				"INNER JOIN CTCT_VPS_OWNER.ROLE_PERMISSION RP ON SR.SYS_ROLE_ID = RP.SYS_ROLE_ID " + 
				"INNER JOIN CTCT_VPS_OWNER.PERMISSION P ON P.PERMISSION_ID = RP.PERMISSION_ID " + 
				"INNER JOIN CTCT_VPS_OWNER.AD_RESOURCE AR ON AR.AD_RESOURCE_ID = P.AD_RESOURCE_ID " + 
				"INNER JOIN CTCT_VPS_OWNER.OPERATION O ON O.OPERATION_ID = P.OPERATION_ID " + 
				"LEFT join CTCT_VPS_OWNER.USER_ROLE_DATA urd on UR.USER_ROLE_ID = urd.USER_ROLE_ID " + 
				"LEFT join CTCT_VPS_OWNER.DOMAIN_DATA dd on urd.DOMAIN_DATA_ID = dd.DOMAIN_DATA_ID " + 
				"WHERE AR.CODE = 'WOXL' AND O.CODE = 'CREATE' " + 
				"AND dd.DATA_ID IN (SELECT DATA_ID FROM CTCT_VPS_OWNER.DOMAIN_DATA where DATA_CODE = 'TTVHKT' and DOMAIN_TYPE_ID = 321) ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("sysUserId", new LongType());
		query.addScalar("phoneNumber", new StringType());
		query.addScalar("email", new StringType());
		query.addScalar("sysGroupId", new LongType());
		query.setResultTransformer(Transformers.aliasToBean(SysUserCOMSDTO.class));
		return query.list();
	}
    
	
	public void insertSendSMS(String email, String phoneNumber, Long createdUserId, String content) {
		StringBuilder sql = new StringBuilder(" ");
		sql.append(
				"INSERT INTO SEND_SMS_EMAIL(SEND_SMS_EMAIL_ID,SUBJECT,CONTENT,STATUS,RECEIVE_EMAIL,RECEIVE_PHONE_NUMBER,CREATED_DATE,CREATED_USER_ID)  ");
		sql.append(
				" values( SEND_SMS_EMAIL_seq.nextval, :subject, :content, :status, :receiveEmail,:receivePhone, :createdDate, :createdUserId )");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("subject", "Quản lý chứng chỉ");
		query.setParameter("content", content);
		query.setParameter("status", 0);
		query.setParameter("receiveEmail", email);
		query.setParameter("receivePhone", phoneNumber);
		query.setParameter("createdDate", new Date());
		query.setParameter("createdUserId", createdUserId);
		query.executeUpdate();
	}
}
