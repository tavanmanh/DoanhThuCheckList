package com.viettel.coms.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.viettel.cat.dto.CatProvinceDTO;
import com.viettel.coms.bo.ComplainOrderRequestBO;
import com.viettel.coms.dto.AssignHandoverDTO;
import com.viettel.coms.dto.CertificateExtendDTO;
import com.viettel.coms.dto.ComplainOrderRequestDTO;
import com.viettel.coms.dto.ConfigStaffTangentDTO;
import com.viettel.coms.dto.SysUserCOMSDTO;
import com.viettel.coms.dto.WoCatWorkItemTypeDTO;
import com.viettel.erp.dto.SysUserDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@Transactional
@Repository("complainOrderRequestDAO")
public class ComplainOrderRequestDAO extends BaseFWDAOImpl<ComplainOrderRequestBO, Long> {

	public ComplainOrderRequestDAO() {
		this.model = new ComplainOrderRequestBO();
	}

	public ComplainOrderRequestDAO(Session session) {
		this.session = session;
	}

	@SuppressWarnings("unchecked")
	public List<ComplainOrderRequestDTO> doSearch(ComplainOrderRequestDTO criteria) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cal = Calendar.getInstance();
		StringBuilder stringBuilder = new StringBuilder("  with tbl as ( "
				+ "SELECT T1.COMPLAIN_ORDER_REQUEST_ID complainOrderRequestId," + "T1.TICKET_CODE ticketCode,"
				+ "T1.CUSTOMER_NAME customerName," + " T1.PROVINCE_ID provinceId," + "T1.PROVINCE_CODE provinceCode,"
				+ "T1.PROVINCE_NAME provinceName," + " T1.DISTRICT_NAME districtName," + "T1.WARDS_NAME wardsName,"
				+ " T1.CUSTOMER_ADDRESS customerAddress," + "T1.CUSTOMER_PHONE customerPhone," + "T1.STATUS status,"
				+ " T1.CREATE_DATE createDate," + " to_char(T1.CREATE_DATE,'dd/MM/yyyy HH24:Mi:SS') createDateString, "
				+ " T1.CREATE_USER createUser,"

				+ " T3.FULL_NAME createUserName,"

				+ " T1.RECEIVED_DATE receivedDate, to_char(T1.RECEIVED_DATE,'dd/MM/yyyy HH24:Mi:SS') receivedDateString,"

				+ " (sysdate - T1.CREATE_DATE) realTimeDate, "

				+ " (T1.COMPLETE_TIME_EXPECTED - T1.CREATE_DATE) workTimeDate, "

				+ " (T1.COMPLETE_TIME_REAL -  T1.CREATE_DATE) realCompletedTimeDate, "

				+ " T1.COMPLETE_TIME_EXPECTED completedTimeExpected, to_char(T1.COMPLETE_TIME_EXPECTED,'dd/MM/yyyy HH24:Mi:SS') completedTimeExpectedString,"
				+ " T1.COMPLETE_TIME_REAL completedTimeReal, to_char(T1.COMPLETE_TIME_REAL,'dd/MM/yyyy HH24:Mi:SS') completedTimeRealString, "
				+ " T1.SERVICE service," + "T1.COMPLAIN_GROUP complainGroup," + " T1.TITLE title,"
				+ " T1.PERFORM_NAME performerName," + " T2.FULL_NAME performerfullName, "
				+ " (T2.EMPLOYEE_CODE || ' - ' ||T2.FULL_NAME || ' - ' || T2.PHONE_NUMBER) performerShow "
				+ " FROM CTCT_COMS_OWNER.COMPLAIN_ORDER_REQUEST T1  "
				+ " LEFT JOIN SYS_USER T2 ON T1.PERFORM_NAME = T2.EMPLOYEE_CODE "
				+ " LEFT JOIN SYS_USER T3 ON T1.CREATE_USER = T3.EMPLOYEE_CODE )");

		stringBuilder.append(" select ").append("complainOrderRequestId complainOrderRequestId, ")
				.append(" ticketCode ticketCode, ")

				.append(" provinceId provinceId, ").append(" provinceCode provinceCode, ")
				.append(" provinceName provinceName, ")

				.append(" districtName districtName, ").append(" wardsName wardsName, ")

				.append(" customerName customerName, ").append(" customerAddress customerAddress, ")

				.append(" customerPhone customerPhone, ").append(" status status, ").append(" createDate createDate, ")
				.append(" createUser createUser, ").append(" createUserName createUserName, ")
				.append(" receivedDate receivedDate, ").append(" receivedDateString receivedDateString, ")
				.append(" createDateString createDateString,").append(" realTimeDate realTimeDate, ")
				.append(" workTimeDate workTimeDate, ").append(" realCompletedTimeDate realCompletedTimeDate, ")
				.append(" completedTimeExpectedString completedTimeExpectedString, ")
				.append(" completedTimeRealString completedTimeRealString, ")
				.append(" completedTimeExpected completedTimeExpected, ")
				.append(" completedTimeReal completedTimeReal, ").append(" service service, ").append(" title title, ")
				.append(" complainGroup complainGroup, ").append(" performerfullName performerfullName,  ")
				.append(" performerName performerName,  ").append(" performerShow performerShow,  ").append(" ( CASE  ")
				.append("        WHEN tbl.status NOT IN (3,4) THEN (            CASE           ")
				.append("            WHEN tbl.realTimeDate  BETWEEN 0  and  (0.7  * tbl.workTimeDate)            THEN 1  ")
				.append("            WHEN tbl.realTimeDate  BETWEEN (0.7 * tbl.workTimeDate)  and  tbl.workTimeDate            THEN 2  ")
				.append("           WHEN             SYSDATE  > to_date(to_char(tbl.completedTimeExpected, ")
				.append("           'dd/MM/yyyy HH24:Mi:SS'), ")
				.append("           'dd/mm/yyyy hh24:mi:ss')            THEN 3   ").append("            ELSE null   ")
				.append("        END      )    ").append("        WHEN tbl.status IN (3) THEN (             CASE    ")
				.append("           WHEN tbl.realCompletedTimeDate  BETWEEN 0  and  (0.7  * tbl.workTimeDate)            THEN 1   ")
				.append("           WHEN tbl.realCompletedTimeDate  BETWEEN (0.7 * tbl.workTimeDate)  and  tbl.workTimeDate            THEN 2    ")
				.append("           WHEN             tbl.completedTimeReal  > to_date(to_char(tbl.completedTimeExpected, ")
				.append("            'dd/MM/yyyy HH24:Mi:SS'), ")
				.append("            'dd/mm/yyyy hh24:mi:ss')            THEN 3     ")
				.append("            ELSE null      ").append("        END      )   ")

				.append("        WHEN tbl.status IN (4) THEN (             CASE    ")

				.append(" 		 WHEN 				tbl.completedTimeReal< to_date(to_char(tbl.completedTimeExpected, ")
				.append(" 			  'dd/MM/yyyy HH24:Mi:SS'), ")
				.append(" 			  'dd/mm/yyyy hh24:mi:ss') 			 THEN 1   ")
				.append("           WHEN             tbl.completedTimeReal  > to_date(to_char(tbl.completedTimeExpected, ")
				.append("            'dd/MM/yyyy HH24:Mi:SS'), ")
				.append("            'dd/mm/yyyy hh24:mi:ss')            THEN 3     ")
				.append("            ELSE null      ").append("        END      )   ")

				.append("        ELSE null     ").append("    END )   process ")

				.append("  FROM tbl ").append(" WHERE 1=1 ");
		if (null != criteria.getComplainOrderRequestId()) {
			stringBuilder.append("AND complainOrderRequestId = :complainOrderRequestId ");
		}

		if (StringUtils.isNotBlank(criteria.getKeySearch())) {
			stringBuilder.append("AND ( upper(customerName) like upper(:keySearch) escape '&' OR "
					+ " customerPhone like :keySearch escape '&' OR upper(customerAddress) like upper(:keySearch) escape '&' "
					+ " ) ");

		}

		if (criteria.getCreateDateFrom() != null) {
			stringBuilder.append(" AND createDate >= :createDateFrom ");
		}

		if (criteria.getCreateDateTo() != null) {
			cal.setTime(criteria.getCreateDateTo());
			cal.add(Calendar.DATE, 1);
			stringBuilder.append(" AND createDate <= :createDateTo ");
		}

		if (criteria.getCheckRoleTTHTView() == 0l) {
			if (criteria.getCheckRoleViewData() == 1l) {
				if (null != criteria.getProvinceViewData() && criteria.getProvinceViewData().size() > 0) {
					stringBuilder.append("And provinceId IN (:provinceViewData) ");
				}
			}
			// if ( criteria.getCheckRoleDeployTicket() == 1l) {
			// if (null != criteria.getProvinceViewDeploy() &&
			// criteria.getProvinceViewDeploy().size() > 0) {
			// stringBuilder.append("And provinceId IN (select PROVINCE_ID FROM SYS_GROUP
			// WHERE SYS_GROUP_ID IN (:provinceViewDeploy) ) ");
			// }
			// }
			else if (criteria.getCheckRoleCSKH() == 1l) {
				stringBuilder.append("And createUser like :createUser ");
			} else
				stringBuilder.append("And performerName like :performerName ");
		}
		if (criteria.getPerformerUser() != null) {

			stringBuilder.append("And performerName like :performerUser ");
		}

		if (criteria.getStatus() != null && criteria.getStatus() < 5) {
			stringBuilder.append("And status = :status ");
		}

		if (StringUtils.isNotBlank(criteria.getProcess())) {
			stringBuilder.append("And process = :process ");
		}

		stringBuilder.append("ORDER BY createDate DESC ");

		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(stringBuilder.toString());
		sqlCount.append(")");

		SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		query.addScalar("complainOrderRequestId", new LongType());
		query.addScalar("ticketCode", new StringType());
		query.addScalar("provinceId", new LongType());
		query.addScalar("provinceCode", new StringType());
		query.addScalar("provinceName", new StringType());
		query.addScalar("districtName", new StringType());
		query.addScalar("wardsName", new StringType());
		query.addScalar("customerName", new StringType());
		query.addScalar("customerAddress", new StringType());
		query.addScalar("customerPhone", new StringType());
		query.addScalar("complainGroup", new StringType());
		query.addScalar("service", new StringType());
		query.addScalar("title", new StringType());
		query.addScalar("performerName", new StringType());
		query.addScalar("performerfullName", new StringType());
		query.addScalar("performerShow", new StringType());
		query.addScalar("status", new LongType());

		query.addScalar("receivedDate", new DateType());
		query.addScalar("completedTimeExpected", new DateType());
		query.addScalar("completedTimeReal", new DateType());

		query.addScalar("realTimeDate", new DoubleType());
		query.addScalar("workTimeDate", new DoubleType());
		query.addScalar("realCompletedTimeDate", new DoubleType());
		query.addScalar("createDate", new DateType());
		query.addScalar("createUser", new StringType());
		query.addScalar("createUserName", new StringType());
		query.addScalar("process", new StringType());
		query.addScalar("createDateString", new StringType());
		query.addScalar("receivedDateString", new StringType());
		query.addScalar("completedTimeExpectedString", new StringType());
		query.addScalar("completedTimeRealString", new StringType());

		if (null != criteria.getComplainOrderRequestId()) {
			query.setParameter("complainOrderRequestId", criteria.getComplainOrderRequestId());
			queryCount.setParameter("complainOrderRequestId", criteria.getComplainOrderRequestId());
		}

		if (StringUtils.isNotBlank(criteria.getKeySearch())) {
			query.setParameter("keySearch", "%" + criteria.getKeySearch() + "%");
			queryCount.setParameter("keySearch", "%" + criteria.getKeySearch() + "%");
		}

		if (criteria.getCreateDateFrom() != null) {
			query.setParameter("createDateFrom", criteria.getCreateDateFrom());
			queryCount.setParameter("createDateFrom", criteria.getCreateDateFrom());
		}

		if (criteria.getCreateDateTo() != null) {
			query.setParameter("createDateTo", cal.getTime());
			queryCount.setParameter("createDateTo", cal.getTime());
		}

		if (criteria.getCheckRoleTTHTView() == 0l) {
			if (criteria.getCheckRoleViewData() == 1l) {
				if (null != criteria.getProvinceViewData() && criteria.getProvinceViewData().size() > 0) {
					query.setParameterList("provinceViewData", criteria.getProvinceViewData());
					queryCount.setParameterList("provinceViewData", criteria.getProvinceViewData());

				}

			}
			// if ( criteria.getCheckRoleDeployTicket() == 1l) {
			// if (null != criteria.getProvinceViewDeploy() &&
			// criteria.getProvinceViewDeploy().size() > 0) {
			// query.setParameter("provinceViewDeploy", "%" +
			// criteria.getProvinceViewDeploy() + "%");
			// queryCount.setParameter("provinceViewDeploy", "%" +
			// criteria.getProvinceViewDeploy() + "%");
			// }
			// }
			else if (criteria.getCheckRoleCSKH() == 1l) {
				query.setParameter("createUser", "%" + criteria.getCreateUser() + "%");
				queryCount.setParameter("createUser", "%" + criteria.getCreateUser() + "%");
			} else {
				query.setParameter("performerName", "%" + criteria.getPerformerName() + "%");
				queryCount.setParameter("performerName", "%" + criteria.getPerformerName() + "%");
			}
		}
		if (criteria.getPerformerUser() != null) {
			query.setParameter("performerUser", "%" + criteria.getPerformerUser() + "%");
			queryCount.setParameter("performerUser", "%" + criteria.getPerformerUser() + "%");
		}

		if (criteria.getStatus() != null && criteria.getStatus() < 5) {
			query.setParameter("status", criteria.getStatus());
			queryCount.setParameter("status", criteria.getStatus());
		}
		if (StringUtils.isNotBlank(criteria.getProcess())) {
			query.setParameter("process", criteria.getProcess());
			queryCount.setParameter("process", criteria.getProcess());
		}

		query.setResultTransformer(Transformers.aliasToBean(ComplainOrderRequestDTO.class));
		if (criteria.getPage() != null && criteria.getPageSize() != null) {
			query.setFirstResult((criteria.getPage().intValue() - 1) * criteria.getPageSize().intValue());
			query.setMaxResults(criteria.getPageSize().intValue());
		}

		List ls = query.list();
		criteria.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
		return ls;
	}

	public Long updateComplainOrderRequest(ComplainOrderRequestDTO complainOrderRequestDTO) {
		StringBuilder sql = new StringBuilder(
				" update CTCT_COMS_OWNER.COMPLAIN_ORDER_REQUEST set STATUS= :status ,UPDATE_DATE = :updateDate,"
						+ " PROVINCE_ID = :provinceId, PROVINCE_CODE = :provinceCode, PROVINCE_NAME = :provinceName,"
						+ " DISTRICT_NAME = :districtName, WARDS_NAME = :wardsName,"
						+ " CUSTOMER_ADDRESS = :address, CUSTOMER_PHONE = :phone, CUSTOMER_NAME = :customerName,  "
						+ " TITLE = :title,COMPLAIN_GROUP = :complainGroup,UPDATE_USER = :updateUser,ACTION_LAST = :actionLast ");
		if (complainOrderRequestDTO.getStatus() == 1l) {

			if (complainOrderRequestDTO.getCompletedTimeExpected() != null) {
				sql.append(" ,COMPLETE_TIME_EXPECTED = :completedTimeExpected ");
			}
		}else if (complainOrderRequestDTO.getStatus() == 2l) {
			if (complainOrderRequestDTO.getReceivedDate() != null) {
				sql.append(" ,RECEIVED_DATE = :receiveDate ");
			}
			if (complainOrderRequestDTO.getCompletedTimeExpected() != null) {
				sql.append(" ,COMPLETE_TIME_EXPECTED = :completedTimeExpected ");
			}
			sql.append(" ,COMPLETE_TIME_REAL = NULL ");

		} else if (complainOrderRequestDTO.getStatus() == 3l) {
			if (complainOrderRequestDTO.getCompletedTimeReal() != null) {
				sql.append(" ,COMPLETE_TIME_REAL = :completedTimeReal ");
			}
		}
		if (null != complainOrderRequestDTO.getPerformerName()) {
			sql.append(" ,PERFORM_NAME = :performerName ");
		}
		
		if (null != complainOrderRequestDTO.getIsTrace()) {
			sql.append(" ,IS_TRACE = :isTrace ");
		}

		sql.append(" where COMPLAIN_ORDER_REQUEST_ID = :complainOrderRequestId ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.setParameter("status", complainOrderRequestDTO.getStatus());
		query.setParameter("updateDate", complainOrderRequestDTO.getUpdateDate());

		query.setParameter("provinceId", complainOrderRequestDTO.getProvinceId());
		query.setParameter("provinceCode", complainOrderRequestDTO.getProvinceCode());
		query.setParameter("provinceName", complainOrderRequestDTO.getProvinceName());
		query.setParameter("districtName", complainOrderRequestDTO.getDistrictName());
		query.setParameter("wardsName", complainOrderRequestDTO.getWardsName());
		query.setParameter("address", complainOrderRequestDTO.getCustomerAddress());
		query.setParameter("phone", complainOrderRequestDTO.getCustomerPhone());
		query.setParameter("customerName", complainOrderRequestDTO.getCustomerName());
		query.setParameter("title", complainOrderRequestDTO.getTitle());
		query.setParameter("complainGroup", complainOrderRequestDTO.getComplainGroup());
		query.setParameter("updateUser", complainOrderRequestDTO.getUpdateUser());
		query.setParameter("actionLast", complainOrderRequestDTO.getActionLast());
		if (complainOrderRequestDTO.getStatus() == 1l) {
			if (complainOrderRequestDTO.getCompletedTimeExpected() != null) {
				query.setParameter("completedTimeExpected", complainOrderRequestDTO.getCompletedTimeExpected());
			}

		}else if (complainOrderRequestDTO.getStatus() == 2l) {
			if (complainOrderRequestDTO.getReceivedDate() != null) {
				query.setParameter("receiveDate", complainOrderRequestDTO.getReceivedDate());
			}
			if (complainOrderRequestDTO.getCompletedTimeExpected() != null) {
				query.setParameter("completedTimeExpected", complainOrderRequestDTO.getCompletedTimeExpected());
			}

		} else if (complainOrderRequestDTO.getStatus() == 3l) {
			if (complainOrderRequestDTO.getCompletedTimeReal() != null) {
				query.setParameter("completedTimeReal", complainOrderRequestDTO.getCompletedTimeReal());
			}
		}
		if (null != complainOrderRequestDTO.getPerformerName()) {
			query.setParameter("performerName", complainOrderRequestDTO.getPerformerName());
		}
	
		if (null != complainOrderRequestDTO.getIsTrace()) {
			query.setParameter("isTrace", complainOrderRequestDTO.getIsTrace());
		}

		query.setParameter("complainOrderRequestId", complainOrderRequestDTO.getComplainOrderRequestId());
		return (long) query.executeUpdate();

	}

	public Long saveComplainOrderRequest(ComplainOrderRequestDTO complainOrderRequestDTO) {
		String sql = "INSERT INTO CTCT_COMS_OWNER.COMPLAIN_ORDER_REQUEST " + "(COMPLAIN_ORDER_REQUEST_ID, TICKET_CODE ,"
				+ "PROVINCE_ID, PROVINCE_CODE,PROVINCE_NAME, " + "DISTRICT_NAME,WARDS_NAME, "
				+ "CUSTOMER_NAME, CUSTOMER_ADDRESS, CUSTOMER_PHONE, CREATE_DATE, CREATE_USER, "
				+ "COMPLETE_TIME_EXPECTED, SERVICE,COMPLAIN_GROUP, TITLE,PERFORM_NAME, STATUS,ACTION_LAST,IS_TRACE,LAST_STATE_TRACE) "
				+ "VALUES " + "(:complainOrderRequestId, :ticketCode ," + " :provinceId, :provinceCode, :provinceName,"
				+ " :districtName, :wardsName," + " :customerName, :address, :phone, :createDate, :createUser,"
				+ " :completedTimeExpected, :service, :complainGroup, :title,:performerName, :status,:actionLast,:isTrace,1)";

		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("complainOrderRequestId", complainOrderRequestDTO.getComplainOrderRequestId());
		query.setParameter("ticketCode", complainOrderRequestDTO.getTicketCode());
		query.setParameter("provinceId", complainOrderRequestDTO.getProvinceId());
		query.setParameter("provinceCode", complainOrderRequestDTO.getProvinceCode());
		query.setParameter("provinceName", complainOrderRequestDTO.getProvinceName());
		query.setParameter("districtName", complainOrderRequestDTO.getDistrictName());
		query.setParameter("wardsName", complainOrderRequestDTO.getWardsName());
		query.setParameter("customerName", complainOrderRequestDTO.getCustomerName());
		query.setParameter("address", complainOrderRequestDTO.getCustomerAddress());
		query.setParameter("phone", complainOrderRequestDTO.getCustomerPhone());

		query.setParameter("createDate", complainOrderRequestDTO.getCreateDate());
		query.setParameter("createUser", complainOrderRequestDTO.getCreateUser());
		query.setParameter("completedTimeExpected", complainOrderRequestDTO.getCompletedTimeExpected());
		query.setParameter("service", complainOrderRequestDTO.getService());
		query.setParameter("title", complainOrderRequestDTO.getTitle());
		query.setParameter("complainGroup", complainOrderRequestDTO.getComplainGroup());
		query.setParameter("performerName", complainOrderRequestDTO.getPerformerName());
		query.setParameter("status", complainOrderRequestDTO.getStatus());
		query.setParameter("actionLast", complainOrderRequestDTO.getActionLast());
		query.setParameter("isTrace", complainOrderRequestDTO.getIsTrace());
		return (long) query.executeUpdate();
	}

	public List<SysUserCOMSDTO> getListPerformer(SysUserCOMSDTO sysUser) {
		// TODO Auto-generated method stub
		StringBuilder stringBuilder = new StringBuilder(" select ");

		stringBuilder.append("SU.sys_user_id sysUserId, ").append("SU.login_name loginName, ")
				.append("SU.full_name fullName, ").append("SU.employee_code employeeCode, ").append("SU.email email, ")
				.append("SU.phone_number phoneNumber, ").append("SU.sys_group_id sysGroupId ")
				.append("  FROM SYS_USER SU ").append(" WHERE 1=1 ")
				.append(" AND SU.status !=0 and SU.type_user IS NULL");

		if (null != sysUser.getSysGroupId()) {
			stringBuilder.append(
					" AND SU.sys_group_id in ( select a.sys_group_id from sys_group a where a.sys_group_id in ( ")
					.append(" (case when a.group_level=4 then (select sys_group_id from sys_group b where b.sys_group_id= ")
					.append(" (select parent_id from sys_group c where c.sys_group_id=:sysGroupId)) ")
					.append(" when a.group_level=3 then (select sys_group_id from sys_group b where b.sys_group_id=:sysGroupId) ")
					.append(" else :sysGroupId end ) ) ) ");

		}

		if (StringUtils.isNotBlank(sysUser.getKeySearch())) {
			stringBuilder.append(" AND Upper(SU.login_name) like Upper(:keySearch) escape '&' OR "
					+ "Upper(SU.full_name) like Upper(:keySearch) escape '&' OR Upper(SU.employee_code) like Upper(:keySearch) escape '&' ");

		}

		stringBuilder.append(" ORDER BY SU.full_name ASC ");

		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(stringBuilder.toString());
		sqlCount.append(")");

		SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		query.addScalar("sysUserId", new LongType());
		query.addScalar("loginName", new StringType());
		query.addScalar("fullName", new StringType());
		query.addScalar("employeeCode", new StringType());

		query.addScalar("email", new StringType());
		query.addScalar("phoneNumber", new StringType());

		query.addScalar("sysGroupId", new LongType());
		if (null != sysUser.getSysGroupId()) {
			query.setParameter("sysGroupId", sysUser.getSysGroupId());
			queryCount.setParameter("sysGroupId", sysUser.getSysGroupId());
		}

		if (StringUtils.isNotBlank(sysUser.getKeySearch())) {
			query.setParameter("keySearch", "%" + sysUser.getKeySearch() + "%");
			queryCount.setParameter("keySearch", "%" + sysUser.getKeySearch() + "%");
		}

		query.setResultTransformer(Transformers.aliasToBean(SysUserCOMSDTO.class));
		if (sysUser.getPage() != null && sysUser.getPageSize() != null) {
			query.setFirstResult((sysUser.getPage().intValue() - 1) * sysUser.getPageSize().intValue());
			query.setMaxResults(sysUser.getPageSize().intValue());
		}
		List ls = query.list();
		sysUser.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
		return ls;
	}

	public Long choosePerformer(ComplainOrderRequestDTO criteria) {
		StringBuilder sql = new StringBuilder(
				"UPDATE CTCT_COMS_OWNER.COMPLAIN_ORDER_REQUEST SET PERFORM_NAME = :performerId ,"
						+ " UPDATE_USER = :updateUser , UPDATE_DATE = :updateDate,ACTION_LAST = :actionLast,IS_TRACE = :isTrace "
						+ "WHERE COMPLAIN_ORDER_REQUEST_ID IN ( :listId ) ");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("performerId", criteria.getPerformerName());
		query.setParameter("updateUser", criteria.getUpdateUser());
		query.setParameter("updateDate", criteria.getUpdateDate());
		query.setParameter("actionLast", criteria.getActionLast());
		query.setParameter("isTrace", criteria.getIsTrace());
		query.setParameterList("listId", criteria.getListId());
		return (long) query.executeUpdate();
	}

	public ComplainOrderRequestDTO getById(Long id) {
		StringBuilder sql = new StringBuilder(
				"select COMPLAIN_ORDER_REQUEST_ID as complainOrderRequestId, TICKET_CODE as ticketCode, STATUS as status "
						+ " from COMPLAIN_ORDER_REQUEST where COMPLAIN_ORDER_REQUEST_ID = :id ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("id", id);

		query.addScalar("complainOrderRequestId", new LongType());
		query.addScalar("ticketCode", new StringType());
		query.addScalar("status", new LongType());
		query.setResultTransformer(Transformers.aliasToBean(ComplainOrderRequestDTO.class));
		return (ComplainOrderRequestDTO) query.uniqueResult();
	}

	public CatProvinceDTO findProvinceByName(String provinceName) {
		StringBuilder stringBuilder = new StringBuilder("Select T1.CAT_PROVINCE_ID catProvinceId," + "T1.CODE code,"
				+ "T1.NAME name," + "T1.STATUS status " + " FROM CTCT_CAT_OWNER.CAT_PROVINCE T1 "
				+ " LEFT JOIN  CTCT_KCS_OWNER.AIO_AREA T2 ON T1.CAT_PROVINCE_ID = T2.PROVINCE_ID "
				+ " WHERE 1=1  AND upper(T2.NAME) LIKE upper(:provinceName) AND T1.STATUS = 1 ");

		SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());

		query.addScalar("catProvinceId", new LongType());
		query.addScalar("code", new StringType());
		query.addScalar("name", new StringType());
		query.addScalar("status", new StringType());

		query.setParameter("provinceName", provinceName);
		query.setResultTransformer(Transformers.aliasToBean(CatProvinceDTO.class));

		return (CatProvinceDTO) query.uniqueResult();
	}

	public List<SysUserDTO> getListUserDeploy(ComplainOrderRequestDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder stringBuilder = new StringBuilder(
				"SELECT DISTINCT SU.SYS_USER_ID sysUserId  " + " FROM CTCT_VPS_OWNER.SYS_USER SU " + " WHERE "
						+ " SU.STATUS = 1 AND SU.POSITION_NAME like '%Phó Giám đốc Hạ tầng%' " + " AND "
						+ " SU.SYS_GROUP_ID IN (" + "  :sysGroupId  " + " ) ");

		SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());

		query.addScalar("sysUserId", new LongType());
		query.setParameter("sysGroupId", obj.getSysGroupId());
		query.setResultTransformer(Transformers.aliasToBean(SysUserDTO.class));

		return query.list();
	}

	public SysUserDTO getUserPGDHT(ComplainOrderRequestDTO obj) {
		// TODO Auto-generated method stub
		StringBuilder stringBuilder = new StringBuilder(
				"SELECT  SU.SYS_USER_ID sysUserId,SU.EMPLOYEE_CODE employeeCode " + ",SU.FULL_NAME fullName "
						+ " FROM CTCT_VPS_OWNER.SYS_USER SU " + " WHERE "
						+ " SU.STATUS = 1 AND UPPER(SU.POSITION_NAME) like UPPER('Phó Giám đốc Hạ tầng') " + " AND "
						+ " SU.SYS_GROUP_ID in (select SYS_GROUP_ID from CTCT_CAT_OWNER.SYS_GROUP where PROVINCE_ID=:provinceId) AND SU.EMAIL is not null  ");

		SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());

		query.addScalar("sysUserId", new LongType());
		query.addScalar("employeeCode", new StringType());
		query.addScalar("fullName", new StringType());
		query.setParameter("provinceId", obj.getProvinceId());
		query.setResultTransformer(Transformers.aliasToBean(SysUserDTO.class));
		List<SysUserDTO> result = query.list();
		if (result.size() > 0)
			return result.get(0);
		else
			result = this.getUserConfigTagentByProvince(obj.getProvinceId());
		if (result.size() > 0)
			return result.get(0);
		else
			return null;
	}

	public List<SysUserDTO> getUserConfigTagentByProvince(Long catProvinceId) {
		StringBuilder sql = new StringBuilder(
				"select SYS_USER_ID sysUserId, FULL_NAME fullName, EMPLOYEE_CODE employeeCode, EMAIL email "
						+ "from SYS_USER  " + "where STATUS=1 " +
						// "AND EMAIL is not null " +
						"AND UPPER(TRIM(position_name)) = UPPER('Nhân viên kinh doanh xây dựng dân dụng') "
						+ "AND SYS_GROUP_ID in (select SYS_GROUP_ID from SYS_GROUP where PROVINCE_ID=:catProvinceId)  "
						+ "order by LOGIN_NAME ASC");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("sysUserId", new LongType());
		query.addScalar("fullName", new StringType());
		query.addScalar("employeeCode", new StringType());
		query.addScalar("email", new StringType());
		query.setParameter("catProvinceId", catProvinceId);
		query.setResultTransformer(Transformers.aliasToBean(SysUserDTO.class));

		List<SysUserDTO> ls = query.list();
		return ls;
	}

	public ComplainOrderRequestDTO getByTicketCode(ComplainOrderRequestDTO obj) {
		StringBuilder stringBuilder = new StringBuilder("SELECT ");
		stringBuilder.append("T1.COMPLAIN_ORDER_REQUEST_ID complainOrderRequestId ");
		stringBuilder.append(",T1.TICKET_CODE ticketCode ");
		stringBuilder.append(",T1.STATUS status ");
		stringBuilder.append(" FROM CTCT_COMS_OWNER.COMPLAIN_ORDER_REQUEST T1 ");

		stringBuilder.append("WHERE 1=1  ");

		if (obj.getTicketId() != null) {

			stringBuilder.append(" AND T1.COMPLAIN_ORDER_REQUEST_ID =:ticketId ");

		}
		if (!Strings.isNullOrEmpty(obj.getTicketCode())) {
			stringBuilder.append("AND UPPER(T1.TICKET_CODE) like UPPER(:ticketCode) ");
		}

		SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());

		query.addScalar("complainOrderRequestId", new LongType());
		query.addScalar("ticketCode", new StringType());
		query.addScalar("status", new LongType());
		if (obj.getTicketId() != null) {
			query.setParameter("ticketId", obj.getTicketId());
		}
		if (!Strings.isNullOrEmpty(obj.getTicketCode())) {
			query.setParameter("ticketCode", "%" + obj.getTicketCode() + "%");
		}

		query.setResultTransformer(Transformers.aliasToBean(ComplainOrderRequestDTO.class));
		return (ComplainOrderRequestDTO) query.uniqueResult();
	}

}
