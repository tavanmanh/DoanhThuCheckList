package com.viettel.coms.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.viettel.coms.bo.ElectricLightningCutFilterBO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@Repository("electricLightningCutFilterDAO")
public class ElectricLightningCutFilterDAO extends BaseFWDAOImpl<ElectricLightningCutFilterBO, Long>{

	public ElectricLightningCutFilterDAO() {
        this.model = new ElectricLightningCutFilterBO();
    }

    public ElectricLightningCutFilterDAO(Session session) {
        this.session = session;
    }
}