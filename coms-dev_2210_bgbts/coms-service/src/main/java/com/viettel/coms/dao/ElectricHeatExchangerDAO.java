package com.viettel.coms.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.viettel.coms.bo.ElectricHeatExchangerBO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@Repository("electricHeatExchangerDAO")
public class ElectricHeatExchangerDAO extends BaseFWDAOImpl<ElectricHeatExchangerBO, Long>{

	public ElectricHeatExchangerDAO() {
        this.model = new ElectricHeatExchangerBO();
    }

    public ElectricHeatExchangerDAO(Session session) {
        this.session = session;
    }
}
