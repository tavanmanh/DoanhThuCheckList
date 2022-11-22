package com.viettel.coms.dao;

import com.viettel.coms.bo.WoXdddChecklistBO;
import com.viettel.coms.dto.WoMappingChecklistDTO;
import com.viettel.coms.dto.WoXdddChecklistDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class WoXdddChecklistDAO extends BaseFWDAOImpl<WoXdddChecklistBO, Long> {
    public WoXdddChecklistDAO() {
        this.model = new WoXdddChecklistBO();
    }

    public WoXdddChecklistDAO(Session session) {
        this.session = session;
    }

    private SQLQuery mapFields(SQLQuery query){
        query.addScalar("id", new LongType());
        query.addScalar("woId", new LongType());
        query.addScalar("state", new StringType());
        query.addScalar("status", new LongType());
        query.addScalar("value", new DoubleType());
        query.addScalar("name", new StringType());
        query.addScalar("confirm", new LongType());
        query.addScalar("confirmDate", new DateType());
        query.addScalar("confirmBy", new StringType());
        query.addScalar("hshc", new LongType());
        return query;
    }

    public WoXdddChecklistBO getOneRaw(long id) {
        return this.get(WoXdddChecklistBO.class, id);
    }

    public Long getNextSeqVal(){
        return this.getNextValSequence("WO_XDDD_CHECKLIST_SEQ");
    }

    public int delete(Long id) {
        StringBuilder sql = new StringBuilder("UPDATE WO_XDDD_CHECKLIST set status = 0 where ID = :id");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("id", id);

        return query.executeUpdate();
    }

    public List<WoXdddChecklistDTO> doSearch(WoXdddChecklistDTO obj){
        String sql = "Select id, wo_id woId, state, status, value, name, confirm, confirm_date confirmDate, confirm_by confirmBy, hshc " +
                " from WO_XDDD_CHECKLIST where status != 0 ";
        if(obj.getWoId()!=null) sql += " and wo_id = :woId ";
        if(StringUtils.isNotEmpty(obj.getName())) sql += " and lower(name) = :name ";
        sql += " order by id ";
        SQLQuery query = getSession().createSQLQuery(sql);
        if(obj.getWoId()!=null) query.setParameter("woId", obj.getWoId());
        if(StringUtils.isNotEmpty(obj.getName())) query.setParameter("name", obj.getName());

        query = mapFields(query);

        query.setResultTransformer(Transformers.aliasToBean(WoXdddChecklistDTO.class));

        return query.list();
    }

    public List<WoXdddChecklistDTO> getXdddChecklistByWorkItem(Long constructionId, Long catWorkItemTypeId, Long hshcWoId){
        String sql = "Select b.id, b.wo_id woId, b.state, b.status, b.value, b.name, b.confirm, b.confirm_date confirmDate, b.confirm_by confirmBy, b.hshc " +
                " from WO a left join WO_XDDD_CHECKLIST b on a.id = b.wo_id where a.construction_id = :constructionId and a.cat_work_item_type_id = :catWorkItemTypeId " +
                " and a.status>0 and b.status>0 ";

        if(hshcWoId != null){
            sql += " and b.hshc = :hshcWoId";
        }

        SQLQuery query = getSession().createSQLQuery(sql);

        query.setParameter("constructionId", constructionId);
        query.setParameter("catWorkItemTypeId", catWorkItemTypeId);
        if(hshcWoId != null){
            query.setParameter("hshcWoId", hshcWoId);
        }

        query = mapFields(query);

        query.setResultTransformer(Transformers.aliasToBean(WoXdddChecklistDTO.class));

        return query.list();
    }

    public void tryUnconfirmAllQuantity(Long woId){
        String sql = "update wo_xddd_checklist set confirm = 0, confirm_by = null, confirm_date = null where wo_id = :woId and (hshc = 0 or hshc is null)";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("woId", woId);
        query.executeUpdate();
    }

    public void tryResetXdddHshc(Long woId){
        String sql = "update wo_xddd_checklist set hshc = null where hshc = :woId ";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("woId", woId);
        query.executeUpdate();
    }

    public void updateCheckWoKcXDDDInContract(Long contractId){
        String sql = "update CNT_CONTRACT set CHECK_WO_KC_XDDD = 1, CHECK_WO_KC_XDDD_DATE=sysdate where CNT_CONTRACT_ID = :contractId ";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("contractId", contractId);
        query.executeUpdate();
    }

    public List<WoXdddChecklistDTO> findByWoID(Long woId) {
        String sql = "SELECT * FROM WO_XDDD_CHECKLIST wxc WHERE wxc.WO_ID = :woId";
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("woId", woId);
        query.addScalar("state", new StringType());


        query.setResultTransformer(Transformers.aliasToBean(WoXdddChecklistDTO.class));

        return query.list();
    }
}
