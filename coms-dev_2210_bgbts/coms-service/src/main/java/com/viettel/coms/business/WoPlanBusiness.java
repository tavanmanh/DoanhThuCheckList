package com.viettel.coms.business;

import com.viettel.coms.dto.WoDTO;
import com.viettel.coms.dto.WoPlanDTO;

import java.util.List;

public interface WoPlanBusiness {
    List<WoPlanDTO> list(long ftId);

    void insert(WoPlanDTO dto, List<WoDTO> lstWosOfPlan) throws Exception;

    void update(WoPlanDTO dto, List<WoDTO> lstWosOfPlan) throws Exception;

    boolean deletePlan(WoPlanDTO woPlanDTO) throws Exception;

    List<WoDTO> getListWosByPlanId(WoPlanDTO woPlanDTO);

    List<WoPlanDTO> getDataForPlanChart(Long loginUserId);
}
