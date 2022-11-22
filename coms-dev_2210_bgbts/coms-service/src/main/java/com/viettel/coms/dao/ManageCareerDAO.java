package com.viettel.coms.dao;

import com.viettel.coms.bo.ManageCareerBO;
import com.viettel.coms.dto.ManageCareerDTO;
import com.viettel.coms.dto.SysUserCOMSDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import com.viettel.wms.utils.ValidateUtils;
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
//Duonghv13-start 16092021
@Repository("manageCareerDAO")
@Transactional
public class ManageCareerDAO extends BaseFWDAOImpl<ManageCareerBO, Long> {


    public ManageCareerDAO() {
        this.model = new ManageCareerBO();
    }

    public ManageCareerDAO(Session session) {
        this.session = session;
    }

    @SuppressWarnings("unchecked")
    public List<ManageCareerDTO> doSearch(ManageCareerDTO criteria) {
        StringBuilder stringBuilder = new StringBuilder(
                "SELECT T1.CAREER_ID careerId," + "T1.CODE code,"
                        + " T1.NAME name," + "T1.WO_ID_LIST woIdList," + "T1.STATUS status,"
                		+ " (SELECT LISTAGG(wo_type_name,', ') WITHIN GROUP(ORDER BY wo_type_name ) FROM wo_type WHERE id in (select to_number(column_value) as IDs from xmltable(wo_id_list))) AS woListName"
                        + " FROM  CAREER T1 where 1=1"
                        + " and T1.STATUS in (0,1) ");
        if (null != criteria.getName()) {
            stringBuilder
                    .append("AND (upper(T1.CODE) like upper(:careerName) or upper(T1.NAME) like :careerName) ");
        }
        if(StringUtils.isNotBlank(criteria.getStatus())) {
        	stringBuilder.append("AND T1.STATUS like (:status) ");
        }
        if (StringUtils.isNotBlank(criteria.getWoIdList())) {
            stringBuilder
                    .append("AND T1.WO_ID_LIST in (:woIdList) ");
        }
        
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM (");
        sqlCount.append(stringBuilder.toString());
        sqlCount.append(")");

        SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());
        SQLQuery queryCount = getSession().createSQLQuery(sqlCount.toString());

        query.addScalar("careerId", new LongType());
        query.addScalar("code", new StringType());
        query.addScalar("name", new StringType());
        query.addScalar("woIdList", new StringType());
        query.addScalar("woListName", new StringType());
        query.addScalar("status", new StringType());

        if (null != criteria.getName()) {
            query.setParameter("careerName", "%" + criteria.getName() + "%");
            queryCount.setParameter("careerName", "%" + criteria.getName() + "%");
        }
        
        if(StringUtils.isNotBlank(criteria.getStatus())) {
            query.setParameter("status","%" +criteria.getStatus()+ "%");
            queryCount.setParameter("status","%" + criteria.getStatus()+ "%");
        }
        
        if (StringUtils.isNotBlank(criteria.getWoIdList())) {
            query.setParameter("woIdList", criteria.getWoIdList());
            queryCount.setParameter("woIdList",criteria.getWoIdList());
        }

        query.setResultTransformer(Transformers
                .aliasToBean(ManageCareerDTO.class));
        if (criteria.getPage() != null && criteria.getPageSize() != null) {
            query.setFirstResult((criteria.getPage().intValue() - 1)
                    * criteria.getPageSize().intValue());
            query.setMaxResults(criteria.getPageSize().intValue());
        }

        List ls = query.list();
        criteria.setTotalRecord(((BigDecimal) queryCount.uniqueResult())
                .intValue());
        return ls;
    }

    public ManageCareerDTO findBycode(String code) {
        StringBuilder stringBuilder = new StringBuilder(
                "select T1.CAREER_ID careerId," + "T1.CODE code,"
                        + "T1.NAME name," + "T1.WO_ID_LIST woIdList," + "T1.STATUS status "
                        + " FROM CAREER T1 "
                        + " WHERE 1=1 AND upper(T1.CODE) like upper(:code)");

        SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());

        query.addScalar("careerId", new LongType());
        query.addScalar("code", new StringType());
        query.addScalar("name", new StringType());
        query.addScalar("woIdList", new StringType());
        query.addScalar("status", new StringType());
        query.setParameter("code", code);
        query.setResultTransformer(Transformers
                .aliasToBean(ManageCareerDTO.class));

        return (ManageCareerDTO) query.uniqueResult();
    }

    

    @SuppressWarnings("unchecked")
    public ManageCareerDTO getById(Long id) {
        StringBuilder stringBuilder = new StringBuilder("SELECT ");
        stringBuilder.append("T1.T1.CAREER_ID careerId ");
        stringBuilder.append(",T1.CODE code ");
        stringBuilder.append(",T1.NAME name ");
        stringBuilder.append(",T1.WO_ID_LIST woIdList ");
        stringBuilder.append(",T1.STATUS status ");
        stringBuilder.append("FROM CAREER T1 ");
        stringBuilder
                .append("WHERE T1.CAREER_ID = :careerId ");

        SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());

        query.addScalar("careerId", new LongType());
        query.addScalar("code", new StringType());
        query.addScalar("name", new StringType());
        query.addScalar("woIdList", new StringType());
        query.addScalar("status", new StringType());
        query.setParameter("careerId", id);
        query.setResultTransformer(Transformers
                .aliasToBean(ManageCareerDTO.class));

        return (ManageCareerDTO) query.uniqueResult();
    }
    
    public Object getForAutoCompleteInSign(ManageCareerDTO obj) {
        String sql = "SELECT distinct T1.CAREER_ID careerId," + "T1.NAME name,"
                + "T1.CODE code" + " FROM CAREER T1 "
                + " WHERE 1=1 ";

        StringBuilder stringBuilder = new StringBuilder(sql);

        stringBuilder.append(" AND ROWNUM <=10 ");
        if (StringUtils.isNotEmpty(obj.getName())) {
            stringBuilder.append(
                    " AND (upper(T1.NAME) LIKE upper(:name) escape '&' OR upper(T1.CODE) LIKE upper(:name) escape '&')");
        }

        stringBuilder.append(" ORDER BY T1.CAREER_ID");

        SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());

        query.addScalar("careerId", new LongType());
        query.addScalar("name", new StringType());
        query.addScalar("code", new StringType());

        query.setResultTransformer(Transformers.aliasToBean(ManageCareerDTO.class));

        if (StringUtils.isNotEmpty(obj.getName())) {
            query.setParameter("name", "%" + ValidateUtils.validateKeySearch(obj.getName()) + "%");
        }

        return query.list();
    }

    @SuppressWarnings("unchecked")
    public List<ManageCareerDTO> findByWoIdList(Long woTypeid) {
        StringBuilder stringBuilder = new StringBuilder("SELECT ");
        stringBuilder.append("T1.CAREER_ID careerId ");
        stringBuilder.append(",T1.WO_ID_LIST woIdList ");
        stringBuilder.append(",T1.STATUS status ");
        stringBuilder.append("FROM CAREER T1 ");
        stringBuilder
                .append("WHERE T1.STATUS = '1' AND CONCAT(CONCAT(',',T1.WO_ID_LIST),',') LIKE :woTypeid ");

        SQLQuery query = getSession().createSQLQuery(stringBuilder.toString());

        query.addScalar("careerId", new LongType());
        query.addScalar("woIdList", new StringType());
        query.addScalar("status", new StringType());
        query.setParameter("woTypeid", "%," + woTypeid + ",%");
        query.setResultTransformer(Transformers
                .aliasToBean(ManageCareerDTO.class));

        return query.list();
    }
    //DUONG-end
    
}

