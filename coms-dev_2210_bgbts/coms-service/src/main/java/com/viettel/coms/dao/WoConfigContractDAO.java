package com.viettel.coms.dao;

import com.viettel.coms.bo.WoConfigContractCommitteeBO;
import com.viettel.coms.dto.WoConfigContractCommitteeDTO;
import com.viettel.coms.dto.WoSimpleStationDTO;
import com.viettel.coms.dto.WoTypeDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
@Transactional
public class WoConfigContractDAO extends BaseFWDAOImpl<WoConfigContractCommitteeBO, Long> {
    public WoConfigContractDAO(){this.model = new WoConfigContractCommitteeBO();}
    public WoConfigContractDAO(Session session) {
        this.session = session;
    }
    public void delete(Long woTypeId) {
        StringBuilder sql = new StringBuilder("UPDATE WO_TYPE set status = 0  where ID = :woTypeId");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("woTypeId", woTypeId);
        query.executeUpdate();
    }

    public WoConfigContractCommitteeBO getOneRaw(long id){
        return this.get(WoConfigContractCommitteeBO.class, id);
    }

    public List<WoTypeDTO> getByRange(long pageNumber, long pageSize){
        StringBuilder sql = new StringBuilder("select "
                + "ID as woTypeId, WO_TYPE_CODE as woTypeCode, WO_TYPE_NAME as woTypeName, STATUS as status, "
                + " HAS_AP_WORK_SRC as hasApWorkSrc, HAS_CONSTRUCTION as hasConstruction, HAS_WORK_ITEM as hasWorkItem "
                + "from WO_TYPE WHERE STATUS>0 offset :offset rows fetch next :pageSize rows only");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("pageSize", pageSize);
        query.setParameter("offset", pageSize*(pageNumber -1));

        query = mapFields(query);
        query.setResultTransformer(Transformers.aliasToBean(WoTypeDTO.class));

        return query.list();
    }

    public List<WoConfigContractCommitteeDTO> doSearch(WoConfigContractCommitteeDTO dto){
        StringBuilder sql = new StringBuilder(" SELECT wcc.id ,\n" +
                "  wcc.CONTRACT_ID contractId,\n" +
                "  wcc.CONTRACT_CODE contractCode,\n" +
                "  wcc.USER_ID userId,\n" +
                "  wcc.USER_NAME userName,wcc.USER_POSITION userPosition, wcc.USER_ROLE  userRole,\n" +
                "  wcc.STATUS ,\n" +
                "  wcc.CREATED_USER userCreated,\n" +
                "  TO_CHAR(wcc.CREATED_DATE_USER,'dd/mm/yyyy') userCreatedDateStr\n" +
                "FROM WO_CONFIG_CONTRACT_COMMITTEE wcc \n" +
                "WHERE wcc.STATUS = 1 ");

        if (StringUtils.isNotEmpty(dto.getContractCode())) {
            sql.append("  AND LOWER(CONTRACT_CODE) LIKE :contractCode ");
        }
        sql.append(" ORDER BY wcc.CONTRACT_CODE,wcc.USER_NAME,wcc.CREATED_USER ");

        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        if (StringUtils.isNotEmpty(dto.getContractCode())) {
            query.setParameter("contractCode", toSearchStr(dto.getContractCode()));
            queryCount.setParameter("contractCode", toSearchStr(dto.getContractCode()));
        }
        if (dto.getPage() != null && dto.getPageSize() != null) {
            query.setFirstResult((dto.getPage().intValue() - 1) * dto.getPageSize().intValue());
            query.setMaxResults(dto.getPageSize().intValue());
        }

        dto.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
        query = mapFields(query);
        query.setResultTransformer(Transformers.aliasToBean(WoConfigContractCommitteeDTO.class));

        return query.list();
    }

    private String toSearchStr(String str){
        return "%"+str.toLowerCase()+"%";
    }

    private SQLQuery mapFields(SQLQuery query){
        query.addScalar("id", new LongType());
        query.addScalar("contractId", new LongType());
        query.addScalar("contractCode", new StringType());
        query.addScalar("userId", new LongType());
        query.addScalar("userName", new StringType());
        query.addScalar("userPosition", new StringType());
        query.addScalar("userRole", new LongType());
        query.addScalar("status", new LongType());
        query.addScalar("userCreated", new StringType());
        query.addScalar("userCreatedDateStr", new StringType());
        return query;
    }
    public List<WoConfigContractCommitteeDTO> doSearchWoConfigContract(WoConfigContractCommitteeDTO trDto) {
        String selectQuery = " SELECT cc.CNT_CONTRACT_ID contractId, cc.CODE  contractCode FROM CNT_CONTRACT cc WHERE CONTRACT_TYPE_O = 11 AND contract_branch = 6 AND (CNT_CONTRACT_APPROVE = 1 OR CNT_CONTRACT_APPROVE is null) AND STATUS <> 0 ";
        StringBuilder sql = new StringBuilder(selectQuery);
        if (StringUtils.isNotEmpty(trDto.getKeySearch())) {
            sql.append(" AND ( lower(cc.CODE) LIKE :keySearch )");
        }
        sql.append(" ORDER BY cc.CODE DESC ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        if (StringUtils.isNotEmpty(trDto.getKeySearch())) {
            query.setParameter("keySearch", toSearchStr(trDto.getKeySearch()));
        }
        query.addScalar("contractId", new LongType());
        query.addScalar("contractCode", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(WoConfigContractCommitteeDTO.class));
        if (trDto.getPage() != null && trDto.getPageSize() != null) {
            query.setFirstResult((trDto.getPage().intValue() - 1) * trDto.getPageSize().intValue());
            query.setMaxResults(trDto.getPageSize().intValue());
        }
        return query.list();
    }
    public List<WoConfigContractCommitteeDTO> getFtList(WoConfigContractCommitteeDTO trDto) {
        String selectQuery = " SELECT SYS_USER_ID AS userId,\n" +
                "                  EMPLOYEE_CODE    AS userCode,\n" +
                "                  email            AS email,\n" +
                "                  FULL_NAME        AS userName,\n" +
                "                  SYS_GROUP_ID     AS sysGroupId\n" +
                "                FROM SYS_USER\n" +
                "                WHERE STATUS      = 1\n" +
                "                AND Sys_Group_id IN (SELECT SYS_GROUP_ID FROM Sys_Group WHERE PATH LIKE '%/9006003/%') ";
        StringBuilder sql = new StringBuilder(selectQuery);
        if (StringUtils.isNotEmpty(trDto.getKeySearch())) {
            sql.append(" AND ( lower(EMPLOYEE_CODE) LIKE :keySearch OR  lower(FULL_NAME) LIKE :keySearch OR lower(EMAIL) LIKE :keySearch )");
        }
        sql.append(" ORDER BY EMPLOYEE_CODE DESC ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        if (StringUtils.isNotEmpty(trDto.getKeySearch())) {
            query.setParameter("keySearch", toSearchStr(trDto.getKeySearch()));
        }
        query.addScalar("userId", new LongType());
        query.addScalar("userCode", new StringType());
        query.addScalar("userName", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(WoConfigContractCommitteeDTO.class));
        if (trDto.getPage() != null && trDto.getPageSize() != null) {
            query.setFirstResult((trDto.getPage().intValue() - 1) * trDto.getPageSize().intValue());
            query.setMaxResults(trDto.getPageSize().intValue());
        }
        return query.list();
    }
    public int deleteWoConfigContract (Long id,String userId) {
        StringBuilder sql = new StringBuilder("UPDATE WO_CONFIG_CONTRACT_COMMITTEE set status = 0, LAST_UPDATED_DATE = sysdate,LAST_UPDATED_USER =:userId where ID = :id");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("userId", userId);
        query.setParameter("id", id);
        return query.executeUpdate();
    }
    public List<WoConfigContractCommitteeDTO> getFtListByContract(WoConfigContractCommitteeDTO trDto) {
        String selectQuery = " SELECT wc.USER_ID userId,wc.USER_CODE userCode,wc.USER_NAME userName\n" +
                " FROM WO_CONFIG_CONTRACT_COMMITTEE wc,SYS_USER su WHERE wc.STATUS = 1 AND wc.USER_ROLE = 1 AND su.SYS_USER_ID = wc.USER_ID ";
        StringBuilder sql = new StringBuilder(selectQuery);
        if (StringUtils.isNotEmpty(trDto.getKeySearch())) {
            sql.append(" AND ( lower(TRIM(su.EMPLOYEE_CODE)) LIKE :keySearch OR  lower(su.EMAIL) LIKE :keySearch OR  lower(su.FULL_NAME) LIKE :keySearch )");
        }
        if (StringUtils.isNotEmpty(trDto.getContractCode())) {
            sql.append(" AND  lower(TRIM(wc.CONTRACT_CODE)) = lower(:contractCode) ");
        }
        sql.append(" ORDER BY wc.USER_CODE DESC ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        if (StringUtils.isNotEmpty(trDto.getKeySearch())) {
            query.setParameter("keySearch", toSearchStr(trDto.getKeySearch().trim().toLowerCase()));
        }
        if (StringUtils.isNotEmpty(trDto.getContractCode())) {
            query.setParameter("contractCode",trDto.getContractCode().trim());
        }
        query.addScalar("userId", new LongType());
        query.addScalar("userCode", new StringType());
        query.addScalar("userName", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(WoConfigContractCommitteeDTO.class));
        if (trDto.getPage() != null && trDto.getPageSize() != null) {
            query.setFirstResult((trDto.getPage().intValue() - 1) * trDto.getPageSize().intValue());
            query.setMaxResults(trDto.getPageSize().intValue());
        }
        return query.list();
    }
    public boolean checkWoConfigContractCommitte (WoConfigContractCommitteeDTO  trDto) {
        String selectQuery = " SELECT CONTRACT_ID contractId, USER_ID  userId from WO_CONFIG_CONTRACT_COMMITTEE WHERE STATUS = 1 ";
        StringBuilder sql = new StringBuilder(selectQuery);
        if (trDto.getContractId() != null) {
            sql.append(" AND CONTRACT_ID = :contractId ");
        }
        if (trDto.getUserId() != null) {
            sql.append(" AND USER_ID = :userId");
        }
        if (trDto.getUserRole() != null) {
            sql.append(" AND USER_ROLE = :userRole");
        }
        if (trDto.getId() != null) {
            sql.append(" AND ID = :id");
        }
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        if (trDto.getContractId() != null) {
            query.setParameter("contractId",trDto.getContractId());
        }
        if (trDto.getUserId() != null) {
            query.setParameter("userId",trDto.getUserId());
        }
        if (trDto.getUserRole() != null) {
            query.setParameter("userRole",trDto.getUserRole());
        }
        if (trDto.getId() != null) {
            query.setParameter("id",trDto.getId());
        }

        query.addScalar("contractId", new LongType());
        query.addScalar("userId", new LongType());

        query.setResultTransformer(Transformers.aliasToBean(WoConfigContractCommitteeDTO.class));
        if (query.list().size()>0){
            return true;
        }
        return false;
    }
    public boolean checkBooleanContractType (WoConfigContractCommitteeDTO  trDto) {
        String selectQuery = " SELECT cc.CNT_CONTRACT_ID contractId, cc.CODE  contractCode FROM CNT_CONTRACT cc WHERE CONTRACT_TYPE_O = 11 AND (CNT_CONTRACT_APPROVE = 1 OR CNT_CONTRACT_APPROVE is null) AND STATUS <> 0  ";
        StringBuilder sql = new StringBuilder(selectQuery);
        if (trDto.getContractId() != null) {
            sql.append(" AND CNT_CONTRACT_ID = :contractId ");
        }
        if (trDto.getContractCode() != null) {
            sql.append(" AND CODE = :contractCode");
        }
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        if (trDto.getContractId() != null) {
            query.setParameter("contractId",trDto.getContractId());
        }
        if (trDto.getContractCode() != null) {
            query.setParameter("contractCode",trDto.getContractCode().trim());
        }

        query.addScalar("contractId", new LongType());
        query.addScalar("contractCode", new StringType());

        query.setResultTransformer(Transformers.aliasToBean(WoConfigContractCommitteeDTO.class));
        if (query.list().size()>0){
            return  false;
        }
        return true;
    }

    public WoConfigContractCommitteeDTO getWoConfigContract(WoConfigContractCommitteeDTO trDto) {
        String selectQuery = " SELECT cc.CNT_CONTRACT_ID contractId, cc.CODE  contractCode FROM CNT_CONTRACT cc WHERE CONTRACT_TYPE_O = 11 AND contract_branch = 6 AND (CNT_CONTRACT_APPROVE = 1 OR CNT_CONTRACT_APPROVE is null) AND STATUS <> 0 ";
        StringBuilder sql = new StringBuilder(selectQuery);
        if (StringUtils.isNotEmpty(trDto.getContractCode())) {
            sql.append(" AND cc.CODE = :contractCode  ");
        }
        sql.append(" ORDER BY cc.CODE DESC ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        if (StringUtils.isNotEmpty(trDto.getContractCode())) {
            query.setParameter("contractCode", trDto.getContractCode().trim());
        }
        query.addScalar("contractId", new LongType());
        query.addScalar("contractCode", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(WoConfigContractCommitteeDTO.class));
        if (query.list().size()>0){
            return (WoConfigContractCommitteeDTO) query.uniqueResult();
        }
        return null;
    }
}
