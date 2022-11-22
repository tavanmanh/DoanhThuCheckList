/*
 * Copyright (C) 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.erp.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.viettel.erp.bo.CntContractBO;
import com.viettel.erp.dto.CntContractDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

/**
 * @author TruongBX3
 * @version 1.0
 * @since 08-May-15 4:07 PM
 */
@Repository("cntContractDAO")
@Transactional
public class CntContractDAO extends BaseFWDAOImpl<CntContractBO, Long> {

    public CntContractDAO() {
        this.model = new CntContractBO();
    }

    public CntContractDAO(Session session) {
        this.session = session;
    }

    //  Hungnx 130618 start
    public List<CntContractDTO> doSearch(CntContractDTO criteria) {
        StringBuilder stringBuilder = getSelectAllQuery();

        stringBuilder.append(" Where STATUS != 0");
        stringBuilder.append(criteria.getIsSize() ? " AND ROWNUM <=10" : "");
        if (StringUtils.isNotEmpty(criteria.getCode())) {
            stringBuilder.append(" AND (UPPER(CODE) like UPPER(:code) escape '&')");
        }
        if (criteria.getContractType() != null) {
            stringBuilder.append(" AND T1.CONTRACT_TYPE = :contractType");
        }
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(stringBuilder.toString());
        sqlCount.append(")");
        SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        query.addScalar("cntContractId", new LongType());
        query.addScalar("code", new StringType());
        query.addScalar("signDate", new DateType());
        query.addScalar("price", new DoubleType());

        query.setResultTransformer(Transformers.aliasToBean(CntContractDTO.class));

        if (StringUtils.isNotEmpty(criteria.getCode())) {
            query.setParameter("code", "%" + criteria.getCode() + "%");
            queryCount.setParameter("code", "%" + criteria.getCode() + "%");
        }
        if (criteria.getContractType() != null) {
            stringBuilder.append(" AND T1.CONTRACT_TYPE = :contractType");
            query.setParameter("contractType", criteria.getContractType());
            queryCount.setParameter("contractType", criteria.getContractType());
        }
        if (criteria.getPage() != null && criteria.getPageSize() != null) {
            query.setFirstResult((criteria.getPage().intValue() - 1)
                    * criteria.getPageSize().intValue());
            query.setMaxResults(criteria.getPageSize().intValue());
        }
        criteria.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
        return query.list();
    }

    public StringBuilder getSelectAllQueryAllColumn() {
        StringBuilder stringBuilder = new StringBuilder("SELECT ");
        stringBuilder.append("T1.CNT_CONTRACT_ID cntContractId ");
        stringBuilder.append(",T1.CODE code ");
        stringBuilder.append(",T1.NAME name ");
        stringBuilder.append(",T1.CONTRACT_CODE_KTTS contractCodeKtts ");
        stringBuilder.append(",T1.CONTENT content ");
        stringBuilder.append(",T1.SIGN_DATE signDate ");
        stringBuilder.append(",T1.START_TIME startTime ");
        stringBuilder.append(",T1.END_TIME endTime ");
        stringBuilder.append(",T1.PRICE price ");
        stringBuilder.append(",T1.APPENDIX_CONTRACT appendixContract ");
        stringBuilder.append(",T1.NUM_STATION numStation ");
        stringBuilder.append(",T1.BIDDING_PACKAGE_ID biddingPackageId ");
        stringBuilder.append(",T1.CAT_PARTNER_ID catPartnerId ");
        stringBuilder.append(",T1.SIGNER_PARTNER signerPartner ");
        stringBuilder.append(",T1.SYS_GROUP_ID sysGroupId ");
        stringBuilder.append(",T1.SIGNER_GROUP signerGroup ");
        stringBuilder.append(",T1.SUPERVISOR supervisor ");
        stringBuilder.append(",T1.STATUS status ");
        stringBuilder.append(",T1.DESCRIPTION description ");
        stringBuilder.append(",T1.FORMAL formal ");
        stringBuilder.append(",T1.CONTRACT_TYPE contractType ");
        stringBuilder.append(",T1.CNT_CONTRACT_PARENT_ID cntContractParentId ");
        stringBuilder.append(",T1.CREATED_DATE createdDate ");
        stringBuilder.append(",T1.CREATED_USER_ID createdUserId ");
        stringBuilder.append(",T1.CREATED_GROUP_ID createdGroupId ");
        stringBuilder.append(",T1.UPDATED_DATE updatedDate ");
        stringBuilder.append(",T1.UPDATED_USER_ID updatedUserId ");
        stringBuilder.append(",T1.UPDATED_GROUP_ID updatedGroupId ");
        stringBuilder.append(",T1.NUM_DAY numDay ");
        stringBuilder.append(",T1.FRAME_PARENT_ID frameParentId ");
        stringBuilder.append(",T1.MONEY_TYPE moneyType ");
        /** hoangnh start 28012019 **/
        stringBuilder.append(",T1.CONTRACT_TYPE_O contractTypeO ");
        stringBuilder.append(",T1.CONTRACT_TYPE_OS_NAME contractTypeOsName ");
        /** hoangnh end 28012019 **/

        stringBuilder.append(",T1.IS_XNXD isXNXD ");
        stringBuilder.append(",T1.CONSTRUCTION_FORM constructionForm ");
        stringBuilder.append(",T1.CURRENT_PROGRESS currentProgess ");
        stringBuilder.append(",T1.HANDOVER_USE_DATE handoverUseDate ");
        stringBuilder.append(",T1.WARRANTY_EXPIRED_DATE warrantyExpiredDate ");
        stringBuilder.append(",T1.STRUCTURE_FILTER structureFilter ");
        stringBuilder.append(",T1.DESCRIPTION_XNXD descriptionXNXD ");
        stringBuilder.append(",T1.PROJECT_ID projectId ");
        stringBuilder.append(",T1.PROJECT_CODE projectCode ");
        stringBuilder.append(",T1.PROJECT_NAME projectName ");
        stringBuilder.append(",T1.EXTENSION_DAYS extensionDays ");
        stringBuilder.append(",T1.PAYMENT_EXPRIED paymentExpried ");
        stringBuilder.append(",T1.TYPE_HTCT typeHTCT ");
        stringBuilder.append(",T1.PRICE_HTCT priceHTCT ");
        stringBuilder.append(",T1.MONTH_HTCT monthHTCT ");
        stringBuilder.append(",T1.CNT_CONTRACT_APPROVE cntContractApprove ");
        stringBuilder.append(",T9.CODE projectContractCode ");
        stringBuilder.append(
                ",(SELECT T8.CODE FROM CNT_CONTRACT T8 WHERE T8.CNT_CONTRACT_ID = T1.FRAME_PARENT_ID) frameParentCode ");
        stringBuilder.append(",T2.NAME catPartnerName ");
        stringBuilder.append(",T3.NAME sysGroupName ");
        stringBuilder.append(",T4.NAME biddingPackageName ");
        stringBuilder.append(",T5.FULL_NAME signerGroupName ");
        stringBuilder.append(",T12.FULL_NAME supervisorName ");
        stringBuilder.append(
                ",(SELECT T6.CODE FROM CNT_CONTRACT T6 WHERE T6.CNT_CONTRACT_ID = T1.CNT_CONTRACT_PARENT_ID) cntContractParentCode ");
        stringBuilder.append(",T1.STATE state ");
        stringBuilder.append(",T7.NAME createdGroupName ");
        stringBuilder.append(
                ",(CASE WHEN T6.EMAIL IS NULL THEN T6.FULL_NAME ELSE T6.FULL_NAME||'('||T6.EMAIL||')' END) createdName ");

        stringBuilder.append(",T1.COEFFICIENT coefficient ");
        stringBuilder.append(",T1.DEPLOYMENT_DATE deploymentDate ");
        stringBuilder.append(",T1.CODE_EXT codeExt ");
        stringBuilder.append(
                " ,(CASE WHEN T10.EMAIL IS NULL THEN T10.FULL_NAME ELSE T10.FULL_NAME||'('||T10.EMAIL||')' END) updatedUserName ");
        stringBuilder.append(
                " ,(CASE WHEN T1.STATUS=1 THEN (CASE WHEN T1.END_TIME < SYSDATE THEN 'Quá hạn' ELSE '' END) else '' END) missStatus ");
        stringBuilder.append(
                " , T1.WARRANTY_NUMBER_DAY warrantyNumberDay, provinceMap.PROVINCE_NAME provinceName, provinceMap.PROVINCE_CODE provinceCode ");
        stringBuilder.append(" ,T1.XDDD_TYPE xdddType ");
        stringBuilder.append(" ,T1.CONSTRUCTION_CODE constructionCode " + ",T1.STATION_CODE_VCC stationCodeVcc "
                + ",T1.ADDRESS_STATION addressStation " + ",T1.STATION_CODE_HTCT stationCodeHtct "
                + ", T1.AREA_CODE areaCode, T1.VALIDITY_DATE validityDate, T1.CAT_PROVINCE_CODE catProvinceCode ");
        stringBuilder.append(
                " ,T1.CONTRACT_BRANCH contractBranch, T1.CONTRACT_SCOPE contractScope, T1.B2B_B2C b2bB2c, T1.CHANNEL channel, T1.SYN_STATE synState, T1.HCQT_PROJECT_ID hcqtProjectId, T13.HCQT_PROJECT_CODE hcqtProjectCode ");
        stringBuilder.append("FROM CNT_CONTRACT T1 ");
        stringBuilder.append("LEFT JOIN CTCT_CAT_OWNER.CAT_PARTNER T2 ON T1.CAT_PARTNER_ID = T2.CAT_PARTNER_ID ");
        stringBuilder.append("LEFT JOIN CTCT_CAT_OWNER.SYS_GROUP T3 ON T1.SYS_GROUP_ID = T3.SYS_GROUP_ID ");
        stringBuilder.append("LEFT JOIN BIDDING_PACKAGE T4 ON T1.BIDDING_PACKAGE_ID = T4.BIDDING_PACKAGE_ID ");
        stringBuilder.append("LEFT JOIN CTCT_VPS_OWNER.SYS_USER T5 ON T1.SIGNER_GROUP = T5.SYS_USER_ID ");
        stringBuilder.append("LEFT JOIN CTCT_VPS_OWNER.SYS_USER T6 ON T1.CREATED_USER_ID = T6.SYS_USER_ID ");
        stringBuilder.append("LEFT JOIN CTCT_CAT_OWNER.SYS_GROUP T7 ON T1.CREATED_GROUP_ID = T7.SYS_GROUP_ID ");
        stringBuilder.append("LEFT JOIN PROJECT_CONTRACT T9 ON T1.PROJECT_CONTRACT_ID = T9.PROJECT_CONTRACT_ID ");
        stringBuilder.append("LEFT JOIN CTCT_VPS_OWNER.SYS_USER T10 ON T1.UPDATED_USER_ID = T10.SYS_USER_ID ");
        stringBuilder.append(
                "LEFT JOIN CONTRACT_XNXD_PROVINCE provinceMap on provinceMap.CONTRACT_ID = T1.CNT_CONTRACT_ID ");
        stringBuilder.append("LEFT JOIN CTCT_VPS_OWNER.SYS_USER T12 ON T1.supervisor = to_char(T12.sys_user_id) ");
        stringBuilder.append("LEFT JOIN WO_HCQT_PROJECT T13 ON T1.HCQT_PROJECT_ID = T13.HCQT_PROJECT_ID ");
        return stringBuilder;
    }
    public StringBuilder getSelectAllQuery() {
        StringBuilder stringBuilder = new StringBuilder("SELECT ");
        stringBuilder.append("T1.CNT_CONTRACT_ID cntContractId ");
        stringBuilder.append(",T1.CODE code ");
        stringBuilder.append(",T1.SIGN_DATE signDate ");
        stringBuilder.append(",T1.PRICE price ");
        stringBuilder.append("FROM CNT_CONTRACT T1 ");
        return stringBuilder;
    }
//  Hungnx 130618 end

    /**
     * getCntContractByCode: get contract by code
     *
     * @param code
     * @return
     */
    public CntContractDTO getCntContractByCode(String code) {
        String sql = "SELECT\n" +
                "    cnt_contract_id cntContractId,\n" +
                "    code,\n" +
                "    sign_date signDate,\n" +
                "    price,\n" +
                "    CONTRACT_TYPE_O contractTypeO,\n" +
                "    HCQT_PROJECT_ID hcqtProjectId,\n" +
                "    UNIT_SETTLEMENT unitSettlement\n" +
                "    ,CNT_CONTRACT_REVENUE cntContractRevenue " +
                "FROM\n" +
                "    cnt_contract\n" +
                "WHERE\n" +
                "    status != 0 AND CONTRACT_TYPE!=1 \n" +
                "    AND ( upper(code) = upper(:code) )";
        SQLQuery query = getSession().createSQLQuery(sql);

        query.addScalar("cntContractId", new LongType());
        query.addScalar("code", new StringType());
        query.addScalar("signDate", new DateType());
        query.addScalar("price", new DoubleType());
        query.addScalar("contractTypeO", new LongType());
        query.addScalar("hcqtProjectId", new LongType());
        query.addScalar("unitSettlement", new StringType());
        query.addScalar("cntContractRevenue", new StringType());

        query.setResultTransformer(Transformers.aliasToBean(CntContractDTO.class));
        query.setParameter("code", code);

        return query.list().size() > 0 ? (CntContractDTO) query.list().get(0) : null;
    }
    
    public Map<Long, String> mapContractUnitSettlementByLstContract(List<String> lstCode){
    	StringBuilder sql = new StringBuilder("SELECT " +
                "    cnt_contract_id cntContractId, " +
                "    code code, " +
                "    UNIT_SETTLEMENT unitSettlement " +
                " FROM " +
                "    cnt_contract " +
                " WHERE " +
                "    status != 0 AND CONTRACT_TYPE!=1 " +
                "    AND upper(code) in (:lstCode) ");
    	
    	SQLQuery query = getSession().createSQLQuery(sql.toString());

        query.addScalar("cntContractId", new LongType());
        query.addScalar("code", new StringType());
        query.addScalar("unitSettlement", new StringType());

        query.setResultTransformer(Transformers.aliasToBean(CntContractDTO.class));
        query.setParameterList("lstCode", lstCode);
        
        Map<Long, String> mapData = new HashMap<>();
        List<CntContractDTO> ls = query.list();
        
        for(CntContractDTO contract : ls) {
        	mapData.put(contract.getCntContractId(), contract.getUnitSettlement());
        }
        
        return mapData;
    }

    public CntContractBO getContractByContractId(Long contractId){
        String sql = "SELECT * FROM CNT_CONTRACT c WHERE 1 = 1 and c.CNT_CONTRACT_ID = :contractId";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.addEntity(CntContractBO.class);
        query.setParameter("contractId", contractId);
        return query.list().size() > 0 ? (CntContractBO) query.list().get(0) : null;
    }

    @SuppressWarnings("unchecked")
    public CntContractDTO getById(Long id) {
        StringBuilder stringBuilder = getSelectAllQueryAllColumn();
        stringBuilder.append("WHERE T1.CNT_CONTRACT_ID = :cntContractId ");

        SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());

        query.addScalar("cntContractId", new LongType());
        query.addScalar("code", new StringType());
        query.addScalar("name", new StringType());
        query.addScalar("signDate", new DateType());
        query.addScalar("startTime", new DateType());
        query.addScalar("endTime", new DateType());
        query.addScalar("price", new DoubleType());
        query.addScalar("appendixContract", new DoubleType());
        query.addScalar("numStation", new DoubleType());
        query.addScalar("biddingPackageId", new LongType());
        query.addScalar("biddingPackageName", new StringType());
        query.addScalar("catPartnerId", new LongType());
        query.addScalar("catPartnerName", new StringType());
        query.addScalar("signerPartner", new StringType());
        query.addScalar("sysGroupId", new LongType());
        query.addScalar("sysGroupName", new StringType());
        query.addScalar("signerGroup", new LongType());
        query.addScalar("signerGroupName", new StringType());
        query.addScalar("status", new LongType());
        query.addScalar("contractType", new LongType());
        query.addScalar("cntContractParentId", new DoubleType());
        query.addScalar("cntContractParentCode", new StringType());
        query.addScalar("hcqtProjectId", new LongType());
        query.addScalar("hcqtProjectCode", new StringType());
        query.addScalar("cntContractApprove", new LongType());

        query.setParameter("cntContractId", id);

        query.setResultTransformer(Transformers.aliasToBean(CntContractDTO.class));

        return (CntContractDTO) query.uniqueResult();
    }
}
