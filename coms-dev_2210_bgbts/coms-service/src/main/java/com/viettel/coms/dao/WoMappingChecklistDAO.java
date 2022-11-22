package com.viettel.coms.dao;

import com.viettel.coms.bo.WoBO;
import com.viettel.coms.bo.WoMappingChecklistBO;
import com.viettel.coms.dto.WoMappingChecklistDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class WoMappingChecklistDAO extends BaseFWDAOImpl<WoMappingChecklistBO, Long> {
    public WoMappingChecklistDAO() {
        this.model = new WoMappingChecklistBO();
    }

    public WoMappingChecklistDAO(Session session) {
        this.session = session;
    }

    public int deleteCheckList(Long id) {
        StringBuilder sql = new StringBuilder("UPDATE WO_MAPPING_CHECKLIST set status = 0  where ID = :id");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    public List<WoMappingChecklistBO> getCheckListForCreateWo(long catWorkItemTypeId, long woId, boolean isGpon, Long constructionId) {
        StringBuilder sql = new StringBuilder(" select :woId as woId, 'NEW' as state, 1 as status, ");

//        if (isGpon) {
//            sql.append(" WORK_ITEM_GPON_ID as checkListId, case when price is not null and amount is not null then 1 end as quantityByDate from WORK_ITEM_GPON ");
//        } else {
//            sql.append(" CAT_TASK_ID as checkListId , QUANTITY_BY_DATE as quantityByDate from CAT_TASK ");
//        }

        sql.append(" CAT_TASK_ID as checkListId , QUANTITY_BY_DATE as quantityByDate from CAT_TASK ");

        sql.append(" where CAT_WORK_ITEM_TYPE_ID = :catWorkItemTypeId and STATUS > 0 ");

//        if (isGpon) {
//            sql.append(" and CONSTRUCTION_ID  = :constructionId");
//        }

        SQLQuery query = getSession().createSQLQuery(sql.toString());

//        if (isGpon) {
//            query.setParameter("constructionId", constructionId);
//        }

        query.setParameter("woId", woId);
        query.setParameter("catWorkItemTypeId", catWorkItemTypeId);

        query.addScalar("woId", new LongType());
        query.addScalar("checkListId", new LongType());
        query.addScalar("state", new StringType());
        query.addScalar("status", new LongType());
        query.addScalar("quantityByDate", new StringType());

        query.setResultTransformer(Transformers.aliasToBean(WoMappingChecklistBO.class));
        return query.list();
    }

    public List<WoMappingChecklistDTO> getCheckListOfWo(long woId) {
        StringBuilder sql = new StringBuilder(" select ct.CAT_TASK_ID as checkListId, ct.NAME as name, mp.WO_ID as woId, mp.STATE as state, mp.NUM_IMG_REQUIRE numImgRequire " +
                " from WO_MAPPING_CHECKLIST mp " +
                " LEFT JOIN CAT_TASK ct on mp.CHECKLIST_ID = ct.CAT_TASK_ID " +
                " where mp.WO_ID = :woId ");

        SQLQuery query = getSession().createSQLQuery(sql.toString());

        query.setParameter("woId", woId);

        query.addScalar("woId", new LongType());
        query.addScalar("checkListId", new LongType());
        query.addScalar("state", new StringType());
        query.addScalar("name", new StringType());
        query.addScalar("numImgRequire", new IntegerType());

        query.setResultTransformer(Transformers.aliasToBean(WoMappingChecklistDTO.class));

        return query.list();
    }

    public List<WoMappingChecklistBO> getAIOChecklistItemByContract(long contractId, long woId) {
        StringBuilder sql = new StringBuilder(" select :woId as woId, 'NEW' as state, 1 as status, " +
                " CONTRACT_DETAIL_ID as checkListId from AIO_CONTRACT_DETAIL " +
                " where CONTRACT_ID = :contractId and STATUS>0 ");

        SQLQuery query = getSession().createSQLQuery(sql.toString());

        query.setParameter("contractId", contractId);
        query.setParameter("woId", woId);

        query.addScalar("woId", new LongType());
        query.addScalar("checkListId", new LongType());
        query.addScalar("state", new StringType());
        query.addScalar("status", new LongType());

        query.setResultTransformer(Transformers.aliasToBean(WoMappingChecklistBO.class));

        return query.list();
    }

    public int acceptChecklistQuantity(WoMappingChecklistDTO dto) {
        StringBuilder sql = new StringBuilder("UPDATE WO_MAPPING_CHECKLIST set " +
                " QUANTITY_LENGTH = NVL(QUANTITY_LENGTH,0) + :unapprovedQuantity " +
                " where ID = :wmcId ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("wmcId", dto.getId());
        query.setParameter("unapprovedQuantity", dto.getUnapprovedQuantity());
        return query.executeUpdate();
    }

    public WoMappingChecklistBO getOneRaw(Long id) {
        return this.get(WoMappingChecklistBO.class, id);
    }

    public List<WoMappingChecklistDTO> doSearch(WoMappingChecklistDTO dto) {
        String sql = "select id, wo_id woId, checklist_id checklistId, state, status, QUANTITY_BY_DATE quantityByDate, " +
                " QUANTITY_LENGTH quantityLength, ADDED_QUANTITY_LENGTH addedQuantityLength, name, NUM_IMG_REQUIRE numImgRequire " +
                " from WO_MAPPING_CHECKLIST where status >0 ";

        if (dto.getWoId() != null) sql += " and wo_id =:woId ";
        if (StringUtils.isNotEmpty(dto.getName())) sql += " and name = :name ";

        SQLQuery query = getSession().createSQLQuery(sql);

        if (dto.getWoId() != null) query.setParameter("woId", dto.getWoId());
        if (StringUtils.isNotEmpty(dto.getName())) query.setParameter("name", dto.getName());

        query.addScalar("id", new LongType());
        query.addScalar("woId", new LongType());
        query.addScalar("checklistId", new LongType());
        query.addScalar("state", new StringType());
        query.addScalar("status", new LongType());
        query.addScalar("quantityByDate", new StringType());
        query.addScalar("quantityLength", new DoubleType());
        query.addScalar("addedQuantityLength", new DoubleType());
        query.addScalar("name", new StringType());
        query.addScalar("numImgRequire", new IntegerType());

        query.setResultTransformer(Transformers.aliasToBean(WoMappingChecklistDTO.class));

        return query.list();
    }

    /**
     * requestValue: request san luong
     *
     * @param dto
     * @return
     */
    public int requestValue(WoMappingChecklistDTO dto) {
        StringBuilder sql = new StringBuilder("UPDATE wo_xddd_checklist set " +
                " STATE = 'DONE', VALUE = :value " +
                " where ID = :id ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("id", dto.getId());
        query.setParameter("value", dto.getValue());
        return query.executeUpdate();
    }

    public List<WoMappingChecklistDTO> getMappingCheclistByWoId(long woId) {
        StringBuilder sql = new StringBuilder(" select wo_id woId, checklist_id checklistId, state, name, TTHQ_RESULT tthqResult, ACTUAL_VALUE actualValue " +
                " from WO_MAPPING_CHECKLIST where WO_ID = :woId ");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("woId", woId);

        query.addScalar("woId", new LongType());
        query.addScalar("checklistId", new LongType());
        query.addScalar("state", new StringType());
        query.addScalar("name", new StringType());
        query.addScalar("tthqResult", new StringType());
        query.addScalar("actualValue", new LongType());
        query.setResultTransformer(Transformers.aliasToBean(WoMappingChecklistDTO.class));

        return query.list();
    }

    public WoMappingChecklistDTO getDetail(Long woId, Long checklistId) {
        StringBuilder sql = new StringBuilder("  select\n" +
                "    wmc.CLASS_ID classId\n" +
                "    , ap.NAME className\n" +
                "    , wcq.QOUTA_VALUE defaultClassValue\n" +
                "    , wmc.ACTUAL_VALUE actualValue\n" +
                "from WO_MAPPING_CHECKLIST wmc\n" +
                "left join APP_PARAM ap ON '' || wmc.CLASS_ID = ap.CODE and ap.par_type = 'CLASS_DETAIL'\n" +
                "left join WO_CLASS_QOUTA wcq ON wcq.CHECKLIST_NAME = wmc.NAME\n" +
                "where wmc.CHECKLIST_ID = :checklistId  and wmc.WO_ID = :woId ");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("woId", woId);
        query.setParameter("checklistId", checklistId);

        query.addScalar("classId", new LongType());
        query.addScalar("className", new StringType());
        query.addScalar("defaultClassValue", new LongType());
        query.addScalar("actualValue", new LongType());

        query.setResultTransformer(Transformers.aliasToBean(WoMappingChecklistDTO.class));
        List<WoMappingChecklistDTO> lst = query.list();

        return lst != null && lst.size() > 0 ? lst.get(0) : null;
    }
    
    public void updateStateMappingCheckList(Long woId) {
    	StringBuilder sql = new StringBuilder("UPDATE WO_MAPPING_CHECKLIST set STATE='DONE' where WO_ID=:woId ");
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	query.setParameter("woId", woId);
    	query.executeUpdate();
    }

    public List<WoMappingChecklistDTO> findByWoId(Long woId) {
        String sql = "SELECT * FROM  WO_MAPPING_CHECKLIST wmc WHERE wmc.WO_ID = :woId";
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("woId", woId);
        query.addScalar("state", new StringType());


        query.setResultTransformer(Transformers.aliasToBean(WoMappingChecklistDTO.class));

        return query.list();
    }

    public List<WoMappingChecklistBO> findByWoIdEntity(Long woId) {
        String sql = "SELECT * FROM  WO_MAPPING_CHECKLIST wmc WHERE wmc.WO_ID = :woId And STATUS = 1";
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.addEntity(WoMappingChecklistBO.class);
        query.setParameter("woId", woId);
        return query.list();
    }
}
