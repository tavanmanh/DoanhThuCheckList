package com.viettel.coms.business;

import com.viettel.coms.dto.WoDTO;
import com.viettel.coms.dto.WoMappingChecklistDTO;

import java.util.List;

public interface WoMappingChecklistBusiness {
    public String update(List<WoMappingChecklistDTO> lstWoMappingChecklists);
}
