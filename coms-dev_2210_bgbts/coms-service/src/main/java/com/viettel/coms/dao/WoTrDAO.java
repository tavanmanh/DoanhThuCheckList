package com.viettel.coms.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.viettel.coms.dto.*;
import com.viettel.coms.utils.ValidateUtils;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ctc.wstx.util.StringUtil;
import com.viettel.coms.bo.WoTrBO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@Repository
@Transactional
public class WoTrDAO extends BaseFWDAOImpl<WoTrBO, Long> {

    private final String ALL_TYPE = "ALL TYPE";
    private final String CREATED_TYPE = "CREATED TYPE";
    private final String ASSIGNED_TYPE = "ASSIGNED TYPE";
    private final String AVAILABLE_TYPE = "AVAILABLE_TYPE";
    private final String TTHT_ID = "242656";
    private final String TTVHKT_ID = "270120";
    private final String TTXDDTHT_ID = "166677";
    private final String TTGPTH_ID = "280483";
    private final String TTCNTT_ID = "280501";
    private final String PKD_ID = "275062";
    private final String PDT_ID = "271149";
    private final String PHC_ID = "271134";
    private final String TT_XDDD_ID = "9006003";

    private String baseSelectStr = "select "
            + " tr.ID as trId, tr.TR_CODE as trCode, tr.TR_NAME as trName, tr.TR_TYPE_ID as trTypeId, tr.CONTRACT_CODE as contractCode, "
            + " tr.PROJECT_CODE as projectCode, tr.USER_CREATED as userCreated, tr.CREATED_DATE as createdDate, "
            + " tr.FINISH_DATE as finishDate, tr.STATE as state, tr.QOUTA_TIME as qoutaTime, tr.EXECUTE_LAT as executeLat, "
            + " tr.EXECUTE_LONG as executeLong, tr.CD_LEVEL_1 as cdLevel1, tr.STATUS as status, tr.CONSTRUCTION_CODE as constructionCode, " +
            " tr.STATION_CODE as stationCode, tr.CONTRACT_ID as contractId, tr.QUANTITY_VALUE as quantityValue ";


    public WoTrDAO() {
        this.model = new WoTrBO();
    }

    public WoTrDAO(Session session) {
        this.session = session;
    }

    public int delete(Long trId) {
        StringBuilder sql = new StringBuilder("UPDATE WO_TR set status = 0 where ID = :trId");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("trId", trId);

        return query.executeUpdate();
    }

    public WoTrBO getOneRaw(long trId) {
        return this.get(WoTrBO.class, trId);
    }

    public WoTrDTO getOneDetails(long trId) {
        StringBuilder sql = new StringBuilder("select "
                + " tr.ID as trId, tr.TR_CODE as trCode, tr.TR_NAME as trName, tr.TR_TYPE_ID as trTypeId, trType.TR_TYPE_CODE as trTypeCode, tr.CONTRACT_CODE as contractCode, "
                + " tr.PROJECT_CODE as projectCode, tr.PROJECT_ID as projectId, tr.USER_CREATED as userCreated, tr.CREATED_DATE as createdDate, "
                + " tr.FINISH_DATE as finishDate, tr.STATE as state, tr.QOUTA_TIME as qoutaTime, tr.EXECUTE_LAT as executeLat, "
                + " tr.EXECUTE_LONG as executeLong, tr.CD_LEVEL_1 as cdLevel1, tr.STATUS as status, "
                + " tr.CONSTRUCTION_CODE as constructionCode, tr.STATION_CODE as stationCode, tr.CONTRACT_ID as contractId, " +
                " cons.CONSTRUCTION_ID as constructionId, cons.CAT_CONSTRUCTION_TYPE_ID as catConstructionTypeId, "
                + " trType.TR_TYPE_NAME as trTypeName, sg.NAME as cdLevel1Name, tr.QUANTITY_VALUE as quantityValue, tr.CUSTOMER_TYPE as customerType, tr.CONTRACT_TYPE as contractType, " +
                "  su.FULL_NAME as userCreatedFullName, tr.CD_LEVEL_2 as cdLevel2, tr.CD_LEVEL_2_NAME as cdLevel2Name" +
                ", cons.STATUS as constructionStatus, TMBT_TARGET tmbtTarget, TMBT_TARGET_DETAIL tmbtTargetDetail, tr.DB_TKDA as dbTkdaDate, tr.DB_TTKDT as dbTtkdtDate, tr.DB_VT as dbVtDate " +

                " from WO_TR tr LEFT JOIN WO_TR_TYPE trType ON tr.TR_TYPE_ID = trType.ID " +

                " left join CONSTRUCTION cons on cons.CODE = tr.CONSTRUCTION_CODE " +
                " left join SYS_GROUP sg on sg.SYS_GROUP_ID = tr.CD_LEVEL_1 " +
                " left join SYS_USER su on su.LOGIN_NAME = tr.USER_CREATED " +
                " WHERE tr.STATUS>0 AND tr.ID = :paramId fetch next 1 row only ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("paramId", trId);

        query.addScalar("trTypeCode", new StringType());
        query.addScalar("trTypeName", new StringType());
        query.addScalar("constructionId", new LongType());
        query.addScalar("catConstructionTypeId", new LongType());
        query.addScalar("cdLevel1Name", new StringType());
        query.addScalar("quantityValue", new DoubleType());
        query.addScalar("customerType", new LongType());
        query.addScalar("userCreatedFullName", new StringType());
        query.addScalar("contractType", new LongType());
        query.addScalar("cdLevel2", new StringType());
        query.addScalar("cdLevel2Name", new StringType());
        query.addScalar("projectId", new LongType());
        query.addScalar("constructionStatus", new LongType());
        query.addScalar("tmbtTarget", new LongType());
        query.addScalar("tmbtTargetDetail", new StringType());
        query.addScalar("dbTkdaDate", new DateType());
        query.addScalar("dbTtkdtDate", new DateType());
        query.addScalar("dbVtDate", new DateType());
        query = mapFields(query);

        return (WoTrDTO) query.uniqueResult();
    }

    public List<WoTrDTO> getByRange(long pageNumber, long pageSize) {
        String selectQuery = baseSelectStr + " from WO_TR tr WHERE tr.STATUS>0 ";
        StringBuilder sql = new StringBuilder(selectQuery + " offset :offset rows fetch next :pageSize rows only");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("pageSize", pageSize);
        query.setParameter("offset", pageSize * (pageNumber - 1));

        query = mapFields(query);

        return query.list();
    }

    private SQLQuery mapFields(SQLQuery query) {
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
        query.addScalar("constructionCode", new StringType());
        query.addScalar("stationCode", new StringType());
        query.addScalar("contractId", new LongType());
        query.addScalar("quantityValue", new DoubleType());
        query.setResultTransformer(Transformers.aliasToBean(WoTrDTO.class));
        return query;
    }

    public List<WoTrDTO> doSearch(WoTrDTO trDto, List<String> groupIdList, String searchType, boolean isDTHT, boolean isGPTH) {
        String selectQuery = baseSelectStr + " ,tr.cd_Level_2 as cdLevel2 , su.FULL_NAME as userCreatedFullName, " +
                " su.EMAIL as userCreatedEmail, trType.TR_TYPE_CODE as trTypeCode, trType.TR_TYPE_NAME as trTypeName" +
                ", tr.CD_LEVEL_1_NAME cdLevel1Name, TMBT_TARGET tmbtTarget, TMBT_TARGET_DETAIL tmbtTargetDetail, DB_TKDA dbTkdaDate, DB_TTKDT dbTtkdtDate, DB_VT dbVtDate ";
        selectQuery += " from WO_TR tr left join SYS_USER su on tr.USER_CREATED = su.LOGIN_NAME and su.status=1 " +
                " left join WO_TR_TYPE trType on tr.TR_TYPE_ID = trType.ID ";

        selectQuery += "  WHERE tr.STATUS>0 and (tr.CD_LEVEL_1 is not null OR (tr.STATE='REJECT_CD' AND tr.CD_LEVEL_1 is null)) "; //Huypq-08022021-add cdlv1 not null
        StringBuilder sql = new StringBuilder(selectQuery);

        if (trDto.getTrId() != null) {
            sql.append(" AND tr.ID = :trId ");
        }

        if (StringUtils.isNotEmpty(trDto.getKeySearch())) {
            sql.append(" AND ( lower(tr.TR_CODE) LIKE :keySearch or lower(tr.TR_NAME) like :keySearch )");
        }

        if (trDto.getTrTypeId() != null) {
            sql.append(" AND tr.TR_TYPE_ID = :trTypeId ");
        }

        //HienLT56 start 16102020
        if (trDto.getListTrTypeId() != null && trDto.getListTrTypeId().size() > 0) {
            sql.append(" AND tr.TR_TYPE_ID in (:listTrTypeId) ");
        }
        //HienLT56 end 16102020

        //HienLT56 start 01122020
        if (trDto.getListTrTypeCode() != null && trDto.getListTrTypeCode().size() > 0) {
            sql.append(" AND trType.TR_TYPE_CODE in (:listTrTypeCode) ");
        }
        //HienLT56 end 01122020
        if (StringUtils.isNotEmpty(trDto.getState())) {
            sql.append(" AND tr.STATE = :state ");
        }

        if (trDto.getFinishDate() != null) {
            sql.append(" AND tr.FINISH_DATE = :finishDate ");
        }

        //Huypq-03122021-start
        if(searchType!=null && "VALIDATE".equalsIgnoreCase(searchType)) {
        	if (StringUtils.isNotEmpty(trDto.getContractCode())) {
                sql.append(" AND tr.CONTRACT_CODE = :contractCode ");
            }
            if (StringUtils.isNotEmpty(trDto.getConstructionCode())) {
                sql.append(" AND tr.CONSTRUCTION_CODE = :constructionCode ");
            }
        } else {
        	if (StringUtils.isNotEmpty(trDto.getContractCode())) {
                sql.append(" AND tr.CONTRACT_CODE LIKE :contractCode ");
            }
            if (StringUtils.isNotEmpty(trDto.getConstructionCode())) {
                sql.append(" AND lower(tr.CONSTRUCTION_CODE) like :constructionCode ");
            }
        }
        //Huy-end

        if (StringUtils.isNotEmpty(trDto.getProjectCode())) {
            sql.append(" AND tr.PROJECT_CODE = :projectCode ");
        }

        if (StringUtils.isNotEmpty(trDto.getStationCode())) {
            sql.append(" AND tr.STATION_CODE = :stationCode ");
        }

        if (StringUtils.isNotEmpty(trDto.getUserCreated())) {
            sql.append(" AND tr.USER_CREATED LIKE :userCreated ");
        }

        if (trDto.getCreatedDateFrom() != null) {
            sql.append(" AND trunc(tr.CREATED_DATE) >= trunc(:createdDateFrom) ");
        }

        if (trDto.getCreatedDateTo() != null) {
            sql.append(" AND trunc(tr.CREATED_DATE) <= trunc(:createdDateTo) ");
        }

        if (trDto.getFinishDateFrom() != null) {
            sql.append(" AND trunc(tr.FINISH_DATE) >= trunc(:finishDateFrom) ");
        }

        if (trDto.getFinishDateTo() != null) {
            sql.append(" AND trunc(tr.FINISH_DATE) <= trunc(:finishDateTo) ");
        }

        if (trDto.getAioContractId() != null)
            sql.append(" AND trType.TR_TYPE_CODE like '%AIO%' AND tr.CONTRACT_ID = :aioContractId ");

        if (trDto.getCodeRange() != null && trDto.getCodeRange().size() > 0) {
            sql.append(" AND tr.TR_CODE in (:codeRange) ");
        }

        if (StringUtils.isNotEmpty(trDto.getLoggedInUser())) {
            if (ALL_TYPE.equalsIgnoreCase(searchType)) {
                sql.append(" AND ( tr.USER_CREATED = :username ");

                if (groupIdList.size() > 0) {
                    sql.append(" OR tr.CD_LEVEL_1 in (:groupIdList) OR tr.CD_LEVEL_2 in (:groupIdList) OR tr.GROUP_CREATED in (:groupIdList) ");
                }

                if (isDTHT) {
                    sql.append(" OR tr.TR_CODE like '%ĐTHT%'  ");
                }

                if (isGPTH) {
                    sql.append(" OR tr.TR_CODE like '%GPTH%'  ");
                }

                sql.append(" ) ");
            } else if (AVAILABLE_TYPE.equalsIgnoreCase(searchType)) {
//                sql.append(" AND ( tr.USER_CREATED = :username ");
                if (groupIdList.size() > 0) {
                    sql.append(" AND ( nvl(tr.CD_LEVEL_2, tr.CD_LEVEL_1) in (:groupIdList) ");
                }
                sql.append(" ) AND tr.STATE != 'OK' AND tr.STATE != 'UNASSIGN' AND tr.STATE != 'ASSIGN_CD' AND tr.STATE != 'REJECT_CD' ");
            }
        }
        if(trDto.isFilterAutoExpire()) {
        	sql.append(" AND tr.AUTO_EXPIRE = '1' ");
        }

        sql.append(" ORDER BY tr.ID DESC ");

        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        if (trDto.getTrId() != null) {
            query.setParameter("trId", trDto.getTrId());
            queryCount.setParameter("trId", trDto.getTrId());
        }

        if (StringUtils.isNotEmpty(trDto.getKeySearch())) {
            query.setParameter("keySearch", toSearchStr(trDto.getKeySearch()));
            queryCount.setParameter("keySearch", toSearchStr(trDto.getKeySearch()));
        }

        //Huypq-03122021-start
        if(searchType!=null && "VALIDATE".equalsIgnoreCase(searchType)) {
        	if (StringUtils.isNotEmpty(trDto.getContractCode())) {
                query.setParameter("contractCode", trDto.getContractCode());
                queryCount.setParameter("contractCode", trDto.getContractCode());
            }
        	
        	if (StringUtils.isNotEmpty(trDto.getConstructionCode())) {
                query.setParameter("constructionCode", trDto.getConstructionCode());
                queryCount.setParameter("constructionCode", trDto.getConstructionCode());
            }
        } else {
        	if (StringUtils.isNotEmpty(trDto.getContractCode())) {
                query.setParameter("contractCode", trDto.getContractCode());
                queryCount.setParameter("contractCode", trDto.getContractCode());
            }
        	
        	if (StringUtils.isNotEmpty(trDto.getConstructionCode())) {
                 query.setParameter("constructionCode", toSearchStr(trDto.getConstructionCode()));
                 queryCount.setParameter("constructionCode", toSearchStr(trDto.getConstructionCode()));
            }
        }
        //Huy-end
        if (StringUtils.isNotEmpty(trDto.getProjectCode())) {
            query.setParameter("projectCode", trDto.getProjectCode());
            queryCount.setParameter("projectCode", trDto.getProjectCode());
        }

        if (StringUtils.isNotEmpty(trDto.getStationCode())) {
            query.setParameter("stationCode", trDto.getStationCode());
            queryCount.setParameter("stationCode", trDto.getStationCode());
        }

        if (StringUtils.isNotEmpty(trDto.getUserCreated())) {
            query.setParameter("userCreated", trDto.getUserCreated());
            queryCount.setParameter("userCreated", trDto.getUserCreated());
        }

        if (trDto.getTrTypeId() != null) {
            query.setParameter("trTypeId", trDto.getTrTypeId());
            queryCount.setParameter("trTypeId", trDto.getTrTypeId());
        }

        //HienLT56 start 16102020
        if (trDto.getListTrTypeId() != null && trDto.getListTrTypeId().size() > 0) {
            query.setParameterList("listTrTypeId", trDto.getListTrTypeId());
            queryCount.setParameterList("listTrTypeId", trDto.getListTrTypeId());
        }
        //HienLT56 end 16102020
        //HienLT56 start 01122020
        if (trDto.getListTrTypeCode() != null && trDto.getListTrTypeCode().size() > 0) {
            query.setParameterList("listTrTypeCode", trDto.getListTrTypeCode());
            queryCount.setParameterList("listTrTypeCode", trDto.getListTrTypeCode());
        }
        //HienLT56 end 01122020
        if (StringUtils.isNotEmpty(trDto.getState())) {
            query.setParameter("state", trDto.getState());
            queryCount.setParameter("state", trDto.getState());
        }

        if (trDto.getFinishDate() != null) {
            query.setParameter("finishDate", trDto.getFinishDate());
            queryCount.setParameter("finishDate", trDto.getFinishDate());
        }

        if (trDto.getCreatedDateFrom() != null) {
            query.setParameter("createdDateFrom", trDto.getCreatedDateFrom());
            queryCount.setParameter("createdDateFrom", trDto.getCreatedDateFrom());
        }

        if (trDto.getCreatedDateTo() != null) {
            query.setParameter("createdDateTo", trDto.getCreatedDateTo());
            queryCount.setParameter("createdDateTo", trDto.getCreatedDateTo());
        }

        if (trDto.getFinishDateFrom() != null) {
            query.setParameter("finishDateFrom", trDto.getFinishDateFrom());
            queryCount.setParameter("finishDateFrom", trDto.getFinishDateFrom());
        }

        if (trDto.getFinishDateTo() != null) {
            query.setParameter("finishDateTo", trDto.getFinishDateTo());
            queryCount.setParameter("finishDateTo", trDto.getFinishDateTo());
        }

        if (trDto.getAioContractId() != null) {
            query.setParameter("aioContractId", trDto.getAioContractId());
            queryCount.setParameter("aioContractId", trDto.getAioContractId());
        }

        if (trDto.getCodeRange() != null && trDto.getCodeRange().size() > 0) {
            query.setParameterList("codeRange", trDto.getCodeRange());
            queryCount.setParameterList("codeRange", trDto.getCodeRange());
        }

        if (StringUtils.isNotEmpty(trDto.getLoggedInUser())) {
            if (ALL_TYPE.equalsIgnoreCase(searchType)) query.setParameter("username", trDto.getLoggedInUser());
            if (groupIdList.size() > 0) query.setParameterList("groupIdList", groupIdList);
            if (ALL_TYPE.equalsIgnoreCase(searchType)) queryCount.setParameter("username", trDto.getLoggedInUser());
            if (groupIdList.size() > 0) queryCount.setParameterList("groupIdList", groupIdList);
        }

        query.addScalar("userCreatedFullName", new StringType());
        query.addScalar("userCreatedEmail", new StringType());
        query.addScalar("trTypeCode", new StringType());
        query.addScalar("trTypeName", new StringType());
        query.addScalar("cdLevel2", new StringType());
        query.addScalar("cdLevel1Name", new StringType());
        query.addScalar("tmbtTarget", new LongType());
        query.addScalar("tmbtTargetDetail", new StringType());
        query.addScalar("dbTkdaDate", new DateType());
        query.addScalar("dbTtkdtDate", new DateType());
        query.addScalar("dbVtDate", new DateType());
        query = mapFields(query);

        if (trDto.getPage() != null && trDto.getPageSize() != null) {
            query.setFirstResult((trDto.getPage().intValue() - 1) * trDto.getPageSize().intValue());
            query.setMaxResults(trDto.getPageSize().intValue());
        }

        trDto.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

        return query.list();
    }

    public WoSimpleSysGroupDTO getSysUserGroup(String username) {
        WoSimpleSysGroupDTO noGroup = new WoSimpleSysGroupDTO();
        noGroup.setSysGroupId(0l);
        StringBuilder sql = new StringBuilder("SELECT sg.SYS_GROUP_ID as sysGroupId, sg.CODE as code, sg.PARENT_ID as parentGroupId, sg.NAME as groupName, sg.GROUP_NAME_LEVEL1 as groupNameLevel1, " +
                " sg.GROUP_NAME_LEVEL2 as groupNameLevel2, sg.GROUP_NAME_LEVEL3 as groupNameLevel3, sg.GROUP_LEVEL as groupLevel from SYS_GROUP sg " +
                " join SYS_USER su ON sg.SYS_GROUP_ID = su.SYS_GROUP_ID " +
                " WHERE LOGIN_NAME = :username ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());

        try {
            query.setParameter("username", username);

            query.addScalar("sysGroupId", new LongType());
            query.addScalar("code", new StringType());
            query.addScalar("parentGroupId", new LongType());
            query.addScalar("groupName", new StringType());
            query.addScalar("groupNameLevel1", new StringType());
            query.addScalar("groupNameLevel2", new StringType());
            query.addScalar("groupNameLevel3", new StringType());
            query.addScalar("groupLevel", new IntegerType());

            query.setResultTransformer(Transformers.aliasToBean(WoSimpleSysGroupDTO.class));

            return (WoSimpleSysGroupDTO) query.uniqueResult() != null ? (WoSimpleSysGroupDTO) query.uniqueResult() : noGroup;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return noGroup;
    }

    public String getCreatorPrefix(long groupId) {
        StringBuilder sql = new StringBuilder("SELECT NAME from APP_PARAM WHERE CODE = :groupId and PAR_TYPE = 'TR_CREATOR' ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("groupId", groupId);
        query.addScalar("NAME", new StringType());
        return (String) query.list().get(0);
    }

    public long countTrInAType(long typeId) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) as total from WO_TR WHERE TR_TYPE_ID = :typeId and STATUS>0 ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("typeId", typeId);
        query.addScalar("total", new LongType());
        return (long) query.list().get(0);
    }

    public long getSysUserId(String username) {
        StringBuilder sql = new StringBuilder("SELECT SYS_USER_ID as sysUserId from SYS_USER WHERE LOGIN_NAME = :username");
        SQLQuery query = getSession().createSQLQuery(sql.toString());

        try {
            query.setParameter("username", username);
            query.addScalar("sysUserId", new LongType());
            return (long) query.uniqueResult();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public boolean checkIsCdLevel1(Long groupId) {
        StringBuilder sql = new StringBuilder(" SELECT CODE as code from APP_PARAM WHERE CODE = :groupId AND PAR_TYPE = 'CD_LEVEL_1' ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("groupId", groupId.toString());
        query.addScalar("code", new StringType());
        List<String> lst = query.list();

        if (lst.size() > 0) return true;
        return false;
    }

    public boolean checkIsTrCreator(Long groupId) {
        StringBuilder sql = new StringBuilder(" SELECT CODE as code from APP_PARAM WHERE CODE = :groupId AND PAR_TYPE = 'TR_CREATOR' ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("groupId", groupId.toString());
        query.addScalar("code", new StringType());
        List<String> lst = query.list();

        if (lst.size() > 0) return true;
        return false;
    }

    private String toSearchStr(String str) {
        return "%" + str.toLowerCase() + "%";
    }

    public List<WoSimpleProjectDTO> getAvailableProjects() {
        String sql = new String(" select PROJECT_ID as projectId, PROJECT_NAME as projectName, PROJECT_CODE as projectCode, START_DATE as startDate, END_DATE as endDate " +
                " from CONSTRUCTION_PROJECT where STATUS>0 ");
        SQLQuery query = getSession().createSQLQuery(sql);

        query.addScalar("projectId", new LongType());
        query.addScalar("projectName", new StringType());
        query.addScalar("projectCode", new StringType());
        query.addScalar("startDate", new DateType());
        query.addScalar("endDate", new DateType());
        query.setResultTransformer(Transformers.aliasToBean(WoSimpleProjectDTO.class));
        return query.list();

    }

    public List<WoSimpleProjectDTO> doSearchProjects(WoTrDTO dto) {
        String sql = " select PROJECT_ID as projectId, PROJECT_NAME as projectName, PROJECT_CODE as projectCode, START_DATE as startDate, END_DATE as endDate " +
                " from CONSTRUCTION_PROJECT where STATUS>0 ";
        if (dto.getProjectCodeRange() != null && dto.getProjectCodeRange().size() > 0)
            sql += " and PROJECT_CODE in (:codeRange) ";

        if (StringUtils.isNotEmpty(dto.getKeySearch()))
            sql += " and (LOWER(PROJECT_NAME) like :keysearch or LOWER(PROJECT_CODE) like :keysearch ) fetch next 20 rows only ";
        SQLQuery query = getSession().createSQLQuery(sql);

        if (dto.getProjectCodeRange() != null && dto.getProjectCodeRange().size() > 0)
            query.setParameterList("codeRange", dto.getProjectCodeRange());
        if (StringUtils.isNotEmpty(dto.getKeySearch()))
            query.setParameter("keysearch", "%" + dto.getKeySearch().toLowerCase() + "%");

        query.addScalar("projectId", new LongType());
        query.addScalar("projectName", new StringType());
        query.addScalar("projectCode", new StringType());
        query.addScalar("startDate", new DateType());
        query.addScalar("endDate", new DateType());
        query.setResultTransformer(Transformers.aliasToBean(WoSimpleProjectDTO.class));

        return query.list();

    }

    public List<WoSimpleContractDTO> getAvailableContracts() {

        String sql = new String(" select CNT_CONTRACT_ID as contractId, CODE as contractCode, NAME as contractName, PROJECT_CONTRACT_ID as projectId, CONSTRUCTION_CODE as constructionCode " +
                " from CNT_CONTRACT where STATUS>0 ");

        SQLQuery query = getSession().createSQLQuery(sql);
        query.addScalar("contractId", new LongType());
        query.addScalar("contractCode", new StringType());
        query.addScalar("contractName", new StringType());
        query.addScalar("projectId", new LongType());
        query.addScalar("constructionCode", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(WoSimpleContractDTO.class));

        return query.list();
    }

    public List<WoSimpleContractDTO> doSearchContracts(WoTrDTO dto) {
        String sql = new String(" select CNT_CONTRACT_ID as contractId, CODE as contractCode, NAME as contractName, PROJECT_CONTRACT_ID as projectId, CONSTRUCTION_CODE as constructionCode " +
        		" ,contract_type_o contractTypeO " + //Huypq-21052021-add
                " from CNT_CONTRACT where STATUS>0 AND (CNT_CONTRACT_APPROVE=1 OR CNT_CONTRACT_APPROVE IS NULL) "); //Huypq-31122020-add CNT_CONTRACT_APPROVE=1
        // Unikom_20210528_start
        if (StringUtils.isNotEmpty(dto.getCreateDoanhThuDomain()) && "DOANHTHU".equalsIgnoreCase(dto.getContractFilter())) {
            String domain = dto.getCreateDoanhThuDomain();
            if (domain.contains(TTGPTH_ID)) sql += " and contract_type=0 and (contract_branch=1 OR CNT_CONTRACT_ID in (select CNT_CONTRACT_ID from CNT_CONTRACT_REVENUE where BRANCH=1 and status!=0)) ";
            if (domain.contains(TTHT_ID)) sql += " and contract_type=0 and (contract_branch=2 OR CNT_CONTRACT_ID in (select CNT_CONTRACT_ID from CNT_CONTRACT_REVENUE where BRANCH=2 and status!=0)) ";
            if (domain.contains(TTXDDTHT_ID)) sql += " and contract_type=0 and (contract_branch=3 OR CNT_CONTRACT_ID in (select CNT_CONTRACT_ID from CNT_CONTRACT_REVENUE where BRANCH=3 and status!=0)) ";
            if (domain.contains(TTVHKT_ID)) sql += " and contract_type=0 and (contract_branch=4 OR CNT_CONTRACT_ID in (select CNT_CONTRACT_ID from CNT_CONTRACT_REVENUE where BRANCH=4 and status!=0)) ";
            if (domain.contains(TTCNTT_ID)) sql += " and contract_type=0 and (contract_branch=5 OR CNT_CONTRACT_ID in (select CNT_CONTRACT_ID from CNT_CONTRACT_REVENUE where BRANCH=5 and status!=0)) ";
            if (domain.contains(TT_XDDD_ID)) sql += " and contract_type=0 and (contract_branch=6 OR CNT_CONTRACT_ID in (select CNT_CONTRACT_ID from CNT_CONTRACT_REVENUE where BRANCH=6 and status!=0)) ";
            if (domain.contains(PKD_ID)) sql += " and contract_type=6 ";
            if (domain.contains(PDT_ID)) sql += " and contract_type=3 ";
            if (domain.contains(PHC_ID)) sql += " and contract_type=6 ";
        } 
        //Huypq-29062021-start comment
//        else if (StringUtils.isNotEmpty(dto.getCreateWoDomain())) {
//            String domain = dto.getCreateWoDomain();
//            sql += " and (UNIT_SETTLEMENT = 7 ";
//            if (domain.contains(TTGPTH_ID)) {
//                sql += " OR UNIT_SETTLEMENT = 1 ";
//            } else if (domain.contains(TTHT_ID)) {
//                sql += " OR UNIT_SETTLEMENT = 2 ";
//            } else if (domain.contains(TTXDDTHT_ID)) {
//                sql += " OR UNIT_SETTLEMENT = 3 ";
//            } else if (domain.contains(TTVHKT_ID)) {
//                sql += " OR UNIT_SETTLEMENT = 4 ";
//            } else if (domain.contains(TTCNTT_ID)) {
//                sql += " OR UNIT_SETTLEMENT = 5 ";
//            } else if (domain.contains(TT_XDDD_ID)) {
//                sql += " OR UNIT_SETTLEMENT = 6 ";
//            }
//            sql += ")";
//        } 
        //Huypq-29062021-end
        else if ("UCTT".equalsIgnoreCase(dto.getContractFilter())) {
            sql += " and (contract_type=0 or contract_type=7) ";
        } else {
            sql += " and CONTRACT_TYPE = 0 ";
        }
        // Unikom_20210528_end

        //Huypq-20052021-start
        if(!StringUtils.isNotBlank(dto.getTrCode()) && StringUtils.isNotBlank(dto.getApWorkSrc())) {
        	if(dto.getApWorkSrc().equals("1")) {
        		sql += " and (contract_type_o=1 OR CNT_CONTRACT_ID in (select CNT_CONTRACT_ID from CNT_CONTRACT_REVENUE where BRANCH=2 and status!=0)) ";
        	} else if(dto.getApWorkSrc().equals("2")) {
        		sql += " and (contract_type_o=2 OR CNT_CONTRACT_ID in (select CNT_CONTRACT_ID from CNT_CONTRACT_REVENUE where BRANCH=2 and status!=0)) ";
        	} else if(dto.getApWorkSrc().equals("3")) {
        		sql += " and (contract_type_o=3 OR CNT_CONTRACT_ID in (select CNT_CONTRACT_ID from CNT_CONTRACT_REVENUE where BRANCH=2 and status!=0)) ";
        	} else if(dto.getApWorkSrc().equals("4")) {
        		sql += " and (contract_type_o=10 OR CNT_CONTRACT_ID in (select CNT_CONTRACT_ID from CNT_CONTRACT_REVENUE where BRANCH=6 and status!=0)) ";
        	}
        }
        //Huy-end
        
        // Neu TR do DTHT thi CONTRACT_TYPE_O = 10
        if (dto.getCreateTrDomain() != null && dto.getCreateTrDomain().contains(TTXDDTHT_ID)) {
            sql += " and (CONTRACT_TYPE = 8 or (CONTRACT_TYPE = 0 and CONTRACT_TYPE_O = 10 )  ";
            if(dto.getTrTypeId() != null && dto.getTrTypeCode().equals("Cơ điện HTCT")) {
            	sql += " or contract_branch = 4  ";
            }
            sql += " ) ";
        }

        // Unikom_20210528_end
        if (dto.getContractCodeRange() != null && dto.getContractCodeRange().size() > 0)
            sql += " and CODE in (:codeRange) ";
        if (StringUtils.isNotEmpty(dto.getKeySearch()))
            sql += " and (LOWER(CODE) like :keysearch or LOWER(NAME) like :keysearch ) fetch next 20 rows only ";

        
        SQLQuery query = getSession().createSQLQuery(sql);

        if (dto.getContractCodeRange() != null && dto.getContractCodeRange().size() > 0)
            query.setParameterList("codeRange", dto.getContractCodeRange());
        if (StringUtils.isNotEmpty(dto.getKeySearch()))
            query.setParameter("keysearch", "%" + dto.getKeySearch().replaceAll("&", "&&").toLowerCase() + "%"); //Huypq-20012021-start

        query.addScalar("contractId", new LongType());
        query.addScalar("contractCode", new StringType());
        query.addScalar("contractName", new StringType());
        query.addScalar("projectId", new LongType());
        query.addScalar("constructionCode", new StringType());
        query.addScalar("contractTypeO", new LongType());
        
        query.setResultTransformer(Transformers.aliasToBean(WoSimpleContractDTO.class));

        return query.list();
    }

    public List<WoSimpleContractDTO> doSearchAIOContracts(WoTrDTO dto) {

        String sql = " select CONTRACT_ID as contractId, CONTRACT_CODE as contractCode, CONTRACT_CONTENT as contractName " +
                " from AIO_CONTRACT where STATUS >0 ";

        sql += " and (LOWER(CONTRACT_CODE) like :keysearch or LOWER(CONTRACT_CONTENT) like :keysearch ) fetch next 20 rows only ";

        SQLQuery query = getSession().createSQLQuery(sql);

        query.setParameter("keysearch", "%" + dto.getKeySearch().toLowerCase() + "%");

        query.addScalar("contractId", new LongType());
        query.addScalar("contractCode", new StringType());
        query.addScalar("contractName", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(WoSimpleContractDTO.class));

        return query.list();
    }

    public List<WoSimpleConstructionDTO> doSearchConstruction(WoTrDTO trDto) {

        String sql = " select cons.CODE as constructionCode, cons.NAME as constructionName, cons.CONSTRUCTION_ID as constructionId, " +
                " cons.CAT_STATION_ID as stationId, cons.CAT_CONSTRUCTION_TYPE_ID as catConstructionTypeId, cons.status as status " +
                " from construction cons where status>0 ";
        if (StringUtils.isNotEmpty(trDto.getKeySearch()))
            sql += " and lower(cons.CODE) like :keySearch fetch next 10 rows only ";

        SQLQuery query = getSession().createSQLQuery(sql);

        if (StringUtils.isNotEmpty(trDto.getKeySearch()))
            query.setParameter("keySearch", '%' + trDto.getKeySearch().toLowerCase() + '%');

        mapConstructionFields(query);

        return query.list();
    }

    public List<WoSimpleConstructionDTO> getConstructionByContract(WoTrDTO trDto) {
        String contractCode = trDto.getContractCode();

        String sql = " select cons.CODE as constructionCode, cons.NAME as constructionName, cons.CONSTRUCTION_ID as constructionId, " +
                " cons.CAT_STATION_ID as stationId, cons.CAT_CONSTRUCTION_TYPE_ID as catConstructionTypeId, cons.status as status" +
                " from CNT_CONTRACT ct " +
                " left join CNT_CONSTR_WORK_ITEM_TASK cc on cc.CNT_CONTRACT_ID = ct.CNT_CONTRACT_ID and cc.status != 0 " +
                " left join CONSTRUCTION cons on cons.CONSTRUCTION_ID = cc.CONSTRUCTION_ID where cons.STATUS != 0 " +
                " and ct.CODE = :contractCode ";
        if (StringUtils.isNotEmpty(trDto.getKeySearch()))
            sql += " and lower(cons.CODE) like :keySearch fetch next 10 rows only ";

        SQLQuery query = getSession().createSQLQuery(sql);

        query.setParameter("contractCode", contractCode);
        if (StringUtils.isNotEmpty(trDto.getKeySearch()))
            query.setParameter("keySearch", '%' + trDto.getKeySearch().toLowerCase() + '%');

        mapConstructionFields(query);

        return query.list();
    }

    public List<WoSimpleConstructionDTO> getConstructionByProject(WoTrDTO trDto) {
        String projectCode = trDto.getProjectCode();

        String sql = new String(" select cons.CODE as constructionCode, cons.NAME as constructionName, cons.CONSTRUCTION_ID as constructionId, " +
                " cons.CAT_STATION_ID as stationId, cons.CAT_CONSTRUCTION_TYPE_ID as catConstructionTypeId, cons.status as status " +
                " from CONSTRUCTION_PROJECT cp " +
                " left join PROJECT_ESTIMATES pe on pe.PROJECT_CODE = cp.PROJECT_CODE " +
                " left join CONSTRUCTION cons on cons.CODE = pe.CONSTRUCTION_CODE where cons.STATUS != 0 " +
                " and cp.PROJECT_CODE = :projectCode");
        SQLQuery query = getSession().createSQLQuery(sql);

        query.setParameter("projectCode", projectCode);

        mapConstructionFields(query);

        return query.list();
    }

    private SQLQuery mapConstructionFields(SQLQuery query) {
        query.addScalar("constructionId", new LongType());
        query.addScalar("constructionCode", new StringType());
        query.addScalar("constructionName", new StringType());
        query.addScalar("stationId", new LongType());
        query.addScalar("catConstructionTypeId", new LongType());
        query.addScalar("status", new LongType());
        query.setResultTransformer(Transformers.aliasToBean(WoSimpleConstructionDTO.class));
        return query;
    }

    public WoSimpleStationDTO getStationById(WoTrDTO trDto) {
        Long stationId = trDto.getStationId();

        String sql = new String(" select \n" +
                "    cs.CAT_STATION_ID as stationId\n" +
                "    , cs.NAME as stationName\n" +
                "    , cs.CODE as stationCode\n" +
                "    , cs.ADDRESS as stationAddress\n" +
                "    , cs.LATITUDE as latitude\n" +
                "    , cs.LONGITUDE as longitude\n" +
                "    , sg.sys_group_id sysGroupId\n" +
                "    , sg.code sysGroupCode\n" +
                "    , sg.name sysGroupName\n" +
                "from CAT_STATION cs\n" +
                "left join (\n" +
                "    select sys_group_id,code, name, PROVINCE_ID from sys_group where (code like '%P.HT%' or code like '%P.XD%')\n" +
                ") sg on cs.cat_province_id = sg.PROVINCE_ID\n" +
                "where cs.STATUS > 0 and cs.CAT_STATION_ID = :stationId ");
        SQLQuery query = getSession().createSQLQuery(sql);

        query.setParameter("stationId", stationId);

        query.addScalar("stationId", new LongType());
        query.addScalar("stationName", new StringType());
        query.addScalar("stationCode", new StringType());
        query.addScalar("stationAddress", new StringType());
        query.addScalar("latitude", new StringType());
        query.addScalar("longitude", new StringType());
        query.addScalar("sysGroupId", new LongType());
        query.addScalar("sysGroupCode", new StringType());
        query.addScalar("sysGroupName", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(WoSimpleStationDTO.class));

        return (WoSimpleStationDTO) query.uniqueResult();
    }

    public List<WoSimpleStationDTO> doSearchStation(WoTrDTO dto) {
        StringBuilder sql = new StringBuilder(" select CAT_STATION_ID as stationId, NAME as stationName, CODE as stationCode, ADDRESS as stationAddress, LATITUDE as latitude, LONGITUDE as longitude " +
                " from CAT_STATION where STATUS>0 ");

        sql.append(" AND ( CODE LIKE :keysearch or NAME LIKE :keysearch or ADDRESS LIKE :keysearch or LATITUDE LIKE :keysearch or LONGITUDE LIKE :keysearch ) ");

        sql.append(" fetch next 20 rows only ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());

        query.setParameter("keysearch", "%" + dto.getKeySearch() + "%");

        query.addScalar("stationId", new LongType());
        query.addScalar("stationName", new StringType());
        query.addScalar("stationCode", new StringType());
        query.addScalar("stationAddress", new StringType());
        query.addScalar("latitude", new StringType());
        query.addScalar("longitude", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(WoSimpleStationDTO.class));

        return query.list();
    }

    public WoSimpleConstructionDTO getConstructionByCode(String code) {
        String sql = new String(" select cons.CODE as constructionCode, cons.NAME as constructionName, cons.CONSTRUCTION_ID as constructionId, " +
                " cons.CAT_STATION_ID as stationId, cons.CAT_CONSTRUCTION_TYPE_ID as catConstructionTypeId, cons.status as status " +
                " from CONSTRUCTION cons where cons.STATUS>0 " +
                " and cons.CODE like :code");
        SQLQuery query = getSession().createSQLQuery(sql);

        query.setParameter("code", code);

        mapConstructionFields(query);

        return (WoSimpleConstructionDTO) query.uniqueResult();
    }

    public WoSimpleConstructionDTO getConstructionById(long id) {
        String sql = " select cons.CODE as constructionCode, cons.NAME as constructionName, cons.CONSTRUCTION_ID as constructionId, " +
                " cons.CAT_STATION_ID as stationId, cons.CAT_CONSTRUCTION_TYPE_ID as catConstructionTypeId, NVL(AMOUNT,0) as amount, NVL(PRICE,0) as price, status as status " +
                " from CONSTRUCTION cons where cons.STATUS>0 " +
                " and cons.CONSTRUCTION_ID like :id";
        SQLQuery query = getSession().createSQLQuery(sql);

        query.setParameter("id", id);

        query.addScalar("amount", new DoubleType());
        query.addScalar("price", new DoubleType());
        mapConstructionFields(query);

        return (WoSimpleConstructionDTO) query.uniqueResult();
    }

    public WoSimpleSysGroupDTO getCdLevel2FromStation(String stationCode) {
        String sql = new String(" select sg.SYS_GROUP_ID as sysGroupId, sg.NAME as groupName  " +
                " from SYS_GROUP sg " +
                " left join CAT_STATION cs on sg.PROVINCE_ID = cs.CAT_PROVINCE_ID " +
                " where cs.CODE like :stationCode and (sg.CODE like '%P.HT%' or sg.CODE like '%P.XD%') ");
        SQLQuery query = getSession().createSQLQuery(sql);

        query.setParameter("stationCode", stationCode);

        query.addScalar("groupName", new StringType());
        query.addScalar("sysGroupId", new LongType());
        query.setResultTransformer(Transformers.aliasToBean(WoSimpleSysGroupDTO.class));

        return (WoSimpleSysGroupDTO) query.uniqueResult();
    }

    public List<AIOTrContractDetailDTO> getAIOPackagesByContract(WoTrDTO dto) {
        long contractId = dto.getContractId();
        String sql = new String("         SELECT DISTINCT\n" +
                "            pd.aio_package_detail_id aioPackageDetailId,\n" +
                "            p.name,\n" +
                "            pd.is_province_bought isProvinceBought,\n" +
                "            (CASE  when pd.is_province_bought = 1 then 'Tỉnh tự mua hàng'\n" +
                "            when pd.is_province_bought = 2 then 'Cty mua hàng' \n" +
                "            end) isProvinceBoughtName,\n" +
                "            (CASE  when pd.is_province_bought = 1 then dp.per_department_assignment\n" +
                "            when pd.is_province_bought = 2 then dp.department_assignment \n" +
                "            end) assignmentName\n" +
                "        FROM\n" +
                "            aio_contract ac,\n" +
                "            aio_contract_detail ad,\n" +
                "            aio_package p\n" +
                "            LEFT JOIN aio_package_detail pd ON p.aio_package_id = pd.aio_package_id\n" +
                "            LEFT JOIN aio_package_detail_price dp ON pd.aio_package_detail_id = dp.package_detail_id\n" +
                "        WHERE\n" +
                "            ac.contract_id = ad.contract_id\n" +
                "            AND pd.aio_package_detail_id = ad.package_detail_id\n" +
                "            AND ac.contract_id = :contractId");
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        query.setParameter("contractId", contractId);
        query.addScalar("aioPackageDetailId", new LongType());
        query.addScalar("name", new StringType());
        query.addScalar("isProvinceBought", new LongType());
        query.addScalar("assignmentName", new StringType());
        query.addScalar("isProvinceBoughtName", new StringType());

        query.setResultTransformer(Transformers.aliasToBean(AIOTrContractDetailDTO.class));

        if (dto.getPage() != null && dto.getPageSize() != null) {
            query.setFirstResult((dto.getPage().intValue() - 1) * dto.getPageSize().intValue());
            query.setMaxResults(dto.getPageSize().intValue());
        }
        queryCount.setParameter("contractId", contractId);
        dto.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

        return query.list();
    }

    public boolean checkDeletable(long trId) {

        String sql = new String(" select count(*) as total from WO where TR_ID = :trId and status > 0");
        SQLQuery query = getSession().createSQLQuery(sql);

        query.setParameter("trId", trId);
        query.addScalar("total", new LongType());

        long total = (long) query.uniqueResult();

        if (total > 0) return false;

        return true;
    }

    public boolean checkAIOTrHasWO(long trId) {
        String sql = new String(" select count(*) as total from WO wo join WO_TR tr on wo.TR_ID = tr.ID " +
                " where TR_ID = :trId and tr.CUSTOMER_TYPE = 1 and wo.TR_ID = :trId and wo.STATUS>0");

        SQLQuery query = getSession().createSQLQuery(sql);

        query.setParameter("trId", trId);
        query.addScalar("total", new LongType());

        long total = (long) query.uniqueResult();

        if (total > 0) return true;

        return false;
    }

    public WoSimpleSysUserDTO getSysUser(String loginName) {
        String sql = " select SYS_USER_ID as sysUserId, LOGIN_NAME as loginName, FULL_NAME as fullName, EMPLOYEE_CODE as employeeCode, EMAIL as email, SYS_GROUP_ID as sysGroupId ";
        sql += " from SYS_USER where LOGIN_NAME like :loginName AND STATUS = 1 ";
        SQLQuery query = getSession().createSQLQuery(sql);

        query.setParameter("loginName", loginName);

        query.addScalar("loginName", new StringType());
        query.addScalar("sysUserId", new LongType());
        query.addScalar("sysGroupId", new LongType());
        query.addScalar("fullName", new StringType());
        query.addScalar("employeeCode", new StringType());
        query.addScalar("email", new StringType());

        query.setResultTransformer(Transformers.aliasToBean(WoSimpleSysUserDTO.class));

        return (WoSimpleSysUserDTO) query.uniqueResult();
    }

    public WoSimpleSysUserDTO getSysUser(Long sysUserId) {
        String sql = " select SYS_USER_ID as sysUserId, LOGIN_NAME as loginName, FULL_NAME as fullName, EMPLOYEE_CODE as employeeCode, EMAIL as email, SYS_GROUP_ID as sysGroupId ";
        sql += " from SYS_USER where SYS_USER_ID = :sysUserId AND STATUS = 1 ";
        SQLQuery query = getSession().createSQLQuery(sql);

        query.setParameter("sysUserId", sysUserId);

        query.addScalar("loginName", new StringType());
        query.addScalar("sysUserId", new LongType());
        query.addScalar("sysGroupId", new LongType());
        query.addScalar("fullName", new StringType());
        query.addScalar("employeeCode", new StringType());
        query.addScalar("email", new StringType());

        query.setResultTransformer(Transformers.aliasToBean(WoSimpleSysUserDTO.class));

        return (WoSimpleSysUserDTO) query.uniqueResult();
    }

    public boolean checkConstructionInProject(String projectCode, String constructionCode) {
        String sql = " select count(*) as total " +
                " from CONSTRUCTION_PROJECT cp " +
                " left join PROJECT_ESTIMATES pe on pe.PROJECT_CODE = cp.PROJECT_CODE " +
                " left join CONSTRUCTION cons on cons.CODE = pe.CONSTRUCTION_CODE where cons.STATUS != 0 and cons.STATUS != 5 " +
                " and cp.PROJECT_CODE = :projectCode and cons.CODE = :constructionCode ";

        SQLQuery query = getSession().createSQLQuery(sql);

        query.setParameter("projectCode", projectCode);
        query.setParameter("constructionCode", constructionCode);

        query.addScalar("total", new IntegerType());
        int total = (Integer) query.uniqueResult();

        if (total > 0) return true;
        return false;
    }

    public boolean checkConstructionInContract(String contractCode, String constructionCode) {
        String sql = " select count(*) as total " +
                " from CNT_CONTRACT ct " +
                " left join CNT_CONSTR_WORK_ITEM_TASK cc on cc.CNT_CONTRACT_ID = ct.CNT_CONTRACT_ID " +
                " left join CONSTRUCTION cons on cons.CONSTRUCTION_ID = cc.CONSTRUCTION_ID where cons.STATUS != 0 and cons.STATUS != 5 " +
                " and ct.CODE = :contractCode and cons.CODE = :constructionCode ";

        SQLQuery query = getSession().createSQLQuery(sql);

        query.setParameter("contractCode", contractCode);
        query.setParameter("constructionCode", constructionCode);

        query.addScalar("total", new IntegerType());
        int total = (Integer) query.uniqueResult();

        if (total > 0) return true;
        return false;
    }

    public WoSimpleSysGroupDTO getSysGroupById(Long groupId) {
        StringBuilder sql = new StringBuilder("SELECT sg.SYS_GROUP_ID as sysGroupId, sg.CODE as code, sg.NAME as groupName from SYS_GROUP sg " +
                " WHERE SYS_GROUP_ID = :groupId");
        SQLQuery query = getSession().createSQLQuery(sql.toString());

        query.setParameter("groupId", groupId);

        query.addScalar("sysGroupId", new LongType());
        query.addScalar("code", new StringType());
        query.addScalar("groupName", new StringType());

        query.setResultTransformer(Transformers.aliasToBean(WoSimpleSysGroupDTO.class));

        return (WoSimpleSysGroupDTO) query.uniqueResult();
    }

    public WoSimpleStationDTO tryGetStation(String constructionCode) {
        StringBuilder sql = new StringBuilder("select s.CODE as stationCode, s.LATITUDE as latitude, s.LONGITUDE as longitude ,c.CONSTRUCTION_ID as constructionId from construction c join cat_station s on c.cat_station_id = s.cat_station_id " +
                " where c.CODE = :constructionCode");
        SQLQuery query = getSession().createSQLQuery(sql.toString());

        query.setParameter("constructionCode", constructionCode);
        query.addScalar("stationCode", new StringType());
        query.addScalar("latitude", new StringType());
        query.addScalar("longitude", new StringType());
        query.addScalar("constructionId", new LongType());
        query.setResultTransformer(Transformers.aliasToBean(WoSimpleStationDTO.class));

        return (WoSimpleStationDTO) query.uniqueResult();
    }

    public Long getProject(String code) {
        String sql = new String(" select PROJECT_ID as projectId " +
                " from CONSTRUCTION_PROJECT where STATUS>0  AND  PROJECT_CODE =:code ");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("code", code);
        query.addScalar("projectId", new LongType());
        return (Long) query.uniqueResult();
    }

    public List<WoSimpleStationDTO> doSearchStations(WoSimpleStationDTO dto) {
        StringBuilder sql = new StringBuilder("select cat_station_id as stationId, code as stationCode, " +
                " address as stationAddress, latitude as latitude, longitude as longitude from cat_station where status>0 ");

        if (dto.getCodeRange() != null && dto.getCodeRange().size() > 0)
            sql.append(" and code in (:codeRange) and status_complete != 1 ");

        if (StringUtils.isNotEmpty(dto.getKeySearch()))
            sql.append(" and code like :keySearch fetch next 20 rows only ");

        SQLQuery query = getSession().createSQLQuery(sql.toString());

        if (dto.getCodeRange() != null && dto.getCodeRange().size() > 0)
            query.setParameterList("codeRange", dto.getCodeRange());

        if (StringUtils.isNotEmpty(dto.getKeySearch())) query.setParameter("keySearch", "%" + dto.getKeySearch() + "%");

        query.addScalar("stationId", new LongType());
        query.addScalar("stationCode", new StringType());
        query.addScalar("stationAddress", new StringType());
        query.addScalar("latitude", new StringType());
        query.addScalar("longitude", new StringType());

        query.setResultTransformer(Transformers.aliasToBean(WoSimpleStationDTO.class));

        return query.list();
    }

    public List<WoSimpleStationDTO> doSearchStationsIgnoreStatusComplete(WoSimpleStationDTO dto) {
        StringBuilder sql = new StringBuilder("select s.cat_station_id as stationId, s.code as stationCode, \n" +
                " s.address as stationAddress, s.latitude as latitude, s.longitude as longitude, p.code as catProvinceCode from cat_station s \n" +
                " left join cat_province p on s.cat_province_id = p.cat_province_id where s.status>0 ");

        if (dto.getCodeRange() != null && dto.getCodeRange().size() > 0) sql.append(" and s.code in (:codeRange) ");

        if (StringUtils.isNotEmpty(dto.getKeySearch()))
            sql.append(" and s.code like :keySearch fetch next 20 rows only ");

        SQLQuery query = getSession().createSQLQuery(sql.toString());

        if (dto.getCodeRange() != null && dto.getCodeRange().size() > 0)
            query.setParameterList("codeRange", dto.getCodeRange());

        if (StringUtils.isNotEmpty(dto.getKeySearch())) query.setParameter("keySearch", "%" + dto.getKeySearch() + "%");

        query.addScalar("stationId", new LongType());
        query.addScalar("stationCode", new StringType());
        query.addScalar("stationAddress", new StringType());
        query.addScalar("latitude", new StringType());
        query.addScalar("longitude", new StringType());
        query.addScalar("catProvinceCode", new StringType());

        query.setResultTransformer(Transformers.aliasToBean(WoSimpleStationDTO.class));

        return query.list();
    }

    public Long getContractIdByCode(String code) {
        StringBuilder sql = new StringBuilder("select CNT_CONTRACT_ID as contractId from CNT_CONTRACT where status>0  ");

        if (StringUtils.isNotEmpty(code)) sql.append(" and CODE = :code ");

        sql.append(" fetch next 1 row only ");

        SQLQuery query = getSession().createSQLQuery(sql.toString());

        if (StringUtils.isNotEmpty(code)) query.setParameter("code", code);

        query.addScalar("contractId", new LongType());

        return (Long) query.uniqueResult();
    }

    public List<WoSimpleConstructionDTO> getConstructionByCodeRange(List<String> codeRange) {
        String sql = "Select con.construction_id as constructionId, con.code as constructionCode, con.cat_construction_type_id as catConstructionTypeId, " +
                " cs.code as stationCode, con.status as status, cs.latitude as latitude, cs.longitude as longitude " +
                " from construction con " +
                " left join cat_station cs on con.cat_station_id = cs.cat_station_id " +
                " where con.status>0 ";

        if (codeRange != null && codeRange.size() > 0) sql += " and con.code in (:codeRange) ";

        SQLQuery query = getSession().createSQLQuery(sql);

        if (codeRange != null && codeRange.size() > 0) query.setParameterList("codeRange", codeRange);
        query.addScalar("constructionId", new LongType());
        query.addScalar("constructionCode", new StringType());
        query.addScalar("catConstructionTypeId", new LongType());
        query.addScalar("stationCode", new StringType());
        query.addScalar("status", new LongType());
        query.addScalar("latitude", new StringType());
        query.addScalar("longitude", new StringType());

        query.setResultTransformer(Transformers.aliasToBean(WoSimpleConstructionDTO.class));

        return query.list();
    }

    public List<String> getConstructionMappingWithContract(List<String> constructionCodeRange) {

        String sql = " select ct.CODE || '-' || cons.CODE as contractConstruction " +
                " from CNT_CONTRACT ct " +
                " left join CNT_CONSTR_WORK_ITEM_TASK cc on cc.CNT_CONTRACT_ID = ct.CNT_CONTRACT_ID " +
                " left join CONSTRUCTION cons on cons.CONSTRUCTION_ID = cc.CONSTRUCTION_ID where cons.STATUS != 0 ";

        if (constructionCodeRange != null && constructionCodeRange.size() > 0)
            sql += " and cons.CODE in (:constructionCodeRange) ";
        SQLQuery query = getSession().createSQLQuery(sql);

        if (constructionCodeRange != null && constructionCodeRange.size() > 0)
            query.setParameterList("constructionCodeRange", constructionCodeRange);

        query.addScalar("contractConstruction", new StringType());

        return query.list();
    }

    public List<String> getConstructionMappingWithProject(List<String> constructionCodeRange) {
        String sql = " select cp.PROJECT_CODE || '-' || cons.CODE as projectConstruction" +
                " from CONSTRUCTION_PROJECT cp " +
                " left join PROJECT_ESTIMATES pe on pe.PROJECT_CODE = cp.PROJECT_CODE " +
                " left join CONSTRUCTION cons on cons.CODE = pe.CONSTRUCTION_CODE where cons.STATUS != 0 ";

        if (constructionCodeRange != null && constructionCodeRange.size() > 0)
            sql += " and cons.CODE in (:constructionCodeRange) ";

        SQLQuery query = getSession().createSQLQuery(sql);

        if (constructionCodeRange != null && constructionCodeRange.size() > 0)
            query.setParameterList("constructionCodeRange", constructionCodeRange);

        query.addScalar("projectConstruction", new StringType());

        return query.list();
    }

    public WoSimpleStationDTO getStationByCode(String code) {

        String sql = " select CAT_STATION_ID as stationId, NAME as stationName, CODE as stationCode, ADDRESS as stationAddress, LATITUDE as latitude, LONGITUDE as longitude, complete_status as completeStatus " +
                " from CAT_STATION where STATUS>0 and CODE = :code fetch next 1 row only ";
        SQLQuery query = getSession().createSQLQuery(sql);

        query.setParameter("code", code);

        query.addScalar("stationId", new LongType());
        query.addScalar("stationName", new StringType());
        query.addScalar("stationCode", new StringType());
        query.addScalar("stationAddress", new StringType());
        query.addScalar("latitude", new StringType());
        query.addScalar("longitude", new StringType());
        query.addScalar("completeStatus", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(WoSimpleStationDTO.class));

        return (WoSimpleStationDTO) query.uniqueResult();
    }

    public List<String> getConstructionKey(List<String> listCon) {
        String sql = "with  tb as (\n" +
                "select CONTRACT_CODE,PROJECT_CODE,CONSTRUCTION_CODE,TR_TYPE_ID\n" +
                "FROM WO_tr  where 1=1 and status = 1";
        sql += " and CONSTRUCTION_CODE in (:listCon) ";
        sql += ")select  TR_TYPE_ID ||'-'|| PROJECT_CODE ||'-'|| CONSTRUCTION_CODE key from tb \n" +
                "where \n" +
                "PROJECT_CODE is not null\n" +
                "union all\n" +
                "select  TR_TYPE_ID ||'-'|| CONTRACT_CODE ||'-'|| CONSTRUCTION_CODE key  from tb \n" +
                "where \n" +
                "CONTRACT_CODE is not null";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameterList("listCon", listCon);
        query.addScalar("key", new StringType());
        return query.list();
    }

    public WoSimpleSysGroupDTO getSysGroupCreateTR(String username) {
        WoSimpleSysGroupDTO noGroup = new WoSimpleSysGroupDTO();
        noGroup.setSysGroupId(0l);
        StringBuilder sql = new StringBuilder("SELECT (\n" +
                "    CASE\n" +
                "      WHEN SY.GROUP_LEVEL=4\n" +
                "      THEN\n" +
                "        (SELECT SYS_GROUP_ID\n" +
                "        FROM SYS_GROUP A\n" +
                "        WHERE A.SYS_GROUP_ID=\n" +
                "          (SELECT PARENT_ID FROM SYS_GROUP A WHERE A.SYS_GROUP_ID=SY.PARENT_ID\n" +
                "          )\n" +
                "        )\n" +
                "      WHEN SY.GROUP_LEVEL=3\n" +
                "      THEN\n" +
                "        (SELECT SYS_GROUP_ID FROM SYS_GROUP A WHERE A.SYS_GROUP_ID=SY.PARENT_ID\n" +
                "        )\n" +
                "      ELSE SY.SYS_GROUP_ID\n" +
                "    END ) sysGroupId,\n" +
                "     (\n" +
                "    CASE\n" +
                "      WHEN SY.GROUP_LEVEL=4\n" +
                "      THEN\n" +
                "        (SELECT name\n" +
                "        FROM SYS_GROUP A\n" +
                "        WHERE A.SYS_GROUP_ID=\n" +
                "          (SELECT PARENT_ID FROM SYS_GROUP A WHERE A.SYS_GROUP_ID=SY.PARENT_ID\n" +
                "          )\n" +
                "        )\n" +
                "      WHEN SY.GROUP_LEVEL=3\n" +
                "      THEN\n" +
                "        (SELECT name FROM SYS_GROUP A WHERE A.SYS_GROUP_ID=SY.PARENT_ID\n" +
                "        )\n" +
                "      ELSE SY.name\n" +
                "    END ) groupName\n" +
                "  FROM SYS_USER SU\n" +
                "  LEFT JOIN sys_group SY\n" +
                "  ON SU.SYS_GROUP_ID     = SY.SYS_GROUP_ID\n" +
                "  WHERE SU.EMPLOYEE_CODE = :username");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        try {
            query.setParameter("username", username);
            query.addScalar("sysGroupId", new LongType());
            query.addScalar("groupName", new StringType());
            query.setResultTransformer(Transformers.aliasToBean(WoSimpleSysGroupDTO.class));
            return (WoSimpleSysGroupDTO) query.uniqueResult() != null ? (WoSimpleSysGroupDTO) query.uniqueResult() : noGroup;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return noGroup;
    }

    public List<ReportWoTrDTO> doSearchReportWoTr(ReportWoTrDTO obj) {
        StringBuilder sql = new StringBuilder("   SELECT a.name,a.groupCreated,a.tthtApprove ,a.tthtSystem,a.tthtAssignCd,a.tthtRejectCd ,a.tthtTrWo,a.tthtNotTrWo,\n" +
                "                 a.cnktApprove,a.cnktSystemWo,a.woAssignCd, a.woAcceptCd,a.woAcceptFt, a.woAcceptFtSystem,\n" +
                "                 a.woAcceptFtWo,a.woAssignFt, a.woFinish,a.woCompleted,a.woNotCompleted,\n" +
                "                 a.woNotFinish,a.woNotFinishDate, a.woFinishDayEx,\n" +
                "                 nvl(round(DECODE((a.tthtApprove+a.tthtSystem+a.tthtAssignCd + a.tthtRejectCd),0,0,100 * a.tthtApprove / (a.tthtApprove+a.tthtSystem+a.tthtAssignCd + a.tthtRejectCd)),2),0) tthtApprovePrecent,\n" +
                "                 nvl(round(DECODE((a.tthtApprove+a.tthtSystem+a.tthtAssignCd + a.tthtRejectCd),0,0,100 * a.tthtSystem / (a.tthtApprove+a.tthtSystem+a.tthtAssignCd + a.tthtRejectCd)),2),0) tthtSystemPrecent,\n" +
                "                 nvl(round(DECODE((a.tthtApprove+a.tthtSystem+a.tthtAssignCd + a.tthtRejectCd),0,0,100 * a.tthtAssignCd / (a.tthtApprove+a.tthtSystem+a.tthtAssignCd + a.tthtRejectCd)),2),0) tthtAssignCdPrecent,\n" +
                "                 nvl(round(DECODE((a.tthtApprove+a.tthtSystem+a.tthtAssignCd + a.tthtRejectCd),0,0,100 * a.tthtRejectCd / (a.tthtApprove+a.tthtSystem+a.tthtAssignCd + a.tthtRejectCd)),2),0) tthtRejectCdPrecent,\n" +
                "                 nvl(round(DECODE((a.tthtApprove+a.tthtSystem),0,0,100 * a.tthtTrWo / (a.tthtApprove+a.tthtSystem)),2),0) tthtTrWoPrecent,\n" +
                "                 nvl(round(DECODE((a.tthtApprove+a.tthtSystem),0,0,100 * a.tthtNotTrWo / (a.tthtApprove+a.tthtSystem)),2),0) tthtNotTrWoPrecent,\n" +
                "                 nvl(round(DECODE((a.cnktApprove+a.cnktSystemWo+a.woAssignCd),0,0,100 * a.cnktApprove / (a.cnktApprove+a.cnktSystemWo+a.woAssignCd)),2),0) cnktApprovePrecent,\n" +
                "                 nvl(round(DECODE((a.cnktApprove+a.cnktSystemWo+a.woAssignCd),0,0,100 * a.cnktSystemWo / (a.cnktApprove+a.cnktSystemWo+a.woAssignCd)),2),0) cnktSystemWoPrecent,\n" +
                "                 nvl(round(DECODE((a.cnktApprove+a.cnktSystemWo+a.woAssignCd),0,0,100 * a.woAssignCd / (a.cnktApprove+a.cnktSystemWo+a.woAssignCd)),2),0) woAssignCdPrecent,\n" +
                "                 nvl(round(DECODE((a.woAcceptFt+a.woAcceptFtSystem +a.woAcceptFtWo +a.woAssignFt),0,0,100 * a.woAcceptFt / (a.woAcceptFt+a.woAcceptFtSystem +a.woAcceptFtWo +a.woAssignFt)),2),0) woAcceptFtPrecent,\n" +
                "                 nvl(round(DECODE((a.woAcceptFt+a.woAcceptFtSystem +a.woAcceptFtWo +a.woAssignFt),0,0,100 * a.woAcceptFtSystem / (a.woAcceptFt+a.woAcceptFtSystem +a.woAcceptFtWo +a.woAssignFt)),2),0) woAcceptFtSystemPrecent,\n" +
                "                 nvl(round(DECODE((a.woAcceptFt+a.woAcceptFtSystem +a.woAcceptFtWo +a.woAssignFt),0,0,100 * a.woAcceptFtWo / (a.woAcceptFt+a.woAcceptFtSystem +a.woAcceptFtWo +a.woAssignFt)),2),0) woAcceptFtWoPrecent,\n" +
                "                 nvl(round(DECODE((a.woAcceptFt+a.woAcceptFtSystem +a.woAcceptFtWo +a.woAssignFt),0,0,100 * a.woAssignFt / (a.woAcceptFt+a.woAcceptFtSystem +a.woAcceptFtWo +a.woAssignFt)),2),0) woAssignFtPrecent,\n" +
                "                 nvl(round(DECODE((a.cnktApprove + a.cnktSystemWo),0,0,100 * a.woFinish / (a.cnktApprove + a.cnktSystemWo)),2),0) woFinishPrecent,\n" +
                "                 nvl(round(DECODE(a.woFinish,0,0,100 * a.woCompleted / a.woFinish),2),0) woCompletedPrecent,\n" +
                "                 nvl(round(DECODE(a.woFinish,0,0,100 * a.woNotCompleted / a.woFinish),2),0) woNotCompletedPrecent,\n" +
                "                 nvl(round(DECODE((a.cnktApprove + a.cnktSystemWo),0,0,100 * a.woNotFinish /(a.cnktApprove + a.cnktSystemWo)),2),0) woNotFinishPrecent,\n" +
                "                 nvl(round(DECODE(a.woNotFinish,0,0,100 * a.woNotFinishDate / a.woNotFinish),2),0) woNotFinishDatePrecent,\n" +
                "                 nvl(round(DECODE(a.woNotFinish,0,0,100 * a.woFinishDayEx / a.woNotFinish),2),0 )woFinishDayExPrecent from ( SELECT \n" +
                " GROUP_CREATED_NAME name,GROUP_CREATED groupCreated,\n" +
                "  sum(tthtApprove) tthtApprove,\n" +
                "  sum(tthtSystem) tthtSystem,\n" +
                "  sum(tthtAssignCd) tthtAssignCd,\n" +
                "  sum(tthtRejectCd) tthtRejectCd,\n" +
                "  sum(tthtTrWo) tthtTrWo,\n" +
                "  sum(tthtNotTrWo) tthtNotTrWo,\n" +
                "  sum(cnktApprove) cnktApprove,\n" +
                "  sum(cnktSystemWo) cnktSystemWo,\n" +
                "  sum(woAssignCd) woAssignCd,\n" +
                "  sum(woAcceptCd) woAcceptCd,\n" +
                "  sum(woAcceptFt) woAcceptFt,\n" +
                "  sum(woAcceptFtSystem) woAcceptFtSystem,\n" +
                "  sum(woAcceptFtWo) woAcceptFtWo,\n" +
                "  sum(woAssignFt) woAssignFt,\n" +
                "  sum(woFinish) woFinish,\n" +
                "  sum(woCompleted) woCompleted,\n" +
                "  sum(woNotCompleted) woNotCompleted,\n" +
                "  sum(woNotFinish)  woNotFinish,\n" +
                "  sum(woNotFinishDate) woNotFinishDate,\n" +
                "  sum(woFinishDayEx) woFinishDayEx\n" +
                "  from(\n" +
                "SELECT \n" +
                "   tr.GROUP_CREATED_NAME,tr.GROUP_CREATED ,\n" +
                "  COUNT(DISTINCT CASE WHEN   to_char(nvl(tr.USER_RECEIVE_TR,0)) != '-1' and tr.state not in('ASSIGN_CD','REJECT_CD') THEN tr.tr_code END) AS tthtApprove,\n" +
                "  COUNT( DISTINCT CASE WHEN  tr.USER_RECEIVE_TR = '-1' and tr.state not in('ASSIGN_CD','REJECT_CD')  THEN tr.tr_code END) AS tthtSystem,\n" +
                "  0 AS tthtAssignCd,\n" +
                "  0 AS tthtRejectCd,\n" +
                "  COUNT( DISTINCT CASE WHEN  tr.id is not null THEN tr.tr_code END) AS tthtTrWo,\n" +
                "  0 AS tthtNotTrWo,\n" +
                "  COUNT(CASE WHEN   w.state  not in ('ASSIGN_CD') \n" +
                "  and ( to_char(nvl(w.USER_CD_LEVEL2_RECEIVE_WO,0)) !='-1' and to_char(nvl(w.USER_CD_LEVEL3_RECEIVE_WO,0)) !='-1' and to_char(nvl(w.USER_CD_LEVEL4_RECEIVE_WO,0)) !='-1') THEN w.wo_code END) AS cnktApprove,\n" +
                "  COUNT (CASE WHEN w.state  not in ('ASSIGN_CD') \n" +
                "   and ( to_char(nvl(w.USER_CD_LEVEL2_RECEIVE_WO,0)) ='-1' or to_char(nvl(w.USER_CD_LEVEL3_RECEIVE_WO,0)) ='-1' or to_char(nvl(w.USER_CD_LEVEL4_RECEIVE_WO,0)) ='-1') THEN w.wo_code END) AS cnktSystemWo,\n" +
                "  COUNT(CASE WHEN  w.state='ASSIGN_CD'  THEN w.wo_code END) AS woAssignCd,\n" +
                "  COUNT(CASE WHEN  w.state='ACCEPT_CD'  THEN w.wo_code END) AS woAcceptCd,\n" +
                "  COUNT(CASE WHEN w.state  in ('ACCEPT_FT','PROCESSING','DONE','OK','NG','OPINION_RQ_1','OPINION_RQ_2','OPINION_RQ_3','OPINION_RQ_4','CD_OK','CD_NG','WAIT_TC_TCT','WAIT_TC_BRANCH')  and (nvl(w.USER_FT_RECEIVE_WO,0) !='-1') THEN w.wo_code END) AS woAcceptFt,\n" +
                "  COUNT(CASE WHEN w.state  in ('ACCEPT_FT','PROCESSING','DONE','OK','NG','OPINION_RQ_1','OPINION_RQ_2','OPINION_RQ_3','OPINION_RQ_4','CD_OK','CD_NG','WAIT_TC_TCT','WAIT_TC_BRANCH')  and (nvl(w.USER_FT_RECEIVE_WO,0) ='-1') THEN w.wo_code END) AS woAcceptFtSystem,\n" +
                "  COUNT(CASE WHEN  w.state ='ACCEPT_CD' THEN w.wo_code END) AS woAcceptFtWo,\n" +
                "  COUNT(CASE WHEN  w.state = 'ASSIGN_FT' THEN w.wo_code END) AS woAssignFt,\n" +
                "  COUNT(CASE WHEN w.state in ('CD_OK','DONE','OK','WAIT_TC_TCT','WAIT_TC_BRANCH') THEN w.wo_code END) AS woFinish,\n" +
                "  COUNT(CASE WHEN w.state in ('CD_OK','DONE','OK','WAIT_TC_TCT','WAIT_TC_BRANCH') and trunc(w.end_time) <= trunc(w.FINISH_DATE) THEN w.wo_code END) AS woCompleted,\n" +
                "  COUNT(CASE WHEN w.state in ('CD_OK','DONE','OK','WAIT_TC_TCT','WAIT_TC_BRANCH') and trunc(w.end_time) > trunc(w.FINISH_DATE)  THEN w.wo_code END) AS woNotCompleted,\n" +
                "  COUNT(CASE WHEN w.state not in ('CD_OK','DONE','OK','ASSIGN_CD','REJECT_CD','WAIT_TC_TCT','WAIT_TC_BRANCH') THEN w.wo_code END)  AS woNotFinish ,\n" +
                "  COUNT(CASE WHEN w.state not in ('CD_OK','DONE','OK','ASSIGN_CD','REJECT_CD','WAIT_TC_TCT','WAIT_TC_BRANCH')and trunc(sysdate) <= trunc(w.FINISH_DATE) THEN w.wo_code END)  AS woNotFinishDate,\n" +
                "  COUNT(CASE WHEN w.state not in ('CD_OK','DONE','OK','ASSIGN_CD','REJECT_CD','WAIT_TC_TCT','WAIT_TC_BRANCH')and trunc(sysdate) > trunc(w.FINISH_DATE) THEN w.wo_code END)  AS woFinishDayEx\n" +
                "  FROM \n" +
                "   WO_TYPE wt, WO_TR tr , WO w\n" +
                "   where  w.status  =1\n" +
                "  AND tr.status   = 1 AND tr.CD_LEVEL_1 = 242656  AND tr.CD_LEVEL_2 is  null  AND wt.wo_type_code NOT IN('XLSC','BDDK','HCQT','DOANHTHU','5S')\n" +
                "  AND tr.id       = w.TR_ID  AND w.WO_TYPE_ID= wt.ID   \n" +
                "  AND tr.CD_LEVEL_1 is not null");
        if (StringUtils.isNotEmpty(obj.getFromDate())) {
            sql.append(" AND tr.CREATED_DATE >= TO_DATE(:fromDate, 'dd/mm/yyyy')");
        }
        if (StringUtils.isNotEmpty(obj.getToDate())) {
            sql.append(" AND tr.CREATED_DATE < TO_DATE(:toDate, 'dd/mm/yyyy') + 1 ");
        }
        if (obj.getListWoTypeId() != null && obj.getListWoTypeId().size() > 0) {
            sql.append(" AND w.WO_TYPE_ID in (:listWoTypeId) ");
        }
        sql.append(" GROUP by   tr.GROUP_CREATED_NAME,tr.GROUP_CREATED \n" +
                "   UNION all SELECT \n" +
                "   tr.GROUP_CREATED_NAME,tr.GROUP_CREATED,\n" +
                "  COUNT(CASE WHEN  to_char(nvl(tr.USER_RECEIVE_TR,0)) != '-1' and tr.state not in('ASSIGN_CD','REJECT_CD') THEN tr.tr_code END) AS tthtApprove,\n" +
                "  COUNT(CASE WHEN  tr.USER_RECEIVE_TR = '-1' and tr.state not in('ASSIGN_CD','REJECT_CD')  THEN tr.tr_code END) AS tthtSystem,\n" +
                "  COUNT(CASE WHEN  tr.state='ASSIGN_CD'  THEN tr.tr_code END) AS tthtAssignCd,\n" +
                "  COUNT(CASE WHEN  tr.state='REJECT_CD'  THEN tr.tr_code END) AS tthtRejectCd,\n" +
                "  0 AS tthtTrWo,\n" +
                "   COUNT(CASE WHEN  tr.state='ACCEPT_CD'  THEN tr.tr_code END) AS tthtNotTrWo,\n" +
                "  0 AS cnktApprove,\n" +
                "  0 cnktWo,\n" +
                "  0 AS woAssignCd,\n" +
                "  0 AS woAcceptCd,\n" +
                "  0 AS woAcceptFt,\n" +
                "  0 AS woAcceptFtSystem,\n" +
                "  0 AS woAcceptFtWo,\n" +
                "  0 AS woAssignFt,\n" +
                "  0 AS woFinish,\n" +
                "  0 AS woCompleted,\n" +
                "  0 AS woNotCompleted,\n" +
                "  0  AS woNotFinish ,\n" +
                "  0 AS woNotFinishDate,\n" +
                "  0  AS woFinishDayEx\n" +
                "  FROM \n" +
                "    WO_TR tr \n" +
                "   where \n" +
                "   tr.status   = 1 AND tr.CD_LEVEL_2 is  null\n" +
                "   AND tr.CD_LEVEL_1 is not null  AND tr.CD_LEVEL_1 = 242656    AND not exists(\n" +
                "   select TR_ID from WO w where tr.id=w.TR_ID and w.TR_ID is not null and w.CD_LEVEL_1 is not null and w.status=1 and w.TR_ID is not null \n" +
                "  )");
        if (StringUtils.isNotEmpty(obj.getFromDate())) {
            sql.append(" AND tr.CREATED_DATE >= TO_DATE(:fromDate, 'dd/mm/yyyy')");
        }
        if (StringUtils.isNotEmpty(obj.getToDate())) {
            sql.append(" AND tr.CREATED_DATE < TO_DATE(:toDate, 'dd/mm/yyyy') + 1 ");
        }
        sql.append("  GROUP by   tr.GROUP_CREATED_NAME,tr.GROUP_CREATED )\n" +
                "  GROUP  by  GROUP_CREATED,GROUP_CREATED_NAME ) a ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        if (StringUtils.isNotEmpty(obj.getFromDate())) {
            query.setParameter("fromDate", obj.getFromDate());
        }
        if (StringUtils.isNotEmpty(obj.getToDate())) {
            query.setParameter("toDate", obj.getToDate());
        }
        if (obj.getListWoTypeId() != null && obj.getListWoTypeId().size() > 0) {
            query.setParameterList("listWoTypeId", obj.getListWoTypeId());
        }
        if (StringUtils.isNotEmpty(obj.getFromDate())) {
            query.setParameter("fromDate", obj.getFromDate());
        }
        if (StringUtils.isNotEmpty(obj.getToDate())) {
            query.setParameter("toDate", obj.getToDate());
        }
        query.addScalar("name", new StringType());
        query.addScalar("groupCreated", new LongType());
        query.addScalar("tthtApprove", new LongType());
        query.addScalar("tthtSystem", new LongType());
        query.addScalar("tthtAssignCd", new LongType());
        query.addScalar("tthtRejectCd", new LongType());
        query.addScalar("tthtTrWo", new LongType());
        query.addScalar("tthtNotTrWo", new LongType());
        query.addScalar("cnktApprove", new LongType());
        query.addScalar("cnktSystemWo", new LongType());
        query.addScalar("woAcceptCd", new LongType());
        query.addScalar("woAssignCd", new LongType());
        query.addScalar("woAcceptFt", new LongType());
        query.addScalar("woAcceptFtSystem", new LongType());
        query.addScalar("woAcceptFtWo", new LongType());
        query.addScalar("woAssignFt", new LongType());
        query.addScalar("woFinish", new LongType());
        query.addScalar("woCompleted", new LongType());
        query.addScalar("woNotCompleted", new LongType());
        query.addScalar("woNotFinish", new LongType());
        query.addScalar("woNotFinishDate", new LongType());
        query.addScalar("woFinishDayEx", new LongType());

        query.addScalar("tthtApprovePrecent", new DoubleType());
        query.addScalar("tthtSystemPrecent", new DoubleType());
        query.addScalar("tthtAssignCdPrecent", new DoubleType());
        query.addScalar("tthtRejectCdPrecent", new DoubleType());
        query.addScalar("tthtTrWoPrecent", new DoubleType());
        query.addScalar("tthtNotTrWoPrecent", new DoubleType());
        query.addScalar("cnktApprovePrecent", new DoubleType());
        query.addScalar("cnktSystemWoPrecent", new DoubleType());
        query.addScalar("woAssignCdPrecent", new DoubleType());
        query.addScalar("woAcceptFtPrecent", new DoubleType());
        query.addScalar("woAcceptFtSystemPrecent", new DoubleType());
        query.addScalar("woAcceptFtWoPrecent", new DoubleType());
        query.addScalar("woAssignFtPrecent", new DoubleType());
        query.addScalar("woFinishPrecent", new DoubleType());
        query.addScalar("woCompletedPrecent", new DoubleType());
        query.addScalar("woNotCompletedPrecent", new DoubleType());
        query.addScalar("woNotFinishPrecent", new DoubleType());
        query.addScalar("woNotFinishDatePrecent", new DoubleType());
        query.addScalar("woFinishDayExPrecent", new DoubleType());
        query.setResultTransformer(Transformers.aliasToBean(ReportWoTrDTO.class));
        List<ReportWoTrDTO> lst = query.list();
        return lst;
    }

    public List<ReportWoTrDTO> doSearchReportWoPro(ReportWoTrDTO obj) {
        StringBuilder sql = new StringBuilder("    SELECT a.name,a.groupCreated, a.cnktApprove, a.cnktSystemWo, a.woAssignCd,a.woAcceptCd,\n" +
                "                 a.woAcceptFt,a.woAcceptFtSystem, a.woAcceptFtWo, a.woAssignFt,\n" +
                "                 a.woFinish,a.woCompleted, a.woNotCompleted,a.woNotFinish, a.woNotFinishDate, a.woFinishDayEx,\n" +
                "                 nvl(round(DECODE((a.cnktApprove+a.cnktSystemWo+a.woAssignCd),0,0,100 * a.cnktApprove / (a.cnktApprove+a.cnktSystemWo+a.woAssignCd)),2),0) cnktApprovePrecent,\n" +
                "                 nvl(round(DECODE((a.cnktApprove+a.cnktSystemWo+a.woAssignCd),0,0,100 * a.cnktSystemWo / (a.cnktApprove+a.cnktSystemWo+a.woAssignCd)),2),0) cnktSystemWoPrecent,\n" +
                "                 nvl(round(DECODE((a.cnktApprove+a.cnktSystemWo+a.woAssignCd),0,0,100 * a.woAssignCd / (a.cnktApprove+a.cnktSystemWo+a.woAssignCd)),2),0) woAssignCdPrecent,\n" +
                "                 nvl(round(DECODE((a.woAcceptFt+a.woAcceptFtSystem +a.woAcceptFtWo +a.woAssignFt),0,0,100 * a.woAcceptFt / (a.woAcceptFt+a.woAcceptFtSystem +a.woAcceptFtWo +a.woAssignFt)),2),0) woAcceptFtPrecent,\n" +
                "                 nvl(round(DECODE((a.woAcceptFt+a.woAcceptFtSystem +a.woAcceptFtWo +a.woAssignFt),0,0,100 * a.woAcceptFtSystem / (a.woAcceptFt+a.woAcceptFtSystem +a.woAcceptFtWo +a.woAssignFt)),2),0) woAcceptFtSystemPrecent,\n" +
                "                 nvl(round(DECODE((a.woAcceptFt+a.woAcceptFtSystem +a.woAcceptFtWo +a.woAssignFt),0,0,100 * a.woAcceptFtWo / (a.woAcceptFt+a.woAcceptFtSystem +a.woAcceptFtWo +a.woAssignFt)),2),0) woAcceptFtWoPrecent,\n" +
                "                 nvl(round(DECODE((a.woAcceptFt+a.woAcceptFtSystem +a.woAcceptFtWo +a.woAssignFt),0,0,100 * a.woAssignFt / (a.woAcceptFt+a.woAcceptFtSystem +a.woAcceptFtWo +a.woAssignFt)),2),0) woAssignFtPrecent,\n" +
                "                 nvl(round(DECODE((a.cnktApprove + a.cnktSystemWo),0,0,100 * a.woFinish /(a.cnktApprove + a.cnktSystemWo)),2),0) woFinishPrecent,\n" +
                "                 nvl(round(DECODE(a.woFinish,0,0,100 * a.woCompleted / a.woFinish),2),0) woCompletedPrecent,\n" +
                "                 nvl(round(DECODE(a.woFinish,0,0,100 * a.woNotCompleted / a.woFinish),2),0) woNotCompletedPrecent,\n" +
                "                 nvl(round(DECODE((a.cnktApprove + a.cnktSystemWo),0,0,100 * a.woNotFinish / (a.cnktApprove + a.cnktSystemWo)),2),0) woNotFinishPrecent,\n" +
                "                 nvl(round(DECODE(a.woNotFinish,0,0,100 * a.woNotFinishDate / a.woNotFinish),2),0) woNotFinishDatePrecent,\n" +
                "                 nvl(round(DECODE(a.woNotFinish,0,0,100 * a.woFinishDayEx / a.woNotFinish),2),0 )woFinishDayExPrecent  from(  SELECT      sg.PARENT_ID groupCreated,sg.province_code name,\n" +
                "        sum(cnktApprove) cnktApprove,\n" +
                "        sum(cnktSystemWo) cnktSystemWo,\n" +
                "        sum(woAssignCd) woAssignCd,\n" +
                "        sum(woAcceptCd) woAcceptCd,\n" +
                "        sum(woAcceptFt) woAcceptFt,\n" +
                "        sum(woAcceptFtSystem) woAcceptFtSystem,\n" +
                "        sum(woAcceptFtWo) woAcceptFtWo,\n" +
                "        sum(woAssignFt) woAssignFt,\n" +
                "        sum(woFinish) woFinish,\n" +
                "        sum(woCompleted) woCompleted,\n" +
                "        sum(woNotCompleted) woNotCompleted,\n" +
                "        sum(woNotFinish) woNotFinish,\n" +
                "        sum(woNotFinishDate) woNotFinishDate,\n" +
                "        sum(woFinishDayEx)  woFinishDayEx from (\n" +
                " SELECT \n" +
                "   w.CD_LEVEL_2,\n" +
                "  COUNT(CASE WHEN   w.state  not in ('ASSIGN_CD') \n" +
                "  and ( to_char(nvl(w.USER_CD_LEVEL2_RECEIVE_WO,0)) !='-1' and to_char(nvl(w.USER_CD_LEVEL3_RECEIVE_WO,0)) !='-1' and to_char(nvl(w.USER_CD_LEVEL4_RECEIVE_WO,0)) !='-1') THEN w.wo_code END) AS cnktApprove,\n" +
                "  COUNT (CASE WHEN w.state  not in ('ASSIGN_CD') \n" +
                "   and ( to_char(nvl(w.USER_CD_LEVEL2_RECEIVE_WO,0)) ='-1' or to_char(nvl(w.USER_CD_LEVEL3_RECEIVE_WO,0)) ='-1' or to_char(nvl(w.USER_CD_LEVEL4_RECEIVE_WO,0)) ='-1') THEN w.wo_code END) AS cnktSystemWo,\n" +
                "  COUNT(CASE WHEN  w.state='ASSIGN_CD'  THEN w.wo_code END) AS woAssignCd,\n" +
                "  COUNT(CASE WHEN  w.state ='ACCEPT_CD'  THEN w.wo_code END) AS woAcceptCd,\n" +
                "  COUNT(CASE WHEN w.state  in ('ACCEPT_FT','PROCESSING','DONE','OK','NG','OPINION_RQ_1','OPINION_RQ_2','OPINION_RQ_3','OPINION_RQ_4','CD_OK','CD_NG','WAIT_TC_TCT','WAIT_TC_BRANCH')  and (nvl(w.USER_FT_RECEIVE_WO,0) !='-1') THEN w.wo_code END) AS woAcceptFt,\n" +
                "  COUNT(CASE WHEN w.state  in ('ACCEPT_FT','PROCESSING','DONE','OK','NG','OPINION_RQ_1','OPINION_RQ_2','OPINION_RQ_3','OPINION_RQ_4','CD_OK','CD_NG','WAIT_TC_TCT','WAIT_TC_BRANCH')  and (nvl(w.USER_FT_RECEIVE_WO,0) ='-1') THEN w.wo_code END) AS woAcceptFtSystem,\n" +
                "  COUNT(CASE WHEN   w.state = 'ACCEPT_CD' THEN w.wo_code END) AS woAcceptFtWo,\n" +
                "  COUNT(CASE WHEN  w.state in ('ASSIGN_FT') THEN w.wo_code END) AS woAssignFt,\n" +
                "  COUNT(CASE WHEN w.state in ('CD_OK','DONE','OK','WAIT_TC_TCT','WAIT_TC_BRANCH') THEN w.wo_code END) AS woFinish,\n" +
                "  COUNT(CASE WHEN w.state in ('CD_OK','DONE','OK','WAIT_TC_TCT','WAIT_TC_BRANCH') and trunc(w.end_time) <= trunc(w.FINISH_DATE) THEN w.wo_code END) AS woCompleted,\n" +
                "  COUNT(CASE WHEN w.state in ('CD_OK','DONE','OK','WAIT_TC_TCT','WAIT_TC_BRANCH') and trunc(w.end_time) > trunc(w.FINISH_DATE)  THEN w.wo_code END) AS woNotCompleted,\n" +
                "  COUNT(CASE WHEN w.state not in ('CD_OK','DONE','OK','ASSIGN_CD','REJECT_CD','WAIT_TC_TCT','WAIT_TC_BRANCH') THEN w.wo_code END)  AS woNotFinish ,\n" +
                "  COUNT(CASE WHEN w.state not in ('CD_OK','DONE','OK','ASSIGN_CD','REJECT_CD','WAIT_TC_TCT','WAIT_TC_BRANCH')and trunc(sysdate) <= trunc(w.FINISH_DATE) THEN w.wo_code END)  AS woNotFinishDate,\n" +
                "  COUNT(CASE WHEN w.state not in ('CD_OK','DONE','OK','ASSIGN_CD','REJECT_CD','WAIT_TC_TCT','WAIT_TC_BRANCH')and trunc(sysdate) > trunc(w.FINISH_DATE) THEN w.wo_code END)  AS woFinishDayEx\n" +
                "  FROM \n" +
                "    WO_TYPE wt , WO w\n" +
                "   where  w.status  =1  AND w.WO_TYPE_ID= wt.ID\n" +
                "  AND wt.status   = 1 AND wt.wo_type_code NOT IN('XLSC','BDDK','HCQT','DOANHTHU','5S')\n" +
                "  AND wt.id       =w.wo_type_id\n" +
                "  AND w.CD_LEVEL_1 is not null   AND w.CD_LEVEL_5 IS NULL  AND w.CD_LEVEL_1 = '242656'  ");
        if (StringUtils.isNotEmpty(obj.getFromDate())) {
            sql.append(" AND w.CREATED_DATE >= TO_DATE(:fromDate, 'dd/mm/yyyy')");
        }
        if (StringUtils.isNotEmpty(obj.getToDate())) {
            sql.append(" AND w.CREATED_DATE < TO_DATE(:toDate, 'dd/mm/yyyy') + 1 ");
        }
        if (obj.getListWoTypeId() != null && obj.getListWoTypeId().size() > 0) {
            sql.append(" AND w.WO_TYPE_ID in (:listWoTypeId) ");
        }

        if (StringUtils.isNoneBlank(obj.getSysGroupAssign())) {
            if (obj.getSysGroupAssign().equals("1")) {
                sql.append(" AND nvl(w.type,0) not in(1,2) and nvl(w.TR_ID,0) =0  ");
            } else if (obj.getSysGroupAssign().equals("2")) {
                sql.append(" and nvl(w.type,0) = 1  ");
            } else if (obj.getSysGroupAssign().equals("3")) {
                sql.append(" and nvl(w.type,0) = 3 ");
            } else if (obj.getSysGroupAssign().equals("4")) {
                sql.append(" and w.tr_id in(select tr_id from wo_tr where status = 1 and GROUP_CREATED_NAME='Trung tâm Giải pháp tích hợp')  ");
            } else if (obj.getSysGroupAssign().equals("5")) {
                sql.append(" and w.tr_id in(select tr_id from wo_tr where status = 1 and GROUP_CREATED ='166677') ");
            }
        }
        sql.append("   GROUP by  \n" +
                "    w.CD_LEVEL_2)\n" +
                "    a,sys_group sg\n" +
                "    where sg.sys_group_id = a.CD_LEVEL_2 and sg.status=1 ");
        if (obj.getListGroupId() != null && obj.getListGroupId().size() > 0) {
            sql.append(" AND sg.PARENT_ID in (:listGroupId) ");
        }
        sql.append("GROUP BY sg.province_code,sg.PARENT_ID  ORDER BY sg.province_code) a");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        if (StringUtils.isNotEmpty(obj.getFromDate())) {
            query.setParameter("fromDate", obj.getFromDate());
        }
        if (StringUtils.isNotEmpty(obj.getToDate())) {
            query.setParameter("toDate", obj.getToDate());
        }
        if (obj.getListWoTypeId() != null && obj.getListWoTypeId().size() > 0) {
            query.setParameterList("listWoTypeId", obj.getListWoTypeId());
        }
        if (obj.getListGroupId() != null && obj.getListGroupId().size() > 0) {
            query.setParameterList("listGroupId", obj.getListGroupId());
        }
        query.addScalar("groupCreated", new LongType());
        query.addScalar("name", new StringType());
        query.addScalar("cnktApprove", new LongType());
        query.addScalar("cnktSystemWo", new LongType());
        query.addScalar("woAcceptCd", new LongType());
        query.addScalar("woAssignCd", new LongType());
        query.addScalar("woAcceptFt", new LongType());
        query.addScalar("woAcceptFtSystem", new LongType());
        query.addScalar("woAcceptFtWo", new LongType());
        query.addScalar("woAssignFt", new LongType());
        query.addScalar("woFinish", new LongType());
        query.addScalar("woCompleted", new LongType());
        query.addScalar("woNotCompleted", new LongType());
        query.addScalar("woNotFinish", new LongType());
        query.addScalar("woNotFinishDate", new LongType());
        query.addScalar("woFinishDayEx", new LongType());

        query.addScalar("cnktApprovePrecent", new DoubleType());
        query.addScalar("cnktSystemWoPrecent", new DoubleType());
        query.addScalar("woAssignCdPrecent", new DoubleType());
        query.addScalar("woAcceptFtPrecent", new DoubleType());
        query.addScalar("woAcceptFtSystemPrecent", new DoubleType());
        query.addScalar("woAcceptFtWoPrecent", new DoubleType());
        query.addScalar("woAssignFtPrecent", new DoubleType());
        query.addScalar("woFinishPrecent", new DoubleType());
        query.addScalar("woCompletedPrecent", new DoubleType());
        query.addScalar("woNotCompletedPrecent", new DoubleType());
        query.addScalar("woNotFinishPrecent", new DoubleType());
        query.addScalar("woNotFinishDatePrecent", new DoubleType());
        query.addScalar("woFinishDayExPrecent", new DoubleType());
        query.setResultTransformer(Transformers.aliasToBean(ReportWoTrDTO.class));
        List<ReportWoTrDTO> lst = query.list();
        return lst;
    }

    public List<ReportWoTrDTO> doSearchReportWoGroup(ReportWoTrDTO obj) {
        StringBuilder sql = new StringBuilder("    SELECT a.name,a.groupCreated, a.cnktApprove, a.cnktSystemWo, a.woAssignCd,a.woAcceptCd,\n" +
                "                 a.woAcceptFt,a.woAcceptFtSystem, a.woAcceptFtWo, a.woAssignFt,\n" +
                "                 a.woFinish,a.woCompleted, a.woNotCompleted,a.woNotFinish, a.woNotFinishDate, a.woFinishDayEx,\n" +
                "                 nvl(round(DECODE((a.cnktApprove+a.cnktSystemWo+a.woAssignCd),0,0,100 * a.cnktApprove / (a.cnktApprove+a.cnktSystemWo+a.woAssignCd)),2),0) cnktApprovePrecent,\n" +
                "                 nvl(round(DECODE((a.cnktApprove+a.cnktSystemWo+a.woAssignCd),0,0,100 * a.cnktSystemWo / (a.cnktApprove+a.cnktSystemWo+a.woAssignCd)),2),0) cnktSystemWoPrecent,\n" +
                "                 nvl(round(DECODE((a.cnktApprove+a.cnktSystemWo+a.woAssignCd),0,0,100 * a.woAssignCd / (a.cnktApprove+a.cnktSystemWo+a.woAssignCd)),2),0) woAssignCdPrecent,\n" +
                "                 nvl(round(DECODE((a.woAcceptFt+a.woAcceptFtSystem +a.woAcceptFtWo +a.woAssignFt),0,0,100 * a.woAcceptFt / (a.woAcceptFt+a.woAcceptFtSystem +a.woAcceptFtWo +a.woAssignFt)),2),0) woAcceptFtPrecent,\n" +
                "                 nvl(round(DECODE((a.woAcceptFt+a.woAcceptFtSystem +a.woAcceptFtWo +a.woAssignFt),0,0,100 * a.woAcceptFtSystem / (a.woAcceptFt+a.woAcceptFtSystem +a.woAcceptFtWo +a.woAssignFt)),2),0) woAcceptFtSystemPrecent,\n" +
                "                 nvl(round(DECODE((a.woAcceptFt+a.woAcceptFtSystem +a.woAcceptFtWo +a.woAssignFt),0,0,100 * a.woAcceptFtWo / (a.woAcceptFt+a.woAcceptFtSystem +a.woAcceptFtWo +a.woAssignFt)),2),0) woAcceptFtWoPrecent,\n" +
                "                 nvl(round(DECODE((a.woAcceptFt+a.woAcceptFtSystem +a.woAcceptFtWo +a.woAssignFt),0,0,100 * a.woAssignFt / (a.woAcceptFt+a.woAcceptFtSystem +a.woAcceptFtWo +a.woAssignFt)),2),0) woAssignFtPrecent,\n" +
                "                 nvl(round(DECODE((a.cnktApprove + a.cnktSystemWo),0,0,100 * a.woFinish /(a.cnktApprove + a.cnktSystemWo)),2),0) woFinishPrecent,\n" +
                "                 nvl(round(DECODE(a.woFinish,0,0,100 * a.woCompleted / a.woFinish),2),0) woCompletedPrecent,\n" +
                "                 nvl(round(DECODE(a.woFinish,0,0,100 * a.woNotCompleted / a.woFinish),2),0) woNotCompletedPrecent,\n" +
                "                 nvl(round(DECODE((a.cnktApprove + a.cnktSystemWo),0,0,100 * a.woNotFinish /(a.cnktApprove + a.cnktSystemWo)),2),0) woNotFinishPrecent,\n" +
                "                 nvl(round(DECODE(a.woNotFinish,0,0,100 * a.woNotFinishDate / a.woNotFinish),2),0) woNotFinishDatePrecent,\n" +
                "                 nvl(round(DECODE(a.woNotFinish,0,0,100 * a.woFinishDayEx / a.woNotFinish),2),0 )woFinishDayExPrecent  from (  SELECT \n" +
                "                  to_char(tr.GROUP_CREATED_NAME) name,to_char(tr.GROUP_CREATED) as groupCreated,\n" +
                "                  COUNT(CASE WHEN   w.state  not in ('ASSIGN_CD') \n" +
                "                  and ( to_char(nvl(w.USER_CD_LEVEL2_RECEIVE_WO,0)) !='-1' and to_char(nvl(w.USER_CD_LEVEL3_RECEIVE_WO,0)) !='-1' and to_char(nvl(w.USER_CD_LEVEL4_RECEIVE_WO,0)) !='-1') THEN w.wo_code END) AS cnktApprove,\n" +
                "                  COUNT (CASE WHEN w.state  not in ('ASSIGN_CD') \n" +
                "                   and ( to_char(nvl(w.USER_CD_LEVEL2_RECEIVE_WO,0)) ='-1' or to_char(nvl(w.USER_CD_LEVEL3_RECEIVE_WO,0)) ='-1' or to_char(nvl(w.USER_CD_LEVEL4_RECEIVE_WO,0)) ='-1') THEN w.wo_code END) AS cnktSystemWo,\n" +
                "                  COUNT(CASE WHEN  w.state='ASSIGN_CD'  THEN w.wo_code END) AS woAssignCd,\n" +
                "                  COUNT(CASE WHEN  w.state='ACCEPT_CD'  THEN w.wo_code END) AS woAcceptCd,\n" +
                "                  COUNT(CASE WHEN w.state  in ('ACCEPT_FT','PROCESSING','DONE','OK','NG','OPINION_RQ_1','OPINION_RQ_2','OPINION_RQ_3','OPINION_RQ_4','CD_OK','CD_NG','WAIT_TC_TCT','WAIT_TC_BRANCH')  and (nvl(w.USER_FT_RECEIVE_WO,0) !='-1') THEN w.wo_code END) AS woAcceptFt,\n" +
                "                  COUNT(CASE WHEN w.state  in ('ACCEPT_FT','PROCESSING','DONE','OK','NG','OPINION_RQ_1','OPINION_RQ_2','OPINION_RQ_3','OPINION_RQ_4','CD_OK','CD_NG','WAIT_TC_TCT','WAIT_TC_BRANCH')  and (nvl(w.USER_FT_RECEIVE_WO,0) ='-1') THEN w.wo_code END) AS woAcceptFtSystem,\n" +
                "                  COUNT(CASE WHEN  w.state ='ACCEPT_CD' THEN w.wo_code END) AS woAcceptFtWo,\n" +
                "                  COUNT(CASE WHEN  w.state in ('ASSIGN_FT')  THEN w.wo_code END) AS woAssignFt,\n" +
                "                  COUNT(CASE WHEN w.state in ('CD_OK','DONE','OK','WAIT_TC_TCT','WAIT_TC_BRANCH') THEN w.wo_code END) AS woFinish,\n" +
                "                  COUNT(CASE WHEN w.state in ('CD_OK','DONE','OK','WAIT_TC_TCT','WAIT_TC_BRANCH') and trunc(w.end_time) <= trunc(w.FINISH_DATE) THEN w.wo_code END) AS woCompleted,\n" +
                "                  COUNT(CASE WHEN w.state in ('CD_OK','DONE','OK','WAIT_TC_TCT','WAIT_TC_BRANCH') and trunc(w.end_time) > trunc(w.FINISH_DATE)  THEN w.wo_code END) AS woNotCompleted,\n" +
                "                  COUNT(CASE WHEN w.state not in ('CD_OK','DONE','OK','ASSIGN_CD','REJECT_CD','WAIT_TC_TCT','WAIT_TC_BRANCH') THEN w.wo_code END)  AS woNotFinish ,\n" +
                "                  COUNT(CASE WHEN w.state not in ('CD_OK','DONE','OK','ASSIGN_CD','REJECT_CD','WAIT_TC_TCT','WAIT_TC_BRANCH')and trunc(sysdate) <= trunc(w.FINISH_DATE) THEN w.wo_code END)  AS woNotFinishDate,\n" +
                "                  COUNT(CASE WHEN w.state not in ('CD_OK','DONE','OK','ASSIGN_CD','REJECT_CD','WAIT_TC_TCT','WAIT_TC_BRANCH')and trunc(sysdate) > trunc(w.FINISH_DATE) THEN w.wo_code END)  AS woFinishDayEx\n" +
                "                  FROM \n" +
                "                    WO_TYPE wt ,wo_tr tr, WO w\n" +
                "                   where  w.status  =1 \n" +
                "                  AND tr.Id = w.tr_id\n" +
                "                  AND tr.status = 1  AND w.WO_TYPE_ID= wt.ID\n" +
                "                  AND wt.status   = 1 AND wt.wo_type_code NOT IN('XLSC','BDDK','HCQT','DOANHTHU','5S')\n" +
                "                  AND wt.id       =w.wo_type_id  AND w.CD_LEVEL_5 IS NULL  \n" +
                "                  AND w.CD_LEVEL_1 is not null AND nvl(w.type,0) < 1 AND w.CD_LEVEL_1 = '242656' ");
        if (StringUtils.isNotEmpty(obj.getFromDate())) {
            sql.append(" AND w.CREATED_DATE >= TO_DATE(:fromDate, 'dd/mm/yyyy')");
        }
        if (StringUtils.isNotEmpty(obj.getToDate())) {
            sql.append(" AND w.CREATED_DATE < TO_DATE(:toDate, 'dd/mm/yyyy') + 1 ");
        }
        if (obj.getListWoTypeId() != null && obj.getListWoTypeId().size() > 0) {
            sql.append(" AND w.WO_TYPE_ID in (:listWoTypeId) ");
        }
        sql.append(" GROUP by tr.GROUP_CREATED_NAME,tr.GROUP_CREATED");
        sql.append("   UNION ALL SELECT\n" +
                "                                  CASE WHEN  name = '1'  THEN 'WO CNKT tự tạo' else  'WO TT TTHT' END AS name, nvl(name,0) as groupCreated,\n" +
                "                                  cnktApprove,cnktSystemWo,woAssignCd,woAcceptCd,woAcceptFt,woAcceptFtSystem,\n" +
                "                                 woAcceptFtWo,woAssignFt,woFinish,woCompleted,woNotCompleted,woNotFinish,woNotFinishDate,woFinishDayEx\n" +
                "                                 from(\n" +
                "                                 SELECT \n" +
                "                                 w.type name,\n" +
                "                                 COUNT(CASE WHEN   w.state  not in ('ASSIGN_CD') \n" +
                "                                 and ( to_char(nvl(w.USER_CD_LEVEL2_RECEIVE_WO,0)) !='-1' and to_char(nvl(w.USER_CD_LEVEL3_RECEIVE_WO,0)) !='-1' and to_char(nvl(w.USER_CD_LEVEL4_RECEIVE_WO,0)) !='-1') THEN w.wo_code END) AS cnktApprove,\n" +
                "                                 COUNT (CASE WHEN w.state  not in ('ASSIGN_CD') \n" +
                "                                  and ( to_char(nvl(w.USER_CD_LEVEL2_RECEIVE_WO,0)) ='-1' or to_char(nvl(w.USER_CD_LEVEL3_RECEIVE_WO,0)) ='-1' or to_char(nvl(w.USER_CD_LEVEL4_RECEIVE_WO,0)) ='-1') THEN w.wo_code END) AS cnktSystemWo,\n" +
                "                                 COUNT(CASE WHEN  w.state='ASSIGN_CD'  THEN w.wo_code END) AS woAssignCd,\n" +
                "                                 COUNT(CASE WHEN  w.state='ACCEPT_CD'  THEN w.wo_code END) AS woAcceptCd,\n" +
                "                                 COUNT(CASE WHEN w.state  in ('ACCEPT_FT','PROCESSING','DONE','OK','NG','OPINION_RQ_1','OPINION_RQ_2','OPINION_RQ_3','OPINION_RQ_4','CD_OK','CD_NG','WAIT_TC_TCT','WAIT_TC_BRANCH')  and (nvl(w.USER_FT_RECEIVE_WO,0) !='-1') THEN w.wo_code END) AS woAcceptFt,\n" +
                "                                 COUNT(CASE WHEN w.state  in ('ACCEPT_FT','PROCESSING','DONE','OK','NG','OPINION_RQ_1','OPINION_RQ_2','OPINION_RQ_3','OPINION_RQ_4','CD_OK','CD_NG','WAIT_TC_TCT','WAIT_TC_BRANCH')  and (nvl(w.USER_FT_RECEIVE_WO,0) ='-1') THEN w.wo_code END) AS woAcceptFtSystem,\n" +
                "                                 COUNT(CASE WHEN  w.state ='ACCEPT_CD' THEN w.wo_code END) AS woAcceptFtWo,\n" +
                "                                 COUNT(CASE WHEN  w.state = 'ASSIGN_FT'   THEN w.wo_code END) AS woAssignFt,\n" +
                "                                 COUNT(CASE WHEN w.state in ('CD_OK','DONE','OK','WAIT_TC_TCT','WAIT_TC_BRANCH') THEN w.wo_code END) AS woFinish,\n" +
                "                                 COUNT(CASE WHEN w.state in ('CD_OK','DONE','OK','WAIT_TC_TCT','WAIT_TC_BRANCH') and trunc(w.end_time) <= trunc(w.FINISH_DATE) THEN w.wo_code END) AS woCompleted,\n" +
                "                                 COUNT(CASE WHEN w.state in ('CD_OK','DONE','OK','WAIT_TC_TCT','WAIT_TC_BRANCH') and trunc(w.end_time) > trunc(w.FINISH_DATE)  THEN w.wo_code END) AS woNotCompleted,\n" +
                "                                 COUNT(CASE WHEN w.state not in ('CD_OK','DONE','OK','ASSIGN_CD','REJECT_CD','WAIT_TC_TCT','WAIT_TC_BRANCH') THEN w.wo_code END)  AS woNotFinish ,\n" +
                "                                 COUNT(CASE WHEN w.state not in ('CD_OK','DONE','OK','ASSIGN_CD','REJECT_CD','WAIT_TC_TCT','WAIT_TC_BRANCH')and trunc(sysdate) <= trunc(w.FINISH_DATE) THEN w.wo_code END)  AS woNotFinishDate,\n" +
                "                                 COUNT(CASE WHEN w.state not in ('CD_OK','DONE','OK','ASSIGN_CD','REJECT_CD','WAIT_TC_TCT','WAIT_TC_BRANCH')and trunc(sysdate) > trunc(w.FINISH_DATE) THEN w.wo_code END)  AS woFinishDayEx\n" +
                "                                 FROM \n" +
                "                                   WO_TYPE wt , WO w\n" +
                "                                  where  w.status  =1 \n" +
                "                                 AND nvl(W.TR_ID,0)= 0\n" +
                "                                 AND wt.status   = 1 AND wt.wo_type_code NOT IN('XLSC','BDDK','HCQT','DOANHTHU','5S')\n" +
                "                                 AND wt.id       =w.wo_type_id\n" +
                "                                 AND w.CD_LEVEL_1 is not null   AND w.CD_LEVEL_5 IS NULL AND w.CD_LEVEL_1 = '242656' \n" +
                "                                 AND nvl(w.type,0) < 2");

        if (StringUtils.isNotEmpty(obj.getFromDate())) {
            sql.append(" AND w.CREATED_DATE >= TO_DATE(:fromDate, 'dd/mm/yyyy')");
        }
        if (StringUtils.isNotEmpty(obj.getToDate())) {
            sql.append(" AND w.CREATED_DATE < TO_DATE(:toDate, 'dd/mm/yyyy') + 1 ");
        }
        if (obj.getListWoTypeId() != null && obj.getListWoTypeId().size() > 0) {
            sql.append(" AND w.WO_TYPE_ID in (:listWoTypeId) ");
        }
        sql.append("    GROUP by w.type)) a");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        if (StringUtils.isNotEmpty(obj.getFromDate())) {
            query.setParameter("fromDate", obj.getFromDate());
        }
        if (StringUtils.isNotEmpty(obj.getToDate())) {
            query.setParameter("toDate", obj.getToDate());
        }
        if (obj.getListWoTypeId() != null && obj.getListWoTypeId().size() > 0) {
            query.setParameterList("listWoTypeId", obj.getListWoTypeId());
        }
        if (StringUtils.isNotEmpty(obj.getFromDate())) {
            query.setParameter("fromDate", obj.getFromDate());
        }
        if (StringUtils.isNotEmpty(obj.getToDate())) {
            query.setParameter("toDate", obj.getToDate());
        }
        if (obj.getListWoTypeId() != null && obj.getListWoTypeId().size() > 0) {
            query.setParameterList("listWoTypeId", obj.getListWoTypeId());
        }
        query.addScalar("name", new StringType());
        query.addScalar("groupCreated", new LongType());
        query.addScalar("cnktApprove", new LongType());
        query.addScalar("cnktSystemWo", new LongType());
        query.addScalar("woAcceptCd", new LongType());
        query.addScalar("woAssignCd", new LongType());
        query.addScalar("woAcceptFt", new LongType());
        query.addScalar("woAcceptFtSystem", new LongType());
        query.addScalar("woAcceptFtWo", new LongType());
        query.addScalar("woAssignFt", new LongType());
        query.addScalar("woFinish", new LongType());
        query.addScalar("woCompleted", new LongType());
        query.addScalar("woNotCompleted", new LongType());
        query.addScalar("woNotFinish", new LongType());
        query.addScalar("woNotFinishDate", new LongType());
        query.addScalar("woFinishDayEx", new LongType());

        query.addScalar("cnktApprovePrecent", new DoubleType());
        query.addScalar("cnktSystemWoPrecent", new DoubleType());
        query.addScalar("woAssignCdPrecent", new DoubleType());
        query.addScalar("woAcceptFtPrecent", new DoubleType());
        query.addScalar("woAcceptFtSystemPrecent", new DoubleType());
        query.addScalar("woAcceptFtWoPrecent", new DoubleType());
        query.addScalar("woAssignFtPrecent", new DoubleType());
        query.addScalar("woFinishPrecent", new DoubleType());
        query.addScalar("woCompletedPrecent", new DoubleType());
        query.addScalar("woNotCompletedPrecent", new DoubleType());
        query.addScalar("woNotFinishPrecent", new DoubleType());
        query.addScalar("woNotFinishDatePrecent", new DoubleType());
        query.addScalar("woFinishDayExPrecent", new DoubleType());
        query.setResultTransformer(Transformers.aliasToBean(ReportWoTrDTO.class));
        List<ReportWoTrDTO> lst = query.list();
        return lst;
    }

    public List<WoSimpleStationDTO> doSearchStationByContract(WoSimpleStationDTO dto) {
        StringBuilder sql = new StringBuilder(" select s.CAT_STATION_ID stationId, s.CODE stationCode, s.ADDRESS stationAddress, s.LATITUDE latitude, s.LONGITUDE longitude, p.code catProvinceCode, p.area_code as areaCode " +
                " from CNT_CONTRACT ct " +
                " left join CNT_CONSTR_WORK_ITEM_TASK cc on cc.CNT_CONTRACT_ID = ct.CNT_CONTRACT_ID and cc.status != 0 " +
                " left join CONSTRUCTION cons on cons.CONSTRUCTION_ID = cc.CONSTRUCTION_ID and cons.STATUS != 0 " +
                " left join CAT_STATION s on cons.CAT_STATION_ID = s.CAT_STATION_ID and s.STATUS != 0 " +
                " left join cat_province p on s.cat_province_id = p.cat_province_id and p.status != 0 " +
                " where ct.CNT_CONTRACT_ID = :contractId ");
        if (StringUtils.isNotEmpty(dto.getKeySearch())) sql.append(" and lower(s.CODE) like :keySearch ");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("contractId", dto.getContractId());

        if (StringUtils.isNotEmpty(dto.getKeySearch()))
            query.setParameter("keySearch", "%" + dto.getKeySearch().toLowerCase() + "%");

        query.addScalar("stationId", new LongType());
        query.addScalar("stationCode", new StringType());
        query.addScalar("stationAddress", new StringType());
        query.addScalar("latitude", new StringType());
        query.addScalar("longitude", new StringType());
        query.addScalar("catProvinceCode", new StringType());
        query.addScalar("areaCode", new StringType());

        query.setResultTransformer(Transformers.aliasToBean(WoSimpleStationDTO.class));

        return query.list();
    }

    public List<WoDTO> doSearchReportTrWoDetail(ReportWoTrDTO obj) {
        StringBuilder sql = new StringBuilder("SELECT  w.WO_CODE AS woCode,\n" +
                "           wt.WO_TYPE_NAME AS woTypeName,\n" +
                "           w.TR_CODE AS trCode,\n" +
                "           ap.name AS state,\n" +
                "           to_char(w.CREATED_DATE,'dd/MM/yyyy') createdDateStr,\n" +
                "           to_char(w.FINISH_DATE,'dd/MM/yyyy') finishDateStr,\n" +
                "           w.MONEY_VALUE moneyValue,\n" +
                "           w.CD_LEVEL_2_NAME cdLevel2Name,\n" +
                "           w.CD_LEVEL_3_NAME cdLevel3Name,\n" +
                "           w.CD_LEVEL_4_NAME cdLevel4Name,\n" +
                "           w.FT_NAME  ftName ,w.OVERDUE_REASON overdueReason, w.OVERDUE_APPROVE_PERSON overdueApprovePerson \n" +
                "        FROM\n" +
                "            WO_TYPE wt,\n" +
                "            APP_PARAM ap,\n" +
                "            WO_TR tr ,\n" +
                "            WO w    \n" +
                "        where\n" +
                "            w.status  =1 \n" +
                "            AND w.state = ap.CODE \n" +
                "            AND ap.PAR_TYPE = 'WO_XL_STATE'\n" +
                "            AND tr.status   = 1  \n" +
                "            AND tr.CD_LEVEL_2 is  null   \n" +
                "            AND wt.wo_type_code NOT IN(\n" +
                "                'XLSC','BDDK','HCQT','DOANHTHU','5S'\n" +
                "            )   \n" +
                "            AND tr.id       = w.TR_ID  \n" +
                "            AND w.WO_TYPE_ID= wt.ID    \n" +
                "            AND tr.CD_LEVEL_1 is not null AND w.CD_LEVEL_5 IS NULL AND w.CD_LEVEL_1 = '242656' ");
        if (StringUtils.isNotEmpty(obj.getFromDate())) {
            sql.append(" AND w.CREATED_DATE >= TO_DATE(:fromDate, 'dd/mm/yyyy')");
        }
        if (StringUtils.isNotEmpty(obj.getToDate())) {
            sql.append(" AND w.CREATED_DATE < TO_DATE(:toDate, 'dd/mm/yyyy') + 1 ");
        }
        if (obj.getListWoTypeId() != null && obj.getListWoTypeId().size() > 0) {
            sql.append(" AND w.WO_TYPE_ID in (:listWoTypeId) ");
        }
        if (StringUtils.isNotEmpty(obj.getGroupCreated().toString())) {
            sql.append(" AND tr.GROUP_CREATED = :groupCreated ");
        }
        String stringSql = stringSql(obj);
        sql.append(stringSql);

        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        if (StringUtils.isNotEmpty(obj.getFromDate())) {
            query.setParameter("fromDate", obj.getFromDate());
            queryCount.setParameter("fromDate", obj.getFromDate());
        }
        if (StringUtils.isNotEmpty(obj.getToDate())) {
            query.setParameter("toDate", obj.getToDate());
            queryCount.setParameter("toDate", obj.getToDate());
        }
        if (obj.getListWoTypeId() != null && obj.getListWoTypeId().size() > 0) {
            query.setParameterList("listWoTypeId", obj.getListWoTypeId());
            queryCount.setParameterList("listWoTypeId", obj.getListWoTypeId());
        }
        if (StringUtils.isNotEmpty(obj.getGroupCreated().toString())) {
            query.setParameter("groupCreated", obj.getGroupCreated());
            queryCount.setParameter("groupCreated", obj.getGroupCreated());
        }
        query.addScalar("woCode", new StringType());
        query.addScalar("woTypeName", new StringType());
        query.addScalar("trCode", new StringType());
        query.addScalar("state", new StringType());
        query.addScalar("createdDateStr", new StringType());
        query.addScalar("finishDateStr", new StringType());
        query.addScalar("moneyValue", new DoubleType());
        query.addScalar("cdLevel2Name", new StringType());
        query.addScalar("cdLevel3Name", new StringType());
        query.addScalar("cdLevel4Name", new StringType());
        query.addScalar("ftName", new StringType());
        query.addScalar("overdueReason", new StringType());
        query.addScalar("overdueApprovePerson", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(WoDTO.class));
        if (obj.getPage() != null && obj.getPageSize() != null) {
            query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
            query.setMaxResults(obj.getPageSize().intValue());
        }
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
        List<WoDTO> lst = query.list();
        return lst;
    }

    public List<WoDTO> doSearchReportWoGroupDetail(ReportWoTrDTO obj) {
        String stringSql = stringSql(obj);
        StringBuilder sql = new StringBuilder("     SELECT    w.WO_CODE AS woCode,\n" +
                "                           wt.WO_TYPE_NAME AS woTypeName,\n" +
                "                           w.TR_CODE AS trCode,\n" +
                "                           ap.name AS state,\n" +
                "                           to_char(w.CREATED_DATE,'dd/MM/yyyy') createdDateStr,\n" +
                "                           to_char(w.FINISH_DATE,'dd/MM/yyyy') finishDateStr,\n" +
                "                           w.MONEY_VALUE moneyValue,\n" +
                "                           w.CD_LEVEL_2_NAME cdLevel2Name,\n" +
                "                           w.CD_LEVEL_3_NAME cdLevel3Name,\n" +
                "                           w.CD_LEVEL_4_NAME cdLevel4Name,\n" +
                "                           w.FT_NAME  ftName ,w.OVERDUE_REASON overdueReason, w.OVERDUE_APPROVE_PERSON overdueApprovePerson  \n" +
                "    FROM\n" +
                "        WO_TYPE wt ,\n" +
                "        APP_PARAM ap,\n" +
                "        wo_tr tr,\n" +
                "        WO w                    \n" +
                "    where \n" +
                "        w.status  =1                    \n" +
                "            AND w.state = ap.CODE     \n" +
                "            AND ap.PAR_TYPE = 'WO_XL_STATE'\n" +
                "        AND tr.Id = w.tr_id                   \n" +
                "        AND tr.status = 1  \n" +
                "        AND w.WO_TYPE_ID= wt.ID                   \n" +
                "        AND wt.status   = 1  AND w.CD_LEVEL_5 IS NULL \n" +
                "        AND wt.wo_type_code NOT IN(\n" +
                "            'XLSC','BDDK','HCQT','DOANHTHU','5S'\n" +
                "        )                   \n" +
                "        AND wt.id       =w.wo_type_id                   \n" +
                "        AND w.CD_LEVEL_1 is not null  AND w.CD_LEVEL_1 = '242656' \n" +
                "        AND nvl(w.type,0) < 1  ");
        if (StringUtils.isNotEmpty(obj.getFromDate())) {
            sql.append(" AND w.CREATED_DATE >= TO_DATE(:fromDate, 'dd/mm/yyyy')");
        }
        if (StringUtils.isNotEmpty(obj.getToDate())) {
            sql.append(" AND w.CREATED_DATE < TO_DATE(:toDate, 'dd/mm/yyyy') + 1 ");
        }
        if (obj.getListWoTypeId() != null && obj.getListWoTypeId().size() > 0) {
            sql.append(" AND w.WO_TYPE_ID in (:listWoTypeId) ");
        }
        if (StringUtils.isNotEmpty(obj.getGroupCreated().toString())) {
            sql.append(" AND tr.GROUP_CREATED = :groupCreated ");
        }
        sql.append(stringSql);
        if (obj.getGroupCreated() != null && obj.getGroupCreated() < 2) {
            sql.append("UNION ALL\n" +
                    "SELECT w.WO_CODE  AS woCode,\n" +
                    "  wt.WO_TYPE_NAME AS woTypeName,\n" +
                    "  w.TR_CODE       AS trCode,\n" +
                    "  ap.name         AS state,\n" +
                    "  TO_CHAR(w.CREATED_DATE,'dd/MM/yyyy') createdDateStr,\n" +
                    "  TO_CHAR(w.FINISH_DATE,'dd/MM/yyyy') finishDateStr,\n" +
                    "  w.MONEY_VALUE moneyValue,\n" +
                    "  w.CD_LEVEL_2_NAME cdLevel2Name,\n" +
                    "  w.CD_LEVEL_3_NAME cdLevel3Name,\n" +
                    "  w.CD_LEVEL_4_NAME cdLevel4Name,\n" +
                    "  w.FT_NAME ftName ,w.OVERDUE_REASON overdueReason, w.OVERDUE_APPROVE_PERSON overdueApprovePerson\n" +
                    "FROM WO_TYPE wt ,\n" +
                    "  APP_PARAM ap,\n" +
                    "  WO w\n" +
                    "WHERE w.status           =1\n" +
                    "            AND w.state = ap.CODE \n" +
                    "            AND ap.PAR_TYPE = 'WO_XL_STATE' \n" +
                    "AND NVL(W.TR_ID,0)       = 0\n" +
                    "AND wt.status            = 1\n" +
                    "AND wt.wo_type_code NOT IN('XLSC','BDDK','HCQT','DOANHTHU','5S')\n" +
                    "AND wt.id                = w.wo_type_id AND w.CD_LEVEL_5 IS NULL \n" +
                    "AND w.CD_LEVEL_1        IS NOT NULL\n" +
                    "AND NVL(w.type,0)        < 2  AND w.CD_LEVEL_1 = '242656' ");
            if (StringUtils.isNotEmpty(obj.getFromDate())) {
                sql.append(" AND w.CREATED_DATE >= TO_DATE(:fromDate, 'dd/mm/yyyy')");
            }
            if (StringUtils.isNotEmpty(obj.getToDate())) {
                sql.append(" AND w.CREATED_DATE < TO_DATE(:toDate, 'dd/mm/yyyy') + 1 ");
            }
            if (obj.getListWoTypeId() != null && obj.getListWoTypeId().size() > 0) {
                sql.append(" AND w.WO_TYPE_ID in (:listWoTypeId) ");
            }
            if (obj.getGroupCreated() != null && obj.getGroupCreated() == 0) {
                sql.append(" AND NVL(w.type,0) = 0");
            }
            if (obj.getGroupCreated() != null && obj.getGroupCreated() == 1) {
                sql.append(" AND NVL(w.type,0) = 1");
            }
            sql.append(stringSql);
        }
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        if (StringUtils.isNotEmpty(obj.getFromDate())) {
            query.setParameter("fromDate", obj.getFromDate());
            queryCount.setParameter("fromDate", obj.getFromDate());
        }
        if (StringUtils.isNotEmpty(obj.getToDate())) {
            query.setParameter("toDate", obj.getToDate());
            queryCount.setParameter("toDate", obj.getToDate());
        }
        if (obj.getListWoTypeId() != null && obj.getListWoTypeId().size() > 0) {
            query.setParameterList("listWoTypeId", obj.getListWoTypeId());
            queryCount.setParameterList("listWoTypeId", obj.getListWoTypeId());
        }
        if (StringUtils.isNotEmpty(obj.getGroupCreated().toString())) {
            query.setParameter("groupCreated", obj.getGroupCreated());
            queryCount.setParameter("groupCreated", obj.getGroupCreated());
        }
        if (obj.getGroupCreated() != null && obj.getGroupCreated() < 2) {
            if (StringUtils.isNotEmpty(obj.getFromDate())) {
                query.setParameter("fromDate", obj.getFromDate());
                queryCount.setParameter("fromDate", obj.getFromDate());
            }
            if (StringUtils.isNotEmpty(obj.getToDate())) {
                query.setParameter("toDate", obj.getToDate());
                queryCount.setParameter("toDate", obj.getToDate());
            }
            if (obj.getListWoTypeId() != null && obj.getListWoTypeId().size() > 0) {
                query.setParameterList("listWoTypeId", obj.getListWoTypeId());
                queryCount.setParameterList("listWoTypeId", obj.getListWoTypeId());
            }
        }
        query.addScalar("woCode", new StringType());
        query.addScalar("woTypeName", new StringType());
        query.addScalar("trCode", new StringType());
        query.addScalar("state", new StringType());
        query.addScalar("createdDateStr", new StringType());
        query.addScalar("finishDateStr", new StringType());
        query.addScalar("moneyValue", new DoubleType());
        query.addScalar("cdLevel2Name", new StringType());
        query.addScalar("cdLevel3Name", new StringType());
        query.addScalar("cdLevel4Name", new StringType());
        query.addScalar("ftName", new StringType());
        query.addScalar("overdueReason", new StringType());
        query.addScalar("overdueApprovePerson", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(WoDTO.class));
        if (obj.getPage() != null && obj.getPageSize() != null) {
            query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
            query.setMaxResults(obj.getPageSize().intValue());
        }
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
        List<WoDTO> lst = query.list();
        return lst;
    }

    public List<WoDTO> doSearchReportWoProDetail(ReportWoTrDTO obj) {
        StringBuilder sql = new StringBuilder("SELECT\n" +
                "            w.WO_CODE AS woCode,\n" +
                "            wt.WO_TYPE_NAME AS woTypeName,\n" +
                "            w.TR_CODE AS trCode,\n" +
                "            ap.name AS state,\n" +
                "            to_char(w.CREATED_DATE,'dd/MM/yyyy') createdDateStr,\n" +
                "            to_char(w.FINISH_DATE, 'dd/MM/yyyy') finishDateStr,\n" +
                "            w.MONEY_VALUE moneyValue,\n" +
                "            w.CD_LEVEL_2_NAME cdLevel2Name,\n" +
                "            w.CD_LEVEL_3_NAME cdLevel3Name,\n" +
                "            w.CD_LEVEL_4_NAME cdLevel4Name,\n" +
                "            w.FT_NAME  ftName ,w.OVERDUE_REASON overdueReason, w.OVERDUE_APPROVE_PERSON overdueApprovePerson \n" +
                "        FROM\n" +
                "            WO_TYPE wt ,\n" +
                "            APP_PARAM ap,\n" +
                "            sys_group sg ,\n" +
                "            WO w    \n" +
                "        where\n" +
                "            w.status  =1  \n" +
                "            AND  sg.sys_group_id = w.CD_LEVEL_2 \n" +
                "            AND sg.status=1 \n" +
                "            AND w.state = ap.CODE              \n" +
                "            AND ap.PAR_TYPE = 'WO_XL_STATE'   \n" +
                "            AND w.WO_TYPE_ID= wt.ID   \n" +
                "            AND wt.status   = 1 \n" +
                "            AND wt.wo_type_code NOT IN(\n" +
                "                'XLSC','BDDK','HCQT','DOANHTHU','5S'\n" +
                "            )   \n" +
                "            AND wt.id       =w.wo_type_id   \n" +
                "            AND w.CD_LEVEL_1 is not null  AND w.CD_LEVEL_5 IS NULL AND w.CD_LEVEL_1 = '242656' ");
        if (StringUtils.isNotEmpty(obj.getFromDate())) {
            sql.append(" AND w.CREATED_DATE >= TO_DATE(:fromDate, 'dd/mm/yyyy')");
        }
        if (StringUtils.isNotEmpty(obj.getToDate())) {
            sql.append(" AND w.CREATED_DATE < TO_DATE(:toDate, 'dd/mm/yyyy') + 1 ");
        }
        if (obj.getListWoTypeId() != null && obj.getListWoTypeId().size() > 0) {
            sql.append(" AND w.WO_TYPE_ID in (:listWoTypeId) ");
        }

        if (StringUtils.isNoneBlank(obj.getSysGroupAssign())) {
            if (obj.getSysGroupAssign().equals("1")) {
                sql.append(" AND nvl(w.type,0) not in(1,2) and nvl(w.TR_ID,0) =0  ");
            } else if (obj.getSysGroupAssign().equals("2")) {
                sql.append(" and nvl(w.type,0) =1  ");
            } else if (obj.getSysGroupAssign().equals("3")) {
                sql.append(" and w.tr_id in(select tr_id from wo_tr where status=1 and GROUP_CREATED_NAME='Trung tâm Xây dựng và Đầu tư hạ tầng') ");
            } else if (obj.getSysGroupAssign().equals("4")) {
                sql.append(" and w.tr_id in(select tr_id from wo_tr where status=1 and GROUP_CREATED_NAME='Trung tâm Giải pháp tích hợp')  ");
            }
        }
        if (obj.getListGroupId() != null && obj.getListGroupId().size() > 0) {
            sql.append(" AND sg.PARENT_ID in (:listGroupId) ");
        }
        if (StringUtils.isNotEmpty(obj.getGroupCreated().toString())) {
            sql.append(" AND sg.PARENT_ID = :groupCreated ");
        }
        String stringSql = stringSql(obj);
        sql.append(stringSql);

        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        if (StringUtils.isNotEmpty(obj.getFromDate())) {
            query.setParameter("fromDate", obj.getFromDate());
            queryCount.setParameter("fromDate", obj.getFromDate());
        }
        if (StringUtils.isNotEmpty(obj.getToDate())) {
            query.setParameter("toDate", obj.getToDate());
            queryCount.setParameter("toDate", obj.getToDate());
        }
        if (obj.getListWoTypeId() != null && obj.getListWoTypeId().size() > 0) {
            query.setParameterList("listWoTypeId", obj.getListWoTypeId());
            queryCount.setParameterList("listWoTypeId", obj.getListWoTypeId());
        }
        if (obj.getListGroupId() != null && obj.getListGroupId().size() > 0) {
            query.setParameterList("listGroupId", obj.getListGroupId());
            queryCount.setParameterList("listGroupId", obj.getListGroupId());
        }
        if (StringUtils.isNotEmpty(obj.getGroupCreated().toString())) {
            query.setParameter("groupCreated", obj.getGroupCreated().toString());
            queryCount.setParameter("groupCreated", obj.getGroupCreated().toString());
        }
        query.addScalar("woCode", new StringType());
        query.addScalar("woTypeName", new StringType());
        query.addScalar("trCode", new StringType());
        query.addScalar("state", new StringType());
        query.addScalar("createdDateStr", new StringType());
        query.addScalar("finishDateStr", new StringType());
        query.addScalar("moneyValue", new DoubleType());
        query.addScalar("cdLevel2Name", new StringType());
        query.addScalar("cdLevel3Name", new StringType());
        query.addScalar("cdLevel4Name", new StringType());
        query.addScalar("ftName", new StringType());
        query.addScalar("overdueReason", new StringType());
        query.addScalar("overdueApprovePerson", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(WoDTO.class));
        if (obj.getPage() != null && obj.getPageSize() != null) {
            query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
            query.setMaxResults(obj.getPageSize().intValue());
        }
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
        List<WoDTO> lst = query.list();
        return lst;
    }

    public String stringSql(ReportWoTrDTO obj) {
        String sql = "";
        if (obj.getStatus() != null && obj.getStatus().toLowerCase().equals("cnktapprove")) {
            sql = "  AND  w.state  not in ('ASSIGN_CD') \n" +
                    "  AND ( to_char(nvl(w.USER_CD_LEVEL2_RECEIVE_WO,0)) !='-1' \n" +
                    " OR to_char(nvl(w.USER_CD_LEVEL3_RECEIVE_WO,0)) !='-1' OR to_char(nvl(w.USER_CD_LEVEL4_RECEIVE_WO,0)) !='-1')";
        }
        if (obj.getStatus() != null && obj.getStatus().toLowerCase().equals("cnktsystemwo")) {
            sql = "  AND  w.state  not in ('ASSIGN_CD') \n" +
                    "  AND ( to_char(nvl(w.USER_CD_LEVEL2_RECEIVE_WO,0)) ='-1' \n" +
                    " OR to_char(nvl(w.USER_CD_LEVEL3_RECEIVE_WO,0)) ='-1' OR to_char(nvl(w.USER_CD_LEVEL4_RECEIVE_WO,0)) ='-1') ";
        }
        if (obj.getStatus() != null && obj.getStatus().toLowerCase().equals("woassigncd")) {
            sql = " AND w.state='ASSIGN_CD' ";
        }
        if (obj.getStatus() != null && obj.getStatus().toLowerCase().equals("woacceptcd")) {
            sql = " AND w.state='ACCEPT_CD' ";
        }
        if (obj.getStatus() != null && obj.getStatus().toLowerCase().equals("woacceptft")) {
            sql = " AND w.state  in ('ACCEPT_FT','PROCESSING','DONE','OK','NG','OPINION_RQ_1','OPINION_RQ_2','OPINION_RQ_3','OPINION_RQ_4','CD_OK','CD_NG','WAIT_TC_TCT','WAIT_TC_BRANCH')  and (nvl(w.USER_FT_RECEIVE_WO,0) !='-1') ";
        }
        if (obj.getStatus() != null && obj.getStatus().toLowerCase().equals("woacceptftsystem")) {
            sql = " AND w.state  in ('ACCEPT_FT','PROCESSING','DONE','OK','NG','OPINION_RQ_1','OPINION_RQ_2','OPINION_RQ_3','OPINION_RQ_4','CD_OK','CD_NG','WAIT_TC_TCT','WAIT_TC_BRANCH')  and (nvl(w.USER_FT_RECEIVE_WO,0) ='-1') ";
        }
        if (obj.getStatus() != null && obj.getStatus().toLowerCase().equals("woacceptftwo")) {
            sql = " AND w.state ='ACCEPT_CD' ";
        }
        if (obj.getStatus() != null && obj.getStatus().toLowerCase().equals("woassignft")) {
            sql = " AND w.state in ('ASSIGN_FT') ";
        }
        if (obj.getStatus() != null && obj.getStatus().toLowerCase().equals("wofinish")) {
            sql = " AND w.state in ('CD_OK','DONE','OK','WAIT_TC_TCT','WAIT_TC_BRANCH') ";
        }
        if (obj.getStatus() != null && obj.getStatus().toLowerCase().equals("wocompleted")) {
            sql = " AND w.state in ('CD_OK','DONE','OK','WAIT_TC_TCT','WAIT_TC_BRANCH') and trunc(w.end_time) <= trunc(w.FINISH_DATE) ";
        }
        if (obj.getStatus() != null && obj.getStatus().toLowerCase().equals("wonotcompleted")) {
            sql = " AND w.state in ('CD_OK','DONE','OK','WAIT_TC_TCT','WAIT_TC_BRANCH') and trunc(w.end_time) > trunc(w.FINISH_DATE) ";
        }
        if (obj.getStatus() != null && obj.getStatus().toLowerCase().equals("wonotfinish")) {
            sql = " AND w.state not in ('CD_OK','DONE','OK','ASSIGN_CD','REJECT_CD','WAIT_TC_TCT','WAIT_TC_BRANCH') ";
        }
        if (obj.getStatus() != null && obj.getStatus().toLowerCase().equals("wonotfinishdate")) {
            sql = " AND w.state not in ('CD_OK','DONE','OK','ASSIGN_CD','REJECT_CD','WAIT_TC_TCT','WAIT_TC_BRANCH')and trunc(sysdate) <= trunc(w.FINISH_DATE) ";
        }
        if (obj.getStatus() != null && obj.getStatus().toLowerCase().equals("wofinishdayex")) {
            sql = " AND w.state not in ('CD_OK','DONE','OK','ASSIGN_CD','REJECT_CD','WAIT_TC_TCT','WAIT_TC_BRANCH')and trunc(sysdate) > trunc(w.FINISH_DATE) ";
        }
        return sql;
    }

    public List<WoTrDTO> doSearchReportTrDetail(ReportWoTrDTO obj) {
        StringBuilder sql = new StringBuilder(" SELECT tr.TR_CODE trCode,\n" +
                "  tr.TR_NAME trName,\n" +
                "  ap.name AS state ,\n" +
                "  TO_CHAR(tr.CREATED_DATE,'dd/MM/yyyy') createdDateStr,\n" +
                "  TO_CHAR(tr.FINISH_DATE, 'dd/MM/yyyy') finishDateStr,\n" +
                "  tr.QUANTITY_VALUE quantityValue\n" +
                "FROM WO_TYPE wt,\n" +
                "  APP_PARAM ap,\n" +
                "  WO_TR tr ,\n" +
                "  WO w\n" +
                "WHERE w.status           =1\n" +
                "AND tr.state = ap.CODE\n" +
                "AND ap.PAR_TYPE = 'WO_TR_XL_STATE'\n" +
                "AND tr.status            = 1\n" +
                "AND tr.CD_LEVEL_2       IS NULL\n" +
                "AND wt.wo_type_code NOT IN ('XLSC','BDDK','HCQT','DOANHTHU','5S')\n" +
                "AND tr.id                = w.TR_ID\n" +
                "AND w.WO_TYPE_ID         = wt.ID\n" +
                "AND tr.CD_LEVEL_1       IS NOT NULL  AND tr.CD_LEVEL_1 = '242656' ");
        if (StringUtils.isNotEmpty(obj.getFromDate())) {
            sql.append(" AND tr.CREATED_DATE >= TO_DATE(:fromDate, 'dd/mm/yyyy')");
        }
        if (StringUtils.isNotEmpty(obj.getToDate())) {
            sql.append(" AND tr.CREATED_DATE < TO_DATE(:toDate, 'dd/mm/yyyy') + 1 ");
        }
        if (StringUtils.isNotEmpty(obj.getGroupCreated().toString())) {
            sql.append(" AND tr.GROUP_CREATED = :groupCreated ");
        }
        if (obj.getStatus() != null && obj.getStatus().toLowerCase().equals("tthtassigncd")) {
            sql.append(" AND 1 = 0 ");
        }
        if (obj.getStatus() != null && !obj.getStatus().toLowerCase().equals("tthttrwo")) {
            String stringSql = stringSqlTr(obj);
            sql.append(stringSql);
            sql.append(" GROUP BY  tr.TR_CODE, tr.TR_NAME, ap.name,tr.CREATED_DATE,tr.FINISH_DATE,tr.QUANTITY_VALUE  UNION ALL  \n" +
                    "SELECT tr.TR_CODE trCode,\n" +
                    "  tr.TR_NAME trName,\n" +
                    "  ap.name AS state ,\n" +
                    "  TO_CHAR(tr.CREATED_DATE,'dd/MM/yyyy') createdDateStr,\n" +
                    "  TO_CHAR(tr.FINISH_DATE, 'dd/MM/yyyy') finishDateStr,\n" +
                    "  tr.QUANTITY_VALUE quantityValue\n" +
                    "FROM WO_TR tr,APP_PARAM ap\n" +
                    "WHERE tr.status    = 1\n" +
                    "AND tr.state = ap.CODE\n" +
                    "AND ap.PAR_TYPE = 'WO_TR_XL_STATE'\n" +
                    "AND tr.CD_LEVEL_2 IS NULL\n" +
                    "AND tr.CD_LEVEL_1 IS NOT NULL\n" +
                    "AND tr.ID NOT IN \n" +
                    "  (SELECT DISTINCT TR_ID\n" +
                    "  FROM WO w\n" +
                    "  WHERE tr.id       =w.TR_ID\n" +
                    "  AND w.TR_ID      IS NOT NULL\n" +
                    "  AND w.CD_LEVEL_1 IS NOT NULL AND w.CD_LEVEL_1 ='242656' \n" +
                    "  AND w.status      =1\n" +
                    "  AND w.TR_ID      IS NOT NULL)");
            if (StringUtils.isNotEmpty(obj.getFromDate())) {
                sql.append(" AND tr.CREATED_DATE >= TO_DATE(:fromDate, 'dd/mm/yyyy')");
            }
            if (StringUtils.isNotEmpty(obj.getToDate())) {
                sql.append(" AND tr.CREATED_DATE < TO_DATE(:toDate, 'dd/mm/yyyy') + 1 ");
            }
            if (StringUtils.isNotEmpty(obj.getGroupCreated().toString())) {
                sql.append(" AND tr.GROUP_CREATED = :groupCreated ");
            }
            sql.append(stringSql);
            sql.append("  GROUP BY  tr.TR_CODE, tr.TR_NAME, ap.name,tr.CREATED_DATE,tr.FINISH_DATE,tr.QUANTITY_VALUE");
        } else {
            sql.append("  GROUP BY  tr.TR_CODE, tr.TR_NAME, ap.name,tr.CREATED_DATE,tr.FINISH_DATE,tr.QUANTITY_VALUE ");
        }
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        if (StringUtils.isNotEmpty(obj.getFromDate())) {
            query.setParameter("fromDate", obj.getFromDate());
            queryCount.setParameter("fromDate", obj.getFromDate());
        }
        if (StringUtils.isNotEmpty(obj.getToDate())) {
            query.setParameter("toDate", obj.getToDate());
            queryCount.setParameter("toDate", obj.getToDate());
        }
        if (StringUtils.isNotEmpty(obj.getGroupCreated().toString())) {
            query.setParameter("groupCreated", obj.getGroupCreated());
            queryCount.setParameter("groupCreated", obj.getGroupCreated());
        }
        if (StringUtils.isNotEmpty(obj.getFromDate())) {
            query.setParameter("fromDate", obj.getFromDate());
            queryCount.setParameter("fromDate", obj.getFromDate());
        }
        if (StringUtils.isNotEmpty(obj.getToDate())) {
            query.setParameter("toDate", obj.getToDate());
            queryCount.setParameter("toDate", obj.getToDate());
        }
        if (StringUtils.isNotEmpty(obj.getGroupCreated().toString())) {
            query.setParameter("groupCreated", obj.getGroupCreated());
            queryCount.setParameter("groupCreated", obj.getGroupCreated());
        }
        query.addScalar("trCode", new StringType());
        query.addScalar("trName", new StringType());
        query.addScalar("state", new StringType());
        query.addScalar("createdDateStr", new StringType());
        query.addScalar("finishDateStr", new StringType());
        query.addScalar("quantityValue", new DoubleType());
        query.setResultTransformer(Transformers.aliasToBean(WoTrDTO.class));
        if (obj.getPage() != null && obj.getPageSize() != null) {
            query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize().intValue());
            query.setMaxResults(obj.getPageSize().intValue());
        }
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
        List<WoTrDTO> lst = query.list();
        return lst;
    }

    public String stringSqlTr(ReportWoTrDTO obj) {
        String sql = "";
        if (obj.getStatus() != null && obj.getStatus().toLowerCase().equals("tthtapprove")) {
            sql = "AND  to_char(nvl(tr.USER_RECEIVE_TR,0)) != '-1'  AND tr.state not in('ASSIGN_CD', 'REJECT_CD')";
        }
        if (obj.getStatus() != null && obj.getStatus().toLowerCase().equals("tthtsystem")) {
            sql = "AND  tr.USER_RECEIVE_TR = '-1'  ";
        }
        if (obj.getStatus() != null && obj.getStatus().toLowerCase().equals("tthtassigncd")) {
            sql = " AND tr.state='ASSIGN_CD' AND tr.USER_RECEIVE_TR is null ";
        }
        if (obj.getStatus() != null && obj.getStatus().toLowerCase().equals("tthtrejectcd")) {
            sql = " AND tr.state='REJECT_CD' ";
        }

        if (obj.getStatus() != null && obj.getStatus().toLowerCase().equals("tthtnottrwo")) {
            sql = "  AND tr.state='ACCEPT_CD' ";
        }
        return sql;
    }

    public List<CatWorkItemTypeDTO> getAutoWoWorkItems(CatWorkItemTypeDTO searchObj) {
        String sql = "select cat_work_item_type_id catWorkItemTypeId, name, code, status, cat_construction_type_id catConstructionTypeId, " +
                " tr_branch trBranch, hm_type_value hmTypeValue, hm_value hmValue, hm_quota_time hmQuotaTime" +
                "  from CAT_WORK_ITEM_TYPE where status > 0 ";

        if (searchObj.getTrBranch() != null) sql += " and tr_branch = :trBranch ";
        if (searchObj.getCatConstructionTypeId() != null)
            sql += " and cat_construction_type_id = :catConstructionTypeId ";

        sql += " order by name";

        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql);
        sqlCount.append(")");
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
        SQLQuery query = getSession().createSQLQuery(sql);

        if (searchObj.getTrBranch() != null) {
            query.setParameter("trBranch", searchObj.getTrBranch());
            queryCount.setParameter("trBranch", searchObj.getTrBranch());
        }

        if (searchObj.getCatConstructionTypeId() != null) {
            query.setParameter("catConstructionTypeId", searchObj.getCatConstructionTypeId());
            queryCount.setParameter("catConstructionTypeId", searchObj.getCatConstructionTypeId());
        }

        query.addScalar("catWorkItemTypeId", new LongType());
        query.addScalar("name", new StringType());
        query.addScalar("code", new StringType());
        query.addScalar("status", new StringType());
        query.addScalar("catConstructionTypeId", new LongType());
        query.addScalar("trBranch", new LongType());
        query.addScalar("hmTypeValue", new LongType());
        query.addScalar("hmValue", new DoubleType());
        query.addScalar("hmQuotaTime", new LongType());
        query.setResultTransformer(Transformers.aliasToBean(CatWorkItemTypeDTO.class));

        if (searchObj.getPage() != null && searchObj.getPageSize() != null) {
            query.setFirstResult((searchObj.getPage().intValue() - 1) * searchObj.getPageSize().intValue());
            query.setMaxResults(searchObj.getPageSize().intValue());
        }
        searchObj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
        List<CatWorkItemTypeDTO> lst = query.list();

        return lst;
    }

    public CatWorkItemTypeDTO getCatWorkItemType(String code) {
        StringBuilder sql = new StringBuilder("SELECT cat.CAT_WORK_ITEM_TYPE_ID catWorkItemTypeId," + " cat.CODE code," + " cat.TAB tab, " + " cat.ITEM_ORDER itemOrder, "
                + " cat.NAME name," + " cat.STATUS status, " + " CAT_CONSTRUCTION_TYPE.NAME catConstructionType, " + " cat.CREATED_DATE createdDate, " + " cat.UPDATED_DATE updatedDate, " + " cat.CREATED_USER createdUser, " + " cat.UPDATED_USER updatedUser, "
                + " cat.DESCRIPTION description, " + " cat.CAT_CONSTRUCTION_TYPE_ID catConstructionTypeId," + "cat.QUANTITY_BY_DATE quantityByDate "
                + " , cat.TR_BRANCH trBranch, cat.HM_TYPE_VALUE hmTypeValue, cat.HM_VALUE hmValue, cat.HM_QUOTA_TIME hmQuotaTime "
                + " FROM CAT_WORK_ITEM_TYPE cat "
                + " LEFT JOIN CAT_CONSTRUCTION_TYPE ON cat.CAT_CONSTRUCTION_TYPE_ID = CAT_CONSTRUCTION_TYPE.CAT_CONSTRUCTION_TYPE_ID"
                + " WHERE cat.status != '0' ");

        if (code != null) sql.append(" and cat.CODE = :code");

        SQLQuery query = getSession().createSQLQuery(sql.toString());

        if (code != null) query.setParameter("code", code);

        query.addScalar("catWorkItemTypeId", new LongType());
        query.addScalar("code", new StringType());
        query.addScalar("name", new StringType());
        query.addScalar("status", new StringType());
        query.addScalar("catConstructionType", new StringType());
        query.addScalar("createdDate", new DateType());
        query.addScalar("updatedDate", new DateType());
        query.addScalar("catConstructionTypeId", new LongType());

        query.addScalar("trBranch", new LongType());
        query.addScalar("hmTypeValue", new LongType());
        query.addScalar("hmValue", new DoubleType());
        query.addScalar("hmQuotaTime", new LongType());
        query.setResultTransformer(Transformers.aliasToBean(CatWorkItemTypeDTO.class));

        List<CatWorkItemTypeDTO> lst = query.list();
        if (lst.size() > 0) return lst.get(0);
        return null;
    }

    public WoSimpleSysGroupDTO tryGetCdLevel2FromConstruction(Long constructionId) {
        if (constructionId == null) return null;

        String sql = "select a.sys_group_id sysGroupId, a.name groupName from sys_group a " +
                " where a.parent_id = (select c.sys_group_id from construction c where c.construction_id = :constructionId) " +
                " and (a.code like '%P.HT%' or a.code like '%P.XD%') and a.status>0 ";

        SQLQuery query = getSession().createSQLQuery(sql);

        query.setParameter("constructionId", constructionId);

        query.addScalar("sysGroupId", new LongType());
        query.addScalar("groupName", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(WoSimpleSysGroupDTO.class));

        List<WoSimpleSysGroupDTO> lst = query.list();
        if (lst.size() > 0) return lst.get(0);
        return null;
    }

    public List<CatConstructionTypeDTO> getCatConstructionTypes() {
        String sql = "select CAT_CONSTRUCTION_TYPE_ID catConstructionTypeId, name from cat_construction_type where status != 0 ";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.addScalar("catConstructionTypeId", new LongType());
        query.addScalar("name", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(CatConstructionTypeDTO.class));
        return query.list();
    }

    public List<WoGeneralReportDTO> getDataTrForChart(Long loginUserId, List<String> lstGroupIds) {
        String employeeCode = getSysUser(loginUserId).getEmployeeCode();
        String sqlChart = "select \n" +
                "  count(case when wt.status = 1 then 1 end) as totalTR\n" +
                ", count(case when wt.state ='OK' then 1 end) as totalTrOk\n" +
                ", count(case when wt.state ='OPINION_RQ' then 1 end) as totalTrOpinionRq\n" +
                ", count(case when wt.state ='PROCESSING' then 1 end) as totalTrProcessing\n" +
                ", count(case when wt.state ='ASSIGN_CD' then 1 end) as totalTrAssignCd\n" +
                ", count(case when wt.state ='ASSIGN_FT' then 1 end) as totalTrAssignFt\n" +
                ", count(case when wt.state ='NOK' then 1 end) as totalTrNotOk\n" +
                ", count(case when wt.state ='DONE' then 1 end) as totalTrDone\n" +
                ", count(case when wt.state ='REJECT_CD' then 1 end) as totalTrRejectCd\n" +
                ", count(case when wt.state ='ACCEPT_CD' then 1 end) as totalTrAcceptCd\n" +
                "from WO_TR wt\n" +
                "left join WO_TR_TYPE wtt on wt.TR_TYPE_ID = wtt.ID\n" +
                "WHERE  wt.STATUS = 1  and wt.CD_LEVEL_1 is not null and wt.CREATED_DATE >=sysdate-90 " +
                "AND (wt.USER_CREATED = :employeeCode";
        if (lstGroupIds.size() > 0) {
            sqlChart += " OR wt.GROUP_CREATED in (:lstGroupIds) OR wt.CD_LEVEL_1 in (:lstGroupIds) OR wt.CD_LEVEL_2 in (:lstGroupIds)";
        }
        sqlChart += ")";
        StringBuilder sql = new StringBuilder(sqlChart);
        SQLQuery query = getSession().createSQLQuery(sql.toString());

        query.addScalar("totalTR", new LongType());
        query.addScalar("totalTrOk", new LongType());
        query.addScalar("totalTrOpinionRq", new LongType());
        query.addScalar("totalTrProcessing", new LongType());
        query.addScalar("totalTrAssignCd", new LongType());
        query.addScalar("totalTrAssignFt", new LongType());
        query.addScalar("totalTrNotOk", new LongType());
        query.addScalar("totalTrDone", new LongType());
        query.addScalar("totalTrRejectCd", new LongType());
        query.addScalar("totalTrAcceptCd", new LongType());

        query.setParameter("employeeCode", employeeCode);
        if (lstGroupIds.size() > 0) {
            query.setParameterList("lstGroupIds", lstGroupIds);
        }

        query.setResultTransformer(Transformers.aliasToBean(WoGeneralReportDTO.class));
        return query.list();
    }

    public List<CatWorkItemTypeDTO> getListWoWorkItems(CatWorkItemTypeDTO searchObj) {
        String sql = "select cat_work_item_type_id catWorkItemTypeId, name, code, status, cat_construction_type_id catConstructionTypeId, " +
                " tr_branch trBranch, hm_type_value hmTypeValue, hm_value hmValue, hm_quota_time hmQuotaTime" +
                "  from CAT_WORK_ITEM_TYPE where status > 0 ";

        if (searchObj.getTrBranch() != null) sql += " and tr_branch = :trBranch ";
        if (searchObj.getCatConstructionTypeId() != null)
            sql += " and cat_construction_type_id = :catConstructionTypeId ";
        SQLQuery query = getSession().createSQLQuery(sql);

        if (searchObj.getTrBranch() != null) {
            query.setParameter("trBranch", searchObj.getTrBranch());
        }

        if (searchObj.getCatConstructionTypeId() != null) {
            query.setParameter("catConstructionTypeId", searchObj.getCatConstructionTypeId());
        }

        query.addScalar("catWorkItemTypeId", new LongType());
        query.addScalar("name", new StringType());
        query.addScalar("code", new StringType());
        query.addScalar("status", new StringType());
        query.addScalar("catConstructionTypeId", new LongType());
        query.addScalar("trBranch", new LongType());
        query.addScalar("hmTypeValue", new LongType());
        query.addScalar("hmValue", new DoubleType());
        query.addScalar("hmQuotaTime", new LongType());
        query.setResultTransformer(Transformers.aliasToBean(CatWorkItemTypeDTO.class));

        List<CatWorkItemTypeDTO> lst = query.list();

        return lst;
    }

    public Map<String, Long> getCatWorkItemTypeId(Long constructionId) {
        Map<String, Long> mapCode = new HashMap<String, Long>();
        StringBuilder sql = new StringBuilder("select cwt.CODE ||'-'||c.CONSTRUCTION_ID code,cwt.cat_work_item_type_id catWorkItemTypeId from CAT_WORK_ITEM_TYPE cwt ,CONSTRUCTION c where cwt.CAT_CONSTRUCTION_TYPE_ID = c.CAT_CONSTRUCTION_TYPE_ID AND cwt.STATUS != 0  AND c.CONSTRUCTION_ID =:constructionId ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());

        query.addScalar("code", new StringType());
        query.addScalar("catWorkItemTypeId", new LongType());

        query.setParameter("constructionId", constructionId);

        query.setResultTransformer(Transformers.aliasToBean(CatWorkItemTypeDTO.class));

        List<CatWorkItemTypeDTO> ls = query.list();

        for (CatWorkItemTypeDTO work : ls) {
            mapCode.put(work.getCode(), work.getCatWorkItemTypeId());
        }

        return mapCode;
    }
    
    public List<ConstructionDTO> doSearchContruction(ConstructionDTO dto) {
        StringBuilder sql = new StringBuilder(" select CONSTRUCTION_ID as constructionId, CODE as code, NAME as name "
        		+ "from CONSTRUCTION "
        		+ "where 1= 1 ");
        if(StringUtils.isNoneEmpty(dto.getKeySearch())) {
        	sql.append(" and lower(CODE) LIKE :keysearch ");
        }
        sql.append(" AND STATUS > 0 "
        		+ "fetch next 20 rows only" );
        
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        if(StringUtils.isNoneEmpty(dto.getKeySearch())) {
        	query.setParameter("keysearch", "%" + dto.getKeySearch().toLowerCase() + "%");
        }
        query.addScalar("constructionId", new LongType());
        query.addScalar("code", new StringType());
        query.addScalar("name", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(ConstructionDTO.class));

        return query.list();
    }

    public boolean checkCanCreateTrDbht(String stationCode) {
        String sql = " select count(*) total from cat_station where code = :stationCode and rent_status = 1 ";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("stationCode", stationCode);
        query.addScalar("total", new LongType());
        long total = (long) query.uniqueResult();
        if (total > 0) return true;

        return false;
    }

    public List<CatStationRentDTO> getRentStation() {
        String sql = "select\n" +
                "    sg.CODE branch\n" +
                "    , sg.SYS_GROUP_ID as sysGroupId\n" +
                "    , rentStation.rentTotal\n" +
                "    , rentStation.rentOk\n" +
                "    , rentStation.rentNg\n" +
                "    , rentStation.rentNew\n" +
                "    , rentStation.rentProcessing\n" +
                "    , 0 rentTarget\n" +
                "from SYS_GROUP sg\n" +
                "left join(\n" +
                "    select\n" +
                "        CAT_PROVINCE_ID\n" +
                "        , count(*) rentTotal\n" +
                "        , sum(rentOk) rentOk\n" +
                "        , sum(rentNg) rentNg\n" +
                "        , sum(rentNew) rentNew\n" +
                "        , sum(rentProcessing) rentProcessing\n" +
                "    from (\n" +
                "        select\n" +
                "            CAT_PROVINCE_ID\n" +
                "            , case when rent_status = 3 then 1 else 0 end rentProcessing\n" +
                "            , case when rent_status = 2 then 1 else 0 end rentNew\n" +
                "            , case when rent_status = 1 then 1 else 0 end rentOk\n" +
                "            , case when rent_status = 0 then 1 else 0 end rentNg\n" +
                "        from cat_station where status!=0 "
                + " and TYPE_HTCT=1 \n" +
                "    )\n" +
                "    group by CAT_PROVINCE_ID\n" +
                ") rentStation ON sg.PROVINCE_ID = rentStation.CAT_PROVINCE_ID\n" +
                "where sg.GROUP_LEVEL = 3\n" +
                "and sg.CODE like '%P.HT%'\n" +
                "order by sg.CODE ";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.addScalar("branch", new StringType());
        query.addScalar("sysGroupId", new LongType());
        query.addScalar("rentTotal", new LongType());
        query.addScalar("rentOk", new LongType());
        query.addScalar("rentNg", new LongType());
        query.addScalar("rentNew", new LongType());
        query.addScalar("rentProcessing", new LongType());
        query.addScalar("rentTarget", new LongType());
        query.setResultTransformer(Transformers.aliasToBean(CatStationRentDTO.class));

        List<CatStationRentDTO> lst = query.list();
        return lst;
    }

    public List<CatStationRentDTO> getRentStationAssigned(Long trId, Long sysGroupId) {
        try {
            String sql = "select cs.code\n" +
                    "from WO_MAPPING_STATION wms\n" +
                    "left join cat_station cs on wms.cat_station_id = cs.cat_station_id\n" +
                    "where wo_id in (\n" +
                    "    select id from wo where tr_id = :trId and CD_LEVEL_2 = :sysGroupId \n" +
                    ") ";
            SQLQuery query = getSession().createSQLQuery(sql);
            query.setParameter("trId", trId);
            query.setParameter("sysGroupId", sysGroupId);
            query.addScalar("code", new StringType());
            query.setResultTransformer(Transformers.aliasToBean(CatStationRentDTO.class));

            List<CatStationRentDTO> lst = query.list();
            return lst;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<CatStationRentDTO> getRentStationNew(Long provinceId) {
        try {
            String sql = "select code from cat_station where cat_province_id = :provinceId and status = 1 and RENT_STATUS = 2 ";
            SQLQuery query = getSession().createSQLQuery(sql);
            query.setParameter("provinceId", provinceId);
            query.addScalar("code", new StringType());
            query.setResultTransformer(Transformers.aliasToBean(CatStationRentDTO.class));

            List<CatStationRentDTO> lst = query.list();
            return lst;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<CatStationDTO> getListStationByBranch(WoTrDTO dto) {
        try {
            String sql = "select\n" +
                    "    cs.cat_station_id catStationId\n" +
                    "    , cs.code\n" +
                    "    , cs.name\n" +
                    "    , cs.address\n" +
                    "    , 0 as isCheck\n" +
                    "    , sg.SYS_GROUP_ID tmbtSysGroupId\n" +
                    "from cat_station cs\n" +
                    "left join sys_group sg on cs.CAT_PROVINCE_ID = sg.PROVINCE_ID\n" +
                    "where sg.code = :branch\n" +
                    "and cs.status = 1\n" +
                    "and cs.type_htct = 1 " +
                    "and cs.rent_status = 2";
            if(StringUtils.isNotBlank(dto.getKeySearch())) {
            	sql += " and upper(cs.code) like upper(:keySearch) ";
            }
            SQLQuery query = getSession().createSQLQuery(sql);
            query.setParameter("branch", dto.getTmbtBranch());
            query.addScalar("catStationId", new LongType());
            query.addScalar("code", new StringType());
            query.addScalar("name", new StringType());
            query.addScalar("address", new StringType());
            query.addScalar("isCheck", new LongType());
            query.addScalar("tmbtSysGroupId", new LongType());
            
            if(StringUtils.isNotBlank(dto.getKeySearch())) {
            	query.setParameter("keySearch", "%" + ValidateUtils.validateKeySearch(dto.getKeySearch()) + "%");
            }
            
            query.setResultTransformer(Transformers.aliasToBean(CatStationDTO.class));

            return query.list();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

	public List<Object[]> getListWo(Long trId) {
		String sql = "SELECT TR_ID trId, WO_CODE woCode, wt.WO_TYPE_NAME woTypeName "
				+ " from WO "
				+ " left join WO_TYPE wt on WO.WO_TYPE_ID = wt.ID"
				+ " where STATE != 'OK' AND TR_ID = :trId ";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("trId", trId);
		query.addScalar("trId", new LongType());
		query.addScalar("woCode", new StringType());
		query.addScalar("woTypeName", new StringType());
		List<Object[]> lstObj = query.list();
		return lstObj;
	}
}

