package com.viettel.coms.business;
 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.viettel.coms.bo.RevokeCashMonthPlanBO;
import com.viettel.coms.dao.RevokeCashMonthPlanDAO;
import com.viettel.coms.dto.RevokeCashMonthPlanDTO;
import com.viettel.service.base.business.BaseFWBusinessImpl;


@Service("revokeCashMonthPlanBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RevokeCashMonthPlanBusinessImpl extends BaseFWBusinessImpl<RevokeCashMonthPlanDAO,RevokeCashMonthPlanDTO, RevokeCashMonthPlanBO> implements RevokeCashMonthPlanBusiness {

    @Autowired
    private RevokeCashMonthPlanDAO revokeCashMonthPlanDAO;
     
    public RevokeCashMonthPlanBusinessImpl() {
        tModel = new RevokeCashMonthPlanBO();
        tDAO = revokeCashMonthPlanDAO;
    }

    @Override
    public RevokeCashMonthPlanDAO gettDAO() {
        return revokeCashMonthPlanDAO;
    }

}
