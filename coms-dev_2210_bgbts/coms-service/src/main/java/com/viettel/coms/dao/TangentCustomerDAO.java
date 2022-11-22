package com.viettel.coms.dao;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Objects;
import com.viettel.cat.bo.CatProvinceBO;
import com.viettel.cat.dto.CatPartnerDTO;
import com.viettel.cat.dto.CatProvinceDTO;
import com.viettel.cat.dto.ConstructionImageInfo;
import com.viettel.coms.bo.TangentCustomerBO;
import com.viettel.coms.bo.UtilAttachDocumentBO;
import com.viettel.coms.dto.AppParamDTO;
import com.viettel.coms.dto.ComplainOrderRequestDTO;
import com.viettel.coms.dto.ConfigStaffTangentDTO;
import com.viettel.coms.dto.ConstructionTaskDTO;
import com.viettel.coms.dto.ResultSolutionDTO;
import com.viettel.coms.dto.ResultTangentDTO;
import com.viettel.coms.dto.SysGroupDTO;
import com.viettel.coms.dto.SysUserDetailCOMSDTO;
import com.viettel.coms.dto.SysUserRequest;
import com.viettel.coms.dto.TangentCustomerDTO;
import com.viettel.coms.dto.TangentCustomerRequest;
import com.viettel.erp.dto.SysUserDTO;
import com.viettel.ktts2.common.UEncrypt;
import com.viettel.ktts2.common.UFile;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import com.viettel.service.base.utils.DataUtil;
import com.viettel.utils.ImageUtil;
import com.viettel.wms.utils.ValidateUtils;
import javax.servlet.http.HttpServletRequest;

@EnableTransactionManagement
@Transactional
@Repository("tangentCustomerDAO")
public class TangentCustomerDAO extends BaseFWDAOImpl<TangentCustomerBO, Long> {

	public TangentCustomerDAO() {
		this.model = new TangentCustomerBO();
	}

	public TangentCustomerDAO(Session session) {
		this.session = session;
	}

	@Autowired
	private ResultTangentDAO resultTangentDAO;

	@Autowired
	private ResultSolutionDAO resultSolutionDAO;

	@Autowired
	private UtilAttachDocumentDAO utilAttachDocumentDAO;

	@Value("${folder_upload2}")
	private String folder2Upload;
	// hoanm1_20200718_start
	// @Value("${input_image_sub_folder_upload}")
	// hoanm1_20200718_end
	@Value("${input_sub_folder_upload}")
	private String input_image_sub_folder_upload;

	public List<TangentCustomerDTO> doSearch(TangentCustomerDTO obj, List<String> provinceIdLst) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    	Calendar cal = Calendar.getInstance();
		StringBuilder sql = new StringBuilder(" with tbl as (select T1.TANGENT_CUSTOMER_ID tangentCustomerId,"
				+ " T1.CUSTOMER_NAME customerName, T1.BIRTH_YEAR birthYear, T1.ADDRESS address, T1.AREA_ID areaId, "
				+ " T1.PROVINCE_ID provinceId, T1.PROVINCE_CODE provinceCode, T1.CUSTOMER_PHONE customerPhone, "
				+ " T1.CUSTOMER_EMAIL customerEmail, T1.SUGGEST_TIME suggestTime, T1.PERFORMER_ID performerId, "
				+ " T1.PERFORMER_SOLUTION_ID performerSolutionId, T1.CONTENT_CUSTOMER contentCustomer, T1.STATUS status, "
				+ " T1.CREATED_USER createdUser, T1.CREATED_DATE createdDate, to_char(T1.CREATED_DATE,'dd/MM/yyyy HH24:Mi:SS') createdDateDb, "
				+ " T1.UPDATED_USER updatedUser, T1.UPDATED_DATE updatedDate, to_char(T1.UPDATED_DATE,'dd/MM/yyyy HH24:Mi:SS') updatedDateDb ,T1.DISTRICT_NAME districtName, "
				+ " T1.COMMUNE_NAME communeName,  T1.apartment_number apartmentNumber, T1.CRM_ID crmId, T1.APPOVED_DATE approvedDate, "
                + " T1.SOURCE source,re_ta.APPOINTMENT_DATE appointmentDate,re_ta.REALITY_TANGENT_DATE realityTangentDate, "
				+ " T1.RECEPTION_CHANNEL receptionChannel,T1.CUSTOMER_RESOURCES customerResources,T1.PARTNER_TYPE partnerType,T1.PARTNER_CODE partnerCode,T1.GROUP_ORDER groupOrder, "
                + " T1.ESTIMATED_CONSTRUCTION_TIME estimatedConstructionTime, T1.ESTIMATED_BUDGET estimatedBudget, T1.REGISTERED_CUSTOMER_SERVICE registeredCustomerService, T1.MODEL_OF_THE_BUILDER modelOfTheBuilder, "
                + " (CASE "
                + "	WHEN T1.STATUS=1 THEN ( "
				+ "	          CASE "
				+ "	          WHEN SYSDATE BETWEEN to_date(to_char(T1.CREATED_DATE,'dd/MM/yyyy HH24:Mi:SS'), 'dd/mm/yyyy hh24:mi:ss')  and  "          
				+ "	                   to_date(to_char(T1.CREATED_DATE,'dd/MM/yyyy HH24:Mi:SS'), 'dd/mm/yyyy hh24:mi:ss') + (2 / 24 ) "
				+ "	          THEN 1 " + "	          WHEN  "
				+ "	          SYSDATE BETWEEN to_date(to_char(T1.CREATED_DATE,'dd/MM/yyyy HH24:Mi:SS'), 'dd/mm/yyyy hh24:mi:ss') + (2 / 24 )  and   "
				+ "	                   to_date(to_char(T1.CREATED_DATE,'dd/MM/yyyy HH24:Mi:SS'), 'dd/mm/yyyy hh24:mi:ss') + (3 / 24 ) "
				+ "	          THEN 2 " + "	          WHEN  "
				+ "	          SYSDATE  > to_date(to_char(T1.CREATED_DATE,'dd/MM/yyyy HH24:Mi:SS'), 'dd/mm/yyyy hh24:mi:ss') + (3 / 24 ) "

				+ "	          THEN 3 " + "	           ELSE null " + "	           END  "

				+ "	   ) " + "	  WHEN T1.STATUS= 2 THEN (  "

				+ "	          CASE "
				+ "	            WHEN SYSDATE <  to_date(to_char(re_ta.APPOINTMENT_DATE,'dd/MM/yyyy HH24:Mi:SS'), 'dd/mm/yyyy hh24:mi:ss') - (24 / 24 ) THEN 1 "
				+ "	            WHEN SYSDATE BETWEEN to_date(to_char(re_ta.APPOINTMENT_DATE,'dd/MM/yyyy HH24:Mi:SS'), 'dd/mm/yyyy hh24:mi:ss') - (12 / 24 )  and  "          
				+ "	                   to_date(to_char(re_ta.APPOINTMENT_DATE,'dd/MM/yyyy HH24:Mi:SS'), 'dd/mm/yyyy hh24:mi:ss') - (10 / 24 ) THEN 2 "
				+ "	            WHEN SYSDATE  > to_date(to_char(re_ta.APPOINTMENT_DATE,'dd/MM/yyyy HH24:Mi:SS'), 'dd/mm/yyyy hh24:mi:ss') THEN 3 "
				+ "	          else null "
				+ "	           END  "
				+ "	    )  "
				+ "	WHEN T1.STATUS= 3 THEN (  "
				+ "	 CASE "
				+ "	    WHEN   "
				+ "	        SYSDATE BETWEEN to_date(to_char(re_ta.REALITY_TANGENT_DATE,'dd/MM/yyyy HH24:Mi:SS'), 'dd/mm/yyyy hh24:mi:ss')  and   "         
				+ "	           to_date(to_char(re_ta.REALITY_TANGENT_DATE,'dd/MM/yyyy HH24:Mi:SS'), 'dd/mm/yyyy hh24:mi:ss') + (96 / 24 ) THEN 1 "
				+ "	    WHEN "
				+ "	        SYSDATE BETWEEN to_date(to_char(re_ta.REALITY_TANGENT_DATE,'dd/MM/yyyy HH24:Mi:SS'), 'dd/mm/yyyy hh24:mi:ss') + (96 / 24 ) and  "          
				+ "	           to_date(to_char(re_ta.REALITY_TANGENT_DATE,'dd/MM/yyyy HH24:Mi:SS'), 'dd/mm/yyyy hh24:mi:ss') + (120 / 24 ) THEN 2 "
				+ "	    WHEN "
				+ "	        SYSDATE  > to_date(to_char(re_ta.REALITY_TANGENT_DATE,'dd/MM/yyyy HH24:Mi:SS'), 'dd/mm/yyyy hh24:mi:ss') + (120 / 24 ) THEN 3 "
				+ "	  ELSE null "
				+ "	   END "
				+ "	 ) "
				+ "	WHEN T1.STATUS= 6 THEN ( "
				+ "	    CASE "
				+ "	      WHEN "       
				+ "	        SYSDATE BETWEEN to_date(to_char(re_so.UPDATED_DATE,'dd/MM/yyyy HH24:Mi:SS'), 'dd/mm/yyyy hh24:mi:ss')  and "           
				+ "	        to_date(to_char(re_so.UPDATED_DATE,'dd/MM/yyyy HH24:Mi:SS'), 'dd/mm/yyyy hh24:mi:ss') + (24 / 24 ) THEN 1 "
				+ "		      WHEN " 
				+ "		        SYSDATE BETWEEN to_date(to_char(re_so.UPDATED_DATE,'dd/MM/yyyy HH24:Mi:SS'), 'dd/mm/yyyy hh24:mi:ss') + (24 / 24 )  and     "       
				+ "		        to_date(to_char(re_so.UPDATED_DATE,'dd/MM/yyyy HH24:Mi:SS'), 'dd/mm/yyyy hh24:mi:ss') + (48 / 24 ) THEN 2 "
				+ "		      WHEN "
				+ "		        SYSDATE  > to_date(to_char(re_so.CREATED_DATE,'dd/MM/yyyy HH24:Mi:SS'), 'dd/mm/yyyy hh24:mi:ss') + (48 / 24 ) THEN 3 "
				+ "		    ELSE null "
				+ "		     END "
				+ "		     ) "
				+ "		ELSE null "
				+ "		END)  overdueStatus from TANGENT_CUSTOMER T1  "
				+ "		                left join RESULT_TANGENT re_ta on re_ta.RESULT_TANGENT_ID =  "
				+ "		                (  SELECT "
				+ "		                    T2.RESULT_TANGENT_ID "
				+ "		                    FROM RESULT_TANGENT T2  where T2.TANGENT_CUSTOMER_ID = T1.TANGENT_CUSTOMER_ID "
				+ "		                          order by T2.CREATED_DATE desc "
				+ "		                          fetch first 1 row only ) "
				+ "						left join RESULT_SOLUTION re_so on re_so.RESULT_SOLUTION_ID =  "
				+ "		                (  SELECT "
				+ "		                    T3.RESULT_SOLUTION_ID "
				+ "		                    FROM RESULT_SOLUTION T3  where T3.TANGENT_CUSTOMER_ID = T1.TANGENT_CUSTOMER_ID "
				+ "		                          order by T3.CREATED_DATE desc "
				+ "		                          fetch first 1 row only ) ) ");

		sql.append("select ").append("tbl.tangentCustomerId tangentCustomerId, ")
				.append("tbl.customerName, ").append(" tbl.birthYear, ").append(" tbl.address, ")
				.append(" tbl.areaId, ").append(" tbl.provinceId, ").append(" tbl.provinceCode, ")
				.append(" tbl.customerPhone, ").append(" tbl.customerEmail, ").append(" tbl.suggestTime, ")
				.append(" tbl.performerId, ").append(" tbl.performerSolutionId, ").append(" tbl.contentCustomer, ")
				.append(" tbl.status, ").append(" tbl.createdUser, ").append(" tbl.createdDate, ")
				.append(" tbl.createdDateDb,").append(" tbl.updatedUser, ").append(" tbl.updatedDate, ").append(" tbl.updatedDateDb, ") 
				.append(" tbl.districtName, ").append(" tbl.communeName, ").append(" tbl.apartmentNumber, ")
				.append(" tbl.crmId, ").append(" tbl.source source, ")

				.append(" tbl.receptionChannel, ").append(" tbl.customerResources, ").append(" tbl.partnerType, ").append(" tbl.partnerCode, ").append(" tbl.groupOrder, ") 

				.append(" tbl.overdueStatus, ").append(" tbl.approvedDate, ")
				.append(" tbl.realityTangentDate, ")
				.append(" tbl.appointmentDate appointmentDate, ").append(" su.FULL_NAME performerName,  ")
				.append(" su1.FULL_NAME performerSolutionName, ").append(" su.EMPLOYEE_CODE performerCode, ")
				.append(" su1.EMPLOYEE_CODE performerSolutionCode, ")
				.append(" su2.EMAIL createdEmail,su2.PHONE_NUMBER createdPhoneNumber, ")
				.append(" su1.EMAIL performerSolutionEmail,su1.PHONE_NUMBER performerSolutionNumber, ")
				.append(" (su2.FULL_NAME ||'-' || su2.LOGIN_NAME) createdUserName, ")
				.append(" CP.CODE provinceKeyCode ")
				.append(" ,estimatedConstructionTime, estimatedBudget, registeredCustomerService, modelOfTheBuilder ")
				.append("  FROM tbl ")
				.append(" left join CAT_PROVINCE CP on tbl.provinceId = CP.CAT_PROVINCE_ID ")
				.append(" left join SYS_USER su on tbl.performerId = su.SYS_USER_ID ")
				.append(" left join SYS_USER su1 on tbl.performerSolutionId = su1.SYS_USER_ID ")
				.append(" left join SYS_USER su2 on tbl.createdUser = su2.SYS_USER_ID ").append(" WHERE 1=1 AND tbl.provinceId in (:provinceIdLst) ");
		if (StringUtils.isNotBlank(obj.getKeySearch())) {
			sql.append(" AND (upper(tbl.customerName) like upper(:keySearch) escape '&' "
					+ "OR upper(tbl.address) like upper(:keySearch) escape '&' "
					+ "OR upper(tbl.customerPhone) like upper(:keySearch) escape '&' " + ") ");
		}

		if (obj.getCreatedDateFrom() != null) {
			sql.append(" AND tbl.createdDate >= :createdDateFrom ");
		}

		if (obj.getCreatedDateTo() != null) {
			cal.setTime(obj.getCreatedDateTo());
			cal.add(Calendar.DATE, 1);
			sql.append(" AND tbl.createdDate <= :createdDateTo ");
		}

		if (obj.getSuggestTimeFrom() != null) {
			sql.append(" AND tbl.suggestTime >= :suggestTimeFrom ");
		}

		if (obj.getSuggestTimeTo() != null) {
			sql.append(" AND tbl.suggestTime <= :suggestTimeTo ");
		}

		if (obj.getLstStatus() != null && obj.getLstStatus().size() > 0) {
			Set<String> setLstStatus = new HashSet<String>();
			setLstStatus.addAll(obj.getLstStatus());
			if(setLstStatus.contains("11")) {
				setLstStatus.add("4");setLstStatus.add("7");setLstStatus.add("9");
				List<String> newListStatus = new ArrayList<String>();newListStatus.addAll(setLstStatus);
				obj.setLstStatus(newListStatus);
			}
			sql.append(" AND tbl.status in (:lstStatus) ");
		}

		if (obj.getPerformerId() != null) {
			sql.append(" AND tbl.performerId = :performerId ");
		}
		if (obj.getProvinceId()!=null) {
			sql.append(" AND tbl.provinceId = :provinceId ");
		}
		if (StringUtils.isNotBlank(obj.getDistrictName())) {
			sql.append(" AND upper(tbl.districtName) LIKE upper(:districtName) escape '&' ");
		}

		if (StringUtils.isNotBlank(obj.getCommuneName())) {
			sql.append(" AND upper(tbl.communeName) LIKE upper(:communeName) escape '&' ");
		}
		if (obj.getLstOverdueStatus() != null && obj.getLstOverdueStatus().size() > 0) {
			sql.append(" AND tbl.overdueStatus in :lstOverdueStatus ");
		}

		sql.append(" ORDER BY tbl.tangentCustomerId DESC ");

		StringBuilder sqlCount = new StringBuilder("select count(*) from (" + sql.toString() + ")");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		query.addScalar("tangentCustomerId", new LongType());
		query.addScalar("customerName", new StringType());
		query.addScalar("birthYear", new StringType());
		query.addScalar("address", new StringType());
		query.addScalar("areaId", new LongType());
		query.addScalar("provinceId", new LongType());
		query.addScalar("provinceCode", new StringType());
		query.addScalar("customerPhone", new StringType());
		query.addScalar("customerEmail", new StringType());
		query.addScalar("suggestTime", new DateType());
		query.addScalar("performerId", new LongType());
		query.addScalar("performerSolutionId", new LongType());
		query.addScalar("contentCustomer", new StringType());
		query.addScalar("status", new StringType());
		query.addScalar("createdUser", new LongType());
		query.addScalar("createdDate", new DateType());
		query.addScalar("updatedUser", new LongType());
		query.addScalar("updatedDate", new DateType());
		query.addScalar("updatedDateDb", new StringType()); 
		query.addScalar("performerName", new StringType());
		query.addScalar("districtName", new StringType());
		query.addScalar("communeName", new StringType());
		query.addScalar("apartmentNumber", new StringType());
		query.addScalar("performerSolutionName", new StringType());
		query.addScalar("performerCode", new StringType());
		query.addScalar("performerSolutionCode", new StringType());
		query.addScalar("createdEmail", new StringType());
		query.addScalar("createdPhoneNumber", new StringType());
		query.addScalar("performerSolutionEmail", new StringType());
		query.addScalar("performerSolutionNumber", new StringType());
		query.addScalar("createdDateDb", new StringType());
		query.addScalar("createdUserName", new StringType());
		query.addScalar("crmId", new LongType());
		// duonghv13 add 25122021
		query.addScalar("source", new StringType());
		// duonghv13 add 11022022
		query.addScalar("receptionChannel", new StringType());
		query.addScalar("customerResources", new StringType());
		query.addScalar("partnerType", new LongType());
		query.addScalar("partnerCode", new StringType());
		query.addScalar("groupOrder", new LongType());
		// duonghv13 end 11022022
		query.addScalar("overdueStatus", new LongType());
		query.addScalar("approvedDate", new DateType());
		query.addScalar("appointmentDate", new DateType());
		query.addScalar("realityTangentDate", new DateType());
		query.addScalar("provinceKeyCode", new StringType());
		//Huypq-05042022-start
		query.addScalar("estimatedConstructionTime", new DateType());
		query.addScalar("estimatedBudget", new StringType());
		query.addScalar("registeredCustomerService", new StringType());
		query.addScalar("modelOfTheBuilder", new StringType());
		//Huy-end
		query.addScalar("source", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(TangentCustomerDTO.class));

		if (StringUtils.isNotBlank(obj.getKeySearch())) {
			query.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
			queryCount.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
		}

		if (obj.getCreatedDateFrom() != null) {
			query.setParameter("createdDateFrom", obj.getCreatedDateFrom());
			queryCount.setParameter("createdDateFrom", obj.getCreatedDateFrom());
		}

		if (obj.getCreatedDateTo() != null) {
			query.setParameter("createdDateTo", cal.getTime());
			queryCount.setParameter("createdDateTo", cal.getTime());
		}

		if (obj.getSuggestTimeFrom() != null) {
			query.setParameter("suggestTimeFrom", obj.getSuggestTimeFrom());
			queryCount.setParameter("suggestTimeFrom", obj.getSuggestTimeFrom());
		}

		if (obj.getSuggestTimeTo() != null) {
			query.setParameter("suggestTimeTo", obj.getSuggestTimeTo());
			queryCount.setParameter("suggestTimeTo", obj.getSuggestTimeTo());
		}

		if (obj.getLstStatus() != null && obj.getLstStatus().size() > 0) {
			query.setParameterList("lstStatus", obj.getLstStatus());
			queryCount.setParameterList("lstStatus", obj.getLstStatus());
		}

		if (obj.getPerformerId() != null) {
			query.setParameter("performerId", obj.getPerformerId());
			queryCount.setParameter("performerId", obj.getPerformerId());
		}

//		if (StringUtils.isNotBlank(obj.getProvinceCode())) {
//			query.setParameter("provinceCode", obj.getProvinceCode());
//			queryCount.setParameter("provinceCode", obj.getProvinceCode());
//		}
		if (obj.getProvinceId()!=null) {
			query.setParameter("provinceId", obj.getProvinceId());
			queryCount.setParameter("provinceId", obj.getProvinceId());
		}
		if (StringUtils.isNotBlank(obj.getDistrictName())) {
			query.setParameter("districtName", "%" + obj.getDistrictName() + "%");
			queryCount.setParameter("districtName", "%" + obj.getDistrictName() + "%");
		}

		if (StringUtils.isNotBlank(obj.getCommuneName())) {
			query.setParameter("communeName", "%" + obj.getCommuneName() + "%");
			queryCount.setParameter("communeName", "%" + obj.getCommuneName() + "%");
		}
		if (obj.getOverdueStatus() != null) {
			query.setParameter("overdueStatus", obj.getOverdueStatus());
			queryCount.setParameter("overdueStatus", obj.getOverdueStatus());
		}
		
		if (obj.getLstOverdueStatus() != null && obj.getLstOverdueStatus().size() > 0) {
			query.setParameterList("lstOverdueStatus", obj.getLstOverdueStatus());
			queryCount.setParameterList("lstOverdueStatus", obj.getLstOverdueStatus());
		}

		query.setParameterList("provinceIdLst", provinceIdLst);
		queryCount.setParameterList("provinceIdLst", provinceIdLst);
		
		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}

		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		return query.list();
	}

	// Lấy danh sách tỉnh
	@SuppressWarnings("unchecked")
	public List<CatProvinceDTO> doSearchProvince(CatProvinceDTO obj) {
		StringBuilder sql = new StringBuilder("SELECT AREA_ID areaId, " + "PROVINCE_ID catProvinceId, "
				+ "  code code, " + "  name name " + "FROM AIO_AREA " + "WHERE AREA_LEVEL=2 ");

		if (StringUtils.isNotEmpty(obj.getKeySearch())) {
			sql.append(" AND (upper(code) like upper(:keySearch) OR upper(name) like upper(:keySearch) escape '&') ");
		}

		sql.append(" ORDER BY name ASC ");

		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		query.addScalar("areaId", new LongType());
		query.addScalar("catProvinceId", new LongType());
		query.addScalar("code", new StringType());
		query.addScalar("name", new StringType());

		query.setResultTransformer(Transformers.aliasToBean(CatProvinceDTO.class));

		if (StringUtils.isNotEmpty(obj.getKeySearch())) {
			query.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch()) + "%");
			queryCount.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch()) + "%");
		}

		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
		return query.list();
	}

	// Lấy danh sách quận/huyện theo tỉnh
	@SuppressWarnings("unchecked")
	public List<TangentCustomerDTO> doSearchDistrictByProvinceCode(TangentCustomerDTO obj) {
		StringBuilder sql = new StringBuilder(
//				"select AREA_ID districtId, name districtName from AIO_AREA where AREA_LEVEL=3 and PARENT_ID = :provinceId ");
				"select AREA_ID districtId, name districtName from CTCT_IMS_OWNER.AREA where AREA_LEVEL=3 and PARENT_ID = :provinceId ");

		if (StringUtils.isNotEmpty(obj.getDistrictName())) {
			sql.append(" AND upper(name) like upper(:districtName) escape '&' ");
		}

		sql.append(" ORDER BY name ASC ");

		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		query.addScalar("districtId", new LongType());
		query.addScalar("districtName", new StringType());

		query.setResultTransformer(Transformers.aliasToBean(TangentCustomerDTO.class));

		query.setParameter("provinceId", obj.getProvinceId());
		queryCount.setParameter("provinceId", obj.getProvinceId());

		if (StringUtils.isNotEmpty(obj.getDistrictName())) {
			query.setParameter("districtName", "%" + ValidateUtils.validateKeySearch(obj.getDistrictName()) + "%");
			queryCount.setParameter("districtName", "%" + ValidateUtils.validateKeySearch(obj.getDistrictName()) + "%");
		}

		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
		return query.list();
	}

	// Lấy danh sách xã/phường theo quận/huyện
	@SuppressWarnings("unchecked")
	public List<TangentCustomerDTO> doSearchCommunneByDistrict(TangentCustomerDTO obj) {
		StringBuilder sql = new StringBuilder(
//				"select area_id communeId, name communeName from AIO_AREA where AREA_LEVEL=4 and PARENT_ID = :districtId ");
				"select area_id communeId, name communeName from CTCT_IMS_OWNER.AREA where AREA_LEVEL=4 and PARENT_ID = :districtId ");

		if (StringUtils.isNotEmpty(obj.getCommuneName())) {
			sql.append(" AND upper(name) like upper(:communeName) escape '&' ");
		}

		sql.append(" ORDER BY name ASC ");

		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		query.addScalar("communeId", new LongType());
		query.addScalar("communeName", new StringType());

		query.setResultTransformer(Transformers.aliasToBean(TangentCustomerDTO.class));

		query.setParameter("districtId", obj.getDistrictId());
		queryCount.setParameter("districtId", obj.getDistrictId());

		if (StringUtils.isNotEmpty(obj.getCommuneName())) {
			query.setParameter("communeName", "%" + ValidateUtils.validateKeySearch(obj.getCommuneName()) + "%");
			queryCount.setParameter("communeName", "%" + ValidateUtils.validateKeySearch(obj.getCommuneName()) + "%");
		}

		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
		return query.list();
	}

	public ConfigStaffTangentDTO getUserConfigTagent(Long catProvinceId, String type) {
		StringBuilder sql = new StringBuilder(
				"select STAFF_ID staffId, STAFF_NAME staffName,STAFF_PHONE staffPhone, EMAIL email  from CONFIG_STAFF_TANGENT where STATUS!=0 AND CAT_PROVINCE_ID=:catProvinceId and type=:type ");

		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("staffId", new LongType());
		query.addScalar("staffName", new StringType());
		query.addScalar("staffPhone", new StringType());
		query.addScalar("email", new StringType());

		query.setParameter("catProvinceId", catProvinceId);
		query.setParameter("type", type);

		query.setResultTransformer(Transformers.aliasToBean(ConfigStaffTangentDTO.class));

		List<ConfigStaffTangentDTO> ls = query.list();
		if (ls.size() > 0) {
			return ls.get(0);
		}
		return null;
	}

	public Long deleteRecord(Long id, Long userId) {
		StringBuilder sql = new StringBuilder(
				"UPDATE TANGENT_CUSTOMER SET UPDATED_DATE=SYSDATE, STATUS=0, UPDATED_USER=:userId WHERE TANGENT_CUSTOMER_ID=:id ");

		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.setParameter("userId", userId);
		query.setParameter("id", id);

		return (long) query.executeUpdate();
	}

	public Long updateStatusTangent(Long id, String status, Long performerId, Long userId) {
		StringBuilder sql = new StringBuilder(
				"UPDATE TANGENT_CUSTOMER SET STATUS=:status, UPDATED_DATE=sysdate, UPDATED_USER=:userId  ");
		if (performerId != null) {
			sql.append(" ,PERFORMER_ID= :performerId");
		}

		sql.append(" WHERE TANGENT_CUSTOMER_ID=:id ");

		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.setParameter("status", status);
		query.setParameter("id", id);
		query.setParameter("userId", userId);

		if (performerId != null) {
			query.setParameter("performerId", performerId);
		}

		return (long) query.executeUpdate();
	}

	// hoanm1_20200612_start
	public TangentCustomerDTO getCountStateTangentCustomer(SysUserRequest request) {
		StringBuilder sql = new StringBuilder("");
		sql.append(
				" with tbl as (select a.status,(select APPOINTMENT_DATE from (select APPOINTMENT_DATE from RESULT_TANGENT resultT where  ");
		sql.append(
				" a.TANGENT_CUSTOMER_ID=resultT.TANGENT_CUSTOMER_ID order by ORDER_RESULT_TANGENT desc) where rownum =1) APPOINTMENT_DATE,");
		sql.append(
				" (select PRESENT_SOLUTION_DATE from (select PRESENT_SOLUTION_DATE from RESULT_SOLUTION resultS where ");
		sql.append(
				" a.TANGENT_CUSTOMER_ID=resultS.TANGENT_CUSTOMER_ID order by RESULT_SOLUTION_ORDER desc) where rownum =1) PRESENT_SOLUTION_DATE ");
		sql.append(
				" from TANGENT_CUSTOMER a where a.status in(1,2,3,4,5,6,7) and( a.CREATED_USER= :sysUserId or a.PERFORMER_ID= :sysUserId or (a.PERFORMER_SOLUTION_ID =:sysUserId and a.status=3)))");
		sql.append(
				" select sum(case when (APPOINTMENT_DATE < sysdate and status=2) or (PRESENT_SOLUTION_DATE < sysdate and status=3) then 1 else 0 end) overTimeKPI, ");
		sql.append(
				" sum(case when (APPOINTMENT_DATE < sysdate and status=2) or (PRESENT_SOLUTION_DATE < sysdate and status=3) then 0 else 1 end) onTimeKPI ");
		sql.append(" from tbl ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("sysUserId", request.getSysUserId());
		query.addScalar("overTimeKPI", new LongType());
		query.addScalar("onTimeKPI", new LongType());
		query.setResultTransformer(Transformers.aliasToBean(TangentCustomerDTO.class));
		return (TangentCustomerDTO) query.list().get(0);
	}

	public List<TangentCustomerDTO> getListTangentCustomer(SysUserRequest request) {
		StringBuilder sql = new StringBuilder("");
		sql.append(
				" select a.TANGENT_CUSTOMER_ID tangentCustomerId, a.CUSTOMER_NAME customerName,a.CUSTOMER_PHONE customerPhone,a.ADDRESS address,a.STATUS status, ");
		sql.append(
				" b.login_name||' - '|| b.full_name createdUserName,c.login_name||' - '|| c.full_name performerName,to_char(a.SUGGEST_TIME,'dd/MM/yyyy') suggestTimeCustomer, ");
		sql.append(
				" (select RESULT_TANGENT_id from (select RESULT_TANGENT_id from RESULT_TANGENT resultT where a.TANGENT_CUSTOMER_ID=resultT.TANGENT_CUSTOMER_ID ");
		sql.append(" order by ORDER_RESULT_TANGENT desc) where rownum =1) resultTangentId,  ");
		sql.append(
				" (select REASON_REJECTION from (select REASON_REJECTION from RESULT_TANGENT resultT where a.TANGENT_CUSTOMER_ID=resultT.TANGENT_CUSTOMER_ID  ");
		sql.append(" order by ORDER_RESULT_TANGENT desc) where rownum =1) reasonRejection, ");
		sql.append(
				" (select RESULT_TANGENT_TYPE from (select RESULT_TANGENT_TYPE from RESULT_TANGENT resultT where a.TANGENT_CUSTOMER_ID=resultT.TANGENT_CUSTOMER_ID  ");
		sql.append(" order by ORDER_RESULT_TANGENT desc) where rownum =1) resultTangentType, ");
		sql.append(
				" (select RESULT_SOLUTION_ID from (select RESULT_SOLUTION_ID from RESULT_SOLUTION resultS where a.TANGENT_CUSTOMER_ID=resultS.TANGENT_CUSTOMER_ID ");
		sql.append(" order by RESULT_SOLUTION_ORDER desc) where rownum =1) resultSolutionId, ");
		sql.append(
				" (select RESULT_SOLUTION_TYPE from (select RESULT_SOLUTION_TYPE from RESULT_SOLUTION resultS where a.TANGENT_CUSTOMER_ID=resultS.TANGENT_CUSTOMER_ID ");
		sql.append(" order by RESULT_SOLUTION_ORDER desc) where rownum =1) resultSolutionType, ");
		sql.append(
				" (select CONTENT_RESULT_SOLUTION from (select CONTENT_RESULT_SOLUTION from RESULT_SOLUTION resultS where a.TANGENT_CUSTOMER_ID=resultS.TANGENT_CUSTOMER_ID ");
		sql.append(" order by RESULT_SOLUTION_ORDER desc) where rownum =1) contentResultSolution, ");
		sql.append(
				" (select CONTRACT_ID from (select CONTRACT_ID from RESULT_SOLUTION resultS where a.TANGENT_CUSTOMER_ID=resultS.TANGENT_CUSTOMER_ID ");
		sql.append(" order by RESULT_SOLUTION_ORDER desc) where rownum =1) contractId, ");
		sql.append(
				" (select CONTRACT_CODE from (select CONTRACT_CODE from RESULT_SOLUTION resultS where a.TANGENT_CUSTOMER_ID=resultS.TANGENT_CUSTOMER_ID ");
		sql.append(" order by RESULT_SOLUTION_ORDER desc) where rownum =1) contractCode, ");
		sql.append(
				" (select to_char(SIGN_DATE,'dd/MM/yyyy') from (select SIGN_DATE from RESULT_SOLUTION resultS where a.TANGENT_CUSTOMER_ID=resultS.TANGENT_CUSTOMER_ID ");
		sql.append(" order by RESULT_SOLUTION_ORDER desc) where rownum =1) signDate, ");
		sql.append(
				" (select CONTRACT_PRICE from (select CONTRACT_PRICE from RESULT_SOLUTION resultS where a.TANGENT_CUSTOMER_ID=resultS.TANGENT_CUSTOMER_ID ");
		sql.append(" order by RESULT_SOLUTION_ORDER desc) where rownum =1) contractPrice, ");
		sql.append(
				" (select to_char(PRESENT_SOLUTION_DATE,'dd/MM/yyyy') from (select PRESENT_SOLUTION_DATE from RESULT_SOLUTION resultS where a.TANGENT_CUSTOMER_ID=resultS.TANGENT_CUSTOMER_ID ");
		sql.append(" order by RESULT_SOLUTION_ORDER desc) where rownum =1) presentSolutionDate, ");
		sql.append(
				" (select to_char(PRESENT_SOLUTION_DATE,'dd/MM/yyyy') from (select PRESENT_SOLUTION_DATE from RESULT_SOLUTION resultS where a.TANGENT_CUSTOMER_ID=resultS.TANGENT_CUSTOMER_ID ");
		sql.append(" order by RESULT_SOLUTION_ORDER desc) where rownum =1) presentSolutionDateCheck, ");
		sql.append(
				" (select to_char(PRESENT_SOLUTION_DATE_NEXT,'dd/MM/yyyy') from (select PRESENT_SOLUTION_DATE_NEXT from RESULT_SOLUTION resultS where ");
		sql.append(
				" a.TANGENT_CUSTOMER_ID=resultS.TANGENT_CUSTOMER_ID  order by RESULT_SOLUTION_ORDER desc) where rownum =1) presentSolutionDateNext, ");
		sql.append(
				" (select to_char(APPOINTMENT_DATE,'dd/MM/yyyy') from (select APPOINTMENT_DATE from RESULT_TANGENT resultT where a.TANGENT_CUSTOMER_ID=resultT.TANGENT_CUSTOMER_ID ");
		sql.append(
				" order by ORDER_RESULT_TANGENT desc) where rownum =1) appointmentDateCustomer,a.PERFORMER_ID performerId,c.email,c.phone_number phoneNumber, ");
		sql.append(
				" a.PERFORMER_SOLUTION_ID performerSolutionId,d.LOGIN_NAME||' - '||d.full_name fullNameSolution,case when a.CREATED_USER= :sysUserId then 1 else 0 end checkCreatedUser,");
		sql.append(
				" case when a.PERFORMER_ID= :sysUserId or (a.PERFORMER_SOLUTION_ID =:sysUserId and a.status=3) then 1 else 0 end checkPerformerUser,a.BIRTH_YEAR birthYear,a.CONTENT_CUSTOMER contentCustomer, ");
		sql.append(
				" a.CUSTOMER_EMAIL customerEmail,b.email createdEmail,b.phone_Number createdPhoneNumber,a.PROVINCE_ID provinceId,a.province_code provinceCode from TANGENT_CUSTOMER a left join sys_user b on a.CREATED_USER=b.sys_user_id ");
		sql.append(" left join sys_user c on a.PERFORMER_ID=c.sys_user_id ");
		sql.append(" left join sys_user d on a.PERFORMER_SOLUTION_ID=d.sys_user_id ");
		sql.append(
				" where a.status in(1,2,3,4,5,6,7) and( a.CREATED_USER= :sysUserId or a.PERFORMER_ID= :sysUserId or (a.PERFORMER_SOLUTION_ID =:sysUserId and a.status=3)) ");
		sql.append(" order by a.TANGENT_CUSTOMER_ID desc ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("tangentCustomerId", new LongType());
		query.addScalar("customerName", new StringType());
		query.addScalar("customerPhone", new StringType());
		query.addScalar("address", new StringType());
		query.addScalar("status", new StringType());
		query.addScalar("createdUserName", new StringType());
		query.addScalar("performerName", new StringType());
		query.addScalar("suggestTimeCustomer", new StringType());
		query.addScalar("appointmentDateCustomer", new StringType());
		query.addScalar("resultTangentId", new LongType());
		query.addScalar("reasonRejection", new StringType());
		query.addScalar("resultTangentType", new StringType());
		query.addScalar("resultSolutionId", new LongType());
		query.addScalar("resultSolutionType", new StringType());
		query.addScalar("contentResultSolution", new StringType());
		query.addScalar("contractId", new LongType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("contractPrice", new StringType());
		query.addScalar("signDate", new StringType());
		query.addScalar("presentSolutionDate", new StringType());
		query.addScalar("presentSolutionDateNext", new StringType());
		query.addScalar("performerId", new LongType());
		query.addScalar("performerSolutionId", new LongType());
		query.addScalar("email", new StringType());
		query.addScalar("phoneNumber", new StringType());
		query.addScalar("fullNameSolution", new StringType());
		query.addScalar("presentSolutionDateCheck", new StringType());
		query.addScalar("checkCreatedUser", new StringType());
		query.addScalar("checkPerformerUser", new StringType());
		query.addScalar("birthYear", new StringType());
		query.addScalar("contentCustomer", new StringType());
		query.addScalar("customerEmail", new StringType());
		query.addScalar("createdEmail", new StringType());
		query.addScalar("createdPhoneNumber", new StringType());
		query.addScalar("provinceId", new LongType());
		query.addScalar("provinceCode", new StringType());
		query.setParameter("sysUserId", request.getSysUserId());
		query.setResultTransformer(Transformers.aliasToBean(TangentCustomerDTO.class));
		return query.list();
	}

	public List<TangentCustomerDTO> getListContract(SysUserRequest request) {
		StringBuilder sql = new StringBuilder("");
		sql.append(
				" select CNT_CONTRACT_ID contractId,code contractCode,to_char(SIGN_DATE,'dd/MM/yyyy') signDate,PRICE contractPrice,WARRANTY_NUMBER_DAY guaranteeTime,to_char(DEPLOYMENT_DATE,'dd/MM/yyyy') constructTime from cnt_contract  ");
		sql.append(
				" where CONTRACT_TYPE=0 and status!=0 and CONTRACT_TYPE_O=4 and CNT_CONTRACT_ID not in(select nvl(CONTRACT_ID,0) from RESULT_SOLUTION) and SYS_GROUP_ID= :sysGroupId ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("contractId", new LongType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("signDate", new StringType());
		query.addScalar("contractPrice", new StringType());
		query.addScalar("guaranteeTime", new StringType());
		query.addScalar("constructTime", new StringType());
		query.setParameter("sysGroupId", request.getSysGroupId());
		query.setResultTransformer(Transformers.aliasToBean(TangentCustomerDTO.class));
		return query.list();
	}

	public int updateTangentCustomer(TangentCustomerRequest request) {
		try {
			// cap nhat ngay hen tiep xuc
			if ("1".equals(request.getTangentCustomerDTO().getStatus())) {
				StringBuilder sqlTange = new StringBuilder("");
				sqlTange.append(
						"Update TANGENT_CUSTOMER set status=2,UPDATED_USER= :sysUserId ,UPDATED_DATE =:updatedDate where TANGENT_CUSTOMER_ID= :tangentCustomerId ");
				SQLQuery queryCar = getSession().createSQLQuery(sqlTange.toString());
				queryCar.setParameter("tangentCustomerId", request.getTangentCustomerDTO().getTangentCustomerId());
				queryCar.setParameter("sysUserId", request.getSysUserRequest().getSysUserId());
				queryCar.setParameter("updatedDate", new Date());
				queryCar.executeUpdate();
				List<ResultTangentDTO> lstOrderResultTangent = getOrderResultTangent(
						request.getTangentCustomerDTO().getTangentCustomerId());
				if (lstOrderResultTangent.size() > 0) {
					// cap nhat bang RESULT_TANGENT
					StringBuilder sqlResult = new StringBuilder("");
					sqlResult.append(
							"Update RESULT_TANGENT set APPOINTMENT_DATE= to_date(:appointmentDate,'dd/MM/yyyy'),UPDATED_USER= :sysUserId ,UPDATED_DATE =:updatedDate where RESULT_TANGENT_ID= :resultTangentId ");
					SQLQuery queryResult = getSession().createSQLQuery(sqlResult.toString());
					queryResult.setParameter("appointmentDate",
							request.getTangentCustomerDTO().getAppointmentDateCustomer());
					queryResult.setParameter("resultTangentId", request.getTangentCustomerDTO().getResultTangentId());
					queryResult.setParameter("sysUserId", request.getSysUserRequest().getSysUserId());
					queryResult.setParameter("updatedDate", new Date());
					queryResult.executeUpdate();

				} else {
					ResultTangentDTO dto = new ResultTangentDTO();
					dto.setTangentCustomerId(request.getTangentCustomerDTO().getTangentCustomerId());
					DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
					dto.setAppointmentDate(
							dateFormat.parse(request.getTangentCustomerDTO().getAppointmentDateCustomer()));
					dto.setOrderResultTangent(Integer.toString((lstOrderResultTangent.size() + 1)));
					dto.setCreatedUser(request.getSysUserRequest().getSysUserId());
					dto.setCreatedDate(new Date());
					dto.setPerformerId(request.getTangentCustomerDTO().getPerformerId());
					Long resultTangent = resultTangentDAO.saveObject(dto.toModel());
				}
				// gui toi nhan vien thuc hien
				String content = "Yêu cầu tiếp xúc khách hàng " + request.getTangentCustomerDTO().getCustomerName()
						+ " của Đ/c đã được nhân viên: " + request.getTangentCustomerDTO().getPerformerName()
						+ " xác nhận thời gian triển khai tiếp xúc ";
				insertSendEmail(request.getTangentCustomerDTO().getCreatedEmail(),
						request.getSysUserRequest().getSysUserId(), content);
				insertSendSMS(request.getTangentCustomerDTO().getCreatedEmail(),
						request.getTangentCustomerDTO().getCreatedPhoneNumber(),
						request.getSysUserRequest().getSysUserId(), content);
				// gui toi quan ly
				List<TangentCustomerDTO> lstManager = getListManager(request.getTangentCustomerDTO().getProvinceId());
				if (lstManager.size() > 0) {
					for (TangentCustomerDTO dto : lstManager) {
						content = "Yêu cầu tiếp xúc khách hàng " + request.getTangentCustomerDTO().getCustomerName()
								+ " của CNKT Tỉnh/TP đã được xác nhận thời gian triển khai ";
						insertSendEmail(dto.getEmail(), request.getSysUserRequest().getSysUserId(), content);
						insertSendSMS(dto.getEmail(), dto.getPhoneNumber(), request.getSysUserRequest().getSysUserId(),
								content);
					}
				}
			}
			// cap nhat nhu cau khach hang
			else if ("2".equals(request.getTangentCustomerDTO().getStatus())) {
				// KH co nhu cau
				String KQTX = "";
				if ("1".equals(request.getTangentCustomerDTO().getResultTangentType())) {
					StringBuilder sqlTange = new StringBuilder("");
					sqlTange.append(
							"Update TANGENT_CUSTOMER set status=3,UPDATED_USER= :sysUserId ,UPDATED_DATE =:updatedDate where TANGENT_CUSTOMER_ID= :tangentCustomerId ");
					SQLQuery queryTangent = getSession().createSQLQuery(sqlTange.toString());
					queryTangent.setParameter("tangentCustomerId",
							request.getTangentCustomerDTO().getTangentCustomerId());
					queryTangent.setParameter("sysUserId", request.getSysUserRequest().getSysUserId());
					queryTangent.setParameter("updatedDate", new Date());
					queryTangent.executeUpdate();
					// cap nhat bang RESULT_TANGENT
					StringBuilder sqlResult = new StringBuilder("");
					sqlResult.append(
							"Update RESULT_TANGENT set RESULT_TANGENT_TYPE=1,UPDATED_USER= :sysUserId ,UPDATED_DATE =:updatedDate,REALITY_TANGENT_DATE=:updatedDate where RESULT_TANGENT_ID= :resultTangentId ");
					SQLQuery queryResult = getSession().createSQLQuery(sqlResult.toString());
					queryResult.setParameter("resultTangentId", request.getTangentCustomerDTO().getResultTangentId());
					queryResult.setParameter("sysUserId", request.getSysUserRequest().getSysUserId());
					queryResult.setParameter("updatedDate", new Date());
					queryResult.executeUpdate();
					if (request.getLstImage() != null) {
						List<TangentCustomerDTO> lstPakageImages = saveConstructionImages(request.getLstImage());
						saveImagePathsDao(lstPakageImages, request.getTangentCustomerDTO().getResultTangentId(),
								request.getSysUserRequest(), request.getTangentCustomerDTO().getStatus());
					}
					KQTX = "Khách hàng có nhu cầu";
				} else {
					StringBuilder sqlTange = new StringBuilder("");
					sqlTange.append(
							"Update TANGENT_CUSTOMER set status=4,UPDATED_USER= :sysUserId ,UPDATED_DATE =:updatedDate where TANGENT_CUSTOMER_ID= :tangentCustomerId ");
					SQLQuery queryCar = getSession().createSQLQuery(sqlTange.toString());
					queryCar.setParameter("sysUserId", request.getSysUserRequest().getSysUserId());
					queryCar.setParameter("updatedDate", new Date());
					queryCar.setParameter("tangentCustomerId", request.getTangentCustomerDTO().getTangentCustomerId());
					queryCar.executeUpdate();
					// cap nhat bang RESULT_TANGENT
					StringBuilder sqlResult = new StringBuilder("");
					sqlResult.append(
							"Update RESULT_TANGENT set RESULT_TANGENT_TYPE=0,APPROVED_STATUS=0,UPDATED_USER= :sysUserId ,UPDATED_DATE =:updatedDate,REASON_REJECTION= :reasonRejection,REALITY_TANGENT_DATE=:updatedDate where RESULT_TANGENT_ID= :resultTangentId ");
					SQLQuery queryResult = getSession().createSQLQuery(sqlResult.toString());
					queryResult.setParameter("resultTangentId", request.getTangentCustomerDTO().getResultTangentId());
					queryResult.setParameter("sysUserId", request.getSysUserRequest().getSysUserId());
					queryResult.setParameter("updatedDate", new Date());
					queryResult.setParameter("reasonRejection", request.getTangentCustomerDTO().getReasonRejection());
					queryResult.executeUpdate();
					KQTX = "Khách hàng không có nhu cầu";
				}
				// gui ket qua tiep xuc toi quan ly
				List<TangentCustomerDTO> lstManager = getListManager(request.getTangentCustomerDTO().getProvinceId());
				if (lstManager.size() > 0) {
					for (TangentCustomerDTO dto : lstManager) {
						String content = "Khách hàng " + request.getTangentCustomerDTO().getCustomerName()
								+ " đã được nhân viên " + request.getTangentCustomerDTO().getPerformerName()
								+ " tiếp xúc thành công, kết quả tiếp xúc: " + KQTX;
						insertSendEmail(dto.getEmail(), request.getSysUserRequest().getSysUserId(), content);
						insertSendSMS(dto.getEmail(), dto.getPhoneNumber(), request.getSysUserRequest().getSysUserId(),
								content);
					}
				}
				// gui ket qua tiep xuc toi TTHT
				List<TangentCustomerDTO> lstUserTTHT = getListUserTTHT();
				if (lstUserTTHT.size() > 0) {
					for (TangentCustomerDTO dto : lstUserTTHT) {
						String content = "Khách hàng " + request.getTangentCustomerDTO().getCustomerName()
								+ " đã được CNKT Tỉnh/TP: " + request.getTangentCustomerDTO().getProvinceCode()
								+ " tiếp xúc thành công, kết quả tiếp xúc: " + KQTX;
						insertSendEmail(dto.getEmail(), request.getSysUserRequest().getSysUserId(), content);
						insertSendSMS(dto.getEmail(), dto.getPhoneNumber(), request.getSysUserRequest().getSysUserId(),
								content);
					}
				}
			} else if ("3".equals(request.getTangentCustomerDTO().getStatus())) {
				if (request.getTangentCustomerDTO().getPresentSolutionDateCheck() != null) {
					List<ConstructionImageInfo> listImage = getImagesResultSolutionId(
							request.getTangentCustomerDTO().getResultSolutionId());
					// nhap ngay trinh bay, chup anh
					if (listImage.size() > 0) {
						StringBuilder sqlTange = new StringBuilder("");
						sqlTange.append(
								"Update TANGENT_CUSTOMER set status=:status,UPDATED_USER= :sysUserId ,UPDATED_DATE =:updatedDate where TANGENT_CUSTOMER_ID= :tangentCustomerId ");
						SQLQuery queryTange = getSession().createSQLQuery(sqlTange.toString());
						queryTange.setParameter("tangentCustomerId",
								request.getTangentCustomerDTO().getTangentCustomerId());
						queryTange.setParameter("sysUserId", request.getSysUserRequest().getSysUserId());
						queryTange.setParameter("updatedDate", new Date());
						if ("1".equals(request.getTangentCustomerDTO().getResultSolutionType())) {
							if (request.getTangentCustomerDTO().getContractCode() == null) {
								queryTange.setParameter("status", 5);
							} else {
								queryTange.setParameter("status", 8);
							}
						} else if ("2".equals(request.getTangentCustomerDTO().getResultSolutionType())) {
							queryTange.setParameter("status", 7);
						} else if ("3".equals(request.getTangentCustomerDTO().getResultSolutionType())) {
							queryTange.setParameter("status", 6);
						}
						queryTange.executeUpdate();
						// cap nhat bang ResultSolution
						StringBuilder sqlResult = new StringBuilder("");
						sqlResult.append("Update RESULT_SOLUTION set RESULT_SOLUTION_TYPE=:resultSolutionType,");
						if ("1".equals(request.getTangentCustomerDTO().getResultSolutionType())
								&& request.getTangentCustomerDTO().getContractCode() != null) {
							sqlResult.append(
									" CONTRACT_ID =:contractId,CONTRACT_CODE =:contractCode,CONTRACT_PRICE =:contractPrice,CONTRACT_ROSE= round(:contractRose,2),");
							sqlResult.append(
									" GUARANTEE_TIME=:guaranteeTime,CONSTRUCT_TIME=to_date(:constructTime,'dd/MM/yyyy'),SIGN_DATE =to_date(:singDate,'dd/MM/yyyy'), ");
						} else if ("2".equals(request.getTangentCustomerDTO().getResultSolutionType())) {
							sqlResult.append("CONTENT_RESULT_SOLUTION =:contentResultSolution, ");
						} else if ("3".equals(request.getTangentCustomerDTO().getResultSolutionType())) {
							sqlResult.append(
									"CONTENT_RESULT_SOLUTION =:contentResultSolution,PRESENT_SOLUTION_DATE_NEXT =to_date(:presentSolutionDateNext,'dd/MM/yyyy'), ");
						}
						sqlResult.append(
								" UPDATED_USER= :sysUserId ,UPDATED_DATE =:updatedDate,REALITY_SOLUTION_DATE=:updatedDate where RESULT_SOLUTION_ID= :resultSolutionId ");
						SQLQuery queryResult = getSession().createSQLQuery(sqlResult.toString());
						queryResult.setParameter("resultSolutionId",
								request.getTangentCustomerDTO().getResultSolutionId());
						queryResult.setParameter("sysUserId", request.getSysUserRequest().getSysUserId());
						queryResult.setParameter("updatedDate", new Date());
						queryResult.setParameter("resultSolutionType",
								request.getTangentCustomerDTO().getResultSolutionType());
						if ("1".equals(request.getTangentCustomerDTO().getResultSolutionType())
								&& request.getTangentCustomerDTO().getContractCode() != null) {
							queryResult.setParameter("contractId", request.getTangentCustomerDTO().getContractId());
							queryResult.setParameter("contractCode", request.getTangentCustomerDTO().getContractCode());
							queryResult.setParameter("contractPrice",
									request.getTangentCustomerDTO().getContractPrice());
							Double contractRose = (Double.parseDouble(
									request.getTangentCustomerDTO().getContractPrice()) * getContractRose()) / 1.1;
							queryResult.setParameter("contractRose", contractRose);
							queryResult.setParameter("singDate", request.getTangentCustomerDTO().getSignDate());
							queryResult.setParameter("guaranteeTime",
									request.getTangentCustomerDTO().getGuaranteeTime());
							queryResult.setParameter("constructTime",
									request.getTangentCustomerDTO().getConstructTime());
						} else if ("2".equals(request.getTangentCustomerDTO().getResultSolutionType())) {
							queryResult.setParameter("contentResultSolution",
									request.getTangentCustomerDTO().getContentResultSolution());
						} else if ("3".equals(request.getTangentCustomerDTO().getResultSolutionType())) {
							queryResult.setParameter("contentResultSolution",
									request.getTangentCustomerDTO().getContentResultSolution());
							queryResult.setParameter("presentSolutionDateNext",
									request.getTangentCustomerDTO().getPresentSolutionDateNext());
						}
						queryResult.executeUpdate();
						// insert bang ResultSolution khi chon bo sung
						if ("3".equals(request.getTangentCustomerDTO().getResultSolutionType())) {
							List<ResultSolutionDTO> lstOrderResultSolution = getOrderResultSolution(
									request.getTangentCustomerDTO().getTangentCustomerId());
							ResultSolutionDTO dto = new ResultSolutionDTO();
							dto.setTangentCustomerId(request.getTangentCustomerDTO().getTangentCustomerId());
							DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
							dto.setPresentSolutionDate(
									dateFormat.parse(request.getTangentCustomerDTO().getPresentSolutionDateNext()));
							// dto.setPresentSolutionDateNext(dateFormat.parse(request.getTangentCustomerDTO().getPresentSolutionDateNext()));
							dto.setResultSolutionOrder(Long.valueOf(lstOrderResultSolution.size() + 1));
							dto.setCreatedUser(request.getSysUserRequest().getSysUserId());
							dto.setCreatedDate(new Date());
							dto.setPerformerId(request.getTangentCustomerDTO().getPerformerSolutionId());
							Long resultSolutionId = resultSolutionDAO.saveObject(dto.toModel());
						}
						return 1;
					}
				}
				Long resultSolutionId = 0L;
				// xu ly khi nhap ngay hen trinh bay giai phap
				if (request.getTangentCustomerDTO().getResultSolutionId() == null) {
					List<ResultSolutionDTO> lstOrderResultSolution = getOrderResultSolution(
							request.getTangentCustomerDTO().getTangentCustomerId());
					ResultSolutionDTO dto = new ResultSolutionDTO();
					dto.setTangentCustomerId(request.getTangentCustomerDTO().getTangentCustomerId());
					if (request.getTangentCustomerDTO().getPresentSolutionDate() != null) {
						DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
						dto.setPresentSolutionDate(
								dateFormat.parse(request.getTangentCustomerDTO().getPresentSolutionDate()));
					}
					dto.setResultSolutionOrder(Long.valueOf(lstOrderResultSolution.size() + 1));
					dto.setCreatedUser(request.getSysUserRequest().getSysUserId());
					dto.setCreatedDate(new Date());
					dto.setPerformerId(request.getTangentCustomerDTO().getPerformerSolutionId());
					resultSolutionId = resultSolutionDAO.saveObject(dto.toModel());
				} else {
					resultSolutionId = request.getTangentCustomerDTO().getResultSolutionId();
					if (request.getTangentCustomerDTO().getPresentSolutionDateCheck() == null
							&& request.getTangentCustomerDTO().getPresentSolutionDate() != null) {
						StringBuilder sqlResult = new StringBuilder("");
						sqlResult.append(
								"Update RESULT_SOLUTION set PRESENT_SOLUTION_DATE =to_date(:presentSolutionDate,'dd/MM/yyyy'),  UPDATED_USER= :sysUserId ,UPDATED_DATE =:updatedDate,REALITY_SOLUTION_DATE=:updatedDate where RESULT_SOLUTION_ID= :resultSolutionId");
						SQLQuery queryResult = getSession().createSQLQuery(sqlResult.toString());
						queryResult.setParameter("resultSolutionId",
								request.getTangentCustomerDTO().getResultSolutionId());
						queryResult.setParameter("sysUserId", request.getSysUserRequest().getSysUserId());
						queryResult.setParameter("updatedDate", new Date());
						queryResult.setParameter("presentSolutionDate",
								request.getTangentCustomerDTO().getPresentSolutionDate());
						queryResult.executeUpdate();
					}
				}
				// xu ly khi chup anh
				if (request.getLstImage() != null) {
					List<TangentCustomerDTO> lstPakageImages = saveConstructionImages(request.getLstImage());
					saveImagePathsDao(lstPakageImages, resultSolutionId, request.getSysUserRequest(),
							request.getTangentCustomerDTO().getStatus());
					String content = "Nhân viên " + request.getTangentCustomerDTO().getFullNameSolution()
							+ " đã hoàn thành tài liệu giải pháp cho khách hàng: "
							+ request.getTangentCustomerDTO().getCustomerName() + ", địa chỉ: "
							+ request.getTangentCustomerDTO().getAddress() + ", SĐT khách hàng: "
							+ request.getTangentCustomerDTO().getCustomerPhone();
					insertSendEmail(request.getTangentCustomerDTO().getEmail(),
							request.getSysUserRequest().getSysUserId(), content);
					insertSendSMS(request.getTangentCustomerDTO().getEmail(),
							request.getTangentCustomerDTO().getPhoneNumber(),
							request.getSysUserRequest().getSysUserId(), content);
				}
				// gui ket qua trinh bay giai phap toi quan ly
				List<TangentCustomerDTO> lstManager = getListManager(request.getTangentCustomerDTO().getProvinceId());
				if (lstManager.size() > 0) {
					String content = "";
					if ("1".equals(request.getTangentCustomerDTO().getResultSolutionType())
							&& request.getTangentCustomerDTO().getContractCode() != null) {
						content = "Khách hàng " + request.getTangentCustomerDTO().getCustomerName()
								+ " đã được trình bày giải pháp, kết quả khách hàng đồng ý ký hợp đồng số: "
								+ request.getTangentCustomerDTO().getContractCode();
					} else if ("2".equals(request.getTangentCustomerDTO().getResultSolutionType())) {
						content = "Khách hàng " + request.getTangentCustomerDTO().getCustomerName()
								+ " đã được trình bày giải pháp, kết quả khách hàng từ chối do: "
								+ request.getTangentCustomerDTO().getContentResultSolution();
					} else if ("3".equals(request.getTangentCustomerDTO().getResultSolutionType())) {
						content = "Khách hàng " + request.getTangentCustomerDTO().getCustomerName()
								+ " đã được trình bày giải pháp, kết quả khách hàng yêu cầu điều chỉnh giải pháp: "
								+ request.getTangentCustomerDTO().getContentResultSolution();
					}
					if (!"".equals(content)) {
						for (TangentCustomerDTO dto : lstManager) {
							insertSendEmail(dto.getEmail(), request.getSysUserRequest().getSysUserId(), content);
							insertSendSMS(dto.getEmail(), dto.getPhoneNumber(),
									request.getSysUserRequest().getSysUserId(), content);
						}
					}
				}
				// gui ket qua trinh bay giai phap toi TTHT
				List<TangentCustomerDTO> lstUserTTHT = getListUserTTHT();
				if (lstUserTTHT.size() > 0) {
					String content = "";
					if ("1".equals(request.getTangentCustomerDTO().getResultSolutionType())
							&& request.getTangentCustomerDTO().getContractCode() != null) {
						content = "Khách hàng " + request.getTangentCustomerDTO().getCustomerName()
								+ " đã được CNKT Tỉnh/TP: " + request.getTangentCustomerDTO().getProvinceCode()
								+ " trình bày giải pháp, kết quả khách hàng đồng ý ký hợp đồng số: "
								+ request.getTangentCustomerDTO().getContractCode();
					} else if ("2".equals(request.getTangentCustomerDTO().getResultSolutionType())) {
						content = "Khách hàng " + request.getTangentCustomerDTO().getCustomerName()
								+ " đã được CNKT Tỉnh/TP: " + request.getTangentCustomerDTO().getProvinceCode()
								+ " trình bày giải pháp, kết quả khách hàng từ chối do: "
								+ request.getTangentCustomerDTO().getContentResultSolution();
					} else if ("3".equals(request.getTangentCustomerDTO().getResultSolutionType())) {
						content = "Khách hàng " + request.getTangentCustomerDTO().getCustomerName()
								+ " đã được CNKT Tỉnh/TP: " + request.getTangentCustomerDTO().getProvinceCode()
								+ " trình bày giải pháp, kết quả khách hàng yêu cầu điều chỉnh giải pháp: "
								+ request.getTangentCustomerDTO().getContentResultSolution();
					}
					if (!"".equals(content)) {
						for (TangentCustomerDTO dto : lstUserTTHT) {
							insertSendEmail(dto.getEmail(), request.getSysUserRequest().getSysUserId(), content);
							insertSendSMS(dto.getEmail(), dto.getPhoneNumber(),
									request.getSysUserRequest().getSysUserId(), content);
						}
					}
				}
			} else if ("6".equals(request.getTangentCustomerDTO().getStatus())) {
				StringBuilder sqlTange = new StringBuilder("");
				sqlTange.append(
						"Update TANGENT_CUSTOMER set status=3,UPDATED_USER= :sysUserId ,UPDATED_DATE =:updatedDate where TANGENT_CUSTOMER_ID= :tangentCustomerId ");
				SQLQuery queryTange = getSession().createSQLQuery(sqlTange.toString());
				queryTange.setParameter("tangentCustomerId", request.getTangentCustomerDTO().getTangentCustomerId());
				queryTange.setParameter("sysUserId", request.getSysUserRequest().getSysUserId());
				queryTange.setParameter("updatedDate", new Date());
				queryTange.executeUpdate();
				if (request.getLstImage() != null) {
					List<TangentCustomerDTO> lstPakageImages = saveConstructionImages(request.getLstImage());
					saveImagePathsDao(lstPakageImages, request.getTangentCustomerDTO().getResultSolutionId(),
							request.getSysUserRequest(), request.getTangentCustomerDTO().getStatus());
					String content = "Nhân viên " + request.getTangentCustomerDTO().getFullNameSolution()
							+ " đã hoàn thành tài liệu giải pháp cho khách hàng: "
							+ request.getTangentCustomerDTO().getCustomerName() + ", địa chỉ: "
							+ request.getTangentCustomerDTO().getAddress() + ", SĐT khách hàng: "
							+ request.getTangentCustomerDTO().getCustomerPhone();
					insertSendEmail(request.getTangentCustomerDTO().getEmail(),
							request.getSysUserRequest().getSysUserId(), content);
					insertSendSMS(request.getTangentCustomerDTO().getEmail(),
							request.getTangentCustomerDTO().getPhoneNumber(),
							request.getSysUserRequest().getSysUserId(), content);
				}
			} else if ("5".equals(request.getTangentCustomerDTO().getStatus())) {
				StringBuilder sqlTange = new StringBuilder("");
				sqlTange.append(
						"Update TANGENT_CUSTOMER set status=8,UPDATED_USER= :sysUserId ,UPDATED_DATE =:updatedDate where TANGENT_CUSTOMER_ID= :tangentCustomerId ");
				SQLQuery queryTange = getSession().createSQLQuery(sqlTange.toString());
				queryTange.setParameter("tangentCustomerId", request.getTangentCustomerDTO().getTangentCustomerId());
				queryTange.setParameter("sysUserId", request.getSysUserRequest().getSysUserId());
				queryTange.setParameter("updatedDate", new Date());
				queryTange.executeUpdate();
				// cap nhat bang ResultSolution
				StringBuilder sqlResult = new StringBuilder("");
				sqlResult.append(
						"Update RESULT_SOLUTION set CONTRACT_ID =:contractId,CONTRACT_CODE =:contractCode,CONTRACT_PRICE =:contractPrice,CONTRACT_ROSE= round(:contractRose,2),SIGN_DATE =to_date(:singDate,'dd/MM/yyyy'),");
				sqlResult.append(
						" UPDATED_USER= :sysUserId ,UPDATED_DATE =:updatedDate,REALITY_SOLUTION_DATE=:updatedDate,GUARANTEE_TIME=:guaranteeTime,CONSTRUCT_TIME=to_date(:constructTime,'dd/MM/yyyy') where RESULT_SOLUTION_ID= :resultSolutionId ");
				SQLQuery queryResult = getSession().createSQLQuery(sqlResult.toString());
				queryResult.setParameter("resultSolutionId", request.getTangentCustomerDTO().getResultSolutionId());
				queryResult.setParameter("sysUserId", request.getSysUserRequest().getSysUserId());
				queryResult.setParameter("updatedDate", new Date());
				queryResult.setParameter("contractId", request.getTangentCustomerDTO().getContractId());
				queryResult.setParameter("contractCode", request.getTangentCustomerDTO().getContractCode());
				queryResult.setParameter("contractPrice", request.getTangentCustomerDTO().getContractPrice());
				Double contractRose = (Double.parseDouble(request.getTangentCustomerDTO().getContractPrice())
						* getContractRose()) / 1.1;
				queryResult.setParameter("contractRose", contractRose);
				queryResult.setParameter("singDate", request.getTangentCustomerDTO().getSignDate());
				queryResult.setParameter("guaranteeTime", request.getTangentCustomerDTO().getGuaranteeTime());
				queryResult.setParameter("constructTime", request.getTangentCustomerDTO().getConstructTime());
				queryResult.executeUpdate();
				// gui ket qua trinh bay giai phap toi quan ly
				List<TangentCustomerDTO> lstManager = getListManager(request.getTangentCustomerDTO().getProvinceId());
				if (lstManager.size() > 0) {
					String content = "Khách hàng " + request.getTangentCustomerDTO().getCustomerName()
							+ " đã được trình bày giải pháp, kết quả khách hàng đồng ý ký hợp đồng số: "
							+ request.getTangentCustomerDTO().getContractCode();
					for (TangentCustomerDTO dto : lstManager) {
						insertSendEmail(dto.getEmail(), request.getSysUserRequest().getSysUserId(), content);
						insertSendSMS(dto.getEmail(), dto.getPhoneNumber(), request.getSysUserRequest().getSysUserId(),
								content);
					}
				}
				// gui ket qua trinh bay giai phap toi TTHT
				List<TangentCustomerDTO> lstUserTTHT = getListUserTTHT();
				if (lstUserTTHT.size() > 0) {
					String content = "Khách hàng " + request.getTangentCustomerDTO().getCustomerName()
							+ " đã được CNKT Tỉnh/TP: " + request.getTangentCustomerDTO().getProvinceCode()
							+ " trình bày giải pháp, kết quả khách hàng đồng ý ký hợp đồng số: "
							+ request.getTangentCustomerDTO().getContractCode();
					for (TangentCustomerDTO dto : lstUserTTHT) {
						insertSendEmail(dto.getEmail(), request.getSysUserRequest().getSysUserId(), content);
						insertSendSMS(dto.getEmail(), dto.getPhoneNumber(), request.getSysUserRequest().getSysUserId(),
								content);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			this.getSession().clear();
			return 0;
		}
		return 1;
	}

	public void insertSendEmail(String email, Long createdUserId, String content) {
		StringBuilder sql = new StringBuilder(" ");
		sql.append(
				"INSERT INTO SEND_EMAIL(SEND_EMAIL_ID,SUBJECT,CONTENT,STATUS,RECEIVE_EMAIL,CREATED_DATE,CREATED_USER_ID)  ");
		sql.append(
				" values( SEND_EMAIL_seq.nextval, :subject, :content, :status, :receiveEmail, :createdDate, :createdUserId )");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("subject", "Quản lý tiếp xúc khách hàng mảng Xây dựng dân dụng");
		query.setParameter("content", content);
		query.setParameter("status", 0);
		query.setParameter("receiveEmail", email);
		query.setParameter("createdDate", new Date());
		query.setParameter("createdUserId", createdUserId);
		query.executeUpdate();
	}

	public void insertSendSMS(String email, String phoneNumber, Long createdUserId, String content) {
		StringBuilder sql = new StringBuilder(" ");
		sql.append(
				"INSERT INTO SEND_SMS_EMAIL(SEND_SMS_EMAIL_ID,SUBJECT,CONTENT,STATUS,RECEIVE_EMAIL,RECEIVE_PHONE_NUMBER,CREATED_DATE,CREATED_USER_ID)  ");
		sql.append(
				" values( SEND_SMS_EMAIL_seq.nextval, :subject, :content, :status, :receiveEmail,:receivePhone, :createdDate, :createdUserId )");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("subject", "Quản lý tiếp xúc khách hàng mảng Xây dựng dân dụng");
		query.setParameter("content", content);
		query.setParameter("status", 0);
		query.setParameter("receiveEmail", email);
		query.setParameter("receivePhone", phoneNumber);
		query.setParameter("createdDate", new Date());
		query.setParameter("createdUserId", createdUserId);
		query.executeUpdate();
	}

	public List<ResultTangentDTO> getOrderResultTangent(Long tangentCustomerId) {
		String sql = "select nvl(ORDER_RESULT_TANGENT,0) orderResultTangent from RESULT_TANGENT resultT where "
				+ " resultT.TANGENT_CUSTOMER_ID= :tangentCustomerId ";
		SQLQuery query = super.getSession().createSQLQuery(sql);
		query.setParameter("tangentCustomerId", tangentCustomerId);
		query.addScalar("orderResultTangent", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(ResultTangentDTO.class));
		return query.list();
	}

	public List<ResultSolutionDTO> getOrderResultSolution(Long tangentCustomerId) {
		String sql = "select nvl(RESULT_SOLUTION_ORDER,0) resultSolutionOrder from RESULT_SOLUTION resultS where "
				+ " resultS.TANGENT_CUSTOMER_ID= :tangentCustomerId ";
		SQLQuery query = super.getSession().createSQLQuery(sql);
		query.setParameter("tangentCustomerId", tangentCustomerId);
		query.addScalar("resultSolutionOrder", new LongType());
		query.setResultTransformer(Transformers.aliasToBean(ResultSolutionDTO.class));
		return query.list();
	}

	public List<TangentCustomerDTO> saveConstructionImages(List<TangentCustomerDTO> lstImages) {
		List<TangentCustomerDTO> result = new ArrayList<>();
		for (TangentCustomerDTO pakageDetailMage : lstImages) {
			if (pakageDetailMage.getStatusImage() == 0) {
				TangentCustomerDTO obj = new TangentCustomerDTO();
				obj.setImageName(pakageDetailMage.getImageName());
				obj.setLatitude(pakageDetailMage.getLatitude());
				obj.setLongtitude(pakageDetailMage.getLongtitude());
				InputStream inputStream = ImageUtil.convertBase64ToInputStream(pakageDetailMage.getBase64String());
				try {
					String imagePath = UFile.writeToFileServerATTT2(inputStream, pakageDetailMage.getImageName(),
							input_image_sub_folder_upload, folder2Upload);
					// hoanm1_20200718_start
					imagePath = UEncrypt.encryptFileUploadPath(imagePath);
					// hoanm1_20200718_end
					obj.setImagePath(imagePath);
				} catch (Exception e) {
					continue;
				}
				result.add(obj);
			}
			if (pakageDetailMage.getStatusImage() == -1 && pakageDetailMage.getStatusImage() != null) {
				updateUtilAttachDocumentById(pakageDetailMage.getUtilAttachDocumentId());
			}
		}

		return result;
	}

	public void updateUtilAttachDocumentById(Long utilAttachDocumentId) {
		StringBuilder sql = new StringBuilder(" ");
		sql.append("DELETE FROM UTIL_ATTACH_DOCUMENT a  WHERE a.UTIL_ATTACH_DOCUMENT_ID =:id ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("id", utilAttachDocumentId);
		query.executeUpdate();
	}

	public void saveImagePathsDao(List<TangentCustomerDTO> lstImages, long objectId, SysUserRequest request,
			String status) {
		if (lstImages == null) {
			return;
		}
		for (TangentCustomerDTO pakageDetailImage : lstImages) {
			UtilAttachDocumentBO utilAttachDocumentBO = new UtilAttachDocumentBO();
			utilAttachDocumentBO.setObjectId(objectId);
			utilAttachDocumentBO.setName(pakageDetailImage.getImageName());
			if ("2".equals(status)) {
				utilAttachDocumentBO.setType("KQTX");
				utilAttachDocumentBO.setDescription("file thu thập thông tin khách hàng");
			} else if ("3".equals(status) || "6".equals(status)) {
				utilAttachDocumentBO.setType("GP");
				utilAttachDocumentBO.setDescription("file trình bày giải pháp");
			}
			utilAttachDocumentBO.setStatus("1");
			utilAttachDocumentBO.setFilePath(pakageDetailImage.getImagePath());
			utilAttachDocumentBO.setCreatedDate(new Date());
			utilAttachDocumentBO.setCreatedUserId(request.getSysUserId());
			utilAttachDocumentBO.setCreatedUserName(request.getName());
			if (pakageDetailImage.getLongtitude() != null) {
				utilAttachDocumentBO.setLongtitude(pakageDetailImage.getLongtitude());
			}
			if (pakageDetailImage.getLatitude() != null) {
				utilAttachDocumentBO.setLatitude(pakageDetailImage.getLatitude());
			}
			long ret = utilAttachDocumentDAO.saveObject(utilAttachDocumentBO);
		}
	}

	public List<ConstructionImageInfo> getImagesResultSolutionId(Long resultSolutionId) {
		String sql = new String(
				"select a.UTIL_ATTACH_DOCUMENT_ID utilAttachDocumentId, a.name imageName, a.file_path imagePath , 1 status from UTIL_ATTACH_DOCUMENT a "
						+ " where a.object_id = :resultSolutionId AND a.TYPE = 'GP' and a.STATUS = 1 "
						+ " ORDER BY a.UTIL_ATTACH_DOCUMENT_ID DESC ");
		SQLQuery query = getSession().createSQLQuery(sql);
		query.addScalar("imageName", new StringType());
		query.addScalar("imagePath", new StringType());
		query.addScalar("status", new LongType());
		query.addScalar("utilAttachDocumentId", new LongType());
		if (resultSolutionId == null) {
			resultSolutionId = 1L;
		}
		query.setParameter("resultSolutionId", resultSolutionId);
		query.setResultTransformer(Transformers.aliasToBean(ConstructionImageInfo.class));
		return query.list();
	}

	public void functionSaveTangentCustomer(TangentCustomerDTO obj) throws ParseException {
		TangentCustomerDTO customerDto = new TangentCustomerDTO();
		customerDto.setCustomerName(obj.getCustomerName());
		customerDto.setBirthYear(obj.getBirthYear());
		customerDto.setAddress(obj.getAddress());
		customerDto.setProvinceId(obj.getProvinceId());
		customerDto.setProvinceCode(obj.getProvinceCode());
		customerDto.setDistrictName(obj.getDistrictName());
		customerDto.setAreaId(obj.getCommuneId());
		customerDto.setCommuneName(obj.getCommuneName());
		customerDto.setApartmentNumber(obj.getApartmentNumber());
		customerDto.setCustomerPhone(obj.getCustomerPhone());
		customerDto.setCustomerEmail(obj.getCustomerEmail());
		customerDto.setCrmId(obj.getCrmId());
		customerDto.setReceptionChannel(obj.getReceptionChannel());
		customerDto.setSource(obj.getSource());
		customerDto.setGroupOrder(obj.getGroupOrder());
		customerDto.setCustomerResources(obj.getCustomerResources());
		TangentCustomerDTO dtoPerformer = getPerformerId(obj.getProvinceId());
		if (dtoPerformer != null) {
			customerDto.setPerformerId(getUserConfigTagentByProvince(obj.getProvinceId()).getStaffId());
			customerDto.setPerformerSolutionId(getUserConfigTagentByProvince(obj.getProvinceId()).getStaffId());
		} else {
			customerDto.setPerformerId(-1L);
			customerDto.setPerformerSolutionId(-1L);
		}
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//		customerDto.setSuggestTime(dateFormat.parse(obj.getSuggestTimeCustomer()));
		customerDto.setContentCustomer(obj.getContentCustomer());
		customerDto.setStatus("1");
		customerDto.setCreatedDate(new Date());
		customerDto.setCreatedUser(obj.getCreatedUser());
		Long id = this.saveObject(customerDto.toModel());
		if (dtoPerformer != null) {
			String content = "Đ/c được giao nhiệm vụ tiếp xúc khách hàng "
					+ obj.getCustomerName() + " - "
					+ obj.getCustomerPhone() + ", đề nghị triển khai trước ngày "
					+ obj.getSuggestTimeCustomer();
			if(StringUtils.isNotBlank(dtoPerformer.getEmail()) && StringUtils.isNotBlank(dtoPerformer.getPhoneNumber()) && obj.getCreatedUser() != null) {
				insertSendSMS(dtoPerformer.getEmail(), dtoPerformer.getPhoneNumber(),
						obj.getCreatedUser(), content);
				insertSendEmail(dtoPerformer.getEmail(), obj.getCreatedUser(), content);
			}
		}
	}
	
	public Long addTangentCustomer(TangentCustomerRequest obj) {
		try {
			if (!DataUtil.isListNullOrEmpty(obj.getLstTangentCustomer())) {
				for (TangentCustomerDTO dto : obj.getLstTangentCustomer()) {
					functionSaveTangentCustomer(dto);
				}
			} else {
				functionSaveTangentCustomer(obj.getTangentCustomerDTO());
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			this.getSession().clear();
			return 0L;
		}
		return 1L;
	}

	public Long updateTangentCustomerCreate(TangentCustomerRequest request) {
		try {
			StringBuilder sqlTange = new StringBuilder("");
			sqlTange.append(
					"Update TANGENT_CUSTOMER set BIRTH_YEAR=:birthDay,SUGGEST_TIME= to_date(:suggestTime,'dd/MM/yyyy') ,CONTENT_CUSTOMER=:contentCustomer, UPDATED_USER= :sysUserId ,UPDATED_DATE =:updatedDate where TANGENT_CUSTOMER_ID= :tangentCustomerId ");
			SQLQuery queryCar = getSession().createSQLQuery(sqlTange.toString());
			queryCar.setParameter("birthDay", request.getTangentCustomerDTO().getBirthYear());
			queryCar.setParameter("suggestTime", request.getTangentCustomerDTO().getSuggestTimeCustomer());
			queryCar.setParameter("contentCustomer", request.getTangentCustomerDTO().getContentCustomer());
			queryCar.setParameter("sysUserId", request.getSysUserRequest().getSysUserId());
			queryCar.setParameter("updatedDate", new Date());
			queryCar.setParameter("tangentCustomerId", request.getTangentCustomerDTO().getTangentCustomerId());
			queryCar.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			this.getSession().clear();
			return 0L;
		}
		return 1L;
	}

	public TangentCustomerDTO getPerformerId(Long provinceId) {
		String sql = new String(
				"select staff_id performerId,STAFF_PHONE phoneNumber,email from CONFIG_STAFF_TANGENT where type=2 and status=1 and cat_province_id= :provinceId ");
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("provinceId", provinceId);
		query.addScalar("performerId", new LongType());
		query.addScalar("phoneNumber", new StringType());
		query.addScalar("email", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(TangentCustomerDTO.class));
		List<TangentCustomerDTO> lstUser = query.list();
		if (lstUser != null && lstUser.size() > 0) {
			return lstUser.get(0);
		}
		return null;
	}

	public List<TangentCustomerDTO> getDataProvinceCity() {
		StringBuilder sql = new StringBuilder(
				"SELECT AREA_ID areaId, " + "code codeLocation, " + "name nameLocation, " + "PROVINCE_ID provinceId, "
						+ "AREA_LEVEL areaLevel " + "FROM AIO_AREA " + "where AREA_LEVEL=2 order by AREA_ID asc ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("areaId", new LongType());
		query.addScalar("codeLocation", new StringType());
		query.addScalar("nameLocation", new StringType());
		query.addScalar("provinceId", new LongType());
		query.addScalar("areaLevel", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(TangentCustomerDTO.class));
		return query.list();
	}

	public List<TangentCustomerDTO> getDataDistrict(Long id) {
		StringBuilder sql = new StringBuilder("SELECT AREA_ID areaId, " + "code codeLocation, " + "name nameLocation, "
				+ "PROVINCE_ID provinceId, " + "AREA_LEVEL areaLevel " + "FROM AIO_AREA "
				+ "where AREA_LEVEL=3 and PARENT_ID=:id order by AREA_ID asc ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("areaId", new LongType());
		query.addScalar("codeLocation", new StringType());
		query.addScalar("nameLocation", new StringType());
		query.addScalar("provinceId", new LongType());
		query.addScalar("areaLevel", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(TangentCustomerDTO.class));
		query.setParameter("id", id);
		return query.list();
	}

	public List<TangentCustomerDTO> getDataWard(Long id) {
		StringBuilder sql = new StringBuilder("SELECT AREA_ID areaId, " + "code codeLocation, name nameLocation, "
				+ "PROVINCE_ID provinceId, " + "AREA_LEVEL areaLevel " + "FROM AIO_AREA "
				+ "where AREA_LEVEL=4 and PARENT_ID=:id order by AREA_ID asc ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("areaId", new LongType());
		query.addScalar("codeLocation", new StringType());
		query.addScalar("nameLocation", new StringType());
		query.addScalar("provinceId", new LongType());
		query.addScalar("areaLevel", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(TangentCustomerDTO.class));
		query.setParameter("id", id);
		return query.list();
	}

	public Double getContractRose() {
		StringBuilder sql = new StringBuilder(
				" select code from APP_PARAM where par_type= 'ROSE_CONTRACT' and status=1");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("code", new DoubleType());
		return (Double) query.uniqueResult();
	}

	public List<TangentCustomerDTO> getListManager(Long provinceId) {
		String sql = new String(
				"select a.STAFF_NAME,a.STAFF_phone phoneNumber,a.email from CONFIG_STAFF_TANGENT a where type=1 and status=1 and cat_province_id=:provinceId ");
		SQLQuery query = getSession().createSQLQuery(sql);
		query.addScalar("phoneNumber", new StringType());
		query.addScalar("email", new StringType());
		query.setParameter("provinceId", provinceId);
		query.setResultTransformer(Transformers.aliasToBean(TangentCustomerDTO.class));
		return query.list();
	}

	public List<TangentCustomerDTO> getListUserTTHT() {
		String sql = new String(
				"select a.STAFF_NAME,a.STAFF_phone phoneNumber,a.email from CONFIG_STAFF_TANGENT a where type=3 and status=1 ");
		SQLQuery query = getSession().createSQLQuery(sql);
		query.addScalar("phoneNumber", new StringType());
		query.addScalar("email", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(TangentCustomerDTO.class));
		return query.list();
	}

	// hoanm1_20200612_end

	public Long approveRose(ResultSolutionDTO obj, HttpServletRequest request) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE TANGENT_CUSTOMER ");
		sql.append("SET USER_APPROVED_ID = :userApprovedId, APPOVED_DATE = sysdate, STATUS =10 ");
		sql.append("WHERE TANGENT_CUSTOMER_ID = :tangentCustomerId ");

		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.setParameter("userApprovedId", obj.getUserApprovedId());
		query.setParameter("tangentCustomerId", obj.getTangentCustomerId());

		return (long) query.executeUpdate();
	}

	// huypq-24062021-start
	public Long getCrmIdByTangentCustomerId(Long tangentId) {
		String sql = new String(
				"select nvl(CRM_ID,0) crmId FROM TANGENT_CUSTOMER where STATUS!=0 AND TANGENT_CUSTOMER_ID=:tangentId ");
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("tangentId", tangentId);
		return Long.parseLong(String.valueOf(query.uniqueResult()));
	}

	// huy-end
	// DUONGHV13 27122021 start
	public SysUserDTO getTypeUserTangentForSource(TangentCustomerDTO obj) {
		StringBuilder sql = new StringBuilder("");
		sql.append(" SELECT  ");
		sql.append(" 	SU.SYS_USER_ID  sysUserId, ");
		sql.append(" 	SU.TYPE_USER  typeUser, ");
		sql.append(" 	SU.SYS_GROUP_ID  sysGroupId ");
		sql.append(" FROM SYS_USER SU  ");
		sql.append(" where 1 = 1  and SU.STATUS > 0 ");
		
		if(null != obj.getCreatedUser()) {
			sql.append(" AND SU.SYS_USER_ID = :sysUserId  ");
		}
		if (StringUtils.isNotBlank(obj.getUserName())) {
			sql.append(" AND SU.LOGIN_NAME LIKE :loginName  ");
			
			
		}
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("sysUserId", new LongType());
		query.addScalar("typeUser", new StringType());
		query.addScalar("sysGroupId", new LongType());
		query.setResultTransformer(Transformers.aliasToBean(SysUserDTO.class));
		
		if(null != obj.getCreatedUser()) {
			query.setParameter("sysUserId", obj.getCreatedUser());
		}
		if (StringUtils.isNotBlank(obj.getUserName())) {
			
			query.setParameter("loginName","%" + obj.getUserName() + "%");
		}
		return (SysUserDTO) query.uniqueResult();
	}
	// DUONGHV13 27122021 end

	public List<SysGroupDTO> getListGroupTangent(Long sysGroupId) {
		StringBuilder sql = new StringBuilder("");
		sql.append(" SELECT  ");
		sql.append(" SG.SYS_GROUP_ID  sysGroupId ");
		sql.append(" FROM SYS_GROUP SG ");
		sql.append(" where 1 = 1 AND SG.PARENT_ID = :sysGroupId ");

		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("sysGroupId", new LongType());
		query.setParameter("sysGroupId", sysGroupId);
		query.setResultTransformer(Transformers.aliasToBean(SysGroupDTO.class));
		return query.list();
	}
	// DUONGHV13 27122021 end

	public List<AppParamDTO> getChannel(AppParamDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder("SELECT APP_PARAM_ID appParamId," + "CODE code," + "NAME name,"
				+ "PAR_TYPE parType," + "STATUS status " + " FROM APP_PARAM WHERE 1=1 ");

		if (null != obj.getLstParType() && obj.getLstParType().size() > 0) {
			sql.append(" AND PAR_TYPE  IN (:lstParType) ");
		}
		
		sql.append(" ORDER BY APP_PARAM_ID ASC ");

		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		query.addScalar("appParamId", new LongType());
		query.addScalar("code", new StringType());
		query.addScalar("name", new StringType());
		query.addScalar("parType", new StringType());
		query.addScalar("status", new StringType());

		query.setResultTransformer(Transformers.aliasToBean(AppParamDTO.class));

		if (null != obj.getLstParType() && obj.getLstParType().size() > 0) {
			query.setParameterList("lstParType", obj.getLstParType());
			queryCount.setParameterList("lstParType",obj.getLstParType());
		}

		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		return query.list();
	}
	
	public CatPartnerDTO getPartnerTTHT(TangentCustomerDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder("SELECT T1.CODE code," + "T1.NAME name," + "T1.PHONE phone, " + " T1.TANGENT_CUSTOMER_ID tangentCustomerId "
				+ " FROM CTCT_CAT_OWNER.CAT_PARTNER_TTHT T1 WHERE 1=1 AND T1.STATUS = 1 ");

		if (null  != obj.getTangentCustomerId()) {
			sql.append(" AND T1.TANGENT_CUSTOMER_ID  = :tangentCustomerId ");
		}
		if (StringUtils.isNotBlank(obj.getCustomerPhone())) {
			sql.append(" AND  upper(T1.PHONE) like upper(:customerPhone) escape '&' ");
		}

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("code", new StringType());
		query.addScalar("name", new StringType());
		query.addScalar("phone", new StringType());
		query.addScalar("tangentCustomerId", new LongType());
		query.setResultTransformer(Transformers.aliasToBean(CatPartnerDTO.class));
		if ( null != obj.getTangentCustomerId()) {
			query.setParameter("tangentCustomerId", obj.getTangentCustomerId());
			
		}
		if (StringUtils.isNotBlank(obj.getCustomerPhone())) {
			query.setParameter("customerPhone","%"+obj.getCustomerPhone().toString()+"%");
			
		}
		System.out.println(query.toString());
		return (CatPartnerDTO) query.uniqueResult();

	}

	public Long updatePartnerTTHT(CatPartnerDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder(
				"UPDATE CTCT_CAT_OWNER.CAT_PARTNER_TTHT SET UPDATED_DATE = :updatedDate,UPDATED_USER_ID = :updatedUserId ");
		if(obj.getCode()!=null) {
			sql.append(", CODE = :code  ");
		}
		if(obj.getPhone()!=null) {
			sql.append(", PHONE = :phone   ");
		}
		if(obj.getStatus()!=null) {
			sql.append(", STATUS = :status   ");
		}
		sql.append("  WHERE TANGENT_CUSTOMER_ID  = :tangentCustomerId  ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		if(obj.getCode()!=null) {
			query.setParameter("code", obj.getCode());
		}
		if(obj.getPhone()!=null) {
			query.setParameter("phone", obj.getPhone());
		}
		if(obj.getStatus()!=null) {
			query.setParameter("status", obj.getStatus());
		}
		
		query.setParameter("updatedUserId", obj.getUpdatedUserId());
		query.setParameter("updatedDate", obj.getUpdatedDate());
		query.setParameter("tangentCustomerId", obj.getTangentCustomerId());
		return (long)query.executeUpdate();
	}
	
	public List<TangentCustomerDTO> getDataTangentFollowPhoneNumber(String phone) {
		StringBuilder sql = new StringBuilder("SELECT TANGENT_CUSTOMER_ID tangentCustomerId,  "
				+ "PROVINCE_ID provinceId FROM TANGENT_CUSTOMER  "
				+ "where upper(CUSTOMER_PHONE) like upper(:customerPhone) escape '&' ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("tangentCustomerId", new LongType());
		query.addScalar("provinceId", new LongType());
		query.setResultTransformer(Transformers.aliasToBean(TangentCustomerDTO.class));
		query.setParameter("customerPhone","%"+phone+"%");
		return query.list();
	}
	
	//Huypq-21032022-start
	public ConfigStaffTangentDTO getUserConfigTagentByProvince(Long catProvinceId) {
		StringBuilder sql = new StringBuilder(
				"select SYS_USER_ID staffId, FULL_NAME staffName, PHONE_NUMBER staffPhone, EMAIL email " + 
				"from SYS_USER  " + 
				"where STATUS=1 " + 
//				"AND EMAIL is not null " + 
				"AND (UPPER(TRIM(position_name)) = UPPER('Nhân viên kinh doanh xây dựng dân dụng') "
				+ " OR UPPER(TRIM(position_name)) = UPPER('Nhân viên Xây dựng')) " + 
				"AND SYS_GROUP_ID in (select SYS_GROUP_ID from SYS_GROUP where PROVINCE_ID=:catProvinceId)  " + 
				"order by LOGIN_NAME ASC");
		StringBuilder sql2 = new StringBuilder(
				"select SYS_USER_ID staffId, FULL_NAME staffName, PHONE_NUMBER staffPhone, EMAIL email  " + 
				"from SYS_USER  " + 
				"where status!=0  " + 
				"AND (UPPER(TRIM(position_name)) = UPPER('Phó Giám đốc Hạ tầng') "
				+ " OR UPPER(TRIM(position_name)) = UPPER('Phó Giám đốc Xây dựng')) " + 
				"AND SYS_GROUP_ID in (select SYS_GROUP_ID from SYS_GROUP where PROVINCE_ID=:catProvinceId)  " + 
				"order by LOGIN_NAME ASC");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery query2 = getSession().createSQLQuery(sql2.toString());

		query.addScalar("staffId", new LongType());
		query.addScalar("staffName", new StringType());
		query.addScalar("staffPhone", new StringType());
		query.addScalar("email", new StringType());
		
		query2.addScalar("staffId", new LongType());
		query2.addScalar("staffName", new StringType());
		query2.addScalar("staffPhone", new StringType());
		query2.addScalar("email", new StringType());

		query.setParameter("catProvinceId", catProvinceId);
		query2.setParameter("catProvinceId", catProvinceId);

		query.setResultTransformer(Transformers.aliasToBean(ConfigStaffTangentDTO.class));
		query2.setResultTransformer(Transformers.aliasToBean(ConfigStaffTangentDTO.class));

		List<ConfigStaffTangentDTO> ls = query.list();
		if (ls.size() > 0) {
			return ls.get(0);
		} else {
			List<ConfigStaffTangentDTO> pgd = query2.list();
			return pgd.get(0);
		}
	}
	
	public Boolean checkRoleUserAssignYctx(Long userId) {
		StringBuilder sql = new StringBuilder(
				"select SYS_USER_ID sysUserId  " + 
				"from SYS_USER  " + 
				"where SYS_USER_ID=:userId  " + 
				"AND (UPPER(TRIM(position_name)) = UPPER('Phó Giám đốc Hạ tầng') " + 
				"                 OR UPPER(TRIM(position_name)) = UPPER('Trưởng phòng Kinh doanh'))");

		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("sysUserId", new LongType());
		
		query.setParameter("userId", userId);

		List<Long> ls = query.list();
		if (ls.size() > 0) {
			return true;
		}
		return false;
	}
	//Huy-end
}
