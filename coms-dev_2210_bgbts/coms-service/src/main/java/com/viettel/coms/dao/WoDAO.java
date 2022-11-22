package com.viettel.coms.dao;

import java.math.BigDecimal;
import java.util.*;

import javax.management.Query;

import com.viettel.coms.dto.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.viettel.asset.dto.SysGroupDto;
import com.viettel.coms.bo.WoBO;
import com.viettel.coms.bo.WoMappingWorkItemHtctBO;
import com.viettel.erp.dto.SysUserDTO;
import com.viettel.ktts2.dto.KttsUserSession;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import com.viettel.wms.dto.UserRoleDTO;
import org.springframework.util.ObjectUtils;

@Repository
@Transactional
public class WoDAO extends BaseFWDAOImpl<WoBO, Long> {
	private final String CREATE_NEW_MSG = "Đã tạo mới";
	private final String UPDATED_MSG = "Đã chỉnh sửa";

	private final String ALL_TYPE = "ALL TYPE";
	private final String REPORT_TYPE = "REPORT_TYPE";
	private final String OTHERS = "OTHERS";
	private static final String TYPE_THUE_MAT_BANG = "THUE_MAT_BANG";
	private static final String TYPE_KHOI_CONG = "KHOI_CONG";
	private static final String TYPE_DONG_BO = "DONG_BO";
	private static final String TYPE_PHAT_SONG = "PHAT_SONG";
	private static final String TYPE_HSHC = "HSHC";

	@Autowired
	private ConstructionTaskDAO constructionTaskDao;

	public WoDAO() {
		this.model = new WoBO();
	}

	public WoDAO(Session session) {
		this.session = session;
	}

	public int delete(Long woId) {
		StringBuilder sql = new StringBuilder("UPDATE WO set status = 0 where ID = :woId");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("woId", woId);
		return query.executeUpdate();
	}

	public List<WoDTO> getListWoByLstWos(List<Long> lstWoId) {
		String sqlText = baseSelectStr
				+ " , wt.WO_TYPE_CODE as woTypeCode from WO woTbl left join WO_TYPE wt on woTbl.WO_TYPE_ID = wt.ID \n"
				+ "WHERE 1 = 1\n" + "AND woTbl.status = 1\n";
		if (lstWoId != null && lstWoId.size() > 0) {
			sqlText += "AND woTbl.id IN (:lstWoId)\n";
		}
		sqlText += "ORDER BY woTbl.END_TIME";
		SQLQuery query = getSession().createSQLQuery(sqlText);
		if (lstWoId != null && lstWoId.size() > 0) {
			query.setParameterList("lstWoId", lstWoId);
		}

		query = mapFields(query);
		query.addScalar("woTypeCode", new StringType());

		query.setResultTransformer(Transformers.aliasToBean(WoDTO.class));
		return query.list();
	}

	public List<Long> getListWosByPlanId(WoPlanDTO woPlanDTO) {
		// Get list WO_MAPPING_PLAN
		StringBuilder sql = new StringBuilder("select\n" + "    wmp.WO_ID woId\n" + "    , wo.FT_ID ftId\n"
				+ "from WO_MAPPING_PLAN wmp\n" + "left join WO wo ON wmp.WO_ID = wo.ID\n"
				+ "where wmp.WO_PLAN_ID =:woPlanId order by wo.CREATED_DATE desc");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("woPlanId", woPlanDTO.getId());
		query.addScalar("woId", new LongType());
		query.setResultTransformer(Transformers.aliasToBean(WoMappingChecklistDTO.class));

		List<WoMappingChecklistDTO> lstMappings = query.list();
		List<Long> lstWoIds = new ArrayList<>();
		for (WoMappingChecklistDTO iObj : lstMappings) {
			lstWoIds.add(iObj.getWoId());
		}

		return lstWoIds;
	}

	// Báo cáo KH tháng OS
	public List<ReportWoDTO> doSearchReport(ReportWoDTO obj) {
		StringBuilder sql = new StringBuilder("SELECT\n" + "    b.name,\n" + "    b.month,\n" + "    b.year,\n"
				+ "    b.sysgroupid,\n" + "    b.quantitymonth quantitymonth,\n" + "    b.quantitywo quantitywo,\n"
				+ "    CASE\n" + "            WHEN b.quantitymonth > 0\n"
				+ "                 AND b.quantitywo > 0 THEN nvl(round(DECODE(b.quantitymonth,0,0,100 * b.quantitywo / b.quantitymonth),2),0)\n"
				+ "            ELSE 0\n" + "        END\n" + "    quantitypercent,\n"
				+ "    b.salarymonth salarymonth,\n" + "    b.salarywo salarywo,\n" + "    CASE\n"
				+ "            WHEN b.salarymonth > 0\n"
				+ "                 AND b.salarywo > 0 THEN nvl(round(DECODE(b.salarymonth,0,0,100 * b.salarywo / b.salarymonth),2),0)\n"
				+ "            ELSE 0\n" + "        END\n" + "    salarypercent,\n" + "    b.filemonth filemonth,\n"
				+ "    b.filewo filewo,\n" + "    CASE\n" + "            WHEN b.filemonth > 0\n"
				+ "                 AND b.filewo > 0 THEN nvl(round(DECODE(b.filemonth,0,0,100 * b.filewo / b.filemonth),2),0)\n"
				+ "            ELSE 0\n" + "        END\n" + "    filepercent,\n" + "    b.moneymonth moneymonth,\n"
				+ "    b.moneywo moneywo,\n" + "    CASE\n" + "            WHEN b.moneymonth > 0\n"
				+ "                 AND b.moneywo > 0 THEN nvl(round(DECODE(b.moneymonth,0,0,100 * b.moneywo / b.moneymonth),2),0)\n"
				+ "            ELSE 0\n" + "        END\n" + "    moneypercent, \n" + "    b.provinceth  provinceth,\n"
				+ "    CASE\n" + "            WHEN b.filemonth > 0\n"
				+ "                 AND b.provinceth > 0 THEN nvl(round(DECODE(b.filemonth,0,0,100 * b.provinceth / b.filemonth),2),0)\n"
				+ "            ELSE 0\n" + "        END\n" + "    provincethpercent\n" + "FROM\n" + "    (\n"
				+ "        SELECT\n" + "            name,\n" + "            month,\n" + "            year,\n"
				+ "            sysgroupid,\n" + "            ( qtslxlkh + slkh + slgpdnkh + slxdddkh ) quantitymonth,\n"
				+ "            ( qtslxlwo + slwo + slgpdnwo + slxdddwo ) quantitywo,\n"
				+ "            salarymonth salarymonth,\n" + "            salarywo salarywo,\n"
				+ "            ( hshcxaylapkh + qtkh + qtgpdnkh + qtxdddkh + qthtctkh ) filemonth,\n"
				+ "            ( hshcxaylapwo + qtwo + qtgpdnwo + qtxdddwo + qthtctwo ) filewo,\n"
				+ "            thdtkh moneymonth,\n" + "            thdtwo moneywo,provinceth  provinceth\n"
				+ "        FROM\n" + "            rp_wo_month\n" + "    ) b\n" + "WHERE\n" + "    1 = 1");
		if (obj.getYear() != null && obj.getMonth() != null) {
			sql.append(" AND b.month = :month AND b.year = :year");
		}
		if (obj.getSysGroupId() != null) {
			sql.append(" AND b.sysGroupId =:sysGroupId ");
		}
		sql.append(" ORDER BY name");
		// hoanm1_20200828_start
		StringBuilder sqlQuerySum = new StringBuilder(
				" SELECT SUM(quantityMonth) quantityMonthTotal ,SUM(quantityWo) quantityWoTotal, "
						+ " nvl(round(DECODE(sum(quantitymonth),0,0,100 * sum(quantitywo) / sum(quantitymonth)),2),0) quantityPercentTotal,\n"
						+ " SUM(salaryMonth) salaryMonthTotal ,SUM(salaryWo) salaryWoTotal, "
						+ " nvl(round(DECODE(sum(salaryMonth),0,0,100 * sum(salaryWo) / sum(salaryMonth)),2),0) salaryPercentTotal ,"
						+ " SUM(fileMonth) fileMonthTotal ,SUM(fileWo) fileWoTotal, "
						+ " nvl(round(DECODE(sum(fileMonth),0,0,100 * sum(fileWo) / sum(fileMonth)),2),0)  filePercentTotal,\n"
						+ " SUM(moneyMonth) moneyMonthTotal ,SUM(moneyWo) moneyWoTotal, "
						+ " nvl(round(DECODE(sum(moneyMonth),0,0,100 * sum(moneyWo) / sum(moneyMonth)),2),0) moneyPercentTotal, "
						+ " sum(provinceTH) provinceTHTotal , nvl(round(DECODE(sum(fileMonth),0,0,100 * sum(provinceTH) / sum(fileMonth)),2),0) provinceTHPercentTotal FROM(");
		// hoanm1_20200828_end
		sqlQuerySum.append(sql);
		sqlQuerySum.append(")");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery querySum = getSession().createSQLQuery(sqlQuerySum.toString());
		if (obj.getMonth() != null) {
			query.setParameter("month", obj.getMonth());
			querySum.setParameter("month", obj.getMonth());
		}
		if (obj.getYear() != null) {
			query.setParameter("year", obj.getYear());
			querySum.setParameter("year", obj.getYear());
		}
		if (obj.getSysGroupId() != null) {
			query.setParameter("sysGroupId", obj.getSysGroupId());
			querySum.setParameter("sysGroupId", obj.getSysGroupId());
		}
		query.addScalar("name", new StringType());
		query.addScalar("year", new LongType());
		query.addScalar("month", new LongType());
		query.addScalar("quantityMonth", new DoubleType());
		query.addScalar("quantityWo", new DoubleType());
		query.addScalar("quantityPercent", new DoubleType());
		query.addScalar("salaryMonth", new DoubleType());
		query.addScalar("salaryWo", new DoubleType());
		query.addScalar("salaryPercent", new DoubleType());
		query.addScalar("fileMonth", new DoubleType());
		query.addScalar("fileWo", new DoubleType());
		query.addScalar("filePercent", new DoubleType());
		query.addScalar("moneyMonth", new DoubleType());
		query.addScalar("moneyWo", new DoubleType());
		query.addScalar("moneyPercent", new DoubleType());
		query.addScalar("provinceTH", new DoubleType());
		query.addScalar("provinceTHPercent", new DoubleType());
		query.setResultTransformer(Transformers.aliasToBean(ReportWoDTO.class));
		// total
		querySum.addScalar("quantityMonthTotal", new DoubleType());
		querySum.addScalar("quantityWoTotal", new DoubleType());
		querySum.addScalar("quantityPercentTotal", new DoubleType());
		querySum.addScalar("salaryMonthTotal", new DoubleType());
		querySum.addScalar("salaryWoTotal", new DoubleType());
		querySum.addScalar("salaryPercentTotal", new DoubleType());
		querySum.addScalar("fileMonthTotal", new DoubleType());
		querySum.addScalar("provinceTHTotal", new DoubleType());
		querySum.addScalar("provinceTHPercentTotal", new DoubleType());
		querySum.addScalar("fileWoTotal", new DoubleType());
		querySum.addScalar("filePercentTotal", new DoubleType());
		querySum.addScalar("moneyMonthTotal", new DoubleType());
		querySum.addScalar("moneyWoTotal", new DoubleType());
		querySum.addScalar("moneyPercentTotal", new DoubleType());

		List<ReportWoDTO> lst = query.list();
		obj.setTotalRecord(lst.size());
		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		List<ReportWoDTO> lstNew = query.list();
		if (lstNew.size() > 0) {
			List<Object[]> rs = querySum.list();
			for (Object[] objects : rs) {
				lstNew.get(0).setQuantityMonthTotal((Double) objects[0]);
				lstNew.get(0).setQuantityWoTotal((Double) objects[1]);
				lstNew.get(0).setQuantityPercentTotal((Double) objects[2]);
				lstNew.get(0).setSalaryMonthTotal((Double) objects[3]);
				lstNew.get(0).setSalaryWoTotal((Double) objects[4]);
				lstNew.get(0).setSalaryPercentTotal((Double) objects[5]);
				lstNew.get(0).setFileMonthTotal((Double) objects[6]);
				lstNew.get(0).setProvinceTHTotal((Double) objects[7]);
				lstNew.get(0).setProvinceTHPercentTotal((Double) objects[8]);
				lstNew.get(0).setFileWoTotal((Double) objects[9]);
				lstNew.get(0).setFilePercentTotal((Double) objects[10]);
				lstNew.get(0).setMoneyMonthTotal((Double) objects[11]);
				lstNew.get(0).setMoneyWoTotal((Double) objects[12]);
				lstNew.get(0).setMoneyPercentTotal((Double) objects[13]);
			}
		}
		return lstNew;
	}

	// Báo cáo KH tháng OS
	public List<RpWoDetailOsDTO> getDataExportFile(ReportWoDTO obj) {
		StringBuilder sql = new StringBuilder("SELECT\n" + "    a.name,\n" + "    a.month,\n" + "    a.year,\n"
				+ "    a.sysgroupid,\n" + "    a.salarymonth salarymonth,\n" + "    a.salarywo salarywo,\n"
				+ "    CASE\n" + "            WHEN a.salarymonth > 0\n"
				+ "                 AND a.salarywo > 0 THEN nvl(round(DECODE(a.salarymonth,0,0,100 * a.salarywo / a.salarymonth),2),0)\n"
				+ "            ELSE 0\n" + "        END\n" + "    salarypercent,\n"
				+ "    a.hshcxaylapkh hshcxaylapkh,\n" + "    a.hshcxaylapwo hshcxaylapwo,\n" + "    CASE\n"
				+ "            WHEN a.hshcxaylapkh > 0\n"
				+ "                 AND a.hshcxaylapwo > 0 THEN nvl(round(DECODE(a.hshcxaylapkh,0,0,100 * a.hshcxaylapwo / a.hshcxaylapkh),2),0)\n"
				+ "            ELSE 0\n" + "        END\n" + "    hshcxaylappercent,\n" + "    a.qtkh qtkh,\n"
				+ "    a.qtwo qtwo,\n" + "    CASE\n" + "            WHEN a.qtkh > 0\n"
				+ "                 AND a.qtwo > 0 THEN nvl(round(DECODE(a.qtkh,0,0,100 * a.qtwo / a.qtkh),2),0)\n"
				+ "            ELSE 0\n" + "        END\n" + "    qtpercent,\n" + "    a.qtgpdnkh qtgpdnkh,\n"
				+ "    a.qtgpdnwo qtgpdnwo,\n" + "    CASE\n" + "            WHEN a.qtgpdnkh > 0\n"
				+ "                 AND a.qtgpdnwo > 0 THEN nvl(round(DECODE(a.qtgpdnkh,0,0,100 * a.qtgpdnwo / a.qtgpdnkh),2),0)\n"
				+ "            ELSE 0\n" + "        END\n" + "    qtgpdnpercent,\n" + "    a.qtxdddkh qtxdddkh,\n"
				+ "    a.qtxdddwo qtxdddwo,\n" + "    CASE\n" + "            WHEN a.qtxdddkh > 0\n"
				+ "                 AND a.qtxdddwo > 0 THEN nvl(round(DECODE(a.qtxdddkh,0,0,100 * a.qtxdddwo / a.qtxdddkh),2),0)\n"
				+ "            ELSE 0\n" + "        END\n" + "    qtxdddpercent,\n" + "    a.qthtctkh qthtctkh,\n"
				+ "    a.qthtctwo qthtctwo,\n" + "    CASE\n" + "            WHEN a.qthtctkh > 0\n"
				+ "                 AND a.qthtctwo > 0 THEN nvl(round(DECODE(a.qthtctkh,0,0,100 * a.qthtctwo / a.qthtctkh),2),0)\n"
				+ "            ELSE 0\n" + "        END\n" + "    qthtctpercent,\n" + "    a.qtslxlkh qtslxlkh,\n"
				+ "    a.qtslxlwo qtslxlwo,\n" + "    CASE\n" + "            WHEN a.qtslxlkh > 0\n"
				+ "                 AND a.qtslxlwo > 0 THEN nvl(round(DECODE(a.qtslxlkh,0,0,100 * a.qtslxlwo / a.qtslxlkh),2),0)\n"
				+ "            ELSE 0\n" + "        END\n" + "    qtslxlpercent,\n" + "    a.slkh slkh,\n"
				+ "    a.slwo slwo,\n" + "    CASE\n" + "            WHEN a.slkh > 0\n"
				+ "                 AND a.slwo > 0 THEN nvl(round(DECODE(a.slkh,0,0,100 * a.slwo / a.slkh),2),0)\n"
				+ "            ELSE 0\n" + "        END\n" + "    slpercent,\n" + "    a.slgpdnkh slgpdnkh,\n"
				+ "    a.slgpdnwo slgpdnwo,\n" + "    CASE\n" + "            WHEN a.slgpdnkh > 0\n"
				+ "                 AND a.slgpdnwo > 0 THEN nvl(round(DECODE(a.slgpdnkh,0,0,100 * a.slgpdnwo / a.slgpdnkh),2),0)\n"
				+ "            ELSE 0\n" + "        END\n" + "    slgpdnpercent,\n" + "    a.slxdddkh slxdddkh,\n"
				+ "    a.slxdddwo slxdddwo,\n" + "    CASE\n" + "            WHEN a.slxdddkh > 0\n"
				+ "                 AND a.slxdddwo > 0 THEN nvl(round(DECODE(a.slxdddkh,0,0,100 * a.slxdddwo / a.slxdddkh),2),0)\n"
				+ "            ELSE 0\n" + "        END\n" + "    slxdddpercent,\n" + "    a.tkvkh tkvkh,\n"
				+ "    a.tkvwo tkvwo,\n" + "    CASE\n" + "            WHEN a.tkvkh > 0\n"
				+ "                 AND a.tkvwo > 0 THEN nvl(round(DECODE(a.tkvkh,0,0,100 * a.tkvwo / a.tkvkh),2),0)\n"
				+ "            ELSE 0\n" + "        END\n" + "    tkvpercent,\n" + "    a.thdtkh thdtkh,\n"
				+ "    a.thdtwo thdtwo,\n" + "    CASE\n" + "            WHEN a.thdtkh > 0\n"
				+ "                 AND a.thdtwo > 0 THEN nvl(round(DECODE(a.thdtkh,0,0,100 * a.thdtwo / a.thdtkh),2),0)\n"
				+ "            ELSE 0\n" + "        END\n" + "    thdtpercent,\n" + "    a.ttkkh ttkkh,\n"
				+ "    a.ttkwo ttkwo,\n" + "    CASE\n" + "            WHEN a.ttkkh > 0\n"
				+ "                 AND a.ttkwo > 0 THEN nvl(round(DECODE(a.ttkkh,0,0,100 * a.ttkwo / a.ttkkh),2),0)\n"
				+ "            ELSE 0\n" + "        END\n" + "    ttkpercent,\n" + "    a.xdxmkh xdxmkh,\n"
				+ "    a.xdxmwo xdxmwo,\n" + "    CASE\n" + "            WHEN a.xdxmkh > 0\n"
				+ "                 AND a.xdxmwo > 0 THEN nvl(round(DECODE(a.xdxmkh,0,0,100 * a.xdxmwo / a.xdxmkh),2),0)\n"
				+ "            ELSE 0\n" + "        END\n" + "    xdxmpercent,\n" + "    a.xdbkh xdbkh,\n"
				+ "    a.xdbwo xdbwo,\n" + "    CASE\n" + "            WHEN a.xdbkh > 0\n"
				+ "                 AND a.xdbwo > 0 THEN nvl(round(DECODE(a.xdbkh,0,0,100 * a.xdbwo / a.xdbkh),2),0)\n"
				+ "            ELSE 0\n" + "        END\n" + "    xdbpercent,\n" + "    a.tmbkh tmbkh,\n"
				+ "    a.tmbwo tmbwo,\n" + "    CASE\n" + "            WHEN a.tmbkh > 0\n"
				+ "                 AND a.tmbwo > 0 THEN nvl(round(DECODE(a.tmbkh,0,0,100 * a.tmbwo / a.tmbkh),2),0)\n"
				+ "            ELSE 0\n" + "        END\n" + "    tmbpercent\n" + "FROM\n" + "    RP_WO_MONTH a\n"
				+ "WHERE\n" + "    1 = 1 ");
		if (obj.getSysGroupId() != null) {
			sql.append(" AND a.sysGroupId =:sysGroupId ");
		}
		if (obj.getYear() != null && obj.getMonth() != null) {
			sql.append(" AND a.month = :month AND a.year = :year ");
		}
		sql.append(" ORDER BY name ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("name", new StringType());
		query.addScalar("month", new StringType());
		query.addScalar("year", new StringType());
		query.addScalar("sysGroupId", new LongType());
		query.addScalar("salaryMonth", new DoubleType());
		query.addScalar("salaryWo", new DoubleType());
		query.addScalar("salaryPercent", new DoubleType());
		query.addScalar("hshcXayLapKH", new DoubleType());
		query.addScalar("hshcXayLapWo", new DoubleType());
		query.addScalar("hshcXayLapPercent", new DoubleType());
		query.addScalar("qtKH", new DoubleType());
		query.addScalar("qtWo", new DoubleType());
		query.addScalar("qtPercent", new DoubleType());
		query.addScalar("qtGPDNKH", new DoubleType());
		query.addScalar("qtGPDNWo", new DoubleType());
		query.addScalar("qtGPDNPercent", new DoubleType());
		query.addScalar("qtXDDDKH", new DoubleType());
		query.addScalar("qtXDDDWo", new DoubleType());
		query.addScalar("qtXDDDPercent", new DoubleType());
		query.addScalar("qtHTCTKH", new DoubleType());
		query.addScalar("qtHTCTWo", new DoubleType());
		query.addScalar("qtHTCTPercent", new DoubleType());
		query.addScalar("qtSLXLKH", new DoubleType());
		query.addScalar("qtSLXLWo", new DoubleType());
		query.addScalar("qtSLXLPercent", new DoubleType());
		query.addScalar("slKH", new DoubleType());
		query.addScalar("slWo", new DoubleType());
		query.addScalar("slPercent", new DoubleType());
		query.addScalar("slGPDNKH", new DoubleType());
		query.addScalar("slGPDNWo", new DoubleType());
		query.addScalar("slGPDNPercent", new DoubleType());
		query.addScalar("slXDDDKH", new DoubleType());
		query.addScalar("slXDDDWo", new DoubleType());
		query.addScalar("slXDDDPercent", new DoubleType());
		query.addScalar("tkvKH", new DoubleType());
		query.addScalar("tkvWo", new DoubleType());
		query.addScalar("tkvPercent", new DoubleType());
		query.addScalar("thdtKH", new DoubleType());
		query.addScalar("thdtWo", new DoubleType());
		query.addScalar("thdtPercent", new DoubleType());
		query.addScalar("ttkKH", new DoubleType());
		query.addScalar("ttkWo", new DoubleType());
		query.addScalar("ttkPercent", new DoubleType());
		query.addScalar("xdXMKH", new DoubleType());
		query.addScalar("xdXMWo", new DoubleType());
		query.addScalar("xdXMPercent", new DoubleType());
		query.addScalar("xDBKH", new DoubleType());
		query.addScalar("xDBWo", new DoubleType());
		query.addScalar("xDBPercent", new DoubleType());
		query.addScalar("tmbKH", new DoubleType());
		query.addScalar("tmbWo", new DoubleType());
		query.addScalar("tmbPercent", new DoubleType());
		if (obj.getMonth() != null) {
			query.setParameter("month", obj.getMonth());
		}
		if (obj.getYear() != null) {
			query.setParameter("year", obj.getYear());
		}
		if (obj.getSysGroupId() != null) {
			query.setParameter("sysGroupId", obj.getSysGroupId());
		}
		query.setResultTransformer(Transformers.aliasToBean(RpWoDetailOsDTO.class));
		return query.list();
	}

	public List<WoMappingChecklistDTO> getListChecklistsOfWo(Long woId) {
		try {
			StringBuilder sql = new StringBuilder("select\n" + "    wmc.id,\n" + "    wmc.WO_ID woId,\n"
					+ "    wo.WO_CODE || '-' || wo.WO_NAME woName,\n" + "    wmc.CHECKLIST_ID checklistId,\n"
					+ "    nvl(wmc.name, ct.NAME) checklistName,\n" + "    wmc.state,\n"
					+ "    wmc.quantity_by_date quantityByDate,\n" + "    wmc.quantity_length quantityLength,\n"
					+ "    wmc.status, \n" + "    wmc.added_quantity_length addedQuantityLength, \n"
					+ "    wmc.NUM_IMG_REQUIRE numImgRequire, \n" + "    wmc.TTHQ_RESULT tthqResult, \n"
					+ "    wmc.ACTUAL_VALUE actualValue, \n" + "    wmc.DBHT_VUONG dbhtVuong \n"
					+ "from WO_MAPPING_CHECKLIST wmc\n" + "LEFT JOIN WO wo on wmc.WO_ID = wo.ID\n"
					+ "LEFT JOIN CTCT_COMS_OWNER.CAT_TASK ct ON wmc.CHECKLIST_ID = ct.CAT_TASK_ID\n" + "WHERE 1 = 1\n"
					+ "AND wmc.WO_ID = :woId\n" + "AND wmc.status = 1");
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			query.setParameter("woId", woId);
			query.addScalar("numImgRequire", new IntegerType());
			query.addScalar("actualValue", new LongType());
			query.addScalar("tthqResult", new StringType());
			query.addScalar("dbhtVuong", new StringType());
			query = mapFieldsChecklist(query);

			return query.list();
		} catch (Exception ex) {
			//ex.printStackTrace();
		}
		return null;
	}

	public List<WoMappingChecklistDTO> getListGPONChecklistsOfWo(Long woId) {
		StringBuilder sql = new StringBuilder("select\n" + "    wmc.id,\n" + "    wmc.WO_ID woId,\n"
				+ "    wo.WO_CODE || '-' || wo.WO_NAME woName,\n" + "    wmc.CHECKLIST_ID checklistId,\n"
				+ "    wig.TASK_NAME checklistName,\n" + "    wig.AMOUNT totalAmount,\n" + "    wmc.state,\n"
				+ "    wmc.quantity_by_date quantityByDate,\n" + "    wmc.quantity_length quantityLength,\n"
				+ "    wmc.status,\n" + "    wmc.added_quantity_length addedQuantityLength\n"
				+ "from WO_MAPPING_CHECKLIST wmc\n" + "LEFT JOIN WO wo on wmc.WO_ID = wo.ID\n"
				+ "LEFT JOIN WORK_ITEM_GPON wig ON wmc.CHECKLIST_ID = wig.WORK_ITEM_GPON_ID \n" + "WHERE 1 = 1\n"
				+ "AND wmc.WO_ID = :woId\n" + "AND wmc.status = 1");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.setParameter("woId", woId);
		query.addScalar("addedQuantityLength", new IntegerType());
		query.addScalar("totalAmount", new DoubleType());
		query = mapFieldsChecklist(query);

		return query.list();
	}

	public List<WoMappingChecklistDTO> getListAIOChecklistsOfWo(Long woId) {
		StringBuilder sql = new StringBuilder("select\n" + "    wmc.id,\n" + "    wmc.WO_ID woId,\n"
				+ "    wo.WO_CODE || '-' || wo.WO_NAME woName,\n" + "    wmc.CHECKLIST_ID checklistId,\n"
				+ "    acd.PACKAGE_NAME checklistName,\n" + "    wmc.state,\n"
				+ "    wmc.quantity_by_date quantityByDate,\n" + "    wmc.quantity_length quantityLength,\n"
				+ "    wmc.status\n" + "from WO_MAPPING_CHECKLIST wmc\n" + "LEFT JOIN WO wo on wmc.WO_ID = wo.ID\n"
				+ "LEFT JOIN AIO_CONTRACT_DETAIL acd ON wmc.CHECKLIST_ID = acd.CONTRACT_DETAIL_ID \n" + "WHERE 1 = 1\n"
				+ "AND wmc.WO_ID = :woId\n" + "AND wmc.status = 1");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.setParameter("woId", woId);
		query = mapFieldsChecklist(query);

		return query.list();
	}

	public List<WoMappingChecklistDTO> getListBDDKChecklistOfWo(Long woId) {
		StringBuilder sql = new StringBuilder("select\n" + "    wmc.id,\n" + "    wmc.WO_ID woId,\n"
				+ "    wo.WO_CODE || '-' || wo.WO_NAME woName,\n" + "    wmc.CHECKLIST_ID checklistId,\n"
				+ "    wsc.NAME checklistName,\n" + "    wmc.state,\n" + "    wmc.quantity_by_date quantityByDate,\n"
				+ "    wmc.quantity_length quantityLength,\n" + "    wmc.status\n" + "from WO_MAPPING_CHECKLIST wmc\n"
				+ "LEFT JOIN WO wo on wmc.WO_ID = wo.ID\n"
				+ "LEFT JOIN WO_SCHEDULE_CHECKLIST wsc ON wmc.CHECKLIST_ID = wsc.ID \n" + "WHERE 1 = 1\n"
				+ "AND wmc.WO_ID = :woId\n" + "AND wmc.status = 1");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.setParameter("woId", woId);
		query = mapFieldsChecklist(query);

		return query.list();
	}

	private SQLQuery mapFieldsChecklist(SQLQuery query) {
		query.addScalar("id", new LongType());
		query.addScalar("woId", new LongType());
		query.addScalar("woName", new StringType());
		query.addScalar("checklistId", new LongType());
		query.addScalar("checklistName", new StringType());
		query.addScalar("state", new StringType());
		query.addScalar("quantityByDate", new StringType());
		query.addScalar("quantityLength", new DoubleType());
		query.addScalar("status", new LongType());
		query.setResultTransformer(Transformers.aliasToBean(WoMappingChecklistDTO.class));
		return query;
	}

	public void removeAttachOfChecklist(Long woId, Long checklistId) {
		try {
			StringBuilder sql = new StringBuilder(
					"UPDATE WO_MAPPING_ATTACH SET STATUS = 0 WHERE WO_ID = :woId AND CHECKLIST_ID = :checklistId");
			SQLQuery query = getSession().createSQLQuery(sql.toString());
			query.setParameter("woId", woId);
			query.setParameter("checklistId", checklistId);
			query.executeUpdate();
		} catch (Exception ex) {
			//ex.printStackTrace();
		}
	}

	private String baseSelectStr = "select "
			+ "woTbl.ID as woId, woTbl.WO_CODE as woCode, woTbl.WO_NAME as woName, woTbl.WO_TYPE_ID as woTypeId, woTbl.TR_ID as trId, "
			+ " nvl(woTbl.STATE_HTCT, woTbl.STATE) as state, woTbl.CONSTRUCTION_ID as constructionId, woTbl.CAT_WORK_ITEM_TYPE_ID as catWorkItemTypeId, "
			+ "woTbl.STATION_CODE as stationCode, woTbl.USER_CREATED as userCreated, woTbl.CREATED_DATE as createdDate, woTbl.FINISH_DATE as finishDate, "
			+ "woTbl.QOUTA_TIME as qoutaTime, woTbl.EXECUTE_METHOD as executeMethod, woTbl.QUANTITY_VALUE as quantityValue, woTbl.CD_LEVEL_1 as cdLevel1, "
			+ "woTbl.CD_LEVEL_2 as cdLevel2, woTbl.CD_LEVEL_3 as cdLevel3, woTbl.CD_LEVEL_4 as cdLevel4,woTbl.CD_LEVEL_5 as cdLevel5, woTbl.FT_ID as ftId, woTbl.ACCEPT_TIME as acceptTime, woTbl.END_TIME as endTime, "
			+ "woTbl.EXECUTE_LAT as executeLat, woTbl.EXECUTE_LONG as executeLong, woTbl.STATUS as status, "
			+ "woTbl.TOTAL_MONTH_PLAN_ID as totalMonthPlanId, woTbl.MONEY_VALUE as moneyValue, woTbl.MONEY_FLOW_BILL as moneyFlowBill, "
			+ "woTbl.MONEY_FLOW_DATE as moneyFlowDate, woTbl.MONEY_FLOW_VALUE as moneyFlowValue, woTbl.MONEY_FLOW_REQUIRED as moneyFlowRequired, woTbl.MONEY_FLOW_CONTENT as moneyFlowContent, woTbl.AP_CONSTRUCTION_TYPE as apConstructionType, "
			+ "woTbl.AP_WORK_SRC as apWorkSrc, woTbl.OPINION_RESULT as opinionResult, woTbl.EXECUTE_CHECKLIST as executeChecklist, woTbl.WO_NAME_ID as woNameId, "
			+ " woTbl.START_TIME as startTime, woTbl.CONTRACT_ID as contractId, woTbl.QUANTITY_BY_DATE as quantityByDate, woTbl.CLOSED_TIME as closedTime, "
			// + "woTbl.CONSTRUCTION_CODE as constructionCode, " + //Huypq-12072021-comment
			+ " nvl(woTbl.CONSTRUCTION_CODE, case when woTbl.wo_type_id=243 then (select max(cat.code||' / '||staPartner.CAT_PARTNER_STATION_CODE)  from wo_mapping_station staMap,CAT_STATION cat,CAT_PARTNER_STATION staPartner where staMap.wo_id=woTbl.id and staMap.CAT_STATION_id=cat.CAT_STATION_id and staPartner.CAT_STATION_HOUSE_CODE=cat.code and staPartner.status=1 and cat.status=1)else null end) as constructionCode, "
			+ // Huypq-12072021-add
			" woTbl.CONTRACT_CODE as contractCode, woTbl.PROJECT_ID as projectId, woTbl.PROJECT_CODE as projectCode, woTbl.CD_LEVEL_1_NAME as cdLevel1Name, woTbl.CD_LEVEL_2_NAME as cdLevel2Name, woTbl.CD_LEVEL_3_NAME as cdLevel3Name, woTbl.CD_LEVEL_4_NAME as cdLevel4Name, woTbl.CD_LEVEL_5_NAME as cdLevel5Name,"
			+ " woTbl.FT_NAME as ftName, woTbl.FT_EMAIL as ftEmail, woTbl.CREATED_USER_FULL_NAME as createdUserFullName, woTbl.CREATED_USER_EMAIL as createdUserEmail, woTbl.TR_CODE as trCode, woTbl.CAT_CONSTRUCTION_TYPE_ID as catConstructionTypeId, "
			+ " woTbl.USER_CD_LEVEL2_RECEIVE_WO as userCdLevel2ReceiveWo,"
			+ " woTbl.UPDATE_CD_LEVEL2_RECEIVE_WO as updateCdLevel2ReceiveWo,"
			+ " woTbl.USER_CD_LEVEL3_RECEIVE_WO as userCdLevel3ReceiveWo,"
			+ " woTbl.UPDATE_CD_LEVEL3_RECEIVE_WO as updateCdLevel3ReceiveWo,"
			+ " woTbl.USER_CD_LEVEL4_RECEIVE_WO as userCdLevel4ReceiveWo,"
			+ " woTbl.UPDATE_CD_LEVEL4_RECEIVE_WO as updateCdLevel4ReceiveWo, "
			+ " woTbl.USER_CD_LEVEL5_RECEIVE_WO as userCdLevel5ReceiveWo, "
			+ " woTbl.UPDATE_CD_LEVEL5_RECEIVE_WO as updateCdLevel5ReceiveWo, "
			+ " woTbl.USER_FT_RECEIVE_WO as userFtReceiveWo, " + " woTbl.UPDATE_FT_RECEIVE_WO as updateFtReceiveWo, "
			+ " woTbl.HSHC_RECEIVE_DATE as hshcReceiveDate, woTbl.HCQT_CONTRACT_CODE as hcqtContractCode, woTbl.CNKV as cnkv, woTbl.CAT_PROVINCE_CODE as catProvinceCode, woTbl.HCQT_PROJECT_ID as hcqtProjectId, "
			+ " woTbl.OVERDUE_REASON overdueReason, woTbl.OVERDUE_APPROVE_STATE overdueApproveState, woTbl.OVERDUE_APPROVE_PERSON overdueApprovePerson, "
			+ " woTbl.DESCRIPTION description, woTbl.VTNET_WO_CODE vtnetWoCode, woTbl.PARTNER partner,"
			+ " woTbl.WO_ORDER woOrder, woTbl.WO_ORDER_CONFIRM woOrderConfirm, woTbl.VO_STATE voState, woTbl.VO_REQUEST_DEPT voRequestDept, woTbl.VO_REQUEST_ROLE voRequestRole, "
			+ " woTbl.VO_APPROVED_DEPT voApprovedDept, woTbl.VO_APPROVED_ROLE voApprovedRole, woTbl.VO_MNGT_DEPT voMngtDept, woTbl.VO_MNGT_ROLE voMngtRole, woTbl.CREATED_DATE_5S createdDate5s "
			+ " ,woTbl.MONEY_VALUE_HTCT moneyValueHtct, woTbl.USER_DTHT_APPROVED_WO userDthtApprovedWo, woTbl.UPDATE_DTHT_APPROVED_WO updateDthtApprovedWo, woTbl.OPINION_TYPE opinionType ";

	// hoanm1_20200924_start
	private String baseSelectStrReport = "        SELECT\n" + "            wotbl.id AS woId,\n"
			+ "            wotbl.wo_code AS woCode,\n" + "            wotbl.wo_name AS woName,\n"
			+ "            wotbl.wo_type_id AS woTypeId,\n" + "            wotbl.tr_id AS trId,\n"
			+ "            wotbl.state AS state,\n" + "            wotbl.construction_id AS constructionId,\n"
			+ "            wotbl.cat_work_item_type_id AS catWorkItemTypeId,\n"
			+ "            wotbl.station_code AS stationCode,\n" + "            wotbl.user_created AS userCreated,\n"
			+ "            wotbl.created_date AS createdDate,\n" + "            wotbl.finish_date AS finishDate,\n"
			+ "            wotbl.qouta_time AS qoutaTime,\n" + "            wotbl.execute_method AS executeMethod,\n"
			+ "            wotbl.quantity_value AS quantityValue,\n" + "            wotbl.cd_level_1 AS cdLevel1,\n"
			+ "            wotbl.cd_level_2 AS cdLevel2,\n" + "            wotbl.cd_level_3 AS cdLevel3,\n"
			+ "            wotbl.cd_level_4 AS cdLevel4,\n" + "            wotbl.ft_id AS ftId,\n"
			+ "            wotbl.accept_time AS acceptTime,\n" + "            wotbl.end_time AS endTime,\n"
			+ "            wotbl.execute_lat AS executeLat,\n" + "            wotbl.execute_long AS executeLong,\n"
			+ "            wotbl.status AS status,\n" + "            wotbl.total_month_plan_id AS totalMonthPlanId,\n"
			+ "            wotbl.money_value AS moneyValue,\n" + "            wotbl.money_flow_bill AS moneyFlowBill,\n"
			+ "            wotbl.money_flow_date AS moneyFlowDate,\n"
			+ "            wotbl.money_flow_value AS moneyFlowValue,\n"
			+ "            wotbl.money_flow_required AS moneyFlowRequired,\n"
			+ "            wotbl.money_flow_content AS moneyFlowContent,\n"
			+ "            wotbl.ap_construction_type AS apConstructionType,\n"
			+ "            wotbl.ap_work_src AS apWorkSrc,\n" + "            wotbl.opinion_result AS opinionResult,\n"
			+ "            wotbl.execute_checklist AS executeChecklist,\n"
			+ "            wotbl.wo_name_id AS woNameId,\n" + "            wotbl.start_time AS startTime,\n"
			+ "            wotbl.contract_id AS contractId,\n"
			+ "            wotbl.quantity_by_date AS quantityByDate,\n"
			+ "            wotbl.closed_time AS closedTime,\n"
			+ "            wotbl.construction_code AS constructionCode,\n"
			+ "            wotbl.contract_code AS contractCode,\n" + "            wotbl.project_id AS projectId,\n"
			+ "            wotbl.project_code AS projectCode,\n"
			+ "            wotbl.cd_level_1_name AS cdLevel1Name,\n"
			+ "            wotbl.cd_level_2_name AS cdLevel2Name,\n"
			+ "            wotbl.cd_level_3_name AS cdLevel3Name,\n"
			+ "            wotbl.cd_level_4_name AS cdLevel4Name,\n" + "            wotbl.ft_name AS ftName,\n"
			+ "            wotbl.ft_email AS ftEmail,\n"
			+ "            wotbl.created_user_full_name AS createdUserFullName,\n"
			+ "            wotbl.created_user_email AS createdUserEmail,\n" + "            wotbl.tr_code AS trCode,\n"
			+ "            wotbl.cat_construction_type_id AS catConstructionTypeId,\n"
			+ "            wotbl.partner partner,\n" + "            wotbl.wo_order woOrder,\n"
			+ "            wotbl.vtnet_wo_code vtnetWoCode ";
	// hoanm1_20200924_end

	public WoBO getOneRaw(long woId) {
		return this.get(WoBO.class, woId);
	}

	public Long getNextSeqVal() {
		return this.getNextValSequence("WO_SEQ");
	}

	public WoDTO getOneDetails(long woId) {
		StringBuilder sql = new StringBuilder(baseSelectStr);
		sql.append(
				", woTypeTbl.WO_TYPE_NAME as woTypeName, woTypeTbl.WO_TYPE_CODE as woTypeCode, woTbl.HCQT_CONTRACT_CODE as hcqtContractCode, woTbl.TYPE as type ");
		sql.append(
				", trTbl.TR_NAME as trName, trTbl.TR_CODE as trCode, trTbl.EXECUTE_LAT as trExecuteLat, trTbl.EXECUTE_LONG as trExecuteLong ");
		sql.append(
				", trTypeTbl.TR_TYPE_NAME as trTypeName, trTypeTbl.TR_TYPE_CODE as trTypeCode, cwit.NAME as catWorkItemTypeName ");
		sql.append(", appParamTbl1.NAME as apWorkSrcName,  appParamTbl2.NAME as apConstructionTypeName ");
		sql.append(
				", CASE WHEN cth.code is not null then cth.code || ' - ' || cth.address else null end catStationHouseTxt ");
		sql.append(
				", TO_CHAR(woTbl.START_TIME,'dd/MM/yyyy HH:MI:ss') AS startTimeStr, TO_CHAR(woTbl.END_TIME,'dd/MM/yyyy HH:MI:ss') as endTimeStr, woTbl.EMAIL_TC_TCT emailTcTct, "
						+ " TO_CHAR(woTbl.ACCEPT_TIME,'dd/MM/yyyy HH:MI:ss') AS acceptTimeStr, TO_CHAR(woTbl.CREATED_DATE,'dd/MM/yyyy HH:MI:ss') AS createdDateStr, wotbl.checklist_step checklistStep  ");
		sql.append(", cst.CHECK_HTCT checkHTCT ");// Huypq-30062021-add
		sql.append(", woTbl.DESCRIPTION description ");// Duonghv13-11092021-add
		sql.append(
				" , (case when woTbl.OPINION_TYPE='Đề xuất gia hạn' AND woTbl.STATE like '%PAUSE%' then (woTbl.STATE || '-GH') when woTbl.OPINION_TYPE='Đề xuất hủy' AND woTbl.STATE like '%PAUSE%' then (woTbl.STATE || '-HUY') else woTbl.STATE END) stateVuong ");
		sql.append(
				", (SELECT staPartner.CAT_PARTNER_STATION_CODE FROM wo_mapping_station staMap left join CAT_STATION cat ON staMap.CAT_STATION_id =cat.CAT_STATION_id left join CAT_PARTNER_STATION staPartner ON staPartner.CAT_STATION_HOUSE_CODE=cat.code where  staPartner.status =1 AND cat.CODE = woTbl.STATION_CODE AND ROWNUM=1 ) stationVtnet ");
		sql.append(" , cps.CAT_PARTNER_STATION_CODE vtNetStationCode "); // Huypq-29112021-add
		sql.append(", wpa.ORDER_CODE_TGDD orderCodeTgdd ");// thanhpt
		sql.append(", wpa.ORDER_CODE_AVG orderCodeAvg ");// thanhpt
		sql.append(", wpa.CUSTOMER_NAME customerName ");// thanhpt
		sql.append(", wpa.PHONE_NUMBER phoneNumber ");// thanhpt
		sql.append(", wpa.PERSONAL_ID personalId ");// thanhpt
		sql.append(", wpa.ADDRESS address ");// thanhpt
		sql.append(", wpa.PRODUCT_CODE productCode ");// thanhpt
		sql.append(", wpa.PAYMENT_STATUS paymentStatus ");// thanhpt
		sql.append(" from WO woTbl ");

		sql.append(" left join WO_TYPE woTypeTbl on woTypeTbl.ID = woTbl.WO_TYPE_ID ");
		sql.append(" left join WO_TR trTbl on trTbl.ID = woTbl.TR_ID ");
		sql.append(" left join WO_TR_TYPE trTypeTbl on trTypeTbl.ID = trTbl.TR_TYPE_ID ");
		sql.append(" left join CAT_WORK_ITEM_TYPE cwit on woTbl.CAT_WORK_ITEM_TYPE_ID = cwit.CAT_WORK_ITEM_TYPE_ID ");
		sql.append(
				" left join APP_PARAM appParamTbl1 on woTbl.AP_WORK_SRC = appParamTbl1.CODE and appParamTbl1.PAR_TYPE = 'AP_WORK_SRC' ");
		sql.append(
				" left join APP_PARAM appParamTbl2 on woTbl.AP_CONSTRUCTION_TYPE = appParamTbl2.CODE and appParamTbl2.PAR_TYPE = 'AP_CONSTRUCTION_TYPE' ");
		sql.append(" left join CAT_STATION ct ON woTbl.STATION_CODE = ct.CODE ");
		sql.append(" left join CAT_STATION_HOUSE cth on cth.CAT_STATION_HOUSE_ID = ct.CAT_STATION_HOUSE_ID ");
		sql.append(" left join construction cst on woTbl.construction_id = cst.construction_id ");
		sql.append(" left join WO_MAPPING_AVG wpa on woTbl.ID = wpa.WO_ID ");
		sql.append(
				" LEFT JOIN CAT_PARTNER_STATION cps on cth.CODE = cps.CAT_STATION_HOUSE_CODE AND cth.STATUS!=0 AND cps.STATUS !=0 ");
		sql.append(" WHERE woTbl.STATUS>0 AND woTbl.ID = :woId ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("woId", woId);

		query = mapFields(query);
		query.addScalar("woTypeName", new StringType());
		query.addScalar("woTypeCode", new StringType());
		query.addScalar("trTypeName", new StringType());
		query.addScalar("trTypeCode", new StringType());
		query.addScalar("apWorkSrcName", new StringType());
		query.addScalar("apConstructionTypeName", new StringType());
		query.addScalar("trExecuteLong", new StringType());
		query.addScalar("trExecuteLat", new StringType());
		query.addScalar("startTimeStr", new StringType());
		query.addScalar("endTimeStr", new StringType());
		query.addScalar("acceptTimeStr", new StringType());
		query.addScalar("createdDateStr", new StringType());
		query.addScalar("catWorkItemTypeName", new StringType());
		query.addScalar("hcqtContractCode", new StringType());
		query.addScalar("type", new StringType());
		query.addScalar("catStationHouseTxt", new StringType());
		query.addScalar("emailTcTct", new StringType());
		query.addScalar("checklistStep", new LongType());
		query.addScalar("checkHTCT", new StringType()); // Huypq-30062021-add
		query.addScalar("stationVtnet", new StringType()); // taotq-add

		query.addScalar("description", new StringType()); // Duonghv13-11092021-add
		query.addScalar("stateVuong", new StringType());
		query.addScalar("vtNetStationCode", new StringType());
		query.addScalar("orderCodeTgdd", new StringType()); // thanhpt
		query.addScalar("orderCodeAvg", new StringType()); // thanhpt
		query.addScalar("customerName", new StringType()); // thanhpt
		query.addScalar("phoneNumber", new StringType()); // thanhpt
		query.addScalar("personalId", new StringType()); // thanhpt
		query.addScalar("address", new StringType()); // thanhpt
		query.addScalar("productCode", new StringType()); // thanhpt
		query.addScalar("paymentStatus", new StringType()); // thanhpt
		query.setResultTransformer(Transformers.aliasToBean(WoDTO.class));

		List<WoDTO> ls = query.list();
		if (ls.size() > 0) {
			return ls.get(0);
		}
		return null;
	}

	public List<WoDTO> getByRange(long pageNumber, long pageSize) {
		StringBuilder sql = new StringBuilder(baseSelectStr
				+ " from WO woTbl WHERE woTbl.STATUS>0 offset :offset rows fetch next :pageSize rows only ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("pageSize", pageSize);
		query.setParameter("offset", pageSize * (pageNumber - 1));

		query = mapFields(query);
		query.setResultTransformer(Transformers.aliasToBean(WoDTO.class));

		return query.list();
	}

	public List<WoDTO> getListWo(WoDTO woDto) {
		StringBuilder sql = new StringBuilder(baseSelectStr + " from WO woTbl WHERE woTbl.STATUS>0");
		if (woDto.getTrId() != null) {
			sql.append(" AND TR_ID = :trId ");
		}
		if (woDto.getWoTypeId() != null) {
			sql.append(" AND WO_TYPE_ID = :woTypeId ");
		}
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		if (woDto.getTrId() != null) {
			query.setParameter("trId", woDto.getTrId());
		}
		if (woDto.getWoTypeId() != null) {
			query.setParameter("woTypeId", woDto.getWoTypeId());
		}
		query = mapFields(query);
		query.setResultTransformer(Transformers.aliasToBean(WoDTO.class));

		return query.list();
	}
	
	//ducpm
	
	public List<WoDTO> getListUpdateElectric(WoDTO woDTO){
	
		String basicSql = " SELECT "
				+ " se.STATION_CODE stationCode, w.WO_CODE woCode ,dse.DEVICE_NAME deviceName, dse.SERIAL serial,"
				+ " ( CASE"
				+ " WHEN dse.FAILURE_STATUS = 1"
				+ " THEN 'Báo hỏng'"
				+ " WHEN dse.FAILURE_STATUS = 2"
				+ " THEN 'Chờ phê duyệt'"
				+ " WHEN dse.FAILURE_STATUS = 3"
				+ " THEN 'Đang sửa chữa'"
				+ " WHEN dse.FAILURE_STATUS = 4"
				+ " THEN 'Đã hoàn thành chờ hậu kiểm'"
				+ " WHEN dse.FAILURE_STATUS = 5"
				+ " THEN 'Từ chối báo hỏng'"
				+ " ELSE NULL"
				+ " END )status,"
				+ " w.STATE state,"
				+ " se.MANAGE_USER_NAME manageUserName, w.CREATED_DATE createdDate,"
				+ " w.START_TIME startTime, w.END_TIME endTime,"
				+ " w.USER_TTHT_APPROVE_WO userTthtApproveWo,"
				+ " w.UPDATE_TTHT_APPROVE_WO updateTthtApproveWo"
				+ " ,dse.DESCRIPTION_FAILURE descriptionFailure"
				+ " ,w.DESCRIBE_AFTERMATH describeAftemath"
				+ " FROM STATION_ELECTRICAL se left join DEVICE_STATION_ELECTRICAL dse"
				+ " on se.STATION_ID=dse.STATION_ID"
				+ " left join WO w on se.STATION_CODE=w.STATION_CODE"
				+ " left join WO_TYPE wt on w.WO_TYPE_ID=wt.ID"
				+ " WHERE wt.id=501 ";
		//+ " where wt.WO_TYPE_CODE='SUA_CHUA_HT_CO_DIEN'");
		StringBuilder sql = new StringBuilder(basicSql);	
		
		// add điều kiện tìm kiếm
		if(woDTO.getStationCode() != null && !woDTO.getStationCode().equals("")) {
			sql.append(" AND se.STATION_CODE LIKE :stationCode");
		}
		if(woDTO.getWoCode() != null && !woDTO.getWoCode().equals("")) {
			sql.append(" AND w.WO_CODE LIKE :woCode");
		}
		if(woDTO.getDeviceName() != null && !woDTO.getDeviceName().equals("")) {
			sql.append(" AND dse.DEVICE_NAME LIKE :deviceName");
		}
		
		if (woDTO.getFromDate() != null)
			sql.append(" AND trunc(w.START_TIME) >= trunc(:fromDate) ");
		if (woDTO.getToDate() != null)
			sql.append(" AND trunc(w.END_TIME) <= trunc(:toDate) ");
		
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM ( ");
		sqlCount.append(sql.toString() + " )");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
		
		// add Scalar
		query.addScalar("stationCode", new StringType());
		query.addScalar("woCode", new StringType());
		query.addScalar("deviceName", new StringType());
		query.addScalar("serial", new StringType());
		query.addScalar("status", new LongType());
		query.addScalar("state", new StringType());
		query.addScalar("manageUserName", new StringType());
		query.addScalar("createdDate", new DateType());
		query.addScalar("startTime", new DateType());
		query.addScalar("endTime", new DateType());
		query.addScalar("userTthtApproveWo", new StringType());
		query.addScalar("updateTthtApproveWo", new DateType());
		query.addScalar("descriptionFailure", new StringType());
		query.addScalar("describeAftemath", new StringType());
		
		//set paramater cho query
		if(woDTO.getStationCode() != null && !woDTO.getStationCode().equals("")) {
			query.setParameter("stationCode", "%" + woDTO.getStationCode()+"%");
			queryCount.setParameter("stationCode", "%" + woDTO.getStationCode()+"%");
		}
		
		if(woDTO.getWoCode() != null && !woDTO.getWoCode().equals("")) {
			query.setParameter("woCode", "%" + woDTO.getWoCode()+"%");
			queryCount.setParameter("woCode", "%" + woDTO.getWoCode()+"%");
		}
		
		if(woDTO.getDeviceName() != null && !woDTO.getDeviceName().equals("")) {
			query.setParameter("deviceName", "%" + woDTO.getDeviceName()+"%");
			queryCount.setParameter("deviceName", "%" + woDTO.getDeviceName()+"%");
		}
		if (woDTO.getFromDate() != null) {
			query.setParameter("fromDate", woDTO.getFromDate());
			queryCount.setParameter("fromDate", woDTO.getFromDate());
		}
			
		if (woDTO.getToDate() != null) {
			query.setParameter("toDate", woDTO.getToDate());
			queryCount.setParameter("toDate", woDTO.getToDate());
		}
			
		
		query.setResultTransformer(Transformers.aliasToBean(WoDTO.class));
		woDTO.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
	
		if(woDTO.getPage() != null && woDTO.getPageSize() != null) {
			query.setFirstResult((woDTO.getPage().intValue()-1) * woDTO.getPageSize().intValue());
			query.setMaxResults(woDTO.getPageSize().intValue());
		}
		return query.list();
	}
	//ducpm end

	public List<WoDTO> doSearch(WoDTO woDto, List<String> groupIdListView, boolean isTcTct, boolean isTcBranch,
			List<String> contractBranchs) {
		StringBuilder sql = new StringBuilder(baseSelectStr + " , woTbl.type as type , apr.NAME as apWorkSrcName, ");
		sql.append(" wi.NAME as itemName, ");
		sql.append(" wotr.GROUP_CREATED_NAME unitCreateOrBelongToWO, "); // HienLT56 add 01122020
		sql.append(
				" wt.WO_TYPE_NAME as woTypeName, wt.WO_TYPE_CODE as woTypeCode, apr2.NAME as apConstructionTypeName,to_char(woTbl.END_TIME,'dd/MM/yyyy hh24:mi:ss') endTimeStr ");// huypq30_add_20201002
		sql.append(" , cwit.NAME as catWorkItemTypeName ");
		sql.append(
				" , (case when woTbl.OPINION_TYPE='Đề xuất gia hạn' AND woTbl.STATE like '%PAUSE%' then (woTbl.STATE || '-GH') when woTbl.OPINION_TYPE='Đề xuất hủy' AND woTbl.STATE like '%PAUSE%' then (woTbl.STATE || '-HUY') else woTbl.STATE END) stateVuong ");
		sql.append(
				", (select wc.CHECKLIST_NAME from wo_checklist wc left join wo w ON wc.WO_ID = w.ID  where wc.wo_id = woTbl.ID AND  w.WO_TYPE_ID = 261 AND wc.CHECKLIST_ORDER = (select MIN(CHECKLIST_ORDER) from wo_checklist where STATE = 'NEW' AND wo_id = woTbl.ID)) jobUnfinished ");
		sql.append(", su.EMAIL ftEmailSysUser, su.POSITION_NAME ftPositionName ");
		sql.append(
				", (case when woTbl.wo_type_id=243 then (select max(staPartner.CAT_PARTNER_STATION_CODE)  from WO_MAPPING_STATION staMap,CAT_STATION cat,CAT_PARTNER_STATION staPartner where staMap.wo_id=woTbl.id and staMap.CAT_STATION_id=cat.CAT_STATION_id and staPartner.CAT_STATION_HOUSE_CODE=cat.code and staPartner.status=1 and cat.status=1)else null end) as vtNetStationCode ");
		sql.append(", woTbl.APPROVE_DATE_REPORT_WO approveDateReportWo ");
		sql.append(" from WO woTbl ");

		sql.append(" left join WO_TYPE wt on woTbl.WO_TYPE_ID = wt.ID "
				+ " left join CNT_CONTRACT cc on woTbl.contract_id = cc.CNT_CONTRACT_ID and cc.status > 0 "
				+ " left join APP_PARAM apr on to_char(woTbl.AP_WORK_SRC) = apr.CODE AND apr.PAR_TYPE = 'AP_WORK_SRC' "
				+ " left join APP_PARAM apr2 on to_char(woTbl.AP_CONSTRUCTION_TYPE) = apr2.CODE AND apr2.PAR_TYPE = 'AP_CONSTRUCTION_TYPE' "
				+ " left join APP_PARAM apr3 on to_char(woTbl.AP_CONSTRUCTION_TYPE) = apr3.CODE AND apr3.PAR_TYPE = 'AVG_CHECKLIST' "
				+ " left join WORK_ITEM wi on woTbl.CONSTRUCTION_ID = wi.CONSTRUCTION_ID and woTbl.CAT_WORK_ITEM_TYPE_ID = wi.CAT_WORK_ITEM_TYPE_ID ");
		sql.append(" LEFT JOIN WO_TR woTr ON wotbl.TR_ID = woTr.ID "); // HienLT56 add 01122020
		sql.append(" left join CAT_WORK_ITEM_TYPE cwit on woTbl.CAT_WORK_ITEM_TYPE_ID = cwit.CAT_WORK_ITEM_TYPE_ID ");
		sql.append(" left join SYS_USER su on woTbl.FT_ID = su.SYS_USER_ID ");
		sql.append(" WHERE woTbl.STATUS>0 ");
		if (!isTcTct)
			sql.append(" and wt.WO_TYPE_CODE != 'HCQT' ");
		if (woDto.isMobile()) {
			sql.append(" and wt.WO_TYPE_CODE not in ('AIO') ");
		}

		if (StringUtils.isNotEmpty(woDto.getKeySearch())) {
			sql.append(" AND ( LOWER(woTbl.WO_NAME) LIKE :keySearch OR LOWER(woTbl.WO_CODE) LIKE :keySearch ) ");
		}

		if (woDto.getTrId() != null) {
			sql.append(" AND woTbl.TR_ID = :trId ");
		}

		if (woDto.getWoTypeId() != null) {
			sql.append(" AND woTbl.WO_TYPE_ID = :woTypeId ");
		}
		// HienLT56 start 15102020
		if (woDto.getListWoTypeId() != null && woDto.getListWoTypeId().size() > 0) {
			sql.append(" AND woTbl.WO_TYPE_ID in (:listWoTypeId) ");
		}
		// HienLT56 end 15102020

		if (woDto.getWoTypeCode() != null) {
			sql.append(" AND wt.WO_TYPE_CODE = :woTypeCode ");
		}
		if (woDto.getItem() != null) {
			// taotq start 27082021
			// sql.append(" AND wi.CODE = :itemCode ");
			sql.append(" and woTbl.CAT_WORK_ITEM_TYPE_ID= :itemCode ");
			// taotq end 27082021
		}

		if (woDto.getApWorkSrc() != null) {
			sql.append(" AND nvl(woTbl.AP_WORK_SRC,0) = :apWorkSrc ");
		}

		if (woDto.getApConstructionType() != null) {
			sql.append(" AND nvl(woTbl.AP_CONSTRUCTION_TYPE,0) = :apConstructionType ");
		}

		if (woDto.getCreatedDate() != null) {
			sql.append(" AND woTbl.CREATED_DATE = :createdDate ");
		}

		if (StringUtils.isNotEmpty(woDto.getState())) {
			if (woDto.getState().contains("-GH") || woDto.getState().contains("-HUY")) {
				if (woDto.getState().contains("-GH")) {
					sql.append(" AND woTbl.STATE = :state AND woTbl.OPINION_TYPE='Đề xuất gia hạn' ");
					woDto.setState(woDto.getState().replace("-GH", ""));
				}
				if (woDto.getState().contains("-HUY")) {
					sql.append(" AND woTbl.STATE = :state AND woTbl.OPINION_TYPE='Đề xuất hủy' ");
					woDto.setState(woDto.getState().replace("-HUY", ""));
				}
			} else {
				sql.append(" AND nvl(woTbl.STATE_HTCT, woTbl.STATE) = :state ");
				if (woDto.getState().contains("_MOBILE")) {
					sql.append(" AND woTbl.id not in (select distinct wo_id from WO_MAPPING_PLAN where status = 1) ");
					woDto.setState(woDto.getState().replace("_MOBILE", ""));
				}
			}
		}

		// HienLT56 start 13012021
		// Huypq-12112021-start
		if (woDto.getLstState() != null && woDto.getLstState().size() > 0) {
			// sql.append(" AND woTbl.STATE in (:lstState) ");
			String sqlVuong = " AND ( ";
			int count = 0;
			for (String state : woDto.getLstState()) {
				count++;
				if (state.contains("-GH")) {
					if (count == 1) {
						sqlVuong += " (woTbl.STATE =:stateVuongGh" + count + " AND OPINION_TYPE='Đề xuất gia hạn') ";
					} else {
						sqlVuong += " OR (woTbl.STATE =:stateVuongGh" + count + " AND OPINION_TYPE='Đề xuất gia hạn') ";
					}
				} else if (state.contains("-HUY")) {
					if (count == 1) {
						sqlVuong += " (woTbl.STATE =:stateVuongHuy" + count + " AND OPINION_TYPE='Đề xuất hủy') ";
					} else {
						sqlVuong += " OR (woTbl.STATE =:stateVuongHuy" + count + " AND OPINION_TYPE='Đề xuất hủy') ";
					}
				} else {
					if (count == 1) {
						sqlVuong += " nvl(woTbl.STATE_HTCT, woTbl.STATE) =:stateVuong" + count;
					} else {
						sqlVuong += " OR nvl(woTbl.STATE_HTCT, woTbl.STATE) =:stateVuong" + count;
					}
				}
			}

			sqlVuong += ") ";
			sql.append(sqlVuong);
		}
		// Huy-end
		if (woDto.getFinishDateFrom() != null) {
			sql.append(" AND trunc(woTbl.FINISH_DATE) >= trunc(:finishDateFrom) ");
		}
		if (woDto.getFinishDateTo() != null) {
			sql.append(" AND trunc(woTbl.FINISH_DATE) <= trunc(:finishDateTo) ");
		}
		// HienLT56 end 13012021

		if (woDto.getFinishDate() != null) {
			sql.append(" AND woTbl.FINISH_DATE = :finishDate ");
		}

		if (StringUtils.isNotEmpty(woDto.getStationCode())) {
			sql.append(" AND woTbl.STATION_CODE = :stationCode ");
		}

		if (StringUtils.isNotEmpty(woDto.getHcqtContractCode())) {
			sql.append(" AND woTbl.HCQT_CONTRACT_CODE = :hcqtContractCode ");
		}

		if (StringUtils.isNotEmpty(woDto.getConstructionCode())) {
			sql.append(" AND woTbl.CONSTRUCTION_CODE = :constructionCode ");
		}

		if (StringUtils.isNotEmpty(woDto.getMoneyFlowBill())) {
			sql.append(" and woTbl.MONEY_FLOW_BILL = :moneyFlowBill ");
		}

		if (woDto.isFilterWoTaskDaily()) {
			sql.append(" and woTbl.id in (:woDailyIdList) ");
		}

		if (StringUtils.isNotEmpty(woDto.getOverdueApproveState())) {
			sql.append(" AND woTbl.OVERDUE_APPROVE_STATE = :overdueApproveState ");
		}

		if (woDto.getCreatedDateFrom() != null) {
			sql.append(" and woTbl.created_date >= trunc(:createdDateFrom) ");
		}
		if (woDto.getCreatedDateTo() != null) {
			sql.append(" and woTbl.created_date <= trunc(:createdDateTo) + 1 ");
		}

		if (woDto.getFromDate() != null)
			sql.append(" AND trunc(woTbl.START_TIME) >= trunc(:fromDate) ");
		if (woDto.getToDate() != null)
			sql.append(" AND trunc(woTbl.END_TIME) <= trunc(:toDate) ");
		if (woDto.getConstructionId() != null)
			sql.append(" AND woTbl.CONSTRUCTION_ID = :constructionId ");
		if (woDto.getCatWorkItemTypeId() != null)
			sql.append(" AND woTbl.CAT_WORK_ITEM_TYPE_ID = :catWorkItemTypeId ");
		if (woDto.getContractId() != null)
			sql.append("AND woTbl.contract_id = :contractId ");
		if (woDto.getAioContractId() != null)
			sql.append("AND woTbl.contract_id = :aioContractId ");
		if (woDto.getCdLevel2() != null)
			sql.append(" AND woTbl.cd_level_2 = :cdLevel2 ");
		// HienLT56 start 01122020
		if (woDto.getGroupCreateWO() != null) {
			if (woDto.getGroupCreateWO().equals("TTGPTH")) {
				sql.append(
						" AND woTr.GROUP_CREATED_NAME LIKE '%Trung tâm Giải pháp tích hợp%' AND wotbl.TR_ID IS NOT NULL ");
			}
			if (woDto.getGroupCreateWO().equals("TTXD_ĐTHT")) {
				sql.append(
						" AND woTr.GROUP_CREATED_NAME LIKE '%Trung tâm Xây dựng và Đầu tư hạ tầng%' AND wotbl.TR_ID IS NOT NULL ");
			}
			if (woDto.getGroupCreateWO().equals("CNCT")) {
				sql.append(" AND wotbl.TYPE = 1 ");
			}
			if (woDto.getGroupCreateWO().equals("TTVHKT")) {
				sql.append(" AND wotbl.TYPE = 2 ");
			}
			if (woDto.getGroupCreateWO().equals("TTHT")) {
				sql.append(" AND wotbl.TYPE IS NULL ");
			}
		}
		// HienLT56 end 01122020
		// search by username to return wo related to him
		if (StringUtils.isNotEmpty(woDto.getLoggedInUser())) {
			if (woDto.isMobile()) {
				sql.append(" AND woTbl.FT_ID = :ftId ");
			} else if (!isTcTct && !isTcBranch) {
				// không phải role tài chính
				sql.append(" AND ( woTbl.USER_CREATED = :username OR woTbl.FT_ID = :ftId ");

				// giao thẳng FT không qua CD
				sql.append(
						" OR ( woTbl.FT_ID is not null AND woTbl.cd_level_1 is null AND woTbl.cd_level_2 is null AND woTbl.cd_level_3 is null AND woTbl.cd_level_4 is null  AND woTbl.cd_level_5 is null) ");

				// ham list cho FT thi ko tim theo cd cac level
				if (!woDto.isMobile() && groupIdListView.size() > 0) {
					sql.append(
							" OR woTbl.CD_LEVEL_1 in (:groupIdListView) OR woTbl.CD_LEVEL_2 in (:groupIdListView) OR woTbl.CD_LEVEL_3 in (:groupIdListView) OR woTbl.CD_LEVEL_4 in (:groupIdListView) ");
					if (woDto.getSysUserId() != null) {
						sql.append("OR  woTbl.CD_LEVEL_5 = :cdLevel5 ");
					}
				}

				SysUserCOMSDTO sysUserCOMSDTO = getUserInfoById(woDto.getSysUserId());
				if (sysUserCOMSDTO != null && sysUserCOMSDTO.getSysGroupId() != null) {
					sql.append(" OR woTbl.CD_LEVEL_2 = " + sysUserCOMSDTO.getSysGroupId() + " OR CD_LEVEL_3 = "
							+ sysUserCOMSDTO.getSysGroupId());
				}
				sql.append(" ) ");
			} else {
				// role tài chính
				sql.append(" AND ( woTbl.USER_CREATED = :username OR woTbl.USER_CREATED = '-1' ");
				if (isTcTct) {
					// tổng công ty
					sql.append(" OR wt.wo_type_code in ('HSHC','HCQT','DOANHTHU') ");
					if (woDto.getLstState() != null
							&& (woDto.getLstState().contains("WAIT_TC_TCT")
									|| woDto.getLstState().contains("TC_TCT_REJECTED"))
							&& StringUtils.isNotEmpty(woDto.getEmailTcTct())) {
						sql.append(" AND woTbl.EMAIL_TC_TCT=:emailTcTct ");
					}
				} else {
					// tài chính trụ
					sql.append(" OR wt.wo_type_code = 'HSHC' ");
					if (contractBranchs.size() > 0) {
						sql.append(" OR cc.contract_branch in (:contractBranchs) ");
					}
				}
				sql.append(" ) ");
			}

		}
		if (woDto.isFilterAutoExpire()) {
			sql.append(" AND woTbl.AUTO_EXPIRE = '1' ");
		}

		sql.append(" order by woTbl.CREATED_DATE desc ");

		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		if (StringUtils.isNotEmpty(woDto.getKeySearch())) {
			query.setParameter("keySearch", toSearchStr(woDto.getKeySearch()));
			queryCount.setParameter("keySearch", toSearchStr(woDto.getKeySearch()));
		}

		if (woDto.getTrId() != null) {
			query.setParameter("trId", woDto.getTrId());
			queryCount.setParameter("trId", woDto.getTrId());
		}

		if (woDto.getWoTypeId() != null) {
			query.setParameter("woTypeId", woDto.getWoTypeId());
			queryCount.setParameter("woTypeId", woDto.getWoTypeId());
		}
		// HienLT56 start 15102020
		if (woDto.getListWoTypeId() != null && woDto.getListWoTypeId().size() > 0) {
			query.setParameterList("listWoTypeId", woDto.getListWoTypeId());
			queryCount.setParameterList("listWoTypeId", woDto.getListWoTypeId());
		}
		// HienLT56 end 15102020

		if (woDto.getWoTypeCode() != null) {
			query.setParameter("woTypeCode", woDto.getWoTypeCode());
			queryCount.setParameter("woTypeCode", woDto.getWoTypeCode());
		}

		if (woDto.getItem() != null) {
			query.setParameter("itemCode", woDto.getItem());
			queryCount.setParameter("itemCode", woDto.getItem());
		}

		if (woDto.getApWorkSrc() != null) {
			query.setParameter("apWorkSrc", woDto.getApWorkSrc());
			queryCount.setParameter("apWorkSrc", woDto.getApWorkSrc());
		}

		if (woDto.getApConstructionType() != null) {
			query.setParameter("apConstructionType", woDto.getApConstructionType());
			queryCount.setParameter("apConstructionType", woDto.getApConstructionType());
		}

		if (woDto.getCreatedDate() != null) {
			query.setParameter("createdDate", woDto.getCreatedDate());
			queryCount.setParameter("createdDate", woDto.getCreatedDate());
		}

		if (StringUtils.isNotEmpty(woDto.getState())) {
			query.setParameter("state", woDto.getState());
			queryCount.setParameter("state", woDto.getState());
		}

		// HienLT56 start 13012021
		// Huypq-12112021-start
		if (woDto.getLstState() != null && woDto.getLstState().size() > 0) {
			int countPara = 0;
			for (String state : woDto.getLstState()) {
				countPara++;
				if (state.contains("-GH")) {
					query.setParameter("stateVuongGh" + countPara, state.replace("-GH", ""));
					queryCount.setParameter("stateVuongGh" + countPara, state.replace("-GH", ""));
				} else if (state.contains("-HUY")) {
					query.setParameter("stateVuongHuy" + countPara, state.replace("-HUY", ""));
					queryCount.setParameter("stateVuongHuy" + countPara, state.replace("-HUY", ""));
				} else {
					query.setParameter("stateVuong" + countPara, state);
					queryCount.setParameter("stateVuong" + countPara, state);
				}
			}
		}
		// Huy-end
		if (woDto.getFinishDateFrom() != null) {
			query.setParameter("finishDateFrom", woDto.getFinishDateFrom());
			queryCount.setParameter("finishDateFrom", woDto.getFinishDateFrom());
		}

		if (woDto.getFinishDateTo() != null) {
			query.setParameter("finishDateTo", woDto.getFinishDateTo());
			queryCount.setParameter("finishDateTo", woDto.getFinishDateTo());
		}
		// HienLT56 end 13012021

		if (woDto.getFinishDate() != null) {
			query.setParameter("finishDate", woDto.getFinishDate());
			queryCount.setParameter("finishDate", woDto.getFinishDate());
		}

		if (woDto.getFtId() != null) {
			query.setParameter("ftId", woDto.getFtId());
			queryCount.setParameter("ftId", woDto.getFtId());
		}

		if (woDto.getContractId() != null) {
			query.setParameter("contractId", woDto.getContractId());
			queryCount.setParameter("contractId", woDto.getContractId());
		}

		if (woDto.getAioContractId() != null) {
			query.setParameter("aioContractId", woDto.getAioContractId());
			queryCount.setParameter("aioContractId", woDto.getAioContractId());
		}

		if (woDto.getFromDate() != null) {
			query.setParameter("fromDate", woDto.getFromDate());
			queryCount.setParameter("fromDate", woDto.getFromDate());
		}

		if (woDto.getToDate() != null) {
			query.setParameter("toDate", woDto.getToDate());
			queryCount.setParameter("toDate", woDto.getToDate());
		}

		if (StringUtils.isNotEmpty(woDto.getStationCode())) {
			query.setParameter("stationCode", woDto.getStationCode());
			queryCount.setParameter("stationCode", woDto.getStationCode());
		}

		if (StringUtils.isNotEmpty(woDto.getHcqtContractCode())) {
			query.setParameter("hcqtContractCode", woDto.getHcqtContractCode());
			queryCount.setParameter("hcqtContractCode", woDto.getHcqtContractCode());
		}

		if (woDto.getConstructionId() != null) {
			query.setParameter("constructionId", woDto.getConstructionId());
			queryCount.setParameter("constructionId", woDto.getConstructionId());
		}

		if (StringUtils.isNotEmpty(woDto.getConstructionCode())) {
			query.setParameter("constructionCode", woDto.getConstructionCode());
			queryCount.setParameter("constructionCode", woDto.getConstructionCode());
		}

		if (woDto.getCatWorkItemTypeId() != null) {
			query.setParameter("catWorkItemTypeId", woDto.getCatWorkItemTypeId());
			queryCount.setParameter("catWorkItemTypeId", woDto.getCatWorkItemTypeId());
		}

		if (woDto.getCdLevel2() != null) {
			query.setParameter("cdLevel2", woDto.getCdLevel2());
			queryCount.setParameter("cdLevel2", woDto.getCdLevel2());
		}

		if (StringUtils.isNotEmpty(woDto.getMoneyFlowBill())) {
			query.setParameter("moneyFlowBill", woDto.getMoneyFlowBill());
			queryCount.setParameter("moneyFlowBill", woDto.getMoneyFlowBill());
		}

		if (woDto.isFilterWoTaskDaily()) {
			List<Long> woDailyIdList = getListIdWoTaskDaily();
			query.setParameterList("woDailyIdList", woDailyIdList);
			queryCount.setParameterList("woDailyIdList", woDailyIdList);
		}

		if (StringUtils.isNotEmpty(woDto.getOverdueApproveState())) {
			query.setParameter("overdueApproveState", woDto.getOverdueApproveState());
			queryCount.setParameter("overdueApproveState", woDto.getOverdueApproveState());
		}

		if (StringUtils.isNotEmpty(woDto.getLoggedInUser())) {
			if (woDto.isMobile()) {
				query.setParameter("ftId", woDto.getFtId());
			} else if (!isTcTct && !isTcBranch) {
				query.setParameter("username", woDto.getLoggedInUser());
				if (!woDto.isMobile() && groupIdListView.size() > 0) {
					query.setParameterList("groupIdListView", groupIdListView);
				}
				query.setParameter("ftId", woDto.getFtId());

				queryCount.setParameter("username", woDto.getLoggedInUser());
				if (!woDto.isMobile() && groupIdListView.size() > 0) {
					queryCount.setParameterList("groupIdListView", groupIdListView);
				}
				queryCount.setParameter("ftId", woDto.getFtId());

				if (!woDto.isMobile() && groupIdListView.size() > 0) {
					if (woDto.getSysUserId() != null) {
						query.setParameter("cdLevel5", woDto.getSysUserId());
						queryCount.setParameter("cdLevel5", woDto.getSysUserId());
					}
				}
			} else {
				// role tài chính
				query.setParameter("username", woDto.getLoggedInUser());
				queryCount.setParameter("username", woDto.getLoggedInUser());
				if (!isTcTct) {
					// tài chính trụ
					if (contractBranchs.size() > 0) {
						query.setParameterList("contractBranchs", contractBranchs);
						queryCount.setParameterList("contractBranchs", contractBranchs);
					}
				}

				if (woDto.getLstState() != null
						&& (woDto.getLstState().contains("WAIT_TC_TCT")
								|| woDto.getLstState().contains("TC_TCT_REJECTED"))
						&& StringUtils.isNotEmpty(woDto.getEmailTcTct())) {
					SysUserCOMSDTO user = this.getUserInfoByLoginname(woDto.getLoggedInUser());
					query.setParameter("emailTcTct", woDto.getEmailTcTct());
					queryCount.setParameter("emailTcTct", woDto.getEmailTcTct());
				}
			}

		}

		if (woDto.getCreatedDateFrom() != null) {
			query.setParameter("createdDateFrom", woDto.getCreatedDateFrom());
			queryCount.setParameter("createdDateFrom", woDto.getCreatedDateFrom());
		}

		if (woDto.getCreatedDateTo() != null) {
			query.setParameter("createdDateTo", woDto.getCreatedDateTo());
			queryCount.setParameter("createdDateTo", woDto.getCreatedDateTo());
		}

		query.addScalar("type", new StringType());
		query.addScalar("itemName", new StringType());
		query.addScalar("unitCreateOrBelongToWO", new StringType()); // HienLT56 add 01122020 - for test
		query.addScalar("apWorkSrcName", new StringType());
		query.addScalar("woTypeName", new StringType());
		query.addScalar("woTypeCode", new StringType());
		query.addScalar("apConstructionTypeName", new StringType());
		query.addScalar("endTimeStr", new StringType());
		query.addScalar("catWorkItemTypeName", new StringType());
		query.addScalar("jobUnfinished", new StringType());
		query.addScalar("stateVuong", new StringType());
		query.addScalar("vtNetStationCode", new StringType());
		query.addScalar("ftPositionName", new StringType());
		query.addScalar("ftEmailSysUser", new StringType());
		query.addScalar("approveDateReportWo", new DateType());

		query = mapFields(query);
		query.setResultTransformer(Transformers.aliasToBean(WoDTO.class));

		if (woDto.getPage() != null && woDto.getPageSize() != null) {
			query.setFirstResult((woDto.getPage().intValue() - 1) * woDto.getPageSize().intValue());
			query.setMaxResults(woDto.getPageSize().intValue());
		}

		woDto.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		// List<WoDTO> lst = query.list();
		return query.list();
	}

	public List<WoAppParamDTO> getAppParam(String parType) {
		StringBuilder sql = new StringBuilder(
				"select CODE as code, NAME as name, PAR_TYPE as parType, PAR_ORDER as parOrder, DESCRIPTION description from APP_PARAM where PAR_TYPE = :parType and status>0 order by par_order");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.setParameter("parType", parType);

		query.addScalar("code", new StringType());
		query.addScalar("name", new StringType());
		query.addScalar("parType", new StringType());
		query.addScalar("parOrder", new LongType());
		query.addScalar("description", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(WoAppParamDTO.class));
		return query.list();
	}

	public List<WoSimpleConstructionDTO> suggestConstructions(String keySearch) {

		StringBuilder sql = new StringBuilder("select CONSTRUCTION_ID as constructionId, NAME as constructionName "
				+ " from CONSTRUCTION where NAME LIKE :keySearch " + " fetch next 10 rows only ");

		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.setParameter("keySearch", toSearchStr(keySearch));

		query.addScalar("constructionId", new LongType());
		query.addScalar("constructionName", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(WoSimpleConstructionDTO.class));
		return query.list();
	}

	public List<WoCdDTO> getCdLevel1() {
		StringBuilder sql = new StringBuilder(
				"select CODE as sysGroupId, NAME as groupName FROM APP_PARAM WHERE PAR_TYPE = 'CD_LEVEL_1'");

		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("sysGroupId", new LongType());
		query.addScalar("groupName", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(WoCdDTO.class));
		return query.list();
	}

	public List<WoSimpleSysGroupDTO> getCds(int level, Long higerCdId, List<String> geoAreaList, boolean isGeoArea) {
		StringBuilder sql = new StringBuilder("select SYS_GROUP_ID as sysGroupId, GROUP_NAME_LEVEL2 as groupNameLv2, "
				+ " GROUP_NAME_LEVEL3 as groupNameLv3, NAME as groupName, " + " PARENT_ID as parentId "
				+ " from SYS_GROUP where GROUP_LEVEL = 3 ");

		if (level == 2)
			sql.append(" and (CODE like '%P.HT%' or CODE like '%P.XD%' )");
		if (level == 3 && higerCdId != null)
			sql.append(
					" and CODE like '%P.KT%' and PARENT_ID like ( select PARENT_ID from SYS_GROUP where SYS_GROUP_ID = :higerCdId) ");
		if (level == 4 && higerCdId != null)
			sql.append(
					" and CODE not like '%CTCT%' and PARENT_ID like ( select PARENT_ID from SYS_GROUP where SYS_GROUP_ID = :higerCdId) ");

		if (isGeoArea) {
			sql.append(" and AREA_CODE in (:geoAreaList) ");
		}
		if (level == 2)
			sql.append("  UNION ALL\n" + "  select\n" + "        SYS_GROUP_ID as sysGroupId,\n"
					+ "        GROUP_NAME_LEVEL2 as groupNameLv2,\n" + "        GROUP_NAME_LEVEL3 as groupNameLv3,\n"
					+ "        NAME as groupName,\n" + "        PARENT_ID as parentId  \n" + "    from\n"
					+ "        SYS_GROUP \n" + "    where  \n"
					+ "        SYS_GROUP_ID in (select CODE from APP_PARAM where PAR_TYPE= 'WO_XL_CD_LEVEL_2' and status = 1)  ");
		sql.append(" ORDER BY groupName ");

		SQLQuery query = getSession().createSQLQuery(sql.toString());

		if (higerCdId != null) {
			query.setParameter("higerCdId", higerCdId);
		}

		if (isGeoArea) {
			query.setParameterList("geoAreaList", geoAreaList);
		}

		query.addScalar("sysGroupId", new LongType());
		query.addScalar("groupName", new StringType());
		query.addScalar("groupNameLv2", new StringType());
		query.addScalar("groupNameLv3", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(WoSimpleSysGroupDTO.class));
		return query.list();
	}

	public List<WoCatWorkItemTypeDTO> getCatWorkTypes(Long catConsId) {
		StringBuilder sql = new StringBuilder(
				"select CAT_WORK_ITEM_TYPE_ID as catWorkItemTypeId, NAME as name, CODE as catWorkItemTypeCode "
						+ " from cat_work_item_type where STATUS > 0 ");

		if (catConsId != null)
			sql.append(" AND cat_construction_type_id = :catConsId ");

		SQLQuery query = getSession().createSQLQuery(sql.toString());

		if (catConsId != null)
			query.setParameter("catConsId", catConsId);

		query.addScalar("catWorkItemTypeId", new LongType());
		query.addScalar("name", new StringType());
		query.addScalar("catWorkItemTypeCode", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(WoCatWorkItemTypeDTO.class));
		return query.list();
	}

	public WoCatWorkItemTypeDTO getCatWorkTypeById(Long id) {
		StringBuilder sql = new StringBuilder(
				"select CAT_WORK_ITEM_TYPE_ID as catWorkItemTypeId, NAME as name, CODE as catWorkItemTypeCode "
						+ " from cat_work_item_type where STATUS = 1 and CAT_WORK_ITEM_TYPE_ID =:id ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		if (id != null)
			query.setParameter("id", id);

		query.addScalar("catWorkItemTypeId", new LongType());
		query.addScalar("name", new StringType());
		query.addScalar("catWorkItemTypeCode", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(WoCatWorkItemTypeDTO.class));
		return (WoCatWorkItemTypeDTO) query.uniqueResult();
	}

	public List<WoSimpleFtDTO> getFtList(long parentGroupId, boolean isFt2, String keysearch) {
		StringBuilder sql = new StringBuilder(
				" select SYS_USER_ID as ftId, EMPLOYEE_CODE as employeeCode, email as email, FULL_NAME as fullName, SYS_GROUP_ID as sysGroupId "
						+ " from SYS_USER where STATUS = 1 AND TYPE_USER is null ");

		if (parentGroupId > 0) {
			if (isFt2 == true) {
				// tìm tất cả ft trong tỉnh
				sql.append(
						" and SYS_GROUP_ID in (select SYS_GROUP_ID from SYS_GROUP where PATH like '%' ||( select parent_id from SYS_GROUP where sys_group_id = :parentGroupId ) || '%' ) ");
				// sql.append(" and SYS_GROUP_ID in (select SYS_GROUP_ID from SYS_GROUP where
				// parent_id = ( select parent_id from SYS_GROUP where sys_group_id =
				// :parentGroupId )) ");
			} else {
				// tìm ft thuộc 1 group
				sql.append(" and SYS_GROUP_ID = :parentGroupId ");
			}
		}

		if (StringUtils.isNotEmpty(keysearch)) {
			sql.append(
					" and (lower(LOGIN_NAME) like :keysearch or lower(FULL_NAME) like :keysearch or lower(email) like :keysearch) fetch next 20 rows only ");
		} else {
			sql.append(" order by FULL_NAME ");
		}

		SQLQuery query = getSession().createSQLQuery(sql.toString());

		if (parentGroupId > 0) {
			query.setParameter("parentGroupId", parentGroupId);
		}

		if (StringUtils.isNotEmpty(keysearch)) {
			query.setParameter("keysearch", "%" + keysearch.toLowerCase() + "%");
		}

		query.addScalar("ftId", new LongType());
		query.addScalar("sysGroupId", new LongType());
		query.addScalar("fullName", new StringType());
		query.addScalar("employeeCode", new StringType());
		query.addScalar("email", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(WoSimpleFtDTO.class));
		return query.list();
	}

	public WoDTO getAppParam(String code, String parType) {
		StringBuilder sql = new StringBuilder(
				" SELECT NAME as apWorkSrcName, PAR_ORDER as parOrder FROM APP_PARAM WHERE CODE = :code and PAR_TYPE= :parType ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("code", code);
		query.setParameter("parType", parType);
		query.addScalar("apWorkSrcName", new StringType());
		query.addScalar("parOrder", new IntegerType());
		query.setResultTransformer(Transformers.aliasToBean(WoDTO.class));
		return (WoDTO) query.uniqueResult();
	}

	public List<WoSimpleFtDTO> getAllFtList() {
		StringBuilder sql = new StringBuilder(
				"select SYS_USER_ID as ftId, FULL_NAME as fullName, SYS_GROUP_ID as sysGroupId "
						+ " from SYS_USER where STATUS > 0 and SYS_GROUP_ID is not null fetch next 10 rows only ");

		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("ftId", new LongType());
		query.addScalar("sysGroupId", new LongType());
		query.addScalar("fullName", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(WoSimpleFtDTO.class));
		return query.list();
	}

	private String toSearchStr(String str) {
		return "%" + str.toLowerCase() + "%";
	}

	private SQLQuery mapFields(SQLQuery query) {
		query.addScalar("woId", new LongType());
		query.addScalar("woCode", new StringType());
		query.addScalar("woName", new StringType());
		query.addScalar("woTypeId", new LongType());
		query.addScalar("trId", new LongType());
		query.addScalar("state", new StringType());
		query.addScalar("constructionId", new LongType());
		query.addScalar("catWorkItemTypeId", new LongType());
		query.addScalar("stationCode", new StringType());
		query.addScalar("userCreated", new StringType());
		query.addScalar("createdDate", new TimestampType());
		query.addScalar("finishDate", new TimestampType());
		query.addScalar("qoutaTime", new IntegerType());
		query.addScalar("executeMethod", new StringType());
		query.addScalar("quantityValue", new StringType());
		query.addScalar("cdLevel1", new StringType());
		query.addScalar("cdLevel2", new StringType());
		query.addScalar("cdLevel3", new StringType());
		query.addScalar("cdLevel4", new StringType());
		query.addScalar("cdLevel5", new StringType());
		query.addScalar("ftId", new LongType());
		query.addScalar("acceptTime", new TimestampType());
		query.addScalar("endTime", new TimestampType());
		query.addScalar("executeLat", new StringType());
		query.addScalar("executeLong", new StringType());
		query.addScalar("status", new LongType());
		query.addScalar("totalMonthPlanId", new LongType());
		query.addScalar("moneyValue", new DoubleType());
		query.addScalar("moneyFlowBill", new StringType());
		query.addScalar("moneyFlowDate", new TimestampType());
		query.addScalar("moneyFlowValue", new LongType());
		query.addScalar("moneyFlowRequired", new LongType());
		query.addScalar("moneyFlowContent", new StringType());
		query.addScalar("apConstructionType", new LongType());
		query.addScalar("apWorkSrc", new LongType());
		query.addScalar("opinionResult", new StringType());
		query.addScalar("executeChecklist", new LongType());
		query.addScalar("woNameId", new LongType());
		query.addScalar("startTime", new TimestampType());
		query.addScalar("contractId", new LongType());
		query.addScalar("quantityByDate", new LongType());
		query.addScalar("closedTime", new TimestampType());
		query.addScalar("constructionCode", new StringType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("projectId", new LongType());
		query.addScalar("projectCode", new StringType());
		query.addScalar("cdLevel1Name", new StringType());
		query.addScalar("cdLevel2Name", new StringType());
		query.addScalar("cdLevel3Name", new StringType());
		query.addScalar("cdLevel4Name", new StringType());
		query.addScalar("cdLevel5Name", new StringType());
		query.addScalar("ftName", new StringType());
		query.addScalar("ftEmail", new StringType());
		query.addScalar("createdUserFullName", new StringType());
		query.addScalar("createdUserEmail", new StringType());
		query.addScalar("trCode", new StringType());
		query.addScalar("catConstructionTypeId", new LongType());
		query.addScalar("hshcReceiveDate", new TimestampType());
		query.addScalar("hcqtContractCode", new StringType());
		query.addScalar("cnkv", new StringType());
		query.addScalar("catProvinceCode", new StringType());
		query.addScalar("hcqtProjectId", new LongType());
		query.addScalar("userCdLevel2ReceiveWo", new StringType());
		query.addScalar("updateCdLevel2ReceiveWo", new TimestampType());
		query.addScalar("userCdLevel3ReceiveWo", new StringType());
		query.addScalar("updateCdLevel3ReceiveWo", new TimestampType());
		query.addScalar("userCdLevel4ReceiveWo", new StringType());
		query.addScalar("updateCdLevel4ReceiveWo", new TimestampType());
		query.addScalar("userCdLevel5ReceiveWo", new StringType());
		query.addScalar("updateCdLevel5ReceiveWo", new TimestampType());
		query.addScalar("userFtReceiveWo", new StringType());
		query.addScalar("updateFtReceiveWo", new TimestampType());

		query.addScalar("overdueReason", new StringType());
		query.addScalar("overdueApproveState", new StringType());
		query.addScalar("overdueApprovePerson", new StringType());

		query.addScalar("description", new StringType());
		query.addScalar("vtnetWoCode", new StringType());
		query.addScalar("partner", new StringType());

		query.addScalar("woOrder", new StringType());
		query.addScalar("woOrderConfirm", new LongType());
		query.addScalar("voState", new LongType());
		query.addScalar("voRequestDept", new StringType());
		query.addScalar("voRequestRole", new StringType());
		query.addScalar("voApprovedDept", new StringType());
		query.addScalar("voApprovedRole", new StringType());
		query.addScalar("voMngtDept", new StringType());
		query.addScalar("voMngtRole", new StringType());
		query.addScalar("createdDate5s", new DateType());
		query.addScalar("moneyValueHtct", new DoubleType());
		query.addScalar("userDthtApprovedWo", new StringType());
		query.addScalar("updateDthtApprovedWo", new DateType());
		query.addScalar("opinionType", new StringType());
		return query;
	}

	private SQLQuery mapFieldsReport(SQLQuery query) {
		query.addScalar("woId", new LongType());
		query.addScalar("woCode", new StringType());
		query.addScalar("woName", new StringType());
		query.addScalar("woTypeId", new LongType());
		query.addScalar("trId", new LongType());
		query.addScalar("state", new StringType());
		query.addScalar("constructionId", new LongType());
		query.addScalar("catWorkItemTypeId", new LongType());
		query.addScalar("stationCode", new StringType());
		query.addScalar("userCreated", new StringType());
		query.addScalar("createdDate", new DateType());
		query.addScalar("finishDate", new DateType());
		query.addScalar("qoutaTime", new IntegerType());
		query.addScalar("executeMethod", new StringType());
		query.addScalar("quantityValue", new StringType());
		query.addScalar("cdLevel1", new StringType());
		query.addScalar("cdLevel2", new StringType());
		query.addScalar("cdLevel3", new StringType());
		query.addScalar("cdLevel4", new StringType());
		query.addScalar("ftId", new LongType());
		query.addScalar("acceptTime", new DateType());
		query.addScalar("endTime", new DateType());
		query.addScalar("executeLat", new StringType());
		query.addScalar("executeLong", new StringType());
		query.addScalar("status", new LongType());
		query.addScalar("totalMonthPlanId", new LongType());
		query.addScalar("moneyValue", new DoubleType());
		query.addScalar("moneyFlowBill", new StringType());
		query.addScalar("moneyFlowDate", new DateType());
		query.addScalar("moneyFlowValue", new LongType());
		query.addScalar("moneyFlowRequired", new LongType());
		query.addScalar("moneyFlowContent", new StringType());
		query.addScalar("apConstructionType", new LongType());
		query.addScalar("apWorkSrc", new LongType());
		query.addScalar("opinionResult", new StringType());
		query.addScalar("executeChecklist", new LongType());
		query.addScalar("woNameId", new LongType());
		query.addScalar("startTime", new DateType());
		query.addScalar("contractId", new LongType());
		query.addScalar("quantityByDate", new LongType());
		query.addScalar("closedTime", new DateType());
		query.addScalar("constructionCode", new StringType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("projectId", new LongType());
		query.addScalar("projectCode", new StringType());
		query.addScalar("cdLevel1Name", new StringType());
		query.addScalar("cdLevel2Name", new StringType());
		query.addScalar("cdLevel3Name", new StringType());
		query.addScalar("cdLevel4Name", new StringType());
		query.addScalar("ftName", new StringType());
		query.addScalar("ftEmail", new StringType());
		query.addScalar("createdUserFullName", new StringType());
		query.addScalar("createdUserEmail", new StringType());
		query.addScalar("trCode", new StringType());
		query.addScalar("catConstructionTypeId", new LongType());
		query.addScalar("partner", new StringType());
		query.addScalar("woOrder", new StringType());
		query.addScalar("vtnetWoCode", new StringType());

		return query;
	}

	// Báo cáo Kpi
	public List<RpWoDetailOsDTO> doSearchBaoCaoChamDiemKpi(ReportWoDTO obj) {
		StringBuilder sql = new StringBuilder("SELECT\n" + "    '1' type,\n" + "    TO_CHAR('Toàn quốc') areacode,\n"
				+ "    TO_CHAR('Toàn quốc') procode,\n" + "    a.month,\n" + "    a.year,\n"
				+ "   sum(a.salaryMonth) salaryMonth,sum(a.salaryWo) salaryWo, nvl(ROUND(DECODE( sum(a.salaryMonth),0,0,100* sum(a.salaryWo)/ sum(a.salaryMonth)),2),0) salaryPercent, \n"
				+ "sum(a.hshcXayLapKH) hshcXayLapKH,sum(a.hshcXayLapWo) hshcXayLapWo,nvl(ROUND(DECODE( sum(a.hshcXayLapKH),0,0,100* sum(a.hshcXayLapWo)/ sum(a.hshcXayLapKH)),2),0) hshcXayLapPercent,\n"
				+ "sum(a.qtKH) qtKH,sum(a.qtWo) qtWo, nvl(ROUND(DECODE( sum(a.qtKH),0,0,100* sum(a.qtWo)/ sum(a.qtKH)),2),0) qtPercent,\n"
				+ "sum(a.qtGPDNKH) qtGPDNKH,sum(a.qtGPDNWo) qtGPDNWo,nvl(ROUND(DECODE( sum(a.qtGPDNKH),0,0,100* sum(a.qtGPDNWo)/ sum(a.qtGPDNKH)),2),0) qtGPDNPercent,\n"
				+ "sum(a.qtSLXLKH) qtSLXLKH,sum(a.qtSLXLWo) qtSLXLWo,nvl(ROUND(DECODE( sum(a.qtSLXLKH),0,0,100* sum(a.qtSLXLWo)/ sum(a.qtSLXLKH)),2),0) qtSLXLPercent,\n"
				+ "sum(a.slKH) slKH,sum(a.slWo) slWo,nvl(ROUND(DECODE( sum(a.slKH),0,0,100* sum(a.slWo)/ sum(a.slKH)),2),0) slPercent,\n"
				+ "sum(a.slGPDNKH) slGPDNKH,sum(a.slGPDNWo) slGPDNWo,nvl(ROUND(DECODE( sum(a.slGPDNKH),0,0,100* sum(a.slGPDNWo)/ sum(a.slGPDNKH)),2),0) slGPDNPercent,\n"
				+ "sum(a.tkvKH) tkvKH,sum(tkvWo) tkvWo,nvl(ROUND(DECODE( sum(a.tkvKH),0,0,100* sum(a.tkvWo)/ sum(a.tkvKH)),2),0) tkvPercent,\n"
				+ "sum(a.thdtKH) thdtKH,sum(a.thdtWo) thdtWo,nvl(ROUND(DECODE( sum(a.thdtKH),0,0,100* sum(a.thdtWo)/ sum(a.thdtKH)),2),0) thdtPercent,\n"
				+ "sum(a.ttkKH) ttkKH,sum(a.ttkWo) ttkWo, nvl(ROUND(DECODE( sum(a.ttkKH),0,0,100* sum(a.ttkWo)/ sum(a.ttkKH)),2),0) ttkPercent,\n"
				+ "sum(a.xdXMKH) xdXMKH,sum(a.xdXMWo) xdXMWo,nvl(ROUND(DECODE( sum(a.xdXMKH),0,0,100* sum(a.xdXMWo)/ sum(a.xdXMKH)),2),0) xdXMPercent,\n"
				+ "sum(a.xDBKH) xDBKH,sum(a.xDBWo) xDBWo,nvl(ROUND(DECODE( sum(a.xDBKH),0,0,100* sum(a.xDBWo)/ sum(a.xDBKH)),2),0) xDBPercent,\n"
				+ "sum(a.tmbKH) tmbKH,sum(a.tmbWo) tmbWo,nvl(ROUND(DECODE( sum(a.tmbKH),0,0,100* sum(a.tmbWo)/ sum(a.tmbKH)),2),0) tmbPercent,"
				+ "sum(a.salaryDat) salaryDat,sum(a.salaryThuong) salaryThuong,sum(a.hshcXayLapDat) hshcXayLapDat,sum(a.hshcXayLapThuong) hshcXayLapThuong,sum(a.qtDat) qtDat,sum(a.qtThuong) qtThuong,\n"
				+ "               sum(a.qtGPDNDat) qtGPDNDat,sum(a.qtGPDNThuong) qtGPDNThuong,sum(a.qtSLXLDat) qtSLXLDat,sum(a.qtSLXLThuong) qtSLXLThuong,sum(a.slDat) slDat,sum(a.slThuong) slThuong\n"
				+ "               ,sum(a.slGPDNDat) slGPDNDat,sum(a.slGPDNThuong) slGPDNThuong,sum(a.tkvDat) tkvDat,sum(a.tkvThuong) tkvThuong,sum(a.thdtDat) thdtDat,sum(a.thdtThuong) thdtThuong\n"
				+ "               ,sum(a.ttkDat) ttkDat,sum(a.ttkThuong) ttkThuong,sum(a.xdXMDat) xdXMDat,sum(a.xdXMThuong) xdXMThuong,sum(a.xDBDat) xDBDat,sum(a.xDBThuong) xDBThuong\n"
				+ "               ,sum(a.tmbDat) tmbDat,sum(a.tmbThuong) tmbThuong,sum(a.diemThuongTong) diemThuongTong,sum(a.diemDatTong) diemDatTong,sum(a.tongDiem) tongDiem,sum(a.quyDoiDiem) quyDoiDiem \n"
				+ "FROM\n" + "    RP_WO_KPI a\n" + "WHERE 1 = 1");
		if (obj.getSysGroupId() != null) {
			sql.append(" AND a.sysGroupId =:sysGroupId ");
		}
		if (obj.getYear() != null && obj.getMonth() != null) {
			sql.append(" AND a.month = :month AND a.year = :year");
		}
		sql.append(" GROUP BY month,year ");
		sql.append("UNION ALL SELECT\n" + "        '2' type,\n" + "        TO_CHAR('KV1') areacode,\n"
				+ "        TO_CHAR('KV1') procode,\n" + "        a.month,\n" + "        a.year,\n"
				+ "        sum(a.salaryMonth) salaryMonth,\n" + "        sum(a.salaryWo) salaryWo,\n"
				+ "        nvl(ROUND(DECODE( sum(a.salaryMonth),0,0,100* sum(a.salaryWo)/ sum(a.salaryMonth)),2),0) salaryPercent,\n"
				+ "        sum(a.hshcXayLapKH) hshcXayLapKH,\n" + "        sum(a.hshcXayLapWo) hshcXayLapWo,\n"
				+ "        nvl(ROUND(DECODE( sum(a.hshcXayLapKH),0,0,100* sum(a.hshcXayLapWo)/ sum(a.hshcXayLapKH)),2), 0) hshcXayLapPercent,\n"
				+ "        sum(a.qtKH) qtKH,sum(a.qtWo) qtWo,\n"
				+ "        nvl(ROUND(DECODE( sum(a.qtKH),0,0,100* sum(a.qtWo)/ sum(a.qtKH)),2),0) qtPercent,\n"
				+ "        sum(a.qtGPDNKH) qtGPDNKH,\n" + "        sum(a.qtGPDNWo) qtGPDNWo,\n"
				+ "        nvl(ROUND(DECODE( sum(a.qtGPDNKH),0,0,100* sum(a.qtGPDNWo)/ sum(a.qtGPDNKH)),2),0) qtGPDNPercent,\n"
				+ "        sum(a.qtSLXLKH) qtSLXLKH,sum(a.qtSLXLWo) qtSLXLWo,\n"
				+ "        nvl(ROUND(DECODE( sum(a.qtSLXLKH),0,0,100* sum(a.qtSLXLWo)/ sum(a.qtSLXLKH)), 2),0) qtSLXLPercent,\n"
				+ "        sum(a.slKH) slKH,sum(a.slWo) slWo, nvl(ROUND(DECODE( sum(a.slKH),0,0,100* sum(a.slWo)/ sum(a.slKH)),2),0) slPercent,sum(a.slGPDNKH) slGPDNKH,\n"
				+ "        sum(a.slGPDNWo) slGPDNWo,nvl(ROUND(DECODE( sum(a.slGPDNKH),0,0,100* sum(a.slGPDNWo)/ sum(a.slGPDNKH)),2),0) slGPDNPercent,\n"
				+ "        sum(a.tkvKH) tkvKH,sum(tkvWo) tkvWo,nvl(ROUND(DECODE( sum(a.tkvKH),0,0,100* sum(a.tkvWo)/ sum(a.tkvKH)),2),0) tkvPercent,\n"
				+ "        sum(a.thdtKH) thdtKH,sum(a.thdtWo) thdtWo,nvl(ROUND(DECODE( sum(a.thdtKH),0,0,100* sum(a.thdtWo)/ sum(a.thdtKH)),2),0) thdtPercent,\n"
				+ "        sum(a.ttkKH) ttkKH,sum(a.ttkWo) ttkWo,nvl(ROUND(DECODE( sum(a.ttkKH),0,0,100* sum(a.ttkWo)/ sum(a.ttkKH)),2),0) ttkPercent,\n"
				+ "        sum(a.xdXMKH) xdXMKH,sum(a.xdXMWo) xdXMWo,nvl(ROUND(DECODE( sum(a.xdXMKH),0,0,100* sum(a.xdXMWo)/ sum(a.xdXMKH)),2),0) xdXMPercent,\n"
				+ "        sum(a.xDBKH) xDBKH,sum(a.xDBWo) xDBWo,nvl(ROUND(DECODE( sum(a.xDBKH),0,0,100* sum(a.xDBWo)/ sum(a.xDBKH)),2),0) xDBPercent,\n"
				+ "        sum(a.tmbKH) tmbKH,sum(a.tmbWo) tmbWo,nvl(ROUND(DECODE( sum(a.tmbKH),0,0,100* sum(a.tmbWo)/ sum(a.tmbKH)),2),0) tmbPercent,\n"
				+ "        sum(a.salaryDat) salaryDat,sum(a.salaryThuong) salaryThuong,sum(a.hshcXayLapDat) hshcXayLapDat,sum(a.hshcXayLapThuong) hshcXayLapThuong,\n"
				+ "        sum(a.qtDat) qtDat,sum(a.qtThuong) qtThuong,sum(a.qtGPDNDat) qtGPDNDat,sum(a.qtGPDNThuong) qtGPDNThuong,sum(a.qtSLXLDat) qtSLXLDat,sum(a.qtSLXLThuong) qtSLXLThuong,\n"
				+ "        sum(a.slDat) slDat,sum(a.slThuong) slThuong,sum(a.slGPDNDat) slGPDNDat,sum(a.slGPDNThuong) slGPDNThuong,sum(a.tkvDat) tkvDat,\n"
				+ "        sum(a.tkvThuong) tkvThuong,sum(a.thdtDat) thdtDat,sum(a.thdtThuong) thdtThuong,sum(a.ttkDat) ttkDat,sum(a.ttkThuong) ttkThuong,\n"
				+ "        sum(a.xdXMDat) xdXMDat,sum(a.xdXMThuong) xdXMThuong,sum(a.xDBDat) xDBDat,sum(a.xDBThuong) xDBThuong,\n"
				+ "        sum(a.tmbDat) tmbDat,sum(a.tmbThuong) tmbThuong,sum(a.diemThuongTong) diemThuongTong,sum(a.diemDatTong) diemDatTong,\n"
				+ "        sum(a.tongDiem) tongDiem,sum(a.quyDoiDiem) quyDoiDiem  \n" + "    FROM\n"
				+ "        RP_WO_KPI a \n" + "    WHERE\n" + "        1 = 1 \n" + "        AND  a.areacode = 'KV1'");
		if (obj.getSysGroupId() != null) {
			sql.append(" AND a.sysGroupId =:sysGroupId ");
		}
		if (obj.getYear() != null && obj.getMonth() != null) {
			sql.append(" AND a.month = :month AND a.year = :year");
		}
		sql.append(" GROUP BY month,year ");
		sql.append("UNION ALL SELECT\n" + "        '2' type,\n" + "        TO_CHAR('KV2') areacode,\n"
				+ "        TO_CHAR('KV2') procode,\n" + "        a.month,\n" + "        a.year,\n"
				+ "        sum(a.salaryMonth) salaryMonth,\n" + "        sum(a.salaryWo) salaryWo,\n"
				+ "        nvl(ROUND(DECODE( sum(a.salaryMonth),0,0,100* sum(a.salaryWo)/ sum(a.salaryMonth)),2),0) salaryPercent,\n"
				+ "        sum(a.hshcXayLapKH) hshcXayLapKH,\n" + "        sum(a.hshcXayLapWo) hshcXayLapWo,\n"
				+ "        nvl(ROUND(DECODE( sum(a.hshcXayLapKH),0,0,100* sum(a.hshcXayLapWo)/ sum(a.hshcXayLapKH)),2), 0) hshcXayLapPercent,\n"
				+ "        sum(a.qtKH) qtKH,sum(a.qtWo) qtWo,\n"
				+ "        nvl(ROUND(DECODE( sum(a.qtKH),0,0,100* sum(a.qtWo)/ sum(a.qtKH)),2),0) qtPercent,\n"
				+ "        sum(a.qtGPDNKH) qtGPDNKH,\n" + "        sum(a.qtGPDNWo) qtGPDNWo,\n"
				+ "        nvl(ROUND(DECODE( sum(a.qtGPDNKH),0,0,100* sum(a.qtGPDNWo)/ sum(a.qtGPDNKH)),2),0) qtGPDNPercent,\n"
				+ "        sum(a.qtSLXLKH) qtSLXLKH,sum(a.qtSLXLWo) qtSLXLWo,\n"
				+ "        nvl(ROUND(DECODE( sum(a.qtSLXLKH),0,0,100* sum(a.qtSLXLWo)/ sum(a.qtSLXLKH)), 2),0) qtSLXLPercent,\n"
				+ "        sum(a.slKH) slKH,sum(a.slWo) slWo, nvl(ROUND(DECODE( sum(a.slKH),0,0,100* sum(a.slWo)/ sum(a.slKH)),2),0) slPercent,sum(a.slGPDNKH) slGPDNKH,\n"
				+ "        sum(a.slGPDNWo) slGPDNWo,nvl(ROUND(DECODE( sum(a.slGPDNKH),0,0,100* sum(a.slGPDNWo)/ sum(a.slGPDNKH)),2),0) slGPDNPercent,\n"
				+ "        sum(a.tkvKH) tkvKH,sum(tkvWo) tkvWo,nvl(ROUND(DECODE( sum(a.tkvKH),0,0,100* sum(a.tkvWo)/ sum(a.tkvKH)),2),0) tkvPercent,\n"
				+ "        sum(a.thdtKH) thdtKH,sum(a.thdtWo) thdtWo,nvl(ROUND(DECODE( sum(a.thdtKH),0,0,100* sum(a.thdtWo)/ sum(a.thdtKH)),2),0) thdtPercent,\n"
				+ "        sum(a.ttkKH) ttkKH,sum(a.ttkWo) ttkWo,nvl(ROUND(DECODE( sum(a.ttkKH),0,0,100* sum(a.ttkWo)/ sum(a.ttkKH)),2),0) ttkPercent,\n"
				+ "        sum(a.xdXMKH) xdXMKH,sum(a.xdXMWo) xdXMWo,nvl(ROUND(DECODE( sum(a.xdXMKH),0,0,100* sum(a.xdXMWo)/ sum(a.xdXMKH)),2),0) xdXMPercent,\n"
				+ "        sum(a.xDBKH) xDBKH,sum(a.xDBWo) xDBWo,nvl(ROUND(DECODE( sum(a.xDBKH),0,0,100* sum(a.xDBWo)/ sum(a.xDBKH)),2),0) xDBPercent,\n"
				+ "        sum(a.tmbKH) tmbKH,sum(a.tmbWo) tmbWo,nvl(ROUND(DECODE( sum(a.tmbKH),0,0,100* sum(a.tmbWo)/ sum(a.tmbKH)),2),0) tmbPercent,\n"
				+ "        sum(a.salaryDat) salaryDat,sum(a.salaryThuong) salaryThuong,sum(a.hshcXayLapDat) hshcXayLapDat,sum(a.hshcXayLapThuong) hshcXayLapThuong,\n"
				+ "        sum(a.qtDat) qtDat,sum(a.qtThuong) qtThuong,sum(a.qtGPDNDat) qtGPDNDat,sum(a.qtGPDNThuong) qtGPDNThuong,sum(a.qtSLXLDat) qtSLXLDat,sum(a.qtSLXLThuong) qtSLXLThuong,\n"
				+ "        sum(a.slDat) slDat,sum(a.slThuong) slThuong,sum(a.slGPDNDat) slGPDNDat,sum(a.slGPDNThuong) slGPDNThuong,sum(a.tkvDat) tkvDat,\n"
				+ "        sum(a.tkvThuong) tkvThuong,sum(a.thdtDat) thdtDat,sum(a.thdtThuong) thdtThuong,sum(a.ttkDat) ttkDat,sum(a.ttkThuong) ttkThuong,\n"
				+ "        sum(a.xdXMDat) xdXMDat,sum(a.xdXMThuong) xdXMThuong,sum(a.xDBDat) xDBDat,sum(a.xDBThuong) xDBThuong,\n"
				+ "        sum(a.tmbDat) tmbDat,sum(a.tmbThuong) tmbThuong,sum(a.diemThuongTong) diemThuongTong,sum(a.diemDatTong) diemDatTong,\n"
				+ "        sum(a.tongDiem) tongDiem,sum(a.quyDoiDiem) quyDoiDiem  \n" + "    FROM\n"
				+ "        RP_WO_KPI a \n" + "    WHERE\n" + "        1 = 1 \n" + "        AND  a.areacode = 'KV2' ");
		if (obj.getSysGroupId() != null) {
			sql.append(" AND a.sysGroupId =:sysGroupId ");
		}
		if (obj.getYear() != null && obj.getMonth() != null) {
			sql.append(" AND a.month = :month AND a.year = :year");
		}
		sql.append(" GROUP BY month,year ");
		sql.append("UNION ALL SELECT\n" + "        '2' type,\n" + "        TO_CHAR('KV3') areacode,\n"
				+ "        TO_CHAR('KV3') procode,\n" + "        a.month,\n" + "        a.year,\n"
				+ "        sum(a.salaryMonth) salaryMonth,\n" + "        sum(a.salaryWo) salaryWo,\n"
				+ "        nvl(ROUND(DECODE( sum(a.salaryMonth),0,0,100* sum(a.salaryWo)/ sum(a.salaryMonth)),2),0) salaryPercent,\n"
				+ "        sum(a.hshcXayLapKH) hshcXayLapKH,\n" + "        sum(a.hshcXayLapWo) hshcXayLapWo,\n"
				+ "        nvl(ROUND(DECODE( sum(a.hshcXayLapKH),0,0,100* sum(a.hshcXayLapWo)/ sum(a.hshcXayLapKH)),2), 0) hshcXayLapPercent,\n"
				+ "        sum(a.qtKH) qtKH,sum(a.qtWo) qtWo,\n"
				+ "        nvl(ROUND(DECODE( sum(a.qtKH),0,0,100* sum(a.qtWo)/ sum(a.qtKH)),2),0) qtPercent,\n"
				+ "        sum(a.qtGPDNKH) qtGPDNKH,\n" + "        sum(a.qtGPDNWo) qtGPDNWo,\n"
				+ "        nvl(ROUND(DECODE( sum(a.qtGPDNKH),0,0,100* sum(a.qtGPDNWo)/ sum(a.qtGPDNKH)),2),0) qtGPDNPercent,\n"
				+ "        sum(a.qtSLXLKH) qtSLXLKH,sum(a.qtSLXLWo) qtSLXLWo,\n"
				+ "        nvl(ROUND(DECODE( sum(a.qtSLXLKH),0,0,100* sum(a.qtSLXLWo)/ sum(a.qtSLXLKH)), 2),0) qtSLXLPercent,\n"
				+ "        sum(a.slKH) slKH,sum(a.slWo) slWo, nvl(ROUND(DECODE( sum(a.slKH),0,0,100* sum(a.slWo)/ sum(a.slKH)),2),0) slPercent,sum(a.slGPDNKH) slGPDNKH,\n"
				+ "        sum(a.slGPDNWo) slGPDNWo,nvl(ROUND(DECODE( sum(a.slGPDNKH),0,0,100* sum(a.slGPDNWo)/ sum(a.slGPDNKH)),2),0) slGPDNPercent,\n"
				+ "        sum(a.tkvKH) tkvKH,sum(tkvWo) tkvWo,nvl(ROUND(DECODE( sum(a.tkvKH),0,0,100* sum(a.tkvWo)/ sum(a.tkvKH)),2),0) tkvPercent,\n"
				+ "        sum(a.thdtKH) thdtKH,sum(a.thdtWo) thdtWo,nvl(ROUND(DECODE( sum(a.thdtKH),0,0,100* sum(a.thdtWo)/ sum(a.thdtKH)),2),0) thdtPercent,\n"
				+ "        sum(a.ttkKH) ttkKH,sum(a.ttkWo) ttkWo,nvl(ROUND(DECODE( sum(a.ttkKH),0,0,100* sum(a.ttkWo)/ sum(a.ttkKH)),2),0) ttkPercent,\n"
				+ "        sum(a.xdXMKH) xdXMKH,sum(a.xdXMWo) xdXMWo,nvl(ROUND(DECODE( sum(a.xdXMKH),0,0,100* sum(a.xdXMWo)/ sum(a.xdXMKH)),2),0) xdXMPercent,\n"
				+ "        sum(a.xDBKH) xDBKH,sum(a.xDBWo) xDBWo,nvl(ROUND(DECODE( sum(a.xDBKH),0,0,100* sum(a.xDBWo)/ sum(a.xDBKH)),2),0) xDBPercent,\n"
				+ "        sum(a.tmbKH) tmbKH,sum(a.tmbWo) tmbWo,nvl(ROUND(DECODE( sum(a.tmbKH),0,0,100* sum(a.tmbWo)/ sum(a.tmbKH)),2),0) tmbPercent,\n"
				+ "        sum(a.salaryDat) salaryDat,sum(a.salaryThuong) salaryThuong,sum(a.hshcXayLapDat) hshcXayLapDat,sum(a.hshcXayLapThuong) hshcXayLapThuong,\n"
				+ "        sum(a.qtDat) qtDat,sum(a.qtThuong) qtThuong,sum(a.qtGPDNDat) qtGPDNDat,sum(a.qtGPDNThuong) qtGPDNThuong,sum(a.qtSLXLDat) qtSLXLDat,sum(a.qtSLXLThuong) qtSLXLThuong,\n"
				+ "        sum(a.slDat) slDat,sum(a.slThuong) slThuong,sum(a.slGPDNDat) slGPDNDat,sum(a.slGPDNThuong) slGPDNThuong,sum(a.tkvDat) tkvDat,\n"
				+ "        sum(a.tkvThuong) tkvThuong,sum(a.thdtDat) thdtDat,sum(a.thdtThuong) thdtThuong,sum(a.ttkDat) ttkDat,sum(a.ttkThuong) ttkThuong,\n"
				+ "        sum(a.xdXMDat) xdXMDat,sum(a.xdXMThuong) xdXMThuong,sum(a.xDBDat) xDBDat,sum(a.xDBThuong) xDBThuong,\n"
				+ "        sum(a.tmbDat) tmbDat,sum(a.tmbThuong) tmbThuong,sum(a.diemThuongTong) diemThuongTong,sum(a.diemDatTong) diemDatTong,\n"
				+ "        sum(a.tongDiem) tongDiem,sum(a.quyDoiDiem) quyDoiDiem  \n" + "    FROM\n"
				+ "        RP_WO_KPI a \n" + "    WHERE\n" + "        1 = 1 \n" + "        AND  a.areacode = 'KV3' ");
		if (obj.getSysGroupId() != null) {
			sql.append(" AND a.sysGroupId =:sysGroupId ");
		}
		if (obj.getYear() != null && obj.getMonth() != null) {
			sql.append(" AND a.month = :month AND a.year = :year");
		}
		sql.append(" GROUP BY month,year ");
		sql.append(" UNION ALL\n"
				+ "    SELECT '3' type,to_char(a.areaCode) areaCode,to_char(a.proCode) proCode, a.month, a.year, salaryMonth,salaryWo, salaryPercent, \n"
				+ "                hshcXayLapKH,hshcXayLapWo,hshcXayLapPercent,qtKH,qtWo,qtPercent,qtGPDNKH,qtGPDNWo,qtGPDNPercent,\n"
				+ "                qtSLXLKH,qtSLXLWo,qtSLXLPercent,\n"
				+ "                slKH,slWo,slPercent,slGPDNKH,slGPDNWo,slGPDNPercent,\n"
				+ "                tkvKH,tkvWo,tkvPercent,thdtKH,thdtWo,thdtPercent,ttkKH,ttkWo,ttkPercent,xdXMKH,xdXMWo,xdXMPercent,\n"
				+ "                xDBKH,xDBWo,xDBPercent,tmbKH,tmbWo,tmbPercent,    a.salaryDat,a.salaryThuong,a.hshcXayLapDat,a.hshcXayLapThuong,a.qtDat,a.qtThuong,\n"
				+ "                a.qtGPDNDat,a.qtGPDNThuong,a.qtSLXLDat,a.qtSLXLThuong,a.slDat,a.slThuong\n"
				+ "                ,a.slGPDNDat,a.slGPDNThuong,a.tkvDat,a.tkvThuong,a.thdtDat,a.thdtThuong\n"
				+ "                ,a.ttkDat,a.ttkThuong,a.xdXMDat,a.xdXMThuong,a.xDBDat,a.xDBThuong\n"
				+ "                ,a.tmbDat,a.tmbThuong,a.diemThuongTong,a.diemDatTong,a.tongDiem,a.quyDoiDiem \n"
				+ "                FROM\n" + "                    RP_WO_KPI a \n" + "                    where  1=1 ");
		if (obj.getSysGroupId() != null) {
			sql.append(" AND a.sysGroupId =:sysGroupId ");
		}
		if (obj.getYear() != null && obj.getMonth() != null) {
			sql.append(" AND a.month = :month AND a.year = :year");
		}
		sql.append(" ORDER BY type,areacode,procode");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("type", new StringType());
		query.addScalar("areaCode", new StringType());
		query.addScalar("proCode", new StringType());
		query.addScalar("month", new StringType());
		query.addScalar("year", new StringType());
		query.addScalar("salaryMonth", new DoubleType());
		query.addScalar("salaryWo", new DoubleType());
		query.addScalar("salaryPercent", new DoubleType());
		query.addScalar("hshcXayLapKH", new DoubleType());
		query.addScalar("hshcXayLapWo", new DoubleType());
		query.addScalar("hshcXayLapPercent", new DoubleType());
		query.addScalar("qtKH", new DoubleType());
		query.addScalar("qtWo", new DoubleType());
		query.addScalar("qtPercent", new DoubleType());
		query.addScalar("qtGPDNKH", new DoubleType());
		query.addScalar("qtGPDNWo", new DoubleType());
		query.addScalar("qtGPDNPercent", new DoubleType());
		query.addScalar("qtSLXLKH", new DoubleType());
		query.addScalar("qtSLXLWo", new DoubleType());
		query.addScalar("qtSLXLPercent", new DoubleType());
		query.addScalar("slKH", new DoubleType());
		query.addScalar("slWo", new DoubleType());
		query.addScalar("slPercent", new DoubleType());
		query.addScalar("slGPDNKH", new DoubleType());
		query.addScalar("slGPDNWo", new DoubleType());
		query.addScalar("slGPDNPercent", new DoubleType());
		query.addScalar("tkvKH", new DoubleType());
		query.addScalar("tkvWo", new DoubleType());
		query.addScalar("tkvPercent", new DoubleType());
		query.addScalar("thdtKH", new DoubleType());
		query.addScalar("thdtWo", new DoubleType());
		query.addScalar("thdtPercent", new DoubleType());
		query.addScalar("ttkKH", new DoubleType());
		query.addScalar("ttkWo", new DoubleType());
		query.addScalar("ttkPercent", new DoubleType());
		query.addScalar("xdXMKH", new DoubleType());
		query.addScalar("xdXMWo", new DoubleType());
		query.addScalar("xdXMPercent", new DoubleType());
		query.addScalar("xDBKH", new DoubleType());
		query.addScalar("xDBWo", new DoubleType());
		query.addScalar("xDBPercent", new DoubleType());
		query.addScalar("tmbKH", new DoubleType());
		query.addScalar("tmbWo", new DoubleType());
		query.addScalar("tmbPercent", new DoubleType());
		//
		query.addScalar("salaryDat", new DoubleType());
		query.addScalar("salaryThuong", new DoubleType());
		query.addScalar("hshcXayLapDat", new DoubleType());
		query.addScalar("hshcXayLapThuong", new DoubleType());
		query.addScalar("qtDat", new DoubleType());
		query.addScalar("qtThuong", new DoubleType());
		query.addScalar("qtGPDNDat", new DoubleType());
		query.addScalar("qtGPDNThuong", new DoubleType());
		query.addScalar("qtSLXLDat", new DoubleType());
		query.addScalar("qtSLXLThuong", new DoubleType());
		query.addScalar("slDat", new DoubleType());
		query.addScalar("slThuong", new DoubleType());
		query.addScalar("slGPDNDat", new DoubleType());
		query.addScalar("slGPDNThuong", new DoubleType());
		query.addScalar("tkvDat", new DoubleType());
		query.addScalar("tkvThuong", new DoubleType());
		query.addScalar("thdtDat", new DoubleType());
		query.addScalar("thdtThuong", new DoubleType());
		query.addScalar("ttkDat", new DoubleType());
		query.addScalar("ttkThuong", new DoubleType());
		query.addScalar("xdXMDat", new DoubleType());
		query.addScalar("xdXMThuong", new DoubleType());
		query.addScalar("xDBDat", new DoubleType());
		query.addScalar("xDBThuong", new DoubleType());
		query.addScalar("tmbDat", new DoubleType());
		query.addScalar("tmbThuong", new DoubleType());
		query.addScalar("diemDatTong", new DoubleType());
		query.addScalar("diemThuongTong", new DoubleType());
		query.addScalar("tongDiem", new DoubleType());
		query.addScalar("quyDoiDiem", new DoubleType());
		if (obj.getSysGroupId() != null) {
			query.setParameter("sysGroupId", obj.getSysGroupId());
		}
		if (obj.getMonth() != null) {
			query.setParameter("month", obj.getMonth());
		}
		if (obj.getYear() != null) {
			query.setParameter("year", obj.getYear());
		}
		if (obj.getSysGroupId() != null) {
			query.setParameter("sysGroupId", obj.getSysGroupId());
		}
		if (obj.getMonth() != null) {
			query.setParameter("month", obj.getMonth());
		}
		if (obj.getYear() != null) {
			query.setParameter("year", obj.getYear());
		}
		if (obj.getSysGroupId() != null) {
			query.setParameter("sysGroupId", obj.getSysGroupId());
		}
		if (obj.getMonth() != null) {
			query.setParameter("month", obj.getMonth());
		}
		if (obj.getYear() != null) {
			query.setParameter("year", obj.getYear());
		}
		if (obj.getSysGroupId() != null) {
			query.setParameter("sysGroupId", obj.getSysGroupId());
		}
		if (obj.getMonth() != null) {
			query.setParameter("month", obj.getMonth());
		}
		if (obj.getYear() != null) {
			query.setParameter("year", obj.getYear());
		}
		if (obj.getSysGroupId() != null) {
			query.setParameter("sysGroupId", obj.getSysGroupId());
		}
		if (obj.getMonth() != null) {
			query.setParameter("month", obj.getMonth());
		}
		if (obj.getYear() != null) {
			query.setParameter("year", obj.getYear());
		}
		query.setResultTransformer(Transformers.aliasToBean(RpWoDetailOsDTO.class));
		List<RpWoDetailOsDTO> lst = query.list();
		obj.setTotalRecord(lst.size());
		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		List<RpWoDetailOsDTO> lstNew = query.list();
		return lstNew;
	}

	// EX báo cáo Kpi
	public List<RpWoDetailOsDTO> exportFileWoKpi(ReportWoDTO obj) {
		StringBuilder sql = new StringBuilder("SELECT\n" + "    '1' type,\n" + "    TO_CHAR('Toàn quốc') areacode,\n"
				+ "    TO_CHAR('Toàn quốc') procode,\n" + "    a.month,\n" + "    a.year,\n"
				+ "   sum(a.salaryMonth) salaryMonth,sum(a.salaryWo) salaryWo, nvl(ROUND(DECODE( sum(a.salaryMonth),0,0,100* sum(a.salaryWo)/ sum(a.salaryMonth)),2),0) salaryPercent, \n"
				+ "sum(a.hshcXayLapKH) hshcXayLapKH,sum(a.hshcXayLapWo) hshcXayLapWo,nvl(ROUND(DECODE( sum(a.hshcXayLapKH),0,0,100* sum(a.hshcXayLapWo)/ sum(a.hshcXayLapKH)),2),0) hshcXayLapPercent,\n"
				+ "sum(a.qtKH) qtKH,sum(a.qtWo) qtWo, nvl(ROUND(DECODE( sum(a.qtKH),0,0,100* sum(a.qtWo)/ sum(a.qtKH)),2),0) qtPercent,\n"
				+ "sum(a.qtGPDNKH) qtGPDNKH,sum(a.qtGPDNWo) qtGPDNWo,nvl(ROUND(DECODE( sum(a.qtGPDNKH),0,0,100* sum(a.qtGPDNWo)/ sum(a.qtGPDNKH)),2),0) qtGPDNPercent,\n"
				+ "sum(a.qtSLXLKH) qtSLXLKH,sum(a.qtSLXLWo) qtSLXLWo,nvl(ROUND(DECODE( sum(a.qtSLXLKH),0,0,100* sum(a.qtSLXLWo)/ sum(a.qtSLXLKH)),2),0) qtSLXLPercent,\n"
				+ "sum(a.slKH) slKH,sum(a.slWo) slWo,nvl(ROUND(DECODE( sum(a.slKH),0,0,100* sum(a.slWo)/ sum(a.slKH)),2),0) slPercent,\n"
				+ "sum(a.slGPDNKH) slGPDNKH,sum(a.slGPDNWo) slGPDNWo,nvl(ROUND(DECODE( sum(a.slGPDNKH),0,0,100* sum(a.slGPDNWo)/ sum(a.slGPDNKH)),2),0) slGPDNPercent,\n"
				+ "sum(a.tkvKH) tkvKH,sum(tkvWo) tkvWo,nvl(ROUND(DECODE( sum(a.tkvKH),0,0,100* sum(a.tkvWo)/ sum(a.tkvKH)),2),0) tkvPercent,\n"
				+ "sum(a.thdtKH) thdtKH,sum(a.thdtWo) thdtWo,nvl(ROUND(DECODE( sum(a.thdtKH),0,0,100* sum(a.thdtWo)/ sum(a.thdtKH)),2),0) thdtPercent,\n"
				+ "sum(a.ttkKH) ttkKH,sum(a.ttkWo) ttkWo, nvl(ROUND(DECODE( sum(a.ttkKH),0,0,100* sum(a.ttkWo)/ sum(a.ttkKH)),2),0) ttkPercent,\n"
				+ "sum(a.xdXMKH) xdXMKH,sum(a.xdXMWo) xdXMWo,nvl(ROUND(DECODE( sum(a.xdXMKH),0,0,100* sum(a.xdXMWo)/ sum(a.xdXMKH)),2),0) xdXMPercent,\n"
				+ "sum(a.xDBKH) xDBKH,sum(a.xDBWo) xDBWo,nvl(ROUND(DECODE( sum(a.xDBKH),0,0,100* sum(a.xDBWo)/ sum(a.xDBKH)),2),0) xDBPercent,\n"
				+ "sum(a.tmbKH) tmbKH,sum(a.tmbWo) tmbWo,nvl(ROUND(DECODE( sum(a.tmbKH),0,0,100* sum(a.tmbWo)/ sum(a.tmbKH)),2),0) tmbPercent,"
				+ "sum(a.salaryDat) salaryDat,sum(a.salaryThuong) salaryThuong,sum(a.hshcXayLapDat) hshcXayLapDat,sum(a.hshcXayLapThuong) hshcXayLapThuong,sum(a.qtDat) qtDat,sum(a.qtThuong) qtThuong,\n"
				+ "               sum(a.qtGPDNDat) qtGPDNDat,sum(a.qtGPDNThuong) qtGPDNThuong,sum(a.qtSLXLDat) qtSLXLDat,sum(a.qtSLXLThuong) qtSLXLThuong,sum(a.slDat) slDat,sum(a.slThuong) slThuong\n"
				+ "               ,sum(a.slGPDNDat) slGPDNDat,sum(a.slGPDNThuong) slGPDNThuong,sum(a.tkvDat) tkvDat,sum(a.tkvThuong) tkvThuong,sum(a.thdtDat) thdtDat,sum(a.thdtThuong) thdtThuong\n"
				+ "               ,sum(a.ttkDat) ttkDat,sum(a.ttkThuong) ttkThuong,sum(a.xdXMDat) xdXMDat,sum(a.xdXMThuong) xdXMThuong,sum(a.xDBDat) xDBDat,sum(a.xDBThuong) xDBThuong\n"
				+ "               ,sum(a.tmbDat) tmbDat,sum(a.tmbThuong) tmbThuong,sum(a.diemThuongTong) diemThuongTong,sum(a.diemDatTong) diemDatTong,sum(a.tongDiem) tongDiem,sum(a.quyDoiDiem) quyDoiDiem \n"
				+ "FROM\n" + "    RP_WO_KPI a\n" + "WHERE 1 = 1");
		if (obj.getSysGroupId() != null) {
			sql.append(" AND a.sysGroupId =:sysGroupId ");
		}
		if (obj.getYear() != null && obj.getMonth() != null) {
			sql.append(" AND a.month = :month AND a.year = :year");
		}
		sql.append(" GROUP BY month,year ");
		sql.append("UNION ALL SELECT\n" + "        '2' type,\n" + "        TO_CHAR('KV1') areacode,\n"
				+ "        TO_CHAR('KV1') procode,\n" + "        a.month,\n" + "        a.year,\n"
				+ "        sum(a.salaryMonth) salaryMonth,\n" + "        sum(a.salaryWo) salaryWo,\n"
				+ "        nvl(ROUND(DECODE( sum(a.salaryMonth),0,0,100* sum(a.salaryWo)/ sum(a.salaryMonth)),2),0) salaryPercent,\n"
				+ "        sum(a.hshcXayLapKH) hshcXayLapKH,\n" + "        sum(a.hshcXayLapWo) hshcXayLapWo,\n"
				+ "        nvl(ROUND(DECODE( sum(a.hshcXayLapKH),0,0,100* sum(a.hshcXayLapWo)/ sum(a.hshcXayLapKH)),2), 0) hshcXayLapPercent,\n"
				+ "        sum(a.qtKH) qtKH,sum(a.qtWo) qtWo,\n"
				+ "        nvl(ROUND(DECODE( sum(a.qtKH),0,0,100* sum(a.qtWo)/ sum(a.qtKH)),2),0) qtPercent,\n"
				+ "        sum(a.qtGPDNKH) qtGPDNKH,\n" + "        sum(a.qtGPDNWo) qtGPDNWo,\n"
				+ "        nvl(ROUND(DECODE( sum(a.qtGPDNKH),0,0,100* sum(a.qtGPDNWo)/ sum(a.qtGPDNKH)),2),0) qtGPDNPercent,\n"
				+ "        sum(a.qtSLXLKH) qtSLXLKH,sum(a.qtSLXLWo) qtSLXLWo,\n"
				+ "        nvl(ROUND(DECODE( sum(a.qtSLXLKH),0,0,100* sum(a.qtSLXLWo)/ sum(a.qtSLXLKH)), 2),0) qtSLXLPercent,\n"
				+ "        sum(a.slKH) slKH,sum(a.slWo) slWo, nvl(ROUND(DECODE( sum(a.slKH),0,0,100* sum(a.slWo)/ sum(a.slKH)),2),0) slPercent,sum(a.slGPDNKH) slGPDNKH,\n"
				+ "        sum(a.slGPDNWo) slGPDNWo,nvl(ROUND(DECODE( sum(a.slGPDNKH),0,0,100* sum(a.slGPDNWo)/ sum(a.slGPDNKH)),2),0) slGPDNPercent,\n"
				+ "        sum(a.tkvKH) tkvKH,sum(tkvWo) tkvWo,nvl(ROUND(DECODE( sum(a.tkvKH),0,0,100* sum(a.tkvWo)/ sum(a.tkvKH)),2),0) tkvPercent,\n"
				+ "        sum(a.thdtKH) thdtKH,sum(a.thdtWo) thdtWo,nvl(ROUND(DECODE( sum(a.thdtKH),0,0,100* sum(a.thdtWo)/ sum(a.thdtKH)),2),0) thdtPercent,\n"
				+ "        sum(a.ttkKH) ttkKH,sum(a.ttkWo) ttkWo,nvl(ROUND(DECODE( sum(a.ttkKH),0,0,100* sum(a.ttkWo)/ sum(a.ttkKH)),2),0) ttkPercent,\n"
				+ "        sum(a.xdXMKH) xdXMKH,sum(a.xdXMWo) xdXMWo,nvl(ROUND(DECODE( sum(a.xdXMKH),0,0,100* sum(a.xdXMWo)/ sum(a.xdXMKH)),2),0) xdXMPercent,\n"
				+ "        sum(a.xDBKH) xDBKH,sum(a.xDBWo) xDBWo,nvl(ROUND(DECODE( sum(a.xDBKH),0,0,100* sum(a.xDBWo)/ sum(a.xDBKH)),2),0) xDBPercent,\n"
				+ "        sum(a.tmbKH) tmbKH,sum(a.tmbWo) tmbWo,nvl(ROUND(DECODE( sum(a.tmbKH),0,0,100* sum(a.tmbWo)/ sum(a.tmbKH)),2),0) tmbPercent,\n"
				+ "        sum(a.salaryDat) salaryDat,sum(a.salaryThuong) salaryThuong,sum(a.hshcXayLapDat) hshcXayLapDat,sum(a.hshcXayLapThuong) hshcXayLapThuong,\n"
				+ "        sum(a.qtDat) qtDat,sum(a.qtThuong) qtThuong,sum(a.qtGPDNDat) qtGPDNDat,sum(a.qtGPDNThuong) qtGPDNThuong,sum(a.qtSLXLDat) qtSLXLDat,sum(a.qtSLXLThuong) qtSLXLThuong,\n"
				+ "        sum(a.slDat) slDat,sum(a.slThuong) slThuong,sum(a.slGPDNDat) slGPDNDat,sum(a.slGPDNThuong) slGPDNThuong,sum(a.tkvDat) tkvDat,\n"
				+ "        sum(a.tkvThuong) tkvThuong,sum(a.thdtDat) thdtDat,sum(a.thdtThuong) thdtThuong,sum(a.ttkDat) ttkDat,sum(a.ttkThuong) ttkThuong,\n"
				+ "        sum(a.xdXMDat) xdXMDat,sum(a.xdXMThuong) xdXMThuong,sum(a.xDBDat) xDBDat,sum(a.xDBThuong) xDBThuong,\n"
				+ "        sum(a.tmbDat) tmbDat,sum(a.tmbThuong) tmbThuong,sum(a.diemThuongTong) diemThuongTong,sum(a.diemDatTong) diemDatTong,\n"
				+ "        sum(a.tongDiem) tongDiem,sum(a.quyDoiDiem) quyDoiDiem  \n" + "    FROM\n"
				+ "        RP_WO_KPI a \n" + "    WHERE\n" + "        1 = 1 \n" + "        AND  a.areacode = 'KV1'");
		if (obj.getSysGroupId() != null) {
			sql.append(" AND a.sysGroupId =:sysGroupId ");
		}
		if (obj.getYear() != null && obj.getMonth() != null) {
			sql.append(" AND a.month = :month AND a.year = :year");
		}
		sql.append(" GROUP BY month,year ");
		sql.append("UNION ALL SELECT\n" + "        '2' type,\n" + "        TO_CHAR('KV2') areacode,\n"
				+ "        TO_CHAR('KV2') procode,\n" + "        a.month,\n" + "        a.year,\n"
				+ "        sum(a.salaryMonth) salaryMonth,\n" + "        sum(a.salaryWo) salaryWo,\n"
				+ "        nvl(ROUND(DECODE( sum(a.salaryMonth),0,0,100* sum(a.salaryWo)/ sum(a.salaryMonth)),2),0) salaryPercent,\n"
				+ "        sum(a.hshcXayLapKH) hshcXayLapKH,\n" + "        sum(a.hshcXayLapWo) hshcXayLapWo,\n"
				+ "        nvl(ROUND(DECODE( sum(a.hshcXayLapKH),0,0,100* sum(a.hshcXayLapWo)/ sum(a.hshcXayLapKH)),2), 0) hshcXayLapPercent,\n"
				+ "        sum(a.qtKH) qtKH,sum(a.qtWo) qtWo,\n"
				+ "        nvl(ROUND(DECODE( sum(a.qtKH),0,0,100* sum(a.qtWo)/ sum(a.qtKH)),2),0) qtPercent,\n"
				+ "        sum(a.qtGPDNKH) qtGPDNKH,\n" + "        sum(a.qtGPDNWo) qtGPDNWo,\n"
				+ "        nvl(ROUND(DECODE( sum(a.qtGPDNKH),0,0,100* sum(a.qtGPDNWo)/ sum(a.qtGPDNKH)),2),0) qtGPDNPercent,\n"
				+ "        sum(a.qtSLXLKH) qtSLXLKH,sum(a.qtSLXLWo) qtSLXLWo,\n"
				+ "        nvl(ROUND(DECODE( sum(a.qtSLXLKH),0,0,100* sum(a.qtSLXLWo)/ sum(a.qtSLXLKH)), 2),0) qtSLXLPercent,\n"
				+ "        sum(a.slKH) slKH,sum(a.slWo) slWo, nvl(ROUND(DECODE( sum(a.slKH),0,0,100* sum(a.slWo)/ sum(a.slKH)),2),0) slPercent,sum(a.slGPDNKH) slGPDNKH,\n"
				+ "        sum(a.slGPDNWo) slGPDNWo,nvl(ROUND(DECODE( sum(a.slGPDNKH),0,0,100* sum(a.slGPDNWo)/ sum(a.slGPDNKH)),2),0) slGPDNPercent,\n"
				+ "        sum(a.tkvKH) tkvKH,sum(tkvWo) tkvWo,nvl(ROUND(DECODE( sum(a.tkvKH),0,0,100* sum(a.tkvWo)/ sum(a.tkvKH)),2),0) tkvPercent,\n"
				+ "        sum(a.thdtKH) thdtKH,sum(a.thdtWo) thdtWo,nvl(ROUND(DECODE( sum(a.thdtKH),0,0,100* sum(a.thdtWo)/ sum(a.thdtKH)),2),0) thdtPercent,\n"
				+ "        sum(a.ttkKH) ttkKH,sum(a.ttkWo) ttkWo,nvl(ROUND(DECODE( sum(a.ttkKH),0,0,100* sum(a.ttkWo)/ sum(a.ttkKH)),2),0) ttkPercent,\n"
				+ "        sum(a.xdXMKH) xdXMKH,sum(a.xdXMWo) xdXMWo,nvl(ROUND(DECODE( sum(a.xdXMKH),0,0,100* sum(a.xdXMWo)/ sum(a.xdXMKH)),2),0) xdXMPercent,\n"
				+ "        sum(a.xDBKH) xDBKH,sum(a.xDBWo) xDBWo,nvl(ROUND(DECODE( sum(a.xDBKH),0,0,100* sum(a.xDBWo)/ sum(a.xDBKH)),2),0) xDBPercent,\n"
				+ "        sum(a.tmbKH) tmbKH,sum(a.tmbWo) tmbWo,nvl(ROUND(DECODE( sum(a.tmbKH),0,0,100* sum(a.tmbWo)/ sum(a.tmbKH)),2),0) tmbPercent,\n"
				+ "        sum(a.salaryDat) salaryDat,sum(a.salaryThuong) salaryThuong,sum(a.hshcXayLapDat) hshcXayLapDat,sum(a.hshcXayLapThuong) hshcXayLapThuong,\n"
				+ "        sum(a.qtDat) qtDat,sum(a.qtThuong) qtThuong,sum(a.qtGPDNDat) qtGPDNDat,sum(a.qtGPDNThuong) qtGPDNThuong,sum(a.qtSLXLDat) qtSLXLDat,sum(a.qtSLXLThuong) qtSLXLThuong,\n"
				+ "        sum(a.slDat) slDat,sum(a.slThuong) slThuong,sum(a.slGPDNDat) slGPDNDat,sum(a.slGPDNThuong) slGPDNThuong,sum(a.tkvDat) tkvDat,\n"
				+ "        sum(a.tkvThuong) tkvThuong,sum(a.thdtDat) thdtDat,sum(a.thdtThuong) thdtThuong,sum(a.ttkDat) ttkDat,sum(a.ttkThuong) ttkThuong,\n"
				+ "        sum(a.xdXMDat) xdXMDat,sum(a.xdXMThuong) xdXMThuong,sum(a.xDBDat) xDBDat,sum(a.xDBThuong) xDBThuong,\n"
				+ "        sum(a.tmbDat) tmbDat,sum(a.tmbThuong) tmbThuong,sum(a.diemThuongTong) diemThuongTong,sum(a.diemDatTong) diemDatTong,\n"
				+ "        sum(a.tongDiem) tongDiem,sum(a.quyDoiDiem) quyDoiDiem  \n" + "    FROM\n"
				+ "        RP_WO_KPI a \n" + "    WHERE\n" + "        1 = 1 \n" + "        AND  a.areacode = 'KV2' ");
		if (obj.getSysGroupId() != null) {
			sql.append(" AND a.sysGroupId =:sysGroupId ");
		}
		if (obj.getYear() != null && obj.getMonth() != null) {
			sql.append(" AND a.month = :month AND a.year = :year");
		}
		sql.append(" GROUP BY month,year ");
		sql.append("UNION ALL SELECT\n" + "        '2' type,\n" + "        TO_CHAR('KV3') areacode,\n"
				+ "        TO_CHAR('KV3') procode,\n" + "        a.month,\n" + "        a.year,\n"
				+ "        sum(a.salaryMonth) salaryMonth,\n" + "        sum(a.salaryWo) salaryWo,\n"
				+ "        nvl(ROUND(DECODE( sum(a.salaryMonth),0,0,100* sum(a.salaryWo)/ sum(a.salaryMonth)),2),0) salaryPercent,\n"
				+ "        sum(a.hshcXayLapKH) hshcXayLapKH,\n" + "        sum(a.hshcXayLapWo) hshcXayLapWo,\n"
				+ "        nvl(ROUND(DECODE( sum(a.hshcXayLapKH),0,0,100* sum(a.hshcXayLapWo)/ sum(a.hshcXayLapKH)),2), 0) hshcXayLapPercent,\n"
				+ "        sum(a.qtKH) qtKH,sum(a.qtWo) qtWo,\n"
				+ "        nvl(ROUND(DECODE( sum(a.qtKH),0,0,100* sum(a.qtWo)/ sum(a.qtKH)),2),0) qtPercent,\n"
				+ "        sum(a.qtGPDNKH) qtGPDNKH,\n" + "        sum(a.qtGPDNWo) qtGPDNWo,\n"
				+ "        nvl(ROUND(DECODE( sum(a.qtGPDNKH),0,0,100* sum(a.qtGPDNWo)/ sum(a.qtGPDNKH)),2),0) qtGPDNPercent,\n"
				+ "        sum(a.qtSLXLKH) qtSLXLKH,sum(a.qtSLXLWo) qtSLXLWo,\n"
				+ "        nvl(ROUND(DECODE( sum(a.qtSLXLKH),0,0,100* sum(a.qtSLXLWo)/ sum(a.qtSLXLKH)), 2),0) qtSLXLPercent,\n"
				+ "        sum(a.slKH) slKH,sum(a.slWo) slWo, nvl(ROUND(DECODE( sum(a.slKH),0,0,100* sum(a.slWo)/ sum(a.slKH)),2),0) slPercent,sum(a.slGPDNKH) slGPDNKH,\n"
				+ "        sum(a.slGPDNWo) slGPDNWo,nvl(ROUND(DECODE( sum(a.slGPDNKH),0,0,100* sum(a.slGPDNWo)/ sum(a.slGPDNKH)),2),0) slGPDNPercent,\n"
				+ "        sum(a.tkvKH) tkvKH,sum(tkvWo) tkvWo,nvl(ROUND(DECODE( sum(a.tkvKH),0,0,100* sum(a.tkvWo)/ sum(a.tkvKH)),2),0) tkvPercent,\n"
				+ "        sum(a.thdtKH) thdtKH,sum(a.thdtWo) thdtWo,nvl(ROUND(DECODE( sum(a.thdtKH),0,0,100* sum(a.thdtWo)/ sum(a.thdtKH)),2),0) thdtPercent,\n"
				+ "        sum(a.ttkKH) ttkKH,sum(a.ttkWo) ttkWo,nvl(ROUND(DECODE( sum(a.ttkKH),0,0,100* sum(a.ttkWo)/ sum(a.ttkKH)),2),0) ttkPercent,\n"
				+ "        sum(a.xdXMKH) xdXMKH,sum(a.xdXMWo) xdXMWo,nvl(ROUND(DECODE( sum(a.xdXMKH),0,0,100* sum(a.xdXMWo)/ sum(a.xdXMKH)),2),0) xdXMPercent,\n"
				+ "        sum(a.xDBKH) xDBKH,sum(a.xDBWo) xDBWo,nvl(ROUND(DECODE( sum(a.xDBKH),0,0,100* sum(a.xDBWo)/ sum(a.xDBKH)),2),0) xDBPercent,\n"
				+ "        sum(a.tmbKH) tmbKH,sum(a.tmbWo) tmbWo,nvl(ROUND(DECODE( sum(a.tmbKH),0,0,100* sum(a.tmbWo)/ sum(a.tmbKH)),2),0) tmbPercent,\n"
				+ "        sum(a.salaryDat) salaryDat,sum(a.salaryThuong) salaryThuong,sum(a.hshcXayLapDat) hshcXayLapDat,sum(a.hshcXayLapThuong) hshcXayLapThuong,\n"
				+ "        sum(a.qtDat) qtDat,sum(a.qtThuong) qtThuong,sum(a.qtGPDNDat) qtGPDNDat,sum(a.qtGPDNThuong) qtGPDNThuong,sum(a.qtSLXLDat) qtSLXLDat,sum(a.qtSLXLThuong) qtSLXLThuong,\n"
				+ "        sum(a.slDat) slDat,sum(a.slThuong) slThuong,sum(a.slGPDNDat) slGPDNDat,sum(a.slGPDNThuong) slGPDNThuong,sum(a.tkvDat) tkvDat,\n"
				+ "        sum(a.tkvThuong) tkvThuong,sum(a.thdtDat) thdtDat,sum(a.thdtThuong) thdtThuong,sum(a.ttkDat) ttkDat,sum(a.ttkThuong) ttkThuong,\n"
				+ "        sum(a.xdXMDat) xdXMDat,sum(a.xdXMThuong) xdXMThuong,sum(a.xDBDat) xDBDat,sum(a.xDBThuong) xDBThuong,\n"
				+ "        sum(a.tmbDat) tmbDat,sum(a.tmbThuong) tmbThuong,sum(a.diemThuongTong) diemThuongTong,sum(a.diemDatTong) diemDatTong,\n"
				+ "        sum(a.tongDiem) tongDiem,sum(a.quyDoiDiem) quyDoiDiem  \n" + "    FROM\n"
				+ "        RP_WO_KPI a \n" + "    WHERE\n" + "        1 = 1 \n" + "        AND  a.areacode = 'KV3' ");
		if (obj.getSysGroupId() != null) {
			sql.append(" AND a.sysGroupId =:sysGroupId ");
		}
		if (obj.getYear() != null && obj.getMonth() != null) {
			sql.append(" AND a.month = :month AND a.year = :year");
		}
		sql.append(" GROUP BY month,year ");
		sql.append(" UNION ALL\n"
				+ "    SELECT '3' type,to_char(a.areaCode) areaCode,to_char(a.proCode) proCode, a.month, a.year, salaryMonth,salaryWo, salaryPercent, \n"
				+ "                hshcXayLapKH,hshcXayLapWo,hshcXayLapPercent,qtKH,qtWo,qtPercent,qtGPDNKH,qtGPDNWo,qtGPDNPercent,\n"
				+ "                qtSLXLKH,qtSLXLWo,qtSLXLPercent,\n"
				+ "                slKH,slWo,slPercent,slGPDNKH,slGPDNWo,slGPDNPercent,\n"
				+ "                tkvKH,tkvWo,tkvPercent,thdtKH,thdtWo,thdtPercent,ttkKH,ttkWo,ttkPercent,xdXMKH,xdXMWo,xdXMPercent,\n"
				+ "                xDBKH,xDBWo,xDBPercent,tmbKH,tmbWo,tmbPercent,a.salaryDat,a.salaryThuong,a.hshcXayLapDat,a.hshcXayLapThuong,a.qtDat,a.qtThuong,\n"
				+ "                a.qtGPDNDat,a.qtGPDNThuong,a.qtSLXLDat,a.qtSLXLThuong,a.slDat,a.slThuong\n"
				+ "                ,a.slGPDNDat,a.slGPDNThuong,a.tkvDat,a.tkvThuong,a.thdtDat,a.thdtThuong\n"
				+ "                ,a.ttkDat,a.ttkThuong,a.xdXMDat,a.xdXMThuong,a.xDBDat,a.xDBThuong\n"
				+ "                ,a.tmbDat,a.tmbThuong,a.diemThuongTong,a.diemDatTong,a.tongDiem,a.quyDoiDiem \n"
				+ "                FROM\n" + "                    RP_WO_KPI a \n" + "                    where  1=1 ");
		if (obj.getSysGroupId() != null) {
			sql.append(" AND a.sysGroupId =:sysGroupId ");
		}
		if (obj.getYear() != null && obj.getMonth() != null) {
			sql.append(" AND a.month = :month AND a.year = :year");
		}
		sql.append(" ORDER BY type,areacode,procode");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("type", new StringType());
		query.addScalar("areaCode", new StringType());
		query.addScalar("proCode", new StringType());
		query.addScalar("month", new StringType());
		query.addScalar("year", new StringType());
		query.addScalar("salaryMonth", new DoubleType());
		query.addScalar("salaryWo", new DoubleType());
		query.addScalar("salaryPercent", new DoubleType());
		query.addScalar("hshcXayLapKH", new DoubleType());
		query.addScalar("hshcXayLapWo", new DoubleType());
		query.addScalar("hshcXayLapPercent", new DoubleType());
		query.addScalar("qtKH", new DoubleType());
		query.addScalar("qtWo", new DoubleType());
		query.addScalar("qtPercent", new DoubleType());
		query.addScalar("qtGPDNKH", new DoubleType());
		query.addScalar("qtGPDNWo", new DoubleType());
		query.addScalar("qtGPDNPercent", new DoubleType());
		query.addScalar("qtSLXLKH", new DoubleType());
		query.addScalar("qtSLXLWo", new DoubleType());
		query.addScalar("qtSLXLPercent", new DoubleType());
		query.addScalar("slKH", new DoubleType());
		query.addScalar("slWo", new DoubleType());
		query.addScalar("slPercent", new DoubleType());
		query.addScalar("slGPDNKH", new DoubleType());
		query.addScalar("slGPDNWo", new DoubleType());
		query.addScalar("slGPDNPercent", new DoubleType());
		query.addScalar("tkvKH", new DoubleType());
		query.addScalar("tkvWo", new DoubleType());
		query.addScalar("tkvPercent", new DoubleType());
		query.addScalar("thdtKH", new DoubleType());
		query.addScalar("thdtWo", new DoubleType());
		query.addScalar("thdtPercent", new DoubleType());
		query.addScalar("ttkKH", new DoubleType());
		query.addScalar("ttkWo", new DoubleType());
		query.addScalar("ttkPercent", new DoubleType());
		query.addScalar("xdXMKH", new DoubleType());
		query.addScalar("xdXMWo", new DoubleType());
		query.addScalar("xdXMPercent", new DoubleType());
		query.addScalar("xDBKH", new DoubleType());
		query.addScalar("xDBWo", new DoubleType());
		query.addScalar("xDBPercent", new DoubleType());
		query.addScalar("tmbKH", new DoubleType());
		query.addScalar("tmbWo", new DoubleType());
		query.addScalar("tmbPercent", new DoubleType());
		//
		query.addScalar("salaryDat", new DoubleType());
		query.addScalar("salaryThuong", new DoubleType());
		query.addScalar("hshcXayLapDat", new DoubleType());
		query.addScalar("hshcXayLapThuong", new DoubleType());
		query.addScalar("qtDat", new DoubleType());
		query.addScalar("qtThuong", new DoubleType());
		query.addScalar("qtGPDNDat", new DoubleType());
		query.addScalar("qtGPDNThuong", new DoubleType());
		query.addScalar("qtSLXLDat", new DoubleType());
		query.addScalar("qtSLXLThuong", new DoubleType());
		query.addScalar("slDat", new DoubleType());
		query.addScalar("slThuong", new DoubleType());
		query.addScalar("slGPDNDat", new DoubleType());
		query.addScalar("slGPDNThuong", new DoubleType());
		query.addScalar("tkvDat", new DoubleType());
		query.addScalar("tkvThuong", new DoubleType());
		query.addScalar("thdtDat", new DoubleType());
		query.addScalar("thdtThuong", new DoubleType());
		query.addScalar("ttkDat", new DoubleType());
		query.addScalar("ttkThuong", new DoubleType());
		query.addScalar("xdXMDat", new DoubleType());
		query.addScalar("xdXMThuong", new DoubleType());
		query.addScalar("xDBDat", new DoubleType());
		query.addScalar("xDBThuong", new DoubleType());
		query.addScalar("tmbDat", new DoubleType());
		query.addScalar("tmbThuong", new DoubleType());
		query.addScalar("diemDatTong", new DoubleType());
		query.addScalar("diemThuongTong", new DoubleType());
		query.addScalar("tongDiem", new DoubleType());
		query.addScalar("quyDoiDiem", new DoubleType());
		if (obj.getSysGroupId() != null) {
			query.setParameter("sysGroupId", obj.getSysGroupId());
		}
		if (obj.getMonth() != null) {
			query.setParameter("month", obj.getMonth());
		}
		if (obj.getYear() != null) {
			query.setParameter("year", obj.getYear());
		}
		if (obj.getSysGroupId() != null) {
			query.setParameter("sysGroupId", obj.getSysGroupId());
		}
		if (obj.getMonth() != null) {
			query.setParameter("month", obj.getMonth());
		}
		if (obj.getYear() != null) {
			query.setParameter("year", obj.getYear());
		}
		if (obj.getSysGroupId() != null) {
			query.setParameter("sysGroupId", obj.getSysGroupId());
		}
		if (obj.getMonth() != null) {
			query.setParameter("month", obj.getMonth());
		}
		if (obj.getYear() != null) {
			query.setParameter("year", obj.getYear());
		}
		if (obj.getSysGroupId() != null) {
			query.setParameter("sysGroupId", obj.getSysGroupId());
		}
		if (obj.getMonth() != null) {
			query.setParameter("month", obj.getMonth());
		}
		if (obj.getYear() != null) {
			query.setParameter("year", obj.getYear());
		}
		if (obj.getSysGroupId() != null) {
			query.setParameter("sysGroupId", obj.getSysGroupId());
		}
		if (obj.getMonth() != null) {
			query.setParameter("month", obj.getMonth());
		}
		if (obj.getYear() != null) {
			query.setParameter("year", obj.getYear());
		}
		query.setResultTransformer(Transformers.aliasToBean(RpWoDetailOsDTO.class));
		List<RpWoDetailOsDTO> lst = query.list();
		obj.setTotalRecord(lst.size());
		return lst;
	}

	// check updateWO de cap nhat cong trinh
	public Double checkStatusWo(Long constructionId) {
		String sql = new String("SELECT count(*) count  from wo w where w.state not like'%OK%' "
				+ "and w.construction_id = :constructionId ");
		SQLQuery query = getSession().createSQLQuery(sql);
		query.addScalar("count", new DoubleType());
		query.setParameter("constructionId", constructionId);
		List<Double> lstDoub = query.list();
		if (lstDoub != null && lstDoub.size() > 0) {
			return lstDoub.get(0);
		}
		return 0D;
	}

	public int updateConstructionComplete(Long constructionId) {
//		taotq start 01072022
		Boolean check = checkWo(constructionId);
		StringBuilder sql = new StringBuilder(" ");
		if(check) {
			sql.append(" update construction set status= -5,complete_date=sysdate, ");
		}else {
			sql.append(" update construction set status=5,complete_date=sysdate, ");
		}
		sql.append(" COMPLETE_VALUE= (select sum(quantity_value) from wo where construction_id = :constructionId) ");
		sql.append("  where construction_id = :constructionId ");
//		String sql = new String("update construction set status=5,complete_date=sysdate,"
//				+ " COMPLETE_VALUE= (select sum(quantity_value) from wo where construction_id = :constructionId) "
//				+ " where construction_id = :constructionId ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
//		taotq end 01072022
		query.setParameter("constructionId", constructionId);
		query.setParameter("constructionId", constructionId);
		return query.executeUpdate();

	}

	public void createSendSmsEmailForMobile(String strContent, String nameSubject, Long sysUserId, Long sysGroupId) {
		try {
			if (sysUserId != null) {
				SysUserCOMSDTO userDTO = getUserInfoById(sysUserId);
				insertSendSmsEmail(userDTO, nameSubject, strContent);
			}
			if (sysGroupId != null) {
				List<SysUserCOMSDTO> lstUsersOfGroup = getListUserOfGroup(sysGroupId);

				boolean isSend = false;
				for (SysUserCOMSDTO userDTO : lstUsersOfGroup) {
					// Neu co quyen nhan sms thi gui
					if (hasRole(userDTO.getSysUserId(), "CD", "WOXL")
							|| hasRole(userDTO.getSysUserId(), "CD", "WOXL_TR")
							|| hasRole(userDTO.getSysUserId(), "CREATE", "WOXL")
							|| hasRole(userDTO.getSysUserId(), "CREATE", "WOXL_TR")) {
						isSend = true;
						insertSendSmsEmail(userDTO, nameSubject, strContent);
					}
				}
				// if (!isSend) {
				// for (SysUserCOMSDTO userDTO : lstUsersOfGroup) {
				// insertSendSmsEmail(userDTO, nameSubject, strContent);
				// }
				// }

				// listUserBGD
				WoSimpleSysGroupDTO woSimpleSysGroupDTO = new WoSimpleSysGroupDTO();
				woSimpleSysGroupDTO = getParentGroupByName(sysGroupId);
				if (!Objects.isNull(woSimpleSysGroupDTO)) {
					List<SysUserCOMSDTO> lstUsersOfGroupBGD = getListUserOfGroup(woSimpleSysGroupDTO.getSysGroupId());

					boolean isSendBGD = false;
					for (SysUserCOMSDTO userDTO : lstUsersOfGroupBGD) {
						// Neu co quyen nhan sms thi gui
						if (hasRole(userDTO.getSysUserId(), "RECEIVE", "SMS_WO")) {
							isSendBGD = true;
							insertSendSmsEmail(userDTO, nameSubject, strContent);
						}
					}
					// if (!isSendBGD) {
					// for (SysUserCOMSDTO userDTO : lstUsersOfGroupBGD) {
					// insertSendSmsEmail(userDTO, nameSubject, strContent);
					// }
					// }
				}
			}
		} catch (Exception ex) {
			//ex.printStackTrace();
		}
	}

	private void insertSendSmsEmail(SysUserCOMSDTO userDTO, String nameSubject, String strContent) {
		String sql = new String(" SELECT SEND_SMS_EMAIL_SEQ.nextval FROM DUAL ");
		SQLQuery query = getSession().createSQLQuery(sql);
		int sendSmsEmailId = ((BigDecimal) query.uniqueResult()).intValue();
		StringBuilder sqlSendSmsEmail = new StringBuilder("INSERT INTO SEND_SMS_EMAIL (" + "   SEND_SMS_EMAIL_ID "
				+ " , TYPE" + " , RECEIVE_PHONE_NUMBER " + " , RECEIVE_EMAIL" + " , CREATED_DATE "
				+ " , CREATED_USER_ID" + " , CREATED_GROUP_ID" + " , SUBJECT" + " , CONTENT,status " + " ) VALUES ( "
				+ " :sendSmsEmailId" + " , 1 " + " , :phoneNumber " + " , :email" + " , :createdDate"
				+ " , :createUserId" + " , :createGroupId" + " , :subject " + " , :content,0 " + ")");
		SQLQuery querySms = getSession().createSQLQuery(sqlSendSmsEmail.toString());
		querySms.setParameter("phoneNumber", userDTO.getPhoneNumber());
		querySms.setParameter("email", userDTO.getEmail());
		querySms.setParameter("createUserId", userDTO.getSysUserId());
		querySms.setParameter("createGroupId", userDTO.getSysGroupId());
		querySms.setParameter("sendSmsEmailId", sendSmsEmailId);
		querySms.setParameter("createdDate", new Date());
		querySms.setParameter("content", strContent);
		querySms.setParameter("subject", nameSubject);
		querySms.executeUpdate();
	}

	public String getCodeAIOWOTR(String parOder) {
		String sql = new String(
				"SELECT ap.code  from app_param ap where ap.par_type ='WNM_AIO_PMXL_TR' and ap.status = 1 and PAR_ORDER = :parOder ");
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("parOder", parOder);
		query.addScalar("code", new StringType());
		List<String> lstDoub = query.list();
		if (lstDoub != null && lstDoub.size() > 0) {
			return lstDoub.get(0);
		}
		return null;
	}

	public String getCodeAIOWO(String parOder) {
		String sql = new String(
				"SELECT ap.code  from app_param ap where ap.par_type ='WNM_AIO_PMXL_WO' and ap.status = 1 and PAR_ORDER = :parOder");
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("parOder", parOder);
		query.addScalar("code", new StringType());
		List<String> lstDoub = query.list();
		if (lstDoub != null && lstDoub.size() > 0) {
			return lstDoub.get(0);
		}
		return null;
	}

	public WoDTO getCDLevel(Long sysGroupId) {
		String sql = new String(" SELECT\n" + "    a.cdlevel2, a.cdLevel2Name, \n" + "    b.cdlevel3, b.cdLevel3Name\n"
				+ " FROM \n" + " (SELECT\n" + "    sg.sys_group_id cdLevel2, sg.name cdLevel2Name\n" + "FROM\n"
				+ "    sys_group sg\n" + "WHERE\n" + "    parent_id = (\n" + "        SELECT\n"
				+ "            parent_id\n" + "        FROM\n" + "            sys_group\n" + "        WHERE\n"
				+ "            sys_group_id = :sysGroupId\n" + "    )\n" + "    AND code LIKE '%P.HT%') a,\n"
				+ "(SELECT\n" + "    sg.sys_group_id cdLevel3 ,sg.name cdLevel3Name\n" + "FROM\n" + "    sys_group sg\n"
				+ "WHERE\n" + "    parent_id = (\n" + "        SELECT\n" + "            parent_id\n" + "        FROM\n"
				+ "            sys_group\n" + "        WHERE\n" + "            sys_group_id = :sysGroupId \n"
				+ "    )\n" + "    AND code LIKE '%P.KT%') b where 1=1 ");
		SQLQuery query = getSession().createSQLQuery(sql);
		query.addScalar("cdLevel2", new StringType());
		query.addScalar("cdLevel2Name", new StringType());
		query.addScalar("cdLevel3", new StringType());
		query.addScalar("cdLevel3Name", new StringType());

		query.setParameter("sysGroupId", sysGroupId);
		query.setResultTransformer(Transformers.aliasToBean(WoDTO.class));
		List<WoDTO> lstDoub = query.list();
		if (lstDoub != null && lstDoub.size() > 0) {
			return lstDoub.get(0);
		}
		return null;
	}

	public WoTrDTO getWoTrByConTract(Long contractId) {
		StringBuilder sql = new StringBuilder("select "
				+ " tr.ID as trId, tr.TR_CODE as trCode, tr.TR_NAME as trName, tr.TR_TYPE_ID as trTypeId, tr.CONTRACT_CODE as contractCode, "
				+ " tr.PROJECT_CODE as projectCode, tr.USER_CREATED as userCreated, tr.CREATED_DATE as createdDate, "
				+ " tr.FINISH_DATE as finishDate, tr.STATE as state, tr.QOUTA_TIME as qoutaTime, tr.EXECUTE_LAT as executeLat, "
				+ " tr.EXECUTE_LONG as executeLong, tr.CD_LEVEL_1 as cdLevel1, tr.STATUS as status,tr.START_DATE startDate, tr.QUANTITY_VALUE quantityValue,tr.CONTRACT_ID contractId,tr.CUSTOMER_TYPE customerType, tr.CONTRACT_TYPE contractType, "
				+ " trType.TR_TYPE_NAME as trTypeName, tr.CD_LEVEL_1_NAME cdLevel1Name,USER_RECEIVE_TR userReceiveTr,\n"
				+ "UPDATE_RECEIVE_TR updateReceiveTr,\n" + "USER_APPROVE_TR userApproveTr,\n"
				+ "UPDATE_APPROVE_TR updateApproveTr,\n" + "CD_LEVEL_2 cdLevel2,\n" + "CD_LEVEL_2_NAME cdLevel2Name,\n"
				+ "GROUP_CREATED groupCreated,\n" + "GROUP_CREATED_NAME groupCreatedName "
				+ " from WO_TR tr LEFT JOIN WO_TR_TYPE trType ON tr.TR_TYPE_ID = trType.ID "
				+ " WHERE tr.STATUS > 0 AND tr.CONTRACT_ID = :contractId");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("contractId", contractId);
		query.addScalar("trTypeName", new StringType());
		query.addScalar("trId", new LongType());
		query.addScalar("trCode", new StringType());
		query.addScalar("trName", new StringType());
		query.addScalar("trTypeId", new LongType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("projectCode", new StringType());
		query.addScalar("userCreated", new StringType());
		query.addScalar("createdDate", new DateType());
		query.addScalar("finishDate", new DateType());
		query.addScalar("state", new StringType());
		query.addScalar("qoutaTime", new IntegerType());
		query.addScalar("executeLat", new StringType());
		query.addScalar("executeLong", new StringType());
		query.addScalar("cdLevel1", new StringType());
		query.addScalar("status", new IntegerType());
		query.addScalar("startDate", new DateType());
		query.addScalar("quantityValue", new DoubleType());
		query.addScalar("contractId", new LongType());
		query.addScalar("customerType", new LongType());
		query.addScalar("contractType", new LongType());
		query.addScalar("cdLevel1Name", new StringType());
		query.addScalar("userReceiveTr", new StringType());
		query.addScalar("updateReceiveTr", new DateType());
		query.addScalar("userApproveTr", new StringType());
		query.addScalar("updateApproveTr", new DateType());
		query.addScalar("cdLevel2", new StringType());
		query.addScalar("cdLevel2Name", new StringType());
		query.addScalar("groupCreated", new StringType());
		query.addScalar("groupCreatedName", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(WoTrDTO.class));
		return (WoTrDTO) query.uniqueResult();
	}

	public WoDTO getWoByTR(long trId) {
		StringBuilder sql = new StringBuilder("SELECT\n" + "    w.id woId,\n" + "    w.wo_code woCode,\n"
				+ "    w.wo_name woName,\n" + "    w.wo_type_id woTypeId,\n" + "    w.tr_id trId,\n"
				+ "    w.state state,\n" + "    w.construction_id constructionId,\n"
				+ "    w.cat_work_item_type_id catWorkItemTypeId,\n" + "    w.station_code stationCode,\n"
				+ "    w.user_created userCreated,\n" + "    w.created_date createdDate, \n"
				+ "    w.finish_date finishDate,\n" + "    w.qouta_time qoutaTime,\n"
				+ "    w.quantity_value quantityValue,\n" + "    w.cd_level_1 cdLevel1,\n"
				+ "    w.cd_level_2 cdLevel2,\n" + "    w.cd_level_3 cdLevel3,\n" + "    w.ft_id ftId,\n"
				+ "    w.accept_time acceptTime,\n" + "    w.end_time endTime,\n" + "    w.status status,\n "
				+ "    w.total_month_plan_id totalMonthPlanId, w.MONEY_VALUE moneyValue,\n"
				+ "    w.money_flow_bill moneyFlowBill,\n" + "    w.money_flow_value moneyFlowValue,\n"
				+ "    w.MONEY_FLOW_REQUIRED moneyFlowRequired,\n" + "    w.MONEY_FLOW_CONTENT moneyFlowContent,\n"
				+ "    w.ap_construction_type apConstructionType, \n"
				+ "    w.ap_work_src apWorkSrc,w.FT_NAME ftName,w.FT_EMAIL ftEmail,w.CREATED_USER_FULL_NAME createdUserFullName,w.CREATED_USER_EMAIL createdUserEmail,w.TR_CODE trCode,\n"
				+ "    w.start_time startTime, w.CONTRACT_ID contractId, w.CD_LEVEL_4 cdLevel4,\n"
				+ "   w.WO_NAME_ID woNameId,w.CD_LEVEL_1_NAME as cdLevel1Name,\n"
				+ "   w.CD_LEVEL_2_NAME as cdLevel2Name, w.CD_LEVEL_3_NAME as cdLevel3Name, \n"
				+ "   w.CD_LEVEL_4_NAME as cdLevel4Name ,w.CONTRACT_CODE contractCode, USER_CD_LEVEL2_RECEIVE_WO userCdLevel2ReceiveWo,\n"
				+ "UPDATE_CD_LEVEL2_RECEIVE_WO updateCdLevel2ReceiveWo,\n"
				+ "USER_CD_LEVEL3_RECEIVE_WO userCdLevel3ReceiveWo,\n"
				+ "UPDATE_CD_LEVEL3_RECEIVE_WO updateCdLevel3ReceiveWo, \n"
				+ "USER_CD_LEVEL4_RECEIVE_WO userCdLevel4ReceiveWo,\n"
				+ "UPDATE_CD_LEVEL4_RECEIVE_WO updateCdLevel4ReceiveWo,\n" + "USER_FT_RECEIVE_WO userFtReceiveWo,\n"
				+ "UPDATE_FT_RECEIVE_WO updateFtReceiveWo,\n" + "USER_CD_APPROVE_WO userCdApproveWo,\n"
				+ "UPDATE_CD_APPROVE_WO updateCdApproveWo,\n" + "USER_TTHT_APPROVE_WO userTthtApproveWo,\n"
				+ "UPDATE_TTHT_APPROVE_WO updateTthtApproveWo,\n" + "APPROVE_DATE_REPORT_WO approveDateReportWo    \n"
				+ "FROM  wo w  where w.tr_id = :trId");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("trId", trId);
		query.addScalar("woId", new LongType());
		query.addScalar("woCode", new StringType());
		query.addScalar("woName", new StringType());
		query.addScalar("woTypeId", new LongType());
		query.addScalar("trId", new LongType());
		query.addScalar("state", new StringType());
		query.addScalar("constructionId", new LongType());
		query.addScalar("catWorkItemTypeId", new LongType());
		query.addScalar("stationCode", new StringType());
		query.addScalar("userCreated", new StringType());
		query.addScalar("createdDate", new DateType());
		query.addScalar("finishDate", new DateType());
		query.addScalar("quantityValue", new LongType());
		query.addScalar("cdLevel1", new StringType());
		query.addScalar("cdLevel2", new StringType());
		query.addScalar("cdLevel3", new StringType());
		query.addScalar("ftId", new LongType());
		query.addScalar("acceptTime", new DateType());
		query.addScalar("endTime", new DateType());
		query.addScalar("status", new LongType());
		query.addScalar("totalMonthPlanId", new LongType());
		query.addScalar("moneyValue", new DoubleType());
		query.addScalar("moneyFlowBill", new StringType());
		query.addScalar("moneyFlowValue", new LongType());
		query.addScalar("moneyFlowRequired", new LongType());
		query.addScalar("moneyFlowContent", new StringType());
		query.addScalar("apConstructionType", new LongType());
		query.addScalar("apWorkSrc", new LongType());
		query.addScalar("startTime", new DateType());
		query.addScalar("contractId", new LongType());
		query.addScalar("cdLevel4", new StringType());
		query.addScalar("woNameId", new LongType());
		query.addScalar("cdLevel1Name", new StringType());
		query.addScalar("cdLevel2Name", new StringType());
		query.addScalar("cdLevel3Name", new StringType());
		query.addScalar("cdLevel4Name", new StringType());
		query.addScalar("ftName", new StringType());
		query.addScalar("ftEmail", new StringType());
		query.addScalar("createdUserFullName", new StringType());
		query.addScalar("createdUserEmail", new StringType());
		query.addScalar("trCode", new StringType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("userCdLevel2ReceiveWo", new StringType());
		query.addScalar("updateCdLevel2ReceiveWo", new DateType());
		query.addScalar("userCdLevel3ReceiveWo", new StringType());
		query.addScalar("updateCdLevel3ReceiveWo", new DateType());
		query.addScalar("userCdLevel4ReceiveWo", new StringType());
		query.addScalar("updateCdLevel4ReceiveWo", new DateType());
		query.addScalar("userFtReceiveWo", new StringType());
		query.addScalar("updateFtReceiveWo", new DateType());
		query.addScalar("userCdApproveWo", new StringType());
		query.addScalar("updateCdApproveWo", new DateType());
		query.addScalar("userTthtApproveWo", new StringType());
		query.addScalar("updateTthtApproveWo", new DateType());
		query.addScalar("approveDateReportWo", new DateType());
		query.setResultTransformer(Transformers.aliasToBean(WoDTO.class));
		return (WoDTO) query.uniqueResult();
	}

	public WoAppParamDTO getCodeAppParam(String parType, String parOder) {
		StringBuilder sql = new StringBuilder(
				"select CODE as code, NAME as name, PAR_TYPE as parType, DESCRIPTION description from APP_PARAM where PAR_TYPE = :parType and PAR_ORDER = :parOder ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("parType", parType);
		query.setParameter("parOder", parOder);

		query.addScalar("code", new StringType());
		query.addScalar("name", new StringType());
		query.addScalar("parType", new StringType());
		query.addScalar("description", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(WoAppParamDTO.class));
		return (WoAppParamDTO) query.uniqueResult();
	}

	public WoTypeDTO getWoTypeAio(String woTypeCode) {
		StringBuilder sql = new StringBuilder(
				"select id woTypeId, wo_type_code woTypeCode from wo_type where status = 1 and wo_type_code = :woTypeCode ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("woTypeCode", woTypeCode);

		query.addScalar("woTypeId", new LongType());
		query.addScalar("woTypeCode", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(WoTypeDTO.class));
		return (WoTypeDTO) query.uniqueResult();
	}

	public List<WoSimpleConstructionDTO> getListConstruction(String constructionCode) {

		StringBuilder sql = new StringBuilder(
				"select CONSTRUCTION_ID as constructionId, NAME as constructionName " + " from CONSTRUCTION");

		if (StringUtils.isNotEmpty(constructionCode)) {
			sql.append(" WHERE CODE LIKE :constructionCode ");
		}

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("constructionCode", constructionCode);

		query.addScalar("constructionId", new LongType());
		query.addScalar("constructionName", new StringType());
		query.addScalar("constructionCode", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(WoSimpleConstructionDTO.class));
		return query.list();
	}

	public List<WoSimpleConstructionDTO> getListConstructionByCode(String constructionCode) {

		StringBuilder sql = new StringBuilder(
				"select CONSTRUCTION_ID as constructionId, NAME as constructionName, CODE as constructionCode"
						+ " from CONSTRUCTION");

		if (StringUtils.isNotEmpty(constructionCode)) {
			sql.append(" WHERE CODE LIKE :constructionCode ");
		}

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("constructionCode", constructionCode);

		query.addScalar("constructionId", new LongType());
		query.addScalar("constructionName", new StringType());
		query.addScalar("constructionCode", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(WoSimpleConstructionDTO.class));
		return query.list();
	}

	public Double getCurrentAmount(Long constructionId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select nvl(sum(case when wtd.amount is null then 0 else wtd.amount end ), 0) as currentAmount \n"
				+ "from WO_TASK_DAILY wtd \n"
				+ "left join WO_MAPPING_CHECKLIST wmc on wtd.WO_MAPPING_CHECKLIST_ID = wmc.ID \n"
				+ "left join wo on wmc.wo_id = wo.id \n"
				+ "where wtd.status>0 and wmc.status>0 and wo.construction_id = :constructionId  ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("constructionId", constructionId);
		return ((BigDecimal) query.uniqueResult()).doubleValue();
	}

	public Double getAmmountOfConstruction(Long constructionId) {
		if (constructionId == null || constructionId == 0l) {
			return 0.0;
		}

		StringBuilder sql = new StringBuilder();
		sql.append(" select nvl(amount,0) from construction where construction_id = :constructionId  ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("constructionId", constructionId);
		if (query.uniqueResult() != null) {
			return ((BigDecimal) query.uniqueResult()).doubleValue();
		}
		return 0.0;
	}

	private Long getGroupOfLoginUser(Long loginUserId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT sys_group_id FROM sys_user WHERE SYS_USER_ID = :loginUserId  ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("loginUserId", loginUserId);
		return ((BigDecimal) query.uniqueResult()).longValue();
	}

	public int deleteWoMappingChecklist(Long woId) {
		StringBuilder sql = new StringBuilder("DELETE FROM wo_mapping_checklist ").append("WHERE wo_id = :woId  ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("woId", woId);
		return query.executeUpdate();
	}

	public int updateAioAcceptance(AIOWoTrDTO aioWoTrDTO) {
		StringBuilder sql = new StringBuilder("UPDATE AIO_ACCEPTANCE_RECORDS SET PERFORMER_ID = :performerId")
				.append(" WHERE CONTRACT_ID = :idContract");
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		query.setParameter("performerId", aioWoTrDTO.getPerformerId());
		query.setParameter("idContract", aioWoTrDTO.getContractId());
		return query.executeUpdate();
	}

	public long updateAioContract(AIOWoTrDTO obj) {
		StringBuilder sql = new StringBuilder(
				"Update AIO_CONTRACT SET STATUS = 1, UPDATED_DATE = sysdate, UPDATED_USER = :sysUser,");
		sql.append(
				" PERFORMER_ID = :performer,PERFORMER_NAME = :performerName ,PERFORMER_CODE= :performerCode,performer_group_id = :performerGroupId ");
		sql.append(" where CONTRACT_ID = :idContract ");
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		query.setParameter("sysUser", obj.getUserCreated());
		query.setParameter("performer", obj.getPerformerId());
		query.setParameter("performerName", obj.getPerformerName());
		query.setParameter("performerCode", obj.getPerformerCode());
		query.setParameter("performerGroupId", obj.getPerformerGroupId());
		query.setParameter("idContract", obj.getContractId());
		return query.executeUpdate();
	}

	public Long getUserSysGroupLevel2ByUserId(Long sysUserId) {
		StringBuilder sql = new StringBuilder("SELECT ").append("(case ").append("when sg.group_level = 0 then (0) ")
				.append("when sg.group_level = 1 then (sg.sys_group_id) ")
				.append("else TO_NUMBER((substr(sg.path, INSTR(sg.path, '/', 1, 2) + 1, INSTR(sg.path, '/', 1, 3) - (INSTR(sg.path, '/', 1, 2) + 1)))) end) groupLv2 ")
				.append("from sys_Group sg ")
				.append("where sg.sys_group_id = (select sys_group_id from sys_user where sys_user_id = :sysUserId) ");

		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		query.setParameter("sysUserId", sysUserId);
		query.addScalar("groupLv2", new LongType());

		return (Long) query.uniqueResult();
	}

	public SysUserCOMSDTO getUserInfoById(Long userid) {
		StringBuilder sql = new StringBuilder("SELECT\n" + "    su.sys_user_id sysuserid,\n"
				+ "    su.login_name loginname,\n" + "    su.full_name fullname,\n" + "    su.password password,\n"
				+ "    su.employee_code employeecode,\n" + "    su.email email,\n"
				+ "    su.phone_number phonenumber,\n" + "    su.status status,\n" + "    su.sys_group_id sysgroupid,\n"
				+ "    sg.name sysgroupname\n" + "FROM\n" + "    sys_user su,\n" + "    sys_group sg\n" + "WHERE\n"
				+ "    su.sys_user_id =:userid\n" + "    AND sg.sys_group_id = su.sys_group_id");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("sysUserId", new LongType());
		query.addScalar("loginName", new StringType());
		query.addScalar("employeeCode", new StringType());
		query.addScalar("fullName", new StringType());
		query.addScalar("email", new StringType());
		query.addScalar("phoneNumber", new StringType());
		query.addScalar("password", new StringType());
		query.addScalar("status", new StringType());
		query.addScalar("sysGroupId", new LongType());
		query.addScalar("sysGroupName", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(SysUserCOMSDTO.class));
		query.setParameter("userid", userid);
		return (SysUserCOMSDTO) query.uniqueResult();
	}

	public List<SysUserCOMSDTO> getListUserOfGroup(Long sysGroupId) {
		StringBuilder sql = new StringBuilder("SELECT\n" + "    su.sys_user_id sysuserid,\n"
				+ "    su.login_name loginname,\n" + "    su.full_name fullname,\n" + "    su.password password,\n"
				+ "    su.employee_code employeecode,\n" + "    su.email email,\n"
				+ "    su.phone_number phonenumber,\n" + "    su.status status,\n" + "    su.sys_group_id sysgroupid,\n"
				+ "    sg.name sysgroupname\n" + "FROM\n" + "    sys_user su,\n" + "    sys_group sg\n"
				+ "WHERE su.status!=0 and sg.status!=0 \n" + "    AND su.SYS_GROUP_ID =:sysGroupId\n"
				+ "    AND sg.sys_group_id = su.sys_group_id");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("sysUserId", new LongType());
		query.addScalar("loginName", new StringType());
		query.addScalar("employeeCode", new StringType());
		query.addScalar("fullName", new StringType());
		query.addScalar("email", new StringType());
		query.addScalar("phoneNumber", new StringType());
		query.addScalar("password", new StringType());
		query.addScalar("status", new StringType());
		query.addScalar("sysGroupId", new LongType());
		query.addScalar("sysGroupName", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(SysUserCOMSDTO.class));
		query.setParameter("sysGroupId", sysGroupId);
		return query.list();
	}

	public Long getCountWoTypeAIO(Long woTypeId) {
		StringBuilder sql = new StringBuilder("select COUNT(*) countWo from WO WHERE WO_TYPE_ID = :woTypeId ");
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		query.setParameter("woTypeId", woTypeId);
		query.addScalar("countWo", new LongType());

		return (Long) query.uniqueResult();
	}

	public List<WoMappingChecklistDTO> getListGPONCheckListNeedAdd(WoDTO woDTO) {
		StringBuilder sql = new StringBuilder("select\n" + "    wmc.id,\n" + "    wmc.WO_ID woId,\n"
				+ "    wo.WO_CODE || '-' || wo.WO_NAME woName,\n" + "    wmc.CHECKLIST_ID checklistId,\n"
				+ "    wig.TASK_NAME checklistName,\n" + "    wmc.state,\n"
				+ "    wmc.quantity_by_date quantityByDate,\n" + "    wmc.quantity_length quantityLength,\n"
				+ "    wmc.status\n" + "from WO_MAPPING_CHECKLIST wmc\n" + "LEFT JOIN WO wo on wmc.WO_ID = wo.ID\n"
				+ "LEFT JOIN WORK_ITEM_GPON wig ON wmc.CHECKLIST_ID = wig.WORK_ITEM_GPON_ID \n" + "WHERE 1 = 1\n"
				+ "AND wig.CAT_WORK_ITEM_TYPE_ID = :catWorkItemTypeId\n" + "AND wig.CONSTRUCTION_ID = :constructionId\n"
				+ "AND wmc.status = 1");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.setParameter("catWorkItemTypeId", woDTO.getCatWorkItemTypeId());
		query.setParameter("constructionId", woDTO.getConstructionId());
		query = mapFieldsChecklist(query);

		return query.list();
	}

	public List<WoMappingChecklistDTO> getListAIOCheckListNeedAdd(WoDTO woDTO) {
		StringBuilder sql = new StringBuilder("select\n" + "    wmc.id,\n" + "    wmc.WO_ID woId,\n"
				+ "    wo.WO_CODE || '-' || wo.WO_NAME woName,\n" + "    wmc.CHECKLIST_ID checklistId,\n"
				+ "    ct.NAME checklistName,\n" + "    wmc.state,\n" + "    wmc.quantity_by_date quantityByDate,\n"
				+ "    wmc.quantity_length quantityLength,\n" + "    wmc.status\n" + "from WO_MAPPING_CHECKLIST wmc\n"
				+ "LEFT JOIN WO wo on wmc.WO_ID = wo.ID\n"
				+ "LEFT JOIN CTCT_COMS_OWNER.CAT_TASK ct ON wmc.CHECKLIST_ID = ct.CAT_TASK_ID\n" + "WHERE 1 = 1\n"
				+ "AND ct.CAT_WORK_ITEM_TYPE_ID = :catWorkItemTypeId\n" + "AND wmc.status = 1");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.setParameter("catWorkItemTypeId", woDTO.getCatWorkItemTypeId());

		query = mapFieldsChecklist(query);

		return query.list();
	}

	public String getCatWorkName(long catWorkItemTypeId, long constructionId) {
		StringBuilder sql = new StringBuilder(
				"SELECT NAME FROM WORK_ITEM WHERE CAT_WORK_ITEM_TYPE_ID = :id and CONSTRUCTION_ID = :consId ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.setParameter("id", catWorkItemTypeId);
		query.setParameter("consId", constructionId);

		query.addScalar("NAME", new StringType());

		List<String> names = query.list();
		if (names.size() > 0)
			return names.get(0);
		else
			return null;
	}

	public Long getAIOWoTypeId() {
		StringBuilder sql = new StringBuilder(
				"SELECT ID FROM WO_TYPE WHERE WO_TYPE_CODE LIKE '%AIO%' fetch next 1 row only ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("ID", new LongType());

		return (Long) query.list().get(0);
	}

	public WoNameDTO getAIOWoName(long woTypeId) {
		StringBuilder sql = new StringBuilder(
				"SELECT ID as id, NAME as name FROM WO_NAME WHERE WO_TYPE_ID = :woTypeId fetch next 1 row only ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.setParameter("woTypeId", woTypeId);

		query.addScalar("id", new LongType());
		query.addScalar("name", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(WoNameDTO.class));
		return (WoNameDTO) query.list().get(0);
	}

	// Huypq-26082020-start edit
	// public WoSimpleSysGroupDTO getCdLevel2FromAIOContract(Long contractId) {
	// String sql = " select sg.SYS_GROUP_ID as sysGroupId, sg.NAME as groupName " +
	// " from SYS_GROUP sg where sg.status>0 and sg.CODE like '%P.HT%' and " +
	// " sg.area_id = (select acus.area_id from AIO_CONTRACT ac left join
	// aio_customer acus on ac.customer_id = acus.customer_id " +
	// " where ac.CONTRACT_ID = :contractId and ac.status>0 and acus.status>0 fetch
	// next 1 row only )";
	// SQLQuery query = getSession().createSQLQuery(sql);
	//
	// query.setParameter("contractId", contractId);
	//
	// query.addScalar("groupName", new StringType());
	// query.addScalar("sysGroupId", new LongType());
	// query.setResultTransformer(Transformers.aliasToBean(WoSimpleSysGroupDTO.class));
	//
	// return (WoSimpleSysGroupDTO) query.uniqueResult();
	// }

	public WoSimpleSysGroupDTO getCdLevel2FromAIOContract(Long contractId) {
		String sql = new String(" SELECT\n" + "sg.sys_group_id AS sysgroupid,\n" + "sg.name AS groupname\n" + "FROM\n"
				+ "    sys_group sg\n" + "WHERE\n" + "    sg.status > 0\n" + "    AND sg.code LIKE '%P.HT%'\n"
				+ "    AND sg.parent_id IN (\n" + "        SELECT\n" + "            sg1.PARENT_ID\n" + "        FROM\n"
				+ "            aio_area aa,\n" + "            sys_group sg1,\n" + "            aio_customer acus,\n"
				+ "            aio_contract ac\n" + "        WHERE\n"
				+ "            ac.customer_id = acus.customer_id\n"
				+ "            AND sg1.SYS_GROUP_ID = aa.sys_group_level3\n"
				+ "            AND acus.aio_area_id = aa.area_id\n" + "            AND ac.contract_id = :contractId\n"
				+ "    )\n" + "FETCH NEXT 1 ROW ONLY");
		SQLQuery query = getSession().createSQLQuery(sql);

		query.setParameter("contractId", contractId);

		query.addScalar("groupName", new StringType());
		query.addScalar("sysGroupId", new LongType());
		query.setResultTransformer(Transformers.aliasToBean(WoSimpleSysGroupDTO.class));

		return (WoSimpleSysGroupDTO) query.uniqueResult();
	}

	// Huy-end

	public String getCNTContractCode(long contractId) {
		StringBuilder sql = new StringBuilder("SELECT CODE FROM CNT_CONTRACT WHERE CNT_CONTRACT_ID = :contractId ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.setParameter("contractId", contractId);

		query.addScalar("CODE", new StringType());
		List<String> codes = query.list();
		String code = "";
		if (codes.size() > 0)
			code = codes.get(0);

		return code;
	}

	public Long getAIOWoTRTypeId() {
		StringBuilder sql = new StringBuilder(
				"SELECT ID FROM WO_TR_TYPE WHERE TR_TYPE_CODE LIKE '%AIO%' fetch next 1 row only ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("ID", new LongType());

		return (Long) query.list().get(0);
	}

	public int updateWoMappingCheckList(Long woId, Long checkListId, String state) {
		StringBuilder sql = new StringBuilder(
				"UPDATE WO_MAPPING_CHECKLIST SET STATE = :state WHERE  WO_ID = :woId AND CHECKLIST_ID = :checkListId  ");
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		query.setParameter("state", state);
		query.setParameter("woId", woId);
		query.setParameter("checkListId", checkListId);
		return query.executeUpdate();
	}

	public boolean checkWoIsAIO(long woTypeId) {
		StringBuilder sql = new StringBuilder(
				"SELECT WO_TYPE_CODE FROM WO_TYPE WHERE ID = :woTypeId AND WO_TYPE_CODE like 'AIO' ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("woTypeId", woTypeId);

		query.addScalar("WO_TYPE_CODE", new StringType());

		if (query.list().size() > 0)
			return true;
		else
			return false;
	}

	public boolean checkWoIsCVDK(long woTypeId) {
		StringBuilder sql = new StringBuilder(
				"SELECT WO_TYPE_CODE FROM WO_TYPE WHERE ID = :woTypeId AND WO_TYPE_CODE like 'BDDK' ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("woTypeId", woTypeId);

		query.addScalar("WO_TYPE_CODE", new StringType());

		if (query.list().size() > 0)
			return true;
		else
			return false;
	}

	public List<WoDTO> genDetailsReport(WoDTO woDto, List<String> groupIdList) {
		String sqlReportDetail = baseSelectStrReport + " , apr1.name AS apWorkSrcName,\n"
				+ "    apr2.name AS apConstructionTypeName,\n" + "    wtype.wo_type_code AS woTypeCode,\n"
				+ "    wmc.checklistItemNames,\n"
				+ "    case when wmc.quantity_length > 0 then wmc.quantity_length else wxc.acceptedMoneyValueXddd end acceptedMoneyValue,"
				+ "    wtd.acceptedMoneyValueDaily acceptedMoneyValueDaily,\n"
				+ "    wtm.acceptedMoneyValueMonthly acceptedMoneyValueMonthly,\n"
				+ "    cwit.name catWorkItemTypeName,\n" + "    wotbl.approve_date_report_wo approveDateReportWo,\n"
				+ "    wotbl.update_ttht_approve_wo updateTthtApproveWo,\n"
				+ "    TO_CHAR(wotbl.finish_date,'dd/MM/yyyy') finishDateStr,\n"
				+ "    TO_CHAR(wotbl.end_time,'dd/MM/yyyy') endTimeStr,\n"
				+ "    TO_CHAR(nvl(wotbl.update_ttht_approve_wo,update_cd_approve_wo),'dd/MM/yyyy') userTthtApproveWoStr,\n"
				+ "    TO_CHAR(wotbl.approve_date_report_wo,'MM/yyyy') approveDateReportWoStr\n" +
				// Huypq-12062021-start
				" ,sys.email userTthtApproveWo, " + " catH.code catStationHouseTxt " +
				// Huy-end
				"FROM wo wotbl\n" + "LEFT JOIN (\n" + "    select \n" + "        wo_id\n"
				+ "        , sum(quantity_length) quantity_length\n"
				+ "        , LISTAGG(name,'; ') WITHIN GROUP(ORDER BY name) AS checklistItemNames\n"
				+ "    from wo_mapping_checklist\n" + "    where 1 = 1\n" + "    and status > 0\n"
				+ "    group by wo_id\n" + ") wmc ON wmc.wo_id = wotbl.id\n" + " LEFT JOIN (\r\n" + "    select \r\n"
				+ "        wo_id\r\n" + "        , sum(value) acceptedMoneyValueXddd\r\n"
				+ "    from wo_xddd_checklist\r\n" + "    where 1 = 1\r\n" + "    and state = 'DONE'\r\n"
				+ "    group by wo_id\r\n" + ") wxc ON wxc.wo_id = wotbl.id \n"
				+ "LEFT JOIN wo_type wtype ON wotbl.wo_type_id = wtype.id\n"
				+ "LEFT JOIN cat_work_item_type cwit ON wotbl.cat_work_item_type_id = cwit.cat_work_item_type_id AND cwit.status = 1\n"
				+ "LEFT JOIN (\n" + "    select \n" + "        wo_id\n"
				+ "        , sum(quantity) acceptedMoneyValueDaily\n" + "    from WO_TASK_DAILY\n" + "    where 1 = 1\n"
				+ "    and status > 0 and confirm=1 \n";
		if (woDto.getFromDate() != null) {
			sqlReportDetail += "    and APPROVE_DATE >= :fromDate\n";
		}
		if (woDto.getToDate() != null) {
			sqlReportDetail += "    and APPROVE_DATE < :toDate\n";
		}
		sqlReportDetail += "    group by wo_id\n" + ") wtd ON wtd.wo_id = wotbl.id\n" + "LEFT JOIN (\n"
				+ "    select \n" + "        wo_id\n" + "        , sum(quantity) acceptedMoneyValueMonthly\n"
				+ "    from wo_task_monthly\n" + "    where 1 = 1\n" + "    and status > 0 and confirm=1 \n";
		if (woDto.getFromDate() != null) {
			sqlReportDetail += "    and APPROVE_DATE >= :fromDate\n";
		}
		if (woDto.getToDate() != null) {
			sqlReportDetail += "    and APPROVE_DATE < :toDate\n";
		}
		sqlReportDetail += "    group by wo_id\n" + ") wtm ON wtm.wo_id = wotbl.id AND wtype.wo_type_code = 'THDT'\n"
				+ "LEFT JOIN app_param apr1 ON wotbl.ap_work_src = apr1.code AND apr1.par_type = 'AP_WORK_SRC'\n"
				+ "LEFT JOIN app_param apr2 ON wotbl.ap_construction_type = apr2.code AND apr2.par_type = 'AP_CONSTRUCTION_TYPE'\n"
				+
				// Huypq-12062021-start
				" left join cat_station catS on wotbl.station_code=catS.code and catS.status=1 "
				+ " left join cat_station_house catH on catS.cat_station_house_id=catH.cat_station_house_id and catH.status=1 "
				+ " left join sys_user sys on wotbl.USER_TTHT_APPROVE_WO=sys.login_name and sys.status=1 " +
				// Huy-end
				"WHERE 1 = 1\n" + "AND wotbl.status > 0\n" + "AND wotbl.cd_level_1 IS NOT NULL\n"
				+ "AND wtype.wo_type_code != 'HCQT'\n" + "AND (\n" + "    wotbl.state = 'OK'\n"
				+ "    OR (wotbl.state = 'CD_OK' AND wtype.wo_type_code IN ('THICONG', 'AIO'))\n"
				+ "    OR (wtype.wo_type_code = 'THDT' AND (wotbl.state IN ('PROCESSING', 'DONE', 'CD_OK','OK') OR wotbl.state LIKE 'OPINION_RQ%'))\n"
				+ "    OR (\n" + "        wtype.wo_type_code = 'THICONG' \n"
				+ "        AND (wotbl.state IN ('PROCESSING', 'DONE', 'CD_OK','OK') OR wotbl.state LIKE 'OPINION_RQ%') \n"
				+ "        AND (wotbl.cat_construction_type_id = 2 OR wotbl.cat_construction_type_id = 3)\n"
				+ "        AND wtd.acceptedMoneyValueDaily > 0\n" + "    )\n" + "    OR (\n"
				+ "        wtype.wo_type_code = 'THICONG' \n"
				+ "        AND (wotbl.state IN ('PROCESSING', 'DONE', 'CD_OK','OK') OR wotbl.state LIKE 'OPINION_RQ%') \n"
				+ "        AND wotbl.cat_construction_type_id = 8\n" + " AND wxc.acceptedMoneyValueXddd > 0 " + // Hoanm1-29052021-bo
																												// sung
																												// them
				"    )\n" + ") ";

		if (StringUtils.isNotEmpty(woDto.getKeySearch())) {
			sqlReportDetail += " AND ( LOWER(woTbl.STATION_CODE) LIKE :keySearch OR LOWER(woTbl.CONTRACT_CODE) LIKE :keySearch OR LOWER(woTbl.CONSTRUCTION_CODE) "
					+ "LIKE :keySearch OR LOWER(woTbl.PROJECT_CODE) LIKE :keySearch ) \n";
		}

		if (woDto.getListWoTypeId() != null && woDto.getListWoTypeId().size() > 0) {
			sqlReportDetail += " and woTbl.WO_TYPE_ID in (:listWoTypeId) \n";
		}

		if (woDto.getFromDate() != null) {
			sqlReportDetail += " AND (woTbl.APPROVE_DATE_REPORT_WO is null OR woTbl.APPROVE_DATE_REPORT_WO >= :fromDate) \n";
		}

		if (woDto.getToDate() != null) {
			sqlReportDetail += " AND (woTbl.APPROVE_DATE_REPORT_WO is null OR woTbl.APPROVE_DATE_REPORT_WO <= :toDate) \n";
		}

		if (StringUtils.isNotEmpty(woDto.getCdLevel2())) {
			sqlReportDetail += " AND woTbl.cd_level_2 like :cdLevel2 \n";
		}

		if (StringUtils.isNotEmpty(woDto.getCdLevel4())) {
			sqlReportDetail += " AND woTbl.CD_LEVEL_4 like :cdLevel4 \n";
		}

		if (woDto.getFtId() != null) {
			sqlReportDetail += " AND woTbl.FT_ID like :ftId \n";
		}

		if (StringUtils.isNotEmpty(woDto.getFtName())) {
			sqlReportDetail += " AND ( lower(woTbl.FT_NAME) like :ftName or lower(woTbl.FT_EMAIL) like :ftName ) \n";
		}

		if (woDto.getWoTypeId() != null) {
			sqlReportDetail += " AND woTbl.WO_TYPE_ID like :woTypeId \n";
		}

		if (woDto.getApWorkSrc() != null) {
			sqlReportDetail += " AND to_char(woTbl.AP_WORK_SRC) in (:apWorkSrc) \n";
		}

		if (woDto.getApConstructionType() != null) {
			sqlReportDetail += " AND woTbl.AP_CONSTRUCTION_TYPE like :apConstructionType \n";
		}

		if (StringUtils.isNotEmpty(woDto.getWoCode())) {
			sqlReportDetail += " AND ( LOWER(woTbl.WO_CODE) like :woCode or LOWER(woTbl.WO_NAME) like :woCode ) \n";
		}

		if (StringUtils.isNotEmpty(woDto.getState())) {
			sqlReportDetail += " AND LOWER(woTbl.STATE) like :state \n";
		}

		// if (StringUtils.isNotEmpty(woDto.getType()) &&
		// "0".equalsIgnoreCase(woDto.getType()) == false) {
		// sqlReportDetail += " and woTbl.type = :type \n";
		// }
		//
		// if (StringUtils.isNotEmpty(woDto.getType()) &&
		// "0".equalsIgnoreCase(woDto.getType()) == true) {
		// sqlReportDetail += " and woTbl.type is null \n";
		// }

		if (StringUtils.isNotEmpty(woDto.getType())) {
			sqlReportDetail += " and woTbl.cd_level_1 like :cdLevel1 \n";
		}

		if (groupIdList.size() > 0) {
			sqlReportDetail += " and (woTbl.CD_LEVEL_1 in (:groupIdList) OR woTbl.CD_LEVEL_2 in (:groupIdList) OR woTbl.CD_LEVEL_3 in (:groupIdList) OR woTbl.CD_LEVEL_4 in (:groupIdList))\n";
		}

		if (StringUtils.isNotBlank(woDto.getUserTthtApproveWo())) {
			sqlReportDetail += " AND ( lower(sys.full_name) like :userTthtApproveWo or lower(sys.email) like :userTthtApproveWo ) ";
		}

		sqlReportDetail += " order by woTbl.ID desc ";

		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sqlReportDetail);
		sqlCount.append(")");
		SQLQuery query = getSession().createSQLQuery(sqlReportDetail);
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		if (StringUtils.isNotEmpty(woDto.getKeySearch())) {
			query.setParameter("keySearch", toSearchStr(woDto.getKeySearch()));
			queryCount.setParameter("keySearch", toSearchStr(woDto.getKeySearch()));
		}

		if (woDto.getListWoTypeId() != null && woDto.getListWoTypeId().size() > 0) {
			query.setParameterList("listWoTypeId", woDto.getListWoTypeId());
			queryCount.setParameterList("listWoTypeId", woDto.getListWoTypeId());
		}

		if (woDto.getFromDate() != null) {
			query.setParameter("fromDate", woDto.getFromDate());
			queryCount.setParameter("fromDate", woDto.getFromDate());
		}

		if (woDto.getToDate() != null) {
			query.setParameter("toDate", woDto.getToDate());
			queryCount.setParameter("toDate", woDto.getToDate());
		}

		if (StringUtils.isNotEmpty(woDto.getCdLevel2())) {
			query.setParameter("cdLevel2", woDto.getCdLevel2());
			queryCount.setParameter("cdLevel2", woDto.getCdLevel2());
		}

		if (StringUtils.isNotEmpty(woDto.getCdLevel4())) {
			query.setParameter("cdLevel4", woDto.getCdLevel4());
			queryCount.setParameter("cdLevel4", woDto.getCdLevel4());
		}

		if (woDto.getFtId() != null) {
			query.setParameter("ftId", woDto.getFtId());
			queryCount.setParameter("ftId", woDto.getFtId());
		}

		if (StringUtils.isNotEmpty(woDto.getFtName())) {
			query.setParameter("ftName", '%' + woDto.getFtName().toLowerCase() + '%');
			queryCount.setParameter("ftName", '%' + woDto.getFtName().toLowerCase() + '%');
		}

		if (woDto.getWoTypeId() != null) {
			query.setParameter("woTypeId", woDto.getWoTypeId());
			queryCount.setParameter("woTypeId", woDto.getWoTypeId());
		}

		if (woDto.getApWorkSrc() != null) {
			query.setParameter("apWorkSrc", woDto.getApWorkSrc());
			queryCount.setParameter("apWorkSrc", woDto.getApWorkSrc());
		}

		if (woDto.getApConstructionType() != null) {
			query.setParameter("apConstructionType", woDto.getApConstructionType());
			queryCount.setParameter("apConstructionType", woDto.getApConstructionType());
		}

		if (StringUtils.isNotEmpty(woDto.getKeySearch())) {
			query.setParameter("keySearch", "%" + woDto.getKeySearch().toLowerCase() + "%");
			queryCount.setParameter("keySearch", "%" + woDto.getKeySearch().toLowerCase() + "%");
		}

		if (StringUtils.isNotEmpty(woDto.getState())) {
			query.setParameter("state", woDto.getState().toLowerCase());
			queryCount.setParameter("state", woDto.getState().toLowerCase());
		}

		// if (StringUtils.isNotEmpty(woDto.getType()) &&
		// "0".equalsIgnoreCase(woDto.getType()) == false) {
		// query.setParameter("type", woDto.getType());
		// queryCount.setParameter("type", woDto.getType());
		// }

		if (StringUtils.isNotEmpty(woDto.getType())) {
			query.setParameter("cdLevel1", woDto.getType());
			queryCount.setParameter("cdLevel1", woDto.getType());
		}

		if (groupIdList.size() > 0) {
			query.setParameterList("groupIdList", groupIdList);
			queryCount.setParameterList("groupIdList", groupIdList);
		}

		if (StringUtils.isNotEmpty(woDto.getWoCode())) {
			query.setParameter("woCode", '%' + woDto.getWoCode().toLowerCase() + '%');
			queryCount.setParameter("woCode", '%' + woDto.getWoCode().toLowerCase() + '%');
		}

		if (StringUtils.isNotBlank(woDto.getUserTthtApproveWo())) {
			query.setParameter("userTthtApproveWo", '%' + woDto.getUserTthtApproveWo().toLowerCase() + '%');
			queryCount.setParameter("userTthtApproveWo", '%' + woDto.getUserTthtApproveWo().toLowerCase() + '%');
		}

		query.addScalar("apWorkSrcName", new StringType());
		query.addScalar("apConstructionTypeName", new StringType());
		query.addScalar("acceptedMoneyValue", new DoubleType());
		query.addScalar("acceptedMoneyValueDaily", new DoubleType());
		query.addScalar("acceptedMoneyValueMonthly", new DoubleType());
		query.addScalar("woTypeCode", new StringType());
		query.addScalar("catWorkItemTypeName", new StringType());
		query.addScalar("approveDateReportWo", new DateType());
		query.addScalar("updateTthtApproveWo", new DateType());
		query.addScalar("finishDateStr", new StringType());
		query.addScalar("approveDateReportWoStr", new StringType());
		query.addScalar("userTthtApproveWoStr", new StringType());
		query.addScalar("endTimeStr", new StringType());
		query.addScalar("checklistItemNames", new StringType());
		query.addScalar("userTthtApproveWo", new StringType());
		query.addScalar("catStationHouseTxt", new StringType());
		query = mapFieldsReport(query);
		query.setResultTransformer(Transformers.aliasToBean(WoDTO.class));

		if (woDto.getPage() != null && woDto.getPageSize() != null) {
			query.setFirstResult((woDto.getPage().intValue() - 1) * woDto.getPageSize().intValue());
			query.setMaxResults(woDto.getPageSize().intValue());
		}

		woDto.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		List<WoDTO> result = query.list();

		for (WoDTO item : result) {
			if ("THDT".equalsIgnoreCase(item.getWoTypeCode())) {
				item.setCalculateMethod("Theo tháng");
				item.setAcceptedMoneyValue(item.getAcceptedMoneyValueMonthly());
			} else {
				if ("THICONG".equalsIgnoreCase(item.getWoTypeCode()) == false
						|| item.getCatConstructionTypeId() == null) {
					item.setAcceptedMoneyValue(item.getMoneyValue());
					item.setCalculateMethod("Phần trăm");
				} else {
					if (item.getCatConstructionTypeId() != 2 && item.getCatConstructionTypeId() != 3) {
						item.setAcceptedMoneyValue(item.getMoneyValue());
						item.setCalculateMethod("Phần trăm");
					} else {
						item.setCalculateMethod("Độ dài");
						item.setAcceptedMoneyValue(item.getAcceptedMoneyValueDaily());
					}
				}
			}

			// Huypq-2912021-start comment chờ tối ưu
			// if ("HSHC".equalsIgnoreCase(item.getWoTypeCode())) {
			// Long consId = item.getConstructionId();
			// List<WorkItemDTO> completedWorkItems =
			// getDoneWorkItemByConstructionId(consId);
			// String allWorkItemNames = "";
			// for (WorkItemDTO completedWorkItem : completedWorkItems) {
			// allWorkItemNames += completedWorkItem.getName() + "; ";
			// }
			// item.setCatWorkItemTypeName(allWorkItemNames);
			// }
			//
			// if(item.getChecklistItemNames() == null){
			// if("HCQT".equalsIgnoreCase(item.getWoTypeCode())){
			// String checklistItemNames = getHcqtChecklistItemNames(item.getWoId());
			// item.setChecklistItemNames(checklistItemNames);
			// }
			//
			// if("THICONG".equalsIgnoreCase(item.getWoTypeCode()) &&
			// item.getCatConstructionTypeId() != null && 8 ==
			// item.getCatConstructionTypeId()){
			// String checklistItemNames = getXdddChecklistItemNames(item.getWoId());
			// item.setChecklistItemNames(checklistItemNames);
			// }
			//
			// if("THICONG".equalsIgnoreCase(item.getWoTypeCode()) &&
			// item.getCatConstructionTypeId() != null && 8 !=
			// item.getCatConstructionTypeId()){
			// String checklistItemNames = getChecklistItemNames(item.getWoId());
			// item.setChecklistItemNames(checklistItemNames);
			// }
			// }
			// Huypq-end comment chờ tối ưu
		}

		return result;
	}

	private String getHcqtChecklistItemNames(Long woId) {
		String sql = "select listagg(checklist_name , '; ') within group (order by checklist_name) checklistItemNames from wo_checklist where wo_id = :woId and status>0";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("woId", woId);
		query.addScalar("checklistItemNames", new StringType());

		return (String) query.uniqueResult();
	}

	private String getXdddChecklistItemNames(Long woId) {
		String sql = "select listagg(name , '; ') within group (order by name) checklistItemNames from wo_xddd_checklist where wo_id = :woId and status>0";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("woId", woId);
		query.addScalar("checklistItemNames", new StringType());

		return (String) query.uniqueResult();
	}

	private String getChecklistItemNames(Long woId) {
		String sql = "select listagg(ct.name , '; ') within group (order by ct.name) checklistItemNames "
				+ "from WO_MAPPING_CHECKLIST wmc " +
				// "LEFT JOIN WO wo on wmc.WO_ID = wo.ID " + //Huypq-comment 27012021
				"LEFT JOIN CTCT_COMS_OWNER.CAT_TASK ct ON wmc.CHECKLIST_ID = ct.CAT_TASK_ID " + "AND wmc.WO_ID = :woId "
				+ "AND wmc.status = 1";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("woId", woId);
		query.addScalar("checklistItemNames", new StringType());

		return (String) query.uniqueResult();
	}

	public Long checkWoMapping(long woId) {
		StringBuilder sql = new StringBuilder(
				"select count(*) as sl FROM WO_MAPPING_CHECKLIST where state not like '%DONE%' AND WO_ID= :woId");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("woId", woId);
		query.addScalar("sl", new LongType());
		if (query.list().size() > 0)
			return (Long) query.list().get(0);
		else
			return 0L;
	}

	public boolean checkOKAll(List<WoDTO> woList) {
		if (woList.size() == 0) {
			return false;
		}
		List<Long> woIdList = new ArrayList<>();
		for (WoDTO wo : woList)
			woIdList.add(wo.getWoId());

		StringBuilder sql = new StringBuilder(
				"select count(*) as totalOK from WO where STATUS > 0 and STATE = 'OK' and ID in (:woIdList) ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameterList("woIdList", woIdList);
		query.addScalar("totalOK", new IntegerType());

		int totalOk = (Integer) query.uniqueResult();

		if (totalOk != 0 && totalOk == woIdList.size())
			return true;
		else
			return false;
	}

	// Báo cáo THDT
	public List<ReportWoTHDTDTO> doSearchReportTHDT(ReportWoTHDTDTO obj) {
		StringBuilder sql = new StringBuilder("SELECT\n" + "    sg.province_code proCode,\n"
				+ "    cc.code contractCode,\n" + "    cc.signer_partner signerPartner,\n"
				+ "    to_char(w.money_flow_bill) moneyFlowBill,\n"
				+ "    to_char(w.money_flow_date,'dd/mm/yyyy') moneyFlowDate,\n"
				+ "    w.money_flow_value moneyFlowValue,\n" + "    SUM(w.money_flow_required) moneyFlowRequired\n"
				+ "FROM\n" + "   wo_type wt, sys_group sg,\n" + "    cnt_contract cc,\n" + "    wo w\n" + "WHERE\n"
				+ "    w.contract_id = cc.cnt_contract_id AND w.wo_type_id = wt.id\n"
				+ "    AND wt.wo_type_code like 'THDT'\n" + "    AND sg.sys_group_id = w.cd_level_2 ");
		if (obj.getDateFrom() != null && obj.getDateTo() != null) {
			sql.append(" AND w.end_time >= :dateFrom AND w.end_time <= :dateTo");
		}
		if (obj.getSysGroupId() != null) {
			sql.append(" AND sg.sysGroupId =:sysGroupId ");
		}
		sql.append(" GROUP BY\n" + "    sg.province_code,\n" + "    cc.code,\n" + "    cc.signer_partner,\n"
				+ "    w.money_flow_bill,\n" + "    w.money_flow_date,\n" + "    w.money_flow_value \n"
				+ " ORDER by   sg.province_code,cc.code, w.money_flow_date, cc.signer_partner");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		if (obj.getDateFrom() != null) {
			query.setParameter("dateFrom", obj.getDateFrom());
		}
		if (obj.getDateTo() != null) {
			query.setParameter("dateTo", obj.getDateTo());
		}
		if (obj.getSysGroupId() != null) {
			query.setParameter("sysGroupId", obj.getSysGroupId());
		}
		query.addScalar("proCode", new StringType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("signerPartner", new StringType());
		query.addScalar("moneyFlowBill", new StringType());
		query.addScalar("moneyFlowDate", new StringType());
		query.addScalar("moneyFlowValue", new LongType());
		query.addScalar("moneyFlowRequired", new LongType());
		query.setResultTransformer(Transformers.aliasToBean(ReportWoTHDTDTO.class));
		List<ReportWoTHDTDTO> lst = query.list();
		obj.setTotalRecord(lst.size());
		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		List<ReportWoTHDTDTO> lstNew = query.list();
		return lstNew;
	}

	public List<ReportWoTHDTDTO> exportFileTHDT(ReportWoTHDTDTO obj) {
		StringBuilder sql = new StringBuilder("SELECT\n" + "    sg.province_code proCode,\n"
				+ "    cc.code contractCode,\n" + "    cc.signer_partner signerPartner,\n"
				+ "    to_char(w.money_flow_bill) moneyFlowBill,\n"
				+ "    to_char(w.money_flow_date,'dd/mm/yyyy') moneyFlowDate,\n"
				+ "    w.money_flow_value moneyFlowValue,\n" + "    SUM(w.money_flow_required) moneyFlowRequired\n"
				+ "FROM\n" + "    wo_type wt,sys_group sg,\n" + "    cnt_contract cc,\n" + "    wo w\n" + "WHERE\n"
				+ "    w.contract_id = cc.cnt_contract_id AND w.wo_type_id = wt.id\n"
				+ "    AND wt.wo_type_code like '%THDT%'\n" + "    AND sg.sys_group_id = w.cd_level_2 ");
		if (obj.getDateFrom() != null && obj.getDateTo() != null) {
			sql.append(" AND w.end_time >= :dateFrom AND w.end_time <= :dateTo");
		}
		if (obj.getSysGroupId() != null) {
			sql.append(" AND sg.sysGroupId =:sysGroupId ");
		}
		sql.append(" GROUP BY\n" + "    sg.province_code,\n" + "    cc.code,\n" + "    cc.signer_partner,\n"
				+ "    w.money_flow_bill,\n" + "    w.money_flow_date,\n" + "    w.money_flow_value \n"
				+ " ORDER by   sg.province_code,cc.code, w.money_flow_date, cc.signer_partner");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		if (obj.getDateFrom() != null) {
			query.setParameter("dateFrom", obj.getDateFrom());
		}
		if (obj.getDateTo() != null) {
			query.setParameter("dateTo", obj.getDateTo());
		}
		if (obj.getSysGroupId() != null) {
			query.setParameter("sysGroupId", obj.getSysGroupId());
		}
		query.addScalar("proCode", new StringType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("signerPartner", new StringType());
		query.addScalar("moneyFlowBill", new StringType());
		query.addScalar("moneyFlowDate", new StringType());
		query.addScalar("moneyFlowValue", new LongType());
		query.addScalar("moneyFlowRequired", new LongType());
		query.setResultTransformer(Transformers.aliasToBean(ReportWoTHDTDTO.class));
		List<ReportWoTHDTDTO> lst = query.list();
		return lst;
	}

	public Double getCheckavgStatus(Long constructionId, Boolean isHTCT) {
		String sql = new String("select nvl(round(AVG(b.status),2),0) status "
				+ " from construction a, work_item b where a.CONSTRUCTION_ID=b.CONSTRUCTION_ID and a.status !=0  "
				+ " and a.construction_id = :constructionId ");
		// Nếu là ct thực hiện theo TR ĐBHT của ct HTCT thì chỉ check 5 hạng mục này
		if (isHTCT) {
			sql += " AND b.CAT_WORK_ITEM_TYPE_ID in (2083,356,358,365,360) ";
		}
		SQLQuery query = getSession().createSQLQuery(sql);
		query.addScalar("status", new DoubleType());
		query.setParameter("constructionId", constructionId);
		List<Double> lstDoub = query.list();
		if (lstDoub != null && lstDoub.size() > 0) {
			return lstDoub.get(0);
		}
		return 0D;
	}

	// Cap nhat ct dang hoat dong
	public int updateStartConstruction(Long constructionId) {
		StringBuilder sql = new StringBuilder(
				"update CONSTRUCTION SET status = 3 where CONSTRUCTION_ID = :constructionId");
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		query.setParameter("constructionId", constructionId);
		return query.executeUpdate();
	}

	public int updateStartWorkItiem(Long constructionId, Long catWorkID) {
		StringBuilder sql = new StringBuilder(
				"update WORK_ITEM SET status = '2', STARTING_DATE = sysdate where CONSTRUCTION_ID = :constructionId AND CAT_WORK_ITEM_TYPE_ID = :catWorkID");
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		query.setParameter("constructionId", constructionId);
		query.setParameter("catWorkID", catWorkID);
		return query.executeUpdate();
	}

	// Kiem tra ct khi ket thuc Wo
	@Transactional
	public int updatePercentConstructionCDOK(WoDTO dto, Double quantity, Boolean isHTCT, String state) {
		int result = 0;
		try {
			// cap nhat hang muc
			if (dto.getCatWorkItemTypeId() != null) {
				StringBuilder sql = new StringBuilder(
						"update WORK_ITEM SET STATUS = '3', COMPLETE_DATE = :completeDate,QUANTITY = :quantity  where CONSTRUCTION_ID = :constructionId AND CAT_WORK_ITEM_TYPE_ID = :catWorkID");
				SQLQuery query = this.getSession().createSQLQuery(sql.toString());
				query.setParameter("completeDate", new Date());
				query.setParameter("quantity", quantity);
				query.setParameter("constructionId", dto.getConstructionId());
				query.setParameter("catWorkID", dto.getCatWorkItemTypeId());
				result = query.executeUpdate();
			}
			this.getSession().flush();
			Double checkCountConstruction = getCheckavgStatus(dto.getConstructionId(), isHTCT);
			Boolean check = checkWo(dto.getConstructionId());
			// if (checkCountConstruction.compareTo(3.0) == 0 ||
			// "TR_DONG_BO_HA_TANG".equalsIgnoreCase(dto.getTrTypeCode())) {
			if (checkCountConstruction.compareTo(3.0) == 0) {
				if (dto.getContractId() != null && getCntContractRevenueById(dto.getContractId())
						&& "CD_OK".equalsIgnoreCase(state)) {
					List<WoDTO> lst = checkCntContractRevenue(dto);
					if (lst.size() == 1 && lst.get(0).getState().equalsIgnoreCase("CD_OK")) {
						StringBuilder sql = new StringBuilder("");
//						taotq start 01072022
						if (isHTCT) {
							if(check) {
								sql.append(" update CONSTRUCTION SET status = -5 ");
							}else {
								sql.append(" update CONSTRUCTION SET status = 5 ");
							}
							sql.append(" ,COMPLETE_DATE = :completeDate , COMPLETE_VALUE =(SELECT SUM(QUANTITY) FROM WORK_ITEM WHERE CONSTRUCTION_ID = :constructionWork AND CAT_WORK_ITEM_TYPE_ID in (2083,356,358,365,360)) WHERE CONSTRUCTION_ID = :constructionId ");
						} else {
							if(check) {
								sql.append(" update CONSTRUCTION SET status = -5 ");
							}else {
								sql.append(" update CONSTRUCTION SET status = 5 ");
							}
							sql.append(",COMPLETE_DATE = :completeDate , COMPLETE_VALUE =(SELECT SUM(nvl(QUANTITY,0)) FROM WORK_ITEM WHERE CONSTRUCTION_ID = :constructionWork) WHERE CONSTRUCTION_ID = :constructionId ");
						}
//						if (isHTCT) {
//							sql.append(
//									"update CONSTRUCTION SET status = 5,COMPLETE_DATE = :completeDate , COMPLETE_VALUE =(SELECT SUM(QUANTITY) FROM WORK_ITEM WHERE CONSTRUCTION_ID = :constructionWork AND CAT_WORK_ITEM_TYPE_ID in (2083,356,358,365,360)) WHERE CONSTRUCTION_ID = :constructionId ");
//						} else {
//							sql.append(
//									"update CONSTRUCTION SET status = 5,COMPLETE_DATE = :completeDate , COMPLETE_VALUE =(SELECT SUM(nvl(QUANTITY,0)) FROM WORK_ITEM WHERE CONSTRUCTION_ID = :constructionWork) WHERE CONSTRUCTION_ID = :constructionId ");
//						}
//						taotq start 01072022
						SQLQuery query = this.getSession().createSQLQuery(sql.toString());
						query.setParameter("constructionWork", dto.getConstructionId());
						query.setParameter("completeDate", new Date());
						query.setParameter("constructionId", dto.getConstructionId());
						result = query.executeUpdate();
						if (result == 1) {
							result = 2;
						}
					}
				} else {
					StringBuilder sql = new StringBuilder("");
//					taotq start 01072022
					if (isHTCT) {
						if(check) {
							sql.append(" update CONSTRUCTION SET status = -5 ");
						}else {
							sql.append(" update CONSTRUCTION SET status = 5 ");
						}
						sql.append(" ,COMPLETE_DATE = :completeDate , COMPLETE_VALUE =(SELECT SUM(QUANTITY) FROM WORK_ITEM WHERE CONSTRUCTION_ID = :constructionWork AND CAT_WORK_ITEM_TYPE_ID in (2083,356,358,365,360)) WHERE CONSTRUCTION_ID = :constructionId ");
					} else {
						if(check) {
							sql.append(" update CONSTRUCTION SET status = -5 ");
						}else {
							sql.append(" update CONSTRUCTION SET status = 5 ");
						}
						sql.append(" ,COMPLETE_DATE = :completeDate , COMPLETE_VALUE =(SELECT SUM(nvl(QUANTITY,0)) FROM WORK_ITEM WHERE CONSTRUCTION_ID = :constructionWork) WHERE CONSTRUCTION_ID = :constructionId ");
					}
//					if (isHTCT) {
//						sql.append("update CONSTRUCTION SET status = 5,COMPLETE_DATE = :completeDate , COMPLETE_VALUE =(SELECT SUM(QUANTITY) FROM WORK_ITEM WHERE CONSTRUCTION_ID = :constructionWork AND CAT_WORK_ITEM_TYPE_ID in (2083,356,358,365,360)) WHERE CONSTRUCTION_ID = :constructionId ");
//					} else {
//						sql.append("update CONSTRUCTION SET status = 5,COMPLETE_DATE = :completeDate , COMPLETE_VALUE =(SELECT SUM(nvl(QUANTITY,0)) FROM WORK_ITEM WHERE CONSTRUCTION_ID = :constructionWork) WHERE CONSTRUCTION_ID = :constructionId ");
//					}
//					taotq end 01072022
					SQLQuery query = this.getSession().createSQLQuery(sql.toString());
					query.setParameter("constructionWork", dto.getConstructionId());
					query.setParameter("completeDate", new Date());
					query.setParameter("constructionId", dto.getConstructionId());
					result = query.executeUpdate();
				}

			}
		} catch (Exception ex) {
			//ex.printStackTrace();
			result = 0;
		}
		return result;

	}

	public int updatePercentConstruction(WoDTO dto) {
		int result = 0;
		try {
			// cap nhat quy luong
			if ("QUYLUONG".equalsIgnoreCase(dto.getWoTypeCode())) {
				StringBuilder sqlConstruction = new StringBuilder(
						" update construction set COMPLETE_APPROVED_VALUE_PLAN = :quantity,APPROVE_COMPLETE_VALUE =:quantity, APPROVE_COMPLETE_STATE = 1,"
								+ " APPROVE_COMPLETE_DATE = :completeDate where construction_id= :constructionId");
				SQLQuery queryConstruction = getSession().createSQLQuery(sqlConstruction.toString());
				queryConstruction.setParameter("constructionId", dto.getConstructionId());
				queryConstruction.setParameter("completeDate", new Date());
				queryConstruction.setParameter("quantity", dto.getMoneyValue());
				result = queryConstruction.executeUpdate();
			}
			// cap nhat hồ sơ hoàn công cong trinh
			if ("HSHC".equalsIgnoreCase(dto.getWoTypeCode())) {
				StringBuilder sqlConstruction = new StringBuilder(" update construction set  "
						+ " APPROVE_REVENUE_DATE = :completeDate,APPROVE_REVENUE_STATE = 1,APPROVE_REVENUE_VALUE_PLAN = :quantity,APPROVE_REVENUE_VALUE = :quantity,STATUS = 7 where construction_id= :constructionId");
				SQLQuery queryConstruction = getSession().createSQLQuery(sqlConstruction.toString());
				queryConstruction.setParameter("completeDate", dto.getApproveDateReportWo());
				queryConstruction.setParameter("quantity", dto.getMoneyValue());
				queryConstruction.setParameter("constructionId", dto.getConstructionId());
				result = queryConstruction.executeUpdate();
			}
			if ("TMBTHTTC".equalsIgnoreCase(dto.getWoTypeCode()) && dto.getStationCode() != null) {
				StringBuilder sqlRent = new StringBuilder("update cat_station set  " + " complete_status=1, "
						+ " complete_date=sysdate " + " where code =:consCode ");
				SQLQuery queryRent = getSession().createSQLQuery(sqlRent.toString());
				queryRent.setParameter("consCode", dto.getStationCode());
				result = queryRent.executeUpdate();
			}
		} catch (Exception ex) {
			//ex.printStackTrace();
			result = 0;
		}
		return result;
	}

	public String getNameAppParam(String code, String parType) {
		StringBuilder sql = new StringBuilder(
				"select  NAME as name from APP_PARAM where PAR_TYPE = :parType and code = :code ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("parType", parType);
		query.setParameter("code", code);
		query.addScalar("name", new StringType());
		return (String) query.uniqueResult();
	}

	public Double getValueContract(Long idContract) {
		StringBuilder sql = new StringBuilder(
				"select SUM( nvl(amount,0)) amount from AIO_ACCEPTANCE_RECORDS where CONTRACT_ID = :idContract ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("idContract", idContract);
		query.addScalar("amount", new DoubleType());
		return (Double) query.uniqueResult();
	}

	public Double getValueAcceptane(Long idContract, Long contractDetailId) {
		StringBuilder sql = new StringBuilder(
				"select SUM( nvl(amount,0)) amount from AIO_ACCEPTANCE_RECORDS where CONTRACT_ID = :idContract AND CONTRACT_DETAIL_ID = :contractDetailId ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("idContract", idContract);
		query.setParameter("contractDetailId", contractDetailId);
		query.addScalar("amount", new DoubleType());
		return (Double) query.uniqueResult();
	}

	public int updateWoMappingDone(Long woId, Long checkListId, String state, Double amount) {
		StringBuilder sql = new StringBuilder(
				"UPDATE WO_MAPPING_CHECKLIST SET STATE = :state , QUANTITY_LENGTH = :amount WHERE  WO_ID = :woId AND CHECKLIST_ID = :checkListId  ");
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		query.setParameter("state", state);
		query.setParameter("amount", amount);
		query.setParameter("woId", woId);
		query.setParameter("checkListId", checkListId);
		return query.executeUpdate();
	}

	public AIOWoTrDTO getDateAcceptane(Long contractId) {
		StringBuilder sql = new StringBuilder(
				" select  min(start_date) startDate,max(end_date) endDate from AIO_ACCEPTANCE_RECORDS where contract_id = :contractId ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("contractId", contractId);
		query.addScalar("startDate", new DateType());
		query.addScalar("endDate", new DateType());
		query.setResultTransformer(Transformers.aliasToBean(AIOWoTrDTO.class));
		return (AIOWoTrDTO) query.uniqueResult();
	}

	public List<DomainDTO> getByAdResource(long sysUserId, String adResource) {
		StringBuilder sql = new StringBuilder("");
		sql.append("SELECT e.data_code dataCode, e.DATA_ID dataId, ");
		sql.append("  op.code ");
		sql.append("  ||' ' ");
		sql.append("  ||ad.code adResource ");
		sql.append("FROM SYS_USER a, ");
		sql.append("  USER_ROLE b, ");
		sql.append("  SYS_ROLE c, ");
		sql.append("  USER_ROLE_DATA d, ");
		sql.append("  DOMAIN_DATA e, ");
		sql.append("  ROLE_PERMISSION role_per, ");
		sql.append("  permission pe, ");
		sql.append("  OPERATION op, ");
		sql.append("  AD_RESOURCE ad ");
		sql.append("WHERE a.SYS_USER_ID       =b.SYS_USER_ID ");
		sql.append("AND b.SYS_ROLE_ID         =c.SYS_ROLE_ID ");
		sql.append("AND b.USER_ROLE_ID        =d.USER_ROLE_ID ");
		sql.append("AND d.DOMAIN_DATA_ID      =e.DOMAIN_DATA_ID ");
		sql.append("AND c.SYS_ROLE_ID         =role_per.SYS_ROLE_ID ");
		sql.append("AND role_per.permission_id=pe.permission_id ");
		sql.append("AND pe.OPERATION_id       =op.OPERATION_id ");
		sql.append("AND pe.AD_RESOURCE_ID     =ad.AD_RESOURCE_ID ");
		sql.append("AND a.SYS_USER_ID         = '" + sysUserId + "' ");
		sql.append("AND upper(op.code ");
		sql.append("  ||' ' ");
		sql.append("  ||ad.code) LIKE '%" + adResource + "%' ");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("dataCode", new StringType());
		query.addScalar("dataId", new LongType());
		query.addScalar("adResource", new StringType());

		query.setResultTransformer(Transformers.aliasToBean(DomainDTO.class));
		return query.list();
	}

	public boolean isCd(Long sysUserId) {
		String sql = "select  " + "    ur.SYS_USER_ID sysUserId  " + "    , ur.SYS_ROLE_ID " + "    , rp.PERMISSION_ID "
				+ "    , op.CODE opCode " + "    , ar.CODE arCode " + "from USER_ROLE ur "
				+ "left join ROLE_PERMISSION rp on ur.SYS_ROLE_ID = rp.SYS_ROLE_ID "
				+ "left join PERMISSION p ON rp.PERMISSION_ID = p.PERMISSION_ID "
				+ "left join operation op ON p.operation_id = op.operation_id "
				+ "left join ad_resource ar ON p.ad_resource_id = ar.ad_resource_id "
				+ "where ur.SYS_USER_ID = :sysUserId " + "AND op.CODE = 'CD' " + "AND ar.code = 'WOXL'";
		StringBuilder sqlBuilder = new StringBuilder(sql);
		SQLQuery query = getSession().createSQLQuery(sqlBuilder.toString());
		query.addScalar("sysUserId", new LongType());
		query.setParameter("sysUserId", sysUserId);
		query.setResultTransformer(Transformers.aliasToBean(SysUserDTO.class));

		return query.list().size() > 0 ? true : false;
	}

	public boolean hasRole(Long sysUserId, String operationCode, String resourceCode) {
		String sql = "select  " + "    ur.SYS_USER_ID sysUserId  " + "    , ur.SYS_ROLE_ID " + "    , rp.PERMISSION_ID "
				+ "    , op.CODE opCode " + "    , ar.CODE arCode " + "from USER_ROLE ur "
				+ "left join ROLE_PERMISSION rp on ur.SYS_ROLE_ID = rp.SYS_ROLE_ID "
				+ "left join PERMISSION p ON rp.PERMISSION_ID = p.PERMISSION_ID "
				+ "left join operation op ON p.operation_id = op.operation_id "
				+ "left join ad_resource ar ON p.ad_resource_id = ar.ad_resource_id "
				+ "where ur.SYS_USER_ID = :sysUserId " + "AND op.CODE = '" + operationCode + "' " + "AND ar.code = '"
				+ resourceCode + "'";
		StringBuilder sqlBuilder = new StringBuilder(sql);
		SQLQuery query = getSession().createSQLQuery(sqlBuilder.toString());
		query.addScalar("sysUserId", new LongType());
		query.setParameter("sysUserId", sysUserId);
		query.setResultTransformer(Transformers.aliasToBean(SysUserDTO.class));

		return query.list().size() > 0 ? true : false;
	}

	// Unikom - get data for chart - start
	public List<WoGeneralReportDTO> getGeneralReport(WoGeneralReportDTO dto, List<String> groupIdList) {
		StringBuilder sql = new StringBuilder(" select count(case when wo.status = 1 then 1 end) as totalWO ");
		sql.append(" , count(case when wo.state ='UNASSIGN' then 1 end) as totalUnassign ");
		sql.append(" , count(case when wo.state ='ASSIGN_CD' then 1 end) as totalAssignCd ");
		sql.append(" , count(case when wo.state ='ACCEPT_CD' then 1 end) as totalAcceptCd ");
		sql.append(" , count(case when wo.state ='REJECT_CD' then 1 end) as totalRejectCd ");
		sql.append(" , count(case when wo.state ='ASSIGN_FT' then 1 end) as totalAssignFt ");
		sql.append(" , count(case when wo.state ='ACCEPT_FT' then 1 end) as totalAcceptFt ");
		sql.append(" , count(case when wo.state ='REJECT_FT' then 1 end) as totalRejectFt ");
		sql.append(" , count(case when wo.state ='PROCESSING' then 1 end) as totalProcessing ");
		sql.append(" , count(case when wo.state ='DONE' then 1 end) as totalDone ");
		sql.append(" , count(case when wo.state ='CD_OK' then 1 end) as totalCdOk ");
		sql.append(" , count(case when wo.state ='CD_NG' then 1 end) as totalCdNotGood ");
		sql.append(" , count(case when wo.state ='OK'  then 1 end) as totalOk ");
		sql.append(" , count(case when wo.state ='NG' then 1 end) as totalNotGood ");
		sql.append(" , count(case when wo.state like '%OPINION_RQ%' then 1 end) as totalOpinionRequest ");
		sql.append(
				" , count(case when trunc(wo.finish_date) < nvl(trunc(wo.end_time),trunc(sysdate)) and wo.state not in ('OK','CD_OK','DONE') then 1 end) as totalOverDue ");
		sql.append(
				" , count(case when trunc(wo.finish_date) < nvl(trunc(wo.end_time),trunc(sysdate)) and wo.state  in ('OK','CD_OK','DONE') then 1 end) as totalFinishOverDue ");

		if (dto.getSectionId() != null || dto.getFtId() != null) {
			sql.append(" , wo.cd_level_2_name as areaName ");
			sql.append(" , wo.cd_level_4_name as sectionName ");
			sql.append(" , NVL(wo.ft_name, 'Chưa giao') as ftName, NVL(wo.ft_id, 0) as ftId ");
		} else if (dto.getAreaId() != null) {
			sql.append(" , wo.cd_level_2_name as areaName ");
			sql.append(" , wo.cd_level_4_name as sectionName ");
		} else if (dto.getGeoAreaList() != null) {
			sql.append(" , wo.cd_level_2_name as areaName ");
		}

		if (dto.getSectionId() != null || dto.getFtId() != null || dto.getAreaId() != null) {
			// sql.append(" , :geoArea as geoArea ");
			sql.append(" from wo ");
		}
		// else if (dto.getAreaId() != null) {
		// sql.append(" , :geoArea as geoArea ");
		// sql.append(" from wo ");
		// }
		// else if (StringUtils.isNotEmpty(dto.getGeoArea())) {
		// sql.append(" , NVL(sg.AREA_CODE, 'OTHERS') as geoArea ");
		// sql.append(" from sys_group sg ");
		// sql.append(" left join wo on sg.sys_group_id like wo.cd_level_2 and
		// sg.status>0 ");
		// }
		else {
			sql.append(" , NVL(sg.AREA_CODE, 'OTHERS') as geoArea ");
			sql.append(" from sys_group sg ");
			sql.append(" left join wo on sg.sys_group_id = wo.cd_level_2 and sg.status>0 ");
		}

		sql.append(" where wo.status>0 and wo.CD_LEVEL_1 is not null");

		if (dto.getFtId() != null) {
			sql.append(" and wo.ft_id = :ftId and wo.cd_level_4 = :sectionId");
		} else if (dto.getSectionId() != null) {
			sql.append(" and wo.cd_level_4 = :sectionId ");
		} else if (dto.getAreaId() != null) {
			sql.append(" and wo.cd_level_2 = :areaId ");
		} else if (dto.getGeoAreaList() != null) {
			if (dto.getGeoAreaList().size() > 0)
				sql.append(" and sg.area_code in (:geoAreaList) and sg.code like '%P.HT%' ");
		}

		if (groupIdList.size() > 0) {
			sql.append(
					" and (wo.cd_level_1 in (:groupIdList) or wo.cd_level_2 in (:groupIdList) or wo.cd_level_3 in (:groupIdList) or wo.cd_level_4 in (:groupIdList) )  ");
		}

		if (dto.getFromDate() != null) {
			sql.append(" and wo.created_date >= :fromDate ");
		}

		if (dto.getToDate() != null) {
			sql.append(" and wo.created_date <= :toDate ");
		}

		if (StringUtils.isNotEmpty(dto.getType()) && "0".equalsIgnoreCase(dto.getType()) == false) {
			sql.append(" and wo.type = :type ");
		}

		if (StringUtils.isNotEmpty(dto.getType()) && "0".equalsIgnoreCase(dto.getType())) {
			sql.append(" and wo.type is null ");
		}

		if (dto.getSectionId() != null || dto.getFtId() != null) {
			sql.append(
					" group by wo.cd_level_2_name, wo.cd_level_4_name, wo.ft_name, wo.ft_id  ORDER by wo.cd_level_2_name, wo.cd_level_4_name, wo.ft_name ");
		} else if (dto.getAreaId() != null) {
			sql.append(
					" group by wo.cd_level_2_name, wo.cd_level_4_name ORDER by wo.cd_level_2_name, wo.cd_level_4_name ");
		} else if (dto.getGeoAreaList() != null) {
			sql.append(" group by sg.AREA_CODE  ");
			sql.append(" , wo.cd_level_2_name  ORDER by sg.AREA_CODE , wo.cd_level_2_name ");
		} else {
			sql.append(" group by sg.AREA_CODE  ORDER by sg.AREA_CODE ");
		}

		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");

		StringBuilder sqlTotalQuantity = new StringBuilder("SELECT " + " NVL(sum(totalWO), 0) as totalAllWO, "
				+ " NVL(sum(totalUnassign), 0) as totalAllUnassign, "
				+ " NVL(sum(totalAssignCd), 0) as totalAllAssignCd, "
				+ " NVL(sum(totalAcceptCd), 0) as totalAllAcceptCd, "
				+ " NVL(sum(totalRejectCd), 0) as totalAllRejectCd, "
				+ " NVL(sum(totalAssignFt), 0) as totalAllAssignFt, "
				+ " NVL(sum(totalAcceptFt), 0) as totalAllAcceptFt, "
				+ " NVL(sum(totalRejectFt), 0) as totalAllRejectFt, "
				+ " NVL(sum(totalProcessing), 0) as totalAllProcessing, " + " NVL(sum(totalDone), 0) as totalAllDone, "
				+ " NVL(sum(totalCdOk), 0) as totalAllCdOk, " + " NVL(sum(totalCdNotGood), 0) as totalAllCdNotGood, "
				+ " NVL(sum(totalOk), 0) as totalAllOk, " + " NVL(sum(totalNotGood), 0) as totalAllNotGood, "
				+ " NVL(sum(totalOpinionRequest), 0) as totalAllOpinionRequest, "
				+ " NVL(sum(totalOverDue), 0) as totalAllOverDue, "
				+ " NVL(sum(totalFinishOverDue), 0) as totalFinishOverDue " + " FROM (");
		sqlTotalQuantity.append(sql);
		sqlTotalQuantity.append(")");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
		SQLQuery queryQuantity = getSession().createSQLQuery(sqlTotalQuantity.toString());

		query.addScalar("totalWO", new LongType());
		query.addScalar("totalUnassign", new LongType());
		query.addScalar("totalAssignCd", new LongType());
		query.addScalar("totalAcceptCd", new LongType());
		query.addScalar("totalRejectCd", new LongType());
		query.addScalar("totalAssignFt", new LongType());
		query.addScalar("totalAcceptFt", new LongType());
		query.addScalar("totalRejectFt", new LongType());
		query.addScalar("totalProcessing", new LongType());
		query.addScalar("totalDone", new LongType());
		query.addScalar("totalCdOk", new LongType());
		query.addScalar("totalCdNotGood", new LongType());
		query.addScalar("totalOk", new LongType());
		query.addScalar("totalNotGood", new LongType());
		query.addScalar("totalOpinionRequest", new LongType());
		query.addScalar("totalOverDue", new LongType());
		query.addScalar("totalFinishOverDue", new LongType());

		queryQuantity.addScalar("totalAllWO", new LongType());
		queryQuantity.addScalar("totalAllUnassign", new LongType());
		queryQuantity.addScalar("totalAllAssignCd", new LongType());
		queryQuantity.addScalar("totalAllAcceptCd", new LongType());
		queryQuantity.addScalar("totalAllRejectCd", new LongType());
		queryQuantity.addScalar("totalAllAssignFt", new LongType());
		queryQuantity.addScalar("totalAllAcceptFt", new LongType());
		queryQuantity.addScalar("totalAllRejectFt", new LongType());
		queryQuantity.addScalar("totalAllProcessing", new LongType());
		queryQuantity.addScalar("totalAllDone", new LongType());
		queryQuantity.addScalar("totalAllCdOk", new LongType());
		queryQuantity.addScalar("totalAllCdNotGood", new LongType());
		queryQuantity.addScalar("totalAllOk", new LongType());
		queryQuantity.addScalar("totalAllNotGood", new LongType());
		queryQuantity.addScalar("totalAllOpinionRequest", new LongType());
		queryQuantity.addScalar("totalAllOverDue", new LongType());
		queryQuantity.addScalar("totalFinishOverDue", new LongType());

		if (dto.getSectionId() != null || dto.getFtId() != null) {
			query.addScalar("areaName", new StringType());
			query.addScalar("sectionName", new StringType());
			query.addScalar("ftName", new StringType());
			query.addScalar("ftId", new LongType());
		} else if (dto.getAreaId() != null) {
			query.addScalar("areaName", new StringType());
			query.addScalar("sectionName", new StringType());
		} else if (dto.getGeoAreaList() != null) {
			query.addScalar("geoArea", new StringType());
			query.addScalar("areaName", new StringType());
		} else {
			query.addScalar("geoArea", new StringType());
		}

		if (dto.getFtId() != null) {
			query.setParameter("ftId", dto.getFtId());
			queryCount.setParameter("ftId", dto.getFtId());
			queryQuantity.setParameter("ftId", dto.getFtId());
			query.setParameter("sectionId", dto.getSectionId());
			queryCount.setParameter("sectionId", dto.getSectionId());
			queryQuantity.setParameter("sectionId", dto.getSectionId());
			query.setParameter("geoArea", dto.getGeoArea());
			queryCount.setParameter("geoArea", dto.getGeoArea());
			queryQuantity.setParameter("geoArea", dto.getGeoArea());
		} else if (dto.getSectionId() != null) {
			query.setParameter("sectionId", dto.getSectionId());
			queryCount.setParameter("sectionId", dto.getSectionId());
			queryQuantity.setParameter("sectionId", dto.getSectionId());
			// query.setParameter("geoArea", dto.getGeoArea());
			// queryCount.setParameter("geoArea", dto.getGeoArea());
			// queryQuantity.setParameter("geoArea", dto.getGeoArea());
		} else if (dto.getAreaId() != null) {
			query.setParameter("areaId", dto.getAreaId());
			queryCount.setParameter("areaId", dto.getAreaId());
			queryQuantity.setParameter("areaId", dto.getAreaId());
			// query.setParameter("geoArea", dto.getGeoArea());
			// queryCount.setParameter("geoArea", dto.getGeoArea());
			// queryQuantity.setParameter("geoArea", dto.getGeoArea());
		} else if (dto.getGeoAreaList() != null) {
			if (dto.getGeoAreaList().size() > 0) {
				query.setParameterList("geoAreaList", dto.getGeoAreaList());
				queryCount.setParameterList("geoAreaList", dto.getGeoAreaList());
				queryQuantity.setParameterList("geoAreaList", dto.getGeoAreaList());
			}
		}

		if (groupIdList.size() > 0) {
			query.setParameterList("groupIdList", groupIdList);
			queryCount.setParameterList("groupIdList", groupIdList);
			queryQuantity.setParameterList("groupIdList", groupIdList);
		}

		if (dto.getFromDate() != null) {
			query.setParameter("fromDate", dto.getFromDate());
			queryCount.setParameter("fromDate", dto.getFromDate());
			queryQuantity.setParameter("fromDate", dto.getFromDate());
		}

		if (dto.getToDate() != null) {
			query.setParameter("toDate", dto.getToDate());
			queryCount.setParameter("toDate", dto.getToDate());
			queryQuantity.setParameter("toDate", dto.getToDate());
		}

		if (StringUtils.isNotEmpty(dto.getType()) && "0".equalsIgnoreCase(dto.getType()) == false) {
			query.setParameter("type", dto.getType());
			queryCount.setParameter("type", dto.getType());
			queryQuantity.setParameter("type", dto.getType());
		}

		query.setResultTransformer(Transformers.aliasToBean(WoGeneralReportDTO.class));
		queryQuantity.setResultTransformer(Transformers.aliasToBean(WoGeneralReportDTO.class));

		if (dto.getPage() != null && dto.getPageSize() != null) {
			query.setFirstResult((dto.getPage().intValue() - 1) * dto.getPageSize().intValue());
			query.setMaxResults(dto.getPageSize().intValue());
		}

		dto.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		List<WoGeneralReportDTO> reports = query.list();
		WoGeneralReportDTO reportTotalAll = (WoGeneralReportDTO) queryQuantity.uniqueResult();

		if (reports.size() > 0) {
			reports.get(0).setTotalAllWO(reportTotalAll.getTotalAllWO());
			reports.get(0).setTotalAllUnassign(reportTotalAll.getTotalAllUnassign());
			reports.get(0).setTotalAllAssignCd(reportTotalAll.getTotalAllAssignCd());
			reports.get(0).setTotalAllAcceptCd(reportTotalAll.getTotalAllAcceptCd());
			reports.get(0).setTotalAllRejectCd(reportTotalAll.getTotalAllRejectCd());
			reports.get(0).setTotalAllAssignFt(reportTotalAll.getTotalAllAssignFt());
			reports.get(0).setTotalAllAcceptFt(reportTotalAll.getTotalAllAcceptFt());
			reports.get(0).setTotalAllRejectFt(reportTotalAll.getTotalAllRejectFt());
			reports.get(0).setTotalAllProcessing(reportTotalAll.getTotalAllProcessing());
			reports.get(0).setTotalAllDone(reportTotalAll.getTotalAllDone());
			reports.get(0).setTotalAllCdOk(reportTotalAll.getTotalAllCdOk());
			reports.get(0).setTotalAllCdNotGood(reportTotalAll.getTotalAllCdNotGood());
			reports.get(0).setTotalAllOk(reportTotalAll.getTotalAllOk());
			reports.get(0).setTotalAllNotGood(reportTotalAll.getTotalAllNotGood());
			reports.get(0).setTotalAllOpinionRequest(reportTotalAll.getTotalAllOpinionRequest());
			reports.get(0).setTotalAllOverDue(reportTotalAll.getTotalAllOverDue());
			reports.get(0).setTotalAllTotalFinishOverDue(reportTotalAll.getTotalFinishOverDue());
		}

		return reports;
	}

	public List<WoGeneralReportDTO> getDataForChart(Long loginUserId, List<String> lstGroupIds) {
		String sqlChart = "select\n" + "    count(case when wo.status = 1 then 1 end) as totalWO\n"
				+ "    , count(case when wo.state ='UNASSIGN' then 1 end) as totalUnassign\n"
				+ "    , count(case when wo.state ='ASSIGN_CD' then 1 end) as totalAssignCd\n"
				+ "    , count(case when wo.state ='ACCEPT_CD' then 1 end) as totalAcceptCd\n"
				+ "    , count(case when wo.state ='REJECT_CD' then 1 end) as totalRejectCd\n"
				+ "    , count(case when wo.state ='ASSIGN_FT' then 1 end) as totalAssignFt\n"
				+ "    , count(case when wo.state ='ACCEPT_FT' then 1 end) as totalAcceptFt\n"
				+ "    , count(case when wo.state ='REJECT_FT' then 1 end) as totalRejectFt\n"
				+ "    , count(case when wo.state ='PROCESSING' then 1 end) as totalProcessing\n"
				+ "    , count(case when wo.state ='DONE' then 1 end) as totalDone\n"
				+ "    , count(case when wo.state ='CD_OK' then 1 end) as totalCdOk\n"
				+ "    , count(case when wo.state ='CD_NG' then 1 end) as totalCdNotGood\n"
				+ "    , count(case when wo.state ='OK' then 1 end) as totalOk\n"
				+ "    , count(case when wo.state ='NG' then 1 end) as totalNotGood\n"
				+ "    , count(case when wo.state like '%OPINION_RQ%' then 1 end) as totalOpinionRequest\n"
				+ "from wo\n" + "left join WO_TYPE wt on wo.WO_TYPE_ID = wt.ID\n" + "where wo.FT_ID = :ftId\n"
				+ "and wo.STATUS > 0 and wt.WO_TYPE_CODE != 'HCQT' and wt.WO_TYPE_CODE != 'AIO' ";
		// if (lstGroupIds.size() > 0) {
		// sqlChart += " or CD_LEVEL_4 in (:lstGroupIds)";
		// }
		// sqlChart += ")";
		StringBuilder sql = new StringBuilder(sqlChart);
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("totalWO", new LongType());
		query.addScalar("totalUnassign", new LongType());
		query.addScalar("totalAssignCd", new LongType());
		query.addScalar("totalAcceptCd", new LongType());
		query.addScalar("totalRejectCd", new LongType());
		query.addScalar("totalAssignFt", new LongType());
		query.addScalar("totalAcceptFt", new LongType());
		query.addScalar("totalRejectFt", new LongType());
		query.addScalar("totalProcessing", new LongType());
		query.addScalar("totalDone", new LongType());
		query.addScalar("totalCdOk", new LongType());
		query.addScalar("totalCdNotGood", new LongType());
		query.addScalar("totalOk", new LongType());
		query.addScalar("totalNotGood", new LongType());
		query.addScalar("totalOpinionRequest", new LongType());

		query.setParameter("ftId", loginUserId);
		// if (lstGroupIds.size() > 0) {
		// query.setParameterList("lstGroupIds", lstGroupIds);
		// }

		query.setResultTransformer(Transformers.aliasToBean(WoGeneralReportDTO.class));
		return query.list();
	}
	// Unikom - get data for chart - end

	public Long getCatConstructionId(long constructionId) {
		String sql = "select cat_construction_type_id as catConType from construction where construction_id = :constructionId and status >0";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.addScalar("catConType", new LongType());
		query.setParameter("constructionId", constructionId);
		return (Long) query.uniqueResult();
	}

	public String getStationByConstruction(long constructionId) {
		String sql = " select s.code as stationCode from construction c "
				+ " left join cat_station s on c.cat_station_id = s.cat_station_id "
				+ " where c.construction_id = :constructionId and c.status >0 and s.status>0 ";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.addScalar("stationCode", new StringType());
		query.setParameter("constructionId", constructionId);
		return (String) query.uniqueResult();
	}

	public boolean checkExistConstructionWorkItem(Long constructionId, Long catWorkItemTypeId) {
		String sql = "Select construction_id from work_item where status>0 and construction_id = :constructionId and cat_work_item_type_id = :catWorkItemTypeId ";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("constructionId", constructionId);
		query.setParameter("catWorkItemTypeId", catWorkItemTypeId);
		query.addScalar("construction_id", new LongType());
		if (query.list().size() > 0)
			return true;
		else
			return false;
	}

	public void insertWorkItem(WoDTO dto) {
		String sql = " Insert into work_item (CONSTRUCTION_ID, CAT_WORK_ITEM_TYPE_ID, CODE, NAME, IS_INTERNAL, CONSTRUCTOR_ID, SUPERVISOR_ID, STATUS, CREATED_DATE, CREATED_USER_ID, CREATED_GROUP_ID, WORK_ITEM_ID, BRANCH) "
				+ " values (:constructionId, :catWorkItemTypeId, :workItemCode, :name, 1, :constructorId, :constructorId, 1, sysdate, :createdUserId, :createGroupId, work_item_seq.nextval, :branch) ";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("constructionId", dto.getConstructionId());
		query.setParameter("catWorkItemTypeId", dto.getCatWorkItemTypeId());
		query.setParameter("workItemCode", dto.getConstructionCode() + "_" + dto.getCatWorkItemTypeCode());
		query.setParameter("name", dto.getCatWorkItemTypeName());
		if (dto.getCdLevel2() != null) {
			query.setParameter("constructorId", dto.getCdLevel2());
		} else if (dto.getCdLevel5() != null) {
			query.setParameter("constructorId", dto.getCdLevel5());
		} else {
			query.setParameter("constructorId", -1);
		}
		query.setParameter("createdUserId", dto.getSysUserId());
		query.setParameter("createGroupId", dto.getSysGroupId());
		query.setParameter("branch", dto.getWorkItemBranch());
		query.executeUpdate();
	}

	// Báo cáo AIO
	public List<ReportWoAIODTO> doSearchReportAIO(ReportWoAIODTO obj) {
		StringBuilder sql = new StringBuilder(" select\n" + "        w.CD_LEVEL_2_NAME cdLevel2    ,\n"
				+ "        w.CD_LEVEL_4_NAME cdLevel4  ,\n" + "        wtr.TR_CODE trCode ,\n"
				+ "        wtr.CONTRACT_CODE contractCode     ,\n" + "        w.WO_CODE woCode      ,\n"
				+ "        ac.INDUSTRY_CODE insdustryName,\n" + "        wtr.QUANTITY_VALUE moneyValue     ,\n"
				+ "        ap.name state      ,\n" + "        w.CREATED_USER_FULL_NAME userCreated     ,\n"
				+ "        w.FT_NAME userFt     ,\n" + "        TO_CHAR(w.ACCEPT_TIME,\n"
				+ "        'dd/MM/yyyy HH:mm:ss')  acceptTime      ,\n" + "        TO_CHAR(w.END_TIME,\n"
				+ "        'dd/MM/yyyy HH:mm:ss')  endTime     ,\n" + "        LISTAGG(ww.CONTENT,\n"
				+ "        ', ') within \n" + "    group( order by\n" + "        ww.CONTENT ) reason      \n"
				+ "    from\n" + "        WO_TR_TYPE wt,\n" + "        wo_tr wtr\n"
				+ "     LEFT JOIN APP_PARAM ap ON  ap.par_type = 'WO_TR_XL_STATE' and ap.code= wtr.state \n"
				+ "        left join  wo w  on wtr.id = w.TR_ID and w.status = 1\n" + "    left join\n"
				+ "        AIO_CONTRACT ac \n" + "            on wtr.CONTRACT_ID = ac.CONTRACT_ID \n"
				+ "    left join\n" + "        TR_WORKLOGS ww \n" + "            on wtr.ID = ww.tr_id \n"
				+ "            AND (\n" + "                ww.CONTENT like '%REJECT%' \n"
				+ "                OR ww.CONTENT like '%Từ chối%'\n" + "            ) \n" + "    where\n"
				+ "        1 = 1 \n" + "        and wtr.STATUS = 1\n" + "        and wt.id = wtr.TR_TYPE_ID \n"
				+ "        and wt.tr_type_code like 'AIO'");
		if (StringUtils.isNotEmpty(obj.getDateFrom())) {
			sql.append(" AND wtr.CREATED_DATE >=  to_date(:dateFrom,'DD/MM/YYYY') ");
		}
		if (StringUtils.isNotEmpty(obj.getDateTo())) {
			sql.append("  AND wtr.CREATED_DATE <= to_date(:dateTo,'DD/MM/YYYY') + 1 ");
		}
		if (obj.getSysGroupId() != null) {
			sql.append(" AND (w.cd_level_2 =:sysGroupId OR w.cd_level_3 =:sysGroupId OR w.cd_level_4 =:sysGroupId) ");
		}
		sql.append("  group by\n" + "        w.CD_LEVEL_2_NAME, w.CD_LEVEL_4_NAME,wtr.TR_CODE,wtr.CONTRACT_CODE,\n"
				+ "        w.WO_CODE,ac.INDUSTRY_CODE,wtr.QUANTITY_VALUE,ap.name,w.CREATED_USER_FULL_NAME,\n"
				+ "        w.FT_NAME,w.ACCEPT_TIME,w.END_TIME \n" + "    order by\n"
				+ "        w.CD_LEVEL_2_NAME, w.CD_LEVEL_4_NAME, wtr.TR_CODE,wtr.CONTRACT_CODE,w.WO_CODE,\n"
				+ "        ac.INDUSTRY_CODE,wtr.QUANTITY_VALUE, ap.name,\n"
				+ "        w.CREATED_USER_FULL_NAME, w.FT_NAME, w.ACCEPT_TIME, w.END_TIME");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		if (StringUtils.isNotEmpty(obj.getDateFrom())) {
			query.setParameter("dateFrom", obj.getDateFrom());
		}
		if (StringUtils.isNotEmpty(obj.getDateTo())) {
			query.setParameter("dateTo", obj.getDateTo());
		}
		if (obj.getSysGroupId() != null) {
			query.setParameter("sysGroupId", obj.getSysGroupId());
			query.setParameter("sysGroupId", obj.getSysGroupId());
			query.setParameter("sysGroupId", obj.getSysGroupId());
		}
		query.addScalar("cdLevel2", new StringType());
		query.addScalar("cdLevel4", new StringType());
		query.addScalar("trCode", new StringType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("woCode", new StringType());
		query.addScalar("insdustryName", new StringType());
		query.addScalar("moneyValue", new LongType());
		query.addScalar("state", new StringType());
		query.addScalar("userCreated", new StringType());
		query.addScalar("userFt", new StringType());
		query.addScalar("acceptTime", new StringType());
		query.addScalar("endTime", new StringType());
		query.addScalar("reason", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(ReportWoAIODTO.class));
		List<ReportWoAIODTO> lst = query.list();
		obj.setTotalRecord(lst.size());
		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		List<ReportWoAIODTO> lstNew = query.list();
		return lstNew;
	}

	public List<ReportWoAIODTO> exportFileAIO(ReportWoAIODTO obj) {
		StringBuilder sql = new StringBuilder(" select\n" + "        w.CD_LEVEL_2_NAME cdLevel2    ,\n"
				+ "        w.CD_LEVEL_4_NAME cdLevel4  ,\n" + "        wtr.TR_CODE trCode ,\n"
				+ "        wtr.CONTRACT_CODE contractCode     ,\n" + "        w.WO_CODE woCode      ,\n"
				+ "        ac.INDUSTRY_CODE insdustryName,\n" + "        wtr.QUANTITY_VALUE moneyValue     ,\n"
				+ "        ap.name state      ,\n" + "        w.CREATED_USER_FULL_NAME userCreated     ,\n"
				+ "        w.FT_NAME userFt     ,\n" + "        TO_CHAR(w.ACCEPT_TIME,\n"
				+ "        'dd/MM/yyyy HH:mm:ss')  acceptTime      ,\n" + "        TO_CHAR(w.END_TIME,\n"
				+ "        'dd/MM/yyyy HH:mm:ss')  endTime     ,\n" + "        LISTAGG(ww.CONTENT,\n"
				+ "        ', ') within \n" + "    group( order by\n" + "        ww.CONTENT ) reason      \n"
				+ "    from\n" + "        WO_TR_TYPE wt,\n" + "        wo_tr wtr\n"
				+ "     LEFT JOIN APP_PARAM ap ON  ap.par_type = 'WO_TR_XL_STATE' and ap.code= wtr.state \n"
				+ "        left join  wo w  on wtr.id = w.TR_ID and w.status = 1\n" + "    left join\n"
				+ "        AIO_CONTRACT ac \n" + "            on wtr.CONTRACT_ID = ac.CONTRACT_ID \n"
				+ "    left join\n" + "        TR_WORKLOGS ww \n" + "            on wtr.ID = ww.tr_id \n"
				+ "            AND (\n" + "                ww.CONTENT like '%REJECT%' \n"
				+ "                OR ww.CONTENT like '%Từ chối%'\n" + "            ) \n" + "    where\n"
				+ "        1 = 1 \n" + "        and wtr.STATUS = 1\n" + "        and wt.id = wtr.TR_TYPE_ID \n"
				+ "        and wt.tr_type_code like 'AIO'");
		if (StringUtils.isNotEmpty(obj.getDateFrom())) {
			sql.append(" AND wtr.CREATED_DATE >=  to_date(:dateFrom,'DD/MM/YYYY') ");
		}
		if (StringUtils.isNotEmpty(obj.getDateTo())) {
			sql.append("  AND wtr.CREATED_DATE <= to_date(:dateTo,'DD/MM/YYYY') + 1 ");
		}
		if (obj.getSysGroupId() != null) {
			sql.append(" AND (w.cd_level_2 =:sysGroupId OR w.cd_level_3 =:sysGroupId OR w.cd_level_4 =:sysGroupId) ");
		}
		sql.append("  group by\n" + "        w.CD_LEVEL_2_NAME, w.CD_LEVEL_4_NAME,wtr.TR_CODE,wtr.CONTRACT_CODE,\n"
				+ "        w.WO_CODE,ac.INDUSTRY_CODE,wtr.QUANTITY_VALUE,ap.name,w.CREATED_USER_FULL_NAME,\n"
				+ "        w.FT_NAME,w.ACCEPT_TIME,w.END_TIME \n" + "    order by\n"
				+ "        w.CD_LEVEL_2_NAME, w.CD_LEVEL_4_NAME, wtr.TR_CODE,wtr.CONTRACT_CODE,w.WO_CODE,\n"
				+ "        ac.INDUSTRY_CODE,wtr.QUANTITY_VALUE, ap.name,\n"
				+ "        w.CREATED_USER_FULL_NAME, w.FT_NAME, w.ACCEPT_TIME, w.END_TIME");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		if (StringUtils.isNotEmpty(obj.getDateFrom())) {
			query.setParameter("dateFrom", obj.getDateFrom());
		}
		if (StringUtils.isNotEmpty(obj.getDateTo())) {
			query.setParameter("dateTo", obj.getDateTo());
		}
		if (obj.getSysGroupId() != null) {
			query.setParameter("sysGroupId", obj.getSysGroupId());
			query.setParameter("sysGroupId", obj.getSysGroupId());
			query.setParameter("sysGroupId", obj.getSysGroupId());
		}
		query.addScalar("cdLevel2", new StringType());
		query.addScalar("cdLevel4", new StringType());
		query.addScalar("trCode", new StringType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("woCode", new StringType());
		query.addScalar("insdustryName", new StringType());
		query.addScalar("moneyValue", new LongType());
		query.addScalar("state", new StringType());
		query.addScalar("userCreated", new StringType());
		query.addScalar("userFt", new StringType());
		query.addScalar("acceptTime", new StringType());
		query.addScalar("endTime", new StringType());
		query.addScalar("reason", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(ReportWoAIODTO.class));
		List<ReportWoAIODTO> lst = query.list();
		return lst;
	}

	public Long getStatusContractAIO(Long contractId) {
		String sql = "SELECT status from AIO_CONTRACT where CONTRACT_ID = :contractId";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.addScalar("status", new LongType());
		query.setParameter("contractId", contractId);
		return (Long) query.uniqueResult();
	}

	public WoSimpleFtDTO getFtById(Long ftId) {
		StringBuilder sql = new StringBuilder(
				" select SYS_USER_ID as ftId, EMPLOYEE_CODE as employeeCode, email as email, FULL_NAME as fullName, SYS_GROUP_ID as sysGroupId "
						+ " from SYS_USER where STATUS > 0 and SYS_USER_ID = :ftId ");

		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.setParameter("ftId", ftId);

		query.addScalar("ftId", new LongType());
		query.addScalar("sysGroupId", new LongType());
		query.addScalar("fullName", new StringType());
		query.addScalar("employeeCode", new StringType());
		query.addScalar("email", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(WoSimpleFtDTO.class));

		return (WoSimpleFtDTO) query.uniqueResult();
	}

	public List<WoSimpleFtDTO> getFtListFromLv2SysGroup(Long groupId, String keySearch) {
		StringBuilder sql = new StringBuilder(
				" select SYS_USER_ID as ftId, EMPLOYEE_CODE as employeeCode, email as email, FULL_NAME as fullName, SYS_GROUP_ID as sysGroupId "
						+ " from SYS_USER where STATUS > 0  AND TYPE_USER is null ");

		if (groupId != null)
			sql.append(" and SYS_GROUP_ID in (select SYS_GROUP_ID from SYS_GROUP where parent_id = :groupId) ");
		if (StringUtils.isNotEmpty(keySearch))
			sql.append(
					" and (lower(LOGIN_NAME) like :keysearch or lower(FULL_NAME) like :keysearch or lower(email) like :keysearch) fetch next 20 rows only ");

		SQLQuery query = getSession().createSQLQuery(sql.toString());

		if (groupId != null)
			query.setParameter("groupId", groupId);
		if (StringUtils.isNotEmpty(keySearch))
			query.setParameter("keysearch", "%" + keySearch.toLowerCase() + "%");

		query.addScalar("ftId", new LongType());
		query.addScalar("sysGroupId", new LongType());
		query.addScalar("fullName", new StringType());
		query.addScalar("employeeCode", new StringType());
		query.addScalar("email", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(WoSimpleFtDTO.class));

		return query.list();
	}

	public String getCdLevel2FromProvinceCode(String provinceCode) {
		StringBuilder sql = new StringBuilder(
				" select sys_group_id as cdLevel2 from sys_group where status >0 and code like '%P.HT' and province_code like :provinceCode fetch next 1 row only ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("provinceCode", provinceCode);
		query.addScalar("cdLevel2", new StringType());
		return (String) query.uniqueResult();
	}

	public List<WoSimpleSysGroupDTO> getChildrenGroup(String id) {
		String sql = " select SYS_GROUP_ID as sysGroupId, NAME as groupName " + " from SYS_GROUP "
				+ " where status>0 and parent_id = :id ";
		SQLQuery query = getSession().createSQLQuery(sql);

		query.setParameter("id", id);

		query.addScalar("groupName", new StringType());
		query.addScalar("sysGroupId", new LongType());
		query.setResultTransformer(Transformers.aliasToBean(WoSimpleSysGroupDTO.class));

		return query.list();
	}

	public WoSimpleSysGroupDTO getSysGroup(String id) {
		String sql = " select SYS_GROUP_ID as sysGroupId, NAME as groupName, GROUP_LEVEL as groupLevel, code, PROVINCE_ID as provinceId "
				+ " from SYS_GROUP " + " where status>0 and SYS_GROUP_ID = :id ";
		SQLQuery query = getSession().createSQLQuery(sql);

		query.setParameter("id", id);

		query.addScalar("groupName", new StringType());
		query.addScalar("code", new StringType());
		query.addScalar("sysGroupId", new LongType());
		query.addScalar("groupLevel", new IntegerType());
		query.addScalar("provinceId", new LongType());
		query.setResultTransformer(Transformers.aliasToBean(WoSimpleSysGroupDTO.class));

		return (WoSimpleSysGroupDTO) query.uniqueResult();
	}

	public WoSimpleSysGroupDTO getParentGroup(String id) {
		String sql = " select SYS_GROUP_ID as sysGroupId, NAME as groupName, GROUP_LEVEL as groupLevel "
				+ " from SYS_GROUP where status>0 "
				+ " and SYS_GROUP_ID = (select parent_id from sys_group where sys_group_id = :id ) ";
		SQLQuery query = getSession().createSQLQuery(sql);

		query.setParameter("id", id);

		query.addScalar("groupName", new StringType());
		query.addScalar("sysGroupId", new LongType());
		query.addScalar("groupLevel", new IntegerType());
		query.setResultTransformer(Transformers.aliasToBean(WoSimpleSysGroupDTO.class));

		return (WoSimpleSysGroupDTO) query.uniqueResult();
	}

	// bao cao theo dang vương
	public List<ReportHSHCQTDTO> doSearchReportHSHCStatus(ReportHSHCQTDTO obj) {
		StringBuilder sqlQuerySum = new StringBuilder();
		StringBuilder sql = new StringBuilder("     WITH bc AS (   \n" + "  SELECT \tb.id,\n"
				+ "        b.CHECKLIST_ORDER,\n" + "        b.checklist_step,\n" + "        b.has_problem ,\n"
				+ "        b.problemname,\n" + "        b.moneyvalue,\n" + "        b.dnqtvalue,\n"
				+ "        b.vtnetvalue,\n" + "        b.aprovedvalue from (SELECT\n" + "        a.id,\n"
				+ "        a.CHECKLIST_ORDER,\n" + "        a.checklist_step,\n" + "        a.has_problem ,\n"
				+ "        (CASE              \n" + "            when  nvl(a.has_problem,\n"
				+ "            0) >  1 then 'Khác'                \n"
				+ "            else          (CASE                  \n" + "                when  nvl(a.has_problem,\n"
				+ "                0) = 0 then 'Không vướng'                   \n"
				+ "                ELSE   a.problem_name              \n" + "            end )                  \n"
				+ "        end) problemname,\n" + "        a.money_value moneyvalue,\n"
				+ "        SUM(nvl(wcl.dnqt_value,\n" + "        0) ) dnqtvalue,\n"
				+ "        SUM(nvl(wcl.vtnet_sent_value,\n" + "        0) ) vtnetvalue,\n"
				+ "        SUM(nvl(wcl.VTNET_CONFIRM_VALUE,\n" + "        0) ) aprovedvalue ,\n"
				+ "          MAX(DNQT_DATE) DNQT_DATE,\n" + "        MAX(VTNET_SEND_DATE) VTNET_SEND_DATE,\n"
				+ "        MAX(VTNET_CONFIRM_DATE) VTNET_CONFIRM_DATE\n" + "    FROM\n" + "        wo_checklist wcl,\n"
				+ "        (     SELECT\n" + "            DISTINCT          \n" + "            wo.id,\n"
				+ "            wo.checklist_step,\n" + "            wc.CHECKLIST_ORDER,\n"
				+ "            wc.problem_name,\n" + "            wo.money_value ,\n"
				+ "            nvl(wc.has_problem,0)  has_problem                    \n" + "        FROM\n"
				+ "            WO_TYPE wt,\n" + "            wo_checklist wc,\n"
				+ "            wo wo                        \n" + "        WHERE\n" + "            wc.wo_id = wo.id\n"
				+ "            AND wo.wo_type_id= wt.id\n" + "            AND wt.wo_type_code='HCQT'\n"
				+ "            AND wo.status = 1 \n" + "            AND wc.CHECKLIST_ORDER = wo.checklist_step + 1");
		if (StringUtils.isNotEmpty(obj.getDateFrom())) {
			sql.append(" AND wo.CREATED_DATE >= TO_DATE(:fromDate, 'dd/mm/yyyy')");
		}
		if (StringUtils.isNotEmpty(obj.getDateTo())) {
			sql.append(" AND wo.CREATED_DATE < TO_DATE(:toDate, 'dd/mm/yyyy') + 1 ");
		}

		sql.append("   ) a  WHERE\n" + "        wcl.wo_id = a.id                      \n" + "    GROUP BY\n"
				+ "        a.id,\n" + "        a.problem_name,\n" + "        a.has_problem,\n"
				+ "        a.money_value,\n" + "        a.checklist_step,\n"
				+ "        a.CHECKLIST_ORDER  ) b where 1=1");

		if (StringUtils.isNotEmpty(obj.getDateFromDNQT())) {
			sql.append(" AND b.DNQT_DATE >= TO_DATE(:fromDateDNQT, 'dd/mm/yyyy')");
		}
		if (StringUtils.isNotEmpty(obj.getDateToDNQT())) {
			sql.append(" AND b.DNQT_DATE < TO_DATE(:toDateDNQT, 'dd/mm/yyyy') + 1 ");
		}
		if (StringUtils.isNotEmpty(obj.getDateFromVTnet())) {
			sql.append(" AND b.VTNET_SEND_DATE >= TO_DATE(:fromDateVTnet, 'dd/mm/yyyy')");
		}
		if (StringUtils.isNotEmpty(obj.getDateToVTnet())) {
			sql.append(" AND b.VTNET_SEND_DATE < TO_DATE(:toDateVTnet, 'dd/mm/yyyy') + 1 ");
		}
		if (StringUtils.isNotEmpty(obj.getDateFromInvestor())) {
			sql.append(" AND b.VTNET_CONFIRM_DATE >= TO_DATE(:fromDateInvestor, 'dd/mm/yyyy')");
		}
		if (StringUtils.isNotEmpty(obj.getDateToInvestor())) {
			sql.append(" AND b.VTNET_CONFIRM_DATE < TO_DATE(:toDateInvestor, 'dd/mm/yyyy') + 1 ");
		}
		sql.append("  )");
		sqlQuerySum.append(sql);
		sqlQuerySum.append("SELECT    SUM(quantitymoney) quantityMoneyTotal,\n"
				+ "                    SUM(moneyvalue) moneyvalueTotal,\n"
				+ "                    SUM(quantitydnqt) quantitydnqtTotal,\n"
				+ "                    SUM(dnqtvalue) dnqtvalueTotal,\n"
				+ "                    SUM(quantityvtnet) quantityvtnetTotal,\n"
				+ "                    SUM(vtnetvalue) vtnetvalueTotal,\n"
				+ "                    SUM(quantityaproved) quantityaprovedTotal,\n"
				+ "                    SUM(aprovedvalue) aprovedvalueTotal FROM (");
		String sqltext = " SELECT\n"
				+ "    problemName, SUM(moneyvalue) + SUM(dnqtvalue) +  SUM(aprovedvalue) + SUM(vtnetvalue) valueTotal , \n"
				+ "    SUM(quantitymoney) quantityMoney,\n" + "    SUM(moneyvalue) moneyvalue,\n"
				+ "    SUM(quantitydnqt) quantitydnqt,\n" + "    SUM(dnqtvalue) dnqtvalue,\n"
				+ "    SUM(quantityvtnet) quantityvtnet,\n" + "    SUM(vtnetvalue) vtnetvalue,\n"
				+ "    SUM(quantityaproved) quantityaproved,\n" + "    SUM(aprovedvalue) aprovedvalue\n" + "  FROM\n"
				+ "    (\n" + "        SELECT\n" + "            bc.problemname,\n"
				+ "            COUNT(bc.id) quantitymoney,\n" + "            SUM(moneyvalue) moneyvalue,\n"
				+ "            0 quantitydnqt,\n" + "            0 dnqtvalue,\n" + "            0 quantityvtnet,\n"
				+ "            0 vtnetvalue,\n" + "            0 quantityaproved,\n" + "            0 aprovedvalue\n"
				+ "        FROM\n" + "            bc\n" + "        WHERE\n"
				+ "            bc.checklist_step = 0  and bc.CHECKLIST_ORDER = 1 \n" + "        GROUP BY\n"
				+ "            bc.problemname\n" + "        UNION ALL\n" + "        SELECT\n"
				+ "            bc.problemname,\n" + "            0 quantitymoney,\n" + "            0 moneyvalue,\n"
				+ "            COUNT(bc.id) quantitydnqt,\n" + "            SUM(dnqtvalue) dnqtvalue,\n"
				+ "            0 quantityvtnet,\n" + "            0 vtnetvalue,\n" + "            0 quantityaproved,\n"
				+ "            0 aprovedvalue\n" + "        FROM\n" + "            bc\n" + "        WHERE\n"
				+ "            bc.checklist_step = 1 and bc.CHECKLIST_ORDER = 2\n" + "        GROUP BY\n"
				+ "            bc.problemname\n" + "        UNION ALL\n" + "        SELECT\n"
				+ "            bc.problemname,\n" + "            0 quantitymoney,\n" + "            0 moneyvalue,\n"
				+ "            0 quantitydnqt,\n" + "            0 dnqtvalue,\n"
				+ "            COUNT(bc.id) quantityvtnet,\n" + "            SUM(vtnetvalue) vtnetvalue,\n"
				+ "            0 quantityaproved,\n" + "            0 aprovedvalue\n" + "        FROM\n"
				+ "            bc\n" + "        WHERE\n"
				+ "            bc.checklist_step = 2 and bc.CHECKLIST_ORDER = 3\n" + "        GROUP BY\n"
				+ "            bc.problemname\n" + "        UNION ALL\n" + "        SELECT\n"
				+ "            bc.problemname,\n" + "            0 quantitymoney,\n" + "            0 moneyvalue,\n"
				+ "            0 quantitydnqt,\n" + "            0 dnqtvalue,\n" + "            0 quantityvtnet,\n"
				+ "            0 vtnetvalue,\n" + "            COUNT(bc.id) quantityaproved,\n"
				+ "            SUM(aprovedvalue) aprovedvalue\n" + "        FROM\n" + "            bc\n"
				+ "        WHERE\n" + "            bc.checklist_step = 3 and bc.CHECKLIST_ORDER =4\n"
				+ "        GROUP BY\n" + "            bc.problemname\n" + "    )\n" + "  GROUP BY\n"
				+ "    problemname\n" + "ORDER BY\n" + "    problemname";
		sql.append(sqltext);
		sqlQuerySum.append(sqltext);
		sqlQuerySum.append(")");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery querySum = getSession().createSQLQuery(sqlQuerySum.toString());
		if (StringUtils.isNotEmpty(obj.getDateFrom())) {
			query.setParameter("fromDate", obj.getDateFrom());
			querySum.setParameter("fromDate", obj.getDateFrom());
		}
		if (StringUtils.isNotEmpty(obj.getDateTo())) {
			query.setParameter("toDate", obj.getDateTo());
			querySum.setParameter("toDate", obj.getDateTo());
		}
		if (StringUtils.isNotEmpty(obj.getDateFromDNQT())) {
			query.setParameter("fromDateDNQT", obj.getDateFromDNQT());
			querySum.setParameter("fromDateDNQT", obj.getDateFromDNQT());
		}
		if (StringUtils.isNotEmpty(obj.getDateToDNQT())) {
			query.setParameter("toDateDNQT", obj.getDateToDNQT());
			querySum.setParameter("toDateDNQT", obj.getDateToDNQT());
		}
		if (StringUtils.isNotEmpty(obj.getDateFromVTnet())) {
			query.setParameter("fromDateVTnet", obj.getDateFromVTnet());
			querySum.setParameter("fromDateVTnet", obj.getDateFromVTnet());
		}
		if (StringUtils.isNotEmpty(obj.getDateToVTnet())) {
			query.setParameter("toDateVTnet", obj.getDateToVTnet());
			querySum.setParameter("toDateVTnet", obj.getDateToVTnet());
		}
		if (StringUtils.isNotEmpty(obj.getDateFromInvestor())) {
			query.setParameter("fromDateInvestor", obj.getDateFromInvestor());
			querySum.setParameter("fromDateInvestor", obj.getDateFromInvestor());
		}
		if (StringUtils.isNotEmpty(obj.getDateToInvestor())) {
			query.setParameter("toDateInvestor", obj.getDateToInvestor());
			querySum.setParameter("toDateInvestor", obj.getDateToInvestor());
		}
		query.addScalar("problemName", new StringType());
		query.addScalar("valueTotal", new DoubleType());
		query.addScalar("quantityMoney", new LongType());
		query.addScalar("moneyValue", new DoubleType());
		query.addScalar("quantityDnqt", new LongType());
		query.addScalar("dnqtValue", new DoubleType());
		query.addScalar("quantityVtnet", new LongType());
		query.addScalar("vtnetValue", new DoubleType());
		query.addScalar("quantityAproved", new LongType());
		query.addScalar("aprovedValue", new DoubleType());
		// total
		querySum.addScalar("quantityMoneyTotal", new LongType());
		querySum.addScalar("moneyValueTotal", new DoubleType());
		querySum.addScalar("quantityDnqtTotal", new LongType());
		querySum.addScalar("dnqtValueTotal", new DoubleType());
		querySum.addScalar("quantityVtnetTotal", new LongType());
		querySum.addScalar("vtnetValueTotal", new DoubleType());
		querySum.addScalar("quantityAprovedTotal", new LongType());
		querySum.addScalar("aprovedValueTotal", new DoubleType());

		query.setResultTransformer(Transformers.aliasToBean(ReportHSHCQTDTO.class));
		List<ReportHSHCQTDTO> lst = query.list();
		obj.setTotalRecord(lst.size());
		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		List<ReportHSHCQTDTO> lstNew = query.list();
		if (lstNew.size() > 0) {
			List<Object[]> rs = querySum.list();
			for (Object[] objects : rs) {
				lstNew.get(0).setQuantityMoneyTotal((Long) objects[0]);
				lstNew.get(0).setMoneyValueTotal((Double) objects[1]);
				lstNew.get(0).setQuantityDnqtTotal((Long) objects[2]);
				lstNew.get(0).setDnqtValueTotal((Double) objects[3]);
				lstNew.get(0).setQuantityVtnetTotal((Long) objects[4]);
				lstNew.get(0).setVtnetValueTotal((Double) objects[5]);
				lstNew.get(0).setQuantityAprovedTotal((Long) objects[6]);
				lstNew.get(0).setAprovedValueTotal((Double) objects[7]);
			}
		}
		return lstNew;
	}

	public List<ReportHSHCQTDTO> exportFileHSHCStatus(ReportHSHCQTDTO obj) {
		StringBuilder sql = new StringBuilder("     WITH bc AS (   \n" + "  SELECT \tb.id,\n"
				+ "        b.CHECKLIST_ORDER,\n" + "        b.checklist_step,\n" + "        b.has_problem ,\n"
				+ "        b.problemname,\n" + "        b.moneyvalue,\n" + "        b.dnqtvalue,\n"
				+ "        b.vtnetvalue,\n" + "        b.aprovedvalue from (SELECT\n" + "        a.id,\n"
				+ "        a.CHECKLIST_ORDER,\n" + "        a.checklist_step,\n" + "        a.has_problem ,\n"
				+ "        (CASE              \n" + "            when  nvl(a.has_problem,\n"
				+ "            0) >  1 then 'Khác'                \n"
				+ "            else          (CASE                  \n" + "                when  nvl(a.has_problem,\n"
				+ "                0) = 0 then 'Không vướng'                   \n"
				+ "                ELSE   a.problem_name              \n" + "            end )                  \n"
				+ "        end) problemname,\n" + "        a.money_value moneyvalue,\n"
				+ "        SUM(nvl(wcl.dnqt_value,\n" + "        0) ) dnqtvalue,\n"
				+ "        SUM(nvl(wcl.vtnet_sent_value,\n" + "        0) ) vtnetvalue,\n"
				+ "        SUM(nvl(wcl.VTNET_CONFIRM_VALUE,\n" + "        0) ) aprovedvalue ,\n"
				+ "          MAX(DNQT_DATE) DNQT_DATE,\n" + "        MAX(VTNET_SEND_DATE) VTNET_SEND_DATE,\n"
				+ "        MAX(VTNET_CONFIRM_DATE) VTNET_CONFIRM_DATE\n" + "    FROM\n" + "        wo_checklist wcl,\n"
				+ "        (     SELECT\n" + "            DISTINCT          \n" + "            wo.id,\n"
				+ "            wo.checklist_step,\n" + "            wc.CHECKLIST_ORDER,\n"
				+ "            wc.problem_name,\n" + "            wo.money_value ,\n"
				+ "            nvl(wc.has_problem,0)  has_problem                    \n" + "        FROM\n"
				+ "            WO_TYPE wt,\n" + "            wo_checklist wc,\n"
				+ "            wo wo                        \n" + "        WHERE\n" + "            wc.wo_id = wo.id\n"
				+ "            AND wo.wo_type_id= wt.id\n" + "            AND wt.wo_type_code='HCQT'\n"
				+ "            AND wo.status = 1 \n" + "            AND wc.CHECKLIST_ORDER = wo.checklist_step + 1");
		if (StringUtils.isNotEmpty(obj.getDateFrom())) {
			sql.append(" AND wo.CREATED_DATE >= TO_DATE(:fromDate, 'dd/mm/yyyy')");
		}
		if (StringUtils.isNotEmpty(obj.getDateTo())) {
			sql.append(" AND wo.CREATED_DATE < TO_DATE(:toDate, 'dd/mm/yyyy') + 1 ");
		}

		sql.append("   ) a  WHERE\n" + "        wcl.wo_id = a.id                      \n" + "    GROUP BY\n"
				+ "        a.id,\n" + "        a.problem_name,\n" + "        a.has_problem,\n"
				+ "        a.money_value,\n" + "        a.checklist_step,\n"
				+ "        a.CHECKLIST_ORDER  ) b where 1=1");

		if (StringUtils.isNotEmpty(obj.getDateFromDNQT())) {
			sql.append(" AND b.DNQT_DATE >= TO_DATE(:fromDateDNQT, 'dd/mm/yyyy')");
		}
		if (StringUtils.isNotEmpty(obj.getDateToDNQT())) {
			sql.append(" AND b.DNQT_DATE < TO_DATE(:toDateDNQT, 'dd/mm/yyyy') + 1 ");
		}
		if (StringUtils.isNotEmpty(obj.getDateFromVTnet())) {
			sql.append(" AND b.VTNET_SEND_DATE >= TO_DATE(:fromDateVTnet, 'dd/mm/yyyy')");
		}
		if (StringUtils.isNotEmpty(obj.getDateToVTnet())) {
			sql.append(" AND b.VTNET_SEND_DATE < TO_DATE(:toDateVTnet, 'dd/mm/yyyy') + 1 ");
		}
		if (StringUtils.isNotEmpty(obj.getDateFromInvestor())) {
			sql.append(" AND b.VTNET_CONFIRM_DATE >= TO_DATE(:fromDateInvestor, 'dd/mm/yyyy')");
		}
		if (StringUtils.isNotEmpty(obj.getDateToInvestor())) {
			sql.append(" AND b.VTNET_CONFIRM_DATE < TO_DATE(:toDateInvestor, 'dd/mm/yyyy') + 1 ");
		}
		sql.append("  )");
		String sqltext = " SELECT\n"
				+ "    problemName, SUM(moneyvalue) + SUM(dnqtvalue) +  SUM(aprovedvalue) + SUM(vtnetvalue) valueTotal , \n"
				+ "    SUM(quantitymoney) quantityMoney,\n" + "    SUM(moneyvalue) moneyvalue,\n"
				+ "    SUM(quantitydnqt) quantitydnqt,\n" + "    SUM(dnqtvalue) dnqtvalue,\n"
				+ "    SUM(quantityvtnet) quantityvtnet,\n" + "    SUM(vtnetvalue) vtnetvalue,\n"
				+ "    SUM(quantityaproved) quantityaproved,\n" + "    SUM(aprovedvalue) aprovedvalue\n" + "  FROM\n"
				+ "    (\n" + "        SELECT\n" + "            bc.problemname,\n"
				+ "            COUNT(bc.id) quantitymoney,\n" + "            SUM(moneyvalue) moneyvalue,\n"
				+ "            0 quantitydnqt,\n" + "            0 dnqtvalue,\n" + "            0 quantityvtnet,\n"
				+ "            0 vtnetvalue,\n" + "            0 quantityaproved,\n" + "            0 aprovedvalue\n"
				+ "        FROM\n" + "            bc\n" + "        WHERE\n"
				+ "            bc.checklist_step = 0 and bc.CHECKLIST_ORDER = 1  \n" + "        GROUP BY\n"
				+ "            bc.problemname\n" + "        UNION ALL\n" + "        SELECT\n"
				+ "            bc.problemname,\n" + "            0 quantitymoney,\n" + "            0 moneyvalue,\n"
				+ "            COUNT(bc.id) quantitydnqt,\n" + "            SUM(dnqtvalue) dnqtvalue,\n"
				+ "            0 quantityvtnet,\n" + "            0 vtnetvalue,\n" + "            0 quantityaproved,\n"
				+ "            0 aprovedvalue\n" + "        FROM\n" + "            bc\n" + "        WHERE\n"
				+ "            bc.checklist_step = 1 and bc.CHECKLIST_ORDER=  2\n" + "        GROUP BY\n"
				+ "            bc.problemname\n" + "        UNION ALL\n" + "        SELECT\n"
				+ "            bc.problemname,\n" + "            0 quantitymoney,\n" + "            0 moneyvalue,\n"
				+ "            0 quantitydnqt,\n" + "            0 dnqtvalue,\n"
				+ "            COUNT(bc.id) quantityvtnet,\n" + "            SUM(vtnetvalue) vtnetvalue,\n"
				+ "            0 quantityaproved,\n" + "            0 aprovedvalue\n" + "        FROM\n"
				+ "            bc\n" + "        WHERE\n"
				+ "            bc.checklist_step = 2 and bc.CHECKLIST_ORDER=  3\n" + "        GROUP BY\n"
				+ "            bc.problemname\n" + "        UNION ALL\n" + "        SELECT\n"
				+ "            bc.problemname,\n" + "            0 quantitymoney,\n" + "            0 moneyvalue,\n"
				+ "            0 quantitydnqt,\n" + "            0 dnqtvalue,\n" + "            0 quantityvtnet,\n"
				+ "            0 vtnetvalue,\n" + "            COUNT(bc.id) quantityaproved,\n"
				+ "            SUM(aprovedvalue) aprovedvalue\n" + "        FROM\n" + "            bc\n"
				+ "        WHERE\n" + "            bc.checklist_step = 3 and bc.CHECKLIST_ORDER=  4\n"
				+ "        GROUP BY\n" + "            bc.problemname\n" + "    )\n" + "  GROUP BY\n"
				+ "    problemname\n" + "ORDER BY\n" + "    problemname";
		sql.append(sqltext);
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		if (StringUtils.isNotEmpty(obj.getDateFrom())) {
			query.setParameter("fromDate", obj.getDateFrom());
		}
		if (StringUtils.isNotEmpty(obj.getDateTo())) {
			query.setParameter("toDate", obj.getDateTo());
		}
		if (StringUtils.isNotEmpty(obj.getDateFromDNQT())) {
			query.setParameter("fromDateDNQT", obj.getDateFromDNQT());
		}
		if (StringUtils.isNotEmpty(obj.getDateToDNQT())) {
			query.setParameter("toDateDNQT", obj.getDateToDNQT());
		}
		if (StringUtils.isNotEmpty(obj.getDateFromVTnet())) {
			query.setParameter("fromDateVTnet", obj.getDateFromVTnet());
		}
		if (StringUtils.isNotEmpty(obj.getDateToVTnet())) {
			query.setParameter("toDateVTnet", obj.getDateToVTnet());
		}
		if (StringUtils.isNotEmpty(obj.getDateFromInvestor())) {
			query.setParameter("fromDateInvestor", obj.getDateFromInvestor());
		}
		if (StringUtils.isNotEmpty(obj.getDateToInvestor())) {
			query.setParameter("toDateInvestor", obj.getDateToInvestor());
		}
		query.addScalar("problemName", new StringType());
		query.addScalar("valueTotal", new DoubleType());
		query.addScalar("quantityMoney", new LongType());
		query.addScalar("moneyValue", new DoubleType());
		query.addScalar("quantityDnqt", new LongType());
		query.addScalar("dnqtValue", new DoubleType());
		query.addScalar("quantityVtnet", new LongType());
		query.addScalar("vtnetValue", new DoubleType());
		query.addScalar("quantityAproved", new LongType());
		query.addScalar("aprovedValue", new DoubleType());
		query.setResultTransformer(Transformers.aliasToBean(ReportHSHCQTDTO.class));
		List<ReportHSHCQTDTO> lst = query.list();
		return lst;
	}

	public List<ReportHSHCQTDTO> doSearchReportHSHCProject(ReportHSHCQTDTO obj) {
		StringBuilder sqlQuerySum = new StringBuilder();
		StringBuilder sql = new StringBuilder("WITH bc AS ( \n" + "        SELECT     b.id,\n"
				+ "        b.checklist_step,\n" + "        b.CHECKLIST_ORDER,\n" + "        b.problemname,\n"
				+ "        b.moneyvalue,\n" + "        b.dnqtvalue,\n" + "        b.vtnetvalue,\n"
				+ "        b.aprovedvalue  from (\n" + "       SELECT\n" + "        a.id,\n"
				+ "        a.checklist_step,\n" + "        a.CHECKLIST_ORDER,\n"
				+ "        a.problemname problemname,\n" + "        a.money_value moneyvalue,\n"
				+ "        SUM(nvl(wc.dnqt_value,0) ) dnqtvalue,\n"
				+ "        SUM(nvl(wc.vtnet_sent_value,0) ) vtnetvalue,\n"
				+ "        SUM(nvl(wc.VTNET_CONFIRM_VALUE, 0) ) aprovedvalue ,\n"
				+ "        MAX(wc.DNQT_DATE) DNQT_DATE,\n" + "        MAX(wc.VTNET_SEND_DATE) VTNET_SEND_DATE,\n"
				+ "        MAX(wc.VTNET_CONFIRM_DATE) VTNET_CONFIRM_DATE\n" + "    FROM\n"
				+ "        wo_checklist wc,\n" + "        (     \n" + "  SELECT\n" + "            DISTINCT  \n"
				+ "         w.id,\n" + "            wp.HCQT_PROJECT_NAME problemname,\n"
				+ "            w.checklist_step,\n" + "            wc.CHECKLIST_ORDER,\n"
				+ "            nvl(wc.has_problem,0)  has_problem, \n"
				+ "            w.money_value                                    \n" + "        FROM\n"
				+ "      WO_TYPE wt,\n" + "            WO_HCQT_PROJECT wp,\n" + "            wo_checklist wc,\n"
				+ "            wo w                                      \n" + "        WHERE\n"
				+ "            wc.wo_id = w.id  \n" + "            AND w.wo_type_id= wt.id\n"
				+ "            AND wt.wo_type_code='HCQT' \n" + "            AND w.status = 1\n"
				+ "            AND wc.CHECKLIST_ORDER = w.checklist_step + 1  \n"
				+ "            AND wp.HCQT_PROJECT_ID=w.HCQT_PROJECT_ID  ");
		if (StringUtils.isNotEmpty(obj.getDateFrom())) {
			sql.append(" AND w.CREATED_DATE >= TO_DATE(:fromDate, 'dd/mm/yyyy')");
		}
		if (StringUtils.isNotEmpty(obj.getDateTo())) {
			sql.append(" AND w.CREATED_DATE < TO_DATE(:toDate, 'dd/mm/yyyy') + 1 ");
		}

		sql.append("   ) a where  wc.wo_id = a.id  \n" + "    GROUP BY\n" + "        a.id,\n"
				+ "        a.problemname,\n" + "        a.money_value,\n" + "        a.checklist_step,\n"
				+ "        a.CHECKLIST_ORDER ) b where 1=1  ");
		if (StringUtils.isNotEmpty(obj.getDateFromDNQT())) {
			sql.append(" AND b.DNQT_DATE >= TO_DATE(:fromDateDNQT, 'dd/mm/yyyy')");
		}
		if (StringUtils.isNotEmpty(obj.getDateToDNQT())) {
			sql.append(" AND b.DNQT_DATE < TO_DATE(:toDateDNQT, 'dd/mm/yyyy') + 1 ");
		}
		if (StringUtils.isNotEmpty(obj.getDateFromVTnet())) {
			sql.append(" AND b.VTNET_SEND_DATE >= TO_DATE(:fromDateVTnet, 'dd/mm/yyyy')");
		}
		if (StringUtils.isNotEmpty(obj.getDateToVTnet())) {
			sql.append(" AND b.VTNET_SEND_DATE < TO_DATE(:toDateVTnet, 'dd/mm/yyyy') + 1 ");
		}
		if (StringUtils.isNotEmpty(obj.getDateFromInvestor())) {
			sql.append(" AND b.VTNET_CONFIRM_DATE >= TO_DATE(:fromDateInvestor, 'dd/mm/yyyy')");
		}
		if (StringUtils.isNotEmpty(obj.getDateToInvestor())) {
			sql.append(" AND b.VTNET_CONFIRM_DATE < TO_DATE(:toDateInvestor, 'dd/mm/yyyy') + 1 ");
		}
		sql.append("  ) ");
		sqlQuerySum.append(sql);
		sqlQuerySum.append("SELECT    SUM(quantitymoney) quantityMoneyTotal,\n"
				+ "                    SUM(moneyvalue) moneyvalueTotal,\n"
				+ "                    SUM(quantitydnqt) quantitydnqtTotal,\n"
				+ "                    SUM(dnqtvalue) dnqtvalueTotal,\n"
				+ "                    SUM(quantityvtnet) quantityvtnetTotal,\n"
				+ "                    SUM(vtnetvalue) vtnetvalueTotal,\n"
				+ "                    SUM(quantityaproved) quantityaprovedTotal,\n"
				+ "                    SUM(aprovedvalue) aprovedvalueTotal FROM (");
		String sqltext = " SELECT\n"
				+ "    problemName, SUM(moneyvalue) + SUM(dnqtvalue) +  SUM(aprovedvalue) + SUM(vtnetvalue) valueTotal , \n"
				+ "    SUM(quantitymoney) quantityMoney,\n" + "    SUM(moneyvalue) moneyvalue,\n"
				+ "    SUM(quantitydnqt) quantitydnqt,\n" + "    SUM(dnqtvalue) dnqtvalue,\n"
				+ "    SUM(quantityvtnet) quantityvtnet,\n" + "    SUM(vtnetvalue) vtnetvalue,\n"
				+ "    SUM(quantityaproved) quantityaproved,\n" + "    SUM(aprovedvalue) aprovedvalue\n" + "  FROM\n"
				+ "    (\n" + "        SELECT\n" + "            bc.problemname,\n"
				+ "            COUNT(bc.id) quantitymoney,\n" + "            SUM(moneyvalue) moneyvalue,\n"
				+ "            0 quantitydnqt,\n" + "            0 dnqtvalue,\n" + "            0 quantityvtnet,\n"
				+ "            0 vtnetvalue,\n" + "            0 quantityaproved,\n" + "            0 aprovedvalue\n"
				+ "        FROM\n" + "            bc\n" + "        WHERE\n"
				+ "            bc.checklist_step = 0 and bc.CHECKLIST_ORDER = 1\n" + "        GROUP BY\n"
				+ "            bc.problemname\n" + "        UNION ALL\n" + "        SELECT\n"
				+ "            bc.problemname,\n" + "            0 quantitymoney,\n" + "            0 moneyvalue,\n"
				+ "            COUNT(bc.id) quantitydnqt,\n" + "            SUM(dnqtvalue) dnqtvalue,\n"
				+ "            0 quantityvtnet,\n" + "            0 vtnetvalue,\n" + "            0 quantityaproved,\n"
				+ "            0 aprovedvalue\n" + "        FROM\n" + "            bc\n" + "        WHERE\n"
				+ "            bc.checklist_step = 1 AND 2 = bc.CHECKLIST_ORDER\n" + "        GROUP BY\n"
				+ "            bc.problemname\n" + "        UNION ALL\n" + "        SELECT\n"
				+ "            bc.problemname,\n" + "            0 quantitymoney,\n" + "            0 moneyvalue,\n"
				+ "            0 quantitydnqt,\n" + "            0 dnqtvalue,\n"
				+ "            COUNT(bc.id) quantityvtnet,\n" + "            SUM(vtnetvalue) vtnetvalue,\n"
				+ "            0 quantityaproved,\n" + "            0 aprovedvalue\n" + "        FROM\n"
				+ "            bc\n" + "        WHERE\n"
				+ "            bc.checklist_step = 2 AND 3 = bc.CHECKLIST_ORDER\n" + "        GROUP BY\n"
				+ "            bc.problemname\n" + "        UNION ALL\n" + "        SELECT\n"
				+ "            bc.problemname,\n" + "            0 quantitymoney,\n" + "            0 moneyvalue,\n"
				+ "            0 quantitydnqt,\n" + "            0 dnqtvalue,\n" + "            0 quantityvtnet,\n"
				+ "            0 vtnetvalue,\n" + "            COUNT(bc.id) quantityaproved,\n"
				+ "            SUM(aprovedvalue) aprovedvalue\n" + "        FROM\n" + "            bc\n"
				+ "        WHERE\n" + "            bc.checklist_step = 3 AND 4 = bc.CHECKLIST_ORDER\n"
				+ "        GROUP BY\n" + "            bc.problemname\n" + "    )\n" + "  GROUP BY\n"
				+ "    problemname\n" + "ORDER BY\n" + "    problemname";
		sql.append(sqltext);
		sqlQuerySum.append(sqltext);
		sqlQuerySum.append(")");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery querySum = getSession().createSQLQuery(sqlQuerySum.toString());
		if (StringUtils.isNotEmpty(obj.getDateFrom())) {
			query.setParameter("fromDate", obj.getDateFrom());
			querySum.setParameter("fromDate", obj.getDateFrom());
		}
		if (StringUtils.isNotEmpty(obj.getDateTo())) {
			query.setParameter("toDate", obj.getDateTo());
			querySum.setParameter("toDate", obj.getDateTo());
		}
		if (StringUtils.isNotEmpty(obj.getDateFromDNQT())) {
			query.setParameter("fromDateDNQT", obj.getDateFromDNQT());
			querySum.setParameter("fromDateDNQT", obj.getDateFromDNQT());
		}
		if (StringUtils.isNotEmpty(obj.getDateToDNQT())) {
			query.setParameter("toDateDNQT", obj.getDateToDNQT());
			querySum.setParameter("toDateDNQT", obj.getDateToDNQT());
		}
		if (StringUtils.isNotEmpty(obj.getDateFromVTnet())) {
			query.setParameter("fromDateVTnet", obj.getDateFromVTnet());
			querySum.setParameter("fromDateVTnet", obj.getDateFromVTnet());
		}
		if (StringUtils.isNotEmpty(obj.getDateToVTnet())) {
			query.setParameter("toDateVTnet", obj.getDateToVTnet());
			querySum.setParameter("toDateVTnet", obj.getDateToVTnet());
		}
		if (StringUtils.isNotEmpty(obj.getDateFromInvestor())) {
			query.setParameter("fromDateInvestor", obj.getDateFromInvestor());
			querySum.setParameter("fromDateInvestor", obj.getDateFromInvestor());
		}
		if (StringUtils.isNotEmpty(obj.getDateToInvestor())) {
			query.setParameter("toDateInvestor", obj.getDateToInvestor());
			querySum.setParameter("toDateInvestor", obj.getDateToInvestor());
		}
		query.addScalar("problemName", new StringType());
		query.addScalar("valueTotal", new DoubleType());
		query.addScalar("quantityMoney", new LongType());
		query.addScalar("moneyValue", new DoubleType());
		query.addScalar("quantityDnqt", new LongType());
		query.addScalar("dnqtValue", new DoubleType());
		query.addScalar("quantityVtnet", new LongType());
		query.addScalar("vtnetValue", new DoubleType());
		query.addScalar("quantityAproved", new LongType());
		query.addScalar("aprovedValue", new DoubleType());
		// total
		querySum.addScalar("quantityMoneyTotal", new LongType());
		querySum.addScalar("moneyValueTotal", new DoubleType());
		querySum.addScalar("quantityDnqtTotal", new LongType());
		querySum.addScalar("dnqtValueTotal", new DoubleType());
		querySum.addScalar("quantityVtnetTotal", new LongType());
		querySum.addScalar("vtnetValueTotal", new DoubleType());
		querySum.addScalar("quantityAprovedTotal", new LongType());
		querySum.addScalar("aprovedValueTotal", new DoubleType());

		query.setResultTransformer(Transformers.aliasToBean(ReportHSHCQTDTO.class));
		List<ReportHSHCQTDTO> lst = query.list();
		obj.setTotalRecord(lst.size());
		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		List<ReportHSHCQTDTO> lstNew = query.list();
		if (lstNew.size() > 0) {
			List<Object[]> rs = querySum.list();
			for (Object[] objects : rs) {
				lstNew.get(0).setQuantityMoneyTotal((Long) objects[0]);
				lstNew.get(0).setMoneyValueTotal((Double) objects[1]);
				lstNew.get(0).setQuantityDnqtTotal((Long) objects[2]);
				lstNew.get(0).setDnqtValueTotal((Double) objects[3]);
				lstNew.get(0).setQuantityVtnetTotal((Long) objects[4]);
				lstNew.get(0).setVtnetValueTotal((Double) objects[5]);
				lstNew.get(0).setQuantityAprovedTotal((Long) objects[6]);
				lstNew.get(0).setAprovedValueTotal((Double) objects[7]);
			}
		}
		return lstNew;
	}

	public List<ReportHSHCQTDTO> exportFileHSHCProject(ReportHSHCQTDTO obj) {
		StringBuilder sql = new StringBuilder("WITH bc AS ( \n" + "        SELECT     b.id,\n"
				+ "        b.checklist_step,\n" + "        b.CHECKLIST_ORDER,\n" + "        b.problemname,\n"
				+ "        b.moneyvalue,\n" + "        b.dnqtvalue,\n" + "        b.vtnetvalue,\n"
				+ "        b.aprovedvalue  from (\n" + "       SELECT\n" + "        a.id,\n"
				+ "        a.checklist_step,\n" + "        a.CHECKLIST_ORDER,\n"
				+ "        a.problemname problemname,\n" + "        a.money_value moneyvalue,\n"
				+ "        SUM(nvl(wc.dnqt_value,0) ) dnqtvalue,\n"
				+ "        SUM(nvl(wc.vtnet_sent_value,0) ) vtnetvalue,\n"
				+ "        SUM(nvl(wc.VTNET_CONFIRM_VALUE, 0) ) aprovedvalue ,\n"
				+ "        MAX(wc.DNQT_DATE) DNQT_DATE,\n" + "        MAX(wc.VTNET_SEND_DATE) VTNET_SEND_DATE,\n"
				+ "        MAX(wc.VTNET_CONFIRM_DATE) VTNET_CONFIRM_DATE\n" + "    FROM\n"
				+ "        wo_checklist wc,\n" + "        (     \n" + "  SELECT\n" + "            DISTINCT  \n"
				+ "         w.id,\n" + "            wp.HCQT_PROJECT_NAME problemname,\n"
				+ "            w.checklist_step,\n" + "            wc.CHECKLIST_ORDER,\n"
				+ "            nvl(wc.has_problem,0)  has_problem, \n"
				+ "            w.money_value                                    \n" + "        FROM\n"
				+ "      WO_TYPE wt,\n" + "            WO_HCQT_PROJECT wp,\n" + "            wo_checklist wc,\n"
				+ "            wo w                                      \n" + "        WHERE\n"
				+ "            wc.wo_id = w.id  \n" + "            AND w.wo_type_id= wt.id\n"
				+ "            AND wt.wo_type_code='HCQT' \n" + "            AND w.status = 1\n"
				+ "            AND wc.CHECKLIST_ORDER = w.checklist_step + 1  \n"
				+ "            AND wp.HCQT_PROJECT_ID=w.HCQT_PROJECT_ID  ");
		if (StringUtils.isNotEmpty(obj.getDateFrom())) {
			sql.append(" AND w.CREATED_DATE >= TO_DATE(:fromDate, 'dd/mm/yyyy')");
		}
		if (StringUtils.isNotEmpty(obj.getDateTo())) {
			sql.append(" AND w.CREATED_DATE < TO_DATE(:toDate, 'dd/mm/yyyy') + 1 ");
		}

		sql.append("   ) a where  wc.wo_id = a.id  \n" + "    GROUP BY\n" + "        a.id,\n"
				+ "        a.problemname,\n" + "        a.money_value,\n" + "        a.checklist_step,\n"
				+ "        a.CHECKLIST_ORDER ) b where 1=1  ");
		if (StringUtils.isNotEmpty(obj.getDateFromDNQT())) {
			sql.append(" AND b.DNQT_DATE >= TO_DATE(:fromDateDNQT, 'dd/mm/yyyy')");
		}
		if (StringUtils.isNotEmpty(obj.getDateToDNQT())) {
			sql.append(" AND b.DNQT_DATE < TO_DATE(:toDateDNQT, 'dd/mm/yyyy') + 1 ");
		}
		if (StringUtils.isNotEmpty(obj.getDateFromVTnet())) {
			sql.append(" AND b.VTNET_SEND_DATE >= TO_DATE(:fromDateVTnet, 'dd/mm/yyyy')");
		}
		if (StringUtils.isNotEmpty(obj.getDateToVTnet())) {
			sql.append(" AND b.VTNET_SEND_DATE < TO_DATE(:toDateVTnet, 'dd/mm/yyyy') + 1 ");
		}
		if (StringUtils.isNotEmpty(obj.getDateFromInvestor())) {
			sql.append(" AND b.VTNET_CONFIRM_DATE >= TO_DATE(:fromDateInvestor, 'dd/mm/yyyy')");
		}
		if (StringUtils.isNotEmpty(obj.getDateToInvestor())) {
			sql.append(" AND b.VTNET_CONFIRM_DATE < TO_DATE(:toDateInvestor, 'dd/mm/yyyy') + 1 ");
		}

		sql.append(" )  SELECT\n"
				+ "    problemName, SUM(moneyvalue) + SUM(dnqtvalue) +  SUM(aprovedvalue) + SUM(vtnetvalue) valueTotal , \n"
				+ "    SUM(quantitymoney) quantityMoney,\n" + "    SUM(moneyvalue) moneyvalue,\n"
				+ "    SUM(quantitydnqt) quantitydnqt,\n" + "    SUM(dnqtvalue) dnqtvalue,\n"
				+ "    SUM(quantityvtnet) quantityvtnet,\n" + "    SUM(vtnetvalue) vtnetvalue,\n"
				+ "    SUM(quantityaproved) quantityaproved,\n" + "    SUM(aprovedvalue) aprovedvalue\n" + "  FROM\n"
				+ "    (\n" + "        SELECT\n" + "            bc.problemname,\n"
				+ "            COUNT(bc.id) quantitymoney,\n" + "            SUM(moneyvalue) moneyvalue,\n"
				+ "            0 quantitydnqt,\n" + "            0 dnqtvalue,\n" + "            0 quantityvtnet,\n"
				+ "            0 vtnetvalue,\n" + "            0 quantityaproved,\n" + "            0 aprovedvalue\n"
				+ "        FROM\n" + "            bc\n" + "        WHERE\n"
				+ "            bc.checklist_step = 0 AND  bc.CHECKLIST_ORDER = 1\n" + "        GROUP BY\n"
				+ "            bc.problemname\n" + "        UNION ALL\n" + "        SELECT\n"
				+ "            bc.problemname,\n" + "            0 quantitymoney,\n" + "            0 moneyvalue,\n"
				+ "            COUNT(bc.id) quantitydnqt,\n" + "            SUM(dnqtvalue) dnqtvalue,\n"
				+ "            0 quantityvtnet,\n" + "            0 vtnetvalue,\n" + "            0 quantityaproved,\n"
				+ "            0 aprovedvalue\n" + "        FROM\n" + "            bc\n" + "        WHERE\n"
				+ "            bc.checklist_step = 1  AND 2 = bc.CHECKLIST_ORDER\n" + "        GROUP BY\n"
				+ "            bc.problemname\n" + "        UNION ALL\n" + "        SELECT\n"
				+ "            bc.problemname,\n" + "            0 quantitymoney,\n" + "            0 moneyvalue,\n"
				+ "            0 quantitydnqt,\n" + "            0 dnqtvalue,\n"
				+ "            COUNT(bc.id) quantityvtnet,\n" + "            SUM(vtnetvalue) vtnetvalue,\n"
				+ "            0 quantityaproved,\n" + "            0 aprovedvalue\n" + "        FROM\n"
				+ "            bc\n" + "        WHERE\n"
				+ "            bc.checklist_step = 2 AND 3 = bc.CHECKLIST_ORDER\n" + "        GROUP BY\n"
				+ "            bc.problemname\n" + "        UNION ALL\n" + "        SELECT\n"
				+ "            bc.problemname,\n" + "            0 quantitymoney,\n" + "            0 moneyvalue,\n"
				+ "            0 quantitydnqt,\n" + "            0 dnqtvalue,\n" + "            0 quantityvtnet,\n"
				+ "            0 vtnetvalue,\n" + "            COUNT(bc.id) quantityaproved,\n"
				+ "            SUM(aprovedvalue) aprovedvalue\n" + "        FROM\n" + "            bc\n"
				+ "        WHERE\n" + "            bc.checklist_step = 3 AND bc.CHECKLIST_ORDER = 4\n"
				+ "        GROUP BY\n" + "            bc.problemname\n" + "    )\n" + "  GROUP BY\n"
				+ "    problemname\n" + "ORDER BY\n" + "    problemname");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		if (StringUtils.isNotEmpty(obj.getDateFrom())) {
			query.setParameter("fromDate", obj.getDateFrom());
		}
		if (StringUtils.isNotEmpty(obj.getDateTo())) {
			query.setParameter("toDate", obj.getDateTo());
		}
		if (StringUtils.isNotEmpty(obj.getDateFromDNQT())) {
			query.setParameter("fromDateDNQT", obj.getDateFromDNQT());
		}
		if (StringUtils.isNotEmpty(obj.getDateToDNQT())) {
			query.setParameter("toDateDNQT", obj.getDateToDNQT());
		}
		if (StringUtils.isNotEmpty(obj.getDateFromVTnet())) {
			query.setParameter("fromDateVTnet", obj.getDateFromVTnet());
		}
		if (StringUtils.isNotEmpty(obj.getDateToVTnet())) {
			query.setParameter("toDateVTnet", obj.getDateToVTnet());
		}
		if (StringUtils.isNotEmpty(obj.getDateFromInvestor())) {
			query.setParameter("fromDateInvestor", obj.getDateFromInvestor());
		}
		if (StringUtils.isNotEmpty(obj.getDateToInvestor())) {
			query.setParameter("toDateInvestor", obj.getDateToInvestor());
		}
		query.addScalar("problemName", new StringType());
		query.addScalar("valueTotal", new DoubleType());
		query.addScalar("quantityMoney", new LongType());
		query.addScalar("moneyValue", new DoubleType());
		query.addScalar("quantityDnqt", new LongType());
		query.addScalar("dnqtValue", new DoubleType());
		query.addScalar("quantityVtnet", new LongType());
		query.addScalar("vtnetValue", new DoubleType());
		query.addScalar("quantityAproved", new LongType());
		query.addScalar("aprovedValue", new DoubleType());
		query.setResultTransformer(Transformers.aliasToBean(ReportHSHCQTDTO.class));
		List<ReportHSHCQTDTO> lst = query.list();
		return lst;
	}

	public List<WoHcqtFtReportDTO> genHcqtFtReport(WoHcqtFtReportDTO obj) {
		StringBuilder sqlQuerySum = new StringBuilder();
		StringBuilder sql = new StringBuilder(
				"WITH bc AS (     SELECT\n" + "        w.id,\n" + "        w.checklist_step,\n"
						+ "        w.FT_NAME ftname,\n" + "        w.money_value totalValueByPlan,\n"
						+ "        SUM(nvl(wc.dnqt_value,\n" + "        0) ) totalDnqtValue,\n"
						+ "        SUM(nvl(wc.vtnet_sent_value,\n" + "        0) ) totalSendVtnetValue,\n"
						+ "        SUM(nvl(wc.VTNET_CONFIRM_VALUE,\n" + "        0) ) totalVtnetConfirmedValue ,\n"
						+ "        SUM(nvl(wc.APROVE_DOC_VALUE,\n" + "        0) ) totalApprovedValue   \t\t\n"
						+ "    FROM\n" + "        wo_checklist wc,\n" + "        wo w     \n" + "    WHERE\n"
						+ "        wc.wo_id = w.id           \n" + "        AND w.status = 1  ");
		if (StringUtils.isNotEmpty(obj.getDateFrom())) {
			sql.append(" AND w.CREATED_DATE >= TO_DATE(:fromDate, 'dd/mm/yyyy')");
		}
		if (StringUtils.isNotEmpty(obj.getDateTo())) {
			sql.append(" AND w.CREATED_DATE < TO_DATE(:toDate, 'dd/mm/yyyy') + 1 ");
		}
		if (StringUtils.isNotEmpty(obj.getDateFromDNQT())) {
			sql.append(" AND wc.DNQT_DATE >= TO_DATE(:fromDateDNQT, 'dd/mm/yyyy')");
		}
		if (StringUtils.isNotEmpty(obj.getDateToDNQT())) {
			sql.append(" AND wc.DNQT_DATE < TO_DATE(:toDateDNQT, 'dd/mm/yyyy') + 1 ");
		}
		if (StringUtils.isNotEmpty(obj.getDateFromVTnet())) {
			sql.append(" AND wc.VTNET_SEND_DATE >= TO_DATE(:fromDateVTnet, 'dd/mm/yyyy')");
		}
		if (StringUtils.isNotEmpty(obj.getDateToVTnet())) {
			sql.append(" AND wc.VTNET_SEND_DATE < TO_DATE(:toDateVTnet, 'dd/mm/yyyy') + 1 ");
		}
		if (StringUtils.isNotEmpty(obj.getDateFromInvestor())) {
			sql.append(" AND wc.VTNET_CONFIRM_DATE >= TO_DATE(:fromDateInvestor, 'dd/mm/yyyy')");
		}
		if (StringUtils.isNotEmpty(obj.getDateToInvestor())) {
			sql.append(" AND wc.VTNET_CONFIRM_DATE < TO_DATE(:toDateInvestor, 'dd/mm/yyyy') + 1 ");
		}
		sql.append("   GROUP BY\n" + "        w.id,\n" + "        w.FT_NAME,\n" + "        w.money_value,\n"
				+ "        w.checklist_step )");
		sqlQuerySum.append(sql);
		sqlQuerySum.append("  SELECT\n" + "            SUM(totalQuantityByPlan) quantityByPlanTotal,\n"
				+ "            SUM(totalValueByPlan) valueByPlanTotal,\n"
				+ "            SUM(totalHasDnqtQuantity) hasDnqtQuantityTotal,\n"
				+ "            SUM(totalDnqtValue) dnqtValueTotal,\n"
				+ "            SUM(totalSendVtnetQuantity) sendVtnetQuantityTotal,\n"
				+ "            SUM(totalSendVtnetValue) sendVtnetValueTotal,\n"
				+ "            SUM(totalVtnetConfirmedQuantity) vtnetConfirmedQuantityTotal,\n"
				+ "            SUM(totalVtnetConfirmedValue) vtnetConfirmedValueTotal,\n"
				+ "            SUM(totalApprovedQuantity) approvedQuantityTotal,\n"
				+ "            SUM(totalApprovedValue) approvedValueTotal\n" + "          from (");
		String sqltext = "  SELECT\n" + "            ftname,\n"
				+ "            SUM(totalQuantityByPlan) totalQuantityByPlan,\n"
				+ "            SUM(totalValueByPlan) totalValueByPlan,\n"
				+ "            SUM(totalHasDnqtQuantity) totalHasDnqtQuantity,\n"
				+ "            SUM(totalDnqtValue) totalDnqtValue,\n"
				+ "            SUM(totalSendVtnetQuantity) totalSendVtnetQuantity,\n"
				+ "            SUM(totalSendVtnetValue) totalSendVtnetValue,\n"
				+ "            SUM(totalVtnetConfirmedQuantity) totalVtnetConfirmedQuantity,\n"
				+ "            SUM(totalVtnetConfirmedValue) totalVtnetConfirmedValue,\n"
				+ "            SUM(totalApprovedQuantity) totalApprovedQuantity,\n"
				+ "            SUM(totalApprovedValue) totalApprovedValue ,CASE WHEN  nvl(SUM(totalQuantityByPlan),0) > 0 THEN\n"
				+ "        nvl(ROUND(nvl(SUM(totalHasDnqtQuantity),0)*100/nvl(SUM(totalQuantityByPlan),0),2),0) ELSE 0 END dnqtPercent,\n"
				+ "        CASE WHEN  nvl(SUM(totalSendVtnetQuantity),0) > 0 THEN\n"
				+ "        nvl(ROUND(nvl(SUM(totalVtnetConfirmedQuantity),0)*100/nvl(SUM(totalSendVtnetQuantity),0),2),0)  ELSE 0 END vtnetPercent,\n"
				+ "        CASE WHEN  nvl(SUM(totalQuantityByPlan),0) > 0 THEN\n"
				+ "         nvl(ROUND(nvl(SUM(totalApprovedQuantity),0)*100/nvl(SUM(totalQuantityByPlan),0),2),0)  ELSE 0 END approvedPercent \n"
				+ "        FROM\n" + "            (         SELECT\n" + "                bc.ftname,\n"
				+ "                COUNT(bc.id) totalQuantityByPlan,\n"
				+ "                SUM(totalValueByPlan) totalValueByPlan,\n"
				+ "                0 totalHasDnqtQuantity,\n" + "                0 totalDnqtValue,\n"
				+ "                0 totalSendVtnetQuantity,\n" + "                0 totalSendVtnetValue,\n"
				+ "                0 totalVtnetConfirmedQuantity,\n" + "                0 totalVtnetConfirmedValue,\n"
				+ "     0 totalApprovedQuantity,\n" + "                0 totalApprovedValue \n" + "            FROM\n"
				+ "                bc         \n" + "            WHERE\n"
				+ "                bc.checklist_step >= 0         \n" + "            GROUP BY\n"
				+ "                bc.ftname         \n" + "            UNION\n" + "            ALL         SELECT\n"
				+ "                bc.ftname,\n" + "                0 totalQuantityByPlan,\n"
				+ "                0 totalValueByPlan,\n" + "                COUNT(bc.id) totalHasDnqtQuantity,\n"
				+ "                SUM(totalDnqtValue) totalDnqtValue,\n"
				+ "                0 totalSendVtnetQuantity,\n" + "                0 totalSendVtnetValue,\n"
				+ "                0 totalVtnetConfirmedQuantity,\n" + "                0 totalVtnetConfirmedValue,\n"
				+ "                0 totalApprovedQuantity,\n" + "                0 totalApprovedValue \n"
				+ "            FROM\n" + "                bc         \n" + "            WHERE\n"
				+ "                bc.checklist_step >= 1         \n" + "            GROUP BY\n"
				+ "                bc.ftname         \n" + "            UNION\n" + "            ALL         SELECT\n"
				+ "                bc.ftname,\n" + "                0 totalQuantityByPlan,\n"
				+ "                0 totalValueByPlan,\n" + "                0 totalHasDnqtQuantity,\n"
				+ "                0 totalDnqtValue,\n" + "                COUNT(bc.id) totalSendVtnetQuantity,\n"
				+ "                SUM(totalSendVtnetValue) totalSendVtnetValue,\n"
				+ "                0 totalVtnetConfirmedQuantity,\n" + "                0 totalVtnetConfirmedValue,\n"
				+ "                0 totalApprovedQuantity,\n" + "                0 totalApprovedValue \n"
				+ "            FROM\n" + "                bc         \n" + "            WHERE\n"
				+ "                bc.checklist_step >= 2         \n" + "            GROUP BY\n"
				+ "                bc.ftname         \n" + "            UNION\n" + "            ALL         SELECT\n"
				+ "                bc.ftname,\n" + "                0 totalQuantityByPlan,\n"
				+ "                0 totalValueByPlan,\n" + "                0 totalHasDnqtQuantity,\n"
				+ "                0 totalDnqtValue,\n" + "                0 totalSendVtnetQuantity,\n"
				+ "                0 totalSendVtnetValue,\n"
				+ "                COUNT(bc.id) totalVtnetConfirmedQuantity,\n"
				+ "                SUM(totalVtnetConfirmedValue) totalVtnetConfirmedValue,\n"
				+ "                0 totalApprovedQuantity,\n" + "                0 totalApprovedValue \n"
				+ "            FROM\n" + "                bc         \n" + "            WHERE\n"
				+ "                bc.checklist_step >= 3         \n" + "            GROUP BY\n"
				+ "                bc.ftname \n" + "             UNION\n" + "            ALL         SELECT\n"
				+ "                bc.ftname,\n" + "                0 totalQuantityByPlan,\n"
				+ "                0 totalValueByPlan,\n" + "                0 totalHasDnqtQuantity,\n"
				+ "                0 totalDnqtValue,\n" + "                0 totalSendVtnetQuantity,\n"
				+ "                0 totalSendVtnetValue,\n" + "                0 totalVtnetConfirmedQuantity,\n"
				+ "                0 totalVtnetConfirmedValue,\n"
				+ "                COUNT(bc.id) totalApprovedQuantity,\n"
				+ "                SUM(totalApprovedValue) totalApprovedValue         \n" + "            FROM\n"
				+ "                bc         \n" + "            WHERE\n"
				+ "                bc.checklist_step >= 4         \n" + "            GROUP BY\n"
				+ "                bc.ftname \n" + "        )   \n" + "    GROUP BY\n" + "        ftname \n"
				+ "    ORDER BY\n" + "        ftname";
		sql.append(sqltext);
		sqlQuerySum.append(sqltext);
		sqlQuerySum.append(")");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery querySum = getSession().createSQLQuery(sqlQuerySum.toString());
		if (StringUtils.isNotEmpty(obj.getDateFrom())) {
			query.setParameter("fromDate", obj.getDateFrom());
			querySum.setParameter("fromDate", obj.getDateFrom());
		}
		if (StringUtils.isNotEmpty(obj.getDateTo())) {
			query.setParameter("toDate", obj.getDateTo());
			querySum.setParameter("toDate", obj.getDateTo());
		}
		if (StringUtils.isNotEmpty(obj.getDateFromDNQT())) {
			query.setParameter("fromDateDNQT", obj.getDateFromDNQT());
			querySum.setParameter("fromDateDNQT", obj.getDateFromDNQT());
		}
		if (StringUtils.isNotEmpty(obj.getDateToDNQT())) {
			query.setParameter("toDateDNQT", obj.getDateToDNQT());
			querySum.setParameter("toDateDNQT", obj.getDateToDNQT());
		}
		if (StringUtils.isNotEmpty(obj.getDateFromVTnet())) {
			query.setParameter("fromDateVTnet", obj.getDateFromVTnet());
			querySum.setParameter("fromDateVTnet", obj.getDateFromVTnet());
		}
		if (StringUtils.isNotEmpty(obj.getDateToVTnet())) {
			query.setParameter("toDateVTnet", obj.getDateToVTnet());
			querySum.setParameter("toDateVTnet", obj.getDateToVTnet());
		}
		if (StringUtils.isNotEmpty(obj.getDateFromInvestor())) {
			query.setParameter("fromDateInvestor", obj.getDateFromInvestor());
			querySum.setParameter("fromDateInvestor", obj.getDateFromInvestor());
		}
		if (StringUtils.isNotEmpty(obj.getDateToInvestor())) {
			query.setParameter("toDateInvestor", obj.getDateToInvestor());
			querySum.setParameter("toDateInvestor", obj.getDateToInvestor());
		}
		query.addScalar("ftName", new StringType());
		query.addScalar("totalQuantityByPlan", new LongType());
		query.addScalar("totalValueByPlan", new DoubleType());
		query.addScalar("totalHasDnqtQuantity", new LongType());
		query.addScalar("totalDnqtValue", new DoubleType());
		query.addScalar("totalSendVtnetQuantity", new LongType());
		query.addScalar("totalSendVtnetValue", new DoubleType());
		query.addScalar("totalVtnetConfirmedQuantity", new LongType());
		query.addScalar("totalVtnetConfirmedValue", new DoubleType());
		query.addScalar("totalApprovedQuantity", new LongType());
		query.addScalar("totalApprovedValue", new DoubleType());
		query.addScalar("dnqtPercent", new DoubleType());
		query.addScalar("vtnetPercent", new DoubleType());
		query.addScalar("approvedPercent", new DoubleType());
		// total
		querySum.addScalar("quantityByPlanTotal", new LongType());
		querySum.addScalar("valueByPlanTotal", new DoubleType());
		querySum.addScalar("hasDnqtQuantityTotal", new LongType());
		querySum.addScalar("dnqtValueTotal", new DoubleType());
		querySum.addScalar("sendVtnetQuantityTotal", new LongType());
		querySum.addScalar("sendVtnetValueTotal", new DoubleType());
		querySum.addScalar("vtnetConfirmedQuantityTotal", new LongType());
		querySum.addScalar("vtnetConfirmedValueTotal", new DoubleType());
		querySum.addScalar("approvedQuantityTotal", new LongType());
		querySum.addScalar("approvedValueTotal", new DoubleType());

		query.setResultTransformer(Transformers.aliasToBean(WoHcqtFtReportDTO.class));
		List<WoHcqtFtReportDTO> lst = query.list();
		obj.setTotalRecord(lst.size());
		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		List<WoHcqtFtReportDTO> lstNew = query.list();
		if (lstNew.size() > 0) {
			List<Object[]> rs = querySum.list();
			for (Object[] objects : rs) {
				lstNew.get(0).setQuantityByPlanTotal((Long) objects[0]);
				lstNew.get(0).setValueByPlanTotal((Double) objects[1]);
				lstNew.get(0).setHasDnqtQuantityTotal((Long) objects[2]);
				lstNew.get(0).setDnqtValueTotal((Double) objects[3]);
				lstNew.get(0).setSendVtnetQuantityTotal((Long) objects[4]);
				lstNew.get(0).setSendVtnetValueTotal((Double) objects[5]);
				lstNew.get(0).setVtnetConfirmedQuantityTotal((Long) objects[6]);
				lstNew.get(0).setVtnetConfirmedValueTotal((Double) objects[7]);
				lstNew.get(0).setApprovedQuantityTotal((Long) objects[8]);
				lstNew.get(0).setApprovedValueTotal((Double) objects[9]);
			}
		}
		return lstNew;
	}

	public List<WoHcqtFtReportDTO> exportFileHcqtFtReport(WoHcqtFtReportDTO obj) {
		StringBuilder sql = new StringBuilder(
				"WITH bc AS (     SELECT\n" + "        w.id,\n" + "        w.checklist_step,\n"
						+ "        w.FT_NAME ftname,\n" + "        w.money_value totalValueByPlan,\n"
						+ "        SUM(nvl(wc.dnqt_value,\n" + "        0) ) totalDnqtValue,\n"
						+ "        SUM(nvl(wc.vtnet_sent_value,\n" + "        0) ) totalSendVtnetValue,\n"
						+ "        SUM(nvl(wc.VTNET_CONFIRM_VALUE,\n" + "        0) ) totalVtnetConfirmedValue ,\n"
						+ "        SUM(nvl(wc.APROVE_DOC_VALUE,\n" + "        0) ) totalApprovedValue   \t\t\n"
						+ "    FROM\n" + "        wo_checklist wc,\n" + "        wo w     \n" + "    WHERE\n"
						+ "        wc.wo_id = w.id           \n" + "        AND w.status = 1  ");
		if (StringUtils.isNotEmpty(obj.getDateFrom())) {
			sql.append(" AND w.CREATED_DATE >= TO_DATE(:fromDate, 'dd/mm/yyyy')");
		}
		if (StringUtils.isNotEmpty(obj.getDateTo())) {
			sql.append(" AND w.CREATED_DATE < TO_DATE(:toDate, 'dd/mm/yyyy') + 1 ");
		}
		if (StringUtils.isNotEmpty(obj.getDateFromDNQT())) {
			sql.append(" AND wc.DNQT_DATE >= TO_DATE(:fromDateDNQT, 'dd/mm/yyyy')");
		}
		if (StringUtils.isNotEmpty(obj.getDateToDNQT())) {
			sql.append(" AND wc.DNQT_DATE < TO_DATE(:toDateDNQT, 'dd/mm/yyyy') + 1 ");
		}
		if (StringUtils.isNotEmpty(obj.getDateFromVTnet())) {
			sql.append(" AND wc.VTNET_SEND_DATE >= TO_DATE(:fromDateVTnet, 'dd/mm/yyyy')");
		}
		if (StringUtils.isNotEmpty(obj.getDateToVTnet())) {
			sql.append(" AND wc.VTNET_SEND_DATE < TO_DATE(:toDateVTnet, 'dd/mm/yyyy') + 1 ");
		}
		if (StringUtils.isNotEmpty(obj.getDateFromInvestor())) {
			sql.append(" AND wc.VTNET_CONFIRM_DATE >= TO_DATE(:fromDateInvestor, 'dd/mm/yyyy')");
		}
		if (StringUtils.isNotEmpty(obj.getDateToInvestor())) {
			sql.append(" AND wc.VTNET_CONFIRM_DATE < TO_DATE(:toDateInvestor, 'dd/mm/yyyy') + 1 ");
		}
		sql.append("   GROUP BY\n" + "        w.id,\n" + "        w.FT_NAME,\n" + "        w.money_value,\n"
				+ "        w.checklist_step )");

		String sqltext = "  SELECT\n" + "            ftname,\n"
				+ "            SUM(totalQuantityByPlan) totalQuantityByPlan,\n"
				+ "            SUM(totalValueByPlan) totalValueByPlan,\n"
				+ "            SUM(totalHasDnqtQuantity) totalHasDnqtQuantity,\n"
				+ "            SUM(totalDnqtValue) totalDnqtValue,\n"
				+ "            SUM(totalSendVtnetQuantity) totalSendVtnetQuantity,\n"
				+ "            SUM(totalSendVtnetValue) totalSendVtnetValue,\n"
				+ "            SUM(totalVtnetConfirmedQuantity) totalVtnetConfirmedQuantity,\n"
				+ "            SUM(totalVtnetConfirmedValue) totalVtnetConfirmedValue,\n"
				+ "            SUM(totalApprovedQuantity) totalApprovedQuantity,\n"
				+ "            SUM(totalApprovedValue) totalApprovedValue ,CASE WHEN  nvl(SUM(totalQuantityByPlan),0) > 0 THEN\n"
				+ "        nvl(ROUND(nvl(SUM(totalHasDnqtQuantity),0)*100/nvl(SUM(totalQuantityByPlan),0),2),0) ELSE 0 END dnqtPercent,\n"
				+ "        CASE WHEN  nvl(SUM(totalSendVtnetQuantity),0) > 0 THEN\n"
				+ "        nvl(ROUND(nvl(SUM(totalVtnetConfirmedQuantity),0)*100/nvl(SUM(totalSendVtnetQuantity),0),2),0)  ELSE 0 END vtnetPercent,\n"
				+ "        CASE WHEN  nvl(SUM(totalQuantityByPlan),0) > 0 THEN\n"
				+ "         nvl(ROUND(nvl(SUM(totalApprovedQuantity),0)*100/nvl(SUM(totalQuantityByPlan),0),2),0)  ELSE 0 END approvedPercent \n"
				+ "        FROM\n" + "            (         SELECT\n" + "                bc.ftname,\n"
				+ "                COUNT(bc.id) totalQuantityByPlan,\n"
				+ "                SUM(totalValueByPlan) totalValueByPlan,\n"
				+ "                0 totalHasDnqtQuantity,\n" + "                0 totalDnqtValue,\n"
				+ "                0 totalSendVtnetQuantity,\n" + "                0 totalSendVtnetValue,\n"
				+ "                0 totalVtnetConfirmedQuantity,\n" + "                0 totalVtnetConfirmedValue,\n"
				+ "     0 totalApprovedQuantity,\n" + "                0 totalApprovedValue \n" + "            FROM\n"
				+ "                bc         \n" + "            WHERE\n"
				+ "                bc.checklist_step >= 0         \n" + "            GROUP BY\n"
				+ "                bc.ftname         \n" + "            UNION\n" + "            ALL         SELECT\n"
				+ "                bc.ftname,\n" + "                0 totalQuantityByPlan,\n"
				+ "                0 totalValueByPlan,\n" + "                COUNT(bc.id) totalHasDnqtQuantity,\n"
				+ "                SUM(totalDnqtValue) totalDnqtValue,\n"
				+ "                0 totalSendVtnetQuantity,\n" + "                0 totalSendVtnetValue,\n"
				+ "                0 totalVtnetConfirmedQuantity,\n" + "                0 totalVtnetConfirmedValue,\n"
				+ "                0 totalApprovedQuantity,\n" + "                0 totalApprovedValue \n"
				+ "            FROM\n" + "                bc         \n" + "            WHERE\n"
				+ "                bc.checklist_step >= 1         \n" + "            GROUP BY\n"
				+ "                bc.ftname         \n" + "            UNION\n" + "            ALL         SELECT\n"
				+ "                bc.ftname,\n" + "                0 totalQuantityByPlan,\n"
				+ "                0 totalValueByPlan,\n" + "                0 totalHasDnqtQuantity,\n"
				+ "                0 totalDnqtValue,\n" + "                COUNT(bc.id) totalSendVtnetQuantity,\n"
				+ "                SUM(totalSendVtnetValue) totalSendVtnetValue,\n"
				+ "                0 totalVtnetConfirmedQuantity,\n" + "                0 totalVtnetConfirmedValue,\n"
				+ "                0 totalApprovedQuantity,\n" + "                0 totalApprovedValue \n"
				+ "            FROM\n" + "                bc         \n" + "            WHERE\n"
				+ "                bc.checklist_step >= 2         \n" + "            GROUP BY\n"
				+ "                bc.ftname         \n" + "            UNION\n" + "            ALL         SELECT\n"
				+ "                bc.ftname,\n" + "                0 totalQuantityByPlan,\n"
				+ "                0 totalValueByPlan,\n" + "                0 totalHasDnqtQuantity,\n"
				+ "                0 totalDnqtValue,\n" + "                0 totalSendVtnetQuantity,\n"
				+ "                0 totalSendVtnetValue,\n"
				+ "                COUNT(bc.id) totalVtnetConfirmedQuantity,\n"
				+ "                SUM(totalVtnetConfirmedValue) totalVtnetConfirmedValue,\n"
				+ "                0 totalApprovedQuantity,\n" + "                0 totalApprovedValue \n"
				+ "            FROM\n" + "                bc         \n" + "            WHERE\n"
				+ "                bc.checklist_step >= 3         \n" + "            GROUP BY\n"
				+ "                bc.ftname \n" + "             UNION\n" + "            ALL         SELECT\n"
				+ "                bc.ftname,\n" + "                0 totalQuantityByPlan,\n"
				+ "                0 totalValueByPlan,\n" + "                0 totalHasDnqtQuantity,\n"
				+ "                0 totalDnqtValue,\n" + "                0 totalSendVtnetQuantity,\n"
				+ "                0 totalSendVtnetValue,\n" + "                0 totalVtnetConfirmedQuantity,\n"
				+ "                0 totalVtnetConfirmedValue,\n"
				+ "                COUNT(bc.id) totalApprovedQuantity,\n"
				+ "                SUM(totalApprovedValue) totalApprovedValue         \n" + "            FROM\n"
				+ "                bc         \n" + "            WHERE\n"
				+ "                bc.checklist_step >= 4         \n" + "            GROUP BY\n"
				+ "                bc.ftname \n" + "        )   \n" + "    GROUP BY\n" + "        ftname \n"
				+ "    ORDER BY\n" + "        ftname";
		sql.append(sqltext);
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		if (StringUtils.isNotEmpty(obj.getDateFrom())) {
			query.setParameter("fromDate", obj.getDateFrom());
		}
		if (StringUtils.isNotEmpty(obj.getDateTo())) {
			query.setParameter("toDate", obj.getDateTo());
		}
		if (StringUtils.isNotEmpty(obj.getDateFromDNQT())) {
			query.setParameter("fromDateDNQT", obj.getDateFromDNQT());
		}
		if (StringUtils.isNotEmpty(obj.getDateToDNQT())) {
			query.setParameter("toDateDNQT", obj.getDateToDNQT());
		}
		if (StringUtils.isNotEmpty(obj.getDateFromVTnet())) {
			query.setParameter("fromDateVTnet", obj.getDateFromVTnet());
		}
		if (StringUtils.isNotEmpty(obj.getDateToVTnet())) {
			query.setParameter("toDateVTnet", obj.getDateToVTnet());
		}
		if (StringUtils.isNotEmpty(obj.getDateFromInvestor())) {
			query.setParameter("fromDateInvestor", obj.getDateFromInvestor());
		}
		if (StringUtils.isNotEmpty(obj.getDateToInvestor())) {
			query.setParameter("toDateInvestor", obj.getDateToInvestor());
		}
		query.addScalar("ftName", new StringType());
		query.addScalar("totalQuantityByPlan", new LongType());
		query.addScalar("totalValueByPlan", new DoubleType());
		query.addScalar("totalHasDnqtQuantity", new LongType());
		query.addScalar("totalDnqtValue", new DoubleType());
		query.addScalar("totalSendVtnetQuantity", new LongType());
		query.addScalar("totalSendVtnetValue", new DoubleType());
		query.addScalar("totalVtnetConfirmedQuantity", new LongType());
		query.addScalar("totalVtnetConfirmedValue", new DoubleType());
		query.addScalar("totalApprovedQuantity", new LongType());
		query.addScalar("totalApprovedValue", new DoubleType());
		query.addScalar("dnqtPercent", new DoubleType());
		query.addScalar("vtnetPercent", new DoubleType());
		query.addScalar("approvedPercent", new DoubleType());
		query.setResultTransformer(Transformers.aliasToBean(WoHcqtFtReportDTO.class));
		List<WoHcqtFtReportDTO> lst = query.list();
		return lst;
	}

	public List<ReportHSHCQTDTO> doSearchReportHSHCProvince(ReportHSHCQTDTO obj) {
		StringBuilder sql = new StringBuilder("   WITH bc AS (    \n" + " SELECT   a.id,\n"
				+ "          a.CHECKLIST_ORDER,\n" + "          a.has_problem,\n" + "          a.checklist_step,\n"
				+ "          a. provinceCode,\n" + "          a.moneyvalue,\n" + "          a.dnqtvalue,\n"
				+ "          a.vtnetvalue,\n" + "          a.aprovedvalue \n" + "        from (\n" + "   SELECT \n"
				+ "        w.id,\n" + "        w.CHECKLIST_ORDER,\n" + "        w.has_problem,\n"
				+ "        w.checklist_step,\n" + "        w.CAT_PROVINCE_CODE provinceCode,\n"
				+ "          w.money_value  moneyvalue  ,\n" + "        SUM(nvl(wc.dnqt_value,\n"
				+ "        0) ) dnqtvalue,\n" + "        SUM(nvl(wc.vtnet_sent_value,\n" + "        0) ) vtnetvalue,\n"
				+ "        SUM(nvl(wc.VTNET_CONFIRM_VALUE,\n" + "        0) ) aprovedvalue ,\n"
				+ "        MAX(DNQT_DATE) DNQT_DATE,\n" + "        MAX(VTNET_SEND_DATE) VTNET_SEND_DATE,\n"
				+ "        MAX(VTNET_CONFIRM_DATE) VTNET_CONFIRM_DATE\n" + "    FROM\n" + "        wo_checklist wc,\n"
				+ "        (SELECT\n" + "            DISTINCT          \n" + "            wo.id,\n"
				+ "            wo.checklist_step,\n" + "            wc.CHECKLIST_ORDER,\n"
				+ "            wo.CAT_PROVINCE_CODE,\n" + "            wo.money_value ,\n"
				+ "            nvl(wc.has_problem,0)  has_problem                    \n" + "        FROM\n"
				+ "            WO_TYPE wt,\n" + "            wo_checklist wc,\n"
				+ "            wo wo                        \n" + "        WHERE\n" + "            wc.wo_id = wo.id\n"
				+ "            AND wo.wo_type_id= wt.id\n" + "            AND wt.wo_type_code='HCQT'\n"
				+ "            AND wo.status = 1 \n" + "            AND wc.CHECKLIST_ORDER = wo.checklist_step + 1 ");
		if (StringUtils.isNotEmpty(obj.getDateFrom())) {
			sql.append(" AND wo.CREATED_DATE >= TO_DATE(:fromDate, 'dd/mm/yyyy')");
		}
		if (StringUtils.isNotEmpty(obj.getDateTo())) {
			sql.append(" AND wo.CREATED_DATE < TO_DATE(:toDate, 'dd/mm/yyyy') + 1 ");
		}

		sql.append(") w WHERE\n" + "        wc.wo_id = w.id                           \n" + "    GROUP BY\n"
				+ "        w.id,\n" + "        w.CAT_PROVINCE_CODE,\n" + "        w.checklist_step ,\n"
				+ "        w.has_problem,w.money_value,\n" + "        w.CHECKLIST_ORDER   ) a  where 1 = 1 ");

		if (StringUtils.isNotEmpty(obj.getDateFromDNQT())) {
			sql.append(" AND a.DNQT_DATE >= TO_DATE(:fromDateDNQT, 'dd/mm/yyyy')");
		}
		if (StringUtils.isNotEmpty(obj.getDateToDNQT())) {
			sql.append(" AND a.DNQT_DATE < TO_DATE(:toDateDNQT, 'dd/mm/yyyy') + 1 ");
		}
		if (StringUtils.isNotEmpty(obj.getDateFromVTnet())) {
			sql.append(" AND a.VTNET_SEND_DATE >= TO_DATE(:fromDateVTnet, 'dd/mm/yyyy')");
		}
		if (StringUtils.isNotEmpty(obj.getDateToVTnet())) {
			sql.append(" AND a.VTNET_SEND_DATE < TO_DATE(:toDateVTnet, 'dd/mm/yyyy') + 1 ");
		}
		if (StringUtils.isNotEmpty(obj.getDateFromInvestor())) {
			sql.append(" AND a.VTNET_CONFIRM_DATE >= TO_DATE(:fromDateInvestor, 'dd/mm/yyyy')");
		}
		if (StringUtils.isNotEmpty(obj.getDateToInvestor())) {
			sql.append(" AND a.VTNET_CONFIRM_DATE < TO_DATE(:toDateInvestor, 'dd/mm/yyyy') + 1 ");
		}
		sql.append("     ) SELECT  provinceCode,\n"
				+ "            SUM(quantitymoney) + SUM(quantitydnqt) +  SUM(quantityvtnet) + SUM(quantityaproved) + SUM(quantitymoneyPoblem) + SUM(quantitydnqtPoblem) +  SUM(quantityvtnetPoblem) + SUM(quantityaprovedPoblem)   quantityTotal,\n"
				+ "            SUM(moneyvalue) + SUM(dnqtvalue) +  SUM(aprovedvalue) + SUM(vtnetvalue) + SUM(moneyvaluePoblem) + SUM(dnqtvaluePoblem) +  SUM(aprovedvaluePoblem) + SUM(vtnetvaluePoblem) valueTotal ,\n"
				+ "            SUM(quantitymoney) + SUM(quantitymoneyPoblem)  quantityMoneyTotal,\n"
				+ "            SUM(moneyvalue) + SUM(moneyvaluePoblem) moneyvalueTotal,\n"
				+ "            SUM(quantitydnqt) +  SUM(quantitydnqtPoblem)  quantitydnqtTotal,\n"
				+ "            SUM(dnqtvalue) + SUM(dnqtvaluePoblem) dnqtvalueTotal,\n"
				+ "            SUM(quantityvtnet) + SUM(quantityvtnetPoblem)  quantityvtnetTotal,\n"
				+ "            SUM(vtnetvalue) +  SUM(vtnetvaluePoblem)  vtnetvalueTotal,\n"
				+ "            SUM(quantityaproved) + SUM(quantityaprovedPoblem)  quantityaprovedTotal,\n"
				+ "            SUM(aprovedvalue)+ SUM(aprovedvaluePoblem) aprovedvalueTotal,\n"
				+ "            SUM(quantitymoney) quantityMoney,\n" + "            SUM(moneyvalue) moneyvalue,\n"
				+ "            SUM(quantitydnqt) quantitydnqt,\n" + "            SUM(dnqtvalue) dnqtvalue,\n"
				+ "            SUM(quantityvtnet) quantityvtnet,\n" + "            SUM(vtnetvalue) vtnetvalue,\n"
				+ "            SUM(quantityaproved) quantityaproved,\n"
				+ "            SUM(aprovedvalue) aprovedvalue,\n"
				+ "            SUM(quantitymoneyPoblem) quantityMoneyPoblem,\n"
				+ "            SUM(moneyvaluePoblem) moneyvaluePoblem,\n"
				+ "            SUM(quantitydnqtPoblem) quantitydnqtPoblem,\n"
				+ "            SUM(dnqtvaluePoblem) dnqtvaluePoblem,\n"
				+ "            SUM(quantityvtnetPoblem) quantityvtnetPoblem,\n"
				+ "            SUM(vtnetvaluePoblem) vtnetvaluePoblem,\n"
				+ "            SUM(quantityaprovedPoblem) quantityaprovedPoblem,\n"
				+ "            SUM(aprovedvaluePoblem) aprovedvaluePoblem          \n" + "        FROM\n"
				+ "            (       SELECT\n" + "                bc.provinceCode,\n"
				+ "                COUNT(bc.id) quantitymoney,\n" + "                SUM(moneyvalue) moneyvalue,\n"
				+ "                0 quantitydnqt,\n" + "                0 dnqtvalue,\n"
				+ "                0 quantityvtnet,\n" + "                0 vtnetvalue,\n"
				+ "                0 quantityaproved,\n" + "                0 aprovedvalue,\n"
				+ "                0 quantitymoneyPoblem,\n" + "                0 moneyvaluePoblem,\n"
				+ "                0 quantitydnqtPoblem,\n" + "                0 dnqtvaluePoblem,\n"
				+ "                0 quantityvtnetPoblem,\n" + "                0 vtnetvaluePoblem,\n"
				+ "                0 quantityaprovedPoblem,\n"
				+ "                0 aprovedvaluePoblem                    \n" + "            FROM\n"
				+ "                bc                           \n" + "            WHERE\n"
				+ "                bc.checklist_step = 0      \n" + "                and bc.has_problem = 0   \n"
				+ "                and bc.CHECKLIST_ORDER = 1                 \n" + "            GROUP BY\n"
				+ "                bc.provinceCode                           \n" + "            UNION\n"
				+ "            ALL                  SELECT\n" + "                bc.provinceCode,\n"
				+ "                0 quantitymoney,\n" + "                0 moneyvalue,\n"
				+ "                COUNT(bc.id) quantitydnqt,\n" + "                SUM(dnqtvalue) dnqtvalue,\n"
				+ "                0 quantityvtnet,\n" + "                0 vtnetvalue,\n"
				+ "                0 quantityaproved,\n" + "                0 aprovedvalue,\n"
				+ "                0 quantitymoneyPoblem,\n" + "                0 moneyvaluePoblem,\n"
				+ "                0 quantitydnqtPoblem,\n" + "                0 dnqtvaluePoblem,\n"
				+ "                0 quantityvtnePoblemt,\n" + "                0 vtnetvaluePoblem,\n"
				+ "                0 quantityaprovedPoblem,\n"
				+ "                0 aprovedvaluePoblem                  \n" + "            FROM\n"
				+ "                bc                           \n" + "            WHERE\n"
				+ "                bc.checklist_step = 1    \n" + "                and bc.CHECKLIST_ORDER = 2    \n"
				+ "                and bc.has_problem = 0                  \n" + "            GROUP BY\n"
				+ "                bc.provinceCode                           \n" + "            UNION\n"
				+ "            ALL                 SELECT\n" + "                bc.provinceCode,\n"
				+ "                0 quantitymoney,\n" + "                0 moneyvalue,\n"
				+ "                0 quantitydnqt,\n" + "                0 dnqtvalue,\n"
				+ "                COUNT(bc.id) quantityvtnet,\n" + "                SUM(vtnetvalue) vtnetvalue,\n"
				+ "                0 quantityaproved,\n" + "                0 aprovedvalue,\n"
				+ "                0 quantitymoneyPoblem,\n" + "                0 moneyvaluePoblem,\n"
				+ "                0 quantitydnqtPoblem,\n" + "                0 dnqtvaluePoblem,\n"
				+ "                0 quantityvtnetPoblem,\n" + "                0 vtnetvaluePoblem,\n"
				+ "                0 quantityaprovedPoblem,\n"
				+ "                0 aprovedvaluePoblem                  \n" + "            FROM\n"
				+ "                bc                           \n" + "            WHERE\n"
				+ "                bc.checklist_step = 2  \n" + "                and bc.CHECKLIST_ORDER = 3   \n"
				+ "                and bc.has_problem = 0                     \n" + "            GROUP BY\n"
				+ "                bc.provinceCode                           \n" + "            UNION\n"
				+ "            ALL          SELECT\n" + "                bc.provinceCode,\n"
				+ "                0 quantitymoney,\n" + "                0 moneyvalue,\n"
				+ "                0 quantitydnqt,\n" + "                0 dnqtvalue,\n"
				+ "                0 quantityvtnet,\n" + "                0 vtnetvalue,\n"
				+ "                COUNT(bc.id) quantityaproved,\n"
				+ "                SUM(aprovedvalue) aprovedvalue,\n" + "                0 quantitymoneyPoblem,\n"
				+ "                0 moneyvaluePoblem,\n" + "                0 quantitydnqtPoblem,\n"
				+ "                0 dnqtvaluePoblem,\n" + "                0 quantityvtnetPoblem,\n"
				+ "                0 vtnetvaluePoblem,\n" + "                0 quantityaprovedPoblem,\n"
				+ "                0 aprovedvaluePoblem                  \n" + "            FROM\n"
				+ "                bc                           \n" + "            WHERE\n"
				+ "                bc.checklist_step = 3        \n" + "                and bc.CHECKLIST_ORDER = 4   \n"
				+ "                and bc.has_problem = 0                \n" + "            GROUP BY\n"
				+ "                bc.provinceCode                   \n" + "            UNION\n"
				+ "            ALL          SELECT\n" + "                bc.provinceCode,\n"
				+ "                0 quantitymoney,\n" + "                0 moneyvalue,\n"
				+ "                0 quantitydnqt,\n" + "                0 dnqtvalue,\n"
				+ "                0 quantityvtnet,\n" + "                0 vtnetvalue,\n"
				+ "                0 quantityaproved,\n" + "                0 aprovedvalue,\n"
				+ "                COUNT(bc.id)  quantitymoneyPoblem,\n"
				+ "                SUM(moneyvalue)  moneyvaluePoblem,\n" + "                0 quantitydnqtPoblem,\n"
				+ "                0 dnqtvaluePoblem,\n" + "                0 quantityvtnetPoblem,\n"
				+ "                0 vtnetvaluePoblem,\n" + "                0 quantityaprovedPoblem,\n"
				+ "                0 aprovedvaluePoblem                  \n" + "            FROM\n"
				+ "                bc                           \n" + "            WHERE\n"
				+ "                bc.checklist_step = 0  \n" + "                and bc.CHECKLIST_ORDER = 1     \n"
				+ "                and bc.has_problem > 0               \n" + "            GROUP BY\n"
				+ "                bc.provinceCode                   \n" + "            UNION\n"
				+ "            ALL          SELECT\n" + "                bc.provinceCode,\n"
				+ "                0 quantitymoney,\n" + "                0 moneyvalue,\n"
				+ "                0 quantitydnqt,\n" + "                0 dnqtvalue,\n"
				+ "                0 quantityvtnet,\n" + "                0 vtnetvalue,\n"
				+ "                0 quantityaproved,\n" + "                0 aprovedvalue,\n"
				+ "                0 quantitymoneyPoblem,\n" + "                0 moneyvaluePoblem,\n"
				+ "                COUNT(bc.id) quantitydnqtPoblem,\n"
				+ "                SUM(dnqtvalue) dnqtvaluePoblem,\n" + "                0 quantityvtnetPoblem,\n"
				+ "                0 vtnetvaluePoblem,\n" + "                0 quantityaprovedPoblem,\n"
				+ "                0 aprovedvaluePoblem                  \n" + "            FROM\n"
				+ "                bc                           \n" + "            WHERE\n"
				+ "                bc.checklist_step = 1    \n" + "                and bc.CHECKLIST_ORDER = 2   \n"
				+ "                and bc.has_problem > 0                   \n" + "            GROUP BY\n"
				+ "                bc.provinceCode                   \n" + "            UNION\n"
				+ "            ALL          SELECT\n" + "                bc.provinceCode,\n"
				+ "                0 quantitymoney,\n" + "                0 moneyvalue,\n"
				+ "                0 quantitydnqt,\n" + "                0 dnqtvalue,\n"
				+ "                0 quantityvtnet,\n" + "                0 vtnetvalue,\n"
				+ "                0 quantityaproved,\n" + "                0 aprovedvalue,\n"
				+ "                0 quantitymoneyPoblem,\n" + "                0 moneyvaluePoblem,\n"
				+ "                0 quantitydnqtPoblem,\n" + "                0 dnqtvaluePoblem,\n"
				+ "                COUNT(bc.id) quantityvtnetPoblem,\n"
				+ "                SUM(vtnetvalue) vtnetvaluePoblem,\n" + "                0 quantityaprovedPoblem,\n"
				+ "                0 aprovedvaluePoblem                  \n" + "            FROM\n"
				+ "                bc                           \n" + "            WHERE\n"
				+ "                bc.checklist_step = 2       \n" + "                and bc.CHECKLIST_ORDER = 3    \n"
				+ "                and bc.has_problem > 0                 \n" + "            GROUP BY\n"
				+ "                bc.provinceCode                   \n" + "            UNION\n"
				+ "            ALL          SELECT\n" + "                bc.provinceCode,\n"
				+ "                0 quantitymoney,\n" + "                0 moneyvalue,\n"
				+ "                0 quantitydnqt,\n" + "                0 dnqtvalue,\n"
				+ "                0 quantityvtnet,\n" + "                0 vtnetvalue,\n"
				+ "                0 quantityaproved,\n" + "                0 aprovedvalue,\n"
				+ "                0 quantitymoneyPoblem,\n" + "                0 moneyvaluePoblem,\n"
				+ "                0 quantitydnqtPoblem,\n" + "                0 dnqtvaluePoblem,\n"
				+ "                0 quantityvtnetPoblem,\n" + "                0 vtnetvaluePoblem,\n"
				+ "                COUNT(bc.id) quantityaprovedPoblem,\n"
				+ "                SUM(aprovedvalue) aprovedvaluePoblem                  \n" + "            FROM\n"
				+ "                bc                           \n" + "            WHERE\n"
				+ "                bc.checklist_step = 3    \n" + "                and bc.CHECKLIST_ORDER = 4   \n"
				+ "                and bc.has_problem > 0                  \n" + "            GROUP BY\n"
				+ "                bc.provinceCode               \n" + "        )     \n" + "    GROUP BY\n"
				+ "        provinceCode   \n" + "    ORDER BY\n" + "        provinceCode");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		if (StringUtils.isNotEmpty(obj.getDateFrom())) {
			query.setParameter("fromDate", obj.getDateFrom());
		}
		if (StringUtils.isNotEmpty(obj.getDateTo())) {
			query.setParameter("toDate", obj.getDateTo());
		}
		if (StringUtils.isNotEmpty(obj.getDateFromDNQT())) {
			query.setParameter("fromDateDNQT", obj.getDateFromDNQT());
		}
		if (StringUtils.isNotEmpty(obj.getDateToDNQT())) {
			query.setParameter("toDateDNQT", obj.getDateToDNQT());
		}
		if (StringUtils.isNotEmpty(obj.getDateFromVTnet())) {
			query.setParameter("fromDateVTnet", obj.getDateFromVTnet());
		}
		if (StringUtils.isNotEmpty(obj.getDateToVTnet())) {
			query.setParameter("toDateVTnet", obj.getDateToVTnet());
		}
		if (StringUtils.isNotEmpty(obj.getDateFromInvestor())) {
			query.setParameter("fromDateInvestor", obj.getDateFromInvestor());
		}
		if (StringUtils.isNotEmpty(obj.getDateToInvestor())) {
			query.setParameter("toDateInvestor", obj.getDateToInvestor());
		}
		if (StringUtils.isNotEmpty(obj.getDateFrom())) {
			query.setParameter("fromDate", obj.getDateFrom());
		}
		if (StringUtils.isNotEmpty(obj.getDateTo())) {
			query.setParameter("toDate", obj.getDateTo());
		}
		if (StringUtils.isNotEmpty(obj.getDateFromDNQT())) {
			query.setParameter("fromDateDNQT", obj.getDateFromDNQT());
		}
		if (StringUtils.isNotEmpty(obj.getDateToDNQT())) {
			query.setParameter("toDateDNQT", obj.getDateToDNQT());
		}
		if (StringUtils.isNotEmpty(obj.getDateFromVTnet())) {
			query.setParameter("fromDateVTnet", obj.getDateFromVTnet());
		}
		if (StringUtils.isNotEmpty(obj.getDateToVTnet())) {
			query.setParameter("toDateVTnet", obj.getDateToVTnet());
		}
		if (StringUtils.isNotEmpty(obj.getDateFromInvestor())) {
			query.setParameter("fromDateInvestor", obj.getDateFromInvestor());
		}
		if (StringUtils.isNotEmpty(obj.getDateToInvestor())) {
			query.setParameter("toDateInvestor", obj.getDateToInvestor());
		}
		query.addScalar("provinceCode", new StringType());
		query.addScalar("quantityTotal", new LongType());
		query.addScalar("valueTotal", new DoubleType());
		query.addScalar("quantityMoneyTotal", new LongType());
		query.addScalar("moneyValueTotal", new DoubleType());
		query.addScalar("quantityDnqtTotal", new LongType());
		query.addScalar("dnqtValueTotal", new DoubleType());
		query.addScalar("quantityVtnetTotal", new LongType());
		query.addScalar("vtnetValueTotal", new DoubleType());
		query.addScalar("quantityAprovedTotal", new LongType());
		query.addScalar("aprovedValueTotal", new DoubleType());
		query.addScalar("quantityMoney", new LongType());
		query.addScalar("moneyValue", new DoubleType());
		query.addScalar("quantityDnqt", new LongType());
		query.addScalar("dnqtValue", new DoubleType());
		query.addScalar("quantityVtnet", new LongType());
		query.addScalar("vtnetValue", new DoubleType());
		query.addScalar("quantityAproved", new LongType());
		query.addScalar("aprovedValue", new DoubleType());
		query.addScalar("quantityMoneyPoblem", new LongType());
		query.addScalar("moneyValuePoblem", new DoubleType());
		query.addScalar("quantityDnqtPoblem", new LongType());
		query.addScalar("dnqtValuePoblem", new DoubleType());
		query.addScalar("quantityVtnetPoblem", new LongType());
		query.addScalar("vtnetValuePoblem", new DoubleType());
		query.addScalar("quantityAprovedPoblem", new LongType());
		query.addScalar("aprovedValuePoblem", new DoubleType());
		query.setResultTransformer(Transformers.aliasToBean(ReportHSHCQTDTO.class));
		List<ReportHSHCQTDTO> lst = query.list();
		obj.setTotalRecord(lst.size());
		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		List<ReportHSHCQTDTO> lstNew = query.list();
		return lstNew;
	}

	public List<ReportHSHCQTDTO> exportFileHSHCProvince(ReportHSHCQTDTO obj) {
		StringBuilder sql = new StringBuilder("   WITH bc AS (    \n" + " SELECT   a.id,\n"
				+ "          a.CHECKLIST_ORDER,\n" + "          a.has_problem,\n" + "          a.checklist_step,\n"
				+ "          a. provinceCode,\n" + "          a.moneyvalue,\n" + "          a.dnqtvalue,\n"
				+ "          a.vtnetvalue,\n" + "          a.aprovedvalue \n" + "        from (\n" + "   SELECT \n"
				+ "        w.id,\n" + "        w.CHECKLIST_ORDER,\n" + "        w.has_problem,\n"
				+ "        w.checklist_step,\n" + "        w.CAT_PROVINCE_CODE provinceCode,\n"
				+ "          w.money_value  moneyvalue  ,\n" + "        SUM(nvl(wc.dnqt_value,\n"
				+ "        0) ) dnqtvalue,\n" + "        SUM(nvl(wc.vtnet_sent_value,\n" + "        0) ) vtnetvalue,\n"
				+ "        SUM(nvl(wc.VTNET_CONFIRM_VALUE,\n" + "        0) ) aprovedvalue ,\n"
				+ "        MAX(DNQT_DATE) DNQT_DATE,\n" + "        MAX(VTNET_SEND_DATE) VTNET_SEND_DATE,\n"
				+ "        MAX(VTNET_CONFIRM_DATE) VTNET_CONFIRM_DATE\n" + "    FROM\n" + "        wo_checklist wc,\n"
				+ "        (SELECT\n" + "            DISTINCT          \n" + "            wo.id,\n"
				+ "            wo.checklist_step,\n" + "            wc.CHECKLIST_ORDER,\n"
				+ "            wo.CAT_PROVINCE_CODE,\n" + "            wo.money_value ,\n"
				+ "            nvl(wc.has_problem,0)  has_problem                    \n" + "        FROM\n"
				+ "            WO_TYPE wt,\n" + "            wo_checklist wc,\n"
				+ "            wo wo                        \n" + "        WHERE\n" + "            wc.wo_id = wo.id\n"
				+ "            AND wo.wo_type_id= wt.id\n" + "            AND wt.wo_type_code='HCQT'\n"
				+ "            AND wo.status = 1 \n" + "            AND wc.CHECKLIST_ORDER = wo.checklist_step + 1 ");
		if (StringUtils.isNotEmpty(obj.getDateFrom())) {
			sql.append(" AND wo.CREATED_DATE >= TO_DATE(:fromDate, 'dd/mm/yyyy')");
		}
		if (StringUtils.isNotEmpty(obj.getDateTo())) {
			sql.append(" AND wo.CREATED_DATE < TO_DATE(:toDate, 'dd/mm/yyyy') + 1 ");
		}

		sql.append(") w WHERE\n" + "        wc.wo_id = w.id                           \n" + "    GROUP BY\n"
				+ "        w.id,\n" + "        w.CAT_PROVINCE_CODE,w.money_value,\n" + "        w.checklist_step ,\n"
				+ "        w.has_problem,\n" + "        w.CHECKLIST_ORDER   ) a  where 1 = 1 ");

		if (StringUtils.isNotEmpty(obj.getDateFromDNQT())) {
			sql.append(" AND a.DNQT_DATE >= TO_DATE(:fromDateDNQT, 'dd/mm/yyyy')");
		}
		if (StringUtils.isNotEmpty(obj.getDateToDNQT())) {
			sql.append(" AND a.DNQT_DATE < TO_DATE(:toDateDNQT, 'dd/mm/yyyy') + 1 ");
		}
		if (StringUtils.isNotEmpty(obj.getDateFromVTnet())) {
			sql.append(" AND a.VTNET_SEND_DATE >= TO_DATE(:fromDateVTnet, 'dd/mm/yyyy')");
		}
		if (StringUtils.isNotEmpty(obj.getDateToVTnet())) {
			sql.append(" AND a.VTNET_SEND_DATE < TO_DATE(:toDateVTnet, 'dd/mm/yyyy') + 1 ");
		}
		if (StringUtils.isNotEmpty(obj.getDateFromInvestor())) {
			sql.append(" AND a.VTNET_CONFIRM_DATE >= TO_DATE(:fromDateInvestor, 'dd/mm/yyyy')");
		}
		if (StringUtils.isNotEmpty(obj.getDateToInvestor())) {
			sql.append(" AND a.VTNET_CONFIRM_DATE < TO_DATE(:toDateInvestor, 'dd/mm/yyyy') + 1 ");
		}
		sql.append("     ) SELECT  provinceCode,\n"
				+ "            SUM(quantitymoney) + SUM(quantitydnqt) +  SUM(quantityvtnet) + SUM(quantityaproved) + SUM(quantitymoneyPoblem) + SUM(quantitydnqtPoblem) +  SUM(quantityvtnetPoblem) + SUM(quantityaprovedPoblem)   quantityTotal,\n"
				+ "            SUM(moneyvalue) + SUM(dnqtvalue) +  SUM(aprovedvalue) + SUM(vtnetvalue) + SUM(moneyvaluePoblem) + SUM(dnqtvaluePoblem) +  SUM(aprovedvaluePoblem) + SUM(vtnetvaluePoblem) valueTotal ,\n"
				+ "            SUM(quantitymoney) + SUM(quantitymoneyPoblem)  quantityMoneyTotal,\n"
				+ "            SUM(moneyvalue) + SUM(moneyvaluePoblem) moneyvalueTotal,\n"
				+ "            SUM(quantitydnqt) +  SUM(quantitydnqtPoblem)  quantitydnqtTotal,\n"
				+ "            SUM(dnqtvalue) + SUM(dnqtvaluePoblem) dnqtvalueTotal,\n"
				+ "            SUM(quantityvtnet) + SUM(quantityvtnetPoblem)  quantityvtnetTotal,\n"
				+ "            SUM(vtnetvalue) +  SUM(vtnetvaluePoblem)  vtnetvalueTotal,\n"
				+ "            SUM(quantityaproved) + SUM(quantityaprovedPoblem)  quantityaprovedTotal,\n"
				+ "            SUM(aprovedvalue)+ SUM(aprovedvaluePoblem) aprovedvalueTotal,\n"
				+ "            SUM(quantitymoney) quantityMoney,\n" + "            SUM(moneyvalue) moneyvalue,\n"
				+ "            SUM(quantitydnqt) quantitydnqt,\n" + "            SUM(dnqtvalue) dnqtvalue,\n"
				+ "            SUM(quantityvtnet) quantityvtnet,\n" + "            SUM(vtnetvalue) vtnetvalue,\n"
				+ "            SUM(quantityaproved) quantityaproved,\n"
				+ "            SUM(aprovedvalue) aprovedvalue,\n"
				+ "            SUM(quantitymoneyPoblem) quantityMoneyPoblem,\n"
				+ "            SUM(moneyvaluePoblem) moneyvaluePoblem,\n"
				+ "            SUM(quantitydnqtPoblem) quantitydnqtPoblem,\n"
				+ "            SUM(dnqtvaluePoblem) dnqtvaluePoblem,\n"
				+ "            SUM(quantityvtnetPoblem) quantityvtnetPoblem,\n"
				+ "            SUM(vtnetvaluePoblem) vtnetvaluePoblem,\n"
				+ "            SUM(quantityaprovedPoblem) quantityaprovedPoblem,\n"
				+ "            SUM(aprovedvaluePoblem) aprovedvaluePoblem          \n" + "        FROM\n"
				+ "            (       SELECT\n" + "                bc.provinceCode,\n"
				+ "                COUNT(bc.id) quantitymoney,\n" + "                SUM(moneyvalue) moneyvalue,\n"
				+ "                0 quantitydnqt,\n" + "                0 dnqtvalue,\n"
				+ "                0 quantityvtnet,\n" + "                0 vtnetvalue,\n"
				+ "                0 quantityaproved,\n" + "                0 aprovedvalue,\n"
				+ "                0 quantitymoneyPoblem,\n" + "                0 moneyvaluePoblem,\n"
				+ "                0 quantitydnqtPoblem,\n" + "                0 dnqtvaluePoblem,\n"
				+ "                0 quantityvtnetPoblem,\n" + "                0 vtnetvaluePoblem,\n"
				+ "                0 quantityaprovedPoblem,\n"
				+ "                0 aprovedvaluePoblem                    \n" + "            FROM\n"
				+ "                bc                           \n" + "            WHERE\n"
				+ "                bc.checklist_step = 0      \n" + "                and bc.has_problem = 0   \n"
				+ "                and bc.CHECKLIST_ORDER = 1                 \n" + "            GROUP BY\n"
				+ "                bc.provinceCode                           \n" + "            UNION\n"
				+ "            ALL                  SELECT\n" + "                bc.provinceCode,\n"
				+ "                0 quantitymoney,\n" + "                0 moneyvalue,\n"
				+ "                COUNT(bc.id) quantitydnqt,\n" + "                SUM(dnqtvalue) dnqtvalue,\n"
				+ "                0 quantityvtnet,\n" + "                0 vtnetvalue,\n"
				+ "                0 quantityaproved,\n" + "                0 aprovedvalue,\n"
				+ "                0 quantitymoneyPoblem,\n" + "                0 moneyvaluePoblem,\n"
				+ "                0 quantitydnqtPoblem,\n" + "                0 dnqtvaluePoblem,\n"
				+ "                0 quantityvtnePoblemt,\n" + "                0 vtnetvaluePoblem,\n"
				+ "                0 quantityaprovedPoblem,\n"
				+ "                0 aprovedvaluePoblem                  \n" + "            FROM\n"
				+ "                bc                           \n" + "            WHERE\n"
				+ "                bc.checklist_step = 1    \n" + "                and bc.CHECKLIST_ORDER = 2    \n"
				+ "                and bc.has_problem = 0                  \n" + "            GROUP BY\n"
				+ "                bc.provinceCode                           \n" + "            UNION\n"
				+ "            ALL                 SELECT\n" + "                bc.provinceCode,\n"
				+ "                0 quantitymoney,\n" + "                0 moneyvalue,\n"
				+ "                0 quantitydnqt,\n" + "                0 dnqtvalue,\n"
				+ "                COUNT(bc.id) quantityvtnet,\n" + "                SUM(vtnetvalue) vtnetvalue,\n"
				+ "                0 quantityaproved,\n" + "                0 aprovedvalue,\n"
				+ "                0 quantitymoneyPoblem,\n" + "                0 moneyvaluePoblem,\n"
				+ "                0 quantitydnqtPoblem,\n" + "                0 dnqtvaluePoblem,\n"
				+ "                0 quantityvtnetPoblem,\n" + "                0 vtnetvaluePoblem,\n"
				+ "                0 quantityaprovedPoblem,\n"
				+ "                0 aprovedvaluePoblem                  \n" + "            FROM\n"
				+ "                bc                           \n" + "            WHERE\n"
				+ "                bc.checklist_step = 2  \n" + "                and bc.CHECKLIST_ORDER = 3   \n"
				+ "                and bc.has_problem = 0                     \n" + "            GROUP BY\n"
				+ "                bc.provinceCode                           \n" + "            UNION\n"
				+ "            ALL          SELECT\n" + "                bc.provinceCode,\n"
				+ "                0 quantitymoney,\n" + "                0 moneyvalue,\n"
				+ "                0 quantitydnqt,\n" + "                0 dnqtvalue,\n"
				+ "                0 quantityvtnet,\n" + "                0 vtnetvalue,\n"
				+ "                COUNT(bc.id) quantityaproved,\n"
				+ "                SUM(aprovedvalue) aprovedvalue,\n" + "                0 quantitymoneyPoblem,\n"
				+ "                0 moneyvaluePoblem,\n" + "                0 quantitydnqtPoblem,\n"
				+ "                0 dnqtvaluePoblem,\n" + "                0 quantityvtnetPoblem,\n"
				+ "                0 vtnetvaluePoblem,\n" + "                0 quantityaprovedPoblem,\n"
				+ "                0 aprovedvaluePoblem                  \n" + "            FROM\n"
				+ "                bc                           \n" + "            WHERE\n"
				+ "                bc.checklist_step = 3        \n" + "                and bc.CHECKLIST_ORDER = 4   \n"
				+ "                and bc.has_problem = 0                \n" + "            GROUP BY\n"
				+ "                bc.provinceCode                   \n" + "            UNION\n"
				+ "            ALL          SELECT\n" + "                bc.provinceCode,\n"
				+ "                0 quantitymoney,\n" + "                0 moneyvalue,\n"
				+ "                0 quantitydnqt,\n" + "                0 dnqtvalue,\n"
				+ "                0 quantityvtnet,\n" + "                0 vtnetvalue,\n"
				+ "                0 quantityaproved,\n" + "                0 aprovedvalue,\n"
				+ "                COUNT(bc.id)  quantitymoneyPoblem,\n"
				+ "                SUM(moneyvalue)  moneyvaluePoblem,\n" + "                0 quantitydnqtPoblem,\n"
				+ "                0 dnqtvaluePoblem,\n" + "                0 quantityvtnetPoblem,\n"
				+ "                0 vtnetvaluePoblem,\n" + "                0 quantityaprovedPoblem,\n"
				+ "                0 aprovedvaluePoblem                  \n" + "            FROM\n"
				+ "                bc                           \n" + "            WHERE\n"
				+ "                bc.checklist_step = 0  \n" + "                and bc.CHECKLIST_ORDER = 1     \n"
				+ "                and bc.has_problem > 0               \n" + "            GROUP BY\n"
				+ "                bc.provinceCode                   \n" + "            UNION\n"
				+ "            ALL          SELECT\n" + "                bc.provinceCode,\n"
				+ "                0 quantitymoney,\n" + "                0 moneyvalue,\n"
				+ "                0 quantitydnqt,\n" + "                0 dnqtvalue,\n"
				+ "                0 quantityvtnet,\n" + "                0 vtnetvalue,\n"
				+ "                0 quantityaproved,\n" + "                0 aprovedvalue,\n"
				+ "                0 quantitymoneyPoblem,\n" + "                0 moneyvaluePoblem,\n"
				+ "                COUNT(bc.id) quantitydnqtPoblem,\n"
				+ "                SUM(dnqtvalue) dnqtvaluePoblem,\n" + "                0 quantityvtnetPoblem,\n"
				+ "                0 vtnetvaluePoblem,\n" + "                0 quantityaprovedPoblem,\n"
				+ "                0 aprovedvaluePoblem                  \n" + "            FROM\n"
				+ "                bc                           \n" + "            WHERE\n"
				+ "                bc.checklist_step = 1    \n" + "                and bc.CHECKLIST_ORDER = 2   \n"
				+ "                and bc.has_problem > 0                   \n" + "            GROUP BY\n"
				+ "                bc.provinceCode                   \n" + "            UNION\n"
				+ "            ALL          SELECT\n" + "                bc.provinceCode,\n"
				+ "                0 quantitymoney,\n" + "                0 moneyvalue,\n"
				+ "                0 quantitydnqt,\n" + "                0 dnqtvalue,\n"
				+ "                0 quantityvtnet,\n" + "                0 vtnetvalue,\n"
				+ "                0 quantityaproved,\n" + "                0 aprovedvalue,\n"
				+ "                0 quantitymoneyPoblem,\n" + "                0 moneyvaluePoblem,\n"
				+ "                0 quantitydnqtPoblem,\n" + "                0 dnqtvaluePoblem,\n"
				+ "                COUNT(bc.id) quantityvtnetPoblem,\n"
				+ "                SUM(vtnetvalue) vtnetvaluePoblem,\n" + "                0 quantityaprovedPoblem,\n"
				+ "                0 aprovedvaluePoblem                  \n" + "            FROM\n"
				+ "                bc                           \n" + "            WHERE\n"
				+ "                bc.checklist_step = 2       \n" + "                and bc.CHECKLIST_ORDER = 3    \n"
				+ "                and bc.has_problem > 0                 \n" + "            GROUP BY\n"
				+ "                bc.provinceCode                   \n" + "            UNION\n"
				+ "            ALL          SELECT\n" + "                bc.provinceCode,\n"
				+ "                0 quantitymoney,\n" + "                0 moneyvalue,\n"
				+ "                0 quantitydnqt,\n" + "                0 dnqtvalue,\n"
				+ "                0 quantityvtnet,\n" + "                0 vtnetvalue,\n"
				+ "                0 quantityaproved,\n" + "                0 aprovedvalue,\n"
				+ "                0 quantitymoneyPoblem,\n" + "                0 moneyvaluePoblem,\n"
				+ "                0 quantitydnqtPoblem,\n" + "                0 dnqtvaluePoblem,\n"
				+ "                0 quantityvtnetPoblem,\n" + "                0 vtnetvaluePoblem,\n"
				+ "                COUNT(bc.id) quantityaprovedPoblem,\n"
				+ "                SUM(aprovedvalue) aprovedvaluePoblem                  \n" + "            FROM\n"
				+ "                bc                           \n" + "            WHERE\n"
				+ "                bc.checklist_step = 3    \n" + "                and bc.CHECKLIST_ORDER = 4   \n"
				+ "                and bc.has_problem > 0                  \n" + "            GROUP BY\n"
				+ "                bc.provinceCode               \n" + "        )     \n" + "    GROUP BY\n"
				+ "        provinceCode   \n" + "    ORDER BY\n" + "        provinceCode");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		if (StringUtils.isNotEmpty(obj.getDateFrom())) {
			query.setParameter("fromDate", obj.getDateFrom());
		}
		if (StringUtils.isNotEmpty(obj.getDateTo())) {
			query.setParameter("toDate", obj.getDateTo());
		}
		if (StringUtils.isNotEmpty(obj.getDateFromDNQT())) {
			query.setParameter("fromDateDNQT", obj.getDateFromDNQT());
		}
		if (StringUtils.isNotEmpty(obj.getDateToDNQT())) {
			query.setParameter("toDateDNQT", obj.getDateToDNQT());
		}
		if (StringUtils.isNotEmpty(obj.getDateFromVTnet())) {
			query.setParameter("fromDateVTnet", obj.getDateFromVTnet());
		}
		if (StringUtils.isNotEmpty(obj.getDateToVTnet())) {
			query.setParameter("toDateVTnet", obj.getDateToVTnet());
		}
		if (StringUtils.isNotEmpty(obj.getDateFromInvestor())) {
			query.setParameter("fromDateInvestor", obj.getDateFromInvestor());
		}
		if (StringUtils.isNotEmpty(obj.getDateToInvestor())) {
			query.setParameter("toDateInvestor", obj.getDateToInvestor());
		}
		if (StringUtils.isNotEmpty(obj.getDateFrom())) {
			query.setParameter("fromDate", obj.getDateFrom());
		}
		if (StringUtils.isNotEmpty(obj.getDateTo())) {
			query.setParameter("toDate", obj.getDateTo());
		}
		if (StringUtils.isNotEmpty(obj.getDateFromDNQT())) {
			query.setParameter("fromDateDNQT", obj.getDateFromDNQT());
		}
		if (StringUtils.isNotEmpty(obj.getDateToDNQT())) {
			query.setParameter("toDateDNQT", obj.getDateToDNQT());
		}
		if (StringUtils.isNotEmpty(obj.getDateFromVTnet())) {
			query.setParameter("fromDateVTnet", obj.getDateFromVTnet());
		}
		if (StringUtils.isNotEmpty(obj.getDateToVTnet())) {
			query.setParameter("toDateVTnet", obj.getDateToVTnet());
		}
		if (StringUtils.isNotEmpty(obj.getDateFromInvestor())) {
			query.setParameter("fromDateInvestor", obj.getDateFromInvestor());
		}
		if (StringUtils.isNotEmpty(obj.getDateToInvestor())) {
			query.setParameter("toDateInvestor", obj.getDateToInvestor());
		}
		query.addScalar("provinceCode", new StringType());
		query.addScalar("quantityTotal", new LongType());
		query.addScalar("valueTotal", new DoubleType());
		query.addScalar("quantityMoneyTotal", new LongType());
		query.addScalar("moneyValueTotal", new DoubleType());
		query.addScalar("quantityDnqtTotal", new LongType());
		query.addScalar("dnqtValueTotal", new DoubleType());
		query.addScalar("quantityVtnetTotal", new LongType());
		query.addScalar("vtnetValueTotal", new DoubleType());
		query.addScalar("quantityAprovedTotal", new LongType());
		query.addScalar("aprovedValueTotal", new DoubleType());
		query.addScalar("quantityMoney", new LongType());
		query.addScalar("moneyValue", new DoubleType());
		query.addScalar("quantityDnqt", new LongType());
		query.addScalar("dnqtValue", new DoubleType());
		query.addScalar("quantityVtnet", new LongType());
		query.addScalar("vtnetValue", new DoubleType());
		query.addScalar("quantityAproved", new LongType());
		query.addScalar("aprovedValue", new DoubleType());
		query.addScalar("quantityMoneyPoblem", new LongType());
		query.addScalar("moneyValuePoblem", new DoubleType());
		query.addScalar("quantityDnqtPoblem", new LongType());
		query.addScalar("dnqtValuePoblem", new DoubleType());
		query.addScalar("quantityVtnetPoblem", new LongType());
		query.addScalar("vtnetValuePoblem", new DoubleType());
		query.addScalar("quantityAprovedPoblem", new LongType());
		query.addScalar("aprovedValuePoblem", new DoubleType());
		query.setResultTransformer(Transformers.aliasToBean(ReportHSHCQTDTO.class));
		List<ReportHSHCQTDTO> lst = query.list();
		return lst;
	}

	public WoSimpleStationDTO getStationByCode(String code) {
		String sql = "select s.CAT_STATION_ID as stationId, s.name as stationName, s.code as stationCode, s.address as stationAddress"
				+ ", s.latitude, s.longitude, p.code as catProvinceCode, h.CAT_STATION_HOUSE_ID catStationHouseId, h.CODE catStationHouseCode "
				+ " from cat_station s " + "left join cat_province p on s.cat_province_id = p.cat_province_id "
				+ "left join CAT_STATION_HOUSE h on s.CAT_STATION_HOUSE_ID = h.CAT_STATION_HOUSE_ID "
				+ "where s.status >0 and s.code = :code fetch next 1 row only ";

		SQLQuery query = getSession().createSQLQuery(sql);

		query.setParameter("code", code);
		query.addScalar("stationId", new LongType());
		query.addScalar("stationName", new StringType());
		query.addScalar("stationCode", new StringType());
		query.addScalar("stationAddress", new StringType());
		query.addScalar("latitude", new StringType());
		query.addScalar("longitude", new StringType());
		query.addScalar("catProvinceCode", new StringType());
		query.addScalar("catStationHouseId", new LongType());
		query.addScalar("catStationHouseCode", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(WoSimpleStationDTO.class));
		return (WoSimpleStationDTO) query.uniqueResult();
	}

	public List<WoDTO> getAllWoByTypeId(Long typeId) {
		String sql = baseSelectStr + " from wo woTbl where woTbl.status > 0 and woTbl.WO_TYPE_ID = :typeId ";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("typeId", typeId);
		query = mapFields(query);
		query.setResultTransformer(Transformers.aliasToBean(WoDTO.class));
		return query.list();
	}

	public SysUserCOMSDTO getUserInfoByLoginname(String loginname) {
		StringBuilder sql = new StringBuilder("SELECT\n" + "    su.sys_user_id sysuserid,\n"
				+ "    su.login_name loginname,\n" + "    su.full_name fullname,\n" + "    su.password password,\n"
				+ "    su.employee_code employeecode,\n" + "    su.email email,\n"
				+ "    su.phone_number phonenumber,\n" + "    su.status status,\n" + "    su.sys_group_id sysgroupid,\n"
				+ "    sg.name sysgroupname\n" + "FROM\n" + "    sys_user su,\n" + "    sys_group sg\n" + "WHERE\n"
				+ "    su.login_name =:loginname\n" + "    AND sg.sys_group_id = su.sys_group_id");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("sysUserId", new LongType());
		query.addScalar("loginName", new StringType());
		query.addScalar("employeeCode", new StringType());
		query.addScalar("fullName", new StringType());
		query.addScalar("email", new StringType());
		query.addScalar("phoneNumber", new StringType());
		query.addScalar("password", new StringType());
		query.addScalar("status", new StringType());
		query.addScalar("sysGroupId", new LongType());
		query.addScalar("sysGroupName", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(SysUserCOMSDTO.class));
		query.setParameter("loginname", loginname);
		return (SysUserCOMSDTO) query.uniqueResult();
	}

	public Long sumProblemHcqtWo(Long woId) {
		String sql = "select sum(nvl(has_problem,0)) as totalProblem from wo_checklist where wo_id = :woId ";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("woId", woId);
		query.addScalar("totalProblem", new LongType());
		return (Long) query.uniqueResult();
	}

	public List<WorkItemDTO> getDoneWorkItemByConstructionId(Long constructionId) {
		if (constructionId == null)
			return new ArrayList<>();
		String sql = "select name as name, quantity as quantity from work_item where status = 3 and construction_id = :consId ";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("consId", constructionId);
		query.addScalar("name", new StringType());
		query.addScalar("quantity", new DoubleType());
		query.setResultTransformer(Transformers.aliasToBean(WorkItemDTO.class));
		return query.list();
	}

	public List<WoHcqtDTO> doSearchHcqtWo(WoDTO dto, boolean isCreatorHcqt, boolean isExporting, boolean isViewWoHcqt) {
		String sql = "select wo.id woId, wo.wo_name woName, wo.wo_code woCode, wo.station_code stationCode, wo.cat_province_code catProvinceCode, "
				+ " hp.HCQT_PROJECT_NAME hcqtProjectName, wo.cnkv cnkv, wo.hcqt_contract_code hcqtContractCode, wo.money_value moneyValue, "
				+ " wo.finish_date finishDate, wo.state state, wo.hshc_receive_date hshcReceiveDate, wo.ft_name ftName, wo.checklist_step checklistStep ";

		if (isExporting) {
			sql += " , cl1.dnqt_date dnqtDate, cl1.dnqt_value dnqtValue, cl1.COMPLETE_PERSON_NAME as dnqtPerson "
					+ ", cl2.vtnet_send_date vtnetSendDate"
					+ ", cl3.vtnet_confirm_date vtnetConfirmDate, cl3.aproved_person aprovedPerson, cl3.COMPLETE_PERSON_NAME vtnetConfirmPerson "
					+ ", cl4.APROVED_DOC_DATE aprovedDocDate, cl3.VTNET_CONFIRM_VALUE vtnetConfirmValue "
					+ ", COALESCE (cl5.problem_name, cl4.problem_name, cl3.problem_name, cl2.problem_name, cl1.problem_name, null) problemName "
					+ ", COALESCE (cl5.content, cl4.content, cl3.content, cl2.content, cl1.content, null) problemContent, "
					+ "ap.name apWorkSrcName, ap.CODE apWorkSrcCode ";
		}

		sql += " from wo "
				+ " left join WO_HCQT_PROJECT hp on wo.hcqt_project_id = hp.HCQT_PROJECT_ID and hp.status != 0 "
				+ " inner join WO_TYPE wt on wo.wo_type_id = wt.id and wt.status != 0 and wt.wo_type_code = 'HCQT' ";

		if (isExporting) {
			sql += "left join wo_checklist cl1 on cl1.wo_id = wo.id and cl1.checklist_order = 1 "
					+ "left join wo_checklist cl2 on cl2.wo_id = wo.id and cl2.checklist_order = 2 "
					+ "left join wo_checklist cl3 on cl3.wo_id = wo.id and cl3.checklist_order = 3 "
					+ "left join wo_checklist cl4 on cl4.wo_id = wo.id and cl4.checklist_order = 4 "
					+ "left join wo_checklist cl5 on cl5.wo_id = wo.id and cl5.checklist_order = 5 ";
		}
		sql += " LEFT JOIN APP_PARAM ap ON wo.AP_WORK_SRC = ap.code and ap.PAR_TYPE = 'AP_WORK_SRC'"; // taotq add
		sql += " where wo.status != 0 ";

		if (StringUtils.isNotEmpty(dto.getKeySearch())) {
			sql += " AND lower(wo.wo_code) like :keySearch";
		}

		if (dto.getChecklistStep() != null) {
			sql += " and wo.checklist_step = :checklistStep ";

			// Nếu tìm kiếm các bản ghi bước 4 (bảng thẩm về chờ TC TCT duyệt) phải loại trừ
			// các WO bị TC từ chối
			if (dto.getChecklistStep() == 4) {
				sql += " and wo.state != 'NG' ";
			}
		}

		if (dto.getHcqtProjectId() != null) {
			sql += " and wo.hcqt_project_id = :hcqtProjectId ";
		}

		if (StringUtils.isNotEmpty(dto.getHcqtContractCode())) {
			sql += " and wo.hcqt_contract_code = :hcqtContractCode ";
		}

		if (StringUtils.isNotEmpty(dto.getStationCode())) {
			sql += " and wo.station_code = :stationCode ";
		}

		if (StringUtils.isNotEmpty(dto.getCatProvinceCode())) {
			sql += " and wo.CAT_PROVINCE_CODE = :catProvinceCode ";
		}

		if (StringUtils.isNotEmpty(dto.getState())) {
			sql += " and wo.STATE = :state ";
		}

		// Huypq-02112020-start check view wo hcqt
		if (!isViewWoHcqt) {
			if (!isCreatorHcqt) {
				sql += " and wo.ft_id = :sysUserId ";
			}
		} else {
			if (dto.getFtId() != null) {
				sql += " and wo.ft_id = :ftId ";
			}
		}
		// Huy-end
		sql += " order by wo.id desc ";

		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql);
		sqlCount.append(")");
		SQLQuery query = getSession().createSQLQuery(sql);
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		if (StringUtils.isNotEmpty(dto.getKeySearch())) {
			query.setParameter("keySearch", toSearchStr(dto.getKeySearch()));
			queryCount.setParameter("keySearch", toSearchStr(dto.getKeySearch()));
		}

		if (dto.getChecklistStep() != null) {
			query.setParameter("checklistStep", dto.getChecklistStep());
			queryCount.setParameter("checklistStep", dto.getChecklistStep());
		}

		if (dto.getHcqtProjectId() != null) {
			query.setParameter("hcqtProjectId", dto.getHcqtProjectId());
			queryCount.setParameter("hcqtProjectId", dto.getHcqtProjectId());
		}

		if (StringUtils.isNotEmpty(dto.getHcqtContractCode())) {
			query.setParameter("hcqtContractCode", dto.getHcqtContractCode());
			queryCount.setParameter("hcqtContractCode", dto.getHcqtContractCode());
		}

		if (StringUtils.isNotEmpty(dto.getStationCode())) {
			query.setParameter("stationCode", dto.getStationCode());
			queryCount.setParameter("stationCode", dto.getStationCode());
		}

		if (StringUtils.isNotEmpty(dto.getCatProvinceCode())) {
			query.setParameter("catProvinceCode", dto.getCatProvinceCode());
			queryCount.setParameter("catProvinceCode", dto.getCatProvinceCode());
		}

		if (StringUtils.isNotEmpty(dto.getState())) {
			query.setParameter("state", dto.getState());
			queryCount.setParameter("state", dto.getState());
		}

		// Huypq-02112020-start check view wo hcqt
		if (!isViewWoHcqt) {

			// nếu không phải quản lý thì chỉ nhìn thấy wo được giao
			if (!isCreatorHcqt) {
				query.setParameter("sysUserId", dto.getSysUserId());
				queryCount.setParameter("sysUserId", dto.getSysUserId());
			}
		} else {
			if (dto.getFtId() != null) {
				query.setParameter("ftId", dto.getFtId());
				queryCount.setParameter("ftId", dto.getFtId());
			}
		}
		// Huy-end

		query.addScalar("woId", new LongType());
		query.addScalar("woName", new StringType());
		query.addScalar("woCode", new StringType());
		query.addScalar("stationCode", new StringType());
		query.addScalar("catProvinceCode", new StringType());
		query.addScalar("hcqtProjectName", new StringType());
		query.addScalar("cnkv", new StringType());
		query.addScalar("hcqtContractCode", new StringType());
		query.addScalar("moneyValue", new DoubleType());
		query.addScalar("finishDate", new DateType());
		query.addScalar("state", new StringType());
		query.addScalar("hshcReceiveDate", new DateType());
		query.addScalar("ftName", new StringType());
		query.addScalar("checklistStep", new LongType());
		query.addScalar("apWorkSrcName", new StringType());
		query.addScalar("apWorkSrcCode", new StringType());

		if (isExporting) {
			query.addScalar("dnqtDate", new DateType());
			query.addScalar("dnqtValue", new DoubleType());
			query.addScalar("dnqtPerson", new StringType());
			query.addScalar("vtnetSendDate", new DateType());
			query.addScalar("vtnetConfirmDate", new DateType());
			query.addScalar("aprovedPerson", new StringType());
			query.addScalar("vtnetConfirmPerson", new StringType());
			query.addScalar("aprovedDocDate", new DateType());
			query.addScalar("vtnetConfirmValue", new DoubleType());
			query.addScalar("problemName", new StringType());
			query.addScalar("problemContent", new StringType());
		}

		query.setResultTransformer(Transformers.aliasToBean(WoHcqtDTO.class));

		if (dto.getPage() != null && dto.getPageSize() != null) {
			query.setFirstResult((dto.getPage().intValue() - 1) * dto.getPageSize().intValue());
			query.setMaxResults(dto.getPageSize().intValue());
		}

		dto.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
		return query.list();
	}

	public List<WoSimpleSysGroupDTO> getCnktCdLevel2() {
		StringBuilder sql = new StringBuilder("select SYS_GROUP_ID as sysGroupId, GROUP_NAME_LEVEL2 as groupNameLv2, "
				+ " GROUP_NAME_LEVEL3 as groupNameLv3, NAME as groupName, " + " PARENT_ID as parentId "
				+ " from SYS_GROUP where GROUP_LEVEL = 3 and CODE like '%P.KT%' ");

		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("sysGroupId", new LongType());
		query.addScalar("groupName", new StringType());
		query.addScalar("groupNameLv2", new StringType());
		query.addScalar("groupNameLv3", new StringType());

		query.setResultTransformer(Transformers.aliasToBean(WoSimpleSysGroupDTO.class));
		return query.list();
	}

	// Huypq-14102020-start
	public SysGroupDto getSysGroupNameById(Long id) {
		StringBuilder sql = new StringBuilder(
				"SELECT sys_group_id groupId, name name from sys_group where sys_group_id=:id ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("groupId", new LongType());
		query.addScalar("name", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(SysGroupDto.class));
		query.setParameter("id", id);
		return (SysGroupDto) query.uniqueResult();
	}

	public List<WoDTO> getWoIDByCode(List<String> listwoCode, String type) {
		StringBuilder sql = new StringBuilder("SELECT w.id woId,\n" + "  w.wo_code woCode,\n"
				+ "  w.WO_TYPE_ID woTypeId,w.state state,\n" + "  wt.WO_TYPE_CODE woTypeCode,\n"
				+ "  w.WO_ORDER_CONFIRM woOrderConfirm,\n"
				+ "  w.contract_id contractId,w.AP_WORK_SRC apWorkSrc,w.AP_CONSTRUCTION_TYPE apConstructionType \n"
				+ " ,w.FT_ID ftId " + // Huypq-28042021-add
				"FROM WO_TYPE wt,\n" + "  wo w\n" + "WHERE wt.ID   = w.WO_TYPE_ID\n" + "AND wt.STATUS = 1\n"
				+ "AND w.STATUS  = 1 and w.wo_code in (:listwoCode)");
		if (type != null && type.equals("0")) {
			// sql.append("and w.state ='ACCEPT_CD'");//
			sql.append("and w.state IN ( 'ACCEPT_CD','REJECT_FT') "); // Duonghv13-19102021-add
		}
		if (type != null && type.equals("1")) {
			sql.append("and w.state ='DONE'");
		}
		if (type != null && type.equals("2")) {
			sql.append("and (w.state ='CD_OK' OR w.state ='TC_BRANCH_REJECTED') ");
		}
		if (type != null && type.equals("TCTCT")) {
			sql.append(
					" AND ((w.state ='WAIT_TC_TCT' AND wt.WO_TYPE_CODE='HSHC') OR (w.CHECKLIST_STEP = 4 AND wt.WO_TYPE_CODE='HCQT') OR (w.state ='PROCESSING' AND wt.WO_TYPE_CODE='DOANHTHU') )");
		}
		if (type != null && type.equals("TCBRANCH")) {
			sql.append(
					" AND (w.state ='WAIT_TC_BRANCH' OR (w.state ='TC_TCT_REJECTED' AND wt.WO_TYPE_CODE = 'HSHC')) AND wt.WO_TYPE_CODE not in ('HCQT') ");
		}
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("woId", new LongType());
		query.addScalar("woCode", new StringType());
		query.addScalar("woTypeId", new LongType());
		query.addScalar("state", new StringType());
		query.addScalar("woTypeCode", new StringType());
		query.addScalar("contractId", new LongType());
		query.addScalar("apWorkSrc", new LongType());
		query.addScalar("apConstructionType", new LongType());
		query.addScalar("woOrderConfirm", new LongType());
		query.addScalar("ftId", new LongType());

		if (listwoCode.size() > 0) {
			query.setParameterList("listwoCode", listwoCode);
		}
		query.setResultTransformer(Transformers.aliasToBean(WoDTO.class));
		List<WoDTO> lst = query.list();
		return lst;
	}

	public List<Long> getListIdWoTaskDaily() {
		String sql = "select distinct wo_id from wo_task_daily";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.addScalar("wo_id", new LongType());
		return query.list();
	}

	public Double getHshcMoneyValueByConstructionId(Long consId) {
		String sql = "select nvl(sum(quantity),0) as total from work_item where construction_id = :consId and status = 3";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("consId", consId);
		query.addScalar("total", new DoubleType());
		return (Double) query.uniqueResult();
	}

	public List<WoDTO> exportApproveWo(String type, List<String> listData) {
		StringBuilder sql = new StringBuilder("SELECT w.WO_CODE as woCode,\n"
				+ "                  wt.WO_TYPE_NAME as woTypeName,\n"
				+ "                  (select ap.name from APP_PARAM ap where to_char(w.AP_WORK_SRC) = ap.CODE  AND ap.PAR_TYPE = 'AP_WORK_SRC' AND ap.status  = 1 ) apWorkSrcName,\n"
				+ "                  NVL(w.contract_code,w.project_code) contractCode,\n"
				+ "                  w.STATION_CODE stationCode,\n"
				+ "                  w.CONSTRUCTION_CODE constructionCode,\n"
				+ "                  (SELECT cwt.NAME from   CAT_WORK_ITEM_TYPE cwt where w.CAT_WORK_ITEM_TYPE_ID = cwt.CAT_WORK_ITEM_TYPE_ID  AND cwt.status  = 1) catWorkItemTypeName,\n"
				+ "                  round(nvl(w.MONEY_VALUE,0) / 1000000,2) moneyValue,\n"
				+ "                  to_char(w.FINISH_DATE,'dd/MM/yyyy') finishDateStr,\n"
				+ "                  to_char(w.END_TIME,'dd/MM/yyyy') endTimeStr,\n"
				+ "                  w.CD_LEVEL_2_NAME cdLevel2Name,\n"
				+ "                  w.WO_ORDER_CONFIRM woOrderConfirm,\n" + "                  w.FT_NAME ftName\n"
				+ "                 FROM WO_TYPE wt,\n" + "                  WO w\n"
				+ "                WHERE wt.id                 = w.wo_type_id\n"
				+ "                 AND w.CD_LEVEL_1           IS NOT NULL\n"
				+ "                 AND wt.status               = 1 \n"
//				+ "					AND  w.UPDATE_CD_APPROVE_WO>=sysdate-90 \n"
				+ "                 AND w.status                = 1 ");
		if (type.equals("0")) {
			// sql.append(" AND w.state ='ACCEPT_CD' AND wt.WO_TYPE_CODE not in ('HCQT') ");
			sql.append(" AND w.state IN ('ACCEPT_CD','REJECT_FT') AND wt.WO_TYPE_CODE not in ('HCQT') ");// duonghv13
																											// add
																											// 22102021

			if (listData.size() > 0) {
				sql.append(
						" AND ((w.CD_LEVEL_2 in (:groupIdList) AND w.CD_LEVEL_3 is null) OR w.CD_LEVEL_4 in (:groupIdList) )");
				// sql.append(" AND ((w.CD_LEVEL_2 in (:groupIdList) ) OR w.CD_LEVEL_4 in
				// (:groupIdList) )");
			}
		} else if (type.equals("1")) {
			sql.append("AND w.state ='DONE' AND wt.WO_TYPE_CODE not in ('HCQT') ");
			if (listData.size() > 0) {
				sql.append(
						"AND (w.CD_LEVEL_2 in (:groupIdList) OR w.CD_LEVEL_3 in (:groupIdList) OR w.CD_LEVEL_4 in (:groupIdList))");
			}
		} else if (type.equals("2") || type.equals("PQT")) {
			sql.append(
					" AND (((w.state ='CD_OK' OR w.state ='TC_BRANCH_REJECTED') AND wt.WO_TYPE_CODE not in ('HCQT')) OR (w.state ='WAIT_PQT' AND wt.WO_TYPE_CODE not in ('HSHC') AND w.AP_WORK_SRC=4 AND w.TR_ID is not null)) ");
			if (listData.size() > 0) {
				sql.append(" AND w.CD_LEVEL_1 in (:groupIdList)");
			}
		} else if (type.equals("3")) {
			sql.append(" AND w.state ='ACCEPT_CD' AND wt.WO_TYPE_CODE not in ('HCQT')  ");
			if (listData.size() > 0) {
				sql.append(" AND w.CD_LEVEL_5 in (:groupIdList)");
			}
		} else if (type.equals("4")) {
			sql.append(" AND w.state ='DONE' AND wt.WO_TYPE_CODE not in ('HCQT')  ");
			if (listData.size() > 0) {
				sql.append(" AND w.CD_LEVEL_5 in (:groupIdList)");
			}
		} else if (type.equals("TCTCT")) {
			sql.append(
					" AND ((w.state ='WAIT_TC_TCT' AND wt.WO_TYPE_CODE='HSHC') OR (w.CHECKLIST_STEP = 4 AND wt.WO_TYPE_CODE='HCQT') OR (w.state ='PROCESSING' AND wt.WO_TYPE_CODE='DOANHTHU') )");
		} else if (type.equals("TCBRANCH")) {
			sql.append(
					" AND (w.state ='WAIT_TC_BRANCH' OR (w.state ='TC_TCT_REJECTED' AND wt.WO_TYPE_CODE = 'HSHC')) AND wt.WO_TYPE_CODE not in ('HCQT') ");
			if (listData.size() > 0) {
				sql.append(" AND w.CD_LEVEL_1 in (:groupIdList)");
			}
		}
		// Huypq-22102021-start
		else if (type.equals("PĐH_TTHT")) {
			sql.append(
					" AND w.state ='CD_OK' AND wt.WO_TYPE_CODE not in ('HSHC') AND w.AP_WORK_SRC=4 AND w.TR_ID is not null ");
			if (listData.size() > 0) {
				sql.append(" AND w.CD_LEVEL_1 in (:groupIdList)");
			}
		}
		// else if (type.equals("PQT")) {
		// sql.append(" AND w.state ='WAIT_PQT' AND wt.WO_TYPE_CODE not in ('HSHC') AND
		// w.AP_WORK_SRC=4 AND w.TR_ID is not null ");
		// if (listData.size() > 0) {
		// sql.append(" AND w.CD_LEVEL_1 in (:groupIdList)");
		// }
		// }
		else if (type.equals("TTĐTHT")) {
			sql.append(
					" AND w.state ='WAIT_TTDTHT' AND wt.WO_TYPE_CODE not in ('HSHC') AND w.AP_WORK_SRC=4 AND w.TR_ID is not null ");
			if (listData.size() > 0) {
				sql.append(" AND w.CD_LEVEL_1 in (:groupIdList)");
			}
		}
		// Huy-end
		sql.append("ORDER BY w.WO_CODE,wt.WO_TYPE_NAME");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		if (listData.size() > 0 && !type.equals("TCTCT")) {
			query.setParameterList("groupIdList", listData);
		}
		query.addScalar("woCode", new StringType());
		query.addScalar("woTypeName", new StringType());
		query.addScalar("apWorkSrcName", new StringType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("stationCode", new StringType());
		query.addScalar("constructionCode", new StringType());
		query.addScalar("catWorkItemTypeName", new StringType());
		query.addScalar("moneyValue", new DoubleType());
		query.addScalar("finishDateStr", new StringType());
		query.addScalar("endTimeStr", new StringType());
		query.addScalar("cdLevel2Name", new StringType());
		query.addScalar("ftName", new StringType());
		query.addScalar("woOrderConfirm", new LongType());

		query.setResultTransformer(Transformers.aliasToBean(WoDTO.class));
		List<WoDTO> lst = query.list();
		return lst;
	}

	public boolean checkWorkItemDone(Long consId, Long catWorkId) {
		String sql = "select nvl(status,0) as status from work_item where construction_id = :consId and cat_work_item_type_id = :catWorkId ";

		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("consId", consId);
		query.setParameter("catWorkId", catWorkId);
		query.addScalar("status", new LongType());
		Long status = (Long) query.uniqueResult();
		if (status != null && status.equals(3))
			return true;
		else
			return false;
	}

	public int updatePercentConstructionNG(WoDTO dto) {
		int result = 0;
		try {
			// cap nhat hang muc
			if (dto.getCatWorkItemTypeId() != null) {
				StringBuilder sql = new StringBuilder(
						"update WORK_ITEM SET STATUS = '2', COMPLETE_DATE = null,QUANTITY = null  where CONSTRUCTION_ID = :constructionId AND CAT_WORK_ITEM_TYPE_ID = :catWorkID");
				SQLQuery query = this.getSession().createSQLQuery(sql.toString());
				query.setParameter("constructionId", dto.getConstructionId());
				query.setParameter("catWorkID", dto.getCatWorkItemTypeId());
				result = query.executeUpdate();

			}
			StringBuilder sql = new StringBuilder(
					"update CONSTRUCTION SET status = 3 ,COMPLETE_DATE = null,COMPLETE_VALUE = null where CONSTRUCTION_ID = :constructionId");
			SQLQuery query = this.getSession().createSQLQuery(sql.toString());
			query.setParameter("constructionId", dto.getConstructionId());
			result = query.executeUpdate();

		} catch (Exception ex) {
			//ex.printStackTrace();
			result = 0;
		}
		return result;
	}

	public SysUserDTO checkUserFT(String ft, Long sysGroupId) {
		StringBuilder sql = new StringBuilder("SELECT\n" + "  SYS_USER_ID userId,\n" + "  FULL_NAME fullName,\n"
				+ "  login_name loginName,EMPLOYEE_CODE employeeCode,\n" + "  EMAIL email\n" + "FROM SYS_USER\n"
				+ "WHERE status = 1\n" + "AND (lower(login_name)LIKE lower(:ft)\n"
				+ "OR lower(REPLACE(EMAIL,'@viettel.com.vn','')) LIKE lower(REPLACE(:ft,'@viettel.com.vn','')))  ");
		if (sysGroupId != null && sysGroupId > 0) {
			sql.append(
					" AND SYS_GROUP_ID in (select SYS_GROUP_ID from SYS_GROUP where parent_id = ( select parent_id from SYS_GROUP where sys_group_id = :sysGroupId )) ");
		}
		if (sysGroupId != null && sysGroupId == 0) {
			sql.append(" AND Sys_Group_id IN (SELECT SYS_GROUP_ID FROM Sys_Group WHERE Parent_Id IN (9006003))");
		}
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("userId", new LongType());
		query.addScalar("fullName", new StringType());
		query.addScalar("loginName", new StringType());
		query.addScalar("employeeCode", new StringType());
		query.addScalar("email", new StringType());
		query.setParameter("ft", ft);
		if (sysGroupId != null && sysGroupId > 0) {
			query.setParameter("sysGroupId", sysGroupId);
		}
		query.setResultTransformer(Transformers.aliasToBean(SysUserDTO.class));
		return (SysUserDTO) query.uniqueResult();
	}

	public List<WoTaskDailyDTO> getCheckWoTaskDaily(Long woId) {
		String sql = new String(
				" select WO_MAPPING_CHECKLIST_ID woMappingChecklistId, SUM(QUANTITY) quantity  from WO_TASK_DAILY WHERE WO_ID =:woId AND STATUS = 1 GROUP BY WO_MAPPING_CHECKLIST_ID");
		SQLQuery query = getSession().createSQLQuery(sql);
		query.addScalar("woMappingChecklistId", new LongType());
		query.addScalar("quantity", new DoubleType());
		query.setParameter("woId", woId);
		query.setResultTransformer(Transformers.aliasToBean(WoTaskDailyDTO.class));
		List<WoTaskDailyDTO> lstWoTaskDaily = query.list();
		return lstWoTaskDaily;
	}

	public int updateWoMapingCheckList(Double quantity, Long woMappingChecklistId) {
		int result = 0;
		try {
			StringBuilder sql = new StringBuilder(
					"update WO_MAPPING_CHECKLIST set STATE = 'DONE',QUANTITY_LENGTH =:quantity WHERE ID = :woMappingChecklistId ");
			SQLQuery query = this.getSession().createSQLQuery(sql.toString());
			query.setParameter("quantity", quantity);
			query.setParameter("woMappingChecklistId", woMappingChecklistId);
			result = query.executeUpdate();
		} catch (Exception ex) {
			//ex.printStackTrace();
			result = 0;
		}
		return result;
	}

	// duonghv13-start 20092021
	public int updateWoMapingCheckListXLSC(Long woId) {
		int result = 0;
		try {
			StringBuilder sql = new StringBuilder(
					"update WO_MAPPING_CHECKLIST set STATE = 'DONE' WHERE WO_ID = :woId ");
			SQLQuery query = this.getSession().createSQLQuery(sql.toString());
			query.setParameter("woId", woId);
			result = query.executeUpdate();
		} catch (Exception ex) {
			//ex.printStackTrace();
			result = 0;
		}
		return result;
	}
	// duong-end

	public int updateWoTaskDaily(String loginUser, Long woId) {
		int result = 0;
		try {
			StringBuilder sql = new StringBuilder(
					" update WO_TASK_DAILY set CONFIRM ='1' ,APPROVE_DATE= sysdate , APPROVE_BY =:loginUser  WHERE WO_ID =:woId ");
			SQLQuery query = this.getSession().createSQLQuery(sql.toString());
			query.setParameter("loginUser", loginUser);
			query.setParameter("woId", woId);
			result = query.executeUpdate();
		} catch (Exception ex) {
			//ex.printStackTrace();
			result = 0;
		}
		return result;

	}

	public boolean checkExistCDLevel2(Long id) {
		StringBuilder sql = new StringBuilder(
				"select count(*) as total from SYS_GROUP where GROUP_LEVEL = 3 and SYS_GROUP_ID = :id ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("id", id);
		query.addScalar("total", new LongType());
		long total = (Long) query.uniqueResult();

		if (total > 0)
			return true;
		else
			return false;
	}

	public List<String> getAllBills() {
		String sql = " select money_flow_bill as bill from wo where status!= 0 and money_flow_bill is not null ";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.addScalar("bill", new StringType());
		// Huypq-20112020-start fix error
		query.setResultTransformer(Transformers.aliasToBean(WoDTO.class));
		List<WoDTO> ls = query.list();
		List<String> lstBill = new ArrayList<>();
		for (WoDTO bill : ls) {
			lstBill.add(bill.getBill());
		}
		return lstBill;
		// Huy-end
	}

	public List<WoFTConfig5SDTO> doSearchReportWo5s(WoDTO woDTO) {
		StringBuilder sql = new StringBuilder("select " + "wo.CD_LEVEL_2_NAME as cdLevel2Name , wo.FT_NAME as ftName , "
				+ "count(*) AS totalRecord5s, "
				+ "COUNT(CASE WHEN wo.STATE = 'OK' AND wo.END_TIME <= wo.FINISH_DATE THEN 1 END) AS countDone, "
				+ "COUNT(CASE WHEN wo.STATE = ('OK') AND wo.END_TIME > wo.FINISH_DATE THEN 1 END) AS countDoneOver, "
				+ "COUNT(CASE WHEN wo.STATE NOT IN ('OK') AND wo.END_TIME <= wo.FINISH_DATE THEN 1 END) AS countNotDone, "
				+ "COUNT(CASE WHEN wo.STATE NOT IN ('OK') AND wo.FINISH_DATE > wo.FINISH_DATE THEN 1 END) AS countNotDoneOver "
				+ "from WO wo WHERE wo.WO_TYPE_ID = 241 ");
		if (StringUtils.isNotEmpty(woDTO.getCdLevel2())) {
			sql.append(" AND wo.CD_LEVEL_2 = :cdLevel2Id ");
		}

		if (StringUtils.isNotEmpty(woDTO.getStartTimeStr())) {
			sql.append(" AND wo.START_TIME >= TO_DATE(:startTime, 'dd/mm/yyyy')");
		}

		if (StringUtils.isNotEmpty(woDTO.getEndTimeStr())) {
			sql.append(" AND wo.START_TIME <= TO_DATE(:endTime, 'dd/mm/yyyy')");
		}

		// StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		// sqlCount.append(sql.toString());
		// sqlCount.append(")");
		sql.append("  GROUP BY CD_LEVEL_2_NAME, FT_NAME ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		// SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		if (StringUtils.isNotEmpty(woDTO.getCdLevel2())) {
			query.setParameter("cdLevel2Id", woDTO.getCdLevel2());
			// queryCount.setParameter("name",
			// "%"+woScheduleConfigDTO.getScheduleConfigName().toLowerCase()+"%");
		}

		if (StringUtils.isNotEmpty(woDTO.getStartTimeStr())) {
			query.setParameter("startTime", woDTO.getStartTimeStr());
		}

		if (StringUtils.isNotEmpty(woDTO.getEndTimeStr())) {
			query.setParameter("endTime", woDTO.getEndTimeStr());
		}

		query = mapFieldsReport5s(query);

		if (woDTO.getPage() != null && woDTO.getPageSize() != null) {
			query.setFirstResult((woDTO.getPage().intValue() - 1) * woDTO.getPageSize().intValue());
			query.setMaxResults(woDTO.getPageSize().intValue());
		}

		// woDTO.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		return query.list();
	}

	private SQLQuery mapFieldsReport5s(SQLQuery query) {
		query.addScalar("cdLevel2Name", new StringType());
		query.addScalar("ftName", new StringType());
		query.addScalar("totalRecord5s", new LongType());
		query.addScalar("countDone", new LongType());
		query.addScalar("countDoneOver", new LongType());
		query.addScalar("countNotDone", new LongType());
		query.addScalar("countNotDoneOver", new LongType());
		query.setResultTransformer(Transformers.aliasToBean(Report5sDTO.class));
		return query;
	}

	public List<WoSimpleSysGroupDTO> getCnkt() {
		StringBuilder sql = new StringBuilder("select SYS_GROUP_ID as sysGroupId, GROUP_NAME_LEVEL2 as groupNameLv2, "
				+ " GROUP_NAME_LEVEL3 as groupNameLv3, NAME as groupName, " + " PARENT_ID as parentId "
				+ " from SYS_GROUP where GROUP_LEVEL = 2 and CODE LIKE '%CNCT.%' ");
		sql.append(" ORDER BY groupName ");

		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("sysGroupId", new LongType());
		query.addScalar("groupName", new StringType());
		query.addScalar("groupNameLv2", new StringType());
		query.addScalar("groupNameLv3", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(WoSimpleSysGroupDTO.class));
		return query.list();
	}

	public boolean checkFtDebt(Long ftId) {
		String sql = "select nvl(b.check_debt,0) as checkDebt from sys_user a "
				+ "left join staff_ft_debt_wo b on a.employee_code = b.staff_code where 1=1 ";
		if (ftId != null)
			sql += " and a.sys_user_id = :ftId ";

		SQLQuery query = getSession().createSQLQuery(sql);

		if (ftId != null)
			query.setParameter("ftId", ftId);

		query.addScalar("checkDebt", new LongType());

		Long result = (Long) query.uniqueResult();

		if (result > 0)
			return true;
		return false;
	}

	public boolean checkAIOFtDebt(Long ftId) {
		String sql = "select count(*) as count from CTCT_KCS_OWNER.AIO_CONTRACT c "
				+ "LEFT join CTCT_KCS_OWNER.AIO_CONTRACT_PERFORM_DATE p on c.CONTRACT_ID = p.CONTRACT_ID "
				+ "where STATUS = 3 and IS_PAY is null and trunc(p.END_DATE) + c.NUMBER_PAY < trunc(sysdate) ";

		if (ftId != null)
			sql += " and (SELLER_ID = :ftId or PERFORMER_ID = :ftId) ";

		SQLQuery query = getSession().createSQLQuery(sql);

		if (ftId != null)
			query.setParameter("ftId", ftId);

		query.addScalar("count", new LongType());

		Long result = (Long) query.uniqueResult();

		if (result > 0)
			return true;
		return false;
	}

	public WoSimpleSysGroupDTO getParentGroupByName(Long id) {
		String sql = " select SYS_GROUP_ID as sysGroupId, NAME as groupName, GROUP_LEVEL as groupLevel "
				+ " from SYS_GROUP where status>0 "
				+ " and PARENT_ID = (select parent_id from sys_group where sys_group_id = :id ) "
				+ " and NAME LIKE '%Ban Giám đốc%' ";
		SQLQuery query = getSession().createSQLQuery(sql);

		query.setParameter("id", id);

		query.addScalar("groupName", new StringType());
		query.addScalar("sysGroupId", new LongType());
		query.addScalar("groupLevel", new IntegerType());
		query.setResultTransformer(Transformers.aliasToBean(WoSimpleSysGroupDTO.class));

		return (WoSimpleSysGroupDTO) query.uniqueResult();
	}

	public void tryCompleteWorkItem(WoBO bo) {
		Double moneyValue = bo.getMoneyValue();
		Long consId = bo.getConstructionId();
		Long catWorkItemTypeId = bo.getCatWorkItemTypeId();
		String sql = "UPDATE WORK_ITEM set status = 3, complete_date = SYSDATE, quantity = :moneyValue "
				+ " where construction_id = :consId and cat_work_item_type_id = :catWorkItemTypeId ";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("moneyValue", moneyValue);
		query.setParameter("consId", consId);
		query.setParameter("catWorkItemTypeId", catWorkItemTypeId);

		query.executeUpdate();
	}

	public void tryUncompleteWorkItem(WoBO bo) {
		if (bo.getWoTypeId() != 1)
			return;
		Long consId = bo.getConstructionId();
		Long catWorkItemTypeId = bo.getCatWorkItemTypeId();
		String sql = "UPDATE WORK_ITEM set status = 2, complete_date = NULL, quantity = NULL "
				+ " where construction_id = :consId and cat_work_item_type_id = :catWorkItemTypeId ";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("consId", consId);
		query.setParameter("catWorkItemTypeId", catWorkItemTypeId);

		query.executeUpdate();
	}

	public List<Long> getAllWorkItemStatusByConstruction(Long constructionId) {
		String sql = "Select status from work_item where construction_id = :constructionId ";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("constructionId", constructionId);
		query.addScalar("status", new LongType());
		return (List<Long>) query.list();
	}

	public void tryCompleteConstruction(Long constructionId) {
//		taotq start 01072022
		Boolean check = checkWo(constructionId);
		String sql = "";
		if(check) {
			sql = " update construction set status = -5 where construction_id = :constructionId ";
		}else {
			sql = " update construction set status = 5 where construction_id = :constructionId ";
		}
//		String sql = " update construction set status = 5 where construction_id = :constructionId ";
//		taotq end 01072022
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("constructionId", constructionId);
		query.executeUpdate();
	}

	public void tryUncompleteConstruction(Long constructionId) {
		String sql = " update construction set status = 3 where construction_id = :constructionId ";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("constructionId", constructionId);
		query.executeUpdate();
	}

	public List<WoSimpleSysGroupDTO> getCDCnkt() {
		StringBuilder sql = new StringBuilder(
				"SELECT  SYS_GROUP_ID as sysGroupId, NAME as groupName FROM SYS_GROUP WHERE CODE LIKE '%CNCT%' AND GROUP_LEVEL =2 ORDER BY NAME");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("sysGroupId", new LongType());
		query.addScalar("groupName", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(WoSimpleSysGroupDTO.class));
		return query.list();
	}

	// Huypq-24112020-start
	public List<WoDTO> exportDoneWo(String type, List<String> listData) {
		StringBuilder sql = new StringBuilder("SELECT w.WO_CODE as woCode, " + "  wt.WO_TYPE_NAME as woTypeName, "
				+ "  (select ap.name from APP_PARAM ap where to_char(w.AP_WORK_SRC) = ap.CODE  AND ap.PAR_TYPE = 'AP_WORK_SRC' AND ap.status  = 1 ) apWorkSrcName, "
				+ "  NVL(w.contract_code,w.project_code) contractCode, " + "  w.STATION_CODE stationCode, "
				+ "  w.CONSTRUCTION_CODE constructionCode, "
				+ "  (SELECT cwt.NAME from   CAT_WORK_ITEM_TYPE cwt where w.CAT_WORK_ITEM_TYPE_ID = cwt.CAT_WORK_ITEM_TYPE_ID  AND cwt.status  = 1) catWorkItemTypeName, "
				+ "  round(nvl(w.MONEY_VALUE,0) / 1000000,2) moneyValue, "
				+ "  to_char(w.FINISH_DATE,'dd/MM/yyyy') finishDateStr, "
				+ "  to_char(w.END_TIME,'dd/MM/yyyy') endTimeStr, "
				+ "  (SELECT sg1.name FROM sys_group sg1 WHERE sg1.SYS_GROUP_ID = sg.parent_id " + "  ) cdLevel2Name, "
				+ "  w.FT_NAME ftName, " + "  su.EMPLOYEE_CODE ftCode " + " FROM WO_TYPE wt "
				+ "  INNER JOIN WO w on wt.id          =w.wo_type_id "
				+ "  INNER JOIN sys_group sg on w.CD_LEVEL_2  = sg.SYS_GROUP_ID " + "  LEFT JOIN CONSTRUCTION cons "
				+ "  ON w.CONSTRUCTION_ID = cons.CONSTRUCTION_ID "
				+ "  LEFT JOIN SYS_USER su ON w.FT_ID = su.SYS_USER_ID " + "	WHERE  " + " (wt.WO_TYPE_CODE = 'HSHC' "
				+
				// " OR (wt.WO_TYPE_CODE = 'THICONG' AND cons.CAT_CONSTRUCTION_TYPE_ID not in
				// (select CAT_CONSTRUCTION_TYPE_ID from CAT_CONSTRUCTION_TYPE where code in
				// (2,3) ))) " +
				"	OR (wt.WO_TYPE_CODE  = 'THICONG' AND w.CAT_CONSTRUCTION_TYPE_ID not in (2,3)))  "
				+ " AND w.CD_LEVEL_1           IS NOT NULL " + " AND sg.status               = 1 "
				+ " AND wt.status               = 1 " + " AND w.status                = 1 "
				+ " AND w.FT_ID is not null ");
		sql.append(" AND w.state in ('ASSIGN_FT', 'ACCEPT_FT', 'PROCESSING') ");
		sql.append(" AND w.AP_WORK_SRC = 2 ");
		sql.append(
				" AND (w.CD_LEVEL_2 in (:groupIdList) OR w.CD_LEVEL_3 in (:groupIdList) OR w.CD_LEVEL_4 in (:groupIdList)) ");

		sql.append(" ORDER BY w.WO_CODE,wt.WO_TYPE_NAME");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		if (listData.size() > 0) {
			query.setParameterList("groupIdList", listData);
		}
		query.addScalar("woCode", new StringType());
		query.addScalar("woTypeName", new StringType());
		query.addScalar("apWorkSrcName", new StringType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("stationCode", new StringType());
		query.addScalar("constructionCode", new StringType());
		query.addScalar("catWorkItemTypeName", new StringType());
		query.addScalar("moneyValue", new DoubleType());
		query.addScalar("finishDateStr", new StringType());
		query.addScalar("endTimeStr", new StringType());
		query.addScalar("cdLevel2Name", new StringType());
		query.addScalar("ftName", new StringType());
		query.addScalar("ftCode", new StringType());

		query.setResultTransformer(Transformers.aliasToBean(WoDTO.class));
		List<WoDTO> lst = query.list();
		return lst;
	}

	public Long updateStateWo(WoDTO dto, KttsUserSession userSession) {
		StringBuilder sql = new StringBuilder("update wo set state=:state, " + "END_TIME=sysdate, "
				+ "ACCEPT_TIME=sysdate, " + "START_TIME=sysdate ");
		if (dto.getUpdateFtReceiveWo() == null) {
			sql.append(" ,UPDATE_FT_RECEIVE_WO=sysdate ");
		}
		if (!StringUtils.isNotBlank(dto.getUserFtReceiveWo())) {
			sql.append(" ,USER_FT_RECEIVE_WO =:userLoginId ");
		}
		sql.append(" where wo_code=:woCode");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.setParameter("woCode", dto.getWoCode());
		query.setParameter("state", dto.getState());
		if (!StringUtils.isNotBlank(dto.getUserFtReceiveWo())) {
			query.setParameter("userLoginId", userSession.getVpsUserInfo().getSysUserId());
		}

		return (long) query.executeUpdate();
	}

	public Map<String, Long> getCatWorkItemTypeIdByName(List<String> lstName) {
		Map<String, Long> mapNameId = new HashMap<String, Long>();
		StringBuilder sql = new StringBuilder(
				"SELECT CAT_WORK_ITEM_TYPE_ID catWorkItemTypeId, NAME name from CAT_WORK_ITEM_TYPE where name in (:lstName) ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("catWorkItemTypeId", new LongType());
		query.addScalar("name", new StringType());

		query.setParameterList("lstName", lstName);

		query.setResultTransformer(Transformers.aliasToBean(WoCatWorkItemTypeDTO.class));

		List<WoCatWorkItemTypeDTO> ls = query.list();

		for (WoCatWorkItemTypeDTO work : ls) {
			mapNameId.put(work.getName(), work.getCatWorkItemTypeId());
		}

		return mapNameId;
	}

	public Map<String, Long> getSysUserIdByCode(List<String> lstName) {
		Map<String, Long> mapNameId = new HashMap<String, Long>();
		StringBuilder sql = new StringBuilder(
				"SELECT SYS_USER_ID sysUserId, EMPLOYEE_CODE employeeCode from SYS_USER where EMPLOYEE_CODE in (:lstName) ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("sysUserId", new LongType());
		query.addScalar("employeeCode", new StringType());

		query.setParameterList("lstName", lstName);

		query.setResultTransformer(Transformers.aliasToBean(SysUserDTO.class));

		List<SysUserDTO> ls = query.list();

		for (SysUserDTO work : ls) {
			mapNameId.put(work.getEmployeeCode(), work.getSysUserId());
		}

		return mapNameId;
	}

	public Map<String, String> getSysGroupIdByName(List<String> lstName) {
		Map<String, String> mapNameId = new HashMap<String, String>();
		StringBuilder sql = new StringBuilder(
				"SELECT SYS_GROUP_ID groupId, NAME name from SYS_GROUP where NAME in (:lstName) ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("groupId", new LongType());
		query.addScalar("name", new StringType());

		query.setParameterList("lstName", lstName);

		query.setResultTransformer(Transformers.aliasToBean(SysGroupDto.class));

		List<SysGroupDto> ls = query.list();

		for (SysGroupDto work : ls) {
			mapNameId.put(work.getName(), work.getGroupId().toString());
		}

		return mapNameId;
	}

	public Map<String, Long> getConsTypeByConsCode(List<String> lstName) {
		Map<String, Long> mapNameId = new HashMap<String, Long>();
		StringBuilder sql = new StringBuilder(
				"SELECT CAT_CONSTRUCTION_TYPE_ID catConstructionTypeId, CODE code from CONSTRUCTION where UPPER(CODE) in (:lstName) ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("catConstructionTypeId", new LongType());
		query.addScalar("code", new StringType());

		query.setParameterList("lstName", lstName);

		query.setResultTransformer(Transformers.aliasToBean(ConstructionDetailDTO.class));

		List<ConstructionDetailDTO> ls = query.list();

		for (ConstructionDetailDTO work : ls) {
			if (work.getCode() != null) {
				mapNameId.put(work.getCode().toUpperCase(), work.getCatConstructionTypeId());
			}
		}

		return mapNameId;
	}

	public Map<String, String> getStateByConsCode(List<String> lstName) {
		Map<String, String> mapNameId = new HashMap<String, String>();
		StringBuilder sql = new StringBuilder(
				"SELECT STATE state, WO_CODE woCode from wo where Upper(CONSTRUCTION_CODE) in (:lstName) ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("state", new StringType());
		query.addScalar("woCode", new StringType());

		query.setParameterList("lstName", lstName);

		query.setResultTransformer(Transformers.aliasToBean(WoDTO.class));

		List<WoDTO> ls = query.list();

		for (WoDTO work : ls) {
			if (work.getWoCode() != null) {
				mapNameId.put(work.getWoCode().toUpperCase(), work.getState());
			}
		}

		return mapNameId;
	}

	public Map<String, String> getStateByWoCode(List<String> lstName) {
		Map<String, String> mapNameId = new HashMap<String, String>();
		StringBuilder sql = new StringBuilder("SELECT wo.WO_CODE woCode, su.EMPLOYEE_CODE ftCode " + "from wo "
				+ "left join sys_user su on wo.FT_ID = su.SYS_USER_ID " + "where Upper(wo.WO_CODE) in (:lstName) ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("woCode", new StringType());
		query.addScalar("ftCode", new StringType());

		query.setParameterList("lstName", lstName);

		query.setResultTransformer(Transformers.aliasToBean(WoDTO.class));

		List<WoDTO> ls = query.list();

		for (WoDTO work : ls) {
			if (work.getWoCode() != null) {
				mapNameId.put(work.getWoCode().toUpperCase(), work.getFtCode());
			}
		}

		return mapNameId;
	}

	public String getConfigFromAioConfigService(String code) {
		StringBuilder sql = new StringBuilder(
				"SELECT nvl(CONFIG_WO,0) configWo FROM CTCT_KCS_OWNER.AIO_CONFIG_SERVICE WHERE ROWNUM=1 AND CODE =:code");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("configWo", new StringType());
		query.setParameter("code", code);

		return (String) query.uniqueResult();
	}
	// Huy-end

	public boolean isDoneConstruction(Long consId) {
		if (consId == null)
			return false;

		String sql = "select count(*) as total from work_item where construction_id = :consId and status > 0 ";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("consId", consId);
		query.addScalar("total", new LongType());
		Long total = (Long) query.uniqueResult();

		String sql2 = "select count(*) as completed from work_item where construction_id = :consId and status = 3 ";
		SQLQuery query2 = getSession().createSQLQuery(sql2);
		query2.setParameter("consId", consId);
		query2.addScalar("completed", new LongType());
		Long completed = (Long) query.uniqueResult();

		if (total != null && total != 0 && total == completed)
			return true;

		return false;

	}

	public boolean isContractCanCreateHSHC(Long contractId) {
		// hợp đồng chi phí ngoài os
		// String sql = "select count(*) as total from cnt_contract where contract_type
		// = 0 and contract_type_o = 2 and cnt_contract_id = :contractId and status>0 ";

		// tất cả hđ từ ttht trừ hd xddd
		// Huypq-08122021-thêm cho GPTH contract_branch=1
		String sql = "select count(*) as total from cnt_contract where contract_branch in (1,2,5) and CONTRACT_TYPE_O in (1,2,3,15,16,17,5,6,7,9,14) and cnt_contract_id = :contractId and status>0 ";

		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("contractId", contractId);
		query.addScalar("total", new LongType());
		Long total = (Long) query.uniqueResult();

		if (total > 0)
			return true;
		return false;
	}

	public List<String> getContractStationList(List<String> contractCodes) {
		StringBuilder sql = new StringBuilder(" select ct.CODE || '-' || s.CODE as pairContractStation"
				+ " from CNT_CONTRACT ct "
				+ " left join CNT_CONSTR_WORK_ITEM_TASK cc on cc.CNT_CONTRACT_ID = ct.CNT_CONTRACT_ID and cc.status != 0 "
				+ " left join CONSTRUCTION cons on cons.CONSTRUCTION_ID = cc.CONSTRUCTION_ID and cons.STATUS != 0 "
				+ " left join CAT_STATION s on cons.CAT_STATION_ID = s.CAT_STATION_ID and s.STATUS != 0 "
				+ " left join cat_province p on s.cat_province_id = p.cat_province_id and p.status != 0 "
				+ " where ct.CODE in (:contractCodes) ");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameterList("contractCodes", contractCodes);
		query.addScalar("pairContractStation", new StringType());
		return (List<String>) query.list();
	}

	public boolean checkConstructionIsB2B(Long consId) {
		if (consId == null)
			return false;
		String sql = "select b2b_b2c from construction where CONSTRUCTION_ID = :consId";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("consId", consId);
		query.addScalar("b2b_b2c", new StringType());

		String res = (String) query.uniqueResult();

		if ("1".equalsIgnoreCase(res))
			return true;
		return false;
	}

	public List<WoDTO> get5SWoByConstructionWorkItem(WoDTO dto) {
		if (dto.getConstructionId() == null || dto.getCatWorkItemTypeId() == null)
			return new ArrayList<>();

		String sql = "select wo.id woId, wo.finish_date finishDate from wo join wo_type on wo.wo_type_id = wo_type.id and wo_type.wo_type_code = '5S' "
				+ " where wo.status > 0 "
				+ " and wo.construction_id = :consId and wo.cat_work_item_type_id = :catWorkItemTypeId order by wo.finish_date desc";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("consId", dto.getConstructionId());
		query.setParameter("catWorkItemTypeId", dto.getCatWorkItemTypeId());
		query.addScalar("woId", new LongType());
		query.addScalar("finishDate", new DateType());

		query.setResultTransformer(Transformers.aliasToBean(WoDTO.class));

		return query.list();
	}

	public boolean checkContractIsNDT(Long contractId) {
		if (contractId == null)
			return false;

		String sql = "select cnt_contract_id as cntContractId from cnt_contract where contract_branch = 2 and CONTRACT_TYPE_O = 1 and status > 0 and cnt_contract_id = :contractId ";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("contractId", contractId);
		query.addScalar("cntContractId", new LongType());
		List<Long> ids = query.list();
		if (ids.size() == 0)
			return false;
		else
			return true;
	}

	public boolean checkContractIsHTCT(Long contractId) {
		if (contractId == null)
			return false;

		String sql = "select cnt_contract_id as cntContractId from cnt_contract where contract_branch = 3 and status > 0 and cnt_contract_id = :contractId ";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("contractId", contractId);
		query.addScalar("cntContractId", new LongType());
		List<Long> ids = query.list();
		if (ids.size() == 0)
			return false;
		else
			return true;
	}

	public boolean isContractCanCreate5S(Long contractId) {
		if (contractId == null)
			return false;

		String sql = "select cnt_contract_id as cntContractId from cnt_contract where ((xddd_type in (2,3,4,5) and contract_type_o = 4) or contract_type_o = 11) and status > 0 and cnt_contract_id = :contractId ";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("contractId", contractId);
		query.addScalar("cntContractId", new LongType());
		List<Long> ids = query.list();
		if (ids.size() == 0)
			return false;
		else
			return true;
	}

	public Long getHshcWoByConstruction(Long constructionId) {
		// Huypq-31052021-start
		// String sql = "Select id from wo where wo_type_id = 2 and construction_id =
		// :constructionId and status > 0 ";
		String sql = "Select id from wo where wo_type_id = 3 and construction_id = :constructionId and status > 0 ";
		// Huy-end
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("constructionId", constructionId);
		query.addScalar("id", new LongType());
		List<Long> result = query.list();
		if (result.size() == 0)
			return null;
		else
			return result.get(0);
	}

	public Double getCheckListCountXDDD(Long woId) {
		String sql = "SELECT count(*) total from WO_XDDD_CHECKLIST where  wo_id = :woId ";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("woId", woId);
		query.addScalar("total", new DoubleType());
		return (Double) query.uniqueResult();
	}

	// Huypq-13012021-start
	public WoDTO getCdLevel2FromProvinceId(Long provinceId) {
		StringBuilder sql = new StringBuilder(
				" select sys_group_id as cdLevel2, name cdLevel2Name from sys_group where status >0 and code like '%P.HT' and PROVINCE_ID =:provinceId fetch next 1 row only ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("provinceId", provinceId);
		query.addScalar("cdLevel2", new StringType());
		query.addScalar("cdLevel2Name", new StringType());

		query.setResultTransformer(Transformers.aliasToBean(WoDTO.class));

		List<WoDTO> ls = query.list();
		if (ls.size() > 0) {
			return ls.get(0);
		}
		return null;
	}

	public Long getProvinceByAreaId(Long areaId) {
		String sql = "select PROVINCE_ID provinceId from AIO_AREA where ROWNUM=1 AND AREA_ID=:areaId ";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("areaId", areaId);
		query.addScalar("provinceId", new LongType());
		return (Long) query.uniqueResult();
	}
	// Huy-end

	public boolean checkContractBelongToBranch(Long contractId, String branchCntCode) {
		String sql = "SELECT CNT_CONTRACT_ID id from CNT_CONTRACT where CNT_CONTRACT_ID = :contractId and status > 0 ";

		if (branchCntCode.contains("1"))
			sql += " and contract_type=0 and contract_branch =1 ";
		if (branchCntCode.contains("2"))
			sql += " and contract_type=0 and contract_branch =2 ";
		if (branchCntCode.contains("3"))
			sql += " and contract_type=0 and contract_branch =3 ";
		if (branchCntCode.contains("4"))
			sql += " and contract_type=0 and contract_branch =4 ";
		if (branchCntCode.contains("5"))
			sql += " and contract_type=0 and contract_branch =5 ";
		if (branchCntCode.contains("6") || branchCntCode.contains("8"))
			sql += " and contract_type=6 ";
		if (branchCntCode.contains("7"))
			sql += " and contract_type=3 ";

		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("contractId", contractId);
		query.addScalar("id", new LongType());

		List<Long> result = query.list();

		if (result.size() == 0)
			return false;
		return true;
	}

	public List<Long> getInactiveWoIdListFromTr(Long trId) {
		String sql = "select id from wo where tr_id =:trId ";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("trId", trId);
		query.addScalar("id", new LongType());
		return query.list();
	}

	public List<WoDTO> getInactiveWoList(WoDTO searchObj) {
		if (searchObj.getTrId() == null)
			return new ArrayList<>();
		String sql = "select wo.id woId, wo.cat_work_item_type_id catWorkItemTypeId, wo.money_value moneyValue, wo.qouta_time qoutaTime, cwit.name catWorkItemTypeName, wo.quantity_value quantityValue "
				+ " from wo "
				+ " left join cat_work_item_type cwit on cwit.cat_work_item_type_id = wo.cat_work_item_type_id and cwit.status>0 "
				+ " where wo.tr_id =:trId ";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("trId", searchObj.getTrId());
		query.addScalar("woId", new LongType());
		query.addScalar("catWorkItemTypeId", new LongType());
		query.addScalar("catWorkItemTypeName", new StringType());
		query.addScalar("moneyValue", new DoubleType());
		query.addScalar("quantityValue", new StringType());
		query.addScalar("qoutaTime", new IntegerType());
		query.setResultTransformer(Transformers.aliasToBean(WoDTO.class));
		return query.list();
	}

	public WoSimpleSysUserDTO getContractCdLevel5(String email, Long contractId) {
		if (StringUtils.isEmpty(email) || contractId == null)
			return null;
		String sql = " select su.sys_user_id sysUserId, su.employee_code employeeCode, su.sys_group_id sysGroupId, su.full_name fullName, su.email, su.login_name loginName "
				+ " from sys_user su "
				+ " left join WO_CONFIG_CONTRACT_COMMITTEE wccc on wccc.user_id = su.sys_user_id and wccc.status>0 "
				+ " where su.status > 0 and su.email = :email and wccc.contract_id = :contractId and wccc.user_role = 1 ";

		SQLQuery query = getSession().createSQLQuery(sql);

		query.setParameter("email", email);
		query.setParameter("contractId", contractId);

		query.addScalar("sysUserId", new LongType());
		query.addScalar("employeeCode", new StringType());
		query.addScalar("sysGroupId", new LongType());
		query.addScalar("fullName", new StringType());
		query.addScalar("email", new StringType());
		query.addScalar("employeeCode", new StringType());

		query.setResultTransformer(Transformers.aliasToBean(WoSimpleSysUserDTO.class));
		List<WoSimpleSysUserDTO> result = query.list();

		if (result.size() > 0)
			return result.get(0);
		return null;
	}

	public List<WoSimpleFtDTO> getFtListCdLevel5(String contractCode, String keysearch) {
		StringBuilder sql = new StringBuilder("select \n" + "su.SYS_USER_ID as ftId,\n"
				+ "su.EMPLOYEE_CODE as employeeCode,\n" + "su.email as email,\n" + "su.FULL_NAME as fullName, \n"
				+ "su.SYS_GROUP_ID as sysGroupId\n" + "FROM SYS_USER su, WO_CONFIG_CONTRACT_COMMITTEE wcc \n"
				+ "WHERE \n" + "su.STATUS = 1\n" + "AND su.SYS_USER_ID = wcc.USER_ID\n" + "AND wcc.USER_ROLE =2 ");

		if (StringUtils.isNotEmpty(contractCode)) {
			sql.append(" AND wcc.CONTRACT_CODE = :contractCode ");
		}

		if (StringUtils.isNotEmpty(keysearch)) {
			sql.append(
					" and (lower(su.LOGIN_NAME) like :keysearch or lower(su.FULL_NAME) like :keysearch or lower(su.email) like :keysearch) fetch next 20 rows only ");
		} else {
			sql.append(" order by su.FULL_NAME ");
		}

		SQLQuery query = getSession().createSQLQuery(sql.toString());

		if (StringUtils.isNotEmpty(contractCode)) {
			query.setParameter("contractCode", contractCode);
		}

		if (StringUtils.isNotEmpty(keysearch)) {
			query.setParameter("keysearch", "%" + keysearch.toLowerCase() + "%");
		}

		query.addScalar("ftId", new LongType());
		query.addScalar("sysGroupId", new LongType());
		query.addScalar("fullName", new StringType());
		query.addScalar("employeeCode", new StringType());
		query.addScalar("email", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(WoSimpleFtDTO.class));
		return query.list();
	}

	public int updateWoCheckListOK(Double moneyValue, Long userId, String userName, Long woId) {
		int result = 0;
		try {
			StringBuilder sql = new StringBuilder(
					" UPDATE WO_CHECKLIST set FINAL_VALUE = :moneyValue ,FINAL_DATE =sysdate ,COMPLETED_DATE= sysdate, COMPLETE_PERSON_ID = :userId,COMPLETE_PERSON_NAME=:userName ,state='DONE'"
							+ " WHERE WO_ID = :woId " + " AND CHECKLIST_ORDER = 5 ");
			SQLQuery query = this.getSession().createSQLQuery(sql.toString());
			query.setParameter("moneyValue", moneyValue);
			query.setParameter("userId", userId);
			query.setParameter("userName", userName);
			query.setParameter("woId", woId);
			result = query.executeUpdate();
		} catch (Exception ex) {
			//ex.printStackTrace();
			result = 0;
		}
		return result;
	}

	public void removeInactiveWo(Long woId) {
		String sql = " DELETE from WO where id = :woId and status = 0 ";
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.setParameter("woId", woId);
		query.executeUpdate();
	}

	public List<WoSimpleFtDTO> getFtListLevel5() {
		StringBuilder sql = new StringBuilder(" SELECT SYS_USER_ID AS ftId,\n" + "  EMPLOYEE_CODE    AS employeeCode,\n"
				+ "  email            AS email,\n" + "  FULL_NAME        AS fullName,\n"
				+ "  SYS_GROUP_ID     AS sysGroupId\n" + "FROM SYS_USER\n" + "WHERE STATUS      = 1\n"
				+ "AND Sys_Group_id IN (SELECT SYS_GROUP_ID FROM Sys_Group WHERE Parent_Id IN (9006003))");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("ftId", new LongType());
		query.addScalar("sysGroupId", new LongType());
		query.addScalar("fullName", new StringType());
		query.addScalar("employeeCode", new StringType());
		query.addScalar("email", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(WoSimpleFtDTO.class));
		return query.list();
	}

	public List<WoGeneralReportDTO> getGeneralReportXDDTHT(WoGeneralReportDTO dto) {
		StringBuilder sql = new StringBuilder("       SELECT  su.FULL_NAME ftName, w.CD_LEVEL_5 ftId\n"
				+ "         ,count(case when w.status = 1 then 1 end) as totalWO \n"
				+ "         , count(case when w.state ='UNASSIGN' then 1 end) as totalUnassign \n"
				+ "         , count(case when w.state ='ASSIGN_CD' then 1 end) as totalAssignCd \n"
				+ "         , count(case when w.state ='ACCEPT_CD' then 1 end) as totalAcceptCd \n"
				+ "         , count(case when w.state ='REJECT_CD' then 1 end) as totalRejectCd \n"
				+ "         , count(case when w.state ='ASSIGN_FT' then 1 end) as totalAssignFt \n"
				+ "         , count(case when w.state ='ACCEPT_FT' then 1 end) as totalAcceptFt \n"
				+ "         , count(case when w.state ='REJECT_FT' then 1 end) as totalRejectFt \n"
				+ "         , count(case when w.state ='PROCESSING' then 1 end) as totalProcessing \n"
				+ "         , count(case when w.state ='DONE' then 1 end) as totalDone \n"
				+ "         , count(case when w.state ='CD_OK' then 1 end) as totalCdOk \n"
				+ "         , count(case when w.state ='CD_NG' then 1 end) as totalCdNotGood \n"
				+ "         , count(case when w.state ='OK'  then 1 end) as totalOk \n"
				+ "         , count(case when w.state ='NG' then 1 end) as totalNotGood \n"
				+ "         , count(case when w.state like '%OPINION_RQ%' then 1 end) as totalOpinionRequest \n"
				+ "         , count(case when trunc(w.finish_date) < nvl(trunc(w.end_time),trunc(sysdate)) and w.state not in ('OK','CD_OK','DONE') then 1 end) as totalOverDue \n"
				+ "         , count(case when trunc(w.finish_date) < nvl(trunc(w.end_time),trunc(sysdate)) and w.state  in ('OK','CD_OK','DONE') then 1 end) as totalFinishOverDue  from sys_user su, wo w \n"
				+ "         WHERE su.SYS_USER_ID = w.CD_LEVEL_5\n" + "         AND w.STATUS <> 0 \n"
				+ "         AND w.CD_LEVEL_5 is NOT null  AND CD_LEVEL_1 = '9006003' ");

		if (dto.getFromDate() != null) {
			sql.append(" and w.created_date >= :fromDate ");
		}

		if (dto.getToDate() != null) {
			sql.append(" and w.created_date <= :toDate ");
		}

		if (StringUtils.isNotEmpty(dto.getFtName())) {
			sql.append(
					" AND (lower(su.LOGIN_NAME) like :keysearch or lower(su.FULL_NAME) like :keysearch or lower(su.email) like :keysearch) ");
		}
		sql.append("GROUP BY su.FULL_NAME,w.CD_LEVEL_5");

		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");

		StringBuilder sqlTotalQuantity = new StringBuilder("SELECT " + " NVL(sum(totalWO), 0) as totalAllWO, "
				+ " NVL(sum(totalUnassign), 0) as totalAllUnassign, "
				+ " NVL(sum(totalAssignCd), 0) as totalAllAssignCd, "
				+ " NVL(sum(totalAcceptCd), 0) as totalAllAcceptCd, "
				+ " NVL(sum(totalRejectCd), 0) as totalAllRejectCd, "
				+ " NVL(sum(totalAssignFt), 0) as totalAllAssignFt, "
				+ " NVL(sum(totalAcceptFt), 0) as totalAllAcceptFt, "
				+ " NVL(sum(totalRejectFt), 0) as totalAllRejectFt, "
				+ " NVL(sum(totalProcessing), 0) as totalAllProcessing, " + " NVL(sum(totalDone), 0) as totalAllDone, "
				+ " NVL(sum(totalCdOk), 0) as totalAllCdOk, " + " NVL(sum(totalCdNotGood), 0) as totalAllCdNotGood, "
				+ " NVL(sum(totalOk), 0) as totalAllOk, " + " NVL(sum(totalNotGood), 0) as totalAllNotGood, "
				+ " NVL(sum(totalOpinionRequest), 0) as totalAllOpinionRequest, "
				+ " NVL(sum(totalOverDue), 0) as totalAllOverDue, "
				+ " NVL(sum(totalFinishOverDue), 0) as totalFinishOverDue " + " FROM (");
		sqlTotalQuantity.append(sql);
		sqlTotalQuantity.append(")");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
		SQLQuery queryQuantity = getSession().createSQLQuery(sqlTotalQuantity.toString());

		query.addScalar("ftName", new StringType());
		query.addScalar("ftId", new LongType());
		query.addScalar("totalWO", new LongType());
		query.addScalar("totalUnassign", new LongType());
		query.addScalar("totalAssignCd", new LongType());
		query.addScalar("totalAcceptCd", new LongType());
		query.addScalar("totalRejectCd", new LongType());
		query.addScalar("totalAssignFt", new LongType());
		query.addScalar("totalAcceptFt", new LongType());
		query.addScalar("totalRejectFt", new LongType());
		query.addScalar("totalProcessing", new LongType());
		query.addScalar("totalDone", new LongType());
		query.addScalar("totalCdOk", new LongType());
		query.addScalar("totalCdNotGood", new LongType());
		query.addScalar("totalOk", new LongType());
		query.addScalar("totalNotGood", new LongType());
		query.addScalar("totalOpinionRequest", new LongType());
		query.addScalar("totalOverDue", new LongType());
		query.addScalar("totalFinishOverDue", new LongType());

		queryQuantity.addScalar("totalAllWO", new LongType());
		queryQuantity.addScalar("totalAllUnassign", new LongType());
		queryQuantity.addScalar("totalAllAssignCd", new LongType());
		queryQuantity.addScalar("totalAllAcceptCd", new LongType());
		queryQuantity.addScalar("totalAllRejectCd", new LongType());
		queryQuantity.addScalar("totalAllAssignFt", new LongType());
		queryQuantity.addScalar("totalAllAcceptFt", new LongType());
		queryQuantity.addScalar("totalAllRejectFt", new LongType());
		queryQuantity.addScalar("totalAllProcessing", new LongType());
		queryQuantity.addScalar("totalAllDone", new LongType());
		queryQuantity.addScalar("totalAllCdOk", new LongType());
		queryQuantity.addScalar("totalAllCdNotGood", new LongType());
		queryQuantity.addScalar("totalAllOk", new LongType());
		queryQuantity.addScalar("totalAllNotGood", new LongType());
		queryQuantity.addScalar("totalAllOpinionRequest", new LongType());
		queryQuantity.addScalar("totalAllOverDue", new LongType());
		queryQuantity.addScalar("totalFinishOverDue", new LongType());

		if (dto.getFromDate() != null) {
			query.setParameter("fromDate", dto.getFromDate());
			queryCount.setParameter("fromDate", dto.getFromDate());
			queryQuantity.setParameter("fromDate", dto.getFromDate());
		}

		if (dto.getToDate() != null) {
			query.setParameter("toDate", dto.getToDate());
			queryCount.setParameter("toDate", dto.getToDate());
			queryQuantity.setParameter("toDate", dto.getToDate());
		}

		if (StringUtils.isNotEmpty(dto.getFtName())) {
			query.setParameter("keysearch", dto.getFtName());
			queryCount.setParameter("keysearch", dto.getFtName());
			queryQuantity.setParameter("keysearch", dto.getFtName());
		}

		query.setResultTransformer(Transformers.aliasToBean(WoGeneralReportDTO.class));
		queryQuantity.setResultTransformer(Transformers.aliasToBean(WoGeneralReportDTO.class));

		if (dto.getPage() != null && dto.getPageSize() != null) {
			query.setFirstResult((dto.getPage().intValue() - 1) * dto.getPageSize().intValue());
			query.setMaxResults(dto.getPageSize().intValue());
		}

		dto.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		List<WoGeneralReportDTO> reports = query.list();
		WoGeneralReportDTO reportTotalAll = (WoGeneralReportDTO) queryQuantity.uniqueResult();

		if (reports.size() > 0) {
			reports.get(0).setTotalAllWO(reportTotalAll.getTotalAllWO());
			reports.get(0).setTotalAllUnassign(reportTotalAll.getTotalAllUnassign());
			reports.get(0).setTotalAllAssignCd(reportTotalAll.getTotalAllAssignCd());
			reports.get(0).setTotalAllAcceptCd(reportTotalAll.getTotalAllAcceptCd());
			reports.get(0).setTotalAllRejectCd(reportTotalAll.getTotalAllRejectCd());
			reports.get(0).setTotalAllAssignFt(reportTotalAll.getTotalAllAssignFt());
			reports.get(0).setTotalAllAcceptFt(reportTotalAll.getTotalAllAcceptFt());
			reports.get(0).setTotalAllRejectFt(reportTotalAll.getTotalAllRejectFt());
			reports.get(0).setTotalAllProcessing(reportTotalAll.getTotalAllProcessing());
			reports.get(0).setTotalAllDone(reportTotalAll.getTotalAllDone());
			reports.get(0).setTotalAllCdOk(reportTotalAll.getTotalAllCdOk());
			reports.get(0).setTotalAllCdNotGood(reportTotalAll.getTotalAllCdNotGood());
			reports.get(0).setTotalAllOk(reportTotalAll.getTotalAllOk());
			reports.get(0).setTotalAllNotGood(reportTotalAll.getTotalAllNotGood());
			reports.get(0).setTotalAllOpinionRequest(reportTotalAll.getTotalAllOpinionRequest());
			reports.get(0).setTotalAllOverDue(reportTotalAll.getTotalAllOverDue());
			reports.get(0).setTotalAllTotalFinishOverDue(reportTotalAll.getTotalFinishOverDue());
		}

		return reports;
	}

	public List<WoDTO> getWoDetailReportXDDTHT(WoGeneralReportDTO dto) {
		StringBuilder sql = new StringBuilder(" SELECT w.CD_LEVEL_5_NAME cdLevel5Name,\n" + "w.FT_NAME ftName,\n"
				+ "w.WO_CODE woCode,\n" + "w.WO_NAME woName,\n"
				+ "(select NAME FROM APP_PARAM ap where ap.status = 1 AND ap.code = w.STATE and par_type='WO_XL_STATE') state,\n"
				+ "w.CONTRACT_CODE contractCode,\n" + "w.STATION_CODE stationCode,\n"
				+ "w.CONSTRUCTION_CODE constructionCode,\n" + "cwt.NAME catWorkItemTypeName,\n"
				+ "(select NAME FROM APP_PARAM ap where ap.status = 1 AND ap.code =w.AP_CONSTRUCTION_TYPE and par_type='AP_CONSTRUCTION_TYPE') catConstructionTypeName,\n"
				+ "(select NAME FROM APP_PARAM ap where ap.status = 1 AND ap.code =w.AP_WORK_SRC and par_type='AP_WORK_SRC') apWorkSrcName,\n"
				+ "TO_CHAR(w.FINISH_DATE,'DD/MM/YYYY')  finishDateStr,\n"
				+ "TO_CHAR(w.END_TIME,'DD/MM/YYYY') endTimeStr, \n"
				+ "TO_CHAR( w.UPDATE_CD_LEVEL5_RECEIVE_WO,'DD/MM/YYYY') updateCdLevel5ReceiveWoStr,\n"
				+ " TO_CHAR( w.APPROVE_DATE_REPORT_WO,'MM/YYYY') approveDateReportWoStr,\n"
				+ "wxc.NAME woNameFromConfig,\n" + "wxc.TOTAL_MONEY  moneyFlowValue,\n" + "wxc.VALUE moneyValue\n"
				+ "FROM CAT_WORK_ITEM_TYPE cwt,\n" + "WO_XDDD_CHECKLIST wxc,SYS_USER su,\n" + "WO w \n"
				+ "WHERE w.CD_LEVEL_5 IS NOT NULL \n" + "and wxc.WO_ID= w.ID\n" + "AND w.STATUS = 1\n"
				+ "AND cwt.CAT_WORK_ITEM_TYPE_ID = w.CAT_WORK_ITEM_TYPE_ID AND w.CD_LEVEL_5 = su.SYS_USER_ID AND CD_LEVEL_1 = '9006003'");

		if (dto.getFromDate() != null) {
			sql.append(" and w.created_date >= :fromDate ");
		}

		if (dto.getToDate() != null) {
			sql.append(" and w.created_date <= :toDate ");
		}

		if (StringUtils.isNotEmpty(dto.getFtName())) {
			sql.append(
					" AND (lower(su.LOGIN_NAME) like :keysearch or lower(su.FULL_NAME) like :keysearch or lower(su.EMAIL) like :keysearch) ");
		}
		sql.append(" ORDER BY w.CD_LEVEL_5_NAME,w.CD_LEVEL_5 ");

		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		query.addScalar("cdLevel5Name", new StringType());
		query.addScalar("ftName", new StringType());
		query.addScalar("woCode", new StringType());
		query.addScalar("woName", new StringType());
		query.addScalar("state", new StringType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("stationCode", new StringType());
		query.addScalar("constructionCode", new StringType());
		query.addScalar("catWorkItemTypeName", new StringType());
		query.addScalar("catConstructionTypeName", new StringType());
		query.addScalar("apWorkSrcName", new StringType());
		query.addScalar("finishDateStr", new StringType());
		query.addScalar("endTimeStr", new StringType());
		query.addScalar("updateCdLevel5ReceiveWoStr", new StringType());
		query.addScalar("approveDateReportWoStr", new StringType());
		query.addScalar("woNameFromConfig", new StringType());
		query.addScalar("moneyFlowValue", new DoubleType());
		query.addScalar("moneyValue", new DoubleType());

		if (dto.getFromDate() != null) {
			query.setParameter("fromDate", dto.getFromDate());
			queryCount.setParameter("fromDate", dto.getFromDate());
		}

		if (dto.getToDate() != null) {
			query.setParameter("toDate", dto.getToDate());
			queryCount.setParameter("toDate", dto.getToDate());
		}

		if (StringUtils.isNotEmpty(dto.getFtName())) {
			query.setParameter("keysearch", dto.getFtName());
			queryCount.setParameter("keysearch", dto.getFtName());
		}

		query.setResultTransformer(Transformers.aliasToBean(WoDTO.class));

		if (dto.getPage() != null && dto.getPageSize() != null) {
			query.setFirstResult((dto.getPage().intValue() - 1) * dto.getPageSize().intValue());
			query.setMaxResults(dto.getPageSize().intValue());
		}

		dto.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		List<WoDTO> reports = query.list();

		return reports;
	}

	public List<WoDTO> getLstWoOfStationHouses(Long woId) {
		String sql = "select\n" + "    w.id woId\n" + "    , w.state\n" + "    , w.contract_code contractCode\n"
				+ "    , cs.CAT_STATION_HOUSE_ID catStationHouseId\n" + "from WO w\n"
				+ "left join CAT_STATION cs on w.STATION_CODE = cs.CODE\n"
				+ "left join CAT_STATION_HOUSE csh on cs.CAT_STATION_HOUSE_ID = csh.CAT_STATION_HOUSE_ID\n"
				+ "where 1 = 1\n" + "and w.WO_TYPE_ID = 1\n" + "and csh.CAT_STATION_HOUSE_ID = (\n"
				+ "    select c.CAT_STATION_HOUSE_ID\n" + "    from WO a\n"
				+ "    left join CAT_STATION b on a.STATION_CODE = b.CODE AND b.STATUS=1 \n"
				+ "    left join CAT_STATION_HOUSE c on b.CAT_STATION_HOUSE_ID = c.CAT_STATION_HOUSE_ID\n"
				+ "    where a.id = :woId\n" + ")";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.addScalar("woId", new LongType());
		query.addScalar("state", new StringType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("catStationHouseId", new LongType());
		query.setParameter("woId", woId);
		query.setResultTransformer(Transformers.aliasToBean(WoDTO.class));
		List<WoDTO> lstWoOfStationHouses = query.list();
		return lstWoOfStationHouses;
	}

	public List<CatStationDTO> getCatStationForTmbt(String keySearch) {
		String sql = "SELECT\n" + "    st.cat_station_id catStationId,\n" + "    st.type type,\n"
				+ "    case when st.address is not null then st.address else st.code end address,\n"
				+ "    st.name name,\n" + "    st.code code\n" + "FROM\n" + "    ctct_cat_owner.cat_station st\n"
				+ "WHERE\n" + "    st.status = 1\n" + "    AND UPPER(st.code) LIKE :keySearch\n" + "    AND (\n"
				+ "        st.type = 1\n" + "        OR st.type = 2\n" + "    )\n" + "    AND ROWNUM <= 20\n";

		StringBuilder stringBuilder = new StringBuilder(sql);

		stringBuilder.append(" ORDER BY ST.CODE");

		SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
		query.addScalar("catStationId", new LongType());
		query.addScalar("address", new StringType());
		query.addScalar("code", new StringType());
		query.addScalar("name", new StringType());
		query.addScalar("type", new StringType());

		query.setResultTransformer(Transformers.aliasToBean(CatStationDTO.class));
		query.setParameter("keySearch", "%" + keySearch.toUpperCase() + "%");

		return query.list();
	}

	public List<CatStationDTO> getCatStationOfWoTmbt(Long woId, boolean isRent) {
		String sql = "SELECT\n" + "    st.cat_station_id catStationId,\n" + "    st.type type,\n"
				+ "    case when st.address is not null then st.address else st.code end address,\n"
				+ "    st.name name,\n" + "    st.code code,\n" + "    wms.status status,\n"
				+ "    wms.reason description\n" + "FROM wo_mapping_station wms\n"
				+ "LEFT JOIN ctct_cat_owner.cat_station st ON wms.CAT_STATION_ID = st.CAT_STATION_ID\n"
				+ "WHERE st.status = 1 \n";
		if (isRent) {
			sql += "  and wms.status = 1 ";
		}
		sql += " AND wms.WO_ID = :woId";

		StringBuilder stringBuilder = new StringBuilder(sql);
		SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
		query.addScalar("catStationId", new LongType());
		query.addScalar("address", new StringType());
		query.addScalar("code", new StringType());
		query.addScalar("name", new StringType());
		query.addScalar("type", new StringType());
		query.addScalar("status", new StringType());
		query.addScalar("description", new StringType());

		query.setResultTransformer(Transformers.aliasToBean(CatStationDTO.class));
		query.setParameter("woId", woId);

		return query.list();
	}

	public Long checkStationHasTthq(String stationCode) {
		StringBuilder sql = new StringBuilder("select count(*) countWo \n" + "from wo w\n"
				+ "left join WO_MAPPING_CHECKLIST wmc on w.id = wmc.wo_id\n"
				+ "where w.wo_type_id = (select id from wo_type where wo_type_code = 'TTHQ')\n"
				+ "and w.STATION_CODE = :stationCode\n" + "and lower(wmc.TTHQ_RESULT) = 'hiệu quả' ");
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		query.setParameter("stationCode", stationCode);
		query.addScalar("countWo", new LongType());

		return (Long) query.uniqueResult();
	}

	public List<WorkItemDTO> getListItem(String code) {
		List<WorkItemDTO> lst = new ArrayList<>();
		String sql = "select wi.NAME as name, wi.CODE as code, wi.CONSTRUCTION_ID as constructorId from WORK_ITEM wi INNER JOIN CONSTRUCTION c ON wi.CONSTRUCTION_ID = c.CONSTRUCTION_ID where c.CODE = :code";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.addScalar("name", new StringType());
		query.addScalar("code", new StringType());
		query.addScalar("constructorId", new LongType());
		query.setResultTransformer(Transformers.aliasToBean(WorkItemDTO.class));
		query.setParameter("code", code);
		lst = query.list();
		return lst;
	}

	public void tryUpdateConstructionPs(Long constructionId, Long catWorkId) {
		try {
			// Hang muc phat song = 3
			String sqlHm = "update work_item set status = 3 where construction_id = :constructionId and CAT_WORK_ITEM_TYPE_ID = :catWorkId  ";
			SQLQuery queryHm = getSession().createSQLQuery(sqlHm);
			queryHm.setParameter("constructionId", constructionId);
			queryHm.setParameter("catWorkId", catWorkId);
			queryHm.executeUpdate();

			// Cong trinh = 9
			String sql = "update CONSTRUCTION SET status = 9, BROADCASTING_DATE = sysdate WHERE CONSTRUCTION_ID = :constructionId  ";
			SQLQuery query = getSession().createSQLQuery(sql);
			query.setParameter("constructionId", constructionId);
			query.executeUpdate();
		} catch (Exception ex) {
			//ex.printStackTrace();
		}
	}

	public Double getMoneyValueOfDhbtWo(Long catStationId, Long catWorkItemTypeId) {
		StringBuilder sql = new StringBuilder(
				" select MAX(wo_value) woValue from CAT_STATION_WO_CONFIG_VALUE where cat_station_id = :catStationId and cat_work_item_type_id = :catWorkItemTypeId ");
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		query.setParameter("catStationId", catStationId);
		query.setParameter("catWorkItemTypeId", catWorkItemTypeId);
		query.addScalar("woValue", new DoubleType());
		return (Double) query.uniqueResult();
	}

	// Huypq-05072021-start
	public List<WoSimpleConstructionDTO> getDetailConstructionByListCode(List<String> lstConstructionCode) {

		StringBuilder sql = new StringBuilder(
				"select CONSTRUCTION_ID constructionId, CODE constructionCode, nvl(CHECK_HTCT,0) checkHtct "
						+ " from CONSTRUCTION where status!=0 and CODE in (:lstConstructionCode) ");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameterList("lstConstructionCode", lstConstructionCode);

		query.addScalar("constructionId", new LongType());
		query.addScalar("checkHtct", new LongType());
		query.addScalar("constructionCode", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(WoSimpleConstructionDTO.class));
		return query.list();
	}
	// Huy-end

	public List<WoChartDataDto> getChartDataByWoType(String woType, WoChartDataDto obj) throws Exception {
		List<WoChartDataDto> woChartDataDtos = new ArrayList<>();
		String baseSqlByWoType = getBaseSqlByWoType(woType);
		// todo filters go here
		StringBuilder sql = new StringBuilder(baseSqlByWoType);

		sql.append(" AND to_date(MONTH ||'/'|| YEAR, 'MM/yyyy') >=:dateFrom ");
		sql.append(" AND to_date(MONTH ||'/'|| YEAR, 'MM/yyyy') <=:dateTo ");

		// Huypq-19072021-start
		if (obj.getLstProvinceCode() != null && obj.getLstProvinceCode().size() > 0) {
			sql.append(" AND PROVINCECODE in (:lstProvinceCode) ");
		}
		// Huy-end

		SQLQuery query = this.getSession().createSQLQuery(sql.toString());

		// Huypq-19072021-start
		query.setParameter("dateFrom", obj.getDateFrom());
		query.setParameter("dateTo", obj.getDateTo());
		if (obj.getLstProvinceCode() != null && obj.getLstProvinceCode().size() > 0) {
			query.setParameterList("lstProvinceCode", obj.getLstProvinceCode());
		}
		// Huy-end

		query.setResultTransformer(Transformers.aliasToBean(WoChartDataDto.class));
		woChartDataDtos = query.list();
		return woChartDataDtos;
	}

	/**
	 * Get base sql for chart drawing data
	 */
	public String getBaseSqlByWoTypeBK08092021(String woType) throws Exception {
		String sql = null;
		switch (woType) {
		case TYPE_THUE_MAT_BANG:
			sql = "WITH TBL AS (\n" + " SELECT\n"
					+ "  SYS.PROVINCE_CODE, CAT.CODE STATIONCODE,A.APPROVE_DATE_REPORT_WO NGAYTHUE_MB \n"
					+ "  FROM WO A, WO_MAPPING_STATION B, CAT_STATION CAT, SYS_GROUP SYS, WO_TYPE WOTYPE\n" + " WHERE\n"
					+ "  A.ID = B.WO_ID AND A.WO_TYPE_ID = WOTYPE.ID\n" + "  AND WOTYPE.WO_TYPE_CODE = 'TMBT'\n"
					+ "  AND A.STATE = 'OK'\n" + "  AND A.STATUS = 1\n" + "  AND B.STATUS = 1\n"
					+ "  AND B.CAT_STATION_ID = CAT.CAT_STATION_ID\n" + "  AND A.CD_LEVEL_2 = SYS.SYS_GROUP_ID "
					+ "  AND A.APPROVE_DATE_REPORT_WO >= :dateFrom\r\n"
					+ " AND TRUNC(A.APPROVE_DATE_REPORT_WO) <= :dateTo) \n" + "SELECT\n"
					+ " A.PROVINCE_CODE \"maTinh\",\n" + " A.THUE_MAT_BANG \"keHoachVal\",\n" + " (\n" + " SELECT\n"
					+ "  COUNT(*)\n" + " FROM\n" + "  TBL\n" + "\n" + " WHERE\n"
					+ "  A.PROVINCE_CODE = TBL.PROVINCE_CODE\n"
					+ "  AND TO_CHAR(TBL.NGAYTHUE_MB, 'yyyyMM')= A.YEAR || A.MONTH\n" + " ) \"thucHienVal\",\n"
					+ " ROUND(DECODE(A.THUE_MAT_BANG, 0, 0, 100 *(SELECT COUNT(*) FROM TBL WHERE A.PROVINCE_CODE = TBL.PROVINCE_CODE AND TO_CHAR(TBL.NGAYTHUE_MB, 'yyyyMM')= A.YEAR || A.MONTH)/ A.THUE_MAT_BANG), 2) \"tyLeVal\"\n"
					+ "FROM \n" + "TOTAL_MONTH_PLAN_DTHT A WHERE 1=1 \n ";
			break;
		case TYPE_KHOI_CONG:
			sql = "WITH TBL AS (\n" + "  SELECT\n" + "  SYS.PROVINCE_CODE,\n" + "  A.STATION_CODE STATIONCODE,\n"
					+ "  B.APPROVE_DATE_REPORT_WO NGAY_KHOICONG\n" + " FROM\n" + "  WO_TR A,\n" + "  WO B,\n"
					+ "  SYS_GROUP SYS,\n" + "  WO_TR_TYPE TRTYPE\n" + " WHERE\n" + "  A.TR_TYPE_ID = TRTYPE.ID\n"
					+ "  AND TRTYPE.TR_TYPE_CODE = 'TR_DONG_BO_HA_TANG'\n" + "  AND A.ID = B.TR_ID\n"
					+ "  AND B.CAT_WORK_ITEM_TYPE_ID = 1648\n" + "  AND B.STATUS = 1\n"
					+ "  AND B.STATE IN('CD_OK', 'OK')\n" + "  AND B.CD_LEVEL_2 = SYS.SYS_GROUP_ID "
					+ "  AND B.APPROVE_DATE_REPORT_WO >= :dateFrom\r\n"
					+ "  AND TRUNC(B.APPROVE_DATE_REPORT_WO) <= :dateTo)   \n" + "SELECT\n" + "\n"
					+ " A.PROVINCE_CODE \"maTinh\",\n" + " A.KHOI_CONG \"keHoachVal\",\n" + " (\n" + " SELECT\n"
					+ "  COUNT(*)\n" + " FROM\n" + "  TBL\n" + " WHERE\n" + "  A.PROVINCE_CODE = TBL.PROVINCE_CODE\n"
					+ "  AND TO_CHAR(TBL.NGAY_KHOICONG, 'yyyyMM')= A.YEAR || A.MONTH) \"thucHienVal\",\n"
					+ " ROUND(DECODE(A.KHOI_CONG, 0, 0, 100 *(SELECT COUNT(*) FROM TBL WHERE A.PROVINCE_CODE = TBL.PROVINCE_CODE AND TO_CHAR(TBL.NGAY_KHOICONG, 'yyyyMM')= A.YEAR || A.MONTH)/ A.KHOI_CONG), 2) \"tyLeVal\"\n"
					+ "FROM \n" + "TOTAL_MONTH_PLAN_DTHT A WHERE 1=1 \n ";
			break;
		case TYPE_DONG_BO:
			sql = "WITH TBL AS (\n" + "  SELECT\n" + "  DISTINCT SYS.PROVINCE_CODE,\n"
					+ "  A.STATION_CODE STATIONCODE,\n" + "  C.COMPLETE_DATE NGAY_DONGBO\n" + " FROM\n" + "  WO_TR A,\n"
					+ "  WO B,\n" + "  CONSTRUCTION C,\n" + "  SYS_GROUP SYS,\n" + "  WO_TR_TYPE TRTYPE\n" + " WHERE\n"
					+ "  A.TR_TYPE_ID = TRTYPE.ID\n" + "  AND TRTYPE.TR_TYPE_CODE = 'TR_DONG_BO_HA_TANG'\n"
					+ "  AND A.ID = B.TR_ID\n" + "  AND B.CONSTRUCTION_ID = C.CONSTRUCTION_ID\n"
					+ "  AND B.STATE IN('CD_OK', 'OK')\n" + "   AND B.STATUS = 1\n"
					+ "   AND B.CD_LEVEL_2 = SYS.SYS_GROUP_ID " + " AND C.COMPLETE_DATE >= :dateFrom "
					+ " AND TRUNC(C.COMPLETE_DATE) <= :dateTo)   \n" + "SELECT\n" + "\n"
					+ " A.PROVINCE_CODE \"maTinh\",\n" + " A.DONG_BO \"keHoachVal\",\n" + " (\n" + " SELECT\n"
					+ "  COUNT(*)\n" + " FROM\n" + "  TBL\n" + " WHERE\n" + "  A.PROVINCE_CODE = TBL.PROVINCE_CODE\n"
					+ "  AND TO_CHAR(TBL.NGAY_DONGBO, 'yyyyMM')= A.YEAR || A.MONTH) \"thucHienVal\",\n"
					+ " ROUND(DECODE(A.DONG_BO, 0, 0, 100 *(SELECT COUNT(*) FROM TBL WHERE A.PROVINCE_CODE = TBL.PROVINCE_CODE AND TO_CHAR(TBL.NGAY_DONGBO, 'yyyyMM')= A.YEAR || A.MONTH)/ A.DONG_BO), 2) \"tyLeVal\"\n"
					+ "FROM \n" + "TOTAL_MONTH_PLAN_DTHT A WHERE 1=1 \n ";
			break;
		case TYPE_PHAT_SONG:
			sql = "WITH TBL AS (\n" + "SELECT\n" + " SYS.PROVINCE_CODE,\n" + " A.STATION_CODE STATIONCODE,\n"
					+ " B.APPROVE_DATE_REPORT_WO NGAY_PHATSONG\n" + "FROM\n" + " WO_TR A,\n" + " WO B,\n"
					+ " SYS_GROUP SYS,\n" + " WO_TR_TYPE TRTYPE,\n" + " WO_TYPE WOTYPE\n" + "WHERE\n"
					+ " A.TR_TYPE_ID = TRTYPE.ID\n" + " AND TRTYPE.TR_TYPE_CODE = 'TR_DONG_BO_HA_TANG'\n"
					+ " AND A.ID = B.TR_ID\n" + " AND B.WO_TYPE_ID = WOTYPE.ID\n"
					+ " AND WOTYPE.WO_TYPE_CODE = 'PHATSONG'\n" + " AND B.STATE IN('OK')\n" + " AND B.STATUS = 1\n"
					+ " AND B.CD_LEVEL_2 = SYS.SYS_GROUP_ID \n" + " AND B.APPROVE_DATE_REPORT_WO >= :dateFrom "
					+ " AND TRUNC(B.APPROVE_DATE_REPORT_WO) <= :dateTo)   \n" + "SELECT\n" + "\n"
					+ " A.PROVINCE_CODE \"maTinh\",\n" + " A.PHAT_SONG \"keHoachVal\",\n" + " (\n" + " SELECT\n"
					+ "  COUNT(*)\n" + " FROM\n" + "  TBL\n" + " WHERE\n" + "  A.PROVINCE_CODE = TBL.PROVINCE_CODE\n"
					+ "  AND TO_CHAR(TBL.NGAY_PHATSONG, 'yyyyMM')= A.YEAR || A.MONTH) \"thucHienVal\",\n"
					+ " ROUND(DECODE(A.PHAT_SONG, 0, 0, 100 *(SELECT COUNT(*) FROM TBL WHERE A.PROVINCE_CODE = TBL.PROVINCE_CODE AND TO_CHAR(TBL.NGAY_PHATSONG, 'yyyyMM')= A.YEAR || A.MONTH)/ A.PHAT_SONG), 2) \"tyLeVal\"\n"
					+ "FROM \n" + "TOTAL_MONTH_PLAN_DTHT A WHERE 1=1 \n ";
			break;
		case TYPE_HSHC:
			sql = "WITH TBL AS (\n" + "SELECT\n" + " SYS.PROVINCE_CODE,\n" + " A.STATION_CODE STATIONCODE,\n"
					+ " A.UPDATE_CD_APPROVE_WO NGAY_HSHC_GUI_TTHT\n" + "FROM\n" + " WO A,\n" + " CONSTRUCTION B,\n"
					+ " SYS_GROUP SYS\n" + "WHERE\n" + " A.WO_TYPE_ID = 3\n" + " AND A.STATUS = 1\n"
					+ " AND A.CONSTRUCTION_ID = B.CONSTRUCTION_ID\n" + " AND B.CHECK_HTCT = 1\n"
					+ " AND A.STATE IN('CD_OK', 'OK') \n" + " AND A.UPDATE_CD_APPROVE_WO >= :dateFrom "
					+ " AND TRUNC(A.UPDATE_CD_APPROVE_WO) <= :dateTo)   \n" + "SELECT\n" + "\n"
					+ " A.PROVINCE_CODE \"maTinh\",\n" + " A.HSHC \"keHoachVal\",\n" + " (\n" + " SELECT\n"
					+ "  COUNT(*)\n" + " FROM\n" + "  TBL\n" + " WHERE\n" + "  A.PROVINCE_CODE = TBL.PROVINCE_CODE\n"
					+ "  AND TO_CHAR(TBL.NGAY_HSHC_GUI_TTHT, 'yyyyMM')= A.YEAR || A.MONTH) \"thucHienVal\",\n"
					+ " ROUND(DECODE(A.HSHC, 0, 0, 100 *(SELECT COUNT(*) FROM TBL WHERE A.PROVINCE_CODE = TBL.PROVINCE_CODE AND TO_CHAR(TBL.NGAY_HSHC_GUI_TTHT, 'yyyyMM')= A.YEAR || A.MONTH)/ A.HSHC), 2) \"tyLeVal\"\n"
					+ "FROM \n" + "TOTAL_MONTH_PLAN_DTHT A WHERE 1=1 \n ";
			break;
		default:
			throw new Exception("Wrong WO type");
		}
		return sql;
	}

	// Huypq-16072021-start
	public Long getDoanhThuDTHTWoByConstruction(Long constructionId) {
		String sql = "Select id from wo where wo_type_id = 301 and construction_id = :constructionId and status > 0 ";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("constructionId", constructionId);
		query.addScalar("id", new LongType());
		List<Long> result = query.list();
		if (result.size() == 0)
			return null;
		else
			return result.get(0);
	}
	// Huy-end

	// Huypq-15072021-start
	public int updateWoMappingStateById(Long woId, String state) {
		StringBuilder sql = new StringBuilder("UPDATE WO_MAPPING_CHECKLIST SET STATE = :state WHERE  WO_ID = :woId ");
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		query.setParameter("state", state);
		query.setParameter("woId", woId);
		return query.executeUpdate();
	}
	// Huy-end

	// Huypq-23072021-start
	public String getStatusConstructionById(Long constructionId) {
		String sql = "Select status status from construction where construction_id = :constructionId";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("constructionId", constructionId);
		query.addScalar("status", new StringType());
		List<String> result = query.list();
		if (result.size() == 0)
			return null;
		else
			return result.get(0);
	}

	// Huypq-end
	// Huypq-24072021-start
	public Map<String, String> getCheckImportWoByContractCode(List<String> lstContractCode) {
		Map<String, String> mapContractCode = new HashMap<String, String>();
		StringBuilder sql = new StringBuilder(
				"SELECT CODE contractCode, " + " nvl(CHECK_IMPORT_WO,0) checkImportWo " + "from CNT_CONTRACT "
						+ "where STATUS!=0 AND CONTRACT_TYPE_O=1 AND UPPER(CODE) in (:lstContractCode) ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("contractCode", new StringType());
		query.addScalar("checkImportWo", new StringType());

		query.setParameterList("lstContractCode", lstContractCode);

		query.setResultTransformer(Transformers.aliasToBean(WoDTO.class));

		List<WoDTO> ls = query.list();

		for (WoDTO work : ls) {
			if (work.getContractCode() != null) {
				mapContractCode.put(work.getContractCode().toUpperCase(), work.getCheckImportWo());
			}
		}

		return mapContractCode;
	}
	// Huy-end

	// HienLT56 start 01062021 - Khi xóa wo thi công (WO_TYPE_ID = 1) Nếu hạng mục
	// đang ở trạng thái thực hiện (STATUS trong WORK_ITEM IN (1,2) thì khi xóa phải
	// chuyển trạng thái hạng mục trong bảng work_item về status = 0
	public Long checkWOThiCongItem(Long woId) {
		StringBuilder sql = new StringBuilder(
				"SELECT wi.WORK_ITEM_ID woItemId FROM WO wo INNER JOIN WORK_ITEM wi ON wo.CONSTRUCTION_ID = wi.CONSTRUCTION_ID WHERE "
						+ " wo.CAT_WORK_ITEM_TYPE_ID = wi.CAT_WORK_ITEM_TYPE_ID " + "AND wi.STATUS IN (1,2) "
						+ "AND wo.WO_TYPE_ID = 1 " + "AND wi.STATUS != 0 " + "AND wo.ID =:woId ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("woItemId", new LongType());
		query.setParameter("woId", woId);
		return (Long) query.uniqueResult();
	}

	// chuyển trạng thái hạng mục trong bảng work_item về status = 0
	// Huypq-27072021-start edit
	public void updateStatusWI(Long id) {
		StringBuilder sql = new StringBuilder("UPDATE WORK_ITEM set status = 1 where WORK_ITEM_ID in (SELECT "
				+ "    wi.WORK_ITEM_ID " + "FROM " + "    WO wo "
				+ "    INNER JOIN WORK_ITEM wi ON wo.CONSTRUCTION_ID = wi.CONSTRUCTION_ID " + "WHERE "
				+ "    wo.CAT_WORK_ITEM_TYPE_ID = wi.CAT_WORK_ITEM_TYPE_ID " + "    AND wi.STATUS IN (1,2) "
				+ "    AND wo.WO_TYPE_ID = 1 " + "    AND wi.STATUS != 0 " + "    AND wo.ID =:woId)");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("woId", id);
		query.executeUpdate();
	}

	// Nếu wo mà công trình tuyến , GPON
	public List<Long> checkWOCttGpon(Long woId) {
		StringBuilder sql = new StringBuilder("SELECT wtd.WO_TASK_DAILY_ID woTaskDailyId FROM WO_TASK_DAILY wtd "
				+ " WHERE wtd.STATUS != 0 " + " AND wtd.WO_ID =:woId ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("woTaskDailyId", new LongType());
		query.setParameter("woId", woId);
		return query.list();
	}

	public void updateStatusCttGpon(Long id) {
		StringBuilder sql = new StringBuilder("UPDATE WO_TASK_DAILY set status = 0 "
				+ "where WO_TASK_DAILY_ID in (SELECT wtd.WO_TASK_DAILY_ID " + " FROM WO_TASK_DAILY wtd "
				+ " LEFT JOIN WO wo on wtd.WO_ID = wo.ID " + " WHERE wtd.STATUS != 0 "
				+ " AND wo.CAT_CONSTRUCTION_TYPE_ID in (2,3) " + " AND wtd.WO_ID =:woId)");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("woId", id);
		query.executeUpdate();
	}

	public List<Long> checkWOThdt(Long woId) {
		StringBuilder sql = new StringBuilder(
				"SELECT wtm.WO_TASK_MONTHLY_ID woTaskMonthlyId FROM WO_TASK_MONTHLY wtm INNER JOIN WO wo ON wtm.WO_ID = wo.ID "
						+ "WHERE wtm.STATUS != 0 " + "AND wo.ID =:woId ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("woTaskMonthlyId", new LongType());
		query.setParameter("woId", woId);
		return query.list();
	}

	public void updateStatusThdt(Long id) {
		StringBuilder sql = new StringBuilder(
				"UPDATE WO_TASK_MONTHLY set status = 0 where WO_TASK_MONTHLY_ID in (SELECT "
						+ "    wtm.WO_TASK_MONTHLY_ID " + "FROM " + "    WO_TASK_MONTHLY wtm "
						+ "    INNER JOIN WO wo ON wtm.WO_ID = wo.ID "
						+ "    LEFT JOIN WO_TYPE wt on wo.WO_TYPE_ID = wt.ID " + "WHERE " + "    wtm.STATUS != 0 "
						+ "    AND wt.WO_TYPE_CODE='THDT' " + "    AND wo.ID =:woId)");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("woId", id);
		query.executeUpdate();
	}

	public List<Long> checkWOMcl(Long woId) {
		StringBuilder sql = new StringBuilder(
				"SELECT wmc.ID woMapCheckId FROM WO_MAPPING_CHECKLIST wmc INNER JOIN WO wo ON wmc.WO_ID = wo.ID "
						+ "WHERE wmc.STATUS != 0 " + "AND wo.ID =:woId ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("woMapCheckId", new LongType());
		query.setParameter("woId", woId);
		return query.list();
	}

	public void updateStatusWoMapCl(Long id) {
		StringBuilder sql = new StringBuilder("UPDATE WO_MAPPING_CHECKLIST set status = 0 where WO_ID = :woId ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("woId", id);
		query.executeUpdate();
	}

	public List<Long> checkWoXdddCl(Long woId) {
		StringBuilder sql = new StringBuilder(
				"select wxc.ID from WO_XDDD_CHECKLIST wxc INNER JOIN WO wo ON wxc.WO_ID = wo.ID "
						+ "WHERE wxc.STATUS != 0 " + "AND wo.ID =:woId ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("woMapCheckId", new LongType());
		query.setParameter("woId", woId);
		return query.list();
	}

	public void updateStatusWoXdddCl(Long id) {
		StringBuilder sql = new StringBuilder("UPDATE WO_XDDD_CHECKLIST set status = 0 where WO_ID =:id");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("id", id);
		query.executeUpdate();
	}

	// Huypq-27072021-end edit
	public Long saveChangeForTTHT(WoDTO wo) {
		StringBuilder sql = new StringBuilder("UPDATE WO SET  ");
		if (wo.getApWorkSrc() != null) {
			sql.append(" AP_WORK_SRC =:apWorkSrc ");
		} else {
			sql.append(" AP_WORK_SRC = NULL ");
		}
		if (wo.getApConstructionType() != null) {
			sql.append(" ,AP_CONSTRUCTION_TYPE =:apConstructionType ");
		} else {
			sql.append(" ,AP_CONSTRUCTION_TYPE = NULL ");
		}
		if (wo.getMoneyValue() != null) {
			sql.append(" ,MONEY_VALUE =:moneyValue ");
		} else {
			sql.append(" ,MONEY_VALUE = NULL ");
		}
		if (wo.getMoneyFlowValue() != null) {
			sql.append(" ,MONEY_FLOW_VALUE =:moneyFlowValue ");
		} else {
			sql.append(" ,MONEY_FLOW_VALUE =NULL ");
		}
		if (wo.getMoneyFlowRequired() != null) {
			sql.append(" ,MONEY_FLOW_REQUIRED=:moneyFlowRequired ");
		} else {
			sql.append(" ,MONEY_FLOW_REQUIRED=NULL ");
		}
		sql.append(" WHERE ID =:id ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		if (wo.getApConstructionType() != null) {
			query.setParameter("apConstructionType", wo.getApConstructionType());
		}
		if (wo.getMoneyValue() != null) {
			query.setParameter("moneyValue", wo.getMoneyValue());
		}
		if (wo.getMoneyFlowValue() != null) {
			query.setParameter("moneyFlowValue", wo.getMoneyFlowValue());
		}
		if (wo.getMoneyFlowRequired() != null) {
			query.setParameter("moneyFlowRequired", wo.getMoneyFlowRequired());
		}
		if (wo.getApWorkSrc() != null) {
			query.setParameter("apWorkSrc", wo.getApWorkSrc());
		}
		query.setParameter("id", wo.getWoId());
		Long rowU = (long) query.executeUpdate();
		if (rowU != 0l) {
			return 1l;
		} else {
			return 0l;
		}
	}
	// HienLT56 end 01062021

	public String getWoTypeCodeByWoId(Long woId) {
		StringBuilder sql = new StringBuilder(
				"select wt.WO_TYPE_CODE woTypeCode from WO_TYPE wt inner join wo on wt.ID = wo.WO_TYPE_ID where wo.ID = :woId ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("woTypeCode", new StringType());
		query.setParameter("woId", woId);
		return (String) query.uniqueResult();
	}

	// Huypq-31072021-start
	public Long deleteTrWhenDeleteWo(Long trId) {
		StringBuilder sql = new StringBuilder("UPDATE WO_TR set STATUS=0 where ID=:trId ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("trId", trId);
		return (long) query.executeUpdate();
	}
	// Huy-end

	// Huypq-08092021-start dashboard
	public String getBaseSqlByWoType(String woType) throws Exception {
		String sql = null;
		switch (woType) {
		case TYPE_THUE_MAT_BANG:
			sql = "SELECT    " + "  PROVINCECODE \"maTinh\",    " + "  PERFORMTMB \"thucHienVal\",    "
					+ "  PLANTMB \"keHoachVal\",    " + "  RATIOTMB \"tyLeVal\"   " + "  FROM RP_BTS_HTCT where 1=1";
			break;
		case TYPE_KHOI_CONG:
			sql = "SELECT    " + "  PROVINCECODE \"maTinh\",    " + "  PERFORMKC \"thucHienVal\",    "
					+ "  PLANKC \"keHoachVal\",    " + "  RATIOKC \"tyLeVal\"   " + "  FROM RP_BTS_HTCT where 1=1";
			break;
		case TYPE_DONG_BO:
			sql = "SELECT    " + "  PROVINCECODE \"maTinh\",    " + "  PERFORMDBHT \"thucHienVal\",    "
					+ "  PLANDBHT \"keHoachVal\",    " + "  RATIODBHT \"tyLeVal\"   " + "  FROM RP_BTS_HTCT where 1=1";
			break;
		case TYPE_PHAT_SONG:
			sql = "SELECT    " + "  PROVINCECODE \"maTinh\",    " + "  PERFORMPS \"thucHienVal\",    "
					+ "  PLANPS \"keHoachVal\",    " + "  RATIOPS \"tyLeVal\"   " + "  FROM RP_BTS_HTCT where 1=1";
			break;
		case TYPE_HSHC:
			sql = "SELECT    " + "  PROVINCECODE \"maTinh\",    " + "  PERFORMHSHC \"thucHienVal\",    "
					+ "  PLANHSHC \"keHoachVal\",    " + "  RATIOHSHC \"tyLeVal\"   " + "  FROM RP_BTS_HTCT where 1=1";
			break;
		default:
			throw new Exception("Wrong WO type");
		}
		return sql;
	}
	// Huy-end

	// Huypq-12082021-start
	public String getLogTimeByWoId(Long woId, String content) {
		StringBuilder sql = new StringBuilder(
				"select to_char(MAX(LOG_TIME),'MM/yyyy') logTime from WO_WORKLOGS where STATUS!=0 AND upper(content) like upper(:content) AND WO_ID=:woId ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("logTime", new StringType());
		query.setParameter("woId", woId);
		query.setParameter("content", content + "%");
		if (query.uniqueResult() != null) {
			return (String) query.uniqueResult();
		}
		return null;
	}

	// Huy-end
	// taotq start 23082021
	public List<WoAppParamDTO> getAppWorkSource(String parType) {
		StringBuilder sql = new StringBuilder(
				"select CODE as code, NAME as name, PAR_TYPE as parType, PAR_ORDER as parOrder, DESCRIPTION description from APP_PARAM where PAR_TYPE = :parType and CODE in ('1','2') and status>0 order by par_order");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.setParameter("parType", parType);

		query.addScalar("code", new StringType());
		query.addScalar("name", new StringType());
		query.addScalar("parType", new StringType());
		query.addScalar("parOrder", new LongType());
		query.addScalar("description", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(WoAppParamDTO.class));
		return query.list();
	}

	public List<WorkItemDTO> getListItemByWorkSrc() {
		List<WorkItemDTO> lst = new ArrayList<>();
		String sql = "select NAME name, CAT_WORK_ITEM_TYPE_ID code from CTCT_CAT_OWNER.CAT_WORK_ITEM_TYPE where cat_construction_type_id= 8";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.addScalar("name", new StringType());
		query.addScalar("code", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(CatWorkItemTypeDTO.class));
		lst = query.list();
		return lst;
	}

	public List<WorkItemDTO> getListItemN(String code) {
		List<WorkItemDTO> lst = new ArrayList<>();
		String sql = "select wi.NAME as name, wi.CAT_WORK_ITEM_TYPE_ID as code, wi.CONSTRUCTION_ID as constructorId from WORK_ITEM wi INNER JOIN CONSTRUCTION c ON wi.CONSTRUCTION_ID = c.CONSTRUCTION_ID where c.CODE = :code";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.addScalar("name", new StringType());
		query.addScalar("code", new StringType());
		query.addScalar("constructorId", new LongType());
		query.setResultTransformer(Transformers.aliasToBean(WorkItemDTO.class));
		query.setParameter("code", code);
		lst = query.list();
		return lst;
	}

	public void updateWoCheckList(Long id) {
		String sql = "UPDATE WO_CHECKLIST set STATE = 'NEW' where WO_ID =:id";
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("id", id);
		query.executeUpdate();
		getSession().flush();
	}

	public void updateWoMappingCheckList(Long id) {
		String sql = "UPDATE WO_MAPPING_CHECKLIST set STATE = 'NEW' where WO_ID =:id";
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("id", id);
		query.executeUpdate();
		getSession().flush();
	}
	// taotq end 16092021

	// taotq end 27082021

	// duonghv13 add 08102021

	public List<WoDTO> exportApproveWoUCTTRole(String type, List<String> listData) {
		StringBuilder sql = new StringBuilder("SELECT w.WO_CODE as woCode,\n"
				+ "                  wt.WO_TYPE_NAME as woTypeName,\n"
				+ "                  (select ap.name from APP_PARAM ap where to_char(w.AP_WORK_SRC) = ap.CODE  AND ap.PAR_TYPE = 'AP_WORK_SRC' AND ap.status  = 1 ) apWorkSrcName,\n"
				+ "                  NVL(w.contract_code,w.project_code) contractCode,\n"
				+ "                  w.STATION_CODE stationCode,\n"
				+ "                  w.CONSTRUCTION_CODE constructionCode,\n"
				+ "                  (SELECT cwt.NAME from   CAT_WORK_ITEM_TYPE cwt where w.CAT_WORK_ITEM_TYPE_ID = cwt.CAT_WORK_ITEM_TYPE_ID  AND cwt.status  = 1) catWorkItemTypeName,\n"
				+ "                  round(nvl(w.MONEY_VALUE,0) / 1000000,2) moneyValue,\n"
				+ "                  to_char(w.FINISH_DATE,'dd/MM/yyyy') finishDateStr,\n"
				+ "                  to_char(w.END_TIME,'dd/MM/yyyy') endTimeStr,\n"
				+ "                  w.CD_LEVEL_2_NAME cdLevel2Name,\n"
				+ "                  w.WO_ORDER_CONFIRM woOrderConfirm,\n" + "                  w.FT_NAME ftName\n"
				+ "                 FROM WO_TYPE wt,\n" + "                  WO w\n"
				+ "                WHERE wt.id                 = w.wo_type_id\n"
				+ "                 and wt.WO_TYPE_CODE = 'XLSC' "
				+ "                 AND w.CD_LEVEL_1           IS NOT NULL\n"
				+ "                 and w.CD_LEVEL_2 is not null " + "				  and w.CD_LEVEL_3 is not null "
				+ "                 AND wt.status               = 1\n"
				+ "                 AND w.status                = 1 ");
		if (type.equals("0")) {
			sql.append(" AND w.state IN ( 'ACCEPT_CD','REJECT_FT') AND wt.WO_TYPE_CODE not in ('HCQT') ");

			if (listData.size() > 0) {
				sql.append(
						" and w.CD_LEVEL_4 is null  AND ((w.CD_LEVEL_2 in (:groupIdList) ) OR w.CD_LEVEL_3 in (:groupIdList) )");
			}
		}
		sql.append("ORDER BY w.WO_CODE,wt.WO_TYPE_NAME");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		if (listData.size() > 0 && !type.equals("TCTCT")) {
			query.setParameterList("groupIdList", listData);
		}
		query.addScalar("woCode", new StringType());
		query.addScalar("woTypeName", new StringType());
		query.addScalar("apWorkSrcName", new StringType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("stationCode", new StringType());
		query.addScalar("constructionCode", new StringType());
		query.addScalar("catWorkItemTypeName", new StringType());
		query.addScalar("moneyValue", new DoubleType());
		query.addScalar("finishDateStr", new StringType());
		query.addScalar("endTimeStr", new StringType());
		query.addScalar("cdLevel2Name", new StringType());
		query.addScalar("ftName", new StringType());
		query.addScalar("woOrderConfirm", new LongType());

		query.setResultTransformer(Transformers.aliasToBean(WoDTO.class));
		List<WoDTO> lst = query.list();
		return lst;
	}

	private List<WoDTO> checkCntContractRevenue(WoDTO obj) {
		StringBuilder sql = new StringBuilder("SELECT DISTINCT " + "    wo.STATE " + "FROM "
				+ "    CNT_CONTRACT_REVENUE ccr " + "    left join WO wo on ccr.CNT_CONTRACT_ID = wo.CONTRACT_ID "
				+ "    LEFT join WORK_ITEM wi on wo.CAT_WORK_ITEM_TYPE_ID = wi.CAT_WORK_ITEM_TYPE_ID  "
				+ "    AND wo.CONSTRUCTION_ID = wi.CONSTRUCTION_ID " + "    AND ( " + "        CASE ccr.BRANCH "
				+ "            WHEN 1   THEN '280483' " + "            WHEN 2   THEN '242656' "
				+ "            WHEN 3   THEN '166677' " + "            WHEN 4   THEN '270120' "
				+ "            WHEN 5   THEN '280501' " + "            WHEN 6   THEN '9006003' "
				+ "            ELSE '0' " + "        END " + "    ) = TO_CHAR(wi.BRANCH) "
				+ "WHERE ccr.STATUS!=0 AND wo.STATUS!=0 " + "    AND wo.ID not in (:woId) "
				+ "    AND ccr.CNT_CONTRACT_ID = :contractId " + "    AND wi.CONSTRUCTION_ID = :constructionId ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("state", new StringType());

		query.setParameter("woId", obj.getWoId());
		query.setParameter("contractId", obj.getContractId());
		query.setParameter("constructionId", obj.getConstructionId());

		query.setResultTransformer(Transformers.aliasToBean(WoDTO.class));

		return query.list();
	}

	private Boolean getCntContractRevenueById(Long contractId) {
		StringBuilder sql = new StringBuilder("select CNT_CONTRACT_REVENUE cntContractRevenue "
				+ "    FROM CNT_CONTRACT " + "    WHERE STATUS!=0 " + "    AND CNT_CONTRACT_ID = :contractId ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("cntContractRevenue", new StringType());

		query.setParameter("contractId", contractId);

		String cntContractRevenue = (String) query.uniqueResult();

		if (StringUtils.isNotBlank(cntContractRevenue) && cntContractRevenue.equals("1")) {
			return true;
		}

		return false;
	}

	// Huypq-23102021-start
	public List<CatWorkItemTypeDTO> getDataWorkItemByConsTypeId(CatWorkItemTypeDTO obj) {
		StringBuilder sql = new StringBuilder(
				" select CAT_WORK_ITEM_TYPE_ID catWorkItemTypeId, CODE code," + "NAME name "
						+ "from CAT_WORK_ITEM_TYPE " + "where STATUS=1 AND CAT_CONSTRUCTION_TYPE_ID = :consTypeId ");

		if (StringUtils.isNotBlank(obj.getName())) {
			sql.append(" AND (UPPER(CODE) LIKE UPPER(:keySearch) OR UPPER(NAME) LIKE UPPER(:keySearch)) ");
		}

		sql.append(" AND ROWNUM<=10 ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("catWorkItemTypeId", new LongType());
		query.addScalar("code", new StringType());
		query.addScalar("name", new StringType());

		query.setParameter("consTypeId", obj.getCatConstructionTypeId());

		if (StringUtils.isNotBlank(obj.getName())) {
			query.setParameter("keySearch", "%" + obj.getName() + "%");
		}

		query.setResultTransformer(Transformers.aliasToBean(CatWorkItemTypeDTO.class));

		return query.list();
	}

	public List<WorkItemDTO> getWorkItemHtctByConstructionId(Long constructionId) {
		if (constructionId == null)
			return new ArrayList<>();
		String sql = "select WORK_ITEM_NAME name, round((nvl(WORK_ITEM_VALUE,0)/1000000),2) quantity from WO_MAPPING_WORK_ITEM_HTCT where CONSTRUCTION_ID=:consId ";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("consId", constructionId);
		query.addScalar("name", new StringType());
		query.addScalar("quantity", new DoubleType());
		query.setResultTransformer(Transformers.aliasToBean(WorkItemDTO.class));
		return query.list();
	}

	public List<WoAppParamDTO> getAppParamByParOrder(String parType, String parOrder) {
		StringBuilder sql = new StringBuilder(
				"select CODE as code, NAME as name, PAR_TYPE as parType, PAR_ORDER as parOrder, DESCRIPTION description from APP_PARAM where PAR_TYPE = :parType and PAR_ORDER=:parOrder and status>0");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.setParameter("parType", parType);
		query.setParameter("parOrder", parOrder);

		query.addScalar("code", new StringType());
		query.addScalar("name", new StringType());
		query.addScalar("parType", new StringType());
		query.addScalar("parOrder", new LongType());
		query.addScalar("description", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(WoAppParamDTO.class));
		return query.list();
	}

	public void insertWorkItemMappingWo(WoDTO dto) {
		String sql = " Insert into work_item (CONSTRUCTION_ID, CAT_WORK_ITEM_TYPE_ID, CODE, NAME, IS_INTERNAL, CONSTRUCTOR_ID, SUPERVISOR_ID, STATUS, CREATED_DATE, CREATED_USER_ID, CREATED_GROUP_ID, WORK_ITEM_ID, QUANTITY) "
				+ " values (:constructionId, :catWorkItemTypeId, :workItemCode, :name, 1, :constructorId, :constructorId, 1, sysdate, :createdUserId, :createGroupId, work_item_seq.nextval, :quantity) ";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("constructionId", dto.getConstructionId());
		query.setParameter("catWorkItemTypeId", dto.getCatWorkItemTypeId());
		query.setParameter("workItemCode", dto.getConstructionCode() + "_" + dto.getCatWorkItemTypeCode());
		query.setParameter("name", dto.getCatWorkItemTypeName());
		query.setParameter("quantity", dto.getMoneyValueHtct());
		if (dto.getCdLevel2() != null) {
			query.setParameter("constructorId", dto.getCdLevel2());
		} else if (dto.getCdLevel5() != null) {
			query.setParameter("constructorId", dto.getCdLevel5());
		} else {
			query.setParameter("constructorId", -1);
		}
		query.setParameter("createdUserId", dto.getSysUserId());
		query.setParameter("createGroupId", dto.getSysGroupId());

		query.executeUpdate();
	}

	public Long getWorkItemTypeIdByCode(String code) {
		StringBuilder sql = new StringBuilder("select CAT_WORK_ITEM_TYPE_ID from CAT_WORK_ITEM_TYPE where code=:code ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("code", code);
		query.addScalar("CAT_WORK_ITEM_TYPE_ID", new LongType());
		return (Long) query.uniqueResult();
	}

	public List<WoMappingWorkItemHtctBO> getWoMappingWiHtctByConsId(Long consId) {
		StringBuilder sql = new StringBuilder(
				"select * from WO_MAPPING_WORK_ITEM_HTCT where CONSTRUCTION_ID = :consId AND STATUS=1 AND WORK_ITEM_VALUE>0 ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.setParameter("consId", consId);

		query.addEntity(WoMappingWorkItemHtctBO.class);
		return query.list();
	}

	public List<WoMappingWorkItemHtctBO> checkExistWoMappingWiHtct(WoBO bo) {
		StringBuilder sql = new StringBuilder(
				"select * from WO_MAPPING_WORK_ITEM_HTCT where CONSTRUCTION_ID = :consId AND CONTRACT_ID=:contractId AND WORK_ITEM_ID=:workItemId AND STATUS!=0 ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.setParameter("consId", bo.getConstructionId());
		query.setParameter("contractId", bo.getContractId());
		query.setParameter("workItemId", bo.getCatWorkItemTypeId());

		query.addEntity(WoMappingWorkItemHtctBO.class);
		return query.list();
	}

	public Double getCheckStatusWoMappingWiHtct(WoBO bo) {
		String sql = new String("select nvl(round(AVG(status),2),0) status "
				+ " from WO_MAPPING_WORK_ITEM_HTCT where CONSTRUCTION_ID = :consId AND CONTRACT_ID=:contractId AND WORK_ITEM_VALUE>0 ");
		SQLQuery query = getSession().createSQLQuery(sql);
		query.addScalar("status", new DoubleType());
		query.setParameter("consId", bo.getConstructionId());
		query.setParameter("contractId", bo.getContractId());
		List<Double> lstDoub = query.list();
		if (lstDoub != null && lstDoub.size() > 0) {
			return lstDoub.get(0);
		}
		return 0D;
	}

	public Long updatePercentWoMappingWiHtct(WoBO bo, Long constructionStatus) {
		int result = 0;
		try {
			// cap nhat hang muc
			if (bo.getCatWorkItemTypeId() != null) {
				StringBuilder sql = new StringBuilder(
						"update WORK_ITEM SET STATUS = '3', COMPLETE_DATE = :completeDate,QUANTITY = :quantity  where CONSTRUCTION_ID = :constructionId AND CAT_WORK_ITEM_TYPE_ID = :catWorkID");
				SQLQuery query = this.getSession().createSQLQuery(sql.toString());
				query.setParameter("completeDate", new Date());
				query.setParameter("quantity", bo.getMoneyValue());
				query.setParameter("constructionId", bo.getConstructionId());
				query.setParameter("catWorkID", bo.getCatWorkItemTypeId());
				result = query.executeUpdate();

				StringBuilder sqlWi = new StringBuilder(
						"UPDATE WO_MAPPING_WORK_ITEM_HTCT set status=3, COMPLETED_DATE=sysdate where CONSTRUCTION_ID = :consId AND CONTRACT_ID=:contractId AND WORK_ITEM_ID=:workItemId ");
				SQLQuery queryWi = getSession().createSQLQuery(sqlWi.toString());

				queryWi.setParameter("consId", bo.getConstructionId());
				queryWi.setParameter("contractId", bo.getContractId());
				queryWi.setParameter("workItemId", bo.getCatWorkItemTypeId());
				result = queryWi.executeUpdate();
			}
			this.getSession().flush();
			Double checkCountConstruction = getCheckStatusWoMappingWiHtct(bo);
			if (checkCountConstruction.compareTo(3.0) == 0) {
				StringBuilder sqlCons = new StringBuilder("");
				sqlCons.append(
						"update CONSTRUCTION SET status = :constructionStatus,COMPLETE_DATE = sysdate , COMPLETE_VALUE =(SELECT SUM(nvl(WORK_ITEM_VALUE,0)) FROM WO_MAPPING_WORK_ITEM_HTCT WHERE CONSTRUCTION_ID = :constructionWork AND CONTRACT_ID=:contractId) WHERE CONSTRUCTION_ID = :constructionId ");
				SQLQuery queryCons = this.getSession().createSQLQuery(sqlCons.toString());
				queryCons.setParameter("constructionWork", bo.getConstructionId());
				queryCons.setParameter("contractId", bo.getContractId());
				queryCons.setParameter("constructionId", bo.getConstructionId());
				queryCons.setParameter("constructionStatus", constructionStatus);
				result = queryCons.executeUpdate();
				if (result == 1) {
					result = 2;
				}
			}
		} catch (Exception ex) {
			//ex.printStackTrace();
			result = 0;
		}
		return (long) result;
	}

	public boolean checkContractCanCreateHSHC(Long contractId) {
		// hợp đồng chi phí ngoài os
		// String sql = "select count(*) as total from cnt_contract where contract_type
		// = 0 and contract_type_o = 2 and cnt_contract_id = :contractId and status>0 ";

		// tất cả hđ từ ttht trừ hd xddd
		String sql = "select count(*) as total from cnt_contract where contract_branch in (3) and CONTRACT_TYPE in (0,8) and cnt_contract_id = :contractId and status>0 ";

		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("contractId", contractId);
		query.addScalar("total", new LongType());
		Long total = (Long) query.uniqueResult();

		if (total > 0)
			return true;
		return false;
	}

	public WoDTO getDataConstructionContractByStationCode(String stationCode) {
		StringBuilder sql = new StringBuilder(" SELECT " + "    cc.CNT_CONTRACT_ID contractId, "
				+ "    cc.code contractCode, " + "    cons.CONSTRUCTION_ID constructionId, "
				+ "    cons.code constructionCode, cwit.PLACEMENT placement " + "FROM CNT_CONSTR_WORK_ITEM_TASK cwit "
				+ "left join CNT_CONTRACT cc on cwit.CNT_CONTRACT_ID = cc.CNT_CONTRACT_ID and cc.CONTRACT_TYPE=8 "
				+ "left join CONSTRUCTION cons on cwit.CONSTRUCTION_ID = cons.CONSTRUCTION_ID "
				+ "left join CAT_STATION cs on cons.CAT_STATION_ID = cs.CAT_STATION_ID "
				+ "where cwit.STATUS!=0 AND cc.STATUS!=0 AND cons.STATUS!=0 AND cs.STATUS!=0 AND cs.code=:stationCode ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("contractId", new LongType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("constructionId", new LongType());
		query.addScalar("constructionCode", new StringType());
		query.addScalar("placement", new StringType());

		query.setParameter("stationCode", stationCode);
		query.setResultTransformer(Transformers.aliasToBean(WoDTO.class));

		List<WoDTO> ls = query.list();
		if (ls.size() > 0) {
			return ls.get(0);
		}
		return null;
	}

	public List<WoCatWorkItemTypeDTO> getCatWorkItemTypeIdByLstName(List<String> lstName) {
		Map<String, Long> mapNameId = new HashMap<String, Long>();
		StringBuilder sql = new StringBuilder(
				"SELECT CAT_WORK_ITEM_TYPE_ID catWorkItemTypeId, NAME name, code catWorkItemTypeCode from CAT_WORK_ITEM_TYPE where name in (:lstName) ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("catWorkItemTypeId", new LongType());
		query.addScalar("name", new StringType());
		query.addScalar("catWorkItemTypeCode", new StringType());

		query.setParameterList("lstName", lstName);

		query.setResultTransformer(Transformers.aliasToBean(WoCatWorkItemTypeDTO.class));

		List<WoCatWorkItemTypeDTO> ls = query.list();

		return ls;
	}

	public Long updatePercentConsWorkItem(WoBO bo) {
		int result = 0;
		try {
			// cap nhat hang muc
			// update trạng thái hạng mục của wo đã được ĐTHT duyệt đề xuất hủy
			if (bo.getCatWorkItemTypeId() != null) {
				StringBuilder sql = new StringBuilder(
						"update WORK_ITEM SET STATUS = '3', COMPLETE_DATE = :completeDate,QUANTITY = :quantity  where CONSTRUCTION_ID = :constructionId AND CAT_WORK_ITEM_TYPE_ID = :catWorkID");
				SQLQuery query = this.getSession().createSQLQuery(sql.toString());
				query.setParameter("completeDate", new Date());
				query.setParameter("quantity", bo.getMoneyValue());
				query.setParameter("constructionId", bo.getConstructionId());
				query.setParameter("catWorkID", bo.getCatWorkItemTypeId());
				result = query.executeUpdate();
			}

			StringBuilder sqlWi = new StringBuilder(
					"UPDATE WO_MAPPING_WORK_ITEM_HTCT set status=3, COMPLETED_DATE=sysdate, WORK_ITEM_VALUE=:quantity where CONSTRUCTION_ID = :consId AND CONTRACT_ID=:contractId AND WORK_ITEM_ID=:workItemId ");
			SQLQuery queryWi = getSession().createSQLQuery(sqlWi.toString());

			queryWi.setParameter("consId", bo.getConstructionId());
			queryWi.setParameter("contractId", bo.getContractId());
			queryWi.setParameter("workItemId", bo.getCatWorkItemTypeId());
			queryWi.setParameter("quantity", bo.getMoneyValue());
			result = queryWi.executeUpdate();

			this.getSession().flush();

			// Update trạng thái hạng mục còn lại khi ĐTHT duyệt đề xuất hủy
			StringBuilder sqlOther = new StringBuilder(
					"update WORK_ITEM SET STATUS = '3', COMPLETE_DATE = :completeDate, QUANTITY = 0  where STATUS != '3' AND CONSTRUCTION_ID = :constructionId ");
			SQLQuery queryOther = this.getSession().createSQLQuery(sqlOther.toString());
			queryOther.setParameter("completeDate", new Date());
			queryOther.setParameter("constructionId", bo.getConstructionId());
			result = queryOther.executeUpdate();

			StringBuilder sqlOtherHtct = new StringBuilder(
					"update WO_MAPPING_WORK_ITEM_HTCT SET STATUS = 3, COMPLETED_DATE = :completeDate, WORK_ITEM_VALUE = 0  where STATUS != '3' AND CONSTRUCTION_ID = :constructionId ");
			SQLQuery queryOtherHtct = this.getSession().createSQLQuery(sqlOtherHtct.toString());
			queryOtherHtct.setParameter("completeDate", new Date());
			queryOtherHtct.setParameter("constructionId", bo.getConstructionId());
			result = queryOtherHtct.executeUpdate();

			this.getSession().flush();

			// Double checkCountConstruction = getCheckStatusWoMappingWiHtct(bo);
			// if (checkCountConstruction.compareTo(3.0) == 0) {
			StringBuilder sqlCons = new StringBuilder("");
			sqlCons.append(
					"update CONSTRUCTION SET status = 10,COMPLETE_DATE = sysdate , COMPLETE_VALUE =(SELECT SUM(nvl(WORK_ITEM_VALUE,0)) FROM WO_MAPPING_WORK_ITEM_HTCT WHERE CONSTRUCTION_ID = :constructionWork AND CONTRACT_ID=:contractId) WHERE CONSTRUCTION_ID = :constructionId ");
			SQLQuery queryCons = this.getSession().createSQLQuery(sqlCons.toString());
			queryCons.setParameter("constructionWork", bo.getConstructionId());
			queryCons.setParameter("contractId", bo.getContractId());
			queryCons.setParameter("constructionId", bo.getConstructionId());
			result = queryCons.executeUpdate();
			if (result == 1) {
				result = 2;
			}
			// }
		} catch (Exception ex) {
			//ex.printStackTrace();
			result = 0;
		}
		return (long) result;
	}

	public List<WoBO> getWoByConsId(WoBO bo) {
		StringBuilder sql = new StringBuilder(
				"SELECT * from WO where CONSTRUCTION_ID=:consId AND CAT_WORK_ITEM_TYPE_ID is not null AND WO_TYPE_ID=1 AND AP_WORK_SRC=4 AND STATE NOT IN ('CD_OK','OK')");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addEntity(WoBO.class);

		query.setParameter("consId", bo.getConstructionId());

		List<WoBO> ls = query.list();

		return ls;
	}

	public List<WoDTO> getWorkItemCompleteByConstruction(Long constructionId) {
		if (constructionId == null)
			return new ArrayList<>();
		String sql = "SELECT wo.ID woId, " + "    wo.wo_code woCode, " + "    cw.name catWorkItemTypeName, "
				+ "	 round((nvl(wo.money_value,0)/1000000), 3) moneyValue, " + "    wo.state state " + " FROM wo "
				+ "   LEFT JOIN CAT_WORK_ITEM_TYPE cw ON wo.CAT_WORK_ITEM_TYPE_ID = cw.CAT_WORK_ITEM_TYPE_ID "
				+ " WHERE " + "    wo.CONSTRUCTION_ID = :constructionId "
				+ "    AND wo.CAT_WORK_ITEM_TYPE_ID IS NOT NULL " + "    AND wo.WO_TYPE_ID=1 AND wo.AP_WORK_SRC=4 ";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("constructionId", constructionId);
		query.addScalar("woId", new LongType());
		query.addScalar("woCode", new StringType());
		query.addScalar("catWorkItemTypeName", new StringType());
		query.addScalar("moneyValue", new DoubleType());
		query.addScalar("state", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(WoDTO.class));
		return query.list();
	}

	public int updatePercentWiConstrNG(WoDTO dto) {
		int result = 0;
		try {
			// cap nhat hang muc
			if (dto.getCatWorkItemTypeId() != null) {
				StringBuilder sql = new StringBuilder(
						"update WORK_ITEM SET STATUS = '2', COMPLETE_DATE = null  where CONSTRUCTION_ID = :constructionId AND CAT_WORK_ITEM_TYPE_ID = :catWorkID");
				SQLQuery query = this.getSession().createSQLQuery(sql.toString());
				query.setParameter("constructionId", dto.getConstructionId());
				query.setParameter("catWorkID", dto.getCatWorkItemTypeId());
				result = query.executeUpdate();

				StringBuilder sqlHtct = new StringBuilder(
						"update WO_MAPPING_WORK_ITEM_HTCT SET STATUS = '2', COMPLETED_DATE = null  where CONSTRUCTION_ID = :constructionId AND CONTRACT_ID=:contractId AND WORK_ITEM_ID=:workItemId ");
				SQLQuery queryHtct = this.getSession().createSQLQuery(sqlHtct.toString());
				queryHtct.setParameter("constructionId", dto.getConstructionId());
				queryHtct.setParameter("contractId", dto.getContractId());
				queryHtct.setParameter("workItemId", dto.getCatWorkItemTypeId());
				result = queryHtct.executeUpdate();

			}
			StringBuilder sql = new StringBuilder(
					"update CONSTRUCTION SET status = 3 ,COMPLETE_DATE = null,COMPLETE_VALUE = null where CONSTRUCTION_ID = :constructionId");
			SQLQuery query = this.getSession().createSQLQuery(sql.toString());
			query.setParameter("constructionId", dto.getConstructionId());
			result = query.executeUpdate();

		} catch (Exception ex) {
			//ex.printStackTrace();
			result = 0;
		}
		return result;
	}

	public List<WoBO> getWoThiCongCompleteByConsId(Long consId) {
		StringBuilder sql = new StringBuilder(
				"SELECT * from WO where CONSTRUCTION_ID=:consId AND CAT_WORK_ITEM_TYPE_ID is not null AND WO_TYPE_ID=1 AND AP_WORK_SRC=4");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addEntity(WoBO.class);

		query.setParameter("consId", consId);

		List<WoBO> ls = query.list();

		return ls;
	}

	// Huy-end
	// Huypq-04122021-start
	public Long getWoDBHTByConstruction(Long constructionId) {
		String sql = "select wo.ID id from WO wo " + "left join wo_type wt on wo.WO_TYPE_ID = wt.ID "
				+ "where wo.STATUS>0 " + "AND wt.WO_TYPE_CODE='DBHT' " + "AND wo.CONSTRUCTION_ID= :constructionId ";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("constructionId", constructionId);
		query.addScalar("id", new LongType());
		List<Long> result = query.list();
		if (result.size() == 0)
			return null;
		else
			return result.get(0);
	}

	public Long updateWoDbhtWhenReApprove(Long woId, String state, Long status) {
		StringBuilder sql = new StringBuilder(" UPDATE WO SET STATE=:state, STATUS=:status where ID=:woId ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.setParameter("state", state);
		query.setParameter("status", status);
		query.setParameter("woId", woId);

		return (long) query.executeUpdate();
	}
	// Huy-end

	public SysUserCOMSDTO getGroupLevel2ByUserId(Long userid) {
		StringBuilder sql = new StringBuilder("SELECT " + "    REGEXP_SUBSTR(sg.path, '[^/]+', 1, 2) sysGroupId "
				+ "	FROM " + "    sys_user su, " + "    sys_group sg " + "	WHERE " + "    su.sys_user_id =:userid "
				+ "    AND sg.sys_group_id = su.sys_group_id");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("sysGroupId", new LongType());
		query.setResultTransformer(Transformers.aliasToBean(SysUserCOMSDTO.class));
		query.setParameter("userid", userid);
		return (SysUserCOMSDTO) query.uniqueResult();
	}

	// Huypq-28012022-start
	public CatStationDTO checkStationByCode(String code) {
		StringBuilder sql = new StringBuilder("SELECT CODE code FROM CAT_STATION where status!=0 AND CODE = :code ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("code", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(CatStationDTO.class));
		query.setParameter("code", code);
		List<CatStationDTO> ls = query.list();
		if (ls.size() > 0) {
			return ls.get(0);
		}
		return null;
	}

	// Huy-end
	// taotq start 13012022
	public List<WoDTO> getCD(Long constructionId) {
		// TODO Auto-generated method stub
		String sql = "select DISTINCT CD_LEVEL_1 cdLevel1, CD_LEVEL_1_NAME cdLevel1Name, CD_LEVEL_2 cdLevel2, CD_LEVEL_2_NAME cdLevel2Name "
				+ "from wo where WO_TYPE_ID = 1 and CONSTRUCTION_ID = :constructionId";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.addScalar("cdLevel1", new StringType());
		query.addScalar("cdLevel1Name", new StringType());
		query.addScalar("cdLevel2", new StringType());
		query.addScalar("cdLevel2Name", new StringType());
		query.setParameter("constructionId", constructionId);
		query.setResultTransformer(Transformers.aliasToBean(WoDTO.class));
		return query.list();
	}
	// taotq end 13012022

	public Long getCntContractId(String contractCode) {
		// TODO Auto-generated method stub
		Long id = null;
		String sql = "select CNT_CONTRACT_ID contractId, CODE contractCode from CTCT_IMS_OWNER.CNT_CONTRACT where code = :code and ROWNUM <=1";
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("contractId", new LongType());
		query.addScalar("contractCode", new StringType());
		query.setParameter("code", contractCode);
		query.setResultTransformer(Transformers.aliasToBean(WoDTO.class));
		List<WoDTO> lstDto = query.list();
		if (lstDto.size() > 0) {
			id = lstDto.get(0).getContractId();
		}
		return id;
	}

	// Huypq-08022022-start
	public void createSendEmailForDTHT(String strContent, String nameSubject, Long sysUserId, Long sysGroupId) {
		try {
			// if (sysUserId != null) {
			// SysUserCOMSDTO userDTO = getUserInfoById(sysUserId);
			// insertSendEmail(userDTO, nameSubject, strContent);
			// }
			if (sysGroupId != null) {
				List<SysUserCOMSDTO> lstUsersOfGroup = getListUserOfGroup(sysGroupId);

				boolean isSend = false;
				for (SysUserCOMSDTO userDTO : lstUsersOfGroup) {
					// Neu co quyen nhan sms thi gui
					if (hasRole(userDTO.getSysUserId(), "CD", "WOXL")) {
						isSend = true;
						insertSendEmail(userDTO, nameSubject, strContent);
					}
				}

				// listUserBGD
				WoSimpleSysGroupDTO woSimpleSysGroupDTO = new WoSimpleSysGroupDTO();
				woSimpleSysGroupDTO = getParentGroupByName(sysGroupId);
				if (!Objects.isNull(woSimpleSysGroupDTO)) {
					List<SysUserCOMSDTO> lstUsersOfGroupBGD = getListUserOfGroup(woSimpleSysGroupDTO.getSysGroupId());

					boolean isSendBGD = false;
					for (SysUserCOMSDTO userDTO : lstUsersOfGroupBGD) {
						// Neu co quyen nhan sms thi gui
						if (hasRole(userDTO.getSysUserId(), "RECEIVE", "SMS_WO")) {
							isSendBGD = true;
							insertSendEmail(userDTO, nameSubject, strContent);
						}
					}
				}

				// GD TTĐTHT
				woSimpleSysGroupDTO = getGroupByNameGdTTDTHT(sysGroupId);
				if (!Objects.isNull(woSimpleSysGroupDTO)) {
					List<SysUserCOMSDTO> lstUsersOfGroupBGD = getListUserOfGroup(woSimpleSysGroupDTO.getSysGroupId());

					boolean isSendBGD = false;
					for (SysUserCOMSDTO userDTO : lstUsersOfGroupBGD) {
						// Neu co quyen nhan sms thi gui
						isSendBGD = true;
						insertSendEmail(userDTO, nameSubject, strContent);
					}
				}
			}
		} catch (Exception ex) {
			//ex.printStackTrace();
		}
	}

	private void insertSendEmail(SysUserCOMSDTO userDTO, String nameSubject, String strContent) {
		String sql = new String(" SELECT SEND_EMAIL_SEQ.nextval FROM DUAL ");
		SQLQuery query = getSession().createSQLQuery(sql);
		int sendEmailId = ((BigDecimal) query.uniqueResult()).intValue();
		StringBuilder sqlSendSmsEmail = new StringBuilder(
				"INSERT INTO SEND_EMAIL (SEND_EMAIL_ID, SUBJECT, CONTENT, STATUS, RECEIVE_EMAIL, CREATED_DATE, CREATED_USER_ID) "
						+ " VALUES (:sendEmailId, :subject, :content, 0, :email, :createdDate, :createUserId)");
		SQLQuery querySms = getSession().createSQLQuery(sqlSendSmsEmail.toString());
		querySms.setParameter("email", userDTO.getEmail());
		querySms.setParameter("createUserId", userDTO.getSysUserId());
		querySms.setParameter("sendEmailId", sendEmailId);
		querySms.setParameter("createdDate", new Date());
		querySms.setParameter("content", strContent);
		querySms.setParameter("subject", nameSubject);
		querySms.executeUpdate();
	}

	public WoSimpleSysGroupDTO getGroupByNameGdTTDTHT(Long id) {
		String sql = " select SYS_GROUP_ID as sysGroupId, NAME as groupName, GROUP_LEVEL as groupLevel \r\n"
				+ "                 from CTCT_CAT_OWNER.SYS_GROUP where status>0 \r\n"
				+ "                 and path like '%/9006211/%'\r\n"
				+ "                 and NAME LIKE '%Thủ trưởng đơn vị%' ";
		SQLQuery query = getSession().createSQLQuery(sql);

		query.setParameter("id", id);

		query.addScalar("groupName", new StringType());
		query.addScalar("sysGroupId", new LongType());
		query.addScalar("groupLevel", new IntegerType());
		query.setResultTransformer(Transformers.aliasToBean(WoSimpleSysGroupDTO.class));

		return (WoSimpleSysGroupDTO) query.uniqueResult();
	}

	// Huy-end
	// Huypq-18022022-start
	public Long updateConstrWhenCloseWoKhoiCong(Long constrId) {
		StringBuilder sql = new StringBuilder(
				"UPDATE CONSTRUCTION SET STARTING_DATE=sysdate WHERE CONSTRUCTION_ID=:constrId ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("constrId", constrId);
		return (long) query.executeUpdate();
	}
	// Huy-end
	
	//Huypq-30052022-start
    public List<WoXdddChecklistDTO> getCheckListIsConfirm() {
//    	String sql = " SELECT " +
//    			"    wo.CONSTRUCTION_ID constructionId, " +
//    			"    wo.CONSTRUCTION_CODE constructionCode, " +
//    			"    cwit.NAME workItemName, " +
//    			"    wx.NAME name, " +
//    			"    wx.VALUE value, " +
//    			"    wx.STATE state, " +
//    			"    wx.CONFIRM confirm, " +
//    			"    wx.CONFIRM_DATE confirmDate, " +
//    			"    wx.CONFIRM_BY confirmBy, " +
//    			"    wx.USER_CREATED userCreated, " +
//    			"    wx.CREATE_DATE createDate " +
//    			"FROM " +
//    			"    WO_XDDD_CHECKLIST wx " +
//    			"    LEFT JOIN WO wo ON wx.WO_ID = wo.ID " +
//    			"    LEFT JOIN CAT_WORK_ITEM_TYPE cwit ON wo.CAT_WORK_ITEM_TYPE_ID = cwit.CAT_WORK_ITEM_TYPE_ID " +
//    			"WHERE " +
//    			"    wo.CONSTRUCTION_ID IS NOT NULL " +
//    			"    AND wo.CONSTRUCTION_CODE IS NOT NULL " +
//    			"    AND cwit.NAME IS NOT NULL " +
//    			"    AND wx.NAME IS NOT NULL " +
//    			"    AND nvl(wx.CONFIRM,0) = 1 ";
    	String sql = " SELECT " +
    			"    wx.CONFIRM confirm, " +
    			"    wx.CONFIRM_BY confirmBy, " +
    			"    wx.CONFIRM_DATE confirmDate, " +
    			"    wx.WO_ID woPmxlId, " +
    			"    wx.STATE state, " +
    			"    wx.STATUS status, " +
    			"    wx.VALUE value, " +
    			"    wx.CREATE_DATE createDate, " +
    			"    wx.USER_CREATED userCreated, " +
    			"    UPPER(wo.CONSTRUCTION_ID||'_'||cwit.NAME||'_'||wx.NAME) keyMap " +
    			"	 ,wx.NAME name " +
    			"FROM " + 
    			"    WO_XDDD_CHECKLIST wx " + 
    			"    LEFT JOIN WO wo on wx.WO_ID = wo.ID " +
    			"    LEFT JOIN CAT_WORK_ITEM_TYPE cwit ON wo.CAT_WORK_ITEM_TYPE_ID = cwit.CAT_WORK_ITEM_TYPE_ID " + 
    			"WHERE " + 
    			"    nvl(wx.CONFIRM,0) = 1 " +
    			"    AND wx.WO_ID IN ( " +
    			"        SELECT " +
    			"            WO_ID " +
    			"        FROM " +
    			"            WO_WORKLOGS " +
    			"        WHERE " +
    			"            CONTENT = 'Thực hiện WO từ hệ thống IOC' " +
    			"    ) ";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.addScalar("confirm", new LongType());
		query.addScalar("confirmBy", new StringType());
		query.addScalar("confirmDate", new DateType());
		query.addScalar("woPmxlId", new LongType());
		query.addScalar("state", new StringType());
		query.addScalar("status", new LongType());
		query.addScalar("value", new DoubleType());
		query.addScalar("createDate", new DateType());
		query.addScalar("userCreated", new StringType());
		query.addScalar("keyMap", new StringType());
		query.addScalar("name", new StringType());
		query.setResultTransformer(Transformers.aliasToBean(WoXdddChecklistDTO.class));
		return query.list();
    }
    
    public Long approveExtentionWoHcFromIoc(WoDTO dto) {
    	StringBuilder sql = new StringBuilder(" UPDATE WO SET FINISH_DATE=:finishDate where ID=:woId ");
    	
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	
    	query.setParameter("woId", dto.getWoId());
    	query.setParameter("finishDate", dto.getFinishDate());
    	
    	return (long)query.executeUpdate();
    }
    //Huy-end
    
    //Huypq-22062022-start
    public CatWorkItemTypeDTO checkExistCatWorkItemType(String catWorkItemTypeName) {
		String sql = "Select CAT_WORK_ITEM_TYPE_ID catWorkItemTypeId, NAME name from CAT_WORK_ITEM_TYPE where upper(name) = upper(:catWorkItemTypeName) ";
		
		SQLQuery query = getSession().createSQLQuery(sql);
		
		query.setParameter("catWorkItemTypeName", catWorkItemTypeName);
		
		query.addScalar("catWorkItemTypeId", new LongType());
		query.addScalar("name", new StringType());
		
		query.setResultTransformer(Transformers.aliasToBean(CatWorkItemTypeDTO.class));
		
		List<CatWorkItemTypeDTO> lst = query.list();
		
		if (lst.size() > 0) {
			return lst.get(0);
		}
		return null;
	}
    
    public SysGroupDTO getCdLevel2BySysGroupIdAndCode(Long sysGroupId, String code) {
    	String sql = " select SYS_GROUP_ID sysGroupId,  " + 
    			"NAME name " + 
    			"from SYS_GROUP  " + 
    			"where PARENT_ID =  " + 
    			"(select REGEXP_SUBSTR(PATH, '[^/]+', 1, 2) from SYS_GROUP where SYS_GROUP_ID=:sysGroupId) " + 
    			"AND upper(code) LIKE upper(:code) ";
    	
		SQLQuery query = getSession().createSQLQuery(sql);
		
		query.addScalar("sysGroupId", new LongType());
		query.addScalar("name", new StringType());
		
		query.setParameter("sysGroupId", sysGroupId);
		query.setParameter("code", "%" + code + "%");
		
		query.setResultTransformer(Transformers.aliasToBean(SysGroupDTO.class));
		
		List<SysGroupDTO> lst = query.list();
		
		if(lst.size()>0) {
			return lst.get(0);
		}
		return null;
    }
    //Huy-end
//    taotq start 01072022
    public Boolean checkWo(Long consId) {
		Boolean check = false;
		String sql1 = "select distinct w.WO_TYPE_ID woTypeId, w.CD_LEVEL_1 cdLevel1 from CONSTRUCTION c LEFT JOIN WO w ON c.CONSTRUCTION_ID = w.CONSTRUCTION_ID where c.CAT_CONSTRUCTION_TYPE_ID =8 AND c.CONSTRUCTION_ID = :constructionId ";
		SQLQuery query1 = getSession().createSQLQuery(sql1);
		query1.setParameter("constructionId", consId);
		query1.setResultTransformer(Transformers.aliasToBean(WoDTO.class));
		query1.addScalar("woTypeId", new LongType());
		query1.addScalar("cdLevel1", new StringType());
		List<WoDTO> lst = query1.list();
		if(lst.size()>0) {
			if(lst.get(0).getWoTypeId() == 1 && lst.get(0).getCdLevel1().equals("242656")) {
				check = true;
			}
		}
		return check;
	}
//    taotq end 01072022

	public void updateConstruction(Long constructionId) {
		// TODO Auto-generated method stub
		String sql = "update CONSTRUCTION SET status = -5 WHERE CONSTRUCTION_ID = :constructionId ";
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("constructionId", constructionId);
		query.executeUpdate();
	}

	//ducpm23 start
	public boolean checkCntContractIsGpxd(Long cntContractId) {
		if (cntContractId == null)
			return false;

		StringBuilder sql = new StringBuilder("SELECT IS_GPXD FROM CNT_CONTRACT WHERE CNT_CONTRACT_ID = :contractId ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.setParameter("contractId", cntContractId);

		query.addScalar("IS_GPXD", new LongType());
		List<Long> isGpxd = query.list();

		if (isGpxd.size() > 0) {
			if(null != isGpxd.get(0) &&  isGpxd.get(0) == 1) return true;
		}
		return false;
	}

	public void updateLicenceName(WoDTO obj) {
		// TODO Auto-generated method stub
		String sql = "update WO SET LICENCE_NAME = :licenceName WHERE ID = :woId ";
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("licenceName", obj.getLicenceName());
		query.setParameter("woId", obj.getWoId());
		query.executeUpdate();
	}
	//ducpm23-end

	public WoBO getWoById(WoDTO wo) {
		StringBuilder sql = new StringBuilder("SELECT * FROM WO WHERE ID = :id");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addEntity(WoBO.class);

		query.setParameter("id", wo.getWoId());
		List<WoBO> list = query.list();
		WoBO result = ObjectUtils.isEmpty(list)? null : list.get(0);
		return result;
	}

	public WoBO findByWoIdAndWoTypeIdAndStatus(Long trId, Long woTypeId, Long active) {
		StringBuilder sql = new StringBuilder("SELECT *  FROM WO w WHERE w.TR_ID =:trId AND w.WO_TYPE_ID = :woTypeId AND w.STATUS = :active ");SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addEntity(WoBO.class);
		query.setParameter("trId", trId);
		query.setParameter("woTypeId", woTypeId);
		query.setParameter("active", active);
		List<WoBO> list = query.list();
		WoBO result = ObjectUtils.isEmpty(list)? null : list.get(0);
		return result;
	}
}
