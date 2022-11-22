package com.viettel.coms.dao;

import com.viettel.coms.bo.WoTrMappingStationBO;
import com.viettel.coms.dto.WoTrMappingStationDTO;
import com.viettel.service.base.dao.BaseFWDAOImpl;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("woTrMappingStationDAO")
@Transactional
public class WoTrMappingStationDAO extends BaseFWDAOImpl<WoTrMappingStationBO, Long> {
    public WoTrMappingStationDAO() {
        this.model = new WoTrMappingStationBO();
    }

    public WoTrMappingStationDAO(Session session) {
        this.session = session;
    }

    public List<WoTrMappingStationDTO> getStationsOfTr(Long trId) {
        String sql = "select\n" +
                "    TR_ID trId\n" +
                "    , SYS_GROUP_ID sysGroupId\n" +
                "    , listagg(CAT_STATION_ID,',') within group( order by CAT_STATION_ID ) lstStations\n" +
                "    , listagg(code,',') within group( order by code ) lstStationCodes\n" +
                "from (\n" +
                "    select\n" +
                "        wtms.TR_ID\n" +
                "        , wtms.SYS_GROUP_ID\n" +
                "        , wtms.CAT_STATION_ID\n" +
                "        , cs.code\n" +
                "    from WO_TR_MAPPING_STATION wtms\n" +
                "    left join cat_station cs on wtms.CAT_STATION_ID = cs.CAT_STATION_ID\n" +
                "    where wtms.status!=0 AND wtms.tr_id = :trId\n" +
                ") \n" +
                "GROUP BY TR_ID, SYS_GROUP_ID";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("trId", trId);

        query.addScalar("trId", new LongType());
        query.addScalar("sysGroupId", new LongType());
        query.addScalar("lstStations", new StringType());
        query.addScalar("lstStationCodes", new StringType());
        query.setResultTransformer(Transformers.aliasToBean(WoTrMappingStationDTO.class));

        return query.list();
    }
}
