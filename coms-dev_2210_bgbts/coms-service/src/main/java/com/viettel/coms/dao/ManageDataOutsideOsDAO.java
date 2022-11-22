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

import com.viettel.coms.bo.ManageDataOutsideOsBO;
import com.viettel.coms.dto.ManageDataOutsideOsDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@EnableTransactionManagement
@Transactional
@Repository("manageDataOutsideOsDAO")
public class ManageDataOutsideOsDAO extends BaseFWDAOImpl<ManageDataOutsideOsBO, Long> {

	public ManageDataOutsideOsDAO() {
		this.model = new ManageDataOutsideOsBO();
	}

	public ManageDataOutsideOsDAO(Session session) {
		this.session = session;
	}

	public List<ManageDataOutsideOsDTO> doSearchOS(ManageDataOutsideOsDTO obj) {
		StringBuilder sql = new StringBuilder();
		sql.append("select ");
		sql.append("man.MANAGE_DATA_OUTSIDE_OS_ID manageDataOutsideOsId, ")
		.append("man.CONSTRUCTION_CODE constructionCode, ")
		.append("man.STATION_CODE stationCode, ")
		.append("man.PROVINCE_CODE provinceCode, ")
		.append("man.HD_CONTRACT_CODE hdContractCode, ")
		.append("man.HD_SIGN_DATE hdSignDate, ")
		.append("man.HD_CONTRACT_VALUE hdContractValue, ")
		.append("man.HD_PERFORM_DAY hdPerformDay, ")
		.append("man.CONSTRUCTION_TYPE constructionType, ")
		.append(" (CASE WHEN man.CONSTRUCTION_TYPE=1 then 'Công trình BTS, Costie, SWAP và các công trình nguồn đầu tư TCT ký' "
				+ " WHEN man.CONSTRUCTION_TYPE=2 then 'Công trình Nguồn chi phí' "
				+ " WHEN man.CONSTRUCTION_TYPE=3 then 'Công trình Bảo dưỡng ĐH và MFĐ' "
				+ " WHEN man.CONSTRUCTION_TYPE=4 then 'Công trình Gpon' "
				+ " WHEN man.CONSTRUCTION_TYPE=5 then 'Công trình Hợp đồng 12 đầu việc' "
				+ " WHEN man.CONSTRUCTION_TYPE=6 then 'Công trình Ngoài Tập đoàn' "
				+ " END) constructionTypeName, ")
		
		.append("man.CONTENT content, ")
		.append("man.CAPITAL_NTD capitalNtd, ")
		.append("(CASE WHEN man.CAPITAL_NTD=1 then 'Chi phí' "
				+ " WHEN man.CAPITAL_NTD=2 then 'Đầu tư' "
				+ " WHEN man.CAPITAL_NTD=3 then 'Ngoài tập đoàn' "
				+ " END) capitalNtdName, ")
		
		.append("man.KHTC_SALARY khtcSalary, ")
		.append("man.KHTC_LABOR_OUTSOURCE khtcLaborOutsource, ")
		.append("man.KHTC_COST_MATERIAL khtcCostMaterial, ")
		.append("man.KHTC_COST_HSHC khtcCostHshc, ")
		.append("man.KHTC_COST_TRANSPORT khtcCostTransport, ")
		.append("man.KHTC_COST_ORTHER khtcCostOrther, ")
		.append("man.KHTC_DEPLOYMENT_MONTH khtcDeploymentMonth, ")
		.append("man.KHTC_TOTAL_MONEY khtcTotalMoney, ")
		.append("man.KHTC_EFFECTIVE khtcEffective, ")
		.append("man.KHTC_DESCRIPTION khtcDescription, ")
		.append("man.TU_ADVANCE_DATE tuAdvanceDate, ")
		.append("man.TU_LABOR tuLabor, ")
		.append("man.TU_MATERIAL tuMaterial, ")
		.append("man.TU_HSHC tuHshc, ")
		.append("man.TU_COST_TRANSPORT tuCostTransport, ")
		.append("man.TU_COST_ORTHER tuCostOrther, ")
		.append("man.VTA_SYNCHRONIZE_DATE vtaSynchronizeDate, ")
		.append("man.VTA_VALUE vtaValue, ")
		.append("man.GTSL_QUANTITY_VALUE gtslQuantityValue, ")
		.append("man.HTTC_TDT httcTdt, ")
		.append("man.HTTC_TCTT httcTctt, ")
		.append("man.HTTC_KN httcKn, ")
		.append("man.TTTC_START_DATE tttcStartDate, ")
		.append("man.TTTC_END_DATE tttcEndDate, ")
		.append("man.TTTC_VUONG tttcVuong, ")
		.append("man.TTTC_CLOSE tttcClose, ")
		.append("man.GTSL_COMPLETE_EXPECTED_DATE gtslCompleteExpectedDate, ")
		.append("man.GTSL_DESCRIPTION gtslDescription, ")
		.append("man.TDNT_HSHC_START_DATE tdntHshcStartDate, ")
		.append("man.TDNT_ACCEPTANCE_START_DATE tdntAcceptanceStartDate, ")
		.append("man.TDNT_KTHT_EXPERTISE_DATE tdntKthtExpertiseDate, ")
		.append("man.TDNT_4A_CONTROL_START_DATE tdnt4AControlStartDate, ")
		.append("man.TDNT_SIGN_PROVINCE_DATE tdntSignProvinceDate, ")
		.append("man.TDNT_SEND_TCT_DATE tdntSendTctDate, ")
		.append("man.TDNT_COMPLETE_EXPECTED_DATE tdntCompleteExpectedDate, ")
		.append("man.TDNT_VUONG_DATE tdntVuongDate, ")
		.append("man.TDNT_VUONG_REASON tdntVuongReason, ")
		.append("man.DNQT_QT_CDT_VAT dnqtQtCdtVat, ")
		.append("man.DNQT_QT_CDT_NOT_VAT dnqtQtCdtNotVat, ")
		.append("man.DNQT_ELECTRICAL_PROCEDURES dnqtElectricalProcedures, ")
		.append("man.DNQT_PULL_CABLE_LABOR dnqtPullCableLabor, ")
		.append("man.DNQT_COST_MATERIAL dnqtCostMaterial, ")
		.append("man.DNQT_COST_HSHC dnqtCostHshc, ")
		.append("man.DNQT_COST_TRANSPORT_WAREHOUSE dnqtCostTransportWarehouse, ")
		.append("man.DNQT_COST_ORTHER dnqtCostOrther, ")
		.append("man.DNQT_SALARY_CABLE_ORTHER dnqtSalaryCableOrther, ")
		.append("man.DNQT_WELDING_SALARY dnqtWeldingSalary, ")
		.append("man.DNQT_VAT dnqtVat, ")
		.append("man.DNQT_TOTAL_MONEY dnqtTotalMoney, ")
		.append("man.GTTD_HSHC_HARD_DATE gttdHshcHardDate, ")
		.append("man.GTTD_COMPLETE_EXPERTISE_DATE gttdCompleteExpertiseDate, ")
		.append("man.GTTD_ELECTRICAL_PROCEDURES gttdElectricalProcedures, ")
		.append("man.GTTD_PULL_CABLE_LABOR gttdPullCableLabor, ")
		.append("man.GTTD_COST_MATERIAL gttdCostMaterial, ")
		.append("man.GTTD_COST_HSHC gttdCostHshc, ")
		.append("man.GTTD_COST_TRANSPORT_WAREHOUSE gttdCostTransportWarehouse, ")
		.append("man.GTTD_COST_ORTHER gttdCostOrther, ")
		.append("man.GTTD_SALARY_CABLE_ORTHER gttdSalaryCableOrther, ")
		.append("man.GTTD_WELDING_SALARY gttdWeldingSalary, ")
		.append("man.GTTD_VAT gttdVat, ")
		.append("man.GTTD_TOTAL_MONEY gttdTotalMoney, ")
		.append("man.GTTD_GTTD_PTK gttdGttdPtk, ")
		.append("man.GTTD_HSHC_MONTH gttdHshcMonth, ")
		.append("man.GTTD_SALARY_MONTH gttdSalaryMonth, ")
		.append("man.GTTD_SALARY_REAL gttdSalaryReal, ")
		.append("man.GTTD_HSHC_ERROR gttdHshcError, ")
		.append("man.GTTD_ERROR_REASON gttdErrorReason, ")
		.append("man.QTDN_SUGGESTIONS_DATE qtdnSuggestionsDate, ")
		.append("man.QTDN_VALUE qtdnValue, ")
		.append("man.QTDN_VTNET_DATE qtdnVtnetDate, ")
		.append("man.QTDN_DESCRIPTION qtdnDescription, ")
		.append("man.QTTD_EXPERTISE_EMPLOYEE qttdExpertiseEmployee, ")
		.append("man.QTTD_EXPERTISE_COMPLETE_DATE qttdExpertiseCompleteDate, ")
		.append("man.QTTD_VALUE qttdValue, ")
		.append("man.QTTD_DESCRIPTION qttdDescription, ")
		.append("man.XHD_PTC_DATE xhdPtcDate, ")
		.append("man.XHD_XHD_DATE xhdXhdDate, ")
		.append("man.XHD_SO_HD xhdSoHd, ")
		.append("man.XHD_REVENUE_MONTH xhdRevenueMonth, ")
		.append("man.XHD_DESCRIPTION xhdDescription, ")
		.append("man.TL_SIGN_DATE tlSignDate, ")
		.append("man.TL_VALUE tlValue, ")
		.append("man.TL_DESCRIPTION tlDescription, ")
		.append("man.TL_DIFFERENCE_QUANTITY tlDifferenceQuantity, ")
		.append("man.TL_RATE tlRate, ")
		.append("man.QTNC_PHT_DATE qtncPhtDate, ")
		.append("man.QTNC_PTC_DATE qtncPtcDate, ")
		.append("man.QTNC_VTA_ACCOUNT_DATE qtncVtaAccountDate, ")
		.append("man.QTNC_TAKE_MONEY_DATE qtncTakeMoneyDate, ")
		.append("man.QTNC_VUONG qtncVuong, ")
		.append("man.DESCRIPTION description, ")
		.append("man.STATUS status, ")
		.append("(CASE WHEN man.STATUS=1 THEN 'Thi công' "
				+ " WHEN man.STATUS=2 THEN 'Lập HSHC' "
				+ " WHEN man.STATUS=3 THEN 'Đề nghị quyết toán' "
				+ " WHEN man.STATUS=4 THEN 'Thẩm định quyết toán' "
				+ " WHEN man.STATUS=5 THEN 'Quyết toán CĐT' "
				+ " WHEN man.STATUS=6 THEN 'Xuất hóa đơn' "
				+ " WHEN man.STATUS=7 THEN 'Thanh lý' "
				+ " WHEN man.STATUS=8 THEN 'Vật tư A cấp và thu tiền' "
				+ " END) statusName, ")
		.append("man.CREATED_USER_ID createdUserId, ")
		.append("man.CREATED_DATE createdDate, ")
		.append("man.SCHEDULED_USER_ID scheduledUserId, ")
		.append("man.SCHEDULED_UPDATED_ID scheduledUpdatedId, ")
		.append("man.SUGGESTED_USER_ID suggestedUserId, ")
		.append("man.SUGGESTED_UPDATED_USER_ID suggestedUpdatedUserId, ")
		.append("man.EXPERTISED_USER_ID expertisedUserId, ")
		.append("man.EXPERTISED_UPDATED_USER_ID expertisedUpdatedUserId, ")
		.append("man.SETTLEMENTED_USER_ID settlementedUserId, ")
		.append("man.SETTLEMENTED_UPDATED_USER_ID settlementedUpdatedUserId, ")
		.append("man.INVOICE_USER_ID invoiceUserId, ")
		.append("man.INVOICE_UPDATED_USER_ID invoiceUpdatedUserId, ")
		.append("man.LIQUIDATED_USER_ID liquidatedUserId, ")
		.append("man.LIQUIDATED_UPDATED_USER_ID liquidatedUpdatedUserId, ")
		.append("man.LABOR_USER_ID laborUserId, ")
		.append("man.LABOR_UPDATED_USER_ID laborUpdatedUserId ")
		.append("FROM MANAGE_DATA_OUTSIDE_OS man ")
		.append(" WHERE 1 = 1 and man.status!=0 ");
		
		if(StringUtils.isNotBlank(obj.getStatus())) {
			sql.append(" AND man.status = :status ");
		}
		
		if(StringUtils.isNotBlank(obj.getConstructionCode())) {
			sql.append(" AND man.CONSTRUCTION_CODE = :constructionCode ");
		}
		
		if(StringUtils.isNotBlank(obj.getKeySearch())) {
			sql.append(" AND (upper(man.CONSTRUCTION_CODE) like upper(:keySearch) escape '&' "
					+ " OR upper(man.HD_CONTRACT_CODE) like upper(:keySearch) escape '&' ) ");
		}
		
		if(obj.getListStatus()!=null && obj.getListStatus().size()>0) {
			sql.append(" AND man.status in (:listStatus) ");
		}
		
		sql.append(" ORDER BY man.MANAGE_DATA_OUTSIDE_OS_ID DESC ");
		
		StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (" + sql.toString() +")");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		query.addScalar("manageDataOutsideOsId", new LongType());
		query.addScalar("constructionCode", new StringType());
		query.addScalar("stationCode", new StringType());
		query.addScalar("provinceCode", new StringType());
		query.addScalar("hdContractCode", new StringType());
		query.addScalar("hdSignDate", new DateType());
		query.addScalar("hdContractValue", new DoubleType());
		query.addScalar("hdPerformDay", new LongType());
		query.addScalar("constructionType", new StringType());
		query.addScalar("content", new StringType());
		query.addScalar("capitalNtd", new StringType());
		query.addScalar("khtcSalary", new DoubleType());
		query.addScalar("khtcLaborOutsource", new DoubleType());
		query.addScalar("khtcCostMaterial", new DoubleType());
		query.addScalar("khtcCostHshc", new DoubleType());
		query.addScalar("khtcCostTransport", new DoubleType());
		query.addScalar("khtcCostOrther", new DoubleType());
		query.addScalar("khtcDeploymentMonth", new StringType());
		query.addScalar("khtcTotalMoney", new DoubleType());
		query.addScalar("khtcEffective", new DoubleType());
		query.addScalar("khtcDescription", new StringType());
		query.addScalar("tuAdvanceDate", new DateType());
		query.addScalar("tuLabor", new DoubleType());
		query.addScalar("tuMaterial", new DoubleType());
		query.addScalar("tuHshc", new DoubleType());
		query.addScalar("tuCostTransport", new DoubleType());
		query.addScalar("tuCostOrther", new DoubleType());
		query.addScalar("vtaSynchronizeDate", new DateType());
		query.addScalar("vtaValue", new DoubleType());
		query.addScalar("gtslQuantityValue", new DoubleType());
		query.addScalar("httcTdt", new StringType());
		query.addScalar("httcTctt", new StringType());
		query.addScalar("httcKn", new StringType());
		query.addScalar("tttcStartDate", new DateType());
		query.addScalar("tttcEndDate", new DateType());
		query.addScalar("tttcVuong", new StringType());
		query.addScalar("tttcClose", new StringType());
		query.addScalar("gtslCompleteExpectedDate", new DateType());
		query.addScalar("gtslDescription", new StringType());
		query.addScalar("tdntHshcStartDate", new DateType());
		query.addScalar("tdntAcceptanceStartDate", new DateType());
		query.addScalar("tdntKthtExpertiseDate", new DateType());
		query.addScalar("tdnt4AControlStartDate", new DateType());
		query.addScalar("tdntSignProvinceDate", new DateType());
		query.addScalar("tdntSendTctDate", new DateType());
		query.addScalar("tdntCompleteExpectedDate", new DateType());
		query.addScalar("tdntVuongDate", new DateType());
		query.addScalar("tdntVuongReason", new StringType());
		query.addScalar("dnqtQtCdtVat", new DoubleType());
		query.addScalar("dnqtQtCdtNotVat", new DoubleType());
		query.addScalar("dnqtElectricalProcedures", new DoubleType());
		query.addScalar("dnqtPullCableLabor", new DoubleType());
		query.addScalar("dnqtCostMaterial", new DoubleType());
		query.addScalar("dnqtCostHshc", new DoubleType());
		query.addScalar("dnqtCostTransportWarehouse", new DoubleType());
		query.addScalar("dnqtCostOrther", new DoubleType());
		query.addScalar("dnqtSalaryCableOrther", new DoubleType());
		query.addScalar("dnqtWeldingSalary", new DoubleType());
		query.addScalar("dnqtVat", new DoubleType());
		query.addScalar("dnqtTotalMoney", new DoubleType());
		query.addScalar("gttdHshcHardDate", new DateType());
		query.addScalar("gttdCompleteExpertiseDate", new DateType());
		query.addScalar("gttdElectricalProcedures", new DoubleType());
		query.addScalar("gttdPullCableLabor", new DoubleType());
		query.addScalar("gttdCostMaterial", new DoubleType());
		query.addScalar("gttdCostHshc", new DoubleType());
		query.addScalar("gttdCostTransportWarehouse", new DoubleType());
		query.addScalar("gttdCostOrther", new DoubleType());
		query.addScalar("gttdSalaryCableOrther", new DoubleType());
		query.addScalar("gttdWeldingSalary", new DoubleType());
		query.addScalar("gttdVat", new DoubleType());
		query.addScalar("gttdTotalMoney", new DoubleType());
		query.addScalar("gttdGttdPtk", new DoubleType());
		query.addScalar("gttdHshcMonth", new StringType());
		query.addScalar("gttdSalaryMonth", new StringType());
		query.addScalar("gttdSalaryReal", new DoubleType());
		query.addScalar("gttdHshcError", new StringType());
		query.addScalar("gttdErrorReason", new StringType());
		query.addScalar("qtdnSuggestionsDate", new DateType());
		query.addScalar("qtdnValue", new DoubleType());
		query.addScalar("qtdnVtnetDate", new DateType());
		query.addScalar("qtdnDescription", new StringType());
		query.addScalar("qttdExpertiseEmployee", new StringType());
		query.addScalar("qttdExpertiseCompleteDate", new DateType());
		query.addScalar("qttdValue", new DoubleType());
		query.addScalar("qttdDescription", new StringType());
		query.addScalar("xhdPtcDate", new DateType());
		query.addScalar("xhdXhdDate", new DateType());
		query.addScalar("xhdSoHd", new StringType());
		query.addScalar("xhdRevenueMonth", new StringType());
		query.addScalar("xhdDescription", new StringType());
		query.addScalar("tlSignDate", new DateType());
		query.addScalar("tlValue", new DoubleType());
		query.addScalar("tlDescription", new StringType());
		query.addScalar("tlDifferenceQuantity", new DoubleType());
		query.addScalar("tlRate", new DoubleType());
		query.addScalar("qtncPhtDate", new DateType());
		query.addScalar("qtncPtcDate", new DateType());
		query.addScalar("qtncVtaAccountDate", new DateType());
		query.addScalar("qtncTakeMoneyDate", new DateType());
		query.addScalar("qtncVuong", new StringType());
		query.addScalar("description", new StringType());
		query.addScalar("status", new StringType());
		query.addScalar("constructionTypeName", new StringType());
		query.addScalar("statusName", new StringType());
		query.addScalar("capitalNtdName", new StringType());
		query.addScalar("createdUserId", new LongType());
		query.addScalar("createdDate", new DateType());
		query.addScalar("scheduledUserId", new LongType());
		query.addScalar("scheduledUpdatedId", new LongType());
		query.addScalar("suggestedUserId", new LongType());
		query.addScalar("suggestedUpdatedUserId", new LongType());
		query.addScalar("expertisedUserId", new LongType());
		query.addScalar("expertisedUpdatedUserId", new LongType());
		query.addScalar("settlementedUserId", new LongType());
		query.addScalar("settlementedUpdatedUserId", new LongType());
		query.addScalar("invoiceUserId", new LongType());
		query.addScalar("invoiceUpdatedUserId", new LongType());
		query.addScalar("liquidatedUserId", new LongType());
		query.addScalar("liquidatedUpdatedUserId", new LongType());
		query.addScalar("laborUserId", new LongType());
		query.addScalar("laborUpdatedUserId", new LongType());
		
		query.setResultTransformer(Transformers.aliasToBean(ManageDataOutsideOsDTO.class));
		
		if(StringUtils.isNotBlank(obj.getStatus())) {
			query.setParameter("status", obj.getStatus());
			queryCount.setParameter("status", obj.getStatus());
		}
		
		if(StringUtils.isNotBlank(obj.getConstructionCode())) {
			query.setParameter("constructionCode", obj.getConstructionCode());
			queryCount.setParameter("constructionCode", obj.getConstructionCode());
		}
		
		if(StringUtils.isNotBlank(obj.getKeySearch())) {
			query.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
			queryCount.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
		}
		
		if(obj.getListStatus()!=null && obj.getListStatus().size()>0) {
			query.setParameterList("listStatus", obj.getListStatus());
			queryCount.setParameterList("listStatus", obj.getListStatus());
		}
		
		if (obj.getPageSize() != null) {
            query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
            query.setMaxResults(obj.getPageSize().intValue());
        }
		
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
		
		return query.list();
	}
	
	public List<ManageDataOutsideOsDTO> getAutoCompleteConstruction(ManageDataOutsideOsDTO obj){
    	StringBuilder sql = new StringBuilder(" SELECT DISTINCT cc.code hdContractCode, " + 
    			"  cc.SIGN_DATE hdSignDate, " + 
    			"  cc.PRICE hdContractValue, " + 
    			"  NVL((cc.END_TIME - cc.START_TIME),0) hdPerformDay, " + 
    			"  cp.code provinceCode, " + 
    			"  cons.code constructionCode, " + 
    			"  cons.name constructionName, " + 
    			"  cs.code stationCode, " + 
    			"  (select MAX(CAST(REAL_IE_TRANS_DATE AS DATE)) from SYN_STOCK_TRANS syn where syn.CONSTRUCTION_ID = cons.CONSTRUCTION_ID " + 
    			"    ) vtaSynchronizeDate, " + 
    			"  (select sum(nvl(ser.AMOUNT,0) * nvl(ser.UNIT_PRICE,0)) from SYN_STOCK_TRANS_DETAIL_SERIAL ser " + 
    			"  left join SYN_STOCK_TRANS syn on ser.SYN_STOCK_TRANS_ID = syn.SYN_STOCK_TRANS_ID " + 
    			"  where syn.CONSTRUCTION_ID = cons.CONSTRUCTION_ID " + 
    			"  ) vtaValue " + 
    			"FROM CONSTRUCTION cons " + 
    			"LEFT JOIN CNT_CONSTR_WORK_ITEM_TASK cwit " + 
    			"ON cons.CONSTRUCTION_ID = cwit.CONSTRUCTION_ID " + 
    			"LEFT JOIN CNT_CONTRACT cc " + 
    			"ON cc.CNT_CONTRACT_ID = cwit.CNT_CONTRACT_ID " + 
    			"LEFT JOIN cat_station cs " + 
    			"ON cons.CAT_STATION_ID = cs.CAT_STATION_ID " + 
    			"LEFT JOIN CAT_PROVINCE cp " + 
    			"ON cs.CAT_PROVINCE_ID = cp.CAT_PROVINCE_ID " + 
    			"LEFT JOIN CAT_CONSTRUCTION_TYPE cct " + 
    			"ON cct.CAT_CONSTRUCTION_TYPE_ID = cons.CAT_CONSTRUCTION_TYPE_ID " + 
    			"WHERE cc.CONTRACT_TYPE_O       IS NOT NULL " + 
    			"AND cc.CONTRACT_TYPE_OS_NAME   IS NOT NULL " + 
    			"AND cons.status!                =0 " + 
    			"AND cc.STATUS!                  =0 ");
    	if(StringUtils.isNotBlank(obj.getKeySearch())) {
    		sql.append(" and (upper(cons.CODE) like upper(:keySearch) escape '&'"
    				+ " OR upper(cons.name) like upper(:keySearch) escape '&' ) ");
    	}
    	
    	sql.append(" ORDER BY cons.CODE ASC ");
    	StringBuilder sqlCount = new StringBuilder("select count(*) from (" + sql.toString() + ")");
    	
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
    	
    	query.addScalar("hdSignDate", new DateType());
    	query.addScalar("hdContractCode", new StringType());
    	query.addScalar("hdContractValue", new DoubleType());
    	query.addScalar("hdPerformDay", new LongType());
    	query.addScalar("provinceCode", new StringType());
    	query.addScalar("constructionCode", new StringType());
    	query.addScalar("stationCode", new StringType());
    	query.addScalar("constructionName", new StringType());
    	query.addScalar("vtaSynchronizeDate", new DateType());
    	query.addScalar("vtaValue", new DoubleType());
    	
    	query.setResultTransformer(Transformers.aliasToBean(ManageDataOutsideOsDTO.class));
    	
    	if(StringUtils.isNotBlank(obj.getKeySearch())) {
    		query.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
    		queryCount.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
    	}
    	
    	if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1)
					* obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
		
		return query.list();
    }
	
	public void updateManaOs(ManageDataOutsideOsDTO manageDataOutsideOsDTO) {
		Session ss = getSession();
		ss.update(manageDataOutsideOsDTO.toModel());
	}
	public void addManaOs(ManageDataOutsideOsDTO manageDataOutsideOsDTO) {
		Session ss = getSession();
		ss.save(manageDataOutsideOsDTO.toModel());
	}
	
	//Huypq-20191114-start
	public List<ManageDataOutsideOsDTO> getDataManageOutsideOs(){
		StringBuilder sql = new StringBuilder(" Select CONSTRUCTION_CODE constructionCode, status status, CONSTRUCTION_TYPE constructionType  from MANAGE_DATA_OUTSIDE_OS where status!=0");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("constructionCode", new StringType());
		query.addScalar("status", new StringType());
		query.addScalar("constructionType", new StringType());
		
		query.setResultTransformer(Transformers.aliasToBean(ManageDataOutsideOsDTO.class));
		
		return query.list();
	}
	
	public ManageDataOutsideOsDTO checkDuplicateInDb(String code, Long id){
		StringBuilder sql = new StringBuilder(" Select CONSTRUCTION_CODE constructionCode from MANAGE_DATA_OUTSIDE_OS "
				+ " where status!=0 "
				+ " and CONSTRUCTION_CODE=:code ");
		if(id!=null) {
			sql.append(" AND MANAGE_DATA_OUTSIDE_OS_ID=:id ");
		}
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("constructionCode", new StringType());
		
		query.setParameter("code", code);
		
		if(id!=null) {
			query.setParameter("id", id);
		}
		
		query.setResultTransformer(Transformers.aliasToBean(ManageDataOutsideOsDTO.class));
		
		return (ManageDataOutsideOsDTO)query.uniqueResult();
	}
	
	public void updateSchedule(ManageDataOutsideOsDTO obj) {
		StringBuilder sql = new StringBuilder("update MANAGE_DATA_OUTSIDE_OS set"
				+ " status=2, "
				+ " TDNT_HSHC_START_DATE=:tdntHshcStartDate,"
				+ " TDNT_ACCEPTANCE_START_DATE=:tdntAcceptanceStartDate,"
				+ " TDNT_KTHT_EXPERTISE_DATE=:tdntKthtExpertiseDate,"
				+ " TDNT_4A_CONTROL_START_DATE=:tdnt4AControlStartDate,"
				+ " TDNT_SIGN_PROVINCE_DATE=:tdntSignProvinceDate,"
				+ " TDNT_SEND_TCT_DATE=:tdntSendTctDate,"
				+ " TDNT_COMPLETE_EXPECTED_DATE=:tdntCompleteExpectedDate, "
				+ " SCHEDULED_USER_ID=:scheduledUserId ");
				if(obj.getTdntVuongDate()!=null) {
					sql.append(" ,TDNT_VUONG_DATE=:tdntVuongDate,"
							+ " TDNT_VUONG_REASON=:tdntVuongReason ");
				}
				
				sql.append(" where CONSTRUCTION_CODE=:consCode ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.setParameter("tdntHshcStartDate", obj.getTdntHshcStartDate());
		query.setParameter("tdntAcceptanceStartDate", obj.getTdntAcceptanceStartDate());
		query.setParameter("tdntKthtExpertiseDate", obj.getTdntKthtExpertiseDate());
		query.setParameter("tdnt4AControlStartDate", obj.getTdnt4AControlStartDate());
		query.setParameter("tdntSignProvinceDate", obj.getTdntSignProvinceDate());
		query.setParameter("tdntSendTctDate", obj.getTdntSendTctDate());
		query.setParameter("tdntCompleteExpectedDate", obj.getTdntCompleteExpectedDate());
		query.setParameter("scheduledUserId", obj.getScheduledUserId());
		if(obj.getTdntVuongDate()!=null) {
			query.setParameter("tdntVuongDate", obj.getTdntVuongDate());
			query.setParameter("tdntVuongReason", obj.getTdntVuongReason());
		}
		query.setParameter("consCode", obj.getConstructionCode());
		
		query.executeUpdate();
	}
	
	public void updateListSettle(ManageDataOutsideOsDTO obj) {
		StringBuilder sql = new StringBuilder("update MANAGE_DATA_OUTSIDE_OS set "
				+ " status=3,"
				+ " DNQT_PULL_CABLE_LABOR=:dnqtPullCableLabor,"
				+ " DNQT_COST_MATERIAL=:dnqtCostMaterial,"
				+ " DNQT_COST_HSHC=:dnqtCostHshc,"
				+ " DNQT_COST_TRANSPORT_WAREHOUSE=:dnqtCostTransportWarehouse,"
				+ " DNQT_COST_ORTHER=:dnqtCostOrther,"
				+ " DNQT_SALARY_CABLE_ORTHER=:dnqtSalaryCableOrther,"
				+ " DNQT_VAT=:dnqtVat,"
				+ " DNQT_TOTAL_MONEY=:dnqtTotalMoney, "
				+ " SUGGESTED_USER_ID=:suggestedUserId ");
		
				if(obj.getDnqtQtCdtNotVat()!=null) {
					sql.append(" ,DNQT_QT_CDT_NOT_VAT=:dnqtQtCdtNotVat ");
				}
				
				if(obj.getDnqtQtCdtVat()!=null) {
					sql.append(" ,DNQT_QT_CDT_VAT=:dnqtQtCdtVat ");
				}
				
				if(obj.getDnqtElectricalProcedures()!=null) {
					sql.append(" ,DNQT_ELECTRICAL_PROCEDURES=:dnqtElectricalProcedures ");
				}
				
				if(obj.getDnqtWeldingSalary()!=null) {
					sql.append(" ,DNQT_WELDING_SALARY=:dnqtWeldingSalary ");
				}
				
				sql.append(" where CONSTRUCTION_CODE=:consCode ");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		if(obj.getDnqtQtCdtNotVat()!=null) {
			query.setParameter("dnqtQtCdtNotVat", obj.getDnqtQtCdtNotVat());
		}
		
		if(obj.getDnqtQtCdtVat()!=null) {
			query.setParameter("dnqtQtCdtVat", obj.getDnqtQtCdtVat());
		}
		
		if(obj.getDnqtElectricalProcedures()!=null) {
			query.setParameter("dnqtElectricalProcedures", obj.getDnqtElectricalProcedures());
		}
		
		if(obj.getDnqtWeldingSalary()!=null) {
			query.setParameter("dnqtWeldingSalary", obj.getDnqtWeldingSalary());
		}
		
		query.setParameter("dnqtPullCableLabor", obj.getDnqtPullCableLabor());
		query.setParameter("dnqtCostMaterial", obj.getDnqtCostMaterial());
		query.setParameter("dnqtCostHshc", obj.getDnqtCostHshc());
		query.setParameter("dnqtCostTransportWarehouse", obj.getDnqtCostTransportWarehouse());
		query.setParameter("dnqtCostOrther", obj.getDnqtCostOrther());
		query.setParameter("dnqtSalaryCableOrther", obj.getDnqtSalaryCableOrther());
		query.setParameter("dnqtVat", obj.getDnqtVat());
		query.setParameter("dnqtTotalMoney", obj.getDnqtTotalMoney());
		query.setParameter("suggestedUserId", obj.getSuggestedUserId());
		query.setParameter("consCode", obj.getConstructionCode());
		
		query.executeUpdate();
	}
	
	public void updateListLabor(ManageDataOutsideOsDTO obj) {
		StringBuilder sql = new StringBuilder("update MANAGE_DATA_OUTSIDE_OS set "
				+ " status=8,"
				+ " QTNC_PHT_DATE=:qtncPhtDate,"
				+ " QTNC_PTC_DATE=:qtncPtcDate,"
				+ " QTNC_VTA_ACCOUNT_DATE=:qtncVtaAccountDate,"
				+ " QTNC_TAKE_MONEY_DATE=:qtncTakeMoneyDate, "
				+ " LABOR_USER_ID=:laborUserId ");
				if(StringUtils.isNotBlank(obj.getQtncVuong())) {
					sql.append(" ,QTNC_VUONG=:qtncVuong ");
				}
				sql.append(" where CONSTRUCTION_CODE=:consCode ");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.setParameter("qtncPhtDate", obj.getQtncPhtDate());
		query.setParameter("qtncPtcDate", obj.getQtncPtcDate());
		query.setParameter("qtncVtaAccountDate", obj.getQtncVtaAccountDate());
		query.setParameter("qtncTakeMoneyDate", obj.getQtncTakeMoneyDate());
		query.setParameter("laborUserId", obj.getLaborUserId());
		query.setParameter("consCode", obj.getConstructionCode());
		if(StringUtils.isNotBlank(obj.getQtncVuong())) {
			query.setParameter("qtncVuong", obj.getQtncVuong());
		}
		
		query.executeUpdate();
	}
	//Huy-end
}
