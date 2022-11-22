package com.viettel.coms.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.viettel.coms.bo.ElectricNotificationFilterDustBO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@Repository("electricNotificationFilterDustDAO")
public class ElectricNotificationFilterDustDAO extends BaseFWDAOImpl<ElectricNotificationFilterDustBO, Long>{

	public ElectricNotificationFilterDustDAO() {
        this.model = new ElectricNotificationFilterDustBO();
    }

    public ElectricNotificationFilterDustDAO(Session session) {
        this.session = session;
    }
}
