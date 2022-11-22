package com.viettel.coms.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.viettel.coms.bo.BatteryBO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@Repository("batteryDAO")
public class BatteryDAO extends BaseFWDAOImpl<BatteryBO, Long>{

	public BatteryDAO() {
        this.model = new BatteryBO();
    }

    public BatteryDAO(Session session) {
        this.session = session;
    }
}