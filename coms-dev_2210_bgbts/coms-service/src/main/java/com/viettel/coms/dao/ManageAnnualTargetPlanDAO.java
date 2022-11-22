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
import com.viettel.coms.bo.ManageAnnualTargetPlanBO;
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
import com.viettel.coms.dto.ManageAnnualTargetPlanDTO;
import com.viettel.coms.dto.ManageUsedMaterialDTO;
import com.viettel.coms.dto.ManageVttbDTO;
import com.viettel.coms.dto.RevokeCashMonthPlanDTO;
import com.viettel.coms.dto.SysUserCOMSDTO;
import com.viettel.coms.dto.TmpnTargetDTO;
import com.viettel.coms.dto.TmpnTargetDetailDTO;
import com.viettel.coms.dto.WorkItemDTO;
import com.viettel.coms.dto.WorkItemDetailDTO;
import com.viettel.erp.dto.SysUserDTO;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import com.viettel.wms.utils.ValidateUtils;

/**
 * @author HoangNH38
 */
@Repository("manageAnnualTargetPlanDAO")
public class ManageAnnualTargetPlanDAO extends BaseFWDAOImpl<ManageAnnualTargetPlanBO, Long> {

	public ManageAnnualTargetPlanDAO() {
		this.model = new ManageAnnualTargetPlanBO();
	}

	public ManageAnnualTargetPlanDAO(Session session) {
		this.session = session;
	}

	public List<ManageAnnualTargetPlanDTO> doSearch(ManageAnnualTargetPlanDTO obj, List<String> groupIdList) {
		StringBuilder sql = new StringBuilder().append(" SELECT ")
				.append(" T1.MANAGE_ANNUAL_TARGET_PLAN_ID manageAnnualTargetPlanId, ")
				.append(" T1.YEAR year, ")
				.append(" T1.MONTH month, ")
				.append(" T1.CONTRACT_VALUE contractValue, ")
				.append(" T1.TC_VALUE tcValue, ")
				.append(" T1.DOANH_THU doanhThu, ")
				.append(" T1.CREATE_DATE createDate, ")
				.append(" T1.UPDATE_DATE updateDate, ")
				.append(" T1.CREATE_USER_ID createUserId, ")
				.append(" T1.UPDATE_USER_ID updateUserId ")
				.append(" FROM MANAGE_ANNUAL_TARGET_PLAN T1 ")
				.append(" WHERE 1 = 1  AND T1.STATUS != 0 ")
				;

		if (StringUtils.isNotEmpty(obj.getKeySearch())) {
			sql.append(" AND (upper(T1.YEAR) LIKE upper(:keySearch) escape '&')  OR  (upper(T1.MONTH) LIKE upper(:keySearch) escape '&')");
		}
		sql.append(" ORDER BY MANAGE_ANNUAL_TARGET_PLAN_ID DESC ");
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
		if (StringUtils.isNotEmpty(obj.getKeySearch())) {
			query.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch()) + "%");
			queryCount.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch()) + "%");
		}

		query.addScalar("manageAnnualTargetPlanId", new LongType());
		query.addScalar("year", new StringType());
		query.addScalar("month", new StringType());
		query.addScalar("contractValue", new LongType());
		query.addScalar("tcValue", new LongType());
		query.addScalar("doanhThu", new LongType());
		query.addScalar("createDate", new DateType());
		query.addScalar("createUserId", new LongType());

		query.setResultTransformer(Transformers.aliasToBean(ManageAnnualTargetPlanDTO.class));
		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		return query.list();
	}
	
	public List<ManageAnnualTargetPlanDTO> getById(ManageAnnualTargetPlanDTO obj, List<String> groupIdList) {
		StringBuilder sql = new StringBuilder().append(" SELECT ")
				.append(" T1.MANAGE_ANNUAL_TARGET_PLAN_ID manageAnnualTargetPlanId, ")
				.append(" T1.YEAR year, ")
				.append(" T1.MONTH month, ")
				.append(" T1.CONTRACT_VALUE contractValue, ")
				.append(" T1.TC_VALUE tcValue, ")
				.append(" T1.DOANH_THU doanhThu, ")
				.append(" T1.CREATE_DATE createDate, ")
				.append(" T1.UPDATE_DATE updateDate, ")
				.append(" T1.CREATE_USER_ID createUserId, ")
				.append(" T1.UPDATE_USER_ID updateUserId ")
				.append(" FROM MANAGE_ANNUAL_TARGET_PLAN T1 ")
				.append(" WHERE 1 = 1 ")
				.append(" AND  T1.MANAGE_ANNUAL_TARGET_PLAN_ID = :manageAnnualTargetPlanId ")
				;
		sql.append(" ORDER BY MANAGE_ANNUAL_TARGET_PLAN_ID DESC ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		if (obj.getManageAnnualTargetPlanId() != null) {
			query.setParameter("manageAnnualTargetPlanId", obj.getManageAnnualTargetPlanId());
		}

		query.addScalar("manageAnnualTargetPlanId", new LongType());
		query.addScalar("year", new StringType());
		query.addScalar("month", new StringType());
		query.addScalar("contractValue", new LongType());
		query.addScalar("tcValue", new LongType());
		query.addScalar("doanhThu", new LongType());
		query.addScalar("createDate", new DateType());
		query.addScalar("createUserId", new LongType());

		query.setResultTransformer(Transformers.aliasToBean(ManageAnnualTargetPlanDTO.class));
		return query.list();
	}
	
	public void remove(ManageAnnualTargetPlanDTO obj) {
		StringBuilder sql = new StringBuilder().append(" UPDATE MANAGE_ANNUAL_TARGET_PLAN set status = 0 where  MANAGE_ANNUAL_TARGET_PLAN_ID = :manageAnnualTargetPlanId ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("manageAnnualTargetPlanId", obj.getManageAnnualTargetPlanId());
		query.executeUpdate();
	}


}
