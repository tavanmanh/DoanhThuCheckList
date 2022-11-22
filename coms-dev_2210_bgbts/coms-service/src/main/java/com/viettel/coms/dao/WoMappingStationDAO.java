package com.viettel.coms.dao;

import com.viettel.coms.bo.WoMappingStationBO;
import com.viettel.coms.dto.WoMappingStationDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("woMappingStationDAO")
@Transactional
public class WoMappingStationDAO extends BaseFWDAOImpl<WoMappingStationBO, Long> {
    public WoMappingStationDAO() {
        this.model = new WoMappingStationBO();
    }

    public WoMappingStationDAO(Session session) {
        this.session = session;
    }

    public void removeWoMappingStation(Long woId) {
        StringBuilder sql = new StringBuilder("DELETE WO_MAPPING_STATION where WO_ID =:woId ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setParameter("woId", woId);
        query.executeUpdate();
    }

    public List<WoMappingStationDTO> getStationsOfWo(Long woId) {
        String sql = "select\n" +
                "    wms.id\n" +
                "    , wms.WO_ID woId\n" +
                "    , wms.CAT_STATION_ID catStationId\n" +
                "    , wms.status\n" +
                "    , wms.reason\n" +
                "    , cs.CODE\n" +
                "    , cs.ADDRESS\n" +
                "from WO_MAPPING_STATION wms\n" +
                "left join CAT_STATION cs ON wms.CAT_STATION_ID = cs.CAT_STATION_ID\n" +
                "where wms.wo_id = :woId";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("woId", woId);

        query.addScalar("id", new LongType());
        query.addScalar("woId", new LongType());
        query.addScalar("catStationId", new LongType());
        query.addScalar("status", new LongType());
        query.addScalar("reason", new StringType());
        query.addScalar("code", new StringType());
        query.addScalar("address", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(WoMappingStationDTO.class));

        return query.list();
    }
}
