package com.viettel.erp.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.viettel.erp.bo.ConstrConstructionsBO;
import com.viettel.service.base.dao.BaseFWDAOImpl;

@EnableTransactionManagement
@Transactional
@Repository("tCTCTSettlementDebtDAO")
public class TCTCTSettlementDebtDAO extends BaseFWDAOImpl<ConstrConstructionsBO, Long>{

}
