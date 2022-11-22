package com.viettel.coms.dao;

import com.viettel.coms.bo.WoClassQoutaBO;
import com.viettel.coms.dto.WoClassQoutaDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class WoClassQoutaDAO extends BaseFWDAOImpl<WoClassQoutaBO, Long> {
    public List<WoClassQoutaDTO> getByClassIdAndChecklistName(Long classId, String checklistName) {
        StringBuilder sql = new StringBuilder(" select\n" +
                "    id\n" +
                "    , CLASS_ID classId\n" +
                "    , CHECKLIST_NAME checklistName\n" +
                "    , QOUTA_VALUE qoutaValue\n" +
                "from WO_CLASS_QOUTA\n" +
                "where status = 1\n" +
                "and CLASS_ID = :classId\n" +
                "and CHECKLIST_NAME = :checklistName ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("classId", classId);
        query.setParameter("checklistName", checklistName);

        query.addScalar("classId", new LongType());
        query.addScalar("checklistName", new StringType());
        query.addScalar("qoutaValue", new LongType());
        query.setResultTransformer(Transformers.aliasToBean(WoClassQoutaDTO.class));

        return query.list();
    }
}
