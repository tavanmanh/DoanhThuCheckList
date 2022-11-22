package com.viettel.coms.dao;

import java.io.StringBufferInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.resource.spi.work.Work;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import com.viettel.cat.dto.ConstructionImageInfo;
import com.viettel.asset.dto.SysGroupDto;
import com.viettel.coms.bo.AssignHandoverBO;
import com.viettel.coms.bo.UtilAttachDocumentBO;
import com.viettel.coms.dto.AppParamDTO;
import com.viettel.coms.dto.AssignHandoverDTO;
import com.viettel.coms.dto.CatWorkItemTypeDTO;
import com.viettel.coms.dto.ConstructionDTO;
import com.viettel.coms.dto.ConstructionTaskDTO;
import com.viettel.coms.dto.EntangleManageDTO;
import com.viettel.coms.dto.GoodsPlanDTO;
import com.viettel.coms.dto.ObstructedDTO;
import com.viettel.coms.dto.RequestGoodsDetailDTO;
import com.viettel.coms.dto.SysUserCOMSDTO;
import com.viettel.coms.dto.UtilAttachDocumentDTO;
import com.viettel.coms.dto.WorkItemDTO;
import com.viettel.erp.dto.CntContractDTO;
import com.viettel.erp.dto.SysUserDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

//VietNT_20181210_created
@EnableTransactionManagement
@Transactional
@Repository("assignHandoverDAO")
public class AssignHandoverDAO extends BaseFWDAOImpl<AssignHandoverBO, Long> {

    public AssignHandoverDAO() {
        this.model = new AssignHandoverBO();
    }

    public AssignHandoverDAO(Session session) {
        this.session = session;
    }
    static Logger LOGGER = LoggerFactory.getLogger(AssignHandoverDAO.class);
    @Autowired
	private UtilAttachDocumentDAO utilAttachDocumentDAO;
    private final String PERMISSION_RECEIVE_SMS_HANDOVER = "RECEIVE SMS_HANDOVER";


    public List<AssignHandoverDTO> doSearch(AssignHandoverDTO criteria) {
        StringBuilder sql = this.createDoSearchBaseQuery();
//        hoanm1_21090710_start
        sql.append(", '' fullName ");
//        hoanm1_21090710_end
        // if listConsType exist then do left join query
        if (criteria.getListCatConstructionType() != null && !criteria.getListCatConstructionType().isEmpty()) {
            sql.append(", t.cat_construction_type_id ");
            sql.append("FROM ASSIGN_HANDOVER a ");
            sql.append("LEFT JOIN construction t on a.construction_id = t.construction_id " +
                    "WHERE 1=1 ");
            sql.append("AND t.cat_construction_type_id in (:listConsType) ");
        } else {
            sql.append("FROM ASSIGN_HANDOVER a ");
            sql.append("WHERE 1=1 ");
        }

        sql.append(" and a.SYS_GROUP_NAME is not null ");
        sql.append(" AND a.CONSTRUCTION_CODE is not null ");
        
        if (null != criteria.getStatus()) {
            sql.append("AND a.STATUS = :status ");
        }
        //query by keySearch: Mã công trình/trạm/tỉnh/kế hoạch/hợp đồng
        if (StringUtils.isNotEmpty(criteria.getKeySearch())) {
            sql.append("AND (" +
                    "upper(a.CONSTRUCTION_CODE) LIKE upper(:keySearch) " +
                    "OR upper(a.CAT_STATION_CODE) LIKE upper(:keySearch) " +
                    "OR upper(a.CAT_PROVINCE_CODE) LIKE upper(:keySearch) " +
                    "OR upper(a.CNT_CONTRACT_CODE) LIKE upper(:keySearch) escape '&') ");
        }

        //, sysGroupId, catConstructionType, companyAssignDate
//        if (null != criteria.getSysGroupId()) {
//            sql.append("AND SYS_GROUP_ID LIKE :sysGroupId ");
//        }
        if (null != criteria.getSysGroupId()) {
            sql.append("AND a.SYS_GROUP_ID LIKE :sysGroupId ");
        } else if (StringUtils.isNotEmpty(criteria.getText())) {
            sql.append("AND upper(a.SYS_GROUP_CODE || '_' || a.SYS_GROUP_NAME) like upper(:text) escape '&' ");
        }

        if (null != criteria.getDateFrom()) {
            sql.append("AND TRUNC(a.COMPANY_ASSIGN_DATE) >= :dateFrom ");
        }
        if (null != criteria.getDateTo()) {
            sql.append("AND TRUNC(a.COMPANY_ASSIGN_DATE) <= :dateTo ");
        }

        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");

        sql.append("ORDER BY assignHandoverId desc ");
        SQLQuery query = this.getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = this.getSession().createSQLQuery(sqlCount.toString());

        // set params
        if (null != criteria.getStatus()) {
            query.setParameter("status", criteria.getStatus());
            queryCount.setParameter("status", criteria.getStatus());
        }
        if (criteria.getListCatConstructionType() != null && !criteria.getListCatConstructionType().isEmpty()) {
            query.setParameterList("listConsType", criteria.getListCatConstructionType());
            queryCount.setParameterList("listConsType", criteria.getListCatConstructionType());
        }
        if (StringUtils.isNotEmpty(criteria.getKeySearch())) {
            query.setParameter("keySearch", "%" + criteria.getKeySearch() + "%");
            queryCount.setParameter("keySearch", "%" + criteria.getKeySearch() + "%");
        }
        if (null != criteria.getSysGroupId()) {
            query.setParameter("sysGroupId", criteria.getSysGroupId());
            queryCount.setParameter("sysGroupId", criteria.getSysGroupId());
        } else if (StringUtils.isNotEmpty(criteria.getText())) {
            query.setParameter("text", "%" + criteria.getText() + "%");
            queryCount.setParameter("text", "%" + criteria.getText() + "%");
        }
        if (null != criteria.getDateFrom()) {
            query.setParameter("dateFrom", criteria.getDateFrom());
            queryCount.setParameter("dateFrom", criteria.getDateFrom());
        }
        if (null != criteria.getDateTo()) {
            query.setParameter("dateTo", criteria.getDateTo());
            queryCount.setParameter("dateTo", criteria.getDateTo());
        }

        query.setResultTransformer(Transformers.aliasToBean(AssignHandoverDTO.class));
        this.addQueryScalarDoSearch(query);

        if (criteria.getPage() != null && criteria.getPageSize() != null) {
            query.setFirstResult((criteria.getPage().intValue() - 1) * criteria.getPageSize());
            query.setMaxResults(criteria.getPageSize());
        }

        criteria.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
        return query.list();
    }

    public AssignHandoverDTO findById(Long id) {
        StringBuilder sql = this.createDoSearchBaseQuery();
//      hoanm1_20190710_start
        sql.append(", '' fullName ");
//      hoanm1_20190710_end
        sql.append("FROM ASSIGN_HANDOVER a " +
                "WHERE assign_handover_id = :id");

        SQLQuery query = super.getSession().createSQLQuery(sql.toString());
        query.setParameter("id", id);
        query.setResultTransformer(Transformers.aliasToBean(AssignHandoverDTO.class));
        this.addQueryScalarDoSearch(query);

        return (AssignHandoverDTO) query.uniqueResult();
    }

    public List<AssignHandoverDTO> findByCodes(String... constructionCodes) {
        String sql = "SELECT " +
                "a.SYS_GROUP_NAME sysGroupName " +
                "FROM ASSIGN_HANDOVER a " +
                "WHERE construction_code = :code";

        SQLQuery query = super.getSession().createSQLQuery(sql);
        query.setParameterList("code", constructionCodes);
        query.setResultTransformer(Transformers.aliasToBean(AssignHandoverDTO.class));
        query.addScalar("sysGroupName", new StringType());

        return query.list();
    }

    public List<AssignHandoverDTO> findByIdList(List<Long> assignHandoverIds) {
        StringBuilder sql = this.createDoSearchBaseQuery();
//        hoanm1_20190710_start
        sql.append(", '' fullName ");
//        hoanm1_20190710_end
        sql.append("FROM ASSIGN_HANDOVER a " +
                "WHERE assign_handover_id IN (:ids)");

        SQLQuery query = super.getSession().createSQLQuery(sql.toString());
        query.setParameterList("ids", assignHandoverIds);
        query.setResultTransformer(Transformers.aliasToBean(AssignHandoverDTO.class));
        this.addQueryScalarDoSearch(query);

        return query.list();
    }

    //VietNT_20190122_start
    public List<AssignHandoverDTO> findConstructionContractRef(Long constructionId) {
        String sql = "SELECT " +
                "cnt.cnt_contract_id cntContractId, " +
                "cnt.code cntContractCode, " +
                "ct.construction_id constructionId, " +
                "ct.code constructionCode " +
                "FROM construction ct " +
                "LEFT JOIN cnt_constr_work_item_task t ON ct.construction_id = t.construction_id " +
                "LEFT JOIN cnt_contract cnt ON cnt.cnt_contract_id = t.cnt_contract_id " +
                "WHERE 1=1 " +
                "AND cnt.code IS NOT NULL " +
                "AND ct.code IS NOT NULL ";

        if (null != constructionId) {
            sql += "AND ct.construction_id = :constructionId ";
        }

        SQLQuery query = super.getSession().createSQLQuery(sql);
        if (null != constructionId) {
            query.setParameter("constructionId", constructionId);
        }

        query.addScalar("cntContractId", new LongType());
        query.addScalar("cntContractCode", new StringType());
        query.addScalar("constructionId", new LongType());
        query.addScalar("constructionCode", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(AssignHandoverDTO.class));
        return query.list();
    }

    /*
    public List<AssignHandoverDTO> findConstructionContractRef(Long constructionId) {
        String sql = "SELECT " +
                "pr.cat_province_id catProvinceId, " +
                "pr.code catProvinceCode, " +
                "cnt.cnt_contract_id cntContractId, " +
                "cnt.code cntContractCode, " +
                "ct.construction_id constructionId, " +
                "ct.code constructionCode, " +
                "ch.code catStationHouseCode, " +
                "ch.cat_station_house_id catStationHouseId, " +
                "cs.cat_station_id catStationId, " +
                "cs.code catStationCode " +
                "FROM construction ct " +
                "LEFT JOIN cat_station cs ON cs.cat_station_id = ct.cat_station_id " +
                "LEFT JOIN cat_station_house ch on ch.cat_station_house_id = cs.cat_station_house_id " +
                "LEFT JOIN cat_province pr ON pr.cat_province_id = cs.cat_province_id " +
                "LEFT JOIN cnt_constr_work_item_task t ON ct.construction_id = t.construction_id " +
                "LEFT JOIN cnt_contract cnt ON cnt.cnt_contract_id = t.cnt_contract_id " +
                "WHERE cnt.code IS NOT NULL " +
                "AND cs.code IS NOT NULL " +
                "AND cnt.code IS NOT NULL " +
                "AND ct.code IS NOT NULL " +
                "AND ch.code IS NOT NULL " +
                "AND pr.code IS NOT NULL ";

        if (null != constructionId) {
            sql += "AND ct.construction_id = :constructionId ";
        }

        SQLQuery query = super.getSession().createSQLQuery(sql);
        if (null != constructionId) {
            query.setParameter("constructionId", constructionId);
        }

        query.addScalar("catProvinceId", new LongType());
        query.addScalar("catProvinceCode", new StringType());
        query.addScalar("cntContractId", new LongType());
        query.addScalar("cntContractCode", new StringType());
        query.addScalar("constructionId", new LongType());
        query.addScalar("constructionCode", new StringType());
        query.addScalar("catStationHouseId", new LongType());
        query.addScalar("catStationHouseCode", new StringType());
        query.addScalar("catStationId", new LongType());
        query.addScalar("catStationCode", new StringType());

        query.setResultTransformer(Transformers.aliasToBean(AssignHandoverDTO.class));
        return query.list();
    }
    */
    //VietNT_end

    public List<AssignHandoverDTO> getListSysGroupCode() {
        String sql = "SELECT CODE sysGroupCode, SYS_GROUP_ID sysGroupId, NAME sysGroupName FROM SYS_GROUP";
        SQLQuery query = super.getSession().createSQLQuery(sql);
        query.addScalar("sysGroupCode", new StringType());
        query.addScalar("sysGroupName", new StringType());
        query.addScalar("sysGroupId", new LongType());
        query.setResultTransformer(Transformers.aliasToBean(AssignHandoverDTO.class));

        return query.list();
    }

    public int insertIntoRpStationComplete(AssignHandoverDTO dto) {
        //mã tỉnh, mã nhà trạm, mã hợp đồng
        String idSql = "SELECT rp_station_complete_seq.nextval FROM DUAL";
        SQLQuery idQuery = getSession().createSQLQuery(idSql);
        int id = ((BigDecimal) idQuery.uniqueResult()).intValue();
        String sql = "INSERT INTO RP_STATION_COMPLETE " +
                "(RP_STATION_COMPLETE_ID, CAT_PROVINCE_CODE, CAT_PROVINCE_ID, CAT_STATION_HOUSE_CODE, CAT_STATION_HOUSE_ID, CNT_CONTRACT_CODE, CNT_CONTRACT_ID, SYS_GROUP_ID, SYS_GROUP_NAME, SYS_GROUP_CODE ) " +
                "VALUES " +
                "(:id, :catProvinceCode, :catProvinceId, :catStationHouseCode, :catStationHouseId, :cntContractCode, :cntContractId, :sysGroupId, :sysGroupName, :sysGroupCode)";

        SQLQuery query = super.getSession().createSQLQuery(sql);
        query.setParameter("id", id);
        query.setParameter("catProvinceCode", dto.getCatProvinceCode());
        query.setParameter("catProvinceId", dto.getCatProvinceId());
        query.setParameter("catStationHouseCode", dto.getCatStationHouseCode());
        query.setParameter("catStationHouseId", dto.getCatStationHouseId());
        query.setParameter("cntContractCode", dto.getCntContractCode());
        query.setParameter("cntContractId", dto.getCntContractId());
        query.setParameter("sysGroupId", dto.getSysGroupId());
        query.setParameter("sysGroupName", dto.getSysGroupName());
        query.setParameter("sysGroupCode", dto.getSysGroupCode());

        return query.executeUpdate();
    }

    public List<SysUserDTO> findUsersReceiveMail(Long sysGroupId) {
        String sql = "SELECT a.PHONE_NUMBER phone, a.EMAIL email " +
                "FROM sys_user a, " +
                "user_role b, " +
                "sys_role c, " +
                "user_role_data d, " +
                "domain_data e, " +
                "role_permission role_per, " +
                "permission pe, " +
                "operation op, " +
                "ad_resource ad " +
                "WHERE " +
                "a.sys_user_id = b.sys_user_id " +
                "AND b.sys_role_id = c.sys_role_id " +
                "AND b.user_role_id = d.user_role_id " +
                "AND d.domain_data_id = e.domain_data_id " +
                "AND c.sys_role_id = role_per.sys_role_id " +
                "AND role_per.permission_id = pe.permission_id " +
                "AND pe.operation_id = op.operation_id " +
                "AND pe.ad_resource_id = ad.ad_resource_id " +
                "AND e.data_id = :sysGroupId " +
                "AND upper(op.code ||' ' ||ad.code) LIKE upper(:permission) escape '&'";
        SQLQuery query = super.getSession().createSQLQuery(sql);
        query.setParameter("sysGroupId", sysGroupId);
        query.setParameter("permission", "%" + PERMISSION_RECEIVE_SMS_HANDOVER + "%");

        query.addScalar("email", new StringType());
        query.addScalar("phone", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(SysUserDTO.class));

        return query.list();
    }

    public int insertIntoSendSmsEmailTable(SysUserDTO user, String subject, String content,
                                           Long createdUserId, Date createdDate,Long createdGroupId) {
        return this.insertIntoSendSmsEmailTable(subject, content, user.getEmail(), user.getPhone(), createdUserId, createdDate,createdGroupId);
    }

    public int insertIntoSendSmsEmailTable(String subject, String content, String email,
                                           String phoneNum, Long createdUserId, Date createdDate,Long createdGroupId) {
        String idSql = "SELECT SEND_SMS_EMAIL_SEQ.nextval FROM DUAL";
        SQLQuery idQuery = getSession().createSQLQuery(idSql);
        int smsId = ((BigDecimal) idQuery.uniqueResult()).intValue();

        String sql = "INSERT INTO SEND_SMS_EMAIL " +
                "(SEND_SMS_EMAIL_ID, SUBJECT, CONTENT, RECEIVE_PHONE_NUMBER, RECEIVE_EMAIL, CREATED_DATE, CREATED_USER_ID,status,CREATED_GROUP_ID) " +
                "VALUES " +
                "(:id, :subject, :content, :phoneNum, :email, :createdDate, :createdUserId,0,:createdGroupId) ";
        SQLQuery query = super.getSession().createSQLQuery(sql);
        query.setParameter("id", smsId);
        query.setParameter("subject", subject);
        query.setParameter("content", content);
        query.setParameter("email", email);
        query.setParameter("phoneNum", phoneNum);
        query.setParameter("createdUserId", createdUserId);
        query.setParameter("createdDate", createdDate);
//        hoanm1_20190528_start
        if(createdGroupId !=null){
        	query.setParameter("createdGroupId", createdGroupId);
        }else{
        	query.setParameter("createdGroupId", 0L);
        }
//        hoanm1_20190528_end
        return query.executeUpdate();
    }

    public boolean checkContractCatStationHouseExist(Long catStationHouseId, Long contractId) {
        String sql = "SELECT COUNT(1) " +
                "FROM RP_STATION_COMPLETE " +
                "WHERE " +
                "CAT_STATION_HOUSE_ID = :catStationHouseId " +
//                "AND CNT_CONSTRACT_ID = :cntContractId";
				"AND CNT_CONTRACT_ID = :cntContractId";

        SQLQuery query = super.getSession().createSQLQuery(sql);
        query.setParameter("catStationHouseId", catStationHouseId);
        query.setParameter("cntContractId", contractId);

        return ((BigDecimal) query.uniqueResult()).intValue() == 1;
    }

    public List<String> findUniqueRpStationComplete() {
//        String sql = "select upper(CAT_STATION_HOUSE_ID) || '_' || upper(CNT_CONSTRACT_ID) " + //as customField
        String sql = "select upper(CAT_STATION_HOUSE_ID) || '_' || upper(CNT_CONTRACT_ID) " + //as customField
                "FROM RP_STATION_COMPLETE";

        SQLQuery query = super.getSession().createSQLQuery(sql);
//        query.addScalar("customField", new StringType());

        return (List<String>) query.list();
    }

    private StringBuilder createDoSearchBaseQuery() {
        StringBuilder sql = new StringBuilder("SELECT " +
                "a.ASSIGN_HANDOVER_ID assignHandoverId, " +
                "a.SYS_GROUP_ID sysGroupId, " +
                "a.SYS_GROUP_CODE sysGroupCode, " +
                "a.SYS_GROUP_NAME sysGroupName, " +
                "a.CAT_PROVINCE_ID catProvinceId, " +
                "a.CAT_PROVINCE_CODE catProvinceCode, " +
                "a.CAT_STATION_HOUSE_ID catStationHouseId, " +
                "a.CAT_STATION_HOUSE_CODE catStationHouseCode, " +
                "a.CAT_STATION_ID catStationId, " +
                "a.CAT_STATION_CODE catStationCode, " +
                "a.CONSTRUCTION_ID constructionId, " +
                "a.CONSTRUCTION_CODE constructionCode, " +
                "a.CNT_CONTRACT_ID cntContractId, " +
                "a.CNT_CONTRACT_CODE cntContractCode, " +
                "a.IS_DESIGN isDesign, " +
                "a.COMPANY_ASSIGN_DATE companyAssignDate, " +
                "a.CREATE_DATE createDate, " +
                "a.CREATE_USER_ID createUserId, " +
                "a.UPDATE_DATE updateDate, " +
                "a.UPDATE_USER_ID updateUserId, " +
                "a.STATUS status, " +
                "a.PERFORMENT_ID performentId, " +
                "a.EMAIL email, " +
                "a.DEPARTMENT_ASSIGN_DATE departmentAssignDate, " +
                "a.RECEIVED_STATUS receivedStatus, " +
                "a.OUT_OF_DATE_RECEIVED outOfDateReceived, " +
                "a.OUT_OF_DATE_START_DATE outOfDateStartDate, " +
                "a.RECEIVED_OBSTRUCT_DATE receivedObstructDate, " +
                "a.RECEIVED_OBSTRUCT_CONTENT receivedObstructContent, " +
                "a.RECEIVED_GOODS_DATE receivedGoodsDate, " +
                "a.RECEIVED_GOODS_CONTENT receivedGoodsContent, " +
                "a.RECEIVED_DATE receivedDate, " +
                "a.DELIVERY_CONSTRUCTION_DATE deliveryConstructionDate, " +
                "a.PERFORMENT_CONSTRUCTION_ID performentConstructionId, " +
                "a.PERFORMENT_CONSTRUCTION_NAME performentConstructionName, " +
                "a.SUPERVISOR_CONSTRUCTION_ID supervisorConstructionId, " +
                "a.SUPERVISOR_CONSTRUCTION_NAME supervisorConstructionName, " +
                "a.STARTING_DATE startingDate, " +
                "a.CONSTRUCTION_STATUS constructionStatus, " +
                "a.COLUMN_HEIGHT columnHeight, " +
                "a.STATION_TYPE stationType, " +
                "a.NUMBER_CO numberCo, " +
                "a.HOUSE_TYPE_ID houseTypeId, " +
                "a.HOUSE_TYPE_NAME houseTypeName, " +
                "a.GROUNDING_TYPE_ID groundingTypeId, " +
                "a.GROUNDING_TYPE_NAME groundingTypeName, " +
                "a.HAVE_WORK_ITEM_NAME haveWorkItemName, " +
                "a.IS_FENCE isFence, " +
                "a.OUT_OF_DATE_CONSTRUCTION outOfDateConstruction," +
                "a.PARTNER_NAME partnerName, ");
        sql.append("a.AREA_ID areaId, ")
        .append("a.AREA_CODE areaCode, ")
        .append("a.TTKV_ASSIGN_DATE ttkvAssignDate, ")
        .append("a.IS_DELIVERED isDelivered, ")
        .append("a.AVAILABLE_COLUMNS availableColumns, ")
        .append("a.PLANT_COLUMNS plantColunms, ")
        .append("a.CABLE_IN_TANK_DRAIN cableInTankDrain, ")
        .append("a.CABLE_IN_TANK cableInTank, ")
        .append("a.HIDDEN_IMMEDIACY hiddenImmediacy, ")
        .append("a.TOTAL_LENGTH totalLength ");
//                "FROM ASSIGN_HANDOVER a");
        return sql;
    }

    private void addQueryScalarDoSearch(SQLQuery query) {
        query.addScalar("assignHandoverId", new LongType());
        query.addScalar("sysGroupId", new LongType());
        query.addScalar("sysGroupCode", new StringType());
        query.addScalar("sysGroupName", new StringType());
        query.addScalar("catProvinceId", new LongType());
        query.addScalar("catProvinceCode", new StringType());
        query.addScalar("catStationHouseId", new LongType());
        query.addScalar("catStationHouseCode", new StringType());
        query.addScalar("catStationId", new LongType());
        query.addScalar("catStationCode", new StringType());
        query.addScalar("constructionId", new LongType());
        query.addScalar("constructionCode", new StringType());
        query.addScalar("cntContractId", new LongType());
        query.addScalar("cntContractCode", new StringType());
        query.addScalar("isDesign", new LongType());
        query.addScalar("companyAssignDate", new DateType());
        query.addScalar("createDate", new DateType());
        query.addScalar("createUserId", new LongType());
        query.addScalar("updateDate", new DateType());
        query.addScalar("updateUserId", new LongType());
        query.addScalar("status", new LongType());
        query.addScalar("performentId", new LongType());
        query.addScalar("email", new StringType());
        query.addScalar("departmentAssignDate", new DateType());
        query.addScalar("receivedStatus", new LongType());
        query.addScalar("outOfDateReceived", new LongType());
        query.addScalar("outOfDateStartDate", new LongType());
        query.addScalar("receivedObstructDate", new DateType());
        query.addScalar("receivedObstructContent", new StringType());
        query.addScalar("receivedGoodsDate", new DateType());
        query.addScalar("receivedGoodsContent", new StringType());
        query.addScalar("receivedDate", new DateType());
        query.addScalar("deliveryConstructionDate", new DateType());
        query.addScalar("performentConstructionId", new LongType());
        query.addScalar("performentConstructionName", new StringType());
        query.addScalar("supervisorConstructionId", new LongType());
        query.addScalar("supervisorConstructionName", new StringType());
        query.addScalar("startingDate", new DateType());
        query.addScalar("constructionStatus", new LongType());
        query.addScalar("columnHeight", new LongType());
        query.addScalar("stationType", new LongType());
        query.addScalar("numberCo", new LongType());
        query.addScalar("houseTypeId", new LongType());
        query.addScalar("houseTypeName", new StringType());
        query.addScalar("groundingTypeId", new LongType());
        query.addScalar("groundingTypeName", new StringType());
        query.addScalar("haveWorkItemName", new StringType());
        query.addScalar("isFence", new LongType());
        query.addScalar("outOfDateConstruction", new LongType());
        query.addScalar("partnerName", new StringType());
        query.addScalar("areaId", new LongType());
        query.addScalar("areaCode", new StringType());
        query.addScalar("ttkvAssignDate", new DateType());
        query.addScalar("isDelivered", new StringType());
        query.addScalar("availableColumns", new StringType());
        query.addScalar("plantColunms", new StringType());
        query.addScalar("cableInTankDrain", new StringType());
        query.addScalar("cableInTank", new StringType());
        query.addScalar("hiddenImmediacy", new StringType());
        query.addScalar("totalLength", new StringType());
//        hoanm1_20190710_start
        query.addScalar("fullName", new StringType());
//        hoanm1_20190710_end
    }

    //VietNT_20181218_start
    @SuppressWarnings("Duplicates")
    public List<AssignHandoverDTO> doSearchNV(AssignHandoverDTO criteria, List<String> sysGroupId) {
        StringBuilder sql = this.createDoSearchBaseQuery();
        sql.append(", su.full_name fullName ");
        sql.append("FROM ASSIGN_HANDOVER a " +
                "left join sys_user su on su.sys_user_id = a.PERFORMENT_ID " +
                "LEFT JOIN UTIL_ATTACH_DOCUMENT u on a.ASSIGN_HANDOVER_ID = u.OBJECT_ID and u.type='57'");
        sql.append("WHERE 1=1 ");
        sql.append("AND a.SYS_GROUP_ID in (:sysGroupId) ");

        if (null != criteria.getStatus()) {
            sql.append("AND a.STATUS = :status ");
        }
        //query by keySearch: Mã công trình/trạm/tỉnh/kế hoạch/hợp đồng
        if (StringUtils.isNotEmpty(criteria.getKeySearch())) {
            sql.append("AND (" +
                    "upper(a.CONSTRUCTION_CODE) LIKE upper(:keySearch) " +
                    "OR upper(a.CAT_STATION_CODE) LIKE upper(:keySearch) " +
                    "OR upper(a.CAT_PROVINCE_CODE) LIKE upper(:keySearch) " +
                    "OR upper(a.CNT_CONTRACT_CODE) LIKE upper(:keySearch) escape '&') ");
        }

        if (null != criteria.getPerformentId()) {
            sql.append("AND a.PERFORMENT_ID = :performentId ");
        }
        if (null != criteria.getDateDeptFrom()) {
            sql.append("AND TRUNC(a.DEPARTMENT_ASSIGN_DATE) >= :dateDeptFrom ");
        }
        if (null != criteria.getDateDeptTo()) {
            sql.append("AND TRUNC(a.DEPARTMENT_ASSIGN_DATE) <= :dateDeptTo ");
        }
        if (null != criteria.getDateFrom()) {
            sql.append("AND TRUNC(a.COMPANY_ASSIGN_DATE) >= :dateFrom ");
        }
        if (null != criteria.getDateTo()) {
            sql.append("AND TRUNC(a.COMPANY_ASSIGN_DATE) <= :dateTo ");
        }
        if (null != criteria.getConstructionStatusList() && !criteria.getConstructionStatusList().isEmpty()) {
            sql.append("AND a.CONSTRUCTION_STATUS in (:constructionStatusList) ");
        }
        if (null != criteria.getReceivedStatusList() && !criteria.getReceivedStatusList().isEmpty()) {
            // search quá hạn nhận bgmb
            int index = criteria.getReceivedStatusList().indexOf(6L);
            if (index >= 0) {
                sql.append("AND (a.OUT_OF_DATE_RECEIVED IS NOT NULL " +
                        "AND a.OUT_OF_DATE_RECEIVED > 0) ");
                criteria.getReceivedStatusList().remove(index);
            }
            //VietNT_20190219_start
            // tim cong trình chua BGMB: performentId = null
            index = criteria.getReceivedStatusList().indexOf(0L);
            if (index >= 0) {
                sql.append("AND a.PERFORMENT_ID IS NULL ");
                criteria.getReceivedStatusList().remove(index);
            }
            //VietNT_end
            if (!criteria.getReceivedStatusList().isEmpty()) {
                sql.append("AND a.RECEIVED_STATUS in (:receivedStatusList) ");	
            }
        }
        if (null != criteria.getConstructionCodeList() && !criteria.getConstructionCodeList().isEmpty()) {
            sql.append("AND upper(a.CONSTRUCTION_CODE) IN (:constructionCodeList) ");
        }
        if (null != criteria.getOutOfDateConstruction()) {
        	if (0 == criteria.getOutOfDateConstruction()) {
        		sql.append("AND (a.OUT_OF_DATE_CONSTRUCTION = 0 OR a.OUT_OF_DATE_CONSTRUCTION IS NULL) ");
        	} else {
                sql.append("AND a.OUT_OF_DATE_CONSTRUCTION > 0 ");
        	}
        }
        if (null != criteria.getOutOfDateStartDate()) {
            if (criteria.getOutOfDateStartDate() == 0) {
                sql.append("AND (a.OUT_OF_DATE_START_DATE IS NULL OR a.OUT_OF_DATE_START_DATE = 0) ");
            } else {
                sql.append("AND a.OUT_OF_DATE_START_DATE > 0 ");
            }
        }
        if (criteria.getIsReceivedGoods()) {
            sql.append("AND a.RECEIVED_GOODS_DATE IS NOT NULL ");
        }
        if (criteria.getIsReceivedObstruct()) {
            sql.append("AND a.RECEIVED_OBSTRUCT_DATE IS NOT NULL ");
        }

        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");

        sql.append("ORDER BY assignHandoverId desc ");
        SQLQuery query = this.getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = this.getSession().createSQLQuery(sqlCount.toString());

        query.setParameterList("sysGroupId", sysGroupId);
        queryCount.setParameterList("sysGroupId", sysGroupId);

        if (null != criteria.getStatus()) {
            query.setParameter("status", criteria.getStatus());
            queryCount.setParameter("status", criteria.getStatus());
        }
        if (StringUtils.isNotEmpty(criteria.getKeySearch())) {
            query.setParameter("keySearch", "%" + criteria.getKeySearch() + "%");
            queryCount.setParameter("keySearch", "%" + criteria.getKeySearch() + "%");
        }
        if (null != criteria.getPerformentId()) {
            query.setParameter("performentId", criteria.getPerformentId());
            queryCount.setParameter("performentId", criteria.getPerformentId());
        }
        if (null != criteria.getDateDeptFrom()) {
            query.setParameter("dateDeptFrom", criteria.getDateDeptFrom());
            queryCount.setParameter("dateDeptFrom", criteria.getDateDeptFrom());
        }
        if (null != criteria.getDateDeptTo()) {
            query.setParameter("dateDeptTo", criteria.getDateDeptTo());
            queryCount.setParameter("dateDeptTo", criteria.getDateDeptTo());
        }
        if (null != criteria.getDateFrom()) {
            query.setParameter("dateFrom", criteria.getDateFrom());
            queryCount.setParameter("dateFrom", criteria.getDateFrom());
        }
        if (null != criteria.getDateTo()) {
            query.setParameter("dateTo", criteria.getDateTo());
            queryCount.setParameter("dateTo", criteria.getDateTo());
        }

        if (null != criteria.getConstructionStatusList() && !criteria.getConstructionStatusList().isEmpty()) {
            query.setParameterList("constructionStatusList", criteria.getConstructionStatusList());
            queryCount.setParameterList("constructionStatusList", criteria.getConstructionStatusList());
        }
        if (null != criteria.getReceivedStatusList() && !criteria.getReceivedStatusList().isEmpty()) {
            query.setParameterList("receivedStatusList", criteria.getReceivedStatusList());
            queryCount.setParameterList("receivedStatusList", criteria.getReceivedStatusList());
        }
        if (null != criteria.getConstructionCodeList() && !criteria.getConstructionCodeList().isEmpty()) {
            query.setParameterList("constructionCodeList", criteria.getConstructionCodeList());
            queryCount.setParameterList("constructionCodeList", criteria.getConstructionCodeList());
        }

        query.setResultTransformer(Transformers.aliasToBean(AssignHandoverDTO.class));
        this.addQueryScalarDoSearch(query);
        if (criteria.getPage() != null && criteria.getPageSize() != null) {
            query.setFirstResult((criteria.getPage().intValue() - 1) * criteria.getPageSize());
            query.setMaxResults(criteria.getPageSize());
        }

        criteria.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
        return query.list();
    }

    public List<SysUserCOMSDTO> getForSysUserAutoComplete(SysUserCOMSDTO obj) {
        StringBuilder sql = new StringBuilder("SELECT " +
                "su.SYS_USER_ID sysUserId, " +
                "su.FULL_NAME fullName, " +
                "su.EMPLOYEE_CODE employeeCode, " +
                "su.EMAIL email " +
                "FROM CTCT_VPS_OWNER.SYS_USER su " +
                "WHERE 1=1 ");

        if (StringUtils.isNotEmpty(obj.getFullName())) {
            sql.append("AND (upper(su.FULL_NAME) LIKE upper(:fullName) OR " +
                    "upper(su.EMPLOYEE_CODE) LIKE upper(:fullName) OR " +
                    "upper(su.EMAIL) like upper(:fullName) escape '&')");
        }

        sql.append(" ORDER BY su.EMPLOYEE_CODE");
        
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = this.getSession().createSQLQuery(sqlCount.toString());

        query.addScalar("sysUserId", new LongType());
        query.addScalar("fullName", new StringType());
        query.addScalar("employeeCode", new StringType());
        query.addScalar("email", new StringType());

        query.setResultTransformer(Transformers.aliasToBean(SysUserCOMSDTO.class));

        if (StringUtils.isNotEmpty(obj.getFullName())) {
            query.setParameter("fullName", "%" + obj.getFullName() + "%");
            queryCount.setParameter("fullName", "%" + obj.getFullName() + "%");
        }
        query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize());
        query.setMaxResults(obj.getPageSize());
        obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

        return query.list();
    }


    public void updateWorkItemConstructor(Long workItemId, Long constructorId) {
        String sql = " UPDATE WORK_ITEM wi SET " +
                "wi.is_internal = 2, " +
                "wi.CONSTRUCTOR_ID = :constructorId " +
                "where work_item_id = :workItemId ";

        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("constructorId", constructorId);
        query.setParameter("workItemId", workItemId);
        query.executeUpdate();
    }
    //VietNT_end
    
	 /**Hoangnh start 02042019**/
    public void saveImagePath(List<ConstructionImageInfo> lstConstructionImages,AssignHandoverDTO req) {
		if (lstConstructionImages == null || lstConstructionImages.size() == 0) {
			return;
		}
		for (ConstructionImageInfo constructionImage : lstConstructionImages) {
			LOGGER.warn("log 6:" + constructionImage.getImagePath() );
			UtilAttachDocumentBO utilAttachDocumentBO = new UtilAttachDocumentBO();
			utilAttachDocumentBO.setObjectId(req.getAssignHandoverId());//
			utilAttachDocumentBO.setName(constructionImage.getImageName());
			utilAttachDocumentBO.setType("56");
			utilAttachDocumentBO.setDescription("File ảnh BGMT tuyến");
			utilAttachDocumentBO.setStatus("1");
			utilAttachDocumentBO.setFilePath(constructionImage.getImagePath());
			utilAttachDocumentBO.setCreatedDate(new Date());
			utilAttachDocumentBO.setCreatedUserId(req.getSysUserId());
			utilAttachDocumentBO.setCreatedUserName(req.getSysUserName());
			if (constructionImage.getLongtitude() != null) {
				utilAttachDocumentBO.setLongtitude(constructionImage.getLongtitude());
			}
			if (constructionImage.getLatitude() != null) {
				utilAttachDocumentBO.setLatitude(constructionImage.getLatitude());
			}
			long ret = utilAttachDocumentDAO.saveObject(utilAttachDocumentBO);
		}
  	}
    
    public int updateAH(AssignHandoverDTO req){
    	StringBuilder sql = new StringBuilder("UPDATE ASSIGN_HANDOVER SET STATION_TYPE =:stationType ");
    	sql.append(",TOTAL_LENGTH =:totalLength ");
    	if(StringUtils.isNotBlank(req.getReceivedObstructContent())){
    		sql.append(",RECEIVED_OBSTRUCT_DATE = SYSDATE ");
        	sql.append(",RECEIVED_OBSTRUCT_CONTENT =:receivedObstructContent ");
        	sql.append(",RECEIVED_STATUS = 3 ");
    	} else {
    		sql.append(",RECEIVED_DATE = SYSDATE ");
        	sql.append(",RECEIVED_STATUS = 2 ");
    	}
    	if(req.getStationType() == 3 ){
    		sql.append(",HIDDEN_IMMEDIACY =:hiddenImmediacy ");
        	sql.append(",CABLE_IN_TANK =:cableInTank ");
        	sql.append(",CABLE_IN_TANK_DRAIN =:cableInTankDrain ");
    	} else if(req.getStationType() == 4 ){
    		sql.append(",PLANT_COLUMNS =:plantColunms ");
        	sql.append(",AVAILABLE_COLUMNS =:availableColumns ");
    	}
    	sql.append("WHERE ASSIGN_HANDOVER_ID =:assignHandoverId ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("stationType", req.getStationType());
    	query.setParameter("totalLength", req.getTotalLength());
    	if(StringUtils.isNotBlank(req.getReceivedObstructContent())){
        	query.setParameter("receivedObstructContent", req.getReceivedObstructContent());
    	}
    	if(req.getStationType() == 3){
    		query.setParameter("hiddenImmediacy", req.getHiddenImmediacy());
    		query.setParameter("cableInTank", req.getCableInTank());
    		query.setParameter("cableInTankDrain", req.getCableInTankDrain());
    	} else if(req.getStationType() == 4){
    		query.setParameter("plantColunms", req.getPlantColunms());
    		query.setParameter("availableColumns", req.getAvailableColumns());
    	}
    	query.setParameter("assignHandoverId", req.getAssignHandoverId());
    	int i = query.executeUpdate();
    	return i;
    }
    
    public void removeFile(Long objectId, String type){
    	StringBuilder sql = new StringBuilder("DELETE FROM UTIL_ATTACH_DOCUMENT WHERE OBJECT_ID =:objectId AND TYPE =:type ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("objectId", objectId);
    	query.setParameter("type", type);
    	query.executeUpdate();
    }
    
    public int updateCons(AssignHandoverDTO req){
    	StringBuilder sql = new StringBuilder("UPDATE CONSTRUCTION CO SET CO.HANDOVER_DATE_BUILD = SYSDATE ");
    	if(StringUtils.isNotBlank(req.getReceivedObstructContent())){
    		sql.append(",CO.STATUS=4 ");
    	} else {
    		sql.append(",CO.STATUS=2 ");
    		sql.append(",CO.AMOUNT =:amount ");
    	}
    	sql.append("WHERE CO.CONSTRUCTION_ID=:constructionId ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("constructionId", req.getConstructionId());
    	if(StringUtils.isBlank(req.getReceivedObstructContent())){
    		query.setParameter("amount", req.getTotalLength());
    	}
    	int i = query.executeUpdate();
    	return i;
    }
    
    @SuppressWarnings("unchecked")
	public List<WorkItemDTO> getListWorkItem(AssignHandoverDTO req){
    	StringBuilder sql = new StringBuilder("SELECT WORK_ITEM_ID workItemId,")
    	.append("STATUS status ")
    	.append("FROM WORK_ITEM WHERE STATUS !=3 AND CONSTRUCTION_ID=:constructionId ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("workItemId", new LongType())
    	.addScalar("status", new StringType());
    	query.setParameter("constructionId", req.getConstructionId());
    	query.setResultTransformer(Transformers.aliasToBean(WorkItemDTO.class));
    	return query.list();
    }
    
    public void updateWorkItem(Long workItemId){
    	StringBuilder sql = new StringBuilder("UPDATE WORK_ITEM SET STATUS=4 WHERE WORK_ITEM_ID=:workItemId ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("workItemId", workItemId);
    	query.executeUpdate();
    }
    
    public void updateRP(Long cntContractId, Long catStationHouseId){
    	StringBuilder sql = new StringBuilder("UPDATE RP_STATION_COMPLETE RP set RP.HANDOVER_DATE_BUILD= sysdate ")
    	.append("WHERE RP.CNT_CONTRACT_ID =:cntContractId ")
    	.append("AND RP.CAT_STATION_HOUSE_ID =:catStationHouseId ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("cntContractId", cntContractId)
    	.setParameter("catStationHouseId", catStationHouseId);
    	query.executeUpdate();
    }
    
    public AssignHandoverDTO getStation(Long assignHandoverId){
    	StringBuilder sql = new StringBuilder("select CNT_CONTRACT_ID cntContractId,")
    	.append("CAT_STATION_HOUSE_ID catStationHouseId ")
    	.append("from ASSIGN_HANDOVER where ASSIGN_HANDOVER_ID =:assignHandoverId ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("cntContractId", new LongType())
    	.addScalar("catStationHouseId" , new LongType());
    	query.setParameter("assignHandoverId", assignHandoverId);
    	query.setResultTransformer(Transformers.aliasToBean(AssignHandoverDTO.class));
    	return (AssignHandoverDTO) query.uniqueResult();
    }
    
    public SysUserDTO getSysUser(Long sysUserId){
    	StringBuilder sql = new StringBuilder("SELECT SYS_GROUP_ID sysGroupId FROM SYS_USER WHERE SYS_USER_ID=:sysUserId ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("sysGroupId", new LongType());
    	query.setParameter("sysUserId", sysUserId);
    	query.setResultTransformer(Transformers.aliasToBean(SysUserDTO.class));
    	return (SysUserDTO) query.uniqueResult();
    }
    
    @SuppressWarnings("unchecked")
	public List<CatWorkItemTypeDTO> getWIType(String type){
    	StringBuilder sql = new StringBuilder("SELECT CAT_WORK_ITEM_TYPE_ID catWorkItemTypeId,")
    	.append("NAME name,CODE code,CAT_WORK_ITEM_GROUP_ID catWorkItemGroupId ")
    	.append("FROM CAT_WORK_ITEM_TYPE WHERE CAT_CONSTRUCTION_TYPE_ID = 2 AND STATUS=1 ")
    	.append("AND TYPE =:type ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("catWorkItemTypeId", new LongType())
    	.addScalar("code", new StringType())
    	.addScalar("name", new StringType())
    	.addScalar("catWorkItemGroupId", new LongType() );
    	query.setParameter("type", type);
    	query.setResultTransformer(Transformers.aliasToBean(CatWorkItemTypeDTO.class));
    	return query.list();
    }
    /**Hoangnh end 02042019**/
	
    //Huypq-20190315-start
    public List<AssignHandoverDTO> checkStationContractBGMB(AssignHandoverDTO obj){
    	StringBuilder sql = new StringBuilder("SELECT PERFORMENT_ID performentId FROM ASSIGN_HANDOVER " + 
    			" where CAT_STATION_HOUSE_CODE=:houseCode " + 
    			" and CNT_CONTRACT_CODE=:contractCode"
    			+ " and PERFORMENT_ID is not null");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("performentId", new LongType());
    	
    	query.setResultTransformer(Transformers.aliasToBean(AssignHandoverDTO.class));
    	
    	query.setParameter("houseCode", obj.getCatStationHouseCode());
    	query.setParameter("contractCode", obj.getCntContractCode());
    	
    	return query.list();
    }
    
    public List<AssignHandoverDTO> checkStationBGMB(List<String> listHouse, List<String> listContract){
    	StringBuilder sql = new StringBuilder("SELECT PERFORMENT_ID performentId,CONSTRUCTION_CODE constructionCode FROM ASSIGN_HANDOVER " + 
    			" where CAT_STATION_HOUSE_CODE in (:listHouse) " + 
    			" and CNT_CONTRACT_CODE in (:listContract)"
    			+ " and PERFORMENT_ID is not null");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("performentId", new LongType());
    	query.addScalar("constructionCode", new StringType());
    	
    	query.setResultTransformer(Transformers.aliasToBean(AssignHandoverDTO.class));
    	
    	query.setParameterList("listHouse", listHouse);
    	query.setParameterList("listContract", listContract);
    	
    	return query.list();
    }
    
    public List<AssignHandoverDTO> doSearchTTKT(AssignHandoverDTO criteria, List<String> groupIdList) {
        StringBuilder sql = this.createDoSearchBaseQuery();

        //HuyPq-20190717-start
        sql.append(", '' fullName ");
        //huy-end
        
        if (criteria.getListCatConstructionType() != null && !criteria.getListCatConstructionType().isEmpty()) {
            sql.append(", t.cat_construction_type_id ");
//          hoanm1_20190710_start
//            sql.append(", '' fullName ");  //HuyPq-20190717-command
//            hoanm1_20190710_end
            sql.append(" FROM ASSIGN_HANDOVER a ");
            sql.append("LEFT JOIN construction t on a.construction_id = t.construction_id " +
                    "WHERE 1=1 ");
            sql.append("AND t.cat_construction_type_id in (:listConsType) ");
        } else {
            sql.append("FROM ASSIGN_HANDOVER a ");
            sql.append("WHERE 1=1 AND STATUS=1 ");
        }
        sql.append("AND a.AREA_ID in (:groupIdList) ");
        if (StringUtils.isNotEmpty(criteria.getIsDelivered())) {
        	if(criteria.getIsDelivered().equals("2")) {
        		sql.append("AND a.IS_DELIVERED in (0,1) ");
        	} else {
        		sql.append("AND a.IS_DELIVERED = :isDelivered ");
        	}
        }
        if (StringUtils.isNotEmpty(criteria.getKeySearch())) {
            sql.append("AND (" +
                    "upper(a.CONSTRUCTION_CODE) LIKE upper(:keySearch) " +
                    "OR upper(a.CAT_STATION_CODE) LIKE upper(:keySearch) " +
                    "OR upper(a.CAT_PROVINCE_CODE) LIKE upper(:keySearch) " +
                    "OR upper(a.CAT_STATION_HOUSE_CODE) LIKE upper(:keySearch) " +
                    "OR upper(a.CNT_CONTRACT_CODE) LIKE upper(:keySearch) escape '&') ");
        }

        if(StringUtils.isNotEmpty(criteria.getCatProvinceCode())) {
        	sql.append(" AND upper(a.CAT_PROVINCE_CODE) LIKE upper(:provinceCode) escape '&' ");
        }
        
        if (null != criteria.getSysGroupId()) {
            sql.append(" AND a.AREA_ID = :sysGroupId ");
        } 

        if (null != criteria.getDateFrom()) {
            sql.append(" AND TRUNC(a.Company_assign_date) >= :dateFrom ");
        }
        if (null != criteria.getDateTo()) {
            sql.append(" AND TRUNC(a.Company_assign_date) <= :dateTo ");
        }

        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");

        sql.append("ORDER BY assignHandoverId desc ");
        SQLQuery query = this.getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = this.getSession().createSQLQuery(sqlCount.toString());

        // set params
        if (StringUtils.isNotEmpty(criteria.getIsDelivered())) {
        	if(!criteria.getIsDelivered().equals("2")) {
	            query.setParameter("isDelivered", criteria.getIsDelivered());
	            queryCount.setParameter("isDelivered", criteria.getIsDelivered());
        	}
        }
        if (criteria.getListCatConstructionType() != null && !criteria.getListCatConstructionType().isEmpty()) {
            query.setParameterList("listConsType", criteria.getListCatConstructionType());
            queryCount.setParameterList("listConsType", criteria.getListCatConstructionType());
        }
        if (StringUtils.isNotEmpty(criteria.getKeySearch())) {
            query.setParameter("keySearch", "%" + criteria.getKeySearch() + "%");
            queryCount.setParameter("keySearch", "%" + criteria.getKeySearch() + "%");
        }
        
        if(StringUtils.isNotEmpty(criteria.getCatProvinceCode())) {
        	 query.setParameter("provinceCode", "%" + criteria.getCatProvinceCode() + "%");
             queryCount.setParameter("provinceCode", "%" + criteria.getCatProvinceCode() + "%");
        }
        
        if (null != criteria.getSysGroupId()) {
            query.setParameter("sysGroupId", criteria.getSysGroupId());
            queryCount.setParameter("sysGroupId", criteria.getSysGroupId());
        } 
        if (null != criteria.getDateFrom()) {
            query.setParameter("dateFrom", criteria.getDateFrom());
            queryCount.setParameter("dateFrom", criteria.getDateFrom());
        }
        if (null != criteria.getDateTo()) {
            query.setParameter("dateTo", criteria.getDateTo());
            queryCount.setParameter("dateTo", criteria.getDateTo());
        }

        query.setParameterList("groupIdList", groupIdList);
        queryCount.setParameterList("groupIdList", groupIdList);
        
        query.setResultTransformer(Transformers.aliasToBean(AssignHandoverDTO.class));
        this.addQueryScalarDoSearch(query);
        
        if (criteria.getPage() != null && criteria.getPageSize() != null) {
            query.setFirstResult((criteria.getPage().intValue() - 1) * criteria.getPageSize());
            query.setMaxResults(criteria.getPageSize());
        }

        criteria.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
        return query.list();
    }
    
    public int updateSysGroupInAssignHandover(AssignHandoverDTO obj) {
    	StringBuilder sql = new StringBuilder("UPDATE ASSIGN_HANDOVER set SYS_GROUP_ID=:groupId,"
    			+ " SYS_GROUP_CODE=:code,"
    			+ " SYS_GROUP_NAME=:name,"
    			+ " TTKV_ASSIGN_DATE = sysdate,"
    			+ "IS_DELIVERED = 1 "
    			+ " where ASSIGN_HANDOVER_ID =:lstAssignId ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("lstAssignId", obj.getAssignHandoverId());
    	query.setParameter("groupId", obj.getSysGroupId());
    	query.setParameter("code", obj.getSysGroupCode());
    	query.setParameter("name", obj.getSysGroupName());
    	
    	return query.executeUpdate();
    }
    
    public int removeAssignById(Long id) {
    	StringBuilder sql = new StringBuilder("DELETE FROM ASSIGN_HANDOVER WHERE ASSIGN_HANDOVER_ID=:id ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("id", id);
    	return query.executeUpdate();
    }
    
    public UtilAttachDocumentDTO getFileByAssignHandoverId(Long id) {
    	StringBuilder sql = new StringBuilder("Select UTIL_ATTACH_DOCUMENT_ID utilAttachDocumentId,"+
    			" OBJECT_ID objectId," + 
    			" TYPE type," + 
    			" APP_PARAM_CODE appParamCode," + 
    			" CODE code," + 
    			" NAME name," + 
    			" ENCRYT_NAME encrytName," + 
    			" DESCRIPTION description," + 
    			" STATUS status," + 
    			" FILE_PATH filePath," + 
    			" CREATED_DATE createdDate," + 
    			" CREATED_USER_ID createdUserId," + 
    			" CREATED_USER_NAME createdUserName" +
    			" FROM UTIL_ATTACH_DOCUMENT " +
    			" WHERE OBJECT_ID =:id AND TYPE='57' ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("utilAttachDocumentId", new LongType());
    	query.addScalar("objectId", new LongType());
    	query.addScalar("type", new StringType());
    	query.addScalar("appParamCode", new StringType());
    	query.addScalar("code", new StringType());
    	query.addScalar("name", new StringType());
    	query.addScalar("encrytName", new StringType());
    	query.addScalar("description", new StringType());
    	query.addScalar("status", new StringType());
    	query.addScalar("filePath", new StringType());
    	query.addScalar("createdDate", new DateType());
    	query.addScalar("createdUserId", new LongType());
    	query.addScalar("createdUserName", new StringType());
    	
    	query.setResultTransformer(Transformers.aliasToBean(UtilAttachDocumentDTO.class));
    	
    	query.setParameter("id", id);
    	
    	@SuppressWarnings("unchecked")
        List<UtilAttachDocumentDTO> lst = query.list();
        if (lst.size() > 0) {
            return lst.get(0);
        }
        return null;
    	
    }
    
    public List<AssignHandoverDTO> getCheckDataWorkItem(AssignHandoverDTO obj){
    	StringBuilder sql = new StringBuilder("SELECT \r\n" + 
    			"  csh.code catStationHouseCode,\r\n" + 
    			"  cs.code catStationCode,\r\n" + 
    			"  cons.code constructionCode,\r\n" + 
    			"  cc.CODE cntContractCode\r\n" + 
    			"FROM CONSTRUCTION cons\r\n" + 
    			"INNER JOIN CTCT_CAT_OWNER.CAT_STATION cs\r\n" + 
    			"ON cs.CAT_STATION_ID = cons.CAT_STATION_ID\r\n" + 
    			"INNER JOIN CTCT_CAT_OWNER.CAT_STATION_HOUSE csh\r\n" + 
    			"ON csh.CAT_STATION_HOUSE_ID = cs.CAT_STATION_HOUSE_ID\r\n" + 
    			"INNER JOIN WORK_ITEM wi\r\n" + 
    			"ON wi.CONSTRUCTION_ID = cons.CONSTRUCTION_ID\r\n" + 
    			"INNER JOIN CTCT_IMS_OWNER.CNT_CONSTR_WORK_ITEM_TASK ccwit\r\n" + 
    			"ON ccwit.CONSTRUCTION_ID = cons.CONSTRUCTION_ID\r\n" + 
    			"INNER JOIN CTCT_IMS_OWNER.CNT_CONTRACT cc\r\n" + 
    			"ON cc.CNT_CONTRACT_ID = ccwit.CNT_CONTRACT_ID\r\n" + 
    			"WHERE wi.STATUS       =3\r\n" + 
    			"and cons.code=:constructionCode\r\n" + 
    			"and cc.code =:cntContractCode\r\n" + 
    			"and csh.code =:catStationHouseCode ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("catStationHouseCode", new StringType());
    	query.addScalar("catStationCode", new StringType());
    	query.addScalar("constructionCode", new StringType());
    	query.addScalar("cntContractCode", new StringType());
    	
    	query.setResultTransformer(Transformers.aliasToBean(AssignHandoverDTO.class));
    	
    	query.setParameter("constructionCode", obj.getConstructionCode());
    	query.setParameter("cntContractCode", obj.getCntContractCode());
    	query.setParameter("catStationHouseCode", obj.getCatStationHouseCode());
    	
    	return query.list();
    }
    
    public void deleteDataRpStation(AssignHandoverDTO obj) {
    	StringBuilder sql = new StringBuilder("DELETE FROM RP_STATION_COMPLETE "
    			+ " where CAT_STATION_HOUSE_CODE=:houseCode "
    			+ " and CNT_CONTRACT_CODE =:contractCode");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("houseCode", obj.getCatStationHouseCode());
    	query.setParameter("contractCode", obj.getCntContractCode());
    	
    	query.executeUpdate();
    }
    
    public List<WorkItemDTO> getWorkItemByStationHouseCode(AssignHandoverDTO obj) {
    	StringBuilder sql = new StringBuilder("SELECT "); 
    		 sql.append("wi.AMOUNT amount, ")
    			.append("wi.APPROVE_DATE approveDate, ")
    			.append("wi.APPROVE_DESCRIPTION approveDescription, ")
    			.append("wi.APPROVE_QUANTITY approveQuantity, ")
    			.append("wi.APPROVE_STATE approveState, ")
    			.append("wi.APPROVE_USER_ID approveUserId, ")
    			.append("wi.CAT_WORK_ITEM_GROUP_ID catWorkItemGroupId, ")
    			.append("wi.CAT_WORK_ITEM_TYPE_ID catWorkItemTypeId, ")
    			.append("wi.CODE code, ")
    			.append("wi.COMPLETE_DATE completeDate, ")
    			.append("wi.CONSTRUCTION_ID constructionId, ")
    			.append("wi.CONSTRUCTOR_ID constructorId, ")
    			.append("wi.CREATED_DATE createdDate, ")
    			.append("wi.CREATED_GROUP_ID createdGroupId, ")
    			.append("wi.CREATED_USER_ID createdUserId, ")
//    			.append("wi.EXPECT_QUANTITY expectQuantity, ")
//    			.append("wi.HSHC_WORK_ITEM_STATUS hshcWorkItemStatus, ")
    			.append("wi.IS_INTERNAL isInternal, ")
    			.append("wi.NAME name, ")
    			.append("wi.PERFORMER_ID performerId, ")
    			.append("wi.PRICE price, ")
    			.append("wi.PRICE_CHEST priceChest, ")
    			.append("wi.PRICE_GATE priceGate, ")
    			.append("wi.QUANTITY quantity, ")
    			.append("wi.STARTING_DATE startingDate, ")
    			.append("wi.STATUS status, ")
    			.append("wi.SUPERVISOR_ID supervisorId, ")
    			.append("wi.TOTAL_AMOUNT_CHEST totalAmountChest, ")
    			.append("wi.TOTAL_AMOUNT_GATE totalAmountGate, ")
    			.append("wi.UPDATED_DATE updatedDate, ")
    			.append("wi.UPDATED_GROUP_ID updatedGroupId, ")
    			.append("wi.UPDATED_USER_ID updatedUserId, ")
    			.append("wi.WORK_ITEM_ID workItemId ")
    			.append("FROM WORK_ITEM wi ")
    			.append("inner JOIN CONSTRUCTION cons " )
    			.append("ON wi.CONSTRUCTION_ID = cons.CONSTRUCTION_ID ")
    			.append("inner JOIN CTCT_CAT_OWNER.CAT_STATION cs ")
    			.append("ON cs.CAT_STATION_ID = cons.CAT_STATION_ID ") 
    			.append("inner JOIN CTCT_CAT_OWNER.CAT_STATION_HOUSE csh " )
    			.append("ON csh.CAT_STATION_HOUSE_ID = cs.CAT_STATION_HOUSE_ID " )
    			.append("inner JOIN CTCT_IMS_OWNER.CNT_CONSTR_WORK_ITEM_TASK ccwit " ) 
    			.append("ON ccwit.CONSTRUCTION_ID = cons.CONSTRUCTION_ID " )
    			.append("inner JOIN CTCT_IMS_OWNER.CNT_CONTRACT cc " )
    			.append("ON cc.CNT_CONTRACT_ID = ccwit.CNT_CONTRACT_ID " )
    			.append("WHERE 1=1 " )
    			.append("and wi.CONSTRUCTION_ID=:consId " ) 
    			.append("and cc.code =:contractCode " )
    			.append("and csh.code = :houseCode  ");
    		 SQLQuery query = getSession().createSQLQuery(sql.toString());
    		 query.addScalar("amount", new DoubleType());
    		 query.addScalar("approveDate", new DateType());
    		 query.addScalar("approveDescription", new StringType());
    		 query.addScalar("approveQuantity", new DoubleType());
    		 query.addScalar("approveState", new StringType());
    		 query.addScalar("approveUserId", new LongType());
    		 query.addScalar("catWorkItemGroupId", new LongType());
    		 query.addScalar("catWorkItemTypeId", new LongType());
    		 query.addScalar("code", new StringType());
    		 query.addScalar("completeDate", new DateType());
    		 query.addScalar("constructionId", new LongType());
    		 query.addScalar("constructorId", new LongType());
    		 query.addScalar("createdDate", new DateType());
    		 query.addScalar("createdGroupId", new LongType());
    		 query.addScalar("createdUserId", new LongType());
//    		 query.addScalar("expectQuantity", new DoubleType());
//    		 query.addScalar("hshcWorkItemStatus", new StringType());
    		 query.addScalar("isInternal", new StringType());
    		 query.addScalar("name", new StringType());
    		 query.addScalar("performerId", new LongType());
    		 query.addScalar("price", new DoubleType());
    		 query.addScalar("priceChest", new LongType());
    		 query.addScalar("priceGate", new LongType());
    		 query.addScalar("quantity", new DoubleType());
    		 query.addScalar("startingDate", new DateType());
    		 query.addScalar("status", new StringType());
    		 query.addScalar("supervisorId", new LongType());
    		 query.addScalar("totalAmountChest", new LongType());
    		 query.addScalar("totalAmountGate", new LongType());
    		 query.addScalar("updatedDate", new DateType());
    		 query.addScalar("updatedGroupId", new LongType());
    		 query.addScalar("updatedUserId", new LongType());
    		 query.addScalar("workItemId", new LongType());
    		 
    		 query.setResultTransformer(Transformers.aliasToBean(WorkItemDTO.class));
    		 
    		 query.setParameter("consId", obj.getConstructionId());
    		 query.setParameter("contractCode", obj.getCntContractCode());
    		 query.setParameter("houseCode", obj.getCatStationHouseCode());
    		 
    		 return query.list();
    }
    
    public List<ConstructionTaskDTO> getConsTaskByConsId(AssignHandoverDTO obj){
    	StringBuilder sql = new StringBuilder("SELECT ");
    	sql.append("ct.CONSTRUCTION_TASK_ID constructionTaskId, ")
    	.append("ct.SYS_GROUP_ID sysGroupId, ")
    	.append("ct.MONTH month, ")
    	.append("ct.YEAR year, ")
    	.append("ct.TASK_NAME taskName, ")
    	.append("ct.START_DATE startDate, ")
    	.append("ct.END_DATE endDate, ")
    	.append("ct.BASELINE_START_DATE baselineStartDate, ")
    	.append("ct.BASELINE_END_DATE baselineEndDate, ")
    	.append("ct.CONSTRUCTION_ID constructionId, ")
    	.append("ct.WORK_ITEM_ID workItemId, ")
    	.append("ct.CAT_TASK_ID catTaskId, ")
    	.append("ct.PERFORMER_ID performerId, ")
    	.append("ct.QUANTITY quantity, ")
    	.append("ct.COMPLETE_PERCENT completePercent, ")
    	.append("ct.DESCRIPTION description, ")
    	.append("ct.STATUS status, ")
    	.append("ct.SOURCE_TYPE sourceType, ")
    	.append("ct.DEPLOY_TYPE deployType, ")
    	.append("ct.TYPE type, ")
    	.append("ct.VAT vat, ")
    	.append("ct.DETAIL_MONTH_PLAN_ID detailMonthPlanId, ")
    	.append("ct.CREATED_DATE createdDate, ")
    	.append("ct.CREATED_USER_ID createdUserId, ")
    	.append("ct.CREATED_GROUP_ID createdGroupId, ")
    	.append("ct.UPDATED_DATE updatedDate, ")
    	.append("ct.UPDATED_USER_ID updatedUserId, ")
    	.append("ct.UPDATED_GROUP_ID updatedGroupId, ")
    	.append("ct.COMPLETE_STATE completeState, ")
    	.append("ct.PERFORMER_WORK_ITEM_ID performerWorkItemId, ")
    	.append("ct.SUPERVISOR_ID supervisorId, ")
    	.append("ct.DIRECTOR_ID directorId, ")
    	.append("ct.PARENT_ID parentId, ")
    	.append("ct.LEVEL_ID levelId, ")
    	.append("ct.PATH path, ")
    	.append("ct.REASON_STOP reasonStop, ")
    	.append("ct.TASK_ORDER taskOrder, ")
    	.append("ct.TASK_NAME_BK taskNameBk, ")
    	.append("ct.SWITCH_JOB switchJob, ")
    	.append("ct.WORK_ITEM_TYPE workItemType, ")
    	.append("ct.WORK_ITEM_NAME_HSHC workItemNameHSHC, ")
    	.append("ct.CHECK_STOCK checkStock ")
    	.append("FROM CONSTRUCTION_TASK ct ") 
    	.append("inner JOIN CONSTRUCTION cons ") 
    	.append("ON ct.CONSTRUCTION_ID = cons.CONSTRUCTION_ID ") 
    	.append("inner JOIN CTCT_CAT_OWNER.CAT_STATION cs ") 
    	.append("ON cs.CAT_STATION_ID = cons.CAT_STATION_ID ") 
    	.append("inner JOIN CTCT_CAT_OWNER.CAT_STATION_HOUSE csh ") 
    	.append("ON csh.CAT_STATION_HOUSE_ID = cs.CAT_STATION_HOUSE_ID ") 
    	.append("inner JOIN CTCT_IMS_OWNER.CNT_CONSTR_WORK_ITEM_TASK ccwit ") 
    	.append("ON ccwit.CONSTRUCTION_ID = cons.CONSTRUCTION_ID ") 
    	.append("inner JOIN CTCT_IMS_OWNER.CNT_CONTRACT cc ") 
    	.append("ON cc.CNT_CONTRACT_ID = ccwit.CNT_CONTRACT_ID ") 
    	.append("WHERE cons.CONSTRUCTION_ID=:consId ") 
    	.append("and cc.code =:contractCode ") 
    	.append("and csh.code = :houseCode ");
    	
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	
    	query.addScalar("constructionTaskId", new LongType());
    	query.addScalar("sysGroupId", new LongType());
    	query.addScalar("month", new LongType());
    	query.addScalar("year", new LongType());
    	query.addScalar("taskName", new StringType());
    	query.addScalar("startDate", new DateType());
    	query.addScalar("endDate", new DateType());
    	query.addScalar("baselineStartDate", new DateType());
    	query.addScalar("baselineEndDate", new DateType());
    	query.addScalar("constructionId", new LongType());
    	query.addScalar("workItemId", new LongType());
    	query.addScalar("catTaskId", new LongType());
    	query.addScalar("performerId", new LongType());
    	query.addScalar("quantity", new DoubleType());
    	query.addScalar("completePercent", new DoubleType());
    	query.addScalar("description", new StringType());
    	query.addScalar("status", new StringType());
    	query.addScalar("sourceType", new StringType());
    	query.addScalar("deployType", new StringType());
    	query.addScalar("type", new StringType());
    	query.addScalar("vat", new DoubleType());
    	query.addScalar("detailMonthPlanId", new LongType());
    	query.addScalar("createdDate", new DateType());
    	query.addScalar("createdUserId", new LongType());
    	query.addScalar("createdGroupId", new LongType());
    	query.addScalar("updatedDate", new DateType());
    	query.addScalar("updatedUserId", new LongType());
    	query.addScalar("updatedGroupId", new LongType());
    	query.addScalar("completeState", new StringType());
    	query.addScalar("performerWorkItemId", new LongType());
    	query.addScalar("supervisorId", new DoubleType());
    	query.addScalar("directorId", new DoubleType());
    	query.addScalar("parentId", new LongType());
    	query.addScalar("levelId", new LongType());
    	query.addScalar("path", new StringType());
    	query.addScalar("reasonStop", new StringType());
    	query.addScalar("taskOrder", new StringType());
    	query.addScalar("taskNameBk", new StringType());
    	query.addScalar("switchJob", new LongType());
    	query.addScalar("workItemType", new LongType());
    	query.addScalar("workItemNameHSHC", new StringType());
    	query.addScalar("checkStock", new LongType());
    	
    	query.setResultTransformer(Transformers.aliasToBean(ConstructionTaskDTO.class));
    	
    	query.setParameter("consId", obj.getConstructionId());
		query.setParameter("contractCode", obj.getCntContractCode());
		query.setParameter("houseCode", obj.getCatStationHouseCode());
		 
		return query.list();
    }
    
    public List<AssignHandoverDTO> checkGroupList(List<String> lstGroupId, Long id){
    	StringBuilder sql = new StringBuilder("SELECT ASSIGN_HANDOVER_ID assignHandoverId, CONSTRUCTION_CODE constructionCode "
    			+ " FROM ASSIGN_HANDOVER where ASSIGN_HANDOVER_ID=:id "
    			+ " AND SYS_GROUP_ID IN (:lstId) ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("assignHandoverId", new LongType());
    	query.addScalar("constructionCode", new StringType());
    	query.setResultTransformer(Transformers.aliasToBean(AssignHandoverDTO.class));
    	query.setParameter("id", id);
    	query.setParameterList("lstId", lstGroupId);
    	return query.list();
    }
    
    public List<AssignHandoverDTO> checkStationContract(AssignHandoverDTO obj){
    	StringBuilder sql = new StringBuilder("SELECT ASSIGN_HANDOVER_ID assignHandoverId " + 
    			" FROM ASSIGN_HANDOVER "
    			+ " WHERE CAT_STATION_HOUSE_CODE=:houseCode"
    			+ " AND CNT_CONTRACT_CODE=:contractCode");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("assignHandoverId", new LongType());
    	query.setResultTransformer(Transformers.aliasToBean(AssignHandoverDTO.class));
    	query.setParameter("houseCode", obj.getCatStationHouseCode());
    	query.setParameter("contractCode", obj.getCntContractCode());
    	
    	return query.list();
    }
    
    public AssignHandoverDTO getAssignByConsCode(String code) {
    	StringBuilder sql = new StringBuilder("SELECT ASSIGN_HANDOVER_ID assignHandoverId, "
    			+ "CAT_STATION_HOUSE_CODE catStationHouseCode, "
    			+ "CNT_CONTRACT_ID cntContractCode "
    			+ " FROM ASSIGN_HANDOVER "
    			+ " WHERE CONSTRUCTION_CODE=:code and status!=0");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("assignHandoverId", new LongType());
    	query.addScalar("catStationHouseCode", new StringType());
    	query.addScalar("cntContractCode", new StringType());
    	
    	query.setResultTransformer(Transformers.aliasToBean(AssignHandoverDTO.class));
    	
    	query.setParameter("code", code);
    	
    	@SuppressWarnings("unchecked")
        List<AssignHandoverDTO> lst = query.list();
        if (lst.size() > 0) {
            return lst.get(0);
        }
        return null;
    }
    
    public List<AssignHandoverDTO> getHouseType(AssignHandoverDTO obj){
    	StringBuilder sql = new StringBuilder("SElect ");
    	sql.append("APP_PARAM_ID houseTypeId, ")
//    	.append("CODE code, ")
    	.append("NAME houseTypeName ")
//    	.append("PAR_TYPE parType, ")
//    	.append("PAR_ORDER parOrder, ")
//    	.append("STATUS status ")
//    	.append("DESCRIPTION description, ")
//    	.append("CREATED_BY createdBy, ")
//    	.append("CREATED_DATE createdDate, ")
//    	.append("UPDATED_BY updatedBy, ")
//    	.append("UPDATED_DATE updatedDate ")
    	.append(" FROM APP_PARAM ")
    	.append(" WHERE PAR_TYPE='HOUSE_TYPE' ")
    	.append(" ORDER BY PAR_ORDER ");
    	
    	StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = this.getSession().createSQLQuery(sqlCount.toString());
    	
    	query.addScalar("houseTypeId", new LongType());
//    	query.addScalar("code", new StringType());
    	query.addScalar("houseTypeName", new StringType());
//    	query.addScalar("parType", new StringType());
//    	query.addScalar("parOrder", new StringType());
//    	query.addScalar("status", new StringType());
//    	query.addScalar("description", new StringType());
//    	query.addScalar("createdBy", new LongType());
//    	query.addScalar("createdDate", new DateType());
//    	query.addScalar("updatedBy", new LongType());
//    	query.addScalar("updatedDate", new DateType());
    	
    	query.setResultTransformer(Transformers.aliasToBean(AssignHandoverDTO.class));
    	
    	if (obj.getPage() != null && obj.getPageSize() != null) {
            query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize());
            query.setMaxResults(obj.getPageSize());
        }

    	obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
    	
    	return query.list();
    	
    }
    
    public List<AssignHandoverDTO> getGroundingType(AssignHandoverDTO obj){
    	StringBuilder sql = new StringBuilder("Select ");
    	sql.append("APP_PARAM_ID groundingTypeId, ")
//    	.append("CODE code, ")
    	.append("NAME groundingTypeName ")
//    	.append("PAR_TYPE parType, ")
//    	.append("PAR_ORDER parOrder, ")
//    	.append("STATUS status ")
//    	.append("DESCRIPTION description, ")
//    	.append("CREATED_BY createdBy, ")
//    	.append("CREATED_DATE createdDate, ")
//    	.append("UPDATED_BY updatedBy, ")
//    	.append("UPDATED_DATE updatedDate ")
    	.append(" FROM APP_PARAM ")
    	.append(" WHERE PAR_TYPE='GROUNDING_TYPE' ")
    	.append(" ORDER BY PAR_ORDER ");
    	
    	StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = this.getSession().createSQLQuery(sqlCount.toString());
    	
    	query.addScalar("groundingTypeId", new LongType());
//    	query.addScalar("code", new StringType());
    	query.addScalar("groundingTypeName", new StringType());
//    	query.addScalar("parType", new StringType());
//    	query.addScalar("parOrder", new StringType());
//    	query.addScalar("status", new StringType());
//    	query.addScalar("description", new StringType());
//    	query.addScalar("createdBy", new LongType());
//    	query.addScalar("createdDate", new DateType());
//    	query.addScalar("updatedBy", new LongType());
//    	query.addScalar("updatedDate", new DateType());
    	
    	query.setResultTransformer(Transformers.aliasToBean(AssignHandoverDTO.class));
    	
    	if (obj.getPage() != null && obj.getPageSize() != null) {
            query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize());
            query.setMaxResults(obj.getPageSize());
        }

    	obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
    	
    	return query.list();
    }
    
    public int updateRpStationCompleteByHouseId(AssignHandoverDTO obj) {
    	StringBuilder sql = new StringBuilder("update rp_station_complete set "
    			+ " column_height=:height,"
    			+ " station_type=:type,"
    			+ " number_co=:number,"
    			+ " house_type_name=:name"
    			+ " where cat_station_house_Code=:code"
    			+ " and cnt_contract_Code=:contractCode ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("height", obj.getColumnHeight());
    	query.setParameter("type", obj.getStationType());
    	query.setParameter("number", obj.getNumberCo());
    	query.setParameter("name", obj.getHouseTypeName());
    	query.setParameter("code", obj.getCatStationHouseCode());
    	query.setParameter("contractCode", obj.getCntContractCode());
    	return query.executeUpdate();
    }
    
    public List<ConstructionTaskDTO> findConstructionTaskByConsId(AssignHandoverDTO obj){
    	StringBuilder sql = new StringBuilder("SELECT ct.CONSTRUCTION_TASK_ID constructionTaskId"
    			+ " FROM CONSTRUCTION_TASK ct " + 
    			"inner join CONSTRUCTION cons on ct.CONSTRUCTION_ID = cons.CONSTRUCTION_ID " + 
    			"inner join WORK_ITEM wi on ct.WORK_ITEM_ID = wi.WORK_ITEM_ID " + 
    			"where ct.LEVEL_ID=4 " + 
    			"and wi.STATUS=4 " + 
    			"and cons.STATUS=4 " + 
    			"and cons.CONSTRUCTION_ID=:id");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("constructionTaskId", new LongType());
    	
    	query.setResultTransformer(Transformers.aliasToBean(ConstructionTaskDTO.class));
    	
    	query.setParameter("id", obj.getConstructionId());
    	
    	return query.list();
    }
    
    public List<ConstructionTaskDTO> findConstructionTaskByConsIdVtmd(AssignHandoverDTO obj){
    	StringBuilder sql = new StringBuilder("SELECT ct.CONSTRUCTION_TASK_ID constructionTaskId"
    			+ " FROM CONSTRUCTION_TASK ct " + 
    			"inner join CONSTRUCTION cons on ct.CONSTRUCTION_ID = cons.CONSTRUCTION_ID " + 
    			"inner join WORK_ITEM wi on ct.WORK_ITEM_ID = wi.WORK_ITEM_ID " + 
    			"where ct.LEVEL_ID=4 " + 
    			"and wi.STATUS=1 " + 
    			"and cons.STATUS=2 " + 
    			"and cons.CONSTRUCTION_ID=:id");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("constructionTaskId", new LongType());
    	
    	query.setResultTransformer(Transformers.aliasToBean(ConstructionTaskDTO.class));
    	
    	query.setParameter("id", obj.getConstructionId());
    	
    	return query.list();
    }
    
    public List<GoodsPlanDTO> findGoodsPlanByConsId(AssignHandoverDTO obj){
    	StringBuilder sql = new StringBuilder("SELECT gp.SIGN_STATE signState, gpd.GOODS_PLAN_ID goodsPlanId, gpd.GOODS_PLAN_DETAIL_ID goodsPlanDetailId " + 
    			"FROM GOODS_PLAN gp " + 
    			"LEFT JOIN GOODS_PLAN_DETAIL gpd " + 
    			"ON gpd.GOODS_PLAN_ID     = gp.GOODS_PLAN_ID " + 
    			"WHERE gpd.CONSTRUCTION_ID=:id");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	
    	query.addScalar("signState", new StringType());
    	query.addScalar("goodsPlanId", new LongType());
    	query.addScalar("goodsPlanDetailId", new LongType());
    	
    	query.setResultTransformer(Transformers.aliasToBean(GoodsPlanDTO.class));
    	
    	query.setParameter("id", obj.getConstructionId());
    	
    	return query.list();
    }
    
    public List<RequestGoodsDetailDTO> findRequestGoods(AssignHandoverDTO obj){
    	StringBuilder sql = new StringBuilder("SELECT rgd.REQUEST_GOODS_DETAIL_ID requestGoodsDetailId,\r\n" + 
    			"  rg.REQUEST_GOODS_ID requestGoodsId\r\n" + 
    			"FROM REQUEST_GOODS_DETAIL rgd\r\n" + 
    			"inner JOIN REQUEST_GOODS rg\r\n" + 
    			"ON rg.REQUEST_GOODS_ID  = rgd.REQUEST_GOODS_ID\r\n" + 
    			"WHERE rg.CONSTRUCTION_ID=:id");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("requestGoodsDetailId", new LongType());
    	query.addScalar("requestGoodsId", new LongType());
    	
    	query.setResultTransformer(Transformers.aliasToBean(RequestGoodsDetailDTO.class));
    	
    	query.setParameter("id", obj.getConstructionId());
    	
    	return query.list();
    }
    
    public void deleteRequestGoodsByConsId(Long id) {
    	StringBuilder sql = new StringBuilder("DELETE FROM REQUEST_GOODS WHERE REQUEST_GOODS_ID=:id");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("id", id);
    	query.executeUpdate();
    }
    
    public void deleteRequestGoodsDetailByConsId(Long id) {
    	StringBuilder sql = new StringBuilder("DELETE FROM REQUEST_GOODS WHERE REQUEST_GOODS_ID=:id");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("id", id);
    	query.executeUpdate();
    }
    
    public void deleteGoodsPlanDetailByConsId(Long id) {
    	StringBuilder sql = new StringBuilder("DELETE FROM GOODS_PLAN_DETAIL WHERE GOODS_PLAN_DETAIL_ID=:id");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("id", id);
    	query.executeUpdate();
    }
    
    public long updateAssignHandoverVuong(AssignHandoverDTO obj) {
    	StringBuilder sql = new StringBuilder("Update Assign_Handover set Received_obstruct_date = sysdate,  "
    			+ " Received_obstruct_content=:content, Received_status = 3,Received_goods_date=null,"
    			+ " Received_goods_content=null,RECEIVED_DATE=null "
    			+ " where Assign_Handover_id=:id");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("content", obj.getReceivedObstructContent());
    	query.setParameter("id", obj.getAssignHandoverId());
    	
    	return query.executeUpdate();
    }
    
    public long updateAssignHandoverVtmd(AssignHandoverDTO obj) {
    	StringBuilder sql = new StringBuilder("Update Assign_Handover set Received_goods_date = sysdate,  "
    			+ " Received_goods_content=:content, Received_status = 5, Received_obstruct_date=null,"
    			+ " Received_obstruct_content=null,RECEIVED_DATE=null "
    			+ " where Assign_Handover_id=:id");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("content", obj.getReceivedGoodsContent());
    	query.setParameter("id", obj.getAssignHandoverId());
    	
    	return query.executeUpdate();
    }
    
    public long updateAssignHandoverVuongVtmd(AssignHandoverDTO obj) {
    	StringBuilder sql = new StringBuilder("Update Assign_Handover set Received_obstruct_date = sysdate,"
    			+ " Received_obstruct_content=:obsContent,  "
    			+ " Received_goods_date = sysdate,"
    			+ " Received_goods_content=:goodsContent, "
    			+ " Received_status = 4,"
    			+ " RECEIVED_DATE=null "
    			+ " where Assign_Handover_id=:id");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("goodsContent", obj.getReceivedGoodsContent());
    	query.setParameter("obsContent", obj.getReceivedObstructContent());
    	query.setParameter("id", obj.getAssignHandoverId());
    	
    	return query.executeUpdate();
    }
    
    public long updateAssignHandoverNotVuongVtmd(AssignHandoverDTO obj) {
    	StringBuilder sql = new StringBuilder("Update Assign_Handover set Received_date = sysdate,  "
    			+ " Received_obstruct_date = null,"
    	    	+ " Received_obstruct_content=null,  "
    	    	+ " Received_goods_date = null,"
    	    	+ " Received_goods_content=null, "
    			+ " Received_status = 2"
    			+ " where Assign_Handover_id=:id");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("id", obj.getAssignHandoverId());
    	return query.executeUpdate();
    }
    
    public List<ObstructedDTO> findObstructedByConsId(AssignHandoverDTO obj){
    	StringBuilder sql = new StringBuilder("SELECT ");
    			sql.append("ob.CLOSED_DATE closedDate, ")
    			.append("ob.CONSTRUCTION_ID constructionId, ")
    			.append("ob.CREATED_DATE createdDate, ")
    			.append("ob.CREATED_GROUP_ID createdGroupId, ")
    			.append("ob.CREATED_USER_ID createdUserId, ")
    			.append("ob.OBSTRUCTED_CONTENT obstructedContent, ")
    			.append("ob.OBSTRUCTED_ID obstructedId, ")
    			.append("ob.OBSTRUCTED_STATE obstructedState, ")
    			.append("ob.UPDATED_DATE updatedDate, ")
    			.append("ob.UPDATED_GROUP_ID updatedGroupId, ")
    			.append("ob.UPDATED_USER_ID updatedUserId, ")
    			.append("ob.WORK_ITEM_ID workItemId ")
    			.append("FROM OBSTRUCTED ob " + 
    			"INNER JOIN CONSTRUCTION cons " + 
    			"ON ob.CONSTRUCTION_ID  = cons.CONSTRUCTION_ID " + 
    			"WHERE ob.WORK_ITEM_ID IS NULL " + 
    			"AND ob.CONSTRUCTION_ID =:id");
    			
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
	    	query.addScalar("closedDate", new DateType());
	    	query.addScalar("constructionId", new LongType());
	    	query.addScalar("createdDate", new DateType());
	    	query.addScalar("createdGroupId", new LongType());
	    	query.addScalar("createdUserId", new LongType());
	    	query.addScalar("obstructedContent", new StringType());
	    	query.addScalar("obstructedId", new LongType());
	    	query.addScalar("obstructedState", new StringType());
	    	query.addScalar("updatedDate", new DateType());
	    	query.addScalar("updatedGroupId", new LongType());
	    	query.addScalar("updatedUserId", new LongType());
	    	query.addScalar("workItemId", new LongType());
    	
    	query.setResultTransformer(Transformers.aliasToBean(ObstructedDTO.class));
    	
    	query.setParameter("id", obj.getConstructionId());
    	
    	return query.list();
    }
    
    public List<ConstructionTaskDTO> findConstructionTaskByConsIdVuongVtmd(AssignHandoverDTO obj){
    	StringBuilder sql = new StringBuilder("SELECT ct.CONSTRUCTION_TASK_ID constructionTaskId"
    			+ " FROM CONSTRUCTION_TASK ct " + 
    			"inner join CONSTRUCTION cons on ct.CONSTRUCTION_ID = cons.CONSTRUCTION_ID " + 
    			"inner join WORK_ITEM wi on ct.WORK_ITEM_ID = wi.WORK_ITEM_ID " + 
    			"where ct.LEVEL_ID=4 " + 
    			"and wi.STATUS=4 " + 
    			"and cons.STATUS=4 " + 
    			"and cons.CONSTRUCTION_ID=:id");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("constructionTaskId", new LongType());
    	
    	query.setResultTransformer(Transformers.aliasToBean(ConstructionTaskDTO.class));
    	
    	query.setParameter("id", obj.getConstructionId());
    	
    	return query.list();
    }
    
    public List<ConstructionTaskDTO> findConstructionTaskByConsIdNotVuongVtmd(AssignHandoverDTO obj){
    	StringBuilder sql = new StringBuilder("SELECT ct.CONSTRUCTION_TASK_ID constructionTaskId"
    			+ " FROM CONSTRUCTION_TASK ct " + 
    			"inner join CONSTRUCTION cons on ct.CONSTRUCTION_ID = cons.CONSTRUCTION_ID " + 
    			"inner join WORK_ITEM wi on ct.WORK_ITEM_ID = wi.WORK_ITEM_ID " + 
    			"where ct.LEVEL_ID=4 " + 
    			"and wi.STATUS=1 " + 
    			"and cons.STATUS=2 " + 
    			"and cons.CONSTRUCTION_ID=:id");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("constructionTaskId", new LongType());
    	
    	query.setResultTransformer(Transformers.aliasToBean(ConstructionTaskDTO.class));
    	
    	query.setParameter("id", obj.getConstructionId());
    	
    	return query.list();
    }
    
    public int updateRpStationCompleteHandoverDateBuild(AssignHandoverDTO obj) {
    	StringBuilder sql = new StringBuilder("update rp_station_complete set handover_date_build=sysdate "
    			+ " where cat_station_house_code=:houseCode"
    			+ " and cnt_contract_Code=:contractCode ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("houseCode", obj.getCatStationHouseCode());
    	query.setParameter("contractCode", obj.getCntContractCode());
    	return query.executeUpdate();
    }
    
    public List<String> getCodeByGroupIdLst(List<String> lstId, String code){
    	StringBuilder sql = new StringBuilder("Select code sysGroupCode from sys_group where sys_group_id in (:lstId) and CODE=:code ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("sysGroupCode", new StringType());
    	query.setResultTransformer(Transformers.aliasToBean(AssignHandoverDTO.class));
    	query.setParameterList("lstId", lstId);
    	query.setParameter("code", code);
    	return query.list();
    }
    
    public List<AssignHandoverDTO> findConstructionByAssignHandoverTable(){
    	StringBuilder sql = this.createDoSearchBaseQuery();
//      hoanm1_20190710_start
    	sql.append(", '' fullName ");
//      hoanm1_20190710_end
        sql.append("FROM ASSIGN_HANDOVER a ");
        sql.append("WHERE 1=1 and a.IS_DELIVERED = 0 and construction_code is not null ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	this.addQueryScalarDoSearch(query);
    	query.setResultTransformer(Transformers.aliasToBean(AssignHandoverDTO.class));
    	return query.list();
    }
    
    //Check Cao cột & dưới đất
    public List<WorkItemDTO> checkByType(AssignHandoverDTO obj) {
    	StringBuilder sql = new StringBuilder("SELECT cwit.CAT_WORK_ITEM_TYPE_ID catWorkItemTypeId, " + 
    			" wi.WORK_ITEM_ID workItemId, " + 
    			" wi.CONSTRUCTION_ID constructionId, " +
    			" wi.IS_INTERNAL isInternal, " +
    			" wi.CONSTRUCTOR_ID constructorId " +
    			" FROM CTCT_CAT_OWNER.CAT_WORK_ITEM_TYPE cwit " + 
    			" inner JOIN WORK_ITEM wi " + 
    			" ON cwit.CAT_WORK_ITEM_TYPE_ID = wi.CAT_WORK_ITEM_TYPE_ID " + 
    			" where wi.CONSTRUCTION_ID = :id " + 
    			" and cwit.type in (:lstType)");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("catWorkItemTypeId", new LongType());
    	query.addScalar("workItemId", new LongType());
    	query.addScalar("constructionId", new LongType());
    	query.addScalar("isInternal", new StringType());
    	query.addScalar("constructorId", new LongType());
    	
    	query.setResultTransformer(Transformers.aliasToBean(WorkItemDTO.class));
    	
    	query.setParameter("id", obj.getConstructionId());
    	query.setParameterList("lstType", obj.getLstType());
    	
    	return query.list();
    }
    
    public void deleteWorkItem(Long workItemId) {
    	StringBuilder sql = new StringBuilder("delete from work_item where work_item_id=:workItemId ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("workItemId", workItemId);
    	query.executeUpdate();
    }
    
    public void deleteConstructionTask(Long workItemId) {
    	StringBuilder sql = new StringBuilder("delete from Construction_task where work_item_id=:workItemId ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("workItemId", workItemId);
    	query.executeUpdate();
    }
    
    public void deleteConsTask(Long workItemId, Long constructionId) {
    	StringBuilder sql = new StringBuilder("delete from construction_task where work_item_id=:workItemId "
    			+ " and construction_id=:constructionId");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("workItemId", workItemId);
    	query.setParameter("constructionId", constructionId);
    	query.executeUpdate();
    }
    
    public void updateReceiveDate(Long id) {
    	StringBuilder sql = new StringBuilder("update assign_handover set RECEIVED_DATE = null where ASSIGN_HANDOVER_ID=:id");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("id", id);
    	query.executeUpdate();
    }
    
    public List<CatWorkItemTypeDTO> getCatWorkItemByType(AssignHandoverDTO obj){
    	StringBuilder sql = new StringBuilder("SELECT ");
    	sql.append("CAT_WORK_ITEM_TYPE_ID catWorkItemTypeId, ")
    	.append("NAME name, ")
    	.append("CODE code, ")
    	.append("STATUS status, ")
    	.append("DESCRIPTION description, ")
    	.append("CAT_CONSTRUCTION_TYPE_ID catConstructionTypeId, ")
    	.append("CREATED_DATE createdDate, ")
    	.append("UPDATED_DATE updatedDate, ")
    	.append("CREATED_USER createdUser, ")
    	.append("UPDATED_USER updatedUser, ")
    	.append("ITEM_ORDER itemOrder, ")
    	.append("TAB tab, ")
    	.append("QUANTITY_BY_DATE quantityByDate, ")
    	.append("CAT_WORK_ITEM_GROUP_ID catWorkItemGroupId, ")
    	.append("TYPE type FROM CAT_WORK_ITEM_TYPE"
    			+ " WHERE TYPE=:typeSave"
    			+ " AND construction_id=:consId"
    			+ " AND WORK_ITEM_ID=:workId");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("catWorkItemTypeId", new LongType());
    	query.addScalar("name", new StringType());
    	query.addScalar("code", new StringType());
    	query.addScalar("status", new StringType());
    	query.addScalar("description", new StringType());
    	query.addScalar("catConstructionTypeId", new LongType());
    	query.addScalar("createdDate", new DateType());
    	query.addScalar("updatedDate", new DateType());
    	query.addScalar("createdUser", new LongType());
    	query.addScalar("updatedUser", new LongType());
    	query.addScalar("itemOrder", new LongType());
    	query.addScalar("tab", new StringType());
    	query.addScalar("quantityByDate", new StringType());
    	query.addScalar("catWorkItemGroupId", new LongType());
    	query.addScalar("type", new LongType());
    	
    	query.setResultTransformer(Transformers.aliasToBean(CatWorkItemTypeDTO.class));
    	
    	query.setParameter("typeSave", obj.getTypeSave());
    	query.setParameter("consId", obj.getConstructionId());
    	query.setParameter("workId", obj.getWorkItemId());
    	
    	return query.list();
    }
    
    public long updateUserId(Long id) {
    	StringBuilder sql = new StringBuilder("update assign_handover set update_date=sysdate,update_user_id=:id");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("id", id);
    	return query.executeUpdate();
    	
    }
    
    public AssignHandoverDTO findStationContractByConstructionCode(String code){
    	StringBuilder sql = this.createDoSearchBaseQuery();
//      hoanm1_20190710_start
    	sql.append(", '' fullName ");
//      hoanm1_20190710_end
        sql.append("FROM ASSIGN_HANDOVER a ");
        sql.append("WHERE 1=1 and a.construction_code = :code ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	this.addQueryScalarDoSearch(query);
    	query.setResultTransformer(Transformers.aliasToBean(AssignHandoverDTO.class));
    	query.setParameter("code", code);

    	@SuppressWarnings("unchecked")
        List<AssignHandoverDTO> lst = query.list();
        if (lst.size() > 0) {
            return lst.get(0);
        }
        return null;
    }
    
    public List<AssignHandoverDTO> checkGroupByRole(String email,List<String> groupLst) {
    	StringBuilder sql = new StringBuilder("SELECT sg.PARENT_ID parentId, " +
    			" su.SYS_USER_ID sysUserId, " + 
    			" su.EMPLOYEE_CODE employeeCode, " + 
    			" su.FULL_NAME fullName, " + 
    			" su.EMAIL email," +
    			" su.PHONE_NUMBER phoneNumber " +
    			" FROM CTCT_VPS_OWNER.SYS_USER su " + 
    			" INNER JOIN CTCT_CAT_OWNER.SYS_GROUP sg " + 
    			" ON su.SYS_GROUP_ID = sg.SYS_GROUP_ID " + 
    			" where su.EMAIL=:email " + 
    			" and sg.GROUP_NAME_LEVEL2 IN " + 
    			"  (SELECT sg.name " + 
    			"  FROM CTCT_CAT_OWNER.SYS_GROUP sg " + 
    			"  WHERE sg.SYS_GROUP_ID IN (:groupLst))");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("parentId", new LongType());
    	query.addScalar("sysUserId", new LongType());
    	query.addScalar("employeeCode", new StringType());
    	query.addScalar("fullName", new StringType());
    	query.addScalar("email", new StringType());
    	query.addScalar("phoneNumber", new StringType());
    	
    	query.setResultTransformer(Transformers.aliasToBean(AssignHandoverDTO.class));
    	query.setParameter("email", email);
    	query.setParameterList("groupLst", groupLst);
    	
    	return query.list();
    }
    
    @SuppressWarnings("unchecked")
	public List<AssignHandoverDTO> getAllSysGroup(){
    	StringBuilder sql = new StringBuilder("select code sysGroupCode,"
    			+ " name sysGroupName "
    			+ " from CTCT_CAT_OWNER.SYS_GROUP "
    			+ " where status=1 "
    			+ " order by sys_group_id asc ");
    	
//    	StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
//        sqlCount.append(sql.toString());
//        sqlCount.append(")");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
//        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());
        
        query.addScalar("sysGroupCode", new StringType());
        query.addScalar("sysGroupName", new StringType());
        
        query.setResultTransformer(Transformers.aliasToBean(AssignHandoverDTO.class));
        
        return query.list();
    }
    
    public List<SysUserDTO> getAllSysUser(List<String> groupLst){
    	StringBuilder sql = new StringBuilder("SELECT " +
    			" su.SYS_USER_ID sysUserId, " + 
    			" su.EMPLOYEE_CODE employeeCode, " + 
    			" su.FULL_NAME fullName, " + 
    			" su.EMAIL email," +
    			" su.PHONE_NUMBER phone " +
    			" FROM CTCT_VPS_OWNER.SYS_USER su " + 
    			" INNER JOIN CTCT_CAT_OWNER.SYS_GROUP sg " + 
    			" ON su.SYS_GROUP_ID = sg.SYS_GROUP_ID " + 
    			" where sg.GROUP_NAME_LEVEL2 IN " + 
    			"  (SELECT sg.name " + 
    			"  FROM CTCT_CAT_OWNER.SYS_GROUP sg " + 
    			"  WHERE su.email is not null and sg.SYS_GROUP_ID IN (:groupLst))");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("sysUserId", new LongType());
//        query.addScalar("loginName", new StringType());
        query.addScalar("fullName", new StringType());
        query.addScalar("employeeCode", new StringType());
        query.addScalar("email", new StringType());
        query.addScalar("phone", new StringType());
        
        query.setResultTransformer(Transformers.aliasToBean(SysUserDTO.class));
        
        query.setParameterList("groupLst", groupLst);
        
        return query.list();
    }
    
    public SysUserDTO getSysUserById(String employeeCode, String emailName){
    	StringBuilder sql = new StringBuilder("SELECT su.SYS_USER_ID sysUserId, " + 
        		" su.login_name loginName, " + 
        		" su.full_name fullName, " + 
        		" su.EMPLOYEE_CODE employeeCode, " + 
        		" su.email email, " + 
        		" su.PHONE_NUMBER phone " + 
        		" FROM sys_user su "
        		+ " where su.EMPLOYEE_CODE=:employeeCode"
        		+ " or email=:emailName ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("sysUserId", new LongType());
        query.addScalar("loginName", new StringType());
        query.addScalar("fullName", new StringType());
        query.addScalar("employeeCode", new StringType());
        query.addScalar("email", new StringType());
        query.addScalar("phone", new StringType());
        
        query.setResultTransformer(Transformers.aliasToBean(SysUserDTO.class));
        
        query.setParameter("employeeCode", employeeCode);
        query.setParameter("emailName", emailName);
        
        @SuppressWarnings("unchecked")
        List<SysUserDTO> lst = query.list();
        if (lst.size() > 0) {
            return lst.get(0);
        }
        return null;
    }
    
    public void updateHandoverDateBuild(Long id) {
    	StringBuilder sql = new StringBuilder("Update construction set HANDOVER_DATE_BUILD=null,status=1 where construction_id=:id");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("id", id);
    	query.executeUpdate();
    }
    
    public ConstructionDTO getStatusByConsId(Long id) {
    	StringBuilder sql = new StringBuilder("select status status from construction where construction_id=:id");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("status", new StringType());
    	query.setResultTransformer(Transformers.aliasToBean(ConstructionDTO.class));
    	query.setParameter("id", id);
    	 @SuppressWarnings("unchecked")
         List<ConstructionDTO> lst = query.list();
         if (lst.size() > 0) {
             return lst.get(0);
         }
         return null;
    }
    
    public List<CatWorkItemTypeDTO> getCatWorkItemTypeByType(List<String> typeSave){
    	StringBuilder sql = new StringBuilder("Select ");
    	sql.append("CAT_WORK_ITEM_TYPE_ID catWorkItemTypeId, ")
    	.append("NAME name, ")
    	.append("CODE code, ")
    	.append("STATUS status, ")
    	.append("DESCRIPTION description, ")
    	.append("CAT_CONSTRUCTION_TYPE_ID catConstructionTypeId, ")
    	.append("CREATED_DATE createdDate, ")
    	.append("UPDATED_DATE updateDate, ")
    	.append("CREATED_USER createdUser, ")
    	.append("UPDATED_USER updateUser, ")
    	.append("ITEM_ORDER itemOrder, ")
    	.append("TAB tab, ")
    	.append("QUANTITY_BY_DATE quantityByDate, ")
    	.append("CAT_WORK_ITEM_GROUP_ID catWorkItemGroupId, ")
    	.append("TYPE type ")
    	.append("From cat_work_item_type where type in (:typeSave) ");
    	
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	
    	query.addScalar("catWorkItemTypeId", new LongType());
    	query.addScalar("name", new StringType());
    	query.addScalar("code", new StringType());
    	query.addScalar("status", new StringType());
    	query.addScalar("description", new StringType());
    	query.addScalar("catConstructionTypeId", new LongType());
    	query.addScalar("createdDate", new DateType());
    	query.addScalar("updateDate", new DateType());
    	query.addScalar("createdUser", new LongType());
    	query.addScalar("updateUser", new LongType());
    	query.addScalar("itemOrder", new StringType());
    	query.addScalar("tab", new StringType());
    	query.addScalar("quantityByDate", new StringType());
    	query.addScalar("catWorkItemGroupId", new LongType());
    	query.addScalar("type", new StringType());
    	
    	query.setResultTransformer(Transformers.aliasToBean(CatWorkItemTypeDTO.class));
    	
    	query.setParameterList("typeSave", typeSave);
    	
    	return query.list();
    }
    
    public List<WorkItemDTO> checkHaveCatWorkById(String type, Long id) {
    	StringBuilder sql = new StringBuilder("SELECT wi.WORK_ITEM_ID workItemId,"
    			+ " wi.CAT_WORK_ITEM_TYPE_ID catWorkItemTypeId "
    			+ " FROM WORK_ITEM wi " + 
    			"left join CTCT_CAT_OWNER.CAT_WORK_ITEM_TYPE cwit " + 
    			"on wi.CAT_WORK_ITEM_TYPE_ID = cwit.CAT_WORK_ITEM_TYPE_ID " + 
    			"where cwit.type=:type " + 
    			"and wi.CONSTRUCTION_ID=:id");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("workItemId", new LongType());
    	query.addScalar("catWorkItemTypeId", new LongType());
    	query.setResultTransformer(Transformers.aliasToBean(WorkItemDTO.class));
    	query.setParameter("type", type);
    	query.setParameter("id", id);
    	return query.list();
    }
    
    public void deleteWorkItemByConsId(Long id, Long workId) {
    	StringBuilder sql = new StringBuilder("delete from work_item where construction_id=:id and cat_work_item_type_id = :workId ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("id", id);
    	query.setParameter("workId", workId);
    	query.executeUpdate();
    }
    //HuyPQ-20190524-start
    public CntContractDTO getContractByConsId(Long id) {
    	StringBuilder sql = new StringBuilder("Select cc.cnt_contract_id cntContractId, " + 
    			"cc.code code, " + 
    			"cc.name name " + 
    			"FROM CONSTRUCTION cons, " + 
    			"(select distinct cc.code,cc.name,cc.cnt_contract_id,cwit.CONSTRUCTION_ID from " + 
    			"CTCT_IMS_OWNER.CNT_CONSTR_WORK_ITEM_TASK cwit, CTCT_IMS_OWNER.CNT_CONTRACT cc " + 
    			"where cc.CNT_CONTRACT_ID = cwit.CNT_CONTRACT_ID and cc.status !=0 and cwit.status !=0 and cc.contract_type=0)cc " + 
    			"where cons.CONSTRUCTION_ID=cc.CONSTRUCTION_ID and cons.CONSTRUCTION_ID=:id ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	
    	query.addScalar("cntContractId", new LongType());
    	query.addScalar("code", new StringType());
    	query.addScalar("name", new StringType());
    	
    	query.setResultTransformer(Transformers.aliasToBean(CntContractDTO.class));
    	
    	query.setParameter("id", id);
    	
    	@SuppressWarnings("unchecked")
        List<CntContractDTO> lst = query.list();
        if (lst.size() > 0) {
            return lst.get(0);
        }
        return null;
    }
    
    //HuyPQ-end
    public List<WorkItemDTO> getWorkItemByCatType(AssignHandoverDTO obj){
    StringBuilder sql = new StringBuilder("SELECT wi.work_item_id workItemId,"
    		+ " wi.cat_Work_Item_Type_Id catWorkItemTypeId,"
    		+ " wi.construction_id constructionId "
    		+ " FROM WORK_ITEM wi " + 
    		"           LEFT join CTCT_CAT_OWNER.CAT_WORK_ITEM_TYPE cwit " + 
    		"           on wi.CAT_WORK_ITEM_TYPE_ID = cwit.CAT_WORK_ITEM_TYPE_ID " + 
    		"           where cwit.type in (:typeSave) " + 
    		"           and wi.CONSTRUCTION_ID=:id");
	SQLQuery query = getSession().createSQLQuery(sql.toString());
	query.addScalar("catWorkItemTypeId", new LongType());
	query.addScalar("workItemId", new LongType());
	query.addScalar("constructionId", new LongType());
	
	query.setResultTransformer(Transformers.aliasToBean(WorkItemDTO.class));
	
	query.setParameter("id", obj.getConstructionId());
	query.setParameterList("typeSave", obj.getTypeSave());
	
	return query.list();
    }
    
    public List<WorkItemDTO> getWorkItemByCatTypeDelete(AssignHandoverDTO obj){
        StringBuilder sql = new StringBuilder("SELECT wi.work_item_id workItemId,"
        		+ " wi.cat_Work_Item_Type_Id catWorkItemTypeId,"
        		+ " wi.construction_id constructionId "
        		+ " FROM WORK_ITEM wi " + 
        		"           LEFT join CTCT_CAT_OWNER.CAT_WORK_ITEM_TYPE cwit " + 
        		"           on wi.CAT_WORK_ITEM_TYPE_ID = cwit.CAT_WORK_ITEM_TYPE_ID " + 
        		"           where cwit.type in (:lstType) " + 
        		"           and wi.CONSTRUCTION_ID=:id");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("catWorkItemTypeId", new LongType());
    	query.addScalar("workItemId", new LongType());
    	query.addScalar("constructionId", new LongType());
    	
    	query.setResultTransformer(Transformers.aliasToBean(WorkItemDTO.class));
    	
    	query.setParameter("id", obj.getConstructionId());
    	query.setParameterList("lstType", obj.getLstType());
    	
    	return query.list();
    }
    
    public int updateRpStationCompleteTTKT(AssignHandoverDTO obj, String houseCode, String contractCode) {
    	StringBuilder sql = new StringBuilder("update rp_station_complete set sys_group_id=:id,"
    			+ " sys_group_code=:code, "
    			+ " sys_group_name=:name "
    			+ " where cat_station_house_code=:houseCode"
    			+ " and cnt_contract_code=:contractCode ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("id", obj.getSysGroupId());
    	query.setParameter("code", obj.getSysGroupCode());
    	query.setParameter("name", obj.getSysGroupName());
    	query.setParameter("houseCode", houseCode);
    	query.setParameter("contractCode", contractCode);
    	
    	return query.executeUpdate();
    }
    
    public int updateRpStationCompleteNV(AssignHandoverDTO obj) {
    	StringBuilder sql = new StringBuilder("update rp_station_complete set "
    			+ " HOUSE_TYPE_NAME=null,"
    			+ " COLUMN_HEIGHT=null "
    			+ " where cat_station_house_code=:houseCode"
    			+ " and cnt_contract_code=:contractCode ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("houseCode", obj.getCatStationHouseCode());
    	query.setParameter("contractCode", obj.getCntContractCode());
    	
    	return query.executeUpdate();
    }
    
    public int updateRpStationCompleteWhenAssign(AssignHandoverDTO obj) {
    	StringBuilder sql = new StringBuilder("update rp_station_complete set ");
    			if(StringUtils.isNotEmpty(obj.getHouseTypeName())) {
    				sql.append(" HOUSE_TYPE_NAME=:name,");
    	    	} else {
    	    		sql.append(" HOUSE_TYPE_NAME=null,");
    	    	}
    			if(obj.getColumnHeight()!=null) {
    				sql.append(" COLUMN_HEIGHT=:height,");
    			} else {
    				sql.append(" COLUMN_HEIGHT=null,");
    			}
    			sql.append(" NUMBER_CO=:numberCo,"
    					+ " STATION_TYPE=:type "
    			+ " where cat_station_house_code=:houseCode"
    			+ " and cnt_contract_code=:contractCode ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	if(StringUtils.isNotEmpty(obj.getHouseTypeName())) {
    		query.setParameter("name", obj.getHouseTypeName());
    	}
		if(obj.getColumnHeight()!=null) {
			query.setParameter("height", obj.getColumnHeight());
		}
    	query.setParameter("numberCo", obj.getNumberCo());
    	query.setParameter("houseCode", obj.getCatStationHouseCode());
    	query.setParameter("contractCode", obj.getCntContractCode());
    	query.setParameter("type", obj.getStationType());
    	
    	return query.executeUpdate();
    }
    
    public WorkItemDTO getIsInternal(AssignHandoverDTO obj) {
    	StringBuilder sql = new StringBuilder("SELECT DISTINCT IS_INTERNAL isInternal,"
    			+ " CONSTRUCTOR_ID constructorId, "
    			+ " SUPERVISOR_ID supervisorId" + 
    			" FROM WORK_ITEM " + 
    			" WHERE  CONSTRUCTION_ID=:id "
    			+ " and IS_INTERNAL is not null"
    			+ " and CONSTRUCTOR_ID is not null"
    			+ " and SUPERVISOR_ID is not null ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("isInternal", new StringType());
    	query.addScalar("constructorId", new LongType());
    	query.addScalar("supervisorId", new LongType());
    	
    	query.setResultTransformer(Transformers.aliasToBean(WorkItemDTO.class));
    	
    	query.setParameter("id", obj.getConstructionId());
    	
    	@SuppressWarnings("unchecked")
        List<WorkItemDTO> lst = query.list();
        if (lst.size() > 0) {
            return lst.get(0);
        }
        return null;
    }
    
    public AssignHandoverDTO getCatConstructionTypeId(Long id) {
    	StringBuilder sql = new StringBuilder("SELECT CAT_CONSTRUCTION_TYPE_ID catConstructionTypeId"
    			+ " FROM CONSTRUCTION"
    			+ " where construction_id=:id ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("catConstructionTypeId", new LongType());
    	query.setResultTransformer(Transformers.aliasToBean(AssignHandoverDTO.class));
    	query.setParameter("id", id);
    	@SuppressWarnings("unchecked")
        List<AssignHandoverDTO> lst = query.list();
        if (lst.size() > 0) {
            return lst.get(0);
        }
        return null; 
    }
    
    public List<GoodsPlanDTO> findSignStateGoodsPlanByConsId(Long id){
    	StringBuilder sql = new StringBuilder("SELECT gp.SIGN_STATE signState " + 
    			"FROM GOODS_PLAN gp " + 
    			"LEFT JOIN GOODS_PLAN_DETAIL gpd " + 
    			"ON gpd.GOODS_PLAN_ID     = gp.GOODS_PLAN_ID " + 
    			"WHERE gpd.CONSTRUCTION_ID=:id");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	
    	query.addScalar("signState", new StringType());
    	
    	query.setResultTransformer(Transformers.aliasToBean(GoodsPlanDTO.class));
    	
    	query.setParameter("id", id);
    	
    	return query.list();
    }
    
    public void updateConstructionTuyen(AssignHandoverDTO obj, Long userId) {
    	StringBuilder sql = new StringBuilder("update assign_handover set total_Length=:totalLength,UPDATE_DATE=sysdate,UPDATE_USER_ID=:userId, ");
    	if(obj.getStationType()==3l) {
    		sql.append(" Hidden_immediacy=:hiddenImmediacy ,"
        			+ " cable_In_Tank=:cableInTank,"
        			+ " cable_In_Tank_Drain=:cableInTankDrain,"
        			+ " plant_Columns=null,"
        			+ " available_Columns=null,");
    	} 
    	if(obj.getStationType()==4l) {
    		sql.append(" Hidden_immediacy=null ,"
        			+ " cable_In_Tank=null,"
        			+ " cable_In_Tank_Drain=null,"
        			+ " plant_Columns=:plantColumns,"
        			+ " available_Columns=:availableColumns,");
    	}
    	sql.append(" station_type=:type ");
    	sql.append(" where assign_handover_id=:id ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("totalLength", obj.getTotalLength());
    	if(obj.getStationType()==3l) {
    		query.setParameter("hiddenImmediacy", obj.getHiddenImmediacy());
        	query.setParameter("cableInTank", obj.getCableInTank());
        	query.setParameter("cableInTankDrain", obj.getCableInTankDrain());
    	}
    	if(obj.getStationType()==4l) {
    		query.setParameter("plantColumns", obj.getPlantColunms());
        	query.setParameter("availableColumns", obj.getAvailableColumns());
    	}
    	query.setParameter("id", obj.getAssignHandoverId());
    	query.setParameter("type", obj.getStationType());
    	query.setParameter("userId", userId);
    	
    	query.executeUpdate();
    }
    
    public void updateHandoverDateBuildNow(Long id) {
    	StringBuilder sql = new StringBuilder("Update construction set HANDOVER_DATE_BUILD=sysdate where construction_id=:id");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("id", id);
    	query.executeUpdate();
    }
    
    public void updateHandoverDateBuildNowStatus(Long id) {
    	StringBuilder sql = new StringBuilder("Update construction set HANDOVER_DATE_BUILD=sysdate,status=2 where construction_id=:id");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("id", id);
    	query.executeUpdate();
    }
    
    public void updateStatusHetVuong(Long id) {
    	StringBuilder sql = new StringBuilder("Update work_item set status='1' where construction_id=:id ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("id", id);
    	query.executeUpdate();
    }
    
    public AssignHandoverDTO getContractHouseByConsId(Long id) {
    	StringBuilder sql = new StringBuilder("SELECT CAT_STATION_HOUSE_CODE catStationHouseCode,"
    			+ "CNT_CONTRACT_CODE cntContractCode "
    			+ " FROM ASSIGN_HANDOVER where ASSIGN_HANDOVER_ID=:id ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("catStationHouseCode", new StringType());
    	query.addScalar("cntContractCode", new StringType());
    	query.setResultTransformer(Transformers.aliasToBean(AssignHandoverDTO.class));
    	query.setParameter("id", id);
    	@SuppressWarnings("unchecked")
        List<AssignHandoverDTO> lst = query.list();
        if (lst.size() > 0) {
            return lst.get(0);
        }
        return null;

    }
    
    public void updateRpStationComplete(AssignHandoverDTO obj, String houseCode, String contractCode) {
    	StringBuilder sql = new StringBuilder("Update RP_STATION_COMPLETE set "
    			+ " SYS_GROUP_ID=:id,"
    			+ " SYS_GROUP_code=:code,"
    			+ " SYS_GROUP_name=:name"
    			+ " where CAT_STATION_HOUSE_CODE=:houseCode"
    			+ " and CNT_CONTRACT_CODE=:contractCode ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("id", obj.getSysGroupId());
    	query.setParameter("code", obj.getSysGroupCode());
    	query.setParameter("name", obj.getSysGroupName());
    	query.setParameter("houseCode", houseCode);
    	query.setParameter("contractCode", contractCode);
    	query.executeUpdate();
    }
    
    public void updateAmountConstruction(AssignHandoverDTO obj) {
    	StringBuilder sql = new StringBuilder("Update construction set amount=:amount where construction_id=:id ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("id", obj.getConstructionId());
    	query.setParameter("amount", obj.getTotalLength());
    	query.executeUpdate();
    }
    //Huy-end
    
    public void updateVuongTaskWorkCons(Long constructionId) {
        String HET_VUONG = "0";
        //cap nhat cong trinh ve status=4
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(
                    "UPDATE CONSTRUCTION set status=4 where construction_id = :constructionId ");
            SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
//            cap nhat hang muc ve status=4
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(
                    "UPDATE work_item set status=4 where construction_id = :constructionId ");
            SQLQuery query2 = getSession().createSQLQuery(stringBuilder2.toString());
//            cap nhat cong viec ve status=3
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append(" update construction_task set status=3 where status !=4 and level_id=4 and type=1 and construction_id = :constructionId ");
            SQLQuery query3 = getSession().createSQLQuery(stringBuilder3.toString());
            
            query.setParameter("constructionId", constructionId);
            query2.setParameter("constructionId", constructionId);
            query3.setParameter("constructionId", constructionId);
            query.executeUpdate();
            query2.executeUpdate();
            query3.executeUpdate();
            getSession().flush();
        
    }
    public void updateVTMDTaskWorkCons(Long constructionId) {
        //cap nhat cong trinh ve status=2
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(
                    "UPDATE CONSTRUCTION set status=2 where construction_id = :constructionId ");
            SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
//            cap nhat hang muc ve status=1
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(
                    "UPDATE work_item set status=1 where construction_id = :constructionId ");
            SQLQuery query2 = getSession().createSQLQuery(stringBuilder2.toString());
//            cap nhat cong viec ve status=1
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append(" update construction_task set status=1 where status !=4 and level_id=4 and type=1 and construction_id = :constructionId ");
            SQLQuery query3 = getSession().createSQLQuery(stringBuilder3.toString());
            
            query.setParameter("constructionId", constructionId);
            query2.setParameter("constructionId", constructionId);
            query3.setParameter("constructionId", constructionId);
            query.executeUpdate();
            query2.executeUpdate();
            query3.executeUpdate();
            getSession().flush();
        
    }
    
    public void updateHetVuongTaskWorkCons(Long constructionId) {
        //cap nhat cong viec ve status=1
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(
                    "UPDATE CONSTRUCTION_TASK SET STATUS=2 WHERE nvl(COMPLETE_PERCENT,0) !=0 AND CONSTRUCTION_TASK_ID IN( ");
            stringBuilder.append("SELECT CONSTRUCTION_TASK_ID FROM CONSTRUCTION_TASK A,DETAIL_MONTH_PLAN B WHERE ");
            stringBuilder
                    .append("A.DETAIL_MONTH_PLAN_ID=B.DETAIL_MONTH_PLAN_ID AND B.SIGN_STATE=3 AND B.STATUS=1 AND ");
            stringBuilder.append(
                    "LPAD(A.MONTH,2,0) = TO_CHAR(SYSDATE,'MM') AND A.YEAR = TO_CHAR(SYSDATE,'YYYY') AND A.TYPE=1 AND LEVEL_ID=4 AND A.STATUS !=4 ");
            stringBuilder.append("START WITH A.CONSTRUCTION_ID = :constructionId ");
            stringBuilder.append("CONNECT BY PRIOR A.CONSTRUCTION_TASK_ID = A.PARENT_ID) ");
            SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
//            cap nhat cong viec ve status=2
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(
                    "UPDATE CONSTRUCTION_TASK SET STATUS=1 WHERE nvl(COMPLETE_PERCENT,0) =0 AND CONSTRUCTION_TASK_ID IN( ");
            stringBuilder2.append("SELECT CONSTRUCTION_TASK_ID FROM CONSTRUCTION_TASK A,DETAIL_MONTH_PLAN B WHERE ");
            stringBuilder2
                    .append("A.DETAIL_MONTH_PLAN_ID=B.DETAIL_MONTH_PLAN_ID AND B.SIGN_STATE=3 AND B.STATUS=1 AND ");
            stringBuilder2.append(
                    "LPAD(A.MONTH,2,0) = TO_CHAR(SYSDATE,'MM') AND A.YEAR = TO_CHAR(SYSDATE,'YYYY') AND A.TYPE=1 AND LEVEL_ID=4 AND A.STATUS !=4 ");
            stringBuilder2.append("START WITH A.CONSTRUCTION_ID = :constructionId ");
            stringBuilder2.append("CONNECT BY PRIOR A.CONSTRUCTION_TASK_ID=A.PARENT_ID) ");
            SQLQuery query2 = getSession().createSQLQuery(stringBuilder2.toString());
            
//            cap nhat hang muc ve status=1
            StringBuilder workUnImplemented = new StringBuilder();
            workUnImplemented.append(" update work_item set status=1 where status !=3 and  work_item_id in( ");
            workUnImplemented.append(" SELECT work_item_id FROM CONSTRUCTION_TASK A,DETAIL_MONTH_PLAN B WHERE ");
            workUnImplemented.append(" A.DETAIL_MONTH_PLAN_ID=B.DETAIL_MONTH_PLAN_ID AND B.SIGN_STATE=3 AND B.STATUS=1 AND ");
            workUnImplemented.append(" LPAD(A.MONTH,2,0) = TO_CHAR(SYSDATE,'MM') AND A.YEAR = TO_CHAR(SYSDATE,'YYYY') AND A.TYPE=1 AND LEVEL_ID=3 ");
            workUnImplemented.append(" and nvl(A.COMPLETE_PERCENT,0) =0 and A.CONSTRUCTION_ID = :constructionId) ");
            SQLQuery queryWorkUnImplemented = getSession().createSQLQuery(workUnImplemented.toString());
//            cap nhat hang muc ve status=2
            StringBuilder workImplemented = new StringBuilder();
            workImplemented.append(" update work_item set status=2 where status !=3 and work_item_id in( ");
            workImplemented.append(" SELECT work_item_id FROM CONSTRUCTION_TASK A,DETAIL_MONTH_PLAN B WHERE ");
            workImplemented.append(" A.DETAIL_MONTH_PLAN_ID=B.DETAIL_MONTH_PLAN_ID AND B.SIGN_STATE=3 AND B.STATUS=1 AND ");
            workImplemented.append(" LPAD(A.MONTH,2,0) = TO_CHAR(SYSDATE,'MM') AND A.YEAR = TO_CHAR(SYSDATE,'YYYY') AND A.TYPE=1 AND LEVEL_ID=3 ");
            workImplemented.append(" and nvl(A.COMPLETE_PERCENT,0) !=0 and A.CONSTRUCTION_ID = :constructionId) ");
            SQLQuery queryWorkImplemented = getSession().createSQLQuery(workImplemented.toString());
            
//          cap nhat construction ve status=2
            StringBuilder constr = new StringBuilder();
            constr.append(" update construction set status=2 where CONSTRUCTION_ID = :constructionId ");
            SQLQuery queryCons = getSession().createSQLQuery(constr.toString());
          
            queryWorkUnImplemented.setParameter("constructionId", constructionId);
            queryWorkImplemented.setParameter("constructionId", constructionId);
            query.setParameter("constructionId", constructionId);
            query2.setParameter("constructionId", constructionId);
            queryCons.setParameter("constructionId", constructionId);
            query.executeUpdate();
            query2.executeUpdate();
            queryWorkUnImplemented.executeUpdate();
            queryWorkImplemented.executeUpdate();
            queryCons.executeUpdate();
            getSession().flush();
        
    }
    //HuyPQ-20190524-start
    public ConstructionDTO getHouseCodeByConsId(String code) {
    	StringBuilder sql = new StringBuilder("SELECT cons.CONSTRUCTION_ID constructionId, " + 
    			"csh.code catStationHouseCode  " + 
    			"FROM CONSTRUCTION cons " + 
    			"left join CTCT_CAT_OWNER.CAT_STATION cs on cs.CAT_STATION_ID = cons.CAT_STATION_ID " + 
    			"left join CTCT_CAT_OWNER.CAT_STATION_HOUSE csh on csh.CAT_STATION_HOUSE_ID = cs.CAT_STATION_HOUSE_ID " + 
    			"where cons.code=:code");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.addScalar("constructionId", new LongType());
    	query.addScalar("catStationHouseCode", new StringType());
    	query.setResultTransformer(Transformers.aliasToBean(ConstructionDTO.class));
    	query.setParameter("code", code);
    	@SuppressWarnings("unchecked")
        List<ConstructionDTO> lst = query.list();
        if (lst.size() > 0) {
            return lst.get(0);
        }
        return null;
    }

    //HuyPQ-end

    //hienvd: created 3/7/2019
    public List<AssignHandoverDTO> doSearchKPI(AssignHandoverDTO criteria) {

        StringBuilder sql = new StringBuilder("SELECT " +
                "a.ASSIGN_HANDOVER_ID assignHandoverId, " +
                "a.SYS_GROUP_NAME sysGroupName, " +
                "a.CAT_PROVINCE_CODE catProvinceCode, " +
                "a.CAT_STATION_HOUSE_CODE catStationHouseCode, " +
                "a.CAT_STATION_CODE catStationCode, " +
                "a.CONSTRUCTION_CODE constructionCode, " +
                "a.CNT_CONTRACT_CODE cntContractCode, " +
                "a.IS_DESIGN isDesign, " +
                "a.COMPANY_ASSIGN_DATE companyAssignDate, "+
                "c.NAME constructTypeName, " +
                "b.HANDOVER_DATE_BUILD handoverDate, " +
                "b.STARTING_DATE startDate, " +
//                hoanm1_20190806_start
                " case when ( trunc(nvl(HANDOVER_DATE_BUILD,sysdate)) - trunc(Company_assign_date)>5 ) then 1 else 0 end viphammb, " +
                " case when (trunc(nvl(b.STARTING_DATE,sysdate))- trunc(b.HANDOVER_DATE_BUILD) > 7) then 1 else 0 end viphamkc,   " +
                " case when  a.Station_type =1 and (trunc(nvl(b.complete_date,sysdate))-trunc(b.Starting_date)-45)> 0 then 1 "
                + " when a.Station_type =2 and (trunc(nvl(b.complete_date,sysdate))-trunc(b.Starting_date)-30)>0 then 1 else 0 end viphamtc, " +
//                hoanm1_20190806_end
                " a.DESCRIPTION description ");

        if (criteria.getListCatConstructionType() != null && !criteria.getListCatConstructionType().isEmpty()) {
            sql.append("FROM ASSIGN_HANDOVER a ");
            sql.append("INNER JOIN construction b on a.construction_id = b.construction_id");
            sql.append(" LEFT JOIN CAT_CONSTRUCTION_TYPE c on b.CAT_CONSTRUCTION_TYPE_id = c.CAT_CONSTRUCTION_TYPE_id " +
                    "WHERE 1=1 ");
            sql.append("AND b.cat_construction_type_id in (:listConsType) ");
        } else {
            sql.append("FROM ASSIGN_HANDOVER a ");
            sql.append("INNER JOIN construction b on a.construction_id = b.construction_id ");
            sql.append("LEFT JOIN CAT_CONSTRUCTION_TYPE c on b.CAT_CONSTRUCTION_TYPE_id = c.CAT_CONSTRUCTION_TYPE_id ");
            sql.append("WHERE 1=1 AND a.STATUS = 1");
        }
//        sql.append(" WHERE a.SYS_GROUP_NAME is not null ");
        sql.append(" AND a.SYS_GROUP_NAME is not null ");
        sql.append(" AND a.CONSTRUCTION_CODE is not null ");

        //query by keySearch: Mã công trình/trạm/tỉnh/kế hoạch/hợp đồng
        if (StringUtils.isNotEmpty(criteria.getKeySearch())) {
            sql.append("AND (" +
                    "upper(a.CONSTRUCTION_CODE) LIKE upper(:keySearch) " +
                    "OR upper(a.CAT_STATION_CODE) LIKE upper(:keySearch) " +
                    "OR upper(a.CAT_PROVINCE_CODE) LIKE upper(:keySearch) " +
                    "OR upper(a.CNT_CONTRACT_CODE) LIKE upper(:keySearch) escape '&') ");
        }

        if (null != criteria.getSysGroupId()) {
            sql.append("AND a.SYS_GROUP_ID LIKE :sysGroupId ");
        } else if (StringUtils.isNotEmpty(criteria.getText())) {
            sql.append("AND upper(a.SYS_GROUP_CODE || '_' || a.SYS_GROUP_NAME) like upper(:text) escape '&' ");
        }

        if (null != criteria.getDateFrom()) {
            sql.append("AND TRUNC(a.COMPANY_ASSIGN_DATE) >=:dateFrom ");
        }
        if (null != criteria.getDateTo()) {
            sql.append("AND TRUNC(a.COMPANY_ASSIGN_DATE) <=:dateTo ");
        }

        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        sql.append("ORDER BY assignHandoverId desc ");
        SQLQuery query = this.getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = this.getSession().createSQLQuery(sqlCount.toString());

        if (criteria.getListCatConstructionType() != null && !criteria.getListCatConstructionType().isEmpty()) {
            query.setParameterList("listConsType", criteria.getListCatConstructionType());
            queryCount.setParameterList("listConsType", criteria.getListCatConstructionType());
        }
        if (StringUtils.isNotEmpty(criteria.getKeySearch())) {
            query.setParameter("keySearch", "%" + criteria.getKeySearch() + "%");
            queryCount.setParameter("keySearch", "%" + criteria.getKeySearch() + "%");
        }
        if (null != criteria.getSysGroupId()) {
            query.setParameter("sysGroupId", criteria.getSysGroupId());
            queryCount.setParameter("sysGroupId", criteria.getSysGroupId());
        } else if (StringUtils.isNotEmpty(criteria.getText())) {
            query.setParameter("text", "%" + criteria.getText() + "%");
            queryCount.setParameter("text", "%" + criteria.getText() + "%");
        }
        if (null != criteria.getDateFrom()) {
            query.setParameter("dateFrom", criteria.getDateFrom());
            queryCount.setParameter("dateFrom", criteria.getDateFrom());
        }
        if (null != criteria.getDateTo()) {
            query.setParameter("dateTo", criteria.getDateTo());
            queryCount.setParameter("dateTo", criteria.getDateTo());
        }
        query.setResultTransformer(Transformers.aliasToBean(AssignHandoverDTO.class));
        query.addScalar("assignHandoverId", new LongType());
        query.addScalar("sysGroupName", new StringType());
        query.addScalar("catProvinceCode", new StringType());
        query.addScalar("catStationHouseCode", new StringType());
        query.addScalar("catStationCode", new StringType());
        query.addScalar("constructionCode", new StringType());
        query.addScalar("cntContractCode", new StringType());
        query.addScalar("isDesign", new LongType());
        query.addScalar("companyAssignDate", new DateType());
        query.addScalar("constructTypeName", new StringType());
        query.addScalar("handoverDate", new DateType());
        query.addScalar("startDate", new DateType());
        query.addScalar("viphamMB", new StringType());
        query.addScalar("viphamKC", new StringType());
        query.addScalar("viphamTC", new StringType());
        query.addScalar("description", new StringType());
        if (criteria.getPage() != null && criteria.getPageSize() != null) {
            query.setFirstResult((criteria.getPage().intValue() - 1) * criteria.getPageSize());
            query.setMaxResults(criteria.getPageSize());
        }
        criteria.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());

        return query.list();
    }
//    hienvd: end
    
    //Huypq-20190828-start
    public List<AssignHandoverDTO> getDataReportStartInMonth(AssignHandoverDTO obj){
    	StringBuilder sql = new StringBuilder("SELECT a.SYS_GROUP_NAME sysGroupName, " + 
    			"  a.CAT_PROVINCE_CODE catProvinceCode, " + 
    			"  a.CAT_STATION_HOUSE_CODE catStationHouseCode, " + 
    			"  a.CAT_STATION_CODE catStationCode, " + 
    			"  a.CNT_CONTRACT_CODE cntContractCode, " + 
    			"  a.CONSTRUCTION_CODE constructionCode, " + 
    			"  c.NAME constructTypeName, " + 
    			"  a.COMPANY_ASSIGN_DATE companyAssignDateConvert, " + 
    			"  b.HANDOVER_DATE_BUILD handoverDateConvert, " + 
    			"  b.STARTING_DATE startDateConvert " + 
    			"  FROM ASSIGN_HANDOVER a " + 
    			"  INNER JOIN construction b " + 
    			"  ON a.construction_id = b.construction_id " + 
    			"  LEFT JOIN CAT_CONSTRUCTION_TYPE c " + 
    			"  ON b.CAT_CONSTRUCTION_TYPE_id = c.CAT_CONSTRUCTION_TYPE_id " + 
    			"  WHERE a.status                =1 ");
    	if(StringUtils.isNotBlank(obj.getKeySearch())) {
    		sql.append(" AND (upper(a.CNT_CONTRACT_CODE) like upper(:keySearch) escape '&' "
    				+ " OR upper(a.CONSTRUCTION_CODE) like upper(:keySearch) escape '&' "
    				+ " OR upper(a.CAT_STATION_CODE) like upper(:keySearch) escape '&' "
    				+ " OR upper(a.CAT_STATION_HOUSE_CODE) like upper(:keySearch) escape '&') ");
    	}
    	
    	if(obj.getListCatConstructionType()!=null && !obj.getListCatConstructionType().isEmpty()) {
    		sql.append(" AND c.cat_construction_type_id in (:listConsType) ");
    	}
    	
    	if(obj.getSysGroupId()!=null) {
    		sql.append(" AND a.SYS_GROUP_ID = :sysGroupId ");
    	}
    	
    	if(obj.getDateFrom()!=null) {
    		sql.append(" AND b.STARTING_DATE >= :dateFrom ");
    	}
    	
    	if(obj.getDateTo()!=null) {
    		sql.append(" AND b.STARTING_DATE <= :dateTo ");
    	}
    	
    	sql.append(" ORDER BY a.ASSIGN_HANDOVER_ID desc ");
    	
    	StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(sql.toString());
        sqlCount.append(")");
        SQLQuery query = this.getSession().createSQLQuery(sql.toString());
        SQLQuery queryCount = this.getSession().createSQLQuery(sqlCount.toString());
        
    	query.addScalar("sysGroupName", new StringType());
        query.addScalar("catProvinceCode", new StringType());
        query.addScalar("catStationHouseCode", new StringType());
        query.addScalar("catStationCode", new StringType());
        query.addScalar("constructionCode", new StringType());
        query.addScalar("cntContractCode", new StringType());
        query.addScalar("companyAssignDateConvert", new DateType());
        query.addScalar("constructTypeName", new StringType());
        query.addScalar("handoverDateConvert", new DateType());
        query.addScalar("startDateConvert", new DateType());
        
        query.setResultTransformer(Transformers.aliasToBean(AssignHandoverDTO.class));
        
        if(StringUtils.isNotBlank(obj.getKeySearch())) {
        	query.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
        	queryCount.setParameter("keySearch", "%" + obj.getKeySearch() + "%");
        }
    	
    	if(obj.getListCatConstructionType()!=null && !obj.getListCatConstructionType().isEmpty()) {
    		query.setParameterList("listConsType", obj.getListCatConstructionType());
    		queryCount.setParameterList("listConsType", obj.getListCatConstructionType());
    	}
    	
    	if(obj.getSysGroupId()!=null) {
    		query.setParameter("sysGroupId", obj.getSysGroupId());
    		queryCount.setParameter("sysGroupId", obj.getSysGroupId());
    	}
    	
    	if(obj.getDateFrom()!=null) {
    		query.setParameter("dateFrom", obj.getDateFrom());
    		queryCount.setParameter("dateFrom", obj.getDateFrom());
    	}
    	
    	if(obj.getDateTo()!=null) {
    		query.setParameter("dateTo", obj.getDateTo());
    		queryCount.setParameter("dateTo", obj.getDateTo());
    	}
        
    	if (obj.getPage() != null && obj.getPageSize() != null) {
            query.setFirstResult((obj.getPage().intValue() - 1) * obj.getPageSize());
            query.setMaxResults(obj.getPageSize());
        }
    	obj.setTotalRecord(((BigDecimal) queryCount.uniqueResult()).intValue());
    	
        return query.list();
    }
    
    public List<AssignHandoverDTO> findConstructionContractRef(List<String> lstCons) {
        String sql = "SELECT " +
                "cnt.cnt_contract_id cntContractId, " +
                "cnt.code cntContractCode, " +
                "ct.construction_id constructionId, " +
                "ct.code constructionCode " +
                "FROM construction ct " +
                "LEFT JOIN cnt_constr_work_item_task t ON ct.construction_id = t.construction_id " +
                "LEFT JOIN cnt_contract cnt ON cnt.cnt_contract_id = t.cnt_contract_id " +
                "WHERE 1=1 " +
                "AND cnt.code IS NOT NULL " +
                "AND ct.code IS NOT NULL ";

        if (null != lstCons && lstCons.size()>0) {
            sql += "AND upper(ct.code) = (:lstCons) ";
        }

        SQLQuery query = super.getSession().createSQLQuery(sql);
        if (null != lstCons && lstCons.size()>0) {
            query.setParameterList("lstCons", lstCons);
        }

        query.addScalar("cntContractId", new LongType());
        query.addScalar("cntContractCode", new StringType());
        query.addScalar("constructionId", new LongType());
        query.addScalar("constructionCode", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(AssignHandoverDTO.class));
        return query.list();
    }
    //Huy-end
}
