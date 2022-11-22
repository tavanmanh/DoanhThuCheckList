package com.viettel.coms.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.viettel.coms.bo.CatWorkItemTypeBO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@Repository("catWorkItemTypeDAO")
@Transactional
public class CatWorkItemTypeDAO extends BaseFWDAOImpl<CatWorkItemTypeBO, Long>{

	public CatWorkItemTypeDAO() {
        this.model = new CatWorkItemTypeBO();
    }

    public CatWorkItemTypeDAO(Session session) {
        this.session = session;
    }
}
