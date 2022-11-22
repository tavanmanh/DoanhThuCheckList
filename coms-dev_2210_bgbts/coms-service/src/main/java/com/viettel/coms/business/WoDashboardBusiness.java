package com.viettel.coms.business;

import com.viettel.coms.dto.WoDTO;
import com.viettel.coms.dto.WoDashboardDTO;
import com.viettel.coms.dto.WoDashboardRequest;

import java.util.List;

public interface WoDashboardBusiness {

    WoDashboardDTO getDataDashboardExecuteWo(long sysUserId, String fromDate, String toDate);

    WoDashboardDTO getDataDashboardWoMngt(long sysUserId, String fromDate, String toDate);

    List<WoDTO> detailTotalWoDasboard(WoDashboardRequest request, String fromDate, String toDate, String type);
}
