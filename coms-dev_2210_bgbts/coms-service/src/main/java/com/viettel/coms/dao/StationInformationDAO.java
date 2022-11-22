package com.viettel.coms.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.viettel.coms.bo.StationInformationBO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@Repository("stationInformationDAO")
public class StationInformationDAO extends BaseFWDAOImpl<StationInformationBO, Long>{

	public StationInformationDAO() {
        this.model = new StationInformationBO();
    }

    public StationInformationDAO(Session session) {
        this.session = session;
    }
}