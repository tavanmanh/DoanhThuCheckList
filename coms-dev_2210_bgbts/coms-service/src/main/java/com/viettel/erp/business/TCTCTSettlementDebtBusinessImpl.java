package com.viettel.erp.business;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.viettel.erp.bo.ConstrConstructionsBO;
import com.viettel.erp.dao.TCTCTSettlementDebtDAO;
import com.viettel.erp.dto.TCTCTSettlementDebtDTO;
import com.viettel.service.base.business.BaseFWBusinessImpl;

@Service("tCTCTSettlementDebtBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TCTCTSettlementDebtBusinessImpl extends BaseFWBusinessImpl<TCTCTSettlementDebtDAO, TCTCTSettlementDebtDTO, ConstrConstructionsBO>
implements TCTCTSettlementDebtBusiness{

	
}
