package com.viettel.coms.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.viettel.coms.bo.ElectricWarningSystemBO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@Repository("electricWarningSystemDAO")
public class ElectricWarningSystemDAO extends BaseFWDAOImpl<ElectricWarningSystemBO, Long>{

	public ElectricWarningSystemDAO() {
        this.model = new ElectricWarningSystemBO();
    }

    public ElectricWarningSystemDAO(Session session) {
        this.session = session;
    }
}