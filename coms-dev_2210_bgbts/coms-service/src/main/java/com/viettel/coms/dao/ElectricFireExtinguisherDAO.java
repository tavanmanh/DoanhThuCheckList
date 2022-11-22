package com.viettel.coms.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.viettel.coms.bo.ElectricFireExtinguisherBO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@Repository("electricFireExtinguisherDAO")
public class ElectricFireExtinguisherDAO extends BaseFWDAOImpl<ElectricFireExtinguisherBO, Long>{

	public ElectricFireExtinguisherDAO() {
        this.model = new ElectricFireExtinguisherBO();
    }

    public ElectricFireExtinguisherDAO(Session session) {
        this.session = session;
    }
}