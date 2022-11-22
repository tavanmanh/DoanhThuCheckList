package com.viettel.coms.dao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.viettel.coms.bo.ContactUnitBO;
import com.viettel.coms.dto.ConstructionTaskDetailDTO;
import com.viettel.coms.dto.ContactUnitDTO;
import com.viettel.coms.dto.ContactUnitDetailDTO;
import com.viettel.coms.dto.ContactUnitDetailDescriptionDTO;
import com.viettel.coms.dto.ContactUnitLibraryDTO;
import com.viettel.coms.dto.UtilAttachDocumentDTO;
import com.viettel.erp.dto.CntContractDTO;
import com.viettel.ktts2.common.BusinessException;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import com.viettel.wms.utils.ValidateUtils;

/**
 * @author HoangNH38
 */
@Repository("recommendContactUnitDAO")
public class RecommendContactUnitDAO extends BaseFWDAOImpl<ContactUnitBO, Long>{


    public RecommendContactUnitDAO() {
        this.model = new ContactUnitBO();
    }

    public RecommendContactUnitDAO(Session session) {
        this.session = session;
    }

    
    //tatph -start - 20112019
    
    
    public List<ContactUnitDTO> doSearch(ContactUnitDTO obj , List<String> groupIdList) {
        StringBuilder sql = new StringBuilder().append(" SELECT " + 
//        				"            T1.CONTACT_UNIT_ID contactUnitId, " + 
//        				"            T1.CREATE_DATE createDate, " + 
//        				"            T1.CREATE_USER_ID createUserId, " + 
//        				"            TO_CHAR(T1.DEADLINE_DATE_COMPLETE, " + 
//        				"            'dd/MM/yyyy') deadlineDateCompleteS, " + 
//        				"            T1.PROVINCE_CODE provinceCode, " + 
//        				"            T1.PROVINCE_NAME provinceName, " + 
//        				"            T1.UNIT_ADDRESS unitAddress, " + 
//        				"            T1.UNIT_BOSS unitBoss, " + 
//        				"            T1.UNIT_FIELD unitField, " + 
//        				"            T1.UNIT_ID unitId, " + 
//        				"            T1.UNIT_NAME unitName, " + 
//        				"            T1.UPDATE_DATE updateDate, " + 
//        				"            T1.UPDATE_USER_ID updateUserId, " + 
//        				"            CASE  " + 
//        				"                WHEN T1.TYPE = 1 THEN 'Trạm + cột BTS'              " + 
//        				"                WHEN T1.TYPE = 2 THEN 'Truyền dẫn'              " + 
//        				"                WHEN T1.TYPE = 3 THEN 'DAS CĐBR'              " + 
//        				"                WHEN T1.TYPE = 4 THEN 'Khác'      " + 
//        				"            end typeS   , " + 
//        				"            T2.CONTACT_UNIT_DETAIL_ID contactUnitDetailId, " + 
//        				"            (select count(CONTACT_UNIT_DETAIL_DESCRIPTION_ID) from CONTACT_UNIT_DETAIL_DESCRIPTION where is_view = 0 and CONTACT_UNIT_DETAIL_ID =T2.CONTACT_UNIT_DETAIL_ID  and CREATE_USER_ID != :userLoginId  ) countMess " + 
//        				"            FROM " + 
//        				"            CONTACT_UNIT T1 LEFT JOIN CONTACT_UNIT_DETAIL T2 ON T1.CONTACT_UNIT_ID = T2.CONTACT_UNIT_ID " + 
//        				"            WHERE " + 
//        				"            1 = 1  ");
						//HienLT56 start 01072020
						"            T1.CONTACT_UNIT_ID contactUnitId, " + 
						"			 T1.CREATE_DATE createDate, " + 
        				"            T1.CREATE_USER_ID createUserId, " + 
						"            T1.unit_code unitCode, " + 
						"            T1.AREA_CODE areaCode, " + 
						"            T1.PROVINCE_CODE provinceCode, " + 
						"            T1.PROVINCE_NAME provinceName, " + 
						"            T1.Unit_Name unitName, " + 
						"			 T1.UNIT_ID unitId, " + 
						"            T1.Unit_Address unitAddress, " +
						"            T1.UPDATE_DATE updateDate, " + 
						"            T1.UPDATE_USER_ID updateUserId, " + 
						"            T1.Unit_Field unitField, " + 
						"            T1.Unit_Boss unitBoss, " + 
//						"            TO_CHAR(T1.Deadline_Date_complete, 'dd/MM/yyyy') deadlineDateCompleteS, " + 
						" 			 T1.Deadline_Date_complete deadlineDateComplete, " + 	
						"            case " + 
						"                when T1.TYPE =1 then 'Trạm + cột BTS' " + 
						"                when T1.TYPE =2 then 'Truyền dẫn' " + 
						"                when T1.TYPE =3 then 'DAS CĐBR ' " + 
						"                when T1.TYPE =4 then ' Khác' " + 
						"           end  typeS , " + 
//						"           T2.CONTACT_DATE contactDateS, " + 
						"           T2.CONTACT_DATE contactDate, " + 
						"           T2.FULL_NAME_CUS fullNameCus, " + 
						"           T2.POSITION_CUS positionCus, " + 
						"           T2.PHONE_NUMBER_CUS phoneNumberCus, " + 
						"           T2.MAIL_CUS mailCus, " + 
						"            case  " + 
						"                when T2.RESULT=1 then ' Có nhu cầu thuê nhà trạm + cột BTS'  " + 
						"                when T2.RESULT=2 then ' Có nhu cầu thuê truyền dẫn' " + 
						"                when T2.RESULT=3 then 'Có nhu cầu triển khai DAS CĐBR'  " + 
						"                when T2.RESULT=4 then 'Có nhu cầu triển khai thuê năng lượng mặt trời'  " + 
						"                when T2.RESULT= 5 then 'Có nhu cầu khác'  " + 
						"                when T2.RESULT= 6  then 'Địa chỉ vị trí của đối tác cần thuê hoặc dự án của đối tác cần hợp tác' " + 
						"                when T2.RESULT= 7 then 'Không có nhu cầu'  " + 
						"                when T2.RESULT= 8  then 'Địa chỉ vị trí của đối tác cần thuê hoặc dự án của đối tác cần hợp tác' " + 
						"                when T2.RESULT= 9  then 'Ký hợp đồng cho thuê hoặc đầu tư cho thuê'  " + 
						"           end  resultS, " + 
						"           T2.SHORT_CONTENT shortContent, " + 
						"           T2.FULL_NAME_EMPLOY fullNameEmploy, " + 
						"           T2.PHONE_NUMBER_EMPLOY phoneNumberEmploy, " + 
						"           T2.MAIL_EMPLOY mailEmploy, " + 
						"           T2.DESCRIPTION description, " + 
						"           (select count(T3.CONTACT_UNIT_DETAIL_DESCRIPTION_ID) from CONTACT_UNIT_DETAIL_DESCRIPTION T3 where T3.is_view = 0 and T3.CONTACT_UNIT_DETAIL_ID =T2.CONTACT_UNIT_DETAIL_ID  and T3.CREATE_USER_ID != :userLoginId  ) countMess "+
						"			FROM CONTACT_UNIT T1 LEFT JOIN CONTACT_UNIT_DETAIL T2 ON T1.CONTACT_UNIT_ID = T2.CONTACT_UNIT_ID WHERE 1=1 ");
        if (StringUtils.isNotEmpty(obj.getProvinceCode())) {
        	sql.append("AND ( (UPPER(T1.PROVINCE_CODE) like UPPER(:provinceCode)  escape '&'))");
//        			+ " OR (UPPER(T1.PROVINCE_NAME) like UPPER(:keySearch)  escape '&')");
//        			+ " OR (UPPER(T1.UNIT_NAME) like UPPER(:keySearch)  escape '&') )");
        }
        if(obj.getStartDate() != null && obj.getEndDate() != null) {
        	sql.append(" AND  trunc(T1.DEADLINE_DATE_COMPLETE) >= trunc(:startDate)  AND  trunc(T1.DEADLINE_DATE_COMPLETE) <= trunc(:endDate) ");
        }
        //HienLT56 start 30/7/2020
        if(obj.getStartDate() != null && obj.getEndDate() == null) {
        	sql.append(" AND (T1.DEADLINE_DATE_COMPLETE) = :startDate ");
        }
        if(obj.getStartDate() == null && obj.getEndDate() != null) {
        	sql.append(" AND  (T1.DEADLINE_DATE_COMPLETE) = :endDate ");
        }
        //HienLT56 end 30/7/2020
        if(obj.getType() != null) {
        	sql.append(" AND  T1.TYPE = :type ");
        }
//        if(obj.getIsMess() != null && obj.getIsMess() != 2) {
//        	if(obj.getIsMess() == 1L) {
//        		sql.append(" AND  (select count(T3.CONTACT_UNIT_DETAIL_DESCRIPTION_ID) from CONTACT_UNIT_DETAIL_DESCRIPTION T3 where T3.is_view = 0 and T3.CONTACT_UNIT_DETAIL_ID =T2.CONTACT_UNIT_DETAIL_ID  and T3.CREATE_USER_ID != :userLoginId  ) > 0 ");
//        	}else {
//        		sql.append(" AND  (select count(T3.CONTACT_UNIT_DETAIL_DESCRIPTION_ID) from CONTACT_UNIT_DETAIL_DESCRIPTION T3 where T3.is_view = 0 and T3.CONTACT_UNIT_DETAIL_ID =T2.CONTACT_UNIT_DETAIL_ID  and T3.CREATE_USER_ID != :userLoginId  ) = 0  ");
//        	}
//        }
        if(obj.getIsMess() != null && obj.getIsMess() == 1) {
        	sql.append(" AND  (select count(T3.CONTACT_UNIT_DETAIL_DESCRIPTION_ID) from CONTACT_UNIT_DETAIL_DESCRIPTION T3 where T3.is_view = 0 and T3.CONTACT_UNIT_DETAIL_ID =T2.CONTACT_UNIT_DETAIL_ID  and T3.CREATE_USER_ID != :userLoginId  ) > 0 ");
        }
        if(obj.getIsMess() != null && obj.getIsMess() == 0) {
        	sql.append(" AND (select count(T3.CONTACT_UNIT_DETAIL_DESCRIPTION_ID) from CONTACT_UNIT_DETAIL_DESCRIPTION T3 where T3.is_view = 0 and T3.CONTACT_UNIT_DETAIL_ID =T2.CONTACT_UNIT_DETAIL_ID  and T3.CREATE_USER_ID != :userLoginId  ) = 0 ");
        }
        if(obj.getIsMess() != null && obj.getIsMess() == 2) {
        	sql.append(" AND ((select count(T3.CONTACT_UNIT_DETAIL_DESCRIPTION_ID) from CONTACT_UNIT_DETAIL_DESCRIPTION T3 where T3.is_view = 0 and T3.CONTACT_UNIT_DETAIL_ID =T2.CONTACT_UNIT_DETAIL_ID  and T3.CREATE_USER_ID != :userLoginId  ) >0 OR "
        			+ "(select count(T3.CONTACT_UNIT_DETAIL_DESCRIPTION_ID) from CONTACT_UNIT_DETAIL_DESCRIPTION T3 where T3.is_view = 0 and T3.CONTACT_UNIT_DETAIL_ID =T2.CONTACT_UNIT_DETAIL_ID  and T3.CREATE_USER_ID != :userLoginId  ) = 0 )");
        }
        
        
        sql.append(" ORDER BY T1.CONTACT_UNIT_ID DESC");
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
//        if(obj.getIsMess() != null) {
        	query.setParameter("userLoginId", obj.getUserLoginId());
            queryCount.setParameter("userLoginId", obj.getUserLoginId());
//        }
        
        if (StringUtils.isNotEmpty(obj.getProvinceCode())) {
            query.setParameter("provinceCode", "%" + obj.getProvinceCode() + "%");
            queryCount.setParameter("provinceCode", "%" + obj.getProvinceCode() + "%");
        }
        
        if (obj.getStartDate() != null && obj.getEndDate() != null) {
            query.setParameter("startDate", obj.getStartDate());
            queryCount.setParameter("startDate", obj.getStartDate());
            query.setParameter("endDate", obj.getEndDate());
            queryCount.setParameter("endDate", obj.getEndDate());
        }
        //HienLT56 start 30072020
        if(obj.getStartDate() != null && obj.getEndDate() == null) {
        	query.setParameter("startDate", obj.getStartDate());
        	queryCount.setParameter("startDate", obj.getStartDate());
        	
        }
        if(obj.getStartDate() == null && obj.getEndDate() != null) {
        	query.setParameter("endDate", obj.getEndDate());
        	queryCount.setParameter("endDate", obj.getEndDate());
        }
      //HienLT56 end 30072020
        if (obj.getType() != null && obj.getType() != null) {
            query.setParameter("type", obj.getType());
            queryCount.setParameter("type", obj.getType());
        }
        
        /*if(obj.getIsMess() != null && obj.getIsMess() != 2) {
        	query.setParameter("countMess", obj.getIsMess());
        	queryCount.setParameter("countMess", obj.getIsMess());
        }*/
        query.addScalar("unitCode", new StringType());
        query.addScalar("areaCode", new StringType());
        query.addScalar("provinceCode", new StringType());
        query.addScalar("unitName", new StringType());
        query.addScalar("unitAddress", new StringType());
        query.addScalar("unitField", new StringType());
        query.addScalar("unitBoss", new StringType());
        query.addScalar("typeS", new StringType());
        query.addScalar("contactDate", new DateType());
        query.addScalar("fullNameCus", new StringType());
        query.addScalar("positionCus", new StringType());
        query.addScalar("phoneNumberCus", new StringType());
        query.addScalar("mailCus", new StringType());
        query.addScalar("resultS", new StringType());
        query.addScalar("shortContent", new StringType());
        query.addScalar("fullNameEmploy", new StringType());
        query.addScalar("phoneNumberEmploy", new StringType());
        query.addScalar("mailEmploy", new StringType());
        query.addScalar("description", new StringType());
        query.addScalar("countMess", new LongType());
        query.addScalar("unitId", new LongType());
        query.addScalar("updateUserId", new LongType());
        query.addScalar("updateDate", new DateType());
        query.addScalar("provinceName", new StringType());
        query.addScalar("contactUnitId", new LongType());
        query.addScalar("createDate", new DateType());
        query.addScalar("createUserId", new LongType());
//        query.addScalar("deadlineDateCompleteS", new StringType());
        query.addScalar("deadlineDateComplete", new DateType());
        
//        query.addScalar("contactDate", new DateType());
//        query.addScalar("contactUnitDetailId", new LongType());
//        query.addScalar("contactUnitId", new LongType());
//        query.addScalar("customerAddress", new StringType());
//        query.addScalar("duringDisscus", new StringType());
//        query.addScalar("needDoDasCdbr", new StringType());
//        query.addScalar("needDoSunEnergy", new StringType());
//        query.addScalar("needHireStationBts", new StringType());
//        query.addScalar("needHireTransmission", new StringType());
//        query.addScalar("needOther", new StringType());
//        query.addScalar("noNeed", new StringType());
//        query.addScalar("signContract", new StringType());
//        query.addScalar("result", new LongType());
//        query.addScalar("type", new LongType());
//        query.addScalar("deadlineDateComplete", new DateType());
//        query.addScalar("deadlineDateCompleteS", new StringType());
//        query.addScalar("provinceCode", new StringType());
//        query.addScalar("provinceName", new StringType());
//        
       
//        query.addScalar("contactUnitDetailId", new LongType());
//        query.addScalar("countMess", new LongType());
//        query.addScalar("contactUnitId", new LongType());
//        query.addScalar("createDate", new DateType());
//        query.addScalar("createUserId", new LongType());
//        query.addScalar("deadlineDateCompleteS", new StringType());
//        query.addScalar("provinceCode", new StringType());
//        query.addScalar("provinceName", new StringType());
//        query.addScalar("unitAddress", new StringType());
//        query.addScalar("unitBoss", new StringType());
//        query.addScalar("unitField", new StringType());
//        query.addScalar("unitId", new LongType());
//        query.addScalar("typeS", new StringType());
//        
//        
        
      //HienLT56 end 01072020
        query.setResultTransformer(Transformers.aliasToBean(ContactUnitDTO.class));
        if(obj.getPage() != null && obj.getPageSize() != null) {
            query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
            query.setMaxResults(obj.getPageSize().intValue());
        }
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

        return query.list();
    }
    
    public List<ContactUnitDTO> countDetail(ContactUnitDTO obj ) {
        StringBuilder sql = new StringBuilder()
        		.append(" SELECT ")
        		.append(" T1.CONTACT_UNIT_DETAIL_ID contactUnitDetailId ")
        		.append(" FROM CONTACT_UNIT_DETAIL T1")
        		.append(" WHERE 1 = 1 ")
        		.append(" AND CONTACT_UNIT_ID  = :contactUnitId ");
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
        query.addScalar("contactUnitDetailId", new LongType());
        query.setParameter("contactUnitId", obj.getContactUnitId());
        queryCount.setParameter("contactUnitId", obj.getContactUnitId());
        query.setResultTransformer(Transformers.aliasToBean(ContactUnitDTO.class));
        if(obj.getPage() != null && obj.getPageSize() != null) {
            query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
            query.setMaxResults(obj.getPageSize().intValue());
        }
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

        return query.list();
    }
    
    public List<ContactUnitDetailDTO> doSearchDetail(ContactUnitDetailDTO obj , List<String> groupIdList) {
        StringBuilder sql = new StringBuilder()
        		.append(" SELECT ")
        		.append(" TO_CHAR(T1.CONTACT_DATE,'dd/MM/yyyy') contactDateS, ")
        		.append(" T1.CONTACT_DATE contactDate, ")
        		.append(" T1.CONTACT_UNIT_DETAIL_ID contactUnitDetailId, ")
        		.append(" T1.CONTACT_UNIT_ID contactUnitId, ")
        		.append(" T1.CUSTOMER_ADDRESS customerAddress, ")
        		.append(" T1.DESCRIPTION description, ")
        		.append(" T1.DURING_DISSCUS duringDisscus, ")
        		.append(" T1.FULL_NAME_CUS fullNameCus, ")
        		.append(" T1.FULL_NAME_EMPLOY fullNameEmploy, ")
        		.append(" T1.MAIL_CUS mailCus, ")
        		.append(" T1.MAIL_EMPLOY mailEmploy, ")
        		.append(" T1.NEED_DO_DAS_CDBR needDoDasCdbr, ")
        		.append(" T1.NEED_DO_SUN_ENERGY needDoSunEnergy, ")
        		.append(" T1.NEED_HIRE_STATION_BTS needHireStationBts, ")
        		.append(" T1.NEED_HIRE_TRANSMISSION needHireTransmission, ")
        		.append(" T1.NEED_OTHER needOther, ")
        		.append(" T1.NO_NEED noNeed, ")
        		.append(" T1.PHONE_NUMBER_CUS phoneNumberCus, ")
        		.append(" T1.PHONE_NUMBER_EMPLOY phoneNumberEmploy, ")
        		.append(" T1.POSITION_CUS positionCus, ")
        		.append(" T1.SHORT_CONTENT shortContent, ")
        		.append(" T1.SIGN_CONTRACT signContract, ")
        		.append("   CASE WHEN T1.TYPE = 1 THEN 'Trạm + cột BTS' " + 
        				"            WHEN T1.TYPE = 2 THEN 'Truyền dẫn' " + 
        				"            WHEN T1.TYPE = 3 THEN 'DAS CĐBR' " + 
        				"            WHEN T1.TYPE = 4 THEN 'Khác' " + 
        				"			 end typeS ,  ")
        		
        		.append("   CASE WHEN T1.RESULT = 1 THEN 'Có nhu cầu thuê nhà trạm + cột BTS' " + 
        				"            WHEN T1.RESULT = 2 THEN 'Có nhu cầu thuê truyền dẫn' " + 
        				"            WHEN T1.RESULT = 3 THEN 'Có nhu cầu triển khai DAS CĐBR' " + 
        				"            WHEN T1.RESULT = 4 THEN 'Có nhu cầu triển khai thuê năng lượng mặt trời' " + 
        				"            WHEN T1.RESULT = 5 THEN 'Có nhu cầu khác ' " + 
        				"            WHEN T1.RESULT = 6 THEN 'Địa chỉ vị trí của đối tác cần thuê hoặc dự án của đối tác cần hợp tác' " + 
        				"            WHEN T1.RESULT = 7 THEN 'Không có nhu cầu' " + 
        				"            WHEN T1.RESULT = 8 THEN 'Đang đàm phán thương thảo cho thuê hoặc đầu tư cho thuê' " + 
        				"            WHEN T1.RESULT = 9 THEN 'Ký hợp đồng cho thuê hoặc đầu tư cho thuê' end resultS  ")
        		
        		.append(" FROM CONTACT_UNIT_DETAIL T1")
        		.append(" WHERE 1 = 1 ");
        if(obj.getContactUnitId() != null) {
        	sql.append(" AND T1.CONTACT_UNIT_ID = :contactUnitId ");
        }
        sql.append(" ORDER BY T1.CONTACT_UNIT_DETAIL_ID ASC");
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
        if (StringUtils.isNotEmpty(obj.getKeySearch())) {
            query.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch()) + "%");
            queryCount.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch()) + "%");
        }
        
        if (obj.getContactUnitId() != null) {
            query.setParameter("contactUnitId", obj.getContactUnitId());
            queryCount.setParameter("contactUnitId",obj.getContactUnitId());
        }
        
        query.addScalar("contactDateS", new StringType());
        query.addScalar("contactDate", new DateType());
        query.addScalar("contactUnitDetailId", new LongType());
        query.addScalar("contactUnitId", new LongType());
        query.addScalar("customerAddress", new StringType());
        query.addScalar("description", new StringType());
        query.addScalar("duringDisscus", new StringType());
        query.addScalar("fullNameCus", new StringType());
        query.addScalar("fullNameEmploy", new StringType());
        query.addScalar("mailCus", new StringType());
        query.addScalar("mailEmploy", new StringType());
        query.addScalar("needDoDasCdbr", new StringType());
        query.addScalar("needDoSunEnergy", new StringType());
        query.addScalar("needHireStationBts", new StringType());
        query.addScalar("needHireTransmission", new StringType());
        query.addScalar("needOther", new StringType());
        query.addScalar("noNeed", new StringType());
        query.addScalar("phoneNumberCus", new StringType());
        query.addScalar("phoneNumberEmploy", new StringType());
        query.addScalar("positionCus", new StringType());
        query.addScalar("shortContent", new StringType());
        query.addScalar("signContract", new StringType());
        query.addScalar("resultS", new StringType());
        query.addScalar("typeS", new StringType());
        
        

        query.setResultTransformer(Transformers.aliasToBean(ContactUnitDetailDTO.class));
        if(obj.getPage() != null && obj.getPageSize() != null) {
            query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
            query.setMaxResults(obj.getPageSize().intValue());
        }
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

        return query.list();
    }
    
    public List<ContactUnitDetailDTO> doSearchDetailById(ContactUnitDetailDTO obj , List<String> groupIdList) {
        StringBuilder sql = new StringBuilder()
        		.append(" with tbl as(SELECT" + 
        				"                 T1.CONTACT_DATE contactDate," + 
        				"                 T1.CONTACT_UNIT_DETAIL_ID contactUnitDetailId," + 
        				"                 T1.CONTACT_UNIT_ID contactUnitId," + 
        				"                 T1.CUSTOMER_ADDRESS customerAddress," + 
        				"                 T1.DESCRIPTION description," + 
        				"                 T1.DURING_DISSCUS duringDisscus," + 
        				"                 T1.FULL_NAME_CUS fullNameCus," + 
        				"                 T1.FULL_NAME_EMPLOY fullNameEmploy," + 
        				"                 T1.MAIL_CUS mailCus," + 
        				"                 T1.MAIL_EMPLOY mailEmploy," + 
        				"                 T1.NEED_DO_DAS_CDBR needDoDasCdbr," + 
        				"                 T1.NEED_DO_SUN_ENERGY needDoSunEnergy," + 
        				"                 T1.NEED_HIRE_STATION_BTS needHireStationBts," + 
        				"                 T1.NEED_HIRE_TRANSMISSION needHireTransmission," + 
        				"                 T1.NEED_OTHER needOther," + 
        				"                 T1.NO_NEED noNeed," + 
        				"                 T1.PHONE_NUMBER_CUS phoneNumberCus," + 
        				"                 T1.PHONE_NUMBER_EMPLOY phoneNumberEmploy," + 
        				"                 T1.POSITION_CUS positionCus," + 
        				"                 T1.SHORT_CONTENT shortContent," + 
        				"                 T1.SIGN_CONTRACT signContract ,     " + 
        				"                 T1.RESULT  result,      " + 
        				"                 T1.TYPE  type      " + 
        				"                 FROM" + 
        				"                 CONTACT_UNIT_DETAIL T1  " + 
        				"                 WHERE" + 
        				"                 1 = 1  " + 
        				"                 AND T1.CONTACT_UNIT_DETAIL_ID = :contactUnitDetailId  " + 
        				"                 AND T1.CONTACT_UNIT_ID = :contactUnitId" + 
        				"                 ORDER BY" + 
        				"                 T1.CONTACT_UNIT_DETAIL_ID ASC)" + 
        				"                 select      		" + 
        				"        		  T1.DEADLINE_DATE_COMPLETE deadlineDateComplete, " + 
        				"        		  T1.PROVINCE_CODE provinceCode, " + 
        				"        		  T1.PROVINCE_NAME provinceName, " + 
        				"        		  T1.UNIT_ADDRESS unitAddress, " + 
        				"        		  T1.UNIT_BOSS unitBoss, " + 
        				"        		  T1.UNIT_FIELD unitField, " + 
        				"        		  T1.UNIT_ID unitId, " + 
        				"        		  T1.UNIT_NAME unitName, " + //HienLT56 add 03072020
        				"        		  T1.UNIT_CODE unitCode, " + //HienLT56 add 03072020
        				"        		  T1.AREA_CODE areaCode, " + 
        				"                 tbl.contactDate contactDate," + 
        				"                 tbl.contactUnitDetailId contactUnitDetailId," + 
        				"                 tbl.contactUnitId contactUnitId," + 
        				"                 tbl.customerAddress customerAddress," + 
        				"                 tbl.description description," + 
        				"                 tbl.duringDisscus duringDisscus," + 
        				"                 tbl.fullNameCus fullNameCus," + 
        				"                 tbl.fullNameEmploy fullNameEmploy," + 
        				"                 tbl.mailCus mailCus," + 
        				"                 tbl.mailEmploy mailEmploy," + 
        				"                 tbl.needDoDasCdbr needDoDasCdbr," + 
        				"                 tbl.needDoSunEnergy needDoSunEnergy," + 
        				"                 tbl.needHireStationBts needHireStationBts," + 
        				"                 tbl.needHireTransmission needHireTransmission," + 
        				"                 tbl.needOther needOther," + 
        				"                 tbl.noNeed noNeed," + 
        				"                 tbl.phoneNumberCus phoneNumberCus," + 
        				"                 tbl.phoneNumberEmploy phoneNumberEmploy," + 
        				"                 tbl.positionCus positionCus," + 
        				"                 tbl.shortContent shortContent, " + 
        				"                 tbl.signContract signContract," + 
        				"                 tbl.result result," + 
        				"                 tbl.type type" + 
        				"        		  FROM CONTACT_UNIT T1" + 
        				"                 JOIN tbl on T1.CONTACT_UNIT_ID = tbl.contactUnitId " + 
        				"                 WHERE T1.CONTACT_UNIT_ID = :contactUnitId ");
        
        
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        
        query.setParameter("contactUnitDetailId", obj.getContactUnitDetailId());
        query.setParameter("contactUnitId", obj.getContactUnitId());
        
        query.addScalar("contactDate", new DateType());
        query.addScalar("contactUnitDetailId", new LongType());
        query.addScalar("contactUnitId", new LongType());
        query.addScalar("customerAddress", new StringType());
        query.addScalar("description", new StringType());
        query.addScalar("duringDisscus", new StringType());
        query.addScalar("fullNameCus", new StringType());
        query.addScalar("fullNameEmploy", new StringType());
        query.addScalar("mailCus", new StringType());
        query.addScalar("mailEmploy", new StringType());
        query.addScalar("needDoDasCdbr", new StringType());
        query.addScalar("needDoSunEnergy", new StringType());
        query.addScalar("needHireStationBts", new StringType());
        query.addScalar("needHireTransmission", new StringType());
        query.addScalar("needOther", new StringType());
        query.addScalar("noNeed", new StringType());
        query.addScalar("phoneNumberCus", new StringType());
        query.addScalar("phoneNumberEmploy", new StringType());
        query.addScalar("positionCus", new StringType());
        query.addScalar("shortContent", new StringType());
        query.addScalar("signContract", new StringType());
        query.addScalar("result", new LongType());
        query.addScalar("type", new LongType());
        
        
        query.addScalar("deadlineDateComplete", new DateType());
        query.addScalar("provinceCode", new StringType());
        query.addScalar("provinceName", new StringType());
        query.addScalar("unitAddress", new StringType());
        query.addScalar("unitBoss", new StringType());
        query.addScalar("unitField", new StringType());
        query.addScalar("unitId", new LongType());
        query.addScalar("unitName", new StringType());
        query.addScalar("unitCode", new StringType());//HienLT56 add 03072020
        query.addScalar("areaCode", new StringType());//HienLT56 add 03072020
        
        query.setResultTransformer(Transformers.aliasToBean(ContactUnitDetailDTO.class));

        return query.list();
    }
    
    public List<ContactUnitLibraryDTO> doSearchContactUnitLibrary(ContactUnitDetailDTO obj , List<String> groupIdList) {
        StringBuilder sql = new StringBuilder()
        		.append(" "+
        				"                 SELECT      		" + 
        				"        		  T1.DEADLINE_DATE_COMPLETE deadlineDateComplete, " + 
        				"        		  to_char(T1.DEADLINE_DATE_COMPLETE,'dd/MM/yyyy') deadlineDateCompleteS, " + 
        				"        		  T1.PROVINCE_CODE provinceCode, " + 
        				"        		  T1.PROVINCE_NAME provinceName, " + 
        				"        		  T1.UNIT_ADDRESS unitAddress, " + 
        				"        		  T1.UNIT_BOSS unitBoss, " + 
        				"        		  T1.UNIT_FIELD unitField, " + 
        				"        		  T1.UNIT_ID unitId, " + 
        				"        		  T1.UNIT_NAME unitName, " + 
        				"                 T1.CONTACT_DATE contactDate," + 
        				"                 TO_CHAR(T1.CONTACT_DATE,'dd/MM/yyyy') contactDateS," + 
        				"                 T1.CONTACT_UNIT_DETAIL_ID contactUnitDetailId," + 
        				"                 T1.CONTACT_UNIT_ID contactUnitId," + 
        				"                 T1.CUSTOMER_ADDRESS customerAddress," + 
        				"                 T1.DESCRIPTION description," + 
        				"                 T1.DURING_DISSCUS duringDisscus," + 
        				"                 T1.FULL_NAME_CUS fullNameCus," + 
        				"                 T1.FULL_NAME_EMPLOY fullNameEmploy," + 
        				"                 T1.MAIL_CUS mailCus," + 
        				"                 T1.MAIL_EMPLOY mailEmploy," + 
        				"                 T1.NEED_DO_DAS_CDBR needDoDasCdbr," + 
        				"                 T1.NEED_DO_SUN_ENERGY needDoSunEnergy," + 
        				"                 T1.NEED_HIRE_STATION_BTS needHireStationBts," + 
        				"                 T1.NEED_HIRE_TRANSMISSION needHireTransmission," + 
        				"                 T1.NEED_OTHER needOther," + 
        				"                 T1.NO_NEED noNeed," + 
        				"                 T1.PHONE_NUMBER_CUS phoneNumberCus," + 
        				"                 T1.PHONE_NUMBER_EMPLOY phoneNumberEmploy," + 
        				"                 T1.POSITION_CUS positionCus," + 
        				"                 T1.SHORT_CONTENT shortContent," + 
        				"                 T1.SIGN_CONTRACT signContract ,     " + 
        				"                 T1.RESULT  result,      " + 
        				"                 T1.TYPE  type   ,   " +
        				"   			  CASE WHEN T1.TYPE = 1 THEN 'Trạm + cột BTS' " + 
                		"            	  WHEN T1.TYPE = 2 THEN 'Truyền dẫn' " + 
                		"            	  WHEN T1.TYPE = 3 THEN 'DAS CĐBR' " + 
                		"            	  WHEN T1.TYPE = 4 THEN 'Khác' " + 
                		"			 	  end typeS ,  " +
                		"   			  CASE WHEN T1.RESULT = 1 THEN 'Có nhu cầu thuê nhà trạm + cột BTS' " + 
                		"            	  WHEN T1.RESULT = 2 THEN 'Có nhu cầu thuê truyền dẫn' " + 
                		"            	  WHEN T1.RESULT = 3 THEN 'Có nhu cầu triển khai DAS CĐBR' " + 
                		"            	  WHEN T1.RESULT = 4 THEN 'Có nhu cầu triển khai thuê năng lượng mặt trời' " + 
                		"            	  WHEN T1.RESULT = 5 THEN 'Có nhu cầu khác ' " + 
                		"            	  WHEN T1.RESULT = 6 THEN 'Địa chỉ vị trí của đối tác cần thuê hoặc dự án của đối tác cần hợp tác' " + 
                		"            	  WHEN T1.RESULT = 7 THEN 'Không có nhu cầu' " + 
                		"           	  WHEN T1.RESULT = 8 THEN 'Đang đàm phán thương thảo cho thuê hoặc đầu tư cho thuê' " + 
                		"            	  WHEN T1.RESULT = 9 THEN 'Ký hợp đồng cho thuê hoặc đầu tư cho thuê' end resultS  "+
        				"        		  FROM CONTACT_UNIT_LIBRARY T1" + 
        				"                 WHERE 1 = 1 ");
        
        if (StringUtils.isNotEmpty(obj.getKeySearch())) {
        	sql.append("AND ( (UPPER(T1.UNIT_NAME) like UPPER(:keySearch)  escape '&') OR (UPPER(T1.UNIT_BOSS) like UPPER(:keySearch)  escape '&') )");
        }
        
        
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
        if (StringUtils.isNotEmpty(obj.getKeySearch())) {
            query.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch()) + "%");
            queryCount.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch()) + "%");
        }
        
        query.addScalar("contactDate", new DateType());
        query.addScalar("contactDateS", new StringType());
        query.addScalar("contactUnitDetailId", new LongType());
        query.addScalar("contactUnitId", new LongType());
        query.addScalar("customerAddress", new StringType());
        query.addScalar("description", new StringType());
        query.addScalar("duringDisscus", new StringType());
        query.addScalar("fullNameCus", new StringType());
        query.addScalar("fullNameEmploy", new StringType());
        query.addScalar("mailCus", new StringType());
        query.addScalar("mailEmploy", new StringType());
        query.addScalar("needDoDasCdbr", new StringType());
        query.addScalar("needDoSunEnergy", new StringType());
        query.addScalar("needHireStationBts", new StringType());
        query.addScalar("needHireTransmission", new StringType());
        query.addScalar("needOther", new StringType());
        query.addScalar("noNeed", new StringType());
        query.addScalar("phoneNumberCus", new StringType());
        query.addScalar("phoneNumberEmploy", new StringType());
        query.addScalar("positionCus", new StringType());
        query.addScalar("shortContent", new StringType());
        query.addScalar("signContract", new StringType());
        query.addScalar("result", new LongType());
        query.addScalar("type", new LongType());
        query.addScalar("resultS", new StringType());
        query.addScalar("typeS", new StringType());
        
        
        query.addScalar("deadlineDateComplete", new DateType());
        query.addScalar("deadlineDateCompleteS", new StringType());
        query.addScalar("provinceCode", new StringType());
        query.addScalar("provinceName", new StringType());
        query.addScalar("unitAddress", new StringType());
        query.addScalar("unitBoss", new StringType());
        query.addScalar("unitField", new StringType());
        query.addScalar("unitId", new LongType());
        query.addScalar("unitName", new StringType());
        
        query.setResultTransformer(Transformers.aliasToBean(ContactUnitLibraryDTO.class));
        if(obj.getPage() != null && obj.getPageSize() != null) {
            query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
            query.setMaxResults(obj.getPageSize().intValue());
        }
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
        return query.list();
    }
    
    public List<ContactUnitDetailDTO> exportListContact(ContactUnitDTO obj ) {
        StringBuilder sql = new StringBuilder()
        		.append("SELECT " + 
        				"    t1.contact_unit_id contactUnitId, " +
        				"    t1.UNIT_CODE unitCode, " + 
        				"    t1.AREA_CODE areaCode, "+
        				"    to_char(t1.deadline_date_complete,'dd/MM/yyyy') deadLineDateCompleteS, " + 
        				"    t1.province_code provinceCode, " + 
        				"    t1.province_name provinceName, " + 
        				"    t1.unit_address unitAddress, " + 
        				"    t1.unit_boss unitBoss, " + 
        				"    t1.unit_field unitField, " + 
        				"    t1.unit_id unitId, " + 
        				"    t1.unit_name unitName, " + 
        				"	 case " + 
					    "            when t1.TYPE =1 then 'Trạm + cột BTS' " + 
					    "            when t1.TYPE =2 then 'Truyền dẫn' " + 
					    "            when t1.TYPE =3 then 'DAS CĐBR ' " + 
					    "            when t1.TYPE =4 then ' Khác' " + 
					    "    end  typeS , " +
						"    to_char(tbl.contact_date,'dd/MM/yyyy') contactDateS, " + 
        				"    tbl.contact_unit_detail_id contactUnitDetailId, " + 
        				"    tbl.customer_address customerAddress, " + 
        				"    tbl.description description, " + 
        				"    tbl.during_disscus duringDisscus, " + 
        				"    tbl.full_name_cus fullNameCus, " + 
        				"    tbl.full_name_employ fullNameEmploy, " + 
        				"    tbl.mail_cus mailCus, " + 
        				"    tbl.mail_employ mailEmploy, " + 
        				"    tbl.need_do_das_cdbr needDoDasCdbr, " + 
        				"    tbl.need_do_sun_energy needDoSunEnergy, " + 
        				"    tbl.need_hire_station_bts needHireStationBts, " + 
        				"    tbl.need_hire_transmission needHireTransmission, " + 
        				"    tbl.need_other needOther, " + 
        				"    tbl.no_need noNeed, " + 
        				"    tbl.phone_number_cus phoneNumberCus, " + 
        				"    tbl.phone_number_employ phoneNumberEmploy, " + 
        				"    tbl.position_cus positionCus, " + 
        				"    tbl.short_content shortContent, " + 
        				"    tbl.sign_contract signContract, " + 
        				"    tbl.result  result " + 
        				
        				"    FROM " + 
        				"    contact_unit t1 " + 
        				"    LEFT JOIN contact_unit_detail tbl ON t1.contact_unit_id = tbl.contact_unit_id " + 
        				"    WHERE " + 
        				"    1 = 1 ");
        if (StringUtils.isNotEmpty(obj.getKeySearch())) {
        	sql.append("AND ( (UPPER(T1.PROVINCE_CODE) like UPPER(:keySearch)  escape '&') OR (UPPER(T1.UNIT_NAME) like UPPER(:keySearch)  escape '&') )");
        }
        if(obj.getStartDate() != null && obj.getEndDate() != null) {
        	sql.append(" AND  trunc(T1.DEADLINE_DATE_COMPLETE) >= trunc(:startDate)  AND  trunc(T1.DEADLINE_DATE_COMPLETE) <= trunc(:endDate) ");
        }
        //HienLT56 start 30/7/2020
        if(obj.getStartDate() != null && obj.getEndDate() == null) {
        	sql.append(" AND (T1.DEADLINE_DATE_COMPLETE) = :startDate ");
        }
        if(obj.getStartDate() == null && obj.getEndDate() != null) {
        	sql.append(" AND  (T1.DEADLINE_DATE_COMPLETE) = :endDate ");
        }
        //HienLT56 end 30/7/2020
        if(obj.getType() != null) {
        	sql.append(" AND  T1.TYPE = :type ");
        }
        
        sql.append(" ORDER BY T1.CONTACT_UNIT_ID DESC ");
        
        
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        if (StringUtils.isNotEmpty(obj.getKeySearch())) {
            query.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch()) + "%");
        }
        
        if (obj.getStartDate() != null && obj.getEndDate() != null) {
            query.setParameter("startDate", obj.getStartDate());
            query.setParameter("endDate", obj.getEndDate());
        }
        //HienLT56 start 30072020
        if(obj.getStartDate() != null && obj.getEndDate() == null) {
        	query.setParameter("startDate", obj.getStartDate());
        	
        }
        if(obj.getStartDate() == null && obj.getEndDate() != null) {
        	query.setParameter("endDate", obj.getEndDate());
        }
      //HienLT56 end 30072020
     
        query.addScalar("unitCode", new StringType());
        query.addScalar("areaCode", new StringType());
        query.addScalar("contactDateS", new StringType());
        query.addScalar("contactUnitDetailId", new LongType());
        query.addScalar("contactUnitId", new LongType());
        query.addScalar("customerAddress", new StringType());
        query.addScalar("description", new StringType());
        query.addScalar("duringDisscus", new StringType());
        query.addScalar("fullNameCus", new StringType());
        query.addScalar("fullNameEmploy", new StringType());
        query.addScalar("mailCus", new StringType());
        query.addScalar("mailEmploy", new StringType());
        query.addScalar("needDoDasCdbr", new StringType());
        query.addScalar("needDoSunEnergy", new StringType());
        query.addScalar("needHireStationBts", new StringType());
        query.addScalar("needHireTransmission", new StringType());
        query.addScalar("needOther", new StringType());
        query.addScalar("noNeed", new StringType());
        query.addScalar("phoneNumberCus", new StringType());
        query.addScalar("phoneNumberEmploy", new StringType());
        query.addScalar("positionCus", new StringType());
        query.addScalar("shortContent", new StringType());
        query.addScalar("signContract", new StringType());
        query.addScalar("result", new LongType());
        query.addScalar("typeS", new StringType());
        
        query.addScalar("deadlineDateCompleteS", new StringType());
        query.addScalar("provinceCode", new StringType());
        query.addScalar("provinceName", new StringType());
        query.addScalar("unitAddress", new StringType());
        query.addScalar("unitBoss", new StringType());
        query.addScalar("unitField", new StringType());
        query.addScalar("unitId", new LongType());
        query.addScalar("unitName", new StringType());
        
        query.setResultTransformer(Transformers.aliasToBean(ContactUnitDetailDTO.class));

        return query.list();
    }
    
    public Long saveContactUnitDetail(ContactUnitDetailDTO contactUnitDetailDTO) {
    	Session session = this.getSession();
    	Long id = (Long) session.save(contactUnitDetailDTO.toModel());
		return id;
    }
    
    public void updateContactUnitDetail(ContactUnitDetailDTO contactUnitDetailDTO) {
    	Session session = this.getSession();
    	session.update(contactUnitDetailDTO.toModel());
    }
    
    public List<UtilAttachDocumentDTO> getListAttachmentByIdAndType(Long idList, String types) {
        StringBuilder sql = new StringBuilder()
                .append("select ")
                .append("UTIL_ATTACH_DOCUMENT_ID utilAttachDocumentId, ")
                .append("OBJECT_ID objectId, ")
                .append("NAME name, ")
                .append("CREATED_DATE createdDate, ")
                .append("CREATED_USER_NAME createdUserName, ")
                .append("TYPE type, ")
                .append("FILE_PATH filePath ")
                .append("FROM ")
                .append("UTIL_ATTACH_DOCUMENT ")
                .append("WHERE OBJECT_ID in (:idList) ")
                .append("AND TYPE in (:types) ");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("idList", idList);
        query.setParameter("types", types);

        query.setResultTransformer(Transformers.aliasToBean(UtilAttachDocumentDTO.class));
        query.addScalar("utilAttachDocumentId", new LongType());
        query.addScalar("objectId", new LongType());
        query.addScalar("name", new StringType());
        query.addScalar("createdDate", new DateType());
        query.addScalar("createdUserName", new StringType());
        query.addScalar("filePath", new StringType());
        query.addScalar("type", new StringType());

        return query.list();
    }
    
    public List<ContactUnitDetailDescriptionDTO> getListDescription(ContactUnitDetailDTO contactUnitDetailDTO) {
        StringBuilder sql = new StringBuilder()
                .append("select ")
                .append("CONTACT_UNIT_DETAIL_DESCRIPTION_ID contactUnitDetailDescriptionId, ")
                .append("CONTACT_UNIT_DETAIL_ID contactUnitDetailId, ")
                .append("to_char(CREATE_DATE,'dd/MM/yyyy HH24:MI:SS') createDateS ,")
                .append("DESCRIPTION description ")
                .append("FROM ")
                .append("CONTACT_UNIT_DETAIL_DESCRIPTION ")
                .append("WHERE CONTACT_UNIT_DETAIL_ID =:contactUnitDetailId ")
                .append("ORDER BY CONTACT_UNIT_DETAIL_DESCRIPTION_ID DESC ");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("contactUnitDetailId", contactUnitDetailDTO.getContactUnitDetailId());

        query.setResultTransformer(Transformers.aliasToBean(ContactUnitDetailDescriptionDTO.class));
        query.addScalar("contactUnitDetailDescriptionId", new LongType());
        query.addScalar("contactUnitDetailId", new LongType());
        query.addScalar("createDateS", new StringType());
        query.addScalar("description", new StringType());

        return query.list();
    }
    
    public void  saveContactUnitDetailDescription(ContactUnitDetailDescriptionDTO contactUnitDetailDescriptionDTO) {
    	Session session = this.getSession();
    	session.save(contactUnitDetailDescriptionDTO.toModel());
    }
    
    
    public void updateContactUnit(Long type , Long id) {
    	 StringBuilder sql = new StringBuilder()
    			 .append(" update contact_unit set type = :type where contact_unit_id = :contactUnitId ");
    	 SQLQuery query = getSession().createSQLQuery(sql.toString());
    	 query.setParameter("type", type);
    	 query.setParameter("contactUnitId", id);
    	 query.executeUpdate();
    }
    
    public void updateConstruction(ConstructionTaskDetailDTO constructionTaskDetailDTO, Long sysUserId) {
    SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
   	 StringBuilder sql = new StringBuilder()
   			 .append(" update construction set "
   			 		+ "APPROVE_REVENUE_STATE = :approveState ,"
   			 		+ "APPROVE_REVENUE_UPDATED_DATE = sysdate, "
   			 		+ "APPROVE_REVENUE_USER_ID = :sysUserId, "
   			 		+" APPROVE_REVENUE_DESCRIPTION = :approveCompleteDescription, "
   			 		+ "DATE_COMPLETE_TC=:dateCompleteTC,APPROVE_REVENUE_VALUE= :approveValuePlan "
   			 		+ "where CONSTRUCTION_ID = :id ");
   	 SQLQuery query = getSession().createSQLQuery(sql.toString());
   	 query.setParameter("approveState", constructionTaskDetailDTO.getAction());
     query.setParameter("dateCompleteTC", sdfDate.format(constructionTaskDetailDTO.getDoneDate()));
     if("2".equals(constructionTaskDetailDTO.getAction())) {
    	 query.setParameter("approveValuePlan", constructionTaskDetailDTO.getConsAppRevenueValue()*1000000);
     }else {
    	 query.setParameter("approveValuePlan", 0);
     }
   	 query.setParameter("sysUserId", sysUserId);
     query.setParameter("approveCompleteDescription", constructionTaskDetailDTO.getApproceCompleteDescription());
   	 query.setParameter("id", constructionTaskDetailDTO.getConstructionId());
   	 query.executeUpdate();
   }
    
    public void updateRpRevenue(ConstructionTaskDetailDTO constructionTaskDetailDTO) {
    	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
      	 StringBuilder sql = new StringBuilder()
      			 .append(" update RP_REVENUE set "
      			 		+ "completeValue = :completeValue ,"
      			 		+ "consAppRevenueValueDB = :consAppRevenueValue ,"
      			 		+ "consAppRevenueState = :consAppRevenueState,APPROVEREVENUEDESCRIPTION=:approveCompleteDescription "
      			 		+ "where CONSTRUCTIONID = :id and PROCESS_DATE=to_char(:dateComplete,'yyyyMM') ");
      	 SQLQuery query = getSession().createSQLQuery(sql.toString());
      	 try {
			query.setParameter("dateComplete", formatter.parse(constructionTaskDetailDTO.getDateComplete()));
		} catch (Exception e) {
			throw new BusinessException("Invalid date");
		}
      	 query.setParameter("id", constructionTaskDetailDTO.getConstructionId());
      	 query.setParameter("completeValue", Double.parseDouble(constructionTaskDetailDTO.getCompleteValue()));
      	 if("2".equals(constructionTaskDetailDTO.getAction())) {
      		 query.setParameter("consAppRevenueValue", constructionTaskDetailDTO.getValueApproveDTOS());
      		 query.setParameter("approveCompleteDescription", null);
      	 }else {
      		query.setParameter("consAppRevenueValue", 0);
      		query.setParameter("approveCompleteDescription", constructionTaskDetailDTO.getApproceCompleteDescription());
      	 }
      	 query.setParameter("consAppRevenueState", constructionTaskDetailDTO.getAction());
      	 query.executeUpdate();
      }
    
    public Integer getMaxId() {
    	StringBuilder sql = new StringBuilder().append("select max(nvl(contact_unit_id,0)) from CONTACT_UNIT_LIBRARY");
    	 SQLQuery query = getSession().createSQLQuery(sql.toString());
    	 Integer id = ((BigDecimal) query.uniqueResult()).intValue();
    	 if(id < 10000000) {
    		 return id + 10000000 + 1;
    	 }else {
    		 return id + 1 ;
    	 }
    	
    }
    
    public void saveContactUnitLibrary(ContactUnitLibraryDTO contactUnitLibraryDTO) {
    	Session session = this.getSession();
    	session.save(contactUnitLibraryDTO.toModel());
    }
    
    public void updateContactUnitLibrary(ContactUnitLibraryDTO contactUnitLibraryDTO) {
    	Session session = this.getSession();
    	session.saveOrUpdate(contactUnitLibraryDTO.toModel());
    }
    
    public void updateDescription(ContactUnitDetailDTO obj) {
   	 StringBuilder sql = new StringBuilder()
   			 .append(" update contact_unit_detail_description set is_view = 1 where contact_unit_detail_id = :contactUnitId ");
   	 SQLQuery query = getSession().createSQLQuery(sql.toString());
   	 query.setParameter("contactUnitId", obj.getContactUnitDetailId());
   	 query.executeUpdate();
   }
    //tatph -end - 20112019

    //HienLT56 start 01072020
	public List<ContactUnitDTO> getForAutoCompleteProvince(ContactUnitDTO obj) {
		StringBuilder stringBuilder =  new StringBuilder("SELECT ");
		stringBuilder.append("T1.NAME name ");
		stringBuilder.append(",T1.CODE code ");
		stringBuilder.append(",T1.CAT_PROVINCE_ID catProvinceId ");
		stringBuilder.append(",T1.AREA_CODE areaCode ");
    	
    	stringBuilder.append("FROM CAT_PROVINCE T1 ");    	
  		stringBuilder.append(" where STATUS != 0");
      	if (StringUtils.isNotEmpty(obj.getKeySearch())) {
  			stringBuilder
  					.append(" AND (UPPER(NAME) like UPPER(:keySearch) OR UPPER(CODE) like UPPER(:keySearch) escape '&')");
  		}
      	StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
      	sqlCount.append(stringBuilder.toString());
		sqlCount.append(")");
		SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
		SQLQuery queryCount=getSession().createSQLQuery(sqlCount.toString());
	
		query.addScalar("name", new StringType());
  		query.addScalar("code", new StringType());
  		query.addScalar("catProvinceId", new LongType());
  		query.addScalar("areaCode", new StringType());
	
		query.setResultTransformer(Transformers.aliasToBean(ContactUnitDTO.class));
		if (StringUtils.isNotEmpty(obj.getKeySearch())) {
			query.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
			queryCount.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
		}
		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1)
					* obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
		query.setMaxResults(20);
		return query.list();
	}
	
	public ContactUnitDTO findByCode(ContactUnitDTO obj) {
		StringBuilder stringBuilder = new StringBuilder("SELECT ");
//		stringBuilder.append("T1.CNT_CONTRACT_ID cntContractId ");
//		stringBuilder.append(",T1.CODE code ");
//		stringBuilder.append(",T1.NAME name ");
//		stringBuilder.append(",T1.CONTRACT_CODE_KTTS contractCodeKtts ");
//		stringBuilder.append(",T1.CONTENT content ");
//		stringBuilder.append(",T1.SIGN_DATE signDate ");
//		stringBuilder.append(",T1.START_TIME startTime ");
//		stringBuilder.append(",T1.END_TIME endTime ");
//		stringBuilder.append(",T1.PRICE price ");
//		stringBuilder.append(",T1.APPENDIX_CONTRACT appendixContract ");
//		stringBuilder.append(",T1.NUM_STATION numStation ");
//		stringBuilder.append(",T1.BIDDING_PACKAGE_ID biddingPackageId ");
//		stringBuilder.append(",T1.CAT_PARTNER_ID catPartnerId ");
//		stringBuilder.append(",T1.SIGNER_PARTNER signerPartner ");
//		stringBuilder.append(",T1.SYS_GROUP_ID sysGroupId ");
//		stringBuilder.append(",T1.SIGNER_GROUP signerGroup ");
//		stringBuilder.append(",T1.SUPERVISOR supervisor ");
//		stringBuilder.append(",T1.STATUS status ");
//		stringBuilder.append(",T1.NUM_DAY numDay ");
//		stringBuilder.append(",T1.FORMAL formal ");
//		stringBuilder.append(",T1.CONTRACT_TYPE contractType ");
//		stringBuilder.append(",T1.CNT_CONTRACT_PARENT_ID cntContractParentId ");
//		stringBuilder.append(",T1.CREATED_DATE createdDate ");
//		stringBuilder.append(",T1.CREATED_USER_ID createdUserId ");
//		stringBuilder.append(",T1.CREATED_GROUP_ID createdGroupId ");
//		stringBuilder.append(",T1.UPDATED_DATE updatedDate ");
//		stringBuilder.append(",T1.UPDATED_USER_ID updatedUserId ");
//		stringBuilder.append(",T1.UPDATED_GROUP_ID updatedGroupId ");
//		stringBuilder.append(",T1.MONEY_TYPE moneyType ");
//    	stringBuilder.append("FROM CNT_CONTRACT T1 ");    	
//    	stringBuilder.append("WHERE  upper(T1.CODE) = upper(:code) AND T1.STATUS != 0");	
//    	stringBuilder.append(" AND T1.CONTRACT_TYPE = :contractType");
    	SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
    	
//		query.addScalar("cntContractId", new LongType());
//		query.addScalar("code", new StringType());
//		query.addScalar("name", new StringType());
//		query.addScalar("contractCodeKtts", new StringType());
//		query.addScalar("content", new StringType());
//		query.addScalar("signDate", new DateType());
//		query.addScalar("startTime", new DateType());
//		query.addScalar("endTime", new DateType());
//		query.addScalar("price", new DoubleType());
//		query.addScalar("appendixContract", new DoubleType());
//		query.addScalar("numStation", new DoubleType());
//		query.addScalar("biddingPackageId", new LongType());
//		query.addScalar("catPartnerId", new LongType());
//		query.addScalar("signerPartner", new StringType());
//		query.addScalar("sysGroupId", new LongType());
//		query.addScalar("signerGroup", new LongType());
//		query.addScalar("supervisor", new StringType());
//		query.addScalar("status", new LongType());
//		query.addScalar("numDay", new DoubleType());
//		query.addScalar("formal", new DoubleType());
//		query.addScalar("contractType", new LongType());
//		query.addScalar("cntContractParentId", new DoubleType());
//		query.addScalar("createdDate", new DateType());
//		query.addScalar("createdUserId", new LongType());
//		query.addScalar("createdGroupId", new LongType());
//		query.addScalar("updatedDate", new DateType());
//		query.addScalar("updatedUserId", new LongType());
//		query.addScalar("updatedGroupId", new LongType());
//		query.addScalar("moneyType", new IntegerType());
//		
//		query.setParameter("code", obj.getCode());  
//		query.setParameter("contractType", obj.getContractType());
		query.setResultTransformer(Transformers.aliasToBean(CntContractDTO.class));    	

		return (ContactUnitDTO) query.uniqueResult();
	}
	

	public List<String> getProvinceLst() {
		StringBuilder sql = new StringBuilder("SELECT DISTINCT CODE code "
				+ "FROM CAT_PROVINCE");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("code", new StringType());
		return query.list();
	}

	public String getProvinceNameForImport(String provinceCode) {
		StringBuilder sql = new StringBuilder("SELECT DISTINCT NAME name FROM CAT_PROVINCE WHERE CODE = :provinceCode ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("name", new StringType());
		query.setParameter("provinceCode", provinceCode);
		return (String) query.uniqueResult();
	}

	public List<String> getProvinceKV1Lst() {
		StringBuilder sql = new StringBuilder("SELECT DISTINCT CODE code FROM CAT_PROVINCE WHERE AREA_CODE = 'KV1' ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("code", new StringType());
		return query.list();
	}
	public List<String> getProvinceKV2Lst() {
		StringBuilder sql = new StringBuilder("SELECT DISTINCT CODE code FROM CAT_PROVINCE WHERE AREA_CODE = 'KV2' ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("code", new StringType());
		return query.list();
	}
	public List<String> getProvinceKV3Lst() {
		StringBuilder sql = new StringBuilder("SELECT DISTINCT CODE code FROM CAT_PROVINCE WHERE AREA_CODE = 'KV3' ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("code", new StringType());
		return query.list();
	}


	//HienLT56 end 01072020
    
}
