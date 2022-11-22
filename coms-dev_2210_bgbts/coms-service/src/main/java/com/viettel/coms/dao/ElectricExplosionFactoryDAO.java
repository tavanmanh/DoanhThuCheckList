package com.viettel.coms.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.viettel.coms.bo.ElectricExplosionFactoryBO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@Repository("electricExplosionFactoryDAO")
public class ElectricExplosionFactoryDAO extends BaseFWDAOImpl<ElectricExplosionFactoryBO, Long>{

	public ElectricExplosionFactoryDAO() {
        this.model = new ElectricExplosionFactoryBO();
    }

    public ElectricExplosionFactoryDAO(Session session) {
        this.session = session;
    }
}