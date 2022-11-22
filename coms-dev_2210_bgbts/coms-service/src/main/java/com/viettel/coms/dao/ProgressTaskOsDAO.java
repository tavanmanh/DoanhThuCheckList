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

import com.viettel.coms.bo.ProgressTaskOsBO;
import com.viettel.coms.dto.ProgressTaskOsDTO;
import com.viettel.coms.dto.RpProgressMonthPlanOsDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

/**
 * @author hailh10
 */
@Repository("progressTaskOsDAO")
public class ProgressTaskOsDAO extends BaseFWDAOImpl<ProgressTaskOsBO, Long> {

	public ProgressTaskOsDAO() {
		this.model = new ProgressTaskOsBO();
	}

	public ProgressTaskOsDAO(Session session) {
		this.session = session;
	}

	@SuppressWarnings("unchecked")
	public List<ProgressTaskOsDTO> doSearch(ProgressTaskOsDTO obj) {
		StringBuilder stringBuilder = new StringBuilder("select ");
		stringBuilder.append("T1.PROGRESS_TASK_OS_ID progressTaskOsId ");
		stringBuilder.append(",T1.CONSTRUCTION_CODE constructionCode ");
		stringBuilder.append(",T1.CNT_CONTRACT_CODE cntContractCode ");
		stringBuilder.append(",T1.TTKV ttkv ");
		stringBuilder.append(",T1.TTKT ttkt ");
		stringBuilder.append(",T1.DESCRIPTION description ");
		stringBuilder.append(",T1.SOURCE_TASK sourceTask ");
		stringBuilder.append(",T1.CONSTRUCTION_TYPE constructionType ");
		stringBuilder.append(",T1.QUANTITY_VALUE quantityValue ");
		stringBuilder.append(",T1.HSHC_VALUE hshcValue ");
		stringBuilder.append(",T1.SALARY_VALUE salaryValue ");
		stringBuilder.append(",T1.BILL_VALUE billValue ");
		stringBuilder.append(",T1.TDSL_ACCOMPLISHED_DATE tdslAccomplishedDate ");
		stringBuilder.append(",T1.TDSL_CONSTRUCTING tdslConstructing ");
		stringBuilder.append(",T1.TDSL_EXPECTED_COMPLETE_DATE tdslExpectedCompleteDate ");
		stringBuilder.append(",T1.TDHS_TCT_NOT_APPROVAL tdhsTctNotApproval ");
		stringBuilder.append(",T1.TDHS_SIGNING_GDCN tdhsSigningGdcn ");
		stringBuilder.append(",T1.TDHS_CONTROL_4A tdhsControl4a ");
		stringBuilder.append(",T1.TDHS_PHT_APPROVALING tdhsPhtApprovaling ");
		stringBuilder.append(",T1.TDHS_PHT_ACCEPTANCING tdhsPhtAcceptancing ");
		stringBuilder.append(",T1.TDHS_TTKT_PROFILE tdhsTtktProfile ");
		stringBuilder.append(",T1.TDHS_EXPECTED_COMPLETE_DATE tdhsExpectedCompleteDate ");
		stringBuilder.append(",T1.TDTT_COLLECT_MONEY_DATE tdttCollectMoneyDate ");
		stringBuilder.append(",T1.TDTT_PROFILE_PHT tdttProfilePht ");
		stringBuilder.append(",T1.TDTT_PROFILE_PTC tdttProfilePtc ");
		stringBuilder.append(",T1.TDTT_EXPECTED_COMPLETE_DATE tdttExpectedCompleteDate ");
		stringBuilder.append(",T1.CREATED_USER_ID createdUserId ").append(",T1.CREATED_DATE createdDate ");
		stringBuilder.append(",T1.UPDATED_USER_ID updatedUserId ").append(",T1.UPDATED_DATE updatedDate ");
		stringBuilder.append(",T1.STATUS status ")
					.append(",T1.MONTH_YEAR monthYear ")
					.append(",T1.CHECK_HTCT checkHtct ")
					.append(",T1.TTKT_ID ttktId ")
					.append(",T1.WORK_ITEM_NAME workItemName ")
					.append(",T1.TYPE type ");
//					.append(",T1.SYS_GROUP_ID sysGroupId ");
		stringBuilder.append("FROM PROGRESS_TASK_OS T1 ");
		stringBuilder.append("WHERE 1=1 AND T1.STATUS!=0 ");
		// stringBuilder.append("WHERE 1=1 AND STATUS=1 ");

		if (StringUtils.isNotBlank(obj.getTtkt())) {
			stringBuilder.append(" and T1.TTKT = :ttkt ");
		}

		if (StringUtils.isNotBlank(obj.getMonthYear())) {
			stringBuilder.append(" and T1.MONTH_YEAR = :monthYear ");
		}

		if(obj.getGroupIdList()!=null && !obj.getGroupIdList().isEmpty()) {
			stringBuilder.append(" AND T1.TTKT_ID in (:groupIdList) ");
		}
		stringBuilder.append(" ORDER BY T1.TTKV,T1.TTKT,T1.CNT_CONTRACT_CODE,T1.CONSTRUCTION_CODE,T1.TYPE,T1.WORK_ITEM_NAME ");
		StringBuilder sqlCount = new StringBuilder("select count(*) from (" + stringBuilder.toString() + ")");

		SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		query.addScalar("progressTaskOsId", new LongType());
		query.addScalar("constructionCode", new StringType());
		query.addScalar("cntContractCode", new StringType());
		query.addScalar("ttkv", new StringType());
		query.addScalar("ttkt", new StringType());
		query.addScalar("description", new StringType());
		query.addScalar("sourceTask", new StringType());
		query.addScalar("constructionType", new StringType());
		query.addScalar("quantityValue", new LongType());
		query.addScalar("hshcValue", new LongType());
		query.addScalar("salaryValue", new LongType());
		query.addScalar("billValue", new LongType());
		query.addScalar("tdslAccomplishedDate", new DateType());
		query.addScalar("tdslConstructing", new DateType());
		query.addScalar("tdslExpectedCompleteDate", new DateType());
		query.addScalar("tdhsTctNotApproval", new DateType());
		query.addScalar("tdhsSigningGdcn", new DateType());
		query.addScalar("tdhsControl4a", new DateType());
		query.addScalar("tdhsPhtApprovaling", new DateType());
		query.addScalar("tdhsPhtAcceptancing", new DateType());
		query.addScalar("tdhsTtktProfile", new DateType());
		query.addScalar("tdhsExpectedCompleteDate", new DateType());
		query.addScalar("tdttCollectMoneyDate", new DateType());
		query.addScalar("tdttProfilePht", new DateType());
		query.addScalar("tdttProfilePtc", new DateType());
		query.addScalar("tdttExpectedCompleteDate", new DateType());
		query.addScalar("createdUserId", new LongType());
		query.addScalar("createdDate", new DateType());
		query.addScalar("updatedUserId", new LongType());
		query.addScalar("updatedDate", new DateType());
		query.addScalar("status", new StringType());
		query.addScalar("monthYear", new StringType());
		query.addScalar("checkHtct", new StringType());
		query.addScalar("ttktId", new LongType());
		query.addScalar("workItemName", new StringType());
		query.addScalar("type", new StringType());
//		query.addScalar("sysGroupId", new LongType());

		if (StringUtils.isNotBlank(obj.getTtkt())) {
			query.setParameter("ttkt", obj.getTtkt());
			queryCount.setParameter("ttkt", obj.getTtkt());
		}

		if (StringUtils.isNotBlank(obj.getMonthYear())) {
			query.setParameter("monthYear", obj.getMonthYear());
			queryCount.setParameter("monthYear", obj.getMonthYear());
		}

		if(obj.getGroupIdList()!=null && !obj.getGroupIdList().isEmpty()) {
			query.setParameterList("groupIdList", obj.getGroupIdList());
			queryCount.setParameterList("groupIdList", obj.getGroupIdList());
		}
		
		query.setResultTransformer(Transformers.aliasToBean(ProgressTaskOsDTO.class));

		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		List ls = query.list();
		return ls;
	}

	@SuppressWarnings("unchecked")
	public List<ProgressTaskOsDTO> getById(ProgressTaskOsDTO obj) {
		StringBuilder stringBuilder = new StringBuilder("SELECT ");
		stringBuilder.append("T1.PROGRESS_TASK_OS_ID progressTaskOsId ");
		stringBuilder.append(",T1.CONSTRUCTION_CODE constructionCode ");
		stringBuilder.append(",T1.CNT_CONTRACT_CODE cntContractCode ");
		stringBuilder.append(",T1.TTKV ttkv ");
		stringBuilder.append(",T1.TTKT ttkt ");
		stringBuilder.append(",T1.DESCRIPTION description ");
		stringBuilder.append(",T1.SOURCE_TASK sourceTask ");
		stringBuilder.append(",T1.CONSTRUCTION_TYPE constructionType ");
		stringBuilder.append(",T1.QUANTITY_VALUE quantityValue ");
		stringBuilder.append(",T1.HSHC_VALUE hshcValue ");
		stringBuilder.append(",T1.SALARY_VALUE salaryValue ");
		stringBuilder.append(",T1.BILL_VALUE billValue ");
		stringBuilder.append(",T1.TDSL_ACCOMPLISHED_DATE tdslAccomplishedDate ");
		stringBuilder.append(",T1.TDSL_CONSTRUCTING tdslConstructing ");
		stringBuilder.append(",T1.TDSL_EXPECTED_COMPLETE_DATE tdslExpectedCompleteDate ");
		stringBuilder.append(",T1.TDHS_TCT_NOT_APPROVAL tdhsTctNotApproval ");
		stringBuilder.append(",T1.TDHS_SIGNING_GDCN tdhsSigningGdcn ");
		stringBuilder.append(",T1.TDHS_CONTROL_4A tdhsControl4a ");
		stringBuilder.append(",T1.TDHS_PHT_APPROVALING tdhsPhtApprovaling ");
		stringBuilder.append(",T1.TDHS_PHT_ACCEPTANCING tdhsPhtAcceptancing ");
		stringBuilder.append(",T1.TDHS_TTKT_PROFILE tdhsTtktProfile ");
		stringBuilder.append(",T1.TDHS_EXPECTED_COMPLETE_DATE tdhsExpectedCompleteDate ");
		stringBuilder.append(",T1.TDTT_COLLECT_MONEY_DATE tdttCollectMoneyDate ");
		stringBuilder.append(",T1.TDTT_PROFILE_PHT tdttProfilePht ");
		stringBuilder.append(",T1.TDTT_PROFILE_PTC tdttProfilePtc ");
		stringBuilder.append(",T1.TDTT_EXPECTED_COMPLETE_DATE tdttExpectedCompleteDate ");
		stringBuilder.append(",T1.CREATED_USER_ID createdUserId ").append(",T1.CREATED_DATE createdDate ");
		stringBuilder.append(",T1.UPDATED_USER_ID updatedUserId ").append(",T1.UPDATED_DATE updatedDate ");
		stringBuilder.append(",T1.STATUS status ")
				.append(",T1.MONTH_YEAR monthYear ")
				.append(",T1.CHECK_HTCT checkHtct ")
				.append(",T1.APPROVE_REVENUE_DATE approveRevenueDate ")
				.append(",T1.APPROVE_COMPLETE_DATE approveCompleteDate ")
				.append(",T1.TTKT_ID ttktId ")
				.append(",T1.WORK_ITEM_NAME workItemName ")
				.append(",T1.TYPE type ");
//				.append(",T1.SYS_GROUP_ID sysGroupId ");
		stringBuilder.append("FROM PROGRESS_TASK_OS T1 ");
		stringBuilder.append("WHERE T1.STATUS!=0 AND T1.MONTH_YEAR = :monthYear AND T1.TTKT=:ttkt ");
		stringBuilder.append(" ORDER BY T1.CNT_CONTRACT_CODE,T1.CONSTRUCTION_CODE,T1.TYPE,T1.WORK_ITEM_NAME ");
		StringBuilder sqlCount = new StringBuilder("select count(*) from (" + stringBuilder.toString() + ")");

		SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		query.addScalar("progressTaskOsId", new LongType());
		query.addScalar("constructionCode", new StringType());
		query.addScalar("cntContractCode", new StringType());
		query.addScalar("ttkv", new StringType());
		query.addScalar("ttkt", new StringType());
		query.addScalar("description", new StringType());
		query.addScalar("sourceTask", new StringType());
		query.addScalar("constructionType", new StringType());
		query.addScalar("quantityValue", new LongType());
		query.addScalar("hshcValue", new LongType());
		query.addScalar("salaryValue", new LongType());
		query.addScalar("billValue", new LongType());
		query.addScalar("tdslAccomplishedDate", new DateType());
		query.addScalar("tdslConstructing", new DateType());
		query.addScalar("tdslExpectedCompleteDate", new DateType());
		query.addScalar("tdhsTctNotApproval", new DateType());
		query.addScalar("tdhsSigningGdcn", new DateType());
		query.addScalar("tdhsControl4a", new DateType());
		query.addScalar("tdhsPhtApprovaling", new DateType());
		query.addScalar("tdhsPhtAcceptancing", new DateType());
		query.addScalar("tdhsTtktProfile", new DateType());
		query.addScalar("tdhsExpectedCompleteDate", new DateType());
		query.addScalar("tdttCollectMoneyDate", new DateType());
		query.addScalar("tdttProfilePht", new DateType());
		query.addScalar("tdttProfilePtc", new DateType());
		query.addScalar("tdttExpectedCompleteDate", new DateType());
		query.addScalar("createdUserId", new LongType());
		query.addScalar("createdDate", new DateType());
		query.addScalar("updatedUserId", new LongType());
		query.addScalar("updatedDate", new DateType());
		query.addScalar("status", new StringType());
		query.addScalar("monthYear", new StringType());
		query.addScalar("checkHtct", new StringType());
		query.addScalar("approveRevenueDate", new DateType());
		query.addScalar("approveCompleteDate", new DateType());
		query.addScalar("ttktId", new LongType());
		query.addScalar("workItemName", new StringType());
		query.addScalar("type", new StringType());
//		query.addScalar("sysGroupId", new LongType());
		
		query.setParameter("monthYear", obj.getMonthYear());
		queryCount.setParameter("monthYear", obj.getMonthYear());
		query.setParameter("ttkt", obj.getTtkt());
		queryCount.setParameter("ttkt", obj.getTtkt());

		query.setResultTransformer(Transformers.aliasToBean(ProgressTaskOsDTO.class));

		return query.list();
	}

	public List<ProgressTaskOsDTO> getDataTaskByProvince(ProgressTaskOsDTO obj) {
		StringBuilder sql = new StringBuilder(
				" with tbl as(SELECT  '1' type, " + 
				"					(SELECT MAX(b.code) FROM CNT_CONSTR_WORK_ITEM_TASK cnt_task,CNT_CONTRACT b " + 
				"					 WHERE cnt_task.CNT_CONTRACT_ID=b.CNT_CONTRACT_ID " + 
				"					  AND b.CONTRACT_TYPE = 0 and cnt_task.status =1 AND b.status !=0 " + 
				"					AND cnt_task.CONSTRUCTION_ID  =ct.CONSTRUCTION_ID) cntContractCode,   " + 
				"          cons.code constructionCode,  " + 
				"					workItem.name workItemName, " + 
				"					ct.SOURCE_WORK sourceTask,   " + 
				"          ct.CONSTRUCTION_TYPE constructionType, " + 
				"					ct.quantity quantityValue, " + 
				"					0 salaryValue, " + 
				"					0 hshcValue " + 
				"					,0 billValue, " + 
				"					workItem.complete_date tdslAccomplishedDate, " + 
				"					null approveCompleteDate, " +
				"					null approveRevenueDate, " + 
				"					null tdttCollectMoneyDate					 " + 
				"			FROM    " + 
				"          CONSTRUCTION_TASK ct,   " + 
				"          DETAIL_MONTH_PLAN plan,   " + 
				"					CONSTRUCTION cons, " + 
				"					work_item workItem " + 
				"                  WHERE " + 
				"				    ct.DETAIL_MONTH_PLAN_ID = plan.DETAIL_MONTH_PLAN_ID and plan.SIGN_STATE =3 and plan.status=1 and ct.type=1 and level_id=3 " + 
				"				   and ct.CONSTRUCTION_ID  =cons.CONSTRUCTION_ID and ct.work_item_id  =workItem.work_item_id AND cons.STATUS !=0       " + 
				"				  and cons.CONSTRUCTION_id=workItem.CONSTRUCTION_id " + 
				"                  AND plan.MONTH               = EXTRACT(MONTH FROM to_date(:dateOptions,'MM/yyyy'))   " + 
				"                  AND plan.YEAR = EXTRACT(YEAR FROM to_date(:dateOptions,'MM/yyyy'))   " + 
				"                  AND plan.SYS_GROUP_ID IN   " + 
				"                    (SELECT sys_group_id   " + 
				"                    FROM sys_group   " + 
				"                    WHERE PROVINCE_CODE      =:code   " + 
				"                    AND sys_group.GROUP_LEVEL=2   " + 
				"                    )   " + 
				"					union all " + 
				"		SELECT  '2' type, " + 
				"                    	(SELECT MAX(b.code) FROM CNT_CONSTR_WORK_ITEM_TASK cnt_task,CNT_CONTRACT b " + 
				"					 WHERE cnt_task.CNT_CONTRACT_ID=b.CNT_CONTRACT_ID " + 
				"					  AND b.CONTRACT_TYPE = 0 and cnt_task.status =1 AND b.status !=0 " + 
				"					AND cnt_task.CONSTRUCTION_ID  =ct.CONSTRUCTION_ID)cntContractCode,    " + 
				"          cons.code constructionCode,   " + 
				"					null workItemName, " + 
				"					ct.SOURCE_WORK sourceTask,   " + 
				"          ct.CONSTRUCTION_TYPE constructionType, " + 
				"					0 quantityValue, " + 
				"					ct.quantity salaryValue, " + 
				"					0 hshcValue " + 
				"					,0 billValue, " + 
				"					null tdslAccomplishedDate, " + 
				"					cons.APPROVE_COMPLETE_DATE approveCompleteDate, " +
				"					null approveRevenueDate, " + 
				"					null tdttCollectMoneyDate					 " + 
				" " + 
				"		FROM    " + 
				"          CONSTRUCTION_TASK ct,   " + 
				"          DETAIL_MONTH_PLAN plan,   " + 
				"					CONSTRUCTION cons " + 
				"                  WHERE " + 
				"				    ct.DETAIL_MONTH_PLAN_ID = plan.DETAIL_MONTH_PLAN_ID and plan.SIGN_STATE =3 and plan.status=1 and ct.type=2 and level_id=4 " + 
				"				   and ct.CONSTRUCTION_ID  =cons.CONSTRUCTION_ID AND cons.STATUS !=0       " + 
				"                  AND plan.MONTH               = EXTRACT(MONTH FROM to_date(:dateOptions,'MM/yyyy'))   " + 
				"                  AND plan.YEAR = EXTRACT(YEAR FROM to_date(:dateOptions,'MM/yyyy'))   " + 
				"                  AND plan.SYS_GROUP_ID IN   " + 
				"                    (SELECT sys_group_id   " + 
				"                    FROM sys_group   " + 
				"                    WHERE PROVINCE_CODE      =:code   " + 
				"                    AND sys_group.GROUP_LEVEL=2   " + 
				"                    )   " + 
				"					union all " + 
				"					 " + 
				"					SELECT  '3' type, " + 
				"                    	(SELECT MAX(b.code) FROM CNT_CONSTR_WORK_ITEM_TASK cnt_task,CNT_CONTRACT b " + 
				"					 WHERE cnt_task.CNT_CONTRACT_ID=b.CNT_CONTRACT_ID " + 
				"					  AND b.CONTRACT_TYPE = 0 and cnt_task.status =1 AND b.status !=0 " + 
				"					AND cnt_task.CONSTRUCTION_ID  =ct.CONSTRUCTION_ID)cntContractCode, " + 
				"          cons.code constructionCode,   " + 
				"					null workItemName, " + 
				"					ct.SOURCE_WORK sourceTask,   " + 
				"          ct.CONSTRUCTION_TYPE constructionType, " + 
				"					0 quantityValue, " + 
				"					 0 salaryValue, " + 
				"					ct.quantity hshcValue " + 
				"					,0 billValue, " + 
				"					null tdslAccomplishedDate, " + 
				"					null approveCompleteDate, " +
				"					cons.APPROVE_REVENUE_DATE approveRevenueDate, " + 
				"					null tdttCollectMoneyDate					 " + 
				" " + 
				"  FROM    " + 
				"          CONSTRUCTION_TASK ct,   " + 
				"          DETAIL_MONTH_PLAN plan,   " + 
				"					CONSTRUCTION cons  " + 
				"                  WHERE " + 
				"				    ct.DETAIL_MONTH_PLAN_ID = plan.DETAIL_MONTH_PLAN_ID and plan.SIGN_STATE =3 and plan.status=1 and ct.type=3 and level_id=4 " + 
				"				   and ct.CONSTRUCTION_ID  =cons.CONSTRUCTION_ID AND cons.STATUS !=0       " + 
				"                  AND plan.MONTH               = EXTRACT(MONTH FROM to_date(:dateOptions,'MM/yyyy'))   " + 
				"                  AND plan.YEAR = EXTRACT(YEAR FROM to_date(:dateOptions,'MM/yyyy'))   " + 
				"                  AND plan.SYS_GROUP_ID IN   " + 
				"                    (SELECT sys_group_id   " + 
				"                    FROM sys_group   " + 
				"                    WHERE PROVINCE_CODE      =:code   " + 
				"                    AND sys_group.GROUP_LEVEL=2   " + 
				"                    )   " + 
				"					union all " + 
				" " + 
				"		SELECT  '4' type, " + 
				"          cash.CNT_CONTRACT_CODE cntContractCode,   " + 
				"          cash.CONSTRUCTION_CODE constructionCode,  " + 
				"          NULL workItemName,   " + 
				"          NULL sourceTask,   " + 
				"          NULL constructionType,   " + 
				"					0 quantityValue, " + 
				"					0 salaryValue, " + 
				"					0 hshcValue, " + 
				"          (cash.BILL_VALUE * 1000000) billValue, " + 
				"					null tdslAccomplishedDate, " + 
				"					null approveCompleteDate, " +
				"					null approveRevenueDate, " + 
				"					cash.END_DATE tdttCollectMoneyDate					 " + 
				"                  FROM REVOKE_CASH_MONTH_PLAN cash   " + 
				"                  LEFT JOIN DETAIL_MONTH_PLAN monthPlan   " + 
				"                  ON cash.DETAIL_MONTH_PLAN_ID = monthPlan.DETAIL_MONTH_PLAN_ID   " + 
				"                  WHERE cash.STATUS            = 2   " + 
				"                  AND cash.PROVINCE_CODE       = :code   " + 
				"                  AND monthPlan.SIGN_STATE     =3   " + 
				"                  AND monthPlan.MONTH               = EXTRACT(MONTH FROM to_date(:dateOptions,'MM/yyyy'))    " + 
				"                  AND monthPlan.YEAR = EXTRACT(YEAR FROM to_date(:dateOptions,'MM/yyyy'))  " + 
				" ) select * from tbl order by cntContractCode,constructionCode,type,workItemName ");
		StringBuilder sqlCount = new StringBuilder("select count(*) from (" + sql.toString() + ")");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		query.addScalar("cntContractCode", new StringType());
		query.addScalar("constructionCode", new StringType());
		query.addScalar("sourceTask", new StringType());
		query.addScalar("constructionType", new StringType());
		query.addScalar("workItemName", new StringType());
		query.addScalar("quantityValue", new LongType());
		query.addScalar("hshcValue", new LongType());
		query.addScalar("salaryValue", new LongType());
		query.addScalar("billValue", new LongType());
		query.addScalar("tdslAccomplishedDate", new DateType());
		query.addScalar("tdttCollectMoneyDate", new DateType());
		query.addScalar("approveRevenueDate", new DateType());
		query.addScalar("approveCompleteDate", new DateType());
		query.addScalar("type", new StringType());

		query.setResultTransformer(Transformers.aliasToBean(ProgressTaskOsDTO.class));

		query.setParameter("code", obj.getTtkt());
		queryCount.setParameter("code", obj.getTtkt());

		query.setParameter("dateOptions", obj.getMonthYear());
		queryCount.setParameter("dateOptions", obj.getMonthYear());

		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<ProgressTaskOsDTO> doSearchMain(ProgressTaskOsDTO obj, List<String> groupIdList) {
		StringBuilder stringBuilder = new StringBuilder("select DISTINCT ");
		stringBuilder.append("T1.TTKV ttkv ");
		stringBuilder.append(",T1.TTKT ttkt ")
					.append(",T1.MONTH_YEAR monthYear ")
					.append(",cp.name provinceName ")
					.append(",cp.CAT_PROVINCE_ID ttktId ");
		stringBuilder.append("FROM PROGRESS_TASK_OS T1 ");
		stringBuilder.append("left join cat_province cp on T1.TTKT = cp.code ");
		stringBuilder.append("WHERE 1=1 AND T1.STATUS!=0 ");

		if (StringUtils.isNoneBlank(obj.getTtkt())) {
			stringBuilder.append(" AND T1.TTKT = :ttkt ");
		}

		if (StringUtils.isNotBlank(obj.getMonthYear())) {
			stringBuilder.append(" AND T1.MONTH_YEAR = :monthYear ");
		}

		if(groupIdList!=null && !groupIdList.isEmpty()) {
			stringBuilder.append(" AND T1.TTKT_ID in (:groupIdList) ");
		}
		
		StringBuilder sqlCount = new StringBuilder("select count(*) from (" + stringBuilder.toString() + ")");

		SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		query.addScalar("ttkv", new StringType());
		query.addScalar("ttkt", new StringType());
		query.addScalar("monthYear", new StringType());
		query.addScalar("provinceName", new StringType());
		query.addScalar("ttktId", new LongType());

		if (StringUtils.isNoneBlank(obj.getTtkt())) {
			query.setParameter("ttkt", obj.getTtkt());
			queryCount.setParameter("ttkt", obj.getTtkt());
		}

		if (StringUtils.isNotBlank(obj.getMonthYear())) {
			query.setParameter("monthYear", obj.getMonthYear());
			queryCount.setParameter("monthYear", obj.getMonthYear());
		}

		if(groupIdList!=null && !groupIdList.isEmpty()) {
			query.setParameterList("groupIdList", groupIdList);
			queryCount.setParameterList("groupIdList", groupIdList);
		}
		
		query.setResultTransformer(Transformers.aliasToBean(ProgressTaskOsDTO.class));

		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

		List ls = query.list();
		return ls;
	}

	public Long deleteRecord(ProgressTaskOsDTO obj) {
//		StringBuilder sql = new StringBuilder("update PROGRESS_TASK_OS set status=:status," + " updated_date=:date,"
//				+ " updated_user_id=:userId" + " where month_year=:monthYear and ttkt=:ttkt");
		StringBuilder sql = new StringBuilder("DELETE FROM PROGRESS_TASK_OS where month_year=:monthYear and ttkt=:ttkt");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
//		query.setParameter("status", obj.getStatus());
//		query.setParameter("date", obj.getUpdatedDate());
//		query.setParameter("userId", obj.getUpdatedUserId());
		query.setParameter("monthYear", obj.getMonthYear());
		query.setParameter("ttkt", obj.getTtkt());
		return (long) query.executeUpdate();
	}
	
	public void tdQLCHT(StringBuilder sql, ProgressTaskOsDTO obj) {
		sql.append(" tdql as (select  " + "pt.TTKV ttkv, " + "pt.TTKT ttkt, "
				+ "nvl(MAX(taskOs.valueSalary),0) tdQlKhThang, "
				+ "nvl((select sum(pt1.SALARY_VALUE)  "
//hoanm1_20200710_start
//+ "         from PROGRESS_TASK_OS pt1 where pt1.status!=0 and pt1.TDSL_ACCOMPLISHED_DATE is not null and pt1.TTKT = pt.TTKT ");
+ "         from PROGRESS_TASK_OS pt1 where pt1.status!=0 and pt1.TDHS_TCT_NOT_APPROVAL is not null and pt1.TTKT = pt.TTKT ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("         group by pt1.TTKT),0) tdQlDuKienHoanThanhValue, " 
				+ "null tdQlNguyenNhanKoHt "
				+ "from PROGRESS_TASK_OS pt " + "left join (select sum(task.SALARY_VALUE) valueSalary, task.TTKT ttkt  "
				+ "from PROGRESS_TASK_OS task where task.STATUS!=0 ");
		ifElseToanQuoc(sql,obj,"task");
		sql.append(" group by  task.TTKV,task.TTKT) taskOs "
				+ "on taskOs.ttkt = pt.TTKT " + "where 1=1 ");
	}
	
	public void tdSlXlCHT(StringBuilder sql, ProgressTaskOsDTO obj) {
		sql.append(" ,tdSlXl as (select  " + "pt.TTKV ttkv, " + "pt.TTKT ttkt, "
				+ "nvl(MAX(taskOs.valueQuantity),0) tdSlXlKhThang, " 
//				+ "nvl((select sum(pt1.QUANTITY_VALUE) " + "			from PROGRESS_TASK_OS pt1  "
//				+ "      where pt1.status!=0 and pt1.TDSL_ACCOMPLISHED_DATE is not null  " 
				+ "nvl((select sum(pt1.QUANTITY_VALUE) "
				+ "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 "
				+ "      and pt1.TTKT = pt.TTKT "
				+ "      and pt1.SOURCE_TASK in (1,4,5) and pt1.TDSL_ACCOMPLISHED_DATE is not null ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdSlXlDuKienHoanThanhValue, "
				+ "null tdSlXlNguyenNhanKoHt " + "from PROGRESS_TASK_OS pt "
				+ "left join (select sum(task.QUANTITY_VALUE) valueQuantity, task.TTKT ttkt  "
				+ "from PROGRESS_TASK_OS task where task.STATUS!=0 and task.SOURCE_TASK  in (1,4,5) ");
		ifElseToanQuoc(sql,obj,"task");
		sql.append(" group by  task.TTKV,task.TTKT) taskOs " + "on taskOs.ttkt = pt.TTKT " + "where 1=1 ");
	}
	
	public void tdSlCpCHT(StringBuilder sql, ProgressTaskOsDTO obj) {
		sql.append(" ,tdSlCp as (select  " + "pt.TTKV ttkv, " + "pt.TTKT ttkt, "
				+ "nvl(MAX(taskOs.valueQuantity),0) tdSlCpKhThang, " 
//				+ "nvl((select sum(pt1.QUANTITY_VALUE) " + "			from PROGRESS_TASK_OS pt1  "
//				+ "      where pt1.status!=0 and pt1.TDSL_ACCOMPLISHED_DATE is not null  " 
				+ "nvl((select sum(pt1.QUANTITY_VALUE) "
				+ "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 "
				+ "      and pt1.TTKT = pt.TTKT "
				+ "      and pt1.SOURCE_TASK=2 and pt1.TDSL_ACCOMPLISHED_DATE is not null ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdSlCpDuKienHoanThanhValue, "
				+ "null tdSlCpNguyenNhanKoHt " + "from PROGRESS_TASK_OS pt "
				+ "left join (select sum(task.QUANTITY_VALUE) valueQuantity, task.TTKT ttkt  "
				+ "from PROGRESS_TASK_OS task where task.STATUS!=0 and task.SOURCE_TASK=2 ");
		ifElseToanQuoc(sql,obj,"task");
		sql.append(" group by  task.TTKV,task.TTKT) taskOs " + "on taskOs.ttkt = pt.TTKT " + "where 1=1 ");
	}
	
	public void tdSlNtdCHT(StringBuilder sql, ProgressTaskOsDTO obj) {
		sql.append(" ,tdSlNtd as (select  " + "pt.TTKV ttkv, " + "pt.TTKT ttkt, "
				+ "nvl(MAX(taskOs.valueQuantity),0) tdSlNtdKhThang, " 
//				+ "nvl((select sum(pt1.QUANTITY_VALUE) " + "			from PROGRESS_TASK_OS pt1  "
//				+ "      where pt1.status!=0 and pt1.TDSL_ACCOMPLISHED_DATE is not null  " 
				+ "nvl((select sum(pt1.QUANTITY_VALUE) "
				+ "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 "
				+ "      and pt1.TTKT = pt.TTKT "
				+ "      and pt1.SOURCE_TASK=3 and pt1.TDSL_ACCOMPLISHED_DATE is not null ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdSlNtdDuKienHoanThanhValue, "
				+ "null tdSlNtdNguyenNhanKoHt " + "from PROGRESS_TASK_OS pt "
				+ "left join (select sum(task.QUANTITY_VALUE) valueQuantity, task.TTKT ttkt  "
				+ "from PROGRESS_TASK_OS task where task.STATUS!=0 and task.SOURCE_TASK=3 ");
		ifElseToanQuoc(sql,obj,"task");
		sql.append(" group by  task.TTKV,task.TTKT) taskOs " + "on taskOs.ttkt = pt.TTKT " + "where 1=1 ");
	}
	
	public void tdSlXdddCHT(StringBuilder sql, ProgressTaskOsDTO obj) {
		sql.append(" ,tdSlXddd as (select pt.TTKV ttkv, " + "pt.TTKT ttkt, "
				+ "nvl(MAX(taskOs.valueQuantity),0) tdSlXdddKhThang, " 
//				+ "nvl((select sum(pt1.QUANTITY_VALUE) " + "			from PROGRESS_TASK_OS pt1  "
//				+ "      where pt1.status!=0 and pt1.TDSL_ACCOMPLISHED_DATE is not null  "
				+ "nvl((select sum(pt1.QUANTITY_VALUE) "
				+ "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 "
				+ "      and pt1.TTKT = pt.TTKT " + "      and pt1.SOURCE_TASK=6 and pt1.TDSL_ACCOMPLISHED_DATE is not null ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdSlXdddDuKienHoanThanhValue " 
				+ "from PROGRESS_TASK_OS pt "
				+ "left join (select " + "task.TTKT ttkt, " + "sum(task.QUANTITY_VALUE) valueQuantity "
				+ "from PROGRESS_TASK_OS task " + "where task.status!=0 and task.SOURCE_TASK = 6 ");
		ifElseToanQuoc(sql,obj,"task");
		sql.append(" group by "
				+ "task.TTKT) taskOs " + "on pt.ttkt = taskOs.ttkt where 1=1 ");
	}
	
	public void tdHshcXlCHT(StringBuilder sql, ProgressTaskOsDTO obj) {
		sql.append(" ,tdHshcXl as (select  " + "pt.TTKV ttkv, " + "pt.TTKT ttkt, "
				+ "nvl(MAX(taskOs.valueHshc),0) tdHshcXlKhThang, "
				+ "nvl((select sum(pt1.HSHC_VALUE) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDHS_TCT_NOT_APPROVAL is not null  "
				+ "      and pt1.TTKT = pt.TTKT " + "      and pt1.SOURCE_TASK=1 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcXlDuKienHoanThanhValue, " + "null tdHshcXlNguyenNhanKoHt "
				+ "from PROGRESS_TASK_OS pt " + "left join (select sum(task.HSHC_VALUE) valueHshc, task.TTKT ttkt  "
				+ "from PROGRESS_TASK_OS task where task.STATUS!=0 and task.SOURCE_TASK=1 ");
		ifElseToanQuoc(sql,obj,"task");
		sql.append(" group by  task.TTKV,task.TTKT) taskOs " + "on taskOs.ttkt = pt.TTKT " + "where 1=1 ");
	}
	
	public void tdHshcNtdCHT(StringBuilder sql, ProgressTaskOsDTO obj) {
		sql.append(" ,tdHshcNtd as (select  " + "pt.TTKV ttkv, " + "pt.TTKT ttkt, "
				+ "nvl(MAX(taskOs.valueHshc),0) tdHshcNtdKhThang, "
				+ "nvl((select sum(pt1.HSHC_VALUE) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDHS_TCT_NOT_APPROVAL is not null  "
				+ "      and pt1.TTKT = pt.TTKT " + "      and pt1.SOURCE_TASK=3 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcNtdDuKienHoanThanhValue, " + "null tdHshcNtdNguyenNhanKoHt "
				+ "from PROGRESS_TASK_OS pt " + "left join (select sum(task.HSHC_VALUE) valueHshc, task.TTKT ttkt  "
				+ "from PROGRESS_TASK_OS task where task.STATUS!=0 and task.SOURCE_TASK=3 ");
		ifElseToanQuoc(sql,obj,"task");
		sql.append(" group by  task.TTKV,task.TTKT) taskOs " + "on taskOs.ttkt = pt.TTKT " + "where 1=1 ");
	}
	
	public void tdHshcCpCHT(StringBuilder sql, ProgressTaskOsDTO obj) {
		sql.append(" ,tdHshcCp as (select  " + "pt.TTKV ttkv, " + "pt.TTKT ttkt, "
				+ "nvl(MAX(taskOs.valueHshc),0) tdHshcCpKhThang, "
				+ "nvl((select sum(pt1.HSHC_VALUE) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDHS_TCT_NOT_APPROVAL is not null  "
				+ "      and pt1.TTKT = pt.TTKT " + "      and pt1.SOURCE_TASK=2 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcCpDuKienHoanThanhValue, " 
				+ "null tdHshcCpNguyenNhanKoHt "
				+ "from PROGRESS_TASK_OS pt " + "left join (select sum(task.HSHC_VALUE) valueHshc, task.TTKT ttkt  "
				+ "from PROGRESS_TASK_OS task where task.STATUS!=0 and task.SOURCE_TASK=2 ");
		ifElseToanQuoc(sql,obj,"task");
		sql.append(" group by  task.TTKV,task.TTKT) taskOs " + "on taskOs.ttkt = pt.TTKT " + "where 1=1 ");
	}
	
	public void tdHshcHtctCHT(StringBuilder sql, ProgressTaskOsDTO obj) {
		sql.append(" ,tdHshcHtct as ( " + "select  " + "pt.TTKV ttkv, " + "pt.TTKT ttkt, "
				+ "nvl(MAX(taskOs.valueHshc),0) tdHshcHtctKhThang, " 
				+ "nvl((select sum(pt1.HSHC_VALUE) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDHS_TCT_NOT_APPROVAL is not null  "
				+ "      and pt1.TTKT = pt.TTKT " + "      and pt1.SOURCE_TASK in (4,5) ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcHtctDuKienHtValue " + "from PROGRESS_TASK_OS pt "
				+ "left join (select sum(task.HSHC_VALUE) valueHshc, task.TTKT ttkt  "
				+ "from PROGRESS_TASK_OS task where task.status!=0 and task.SOURCE_TASK in (4,5) ");
		ifElseToanQuoc(sql,obj,"task");
		sql.append(" group by  task.TTKV,task.TTKT) taskOs " + "on taskOs.ttkt = pt.TTKT where 1=1 ");
	}
	
	public void tdThDtCHT(StringBuilder sql, ProgressTaskOsDTO obj) {
		sql.append(" ,tdThDt as (select  " + "pt.TTKV ttkv, " + "pt.TTKT ttkt, "
				+ "nvl(MAX(taskOs.valueBill),0) tdThdtKhThang, " 
				+ "nvl((select sum(pt1.BILL_VALUE) "
				+ "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDTT_COLLECT_MONEY_DATE is not null  "
				+ "      and pt1.TTKT = pt.TTKT ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdThdtDuKienHoanThanhValue, "
				+ "null tdThdtNguyenNhanKoHt " + "from PROGRESS_TASK_OS pt "
				+ "left join (select sum(task.bill_Value) valueBill, task.TTKT ttkt  "
				+ "from PROGRESS_TASK_OS task where task.STATUS!=0 ");
		ifElseToanQuoc(sql,obj,"task");
		sql.append(" group by  task.TTKV,task.TTKT) taskOs "
				+ "on taskOs.ttkt = pt.TTKT " + "where 1=1 ");
	}
	
	public void tdHshcXdddCHT(StringBuilder sql, ProgressTaskOsDTO obj) {
		sql.append(" ,tdHshcXddd as (select pt.TTKV ttkv, " + "pt.TTKT ttkt, "
				+ "nvl(MAX(taskOs.valueHshc),0) tdHshcXdddKhThang, " 
				+ "nvl((select sum(pt1.HSHC_VALUE) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDHS_TCT_NOT_APPROVAL is not null  "
				+ "      and pt1.TTKT = pt.TTKT " + "      and pt1.SOURCE_TASK=6 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcXdddDuKienHoanThanhValue " + "from PROGRESS_TASK_OS pt "
				+ "left join (select " + "task.TTKT ttkt, " + "sum(task.HSHC_VALUE) valueHshc "
				+ "from PROGRESS_TASK_OS task " + "where task.status!=0 and task.SOURCE_TASK = 6 " );
		ifElseToanQuoc(sql,obj,"task");
		sql.append(" group by "
				+ "task.TTKT) taskOs " + "on pt.ttkt = taskOs.ttkt where 1=1 ");
	}
	
	public void tdSlHtctCHT(StringBuilder sql, ProgressTaskOsDTO obj) {
		sql.append(" ,tdSlHtct as (select pt.TTKV ttkv, " + "pt.TTKT ttkt, "
				+ "nvl(MAX(taskOs.valueQuantity),0) tdSlHtctKhThang, " 
				+ "nvl((select sum(pt1.QUANTITY_VALUE) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDSL_ACCOMPLISHED_DATE is not null  "
				+ "      and pt1.TTKT = pt.TTKT " + "      and pt1.SOURCE_TASK in (4,5) ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdSlHtctDuKienHtValue " + "from PROGRESS_TASK_OS pt "
				+ "left join (select " + "task.TTKT ttkt, " + "sum(task.QUANTITY_VALUE) valueQuantity "
				+ "from PROGRESS_TASK_OS task " + "where task.status!=0 and task.SOURCE_TASK in (4,5) ");
		ifElseToanQuoc(sql,obj,"task");
		sql.append(" group by "
				+ "task.TTKT) taskOs " + "on pt.ttkt = taskOs.ttkt where 1=1 ");
	}
	
	// tatph-start-7/1/2019
	//TH chốt hoàn thành
	@SuppressWarnings("unchecked")
	public List<ProgressTaskOsDTO> doSearchCHT(ProgressTaskOsDTO obj) {
		StringBuilder sql = new StringBuilder(" with ");

		// Tiến độ Quỹ lương
		tdQLCHT(sql, obj);
		ifElseToanQuoc(sql,obj,"pt");
		sql.append("group by pt.TTKV, " + "pt.TTKT) ");

		// Tiến độ HSHC Chi phí
		tdHshcCpCHT(sql, obj);
		ifElseToanQuoc(sql,obj,"pt");
		sql.append("group by pt.TTKV, " + "pt.TTKT) ");

		// Tiến độ HSHC Xây lắp
		tdHshcXlCHT(sql, obj);
		ifElseToanQuoc(sql,obj,"pt");
		sql.append("group by pt.TTKV, " + "pt.TTKT) ");

		// Tiến độ HSHC Ngoài tập đoàn
		tdHshcNtdCHT(sql, obj);
		ifElseToanQuoc(sql,obj,"pt");
		sql.append("group by pt.TTKV, " + "pt.TTKT) ");

		// Tiến độ Sản lượng Chi phí
		tdSlCpCHT(sql, obj); 
		ifElseToanQuoc(sql,obj,"pt");
		sql.append("group by pt.TTKV, " + "pt.TTKT) ");

		// Tiến độ Sản lượng Xây lắp
		tdSlXlCHT(sql, obj);
		ifElseToanQuoc(sql,obj,"pt");
		sql.append("group by pt.TTKV, " + "pt.TTKT) ");

		// Tiến độ Sản lượng Ngoài tập đoàn
		tdSlNtdCHT(sql, obj);
		ifElseToanQuoc(sql,obj,"pt");
		sql.append("group by pt.TTKV, " + "pt.TTKT) ");

		// Tiến độ Sản lượng XDDD
		tdSlXdddCHT(sql, obj);
		ifElseToanQuoc(sql,obj,"pt");
		sql.append("group by pt.TTKV, " + "pt.TTKT) ");

		// Tiến độ Thu hồi dòng tiền
		tdThDtCHT(sql, obj);
		ifElseToanQuoc(sql,obj,"pt");
		sql.append("group by pt.TTKV, " + "pt.TTKT) ");

		// Tiến độ HSHC HTCT
		tdHshcHtctCHT(sql, obj);
		ifElseToanQuoc(sql,obj,"pt");
		sql.append("group by  pt.TTKV, " + " pt.TTKT) ");

		// Tiến độ tổng HSHC XDDD
		tdHshcXdddCHT(sql, obj);
		ifElseToanQuoc(sql,obj,"pt");
		sql.append("group by  pt.TTKV, " + " pt.TTKT) ");

		// Tiến độ Sản lượng HTCT
		tdSlHtctCHT(sql, obj);
		ifElseToanQuoc(sql,obj,"pt");
		sql.append("group by  pt.TTKV, " + " pt.TTKT) ");

		sql.append(" select tdql.ttkv,tdql.ttkt,nvl(tdql.tdQlKhThang,0) tdQlKhThang, "
				+ "nvl(tdql.tdQlDuKienHoanThanhValue,0) tdQlDuKienHoanThanhValue,"
				+ "(case when tdql.tdQlKhThang>0 then tdql.tdQlDuKienHoanThanhValue/tdql.tdQlKhThang else 0 end) tdQlDuKienHoanThanhTyLe,"
				+ "tdql.tdQlNguyenNhanKoHt,"

				+ "nvl(tdHshcCp.tdHshcCpKhThang,0) tdHshcCpKhThang,"
				+ "nvl(tdHshcCp.tdHshcCpDuKienHoanThanhValue,0) tdHshcCpDuKienHoanThanhValue,"
				+ "(case when tdHshcCp.tdHshcCpKhThang>0 then tdHshcCp.tdHshcCpDuKienHoanThanhValue/tdHshcCp.tdHshcCpKhThang else 0 end) tdHshcCpDuKienHoanThanhTyLe,"
				+ "tdHshcCp.tdHshcCpNguyenNhanKoHt, "

				+ "nvl(tdHshcXl.tdHshcXlKhThang,0) tdHshcXlKhThang, "
				+ "nvl(tdHshcXl.tdHshcXlDuKienHoanThanhValue,0) tdHshcXlDuKienHoanThanhValue, "
				+ "(case when tdHshcXl.tdHshcXlKhThang>0 then tdHshcXl.tdHshcXlDuKienHoanThanhValue/tdHshcXl.tdHshcXlKhThang else 0 end) tdHshcXlDuKienHoanThanhTyLe, "
				+ "tdHshcXl.tdHshcXlNguyenNhanKoHt,"

				+ "nvl(tdHshcNtd.tdHshcNtdKhThang,0) tdHshcNtdKhThang, "
				+ "nvl(tdHshcNtd.tdHshcNtdDuKienHoanThanhValue,0) tdHshcNtdDuKienHoanThanhValue,"
				+ "(case when tdHshcNtd.tdHshcNtdKhThang>0 then tdHshcNtd.tdHshcNtdDuKienHoanThanhValue/tdHshcNtd.tdHshcNtdKhThang else 0 end) tdHshcNtdDuKienHoanThanhTyLe,"
				+ "tdHshcNtd.tdHshcNtdNguyenNhanKoHt,"

				+ "nvl(tdSlCp.tdSlCpKhThang,0) tdSlCpKhThang,"
				+ "nvl(tdSlCp.tdSlCpDuKienHoanThanhValue,0) tdSlCpDuKienHoanThanhValue,"
				+ "(case when tdSlCp.tdSlCpKhThang>0 then tdSlCp.tdSlCpDuKienHoanThanhValue/tdSlCp.tdSlCpKhThang else 0 end) tdSlCpDuKienHoanThanhTyLe,"
				+ "tdSlCp.tdSlCpNguyenNhanKoHt,"

				+ "nvl(tdSlXl.tdSlXlKhThang,0) tdSlXlKhThang,"
				+ "nvl(tdSlXl.tdSlXlDuKienHoanThanhValue,0) tdSlXlDuKienHoanThanhValue,"
				+ "(case when tdSlXl.tdSlXlKhThang>0 then tdSlXl.tdSlXlDuKienHoanThanhValue/tdSlXl.tdSlXlKhThang else 0 end) tdSlXlDuKienHoanThanhTyLe,"
				+ "tdSlXl.tdSlXlNguyenNhanKoHt,"

				+ "nvl(tdSlNtd.tdSlNtdKhThang,0) tdSlNtdKhThang,"
				+ "nvl(tdSlNtd.tdSlNtdDuKienHoanThanhValue,0) tdSlNtdDuKienHoanThanhValue,"
				+ "(case when tdSlNtd.tdSlNtdKhThang>0 then tdSlNtd.tdSlNtdDuKienHoanThanhValue/tdSlNtd.tdSlNtdKhThang else 0 end) tdSlNtdDuKienHoanThanhTyLe,"
				+ "tdSlNtd.tdSlNtdNguyenNhanKoHt,"

				+ "tdSlXddd.tdSlXdddKhThang tdSlXdddKhThang,"
				+ "tdSlXddd.tdSlXdddDuKienHoanThanhValue tdSlXdddDuKienHoanThanhValue, "
				+ "(case when tdSlXddd.tdSlXdddKhThang>0 then (tdSlXddd.tdSlXdddDuKienHoanThanhValue / tdSlXddd.tdSlXdddKhThang) else 0 end) tdSlXdddDuKienHoanThanhTyLe, "
				+ "null tdSlXdddNguyenNhanKoHt, "

				+ "nvl(tdThdt.tdThdtKhThang,0) tdThdtKhThang,"
				+ "nvl(tdThdt.tdThdtDuKienHoanThanhValue,0) tdThdtDuKienHoanThanhValue,"
				+ "(case when tdThdt.tdThdtKhThang>0 then tdThdt.tdThdtDuKienHoanThanhValue/tdThdt.tdThdtKhThang else 0 end) tdThdtDuKienHoanThanhTyLe,"
				+ "tdThdt.tdThdtNguyenNhanKoHt, "

				+ "tdHshcHtct.tdHshcHtctKhThang tdHshcHtctKhThang,"
				+ " tdHshcHtct.tdHshcHtctDuKienHtValue tdHshcHtctDuKienHtValue,"
				+ "(case when tdHshcHtct.tdHshcHtctKhThang > 0 then (tdHshcHtct.tdHshcHtctDuKienHtValue / tdHshcHtct.tdHshcHtctKhThang) else 0 end) tdHshcHtctDuKienHtTyLe, null tdHshcHtctNguyenNhanKoHt, "

				+ "tdHshcXddd.tdHshcXdddKhThang tdHshcXdddKhThang,"
				+ " tdHshcXddd.tdHshcXdddDuKienHoanThanhValue tdHshcXdddDuKienHoanThanhValue,"
				+ " (case when tdHshcXddd.tdHshcXdddKhThang > 0 then (tdHshcXddd.tdHshcXdddDuKienHoanThanhValue / tdHshcXddd.tdHshcXdddKhThang) else 0 end) tdHshcXdddDuKienHoanThanhTyLe,"
				+ " null tdHshcXdddNguyenNhanKoHt "

				+ ", tdSlHtct.tdSlHtctKhThang tdSlHtctKhThang,"
				+ " tdSlHtct.tdSlHtctDuKienHtValue tdSlHtctDuKienHtValue,"
				+ " (case when tdSlHtct.tdSlHtctKhThang > 0 then (tdSlHtct.tdSlHtctDuKienHtValue / tdSlHtct.tdSlHtctKhThang) else 0 end) tdSlHtctDuKienHtTyLe,"
				+ " null tdSlHtctNguyenNhanKoHt "

				+ " from tdql " + " full join tdHshcCp on tdql.ttkt = tdHshcCp.ttkt "
				+ " full join tdHshcXl on tdql.ttkt = tdHshcXl.ttkt "
				+ " full join tdHshcNtd on tdql.ttkt = tdHshcNtd.ttkt "
				+ " full join tdSlCp on tdql.ttkt = tdSlCp.ttkt " + " full join tdSlXl on tdql.ttkt = tdSlXl.ttkt "
				+ " full join tdSlNtd on tdql.ttkt = tdSlNtd.ttkt "
				+ " full join tdSlXddd on tdql.ttkt = tdSlXddd.ttkt " + " full join tdThDt on tdql.ttkt = tdThDt.ttkt "
				+ " full join tdHshcHtct on tdql.ttkt =  tdHshcHtct.ttkt "
				+ " full join tdHshcXddd on tdql.ttkt =  tdHshcXddd.ttkt "
				+ " full join tdSlHtct on tdql.ttkt =  tdSlHtct.ttkt ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("ttkv", new StringType());
		query.addScalar("ttkt", new StringType());

		query.addScalar("tdQlKhThang", new LongType());
		query.addScalar("tdQlDuKienHoanThanhValue", new LongType());
		query.addScalar("tdQlDuKienHoanThanhTyLe", new DoubleType());
		query.addScalar("tdQlNguyenNhanKoHt", new StringType());

		query.addScalar("tdHshcCpKhThang", new LongType());
		query.addScalar("tdHshcCpDuKienHoanThanhValue", new LongType());
		query.addScalar("tdHshcCpDuKienHoanThanhTyLe", new DoubleType());
		query.addScalar("tdHshcCpNguyenNhanKoHt", new StringType());

		query.addScalar("tdHshcXlKhThang", new LongType());
		query.addScalar("tdHshcXlDuKienHoanThanhValue", new LongType());
		query.addScalar("tdHshcXlDuKienHoanThanhTyLe", new DoubleType());
		query.addScalar("tdHshcXlNguyenNhanKoHt", new StringType());

		query.addScalar("tdHshcNtdKhThang", new LongType());
		query.addScalar("tdHshcNtdDuKienHoanThanhValue", new LongType());
		query.addScalar("tdHshcNtdDuKienHoanThanhTyLe", new DoubleType());
		query.addScalar("tdHshcNtdNguyenNhanKoHt", new StringType());

		query.addScalar("tdSlCpKhThang", new LongType());
		query.addScalar("tdSlCpDuKienHoanThanhValue", new LongType());
		query.addScalar("tdSlCpDuKienHoanThanhTyLe", new DoubleType());
		query.addScalar("tdSlCpNguyenNhanKoHt", new StringType());

		query.addScalar("tdSlXlKhThang", new LongType());
		query.addScalar("tdSlXlDuKienHoanThanhValue", new LongType());
		query.addScalar("tdSlXlDuKienHoanThanhTyLe", new DoubleType());
		query.addScalar("tdSlXlNguyenNhanKoHt", new StringType());

		query.addScalar("tdSlNtdKhThang", new LongType());
		query.addScalar("tdSlNtdDuKienHoanThanhValue", new LongType());
		query.addScalar("tdSlNtdDuKienHoanThanhTyLe", new DoubleType());
		query.addScalar("tdSlNtdNguyenNhanKoHt", new StringType());

		query.addScalar("tdSlXdddKhThang", new LongType());
		query.addScalar("tdSlXdddDuKienHoanThanhValue", new LongType());
		query.addScalar("tdSlXdddDuKienHoanThanhTyLe", new DoubleType());
		query.addScalar("tdSlXdddNguyenNhanKoHt", new StringType());

		query.addScalar("tdThdtKhThang", new LongType());
		query.addScalar("tdThdtDuKienHoanThanhValue", new LongType());
		query.addScalar("tdThdtDuKienHoanThanhTyLe", new DoubleType());
		query.addScalar("tdThdtNguyenNhanKoHt", new StringType());

		query.addScalar("tdHshcHtctKhThang", new LongType());
		query.addScalar("tdHshcHtctDuKienHtValue", new LongType());
		query.addScalar("tdHshcHtctDuKienHtTyLe", new DoubleType());
		query.addScalar("tdHshcHtctNguyenNhanKoHt", new StringType());

		query.addScalar("tdHshcXdddKhThang", new LongType());
		query.addScalar("tdHshcXdddDuKienHoanThanhValue", new LongType());
		query.addScalar("tdHshcXdddDuKienHoanThanhTyLe", new DoubleType());
		query.addScalar("tdHshcXdddNguyenNhanKoHt", new StringType());

		query.addScalar("tdSlHtctKhThang", new LongType());
		query.addScalar("tdSlHtctDuKienHtValue", new LongType());
		query.addScalar("tdSlHtctDuKienHtTyLe", new DoubleType());
		query.addScalar("tdSlHtctNguyenNhanKoHt", new LongType());

		if (StringUtils.isNoneBlank(obj.getTtkt())) {
			query.setParameter("ttkt", obj.getTtkt());
		}

		if (StringUtils.isNotBlank(obj.getMonthYear())) {
			query.setParameter("monthYear", obj.getMonthYear());
		}

		if (obj.getGroupIdList()!=null && !obj.getGroupIdList().isEmpty()) {
			query.setParameterList("groupIdList", obj.getGroupIdList());
		}
		
		query.setResultTransformer(Transformers.aliasToBean(ProgressTaskOsDTO.class));

		return query.list();
	}
	// tatph-end-7/1/2019

	// Huypq-20200108-start
	public void ifElseToanQuoc(StringBuilder sql, ProgressTaskOsDTO obj, String pt) {
		if(StringUtils.isNotBlank(obj.getMonthYear())) {
			sql.append(" and "+ pt + ".MONTH_YEAR=:monthYear ");
		}
		if(StringUtils.isNotBlank(obj.getTtkt())) {
			sql.append(" and "+ pt + ".TTKT=:ttkt ");
		}
		if(obj.getGroupIdList()!=null && !obj.getGroupIdList().isEmpty()) {
			sql.append(" and "+ pt + ".TTKT_ID in (:groupIdList) ");
		}
	}
	
	public void ifElseBaoCao(StringBuilder sql, ProgressTaskOsDTO obj, String pt) {
		if(StringUtils.isNotBlank(obj.getMonthYear())) {
			sql.append(" and "+ pt + ".MONTH_YEAR=:monthYear ");
		}
//		if(StringUtils.isNotBlank(obj.getTtkt())) {
//			sql.append(" and "+ pt + ".TTKT=:ttkt ");
//		}
	}
	
	public void tdqlToanQuoc(StringBuilder sql, ProgressTaskOsDTO obj) {
		sql.append(" tdql as (select  " + "pt.TTKV ttkv, " + "pt.TTKT ttkt, "
				+ "nvl(MAX(taskOs.valueSalary),0) tdQlKhThang, " + "null tdQlTong, " + "null tdQlTthtDaDuyet, "
				+ "nvl((select sum(nvl(pt1.SALARY_VALUE,0)) "
				+ "         from PROGRESS_TASK_OS pt1 where pt1.status!=0 and pt1.TDHS_TCT_NOT_APPROVAL is not null  and pt1.APPROVE_COMPLETE_DATE is not null "
				+ "	and pt1.TTKT = pt.TTKT ");
		ifElseToanQuoc(sql,obj,"pt1");
				sql.append("         group by pt1.TTKT),0) tdQlTrenDuongValue, " 
				+ "nvl((select sum(nvl(pt1.SALARY_VALUE,0)) "
				+ "         from PROGRESS_TASK_OS pt1 where pt1.status!=0 and pt1.TDHS_SIGNING_GDCN is not null  and pt1.APPROVE_COMPLETE_DATE is null  and pt1.TTKT = pt.TTKT ");
		ifElseToanQuoc(sql,obj,"pt1");
				sql.append("         group by pt1.TTKT),0) tdQlGdCnKyValue, " 
				+ "nvl((select sum(nvl(pt1.SALARY_VALUE,0)) "
				+ "         from PROGRESS_TASK_OS pt1 where pt1.status!=0 and pt1.TDHS_CONTROL_4A is not null  and pt1.APPROVE_COMPLETE_DATE is null  and pt1.TTKT = pt.TTKT ");
		ifElseToanQuoc(sql,obj,"pt1");
				sql.append("         group by pt1.TTKT),0) tdQlDoiSoat4aValue, " 
				+ "nvl((select sum(nvl(pt1.SALARY_VALUE,0))  "
				+ "         from PROGRESS_TASK_OS pt1 where pt1.status!=0 and pt1.TDHS_PHT_APPROVALING is not null  and pt1.APPROVE_COMPLETE_DATE is null  and pt1.TTKT = pt.TTKT ");
		ifElseToanQuoc(sql,obj,"pt1");
				sql.append("         group by pt1.TTKT),0) tdQlPhtThamDuyetValue, " 
				+ "nvl((select sum(nvl(pt1.SALARY_VALUE,0))  "
				+ "         from PROGRESS_TASK_OS pt1 where pt1.status!=0 and pt1.TDHS_PHT_ACCEPTANCING is not null  and pt1.APPROVE_COMPLETE_DATE is null  and pt1.TTKT = pt.TTKT ");
		ifElseToanQuoc(sql,obj,"pt1");
				sql.append("         group by pt1.TTKT),0) tdQlPhtNghiemThuValue, " 
				+ "nvl((select sum(nvl(pt1.SALARY_VALUE,0))  "
				+ "         from PROGRESS_TASK_OS pt1 where pt1.status!=0 and pt1.TDHS_TTKT_PROFILE is not null  and pt1.APPROVE_COMPLETE_DATE is null  and pt1.TTKT = pt.TTKT ");
		ifElseToanQuoc(sql,obj,"pt1");
				sql.append("         group by pt1.TTKT),0) tdQlDangLamHoSoValue, " 
				+ "nvl((select sum(nvl(pt1.SALARY_VALUE,0))  "
				+ "         from PROGRESS_TASK_OS pt1 where pt1.status!=0 and pt1.TDHS_EXPECTED_COMPLETE_DATE is not null and pt1.TTKT = pt.TTKT ");
		ifElseToanQuoc(sql,obj,"pt1");
				sql.append("         group by pt1.TTKT),0) tdQlDuKienHoanThanhValue, " 
				+ "null tdQlNguyenNhanKoHt "
				+ "from PROGRESS_TASK_OS pt " + "left join (select sum(nvl(task.SALARY_VALUE,0)) valueSalary, task.TTKT ttkt  "
				+ "from PROGRESS_TASK_OS task where task.STATUS!=0 ");
		ifElseToanQuoc(sql,obj,"task");
				sql.append("group by  task.TTKV,task.TTKT) taskOs "
				+ "on taskOs.ttkt = pt.TTKT " + "where 1=1 ");
	}
	
	public void tdHshcCpToanQuoc(StringBuilder sql, ProgressTaskOsDTO obj) {
		sql.append(" ,tdHshcCp as (select  " + "pt.TTKV ttkv, " + "pt.TTKT ttkt, "
				+ "nvl(MAX(taskOs.valueHshc),0) tdHshcCpKhThang, " + "null tdHshcCpTong, " + "null tdHshcCpTthtDaDuyet, "
				+ "nvl((select sum(nvl(pt1.HSHC_VALUE,0)) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDHS_TCT_NOT_APPROVAL is not null  " + "      and pt1.TTKT = pt.TTKT "
				+ "      and pt1.SOURCE_TASK=2 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcCpTrenDuongValue, "
				+ "nvl((select sum(nvl(pt1.HSHC_VALUE,0)) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDHS_SIGNING_GDCN is not null and pt1.TDHS_TCT_NOT_APPROVAL is null   " + "      and pt1.TTKT = pt.TTKT "
				+ "      and pt1.SOURCE_TASK=2 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcCpGdCnKyValue, "
				+ "nvl((select sum(nvl(pt1.HSHC_VALUE,0)) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDHS_CONTROL_4A is not null and pt1.TDHS_TCT_NOT_APPROVAL is null   " + "      and pt1.TTKT = pt.TTKT "
				+ "      and pt1.SOURCE_TASK=2 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcCpDoiSoat4aValue, "
				+ "nvl((select sum(nvl(pt1.HSHC_VALUE,0)) " 
				+ "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDHS_PHT_APPROVALING is not null and pt1.TDHS_TCT_NOT_APPROVAL is null   " + "      and pt1.TTKT = pt.TTKT "
				+ "      and pt1.SOURCE_TASK=2 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcCpPhtThamDuyetValue, "
				+ "nvl((select sum(nvl(pt1.HSHC_VALUE,0)) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDHS_PHT_ACCEPTANCING is not null and pt1.TDHS_TCT_NOT_APPROVAL is null   " + "      and pt1.TTKT = pt.TTKT "
				+ "      and pt1.SOURCE_TASK=2 " );
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcCpPhtNghiemThuValue, "
				+ "nvl((select sum(nvl(pt1.HSHC_VALUE,0)) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDHS_TTKT_PROFILE is not null and pt1.TDHS_TCT_NOT_APPROVAL is null   " + "      and pt1.TTKT = pt.TTKT "
				+ "      and pt1.SOURCE_TASK=2 " );
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcCpDangLamHoSoValue, "
				+ "nvl((select sum(nvl(pt1.HSHC_VALUE,0)) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDHS_EXPECTED_COMPLETE_DATE is not null  "
				+ "      and pt1.TTKT = pt.TTKT " + "      and pt1.SOURCE_TASK=2 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcCpDuKienHoanThanhValue, " 
				+ "null tdHshcCpNguyenNhanKoHt "
				+ "from PROGRESS_TASK_OS pt " + "left join (select sum(nvl(task.HSHC_VALUE,0)) valueHshc, task.TTKT ttkt  "
				+ "from PROGRESS_TASK_OS task where task.STATUS!=0 and task.SOURCE_TASK=2 ");
		ifElseToanQuoc(sql,obj,"task");
		sql.append("group by  task.TTKV,task.TTKT) taskOs " + "on taskOs.ttkt = pt.TTKT " + "where 1=1 ");
	}
	public void tdHshcXlToanQuoc(StringBuilder sql, ProgressTaskOsDTO obj) {
		sql.append(" ,tdHshcXl as (select  " + "pt.TTKV ttkv, " + "pt.TTKT ttkt, "
				+ "nvl(MAX(taskOs.valueHshc),0) tdHshcXlKhThang, " + "null tdHshcXlTong, " + "null tdHshcXlTthtDaDuyet, "
				+ "nvl((select sum(nvl(pt1.HSHC_VALUE,0)) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDHS_TCT_NOT_APPROVAL is not null " + "      and pt1.TTKT = pt.TTKT "
				+ "      and pt1.SOURCE_TASK=1 " );
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcXlTrenDuongValue, "
				+ "nvl((select sum(nvl(pt1.HSHC_VALUE,0)) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDHS_SIGNING_GDCN is not null and pt1.TDHS_TCT_NOT_APPROVAL is null " + "      and pt1.TTKT = pt.TTKT "
				+ "      and pt1.SOURCE_TASK=1 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcXlGdCnKyValue, "
				+ "nvl((select sum(nvl(pt1.HSHC_VALUE,0)) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDHS_CONTROL_4A is not null and pt1.TDHS_TCT_NOT_APPROVAL is null " + "      and pt1.TTKT = pt.TTKT "
				+ "      and pt1.SOURCE_TASK=1 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcXlDoiSoat4aValue, "
				+ "nvl((select sum(nvl(pt1.HSHC_VALUE,0)) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDHS_PHT_APPROVALING is not null and pt1.TDHS_TCT_NOT_APPROVAL is null " + "      and pt1.TTKT = pt.TTKT "
				+ "      and pt1.SOURCE_TASK=1 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcXlPhtThamDuyetValue, "
				+ "nvl((select sum(nvl(pt1.HSHC_VALUE,0)) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDHS_PHT_ACCEPTANCING is not null and pt1.TDHS_TCT_NOT_APPROVAL is null " + "      and pt1.TTKT = pt.TTKT "
				+ "      and pt1.SOURCE_TASK=1 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcXlPhtNghiemThuValue, "
				+ "nvl((select sum(nvl(pt1.HSHC_VALUE,0)) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDHS_TTKT_PROFILE is not null and pt1.TDHS_TCT_NOT_APPROVAL is null " + "      and pt1.TTKT = pt.TTKT "
				+ "      and pt1.SOURCE_TASK=1 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcXlDangLamHoSoValue, "
				+ "nvl((select sum(nvl(pt1.HSHC_VALUE,0)) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDHS_EXPECTED_COMPLETE_DATE is not null  "
				+ "      and pt1.TTKT = pt.TTKT " + "      and pt1.SOURCE_TASK=1 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcXlDuKienHoanThanhValue, " + "null tdHshcXlNguyenNhanKoHt "
				+ "from PROGRESS_TASK_OS pt " + "left join (select sum(nvl(task.HSHC_VALUE,0)) valueHshc, task.TTKT ttkt  "
				+ "from PROGRESS_TASK_OS task where task.STATUS!=0 and task.SOURCE_TASK=1 ");
		ifElseToanQuoc(sql,obj,"task");
		sql.append("group by  task.TTKV,task.TTKT) taskOs " + "on taskOs.ttkt = pt.TTKT " + "where 1=1 ");
	}
	
	public void tdHshcNtdToanQuoc(StringBuilder sql, ProgressTaskOsDTO obj) {
		sql.append(" ,tdHshcNtd as (select  " + "pt.TTKV ttkv, " + "pt.TTKT ttkt, "
				+ "nvl(MAX(taskOs.valueHshc),0) tdHshcNtdKhThang, " + "null tdHshcNtdTong, " + "null tdHshcNtdPtkDaDuyet, "
				+ "nvl((select sum(nvl(pt1.HSHC_VALUE,0)) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDHS_TCT_NOT_APPROVAL is not null  " + "      and pt1.TTKT = pt.TTKT "
				+ "      and pt1.SOURCE_TASK=3 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcNtdTrenDuongValue, "
				+ "nvl((select sum(nvl(pt1.HSHC_VALUE,0)) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDHS_SIGNING_GDCN is not null and pt1.TDHS_TCT_NOT_APPROVAL is null  " + "      and pt1.TTKT = pt.TTKT "
				+ "      and pt1.SOURCE_TASK=3 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcNtdGdCnKyValue, "
				+ "nvl((select sum(nvl(pt1.HSHC_VALUE,0)) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDHS_CONTROL_4A is not null and pt1.TDHS_TCT_NOT_APPROVAL is null  " + "      and pt1.TTKT = pt.TTKT "
				+ "      and pt1.SOURCE_TASK=3 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcNtdDoiSoat4aValue, "
				+ "nvl((select sum(nvl(pt1.HSHC_VALUE,0)) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDHS_PHT_APPROVALING is not null and pt1.TDHS_TCT_NOT_APPROVAL is null  " + "      and pt1.TTKT = pt.TTKT "
				+ "      and pt1.SOURCE_TASK=3 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcNtdPhtThamDuyetValue, "
				+ "nvl((select sum(nvl(pt1.HSHC_VALUE,0)) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDHS_PHT_ACCEPTANCING is not null and pt1.TDHS_TCT_NOT_APPROVAL is null  " + "      and pt1.TTKT = pt.TTKT "
				+ "      and pt1.SOURCE_TASK=3 " );
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcNtdPhtNghiemThuValue, "
				+ "nvl((select sum(nvl(pt1.HSHC_VALUE,0)) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDHS_TTKT_PROFILE is not null and pt1.TDHS_TCT_NOT_APPROVAL is null  " + "      and pt1.TTKT = pt.TTKT "
				+ "      and pt1.SOURCE_TASK=3 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcNtdDangLamHoSoValue, "
				+ "nvl((select sum(nvl(pt1.HSHC_VALUE,0)) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDHS_EXPECTED_COMPLETE_DATE is not null  "
				+ "      and pt1.TTKT = pt.TTKT " + "      and pt1.SOURCE_TASK=3 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcNtdDuKienHoanThanhValue, " + "null tdHshcNtdNguyenNhanKoHt "
				+ "from PROGRESS_TASK_OS pt " + "left join (select sum(nvl(task.HSHC_VALUE,0)) valueHshc, task.TTKT ttkt  "
				+ "from PROGRESS_TASK_OS task where task.STATUS!=0 and task.SOURCE_TASK=3 ");
		ifElseToanQuoc(sql,obj,"task");
		sql.append("group by  task.TTKV,task.TTKT) taskOs " + "on taskOs.ttkt = pt.TTKT " + "where 1=1 ");
	}
	
	public void tdSlCpToanQuoc(StringBuilder sql, ProgressTaskOsDTO obj) {
		sql.append(" ,tdSlCp as (select  " + "pt.TTKV ttkv, " + "pt.TTKT ttkt, "
				+ "nvl(MAX(taskOs.valueQuantity),0) tdSlCpKhThang, " 
				+ "nvl((select sum(nvl(pt1.QUANTITY_VALUE,0)) "
				+ "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0  "
				+ "      and pt1.TTKT = pt.TTKT " + "      and pt1.SOURCE_TASK=2 and pt1.TDSL_ACCOMPLISHED_DATE is not null ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdSlCpDaHoanThanh, " 
				+ "nvl((select sum(nvl(pt1.QUANTITY_VALUE,0)) "
				+ "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDSL_CONSTRUCTING is not null and pt1.TDSL_ACCOMPLISHED_DATE is null " + "      and pt1.TTKT = pt.TTKT "
				+ "      and pt1.SOURCE_TASK=2 " );
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdSlCpDangThiCongValue, "
				+ "nvl((select sum(nvl(pt1.QUANTITY_VALUE,0)) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDSL_EXPECTED_COMPLETE_DATE is not null  " + "      and pt1.TTKT = pt.TTKT "
				+ "      and pt1.SOURCE_TASK=2 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdSlCpDuKienHoanThanhValue, "
				+ "null tdSlCpNguyenNhanKoHt " + "from PROGRESS_TASK_OS pt "
				+ "left join (select sum(nvl(task.QUANTITY_VALUE,0)) valueQuantity, task.TTKT ttkt  "
				+ "from PROGRESS_TASK_OS task where task.STATUS!=0 and task.SOURCE_TASK=2 ");
		ifElseToanQuoc(sql,obj,"task");
		sql.append("group by  task.TTKV,task.TTKT) taskOs " + "on taskOs.ttkt = pt.TTKT " + "where 1=1 ");
	}
	
	public void tdSlXlToanQuoc(StringBuilder sql, ProgressTaskOsDTO obj) {
		sql.append(" ,tdSlXl as (select  " + "pt.TTKV ttkv, " + "pt.TTKT ttkt, "
				+ "nvl(MAX(taskOs.valueQuantity),0) tdSlXlKhThang, " 
				+ "nvl((select sum(nvl(pt1.QUANTITY_VALUE,0)) "
				+ "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 "
				+ "      and pt1.TTKT = pt.TTKT " + "      and pt1.SOURCE_TASK=1  and pt1.TDSL_ACCOMPLISHED_DATE is not null ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdSlXlDaHoanThanh, " 
				+ "nvl((select sum(nvl(pt1.QUANTITY_VALUE,0)) "
				+ "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDSL_CONSTRUCTING is not null and pt1.TDSL_ACCOMPLISHED_DATE is null " + "      and pt1.TTKT = pt.TTKT "
				+ "      and pt1.SOURCE_TASK=1 " );
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdSlXlDangThiCongValue, "
				+ "nvl((select sum(nvl(pt1.QUANTITY_VALUE,0)) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDSL_EXPECTED_COMPLETE_DATE is not null  " + "      and pt1.TTKT = pt.TTKT "
				+ "      and pt1.SOURCE_TASK=1 " );
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdSlXlDuKienHoanThanhValue, "
				+ "null tdSlXlNguyenNhanKoHt " + "from PROGRESS_TASK_OS pt "
				+ "left join (select sum(nvl(task.QUANTITY_VALUE,0)) valueQuantity, task.TTKT ttkt  "
				+ "from PROGRESS_TASK_OS task where task.STATUS!=0 and task.SOURCE_TASK=1 ");
		ifElseToanQuoc(sql,obj,"task");
		sql.append("group by  task.TTKV,task.TTKT) taskOs " + "on taskOs.ttkt = pt.TTKT " + "where 1=1 ");
	}
	
	public void tdSlNtdToanQuoc(StringBuilder sql, ProgressTaskOsDTO obj) {
		sql.append(" ,tdSlNtd as (select  " + "pt.TTKV ttkv, " + "pt.TTKT ttkt, "
				+ "nvl(MAX(taskOs.valueQuantity),0) tdSlNtdKhThang, " 
				+ "nvl((select sum(nvl(pt1.QUANTITY_VALUE,0)) "
				+ "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0   "
				+ "      and pt1.TTKT = pt.TTKT " + "      and pt1.SOURCE_TASK=3   and pt1.TDSL_ACCOMPLISHED_DATE is not null  ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdSlNtdDaHoanThanh, " 
				+ "nvl((select sum(nvl(pt1.QUANTITY_VALUE,0)) "
				+ "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDSL_CONSTRUCTING is not null and pt1.TDSL_ACCOMPLISHED_DATE is null   " + "      and pt1.TTKT = pt.TTKT "
				+ "      and pt1.SOURCE_TASK=3 " );
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdSlNtdDangThiCongValue, "
				+ "nvl((select sum(nvl(pt1.QUANTITY_VALUE,0)) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDSL_EXPECTED_COMPLETE_DATE is not null  " + "      and pt1.TTKT = pt.TTKT "
				+ "      and pt1.SOURCE_TASK=3 " );
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdSlNtdDuKienHoanThanhValue, "
				+ "null tdSlNtdNguyenNhanKoHt " + "from PROGRESS_TASK_OS pt "
				+ "left join (select sum(nvl(task.QUANTITY_VALUE,0)) valueQuantity, task.TTKT ttkt  "
				+ "from PROGRESS_TASK_OS task where task.STATUS!=0 and task.SOURCE_TASK=3 ");
		ifElseToanQuoc(sql,obj,"task");
		sql.append("group by  task.TTKV,task.TTKT) taskOs " + "on taskOs.ttkt = pt.TTKT " + "where 1=1 ");
	}
	
	public void tdThDtToanQuoc(StringBuilder sql, ProgressTaskOsDTO obj) {
		sql.append(" ,tdThDt as (select  " + "pt.TTKV ttkv, " + "pt.TTKT ttkt, "
				+ "nvl(MAX(taskOs.valueBill),0) tdThdtKhThang, " 
				+ "nvl((select sum(nvl(pt1.BILL_VALUE,0)) "
				+ "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDTT_COLLECT_MONEY_DATE is not null  "
				+ "      and pt1.TTKT = pt.TTKT ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdThdtDaHoanThanh, "
				+ "nvl((select sum(pt1.BILL_VALUE) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDTT_PROFILE_PHT is not null and pt1.TDTT_COLLECT_MONEY_DATE is null " + "      and pt1.TTKT = pt.TTKT ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdThdtPhtDangKiemTraValue, " 
				+ "nvl((select sum(pt1.BILL_VALUE) "
				+ "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDTT_PROFILE_PTC is not null and pt1.TDTT_COLLECT_MONEY_DATE is null " + "      and pt1.TTKT = pt.TTKT ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdThdtPtcDangKiemTraValue, " 
				+ "nvl((select sum(pt1.BILL_VALUE) "
				+ "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDTT_EXPECTED_COMPLETE_DATE is not null  "
				+ "      and pt1.TTKT = pt.TTKT ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdThdtDuKienHoanThanhValue, "
				+ "null tdThdtNguyenNhanKoHt " + "from PROGRESS_TASK_OS pt "
				+ "left join (select sum(task.bill_Value) valueBill, task.TTKT ttkt  "
				+ "from PROGRESS_TASK_OS task where task.STATUS!=0 ");
		ifElseToanQuoc(sql,obj,"task");
		sql.append("group by  task.TTKV,task.TTKT) taskOs "
				+ "on taskOs.ttkt = pt.TTKT " + "where 1=1 ");
	}
	
	public void tdHshcHtctToanQuoc(StringBuilder sql, ProgressTaskOsDTO obj) {
		sql.append(" ,tdHshcHtct as ( " + "select  " + "pt.TTKV ttkv, " + "pt.TTKT ttkt, "
				+ "nvl(MAX(taskOs.valueHshc),0) tdHshcHtctKhThang, " + "nvl((select sum(pt1.HSHC_VALUE) "
				+ "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDHS_TCT_NOT_APPROVAL is not null  " + "      and pt1.TTKT = pt.TTKT "
				+ "      and pt1.SOURCE_TASK in (4,5) ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcHtctTrenDuongValue, "
				+ "nvl((select sum(pt1.HSHC_VALUE) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDHS_SIGNING_GDCN is not null and pt1.TDHS_TCT_NOT_APPROVAL is null    " + "      and pt1.TTKT = pt.TTKT "
				+ "      and pt1.SOURCE_TASK in (4,5) ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcHtctGdCnKyValue, "
				+ "nvl((select sum(pt1.HSHC_VALUE) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDHS_CONTROL_4A is not null and pt1.TDHS_TCT_NOT_APPROVAL is null   " + "      and pt1.TTKT = pt.TTKT "
				+ "      and pt1.SOURCE_TASK in (4,5) ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcHtctDoiSoat4aValue, "
				+ "nvl((select sum(pt1.HSHC_VALUE) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDHS_PHT_APPROVALING is not null and pt1.TDHS_TCT_NOT_APPROVAL is null   " + "      and pt1.TTKT = pt.TTKT "
				+ "      and pt1.SOURCE_TASK in (4,5) ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcHtctPhtThamDuyetValue, "
				+ "nvl((select sum(pt1.HSHC_VALUE) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDHS_PHT_ACCEPTANCING is not null and pt1.TDHS_TCT_NOT_APPROVAL is null   " + "      and pt1.TTKT = pt.TTKT "
				+ "      and pt1.SOURCE_TASK in (4,5) ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcHtctPhtNghiemThuValue, "
				+ "nvl((select sum(pt1.HSHC_VALUE) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDHS_TTKT_PROFILE is not null and pt1.TDHS_TCT_NOT_APPROVAL is null   " + "      and pt1.TTKT = pt.TTKT "
				+ "      and pt1.SOURCE_TASK in (4,5) ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcHtctTtktHoSoValue, "
				+ "nvl((select sum(pt1.HSHC_VALUE) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDHS_EXPECTED_COMPLETE_DATE is not null  "
				+ "      and pt1.TTKT = pt.TTKT " + "      and pt1.SOURCE_TASK in (4,5) ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcHtctDuKienHtValue " 
				+ " from PROGRESS_TASK_OS pt "
				+ "left join (select sum(task.HSHC_VALUE) valueHshc, task.TTKT ttkt  "
				+ "from PROGRESS_TASK_OS task where task.status!=0 and task.SOURCE_TASK in (4,5) ");
		ifElseToanQuoc(sql,obj,"task");
		sql.append(" group by  task.TTKV,task.TTKT) taskOs " + "on taskOs.ttkt = pt.TTKT where 1=1 ");
	}
	
	public void tdSlXdddToanQuoc(StringBuilder sql, ProgressTaskOsDTO obj) {
		sql.append(" ,tdSlXddd as (select pt.TTKV ttkv, " + "pt.TTKT ttkt, "
				+ "nvl(MAX(taskOs.valueQuantity),0) tdSlXdddKhThang, " 
				+ "nvl((select sum(pt1.QUANTITY_VALUE) "
				+ "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDSL_ACCOMPLISHED_DATE is not null  " + "      and pt1.TTKT = pt.TTKT "
				+ "      and pt1.SOURCE_TASK=6 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdSlXdddDaHoanThanh, "
				+ "nvl((select sum(pt1.QUANTITY_VALUE) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDSL_CONSTRUCTING is not null and pt1.TDSL_ACCOMPLISHED_DATE is null   " + "      and pt1.TTKT = pt.TTKT "
				+ "      and pt1.SOURCE_TASK=6 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdSlXdddDangThiCongValue, "
				+ "nvl((select sum(pt1.QUANTITY_VALUE) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDSL_EXPECTED_COMPLETE_DATE is not null  "
				+ "      and pt1.TTKT = pt.TTKT " + "      and pt1.SOURCE_TASK=6 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdSlXdddDuKienHoanThanhValue " + "from PROGRESS_TASK_OS pt "
				+ "left join (select " + "task.TTKT ttkt, " + "sum(task.QUANTITY_VALUE) valueQuantity "
				+ "from PROGRESS_TASK_OS task " + "where task.status!=0 and task.SOURCE_TASK = 6 ");
		ifElseToanQuoc(sql,obj,"task");
		sql.append(" group by "
				+ "task.TTKT) taskOs " + "on pt.ttkt = taskOs.ttkt where 1=1 ");
	}
	
	public void tdHshcXdddToanQuoc(StringBuilder sql, ProgressTaskOsDTO obj) {
		sql.append(" ,tdHshcXddd as (select pt.TTKV ttkv, " + "pt.TTKT ttkt, "
			+ "nvl(MAX(taskOs.valueHshc),0) tdHshcXdddKhThang, " + "nvl((select sum(pt1.HSHC_VALUE) "
			+ "			from PROGRESS_TASK_OS pt1  "
			+ "      where pt1.status!=0 and pt1.TDHS_TCT_NOT_APPROVAL is not null   " + "      and pt1.TTKT = pt.TTKT "
			+ "      and pt1.SOURCE_TASK=6 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcXdddTrenDuongValue, "
			+ "nvl((select sum(pt1.HSHC_VALUE) " + "			from PROGRESS_TASK_OS pt1  "
			+ "      where pt1.status!=0 and pt1.TDHS_SIGNING_GDCN is not null and pt1.TDHS_TCT_NOT_APPROVAL is null   " + "      and pt1.TTKT = pt.TTKT "
			+ "      and pt1.SOURCE_TASK=6 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcXdddGdCnKyValue, "
			+ "nvl((select sum(pt1.HSHC_VALUE) " + "			from PROGRESS_TASK_OS pt1  "
			+ "      where pt1.status!=0 and pt1.TDHS_CONTROL_4A is not null and pt1.TDHS_TCT_NOT_APPROVAL is null   " + "      and pt1.TTKT = pt.TTKT "
			+ "      and pt1.SOURCE_TASK=6 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcXdddDoiSoat4aValue, "
			+ "nvl((select sum(pt1.HSHC_VALUE) " + "			from PROGRESS_TASK_OS pt1  "
			+ "      where pt1.status!=0 and pt1.TDHS_PHT_APPROVALING is not null and pt1.TDHS_TCT_NOT_APPROVAL is null   " + "      and pt1.TTKT = pt.TTKT "
			+ "      and pt1.SOURCE_TASK=6 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcXdddPhtThamDuyetValue, "
			+ "nvl((select sum(pt1.HSHC_VALUE) " + "			from PROGRESS_TASK_OS pt1  "
			+ "      where pt1.status!=0 and pt1.TDHS_PHT_ACCEPTANCING is not null and pt1.TDHS_TCT_NOT_APPROVAL is null   " + "      and pt1.TTKT = pt.TTKT "
			+ "      and pt1.SOURCE_TASK=6 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcXdddPhtNghiemThuValue, "
			+ "nvl((select sum(pt1.HSHC_VALUE) " + "			from PROGRESS_TASK_OS pt1  "
			+ "      where pt1.status!=0 and pt1.TDHS_TTKT_PROFILE is not null and pt1.TDHS_TCT_NOT_APPROVAL is null   " + "      and pt1.TTKT = pt.TTKT "
			+ "      and pt1.SOURCE_TASK=6 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcXdddDangLamHoSoValue, "
			+ "nvl((select sum(pt1.HSHC_VALUE) " + "			from PROGRESS_TASK_OS pt1  "
			+ "      where pt1.status!=0 and pt1.TDHS_EXPECTED_COMPLETE_DATE is not null  "
			+ "      and pt1.TTKT = pt.TTKT " + "      and pt1.SOURCE_TASK=6 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcXdddDuKienHoanThanhValue " + "from PROGRESS_TASK_OS pt "
			+ "left join (select " + "task.TTKT ttkt, " + "sum(task.HSHC_VALUE) valueHshc "
			+ "from PROGRESS_TASK_OS task " + "where task.status!=0 and task.SOURCE_TASK = 6 ");
		ifElseToanQuoc(sql,obj,"task");
		sql.append(" group by "
			+ "task.TTKT) taskOs " + "on pt.ttkt = taskOs.ttkt where 1=1 ");
	}
	
	public void tdSlHtctToanQuoc(StringBuilder sql, ProgressTaskOsDTO obj) {
		sql.append(" ,tdSlHtct as (select pt.TTKV ttkv, " + "pt.TTKT ttkt, "
			+ "nvl(MAX(taskOs.valueQuantity),0) tdSlHtctKhThang, " 
			+ "nvl((select sum(pt1.QUANTITY_VALUE) "
			+ "			from PROGRESS_TASK_OS pt1  "
			+ "      where pt1.status!=0 and pt1.TDSL_ACCOMPLISHED_DATE is not null  " + "      and pt1.TTKT = pt.TTKT "
			+ "      and pt1.SOURCE_TASK in (4,5) ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdSlHtctDaHt, "
			+ "nvl((select sum(pt1.QUANTITY_VALUE) " + "			from PROGRESS_TASK_OS pt1  "
			+ "      where pt1.status!=0 and pt1.TDSL_CONSTRUCTING is not null and pt1.TDSL_ACCOMPLISHED_DATE is null   " + "      and pt1.TTKT = pt.TTKT "
			+ "      and pt1.SOURCE_TASK in (4,5) ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdSlHtctDangTcValue, "
			+ "nvl((select sum(pt1.QUANTITY_VALUE) " + "			from PROGRESS_TASK_OS pt1  "
			+ "      where pt1.status!=0 and pt1.TDSL_EXPECTED_COMPLETE_DATE is not null  "
			+ "      and pt1.TTKT = pt.TTKT " + "      and pt1.SOURCE_TASK in (4,5) ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdSlHtctDuKienHtValue " + "from PROGRESS_TASK_OS pt "
			+ "left join (select " + "task.TTKT ttkt, " + "sum(task.QUANTITY_VALUE) valueQuantity "
			+ "from PROGRESS_TASK_OS task " + "where task.status!=0 and task.SOURCE_TASK in (4,5) ");
		ifElseToanQuoc(sql,obj,"task");
		sql.append(" group by "
			+ "task.TTKT) taskOs " + "on pt.ttkt = taskOs.ttkt where 1=1 ");
	}
	
	public void tong3Kv(StringBuilder sql, String khuVuc) {
		sql.append(" select tdql.ttkv ttkvGroup, translate ('' using nchar_cs) ttkv,translate (" +"'"+ khuVuc +"'" + " using nchar_cs) ttkt, sum(nvl(tdql.tdQlKhThang,0)) tdQlKhThang, "
				+ "sum(nvl(tdql.tdQlTrenDuongValue,0)) tdQlTrenDuongValue, "
				+ "case when sum(nvl(tdql.tdQlKhThang,0))>0 then (sum(nvl(tdql.tdQlTrenDuongValue,0))/sum(nvl(tdql.tdQlKhThang,0)) ) else 0 end tdQlTrenDuongTyle, "
				+ "sum(nvl(tdql.tdQlGdCnKyValue,0)) tdQlGdCnKyValue,"
				+ "case when sum(nvl(tdql.tdQlKhThang,0))>0 then (sum(nvl(tdql.tdQlGdCnKyValue,0))/sum(nvl(tdql.tdQlKhThang,0)) ) else 0 end tdQlGdCnKyTyLe, "
				+ "sum(nvl(tdql.tdQlDoiSoat4aValue,0)) tdQlDoiSoat4aValue,"
				+ "case when sum(nvl(tdql.tdQlKhThang,0))>0 then (sum(nvl(tdql.tdQlDoiSoat4aValue,0))/sum(nvl(tdql.tdQlKhThang,0)) ) else 0 end tdQlDoiSoat4aTyLe,"
				+ "sum(nvl(tdql.tdQlPhtThamDuyetValue,0)) tdQlPhtThamDuyetValue,"
				+ "case when sum(nvl(tdql.tdQlKhThang,0))>0 then (sum(nvl(tdql.tdQlPhtThamDuyetValue,0))/sum(nvl(tdql.tdQlKhThang,0)) ) else 0 end tdQlPhtThamDuyetTyLe,"
				+ "sum(nvl(tdql.tdQlPhtNghiemThuValue,0)) tdQlPhtNghiemThuValue,"
				+ "case when sum(nvl(tdql.tdQlKhThang,0))>0 then (sum(nvl(tdql.tdQlPhtNghiemThuValue,0))/sum(nvl(tdql.tdQlKhThang,0)) ) else 0 end tdQlPhtNghiemThuTyLe, "
				+ "sum(nvl(tdql.tdQlDangLamHoSoValue,0)) tdQlDangLamHoSoValue,"
				+ "case when sum(nvl(tdql.tdQlKhThang,0))>0 then (sum(nvl(tdql.tdQlDangLamHoSoValue,0))/sum(nvl(tdql.tdQlKhThang,0)) ) else 0 end tdQlDangLamHoSoTyLe, "
				+ "sum(nvl(tdql.tdQlDuKienHoanThanhValue,0)) tdQlDuKienHoanThanhValue,"
				+ "case when sum(nvl(tdql.tdQlKhThang,0))>0 then (sum(nvl(tdql.tdQlDuKienHoanThanhValue,0))/sum(nvl(tdql.tdQlKhThang,0)) ) else 0 end tdQlDuKienHoanThanhTyLe,"
				+ "null tdQlNguyenNhanKoHt,"

				+ "sum(nvl(tdHshcCp.tdHshcCpKhThang,0)) tdHshcCpKhThang,"
				+ "sum(nvl(tdHshcCp.tdHshcCpTrenDuongValue,0)) tdHshcCpTrenDuongValue,"
				+ "case  when sum(nvl(tdHshcCp.tdHshcCpKhThang,0))>0 then sum(nvl(tdHshcCp.tdHshcCpTrenDuongValue,0))/sum(nvl(tdHshcCp.tdHshcCpKhThang,0))  else 0 end  tdHshcCpTrenDuongTyLe,"
				+ "sum(nvl(tdHshcCp.tdHshcCpGdCnKyValue,0)) tdHshcCpGdCnKyValue,"
				+ "case  when sum(nvl(tdHshcCp.tdHshcCpKhThang,0))>0 then sum(nvl(tdHshcCp.tdHshcCpGdCnKyValue,0))/sum(nvl(tdHshcCp.tdHshcCpKhThang,0))  else 0 end  tdHshcCpGdCnKyTyLe,"
				+ "sum(nvl(tdHshcCp.tdHshcCpDoiSoat4aValue,0)) tdHshcCpDoiSoat4aValue, "
				+ "case  when sum(nvl(tdHshcCp.tdHshcCpKhThang,0))>0 then sum(nvl(tdHshcCp.tdHshcCpDoiSoat4aValue,0))/sum(nvl(tdHshcCp.tdHshcCpKhThang,0))  else 0 end  tdHshcCpDoiSoat4aTyLe,"
				+ "sum(nvl(tdHshcCp.tdHshcCpPhtThamDuyetValue,0)) tdHshcCpPhtThamDuyetValue, "
				+ "case  when sum(nvl(tdHshcCp.tdHshcCpKhThang,0))>0 then sum(nvl(tdHshcCp.tdHshcCpPhtThamDuyetValue,0))/sum(nvl(tdHshcCp.tdHshcCpKhThang,0))  else 0 end  tdHshcCpPhtThamDuyetTyLe, "
				+ "sum(nvl(tdHshcCp.tdHshcCpPhtNghiemThuValue,0)) tdHshcCpPhtNghiemThuValue,"
				+ "case  when sum(nvl(tdHshcCp.tdHshcCpKhThang,0))>0 then sum(nvl(tdHshcCp.tdHshcCpPhtNghiemThuValue,0))/sum(nvl(tdHshcCp.tdHshcCpKhThang,0))  else 0 end  tdHshcCpPhtNghiemThuTyLe,"
				+ "sum(nvl(tdHshcCp.tdHshcCpDangLamHoSoValue,0)) tdHshcCpDangLamHoSoValue, "
				+ "case  when sum(nvl(tdHshcCp.tdHshcCpKhThang,0))>0 then sum(nvl(tdHshcCp.tdHshcCpDangLamHoSoValue,0))/sum(nvl(tdHshcCp.tdHshcCpKhThang,0))  else 0 end  tdHshcCpDangLamHoSoTyLe,"
				+ "sum(nvl(tdHshcCp.tdHshcCpDuKienHoanThanhValue,0)) tdHshcCpDuKienHoanThanhValue,"
				+ "case  when sum(nvl(tdHshcCp.tdHshcCpKhThang,0))>0 then sum(nvl(tdHshcCp.tdHshcCpDuKienHoanThanhValue,0))/sum(nvl(tdHshcCp.tdHshcCpKhThang,0))  else 0 end  tdHshcCpDuKienHoanThanhTyLe,"
				+ "null tdHshcCpNguyenNhanKoHt, "

				+ "sum(nvl(tdHshcXl.tdHshcXlKhThang,0)) tdHshcXlKhThang, "
				+ "sum(nvl(tdHshcXl.tdHshcXlTrenDuongValue,0)) tdHshcXlTrenDuongValue, "
				+ "case  when sum(nvl(tdHshcXl.tdHshcXlKhThang,0))>0 then sum(nvl(tdHshcXl.tdHshcXlTrenDuongValue,0))/sum(nvl(tdHshcXl.tdHshcXlKhThang,0))  else 0 end  tdHshcXlTrenDuongTyLe, "
				+ "sum(nvl(tdHshcXl.tdHshcXlGdCnKyValue,0)) tdHshcXlGdCnKyValue,"
				+ "case  when sum(nvl(tdHshcXl.tdHshcXlKhThang,0))>0 then sum(nvl(tdHshcXl.tdHshcXlGdCnKyValue,0))/sum(nvl(tdHshcXl.tdHshcXlKhThang,0))  else 0 end  tdHshcXlGdCnKyTyLe, "
				+ "sum(nvl(tdHshcXl.tdHshcXlDoiSoat4aValue,0)) tdHshcXlDoiSoat4aValue,"
				+ "case  when sum(nvl(tdHshcXl.tdHshcXlKhThang,0))>0 then sum(nvl(tdHshcXl.tdHshcXlDoiSoat4aValue,0))/sum(nvl(tdHshcXl.tdHshcXlKhThang,0))  else 0 end  tdHshcXlDoiSoat4aTyLe, "
				+ "sum(nvl(tdHshcXl.tdHshcXlPhtThamDuyetValue,0)) tdHshcXlPhtThamDuyetValue,"
				+ "case  when sum(nvl(tdHshcXl.tdHshcXlKhThang,0))>0 then sum(nvl(tdHshcXl.tdHshcXlPhtThamDuyetValue,0))/sum(nvl(tdHshcXl.tdHshcXlKhThang,0))  else 0 end  tdHshcXlPhtThamDuyetTyLe, "
				+ "sum(nvl(tdHshcXl.tdHshcXlPhtNghiemThuValue,0)) tdHshcXlPhtNghiemThuValue,"
				+ "case  when sum(nvl(tdHshcXl.tdHshcXlKhThang,0))>0 then sum(nvl(tdHshcXl.tdHshcXlPhtNghiemThuValue,0))/sum(nvl(tdHshcXl.tdHshcXlKhThang,0))  else 0 end  tdHshcXlPhtNghiemThuTyLe, "
				+ "sum(nvl(tdHshcXl.tdHshcXlDangLamHoSoValue,0)) tdHshcXlDangLamHoSoValue,"
				+ "case  when sum(nvl(tdHshcXl.tdHshcXlKhThang,0))>0 then sum(nvl(tdHshcXl.tdHshcXlDangLamHoSoValue,0))/sum(nvl(tdHshcXl.tdHshcXlKhThang,0))  else 0 end  tdHshcXlDangLamHoSoTyLe, "
				+ "sum(nvl(tdHshcXl.tdHshcXlDuKienHoanThanhValue,0)) tdHshcXlDuKienHoanThanhValue, "
				+ "case  when sum(nvl(tdHshcXl.tdHshcXlKhThang,0))>0 then sum(nvl(tdHshcXl.tdHshcXlDuKienHoanThanhValue,0))/sum(nvl(tdHshcXl.tdHshcXlKhThang,0))  else 0 end  tdHshcXlDuKienHoanThanhTyLe, "
				+ "null tdHshcXlNguyenNhanKoHt,"

				+ "sum(nvl(tdHshcNtd.tdHshcNtdKhThang,0)) tdHshcNtdKhThang, "
				+ "sum(nvl(tdHshcNtd.tdHshcNtdTrenDuongValue,0)) tdHshcNtdTrenDuongValue, "
				+ "case  when sum(nvl(tdHshcNtd.tdHshcNtdKhThang,0))>0 then sum(nvl(tdHshcNtd.tdHshcNtdTrenDuongValue,0))/sum(nvl(tdHshcNtd.tdHshcNtdKhThang,0))  else 0 end  tdHshcNtdTrenDuongTyLe, "
				+ "sum(nvl(tdHshcNtd.tdHshcNtdGdCnKyValue,0)) tdHshcNtdGdCnKyValue,"
				+ "case  when sum(nvl(tdHshcNtd.tdHshcNtdKhThang,0))>0 then sum(nvl(tdHshcNtd.tdHshcNtdGdCnKyValue,0))/sum(nvl(tdHshcNtd.tdHshcNtdKhThang,0))  else 0 end  tdHshcNtdGdCnKyTyLe, "
				+ "sum(nvl(tdHshcNtd.tdHshcNtdDoiSoat4aValue,0)) tdHshcNtdDoiSoat4aValue,"
				+ "case  when sum(nvl(tdHshcNtd.tdHshcNtdKhThang,0))>0 then sum(nvl(tdHshcNtd.tdHshcNtdDoiSoat4aValue,0))/sum(nvl(tdHshcNtd.tdHshcNtdKhThang,0))  else 0 end  tdHshcNtdDoiSoat4aTyLe, "
				+ "sum(nvl(tdHshcNtd.tdHshcNtdPhtThamDuyetValue,0)) tdHshcNtdPhtThamDuyetValue,"
				+ "case  when sum(nvl(tdHshcNtd.tdHshcNtdKhThang,0))>0 then sum(nvl(tdHshcNtd.tdHshcNtdPhtThamDuyetValue,0))/sum(nvl(tdHshcNtd.tdHshcNtdKhThang,0))  else 0 end  tdHshcNtdPhtThamDuyetTyLe, "
				+ "sum(nvl(tdHshcNtd.tdHshcNtdPhtNghiemThuValue,0)) tdHshcNtdPhtNghiemThuValue,"
				+ "case  when sum(nvl(tdHshcNtd.tdHshcNtdKhThang,0))>0 then sum(nvl(tdHshcNtd.tdHshcNtdPhtNghiemThuValue,0))/sum(nvl(tdHshcNtd.tdHshcNtdKhThang,0))  else 0 end  tdHshcNtdPhtNghiemThuTyLe, "
				+ "sum(nvl(tdHshcNtd.tdHshcNtdDangLamHoSoValue,0)) tdHshcNtdDangLamHoSoValue,"
				+ "case  when sum(nvl(tdHshcNtd.tdHshcNtdKhThang,0))>0 then sum(nvl(tdHshcNtd.tdHshcNtdDangLamHoSoValue,0))/sum(nvl(tdHshcNtd.tdHshcNtdKhThang,0))  else 0 end  tdHshcNtdDangLamHoSoTyLe, "
				+ "sum(nvl(tdHshcNtd.tdHshcNtdDuKienHoanThanhValue,0)) tdHshcNtdDuKienHoanThanhValue,"
				+ "case  when sum(nvl(tdHshcNtd.tdHshcNtdKhThang,0))>0 then sum(nvl(tdHshcNtd.tdHshcNtdDuKienHoanThanhValue,0))/sum(nvl(tdHshcNtd.tdHshcNtdKhThang,0))  else 0 end  tdHshcNtdDuKienHoanThanhTyLe,"
				+ "null tdHshcNtdNguyenNhanKoHt,"

				+ "sum(nvl(tdSlCp.tdSlCpKhThang,0)) tdSlCpKhThang, "
				+ "sum(nvl(tdSlCp.tdSlCpDaHoanThanh,0)) tdSlCpDaHoanThanh, "
				+ "sum(nvl(tdSlCp.tdSlCpDangThiCongValue,0)) tdSlCpDangThiCongValue,"
				+ "case  when sum(nvl(tdSlCp.tdSlCpKhThang,0))>0 then sum(nvl(tdSlCp.tdSlCpDangThiCongValue,0))/sum(nvl(tdSlCp.tdSlCpKhThang,0))  else 0 end  tdSlCpDangThiCongTyLe, "
				+ "sum(nvl(tdSlCp.tdSlCpDuKienHoanThanhValue,0)) tdSlCpDuKienHoanThanhValue,"
				+ "case  when sum(nvl(tdSlCp.tdSlCpKhThang,0))>0 then sum(nvl(tdSlCp.tdSlCpDuKienHoanThanhValue,0))/sum(nvl(tdSlCp.tdSlCpKhThang,0))  else 0 end  tdSlCpDuKienHoanThanhTyLe,"
				+ "null tdSlCpNguyenNhanKoHt,"

				+ "sum(nvl(tdSlXl.tdSlXlKhThang,0)) tdSlXlKhThang, "
				+ "Sum(nvl(tdSlXl.tdSlXlDaHoanThanh,0)) tdSlXlDaHoanThanh,"
				+ "sum(nvl(tdSlXl.tdSlXlDangThiCongValue,0)) tdSlXlDangThiCongValue, "
				+ "case  when sum(nvl(tdSlXl.tdSlXlKhThang,0))>0 then sum(nvl(tdSlXl.tdSlXlDangThiCongValue,0))/sum(nvl(tdSlXl.tdSlXlKhThang,0))  else 0 end  tdSlXlDangThiCongTyLe, "
				+ "sum(nvl(tdSlXl.tdSlXlDuKienHoanThanhValue,0)) tdSlXlDuKienHoanThanhValue,"
				+ "case  when sum(nvl(tdSlXl.tdSlXlKhThang,0))>0 then sum(nvl(tdSlXl.tdSlXlDuKienHoanThanhValue,0))/sum(nvl(tdSlXl.tdSlXlKhThang,0))  else 0 end  tdSlXlDuKienHoanThanhTyLe,"
				+ "null tdSlXlNguyenNhanKoHt,"

				+ "sum(nvl(tdSlNtd.tdSlNtdKhThang,0)) tdSlNtdKhThang, "
				+ "sum(nvl(tdSlNtd.tdSlNtdDaHoanThanh,0)) tdSlNtdDaHoanThanh, "
				+ "sum(nvl(tdSlNtd.tdSlNtdDangThiCongValue,0)) tdSlNtdDangThiCongValue, "
				+ "case  when sum(nvl(tdSlNtd.tdSlNtdKhThang,0))>0 then sum(nvl(tdSlNtd.tdSlNtdDangThiCongValue,0))/sum(nvl(tdSlNtd.tdSlNtdKhThang,0))  else 0 end  tdSlNtdDangThiCongTyLe, "
				+ "sum(nvl(tdSlNtd.tdSlNtdDuKienHoanThanhValue,0)) tdSlNtdDuKienHoanThanhValue,"
				+ "case  when sum(nvl(tdSlNtd.tdSlNtdKhThang,0))>0 then sum(nvl(tdSlNtd.tdSlNtdDuKienHoanThanhValue,0))/sum(nvl(tdSlNtd.tdSlNtdKhThang,0))  else 0 end  tdSlNtdDuKienHoanThanhTyLe,"
				+ "null tdSlNtdNguyenNhanKoHt,"

				+ "sum(nvl(tdSlXddd.tdSlXdddKhThang,0)) tdSlXdddKhThang, "
				+ "sum(nvl(tdSlXddd.tdSlXdddDaHoanThanh,0)) tdSlXdddDaHoanThanh, "
				+ "sum(nvl(tdSlXddd.tdSlXdddDangThiCongValue,0)) tdSlXdddDangThiCongValue, "
				+ "case  when sum(nvl(tdSlXddd.tdSlXdddKhThang,0))>0 then sum(nvl(tdSlXddd.tdSlXdddDangThiCongValue,0)) / sum(nvl(tdSlXddd.tdSlXdddKhThang,0))  else 0 end  tdSlXdddDangThiCongTyLe,"
				+ "sum(nvl(tdSlXddd.tdSlXdddDuKienHoanThanhValue,0)) tdSlXdddDuKienHoanThanhValue, "
				+ "case  when sum(nvl(tdSlXddd.tdSlXdddKhThang,0))>0 then sum(nvl(tdSlXddd.tdSlXdddDuKienHoanThanhValue,0)) / sum(nvl(tdSlXddd.tdSlXdddKhThang,0))  else 0 end  tdSlXdddDuKienHoanThanhTyLe, "
				+ "null tdSlXdddNguyenNhanKoHt, "
				
				+ "sum(nvl(tdThdt.tdThdtKhThang,0)) tdThdtKhThang, "
				+ "sum(nvl(tdThdt.tdThdtDaHoanThanh,0)) tdThdtDaHoanThanh,"
				+ "sum(nvl(tdThdt.tdThdtPhtDangKiemTraValue,0)) tdThdtPhtDangKiemTraValue,"
				+ "case  when sum(nvl(tdThdt.tdThdtKhThang,0))>0 then sum(nvl(tdThdt.tdThdtPhtDangKiemTraValue,0))/sum(nvl(tdThdt.tdThdtKhThang,0))  else 0 end  tdThdtPhtDangKiemTraTyLe,"
				+ "sum(nvl(tdThdt.tdThdtPtcDangKiemTraValue,0)) tdThdtPtcDangKiemTraValue,"
				+ "case  when sum(nvl(tdThdt.tdThdtKhThang,0))>0 then sum(nvl(tdThdt.tdThdtPtcDangKiemTraValue,0))/sum(nvl(tdThdt.tdThdtKhThang,0))  else 0 end  tdThdtPtcDangKiemTraTyLe, "
				+ "sum(nvl(tdThdt.tdThdtDuKienHoanThanhValue,0)) tdThdtDuKienHoanThanhValue,"
				+ "case  when sum(nvl(tdThdt.tdThdtKhThang,0))>0 then sum(nvl(tdThdt.tdThdtDuKienHoanThanhValue,0))/sum(nvl(tdThdt.tdThdtKhThang,0))  else 0 end  tdThdtDuKienHoanThanhTyLe,"
				+ "null tdThdtNguyenNhanKoHt, " 
				
				+ "sum(nvl(tdHtctXdm.tdHtctXdmKhThang,0)) tdHtctXdmKhThang, "
				+ "sum(nvl(tdHtctXdm.tdHtctXdmDaHt,0)) tdHtctXdmDaHt, "
				+ "sum(nvl(tdHtctXdm.tdHtctXdmDangTc,0)) tdHtctXdmDangTc,"
				+ "sum(nvl(tdHtctXdm.tdHtctXdmDuKienHt,0)) tdHtctXdmDuKienHt, "
				+ "case when sum(nvl(tdHtctXdm.tdHtctXdmKhThang,0)) > 0 then sum(nvl(tdHtctXdm.tdHtctXdmDuKienHt,0)) / sum(nvl(tdHtctXdm.tdHtctXdmKhThang,0))  else 0 end tdHtctXdmTyLe,"
				+ "sum(nvl(tdHtctHt.tdHtctHtKhThang,0)) tdHtctHtKhThang, "
				+ "sum(nvl(tdHtctHt.tdHtctHtDaHt,0)) tdHtctHtDaHt, "
				+ "sum(nvl(tdHtctHt.tdHtctHtDangTc,0)) tdHtctHtDangTc,"
				+ "sum(nvl(tdHtctHt.tdHtctHtDuKienHt,0)) tdHtctHtDuKienHt, "
				+ "case when sum(nvl(tdHtctXdm.tdHtctXdmKhThang,0)) > 0 then sum(nvl(tdHtctHt.tdHtctHtDuKienHt,0)) / sum(nvl(tdHtctXdm.tdHtctXdmKhThang,0))  else 0 end tdHtctHtTyLe,"
				+ " null tdHtctNguyenNhanKoHt, "
				
				+ "sum(nvl(tdHshcHtct.tdHshcHtctKhThang,0)) tdHshcHtctKhThang, "
				+ "sum(nvl(tdHshcHtct.tdHshcHtctTrenDuongValue,0)) tdHshcHtctTrenDuongValue,"
				+ "case  when sum(nvl(tdHshcHtct.tdHshcHtctKhThang,0)) > 0 then sum(nvl(tdHshcHtct.tdHshcHtctTrenDuongValue,0)) / sum(nvl(tdHshcHtct.tdHshcHtctKhThang,0))  else 0 end  tdHshcHtctTrenDuongTyLe, "
				+ "sum(nvl(tdHshcHtct.tdHshcHtctGdCnKyValue,0)) tdHshcHtctGdCnKyValue,"
				+ "case  when sum(nvl(tdHshcHtct.tdHshcHtctKhThang,0)) > 0 then sum(nvl(tdHshcHtct.tdHshcHtctGdCnKyValue,0)) / sum(nvl(tdHshcHtct.tdHshcHtctKhThang,0))  else 0 end  tdHshcHtctGdCnKyTyLe,"
				+ "sum(nvl(tdHshcHtct.tdHshcHtctDoiSoat4aValue,0)) tdHshcHtctDoiSoat4aValue,"
				+ "case  when sum(nvl(tdHshcHtct.tdHshcHtctKhThang,0)) > 0 then sum(nvl(tdHshcHtct.tdHshcHtctDoiSoat4aValue,0)) / sum(nvl(tdHshcHtct.tdHshcHtctKhThang,0))  else 0 end  tdHshcHtctDoiSoat4aTyLe,"
				+ "sum(nvl(tdHshcHtct.tdHshcHtctPhtThamDuyetValue,0)) tdHshcHtctPhtThamDuyetValue,"
				+ "case  when sum(nvl(tdHshcHtct.tdHshcHtctKhThang,0)) > 0 then sum(nvl(tdHshcHtct.tdHshcHtctPhtThamDuyetValue,0)) / sum(nvl(tdHshcHtct.tdHshcHtctKhThang,0))  else 0 end  tdHshcHtctPhtThamDuyetTyLe,"
				+ "sum(nvl(tdHshcHtct.tdHshcHtctPhtNghiemThuValue,0)) tdHshcHtctPhtNghiemThuValue,"
				+ "case  when sum(nvl(tdHshcHtct.tdHshcHtctKhThang,0)) > 0 then sum(nvl(tdHshcHtct.tdHshcHtctPhtNghiemThuValue,0)) / sum(nvl(tdHshcHtct.tdHshcHtctKhThang,0))  else 0 end  tdHshcHtctPhtNghiemThuTyLe,"
				+ "sum(nvl(tdHshcHtct.tdHshcHtctTtktHoSoValue,0)) tdHshcHtctTtktHoSoValue,"
				+ "case  when sum(nvl(tdHshcHtct.tdHshcHtctKhThang,0)) > 0 then sum(nvl(tdHshcHtct.tdHshcHtctTtktHoSoValue,0)) / sum(nvl(tdHshcHtct.tdHshcHtctKhThang,0))  else 0 end  tdHshcHtctTtktHoSoTyLe,"
				+ "sum(nvl(tdHshcHtct.tdHshcHtctDuKienHtValue,0)) tdHshcHtctDuKienHtValue,"
				+ "case  when sum(nvl(tdHshcHtct.tdHshcHtctKhThang,0)) > 0 then sum(nvl(tdHshcHtct.tdHshcHtctDuKienHtValue,0)) / sum(nvl(tdHshcHtct.tdHshcHtctKhThang,0))  else 0 end  tdHshcHtctDuKienHtTyLe, null tdHshcHtctNguyenNhanKoHt, "
				
				+ "sum(nvl(tdSlCp.tdSlCpKhThang,0)) + sum(nvl(tdSlXl.tdSlXlKhThang,0)) + sum(nvl(tdSlNtd.tdSlNtdKhThang,0)) tdTongKhThang, " 
				+ "sum(nvl(tdSlCp.tdSlCpDaHoanThanh,0)) + sum(nvl(tdSlXl.tdSlXlDaHoanThanh,0)) + sum(nvl(tdSlNtd.tdSlNtdDaHoanThanh,0)) tdTongDaHt, "
				+ "sum(nvl(tdSlCp.tdSlCpDangThiCongValue,0)) + sum(nvl(tdSlXl.tdSlXlDangThiCongValue,0)) + sum(nvl(tdSlNtd.tdSlNtdDangThiCongValue,0)) tdTongDangTcValue,"
				+ " case when (sum(nvl(tdSlCp.tdSlCpKhThang,0)) + sum(nvl(tdSlXl.tdSlXlKhThang,0)) + sum(nvl(tdSlNtd.tdSlNtdKhThang,0)))>0 then ((sum(nvl(tdSlCp.tdSlCpDangThiCongValue,0)) + sum(nvl(tdSlXl.tdSlXlDangThiCongValue,0)) + sum(nvl(tdSlNtd.tdSlNtdDangThiCongValue,0)))/(sum(nvl(tdSlCp.tdSlCpKhThang,0)) + sum(nvl(tdSlXl.tdSlXlKhThang,0)) + sum(nvl(tdSlNtd.tdSlNtdKhThang,0)))) else 0 end tdTongDangTcTyLe,"
				+ "sum(nvl(tdSlCp.tdSlCpDuKienHoanThanhValue,0)) + sum(nvl(tdSlXl.tdSlXlDuKienHoanThanhValue,0)) + sum(nvl(tdSlNtd.tdSlNtdDuKienHoanThanhValue,0)) tdTongDuKienHtValue,"
				+ "case when (sum(nvl(tdSlCp.tdSlCpKhThang,0)) + sum(nvl(tdSlXl.tdSlXlKhThang,0)) + sum(nvl(tdSlNtd.tdSlNtdKhThang,0)))>0 then ((sum(nvl(tdSlCp.tdSlCpDuKienHoanThanhValue,0)) + sum(nvl(tdSlXl.tdSlXlDuKienHoanThanhValue,0)) + sum(nvl(tdSlNtd.tdSlNtdDuKienHoanThanhValue,0)))/(sum(nvl(tdSlCp.tdSlCpKhThang,0)) + sum(nvl(tdSlXl.tdSlXlKhThang,0)) + sum(nvl(tdSlNtd.tdSlNtdKhThang,0)))) else 0 end tdTongDuKienHtTyLe,"
				+ "null tdTongDeXuatVuongMac "
				
				+ ", sum(nvl(tdHshcXddd.tdHshcXdddKhThang,0)) tdHshcXdddKhThang, "
				+ " sum(nvl(tdHshcXddd.tdHshcXdddTrenDuongValue,0)) tdHshcXdddTrenDuongValue,"
				+ " case  when sum(nvl(tdHshcXddd.tdHshcXdddKhThang,0)) > 0 then (sum(nvl(tdHshcXddd.tdHshcXdddTrenDuongValue,0)) / sum(nvl(tdHshcXddd.tdHshcXdddKhThang,0)) ) else 0 end  tdHshcXdddTrenDuongTyLe,"
				+ " sum(nvl(tdHshcXddd.tdHshcXdddGdCnKyValue,0)) tdHshcXdddGdCnKyValue,"
				+ " case  when sum(nvl(tdHshcXddd.tdHshcXdddKhThang,0)) > 0 then (sum(nvl(tdHshcXddd.tdHshcXdddGdCnKyValue,0)) / sum(nvl(tdHshcXddd.tdHshcXdddKhThang,0)) ) else 0 end  tdHshcXdddGdCnKyTyLe,"
				+ " sum(nvl(tdHshcXddd.tdHshcXdddDoiSoat4aValue,0)) tdHshcXdddDoiSoat4aValue,"
				+ " case  when sum(nvl(tdHshcXddd.tdHshcXdddKhThang,0)) > 0 then (sum(nvl(tdHshcXddd.tdHshcXdddDoiSoat4aValue,0)) / sum(nvl(tdHshcXddd.tdHshcXdddKhThang,0)) ) else 0 end  tdHshcXdddDoiSoat4aTyLe,"
				+ " sum(nvl(tdHshcXddd.tdHshcXdddPhtThamDuyetValue,0)) tdHshcXdddPhtThamDuyetValue,"
				+ " case  when sum(nvl(tdHshcXddd.tdHshcXdddKhThang,0)) > 0 then (sum(nvl(tdHshcXddd.tdHshcXdddPhtThamDuyetValue,0)) / sum(nvl(tdHshcXddd.tdHshcXdddKhThang,0)) ) else 0 end  tdHshcXdddPhtThamDuyetTyLe,"
				+ " sum(nvl(tdHshcXddd.tdHshcXdddPhtNghiemThuValue,0)) tdHshcXdddPhtNghiemThuValue,"
				+ " case  when sum(nvl(tdHshcXddd.tdHshcXdddKhThang,0)) > 0 then (sum(nvl(tdHshcXddd.tdHshcXdddPhtNghiemThuValue,0)) / sum(nvl(tdHshcXddd.tdHshcXdddKhThang,0)) ) else 0 end  tdHshcXdddPhtNghiemThuTyLe,"
				+ " sum(nvl(tdHshcXddd.tdHshcXdddDangLamHoSoValue,0)) tdHshcXdddDangLamHoSoValue,"
				+ " case  when sum(nvl(tdHshcXddd.tdHshcXdddKhThang,0)) > 0 then (sum(nvl(tdHshcXddd.tdHshcXdddDangLamHoSoValue,0)) / sum(nvl(tdHshcXddd.tdHshcXdddKhThang,0)) ) else 0 end  tdHshcXdddDangLamHoSoTyLe,"
				+ " sum(nvl(tdHshcXddd.tdHshcXdddDuKienHoanThanhValue,0)) tdHshcXdddDuKienHoanThanhValue,"
				+ " case  when sum(nvl(tdHshcXddd.tdHshcXdddKhThang,0)) > 0 then (sum(nvl(tdHshcXddd.tdHshcXdddDuKienHoanThanhValue,0)) / sum(nvl(tdHshcXddd.tdHshcXdddKhThang,0)) ) else 0 end  tdHshcXdddDuKienHoanThanhTyLe,"
				+ " null tdHshcXdddNguyenNhanKoHt "
				
				+ ", sum(nvl(tdSlHtct.tdSlHtctKhThang,0)) tdSlHtctKhThang, "
				+ " sum(nvl(tdSlHtct.tdSlHtctDaHt,0)) tdSlHtctDaHt, "
				+ " sum(nvl(tdSlHtct.tdSlHtctDangTcValue,0)) tdSlHtctDangTcValue,"
				+ " case when sum(nvl(tdSlHtct.tdSlHtctKhThang,0)) > 0 then (sum(nvl(tdSlHtct.tdSlHtctDangTcValue,0)) / sum(nvl(tdSlHtct.tdSlHtctKhThang,0)) ) else 0 end tdSlHtctDangTcTyLe,"
				+ " sum(nvl(tdSlHtct.tdSlHtctDuKienHtValue,0)) tdSlHtctDuKienHtValue,"
				+ " case when sum(nvl(tdSlHtct.tdSlHtctKhThang,0)) > 0 then (sum(nvl(tdSlHtct.tdSlHtctDuKienHtValue,0)) / sum(nvl(tdSlHtct.tdSlHtctKhThang,0)) ) else 0 end tdSlHtctDuKienHtTyLe,"
				+ " null tdSlHtctNguyenNhanKoHt "
				
				+ " from tdql " 
				+ " full join tdHshcCp on tdql.ttkv = tdHshcCp.ttkv and tdql.ttkt = tdHshcCp.ttkt "
				+ " full join tdHshcXl on tdql.ttkv = tdHshcXl.ttkv and tdql.ttkt = tdHshcXl.ttkt "
				+ " full join tdHshcNtd on tdql.ttkv = tdHshcNtd.ttkv and tdql.ttkt = tdHshcNtd.ttkt "
				+ " full join tdSlCp on tdql.ttkv = tdSlCp.ttkv and tdql.ttkt = tdSlCp.ttkt " 
				+ " full join tdSlXl on tdql.ttkv = tdSlXl.ttkv and tdql.ttkt = tdSlXl.ttkt "
				+ " full join tdSlNtd on tdql.ttkv = tdSlNtd.ttkv and tdql.ttkt = tdSlNtd.ttkt " 
				+ " full join tdSlXddd on tdql.ttkv = tdSlXddd.ttkv and tdql.ttkt = tdSlXddd.ttkt "
				+ " full join tdThDt on tdql.ttkv = tdThDt.ttkv and tdql.ttkt = tdThDt.ttkt "
				+ " full join tdHtctXdm on tdql.ttkv = tdHtctXdm.ttkv and tdql.ttkt = tdHtctXdm.ttkt " 
				+ " full join tdHtctHt on tdql.ttkv =  tdHtctHt.ttkv and tdql.ttkt = tdHtctHt.ttkt "
				+ " full join tdHshcHtct on tdql.ttkv =  tdHshcHtct.ttkv and tdql.ttkt = tdHshcHtct.ttkt "
				+ " full join tdHshcXddd on tdql.ttkv =  tdHshcXddd.ttkv and tdql.ttkt = tdHshcXddd.ttkt "
				+ " full join tdSlHtct on tdql.ttkv =  tdSlHtct.ttkv and tdql.ttkt = tdSlHtct.ttkt ");
				if(khuVuc.equals("KV1")) {
					sql.append(" where tdql.ttkv='KV1' group by tdql.ttkv ");
				} else if(khuVuc.equals("KV2")) {
					sql.append(" where tdql.ttkv='KV2' group by tdql.ttkv ");
				} else if(khuVuc.equals("KV3")) {
					sql.append(" where tdql.ttkv='KV3' group by tdql.ttkv ");
				} else {
					sql.append(" group by tdql.ttkv ");
				}
				
	}
	
	
	public List<ProgressTaskOsDTO> getDataExportSheet1(ProgressTaskOsDTO obj) {
		StringBuilder sql = new StringBuilder(" with ");
		
		// Tiến độ Quỹ lương
		tdqlToanQuoc(sql, obj);
		ifElseToanQuoc(sql,obj,"pt");
		sql.append("group by pt.TTKV, " + "pt.TTKT) ");
		
		// Tiến độ HSHC Chi phí
		tdHshcCpToanQuoc(sql, obj);
		ifElseToanQuoc(sql,obj,"pt");
		sql.append("group by pt.TTKV, " + "pt.TTKT) ");
		
		// Tiến độ HSHC Xây lắp
		tdHshcXlToanQuoc(sql, obj);
		ifElseToanQuoc(sql,obj,"pt");
		sql.append("group by pt.TTKV, " + "pt.TTKT) ");
		
		// Tiến độ HSHC Ngoài tập đoàn
		tdHshcNtdToanQuoc(sql, obj);
		ifElseToanQuoc(sql,obj,"pt");
		sql.append("group by pt.TTKV, " + "pt.TTKT) ");
		
		// Tiến độ Sản lượng Chi phí
		tdSlCpToanQuoc(sql, obj);
		ifElseToanQuoc(sql,obj,"pt");
		sql.append("group by pt.TTKV, " + "pt.TTKT) ");
		
		// Tiến độ Sản lượng Xây lắp
		tdSlXlToanQuoc(sql, obj);
		ifElseToanQuoc(sql,obj,"pt");
		sql.append("group by pt.TTKV, " + "pt.TTKT) ");
		
		// Tiến độ Sản lượng Ngoài tập đoàn
		tdSlNtdToanQuoc(sql, obj);
		ifElseToanQuoc(sql,obj,"pt");
		sql.append("group by pt.TTKV, " + "pt.TTKT) ");
		
		//Tiến độ Sản lượng XDDD
		tdSlXdddToanQuoc(sql, obj);
		ifElseToanQuoc(sql,obj,"pt");
		sql.append("group by pt.TTKV, " + "pt.TTKT) ");
		
		// Tiến độ Thu hồi dòng tiền
		tdThDtToanQuoc(sql, obj);
		ifElseToanQuoc(sql,obj,"pt");
		sql.append("group by pt.TTKV, " + "pt.TTKT) ");
		
		//Tiến độ triển khai HTCT Xây dựng móng
			sql.append(" ,tdHtctXdm as (SELECT DISTINCT " + "  TTKV ttkv, " + "  TTKT ttkt, " + "  MAX(khThang) tdHtctXdmKhThang, "
					+ "  MAX((case when accomplish is not null then (select count(distinct CONSTRUCTION_CODE) from (select * from PROGRESS_TASK_OS WHERE SOURCE_TASK = 4 and STATUS!=0 and WORK_ITEM_NAME is not null ");
			if (StringUtils.isNoneBlank(obj.getTtkt())) {
				sql.append(" and TTKT = :ttkt ");
			}
			if (StringUtils.isNotBlank(obj.getMonthYear())) {
				sql.append(" AND MONTH_YEAR = :monthYear ");
			}
			
			if(obj.getGroupIdList()!=null && !obj.getGroupIdList().isEmpty()) {
				sql.append(" and TTKT_ID in (:groupIdList) ");
			}
			
			sql.append("  and TDSL_ACCOMPLISHED_DATE is not null)) else null end)) tdHtctXdmDaHt, "
					+ "  MAX((case when constructing is not null and accomplish is null then (select count(distinct CONSTRUCTION_CODE) from (select * from PROGRESS_TASK_OS WHERE SOURCE_TASK = 4 and STATUS!=0 and WORK_ITEM_NAME is not null ");
	
			if (StringUtils.isNoneBlank(obj.getTtkt())) {
				sql.append(" and TTKT = :ttkt ");
			}
			if (StringUtils.isNotBlank(obj.getMonthYear())) {
				sql.append(" AND MONTH_YEAR = :monthYear ");
			}
	
			if(obj.getGroupIdList()!=null && !obj.getGroupIdList().isEmpty()) {
				sql.append(" and TTKT_ID in (:groupIdList) ");
			}
			
			sql.append("  and TDSL_CONSTRUCTING is not null))  "
					+ "  when constructing is not null and accomplish is not null then (select count(distinct CONSTRUCTION_CODE) from (select * from PROGRESS_TASK_OS WHERE SOURCE_TASK = 4 and STATUS!=0 and WORK_ITEM_NAME is not null ");
	
			if (StringUtils.isNoneBlank(obj.getTtkt())) {
				sql.append(" and TTKT = :ttkt ");
			}
			if (StringUtils.isNotBlank(obj.getMonthYear())) {
				sql.append(" AND MONTH_YEAR = :monthYear ");
			}
	
			if(obj.getGroupIdList()!=null && !obj.getGroupIdList().isEmpty()) {
				sql.append(" and TTKT_ID in (:groupIdList) ");
			}
			
			sql.append("  and TDSL_CONSTRUCTING is not null and TDSL_ACCOMPLISHED_DATE is null)) "
					+ "  else null end)) tdHtctXdmDangTc, ");
		sql.append(" MAX((case when expectedDate is not null  "
				+ "then (select count(*) from (select TTKV from PROGRESS_TASK_OS  " + "WHERE " + "SOURCE_TASK = 4  "
				+ "and STATUS!=0 and WORK_ITEM_NAME is not null ");
		if (StringUtils.isNoneBlank(obj.getTtkt())) {
			sql.append(" and TTKT = :ttkt ");
		}
		if (StringUtils.isNotBlank(obj.getMonthYear())) {
			sql.append(" AND MONTH_YEAR = :monthYear ");
		}

		if (obj.getGroupIdList() != null && !obj.getGroupIdList().isEmpty()) {
			sql.append(" and TTKT_ID in (:groupIdList) ");
		}
		sql.append(" and TDSL_EXPECTED_COMPLETE_DATE is not null)) " + "else null " + "end))  tdHtctXdmDuKienHt "
				+ "FROM " + "  (SELECT  " + "  COUNT(distinct CONSTRUCTION_CODE) khThang, TTKV, TTKT, "
				+ "    MAX(TDSL_ACCOMPLISHED_DATE) accomplish, " + "    MAX(TDSL_CONSTRUCTING) constructing, "
				+ "    MAX(TDSL_EXPECTED_COMPLETE_DATE) expectedDate " + "  FROM PROGRESS_TASK_OS "
				+ "  WHERE SOURCE_TASK = 4 and STATUS!=0 and WORK_ITEM_NAME is not null ");
	
			if (StringUtils.isNoneBlank(obj.getTtkt())) {
				sql.append(" and TTKT = :ttkt ");
			}
			if (StringUtils.isNotBlank(obj.getMonthYear())) {
				sql.append(" AND MONTH_YEAR = :monthYear ");
			}
	
			if(obj.getGroupIdList()!=null && !obj.getGroupIdList().isEmpty()) {
				sql.append(" and TTKT_ID in (:groupIdList) ");
			}
			
			sql.append("  GROUP BY TTKT, " + "  TTKV " + "  ) " + "  GROUP BY TTKT, " + "  TTKV) ");
			
			//Tiến độ HTCT Hoàn thiện
			sql.append(" ,tdHtctHt as (SELECT DISTINCT " + "  TTKV ttkv, " + "  TTKT ttkt, " + "  MAX(khThang) tdHtctHtKhThang, "
					+ "  MAX((case when accomplish is not null then (select count(distinct CONSTRUCTION_CODE) from (select * from PROGRESS_TASK_OS WHERE SOURCE_TASK = 5 and STATUS!=0 and WORK_ITEM_NAME is not null ");
			if (StringUtils.isNoneBlank(obj.getTtkt())) {
				sql.append(" and TTKT = :ttkt ");
			} 
			if (StringUtils.isNotBlank(obj.getMonthYear())) {
				sql.append(" AND MONTH_YEAR = :monthYear ");
			}
			
			if(obj.getGroupIdList()!=null && !obj.getGroupIdList().isEmpty()) {
				sql.append(" and TTKT_ID in (:groupIdList) ");
			}
			
			sql.append("  and TDSL_ACCOMPLISHED_DATE is not null)) else null end)) tdHtctHtDaHt, "
					+ "  MAX((case when constructing is not null and accomplish is null then (select count(distinct CONSTRUCTION_CODE) from (select * from PROGRESS_TASK_OS WHERE SOURCE_TASK = 5 and STATUS!=0 and WORK_ITEM_NAME is not null ");
	
			if (StringUtils.isNoneBlank(obj.getTtkt())) {
				sql.append(" and TTKT = :ttkt ");
			}
			if (StringUtils.isNotBlank(obj.getMonthYear())) {
				sql.append(" AND MONTH_YEAR = :monthYear ");
			}
	
			if(obj.getGroupIdList()!=null && !obj.getGroupIdList().isEmpty()) {
				sql.append(" and TTKT_ID in (:groupIdList) ");
			}
			
			sql.append("  and TDSL_CONSTRUCTING is not null))  "
					+ "  when constructing is not null and accomplish is not null then (select count(distinct CONSTRUCTION_CODE) from (select * from PROGRESS_TASK_OS WHERE SOURCE_TASK = 5 and STATUS!=0 and WORK_ITEM_NAME is not null ");
	
			if (StringUtils.isNoneBlank(obj.getTtkt())) {
				sql.append(" and TTKT = :ttkt ");
			}
			if (StringUtils.isNotBlank(obj.getMonthYear())) {
				sql.append(" AND MONTH_YEAR = :monthYear ");
			}
	
			if(obj.getGroupIdList()!=null && !obj.getGroupIdList().isEmpty()) {
				sql.append(" and TTKT_ID in (:groupIdList) ");
			}
			
			sql.append("  and TDSL_CONSTRUCTING is not null and TDSL_ACCOMPLISHED_DATE is null)) "
					+ "  else null end)) tdHtctHtDangTc, ");
			sql.append(" MAX((case when expectedDate is not null  "
					+ "then (select count(*) from (select TTKV from PROGRESS_TASK_OS  " + "WHERE " + "SOURCE_TASK = 5  "
					+ "and STATUS!=0  and WORK_ITEM_NAME is not null ");
			if (StringUtils.isNoneBlank(obj.getTtkt())) {
				sql.append(" and TTKT = :ttkt ");
			}
			if (StringUtils.isNotBlank(obj.getMonthYear())) {
				sql.append(" AND MONTH_YEAR = :monthYear ");
			}

			if (obj.getGroupIdList() != null && !obj.getGroupIdList().isEmpty()) {
				sql.append(" and TTKT_ID in (:groupIdList) ");
			}
			sql.append(" and TDSL_EXPECTED_COMPLETE_DATE is not null)) " + "else null " + "end))  tdHtctHtDuKienHt "
					+ " FROM " + "  (SELECT  "
					+ "  COUNT(distinct CONSTRUCTION_CODE) khThang, TTKV, TTKT, " + "    MAX(TDSL_ACCOMPLISHED_DATE) accomplish, "
					+ "    MAX(TDSL_CONSTRUCTING) constructing, " + "    MAX(TDSL_EXPECTED_COMPLETE_DATE) expectedDate "
					+ "  FROM PROGRESS_TASK_OS " + "  WHERE SOURCE_TASK = 5 and STATUS!=0  and WORK_ITEM_NAME is not null ");
	
			if (StringUtils.isNoneBlank(obj.getTtkt())) {
				sql.append(" and TTKT = :ttkt ");
			}
			if (StringUtils.isNotBlank(obj.getMonthYear())) {
				sql.append(" AND MONTH_YEAR = :monthYear ");
			}
	
			if(obj.getGroupIdList()!=null && !obj.getGroupIdList().isEmpty()) {
				sql.append(" and TTKT_ID in (:groupIdList) ");
			}
			
			sql.append("  GROUP BY TTKT, " + "  TTKV " + "  ) " + "  GROUP BY TTKT, " + "  TTKV) ");
			
			//Tiến độ HSHC HTCT
			tdHshcHtctToanQuoc(sql, obj);
			ifElseToanQuoc(sql,obj,"pt");
			sql.append("group by  pt.TTKV, "
				+ " pt.TTKT) ");
			
			//Tiến độ tổng HSHC XDDD
			tdHshcXdddToanQuoc(sql, obj); 
			ifElseToanQuoc(sql,obj,"pt");
			sql.append("group by  pt.TTKV, "
				+ " pt.TTKT) ");

			//Tiến độ Sản lượng HTCT
			tdSlHtctToanQuoc(sql, obj);
			ifElseToanQuoc(sql,obj,"pt");
			sql.append("group by  pt.TTKV, "
				+ " pt.TTKT) ");
		
		tong3Kv(sql, "Tổng");
		sql.append(" UNION ALL ");
		tong3Kv(sql, "KV1");
		sql.append(" UNION ALL ");
		tong3Kv(sql, "KV2");
		sql.append(" UNION ALL ");
		tong3Kv(sql, "KV3");
			
		sql.append(" UNION ALL ");
		
		sql.append(" select translate ('' using nchar_cs) ttkvGroup,tdql.ttkv ttkv,tdql.ttkt ttkt,nvl(tdql.tdQlKhThang,0) tdQlKhThang, "
				+ "nvl(tdql.tdQlTrenDuongValue,0) tdQlTrenDuongValue, "
				+ "nvl(tdql.tdQlGdCnKyValue,0) tdQlGdCnKyValue,"
				+ "(case when nvl(tdql.tdQlKhThang,0)>0 then (nvl(tdql.tdQlTrenDuongValue,0)/tdql.tdQlKhThang ) else 0 end) tdQlTrenDuongTyle, "
				+ "(case when nvl(tdql.tdQlKhThang,0)>0 then nvl(tdql.tdQlGdCnKyValue,0)/tdql.tdQlKhThang  else 0 end) tdQlGdCnKyTyLe, "
				+ "nvl(tdql.tdQlDoiSoat4aValue,0) tdQlDoiSoat4aValue,"
				+ "(case when nvl(tdql.tdQlKhThang,0)>0 then nvl(tdql.tdQlDoiSoat4aValue,0)/tdql.tdQlKhThang  else 0 end) tdQlDoiSoat4aTyLe,"
				+ "nvl(tdql.tdQlPhtThamDuyetValue,0) tdQlPhtThamDuyetValue,"
				+ "(case when nvl(tdql.tdQlKhThang,0)>0 then nvl(tdql.tdQlPhtThamDuyetValue,0)/tdql.tdQlKhThang  else 0 end) tdQlPhtThamDuyetTyLe,"
				+ "nvl(tdql.tdQlPhtNghiemThuValue,0) tdQlPhtNghiemThuValue,"
				+ "(case when nvl(tdql.tdQlKhThang,0)>0 then nvl(tdql.tdQlPhtNghiemThuValue,0)/tdql.tdQlKhThang  else 0 end) tdQlPhtNghiemThuTyLe, "
				+ "nvl(tdql.tdQlDangLamHoSoValue,0) tdQlDangLamHoSoValue,"
				+ "(case when nvl(tdql.tdQlKhThang,0)>0 then nvl(tdql.tdQlDangLamHoSoValue,0)/tdql.tdQlKhThang  else 0 end) tdQlDangLamHoSoTyLe, "
				+ "nvl(tdql.tdQlDuKienHoanThanhValue,0) tdQlDuKienHoanThanhValue,"
				+ "(case when nvl(tdql.tdQlKhThang,0)>0 then nvl(tdql.tdQlDuKienHoanThanhValue,0)/tdql.tdQlKhThang  else 0 end) tdQlDuKienHoanThanhTyLe,"
				+ "tdql.tdQlNguyenNhanKoHt,"

				+ "nvl(tdHshcCp.tdHshcCpKhThang,0) tdHshcCpKhThang,"
				+ "nvl(tdHshcCp.tdHshcCpTrenDuongValue,0) tdHshcCpTrenDuongValue,"
				+ "(case when nvl(tdHshcCp.tdHshcCpKhThang,0)>0 then nvl(tdHshcCp.tdHshcCpTrenDuongValue,0)/tdHshcCp.tdHshcCpKhThang  else 0 end) tdHshcCpTrenDuongTyLe,"
				+ "nvl(tdHshcCp.tdHshcCpGdCnKyValue,0) tdHshcCpGdCnKyValue,"
				+ "(case when nvl(tdHshcCp.tdHshcCpKhThang,0)>0 then nvl(tdHshcCp.tdHshcCpGdCnKyValue,0)/tdHshcCp.tdHshcCpKhThang  else 0 end) tdHshcCpGdCnKyTyLe,"
				+ "nvl(tdHshcCp.tdHshcCpDoiSoat4aValue,0) tdHshcCpDoiSoat4aValue, "
				+ "(case when nvl(tdHshcCp.tdHshcCpKhThang,0)>0 then nvl(tdHshcCp.tdHshcCpDoiSoat4aValue,0)/tdHshcCp.tdHshcCpKhThang  else 0 end) tdHshcCpDoiSoat4aTyLe,"
				+ "nvl(tdHshcCp.tdHshcCpPhtThamDuyetValue,0) tdHshcCpPhtThamDuyetValue, "
				+ "(case when nvl(tdHshcCp.tdHshcCpKhThang,0)>0 then nvl(tdHshcCp.tdHshcCpPhtThamDuyetValue,0)/tdHshcCp.tdHshcCpKhThang  else 0 end) tdHshcCpPhtThamDuyetTyLe, "
				+ "nvl(tdHshcCp.tdHshcCpPhtNghiemThuValue,0) tdHshcCpPhtNghiemThuValue,"
				+ "(case when nvl(tdHshcCp.tdHshcCpKhThang,0)>0 then nvl(tdHshcCp.tdHshcCpPhtNghiemThuValue,0)/tdHshcCp.tdHshcCpKhThang  else 0 end) tdHshcCpPhtNghiemThuTyLe,"
				+ "nvl(tdHshcCp.tdHshcCpDangLamHoSoValue,0) tdHshcCpDangLamHoSoValue, "
				+ "(case when nvl(tdHshcCp.tdHshcCpKhThang,0)>0 then nvl(tdHshcCp.tdHshcCpDangLamHoSoValue,0)/tdHshcCp.tdHshcCpKhThang  else 0 end) tdHshcCpDangLamHoSoTyLe,"
				+ "nvl(tdHshcCp.tdHshcCpDuKienHoanThanhValue,0) tdHshcCpDuKienHoanThanhValue,"
				+ "(case when nvl(tdHshcCp.tdHshcCpKhThang,0)>0 then nvl(tdHshcCp.tdHshcCpDuKienHoanThanhValue,0)/tdHshcCp.tdHshcCpKhThang  else 0 end) tdHshcCpDuKienHoanThanhTyLe,"
				+ "tdHshcCp.tdHshcCpNguyenNhanKoHt, "

				+ "nvl(tdHshcXl.tdHshcXlKhThang,0) tdHshcXlKhThang, "
				+ "nvl(tdHshcXl.tdHshcXlTrenDuongValue,0) tdHshcXlTrenDuongValue, "
				+ "(case when nvl(tdHshcXl.tdHshcXlKhThang,0)>0 then nvl(tdHshcXl.tdHshcXlTrenDuongValue,0)/tdHshcXl.tdHshcXlKhThang  else 0 end) tdHshcXlTrenDuongTyLe, "
				+ "nvl(tdHshcXl.tdHshcXlGdCnKyValue,0) tdHshcXlGdCnKyValue,"
				+ "(case when nvl(tdHshcXl.tdHshcXlKhThang,0)>0 then nvl(tdHshcXl.tdHshcXlGdCnKyValue,0)/tdHshcXl.tdHshcXlKhThang  else 0 end) tdHshcXlGdCnKyTyLe, "
				+ "nvl(tdHshcXl.tdHshcXlDoiSoat4aValue,0) tdHshcXlDoiSoat4aValue,"
				+ "(case when nvl(tdHshcXl.tdHshcXlKhThang,0)>0 then nvl(tdHshcXl.tdHshcXlDoiSoat4aValue,0)/tdHshcXl.tdHshcXlKhThang  else 0 end) tdHshcXlDoiSoat4aTyLe, "
				+ "nvl(tdHshcXl.tdHshcXlPhtThamDuyetValue,0) tdHshcXlPhtThamDuyetValue,"
				+ "(case when nvl(tdHshcXl.tdHshcXlKhThang,0)>0 then nvl(tdHshcXl.tdHshcXlPhtThamDuyetValue,0)/tdHshcXl.tdHshcXlKhThang  else 0 end) tdHshcXlPhtThamDuyetTyLe, "
				+ "nvl(tdHshcXl.tdHshcXlPhtNghiemThuValue,0) tdHshcXlPhtNghiemThuValue,"
				+ "(case when nvl(tdHshcXl.tdHshcXlKhThang,0)>0 then nvl(tdHshcXl.tdHshcXlPhtNghiemThuValue,0)/tdHshcXl.tdHshcXlKhThang  else 0 end) tdHshcXlPhtNghiemThuTyLe, "
				+ "nvl(tdHshcXl.tdHshcXlDangLamHoSoValue,0) tdHshcXlDangLamHoSoValue,"
				+ "(case when nvl(tdHshcXl.tdHshcXlKhThang,0)>0 then nvl(tdHshcXl.tdHshcXlDangLamHoSoValue,0)/tdHshcXl.tdHshcXlKhThang  else 0 end) tdHshcXlDangLamHoSoTyLe, "
				+ "nvl(tdHshcXl.tdHshcXlDuKienHoanThanhValue,0) tdHshcXlDuKienHoanThanhValue, "
				+ "(case when nvl(tdHshcXl.tdHshcXlKhThang,0)>0 then nvl(tdHshcXl.tdHshcXlDuKienHoanThanhValue,0)/tdHshcXl.tdHshcXlKhThang  else 0 end) tdHshcXlDuKienHoanThanhTyLe, "
				+ "tdHshcXl.tdHshcXlNguyenNhanKoHt,"

				+ "nvl(tdHshcNtd.tdHshcNtdKhThang,0) tdHshcNtdKhThang, "
				+ "nvl(tdHshcNtd.tdHshcNtdTrenDuongValue,0) tdHshcNtdTrenDuongValue, "
				+ "(case when nvl(tdHshcNtd.tdHshcNtdKhThang,0)>0 then nvl(tdHshcNtd.tdHshcNtdTrenDuongValue,0)/tdHshcNtd.tdHshcNtdKhThang  else 0 end) tdHshcNtdTrenDuongTyLe, "
				+ "nvl(tdHshcNtd.tdHshcNtdGdCnKyValue,0) tdHshcNtdGdCnKyValue,"
				+ "(case when nvl(tdHshcNtd.tdHshcNtdKhThang,0)>0 then nvl(tdHshcNtd.tdHshcNtdGdCnKyValue,0)/tdHshcNtd.tdHshcNtdKhThang  else 0 end) tdHshcNtdGdCnKyTyLe, "
				+ "nvl(tdHshcNtd.tdHshcNtdDoiSoat4aValue,0) tdHshcNtdDoiSoat4aValue,"
				+ "(case when nvl(tdHshcNtd.tdHshcNtdKhThang,0)>0 then nvl(tdHshcNtd.tdHshcNtdDoiSoat4aValue,0)/tdHshcNtd.tdHshcNtdKhThang  else 0 end) tdHshcNtdDoiSoat4aTyLe, "
				+ "nvl(tdHshcNtd.tdHshcNtdPhtThamDuyetValue,0) tdHshcNtdPhtThamDuyetValue,"
				+ "(case when nvl(tdHshcNtd.tdHshcNtdKhThang,0)>0 then nvl(tdHshcNtd.tdHshcNtdPhtThamDuyetValue,0)/tdHshcNtd.tdHshcNtdKhThang  else 0 end) tdHshcNtdPhtThamDuyetTyLe, "
				+ "nvl(tdHshcNtd.tdHshcNtdPhtNghiemThuValue,0) tdHshcNtdPhtNghiemThuValue,"
				+ "(case when nvl(tdHshcNtd.tdHshcNtdKhThang,0)>0 then nvl(tdHshcNtd.tdHshcNtdPhtNghiemThuValue,0)/tdHshcNtd.tdHshcNtdKhThang  else 0 end) tdHshcNtdPhtNghiemThuTyLe, "
				+ "nvl(tdHshcNtd.tdHshcNtdDangLamHoSoValue,0) tdHshcNtdDangLamHoSoValue,"
				+ "(case when nvl(tdHshcNtd.tdHshcNtdKhThang,0)>0 then nvl(tdHshcNtd.tdHshcNtdDangLamHoSoValue,0)/tdHshcNtd.tdHshcNtdKhThang  else 0 end) tdHshcNtdDangLamHoSoTyLe, "
				+ "nvl(tdHshcNtd.tdHshcNtdDuKienHoanThanhValue,0) tdHshcNtdDuKienHoanThanhValue,"
				+ "(case when nvl(tdHshcNtd.tdHshcNtdKhThang,0)>0 then nvl(tdHshcNtd.tdHshcNtdDuKienHoanThanhValue,0)/tdHshcNtd.tdHshcNtdKhThang  else 0 end) tdHshcNtdDuKienHoanThanhTyLe,"
				+ "tdHshcNtd.tdHshcNtdNguyenNhanKoHt,"

				+ "nvl(tdSlCp.tdSlCpKhThang,0) tdSlCpKhThang, nvl(tdSlCp.tdSlCpDaHoanThanh,0) tdSlCpDaHoanThanh, "
				+ "nvl(tdSlCp.tdSlCpDangThiCongValue,0) tdSlCpDangThiCongValue,"
				+ "(case when nvl(tdSlCp.tdSlCpKhThang,0)>0 then nvl(tdSlCp.tdSlCpDangThiCongValue,0)/tdSlCp.tdSlCpKhThang  else 0 end) tdSlCpDangThiCongTyLe, "
				+ "nvl(tdSlCp.tdSlCpDuKienHoanThanhValue,0) tdSlCpDuKienHoanThanhValue,"
				+ "(case when nvl(tdSlCp.tdSlCpKhThang,0)>0 then nvl(tdSlCp.tdSlCpDuKienHoanThanhValue,0)/tdSlCp.tdSlCpKhThang  else 0 end) tdSlCpDuKienHoanThanhTyLe,"
				+ "tdSlCp.tdSlCpNguyenNhanKoHt,"

				+ "nvl(tdSlXl.tdSlXlKhThang,0) tdSlXlKhThang, nvl(tdSlXl.tdSlXlDaHoanThanh,0) tdSlXlDaHoanThanh,"
				+ "nvl(tdSlXl.tdSlXlDangThiCongValue,0) tdSlXlDangThiCongValue, "
				+ "(case when nvl(tdSlXl.tdSlXlKhThang,0)>0 then nvl(tdSlXl.tdSlXlDangThiCongValue,0)/tdSlXl.tdSlXlKhThang  else 0 end) tdSlXlDangThiCongTyLe, "
				+ "nvl(tdSlXl.tdSlXlDuKienHoanThanhValue,0) tdSlXlDuKienHoanThanhValue,"
				+ "(case when nvl(tdSlXl.tdSlXlKhThang,0)>0 then nvl(tdSlXl.tdSlXlDuKienHoanThanhValue,0)/tdSlXl.tdSlXlKhThang  else 0 end) tdSlXlDuKienHoanThanhTyLe,"
				+ "tdSlXl.tdSlXlNguyenNhanKoHt,"

				+ "nvl(tdSlNtd.tdSlNtdKhThang,0) tdSlNtdKhThang, nvl(tdSlNtd.tdSlNtdDaHoanThanh,0) tdSlNtdDaHoanThanh, "
				+ "nvl(tdSlNtd.tdSlNtdDangThiCongValue,0) tdSlNtdDangThiCongValue, "
				+ "(case when nvl(tdSlNtd.tdSlNtdKhThang,0)>0 then nvl(tdSlNtd.tdSlNtdDangThiCongValue,0)/tdSlNtd.tdSlNtdKhThang  else 0 end) tdSlNtdDangThiCongTyLe, "
				+ "nvl(tdSlNtd.tdSlNtdDuKienHoanThanhValue,0) tdSlNtdDuKienHoanThanhValue,"
				+ "(case when nvl(tdSlNtd.tdSlNtdKhThang,0)>0 then nvl(tdSlNtd.tdSlNtdDuKienHoanThanhValue,0)/tdSlNtd.tdSlNtdKhThang  else 0 end) tdSlNtdDuKienHoanThanhTyLe,"
				+ "tdSlNtd.tdSlNtdNguyenNhanKoHt,"

				+ "nvl(tdSlXddd.tdSlXdddKhThang,0) tdSlXdddKhThang, nvl(tdSlXddd.tdSlXdddDaHoanThanh,0) tdSlXdddDaHoanThanh, "
				+ "nvl(tdSlXddd.tdSlXdddDangThiCongValue,0) tdSlXdddDangThiCongValue, "
				+ "(case when nvl(tdSlXddd.tdSlXdddKhThang,0)>0 then (nvl(tdSlXddd.tdSlXdddDangThiCongValue,0) / tdSlXddd.tdSlXdddKhThang ) else 0 end) tdSlXdddDangThiCongTyLe,"
				+ "nvl(tdSlXddd.tdSlXdddDuKienHoanThanhValue,0) tdSlXdddDuKienHoanThanhValue, "
				+ "(case when nvl(tdSlXddd.tdSlXdddKhThang,0)>0 then (nvl(tdSlXddd.tdSlXdddDuKienHoanThanhValue,0) / tdSlXddd.tdSlXdddKhThang ) else 0 end) tdSlXdddDuKienHoanThanhTyLe, "
				+ "null tdSlXdddNguyenNhanKoHt, "
				
				+ "nvl(tdThdt.tdThdtKhThang,0) tdThdtKhThang, nvl(tdThdt.tdThdtDaHoanThanh,0) tdThdtDaHoanThanh,"
				+ "nvl(tdThdt.tdThdtPhtDangKiemTraValue,0) tdThdtPhtDangKiemTraValue,"
				+ "(case when nvl(tdThdt.tdThdtKhThang,0)>0 then nvl(tdThdt.tdThdtPhtDangKiemTraValue,0)/tdThdt.tdThdtKhThang  else 0 end) tdThdtPhtDangKiemTraTyLe,"
				+ "nvl(tdThdt.tdThdtPtcDangKiemTraValue,0) tdThdtPtcDangKiemTraValue,"
				+ "(case when nvl(tdThdt.tdThdtKhThang,0)>0 then nvl(tdThdt.tdThdtPtcDangKiemTraValue,0)/tdThdt.tdThdtKhThang  else 0 end) tdThdtPtcDangKiemTraTyLe, "
				+ "nvl(tdThdt.tdThdtDuKienHoanThanhValue,0) tdThdtDuKienHoanThanhValue,"
				+ "(case when nvl(tdThdt.tdThdtKhThang,0)>0 then nvl(tdThdt.tdThdtDuKienHoanThanhValue,0)/tdThdt.tdThdtKhThang  else 0 end) tdThdtDuKienHoanThanhTyLe,"
				+ "tdThdt.tdThdtNguyenNhanKoHt, " 
				
				+ "nvl(tdHtctXdm.tdHtctXdmKhThang,0) tdHtctXdmKhThang, nvl(tdHtctXdm.tdHtctXdmDaHt,0) tdHtctXdmDaHt, nvl(tdHtctXdm.tdHtctXdmDangTc,0) tdHtctXdmDangTc,"
				+ "nvl(tdHtctXdm.tdHtctXdmDuKienHt,0) tdHtctXdmDuKienHt, "
				+ "(case when nvl(tdHtctXdm.tdHtctXdmKhThang,0) > 0 then (nvl(tdHtctXdm.tdHtctXdmDuKienHt,0) / tdHtctXdm.tdHtctXdmKhThang ) else 0 end) tdHtctXdmTyLe,"
				+ "nvl(tdHtctHt.tdHtctHtKhThang,0) tdHtctHtKhThang, "
				+ "nvl(tdHtctHt.tdHtctHtDaHt,0) tdHtctHtDaHt, "
				+ "nvl(tdHtctHt.tdHtctHtDangTc,0) tdHtctHtDangTc,"
				+ "nvl(tdHtctHt.tdHtctHtDuKienHt,0) tdHtctHtDuKienHt, "
				+ "(case when nvl(tdHtctHt.tdHtctHtKhThang,0) > 0 then (nvl(tdHtctHt.tdHtctHtDuKienHt,0) / tdHtctHt.tdHtctHtKhThang ) else 0 end) tdHtctHtTyLe, null tdHtctNguyenNhanKoHt, "
				
				+ "nvl(tdHshcHtct.tdHshcHtctKhThang,0) tdHshcHtctKhThang, "
				+ "nvl(tdHshcHtct.tdHshcHtctTrenDuongValue,0) tdHshcHtctTrenDuongValue,"
				+ "(case when nvl(tdHshcHtct.tdHshcHtctKhThang,0) > 0 then (nvl(tdHshcHtct.tdHshcHtctTrenDuongValue,0) / tdHshcHtct.tdHshcHtctKhThang ) else 0 end) tdHshcHtctTrenDuongTyLe, "
				+ "nvl(tdHshcHtct.tdHshcHtctGdCnKyValue,0) tdHshcHtctGdCnKyValue,"
				+ "(case when nvl(tdHshcHtct.tdHshcHtctKhThang,0) > 0 then (nvl(tdHshcHtct.tdHshcHtctGdCnKyValue,0) / tdHshcHtct.tdHshcHtctKhThang ) else 0 end) tdHshcHtctGdCnKyTyLe,"
				+ "nvl(tdHshcHtct.tdHshcHtctDoiSoat4aValue,0) tdHshcHtctDoiSoat4aValue,"
				+ "(case when nvl(tdHshcHtct.tdHshcHtctKhThang,0) > 0 then (nvl(tdHshcHtct.tdHshcHtctDoiSoat4aValue,0) / tdHshcHtct.tdHshcHtctKhThang ) else 0 end) tdHshcHtctDoiSoat4aTyLe,"
				+ "nvl(tdHshcHtct.tdHshcHtctPhtThamDuyetValue,0) tdHshcHtctPhtThamDuyetValue,"
				+ "(case when nvl(tdHshcHtct.tdHshcHtctKhThang,0) > 0 then (nvl(tdHshcHtct.tdHshcHtctPhtThamDuyetValue,0) / tdHshcHtct.tdHshcHtctKhThang ) else 0 end) tdHshcHtctPhtThamDuyetTyLe,"
				+ "nvl(tdHshcHtct.tdHshcHtctPhtNghiemThuValue,0) tdHshcHtctPhtNghiemThuValue,"
				+ "(case when nvl(tdHshcHtct.tdHshcHtctKhThang,0) > 0 then (nvl(tdHshcHtct.tdHshcHtctPhtNghiemThuValue,0) / tdHshcHtct.tdHshcHtctKhThang ) else 0 end) tdHshcHtctPhtNghiemThuTyLe,"
				+ "nvl(tdHshcHtct.tdHshcHtctTtktHoSoValue,0) tdHshcHtctTtktHoSoValue,"
				+ "(case when nvl(tdHshcHtct.tdHshcHtctKhThang,0) > 0 then (nvl(tdHshcHtct.tdHshcHtctTtktHoSoValue,0) / tdHshcHtct.tdHshcHtctKhThang ) else 0 end) tdHshcHtctTtktHoSoTyLe,"
				+ "nvl(tdHshcHtct.tdHshcHtctDuKienHtValue,0) tdHshcHtctDuKienHtValue,"
				+ "(case when nvl(tdHshcHtct.tdHshcHtctKhThang,0) > 0 then (nvl(tdHshcHtct.tdHshcHtctDuKienHtValue,0) / tdHshcHtct.tdHshcHtctKhThang ) else 0 end) tdHshcHtctDuKienHtTyLe, null tdHshcHtctNguyenNhanKoHt, "
				
				+ "(nvl(tdSlCp.tdSlCpKhThang,0) + nvl(tdSlXl.tdSlXlKhThang,0) + nvl(tdSlNtd.tdSlNtdKhThang,0)) tdTongKhThang, " 
				+ "(nvl(tdSlCp.tdSlCpDaHoanThanh,0) + nvl(tdSlXl.tdSlXlDaHoanThanh,0) + nvl(tdSlNtd.tdSlNtdDaHoanThanh,0)) tdTongDaHt, "
				+ "(nvl(tdSlCp.tdSlCpDangThiCongValue,0) + nvl(tdSlXl.tdSlXlDangThiCongValue,0) + nvl(tdSlNtd.tdSlNtdDangThiCongValue,0)) tdTongDangTcValue,"
				+ "case when (nvl(tdSlCp.tdSlCpKhThang,0) + nvl(tdSlXl.tdSlXlKhThang,0) + nvl(tdSlNtd.tdSlNtdKhThang,0))>0 then ((nvl(tdSlCp.tdSlCpDangThiCongValue,0) + nvl(tdSlXl.tdSlXlDangThiCongValue,0) + nvl(tdSlNtd.tdSlNtdDangThiCongValue,0))/(nvl(tdSlCp.tdSlCpKhThang,0) + nvl(tdSlXl.tdSlXlKhThang,0) + nvl(tdSlNtd.tdSlNtdKhThang,0))) else 0 end tdTongDangTcTyLe,"
				+ "(nvl(tdSlCp.tdSlCpDuKienHoanThanhValue,0) + nvl(tdSlXl.tdSlXlDuKienHoanThanhValue,0) + nvl(tdSlNtd.tdSlNtdDuKienHoanThanhValue,0)) tdTongDuKienHtValue,"
				+ "case when (nvl(tdSlCp.tdSlCpKhThang,0) + nvl(tdSlXl.tdSlXlKhThang,0) + nvl(tdSlNtd.tdSlNtdKhThang,0))>0 then ((nvl(tdSlCp.tdSlCpDuKienHoanThanhValue,0) + nvl(tdSlXl.tdSlXlDuKienHoanThanhValue,0) + nvl(tdSlNtd.tdSlNtdDuKienHoanThanhValue,0))/(nvl(tdSlCp.tdSlCpKhThang,0) + nvl(tdSlXl.tdSlXlKhThang,0) + nvl(tdSlNtd.tdSlNtdKhThang,0))) else 0 end tdTongDuKienHtTyLe,"
				+ "null tdTongDeXuatVuongMac "
				
				+ ", nvl(tdHshcXddd.tdHshcXdddKhThang,0) tdHshcXdddKhThang, "
				+ " nvl(tdHshcXddd.tdHshcXdddTrenDuongValue,0) tdHshcXdddTrenDuongValue,"
				+ " (case when nvl(tdHshcXddd.tdHshcXdddKhThang,0) > 0 then (nvl(tdHshcXddd.tdHshcXdddTrenDuongValue,0) / tdHshcXddd.tdHshcXdddKhThang ) else 0 end) tdHshcXdddTrenDuongTyLe,"
				+ " nvl(tdHshcXddd.tdHshcXdddGdCnKyValue,0) tdHshcXdddGdCnKyValue,"
				+ " (case when nvl(tdHshcXddd.tdHshcXdddKhThang,0) > 0 then (nvl(tdHshcXddd.tdHshcXdddGdCnKyValue,0) / tdHshcXddd.tdHshcXdddKhThang ) else 0 end) tdHshcXdddGdCnKyTyLe,"
				+ " nvl(tdHshcXddd.tdHshcXdddDoiSoat4aValue,0) tdHshcXdddDoiSoat4aValue,"
				+ " (case when nvl(tdHshcXddd.tdHshcXdddKhThang,0) > 0 then (nvl(tdHshcXddd.tdHshcXdddDoiSoat4aValue,0) / tdHshcXddd.tdHshcXdddKhThang ) else 0 end) tdHshcXdddDoiSoat4aTyLe,"
				+ " nvl(tdHshcXddd.tdHshcXdddPhtThamDuyetValue,0) tdHshcXdddPhtThamDuyetValue,"
				+ " (case when nvl(tdHshcXddd.tdHshcXdddKhThang,0) > 0 then (nvl(tdHshcXddd.tdHshcXdddPhtThamDuyetValue,0) / tdHshcXddd.tdHshcXdddKhThang ) else 0 end) tdHshcXdddPhtThamDuyetTyLe,"
				+ " nvl(tdHshcXddd.tdHshcXdddPhtNghiemThuValue,0) tdHshcXdddPhtNghiemThuValue,"
				+ " (case when nvl(tdHshcXddd.tdHshcXdddKhThang,0) > 0 then (nvl(tdHshcXddd.tdHshcXdddPhtNghiemThuValue,0) / tdHshcXddd.tdHshcXdddKhThang ) else 0 end) tdHshcXdddPhtNghiemThuTyLe,"
				+ " nvl(tdHshcXddd.tdHshcXdddDangLamHoSoValue,0) tdHshcXdddDangLamHoSoValue,"
				+ " (case when nvl(tdHshcXddd.tdHshcXdddKhThang,0) > 0 then (nvl(tdHshcXddd.tdHshcXdddDangLamHoSoValue,0) / tdHshcXddd.tdHshcXdddKhThang ) else 0 end) tdHshcXdddDangLamHoSoTyLe,"
				+ " nvl(tdHshcXddd.tdHshcXdddDuKienHoanThanhValue,0) tdHshcXdddDuKienHoanThanhValue,"
				+ " (case when nvl(tdHshcXddd.tdHshcXdddKhThang,0) > 0 then (nvl(tdHshcXddd.tdHshcXdddDuKienHoanThanhValue,0) / tdHshcXddd.tdHshcXdddKhThang ) else 0 end) tdHshcXdddDuKienHoanThanhTyLe,"
				+ " null tdHshcXdddNguyenNhanKoHt "
				
				+ ", nvl(tdSlHtct.tdSlHtctKhThang,0) tdSlHtctKhThang, nvl(tdSlHtct.tdSlHtctDaHt,0) tdSlHtctDaHt, nvl(tdSlHtct.tdSlHtctDangTcValue,0) tdSlHtctDangTcValue,"
				+ " (case when nvl(tdSlHtct.tdSlHtctKhThang,0) > 0 then (nvl(tdSlHtct.tdSlHtctDangTcValue,0) / tdSlHtct.tdSlHtctKhThang ) else 0 end) tdSlHtctDangTcTyLe,"
				+ " nvl(tdSlHtct.tdSlHtctDuKienHtValue,0) tdSlHtctDuKienHtValue,"
				+ " (case when nvl(tdSlHtct.tdSlHtctKhThang,0) > 0 then (nvl(tdSlHtct.tdSlHtctDuKienHtValue,0) / tdSlHtct.tdSlHtctKhThang ) else 0 end) tdSlHtctDuKienHtTyLe,"
				+ " null tdSlHtctNguyenNhanKoHt "
				
				+ " from tdql " 
				+ " full join tdHshcCp on tdql.ttkt = tdHshcCp.ttkt "
				+ " full join tdHshcXl on tdql.ttkt = tdHshcXl.ttkt "
				+ " full join tdHshcNtd on tdql.ttkt = tdHshcNtd.ttkt "
				+ " full join tdSlCp on tdql.ttkt = tdSlCp.ttkt " 
				+ " full join tdSlXl on tdql.ttkt = tdSlXl.ttkt "
				+ " full join tdSlNtd on tdql.ttkt = tdSlNtd.ttkt " 
				+ " full join tdSlXddd on tdql.ttkt = tdSlXddd.ttkt "
				+ " full join tdThDt on tdql.ttkt = tdThDt.ttkt "
				+ " full join tdHtctXdm on tdql.ttkt = tdHtctXdm.ttkt " 
				+ " full join tdHtctHt on tdql.ttkt =  tdHtctHt.ttkt "
				+ " full join tdHshcHtct on tdql.ttkt =  tdHshcHtct.ttkt "
				+ " full join tdHshcXddd on tdql.ttkt =  tdHshcXddd.ttkt "
				+ " full join tdSlHtct on tdql.ttkt =  tdSlHtct.ttkt "
//				+ " ORDER BY ttkv, ttkt ASC "
				);
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("ttkv", new StringType());
		query.addScalar("ttkt", new StringType());
		query.addScalar("tdQlKhThang", new LongType());
//		query.addScalar("tdQlTong", new LongType());
//		query.addScalar("tdQlTthtDaDuyet", new LongType());
		query.addScalar("tdQlTrenDuongValue", new LongType());
		query.addScalar("tdQlTrenDuongTyle", new DoubleType());
		query.addScalar("tdQlGdCnKyValue", new LongType());
		query.addScalar("tdQlGdCnKyTyLe", new DoubleType());
		query.addScalar("tdQlDoiSoat4aValue", new LongType());
		query.addScalar("tdQlDoiSoat4aTyLe", new DoubleType());
		query.addScalar("tdQlPhtThamDuyetValue", new LongType());
		query.addScalar("tdQlPhtThamDuyetTyLe", new DoubleType());
		query.addScalar("tdQlPhtNghiemThuValue", new LongType());
		query.addScalar("tdQlPhtNghiemThuTyLe", new DoubleType());
		query.addScalar("tdQlDangLamHoSoValue", new LongType());
		query.addScalar("tdQlDangLamHoSoTyLe", new DoubleType());
		query.addScalar("tdQlDuKienHoanThanhValue", new LongType());
		query.addScalar("tdQlDuKienHoanThanhTyLe", new DoubleType());
		query.addScalar("tdQlNguyenNhanKoHt", new StringType());

		query.addScalar("tdHshcCpKhThang", new LongType());
//		query.addScalar("tdHshcCpTong", new LongType());
//		query.addScalar("tdHshcCpTthtDaDuyet", new LongType());
		query.addScalar("tdHshcCpTrenDuongValue", new LongType());
		query.addScalar("tdHshcCpTrenDuongTyLe", new DoubleType());
		query.addScalar("tdHshcCpGdCnKyValue", new LongType());
		query.addScalar("tdHshcCpGdCnKyTyLe", new DoubleType());
		query.addScalar("tdHshcCpDoiSoat4aValue", new LongType());
		query.addScalar("tdHshcCpDoiSoat4aTyLe", new DoubleType());
		query.addScalar("tdHshcCpPhtThamDuyetValue", new LongType());
		query.addScalar("tdHshcCpPhtThamDuyetTyLe", new DoubleType());
		query.addScalar("tdHshcCpPhtNghiemThuValue", new LongType());
		query.addScalar("tdHshcCpPhtNghiemThuTyLe", new DoubleType());
		query.addScalar("tdHshcCpDangLamHoSoValue", new LongType());
		query.addScalar("tdHshcCpDangLamHoSoTyLe", new DoubleType());
		query.addScalar("tdHshcCpDuKienHoanThanhValue", new LongType());
		query.addScalar("tdHshcCpDuKienHoanThanhTyLe", new DoubleType());
		query.addScalar("tdHshcCpNguyenNhanKoHt", new StringType());

		query.addScalar("tdHshcXlKhThang", new LongType());
//		query.addScalar("tdHshcXlTong", new LongType());
//		query.addScalar("tdHshcXlTthtDaDuyet", new LongType());
		query.addScalar("tdHshcXlTrenDuongValue", new LongType());
		query.addScalar("tdHshcXlTrenDuongTyLe", new DoubleType());
		query.addScalar("tdHshcXlGdCnKyValue", new LongType());
		query.addScalar("tdHshcXlGdCnKyTyLe", new DoubleType());
		query.addScalar("tdHshcXlDoiSoat4aValue", new LongType());
		query.addScalar("tdHshcXlDoiSoat4aTyLe", new DoubleType());
		query.addScalar("tdHshcXlPhtThamDuyetValue", new LongType());
		query.addScalar("tdHshcXlPhtThamDuyetTyLe", new DoubleType());
		query.addScalar("tdHshcXlPhtNghiemThuValue", new LongType());
		query.addScalar("tdHshcXlPhtNghiemThuTyLe", new DoubleType());
		query.addScalar("tdHshcXlDangLamHoSoValue", new LongType());
		query.addScalar("tdHshcXlDangLamHoSoTyLe", new DoubleType());
		query.addScalar("tdHshcXlDuKienHoanThanhValue", new LongType());
		query.addScalar("tdHshcXlDuKienHoanThanhTyLe", new DoubleType());
		query.addScalar("tdHshcXlNguyenNhanKoHt", new StringType());

		query.addScalar("tdHshcNtdKhThang", new LongType());
//		query.addScalar("tdHshcNtdTong", new LongType());
//		query.addScalar("tdHshcNtdPtkDaDuyet", new LongType());
		query.addScalar("tdHshcNtdTrenDuongValue", new LongType());
		query.addScalar("tdHshcNtdTrenDuongTyLe", new DoubleType());
		query.addScalar("tdHshcNtdGdCnKyValue", new LongType());
		query.addScalar("tdHshcNtdGdCnKyTyLe", new DoubleType());
		query.addScalar("tdHshcNtdDoiSoat4aValue", new LongType());
		query.addScalar("tdHshcNtdDoiSoat4aTyLe", new DoubleType());
		query.addScalar("tdHshcNtdPhtThamDuyetValue", new LongType());
		query.addScalar("tdHshcNtdPhtThamDuyetTyLe", new DoubleType());
		query.addScalar("tdHshcNtdPhtNghiemThuValue", new LongType());
		query.addScalar("tdHshcNtdPhtNghiemThuTyLe", new DoubleType());
		query.addScalar("tdHshcNtdDangLamHoSoValue", new LongType());
		query.addScalar("tdHshcNtdDangLamHoSoTyLe", new DoubleType());
		query.addScalar("tdHshcNtdDuKienHoanThanhValue", new LongType());
		query.addScalar("tdHshcNtdDuKienHoanThanhTyLe", new DoubleType());
		query.addScalar("tdHshcNtdNguyenNhanKoHt", new StringType());

		query.addScalar("tdSlCpKhThang", new LongType());
		query.addScalar("tdSlCpDaHoanThanh", new LongType());
		query.addScalar("tdSlCpDangThiCongValue", new LongType());
		query.addScalar("tdSlCpDangThiCongTyLe", new DoubleType());
		query.addScalar("tdSlCpDuKienHoanThanhValue", new LongType());
		query.addScalar("tdSlCpDuKienHoanThanhTyLe", new DoubleType());
		query.addScalar("tdSlCpNguyenNhanKoHt", new StringType());

		query.addScalar("tdSlXlKhThang", new LongType());
		query.addScalar("tdSlXlDaHoanThanh", new LongType());
		query.addScalar("tdSlXlDangThiCongValue", new LongType());
		query.addScalar("tdSlXlDangThiCongTyLe", new DoubleType());
		query.addScalar("tdSlXlDuKienHoanThanhValue", new LongType());
		query.addScalar("tdSlXlDuKienHoanThanhTyLe", new DoubleType());
		query.addScalar("tdSlXlNguyenNhanKoHt", new StringType());

		query.addScalar("tdSlNtdKhThang", new LongType());
		query.addScalar("tdSlNtdDaHoanThanh", new LongType());
		query.addScalar("tdSlNtdDangThiCongValue", new LongType());
		query.addScalar("tdSlNtdDangThiCongTyLe", new DoubleType());
		query.addScalar("tdSlNtdDuKienHoanThanhValue", new LongType());
		query.addScalar("tdSlNtdDuKienHoanThanhTyLe", new DoubleType());
		query.addScalar("tdSlNtdNguyenNhanKoHt", new StringType());

		query.addScalar("tdSlXdddKhThang", new LongType());
		query.addScalar("tdSlXdddDaHoanThanh", new LongType());
		query.addScalar("tdSlXdddDangThiCongValue", new LongType());
		query.addScalar("tdSlXdddDangThiCongTyLe", new DoubleType());
		query.addScalar("tdSlXdddDuKienHoanThanhValue", new LongType());
		query.addScalar("tdSlXdddDuKienHoanThanhTyLe", new DoubleType());
		query.addScalar("tdSlXdddNguyenNhanKoHt", new StringType());
		
		query.addScalar("tdThdtKhThang", new LongType());
		query.addScalar("tdThdtDaHoanThanh", new LongType());
		query.addScalar("tdThdtPhtDangKiemTraValue", new LongType());
		query.addScalar("tdThdtPhtDangKiemTraTyLe", new DoubleType());
		query.addScalar("tdThdtPtcDangKiemTraValue", new LongType());
		query.addScalar("tdThdtPtcDangKiemTraTyLe", new DoubleType());
		query.addScalar("tdThdtDuKienHoanThanhValue", new LongType());
		query.addScalar("tdThdtDuKienHoanThanhTyLe", new DoubleType());
		query.addScalar("tdThdtNguyenNhanKoHt", new StringType());

		query.addScalar("tdHtctXdmKhThang", new LongType());
		query.addScalar("tdHtctXdmDaHt", new LongType());
		query.addScalar("tdHtctXdmDangTc", new LongType());
		query.addScalar("tdHtctXdmDuKienHt", new LongType());
		query.addScalar("tdHtctXdmTyLe", new DoubleType());
		query.addScalar("tdHtctHtKhThang", new LongType());
		query.addScalar("tdHtctHtDaHt", new LongType());
		query.addScalar("tdHtctHtDangTc", new LongType());
		query.addScalar("tdHtctHtDuKienHt", new LongType());
		query.addScalar("tdHtctHtTyLe", new DoubleType());
		query.addScalar("tdHtctNguyenNhanKoHt", new StringType());
		
		query.addScalar("tdHshcHtctKhThang", new LongType());
		query.addScalar("tdHshcHtctTrenDuongValue", new LongType());
		query.addScalar("tdHshcHtctTrenDuongTyLe", new DoubleType());
		query.addScalar("tdHshcHtctGdCnKyValue", new LongType());
		query.addScalar("tdHshcHtctGdCnKyTyLe", new DoubleType());
		query.addScalar("tdHshcHtctDoiSoat4aValue", new LongType());
		query.addScalar("tdHshcHtctDoiSoat4aTyLe", new DoubleType());
		query.addScalar("tdHshcHtctPhtThamDuyetValue", new LongType());
		query.addScalar("tdHshcHtctPhtThamDuyetTyLe", new DoubleType());
		query.addScalar("tdHshcHtctPhtNghiemThuValue", new LongType());
		query.addScalar("tdHshcHtctPhtNghiemThuTyLe", new DoubleType());
		query.addScalar("tdHshcHtctTtktHoSoValue", new LongType());
		query.addScalar("tdHshcHtctTtktHoSoTyLe", new DoubleType());
		query.addScalar("tdHshcHtctDuKienHtValue", new LongType());
		query.addScalar("tdHshcHtctDuKienHtTyLe", new DoubleType());
		query.addScalar("tdHshcHtctNguyenNhanKoHt", new StringType());
		
		query.addScalar("tdTongKhThang", new LongType());
		query.addScalar("tdTongDaHt", new LongType());
		query.addScalar("tdTongDangTcValue", new LongType());
		query.addScalar("tdTongDangTcTyLe", new DoubleType());
		query.addScalar("tdTongDuKienHtValue", new LongType());
		query.addScalar("tdTongDuKienHtTyLe", new DoubleType());
		query.addScalar("tdTongDeXuatVuongMac", new StringType());
		
		query.addScalar("tdHshcXdddKhThang", new LongType());
		query.addScalar("tdHshcXdddTrenDuongValue", new LongType());
		query.addScalar("tdHshcXdddTrenDuongTyLe", new DoubleType());
		query.addScalar("tdHshcXdddGdCnKyValue", new LongType());
		query.addScalar("tdHshcXdddGdCnKyTyLe", new DoubleType());
		query.addScalar("tdHshcXdddDoiSoat4aValue", new LongType());
		query.addScalar("tdHshcXdddDoiSoat4aTyLe", new DoubleType());
		query.addScalar("tdHshcXdddPhtThamDuyetValue", new LongType());
		query.addScalar("tdHshcXdddPhtThamDuyetTyLe", new DoubleType());
		query.addScalar("tdHshcXdddPhtNghiemThuValue", new LongType());
		query.addScalar("tdHshcXdddPhtNghiemThuTyLe", new DoubleType());
		query.addScalar("tdHshcXdddDangLamHoSoValue", new LongType());
		query.addScalar("tdHshcXdddDangLamHoSoTyLe", new DoubleType());
		query.addScalar("tdHshcXdddDuKienHoanThanhValue", new LongType());
		query.addScalar("tdHshcXdddDuKienHoanThanhTyLe", new DoubleType());
		query.addScalar("tdHshcXdddNguyenNhanKoHt", new StringType());
		
		query.addScalar("tdSlHtctKhThang", new LongType());
		query.addScalar("tdSlHtctDaHt", new LongType());
		query.addScalar("tdSlHtctDangTcValue", new LongType());
		query.addScalar("tdSlHtctDangTcTyLe", new DoubleType());
		query.addScalar("tdSlHtctDuKienHtValue", new LongType());
		query.addScalar("tdSlHtctDuKienHtTyLe", new DoubleType());
		query.addScalar("tdSlHtctNguyenNhanKoHt", new LongType());
		
		if (StringUtils.isNoneBlank(obj.getTtkt())) {
			query.setParameter("ttkt", obj.getTtkt());
		}

		if (StringUtils.isNotBlank(obj.getMonthYear())) {
			query.setParameter("monthYear", obj.getMonthYear());
		}

		if (obj.getGroupIdList()!=null && !obj.getGroupIdList().isEmpty()) {
			query.setParameterList("groupIdList", obj.getGroupIdList());
		}
		query.setResultTransformer(Transformers.aliasToBean(ProgressTaskOsDTO.class));

		return query.list();
	}

	public void tdQLDuKien(StringBuilder sql, ProgressTaskOsDTO obj) {
		sql.append(" tdql as (select  " + "pt.TTKV ttkv, " + "pt.TTKT ttkt, "
				+ "nvl(MAX(taskOs.valueSalary),0) tdQlKhThang, "
				+ "nvl((select sum(pt1.SALARY_VALUE)  "
				+ "         from PROGRESS_TASK_OS pt1 where pt1.status!=0 and pt1.TDHS_EXPECTED_COMPLETE_DATE is not null and pt1.TTKT = pt.TTKT ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("         group by pt1.TTKT),0) tdQlDuKienHoanThanhValue, " 
				+ "null tdQlNguyenNhanKoHt "
				+ "from PROGRESS_TASK_OS pt " + "left join (select sum(task.SALARY_VALUE) valueSalary, task.TTKT ttkt  "
				+ "from PROGRESS_TASK_OS task where task.STATUS!=0 " );
		ifElseToanQuoc(sql,obj,"task");
		sql.append(" group by  task.TTKV,task.TTKT) taskOs "
				+ "on taskOs.ttkt = pt.TTKT " + "where 1=1 ");
	}
	
	public void tdSlXlDuKien(StringBuilder sql, ProgressTaskOsDTO obj) {
		sql.append(" ,tdSlXl as (select  " + "pt.TTKV ttkv, " + "pt.TTKT ttkt, "
				+ "nvl(MAX(taskOs.valueQuantity),0) tdSlXlKhThang, " 
				+ "nvl((select sum(pt1.QUANTITY_VALUE) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDSL_EXPECTED_COMPLETE_DATE is not null  " + "      and pt1.TTKT = pt.TTKT "
				+ "      and pt1.SOURCE_TASK=1 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdSlXlDuKienHoanThanhValue, "
				+ "null tdSlXlNguyenNhanKoHt " + "from PROGRESS_TASK_OS pt "
				+ "left join (select sum(task.QUANTITY_VALUE) valueQuantity, task.TTKT ttkt  "
				+ "from PROGRESS_TASK_OS task where task.STATUS!=0 and task.SOURCE_TASK=1 ");
		ifElseToanQuoc(sql,obj,"task");
		sql.append(" group by  task.TTKV,task.TTKT) taskOs " + "on taskOs.ttkt = pt.TTKT " + "where 1=1 ");
	}
	
	public void tdSlCpDuKien(StringBuilder sql, ProgressTaskOsDTO obj) {
		sql.append(" ,tdSlCp as (select  " + "pt.TTKV ttkv, " + "pt.TTKT ttkt, "
				+ "nvl(MAX(taskOs.valueQuantity),0) tdSlCpKhThang, " 
				+ "nvl((select sum(pt1.QUANTITY_VALUE) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDSL_EXPECTED_COMPLETE_DATE is not null  " + "      and pt1.TTKT = pt.TTKT "
				+ "      and pt1.SOURCE_TASK=2 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdSlCpDuKienHoanThanhValue, "
				+ "null tdSlCpNguyenNhanKoHt " + "from PROGRESS_TASK_OS pt "
				+ "left join (select sum(task.QUANTITY_VALUE) valueQuantity, task.TTKT ttkt  "
				+ "from PROGRESS_TASK_OS task where task.STATUS!=0 and task.SOURCE_TASK=2 ");
		ifElseToanQuoc(sql,obj,"task");
		sql.append(" group by  task.TTKV,task.TTKT) taskOs " + "on taskOs.ttkt = pt.TTKT " + "where 1=1 ");
	}
	
	public void tdSlNtdDuKien(StringBuilder sql, ProgressTaskOsDTO obj) {
		sql.append(" ,tdSlNtd as (select  " + "pt.TTKV ttkv, " + "pt.TTKT ttkt, "
			+ "nvl(MAX(taskOs.valueQuantity),0) tdSlNtdKhThang, " 
			+ "nvl((select sum(pt1.QUANTITY_VALUE) " + "			from PROGRESS_TASK_OS pt1  "
			+ "      where pt1.status!=0 and pt1.TDSL_EXPECTED_COMPLETE_DATE is not null  " + "      and pt1.TTKT = pt.TTKT "
			+ "      and pt1.SOURCE_TASK=3 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdSlNtdDuKienHoanThanhValue, "
			+ "null tdSlNtdNguyenNhanKoHt " + "from PROGRESS_TASK_OS pt "
			+ "left join (select sum(task.QUANTITY_VALUE) valueQuantity, task.TTKT ttkt  "
			+ "from PROGRESS_TASK_OS task where task.STATUS!=0 and task.SOURCE_TASK=3 ");
		ifElseToanQuoc(sql,obj,"task");
		sql.append(" group by  task.TTKV,task.TTKT) taskOs " + "on taskOs.ttkt = pt.TTKT " + "where 1=1 ");
	}
	
	public void tdSlXdddDuKien(StringBuilder sql, ProgressTaskOsDTO obj) {
		sql.append(" ,tdSlXddd as (select pt.TTKV ttkv, " + "pt.TTKT ttkt, "
			+ "nvl(MAX(taskOs.valueQuantity),0) tdSlXdddKhThang, " 
			+ "nvl((select sum(pt1.QUANTITY_VALUE) " + "			from PROGRESS_TASK_OS pt1  "
			+ "      where pt1.status!=0 and pt1.TDSL_EXPECTED_COMPLETE_DATE is not null  "
			+ "      and pt1.TTKT = pt.TTKT " + "      and pt1.SOURCE_TASK=6 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdSlXdddDuKienHoanThanhValue " 
			+ "from PROGRESS_TASK_OS pt "
			+ "left join (select " + "task.TTKT ttkt, " + "sum(task.QUANTITY_VALUE) valueQuantity "
			+ "from PROGRESS_TASK_OS task " + "where task.status!=0 and task.SOURCE_TASK = 6 ");
		ifElseToanQuoc(sql,obj,"task");
		sql.append(" group by "
			+ "task.TTKT) taskOs " + "on pt.ttkt = taskOs.ttkt where 1=1 ");
	}
	
	public void tdHshcXlDuKien(StringBuilder sql, ProgressTaskOsDTO obj) {
		sql.append(" ,tdHshcXl as (select  " + "pt.TTKV ttkv, " + "pt.TTKT ttkt, "
				+ "nvl(MAX(taskOs.valueHshc),0) tdHshcXlKhThang, "
				+ "nvl((select sum(pt1.HSHC_VALUE) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDHS_EXPECTED_COMPLETE_DATE is not null  "
				+ "      and pt1.TTKT = pt.TTKT " + "      and pt1.SOURCE_TASK=1 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcXlDuKienHoanThanhValue, " + "null tdHshcXlNguyenNhanKoHt "
				+ "from PROGRESS_TASK_OS pt " + "left join (select sum(task.HSHC_VALUE) valueHshc, task.TTKT ttkt  "
				+ "from PROGRESS_TASK_OS task where task.STATUS!=0 and task.SOURCE_TASK=1 ");
		ifElseToanQuoc(sql,obj,"task");
		sql.append(" group by  task.TTKV,task.TTKT) taskOs " + "on taskOs.ttkt = pt.TTKT " + "where 1=1 ");
	}
	
	public void tdHshcNtdDuKien(StringBuilder sql, ProgressTaskOsDTO obj) {
		sql.append(" ,tdHshcNtd as (select  " + "pt.TTKV ttkv, " + "pt.TTKT ttkt, "
			+ "nvl(MAX(taskOs.valueHshc),0) tdHshcNtdKhThang, "
			+ "nvl((select sum(pt1.HSHC_VALUE) " + "			from PROGRESS_TASK_OS pt1  "
			+ "      where pt1.status!=0 and pt1.TDHS_EXPECTED_COMPLETE_DATE is not null  "
			+ "      and pt1.TTKT = pt.TTKT " + "      and pt1.SOURCE_TASK=3 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcNtdDuKienHoanThanhValue, " + "null tdHshcNtdNguyenNhanKoHt "
			+ "from PROGRESS_TASK_OS pt " + "left join (select sum(task.HSHC_VALUE) valueHshc, task.TTKT ttkt  "
			+ "from PROGRESS_TASK_OS task where task.STATUS!=0 and task.SOURCE_TASK=3 ");
		ifElseToanQuoc(sql,obj,"task");
		sql.append(" group by  task.TTKV,task.TTKT) taskOs " + "on taskOs.ttkt = pt.TTKT " + "where 1=1 ");
	}
	
	public void tdHshcCpDuKien(StringBuilder sql, ProgressTaskOsDTO obj) {
		sql.append(" ,tdHshcCp as (select  " + "pt.TTKV ttkv, " + "pt.TTKT ttkt, "
			+ "nvl(MAX(taskOs.valueHshc),0) tdHshcCpKhThang, "
			+ "nvl((select sum(pt1.HSHC_VALUE) " + "			from PROGRESS_TASK_OS pt1  "
			+ "      where pt1.status!=0 and pt1.TDHS_EXPECTED_COMPLETE_DATE is not null  "
			+ "      and pt1.TTKT = pt.TTKT " + "      and pt1.SOURCE_TASK=2 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcCpDuKienHoanThanhValue, " 
			+ "null tdHshcCpNguyenNhanKoHt "
			+ "from PROGRESS_TASK_OS pt " + "left join (select sum(task.HSHC_VALUE) valueHshc, task.TTKT ttkt  "
			+ "from PROGRESS_TASK_OS task where task.STATUS!=0 and task.SOURCE_TASK=2 ");
		ifElseToanQuoc(sql,obj,"task");
		sql.append("group by  task.TTKV,task.TTKT) taskOs " + "on taskOs.ttkt = pt.TTKT " + "where 1=1 ");
	}
	
	public void tdHshcHtctDuKien(StringBuilder sql, ProgressTaskOsDTO obj) {
		sql.append(" ,tdHshcHtct as ( " + "select  " + "pt.TTKV ttkv, " + "pt.TTKT ttkt, "
				+ "nvl(MAX(taskOs.valueHshc),0) tdHshcHtctKhThang, " 
				+ "nvl((select sum(pt1.HSHC_VALUE) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDHS_EXPECTED_COMPLETE_DATE is not null  "
				+ "      and pt1.TTKT = pt.TTKT " + "      and pt1.SOURCE_TASK in (4,5) ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcHtctDuKienHtValue " + "from PROGRESS_TASK_OS pt "
				+ "left join (select sum(task.HSHC_VALUE) valueHshc, task.TTKT ttkt  "
				+ "from PROGRESS_TASK_OS task where task.status!=0 and task.SOURCE_TASK in (4,5) ");
		ifElseToanQuoc(sql,obj,"task");
		sql.append(" group by  task.TTKV,task.TTKT) taskOs " + "on taskOs.ttkt = pt.TTKT where 1=1 ");
	}
	
	public void tdThDtDuKien(StringBuilder sql, ProgressTaskOsDTO obj) {
		sql.append(" ,tdThDt as (select  " + "pt.TTKV ttkv, " + "pt.TTKT ttkt, "
				+ "nvl(MAX(taskOs.valueBill),0) tdThdtKhThang, " 
				+ "nvl((select sum(pt1.BILL_VALUE) "
				+ "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDTT_EXPECTED_COMPLETE_DATE is not null  "
				+ "      and pt1.TTKT = pt.TTKT ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdThdtDuKienHoanThanhValue, "
				+ "null tdThdtNguyenNhanKoHt " + "from PROGRESS_TASK_OS pt "
				+ "left join (select sum(task.bill_Value) valueBill, task.TTKT ttkt  "
				+ "from PROGRESS_TASK_OS task where task.STATUS!=0 ");
		ifElseToanQuoc(sql,obj,"task");
		sql.append("group by  task.TTKV,task.TTKT) taskOs "
				+ "on taskOs.ttkt = pt.TTKT " + "where 1=1 ");
	}
	
	public void tdHshcXdddDuKien(StringBuilder sql, ProgressTaskOsDTO obj) {
		sql.append(" ,tdHshcXddd as (select pt.TTKV ttkv, " + "pt.TTKT ttkt, "
				+ "nvl(MAX(taskOs.valueHshc),0) tdHshcXdddKhThang, " 
				+ "nvl((select sum(pt1.HSHC_VALUE) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDHS_EXPECTED_COMPLETE_DATE is not null  "
				+ "      and pt1.TTKT = pt.TTKT " + "      and pt1.SOURCE_TASK=6 ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdHshcXdddDuKienHoanThanhValue " + "from PROGRESS_TASK_OS pt "
				+ "left join (select " + "task.TTKT ttkt, " + "sum(task.HSHC_VALUE) valueHshc "
				+ "from PROGRESS_TASK_OS task " + "where task.status!=0 and task.SOURCE_TASK = 6 ");
		ifElseToanQuoc(sql,obj,"task");
		sql.append(" group by "
				+ "task.TTKT) taskOs " + "on pt.ttkt = taskOs.ttkt where 1=1 ");
	}
	
	public void tdSlHtctDuKien(StringBuilder sql, ProgressTaskOsDTO obj) {
		sql.append(" ,tdSlHtct as (select pt.TTKV ttkv, " + "pt.TTKT ttkt, "
				+ "nvl(MAX(taskOs.valueQuantity),0) tdSlHtctKhThang, " 
				+ "nvl((select sum(pt1.QUANTITY_VALUE) " + "			from PROGRESS_TASK_OS pt1  "
				+ "      where pt1.status!=0 and pt1.TDSL_EXPECTED_COMPLETE_DATE is not null  "
				+ "      and pt1.TTKT = pt.TTKT " + "      and pt1.SOURCE_TASK in (4,5) ");
		ifElseToanQuoc(sql,obj,"pt1");
		sql.append("			group by pt1.TTKT),0) tdSlHtctDuKienHtValue " + "from PROGRESS_TASK_OS pt "
				+ "left join (select " + "task.TTKT ttkt, " + "sum(task.QUANTITY_VALUE) valueQuantity "
				+ "from PROGRESS_TASK_OS task " + "where task.status!=0 and task.SOURCE_TASK in (4,5) ");
		ifElseToanQuoc(sql,obj,"task");
		sql.append(" group by "
				+ "task.TTKT) taskOs " + "on pt.ttkt = taskOs.ttkt where 1=1 ");
	}
	
	@SuppressWarnings("unchecked")
	public List<ProgressTaskOsDTO> doSearchDkHt(ProgressTaskOsDTO obj) {
		StringBuilder sql = new StringBuilder(" with ");

		// Tiến độ Quỹ lương
		tdQLDuKien(sql, obj);
		ifElseToanQuoc(sql,obj,"pt");
		sql.append("group by pt.TTKV, " + "pt.TTKT) ");

		// Tiến độ HSHC Chi phí
		tdHshcCpDuKien(sql, obj);
		ifElseToanQuoc(sql,obj,"pt");
		sql.append("group by pt.TTKV, " + "pt.TTKT) ");

		// Tiến độ HSHC Xây lắp
		tdHshcXlDuKien(sql, obj);
		ifElseToanQuoc(sql,obj,"pt");
		sql.append("group by pt.TTKV, " + "pt.TTKT) ");

		// Tiến độ HSHC Ngoài tập đoàn
		tdHshcNtdDuKien(sql, obj);
		ifElseToanQuoc(sql,obj,"pt");
		sql.append("group by pt.TTKV, " + "pt.TTKT) ");

		// Tiến độ Sản lượng Chi phí
		tdSlCpDuKien(sql, obj);
		ifElseToanQuoc(sql,obj,"pt");
		sql.append("group by pt.TTKV, " + "pt.TTKT) ");

		// Tiến độ Sản lượng Xây lắp
		tdSlXlDuKien(sql, obj);
		ifElseToanQuoc(sql,obj,"pt");
		sql.append("group by pt.TTKV, " + "pt.TTKT) ");

		// Tiến độ Sản lượng Ngoài tập đoàn
		tdSlNtdDuKien(sql, obj);
		ifElseToanQuoc(sql,obj,"pt");
		sql.append("group by pt.TTKV, " + "pt.TTKT) ");

		// Tiến độ Sản lượng XDDD
		tdSlXdddDuKien(sql, obj);
		ifElseToanQuoc(sql,obj,"pt");
		sql.append("group by pt.TTKV, " + "pt.TTKT) ");

		// Tiến độ Thu hồi dòng tiền
		tdThDtDuKien(sql, obj);
		ifElseToanQuoc(sql,obj,"pt");
		sql.append("group by pt.TTKV, " + "pt.TTKT) ");

		// Tiến độ HSHC HTCT
		tdHshcHtctDuKien(sql, obj);
		ifElseToanQuoc(sql,obj,"pt");
		sql.append("group by  pt.TTKV, " + " pt.TTKT) ");

		// Tiến độ tổng HSHC XDDD
		tdHshcXdddDuKien(sql, obj);
		ifElseToanQuoc(sql,obj,"pt");
		sql.append("group by  pt.TTKV, " + " pt.TTKT) ");

		// Tiến độ Sản lượng HTCT
		tdSlHtctDuKien(sql, obj);
		ifElseToanQuoc(sql,obj,"pt");
		sql.append("group by  pt.TTKV, " + " pt.TTKT) ");

		sql.append(" select tdql.ttkv,tdql.ttkt,nvl(tdql.tdQlKhThang,0) tdQlKhThang, "
				+ "nvl(tdql.tdQlDuKienHoanThanhValue,0) tdQlDuKienHoanThanhValue,"
				+ "(case when tdql.tdQlKhThang>0 then tdql.tdQlDuKienHoanThanhValue/tdql.tdQlKhThang   else 0 end) tdQlDuKienHoanThanhTyLe,"
				+ "tdql.tdQlNguyenNhanKoHt,"

				+ "nvl(tdHshcCp.tdHshcCpKhThang,0) tdHshcCpKhThang,"
				+ "nvl(tdHshcCp.tdHshcCpDuKienHoanThanhValue,0) tdHshcCpDuKienHoanThanhValue,"
				+ "(case when tdHshcCp.tdHshcCpKhThang>0 then tdHshcCp.tdHshcCpDuKienHoanThanhValue/tdHshcCp.tdHshcCpKhThang   else 0 end) tdHshcCpDuKienHoanThanhTyLe,"
				+ "tdHshcCp.tdHshcCpNguyenNhanKoHt, "

				+ "nvl(tdHshcXl.tdHshcXlKhThang,0) tdHshcXlKhThang, "
				+ "nvl(tdHshcXl.tdHshcXlDuKienHoanThanhValue,0) tdHshcXlDuKienHoanThanhValue, "
				+ "(case when tdHshcXl.tdHshcXlKhThang>0 then tdHshcXl.tdHshcXlDuKienHoanThanhValue/tdHshcXl.tdHshcXlKhThang   else 0 end) tdHshcXlDuKienHoanThanhTyLe, "
				+ "tdHshcXl.tdHshcXlNguyenNhanKoHt,"

				+ "nvl(tdHshcNtd.tdHshcNtdKhThang,0) tdHshcNtdKhThang, "
				+ "nvl(tdHshcNtd.tdHshcNtdDuKienHoanThanhValue,0) tdHshcNtdDuKienHoanThanhValue,"
				+ "(case when tdHshcNtd.tdHshcNtdKhThang>0 then tdHshcNtd.tdHshcNtdDuKienHoanThanhValue/tdHshcNtd.tdHshcNtdKhThang   else 0 end) tdHshcNtdDuKienHoanThanhTyLe,"
				+ "tdHshcNtd.tdHshcNtdNguyenNhanKoHt,"

				+ "nvl(tdSlCp.tdSlCpKhThang,0) tdSlCpKhThang,"
				+ "nvl(tdSlCp.tdSlCpDuKienHoanThanhValue,0) tdSlCpDuKienHoanThanhValue,"
				+ "(case when tdSlCp.tdSlCpKhThang>0 then tdSlCp.tdSlCpDuKienHoanThanhValue/tdSlCp.tdSlCpKhThang   else 0 end) tdSlCpDuKienHoanThanhTyLe,"
				+ "tdSlCp.tdSlCpNguyenNhanKoHt,"

				+ "nvl(tdSlXl.tdSlXlKhThang,0) tdSlXlKhThang,"
				+ "nvl(tdSlXl.tdSlXlDuKienHoanThanhValue,0) tdSlXlDuKienHoanThanhValue,"
				+ "(case when tdSlXl.tdSlXlKhThang>0 then tdSlXl.tdSlXlDuKienHoanThanhValue/tdSlXl.tdSlXlKhThang   else 0 end) tdSlXlDuKienHoanThanhTyLe,"
				+ "tdSlXl.tdSlXlNguyenNhanKoHt,"

				+ "nvl(tdSlNtd.tdSlNtdKhThang,0) tdSlNtdKhThang,"
				+ "nvl(tdSlNtd.tdSlNtdDuKienHoanThanhValue,0) tdSlNtdDuKienHoanThanhValue,"
				+ "(case when tdSlNtd.tdSlNtdKhThang>0 then tdSlNtd.tdSlNtdDuKienHoanThanhValue/tdSlNtd.tdSlNtdKhThang   else 0 end) tdSlNtdDuKienHoanThanhTyLe,"
				+ "tdSlNtd.tdSlNtdNguyenNhanKoHt,"

				+ "tdSlXddd.tdSlXdddKhThang tdSlXdddKhThang,"
				+ "tdSlXddd.tdSlXdddDuKienHoanThanhValue tdSlXdddDuKienHoanThanhValue, "
				+ "(case when tdSlXddd.tdSlXdddKhThang>0 then (tdSlXddd.tdSlXdddDuKienHoanThanhValue / tdSlXddd.tdSlXdddKhThang  ) else 0 end) tdSlXdddDuKienHoanThanhTyLe, "
				+ "null tdSlXdddNguyenNhanKoHt, "

				+ "nvl(tdThdt.tdThdtKhThang,0) tdThdtKhThang,"
				+ "nvl(tdThdt.tdThdtDuKienHoanThanhValue,0) tdThdtDuKienHoanThanhValue,"
				+ "(case when tdThdt.tdThdtKhThang>0 then tdThdt.tdThdtDuKienHoanThanhValue/tdThdt.tdThdtKhThang   else 0 end) tdThdtDuKienHoanThanhTyLe,"
				+ "tdThdt.tdThdtNguyenNhanKoHt, "

				+ "tdHshcHtct.tdHshcHtctKhThang tdHshcHtctKhThang,"
				+ " tdHshcHtct.tdHshcHtctDuKienHtValue tdHshcHtctDuKienHtValue,"
				+ "(case when tdHshcHtct.tdHshcHtctKhThang > 0 then (tdHshcHtct.tdHshcHtctDuKienHtValue / tdHshcHtct.tdHshcHtctKhThang ) else 0 end) tdHshcHtctDuKienHtTyLe, null tdHshcHtctNguyenNhanKoHt, "

				+ "tdHshcXddd.tdHshcXdddKhThang tdHshcXdddKhThang,"
				+ " tdHshcXddd.tdHshcXdddDuKienHoanThanhValue tdHshcXdddDuKienHoanThanhValue,"
				+ " (case when tdHshcXddd.tdHshcXdddKhThang > 0 then (tdHshcXddd.tdHshcXdddDuKienHoanThanhValue / tdHshcXddd.tdHshcXdddKhThang  ) else 0 end) tdHshcXdddDuKienHoanThanhTyLe,"
				+ " null tdHshcXdddNguyenNhanKoHt "

				+ ", tdSlHtct.tdSlHtctKhThang tdSlHtctKhThang,"
				+ " tdSlHtct.tdSlHtctDuKienHtValue tdSlHtctDuKienHtValue,"
				+ " (case when tdSlHtct.tdSlHtctKhThang > 0 then (tdSlHtct.tdSlHtctDuKienHtValue / tdSlHtct.tdSlHtctKhThang  ) else 0 end) tdSlHtctDuKienHtTyLe,"
				+ " null tdSlHtctNguyenNhanKoHt "

				+ " from tdql " + " full join tdHshcCp on tdql.ttkt = tdHshcCp.ttkt "
				+ " full join tdHshcXl on tdql.ttkt = tdHshcXl.ttkt "
				+ " full join tdHshcNtd on tdql.ttkt = tdHshcNtd.ttkt "
				+ " full join tdSlCp on tdql.ttkt = tdSlCp.ttkt " + " full join tdSlXl on tdql.ttkt = tdSlXl.ttkt "
				+ " full join tdSlNtd on tdql.ttkt = tdSlNtd.ttkt "
				+ " full join tdSlXddd on tdql.ttkt = tdSlXddd.ttkt " + " full join tdThDt on tdql.ttkt = tdThDt.ttkt "
				+ " full join tdHshcHtct on tdql.ttkt =  tdHshcHtct.ttkt "
				+ " full join tdHshcXddd on tdql.ttkt =  tdHshcXddd.ttkt "
				+ " full join tdSlHtct on tdql.ttkt =  tdSlHtct.ttkt ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("ttkv", new StringType());
		query.addScalar("ttkt", new StringType());

		query.addScalar("tdQlKhThang", new LongType());
		query.addScalar("tdQlDuKienHoanThanhValue", new LongType());
		query.addScalar("tdQlDuKienHoanThanhTyLe", new DoubleType());
		query.addScalar("tdQlNguyenNhanKoHt", new StringType());

		query.addScalar("tdHshcCpKhThang", new LongType());
		query.addScalar("tdHshcCpDuKienHoanThanhValue", new LongType());
		query.addScalar("tdHshcCpDuKienHoanThanhTyLe", new DoubleType());
		query.addScalar("tdHshcCpNguyenNhanKoHt", new StringType());

		query.addScalar("tdHshcXlKhThang", new LongType());
		query.addScalar("tdHshcXlDuKienHoanThanhValue", new LongType());
		query.addScalar("tdHshcXlDuKienHoanThanhTyLe", new DoubleType());
		query.addScalar("tdHshcXlNguyenNhanKoHt", new StringType());

		query.addScalar("tdHshcNtdKhThang", new LongType());
		query.addScalar("tdHshcNtdDuKienHoanThanhValue", new LongType());
		query.addScalar("tdHshcNtdDuKienHoanThanhTyLe", new DoubleType());
		query.addScalar("tdHshcNtdNguyenNhanKoHt", new StringType());

		query.addScalar("tdSlCpKhThang", new LongType());
		query.addScalar("tdSlCpDuKienHoanThanhValue", new LongType());
		query.addScalar("tdSlCpDuKienHoanThanhTyLe", new DoubleType());
		query.addScalar("tdSlCpNguyenNhanKoHt", new StringType());

		query.addScalar("tdSlXlKhThang", new LongType());
		query.addScalar("tdSlXlDuKienHoanThanhValue", new LongType());
		query.addScalar("tdSlXlDuKienHoanThanhTyLe", new DoubleType());
		query.addScalar("tdSlXlNguyenNhanKoHt", new StringType());

		query.addScalar("tdSlNtdKhThang", new LongType());
		query.addScalar("tdSlNtdDuKienHoanThanhValue", new LongType());
		query.addScalar("tdSlNtdDuKienHoanThanhTyLe", new DoubleType());
		query.addScalar("tdSlNtdNguyenNhanKoHt", new StringType());

		query.addScalar("tdSlXdddKhThang", new LongType());
		query.addScalar("tdSlXdddDuKienHoanThanhValue", new LongType());
		query.addScalar("tdSlXdddDuKienHoanThanhTyLe", new DoubleType());
		query.addScalar("tdSlXdddNguyenNhanKoHt", new StringType());

		query.addScalar("tdThdtKhThang", new LongType());
		query.addScalar("tdThdtDuKienHoanThanhValue", new LongType());
		query.addScalar("tdThdtDuKienHoanThanhTyLe", new DoubleType());
		query.addScalar("tdThdtNguyenNhanKoHt", new StringType());

		query.addScalar("tdHshcHtctKhThang", new LongType());
		query.addScalar("tdHshcHtctDuKienHtValue", new LongType());
		query.addScalar("tdHshcHtctDuKienHtTyLe", new DoubleType());
		query.addScalar("tdHshcHtctNguyenNhanKoHt", new StringType());

		query.addScalar("tdHshcXdddKhThang", new LongType());
		query.addScalar("tdHshcXdddDuKienHoanThanhValue", new LongType());
		query.addScalar("tdHshcXdddDuKienHoanThanhTyLe", new DoubleType());
		query.addScalar("tdHshcXdddNguyenNhanKoHt", new StringType());

		query.addScalar("tdSlHtctKhThang", new LongType());
		query.addScalar("tdSlHtctDuKienHtValue", new LongType());
		query.addScalar("tdSlHtctDuKienHtTyLe", new DoubleType());
		query.addScalar("tdSlHtctNguyenNhanKoHt", new LongType());

		if (StringUtils.isNoneBlank(obj.getTtkt())) {
			query.setParameter("ttkt", obj.getTtkt());
		}

		if (StringUtils.isNotBlank(obj.getMonthYear())) {
			query.setParameter("monthYear", obj.getMonthYear());
		}
		
		if (obj.getGroupIdList()!=null && !obj.getGroupIdList().isEmpty()) {
			query.setParameterList("groupIdList", obj.getGroupIdList());
		}

		query.setResultTransformer(Transformers.aliasToBean(ProgressTaskOsDTO.class));

		return query.list();
	}
	
	public Long updateDataEdit(ProgressTaskOsDTO obj) {
		StringBuilder sql = new StringBuilder(" update PROGRESS_TASK_OS set DESCRIPTION=:description, "
				+ "SOURCE_TASK=:sourceTask, "
				+ "CONSTRUCTION_TYPE=:consType, "
				+ "QUANTITY_VALUE=:quantity, "
				+ "HSHC_VALUE=:hshc, "
				+ "SALARY_VALUE=:salary, "
				+ "BILL_VALUE=:bill ");
//				+ "APPROVE_REVENUE_DATE=:revenueDate, "
//				+ "APPROVE_COMPLETE_DATE=:completeDate ");
			if(obj.getTdslAccomplishedDate()!=null) {
				sql.append(",TDSL_ACCOMPLISHED_DATE=:dateAcc ");
			}
			if(obj.getTdttCollectMoneyDate()!=null) {
				sql.append(",TDTT_COLLECT_MONEY_DATE=:cashDate ");
			}
			if(obj.getApproveRevenueDate()!=null) {
				sql.append(",APPROVE_REVENUE_DATE=:revenueDate ");
			}
			if(obj.getApproveCompleteDate()!=null) {
				sql.append(",APPROVE_COMPLETE_DATE=:completeDate ");
			}
				sql.append("WHERE status != 0 and CONSTRUCTION_CODE =:consCode and CNT_CONTRACT_CODE =:contractCode ");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.setParameter("description", obj.getDescription());
		query.setParameter("sourceTask", obj.getSourceTask());
		query.setParameter("consType", obj.getConstructionType());
		query.setParameter("quantity", obj.getQuantityValue());
		query.setParameter("hshc", obj.getHshcValue());
		query.setParameter("salary", obj.getSalaryValue());
		query.setParameter("bill", obj.getBillValue());
		if(obj.getTdslAccomplishedDate()!=null) {
			query.setParameter("dateAcc", obj.getTdslAccomplishedDate());
		}
		
		if(obj.getTdttCollectMoneyDate()!=null) {
			query.setParameter("cashDate", obj.getTdttCollectMoneyDate());
		}
		
		if(obj.getApproveRevenueDate()!=null) {
			query.setParameter("revenueDate", obj.getApproveRevenueDate());
		}
		
		if(obj.getApproveCompleteDate()!=null) {
			query.setParameter("completeDate", obj.getApproveCompleteDate());
		}
		query.setParameter("consCode", obj.getConstructionCode());
		query.setParameter("contractCode", obj.getCntContractCode());
		
		return (long)query.executeUpdate();
	}
	
//	public List<DetailMonthPlanDTO> checkMonthPlan(ProgressTaskOsDTO obj) {
//		StringBuilder sql = new StringBuilder(" SELECT DISTINCT DETAIL_MONTH_PLAN_ID detailMonthPlanId, "
//				+ "NAME name, "
//				+ "SIGN_STATE signState "
//				+ "FROM DETAIL_MONTH_PLAN "
//				+ "WHERE SIGN_STATE=3 "
//				+ "AND STATUS!=0 "
//				+ "AND SYS_GROUP_ID in (select sys_group_id from sys_group where PROVINCE_CODE=:code and sys_group.GROUP_LEVEL=2) "
//				+ "and month=EXTRACT(MONTH FROM TO_DATE(:dateOptions, 'MM/yyyy')) "
//				+ "and year=EXTRACT(YEAR FROM TO_DATE(:dateOptions, 'MM/yyyy')) ");
//		SQLQuery query = getSession().createSQLQuery(sql.toString());
//		
//		query.addScalar("detailMonthPlanId", new LongType());
//		query.addScalar("name", new StringType());
//		query.addScalar("signState", new LongType());
//		
//		query.setParameter("code", obj.getTtkt());
//		query.setParameter("code", obj.getMonthYear());
//		
//		query.setResultTransformer(Transformers.aliasToBean(DetailMonthPlanDTO.class));
//		
//		return query.list();
//		
//	}
	
	//Quỹ lương
	public List<ProgressTaskOsDTO> getDataTienDoQuyLuongKv(ProgressTaskOsDTO obj){
		StringBuilder sql = new StringBuilder("with tdql  as (select     pt.TTKV ttkv, "
				+ "			 nvl(MAX(taskOs.valueSalary),0) tdQlKhThang, "
				+ "			 nvl((select sum(pt1.SALARY_VALUE)   "
				+ "			          from PROGRESS_TASK_OS pt1 where pt1.status!=0 and pt1.TDHS_TCT_NOT_APPROVAL is not null and pt1.TTKV = pt.TTKV   ");
		ifElseBaoCao(sql,obj,"pt1");
		sql.append("			          group by pt1.TTKV),0) tdQlTrenDuongValue,    "
				+ "			 nvl((select sum(pt1.SALARY_VALUE)   "
				+ "			          from PROGRESS_TASK_OS pt1 where pt1.status!=0 and pt1.TDHS_SIGNING_GDCN is not null and pt1.TTKV = pt.TTKV   ");
		ifElseBaoCao(sql,obj,"pt1");
		sql.append("			          group by pt1.TTKV),0) tdQlGdCnKyValue,    "
				+ "			 nvl((select sum(pt1.SALARY_VALUE)   "
				+ "			          from PROGRESS_TASK_OS pt1 where pt1.status!=0 and pt1.TDHS_CONTROL_4A is not null and pt1.TTKV = pt.TTKV   ");
		ifElseBaoCao(sql,obj,"pt1");
		sql.append("			          group by pt1.TTKV),0) tdQlDoiSoat4aValue,    "
				+ "			 nvl((select sum(pt1.SALARY_VALUE)    "
				+ "			          from PROGRESS_TASK_OS pt1 where pt1.status!=0 and pt1.TDHS_PHT_APPROVALING is not null and pt1.TTKV = pt.TTKV   ");
		ifElseBaoCao(sql,obj,"pt1");
		sql.append("			          group by pt1.TTKV),0) tdQlPhtThamDuyetValue,    "
				+ "			 nvl((select sum(pt1.SALARY_VALUE)    "
				+ "			          from PROGRESS_TASK_OS pt1 where pt1.status!=0 and pt1.TDHS_PHT_ACCEPTANCING is not null and pt1.TTKV = pt.TTKV   ");
		ifElseBaoCao(sql,obj,"pt1");
		sql.append("			          group by pt1.TTKV),0) tdQlPhtNghiemThuValue,    "
				+ "			 nvl((select sum(pt1.SALARY_VALUE)    "
				+ "			          from PROGRESS_TASK_OS pt1 where pt1.status!=0 and pt1.TDHS_TTKT_PROFILE is not null and pt1.TTKV = pt.TTKV   ");
		ifElseBaoCao(sql,obj,"pt1");
		sql.append("			          group by pt1.TTKV),0) tdQlDangLamHoSoValue,    "
				+ "			 nvl((select sum(pt1.SALARY_VALUE)    "
				+ "			          from PROGRESS_TASK_OS pt1 where pt1.status!=0 and pt1.TDHS_EXPECTED_COMPLETE_DATE is not null and pt1.TTKV = pt.TTKV   ");
		ifElseBaoCao(sql,obj,"pt1");
		sql.append("			          group by pt1.TTKV),0) tdQlDuKienHoanThanhValue "
				+ "			 from PROGRESS_TASK_OS pt    left join (select sum(task.SALARY_VALUE) valueSalary, task.TTKV ttkv    "
				+ "			 from PROGRESS_TASK_OS task where task.STATUS!=0  ");
		ifElseBaoCao(sql,obj,"task");
		sql.append("  group by  task.ttkv) taskOs   "
				+ "			 on taskOs.ttkv = pt.TTKV    where 1=1  ");
		if (StringUtils.isNotBlank(obj.getMonthYear())) {
			sql.append(" AND pt.MONTH_YEAR = :monthYear ");
		}
		sql.append(" group by  pt.ttkv) ");
		
		sql.append(" select ttkv ttkv,"
				+ " tdQlKhThang tdQlKhThang,"
				+ " tdQlTrenDuongValue tdQlTrenDuongValue,"
				+ " (case when tdQlKhThang > 0 then (tdQlTrenDuongValue / tdQlKhThang  ) else 0 end) tdQlTrenDuongTyle,"
				+ " tdQlGdCnKyValue tdQlGdCnKyValue,"
				+ " (case when tdQlKhThang > 0 then (tdQlGdCnKyValue / tdQlKhThang  ) else 0 end) tdQlGdCnKyTyLe,"
				+ " tdQlDoiSoat4aValue tdQlDoiSoat4aValue, "
				+ " (case when tdQlKhThang > 0 then (tdQlDoiSoat4aValue / tdQlKhThang  ) else 0 end) tdQlDoiSoat4aTyLe,"
				+ " tdQlPhtThamDuyetValue tdQlPhtThamDuyetValue,"
				+ " (case when tdQlKhThang > 0 then (tdQlPhtThamDuyetValue / tdQlKhThang  ) else 0 end) tdQlPhtThamDuyetTyLe,"
				+ " tdQlPhtNghiemThuValue tdQlPhtNghiemThuValue,"
				+ " (case when tdQlKhThang > 0 then (tdQlPhtNghiemThuValue / tdQlKhThang  ) else 0 end) tdQlPhtNghiemThuTyLe,"
				+ " tdQlDangLamHoSoValue tdQlDangLamHoSoValue,"
				+ " (case when tdQlKhThang > 0 then (tdQlDangLamHoSoValue / tdQlKhThang  ) else 0 end) tdQlDangLamHoSoTyLe,"
				+ " tdQlDuKienHoanThanhValue tdQlDuKienHoanThanhValue, "
				+ " (case when tdQlKhThang > 0 then (tdQlDuKienHoanThanhValue / tdQlKhThang  ) else 0 end) tdQlDuKienHoanThanhTyLe "
				+ " from tdql ");
		
		SQLQuery query =  getSession().createSQLQuery(sql.toString());
		query.addScalar("ttkv", new StringType());
		query.addScalar("tdQlKhThang", new LongType());
		query.addScalar("tdQlTrenDuongValue", new LongType());
		query.addScalar("tdQlTrenDuongTyle", new DoubleType());
		query.addScalar("tdQlGdCnKyValue", new LongType());
		query.addScalar("tdQlGdCnKyTyLe", new DoubleType());
		query.addScalar("tdQlDoiSoat4aValue", new LongType());
		query.addScalar("tdQlDoiSoat4aTyLe", new DoubleType());
		query.addScalar("tdQlPhtThamDuyetValue", new LongType());
		query.addScalar("tdQlPhtThamDuyetTyLe", new DoubleType());
		query.addScalar("tdQlPhtNghiemThuValue", new LongType());
		query.addScalar("tdQlPhtNghiemThuTyLe", new DoubleType());
		query.addScalar("tdQlDangLamHoSoValue", new LongType());
		query.addScalar("tdQlDangLamHoSoTyLe", new DoubleType());
		query.addScalar("tdQlDuKienHoanThanhValue", new LongType());
		query.addScalar("tdQlDuKienHoanThanhTyLe", new DoubleType());
		
		if (StringUtils.isNotBlank(obj.getMonthYear())) {
			query.setParameter("monthYear", obj.getMonthYear());
		}
		
		query.setResultTransformer(Transformers.aliasToBean(ProgressTaskOsDTO.class));
		
		return query.list();
	}
	
	//Sản lượng chi phí
	public List<ProgressTaskOsDTO> getDataTienDoSlCp(ProgressTaskOsDTO obj){
		StringBuilder sql = new StringBuilder("WITH tdSlCp AS " + "  (SELECT pt.TTKV ttkv, "
				+ "    NVL(MAX(taskOs.valueQuantity),0) tdSlCpKhThang, " 
				+ "    NVL( "
				+ "    (SELECT SUM(pt1.QUANTITY_VALUE) " + "    FROM PROGRESS_TASK_OS pt1 "
				+ "    WHERE pt1.status!                    =0 "
				+ "    AND pt1.TDSL_ACCOMPLISHED_DATE IS NOT NULL "
				+ "    AND pt1.TTKV                         = pt.TTKV " + "    AND pt1.SOURCE_TASK                  =2 ");
		ifElseBaoCao(sql,obj,"pt1");
		sql.append("    GROUP BY pt1.TTKV " + "    ),0) tdSlCpDaHoanThanh, " 
				+ "    NVL( "
				+ "    (SELECT SUM(pt1.QUANTITY_VALUE) " + "    FROM PROGRESS_TASK_OS pt1 "
				+ "    WHERE pt1.status!          =0 " + "    AND pt1.TDSL_CONSTRUCTING IS NOT NULL AND pt1.TDSL_ACCOMPLISHED_DATE is null "
				+ "    AND pt1.TTKV               = pt.TTKV " + "    AND pt1.SOURCE_TASK        =2 ");
		ifElseBaoCao(sql,obj,"pt1");
		sql.append("    GROUP BY pt1.TTKV " + "    ),0) tdSlCpDangThiCongValue, " 
				+ "    NVL( "
				+ "    (SELECT SUM(pt1.QUANTITY_VALUE) " + "    FROM PROGRESS_TASK_OS pt1 "
				+ "    WHERE pt1.status!               =0 " + "    AND pt1.TDSL_EXPECTED_COMPLETE_DATE IS NOT NULL "
				+ "    AND pt1.TTKV                    = pt.TTKV " + "    AND pt1.SOURCE_TASK             =2 ");
		ifElseBaoCao(sql,obj,"pt1");
		sql.append("    GROUP BY pt1.TTKV " + "    ),0) tdSlCpDuKienHoanThanhValue, " 
				+ "    NULL tdSlCpNguyenNhanKoHt "
				+ "  FROM PROGRESS_TASK_OS pt " 
				+ "  LEFT JOIN "
				+ "    (SELECT SUM(task.QUANTITY_VALUE) valueQuantity, " + "      task.TTKV ttkv "
				+ "    FROM PROGRESS_TASK_OS task " + "    WHERE task.STATUS!  =0 " + "    AND task.SOURCE_TASK=2 ");
		ifElseBaoCao(sql,obj,"task");
		sql.append("    GROUP BY task.TTKV " + "    ) taskOs " + "  ON taskOs.TTKV = pt.TTKV "
				+ "  WHERE 1        =1  ");
		if (StringUtils.isNotBlank(obj.getMonthYear())) {
			sql.append(" AND pt.MONTH_YEAR = :monthYear ");
		}
		sql.append(" group by  pt.ttkv) ");
		
		sql.append(" select ttkv ttkv,"
				+ " tdSlCpKhThang tdSlCpKhThang,"
				+ " tdSlCpDaHoanThanh tdSlCpDaHoanThanh,"
				+ " (case when tdSlCpKhThang > 0 then (tdSlCpDaHoanThanh / tdSlCpKhThang ) else 0 end) tdSlCpDaHoanThanhTyLe,"
				+ " tdSlCpDangThiCongValue tdSlCpDangThiCongValue,"
				+ " (case when tdSlCpKhThang > 0 then (tdSlCpDangThiCongValue / tdSlCpKhThang ) else 0 end) tdSlCpDangThiCongTyLe,"
				+ " tdSlCpDuKienHoanThanhValue tdSlCpDuKienHoanThanhValue, "
				+ " (case when tdSlCpKhThang > 0 then (tdSlCpDuKienHoanThanhValue / tdSlCpKhThang) else 0 end) tdSlCpDuKienHoanThanhTyLe "
				+ " from tdSlCp ");
		
		SQLQuery query =  getSession().createSQLQuery(sql.toString());
		query.addScalar("ttkv", new StringType());
		query.addScalar("tdSlCpKhThang", new LongType());
		query.addScalar("tdSlCpDaHoanThanh", new LongType());
		query.addScalar("tdSlCpDaHoanThanhTyLe", new DoubleType());
		query.addScalar("tdSlCpDangThiCongValue", new LongType());
		query.addScalar("tdSlCpDangThiCongTyLe", new DoubleType());
		query.addScalar("tdSlCpDuKienHoanThanhValue", new LongType());
		query.addScalar("tdSlCpDuKienHoanThanhTyLe", new DoubleType());
		
		if (StringUtils.isNotBlank(obj.getMonthYear())) {
			query.setParameter("monthYear", obj.getMonthYear());
		}
		
		query.setResultTransformer(Transformers.aliasToBean(ProgressTaskOsDTO.class));
		
		return query.list();
	}
	
	//HSHC Chi phí
	public List<ProgressTaskOsDTO> getDataTienDoHshcCp(ProgressTaskOsDTO obj){
		StringBuilder sql = new StringBuilder("WITH tdHshcCp as (select     pt.TTKV ttkv, " + 
				"			 nvl(MAX(taskOs.valueHshc),0) tdHshcCpKhThang,    null tdHshcCpTong,    null tdHshcCpTthtDaDuyet,   " + 
				"			 nvl((select sum(pt1.HSHC_VALUE)    			from PROGRESS_TASK_OS pt1    " + 
				"			       where pt1.status!=0 and pt1.TDHS_TCT_NOT_APPROVAL is not null           and pt1.TTKV = pt.TTKV   " + 
				"			       and pt1.SOURCE_TASK=2    	");
		ifElseBaoCao(sql,obj,"pt1");
		sql.append("		group by pt1.TTKV),0) tdHshcCpTrenDuongValue,   " + 
				"			 nvl((select sum(pt1.HSHC_VALUE)    			from PROGRESS_TASK_OS pt1    " + 
				"			       where pt1.status!=0 and pt1.TDHS_SIGNING_GDCN is not null and pt1.TDHS_TCT_NOT_APPROVAL is null        and pt1.TTKV = pt.TTKV   " + 
				"			       and pt1.SOURCE_TASK=2    	");
		ifElseBaoCao(sql,obj,"pt1");
		sql.append("		group by pt1.TTKV),0) tdHshcCpGdCnKyValue,   " + 
				"			 nvl((select sum(pt1.HSHC_VALUE)    			from PROGRESS_TASK_OS pt1    " + 
				"			       where pt1.status!=0 and pt1.TDHS_CONTROL_4A is not null   and pt1.TDHS_TCT_NOT_APPROVAL is null        and pt1.TTKV = pt.TTKV   " + 
				"			       and pt1.SOURCE_TASK=2    		");
		ifElseBaoCao(sql,obj,"pt1");
		sql.append("	group by pt1.TTKV),0) tdHshcCpDoiSoat4aValue,   " + 
				"			 nvl((select sum(pt1.HSHC_VALUE)    			from PROGRESS_TASK_OS pt1    " + 
				"			       where pt1.status!=0 and pt1.TDHS_PHT_APPROVALING is not null   and pt1.TDHS_TCT_NOT_APPROVAL is null        and pt1.TTKV = pt.TTKV   " + 
				"			       and pt1.SOURCE_TASK=2    		");
		ifElseBaoCao(sql,obj,"pt1");
		sql.append("	group by pt1.TTKV),0) tdHshcCpPhtThamDuyetValue,   " + 
				"			 nvl((select sum(pt1.HSHC_VALUE)    			from PROGRESS_TASK_OS pt1    " + 
				"			       where pt1.status!=0 and pt1.TDHS_PHT_ACCEPTANCING is not null   and pt1.TDHS_TCT_NOT_APPROVAL is null        and pt1.TTKV = pt.TTKV   " + 
				"			       and pt1.SOURCE_TASK=2    		");
		ifElseBaoCao(sql,obj,"pt1");
		sql.append("	group by pt1.TTKV),0) tdHshcCpPhtNghiemThuValue,   " + 
				"			 nvl((select sum(pt1.HSHC_VALUE)    			from PROGRESS_TASK_OS pt1    " + 
				"			       where pt1.status!=0 and pt1.TDHS_TTKT_PROFILE is not null    and pt1.TDHS_TCT_NOT_APPROVAL is null       and pt1.TTKV = pt.TTKV   " + 
				"			       and pt1.SOURCE_TASK=2    		");
		ifElseBaoCao(sql,obj,"pt1");
		sql.append("	group by pt1.TTKV),0) tdHshcCpDangLamHoSoValue,   " + 
				"			 nvl((select sum(pt1.HSHC_VALUE)    			from PROGRESS_TASK_OS pt1    " + 
				"			       where pt1.status!=0 and pt1.TDHS_EXPECTED_COMPLETE_DATE is not null    " + 
				"			       and pt1.TTKV = pt.TTKV          and pt1.SOURCE_TASK=2   ");
		ifElseBaoCao(sql,obj,"pt1");
		sql.append("			 			group by pt1.TTKV),0) tdHshcCpDuKienHoanThanhValue,    " + 
				"			 null tdHshcCpNguyenNhanKoHt   " + 
				"			 from PROGRESS_TASK_OS pt    left join (select sum(task.HSHC_VALUE) valueHshc, task.TTKV ttkv    " + 
				"			 from PROGRESS_TASK_OS task where task.STATUS!=0 and task.SOURCE_TASK=2   ");
		ifElseBaoCao(sql,obj,"task");
		sql.append("			 group by  task.TTKV) taskOs    on taskOs.ttkv = pt.TTKV    where 1=1  ");
		if (StringUtils.isNotBlank(obj.getMonthYear())) {
			sql.append(" AND pt.MONTH_YEAR = :monthYear ");
		}
		sql.append(" group by  pt.ttkv) ");
		
		sql.append(" select ttkv ttkv,"
				+ " tdHshcCpKhThang tdHshcCpKhThang,"
				+ " tdHshcCpTrenDuongValue tdHshcCpTrenDuongValue,"
				+ " (case when tdHshcCpKhThang > 0 then (tdHshcCpTrenDuongValue / tdHshcCpKhThang  ) else 0 end) tdHshcCpTrenDuongTyLe,"
				+ " tdHshcCpGdCnKyValue tdHshcCpGdCnKyValue,"
				+ " (case when tdHshcCpKhThang > 0 then (tdHshcCpGdCnKyValue / tdHshcCpKhThang  ) else 0 end) tdHshcCpGdCnKyTyLe,"
				+ " tdHshcCpDoiSoat4aValue tdHshcCpDoiSoat4aValue, "
				+ " (case when tdHshcCpKhThang > 0 then (tdHshcCpDoiSoat4aValue / tdHshcCpKhThang  ) else 0 end) tdHshcCpDoiSoat4aTyLe,"
				+ " tdHshcCpPhtThamDuyetValue tdHshcCpPhtThamDuyetValue,"
				+ " (case when tdHshcCpKhThang > 0 then (tdHshcCpPhtThamDuyetValue / tdHshcCpKhThang  ) else 0 end) tdHshcCpPhtThamDuyetTyLe,"
				+ " tdHshcCpPhtNghiemThuValue tdHshcCpPhtNghiemThuValue,"
				+ " (case when tdHshcCpKhThang > 0 then (tdHshcCpPhtNghiemThuValue / tdHshcCpKhThang  ) else 0 end) tdHshcCpPhtNghiemThuTyLe,"
				+ " tdHshcCpDangLamHoSoValue tdHshcCpDangLamHoSoValue,"
				+ " (case when tdHshcCpKhThang > 0 then (tdHshcCpDangLamHoSoValue / tdHshcCpKhThang  ) else 0 end) tdHshcCpDangLamHoSoTyLe,"
				+ " tdHshcCpDuKienHoanThanhValue tdHshcCpDuKienHoanThanhValue, "
				+ " (case when tdHshcCpKhThang > 0 then (tdHshcCpDuKienHoanThanhValue / tdHshcCpKhThang  ) else 0 end) tdHshcCpDuKienHoanThanhTyLe "
				+ " from tdHshcCp ");
		
		SQLQuery query =  getSession().createSQLQuery(sql.toString());
		query.addScalar("ttkv", new StringType());
		query.addScalar("tdHshcCpKhThang", new LongType());
		query.addScalar("tdHshcCpTrenDuongValue", new LongType());
		query.addScalar("tdHshcCpTrenDuongTyLe", new DoubleType());
		query.addScalar("tdHshcCpGdCnKyValue", new LongType());
		query.addScalar("tdHshcCpGdCnKyTyLe", new DoubleType());
		query.addScalar("tdHshcCpDoiSoat4aValue", new LongType());
		query.addScalar("tdHshcCpDoiSoat4aTyLe", new DoubleType());
		query.addScalar("tdHshcCpPhtThamDuyetValue", new LongType());
		query.addScalar("tdHshcCpPhtThamDuyetTyLe", new DoubleType());
		query.addScalar("tdHshcCpPhtNghiemThuValue", new LongType());
		query.addScalar("tdHshcCpPhtNghiemThuTyLe", new DoubleType());
		query.addScalar("tdHshcCpDangLamHoSoValue", new LongType());
		query.addScalar("tdHshcCpDangLamHoSoTyLe", new DoubleType());
		query.addScalar("tdHshcCpDuKienHoanThanhValue", new LongType());
		query.addScalar("tdHshcCpDuKienHoanThanhTyLe", new DoubleType());
		
		if (StringUtils.isNotBlank(obj.getMonthYear())) {
			query.setParameter("monthYear", obj.getMonthYear());
		}
		
		query.setResultTransformer(Transformers.aliasToBean(ProgressTaskOsDTO.class));
		
		return query.list();
	}
	
	//Sản lượng xây lắp
		public List<ProgressTaskOsDTO> getDataTienDoSlXl(ProgressTaskOsDTO obj){
			StringBuilder sql = new StringBuilder("WITH tdSlXl AS " + "  (SELECT pt.TTKV ttkv, "
					+ "    NVL(MAX(taskOs.valueQuantity),0) tdSlXlKhThang, " 
					+ "    NVL( "
					+ "    (SELECT SUM(pt1.QUANTITY_VALUE) " + "    FROM PROGRESS_TASK_OS pt1 "
					+ "    WHERE pt1.status!                    =0 "
					+ "    AND pt1.TDSL_ACCOMPLISHED_DATE IS NOT NULL "
					+ "    AND pt1.TTKV                         = pt.TTKV " + "    AND pt1.SOURCE_TASK                  =1 ");
			ifElseBaoCao(sql,obj,"pt1");
			sql.append("    GROUP BY pt1.TTKV " + "    ),0) tdSlXlDaHoanThanh, " 
					+ "    NVL( "
					+ "    (SELECT SUM(pt1.QUANTITY_VALUE) " + "    FROM PROGRESS_TASK_OS pt1 "
					+ "    WHERE pt1.status!          =0 " + "    AND pt1.TDSL_CONSTRUCTING IS NOT NULL AND pt1.TDSL_ACCOMPLISHED_DATE is null "
					+ "    AND pt1.TTKV               = pt.TTKV " + "    AND pt1.SOURCE_TASK        =1 ");
			ifElseBaoCao(sql,obj,"pt1");
			sql.append("    GROUP BY pt1.TTKV " + "    ),0) tdSlXlDangThiCongValue, " 
					+ "    NVL( "
					+ "    (SELECT SUM(pt1.QUANTITY_VALUE) " + "    FROM PROGRESS_TASK_OS pt1 "
					+ "    WHERE pt1.status!               =0 " + "    AND pt1.TDSL_EXPECTED_COMPLETE_DATE IS NOT NULL "
					+ "    AND pt1.TTKV                    = pt.TTKV " + "    AND pt1.SOURCE_TASK             =1 ");
			ifElseBaoCao(sql,obj,"pt1");
			sql.append("    GROUP BY pt1.TTKV " + "    ),0) tdSlXlDuKienHoanThanhValue, " 
					+ "    NULL tdSlXlNguyenNhanKoHt "
					+ "  FROM PROGRESS_TASK_OS pt " 
					+ "  LEFT JOIN "
					+ "    (SELECT SUM(task.QUANTITY_VALUE) valueQuantity, " + "      task.TTKV ttkv "
					+ "    FROM PROGRESS_TASK_OS task " + "    WHERE task.STATUS!  =0 " + "    AND task.SOURCE_TASK=1 ");
			ifElseBaoCao(sql,obj,"task");
			sql.append("    GROUP BY task.TTKV " + "    ) taskOs " + "  ON taskOs.TTKV = pt.TTKV "
					+ "  WHERE 1        =1  ");
			if (StringUtils.isNotBlank(obj.getMonthYear())) {
				sql.append(" AND pt.MONTH_YEAR = :monthYear ");
			}
			sql.append(" group by  pt.ttkv) ");
			
			sql.append(" select ttkv ttkv,"
					+ " tdSlXlKhThang tdSlXlKhThang,"
					+ " tdSlXlDaHoanThanh tdSlXlDaHoanThanh,"
					+ " (case when tdSlXlKhThang > 0 then (tdSlXlDaHoanThanh / tdSlXlKhThang ) else 0 end) tdSlXlDaHoanThanhTyLe,"
					+ " tdSlXlDangThiCongValue tdSlXlDangThiCongValue,"
					+ " (case when tdSlXlKhThang > 0 then (tdSlXlDangThiCongValue / tdSlXlKhThang ) else 0 end) tdSlXlDangThiCongTyLe,"
					+ " tdSlXlDuKienHoanThanhValue tdSlXlDuKienHoanThanhValue, "
					+ " (case when tdSlXlKhThang > 0 then (tdSlXlDuKienHoanThanhValue / tdSlXlKhThang ) else 0 end) tdSlXlDuKienHoanThanhTyLe "
					+ " from tdSlXl ");
			
			SQLQuery query =  getSession().createSQLQuery(sql.toString());
			query.addScalar("ttkv", new StringType());
			query.addScalar("tdSlXlKhThang", new LongType());
			query.addScalar("tdSlXlDaHoanThanh", new LongType());
			query.addScalar("tdSlXlDaHoanThanhTyLe", new DoubleType());
			query.addScalar("tdSlXlDangThiCongValue", new LongType());
			query.addScalar("tdSlXlDangThiCongTyLe", new DoubleType());
			query.addScalar("tdSlXlDuKienHoanThanhValue", new LongType());
			query.addScalar("tdSlXlDuKienHoanThanhTyLe", new DoubleType());
			
			if (StringUtils.isNotBlank(obj.getMonthYear())) {
				query.setParameter("monthYear", obj.getMonthYear());
			}
			
			query.setResultTransformer(Transformers.aliasToBean(ProgressTaskOsDTO.class));
			
			return query.list();
		}
		
		//HSHC xây lắp
		public List<ProgressTaskOsDTO> getDataTienDoHshcXl(ProgressTaskOsDTO obj){
			StringBuilder sql = new StringBuilder("WITH tdHshcXl as (select     pt.TTKV ttkv, " + 
					"			 nvl(MAX(taskOs.valueHshc),0) tdHshcXlKhThang, " + 
					"			 nvl((select sum(pt1.HSHC_VALUE)    			from PROGRESS_TASK_OS pt1    " + 
					"			       where pt1.status!=0 and pt1.TDHS_TCT_NOT_APPROVAL is not null           and pt1.TTKV = pt.TTKV   " + 
					"			       and pt1.SOURCE_TASK=1    	");
			ifElseBaoCao(sql,obj,"pt1");
			sql.append("		group by pt1.TTKV),0) tdHshcXlTrenDuongValue,   " + 
					"			 nvl((select sum(pt1.HSHC_VALUE)    			from PROGRESS_TASK_OS pt1    " + 
					"			       where pt1.status!=0 and pt1.TDHS_SIGNING_GDCN is not null   and pt1.TDHS_TCT_NOT_APPROVAL is null        and pt1.TTKV = pt.TTKV   " + 
					"			       and pt1.SOURCE_TASK=1    	");
			ifElseBaoCao(sql,obj,"pt1");
			sql.append("		group by pt1.TTKV),0) tdHshcXlGdCnKyValue,   " + 
					"			 nvl((select sum(pt1.HSHC_VALUE)    			from PROGRESS_TASK_OS pt1    " + 
					"			       where pt1.status!=0 and pt1.TDHS_CONTROL_4A is not null     and pt1.TDHS_TCT_NOT_APPROVAL is null      and pt1.TTKV = pt.TTKV   " + 
					"			       and pt1.SOURCE_TASK=1    	");
			ifElseBaoCao(sql,obj,"pt1");
			sql.append("		group by pt1.TTKV),0) tdHshcXlDoiSoat4aValue,   " + 
					"			 nvl((select sum(pt1.HSHC_VALUE)    			from PROGRESS_TASK_OS pt1    " + 
					"			       where pt1.status!=0 and pt1.TDHS_PHT_APPROVALING is not null   and pt1.TDHS_TCT_NOT_APPROVAL is null        and pt1.TTKV = pt.TTKV   " + 
					"			       and pt1.SOURCE_TASK=1    	");
			ifElseBaoCao(sql,obj,"pt1");
			sql.append("		group by pt1.TTKV),0) tdHshcXlPhtThamDuyetValue,   " + 
					"			 nvl((select sum(pt1.HSHC_VALUE)    			from PROGRESS_TASK_OS pt1    " + 
					"			       where pt1.status!=0 and pt1.TDHS_PHT_ACCEPTANCING is not null  and pt1.TDHS_TCT_NOT_APPROVAL is null         and pt1.TTKV = pt.TTKV   " + 
					"			       and pt1.SOURCE_TASK=1    	");
			ifElseBaoCao(sql,obj,"pt1");
			sql.append("		group by pt1.TTKV),0) tdHshcXlPhtNghiemThuValue,   " + 
					"			 nvl((select sum(pt1.HSHC_VALUE)    			from PROGRESS_TASK_OS pt1    " + 
					"			       where pt1.status!=0 and pt1.TDHS_TTKT_PROFILE is not null    and pt1.TDHS_TCT_NOT_APPROVAL is null       and pt1.TTKV = pt.TTKV   " + 
					"			       and pt1.SOURCE_TASK=1    	");
			ifElseBaoCao(sql,obj,"pt1");
			sql.append("		group by pt1.TTKV),0) tdHshcXlDangLamHoSoValue,   " + 
					"			 nvl((select sum(pt1.HSHC_VALUE)    			from PROGRESS_TASK_OS pt1    " + 
					"			       where pt1.status!=0 and pt1.TDHS_EXPECTED_COMPLETE_DATE is not null    " + 
					"			       and pt1.TTKV = pt.TTKV          and pt1.SOURCE_TASK=1   ");
			ifElseBaoCao(sql,obj,"pt1");
			sql.append("			 			group by pt1.TTKV),0) tdHshcXlDuKienHoanThanhValue,    " + 
					"			 null tdHshcXlNguyenNhanKoHt   " + 
					"			 from PROGRESS_TASK_OS pt    left join (select sum(task.HSHC_VALUE) valueHshc, task.TTKV ttkv    " + 
					"			 from PROGRESS_TASK_OS task where task.STATUS!=0 and task.SOURCE_TASK=1   ");
			ifElseBaoCao(sql,obj,"task");
			sql.append("			 group by  task.TTKV) taskOs    on taskOs.ttkv = pt.TTKV    where 1=1  ");
			if (StringUtils.isNotBlank(obj.getMonthYear())) {
				sql.append(" AND pt.MONTH_YEAR = :monthYear ");
			}
			sql.append(" group by  pt.ttkv) ");
			
			sql.append(" select ttkv ttkv,"
					+ " tdHshcXlKhThang tdHshcXlKhThang,"
					+ " tdHshcXlTrenDuongValue tdHshcXlTrenDuongValue,"
					+ " (case when tdHshcXlKhThang > 0 then (tdHshcXlTrenDuongValue / tdHshcXlKhThang  ) else 0 end) tdHshcXlTrenDuongTyLe,"
					+ " tdHshcXlGdCnKyValue tdHshcXlGdCnKyValue,"
					+ " (case when tdHshcXlKhThang > 0 then (tdHshcXlGdCnKyValue / tdHshcXlKhThang  ) else 0 end) tdHshcXlGdCnKyTyLe,"
					+ " tdHshcXlDoiSoat4aValue tdHshcXlDoiSoat4aValue, "
					+ " (case when tdHshcXlKhThang > 0 then (tdHshcXlDoiSoat4aValue / tdHshcXlKhThang  ) else 0 end) tdHshcXlDoiSoat4aTyLe,"
					+ " tdHshcXlPhtThamDuyetValue tdHshcXlPhtThamDuyetValue,"
					+ " (case when tdHshcXlKhThang > 0 then (tdHshcXlPhtThamDuyetValue / tdHshcXlKhThang  ) else 0 end) tdHshcXlPhtThamDuyetTyLe,"
					+ " tdHshcXlPhtNghiemThuValue tdHshcXlPhtNghiemThuValue,"
					+ " (case when tdHshcXlKhThang > 0 then (tdHshcXlPhtNghiemThuValue / tdHshcXlKhThang  ) else 0 end) tdHshcXlPhtNghiemThuTyLe,"
					+ " tdHshcXlDangLamHoSoValue tdHshcXlDangLamHoSoValue,"
					+ " (case when tdHshcXlKhThang > 0 then (tdHshcXlDangLamHoSoValue / tdHshcXlKhThang  ) else 0 end) tdHshcXlDangLamHoSoTyLe,"
					+ " tdHshcXlDuKienHoanThanhValue tdHshcXlDuKienHoanThanhValue, "
					+ " (case when tdHshcXlKhThang > 0 then (tdHshcXlDuKienHoanThanhValue / tdHshcXlKhThang  ) else 0 end) tdHshcXlDuKienHoanThanhTyLe "
					+ " from tdHshcXl ");
			
			SQLQuery query =  getSession().createSQLQuery(sql.toString());
			query.addScalar("ttkv", new StringType());
			query.addScalar("tdHshcXlKhThang", new LongType());
			query.addScalar("tdHshcXlTrenDuongValue", new LongType());
			query.addScalar("tdHshcXlTrenDuongTyLe", new DoubleType());
			query.addScalar("tdHshcXlGdCnKyValue", new LongType());
			query.addScalar("tdHshcXlGdCnKyTyLe", new DoubleType());
			query.addScalar("tdHshcXlDoiSoat4aValue", new LongType());
			query.addScalar("tdHshcXlDoiSoat4aTyLe", new DoubleType());
			query.addScalar("tdHshcXlPhtThamDuyetValue", new LongType());
			query.addScalar("tdHshcXlPhtThamDuyetTyLe", new DoubleType());
			query.addScalar("tdHshcXlPhtNghiemThuValue", new LongType());
			query.addScalar("tdHshcXlPhtNghiemThuTyLe", new DoubleType());
			query.addScalar("tdHshcXlDangLamHoSoValue", new LongType());
			query.addScalar("tdHshcXlDangLamHoSoTyLe", new DoubleType());
			query.addScalar("tdHshcXlDuKienHoanThanhValue", new LongType());
			query.addScalar("tdHshcXlDuKienHoanThanhTyLe", new DoubleType());
			
			if (StringUtils.isNotBlank(obj.getMonthYear())) {
				query.setParameter("monthYear", obj.getMonthYear());
			}
			
			query.setResultTransformer(Transformers.aliasToBean(ProgressTaskOsDTO.class));
			
			return query.list();
		}
		
		//Triển khai HTCT
		public List<ProgressTaskOsDTO> getDataTienDoHtct(ProgressTaskOsDTO obj){
			StringBuilder sql = new StringBuilder(" with ");
			sql.append(" tdHtctXdm as (SELECT DISTINCT " + "  TTKV ttkv, " + "  MAX(khThang) tdHtctXdmKhThang, "
					+ "  MAX((case when accomplish is not null then (select count(distinct CONSTRUCTION_CODE) from (select * from PROGRESS_TASK_OS WHERE SOURCE_TASK = 4 and STATUS!=0 and WORK_ITEM_NAME is not null ");
			
			if (StringUtils.isNotBlank(obj.getMonthYear())) {
				sql.append(" AND MONTH_YEAR = :monthYear ");
			}
			sql.append("  and TDSL_ACCOMPLISHED_DATE is not null)) else null end)) tdHtctXdmDaHt, "
					+ "  MAX((case when constructing is not null and accomplish is null then (select count(distinct CONSTRUCTION_CODE) from (select * from PROGRESS_TASK_OS WHERE SOURCE_TASK = 4 and STATUS!=0 and WORK_ITEM_NAME is not null ");
	
			if (StringUtils.isNotBlank(obj.getMonthYear())) {
				sql.append(" AND MONTH_YEAR = :monthYear ");
			}
	
			sql.append("  and TDSL_CONSTRUCTING is not null))  "
					+ "  when constructing is not null and accomplish is not null then (select count(distinct CONSTRUCTION_CODE) from (select * from PROGRESS_TASK_OS WHERE SOURCE_TASK = 4 and STATUS!=0 and WORK_ITEM_NAME is not null ");
	
			if (StringUtils.isNotBlank(obj.getMonthYear())) {
				sql.append(" AND MONTH_YEAR = :monthYear ");
			}
	
			sql.append("  and TDSL_CONSTRUCTING is not null and TDSL_ACCOMPLISHED_DATE is null)) "
					+ "  else null end)) tdHtctXdmDangTc, ");
			sql.append(" MAX((case when expectedDate is not null  "
					+ "then (select count(*) from (select TTKV from PROGRESS_TASK_OS  " + "WHERE " + "SOURCE_TASK = 4  "
					+ "and STATUS!=0 and WORK_ITEM_NAME is not null ");
//			if (StringUtils.isNoneBlank(obj.getTtkt())) {
//				sql.append(" and TTKT = :ttkt ");
//			}
			if (StringUtils.isNotBlank(obj.getMonthYear())) {
				sql.append(" AND MONTH_YEAR = :monthYear ");
			}

//			if (obj.getGroupIdList() != null && !obj.getGroupIdList().isEmpty()) {
//				sql.append(" and TTKT_ID in (:groupIdList) ");
//			}
			sql.append(" and TDSL_EXPECTED_COMPLETE_DATE is not null)) " + "else null " + "end))  tdHtctXdmDuKienHt "
					+ "FROM " + "  (SELECT  "
					+ "  COUNT(distinct CONSTRUCTION_CODE) khThang, TTKV, " + "    MAX(TDSL_ACCOMPLISHED_DATE) accomplish, "
					+ "    MAX(TDSL_CONSTRUCTING) constructing, " + "    MAX(TDSL_EXPECTED_COMPLETE_DATE) expectedDate "
					+ "  FROM PROGRESS_TASK_OS " + "  WHERE SOURCE_TASK = 4 and STATUS!=0 and WORK_ITEM_NAME is not null ");
	
			if (StringUtils.isNotBlank(obj.getMonthYear())) {
				sql.append(" AND MONTH_YEAR = :monthYear ");
			}
	
			sql.append("  GROUP BY TTKV " + "  ) " + "  GROUP BY TTKV) ");
			
			//Tiến độ HTCT Hoàn thiện
			sql.append(" ,tdHtctHt as (SELECT DISTINCT " + "  TTKV ttkv, " + "  MAX(khThang) tdHtctHtKhThang, "
					+ "  MAX((case when accomplish is not null then (select count(distinct CONSTRUCTION_CODE) from (select * from PROGRESS_TASK_OS WHERE SOURCE_TASK = 5 and STATUS!=0 and WORK_ITEM_NAME is not null ");
			if (StringUtils.isNotBlank(obj.getMonthYear())) {
				sql.append(" AND MONTH_YEAR = :monthYear ");
			}
			sql.append("  and TDSL_ACCOMPLISHED_DATE is not null)) else null end)) tdHtctHtDaHt, "
					+ "  MAX((case when constructing is not null and accomplish is null then (select count(distinct CONSTRUCTION_CODE) from (select * from PROGRESS_TASK_OS WHERE SOURCE_TASK = 5 and STATUS!=0 and WORK_ITEM_NAME is not null ");
	
			if (StringUtils.isNotBlank(obj.getMonthYear())) {
				sql.append(" AND MONTH_YEAR = :monthYear ");
			}
	
			sql.append("  and TDSL_CONSTRUCTING is not null))  "
					+ "  when constructing is not null and accomplish is not null then (select count(distinct CONSTRUCTION_CODE) from (select * from PROGRESS_TASK_OS WHERE SOURCE_TASK = 5 and STATUS!=0 and WORK_ITEM_NAME is not null ");
	
			if (StringUtils.isNotBlank(obj.getMonthYear())) {
				sql.append(" AND MONTH_YEAR = :monthYear ");
			}
	
			sql.append("  and TDSL_CONSTRUCTING is not null and TDSL_ACCOMPLISHED_DATE is null)) "
					+ "  else null end)) tdHtctHtDangTc, " );
					sql.append(" MAX((case when expectedDate is not null  "
							+ "then (select count(*) from (select TTKV from PROGRESS_TASK_OS  " + "WHERE " + "SOURCE_TASK = 5  "
							+ "and STATUS!=0 and WORK_ITEM_NAME is not null ");
//					if (StringUtils.isNoneBlank(obj.getTtkt())) {
//						sql.append(" and TTKT = :ttkt ");
//					}
					if (StringUtils.isNotBlank(obj.getMonthYear())) {
						sql.append(" AND MONTH_YEAR = :monthYear ");
					}

//					if (obj.getGroupIdList() != null && !obj.getGroupIdList().isEmpty()) {
//						sql.append(" and TTKT_ID in (:groupIdList) ");
//					}
					sql.append(" and TDSL_EXPECTED_COMPLETE_DATE is not null)) " + "else null " + "end))  tdHtctHtDuKienHt " 
					+ " FROM " + "  (SELECT  "
					+ "  COUNT(distinct CONSTRUCTION_CODE) khThang, TTKV, " + "    MAX(TDSL_ACCOMPLISHED_DATE) accomplish, "
					+ "    MAX(TDSL_CONSTRUCTING) constructing, " + "    MAX(TDSL_EXPECTED_COMPLETE_DATE) expectedDate "
					+ "  FROM PROGRESS_TASK_OS " + "  WHERE SOURCE_TASK = 5 and STATUS!=0 and WORK_ITEM_NAME is not null ");
	
			if (StringUtils.isNotBlank(obj.getMonthYear())) {
				sql.append(" AND MONTH_YEAR = :monthYear ");
			}
	
			sql.append("  GROUP BY TTKV " + "  ) " + "  GROUP BY TTKV) ");
			
			sql.append(" SELECT tdHtctXdm.ttkv ttkv,tdHtctXdm.tdHtctXdmKhThang tdHtctXdmKhThang,"
					+ " tdHtctXdm.tdHtctXdmDaHt tdHtctXdmDaHt,"
					+ " (case when tdHtctXdm.tdHtctXdmKhThang>0 then (tdHtctXdm.tdHtctXdmDaHt / tdHtctXdm.tdHtctXdmKhThang  ) else 0 end) tdHtctXdmTyLe, "
					+ " tdHtctXdmDangTc tdHtctXdmDangTc, "
					+ " (case when tdHtctXdm.tdHtctXdmKhThang>0 then (tdHtctXdm.tdHtctXdmDangTc / tdHtctXdm.tdHtctXdmKhThang  ) else 0 end) tdHtctXdmDangTcTyLe, "
					+ " tdHtctXdm.tdHtctXdmDuKienHt tdHtctXdmDuKienHt,"
					+ " (case when tdHtctXdm.tdHtctXdmKhThang>0 then (tdHtctXdm.tdHtctXdmDuKienHt / tdHtctXdm.tdHtctXdmKhThang  ) else 0 end) tdHtctXdmDuKienHtTyLe, "
					+ " tdHtctHt.tdHtctHtKhThang tdHtctHtKhThang,"
					+ " tdHtctHt.tdHtctHtDaHt tdHtctHtDaHt,"
					+ " (case when tdHtctHt.tdHtctHtKhThang>0 then (tdHtctHt.tdHtctHtDaHt / tdHtctHt.tdHtctHtKhThang  ) else 0 end) tdHtctHtTyLe, "
					+ " tdHtctHt.tdHtctHtDangTc tdHtctHtDangTc,"
					+ " (case when tdHtctHt.tdHtctHtKhThang>0 then (tdHtctHt.tdHtctHtDangTc / tdHtctHt.tdHtctHtKhThang  ) else 0 end) tdHtctHtDangTcTyLe, "
					+ " tdHtctHt.tdHtctHtDuKienHt tdHtctHtDuKienHt, "
					+ " (case when tdHtctHt.tdHtctHtKhThang>0 then (tdHtctHt.tdHtctHtDuKienHt / tdHtctHt.tdHtctHtKhThang  ) else 0 end) tdHtctHtDuKienHtTyLe "
					+ " from tdHtctXdm tdHtctXdm "
					+ " full join tdHtctHt tdHtctHt on tdHtctXdm.ttkv = tdHtctHt.ttkv ");
			
			SQLQuery query =  getSession().createSQLQuery(sql.toString());
			query.addScalar("ttkv", new StringType());
			query.addScalar("tdHtctXdmKhThang", new LongType());
			query.addScalar("tdHtctXdmDaHt", new LongType());
			query.addScalar("tdHtctXdmTyLe", new DoubleType());
			query.addScalar("tdHtctXdmDangTc", new LongType());
			query.addScalar("tdHtctXdmDangTcTyLe", new DoubleType());
			query.addScalar("tdHtctXdmDuKienHt", new LongType());
			query.addScalar("tdHtctXdmDuKienHtTyLe", new DoubleType());
			query.addScalar("tdHtctHtKhThang", new LongType());
			query.addScalar("tdHtctHtDaHt", new LongType());
			query.addScalar("tdHtctHtTyLe", new DoubleType());
			query.addScalar("tdHtctHtDangTc", new LongType());
			query.addScalar("tdHtctHtDangTcTyLe", new DoubleType());
			query.addScalar("tdHtctHtDuKienHt", new LongType());
			query.addScalar("tdHtctHtDuKienHtTyLe", new DoubleType());
			
			if (StringUtils.isNotBlank(obj.getMonthYear())) {
				query.setParameter("monthYear", obj.getMonthYear());
			}
			
			query.setResultTransformer(Transformers.aliasToBean(ProgressTaskOsDTO.class));
			
			return query.list();
		}
		
		//Sản lượng HTCT
				public List<ProgressTaskOsDTO> getDataTienDoSlHtct(ProgressTaskOsDTO obj){
		StringBuilder sql = new StringBuilder("WITH tdSlHtct as (select pt.TTKV ttkv,   "
				+ "			 nvl(MAX(taskOs.valueQuantity),0) tdSlHtctKhThang,    "
				+ "			 nvl((select sum(pt1.QUANTITY_VALUE)   "
				+ "			 			from PROGRESS_TASK_OS pt1    "
				+ "			       where pt1.status!=0 and pt1.TDSL_ACCOMPLISHED_DATE is not null           and pt1.TTKV = pt.TTKV   "
				+ "			       and pt1.SOURCE_TASK in (4,5)    	");
		ifElseBaoCao(sql,obj,"pt1");
		sql.append("		group by pt1.TTKV),0) tdSlHtctDaHt,   "
				+ "			 nvl((select sum(pt1.QUANTITY_VALUE)    			from PROGRESS_TASK_OS pt1    "
				+ "			       where pt1.status!=0 and pt1.TDSL_CONSTRUCTING is not null           and pt1.TTKV = pt.TTKV   "
				+ "			       and pt1.SOURCE_TASK in (4,5)    	");
		ifElseBaoCao(sql,obj,"pt1");
		sql.append("		group by pt1.TTKV),0) tdSlHtctDangTcValue,   "
				+ "			 nvl((select sum(pt1.QUANTITY_VALUE)    			from PROGRESS_TASK_OS pt1    "
				+ "			       where pt1.status!=0 and pt1.TDSL_EXPECTED_COMPLETE_DATE is not null    "
				+ "			       and pt1.TTKV = pt.TTKV          and pt1.SOURCE_TASK in (4,5)   ");
		ifElseBaoCao(sql,obj,"pt1");
		sql.append("			 			group by pt1.TTKV),0) tdSlHtctDuKienHtValue    from PROGRESS_TASK_OS pt   "
				+ "			 left join (select    task.TTKV ttkv,    sum(task.QUANTITY_VALUE) valueQuantity   "
				+ "			 from PROGRESS_TASK_OS task    where task.status!=0 and task.SOURCE_TASK in (4,5)  ");
		ifElseBaoCao(sql,obj,"task");
		sql.append("   group by   "
				+ "			 task.ttkv) taskOs    on pt.TTKV = taskOs.ttkv where 1=1 ");
					if (StringUtils.isNotBlank(obj.getMonthYear())) {
						sql.append(" AND pt.MONTH_YEAR = :monthYear ");
					}
					sql.append(" group by  pt.ttkv) ");
					
					sql.append(" select ttkv ttkv,"
							+ " tdSlHtctKhThang tdSlHtctKhThang,"
							+ " tdSlHtctDaHt tdSlHtctDaHt,"
							+ " (case when tdSlHtctKhThang > 0 then (tdSlHtctDaHt / tdSlHtctKhThang ) else 0 end) tdSlHtctDaHtTyLe,"
							+ " tdSlHtctDangTcValue tdSlHtctDangTcValue,"
							+ " (case when tdSlHtctKhThang > 0 then (tdSlHtctDangTcValue / tdSlHtctKhThang ) else 0 end) tdSlHtctDangTcTyLe,"
							+ " tdSlHtctDuKienHtValue tdSlHtctDuKienHtValue, "
							+ " (case when tdSlHtctKhThang > 0 then (tdSlHtctDuKienHtValue / tdSlHtctKhThang ) else 0 end) tdSlHtctDuKienHtTyLe "
							+ " from tdSlHtct ");
					
					SQLQuery query =  getSession().createSQLQuery(sql.toString());
					query.addScalar("ttkv", new StringType());
					query.addScalar("tdSlHtctKhThang", new LongType());
					query.addScalar("tdSlHtctDaHt", new LongType());
					query.addScalar("tdSlHtctDaHtTyLe", new DoubleType());
					query.addScalar("tdSlHtctDangTcValue", new LongType());
					query.addScalar("tdSlHtctDangTcTyLe", new DoubleType());
					query.addScalar("tdSlHtctDuKienHtValue", new LongType());
					query.addScalar("tdSlHtctDuKienHtTyLe", new DoubleType());
					
					if (StringUtils.isNotBlank(obj.getMonthYear())) {
						query.setParameter("monthYear", obj.getMonthYear());
					}
					
					query.setResultTransformer(Transformers.aliasToBean(ProgressTaskOsDTO.class));
					
					return query.list();
				}
		
				//HSHC HTCT
				public List<ProgressTaskOsDTO> getDataTienDoHshcHtct(ProgressTaskOsDTO obj){
					StringBuilder sql = new StringBuilder("WITH tdHshcHtct as (select     pt.TTKV ttkv, " + 
							"			 nvl(MAX(taskOs.valueHshc),0) tdHshcHtctKhThang, " + 
							"			 nvl((select sum(pt1.HSHC_VALUE)    			from PROGRESS_TASK_OS pt1    " + 
							"			       where pt1.status!=0 and pt1.TDHS_TCT_NOT_APPROVAL is not null           and pt1.TTKV = pt.TTKV   " + 
							"			       and pt1.SOURCE_TASK in (4,5)    	");
					ifElseBaoCao(sql,obj,"pt1");
					sql.append("		group by pt1.TTKV),0) tdHshcHtctTrenDuongValue,   " + 
							"			 nvl((select sum(pt1.HSHC_VALUE)    			from PROGRESS_TASK_OS pt1    " + 
							"			       where pt1.status!=0 and pt1.TDHS_SIGNING_GDCN is not null   and pt1.TDHS_TCT_NOT_APPROVAL is null         and pt1.TTKV = pt.TTKV   " + 
							"			       and pt1.SOURCE_TASK in (4,5)    	");
					ifElseBaoCao(sql,obj,"pt1");
					sql.append("		group by pt1.TTKV),0) tdHshcHtctGdCnKyValue,   " + 
							"			 nvl((select sum(pt1.HSHC_VALUE)    			from PROGRESS_TASK_OS pt1    " + 
							"			       where pt1.status!=0 and pt1.TDHS_CONTROL_4A is not null  and pt1.TDHS_TCT_NOT_APPROVAL is null         and pt1.TTKV = pt.TTKV   " + 
							"			       and pt1.SOURCE_TASK in (4,5)    	");
					ifElseBaoCao(sql,obj,"pt1");
					sql.append("		group by pt1.TTKV),0) tdHshcHtctDoiSoat4aValue,   " + 
							"			 nvl((select sum(pt1.HSHC_VALUE)    			from PROGRESS_TASK_OS pt1    " + 
							"			       where pt1.status!=0 and pt1.TDHS_PHT_APPROVALING is not null  and pt1.TDHS_TCT_NOT_APPROVAL is null         and pt1.TTKV = pt.TTKV   " + 
							"			       and pt1.SOURCE_TASK in (4,5)    	");
					ifElseBaoCao(sql,obj,"pt1");
					sql.append("		group by pt1.TTKV),0) tdHshcHtctPhtThamDuyetValue,   " + 
							"			 nvl((select sum(pt1.HSHC_VALUE)    			from PROGRESS_TASK_OS pt1    " + 
							"			       where pt1.status!=0 and pt1.TDHS_PHT_ACCEPTANCING is not null  and pt1.TDHS_TCT_NOT_APPROVAL is null         and pt1.TTKV = pt.TTKV   " + 
							"			       and pt1.SOURCE_TASK in (4,5)    	");
					ifElseBaoCao(sql,obj,"pt1");
					sql.append("		group by pt1.TTKV),0) tdHshcHtctPhtNghiemThuValue,   " + 
							"			 nvl((select sum(pt1.HSHC_VALUE)    			from PROGRESS_TASK_OS pt1    " + 
							"			       where pt1.status!=0 and pt1.TDHS_TTKT_PROFILE is not null    and pt1.TDHS_TCT_NOT_APPROVAL is null       and pt1.TTKV = pt.TTKV   " + 
							"			       and pt1.SOURCE_TASK in (4,5)    	");
					ifElseBaoCao(sql,obj,"pt1");
					sql.append("		group by pt1.TTKV),0) tdHshcHtctTtktHoSoValue,   " + 
							"			 nvl((select sum(pt1.HSHC_VALUE)    			from PROGRESS_TASK_OS pt1    " + 
							"			       where pt1.status!=0 and pt1.TDHS_EXPECTED_COMPLETE_DATE is not null    " + 
							"			       and pt1.TTKV = pt.TTKV          and pt1.SOURCE_TASK in (4,5)   ");
					ifElseBaoCao(sql,obj,"pt1");
					sql.append("			 			group by pt1.TTKV),0) tdHshcHtctDuKienHtValue,    " + 
							"			 null tdHshcHtctNguyenNhanKoHt   " + 
							"			 from PROGRESS_TASK_OS pt    left join (select sum(task.HSHC_VALUE) valueHshc, task.TTKV ttkv    " + 
							"			 from PROGRESS_TASK_OS task where task.STATUS!=0 and task.SOURCE_TASK in (4,5)   ");
					ifElseBaoCao(sql,obj,"task");
					sql.append("			 group by  task.TTKV) taskOs    on taskOs.ttkv = pt.TTKV    where 1=1  ");
					if (StringUtils.isNotBlank(obj.getMonthYear())) {
						sql.append(" AND pt.MONTH_YEAR = :monthYear ");
					}
					sql.append(" group by  pt.ttkv) ");
					
					sql.append(" select ttkv ttkv,"
							+ " tdHshcHtctKhThang tdHshcHtctKhThang,"
							+ " tdHshcHtctTrenDuongValue tdHshcHtctTrenDuongValue,"
							+ " (case when tdHshcHtctKhThang > 0 then (tdHshcHtctTrenDuongValue / tdHshcHtctKhThang  ) else 0 end) tdHshcHtctTrenDuongTyLe,"
							+ " tdHshcHtctGdCnKyValue tdHshcHtctGdCnKyValue,"
							+ " (case when tdHshcHtctKhThang > 0 then (tdHshcHtctGdCnKyValue / tdHshcHtctKhThang  ) else 0 end) tdHshcHtctGdCnKyTyLe,"
							+ " tdHshcHtctDoiSoat4aValue tdHshcHtctDoiSoat4aValue, "
							+ " (case when tdHshcHtctKhThang > 0 then (tdHshcHtctDoiSoat4aValue / tdHshcHtctKhThang  ) else 0 end) tdHshcHtctDoiSoat4aTyLe,"
							+ " tdHshcHtctPhtThamDuyetValue tdHshcHtctPhtThamDuyetValue,"
							+ " (case when tdHshcHtctKhThang > 0 then (tdHshcHtctPhtThamDuyetValue / tdHshcHtctKhThang  ) else 0 end) tdHshcHtctPhtThamDuyetTyLe,"
							+ " tdHshcHtctPhtNghiemThuValue tdHshcHtctPhtNghiemThuValue,"
							+ " (case when tdHshcHtctKhThang > 0 then (tdHshcHtctPhtNghiemThuValue / tdHshcHtctKhThang  ) else 0 end) tdHshcHtctPhtNghiemThuTyLe,"
							+ " tdHshcHtctTtktHoSoValue tdHshcHtctTtktHoSoValue,"
							+ " (case when tdHshcHtctKhThang > 0 then (tdHshcHtctTtktHoSoValue / tdHshcHtctKhThang  ) else 0 end) tdHshcHtctTtktHoSoTyLe,"
							+ " tdHshcHtctDuKienHtValue tdHshcHtctDuKienHtValue, "
							+ " (case when tdHshcHtctKhThang > 0 then (tdHshcHtctDuKienHtValue / tdHshcHtctKhThang  ) else 0 end) tdHshcHtctDuKienHtTyLe "
							+ " from tdHshcHtct ");
					
					SQLQuery query =  getSession().createSQLQuery(sql.toString());
					query.addScalar("ttkv", new StringType());
					query.addScalar("tdHshcHtctKhThang", new LongType());
					query.addScalar("tdHshcHtctTrenDuongValue", new LongType());
					query.addScalar("tdHshcHtctTrenDuongTyLe", new DoubleType());
					query.addScalar("tdHshcHtctGdCnKyValue", new LongType());
					query.addScalar("tdHshcHtctGdCnKyTyLe", new DoubleType());
					query.addScalar("tdHshcHtctDoiSoat4aValue", new LongType());
					query.addScalar("tdHshcHtctDoiSoat4aTyLe", new DoubleType());
					query.addScalar("tdHshcHtctPhtThamDuyetValue", new LongType());
					query.addScalar("tdHshcHtctPhtThamDuyetTyLe", new DoubleType());
					query.addScalar("tdHshcHtctPhtNghiemThuValue", new LongType());
					query.addScalar("tdHshcHtctPhtNghiemThuTyLe", new DoubleType());
					query.addScalar("tdHshcHtctTtktHoSoValue", new LongType());
					query.addScalar("tdHshcHtctTtktHoSoTyLe", new DoubleType());
					query.addScalar("tdHshcHtctDuKienHtValue", new LongType());
					query.addScalar("tdHshcHtctDuKienHtTyLe", new DoubleType());
					
					if (StringUtils.isNotBlank(obj.getMonthYear())) {
						query.setParameter("monthYear", obj.getMonthYear());
					}
					
					query.setResultTransformer(Transformers.aliasToBean(ProgressTaskOsDTO.class));
					
					return query.list();
				}
				
				//Thu hồi dòng tiền
				public List<ProgressTaskOsDTO> getDataTienDoThdt(ProgressTaskOsDTO obj){
					StringBuilder sql = new StringBuilder("WITH tdThDt as (select     pt.TTKV ttkv,   " + 
							"			 nvl(MAX(taskOs.valueBill),0) tdThdtKhThang,    " + 
							"			 nvl((select sum(pt1.BILL_VALUE)   " + 
							"			 			from PROGRESS_TASK_OS pt1    " + 
							"			       where pt1.status!=0 and pt1.TDTT_COLLECT_MONEY_DATE is not null    " + 
							"			       and pt1.TTKV = pt.TTKV    	");
					ifElseBaoCao(sql,obj,"pt1");
					sql.append("		group by pt1.TTKV),0) tdThdtDaHoanThanh,   " + 
							"			 nvl((select sum(pt1.BILL_VALUE)    			from PROGRESS_TASK_OS pt1    " + 
							"			       where pt1.status!=0 and pt1.TDTT_PROFILE_PHT is not null   and pt1.TDTT_COLLECT_MONEY_DATE is null          and pt1.TTKV = pt.TTKV   ");
					ifElseBaoCao(sql,obj,"pt1");
					sql.append("			 			group by pt1.TTKV),0) tdThdtPhtDangKiemTraValue,    " + 
							"			 nvl((select sum(pt1.BILL_VALUE)   " + 
							"			 			from PROGRESS_TASK_OS pt1    " + 
							"			       where pt1.status!=0 and pt1.TDTT_PROFILE_PTC is not null    and pt1.TDTT_COLLECT_MONEY_DATE is null         and pt1.TTKV = pt.TTKV   ");
					ifElseBaoCao(sql,obj,"pt1");
					sql.append("			 			group by pt1.TTKV),0) tdThdtPtcDangKiemTraValue,    " + 
							"			 nvl((select sum(pt1.BILL_VALUE)   " + 
							"			 			from PROGRESS_TASK_OS pt1    " + 
							"			       where pt1.status!=0 and pt1.TDTT_EXPECTED_COMPLETE_DATE is not null    " + 
							"			       and pt1.TTKV = pt.TTKV    	");
					ifElseBaoCao(sql,obj,"pt1");
					sql.append("		group by pt1.TTKV),0) tdThdtDuKienHoanThanhValue,   " + 
							"			 null tdThdtNguyenNhanKoHt    from PROGRESS_TASK_OS pt   " + 
							"			 left join (select sum(task.bill_Value) valueBill, task.TTKV ttkv    " + 
							"			 from PROGRESS_TASK_OS task where task.STATUS!=0  ");
					ifElseBaoCao(sql,obj,"task");
					sql.append("  group by  task.TTKV) taskOs   " + 
							"			 on taskOs.TTKV = pt.TTKV    where 1=1 ");
								if (StringUtils.isNotBlank(obj.getMonthYear())) {
									sql.append(" AND pt.MONTH_YEAR = :monthYear ");
								}
								sql.append(" group by  pt.ttkv) ");
								
								sql.append(" select ttkv ttkv,"
										+ " tdThdtKhThang tdThdtKhThang,"
										+ " tdThdtDaHoanThanh tdThdtDaHoanThanh,"
										+ " (case when tdThdtKhThang > 0 then (tdThdtDaHoanThanh / tdThdtKhThang ) else 0 end) tdThdtDaHoanThanhTyLe,"
										+ " tdThdtPhtDangKiemTraValue tdThdtPhtDangKiemTraValue,"
										+ " tdThdtPtcDangKiemTraValue tdThdtPtcDangKiemTraValue,"
										+ " tdThdtDuKienHoanThanhValue tdThdtDuKienHoanThanhValue, "
										+ " (case when tdThdtKhThang > 0 then (tdThdtDuKienHoanThanhValue / tdThdtKhThang ) else 0 end) tdThdtDuKienHoanThanhTyLe "
										+ " from tdThDt ");
								
								SQLQuery query =  getSession().createSQLQuery(sql.toString());
								query.addScalar("ttkv", new StringType());
								query.addScalar("tdThdtKhThang", new LongType());
								query.addScalar("tdThdtDaHoanThanh", new LongType());
								query.addScalar("tdThdtDaHoanThanhTyLe", new DoubleType());
								query.addScalar("tdThdtPhtDangKiemTraValue", new LongType());
								query.addScalar("tdThdtPtcDangKiemTraValue", new LongType());
								query.addScalar("tdThdtDuKienHoanThanhValue", new LongType());
								query.addScalar("tdThdtDuKienHoanThanhTyLe", new DoubleType());
								
								if (StringUtils.isNotBlank(obj.getMonthYear())) {
									query.setParameter("monthYear", obj.getMonthYear());
								}
								
								query.setResultTransformer(Transformers.aliasToBean(ProgressTaskOsDTO.class));
								
								return query.list();
							}
				
				//Sản lượng Ngoài tập đoàn
				public List<ProgressTaskOsDTO> getDataTienDoSlNtd(ProgressTaskOsDTO obj){
					StringBuilder sql = new StringBuilder("WITH tdSlNtd AS " + "  (SELECT pt.TTKV ttkv, "
							+ "    NVL(MAX(taskOs.valueQuantity),0) tdSlNtdKhThang, " 
							+ "    NVL( "
							+ "    (SELECT SUM(pt1.QUANTITY_VALUE) " + "    FROM PROGRESS_TASK_OS pt1 "
							+ "    WHERE pt1.status!                    =0 "
							+ "    AND pt1.TDSL_ACCOMPLISHED_DATE IS NOT NULL "
							+ "    AND pt1.TTKV                         = pt.TTKV " + "    AND pt1.SOURCE_TASK                  =3 ");
					ifElseBaoCao(sql,obj,"pt1");
					sql.append("    GROUP BY pt1.TTKV " + "    ),0) tdSlNtdDaHoanThanh, " 
							+ "    NVL( "
							+ "    (SELECT SUM(pt1.QUANTITY_VALUE) " + "    FROM PROGRESS_TASK_OS pt1 "
							+ "    WHERE pt1.status!          =0 " + "    AND pt1.TDSL_CONSTRUCTING IS NOT NULL AND pt1.TDSL_ACCOMPLISHED_DATE IS NULL "
							+ "    AND pt1.TTKV               = pt.TTKV " + "    AND pt1.SOURCE_TASK        =3 ");
					ifElseBaoCao(sql,obj,"pt1");
					sql.append("    GROUP BY pt1.TTKV " + "    ),0) tdSlNtdDangThiCongValue, " 
							+ "    NVL( "
							+ "    (SELECT SUM(pt1.QUANTITY_VALUE) " + "    FROM PROGRESS_TASK_OS pt1 "
							+ "    WHERE pt1.status!               =0 " + "    AND pt1.TDSL_EXPECTED_COMPLETE_DATE IS NOT NULL "
							+ "    AND pt1.TTKV                    = pt.TTKV " + "    AND pt1.SOURCE_TASK             =3 ");
					ifElseBaoCao(sql,obj,"pt1");
					sql.append("    GROUP BY pt1.TTKV " + "    ),0) tdSlNtdDuKienHoanThanhValue, " 
							+ "    NULL tdSlNtdNguyenNhanKoHt "
							+ "  FROM PROGRESS_TASK_OS pt " 
							+ "  LEFT JOIN "
							+ "    (SELECT SUM(task.QUANTITY_VALUE) valueQuantity, " + "      task.TTKV ttkv "
							+ "    FROM PROGRESS_TASK_OS task " + "    WHERE task.STATUS!  =0 " + "    AND task.SOURCE_TASK=3 ");
					ifElseBaoCao(sql,obj,"task");
					sql.append("    GROUP BY task.TTKV " + "    ) taskOs " + "  ON taskOs.TTKV = pt.TTKV "
							+ "  WHERE 1        =1  ");
					if (StringUtils.isNotBlank(obj.getMonthYear())) {
						sql.append(" AND pt.MONTH_YEAR = :monthYear ");
					}
					sql.append(" group by  pt.ttkv) ");
					
					sql.append(" select ttkv ttkv,"
							+ " tdSlNtdKhThang tdSlNtdKhThang,"
							+ " tdSlNtdDaHoanThanh tdSlNtdDaHoanThanh,"
							+ " (case when tdSlNtdKhThang > 0 then (tdSlNtdDaHoanThanh / tdSlNtdKhThang ) else 0 end) tdSlNtdDaHoanThanhTyLe,"
							+ " tdSlNtdDangThiCongValue tdSlNtdDangThiCongValue,"
							+ " (case when tdSlNtdKhThang > 0 then (tdSlNtdDangThiCongValue / tdSlNtdKhThang ) else 0 end) tdSlNtdDangThiCongTyLe,"
							+ " tdSlNtdDuKienHoanThanhValue tdSlNtdDuKienHoanThanhValue, "
							+ " (case when tdSlNtdKhThang > 0 then (tdSlNtdDuKienHoanThanhValue / tdSlNtdKhThang ) else 0 end) tdSlNtdDuKienHoanThanhTyLe "
							+ " from tdSlNtd ");
					
					SQLQuery query =  getSession().createSQLQuery(sql.toString());
					query.addScalar("ttkv", new StringType());
					query.addScalar("tdSlNtdKhThang", new LongType());
					query.addScalar("tdSlNtdDaHoanThanh", new LongType());
					query.addScalar("tdSlNtdDaHoanThanhTyLe", new DoubleType());
					query.addScalar("tdSlNtdDangThiCongValue", new LongType());
					query.addScalar("tdSlNtdDangThiCongTyLe", new DoubleType());
					query.addScalar("tdSlNtdDuKienHoanThanhValue", new LongType());
					query.addScalar("tdSlNtdDuKienHoanThanhTyLe", new DoubleType());
					
					if (StringUtils.isNotBlank(obj.getMonthYear())) {
						query.setParameter("monthYear", obj.getMonthYear());
					}
					
					query.setResultTransformer(Transformers.aliasToBean(ProgressTaskOsDTO.class));
					
					return query.list();
				}
				
				//HSHC Ngoài tập đoàn
				public List<ProgressTaskOsDTO> getDataTienDoHshcNtd(ProgressTaskOsDTO obj){
					StringBuilder sql = new StringBuilder("WITH tdHshcNtd as (select     pt.TTKV ttkv, " + 
							"			 nvl(MAX(taskOs.valueHshc),0) tdHshcNtdKhThang, " + 
							"			 nvl((select sum(pt1.HSHC_VALUE)    			from PROGRESS_TASK_OS pt1    " + 
							"			       where pt1.status!=0 and pt1.TDHS_TCT_NOT_APPROVAL is not null           and pt1.TTKV = pt.TTKV   " + 
							"			       and pt1.SOURCE_TASK=3    	");
					ifElseBaoCao(sql,obj,"pt1");
					sql.append("		group by pt1.TTKV),0) tdHshcNtdTrenDuongValue,   " + 
							"			 nvl((select sum(pt1.HSHC_VALUE)    			from PROGRESS_TASK_OS pt1    " + 
							"			       where pt1.status!=0 and pt1.TDHS_SIGNING_GDCN is not null  and pt1.TDHS_TCT_NOT_APPROVAL is null         and pt1.TTKV = pt.TTKV   " + 
							"			       and pt1.SOURCE_TASK=3    	");
					ifElseBaoCao(sql,obj,"pt1");
					sql.append("		group by pt1.TTKV),0) tdHshcNtdGdCnKyValue,   " + 
							"			 nvl((select sum(pt1.HSHC_VALUE)    			from PROGRESS_TASK_OS pt1    " + 
							"			       where pt1.status!=0 and pt1.TDHS_CONTROL_4A is not null     and pt1.TDHS_TCT_NOT_APPROVAL is null      and pt1.TTKV = pt.TTKV   " + 
							"			       and pt1.SOURCE_TASK=3    	");
					ifElseBaoCao(sql,obj,"pt1");
					sql.append("		group by pt1.TTKV),0) tdHshcNtdDoiSoat4aValue,   " + 
							"			 nvl((select sum(pt1.HSHC_VALUE)    			from PROGRESS_TASK_OS pt1    " + 
							"			       where pt1.status!=0 and pt1.TDHS_PHT_APPROVALING is not null    and pt1.TDHS_TCT_NOT_APPROVAL is null       and pt1.TTKV = pt.TTKV   " + 
							"			       and pt1.SOURCE_TASK=3    	");
					ifElseBaoCao(sql,obj,"pt1");
					sql.append("		group by pt1.TTKV),0) tdHshcNtdPhtThamDuyetValue,   " + 
							"			 nvl((select sum(pt1.HSHC_VALUE)    			from PROGRESS_TASK_OS pt1    " + 
							"			       where pt1.status!=0 and pt1.TDHS_PHT_ACCEPTANCING is not null    and pt1.TDHS_TCT_NOT_APPROVAL is null       and pt1.TTKV = pt.TTKV   " + 
							"			       and pt1.SOURCE_TASK=3    	");
					ifElseBaoCao(sql,obj,"pt1");
					sql.append("		group by pt1.TTKV),0) tdHshcNtdPhtNghiemThuValue,   " + 
							"			 nvl((select sum(pt1.HSHC_VALUE)    			from PROGRESS_TASK_OS pt1    " + 
							"			       where pt1.status!=0 and pt1.TDHS_TTKT_PROFILE is not null    and pt1.TDHS_TCT_NOT_APPROVAL is null       and pt1.TTKV = pt.TTKV   " + 
							"			       and pt1.SOURCE_TASK=3    	");
					ifElseBaoCao(sql,obj,"pt1");
					sql.append("		group by pt1.TTKV),0) tdHshcNtdDangLamHoSoValue,   " + 
							"			 nvl((select sum(pt1.HSHC_VALUE)    			from PROGRESS_TASK_OS pt1    " + 
							"			       where pt1.status!=0 and pt1.TDHS_EXPECTED_COMPLETE_DATE is not null    " + 
							"			       and pt1.TTKV = pt.TTKV          and pt1.SOURCE_TASK=3   ");
					ifElseBaoCao(sql,obj,"pt1");
					sql.append("			 			group by pt1.TTKV),0) tdHshcNtdDuKienHoanThanhValue,    " + 
							"			 null tdHshcNtdNguyenNhanKoHt   " + 
							"			 from PROGRESS_TASK_OS pt    left join (select sum(task.HSHC_VALUE) valueHshc, task.TTKV ttkv    " + 
							"			 from PROGRESS_TASK_OS task where task.STATUS!=0 and task.SOURCE_TASK=3   ");
					ifElseBaoCao(sql,obj,"task");
					sql.append("			 group by  task.TTKV) taskOs    on taskOs.ttkv = pt.TTKV    where 1=1  ");
					if (StringUtils.isNotBlank(obj.getMonthYear())) {
						sql.append(" AND pt.MONTH_YEAR = :monthYear ");
					}
					sql.append(" group by  pt.ttkv) ");
					
					sql.append(" select ttkv ttkv,"
							+ " tdHshcNtdKhThang tdHshcNtdKhThang,"
							+ " tdHshcNtdTrenDuongValue tdHshcNtdTrenDuongValue,"
							+ " (case when tdHshcNtdKhThang > 0 then (tdHshcNtdTrenDuongValue / tdHshcNtdKhThang  ) else 0 end) tdHshcNtdTrenDuongTyLe,"
							+ " tdHshcNtdGdCnKyValue tdHshcNtdGdCnKyValue,"
							+ " (case when tdHshcNtdKhThang > 0 then (tdHshcNtdGdCnKyValue / tdHshcNtdKhThang  ) else 0 end) tdHshcNtdGdCnKyTyLe,"
							+ " tdHshcNtdDoiSoat4aValue tdHshcNtdDoiSoat4aValue, "
							+ " (case when tdHshcNtdKhThang > 0 then (tdHshcNtdDoiSoat4aValue / tdHshcNtdKhThang  ) else 0 end) tdHshcNtdDoiSoat4aTyLe,"
							+ " tdHshcNtdPhtThamDuyetValue tdHshcNtdPhtThamDuyetValue,"
							+ " (case when tdHshcNtdKhThang > 0 then (tdHshcNtdPhtThamDuyetValue / tdHshcNtdKhThang  ) else 0 end) tdHshcNtdPhtThamDuyetTyLe,"
							+ " tdHshcNtdPhtNghiemThuValue tdHshcNtdPhtNghiemThuValue,"
							+ " (case when tdHshcNtdKhThang > 0 then (tdHshcNtdPhtNghiemThuValue / tdHshcNtdKhThang  ) else 0 end) tdHshcNtdPhtNghiemThuTyLe,"
							+ " tdHshcNtdDangLamHoSoValue tdHshcNtdDangLamHoSoValue,"
							+ " (case when tdHshcNtdKhThang > 0 then (tdHshcNtdDangLamHoSoValue / tdHshcNtdKhThang  ) else 0 end) tdHshcNtdDangLamHoSoTyLe,"
							+ " tdHshcNtdDuKienHoanThanhValue tdHshcNtdDuKienHoanThanhValue, "
							+ " (case when tdHshcNtdKhThang > 0 then (tdHshcNtdDuKienHoanThanhValue / tdHshcNtdKhThang  ) else 0 end) tdHshcNtdDuKienHoanThanhTyLe "
							+ " from tdHshcNtd ");
					
					SQLQuery query =  getSession().createSQLQuery(sql.toString());
					query.addScalar("ttkv", new StringType());
					query.addScalar("tdHshcNtdKhThang", new LongType());
					query.addScalar("tdHshcNtdTrenDuongValue", new LongType());
					query.addScalar("tdHshcNtdTrenDuongTyLe", new DoubleType());
					query.addScalar("tdHshcNtdGdCnKyValue", new LongType());
					query.addScalar("tdHshcNtdGdCnKyTyLe", new DoubleType());
					query.addScalar("tdHshcNtdDoiSoat4aValue", new LongType());
					query.addScalar("tdHshcNtdDoiSoat4aTyLe", new DoubleType());
					query.addScalar("tdHshcNtdPhtThamDuyetValue", new LongType());
					query.addScalar("tdHshcNtdPhtThamDuyetTyLe", new DoubleType());
					query.addScalar("tdHshcNtdPhtNghiemThuValue", new LongType());
					query.addScalar("tdHshcNtdPhtNghiemThuTyLe", new DoubleType());
					query.addScalar("tdHshcNtdDangLamHoSoValue", new LongType());
					query.addScalar("tdHshcNtdDangLamHoSoTyLe", new DoubleType());
					query.addScalar("tdHshcNtdDuKienHoanThanhValue", new LongType());
					query.addScalar("tdHshcNtdDuKienHoanThanhTyLe", new DoubleType());
					
					if (StringUtils.isNotBlank(obj.getMonthYear())) {
						query.setParameter("monthYear", obj.getMonthYear());
					}
					
					query.setResultTransformer(Transformers.aliasToBean(ProgressTaskOsDTO.class));
					
					return query.list();
				}
				
				//Sản lượng Xây dựng dân dụng
				public List<ProgressTaskOsDTO> getDataTienDoSlXddd(ProgressTaskOsDTO obj){
					StringBuilder sql = new StringBuilder("WITH tdSlXddd AS " + "  (SELECT pt.TTKV ttkv, "
							+ "    NVL(MAX(taskOs.valueQuantity),0) tdSlXdddKhThang, " 
							+ "    NVL( "
							+ "    (SELECT SUM(pt1.QUANTITY_VALUE) " + "    FROM PROGRESS_TASK_OS pt1 "
							+ "    WHERE pt1.status!                    =0 "
							+ "    AND pt1.TDSL_EXPECTED_COMPLETE_DATE IS NOT NULL "
							+ "    AND pt1.TTKV                         = pt.TTKV " + "    AND pt1.SOURCE_TASK                  =6 ");
					ifElseBaoCao(sql,obj,"pt1");
					sql.append("    GROUP BY pt1.TTKV " + "    ),0) tdSlXdddDaHoanThanh, " 
							+ "    NVL( "
							+ "    (SELECT SUM(pt1.QUANTITY_VALUE) " + "    FROM PROGRESS_TASK_OS pt1 "
							+ "    WHERE pt1.status!          =0 " + "    AND pt1.TDSL_CONSTRUCTING IS NOT NULL  AND pt1.TDSL_ACCOMPLISHED_DATE IS NULL "
							+ "    AND pt1.TTKV               = pt.TTKV " + "    AND pt1.SOURCE_TASK        =6 ");
					ifElseBaoCao(sql,obj,"pt1");
					sql.append("    GROUP BY pt1.TTKV " + "    ),0) tdSlXdddDangThiCongValue, " 
							+ "    NVL( "
							+ "    (SELECT SUM(pt1.QUANTITY_VALUE) " + "    FROM PROGRESS_TASK_OS pt1 "
							+ "    WHERE pt1.status!               =0 " + "    AND pt1.TDSL_EXPECTED_COMPLETE_DATE IS NOT NULL "
							+ "    AND pt1.TTKV                    = pt.TTKV " + "    AND pt1.SOURCE_TASK             =6 ");
					ifElseBaoCao(sql,obj,"pt1");
					sql.append("    GROUP BY pt1.TTKV " + "    ),0) tdSlXdddDuKienHoanThanhValue, " 
							+ "    NULL tdSlXdddNguyenNhanKoHt "
							+ "  FROM PROGRESS_TASK_OS pt " 
							+ "  LEFT JOIN "
							+ "    (SELECT SUM(task.QUANTITY_VALUE) valueQuantity, " + "      task.TTKV ttkv "
							+ "    FROM PROGRESS_TASK_OS task " + "    WHERE task.STATUS!  =0 " + "    AND task.SOURCE_TASK=6 ");
					ifElseBaoCao(sql,obj,"task");
					sql.append("    GROUP BY task.TTKV " + "    ) taskOs " + "  ON taskOs.TTKV = pt.TTKV "
							+ "  WHERE 1        =1  ");
					if (StringUtils.isNotBlank(obj.getMonthYear())) {
						sql.append(" AND pt.MONTH_YEAR = :monthYear ");
					}
					sql.append(" group by  pt.ttkv) ");
					
					sql.append(" select ttkv ttkv,"
							+ " tdSlXdddKhThang tdSlXdddKhThang,"
							+ " tdSlXdddDaHoanThanh tdSlXdddDaHoanThanh,"
							+ " (case when tdSlXdddKhThang > 0 then (tdSlXdddDaHoanThanh / tdSlXdddKhThang ) else 0 end) tdSlXdddDaHoanThanhTyLe,"
							+ " tdSlXdddDangThiCongValue tdSlXdddDangThiCongValue,"
							+ " (case when tdSlXdddKhThang > 0 then (tdSlXdddDangThiCongValue / tdSlXdddKhThang ) else 0 end) tdSlXdddDangThiCongTyLe,"
							+ " tdSlXdddDuKienHoanThanhValue tdSlXdddDuKienHoanThanhValue, "
							+ " (case when tdSlXdddKhThang > 0 then (tdSlXdddDuKienHoanThanhValue / tdSlXdddKhThang ) else 0 end) tdSlXdddDuKienHoanThanhTyLe "
							+ " from tdSlXddd ");
					
					SQLQuery query =  getSession().createSQLQuery(sql.toString());
					query.addScalar("ttkv", new StringType());
					query.addScalar("tdSlXdddKhThang", new LongType());
					query.addScalar("tdSlXdddDaHoanThanh", new LongType());
					query.addScalar("tdSlXdddDaHoanThanhTyLe", new DoubleType());
					query.addScalar("tdSlXdddDangThiCongValue", new LongType());
					query.addScalar("tdSlXdddDangThiCongTyLe", new DoubleType());
					query.addScalar("tdSlXdddDuKienHoanThanhValue", new LongType());
					query.addScalar("tdSlXdddDuKienHoanThanhTyLe", new DoubleType());
					
					if (StringUtils.isNotBlank(obj.getMonthYear())) {
						query.setParameter("monthYear", obj.getMonthYear());
					}
					
					query.setResultTransformer(Transformers.aliasToBean(ProgressTaskOsDTO.class));
					
					return query.list();
				}
				
				//HSHC Xây dựng dân dụng
				public List<ProgressTaskOsDTO> getDataTienDoHshcXddd(ProgressTaskOsDTO obj){
					StringBuilder sql = new StringBuilder("WITH tdHshcXddd as (select     pt.TTKV ttkv, " + 
							"			 nvl(MAX(taskOs.valueHshc),0) tdHshcXdddKhThang, " + 
							"			 nvl((select sum(pt1.HSHC_VALUE)    			from PROGRESS_TASK_OS pt1    " + 
							"			       where pt1.status!=0 and pt1.TDHS_TCT_NOT_APPROVAL is not null           and pt1.TTKV = pt.TTKV   " + 
							"			       and pt1.SOURCE_TASK=6    	");
					ifElseBaoCao(sql,obj,"pt1");
					sql.append("		group by pt1.TTKV),0) tdHshcXdddTrenDuongValue,   " + 
							"			 nvl((select sum(pt1.HSHC_VALUE)    			from PROGRESS_TASK_OS pt1    " + 
							"			       where pt1.status!=0 and pt1.TDHS_SIGNING_GDCN is not null   and pt1.TDHS_TCT_NOT_APPROVAL is null        and pt1.TTKV = pt.TTKV   " + 
							"			       and pt1.SOURCE_TASK=6    	");
					ifElseBaoCao(sql,obj,"pt1");
					sql.append("		group by pt1.TTKV),0) tdHshcXdddGdCnKyValue,   " + 
							"			 nvl((select sum(pt1.HSHC_VALUE)    			from PROGRESS_TASK_OS pt1    " + 
							"			       where pt1.status!=0 and pt1.TDHS_CONTROL_4A is not null     and pt1.TDHS_TCT_NOT_APPROVAL is null      and pt1.TTKV = pt.TTKV   " + 
							"			       and pt1.SOURCE_TASK=6    	");
					ifElseBaoCao(sql,obj,"pt1");
					sql.append("		group by pt1.TTKV),0) tdHshcXdddDoiSoat4aValue,   " + 
							"			 nvl((select sum(pt1.HSHC_VALUE)    			from PROGRESS_TASK_OS pt1    " + 
							"			       where pt1.status!=0 and pt1.TDHS_PHT_APPROVALING is not null    and pt1.TDHS_TCT_NOT_APPROVAL is null       and pt1.TTKV = pt.TTKV   " + 
							"			       and pt1.SOURCE_TASK=6    	");
					ifElseBaoCao(sql,obj,"pt1");
					sql.append("		group by pt1.TTKV),0) tdHshcXdddPhtThamDuyetValue,   " + 
							"			 nvl((select sum(pt1.HSHC_VALUE)    			from PROGRESS_TASK_OS pt1    " + 
							"			       where pt1.status!=0 and pt1.TDHS_PHT_ACCEPTANCING is not null    and pt1.TDHS_TCT_NOT_APPROVAL is null       and pt1.TTKV = pt.TTKV   " + 
							"			       and pt1.SOURCE_TASK=6    	");
					ifElseBaoCao(sql,obj,"pt1");
					sql.append("		group by pt1.TTKV),0) tdHshcXdddPhtNghiemThuValue,   " + 
							"			 nvl((select sum(pt1.HSHC_VALUE)    			from PROGRESS_TASK_OS pt1    " + 
							"			       where pt1.status!=0 and pt1.TDHS_TTKT_PROFILE is not null    and pt1.TDHS_TCT_NOT_APPROVAL is null       and pt1.TTKV = pt.TTKV   " + 
							"			       and pt1.SOURCE_TASK=6    	");
					ifElseBaoCao(sql,obj,"pt1");
					sql.append("		group by pt1.TTKV),0) tdHshcXdddDangLamHoSoValue,   " + 
							"			 nvl((select sum(pt1.HSHC_VALUE)    			from PROGRESS_TASK_OS pt1    " + 
							"			       where pt1.status!=0 and pt1.TDHS_EXPECTED_COMPLETE_DATE is not null    " + 
							"			       and pt1.TTKV = pt.TTKV          and pt1.SOURCE_TASK=6   ");
					ifElseBaoCao(sql,obj,"pt1");
					sql.append("			 			group by pt1.TTKV),0) tdHshcXdddDuKienHoanThanhValue,    " + 
							"			 null tdHshcXdddNguyenNhanKoHt   " + 
							"			 from PROGRESS_TASK_OS pt    left join (select sum(task.HSHC_VALUE) valueHshc, task.TTKV ttkv    " + 
							"			 from PROGRESS_TASK_OS task where task.STATUS!=0 and task.SOURCE_TASK=6   ");
					ifElseBaoCao(sql,obj,"task");
					sql.append("			 group by  task.TTKV) taskOs    on taskOs.ttkv = pt.TTKV    where 1=1  ");
					if (StringUtils.isNotBlank(obj.getMonthYear())) {
						sql.append(" AND pt.MONTH_YEAR = :monthYear ");
					}
					sql.append(" group by  pt.ttkv) ");
					
					sql.append(" select ttkv ttkv,"
							+ " tdHshcXdddKhThang tdHshcXdddKhThang,"
							+ " tdHshcXdddTrenDuongValue tdHshcXdddTrenDuongValue,"
							+ " (case when tdHshcXdddKhThang > 0 then (tdHshcXdddTrenDuongValue / tdHshcXdddKhThang  ) else 0 end) tdHshcXdddTrenDuongTyLe,"
							+ " tdHshcXdddGdCnKyValue tdHshcXdddGdCnKyValue,"
							+ " (case when tdHshcXdddKhThang > 0 then (tdHshcXdddGdCnKyValue / tdHshcXdddKhThang  ) else 0 end) tdHshcXdddGdCnKyTyLe,"
							+ " tdHshcXdddDoiSoat4aValue tdHshcXdddDoiSoat4aValue, "
							+ " (case when tdHshcXdddKhThang > 0 then (tdHshcXdddDoiSoat4aValue / tdHshcXdddKhThang  ) else 0 end) tdHshcXdddDoiSoat4aTyLe,"
							+ " tdHshcXdddPhtThamDuyetValue tdHshcXdddPhtThamDuyetValue,"
							+ " (case when tdHshcXdddKhThang > 0 then (tdHshcXdddPhtThamDuyetValue / tdHshcXdddKhThang  ) else 0 end) tdHshcXdddPhtThamDuyetTyLe,"
							+ " tdHshcXdddPhtNghiemThuValue tdHshcXdddPhtNghiemThuValue,"
							+ " (case when tdHshcXdddKhThang > 0 then (tdHshcXdddPhtNghiemThuValue / tdHshcXdddKhThang  ) else 0 end) tdHshcXdddPhtNghiemThuTyLe,"
							+ " tdHshcXdddDangLamHoSoValue tdHshcXdddDangLamHoSoValue,"
							+ " (case when tdHshcXdddKhThang > 0 then (tdHshcXdddDangLamHoSoValue / tdHshcXdddKhThang  ) else 0 end) tdHshcXdddDangLamHoSoTyLe,"
							+ " tdHshcXdddDuKienHoanThanhValue tdHshcXdddDuKienHoanThanhValue, "
							+ " (case when tdHshcXdddKhThang > 0 then (tdHshcXdddDuKienHoanThanhValue / tdHshcXdddKhThang  ) else 0 end) tdHshcXdddDuKienHoanThanhTyLe "
							+ " from tdHshcXddd ");
					
					SQLQuery query =  getSession().createSQLQuery(sql.toString());
					query.addScalar("ttkv", new StringType());
					query.addScalar("tdHshcXdddKhThang", new LongType());
					query.addScalar("tdHshcXdddTrenDuongValue", new LongType());
					query.addScalar("tdHshcXdddTrenDuongTyLe", new DoubleType());
					query.addScalar("tdHshcXdddGdCnKyValue", new LongType());
					query.addScalar("tdHshcXdddGdCnKyTyLe", new DoubleType());
					query.addScalar("tdHshcXdddDoiSoat4aValue", new LongType());
					query.addScalar("tdHshcXdddDoiSoat4aTyLe", new DoubleType());
					query.addScalar("tdHshcXdddPhtThamDuyetValue", new LongType());
					query.addScalar("tdHshcXdddPhtThamDuyetTyLe", new DoubleType());
					query.addScalar("tdHshcXdddPhtNghiemThuValue", new LongType());
					query.addScalar("tdHshcXdddPhtNghiemThuTyLe", new DoubleType());
					query.addScalar("tdHshcXdddDangLamHoSoValue", new LongType());
					query.addScalar("tdHshcXdddDangLamHoSoTyLe", new DoubleType());
					query.addScalar("tdHshcXdddDuKienHoanThanhValue", new LongType());
					query.addScalar("tdHshcXdddDuKienHoanThanhTyLe", new DoubleType());
					
					if (StringUtils.isNotBlank(obj.getMonthYear())) {
						query.setParameter("monthYear", obj.getMonthYear());
					}
					
					query.setResultTransformer(Transformers.aliasToBean(ProgressTaskOsDTO.class));
					
					return query.list();
				}
	// Huy-end
				
	//Huypq-29052020-start
	public List<RpProgressMonthPlanOsDTO> doSearchBaoCaoTienDoOs(ProgressTaskOsDTO obj){
		StringBuilder sql = new StringBuilder("select a.YEAR year, a.month month ,a.sysGroupName sysGroupName,a.sysGroupId sysGroupId, " + 
				"round((a.QUANTITY_XL_TARGET + a.QUANTITY_CP_TARGET+ a.QUANTITY_NTD_GPDN_TARGET+ a.QUANTITY_NTD_XDDD_TARGET)/1000000,2) quantity, " + 
				"round((a.quantity_XL_Complete+ a.quantity_CP_Complete+ a.quantity_NTD_GPDN_Complete+a.quantity_NTD_XDDD_Complete)/1000000,2) currentQuantity, " + 
				"nvl(ROUND(DECODE((a.QUANTITY_XL_TARGET + a.QUANTITY_CP_TARGET+ a.QUANTITY_NTD_GPDN_TARGET+ a.QUANTITY_NTD_XDDD_TARGET),0,0,100* (a.quantity_XL_Complete+ a.quantity_CP_Complete+ a.quantity_NTD_GPDN_Complete+a.quantity_NTD_XDDD_Complete)/ " + 
				"(a.QUANTITY_XL_TARGET + a.QUANTITY_CP_TARGET+ a.QUANTITY_NTD_GPDN_TARGET+ a.QUANTITY_NTD_XDDD_TARGET)),2),0) progressQuantity, " + 
				"round(a.quyluong_target/1000000,2) complete,round(a.quyluong_complete/1000000,2) currentComplete,a.process_quyluong progressComplete, " + 
				" " + 
				"round((a.HSHC_XL_TARGET+ a.REVENUE_CP_TARGET+a.revenue_NTDGPDN_Target+a.revenue_NTDXDDD_Target+ HSHC_HTCT_TARGET)/1000000,2)revenue, " + 
				"round((a.hshc_XL_Complete+a.revenue_CP_Complete+a.revenue_NTDGPDN_Complete+a.revenue_NTDXDDD_Complete+a.hshc_HTCT_Complete)/1000000,2)currentRevenueMonth, " + 
				" " + 
				"nvl(ROUND(DECODE((a.HSHC_XL_TARGET+ a.REVENUE_CP_TARGET+a.revenue_NTDGPDN_Target+a.revenue_NTDXDDD_Target+ HSHC_HTCT_TARGET),0,0,100* " + 
				" (a.hshc_XL_Complete+a.revenue_CP_Complete+a.revenue_NTDGPDN_Complete+a.revenue_NTDXDDD_Complete+a.hshc_HTCT_Complete)/ " + 
				" ((a.HSHC_XL_TARGET+ a.REVENUE_CP_TARGET+a.revenue_NTDGPDN_Target+a.revenue_NTDXDDD_Target+ HSHC_HTCT_TARGET))),2),0) progressRevenueMonth, " + 
				"  " + 
				" round(a.revokeCashTarget/1000000,2) revokeCashTarget,round(a.revokeCashComplete/1000000,2) revokeCashComplete,process_revokeCash processRevokeCash, " + 
				" round(a.revenue_not_approve/1000000,2) revenueNotApprove, " + 
				" nvl(ROUND(DECODE((a.HSHC_XL_TARGET+ a.REVENUE_CP_TARGET+a.revenue_NTDGPDN_Target+a.revenue_NTDXDDD_Target+ HSHC_HTCT_TARGET),0,0,100* " + 
				" (a.revenue_not_approve)/ " + 
				" ((a.HSHC_XL_TARGET+ a.REVENUE_CP_TARGET+a.revenue_NTDGPDN_Target+a.revenue_NTDXDDD_Target+ HSHC_HTCT_TARGET))),2),0) processRevenueNotApprove " + 
				"from report_process_month a " 
				+ " where 1=1 ");
		
		if(obj.getSysGroupId()!=null) {
			sql.append(" AND a.sysGroupId =:sysGroupId ");
		}
		
		if(StringUtils.isNotBlank(obj.getMonthYear())) {
			sql.append(" AND a.month = EXTRACT(MONTH FROM to_date(:monthYear,'MM/yyyy')) "
					+ " AND a.year = EXTRACT(YEAR FROM to_date(:monthYear,'MM/yyyy')) ");
		}
		
		sql.append(" order by a.month,a.sysGroupName ");
		
		StringBuilder sqlCount = new StringBuilder("select count(*) from (" + sql.toString() + ")");
		
		StringBuilder sqlQuerySum = new StringBuilder("SELECT sum(quantity) quantityTotal, "
				+ " sum(currentQuantity) currentQuantityTotal,"
        		+ " round(decode(sum(quantity),0,0,100* sum(currentQuantity)/sum(quantity)),2) progressQuantityTotal,"
        		+ " sum(complete) completeTotal,"
        		+ " sum(currentComplete) currentCompleteTotal,"
        		+ " round(decode(sum(COMPLETE),0,0,100* sum(CURRENTCOMPLETE)/sum(COMPLETE)),2) progressCompleteTotal,"
        		+ " sum(revenue) revenueTotal,"
        		+ " sum(currentRevenueMonth) currentRevenueTotal,"
        		+ " round(decode(sum(revenue),0,0,100* sum(currentRevenueMonth)/sum(revenue)),2) progressRevenueTotal, "
        		+ " sum(revokeCashTarget) revokeCashTargetTotal,"
        		+ " sum(revokeCashComplete) revokeCashCompleteTotal,"
        		+ " round(decode(sum(revokeCashTarget),0,0,100* sum(revokeCashComplete)/sum(revokeCashTarget)),2) processRevokeCashTotal, "
        		+ " sum(revenueNotApprove) revenueNotApproveTotal,"
        		+ " round(decode(sum(revenue),0,0,100* sum(revenueNotApprove)/sum(revenue)),2) processRevenueNotApproveTotal "
        		+ "  FROM (");
        sqlQuerySum.append(sql);
        sqlQuerySum.append(")");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
		SQLQuery querySum = getSession().createSQLQuery(sqlQuerySum.toString());
		
		query.addScalar("year", new StringType());
		query.addScalar("month", new StringType());
		query.addScalar("sysGroupName", new StringType());
		query.addScalar("sysGroupId", new LongType());
		query.addScalar("quantity", new DoubleType());
		query.addScalar("currentQuantity", new DoubleType());
		query.addScalar("progressQuantity", new DoubleType());
		query.addScalar("complete", new DoubleType());
		query.addScalar("currentComplete", new DoubleType());
		query.addScalar("progressComplete", new DoubleType());
		query.addScalar("revenue", new DoubleType());
		query.addScalar("currentRevenueMonth", new DoubleType());
		query.addScalar("progressRevenueMonth", new DoubleType());
		query.addScalar("revokeCashTarget", new DoubleType());
		query.addScalar("revokeCashComplete", new DoubleType());
		query.addScalar("processRevokeCash", new DoubleType());
		query.addScalar("revenueNotApprove", new DoubleType());
		query.addScalar("processRevenueNotApprove", new DoubleType());
		
		querySum.addScalar("quantityTotal", new DoubleType());
        querySum.addScalar("currentQuantityTotal", new DoubleType());
        querySum.addScalar("progressQuantityTotal", new DoubleType());
        querySum.addScalar("completeTotal", new DoubleType());
        querySum.addScalar("currentCompleteTotal", new DoubleType());
        querySum.addScalar("progressCompleteTotal", new DoubleType());
        querySum.addScalar("revenueTotal", new DoubleType());
        querySum.addScalar("currentRevenueTotal", new DoubleType());
        querySum.addScalar("progressRevenueTotal", new DoubleType());
        querySum.addScalar("revokeCashTargetTotal", new DoubleType());
        querySum.addScalar("revokeCashCompleteTotal", new DoubleType());
        querySum.addScalar("processRevokeCashTotal", new DoubleType());
        querySum.addScalar("revenueNotApproveTotal", new DoubleType());
        querySum.addScalar("processRevenueNotApproveTotal", new DoubleType());
		
		if(obj.getSysGroupId()!=null) {
			query.setParameter("sysGroupId", obj.getSysGroupId());
			queryCount.setParameter("sysGroupId", obj.getSysGroupId());
			querySum.setParameter("sysGroupId", obj.getSysGroupId());
		}
		
		if(StringUtils.isNotBlank(obj.getMonthYear())) {
			query.setParameter("monthYear", obj.getMonthYear());
			queryCount.setParameter("monthYear", obj.getMonthYear());
			querySum.setParameter("monthYear", obj.getMonthYear());
		}
		
		query.setResultTransformer(Transformers.aliasToBean(RpProgressMonthPlanOsDTO.class));
		
		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
		
		List<RpProgressMonthPlanOsDTO> lst = query.list();
		
		if (lst.size() > 0) {
        	List<Object[]> rs = querySum.list();
        	for (Object[] objects : rs) {
        		lst.get(0).setQuantityTotal((Double) objects[0]);
        		lst.get(0).setCurrentQuantityTotal((Double) objects[1]);
            	lst.get(0).setProgressQuantityTotal((Double) objects[2]);
            	lst.get(0).setCompleteTotal((Double) objects[3]);
            	lst.get(0).setCurrentCompleteTotal((Double) objects[4]);
            	lst.get(0).setProgressCompleteTotal((Double) objects[5]);
            	lst.get(0).setRevenueTotal((Double) objects[6]);
            	lst.get(0).setCurrentRevenueTotal((Double) objects[7]);
            	lst.get(0).setProgressRevenueTotal((Double) objects[8]);
            	lst.get(0).setRevokeCashTargetTotal((Double) objects[9]);
            	lst.get(0).setRevokeCashCompleteTotal((Double) objects[10]);
            	lst.get(0).setProcessRevokeCashTotal((Double) objects[11]);
            	lst.get(0).setRevenueNotApproveTotal((Double) objects[12]);
            	lst.get(0).setProcessRevenueNotApproveTotal((Double) objects[13]);
        	}
        }
		
		return lst;
	}
	
	//Báo cáo KH tháng OS
	public List<RpProgressMonthPlanOsDTO> getDataExportFileBaoCaoKHOs(ProgressTaskOsDTO obj){
		StringBuilder sql =new StringBuilder("select  " + 
				"YEAR year, " + 
				"month month, " + 
				"month ||'/'|| year monthYear, " + 
				"sysGroupName sysGroupName, " + 
				"sysGroupId sysGroupId, " + 
				"round(quyluong_target/1000000,2) quyLuongTarget , " + 
				"round(quyluong_complete/1000000,2) quyLuongComplete, " + 
				"process_quyluong processQuyLuong, " + 
				"round(hshc_XL_Target/1000000,2) hshcXlTarget, " + 
				"round(hshc_XL_Complete/1000000,2) hshcXlComplete, " + 
				"process_hshc_XL processHshcXl, " + 
				"round(revenue_CP_Target/1000000,2) revenueCpTarget, " + 
				"round(revenue_CP_Complete/1000000,2) revenueCpComplete, " + 
				"process_revenue_CP processRevenueCp,  " + 
				"round(revenue_NTDGPDN_Target/1000000,2) revenueNtdgpdnTarget, " + 
				"round(revenue_NTDGPDN_Complete/1000000,2) revenueNtdgpdnComplete, " + 
				"process_revenue_NTDGPDN processRevenueNtdgpdn, " + 
				"round(revenue_NTDXDDD_Target/1000000,2) revenueNtdxdddTarget,  " + 
				"round(revenue_NTDXDDD_Complete/1000000,2) revenueNtdxdddComplete, " + 
				"process_revenue_NTDXDDD processRevenueNtdxddd, " + 
				"round(hshc_HTCT_Target/1000000,2) hshcHtctTarget, " + 
				"round(hshc_HTCT_Complete/1000000,2) hshcHtctComplete,  " + 
				"process_hshc_HTCT processHshcHtct, " + 
				"round(quantity_XL_Target/1000000,2) quantityXlTarget, " + 
				"round(quantity_XL_Complete/1000000,2) quantityXlComplete, " + 
				"process_quantity_XL processQuantityXl, " + 
				"round(quantity_CP_Target/1000000,2) quantityCpTarget, " + 
				"round(quantity_CP_Complete/1000000,2) quantityCpComplete, " + 
				"process_quantity_CP processQuantityCp, " + 
				"round(quantity_NTD_GPDN_Target/1000000,2) quantityNtdGpdnTarget, " + 
				"round(quantity_NTD_GPDN_Complete/1000000,2) quantityNtdGpdnComplete ,  " + 
				"process_quantity_NTD_GPDN processQuantityNtdGpdn, " + 
				"round(quantity_NTD_XDDD_Target/1000000,2) quantityNtdXdddTarget, " + 
				"round(quantity_NTD_XDDD_Complete/1000000,2) quantityNtdXdddComplete,  " + 
				"process_quantity_NTD_XDDD processQuantityNtdXddd, " + 
				"round(TASK_XDDD_TARGET/1000000,2) taskXdddTarget, " + 
				"round(TASK_XDDD_COMPLETE/1000000,2) taskXdddComplete, " + 
				"process_TASK_XDDD processTaskXddd,  " + 
				"round(revokeCashTarget/1000000,2) revokeCashTarget, " + 
				"round(revokeCashComplete/1000000,2) revokeCashComplete, " + 
				"process_revokeCash processRevokeCash, " + 
				"SUM_DEPLOY_HTCT_TARGET sumDeployHtctTarget, " + 
				"SUM_DEPLOY_HTCT_COMPLETE sumDeployHtctComplete ,  " + 
				"process_SUM_DEPLOY_HTCT processSumDeployHtct, " + 
				"MONG_HTCT_TARGET mongHtctTarget, " + 
				"MONG_HTCT_COMPLETE mongHtctComplete, " + 
				"process_MONG_HTCT processMongHtct,  " + 
				"DB_HTCT_TARGET dbHtctTarget , " + 
				"DB_HTCT_COMPLETE dbHtctComplete , " + 
				"process_DB_HTCT processDbHtct, " + 
				"RENT_HTCT_TARGET rentHtctTarget, " + 
				"RENT_HTCT_COMPLETE rentHtctComplete, " + 
				"process_RENT_HTCT processRentHtct				 " + 
				"from report_process_month " + 
				"where 1=1 ");
		
		if(obj.getSysGroupId()!=null) {
			sql.append(" AND sysGroupId =:sysGroupId ");
		}
		
		if(StringUtils.isNotBlank(obj.getMonthYear())) {
			sql.append(" AND month = EXTRACT(MONTH FROM to_date(:monthYear,'MM/yyyy')) "
					+ " AND year = EXTRACT(YEAR FROM to_date(:monthYear,'MM/yyyy')) ");
		}
		
		sql.append(" ORDER BY sysGroupName asc ");
		
		StringBuilder sqlCount = new StringBuilder("select count(*) from (" + sql.toString() + ")");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

		query.addScalar("year", new StringType());
		query.addScalar("month", new StringType());
		query.addScalar("monthYear", new StringType());
		query.addScalar("sysGroupName", new StringType());
		query.addScalar("sysGroupId", new LongType());
		query.addScalar("quyLuongTarget", new DoubleType());
		query.addScalar("quyLuongComplete", new DoubleType());
		query.addScalar("processQuyLuong", new DoubleType());
		query.addScalar("hshcXlTarget", new DoubleType());
		query.addScalar("hshcXlComplete", new DoubleType());
		query.addScalar("processHshcXl", new DoubleType());
		query.addScalar("revenueCpTarget", new DoubleType());
		query.addScalar("revenueCpComplete", new DoubleType());
		query.addScalar("processRevenueCp", new DoubleType());
		query.addScalar("revenueNtdgpdnTarget", new DoubleType());
		query.addScalar("revenueNtdgpdnComplete", new DoubleType());
		query.addScalar("processRevenueNtdgpdn", new DoubleType());
		query.addScalar("revenueNtdxdddTarget", new DoubleType());
		query.addScalar("revenueNtdxdddComplete", new DoubleType());
		query.addScalar("processRevenueNtdxddd", new DoubleType());
		query.addScalar("hshcHtctTarget", new DoubleType());
		query.addScalar("hshcHtctComplete", new DoubleType());
		query.addScalar("processHshcHtct", new DoubleType());
		query.addScalar("quantityXlTarget", new DoubleType());
		query.addScalar("quantityXlComplete", new DoubleType());
		query.addScalar("processQuantityXl", new DoubleType());
		query.addScalar("quantityCpTarget", new DoubleType());
		query.addScalar("quantityCpComplete", new DoubleType());
		query.addScalar("processQuantityCp", new DoubleType());
		query.addScalar("quantityNtdGpdnTarget", new DoubleType());
		query.addScalar("quantityNtdGpdnComplete", new DoubleType());
		query.addScalar("processQuantityNtdGpdn", new DoubleType());
		query.addScalar("quantityNtdXdddTarget", new DoubleType());
		query.addScalar("quantityNtdXdddComplete", new DoubleType());
		query.addScalar("processQuantityNtdXddd", new DoubleType());
		query.addScalar("taskXdddTarget", new DoubleType());
		query.addScalar("taskXdddComplete", new DoubleType());
		query.addScalar("processTaskXddd", new DoubleType());
		query.addScalar("revokeCashTarget", new DoubleType());
		query.addScalar("revokeCashComplete", new DoubleType());
		query.addScalar("processRevokeCash", new DoubleType());
		query.addScalar("sumDeployHtctTarget", new DoubleType());
		query.addScalar("sumDeployHtctComplete", new DoubleType());
		query.addScalar("processSumDeployHtct", new DoubleType());
		query.addScalar("mongHtctTarget", new DoubleType());
		query.addScalar("mongHtctComplete", new DoubleType());
		query.addScalar("processMongHtct", new DoubleType());
		query.addScalar("dbHtctTarget", new DoubleType());
		query.addScalar("dbHtctComplete", new DoubleType());
		query.addScalar("processDbHtct", new DoubleType());
		query.addScalar("rentHtctTarget", new DoubleType());
		query.addScalar("rentHtctComplete", new DoubleType());
		query.addScalar("processRentHtct", new DoubleType());
		if(obj.getSysGroupId()!=null) {
			query.setParameter("sysGroupId", obj.getSysGroupId());
			queryCount.setParameter("sysGroupId", obj.getSysGroupId());
		}
		
		if(StringUtils.isNotBlank(obj.getMonthYear())) {
			query.setParameter("monthYear", obj.getMonthYear());
			queryCount.setParameter("monthYear", obj.getMonthYear());
		}
		
		query.setResultTransformer(Transformers.aliasToBean(RpProgressMonthPlanOsDTO.class));
		
		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
		
		return query.list();
	}
	
	//Báo cáo chấm KPI
	public List<RpProgressMonthPlanOsDTO> doSearchBaoCaoChamDiemKpi(ProgressTaskOsDTO obj){
		StringBuilder sql = new StringBuilder("with tbl as( " + 
				"select  " + 
				"YEAR,month,sysGroupName,sysGroupId , " + 
				"quyluong_target ,quyluong_complete,process_quyluong, " + 
				"(case when quyluong_TARGET>0 or quyluong_complete > 0 then 1 else 0 end) diemdat_quyluong,0 diemthuong_quyluong, " + 
				"hshc_XL_Target , hshc_XL_Complete,process_hshc_XL, " + 
				" " + 
				"(case when MONG_HTCT_TARGET = 0 and  MONG_HTCT_COMPLETE = 0 and DB_HTCT_TARGET = 0 and DB_HTCT_COMPLETE=0 then 0.5 else 0 end)+ " + 
				"(case when RENT_HTCT_TARGET =0 and RENT_HTCT_COMPLETE = 0 then 1 else 0 end)+ " + 
				"(case when revenue_NTDXDDD_TARGET=0 and revenue_NTDXDDD_COMPLETE = 0 then 1 else 0 end)+ " + 
				"(case when REVENUE_CP_TARGET=0 and REVENUE_CP_COMPLETE = 0 then 1 else 0 end)+  " + 
				"(case when hshc_XL_TARGET >0 or hshc_XL_Complete > 0 then 2.5 else 0 end)diemdat_hsch_XL, " + 
				" " + 
				"(case when REVENUE_CP_TARGET=0 and REVENUE_CP_COMPLETE = 0 then 0.25 else 0 end)+  " + 
				"(case when hshc_XL_TARGET >0 or hshc_XL_COMPLETE > 0 then 0.25 else 0 end)diemthuong_hshc_XL, " + 
				" " + 
				"revenue_CP_Target,revenue_CP_Complete ,process_revenue_CP ,  " + 
				" " + 
				"(case when MONG_HTCT_TARGET = 0 and  MONG_HTCT_COMPLETE = 0 and DB_HTCT_TARGET = 0 and DB_HTCT_COMPLETE=0 then 0.5 else 0 end)+ " + 
				"(case when RENT_HTCT_TARGET =0 and RENT_HTCT_COMPLETE = 0 then 1 else 0 end)+ " + 
				"(case when hshc_XL_TARGET =0 and hshc_XL_COMPLETE = 0 then 1.5 else 0 end)+ " + 
				"(case when revenue_CP_Target >0 or revenue_CP_Complete > 0 then 1 else 0 end) " + 
				" diemdat_revenue_CP, " + 
				" " + 
				"(case when hshc_XL_TARGET=0 and hshc_XL_COMPLETE = 0 then 0.15 else 0 end)+  " + 
				"(case when revenue_CP_TARGET >0 or revenue_CP_COMPLETE > 0 then 0.25 else 0 end)diemthuong_revenue_CP, " + 
				" " + 
				"revenue_NTDXDDD_Target, revenue_NTDXDDD_Complete,process_revenue_NTDXDDD , " + 
				" " + 
				"(case when hshc_XL_TARGET=0 and hshc_XL_COMPLETE = 0 then 1 else 0 end)+ " + 
				"(case when revenue_NTDXDDD_TARGET>0 or revenue_NTDXDDD_COMPLETE > 0 then 1 else 0 end) diemdat_revenue_NTDXDDD, " + 
				" " + 
				"(case when hshc_XL_TARGET=0 and hshc_XL_COMPLETE = 0 then 0.1 else 0 end) diemthuong_revenue_NTDXDDD, " + 
				" " + 
				"quantity_XL_Target,quantity_XL_Complete ,process_quantity_XL , " + 
				"(case when revokeCashTARGET=0 and revokeCashCOMPLETE = 0 then 0.5 else 0 end)+ " + 
				"(case when quantity_NTD_XDDD_TARGET=0 and quantity_NTD_XDDD_COMPLETE = 0 then 1.5 else 0 end)+ " + 
				"(case when quantity_XL_TARGET >0 or quantity_XL_COMPLETE > 0 then 1.5 else 0 end) diemdat_quantity_XL, " + 
				" " + 
				"(case when revokeCashTARGET = 0 and revokeCashCOMPLETE = 0 then 0.083 else 0 end) diemthuong_quantity_XL, " + 
				"quantity_CP_Target, quantity_CP_Complete,process_quantity_CP, " + 
				"(case when revokeCashTARGET = 0 and revokeCashCOMPLETE = 0 then 0.5 else 0 end)+ " + 
				"(case when quantity_XL_TARGET >0 or quantity_XL_COMPLETE > 0 then 1 else 0 end) diemdat_quantity_CP, " + 
				" " + 
				"(case when revokeCashTARGET =0 and revokeCashCOMPLETE = 0 then 0.083 else 0 end) diemthuong_quantity_CP, " + 
				"quantity_NTD_XDDD_Target,quantity_NTD_XDDD_Complete , process_quantity_NTD_XDDD , " + 
				"(case when revokeCashTARGET=0 and revokeCashCOMPLETE = 0 then 0.5 else 0 end)+ " + 
				"(case when quantity_XL_TARGET >0 or quantity_XL_COMPLETE > 0 then 1.5 else 0 end) diemdat_quantity_NTD_XDDD, " + 
				" " + 
				"(case when revokeCashTARGET=0 and revokeCashCOMPLETE = 0 then 0.083 else 0 end) diemthuong_quantity_NTD_XDDD, " + 
				"TASK_XDDD_TARGET ,TASK_XDDD_COMPLETE,process_TASK_XDDD,  " + 
				" " + 
				"(case when MONG_HTCT_TARGET = 0 and  MONG_HTCT_COMPLETE = 0 and DB_HTCT_TARGET = 0 and DB_HTCT_COMPLETE=0 then 1 else 0 end)+ " + 
				"(case when TASK_XDDD_TARGET >0 or TASK_XDDD_COMPLETE > 0 then 1 else 0 end) diemdat_TASK_XDDD, " + 
				" " + 
				"(case when TASK_XDDD_TARGET >0 or TASK_XDDD_COMPLETE > 0 then 0.25 else 0 end) diemthuong_TASK_XDDD, " + 
				" revokeCashTarget,revokeCashComplete,process_revokeCash , " + 
				"  " + 
				"(case when revokeCashTARGET >0 or revokeCashCOMPLETE > 0 then 1.5 else 0 end) diemdat_revokeCash, " + 
				" " + 
				"(case when revokeCashTARGET >0 or revokeCashCOMPLETE > 0 then 0.25 else 0 end) diemthuong_revokeCash, " + 
				"RENT_HTCT_TARGET,RENT_HTCT_COMPLETE,process_RENT_HTCT, " + 
				"(case when RENT_HTCT_TARGET >0 or RENT_HTCT_COMPLETE > 0 then 2 else 0 end) diemdat_RENT_HTCT,0 diemthuong_RENT_HTCT, " + 
				"MONG_HTCT_TARGET,MONG_HTCT_COMPLETE,process_MONG_HTCT,  " + 
				"(case when DB_HTCT_TARGET=0 and DB_HTCT_COMPLETE = 0 then 1 else 0 end)+ " + 
				"(case when MONG_HTCT_TARGET >0 or MONG_HTCT_COMPLETE > 0 then 1 else 0 end) diemdat_MONG_HTCT,0 diemthuong_MONG_HTCT, " + 
				"DB_HTCT_TARGET ,DB_HTCT_COMPLETE ,process_DB_HTCT , " + 
				"(case when MONG_HTCT_TARGET =0 and  MONG_HTCT_COMPLETE = 0 then 1 else 0 end)+ " + 
				"(case when DB_HTCT_TARGET >0 or DB_HTCT_COMPLETE > 0 then 1 else 0 end) diemdat_DB_HTCT,0 diemthuong_DB_HTCT " + 
				"from report_process_month a where 1=1 "); 
				if(StringUtils.isNotBlank(obj.getMonthYear())) {
					sql.append("AND a.month = EXTRACT(MONTH FROM to_date(:monthYear,'MM/yyyy')) "
							+ " and a.year = EXTRACT(YEAR FROM to_date(:monthYear,'MM/yyyy')) "); 
				}
				sql.append("), " + 
				"tblReport as( select  " + 
				"'1' type, " + 
				"YEAR,month,sysGroupName,sysGroupId ,to_char(b.AREA_CODE) AREA_CODE,to_char(b.PROVINCE_CODE)PROVINCE_CODE, " + 
				"to_number(quyluong_target)quyluong_target ,to_number(quyluong_complete)quyluong_complete,to_number(process_quyluong)process_quyluong, " + 
				"case when process_quyluong < 100 then diemdat_quyluong* process_quyluong/100 else diemdat_quyluong end diemdat_quyluong, diemthuong_quyluong, " + 
				"to_number(hshc_XL_Target) hshc_XL_Target, to_number(hshc_XL_Complete)hshc_XL_Complete,to_number(process_hshc_XL)process_hshc_XL, " + 
				"case when process_hshc_XL < 100 then diemdat_hsch_XL* process_hshc_XL/100 else diemdat_hsch_XL end diemdat_hsch_XL, " + 
				"case when process_hshc_XL >=100 and process_hshc_XL < 120 then (process_hshc_XL-100)*diemthuong_hshc_XL/20 " + 
				"when process_hshc_XL >=120 then diemthuong_hshc_XL else 0 end diemthuong_hshc_XL, " + 
				"to_number(revenue_CP_Target)revenue_CP_Target,to_number(revenue_CP_Complete)revenue_CP_Complete ,to_number(process_revenue_CP)process_revenue_CP ,  " + 
				"case when process_revenue_CP < 100 then diemdat_revenue_CP* process_revenue_CP/100 else diemdat_revenue_CP end diemdat_revenue_CP, " + 
				"case when process_revenue_CP >=100 and process_revenue_CP < 120 then (process_revenue_CP-100)*diemthuong_revenue_CP/20 " + 
				"when process_revenue_CP >=120 then diemthuong_revenue_CP else 0 end diemthuong_revenue_CP, " + 
				"to_number(revenue_NTDXDDD_Target)revenue_NTDXDDD_Target, to_number(revenue_NTDXDDD_Complete)revenue_NTDXDDD_Complete,to_number(process_revenue_NTDXDDD)process_revenue_NTDXDDD , " + 
				"case when process_revenue_NTDXDDD < 100 then diemdat_revenue_NTDXDDD* process_revenue_NTDXDDD/100 else diemdat_revenue_NTDXDDD end diemdat_revenue_NTDXDDD, " + 
				"case when process_revenue_NTDXDDD >=100 and process_revenue_NTDXDDD < 120 then (process_revenue_NTDXDDD-100)*diemthuong_revenue_NTDXDDD/20 " + 
				"when process_revenue_NTDXDDD >=120 then diemthuong_revenue_NTDXDDD else 0 end diemthuong_revenue_NTDXDDD, " + 
				"to_number(quantity_XL_Target)quantity_XL_Target,to_number(quantity_XL_Complete) quantity_XL_Complete,to_number(process_quantity_XL) process_quantity_XL, " + 
				"case when process_quantity_XL < 100 then diemdat_quantity_XL* process_quantity_XL/100 else diemdat_quantity_XL end diemdat_quantity_XL, " + 
				"case when process_revenue_NTDXDDD >=100 and process_quantity_XL < 120 then (process_quantity_XL-100)*diemthuong_quantity_XL/20 " + 
				"when process_quantity_XL >=120 then diemthuong_quantity_XL else 0 end diemthuong_quantity_XL, " + 
				"to_number(quantity_CP_Target)quantity_CP_Target, to_number(quantity_CP_Complete)quantity_CP_Complete,to_number(process_quantity_CP)process_quantity_CP, " + 
				"case when process_quantity_CP < 100 then diemdat_quantity_CP* process_quantity_CP/100 else diemdat_quantity_CP end diemdat_quantity_CP, " + 
				"case when process_revenue_NTDXDDD >=100 and process_quantity_CP < 120 then (process_quantity_CP-100)*diemthuong_quantity_CP/20 " + 
				"when process_quantity_CP >=120 then diemthuong_quantity_CP else 0 end diemthuong_quantity_CP, " + 
				"to_number(quantity_NTD_XDDD_Target)quantity_NTD_XDDD_Target,to_number(quantity_NTD_XDDD_Complete) quantity_NTD_XDDD_Complete, to_number(process_quantity_NTD_XDDD)process_quantity_NTD_XDDD , " + 
				"case when process_quantity_NTD_XDDD < 100 then diemdat_quantity_NTD_XDDD* process_quantity_NTD_XDDD/100 else diemdat_quantity_NTD_XDDD end diemdat_quantity_NTD_XDDD, " + 
				"case when process_revenue_NTDXDDD >=100 and process_quantity_NTD_XDDD < 120 then (process_quantity_NTD_XDDD-100)*diemthuong_quantity_NTD_XDDD/20 " + 
				"when process_quantity_NTD_XDDD >=120 then diemthuong_quantity_NTD_XDDD else 0 end diemthuong_quantity_NTD_XDDD, " + 
				"to_number(TASK_XDDD_TARGET) TASK_XDDD_TARGET,to_number(TASK_XDDD_COMPLETE)TASK_XDDD_COMPLETE,to_number(process_TASK_XDDD)process_TASK_XDDD,  " + 
				"case when process_TASK_XDDD < 100 then diemdat_TASK_XDDD* process_TASK_XDDD/100 else diemdat_TASK_XDDD end diemdat_TASK_XDDD, " + 
				"case when process_revenue_NTDXDDD >=100 and process_TASK_XDDD < 120 then (process_TASK_XDDD-100)*diemthuong_TASK_XDDD/20 " + 
				"when process_TASK_XDDD >=120 then diemthuong_TASK_XDDD else 0 end diemthuong_TASK_XDDD, " + 
				"to_number(revokeCashTarget)revokeCashTarget,to_number(revokeCashComplete)revokeCashComplete,to_number(process_revokeCash) process_revokeCash, " + 
				"case when process_revokeCash < 100 then diemdat_revokeCash* process_revokeCash/100 else diemdat_revokeCash end diemdat_revokeCash, " + 
				"case when process_revenue_NTDXDDD >=100 and process_revokeCash < 120 then (process_revokeCash-100)*diemthuong_revokeCash/20 " + 
				"when process_revokeCash >=120 then diemthuong_revokeCash else 0 end diemthuong_revokeCash, " + 
				"to_number(RENT_HTCT_TARGET)RENT_HTCT_TARGET,to_number(RENT_HTCT_COMPLETE)RENT_HTCT_COMPLETE,to_number(process_RENT_HTCT)process_RENT_HTCT, " + 
				"case when process_RENT_HTCT < 100 then diemdat_RENT_HTCT* process_RENT_HTCT/100 else diemdat_RENT_HTCT end diemdat_RENT_HTCT, " + 
				"case when process_revenue_NTDXDDD >=100 and process_RENT_HTCT < 120 then (process_RENT_HTCT-100)*diemthuong_RENT_HTCT/20 " + 
				"when process_RENT_HTCT >=120 then diemthuong_RENT_HTCT else 0 end diemthuong_RENT_HTCT, " + 
				"to_number(MONG_HTCT_TARGET)MONG_HTCT_TARGET,to_number(MONG_HTCT_COMPLETE)MONG_HTCT_COMPLETE,to_number(process_MONG_HTCT)process_MONG_HTCT,  " + 
				"case when process_MONG_HTCT < 100 then diemdat_MONG_HTCT* process_MONG_HTCT/100 else diemdat_MONG_HTCT end diemdat_MONG_HTCT, " + 
				"case when process_revenue_NTDXDDD >=100 and process_MONG_HTCT < 120 then (process_MONG_HTCT-100)*diemthuong_MONG_HTCT/20 " + 
				"when process_MONG_HTCT >=120 then diemthuong_MONG_HTCT else 0 end diemthuong_MONG_HTCT, " + 
				"to_number(DB_HTCT_TARGET) DB_HTCT_TARGET,to_number(DB_HTCT_COMPLETE)DB_HTCT_COMPLETE ,to_number(process_DB_HTCT)process_DB_HTCT , " + 
				"case when process_DB_HTCT < 100 then diemdat_DB_HTCT* process_DB_HTCT/100 else diemdat_DB_HTCT end diemdat_DB_HTCT, " + 
				"case when process_revenue_NTDXDDD >=100 and process_DB_HTCT < 120 then (process_DB_HTCT-100)*diemthuong_DB_HTCT/20 " + 
				"when process_DB_HTCT >=120 then diemthuong_DB_HTCT else 0 end diemthuong_DB_HTCT " + 
				" from tbl a,sys_group b where a.sysGroupId=b.sys_group_id " + 
//				" --muc kv " + 
				" union all " + 
				"  select  " + 
				"'2' type, " + 
				"YEAR,month,null sysGroupName,null sysGroupId ,to_char(b.AREA_CODE) AREA_CODE,to_char(b.AREA_CODE) PROVINCE_CODE, " + 
				"sum(quyluong_target)quyluong_target ,sum(quyluong_complete)quyluong_complete, " + 
				"round(decode(sum(quyluong_target),0,0,100* sum(quyluong_complete)/sum(quyluong_target)),2)process_quyluong, " + 
				"round(case when decode(sum(quyluong_target),0,0,100* sum(quyluong_complete)/sum(quyluong_target)) < 100 then sum(diemdat_quyluong)* decode(sum(quyluong_target),0,0,100* sum(quyluong_complete)/sum(quyluong_target))/100 else sum(diemdat_quyluong) end,2) diemdat_quyluong, sum(diemthuong_quyluong)diemthuong_quyluong, " + 
				" " + 
				"sum(hshc_XL_Target) hshc_XL_Target, sum(hshc_XL_Complete) hshc_XL_Complete, " + 
				"round(decode(sum(hshc_XL_Target),0,0,100* sum(hshc_XL_Complete)/sum(hshc_XL_Target)),2)process_hshc_XL, " + 
				"round(case when decode(sum(hshc_XL_Target),0,0,100* sum(hshc_XL_Complete)/sum(hshc_XL_Target)) < 100 then sum(diemdat_hsch_XL)* decode(sum(hshc_XL_Target),0,0,100* sum(hshc_XL_Complete)/sum(hshc_XL_Target))/100 else sum(diemdat_hsch_XL) end,2) diemdat_hsch_XL, " + 
				"round(case when decode(sum(hshc_XL_Target),0,0,100* sum(hshc_XL_Complete)/sum(hshc_XL_Target)) >=100 and decode(sum(hshc_XL_Target),0,0,100* sum(hshc_XL_Complete)/sum(hshc_XL_Target)) < 120 then ((decode(sum(hshc_XL_Target),0,0,100* sum(hshc_XL_Complete)/sum(hshc_XL_Target)))-100)*sum(diemthuong_hshc_XL)/20 " + 
				"when decode(sum(hshc_XL_Target),0,0,100* sum(hshc_XL_Complete)/sum(hshc_XL_Target)) >=120 then sum(diemthuong_hshc_XL) else 0 end,2) diemthuong_hshc_XL, " + 
				" " + 
				"sum(revenue_CP_Target)revenue_CP_Target,sum(revenue_CP_Complete)revenue_CP_Complete , " + 
				"round(decode(sum(revenue_CP_Target),0,0,100* sum(revenue_CP_Complete)/sum(revenue_CP_Target)),2)process_revenue_CP ,  " + 
				"round(case when decode(sum(revenue_CP_Target),0,0,100* sum(revenue_CP_Complete)/sum(revenue_CP_Target)) < 100 then sum(diemdat_revenue_CP)* (decode(sum(revenue_CP_Target),0,0,100* sum(revenue_CP_Complete)/sum(revenue_CP_Target)))/100 else sum(diemdat_revenue_CP) end,2) diemdat_revenue_CP, " + 
				"round(case when decode(sum(revenue_CP_Target),0,0,100* sum(revenue_CP_Complete)/sum(revenue_CP_Target)) >=100 and decode(sum(revenue_CP_Target),0,0,100* sum(revenue_CP_Complete)/sum(revenue_CP_Target)) < 120 then (decode(sum(revenue_CP_Target),0,0,100* sum(revenue_CP_Complete)/sum(revenue_CP_Target))-100)*sum(diemthuong_revenue_CP)/20 " + 
				"when decode(sum(revenue_CP_Target),0,0,100* sum(revenue_CP_Complete)/sum(revenue_CP_Target)) >=120 then sum(diemthuong_revenue_CP) else 0 end,2) diemthuong_revenue_CP, " + 
				" " + 
				"sum(revenue_NTDXDDD_Target)revenue_NTDXDDD_Target, sum(revenue_NTDXDDD_Complete)revenue_NTDXDDD_Complete, " + 
				"round(decode(sum(revenue_NTDXDDD_Target),0,0,100* sum(revenue_NTDXDDD_Complete)/sum(revenue_NTDXDDD_Target)),2) process_revenue_NTDXDDD , " + 
				"round(case when decode(sum(revenue_NTDXDDD_Target),0,0,100* sum(revenue_NTDXDDD_Complete)/sum(revenue_NTDXDDD_Target)) < 100 then sum(diemdat_revenue_NTDXDDD)* decode(sum(revenue_NTDXDDD_Target),0,0,100* sum(revenue_NTDXDDD_Complete)/sum(revenue_NTDXDDD_Target))/100 else sum(diemdat_revenue_NTDXDDD) end,2) diemdat_revenue_NTDXDDD, " + 
				"round(case when decode(sum(revenue_NTDXDDD_Target),0,0,100* sum(revenue_NTDXDDD_Complete)/sum(revenue_NTDXDDD_Target)) >=100 and decode(sum(revenue_NTDXDDD_Target),0,0,100* sum(revenue_NTDXDDD_Complete)/sum(revenue_NTDXDDD_Target)) < 120 then (decode(sum(revenue_NTDXDDD_Target),0,0,100* sum(revenue_NTDXDDD_Complete)/sum(revenue_NTDXDDD_Target))-100)*sum(diemthuong_revenue_NTDXDDD)/20 " + 
				"when decode(sum(revenue_NTDXDDD_Target),0,0,100* sum(revenue_NTDXDDD_Complete)/sum(revenue_NTDXDDD_Target)) >=120 then sum(diemthuong_revenue_NTDXDDD) else 0 end ,2)diemthuong_revenue_NTDXDDD, " + 
				" " + 
				"sum(quantity_XL_Target)quantity_XL_Target,sum(quantity_XL_Complete)quantity_XL_Complete , " + 
				"round(decode(sum(quantity_XL_Target),0,0,100* sum(quantity_XL_Complete)/sum(quantity_XL_Target)),2)process_quantity_XL , " + 
				"round(case when decode(sum(quantity_XL_Target),0,0,100* sum(quantity_XL_Complete)/sum(quantity_XL_Target)) < 100 then sum(diemdat_quantity_XL)* decode(sum(quantity_XL_Target),0,0,100* sum(quantity_XL_Complete)/sum(quantity_XL_Target))/100 else sum(diemdat_quantity_XL) end,2) diemdat_quantity_XL, " + 
				"round(case when decode(sum(quantity_XL_Target),0,0,100* sum(quantity_XL_Complete)/sum(quantity_XL_Target)) >=100 and decode(sum(quantity_XL_Target),0,0,100* sum(quantity_XL_Complete)/sum(quantity_XL_Target)) < 120 then (decode(sum(quantity_XL_Target),0,0,100* sum(quantity_XL_Complete)/sum(quantity_XL_Target))-100)*sum(diemthuong_quantity_XL)/20 " + 
				"when decode(sum(quantity_XL_Target),0,0,100* sum(quantity_XL_Complete)/sum(quantity_XL_Target)) >=120 then sum(diemthuong_quantity_XL) else 0 end,2) diemthuong_quantity_XL, " + 
				" " + 
				"sum(quantity_CP_Target)quantity_CP_Target, sum(quantity_CP_Complete)quantity_CP_Complete, " + 
				"round(decode(sum(quantity_CP_Target),0,0,100* sum(quantity_CP_Complete)/sum(quantity_CP_Target)),2)process_quantity_CP, " + 
				"round(case when decode(sum(quantity_CP_Target),0,0,100* sum(quantity_CP_Complete)/sum(quantity_CP_Target)) < 100 then sum(diemdat_quantity_CP)* decode(sum(quantity_CP_Target),0,0,100* sum(quantity_CP_Complete)/sum(quantity_CP_Target))/100 else sum(diemdat_quantity_CP) end,2) diemdat_quantity_CP, " + 
				"round(case when decode(sum(quantity_CP_Target),0,0,100* sum(quantity_CP_Complete)/sum(quantity_CP_Target)) >=100 and decode(sum(quantity_CP_Target),0,0,100* sum(quantity_CP_Complete)/sum(quantity_CP_Target)) < 120 then (decode(sum(quantity_CP_Target),0,0,100* sum(quantity_CP_Complete)/sum(quantity_CP_Target))-100)*sum(diemthuong_quantity_CP)/20 " + 
				"when decode(sum(quantity_CP_Target),0,0,100* sum(quantity_CP_Complete)/sum(quantity_CP_Target)) >=120 then sum(diemthuong_quantity_CP) else 0 end,2) diemthuong_quantity_CP, " + 
				" " + 
				"sum(quantity_NTD_XDDD_Target)quantity_NTD_XDDD_Target,sum(quantity_NTD_XDDD_Complete)quantity_NTD_XDDD_Complete ,  " + 
				"round(decode(sum(quantity_NTD_XDDD_Target),0,0,100* sum(quantity_NTD_XDDD_Complete)/sum(quantity_NTD_XDDD_Target)),2) process_quantity_NTD_XDDD , " + 
				"round(case when decode(sum(quantity_NTD_XDDD_Target),0,0,100* sum(quantity_NTD_XDDD_Complete)/sum(quantity_NTD_XDDD_Target)) < 100 then sum(diemdat_quantity_NTD_XDDD)* decode(sum(quantity_NTD_XDDD_Target),0,0,100* sum(quantity_NTD_XDDD_Complete)/sum(quantity_NTD_XDDD_Target))/100 else sum(diemdat_quantity_NTD_XDDD) end,2) diemdat_quantity_NTD_XDDD, " + 
				"round(case when decode(sum(quantity_NTD_XDDD_Target),0,0,100* sum(quantity_NTD_XDDD_Complete)/sum(quantity_NTD_XDDD_Target)) >=100 and decode(sum(quantity_NTD_XDDD_Target),0,0,100* sum(quantity_NTD_XDDD_Complete)/sum(quantity_NTD_XDDD_Target)) < 120 then (decode(sum(quantity_NTD_XDDD_Target),0,0,100* sum(quantity_NTD_XDDD_Complete)/sum(quantity_NTD_XDDD_Target))-100)* sum(diemthuong_quantity_NTD_XDDD)/20 " + 
				"when decode(sum(quantity_NTD_XDDD_Target),0,0,100* sum(quantity_NTD_XDDD_Complete)/sum(quantity_NTD_XDDD_Target)) >=120 then sum(diemthuong_quantity_NTD_XDDD) else 0 end ,2)diemthuong_quantity_NTD_XDDD, " + 
				" " + 
				"sum(TASK_XDDD_TARGET)TASK_XDDD_TARGET ,sum(TASK_XDDD_COMPLETE)TASK_XDDD_COMPLETE, " + 
				"round(decode(sum(TASK_XDDD_TARGET),0,0,100* sum(TASK_XDDD_COMPLETE)/sum(TASK_XDDD_TARGET)),2)process_TASK_XDDD,  " + 
				"round(case when decode(sum(TASK_XDDD_TARGET),0,0,100* sum(TASK_XDDD_COMPLETE)/sum(TASK_XDDD_TARGET)) < 100 then sum(diemdat_TASK_XDDD)* decode(sum(TASK_XDDD_TARGET),0,0,100* sum(TASK_XDDD_COMPLETE)/sum(TASK_XDDD_TARGET))/100 else sum(diemdat_TASK_XDDD) end,2) diemdat_TASK_XDDD, " + 
				"round(case when decode(sum(TASK_XDDD_TARGET),0,0,100* sum(TASK_XDDD_COMPLETE)/sum(TASK_XDDD_TARGET)) >=100 and decode(sum(TASK_XDDD_TARGET),0,0,100* sum(TASK_XDDD_COMPLETE)/sum(TASK_XDDD_TARGET)) < 120 then (decode(sum(TASK_XDDD_TARGET),0,0,100* sum(TASK_XDDD_COMPLETE)/sum(TASK_XDDD_TARGET))-100)*sum(diemthuong_TASK_XDDD)/20 " + 
				"when decode(sum(TASK_XDDD_TARGET),0,0,100* sum(TASK_XDDD_COMPLETE)/sum(TASK_XDDD_TARGET)) >=120 then sum(diemthuong_TASK_XDDD) else 0 end,2) diemthuong_TASK_XDDD, " + 
				" " + 
				"sum(revokeCashTarget)revokeCashTarget,sum(revokeCashComplete)revokeCashComplete, " + 
				"round(decode(sum(revokeCashTarget),0,0,100* sum(revokeCashComplete)/sum(revokeCashTarget)),2)process_revokeCash , " + 
				"round(case when decode(sum(revokeCashTarget),0,0,100* sum(revokeCashComplete)/sum(revokeCashTarget)) < 100 then sum(diemdat_revokeCash)* decode(sum(revokeCashTarget),0,0,100* sum(revokeCashComplete)/sum(revokeCashTarget))/100 else sum(diemdat_revokeCash) end,2) diemdat_revokeCash, " + 
				"round(case when decode(sum(revokeCashTarget),0,0,100* sum(revokeCashComplete)/sum(revokeCashTarget)) >=100 and decode(sum(revokeCashTarget),0,0,100* sum(revokeCashComplete)/sum(revokeCashTarget)) < 120 then (decode(sum(revokeCashTarget),0,0,100* sum(revokeCashComplete)/sum(revokeCashTarget))-100)*sum(diemthuong_revokeCash)/20 " + 
				"when decode(sum(revokeCashTarget),0,0,100* sum(revokeCashComplete)/sum(revokeCashTarget)) >=120 then sum(diemthuong_revokeCash) else 0 end ,2)diemthuong_revokeCash, " + 
				" " + 
				"sum(RENT_HTCT_TARGET)RENT_HTCT_TARGET,sum(RENT_HTCT_COMPLETE)RENT_HTCT_COMPLETE, " + 
				"round(decode(sum(RENT_HTCT_TARGET),0,0,100* sum(RENT_HTCT_COMPLETE)/sum(RENT_HTCT_TARGET)),2)process_RENT_HTCT, " + 
				"round(case when decode(sum(RENT_HTCT_TARGET),0,0,100* sum(RENT_HTCT_COMPLETE)/sum(RENT_HTCT_TARGET)) < 100 then sum(diemdat_RENT_HTCT)* decode(sum(RENT_HTCT_TARGET),0,0,100* sum(RENT_HTCT_COMPLETE)/sum(RENT_HTCT_TARGET))/100 else sum(diemdat_RENT_HTCT) end,2) diemdat_RENT_HTCT, " + 
				"round(case when decode(sum(RENT_HTCT_TARGET),0,0,100* sum(RENT_HTCT_COMPLETE)/sum(RENT_HTCT_TARGET)) >=100 and decode(sum(RENT_HTCT_TARGET),0,0,100* sum(RENT_HTCT_COMPLETE)/sum(RENT_HTCT_TARGET)) < 120 then (decode(sum(RENT_HTCT_TARGET),0,0,100* sum(RENT_HTCT_COMPLETE)/sum(RENT_HTCT_TARGET))-100)* sum(diemthuong_RENT_HTCT)/20 " + 
				"when decode(sum(RENT_HTCT_TARGET),0,0,100* sum(RENT_HTCT_COMPLETE)/sum(RENT_HTCT_TARGET)) >=120 then sum(diemthuong_RENT_HTCT) else 0 end,2) diemthuong_RENT_HTCT, " + 
				" " + 
				"sum(MONG_HTCT_TARGET)MONG_HTCT_TARGET,sum(MONG_HTCT_COMPLETE)MONG_HTCT_COMPLETE, " + 
				"round(decode(sum(MONG_HTCT_TARGET),0,0,100* sum(MONG_HTCT_COMPLETE)/sum(MONG_HTCT_TARGET)),2)process_MONG_HTCT,  " + 
				"round(case when decode(sum(MONG_HTCT_TARGET),0,0,100* sum(MONG_HTCT_COMPLETE)/sum(MONG_HTCT_TARGET)) < 100 then sum(diemdat_MONG_HTCT)* decode(sum(MONG_HTCT_TARGET),0,0,100* sum(MONG_HTCT_COMPLETE)/sum(MONG_HTCT_TARGET))/100 else sum(diemdat_MONG_HTCT) end,2) diemdat_MONG_HTCT, " + 
				"round(case when decode(sum(MONG_HTCT_TARGET),0,0,100* sum(MONG_HTCT_COMPLETE)/sum(MONG_HTCT_TARGET)) >=100 and decode(sum(MONG_HTCT_TARGET),0,0,100* sum(MONG_HTCT_COMPLETE)/sum(MONG_HTCT_TARGET)) < 120 then (decode(sum(MONG_HTCT_TARGET),0,0,100* sum(MONG_HTCT_COMPLETE)/sum(MONG_HTCT_TARGET))-100)* sum(diemthuong_MONG_HTCT)/20 " + 
				"when decode(sum(MONG_HTCT_TARGET),0,0,100* sum(MONG_HTCT_COMPLETE)/sum(MONG_HTCT_TARGET)) >=120 then sum(diemthuong_MONG_HTCT) else 0 end,2) diemthuong_MONG_HTCT, " + 
				" " + 
				"sum(DB_HTCT_TARGET)DB_HTCT_TARGET ,sum(DB_HTCT_COMPLETE)DB_HTCT_COMPLETE , " + 
				"round(decode(sum(DB_HTCT_TARGET),0,0,100* sum(DB_HTCT_COMPLETE)/sum(DB_HTCT_TARGET)),2) process_DB_HTCT , " + 
				"round(case when decode(sum(DB_HTCT_TARGET),0,0,100* sum(DB_HTCT_COMPLETE)/sum(DB_HTCT_TARGET)) < 100 then sum(diemdat_DB_HTCT)* decode(sum(DB_HTCT_TARGET),0,0,100* sum(DB_HTCT_COMPLETE)/sum(DB_HTCT_TARGET))/100 else sum(diemdat_DB_HTCT) end,2) diemdat_DB_HTCT, " + 
				" " + 
				"round(case when decode(sum(DB_HTCT_TARGET),0,0,100* sum(DB_HTCT_COMPLETE)/sum(DB_HTCT_TARGET)) >=100 and decode(sum(DB_HTCT_TARGET),0,0,100* sum(DB_HTCT_COMPLETE)/sum(DB_HTCT_TARGET)) < 120 then (decode(sum(DB_HTCT_TARGET),0,0,100* sum(DB_HTCT_COMPLETE)/sum(DB_HTCT_TARGET))-100)* sum(diemthuong_DB_HTCT)/20 " + 
				"when decode(sum(DB_HTCT_TARGET),0,0,100* sum(DB_HTCT_COMPLETE)/sum(DB_HTCT_TARGET)) >=120 then sum(diemthuong_DB_HTCT) else 0 end,2) diemthuong_DB_HTCT " + 
				" from tbl a,sys_group b where a.sysGroupId=b.sys_group_id " + 
				" group by YEAR,month,b.AREA_CODE " + 
//				"  --muc tq " + 
				" union all " + 
				"  select  " + 
				"'3' type, " + 
				"YEAR,month,null sysGroupName,null sysGroupId ,'Toàn quốc' AREA_CODE,'Toàn quốc' PROVINCE_CODE, " + 
				"sum(quyluong_target)quyluong_target ,sum(quyluong_complete)quyluong_complete, " + 
				"round(decode(sum(quyluong_target),0,0,100* sum(quyluong_complete)/sum(quyluong_target)),2)process_quyluong, " + 
				"round(case when decode(sum(quyluong_target),0,0,100* sum(quyluong_complete)/sum(quyluong_target)) < 100 then sum(diemdat_quyluong)* decode(sum(quyluong_target),0,0,100* sum(quyluong_complete)/sum(quyluong_target))/100 else sum(diemdat_quyluong) end,2) diemdat_quyluong, sum(diemthuong_quyluong)diemthuong_quyluong, " + 
				" " + 
				"sum(hshc_XL_Target) hshc_XL_Target, sum(hshc_XL_Complete) hshc_XL_Complete, " + 
				"round(decode(sum(hshc_XL_Target),0,0,100* sum(hshc_XL_Complete)/sum(hshc_XL_Target)),2)process_hshc_XL, " + 
				"round(case when decode(sum(hshc_XL_Target),0,0,100* sum(hshc_XL_Complete)/sum(hshc_XL_Target)) < 100 then sum(diemdat_hsch_XL)* decode(sum(hshc_XL_Target),0,0,100* sum(hshc_XL_Complete)/sum(hshc_XL_Target))/100 else sum(diemdat_hsch_XL) end,2) diemdat_hsch_XL, " + 
				"round(case when decode(sum(hshc_XL_Target),0,0,100* sum(hshc_XL_Complete)/sum(hshc_XL_Target)) >=100 and decode(sum(hshc_XL_Target),0,0,100* sum(hshc_XL_Complete)/sum(hshc_XL_Target)) < 120 then ((decode(sum(hshc_XL_Target),0,0,100* sum(hshc_XL_Complete)/sum(hshc_XL_Target)))-100)*sum(diemthuong_hshc_XL)/20 " + 
				"when decode(sum(hshc_XL_Target),0,0,100* sum(hshc_XL_Complete)/sum(hshc_XL_Target)) >=120 then sum(diemthuong_hshc_XL) else 0 end,2) diemthuong_hshc_XL, " + 
				" " + 
				"sum(revenue_CP_Target)revenue_CP_Target,sum(revenue_CP_Complete)revenue_CP_Complete , " + 
				"round(decode(sum(revenue_CP_Target),0,0,100* sum(revenue_CP_Complete)/sum(revenue_CP_Target)),2)process_revenue_CP ,  " + 
				"round(case when decode(sum(revenue_CP_Target),0,0,100* sum(revenue_CP_Complete)/sum(revenue_CP_Target)) < 100 then sum(diemdat_revenue_CP)* (decode(sum(revenue_CP_Target),0,0,100* sum(revenue_CP_Complete)/sum(revenue_CP_Target)))/100 else sum(diemdat_revenue_CP) end,2) diemdat_revenue_CP, " + 
				"round(case when decode(sum(revenue_CP_Target),0,0,100* sum(revenue_CP_Complete)/sum(revenue_CP_Target)) >=100 and decode(sum(revenue_CP_Target),0,0,100* sum(revenue_CP_Complete)/sum(revenue_CP_Target)) < 120 then (decode(sum(revenue_CP_Target),0,0,100* sum(revenue_CP_Complete)/sum(revenue_CP_Target))-100)*sum(diemthuong_revenue_CP)/20 " + 
				"when decode(sum(revenue_CP_Target),0,0,100* sum(revenue_CP_Complete)/sum(revenue_CP_Target)) >=120 then sum(diemthuong_revenue_CP) else 0 end,2) diemthuong_revenue_CP, " + 
				" " + 
				"sum(revenue_NTDXDDD_Target)revenue_NTDXDDD_Target, sum(revenue_NTDXDDD_Complete)revenue_NTDXDDD_Complete, " + 
				"round(decode(sum(revenue_NTDXDDD_Target),0,0,100* sum(revenue_NTDXDDD_Complete)/sum(revenue_NTDXDDD_Target)),2) process_revenue_NTDXDDD , " + 
				"round(case when decode(sum(revenue_NTDXDDD_Target),0,0,100* sum(revenue_NTDXDDD_Complete)/sum(revenue_NTDXDDD_Target)) < 100 then sum(diemdat_revenue_NTDXDDD)* decode(sum(revenue_NTDXDDD_Target),0,0,100* sum(revenue_NTDXDDD_Complete)/sum(revenue_NTDXDDD_Target))/100 else sum(diemdat_revenue_NTDXDDD) end,2) diemdat_revenue_NTDXDDD, " + 
				"round(case when decode(sum(revenue_NTDXDDD_Target),0,0,100* sum(revenue_NTDXDDD_Complete)/sum(revenue_NTDXDDD_Target)) >=100 and decode(sum(revenue_NTDXDDD_Target),0,0,100* sum(revenue_NTDXDDD_Complete)/sum(revenue_NTDXDDD_Target)) < 120 then (decode(sum(revenue_NTDXDDD_Target),0,0,100* sum(revenue_NTDXDDD_Complete)/sum(revenue_NTDXDDD_Target))-100)*sum(diemthuong_revenue_NTDXDDD)/20 " + 
				"when decode(sum(revenue_NTDXDDD_Target),0,0,100* sum(revenue_NTDXDDD_Complete)/sum(revenue_NTDXDDD_Target)) >=120 then sum(diemthuong_revenue_NTDXDDD) else 0 end ,2)diemthuong_revenue_NTDXDDD, " + 
				" " + 
				"sum(quantity_XL_Target)quantity_XL_Target,sum(quantity_XL_Complete)quantity_XL_Complete , " + 
				"round(decode(sum(quantity_XL_Target),0,0,100* sum(quantity_XL_Complete)/sum(quantity_XL_Target)),2)process_quantity_XL , " + 
				"round(case when decode(sum(quantity_XL_Target),0,0,100* sum(quantity_XL_Complete)/sum(quantity_XL_Target)) < 100 then sum(diemdat_quantity_XL)* decode(sum(quantity_XL_Target),0,0,100* sum(quantity_XL_Complete)/sum(quantity_XL_Target))/100 else sum(diemdat_quantity_XL) end,2) diemdat_quantity_XL, " + 
				"round(case when decode(sum(quantity_XL_Target),0,0,100* sum(quantity_XL_Complete)/sum(quantity_XL_Target)) >=100 and decode(sum(quantity_XL_Target),0,0,100* sum(quantity_XL_Complete)/sum(quantity_XL_Target)) < 120 then (decode(sum(quantity_XL_Target),0,0,100* sum(quantity_XL_Complete)/sum(quantity_XL_Target))-100)*sum(diemthuong_quantity_XL)/20 " + 
				"when decode(sum(quantity_XL_Target),0,0,100* sum(quantity_XL_Complete)/sum(quantity_XL_Target)) >=120 then sum(diemthuong_quantity_XL) else 0 end,2) diemthuong_quantity_XL, " + 
				" " + 
				"sum(quantity_CP_Target)quantity_CP_Target, sum(quantity_CP_Complete)quantity_CP_Complete, " + 
				"round(decode(sum(quantity_CP_Target),0,0,100* sum(quantity_CP_Complete)/sum(quantity_CP_Target)),2)process_quantity_CP, " + 
				"round(case when decode(sum(quantity_CP_Target),0,0,100* sum(quantity_CP_Complete)/sum(quantity_CP_Target)) < 100 then sum(diemdat_quantity_CP)* decode(sum(quantity_CP_Target),0,0,100* sum(quantity_CP_Complete)/sum(quantity_CP_Target))/100 else sum(diemdat_quantity_CP) end,2) diemdat_quantity_CP, " + 
				"round(case when decode(sum(quantity_CP_Target),0,0,100* sum(quantity_CP_Complete)/sum(quantity_CP_Target)) >=100 and decode(sum(quantity_CP_Target),0,0,100* sum(quantity_CP_Complete)/sum(quantity_CP_Target)) < 120 then (decode(sum(quantity_CP_Target),0,0,100* sum(quantity_CP_Complete)/sum(quantity_CP_Target))-100)*sum(diemthuong_quantity_CP)/20 " + 
				"when decode(sum(quantity_CP_Target),0,0,100* sum(quantity_CP_Complete)/sum(quantity_CP_Target)) >=120 then sum(diemthuong_quantity_CP) else 0 end,2) diemthuong_quantity_CP, " + 
				" " + 
				"sum(quantity_NTD_XDDD_Target)quantity_NTD_XDDD_Target,sum(quantity_NTD_XDDD_Complete)quantity_NTD_XDDD_Complete ,  " + 
				"round(decode(sum(quantity_NTD_XDDD_Target),0,0,100* sum(quantity_NTD_XDDD_Complete)/sum(quantity_NTD_XDDD_Target)),2) process_quantity_NTD_XDDD , " + 
				"round(case when decode(sum(quantity_NTD_XDDD_Target),0,0,100* sum(quantity_NTD_XDDD_Complete)/sum(quantity_NTD_XDDD_Target)) < 100 then sum(diemdat_quantity_NTD_XDDD)* decode(sum(quantity_NTD_XDDD_Target),0,0,100* sum(quantity_NTD_XDDD_Complete)/sum(quantity_NTD_XDDD_Target))/100 else sum(diemdat_quantity_NTD_XDDD) end,2) diemdat_quantity_NTD_XDDD, " + 
				"round(case when decode(sum(quantity_NTD_XDDD_Target),0,0,100* sum(quantity_NTD_XDDD_Complete)/sum(quantity_NTD_XDDD_Target)) >=100 and decode(sum(quantity_NTD_XDDD_Target),0,0,100* sum(quantity_NTD_XDDD_Complete)/sum(quantity_NTD_XDDD_Target)) < 120 then (decode(sum(quantity_NTD_XDDD_Target),0,0,100* sum(quantity_NTD_XDDD_Complete)/sum(quantity_NTD_XDDD_Target))-100)* sum(diemthuong_quantity_NTD_XDDD)/20 " + 
				"when decode(sum(quantity_NTD_XDDD_Target),0,0,100* sum(quantity_NTD_XDDD_Complete)/sum(quantity_NTD_XDDD_Target)) >=120 then sum(diemthuong_quantity_NTD_XDDD) else 0 end ,2)diemthuong_quantity_NTD_XDDD, " + 
				" " + 
				"sum(TASK_XDDD_TARGET)TASK_XDDD_TARGET ,sum(TASK_XDDD_COMPLETE)TASK_XDDD_COMPLETE, " + 
				"round(decode(sum(TASK_XDDD_TARGET),0,0,100* sum(TASK_XDDD_COMPLETE)/sum(TASK_XDDD_TARGET)),2)process_TASK_XDDD,  " + 
				"round(case when decode(sum(TASK_XDDD_TARGET),0,0,100* sum(TASK_XDDD_COMPLETE)/sum(TASK_XDDD_TARGET)) < 100 then sum(diemdat_TASK_XDDD)* decode(sum(TASK_XDDD_TARGET),0,0,100* sum(TASK_XDDD_COMPLETE)/sum(TASK_XDDD_TARGET))/100 else sum(diemdat_TASK_XDDD) end,2) diemdat_TASK_XDDD, " + 
				"round(case when decode(sum(TASK_XDDD_TARGET),0,0,100* sum(TASK_XDDD_COMPLETE)/sum(TASK_XDDD_TARGET)) >=100 and decode(sum(TASK_XDDD_TARGET),0,0,100* sum(TASK_XDDD_COMPLETE)/sum(TASK_XDDD_TARGET)) < 120 then (decode(sum(TASK_XDDD_TARGET),0,0,100* sum(TASK_XDDD_COMPLETE)/sum(TASK_XDDD_TARGET))-100)*sum(diemthuong_TASK_XDDD)/20 " + 
				"when decode(sum(TASK_XDDD_TARGET),0,0,100* sum(TASK_XDDD_COMPLETE)/sum(TASK_XDDD_TARGET)) >=120 then sum(diemthuong_TASK_XDDD) else 0 end,2) diemthuong_TASK_XDDD, " + 
				" " + 
				"sum(revokeCashTarget)revokeCashTarget,sum(revokeCashComplete)revokeCashComplete, " + 
				"round(decode(sum(revokeCashTarget),0,0,100* sum(revokeCashComplete)/sum(revokeCashTarget)),2)process_revokeCash , " + 
				"round(case when decode(sum(revokeCashTarget),0,0,100* sum(revokeCashComplete)/sum(revokeCashTarget)) < 100 then sum(diemdat_revokeCash)* decode(sum(revokeCashTarget),0,0,100* sum(revokeCashComplete)/sum(revokeCashTarget))/100 else sum(diemdat_revokeCash) end,2) diemdat_revokeCash, " + 
				"round(case when decode(sum(revokeCashTarget),0,0,100* sum(revokeCashComplete)/sum(revokeCashTarget)) >=100 and decode(sum(revokeCashTarget),0,0,100* sum(revokeCashComplete)/sum(revokeCashTarget)) < 120 then (decode(sum(revokeCashTarget),0,0,100* sum(revokeCashComplete)/sum(revokeCashTarget))-100)*sum(diemthuong_revokeCash)/20 " + 
				"when decode(sum(revokeCashTarget),0,0,100* sum(revokeCashComplete)/sum(revokeCashTarget)) >=120 then sum(diemthuong_revokeCash) else 0 end ,2)diemthuong_revokeCash, " + 
				" " + 
				"sum(RENT_HTCT_TARGET)RENT_HTCT_TARGET,sum(RENT_HTCT_COMPLETE)RENT_HTCT_COMPLETE, " + 
				"round(decode(sum(RENT_HTCT_TARGET),0,0,100* sum(RENT_HTCT_COMPLETE)/sum(RENT_HTCT_TARGET)),2)process_RENT_HTCT, " + 
				"round(case when decode(sum(RENT_HTCT_TARGET),0,0,100* sum(RENT_HTCT_COMPLETE)/sum(RENT_HTCT_TARGET)) < 100 then sum(diemdat_RENT_HTCT)* decode(sum(RENT_HTCT_TARGET),0,0,100* sum(RENT_HTCT_COMPLETE)/sum(RENT_HTCT_TARGET))/100 else sum(diemdat_RENT_HTCT) end,2) diemdat_RENT_HTCT, " + 
				"round(case when decode(sum(RENT_HTCT_TARGET),0,0,100* sum(RENT_HTCT_COMPLETE)/sum(RENT_HTCT_TARGET)) >=100 and decode(sum(RENT_HTCT_TARGET),0,0,100* sum(RENT_HTCT_COMPLETE)/sum(RENT_HTCT_TARGET)) < 120 then (decode(sum(RENT_HTCT_TARGET),0,0,100* sum(RENT_HTCT_COMPLETE)/sum(RENT_HTCT_TARGET))-100)* sum(diemthuong_RENT_HTCT)/20 " + 
				"when decode(sum(RENT_HTCT_TARGET),0,0,100* sum(RENT_HTCT_COMPLETE)/sum(RENT_HTCT_TARGET)) >=120 then sum(diemthuong_RENT_HTCT) else 0 end,2) diemthuong_RENT_HTCT, " + 
				" " + 
				"sum(MONG_HTCT_TARGET)MONG_HTCT_TARGET,sum(MONG_HTCT_COMPLETE)MONG_HTCT_COMPLETE, " + 
				"round(decode(sum(MONG_HTCT_TARGET),0,0,100* sum(MONG_HTCT_COMPLETE)/sum(MONG_HTCT_TARGET)),2)process_MONG_HTCT,  " + 
				"round(case when decode(sum(MONG_HTCT_TARGET),0,0,100* sum(MONG_HTCT_COMPLETE)/sum(MONG_HTCT_TARGET)) < 100 then sum(diemdat_MONG_HTCT)* decode(sum(MONG_HTCT_TARGET),0,0,100* sum(MONG_HTCT_COMPLETE)/sum(MONG_HTCT_TARGET))/100 else sum(diemdat_MONG_HTCT) end,2) diemdat_MONG_HTCT, " + 
				"round(case when decode(sum(MONG_HTCT_TARGET),0,0,100* sum(MONG_HTCT_COMPLETE)/sum(MONG_HTCT_TARGET)) >=100 and decode(sum(MONG_HTCT_TARGET),0,0,100* sum(MONG_HTCT_COMPLETE)/sum(MONG_HTCT_TARGET)) < 120 then (decode(sum(MONG_HTCT_TARGET),0,0,100* sum(MONG_HTCT_COMPLETE)/sum(MONG_HTCT_TARGET))-100)* sum(diemthuong_MONG_HTCT)/20 " + 
				"when decode(sum(MONG_HTCT_TARGET),0,0,100* sum(MONG_HTCT_COMPLETE)/sum(MONG_HTCT_TARGET)) >=120 then sum(diemthuong_MONG_HTCT) else 0 end,2) diemthuong_MONG_HTCT, " + 
				" " + 
				"sum(DB_HTCT_TARGET)DB_HTCT_TARGET ,sum(DB_HTCT_COMPLETE)DB_HTCT_COMPLETE , " + 
				"round(decode(sum(DB_HTCT_TARGET),0,0,100* sum(DB_HTCT_COMPLETE)/sum(DB_HTCT_TARGET)),2) process_DB_HTCT , " + 
				"round(case when decode(sum(DB_HTCT_TARGET),0,0,100* sum(DB_HTCT_COMPLETE)/sum(DB_HTCT_TARGET)) < 100 then sum(diemdat_DB_HTCT)* decode(sum(DB_HTCT_TARGET),0,0,100* sum(DB_HTCT_COMPLETE)/sum(DB_HTCT_TARGET))/100 else sum(diemdat_DB_HTCT) end,2) diemdat_DB_HTCT, " + 
				" " + 
				"round(case when decode(sum(DB_HTCT_TARGET),0,0,100* sum(DB_HTCT_COMPLETE)/sum(DB_HTCT_TARGET)) >=100 and decode(sum(DB_HTCT_TARGET),0,0,100* sum(DB_HTCT_COMPLETE)/sum(DB_HTCT_TARGET)) < 120 then (decode(sum(DB_HTCT_TARGET),0,0,100* sum(DB_HTCT_COMPLETE)/sum(DB_HTCT_TARGET))-100)* sum(diemthuong_DB_HTCT)/20 " + 
				"when decode(sum(DB_HTCT_TARGET),0,0,100* sum(DB_HTCT_COMPLETE)/sum(DB_HTCT_TARGET)) >=120 then sum(diemthuong_DB_HTCT) else 0 end,2) diemthuong_DB_HTCT " + 
				" " + 
				" from tbl a,sys_group b where a.sysGroupId=b.sys_group_id " + 
				" group by YEAR,month " + 
				" ) " + 
				"  " + 
				" select  " + 
				" type, " + 
				"YEAR year,month month,sysGroupName,sysGroupId ,AREA_CODE areaCode,PROVINCE_CODE provinceCode, " + 
				"(diemdat_quyluong+diemdat_hsch_XL+diemdat_revenue_CP+diemdat_revenue_NTDXDDD+diemdat_quantity_XL+diemdat_quantity_CP+diemdat_quantity_NTD_XDDD+diemdat_TASK_XDDD+ " + 
				"diemdat_revokeCash+ diemdat_RENT_HTCT+diemdat_MONG_HTCT+diemdat_DB_HTCT) diemDatTong, " + 
				" " + 
				"(diemthuong_quyluong+diemthuong_hshc_XL+diemthuong_revenue_CP+diemthuong_revenue_NTDXDDD+diemthuong_quantity_XL+diemthuong_quantity_CP+ " + 
				"diemthuong_quantity_NTD_XDDD+ diemthuong_TASK_XDDD+diemthuong_revokeCash+diemthuong_RENT_HTCT+diemthuong_MONG_HTCT+diemthuong_DB_HTCT) diemThuongTong, " + 
				" " + 
				"(diemdat_quyluong+diemdat_hsch_XL+diemdat_revenue_CP+diemdat_revenue_NTDXDDD+diemdat_quantity_XL+diemdat_quantity_CP+diemdat_quantity_NTD_XDDD+diemdat_TASK_XDDD+ " + 
				"diemdat_revokeCash+ diemdat_RENT_HTCT+diemdat_MONG_HTCT+diemdat_DB_HTCT)+  " + 
				"(diemthuong_quyluong+diemthuong_hshc_XL+diemthuong_revenue_CP+diemthuong_revenue_NTDXDDD+diemthuong_quantity_XL+diemthuong_quantity_CP+ " + 
				"diemthuong_quantity_NTD_XDDD+ diemthuong_TASK_XDDD+diemthuong_revokeCash+diemthuong_RENT_HTCT+diemthuong_MONG_HTCT+diemthuong_DB_HTCT) tongDiem, " + 
				" " + 
				"round(((diemdat_quyluong+diemdat_hsch_XL+diemdat_revenue_CP+diemdat_revenue_NTDXDDD+diemdat_quantity_XL+diemdat_quantity_CP+diemdat_quantity_NTD_XDDD+diemdat_TASK_XDDD+ " + 
				"diemdat_revokeCash+ diemdat_RENT_HTCT+diemdat_MONG_HTCT+diemdat_DB_HTCT)+  " + 
				"(diemthuong_quyluong+diemthuong_hshc_XL+diemthuong_revenue_CP+diemthuong_revenue_NTDXDDD+diemthuong_quantity_XL+diemthuong_quantity_CP+ " + 
				"diemthuong_quantity_NTD_XDDD+ diemthuong_TASK_XDDD+diemthuong_revokeCash+diemthuong_RENT_HTCT+diemthuong_MONG_HTCT+diemthuong_DB_HTCT))*100/17,2) quyDoiDiem, " + 
				" " + 
				"round(quyluong_target/1000000,2) quyLuongTarget,round(quyluong_complete/1000000,2) quyLuongComplete,process_quyluong processQuyLuong,diemdat_quyluong diemDatQuyLuong, diemthuong_quyluong diemThuongQuyLuong, " + 
				"round(hshc_XL_Target/1000000,2) hshcXlTarget, round(hshc_XL_Complete/1000000,2) hshcXlComplete,process_hshc_XL processHshcXl,diemdat_hsch_XL diemDatHschXl,diemthuong_hshc_XL diemThuongHshcXl, " + 
				"round(revenue_CP_Target/1000000,2) revenueCpTarget,round(revenue_CP_Complete/1000000,2) revenueCpComplete,process_revenue_CP processRevenueCp, diemdat_revenue_CP diemDatRevenueCp, diemthuong_revenue_CP diemThuongRevenueCp, " + 
				"round(revenue_NTDXDDD_Target/1000000,2) revenueNtdXdddTarget, round(revenue_NTDXDDD_Complete/1000000,2) revenueNtdXdddComplete,process_revenue_NTDXDDD processRevenueNtdXddd,diemdat_revenue_NTDXDDD diemDatRevenueNtdXddd,diemthuong_revenue_NTDXDDD diemThuongRevenueNtdXddd, " + 
				"round(quantity_XL_Target/1000000,2) quantityXlTarget,round(quantity_XL_Complete/1000000,2) quantityXlComplete,process_quantity_XL processQuantityXl,diemdat_quantity_XL diemDatQuantityXl,diemthuong_quantity_XL diemThuongQuantityXl, " + 
				"round(quantity_CP_Target/1000000,2) quantityCpTarget, round(quantity_CP_Complete/1000000,2) quantityCpComplete,process_quantity_CP processQuantityCp,diemdat_quantity_CP diemDatQuantityCp, diemthuong_quantity_CP diemThuongQuantityCp, " + 
				"round(quantity_NTD_XDDD_Target/1000000,2) quantityNtdXdddTarget,round(quantity_NTD_XDDD_Complete/1000000,2) quantityNtdXdddComplete, process_quantity_NTD_XDDD processQuantityNtdXddd, diemdat_quantity_NTD_XDDD diemDatQuantityNtdXddd,diemthuong_quantity_NTD_XDDD diemThuongQuantityNtdXddd, " + 
				"round(TASK_XDDD_TARGET/1000000,2) taskXdddTarget,round(TASK_XDDD_COMPLETE/1000000,2) taskXdddComplete,process_TASK_XDDD processTaskXddd, diemdat_TASK_XDDD diemDatTaskXddd, diemthuong_TASK_XDDD diemThuongTaskXddd, " + 
				"round(revokeCashTarget/1000000,2) revokeCashTarget,round(revokeCashComplete/1000000,2) revokeCashComplete,process_revokeCash processRevokeCash,diemdat_revokeCash diemDatRevokeCash, diemthuong_revokeCash diemThuongRevokeCash, " + 
				"RENT_HTCT_TARGET rentHtctTarget,RENT_HTCT_COMPLETE rentHtctComplete,process_RENT_HTCT processRentHtct, diemdat_RENT_HTCT diemdatRentHtct, diemthuong_RENT_HTCT diemThuongRentHtct, " + 
				"MONG_HTCT_TARGET mongHtctTarget,MONG_HTCT_COMPLETE mongHtctComplete,process_MONG_HTCT processMongHtct, diemdat_MONG_HTCT diemDatMongHtct,diemthuong_MONG_HTCT diemThuongMongHtct, " + 
				"DB_HTCT_TARGET dbHtctTarget,DB_HTCT_COMPLETE dbHtctComplete,process_DB_HTCT processDbHtct,diemdat_DB_HTCT diemDatDbHtct, diemthuong_DB_HTCT diemThuongDbHtct, " + 
				" " + 
				"(RENT_HTCT_TARGET+MONG_HTCT_TARGET+DB_HTCT_TARGET) tkHtctTarget, " + 
				"(RENT_HTCT_COMPLETE+MONG_HTCT_COMPLETE+DB_HTCT_COMPLETE) tkHtctComplete, " + 
				" " + 
				"round(decode((RENT_HTCT_TARGET+MONG_HTCT_TARGET+DB_HTCT_TARGET),0,0,100*(RENT_HTCT_COMPLETE+MONG_HTCT_COMPLETE+DB_HTCT_COMPLETE)/ " + 
				"(RENT_HTCT_TARGET+MONG_HTCT_TARGET+DB_HTCT_TARGET)),2) processTkHtct, " + 
				"(diemdat_RENT_HTCT+diemdat_MONG_HTCT+diemdat_DB_HTCT) diemDatTkHtct,0 diemThuongTkHtct " + 
				" " + 
				" from tblReport where 1=1 ");
				
//				if(StringUtils.isNotBlank(obj.getMonthYear())) {
//					sql.append(" AND month = EXTRACT(MONTH FROM to_date(:monthYear,'MM/yyyy')) "
//							+ " and year = EXTRACT(YEAR FROM to_date(:monthYear,'MM/yyyy')) "); 
//				}
				
				if(StringUtils.isNotBlank(obj.getProvinceCode())) {
					sql.append(" AND PROVINCE_CODE = :provinceCode "); 
				}
				
				if(StringUtils.isNotBlank(obj.getTtkv())) {
					sql.append(" AND AREA_CODE = :areaCode "); 
				}
				
				sql.append("  order by year,month,type desc,AREA_CODE,PROVINCE_CODE");
		
		StringBuilder sqlCount = new StringBuilder("select count(*) from (" + sql.toString() + ")");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
		
		query.addScalar("year", new StringType());
		query.addScalar("month", new StringType());
		query.addScalar("sysGroupName", new StringType());
		query.addScalar("sysGroupId", new LongType());
		query.addScalar("areaCode", new StringType());
		query.addScalar("provinceCode", new StringType());
		query.addScalar("diemDatTong", new DoubleType());
		query.addScalar("diemThuongTong", new DoubleType());
		query.addScalar("tongDiem", new DoubleType());
		query.addScalar("quyDoiDiem", new DoubleType());
		query.addScalar("quyLuongTarget", new DoubleType());
		query.addScalar("quyLuongComplete", new DoubleType());
		query.addScalar("processQuyLuong", new DoubleType());
		query.addScalar("diemDatQuyLuong", new DoubleType());
		query.addScalar("diemThuongQuyLuong", new DoubleType());
		query.addScalar("hshcXlTarget", new DoubleType());
		query.addScalar("hshcXlComplete", new DoubleType());
		query.addScalar("processHshcXl", new DoubleType());
		query.addScalar("diemDatHschXl", new DoubleType());
		query.addScalar("diemThuongHshcXl", new DoubleType());
		query.addScalar("revenueCpTarget", new DoubleType());
		query.addScalar("revenueCpComplete", new DoubleType());
		query.addScalar("processRevenueCp", new DoubleType());
		query.addScalar("diemDatRevenueCp", new DoubleType());
		query.addScalar("diemThuongRevenueCp", new DoubleType());
		query.addScalar("revenueNtdXdddTarget", new DoubleType());
		query.addScalar("revenueNtdXdddComplete", new DoubleType());
		query.addScalar("processRevenueNtdXddd", new DoubleType());
		query.addScalar("diemDatRevenueNtdXddd", new DoubleType());
		query.addScalar("diemThuongRevenueNtdXddd", new DoubleType());
		query.addScalar("quantityXlTarget", new DoubleType());
		query.addScalar("quantityXlComplete", new DoubleType());
		query.addScalar("processQuantityXl", new DoubleType());
		query.addScalar("diemDatQuantityXl", new DoubleType());
		query.addScalar("diemThuongQuantityXl", new DoubleType());
		query.addScalar("quantityCpTarget", new DoubleType());
		query.addScalar("quantityCpComplete", new DoubleType());
		query.addScalar("processQuantityCp", new DoubleType());
		query.addScalar("diemDatQuantityCp", new DoubleType());
		query.addScalar("diemThuongQuantityCp", new DoubleType());
		query.addScalar("quantityNtdXdddTarget", new DoubleType());
		query.addScalar("quantityNtdXdddComplete", new DoubleType());
		query.addScalar("processQuantityNtdXddd", new DoubleType());
		query.addScalar("diemDatQuantityNtdXddd", new DoubleType());
		query.addScalar("diemThuongQuantityNtdXddd", new DoubleType());
		query.addScalar("taskXdddTarget", new DoubleType());
		query.addScalar("taskXdddComplete", new DoubleType());
		query.addScalar("processTaskXddd", new DoubleType());
		query.addScalar("diemDatTaskXddd", new DoubleType());
		query.addScalar("diemThuongTaskXddd", new DoubleType());
		query.addScalar("revokeCashTarget", new DoubleType());
		query.addScalar("revokeCashComplete", new DoubleType());
		query.addScalar("processRevokeCash", new DoubleType());
		query.addScalar("diemDatRevokeCash", new DoubleType());
		query.addScalar("diemThuongRevokeCash", new DoubleType());
		query.addScalar("rentHtctTarget", new DoubleType());
		query.addScalar("rentHtctComplete", new DoubleType());
		query.addScalar("processRentHtct", new DoubleType());
		query.addScalar("diemdatRentHtct", new DoubleType());
		query.addScalar("diemThuongRentHtct", new DoubleType());
		query.addScalar("mongHtctTarget", new DoubleType());
		query.addScalar("mongHtctComplete", new DoubleType());
		query.addScalar("processMongHtct", new DoubleType());
		query.addScalar("diemDatMongHtct", new DoubleType());
		query.addScalar("diemThuongMongHtct", new DoubleType());
		query.addScalar("dbHtctTarget", new DoubleType());
		query.addScalar("dbHtctComplete", new DoubleType());
		query.addScalar("processDbHtct", new DoubleType());
		query.addScalar("diemDatDbHtct", new DoubleType());
		query.addScalar("diemThuongDbHtct", new DoubleType());
		
		query.addScalar("tkHtctTarget", new DoubleType());
		query.addScalar("tkHtctComplete", new DoubleType());
		query.addScalar("processTkHtct", new DoubleType());
		query.addScalar("diemDatTkHtct", new DoubleType());
		query.addScalar("diemThuongTkHtct", new DoubleType());

		if(StringUtils.isNotBlank(obj.getMonthYear())) {
			query.setParameter("monthYear", obj.getMonthYear());
			queryCount.setParameter("monthYear", obj.getMonthYear());
		}
		
		if(StringUtils.isNotBlank(obj.getProvinceCode())) {
			query.setParameter("provinceCode", obj.getProvinceCode());
			queryCount.setParameter("provinceCode", obj.getProvinceCode());
		}
		
		if(StringUtils.isNotBlank(obj.getTtkv())) {
			query.setParameter("areaCode", obj.getTtkv());
			queryCount.setParameter("areaCode", obj.getTtkv());
		}
		
		query.setResultTransformer(Transformers.aliasToBean(RpProgressMonthPlanOsDTO.class));
		
		if (obj.getPage() != null && obj.getPageSize() != null) {
			query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
			query.setMaxResults(obj.getPageSize().intValue());
		}
		obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
		
		return query.list();
	}
	//Huy-end
}
