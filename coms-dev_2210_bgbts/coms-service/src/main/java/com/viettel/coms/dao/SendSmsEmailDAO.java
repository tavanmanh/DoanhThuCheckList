package com.viettel.coms.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.viettel.coms.bo.ConstructionBO;
import com.viettel.coms.bo.SendSmsEmailBO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@EnableTransactionManagement
@Transactional
@Repository("sendSmsEmailDAO")
public class SendSmsEmailDAO extends BaseFWDAOImpl<SendSmsEmailBO, Long>{

	public SendSmsEmailDAO() {
        this.model = new SendSmsEmailBO();
    }

    public SendSmsEmailDAO(Session session) {
        this.session = session;
    }
}
