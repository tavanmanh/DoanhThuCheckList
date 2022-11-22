package com.viettel.coms.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.viettel.coms.bo.ElectricATSBO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@Repository("electricATSDAO")
public class ElectricATSDAO extends BaseFWDAOImpl<ElectricATSBO, Long>{

	public ElectricATSDAO() {
        this.model = new ElectricATSBO();
    }

    public ElectricATSDAO(Session session) {
        this.session = session;
    }
}