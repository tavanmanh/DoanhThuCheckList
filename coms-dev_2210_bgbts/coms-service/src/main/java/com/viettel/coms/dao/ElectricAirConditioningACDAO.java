package com.viettel.coms.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.viettel.coms.bo.ElectricAirConditioningACBO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@Repository("electricAirConditioningACDAO")
public class ElectricAirConditioningACDAO extends BaseFWDAOImpl<ElectricAirConditioningACBO, Long>{

	public ElectricAirConditioningACDAO() {
        this.model = new ElectricAirConditioningACBO();
    }

    public ElectricAirConditioningACDAO(Session session) {
        this.session = session;
    }
}
