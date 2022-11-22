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
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.google.common.base.Joiner;
import com.viettel.asset.dto.SysGroupDto;
import com.viettel.coms.bo.GoodsPlanBO;
import com.viettel.coms.dto.GoodsPlanDTO;
import com.viettel.coms.dto.GoodsPlanDetailDTO;
import com.viettel.coms.dto.RequestGoodsDTO;
import com.viettel.coms.dto.RequestGoodsDetailDTO;
import com.viettel.coms.dto.SignVofficeDTO;
import com.viettel.coms.utils.ValidateUtils;
import com.viettel.erp.dto.CatFileInvoiceDTO;
import com.viettel.erp.dto.ConstrCompleteRecordsMapDTO;
import com.viettel.erp.dto.ConstrGroundHandoverDTO;
import com.viettel.erp.dto.ConstrWorkLogsDTO;
import com.viettel.erp.dto.MonitorMissionAssignDTO;
import com.viettel.erp.dto.RoleConfigProvinceUserCDTDTO;
import com.viettel.erp.dto.RoleConfigProvinceUserCTCTDTO;
import com.viettel.erp.dto.SysUserDTO;
import com.viettel.erp.dto.VConstructionHcqtDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@Repository("goodsPlanDAO")
public class GoodsPlanDAO extends BaseFWDAOImpl<GoodsPlanBO, Long> {
	@Value("${monitorMissionAssign.attachType}")
    private Long attachTypeMonitor;
	public GoodsPlanDAO() {
		this.model = new GoodsPlanBO();
	}

	public GoodsPlanDAO(Session session) {
		this.session = session;
	}

	@SuppressWarnings("unchecked")
	public List<GoodsPlanDetailDTO> doSearch(GoodsPlanDTO obj) {
		StringBuilder sql = new StringBuilder("SELECT GD.GOODS_PLAN_DETAIL_ID goodsPlanDetailId,"
				+ "GD.GOODS_PLAN_ID goodsPlanId," + "GD.REQUEST_GOODS_ID requestGoodsId,"
				+ "GD.REQUEST_GOODS_DETAIL_ID requestGoodsDetailId," + "GD.CONSTRUCTION_ID constructionId,"
				+ "GD.CONSTRUCTION_CODE constructionCode," + "GD.CNT_CONTRACT_ID cntContractId,"
				+ "GD.CNT_CONTRACT_CODE cntContractCode," + "GD.GOODS_NAME goodsName," + "GD.CAT_UNIT_ID catUnitId,"
				+ "GD.CAT_UNIT_NAME catUnitName," + "GD.QUANTITY quantity," + "GD.EXPECTED_DATE expectedDate,"
				+ "GD.DESCRIPTION description," + "GD.REAL_IE_TRANS_DATE realIeTransDate "
				+ "FROM GOODS_PLAN_DETAIL GD WHERE GOODS_PLAN_ID=:goodsPlanId ");
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
		query.addScalar("goodsPlanDetailId", new LongType());
		query.addScalar("goodsPlanId", new LongType());
		query.addScalar("requestGoodsId", new LongType());
		query.addScalar("requestGoodsDetailId", new LongType());
		query.addScalar("constructionId", new LongType());
		query.addScalar("constructionCode", new StringType());
		query.addScalar("cntContractId", new LongType());
		query.addScalar("cntContractCode", new StringType());
		query.addScalar("goodsName", new StringType());
		query.addScalar("catUnitId", new LongType());
		query.addScalar("catUnitName", new StringType());
		query.addScalar("quantity", new LongType());
		query.addScalar("expectedDate", new DateType());
		query.addScalar("description", new StringType());
		query.addScalar("realIeTransDate", new StringType());

		query.setResultTransformer(Transformers.aliasToBean(GoodsPlanDetailDTO.class));

		query.setParameter("goodsPlanId", obj.getGoodsPlanId());
		queryCount.setParameter("goodsPlanId", obj.getGoodsPlanId());
		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<RequestGoodsDTO> doSearchPopupGoodsPlan(GoodsPlanDTO obj) {
		StringBuilder sql = new StringBuilder("SELECT DISTINCT RG.REQUEST_GOODS_ID requestGoodsId,"
				+ "RG.SYS_GROUP_ID sysGroupId," + "RG.CONSTRUCTION_ID constructionId,"
				+ "RG.CONSTRUCTION_CODE constructionCode," + "RG.CNT_CONTRACT_ID cntContractId,"
				+ "RG.CNT_CONTRACT_CODE cntContractCode," + "RG.CREATED_DATE createdDate,"
				+ "RG.DESCRIPTION description," + "SS.NAME sysGroupName " + "FROM REQUEST_GOODS RG "
				+ "LEFT JOIN SYS_GROUP SS ON SS.SYS_GROUP_ID=RG.SYS_GROUP_ID "
				+ "LEFT JOIN REQUEST_GOODS_DETAIL RGD ON RG.REQUEST_GOODS_ID=RGD.REQUEST_GOODS_ID "
				+ "WHERE 1=1 AND RG.SYS_GROUP_ID is not null "
				+ "AND RGD.REQUEST_GOODS_ID NOT IN (SELECT gpd.REQUEST_GOODS_ID FROM GOODS_PLAN_DETAIL gpd where gpd.REQUEST_GOODS_ID is not null) ");
		if (obj.getCntContractCode() != null) {
			sql.append("AND RG.CNT_CONTRACT_CODE =:cntContractCode ");
		}
		if (obj.getConstructionId() != null) {
			sql.append("AND RG.CONSTRUCTION_ID =:constructionId ");
		}
		if (obj.getSysGroupId() != null) {
			sql.append("AND RG.SYS_GROUP_ID=:sysGroupId ");
		}
		if (obj.getCatProvinceId() != null) {
			sql.append("AND RG.CAT_PROVINCE_ID=:catProvinceId ");
		}
		if (obj.getToStartDate() != null) {
			sql.append("AND TRUNC(RG.CREATED_DATE) >=:startDate ");
		}
		if (obj.getToEndDate() != null) {
			sql.append("AND TRUNC(RG.CREATED_DATE) <=:endDate ");
		}
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		query.addScalar("requestGoodsId", new LongType());
		query.addScalar("sysGroupId", new LongType());
		query.addScalar("constructionId", new LongType());
		query.addScalar("constructionCode", new StringType());
		query.addScalar("cntContractId", new LongType());
		query.addScalar("cntContractCode", new StringType());
		query.addScalar("createdDate", new DateType());
		query.addScalar("description", new StringType());
		query.addScalar("sysGroupName", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(RequestGoodsDTO.class));

		if (obj.getCntContractCode() != null) {
			query.setParameter("cntContractCode", obj.getCntContractCode());
			queryCount.setParameter("cntContractCode", obj.getCntContractCode());
		}
		if (obj.getConstructionId() != null) {
			query.setParameter("constructionId", obj.getConstructionId());
			queryCount.setParameter("constructionId", obj.getConstructionId());
		}
		if (obj.getSysGroupId() != null) {
			query.setParameter("sysGroupId", obj.getSysGroupId());
			queryCount.setParameter("sysGroupId", obj.getSysGroupId());
		}
		if (obj.getCatProvinceId() != null) {
			query.setParameter("catProvinceId", obj.getCatProvinceId());
			queryCount.setParameter("catProvinceId", obj.getCatProvinceId());
		}
		if (obj.getToStartDate() != null) {
			query.setParameter("startDate", obj.getToStartDate());
			queryCount.setParameter("startDate", obj.getToStartDate());
		}
		if (obj.getToEndDate() != null) {
			query.setParameter("endDate", obj.getToEndDate());
			queryCount.setParameter("endDate", obj.getToEndDate());
		}
		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<SysGroupDto> doSearchSysGroup(GoodsPlanDTO obj) {
		StringBuilder sql = new StringBuilder("SELECT DISTINCT SYS_GROUP_ID groupId," + "CODE groupCode,"
				+ "NAME name FROM SYS_GROUP where 1=1 and ROWNUM < 11 ");
		if (StringUtils.isNotBlank(obj.getKeySearch())) {
			sql.append("AND upper(CODE) like upper(:keySearch) OR upper(NAME) like upper(:keySearch) ");
		}
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("groupId", new LongType());
		query.addScalar("groupCode", new StringType());
		query.addScalar("name", new StringType());

		query.setResultTransformer(Transformers.aliasToBean(SysGroupDto.class));
		if (StringUtils.isNotBlank(obj.getKeySearch())) {
			query.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch()) + "%");
		}
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<RequestGoodsDetailDTO> doSearchReqGoodsDetail(GoodsPlanDTO obj) {
		StringBuilder sql = new StringBuilder("SELECT RD.REQUEST_GOODS_DETAIL_ID requestGoodsDetailId, "
				+ "RD.REQUEST_GOODS_ID requestGoodsId," + "RD.GOODS_NAME goodsName," + "RD.QUANTITY quantity,"
				+ "RD.CAT_UNIT_ID catUnitId," + "RD.CAT_UNIT_NAME catUnitName," + "RD.SUGGEST_DATE suggestDate,"
				// + "RD.DESCRIPTION description,"
				+ "RG.CONSTRUCTION_ID constructionId," + "RG.CONSTRUCTION_CODE constructionCode,"
				+ "RG.CNT_CONTRACT_ID cntContractId," + "RG.CNT_CONTRACT_CODE cntContractCode "
				+ "FROM REQUEST_GOODS_DETAIL RD LEFT JOIN REQUEST_GOODS RG ON RD.REQUEST_GOODS_ID = RG.REQUEST_GOODS_ID "
				+ "WHERE RD.REQUEST_GOODS_ID =:requestGoodsId ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("requestGoodsId", obj.getRequestGoodsId());
		query.addScalar("requestGoodsDetailId", new LongType());
		query.addScalar("requestGoodsId", new LongType());
		query.addScalar("goodsName", new StringType());
		query.addScalar("quantity", new DoubleType());
		query.addScalar("catUnitId", new LongType());
		query.addScalar("catUnitName", new StringType());
		query.addScalar("suggestDate", new DateType());
		// query.addScalar("description", new StringType());
		query.addScalar("constructionId", new LongType());
		query.addScalar("constructionCode", new StringType());
		query.addScalar("cntContractId", new LongType());
		query.addScalar("cntContractCode", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(RequestGoodsDetailDTO.class));
		return query.list();
	}

	public GoodsPlanDTO findByCode(String code) {
		StringBuilder sql = new StringBuilder(
				"SELECT GOODS_PLAN_ID goodsPlanId,CODE code FROM GOODS_PLAN WHERE CODE=:code AND ROWNUM < 2 ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("goodsPlanId", new LongType());
		query.addScalar("code", new StringType());

		query.setResultTransformer(Transformers.aliasToBean(GoodsPlanDTO.class));
		query.setParameter("code", code);
		return (GoodsPlanDTO) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<GoodsPlanDTO> doSearchAll(GoodsPlanDTO obj) {
		StringBuilder sql = new StringBuilder("SELECT T.GOODS_PLAN_ID goodsPlanId," + "T.CODE code," + "T.NAME name,"
				+ "T.CREATED_DATE createdDate," + "T.BASE_CONTENT baseContent," + "T.PERFORM_CONTENT performContent,"
				+ "T.STATUS status," + "T.SIGN_STATE signState," + "T.CREATED_USER_ID createdUserId "
				+ "FROM GOODS_PLAN T WHERE T.STATUS != 0 ");

		if (StringUtils.isNotEmpty(obj.getCode())) {
			sql.append(" AND (upper(T.CODE) LIKE upper(:code) OR upper(T.NAME) LIKE upper(:code) escape '&')");
		}
		if (obj.getSignVO() != null) {
			if (obj.getSignVO().size() > 0) {
				sql.append(" AND T.SIGN_STATE IN (:signVO)");
			}
		}
		if (obj.getStartDate() != null) {
			sql.append(" AND TRUNC(T.CREATED_DATE) >=:startDate ");
		}
		if (obj.getEndDate() != null) {
			sql.append(" AND TRUNC(T.CREATED_DATE) <=:endDate ");
		}
		sql.append(" ORDER BY T.GOODS_PLAN_ID DESC ");
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		query.addScalar("goodsPlanId", new LongType());
		query.addScalar("code", new StringType());
		query.addScalar("name", new StringType());
		query.addScalar("createdDate", new DateType());
		query.addScalar("baseContent", new StringType());
		query.addScalar("performContent", new StringType());
		query.addScalar("status", new StringType());
		query.addScalar("signState", new StringType());
		query.addScalar("createdUserId", new LongType());

		query.setResultTransformer(Transformers.aliasToBean(GoodsPlanDTO.class));
		if (StringUtils.isNotEmpty(obj.getCode())) {
			query.setParameter("code", "%" + ValidateUtils.validateKeySearch(obj.getCode()) + "%");
			queryCount.setParameter("code", "%" + ValidateUtils.validateKeySearch(obj.getCode()) + "%");
		}
		if (obj.getSignVO() != null) {
			if (obj.getSignVO().size() > 0) {
				query.setParameterList("signVO", obj.getSignVO());
				queryCount.setParameterList("signVO", obj.getSignVO());
			}
		}
		if (obj.getStartDate() != null) {
			query.setParameter("startDate", obj.getStartDate());
			queryCount.setParameter("startDate", obj.getStartDate());
		}
		if (obj.getEndDate() != null) {
			query.setParameter("endDate", obj.getEndDate());
			queryCount.setParameter("endDate", obj.getEndDate());
		}

		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		return query.list();
	}

	public Long remove(GoodsPlanDTO obj) {
		StringBuilder sql = new StringBuilder("UPDATE GOODS_PLAN SET STATUS=0 WHERE GOODS_PLAN_ID=:goodsPlanId ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("goodsPlanId", obj.getGoodsPlanId());
		return (long) query.executeUpdate();
	}

	public Long deleteGoodsPlanDetail(GoodsPlanDTO obj) {
		StringBuilder sql = new StringBuilder("DELETE GOODS_PLAN_DETAIL WHERE GOODS_PLAN_ID=:goodsPlanId ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("goodsPlanId", obj.getGoodsPlanId());
		return (long) query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<SysUserDTO> filterSysUser(GoodsPlanDTO obj) {
		StringBuilder sql = new StringBuilder("SELECT SU.SYS_USER_ID sysUserId," + "SU.FULL_NAME fullName,"
				+ "SU.EMPLOYEE_CODE employeeCode," + "SU.EMAIL email," + "SU.SYS_GROUP_ID sysGroupId "
				+ "FROM SYS_USER SU WHERE 1=1 AND SU.STATUS=1 AND ROWNUM < 11 ");
		if (StringUtils.isNotEmpty(obj.getKeySearch())) {
			sql.append(
					" AND (UPPER(SU.FULL_NAME) LIKE UPPER(:keySearch) ESCAPE '&' OR UPPER(SU.EMPLOYEE_CODE) LIKE UPPER(:keySearch) ESCAPE '&'  OR UPPER(SU.EMAIL) LIKE UPPER(:keySearch) ESCAPE '&')");
		}
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("sysUserId", new LongType());
		query.addScalar("fullName", new StringType());
		query.addScalar("employeeCode", new StringType());
		query.addScalar("email", new StringType());
		query.addScalar("sysGroupId", new LongType());

		if (StringUtils.isNotEmpty(obj.getKeySearch())) {
			query.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
		}
		query.setResultTransformer(Transformers.aliasToBean(SysUserDTO.class));
		return query.list();

	}

	public GoodsPlanDTO getByCode(String code) {
		StringBuilder sql = new StringBuilder("SELECT GOODS_PLAN_ID goodsPlanId ,"
				+ "CREATED_USER_ID createdUserId FROM GOODS_PLAN WHERE CODE =:code ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("goodsPlanId", new LongType());
		query.addScalar("createdUserId", new LongType());
		query.setParameter("code", code);
		query.setResultTransformer(Transformers.aliasToBean(GoodsPlanDTO.class));
		return (GoodsPlanDTO) query.uniqueResult();
	}

	public SignVofficeDTO getInformationVO(String objectId) {
		StringBuilder sql = new StringBuilder("SELECT SIGN_VOFFICE_ID signVofficeId,"
				+ "TRANS_CODE transCode FROM SIGN_VOFFICE WHERE OBJECT_ID=:objectId AND BUSS_TYPE_ID='50' AND ROWNUM < 2 ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("signVofficeId", new LongType());
		query.addScalar("transCode", new StringType());
		query.setParameter("objectId", objectId);
		query.setResultTransformer(Transformers.aliasToBean(SignVofficeDTO.class));
		return (SignVofficeDTO) query.uniqueResult();
	}

	public void removeSignVO(Long signVofficeId) {
		StringBuilder sql = new StringBuilder("DELETE SIGN_VOFFICE WHERE SIGN_VOFFICE_ID=:signVofficeId ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("signVofficeId", signVofficeId);
		query.executeUpdate();
	}

	public void removeSignDetailVO(Long signVofficeId) {
		StringBuilder sql = new StringBuilder("DELETE SIGN_VOFFICE_DETAIL WHERE SIGN_VOFFICE_ID=:signVofficeId ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("signVofficeId", signVofficeId);
		query.executeUpdate();
	}

	// HuyPQ-start
	public List<GoodsPlanDTO> getCatUnit(GoodsPlanDTO obj) {
		StringBuilder sql = new StringBuilder(
				"select cat_unit_id catUnitId" + ", name catUnitName " + "from cat_unit where status=1 ");
		if (StringUtils.isNotEmpty(obj.getKeySearch())) {
			sql.append(" AND (upper(name) LIKE upper(:keySearch)) ");
		}

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setResultTransformer(Transformers.aliasToBean(GoodsPlanDTO.class));
		query.addScalar("catUnitId", new LongType());
		query.addScalar("catUnitName", new StringType());
		if (StringUtils.isNotEmpty(obj.getKeySearch())) {
			query.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(obj.getKeySearch().trim()) + "%");
		}
		return query.list();
	}

	public GoodsPlanDetailDTO genDataContract(GoodsPlanDetailDTO obj) {
		StringBuilder sql = new StringBuilder("SELECT cc.CNT_CONTRACT_ID cntContractId, " + " cc.code cntContractCode "
				+ " FROM CONSTRUCTION cons " + " INNER JOIN CTCT_IMS_OWNER.CNT_CONSTR_WORK_ITEM_TASK ccwit "
				+ " ON cons.CONSTRUCTION_ID = ccwit.CONSTRUCTION_ID " + " INNER JOIN CTCT_IMS_OWNER.CNT_CONTRACT cc "
				+ " ON cc.CNT_CONTRACT_ID = ccwit.CNT_CONTRACT_ID " + " WHERE cons.code       =:code");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("cntContractId", new LongType());
		query.addScalar("cntContractCode", new StringType());

		query.setResultTransformer(Transformers.aliasToBean(GoodsPlanDetailDTO.class));

		query.setParameter("code", obj.getConstructionCode());

		@SuppressWarnings("unchecked")
		List<GoodsPlanDetailDTO> lst = query.list();
		if (lst.size() > 0) {
			return lst.get(0);
		}
		return null;
	}

	public SignVofficeDTO getInformationVOReject(String objectId) {
		StringBuilder sql = new StringBuilder("SELECT SIGN_VOFFICE_ID signVofficeId,"
				+ "TRANS_CODE transCode FROM SIGN_VOFFICE WHERE OBJECT_ID=:objectId AND BUSS_TYPE_ID='50' AND ROWNUM < 2 ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("signVofficeId", new LongType());
		query.addScalar("transCode", new StringType());
		query.setParameter("objectId", objectId);
		query.setResultTransformer(Transformers.aliasToBean(SignVofficeDTO.class));
		return (SignVofficeDTO) query.uniqueResult();
	}
	// Huy-end

	// Huypq-11082020-start mobile hcqt
	public List<VConstructionHcqtDTO> getAllConstructionHcqt(Long sysGroupId) {
		StringBuilder sql = new StringBuilder("SELECT v.construct_id constructId, " + 
				"  constr.CODE constrtCode, " + 
				"  v.CONSTRT_NAME constrtName, "+
				"  v.CONSTRT_ADDRESS constrtAddress , " + 
				"  v.contract_code contractCode, " + 
				"  v.CONTRACT_NAME contractName, "+
				"  v.contract_id contractId, " + 
				"  v.constr_type_name constrTypeName, " + 
				"  v.station_code stationCode, " + 
				"  v.station_house_code stationHouseCode, " +
				"  v.PROVINCE_CODE provinceCode, " +
				"	CASE " + 
				"	WHEN " + 
				"	(v.CONSTRUCT_ID not in (select mma.CONSTRUCT_ID from MONITOR_MISSION_ASSIGN mma where mma.IS_ACTIVE!=0 and mma.CONSTRUCT_ID = v.CONSTRUCT_ID) " + 
				"	AND v.CONSTRUCT_ID not in (select cgh.CONSTRUCT_ID from CONSTR_GROUND_HANDOVER cgh where cgh.IS_ACTIVE!=0 and cgh.CONSTRUCT_ID = v.CONSTRUCT_ID) " + 
				"	AND v.CONSTRUCT_ID not in (select cwl.CONSTRUCT_ID from CONSTR_WORK_LOGS cwl where cwl.IS_ACTIVE!=0 and cwl.CONSTRUCT_ID = v.CONSTRUCT_ID)) THEN 0 " + 
				"	WHEN " + 
				"	(v.CONSTRUCT_ID in (select mma.CONSTRUCT_ID from MONITOR_MISSION_ASSIGN mma where mma.IS_ACTIVE!=0 and mma.CONSTRUCT_ID = v.CONSTRUCT_ID)  " + 
				"	OR v.CONSTRUCT_ID in (select cgh.CONSTRUCT_ID from CONSTR_GROUND_HANDOVER cgh where cgh.IS_ACTIVE!=0 and cgh.CONSTRUCT_ID = v.CONSTRUCT_ID) " + 
				"	OR v.CONSTRUCT_ID in (select cwl.CONSTRUCT_ID from CONSTR_WORK_LOGS cwl where cwl.IS_ACTIVE!=0 and cwl.CONSTRUCT_ID = v.CONSTRUCT_ID)  ) THEN 1 " + 
				"	END checkEx "+
				"	FROM V_CONSTRUCTION_HCQT v " + 
				"	LEFT JOIN construction constr " + 
				"	ON v.constrt_code = constr.code " + 
				"	WHERE 1=1 " + 
//				"AND (v.CONSTRUCT_ID in (select mma.CONSTRUCT_ID from MONITOR_MISSION_ASSIGN mma where mma.IS_ACTIVE!=0 and mma.CONSTRUCT_ID = v.CONSTRUCT_ID) " + 
//				"  OR v.CONSTRUCT_ID in (select cgh.CONSTRUCT_ID from CONSTR_GROUND_HANDOVER cgh where cgh.IS_ACTIVE!=0 and cgh.CONSTRUCT_ID = v.CONSTRUCT_ID) " + 
//				"  OR v.CONSTRUCT_ID in (select cwl.CONSTRUCT_ID from CONSTR_WORK_LOGS cwl where cwl.IS_ACTIVE!=0 and cwl.CONSTRUCT_ID = v.CONSTRUCT_ID) " + 
//				"  ) " + 
				"	AND v.PROVINCE_ID = 9 "
		// " (SELECT PROVINCE_ID FROM SYS_GROUP WHERE SYS_GROUP_ID=:sysGroupId " +
		// //Huypq-comment để test
		// " )"
		);
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("constructId", new LongType());
		query.addScalar("constrtCode", new StringType());
		query.addScalar("constrtAddress", new StringType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("contractName", new StringType());
		query.addScalar("contractId", new DoubleType());
		query.addScalar("constrTypeName", new StringType());
		query.addScalar("stationCode", new StringType());
		query.addScalar("stationHouseCode", new StringType());
		query.addScalar("provinceCode", new StringType());
		query.addScalar("checkEx", new LongType());
		query.addScalar("constrtName", new StringType());

		// query.setParameter("sysGroupId", sysGroupId);

		query.setResultTransformer(Transformers.aliasToBean(VConstructionHcqtDTO.class));

		return query.list();
	}

	public List<MonitorMissionAssignDTO> getMonitorMissionAssignByConstrId(MonitorMissionAssignDTO obj) {
		StringBuilder sqlbuilder = new StringBuilder(
				"SELECT V_CONSTRUCTION_HCQT.CONSTRT_CODE constrtCode, MONITOR_MISSION_ASSIGN.MONITOR_MISSION_ASSIGN_ID monitorMissionAssignId, MONITOR_MISSION_ASSIGN.CODE code, V_CONSTRUCTION_HCQT.CONTRACT_CODE contractCode, V_CONSTRUCTION_HCQT.CONTRACT_NAME contractName, "
						+ " MONITOR_MISSION_ASSIGN.STATUS_CA statusCa, MONITOR_MISSION_ASSIGN.CONSTRUCT_ID constructId, "
						+ " UTIL_ATTACHED_DOCUMENTS.DOCUMENT_PATH documentPath, UTIL_ATTACHED_DOCUMENTS.DOCUMENT_NAME documentName, "
						+ " null investGroupName, null monitorGroupName "
						+ " , MONITOR_MISSION_ASSIGN.COMPLETE_DATE completeDate, "
						+ " MONITOR_MISSION_ASSIGN.A_MONITOR_ID aMonitorId, MONITOR_MISSION_ASSIGN.A_DIRECTOR_ID aDirectorId, MONITOR_MISSION_ASSIGN.CREATED_DATE createdDate, MONITOR_MISSION_ASSIGN.CREATED_USER_ID createdUserId, "
						+ " MONITOR_MISSION_ASSIGN.SIGN_DATE signDate, MONITOR_MISSION_ASSIGN.SIGN_PLACE signPlace, MONITOR_MISSION_ASSIGN.MISSION_DATE missionDate, MONITOR_MISSION_ASSIGN.MONITOR_DOCUMENT monitorDocument, "
						+ " MONITOR_MISSION_ASSIGN.ASSIGN_NOTE assignNote, MONITOR_MISSION_ASSIGN.APPROVAL_DATE approvalDate, "
						+ " UTIL_ATTACHED_DOCUMENTS.ATTACH_ID attachId, "
						+ " CONSTR_COMPLETE_RECORDS_MAP.CONSTR_COMP_RE_MAP_ID constrCompReMapId, "
						+ " ASM.COMMENTS comments " + " ,V_CONSTRUCTION_HCQT.CONSTRT_ADDRESS constrtAddress "
						+ " FROM MONITOR_MISSION_ASSIGN "
						+ " INNER JOIN CONSTR_COMPLETE_RECORDS_MAP ON MONITOR_MISSION_ASSIGN.MONITOR_MISSION_ASSIGN_ID = CONSTR_COMPLETE_RECORDS_MAP.DATA_TABLE_ID_VALUE "
						+ " INNER JOIN V_CONSTRUCTION_HCQT ON V_CONSTRUCTION_HCQT.CONSTRUCT_ID = MONITOR_MISSION_ASSIGN.CONSTRUCT_ID "
						+ " LEFT JOIN  APPROVAL_SIGN_MANAGEMENT ASM ON (CONSTR_COMPLETE_RECORDS_MAP.CONSTR_COMP_RE_MAP_ID = ASM.CONSTR_COMP_RE_MAP_ID and ASM.APPROVAL_STATUS =2) "
						+ " LEFT JOIN UTIL_ATTACHED_DOCUMENTS ON MONITOR_MISSION_ASSIGN.MONITOR_MISSION_ASSIGN_ID = UTIL_ATTACHED_DOCUMENTS.PARENT_ID AND UTIL_ATTACHED_DOCUMENTS.TYPE = :type1 "
						+ " WHERE  " + " MONITOR_MISSION_ASSIGN.IS_ACTIVE = 1 "
						+ " AND CONSTR_COMPLETE_RECORDS_MAP.DATA_TABLE_NAME = 'MONITOR_MISSION_ASSIGN' ");

		if (obj.getConstructId() != null) {
			sqlbuilder.append(" AND V_CONSTRUCTION_HCQT.CONSTRUCT_ID = :constructId ");
		}
		if (obj.getContractId() != null) {
			sqlbuilder.append(" AND V_CONSTRUCTION_HCQT.CONTRACT_ID = :contractId ");
		}
		sqlbuilder.append(" ORDER BY MONITOR_MISSION_ASSIGN.MONITOR_MISSION_ASSIGN_ID DESC ");
		SQLQuery query = getSession().createSQLQuery(sqlbuilder.toString());
		query.addScalar("constrtCode", StringType.INSTANCE);
		query.addScalar("monitorMissionAssignId", LongType.INSTANCE);
		query.addScalar("code", StringType.INSTANCE);
		query.addScalar("statusCa", LongType.INSTANCE);
		query.addScalar("constructId", LongType.INSTANCE);
		query.addScalar("documentPath", StringType.INSTANCE);
		query.addScalar("documentName", StringType.INSTANCE);
		query.addScalar("aMonitorId", LongType.INSTANCE);
		query.addScalar("aDirectorId", LongType.INSTANCE);
		query.addScalar("constrCompReMapId", LongType.INSTANCE);
		query.addScalar("createdUserId", new LongType());
		query.addScalar("createdDate", new DateType());
		query.addScalar("signDate", new DateType());
		query.addScalar("missionDate", new DateType());
		query.addScalar("approvalDate", new DateType());
		query.addScalar("signPlace", StringType.INSTANCE);
		query.addScalar("monitorDocument", StringType.INSTANCE);
		query.addScalar("assignNote", StringType.INSTANCE);
		query.addScalar("contractCode", StringType.INSTANCE);
		query.addScalar("contractName", StringType.INSTANCE);
		query.addScalar("comments", StringType.INSTANCE);
		query.addScalar("completeDate", DateType.INSTANCE);
		query.addScalar("investGroupName", StringType.INSTANCE);
		query.addScalar("monitorGroupName", StringType.INSTANCE);
		query.addScalar("constrtAddress", StringType.INSTANCE);

		query.setResultTransformer(Transformers.aliasToBean(MonitorMissionAssignDTO.class));

		if (obj.getConstructId() != null) {
			query.setParameter("constructId", obj.getConstructId());
		}

		if (obj.getContractId() != null) {
			query.setParameter("contractId", obj.getContractId());
		}
		query.setParameter("type1", attachTypeMonitor);
		return query.list();
	}

	public List<ConstrGroundHandoverDTO> getAllConstrGroundHandover(ConstrGroundHandoverDTO dto) {
		StringBuilder sql = new StringBuilder("SELECT " + "CG.EXCLUSION_PLANT_SENT_DATE exclusionPlantSentDate, "
				+ "CG.START_EXCLUSTION_DATE startExclusionDate, " + "CG.END_EXCLUSTION_DATE endExclusionDate, "
				+ "CG.CONSTR_GROUND_HANDOVER_ID constrGroundHandoverId, " + "CG.CODE code,"
				+ "CG.A_DIRECTOR_ID aDirectorId," + "CG.A_MONITOR_ID aMonitorId,"
				+ "CG.A_IN_CHARGE_MONITOR_ID aInChargeMonitorId," + "CG.B_DIRECTOR_ID bDirectorId,"
				+ "CG.B_IN_CHARGE_CONSTRUCT_ID bInChargeConstructId," + "CG.STATUS_CA statusCa,"
				+ "CG.HANDOVER_DATE handoverDate," + "CG.GROUND_CURRENT_STATUS groundCurrentStatus,"
				+ "CG.BENCHMARK benchmark," + "CG.CREATED_DATE createdDate," + "CG.CREATED_USER_ID createdUserId,"
				+ "CG.APPROVAL_DATE approvalDate," + "CG.SIGN_DATE signDate," + "CG.SIGN_PLACE signPlace,"
				+ "CG.CONSTRUCT_ID constructId," + "CE1.FULL_NAME adirectorName," + "CE2.FULL_NAME amonitorName,"
				+ "CE4.FULL_NAME bdirectorName," + "CE5.FULL_NAME binChargeConstructName,"
				+ "VCH.CONSTRT_ADDRESS constrtAddress," + "VCH.CONSTRT_CODE constrtCode,"
				+ "VCH.CONSTRT_NAME constrtName," + "VCH.CONTRACT_NAME contractName,"
				+ "VCH.CONTRACT_CODE contractCode," + "VCH.STATION_CODE stationCode,"
				+ " CCRM.CONSTR_COMP_RE_MAP_ID constrCompReMapId," + " ASM.COMMENTS comments "
				+ " ,CG.SUPERVISING_CONSULTANT supervisingConsultant " + " FROM CONSTR_GROUND_HANDOVER CG "
				+ " INNER JOIN CAT_EMPLOYEE CE1 ON TO_NUMBER(CE1.ID) = CG.A_DIRECTOR_ID "
				+ " INNER JOIN CAT_EMPLOYEE CE2 ON TO_NUMBER(CE2.ID) = CG.A_MONITOR_ID"
				+ " INNER JOIN CAT_EMPLOYEE CE4 ON TO_NUMBER(CE4.ID) = CG.B_DIRECTOR_ID"
				+ " INNER JOIN CAT_EMPLOYEE CE5 ON TO_NUMBER(CE5.ID) = CG.B_IN_CHARGE_CONSTRUCT_ID"
				+ " INNER JOIN V_CONSTRUCTION_HCQT VCH ON VCH.CONSTRUCT_ID = CG.CONSTRUCT_ID"
				+ " INNER JOIN CONSTR_COMPLETE_RECORDS_MAP CCRM ON CCRM.DATA_TABLE_ID_VALUE = CG.CONSTR_GROUND_HANDOVER_ID AND CCRM.DATA_TABLE_NAME = 'CONSTR_GROUND_HANDOVER' "
				+ " LEFT JOIN APPROVAL_SIGN_MANAGEMENT ASM ON (CCRM.CONSTR_COMP_RE_MAP_ID = ASM.CONSTR_COMP_RE_MAP_ID and ASM.APPROVAL_STATUS =2) "
				+ " WHERE 1=1 AND CG.IS_ACTIVE = 1 " );

		if (null != dto.getContractId()) {
			sql.append(" AND VCH.CONTRACT_ID = :contractId ");
		}
		if(null != dto.getConstructId()) {
			sql.append(" AND CG.CONSTRUCT_ID =:constructId ");
		}
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("endExclusionDate", DateType.INSTANCE);
		query.addScalar("startExclusionDate", DateType.INSTANCE);
		query.addScalar("exclusionPlantSentDate", DateType.INSTANCE);
		query.addScalar("constructId", LongType.INSTANCE);
		query.addScalar("constrGroundHandoverId", LongType.INSTANCE);
		query.addScalar("code", StringType.INSTANCE);
		query.addScalar("handoverDate", StandardBasicTypes.TIMESTAMP);
		query.addScalar("aDirectorId", LongType.INSTANCE);
		query.addScalar("aMonitorId", LongType.INSTANCE);
		query.addScalar("aInChargeMonitorId", LongType.INSTANCE);
		query.addScalar("bDirectorId", LongType.INSTANCE);
		query.addScalar("bInChargeConstructId", LongType.INSTANCE);
		query.addScalar("constrCompReMapId", LongType.INSTANCE);
		query.addScalar("groundCurrentStatus", StringType.INSTANCE);
		query.addScalar("benchmark", StringType.INSTANCE);
		query.addScalar("createdDate", StandardBasicTypes.TIMESTAMP);
		query.addScalar("createdUserId", LongType.INSTANCE);
		query.addScalar("approvalDate", StandardBasicTypes.TIMESTAMP);
		query.addScalar("statusCa", LongType.INSTANCE);
		query.addScalar("signDate", StandardBasicTypes.TIMESTAMP);
		query.addScalar("signPlace", StringType.INSTANCE);
		query.addScalar("adirectorName", StringType.INSTANCE);
		query.addScalar("amonitorName", StringType.INSTANCE);
		query.addScalar("supervisingConsultant", StringType.INSTANCE);
		query.addScalar("bdirectorName", StringType.INSTANCE);
		query.addScalar("binChargeConstructName", StringType.INSTANCE);
		query.addScalar("constrtAddress", StringType.INSTANCE);
		query.addScalar("constrtCode", StringType.INSTANCE);
		query.addScalar("constrtName", StringType.INSTANCE);
		query.addScalar("contractName", StringType.INSTANCE);
		query.addScalar("contractCode", StringType.INSTANCE);
		query.addScalar("stationCode", StringType.INSTANCE);
		query.addScalar("comments", StringType.INSTANCE);

		query.setParameter("constructId", dto.getConstructId());
		query.setResultTransformer(Transformers.aliasToBean(ConstrGroundHandoverDTO.class));

		if (null != dto.getContractId()) {
			query.setParameter("contractId", dto.getContractId());
		}
		if(null != dto.getConstructId()) {
			query.setParameter("constructId", dto.getConstructId());
		}
		List<ConstrGroundHandoverDTO> list = query.list();
		return list;
	}

	public List<ConstrWorkLogsDTO> getAllConstrWorkLogs(ConstrWorkLogsDTO dto) {
		StringBuilder sql = new StringBuilder("SELECT "
				+ "CW.CODE code,"
				+ "CW.CONSTR_WORK_LOGS_ID constrWorkLogsId, "
				+ "CW.LOG_DATE logDate, "
				+ "CW.WORK_CONTENT workContent, "
				+ "CW.ADDITION_CHANGE_ARISE additionChangeArise, "
				+ "CW.CONTRACTOR_COMMENTS contractorComments,     "
				+ "CW.MONITOR_COMMENTS monitorComments ,"
				+ "CW.STATUS_CA statusCa, "
				+ "EW.WORK_ITEM_NAME workItemName, "
				+ "EW.ESTIMATES_WORK_ITEM_ID estimatesWorkItemId, "
				+ "CW.A_MONITOR_ID aMonitorId,"
				+ "CW.created_User_Id createdUserId,"
				+ "ce2.FULL_NAME aMonitorName,"
				+ "ce3.FULL_NAME bConstructName, "
				+ "CW.B_CONSTRUCT_ID bConstructId, "
				+ "cw.construct_Id constructId ,"
				+ "cvc.constr_comp_re_map_id constrCompReMapId, "
				+ "ASM.COMMENTS comments "
				+ " ,CW.CONSTRUCTION_CONDITION constructionCondition, "
				+ "CW.WORKER_COUNT workerCount "
				+ " FROM  CONSTR_WORK_LOGS CW " + "left jOIN ESTIMATES_WORK_ITEMS EW "
				+ " ON EW.ESTIMATES_WORK_ITEM_ID = CW.ESTIMATES_WORK_ITEM_ID "
				+ " left join CAT_EMPLOYEE ce2  on ce2.ID = cw.A_MONITOR_ID "
				+ " left join CAT_EMPLOYEE ce3 on ce3.ID = cw.B_CONSTRUCT_ID "
				+ " left join CONSTR_COMPLETE_RECORDS_MAP cvc on cw.CONSTR_WORK_LOGS_ID = cvc.data_table_id_value and cvc.data_table_name = 'CONSTR_WORK_LOGS'"
				+ " left join APPROVAL_SIGN_MANAGEMENT ASM ON (cvc.CONSTR_COMP_RE_MAP_ID = ASM.CONSTR_COMP_RE_MAP_ID and ASM.APPROVAL_STATUS =2) "
				+ " WHERE 1=1 AND CW.is_active=1 ");
		if(null != dto.getConstructId()) {
			sql.append(" and CW.construct_id=:constructId ");
		}
		sql.append(" order by CW.CONSTR_WORK_LOGS_ID DESC ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("code", new StringType());
		query.addScalar("constrWorkLogsId", new LongType());
		query.addScalar("logDate", new DateType());
		query.addScalar("workContent", new StringType());
		query.addScalar("additionChangeArise", new StringType());
		query.addScalar("contractorComments", new StringType());
		query.addScalar("monitorComments", new StringType());
		query.addScalar("statusCa", new LongType());
		query.addScalar("workItemName", new StringType());
		query.addScalar("estimatesWorkItemId", new LongType());
		query.addScalar("aMonitorId", new LongType());
		query.addScalar("createdUserId", new LongType());
		query.addScalar("aMonitorName", new StringType());
		query.addScalar("bConstructName", new StringType());
		query.addScalar("bConstructId", new LongType());
		query.addScalar("constructId", new LongType());
		query.addScalar("constrCompReMapId", new LongType());
		query.addScalar("comments", new StringType());
		query.addScalar("constructionCondition", new StringType());
		query.addScalar("workerCount", new StringType());
		if(null != dto.getConstructId()) {
			query.setParameter("constructId", dto.getConstructId());
		}
		
		query.setResultTransformer(Transformers.aliasToBean(ConstrWorkLogsDTO.class));
		List<ConstrWorkLogsDTO> list = query.list();
		return list;
	}
	// huy-end
	//HienLT56 start 17082020
	public List<VConstructionHcqtDTO> addNewConstruction(Long sysGroupId) {
		StringBuilder sql = new StringBuilder("SELECT v.construct_id constructId, " + 
				"  constr.CODE constrtCode, " + 
				"  v.CONSTRT_ADDRESS constrtAddress , " + 
				"  v.contract_code contractCode, " + 
				"  v.contract_id contractId, " + 
				"  v.constr_type_name constrTypeName, " + 
				"  v.station_code stationCode, " + 
				"  v.station_house_code stationHouseCode " + 
				"FROM V_CONSTRUCTION_HCQT v " + 
				"LEFT JOIN construction constr " + 
				"ON v.constrt_code = constr.code " + 
				"WHERE 1=1 " + 
				"AND (v.CONSTRUCT_ID not in (select mma.CONSTRUCT_ID from MONITOR_MISSION_ASSIGN mma where mma.IS_ACTIVE!=0 and mma.CONSTRUCT_ID = v.CONSTRUCT_ID) " + 
				"  AND v.CONSTRUCT_ID not in (select cgh.CONSTRUCT_ID from CONSTR_GROUND_HANDOVER cgh where cgh.IS_ACTIVE!=0 and cgh.CONSTRUCT_ID = v.CONSTRUCT_ID) " + 
				"  AND v.CONSTRUCT_ID not in (select cwl.CONSTRUCT_ID from CONSTR_WORK_LOGS cwl where cwl.IS_ACTIVE!=0 and cwl.CONSTRUCT_ID = v.CONSTRUCT_ID) " + 
				"  ) " + 
				"AND v.PROVINCE_ID = 18 "
		// " (SELECT PROVINCE_ID FROM SYS_GROUP WHERE SYS_GROUP_ID=:sysGroupId " +
		// //Huypq-comment để test
		// " )"
		);
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("constructId", new LongType());
		query.addScalar("constrtCode", new StringType());
		query.addScalar("constrtAddress", new StringType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("contractId", new DoubleType());
		query.addScalar("constrTypeName", new StringType());
		query.addScalar("stationCode", new StringType());
		query.addScalar("stationHouseCode", new StringType());

		// query.setParameter("sysGroupId", sysGroupId);

		query.setResultTransformer(Transformers.aliasToBean(VConstructionHcqtDTO.class));

		return query.list();
	}

	public String autoGenCode() {
		StringBuffer sql = new StringBuffer("select get_next_code('MONITOR_MISSION_ASSIGN', 'CODE','GNVGS',6) code from dual");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("code", StandardBasicTypes.STRING);
		return (String) query.uniqueResult();
	}

	public CatFileInvoiceDTO onlyFindByTableName(String tableName) {
        SQLQuery query = getSession().createSQLQuery("select invoice.CAT_FILE_INVOICE_ID catFileInvoiceId from CAT_FILE_INVOICE invoice " 
        		+ " where invoice.DATA_TABLE_NAME = :tableName AND ROWNUM = 1");

        query.addScalar("catFileInvoiceId", LongType.INSTANCE);

        query.setResultTransformer(Transformers.aliasToBean(CatFileInvoiceDTO.class));
        query.setParameter("tableName", tableName);

        return (CatFileInvoiceDTO) query.uniqueResult();
	}

	public Long saveTable(MonitorMissionAssignDTO monitorMissionAssign) {
		Session session = getSession();
		Long monitorMissionAssignId = (Long) session.save(monitorMissionAssign.toModel());
		monitorMissionAssign.getConstrCompleteRecordMap().setDataTableIdValue(monitorMissionAssignId);     
		session.save(monitorMissionAssign.getConstrCompleteRecordMap().toModel());
	    return monitorMissionAssignId;
	}

	public String getCode(String tableName, String value) {
		SQLQuery query = getSession().createSQLQuery(Joiner.on("").join("select get_next_code('", tableName, "', 'CODE','", value, "',6) code from dual"));
		query.addScalar("code", StandardBasicTypes.STRING);

		return (String) query.uniqueResult();
	}

	public Long saveConstrGroundHand(ConstrGroundHandoverDTO constrGroundHandover) {
		Session session = getSession();
		Long constrGroundHandId = (Long) session.save(constrGroundHandover.toModel());
		return constrGroundHandId;
	}

	public Long saveConstrComRecMap(ConstrCompleteRecordsMapDTO constrCompleteRecordsMap) {
		Session session = getSession();
		return (Long) session.save(constrCompleteRecordsMap.toModel());
	}

	public void updateAprroval(String tableName, Long id) {
		Session session = getSession();
    	session.createSQLQuery("UPDATE CONSTR_COMPLETE_RECORDS_MAP SET CONSTR_COMPLETE_RECORDS_MAP.STATUS = '0', CONSTR_COMPLETE_RECORDS_MAP.LEVEL_ORDER = '' "
				+ "WHERE CONSTR_COMPLETE_RECORDS_MAP.DATA_TABLE_NAME = :tableName"
				+" AND CONSTR_COMPLETE_RECORDS_MAP.DATA_TABLE_ID_VALUE = :tableId")
				.setParameter("tableName", tableName)
				.setParameter("tableId", id).executeUpdate();
    	
    	session.createSQLQuery("DELETE FROM APPROVAL_SIGN_MANAGEMENT WHERE CONSTR_COMP_RE_MAP_ID = (SELECT CONSTR_COMP_RE_MAP_ID FROM CONSTR_COMPLETE_RECORDS_MAP"
				+ " WHERE DATA_TABLE_NAME = :tableName"
				+" AND DATA_TABLE_ID_VALUE = :tableId)")
				.setParameter("tableName", tableName)
				.setParameter("tableId", id).executeUpdate();
	}

	public String getUpdateConstrCompleteRecod(Long qualityID, String nameTable) {
		StringBuffer sql = new StringBuffer("UPDATE CONSTR_COMPLETE_RECORDS_MAP set STATUS = 0,LEVEL_ORDER = 1 "
				+ "where DATA_TABLE_NAME = :nameTable AND DATA_TABLE_ID_VALUE = :qualityID");
		SQLQuery queryConstr = getSession().createSQLQuery(sql.toString());
		queryConstr.setParameter("nameTable", nameTable);
		queryConstr.setParameter("qualityID", qualityID);
		queryConstr.executeUpdate();
		
		/*getSession().createSQLQuery("DELETE FROM APPROVAL_SIGN_MANAGEMENT WHERE CONSTR_COMP_RE_MAP_ID = (SELECT CONSTR_COMP_RE_MAP_ID FROM CONSTR_COMPLETE_RECORDS_MAP"
				+ " WHERE DATA_TABLE_NAME = :tableName"
				+" AND DATA_TABLE_ID_VALUE = :tableId)")
				.setParameter("tableName", nameTable)
				.setParameter("tableId", qualityID).executeUpdate();*/
		return null;
	}

	public boolean checkBia(Long constructId) {
		StringBuffer sql = new StringBuffer(
                "select CONSTR_WO_LOGS_LAB_ID constrWoLogsLabId from CONSTR_WORK_LOGS_LABEL where CONSTRUCT_ID = :constructId");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.addScalar("constrWoLogsLabId", new LongType());
        query.setParameter("constructId", constructId);
        Long id = (Long) query.uniqueResult();
        if (id == null || id == 0l) {
            return false;
        } else {
        	 return true;
        }
	}

	public List<RoleConfigProvinceUserCDTDTO> getListEmployeeCDT(String provinceCode) {
		StringBuilder sql = new StringBuilder("SELECT rucd.FULL_NAME fullName, rucd.EMAIL email, rucd.SYS_USER_ID sysUserId, rcp.CAT_PROVINCE_CODE catProvinceCode, rcp.CAT_PROVINCE_ID catProvinceId "
				+ " FROM ROLE_CONFIG_PROVINCE_USER_CDT rucd "
				+ "INNER JOIN ROLE_CONFIG_PROVINCE rcp "
				+ "ON rucd.ROLE_CONFIG_PROVINCE_ID = rcp.ROLE_CONFIG_PROVINCE_ID "
				+ "WHERE rcp.STATUS = 1 ");
		if(StringUtils.isNotEmpty(provinceCode)) {
			sql.append(" and rcp.CAT_PROVINCE_CODE =:provinceCode");
		}
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("fullName", new StringType());
		query.addScalar("email", new StringType());
		query.addScalar("sysUserId", new LongType());
		query.addScalar("catProvinceCode", new StringType());
		query.addScalar("catProvinceId", new LongType());
		if(StringUtils.isNotEmpty(provinceCode)) {
			query.setParameter("provinceCode", provinceCode);
		}
		query.setResultTransformer(Transformers.aliasToBean(RoleConfigProvinceUserCDTDTO.class));
		return query.list();
	}

	public List<RoleConfigProvinceUserCTCTDTO> getListEmployeeCTCT(String provinceCode) {
		StringBuilder sql = new StringBuilder("SELECT ruct.FULL_NAME fullName, ruct.EMAIL email, ruct.SYS_USER_ID sysUserId, rcp.CAT_PROVINCE_CODE catProvinceCode, rcp.CAT_PROVINCE_ID catProvinceId "
				+ "FROM ROLE_CONFIG_PROVINCE_USER_CTCT ruct "
				+ "INNER JOIN ROLE_CONFIG_PROVINCE rcp "
				+ "ON ruct.ROLE_CONFIG_PROVINCE_ID = rcp.ROLE_CONFIG_PROVINCE_ID "
				+ "AND rcp.STATUS = 1 ");
		if(StringUtils.isNotEmpty(provinceCode)) {
			sql.append("and rcp.CAT_PROVINCE_CODE =:provinceCode");
		}
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("fullName", new StringType());
		query.addScalar("email", new StringType());
		query.addScalar("sysUserId", new LongType());
		query.addScalar("catProvinceCode", new StringType());
		query.addScalar("catProvinceId", new LongType());
		if(StringUtils.isNotEmpty(provinceCode)) {
			query.setParameter("provinceCode", provinceCode);
		}
		query.setResultTransformer(Transformers.aliasToBean(RoleConfigProvinceUserCTCTDTO.class));
		return query.list();
	}

	
	
	//HienLT56 end 17082020
}
