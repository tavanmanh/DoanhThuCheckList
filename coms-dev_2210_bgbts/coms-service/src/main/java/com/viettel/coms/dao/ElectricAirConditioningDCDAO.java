package com.viettel.coms.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.viettel.coms.bo.ElectricAirConditioningDCBO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@Repository("electricAirConditioningDCDAO")
public class ElectricAirConditioningDCDAO extends BaseFWDAOImpl<ElectricAirConditioningDCBO, Long>{

	public ElectricAirConditioningDCDAO() {
        this.model = new ElectricAirConditioningDCBO();
    }

    public ElectricAirConditioningDCDAO(Session session) {
        this.session = session;
    }
}