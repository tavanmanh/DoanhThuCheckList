package com.viettel.coms.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.viettel.coms.bo.CabinetsSourceACBO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@Repository("cabinetsSourceACDAO")
@Transactional
public class CabinetsSourceACDAO extends BaseFWDAOImpl<CabinetsSourceACBO, Long>{

	public CabinetsSourceACDAO() {
        this.model = new CabinetsSourceACBO();
    }

    public CabinetsSourceACDAO(Session session) {
        this.session = session;
    }
}
