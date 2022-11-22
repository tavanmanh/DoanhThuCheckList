package com.viettel.coms.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.viettel.coms.bo.AttachElectronicStationBO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@Repository("attachElectronicStationDAO")
@Transactional
public class AttachElectronicStationDAO extends BaseFWDAOImpl<AttachElectronicStationBO, Long>{

	public AttachElectronicStationDAO() {
        this.model = new AttachElectronicStationBO();
    }

    public AttachElectronicStationDAO(Session session) {
        this.session = session;
    }
}
