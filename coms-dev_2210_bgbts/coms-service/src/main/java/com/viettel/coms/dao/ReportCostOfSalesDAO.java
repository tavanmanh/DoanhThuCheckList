package com.viettel.coms.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.viettel.coms.dto.ReportCostofSalesDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

@Repository("reportCostOfSalesDAO")
public class ReportCostOfSalesDAO extends BaseFWDAOImpl<BaseFWModelImpl, Serializable>{

	
	public List<ReportCostofSalesDTO> doSearchDetailContract(ReportCostofSalesDTO obj){
		SQLQuery query = null;
		SQLQuery queryCount = null;
			StringBuilder sql = new StringBuilder(" SELECT ");
			sql.append(" cp.AREA_CODE area, sys.PROVINCE_CODE provinceCode, ");
			sql.append(" a.CONTENT contendContract, cpt.name cDT, ");
			sql.append(" a.code contractNumber , ROUND((SUM(a.PRICE)/1.1),2) prirceContract, ");
			sql.append(" a.SIGN_DATE signDate, a.START_TIME startDate, a.NUM_DAY dayNumber, a.END_TIME endDate, ");
			sql.append(" (CASE WHEN a.B2B_B2C = 1 THEN 'B2B' WHEN a.B2B_B2C = 2 THEN 'B2C' ELSE NULL END ) filter, ");
			sql.append(" to_char(a.APPROVED_DATE,'MM_YYYY') recordedInMonth, ");
			sql.append(" su.LOGIN_NAME employeeCode, su.FULL_NAME employeeName, ");
			sql.append(" su.POSITION_NAME tilte, ROUND(((SUM(a.PRICE)/1.1)*(0.5/100)),2) costOfSales ");
			sql.append(" FROM CTCT_IMS_OWNER.CNT_CONTRACT a ");
			sql.append(" left join CTCT_CAT_OWNER.SYS_GROUP sys on a.SYS_GROUP_ID=sys.SYS_GROUP_ID ");
			sql.append(" left join CTCT_VPS_OWNER.SYS_USER su on a.USER_SEARCH_ID=su.SYS_USER_ID ");
			sql.append(" left join CTCT_CAT_OWNER.CAT_PARTNER cpt on a.CAT_PARTNER_ID=cpt.CAT_PARTNER_ID ");
			sql.append(" left join CTCT_CAT_OWNER.CAT_PROVINCE cp on sys.PROVINCE_CODE = cp.CODE ");
			sql.append(" left join CTCT_COMS_OWNER.WO w on w.CONTRACT_CODE=a.CODE  ");
			sql.append(" Where a.CONTRACT_TYPE_O=4 and a.STATUS>0 and a.CHANNEL=1 AND a.CHECK_WO_KC_XDDD=1 ");
			sql.append(" AND w.CAT_WORK_ITEM_TYPE_ID=2563 and a.CONTRACT_TYPE_O=4 and w.STATE='OK' and w.WO_TYPE_ID=1 and a.STATUS>0 and a.CHANNEL=1 ");
			if(StringUtils.isNotBlank(obj.getMonthYear())) {
				sql.append(" AND TO_CHAR(a.APPROVED_DATE,'MM/YYYY') = :approveDate ");
			}
			if(StringUtils.isNotBlank(obj.getYear())) {
				sql.append(" AND TO_CHAR(a.APPROVED_DATE,'YYYY') = :year ");
			}
			if(StringUtils.isNotBlank(obj.getProvinceCode())) {
				sql.append(" AND upper(sys.PROVINCE_CODE) = upper(:provinceCode) ");
			}
			if(obj.getUserSearchId() != null) {
				sql.append(" AND a.USER_SEARCH_ID = :userId ");
			}
			sql.append(" group by cp.AREA_CODE, a.PRICE_VAT,a.CODE, sys.PROVINCE_CODE, a.CONTENT,a.SIGN_DATE,a.START_TIME, a.NUM_DAY, a.END_TIME,a.B2B_B2C,a.APPROVED_DATE, su.LOGIN_NAME,su.FULL_NAME,su.POSITION_NAME,a.CREATED_DATE,cpt.name ");
			
			sql.append(" order by a.CREATED_DATE desc ");
			StringBuilder sqlCount = new StringBuilder("select count(*),sum(prirceContract), sum(costOfSales) from (" + sql.toString() +")");
			
			query = getSession().createSQLQuery(sql.toString());
			queryCount = getSession().createSQLQuery(sqlCount.toString());
			if(StringUtils.isNotBlank(obj.getMonthYear())) {
				query.setParameter("approveDate", obj.getMonthYear());
				queryCount.setParameter("approveDate", obj.getMonthYear());
			}
			if(StringUtils.isNotBlank(obj.getYear())) {
				query.setParameter("year", obj.getYear().trim());
				queryCount.setParameter("year", obj.getYear().trim());
			}
			if(StringUtils.isNotBlank(obj.getProvinceCode())) {
				query.setParameter("provinceCode",  obj.getProvinceCode());
				queryCount.setParameter("provinceCode",  obj.getProvinceCode());
			}
			if(obj.getUserSearchId() != null) {
				query.setParameter("userId",  obj.getUserSearchId());
				queryCount.setParameter("userId",  obj.getUserSearchId());
			}
			
			query.addScalar("area", new StringType());
			query.addScalar("provinceCode", new StringType());
			query.addScalar("contendContract", new StringType());
			query.addScalar("cDT", new StringType());
			query.addScalar("contractNumber", new StringType());
			query.addScalar("filter", new StringType());
			query.addScalar("recordedInMonth", new StringType());
			query.addScalar("employeeCode", new StringType());
			query.addScalar("employeeName", new StringType());
			query.addScalar("prirceContract", new DoubleType());
			query.addScalar("costOfSales", new DoubleType());
			query.addScalar("dayNumber", new LongType());
			query.addScalar("signDate", new DateType());
			query.addScalar("startDate", new DateType());
			query.addScalar("endDate", new DateType());
			
		query.setResultTransformer(Transformers.aliasToBean(ReportCostofSalesDTO.class));
		
		if (obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		
		Object[] ob = (Object[]) queryCount.uniqueResult();
		obj.setTotalRecord(((BigDecimal) ob[0]).intValue());
		
		List<ReportCostofSalesDTO> lstReturn = new ArrayList<>();
		if (obj.getPage() == null || obj.getPage() == 0 || obj.getPage() == 1) {
		ReportCostofSalesDTO dto = new ReportCostofSalesDTO();
		dto.setPrirceContract(ob[1] != null ? Double.valueOf(ob[1].toString()) : 0d);
		dto.setCostOfSales(ob[2] != null ? Double.valueOf(ob[2].toString()) : 0d);
		dto.setArea("Tổng");
		lstReturn.add(dto);
		}
		
		List<ReportCostofSalesDTO> lst = query.list();
		lstReturn.addAll(lst);
		return lstReturn; 
	}
	
	public List<ReportCostofSalesDTO> doSearchTHProvince(ReportCostofSalesDTO obj){
		SQLQuery query = null;
		SQLQuery queryCount = null;
			StringBuilder sql = new StringBuilder(" SELECT ");
			sql.append(" sys.PROVINCE_CODE provinceCode, ");
			sql.append(" ROUND((SUM(a.PRICE)/1.1)*(0.5/100)*(80/100),2) employee, ");
			sql.append(" ROUND(((SUM(a.PRICE)/1.1)*(0.5/100)*(2/100)),2) giamdoc, ");
			sql.append(" ROUND(((SUM(a.PRICE)/1.1)*(0.5/100)*(0/100)),2) pgdkythuat, ");
			sql.append(" ROUND(((SUM(a.PRICE)/1.1)*(0.5/100)*(2/100)),2) pgdhatang, ");
			sql.append(" ROUND(((SUM(a.PRICE)/1.1)*(0.5/100)*(1.5/100)),2) pgdkinhdoanh, ");
			sql.append(" ROUND(((SUM(a.PRICE)/1.1)*(0/100)*(2/100)),2) phongkythuat, ");
			sql.append(" ROUND(((SUM(a.PRICE)/1.1)*(0.5/100)*(5/100)),2) phonghatang, ");
			sql.append(" ROUND(((SUM(a.PRICE)/1.1)*(0.5/100)*(5/100)),2) phongkinhdoanh, ");
			sql.append(" ROUND(((SUM(a.PRICE)/1.1)*(0.5/100)*(1.5/100)),2) khoihotro, ");
			sql.append(" ROUND(((SUM(a.PRICE)/1.1)*(0.5/100)*(3/100)),2) gdttqh, ");
			sql.append(" ROUND((SUM(a.PRICE)/1.1)*(0.5/100), 2) totalMoney ");
			sql.append(" FROM CTCT_IMS_OWNER.CNT_CONTRACT a  ");
			sql.append(" left join CTCT_CAT_OWNER.SYS_GROUP sys on a.SYS_GROUP_ID=sys.SYS_GROUP_ID ");
			sql.append(" left join CTCT_VPS_OWNER.SYS_USER su on a.USER_SEARCH_ID=su.SYS_USER_ID ");
			sql.append(" left join CTCT_COMS_OWNER.WO w on w.CONTRACT_CODE=a.CODE  ");
			sql.append(" Where a.CONTRACT_TYPE_O=4 and a.STATUS>0 and a.CHANNEL=1 AND a.CHECK_WO_KC_XDDD=1 ");
			sql.append(" AND w.CAT_WORK_ITEM_TYPE_ID=2563 and a.CONTRACT_TYPE_O=4 and w.STATE='OK' and w.WO_TYPE_ID=1 and a.STATUS>0 and a.CHANNEL=1 ");
			if(StringUtils.isNotBlank(obj.getMonthYear())) {
				sql.append(" AND TO_CHAR(a.APPROVED_DATE,'MM/YYYY') = :approveDate ");
			}
			if(StringUtils.isNotBlank(obj.getYear())) {
				sql.append(" AND TO_CHAR(a.APPROVED_DATE,'YYYY') = :year ");
			}
			if(StringUtils.isNotBlank(obj.getProvinceCode())) {
				sql.append(" AND upper(sys.PROVINCE_CODE) = upper(:provinceCode) ");
			}
			sql.append(" group by sys.PROVINCE_CODE ");
			sql.append(" order by sys.PROVINCE_CODE ");
			StringBuilder sqlCount = new StringBuilder(" SELECT ");
			sqlCount.append(" SUM(giamdoc) giamdoc, SUM(pgdkythuat) pgdkythuat, ");
			sqlCount.append(" SUM(pgdhatang) pgdhatang, SUM(pgdkinhdoanh) pgdkinhdoanh, ");
			sqlCount.append(" SUM(phongkythuat) phongkythuat, SUM(phonghatang) phonghatang, ");
			sqlCount.append(" SUM(phongkinhdoanh) phongkinhdoanh, SUM(khoihotro) khoihotro, ");
			sqlCount.append(" SUM(gdttqh) gdttqh, SUM(employee) employee, ");
			sqlCount.append(" SUM(giamdoc + pgdkythuat + pgdhatang + pgdkinhdoanh + phongkythuat + phonghatang + phongkinhdoanh + khoihotro + gdttqh + employee) totalMoney ");
			sqlCount.append(" ,count(*) ");
			
			sqlCount.append(" from (" + sql.toString() +") ");
			
			query = getSession().createSQLQuery(sql.toString());
			queryCount = getSession().createSQLQuery(sqlCount.toString());
			
			if(StringUtils.isNotBlank(obj.getMonthYear())) {
				query.setParameter("approveDate", obj.getMonthYear());
				queryCount.setParameter("approveDate", obj.getMonthYear());
			}
			if(StringUtils.isNotBlank(obj.getYear())) {
				query.setParameter("year", obj.getYear().trim());
				queryCount.setParameter("year", obj.getYear().trim());
			}
			if(StringUtils.isNotBlank(obj.getProvinceCode())) {
				query.setParameter("provinceCode",  obj.getProvinceCode());
				queryCount.setParameter("provinceCode",  obj.getProvinceCode());
			}
			query.addScalar("provinceCode", new StringType());
			query.addScalar("totalMoney", new DoubleType());
			query.addScalar("giamdoc", new DoubleType());
			query.addScalar("pgdkythuat", new DoubleType());
			query.addScalar("pgdhatang", new DoubleType());
			query.addScalar("pgdkinhdoanh", new DoubleType());
			query.addScalar("phongkythuat", new DoubleType());
			query.addScalar("phonghatang", new DoubleType());
			query.addScalar("phongkinhdoanh", new DoubleType());
			query.addScalar("khoihotro", new DoubleType());
			query.addScalar("gdttqh", new DoubleType());
			query.addScalar("employee", new DoubleType());
			
		query.setResultTransformer(Transformers.aliasToBean(ReportCostofSalesDTO.class));
		
		if (obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		
		Object[] ob = (Object[]) queryCount.uniqueResult();
		obj.setTotalRecord(((BigDecimal) ob[11]).intValue());
		
		List<ReportCostofSalesDTO> lstReturn = new ArrayList<>();
		if (obj.getPage() == null || obj.getPage() == 0 || obj.getPage() == 1) {
		ReportCostofSalesDTO dto = new ReportCostofSalesDTO();
		dto.setProvinceCode("Tổng");
		dto.setGiamdoc(ob[0] != null ? Double.valueOf(ob[0].toString()) : 0d);
		dto.setPgdkythuat(ob[1] != null ? Double.valueOf(ob[1].toString()) : 0d);
		dto.setPgdhatang(ob[2] != null ? Double.valueOf(ob[2].toString()) : 0d);
		dto.setPgdkinhdoanh(ob[3] != null ? Double.valueOf(ob[3].toString()) : 0d);
		dto.setPhongkythuat(ob[4] != null ? Double.valueOf(ob[4].toString()) : 0d);
		dto.setPhonghatang(ob[5] != null ? Double.valueOf(ob[5].toString()) : 0d);
		dto.setPhongkinhdoanh(ob[6] != null ? Double.valueOf(ob[6].toString()) : 0d);
		dto.setKhoihotro(ob[7] != null ? Double.valueOf(ob[7].toString()) : 0d);
		dto.setGdttqh(ob[8] != null ? Double.valueOf(ob[8].toString()) : 0d);
		dto.setEmployee(ob[9] != null ? Double.valueOf(ob[9].toString()) : 0d);
		dto.setTotalMoney(ob[10] != null ? Double.valueOf(ob[10].toString()) : 0d);
		lstReturn.add(dto);
		}
		
		List<ReportCostofSalesDTO> lst = query.list();
		lstReturn.addAll(lst);
		return lstReturn; 
	}
	
	public List<ReportCostofSalesDTO> doSearchDetailAllocation(ReportCostofSalesDTO obj){
		SQLQuery query = null;
		SQLQuery queryCount = null;
			StringBuilder sql = new StringBuilder(" SELECT ");
			sql.append(" sys.PROVINCE_CODE provinceCode, a.code contractNumber, ");
			sql.append(" ROUND((SUM(a.PRICE)/1.1),2) prirceContract, ");
			sql.append(" ROUND(((SUM(a.PRICE)/1.1)*(0.5/100)),2) costOfSales, ");
			sql.append(" ROUND(((SUM(a.PRICE)/1.1)*(0.5/100)),2) branchFund, ");
			sql.append(" su.LOGIN_NAME employeeCode, ");
			sql.append(" ROUND(((SUM(a.PRICE)/1.1)*(0.5/100)*(80/100)),2) totalMoney, ");
			sql.append(" ROUND(((SUM(a.PRICE)/1.1)*(0.5/100)*(2/100)),2) giamdoc, ");
			sql.append(" ROUND(((SUM(a.PRICE)/1.1)*(0.5/100)*(0/100)),2) pgdkythuat, ");
			sql.append(" ROUND(((SUM(a.PRICE)/1.1)*(0.5/100)*(2/100)),2) pgdhatang, ");
			sql.append(" ROUND(((SUM(a.PRICE)/1.1)*(0.5/100)*(1.5/100)),2) pgdkinhdoanh, ");
			sql.append(" ROUND(((SUM(a.PRICE)/1.1)*(0/100)*(2/100)),2) phongkythuat, ");
			sql.append(" ROUND(((SUM(a.PRICE)/1.1)*(0.5/100)*(5/100)),2) phonghatang, ");
			sql.append(" ROUND(((SUM(a.PRICE)/1.1)*(0.5/100)*(5/100)),2) phongkinhdoanh, ");
			sql.append(" ROUND(((SUM(a.PRICE)/1.1)*(0.5/100)*(1.5/100)),2) khoihotro, ");
			sql.append(" ROUND(((SUM(a.PRICE)/1.1)*(0.5/100)*(3/100)),2) gdttqh ");
			sql.append(" FROM CTCT_IMS_OWNER.CNT_CONTRACT a  ");
			sql.append(" left join CTCT_CAT_OWNER.SYS_GROUP sys on a.SYS_GROUP_ID=sys.SYS_GROUP_ID ");
			sql.append(" left join CTCT_VPS_OWNER.SYS_USER su on a.USER_SEARCH_ID=su.SYS_USER_ID ");
			sql.append(" left join CTCT_COMS_OWNER.WO w on w.CONTRACT_CODE=a.CODE  ");
			sql.append(" Where a.CONTRACT_TYPE_O=4 and a.STATUS>0 and a.CHANNEL=1 AND a.CHECK_WO_KC_XDDD=1 ");
			sql.append(" AND w.CAT_WORK_ITEM_TYPE_ID=2563 and a.CONTRACT_TYPE_O=4 and w.STATE='OK' and w.WO_TYPE_ID=1 and a.STATUS>0 and a.CHANNEL=1 ");
			if(StringUtils.isNotBlank(obj.getMonthYear())) {
				sql.append(" AND TO_CHAR(a.APPROVED_DATE,'MM/YYYY') = :approveDate ");
			}
			if(StringUtils.isNotBlank(obj.getYear())) {
				sql.append(" AND TO_CHAR(a.APPROVED_DATE,'YYYY') = :year ");
			}
			if(StringUtils.isNotBlank(obj.getProvinceCode())) {
				sql.append(" AND upper(sys.PROVINCE_CODE) = upper(:provinceCode) ");
			}
			sql.append(" group by a.PRICE,a.CODE, sys.PROVINCE_CODE,  su.LOGIN_NAME,a.CREATED_DATE ");
			sql.append(" order by sys.PROVINCE_CODE ");
			
			StringBuilder sqlCount = new StringBuilder("select count(*),sum(branchFund), sum(totalMoney), ");
			sqlCount.append(" sum(giamdoc), sum(pgdhatang), sum(pgdkinhdoanh), sum(phonghatang), ");
			sqlCount.append(" sum(phongkinhdoanh), sum(khoihotro), sum(gdttqh)");
			sqlCount.append(" from (" + sql.toString() +") ");
			
			query = getSession().createSQLQuery(sql.toString());
			queryCount = getSession().createSQLQuery(sqlCount.toString());
			
			if(StringUtils.isNotBlank(obj.getMonthYear())) {
				query.setParameter("approveDate", obj.getMonthYear());
				queryCount.setParameter("approveDate", obj.getMonthYear());
			}
			if(StringUtils.isNotBlank(obj.getYear())) {
				query.setParameter("year", obj.getYear().trim());
				queryCount.setParameter("year", obj.getYear().trim());
			}
			if(StringUtils.isNotBlank(obj.getProvinceCode())) {
				query.setParameter("provinceCode",  obj.getProvinceCode());
				queryCount.setParameter("provinceCode",  obj.getProvinceCode());
			}
			query.addScalar("provinceCode", new StringType());
			query.addScalar("contractNumber", new StringType());
			query.addScalar("employeeCode", new StringType());
			query.addScalar("prirceContract", new DoubleType());
			query.addScalar("costOfSales", new DoubleType());
			query.addScalar("branchFund", new DoubleType());
			query.addScalar("totalMoney", new DoubleType());
			query.addScalar("giamdoc", new DoubleType());
			query.addScalar("pgdkythuat", new DoubleType());
			query.addScalar("pgdhatang", new DoubleType());
			query.addScalar("pgdkinhdoanh", new DoubleType());
			query.addScalar("phongkythuat", new DoubleType());
			query.addScalar("phonghatang", new DoubleType());
			query.addScalar("phongkinhdoanh", new DoubleType());
			query.addScalar("khoihotro", new DoubleType());
			query.addScalar("gdttqh", new DoubleType());
			
		query.setResultTransformer(Transformers.aliasToBean(ReportCostofSalesDTO.class));
		
		
		if (obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		
		Object[] ob = (Object[]) queryCount.uniqueResult();
		obj.setTotalRecord(((BigDecimal) ob[0]).intValue());
		
		List<ReportCostofSalesDTO> lstReturn = new ArrayList<>();
		if (obj.getPage() == null || obj.getPage() == 0 || obj.getPage() == 1) {
		ReportCostofSalesDTO dto = new ReportCostofSalesDTO();
		dto.setProvinceCode("Tổng");
		dto.setBranchFund(ob[1] != null ? Double.valueOf(ob[1].toString()) : 0d);
		dto.setTotalMoney(ob[2] != null ? Double.valueOf(ob[2].toString()) : 0d);
		dto.setGiamdoc(ob[3] != null ? Double.valueOf(ob[3].toString()) : 0d);
		dto.setPgdhatang(ob[4] != null ? Double.valueOf(ob[4].toString()) : 0d);
		dto.setPgdkinhdoanh(ob[5] != null ? Double.valueOf(ob[5].toString()) : 0d);
		dto.setPhonghatang(ob[6] != null ? Double.valueOf(ob[6].toString()) : 0d);
		dto.setPhongkinhdoanh(ob[7] != null ? Double.valueOf(ob[7].toString()) : 0d);
		dto.setKhoihotro(ob[8] != null ? Double.valueOf(ob[8].toString()) : 0d);
		dto.setGdttqh(ob[9] != null ? Double.valueOf(ob[9].toString()) : 0d);
		lstReturn.add(dto);
		}
		
		List<ReportCostofSalesDTO> lst = query.list();
		lstReturn.addAll(lst);
		return lstReturn; 
	}
	
}
