package com.viettel.coms.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.viettel.coms.bo.WoMappingWorkItemHtctBO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@Repository("woMappingWorkItemHtctDAO")
public class WoMappingWorkItemHtctDAO extends BaseFWDAOImpl<WoMappingWorkItemHtctBO, Long>{

	public WoMappingWorkItemHtctDAO() {
        this.model = new WoMappingWorkItemHtctBO();
    }

    public WoMappingWorkItemHtctDAO(Session session) {
        this.session = session;
    }

}
