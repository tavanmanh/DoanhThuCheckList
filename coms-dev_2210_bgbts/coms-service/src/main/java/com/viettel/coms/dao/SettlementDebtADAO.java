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
import org.springframework.transaction.annotation.Transactional;

import com.viettel.coms.bo.ConstructionBO;
import com.viettel.coms.dto.SettlementDebtADTO;
import com.viettel.coms.dto.SettlementDebtARpDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import com.viettel.service.base.model.BaseFWModelImpl;

@EnableTransactionManagement
@Transactional
@Repository("settlementDebtADAO")
public class SettlementDebtADAO extends BaseFWDAOImpl<BaseFWModelImpl, Long> {

	public SettlementDebtADAO() {
		this.model = new ConstructionBO();
	}

	public SettlementDebtADAO(Session session) {
		this.session = session;
	}

	public List<SettlementDebtADTO> doSearch(SettlementDebtADTO obj) {
			//Lay ERP lam goc
		StringBuilder sql = new StringBuilder("select " 
				+ " sysG.name bpartnerCrName, a.ProjectCrName projectCrName,a.ContractName contractName,a.ConstructionValue constructionValue,"
				+ " a.ProductDrValue ProductCrValue,a.ProductDrName ProductCrName,b.unit_type_name unitTypeName,"
				+ " sum((select sum(stdr.amount) "
				+ " from SYN_STOCK_TRANS st "
				+ " join syn_stock_trans_Detail std on std.SYN_STOCK_TRANS_ID = st.SYN_STOCK_TRANS_ID "
				+ " join SYN_STOCK_TRANS_DETAIL_SERIAL stdr on stdr.SYN_STOCK_TRANS_DETAIL_ID = std.SYN_STOCK_TRANS_DETAIL_ID " 
				+ " where st.type =2 and st.confirm=1 and to_date(to_char(ST.REAL_IE_TRANS_DATE,'dd/MM/yyyy'),'dd/MM/yyyy') <= :dateFrom -1 "
				+ " and st.CONSTRUCTION_CODE=a.ConstructionValue and a.ProductDrValue=STD.GOODS_CODE))amountTHDu, "

				+ " sum((select sum(stdr.amount* STDR.UNIT_PRICE)   "
				+ " from SYN_STOCK_TRANS st  "
				+ " join syn_stock_trans_Detail std on std.SYN_STOCK_TRANS_ID = st.SYN_STOCK_TRANS_ID "
				+ " join SYN_STOCK_TRANS_DETAIL_SERIAL stdr on stdr.SYN_STOCK_TRANS_DETAIL_ID = std.SYN_STOCK_TRANS_DETAIL_ID " 
				+ " where st.type =2 and st.confirm=1 and to_date(to_char(ST.REAL_IE_TRANS_DATE,'dd/MM/yyyy'),'dd/MM/yyyy') <= :dateFrom -1 "
				+ " and st.CONSTRUCTION_CODE=a.ConstructionValue and a.ProductDrValue=STD.GOODS_CODE))moneyTHDu, "

				+ " sum((select sum(stdr.amount) "
				+ " from SYN_STOCK_TRANS st  "
				+ " join syn_stock_trans_Detail std on std.SYN_STOCK_TRANS_ID = st.SYN_STOCK_TRANS_ID "
				+ " join SYN_STOCK_TRANS_DETAIL_SERIAL stdr on stdr.SYN_STOCK_TRANS_DETAIL_ID = std.SYN_STOCK_TRANS_DETAIL_ID " 
				+ " where st.type =2 and st.confirm=1 and to_date(to_char(ST.REAL_IE_TRANS_DATE,'dd/MM/yyyy'),'dd/MM/yyyy') >= :dateFrom "
				+ " and to_date(to_char(ST.REAL_IE_TRANS_DATE,'dd/MM/yyyy'),'dd/MM/yyyy') <= :dateTo "
				+ " and st.CONSTRUCTION_CODE=a.ConstructionValue and a.ProductDrValue=STD.GOODS_CODE))amountTHNhap, "

				+ " sum((select sum(stdr.amount* STDR.UNIT_PRICE)   "
				+ " from SYN_STOCK_TRANS st  "
				+ " join syn_stock_trans_Detail std on std.SYN_STOCK_TRANS_ID = st.SYN_STOCK_TRANS_ID "
				+ " join SYN_STOCK_TRANS_DETAIL_SERIAL stdr on stdr.SYN_STOCK_TRANS_DETAIL_ID = std.SYN_STOCK_TRANS_DETAIL_ID " 
				+ " where st.type =2 and st.confirm=1 and to_date(to_char(ST.REAL_IE_TRANS_DATE,'dd/MM/yyyy'),'dd/MM/yyyy') >= :dateFrom "
				+ " and to_date(to_char(ST.REAL_IE_TRANS_DATE,'dd/MM/yyyy'),'dd/MM/yyyy') <= :dateTo "
				+ " and st.CONSTRUCTION_CODE=a.ConstructionValue and a.ProductDrValue=STD.GOODS_CODE))moneyTHNhap, "
				+ " sum(a.Quantity)amountErp,sum(a.Amount)moneyErp "

				+ " from SETTLEMENT_DEBT_A a left join goods b on a.ProductDrValue=b.code " 
				+ " left join construction cst on a.ConstructionValue=cst.code left join sys_group sysG on cst.sys_group_id=sysG.sys_group_id where 1=1 ");
		if (obj.getDateFrom() != null) {
			sql.append(" and a.DATE_ACCT_ERP >=:dateFrom ");
		}
		if (obj.getDateTo() != null) {
			sql.append(" and a.DATE_ACCT_ERP <=:dateTo ");
		}
		if (StringUtils.isNotEmpty(obj.getBpartnerDrValue())) {
			sql.append(" and sysG.sys_group_id =:bpartNerCrValue ");
		}
		if (StringUtils.isNotEmpty(obj.getKeySearch())) {
			sql.append(
					" AND (upper(a.ContractName) LIKE upper(:keySearch) OR upper(a.ConstructionValue) LIKE upper(:keySearch))");
		}
		sql.append(" group by a.ProjectCrName,a.ContractName,a.ConstructionValue,"
					+ " a.ProductDrValue,a.ProductDrName,b.unit_type_name,sysG.name ");

		//bao cao ton chuan lay tich hop lam goc
//		StringBuilder sql = new StringBuilder(" with tbl as(select ST.sys_group_id,ST.sys_group_name bpartnerCrName,'' projectCrName," 
//				+ " st.contract_code contractName,st.CONSTRUCTION_CODE constructionValue,st.GOODS_CODE ProductCrValue, "
//				+ " st.GOODS_NAME ProductCrName,st.goods_unit_name unitTypeName, "
//				+ " 0 amountTHDu ,0 moneyTHDu,sum(st.AMOUNT_TOTAL) amountTHNhap, sum(st.TOTAL_MONEY) moneyTHNhap "
//				+ " from SYN_STOCK_DAILY_REPORT ST where ST.REAL_IE_TRANS_DATE >= :dateFrom and ST.REAL_IE_TRANS_DATE <= :dateTo " );
//				if (StringUtils.isNotEmpty(obj.getBpartnerDrValue())) {
//					sql.append(" and ST.sys_group_id =:bpartNerCrValue ");
//				}
//				if (StringUtils.isNotEmpty(obj.getKeySearch())) {
//					sql.append(
//							" AND (upper(ST.contract_code) LIKE upper(:keySearch) OR upper(ST.CONSTRUCTION_CODE) LIKE upper(:keySearch))");
//				}
//				sql.append(" group by ST.sys_group_id,ST.sys_group_name ,st.contract_code ,st.CONSTRUCTION_CODE ,st.GOODS_CODE , st.GOODS_NAME ,st.goods_unit_name "
//				+ " union all select ST.sys_group_id,ST.sys_group_name bpartnerCrName,'' projectCrName, "
//				+ " st.contract_code contractName,st.CONSTRUCTION_CODE constructionValue,st.GOODS_CODE ProductCrValue, "
//				+ " st.GOODS_NAME ProductCrName,st.goods_unit_name unitTypeName,"
//				+ " sum(st.AMOUNT_TOTAL) amountTHDu, sum(st.TOTAL_MONEY) moneyTHDu,0 amountTHNhap,0 moneyTHNhap "
//				+ " from SYN_STOCK_DAILY_REMAIN_ERP ST where ST.REAL_IE_TRANS_DATE =:dateFrom -1 " );
//				if (StringUtils.isNotEmpty(obj.getBpartnerDrValue())) {
//					sql.append(" and ST.sys_group_id =:bpartNerCrValue ");
//				}
//				if (StringUtils.isNotEmpty(obj.getKeySearch())) {
//					sql.append(
//							" AND (upper(ST.contract_code) LIKE upper(:keySearch) OR upper(ST.CONSTRUCTION_CODE) LIKE upper(:keySearch))");
//				}
//				sql.append(" group by ST.sys_group_id,ST.sys_group_name ,st.contract_code ,st.CONSTRUCTION_CODE ,st.GOODS_CODE , st.GOODS_NAME ,st.goods_unit_name )"
//						+ " select bpartnerCrName,projectCrName,contractName,constructionValue,ProductCrValue,ProductCrName,unitTypeName,"
//						+ " sum(amountTHDu)amountTHDu,sum(moneyTHDu)moneyTHDu,sum(amountTHNhap)amountTHNhap,sum(moneyTHNhap)moneyTHNhap ,  "
//						+ " sum((select sum(Quantity) from SETTLEMENT_DEBT_A a where DATE_ACCT_ERP >= :dateFrom and DATE_ACCT_ERP <= :dateTo "
//						+ " and a.ConstructionValue=st.constructionValue and a.ProductDrValue=st.ProductCrValue)) amountErp,"
//						+ " sum((select sum(Amount) from SETTLEMENT_DEBT_A a where DATE_ACCT_ERP >= :dateFrom and DATE_ACCT_ERP <= :dateTo "
//						+ " and a.ConstructionValue=st.constructionValue and a.ProductDrValue=st.ProductCrValue)) moneyErp "
//						+ " from tbl st group by bpartnerCrName,projectCrName,contractName,constructionValue,ProductCrValue,ProductCrName,unitTypeName order by bpartnerCrName,constructionValue "
//				);
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		query.addScalar("bpartnerCrName", new StringType());
		query.addScalar("projectCrName", new StringType());
		query.addScalar("contractName", new StringType());
		query.addScalar("constructionValue", new StringType());
		query.addScalar("productCrValue", new StringType());
		query.addScalar("productCrName", new StringType());
		query.addScalar("unitTypeName", new StringType());
		query.addScalar("amountErp", new DoubleType());
		query.addScalar("moneyErp", new DoubleType());
		query.addScalar("amountTHDu", new DoubleType());
		query.addScalar("moneyTHDu", new DoubleType());
		query.addScalar("amountTHNhap", new DoubleType());
		query.addScalar("moneyTHNhap", new DoubleType());

		query.setResultTransformer(Transformers.aliasToBean(SettlementDebtADTO.class));

		if (obj.getDateFrom() != null) {
			query.setParameter("dateFrom", obj.getDateFrom());
			queryCount.setParameter("dateFrom", obj.getDateFrom());
		}
		if (obj.getDateTo() != null) {
			query.setParameter("dateTo", obj.getDateTo());
			queryCount.setParameter("dateTo", obj.getDateTo());
		}
		if (StringUtils.isNotEmpty(obj.getBpartnerDrValue())) {
			query.setParameter("bpartNerCrValue", obj.getBpartnerDrValue());
			queryCount.setParameter("bpartNerCrValue", obj.getBpartnerDrValue());
		}
		if (StringUtils.isNotEmpty(obj.getKeySearch())) {
			query.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
			queryCount.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
		}

		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		return query.list();
	}

	public List<SettlementDebtARpDTO> doSearchThreeMonth(SettlementDebtARpDTO obj) {
		StringBuilder sql = new StringBuilder("select "
							+" sysG.sys_group_id,sysG.code sys_group_code,sysG.name sysGroupName,"
							+" st.contract_code contractCode,st.construction_code constructionCode,"
							+" to_date(to_char(ST.REAL_IE_TRANS_DATE,'dd/MM/yyyy'),'dd/MM/yyyy') realIeTransDate,"
							+" STD.GOODS_CODE goodsCode,STD.GOODS_NAME goodsName,STD.goods_unit_name goodsUnitName,"
							+" sum(stdr.amount) amountTotal, sum(stdr.amount* STDR.UNIT_PRICE) moneyTotal,"
							+" max(trunc(sysdate)-(real_ie_trans_date+135)) overDay "
							+" from SYN_STOCK_TRANS st "
							+" join syn_stock_trans_Detail std on std.SYN_STOCK_TRANS_ID = st.SYN_STOCK_TRANS_ID "
							+" join SYN_STOCK_TRANS_DETAIL_SERIAL stdr on stdr.SYN_STOCK_TRANS_DETAIL_ID = std.SYN_STOCK_TRANS_DETAIL_ID " 
							+" left join construction cst on st.CONSTRUCTION_CODE=cst.code "
							+" left join sys_group sysG on cst.sys_group_id=sysG.sys_group_id "
							+" where st.type =2 and st.confirm=1 "
							+ " and  EXISTS (select (b.ConstructionValue)  from SETTLEMENT_DEBT_A b "
							+ "where st.construction_code=b.ConstructionValue and STD.goods_code=b.ProductDrValue and b.DATE_ACCT_ERP >= st.real_ie_trans_date + 135) ");
		if (StringUtils.isNotEmpty(obj.getBpartnerDrValue())) {
			sql.append(" and sysG.sys_group_id = :sysGroupId ");
		}
		if (obj.getDateFrom() != null) {
			sql.append(" and to_date(to_char(ST.REAL_IE_TRANS_DATE,'dd/MM/yyyy'),'dd/MM/yyyy') >=:dateFrom ");
		}
		if (obj.getDateTo() != null) {
			sql.append(" and to_date(to_char(ST.REAL_IE_TRANS_DATE,'dd/MM/yyyy'),'dd/MM/yyyy') <=:dateTo ");
		}
		if (StringUtils.isNotEmpty(obj.getKeySearch())) {
			sql.append(
					" AND (upper(contract_code) LIKE upper(:keySearch) OR upper(construction_code) LIKE upper(:keySearch))");
		}
		sql.append(" group by sysG.sys_group_id,sysG.code ,sysG.name , "
					+" st.contract_code,ST.REAL_IE_TRANS_DATE,st.construction_code,"
					+" STD.GOODS_CODE,STD.GOODS_NAME,STD.goods_unit_name  order by sysG.name ");
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
		sqlCount.append(sql.toString());
		sqlCount.append(")");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		query.addScalar("sysGroupName", new StringType());
		query.addScalar("contractCode", new StringType());
		query.addScalar("constructionCode", new StringType());
		query.addScalar("realIeTransDate", new DateType());
		query.addScalar("goodsCode", new StringType());
		query.addScalar("goodsName", new StringType());
		query.addScalar("goodsUnitName", new StringType());
		query.addScalar("amountTotal", new StringType());
		query.addScalar("moneyTotal", new StringType());
		query.addScalar("overDay", new StringType());

		query.setResultTransformer(Transformers.aliasToBean(SettlementDebtARpDTO.class));

		if (StringUtils.isNotEmpty(obj.getBpartnerDrValue())) {
			query.setParameter("sysGroupId", obj.getBpartnerDrValue());
			queryCount.setParameter("sysGroupId", obj.getBpartnerDrValue());
		}
		if (obj.getDateFrom() != null) {
			query.setParameter("dateFrom", obj.getDateFrom());
			queryCount.setParameter("dateFrom", obj.getDateFrom());
		}
		if (obj.getDateTo() != null) {
			query.setParameter("dateTo", obj.getDateTo());
			queryCount.setParameter("dateTo", obj.getDateTo());
		}
		if (StringUtils.isNotEmpty(obj.getKeySearch())) {
			query.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
			queryCount.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
		}

		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		return query.list();
	}
}
