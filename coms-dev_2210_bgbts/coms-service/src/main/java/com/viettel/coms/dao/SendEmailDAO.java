package com.viettel.coms.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.viettel.coms.bo.SendEmailBO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@EnableTransactionManagement
@Transactional
@Repository("sendEmailDAO")
public class SendEmailDAO extends BaseFWDAOImpl<SendEmailBO, Long>{

	public SendEmailDAO() {
        this.model = new SendEmailBO();
    }

    public SendEmailDAO(Session session) {
        this.session = session;
    }
}
