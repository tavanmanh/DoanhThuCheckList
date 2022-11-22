package com.viettel.coms.business;

import com.viettel.coms.dao.WoDashboardDAO;
import com.viettel.coms.dto.WoDTO;
import com.viettel.coms.dto.WoDashboardDTO;
import com.viettel.coms.dto.WoDashboardRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service("woDashboardBusinessImpl")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class WoDashboardBusinessImpl implements WoDashboardBusiness {

    @Autowired
    WoDashboardDAO woDashboardDAO;

    @Override
    public WoDashboardDTO getDataDashboardExecuteWo(long sysUserId, String fromDate, String toDate) {
        WoDashboardDTO woDashboardDTOInfo = woDashboardDAO.getDataDashboardExecuteWo(sysUserId, fromDate, toDate);
        Assert.notNull(woDashboardDTOInfo, "There is no WoImplementationInfo matching information required");
        return woDashboardDTOInfo;
    }

    @Override
    public WoDashboardDTO getDataDashboardWoMngt(long sysUserId, String fromDate, String toDate) {
        WoDashboardDTO woDashboardDTO = woDashboardDAO.getDataDashboardWoMngt(sysUserId, fromDate, toDate);
        Assert.notNull(woDashboardDTO, "There is no Dashboard matching information required");
        return woDashboardDTO;
    }

    @Override
    public List<WoDTO> detailTotalWoDasboard(WoDashboardRequest request, String fromDate, String toDate, String type) {
        List<WoDTO> woDTOList = woDashboardDAO.detailTotalWoDasboard(request, fromDate, toDate, type);
        Assert.notNull(woDTOList, "There is no detailed record");
        return woDTOList;
    }
}
