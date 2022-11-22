package com.viettel.coms.dao;

import com.viettel.coms.bo.WoBO;
import com.viettel.coms.dto.DomainDTO;
import com.viettel.coms.dto.WoDTO;
import com.viettel.coms.dto.WoDashboardDTO;
import com.viettel.coms.dto.WoDashboardRequest;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class WoDashboardDAO extends BaseFWDAOImpl<WoBO, Long> {

    /**
     * getDataDashboardWoMngt
     *
     * @param sysUserId
     * @param fromDate
     * @param toDate
     * @return all total as required
     */
    public WoDashboardDTO getDataDashboardWoMngt(long sysUserId, String fromDate, String toDate) {
        List<DomainDTO> lstDoamins = getByAdResource(sysUserId, "VIEW WOXL");
        List<String> lstGroupIds = new ArrayList<>();
        for (DomainDTO iDomain : lstDoamins) {
            lstGroupIds.add("" + iDomain.getDataId());
        }
        String sqlText = "select\n" +
                "    sum(total) totalWo\n" +
                "    , sum(wOReceiveOver) numWOReceiveOver\n" +
                "    , ROUND((sum(wOReceiveOver) / sum(total) * 100), 1) percentReceiveOver\n" +
                "    , sum(woNotAssignFt) numWoNotAssignFt\n" +
                "    , ROUND((sum(woNotAssignFt) / sum(total) * 100), 1) percentNotAssignFt\n" +
                "    , sum(woNotVerifyComplete) numWoNotVerifyComplete\n" +
                "    , ROUND((sum(woNotVerifyComplete) / sum(total) * 100), 1) percentNotVerifyComplete\n" +
                "    , sum(woFtReceiveOver) numWoFtReceiveOver\n" +
                "    , ROUND((sum(woFtReceiveOver) / sum(total) * 100), 1) percentFtReceiveOver\n" +
                "    , sum(woOngoingInTerm) numWoOngoingInTerm\n" +
                "    , sum(woOngoingOverTerm) numWoOngoingOverTerm\n" +
                "    , sum(woCompletedApproved) numWoCompletedApproved\n" +
                "    , sum(woCompletedUnapproved) numWoCompletedUnapproved\n" +
                "from (\n" +
                "    select\n" +
                "        1 total\n" +
                "        , CASE WHEN (USER_CD_LEVEL2_RECEIVE_WO = -1 OR USER_CD_LEVEL3_RECEIVE_WO = -1 OR USER_CD_LEVEL4_RECEIVE_WO = -1) THEN 1 ELSE 0 END wOReceiveOver\n" +
                "        , CASE WHEN FT_ID IS NULL THEN 1 ELSE 0 END woNotAssignFt\n" +
                "        , CASE WHEN STATE = 'DONE' OR STATE = 'CD_OK' OR STATE = 'CD_NG' OR STATE = 'NG' THEN 1 ELSE 0 END woNotVerifyComplete\n" +
                "        , CASE WHEN USER_FT_RECEIVE_WO = -1 THEN 1 ELSE 0 END woFtReceiveOver\n" +
                "        , CASE WHEN (STATE = 'PROCESSING' AND trunc(FINISH_DATE) >= TRUNC(SYSDATE)) THEN 1 ELSE 0 END woOngoingInTerm\n" +
                "        , CASE WHEN (STATE = 'PROCESSING' AND trunc(FINISH_DATE) < TRUNC(SYSDATE)) THEN 1 ELSE 0 END woOngoingOverTerm  \n" +
                "        , CASE WHEN STATE = 'CD_OK' OR STATE = 'CD_NG' OR STATE = 'OK' OR STATE = 'NG' THEN 1 ELSE 0 END woCompletedApproved\n" +
                "        , CASE WHEN STATE = 'DONE' THEN 1 ELSE 0 END woCompletedUnapproved\n" +
                "    from WO\n" +
                "    WHERE STATUS = 1  \n" +
                "    AND trunc(CREATED_DATE) >= TO_DATE(:fromDate, 'yyyy-MM-dd') \n" +
                "    AND trunc(CREATED_DATE) <= TO_DATE(:toDate, 'yyyy-MM-dd') \n";
        if (lstGroupIds.size() > 0) {
            sqlText += " AND (CD_LEVEL_1 IN (:lstGroupIds) OR CD_LEVEL_2 IN (:lstGroupIds) OR CD_LEVEL_3 IN (:lstGroupIds) OR CD_LEVEL_4 IN (:lstGroupIds) OR CD_LEVEL_5 = :sysUserId)\n";
        } else {
            sqlText += "AND 1 = 0\n";
        }
        sqlText += ")";

        SQLQuery query = getSession().createSQLQuery(sqlText);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        query.setParameter("sysUserId", sysUserId);
        if (lstGroupIds.size() > 0) {
            query.setParameterList("lstGroupIds", lstGroupIds);
        }

        query.addScalar("totalWo", new LongType());
        query.addScalar("numWOReceiveOver", new LongType());
        query.addScalar("percentReceiveOver", new DoubleType());
        query.addScalar("numWoNotAssignFt", new LongType());
        query.addScalar("percentNotAssignFt", new DoubleType());
        query.addScalar("numWoNotVerifyComplete", new LongType());
        query.addScalar("percentNotVerifyComplete", new DoubleType());
        query.addScalar("numWoFtReceiveOver", new LongType());
        query.addScalar("percentFtReceiveOver", new DoubleType());
        query.addScalar("numWoOngoingInTerm", new LongType());
        query.addScalar("numWoOngoingOverTerm", new LongType());
        query.addScalar("numWoCompletedApproved", new LongType());
        query.addScalar("numWoCompletedUnapproved", new LongType());

        query.setResultTransformer(Transformers.aliasToBean(WoDashboardDTO.class));

        WoDashboardDTO woDashboardDTOInfo = (WoDashboardDTO) query.list().get(0);
        if (woDashboardDTOInfo.getTotalWo() != null) {
            Long totalWo = woDashboardDTOInfo.getTotalWo();
            woDashboardDTOInfo.setOtherDh(totalWo - woDashboardDTOInfo.getNumWOReceiveOver()
                    - woDashboardDTOInfo.getNumWoNotAssignFt()
                    - woDashboardDTOInfo.getNumWoNotVerifyComplete()
                    - woDashboardDTOInfo.getNumWoFtReceiveOver());
            woDashboardDTOInfo.setOtherCb(totalWo - woDashboardDTOInfo.getNumWoOngoingInTerm()
                    - woDashboardDTOInfo.getNumWoOngoingOverTerm()
                    - woDashboardDTOInfo.getNumWoCompletedApproved()
                    - woDashboardDTOInfo.getNumWoCompletedUnapproved());
        }

        return woDashboardDTOInfo;
    }

    public WoDashboardDTO getDataDashboardExecuteWo(long sysUserId, String fromDate, String toDate) {
        List<DomainDTO> lstDoamins = getByAdResource(sysUserId, "VIEW WOXL");
        List<String> lstGroupIds = new ArrayList<>();
        for (DomainDTO iDomain : lstDoamins) {
            lstGroupIds.add("" + iDomain.getDataId());
        }
        String sqlText = "select\n" +
                "    sum(total) totalWo\n" +
                "    , sum(assignCd) numAssignCd\n" +
                "    , sum(acceptCd) numAcceptCd\n" +
                "    , sum(assignFt) numAssignFt\n" +
                "    , sum(rejectFt) numRejectFt\n" +
                "    , sum(done) countFtDone\n" +
                "from (\n" +
                "    select\n" +
                "        1 total\n" +
                "        , CASE WHEN state = 'ASSIGN_CD' then 1 else 0 end assignCd\n" +
                "        , CASE WHEN state = 'ACCEPT_CD' then 1 else 0 end acceptCd\n" +
                "        , CASE WHEN state = 'ASSIGN_FT' then 1 else 0 end assignFt\n" +
                "        , CASE WHEN state = 'REJECT_FT' then 1 else 0 end rejectFt\n" +
                "        , CASE WHEN state = 'DONE' then 1 else 0 end done\n" +
                "        , CD_LEVEL_1, CD_LEVEL_2, CD_LEVEL_3, CD_LEVEL_4\n" +
                "    from WO\n" +
                "    WHERE STATUS = 1  \n" +
                "    AND trunc(CREATED_DATE) >= TO_DATE(:fromDate, 'yyyy-MM-dd') \n" +
                "    AND trunc(CREATED_DATE) <= TO_DATE(:toDate, 'yyyy-MM-dd') \n";
        if (lstGroupIds.size() > 0) {
            sqlText += " AND (CD_LEVEL_1 IN (:lstGroupIds) OR CD_LEVEL_2 IN (:lstGroupIds) OR CD_LEVEL_3 IN (:lstGroupIds) OR CD_LEVEL_4 IN (:lstGroupIds) OR CD_LEVEL_5 = :sysUserId)\n";
        } else {
            sqlText += "AND 1 = 0\n";
        }
        sqlText += ")";

        SQLQuery query = getSession().createSQLQuery(sqlText);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        if (lstGroupIds.size() > 0) {
            query.setParameterList("lstGroupIds", lstGroupIds);
            query.setParameter("sysUserId", sysUserId); //Huypq-fix 08022021
        }
        query.addScalar("totalWo", new LongType());
        query.addScalar("numAssignCd", new LongType());
        query.addScalar("numAcceptCd", new LongType());
        query.addScalar("numAssignFt", new LongType());
        query.addScalar("numRejectFt", new LongType());
        query.addScalar("countFtDone", new LongType());

        query.setResultTransformer(Transformers.aliasToBean(WoDashboardDTO.class));

        WoDashboardDTO woDashboardDTOInfo = (WoDashboardDTO) query.list().get(0);

        return woDashboardDTOInfo;
    }

    public List<WoDTO> detailTotalWoDasboard(WoDashboardRequest request, String fromDate, String toDate, String type) {
        long sysUserId = request.getSysUserRequest().getSysUserId();
        List<DomainDTO> lstDoamins = getByAdResource(sysUserId, "VIEW WOXL");
        List<String> lstGroupIds = new ArrayList<>();
        for (DomainDTO iDomain : lstDoamins) {
            lstGroupIds.add("" + iDomain.getDataId());
        }
        String detailSql = "SELECT\n" +
                "\t wo.id woId,\n" +
                "     wo.wo_code woCode,\n" +
                "     wo.wo_name_id woNameId,\n" +
                "     wn.name woName,\n" +
                "     wo.wo_type_id woTypeId,\n" +
                "     wt.wo_type_code woTypeCode,\n" +
                "     wt.wo_type_name woTypeName,\n" +
                "     wo.tr_id trId,\n" +
                "     tr.TR_CODE || '-' || tr.TR_NAME trName,\n" +
                "     wo.state,\n" +
                "     wo.construction_id constructionId,\n" +
                "     wo.cat_work_item_type_id catWorkItemTypeId,\n" +
                "     cwit.name catWorkItemTypeName,\n" +
                "     wo.station_code stationCode,\n" +
                "     wo.user_created userCreated,\n" +
                "     wo.created_date createdDate,\n" +
                "     wo.finish_date finishDate,\n" +
                "     wo.qouta_time qoutaTime,\n" +
                "     wo.execute_method executeMethod,\n" +
                "     wo.quantity_value quantityValue,\n" +
                "     wo.cd_level_1 cdLevel1,\n" +
                "     wo.cd_level_2 cdLevel2,\n" +
                "     wo.cd_level_3 cdLevel3,\n" +
                "     wo.cd_level_4 cdLevel4,\n" +
                "     wo.cd_level_5 cdLevel5,\n" +
                "     wo.ft_id ftId,\n" +
                "     wo.FT_NAME ftName,\n" +
                "     wo.FT_EMAIL ftEmail,\n" +
                "     wo.accept_time acceptTime,\n" +
                "     wo.start_time startTime,\n" +
                "     wo.end_time endTime,\n" +
                "     TO_CHAR(wo.accept_time, 'dd/MM/yyyy hh24:mi:ss') acceptTimeStr,\n" +
                "     TO_CHAR(wo.start_time, 'dd/MM/yyyy hh24:mi:ss') startTimeStr,\n" +
                "     TO_CHAR(wo.end_time, 'dd/MM/yyyy hh24:mi:ss') endTimeStr,\n" +
                "     wo.execute_lat executeLat,\n" +
                "     wo.execute_long executeLong,\n" +
                "     wo.status status,\n" +
                "     wo.total_month_plan_id totalMonthPlanId,\n" +
                "     wo.money_value moneyValue,\n" +
                "     wo.money_flow_bill moneyFlowBill,\n" +
                "     wo.money_flow_date moneyFlowDate,\n" +
                "     wo.money_flow_value moneyFlowValue,\n" +
                "     wo.MONEY_FLOW_REQUIRED moneyFlowRequired,\n" +
                "     wo.MONEY_FLOW_CONTENT moneyFlowContent,\n" +
                "     wo.ap_construction_type apConstructionType,\n" +
                "     wo.opinion_result opinionResult,\n" +
                "     wo.ap_work_src apWorkSrc,\n" +
                "     wo.CONTRACT_ID contractId,\n" +
                "     cct.name catConstructionTypeName,\n" +
                "     wo.cat_province_code catProvinceCode,\n" +
                "     wo.type TYPE,\n" +
                "     wo.EXECUTE_CHECKLIST executeChecklist,\n" +
                "     wo.WO_NAME_ID woNameId,\n" +
                "     wo.QUANTITY_BY_DATE quantityByDate,\n" +
                "     wo.CLOSED_TIME closedTime,\n" +
                "     wo.CONSTRUCTION_CODE constructionCode,\n" +
                "     wo.CONTRACT_CODE contractCode,\n" +
                "     wo.PROJECT_ID projectId,\n" +
                "     wo.PROJECT_CODE projectCode,\n" +
                "     wo.CD_LEVEL_1_NAME cdLevel1Name,\n" +
                "     wo.CD_LEVEL_2_NAME cdLevel2Name,\n" +
                "     wo.CD_LEVEL_3_NAME cdLevel3Name,\n" +
                "     wo.CD_LEVEL_4_NAME cdLevel4Name,\n" +
                "     wo.CD_LEVEL_5_NAME cdLevel5Name,\n" +
                "     wo.CREATED_USER_FULL_NAME createdUserFullName,\n" +
                "     wo.CREATED_USER_EMAIL createdUserEmail,\n" +
                "     tr.TR_CODE trCode,\n" +
                "     wo.CAT_CONSTRUCTION_TYPE_ID catConstructionTypeId,\n" +
                "     wo.CHECKLIST_STEP checklistStep,\n" +
                "     wo.CAT_PROVINCE_CODE catProvinceCode,\n" +
                "     wo.USER_CD_LEVEL2_RECEIVE_WO userCdLevel2ReceiveWo,\n" +
                "     wo.USER_CD_LEVEL3_RECEIVE_WO userCdLevel3ReceiveWo,\n" +
                "     wo.USER_CD_LEVEL4_RECEIVE_WO userCdLevel4ReceiveWo,\n" +
                "     wo.USER_CD_LEVEL5_RECEIVE_WO userCdLevel5ReceiveWo,\n" +
                "     wo.UPDATE_CD_LEVEL2_RECEIVE_WO updateCdLevel2ReceiveWo,\n" +
                "     wo.UPDATE_CD_LEVEL3_RECEIVE_WO updateCdLevel3ReceiveWo,\n" +
                "     wo.UPDATE_CD_LEVEL4_RECEIVE_WO updateCdLevel4ReceiveWo,\n" +
                "     wo.UPDATE_CD_LEVEL5_RECEIVE_WO updateCdLevel5ReceiveWo,\n" +
                "     wo.USER_FT_RECEIVE_WO userFtReceiveWo,     \n" +
                "     wo.UPDATE_FT_RECEIVE_WO updateFtReceiveWo,     \n" +
                "     wo.USER_CD_APPROVE_WO userCdApproveWo,     \n" +
                "     wo.UPDATE_CD_APPROVE_WO updateCdApproveWo,     \n" +
                "     wo.USER_TTHT_APPROVE_WO userTthtApproveWo,     \n" +
                "     wo.UPDATE_TTHT_APPROVE_WO updateTthtApproveWo,     \n" +
                "     wo.APPROVE_DATE_REPORT_WO approveDateReportWo,     \n" +
                "     wo.HCQT_PROJECT_ID hcqtProjectId,     \n" +
                "     wo.HSHC_RECEIVE_DATE hshcReceiveDate,     \n" +
                "     wo.HCQT_CONTRACT_CODE hcqtContractCode,\n" +
                "     wo.CNKV cnkv,     \n" +
                "     hcp.hcqt_project_name hcqtProjectName   \n" +
                "FROM WO wo\n" +
                "LEFT JOIN WO_TYPE wt ON wo.WO_TYPE_ID = wt.id\n" +
                "LEFT JOIN WO_HCQT_PROJECT hcp ON wo.HCQT_PROJECT_ID = hcp.HCQT_PROJECT_ID\n" +
                "LEFT JOIN WO_TR tr ON wo.TR_ID = tr.ID\n" +
                "LEFT JOIN CAT_CONSTRUCTION_TYPE cct ON cct.CAT_CONSTRUCTION_TYPE_ID = wo.CAT_CONSTRUCTION_TYPE_ID\n" +
                "LEFT JOIN CTCT_COMS_OWNER.CAT_WORK_ITEM_TYPE cwit ON wo.CAT_WORK_ITEM_TYPE_ID = cwit.CAT_WORK_ITEM_TYPE_ID\n" +
                "LEFT JOIN WO_NAME wn ON wo.WO_NAME_ID = wn.ID\n" +
                "WHERE wo.STATUS = 1\n" +
                " AND wt.wo_type_code NOT IN ('TMBT', 'TTHQ')  " +
                "AND (trunc(wo.CREATED_DATE) >= trunc(TO_DATE(:fromDate, 'yyyy-MM-dd')))\n" +
                "AND (trunc(wo.CREATED_DATE) <= trunc(TO_DATE(:toDate, 'yyyy-MM-dd')))\n";
        if (lstGroupIds.size() > 0) {
            detailSql += " AND (wo.CD_LEVEL_1 IN (:lstGroupIds) OR wo.CD_LEVEL_2 IN (:lstGroupIds) OR wo.CD_LEVEL_3 IN (:lstGroupIds) OR wo.CD_LEVEL_4 IN (:lstGroupIds) OR CD_LEVEL_5 = :sysUserId)\n";
        } else {
            detailSql += "AND 1 = 0\n";
        }

        switch (type) {
            case "totalWoWarningInformation":
                detailSql += " AND ((wo.USER_CD_LEVEL2_RECEIVE_WO = -1 OR wo.USER_CD_LEVEL3_RECEIVE_WO = -1 OR wo.USER_CD_LEVEL4_RECEIVE_WO = -1)\n" +
                        "OR (wo.FT_ID IS NULL)\n" +
                        "OR (wo.STATE = 'DONE' OR wo.STATE = 'CD_OK' OR wo.STATE = 'CD_NG' OR wo.STATE = 'OK' OR wo.STATE = 'NG')\n" +
                        "OR (wo.USER_FT_RECEIVE_WO = -1))";
                break;
            case "totalWoOperatingInformation":
                detailSql += " AND ((wo.STATE = 'PROCESSING' AND trunc(wo.FINISH_DATE) >= TRUNC(SYSDATE))\n" +
                        " OR (wo.STATE = 'PROCESSING' AND trunc(wo.FINISH_DATE) < TRUNC(SYSDATE))\n" +
                        " OR (wo.STATE = 'CD_OK' OR wo.STATE = 'CD_NG' OR wo.STATE = 'OK' OR wo.STATE = 'NG')\n" +
                        " OR (wo.STATE = 'DONE'))";
                break;
            case "totalImplementationInformation":
                detailSql += " AND ((wo.STATE = 'ASSIGN_CD')\n" +
                        " OR (wo.STATE = 'ACCEPT_CD')\n" +
                        " OR (wo.STATE = 'ASSIGN_FT')\n" +
                        " OR (wo.STATE = 'REJECT_FT')\n" +
                        " OR (wo.STATE = 'DONE' OR wo.STATE = 'CD_OK' OR wo.STATE = 'CD_NG' OR wo.STATE = 'OK' OR wo.STATE = 'NG'))";
                break;
            case "numWOReceiveOver":
                detailSql += " AND (wo.USER_CD_LEVEL2_RECEIVE_WO = -1 OR wo.USER_CD_LEVEL3_RECEIVE_WO = -1 OR wo.USER_CD_LEVEL4_RECEIVE_WO = -1)";
                break;
            case "numWoNotAssignFt":
                detailSql += " AND (wo.FT_ID IS NULL)";
                break;
            case "numWoNotVerifyComplete":
                detailSql += " AND (wo.STATE = 'DONE' OR wo.STATE = 'CD_OK' OR wo.STATE = 'CD_NG' OR wo.STATE = 'NG')";
                break;
            case "numWoFtReceiveOver":
                detailSql += " AND (wo.USER_FT_RECEIVE_WO = -1)";
                break;
            case "numWoOngoingInTerm":
                detailSql += " AND (wo.STATE = 'PROCESSING' AND trunc(wo.FINISH_DATE) >= TRUNC(SYSDATE))";
                break;
            case "numWoOngoingOverTerm":
                detailSql += " AND (wo.STATE = 'PROCESSING' AND trunc(wo.FINISH_DATE) < TRUNC(SYSDATE))";
                break;
            case "numWoCompletedApproved":
                detailSql += " AND (wo.STATE = 'CD_OK' OR wo.STATE = 'CD_NG' OR wo.STATE = 'OK' OR wo.STATE = 'NG')";
                break;
            case "numWoCompletedUnapproved":
                detailSql += " AND (wo.STATE = 'DONE')";
                break;
            case "numAssignCd":
                detailSql += " AND (wo.STATE = 'ASSIGN_CD')";
                break;
            case "numAcceptCd":
                detailSql += " AND (wo.STATE = 'ACCEPT_CD')";
                break;
            case "numAssignFt":
                detailSql += " AND (wo.STATE = 'ASSIGN_FT')";
                break;
            case "numRejectFt":
                detailSql += " AND (wo.STATE = 'REJECT_FT')";
                break;
            case "countFtDone":
                detailSql += " AND (wo.STATE = 'DONE')";
                break;
        }

        detailSql += " ORDER BY wo.END_TIME";

        SQLQuery query = getSession().createSQLQuery(detailSql);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        if (lstGroupIds.size() > 0) {
            query.setParameterList("lstGroupIds", lstGroupIds);
            query.setParameter("sysUserId", sysUserId);
        }
        
        query.addScalar("woId", new LongType());
        query.addScalar("woCode", new StringType());
        query.addScalar("woNameId", new LongType());
        query.addScalar("woName", new StringType());
        query.addScalar("woTypeId", new LongType());
        query.addScalar("woTypeCode", new StringType());
        query.addScalar("woTypeName", new StringType());
        query.addScalar("trId", new LongType());
        query.addScalar("trName", new StringType());
        query.addScalar("trCode", new StringType());
        query.addScalar("state", new StringType());
        query.addScalar("constructionId", new LongType());
        query.addScalar("catWorkItemTypeId", new LongType());
        query.addScalar("catWorkItemTypeName", new StringType());
        query.addScalar("stationCode", new StringType());
        query.addScalar("userCreated", new StringType());
        query.addScalar("createdDate", new DateType());
        query.addScalar("finishDate", new TimestampType());
        query.addScalar("status", new LongType());
        query.addScalar("qoutaTime", new IntegerType());
        query.addScalar("executeMethod", new StringType());
        query.addScalar("quantityValue", new StringType());
        query.addScalar("cdLevel1", new StringType());
        query.addScalar("cdLevel1Name", new StringType());
        query.addScalar("cdLevel2", new StringType());
        query.addScalar("cdLevel2Name", new StringType());
        query.addScalar("cdLevel3", new StringType());
        query.addScalar("cdLevel3Name", new StringType());
        query.addScalar("cdLevel4", new StringType());
        query.addScalar("cdLevel4Name", new StringType());
        query.addScalar("cdLevel5", new StringType());
        query.addScalar("cdLevel5Name", new StringType());
        query.addScalar("ftId", new LongType());
        query.addScalar("ftName", new StringType());
        query.addScalar("ftEmail", new StringType());
        query.addScalar("acceptTime", new TimestampType());
        query.addScalar("startTime", new TimestampType());
        query.addScalar("endTime", new TimestampType());
        query.addScalar("acceptTimeStr", new StringType());
        query.addScalar("startTimeStr", new StringType());
        query.addScalar("endTimeStr", new StringType());
        query.addScalar("executeLat", new StringType());
        query.addScalar("executeLong", new StringType());
        query.addScalar("totalMonthPlanId", new LongType());
        query.addScalar("moneyValue", new DoubleType());
        query.addScalar("moneyFlowBill", new StringType());
        query.addScalar("moneyFlowDate", new DateType());
        query.addScalar("moneyFlowValue", new LongType());
        query.addScalar("moneyFlowRequired", new LongType());
        query.addScalar("moneyFlowContent", new StringType());
        query.addScalar("apConstructionType", new LongType());
        query.addScalar("opinionResult", new StringType());
        query.addScalar("apWorkSrc", new LongType());
        query.addScalar("contractId", new LongType());
        query.addScalar("projectId", new LongType());
        query.addScalar("constructionCode", new StringType());
        query.addScalar("catConstructionTypeId", new LongType());
        query.addScalar("catConstructionTypeName", new StringType());
        query.addScalar("hcqtContractCode", new StringType());
        query.addScalar("hcqtProjectId", new LongType());
        query.addScalar("hcqtProjectName", new StringType());
        query.addScalar("hshcReceiveDate", new DateType());
        query.addScalar("cnkv", new StringType());
        query.addScalar("catProvinceCode", new StringType());
        query.addScalar("createdUserFullName", new StringType());
        query.addScalar("createdUserEmail", new StringType());
        query.addScalar("checklistStep", new LongType());
        query.addScalar("userCdLevel2ReceiveWo", new StringType());
        query.addScalar("userCdLevel3ReceiveWo", new StringType());
        query.addScalar("userCdLevel4ReceiveWo", new StringType());
        query.addScalar("userCdLevel5ReceiveWo", new StringType());
        query.addScalar("updateCdLevel2ReceiveWo", new DateType());
        query.addScalar("updateCdLevel3ReceiveWo", new DateType());
        query.addScalar("updateCdLevel4ReceiveWo", new DateType());
        query.addScalar("updateCdLevel5ReceiveWo", new DateType());
        query.addScalar("userFtReceiveWo", new StringType());
        query.addScalar("updateFtReceiveWo", new DateType());
        query.addScalar("userCdApproveWo", new StringType());
        query.addScalar("updateCdApproveWo", new DateType());
        query.addScalar("userTthtApproveWo", new StringType());
        query.addScalar("updateTthtApproveWo", new DateType());
        query.addScalar("approveDateReportWo", new DateType());

        query.setResultTransformer(Transformers.aliasToBean(WoDTO.class));

//        if (request.getPage() != null && request.getPageSize() != null) {
//            query.setFirstResult((request.getPage().intValue() - 1) * request.getPageSize().intValue());
//            query.setMaxResults(request.getPageSize().intValue());
//        }

            return query.list();
    }

    public List<DomainDTO> getByAdResource(long sysUserId, String adResource) {
        StringBuilder sql = new StringBuilder("");
        sql.append(" SELECT e.data_code dataCode, e.DATA_ID dataId, ");
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
}
