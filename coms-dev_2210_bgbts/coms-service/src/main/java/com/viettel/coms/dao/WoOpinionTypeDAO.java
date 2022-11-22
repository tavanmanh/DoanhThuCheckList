package com.viettel.coms.dao;

import com.viettel.coms.bo.WoOpinionTypeBO;
import com.viettel.coms.bo.WoTypeBO;
import com.viettel.coms.dto.WoDTO;
import com.viettel.coms.dto.WoOpinionTypeDTO;
import com.viettel.coms.dto.WoTypeDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class WoOpinionTypeDAO extends BaseFWDAOImpl<WoOpinionTypeBO, Long> {
    public WoOpinionTypeDAO(){this.model = new WoOpinionTypeBO();}
    public WoOpinionTypeDAO(Session session) {
        this.session = session;
    }

//    private String baseSelectStr = "select "
//            + "woTbl.ID as opinionTypeId, woTbl.OPINION_CODE as opinionCode, woTbl.OPINION_NAME as opinionName, ";
//

    public void delete(Long opinionTypeId) {
        StringBuilder sql = new StringBuilder("UPDATE WO_OPINION_TYPE set status = 0  where ID = :opinionTypeId");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("opinionTypeId", opinionTypeId);
        query.executeUpdate();
    }

    public WoOpinionTypeDTO getOneDetails(long opinionTypeId){
        StringBuilder sql = new StringBuilder(  " SELECT ID as opinionTypeId, OPINION_CODE as opinionCode, OPINION_NAME as opinionName, STATUS as status "+
                " from WO_OPINION_TYPE WHERE STATUS>0 AND ID = :paramId");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("paramId", opinionTypeId);

        query = mapFields(query);

        return (WoOpinionTypeDTO) query.uniqueResult();
    }

    public List<WoOpinionTypeDTO> doSearch(WoOpinionTypeDTO WoOpinionTypeDTO) {
        StringBuilder sql = new StringBuilder("select "
                + "ID as opinionTypeId, OPINION_CODE as opinionCode, OPINION_NAME as opinionName, STATUS as status "
                + "from WO_OPINION_TYPE WHERE STATUS>0 ");
        if (StringUtils.isNotEmpty(WoOpinionTypeDTO.getOpinionName()) ) {
            sql.append(" AND LOWER(OPINION_NAME) LIKE :opinionName ");
        }
        if (StringUtils.isNotEmpty(WoOpinionTypeDTO.getOpinionCode()) ) {
            sql.append(" AND LOWER(OPINION_CODE) LIKE :opinionCode ");
        }

        sql.append("offset :offset rows fetch next :pageSize rows only");

        int pageSize = WoOpinionTypeDTO.getPageSize();
        int offset = (WoOpinionTypeDTO.getPage().intValue()-1)* pageSize;

        SQLQuery query = getSession().createSQLQuery(sql.toString());

        query.setParameter("pageSize", pageSize);
        query.setParameter("offset", offset);
        if(StringUtils.isNotEmpty(WoOpinionTypeDTO.getOpinionName())){
            query.setParameter("opinionName", "%"+WoOpinionTypeDTO.getOpinionName().toLowerCase()+"%");
        }
        if(StringUtils.isNotEmpty(WoOpinionTypeDTO.getOpinionCode())){
            query.setParameter("opinionCode", "%"+WoOpinionTypeDTO.getOpinionCode().toLowerCase()+"%");
        }
        query = mapFields(query);

        return query.list();
    }

    private SQLQuery mapFields(SQLQuery query){
        query.addScalar("opinionTypeId", new LongType());
        query.addScalar("opinionName", new StringType());
        query.addScalar("opinionCode", new StringType());
        query.addScalar("status", new IntegerType());
        query.setResultTransformer(Transformers.aliasToBean(WoOpinionTypeDTO.class));
        return query;
    }
}
