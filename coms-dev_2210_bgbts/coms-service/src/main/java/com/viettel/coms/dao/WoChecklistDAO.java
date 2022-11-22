package com.viettel.coms.dao;

import com.viettel.coms.bo.WoChecklistBO;
import com.viettel.coms.dto.WoChecklistDTO;
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
public class WoChecklistDAO extends BaseFWDAOImpl<WoChecklistBO, Long> {

    public WoChecklistDAO() {
        this.model = new WoChecklistBO();
    }

    public WoChecklistDAO(Session session) {
        this.session = session;
    }

    public WoChecklistBO getOneRaw(long checklistId){
        return this.get(WoChecklistBO.class, checklistId);
    }

    public int delete(Long checklistId) {
        StringBuilder sql = new StringBuilder("UPDATE WO_CHECKLIST set status = 0  where ID = :checklistId");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("checklistId", checklistId);
        return query.executeUpdate();
    }

    public List<WoChecklistDTO> doSearch(WoChecklistDTO dto){
        String sql = "select id as checklistId, wo_id as woId, checklist_name as checklistName, type as type, object_id as objectId, " +
                " dnqt_date as dnqtDate, dnqt_value as dnqtValue, vtnet_send_date as vtnetSendDate, vtnet_sent_value as vtnetSentValue, vtnet_confirm_date as vtnetConfirmDate, vtnet_confirm_value as vtnetConfirmValue, " +
                " aproved_doc_date as aprovedDocDate, aprove_doc_value as aprovedDocValue, aproved_person as aprovedPerson, status as status, state as state, checklist_order as checklistOrder, " +
                " nvl(has_problem,0) as hasProblem, resolve_due_date as resolveDueDate, complete_person_id as completePersonId, complete_person_name as completePersonName, " +
                " completed_date as completedDate, content as content, problem_date as problemDate, problem_code as problemCode, problem_name as problemName, " +
                " problem_declare_person_id as problemDeclarePersonId, problem_declare_person_name as problemDeclarePersonName, final_date as finalDate, final_value as finalValue, " +
                " customer_confirm_date as customerConfirmDate, reject_reason as rejectReason, product_code as productCode, code,  " +
                " cat_stock_id as catStockId, cat_stock_name as catStockName,settop_box as settopBox, smart_card as smartCard " +
                " from wo_checklist where status > 0 ";

        if(dto.getId()!= null ) sql+= " and id = :checklistId ";
        if(dto.getAprovedDocDate()!= null ) sql+= " and aproved_Date = :aprovedDate ";
        if(dto.getAprovedPerson()!= null ) sql+= " and aproved_Person = :aprovedPerson ";
        if(dto.getAprovedDocValue()!= null ) sql+= " and aproved_Value = :aprovedValue ";
        if(dto.getType()!= null ) sql+= " and type = :type ";
        if(dto.getObjectId()!= null ) sql+= " and object_id = :objectId ";
        if(dto.getDnqtDate()!= null ) sql+= " and dnqt_date = :dnqtDate ";
        if(dto.getVtnetSendDate()!= null ) sql+= " and vtnet_Send_Date = :vtnetSendDate ";
        if(dto.getVtnetConfirmDate()!= null ) sql+= " and vtnet_Confirm_Date = :vtnetConfirmDate ";
        if(StringUtils.isNotEmpty(dto.getState())) sql+= " and state = :state ";
        if(dto.getWoId()!=null) sql+= " and wo_id = :woId ";
        if(dto.getCustomerConfirmDate() !=null) sql+= " and customer_confirm_date = :customerConfirmDate ";
        if(dto.getRejectReason() !=null) sql+= " and reject_reason = :rejectReason ";
        if(dto.getProductCode() !=null) sql+= " and product_code = :productCode ";

        sql+= "order by checklist_order";

        SQLQuery query = getSession().createSQLQuery(sql);

        if(dto.getId()!= null ) query.setParameter("checklistId", dto.getId());
        if(dto.getAprovedDocDate()!= null ) query.setParameter("aprovedDate",dto.getAprovedDocDate());
        if(dto.getAprovedPerson()!= null ) query.setParameter("aprovedPerson", dto.getAprovedPerson());
        if(dto.getAprovedDocValue()!= null ) query.setParameter("aprovedValue", dto.getAprovedDocValue());
        if(dto.getType()!= null ) query.setParameter("type", dto.getType());
        if(dto.getObjectId()!= null ) query.setParameter("objectId", dto.getObjectId());
        if(dto.getDnqtDate()!= null ) query.setParameter("dnqtDate", dto.getDnqtDate());
        if(dto.getVtnetSendDate()!= null ) query.setParameter("vtnetSendDate", dto.getVtnetSendDate());
        if(dto.getVtnetConfirmDate()!= null ) query.setParameter("vtnetConfirmDate", dto.getVtnetConfirmDate());
        if(StringUtils.isNotEmpty(dto.getState())) query.setParameter("state", dto.getState());
        if(dto.getWoId()!=null) query.setParameter("woId", dto.getWoId());
        if(dto.getCustomerConfirmDate() !=null) query.setParameter("customerConfirmDate", dto.getCustomerConfirmDate());
        if(dto.getRejectReason() !=null) query.setParameter("rejectReason", dto.getRejectReason());
        if(dto.getProductCode() !=null) query.setParameter("productCode", dto.getProductCode());


        query.addScalar("checklistId", new LongType());
        query.addScalar("woId", new LongType());
        query.addScalar("checklistName", new StringType());
        query.addScalar("type", new LongType());
        query.addScalar("objectId", new LongType());
        query.addScalar("dnqtDate", new DateType());
        query.addScalar("dnqtValue", new DoubleType());
        query.addScalar("vtnetSendDate", new DateType());
        query.addScalar("vtnetSentValue", new DoubleType());
        query.addScalar("vtnetConfirmDate", new DateType());
        query.addScalar("vtnetConfirmValue", new DoubleType());
        query.addScalar("aprovedDocDate", new DateType());
        query.addScalar("aprovedDocValue", new DoubleType());
        query.addScalar("finalDate", new DateType());
        query.addScalar("finalValue", new DoubleType());
        query.addScalar("aprovedPerson", new StringType());
        query.addScalar("status", new LongType());
        query.addScalar("state", new StringType());
        query.addScalar("checklistOrder", new LongType());
        query.addScalar("hasProblem", new LongType());
        query.addScalar("resolveDueDate", new DateType());
        query.addScalar("completePersonId", new LongType());
        query.addScalar("completePersonName", new StringType());
        query.addScalar("completedDate", new DateType());
        query.addScalar("content", new StringType());
        query.addScalar("problemDate", new DateType());
        query.addScalar("problemCode", new StringType());
        query.addScalar("problemName", new StringType());
        query.addScalar("problemDeclarePersonId", new LongType());
        query.addScalar("problemDeclarePersonName", new StringType());
        query.addScalar("customerConfirmDate", new StringType());
        query.addScalar("rejectReason", new StringType());
        query.addScalar("productCode", new StringType());
        query.addScalar("code", new StringType());

        query.setResultTransformer(Transformers.aliasToBean(WoChecklistDTO.class));

        return query.list();
    }

    public List<WoChecklistDTO> findByWoId(Long woId) {
        StringBuilder sql = new StringBuilder("SELECT\n" +
                "    id checklistId,\n" +
                "    wo_id woId,\n" +
                "    checklist_name checklistName,\n" +
                "    type,\n" +
                "    object_id objectId,\n" +
                "    dnqt_date dnqtDate,\n" +
                "    dnqt_value dnqtValue,\n" +
                "    vtnet_send_date vtnetSendDate,\n" +
                "    vtnet_confirm_date vtnetConfirmDate,\n" +
                "    APROVED_DOC_DATE aprovedDocDate,\n" +
                "    VTNET_CONFIRM_VALUE vtnetConfirmValue,\n" +
                "    aproved_person aprovedPerson,\n" +
                "    state,\n" +
                "    COMPLETE_PERSON_ID completePersonId,\n" +
                "    COMPLETE_PERSON_NAME completePersonName,\n" +
                "    COMPLETED_DATE completedDate,\n" +
                "    content,\n" +
                "    PROBLEM_DATE problemDate,\n" +
                "    PROBLEM_CODE problemCode,\n" +
                "    PROBLEM_NAME problemName,\n" +
                "    PROBLEM_DECLARE_PERSON_ID problemDeclarePersonId,\n" +
                "    PROBLEM_DECLARE_PERSON_NAME problemDeclarePersonName,\n" +
                "    HAS_PROBLEM hasProblem,\n" +
                "    RESOLVE_DUE_DATE resolveDueDate,\n" +
                "    CHECKLIST_ORDER checklistOrder,\n" +
                "    status\n" +
                "FROM wo_checklist\n" +
                "WHERE wo_id = :woId");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("woId", woId);
        // Add scalar
        query.addScalar("checklistId", new LongType());
        query.addScalar("woId", new LongType());
        query.addScalar("checklistName", new StringType());
        query.addScalar("type", new LongType());
        query.addScalar("objectId", new LongType());
        query.addScalar("dnqtDate", new DateType());
        query.addScalar("dnqtValue", new DoubleType());
        query.addScalar("vtnetSendDate", new DateType());
        query.addScalar("vtnetConfirmDate", new DateType());
        query.addScalar("aprovedDocDate", new DateType());
        query.addScalar("vtnetConfirmValue", new DoubleType());
        query.addScalar("aprovedPerson", new StringType());
        query.addScalar("state", new StringType());
        query.addScalar("completePersonId", new LongType());
        query.addScalar("completePersonName", new StringType());
        query.addScalar("completedDate", new DateType());
        query.addScalar("content", new StringType());
        query.addScalar("problemDate", new DateType());
        query.addScalar("problemCode", new StringType());
        query.addScalar("problemName", new StringType());
        query.addScalar("problemDeclarePersonId", new LongType());
        query.addScalar("problemDeclarePersonName", new StringType());
        query.addScalar("hasProblem", new LongType());
        query.addScalar("resolveDueDate", new DateType());
        query.addScalar("checklistOrder", new LongType());
        query.addScalar("status", new LongType());

        query.setResultTransformer(Transformers.aliasToBean(WoChecklistDTO.class));
        return query.list();
    }
}
