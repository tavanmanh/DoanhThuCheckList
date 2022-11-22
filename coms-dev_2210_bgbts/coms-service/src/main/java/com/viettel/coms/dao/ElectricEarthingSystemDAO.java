package com.viettel.coms.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.viettel.coms.bo.ElectricEarthingSytemBO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@Repository("electricEarthingSystemDAO")
public class ElectricEarthingSystemDAO extends BaseFWDAOImpl<ElectricEarthingSytemBO, Long>{

	public ElectricEarthingSystemDAO() {
        this.model = new ElectricEarthingSytemBO();
    }

    public ElectricEarthingSystemDAO(Session session) {
        this.session = session;
    }
}