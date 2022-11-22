package com.viettel.coms.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.viettel.coms.bo.ElectricRectifierBO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@Repository("electricRectifierDAO")
public class ElectricRectifierDAO extends BaseFWDAOImpl<ElectricRectifierBO, Long>{

	public ElectricRectifierDAO() {
        this.model = new ElectricRectifierBO();
    }

    public ElectricRectifierDAO(Session session) {
        this.session = session;
    }
}