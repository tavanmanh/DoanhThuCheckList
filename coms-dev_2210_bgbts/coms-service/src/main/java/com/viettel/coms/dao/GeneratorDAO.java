package com.viettel.coms.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.viettel.coms.bo.GeneratorBO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@Repository("generatorDAO")
public class GeneratorDAO extends BaseFWDAOImpl<GeneratorBO, Long>{

	public GeneratorDAO() {
        this.model = new GeneratorBO();
    }

    public GeneratorDAO(Session session) {
        this.session = session;
    }
}